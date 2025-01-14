package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;

import com.track.dao.VendMstDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CurrencyDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.HrEmpSalaryDetDAO;
import com.track.dao.HrEmpTypeDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.OrderPaymentDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.PlantMstDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.HrEmpSalaryDET;
import com.track.db.object.HrLeaveType;
import com.track.db.util.*;
import com.track.gates.sqlBean;
//---Added by Bruhan on June 17 2014, Description:To open Kitting Summary  in excel power shell format
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
//---End Added by Bruhan on June 17 2014, Description:gndoretveo open Kitting Summary  in excel power shell format

public class ReportServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.ReportServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ReportServlet_PRINTPLANTMASTERINFO;
	StrUtils _StrUtils = null;
	XMLUtils _XMLUtils = null;
	DateUtils _dateUtils = null;
	OrderPaymentUtil _ordPaymentUtil = null;
	PlantMstDAO _PlantMstDAO  = null;
	String pcountry ="";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_StrUtils = new StrUtils();
		_XMLUtils = new XMLUtils();
		_dateUtils = new DateUtils();
		_ordPaymentUtil = new OrderPaymentUtil();
		 _PlantMstDAO=new PlantMstDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = "";
		String result = "";
		HttpSession session = request.getSession();
		StrUtils strUtils = new StrUtils();
		pcountry = strUtils.fString((String) session.getAttribute("COUNTRY"));
		if(pcountry.equalsIgnoreCase("United Arab Emirates"))
			pcountry="UAE";
		try {
			action = _StrUtils.fString(request.getParameter("action")).trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("view_movhis")) {
				result = view_movhis(request, response);
			} else if (action.equalsIgnoreCase("view_invdetails")) {

			} else if (action.equalsIgnoreCase("view_orderdetails")) {

			}
			else if(action.equalsIgnoreCase("ExportInvOpenClosingQty")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToInvOCExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=InventoryOCDetails.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			else if(action.equalsIgnoreCase("ExportRIDetails")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToRIDetExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=ProductRecvIssue.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			//start code by Bruhan for export to excel Received order  history on 13 sep 2013
			else if(action.equalsIgnoreCase("ExportReceivedHistory")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrderSummaryByCost.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			//End code by Bruhan for export to excel Received order  history on 13 sep 2013
           
			
			//start code by Bruhan for export to excel received  order  details on 25 oct 2013
			else if(action.equalsIgnoreCase("ExportReceivedOrderDetails")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelReceivedDetails(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=ReceivedOrderDetails.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			//End code by Bruhan for export to excel received  order  details on 25 oct 2013
           
			//start code by Bruhan for export to excel issued order history on 26 feb 2014
			else if(action.equalsIgnoreCase("ExportIssuedHistory")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelIssuedOrderHistory(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummaryByPrice.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			
			else if(action.equalsIgnoreCase("ExportConsignmentPrice")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelConsignmentHistory(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummaryByPrice.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
					
						
			else if(action.equalsIgnoreCase("ExportIssuedOrderDetails")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelIssuedDetails(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummaryByPrice.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			/*else if(action.equalsIgnoreCase("WIP_INV_EXCEL")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelWipInv(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=WipInventory.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }*/
		//---Added by Bruhan on June 12 2014, Description:To open work Order summary  in excel powershell format
	            else if (action.equals("ExportExcelWorkOrderSummary")) {
	         	   String newHtml="";
					 HSSFWorkbook wb = new HSSFWorkbook();
					 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
			         wb = this.writeToExcelWorkOrderSummary(request,response,wb);
					 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					 wb.write(outByteStream);
					 byte [] outArray = outByteStream.toByteArray();
					 response.setContentType("application/ms-excel");
					 response.setContentLength(outArray.length);
					 response.setHeader("Expires:", "0"); // eliminates browser caching
					 response.setHeader("Content-Disposition", "attachment; filename=WorkOrderSummary.xls");
					 OutputStream outStream = response.getOutputStream();
					 outStream.write(outArray);
					 outStream.flush();
					 outStream.close();
	           }
				 //---End Added by Bruhan on June 12 2014, Description:To open work Order summary in excel powershell format
	        /* else if(action.equalsIgnoreCase("WIP_SUMMARY_DETAILS_EXCEL")){
					 HSSFWorkbook wb = new HSSFWorkbook();
					 wb = this.writeToExcelWipSummaryDetails(request,response,wb);
					 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					 wb.write(outByteStream);
					 byte [] outArray = outByteStream.toByteArray();
					 response.setContentType("application/ms-excel");
					 response.setContentLength(outArray.length);
					 response.setHeader("Expires:", "0"); // eliminates browser caching
					 response.setHeader("Content-Disposition", "attachment; filename=WipSummaryDetails.xls");
					 OutputStream outStream = response.getOutputStream();
					 outStream.write(outArray);
					 outStream.flush();
					 
				 }*/

			else if(action.equalsIgnoreCase("Export_Inv_Reports")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelInvReports(request,response,wb);
				 String reportType = StrUtils.fString(request.getParameter("INV_REP_TYPE"));
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 if(reportType.equalsIgnoreCase("invByProdMultiUOM"))
				 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_With_Total_Quantity.xls");
				 else if(reportType.equalsIgnoreCase("invWithBatchUOM"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_BatchSno.xls");
				 else if(reportType.equalsIgnoreCase("invByProd"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_With_Total_Quantity_With_PCS.xls");
				 else if(reportType.equalsIgnoreCase("invWithBatch"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_BatchSno_With_PCS.xls");
				 else if(reportType.equalsIgnoreCase("invMinQty"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_Min_Max_Zero_Quantity.xls");
				 else if(reportType.equalsIgnoreCase("invMinQtyWithCost"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Revenue_Shortage.xls");
				 else if(reportType.equalsIgnoreCase("invExpiry"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_Expiry_Date.xls");
				 else if(reportType.equalsIgnoreCase("invAvgCost"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_Average_Cost.xls");
				 else if(reportType.equalsIgnoreCase("invWithAvlQty"))
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory_Summary_with_Available_Qty.xls");
				 else
					 response.setHeader("Content-Disposition", "attachment; filename=Inventory.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
						
			//---Added by Bruhan on May 26 2014, Description:To open Goods Issue Summary with in excel powershell format
			else if (action.equals("ExportExcelGISummary")) {
	            
	            String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelGISummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 if(DIRTYPE.equals("ISSUE")) {
					 response.setHeader("Content-Disposition", "attachment; filename=OrderIssueSummary.xls");
				 }
				 else if(DIRTYPE.equals("ISSUEWITHPRICE")) {
					 response.setHeader("Content-Disposition", "attachment; filename=OrderIssueSummaryWithPrice.xls");
				 }
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
			 
	       }
           //---End Added by Bruhan on May 26 2014, Description:To open Goods Issue Summary with  in excel powershell format
			
		   //---Added by Bruhan on May 27 2014, Description:To open Goods Receipt summary  with  in excel powershell format
             else if (action.equals("ExportExcelGRSummary")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelGRSummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 if(DIRTYPE.equals("RECEIVE")) {
					 response.setHeader("Content-Disposition", "attachment; filename=OrderReceiptSummary.xls");
				 }
				 else if(DIRTYPE.equals("RECEIVEWITHCOST")) {
					 response.setHeader("Content-Disposition", "attachment; filename=OrderReceiptSummaryWithCost.xls");
				 }
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
            //---End Added by Bruhan on May 27 2014, Description:To open Goods Receipt summary with in excel powershell format
          
             else if(action.equals("Export_Contacts_Excel")){
            	 String ReportType       = StrUtils.fString(request.getParameter("ReportType")).trim();
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToPContcatDetailsExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename="+ReportType+"MasterList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equals("Export_Excel_Bank")){
            	// String ReportType       = StrUtils.fString(request.getParameter("ReportType")).trim();
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToBankDetailsExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=BankMasterList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equals("Export_Employee_Excel")){
            	 String ReportType       = StrUtils.fString(request.getParameter("ReportType")).trim();
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToEmployeeDetailsExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename="+ReportType+"EmployeeMasterList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			
             else if(action.equals("Export_Employee_Leave_Excel")){
            	 String ReportType       = StrUtils.fString(request.getParameter("ReportType")).trim();
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToEmployeeLeaveDetailsExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename="+ReportType+"EmployeeLeaveDetailsList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			
             else if(action.equals("Export_Employee_Salary_Excel")){
            	 String ReportType       = StrUtils.fString(request.getParameter("ReportType")).trim();
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToEmployeeSalaryDetailsExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename="+ReportType+"EmployeeSalaryDetailsList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
		
       //---Added by Bruhan on June 10 2014, Description:To open Transfer Order summary  in excel powershell format
             else if (action.equals("ExportExcelTransferOrderSummary")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelTranferOrderSummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=TransferOrderSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
			 //---End Added by Bruhan on June 10 2014, Description:To open Transfer Order summary in excel powershell format
			
			 //---Added by Bruhan on June 10 2014, Description:To open loan Order summary  in excel powershell format
             else if (action.equals("ExportExcelLoanOrderSummary")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelLoanOrderSummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=RentalAndServiceOrderSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
             else if (action.equals("InvExportExcelLoanOrderSummaryWithPrice")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelLoanOrderSummaryWithPrice(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=RentalAndServiceOrderSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
			
			  else if (action.equals("InvExportExcelLoanOrderSummaryByPrice")) {
            	   String newHtml="";
  				 HSSFWorkbook wb = new HSSFWorkbook();
  				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
  				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
  		         wb = this.writeToExcelLoanOrderSummaryByPrice(request,response,wb);
  		       	 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
  				 wb.write(outByteStream);
  				 byte [] outArray = outByteStream.toByteArray();
  				 response.setContentType("application/ms-excel");
  				 response.setContentLength(outArray.length);
  				 response.setHeader("Expires:", "0"); 
  				 response.setHeader("Content-Disposition", "attachment; filename=RentalAndServiceOrderSummary.xls");
  				 OutputStream outStream = response.getOutputStream();
  				 outStream.write(outArray);
  				 outStream.flush();
  				 outStream.close();
              }
			  
			  else if (action.equals("ReportRentalOrderExportExcel")) {
            	   String newHtml="";
  				 HSSFWorkbook wb = new HSSFWorkbook();
  				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
  				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
  		         wb = this.writeToExcelRentalOrderSummary(request,response,wb);
  				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
  				 wb.write(outByteStream);
  				 byte [] outArray = outByteStream.toByteArray();
  				 response.setContentType("application/ms-excel");
  				 response.setContentLength(outArray.length);
  				 response.setHeader("Expires:", "0"); // eliminates browser caching
  				 response.setHeader("Content-Disposition", "attachment; filename=RentalAndServiceOrderSummary.xls");
  				 OutputStream outStream = response.getOutputStream();
  				 outStream.write(outArray);
  				 outStream.flush();
  				 outStream.close();
              }
             else if (action.equals("ExportExcelLoanOrderSummaryWithPrice")) {
            	   String newHtml="";
  				 HSSFWorkbook wb = new HSSFWorkbook();
  				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
  				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
  		         wb = this.writeToExcelLoanOrderSummaryWithPrice(request,response,wb);
  				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
  				 wb.write(outByteStream);
  				 byte [] outArray = outByteStream.toByteArray();
  				 response.setContentType("application/ms-excel");
  				 response.setContentLength(outArray.length);
  				 response.setHeader("Expires:", "0"); // eliminates browser caching
  				 response.setHeader("Content-Disposition", "attachment; filename=RentalAndServiceOrderSummary.xls");
  				 OutputStream outStream = response.getOutputStream();
  				 outStream.write(outArray);
  				 outStream.flush();
  				 outStream.close();
              }
			 //---End Added by Bruhan on June 10 2014, Description:To open loan Order summary in excel powershell format
             
//---Added by Bruhan on June 17 2014, Description:To open loan Movment History  in excel power shell format
             else if (action.equals("ExportExcelMovementHistory")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelMovementHistory(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=Activity Logs.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
			 //---End Added by Bruhan on June 17 2014, Description:To open Activity Logs in excel power shell format
			
			//---Added by Bruhan on June 17 2014, Description:To open Kitting Summary  in excel power shell format
             else if (action.equals("ExportExcelKittingSummary")) {
          	   String newHtml="";
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				 String DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE")).trim();
		         wb = this.writeToExcelKittingSummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=KittingSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
			 //---End Added by Bruhan on June 17 2014, Description:To open Kitting Summary in excel power shell format
			
			
             else if(action.equalsIgnoreCase("TimeAttendance_Details_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelTimeAttendanceDetails(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=TimeAttendanceDetails.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			
             else if(action.equalsIgnoreCase("LaborTimeAttendance_Details_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelLaborTimeAttendanceDetails(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=LaborTimeAttendanceDetails.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equalsIgnoreCase("Export_Loc_Report_ZeroQty")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelLocZeroQty(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=LocationSummaryZeroQty.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
			
             else if (action.equals("ExportPaymentSummary")) {
 	            
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelPaymentsummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=PaymentSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
			 
	       }
             else if(action.equalsIgnoreCase("KITBOMSUMMARY")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelKitBomSummary(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=KitBomSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equalsIgnoreCase("SUPPLIERDISCOUNT")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelSupplierDiscount(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SupplierDiscount.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equalsIgnoreCase("CUSTOMERDISCOUNT")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelCustomerDiscount(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=CustomerDiscount.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equalsIgnoreCase("view_inv_summary_aging_days")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelInvAging(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=InventoryAging.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
			 }
             else if(action.equalsIgnoreCase("Export_Inv_AGING_Reports")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelInvAgingReports(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=InventoryAging.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
			 }
             else if(action.equalsIgnoreCase("ExportInvVsOutgoing")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToInvVsoutgoingReport(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=Inventoryincomingvsoutgoing.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }
             else if(action.equals("Export_Customer_Discount_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToCustomerDiscountSummartExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=CustomerDiscountMasterList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();				 
			 }
             else if(action.equals("Export_SupplierReport_Excel")){
             	
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToSupplierReportExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); 
				 response.setHeader("Content-Disposition", "attachment; filename=SupplierReportList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }		
             else if (action.equals("ExportExcelSalesReturnSummary")) {
				 HSSFWorkbook wb = new HSSFWorkbook();
				 String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		         wb = this.writeToExcelSalesReturn(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=SalesReturnSummary.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 outStream.close();
            }
             else if(action.equalsIgnoreCase("Export_AlerBrand_Reports")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelAltBrandReports(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=AlternateBrandProduct.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();
				 
			 }else if (action.equals("ExportExcelCostPriceInv")) {
//					String newHtml = "";
					HSSFWorkbook wb = new HSSFWorkbook();
//					String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					wb = this.writeToExcelInvCostPrice(request, response, wb);
					ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					wb.write(outByteStream);
					byte[] outArray = outByteStream.toByteArray();
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Expires:", "0"); // eliminates browser caching
					response.setHeader("Content-Disposition", "attachment; filename=InventorySummaryWithCostAndPrice.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
					outStream.close();
				}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String view_movhis(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "";
		try {

			aPlant = _StrUtils.fString(request.getParameter("PLANT"));

			if (str.equalsIgnoreCase("")) {
				str = _XMLUtils.getXMLMessage(1,
						"No open Outbound order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = _XMLUtils.getXMLMessage(1, e.getMessage());
			throw e;

		}

		return str;

	}
	
	
	//start code by Bruhan for export to excel received order history on 25 sep 2013
	 private HSSFWorkbook writeToExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			DateUtils _dateUtils = new DateUtils();
			ArrayList listQry = new ArrayList();
			HTReportUtil movHisUtil = new HTReportUtil();
			int SheetId =1;
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				String userID = (String) session.getAttribute("LOGIN_USER");
				String FROM_DATE = "", TO_DATE = "",fdate = "", tdate = "", ORDERNO = "",jobno="", cntRec = "false",CUSTOMER = "",INVOICENUM = "",statusID,VIEWSTATUS="",LOCALEXPENSES="";
			
				FROM_DATE = _StrUtils.fString(request.getParameter("FROM_DATE"));
				TO_DATE =   _StrUtils.fString(request.getParameter("TO_DATE"));
				LOCALEXPENSES = _StrUtils.fString(request.getParameter("LOCALEXPENSES"));
				String  fieldDesc="";
				String reportType ="";

				if (FROM_DATE == null)
					FROM_DATE = "";
				else
					FROM_DATE = FROM_DATE.trim();
				String curDate = _dateUtils.getDate();
				if (FROM_DATE.length() < 0 || FROM_DATE == null
						|| FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE = curDate;
				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
				
				if (TO_DATE == null)
					TO_DATE = "";
				else
					TO_DATE = TO_DATE.trim();
				if (TO_DATE.length() > 5)
					 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
								
				ORDERNO = _StrUtils.fString(request.getParameter("ORDERNO"));
				jobno = _StrUtils.fString(request.getParameter("JOBNO"));
				CUSTOMER = _StrUtils.fString(request.getParameter("CUST_NAME"));
				
				String  CURRENCYID      = _StrUtils.fString(request.getParameter("CURRENCYID"));
		        String  CURRENCYDISPLAY = _StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
		        String  CURRENCYUSEQT = _StrUtils.fString(request.getParameter("currencyuseqt"));

	            if(CURRENCYID.equals(""))
	            {
	                     CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
	                     List _listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(plant,CURRENCYDISPLAY);
	                     for(int i =0; i<_listQry.size(); i++) {
	                         Map m=(Map)_listQry.get(i);
	                         CURRENCYID=(String)m.get("currencyid");
	                         CURRENCYUSEQT=(String)m.get("CURRENCYUSEQT");
	                     }
	            }
		        
				//INVOICENUM = _StrUtils.fString(request.getParameter("INVOICENUM"));
				statusID = _StrUtils.fString(request.getParameter("STATUS_ID"));
				
				PlantMstDAO plantMstDAO = new PlantMstDAO();
		        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				
				Hashtable ht = new Hashtable();
				
				if (_StrUtils.fString(ORDERNO).length() > 0)
					ht.put("a.PONO", ORDERNO);
				if (_StrUtils.fString(jobno).length() > 0)
					ht.put("JobNum", jobno);
				//if (_StrUtils.fString(INVOICENUM).length() > 0)
					//ht.put("a.INVOICENO", INVOICENUM);
				if (_StrUtils.fString(statusID).length() > 0)
					ht.put("b.STATUS_ID", statusID);
					
				if (LOCALEXPENSES.equalsIgnoreCase("1"))						
					{ 
						listQry = movHisUtil.getReceivedInboundOrdersSummarylocalexpenses(ht, fdate,tdate,plant,CUSTOMER);
					}else
					{ 
						listQry = movHisUtil.getReceivedInboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER);
					}
					
				//listQry = movHisUtil.getReceivedInboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER);
			
				 if (listQry.size() >= 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							HSSFCellStyle CompHeader = null;
							 sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeader(wb);
							 CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidth(sheet);
							 sheet = this.createHeader(sheet,styleHeader,plant);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
							 Double gst=0.0,gstpercentage=0.0,prodgstsubtotal1=0.0;	
							 ArrayList prodGstList = new ArrayList();
							 RecvDetDAO  _RecvDetDAO =new RecvDetDAO();
							 String taxby=_PlantMstDAO.getTaxBy(plant);
							 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
								decimalFormat.setRoundingMode(RoundingMode.FLOOR);
							
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 	Double subtotal =  Double.parseDouble(((String) lineArr.get("subtotal").toString())) ;
								 	if(taxby.equalsIgnoreCase("BYORDER"))
									{
								 		gstpercentage =  Double.parseDouble(((String) lineArr.get("inbound_gst").toString())) ;
								 		/*gst = (subtotal*gstpercentage)/100;	*/						 	
								 		//gst =  Double.parseDouble(((String) lineArr.get("taxval").toString())) ;
								 		//double tax =0.0;
				              	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
				              	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
				              	           if(taxid != 0){
				              	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
				              	        		   gst = (subtotal*gstpercentage)/100;
				              	        	   } else 
				              	        		 gstpercentage=Double.parseDouble("0.000");
				              	        	   
				              	           }else 
				              	        		 gstpercentage=Double.parseDouble("0.000");
									}
								 	else
								 	{
								 		prodGstList = _RecvDetDAO.getRecvProductGst(plant,(String) lineArr.get("pono").toString());
										prodgstsubtotal1=0.0;
										for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
											Map prodGstArr = (Map)prodGstList.get(jCnt);
											int jIndex = jCnt + 1;
											prodgstsubtotal1=prodgstsubtotal1+Double.parseDouble(((String) prodGstArr.get("subtotal").toString()));
										}
										gst=prodgstsubtotal1;
								 	}
									
								 	
//								 	Double total = subtotal+gst;
								 	
								 	int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
								 	Double total = 0.0;
								 	 if(taxid == 0){
								 		total = subtotal;
								 	 }else {
								 		total = subtotal+gst;
								 	 }
									
								 	//String gstValue = String.valueOf(gst);
								 	String gstValue =  decimalFormat.format(gst);
								 	//String totalValue = String.valueOf(total);
								 	String totalValue =  decimalFormat.format(total);
								 	String GSTValue = String.valueOf(gst);
								 	/*float gstVal ="".equals(gstValue) ? 0.0f :  Float.parseFloat(gstValue);
								 	float totalVal ="".equals(totalValue) ? 0.0f :  Float.parseFloat(totalValue);
						    		
						    		if(gstVal==0f){
						    			gstValue="0.000";
						    		}else{
						    			gstValue=gstValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}if(totalVal==0f){
						    			totalValue="0.00000";
						    		}else{
						    			totalValue=totalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						    		}*/
								 	
								 	double gstVal ="".equals(gstValue) ? 0.0f :  Double.parseDouble(gstValue);
						            double totalVal ="".equals(totalValue) ? 0.0f :  Double.parseDouble(totalValue);
						                
						            gstValue = StrUtils.addZeroes(gstVal, numberOfDecimal);
						            totalValue = StrUtils.addZeroes(totalVal, numberOfDecimal);
						            
						            
						            
								 		dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow( iCnt+2);
									    
									    HSSFCell cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pono"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("JobNum"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("recvdate"))));
										cell.setCellStyle(dataStyle);
											
										String subtotalValue = StrUtils.addZeroes(subtotal, numberOfDecimal);
										
										
										String currencyid = (String)lineArr.get("CURRENCYID");
							            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
							            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
							            
							            rcvCostconv = subtotal * currencyseqt; 
							            String SrcvCostconv= String.valueOf(rcvCostconv);
							            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
							            
							            recvCostwTaxConv = totalVal * currencyseqt; 
							            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
							            String exchangerate = String.format("%.2f", currencyseqt);
							            
							            taxValcon = gstVal  * currencyseqt; 
							            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
										

							            //DISPLAY COST BASED ON CURRENCY VALUE
					                    float CursubtotalValue=Float.parseFloat(subtotalValue)*Float.parseFloat(CURRENCYUSEQT);
					                   String CursubtotalValues = StrUtils.addZeroes(CursubtotalValue, numberOfDecimal);
					                    float CurgstValue = 0.0f;
					                    String CurgstValues ="";
					                    if(taxid == 0){
					                    	CurgstValue=0.0f;
					                    	CurgstValues = StrUtils.addZeroes(CurgstValue, numberOfDecimal);
					                    }else {
					                    	CurgstValue=Float.parseFloat(gstValue)*Float.parseFloat(CURRENCYUSEQT);
					                    	CurgstValues = StrUtils.addZeroes(CurgstValue, numberOfDecimal);
					                    }
//					                    float CurgstValue=Float.parseFloat(gstValue)*Float.parseFloat(CURRENCYUSEQT);
					                    float CurtotalValue=Float.parseFloat(totalValue)*Float.parseFloat(CURRENCYUSEQT);
					                    String CurtotalValues = StrUtils.addZeroes(CurtotalValue, numberOfDecimal);
									 	
					                    
					                    cell = row.createCell( k++);
					                    cell.setCellValue(exchangerate);
					                    cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(subtotalValue);
//										cell.setCellValue(CursubtotalValues);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(SrcvCostconv);
										cell.setCellStyle(dataStyle);
									
//										cell = row.createCell( k++);
//										cell.setCellValue(new HSSFRichTextString(StrUtils.fString(GSTValue)+"%"));
////										cell.setCellValue(new HSSFRichTextString(gstpercentage+"%"));
//										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
//										cell.setCellValue(gstValue);
										cell.setCellValue(CurgstValues);
										cell.setCellStyle(dataStyle);
										

										cell = row.createCell( k++);
										cell.setCellValue(StaxValcon);
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
										cell.setCellValue(totalValue);
//										cell.setCellValue(CurtotalValues);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(SrecvCostwTaxconv);
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("receivedby"))));
										cell.setCellStyle(dataStyle);
										
										
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
	 
	 private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String plant){
			int k = 0;
			try{
				String currency=_PlantMstDAO.getBaseCurrency(plant);
			
			
			HSSFRow rowhead = sheet.createRow(1);
			HSSFCell cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("ORDER NO"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("REF NO"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("SUPPLIER NAME"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("RECEIVED DATE"));
			cell.setCellStyle(styleHeader);
			
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("EXCHANGE RATE"));
			cell.setCellStyle(styleHeader);
						
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("SUBTOTAL ("+currency+")"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("SUBTOTAL"));
			cell.setCellStyle(styleHeader);
			
//			cell = rowhead.createCell( k++);
//			cell.setCellValue(new HSSFRichTextString("TAX%")); 
//			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("TAX ("+currency+")"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("TAX"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("TOTAL ("+currency+")"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("TOTAL"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("RECEIVED BY"));
			cell.setCellStyle(styleHeader);
		
													
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		private HSSFSheet createWidth(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth(0 ,5000);
				sheet.setColumnWidth(1 ,5000);
				sheet.setColumnWidth(2 ,4000);
				sheet.setColumnWidth(3 ,3000);
				sheet.setColumnWidth(4 ,3000);
				sheet.setColumnWidth(5 ,4000);
				sheet.setColumnWidth(6 ,5000);
				sheet.setColumnWidth(7 ,4000);
				sheet.setColumnWidth(8 ,4000);
				sheet.setColumnWidth(9 ,4000);
				sheet.setColumnWidth(10 ,4000);
				sheet.setColumnWidth(11 ,4000);
				
				
				
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
	    
	  //end code by Bruhan for export to excel received order history on 25 sep 2013    

	
	    
	  //start code by Bruhan for export to excel received order details on 25 oct 2013
 private HSSFWorkbook writeToExcelReceivedDetails(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
				String plant = "";
				DateUtils _dateUtils = new DateUtils();
				ArrayList listQry = new ArrayList();
				RecvDetDAO  _RecvDetDAO   = new RecvDetDAO ();
				try{
					HttpSession session = request.getSession();
					plant = (String) session.getAttribute("PLANT");
				    String userID = (String) session.getAttribute("LOGIN_USER");
					String PONO="",PGaction="",fieldDesc="",DATE="",TODATE="";
					PONO = StrUtils.fString(request.getParameter("PONO"));
					DATE = StrUtils.fString(request.getParameter("DATE"));
					//TODATE = StrUtils.fString(request.getParameter("TODATE"));
					
					listQry = _RecvDetDAO.getReceivedInboundOrderDetails(plant,PONO,DATE,TODATE);
				
					 if (listQry.size() > 0) {
								HSSFSheet sheet = null ;
								HSSFCellStyle styleHeader = null;
								HSSFCellStyle dataStyle = null;
								 sheet = wb.createSheet("Sheet1");
								 styleHeader = createStyleHeaderReceivedDetails(wb);
								 sheet = this.createWidthReceivedDetails(sheet);
								 sheet = this.createHeaderReceivedDetails(sheet,styleHeader);
												
								 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
									 Map lineArr = (Map) listQry.get(iCnt);	
									 int k = 0;
									 	Double unitcost =  Double.parseDouble(((String) lineArr.get("unitcost").toString())) ;
									 	Double ordqty =  Double.parseDouble(((String) lineArr.get("ordqty").toString())) ;
									 	Double recqty = Double.parseDouble(((String) lineArr.get("recqty").toString())) ;
									 	
									 	
									 	//String total = StrUtils.formatNum((subtotal).toString());
										
										///Float subtotal= _StrUtils.Round(subtotal1,2);
										//Float gst = _StrUtils.Round(gst1,2);
										//Float total = _StrUtils.Round(total1,2);
										
										//String gst = gst1.toString();
									
									 		dataStyle = createDataStyle(wb);
										    HSSFRow row = sheet.createRow( iCnt+1);
										    
										    HSSFCell cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pono"))));
											cell.setCellStyle(dataStyle);
											
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnno"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
											cell.setCellStyle(dataStyle);
												
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
											cell.setCellStyle(dataStyle);
											
																
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("batch"))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc"))));
											cell.setCellStyle(dataStyle);
										
											
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.currencyWtoutCommSymbol((unitcost.toString()))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("uom"))));
											cell.setCellStyle(dataStyle);
						
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.formatNum((ordqty.toString()))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.formatNum((recqty.toString()))));
											cell.setCellStyle(dataStyle);
											
											
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
		 
		 private HSSFSheet createHeaderReceivedDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
				
				
				HSSFRow rowhead = sheet.createRow( 0);
				HSSFCell cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Line no"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Description"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Batch"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Loc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Unitcost"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("UOM"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Receive Qty"));
				cell.setCellStyle(styleHeader);
														
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			private HSSFSheet createWidthReceivedDetails(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth(0 ,5000);
					sheet.setColumnWidth(1 ,2000);
					sheet.setColumnWidth(2 ,5000);
					sheet.setColumnWidth(3 ,12000);
					sheet.setColumnWidth(4 ,4000);
					sheet.setColumnWidth(5 ,3000);
					sheet.setColumnWidth(6 ,3000);
					sheet.setColumnWidth(7 ,3000);
					sheet.setColumnWidth(8 ,3000);
					sheet.setColumnWidth(9 ,3000);
					
					
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			
			private HSSFCellStyle createStyleHeaderReceivedDetails(HSSFWorkbook wb){
				
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
			
		    private HSSFCellStyle createDataStyleReceivedDetails(HSSFWorkbook wb){
				
				//Create style
				  HSSFCellStyle dataStyle = wb.createCellStyle();
				  dataStyle.setWrapText(true);
				  return dataStyle;
			}
		    
		  //end code by Bruhan for export to excel received order details on 25 oct 2013    

	//start code by Bruhan for export to excel issued order history on 26 feb 2014 
	 private HSSFWorkbook writeToExcelIssuedOrderHistory(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					String plant = "";int SheetId =1;
					DateUtils _dateUtils = new DateUtils();
					ShipHisDAO _ShipHisDAO=new ShipHisDAO();
					 PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
					ArrayList listQry = new ArrayList();
					ArrayList prodGstList = new ArrayList();
					HTReportUtil movHisUtil = new HTReportUtil();
					try{
						HttpSession session = request.getSession();
						plant = session.getAttribute("PLANT").toString();
						String userID = (String) session.getAttribute("LOGIN_USER");
						String reportType ="";
						String FROM_DATE = "", TO_DATE = "",fdate = "", tdate = "", ORDERNO = "", cntRec = "false",CUSTOMER = "",statusID="",sCustomerTypeId="",sort="",LOC="",
								LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",POSSEARCH="1";
					
						FROM_DATE = _StrUtils.fString(request.getParameter("FROM_DATE"));
						TO_DATE =   _StrUtils.fString(request.getParameter("TO_DATE"));
						
						PlantMstDAO plantMstDAO = new PlantMstDAO();
				        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
						
						String  fieldDesc="";

						if (FROM_DATE == null)
							FROM_DATE = "";
						else
							FROM_DATE = FROM_DATE.trim();
						String curDate = _dateUtils.getDate();
						if (FROM_DATE.length() < 0 || FROM_DATE == null
								|| FROM_DATE.equalsIgnoreCase(""))
							FROM_DATE = curDate;
						if (FROM_DATE.length() > 5)
							fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
						
						if (TO_DATE == null)
							TO_DATE = "";
						else
							TO_DATE = TO_DATE.trim();
						if (TO_DATE.length() > 5)
							 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
										
						ORDERNO = _StrUtils.fString(request.getParameter("ORDERNO"));
						CUSTOMER = _StrUtils.fString(request.getParameter("CUSTOMER"));
						statusID = _StrUtils.fString(request.getParameter("STATUS_ID"));
						sCustomerTypeId  = _StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
						sort  = _StrUtils.fString(request.getParameter("SORT"));
						LOC     = StrUtils.fString(request.getParameter("LOC"));
						LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
						LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
						LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
						POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
						if(POSSEARCH.equalsIgnoreCase("") || POSSEARCH.equalsIgnoreCase("null"))
							POSSEARCH="1";
						
						Hashtable ht = new Hashtable();
						
						if (_StrUtils.fString(ORDERNO).length() > 0)	ht.put("a.DONO", ORDERNO);
						if (_StrUtils.fString(statusID).length() > 0)	ht.put("b.STATUS_ID", statusID);
						if (_StrUtils.fString(sCustomerTypeId).length() > 0)	ht.put("CUSTTYPE", sCustomerTypeId);
						if (_StrUtils.fString(LOC).length() > 0)	ht.put("LOC", LOC);
						if (_StrUtils.fString(LOC_TYPE_ID).length() > 0)	ht.put("LOC_TYPE_ID", LOC_TYPE_ID);
						if (_StrUtils.fString(LOC_TYPE_ID2).length() > 0)	ht.put("LOC_TYPE_ID2", LOC_TYPE_ID2);
						if (_StrUtils.fString(LOC_TYPE_ID3).length() > 0)	ht.put("LOC_TYPE_ID3", LOC_TYPE_ID3);
						if (_StrUtils.fString(sort).length() > 0)	ht.put("SORT", sort);
								
						listQry = movHisUtil.getIssuedOutboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER,POSSEARCH);
								
							 if (listQry.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									HSSFCellStyle CompHeader = null;
									sheet = wb.createSheet("Sheet"+SheetId);
									 //sheet = wb.createSheet("Sheet1");
									 styleHeader = createStyleHeader(wb);
									 CompHeader = createCompStyleHeader(wb);
									 sheet = this.createWidthIssuedOrderHistory1(sheet);
									 sheet = this.createHeaderIssuedOrderHistory(sheet,styleHeader,sort);
									 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
									 Double gst=0.0,gstpercentage=0.0,prodgstsubtotal1=0.0;
									 String taxby=_PlantMstDAO.getTaxBy(plant);
									 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
										decimalFormat.setRoundingMode(RoundingMode.FLOOR);
									 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
										 Map lineArr = (Map) listQry.get(iCnt);	
										 int k = 0;
										 	Double subtotal =  Double.parseDouble(((String) lineArr.get("subtotal").toString())) ;
										 	if(taxby.equalsIgnoreCase("BYORDER"))
											{
											 	/*gstpercentage =  Double.parseDouble(((String) lineArr.get("outbound_gst").toString()));
											 	gst = (subtotal*gstpercentage)/100;*/									 	
										 		gst = Double.parseDouble((String) lineArr.get("taxval"));
											}
										 	else
										 	{
										 		prodGstList = _ShipHisDAO.getShippingProductGst(plant,(String) lineArr.get("dono").toString());
												prodgstsubtotal1=0.0;
												//String strprodgst="";
												for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
													//k=k+1;
													Map prodGstArr = (Map)prodGstList.get(jCnt);
													int jIndex = jCnt + 1;
													prodgstsubtotal1=prodgstsubtotal1+Double.parseDouble(((String) prodGstArr.get("subtotal").toString()));
												}
													gst=prodgstsubtotal1;
										 	}
											
											    Double total = subtotal+gst;
											    
											    String totalValue =  decimalFormat.format(total);
											    //String totalValue = String.valueOf(total);
											    String gstValue =  decimalFormat.format(gst);
											    //String gstValue = String.valueOf(gst);
											    String subtotalValue =  decimalFormat.format(subtotal);
											   // String subtotalValue = String.valueOf(subtotal);
											    
											    /*float totalVal ="".equals(totalValue) ? 0.0f :  Float.parseFloat(totalValue);*/
											    /*float gstVal ="".equals(gstValue) ? 0.0f :  Float.parseFloat(gstValue);*/
											    /*float subtotalVal ="".equals(subtotalValue) ? 0.0f :  Float.parseFloat(subtotalValue);*/
											    
											    double totalVal ="".equals(totalValue) ? 0.0d :  Double.parseDouble(totalValue);
											    double subtotalVal ="".equals(subtotalValue) ? 0.0d :  Double.parseDouble(subtotalValue);
											    double gstVal ="".equals(gstValue) ? 0.0d :  Double.parseDouble(gstValue);
					                            
					                            /*if(totalVal==0f){
					                            	totalValue="0.00000";
					                            }else{
					                            	totalValue=totalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					                            }if(gstVal==0f){
					                            	gstValue="0.000";
					                            }else{
					                            	gstValue=gstValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					                            }if(subtotalVal==0f){
					                            	subtotalValue="0.00000";
					                            }else{
					                            	subtotalValue=subtotalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					                            }*/
					                            
					                            totalValue = StrUtils.addZeroes(totalVal, numberOfDecimal);
					                            subtotalValue = StrUtils.addZeroes(subtotalVal, numberOfDecimal);
					                            gstValue = StrUtils.addZeroes(gstVal, numberOfDecimal);
											    
										 		dataStyle = createDataStyle(wb);
											    HSSFRow row = sheet.createRow( iCnt+2);
											    
											    HSSFCell cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dono"))));
												cell.setCellStyle(dataStyle);
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
												cell.setCellStyle(dataStyle);
												
												if(sort.equalsIgnoreCase("LOCATION")) {
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString((String)lineArr.get("loc")));
												cell.setCellStyle(dataStyle);
												}
												
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("issuedate"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell( k++);
												cell.setCellValue(subtotalValue);
												cell.setCellStyle(dataStyle);
											
												/*cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("outbound_gst"))+"%"));
												cell.setCellStyle(dataStyle);*/
											
												cell = row.createCell( k++);
												cell.setCellValue(gstValue);
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(totalValue);
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("issuedby"))));
												cell.setCellStyle(dataStyle);
												
																								
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
	 
	 //imthiyas consignment with price
	 private HSSFWorkbook writeToExcelConsignmentHistory(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";int SheetId =1;
			DateUtils _dateUtils = new DateUtils();
			ShipHisDAO _ShipHisDAO=new ShipHisDAO();
			 PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
			ArrayList listQry = new ArrayList();
			ArrayList prodGstList = new ArrayList();
			HTReportUtil movHisUtil = new HTReportUtil();
			try{
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				String userID = (String) session.getAttribute("LOGIN_USER");
				String reportType ="";
				String FROM_DATE = "", TO_DATE = "",fdate = "", tdate = "", ORDERNO = "", cntRec = "false",CUSTOMER = "",statusID="",sCustomerTypeId="";
			
				FROM_DATE = _StrUtils.fString(request.getParameter("FROM_DATE"));
				TO_DATE =   _StrUtils.fString(request.getParameter("TO_DATE"));
				
				PlantMstDAO plantMstDAO = new PlantMstDAO();
		        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				
				String  fieldDesc="";

				if (FROM_DATE == null)
					FROM_DATE = "";
				else
					FROM_DATE = FROM_DATE.trim();
				String curDate = _dateUtils.getDate();
				if (FROM_DATE.length() < 0 || FROM_DATE == null
						|| FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE = curDate;
				if (FROM_DATE.length() > 5)
					fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
				
				if (TO_DATE == null)
					TO_DATE = "";
				else
					TO_DATE = TO_DATE.trim();
				if (TO_DATE.length() > 5)
					 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
								
				ORDERNO = _StrUtils.fString(request.getParameter("ORDERNO"));
				CUSTOMER = _StrUtils.fString(request.getParameter("CUSTOMER"));
				statusID = _StrUtils.fString(request.getParameter("STATUS"));
				sCustomerTypeId  = _StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
				
				Hashtable ht = new Hashtable();
				
				if (_StrUtils.fString(ORDERNO).length() > 0)	ht.put("a.PONO", ORDERNO);
				if (_StrUtils.fString(statusID).length() > 0)	ht.put("b.STATUS", statusID);
				if (_StrUtils.fString(sCustomerTypeId).length() > 0)	ht.put("CUSTTYPE", sCustomerTypeId);
						
				listQry = movHisUtil.getConsignmentSummary(ht, fdate,tdate,plant,CUSTOMER);
						
					 if (listQry.size() > 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							HSSFCellStyle CompHeader = null;
							sheet = wb.createSheet("Sheet"+SheetId);
							 //sheet = wb.createSheet("Sheet1");
							 styleHeader = createStyleHeader(wb);
							 CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthIssuedOrderHistory1(sheet);
							 sheet = this.createHeaderIssuedOrderHistory(sheet,styleHeader);
							 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
							 Double gst=0.0,gstpercentage=0.0,prodgstsubtotal1=0.0;
							 String taxby=_PlantMstDAO.getTaxBy(plant);
							 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
								decimalFormat.setRoundingMode(RoundingMode.FLOOR);
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 	Double subtotal =  Double.parseDouble(((String) lineArr.get("subtotal").toString())) ;
								 	if(taxby.equalsIgnoreCase("BYORDER"))
									{
									 	/*gstpercentage =  Double.parseDouble(((String) lineArr.get("outbound_gst").toString()));
									 	gst = (subtotal*gstpercentage)/100;*/									 	
								 		gst = Double.parseDouble((String) lineArr.get("taxval"));
									}
								 	else
								 	{
								 		prodGstList = _ShipHisDAO.getShippingProductGST(plant,(String) lineArr.get("pono").toString());
										prodgstsubtotal1=0.0;
										//String strprodgst="";
										for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
											//k=k+1;
											Map prodGstArr = (Map)prodGstList.get(jCnt);
											int jIndex = jCnt + 1;
											prodgstsubtotal1=prodgstsubtotal1+Double.parseDouble(((String) prodGstArr.get("subtotal").toString()));
										}
											gst=prodgstsubtotal1;
								 	}
									
									    Double total = subtotal+gst;
									    
									    String totalValue =  decimalFormat.format(total);
									    //String totalValue = String.valueOf(total);
									    String gstValue =  decimalFormat.format(gst);
									    //String gstValue = String.valueOf(gst);
									    String subtotalValue =  decimalFormat.format(subtotal);
									   // String subtotalValue = String.valueOf(subtotal);
									    
									    /*float totalVal ="".equals(totalValue) ? 0.0f :  Float.parseFloat(totalValue);*/
									    /*float gstVal ="".equals(gstValue) ? 0.0f :  Float.parseFloat(gstValue);*/
									    /*float subtotalVal ="".equals(subtotalValue) ? 0.0f :  Float.parseFloat(subtotalValue);*/
									    
									    double totalVal ="".equals(totalValue) ? 0.0d :  Double.parseDouble(totalValue);
									    double subtotalVal ="".equals(subtotalValue) ? 0.0d :  Double.parseDouble(subtotalValue);
									    double gstVal ="".equals(gstValue) ? 0.0d :  Double.parseDouble(gstValue);
			                            
			                            /*if(totalVal==0f){
			                            	totalValue="0.00000";
			                            }else{
			                            	totalValue=totalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }if(gstVal==0f){
			                            	gstValue="0.000";
			                            }else{
			                            	gstValue=gstValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }if(subtotalVal==0f){
			                            	subtotalValue="0.00000";
			                            }else{
			                            	subtotalValue=subtotalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }*/
			                            
			                            totalValue = StrUtils.addZeroes(totalVal, numberOfDecimal);
			                            subtotalValue = StrUtils.addZeroes(subtotalVal, numberOfDecimal);
			                            gstValue = StrUtils.addZeroes(gstVal, numberOfDecimal);
									    
								 		dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow( iCnt+2);
									    
									    HSSFCell cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pono"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("recvdate"))));
										cell.setCellStyle(dataStyle);
											
										cell = row.createCell( k++);
										cell.setCellValue(subtotalValue);
										cell.setCellStyle(dataStyle);
									
										/*cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("outbound_gst"))+"%"));
										cell.setCellStyle(dataStyle);*/
									
										cell = row.createCell( k++);
										cell.setCellValue(gstValue);
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
										cell.setCellValue(totalValue);
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("issuedby"))));
										cell.setCellStyle(dataStyle);
										
																						
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
			 
	private HSSFSheet createHeaderIssuedOrderHistory(HSSFSheet sheet, HSSFCellStyle styleHeader,String SORT){
					int k = 0;
					try{
					
					
					HSSFRow rowhead = sheet.createRow(1);
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ORDER NO"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
					cell.setCellStyle(styleHeader);
					
					if(SORT.equalsIgnoreCase("LOCATION")) {
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("LOCATION"));
					cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ISSUED DATE"));
					cell.setCellStyle(styleHeader);
					
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("SUBTOTAL"));
					cell.setCellStyle(styleHeader);
					
					/*cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Tax%"));
					cell.setCellStyle(styleHeader);*/
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("TAX"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("TOTAL"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ISSUED BY"));
					cell.setCellStyle(styleHeader);
					
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
	private HSSFSheet createWidthIssuedOrderHistory1(HSSFSheet sheet){
					
					try{
						sheet.setColumnWidth(0 ,5000);
						sheet.setColumnWidth(1 ,5000);
						sheet.setColumnWidth(2 ,4000);
						sheet.setColumnWidth(3 ,3000);
						//sheet.setColumnWidth(4 ,3000);
						sheet.setColumnWidth(4 ,4000);
						sheet.setColumnWidth(5 ,5000);
						sheet.setColumnWidth(6 ,4000);
											
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
	
	private HSSFSheet createHeaderIssuedOrderHistory(HSSFSheet sheet, HSSFCellStyle styleHeader){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow(1);
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("ORDER NO"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
		cell.setCellStyle(styleHeader);

		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("ISSUED DATE"));
		cell.setCellStyle(styleHeader);
		
					
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("SUBTOTAL"));
		cell.setCellStyle(styleHeader);
		
		/*cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Tax%"));
		cell.setCellStyle(styleHeader);*/
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("TAX"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("TOTAL"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("ISSUED BY"));
		cell.setCellStyle(styleHeader);
		
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
private HSSFSheet createWidthIssuedOrderHistory(HSSFSheet sheet){
		
		try{
			sheet.setColumnWidth(0 ,5000);
			sheet.setColumnWidth(1 ,5000);
			sheet.setColumnWidth(2 ,4000);
			sheet.setColumnWidth(3 ,3000);
			//sheet.setColumnWidth(4 ,3000);
			sheet.setColumnWidth(4 ,4000);
			sheet.setColumnWidth(5 ,5000);
			sheet.setColumnWidth(6 ,4000);
								
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFWorkbook writeToExcelIssuedDetails(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
				String plant = "";
				DateUtils _dateUtils = new DateUtils();
				ArrayList listQry = new ArrayList();
				ShipHisDAO _ShipHisDAO = new ShipHisDAO();
				try{
					HttpSession session = request.getSession();
					plant = (String) session.getAttribute("PLANT");
				    String userID = (String) session.getAttribute("LOGIN_USER");
					String DONO="",PGaction="",fieldDesc="",DATE="",TODATE="";
					DONO = StrUtils.fString(request.getParameter("DONO"));
					DATE = StrUtils.fString(request.getParameter("DATE"));
					//TODATE = StrUtils.fString(request.getParameter("TODATE"));
					
					listQry = _ShipHisDAO.getIssuedOutboundOrderDetails(plant,DONO,DATE,TODATE);
				
					 if (listQry.size() > 0) {
								HSSFSheet sheet = null ;
								HSSFCellStyle styleHeader = null;
								HSSFCellStyle dataStyle = null;
								 sheet = wb.createSheet("Sheet1");
								 styleHeader = createStyleHeader(wb);
								 sheet = this.createWidthIssuedDetails(sheet);
								 sheet = this.createHeaderIssuedDetails(sheet,styleHeader);
												
								 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
									 Map lineArr = (Map) listQry.get(iCnt);	
									 int k = 0;
									 	Double unitprice =  Double.parseDouble(((String) lineArr.get("unitprice").toString())) ;
									 	Double ordqty =  Double.parseDouble(((String) lineArr.get("ordqty").toString())) ;
									 	Double issueqty = Double.parseDouble(((String) lineArr.get("pickqty").toString())) ;
									 
									 		dataStyle = createDataStyle(wb);
										    HSSFRow row = sheet.createRow( iCnt+1);
										    
										    HSSFCell cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dono"))));
											cell.setCellStyle(dataStyle);
											
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dolno"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
											cell.setCellStyle(dataStyle);
												
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
											cell.setCellStyle(dataStyle);
											
																
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("batch"))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc"))));
											cell.setCellStyle(dataStyle);
										
											
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.currencyWtoutCommSymbol((unitprice.toString()))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("uom"))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.formatNum((ordqty.toString()))));
											cell.setCellStyle(dataStyle);
										
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(StrUtils.formatNum((issueqty.toString()))));
											cell.setCellStyle(dataStyle);
											
											
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
		 
		 private HSSFSheet createHeaderIssuedDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
				
				
				HSSFRow rowhead = sheet.createRow( 0);
				HSSFCell cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Line no"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Description"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Batch"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Loc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Unitprice"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("UOM"));
				cell.setCellStyle(styleHeader);
				
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Issue Qty"));
				cell.setCellStyle(styleHeader);
														
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			private HSSFSheet createWidthIssuedDetails(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth(0 ,5000);
					sheet.setColumnWidth(1 ,2000);
					sheet.setColumnWidth(2 ,5000);
					sheet.setColumnWidth(3 ,12000);
					sheet.setColumnWidth(4 ,4000);
					sheet.setColumnWidth(5 ,3000);
					sheet.setColumnWidth(6 ,3000);
					sheet.setColumnWidth(7 ,3000);
					sheet.setColumnWidth(8 ,3000);
					sheet.setColumnWidth(9 ,3000);
					
					
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			
		
		
			 private HSSFWorkbook writeToInvOCExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					String plant = "";
					DateUtils _dateUtils = new DateUtils();
					StrUtils strUtils = new StrUtils();
					ArrayList listQry = new ArrayList();
					HTReportUtil movHisUtil = new HTReportUtil();
					int SheetId =1;
					try{
						HttpSession session = request.getSession();
						String PLANT = session.getAttribute("PLANT").toString();
						String userID = (String) session.getAttribute("LOGIN_USER");
						   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
						   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
						   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
						   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
						   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
						   String  FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
			               String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
			               String  LOC     = strUtils.fString(request.getParameter("LOC"));
		                   String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
			               String reportType = StrUtils.fString(request.getParameter("INV_REP_TYPE"));
			               java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
			               DecimalFormat decimalFormat = new DecimalFormat("#.#####");
							decimalFormat.setRoundingMode(RoundingMode.FLOOR);
							
							PlantMstDAO plantMstDAO = new PlantMstDAO();
							String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			               
			               String fdate ="",tdate="";
			                if (FROM_DATE.length()>5)
			                       fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
			                       
			                if (TO_DATE.length()>5){
		                        tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
			                }else{
			                	tdate = new DateUtils().getDateFormatyyyyMMdd();
			                }
					        Hashtable ht = new Hashtable();
					        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
							if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
							if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
							
				           if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
				        	   listQry= new InvUtil().getInvWithOpenCloseBal(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate);
				           }else if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC")){
				        	   listQry= new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"");
				           }else if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC_WAVGCOST")){
				        	   listQry= new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"WITH_AVGCOST");
				           }
				       
					
						 if (listQry.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									HSSFCellStyle CompHeader = null;
									sheet = wb.createSheet("Sheet"+SheetId);
									// sheet = wb.createSheet("Sheet1");
									 styleHeader = createStyleHeader(wb);
									 CompHeader = createCompStyleHeader(wb);
									 sheet = this.createWidthInvOC(sheet,reportType);
									 sheet = this.createHeaderInvOC(sheet,styleHeader,reportType);
									 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
													
									 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
										 Map lineArr = (Map) listQry.get(iCnt);	
										 int k = 0;
										 	
										   
				                          //  sumOfTotalCost = sumOfTotalCost + TotalCost;
				                         //   sumOfTotalPrice = sumOfTotalPrice + TotalPrice;
				                            
										 		dataStyle = createDataStyle(wb);
											    HSSFRow row = sheet.createRow( iCnt+2);
											    
											    HSSFCell cell = row.createCell( k++);
											    if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
												cell.setCellStyle(dataStyle);
											    }else {
											    	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
													cell.setCellStyle(dataStyle);
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
													cell.setCellStyle(dataStyle);
											    }
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
												cell.setCellStyle(dataStyle);
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_CLS_ID"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMTYPE"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_BRAND_ID"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
												cell.setCellStyle(dataStyle);
											
												String openingValue = (String)lineArr.get("OPENING");
												float openingVal ="".equals(openingValue) ? 0.0f :  Float.parseFloat(openingValue);
									    		
									    		if(openingVal==0f){
									    			openingValue="0.000";
									    		}else{
									    			openingValue=openingValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												
												cell = row.createCell( k++);
												cell.setCellValue(openingValue);
												cell.setCellStyle(dataStyle);
											
												String totRecvValue = (String)lineArr.get("TOTRECV");
												float totRecvVal ="".equals(totRecvValue) ? 0.0f :  Float.parseFloat(totRecvValue);
									    		
									    		if(totRecvVal==0f){
									    			totRecvValue="0.000";
									    		}else{
									    			totRecvValue=totRecvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												
												cell = row.createCell( k++);
												cell.setCellValue(totRecvValue);
												cell.setCellStyle(dataStyle);
												 if(!reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
													 	
													 String totLTRecvValue = (String)lineArr.get("TOTLTRECV");
														float totLTRecvVal ="".equals(totLTRecvValue) ? 0.0f :  Float.parseFloat(totLTRecvValue);
											    		
											    		if(totLTRecvVal==0f){
											    			totLTRecvValue="0.000";
											    		}else{
											    			totLTRecvValue=totLTRecvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    		}
													 
													 	cell = row.createCell( k++);
														cell.setCellValue(totLTRecvValue);
														cell.setCellStyle(dataStyle);
														
														String totLTIssueValue = (String)lineArr.get("TOTLTISS");
														float totLTIssueVal ="".equals(totLTIssueValue) ? 0.0f :  Float.parseFloat(totLTIssueValue);
											    		
											    		if(totLTIssueVal==0f){
											    			totLTIssueValue="0.000";
											    		}else{
											    			totLTIssueValue=totLTIssueValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    		}
														
														cell = row.createCell( k++);
														cell.setCellValue(totLTIssueValue);
														cell.setCellStyle(dataStyle);
											    }String total_IssueValue = (String)lineArr.get("TOTAL_ISS");
												float total_IssueVal ="".equals(total_IssueValue) ? 0.0f :  Float.parseFloat(total_IssueValue);
									    		
									    		if(total_IssueVal==0f){
									    			total_IssueValue="0.000";
									    		}else{
									    			total_IssueValue=total_IssueValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												cell = row.createCell( k++);
												cell.setCellValue(total_IssueValue);
												cell.setCellStyle(dataStyle);
												if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
													String totIssRevValue = (String)lineArr.get("TOTISSREV");
													float totIssRevVal ="".equals(totIssRevValue) ? 0.0f :  Float.parseFloat(totIssRevValue);
										    		
										    		if(totIssRevVal==0f){
										    			totIssRevValue="0.000";
										    		}else{
										    			totIssRevValue=totIssRevValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													cell = row.createCell( k++);
													cell.setCellValue(totIssRevValue);
													cell.setCellStyle(dataStyle);
												}
												String closingValue = (String)lineArr.get("CLOSING");
												float closingVal ="".equals(closingValue) ? 0.0f :  Float.parseFloat(closingValue);
									    		
									    		if(closingVal==0f){
									    			closingValue="0.000";
									    		}else{
									    			closingValue=closingValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												cell = row.createCell( k++);
												cell.setCellValue(closingValue);
												cell.setCellStyle(dataStyle);
												
												if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC_WAVGCOST")){
													 double avgCost =Double.parseDouble((String)lineArr.get("AVERAGE_COST"));
							                         double price =Double.parseDouble((String)lineArr.get("LIST_PRICE"));
							                         double TotalCost=avgCost* Double.parseDouble((String)lineArr.get("CLOSING"));
							                         double TotalPrice=price* Double.parseDouble((String)lineArr.get("CLOSING"));
							                       
							                        
							                         
							                         String avgCostValue = (String)lineArr.get("AVERAGE_COST");
							                         String listPriceValue = (String)lineArr.get("LIST_PRICE");
							                         String totalCostValue =  decimalFormat.format(TotalCost);
							                         //String totalCostValue = String.valueOf(TotalCost);
							                         String totalPriceValue =  decimalFormat.format(TotalPrice);
							                         // String totalPriceValue = String.valueOf(TotalPrice);
							                       
							                         
													/*float avgCostVal ="".equals(avgCostValue) ? 0.0f :  Float.parseFloat(avgCostValue);
													float listPriceVal ="".equals(listPriceValue) ? 0.0f :  Float.parseFloat(listPriceValue);
													float totalCostVal ="".equals(totalCostValue) ? 0.0f :  Float.parseFloat(totalCostValue);
													float totalPriceVal ="".equals(totalPriceValue) ? 0.0f :  Float.parseFloat(totalPriceValue);
											    	if(avgCostVal==0f){
											    		avgCostValue="0.00000";
											    	}else{
											    		avgCostValue=avgCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    	}if(listPriceVal==0f){
											    		listPriceValue="0.00000";
											    	}else{
											    		listPriceValue=listPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    	}if(totalCostVal==0f){
											    		totalCostValue="0.00000";
											    	}else{
											    		totalCostValue=totalCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    	}if(totalPriceVal==0f){
											    		totalPriceValue="0.00000";
											    	}else{
											    		totalPriceValue=totalPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    	}*/
							                         double avgCostVal ="".equals(avgCostValue) ? 0.0d :  Double.parseDouble(avgCostValue);
							                         double listPriceVal ="".equals(listPriceValue) ? 0.0d :  Double.parseDouble(listPriceValue);
							                         double totalCostVal ="".equals(totalCostValue) ? 0.0d :  Double.parseDouble(totalCostValue);
							                         double totalPriceVal ="".equals(totalPriceValue) ? 0.0d :  Double.parseDouble(totalPriceValue);
							                         
							                         avgCostValue = StrUtils.addZeroes(avgCostVal, numberOfDecimal);
							                         listPriceValue = StrUtils.addZeroes(listPriceVal, numberOfDecimal);
							                         totalCostValue = StrUtils.addZeroes(totalCostVal, numberOfDecimal);
							                         totalPriceValue = StrUtils.addZeroes(totalPriceVal, numberOfDecimal);
							                         
							                         
												 	cell = row.createCell( k++);
													cell.setCellValue(avgCostValue);
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(listPriceValue);
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(totalCostValue);
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(totalPriceValue);
													cell.setCellStyle(dataStyle);
											 }
												String recvd_AfterValue = (String)lineArr.get("RECVED_AFTER");
												float recvd_AfterVal ="".equals(recvd_AfterValue) ? 0.0f :  Float.parseFloat(recvd_AfterValue);
									    		
									    		if(recvd_AfterVal==0f){
									    			recvd_AfterValue="0.000";
									    		}else{
									    			recvd_AfterValue=recvd_AfterValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												
												cell = row.createCell( k++);
												cell.setCellValue(recvd_AfterValue);
												cell.setCellStyle(dataStyle);
												
												 if(!reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
													 String ctotltRecvValue = (String)lineArr.get("CTOTLTRECV");
														float ctotltRecvVal ="".equals(ctotltRecvValue) ? 0.0f :  Float.parseFloat(ctotltRecvValue);
											    		
											    		if(ctotltRecvVal==0f){
											    			ctotltRecvValue="0.000";
											    		}else{
											    			ctotltRecvValue=ctotltRecvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    		}
													 	cell = row.createCell( k++);
														cell.setCellValue(ctotltRecvValue);
														cell.setCellStyle(dataStyle);
														
														String ctotltIssueValue = (String)lineArr.get("CTOTLTISS");
														float ctotltIssueVal ="".equals(ctotltIssueValue) ? 0.0f :  Float.parseFloat(ctotltIssueValue);
											    		
											    		if(ctotltIssueVal==0f){
											    			ctotltIssueValue="0.000";
											    		}else{
											    			ctotltIssueValue=ctotltIssueValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
											    		}
														
														cell = row.createCell( k++);
														cell.setCellValue(ctotltIssueValue);
														cell.setCellStyle(dataStyle);
											    }
												 String issued_afterValue = (String)lineArr.get("ISSUED_AFTER");
													float issued_afterVal ="".equals(issued_afterValue) ? 0.0f :  Float.parseFloat(issued_afterValue);
										    		
										    		if(issued_afterVal==0f){
										    			issued_afterValue="0.000";
										    		}else{
										    			issued_afterValue=issued_afterValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
												 
												cell = row.createCell( k++);
												cell.setCellValue(issued_afterValue);
												cell.setCellStyle(dataStyle);
												
												String stockOnHandValue = (String)lineArr.get("STOCKONHAND");
												float stockOnHandVal ="".equals(stockOnHandValue) ? 0.0f :  Float.parseFloat(stockOnHandValue);
									    		
									    		if(stockOnHandVal==0f){
									    			stockOnHandValue="0.000";
									    		}else{
									    			stockOnHandValue=stockOnHandValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}
												
												cell = row.createCell( k++);
												cell.setCellValue(stockOnHandValue);
												cell.setCellStyle(dataStyle);
												 
											
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
			 
			 private HSSFSheet createHeaderInvOC(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType){
					int k = 0;
					try{
					
					
					HSSFRow rowhead = sheet.createRow( 1);
					HSSFCell cell = rowhead.createCell( k++);
					if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
					}else{
						cell.setCellValue(new HSSFRichTextString("Loc"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Product ID"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product Category ID"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product Sub Category ID "));
					cell.setCellStyle(styleHeader);
					
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Opening Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Total Recved"));
					cell.setCellStyle(styleHeader);
					
					if(!reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Consignment In"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Out"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Total Issued"));
					cell.setCellStyle(styleHeader);
					if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Issue Reversed"));
					cell.setCellStyle(styleHeader);
					}
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Closing Qty"));
					cell.setCellStyle(styleHeader);
					
					 if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC_WAVGCOST")){
						 cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Avgerage Unit Cost"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("List Price"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Total Cost"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Total Price"));
							cell.setCellStyle(styleHeader);
					 }
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Later Recv"));
					cell.setCellStyle(styleHeader);
					
					if(!reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Consignment In"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Out"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Issue"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Stock On Hand"));
					cell.setCellStyle(styleHeader);
				
															
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
			
			 private HSSFSheet createWidthInvOC(HSSFSheet sheet,String reportType){
					
					try{
						
					
						sheet.setColumnWidth(0 ,5000);
						sheet.setColumnWidth(1 ,12000);
						sheet.setColumnWidth(2 ,5000);
						sheet.setColumnWidth(3 ,5000);
						sheet.setColumnWidth(4 ,5000);
						sheet.setColumnWidth(5 ,4000);
						sheet.setColumnWidth(6 ,3000);
						sheet.setColumnWidth(7 ,3000);
						sheet.setColumnWidth(8 ,3000);
						sheet.setColumnWidth(9 ,3000);
						sheet.setColumnWidth(10 ,3000);
						sheet.setColumnWidth(11 ,3000);
						sheet.setColumnWidth(12 ,3000);
						sheet.setColumnWidth(13 ,3000);
						sheet.setColumnWidth(14 ,3000);
						sheet.setColumnWidth(15 ,3000);
						sheet.setColumnWidth(16 ,3000);
						
						
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
			 
			 
			 private HSSFWorkbook writeToRIDetExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					
					DateUtils _dateUtils = new DateUtils();
					StrUtils strUtils = new StrUtils();
					ArrayList listQry = new ArrayList();
					HTReportUtil movHisUtil = new HTReportUtil();
					try{
						HttpSession session = request.getSession();
						String PLANT = session.getAttribute("PLANT").toString();
						String userID = (String) session.getAttribute("LOGIN_USER");
						 
						   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
						   String fdate        = strUtils.fString(request.getParameter("FROM_DATE"));
			               String  tdate         = strUtils.fString(request.getParameter("TO_DATE"));
			               String  LOC         = strUtils.fString(request.getParameter("LOC"));
			               String  TYPE         = strUtils.fString(request.getParameter("TYPE"));
			               
			               if(TYPE.equalsIgnoreCase("OCLOC")){
			           		
			            	   listQry =  new HTReportUtil().getPrdRecvIssueDetails(PLANT,ITEM,fdate,tdate,LOC);
			       		
					       	}else{
					       		listQry =  new HTReportUtil().getPrdRecvIssueDetails(PLANT,ITEM,fdate,tdate);
					       	}
			             
				   
				       
						 if (listQry.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									 sheet = wb.createSheet("Sheet1");
									 styleHeader = createStyleHeader(wb);
									 sheet = this.createWidthRI(sheet);
									 sheet = this.createHeaderRIDetails(sheet,styleHeader);
													
									 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
										 Map lineArr = (Map) listQry.get(iCnt);	
										 int k = 0;
										 	
										 		dataStyle = createDataStyle(wb);
											    HSSFRow row = sheet.createRow( iCnt+1);
											    
											    HSSFCell cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANTYPE"))));
												cell.setCellStyle(dataStyle);
												

												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DIRTYPE"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
												cell.setCellStyle(dataStyle);
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ORDNO"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CNAME"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(Double.parseDouble(StrUtils.currencyWtoutCommSymbol(((String)lineArr.get("QTY").toString()))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANDATE"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CRBY"))));
												cell.setCellStyle(dataStyle);
											
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
			 
			 private HSSFSheet createHeaderRIDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
					int k = 0;
					try{
					
					
					HSSFRow rowhead = sheet.createRow( 0);
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Type"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Tran Type"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Sup/Cust Name"));
					cell.setCellStyle(styleHeader);
					
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Loc"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Batch"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("TranDate"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Remarks"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("User"));
					cell.setCellStyle(styleHeader);
					
					
															
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
			
			 private HSSFSheet createWidthRI(HSSFSheet sheet){
					
					try{
						sheet.setColumnWidth(0 ,5000);
						sheet.setColumnWidth(1 ,5000);
						sheet.setColumnWidth(1 ,5000);
						sheet.setColumnWidth(2 ,12000);
						sheet.setColumnWidth(3 ,5000);
						sheet.setColumnWidth(4 ,12000);
						sheet.setColumnWidth(5 ,4000);
						sheet.setColumnWidth(6 ,3000);
						sheet.setColumnWidth(7 ,3000);
						sheet.setColumnWidth(8 ,3000);
						sheet.setColumnWidth(9 ,3000);
						sheet.setColumnWidth(10 ,3000);
						
						
						
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
		    
		



 private HSSFWorkbook writeToExcelInvReports(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					String plant = "",fdate="",tdate="";
				    int maxRowsPerSheet = 65535;
					
					int SheetId =1;
					DateUtils _dateUtils = new DateUtils();
					StrUtils strUtils =  new StrUtils();
					ArrayList listQry = new ArrayList();
					InvUtil invUtil =  new InvUtil();
					//int SheetId =1;
					try{
							HttpSession session = request.getSession();
							plant = (String) session.getAttribute("PLANT");
						       String userID = (String) session.getAttribute("LOGIN_USER");
						       String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
						       String LOC = strUtils.fString(request.getParameter("LOC"));
						       String ITEM = strUtils.fString(request.getParameter("ITEM"));
						       String BATCH = strUtils.fString(request.getParameter("BATCH"));
						       String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
						       String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
						       String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
						       String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
						       String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
						       String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
						       String EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
						       String minQty = strUtils.fString(request.getParameter("MINQTY"));
						       String CURRENCYID= strUtils.fString(request.getParameter("CURRENCYID"));
						       String CURRENCYDISPLAY= strUtils.fString(request.getParameter("CURRENCYDISPLAY"));
						       String withZero = strUtils.fString(request.getParameter("WITHZERO"));
						       String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
						       String STATUS = strUtils.fString(request.getParameter("STATUS"));
						       String LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
						       String LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
						       String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
						       String reportType = StrUtils.fString(request.getParameter("INV_REP_TYPE"));
						       String MODEL = StrUtils.fString(request.getParameter("MODEL"));
						       String UOM = StrUtils.fString(request.getParameter("UOM"));
						       String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
						       String SORT=StrUtils.fString(request.getParameter("SORT"));
							   //String reportType ="";
						       java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
						       DecimalFormat decimalFormat = new DecimalFormat("#.#####");
								 decimalFormat.setRoundingMode(RoundingMode.FLOOR);
						       double TotalPrice = 0,TotalCost = 0;
					           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			          			if (FROM_DATE.length()>5)
			          				fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
			          			if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			          			if (TO_DATE.length()>5)
			          				tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
			          			
			        			PlantMstDAO plantMstDAO = new PlantMstDAO();
			        			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			          			
			        			String PARENT_PLANTDESC = "";
			        			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENTBYCHILD(plant);
					               if(PARENT_PLANT==null)
					            	   PARENT_PLANT="";
					               else {
					            	   PARENT_PLANTDESC = new PlantMstDAO().getcmpyname(PARENT_PLANT);
					            	   if(PARENT_PLANTDESC.length()>15)
					            		   PARENT_PLANTDESC=PARENT_PLANTDESC.substring(0, 14);
					               }
			        			
			          			Hashtable ht = new Hashtable();
			          			
			          			if(reportType.equalsIgnoreCase("invAvgCost")){
					            if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
					            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
					            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
					            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
					            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
					            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
					            
					            CurrencyDAO _CurrencyDAO=new CurrencyDAO();
			          		    String strCurrencyCode= _CurrencyDAO.getCurrencyCode(plant,CURRENCYDISPLAY);
			          		  if (LOCALEXPENSES.equalsIgnoreCase("1")){
			          			listQry = invUtil.getInvListSummaryWithAverageCostWithUOMLandedCost(ht,fdate,tdate,plant,ITEM,  PRD_DESCRIP,strCurrencyCode,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);  
			          		  }else {
			          			listQry = invUtil.getInvListSummaryWithAverageCostWithUOM(ht,fdate,tdate,plant,ITEM,  PRD_DESCRIP,strCurrencyCode,baseCurrency,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);  
			          		  }
         	          		    
			          			}else if(reportType.equalsIgnoreCase("invWithBatch")){
			          				if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
			          				if(strUtils.fString(ITEM).length() > 0)                ht.put("b.ITEM",ITEM);
						            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
						            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
									if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 						           
								   listQry = invUtil.getInvListSummaryWithOutPrice(ht, plant,ITEM,  PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC);
			          			}
			          			else if(reportType.equalsIgnoreCase("invByProd")){
			          			    if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
									if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 
						            listQry = invUtil.getInvListSummaryGroupByProd(plant, ITEM,PRD_DESCRIP, ht,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3);
			          			}
			          			else if(reportType.equalsIgnoreCase("invByProdMultiUOM")){
			          			    if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
									if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ;
								
						            listQry = invUtil.getInvListSummaryGroupByProdMultiUom(plant, ITEM,PRD_DESCRIP, ht,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,UOM,SORT);
			          			}
			          			else if(reportType.equalsIgnoreCase("invWithBatchUOM")){
			          			//	if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
			          				if(strUtils.fString(ITEM).length() > 0)                ht.put("b.ITEM",ITEM);
						            if(strUtils.fString(BATCH).length() > 0)               ht.put("BATCH",BATCH);
						            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
									if(strUtils.fString(MODEL).length() > 0)        ht.put("b.MODEL",MODEL) ; 
									
								   listQry = invUtil.getInvListSummaryWithOutPriceMultiUom(ht, plant,ITEM,  PRD_DESCRIP,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,UOM);
			          			}
			          			else if(reportType.equalsIgnoreCase("invMinQty") || reportType.equalsIgnoreCase("invMinQtyWithCost")){
			          				String VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
			          				if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
			          			    if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("PRD_DEPT_ID",PRD_DEPT_ID) ;
						            
						            if(PARENT_PLANT.equalsIgnoreCase(""))
						            listQry = invUtil.getInvListSummaryWithMinStock(ht,plant,ITEM,PRD_DESCRIP,false,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM,"");
						            else
						            listQry = invUtil.getInvListSummaryWithMinStock(ht,plant,ITEM,PRD_DESCRIP,false,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM,PARENT_PLANT);
			          			}else if(reportType.equalsIgnoreCase("invExpiry")){
			          				String VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
			          				if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
			          				if(strUtils.fString(ITEM).length() > 0)                ht.put("b.ITEM",ITEM);
						            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
						            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
						            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
						            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
						            if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ; 
						            listQry = invUtil.getInvListSummaryWithExpireDate(ht,plant,ITEM,PRD_DESCRIP,EXPIREDATE,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,VIEWSTATUS,UOM );
			          			}else if(reportType.equalsIgnoreCase("invWithAvlQty")){
			          				String VIEWSTATUS = strUtils.fString(request.getParameter("VIEWSTATUS"));
			          				if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("PRDCLSID",PRD_CLS_ID);
			          				if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("ITEMTYPE",PRD_TYPE_ID) ;  
			          				if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID) ;
			          				if(strUtils.fString(PRD_DEPT_ID).length() > 0)        ht.put("PRD_DEPT_ID",PRD_DEPT_ID) ;
						            listQry = invUtil.getInvListSummaryWithAvlQtyMultiUom(ht,plant,ITEM,PRD_DESCRIP,UOM);
			          			}
					
						 if (listQry.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									HSSFCellStyle CompHeader = null;
									 sheet = wb.createSheet("Sheet"+SheetId);
									 styleHeader = createStyleHeader(wb);
									 CompHeader = createCompStyleHeader(wb);
									
									 sheet = this.createWidthInvReports(sheet,reportType);
									 sheet = this.createHeaderInvReports(sheet,styleHeader,reportType,PARENT_PLANTDESC);
									 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
									 int index = 2;			
									 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
										 Map lineArr = (Map) listQry.get(iCnt);	
										 int k = 0;
										 TotalPrice = 0;
										 TotalCost = 0;
						                
										//IMTI modified in 16-03-2022 
										 	String invuomqty = StrUtils.fString((String)lineArr.get("INVUOMQTY"));
			                                String stkqty = strUtils.fString((String) lineArr.get("STKQTY"));
			                                String decimal = "00";
			                                String invuomqtydecimal ="";
			                                String stkqtydecimal ="";
			                                if (invuomqty.contains(".00")) {
			                                	invuomqtydecimal = invuomqty ;
			                                	stkqtydecimal = stkqty ;
			                                }else {
			    							invuomqtydecimal = invuomqty + decimal ;
			    							stkqtydecimal = stkqty + decimal ;
			                                }
			                              //END
			                                
//			                              //IMTI modified in 16-03-2022 
//										 	String invuomqty = StrUtils.fString((String)lineArr.get("INVUOMQTY"));
//			                                String stkqty = strUtils.fString((String) lineArr.get("STKQTY"));
//			                                String invuomqtydec = strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("INVUOMQTY"))), "3");
//			                                String stkqtydec = strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("STKQTY"))), "3");
//			                                String decimal = "00";
//			                                String invuomqtydecimal ="";
//			                                String stkqtydecimal ="";
//			                                if (invuomqty.contains(".00")) {
//			                                	invuomqtydecimal = invuomqty ;
//			                                	stkqtydecimal = stkqty ;
//			                                }else {
//			                                	invuomqtydecimal = invuomqtydec;
//			                                	stkqtydecimal = stkqtydec;
//			                                }
//			    						//end
			    							
										        Double qty =  Double.parseDouble(((String) lineArr.get("QTY").toString())) ;
										 		dataStyle = createDataStyle(wb);
											    HSSFRow row = sheet.createRow(index);
											    
											    HSSFCell cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
												cell.setCellStyle(dataStyle);
												
												if(!reportType.equalsIgnoreCase("invWithAvlQty")){
											    cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOCDESC"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC_TYPE_ID"))));
												cell.setCellStyle(dataStyle);
												}
												
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRDCLSID"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMTYPE"))));
												cell.setCellStyle(dataStyle);
												
																	
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_BRAND_ID"))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
												cell.setCellStyle(dataStyle);
												
												if(reportType.equalsIgnoreCase("invWithBatchUOM"))
												{
													cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK1"))));
													cell.setCellStyle(dataStyle);
												}		
												if(reportType.equalsIgnoreCase("invExpiry")){
													cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EXPIREDATE"))));
													cell.setCellStyle(dataStyle);	
												}
											if(!reportType.equalsIgnoreCase("invWithBatchUOM") && !reportType.equalsIgnoreCase("invWithAvlQty"))
											{
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
												cell.setCellStyle(dataStyle);
											}	
											if(reportType.equalsIgnoreCase("invWithAvlQty")){
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVENTORYUOM"))));
												cell.setCellStyle(dataStyle);
											}
												if(reportType.equalsIgnoreCase("invMinQty") || reportType.equalsIgnoreCase("invMinQtyWithCost")){
													
													String stkMinQtyValue = (String)lineArr.get("MINQTY");
													float stkMinQtyVal ="".equals(stkMinQtyValue) ? 0.0f :  Float.parseFloat(stkMinQtyValue);
										    		
										    		if(stkMinQtyVal==0f){
										    			stkMinQtyValue="0.000";
										    		}else{
										    			stkMinQtyValue=stkMinQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													
													cell = row.createCell(k++);
													cell.setCellValue(strUtils.addZeroes(Double.parseDouble(stkMinQtyValue), "3"));
													cell.setCellStyle(dataStyle);
													if(reportType.equalsIgnoreCase("invMinQty")) {
													String maxQtyValue = (String)lineArr.get("MAXQTY");
													float maxQtyVal ="".equals(maxQtyValue) ? 0.0f :  Float.parseFloat(maxQtyValue);
										    		
										    		if(maxQtyVal==0f){
										    			maxQtyValue="0.000";
										    		}else{
										    			maxQtyValue=maxQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													
													cell = row.createCell(k++);
													cell.setCellValue(strUtils.addZeroes(Double.parseDouble(maxQtyValue), "3"));
													cell.setCellStyle(dataStyle);
													}
													
												}
												else if(reportType.equalsIgnoreCase("invWithBatch") || reportType.equalsIgnoreCase("invWithBatchUOM") || reportType.equalsIgnoreCase("invAvgCost")){
												cell = row.createCell(k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
												cell.setCellStyle(dataStyle);
												}
												if(reportType.equalsIgnoreCase("invWithBatchUOM"))
												{
													cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
													cell.setCellStyle(dataStyle);
												}												
												
												if(reportType.equalsIgnoreCase("invAvgCost")){
													
													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKQTY"))));
													cell.setCellValue(new HSSFRichTextString(stkqtydecimal));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVENTORYUOM"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVUOMQTY"))));
													cell.setCellValue(new HSSFRichTextString(invuomqtydecimal));
													cell.setCellStyle(dataStyle);
													
													String avgCostValue = (String)lineArr.get("AVERAGE_COST");
													/*float avgCostVal ="".equals(avgCostValue) ? 0.0f :  Float.parseFloat(avgCostValue);													
										    		if(avgCostVal==0f){
										    			avgCostValue="0.00000";
										    		}else{
										    			avgCostValue=avgCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}*/
													double avgCostVal = "".equals(avgCostValue) ? 0.0d :  Double.parseDouble(avgCostValue);
													avgCostValue = StrUtils.addZeroes(avgCostVal, numberOfDecimal);
													
											    cell = row.createCell(k++);
												cell.setCellValue(avgCostValue);
												cell.setCellStyle(dataStyle);
												
												String costPerPcValue = (String)lineArr.get("COST_PC");
												/*float avgCostVal ="".equals(avgCostValue) ? 0.0f :  Float.parseFloat(avgCostValue);													
									    		if(avgCostVal==0f){
									    			avgCostValue="0.00000";
									    		}else{
									    			avgCostValue=avgCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}*/
												double costPerPcVal = "".equals(costPerPcValue) ? 0.0d :  Double.parseDouble(costPerPcValue);
												avgCostValue = StrUtils.addZeroes(costPerPcVal, numberOfDecimal);
												
											    cell = row.createCell(k++);
												cell.setCellValue(avgCostValue);
												cell.setCellStyle(dataStyle);
												
												String listPriceValue = (String)lineArr.get("LIST_PRICE");
												/*float listPriceVal ="".equals(listPriceValue) ? 0.0f :  Float.parseFloat(listPriceValue);									    		
									    		if(listPriceVal==0f){
									    			listPriceValue="0.00000";
									    		}else{
									    			listPriceValue=listPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}*/
												double listPriceVal = "".equals(listPriceValue) ? 0.0d :  Double.parseDouble(listPriceValue);
												listPriceValue = StrUtils.addZeroes(listPriceVal, numberOfDecimal);
												
												cell = row.createCell(k++);
												cell.setCellValue(listPriceValue);
												cell.setCellStyle(dataStyle);
												}
												String qtyValue = String.valueOf(qty);
												float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
									    		
									    		if(qtyVal==0f){
									    			qtyValue="0.000";
									    		}else{
//									    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
//									    			qtyValue = StrUtils.addZeroes(qtyVal, com.track.gates.DbBean.NOOFDECIMALPTSFORWEIGHT);
									    			qtyValue = StrUtils.addZeroes(qtyVal, "3");
									    		}
									    		if(!reportType.equalsIgnoreCase("invAvgCost")) {
												cell = row.createCell(k++);
												cell.setCellValue(qtyValue);
												cell.setCellStyle(dataStyle);
									    		}
												if(reportType.equalsIgnoreCase("invMinQty") || reportType.equalsIgnoreCase("invMinQtyWithCost")){
													
													cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVENTORYUOM"))));
													cell.setCellStyle(dataStyle);													
													
													String stkMinQtyValue = (String)lineArr.get("INVMINQTY");
													float stkMinQtyVal ="".equals(stkMinQtyValue) ? 0.0f :  Float.parseFloat(stkMinQtyValue);
										    		
										    		if(stkMinQtyVal==0f){
										    			stkMinQtyValue="0.000";
										    		}else{
										    			stkMinQtyValue=stkMinQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													
													cell = row.createCell(k++);
													cell.setCellValue(strUtils.addZeroes(Double.parseDouble(stkMinQtyValue), "3"));
													cell.setCellStyle(dataStyle);
													
													if(reportType.equalsIgnoreCase("invMinQty")) {
													String maxQtyValue = (String)lineArr.get("INVMAXQTY");
													float maxQtyVal ="".equals(maxQtyValue) ? 0.0f :  Float.parseFloat(maxQtyValue);
										    		
										    		if(maxQtyVal==0f){
										    			maxQtyValue="0.000";
										    		}else{
										    			maxQtyValue=maxQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													
													cell = row.createCell(k++);
													cell.setCellValue(maxQtyValue);
													cell.setCellStyle(dataStyle);
													
													String InvqtyValue = (String)lineArr.get("INVQTY");
													float InvqtyVal ="".equals(InvqtyValue) ? 0.0f :  Float.parseFloat(InvqtyValue);
										    		
										    		if(InvqtyVal==0f){
										    			InvqtyValue="0.000";
										    		}else{
										    			InvqtyValue=InvqtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													cell = row.createCell(k++);
													cell.setCellValue(strUtils.addZeroes(Double.parseDouble(InvqtyValue), "3"));
													cell.setCellStyle(dataStyle);
													}
													if(reportType.equalsIgnoreCase("invMinQty")){
														double INVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVQTY")));
														double INVMINQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMINQTY")));
														
														double PARENTINVQTY = 0.0;
														if(!PARENT_PLANT.equalsIgnoreCase("")) {
															PARENTINVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("PARENT_INVQTY")));
															cell = row.createCell(k++);
															cell.setCellValue(StrUtils.addZeroes(PARENTINVQTY, "3"));
															cell.setCellStyle(dataStyle);
														}
														 double TOPUP_QTY= INVMINQTY-(INVQTY+PARENTINVQTY);
							                                if (TOPUP_QTY < 0) {
							                                	TOPUP_QTY = 0;
							                                }
							                              String TOPUPQTY = StrUtils.addZeroes(TOPUP_QTY, numberOfDecimal);
							                              	cell = row.createCell(k++);
															cell.setCellValue(TOPUPQTY);
															cell.setCellStyle(dataStyle);
															
													}
													
													}
												
												if(reportType.equalsIgnoreCase("invAvgCost")){
												TotalCost=Double.parseDouble(((String)lineArr.get("AVERAGE_COST").toString()))* Double.parseDouble(((String)lineArr.get("QTY").toString())) ;
												
												//String totalCostValue = String.valueOf(TotalCost);
												String totalCostValue =  decimalFormat.format(TotalCost);
												/*float totalCostVal ="".equals(totalCostValue) ? 0.0f :  Float.parseFloat(totalCostValue);									    		
									    		if(totalCostVal==0f){
									    			totalCostValue="0.00000";
									    		}else{
									    			totalCostValue=totalCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}*/
												double totalCostVal = "".equals(totalCostValue) ? 0.0d :  Double.parseDouble(totalCostValue);
												totalCostValue = StrUtils.addZeroes(totalCostVal, numberOfDecimal);
												
												cell = row.createCell(k++);
												cell.setCellValue(totalCostValue);
												cell.setCellStyle(dataStyle);
												
												TotalPrice=Double.parseDouble(((String)lineArr.get("LIST_PRICE").toString()))* Double.parseDouble(((String)lineArr.get("QTY").toString())) ;
												//String totalPriceValue = String.valueOf(TotalPrice);
												String totalPriceValue =  decimalFormat.format(TotalPrice);
												/*float totalPriceVal ="".equals(totalPriceValue) ? 0.0f :  Float.parseFloat(totalPriceValue);									    		
									    		if(totalPriceVal==0f){
									    			totalPriceValue="0.00000";
									    		}else{
									    			totalPriceValue=totalPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
									    		}*/
												double totalPriceVal = "".equals(totalPriceValue) ? 0.0d :  Double.parseDouble(totalPriceValue);
												totalPriceValue = StrUtils.addZeroes(totalPriceVal, numberOfDecimal);
												
												cell = row.createCell(k++);
												cell.setCellValue(totalPriceValue);
												cell.setCellStyle(dataStyle);
												}
												if(reportType.equalsIgnoreCase("invWithAvlQty")){													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ESTIMATEQTY"))));
													cell.setCellValue(new HSSFRichTextString(strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("ESTIMATEQTY"))), "3")));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OUTBOUNDQTY"))));
													cell.setCellValue(new HSSFRichTextString(strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("OUTBOUNDQTY"))), "3")));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("AVAILABLEQTY"))));
													cell.setCellValue(new HSSFRichTextString(strUtils.addZeroes(Double.parseDouble(strUtils.fString((String) lineArr.get("AVAILABLEQTY"))), "3")));
													cell.setCellStyle(dataStyle);
												}
												
												if(reportType.equalsIgnoreCase("invMinQtyWithCost")){
													double INVQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVQTY")));
													double INVMINQTY = Double.valueOf(StrUtils.fString((String)lineArr.get("INVMINQTY")));
													double cost = Double.valueOf(StrUtils.fString((String)lineArr.get("TOTCOST")));
					                                double invcost = cost*INVQTY;
					                                double mincost= cost*INVMINQTY;
					                                
					                                String totalinvcost = StrUtils.addZeroes(invcost, numberOfDecimal);
					                                String totalmincost = StrUtils.addZeroes(mincost, numberOfDecimal);
					                                String totalrencost = StrUtils.addZeroes((invcost-mincost), numberOfDecimal);
													
													cell = row.createCell(k++);
													cell.setCellValue(totalmincost);
													cell.setCellStyle(dataStyle);
													
													String InvqtyValue = (String)lineArr.get("INVQTY");
													float InvqtyVal ="".equals(InvqtyValue) ? 0.0f :  Float.parseFloat(InvqtyValue);
										    		
										    		if(InvqtyVal==0f){
										    			InvqtyValue="0.000";
										    		}else{
										    			InvqtyValue=InvqtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
										    		}
													cell = row.createCell(k++);
													cell.setCellValue(strUtils.addZeroes(Double.parseDouble(InvqtyValue), "3"));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
													cell.setCellValue(totalinvcost);
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
													cell.setCellValue(totalrencost);
													cell.setCellStyle(dataStyle);
												}
												index++;
												 if((index-2)%maxRowsPerSheet==0){
													 index = 2;
													 SheetId++;
													 sheet = wb.createSheet("Sheet"+SheetId);
													 CompHeader = createCompStyleHeader(wb);
													 styleHeader = createStyleHeader(wb);
													 sheet = this.createWidthInvReports(sheet,reportType);
													 sheet = this.createHeaderInvReports(sheet,styleHeader,reportType,PARENT_PLANTDESC);
													 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
													  					
													 
												 }	
												 if(reportType.equalsIgnoreCase("invByProdMultiUOM") || reportType.equalsIgnoreCase("invWithBatchUOM")|| reportType.equalsIgnoreCase("invExpiry")){
												    cell = row.createCell(k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVENTORYUOM"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell(k++);
//													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVUOMQTY"))));
													cell.setCellValue(new HSSFRichTextString(invuomqtydecimal));
													cell.setCellStyle(dataStyle);
												 }
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
			 
			 private HSSFSheet createHeaderInvReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PARENT_PLANT){
					int k = 0;
					try{
						
					HSSFRow rowhead = sheet.createRow(1);
					HSSFCell cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Product"));
					cell.setCellStyle(styleHeader);
					
					if(!reportType.equalsIgnoreCase("invWithAvlQty")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Loc"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Loc Desc"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Loc Type One"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Sub Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Product Description"));
					cell.setCellStyle(styleHeader);
					
					if(reportType.equalsIgnoreCase("invWithBatchUOM"))
					{
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Detailed Description"));
						cell.setCellStyle(styleHeader);
					}		
					if(reportType.equalsIgnoreCase("invExpiry")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Exp Date"));
						cell.setCellStyle(styleHeader);
					}
					if(!reportType.equalsIgnoreCase("invWithBatchUOM")){
						if(reportType.equalsIgnoreCase("invMinQty")|| reportType.equalsIgnoreCase("invAvgCost") || reportType.equalsIgnoreCase("invExpiry")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("PC/PCS/EA UOM"));
					cell.setCellStyle(styleHeader);
						}
						else
						{
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("UOM"));
							cell.setCellStyle(styleHeader);
						}
					}
					if(reportType.equalsIgnoreCase("invMinQty") || reportType.equalsIgnoreCase("invMinQtyWithCost")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("PC/PCS/EA Min Qty"));
					cell.setCellStyle(styleHeader);
					
					if(reportType.equalsIgnoreCase("invMinQty")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("PC/PCS/EA Max Qty"));
					cell.setCellStyle(styleHeader);
					}
					
					}
					else if(reportType.equalsIgnoreCase("invWithBatch") || reportType.equalsIgnoreCase("invAvgCost") || reportType.equalsIgnoreCase("invWithBatchUOM")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Batch"));
					cell.setCellStyle(styleHeader);
					}
					if(reportType.equalsIgnoreCase("invWithBatchUOM")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("PC/PCS/EA UOM"));
						cell.setCellStyle(styleHeader);
						}					
					
					if(reportType.equalsIgnoreCase("invAvgCost")){
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("PC/PCS/EA Qty"));
						cell.setCellStyle(styleHeader);	
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv UOM"));
						cell.setCellStyle(styleHeader);	
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Qty"));
						cell.setCellStyle(styleHeader);	
						
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Avg UnitCost Per PC"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Avg Inv.Unitcost"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("List Price"));
					cell.setCellStyle(styleHeader);
					}
					
					if(reportType.equalsIgnoreCase("invWithBatchUOM") || reportType.equalsIgnoreCase("invMinQty")|| reportType.equalsIgnoreCase("invExpiry") || reportType.equalsIgnoreCase("invMinQtyWithCost")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("PC/PCS/EA Qty"));
					cell.setCellStyle(styleHeader);
					} else if(reportType.equalsIgnoreCase("invWithAvlQty")) {
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Stk On Hand"));
						cell.setCellStyle(styleHeader);
					}
					else
					{
						if(!reportType.equalsIgnoreCase("invAvgCost")) {
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Qty"));
						cell.setCellStyle(styleHeader);
						}
					}
					
					if(reportType.equalsIgnoreCase("invAvgCost")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Tot Inv.Cost"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Total Price"));
						cell.setCellStyle(styleHeader);
						
					}
					
					if(reportType.equalsIgnoreCase("invByProdMultiUOM") || reportType.equalsIgnoreCase("invWithBatchUOM")|| reportType.equalsIgnoreCase("invExpiry")){
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("INV.UOM"));
					cell.setCellStyle(styleHeader);
						
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("INV.QTY"));
					cell.setCellStyle(styleHeader);
					}
					
					if(reportType.equalsIgnoreCase("invMinQty")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv UOM"));
						cell.setCellStyle(styleHeader);
							
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Min Qty"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Max Qty"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Qty"));
						cell.setCellStyle(styleHeader);
						
						if(!PARENT_PLANT.equalsIgnoreCase("")) {
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString(PARENT_PLANT+" Inv Qty"));
							cell.setCellStyle(styleHeader);
						}
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("TopUp Qty"));
						cell.setCellStyle(styleHeader);
						
						}

					if(reportType.equalsIgnoreCase("invMinQtyWithCost")){
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv UOM"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Min Qty"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Min Cost"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Qty"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Inv Cost"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Revenue Shortage"));
						cell.setCellStyle(styleHeader);
						
					}
					
					if(reportType.equalsIgnoreCase("invWithAvlQty")){							
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Est QTY"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Sales Qty"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Avl Qty"));
						cell.setCellStyle(styleHeader);
					}
				
															
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				private HSSFSheet createWidthInvReports(HSSFSheet sheet,String reportType){
					
					try{
						sheet.setColumnWidth(0 ,5000);
						sheet.setColumnWidth(1 ,4000);
						sheet.setColumnWidth(2 ,5000);
						sheet.setColumnWidth(3 ,5000);
						sheet.setColumnWidth(4 ,4000);
						sheet.setColumnWidth(5 ,8000);
						sheet.setColumnWidth(6 ,3000);
						sheet.setColumnWidth(7 ,5000);
						if(reportType.equalsIgnoreCase("invWithBatchUOM"))
							sheet.setColumnWidth(6 ,8000);
						if(reportType.equalsIgnoreCase("invAvgCost")){
						sheet.setColumnWidth(8 ,3500);
						}
						else if(reportType.equalsIgnoreCase("invWithAvlQty")){
							sheet.setColumnWidth(8 ,5000);
							sheet.setColumnWidth(9 ,5000);
						}
						else if(!reportType.equalsIgnoreCase("invByProd")){
						sheet.setColumnWidth(8 ,3500);
						}
						if(reportType.equalsIgnoreCase("invExpiry")){
							sheet.setColumnWidth(9 ,3500);
						}
						if(reportType.equalsIgnoreCase("invMinQty")){
							sheet.setColumnWidth(9 ,3500);
						}
						if(reportType.equalsIgnoreCase("invAvgCost")){
						sheet.setColumnWidth(9 ,3500);
						sheet.setColumnWidth(10 ,3500);
						sheet.setColumnWidth(11 ,3500);
						sheet.setColumnWidth(12 ,3500);
						}
						
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}

	//

			     /**---Added by Bruhan on May 27 2014, Description:To open Goods Issue Summary  in excel powershell format
			           Bruhan on Sep 26 2014, Description: To change transaction date as issued date
			     */
				private HSSFWorkbook writeToExcelGISummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 HTReportUtil movHisUtil  = new HTReportUtil();
					 PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",taxby="";
					 int SheetId =1;
					try{
						
								String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE = strUtils.fString(request.getParameter("TO_DATE")).trim();
					        	String DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE")).trim();
					        	String JOBNO         = strUtils.fString(request.getParameter("JOBNO")).trim();
					        	String USER          = strUtils.fString(request.getParameter("USER")).trim();
					        	String ITEMNO        = strUtils.fString(request.getParameter("ITEM")).trim();
					        	String BATCH       = strUtils.fString(request.getParameter("BATCH")).trim();;
					        	String ORDERNO       = strUtils.fString(request.getParameter("ORDERNO")).trim();;
					        	String CUSTOMER      = strUtils.fString(request.getParameter("CUSTOMER")).trim();;
					        	String CUSTOMER_TO      = strUtils.fString(request.getParameter("CUSTOMER_TO")).trim();;
					        	String CUSTOMER_LO      = strUtils.fString(request.getParameter("CUSTOMER_LO")).trim();;
					        	String ITEMDESC      = strUtils.fString(request.getParameter("DESC")).trim();;
					        	String ORDERTYPE= strUtils.fString(request.getParameter("ORDERTYPE")).trim();;
					        	String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();;
					        	String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID")).trim();;
					        	String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID")).trim();;
					        	String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID")).trim();;
					        	String REASONCODE = strUtils.fString(request.getParameter("REASONCODE")).trim();;
					        	String LOC = strUtils.fString(request.getParameter("LOC")).trim();;
					        	String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID")).trim();
					        	String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2")).trim();
					        	String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3")).trim();
					        	String FILTER = strUtils.fString(request.getParameter("FILTER"));
					        	String INVOICENO = strUtils.fString(request.getParameter("INVOICENO")).trim();
					        	String reportType ="";
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					        	if (FROM_DATE.length()>5)
					            fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
					        	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length()>5)
					        	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
					        	
					        	PlantMstDAO plantMstDAO = new PlantMstDAO();
					            String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
					            
					        	if(DIRTYPE.equals("ISSUE"))
					        	{
					        		    Hashtable ht = new Hashtable(); 
					        		    if (strUtils.fString(ITEMNO).length() > 0)  		  ht.put("a.ITEM", ITEMNO);
					        			if(strUtils.fString(BATCH).length() > 0)              ht.put("a.BATCH",BATCH);
					        			if(strUtils.fString(ORDERNO).length() > 0)            ht.put("a.DONO",ORDERNO);
					        			if(strUtils.fString(CUSTOMER).length() > 0)           ht.put("a.CNAME",CUSTOMER);
					        			if(strUtils.fString(CUSTOMER_LO).length() > 0)        ht.put("a.CNAME_LON",CUSTOMER_LO);
					        			if(strUtils.fString(CUSTOMER_TO).length() > 0)        ht.put("a.CNAME_TO",CUSTOMER_TO);
					        			if(strUtils.fString(ORDERTYPE).length() > 0)   	   	  ht.put("d.ORDERTYPE", ORDERTYPE);
					        			if(strUtils.fString(PRD_TYPE_ID).length() > 0) 	      ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					        			if(strUtils.fString(PRD_BRAND_ID).length() > 0)       ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					        			if(strUtils.fString(PRD_DEPT_ID).length() > 0)       ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
					        			if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					        			if(strUtils.fString(REASONCODE).length() > 0)         ht.put("a.REMARK",REASONCODE);
					        			if(strUtils.fString(LOC).length() > 0)         	      ht.put("a.LOC",LOC);
					        			if(strUtils.fString(LOC_TYPE_ID).length() > 0)        ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
					        			if(strUtils.fString(LOC_TYPE_ID2).length() > 0)        ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
					        			if(strUtils.fString(LOC_TYPE_ID3).length() > 0)        ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
					        			if(strUtils.fString(INVOICENO).length() > 0)          ht.put("INVOICENO",INVOICENO);
					        			if(FILTER.length()>0)
									       {
									    	   movQryList = movHisUtil.getPickingSummarybyfilter(ht,fdate,tdate,FILTER,PLANT,ITEMDESC);
										   }
									       else
									       {
									    	   movQryList = movHisUtil.getPickingSummaryList(ht, fdate,tdate, DIRTYPE, PLANT,ITEMDESC );
									       }
					        			     					        			
					        	}
					        	else if(DIRTYPE.equals("ISSUEWITHPRICE"))
					        	{
					        		
							            Hashtable ht = new Hashtable();
							            if(strUtils.fString(ITEMNO).length() > 0)             ht.put("a.ITEM",ITEMNO);
								        if(strUtils.fString(BATCH).length() > 0)              ht.put("a.BATCH",BATCH);
								        if(strUtils.fString(ORDERNO).length() > 0)            ht.put("a.DONO",ORDERNO);
								        if(strUtils.fString(CUSTOMER).length() > 0)           ht.put("a.CNAME",CUSTOMER);
								        if(strUtils.fString(CUSTOMER_LO).length() > 0)        ht.put("a.CNAME_LON",CUSTOMER_LO);
								        if(strUtils.fString(CUSTOMER_TO).length() > 0)        ht.put("a.CNAME_TO",CUSTOMER_TO);
								        if (strUtils.fString(ORDERTYPE).length() > 0)   	  ht.put("d.ORDERTYPE", ORDERTYPE);
								        if(strUtils.fString(PRD_TYPE_ID).length() > 0) 	      ht.put("C.ITEMTYPE",PRD_TYPE_ID);
								        if(strUtils.fString(PRD_BRAND_ID).length() > 0)       ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
								        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
								        if(strUtils.fString(PRD_DEPT_ID).length() > 0)         ht.put("C.PRD_DEPT_ID",PRD_DEPT_ID);
								        if(strUtils.fString(REASONCODE).length() > 0)         ht.put("a.REMARK",REASONCODE);
								        if(strUtils.fString(LOC).length() > 0)         	      ht.put("a.LOC",LOC);
								        if(strUtils.fString(LOC_TYPE_ID).length() > 0)        ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
								        if(strUtils.fString(LOC_TYPE_ID2).length() > 0)        ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
								        if(strUtils.fString(LOC_TYPE_ID3).length() > 0)        ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
								        if(strUtils.fString(INVOICENO).length() > 0)          ht.put("INVOICENO",INVOICENO);
								        //getPickingSummarybyfilterByProductGst
								        taxby=_PlantMstDAO.getTaxBy(PLANT);
								        
								        if(FILTER.length()>0)
								        	
									       {
									        	if(taxby.equalsIgnoreCase("BYORDER"))
									        	{
									        		 movQryList = movHisUtil.getPickingSummarybyfilter(ht,fdate,tdate,FILTER,PLANT,ITEMDESC);
									        	}
									        	else
									        	{
									        		 movQryList = movHisUtil.getPickingSummarybyfilterByProductGst(ht,fdate,tdate,FILTER,PLANT,ITEMDESC);
									        	}
										   }
									       else
									       {
									    	   if(taxby.equalsIgnoreCase("BYORDER"))
									        	{
									       		  movQryList = movHisUtil.getPickingSummaryList(ht,fdate,tdate,"ISSUE",PLANT,ITEMDESC);
									         
									        	}
									    	   else
									    	   {
									    		   movQryList = movHisUtil.getPickingSummaryListByProductGst(ht,fdate,tdate,"ISSUE",PLANT,ITEMDESC);
									    	   }
									       }
						        	}
						 				           
									 Boolean workSheetCreated = true;
									 if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											CreationHelper createHelper = wb.getCreationHelper();
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyle = createDataStyle(wb);
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											 CompHeader = createCompStyleHeader(wb);
											 styleHeader = createStyleHeader(wb);
											sheet = this.createWidthGISummary(sheet,DIRTYPE);
											sheet = this.createHeaderGISummary(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         double subtotal=0,unitprice=0;
									         float gstpercentage = 0;
									         double issuePrice = 0, tax = 0, issPricewTax = 0;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									         DecimalFormat decimalFormat = new DecimalFormat("#.#####");
												decimalFormat.setRoundingMode(RoundingMode.FLOOR);
									         String strDate="";
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
													   Map lineArr = (Map) movQryList.get(iCnt);	
													   int k = 0;
													   
													   if(DIRTYPE.equals("ISSUEWITHPRICE")){
														   unitprice = Double.parseDouble((String)lineArr.get("unitprice"));
														  // unitprice = StrUtils.RoundDB(unitprice,2);
														   
														   String gst = (String) lineArr.get("Tax");
															if (gst.length() > 0) {
																gstpercentage = Float.parseFloat(gst);
															}

															issuePrice = Double.parseDouble((String) lineArr.get("total"));
															//issuePrice = StrUtils.RoundDB(issuePrice, 2);
															
															//tax = Double.parseDouble((String) lineArr.get("taxval"));
															tax = (issuePrice * gstpercentage) / 100;
															//tax = StrUtils.RoundDB(tax, 2);
															issPricewTax = issuePrice + tax;
															//issPricewTax = StrUtils.RoundDB(issPricewTax, 2);

													   }
													   String taxValue = decimalFormat.format(tax);
													  // String taxValue = String.valueOf(tax);
													   String issPricewTaxValue = decimalFormat.format(issPricewTax);
							                   			//String issPricewTaxValue = String.valueOf(issPricewTax);
							                   			String unitpriceValue = decimalFormat.format(unitprice);
							                   			String gstpercentageValue = String.valueOf(gstpercentage);
							                   			//String unitpriceValue = String.valueOf(unitprice);
							                   		
							                   			String issueQtyValue = (String)lineArr.get("issueqty");
							                   			String ordQtyValue = (String)lineArr.get("ordqty");
							                   			String pickQtyValue = (String)lineArr.get("pickqty");
							                   			String reverseQtyValue = (String)lineArr.get("reverseqty");
							                   			//String issuePriceValue = String.valueOf(issuePrice);
							                   			String issuePriceValue = decimalFormat.format(issuePrice);
							                   			
							                   			/*float issuePriceVal ="".equals(issuePriceValue) ? 0.0f :  Float.parseFloat(issuePriceValue);*/
							                   			float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
							                            /*float issPricewTaxVal ="".equals(issPricewTaxValue) ? 0.0f :  Float.parseFloat(issPricewTaxValue);*/
							                            float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
							                            /*float unitpriceVal ="".equals(unitpriceValue) ? 0.0f :  Float.parseFloat(unitpriceValue);*/
							                            float issueQtyVal ="".equals(issueQtyValue) ? 0.0f :  Float.parseFloat(issueQtyValue);
							                            float ordQtyVal ="".equals(ordQtyValue) ? 0.0f :  Float.parseFloat(ordQtyValue);
							                            float pickQtyVal ="".equals(pickQtyValue) ? 0.0f :  Float.parseFloat(pickQtyValue);
							                            float reverseQtyVal ="".equals(reverseQtyValue) ? 0.0f :  Float.parseFloat(reverseQtyValue);
							                            
							                            double unitpriceVal ="".equals(unitpriceValue) ? 0.0d :  Double.parseDouble(unitpriceValue);
							                            double issuePriceVal ="".equals(issuePriceValue) ? 0.0d :  Double.parseDouble(issuePriceValue);
							                            double issPricewTaxVal ="".equals(issPricewTaxValue) ? 0.0d :  Double.parseDouble(issPricewTaxValue);
							                            
							                            /*if(issuePriceVal==0f){
							                            	issuePriceValue="0.00000";
							                            }else{
							                            	issuePriceValue=issuePriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }*/if(issueQtyVal==0f){
							                            	issueQtyValue="0.000";
							                            }else{
							                            	issueQtyValue=issueQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }if(reverseQtyVal==0f){
							                            	reverseQtyValue="0.000";
							                            }else{
							                            	reverseQtyValue=reverseQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }/*if(unitpriceVal==0f){
							                            	unitpriceValue="0.00000";
							                            }else{
							                            	unitpriceValue=unitpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }*/if(taxVal==0f){
							                            	taxValue="0.000";
							                            }else{
							                            	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }/*if(issPricewTaxVal==0f){
							                            	issPricewTaxValue="0.00000";
							                            }else{
							                            	issPricewTaxValue=issPricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }*/if(gstpercentageVal==0f){
							                            	gstpercentageValue="0.00000";
							                            }else{
							                            	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }if(ordQtyVal==0f){
							                            	ordQtyValue="0.000";
							                            }else{
							                            	ordQtyValue=ordQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }if(pickQtyVal==0f){
							                            	pickQtyValue="0.000";
							                            }else{
							                            	pickQtyValue=pickQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							                            }
							                            
							                            unitpriceValue = StrUtils.addZeroes(unitpriceVal, numberOfDecimal);
							                            issuePriceValue = StrUtils.addZeroes(issuePriceVal, numberOfDecimal);
							                            issPricewTaxValue = StrUtils.addZeroes(issPricewTaxVal, numberOfDecimal);
																						        										        
												    
													    HSSFRow row = sheet.createRow(index);
								                                                                                                           
														HSSFCell cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
													    cell.setCellStyle(dataStyle);
														
													    cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("dono"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("InvoiceNo"))));
														cell.setCellStyle(dataStyle);
														
														/*strDate=(String)lineArr.get("toexceltransactiondate");
														SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
														if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
														{
														
														     cell = row.createCell( k++);
															 Calendar c = Calendar.getInstance();
												        	 c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactioncratdate"))));
												        	 dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
												        	 cell.setCellValue(c.getTime());
															 cell.setCellStyle(dataStyleSpecial);
														}
														else
														{
															 cell = row.createCell( k++);
															 Calendar c = Calendar.getInstance();
												        	 c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
												        	 dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
												        	 cell.setCellValue(c.getTime());
															 cell.setCellStyle(dataStyleSpecial);
														}*/
														
														/*cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("transactiondate"))));
														cell.setCellStyle(dataStyle);*/
														
														strDate=(String)lineArr.get("transactiondate");
										        		if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
										        		{
										        			strDate=(String)lineArr.get("transactioncratdate");
										        		}
										        		cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString(strDate)));
														cell.setCellStyle(dataStyle);
															
													    cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("cname"))));
														cell.setCellStyle(dataStyle);
																				   
												     	cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_DEPT_ID"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("batch"))));
														cell.setCellStyle(dataStyle);
																								
														cell = row.createCell( k++);
														//cell.setCellValue(strUtils.formatNum((String)lineArr.get("ordqty")));
														cell.setCellValue(ordQtyValue);
														cell.setCellStyle(dataStyleRightAlign);
																																				
														cell = row.createCell( k++);
														//cell.setCellValue(strUtils.formatNum((String)lineArr.get("pickqty")));
														cell.setCellValue(pickQtyValue);
														cell.setCellStyle(dataStyleRightAlign);
																																						    																
														cell = row.createCell( k++);
														//cell.setCellValue(strUtils.formatNum((String)lineArr.get("issueqty")));
														cell.setCellValue(issueQtyValue);
														cell.setCellStyle(dataStyleRightAlign);
														
														if(DIRTYPE.equals("ISSUE")){
															cell = row.createCell( k++);
															//cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("reverseqty"))));
															cell.setCellValue(reverseQtyValue);
															cell.setCellStyle(dataStyleRightAlign);
															
														 }
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
														cell.setCellStyle(dataStyle);
																											
														 if(DIRTYPE.equals("ISSUEWITHPRICE")){
															    cell = row.createCell(k++);
																cell.setCellValue(unitpriceValue);
																cell.setCellStyle(dataStyleRightAlign);

																cell = row.createCell(k++);
																cell.setCellValue(gstpercentageValue);
																cell.setCellStyle(dataStyleRightAlign);

																cell = row.createCell(k++);
																cell.setCellValue(issuePriceValue);
																cell.setCellStyle(dataStyleRightAlign);

																cell = row.createCell(k++);
																cell.setCellValue(taxValue);
																cell.setCellStyle(dataStyleRightAlign);

																cell = row.createCell(k++);
																cell.setCellValue(issPricewTaxValue);
																cell.setCellStyle(dataStyleRightAlign);
														 }
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("uom"))));
														cell.setCellStyle(dataStyle);
															
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
														cell.setCellStyle(dataStyle);
															
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remark"))));
														cell.setCellStyle(dataStyle);
																				
														index++;
														 if((index-2)%maxRowsPerSheet==0){
															 index = 2;
															 SheetId++;
															 sheet = wb.createSheet("Sheet"+SheetId);
															 styleHeader = createStyleHeader(wb);
															 CompHeader = createCompStyleHeader(wb);
															 sheet = this.createWidthGISummary(sheet,DIRTYPE);
															 sheet = this.createHeaderGISummary(sheet,styleHeader,DIRTYPE);
															 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
															 
														 }
											 
											 }  // end of for loop
												
									 }
									 else if (movQryList.size() < 1) {		
										System.out.println("No Records Found To List");
									}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFSheet createWidthGISummary(HSSFSheet sheet,String type){
					int i = 0;
					try{
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						if(type.equals("ISSUE")){
							sheet.setColumnWidth(i++ ,3200);
						}
						sheet.setColumnWidth(i++ ,3200);
						if(type.equals("ISSUEWITHPRICE")){
							sheet.setColumnWidth(i++, 3200);
							sheet.setColumnWidth(i++, 3200);
							sheet.setColumnWidth(i++, 3200);
							sheet.setColumnWidth(i++, 3200);
							sheet.setColumnWidth(i++, 3500);
						}
						sheet.setColumnWidth(i++ ,4200);
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createHeaderGISummary(HSSFSheet sheet, HSSFCellStyle styleHeader,String type){
					int k = 0;
					try{
						
					HSSFRow rowhead = sheet.createRow(1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ORDER NO"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("GINO"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ISSUED DATE"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("DEPARTMENT"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("CATEGORY"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("BRAND"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("LOCATION"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("BATCH"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ORD QTY"));
					cell.setCellStyle(styleHeader);
										
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("PICK QTY"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ISSUE QTY"));
					cell.setCellStyle(styleHeader);
					
					if(type.equals("ISSUE")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("REVERSE QTY"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("STATUS"));
					cell.setCellStyle(styleHeader);
					
					if(type.equals("ISSUEWITHPRICE")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("UNIT PRICE"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("TAX%"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("ISSUE PRICE"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("TAX"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("TOTAL"));
						cell.setCellStyle(styleHeader);
					}
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("USER"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("REMARK"));
					cell.setCellStyle(styleHeader);
					
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				//---End Added by Bruhan on May 27 2014, Description:To open Goods Issue Summary  in excel powershell format	 
				
        
				//---

			     /****Added by Bruhan on May 27 2014, Description:To open Goods Receipt Summary with price in excel powershell format***
			       **************Modification History*********************************
			              Bruhan on Sep 26 2014, Description: To change transaction date as received date
			    */
				private HSSFWorkbook writeToExcelGRSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 HTReportUtil movHisUtil  = new HTReportUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",taxby="";
						int SheetId =1;
					try{
						
								String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE = strUtils.fString(request.getParameter("TO_DATE")).trim();
					        	String DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE")).trim();
					        	String JOBNO         = strUtils.fString(request.getParameter("JOBNO")).trim();
					        	String USER          = strUtils.fString(request.getParameter("USER")).trim();
					        	String ITEMNO        = strUtils.fString(request.getParameter("ITEM")).trim();
					        	String BATCH       = strUtils.fString(request.getParameter("BATCH")).trim();
					        	String ORDERNO       = strUtils.fString(request.getParameter("ORDERNO")).trim();
					        	String SUPPLIER      = strUtils.fString(request.getParameter("CUSTOMER")).trim();
					        	String TO_ASSIGNEE      = strUtils.fString(request.getParameter("TO_ASSIGNEE")).trim();
					        	String LOANASSIGNEE      = strUtils.fString(request.getParameter("LOANASSIGNEE")).trim();
					        	String ITEMDESC      = strUtils.fString(request.getParameter("DESC")).trim();
					        	String ORDERTYPE= strUtils.fString(request.getParameter("ORDERTYPE")).trim();
					        	String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();
					        	String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID")).trim();
					        	String PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID")).trim();//Resvi
					        	String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID")).trim();
					        	String REASONCODE = strUtils.fString(request.getParameter("REASONCODE")).trim();
					        	String LOC = strUtils.fString(request.getParameter("LOC")).trim();
					        	String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID")).trim();
					        	String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2")).trim();
					        	String LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3")).trim();
					        	String FILTER = strUtils.fString(request.getParameter("sortby"));
					        	String LOCALEXPENSES = strUtils.fString(request.getParameter("LOCALEXPENSES"));
					        	String GRNO = strUtils.fString(request.getParameter("grno"));
								//	String  INVOICENUM = strUtils.fString(request.getParameter("INVOICENUM"));
					        	String reportType ="";
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					        	if (FROM_DATE.length()>5)
					            fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
					        	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length()>5)
					        	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
					        	
					        	PlantMstDAO plantMstDAO = new PlantMstDAO();
					            String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
					        	
					        	
					        	Hashtable ht = new Hashtable();
					        	  if (strUtils.fString(ITEMNO).length() > 0)      ht.put("a.ITEM", ITEMNO);
									if (strUtils.fString(ORDERNO).length() > 0)     ht.put("a.PONO", ORDERNO);
									if(StrUtils.fString(GRNO).length() > 0)  ht.put("a.GRNO",GRNO);
									if (strUtils.fString(SUPPLIER).length() > 0)    ht.put("a.CNAME", SUPPLIER);
									if (strUtils.fString(TO_ASSIGNEE).length() > 0) ht.put("a.CNAME_TO", TO_ASSIGNEE);
									if (strUtils.fString(LOANASSIGNEE).length() > 0)ht.put("a.CNAME_LOAN", LOANASSIGNEE);
									if (strUtils.fString(ORDERTYPE).length() > 0)   ht.put("ORDERTYPE", ORDERTYPE);
									if (strUtils.fString(BATCH).length() > 0)       ht.put("a.BATCH", BATCH);
									if (strUtils.fString(LOC).length() > 0)         ht.put("a.LOC", LOC);
									if(strUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
									if(strUtils.fString(LOC_TYPE_ID2).length() > 0)  ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
									if(strUtils.fString(LOC_TYPE_ID3).length() > 0)  ht.put("LOC_TYPE_ID3",LOC_TYPE_ID3);
									if(strUtils.fString(PRD_TYPE_ID).length() > 0)  ht.put("ITEMTYPE",PRD_TYPE_ID);
							        if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
							        if(strUtils.fString(PRD_DEPT_ID).length() > 0) ht.put("PRD_DEPT_ID",PRD_DEPT_ID);
							        if(strUtils.fString(PRD_CLS_ID).length() > 0)   ht.put("PRD_CLS_ID",PRD_CLS_ID);
							        if(strUtils.fString(REASONCODE).length() > 0)   ht.put("a.REMARK",REASONCODE);
							        if(strUtils.fString(FILTER).length() > 0)   ht.put("FILTER",FILTER);
								    if(strUtils.fString(LOCALEXPENSES).length() > 0)   ht.put("LOCALEXPENSES",LOCALEXPENSES);
									//  if (strUtils.fString(INVOICENUM).length() > 0) ht.put("a.INVOICENO", INVOICENUM);
					           				            

							         taxby=_PlantMstDAO.getTaxBy(PLANT);
	                                 if(LOCALEXPENSES.equalsIgnoreCase("1"))
								        {
								        	
								        	 if(taxby.equalsIgnoreCase("BYORDER"))
												{
								        		 movQryList = movHisUtil.getGoodsRecieveSummaryListlocalexpenses_new(ht, fdate,tdate, DIRTYPE, PLANT,ITEMDESC); 
												}else
												{
													movQryList = movHisUtil.getGoodsRecieveSummaryListByProductGstlocalexpenses(ht, fdate,tdate, DIRTYPE, PLANT,ITEMDESC); 	
												}
								        }else
								        {
								        if(taxby.equalsIgnoreCase("BYORDER"))
										{
								        	movQryList = movHisUtil.getGoodsRecieveSummaryList_new(ht, fdate,tdate, DIRTYPE, PLANT,ITEMDESC); 
										}else
										{
											movQryList = movHisUtil.getGoodsRecieveSummaryListByProductGst(ht, fdate,tdate, DIRTYPE, PLANT,ITEMDESC); 	
										}
								        }
							         
	                                /* if(taxby.equalsIgnoreCase("BYORDER")){
	                                	 movQryList = movHisUtil.getGoodsRecieveSummaryList(ht,fdate, tdate, DIRTYPE, PLANT, ITEMDESC);
					                 }
	                                 else
	                                 {
	                                	 movQryList = movHisUtil.getGoodsRecieveSummaryListByProductGst(ht,fdate, tdate, DIRTYPE, PLANT, ITEMDESC);
	                                 }*/
				             		           
									 Boolean workSheetCreated = true;
									 if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthGRSUMMARY(sheet,DIRTYPE);
											sheet = this.createHeaderGRSUMMARY(sheet,styleHeader,DIRTYPE,PLANT);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
											 float gstpercentage = 0;
												double recvCost = 0, tax = 0, recvCostwTax = 0;
									         double subtotal=0,unitcost=0;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									         DecimalFormat decimalFormat = new DecimalFormat("#.#####");
												decimalFormat.setRoundingMode(RoundingMode.FLOOR);
									         String strDate="";
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												   
												   if(DIRTYPE.equals("RECEIVEWITHCOST")){
													   unitcost = Double.parseDouble((String)lineArr.get("unitcost"));
													  // unitcost = StrUtils.RoundDB(unitcost,2);
													   
													   recvCost = Double.parseDouble((String) lineArr.get("total"));
														//recvCost = StrUtils.RoundDB(recvCost, 2);
													   tax = Double.parseDouble((String) lineArr.get("taxval"));
														String gst = (String) lineArr.get("Tax");
														if (gst.length() > 0) {
															gstpercentage = Float.parseFloat(gst);
														}
														/*tax = (recvCost * gstpercentage) / 100;*/
														//tax = StrUtils.RoundDB(tax, 2);
														recvCostwTax = recvCost + tax;
														//recvCostwTax = StrUtils.RoundDB(recvCostwTax, 2);
												   }
												   
												   //	String taxValue = String.valueOf(tax);
												   	String taxValue =  decimalFormat.format(tax);
						                   			//String recvCostwTaxValue = String.valueOf(recvCostwTax);
						                   			String recvCostwTaxValue =  decimalFormat.format(recvCostwTax);
						                   			String gstpercentageValue = String.valueOf(gstpercentage);
						                   			//String unitCostValue = String.valueOf(unitcost);
						                   			String unitCostValue =  decimalFormat.format(unitcost);
						                   			String recvCostValue =  decimalFormat.format(recvCost);
						                   			//String recvCostValue = String.valueOf(recvCost);
						                   			String ordQtyValue = (String)lineArr.get("ordqty");
						                   			String recQtyValue = (String)lineArr.get("recqty");
						                   			
						                   			/*float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
						                            float recvCostwTaxVal ="".equals(recvCostwTaxValue) ? 0.0f :  Float.parseFloat(recvCostwTaxValue);*/
						                            float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
						                            /*float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
						                            float recvCostVal ="".equals(recvCostValue) ? 0.0f :  Float.parseFloat(recvCostValue);*/
						                            float ordQtyVal ="".equals(ordQtyValue) ? 0.0f :  Float.parseFloat(ordQtyValue);
						                            float recQtyVal ="".equals(recQtyValue) ? 0.0f :  Float.parseFloat(recQtyValue);
						                            
						                            /*if(recvCostVal==0f){
						                            	recvCostValue="0.00000";
						                            }else{
						                            	recvCostValue=recvCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }
						                            if(unitCostVal==0f){
						                            	unitCostValue="0.00000";
						                            }else{
						                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }if(taxVal==0f){
						                            	taxValue="0.000";
						                            }else{
						                            	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }if(recvCostwTaxVal==0f){
						                            	recvCostwTaxValue="0.00000";
						                            }else{
						                            	recvCostwTaxValue=recvCostwTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }*/if(gstpercentageVal==0f){
						                            	gstpercentageValue="0.000";
						                            }else{
						                            	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }if(ordQtyVal==0f){
						                            	ordQtyValue="0.000";
						                            }else{
						                            	ordQtyValue=ordQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }if(recQtyVal==0f){
						                            	recQtyValue="0.000";
						                            }else{
						                            	recQtyValue=recQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						                            }
						                            
						                            double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
						                            double recvCostVal ="".equals(recvCostValue) ? 0.0f :  Double.parseDouble(recvCostValue);
						                            double taxVal ="".equals(taxValue) ? 0.0f :  Double.parseDouble(taxValue);
						                            double recvCostwTaxVal ="".equals(recvCostwTaxValue) ? 0.0f :  Double.parseDouble(recvCostwTaxValue);						                            
						                            
						                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
						                            recvCostValue = StrUtils.addZeroes(recvCostVal, numberOfDecimal);
						                            taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
						                            recvCostwTaxValue = StrUtils.addZeroes(recvCostwTaxVal, numberOfDecimal);
						                            

						                            String currencyid = (String)lineArr.get("CURRENCYID");
						                            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
						                            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
						                            
						                            rcvCostconv = recvCostVal * currencyseqt; 
						                            String SrcvCostconv= String.valueOf(rcvCostconv);
//						                            currencyid = "";
						                            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
						                            
						                            recvCostwTaxConv = recvCostwTaxVal * currencyseqt; 
						                            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
						                            String exchangerate = String.format("%.2f", currencyseqt);
						                            
						                            taxValcon = taxVal  * currencyseqt; 
						                            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
						                            
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+2)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pono"))));
													cell.setCellStyle(dataStyle);
													
													  cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("grno"))));
														cell.setCellStyle(dataStyle);
													
													/*strDate=(String)lineArr.get("toexceltransactiondate");
													
													if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
													{
														  SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
														 cell = row.createCell( k++);
														 Calendar c = Calendar.getInstance();
											        	 c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactioncratdate"))));
											        	 dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
											        	 cell.setCellValue(c.getTime());
														 cell.setCellStyle(dataStyleSpecial);
													}
													else
													{
														 SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
														 cell = row.createCell( k++);
														 Calendar c = Calendar.getInstance();
											        	 c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
											        	 dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
											        	 cell.setCellValue(c.getTime());
														 cell.setCellStyle(dataStyleSpecial);
													}*/
													
													/*cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("transactiondate"))));
													cell.setCellStyle(dataStyle);*/
													
														strDate=(String)lineArr.get("transactiondate");
										        		if(strDate.equals("1900-01-01")|| strDate.equals("")||strDate==null)
										        		{
										        			strDate=(String)lineArr.get("transactioncratdate");
										        		}
										        		
										        		cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString(strDate)));
														cell.setCellStyle(dataStyle);
														
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("cname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_DEPT_ID"))));//RESVI
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("batch"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
													cell.setCellStyle(dataStyle);
																							
													cell = row.createCell( k++);
													cell.setCellValue(ordQtyValue);
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													//cell.setCellValue(strUtils.formatNum((String)lineArr.get("pickqty")));
													cell.setCellValue(recQtyValue);
													cell.setCellStyle(dataStyleRightAlign);
																							
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("uom"))));
													cell.setCellStyle(dataStyle);
																										
													 if(DIRTYPE.equals("RECEIVEWITHCOST")){
														cell = row.createCell( k++);
														cell.setCellValue(unitCostValue);
														cell.setCellStyle(dataStyleRightAlign);
																												
														cell = row.createCell(k++);
														cell.setCellValue(gstpercentageValue);
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell(k++);
														cell.setCellValue(exchangerate);
														cell.setCellStyle(dataStyle);

														cell = row.createCell(k++);
														cell.setCellValue(recvCostValue);
														cell.setCellStyle(dataStyleRightAlign);
														
														cell = row.createCell(k++);
														cell.setCellValue(SrcvCostconv);
														cell.setCellStyle(dataStyleRightAlign);

														cell = row.createCell(k++);
														cell.setCellValue(taxValue);
														cell.setCellStyle(dataStyleRightAlign);
														
														cell = row.createCell(k++);
														cell.setCellValue(StaxValcon);
														cell.setCellStyle(dataStyleRightAlign);

														cell = row.createCell(k++);
														cell.setCellValue(recvCostwTaxValue);
														cell.setCellStyle(dataStyleRightAlign);
														
														cell = row.createCell(k++);
														cell.setCellValue(SrecvCostwTaxconv);
														cell.setCellStyle(dataStyleRightAlign);
														
													 }
													 
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
													cell.setCellStyle(dataStyle);													
														
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remark"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 /*if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 sheet = wb.createSheet();*/
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthGRSUMMARY(sheet,DIRTYPE);
														 sheet = this.createHeaderGRSUMMARY(sheet,styleHeader,DIRTYPE,PLANT);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFSheet createWidthGRSUMMARY(HSSFSheet sheet,String type){
					int i = 0;
					try{
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						 if(type.equals("RECEIVEWITHCOST")){
								sheet.setColumnWidth(i++, 3200);
								sheet.setColumnWidth(i++, 3200);
								sheet.setColumnWidth(i++, 3200);
								sheet.setColumnWidth(i++, 4000);
								sheet.setColumnWidth(i++, 4000);
								sheet.setColumnWidth(i++, 4000);
								sheet.setColumnWidth(i++, 4000);
								sheet.setColumnWidth(i++, 4000);
								sheet.setColumnWidth(i++, 4000);
						 }
						sheet.setColumnWidth(i++ ,4200);
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createHeaderGRSUMMARY(HSSFSheet sheet, HSSFCellStyle styleHeader, String type,String plant){
					int k = 0;
					try{
					String currency=_PlantMstDAO.getBaseCurrency(plant);
						
					HSSFRow rowhead = sheet.createRow(1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ORDER NO"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("GRNO"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("RECEIVED DATE"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("SUPPLIER NAME"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("DEPARTMENT"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("CATEGORY"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("BRAND"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("LOC"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("BATCH"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("EXPIRED DATE"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("ORDER QTY"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("RECEIVE QTY"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					 if(type.equals("RECEIVEWITHCOST")){
						 cell = rowhead.createCell( k++);
						 cell.setCellValue(new HSSFRichTextString("UNIT COST"));
						 cell.setCellStyle(styleHeader);
					 
						 cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("TAX%"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("EXCHANGE RATE"));
							cell.setCellStyle(styleHeader);

							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("RECEIVE COST ("+currency+")"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("RECEIVE COST"));
							cell.setCellStyle(styleHeader);

							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("TAX ("+currency+")"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("TAX"));
							cell.setCellStyle(styleHeader);

							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("TOTAL ("+currency+")"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell(k++);
							cell.setCellValue(new HSSFRichTextString("TOTAL"));
							cell.setCellStyle(styleHeader);
					 }
					 
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("USER"));
					cell.setCellStyle(styleHeader);
										
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("REMARK"));
					cell.setCellStyle(styleHeader);
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
	  private HSSFCellStyle createDataStyleRightAlign(HSSFWorkbook wb){
					
					//Create style
					  HSSFCellStyle dataStyle = wb.createCellStyle();
					  dataStyle.setWrapText(true);
					  dataStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					  return dataStyle;
				}
	  
	  private HSSFWorkbook writeToBankDetailsExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			int maxRowsPerSheet = 65535;
		
			try{
				List listQry = new ArrayList();
				StrUtils strUtils = new StrUtils();
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				int SheetNo =1;	
				// String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
				 // String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
				 String BANK_BRANCH_CODE  = _StrUtils.fString(request.getParameter("BANK_BRANCH_CODE"));
				
				 
			    	 listQry = new BankUtil().getToBank(BANK_BRANCH_CODE, plant);
			     
					
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							dataStyle = createDataStyle(wb);
							 sheet = wb.createSheet("Sheet"+SheetNo);
							 styleHeader = createStyleHeader(wb);
							 sheet = this.createBankWidth(sheet);
							 sheet = this.createBankHeader(sheet,styleHeader);
							 int index = 1;
							 	//String customerType="";
							    //String customerStatus="";
							 if (listQry.size() > 0) {
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								
									   Map lineArr = (Map) listQry.get(iCnt);	
									    int k = 0;
									    
									    HSSFRow row = sheet.createRow(index);
									  
									    HSSFCell cell = row.createCell( k++);
									    /*if(reportType.equalsIgnoreCase("Supplier")){*/
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NAME"))));
										cell.setCellStyle(dataStyle);
										
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BRANCH"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BRANCH_NAME"))));
										cell.setCellStyle(dataStyle);
		
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SWIFT_CODE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("IFSC_CODE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TELNO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("HPNO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FAX"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMAIL"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WEBSITE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UNITNO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BUILDING"))));
										cell.setCellStyle(dataStyle);
										
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STREET"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CITY"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STATE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COUNTRY"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ZIP"))));
										cell.setCellStyle(dataStyle);
																					
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FACEBOOKID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TWITTERID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LINKEDINID"))));
										cell.setCellStyle(dataStyle);
											
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CONTACT_PERSON"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NOTE"))));
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ISACTIVE"))));
										cell.setCellStyle(dataStyle);
	
										 index++;
										 if((index-1)%maxRowsPerSheet==0){
											 index = 1;
											 SheetNo++;
											 sheet = wb.createSheet("Sheet_"+SheetNo);
											 styleHeader = createStyleHeader(wb);
											 sheet = this.createWidth(sheet);
											 sheet = this.createHeader(sheet,styleHeader,plant);
											 
										 }
										

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
				     
	  private HSSFWorkbook writeToPContcatDetailsExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
						String plant = "";
						int maxRowsPerSheet = 65535;
					
						try{
							List listQry = new ArrayList();
							StrUtils strUtils = new StrUtils();
							HttpSession session = request.getSession();
							PlantMstDAO plantMstDAO = new PlantMstDAO();
							plant = session.getAttribute("PLANT").toString();
							int SheetNo =1;	
							 String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
							 // String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
							 String CUST_CODE  = _StrUtils.fString(request.getParameter("CUST_NAME"));
							 String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
							
							 if(reportType.equalsIgnoreCase("Supplier")){
								  String SUPPLIER_TYPE_ID     = strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID")).trim();
								  listQry = new CustUtil().getVendorListStartsWithName(CUST_CODE, plant,SUPPLIER_TYPE_ID);
						     }else if(reportType.equalsIgnoreCase("Customer")){
						    	 String CUSTOMER_TYPE_ID     = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID")).trim();
						    	 //listQry = new CustUtil().getCustomerListStartsWithName(CUST_CODE, plant,CUSTOMER_TYPE_ID);
						    	 listQry = new CustUtil().getCustomerListWithType(CUST_CODE, plant,CUSTOMER_TYPE_ID);
						     }else if(reportType.equalsIgnoreCase("Rental and Service Customer")){
						    	 listQry = new CustUtil().getLoanAssigneeListStartsWithName("",CUST_CODE,plant,"");
						     }else if(reportType.equalsIgnoreCase("Transfer Order Customer")){
						    	 listQry = new CustUtil().getToAssingeListStartsWithName(CUST_CODE, plant);
						     }
								
										HSSFSheet sheet = null ;
										HSSFCellStyle styleHeader = null;
										HSSFCellStyle dataStyle = null;
										dataStyle = createDataStyle(wb);
										 sheet = wb.createSheet("Sheet"+SheetNo);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createPContactsWidth(sheet,reportType);
										 sheet = this.createPConactsHeader(sheet,styleHeader,reportType,COMP_INDUSTRY);
										 int index = 1;
										 	String customerType="";
										    String customerStatus="";
										    String transport="";
										 if (listQry.size() > 0) {
										 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
											
												   Map lineArr = (Map) listQry.get(iCnt);	
												    int k = 0;
												    
												    HSSFRow row = sheet.createRow(index);
												  
												    HSSFCell cell = row.createCell( k++);
												    if(reportType.equalsIgnoreCase("Supplier")){
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VENDNO"))));
													cell.setCellStyle(dataStyle);
													
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VNAME"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CURRENCY_ID"))));
													cell.setCellStyle(dataStyle);
													
												    customerType=(String)lineArr.get("SUPPLIER_TYPE_ID");
													cell = row.createCell( k++);
													if(customerType.equals(""))
													{
														cell.setCellValue("NOSUPPLIERTYPE");
													}
													else
													{
													     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SUPPLIER_TYPE_ID"))));
													}
													cell.setCellStyle(dataStyle);
												    }
												    if(reportType.equalsIgnoreCase("Supplier")){
												    	TransportModeDAO transportmodedao = new TransportModeDAO();
														transport = (String)lineArr.get("TRANSPORTID");
														int trans = Integer.valueOf(transport);
														if(trans > 0){
															String transportmode = transportmodedao.getTransportModeById(plant,trans);
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(transportmode));
															cell.setCellStyle(dataStyle);
														}
														else
														{
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(""));
															cell.setCellStyle(dataStyle);
														}
												    }
												    if(reportType.equalsIgnoreCase("Customer")){
													    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTNO"))));
														cell.setCellStyle(dataStyle);
														
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CNAME"))));
														cell.setCellStyle(dataStyle);
														
														if(COMP_INDUSTRY.equals("Education")){
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COMPANYREGNUMBER"))));
															cell.setCellStyle(dataStyle);
															
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DATEOFBIRTH"))));
															cell.setCellStyle(dataStyle);
															
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NATIONALITY"))));
															cell.setCellStyle(dataStyle);
														}
														
														if(!COMP_INDUSTRY.equals("Education")){
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CURRENCY_ID"))));
														cell.setCellStyle(dataStyle);

														customerType=(String)lineArr.get("CUSTOMER_TYPE_ID");
														cell = row.createCell( k++);
														if(customerType.equals(""))
														{
															cell.setCellValue("NOCUSTOMERTYPE");
														}
														else
														{
														     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTOMER_TYPE_ID"))));
														}
														cell.setCellStyle(dataStyle);
														}
													}
												    if(reportType.equalsIgnoreCase("Customer")){
												    	if(!COMP_INDUSTRY.equals("Education")){
												    	TransportModeDAO transportmodedao = new TransportModeDAO();
														transport = (String)lineArr.get("TRANSPORTID");
														int trans = Integer.valueOf(transport);
														if(trans > 0){
															String transportmode = transportmodedao.getTransportModeById(plant,trans);
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(transportmode));
															cell.setCellStyle(dataStyle);
														}
														else
														{
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(""));
															cell.setCellStyle(dataStyle);
														}
												    	}
												    }
												    if(reportType.equalsIgnoreCase("Transfer Order Customer")){
													    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ASSIGNENO"))));
														cell.setCellStyle(dataStyle);
														
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ASSIGNENAME"))));
														cell.setCellStyle(dataStyle);
												    }
												    if(reportType.equalsIgnoreCase("Rental and Service Customer")){
													    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LASSIGNNO"))));
														cell.setCellStyle(dataStyle);
														
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CNAME"))));
														cell.setCellStyle(dataStyle);
													}
												    if(!COMP_INDUSTRY.equals("Education")){
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TELNO"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FAX"))));
													cell.setCellStyle(dataStyle);
												    }
													
												    if(reportType.equalsIgnoreCase("Customer") || reportType.equalsIgnoreCase("Supplier")){
												    	if(!COMP_INDUSTRY.equals("Education")){
												    	cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTOMEREMAIL"))));
														cell.setCellStyle(dataStyle);
														 
													 	cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WEBSITE"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TAXTREATMENT"))));
														cell.setCellStyle(dataStyle);

														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("RCBNO"))));
														cell.setCellStyle(dataStyle);
												    	}
//														cell = row.createCell( k++);
//														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OPENINGBALANCE"))));
//														cell.setCellStyle(dataStyle);
													 
														customerStatus=(String)lineArr.get("PAYMENT_TERMS");
														cell = row.createCell( k++);
														if(customerStatus.equals(""))
														{
															cell.setCellValue("");
														}
														else
														{
														     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PAYMENT_TERMS"))));
														}
														cell.setCellStyle(dataStyle);
														if(!COMP_INDUSTRY.equals("Education")){
														customerStatus=(String)lineArr.get("DAYS");
														cell = row.createCell( k++);
														if(customerStatus.equals(""))
														{
															cell.setCellValue("0");
														}
														else
														{
														     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DAYS"))));
														}
														cell.setCellStyle(dataStyle);
														}
													if(reportType.equalsIgnoreCase("Customer")){
														if(!COMP_INDUSTRY.equals("Education")){
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CREDITLIMIT"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CREDIT_LIMIT_BY"))));
													cell.setCellStyle(dataStyle);
														}
													}
													 	cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FACEBOOKID"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TWITTERID"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LINKEDINID"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SKYPEID"))));
														cell.setCellStyle(dataStyle);
														
												 }
												    if(!COMP_INDUSTRY.equals("Education")){
												    cell = row.createCell( k++);
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NAME"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DESGINATION"))));
													cell.setCellStyle(dataStyle);
												    }
													if(reportType.equalsIgnoreCase("Customer") || reportType.equalsIgnoreCase("Supplier")){	
														if(!COMP_INDUSTRY.equals("Education")){
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WORKPHONE"))));
													cell.setCellStyle(dataStyle);
														}
													}
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("HPNO"))));
													cell.setCellStyle(dataStyle);					
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMAIL"))));
													cell.setCellStyle(dataStyle);

													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COUNTRY"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR1"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR2"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR3"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR4"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STATE"))));
													cell.setCellStyle(dataStyle);													
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ZIP"))));
													cell.setCellStyle(dataStyle);
													if(!COMP_INDUSTRY.equals("Education")){
												    cell = row.createCell( k++);
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPCONTACTNAME"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPDESGINATION"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPWORKPHONE"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPHPNO"))));
													cell.setCellStyle(dataStyle);					
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPEMAIL"))));
													cell.setCellStyle(dataStyle);

													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPCOUNTRY_CODE"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPADDR1"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPADDR2"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPADDR3"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPADDR4"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPSTATE"))));
													cell.setCellStyle(dataStyle);													
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SHIPZIP"))));
													cell.setCellStyle(dataStyle);
													}
													if(reportType.equalsIgnoreCase("Customer") || reportType.equalsIgnoreCase("Supplier")){
													
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("IBAN"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BANKNAME"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BRANCH"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BANKROUTINGCODE"))));
														cell.setCellStyle(dataStyle);
													}
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARKS"))));
													cell.setCellStyle(dataStyle);
												
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ISACTIVE"))));
													cell.setCellStyle(dataStyle);
																								
													/*cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PAYMENT_TERMS"))));
													cell.setCellStyle(dataStyle);*/
														
													
													/*if(reportType.equalsIgnoreCase("Customer")){
														customerStatus=(String)lineArr.get("CUSTOMER_STATUS_ID");
														cell = row.createCell( k++);
														if(customerStatus.equals(""))
														{
															cell.setCellValue("NOCUSTOMERSTATUS");
														}
														else
														{
														     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTOMER_STATUS_ID"))));
														}
														cell.setCellStyle(dataStyle);
													}*/
														
													 index++;
													 if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 SheetNo++;
														 sheet = wb.createSheet("Sheet_"+SheetNo);
														 styleHeader = createStyleHeader(wb);
														 sheet = this.createWidth(sheet);
														 sheet = this.createHeader(sheet,styleHeader,plant);
														 
													 }
													

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
	  
	  private HSSFWorkbook writeToEmployeeDetailsExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			int maxRowsPerSheet = 65535;
		
			try{
				List listQry = new ArrayList();
				StrUtils strUtils = new StrUtils();
				HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO(); 
				EmployeeDAO employeeDAO = new EmployeeDAO();
		        OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				int SheetNo =1;	
				 String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
				 String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
				 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
				 
				 listQry = new EmployeeUtil().getEmployeeListStartsWithName(CUST_CODE, plant);
				
				
					
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							dataStyle = createDataStyle(wb);
							 sheet = wb.createSheet("Sheet"+SheetNo);
							 styleHeader = createStyleHeader(wb);
							 sheet = this.createEmployeeWidth(sheet,reportType);
							 sheet = this.createEmployeeHeader(sheet,styleHeader,reportType);
							 int index = 1;
								String customerType="";
							    String customerStatus="";
							    
							 if (listQry.size() > 0) {
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								
									   Map lineArr = (Map) listQry.get(iCnt);	
									    int k = 0;
									    
									    HSSFRow row = sheet.createRow(index);
									  
									    HSSFCell cell = row.createCell( k++);
									    
									    String BASICSALARY = StrUtils.fString((String)lineArr.get("BASICSALARY"));
			                        	String HOUSERENTALLOWANCE = StrUtils.fString((String)lineArr.get("HOUSERENTALLOWANCE"));
			                        	String TRANSPORTALLOWANCE = StrUtils.fString((String)lineArr.get("TRANSPORTALLOWANCE"));
			                        	String COMMUNICATIONALLOWANCE = StrUtils.fString((String)lineArr.get("COMMUNICATIONALLOWANCE"));
			                        	String OTHERALLOWANCE = StrUtils.fString((String)lineArr.get("OTHERALLOWANCE"));
			                        	String BONUS = StrUtils.fString((String)lineArr.get("BONUS"));
			                        	String COMMISSION = StrUtils.fString((String)lineArr.get("COMMISSION"));
			                        	
			                        	String GRATUITY = StrUtils.fString((String)lineArr.get("GRATUITY"));
			                        	String AIRTICKET = StrUtils.fString((String)lineArr.get("AIRTICKET"));
			                        	String LEAVESALARY = StrUtils.fString((String)lineArr.get("LEAVESALARY"));
			                        	
				                        float BASICSALARYVal ="".equals(BASICSALARY) ? 0.0f :  Float.parseFloat(BASICSALARY);
			                            if(BASICSALARYVal==0f){
			                            	BASICSALARY="0.00000";
			                            }else{
			                            	BASICSALARY=BASICSALARY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            BASICSALARY = StrUtils.addZeroes(Double.parseDouble(BASICSALARY), numberOfDecimal);
			                            
			                            float HOUSERENTALLOWANCEVal ="".equals(HOUSERENTALLOWANCE) ? 0.0f :  Float.parseFloat(HOUSERENTALLOWANCE);
			                            if(HOUSERENTALLOWANCEVal==0f){
			                            	HOUSERENTALLOWANCE="0.00000";
			                            }else{
			                            	HOUSERENTALLOWANCE=HOUSERENTALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            HOUSERENTALLOWANCE = StrUtils.addZeroes(Double.parseDouble(HOUSERENTALLOWANCE), numberOfDecimal);
			                            
			                            float TRANSPORTALLOWANCEVal ="".equals(TRANSPORTALLOWANCE) ? 0.0f :  Float.parseFloat(TRANSPORTALLOWANCE);
			                            if(TRANSPORTALLOWANCEVal==0f){
			                            	TRANSPORTALLOWANCE="0.00000";
			                            }else{
			                            	TRANSPORTALLOWANCE=TRANSPORTALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            TRANSPORTALLOWANCE = StrUtils.addZeroes(Double.parseDouble(TRANSPORTALLOWANCE), numberOfDecimal);
			                            
			                            float COMMUNICATIONALLOWANCEVal ="".equals(COMMUNICATIONALLOWANCE) ? 0.0f :  Float.parseFloat(COMMUNICATIONALLOWANCE);
			                            if(COMMUNICATIONALLOWANCEVal==0f){
			                            	COMMUNICATIONALLOWANCE="0.00000";
			                            }else{
			                            	COMMUNICATIONALLOWANCE=COMMUNICATIONALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            COMMUNICATIONALLOWANCE = StrUtils.addZeroes(Double.parseDouble(COMMUNICATIONALLOWANCE), numberOfDecimal);
			                            
			                            float OTHERALLOWANCEVal ="".equals(OTHERALLOWANCE) ? 0.0f :  Float.parseFloat(OTHERALLOWANCE);
			                            if(OTHERALLOWANCEVal==0f){
			                            	OTHERALLOWANCE="0.00000";
			                            }else{
			                            	OTHERALLOWANCE=OTHERALLOWANCE.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            OTHERALLOWANCE = StrUtils.addZeroes(Double.parseDouble(OTHERALLOWANCE), numberOfDecimal);
			                            
			                            float BONUSVal ="".equals(BONUS) ? 0.0f :  Float.parseFloat(BONUS);
			                            if(BONUSVal==0f){
			                            	BONUS="0.00000";
			                            }else{
			                            	BONUS=BONUS.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            BONUS = StrUtils.addZeroes(Double.parseDouble(BONUS), numberOfDecimal);
			                            
			                            float COMMISSIONVal ="".equals(COMMISSION) ? 0.0f :  Float.parseFloat(COMMISSION);
			                            if(COMMISSIONVal==0f){
			                            	COMMISSION="0.00000";
			                            }else{
			                            	COMMISSION=COMMISSION.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            COMMISSION = StrUtils.addZeroes(Double.parseDouble(COMMISSION), numberOfDecimal);
			                            
			                            float GRATUITYVal ="".equals(GRATUITY) ? 0.0f :  Float.parseFloat(GRATUITY);
			                            if(GRATUITYVal==0f){
			                            	GRATUITY="0.00000";
			                            }else{
			                            	GRATUITY=GRATUITY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            GRATUITY = StrUtils.addZeroes(Double.parseDouble(GRATUITY), numberOfDecimal);
			                            
			                            float AIRTICKETVal ="".equals(AIRTICKET) ? 0.0f :  Float.parseFloat(AIRTICKET);
			                            if(AIRTICKETVal==0f){
			                            	AIRTICKET="0.00000";
			                            }else{
			                            	AIRTICKET=AIRTICKET.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            AIRTICKET = StrUtils.addZeroes(Double.parseDouble(AIRTICKET), numberOfDecimal);
			                            
			                            float LEAVESALARYVal ="".equals(LEAVESALARY) ? 0.0f :  Float.parseFloat(LEAVESALARY);
			                            if(LEAVESALARYVal==0f){
			                            	LEAVESALARY="0.00000";
			                            }else{
			                            	LEAVESALARY=LEAVESALARY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }                            
			                            LEAVESALARY = StrUtils.addZeroes(Double.parseDouble(LEAVESALARY), numberOfDecimal);
									   
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPNO"))));
										cell.setCellStyle(dataStyle);
											
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FNAME"))));
										cell.setCellStyle(dataStyle);
										
										String emptypeget = StrUtils.fString((String)lineArr.get("EMPLOYEETYPEID"));
										String emptypeshow = "-";
										if(!emptypeget.equalsIgnoreCase("0")) {
											emptypeshow = hrEmpTypeDAO.getEmployeetypeusingId(plant, Integer.parseInt(emptypeget));
										}
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(emptypeshow));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("GENDER"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DOB"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TELNO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMAIL"))));
										cell.setCellStyle(dataStyle);
								
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PASSPORTNUMBER"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COUNTRYOFISSUE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PASSPORTEXPIRYDATE"))));
										cell.setCellStyle(dataStyle);
										
										String reportinginchargeid = StrUtils.fString((String)lineArr.get("REPORTING_INCHARGE"));
										String reportinginchargename = employeeDAO.getEmpnamebyid(plant, reportinginchargeid, "");
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(reportinginchargename));
//										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REPORTING_INCHARGE"))));
										cell.setCellStyle(dataStyle);
										
										String Outletid = StrUtils.fString((String)lineArr.get("OUTLET"));
										String Outletname = outletBeanDAO.getOutletname(plant, Outletid, "");
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(Outletname));
//										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OUTLET"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COUNTRY"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR1"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR2"))));
										cell.setCellStyle(dataStyle);
																			
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR3"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ADDR4"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STATE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ZIP"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("FACEBOOKID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TWITTERID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LINKEDINID"))));
										cell.setCellStyle(dataStyle);
																			
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SKYPEID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMIRATESID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMIRATESIDEXPIRY"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VISANUMBER"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VISAEXPIRYDATE"))));
										cell.setCellStyle(dataStyle);									
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DEPT"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DESGINATION"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DATEOFJOINING"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DATEOFLEAVING"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LABOURCARDNUMBER"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WORKPERMITNUMBER"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CONTRACTSTARTDATE"))));
										cell.setCellStyle(dataStyle);
																			
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CONTRACTENDDATE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("IBAN"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BANKNAME"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BRANCH"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BANKROUTINGCODE"))));
										cell.setCellStyle(dataStyle);
										
										/*cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NUMBEROFMANDAYS"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(BASICSALARY));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(HOUSERENTALLOWANCE));
										cell.setCellStyle(dataStyle);
																			
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(TRANSPORTALLOWANCE));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(COMMUNICATIONALLOWANCE));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
									    cell.setCellValue(new HSSFRichTextString(OTHERALLOWANCE));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(BONUS));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(COMMISSION));
										cell.setCellStyle(dataStyle);*/
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(GRATUITY));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(AIRTICKET));
										cell.setCellStyle(dataStyle);
																			
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(LEAVESALARY));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARKS"))));
										cell.setCellStyle(dataStyle);
											/*if(reportType.equalsIgnoreCase("Employee")){
												customerStatus=(String)lineArr.get("EMPLOYEE_STATUS_ID");
												cell = row.createCell( k++);
												if(customerStatus.equals(""))
												{
													cell.setCellValue("NOEMPLOYEESTATUS");
												}
												else
												{
												     cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPLOYEE_STATUS_ID"))));
												}
												cell.setCellStyle(dataStyle);
											}*/
									
										cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("IsActive"))));
										cell.setCellStyle(dataStyle);
										
											
										 index++;
										 if((index-1)%maxRowsPerSheet==0){
											 index = 1;
											 SheetNo++;
											 sheet = wb.createSheet("Sheet_"+SheetNo);
											 styleHeader = createStyleHeader(wb);
											 sheet = this.createWidth(sheet);
											 sheet = this.createHeader(sheet,styleHeader,plant);
											 
										 }
										

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
	  private HSSFSheet createBankHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow( 0);
				HSSFCell cell = rowhead.createCell( k++);
				/*if(ReportType.equalsIgnoreCase("Transfer Order Customer")){*/
					cell.setCellValue(new HSSFRichTextString("Bank Name"));
					cell.setCellStyle(styleHeader);
				/*}else{*/
				//	cell.setCellValue(new HSSFRichTextString(ReportType+" ID"));
					//cell.setCellStyle(styleHeader);
				//}														
				
			/*	if(ReportType.equalsIgnoreCase("Transfer Order Customer")){*/
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Branch Code"));
				cell.setCellStyle(styleHeader);
		
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Branch Name"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Swift Code"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("IFSC Code"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Telephone"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Handphone"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Fax"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Email"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Website"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Unit No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Building"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Street"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("City"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("State"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Country"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Postal Code"));
			cell.setCellStyle(styleHeader);
								
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Facebook Id"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Twitter Id"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("LinkedIn Id"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Contact Person"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Is Active"));
			cell.setCellStyle(styleHeader);
			

			
			
		
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
				 
				 private HSSFSheet createPConactsHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String ReportType,String COMP_INDUSTRY){
						int k = 0;
						try{
						
						
						HSSFRow rowhead = sheet.createRow( 0);
							HSSFCell cell = rowhead.createCell( k++);
							if(ReportType.equalsIgnoreCase("Transfer Order Customer")){
								cell.setCellValue(new HSSFRichTextString("Consignment Order Customer ID"));
								cell.setCellStyle(styleHeader);
							}else{
								cell.setCellValue(new HSSFRichTextString(ReportType+" ID"));
								cell.setCellStyle(styleHeader);
							}														
							
							if(ReportType.equalsIgnoreCase("Transfer Order Customer")){
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Consignment Order Customer Name"));
							cell.setCellStyle(styleHeader);
							}else{
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(ReportType+" Name"));
								cell.setCellStyle(styleHeader);
							}
							if(COMP_INDUSTRY.equals("Education")){
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString("IC/FIN Number"));
								cell.setCellStyle(styleHeader);
								
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString("Date Of Birth"));
								cell.setCellStyle(styleHeader);
								
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString("Nationality"));
								cell.setCellStyle(styleHeader);
							}
						//if(ReportType.equalsIgnoreCase("Customer")){
							
							if(ReportType.equalsIgnoreCase("Customer")){
								if(!COMP_INDUSTRY.equals("Education")){
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString("Customer Currency"));
								cell.setCellStyle(styleHeader);
							}	
							}
							
							if(ReportType.equalsIgnoreCase("Supplier")){
								cell = rowhead.createCell( k++);
								cell.setCellValue(new HSSFRichTextString("Supplier Currency"));
								cell.setCellStyle(styleHeader);
								
							}
							
						
						if(ReportType.equalsIgnoreCase("Customer")){
							if(!COMP_INDUSTRY.equals("Education")){
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Customer Type"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Transport Mode"));
							cell.setCellStyle(styleHeader);
							}
						}
					
					if(ReportType.equalsIgnoreCase("Supplier")){
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Supplier Type"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Transport Mode"));
							cell.setCellStyle(styleHeader);
							
						}
					
					if(ReportType.equalsIgnoreCase("Customer")){
						if(!COMP_INDUSTRY.equals("Education")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Customer Phone"));
						cell.setCellStyle(styleHeader);
						}
					}else if(ReportType.equalsIgnoreCase("Supplier")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Supplier Phone"));
						cell.setCellStyle(styleHeader);
					}else{
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Telephone"));
						cell.setCellStyle(styleHeader);
					}
					if(!COMP_INDUSTRY.equals("Education")){
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Fax"));
					cell.setCellStyle(styleHeader);
					}
					if(ReportType.equalsIgnoreCase("Customer")){
						if(!COMP_INDUSTRY.equals("Education")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Customer Email"));
						cell.setCellStyle(styleHeader);
						}
					}else if(ReportType.equalsIgnoreCase("Supplier")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Supplier Email"));
						cell.setCellStyle(styleHeader);
					}
					
					if(ReportType.equalsIgnoreCase("Customer") || ReportType.equalsIgnoreCase("Supplier")){
						if(!COMP_INDUSTRY.equals("Education")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Website"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("TRN/GST NO"));
						cell.setCellStyle(styleHeader);

//						cell = rowhead.createCell( k++);
//						cell.setCellValue(new HSSFRichTextString("Opening Balance"));
//						cell.setCellStyle(styleHeader);
						}
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Payment Type"));
						cell.setCellStyle(styleHeader);
						
						if(!COMP_INDUSTRY.equals("Education")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Number of Days (For ageing report)"));
						cell.setCellStyle(styleHeader);
						}
						if(ReportType.equalsIgnoreCase("Customer")){
							if(!COMP_INDUSTRY.equals("Education")){
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Credit Limit"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Credit Limit By"));
							cell.setCellStyle(styleHeader);
							}
							}
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Facebook Id"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Twitter Id"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("LinkedIn Id"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Skype Id"));
						cell.setCellStyle(styleHeader);
						
					}
					
					if(!COMP_INDUSTRY.equals("Education")){			
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Contact Name"));
						cell.setCellStyle(styleHeader);
											
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Designation"));
						cell.setCellStyle(styleHeader);
					}
						if(ReportType.equalsIgnoreCase("Customer") || ReportType.equalsIgnoreCase("Supplier")){
							if(!COMP_INDUSTRY.equals("Education")){
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Work Phone"));
							cell.setCellStyle(styleHeader);
							}
						}
												
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Mobile"));
						cell.setCellStyle(styleHeader);
					
						if(ReportType.equalsIgnoreCase("Customer") || ReportType.equalsIgnoreCase("Supplier")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Contact Person Email"));
						cell.setCellStyle(styleHeader);
						} else{
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Email"));
							cell.setCellStyle(styleHeader);
						}
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Country"));
						cell.setCellStyle(styleHeader);						
												
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Unit No"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Building"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Street"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("City"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("State"));
						cell.setCellStyle(styleHeader);						
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Postal Code"));
						cell.setCellStyle(styleHeader);
						if(!COMP_INDUSTRY.equals("Education")){
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Contact Name"));
						cell.setCellStyle(styleHeader);
											
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Designation"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Work Phone"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Mobile"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Email"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Country"));
						cell.setCellStyle(styleHeader);						
												
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Unit No"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Building"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Street"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship City"));
						cell.setCellStyle(styleHeader);

						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship State"));
						cell.setCellStyle(styleHeader);						
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Ship Postal Code"));
						cell.setCellStyle(styleHeader);
						}
						if(ReportType.equalsIgnoreCase("Customer") || ReportType.equalsIgnoreCase("Supplier")){
						
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("IBAN"));
							cell.setCellStyle(styleHeader);

							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Bank"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Branch"));
							cell.setCellStyle(styleHeader);

							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Routing Code"));
							cell.setCellStyle(styleHeader);
							
						}
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Remarks"));
						cell.setCellStyle(styleHeader);
 						
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Is Active"));
						cell.setCellStyle(styleHeader);
						
							
						/*if(ReportType.equalsIgnoreCase("Customer")){
							 cell = rowhead.createCell( k++);
							 cell.setCellValue(new HSSFRichTextString("CustomerStatus"));
							 cell.setCellStyle(styleHeader);
						 }*/
					
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}
					private HSSFSheet createPContactsWidth(HSSFSheet sheet,String ReportType){
						
						try{
							sheet.setColumnWidth(0 ,5500);
							sheet.setColumnWidth(1 ,8000);
							
						
							if(ReportType.equalsIgnoreCase("Customer") || ReportType.equalsIgnoreCase("Supplier")){
							    sheet.setColumnWidth(2 ,5000);
								sheet.setColumnWidth(3 ,4500);
								sheet.setColumnWidth(4 ,3500);
								sheet.setColumnWidth(5 ,4500);
								sheet.setColumnWidth(6 ,5500);
								sheet.setColumnWidth(7 ,4500);
								sheet.setColumnWidth(8 ,5500);
								sheet.setColumnWidth(9 ,5500);
								sheet.setColumnWidth(10 ,5500);
								sheet.setColumnWidth(11 ,5500);
								sheet.setColumnWidth(12 ,4500);
								sheet.setColumnWidth(13 ,4500);
								sheet.setColumnWidth(14 ,4500);
								sheet.setColumnWidth(15 ,4500);
								sheet.setColumnWidth(16 ,5500);
								sheet.setColumnWidth(17 ,4500);
								sheet.setColumnWidth(18 ,4500);
								sheet.setColumnWidth(19 ,5500);
								sheet.setColumnWidth(20 ,5500);
								sheet.setColumnWidth(21 ,5500);
								sheet.setColumnWidth(22 ,5500);
								sheet.setColumnWidth(23 ,4500);
								sheet.setColumnWidth(24 ,5500);
								sheet.setColumnWidth(25 ,4500);
								sheet.setColumnWidth(26 ,4500);
								sheet.setColumnWidth(27 ,5500);
								sheet.setColumnWidth(28 ,5500);
								sheet.setColumnWidth(29 ,5500);
								sheet.setColumnWidth(30 ,5500);
								sheet.setColumnWidth(31 ,5500);
								
								
							}
							
							else
							{
								sheet.setColumnWidth(2 ,3500);
								sheet.setColumnWidth(3 ,3500);
								sheet.setColumnWidth(4 ,3500);
								sheet.setColumnWidth(5 ,3000);
								sheet.setColumnWidth(6 ,3500);
								sheet.setColumnWidth(7 ,5500);
								sheet.setColumnWidth(8 ,5500);
								sheet.setColumnWidth(9 ,5500);
								sheet.setColumnWidth(10 ,2500);
								sheet.setColumnWidth(11 ,3500);
								sheet.setColumnWidth(12 ,3500);
								sheet.setColumnWidth(13 ,3500);
								sheet.setColumnWidth(14 ,3500);
								sheet.setColumnWidth(15 ,3500);
								sheet.setColumnWidth(16 ,3500);
								sheet.setColumnWidth(17 ,3500);
								sheet.setColumnWidth(18 ,5500);
								sheet.setColumnWidth(19 ,5500);
								sheet.setColumnWidth(20 ,5500);	
								sheet.setColumnWidth(21 ,5500);	
								
							}
													
							
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}
					
					private HSSFSheet createBankWidth(HSSFSheet sheet){
					try{
								sheet.setColumnWidth(0 ,5500);
								sheet.setColumnWidth(1 ,8000);
							    sheet.setColumnWidth(2 ,5000);
								sheet.setColumnWidth(3 ,3500);
								sheet.setColumnWidth(4 ,3500);
								sheet.setColumnWidth(5 ,3500);
								sheet.setColumnWidth(6 ,3000);
								sheet.setColumnWidth(7 ,3500);
								sheet.setColumnWidth(8 ,5500);
								sheet.setColumnWidth(9 ,5500);
								sheet.setColumnWidth(10 ,5500);
								sheet.setColumnWidth(11 ,2500);
								sheet.setColumnWidth(12 ,3500);
								sheet.setColumnWidth(13 ,3500);
								sheet.setColumnWidth(14 ,3500);
								sheet.setColumnWidth(15 ,3500);
								sheet.setColumnWidth(16 ,5500);
								sheet.setColumnWidth(17 ,3500);
								sheet.setColumnWidth(18 ,3500);
								sheet.setColumnWidth(19 ,5500);
								sheet.setColumnWidth(20 ,5500);
								sheet.setColumnWidth(21 ,5500);
								sheet.setColumnWidth(22 ,5500);
																		
							
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}
private HSSFSheet createEmployeeWidth(HSSFSheet sheet,String ReportType){
						
						try{
							   sheet.setColumnWidth(0 ,5500);
							   sheet.setColumnWidth(1 ,8000);
							   sheet.setColumnWidth(2 ,3500);
						    	sheet.setColumnWidth(3 ,3500);
								sheet.setColumnWidth(4 ,3500);
								sheet.setColumnWidth(5 ,5500);
								sheet.setColumnWidth(6 ,5000);
								sheet.setColumnWidth(7 ,5500);
								sheet.setColumnWidth(8 ,5500);
								sheet.setColumnWidth(9 ,5500);
								sheet.setColumnWidth(10 ,5500);
								sheet.setColumnWidth(11 ,2500);
								sheet.setColumnWidth(12 ,3500);
								sheet.setColumnWidth(13 ,3500);
								sheet.setColumnWidth(14 ,3500);
								sheet.setColumnWidth(15 ,3500);
								sheet.setColumnWidth(16 ,3500);
								sheet.setColumnWidth(17 ,3500);
								sheet.setColumnWidth(18 ,3500);
								sheet.setColumnWidth(19 ,3500);
								sheet.setColumnWidth(20 ,3500);
								sheet.setColumnWidth(21 ,3500);
								sheet.setColumnWidth(22 ,5500);
								sheet.setColumnWidth(23 ,3500);
								sheet.setColumnWidth(24 ,5500);
								sheet.setColumnWidth(25 ,3500);
								sheet.setColumnWidth(26 ,5500);
								sheet.setColumnWidth(27 ,5500);
								sheet.setColumnWidth(28 ,3500);
								sheet.setColumnWidth(29 ,3500);
								sheet.setColumnWidth(30 ,5500);
								sheet.setColumnWidth(31 ,5500);
								sheet.setColumnWidth(32 ,3500);
								sheet.setColumnWidth(33 ,3500);
								sheet.setColumnWidth(34 ,3500);
								sheet.setColumnWidth(35 ,3500);
								sheet.setColumnWidth(36 ,3500);
								sheet.setColumnWidth(37 ,3500);
								sheet.setColumnWidth(38 ,3500);
								sheet.setColumnWidth(39 ,3500);
								sheet.setColumnWidth(40 ,3500);
								sheet.setColumnWidth(41 ,3500);
								sheet.setColumnWidth(42 ,3500);
								sheet.setColumnWidth(43 ,3500);
								sheet.setColumnWidth(44 ,3500);
								sheet.setColumnWidth(45 ,3500);
								sheet.setColumnWidth(44 ,3500);			
								sheet.setColumnWidth(45 ,3500);
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}

private HSSFSheet createEmployeeHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String ReportType){
	int k = 0;
	try{
	
	
	HSSFRow rowhead = sheet.createRow( 0);	
	
	HSSFCell cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(ReportType+" ID"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(ReportType+" Name"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Employee Type"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Gender"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Date of Birth"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(ReportType+" Phone"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(ReportType+" Email"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Passport Number"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Country of Issue"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Passport ExpiryDate"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Employee Reporting"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("POS Outlets"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Country"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Unit No"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Building"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Street"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("City"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("State"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Postal Code"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Facebook Id"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Twitter Id"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("LinkedIn Id"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Skype Id"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(pcountry+" ID Number"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString(pcountry+" ExpiryDate"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Visa Number"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Visa ExpiryDate"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Department"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Designation"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Date of Joining"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Date of Leaving"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Labour Card Number"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Work Permit Number"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Contract StartDate"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Contract ExpiryDate"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("IBAN"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Bank"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Branch"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Routing Code"));
	cell.setCellStyle(styleHeader);
	
	/*cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Working Man Days"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Basic Salary"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("House Rent Allowance"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Transport Allowance"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Communication Allowance"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Other Allowance"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Bonus"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Commission"));
	cell.setCellStyle(styleHeader);*/
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Gratuity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Air Ticket"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Leave Salary"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Remarks"));
	cell.setCellStyle(styleHeader);
	
		/*if(ReportType.equalsIgnoreCase("Employee")){
		 cell = rowhead.createCell( k++);
		 cell.setCellValue(new HSSFRichTextString("EmployeeStatus"));
		 cell.setCellStyle(styleHeader);
	 }*/
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Is Active"));
	cell.setCellStyle(styleHeader);
	
	
	}
	
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
					
// Added by Bruhan on June 10 2014, Description:To open Transfer Order Summary  in excel power shell format
				private HSSFWorkbook writeToExcelTranferOrderSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 HTReportUtil movHisUtil  = new HTReportUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="";
					 int SheetId =1;
					try{
						
								String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE = strUtils.fString(request.getParameter("TO_DATE")).trim();
					        	String DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
					        	String JOBNO         = strUtils.fString(request.getParameter("JOBNO"));
					        	String USER          = strUtils.fString(request.getParameter("USER"));
					        	String ITEMNO        = strUtils.fString(request.getParameter("ITEM"));
					        	String DESC          = strUtils.fString(request.getParameter("DESC"));
					        	String ORDERNO       = strUtils.fString(request.getParameter("ORDERNO"));
					        	String CUSTOMER      = strUtils.fString(request.getParameter("CUSTOMER"));
					        	String CUSTOMERID    = strUtils.fString(request.getParameter("CUSTOMERID"));
					        	String status         = strUtils.fString(request.getParameter("STATUS"));
					        	String FROMLOC        = strUtils.fString(request.getParameter("FROMLOC"));
					        	String TOLOC        = strUtils.fString(request.getParameter("TOLOC"));
					           	String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					        	String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					        	String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					        	String RECEIVESTATUS= strUtils.fString(request.getParameter("RECEIVESTATUS"));
					        	String UOM= strUtils.fString(request.getParameter("UOM"));
					        	String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
					        	String reportType ="";
					        	
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					            if (FROM_DATE.length() > 5)
					                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
					        	if (TO_DATE == null)
					        		TO_DATE = "";
					        	else
					        		TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length() > 5)
					                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					        	
					        	Hashtable ht = new Hashtable();
					        				            	
					            if(strUtils.fString(ITEMNO).length() > 0)          ht.put("B.ITEM",ITEMNO);
						        if(strUtils.fString(ORDERNO).length() > 0)         ht.put("B.TONO",ORDERNO);
						        if(strUtils.fString(JOBNO).length() > 0)           ht.put("A.JOBNUM",JOBNO);
						        if(strUtils.fString(CUSTOMERID).length() > 0)        ht.put("A.CUSTCODE",CUSTOMERID);
						        if(strUtils.fString(status).length() > 0)           ht.put("B.PickStatus",status);
						        if(strUtils.fString(FROMLOC).length() > 0)          ht.put("A.FROMWAREHOUSE",FROMLOC);
						        if(strUtils.fString(TOLOC).length() > 0)            ht.put("A.TOWAREHOUSE",TOLOC);
						        if(strUtils.fString(PRD_TYPE_ID).length() > 0) 		ht.put("C.ITEMTYPE",PRD_TYPE_ID);
						        if(strUtils.fString(PRD_BRAND_ID).length() > 0)		ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
						        if(strUtils.fString(PRD_CLS_ID).length() > 0) 		ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
						        if(strUtils.fString(RECEIVESTATUS).length() > 0)    ht.put("B.LNSTAT",RECEIVESTATUS);
						        if(strUtils.fString(UOM).length() > 0)    ht.put("B.SKTUOM",UOM);
						        movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate, tdate, DIRTYPE, PLANT, DESC,CUSTOMER,"",UOM,POSSEARCH);
				             	Boolean workSheetCreated = true;
								if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthTransferOrderSummary(sheet,DIRTYPE);
											sheet = this.createHeaderTransferOrderSummary(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												 
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("tono"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
													cell.setCellStyle(dataStyle);
														
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("fromwarehouse"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("towarehouse"))));
													cell.setCellStyle(dataStyle);
													
													SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

													cell = row.createCell( k++);
													Calendar c = Calendar.getInstance();
										        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
										        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	cell.setCellValue(c.getTime());
													cell.setCellStyle(dataStyleSpecial);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UOM"))));
													cell.setCellStyle(dataStyle);
													
											
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyor"))));
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtypick"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyrc"))));
													cell.setCellStyle(dataStyleRightAlign);
																									
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pickstatus"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 /*if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 sheet = wb.createSheet();*/
													if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthTransferOrderSummary(sheet,DIRTYPE);
														 sheet = this.createHeaderTransferOrderSummary(sheet,styleHeader,DIRTYPE);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
														 
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFSheet createWidthTransferOrderSummary(HSSFSheet sheet,String type){
					int i = 0;
					try{
					
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
					    sheet.setColumnWidth(i++ ,3200);
					    sheet.setColumnWidth(i++ ,3200);
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createHeaderTransferOrderSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
					int k = 0;
					try{
					
					HSSFRow rowhead = sheet.createRow(1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Ref No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Customer Name"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Category"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Sub Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("From Loc"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("To Loc"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Qty"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Receive Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick/Issue Status"));
					cell.setCellStyle(styleHeader);
														
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Recv Status"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("User"));
					cell.setCellStyle(styleHeader);
				
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				//---End Added by Bruhan on June 10 2014, Description:To open Transfer Order Summary  in excel power shell format
								
				// Added by Bruhan on June 11 2014, Description:To open Loan Order Summary   in excel power shell format
				private HSSFWorkbook writeToExcelLoanOrderSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 LoanUtil  movHisUtil= new LoanUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 DateUtils dateUtils = new DateUtils();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",chkExpirydate="";
					 int SheetId =1;
					try{
								String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
					            String JOBNO=strUtils.fString(request.getParameter("JOBNO"));
					            String ITEMNO=strUtils.fString(request.getParameter("ITEM"));
					            String DESC = strUtils.fString(request.getParameter("DESC"));
					            String ORDERNO=strUtils.fString(request.getParameter("DONO"));
					            String CUSTOMER=strUtils.fString(request.getParameter("CUST_NAME"));
					            String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
					            String status = strUtils.fString(request.getParameter("STATUS"));
					            String DIRTYPE=strUtils.fString(request.getParameter("DIRTYPE"));
								String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					            String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					            String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					            String reportType ="";
					            
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					            if (FROM_DATE.length() > 5)
					                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
					        	if (TO_DATE == null)
					        		TO_DATE = "";
					        	else
					        		TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length() > 5)
					                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					        	
					        	Hashtable ht = new Hashtable();
					            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
					            if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
					            if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ORDNO",ORDERNO);
					            if(strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
					            if(strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
					            if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					            if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					            if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					            movQryList = movHisUtil.getLoanOrderSummary(ht,fdate, tdate, DESC, PLANT, CUSTOMER);
						        Boolean workSheetCreated = true;
								if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightForeColorRed = null;
											dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
											
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											 CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthLoanOrderSummary(sheet,DIRTYPE);
											sheet = this.createHeaderLoanOrderSummary(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												  
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("Ordno"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
													cell.setCellStyle(dataStyle);
																										
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
																																			
													SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

													cell = row.createCell( k++);
													Calendar c = Calendar.getInstance();
										        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
										        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	cell.setCellValue(c.getTime());
													cell.setCellStyle(dataStyleSpecial);
													
													 /*chkExpirydate=(String)lineArr.get("expiredate");
										             java.util.Date d1 =  dateUtils.parsetwoDate(curDate); 
										             java.util.Date d2 = dateUtils.parsetwoDate(chkExpirydate); 
										             if(!chkExpirydate.equals("")){
										            	 
										            	 if(dateUtils.compareTo(d1,d2) > 0 ) {
										            		 cell = row.createCell( k++);
															 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
															 cell.setCellStyle(dataStyleRightForeColorRed);
													     }else{
										              		
										              		cell = row.createCell( k++);
														    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
														    cell.setCellStyle(dataStyle);
													     }
										         
									               }else{
									            		cell = row.createCell( k++);
													    cell.setCellValue(new HSSFRichTextString(""));
													    cell.setCellStyle(dataStyle);
									            	
										            }*/
										          // }
												  
													cell = row.createCell( k++);
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
												    cell.setCellStyle(dataStyle);
										             
										            cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UNITMO"))));
													cell.setCellStyle(dataStyle);
										      											
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyor"))));
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyis"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyrc"))));
													cell.setCellStyle(dataStyleRightAlign);
																									
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 /*if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 sheet = wb.createSheet();*/
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthLoanOrderSummary(sheet,DIRTYPE);
														 sheet = this.createHeaderLoanOrderSummary(sheet,styleHeader,DIRTYPE);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
														 
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFWorkbook writeToExcelRentalOrderSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 LoanUtil  movHisUtil= new LoanUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 DateUtils dateUtils = new DateUtils();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",chkExpirydate="";
					 int SheetId =1;
					try{
								String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
					            String JOBNO=strUtils.fString(request.getParameter("JOBNO"));
					            String ITEMNO=strUtils.fString(request.getParameter("ITEM"));
					            String DESC = strUtils.fString(request.getParameter("DESC"));
					            String ORDERNO=strUtils.fString(request.getParameter("DONO"));
					            String CUSTOMER=strUtils.fString(request.getParameter("CUST_NAME"));
					            String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
					            String status = strUtils.fString(request.getParameter("STATUS"));
					            String DIRTYPE=strUtils.fString(request.getParameter("DIRTYPE"));
								String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					            String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					            String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					            String reportType ="";
					            
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					            if (FROM_DATE.length() > 5)
					                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
					        	if (TO_DATE == null)
					        		TO_DATE = "";
					        	else
					        		TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length() > 5)
					                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					        	
					        	Hashtable ht = new Hashtable();
					            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
					            if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
					            if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ORDNO",ORDERNO);
					            if(strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
					            if(strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
					            if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					            if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					            if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					            movQryList = movHisUtil.getLoanOrderSummaryWithPrice(ht,fdate, tdate, DESC, PLANT, CUSTOMER);
						        Boolean workSheetCreated = true;
								if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightForeColorRed = null;
											dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
											
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											 CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthRentalOrderSummary(sheet,DIRTYPE);
											sheet = this.createHeaderRentalOrderSummary(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												  
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("Ordno"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
													cell.setCellStyle(dataStyle);
																										
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
																																			
													SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

													cell = row.createCell( k++);
													Calendar c = Calendar.getInstance();
										        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
										        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	cell.setCellValue(c.getTime());
													cell.setCellStyle(dataStyleSpecial);
													
													 chkExpirydate=(String)lineArr.get("expiredate");
										             java.util.Date d1 =  dateUtils.parsetwoDate(curDate); 
										             java.util.Date d2 = dateUtils.parsetwoDate(chkExpirydate); 
										             if(!chkExpirydate.equals("")){
										            	 
										            	 if(dateUtils.compareTo(d1,d2) > 0 ) {
										            		 cell = row.createCell( k++);
															 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
															 cell.setCellStyle(dataStyleRightForeColorRed);
													     }else{
										              		
										              		cell = row.createCell( k++);
														    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
														    cell.setCellStyle(dataStyle);
													     }
										         
									               }else{
									            		cell = row.createCell( k++);
													    cell.setCellValue(new HSSFRichTextString(""));
													    cell.setCellStyle(dataStyle);
									            	
										            }
										          // }
										      											
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyor"))));
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyis"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyrc"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("employeeid"))));
													cell.setCellStyle(dataStyle);
																										
																									
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 /*if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 sheet = wb.createSheet();*/
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthRentalOrderSummary(sheet,DIRTYPE);
														 sheet = this.createHeaderRentalOrderSummary(sheet,styleHeader,DIRTYPE);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
														 
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
					private HSSFWorkbook writeToExcelLoanOrderSummaryByPrice(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 LoanUtil  movHisUtil= new LoanUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 DateUtils dateUtils = new DateUtils();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",chkExpirydate="";
					 int SheetId =1;
					try{
								String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
					            String JOBNO=strUtils.fString(request.getParameter("JOBNO"));
					            String ITEMNO=strUtils.fString(request.getParameter("ITEM"));
					            String DESC = strUtils.fString(request.getParameter("DESC"));
					            String ORDERNO=strUtils.fString(request.getParameter("DONO"));
					            String CUSTOMER=strUtils.fString(request.getParameter("CUST_NAME"));
					            String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
					            String status = strUtils.fString(request.getParameter("STATUS"));
					            String DIRTYPE=strUtils.fString(request.getParameter("DIRTYPE"));
								String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					            String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					            String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					            String reportType ="";
					                       
					            
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					            if (FROM_DATE.length() > 5)
					                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
					        	if (TO_DATE == null)
					        		TO_DATE = "";
					        	else
					        		TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length() > 5)
					                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					        	
					        	Hashtable ht = new Hashtable();
					            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
					            if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
					            if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ORDNO",ORDERNO);
					            if(strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
					            if(strUtils.fString(status).length() > 0)        ht.put("A.PICKSTATUS",status);
					            if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					            if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					            if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					           
					            movQryList = movHisUtil.getLoanOrderSummaryByPrice(ht,fdate, tdate, DESC, PLANT, CUSTOMER);
						        Boolean workSheetCreated = true;
								if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightForeColorRed = null;
											dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
											
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											 CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthLoanOrderSummaryWithPrice(sheet,DIRTYPE);
											sheet = this.createHeaderLoanOrderSummaryWithPrice(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												  
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("Ordno"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
													cell.setCellStyle(dataStyle);
																										
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
																																			
													SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

													cell = row.createCell( k++);
													Calendar c = Calendar.getInstance();
										        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
										        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	cell.setCellValue(c.getTime());
													cell.setCellStyle(dataStyleSpecial);
																									  
													cell = row.createCell( k++);
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
												    cell.setCellStyle(dataStyle);
												    
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UNITMO"))));
													cell.setCellStyle(dataStyle);
										      											
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyor"))));
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyis"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyrc"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("employeeid"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("RENTALPRICE"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("prodgst"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("orderprice"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("taxval"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("totalwithtax"))));
													cell.setCellStyle(dataStyleRightAlign);
																									
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("pickstatus"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthLoanOrderSummaryWithPrice(sheet,DIRTYPE);
														 sheet = this.createHeaderLoanOrderSummaryWithPrice(sheet,styleHeader,DIRTYPE);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
														 
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFWorkbook writeToExcelLoanOrderSummaryWithPrice(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					 StrUtils strUtils = new StrUtils();
					 LoanUtil  movHisUtil= new LoanUtil();
					 DateUtils _dateUtils = new DateUtils();
					 ArrayList movQryList  = new ArrayList();
					 ArrayList movItemQryList  = new ArrayList();
					 DateUtils dateUtils = new DateUtils();
					 int maxRowsPerSheet = 65535;
					 String fdate="",tdate="",chkExpirydate="";
					 int SheetId =1;
					try{
								String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
					            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
					            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
					            String JOBNO=strUtils.fString(request.getParameter("JOBNO"));
					            String ITEMNO=strUtils.fString(request.getParameter("ITEM"));
					            String DESC = strUtils.fString(request.getParameter("DESC"));
					            String ORDERNO=strUtils.fString(request.getParameter("DONO"));
					            String CUSTOMER=strUtils.fString(request.getParameter("CUST_NAME"));
					            String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
					            String status = strUtils.fString(request.getParameter("STATUS"));
					            String DIRTYPE=strUtils.fString(request.getParameter("DIRTYPE"));
								String PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
					            String PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
					            String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
					            String reportType ="";
					            
					        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
					            String curDate =_dateUtils.getDate();
					            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
					            if (FROM_DATE.length() > 5)
					                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
					        	if (TO_DATE == null)
					        		TO_DATE = "";
					        	else
					        		TO_DATE = TO_DATE.trim();
					        	if (TO_DATE.length() > 5)
					                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
					        	
					        	Hashtable ht = new Hashtable();
					            if(strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
					            if(strUtils.fString(ITEMNO).length() > 0)        ht.put("B.ITEM",ITEMNO);
					            if(strUtils.fString(ORDERNO).length() > 0)        ht.put("B.ORDNO",ORDERNO);
					            if(strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
					            if(strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
					            if(strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					            if(strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					            if(strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					            movQryList = movHisUtil.getLoanOrderSummaryWithPrice(ht,fdate, tdate, DESC, PLANT, CUSTOMER);
						        Boolean workSheetCreated = true;
								if (movQryList.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											HSSFCellStyle CompHeader = null;
											
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightForeColorRed = null;
											dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
											
											 sheet = wb.createSheet("Sheet"+SheetId);
											//sheet = wb.createSheet();
											styleHeader = createStyleHeader(wb);
											 CompHeader = createCompStyleHeader(wb);
											sheet = this.createWidthLoanOrderSummaryWithPrice(sheet,DIRTYPE);
											sheet = this.createHeaderLoanOrderSummaryWithPrice(sheet,styleHeader,DIRTYPE);
											sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
											 int index = 2;
									         String strDiffQty="",deliverydateandtime="";
									         DecimalFormat decformat = new DecimalFormat("#,##0.00");
									    								         
									         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
												   Map lineArr = (Map) movQryList.get(iCnt);	
												   int k = 0;
												  
												    HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
												    cell.setCellStyle(dataStyle);
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("Ordno"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
													cell.setCellStyle(dataStyle);
																										
													
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
													cell.setCellStyle(dataStyle);
																			   
											     	cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
													cell.setCellStyle(dataStyle);
																																			
													SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

													cell = row.createCell( k++);
													Calendar c = Calendar.getInstance();
										        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
										        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	cell.setCellValue(c.getTime());
													cell.setCellStyle(dataStyleSpecial);
													
													 /*chkExpirydate=(String)lineArr.get("expiredate");
										             java.util.Date d1 =  dateUtils.parsetwoDate(curDate); 
										             java.util.Date d2 = dateUtils.parsetwoDate(chkExpirydate); 
										             if(!chkExpirydate.equals("")){
										            	 
										            	 if(dateUtils.compareTo(d1,d2) > 0 ) {
										            		 cell = row.createCell( k++);
															 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
															 cell.setCellStyle(dataStyleRightForeColorRed);
													     }else{
										              		
										              		cell = row.createCell( k++);
														    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
														    cell.setCellStyle(dataStyle);
													     }
										         
									               }else{
									            		cell = row.createCell( k++);
													    cell.setCellValue(new HSSFRichTextString(""));
													    cell.setCellStyle(dataStyle);
									            	
										            }*/
										          // }
												  
													cell = row.createCell( k++);
												    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("expiredate"))));
												    cell.setCellStyle(dataStyle);
												    
												    cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UNITMO"))));
													cell.setCellStyle(dataStyle);
										      											
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyor"))));
													cell.setCellStyle(dataStyleRightAlign);
																																			
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyis"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("qtyrc"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("employeeid"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("RENTALPRICE"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("prodgst"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("orderprice"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("taxval"))));
													cell.setCellStyle(dataStyleRightAlign);
													
													cell = row.createCell( k++);
													cell.setCellValue(Double.parseDouble(((String)lineArr.get("totalwithtax"))));
													cell.setCellStyle(dataStyleRightAlign);
																									
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
													cell.setCellStyle(dataStyle);
													
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("users"))));
													cell.setCellStyle(dataStyle);
																			
													index++;
													 /*if((index-1)%maxRowsPerSheet==0){
														 index = 1;
														 sheet = wb.createSheet();*/
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthLoanOrderSummaryWithPrice(sheet,DIRTYPE);
														 sheet = this.createHeaderLoanOrderSummaryWithPrice(sheet,styleHeader,DIRTYPE);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
														 
													 }
										 
										 }  // end of for loop
											
								 }
								 else if (movQryList.size() < 1) {		
									System.out.println("No Records Found To List");
								}
			       
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}
				
				private HSSFSheet createWidthLoanOrderSummary(HSSFSheet sheet,String type){
					int i = 0;
					try{
					
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
					    sheet.setColumnWidth(i++ ,3200);
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createWidthRentalOrderSummary(HSSFSheet sheet,String type){
					int i = 0;
					try{
					
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3500);
					    sheet.setColumnWidth(i++ ,3200);
					    
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createWidthLoanOrderSummaryWithPrice(HSSFSheet sheet,String type){
					int i = 0;
					try{
					
						sheet.setColumnWidth(i++ ,1000);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,4200);
						sheet.setColumnWidth(i++ ,6500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3500);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3500);
					    sheet.setColumnWidth(i++ ,3200);
					    sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
						sheet.setColumnWidth(i++ ,3200);
					    sheet.setColumnWidth(i++ ,3200);
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createHeaderLoanOrderSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
					int k = 0;
					try{
					
					HSSFRow rowhead = sheet.createRow( 1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Ref No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Customer Name"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Sub Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
						
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Expiry Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Qty"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Receive Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick/Issue Status"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("User"));
					cell.setCellStyle(styleHeader);
				
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				
				private HSSFSheet createHeaderRentalOrderSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
					int k = 0;
					try{
					
					HSSFRow rowhead = sheet.createRow( 1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Ref No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Customer Name"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Sub Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
						
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Expiry Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Receive Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Employee"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick/Issue Status"));
					cell.setCellStyle(styleHeader);
				
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}

				private HSSFSheet createHeaderLoanOrderSummaryWithPrice(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
					int k = 0;
					try{
					
					HSSFRow rowhead = sheet.createRow( 1);
					
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("S/N"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Ref No"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Customer Name"));
					cell.setCellStyle(styleHeader);
								
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product ID"));
					cell.setCellStyle(styleHeader);
							
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Sub Category"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Brand"));
					cell.setCellStyle(styleHeader);
						
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Expiry Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Qty"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Receive Qty"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Employee"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Rental Price"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Tax%"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Order Price"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Tax"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Total"));
					cell.setCellStyle(styleHeader);
					
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Pick/Issue Status"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("User"));
					cell.setCellStyle(styleHeader);
				
				
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}

			
 //---Added by Bruhan on June 12 2014, Description:To make cell fore colour in red 
	    private HSSFCellStyle createDataStyleForeColorRed(HSSFWorkbook wb){
					
					//Create style
					  HSSFCellStyle dataStyle = wb.createCellStyle();
					  dataStyle = wb.createCellStyle();
					  HSSFFont hSSFFont = wb.createFont();
					  hSSFFont.setColor(HSSFColor.RED.index);
					  dataStyle.setFont(hSSFFont);
					  dataStyle.setWrapText(true);
					  return dataStyle;
				}
        //--End Added by Bruhan on June 12 2014,Description:To make cell fore colour in red 
// Added by Bruhan on June 16 2014, Description:To open Activity Logs  in excel power shell format
		private HSSFWorkbook writeToExcelMovementHistory(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			 StrUtils strUtils = new StrUtils();
			 HTReportUtil movHisUtil       = new HTReportUtil();
			 DateUtils _dateUtils = new DateUtils();
			 ArrayList movQryList  = new ArrayList();
			 ArrayList movItemQryList  = new ArrayList();
			 DateUtils dateUtils = new DateUtils();
			 int maxRowsPerSheet = 65535;
			 String fdate="",tdate="",chkExpirydate="";
			 int SheetId =1;
			try{
						String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
			            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
			          	String DIRTYPE          = strUtils.fString(request.getParameter("DIRTYPE"));
						String JOBNO            = strUtils.fString(request.getParameter("JOBNO"));
						String USER        = strUtils.fString(request.getParameter("USER"));
						String USERID        = strUtils.fString(request.getParameter("USERID"));
						String ITEMNO           = strUtils.fString(request.getParameter("ITEM"));
						String sItemDesc        = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("DESC")));
						String ORDERNO          = strUtils.fString(request.getParameter("ORDERNO"));
						String CUSTOMER         = strUtils.fString(request.getParameter("CUSTOMER"));
						String BATCH            = strUtils.fString(request.getParameter("BATCH"));
						String LOC              = strUtils.fString(request.getParameter("LOC_0"));
						String REASONCODE       = strUtils.fString(request.getParameter("REASONCODE"));
						String constkey 		 = strUtils.fString(request.getParameter("DIRTYPE"));
						String LOC_TYPE_ID      =  strUtils.fString(request.getParameter("LOC_TYPE_ID"));
						String LOC_TYPE_ID2      =  strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
						String LOC_TYPE_ID3      =  strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
						String TYPE           =  strUtils.fString(request.getParameter("TYPE"));
						String PRD_CLS_ID =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
						String PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
						String PRD_BRAND_ID =  strUtils.fString(request.getParameter("PRD_BRAND_ID"));
						String PRD_DEPT_ID =  strUtils.fString(request.getParameter("PRD_DEPT_ID"));
						String REMARKS =  strUtils.fString(request.getParameter("REMARKS"));
						  String reportType ="";
			        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			            String curDate =_dateUtils.getDate();
			            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
			            /*if (FROM_DATE.length() > 5)
			                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
			        	if (TO_DATE == null)
			        		TO_DATE = "";
			        	else
			        		TO_DATE = TO_DATE.trim();
			        	if (TO_DATE.length() > 5)
			                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);*/
						  if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
						  if (FROM_DATE.length()>5)
						  if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			        	
			        	 Hashtable ht = new Hashtable();
			        	// if(strUtils.fString(DIRTYPE).length() > 0)      ht.put("DIRTYPE",DIRTYPE);
			             if(strUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
			             if(strUtils.fString(USER).length() > 0)          ht.put("CRBY",USER);
			             if(strUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);
 			             if(strUtils.fString(CUSTOMER).length() > 0)        ht.put("CUSTNO",CUSTOMER);
			             if(strUtils.fString(ORDERNO).length() > 0)         ht.put("ORDNUM",ORDERNO);
			             if(strUtils.fString(BATCH).length() > 0)           ht.put("BATNO",BATCH);
			             movQryList = movHisUtil.getMovHisListWithRemarks(ht,PLANT,FROM_DATE,TO_DATE,USERID,sItemDesc,REASONCODE,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,DIRTYPE,TYPE,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,REMARKS);
			       
			             Boolean workSheetCreated = true;
						if (movQryList.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									HSSFCellStyle CompHeader = null;
									
									CreationHelper createHelper = wb.getCreationHelper();
									dataStyle = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightAlign = null;
									dataStyleRightAlign  = createDataStyleRightAlign(wb);
									
									HSSFCellStyle dataStyleSpecial = null;
									dataStyleSpecial = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightForeColorRed = null;
									dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
									
									sheet = wb.createSheet("Sheet"+SheetId);
									styleHeader = createStyleHeader(wb);
									CompHeader = createCompStyleHeader(wb);
									sheet = this.createWidthMovementHistory(sheet,DIRTYPE);
									sheet = this.createHeaderMovementHistory(sheet,styleHeader,DIRTYPE);
									sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
									 int index = 2;
							         String strDiffQty="",deliverydateandtime="";
							         DecimalFormat decformat = new DecimalFormat("#,##0.00");
							    								         
							         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
										   Map lineArr = (Map) movQryList.get(iCnt);	
										   int k = 0;
										   
										   String trDate= "",TRANDATE="";
						                   trDate=(String)lineArr.get("CRAT");
						                   TRANDATE=(String)lineArr.get("TRANDATE");
						                   if (trDate.length()>8) {
						                       trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
						                    }
						                   if(TRANDATE.contains("-"))
						                   {
						                   	TRANDATE = TRANDATE.substring(8,10)+"/"+ TRANDATE.substring(5,7)+"/"+TRANDATE.substring(0,4);
						                   }
						                 																	  
										    HSSFRow row = sheet.createRow(index);
										    HSSFCell cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
										    cell.setCellStyle(dataStyle);
									
										   cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(TRANDATE));
											cell.setCellStyle(dataStyle);
										
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(trDate));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DIRTYPE"))));
											cell.setCellStyle(dataStyle);
											
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ORDNUM"))));
											cell.setCellStyle(dataStyle);
																										   
									     	cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATNO"))));
											cell.setCellStyle(dataStyle);
																			      				
											
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(((String)lineArr.get("QTY"))));
											cell.setCellStyle(dataStyleRightAlign);
											
																					
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CRBY"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARKS"))));
											cell.setCellStyle(dataStyle);
																	
											index++;
											 if((index-2)%maxRowsPerSheet==0){
												 index = 2;
												 SheetId++;
												 sheet = wb.createSheet("Sheet"+SheetId);
												 styleHeader = createStyleHeader(wb);
												 CompHeader = createCompStyleHeader(wb);
												 sheet = this.createWidthLoanOrderSummary(sheet,DIRTYPE);
												 sheet = this.createHeaderLoanOrderSummary(sheet,styleHeader,DIRTYPE);
												 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
												 
											 }
								 
								 }  // end of for loop
									
						 }
						 else if (movQryList.size() < 1) {		
							System.out.println("No Records Found To List");
						}
	       
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
		
		private HSSFSheet createWidthMovementHistory(HSSFSheet sheet,String type){
			int i = 0;
			try{
			
				sheet.setColumnWidth(i++ ,1000);
				sheet.setColumnWidth(i++ ,3500);
				sheet.setColumnWidth(i++ ,3500);
				sheet.setColumnWidth(i++ ,6000);
				sheet.setColumnWidth(i++ ,3500);
				sheet.setColumnWidth(i++ ,4200);
				sheet.setColumnWidth(i++ ,4200);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,4200);
				sheet.setColumnWidth(i++ ,3500);
				sheet.setColumnWidth(i++ ,3500);
				sheet.setColumnWidth(i++ ,6500);
		
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		
		private HSSFSheet createHeaderMovementHistory(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
			int k = 0;
			try{
			
			HSSFRow rowhead = sheet.createRow(1);
			
			HSSFCell cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Date"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Time"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Logs Type"));
			cell.setCellStyle(styleHeader);
						
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Order No"));
			cell.setCellStyle(styleHeader);
					
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Loc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Product Id"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Batch"));
			cell.setCellStyle(styleHeader);
				
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Qty"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("User"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
		
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		
		//---End Added by Bruhan on June 16 2014, Description:To open Activity Logs  in excel power shell format
		
		
		// Added by Bruhan on June 17 2014, Description:To open Kitting Summary in excel power shell format
		private HSSFWorkbook writeToExcelKittingSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			 StrUtils strUtils = new StrUtils();
		     BOMUtil movHisUtil = new BOMUtil();
			 DateUtils _dateUtils = new DateUtils();
			 ArrayList movQryList  = new ArrayList();
			 ArrayList movItemQryList  = new ArrayList();
			 InvMstDAO _InvMstDAO = new InvMstDAO();
			 ItemMstDAO itemMstDAO = new ItemMstDAO();
			 DateUtils dateUtils = new DateUtils();
			 int maxRowsPerSheet = 65535;
			 String fdate="",tdate="",chkExpirydate="";
			 int SheetId =1;
			try{
						String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
			            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
			            String ITEM    = strUtils.fString(request.getParameter("ITEM"));
			            String PARENTBATCH    = strUtils.fString(request.getParameter("BATCH_0"));
			            String CHILDITEM    = strUtils.fString(request.getParameter("CITEM"));
			            String CHILDBATCH   = strUtils.fString(request.getParameter("BATCH_1"));
			            String PRD_DESCRIP_PARENT = strUtils.fString(request.getParameter("PRD_DESCRIP_PARENT"));
			            String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
			            String LOC     = strUtils.fString(request.getParameter("LOC"));
			            String status         = strUtils.fString(request.getParameter("STATUS"));
			            String SORT         = strUtils.fString(request.getParameter("SORT"));
			           // String USER        = strUtils.fString(request.getParameter("USER"));
						String USERID        = strUtils.fString(request.getParameter("USERID"));
						String KONO        = strUtils.fString(request.getParameter("KONO"));
						String reportType ="";
			        	
			        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			            String curDate =_dateUtils.getDate();
			            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
			            if (FROM_DATE.length() > 5)
			            	fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
			        	if (TO_DATE == null)
			        		TO_DATE = "";
			        	else
			        		TO_DATE = TO_DATE.trim();
			        	if (TO_DATE.length() > 5)
			        		tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
			        	Hashtable ht = new Hashtable();
			            if(strUtils.fString(PLANT).length() > 0)       ht.put("A.PLANT",PLANT);
			            if(strUtils.fString(ITEM).length() > 0)        ht.put("A.PARENT_PRODUCT",ITEM);
			            if(strUtils.fString(PARENTBATCH).length() > 0)        ht.put("A.PARENT_PRODUCT_BATCH",PARENTBATCH);
			            if(strUtils.fString(CHILDITEM).length() > 0)        ht.put("A.CHILD_PRODUCT",CHILDITEM);
			            if(strUtils.fString(CHILDBATCH).length() > 0)        ht.put("A.CHILD_PRODUCT_BATCH",CHILDBATCH);
			            if(strUtils.fString(KONO).length() > 0)        ht.put("A.KONO",KONO);
			           //if(strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
			            movQryList= movHisUtil.getKittingSummary(ht, PLANT, fdate,	tdate,SORT);
			            Boolean workSheetCreated = true;
						if (movQryList.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									HSSFCellStyle CompHeader = null;
									
									CreationHelper createHelper = wb.getCreationHelper();
									dataStyle = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightAlign = null;
									dataStyleRightAlign  = createDataStyleRightAlign(wb);
									
									HSSFCellStyle dataStyleSpecial = null;
									dataStyleSpecial = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightForeColorRed = null;
									dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
									
									sheet = wb.createSheet("Sheet"+SheetId);
									styleHeader = createStyleHeader(wb);
									CompHeader = createCompStyleHeader(wb);
									sheet = this.createWidthKittingSummary(sheet);
									sheet = this.createHeaderKittingSummary(sheet,styleHeader);
									sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
									 int index = 2;
							         String strDiffQty="",deliverydateandtime="";
							         DecimalFormat decformat = new DecimalFormat("#,##0.00");
							    								         
							         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
										   Map lineArr = (Map) movQryList.get(iCnt);	
										   int k = 0;
										   String sDesc="",sdetdesc="";
										    String sItem = (String) lineArr.get("CHILD_PRODUCT");
										    String scanitem =(String)lineArr.get("SCANITEM");
											if(scanitem.equalsIgnoreCase(sItem))
											{
												sDesc = strUtils.fString(itemMstDAO.getItemDesc(PLANT,sItem));
												sdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,sItem));
											}
											else
											{
												sItem = scanitem;
												sDesc = strUtils.fString(itemMstDAO.getItemDesc(PLANT,scanitem));
												sdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,scanitem));	
											}
											
					  					     //get expirydate from inventory
					  						String expiredate = _InvMstDAO.getInvExpiryDate(PLANT,(String) lineArr.get("CHILD_PRODUCT"),(String) lineArr.get("CHILD_PRODUCT_LOC"),
					  										     (String) lineArr.get("CHILD_PRODUCT_BATCH"));
					  						//get parent desc
					  						String parentdesc = itemMstDAO.getKittingParentItem(PLANT,(String) lineArr.get("PARENT_PRODUCT"));
					  						String pdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,(String) lineArr.get("PARENT_PRODUCT")));	
					  						
					  						String remarks = (String)lineArr.get("REMARKS");
					  						String reasoncode = (String) lineArr.get("RSNCODE");
					  						
					  						if(reasoncode.length()>0)
					  						{
					  							remarks = remarks +","+reasoncode;
					  						}
					  						
										    HSSFRow row = sheet.createRow(index);
										    HSSFCell cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
										    cell.setCellStyle(dataStyle);
											
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("KONO"))));
											cell.setCellStyle(dataStyle);
										    
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PARENT_PRODUCT"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(parentdesc);
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(pdetdesc);
											cell.setCellStyle(dataStyle);
																								
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue("1");
											cell.setCellStyle(dataStyle);
																	   
									     	cell = row.createCell( k++);
											cell.setCellValue(sItem);
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(sDesc);
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(sdetdesc);
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_BATCH"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(Double.parseDouble(((String)lineArr.get("QTY"))));
											cell.setCellStyle(dataStyleRightAlign);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CHILD_PRODUCT_LOC"))));
											cell.setCellStyle(dataStyle);
																					
											cell = row.createCell( k++);
											cell.setCellValue(expiredate);
											cell.setCellStyle(dataStyle);
																			
																																	
											cell = row.createCell( k++);
											cell.setCellValue(remarks);
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STATUS"))));
											cell.setCellStyle(dataStyle);
																	
											index++;
											if((index-2)%maxRowsPerSheet==0){
												 index = 2;
												 SheetId++;
												 sheet = wb.createSheet("Sheet"+SheetId);
												 styleHeader = createStyleHeader(wb);
												 CompHeader = createCompStyleHeader(wb);
												 sheet = this.createWidthKittingSummary(sheet);
												 sheet = this.createHeaderKittingSummary(sheet,styleHeader);
												 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
												 
											 }
								 
								 }  // end of for loop
									
						 }
						 else if (movQryList.size() < 1) {		
							System.out.println("No Records Found To List");
						}
	       
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
		
		private HSSFSheet createWidthKittingSummary(HSSFSheet sheet){
			int i = 0;
			try{
				sheet.setColumnWidth(i++ ,1500);
				sheet.setColumnWidth(i++ ,5500);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,4500);
				sheet.setColumnWidth(i++ ,2000);
				sheet.setColumnWidth(i++ ,5500);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,4500);
				sheet.setColumnWidth(i++ ,2000);
				sheet.setColumnWidth(i++ ,4500);
				sheet.setColumnWidth(i++ ,3200);
				sheet.setColumnWidth(i++ ,6500);
				sheet.setColumnWidth(i++ ,3000);
				
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		
		private HSSFSheet createHeaderKittingSummary(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
							
			HSSFRow rowhead = sheet.createRow(1);
			
			HSSFCell cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Kitting/De-Kitting No."));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Parent ProductID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Parent Desc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Parent Detail Desc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Parent Batch"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Qty"));
			cell.setCellStyle(styleHeader);
						
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Child ProductID"));
			cell.setCellStyle(styleHeader);
					
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Child Desc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Child Detail Desc"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Child Batch"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Qty"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Loc"));
			cell.setCellStyle(styleHeader);
						
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Expiry Date"));
			cell.setCellStyle(styleHeader);
				
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Remarks/Reason Code"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Status"));
			cell.setCellStyle(styleHeader);
		
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		
	//---End Added by Bruhan on June 17 2014, Description:To open Kitting Summary  in excel power shell format
	

	
			/*************************  Modification History  ******************************
		     *** Bruhan,Oct 29 2014,Description: To include child item,work order number,Operation Sequence and remove Product class,type,brand
			*/ 
			/*private HSSFWorkbook writeToExcelWipInv(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
				String plant = "";
				DateUtils _dateUtils = new DateUtils();
				StrUtils strUtils =  new StrUtils();
				try{
					HttpSession session = request.getSession();
					plant = (String) session.getAttribute("PLANT");
				       String userID = (String) session.getAttribute("LOGIN_USER");
					   String LOC     = strUtils.fString(request.getParameter("LOC_0"));
			           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
			           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
			           String WONO    = strUtils.fString(request.getParameter("WONO"));
			           String CITEM = strUtils.fString(request.getParameter("CITEM"));
			           String OPSEQ = strUtils.fString(request.getParameter("OPSEQ"));
			           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
			           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
					   Hashtable ht = new Hashtable();
    		            if(strUtils.fString(plant).length() > 0)               ht.put("a.PLANT",plant);
			            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.BATCH",BATCH);
			            if(strUtils.fString(WONO).length() > 0)          ht.put("a.WONO",WONO);
			            if(strUtils.fString(OPSEQ).length() > 0)         ht.put("a.OPSEQ",OPSEQ) ;  
			            if(strUtils.fString(ITEM).length() > 0)        ht.put("a.PITEM",CITEM) ; 
			            if(strUtils.fString(CITEM).length() > 0)        ht.put("a.CITEM",CITEM) ; 
			            ArrayList listQry= new WipUtil().getWipInventory(ht,plant,PRD_DESCRIP,LOC_TYPE_ID,LOC);
				    	 if (listQry.size() > 0) {
								HSSFSheet sheet = null ;
								HSSFCellStyle styleHeader = null;
								HSSFCellStyle dataStyle = null;
								 sheet = wb.createSheet("Sheet1");
								 styleHeader = createStyleHeader(wb);
								 sheet = this.createWidthWipInv(sheet);
								 sheet = this.createHeaderWipInv(sheet,styleHeader);
								 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
									 Map lineArr = (Map) listQry.get(iCnt);	
									 int k = 0;
									 Double qty =  Double.parseDouble(((String) lineArr.get("QTY").toString())) ;
									 		dataStyle = createDataStyle(wb);
										    HSSFRow row = sheet.createRow( iCnt+1);
										    
										    HSSFCell cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WONO"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OPSEQ"))));
											cell.setCellStyle(dataStyle);
											
										    cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
											cell.setCellStyle(dataStyle);
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CITEM"))));
											cell.setCellStyle(dataStyle);
																						
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("citemdesc"))));
											cell.setCellStyle(dataStyle);
																				
										
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
											cell.setCellStyle(dataStyle);
											
											
											cell = row.createCell( k++);
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
											cell.setCellStyle(dataStyle);
											
											
											cell = row.createCell( k++);
											//cell.setCellValue(Double.parseDouble(StrUtils.formatNum((qty.toString()))));
											cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("QTY").toString())));
											cell.setCellStyle(dataStyle);
								
									  }
									
						 }
						 else if (listQry.size() < 1) {		
							

								System.out.println("No Records Found To List");
							}
			
					
				}catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				
				return wb;
			}*/

	/*************************  Modification History  ******************************
		     *** Bruhan,Oct 29 2014,Description: To include child item,work order number,Operation Sequence and remove Product class,type,brand
		*/ 
		 private HSSFSheet createHeaderWipInv(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
				HSSFRow rowhead = sheet.createRow( 0);
				HSSFCell cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Work Order"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Opq Seq"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Loc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Desc"));
				cell.setCellStyle(styleHeader);
							
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("UOM"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Batch"));
				cell.setCellStyle(styleHeader);
										
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Quantity"));
				cell.setCellStyle(styleHeader);
									
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
		 
		 /*************************  Modification History  ******************************
		     *** Bruhan,Oct 29 2014,Description: To include child item,work order number,Operation Sequence and remove Product class,type,brand
			*/ 
			private HSSFSheet createWidthWipInv(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth(0 ,5000);
					sheet.setColumnWidth(1 ,3500);
					sheet.setColumnWidth(2 ,6000);
					sheet.setColumnWidth(3 ,6000);
					sheet.setColumnWidth(4 ,8000);
					sheet.setColumnWidth(5 ,3500);
					sheet.setColumnWidth(6 ,5000);
					sheet.setColumnWidth(7 ,3000);
	
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}

		// Added by Bruhan on June 12 2014, Description:To open Work Order Summary in excel power shell format
			private HSSFWorkbook writeToExcelWorkOrderSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
				 StrUtils strUtils = new StrUtils();
				 HTReportUtil movHisUtil       = new HTReportUtil();
				 DateUtils _dateUtils = new DateUtils();
				 ArrayList movQryList  = new ArrayList();
				 ArrayList movItemQryList  = new ArrayList();
				 DateUtils dateUtils = new DateUtils();
				 int maxRowsPerSheet = 65535;
				 String fdate="",tdate="",chkExpirydate="";
				try{
					
							String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
				            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
				            String DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
				            String JOBNO         = strUtils.fString(request.getParameter("JOBNO"));
				            String USER          = strUtils.fString(request.getParameter("USER"));
				            String ITEMNO        = strUtils.fString(request.getParameter("ITEM"));
				            String DESC          = strUtils.fString(request.getParameter("DESC"));
				            String ORDERNO       = strUtils.fString(request.getParameter("ORDERNO"));
				            String CUSTOMER      = strUtils.fString(request.getParameter("CUSTOMER"));
				            String CUSTOMERID    = strUtils.fString(request.getParameter("CUSTOMERID"));
				            String WSTATUS       = strUtils.fString(request.getParameter("WSTATUS"));
				            String LNSTATUS      = strUtils.fString(request.getParameter("LNSTATUS"));
				            String PRD_TYPE_ID   = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
				            String PRD_BRAND_ID  = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
				            String PRD_CLS_ID    = strUtils.fString(request.getParameter("PRD_CLS_ID"));
				            String UOM    = strUtils.fString(request.getParameter("UOM"));
				            String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
				        	
				        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
				            String curDate =_dateUtils.getDate();
				            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
				            if (FROM_DATE.length() > 5)
				               // fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
				            	fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3,5) +  FROM_DATE.substring(0, 2);
				        	if (TO_DATE == null)
				        		TO_DATE = "";
				        	else
				        		TO_DATE = TO_DATE.trim();
				        	if (TO_DATE.length() > 5)
				                //tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
				        		 tdate = TO_DATE.substring(6) + TO_DATE.substring(3,5)+ TO_DATE.substring(0, 2);    
				        	
				        	Hashtable ht = new Hashtable();
				             if(strUtils.fString(JOBNO).length() > 0)           ht.put("A.JOBNUM",JOBNO);
					        if(strUtils.fString(ITEMNO).length() > 0)          ht.put("B.ITEM",ITEMNO);
					        if(strUtils.fString(ORDERNO).length() > 0)         ht.put("B.WONO",ORDERNO);
					        if(strUtils.fString(CUSTOMERID).length() > 0)      ht.put("A.CUSTCODE",CUSTOMERID);
					        if(strUtils.fString(LNSTATUS).length() > 0)        ht.put("B.LNSTAT",LNSTATUS);
					        if(strUtils.fString(PRD_TYPE_ID).length() > 0)     ht.put("C.ITEMTYPE",PRD_TYPE_ID);
					        if(strUtils.fString(PRD_BRAND_ID).length() > 0)    ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
					        if(strUtils.fString(PRD_CLS_ID).length() > 0)      ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
					        movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,PLANT,DESC,CUSTOMER,"","",POSSEARCH);
					        Boolean workSheetCreated = true;
							if (movQryList.size() > 0) {
										HSSFSheet sheet = null ;
										HSSFCellStyle styleHeader = null;
										HSSFCellStyle dataStyle = null;
										
										CreationHelper createHelper = wb.getCreationHelper();
										dataStyle = createDataStyle(wb);
										
										HSSFCellStyle dataStyleRightAlign = null;
										dataStyleRightAlign  = createDataStyleRightAlign(wb);
										
										HSSFCellStyle dataStyleSpecial = null;
										dataStyleSpecial = createDataStyle(wb);
										
										HSSFCellStyle dataStyleRightForeColorRed = null;
										dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
										
										sheet = wb.createSheet();
										styleHeader = createStyleHeader(wb);
										sheet = this.createWidthWorkOrderSummary(sheet,DIRTYPE);
										sheet = this.createHeaderWorkOrderSummary(sheet,styleHeader,DIRTYPE);
										 int index = 1;
								         String strDiffQty="",deliverydateandtime="";
								         DecimalFormat decformat = new DecimalFormat("#,##0.00");
								    								         
								         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
											   Map lineArr = (Map) movQryList.get(iCnt);	
											   int k = 0;
											
											    HSSFRow row = sheet.createRow(index);
											    HSSFCell cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
											    cell.setCellStyle(dataStyle);
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("wono"))));
												cell.setCellStyle(dataStyle);
																								
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("jobNum"))));
												cell.setCellStyle(dataStyle);
																		
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
												cell.setCellStyle(dataStyle);
												

												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("remarks"))));
												cell.setCellStyle(dataStyle);
																		   
										     	cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("item"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_cls_id"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemtype"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("prd_brand_id"))));
												cell.setCellStyle(dataStyle);
												
																							
												SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");

												cell = row.createCell( k++);
												Calendar c = Calendar.getInstance();
									        	c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactiondate"))));
									        	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
									        	cell.setCellValue(c.getTime());
												cell.setCellStyle(dataStyleSpecial);
												
																					      											
												cell = row.createCell( k++);
												cell.setCellValue(Double.parseDouble(((String)lineArr.get("orderqty"))));
												cell.setCellStyle(dataStyleRightAlign);
																																		
												cell = row.createCell( k++);
												cell.setCellValue(Double.parseDouble(((String)lineArr.get("finishedqty"))));
												cell.setCellStyle(dataStyleRightAlign);
												
																																	
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("lnstat"))));
												cell.setCellStyle(dataStyle);
																		
												index++;
												 if((index-1)%maxRowsPerSheet==0){
													 index = 1;
													 sheet = wb.createSheet();
													 styleHeader = createStyleHeader(wb);
													 sheet = this.createWidthWorkOrderSummary(sheet,DIRTYPE);
													 sheet = this.createHeaderWorkOrderSummary(sheet,styleHeader,DIRTYPE);
													 
												 }
									 
									 }  // end of for loop
										
							 }
							 else if (movQryList.size() < 1) {		
								System.out.println("No Records Found To List");
							}
		       
				}catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				
				return wb;
			}
			
			private HSSFSheet createWidthWorkOrderSummary(HSSFSheet sheet,String type){
				int i = 0;
				try{
					
					sheet.setColumnWidth(i++ ,1000);
					sheet.setColumnWidth(i++ ,3500);
				    sheet.setColumnWidth(i++ ,3500);
					sheet.setColumnWidth(i++ ,4200);
					sheet.setColumnWidth(i++ ,4200);
					sheet.setColumnWidth(i++ ,4200);
					sheet.setColumnWidth(i++ ,6500);
					sheet.setColumnWidth(i++ ,3500);
					sheet.setColumnWidth(i++ ,3500);
					sheet.setColumnWidth(i++ ,3500);
					sheet.setColumnWidth(i++ ,3500);
					sheet.setColumnWidth(i++ ,3200);
					sheet.setColumnWidth(i++ ,3200);
			        sheet.setColumnWidth(i++ ,3200);
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			
			private HSSFSheet createHeaderWorkOrderSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type){
				int k = 0;
				try{
				
					
					
				HSSFRow rowhead = sheet.createRow( 0);
				
				HSSFCell cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("S/N"));
				cell.setCellStyle(styleHeader);
			
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Ref No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Remarks"));
				cell.setCellStyle(styleHeader);
							
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Product ID"));
				cell.setCellStyle(styleHeader);
						
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Description"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Category"));
				cell.setCellStyle(styleHeader);
				
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Sub Category"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Brand"));
				cell.setCellStyle(styleHeader);
					
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order Date"));
				cell.setCellStyle(styleHeader);
				
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Order Qty"));
				cell.setCellStyle(styleHeader);
							
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Finished Qty"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell( k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);
			
			
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
			
			//---End Added by Bruhan on June 12 2014, Description:To open Work Order Summary   in excel power shell format
	
		
   
	 //start code by Bruhan to write wip summary details in excel on 03 oct 2014	     
		     /*private HSSFWorkbook writeToExcelWipSummaryDetails(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
					String plant = "",fdate="",tdate="";
					DateUtils _dateUtils = new DateUtils();
					StrUtils strUtils =  new StrUtils();
					int maxRowsPerSheet = 65535;
					
					try{
						HttpSession session = request.getSession();
						   String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						   
				           String EMPNO     = strUtils.fString(request.getParameter("EMPNO"));
				           String WONO    = strUtils.fString(request.getParameter("WONO"));
				           String PITEM   = strUtils.fString(request.getParameter("ITEM"));
				           String CITEM = strUtils.fString(request.getParameter("CITEM"));
				           String OPSEQ = strUtils.fString(request.getParameter("OPR_SEQNUM"));
				           String BATCH = strUtils.fString(request.getParameter("BATCH"));
				           String RSNCODE = strUtils.fString(request.getParameter("REASONCODE_0"));
				           String FROM_DATE = strUtils.fString(request.getParameter("FROM_DATE"));
				           String TO_DATE = strUtils.fString(request.getParameter("TO_DATE"));
				           String LOC = strUtils.fString(request.getParameter("LOC_0"));
				           String DIRTYPE = strUtils.fString(request.getParameter("DIRTYPE"));
				           
				           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
				           if (FROM_DATE.length()>5)

				           fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



				           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
				           if (TO_DATE.length()>5)
				           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

				          
				           Hashtable ht = new Hashtable();

				           if(strUtils.fString(EMPNO).length() > 0)         ht.put("USERFLD5",EMPNO);
				            if(strUtils.fString(WONO).length() > 0)          ht.put("ORDNUM",WONO);
				            if(strUtils.fString(OPSEQ).length() > 0)         ht.put("USERFLD6",OPSEQ);
				            if(strUtils.fString(BATCH).length() > 0)         ht.put("BATNO",BATCH) ; 
				            if(strUtils.fString(LOC).length() > 0)           ht.put("LOC","WIP_"+LOC) ; 
				          
				            
				            
				            ArrayList listQry= new WipUtil().getWipSummary(ht,fdate,tdate,DIRTYPE,PITEM,CITEM,RSNCODE,PLANT);
				          		
						 if (listQry.size() > 0) {
									HSSFSheet sheet = null ;
									HSSFCellStyle styleHeader = null;
									HSSFCellStyle dataStyle = null;
									
									CreationHelper createHelper = wb.getCreationHelper();
									dataStyle = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightAlign = null;
									dataStyleRightAlign  = createDataStyleRightAlign(wb);
									
									HSSFCellStyle dataStyleSpecial = null;
									dataStyleSpecial = createDataStyle(wb);
									
									HSSFCellStyle dataStyleRightForeColorRed = null;
									dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
								
									
									 sheet = wb.createSheet("Sheet1");
									 styleHeader = createStyleHeader(wb);
									 sheet = this.createWidthWipSummaryDetails(sheet);
									 sheet = this.createHeaderWipSummaryDetails(sheet,styleHeader);
									  
									 int index = 1;
									 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
										 Map lineArr = (Map) listQry.get(iCnt);	
										 int k = 0;
										 Double qty =  Double.parseDouble(((String) lineArr.get("QTY").toString())) ;
										 String trDate= "",Date="",Time="",strDate="";
				                            trDate=(String)lineArr.get("CRAT");
				                            
				                            if (trDate.length()>8) {
				                            	Date    = trDate.substring(0,4)+"-"+ trDate.substring(4,6)+"-"+trDate.substring(6,8);
				                            	Time    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
				                                    }
				                            
										 		dataStyle = createDataStyle(wb);
				                            HSSFRow row = sheet.createRow(index);
										    HSSFCell cell = row.createCell( k++);
										    strDate=(String)lineArr.get("toexceltransactioncratdate");
												
												
													
													 SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
													// cell = row.createCell( k++);
													 Calendar c = Calendar.getInstance();
										        	 c.setTime(_shortDateformatter.parse(StrUtils.fString((String)lineArr.get("toexceltransactioncratdate"))));
										        	 dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
										        	 cell.setCellValue(c.getTime());
													 cell.setCellStyle(dataStyleSpecial);
												
												
											    cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Time)));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DIRTYPE"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPNO"))));
												cell.setCellStyle(dataStyle);
													
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WONO"))));
												cell.setCellStyle(dataStyle);
												
																	
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("itemdesc"))));
												cell.setCellStyle(dataStyle);
											
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OPSEQ"))));
												cell.setCellStyle(dataStyle);
												
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BATCH"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												//cell.setCellValue(Double.parseDouble(StrUtils.formatNum((qty.toString()))));
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("QTY"))));
												cell.setCellStyle(dataStyle);
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("uom"))));
												cell.setCellStyle(dataStyle);
												
												
												cell = row.createCell( k++);
												cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("RSNCODE"))));
												cell.setCellStyle(dataStyle);
												
												
												
												index++;
												 if((index-1)%maxRowsPerSheet==0){
													 index = 1;
													 sheet = wb.createSheet();
													 styleHeader = createStyleHeader(wb);
													 sheet = this.createWidthWipSummaryDetails(sheet);
													 sheet = this.createHeaderWipSummaryDetails(sheet,styleHeader);
														 
												 }
											
												
										  }
										
							 }
							 else if (listQry.size() < 1) {		
								

									System.out.println("No Records Found To List");
								}
						
						
						
					}catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					
					return wb;
				}*/
			 
			 private HSSFSheet createHeaderWipSummaryDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
					int k = 0;
					try{
					
						
					HSSFRow rowhead = sheet.createRow( 0);
					HSSFCell cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Date"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Time"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Movement Type"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Employee"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Work Order"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Product Id"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Description"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Location"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Operation Seq"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Batch"));
					cell.setCellStyle(styleHeader);
										
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Quantity"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("UOM"));
					cell.setCellStyle(styleHeader);
				
					cell = rowhead.createCell( k++);
					cell.setCellValue(new HSSFRichTextString("Remarks"));
					cell.setCellStyle(styleHeader);
				
				
					
					
															
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}
				private HSSFSheet createWidthWipSummaryDetails(HSSFSheet sheet){
					
					try{
						sheet.setColumnWidth(0 ,3000);
						sheet.setColumnWidth(1 ,3000);
						sheet.setColumnWidth(2 ,7000);
						sheet.setColumnWidth(3 ,4000);
						sheet.setColumnWidth(4 ,5000);
						sheet.setColumnWidth(5 ,5000);
						sheet.setColumnWidth(6 ,8000);
						sheet.setColumnWidth(7 ,6000);
						sheet.setColumnWidth(8 ,4000);
						sheet.setColumnWidth(9 ,4000);
						sheet.setColumnWidth(10 ,3000);
						sheet.setColumnWidth(11 ,3000);
						sheet.setColumnWidth(12 ,8000);
						
						
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
				}

		//end code by Bruhan to write wip summary details in excel on 03 oct 2014
		
				
				   private HSSFWorkbook writeToExcelTimeAttendanceDetails(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
						String plant = "",fdate="",tdate="",query="",empno="",empname="",eventtype="";
						DateUtils _dateUtils = new DateUtils();
						StrUtils strUtils =  new StrUtils();
						Hashtable  htEmployee=new Hashtable();
						EmployeeDAO _EmployeeDAO=new EmployeeDAO();
						int maxRowsPerSheet = 65535;
						int SheetId =1;
						
						try{
							HttpSession session = request.getSession();
							   String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
							   
							    String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
					            String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
					            String EMPLOYEE     = strUtils.fString(request.getParameter("CUST_NAME"));
					            String EVENTID    = strUtils.fString(request.getParameter("EVENTID"));
					            String reportType ="";
					            
					            if(EMPLOYEE.length() > 0)
					            {
					             query=" isnull(empno,'') empno";
					             htEmployee.put(IDBConstants.PLANT,PLANT);
					             htEmployee.put("FNAME",EMPLOYEE);
					       	     Map m=_EmployeeDAO.selectRow(query, htEmployee,"");
					    	     empno=(String)m.get("empno");
					    	     if(empno==null || empno=="")
					    	     {
					    	    	 empno =  _EmployeeDAO.getEmpno(PLANT, EMPLOYEE, "");
					    	    	 if(empno==null || empno=="")
					    	    	 {
					    	    		 empno = EMPLOYEE;
					    	    	 }
					    	     }
					            }
					           
					            if (FROM_DATE.length()>5)
					                   fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
					                   
					            if (TO_DATE.length()>5){
					                    tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
					            }else{
					            	tdate = new DateUtils().getDateFormatyyyyMMdd();
					            }
					                        
					             Hashtable ht = new Hashtable();
					             if(strUtils.fString(PLANT).length() > 0)               ht.put("PLANT",PLANT);
					             if(strUtils.fString(empno).length() > 0)               ht.put("EMPNO",empno);
					             if(strUtils.fString(EVENTID).length() > 0)          ht.put("EVENT",EVENTID);
					                     
					             ArrayList listQry=  new TimeTrackingUtil().getTimeTrackingReport(PLANT,ht,fdate,tdate,"");	
					                   		
							 if (listQry.size() > 0) {
										HSSFSheet sheet = null ;
										HSSFCellStyle styleHeader = null;
										HSSFCellStyle dataStyle = null;
										HSSFCellStyle CompHeader = null;

										CreationHelper createHelper = wb.getCreationHelper();
										dataStyle = createDataStyle(wb);
										
										HSSFCellStyle dataStyleRightAlign = null;
										dataStyleRightAlign  = createDataStyleRightAlign(wb);
										
										HSSFCellStyle dataStyleSpecial = null;
										dataStyleSpecial = createDataStyle(wb);
										
										HSSFCellStyle dataStyleRightForeColorRed = null;
										dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
									
										
										sheet = wb.createSheet("Sheet"+SheetId);
										 CompHeader = createCompStyleHeader(wb);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidthTimeAttendanceDetails(sheet);
										 sheet = this.createHeaderTimeAttendanceDetails(sheet,styleHeader);
										 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);
										  
										 int index = 2;
										 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
											 Map lineArr = (Map) listQry.get(iCnt);	
											 int k = 0;
											 eventtype =(String)lineArr.get("EVENTTYPE");
											         
											 		dataStyle = createDataStyle(wb);
					                            HSSFRow row = sheet.createRow(index);
											    HSSFCell cell = row.createCell( k++);
											   		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPNO"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPNAME"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ALTERNATEEMP"))));
													cell.setCellStyle(dataStyle);
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMP_EVENT"))));
													cell.setCellStyle(dataStyle);
														
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EVENTDATE"))));
													cell.setCellStyle(dataStyle);
													
																		
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INTIME"))));
													cell.setCellStyle(dataStyle);
												
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OUTTIME"))));
													cell.setCellStyle(dataStyle);
												
													if(eventtype.equalsIgnoreCase("+"))
						                        	{
						                        	
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TOTAL_TIME"))));
														cell.setCellStyle(dataStyle);
													
														cell = row.createCell( k++);
														cell.setCellValue("00:00:00");
														cell.setCellStyle(dataStyle);
												
						                        	}
													if(eventtype.equalsIgnoreCase("-"))
						                        	{
														cell = row.createCell( k++);
														cell.setCellValue("00:00:00");
														cell.setCellStyle(dataStyle);
													
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TOTAL_TIME"))));
														cell.setCellStyle(dataStyle);
						                        	}
													
													cell = row.createCell( k++);
													cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EVENTTYPE"))));
													cell.setCellStyle(dataStyle);
												
															
													index++;
													 if((index-2)%maxRowsPerSheet==0){
														 index = 2;
														 SheetId++;
														 sheet = wb.createSheet("Sheet"+SheetId);
														 styleHeader = createStyleHeader(wb);
														 CompHeader = createCompStyleHeader(wb);
														 sheet = this.createWidthTimeAttendanceDetails(sheet);
														 sheet = this.createHeaderTimeAttendanceDetails(sheet,styleHeader);
														 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
															 
													 }
												
													
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
				 
				 private HSSFSheet createHeaderTimeAttendanceDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
						int k = 0;
						try{
						
							
						HSSFRow rowhead = sheet.createRow(1);
						HSSFCell cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Employee ID"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Employee Name"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Alternate Employee"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Event"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Date"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Clock In"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Clock Out"));
						cell.setCellStyle(styleHeader);
							
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Productive Time"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Idle Time"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("Idle/Productive"));
						cell.setCellStyle(styleHeader);
					
																	
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}
					private HSSFSheet createWidthTimeAttendanceDetails(HSSFSheet sheet){
						
						try{
							sheet.setColumnWidth(0 ,5000);
							sheet.setColumnWidth(1 ,5000);
							sheet.setColumnWidth(2 ,5000);
							sheet.setColumnWidth(3 ,3000);
							sheet.setColumnWidth(4 ,4000);
							sheet.setColumnWidth(5 ,6000);
							sheet.setColumnWidth(6 ,6000);
							sheet.setColumnWidth(7 ,4000);
							sheet.setColumnWidth(8 ,4000);
							sheet.setColumnWidth(9 ,5000);
								
							
						}
						catch(Exception e){
							this.mLogger.exception(this.printLog, "", e);
						}
						return sheet;
					}

			//end code by Bruhan to write wip summary details in excel on 03 oct 2014
					
					
					  private HSSFWorkbook writeToExcelLaborTimeAttendanceDetails(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
							String plant = "",fdate="",tdate="",query="",empno="",empname="",eventtype="";
							DateUtils _dateUtils = new DateUtils();
							StrUtils strUtils =  new StrUtils();
							Hashtable  htEmployee=new Hashtable();
							EmployeeDAO _EmployeeDAO=new EmployeeDAO();
							int maxRowsPerSheet = 65535;
							String pitem="";
							
							try{
								HttpSession session = request.getSession();
								   String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
								   
								    String FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
						            String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
						            String EMPLOYEE     = strUtils.fString(request.getParameter("CUST_NAME"));
						            String WONO    = strUtils.fString(request.getParameter("WONO"));
						            String OPSEQ    = strUtils.fString(request.getParameter("OPR_SEQNUM"));
						          
						             
						            if(EMPLOYEE.length() > 0)
						            {
						             query=" isnull(empno,'') empno";
						             htEmployee.put(IDBConstants.PLANT,PLANT);
						             htEmployee.put("FNAME",EMPLOYEE);
						       	     Map m=_EmployeeDAO.selectRow(query, htEmployee,"");
						    	     empno=(String)m.get("empno");
						            }
						           
						            if (FROM_DATE.length()>5)
						                   fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
						                   
						            if (TO_DATE.length()>5){
						                    tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
						            }else{
						            	tdate = new DateUtils().getDateFormatyyyyMMdd();
						            }
						            Hashtable ht = new Hashtable();
						            if(strUtils.fString(PLANT).length() > 0)               ht.put("A.PLANT",PLANT);
						            /*if(strUtils.fString(empno).length() > 0)               ht.put("EMPNO",empno);
						            if(strUtils.fString(WONO).length() > 0)        		   ht.put("WONO",WONO);
						            if(strUtils.fString(OPSEQ).length() > 0)                ht.put("OPSEQ",OPSEQ);
						             */       
						            ArrayList listQry=  new LaborTimeTrackingUtil().getLaborTimeTrackingReport(PLANT,ht,fdate,tdate,empno,WONO,OPSEQ,"");	
						                       
						                    		
								 if (listQry.size() > 0) {
											HSSFSheet sheet = null ;
											HSSFCellStyle styleHeader = null;
											HSSFCellStyle dataStyle = null;
											
											CreationHelper createHelper = wb.getCreationHelper();
											dataStyle = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightAlign = null;
											dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
											HSSFCellStyle dataStyleSpecial = null;
											dataStyleSpecial = createDataStyle(wb);
											
											HSSFCellStyle dataStyleRightForeColorRed = null;
											dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
										
											
											 sheet = wb.createSheet("Sheet1");
											 styleHeader = createStyleHeader(wb);
											 sheet = this.createWidthLaborTimeAttendanceDetails(sheet);
											 sheet = this.createHeaderLaborTimeAttendanceDetails(sheet,styleHeader);
											  
											 int index = 1;
											 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
												 Map lineArr = (Map) listQry.get(iCnt);
												 pitem =(String)lineArr.get("PITEM");
												 int k = 0;
													         
												 		dataStyle = createDataStyle(wb);
						                            HSSFRow row = sheet.createRow(index);
												    HSSFCell cell = row.createCell( k++);
												   		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EMPNAME"))));
														cell.setCellStyle(dataStyle);
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("WONO"))));
														cell.setCellStyle(dataStyle);
															
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PITEM"))));
														cell.setCellStyle(dataStyle);
														
																			
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OPSEQ"))));
														cell.setCellStyle(dataStyle);
														
														 if(!pitem.equalsIgnoreCase("N.A."))
								                        {
															 cell = row.createCell( k++);
															 //cell.setCellValue(Double.parseDouble(StrUtils.formatNum(StrUtils.fString((String)lineArr.get("QTY")))));
															 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("QTY"))));	
															 cell.setCellStyle(dataStyle);
								                        }
														 if(pitem.equalsIgnoreCase("N.A."))
								                        {
															 cell = row.createCell( k++);
															 cell.setCellValue(new HSSFRichTextString("N.A."));
															 cell.setCellStyle(dataStyle);
										                     
								                        }
														
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TRANDATE"))));
														cell.setCellStyle(dataStyle);
															
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INTIME"))));
														cell.setCellStyle(dataStyle);
													
														cell = row.createCell( k++);
														cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OUTTIME"))));
														cell.setCellStyle(dataStyle);
													
														 if(!pitem.equalsIgnoreCase("N.A."))
							                        	{
							                        	
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TOTAL_TIME"))));
															cell.setCellStyle(dataStyle);
														
															cell = row.createCell( k++);
															cell.setCellValue("00:00:00");
															cell.setCellStyle(dataStyle);
													
							                        	}
														 if(pitem.equalsIgnoreCase("N.A."))
							                        	{
															cell = row.createCell( k++);
															cell.setCellValue("00:00:00");
															cell.setCellStyle(dataStyle);
														
															
															cell = row.createCell( k++);
															cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("TOTAL_TIME"))));
															cell.setCellStyle(dataStyle);
							                        	}
																	
														index++;
														 if((index-1)%maxRowsPerSheet==0){
															 index = 1;
															 sheet = wb.createSheet();
															 styleHeader = createStyleHeader(wb);
															 sheet = this.createWidthLaborTimeAttendanceDetails(sheet);
															 sheet = this.createHeaderLaborTimeAttendanceDetails(sheet,styleHeader);
																 
														 }
													
														
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
					 
					 private HSSFSheet createHeaderLaborTimeAttendanceDetails(HSSFSheet sheet, HSSFCellStyle styleHeader){
							int k = 0;
							try{
							
								
							HSSFRow rowhead = sheet.createRow( 0);
							HSSFCell cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Employee"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("WONO"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Parent Item"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Operation Seq"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Quantity"));
							cell.setCellStyle(styleHeader);
								
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Date"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Clock In"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Clock Out"));
							cell.setCellStyle(styleHeader);
						
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Productive Time"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell( k++);
							cell.setCellValue(new HSSFRichTextString("Idle Time"));
							cell.setCellStyle(styleHeader);
																	
							}
							catch(Exception e){
								this.mLogger.exception(this.printLog, "", e);
							}
							return sheet;
						}
						private HSSFSheet createWidthLaborTimeAttendanceDetails(HSSFSheet sheet){
							
							try{
								sheet.setColumnWidth(0 ,5000);
								sheet.setColumnWidth(1 ,5000);
								sheet.setColumnWidth(2 ,4000);
								sheet.setColumnWidth(3 ,4000);
								sheet.setColumnWidth(4 ,4000);
								sheet.setColumnWidth(5 ,4000);
								sheet.setColumnWidth(6 ,4000);
								sheet.setColumnWidth(7 ,4000);
								sheet.setColumnWidth(8 ,4000);
								sheet.setColumnWidth(9 ,4000);
									
								
							}
							catch(Exception e){
								this.mLogger.exception(this.printLog, "", e);
							}
							return sheet;
						}				

	private HSSFWorkbook writeToExcelLocZeroQty(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			DateUtils _dateUtils = new DateUtils();
			StrUtils strUtils =  new StrUtils();
			int maxRowsPerSheet = 65535;
			
			int SheetId =1;
				
			try{
				HttpSession session = request.getSession();
				String PLANT= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				   
				String LOC = strUtils.fString(request.getParameter("LOC"));
				String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
				String reportType ="";
		          
				Hashtable ht = new Hashtable();
                if(StrUtils.fString(LOC).length() > 0)          ht.put("LOC",LOC);
                if(StrUtils.fString(LOC_TYPE_ID).length() > 0)  ht.put("LOC_TYPE_ID",LOC_TYPE_ID); 
                 
           		 ArrayList listQry =  new InvUtil().getInvListSummaryLocZeroQty(PLANT,ht);	
						                    		
				 if (listQry.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						HSSFCellStyle CompHeader = null;
						CreationHelper createHelper = wb.getCreationHelper();
						dataStyle = createDataStyle(wb);
											
						HSSFCellStyle dataStyleRightAlign = null;
						dataStyleRightAlign  = createDataStyleRightAlign(wb);
											
						HSSFCellStyle dataStyleSpecial = null;
						dataStyleSpecial = createDataStyle(wb);
											
						HSSFCellStyle dataStyleRightForeColorRed = null;
						dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
											
						 sheet = wb.createSheet("Sheet"+SheetId);
						//sheet = wb.createSheet("Sheet1");
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthLocZeroQty(sheet);
						sheet = this.createHeaderLocZeroQty(sheet,styleHeader);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
						int index = 2;
						for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						Map lineArr = (Map) listQry.get(iCnt);
						 int k = 0;
													         
						dataStyle = createDataStyle(wb);
						HSSFRow row = sheet.createRow(index);
						HSSFCell cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc"))));
						cell.setCellStyle(dataStyle);
														
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("locdesc"))));
						cell.setCellStyle(dataStyle);
															
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc_type_id"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("loc_type_desc"))));
						cell.setCellStyle(dataStyle);
														
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
						cell.setCellStyle(dataStyle);
														
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString("0"));
						cell.setCellStyle(dataStyle);
	                     
						 
						index++;
						/*if((index-1)%maxRowsPerSheet==0){
						index = 1;
						sheet = wb.createSheet();*/
						 if((index-2)%maxRowsPerSheet==0){
							 index = 2;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthLocZeroQty(sheet);
						sheet = this.createHeaderLocZeroQty(sheet,styleHeader);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
						 }
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
					 
 private HSSFSheet createHeaderLocZeroQty(HSSFSheet sheet, HSSFCellStyle styleHeader){
		int k = 0;
		try{
								
			HSSFRow rowhead = sheet.createRow( 1);
			HSSFCell cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Location"));
			cell.setCellStyle(styleHeader);
							
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Description"));
			cell.setCellStyle(styleHeader);
							
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Location Type"));
			cell.setCellStyle(styleHeader);
							
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell( k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);
								
																		
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
			return sheet;
		}
 private HSSFSheet createWidthLocZeroQty(HSSFSheet sheet){
							
	try{
			sheet.setColumnWidth(0 ,4000);
			sheet.setColumnWidth(1 ,6000);
			sheet.setColumnWidth(2 ,4000);
			sheet.setColumnWidth(3 ,6000);
			sheet.setColumnWidth(4 ,3000);
								
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}				
						

	private HSSFWorkbook writeToExcelPaymentsummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="",PGaction="";
		String Order="",orderType="",orderNo="",custName="",paymentRefNo="",paymentMode="",paymentType="",paymentId="";
		OrderPaymentDAO _pmtdao=new OrderPaymentDAO();
		ArrayList listQry = new ArrayList();
		DecimalFormat decformat = new DecimalFormat("#,##0.00"); 
		
		try{
			HttpSession session = request.getSession();
			plant = StrUtils.fString((String)session.getAttribute("PLANT"));
			FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
			TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));

			if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			String curDate =_dateUtils.getDate();
			if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

			if (FROM_DATE.length()>5)

			fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

			if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			if (TO_DATE.length()>5)
			tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


			Order = StrUtils.fString(request.getParameter("Order"));
			custName     = StrUtils.fString(StrUtils.replaceCharacters2Recv(request.getParameter("custName")));
			paymentRefNo     = StrUtils.fString(request.getParameter("paymentRefNo"));
			orderNo     = StrUtils.fString(request.getParameter("orderNo"));
			paymentMode     = StrUtils.fString(request.getParameter("paymentMode"));
			paymentType     = StrUtils.fString(request.getParameter("paymentType"));
			orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
			paymentId     = StrUtils.fString(request.getParameter("PAYMENT_ID"));

			Hashtable ht = new Hashtable();
		      
			if(StrUtils.fString(Order).length() > 0)        ht.put("ORDERNAME",Order);
	        if(StrUtils.fString(paymentRefNo).length() > 0) ht.put("PAYMENT_REFNO",paymentRefNo);
	        if(StrUtils.fString(orderNo).length() > 0)      ht.put("ORDNO",orderNo);
	        if(StrUtils.fString(paymentMode).length() > 0)  ht.put("PAYMENT_MODE",paymentMode);
	        if(StrUtils.fString(paymentType).length() > 0)  ht.put("PAYMENT_TYPE",paymentType);
	        if(StrUtils.fString(orderType).length() > 0)    ht.put("ORDERTYPE",orderType);
	        if(StrUtils.fString(paymentId).length() > 0)    ht.put("PAYMENT_ID",paymentId);
	       
	        listQry = _ordPaymentUtil.getPaymentSummaryDetails(ht,fdate,tdate,plant,Order,custName);
	    	      
	      			 if (listQry.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						 sheet = wb.createSheet("Sheet1");
						 styleHeader = createStyleHeaderPaymentsummary(wb);
						 sheet = this.createWidthPaymentsummary(sheet);
						 sheet = this.createHeaderPaymentsummary(sheet,styleHeader);
						 String lastordno = "";double amount=0;				
						 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
							 Map lineArr = (Map) listQry.get(iCnt);	
							 int k = 0;
							 
							 String ordNo = (String)lineArr.get("ordno");
				               String order = (String)lineArr.get("ordername");
				               
				               double amtOrd = Double.parseDouble((String)lineArr.get("orderAmt"));
				               amtOrd = StrUtils.RoundDB(amtOrd,2);
				                double paidamt = Double.parseDouble((String)lineArr.get("amount_paid"));
				                if((!lastordno.equalsIgnoreCase("") && !lastordno.equalsIgnoreCase(ordNo)) )
				                {
				              	  amount = 0;
				                }
				                               
				                amount = amount + paidamt;
				                
				                paidamt = StrUtils.RoundDB(paidamt,2);
				                
				                /*String totpaid =  _pmtdao.gettotalpaidamt(plant, ordNo);
				                double DueToPay =  amtOrd - Double.parseDouble(totpaid);
				                DueToPay = StrUtils.RoundDB(DueToPay,2);
				                
				                double totpaid = Double.parseDouble((String)lineArr.get("amtReceived"))-Double.parseDouble((String)lineArr.get("returnamt"));
				                double DueToPay =  amtOrd - totpaid;*/
				                double DueToPay =  amtOrd - amount;
				                DueToPay = StrUtils.RoundDB(DueToPay,2);

				                          
				                String amtOrdered= decformat.format(amtOrd);
				                String amtpaid= decformat.format(paidamt);
				                String amtbal = decformat.format(DueToPay);
				                
				                if(order.equalsIgnoreCase("others"))
				                {
				                	amtbal="0.00";
				                }
				                lastordno = ordNo;
		
								    dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow((short) iCnt+1);
								    
								    HSSFCell cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ordno"))));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ordertype"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("custname"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_refno"))));
									cell.setCellStyle(dataStyle);
																			
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_dt"))));
									cell.setCellStyle(dataStyle);
																						
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_type"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_mode"))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_id"))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(amtOrdered));
									cell.setCellStyle(dataStyle);
																	
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(amtpaid));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(amtbal));
									cell.setCellStyle(dataStyle);

									cell = row.createCell((short) k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("payment_remarks"))));
									cell.setCellStyle(dataStyle);
									
									
									
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

	
	 private HSSFCellStyle createStyleHeaderPaymentsummary(HSSFWorkbook wb){
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
	 
	
	   private HSSFSheet createHeaderPaymentsummary(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Order No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Order Type"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Customer/Supplier"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment Ref No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment Date"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment Type"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment Mode"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Payment ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Orig Amt"));
			cell.setCellStyle(styleHeader);
							
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Paid Amt"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Outstg Amt"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);
							
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		private HSSFSheet createWidthPaymentsummary(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth((short)0 ,(short)5000);
				sheet.setColumnWidth((short)1 ,(short)5000);
				sheet.setColumnWidth((short)2 ,(short)7000);
				sheet.setColumnWidth((short)3 ,(short)4000);
				sheet.setColumnWidth((short)4 ,(short)4000);
				sheet.setColumnWidth((short)5 ,(short)4000);
				sheet.setColumnWidth((short)6 ,(short)4000);
				sheet.setColumnWidth((short)7 ,(short)6000);
				sheet.setColumnWidth((short)8 ,(short)6000);
				sheet.setColumnWidth((short)9 ,(short)6000);
				sheet.setColumnWidth((short)10 ,(short)6000);
				sheet.setColumnWidth((short)11 ,(short)10000);
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}

					
		private HSSFWorkbook writeToExcelKitBomSummary(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			ArrayList listQry = new ArrayList();
			ProductionBomUtil _ProductionBomUtil=new ProductionBomUtil();
			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
			ItemMstDAO itemMstDAO = new ItemMstDAO();  
			try{
				   HttpSession session = request.getSession();
				   plant = StrUtils.fString((String)session.getAttribute("PLANT"));
			       String PITEM = StrUtils.fString(request.getParameter("ITEM"));
	               String PDESC = StrUtils.fString(request.getParameter("DESC"));
	               String PDETAILDESC = StrUtils.fString(request.getParameter("DETDESC"));
	               String CITEM = StrUtils.fString(request.getParameter("CITEM"));
	               String CDESC = StrUtils.fString(request.getParameter("CDESC"));
	               String CDETDESC = StrUtils.fString(request.getParameter("CDETDESC"));
	               String EITEM = StrUtils.fString(request.getParameter("EITEM"));
	               String EDESC = StrUtils.fString(request.getParameter("EDESC"));
	               String EDETDESC = StrUtils.fString(request.getParameter("EDETDESC"));
                   Hashtable ht = new Hashtable();
    	            listQry = _ProductionBomUtil.getProdBomSummaryList(PITEM,plant,PDESC,PDETAILDESC,CITEM,CDESC,CDETDESC,EITEM,EDESC,EDETDESC, " AND BOMTYPE='KIT'");
		      			 if (listQry.size() > 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							 sheet = wb.createSheet("Sheet1");
							 styleHeader = createStyleHeader(wb);//createStyleHeaderPaymentsummary(wb);
							 sheet = this.createWidthKitBomSummary(sheet);
							 sheet = this.createHeaderKitBomSummary(sheet,styleHeader);
							 String lastordno = "";double amount=0;				
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								 Map lineArr = (Map) listQry.get(iCnt);	
								 int k = 0;
								 
								    String eitem="",edesc="",edetdesc="";
		                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
		                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
		                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
		                                               
		                            String pdesc = itemMstDAO.getItemDesc(plant, parentitem);
		                           	String pdetdesc = itemMstDAO.getItemDetailDesc(plant,parentitem);
		                           	String cdesc = itemMstDAO.getItemDesc(plant, childitem);
		                           	String cdetdesc = itemMstDAO.getItemDetailDesc(plant, childitem);
		                           
		                            eitem = ProdBomDao.getEquivalentitem(plant,parentitem,childitem);
		                            if(eitem.length()>0)
		                            {
		                            	edesc = itemMstDAO.getItemDesc(plant, eitem);
		                            	edetdesc = itemMstDAO.getItemDetailDesc(plant,eitem);
		                            }
			
									    dataStyle = createDataStyle(wb);
									    HSSFRow row = sheet.createRow((short) iCnt+1);
									    
									    HSSFCell cell = row.createCell( k++);
										cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
									    cell.setCellStyle(dataStyle);
									    
									    cell = row.createCell( k++);
										cell.setCellValue(parentitem);
										cell.setCellStyle(dataStyle);
									    									 										
									    cell = row.createCell((short) k++);
										cell.setCellValue(pdesc);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(pdetdesc);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(childitem);
										cell.setCellStyle(dataStyle);
																				
										cell = row.createCell((short) k++);
										cell.setCellValue(cdesc);
										cell.setCellStyle(dataStyle);
																							
										cell = row.createCell((short) k++);
										cell.setCellValue(cdetdesc);
										cell.setCellStyle(dataStyle);
										
										/*cell = row.createCell((short) k++);
										cell.setCellValue(eitem);
										cell.setCellStyle(dataStyle);
									
										cell = row.createCell((short) k++);
										cell.setCellValue(edesc);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(edetdesc);
										cell.setCellStyle(dataStyle);*/
										
										cell = row.createCell((short) k++);
										cell.setCellValue(qty);
										cell.setCellStyle(dataStyle);
																		
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
	
		private HSSFSheet createWidthKitBomSummary(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth((short)0 ,(short)2000);
				sheet.setColumnWidth((short)1 ,(short)5000);
				sheet.setColumnWidth((short)2 ,(short)7000);
				sheet.setColumnWidth((short)3 ,(short)7000);
				sheet.setColumnWidth((short)4 ,(short)5000);
				sheet.setColumnWidth((short)5 ,(short)7000);
				sheet.setColumnWidth((short)6 ,(short)7000);
				sheet.setColumnWidth((short)7 ,(short)7000);
				sheet.setColumnWidth((short)8 ,(short)9000);
				sheet.setColumnWidth((short)9 ,(short)9000);
				sheet.setColumnWidth((short)10 ,(short)4000);
		
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		  private HSSFSheet createHeaderKitBomSummary(HSSFSheet sheet, HSSFCellStyle styleHeader){
				int k = 0;
				try{
							
				HSSFRow rowhead = sheet.createRow((short) 0);
				HSSFCell cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("No"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Parent Product"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Parent Product Desc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Parent Product Detail Desc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Child Product"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Child Product Desc"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Child Product Detail Desc"));
				cell.setCellStyle(styleHeader);
				
				/*cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Equivalent Product"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Equivalent Product Desc"));
				cell.setCellStyle(styleHeader);
								
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("Equivalent Product Detail Desc"));
				cell.setCellStyle(styleHeader);*/
				
				cell = rowhead.createCell((short) k++);
				cell.setCellValue(new HSSFRichTextString("BOM QTY"));
				cell.setCellStyle(styleHeader);
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
   private HSSFWorkbook writeToExcelSupplierDiscount(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
				String plant = "";
				ArrayList listQry = new ArrayList();
				ItemUtil _ItemUtil=new ItemUtil();
				ProductionBomDAO ProdBomDao = new ProductionBomDAO();
				ItemMstDAO itemMstDAO = new ItemMstDAO();  
				try{
					   HttpSession session = request.getSession();
					   plant = StrUtils.fString((String)session.getAttribute("PLANT"));
				       String ITEM = StrUtils.fString(request.getParameter("ITEM"));
		               String DESC = StrUtils.fString(request.getParameter("ITEM_DESC"));
		               String BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
		               String CLASS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
		               String TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
		               
		               PlantMstDAO plantMstDAO = new PlantMstDAO();
		               String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);	               
		               
	                   Hashtable ht = new Hashtable();
	    	            listQry = _ItemUtil.getSupplierDiscountList(plant, ITEM, DESC, BRAND, CLASS, TYPE);
			      			 if (listQry.size() > 0) {
								HSSFSheet sheet = null ;
								HSSFCellStyle styleHeader = null;
								HSSFCellStyle dataStyle = null;
								 sheet = wb.createSheet("Sheet1");
								 styleHeader = createStyleHeader(wb);//createStyleHeaderPaymentsummary(wb);
								 sheet = this.createWidthSupplierDiscount(sheet);
								 sheet = this.createHeaderSupplierDiscount(sheet,styleHeader);
								 String lastordno = "";double amount=0;				
								 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
									 Map lineArr = (Map) listQry.get(iCnt);	
									 int k = 0;
									 
									    String item = StrUtils.fString((String)lineArr.get("ITEM")) ;
			                            String vendno = StrUtils.fString((String)lineArr.get("VENDNO")) ;
			                            String discount = (String)lineArr.get("IBDISCOUNT") ;
			                            
			                            
			                            
										    dataStyle = createDataStyle(wb);
										    HSSFRow row = sheet.createRow((short) iCnt+1);
										    
										    HSSFCell cell = row.createCell( k++);
										    cell.setCellValue(item);
										    cell.setCellStyle(dataStyle);
										    
										    cell = row.createCell( k++);
											cell.setCellValue(vendno);
											cell.setCellStyle(dataStyle);
										    
											final String metaCharacters = "%";
				                            if(discount.contains(metaCharacters)){
				                            	String discountValue = discount.replace(metaCharacters,"");
				                            	discount=discountValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				                            	cell = row.createCell((short) k++);
												cell.setCellValue(discount+"%");
												cell.setCellStyle(dataStyle);
				                            }else {
				                            	/*float discountVal ="".equals(discount) ? 0.0f :  Float.parseFloat(discount);					                           	
					                            if(discountVal==0f) {
					                            	discount="0.000";
					                            }else {
					                            	discount=discount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					                            }*/
				                            	double discountVal ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
				                            	discount = StrUtils.addZeroes(discountVal, numberOfDecimal);
				                            	
				                            	cell = row.createCell((short) k++);
												cell.setCellValue(discount);
												cell.setCellStyle(dataStyle);
				                            }
											
										    
																													
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
		
	private HSSFSheet createWidthSupplierDiscount(HSSFSheet sheet){
				
				try{
					sheet.setColumnWidth((short)0 ,(short)4000);
					sheet.setColumnWidth((short)1 ,(short)4000);
					sheet.setColumnWidth((short)2 ,(short)4000);
					
				}
				catch(Exception e){
					this.mLogger.exception(this.printLog, "", e);
				}
				return sheet;
			}
	private HSSFSheet createHeaderSupplierDiscount(HSSFSheet sheet, HSSFCellStyle styleHeader){
					int k = 0;
					try{
								
					HSSFRow rowhead = sheet.createRow((short) 0);
					HSSFCell cell = rowhead.createCell((short) k++);
					cell.setCellValue(new HSSFRichTextString("Item"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell((short) k++);
					cell.setCellValue(new HSSFRichTextString("Supplier ID"));
					cell.setCellStyle(styleHeader);
					
					cell = rowhead.createCell((short) k++);
					cell.setCellValue(new HSSFRichTextString("Purchase Discount"));
					cell.setCellStyle(styleHeader);
					
					
					}
					catch(Exception e){
						this.mLogger.exception(this.printLog, "", e);
					}
					return sheet;
		}	
	private HSSFWorkbook writeToExcelCustomerDiscount(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		ArrayList listQry = new ArrayList();
		ItemUtil _ItemUtil=new ItemUtil();
		ProductionBomDAO ProdBomDao = new ProductionBomDAO();
		ItemMstDAO itemMstDAO = new ItemMstDAO();  
		try{
			   HttpSession session = request.getSession();
			   plant = StrUtils.fString((String)session.getAttribute("PLANT"));
		       String ITEM = StrUtils.fString(request.getParameter("ITEM"));
               String DESC = StrUtils.fString(request.getParameter("ITEM_DESC"));
               String BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
               String CLASS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
               String TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
               
               PlantMstDAO plantMstDAO = new PlantMstDAO();
               String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
               
               Hashtable ht = new Hashtable();
	            listQry = _ItemUtil.getCustomerDiscountList(plant, ITEM, DESC, BRAND, CLASS, TYPE);
	      			 if (listQry.size() > 0) {
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						 sheet = wb.createSheet("Sheet1");
						 styleHeader = createStyleHeader(wb);//createStyleHeaderPaymentsummary(wb);
						 sheet = this.createWidthCustomerDiscount(sheet);
						 sheet = this.createHeaderCustomerDiscount(sheet,styleHeader);
						 String lastordno = "";double amount=0;				
						 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
							 Map lineArr = (Map) listQry.get(iCnt);	
							 int k = 0;
							 
							    String item = StrUtils.fString((String)lineArr.get("ITEM")) ;
	                            String custno = StrUtils.fString((String)lineArr.get("CUSTOMER_TYPE_ID")) ;
	                            String discount = StrUtils.fString((String)lineArr.get("OBDISCOUNT")) ;
	                            
	                            
	                           				
								    dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow((short) iCnt+1);
								    
								    HSSFCell cell = row.createCell( k++);
								    cell.setCellValue(item);
								    cell.setCellStyle(dataStyle);
								    
								    cell = row.createCell( k++);
									cell.setCellValue(custno);
									cell.setCellStyle(dataStyle);
								    
									final String metaCharacters = "%";
		                            if(discount.contains(metaCharacters)){
		                            	String discountValue = discount.replace(metaCharacters,"");
		                            	discount=discountValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		                            	cell = row.createCell((short) k++);
										cell.setCellValue(discount+"%");
										cell.setCellStyle(dataStyle);
		                            }else {
		                            	/*float disCountValue ="".equals(discount) ? 0.0f :  Float.parseFloat(discount);			                           	
			                            if(disCountValue==0f) {
			                            	discount="0.000";
			                            }else {
			                            	discount=discount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			                            }*/
		                            	
		                            	double disCountValue ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);	
		                            	discount = StrUtils.addZeroes(disCountValue, numberOfDecimal);
		                            	
			                            cell = row.createCell((short) k++);
										cell.setCellValue(discount);
										cell.setCellStyle(dataStyle);
		                            }
									
																											
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

private HSSFSheet createWidthCustomerDiscount(HSSFSheet sheet){
		
		try{
			sheet.setColumnWidth((short)0 ,(short)4000);
			sheet.setColumnWidth((short)1 ,(short)5000);
			sheet.setColumnWidth((short)2 ,(short)4000);
			
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
private HSSFSheet createHeaderCustomerDiscount(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
						
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Item"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("CustomerType ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Sales Discount"));
			cell.setCellStyle(styleHeader);
			
			
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
}	

private HSSFWorkbook writeToExcelInvAging(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String plant = "",fdate="",tdate="";
    int maxRowsPerSheet = 65535;
    	
	int SheetId =1;
	DateUtils _dateUtils = new DateUtils();
	StrUtils strUtils =  new StrUtils();
	ArrayList listQry = new ArrayList();
	AgeingUtil ageingUtil = new AgeingUtil();
	try{
			HttpSession session = request.getSession();
			plant = (String) session.getAttribute("PLANT");
			String PLANT = session.getAttribute("PLANT").toString();
	           String LOC     = strUtils.fString(request.getParameter("LOC"));
	           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
	           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	           String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	           // Start code modified by Lakshmi for product brand on 11/9/12 
	           String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	           String  LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
	           String  LOC_TYPE_ID3 = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
	           String radiobtnval = strUtils.fString(request.getParameter("radiobtnval"));
	           String FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	           String STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	           String curDate =_dateUtils.getDate();
	           String reportType ="";
	           
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		          curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	           
	           if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
	             
		        if (FROM_DATE.length()>5)
		        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

		       	if (TO_DATE.length()>5)
		        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
		       	
	           Hashtable ht = new Hashtable();

	            if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
	            //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
	            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
	            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("c.PRD_CLS_ID",PRD_CLS_ID);
	            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("c.ITEMTYPE",PRD_TYPE_ID) ;  
	            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("c.PRD_BRAND_ID",PRD_BRAND_ID) ; 
	            //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
	            listQry= ageingUtil.getInvListAgingSummaryByCost( PLANT,ITEM,PRD_DESCRIP,ht,LOC,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,fdate,tdate);
	            if (listQry.size() > 0) {
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					HSSFCellStyle CompHeader = null;
					 sheet = wb.createSheet("Sheet"+SheetId);
					 styleHeader = createStyleHeader(wb);
					 CompHeader = createCompStyleHeader(wb);
					 sheet = this.createWidthInvAgingReports(sheet);
					 	 	 
					 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
					 int index = 2;		
					 double sumprdQty = 0;
		            double recQty = 0;
		            double lastSumPrdQty = 0;
		            String lastProduct="",lastDescription="",lastLocation="",lastBatch="",lastuom="";
		            String item="",loc="",batch="",uom="";
		            double days30=0;double days60=0;double days90=0;double days120=0;
		            double prevdays30=0;double prevdays60=0;double prevdays90=0;double prevdays120=0;
		            curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
					 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						 Map lineArr = (Map) listQry.get(iCnt);	
						 int k = 0;
						 
						 String recdate = strUtils.fString((String)lineArr.get("RECVDATE"));
                            item = (String)lineArr.get("ITEM");
                            loc= (String)lineArr.get("LOC");
                            batch = (String)lineArr.get("BATCH");
                            sumprdQty = Double.parseDouble((String)lineArr.get("QTY"));
                            recQty = Double.parseDouble((String)lineArr.get("RECQTY"));
                            uom=strUtils.fString((String)lineArr.get("STKUOM"));
                        	recdate = recdate.substring(0,2) + recdate.substring(3,5) + recdate.substring(6);
                        	Calendar cal1 = new GregorianCalendar();
     		               Calendar cal2 = new GregorianCalendar();
     		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
     		               Date d1 = sdf.parse(recdate);
     		               cal1.setTime(d1);
     		               Date d2 = sdf.parse(curDate);
     		               cal2.setTime(d2);         		               
     		               Long diff = d2.getTime()-d1.getTime();         		               
     		               int days =  (int) (diff/(1000 * 60 * 60 * 24));
     		              if(days>0 && days<=30)
   		            	   {
   		            		   days30 = days30 + recQty;
   		            	   }
   		            	   if(days>30 && days<=60)
   		            	   {
   		            		   days60 = days60 + recQty;
   		            	   }
   		            	   if(days>60 && days<=90)
   		            	   {
   		            		   days90 = days90 + recQty;
   		            	   }
   		            	   if(days>90)
   		            	   {
   		            		   days120 = days120 + recQty;
   		            	   }
   		            	   
   		            	if(!(lastProduct.equalsIgnoreCase(item)&&lastLocation.equalsIgnoreCase(loc)&&lastBatch.equalsIgnoreCase(batch)&&lastSumPrdQty == sumprdQty)){
   		            		if(!lastProduct.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")){
   		            			if(prevdays30>lastSumPrdQty){
                        			   prevdays30 = lastSumPrdQty;
                        		   }
                        		   if((prevdays30+prevdays60)>lastSumPrdQty){
                        			   prevdays60 = lastSumPrdQty-prevdays30;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90)>lastSumPrdQty){
                        			   prevdays90 = lastSumPrdQty-prevdays30-prevdays60;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90+prevdays120)>lastSumPrdQty){
                        			   prevdays120 = lastSumPrdQty-prevdays30-prevdays60-prevdays90;
                        		   }
                        		   
                        		   dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastProduct)));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastDescription)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastLocation)));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastBatch)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastuom)));
									cell.setCellStyle(dataStyle);
														
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(lastSumPrdQty)))));
									/*cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Double.toString(lastSumPrdQty))));*/
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays30)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays60)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays90)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays120)))));
									cell.setCellStyle(dataStyle);
									
									days30=0;days60=0;days90=0;days120=0;
                        		   prevdays30=0;prevdays60=0;prevdays90=0;prevdays120=0;
                        		   if(days>0 && days<=30)
           		            	   {
           		            		   days30 = days30 + recQty;
           		            	   }
           		            	   if(days>30 && days<=60)
           		            	   {
           		            		   days60 = days60 + recQty;
           		            	   }
           		            	   if(days>60 && days<=90)
           		            	   {
           		            		   days90 = days90 + recQty;
           		            	   }
           		            	   if(days>90)
           		            	   {
           		            		   days120 = days120 + recQty;
           		            	   }
           		            	   
           		            	index++;
           		            	if((index-2)%maxRowsPerSheet==0){
									 index = 2;
									 SheetId++;
									 sheet = wb.createSheet("Sheet"+SheetId);
									 styleHeader = createStyleHeader(wb);
									 CompHeader = createCompStyleHeader(wb);
									 sheet = this.createWidthInvAgingReports(sheet);
									 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
									 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
								 }
   		            		}	
   		            	}
   		            	
   		            	if(iCnt+1 == listQry.size()){
   		            		if(days30>sumprdQty){
                  			   days30 = sumprdQty;
	                     		   }
        		            	   if((days30+days60)>sumprdQty){
	                     			   days60 = sumprdQty-days30;
	                     		   }
        		            	   if((days30+days60+days90)>sumprdQty){
	                     			   days90 = sumprdQty-days30-days60;
	                     		   }
        		            	   if((days30+days60+days90+days120)>sumprdQty){
	                     			   days120 = sumprdQty-days30-days60-days90;
	                     		   }
        		            	   dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(item)));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(loc)));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(batch)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(uom)));
									cell.setCellStyle(dataStyle);
														
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString (StrUtils.formatNum(Double.toString(sumprdQty)))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(days30)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(days60)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(days90)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(days120)))));
									cell.setCellStyle(dataStyle);
									
									index++;
									if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetId++;
										 sheet = wb.createSheet("Sheet"+SheetId);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidthInvAgingReports(sheet);
										 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
									 }
   		            	}
							
							
							lastProduct = item;
	                         lastLocation= loc;
	                         lastBatch = batch;
	                         lastuom = uom;
	                         lastSumPrdQty = sumprdQty;
	                         lastDescription = (String)lineArr.get("ITEMDESC");
	                         prevdays30 = days30;
	                         prevdays60 = days60;
	                         prevdays90 = days90;
	                         prevdays120 = days120;

						 		
								 
								
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

private HSSFWorkbook writeToExcelInvAgingReports(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String plant = "",fdate="",tdate="";
    int maxRowsPerSheet = 65535;
    	
	int SheetId =1;
	DateUtils _dateUtils = new DateUtils();
	StrUtils strUtils =  new StrUtils();
	ArrayList listQry = new ArrayList();
	InvUtil invUtil =  new InvUtil();
	try{
			HttpSession session = request.getSession();
			plant = (String) session.getAttribute("PLANT");
			String PLANT = session.getAttribute("PLANT").toString();
	           String LOC     = strUtils.fString(request.getParameter("LOC"));
	           String ITEM    = strUtils.fString(request.getParameter("ITEM"));
	           String BATCH   = strUtils.fString(request.getParameter("BATCH"));
	           String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	           String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	           String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
	           // Start code modified by Lakshmi for product brand on 11/9/12 
	           String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	           String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	           String curDate =_dateUtils.getDate();
	           String reportType ="";  			
	           Hashtable ht = new Hashtable();

	            if(strUtils.fString(PLANT).length() > 0)               ht.put("a.PLANT",PLANT);
	            //if(strUtils.fString(LOC).length() > 0)                 ht.put("a.LOC",LOC);
	            if(strUtils.fString(BATCH).length() > 0)               ht.put("a.USERFLD4",BATCH);
	            if(strUtils.fString(PRD_CLS_ID).length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
	            if(strUtils.fString(PRD_TYPE_ID).length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;  
	            if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ; 
	            //if(strUtils.fString(LOC_TYPE_ID).length() > 0)         ht.put("isnull(C.LOC_TYPE_ID,'')",LOC_TYPE_ID);
	            listQry= new InvUtil().getInvListAgingSummaryByCost( PLANT,ITEM,PRD_DESCRIP,ht,LOC,LOC_TYPE_ID,curDate);//"","","","",plant,"");
	            if (listQry.size() > 0) {
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					HSSFCellStyle CompHeader = null;
					 sheet = wb.createSheet("Sheet"+SheetId);
					 styleHeader = createStyleHeader(wb);
					 CompHeader = createCompStyleHeader(wb);
					 sheet = this.createWidthInvAgingReports(sheet);
					 	 	 
					 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
					 int index = 2;		
					 double sumprdQty = 0;
		            double recQty = 0;
		            double lastSumPrdQty = 0;
		            String lastProduct="",lastDescription="",lastLocation="",lastBatch="",lastuom="";
		            String item="",loc="",batch="",uom="";
		            double days30=0;double days60=0;double days90=0;double days120=0;
		            double prevdays30=0;double prevdays60=0;double prevdays90=0;double prevdays120=0;
		            curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
					 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						 Map lineArr = (Map) listQry.get(iCnt);	
						 int k = 0;
						 
						 String recdate = strUtils.fString((String)lineArr.get("RECVDATE"));
                            item = (String)lineArr.get("ITEM");
                            loc= (String)lineArr.get("LOC");
                            batch = (String)lineArr.get("BATCH");
                            sumprdQty = Double.parseDouble((String)lineArr.get("QTY"));
                            recQty = Double.parseDouble((String)lineArr.get("RECQTY"));
                            uom=strUtils.fString((String)lineArr.get("STKUOM"));
                        	recdate = recdate.substring(0,2) + recdate.substring(3,5) + recdate.substring(6);
                        	Calendar cal1 = new GregorianCalendar();
     		               Calendar cal2 = new GregorianCalendar();
     		               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
     		               Date d1 = sdf.parse(recdate);
     		               cal1.setTime(d1);
     		               Date d2 = sdf.parse(curDate);
     		               cal2.setTime(d2);         		               
     		               Long diff = d2.getTime()-d1.getTime();         		               
     		               int days =  (int) (diff/(1000 * 60 * 60 * 24));
     		              if(days>0 && days<=30)
   		            	   {
   		            		   days30 = days30 + recQty;
   		            	   }
   		            	   if(days>30 && days<=60)
   		            	   {
   		            		   days60 = days60 + recQty;
   		            	   }
   		            	   if(days>60 && days<=90)
   		            	   {
   		            		   days90 = days90 + recQty;
   		            	   }
   		            	   if(days>90)
   		            	   {
   		            		   days120 = days120 + recQty;
   		            	   }
   		            	   
   		            	if(!(lastProduct.equalsIgnoreCase(item)&&lastLocation.equalsIgnoreCase(loc)&&lastBatch.equalsIgnoreCase(batch)&&lastSumPrdQty == sumprdQty)){
   		            		if(!lastProduct.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")&&!lastLocation.equalsIgnoreCase("")){
   		            			if(prevdays30>lastSumPrdQty){
                        			   prevdays30 = lastSumPrdQty;
                        		   }
                        		   if((prevdays30+prevdays60)>lastSumPrdQty){
                        			   prevdays60 = lastSumPrdQty-prevdays30;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90)>lastSumPrdQty){
                        			   prevdays90 = lastSumPrdQty-prevdays30-prevdays60;
                        		   }
                        		   if((prevdays30+prevdays60+prevdays90+prevdays120)>lastSumPrdQty){
                        			   prevdays120 = lastSumPrdQty-prevdays30-prevdays60-prevdays90;
                        		   }
                        		   
                        		   dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastProduct)));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastDescription)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastLocation)));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastBatch)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(lastuom)));
									cell.setCellStyle(dataStyle);
														
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(lastSumPrdQty)))));
									/*cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Double.toString(lastSumPrdQty))));*/
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays30)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays60)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays90)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(prevdays120)))));
									cell.setCellStyle(dataStyle);
									
									days30=0;days60=0;days90=0;days120=0;
                        		   prevdays30=0;prevdays60=0;prevdays90=0;prevdays120=0;
                        		   if(days>0 && days<=30)
           		            	   {
           		            		   days30 = days30 + recQty;
           		            	   }
           		            	   if(days>30 && days<=60)
           		            	   {
           		            		   days60 = days60 + recQty;
           		            	   }
           		            	   if(days>60 && days<=90)
           		            	   {
           		            		   days90 = days90 + recQty;
           		            	   }
           		            	   if(days>90)
           		            	   {
           		            		   days120 = days120 + recQty;
           		            	   }
           		            	   
           		            	index++;
           		            	if((index-2)%maxRowsPerSheet==0){
									 index = 2;
									 SheetId++;
									 sheet = wb.createSheet("Sheet"+SheetId);
									 styleHeader = createStyleHeader(wb);
									 CompHeader = createCompStyleHeader(wb);
									 sheet = this.createWidthInvAgingReports(sheet);
									 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
									 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
								 }
   		            		}	
   		            	}
   		            	
   		            	if(iCnt+1 == listQry.size()){
   		            		if(days30>sumprdQty){
                  			   days30 = sumprdQty;
	                     		   }
        		            	   if((days30+days60)>sumprdQty){
	                     			   days60 = sumprdQty-days30;
	                     		   }
        		            	   if((days30+days60+days90)>sumprdQty){
	                     			   days90 = sumprdQty-days30-days60;
	                     		   }
        		            	   if((days30+days60+days90+days120)>sumprdQty){
	                     			   days120 = sumprdQty-days30-days60-days90;
	                     		   }
        		            	   dataStyle = createDataStyle(wb);
								    HSSFRow row = sheet.createRow(index);
								    
								    HSSFCell cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(item)));
									cell.setCellStyle(dataStyle);
									
								    cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(loc)));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(batch)));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(uom)));
									cell.setCellStyle(dataStyle);
														
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString (StrUtils.formatNum(Double.toString(sumprdQty)))));
									cell.setCellStyle(dataStyle);
								
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(days30)))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(days60)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(Double.toString(days90)))));
									cell.setCellStyle(dataStyle);
										
									cell = row.createCell(k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.formatNum(Double.toString(days120)))));
									cell.setCellStyle(dataStyle);
									
									index++;
									if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetId++;
										 sheet = wb.createSheet("Sheet"+SheetId);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidthInvAgingReports(sheet);
										 sheet = this.createHeaderInvAgingReports(sheet,styleHeader);
									 }
   		            	}
							
							
							lastProduct = item;
	                         lastLocation= loc;
	                         lastBatch = batch;
	                         lastuom = uom;
	                         lastSumPrdQty = sumprdQty;
	                         lastDescription = (String)lineArr.get("ITEMDESC");
	                         prevdays30 = days30;
	                         prevdays60 = days60;
	                         prevdays90 = days90;
	                         prevdays120 = days120;

						 		
								 
								
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


private HSSFSheet createWidthInvAgingReports(HSSFSheet sheet){
	
	try{
		sheet.setColumnWidth(0 ,5000);
		sheet.setColumnWidth(1 ,4000);
		sheet.setColumnWidth(2 ,5000);
		sheet.setColumnWidth(3 ,5000);
		sheet.setColumnWidth(4 ,4000);
		sheet.setColumnWidth(5 ,8000);
		sheet.setColumnWidth(6 ,3000);
		sheet.setColumnWidth(7 ,5000);
		sheet.setColumnWidth(8 ,3500);
		sheet.setColumnWidth(9 ,3500);
		
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFSheet createHeaderInvAgingReports(HSSFSheet sheet, HSSFCellStyle styleHeader){
	int k = 0;
	try{
		
	HSSFRow rowhead = sheet.createRow(1);
	HSSFCell cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Product ID"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Description"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Loc"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Batch"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("UOM"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Total Quantity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("1-30 days"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("31-60 days"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("61-90 days"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("90+ days"));
	cell.setCellStyle(styleHeader);
											
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFWorkbook writeToInvVsoutgoingReport(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String plant = "";
	DateUtils _dateUtils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	ArrayList listQry = new ArrayList();
	HTReportUtil movHisUtil = new HTReportUtil();
	int SheetId =1;
	try{
		HttpSession session = request.getSession();
		String PLANT = session.getAttribute("PLANT").toString();
		String userID = (String) session.getAttribute("LOGIN_USER");
		   String ITEM    = strUtils.fString(request.getParameter("ITEM"));
		   String PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
		   String  PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
		   String  PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
		   String  PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
		   String  FROM_DATE        = strUtils.fString(request.getParameter("FROM_DATE"));
           String  TO_DATE         = strUtils.fString(request.getParameter("TO_DATE"));
           String  LOC     = strUtils.fString(request.getParameter("LOC"));
        String  LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
          // String reportType = StrUtils.fString(request.getParameter("INV_REP_TYPE"));
           String reportType ="";
           
           java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
           
           String fdate ="",tdate="";
            if (FROM_DATE.length()>5)
                   fdate   = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
                   
            if (TO_DATE.length()>5){
             tdate  = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
            }else{
            	tdate = new DateUtils().getDateFormatyyyyMMdd();
            }
	        Hashtable ht = new Hashtable();
	        if(strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
			if(strUtils.fString(PRD_TYPE_ID).length() > 0)        ht.put("itemtype",PRD_TYPE_ID); 
			if(strUtils.fString(PRD_BRAND_ID).length() > 0)        ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
			
         /*  if(reportType.equalsIgnoreCase("INVOPENCLSBYPRODUCT")){*/
        	   listQry= new InvUtil().getInvVsOutgoing(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate);
          /* }*//*else if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC")){
        	   listQry= new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"");
           }else if(reportType.equalsIgnoreCase("INVOPENCLS_BYLOC_WAVGCOST")){
        	   listQry= new InvUtil().getInvWithOpenCloseBalByLoc(PLANT,ITEM,PRD_DESCRIP,ht,fdate,tdate,LOC,LOC_TYPE_ID,"WITH_AVGCOST");
           }*/
       
	
		 if (listQry.size() > 0) {
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					HSSFCellStyle CompHeader = null;
					sheet = wb.createSheet("Sheet"+SheetId);
					//sheet = wb.createSheet("Sheet1");
					 styleHeader = createStyleHeader(wb);
					 CompHeader = createCompStyleHeader(wb);
					 sheet = this.createWidthInvVsOut(sheet,reportType);
					 sheet = this.createHeaderVsOut(sheet,styleHeader,reportType);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
									
					 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						 Map lineArr = (Map) listQry.get(iCnt);	
						 int k = 0;
						 	
						   
                          //  sumOfTotalCost = sumOfTotalCost + TotalCost;
                         //   sumOfTotalPrice = sumOfTotalPrice + TotalPrice;
                            
						 		dataStyle = createDataStyle(wb);
							    HSSFRow row = sheet.createRow( iCnt+2);
							    
							    HSSFCell cell = row.createCell( k++);
							  
							
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
								cell.setCellStyle(dataStyle);
							 
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("UOM"))));
								cell.setCellStyle(dataStyle);							   
							
								cell = row.createCell( k++);
								cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(((String)lineArr.get("TOTRECV").toString())))));								
								cell.setCellStyle(dataStyle);
							
								cell = row.createCell( k++);								
								cell.setCellValue(Double.parseDouble(StrUtils.fString(StrUtils.formatNum(((String)lineArr.get("TOTAL_ISS").toString())))));
								cell.setCellStyle(dataStyle);
							
								cell = row.createCell( k++);								
								cell.setCellValue(StrUtils.fString(StrUtils.formatNum(((String)lineArr.get("STOCKONHAND").toString()))));
								cell.setCellStyle(dataStyle);
								 
							
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
private HSSFSheet createWidthInvVsOut(HSSFSheet sheet,String reportType){
	
	try{
		
	
		sheet.setColumnWidth(0 ,5000);
		sheet.setColumnWidth(1 ,12000);
		sheet.setColumnWidth(2 ,5000);
		sheet.setColumnWidth(3 ,5000);
		sheet.setColumnWidth(4 ,5000);			
		
		
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
private HSSFSheet createHeaderVsOut(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType){
	int k = 0;
	try{
	
	
	HSSFRow rowhead = sheet.createRow(1);
	HSSFCell cell = rowhead.createCell( k++);

	
	cell.setCellValue(new HSSFRichTextString("Product ID"));
	cell.setCellStyle(styleHeader);
	
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Description"));
	cell.setCellStyle(styleHeader);		
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("UOM"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Total Received"));
	cell.setCellStyle(styleHeader);			

	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Total Issued"));
	cell.setCellStyle(styleHeader);
	
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Stock On Hand"));
	cell.setCellStyle(styleHeader);

											
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFWorkbook writeToCustomerDiscountSummartExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String PLANT = "";
	int maxRowsPerSheet = 65535;

	try{
		List listQry = new ArrayList();
		StrUtils strUtils = new StrUtils();
		HttpSession session = request.getSession();
		PLANT = session.getAttribute("PLANT").toString();
		int SheetNo =1;	
		
		//
		PLANT= session.getAttribute("PLANT").toString();
		String USERID= session.getAttribute("LOGIN_USER").toString();
		String LOC     = strUtils.fString(request.getParameter("LOC"));
		String ITEM    = strUtils.fString(request.getParameter("ITEM"));
		String CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
		String sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
		String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
		
		ITEM = new ItemMstUtil().isValidInvAlternateItemInItemmst( PLANT, ITEM);
		
		PlantMstDAO plantMstDAO = new PlantMstDAO();
        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	      
	      
	      Hashtable ht = new Hashtable();
	      ht.put("M.PLANT",PLANT);
	      if(strUtils.fString(PLANT).length() > 0)        
	      if(strUtils.fString(ITEM).length() > 0)      
	      {
	        ht.put("I.ITEM",ITEM);
	      } 
	     
	      
	      if(strUtils.fString(CUSTOMER).length() > 0)      
	      {
	        ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
	      }
	      
	      if(strUtils.fString(sCustomerTypeId).length() > 0)      
	      {
	        ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
	      }
	      
	      listQry = new CustMstDAO().getCustomerForReport(ht,PRD_DESCRIP);
		//
			
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					dataStyle = createDataStyle(wb);
					 sheet = wb.createSheet("Sheet"+SheetNo);
					 styleHeader = createStyleHeader(wb);
					 sheet = this.createContactsDiscountWidth(sheet);
					 sheet = this.createContactsDiscountHeader(sheet,styleHeader);
					 int index = 1;
					 if (listQry.size() > 0) {
					 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						
							   Map lineArr = (Map) listQry.get(iCnt);	
							    int k = 0;
							    String discountType="";
				                String discount=(String)lineArr.get("OBDISCOUNT");
				                double UnitPrice =Double.parseDouble((String)lineArr.get("UnitPrice"));
				                double discountedcost=0.00d;
				                double converteddiscount=0.00d;
				                double discount1=0.00d;
				                double discountedper=0.00d;
				                int plusIndex = discount.indexOf("%");
				                if (plusIndex != -1) {
				                	discount = discount.substring(0, plusIndex);
				                	converteddiscount=Double.parseDouble(discount);
				                	discountType = "BYPERCENTAGE";
				                	discountedcost=(converteddiscount/100)*UnitPrice;
				                	discountedper=UnitPrice-discountedcost;
				                   }
				                else{
				                	 discount1 =Double.parseDouble(discount);
				                }
				                
				                String discountedPerValue = String.valueOf(discountedper);
				                String discount1Value = String.valueOf(discount1);
				                String UnitPriceValue = String.valueOf(UnitPrice);
				                
				                /*float discountedPerVal ="".equals(discountedPerValue) ? 0.0f :  Float.parseFloat(discountedPerValue);
				                float discount1Val ="".equals(discount1Value) ? 0.0f :  Float.parseFloat(discount1Value);
				                float UnitPriceVal ="".equals(UnitPriceValue) ? 0.0f :  Float.parseFloat(UnitPriceValue);
					    		
					    		if(discountedPerVal==0f){
					    			discountedPerValue="0.000";
					    		}else{
					    			discountedPerValue=discountedPerValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					    		}if(discount1Val==0f){
					    			discount1Value="0.000";
					    		}else{
					    			discount1Value=discount1Value.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					    		}if(UnitPriceVal==0f){
					    			UnitPriceValue="0.00000";
					    		}else{
					    			UnitPriceValue=UnitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					    		}*/
				                
				                double discountedPerVal ="".equals(discountedPerValue) ? 0.0d :  Double.parseDouble(discountedPerValue);
				                double discount1Val ="".equals(discount1Value) ? 0.0d :  Double.parseDouble(discount1Value);
				                double UnitPriceVal ="".equals(UnitPriceValue) ? 0.0d :  Double.parseDouble(UnitPriceValue);
				                
				                discountedPerValue = StrUtils.addZeroes(discountedPerVal, numberOfDecimal);
				                discount1Value = StrUtils.addZeroes(discount1Val, numberOfDecimal);
				                UnitPriceValue = StrUtils.addZeroes(UnitPriceVal, numberOfDecimal);
				                
							    HSSFRow row = sheet.createRow(index);
							  
							    HSSFCell cell = row.createCell( k++);								    
							    
							    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTID"))));
								cell.setCellStyle(dataStyle);
								
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTTYPE"))));
								cell.setCellStyle(dataStyle);
							    
							    cell = row.createCell( k++);
							    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTOMERNAME"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(UnitPriceValue);
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("OBDISCOUNT"))));
								cell.setCellStyle(dataStyle);
								
							if(discountType.equalsIgnoreCase("BYPERCENTAGE")){ 
								cell = row.createCell( k++);
								cell.setCellValue(discountedPerValue);
								cell.setCellStyle(dataStyle);
					               }else{ 
					            	   cell = row.createCell( k++);
										cell.setCellValue(discount1Value);
										cell.setCellStyle(dataStyle);
					              
					               }
								
								
								
									
								 index++;
								 if((index-1)%maxRowsPerSheet==0){
									 index = 1;
									 SheetNo++;
									 sheet = wb.createSheet("Sheet_"+SheetNo);
									 styleHeader = createStyleHeader(wb);
									 sheet = this.createWidth(sheet);
									 sheet = this.createHeader(sheet,styleHeader,PLANT);										 
								 }
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

private HSSFSheet createContactsDiscountWidth(HSSFSheet sheet){
	
	try{
		sheet.setColumnWidth(0 ,5500);
		sheet.setColumnWidth(1 ,8000);		
	    sheet.setColumnWidth(2 ,8000);
		sheet.setColumnWidth(3 ,8000);
		sheet.setColumnWidth(4 ,8000);
		sheet.setColumnWidth(5 ,3500);
		sheet.setColumnWidth(6 ,5500);
		sheet.setColumnWidth(7 ,7000);
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFSheet createContactsDiscountHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
	int k = 0;
	try{		
		HSSFRow rowhead = sheet.createRow( 0);
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Customer ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Customer Type Description"));
		cell.setCellStyle(styleHeader);
	
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Customer Name"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Product ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Product Description"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Price"));
		cell.setCellStyle(styleHeader);	
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Discount"));
		cell.setCellStyle(styleHeader);	
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Price With Discount"));
		cell.setCellStyle(styleHeader);
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFWorkbook writeToSupplierReportExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String PLANT = "";
	int maxRowsPerSheet = 65535;

	try{
		List listQry = new ArrayList();
		StrUtils strUtils = new StrUtils();
		HttpSession session = request.getSession();
		
		int SheetNo =1;	
		//
		 PLANT= session.getAttribute("PLANT").toString();
		 String USERID= session.getAttribute("LOGIN_USER").toString();
		 String LOC     = strUtils.fString(request.getParameter("LOC"));
		 String ITEM    = strUtils.fString(request.getParameter("ITEM"));
		 String CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
		 String sSupplierTypeId  = strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));

		 String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
		
		//
		 String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
		 String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
		 
         PlantMstDAO plantMstDAO = new PlantMstDAO();
         String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
		
		 Hashtable ht = new Hashtable();
	      ht.put("C.PLANT",PLANT);
	      if(strUtils.fString(PLANT).length() > 0)        
	      if(strUtils.fString(ITEM).length() > 0)      
	      {
	        ht.put("I.ITEM",ITEM);
	      } 
	     
	      
	      if(strUtils.fString(CUSTOMER).length() > 0)      
	      {
	        ht.put("V.VNAME",new StrUtils().InsertQuotes(CUSTOMER));
	      }
	      
	      if(strUtils.fString(sSupplierTypeId).length() > 0)      
	      {
	        ht.put("V.VEND_TYPE_ID",sSupplierTypeId);
	      }
	      VendMstDAO vendmstdao = new VendMstDAO();
	      ArrayList invQryList  = new ArrayList();
	      listQry = vendmstdao.getSupplierReportforExcel(ht,PRD_DESCRIP);
	      
					
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					dataStyle = createDataStyle(wb);
					 sheet = wb.createSheet("Sheet"+SheetNo);
					 styleHeader = createStyleHeader(wb);
					 sheet = this.createSupplierReportWidth(sheet);
					 sheet = this.createSupplierReportHeader(sheet,styleHeader);
					 int index = 1;
						
					  
					 if (listQry.size() > 0) {
					 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
						
							   Map lineArr = (Map) listQry.get(iCnt);	
							    int k = 0;
							    String discount=(String)lineArr.get("DISCOUNT");
				                double cost =Double.parseDouble((String)lineArr.get("COST"));
				                double discountedcost=0.00d;
				                	String   discountType="";
				                double converteddiscount=0.00d;
				                double discount1=0.00d;
				                double discountedper=0.00d;
				                int plusIndex = discount.indexOf("%");
				                if (plusIndex != -1) {
				                	discount = discount.substring(0, plusIndex);
				                	converteddiscount=Double.parseDouble(discount);
				                	discountType = "BYPERCENTAGE";
				                	discountedcost=(converteddiscount/100)*cost;
				                	discountedper=cost-discountedcost;
				                   }
				                else{
				                	 discount1 =Double.parseDouble(discount);
				                }
				                
				                String discountedPerValue = String.valueOf(discountedper);
				                String discount1Value = String.valueOf(discount1);
				                
				                /*float discountedPerVal ="".equals(discountedPerValue) ? 0.0f :  Float.parseFloat(discountedPerValue);
				                float discount1Val ="".equals(discount1Value) ? 0.0f :  Float.parseFloat(discount1Value);					    		
					    		if(discountedPerVal==0f){
					    			discountedPerValue="0.000";
					    		}else{
					    			discountedPerValue=discountedPerValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					    		}if(discount1Val==0f){
					    			discount1Value="0.000";
					    		}else{
					    			discount1Value=discount1Value.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					    		}*/
				                
				                double discountedPerVal ="".equals(discountedPerValue) ? 0.0d :  Double.parseDouble(discountedPerValue);
				                double discount1Val ="".equals(discount1Value) ? 0.0d :  Double.parseDouble(discount1Value);
				                
				                discountedPerValue = StrUtils.addZeroes(discountedPerVal, numberOfDecimal);
				                discount1Value = StrUtils.addZeroes(discount1Val, numberOfDecimal);
				                
				                
				                
							    HSSFRow row = sheet.createRow(index);
							  
							    HSSFCell cell = row.createCell( k++);
							    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESCRIPTION"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SUPPLIERID"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SUPPLIERNAME"))));
								cell.setCellStyle(dataStyle);
								
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString(StrUtils.addZeroes(cost, numberOfDecimal))));
								cell.setCellStyle(dataStyle);								
																
								cell = row.createCell( k++);
								cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DISCOUNT"))));
								cell.setCellStyle(dataStyle);
								
								if(discountType.equalsIgnoreCase("BYPERCENTAGE")){
									cell = row.createCell( k++);
									cell.setCellValue(discountedPerValue);
									cell.setCellStyle(dataStyle);
									}else{
										cell = row.createCell( k++);
										cell.setCellValue(discount1Value);
										cell.setCellStyle(dataStyle);
									}
								
								 index++;
								 if((index-1)%maxRowsPerSheet==0){
									 index = 1;
									 SheetNo++;
									 sheet = wb.createSheet("Sheet_"+SheetNo);
									 styleHeader = createStyleHeader(wb);
									 sheet = this.createWidth(sheet);
									 sheet = this.createHeader(sheet,styleHeader,PLANT);
									 
								 }
								

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

private HSSFSheet createSupplierReportWidth(HSSFSheet sheet){
	
	try{
		sheet.setColumnWidth(0 ,5500);
		sheet.setColumnWidth(1 ,8000);
		sheet.setColumnWidth(2 ,3500);
			sheet.setColumnWidth(3 ,3500);
		sheet.setColumnWidth(4 ,6000);
			sheet.setColumnWidth(5 ,3500);
			sheet.setColumnWidth(6 ,6000);	
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

 private HSSFSheet createSupplierReportHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow( 0);
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Product ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Product Description"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Supplier ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Supplier Name"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Cost"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Discount"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Cost With Discount"));
		cell.setCellStyle(styleHeader);
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

 private HSSFWorkbook writeToExcelSalesReturn(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	 StrUtils strUtils = new StrUtils();
	 HTReportUtil movHisUtil       = new HTReportUtil();
	 DateUtils _dateUtils = new DateUtils();
	 ArrayList movQryList  = new ArrayList();
	 ArrayList movItemQryList  = new ArrayList();
	 DateUtils dateUtils = new DateUtils();
	 int maxRowsPerSheet = 65535;
	 String fdate="",tdate="",chkExpirydate="";
	 int SheetId =1;
	try{
				String PLANT 		= StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	            String FROM_DATE   	 = strUtils.fString(request.getParameter("FROM_DATE")).trim();
	            String TO_DATE 		 = strUtils.fString(request.getParameter("TO_DATE")).trim();
				String ITEMNO           = strUtils.fString(request.getParameter("ITEM"));
				String sItemDesc        = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("DESC")));
				String ORDERNO          = strUtils.fString(request.getParameter("ORDERNO"));
				String reportType ="";
				
	        	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	            String curDate =_dateUtils.getDate();
	            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
	            if (FROM_DATE.length() > 5)
	                fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
	        	if (TO_DATE == null)
	        		TO_DATE = "";
	        	else
	        		TO_DATE = TO_DATE.trim();
	        	if (TO_DATE.length() > 5)
	                tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	        	
	        	 Hashtable ht = new Hashtable();
	        	// if(strUtils.fString(DIRTYPE).length() > 0)      ht.put("DIRTYPE",DIRTYPE);
	             if(strUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);
	             if(strUtils.fString(ORDERNO).length() > 0)         ht.put("ORDNUM",ORDERNO);
	             movQryList = movHisUtil.getSalesReturn(ht,PLANT,fdate,tdate,sItemDesc);
	       
	             Boolean workSheetCreated = true;
				if (movQryList.size() > 0) {
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							HSSFCellStyle CompHeader = null;
							
							
							CreationHelper createHelper = wb.getCreationHelper();
							dataStyle = createDataStyle(wb);
							
							HSSFCellStyle dataStyleRightAlign = null;
							dataStyleRightAlign  = createDataStyleRightAlign(wb);
							
							HSSFCellStyle dataStyleSpecial = null;
							dataStyleSpecial = createDataStyle(wb);
							
							HSSFCellStyle dataStyleRightForeColorRed = null;
							dataStyleRightForeColorRed  = createDataStyleForeColorRed(wb);
							
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthSalesReturn(sheet);
							sheet = this.createHeaderSalesReturn(sheet,styleHeader);
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
							 int index = 2;
					         String strDiffQty="",deliverydateandtime="";
					         DecimalFormat decformat = new DecimalFormat("#,##0.00");
					    								         
					         for (int iCnt =0; iCnt<movQryList.size(); iCnt++){	
								   Map lineArr = (Map) movQryList.get(iCnt);	
								   int k = 0;
								   
								   String trDate= "";
				                   trDate=(String)lineArr.get("CRAT");
				                   if (trDate.length()>8) {
				                       trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
				                    }
				                 																	  
								    HSSFRow row = sheet.createRow(index);
								    HSSFCell cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(Integer.toString(iCnt+1)));
								    cell.setCellStyle(dataStyle);
							
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ORDNUM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("CUSTOMER"))));
									cell.setCellStyle(dataStyle);
									
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
									cell.setCellStyle(dataStyle);
									
									
							          cell = row.createCell( k++);
							          cell.setCellValue(new HSSFRichTextString(strUtils.formatNum(StrUtils.fString((String)lineArr.get("QTYOR")))));
									  cell.setCellStyle(dataStyle);
									  
									  cell = row.createCell( k++);
							          cell.setCellValue(new HSSFRichTextString(strUtils.formatNum(StrUtils.fString((String)lineArr.get("QTYIS")))));
									  cell.setCellStyle(dataStyle);
									  
									  cell = row.createCell( k++);
							          cell.setCellValue(new HSSFRichTextString(strUtils.formatNum(StrUtils.fString((String)lineArr.get("QTYRETURN")))));
									  cell.setCellStyle(dataStyle);
											
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARKS"))));
									cell.setCellStyle(dataStyle);
															
									index++;
									 if((index-2)%maxRowsPerSheet==0){
										 index = 2;
										 SheetId++;
										 sheet = wb.createSheet("Sheet"+SheetId);
										 styleHeader = createStyleHeader(wb);
										 CompHeader = createCompStyleHeader(wb);
										 sheet = this.createWidthSalesReturn(sheet);
										 sheet = this.createHeaderSalesReturn(sheet,styleHeader);
										 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
									 }
						 
						 }  // end of for loop
							
				 }
				 else if (movQryList.size() < 1) {		
					System.out.println("No Records Found To List");
				}
   
	}catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	
	return wb;
}

private HSSFSheet createWidthSalesReturn(HSSFSheet sheet){
	int i = 0;
	try{
	
		sheet.setColumnWidth(i++ ,1000);
		sheet.setColumnWidth(i++ ,3500);
		sheet.setColumnWidth(i++ ,3500);
		sheet.setColumnWidth(i++ ,6000);
		sheet.setColumnWidth(i++ ,4500);
		sheet.setColumnWidth(i++ ,4200);
		sheet.setColumnWidth(i++ ,4200);
		sheet.setColumnWidth(i++ ,4200);
		sheet.setColumnWidth(i++ ,5200);
		sheet.setColumnWidth(i++ ,6200);
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

private HSSFSheet createHeaderSalesReturn(HSSFSheet sheet, HSSFCellStyle styleHeader){
	int k = 0;
	try{
	
	HSSFRow rowhead = sheet.createRow(1);
	
	HSSFCell cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("S/N"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Outbound No"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Customer"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Location"));
	cell.setCellStyle(styleHeader);
				
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Product Id"));
	cell.setCellStyle(styleHeader);
			
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Description"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Order Quantity"));
	cell.setCellStyle(styleHeader);
	
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Shipping Quantity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Return Quantity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell( k++);
	cell.setCellValue(new HSSFRichTextString("Remarks"));
	cell.setCellStyle(styleHeader);
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
private HSSFWorkbook writeToExcelAltBrandReports(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
	String plant = "",fdate="",tdate="";
    int maxRowsPerSheet = 65535;
	
	int SheetId =1;
	DateUtils _dateUtils = new DateUtils();
	StrUtils strUtils =  new StrUtils();
	ArrayList listQry = new ArrayList();
	InvUtil invUtil =  new InvUtil();
	try{
		//	HttpSession session = request.getSession();
		 	String UOM = StrUtils.fString(request.getParameter("UOM"));
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String LOC = strUtils.fString(request.getParameter("LOC"));
			String ITEM = strUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));				
			String LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
			String LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			String VINNO = strUtils.fString(request.getParameter("VINNO"));
			String MODEL = strUtils.fString(request.getParameter("MODEL"));
			String VIEWALTERNATE = strUtils.fString(request.getParameter("VIEWALTERNATE"));
			if(VIEWALTERNATE.contentEquals("WithInventory"))
			{
				VIEWALTERNATE="1";
			}
			else
			{
				VIEWALTERNATE="0";
			}
			String CNAME = strUtils.fString(request.getParameter("CUSTOMER"));
			String reportType ="";
			Hashtable ht = new Hashtable();
			if (strUtils.fString(ITEM).length() > 0)
			{
				//Check Item in ALTERNATEBRAND
				ArrayList itemList = new InvUtil().getItemListFromAlterItem(PLANT,ITEM);
				if (itemList.size() > 0) {
					Map lineArrIm = (Map) itemList.get(0);
					ITEM=strUtils.fString((String) lineArrIm.get("ITEM"));						
				}
				ht.put("A.ITEM", ITEM);
			}
			int itemcount=0;
			String NewItem="";
			if(VIEWALTERNATE.equalsIgnoreCase("1"))
			{
				ArrayList itemList = new InvUtil().getItemListUom(PLANT,ht,PRD_DESCRIP,CNAME,UOM);
			if (itemList.size() > 0) {
			HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle CompHeader = null;
					HSSFCellStyle dataStyle = null;
					 sheet = wb.createSheet("Sheet"+SheetId);
					 styleHeader = createStyleHeader(wb);
					 CompHeader = createCompStyleHeader(wb);
					 sheet = this.createWidthAltBrandReports(sheet,reportType);
					 sheet = this.createHeaderAltBrandReports(sheet,styleHeader,reportType);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
					 
					 int index = 2;		
			for (int iCnt1 = 0; iCnt1 < itemList.size(); iCnt1++) {
				Map lineArrIm = (Map) itemList.get(iCnt1);
				ITEM=strUtils.fString((String) lineArrIm.get("ITEM"));
				if(NewItem!=ITEM)
				{
					NewItem=ITEM;
				Hashtable htft = new Hashtable();
				if (strUtils.fString(ITEM).length() > 0)
					htft.put("A.ITEM", ITEM);
				ArrayList ftritemList = new InvUtil().getItemList(PLANT,htft,PRD_DESCRIP,CNAME);
				if(itemcount==0)
					itemcount=ftritemList.size();
				}
				int k = 0;
				dataStyle = createDataStyle(wb);				
				
        if (strUtils.fString(ITEM).length() > 0)	                    
			ht.remove("A.ITEM");
        
        HSSFRow row = sheet.createRow(index);
    	
        VINNO = strUtils.fString(request.getParameter("VINNO"));
		MODEL = strUtils.fString(request.getParameter("MODEL"));
		String VINNO1 = strUtils.fString(request.getParameter("VINNO"));
		String MODEL1 = strUtils.fString(request.getParameter("MODEL"));
		Double prval1=Double.parseDouble((String) lineArrIm.get("UNITPRICE"));
		 String OBDiscount=strUtils.fString((String) lineArrIm.get("DISCOUNTBY"));
	     int plusIndex = OBDiscount.indexOf("%");
	     if (plusIndex != -1) {
	    	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	    	 prval1=prval1-((prval1*Double.valueOf(OBDiscount))/100);
	         }
	     else
	     {
	     	if(OBDiscount!="")
	     	{
	     		prval1=Double.valueOf(OBDiscount);
	     	}
	     }
	     PlantMstDAO _PlantMstDAO = new PlantMstDAO();
         String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			BigDecimal bd1 = new BigDecimal(prval1);
			DecimalFormat format1 = new DecimalFormat("#.#####");		
			 format1.setRoundingMode(RoundingMode.FLOOR);
			String priceval1 = format1.format(bd1);
			priceval1 = strUtils.addZeroes(Double.parseDouble(priceval1), numberOfDecimal);
    	if(VINNO=="" && MODEL=="")
		{
    		HSSFCell cell = row.createCell(k++);
        	cell.setCellValue(new HSSFRichTextString(ITEM));
        	cell.setCellStyle(dataStyle);
        	
    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("ITEMDESC"))));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("BRAND"))));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("VINNO"))));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("MODEL"))));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("LOC"))));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString(priceval1)));
    		cell.setCellStyle(dataStyle);

    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("STKUOM"))));
    		cell.setCellStyle(dataStyle);
    		
    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("STKQTY"))));
    		cell.setCellStyle(dataStyle);
    		
    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("INVENTORYUOM"))));
    		cell.setCellStyle(dataStyle);
    		
    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("INVUOMQTY"))));
    		cell.setCellStyle(dataStyle);
    		
    		cell = row.createCell( k++);
    		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("QTY"))));
    		cell.setCellStyle(dataStyle);
    		
    		index++;
		}
    	
    	if(VINNO!="")
		{
        if (VINNO.contentEquals(strUtils.fString((String) lineArrIm.get("VINNO"))))
        {
        	VINNO="";
        	
        	if(MODEL!="")
			{
        	if (MODEL.contentEquals(strUtils.fString((String) lineArrIm.get("MODEL"))))
            	MODEL="";
			}
        	if(MODEL!="")
        		MODEL="";
        }
		}
		if(MODEL!="")
		{
        if (MODEL.contentEquals(strUtils.fString((String) lineArrIm.get("MODEL"))))
        	MODEL="";
		}
        ht.put("A.ITEM", ITEM);
        itemcount=itemcount-1;
ArrayList invQryList = new InvUtil().getInvListSummaryAlternateBrand(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
		LOC_TYPE_ID,VINNO,MODEL,CNAME,LOC_TYPE_ID2);
if (invQryList.size() > 0) {
	
	prval1=Double.parseDouble((String) lineArrIm.get("UNITPRICE"));
	  OBDiscount=strUtils.fString((String) lineArrIm.get("DISCOUNTBY"));
      plusIndex = OBDiscount.indexOf("%");
     if (plusIndex != -1) {
    	 	OBDiscount = OBDiscount.substring(0, plusIndex);
    	 prval1=prval1-((prval1*Double.valueOf(OBDiscount))/100);
         }
     else
     {
     	if(OBDiscount!="")
     	{
     		prval1=Double.valueOf(OBDiscount);
     	}
     }
		 bd1 = new BigDecimal(prval1);
		 format1 = new DecimalFormat("#.#####");		
		 format1.setRoundingMode(RoundingMode.FLOOR);
		 priceval1 = format1.format(bd1);
	if(VINNO1!="" && MODEL1!="")
	{
		HSSFCell cell = row.createCell(k++);
    	cell.setCellValue(new HSSFRichTextString(ITEM));
    	cell.setCellStyle(dataStyle);
    	
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("ITEMDESC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("BRAND"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("VINNO"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("MODEL"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("LOC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString(priceval1)));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("QTY"))));
		cell.setCellStyle(dataStyle);
		
		index++;
	}
	else if(VINNO1!="" && MODEL1=="")
	{
		HSSFCell cell = row.createCell(k++);
    	cell.setCellValue(new HSSFRichTextString(ITEM));
    	cell.setCellStyle(dataStyle);
    	
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("ITEMDESC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("BRAND"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("VINNO"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("MODEL"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("LOC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString(priceval1)));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("QTY"))));
		cell.setCellStyle(dataStyle);
		
		index++;
	}
	else if(VINNO1=="" && MODEL1!="")
	{
		HSSFCell cell = row.createCell(k++);
    	cell.setCellValue(new HSSFRichTextString(ITEM));
    	cell.setCellStyle(dataStyle);
    	
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("ITEMDESC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("BRAND"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("VINNO"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("MODEL"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("LOC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString(priceval1)));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArrIm.get("QTY"))));
		cell.setCellStyle(dataStyle);
		
		index++;
	}
if((index-2)%maxRowsPerSheet==0){
 index = 2;
 SheetId++;
 sheet = wb.createSheet("Sheet"+SheetId);
 styleHeader = createStyleHeader(wb);
 CompHeader = createCompStyleHeader(wb);
 sheet = this.createWidthAltBrandReports(sheet,reportType);
 sheet = this.createHeaderAltBrandReports(sheet,styleHeader,reportType);			 
 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
}
	
	int iIndex = 0;
	int irow = 0;
	double sumprdQty = 0;
	String lastProduct = "";
	if(itemcount==0)
    {
	for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
		String result = "";
		Map lineArr = (Map) invQryList.get(iCnt);
		sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
		String item = (String) lineArr.get("ALTERNATEITEM");
		String loc = (String) lineArr.get("LOC");
		String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
		 k = 0;		 
		 Double prval=Double.parseDouble((String) lineArr.get("UNITPRICE"));
		 OBDiscount=strUtils.fString((String) lineArr.get("DISCOUNTBY"));
	     plusIndex = OBDiscount.indexOf("%");
	     if (plusIndex != -1) {
	    	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	    	 prval=prval-((prval*Double.valueOf(OBDiscount))/100);
	         }
	     else
	     {
	     	if(OBDiscount!="")
	     	{
	     		prval=Double.valueOf(OBDiscount);
	     	}
	     }
			BigDecimal bd = new BigDecimal(prval);
			DecimalFormat format = new DecimalFormat("#.#####");		
			 format.setRoundingMode(RoundingMode.FLOOR);
			String priceval = format.format(bd);
			
		row = sheet.createRow(index);
		HSSFCell cell = row.createCell(k++);
		cell.setCellValue(new HSSFRichTextString(item));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ALTERNATEITEMDESC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ALTERNATEBRAND"))));
		cell.setCellStyle(dataStyle);

		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VINNO"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("MODEL"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(priceval));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(""));
		cell.setCellStyle(dataStyle);
		
		cell = row.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("QTY"))));
		cell.setCellStyle(dataStyle);
		index++;
		 if((index-1)%maxRowsPerSheet==0){
			 index = 1;
			 SheetId++;
			 sheet = wb.createSheet("Sheet"+SheetId);
			 styleHeader = createStyleHeader(wb);
			 CompHeader = createCompStyleHeader(wb);
			 sheet = this.createWidthAltBrandReports(sheet,reportType);			 
			 sheet = this.createHeaderAltBrandReports(sheet,CompHeader,reportType);
		 }
		 itemcount=0;
		NewItem="";
	}
    }
	}
	
	}
	}
	else if (itemList.size() < 1) {		
				

					System.out.println("No Records Found To List");
				}
			}
			else
			{
				ArrayList invQryList = new InvUtil().getInvListSummaryDetail(PLANT, ITEM, PRD_DESCRIP, ht, LOC,
						LOC_TYPE_ID,VINNO,MODEL,CNAME);
				if (invQryList.size() > 0) {
					int iIndex = 0;
					int irow = 0;
					double sumprdQty = 0;
					String lastProduct = "";
					HSSFSheet sheet = null ;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle CompHeader = null;
					HSSFCellStyle dataStyle = null;
					 sheet = wb.createSheet("Sheet"+SheetId);
					 styleHeader = createStyleHeader(wb);
					 CompHeader = createCompStyleHeader(wb);
					 sheet = this.createWidthAltBrandReports(sheet,reportType);
					 sheet = this.createHeaderAltBrandReports(sheet,styleHeader,reportType);
					 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
					 
					 int index = 2;
					for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
						String result = "";
						Map lineArr = (Map) invQryList.get(iCnt);
						sumprdQty = sumprdQty + Double.parseDouble((String) lineArr.get("QTY"));
						String item = (String) lineArr.get("ITEM");
						String loc = (String) lineArr.get("LOC");
						String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
						int k = 0;
						dataStyle = createDataStyle(wb);
								 
						 Double prval=Double.parseDouble((String) lineArr.get("UNITPRICE"));
						 String OBDiscount=strUtils.fString((String) lineArr.get("DISCOUNTBY"));
					     int plusIndex = OBDiscount.indexOf("%");
					     if (plusIndex != -1) {
					    	 	OBDiscount = OBDiscount.substring(0, plusIndex);
					    	 prval=prval-((prval*Double.valueOf(OBDiscount))/100);
					         }
					     else
					     {
					     	if(OBDiscount!="")
					     	{
					     		prval=Double.valueOf(OBDiscount);
					     	}
					     }
							BigDecimal bd = new BigDecimal(prval);
							DecimalFormat format = new DecimalFormat("#.#####");		
							 format.setRoundingMode(RoundingMode.FLOOR);
							String priceval = format.format(bd);
							
						HSSFRow row = sheet.createRow(index);
						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(item));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("BRAND"))));
						cell.setCellStyle(dataStyle);

						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VINNO"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("MODEL"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("LOC"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(priceval));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell( k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("QTY"))));
						cell.setCellStyle(dataStyle);
						index++;
						 if((index-1)%maxRowsPerSheet==0){
							 index = 1;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							 styleHeader = createStyleHeader(wb);
							 CompHeader = createCompStyleHeader(wb);
							 sheet = this.createWidthAltBrandReports(sheet,reportType);			 
							 sheet = this.createHeaderAltBrandReports(sheet,CompHeader,reportType);
						 }
					}
					}
				else if (invQryList.size() < 1) {		
					

					System.out.println("No Records Found To List");
				}
			}
	}catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	
	return wb;
}
private HSSFSheet createHeaderAltBrandReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType){
	int k = 0;
	try{
		
	HSSFRow rowhead = sheet.createRow(1);
	HSSFCell cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Product ID"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Description"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Brand"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("VIN No"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Model"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Location"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Unit Price"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Stock UOM"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Stock Quantity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Inventory UOM"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Inventory Quantity"));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString("Quantity"));
	cell.setCellStyle(styleHeader);
											
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
private HSSFSheet createWidthAltBrandReports(HSSFSheet sheet,String reportType){
	
	try{
		sheet.setColumnWidth(0 ,5000);
		sheet.setColumnWidth(1 ,4000);
		sheet.setColumnWidth(2 ,5000);
		sheet.setColumnWidth(3 ,5000);
		sheet.setColumnWidth(4 ,4000);
		sheet.setColumnWidth(5 ,8000);
		sheet.setColumnWidth(6 ,3000);		
		
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
	int k = 0;
	try{
		String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",
				CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="" ;
		PlantMstUtil pmUtil = new PlantMstUtil();
		ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
		for (int i = 0; i < listQry.size(); i++) {
			Map map = (Map) listQry.get(i);
			CNAME = (String) map.get("PLNTDESC");
			/*CADD1 = (String) map.get("ADD1");
			CADD2 = (String) map.get("ADD2");
			COL1=CADD1+" "+CADD2;
			CADD3 = (String) map.get("ADD3");
			CADD4 = (String) map.get("ADD4");
			if((CADD3+CADD4).length()>1)
				COL1=COL1+", "+(CADD3+" "+CADD4);*/
			
			CCOUNTRY = (String) map.get("COUNTY");
			CSTATE = (String) map.get("STATE");
			CZIP = (String) map.get("ZIP");
			if((CSTATE).length()>1)
				CNAME=CNAME+", "+CSTATE;
			
			if((CCOUNTRY).length()>1)
				CNAME=CNAME+", "+CCOUNTRY;
			
			if((CZIP).length()>1)
				CNAME=CNAME+"-"+CZIP;
				
				
		}
		
	HSSFRow rowhead = sheet.createRow(0);
	HSSFCell cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(CNAME));	
	cell.setCellStyle(styleHeader);
	CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 4);
	sheet.addMergedRegion(cellRangeAddress);
	
	/*cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(COL1));
	cell.setCellStyle(styleHeader);
	
	cell = rowhead.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(COL2));
	cell.setCellStyle(styleHeader);
	*/
	
											
	}
	catch(Exception e){
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}
private HSSFCellStyle createCompStyleHeader(HSSFWorkbook wb){
	
	//Create style
	 HSSFCellStyle styleHeader = wb.createCellStyle();
	  HSSFFont fontHeader  = wb.createFont();
	  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	  fontHeader.setFontName("Arial");	
	  styleHeader.setFont(fontHeader);
	  styleHeader.setFillForegroundColor(HSSFColor.WHITE.index);
	  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	  styleHeader.setWrapText(true);
	  return styleHeader;
}
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {
			
		}

	}
	
	
	private HSSFWorkbook writeToEmployeeLeaveDetailsExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		int maxRowsPerSheet = 65535;
	
		try{
			List listQry = new ArrayList();
			StrUtils strUtils = new StrUtils();
			EmployeeDAO employeeDAO = new EmployeeDAO();
	        HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
	        EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			int SheetNo =1;	
			 String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
			 String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
			 
			 List<EmployeeLeaveDET> empleavedet = employeeLeaveDetDAO.getAllEmployeeLeavedet(plant);
			
			
				
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						dataStyle = createDataStyle(wb);
						 sheet = wb.createSheet("Sheet"+SheetNo);
						 styleHeader = createStyleHeader(wb);
						 sheet = this.createEmployeeLeaveDetailsWidth(sheet,reportType);
						 sheet = this.createEmployeeLeaveDetHeader(sheet,styleHeader,reportType);
						 int index = 1;
							String customerType="";
						    String customerStatus="";
						    
						 if (!empleavedet.isEmpty()) {
						 for (EmployeeLeaveDET employeeLeaveDET:empleavedet){

								    int k = 0;
								    
								    HSSFRow row = sheet.createRow(index);
								  
								    String empcode = employeeDAO.getEmpcode(plant, String.valueOf(employeeLeaveDET.getEMPNOID()), "");
								    HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, employeeLeaveDET.getLEAVETYPEID());
									
								    HSSFCell cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(empcode));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString(hrLeaveType.getLEAVETYPE())));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString(String.valueOf(employeeLeaveDET.getTOTALENTITLEMENT()))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(String.valueOf(employeeLeaveDET.getLEAVEYEAR()))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
									cell.setCellValue(new HSSFRichTextString(StrUtils.fString(String.valueOf(employeeLeaveDET.getNOTE()))));
									cell.setCellStyle(dataStyle);
							
								
									 index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetNo++;
										 sheet = wb.createSheet("Sheet_"+SheetNo);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidth(sheet);
										 sheet = this.createHeader(sheet,styleHeader,plant);
										 
									 }
									

							  }
						
				 } else{		
					

						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
	
	private HSSFSheet createEmployeeLeaveDetHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String ReportType){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow( 0);	
		
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(ReportType+" ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Leave Type"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Total Entitlement"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Leave Year"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Notes"));
		cell.setCellStyle(styleHeader);
		
		}
		
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createEmployeeLeaveDetailsWidth(HSSFSheet sheet,String ReportType){
		
		try{
			   sheet.setColumnWidth(0 ,5500);
			   sheet.setColumnWidth(1 ,5500);
			   sheet.setColumnWidth(2 ,3500);
		    	sheet.setColumnWidth(3 ,3500);
				sheet.setColumnWidth(4 ,8000);
				
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFWorkbook writeToEmployeeSalaryDetailsExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
		String plant = "";
		int maxRowsPerSheet = 65535;
	
		try{
			List listQry = new ArrayList();
			StrUtils strUtils = new StrUtils();
			EmployeeDAO employeeDAO = new EmployeeDAO();
	        HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			int SheetNo =1;	
			 String reportType = strUtils.fString(request.getParameter("ReportType")).trim();
			 String CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE")).trim();
			 String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
			 
			 List<HrEmpSalaryDET> hrEmpSalaryDET = hrEmpSalaryDetDAO.getAllEmpSalarydet(plant);
			
			
				
						HSSFSheet sheet = null ;
						HSSFCellStyle styleHeader = null;
						HSSFCellStyle dataStyle = null;
						dataStyle = createDataStyle(wb);
						 sheet = wb.createSheet("Sheet"+SheetNo);
						 styleHeader = createStyleHeader(wb);
						 sheet = this.createEmployeeSalaryDetailsWidth(sheet,reportType);
						 sheet = this.createEmployeeSalaryDetHeader(sheet,styleHeader,reportType);
						 int index = 1;
							String customerType="";
						    String customerStatus="";
						    
						 if (!hrEmpSalaryDET.isEmpty()) {
						 for (HrEmpSalaryDET empSalaryDET:hrEmpSalaryDET){

								    int k = 0;
								    
								    HSSFRow row = sheet.createRow(index);
								  
								    String empcode = employeeDAO.getEmpcode(plant, String.valueOf(empSalaryDET.getEMPNOID()), "");
								    
								    String sSalary=StrUtils.addZeroes(empSalaryDET.getSALARY(), numberOfDecimal);

									
								    HSSFCell cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(empcode));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(StrUtils.fString(String.valueOf(empSalaryDET.getSALARYTYPE()))));
									cell.setCellStyle(dataStyle);
									
									cell = row.createCell( k++);
								    cell.setCellValue(new HSSFRichTextString(sSalary));
									cell.setCellStyle(dataStyle);
							
								
									 index++;
									 if((index-1)%maxRowsPerSheet==0){
										 index = 1;
										 SheetNo++;
										 sheet = wb.createSheet("Sheet_"+SheetNo);
										 styleHeader = createStyleHeader(wb);
										 sheet = this.createWidth(sheet);
										 sheet = this.createHeader(sheet,styleHeader,plant);
										 
									 }
									

							  }
						
				 } else{		
					

						System.out.println("No Records Found To List");
					}
		}catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return wb;
	}
	
	private HSSFSheet createEmployeeSalaryDetHeader(HSSFSheet sheet, HSSFCellStyle styleHeader,String ReportType){
		int k = 0;
		try{
		
		
		HSSFRow rowhead = sheet.createRow( 0);	
		
		HSSFCell cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString(ReportType+" ID"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Salary Type"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell( k++);
		cell.setCellValue(new HSSFRichTextString("Salary"));
		cell.setCellStyle(styleHeader);
		}
		
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createEmployeeSalaryDetailsWidth(HSSFSheet sheet,String ReportType){
		
		try{
			   sheet.setColumnWidth(0 ,5500);
			   sheet.setColumnWidth(1 ,5500);
			   sheet.setColumnWidth(2 ,3500);
				
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFWorkbook writeToExcelInvCostPrice(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
//        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		BillUtil billUtil = new BillUtil();
		int maxRowsPerSheet = 65535;
		int SheetId =1;
		try {
			 String PLANT= StrUtils.fString(request.getParameter("PLANT"));
             String USERID= StrUtils.fString(request.getParameter("LOGIN_USER"));
             String LOC     = StrUtils.fString(request.getParameter("LOC"));
             String ITEM    = StrUtils.fString(request.getParameter("ITEM"));
             String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
             String  PRD_TYPE_ID     = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
             String  PRD_DEPT_ID     = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
             String  PRD_BRAND_ID     = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
             String  PRD_DESCRIP     = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
             String  CURRENCYID      = StrUtils.fString(request.getParameter("CURRENCYID"));
             String  CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
             String baseCurrency = StrUtils.fString(
     				(String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
                                    
              if(CURRENCYID.equals(""))
               {
                        CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                        List listQry = _CurrencyDAO.getCurrencyList(PLANT,CURRENCYDISPLAY);
                        for(int i =0; i<listQry.size(); i++) {
                            Map m=(Map)listQry.get(i);
                            CURRENCYID=(String)m.get("currencyid");
                        }
               }        
              Hashtable ht = new Hashtable();
              
              movQryList= new InvUtil().getInvListSummaryWithcostpriceinventory(ht,PLANT,ITEM,PRD_DESCRIP,CURRENCYID,baseCurrency,LOC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID);
              String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	          
	           
//	           Boolean workSheetCreated = true;
	           if (movQryList.size() >= 0) {
	        	    HSSFSheet sheet = null;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
//					HSSFCellStyle dataStyleSpecial = null;
					HSSFCellStyle CompHeader = null;
					dataStyle = createDataStyle(wb);
//					dataStyleSpecial = createDataStyle(wb);
					sheet = wb.createSheet("Sheet"+SheetId);
					styleHeader = createStyleHeader(wb);
					CompHeader = createCompStyleHeader(wb);
					sheet = this.createWidthInvCostPrice(sheet);
					sheet = this.createHeaderinvcostpriceSummary(sheet, styleHeader);
					sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
//					CreationHelper createHelper = wb.getCreationHelper();

					int index = 3;
					
//					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, CostwTax = 0;
//					float gstpercentage = 0;
//					String strDiffQty = "", deliverydateandtime = "";
//					DecimalFormat decformat = new DecimalFormat("#,##0.00");
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String purchasecost = (String)lineArr.get("unitCost");
               			String salesprice = (String)lineArr.get("unitPrice");
                        float unitCostVal ="".equals(purchasecost) ? 0.0f :  Float.parseFloat(purchasecost);
                        if(unitCostVal==0f){
                        	purchasecost="0.00000";
                        }else{
                        	purchasecost=purchasecost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        purchasecost = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                        
                        float balCostVal ="".equals(salesprice) ? 0.0f :  Float.parseFloat(salesprice);
                        if(balCostVal==0f){
                        	salesprice="0.00000";
                        }else{
                        	salesprice=salesprice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        salesprice = StrUtils.addZeroes(balCostVal, numberOfDecimal);
                        
               
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("department"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("category"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("subCategory"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("brand"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemDesc"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("purchaseUom"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(purchasecost)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("salesUom"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(salesprice)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("inventoryUom"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(Double.parseDouble(String.valueOf(lineArr.get("stockQty"))), "3")));
						cell.setCellStyle(dataStyle);
						

						
						index++;
						/* if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "vendorBalances");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "vendorBalances");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}*/
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	

	private HSSFSheet createHeaderinvcostpriceSummary(HSSFSheet sheet, HSSFCellStyle styleHeader) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(2);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("LOCATION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BRAND"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PURCHASE UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PURCHASE COST"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES PRICE"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("INVENTORY QTY"));
			cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createWidthInvCostPrice(HSSFSheet sheet) {
		int i = 0;
		try {
					sheet.setColumnWidth(i++, 1000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 8000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
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