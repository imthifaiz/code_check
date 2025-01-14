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
import com.track.db.util.CustUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadInboundOrderSheet;
import com.track.tran.WmsUploadLoanOrderSheet;
import com.track.tran.WmsUploadOutboundOrderSheet;
import com.track.tran.WmsUploadItemSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.tran.WmsUploadInboundProdRemarksSheet;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class OrderImportServlet
 */
public class OrderImportServlet extends HttpServlet {
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

	public OrderImportServlet() {
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
			}else if (action.equalsIgnoreCase("importCountSheetLoan")) {
					onImportCountSheetLoan(request, response);
			} else if (action.equalsIgnoreCase("confirmCountSheet")) {
				onConfirmCountSheet(request, response);
			}else if (action.equalsIgnoreCase("confirmCountSheetLoan")) {
				onConfirmCountSheetLoan(request, response);
			} else if (action.equalsIgnoreCase("downloadInboundTemplate")) {
				onDownloadInboundTemplate(request, response);
			}else if (action.equalsIgnoreCase("downloadRentalTemplate")) {
				onDownloadRentalTemplate(request, response);
			}	else if (action.equalsIgnoreCase("importInboundProdRemarksCountSheet")) {
				onImportInboundProdRemarksCountSheet(request, response);
			} 
			else if (action.equalsIgnoreCase("confirmInboundProdRemarksCountSheet")) {
				onConfirmInboundProdRemarksCountSheet(request, response);
			} 
			else if (action.equalsIgnoreCase("downloadInboundProdRemarksTemplate")) {
				onDownloadInboundProdRemarksTemplate(request, response);
			}else if (action.equalsIgnoreCase("importCountSheetShopify")) {
				onImportCountSheetShopify(request, response);
			}else if (action.equalsIgnoreCase("confirmCountSheetShopify")) {
				onConfirmCountSheetShopify(request, response);
			}else if (action.equalsIgnoreCase("importCountSheetShopee")) {
				onImportCountSheetShopee(request, response);
			}else if (action.equalsIgnoreCase("confirmCountSheetShopee")) {
				onConfirmCountSheetShopee(request, response);
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

			request.getSession().setAttribute("IMP_INBOUNDRESULT", null);
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
			//Modified by Bruhan for base CUrrency
			String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
			String NOOFORDER = (String) request.getSession().getAttribute("NOOFORDER");
			String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
			alRes = util.downloadInboundSheetData(PLANT, StrFileName,
					StrSheetName,baseCurrency,NOOFORDER,region);

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
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
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

			// throw e;
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("INBOUNDRESULT", result);
		request.getSession().setAttribute("IMP_INBOUNDRESULT", alRes);
		//       
		response
				.sendRedirect("../purchaseorder/importpurchaseorder?ImportFile="
						
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	private void onDownloadInboundTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInBoundTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"PurchaseOrderData.xls\"");
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
	
	private void onDownloadRentalTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadRentalTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"RentalOrderData.xls\"");
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
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_INBOUNDRESULT");
			
