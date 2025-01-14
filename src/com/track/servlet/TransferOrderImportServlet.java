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
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadTransferOrderSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class TransferOrderImportServlet
 */
public class TransferOrderImportServlet extends HttpServlet {
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

	public TransferOrderImportServlet() {
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
			} else if (action.equalsIgnoreCase("downloadTransferTemplate")) {
				onDownloadTransferTemplate(request, response);
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

			request.getSession().setAttribute("IMP_TRANSFERRESULT", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;
			String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
			String NOOFORDER = (String) request.getSession().getAttribute("NOOFORDER");
			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"
					+ StrFileName);

			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();

			alRes = util.downloadTransferSheetData(PLANT, StrFileName,
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

			// throw e;
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("TRANSFERRESULT", result);
		request.getSession().setAttribute("IMP_TRANSFERRESULT", alRes);
		//       
		response
				.sendRedirect("../consignment/import?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	private void onDownloadTransferTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadTransferTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"ConsignmentOrderData.xls\"");
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
		StrUtils strUtils = new StrUtils();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_TRANSFERRESULT");

			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);

				if (flag) {

					linemap.put("LOGIN_USER", _login_user);
//					linemap.put(IConstants.PLANT, PLANT);
//					linemap.put(IConstants.TR_TONO, (String) lineArr.get(IConstants.TR_TONO));
//				    linemap.put(IConstants.TR_TOLNNO, (String) lineArr.get(IConstants.TR_TOLNNO));
//					linemap.put(IConstants.TR_FROM_LOC, (String) lineArr.get(IConstants.TR_FROM_LOC));
//					linemap.put(IConstants.TR_TO_LOC, (String) lineArr.get(IConstants.TR_TO_LOC));
//					linemap.put(IConstants.TR_REF_NO, (String) lineArr.get(IConstants.TR_REF_NO));
//					linemap.put(IConstants.TR_QTY, (String) lineArr.get(IConstants.TR_QTY));
//					linemap.put(IConstants.TR_ASSIGNEE, (String) lineArr.get(IConstants.TR_ASSIGNEE));

					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.TR_TONO, (String) lineArr.get(IConstants.TR_TONO));
				    linemap.put(IConstants.TR_TOLNNO, (String) lineArr.get(IConstants.TR_TOLNNO));
				    linemap.put(IConstants.TR_REF_NO, (String) lineArr.get(IConstants.TR_REF_NO));
				 	linemap.put(IConstants.TR_ORDERTYPE, (String) lineArr.get(IConstants.TR_ORDERTYPE));
				    linemap.put(IConstants.TR_TOLNNO, (String) lineArr.get(IConstants.TR_TOLNNO));
					linemap.put(IConstants.TR_FROM_LOC, (String) lineArr.get(IConstants.TR_FROM_LOC));
					linemap.put(IConstants.TR_TO_LOC, (String) lineArr.get(IConstants.TR_TO_LOC));
					linemap.put(IConstants.TR_ORDER_DATE, (String) lineArr.get(IConstants.TR_ORDER_DATE));
					linemap.put(IConstants.TR_ORDER_TIME, (String) lineArr.get(IConstants.TR_ORDER_TIME));
					linemap.put(IConstants.TR_REMARK1, strUtils.InsertQuotes((String) lineArr.get(IConstants.TR_REMARK1)));
					linemap.put(IConstants.TR_REMARK3, strUtils.InsertQuotes((String) lineArr.get(IConstants.TR_REMARK3)));
					linemap.put(IConstants.TR_CONSIGNMENT_GST, (String) lineArr.get(IConstants.TR_CONSIGNMENT_GST));
					linemap.put(IConstants.TR_ASSIGNEE, (String) lineArr.get(IConstants.TR_ASSIGNEE));
					
					CustUtil custUtil = new CustUtil();
//					ArrayList assignee_detail = custUtil.getToAssigneeDetails((String) lineArr.get(IConstants.TR_ASSIGNEE), PLANT);
					ArrayList supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.TR_ASSIGNEE), PLANT);
					linemap.put(IConstants.LOANHDR_ADDRESS, supplier_detail.get(2));
					linemap.put(IConstants.LOANHDR_ADDRESS2, supplier_detail.get(3));
					linemap.put(IConstants.LOANHDR_ADDRESS3, supplier_detail.get(4));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(11));
					linemap.put(IConstants.LOANHDR_CONTACT_NUM, supplier_detail.get(12));
					
					String taxtret = (String)supplier_detail.get(34);
					linemap.put(IConstants.TR_ASSIGNEE_NAME, strUtils.InsertQuotes((String)supplier_detail.get(1)));
					linemap.put(IConstants.TR_ASSIGNEE_PERSONINCHARGE, strUtils.InsertQuotes((String)supplier_detail.get(9)));
	                linemap.put(IConstants.TR_REMARK2,  strUtils.InsertQuotes((String)supplier_detail.get(15)));
					linemap.put(IConstants.TR_ITEM, (String) lineArr.get(IConstants.TR_ITEM));
					linemap.put(IConstants.TR_QTY, (String) lineArr.get(IConstants.TR_QTY));
					linemap.put(IConstants.TR_ITEM_UOM, (String) lineArr.get(IConstants.TR_ITEM_UOM));
	                
					linemap.put(IConstants.TR_UNITCOST, (String) lineArr.get(IConstants.TR_UNITCOST));
				    linemap.put(IConstants.TR_CURRENCYID, (String) lineArr.get(IConstants.TR_CURRENCYID));
				    linemap.put(IConstants.TOHDR_EMPNO, strUtils.InsertQuotes((String) lineArr.get(IConstants.TOHDR_EMPNO)));
					linemap.put(IConstants.DELIVERYDATE, strUtils.InsertQuotes((String) lineArr.get(IConstants.DELIVERYDATE)));
					String itemDesc = "";
					String itemDetDesc = "";
