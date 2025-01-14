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
import com.track.dao.ItemMstDAO;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadEstimateOrderSheet;
import com.track.tran.WmsUploadOutboundOrderSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.tran.WmsUploadOutboundProdRemarksSheet;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class OutboundOrderImportServlet
 */
public class OutboundOrderImportServlet extends HttpServlet {
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

	public OutboundOrderImportServlet() {
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
			} else if (action.equalsIgnoreCase("confirmCountSheet")) {
				onConfirmCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadOutboundTemplate")) {
				onDownloadOutboundTemplate(request, response);
			}else if (action.equalsIgnoreCase("importEstimateCountSheet")) {
				onImportEstimateCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmEstimateCountSheet")) {
				onConfirmEstimateCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadEstimateTemplate")) {
				onDownloadEstimateTemplate(request, response);
			}
			else if (action.equalsIgnoreCase("importOutboundProdRemarksCountSheet")) {
				onImportOutboundProdRemarksCountSheet(request, response);
			} 
			else if (action.equalsIgnoreCase("confirmOutboundProdRemarksCountSheet")) {
				onConfirmOutboundProdRemarksCountSheet(request, response);
			} 
			else if (action.equalsIgnoreCase("downloadOutboundProdRemarksTemplate")) {
				onDownloadOutboundProdRemarksTemplate(request, response);
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
		String NOOFORDER=((String) request.getSession().getAttribute("NOOFORDER"));
		
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

			request.getSession().setAttribute("IMP_OUTBOUNDRESULT", null);
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
			alRes = util.downloadOutboundSheetData(PLANT, StrFileName,
					StrSheetName,baseCurrency,NOOFORDER);

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

		request.getSession().setAttribute("OUTBOUNDRESULT", result);
		request.getSession().setAttribute("IMP_OUTBOUNDRESULT", alRes);
		//       
		response
				.sendRedirect("jsp/importOutboundOrderExcelSheet.jsp?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	private void onDownloadOutboundTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadOutBoundTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"SalesOrderData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		java.net.URL url = new java.net.URL("file://" + path);
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

		UserTransaction ut = null;
		boolean flag = true;
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDRESULT");

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
					
					linemap.put(IConstants.LOANHDR_ADDRESS, supplier_detail.get(2));
					linemap.put(IConstants.LOANHDR_ADDRESS2, supplier_detail.get(3));
					linemap.put(IConstants.LOANHDR_ADDRESS3, supplier_detail.get(4));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(11));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(12));
					
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
				    if(supplier_detail.size()>0) {
					linemap.put(IConstants.POHDR_CUST_CODE, (String)supplier_detail.get(1));
					linemap.put(IConstants.SHIPPINGID, (String)supplier_detail.get(0));
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
				    }else {
						linemap.put(IConstants.SHIPPINGCUSTOMER,""); 
				    }
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
				    linemap.put(IConstants.payment_terms, (String) lineArr.get(IConstants.payment_terms));
					linemap.put(IConstants.TRANSPORTID, (String) lineArr.get(IConstants.TRANSPORTID));
				    