			Map linemap = new HashMap();
			
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
						
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.IN_PONO, (String) lineArr.get(IConstants.IN_PONO));
				        linemap.put(IConstants.IN_POLNNO, (String) lineArr.get(IConstants.IN_POLNNO));
				        linemap.put(IConstants.TRANSPORTID, (String) lineArr.get(IConstants.TRANSPORTID));
				        linemap.put(IConstants.IN_REF_NO, (String) lineArr.get(IConstants.IN_REF_NO));
				        linemap.put(IConstants.IN_CURRENCYID, (String) lineArr.get(IConstants.IN_CURRENCYID));
					linemap.put(IConstants.IN_ORDERTYPE, (String) lineArr.get(IConstants.IN_ORDERTYPE));
					linemap.put(IConstants.IN_COLLECTION_DATE, (String) lineArr.get(IConstants.IN_COLLECTION_DATE));
					linemap.put(IConstants.IN_COLLECTION_TIME, (String) lineArr.get(IConstants.IN_COLLECTION_TIME));
					linemap.put(IConstants.IN_REMARK1, (String) lineArr.get(IConstants.IN_REMARK1));
					linemap.put(IConstants.IN_REMARK3, (String) lineArr.get(IConstants.IN_REMARK3));
					
					linemap.put(IConstants.IN_INBOUND_GST, (String) lineArr.get(IConstants.IN_INBOUND_GST));
					linemap.put(IConstants.IN_CUST_CODE, (String) lineArr.get(IConstants.IN_CUST_CODE));
					
					CustUtil custUtil = new CustUtil();
					ArrayList supplier_detail = custUtil.getVendorDetails((String) lineArr.get(IConstants.IN_CUST_CODE), PLANT);
					String taxtret = (String)supplier_detail.get(30);
					
					linemap.put(IConstants.IN_CUST_NAME, supplier_detail.get(1));
					linemap.put(IConstants.IN_PERSON_IN_CHARGE, supplier_detail.get(8));
				    linemap.put(IConstants.IN_REMARK2, supplier_detail.get(14));
					linemap.put(IConstants.IN_ITEM, (String) lineArr.get(IConstants.IN_ITEM));
					linemap.put(IConstants.IN_MANUFACTURER, (String) lineArr.get(IConstants.IN_MANUFACTURER));
					linemap.put(IConstants.IN_QTYOR, (String) lineArr.get(IConstants.IN_QTYOR));
					linemap.put(IConstants.IN_UNITMO, (String) lineArr.get(IConstants.IN_UNITMO));
					linemap.put(IConstants.IN_UNITCOST, (String) lineArr.get(IConstants.IN_UNITCOST));
					
					
					linemap.put(IConstants.LOANHDR_ADDRESS, supplier_detail.get(2));
					linemap.put(IConstants.LOANHDR_ADDRESS2, supplier_detail.get(3));
					linemap.put(IConstants.LOANHDR_ADDRESS3, supplier_detail.get(4));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(10));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(11));
					
					
					String itemDesc = "";
					String itemDetDesc = "";
					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT,(String) lineArr.get(IConstants.IN_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT,(String) lineArr.get(IConstants.IN_ITEM));
					linemap.put(IConstants.IN_ITEM_DES,new StrUtils().InsertQuotes( itemDesc));
					linemap.put(IConstants.IN_ITEM_DET_DES,new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.DEL_DATE,(String) lineArr.get(IConstants.DEL_DATE));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.payment_terms, lineArr.get(IConstants.payment_terms));
					linemap.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.POHDR_DELIVERYDATEFORMAT));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
					if(supplier_detail.size()>0) {
						linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					
					linemap.put(IConstants.SHIP_CONTACTNAME, (String)supplier_detail.get(44));
					linemap.put(IConstants.SHIP_DESGINATION, (String)supplier_detail.get(45));
					linemap.put(IConstants.SHIP_WORKPHONE, (String)supplier_detail.get(46));
					linemap.put(IConstants.SHIP_HPNO, (String)supplier_detail.get(47));
					linemap.put(IConstants.SHIP_EMAIL, (String)supplier_detail.get(48));
					linemap.put(IConstants.SHIP_COUNTRY, (String)supplier_detail.get(49));
					linemap.put(IConstants.SHIP_ADDR1, (String)supplier_detail.get(50));
					linemap.put(IConstants.SHIP_ADDR2, (String)supplier_detail.get(51));
					linemap.put(IConstants.SHIP_ADDR3, (String)supplier_detail.get(52));
					linemap.put(IConstants.SHIP_ADDR4, (String)supplier_detail.get(53));
					linemap.put(IConstants.SHIP_STATE, (String)supplier_detail.get(54));
					linemap.put(IConstants.SHIP_ZIP, (String)supplier_detail.get(55));
					
					}else {
						linemap.put(IConstants.SHIPPINGCUSTOMER, "");
					}
					linemap.put(IConstants.LOCALEXPENSES, (String) lineArr.get(IConstants.LOCALEXPENSES));
					linemap.put(IConstants.TAXTREATMENT, taxtret);
					linemap.put(IConstants.PURCHASE_LOCATION, (String) lineArr.get(IConstants.PURCHASE_LOCATION));
					linemap.put(IConstants.POHDR_EMPNO, (String) lineArr.get(IConstants.POHDR_EMPNO));
					linemap.put(IConstants.REVERSECHARGE, (String) lineArr.get(IConstants.REVERSECHARGE));
					linemap.put(IConstants.GOODSIMPORT, (String) lineArr.get(IConstants.GOODSIMPORT));
					linemap.put(IConstants.GOODSIMPORT, (String) lineArr.get(IConstants.GOODSIMPORT));
					linemap.put("CURRENCYUSEQT", (String) lineArr.get("CURRENCYUSEQT"));
					linemap.put("ISDISCOUNTTAX", (String) lineArr.get("ISDISCOUNTTAX"));
					linemap.put("ISSHIPPINGTAX", (String) lineArr.get("ISSHIPPINGTAX"));
					linemap.put("ISTAXINCLUSIVE", (String) lineArr.get("ISTAXINCLUSIVE"));
					linemap.put("ORDERDISCOUNTTYPE", (String) lineArr.get("ORDERDISCOUNTTYPE"));
					linemap.put("TAXID", (String) lineArr.get("TAXID"));
					linemap.put("PROJECTID", (String) lineArr.get("PROJECTID"));
					linemap.put("SHIPPINGID", (String) lineArr.get("SHIPPINGID"));
					linemap.put("ADJUSTMENT", (String) lineArr.get("ADJUSTMENT"));
					linemap.put("PRODUCTDISCOUNT", (String)lineArr.get("PRODUCTDISCOUNT"));
					linemap.put("PRODUCTDISCOUNT_TYPE", (String)lineArr.get("PRODUCTDISCOUNT_TYPE"));
		            linemap.put("ACCOUNT_NAME", (String)lineArr.get("ACCOUNT_NAME"));
		            linemap.put("PRODUCTDELIVERYDATE", (String)lineArr.get("PRODUCTDELIVERYDATE"));
		            linemap.put("Tax", (String)lineArr.get("Tax"));
		            linemap.put("POESTNO","");
					
					flag = process_Wms_CountSheet(linemap);
					
				}else{
					break;
				}
			}
		
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_INBOUNDRESULT");
				flag = true;
				result = "<font color=\"green\">Purchase Order Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Orders </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("INBOUNDRESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("INBOUNDRESULT", result);
		response.sendRedirect("../purchaseorder/importpurchaseorder?");
		

	}

	private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadInboundOrderSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadInboundOrderSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadInboundOrderSheet()");
		return flag;
	}
	
	private boolean process_Wms_CountSheetLoan(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadLoanOrderSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadLoanOrderSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadLoanOrderSheet()");
		return flag;
	}
	
	private boolean process_Wms_CountSheet_shopify(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadOutboundOrderSheet()");
		boolean flag = false;
		try{
		WmsTran tran = new WmsUploadOutboundOrderSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadOutboundOrderSheet()");
		}catch(Exception e){
			throw e;
		}
		return flag;
	}
	
	private void onDownloadInboundProdRemarksTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInboundProdRemarksTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"PurchaseProductRemarks.xls\"");
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
	
	private void onImportInboundProdRemarksCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

		try {

			try {

				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}

			request.getSession().setAttribute("IMP_INBOUNDPRODREMARKSRESULT", null);
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
			
			alRes = util.downloadInboundProdRemarksSheetData(PLANT, StrFileName,StrSheetName);

			if (alRes.size() > 0) {
				mlogger.info("Data Imported Successfully");
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}

		} catch (Exception e) {

			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("INBOUNDPRODREMARKSRESULT", result);
		request.getSession().setAttribute("IMP_INBOUNDPRODREMARKSRESULT", alRes);
		//       
		response
				.sendRedirect("../purchaseorder/importpurchaseproductremarks?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	@SuppressWarnings("unchecked")
	private void onConfirmInboundProdRemarksCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_INBOUNDPRODREMARKSRESULT");

			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.IN_PONO, (String) lineArr.get(IConstants.IN_PONO));
				    linemap.put(IConstants.IN_POLNNO, (String) lineArr.get(IConstants.IN_POLNNO));
				    linemap.put(IConstants.IN_ITEM, (String) lineArr.get(IConstants.IN_ITEM));
				    linemap.put("REMARKS", strUtils.InsertQuotes((String) lineArr.get("REMARKS")));
					
	
					flag = process_Wms_InboundProdRemarksCountSheet(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_INBOUNDPRODREMARKSRESULT");
				flag = true;
				result = "<font color=\"green\">Purchase Product Remarks Uploaded Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("INBOUNDPRODREMARKSRESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("INBOUNDPRODREMARKSRESULT", result);
		response.sendRedirect("../purchaseorder/importpurchaseproductremarks?");

	}
	
	private void onConfirmCountSheetLoan(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_LOANRESULT");
			
			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.LOANHDR_ORDNO, (String) lineArr.get(IConstants.LOANHDR_ORDNO));
				    linemap.put(IConstants.LOANDET_ORDLNNO, (String) lineArr.get(IConstants.LOANDET_ORDLNNO));
				    linemap.put(IConstants.LOANHDR_JOB_NUM, (String) lineArr.get(IConstants.LOANHDR_JOB_NUM));
				 	linemap.put(IConstants.ORDERTYPE, (String) lineArr.get(IConstants.ORDERTYPE));
					linemap.put(IConstants.LOANHDR_COL_DATE, (String) lineArr.get(IConstants.LOANHDR_COL_DATE));
					linemap.put(IConstants.LOANHDR_COL_TIME, (String) lineArr.get(IConstants.LOANHDR_COL_TIME));
					linemap.put(IConstants.LOANHDR_REMARK1, strUtils.InsertQuotes((String) lineArr.get(IConstants.LOANHDR_REMARK1)));
					linemap.put(IConstants.LOANHDR_REMARK2, strUtils.InsertQuotes((String) lineArr.get(IConstants.LOANHDR_REMARK2)));
					linemap.put(IConstants.LOANHDR_GST, (String) lineArr.get(IConstants.LOANHDR_GST));
					
					linemap.put(IConstants.LOANHDR_CUST_CODE, (String) lineArr.get(IConstants.LOANHDR_CUST_CODE));
					
					CustUtil custUtil = new CustUtil();
					ArrayList supplier_detail = custUtil.getCustomerDetailsLoan((String) lineArr.get(IConstants.LOANHDR_CUST_CODE), PLANT);
					
					linemap.put(IConstants.LOANHDR_CUST_NAME, strUtils.InsertQuotes((String)supplier_detail.get(1)));
					linemap.put(IConstants.LOANHDR_PERSON_INCHARGE, strUtils.InsertQuotes((String)supplier_detail.get(9)));
                    //linemap.put(IConstants.LOANHDR_REMARK2,  strUtils.InsertQuotes((String)supplier_detail.get(15)));
					linemap.put(IConstants.LOANDET_ITEM, (String) lineArr.get(IConstants.LOANDET_ITEM));
					linemap.put(IConstants.LOANDET_QTYOR, (String) lineArr.get(IConstants.LOANDET_QTYOR));
					linemap.put(IConstants.UNITMO, (String) lineArr.get(IConstants.UNITMO));
                    
					linemap.put(IConstants.LOANDET_RENTALPRICE, (String) lineArr.get(IConstants.LOANDET_RENTALPRICE));
				    linemap.put(IConstants.CURRENCYID, (String) lineArr.get(IConstants.CURRENCYID));
				    linemap.put(IConstants.LOANHDR_EMPNO, strUtils.InsertQuotes((String) lineArr.get(IConstants.LOANHDR_EMPNO)));
					linemap.put(IConstants.DELIVERYDATE, strUtils.InsertQuotes((String) lineArr.get(IConstants.DELIVERYDATE)));
					String itemDesc = "";
					String itemDetDesc = "";
					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.LOANDET_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.LOANDET_ITEM));
					linemap.put(IConstants.LOANDET_ITEMDESC,  new StrUtils().InsertQuotes( itemDesc));
					//linemap.put(IConstants.OUT_ITEM_DET_DES, new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.LOANHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.LOANHDR_DELIVERYDATEFORMAT));
					linemap.put(IConstants.LOANHDR_EXPIRYDATEFORMAT, (String) lineArr.get(IConstants.LOANHDR_EXPIRYDATEFORMAT));
					linemap.put(IConstants.LOC, (String) lineArr.get(IConstants.LOC));
					linemap.put(IConstants.LOC1, (String) lineArr.get(IConstants.LOC1));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
				    if(supplier_detail.size()>0)
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					else
						linemap.put(IConstants.SHIPPINGCUSTOMER,""); 
	
					flag = process_Wms_CountSheetLoan(linemap);
					
				}else{
					break;
				}
			}
			
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_LOANRESULT");	
				flag = true;
				result = "<font color=\"green\">Rental Order's Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("LOANRESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		request.getSession().setAttribute("LOANRESULT", null);
		request.getSession().setAttribute("LOANRESULT", result);
		response.sendRedirect("jsp/importloanorderexcelsheet.jsp?");

	}	
	
	private void onImportCountSheetLoan(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

		try {

			try {

				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}

			request.getSession().setAttribute("IMP_LOANRESULT", null);
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
			//Modified by Bruhan for base CUrrency
			String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
			alRes = util.downloadOutboundSheetDataLoan(PLANT, StrFileName,
					StrSheetName,baseCurrency);

			if (alRes.size() > 0) {
				mlogger.info("Data Imported Successfully");
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}

		} catch (Exception e) {

			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("LOANRESULT", result);
		request.getSession().setAttribute("IMP_LOANRESULT", alRes);
		//       
		response
				.sendRedirect("jsp/importloanorderexcelsheet.jsp?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	private boolean process_Wms_InboundProdRemarksCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadInboundProdRemarksSheet()");
		boolean flag = false;
		try{
		WmsTran tran = new WmsUploadInboundProdRemarksSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadInboundProdRemarksSheet()");
		}catch(Exception e){
			throw e;
		}
		return flag;
	}	
	
	private void onImportCountSheetShopify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String NOOFORDER=((String) request.getSession().getAttribute("NOOFORDER"));
		String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
		
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

		try {

			try {

				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}

			request.getSession().setAttribute("IMP_OUTBOUNDRESULT_SHPIFY", null);
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
			//Modified by Bruhan for base CUrrency
			String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
			alRes = util.downloadOutboundSheetDataShopify(PLANT, StrFileName,
					StrSheetName,baseCurrency,NOOFORDER,_login_user);

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
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
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

		request.getSession().setAttribute("OUTBOUNDRESULT_SHPIFY", result);
		request.getSession().setAttribute("IMP_OUTBOUNDRESULT_SHPIFY", alRes);
		//       
		response
				.sendRedirect("jsp/importShopifySalesOrder.jsp?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	
	@SuppressWarnings("unchecked")
	private void onConfirmCountSheetShopify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDRESULT_SHPIFY");

			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.OUT_DONO, (String) lineArr.get(IConstants.OUT_DONO));
				    linemap.put(IConstants.OUT_DOLNNO, (String) lineArr.get(IConstants.OUT_DOLNNO));
				    linemap.put(IConstants.OUT_REF_NO, (String) lineArr.get(IConstants.OUT_REF_NO));
				 	linemap.put(IConstants.OUT_ORDERTYPE, (String) lineArr.get(IConstants.OUT_ORDERTYPE));
					linemap.put(IConstants.OUT_COLLECTION_DATE, (String) lineArr.get(IConstants.OUT_COLLECTION_DATE));
					linemap.put(IConstants.OUT_COLLECTION_TIME, (String) lineArr.get(IConstants.OUT_COLLECTION_TIME));
					linemap.put(IConstants.OUT_REMARK1, strUtils.InsertQuotes((String) lineArr.get(IConstants.OUT_REMARK1)));
					linemap.put(IConstants.OUT_REMARK3, strUtils.InsertQuotes((String) lineArr.get(IConstants.OUT_REMARK3)));
					linemap.put(IConstants.OUT_OUTBOUND_GST, (String) lineArr.get(IConstants.OUT_OUTBOUND_GST));
					
					linemap.put(IConstants.OUT_CUST_CODE, (String) lineArr.get(IConstants.OUT_CUST_CODE));
					
					CustUtil custUtil = new CustUtil();
					ArrayList supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.OUT_CUST_CODE), PLANT);
					String taxtret = (String)supplier_detail.get(34);
					linemap.put(IConstants.OUT_CUST_NAME, strUtils.InsertQuotes((String)supplier_detail.get(1)));
					linemap.put(IConstants.OUT_PERSON_IN_CHARGE, strUtils.InsertQuotes((String)supplier_detail.get(9)));
                    linemap.put(IConstants.OUT_REMARK2,  strUtils.InsertQuotes((String)supplier_detail.get(15)));
					linemap.put(IConstants.OUT_ITEM, (String) lineArr.get(IConstants.OUT_ITEM));
					linemap.put(IConstants.OUT_QTYOR, (String) lineArr.get(IConstants.OUT_QTYOR));
					linemap.put(IConstants.OUT_UNITMO, (String) lineArr.get(IConstants.OUT_UNITMO));
                    
					linemap.put(IConstants.OUT_UNITCOST, (String) lineArr.get(IConstants.OUT_UNITCOST));
				    linemap.put(IConstants.OUT_CURRENCYID, (String) lineArr.get(IConstants.OUT_CURRENCYID));
				    linemap.put(IConstants.DOHDR_EMPNO, strUtils.InsertQuotes((String) lineArr.get(IConstants.DOHDR_EMPNO)));
					linemap.put(IConstants.DELIVERYDATE, strUtils.InsertQuotes((String) lineArr.get(IConstants.DELIVERYDATE)));
					String itemDesc = "";
					String itemDetDesc = "";
					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.OUT_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.OUT_ITEM));
					linemap.put(IConstants.OUT_ITEM_DES,  new StrUtils().InsertQuotes( itemDesc));
					linemap.put(IConstants.OUT_ITEM_DET_DES, new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.POHDR_DELIVERYDATEFORMAT));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
				    if(supplier_detail.size()>0)
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					else
						linemap.put(IConstants.SHIPPINGCUSTOMER,""); 
				    linemap.put(IConstants.TAXTREATMENT, taxtret);
				    linemap.put(IConstants.SALES_LOCATION, (String) lineArr.get(IConstants.SALES_LOCATION));
				    
				    linemap.put("DELIVERYDATE", (String) lineArr.get("DELIVERYDATE"));
				    linemap.put("SHIPPINGID", (String) lineArr.get("SHIPPINGID"));
				    linemap.put("DISCOUNT", (String) lineArr.get("DISCOUNT"));
				    linemap.put("DISCOUNTTYPE", (String) lineArr.get("DISCOUNTTYPE"));
				    linemap.put("ADJUSTMENT", (String) lineArr.get("ADJUSTMENT"));
				    linemap.put("ISTAXINCLUSIVE", (String) lineArr.get("ISTAXINCLUSIVE"));
				    linemap.put("PROJECTID", (String) lineArr.get("PROJECTID"));
				    linemap.put("EQUIVALENTCURRENCY", (String) lineArr.get("EQUIVALENTCURRENCY"));
				    linemap.put("ORDERDISCOUNTTYPE", (String) lineArr.get("ORDERDISCOUNTTYPE"));
				    linemap.put("TAXID", (String) lineArr.get("TAXID"));
				    linemap.put("ISORDERDISCOUNTTAX", (String) lineArr.get("ISORDERDISCOUNTTAX"));
				    linemap.put("ISSHIPPINGTAX", (String) lineArr.get("ISSHIPPINGTAX"));
				    linemap.put("ISDISCOUNTTAX", (String) lineArr.get("ISDISCOUNTTAX"));
				    linemap.put("PRODUCTDELIVERYDATE", (String) lineArr.get("PRODUCTDELIVERYDATE"));
				    linemap.put("ACCOUNT", (String) lineArr.get("ACCOUNT"));
				    linemap.put("TAX", (String) lineArr.get("TAX"));
				    linemap.put("PRODUCTDISCOUNT", (String) lineArr.get("PRODUCTDISCOUNT"));
				    linemap.put("PRODUCTDISCOUNT_TYPE", (String) lineArr.get("PRODUCTDISCOUNT_TYPE"));
				    
					flag = process_Wms_CountSheet_shopify(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDRESULT_SHPIFY");
				flag = true;
				result = "<font color=\"green\">Sales Order's Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("OUTBOUNDRESULT_SHPIFY", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDRESULT", result);
		response.sendRedirect("jsp/importShopifySalesOrder.jsp?");

	}
	
	private void onImportCountSheetShopee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String NOOFORDER=((String) request.getSession().getAttribute("NOOFORDER"));
		String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
		
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		try {

			try {

				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}

			request.getSession().setAttribute("IMP_OUTBOUNDRESULT_SHOPEE", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			System.out.println("After conversion Import File  *********:"+ StrFileName);

			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
			alRes = util.downloadOutboundSheetDataShopee(PLANT, StrFileName,
					StrSheetName,baseCurrency,NOOFORDER,_login_user);

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
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
					result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" order's you can create</font>";
				}
			} else {
				throw new Exception("no data found  for given data in the excel");
			}

		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("OUTBOUNDRESULT_SHOPEE", result);
		request.getSession().setAttribute("IMP_OUTBOUNDRESULT_SHOPEE", alRes); 
		response.sendRedirect("jsp/importShopeeSalesOrder.jsp?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");
	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmCountSheetShopee(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDRESULT_SHOPEE");

			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.OUT_DONO, (String) lineArr.get(IConstants.OUT_DONO));
				    linemap.put(IConstants.OUT_DOLNNO, (String) lineArr.get(IConstants.OUT_DOLNNO));
				    linemap.put(IConstants.OUT_REF_NO, (String) lineArr.get(IConstants.OUT_REF_NO));
				 	linemap.put(IConstants.OUT_ORDERTYPE, (String) lineArr.get(IConstants.OUT_ORDERTYPE));
					linemap.put(IConstants.OUT_COLLECTION_DATE, (String) lineArr.get(IConstants.OUT_COLLECTION_DATE));
					linemap.put(IConstants.OUT_COLLECTION_TIME, (String) lineArr.get(IConstants.OUT_COLLECTION_TIME));
					linemap.put(IConstants.OUT_REMARK1, strUtils.InsertQuotes((String) lineArr.get(IConstants.OUT_REMARK1)));
					linemap.put(IConstants.OUT_REMARK3, strUtils.InsertQuotes((String) lineArr.get(IConstants.OUT_REMARK3)));
					linemap.put(IConstants.OUT_OUTBOUND_GST, (String) lineArr.get(IConstants.OUT_OUTBOUND_GST));
					
					linemap.put(IConstants.OUT_CUST_CODE, (String) lineArr.get(IConstants.OUT_CUST_CODE));
					
					CustUtil custUtil = new CustUtil();
					ArrayList supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.OUT_CUST_CODE), PLANT);
					String taxtret = (String)supplier_detail.get(34);
					linemap.put(IConstants.OUT_CUST_NAME, strUtils.InsertQuotes((String)supplier_detail.get(1)));
					linemap.put(IConstants.OUT_PERSON_IN_CHARGE, strUtils.InsertQuotes((String)supplier_detail.get(9)));
                    linemap.put(IConstants.OUT_REMARK2,  strUtils.InsertQuotes((String)supplier_detail.get(15)));
					linemap.put(IConstants.OUT_ITEM, (String) lineArr.get(IConstants.OUT_ITEM));
					linemap.put(IConstants.OUT_QTYOR, (String) lineArr.get(IConstants.OUT_QTYOR));
					linemap.put(IConstants.OUT_UNITMO, (String) lineArr.get(IConstants.OUT_UNITMO));
                    
					linemap.put(IConstants.OUT_UNITCOST, (String) lineArr.get(IConstants.OUT_UNITCOST));
				    linemap.put(IConstants.OUT_CURRENCYID, (String) lineArr.get(IConstants.OUT_CURRENCYID));
				    linemap.put(IConstants.DOHDR_EMPNO, strUtils.InsertQuotes((String) lineArr.get(IConstants.DOHDR_EMPNO)));
					linemap.put(IConstants.DELIVERYDATE, strUtils.InsertQuotes((String) lineArr.get(IConstants.DELIVERYDATE)));
					String itemDesc = "";
					String itemDetDesc = "";
					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.OUT_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.OUT_ITEM));
					linemap.put(IConstants.OUT_ITEM_DES,  new StrUtils().InsertQuotes( itemDesc));
					linemap.put(IConstants.OUT_ITEM_DET_DES, new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.POHDR_DELIVERYDATEFORMAT));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
				    if(supplier_detail.size()>0)
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					else
						linemap.put(IConstants.SHIPPINGCUSTOMER,""); 
				    linemap.put(IConstants.TAXTREATMENT, taxtret);
				    linemap.put(IConstants.SALES_LOCATION, (String) lineArr.get(IConstants.SALES_LOCATION));
				    
				    linemap.put("DELIVERYDATE", (String) lineArr.get("DELIVERYDATE"));
				    linemap.put("SHIPPINGID", (String) lineArr.get("SHIPPINGID"));
				    linemap.put("DISCOUNT", (String) lineArr.get("DISCOUNT"));
				    linemap.put("DISCOUNTTYPE", (String) lineArr.get("DISCOUNTTYPE"));
				    linemap.put("ADJUSTMENT", (String) lineArr.get("ADJUSTMENT"));
				    linemap.put("ISTAXINCLUSIVE", (String) lineArr.get("ISTAXINCLUSIVE"));
				    linemap.put("PROJECTID", (String) lineArr.get("PROJECTID"));
				    linemap.put("EQUIVALENTCURRENCY", (String) lineArr.get("EQUIVALENTCURRENCY"));
				    linemap.put("ORDERDISCOUNTTYPE", (String) lineArr.get("ORDERDISCOUNTTYPE"));
				    linemap.put("TAXID", (String) lineArr.get("TAXID"));
				    linemap.put("ISORDERDISCOUNTTAX", (String) lineArr.get("ISORDERDISCOUNTTAX"));
				    linemap.put("ISSHIPPINGTAX", (String) lineArr.get("ISSHIPPINGTAX"));
				    linemap.put("ISDISCOUNTTAX", (String) lineArr.get("ISDISCOUNTTAX"));
				    linemap.put("PRODUCTDELIVERYDATE", (String) lineArr.get("PRODUCTDELIVERYDATE"));
				    linemap.put("ACCOUNT", (String) lineArr.get("ACCOUNT"));
				    linemap.put("TAX", (String) lineArr.get("TAX"));
				    linemap.put("PRODUCTDISCOUNT", (String) lineArr.get("PRODUCTDISCOUNT"));
				    linemap.put("PRODUCTDISCOUNT_TYPE", (String) lineArr.get("PRODUCTDISCOUNT_TYPE"));
				    
					flag = process_Wms_CountSheet_shopify(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDRESULT_SHOPEE");
				flag = true;
				result = "<font color=\"green\">Sales Order's Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("OUTBOUNDRESULT_SHPEE", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDRESULT_SHOPEE", result);
		response.sendRedirect("jsp/importShopeeSalesOrder.jsp?");

	}
}