//					linemap.put(IConstants.TR_ASSIGNEE_NAME, assignee_detail.get(1));
//					linemap.put(IConstants.TR_ASSIGNEE_PERSONINCHARGE,assignee_detail.get(8));
//
//					linemap.put(IConstants.TR_ASSIGNEE_ADD1, assignee_detail.get(2));
//					linemap.put(IConstants.TR_ASSIGNEE_ADD2, assignee_detail.get(3));
//					linemap.put(IConstants.TR_ASSIGNEE_ADD3, assignee_detail.get(4));
//				        linemap.put(IConstants.TR_REMARK2, assignee_detail.get(14));
//                                        System.out.println(IConstants.TR_REMARK2 +""+assignee_detail.get(14));
//				    System.out.println(IConstants.TR_REMARK1 +""+(String) lineArr.get(IConstants.TR_REMARK1));
//					linemap.put(IConstants.TR_ORDER_DATE, (String) lineArr.get(IConstants.TR_ORDER_DATE));
//					linemap.put(IConstants.TR_ORDER_TIME, (String) lineArr.get(IConstants.TR_ORDER_TIME));
//					linemap.put(IConstants.TR_REMARK1, new StrUtils().InsertQuotes((String) lineArr.get(IConstants.TR_REMARK1)));
//					linemap.put(IConstants.TR_ITEM, (String) lineArr.get(IConstants.TR_ITEM));