					flag = process_Wms_CountSheet(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDRESULT");
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
			request.getSession().setAttribute("OUTBOUNDRESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDRESULT", result);
		response.sendRedirect("jsp/importOutboundOrderExcelSheet.jsp?");

	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmOutboundProdRemarksCountSheet(HttpServletRequest request,
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

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDPRODREMARKSRESULT");

			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.OUT_DONO, (String) lineArr.get(IConstants.OUT_DONO));
				    linemap.put(IConstants.OUT_DOLNNO, (String) lineArr.get(IConstants.OUT_DOLNNO));
				    linemap.put(IConstants.OUT_ITEM, (String) lineArr.get(IConstants.OUT_ITEM));
				    linemap.put("REMARKS", strUtils.InsertQuotes((String) lineArr.get("REMARKS")));
					
	
					flag = process_Wms_OutboundProdRemarksCountSheet(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDPRODREMARKSRESULT");
				flag = true;
				result = "<font color=\"green\">Sales Product Remarks Uploaded Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("OUTBOUNDPRODREMARKSRESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDPRODREMARKSRESULT", result);
		response.sendRedirect("jsp/importOutboundProductRemarksExcelSheet.jsp?");

	}


	private boolean process_Wms_CountSheet(Map map) throws Exception {
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
	

	private void onImportEstimateCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String NOOFORDER=((String) request.getSession().getAttribute("NOOFORDER"));
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

			request.getSession().setAttribute("IMP_ESTIMATERESULT", null);
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
			alRes = util.downloadEstimateSheetData(PLANT, StrFileName,
					StrSheetName,baseCurrency,NOOFORDER);

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

		request.getSession().setAttribute("ESTIMATERESULT", result);
		request.getSession().setAttribute("IMP_ESTIMATERESULT", alRes);
		//       
		response
				.sendRedirect("../salesestimate/import?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");


	}
	private void onConfirmEstimateCountSheet(HttpServletRequest request,
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

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ESTIMATERESULT");

			Map linemap = new HashMap();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
	
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.EST_ESTNO, (String) lineArr.get(IConstants.EST_ESTNO));
				    linemap.put(IConstants.EST_ESTLNNO, (String) lineArr.get(IConstants.EST_ESTLNNO));
				    linemap.put(IConstants.EST_REF_NO, (String) lineArr.get(IConstants.EST_REF_NO));
				 	linemap.put(IConstants.EST_COLLECTION_DATE, (String) lineArr.get(IConstants.EST_COLLECTION_DATE));
					linemap.put(IConstants.EST_COLLECTION_TIME, (String) lineArr.get(IConstants.EST_COLLECTION_TIME));
					linemap.put(IConstants.EST_REMARK1, (String) lineArr.get(IConstants.EST_REMARK1));
					linemap.put(IConstants.OUT_REMARK3, (String) lineArr.get(IConstants.OUT_REMARK3));
					linemap.put(IConstants.EST_EMPNO, strUtils.InsertQuotes((String) lineArr.get(IConstants.EST_EMPNO)));
					linemap.put(IConstants.EST_EXPIRE_DATE, (String) lineArr.get(IConstants.EST_EXPIRE_DATE));
					linemap.put(IConstants.EST_CUST_CODE, (String) lineArr.get(IConstants.EST_CUST_CODE));
					linemap.put(IConstants.OUT_OUTBOUND_GST, (String) lineArr.get(IConstants.OUT_OUTBOUND_GST));
                    linemap.put(IConstants.EST_ORDERTYPE, (String) lineArr.get(IConstants.EST_ORDERTYPE));
                    linemap.put(IDBConstants.DELIVERYDATE,(String) lineArr.get(IDBConstants.DELIVERYDATE)); 
					CustUtil custUtil = new CustUtil();
					ArrayList supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.EST_CUST_CODE), PLANT);
					String taxtret = (String)supplier_detail.get(34);					
					linemap.put(IConstants.OUT_CUST_NAME, strUtils.InsertQuotes((String)supplier_detail.get(1)));
					linemap.put(IConstants.OUT_PERSON_IN_CHARGE, strUtils.InsertQuotes((String)supplier_detail.get(9)));
                    linemap.put(IConstants.OUT_REMARK2,  strUtils.InsertQuotes((String)supplier_detail.get(15)));
					linemap.put(IConstants.EST_ITEM, (String) lineArr.get(IConstants.EST_ITEM));
					linemap.put(IConstants.EST_QTYOR, (String) lineArr.get(IConstants.EST_QTYOR));
					linemap.put(IConstants.EST_UNITMO, (String) lineArr.get(IConstants.EST_UNITMO));
					
					linemap.put(IConstants.LOANHDR_ADDRESS, supplier_detail.get(2));
					linemap.put(IConstants.LOANHDR_ADDRESS2, supplier_detail.get(3));
					linemap.put(IConstants.LOANHDR_ADDRESS3, supplier_detail.get(4));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(11));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(12));
					
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
                    
					linemap.put(IConstants.EST_UNITCOST, (String) lineArr.get(IConstants.EST_UNITCOST));
				    linemap.put(IConstants.EST_CURRENCYID, (String) lineArr.get(IConstants.EST_CURRENCYID));
	
									String itemDesc = "";
					String itemDetDesc = "";
	
					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.EST_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.EST_ITEM));
					ItemMstUtil itemMstUtil = new ItemMstUtil();
					String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, (String) lineArr.get(IConstants.EST_ITEM));
					linemap.put(IConstants.OUT_NONSTKTYPEID,  new StrUtils().InsertQuotes( nonstocktype));
					//end
					linemap.put(IConstants.OUT_ITEM_DES,  new StrUtils().InsertQuotes( itemDesc));
					linemap.put(IConstants.OUT_ITEM_DET_DES, new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.POHDR_DELIVERYDATEFORMAT));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
					if(supplier_detail.size()>0) {
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					linemap.put(IConstants.SHIPPINGID, (String)supplier_detail.get(0));
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					}else {
						linemap.put(IConstants.SHIPPINGCUSTOMER, "");
					}
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
	
	
					flag = process_Wms_EstimateCountSheet(linemap);
					
				}else{
					break;
				}
			}
			

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ESTIMATERESULT");
				flag = true;
				result = "<font color=\"green\">Sales Estimate Order Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order's </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ESTIMATERESULT", result);
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("ESTIMATERESULT", result);
		response.sendRedirect("../salesestimate/import?");
	}

	private void onDownloadEstimateTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadEstimateTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"SalesEstimateOrderData.xls\"");
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
	
	private void onDownloadOutboundProdRemarksTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadOutboundProdRemarksTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"SalesProductRemarks.xls\"");
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
	
	private void onImportOutboundProdRemarksCountSheet(HttpServletRequest request,
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

			request.getSession().setAttribute("IMP_OUTBOUNDPRODREMARKSRESULT", null);
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
			
			alRes = util.downloadOutboundProdRemarksSheetData(PLANT, StrFileName,StrSheetName);

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

		request.getSession().setAttribute("OUTBOUNDPRODREMARKSRESULT", result);
		request.getSession().setAttribute("IMP_OUTBOUNDPRODREMARKSRESULT", alRes);
		//       
		response
				.sendRedirect("jsp/importOutboundProductRemarksExcelSheet.jsp?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	private boolean process_Wms_EstimateCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadEstimateOrderSheet()");
		boolean flag = false;
		try{
		WmsTran tran = new WmsUploadEstimateOrderSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadEstimateOrderSheet()");
		}catch(Exception e){
			throw e;
		}
		return flag;
	}	
	
	private boolean process_Wms_OutboundProdRemarksCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " WmsUploadOutboundProdRemarksSheet()");
		boolean flag = false;
		try{
		WmsTran tran = new WmsUploadOutboundProdRemarksSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " WmsUploadOutboundProdRemarksSheet()");
		}catch(Exception e){
			throw e;
		}
		return flag;
	}	
		
}