//					ItemUtil itemUtil = new ItemUtil();
//					ArrayList item_details = itemUtil.getItemDetails((String) lineArr.get(IConstants.TR_ITEM), PLANT);
//
//					linemap.put(IConstants.TR_ITEM_DESC,  new StrUtils().InsertQuotes((String)item_details.get(1)));
//					linemap.put(IConstants.TR_ITEM_DET_DESC, new StrUtils().InsertQuotes((String)item_details.get(2)));
//					linemap.put(IConstants.TR_ITEM_UOM,  (String) lineArr.get(IConstants.TR_ITEM_UOM));
//

					ItemUtil itemUtil = new ItemUtil();
					itemDesc = itemUtil.getItemDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.TR_ITEM));
					itemDetDesc = itemUtil.getItemDetailDescriptionwithPlant(PLANT, (String) lineArr.get(IConstants.TR_ITEM));
					linemap.put(IConstants.TR_ITEM_DESC,  new StrUtils().InsertQuotes( itemDesc));
					linemap.put(IConstants.TR_ITEM_DET_DESC, new StrUtils().InsertQuotes( itemDetDesc));
					linemap.put(IConstants.ORDERDISCOUNT, (String) lineArr.get(IConstants.ORDERDISCOUNT));
					linemap.put(IConstants.SHIPPINGCOST, (String) lineArr.get(IConstants.SHIPPINGCOST));
					linemap.put(IConstants.INCOTERMS, (String) lineArr.get(IConstants.INCOTERMS));
					linemap.put(IConstants.PAYMENTTYPE, (String) lineArr.get(IConstants.PAYMENTTYPE));
					linemap.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String) lineArr.get(IConstants.POHDR_DELIVERYDATEFORMAT));
					linemap.put(IConstants.PRODGST, (String) lineArr.get(IConstants.PRODGST));
					custUtil = new CustUtil();
					supplier_detail = custUtil.getCustomerDetails((String) lineArr.get(IConstants.SHIPPINGCUSTOMER), PLANT);
				    if(supplier_detail.size()>0) {
					linemap.put(IConstants.SHIPPINGCUSTOMER, (String)supplier_detail.get(1));
					linemap.put(IConstants.SHIPPINGID, (String)supplier_detail.get(0));
				    }else {
						linemap.put(IConstants.SHIPPINGCUSTOMER,""); 
				    }
				    linemap.put(IConstants.TAXTREATMENT, taxtret);
				    linemap.put(IConstants.SALES_LOCATION, (String) lineArr.get(IConstants.SALES_LOCATION));
				    
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
				    linemap.put("ORDERDISCOUNT", (String) lineArr.get("ORDERDISCOUNT"));
				    linemap.put("PRODGST", (String) lineArr.get("PRODGST"));
//				    
//				    linemap.put("DISCOUNT_TYPE", (String) lineArr.get("DISCOUNT_TYPE"));
//				    linemap.put("DISCOUNT", (String) lineArr.get("DISCOUNT"));
//				    
//				    linemap.put("ISDISCOUNTTAX", (String) lineArr.get("ISDISCOUNTTAX"));
//				    linemap.put("ISORDERDISCOUNTTAX", (String) lineArr.get("ISORDERDISCOUNTTAX"));
//				    
				    linemap.put("ISSHIPPINGTAX", (String) lineArr.get("ISSHIPPINGTAX"));
				    linemap.put("ISDISCOUNTTAX", (String) lineArr.get("ISDISCOUNTTAX"));
				    linemap.put("PRODUCTDELIVERYDATE", (String) lineArr.get("PRODUCTDELIVERYDATE"));
				    linemap.put("ACCOUNT", (String) lineArr.get("ACCOUNT"));
				    linemap.put("TAX", (String) lineArr.get("TAX"));
				    linemap.put("PRODUCTDISCOUNT", (String) lineArr.get("PRODUCTDISCOUNT"));
				    linemap.put("PRODUCTDISCOUNT_TYPE", (String) lineArr.get("PRODUCTDISCOUNT_TYPE"));
					flag = process_Wms_CountSheet(linemap); 

				}
				// flag=true;
			}
			// if (!flag) {
			// throw new Exception("Error in Confirming Count Sheet");
			// }

			if (flag == true) {
			  
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_TRANSFERRESULT");
				flag = true;
				result = "<font color=\"green\">Consignment Order's Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Orders </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("TRANSFERRESULT", result);
			// throw e;
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("TRANSFERRESULT", result);
		response.sendRedirect("../consignment/import?");

	}

	private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadTransferOrderSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
}
