package com.track.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.BorderFactory;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.itextpdf.kernel.geom.PageSize;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BomDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.UomDAO;
import com.track.db.object.BillHdr;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinProject;
import com.track.db.object.InvoiceDet;
import com.track.db.object.InvoiceHdr;
import com.track.db.object.InvoicePojo;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.ToDet;
import com.track.db.util.AgeingUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderTypeUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.service.Costofgoods;
import com.track.service.DoHDRService;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.DoHdrServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.ImageUtil;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.http.HttpUtils;
import com.track.util.pdf.PdfUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Servlet implementation class InvoiceAccServlet
 */
@WebServlet("/InvoiceServlet")
@SuppressWarnings({"rawtypes", "unchecked"})
public class InvoiceServlet  extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.InvoiceServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.InvoiceServlet_PRINTPLANTMASTERINFO;
	String action = "";
	ArrayList ALIssueddetails = new ArrayList();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = "";
		String baction = "";
		//Author: Azees  Create date: July 23,2022  Description: Fixed Import Null Error
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
		if (action.equalsIgnoreCase("importCountSheet")) {
		try {
			onImportCountSheet(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		} else {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileItemFactory factory = null;
		ServletFileUpload upload = null;
		List items = null;
		Iterator iterator = null;
		if (isMultipart) {
			factory = new DiskFileItemFactory();
			upload = new ServletFileUpload(factory);
			try {
				items = upload.parseRequest(request);
				iterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* InvoiceHdr*/
					if (fileItem.isFormField()) {
						
						if (fileItem.getFieldName().equalsIgnoreCase("Submit")) {
							action = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("ACTION")) {
							baction = StrUtils.fString(fileItem.getString()).trim();
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}else {
			action = StrUtils.fString(request.getParameter("Submit")).trim();
			baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		}
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject jsonObjectResult = new JSONObject();
		if (baction.equals("VIEW_INVOICE_SUMMARY_VIEW")) {
	    	  
	        jsonObjectResult = this.getinvoiceview(request);
	      //this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		if (baction.equals("VIEW_PEPPOL_SUMMARY_VIEW")) {
			jsonObjectResult = this.getpeppolview(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
			
		}
		if (action.equalsIgnoreCase("EDIT_DO_RCPT_INVOICE_HDR_OTHERLANGUAGE")) {
			DoHdrDAO dhd = new DoHdrDAO();
			boolean poHdrUpt = false;
			String result = "";
			
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String sUserId = (String) session.getAttribute("LOGIN_USER");
			//String Ordertype = StrUtils.fString(request.getParameter("OrderType"));
			String OutboundOrderHeader = StrUtils.fString(request.getParameter("OutboundOrderHeader"));
			String CustomerOrderHeader = StrUtils.fString(request.getParameter("CustomerOrderHeader"));
			String CollectionOrderHeader = StrUtils.fString(request.getParameter("CollectionOrderHeader"));
			String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
			String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
			String DateField = StrUtils.fString(request.getParameter("Date"));
			String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
			String RefNo = StrUtils.fString(request.getParameter("RefNo"));
			String Terms = StrUtils.fString(request.getParameter("Terms"));
			String TermsDetails = StrUtils.fString(request.getParameter("TermsDetails"));
			String SoNo = StrUtils.fString(request.getParameter("SoNo"));
			String Item = StrUtils.fString(request.getParameter("Item"));
			String Description = StrUtils.fString(request.getParameter("Description"));
			String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			String Rate = StrUtils.fString(request.getParameter("Rate"));
			String TaxAmount = StrUtils.fString(request.getParameter("TaxAmount"));
			String Amt = StrUtils.fString(request.getParameter("Amt"));
			//String SubTotal = StrUtils.fString(request.getParameter("SubTotal"));
			//String TotalTax = StrUtils.fString(request.getParameter("TotalTax"));
			//String Total = StrUtils.fString(request.getParameter("Total"));
			String printdeliverynote = (request.getParameter("printdeliverynote") != null) ? "1": "0";
			String isAutoInvoice = (request.getParameter("isAutoInvoice") != null) ? "1": "0";
			String printpackinglist = (request.getParameter("printpackinglist") != null) ? "1": "0";
			String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
			String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
			String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
			String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
			String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
			String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
			String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
			String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
			String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
			String Footer10 = StrUtils.fString(request.getParameter("Footer10"));
			String Footer11 = StrUtils.fString(request.getParameter("Footer11"));
			String Footer12 = StrUtils.fString(request.getParameter("Footer12"));
			String Footer13 = StrUtils.fString(request.getParameter("Footer13"));
			String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1" : "0";
			String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1" : "0";
			String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1" : "0";
			String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1" : "0";
			String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1" : "0";
		    String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";   
		    String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
		    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1": "0"; 
		    String printBalanceDue = (request.getParameter("printBalanceDue") != null) ? "1": "0"; 
		    String printPaymentMade = (request.getParameter("printPaymentMade") != null) ? "1": "0"; 
		    String printAdjustment = (request.getParameter("printAdjustment") != null) ? "1": "0"; 
		    String printShippingCost = (request.getParameter("printShippingCost") != null) ? "1": "0"; 
		    String printOrderDiscount = (request.getParameter("printOrderDiscount") != null) ? "1": "0"; 
			String Container=StrUtils.fString(request.getParameter("Container"));
		    String DisplayContainer = (request.getParameter("DisplayContainer") != null) ? "1": "0";
		    String remark1 = request.getParameter("REMARK1").trim();
		    String remark2 = request.getParameter("REMARK2").trim();
		    String deliverydate = request.getParameter("DeliveryDate").trim();
		    String employee= request.getParameter("Employee").trim();
		    String shipto = request.getParameter("ShipTo").trim(); 
		    String companydate = request.getParameter("CompanyDate").trim();
		    String companyname = request.getParameter("CompanyName").trim();
		    String companystamp = request.getParameter("CompanyStamp").trim();
		    String companysig = request.getParameter("CompanySig").trim(); 
		    String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";
		    //String DisplaySignature = (request.getParameter("DisplaySignature") != null) ? "1" : "0";
		    String Orientation = StrUtils.fString(request.getParameter("Orientation"));
		    String orderdiscount = request.getParameter("OrderDiscount").trim();
	        String shippingcost = request.getParameter("ShippingCost").trim();
		    String incoterm = request.getParameter("INCOTERM").trim();	
		    String printincoterm = (request.getParameter("printincoterm") != null) ? "1" : "0";
		    String hscode = request.getParameter("HSCODE").trim();
		    String coo = request.getParameter("COO").trim();
		    String rcbno = request.getParameter("RCBNO").trim();
		    String invoiceno = request.getParameter("INVOICENO").trim();
		    String invoicedate = request.getParameter("INVOICEDATE").trim();
		    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
		    String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
		    
		    String uenno = request.getParameter("UENNO").trim();
		    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
		    
		    String totalafterdiscount = request.getParameter("TOTALAFTERDISCOUNT").trim();
		    String brand = request.getParameter("BRAND").trim();
		    String cdiscount = request.getParameter("TOTALDISCOUNT").trim();
		    String ctax = request.getParameter("TAX").trim();
		    String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
		    String Seller = StrUtils.fString(request.getParameter("Seller"));
		    String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
		    String Buyer = StrUtils.fString(request.getParameter("Buyer"));
		    String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
		    String printwithTaxInvoice = (request.getParameter("printwithTaxInvoice") != null) ? "1": "0";
		    String Multilanguprint = (request.getParameter("Multilanguprint") != null) ? "1": "0";
			String SubTotal = StrUtils.fString(request.getParameter("SubTotal"));
			String Tax = StrUtils.fString(request.getParameter("Tax"));
			String Total = StrUtils.fString(request.getParameter("Total"));
			String ContactName = StrUtils.fString(request.getParameter("ContactName"));
			String Email = StrUtils.fString(request.getParameter("Email"));
			String Fax = StrUtils.fString(request.getParameter("Fax"));
			String Telephone = StrUtils.fString(request.getParameter("Telephone"));
			String Handphone = StrUtils.fString(request.getParameter("Handphone"));
			String Attention = StrUtils.fString(request.getParameter("Attention"));
			String QtyTotal = StrUtils.fString(request.getParameter("QtyTotal"));
			String remark3 = StrUtils.fString(request.getParameter("remark3"));
			String FooterPage = StrUtils.fString(request.getParameter("FooterPage"));
			String FooterOf = StrUtils.fString(request.getParameter("FooterOf"));
			String CashCustomer = StrUtils.fString(request.getParameter("CashCustomer"));
			String RoundoffTotalwithDecimal = StrUtils.fString(request.getParameter("RoundoffTotalwithDecimal"));
			String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
			String printwithProduct = (request.getParameter("printwithProduct") != null) ? "1": "0";
			String printDiscountReport = (request.getParameter("printDiscountReport") != null) ? "1": "0"; 
		    String Discount = request.getParameter("Discount").trim();
			String NetRate = request.getParameter("NetRate").trim();
			
			String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			
			String Adjustment = StrUtils.fString(request.getParameter("ADJUSTMENT"));
			String PaymentMade = StrUtils.fString(request.getParameter("PAYMENTMADE"));
			String BalanceDue = StrUtils.fString(request.getParameter("BALANCEDUE"));
			
			String Gino = StrUtils.fString(request.getParameter("GINO"));
			String GinoDate = StrUtils.fString(request.getParameter("GINODATE"));
			
//		    RESVI STARTS
		    String project = request.getParameter("Project").trim();
		    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
		    String printWithUENNO = (request.getParameter("printWithUENNO"));
		    String printcopy = (request.getParameter("printcopy"));
//		    String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
		    String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
		    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
		    String printwithcustnameadrress = (request.getParameter("printwithcustnameadrress") != null) ? "1": "0";
		    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
		    String TransportMode = request.getParameter("TransportMode").trim();
		    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
		    String printwithshipingadd = (request.getParameter("printwithshipingadd") != null) ? "1": "0";
//		    RESVI ENDS  
		    String showPreviousSalesCost = (request.getParameter("showPreviousSalesCost") ) ;
			
		    Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IDBConstants.PLANT, PLANT);
			ht.put(IDBConstants.ORDERTYPE, "Tax Invoice English");
			 /*if (Ordertype.equalsIgnoreCase("Tax Invoice English")) {
		            ht.put(IDBConstants.ORDERTYPE, Ordertype);
		    } else {
		            ht.put(IDBConstants.ORDERTYPE, Ordertype);
		    }*/
		    
			StringBuffer QryUpdate = new StringBuffer(" SET ");

			if (OutboundOrderHeader.length() > 0)
				QryUpdate.append(" ORDERHEADER = '" + OutboundOrderHeader
						+ "' ");
			if (CustomerOrderHeader.length() > 0)
				QryUpdate.append(", CUSTOMERHEADER = '" + CustomerOrderHeader
						+ "' ");
			if (CollectionOrderHeader.length() > 0)
				QryUpdate.append(", COLLECTIONHEADER = '" + CollectionOrderHeader
						+ "' ");
			if (InvoiceOrderToHeader.length() > 0)
				QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader
						+ "' ");
			if (FromHeader.length() > 0)
				QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
			if (DateField.length() > 0)
				QryUpdate.append(", DATE = '" + DateField + "' ");
			if (OrderNo.length() > 0)
				QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
			if (RefNo.length() > 0)
				QryUpdate.append(", REFNO = '" + RefNo + "' ");
			if (Terms.length() > 0)
				QryUpdate.append(", TERMS = '" + Terms + "' ");
			//if (TermsDetails.length() > 0)
				QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
			if (SoNo.length() > 0)
				QryUpdate.append(", SONO = '" + SoNo + "' ");
			if (Item.length() > 0)
				QryUpdate.append(", ITEM = '" + Item + "' ");
			if (Description.length() > 0)
				QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
			if (OrderQty.length() > 0)
				QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
			if (UOM.length() > 0)
				QryUpdate.append(", UOM = '" + UOM + "' ");
			if (Rate.length() > 0)
				QryUpdate.append(", RATE = '" + Rate + "' ");
			if (TaxAmount.length() > 0)
				QryUpdate.append(", TAXAMOUNT = '" + TaxAmount + "' ");
			if (Amt.length() > 0)
				QryUpdate.append(", AMT = '" + Amt + "' ");
			//if (SubTotal.length() > 0)
				//QryUpdate.append(", SubTotal = '" + SubTotal + "' ");
			//if (TotalTax.length() > 0)
				//QryUpdate.append(", TotalTax = '" + TotalTax + "' ");
			//if (Total.length() > 0)
				//QryUpdate.append(", Total = '" + Total + "' ");

			if (Footer1.equalsIgnoreCase("")) QryUpdate.append(", FOOTER1 = '" + "  " + "' ");else QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
			if (Footer2.equalsIgnoreCase("")) QryUpdate.append(", FOOTER2 = '" + "  " + "' ");else QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
			if (Footer3.equalsIgnoreCase("")) QryUpdate.append(", FOOTER3 = '" + "  " + "' ");else QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
			if (Footer4.equalsIgnoreCase("")) QryUpdate.append(", FOOTER4 = '" + "  " + "' ");else QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
			if (Footer5.equalsIgnoreCase("")) QryUpdate.append(", FOOTER5 = '" + "  " + "' ");else QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
			if (Footer6.equalsIgnoreCase("")) QryUpdate.append(", FOOTER6 = '" + "  " + "' ");else QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
			if (Footer7.equalsIgnoreCase("")) QryUpdate.append(", FOOTER7 = '" + "  " + "' ");else QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
			if (Footer8.equalsIgnoreCase("")) QryUpdate.append(", FOOTER8 = '" + "  " + "' ");else QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
			if (Footer9.equalsIgnoreCase("")) QryUpdate.append(", FOOTER9 = '" + "  " + "' ");else QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
			if (Footer10.equalsIgnoreCase("")) QryUpdate.append(", FOOTER10 = '" + "  " + "' ");else QryUpdate.append(", FOOTER10 = '" + Footer10 + "' ");
			if (Footer11.equalsIgnoreCase("")) QryUpdate.append(", FOOTER11 = '" + "  " + "' ");else QryUpdate.append(", FOOTER11 = '" + Footer11 + "' ");
			if (Footer12.equalsIgnoreCase("")) QryUpdate.append(", FOOTER12 = '" + "  " + "' ");else QryUpdate.append(", FOOTER12 = '" + Footer12 + "' ");
			if (Footer13.equalsIgnoreCase("")) QryUpdate.append(", FOOTER13 = '" + "  " + "' ");else QryUpdate.append(", FOOTER13 = '" + Footer13 + "' ");
//			QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
//			QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
//			QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
//			QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
//			QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
//			QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
//			QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
//			QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
//			QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
//			QryUpdate.append(", FOOTER10 = '" + Footer10 + "' "+", FOOTER11 = '" + Footer11 + "' "+", FOOTER12 = '" + Footer12 + "' "+", FOOTER13 = '" + Footer13 + "' ");
//			QryUpdate.append(", FOOTER11 = '" + Footer11 + "' ");
//			QryUpdate.append(", FOOTER12 = '" + Footer12 + "' ");
//			QryUpdate.append(", FOOTER13 = '" + Footer13 + "' ");
			QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
			QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype	+ "' ");
			QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand	+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks	+ "' ");
			QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode	+ "' ");
			QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo	+ "' " );
			QryUpdate.append(", CONTAINER ='" + Container+ "' ");
			QryUpdate.append(", DISPLAYCONTAINER ='" + DisplayContainer+ "' ");
		    QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
		    QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
		    QryUpdate.append(", PCUSREMARKS ='" + printCustRemarks+ "' ");
		    QryUpdate.append(", PRINTBALANCEDUE ='" + printBalanceDue+ "' ");
		    QryUpdate.append(", PRINTPAYMENTMADE ='" + printPaymentMade+ "' ");
		    QryUpdate.append(", PRINTADJUSTMENT ='" + printAdjustment+ "' ");
		    QryUpdate.append(", PRINTSHIPPINGCOST ='" + printShippingCost+ "' ");
		    QryUpdate.append(", PRINTORDERDISCOUNT ='" + printOrderDiscount+ "' ");
		    QryUpdate.append(", PRINTDELIVERYNOTE ='" + printdeliverynote+ "' ");
		    QryUpdate.append(", PRINTPACKINGLIST ='" + printpackinglist+ "' ");
		    QryUpdate.append(", ISAUTOINVOICE ='" + isAutoInvoice+ "' ");
		   // QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO	+ "' ");
			//QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO	+ "' ");
		    
			QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
			QryUpdate.append(", UPBY = '" + sUserId + "' ");
			QryUpdate.append(", PrintOrientation = '" + Orientation+ "' ");
			if (remark1.length() > 0)
				QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
		    if (remark2.length() > 0)
				QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
		    
		    if (totalafterdiscount.length() > 0)
				QryUpdate.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscount + "' ");
		    
		    if (brand.length() > 0)
				QryUpdate.append(", BRAND = '" + brand + "' ");
		    
		    if (cdiscount.length() > 0)
				QryUpdate.append(", TOTALDISCOUNT = '" + cdiscount + "' ");
		    
		    if (ctax.length() > 0)
				QryUpdate.append(", TAX = '" + ctax + "' ");
		    
		    if (rcbno.length() > 0)
				QryUpdate.append(", RCBNO = '" + rcbno + "' ");
		    if (hscode.length() > 0)
				QryUpdate.append(", HSCODE = '" + hscode + "' ");
		    if (coo.length() > 0)
				QryUpdate.append(", COO = '" + coo + "' ");
		    
		    if (customerrcbno.length() > 0)
				QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
		    
		    if (uenno.length() > 0)
		    	QryUpdate.append(", UENNO = '" + uenno + "' ");
		    
		    if (customeruenno.length() > 0)
				QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
		    
		    if (invoiceno.length() > 0)
				QryUpdate.append(", INVOICENO = '" + invoiceno + "' ");
		    
		    if (invoicedate.length() > 0)
				QryUpdate.append(", INVOICEDATE = '" + invoicedate + "' ");
		    
		    if (deliverydate.length() > 0)
				QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");	
		    if (employee.length() > 0)
				QryUpdate.append(", EMPLOYEE = '" + employee + "' ");		
			if (shipto.length() > 0)
				QryUpdate.append(", SHIPTO = '" + shipto + "' ");	
			if (companydate.length() > 0)
				QryUpdate.append(", COMPANYDATE = '" + companydate + "' ");
			if (companyname.length() > 0)
				QryUpdate.append(", COMPANYNAME = '" + companyname + "' ");
			if (companystamp.length() > 0)
				QryUpdate.append(", COMPANYSTAMP = '" + companystamp + "' ");
			if (companysig.length() > 0)
				QryUpdate.append(", COMPANYSIG = '" + companysig + "' ");
			if (printEmployee.length() > 0)
				QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
			if (printincoterm.length() > 0)
				QryUpdate.append(", PRINTINCOTERM = '" + printincoterm + "' ");
			//if (DisplaySignature.length() > 0)
				//QryUpdate.append(", DISPLAYSIGNATURE = '" + DisplaySignature + "' ");
			if (orderdiscount.length() > 0)
				QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
			if (shippingcost.length() > 0)
				QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
			if (incoterm.length() > 0)
				QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
			if (PreparedBy.length() > 0)
				QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
			if (Seller.length() > 0)
				QryUpdate.append(", SELLER = '" + Seller + "' ");
			if (SellerSignature.length() > 0)
				QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
			if (Buyer.length() > 0)
				QryUpdate.append(", BUYER = '" + Buyer + "' ");
			if (BuyerSignature.length() > 0)
				QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");
			
			QryUpdate.append(", PRINTWITHTAXINVOICE ='" + printwithTaxInvoice	+ "' ");
			
			QryUpdate.append(", PRINTMULTILANG ='" + Multilanguprint	+ "' ");
			
			if (SubTotal.length() > 0)
				QryUpdate.append(", SUBTOTAL = '" + SubTotal + "' ");
			if (Tax.length() > 0)
				QryUpdate.append(", TOTALTAX = '" + Tax + "' ");
			if (Total.length() > 0)
				QryUpdate.append(", TOTAL = '" + Total + "' ");
			if (ContactName.length() > 0)
				QryUpdate.append(", CONTACTNAME = '" + ContactName + "' ");
			if (Email.length() > 0)
				QryUpdate.append(", EMAIL = '" + Email + "' ");
			if (Fax.length() > 0)
				QryUpdate.append(", FAX = '" + Fax + "' ");
			if (Telephone.length() > 0)
				QryUpdate.append(", TELEPHONE = '" + Telephone + "' ");
			if (Handphone.length() > 0)
				QryUpdate.append(", HANDPHONE = '" + Handphone + "' ");
			if (Attention.length() > 0)
				QryUpdate.append(", ATTENTION = '" + Attention + "' ");
			if (QtyTotal.length() > 0)
				QryUpdate.append(", QTYTOTAL = '" + QtyTotal + "' ");
//			if (remark3.length() > 0)
				QryUpdate.append(", REMARK3 = '" + remark3 + "' ");
			if (FooterPage.length() > 0)
				QryUpdate.append(", FOOTERPAGE = '" + FooterPage + "' ");
			if (FooterOf.length() > 0)
				QryUpdate.append(", FOOTEROF = '" + FooterOf + "' ");
			if (CashCustomer.length() > 0)
				QryUpdate.append(", CASHCUSTOMER = '" + CashCustomer + "' ");
				
			if (RoundoffTotalwithDecimal.length() > 0)
				QryUpdate.append(", ROUNDOFFTOTALWITHDECIMAL = '" + RoundoffTotalwithDecimal + "' ");
			QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCT ='" + printwithProduct+ "' ");
			QryUpdate.append(", PRINTWITHDISCOUNT ='" + printDiscountReport+ "' ");
			if (Discount.length() > 0)
				QryUpdate.append(", DISCOUNT = '" + Discount + "' ");
			if (NetRate.length() > 0)
				QryUpdate.append(", NETRATE = '" + NetRate + "' ");
			
			if (prdDeliveryDate.length() > 0)
				QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
				QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
				QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
			
			if (Adjustment.length() > 0)
				QryUpdate.append(", ADJUSTMENT = '" + Adjustment + "' ");
			
			if (PaymentMade.length() > 0)
				QryUpdate.append(", PAYMENTMADE = '" + PaymentMade + "' ");
			
			if (BalanceDue.length() > 0)
				QryUpdate.append(", BALANCEDUE = '" + BalanceDue + "' ");
			
			if (Gino.length() > 0)
				QryUpdate.append(", GINO = '" + Gino + "' ");
			
			if (GinoDate.length() > 0)
				QryUpdate.append(", GINODate = '" + GinoDate + "' ");
			
			
//			RESVI STARTS
				if (project.length() > 0)
					QryUpdate.append(", PROJECT = '" + project + "' ");	
				
				if (TransportMode.length() > 0)
					QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
				
				QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
				QryUpdate.append(", PRINTWITHSHIPINGADD ='" + printwithshipingadd+ "' ");
				QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
				QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
				QryUpdate.append(", ISPRINTDEFAULT ='" + printcopy+ "' ");
				QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
				QryUpdate.append(", PRINTWITHCUSTNAMEADRRESS='" + printwithcustnameadrress+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
//	         RESVI ENDS
				QryUpdate.append(", SHOWPREVIOUSINVOICECOST = '" + showPreviousSalesCost+ "' ");

			try {
				poHdrUpt = dhd.updateDOReciptInvoiceHeaderDO(QryUpdate.toString(), ht, "");
			} catch (Exception e) {

			}
			
			//To update DO
			if (poHdrUpt)
			{
			//String Ordertypedo = StrUtils.fString(request.getParameter("OrderType"));	
			String OutboundOrderHeaderDO = StrUtils.fString(request.getParameter("OutboundOrderHeaderDO"));
			String InvoiceOrderToHeaderDO = StrUtils.fString(request.getParameter("InvoiceOrderToHeaderDO"));
			String FromHeaderDO = StrUtils.fString(request.getParameter("FromHeaderDO"));
			String DateFieldDO = StrUtils.fString(request.getParameter("DateDO"));
			String OrderNoDO = StrUtils.fString(request.getParameter("OrderNoDO"));
			String RefNoDO = StrUtils.fString(request.getParameter("RefNoDO"));
			String TermsDO = StrUtils.fString(request.getParameter("TermsDO"));
			String TermsDetailsDO = StrUtils.fString(request.getParameter("TermsDetailsDO"));
			String SoNoDO = StrUtils.fString(request.getParameter("SoNoDO"));
			String ItemDO = StrUtils.fString(request.getParameter("ItemDO"));
			String DescriptionDO = StrUtils.fString(request.getParameter("DescriptionDO"));
			String OrderQtyDO = StrUtils.fString(request.getParameter("OrderQtyDO"));
			String UOMDO = StrUtils.fString(request.getParameter("UOMDO"));
			String RateDO = StrUtils.fString(request.getParameter("RateDO"));
			String TaxAmountDO = StrUtils.fString(request.getParameter("TaxAmountDO"));
			String AmtDO = StrUtils.fString(request.getParameter("AmtDO"));
			//String SubTotalDO = StrUtils.fString(request.getParameter("SubTotalDO"));
			//String TotalTaxDO = StrUtils.fString(request.getParameter("TotalTaxDO"));
			//String TotalDO = StrUtils.fString(request.getParameter("TotalDO"));
			String printdeliverynoteDO = (request.getParameter("printdeliverynoteDO") != null) ? "1": "0";
			String isAutoInvoiceDO = (request.getParameter("isAutoInvoiceDO") != null) ? "1": "0";
			String printpackinglistDO = (request.getParameter("printpackinglistDO") != null) ? "1": "0";
			String Footer1DO = StrUtils.fString(request.getParameter("Footer1DO"));
			String Footer2DO = StrUtils.fString(request.getParameter("Footer2DO"));
			String Footer3DO = StrUtils.fString(request.getParameter("Footer3DO"));
			String Footer4DO = StrUtils.fString(request.getParameter("Footer4DO"));
			String Footer5DO = StrUtils.fString(request.getParameter("Footer5DO"));
			String Footer6DO = StrUtils.fString(request.getParameter("Footer6DO"));
			String Footer7DO = StrUtils.fString(request.getParameter("Footer7DO"));
			String Footer8DO = StrUtils.fString(request.getParameter("Footer8DO"));
			String Footer9DO = StrUtils.fString(request.getParameter("Footer9DO"));
			String DisplayByOrdertypeDO = (request.getParameter("DisplayByOrdertypeDO") != null) ? "1" : "0";
			String printwithbrandDO = (request.getParameter("printwithbrandDO") != null) ? "1" : "0";
			String printwithproductremarksDO = (request.getParameter("printwithproductremarksDO") != null) ? "1" : "0";
			String printwithhscodeDO = (request.getParameter("printwithhscodeDO") != null) ? "1" : "0";
			String printwithcooDO = (request.getParameter("printwithcooDO") != null) ? "1" : "0";
		    String printDetailDescDO = (request.getParameter("printDetailDescDO") != null) ? "1": "0";   
		    String printCustTermsDO = (request.getParameter("printCustTermsDO") != null) ? "1": "0";
		    String printCustRemarksDO = (request.getParameter("printCustRemarksDO") != null) ? "1": "0"; 
			String ContainerDO=StrUtils.fString(request.getParameter("ContainerDO"));
		    String DisplayContainerDO = (request.getParameter("DisplayContainerDO") != null) ? "1": "0";
		    String remark1DO = StrUtils.fString(request.getParameter("REMARK1DO"));
		    String remark2DO = StrUtils.fString(request.getParameter("REMARK2DO"));
		    String deliverydateDO = StrUtils.fString(request.getParameter("DeliveryDateDO"));
		    String employeeDO= StrUtils.fString(request.getParameter("EmployeeDO"));
		    String shiptoDO = StrUtils.fString(request.getParameter("ShipToDO")); 
		    String companydateDO = StrUtils.fString(request.getParameter("CompanyDateDO"));
		    String companynameDO = StrUtils.fString(request.getParameter("CompanyNameDO"));
		    String companystampDO = StrUtils.fString(request.getParameter("CompanyStampDO"));
		    String companysigDO = StrUtils.fString(request.getParameter("CompanySigDO")); 
		    String printEmployeeDO = (request.getParameter("printEmployeeDO") != null) ? "1" : "0";
		    //String DisplaySignatureDO = (request.getParameter("DisplaySignatureDO") != null) ? "1" : "0";
		    String OrientationDO = StrUtils.fString(request.getParameter("OrientationDO"));
		    String orderdiscountDO = StrUtils.fString(request.getParameter("OrderDiscountDO"));
	        String shippingcostDO = StrUtils.fString(request.getParameter("ShippingCostDO"));
		    String incotermDO = StrUtils.fString(request.getParameter("INCOTERMDO"));	
		    String hscodeDO = StrUtils.fString(request.getParameter("HSCODEDO"));
		    String cooDO = StrUtils.fString(request.getParameter("COODO"));
		    String rcbnoDO = StrUtils.fString(request.getParameter("RCBNODO"));
		    String invoicenoDO = StrUtils.fString(request.getParameter("INVOICENODO"));
		    String invoicedateDO = StrUtils.fString(request.getParameter("INVOICEDATEDO"));
		    String customerrcbnoDO = StrUtils.fString(request.getParameter("CUSTOMERRCBNODO"));
		    String totalafterdiscountDO = StrUtils.fString(request.getParameter("TOTALAFTERDISCOUNTDO"));
		    String brandDO = StrUtils.fString(request.getParameter("BRANDDO"));
		    String cdiscountDO = StrUtils.fString(request.getParameter("TOTALDISCOUNTDO"));
		    String ctaxDO = StrUtils.fString(request.getParameter("TAXDO"));
		    String PreparedByDO = StrUtils.fString(request.getParameter("PreparedByDO"));
		    String SellerDO = StrUtils.fString(request.getParameter("SellerDO"));
		    String SellerSignatureDO = StrUtils.fString(request.getParameter("SellerSignatureDO"));
		    String BuyerDO = StrUtils.fString(request.getParameter("BuyerDO"));
		    String BuyerSignatureDO = StrUtils.fString(request.getParameter("BuyerSignatureDO"));
		    String printwithTaxInvoiceDO = (request.getParameter("printwithTaxInvoiceDO") != null) ? "1": "0";	
			String SubTotalDO = StrUtils.fString(request.getParameter("SubTotalDO"));
			String TaxDO = StrUtils.fString(request.getParameter("TaxDO"));
			String TotalDO = StrUtils.fString(request.getParameter("TotalDO"));
			String ContactNameDO = StrUtils.fString(request.getParameter("ContactNameDO"));
			String EmailDO = StrUtils.fString(request.getParameter("EmailDO"));
			String FaxDO = StrUtils.fString(request.getParameter("FaxDO"));
			String TelephoneDO = StrUtils.fString(request.getParameter("TelephoneDO"));
			String HandphoneDO = StrUtils.fString(request.getParameter("HandphoneDO"));
			String AttentionDO = StrUtils.fString(request.getParameter("AttentionDO"));
			String QtyTotalDO = StrUtils.fString(request.getParameter("QtyTotalDO"));
			String remark3DO = StrUtils.fString(request.getParameter("remark3DO"));
			String FooterPageDO = StrUtils.fString(request.getParameter("FooterPageDO"));
			String FooterOfDO = StrUtils.fString(request.getParameter("FooterOfDO"));
			String CashCustomerDO = StrUtils.fString(request.getParameter("CashCustomerDO"));
			String RoundoffTotalwithDecimalDO = StrUtils.fString(request.getParameter("RoundoffTotalwithDecimalDO"));
			String printRoundoffTotalwithDecimalDO = (request.getParameter("printRoundoffTotalwithDecimalDO") != null) ? "1": "0";
			String printwithProductDO = (request.getParameter("printwithProductDO") != null) ? "1": "0";
			String printDiscountReportDO = (request.getParameter("printDiscountReportDO") != null) ? "1": "0"; 
		    String DiscountDO = StrUtils.fString(request.getParameter("DiscountDO"));
			String NetRateDO = StrUtils.fString(request.getParameter("NetRateDO"));
			
			String prdDeliveryDateDO = StrUtils.fString(request.getParameter("prdDeliveryDateDO"));
			String printWithPrdDeliveryDateDO = (request.getParameter("printWithPrdDeliveryDateDO") != null) ? "1": "0";
			String printWithDeliveryDateDO = (request.getParameter("printWithDeliveryDateDO") != null) ? "1": "0";
			String printwithcompanysigDO = (request.getParameter("printwithcompanysigDO") != null) ? "1": "0";
			String printwithcompanysealDO = (request.getParameter("printwithcompanysealDO") != null) ? "1": "0";
			
		    
		    Hashtable<String, String> htDO = new Hashtable<String, String>();
			htDO.put(IDBConstants.PLANT, PLANT);
			htDO.put(IDBConstants.ORDERTYPE, "Tax Invoice Other Languages");
			/*if (Ordertypedo.equalsIgnoreCase("Tax Invoice Other Languages")) {
	            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
	    } else {
	            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
	    }*/
			StringBuffer QryUpdateDO = new StringBuffer(" SET ");

			if (OutboundOrderHeaderDO.length() > 0)
				QryUpdateDO.append(" ORDERHEADER = N'" + OutboundOrderHeaderDO
						+ "' ");
			if (InvoiceOrderToHeaderDO.length() > 0)
				QryUpdateDO.append(", TOHEADER = N'" + InvoiceOrderToHeaderDO
						+ "' ");
			if (FromHeaderDO.length() > 0)
				QryUpdateDO.append(", FROMHEADER = N'" + FromHeaderDO + "' ");
			if (DateFieldDO.length() > 0)
				QryUpdateDO.append(", DATE = N'" + DateFieldDO + "' ");
			if (OrderNoDO.length() > 0)
				QryUpdateDO.append(", ORDERNO = N'" + OrderNoDO + "' ");
			if (RefNoDO.length() > 0)
				QryUpdateDO.append(", REFNO = N'" + RefNoDO + "' ");
			if (TermsDO.length() > 0)
				QryUpdateDO.append(", TERMS = N'" + TermsDO + "' ");
			//if (TermsDetails.length() > 0)
				QryUpdateDO.append(" TERMSDETAILS = N'" + TermsDetailsDO + "' ");
			if (SoNoDO.length() > 0)
				QryUpdateDO.append(", SONO = N'" + SoNoDO + "' ");
			if (ItemDO.length() > 0)
				QryUpdateDO.append(", ITEM = N'" + ItemDO + "' ");
			if (DescriptionDO.length() > 0)
				QryUpdateDO.append(", DESCRIPTION = N'" + DescriptionDO + "' ");
			if (OrderQtyDO.length() > 0)
				QryUpdateDO.append(", ORDERQTY = N'" + OrderQtyDO + "' ");
			if (UOMDO.length() > 0)
				QryUpdateDO.append(", UOM = N'" + UOMDO + "' ");
			if (RateDO.length() > 0)
				QryUpdateDO.append(", RATE = N'" + RateDO + "' ");
			if (TaxAmountDO.length() > 0)
				QryUpdateDO.append(", TAXAMOUNT = N'" + TaxAmountDO + "' ");
			if (AmtDO.length() > 0)
				QryUpdateDO.append(", AMT = N'" + AmtDO + "' ");
			//if (SubTotalDO.length() > 0)
				//QryUpdateDO.append(", SubTotal = N'" + SubTotalDO + "' ");
			//if (TotalTaxDO.length() > 0)
				//QryUpdateDO.append(", TotalTax = N'" + TotalTaxDO + "' ");
			//if (TotalDO.length() > 0)
				//QryUpdateDO.append(", Total = N'" + TotalDO + "' ");

			QryUpdateDO.append(", FOOTER1 = N'" + Footer1DO + "' ");
			QryUpdateDO.append(", FOOTER2 = N'" + Footer2DO + "' ");
			QryUpdateDO.append(", FOOTER3 = N'" + Footer3DO + "' ");
			QryUpdateDO.append(", FOOTER4 = N'" + Footer4DO + "' ");
			QryUpdateDO.append(", FOOTER5 = N'" + Footer5DO + "' ");
			QryUpdateDO.append(", FOOTER6 = N'" + Footer6DO + "' ");
			QryUpdateDO.append(", FOOTER7 = N'" + Footer7DO + "' ");
			QryUpdateDO.append(", FOOTER8 = N'" + Footer8DO + "' ");
			QryUpdateDO.append(", FOOTER9 = N'" + Footer9DO + "' ");
			QryUpdateDO.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertypeDO	+ "' ");
			QryUpdateDO.append(", PRINTWITHBRAND ='" + printwithbrandDO	+ "' ");
			QryUpdateDO.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarksDO	+ "' ");
			QryUpdateDO.append(", PRITNWITHHSCODE ='" + printwithhscodeDO	+ "' ");
			QryUpdateDO.append(", PRITNWITHCOO ='" + printwithcooDO	+ "' ");
			QryUpdateDO.append(", CONTAINER = N'" + ContainerDO+ "' ");
			QryUpdateDO.append(", DISPLAYCONTAINER ='" + DisplayContainerDO+ "' ");
		    QryUpdateDO.append(", PRINTXTRADETAILS ='" + printDetailDescDO+ "' ");
		    QryUpdateDO.append(", PRINTCUSTERMS ='" + printCustTermsDO+ "' ");
		    QryUpdateDO.append(", PCUSREMARKS ='" + printCustRemarksDO+ "' ");
		    QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO+ "' ");
		    QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO+ "' ");
		    QryUpdateDO.append(", ISAUTOINVOICE ='" + isAutoInvoiceDO+ "' ");
		    QryUpdateDO.append(", PRINTWITHCOMPANYSIG ='" + printwithcompanysigDO+ "' ");
		    QryUpdateDO.append(", PRINTWITHCOMPANYSEAL ='" + printwithcompanysealDO+ "' ");
		    
		   // QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO	+ "' ");
			//QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO	+ "' ");
		    
			QryUpdateDO.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
			QryUpdateDO.append(", UPBY = '" + sUserId + "' ");
			QryUpdateDO.append(", PrintOrientation = '" + OrientationDO+ "' ");
			if (remark1DO.length() > 0)
				QryUpdateDO.append(", REMARK1 = N'" + remark1DO + "' ");
		    if (remark2DO.length() > 0)
				QryUpdateDO.append(", REMARK2 = N'" + remark2DO + "' ");
		    
		    if (totalafterdiscountDO.length() > 0)
				QryUpdateDO.append(", TOTALAFTERDISCOUNT = N'" + totalafterdiscountDO + "' ");
		    
		    if (brandDO.length() > 0)
				QryUpdateDO.append(", BRAND = N'" + brandDO + "' ");
		    
		    if (cdiscountDO.length() > 0)
				QryUpdateDO.append(", TOTALDISCOUNT = N'" + cdiscountDO + "' ");
		    
		    if (ctaxDO.length() > 0)
				QryUpdateDO.append(", TAX = N'" + ctaxDO + "' ");
		    
		    if (rcbnoDO.length() > 0)
				QryUpdateDO.append(", RCBNO = N'" + rcbnoDO + "' ");
		    if (hscodeDO.length() > 0)
				QryUpdateDO.append(", HSCODE = N'" + hscodeDO + "' ");
		    if (cooDO.length() > 0)
				QryUpdateDO.append(", COO = N'" + cooDO + "' ");
		    
		    if (customerrcbnoDO.length() > 0)
				QryUpdateDO.append(", CUSTOMERRCBNO = N'" + customerrcbnoDO + "' ");
		    if (invoicenoDO.length() > 0)
				QryUpdateDO.append(", INVOICENO = N'" + invoicenoDO + "' ");
		    
		    if (invoicedateDO.length() > 0)
				QryUpdateDO.append(", INVOICEDATE = N'" + invoicedateDO + "' ");
		    
		    if (deliverydateDO.length() > 0)
				QryUpdateDO.append(", DELIVERYDATE = N'" + deliverydateDO + "' ");	
		    if (employeeDO.length() > 0)
				QryUpdateDO.append(", EMPLOYEE = N'" + employeeDO + "' ");		
			if (shiptoDO.length() > 0)
				QryUpdateDO.append(", SHIPTO = N'" + shiptoDO + "' ");	
			if (companydateDO.length() > 0)
				QryUpdateDO.append(", COMPANYDATE = N'" + companydateDO + "' ");
			if (companynameDO.length() > 0)
				QryUpdateDO.append(", COMPANYNAME = N'" + companynameDO + "' ");
			if (companystampDO.length() > 0)
				QryUpdateDO.append(", COMPANYSTAMP = N'" + companystampDO + "' ");
			if (companysigDO.length() > 0)
				QryUpdateDO.append(", COMPANYSIG = N'" + companysigDO + "' ");
			if (printEmployeeDO.length() > 0)
				QryUpdateDO.append(", PRINTEMPLOYEE = '" + printEmployeeDO + "' ");
			//if (DisplaySignatureDO.length() > 0)
				//QryUpdateDO.append(", DISPLAYSIGNATURE = '" + DisplaySignatureDO + "' ");
			if (orderdiscountDO.length() > 0)
				QryUpdateDO.append(", ORDERDISCOUNT = N'" + orderdiscountDO + "' ");
			if (shippingcostDO.length() > 0)
				QryUpdateDO.append(", SHIPPINGCOST = N'" + shippingcostDO + "' ");
			if (incotermDO.length() > 0)
				QryUpdateDO.append(", INCOTERM = N'" + incotermDO + "' ");
			if (PreparedByDO.length() > 0)
				QryUpdateDO.append(", PREPAREDBY = N'" + PreparedByDO + "' ");
			if (SellerDO.length() > 0)
				QryUpdateDO.append(", SELLER = N'" + SellerDO + "' ");
			if (SellerSignatureDO.length() > 0)
				QryUpdateDO.append(", SELLERSIGNATURE = N'" + SellerSignatureDO + "' ");
			if (BuyerDO.length() > 0)
				QryUpdateDO.append(", BUYER = N'" + BuyerDO + "' ");
			if (BuyerSignatureDO.length() > 0)
				QryUpdateDO.append(", BUYERSIGNATURE = N'" + BuyerSignatureDO + "' ");
			
			QryUpdateDO.append(", PRINTWITHTAXINVOICE ='" + printwithTaxInvoiceDO	+ "' ");
			
			QryUpdateDO.append(", PRINTMULTILANG ='" + Multilanguprint	+ "' ");
			
			if (SubTotalDO.length() > 0)
			   QryUpdateDO.append(", SUBTOTAL = N'" + SubTotalDO + "' ");
		    if (TaxDO.length() > 0)
			   QryUpdateDO.append(", TOTALTAX = N'" + TaxDO + "' ");
		    if (TotalDO.length() > 0)
			   QryUpdateDO.append(", TOTAL = N'" + TotalDO + "' ");
			if (ContactNameDO.length() > 0)
				QryUpdateDO.append(", CONTACTNAME = N'" + ContactNameDO + "' ");
			if (EmailDO.length() > 0)
				QryUpdateDO.append(", EMAIL = N'" + EmailDO + "' ");
			if (FaxDO.length() > 0)
				QryUpdateDO.append(", FAX = N'" + FaxDO + "' ");
			if (TelephoneDO.length() > 0)
				QryUpdateDO.append(", TELEPHONE = N'" + TelephoneDO + "' ");
			if (HandphoneDO.length() > 0)
				QryUpdateDO.append(", HANDPHONE = N'" + HandphoneDO + "' ");
			if (AttentionDO.length() > 0)
				QryUpdateDO.append(", ATTENTION = N'" + AttentionDO + "' ");
			if (QtyTotalDO.length() > 0)
				QryUpdateDO.append(", QTYTOTAL = N'" + QtyTotalDO + "' ");
			if (remark3DO.length() > 0)
				QryUpdateDO.append(", REMARK3 = N'" + remark3DO + "' ");
			if (FooterPageDO.length() > 0)
				QryUpdateDO.append(", FOOTERPAGE = N'" + FooterPageDO + "' ");
			if (FooterOfDO.length() > 0)
				QryUpdateDO.append(", FOOTEROF = N'" + FooterOfDO + "' ");
			if (CashCustomerDO.length() > 0)
				QryUpdateDO.append(", CASHCUSTOMER = N'" + CashCustomerDO + "' ");
				
			if (RoundoffTotalwithDecimalDO.length() > 0)
				QryUpdateDO.append(", ROUNDOFFTOTALWITHDECIMAL = N'" + RoundoffTotalwithDecimalDO + "' ");
			QryUpdateDO.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimalDO+ "' ");
			QryUpdateDO.append(", PRINTWITHPRODUCT ='" + printwithProductDO+ "' ");
			QryUpdateDO.append(", PRINTWITHDISCOUNT ='" + printDiscountReportDO+ "' ");
			if (DiscountDO.length() > 0)
				QryUpdateDO.append(", DISCOUNT = N'" + DiscountDO + "' ");
			if (NetRateDO.length() > 0)
				QryUpdateDO.append(", NETRATE = N'" + NetRateDO + "' ");
			
			if (prdDeliveryDateDO.length() > 0)
				QryUpdateDO.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDateDO + "' ");	
				QryUpdateDO.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDateDO+ "' ");
				QryUpdateDO.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDateDO+ "' ");
				


			try {
				poHdrUpt = dhd.updateDOReciptInvoiceHeaderDO(QryUpdateDO.toString(), htDO, "");
			} catch (Exception e) {

		    	}
			}
			

			try {
				if (!poHdrUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
				 }else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Invoice printout edited successfully</font>";

					response
					.sendRedirect("../editsales/invoiceprintout?result="+ result);
				}
			} catch (Exception e) {

			}
		}else if (action.equalsIgnoreCase("EDIT_DO_RCPT_INVOICE_HDR")) {
			DoHdrDAO dhd = new DoHdrDAO();
			boolean poHdrUpt = false;
			String result = "";

			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String sUserId = (String) session.getAttribute("LOGIN_USER");
            String Ordertype = StrUtils.fString(request.getParameter("OrderType"));
			String OutboundOrderHeader = StrUtils.fString(request.getParameter("OutboundOrderHeader"));
			String InvoiceOrderToHeader = StrUtils.fString(request.getParameter("InvoiceOrderToHeader"));
			String FromHeader = StrUtils.fString(request.getParameter("FromHeader"));
			String DateField = StrUtils.fString(request.getParameter("Date"));
			String OrderNo = StrUtils.fString(request.getParameter("OrderNo"));
			String RefNo = StrUtils.fString(request.getParameter("RefNo"));
			String Terms = StrUtils.fString(request.getParameter("Terms"));
			String TermsDetails = StrUtils.fString(request.getParameter("TermsDetails"));
			String SoNo = StrUtils.fString(request.getParameter("SoNo"));
			String Item = StrUtils.fString(request.getParameter("Item"));
			String Description = StrUtils.fString(request.getParameter("Description"));
			String OrderQty = StrUtils.fString(request.getParameter("OrderQty"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			String Rate = StrUtils.fString(request.getParameter("Rate"));
			String TaxAmount = StrUtils.fString(request.getParameter("TaxAmount"));
			String Amt = StrUtils.fString(request.getParameter("Amt"));
			String SubTotal = StrUtils.fString(request.getParameter("SubTotal"));
			String Roundoff = StrUtils.fString(request.getParameter("Roundoff"));
			String TotalTax = StrUtils.fString(request.getParameter("TotalTax"));
			String Total = StrUtils.fString(request.getParameter("Total"));
			String Footer1 = StrUtils.fString(request.getParameter("Footer1"));
			String Footer2 = StrUtils.fString(request.getParameter("Footer2"));
			String Footer3 = StrUtils.fString(request.getParameter("Footer3"));
			String Footer4 = StrUtils.fString(request.getParameter("Footer4"));
			String Footer5 = StrUtils.fString(request.getParameter("Footer5"));
			String Footer6 = StrUtils.fString(request.getParameter("Footer6"));
			String Footer7 = StrUtils.fString(request.getParameter("Footer7"));
			String Footer8 = StrUtils.fString(request.getParameter("Footer8"));
			String Footer9 = StrUtils.fString(request.getParameter("Footer9"));
			String DisplayByOrdertype = (request.getParameter("DisplayByOrdertype") != null) ? "1": "0";
			String printwithbrand = (request.getParameter("printwithbrand") != null) ? "1": "0";
			String printwithproductremarks = (request.getParameter("printwithproductremarks") != null) ? "1": "0";
			String printwithhscode = (request.getParameter("printwithhscode") != null) ? "1": "0";
			String printwithcoo = (request.getParameter("printwithcoo") != null) ? "1": "0";
			String printbuyersign = (request.getParameter("printbuyersign") != null) ? "1": "0";
			String printbuyer = (request.getParameter("printbuyer") != null) ? "1": "0";
			String printDetailDesc = (request.getParameter("printDetailDesc") != null) ? "1": "0";   
		    String printCustTerms = (request.getParameter("printCustTerms") != null) ? "1": "0";
		    String printCustRemarks = (request.getParameter("printCustRemarks") != null) ? "1": "0"; 
		    String printincoterm = (request.getParameter("printincoterm") != null) ? "1": "0"; 
		    
		    String BalanceDue = StrUtils.fString(request.getParameter("BalanceDue"));
		    String PaymentMade = StrUtils.fString(request.getParameter("PaymentMade"));
		    String printPaymentMade = (request.getParameter("printPaymentMade") != null) ? "1": "0"; 
		    String printBalanceDue = (request.getParameter("printBalanceDue") != null) ? "1": "0"; 
		    String printShippingCost = (request.getParameter("printShippingCost") != null) ? "1": "0"; 
		    String printOrderDiscount = (request.getParameter("printOrderDiscount") != null) ? "1": "0"; 
		    String printAdjustment = (request.getParameter("printAdjustment") != null) ? "1": "0"; 
		    
			String Container=StrUtils.fString(request.getParameter("Container"));
		    String DisplayContainer = (request.getParameter("DisplayContainer") != null) ? "1": "0";
		    String remark1 = request.getParameter("REMARK1").trim();
		    String remark2 = request.getParameter("REMARK2").trim();
		    String deliverydate = request.getParameter("DeliveryDate").trim();
		    String employee= request.getParameter("Employee").trim();
		    String shipto = request.getParameter("ShipTo").trim(); 
		    String companydate = request.getParameter("CompanyDate").trim();
		    String companyname = request.getParameter("CompanyName").trim();
		    String companystamp = request.getParameter("CompanyStamp").trim();
		    String companysig = request.getParameter("CompanySig").trim(); 
		    String printEmployee = (request.getParameter("printEmployee") != null) ? "1" : "0";
		    String Orientation = StrUtils.fString(request.getParameter("Orientation"));
			String orderdiscount = request.getParameter("OrderDiscount").trim();
	        String shippingcost = request.getParameter("ShippingCost").trim();
		    String incoterm = request.getParameter("INCOTERM").trim();
		    String brand = request.getParameter("BRAND").trim();
		    String hscode = request.getParameter("HSCODE").trim();
		    String coo = request.getParameter("COO").trim();
		    String rcbno = request.getParameter("RCBNO").trim();
		    //String invoiceno = request.getParameter("INVOICENO").trim();
		    String customerrcbno = request.getParameter("CUSTOMERRCBNO").trim();
		    
		    String uenno = request.getParameter("UENNO").trim();
		    String customeruenno = request.getParameter("CUSTOMERUENNO").trim();
		    
		    String totalafterdiscount = request.getParameter("TOTALAFTERDISCOUNT").trim();
		    String printpackinglistDO = (request.getParameter("printpackinglistDO") != null) ? "1": "0";
			String printdeliverynoteDO = (request.getParameter("printdeliverynoteDO") != null) ? "1": "0";
			String PreparedBy = StrUtils.fString(request.getParameter("PreparedBy"));
			String Seller = StrUtils.fString(request.getParameter("Seller"));
		    String SellerSignature = StrUtils.fString(request.getParameter("SellerSignature"));
		    String Buyer = StrUtils.fString(request.getParameter("Buyer"));
		    String BuyerSignature = StrUtils.fString(request.getParameter("BuyerSignature"));
			String printRoundoffTotalwithDecimal = (request.getParameter("printRoundoffTotalwithDecimal") != null) ? "1": "0";
			String printwithProduct = (request.getParameter("printwithProduct") != null) ? "1": "0";
			String printDiscountReport = (request.getParameter("printDiscountReport") != null) ? "1": "0"; 
		    String Discount = request.getParameter("Discount").trim();
			String NetRate = request.getParameter("NetRate").trim();
			
			String Adjustment = request.getParameter("Adjustment").trim();
			String ProductRatesAre = request.getParameter("ProductRatesAre").trim();
			
			String prdDeliveryDate = request.getParameter("prdDeliveryDate").trim();
			String printWithPrdDeliveryDate = (request.getParameter("printWithPrdDeliveryDate") != null) ? "1": "0";
			String printWithDeliveryDate = (request.getParameter("printWithDeliveryDate") != null) ? "1": "0";
			String showPreviousPurchaseCost = (request.getParameter("showPreviousPurchaseCost") != null) ? "1": "0";
//			String showPreviousSalesCost = (request.getParameter("showPreviousSalesCost") != null) ? "1": "0";
			String showPreviousSalesCost = (request.getParameter("showPreviousSalesCost") ) ;
			String calculateTaxwithShippingCost = (request.getParameter("calculateTaxwithShippingCost") != null) ? "1": "0";
			String printWithUENNO = (request.getParameter("printWithUENNO"));
//			String printWithUENNO = (request.getParameter("printWithUENNO") != null) ? "1": "0";
			String printWithCustomerUENNO = (request.getParameter("printWithCustomerUENNO") != null) ? "1": "0";
			
//		    RESVI STARTS
		    String project = request.getParameter("Project").trim();
		    String printwithproject = (request.getParameter("printwithproject") != null) ? "1": "0";
		    String printwithcompanysig = (request.getParameter("printwithcompanysig") != null) ? "1": "0";
		    String printwithcompanyseal = (request.getParameter("printwithcompanyseal") != null) ? "1": "0";
		    String TransportMode = request.getParameter("TransportMode").trim();
		    String printwithtransportmode = (request.getParameter("printwithtransportmode") != null) ? "1": "0";
		    String printwithshipingadd = (request.getParameter("printwithshipingadd") != null) ? "1": "0";
//		    RESVI ENDS  
		    
		    String replacePreviousSalesCost = (request.getParameter("replacePreviousSalesCost") != null) ? "1": "0";
		    
		    Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IDBConstants.PLANT, PLANT);
		    if (Ordertype.equalsIgnoreCase("Mobile Order")) {
		            ht.put(IDBConstants.ORDERTYPE, Ordertype);
		    } else {
		            ht.put(IDBConstants.ORDERTYPE, Ordertype);
		    }
			StringBuffer QryUpdate = new StringBuffer(" SET ");

			if (OutboundOrderHeader.length() > 0)
				QryUpdate.append(" ORDERHEADER = '" + OutboundOrderHeader
						+ "' ");
			if (InvoiceOrderToHeader.length() > 0)
				QryUpdate.append(", TOHEADER = '" + InvoiceOrderToHeader
						+ "' ");
			if (FromHeader.length() > 0)
				QryUpdate.append(", FROMHEADER = '" + FromHeader + "' ");
			if (DateField.length() > 0)
				QryUpdate.append(", DATE = '" + DateField + "' ");
			if (OrderNo.length() > 0)
				QryUpdate.append(", ORDERNO = '" + OrderNo + "' ");
			if (RefNo.length() > 0)
				QryUpdate.append(", REFNO = '" + RefNo + "' ");
			if (Terms.length() > 0)
				QryUpdate.append(", TERMS = '" + Terms + "' ");
			//if (TermsDetails.length() > 0)
				QryUpdate.append(", TERMSDETAILS = '" + TermsDetails + "' ");
			if (SoNo.length() > 0)
				QryUpdate.append(", SONO = '" + SoNo + "' ");
			if (Item.length() > 0)
				QryUpdate.append(", ITEM = '" + Item + "' ");
			if (Description.length() > 0)
				QryUpdate.append(", DESCRIPTION = '" + Description + "' ");
			if (OrderQty.length() > 0)
				QryUpdate.append(", ORDERQTY = '" + OrderQty + "' ");
			if (UOM.length() > 0)
				QryUpdate.append(", UOM = '" + UOM + "' ");
			if (Rate.length() > 0)
				QryUpdate.append(", RATE = '" + Rate + "' ");
			if (TaxAmount.length() > 0)
				QryUpdate.append(", TAXAMOUNT = '" + TaxAmount + "' ");
			if (Amt.length() > 0)
				QryUpdate.append(", AMT = '" + Amt + "' ");
			if (SubTotal.length() > 0)
				QryUpdate.append(", SubTotal = '" + SubTotal + "' ");
			if (Roundoff.length() > 0)
				QryUpdate.append(", ROUNDOFFTOTALWITHDECIMAL = '" + Roundoff + "' ");
			if (TotalTax.length() > 0)
				QryUpdate.append(", TotalTax = '" + TotalTax + "' ");
			if (Total.length() > 0)
				QryUpdate.append(", Total = '" + Total + "' ");

			QryUpdate.append(", FOOTER1 = '" + Footer1 + "' ");
			QryUpdate.append(", FOOTER2 = '" + Footer2 + "' ");
			QryUpdate.append(", FOOTER3 = '" + Footer3 + "' ");
			QryUpdate.append(", FOOTER4 = '" + Footer4 + "' ");
			QryUpdate.append(", FOOTER5 = '" + Footer5 + "' ");
			QryUpdate.append(", FOOTER6 = '" + Footer6 + "' ");
			QryUpdate.append(", FOOTER7 = '" + Footer7 + "' ");
			QryUpdate.append(", FOOTER8 = '" + Footer8 + "' ");
			QryUpdate.append(", FOOTER9 = '" + Footer9 + "' ");
			QryUpdate.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertype	+ "' ");
			QryUpdate.append(", PRINTWITHBRAND ='" + printwithbrand	+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarks	+ "' ");
			QryUpdate.append(", PRITNWITHHSCODE ='" + printwithhscode	+ "' ");
			QryUpdate.append(", PRITNWITHCOO ='" + printwithcoo	+ "' ");
			QryUpdate.append(", PRINTBUYERSIGN ='" + printbuyersign	+ "' ");
			QryUpdate.append(", PRINTBUYER ='" + printbuyer	+ "' ");
			QryUpdate.append(", CONTAINER ='" + Container+ "' ");
			QryUpdate.append(", DISPLAYCONTAINER ='" + DisplayContainer+ "' ");
		    QryUpdate.append(", PRINTXTRADETAILS ='" + printDetailDesc+ "' ");
		    QryUpdate.append(", PRINTCUSTERMS ='" + printCustTerms+ "' ");
		    QryUpdate.append(", PCUSREMARKS ='" + printCustRemarks+ "' ");
		    QryUpdate.append(", PRINTINCOTERM ='" + printincoterm+ "' ");
		    QryUpdate.append(", PRINTADJUSTMENT ='" + printAdjustment+ "' ");
		    QryUpdate.append(", PRINTORDERDISCOUNT ='" + printOrderDiscount+ "' ");
		    QryUpdate.append(", PRINTSHIPPINGCOST ='" + printShippingCost+ "' ");
		    QryUpdate.append(", PRINTBALANCEDUE ='" + printBalanceDue+ "' ");
		    QryUpdate.append(", PRINTPAYMENTMADE ='" + printPaymentMade+ "' ");
		    QryUpdate.append(", BALANCEDUE ='" + BalanceDue+ "' ");
		    QryUpdate.append(", PAYMENTMADE ='" + PaymentMade+ "' ");
		    QryUpdate.append(", PrintOrientation = '" + Orientation+ "' ");
		    QryUpdate.append(", PRINTDELIVERYNOTE = '" + printdeliverynoteDO+ "' ");
		    QryUpdate.append(", PRINTPACKINGLIST = '" + printpackinglistDO+ "' ");
		    QryUpdate.append(", SHOWPREVIOUSPURCHASECOST = '" + showPreviousPurchaseCost+ "' ");
		    QryUpdate.append(", SHOWPREVIOUSSALESCOST = '" + showPreviousSalesCost+ "' ");
		    QryUpdate.append(", REPLACEPREVIOUSSALESCOST = '" + replacePreviousSalesCost+ "' ");
		    
			QryUpdate.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
			QryUpdate.append(", UPBY = '" + sUserId + "' ");
			if (remark1.length() > 0)
				QryUpdate.append(", REMARK1 = '" + remark1 + "' ");
		    if (remark2.length() > 0)
				QryUpdate.append(", REMARK2 = '" + remark2 + "' ");
		    
		    if (totalafterdiscount.length() > 0)
				QryUpdate.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscount + "' ");
		    
		    if (rcbno.length() > 0)
				QryUpdate.append(", RCBNO = '" + rcbno + "' ");
		    
		  //  if (invoiceno.length() > 0)
			//	QryUpdate.append(", INVOICENO = '" + invoiceno + "' ");
		 
		    if (customerrcbno.length() > 0)
				QryUpdate.append(", CUSTOMERRCBNO = '" + customerrcbno + "' ");
		    
		    if (uenno.length() > 0)
		    	QryUpdate.append(", UENNO = '" + uenno + "' ");
		    
		    if (customeruenno.length() > 0)
				QryUpdate.append(", CUSTOMERUENNO = '" + customeruenno + "' ");
		    
		    if (brand.length() > 0)
				QryUpdate.append(", BRAND = '" + brand + "' ");
		    
		    if (hscode.length() > 0)
				QryUpdate.append(", HSCODE = '" + hscode + "' ");
		    if (coo.length() > 0)
				QryUpdate.append(", COO = '" + coo + "' ");
		    
		    if (deliverydate.length() > 0)
				QryUpdate.append(", DELIVERYDATE = '" + deliverydate + "' ");	
		    if (employee.length() > 0)
				QryUpdate.append(", EMPLOYEE = '" + employee + "' ");		
			if (shipto.length() > 0)
				QryUpdate.append(", SHIPTO = '" + shipto + "' ");	
			if (companydate.length() > 0)
				QryUpdate.append(", COMPANYDATE = '" + companydate + "' ");
			if (companyname.length() > 0)
				QryUpdate.append(", COMPANYNAME = '" + companyname + "' ");
			if (companystamp.length() > 0)
				QryUpdate.append(", COMPANYSTAMP = '" + companystamp + "' ");
			if (companysig.length() > 0)
				QryUpdate.append(", COMPANYSIG = '" + companysig + "' ");
			if (printEmployee.length() > 0)
				QryUpdate.append(", PRINTEMPLOYEE = '" + printEmployee + "' ");
			if (orderdiscount.length() > 0)
				QryUpdate.append(", ORDERDISCOUNT = '" + orderdiscount + "' ");
			if (shippingcost.length() > 0)
				QryUpdate.append(", SHIPPINGCOST = '" + shippingcost + "' ");
			if (incoterm.length() > 0)
				QryUpdate.append(", INCOTERM = '" + incoterm + "' ");
			if (PreparedBy.length() > 0)
				QryUpdate.append(", PREPAREDBY = '" + PreparedBy + "' ");
			if (Seller.length() > 0)
				QryUpdate.append(", SELLER = '" + Seller + "' ");
			if (SellerSignature.length() > 0)
				QryUpdate.append(", SELLERSIGNATURE = '" + SellerSignature + "' ");
			if (Buyer.length() > 0)
				QryUpdate.append(", BUYER = '" + Buyer + "' ");
			if (BuyerSignature.length() > 0)
				QryUpdate.append(", BUYERSIGNATURE = '" + BuyerSignature + "' ");
				
			QryUpdate.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimal+ "' ");
			QryUpdate.append(", PRINTWITHPRODUCT ='" + printwithProduct+ "' ");
			QryUpdate.append(", PRINTWITHDISCOUNT ='" + printDiscountReport+ "' ");
			
			QryUpdate.append(", ADJUSTMENT ='" + Adjustment+ "' ");
			QryUpdate.append(", PRODUCTRATESARE ='" + ProductRatesAre+ "' ");
			
			if (Discount.length() > 0)
				QryUpdate.append(", DISCOUNT = '" + Discount + "' ");
			if (NetRate.length() > 0)
				QryUpdate.append(", NETRATE = '" + NetRate + "' ");
			
			if (prdDeliveryDate.length() > 0)
				QryUpdate.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDate + "' ");	
				QryUpdate.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDate+ "' ");
				QryUpdate.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDate+ "' ");
				QryUpdate.append(", CALCULATETAXWITHSHIPPINGCOST ='" + calculateTaxwithShippingCost+ "' ");
				
//			RESVI STARTS
				if (project.length() > 0)
					QryUpdate.append(", PROJECT = '" + project + "' ");	

				if (TransportMode.length() > 0)
					QryUpdate.append(", TRANSPORT_MODE = '" + TransportMode + "' ");	
				
				QryUpdate.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmode+ "' ");
				QryUpdate.append(", PRINTWITHSHIPINGADD ='" + printwithshipingadd+ "' ");
				QryUpdate.append(", PRINTWITHPROJECT ='" + printwithproject+ "' ");
				QryUpdate.append(", PRINTWITHUENNO ='" + printWithUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNO+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSIG='" + printwithcompanysig+ "' ");
				QryUpdate.append(", PRINTWITHCOMPANYSEAL='" + printwithcompanyseal+ "' ");
//             RESVI ENDS

			try {
				poHdrUpt = dhd.updateDOReciptInvoiceHeader(QryUpdate
						.toString(), ht, "");
			} catch (Exception e) {

			}
							
			try {
				if (!poHdrUpt) {

					result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Failed to edit the details</font>";
				    if (Ordertype.equalsIgnoreCase("Mobile Order")) {
                                            response
                                            .sendRedirect("jsp/editMobileOrderInvoice.jsp?result="
                                                            + result);
                                        }else{
					response
					.sendRedirect("../editsales/salesorderprintoutwithprice?result="
							+ result);
                                        }
				} 
                                        if (Ordertype.equalsIgnoreCase("Mobile Order")) {
                                                result = "<font class = "
                                                        + IConstants.SUCCESS_COLOR
                                                        + ">Mobile Order Printout edited successfully</font>";
                                                response
                                                .sendRedirect("jsp/editMobileOrderInvoice.jsp?result="
                                                                + result);
                                        }else {
					result = "<font class = "
						+ IConstants.SUCCESS_COLOR
						+ ">Sales Order With Price Printout edited successfully</font>";

					response
					.sendRedirect("../editsales/salesorderprintoutwithprice?result="
							+ result);
				}
			} catch (Exception e) {

			}
		}else if(action.equalsIgnoreCase("EDIT_DO_RCPT_INVOICE_HDRDO")){
			//To update DO
			
			DoHdrDAO dhd = new DoHdrDAO();
			boolean poHdrUpt = false;
			String result = "";
			String Ordertype = "";
					
			HttpSession session = request.getSession();
			String PLANT = (String) session.getAttribute("PLANT");
			String sUserId = (String) session.getAttribute("LOGIN_USER");
		String Ordertypedo = StrUtils.fString(request.getParameter("OrderTypeDO"));
		String OutboundOrderHeaderDO = StrUtils.fString(request.getParameter("OutboundOrderHeaderDO"));
		String InvoiceOrderToHeaderDO = StrUtils.fString(request.getParameter("InvoiceOrderToHeaderDO"));
		String FromHeaderDO = StrUtils.fString(request.getParameter("FromHeaderDO"));
		String DateFieldDO = StrUtils.fString(request.getParameter("DateDO"));
		String OrderNoDO = StrUtils.fString(request.getParameter("OrderNoDO"));
		String RefNoDO = StrUtils.fString(request.getParameter("RefNoDO"));
		String TermsDO = StrUtils.fString(request.getParameter("TermsDO"));
		String TermsDetailsDO = StrUtils.fString(request.getParameter("TermsDetailsDO"));
		String SoNoDO = StrUtils.fString(request.getParameter("SoNoDO"));
		String ItemDO = StrUtils.fString(request.getParameter("ItemDO"));
		String DescriptionDO = StrUtils.fString(request.getParameter("DescriptionDO"));
		String OrderQtyDO = StrUtils.fString(request.getParameter("OrderQtyDO"));
		String UOMDO = StrUtils.fString(request.getParameter("UOMDO"));
		String RateDO = StrUtils.fString(request.getParameter("RateDO"));
		String TaxAmountDO = StrUtils.fString(request.getParameter("TaxAmountDO"));
		String AmtDO = StrUtils.fString(request.getParameter("AmtDO"));
		String SubTotalDO = StrUtils.fString(request.getParameter("SubTotalDO"));
		String TotalTaxDO = StrUtils.fString(request.getParameter("TotalTaxDO"));
		String TotalDO = StrUtils.fString(request.getParameter("TotalDO"));
		String printdeliverynoteDO = (request.getParameter("printdeliverynoteDO") != null) ? "1": "0";
		String printpackinglistDO = (request.getParameter("printpackinglistDO") != null) ? "1": "0";
		String Footer1DO = StrUtils.fString(request.getParameter("Footer1DO"));
		String Footer2DO = StrUtils.fString(request.getParameter("Footer2DO"));
		String Footer3DO = StrUtils.fString(request.getParameter("Footer3DO"));
		String Footer4DO = StrUtils.fString(request.getParameter("Footer4DO"));
		String Footer5DO = StrUtils.fString(request.getParameter("Footer5DO"));
		String Footer6DO = StrUtils.fString(request.getParameter("Footer6DO"));
		String Footer7DO = StrUtils.fString(request.getParameter("Footer7DO"));
		String Footer8DO = StrUtils.fString(request.getParameter("Footer8DO"));
		String Footer9DO = StrUtils.fString(request.getParameter("Footer9DO"));
		String DisplayByOrdertypeDO = (request.getParameter("DisplayByOrdertypeDO") != null) ? "1" : "0";
		String printwithbrandDO = (request.getParameter("printwithbrandDO") != null) ? "1" : "0";
		String printwithproductremarksDO = (request.getParameter("printwithproductremarksDO") != null) ? "1" : "0";
		String printwithhscodeDO = (request.getParameter("printwithhscodeDO") != null) ? "1" : "0";
		String printwithcooDO = (request.getParameter("printwithcooDO") != null) ? "1" : "0";
	    String printDetailDescDO = (request.getParameter("printDetailDescDO") != null) ? "1": "0";   
	    String printCustTermsDO = (request.getParameter("printCustTermsDO") != null) ? "1": "0";
	    String printCustRemarksDO = (request.getParameter("printCustRemarksDO") != null) ? "1": "0"; 
	    String printincotermDO = (request.getParameter("printincotermDO") != null) ? "1": "0"; 
		String ContainerDO=StrUtils.fString(request.getParameter("ContainerDO"));
	    String DisplayContainerDO = (request.getParameter("DisplayContainerDO") != null) ? "1": "0";
	    String remark1DO = request.getParameter("REMARK1DO").trim();
	    String remark2DO = request.getParameter("REMARK2DO").trim();
	    String deliverydateDO = request.getParameter("DeliveryDateDO").trim();
	    String employeeDO= request.getParameter("EmployeeDO").trim();
	    String shiptoDO = request.getParameter("ShipToDO").trim(); 
	    String companydateDO = request.getParameter("CompanyDateDO").trim();
	    String companynameDO = request.getParameter("CompanyNameDO").trim();
	    String companystampDO = request.getParameter("CompanyStampDO").trim();
	    String companysigDO = request.getParameter("CompanySigDO").trim(); 
	    String printEmployeeDO = (request.getParameter("printEmployeeDO") != null) ? "1" : "0";
	    String OrientationDO = StrUtils.fString(request.getParameter("OrientationDO"));
	    String orderdiscountDO = request.getParameter("OrderDiscountDO").trim();
        String shippingcostDO = request.getParameter("ShippingCostDO").trim();
        String RoundoffDO = request.getParameter("RoundoffDO").trim();
	    String incotermDO = request.getParameter("INCOTERMDO").trim();
	    String brandDO = request.getParameter("BRANDDO").trim();
	    String hscodeDO = request.getParameter("HSCODEDO").trim();
	    String cooDO = request.getParameter("COODO").trim();
	    String rcbnoDO = request.getParameter("RCBNODO").trim();
	    String invoicenoDO = request.getParameter("INVOICENODO").trim();
	    String invoicedateDO = request.getParameter("INVOICEDATEDO").trim();
	    String customerrcbnoDO = request.getParameter("CUSTOMERRCBNODO").trim();
	    
	    String uennoDO = request.getParameter("UENNODO").trim();
	    String customeruennoDO = request.getParameter("CUSTOMERUENNODO").trim();
	    
	    String totalafterdiscountDO = request.getParameter("TOTALAFTERDISCOUNTDO").trim();
	    String PreparedByDO = StrUtils.fString(request.getParameter("PreparedByDO"));
	    String SellerDO = StrUtils.fString(request.getParameter("SellerDO"));
	    String SellerSignatureDO = StrUtils.fString(request.getParameter("SellerSignatureDO"));
	    String BuyerDO = StrUtils.fString(request.getParameter("BuyerDO"));
	    String BuyerSignatureDO = StrUtils.fString(request.getParameter("BuyerSignatureDO"));
		String printRoundoffTotalwithDecimalDO = (request.getParameter("printRoundoffTotalwithDecimalDO") != null) ? "1": "0";
		String printwithProductDO = (request.getParameter("printwithProductDO") != null) ? "1": "0";
		String printDiscountReportDO = (request.getParameter("printDiscountReportDO") != null) ? "1": "0"; 
	    String DiscountDO = request.getParameter("DiscountDO").trim();
		String NetRateDO = request.getParameter("NetRateDO").trim();
		
		String BalanceDueDO = StrUtils.fString(request.getParameter("BalanceDueDO"));
	    String PaymentMadeDO = StrUtils.fString(request.getParameter("PaymentMadeDO"));
	    String printPaymentMadeDO = (request.getParameter("printPaymentMadeDO") != null) ? "1": "0"; 
	    String printBalanceDueDO = (request.getParameter("printBalanceDueDO") != null) ? "1": "0"; 
	    String printShippingCostDO = (request.getParameter("printShippingCostDO") != null) ? "1": "0"; 
	    String printOrderDiscountDO = (request.getParameter("printOrderDiscountDO") != null) ? "1": "0"; 
	    String printAdjustmentDO = (request.getParameter("printAdjustmentDO") != null) ? "1": "0"; 
		
		String AdjustmentDO = request.getParameter("AdjustmentDO").trim();
		String ProductRatesAreDO = request.getParameter("ProductRatesAreDO").trim();
		
		String prdDeliveryDateDO = request.getParameter("prdDeliveryDateDO").trim();
		String printWithPrdDeliveryDateDO = (request.getParameter("printWithPrdDeliveryDateDO") != null) ? "1": "0";
		String printWithDeliveryDateDO = (request.getParameter("printWithDeliveryDateDO") != null) ? "1": "0";
		String calculateTaxwithShippingCostDO = (request.getParameter("calculateTaxwithShippingCostDO") != null) ? "1": "0";
		String GINODO = request.getParameter("GINODO").trim();
		String GINODateDO = request.getParameter("GINODATEDO").trim();
		
	    
//	    RESVI STARTS
	    String projectDO = request.getParameter("ProjectDO").trim();
	    String printwithprojectDO = (request.getParameter("printwithprojectDO") != null) ? "1": "0";
	    String printWithUENNODO = (request.getParameter("printWithUENNODO"));
//	    String printWithUENNODO = (request.getParameter("printWithUENNODO") != null) ? "1": "0";
	    String printWithCustomerUENNODO = (request.getParameter("printWithCustomerUENNODO") != null) ? "1": "0";
	    String DisplaySignature = (request.getParameter("DisplaySignature") != null) ? "1": "0";
	    String printwithcompanysealDO = (request.getParameter("printwithcompanysealDO") != null) ? "1": "0";
	    String TransportModeDO = request.getParameter("TransportModeDO").trim();
	    String printwithtransportmodeDO = (request.getParameter("printwithtransportmodeDO") != null) ? "1": "0";
	    String printwithshipingaddDO = (request.getParameter("printwithshipingaddDO") != null) ? "1": "0";
//	    RESVI ENDS   
	    
	    
	    Hashtable<String, String> htDO = new Hashtable<String, String>();
		htDO.put(IDBConstants.PLANT, PLANT);
	    if (Ordertype.equalsIgnoreCase("Mobile Order")) {
	            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
	    } else {
	            htDO.put(IDBConstants.ORDERTYPE, Ordertypedo);
	    }
		StringBuffer QryUpdateDO = new StringBuffer(" SET ");

		if (OutboundOrderHeaderDO.length() > 0)
			QryUpdateDO.append(" ORDERHEADER = '" + OutboundOrderHeaderDO
					+ "' ");
		if (InvoiceOrderToHeaderDO.length() > 0)
			QryUpdateDO.append(", TOHEADER = '" + InvoiceOrderToHeaderDO
					+ "' ");
		if (FromHeaderDO.length() > 0)
			QryUpdateDO.append(", FROMHEADER = '" + FromHeaderDO + "' ");
		if (DateFieldDO.length() > 0)
			QryUpdateDO.append(", DATE = '" + DateFieldDO + "' ");
		if (OrderNoDO.length() > 0)
			QryUpdateDO.append(", ORDERNO = '" + OrderNoDO + "' ");
		if (RefNoDO.length() > 0)
			QryUpdateDO.append(", REFNO = '" + RefNoDO + "' ");
		if (TermsDO.length() > 0)
			QryUpdateDO.append(", TERMS = '" + TermsDO + "' ");
		//if (TermsDetails.length() > 0)
			QryUpdateDO.append(", TERMSDETAILS = '" + TermsDetailsDO + "' ");
		if (SoNoDO.length() > 0)
			QryUpdateDO.append(", SONO = '" + SoNoDO + "' ");
		if (ItemDO.length() > 0)
			QryUpdateDO.append(", ITEM = '" + ItemDO + "' ");
		if (DescriptionDO.length() > 0)
			QryUpdateDO.append(", DESCRIPTION = '" + DescriptionDO + "' ");
		if (OrderQtyDO.length() > 0)
			QryUpdateDO.append(", ORDERQTY = '" + OrderQtyDO + "' ");
		if (UOMDO.length() > 0)
			QryUpdateDO.append(", UOM = '" + UOMDO + "' ");
		if (RateDO.length() > 0)
			QryUpdateDO.append(", RATE = '" + RateDO + "' ");
		if (TaxAmountDO.length() > 0)
			QryUpdateDO.append(", TAXAMOUNT = '" + TaxAmountDO + "' ");
		if (AmtDO.length() > 0)
			QryUpdateDO.append(", AMT = '" + AmtDO + "' ");
		if (SubTotalDO.length() > 0)
			QryUpdateDO.append(", SubTotal = '" + SubTotalDO + "' ");
		if (TotalTaxDO.length() > 0)
			QryUpdateDO.append(", TotalTax = '" + TotalTaxDO + "' ");
		if (TotalDO.length() > 0)
			QryUpdateDO.append(", Total = '" + TotalDO + "' ");

		QryUpdateDO.append(", FOOTER1 = '" + Footer1DO + "' ");
		QryUpdateDO.append(", FOOTER2 = '" + Footer2DO + "' ");
		QryUpdateDO.append(", FOOTER3 = '" + Footer3DO + "' ");
		QryUpdateDO.append(", FOOTER4 = '" + Footer4DO + "' ");
		QryUpdateDO.append(", FOOTER5 = '" + Footer5DO + "' ");
		QryUpdateDO.append(", FOOTER6 = '" + Footer6DO + "' ");
		QryUpdateDO.append(", FOOTER7 = '" + Footer7DO + "' ");
		QryUpdateDO.append(", FOOTER8 = '" + Footer8DO + "' ");
		QryUpdateDO.append(", FOOTER9 = '" + Footer9DO + "' ");
		QryUpdateDO.append(", DISPLAYBYORDERTYPE ='" + DisplayByOrdertypeDO	+ "' ");
		QryUpdateDO.append(", PRINTWITHBRAND ='" + printwithbrandDO	+ "' ");
		QryUpdateDO.append(", PRINTWITHPRODUCTREMARKS ='" + printwithproductremarksDO	+ "' ");
		QryUpdateDO.append(", PRITNWITHHSCODE ='" + printwithhscodeDO	+ "' ");
		QryUpdateDO.append(", PRITNWITHCOO ='" + printwithcooDO	+ "' ");
		QryUpdateDO.append(", CONTAINER ='" + ContainerDO+ "' ");
		QryUpdateDO.append(", DISPLAYCONTAINER ='" + DisplayContainerDO+ "' ");
	    QryUpdateDO.append(", PRINTXTRADETAILS ='" + printDetailDescDO+ "' ");
	    QryUpdateDO.append(", PRINTCUSTERMS ='" + printCustTermsDO+ "' ");
	    QryUpdateDO.append(", PCUSREMARKS ='" + printCustRemarksDO+ "' ");
	    QryUpdateDO.append(", PRINTINCOTERM ='" + printincotermDO+ "' ");
	    QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO+ "' ");
	    QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO+ "' ");
	   // QryUpdateDO.append(", PRINTDELIVERYNOTE ='" + printdeliverynoteDO	+ "' ");
		//QryUpdateDO.append(", PRINTPACKINGLIST ='" + printpackinglistDO	+ "' ");
	    
		QryUpdateDO.append(", UPAT = '" + DateUtils.getDateTime() + "' ");
		QryUpdateDO.append(", UPBY = '" + sUserId + "' ");
		QryUpdateDO.append(", PrintOrientation = '" + OrientationDO+ "' ");
		if (remark1DO.length() > 0)
			QryUpdateDO.append(", REMARK1 = '" + remark1DO + "' ");
	    if (remark2DO.length() > 0)
			QryUpdateDO.append(", REMARK2 = '" + remark2DO + "' ");
	    
	    if (totalafterdiscountDO.length() > 0)
			QryUpdateDO.append(", TOTALAFTERDISCOUNT = '" + totalafterdiscountDO + "' ");
	    
	    if (rcbnoDO.length() > 0)
			QryUpdateDO.append(", RCBNO = '" + rcbnoDO + "' ");
	    
	    if (brandDO.length() > 0)
			QryUpdateDO.append(", BRAND = '" + brandDO + "' ");
	    
	    if (hscodeDO.length() > 0)
			QryUpdateDO.append(", HSCODE = '" + hscodeDO + "' ");
	    if (cooDO.length() > 0)
			QryUpdateDO.append(", COO = '" + cooDO + "' ");
	    
	    if (customerrcbnoDO.length() > 0)
			QryUpdateDO.append(", CUSTOMERRCBNO = '" + customerrcbnoDO + "' ");
	    
	    if (uennoDO.length() > 0)
	    	QryUpdateDO.append(", UENNO = '" + uennoDO + "' ");
	    
	    if (customeruennoDO.length() > 0)
			QryUpdateDO.append(", CUSTOMERUENNO = '" + customeruennoDO + "' ");
	    
	    if (invoicenoDO.length() > 0)
			QryUpdateDO.append(", INVOICENO = '" + invoicenoDO + "' ");
	    
	    if (invoicedateDO.length() > 0)
			QryUpdateDO.append(", INVOICEDATE = '" + invoicedateDO + "' ");
	    
	    if (deliverydateDO.length() > 0)
			QryUpdateDO.append(", DELIVERYDATE = '" + deliverydateDO + "' ");	
	    if (employeeDO.length() > 0)
			QryUpdateDO.append(", EMPLOYEE = '" + employeeDO + "' ");		
		if (shiptoDO.length() > 0)
			QryUpdateDO.append(", SHIPTO = '" + shiptoDO + "' ");	
		if (companydateDO.length() > 0)
			QryUpdateDO.append(", COMPANYDATE = '" + companydateDO + "' ");
		if (companynameDO.length() > 0)
			QryUpdateDO.append(", COMPANYNAME = '" + companynameDO + "' ");
		if (companystampDO.length() > 0)
			QryUpdateDO.append(", COMPANYSTAMP = '" + companystampDO + "' ");
		if (companysigDO.length() > 0)
			QryUpdateDO.append(", COMPANYSIG = '" + companysigDO + "' ");
		if (printEmployeeDO.length() > 0)
			QryUpdateDO.append(", PRINTEMPLOYEE = '" + printEmployeeDO + "' ");
		
		if (orderdiscountDO.length() > 0)
			QryUpdateDO.append(", ORDERDISCOUNT = '" + orderdiscountDO + "' ");
		if (shippingcostDO.length() > 0)
			QryUpdateDO.append(", SHIPPINGCOST = '" + shippingcostDO + "' ");
		if (RoundoffDO.length() > 0)
			QryUpdateDO.append(", ROUNDOFFTOTALWITHDECIMAL = '" + RoundoffDO + "' ");
		if (incotermDO.length() > 0)
			QryUpdateDO.append(", INCOTERM = '" + incotermDO + "' ");
		if (PreparedByDO.length() > 0)
			QryUpdateDO.append(", PREPAREDBY = '" + PreparedByDO + "' ");
		if (SellerDO.length() > 0)
			QryUpdateDO.append(", SELLER = '" + SellerDO + "' ");
		if (SellerSignatureDO.length() > 0)
			QryUpdateDO.append(", SELLERSIGNATURE = '" + SellerSignatureDO + "' ");
		if (BuyerDO.length() > 0)
			QryUpdateDO.append(", BUYER = '" + BuyerDO + "' ");
		if (BuyerSignatureDO.length() > 0)
			QryUpdateDO.append(", BUYERSIGNATURE = '" + BuyerSignatureDO + "' ");
			
		QryUpdateDO.append(", PRINTROUNDOFFTOTALWITHDECIMAL ='" + printRoundoffTotalwithDecimalDO+ "' ");
		QryUpdateDO.append(", PRINTWITHPRODUCT ='" + printwithProductDO+ "' ");
		QryUpdateDO.append(", PRINTWITHDISCOUNT ='" + printDiscountReportDO+ "' ");
		
		QryUpdateDO.append(", ADJUSTMENT ='" + AdjustmentDO+ "' ");
		QryUpdateDO.append(", PRODUCTRATESARE ='" + ProductRatesAreDO+ "' ");
		
	    QryUpdateDO.append(", PRINTADJUSTMENT ='" + printAdjustmentDO+ "' ");
	    QryUpdateDO.append(", PRINTORDERDISCOUNT ='" + printOrderDiscountDO+ "' ");
	    QryUpdateDO.append(", PRINTSHIPPINGCOST ='" + printShippingCostDO+ "' ");
	    QryUpdateDO.append(", PRINTBALANCEDUE ='" + printBalanceDueDO+ "' ");
	    QryUpdateDO.append(", PRINTPAYMENTMADE ='" + printPaymentMadeDO+ "' ");
	    QryUpdateDO.append(", BALANCEDUE ='" + BalanceDueDO+ "' ");
	    QryUpdateDO.append(", PAYMENTMADE ='" + PaymentMadeDO+ "' ");
		
		if (DiscountDO.length() > 0)
			QryUpdateDO.append(", DISCOUNT = '" + DiscountDO + "' ");
		if (NetRateDO.length() > 0)
			QryUpdateDO.append(", NETRATE = '" + NetRateDO + "' ");
		
		if (prdDeliveryDateDO.length() > 0)
			QryUpdateDO.append(", PRODUCTDELIVERYDATE = '" + prdDeliveryDateDO + "' ");	
			QryUpdateDO.append(", PRINTWITHPRODUCTDELIVERYDATE ='" + printWithPrdDeliveryDateDO+ "' ");
			QryUpdateDO.append(", PRINTWITHDELIVERYDATE ='" + printWithDeliveryDateDO+ "' ");
			QryUpdateDO.append(", CALCULATETAXWITHSHIPPINGCOST ='" + calculateTaxwithShippingCostDO+ "' ");
			QryUpdateDO.append(", GINO ='" + GINODO+ "' ");
			QryUpdateDO.append(", GINODATE ='" + GINODateDO+ "' ");
			
//			RESVI STARTS
			if (projectDO.length() > 0)
				QryUpdateDO.append(", PROJECT = '" + projectDO + "' ");	
			

			if (TransportModeDO.length() > 0)
				QryUpdateDO.append(", TRANSPORT_MODE = '" + TransportModeDO + "' ");	
			
			QryUpdateDO.append(", PRINTWITHTRANSPORT_MODE ='" + printwithtransportmodeDO+ "' ");
			QryUpdateDO.append(", PRINTWITHSHIPINGADD ='" + printwithshipingaddDO+ "' ");
			QryUpdateDO.append(", PRINTWITHPROJECT ='" + printwithprojectDO+ "' ");
			QryUpdateDO.append(", PRINTWITHUENNO ='" + printWithUENNODO+ "' ");
			QryUpdateDO.append(", PRINTWITHCUSTOMERUENNO ='" + printWithCustomerUENNODO+ "' ");
			QryUpdateDO.append(", PRINTWITHCOMPANYSEAL ='" + printwithcompanysealDO+ "' ");
			QryUpdateDO.append(", DISPLAYSIGNATURE = '" + DisplaySignature + "' ");
//         RESVI ENDS

		try {
			poHdrUpt = dhd.updateDOReciptInvoiceHeaderDO(QryUpdateDO.toString(), htDO, "");
		} catch (Exception e) {

	    	}
		try {
			if (!poHdrUpt) {

				result = "<font class = " + IConstants.FAILED_COLOR
				+ ">Failed to edit the details</font>";
			    if (Ordertype.equalsIgnoreCase("Mobile Order")) {
                                        response
                                        .sendRedirect("jsp/editMobileOrderInvoice.jsp?result="
                                                        + result);
                                    }else{
				response
				.sendRedirect("../editsales/salesorderprintoutwithprice?result="
						+ result);
                                    }
			} 
                                    if (Ordertype.equalsIgnoreCase("Mobile Order")) {
                                            result = "<font class = "
                                                    + IConstants.SUCCESS_COLOR
                                                    + ">Mobile Order Printout edited successfully</font>";
                                            response
                                            .sendRedirect("jsp/editMobileOrderInvoice.jsp?result="
                                                            + result);
                                    }else {
				result = "<font class = "
					+ IConstants.SUCCESS_COLOR
					+ ">Sales Order With Price Printout edited successfully</font>";

				response
				.sendRedirect("../editsales/salesorderprintoutwithprice?result="
						+ result);
			}
		} catch (Exception e) {

		}
		
	}
		if (baction.equals("VIEW_INVOICE_SUMMARY_DASHBOARD_VIEW")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();  
	        jsonObjectResult = this.getinvoicedashboardview(request,plant);
	      //this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}
		//Author: Azees  Create date: August 08,2021  Description: Show only order no.
		if (action.equalsIgnoreCase("GET_ORDER_NO_REVERSE_AUTOSUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestionRevers(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_FORCE_CLOSE")) {			
			jsonObjectResult = this.getOrderNoForForceClose(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} 
		
		
		if (action.equalsIgnoreCase("GET_ORDER_TYPE_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderTypeForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} 
		else if (action.equalsIgnoreCase("GET_ORDER_DETAILS_FOR_INVOICE")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getOrderDetailsForInvoice(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION_DNPL")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestiondnpl(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();    
		}else if (action.equalsIgnoreCase("GET_INVOICE_DETAILS_FOR_CUSTOMER")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceNoforCreditNotr(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if (action.equalsIgnoreCase("GET_PACKING_LIST_DETAILS_FOR_ORDERNO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getPLNoforDono(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if (action.equalsIgnoreCase("GET_PACKING_LIST_DETAILS_FOR_INVOICE")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getPLNoforInvoice(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();    
		}else if (action.equalsIgnoreCase("GET_INVOICE_DETAILS_FOR_ORDERNO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceNoforDono(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_ORDER_DETAILS_FOR_INVOICE_USING_INVOICENO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getOrderDetailsForInvoiceUsingInvno(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_SALES_ORDER_DETAILS_FOR_INVOICE_USING_INVOICENO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getSalesOrderDetailsForInvoiceUsingInvno(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}
		else if (action.equalsIgnoreCase("GET_ORDER_DETAILS_USING_INVOICENO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getOrderDetailsUsingInvno(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_EDIT_INVOICE_DETAILS")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getEditInvoiceDetails(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (baction.equalsIgnoreCase("VIEW_INVOICE_AGING_SUMMARY_VIEW")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getBillAgingView(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (baction.equalsIgnoreCase("GET_INVOICE_DETAILS_USING_INVOICENO")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceDetailsUsingInvno(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} else if (baction.equals("VIEW_GINOTOINVOICE_SUMMARY")) {
			jsonObjectResult = new JSONObject();  
	        jsonObjectResult = this.getginotoinvoiceview(request);	      
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (baction.equalsIgnoreCase("CONVERT_INVOICE_DRAFT_TO_OPEN")) {

			
			  InvoiceUtil invoiceUtil = new InvoiceUtil(); 
//			  DateUtils dateutils = new DateUtils(); 
			  InvoiceDAO invoicedao = new InvoiceDAO();
			  UserTransaction ut = null;
			  
			  String invoiceid = StrUtils.fString(request.getParameter("INVID")).trim(); 
			  String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			  String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim(); 
			  Hashtable ht = new Hashtable(); 
			  ht.put("ID", invoiceid); ht.put("PLANT", plant); 
			  try {
				
				  List invoiceHdrList = invoiceUtil.getInvoiceHdrById(ht); 
				  Map invoiceHdrmap=(Map)invoiceHdrList.get(0);
				  
				  Hashtable htDet = new Hashtable(); 
				  htDet.put("INVOICEHDRID", invoiceid); 
				  htDet.put("PLANT", plant); 
				  List<Hashtable<String,String>> invoiceDetInfoList=invoiceUtil.getInvoiceDetByHdrId(htDet);
				  
				/*
				 * Hashtable invoiceHdr =new Hashtable(); invoiceHdr.put("PLANT", plant);
				 * invoiceHdr.put("CUSTNO", invoiceHdrmap.get("CUSTNO"));
				 * invoiceHdr.put("INVOICE", invoiceHdrmap.get("INVOICE"));
				 * invoiceHdr.put("DONO", invoiceHdrmap.get("DONO"));
				 * invoiceHdr.put("INVOICE_DATE", invoiceHdrmap.get("INVOICE_DATE"));
				 * invoiceHdr.put("DUE_DATE", invoiceHdrmap.get("DUE_DATE"));
				 * invoiceHdr.put("PAYMENT_TERMS", invoiceHdrmap.get("PAYMENT_TERMS"));
				 * invoiceHdr.put("EMPNO", invoiceHdrmap.get("EMPNO"));
				 * invoiceHdr.put("ITEM_RATES", invoiceHdrmap.get("ITEM_RATES"));
				 * invoiceHdr.put("DISCOUNT", invoiceHdrmap.get("DISCOUNT"));
				 * invoiceHdr.put("DISCOUNT_TYPE", invoiceHdrmap.get("DISCOUNT_TYPE"));
				 * invoiceHdr.put("DISCOUNT_ACCOUNT", invoiceHdrmap.get("DISCOUNT_ACCOUNT"));
				 * invoiceHdr.put("SHIPPINGCOST", invoiceHdrmap.get("SHIPPINGCOST"));
				 * invoiceHdr.put("ADJUSTMENT", invoiceHdrmap.get("ADJUSTMENT"));
				 * invoiceHdr.put("SUB_TOTAL", invoiceHdrmap.get("SUB_TOTAL"));
				 * invoiceHdr.put("TOTAL_AMOUNT", invoiceHdrmap.get("TOTAL_AMOUNT"));
				 * invoiceHdr.put("BILL_STATUS", "Open");
				 * invoiceHdr.put("NOTE",invoiceHdrmap.get("NOTE"));
				 * invoiceHdr.put("TERMSCONDITIONS", invoiceHdrmap.get("TERMSCONDITIONS"));
				 * invoiceHdr.put("CRAT",invoiceHdrmap.get("CRAT"));
				 * invoiceHdr.put("CRBY",invoiceHdrmap.get("CRBY"));
				 * invoiceHdr.put("UPAT",DateUtils.getDateTime());
				 * invoiceHdr.put("ID",invoiceid); invoiceHdr.put("UPBY",username);
				 */
				 
			  ut =DbBean.getUserTranaction(); 
			  ut.begin(); 
			  	Hashtable htgi = new Hashtable();	
				String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Open',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoiceid+"'";
				invoicedao.updategino(sqlgi, htgi, "");
				
			  /*int invoiceHdrId=0;
			  invoiceHdrId=invoiceUtil.updateInvoiceHdr(invoiceHdr); 
			  if(invoiceHdrId > 0) {*/
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				//Journal Entry
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(invoiceHdrmap.get("INVOICE_DATE").toString());
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("INVOICE");
				journalHead.setTRANSACTION_ID(invoiceHdrmap.get("INVOICE").toString());
				journalHead.setSUB_TOTAL(Double.parseDouble(invoiceHdrmap.get("SUB_TOTAL").toString()));
				//journalHead.setTOTAL_AMOUNT(Double.parseDouble(invoiceHdrmap.get("TOTAL_AMOUNT").toString()));
				journalHead.setCRAT(DateUtils.getDateTime());
				journalHead.setCRBY(username);
				
				List<JournalDetail> journalDetails=new ArrayList<>();
				CoaDAO coaDAO=new CoaDAO();
				CustMstDAO cusDAO=new CustMstDAO();
				ItemMstDAO itemMstDAO=new ItemMstDAO();
				Double totalItemNetWeight=0.00;
				Double totalCostofGoodSold=0.00;

				for(Map invDet:invoiceDetInfoList)
				{
					Double quantity=Double.parseDouble(invDet.get("QTY").toString());
					String netWeight=itemMstDAO.getItemNetWeight(plant, invDet.get("ITEM").toString());
					if(netWeight!=null && !"".equals(netWeight))
					{
						Double Netweight=quantity*Double.parseDouble(netWeight);
						totalItemNetWeight+=Netweight;
						System.out.println("TotalNetWeight:"+totalItemNetWeight);
					}
					
					JournalDetail journalDetail=new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson=coaDAO.getCOAByName(plant, (String) invDet.get("ACCOUNT_NAME"));
					System.out.println("Json"+coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME((String) invDet.get("ACCOUNT_NAME"));
					//journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString()));
					
					if(!invoiceHdrmap.get("ORDERDISCOUNTTYPE").toString().equalsIgnoreCase("%")) {
						journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())-Double.parseDouble(invoiceHdrmap.get("ORDER_DISCOUNT").toString())/invoiceDetInfoList.size());
					}else {
						Double jodamt = (Double.parseDouble(invDet.get("AMOUNT").toString())/100)*Double.parseDouble(invoiceHdrmap.get("ORDER_DISCOUNT").toString());
						journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString()) -jodamt);
					}
					
					String avg=new InvUtil().getCostOfGoods((String)invDet.get("ITEM"),plant);
					if(avg!=null && !"".equals(avg)) {
						totalCostofGoodSold+=Double.parseDouble(avg)*(quantity);
					}else {
						avg=new InvUtil().getAvgCostofItem((String)invDet.get("ITEM"),plant);
						if(avg!=null && !"".equals(avg)) {
							totalCostofGoodSold+=Double.parseDouble(avg)*(quantity);
						}
						
					}
					//totalCostofGoodSold+=Double.parseDouble(invDet.get("AMOUNT").toString());
					boolean isLoop=false;
					if(journalDetails.size()>0)
					{
						int i=0;
						for(JournalDetail journal:journalDetails) {
							int accountId=journal.getACCOUNT_ID();
							if(accountId==journalDetail.getACCOUNT_ID()) {
								isLoop=true;
								Double sumDetit=journal.getCREDITS()+journalDetail.getCREDITS();
								journalDetail.setCREDITS(sumDetit);
								journalDetails.set(i, journalDetail);
								break;
							}
							i++;
							
						}
						if(isLoop==false) {
							journalDetails.add(journalDetail);
						}
					}
					else
					{
						journalDetails.add(journalDetail);
					}
				}
				JournalDetail journalDetail_1=new JournalDetail();
				journalDetail_1.setPLANT(plant);
				JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) invoiceHdrmap.get("CUSTNO"));
				if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
					JSONObject cusJson=cusDAO.getCustomerName(plant, (String) invoiceHdrmap.get("CUSTNO"));
					if(!cusJson.isEmpty()) {
						coaJson1=coaDAO.getCOAByName(plant, cusJson.getString("CNAME"));
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							coaJson1=coaDAO.getCOAByName(plant, cusJson.getString("CUSTNO")+"-"+cusJson.getString("CNAME"));
						}
					
					}
				}
				if(coaJson1.isEmpty() || coaJson1.isNullObject())
				{
					
				}
				else
				{
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					if(coaJson1.getString("account_name")!=null) {
						journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
					}
					//journalDetail_1.setACCOUNT_NAME((String) invoiceHdrmap.get("CUSTNO"));
					journalDetail_1.setDEBITS(Double.parseDouble(invoiceHdrmap.get("TOTAL_AMOUNT").toString()));
					journalDetails.add(journalDetail_1);
				}
				String taxamount=invoiceHdrmap.get("TAXAMOUNT").toString();
				if(taxamount.isEmpty())
				{
					taxamount="0.00";
				}
				Double taxAmountFrom=Double.parseDouble(taxamount);
				if(taxAmountFrom>0)
				{
					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
					journalDetail_2.setACCOUNT_NAME("VAT Output");*/
					MasterDAO masterDAO = new MasterDAO();
					String planttaxtype = masterDAO.GetTaxType(plant);
					
					if(planttaxtype.equalsIgnoreCase("TAX")) {
						JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("VAT Output");
					}else if(planttaxtype.equalsIgnoreCase("GST")) {
						JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Payable");
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("GST Payable");
					}else if(planttaxtype.equalsIgnoreCase("VAT")) {
						JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("VAT Output");
					}else {
						JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("VAT Output");
					}
					journalDetail_2.setCREDITS(taxAmountFrom);
					journalDetails.add(journalDetail_2);
				}
				
				String discount=invoiceHdrmap.get("DISCOUNT").toString();
				Double discountFrom = Double.parseDouble(discount);
				String discountType=invoiceHdrmap.get("DISCOUNT_TYPE").toString();
				String orderdiscount=invoiceHdrmap.get("ORDER_DISCOUNT").toString();
				Double orderDiscountFrom=0.00;
				if(!orderdiscount.isEmpty())
				{
					orderDiscountFrom=Double.parseDouble(orderdiscount);
					orderDiscountFrom=(Double.parseDouble(invoiceHdrmap.get("SUB_TOTAL").toString())*orderDiscountFrom)/100;
				}
				if(discountFrom>0 || orderDiscountFrom>0)
				{
					if(!discountType.isEmpty())
					{
						if(discountType.equalsIgnoreCase("%"))
						{
							Double subTotalAfterOrderDiscount=Double.parseDouble(invoiceHdrmap.get("SUB_TOTAL").toString());
							discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
						}
					}
//					discountFrom=discountFrom;
					JournalDetail journalDetail_3=new JournalDetail();
					journalDetail_3.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discounts given - COS");
					journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
					journalDetail_3.setDEBITS(discountFrom);
					journalDetails.add(journalDetail_3);
				}
				String shippingCost=invoiceHdrmap.get("SHIPPINGCOST").toString();
				if(!shippingCost.isEmpty())
				{
					Double shippingCostFrom=Double.parseDouble(shippingCost);
					if(shippingCostFrom>0)
					{
						JournalDetail journalDetail_4=new JournalDetail();
						journalDetail_4.setPLANT(plant);
						JSONObject coaJson4=coaDAO.getCOAByName(plant, "Outward freight & shipping");
						journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
						journalDetail_4.setACCOUNT_NAME("Outward freight & shipping");
						journalDetail_4.setCREDITS(shippingCostFrom);
						journalDetails.add(journalDetail_4);
					}
				}
				String adjustment=invoiceHdrmap.get("ADJUSTMENT").toString();
				if(!adjustment.isEmpty())
				{
					Double adjustFrom=Double.parseDouble(adjustment);
					if(adjustFrom>0)
					{
						JournalDetail journalDetail_7=new JournalDetail();
						journalDetail_7.setPLANT(plant);
						JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
						journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
						journalDetail_7.setACCOUNT_NAME("Adjustment");
						journalDetail_7.setCREDITS(adjustFrom);
						journalDetails.add(journalDetail_7);
					}
					else if(adjustFrom<0)
					{
						JournalDetail journalDetail_7=new JournalDetail();
						journalDetail_7.setPLANT(plant);
						JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
						journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
						journalDetail_7.setACCOUNT_NAME("Adjustment");
						adjustFrom=Math.abs(adjustFrom);
						journalDetail_7.setDEBITS(adjustFrom);
						journalDetails.add(journalDetail_7);
					}
				}
				Journal journal=new Journal();
				Double totalDebitAmount=0.00;
				for(JournalDetail jourDet:journalDetails)
				{
					 totalDebitAmount=totalDebitAmount+jourDet.getDEBITS();
				}
				journalHead.setTOTAL_AMOUNT(totalDebitAmount);
				journal.setJournalHeader(journalHead);
				journal.setJournalDetails(journalDetails);
				JournalService journalService=new JournalEntry();
				Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
				if(journalFrom.getJournalHeader()!=null)
				{
					if(journalFrom.getJournalHeader().getID()>0)
					{
						journalHead.setID(journalFrom.getJournalHeader().getID());
						journalService.updateJournal(journal, username);
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}
					else
					{
						journalService.addJournal(journal, username);
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable htMovHis = new Hashtable();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						htMovHis.put(IDBConstants.ITEM, "");
						htMovHis.put(IDBConstants.QTY, "0.0");
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(htMovHis);
					}
					//Cost of goods sold
					if(totalCostofGoodSold>0)
					{
						journalDetails.clear();
						journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
						JournalDetail journalDetail_InvAsset=new JournalDetail();
						journalDetail_InvAsset.setPLANT(plant);
						JSONObject coaJson7=coaDAO.getCOAByName(plant, "Inventory Asset");
						System.out.println("Json"+coaJson7.toString());
						journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
						journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));  
						journalDetail_InvAsset.setCREDITS(totalCostofGoodSold); 
						journalDetails.add(journalDetail_InvAsset);
						
						JournalDetail journalDetail_COG=new JournalDetail();
						journalDetail_COG.setPLANT(plant);
						JSONObject coaJson8=coaDAO.getCOAByName(plant, "Cost of goods sold");
						System.out.println("Json"+coaJson1.toString());
						journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
						journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
						journalDetail_COG.setDEBITS(totalCostofGoodSold);  
						journalDetails.add(journalDetail_COG);
						
						journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						
						Journal journalCOG=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
						if(journalCOG.getJournalHeader()!=null)
						{
							if(journalCOG.getJournalHeader().getID()>0)
							{
								journalHead.setID(journalCOG.getJournalHeader().getID());
								journalService.updateJournal(journal, username);
								MovHisDAO movHisDao = new MovHisDAO();
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
								jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);		
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}
							else
							{
								journalService.addJournal(journal, username);
								MovHisDAO movHisDao = new MovHisDAO();
								Hashtable htMovHis = new Hashtable();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put(IDBConstants.QTY, "0.0");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS","");
								movHisDao.insertIntoMovHis(htMovHis);
							}
						}
					}
				}
				
				boolean isAdded = true;
				/*if(invoiceHdrmap.get("INVOICE").toString().length() > 0 && invoiceHdrmap.get("GINO").toString().length() > 0 && invoiceHdrmap.get("DONO").toString().length() == 0) {
					for(Map invDet:invoiceDetInfoList) {
						int lnno = Integer.parseInt(invDet.get("LNNO").toString());
						String ITEM = "",UOM = "", QTY = "", LOC = "", BATCH = "";
						ITEM = invDet.get("ITEM").toString();
						UOM = invDet.get("UOM").toString();
						QTY = invDet.get("QTY").toString();
						LOC = invDet.get("LOC").toString();
						BATCH = invDet.get("BATCH").toString();
						
						Map invmap = null;
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails(UOM, plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap = new HashMap();
						invmap.put(IConstants.PLANT, plant);
						invmap.put(IConstants.ITEM, ITEM);
						invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, ITEM));
						invmap.put("INVOICE", invoiceHdrmap.get("INVOICE").toString());
						invmap.put(IConstants.DODET_DONUM, "");
						invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
						invmap.put(IConstants.CUSTOMER_NAME, invoiceHdrmap.get("CUSTNO").toString());
						invmap.put(IConstants.LOC, LOC);			
						invmap.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, invoiceHdrmap.get("CUSTNO").toString());
						invmap.put(IConstants.BATCH, BATCH);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put("ISSUEDQTY", QTY);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						if (invoiceHdrmap.get("INVOICE_DATE").toString().length()>5)
							strTranDate    = invoiceHdrmap.get("INVOICE_DATE").toString().substring(6)+"-"+ invoiceHdrmap.get("INVOICE_DATE").toString().substring(3,5)+"-"+invoiceHdrmap.get("INVOICE_DATE").toString().substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put("RETURN_DATE", strTranDate);
						invmap.put("UOMQTY", uomQty);
						invmap.put("NOTES", "");
						invmap.put("SORETURN", "");
						invmap.put("GINO", invoiceHdrmap.get("GINO").toString());
						invmap.put(IConstants.CUSTOMER_NAME, invoiceHdrmap.get("CUSTNO").toString());
						isAdded = processShipHisReversal(invmap);
						if(isAdded) {
							processInvAdd(invmap);
						}
						if(isAdded) {
							processMovHis_IN(invmap);
						}					
					}
				}*/
				String deductInv = (String)invoiceHdrmap.get("DEDUCT_INV");
				process: 
				if(isAdded && deductInv.equalsIgnoreCase("1")) {
					for(Map invDet:invoiceDetInfoList) {
						
						int lnno = Integer.parseInt(invDet.get("LNNO").toString());
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
						htCondiShipHis.put("INVOICENO", invoiceHdrmap.get("GINO").toString());
						htCondiShipHis.put("STATUS","C");
						htCondiShipHis.put("BATCH",invDet.get("BATCH").toString());
						htCondiShipHis.put("dolno", Integer.toString(lnno));
						htCondiShipHis.put("LOC1", invDet.get("LOC").toString());
						htCondiShipHis.put(IConstants.ITEM, invDet.get("ITEM").toString());
						boolean isexists = new ShipHisDAO().isExisit(htCondiShipHis, "");
						
						double costdouble = Double.valueOf(invDet.get("UNITPRICE").toString())/Double.valueOf(invoiceHdrmap.get("CURRENCYUSEQT").toString());
						String scost = String.valueOf(costdouble);
						
						if(isexists){
					        String query = "set UNITPRICE=" + scost + ",ORDQTY=" + invDet.get("QTY").toString();
					        isAdded = new ShipHisDAO().updateShipHis(query,htCondiShipHis,"");
			            }
						
						/**/
						String QTY = "", ORDQTY = "";
						boolean processInv = false;
						/*if(ordQty.size() == 0) {*/
							QTY = invDet.get("QTY").toString();
							ORDQTY = invDet.get("QTY").toString();
							processInv = true;
						/*}else {
							if(ordQty.size()> i) {
								ORDQTY = (String) qty.get(i);
								double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
								double i_Qty = Double.parseDouble((String) qty.get(i));
								i_Qty = i_Qty - i_OrdQty;							
								QTY = Double.toString(i_Qty);
								if(i_Qty > 0) {
									processInv = true;
								}
							}else {
								QTY = (String) qty.get(i);
								ORDQTY = (String) qty.get(i);
								processInv = true;
							}
						}*/
						/**/
					
					if(processInv) { 
					String ITEM_QTY="";
					Map invmap = new HashMap();
						String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,invDet.get("ITEM").toString());
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {
							List itemQry = new InvMstDAO().getOutBoundPickingBatchByWMS(plant,invDet.get("ITEM").toString(), invDet.get("LOC").toString(), invDet.get("BATCH").toString());
							double invqty = 0;
							if (itemQry.size() > 0) {
								for (int j = 0; j < itemQry.size(); j++) {
//									Map m = (Map) itemQry.get(j);
									ITEM_QTY =  invDet.get("QTY").toString();
									invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
								}
								/*double calinqty = pickingQty * Double.valueOf(UOMQTY);*/
								double pickingQty = Double.parseDouble((invDet.get("QTY").toString()));
								if(invqty < pickingQty){
									throw new Exception(
										"Not enough inventory found for ProductID/Batch for Order Line No "+lnno+" in the location selected");
								}						
							} else {
								throw new Exception(
									"Not enough inventory found for ProductID/Batch for Order Line No "+lnno+" in the location selected");
								
							}
						}
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails(invDet.get("UOM").toString(), plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap.put(IDBConstants.PLANT, plant);
						invmap.put(IConstants.DODET_DONUM, "");
						invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
						invmap.put(IConstants.CUSTOMER_NAME, invoiceHdrmap.get("CUSTNO").toString());
						invmap.put(IDBConstants.ITEM, invDet.get("ITEM").toString());
						invmap.put(IDBConstants.ITEM_DESC, invDet.get("ITEM").toString());
						invmap.put(IDBConstants.LOC, invDet.get("LOC").toString());
						invmap.put(IDBConstants.USERFLD4, invDet.get("BATCH").toString());
						invmap.put(IConstants.BATCH, invDet.get("BATCH").toString());						
						
						invmap.put(IConstants.ORD_QTY, ORDQTY);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IDBConstants.CURRENCYID, (String) request.getSession().getAttribute("BASE_CURRENCY"));
						invmap.put("UNITPRICE", scost);
						invmap.put(IConstants.ISSUEDATE, invoiceHdrmap.get("INVOICE_DATE").toString());
						invmap.put(IConstants.INVOICENO, invoiceHdrmap.get("INVOICE").toString());
						invmap.put("GINO", invoiceHdrmap.get("GINO").toString());
						invmap.put("UOMQTY", uomQty);						
						if (invoiceHdrmap.get("INVOICE_DATE").toString().length()>5)
							strTranDate    = invoiceHdrmap.get("INVOICE_DATE").toString().substring(6)+"-"+ invoiceHdrmap.get("INVOICE_DATE").toString().substring(3,5)+"-"+invoiceHdrmap.get("INVOICE_DATE").toString().substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						isAdded = processShipHis(invmap);
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {
							if(isAdded) {
								isAdded = processInvRemove(invmap);
							}
							if(isAdded) {
								processBOM(invmap);
							}
						}
						if(isAdded) {
							processMovHis_OUT(invmap);
						}
						if (isAdded == true) {//Shopify Inventory Update
		   					Hashtable htCond = new Hashtable();
		   					htCond.put(IConstants.PLANT, plant);
		   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
		   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,invDet.get("ITEM").toString());						
								if(nonstkflag.equalsIgnoreCase("N")) {
		   						String availqty ="0";
		   						ArrayList invQryList = null;
		   						htCond = new Hashtable();
		   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,invDet.get("ITEM").toString(), new ItemMstDAO().getItemDesc(plant, invDet.get("ITEM").toString()),htCond);						
		   						if(new PlantMstDAO().getisshopify(plant)) {
		   						if (invQryList.size() > 0) {					
		   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		   								//String result="";
		                                Map lineArr = (Map) invQryList.get(iCnt);
		                                availqty = (String)lineArr.get("AVAILABLEQTY");
		                                System.out.println(availqty);
		   							}
		   							double availableqty = Double.parseDouble(availqty);
		       						new ShopifyService().UpdateShopifyInventoryItem(plant, invDet.get("ITEM").toString(), availableqty);
		   						}	
								}
								}
		   					}
		   				}
						if(!isAdded) {
							break process;
						}
					}
				}
			}
				
				
				
			  DbBean.CommitTran(ut); 
			  
			  	
			 /* } else { 
				  DbBean.RollbackTran(ut); 
				  }*/
			  
			  response.sendRedirect("../invoice/detail?dono="+invoiceHdrmap.get("DONO")+"&custno="+invoiceHdrmap.get("CUSTNO")+"&INVOICE_HDR="+invoiceid);
			  } catch (Exception e) {
			  DbBean.RollbackTran(ut); e.printStackTrace(); 
			  }
			 
			
		}
		
		
		else if(baction.equalsIgnoreCase("convertToCancel")) {
//			 InvoiceUtil invoiceUtil = new InvoiceUtil(); 
//			  DateUtils dateutils = new DateUtils(); 
			  InvoiceDAO invoicedao = new InvoiceDAO();
			  MovHisDAO movHisDao = new MovHisDAO();
			  UserTransaction ut = null;
			  String result ="";
			  String invoiceid = StrUtils.fString(request.getParameter("INVID")).trim(); 
			  String invoice = StrUtils.fString(request.getParameter("INVOICE")).trim(); 
			  String status = StrUtils.fString(request.getParameter("STATUS")).trim(); 
			  String taxstatus = StrUtils.fString(request.getParameter("TAXSTATUS")).trim(); 
			  String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			  String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim(); 
//			  boolean isUpdated = false;
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if(status.equalsIgnoreCase("open") || status.equalsIgnoreCase("draft") ) {
					if(taxstatus.equalsIgnoreCase("Tax Generated")){
						result = "Couldn't cancel the invoice.";
						response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
					}else {
					
						Hashtable htgi = new Hashtable();	
						String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='CANCELLED',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoiceid+"'";
						invoicedao.updategino(sqlgi, htgi, "");

							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, invoice,"INVOICE");
							if(journalFrom.getJournalHeader()!=null)
							{
								if(journalFrom.getJournalHeader().getID()>0)
								{
									journalFrom.getJournalHeader().setJOURNAL_STATUS("CANCELLED");
									journalService.updateJournal(journalFrom, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalFrom.getJournalHeader().getTRANSACTION_TYPE()+" "+journalFrom.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
							}
							
							Hashtable ht1 = new Hashtable();
							ht1.put("ID", invoiceid);
							ht1.put("PLANT", plant);
							
							List invoicehdr = invoicedao.getInvoiceHdrById(ht1);
							Map invoiceHdrmap=(Map)invoicehdr.get(0);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("INVOICEHDRID", invoiceid);
							ht2.put("PLANT", plant);
							
							List invoicedet = invoicedao.getInvoiceDetByHdrId(ht2);
							
							String origin = (String)invoiceHdrmap.get("ORIGIN");
							String deductInv = (String)invoiceHdrmap.get("DEDUCT_INV");							
							String JobNum = (String)invoiceHdrmap.get("JobNum"); //azees consignmentToInvoice
							for(int i =0 ; i < invoicedet.size() ; i++){
								
								Map invoicedetmap=(Map)invoicedet.get(i);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CANCEL_INVOICE);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd((String)invoiceHdrmap.get("INVOICE_DATE")));														
								htMovHis.put(IDBConstants.ITEM, (String) invoicedetmap.get("ITEM"));
								String billqty = String.valueOf((String) invoicedetmap.get("QTY"));
								htMovHis.put(IDBConstants.QTY, billqty);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, (String)invoiceHdrmap.get("INVOICE"));
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS",(String)invoiceHdrmap.get("DONO"));
								
								Hashtable htMovChk = new Hashtable();
								htMovChk.clear();
								htMovChk.put(IDBConstants.PLANT, plant);
								htMovChk.put("DIRTYPE", TransactionConstants.CANCEL_INVOICE);
								htMovChk.put(IDBConstants.ITEM, (String) invoicedetmap.get("ITEM"));
								htMovChk.put(IDBConstants.QTY, billqty);
								htMovChk.put(IDBConstants.MOVHIS_ORDNUM, (String)invoiceHdrmap.get("INVOICE"));
								boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE%' ");
								if(!isAdded)	
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								
								
								/* Code to reverse the Inventory */								

								if(isAdded&&!status.equalsIgnoreCase("draft")&&
									origin.equalsIgnoreCase("manual")&&deductInv.equalsIgnoreCase("1")) {
											int lnno = i+1;
											String ITEM = "",UOM = "", LOC = "", BATCH = "",
													invoiceNo="", CustCode ="", invoiceDate="";//QTY = "", 
											ITEM = (String) invoicedetmap.get("ITEM");
											UOM = (String) invoicedetmap.get("UOM");
//											QTY = (String) invoicedetmap.get("billqty");
											LOC = (String) invoicedetmap.get("LOC");
											BATCH = (String) invoicedetmap.get("BATCH");
											invoiceNo = (String)invoiceHdrmap.get("INVOICE");
											CustCode = (String)invoiceHdrmap.get("CUSTNO");
											invoiceDate = (String)invoiceHdrmap.get("INVOICE_DATE");
											
											Map invmap = null;
											String uomQty="";
											List uomQry = new UomDAO().getUomDetails(UOM, plant, "");
											if(uomQry.size()>0) {
												Map m = (Map) uomQry.get(0);
												uomQty = (String)m.get("QPUOM");
											}else {
												uomQty = "0";
											}
											
											String strTranDate ="";
											invmap = new HashMap();
											invmap.put(IConstants.PLANT, plant);
											invmap.put(IConstants.ITEM, ITEM);
											invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, ITEM));
											invmap.put("INVOICE", invoiceNo);
											invmap.put(IConstants.DODET_DONUM, "");
											invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
											invmap.put(IConstants.CUSTOMER_NAME, CustCode);
											invmap.put(IConstants.LOC, LOC);			
											invmap.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
											invmap.put(IConstants.LOGIN_USER, username);
											invmap.put(IConstants.CUSTOMER_CODE, CustCode);
											invmap.put(IConstants.BATCH, BATCH);
											invmap.put(IConstants.QTY, billqty);
											invmap.put(IConstants.ORD_QTY, billqty);
											invmap.put("ISSUEDQTY", billqty);
											invmap.put(IConstants.REMARKS, "");
											invmap.put(IConstants.RSNDESC, "");
											if (invoiceDate.length()>5)
												strTranDate    = invoiceDate.substring(6)+"-"+ invoiceDate.substring(3,5)+"-"+invoiceDate.substring(0,2);
											invmap.put(IConstants.TRAN_DATE, strTranDate);
											invmap.put("RETURN_DATE", strTranDate);
											invmap.put("UOMQTY", uomQty);
											invmap.put("NOTES", "");
											invmap.put("SORETURN", "");
											invmap.put("GINO", (String)invoiceHdrmap.get("GINO"));
											invmap.put(IConstants.CUSTOMER_NAME, CustCode);
											isAdded = processShipHisReversal(invmap);
											if(isAdded) {
												processInvAdd(invmap);
											}
											if(isAdded) {
												processMovHis_IN(invmap);
											}
											if (isAdded == true) {//Shopify Inventory Update
							   					Hashtable htCond = new Hashtable();
							   					htCond.put(IConstants.PLANT, plant);
							   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
							   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,ITEM);						
													if(nonstkflag.equalsIgnoreCase("N")) {
							   						String availqty ="0";
							   						ArrayList invQryList = null;
							   						htCond = new Hashtable();
							   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,ITEM, new ItemMstDAO().getItemDesc(plant, ITEM),htCond);						
							   						if(new PlantMstDAO().getisshopify(plant)) {
							   						if (invQryList.size() > 0) {					
							   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
							   								//String result="";
							                                Map lineArr = (Map) invQryList.get(iCnt);
							                                availqty = (String)lineArr.get("AVAILABLEQTY");
							                                System.out.println(availqty);
							   							}
							   							double availableqty = Double.parseDouble(availqty);
							       						new ShopifyService().UpdateShopifyInventoryItem(plant, ITEM, availableqty);
							   						}	
													}
													}
							   					}
							   				}
								}
								/*	*/
								
									//azees consignmentToInvoice
									if(!JobNum.isEmpty()) {
										ToDetDAO _ToDetDAO = new ToDetDAO();
										
										Hashtable htestCond = new Hashtable();
										htestCond.put("PLANT", plant);
										htestCond.put("TONO", JobNum);
										htestCond.put("TOLNNO", (String) invoicedetmap.get("LNNO"));
										htestCond.put("ITEM", (String) invoicedetmap.get("ITEM"));
							
										String queryest = "set RECSTAT='',QTYAC= isNull(QTYAC,0) - " + (String) invoicedetmap.get("QTY") ;
//										boolean flag = 
												_ToDetDAO.update(queryest, htestCond, "");
							
										/*updatestatus = "set STATUS=CASE WHEN qtyis > 0 THEN 'O' ELSE 'N' END";
										flag = _EstDetDAO.update(updatestatus, htestCond, "");*/
										
									}
							}

							if(!JobNum.isEmpty()) {	//azees consignmentToInvoice							
								
//								ToDetDAO _ToDetDAO = new ToDetDAO();
//								ToHdrDAO _ToHdrDAO = new ToHdrDAO();
//							String updateEstHdr="";
//							Hashtable htestCond = new Hashtable();
//							htestCond.put("PLANT", plant);
//							htestCond.put("TONO", JobNum);
//							boolean flag = _ToDetDAO.isExisit(htestCond," QTYAC > 0 ");	
//							if(!flag)
//								updateEstHdr = "set ORDER_STATUS=CASE WHEN STATUS='C' THEN 'PROCESSED' WHEN STATUS='N' THEN 'Open' ELSE 'PARTIALLY PROCESSED' END ";
//															
							}						
							DbBean.CommitTran(ut);
							result = "Invoice cancelled successfully.";
							response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&rsuccess="+ result);
						
						
					}
				}else if(status.equalsIgnoreCase("CANCELLED")){
					result = "Invoice already cancelled";
					response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
				}else {
					DbBean.RollbackTran(ut);
					result = "Couldn't cancel the invoice.";
					response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't cancel the invoice.";
				response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
			}
			
		}
		
		else if(baction.equalsIgnoreCase("convertToDraft")) {
//			 InvoiceUtil invoiceUtil = new InvoiceUtil(); 
//			  DateUtils dateutils = new DateUtils(); 
			  InvoiceDAO invoicedao = new InvoiceDAO();
//			  MovHisDAO movHisDao = new MovHisDAO();
			  UserTransaction ut = null;
			  String result ="";
			  String invoiceid = StrUtils.fString(request.getParameter("INVID")).trim(); 
			  String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			  String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim(); 
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
					
				Hashtable htgi = new Hashtable();	
				String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Draft',UPAT='"+DateUtils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoiceid+"'";
				invoicedao.updategino(sqlgi, htgi, "");

				DbBean.CommitTran(ut);
				result = "Invoice Drafted successfully.";
				response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&rsuccess="+ result);
					
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't draft the invoice.";
				response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
			}
			
		}
		
		
		else if(baction.equalsIgnoreCase("deleteinvoice")) {
//			 InvoiceUtil invoiceUtil = new InvoiceUtil(); 
//			  DateUtils dateutils = new DateUtils(); 
			  InvoiceDAO invoicedao = new InvoiceDAO();
			  MovHisDAO movHisDao = new MovHisDAO();
			  UserTransaction ut = null;
			  String result ="";
			  String invoiceid = StrUtils.fString(request.getParameter("INVID")).trim(); 
			  String invoice = StrUtils.fString(request.getParameter("INVOICE")).trim(); 
			  String status = StrUtils.fString(request.getParameter("STATUS")).trim(); 
			  String taxstatus = StrUtils.fString(request.getParameter("TAXSTATUS")).trim(); 
			  String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			  String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim(); 
			  boolean isUpdated = false;
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if(status.equalsIgnoreCase("open") || status.equalsIgnoreCase("draft") || status.equalsIgnoreCase("CANCELLED")) {
					 if(taxstatus.equalsIgnoreCase("Tax Generated")){
						result = "Couldn't delete the invoice.";
						response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
					}else {
						
						Hashtable ht1 = new Hashtable();
						ht1.put("ID", invoiceid);
						ht1.put("PLANT", plant);
						
						List invoicehdr = invoicedao.getInvoiceHdrById(ht1);
						Map invoiceHdrmap=(Map)invoicehdr.get(0);
						
						Hashtable ht2 = new Hashtable();
						ht2.put("INVOICEHDRID", invoiceid);
						ht2.put("PLANT", plant);
						
						List invoicedet = invoicedao.getInvoiceDetByHdrId(ht2);
					
						isUpdated =invoicedao.deleteInvoice(plant, invoiceid);
						
						if(isUpdated) {
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, invoice,"INVOICE");
							if(journalFrom.getJournalHeader()!=null)
							{
								if(journalFrom.getJournalHeader().getID()>0)
								{
									isUpdated = journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalFrom.getJournalHeader().getTRANSACTION_TYPE()+" "+journalFrom.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
							}
							if(isUpdated) {
								journalFrom=journalService.getJournalByTransactionId(plant, invoice,"COSTOFGOODSOLD");
								if(journalFrom.getJournalHeader()!=null)
								{
									if(journalFrom.getJournalHeader().getID()>0)
									{
										isUpdated = journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
										Hashtable jhtMovHis = new Hashtable();
										jhtMovHis.put(IDBConstants.PLANT, plant);
										jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);	
										jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
										jhtMovHis.put(IDBConstants.ITEM, "");
										jhtMovHis.put(IDBConstants.QTY, "0.0");
										jhtMovHis.put("RECID", "");
										jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journalFrom.getJournalHeader().getTRANSACTION_TYPE()+" "+journalFrom.getJournalHeader().getTRANSACTION_ID());
										jhtMovHis.put(IDBConstants.CREATED_BY, username);		
										jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										jhtMovHis.put("REMARKS","");
										movHisDao.insertIntoMovHis(jhtMovHis);
									}
								}
							}
							String origin = (String)invoiceHdrmap.get("ORIGIN");
							String deductInv = (String)invoiceHdrmap.get("DEDUCT_INV");
							String JobNum = (String)invoiceHdrmap.get("JobNum"); //azees consignmentToInvoice
							String gino= (String)invoiceHdrmap.get("GINO");
							if(gino.length() > 0 && !deductInv.equalsIgnoreCase("1")) {
								Hashtable htgi = new Hashtable();	
								String sqlgi = "UPDATE "+ plant+"_FINGINOTOINVOICE SET STATUS='NOT INVOICED' WHERE PLANT='"+ plant+"' AND GINO='"+gino+"'";
								invoicedao.updategino(sqlgi, htgi, "");
							}
							
							
							for(int i =0 ; i < invoicedet.size() ; i++){
								
								Map invoicedetmap=(Map)invoicedet.get(i);
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.DELETE_INVOICE);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd((String)invoiceHdrmap.get("INVOICE_DATE")));														
								htMovHis.put(IDBConstants.ITEM, (String) invoicedetmap.get("ITEM"));
								String billqty = String.valueOf((String) invoicedetmap.get("QTY"));
								htMovHis.put(IDBConstants.QTY, billqty);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, (String)invoiceHdrmap.get("INVOICE"));
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS",(String)invoiceHdrmap.get("DONO"));
								
								Hashtable htMovChk = new Hashtable();
								htMovChk.clear();
								htMovChk.put(IDBConstants.PLANT, plant);
								htMovChk.put("DIRTYPE", TransactionConstants.DELETE_INVOICE);
								htMovChk.put(IDBConstants.ITEM, (String) invoicedetmap.get("ITEM"));
								htMovChk.put(IDBConstants.QTY, billqty);
								htMovChk.put(IDBConstants.MOVHIS_ORDNUM, (String)invoiceHdrmap.get("INVOICE"));
								boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE%' ");
								if(!isAdded)	
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								
								if(isAdded&&status.equalsIgnoreCase("open")&&
										origin.equalsIgnoreCase("manual")&&deductInv.equalsIgnoreCase("1")) {
												int lnno = i+1;
												String ITEM = "",UOM = "", LOC = "", BATCH = "",
														invoiceNo="", CustCode ="", invoiceDate="";//QTY = "", 
												ITEM = (String) invoicedetmap.get("ITEM");
												UOM = (String) invoicedetmap.get("UOM");
//												QTY = (String) invoicedetmap.get("billqty");
												LOC = (String) invoicedetmap.get("LOC");
												BATCH = (String) invoicedetmap.get("BATCH");
												invoiceNo = (String)invoiceHdrmap.get("INVOICE");
												CustCode = (String)invoiceHdrmap.get("CUSTNO");
												invoiceDate = (String)invoiceHdrmap.get("INVOICE_DATE");
												
												Map invmap = null;
												String uomQty="";
												List uomQry = new UomDAO().getUomDetails(UOM, plant, "");
												if(uomQry.size()>0) {
													Map m = (Map) uomQry.get(0);
													uomQty = (String)m.get("QPUOM");
												}else {
													uomQty = "0";
												}
												
												String strTranDate ="";
												invmap = new HashMap();
												invmap.put(IConstants.PLANT, plant);
												invmap.put(IConstants.ITEM, ITEM);
												invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, ITEM));
												invmap.put("INVOICE", invoiceNo);
												invmap.put(IConstants.DODET_DONUM, "");
												invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
												invmap.put(IConstants.CUSTOMER_NAME, CustCode);
												invmap.put(IConstants.LOC, LOC);			
												invmap.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
												invmap.put(IConstants.LOGIN_USER, username);
												invmap.put(IConstants.CUSTOMER_CODE, CustCode);
												invmap.put(IConstants.BATCH, BATCH);
												invmap.put(IConstants.QTY, billqty);
												invmap.put(IConstants.ORD_QTY, billqty);
												invmap.put("ISSUEDQTY", billqty);
												invmap.put(IConstants.REMARKS, "");
												invmap.put(IConstants.RSNDESC, "");
												if (invoiceDate.length()>5)
													strTranDate    = invoiceDate.substring(6)+"-"+ invoiceDate.substring(3,5)+"-"+invoiceDate.substring(0,2);
												invmap.put(IConstants.TRAN_DATE, strTranDate);
												invmap.put("RETURN_DATE", strTranDate);
												invmap.put("UOMQTY", uomQty);
												invmap.put("NOTES", "");
												invmap.put("SORETURN", "");
												invmap.put("GINO", gino);
												invmap.put(IConstants.CUSTOMER_NAME, CustCode);
												isAdded = processShipHisReversal(invmap);
												if(isAdded) {
													processInvAdd(invmap);
												}
												if(isAdded) {
													processMovHis_IN(invmap);
												}
												if (isAdded == true) {//Shopify Inventory Update
								   					Hashtable htCond = new Hashtable();
								   					htCond.put(IConstants.PLANT, plant);
								   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
								   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,ITEM);						
														if(nonstkflag.equalsIgnoreCase("N")) {
								   						String availqty ="0";
								   						ArrayList invQryList = null;
								   						htCond = new Hashtable();
								   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,ITEM, new ItemMstDAO().getItemDesc(plant, ITEM),htCond);						
								   						if(new PlantMstDAO().getisshopify(plant)) {
								   						if (invQryList.size() > 0) {					
								   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
								   								//String result="";
								                                Map lineArr = (Map) invQryList.get(iCnt);
								                                availqty = (String)lineArr.get("AVAILABLEQTY");
								                                System.out.println(availqty);
								   							}
								   							double availableqty = Double.parseDouble(availqty);
								       						new ShopifyService().UpdateShopifyInventoryItem(plant, ITEM, availableqty);
								   						}	
														}
														}
								   					}
								   				}
									}
								 //azees consignmentToInvoice
								if(!JobNum.isEmpty() && gino.isEmpty()) {
									ToDetDAO _ToDetDAO = new ToDetDAO();
									
									Hashtable htestCond = new Hashtable();
									htestCond.put("PLANT", plant);
									htestCond.put("TONO", JobNum);
									htestCond.put("TOLNNO", (String) invoicedetmap.get("LNNO"));
									htestCond.put("ITEM", (String) invoicedetmap.get("ITEM"));
						
									String queryest = "set RECSTAT='',QTYAC= isNull(QTYAC,0) - " + (String) invoicedetmap.get("QTY") ;
//									boolean flag = 
											_ToDetDAO.update(queryest, htestCond, "");
						
									/*updatestatus = "set STATUS=CASE WHEN qtyis > 0 THEN 'O' ELSE 'N' END";
									flag = _EstDetDAO.update(updatestatus, htestCond, "");*/
									
								}
									/*	*/
							}
							if(!JobNum.isEmpty()) {	//azees consignmentToInvoice							
								
//								ToHdrDAO _ToHdrDAO = new ToHdrDAO();
//								ToDetDAO _ToDetDAO = new ToDetDAO();
//							String updateEstHdr="";
//							Hashtable htestCond = new Hashtable();
//							htestCond.put("PLANT", plant);
//							htestCond.put("TONO", JobNum);
//							boolean flag = _ToDetDAO.isExisit(htestCond," QTYAC > 0 ");	
//							if(!flag)
//								updateEstHdr = "set ORDER_STATUS=CASE WHEN STATUS='C' THEN 'PROCESSED' WHEN STATUS='N' THEN 'Open' ELSE 'PARTIALLY PROCESSED' END ";
															
							}
							DbBean.CommitTran(ut);
							result = "Invoice deleted successfully.";
							response.sendRedirect("../invoice/summary?result="+ result);
						
						}else {
							DbBean.RollbackTran(ut);
							result = "Couldn't delete the invoice.";
							response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
						}
					}
				}else {
					DbBean.RollbackTran(ut);
					result = "Couldn't delete the invoice.";
					response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't delete the invoice.";
				response.sendRedirect("../invoice/detail?INVOICE_HDR="+invoiceid+"&resultnew="+ result);
			}
			
		}
		else if (baction.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim(); 
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			InvoiceDAO invoicedao = new InvoiceDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = invoicedao.getInvoiceAttachByPrimId(ht1);
				Map billAttach=(Map)paymentAttachment.get(0);
				String filePath=(String) billAttach.get("FilePath");
				String fileType=(String) billAttach.get("FileType");
				String fileName=(String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	  
		}else if(baction.equalsIgnoreCase("removeAttachmentById"))
			{
				System.out.println("Remove Attachments by ID");
				String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim(); 
				int ID=Integer.parseInt(request.getParameter("removeid"));
				InvoiceDAO invoicedao = new InvoiceDAO();
				try {
					Hashtable ht1 = new Hashtable();
					ht1.put("ID", String.valueOf(ID));
					ht1.put("PLANT", plant);
					invoicedao.deleteInvAttachByPrimId(ht1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.getWriter().write("Deleted");  
			
		}else if(action.equalsIgnoreCase("Save")) {
			/* InvoiceHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			DoHdrDAO DoHdrDAO = new DoHdrDAO();
			String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "",cmd = "",TranId = "",salesloc="",orderdiscount="0",
			itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "",isexpense = "0",taxamount = "0",gino="",
			adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",empno="",terms="",custName="",custName1="",ORDERTYPE="",empName="",taxtreatment="",
			shipId = "", shipCust = "", incoterm = "", origin = "", deductInv = "",currencyid="",currencyuseqt="0",projectid="",transportid="",orderdiscstatus = "0",discstatus = "0",
			shipstatus = "0",taxid = "",orderdisctype = "%",gst="0",jobNum="";//shippingcost="",
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="";
			String dob="",nationality="";
			String alertitem="";
			/*InvoiceDet*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),addcost = new ArrayList(),addtype = new ArrayList(),Ucost = new ArrayList(),notesexp = new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(),detDiscounttype = new ArrayList(), taxType = new ArrayList(),DETID= new ArrayList(),
					amount = new ArrayList(),edit_item=new ArrayList<>(),edit_qty=new ArrayList<>(),tlnno= new ArrayList(),dolnno= new ArrayList();
			List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),index = new ArrayList(),
					 ordQty = new ArrayList(),delLnno = new ArrayList(),delLnQty = new ArrayList(),
							 delLnLoc = new ArrayList(),delLnBatch = new ArrayList(),delLnUom = new ArrayList(),
							 delLnItem = new ArrayList(), convcost= new ArrayList(), is_cogs_set = new ArrayList();
			List<Hashtable<String,String>> invoiceDetInfoList = null;
			List<Hashtable<String,String>> invoiceAttachmentList = null;
			List<Hashtable<String,String>> invoiceAttachmentInfoList = null;
			Hashtable<String,String> invoiceDetInfo = null;
			Hashtable<String,String> invoiceAttachment = null;
			UserTransaction ut = null;
			InvoiceUtil invoiceUtil = new InvoiceUtil();
			InvoiceDAO invoiceDAO = new InvoiceDAO();
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isAdded = false;
			boolean isAmntExceed = false;
			boolean Isconvcost=false;
			String result="", amntExceedMsg ="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0,addcostCount  = 0,addtypeCount  = 0,UcostCount  = 0,dolnnoCount = 0, costCount  = 0, detDiscountCount  = 0, detDiscounttypeCount  = 0,
					taxTypeCount  = 0,DETIDCount  = 0, amountCount  = 0, notesexpcount =0,editItemCount=0,editQtyCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0, idCount = 0, ordQtyCount = 0,
					delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
					delLnItemCount=0;//,convcostCount = 0,is_cogs_setCount=0,tlnnoCount = 0,;
			/*others*/
			String personIncharge="",ctype="",fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
			try{
				////////////////
				List listQry = new ArrayList();
		    	listQry = (List) request.getSession().getAttribute("EST_LIST");
		    	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
		    	String ISINVENTORYMINQTY = DoHdrDAO.getISINVENTORYMINQTY(plant);
				if(isMultipart) {
								
				iterator = items.iterator();
//				StrUtils strUtils = new StrUtils();
				invoiceAttachmentList = new ArrayList<Hashtable<String,String>>();
				invoiceAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* InvoiceHdr*/
					if (fileItem.isFormField()) {
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							
							CustCode = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER")) {
							
							custName = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER1")) {
							
							custName1 = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("invoice")) {
							
							invoiceNo = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("gino")) {
							
							gino = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("ORDERNO")) {
							
							dono = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("invoice_date")) {
							
							invoiceDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("due_date")) {
							
							dueDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							
							payTerms = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
							
							ORDERTYPE = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
							
							empno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
							
							empName = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
							
							itemRates = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
							
							discount = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
							
							discountType = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("orderdiscount")) {
							
							orderdiscount = StrUtils.fString(fileItem.getString()).trim();
						}
					
							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("discount_account")) { double
							 * discountVal = Double.parseDouble(discount); if(discountVal > 0) {
							 * discountAccount = StrUtils.fString(fileItem.getString()).trim(); } }
							 */
					
						if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
							
							//shippingCost = StrUtils.fString(fileItem.getString()).trim();
							shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
							
							//adjustment = StrUtils.fString(fileItem.getString()).trim();
							adjustment = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxamount")) {
							
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("invoice_status")) {
							
							invoiceStatus = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							
							note = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("terms")) {
							
							terms = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cmd")) {
							
							cmd = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("TranId")) {
							
							TranId = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
							
							salesloc = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("isexpense")) {
							
							isexpense = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
							
							taxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
							
							shipId = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
							
							shipCust = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("ORIGIN")) {
							
							origin = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("DEDUCT_INV")) {
							
							deductInv = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("INCOTERM")) {
							
							incoterm = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							
							currencyid=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
							
							orderdiscstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("discounttaxstatus")) {
							
							discstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
							
							shipstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							
							taxid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
							
							orderdisctype = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							
							projectid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
							
							transportid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							
							gst = StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
							jobNum = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPCONTACTNAME")) {
							shipcontactname = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPDESGINATION")) {
							shipdesgination = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPADDR1")) {
							shipaddr1 = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPADDR2")) {
							shipaddr2 = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPADDR3")) {
							shipaddr3 = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPADDR4")) {
							shipaddr4 = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPSTATE")) {
							shipstate = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPZIP")) {
							shipzip = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPWORKPHONE")) {
							shipworkphone = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPCOUNTRY")) {
							shipcountry = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPHPNO")) {
							shiphpno = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPEMAIL")) {
							shipemail = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("DOBYEAR")) {
							dob = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("NATIONAL")) {
							nationality = StrUtils.fString(fileItem.getString()).trim();
						}
					}
					// Get Customer Code by Name
					/*if(CustCode.isEmpty())
					{
						if(!custName.isEmpty())
						{
						ArrayList arrList = new ArrayList();
						CustMstDAO movHisDAO= new CustMstDAO();
						Hashtable htData =new Hashtable();
						htData.put("PLANT", plant);
						htData.put("CNAME", custName);
						arrList = movHisDAO.selectCustMst("CUSTNO", htData,"");
						Map m = (Map) arrList.get(0);
						CustCode = (String) m.get("CUSTNO");
						}
					}*/
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						
						String fileLocation = "C:/ATTACHMENTS/Invoice" + "/"+ CustCode + "/"+ invoiceNo;
						String filetempLocation = "C:/ATTACHMENTS/Invoice" + "/temp" + "/"+ CustCode + "/"+ invoiceNo;
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						invoiceAttachment = new Hashtable<String, String>();
						invoiceAttachment.put("PLANT", plant);
						invoiceAttachment.put("FILETYPE", fileItem.getContentType());
						invoiceAttachment.put("FILENAME", fileName);
						invoiceAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						invoiceAttachment.put("FILEPATH", fileLocation);
						invoiceAttachment.put("CRAT",DateUtils.getDateTime());
						invoiceAttachment.put("CRBY",username);
						invoiceAttachmentList.add(invoiceAttachment);
					}
					
					/*InvoiceDet*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("item")) {
							
							item.add(itemCount, StrUtils.fString(fileItem.getString()).trim());
							itemCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("edit_item")) {
							
							edit_item.add(editItemCount, StrUtils.fString(fileItem.getString()).trim());
							editItemCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
							
							accountName.add(accountNameCount, StrUtils.fString(fileItem.getString()).trim());
							accountNameCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("qty")) {
							
							qty.add(qtyCount, StrUtils.fString(fileItem.getString()).trim());
							qtyCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("addonprice")) {
							
							addcost.add(addcostCount, StrUtils.fString(fileItem.getString()).trim());
							addcostCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("addontype")) {
							
							addtype.add(addtypeCount, StrUtils.fString(fileItem.getString()).trim());
							addtypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("unitcost")) {
							
							Ucost.add(UcostCount, StrUtils.fString(fileItem.getString()).trim());
							UcostCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("edit_qty")) {
							
							edit_qty.add(editQtyCount, StrUtils.fString(fileItem.getString()).trim());
							editQtyCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("cost")) {
							
							cost.add(costCount, StrUtils.fString(fileItem.getString()).trim());
							costCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("item_discount")) {
							
							detDiscount.add(detDiscountCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscountCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("item_discounttype")) {
							
							detDiscounttype.add(detDiscounttypeCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscounttypeCount++;
						}
					}
					if (fileItem.isFormField()) {
						/*if (fileItem.getFieldName().equalsIgnoreCase("tax_type")) {
							if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
							else
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}*/
						
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
							
							taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
							
							amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
							amountCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DETID")) {
							
							DETID.add(DETIDCount, StrUtils.fString(fileItem.getString()).trim());
							DETIDCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("notesexp")) {
							
							notesexp.add(notesexpcount, StrUtils.fString(fileItem.getString()).trim());
							notesexpcount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("loc")) {
							
							loc.add(locCount, StrUtils.fString(fileItem.getString()).trim());
							locCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("uom")) {
							
							uom.add(uomCount, StrUtils.fString(fileItem.getString()).trim());
							uomCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("batch")) {
							
							batch.add(batchCount, StrUtils.fString(fileItem.getString()).trim());
							batchCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("ID")) {
							
							index.add(idCount, StrUtils.fString(fileItem.getString()).trim());
							idCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
							
							tlnno.add(qtyCount, StrUtils.fString(fileItem.getString()).trim());
//							tlnnoCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("dolnno")) {
							
							dolnno.add(dolnnoCount, StrUtils.fString(fileItem.getString()).trim());
//							tlnnoCount++;
						}
					}
					
					/*others*/
					if (fileItem.getFieldName().equalsIgnoreCase("NAME")) {
						
						personIncharge = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("CTYPE")) {
						
						ctype = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FITEM")) {
						
						fitem = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FLOC")) {
						
						floc = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FLOC_TYPE_ID")) {
						
						floc_type_id = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FLOC_TYPE_ID2")) {
						
						floc_type_id2 = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FMODEL")) {
						
						fmodel = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.getFieldName().equalsIgnoreCase("FUOM")) {
						
						fuom = StrUtils.fString(fileItem.getString()).trim();
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("ORDQTY")) {
							
							ordQty.add(ordQtyCount, StrUtils.fString(fileItem.getString()).trim());
							ordQtyCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELLNO")) {
							
							delLnno.add(delLnnoCount, StrUtils.fString(fileItem.getString()).trim());
							delLnnoCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELVAL")) {
							
							delLnQty.add(delLnQtyCount, StrUtils.fString(fileItem.getString()).trim());
							delLnQtyCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELLOC")) {
							
							delLnLoc.add(delLnLocCount, StrUtils.fString(fileItem.getString()).trim());
							delLnLocCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELBATCH")) {
							
							delLnBatch.add(delLnBatchCount, StrUtils.fString(fileItem.getString()).trim());
							delLnBatchCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELUOM")) {
							
							delLnUom.add(delLnUomCount, StrUtils.fString(fileItem.getString()).trim());
							delLnUomCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DELITEM")) {
							
							delLnItem.add(delLnItemCount, StrUtils.fString(fileItem.getString()).trim());
							delLnItemCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("convcost")) {
							
							if(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())>0) //Removed By: Azees, On: 14.12.21, Desc: Zero invoice error 
							{
							Isconvcost=true;
							}
							convcost.add(costCount, StrUtils.fString(fileItem.getString()).trim());
//							convcostCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("IS_COGS_SET")) {							
							is_cogs_set.add(delLnItemCount, StrUtils.fString(fileItem.getString()).trim());
//							is_cogs_setCount++;
						}
					}
				}
				}
				
//				//imti added for (if invoice number alreday exits then it take the next invoice number from the tbl and replace the exit num with new number)
//				Hashtable ht = new Hashtable();
//				ht.put(IDBConstants.PLANT, plant);
//				ht.put(IDBConstants.INVOICE, invoiceNo);
//				if (new InvoiceDAO().isExisit(ht)) {
//					invoiceNo = new TblControlDAO().getNextOrder(plant,username,"INVOICE");
//				}
				//Get Sales Location Name
				/*
				 * if(!salesloc.isEmpty()) { ArrayList arrList = new ArrayList(); MasterDAO
				 * movHisDAO= new MasterDAO(); Hashtable htData =new Hashtable(); arrList =
				 * movHisDAO.getSalesLocationName(salesloc, plant,""); Map m = (Map)
				 * arrList.get(0); salesloc = (String) m.get("STATE");
				 * 
				 * }
				 */
				//Get Employee Code by Name
				if(!empName.isEmpty())
				{
					if(empno.isEmpty())
					{
					ArrayList arrList = new ArrayList();
					EmployeeDAO movHisDAO= new EmployeeDAO();
					Hashtable htData =new Hashtable();
					htData.put("PLANT", plant);
					htData.put("FNAME", empName);
					arrList = movHisDAO.getEmployeeDetails("EMPNO", htData,"");
					if(!arrList.isEmpty()) {
					Map m = (Map) arrList.get(0);
					empno = (String) m.get("EMPNO");
					}
					}
				}
				
				if(!dono.isEmpty()  && cmd.equals("IssueingGoodsInvoice")) {
					Collections.reverse(dolnno); 
				{
					for(int i =0 ; i < tlnno.size() ; i++){
						int InvoiceZero = i + 0;					
						int InvoiceOne = i + 1;					
						String donos = dono;
						
						Hashtable<String, String> htm = new Hashtable<>();
						htm.put(IDBConstants.PLANT, plant);
						htm.put(IDBConstants.DONO, donos);
						htm.put("ITEM", (String) item.get(InvoiceZero));
						htm.put("DOLNNO", (String) dolnno.get(InvoiceZero));
					
						List al = new DoDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", htm);
						if (al.size() > 0) {
							for (int j = 0; j < al.size(); j++) {
								Map m = (Map) al.get(j);
								String remarksval = (String) m.get("REMARKS");
							
								Hashtable<String, String> htRemarksDodet = new Hashtable<>();
								htRemarksDodet.put(IDBConstants.PLANT, plant);
								htRemarksDodet.put(IDBConstants.INVOICE, invoiceNo);
								htRemarksDodet.put(IDBConstants.LNNO, Integer.toString(InvoiceOne));
								htRemarksDodet.put("ITEM",  String.valueOf((String) item.get(i)));
								htRemarksDodet.put(IDBConstants.REMARKS, remarksval);
							
								if (!new InvoiceDAO().isExisitInvoiceMultiRemarks(htRemarksDodet)) {
									htRemarksDodet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarksDodet.put(IDBConstants.CREATED_BY, username);
									boolean insertFlag = new InvoiceUtil().saveInvoiceMultiRemarks(htRemarksDodet);
									}
								}
							}
						}
					}
				}
				if(custName.isEmpty())
					if(!custName1.isEmpty())
						custName=custName1;

				String CURRENCYID = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				if(currencyid.isEmpty())
					if(!CURRENCYID.isEmpty())
						currencyid=CURRENCYID;
				
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));
				
				if(!orderdisctype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Double.parseDouble(StrUtils.fString(orderdiscount)) / (Double.parseDouble(currencyuseqt))));
				
				//////////////////////
//				IMTHIYAS Modified on 02.03.2022
				if(COMP_INDUSTRY.equals("Education")){
					 Date dates =new Date();  
					  int year=dates.getYear();
					 int currentYear=year+1900;
					 int age = 0;
					String date = dob;
					String national = nationality;
					if(!date.equals("")){
					String[] DOBsplit = date.split("/"); 
					String DOByear = DOBsplit[2];
					int IntDobYear = Integer.parseInt(((String) DOByear.trim().toString()));
				    age = currentYear - IntDobYear; 
					}
					
					if((COMP_INDUSTRY.equals("Education") && (national.equals("Singapore Citizen") && (age >= 40)))){
						discount = "70";
						discountType = "%";
						}else if((COMP_INDUSTRY.equals("Education") && (national.equals("Singapore Citizen") && (age <= 40) && (age != 0)))){
						discount = "50";
						discountType = "%";
						}else if((COMP_INDUSTRY.equals("Education") && (national.equals("Permanent Residence")))){
						discount = "50";
						discountType = "%";
						}else if((COMP_INDUSTRY.equals("Education") && (national.equals("Foreigner")))){
						discount = "0";
						discountType = "%";
						}else if((COMP_INDUSTRY.equals("Education") && (age == 0))){
						discount = "0";
						discountType = "%";
						}
					}
				//end
				
				invoiceDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable invoiceHdr =new Hashtable(); 
				invoiceHdr.put("PLANT", plant);
				invoiceHdr.put("CUSTNO", CustCode);
				invoiceHdr.put("INVOICE", invoiceNo);
				invoiceHdr.put("GINO",gino);
				invoiceHdr.put("DONO", dono);
				invoiceHdr.put("INVOICE_DATE", invoiceDate);
				invoiceHdr.put("DUE_DATE", dueDate);
				invoiceHdr.put("PAYMENT_TERMS", payTerms);
				invoiceHdr.put("EMPNO", empno);
				invoiceHdr.put("ORDERTYPE", ORDERTYPE);
				invoiceHdr.put("ITEM_RATES", itemRates);
				invoiceHdr.put("DISCOUNT", discount);
				invoiceHdr.put("ORDER_DISCOUNT", orderdiscount);
				invoiceHdr.put("DISCOUNT_TYPE", discountType);
				invoiceHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				invoiceHdr.put("SHIPPINGCOST", shippingCost);
				invoiceHdr.put("ADJUSTMENT", adjustment);
				invoiceHdr.put("SUB_TOTAL", subTotal);
				invoiceHdr.put("TOTAL_AMOUNT", totalAmount);
				invoiceHdr.put("BILL_STATUS", invoiceStatus);
				invoiceHdr.put("NOTE", note);
				invoiceHdr.put("TERMSCONDITIONS", terms);
				invoiceHdr.put("CRAT",DateUtils.getDateTime());
				invoiceHdr.put("CRBY",username);
				invoiceHdr.put("UPAT",DateUtils.getDateTime());
				invoiceHdr.put("SALES_LOCATION", salesloc);
				invoiceHdr.put("ISEXPENSE",isexpense);
				invoiceHdr.put("TAXTREATMENT",taxtreatment);
				invoiceHdr.put("TAXAMOUNT",taxamount);
				invoiceHdr.put("SHIPPINGID",shipId);
				invoiceHdr.put("SHIPPINGCUSTOMER",shipCust);
				invoiceHdr.put("INCOTERMS",incoterm);
				invoiceHdr.put("ORIGIN",origin);
				invoiceHdr.put("DEDUCT_INV",deductInv);
				invoiceHdr.put("CURRENCYUSEQT",currencyuseqt);
				invoiceHdr.put("ORDERDISCOUNTTYPE",orderdisctype);
				invoiceHdr.put("TAXID",taxid);
				invoiceHdr.put("ISDISCOUNTTAX",discstatus);
				invoiceHdr.put("ISORDERDISCOUNTTAX",orderdiscstatus);
				invoiceHdr.put("ISSHIPPINGTAX",shipstatus);
				invoiceHdr.put("PROJECTID",projectid);
				invoiceHdr.put("TRANSPORTID",transportid);
				invoiceHdr.put("OUTBOUD_GST",gst);
				invoiceHdr.put(IDBConstants.CURRENCYID, currencyid);
				invoiceHdr.put("JobNum",jobNum);
				invoiceHdr.put("SHIPCONTACTNAME",shipcontactname);
				invoiceHdr.put("SHIPDESGINATION",shipdesgination);
				invoiceHdr.put("SHIPWORKPHONE",shipworkphone);
				invoiceHdr.put("SHIPHPNO",shiphpno);
				invoiceHdr.put("SHIPEMAIL",shipemail);
				invoiceHdr.put("SHIPCOUNTRY",shipcountry);
				invoiceHdr.put("SHIPADDR1",shipaddr1);
				invoiceHdr.put("SHIPADDR2",shipaddr2);
				invoiceHdr.put("SHIPADDR3",shipaddr3);
				invoiceHdr.put("SHIPADDR4",shipaddr4);
				invoiceHdr.put("SHIPSTATE",shipstate);
				invoiceHdr.put("SHIPZIP",shipzip);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int invoiceHdrId=0;
				BillDAO itemCogsDao=new BillDAO();
				Costofgoods costofGoods=new CostofgoodsImpl();
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!TranId.isEmpty())
					{
						Hashtable htInvHdr = new Hashtable();
						htInvHdr.put("ID",TranId);
						htInvHdr.put("PLANT", plant);
						List arrayInvHdr = invoiceUtil.getInvoiceHdrById(htInvHdr);
						Map mapInvHdr = (Map) arrayInvHdr.get(0);
						double total_amount = Double.parseDouble(mapInvHdr.get("TOTAL_AMOUNT").toString());
						double balanceAmount = Double.parseDouble(totalAmount) - total_amount;
						if(balanceAmount > 0) {
							
							Map resultMap = getOutstandingAmountForCustomer(CustCode, balanceAmount, plant);
							isAmntExceed = (boolean) resultMap.get("STATUS");
							amntExceedMsg = (String) resultMap.get("MSG");
						}else {
							isAmntExceed = false;
						}
						if(!isAmntExceed) {
							invoiceHdr.put("ID",TranId);
							invoiceHdr.put("UPBY",username);
							invoiceHdrId = invoiceUtil.updateInvoiceHdr(invoiceHdr);
						}
					}
				}
				else {	
					Map resultMap = getOutstandingAmountForCustomer(CustCode, Double.parseDouble(totalAmount), plant);
					isAmntExceed = (boolean) resultMap.get("STATUS");
					amntExceedMsg = (String) resultMap.get("MSG");
					if(!isAmntExceed) {
						invoiceHdr.put("CREDITNOTESSTATUS","0");
						invoiceHdr.put("TOTAL_PAYING","0");
						invoiceHdrId = invoiceUtil.addInvoiceHdr(invoiceHdr, plant);
					}
				}
				
				if(invoiceHdrId > 0) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						String convDiscount=""; 
						String convCost = String.valueOf((Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
						if(Isconvcost)
							convCost = String.valueOf((Double.parseDouble((String) convcost.get(i)) / Double.parseDouble(currencyuseqt)));
						if(COMP_INDUSTRY.equals("Education")){
							detDiscounttype.add("SGD");
							detDiscount.add("0.00");
						}
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i)) / Double.parseDouble(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));
						
						invoiceDetInfo = new Hashtable<String, String>();
						invoiceDetInfo.put("PLANT", plant);
						invoiceDetInfo.put("LNNO", Integer.toString(lnno));
						if(cmd.equalsIgnoreCase("Edit"))
							invoiceDetInfo.put("INVOICEHDRID", TranId);						
						else
							invoiceDetInfo.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
						//invoiceDetInfo.put("INVOICEHDRID", TranId);							
						invoiceDetInfo.put("ITEM", (String) item.get(i));
						invoiceDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						invoiceDetInfo.put("QTY", (String) qty.get(i));
						invoiceDetInfo.put("UNITPRICE", convCost);
						invoiceDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						invoiceDetInfo.put("AMOUNT", convAmount);
						invoiceDetInfo.put("CRAT",DateUtils.getDateTime());
						invoiceDetInfo.put("CRBY",username);
						invoiceDetInfo.put("UPAT",DateUtils.getDateTime());
						invoiceDetInfo.put("DISCOUNT", convDiscount);
						invoiceDetInfo.put("ADDONAMOUNT", (String)addcost.get(i));
						invoiceDetInfo.put("ADDONTYPE", (String)addtype.get(i));
						double uconv = ((Double.valueOf((String)Ucost.get(i)))/(Double.valueOf(currencyuseqt)));
						invoiceDetInfo.put("UNITCOST", (String.valueOf(uconv)));
						invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						if(!dono.equalsIgnoreCase("") && !gino.equalsIgnoreCase("")){
//							if(deductInv.equalsIgnoreCase("1") && !dono.equalsIgnoreCase("") && !gino.equalsIgnoreCase("")){
							if(uom.size() > 0)
							invoiceDetInfo.put("UOM", (String) uom.get(i));
						}else if(!gino.equalsIgnoreCase("")){
//						}else if(deductInv.equalsIgnoreCase("1") && !gino.equalsIgnoreCase("")){
							if(uom.size() > 0)
								invoiceDetInfo.put("UOM", (String) uom.get(i));
						}else {
							invoiceDetInfo.put("UOM", "");	
						}
						invoiceDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						if(cmd.equalsIgnoreCase("Edit")) {
							if(((String) is_cogs_set.get(i)).equalsIgnoreCase("Y")) {
								if(((String)qty.get(i)).equalsIgnoreCase((String)edit_qty.get(i)) && 
										((String)item.get(i)).equalsIgnoreCase((String)edit_item.get(i))) {
									invoiceDetInfo.put("IS_COGS_SET", (String) is_cogs_set.get(i));
								}else {
									invoiceDetInfo.put("IS_COGS_SET", "N");
								}
							}
						}
						if(loc.size() > 0) {
							invoiceDetInfo.put("LOC", (String) loc.get(i));
							
							invoiceDetInfo.put("BATCH", (String) batch.get(i));
						}
						if(notesexp.size() > 0) {
							invoiceDetInfo.put("NOTE", (String) notesexp.get(i));
						}else {
							/*
							 * invoiceDetInfo.put("DISCOUNT", (String) detDiscount.get(i));
							 * invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
							 */
						}
						invoiceDetInfoList.add(invoiceDetInfo);
						
						 if(cmd.equalsIgnoreCase("Edit")) {
								 int cogsCnt=itemCogsDao.addItemCogs(costofGoods.revisedSoldProductDetails((String)qty.get(i), (String)edit_qty.get(i), (String)item.get(i), (String)edit_item.get(i), plant, dueDate),plant);
								 System.out.println("Insert ItemCogs Status :"+ cogsCnt); 
						 }else {
							 int cogsCnt=itemCogsDao.addItemCogs(costofGoods.soldProductDetails((String)qty.get(i), (String)item.get(i), plant, dueDate),plant);
							 System.out.println("Insert ItemCogs Status :"+ cogsCnt);
						 }
						
						
					}
					if(cmd.equalsIgnoreCase("Edit"))
					{
						if(!TranId.isEmpty())
						{
							invoiceDetInfo.put("UPBY",username);
							isAdded = invoiceDAO.deleteInvoiceDet(plant, TranId);
							if(isAdded)
							isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);
						}
					}
					else
					isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);
					int attchSize = invoiceAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						invoiceAttachment = new Hashtable<String, String>();
						invoiceAttachment = invoiceAttachmentList.get(i);
						if(cmd.equalsIgnoreCase("Edit"))
						invoiceAttachment.put("INVOICEHDRID", TranId);
						else
							invoiceAttachment.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
						invoiceAttachmentInfoList.add(invoiceAttachment);
					}
					if(isAdded) {
						if(invoiceAttachmentInfoList.size() > 0)
							isAdded = invoiceUtil.addInvoiceAttachments(invoiceAttachmentInfoList, plant);
						
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						System.out.println("invoice Status"+invoiceStatus);
						if(!invoiceStatus.equalsIgnoreCase("Draft"))
						{
							//Journal Entry
							JournalHeader journalHead=new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(invoiceDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("INVOICE");
							journalHead.setTRANSACTION_ID(invoiceNo);
							journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
							//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);
							
							List<JournalDetail> journalDetails=new ArrayList<>();
							CoaDAO coaDAO=new CoaDAO();
							CustMstDAO cusDAO=new CustMstDAO();
							ItemMstDAO itemMstDAO=new ItemMstDAO();
							Double totalItemNetWeight=0.00;
							Double totalCostofGoodSold=0.00;
							
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) CustCode);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject CusJson=cusDAO.getCustomerName(plant, (String) CustCode);
								if(!CusJson.isEmpty()) {
									coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CNAME"));
									if(coaJson1.isEmpty() || coaJson1.isNullObject())
									{
										coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CUSTNO")+"-"+CusJson.getString("CNAME"));
									}
								}
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								//journalDetail_1.setACCOUNT_NAME((String) CustCode);
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_1);
							}
							
							if(discount.isEmpty())
							{
								discount="0.00";
							}
							Double discountFrom = Double.parseDouble(discount);
							Double orderDiscountFrom=0.00;
							if(!orderdiscount.isEmpty())
							{
								orderDiscountFrom=Double.parseDouble(orderdiscount);
								
								if(orderdisctype.toString().equalsIgnoreCase("%"))
									orderdiscount=Double.toString((Double.parseDouble(subTotal)*orderDiscountFrom)/100);
							}
							if(discountFrom>0 || orderDiscountFrom>0)
							{
								if(!discountType.isEmpty())
								{
									if(discountType.equalsIgnoreCase("%"))
									{
										Double subTotalAfterOrderDiscount=Double.parseDouble(subTotal);
										discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
									}
								}
								//discountFrom=discountFrom+orderDiscountFrom;
								JournalDetail journalDetail_3=new JournalDetail();
								journalDetail_3.setPLANT(plant);
								JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discounts given - COS");
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
								journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
								journalDetail_3.setDEBITS(discountFrom);
								journalDetails.add(journalDetail_3);
							}
							
							for(Map invDet:invoiceDetInfoList)
							{
								Double quantity=Double.parseDouble(invDet.get("QTY").toString());
								String netWeight=itemMstDAO.getItemNetWeight(plant, invDet.get("ITEM").toString());
								if(netWeight!=null && !"".equals(netWeight))
								{
									Double Netweight=quantity*Double.parseDouble(netWeight);
									totalItemNetWeight+=Netweight;
								}
								
								
								System.out.println("TotalNetWeight:"+totalItemNetWeight);
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) invDet.get("ACCOUNT_NAME"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) invDet.get("ACCOUNT_NAME"));
								//double discpercal = (Double.parseDouble(invDet.get("AMOUNT").toString()) * 100)/Double.parseDouble(subTotal);
								//double deductamt = (Double.parseDouble(orderdiscount.toString())/100)*discpercal;
								//journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())- deductamt);
								
								if(!orderdisctype.toString().equalsIgnoreCase("%")) {
									journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())-Double.parseDouble(orderDiscountFrom.toString())/invoiceDetInfoList.size());
								}else {
									Double jodamt = (Double.parseDouble(invDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderDiscountFrom.toString());
									journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString()) -jodamt);
								}
								
								/*
								 * String avg=new InvUtil().getCostOfGoods((String)invDet.get("ITEM"),plant);
								 * if(avg!=null && !"".equals(avg)) {
								 * totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }else { avg=new
								 * InvUtil().getAvgCostofItem((String)invDet.get("ITEM"),plant); if(avg!=null &&
								 * !"".equals(avg)) { totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }
								 * 
								 * }
								 */
								/**/
								int invoicesCount = new InvoiceDAO().invoiceWoCOGSCount((String)invDet.get("ITEM"), plant);
								if(invoicesCount == 1) {
									Map invDetail = new InvMstDAO().getInvDataByProduct((String)invDet.get("ITEM"), plant);
									double bill_qty = 0, invoiced_qty = 0;//inv_qty=0, unbill_qty = 0, 
//									inv_qty = Double.parseDouble((String)invDetail.get("INV_QTY"));
									bill_qty = Double.parseDouble((String)invDetail.get("BILL_QTY"));
//									unbill_qty = Double.parseDouble((String)invDetail.get("UNBILL_QTY"));
									invoiced_qty = Double.parseDouble((String)invDetail.get("INVOICE_QTY"));
									
									bill_qty = bill_qty - invoiced_qty;
									
									if(bill_qty >= quantity) {
								/**/
									ArrayList invQryList;
									Hashtable ht_cog = new Hashtable();
									ht_cog.put("a.PLANT",plant);
									ht_cog.put("a.ITEM",(String)invDet.get("ITEM"));
									invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
									if(invQryList.isEmpty())
									{
										invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
									}
									if(invQryList!=null)
									{
										if(!invQryList.isEmpty())
										{
											Map lineArr = (Map) invQryList.get(0);
											String avg= StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("AVERAGE_COST")),"2");
											totalCostofGoodSold+=Double.parseDouble(avg)*(quantity);
										}
									}
									new InvoiceDAO().update_is_cogs_set((String)invDet.get("INVOICEHDRID"), (String)invDet.get("LNNO"), (String)invDet.get("ITEM"), plant);
									}
								}
								//totalCostofGoodSold+=Double.parseDouble(invDet.get("AMOUNT").toString());
								boolean isLoop=false;
								if(journalDetails.size()>0)
								{
									int i=0;
									for(JournalDetail journal:journalDetails) {
										int accountId=journal.getACCOUNT_ID();
										if(accountId==journalDetail.getACCOUNT_ID()) {
											isLoop=true;
											Double sumDetit=journal.getCREDITS()+journalDetail.getCREDITS();
											journalDetail.setCREDITS(sumDetit);
											journalDetails.set(i, journalDetail);
											break;
										}
										i++;
										
									}
									if(isLoop==false) {
										journalDetails.add(journalDetail);
									}
								
								}
								else
								{
									journalDetails.add(journalDetail);
								}
							}
							
							if(!shippingCost.isEmpty())
							{
								Double shippingCostFrom=Double.parseDouble(shippingCost);
								if(shippingCostFrom>0)
								{
									JournalDetail journalDetail_4=new JournalDetail();
									journalDetail_4.setPLANT(plant);
									JSONObject coaJson4=coaDAO.getCOAByName(plant, "Outward freight & shipping");
									journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
									journalDetail_4.setACCOUNT_NAME("Outward freight & shipping");
									journalDetail_4.setCREDITS(shippingCostFrom);
									journalDetails.add(journalDetail_4);
								}
							}
							
							if(taxamount.isEmpty())
							{
								taxamount="0.00";
							}
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Output");*/
								
								MasterDAO masterDAO = new MasterDAO();
								String planttaxtype = masterDAO.GetTaxType(plant);
								
								if(planttaxtype.equalsIgnoreCase("TAX")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}else if(planttaxtype.equalsIgnoreCase("GST")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Payable");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("GST Payable");
								}else if(planttaxtype.equalsIgnoreCase("VAT")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}else {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}
								
								journalDetail_2.setCREDITS(taxAmountFrom);
								journalDetails.add(journalDetail_2);
							}
							
							if(!adjustment.isEmpty())
							{
								Double adjustFrom=Double.parseDouble(adjustment);
								if(adjustFrom>0)
								{
									JournalDetail journalDetail_7=new JournalDetail();
									journalDetail_7.setPLANT(plant);
									JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
									journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
									journalDetail_7.setACCOUNT_NAME("Adjustment");
									journalDetail_7.setCREDITS(adjustFrom);
									journalDetails.add(journalDetail_7);
								}
								else if(adjustFrom<0)
								{
									JournalDetail journalDetail_7=new JournalDetail();
									journalDetail_7.setPLANT(plant);
									JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
									journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
									journalDetail_7.setACCOUNT_NAME("Adjustment");
									adjustFrom=Math.abs(adjustFrom);
									journalDetail_7.setDEBITS(adjustFrom);
									journalDetails.add(journalDetail_7);
								}
							}
							Journal journal=new Journal();
							Double totalDebitAmount=0.00;
							for(JournalDetail jourDet:journalDetails)
							{
								 totalDebitAmount=totalDebitAmount+jourDet.getDEBITS();
							}
							journalHead.setTOTAL_AMOUNT(totalDebitAmount);
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalFrom.getJournalHeader()!=null)
							{
								if(journalFrom.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalFrom.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
									
									Hashtable htMovHis = new Hashtable();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									htMovHis.put(IDBConstants.ITEM, "");
									htMovHis.put(IDBConstants.QTY, "0.0");
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									htMovHis.put(IDBConstants.CREATED_BY, username);		
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMovHis.put("REMARKS","");
									isAdded = movHisDao.insertIntoMovHis(htMovHis);
								}
								else
								{
									journalService.addJournal(journal, username);
									
									Hashtable htMovHis = new Hashtable();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									htMovHis.put(IDBConstants.ITEM, "");
									htMovHis.put(IDBConstants.QTY, "0.0");
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									htMovHis.put(IDBConstants.CREATED_BY, username);		
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMovHis.put("REMARKS","");
									isAdded = movHisDao.insertIntoMovHis(htMovHis);
								}
								
								//Cost of goods sold
								if(totalCostofGoodSold>0)
								{
									journalDetails.clear();
									journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
									JournalDetail journalDetail_InvAsset=new JournalDetail();
									journalDetail_InvAsset.setPLANT(plant);
									JSONObject coaJson7=coaDAO.getCOAByName(plant, "Inventory Asset");
									System.out.println("Json"+coaJson7.toString());
									journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
									journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
									journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
									journalDetails.add(journalDetail_InvAsset);
									
									JournalDetail journalDetail_COG=new JournalDetail();
									journalDetail_COG.setPLANT(plant);
									JSONObject coaJson8=coaDAO.getCOAByName(plant, "Cost of goods sold");
									System.out.println("Json"+coaJson8.toString());
									journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
									journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
									journalDetail_COG.setDEBITS(totalCostofGoodSold);
									journalDetails.add(journalDetail_COG);
									
									journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);
									
									Journal journalCOG=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
									if(journalCOG.getJournalHeader()!=null)
									{
										if(journalCOG.getJournalHeader().getID()>0)
										{
											journalHead.setID(journalCOG.getJournalHeader().getID());
											journalService.updateJournal(journal, username);
											Hashtable htMovHis = new Hashtable();
											htMovHis.put(IDBConstants.PLANT, plant);
											htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
											htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
											htMovHis.put(IDBConstants.ITEM, "");
											htMovHis.put(IDBConstants.QTY, "0.0");
											htMovHis.put("RECID", "");
											htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
											htMovHis.put(IDBConstants.CREATED_BY, username);		
											htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htMovHis.put("REMARKS","");
											isAdded = movHisDao.insertIntoMovHis(htMovHis);
										}
										else
										{
											journalService.addJournal(journal, username);
											Hashtable htMovHis = new Hashtable();
											htMovHis.put(IDBConstants.PLANT, plant);
											htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
											htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
											htMovHis.put(IDBConstants.ITEM, "");
											htMovHis.put(IDBConstants.QTY, "0.0");
											htMovHis.put("RECID", "");
											htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
											htMovHis.put(IDBConstants.CREATED_BY, username);		
											htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htMovHis.put("REMARKS","");
											isAdded = movHisDao.insertIntoMovHis(htMovHis);
										}
									}
								}
							}
							
						}
					}
					
					
					if(isexpense.equals("1")) {
						if(cmd.equalsIgnoreCase("Edit")) {
							
						}else {
							ExpensesUtil expUtil = new ExpensesUtil();
							Hashtable expenseHdr =new Hashtable(); 
							expenseHdr.put("PLANT", plant);
							expenseHdr.put("STATUS", "BILLED");
							expenseHdr.put("ID",TranId);
							expenseHdr.put("UPBY",username);
							expUtil.updateExpensesHdrstatus(expenseHdr);
						}
							
					}
					if(isAdded) {
						if(!deductInv.equalsIgnoreCase("1") && !isexpense.equals("1") && !gino.equalsIgnoreCase("")) {
							Hashtable htgi = new Hashtable();	
							String sqlgi = "UPDATE "+ plant+"_FINGINOTOINVOICE SET STATUS='INVOICED' WHERE PLANT='"+ plant+"' AND GINO='"+gino+"'";
							invoiceDAO.updategino(sqlgi, htgi, "");
						}
					}
					
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit"))
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);	
						else
						{
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_INVOICE);
							if(isexpense.equalsIgnoreCase("1"))
								htMovHis.put("DIRTYPE", TransactionConstants.EXPENSES_TO_INVOICE);
							if(cmd.equalsIgnoreCase("IssueingGoodsInvoice"))
								htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_INVOICE);
						}
						
							
//						htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(invoiceDate));														
						htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
						String billqty = String.valueOf((String) qty.get(i));
						htMovHis.put(IDBConstants.QTY, billqty);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						if(!jobNum.isEmpty())
							htMovHis.put("REMARKS",jobNum);
						else
							htMovHis.put("REMARKS",dono);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, plant);
						htMovChk.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);
						htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
						htMovChk.put(IDBConstants.QTY, billqty);
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE%' ");
						if(!isAdded)	
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
				}
				
				/* Added by Abhilash to deduct from inventory*/

				if(delLnno.size() > 0 && deductInv.equalsIgnoreCase("1")) {
					for(int i =0 ; i < delLnno.size() ; i++){
						int lnno = Integer.parseInt((String)delLnno.get(i));
						String ITEM = "",UOM = "", QTY = "", LOC = "", BATCH = "";
						ITEM = (String)delLnItem.get(i);
						UOM = (String)delLnUom.get(i);
						QTY = (String)delLnQty.get(i);
						LOC = (String)delLnLoc.get(i);
						BATCH = (String)delLnBatch.get(i);
						
						Map invmap = null;
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails(UOM, plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap = new HashMap();
						invmap.put(IConstants.PLANT, plant);
						invmap.put(IConstants.ITEM, ITEM);
						invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, ITEM));
						invmap.put("INVOICE", invoiceNo);
						invmap.put(IConstants.DODET_DONUM, "");
						invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
						invmap.put(IConstants.CUSTOMER_NAME, CustCode);
						invmap.put(IConstants.LOC, LOC);			
						invmap.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, CustCode);
						invmap.put(IConstants.BATCH, BATCH);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put("ISSUEDQTY", QTY);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						if (invoiceDate.length()>5)
							strTranDate    = invoiceDate.substring(6)+"-"+ invoiceDate.substring(3,5)+"-"+invoiceDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put("RETURN_DATE", strTranDate);
						invmap.put("UOMQTY", uomQty);
						invmap.put("NOTES", "");
						invmap.put("SORETURN", "");
						invmap.put("GINO", gino);
						invmap.put(IConstants.CUSTOMER_NAME, CustCode);
						isAdded = processShipHisReversal(invmap);
						if(isAdded) {
							processInvAdd(invmap);
						}
						if(isAdded) {
							processMovHis_IN(invmap);
						}
						if (isAdded == true) {//Shopify Inventory Update
		   					Hashtable htCond = new Hashtable();
		   					htCond.put(IConstants.PLANT, plant);
		   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
		   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,ITEM);						
								if(nonstkflag.equalsIgnoreCase("N")) {
		   						String availqty ="0";
		   						ArrayList invQryList = null;
		   						htCond = new Hashtable();
		   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,ITEM, new ItemMstDAO().getItemDesc(plant, ITEM),htCond);						
		   						if(new PlantMstDAO().getisshopify(plant)) {
		   						if (invQryList.size() > 0) {					
		   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		   								//String result="";
		                                Map lineArr = (Map) invQryList.get(iCnt);
		                                availqty = (String)lineArr.get("AVAILABLEQTY");
		                                System.out.println(availqty);
		   							}
		   							double availableqty = Double.parseDouble(availqty);
		       						new ShopifyService().UpdateShopifyInventoryItem(plant, ITEM, availableqty);
		   						}
								}
								}
		   					}
		   				}
					}
				}
				process: 
				if(isAdded && invoiceStatus.equalsIgnoreCase("Open") && deductInv.equalsIgnoreCase("1")) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
						htCondiShipHis.put("INVOICENO", gino);
						htCondiShipHis.put("STATUS","C");
						htCondiShipHis.put("BATCH",(String) batch.get(i));
						htCondiShipHis.put("dolno", Integer.toString(lnno));
						htCondiShipHis.put("LOC1", (String) loc.get(i));
						htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
						boolean isexists = new ShipHisDAO().isExisit(htCondiShipHis, "");
						
						double costdouble = Double.valueOf((String) cost.get(i))/Double.valueOf(currencyuseqt);
						String scost = String.valueOf(costdouble);
						
						if(isexists){
					        String query = "set UNITPRICE=" + scost + ",ORDQTY=" + (String) qty.get(i);
					        isAdded = new ShipHisDAO().updateShipHis(query,htCondiShipHis,"");
			            }
						
						/**/
						String QTY = "", ORDQTY = "";
						boolean processInv = false;
						if(ordQty.size() == 0) {
							QTY = (String) qty.get(i);
							ORDQTY = (String) qty.get(i);
							processInv = true;
						}else {
							if(ordQty.size()> i) {
								ORDQTY = (String) qty.get(i);
								double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
								double i_Qty = Double.parseDouble((String) qty.get(i));
								i_Qty = i_Qty - i_OrdQty;							
								QTY = Double.toString(i_Qty);
								if(i_Qty > 0) {
									processInv = true;
								}
							}else {
								QTY = (String) qty.get(i);
								ORDQTY = (String) qty.get(i);
								processInv = true;
							}
						}
						/**/
					
					if(processInv) { 
					String ITEM_QTY="";
					Map invmap = new HashMap();
						String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {
							List itemQry = new InvMstDAO().getOutBoundPickingBatchByWMS(plant,(String) item.get(i), (String) loc.get(i), (String) batch.get(i));
							double invqty = 0;
						      double Detuctqty = 0;
								double STKQTY = 0;
								Double pickqty = Double.parseDouble(QTY);
							if (itemQry.size() > 0) {
								for (int j = 0; j < itemQry.size(); j++) {
									Map m = (Map) itemQry.get(j);
									ITEM_QTY = (String) m.get("qty");
									String MINSTKQTY = (String) m.get("MINSTKQTY");
									invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
									STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
								}
								
								if(STKQTY!=0) {
									Detuctqty = invqty-pickqty;
									if(STKQTY>Detuctqty) {
										if(alertitem.equalsIgnoreCase("")) {
											alertitem =(String)item.get(i);
										}else {
											alertitem = alertitem+" , "+(String)item.get(i);
										}
									}
										
									}
								if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
									alertitem = alertitem;
								else 
									alertitem="";
								
								/*double calinqty = pickingQty * Double.valueOf(UOMQTY);*/
								double pickingQty = Double.parseDouble(((String) qty.get(i)));
								if(invqty < pickingQty){
									throw new Exception(
										"Not enough inventory found for ProductID/Batch for Order Line No "+lnno+" in the location selected");
								}						
							} else {
								throw new Exception(
									"Not enough inventory found for ProductID/Batch for Order Line No "+lnno+" in the location selected");
								
							}
						}
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails((String) uom.get(i), plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap.put(IDBConstants.PLANT, plant);
						invmap.put(IConstants.DODET_DONUM, "");
						invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
						invmap.put(IConstants.CUSTOMER_NAME, CustCode);
						invmap.put(IDBConstants.ITEM, (String) item.get(i));
						invmap.put(IDBConstants.ITEM_DESC, (String) item.get(i));
						invmap.put(IDBConstants.LOC, (String) loc.get(i));
						invmap.put(IDBConstants.USERFLD4, (String) batch.get(i));
						invmap.put(IConstants.BATCH, (String) batch.get(i));						
						
						invmap.put(IConstants.ORD_QTY, ORDQTY);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IDBConstants.CURRENCYID, (String) request.getSession().getAttribute("BASE_CURRENCY"));
						invmap.put("UNITPRICE", scost);
						invmap.put(IConstants.ISSUEDATE, invoiceDate);
						invmap.put(IConstants.INVOICENO, invoiceNo);
						invmap.put(IConstants.UOM, (String) uom.get(i));
						invmap.put("GINO", gino);
						invmap.put("UOMQTY", uomQty);						
						if (invoiceDate.length()>5)
							strTranDate    = invoiceDate.substring(6)+"-"+ invoiceDate.substring(3,5)+"-"+invoiceDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						isAdded = processShipHis(invmap);
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {
							if(isAdded) {
								isAdded = processInvRemove(invmap);
							}
							if(isAdded) {
								processBOM(invmap);
							}
							if (isAdded == true) {//Shopify Inventory Update
			   					Hashtable htCond = new Hashtable();
			   					htCond.put(IConstants.PLANT, plant);
			   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
			   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));						
									if(nonstkflag.equalsIgnoreCase("N")) {
			   						String availqty ="0";
			   						ArrayList invQryList = null;
			   						htCond = new Hashtable();
			   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,(String) item.get(i), new ItemMstDAO().getItemDesc(plant, (String) item.get(i)),htCond);						
			   						if(new PlantMstDAO().getisshopify(plant)) {
			   						if (invQryList.size() > 0) {					
			   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
			   								//String result="";
			                                Map lineArr = (Map) invQryList.get(iCnt);
			                                availqty = (String)lineArr.get("AVAILABLEQTY");
			                                System.out.println(availqty);
			   							}
			   							double availableqty = Double.parseDouble(availqty);
			       						new ShopifyService().UpdateShopifyInventoryItem(plant, (String) item.get(i), availableqty);
			   						}	
									}
									}
			   					}
			   				}
						}
						if(isAdded) {
							processMovHis_OUT(invmap);
						}
						if(!isAdded) {
							break process;
						}
					}
				}
			}
				if(isAdded) {/* Remove Items from Estimate list */
		    		for(int i=0; i<idCount; i++) {
		     		   if(i==0)
		     			   listQry.remove(Integer.parseInt((String)index.get(i)));
		     		   else
		     			   listQry.remove(Integer.parseInt((String)index.get(i-i)));
		     	   }
		    	}
				
				//azees consignmentToInvoice Status & Qty Update
				if(!cmd.equalsIgnoreCase("Edit"))
				{
				if(jobNum.contains("C")) {
				for(int i=0;i<item.size();i++) {
					
//					int lnv=i+1;
				ToDetDAO _ToDetDAO = new ToDetDAO();
				ToHdrDAO _ToHdrDAO = new ToHdrDAO();
				String updateEstHdr = "",updateEstDet="";
				Hashtable htCondition = new Hashtable();
				htCondition.put("PLANT", plant);
				htCondition.put("TONO", jobNum);
				htCondition.put("TOLNNO", String.valueOf(tlnno.get(i)));
				int tllno=Integer.parseInt(((String)tlnno.get(i)));
				String issuingQty = (String)qty.get(i);
				ToDet toDet = new ToDet();
				toDet = _ToDetDAO.getToDetById(plant, jobNum, tllno, (String)item.get(i));
				
				//String issuedqty = (String)qtyIs.get(index);
				BigDecimal Ordqty = toDet.getQTYOR();
				BigDecimal tranQty = BigDecimal.valueOf(Double.parseDouble(issuingQty));
				BigDecimal issqty = BigDecimal.valueOf(Double.parseDouble("0"));
						if (toDet.getQTYAC() != null) {
							issqty =toDet.getQTYAC();
						}
				BigDecimal sumqty = issqty.add(tranQty);
				//sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
				
				String extraCond = " AND  QtyOr >= isNull(QTYAC,0) + "+ issuingQty;
				if (Ordqty.compareTo(sumqty) == 0) {
					updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='C' ";

				} else {
					updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='O' ";
				
				}
			
			boolean	insertFlag = _ToDetDAO.update(updateEstDet, htCondition, extraCond);
					
				if (insertFlag) {
					htCondition.remove("TOLNNO");
					
					insertFlag = _ToDetDAO.isExisit(htCondition,"RECSTAT in ('O','N')");
					if (!insertFlag){
						updateEstHdr = "set  RECSTAT='C',ORDER_STATUS='INVOICED' ";
						insertFlag = _ToHdrDAO.update(updateEstHdr, htCondition, "");
					}					
						
				
				}
				}
				}
				}
				
				if(isAdded) {
					DbBean.CommitTran(ut);
					if(cmd.equalsIgnoreCase("Edit"))
						result = "Invoice updated successfully";
					else
					{
						ShipHisDAO shiphstdao = new ShipHisDAO();
						Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
						htTrandId1.put(IConstants.INVOICENO, invoiceNo);
						htTrandId1.put(IConstants.PLANT, plant);
//						boolean flag = 
								shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS	 						
						/*if(flag)
						{*/
						new TblControlUtil().updateTblControlIESeqNo(plant, "INVOICE", "IN", invoiceNo);
						/*}*/
						if(deductInv.equalsIgnoreCase("1")) {
							new TblControlUtil().updateTblControlIESeqNo(plant, "GINO", "GI", gino);
						}
						if(alertitem.equalsIgnoreCase("")) {
							result = "Invoice created successfully";
						}else {
							result = "Invoice created successfully <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>";
						}
//						result = "Invoice created successfully";
					}
						
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Invoice not created";
				}
				
				if(result.equalsIgnoreCase("Invoice not created"))
				{
					if (ajax) {
						JSONObject resultJson = new JSONObject();
						resultJson.put("MESSAGE", result);
						resultJson.put("ERROR_CODE", "98");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						DOUtil _DOUtil = new DOUtil();
						if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")) {
							response.sendRedirect("jsp/IssueingGoodsInvoice.jsp?DONO="
									+dono+"&invcusnum="+custName
									+"&invcuscode="+CustCode
									+"&invnum="+invoiceNo
									+"&CUST_CODE="+_DOUtil.getCustCode(plant, dono)
									+"&cmd=IssueingGoodsInvoice&result="+ result);
						}else if(isAmntExceed) {
							if(cmd.equalsIgnoreCase("Edit")) {
								response.sendRedirect("jsp/IssueingGoodsInvoice.jsp?DONO="
										+dono+"&invcusnum="+custName
										+"&TRANID="+TranId
										+"&invnum="+invoiceNo
										+"&cmd=Edit"
										+"&CUST_CODE="+_DOUtil.getCustCode(plant, dono)
										+"&result="+ amntExceedMsg);
							}else {
								response.sendRedirect("../invoice/new?result="+ result+" "+amntExceedMsg);
							}
						}else if(cmd.equalsIgnoreCase("conToinvc")) {
							response.sendRedirect("jsp/convertToInvoice.jsp?DONO="
									+dono+"&invcusnum="+custName
									+"&TRANID="+TranId
									+"&invnum="+invoiceNo
									+"&cmd=conToinvc"
									+"&CUST_CODE="+CustCode
									+"&CUST_NAME="+custName
									+"&TAXTREATMENT="+taxtreatment
									+"&NAME="+personIncharge
									+"&CTYPE="+ctype
									+"&FITEM="+fitem
									+"&FLOC="+floc
									+"&FLOC_TYPE_ID="+floc_type_id
									+"&FLOC_TYPE_ID2="+floc_type_id2
									+"&FMODEL="+fmodel
									+"&FUOM="+fuom
									+"&result="+ result);					
						}else {
							response.sendRedirect("../invoice/new?result="+ result);
						}
					}
				}
				else {
					if (ajax) {
						try {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
							if ("invoice".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								invoicePDFGenration(request, response, invoiceNo, "Edit".equalsIgnoreCase(cmd) ? Integer.parseInt(TranId) : invoiceHdrId);
							}
							JSONObject resultJson = new JSONObject();
							resultJson.put("MESSAGE", result);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}catch(Exception e) {
							e.printStackTrace();
							JSONObject resultJson = new JSONObject();
							resultJson.put("MESSAGE", "Invoice Added Successfully and email not sent");
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
							
						}
					}else {
						response.sendRedirect("../invoice/summary?result="+ result);/* Redirect to Invoice Summary page */
					}
				}
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 if (ajax) {
					 JSONObject resultJson = new JSONObject();
					resultJson.put("MESSAGE", "Invoice not created. " + ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }else {
					 if(cmd.equalsIgnoreCase("IssueingGoodsInvoice"))
							response.sendRedirect("jsp/IssueingGoodsInvoice.jsp?DONO="
									+dono+"&invcusnum="+custName
									+"&invcuscode="+CustCode
									+"&invnum="+invoiceNo
									+"&CUST_CODE="+CustCode
									+"&cmd=IssueingGoodsInvoice&result="+ e.getLocalizedMessage());
						else if(cmd.equalsIgnoreCase("Edit"))
							response.sendRedirect("jsp/IssueingGoodsInvoice.jsp?DONO="
									+dono+"&invcusnum="+custName
									+"&TRANID="+TranId
									+"&invnum="+invoiceNo
									+"&cmd=Edit"
									+"&CUST_CODE="+CustCode
									+"&result="+ ThrowableUtil.getMessage(e));
						else if(cmd.equalsIgnoreCase("conToinvc"))
							response.sendRedirect("jsp/convertToInvoice.jsp?DONO="
									+dono+"&invcusnum="+custName
									+"&TRANID="+TranId
									+"&invnum="+invoiceNo
									+"&cmd=conToinvc"
									+"&CUST_CODE="+CustCode
									+"&CUST_NAME="+custName
									+"&TAXTREATMENT="+taxtreatment
									+"&NAME="+personIncharge
									+"&CTYPE="+ctype
									+"&FITEM="+fitem
									+"&FLOC="+floc
									+"&FLOC_TYPE_ID="+floc_type_id
									+"&FLOC_TYPE_ID2="+floc_type_id2
									+"&FMODEL="+fmodel
									+"&FUOM="+fuom
									+"&result="+ ThrowableUtil.getMessage(e));
						else
							response.sendRedirect("../invoice/new?result="+ e.getMessage());
				 }
			}			
		
		}else if (action.equalsIgnoreCase("GET_BILL_NO_FOR_AUTO_SUGGESTION")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_INVOICE_NO_FOR_AUTO_SUGGESTION")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceNoForAutoSuggestionreturn(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_INVOICE_NO_FOR_AUTO_SUGGESTION_DNPL")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getInvoiceNoForAutoSuggestionreturndnpl(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}
		
		
		else if (action.equalsIgnoreCase("GET_GI_NO_FOR_AUTO_SUGGESTION")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getginoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}
		
		else if (action.equalsIgnoreCase("GET_GI_NO_FOR_AUTO_SUGGESTION_DNPL")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getginoForAutoSuggestiondnpl(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equals("GET_ORDERNO_FOR_AUTO_SUGGESTION")) {
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	
		}
		
		else if (baction.equals("VIEW_INVOICE_DETAILS_SUMMARY")) {	    	  
	        jsonObjectResult = this.getinvoicedetailsview(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	       	       
	    }else if (baction.equals("ExportExcelInvoiceDetails")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelInvoiceDetails(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=invoice_details.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("VIEW_CUSTOMER_BALANCES")) {	    	  
	        jsonObjectResult = this.getCustomerBalances(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	       	       
	    }else if (baction.equals("ExportExcelCustomerbalances")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelCustomerBalances(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=customer_balances.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (action.equalsIgnoreCase("InvoiceImportTemplate")) {
			try {
				//invoiceImportTemplate(request, response);
				InvoiceDataImportTemplate(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * }else if (action.equalsIgnoreCase("importCountSheet")) { try {
			 * onImportCountSheet(request, response); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
		}else if (action.equalsIgnoreCase("confirmCountSheet")) {
			try {
				onConfirmInvoiceSheet(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("ImportInvoiceTemplate")) {
			try {
				invoiceImportTemplate(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
		}
	}

	private void invoicePDFGenration(HttpServletRequest request, HttpServletResponse response, String invoiceNo,
			int invoiceHdrId) throws IOException{
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (ajax) {
			HttpSession session = ((HttpServletRequest) request).getSession(false);
			String rootURI = HttpUtils.getRootURI(request);
			String invoiceDetailURL = rootURI + "/invoice/detail?INVOICE_HDR=" + invoiceHdrId + "&PLANT="
					+ StrUtils.fString((String) session.getAttribute("PLANT")) + "&SYSTEMNOW="
					+ StrUtils.fString((String) session.getAttribute("SYSTEMNOW")) + "&BASE_CURRENCY="
					+ StrUtils.fString((String) session.getAttribute("BASE_CURRENCY")) + "&LOGIN_USER="
					+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&NOOFPAYMENT="
					+ StrUtils.fString((String) session.getAttribute("NOOFPAYMENT")) + "&LOGIN_USER="
					+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&INTERNAL_REQUESET=TRUE";
			URL url = new URL(invoiceDetailURL);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			StringBuffer sbData = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = br.readLine()) != null) {
				sbData.append(line).append("\n");
			}
			br.close();
			is.close();
			Barcode barcode;
			BufferedImage image;
			try {
				barcode = BarcodeFactory.createCode128(invoiceNo);
				barcode.setDrawingText(false);
				barcode.setBorder(BorderFactory.createEmptyBorder());
			    image = BarcodeImageHandler.getImage(barcode);
			    
			    String base64 = ImageUtil.imgToBase64String(image, "png");
				String barCodeTag = "<img id=\"barCode\" style=\"width:215px;height:65px;\">";
				int barcodeStartIndex = sbData.indexOf(barCodeTag);
				int barcodeEndIndex = barcodeStartIndex + barCodeTag.length();
				if (barcodeStartIndex != -1 && barcodeEndIndex != -1) {
					sbData.replace(barcodeStartIndex, barcodeEndIndex, "<img id=\"barCode\" style=\"width:215px;height:65px;\" src=\"data:image/png;base64," + base64 + "\">");
				}
				PdfUtil.createPDF(sbData.toString(), DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + invoiceNo + ".pdf", rootURI, PageSize.A4, PageSize.A4.getWidth());
			} catch (BarcodeException e) {
				e.printStackTrace();
			} catch (OutputException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
//	private void diaplayInfoLogs(HttpServletRequest request) {
//		try {
//			Map requestParameterMap = request.getParameterMap();
//			Set<String> keyMap = requestParameterMap.keySet();
//			StringBuffer requestParams = new StringBuffer();
//			requestParams.append("Class Name : " + this.getClass() + "\n");
//			requestParams.append("Paramter Mapping : \n");
//			for (String key : keyMap) {
//				requestParams.append("[" + key + " : "
//						+ request.getParameter(key) + "] ");
//			}
//			this.mLogger.auditInfo(this.printInfo, requestParams.toString());
//
//		} catch (Exception e) {
//
//		}
//
//	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;

	}

	
	private JSONObject getinvoiceview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
//        StrUtils strUtils = new StrUtils();
        String fdate="",tdate="",covunitCostValue="0";//,taxby=""
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
           String  INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
//           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String  PLNO = StrUtils.fString(request.getParameter("PLNO"));
           String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
           String  EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           
	        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
	        if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
	        if(StrUtils.fString(INVOICENO).length() > 0)       ht.put("INVOICE",INVOICENO);
	        if(StrUtils.fString(STATUS).length() > 0)       ht.put("BILL_STATUS",STATUS);
	        if(StrUtils.fString(PLNO).length() > 0)       ht.put("PLNO",PLNO);
	        if(StrUtils.fString(EMPNO).length() > 0)       ht.put("EMPNO",EMPNO);
	        if(StrUtils.fString(ORDERTYPE).length() > 0)       ht.put("ORDERTYPE",ORDERTYPE);
	        
				movQryList = movHisUtil.getInvoiceSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
            
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
                                  
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                            double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                            
                            double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
                            
                            String crdbalCostValue = (String)lineArr.get("creditbalance");
                            double crdbalCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(crdbalCostValue);
                            if(crdbalCostVal==0f){
                            	crdbalCostValue="0.00000";
                            }else{
                            	crdbalCostValue=crdbalCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            crdbalCostValue = StrUtils.addZeroes(Double.parseDouble(crdbalCostValue), numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("INVOICE_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("INVOICE")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("DONO")));
                    	 resultJsonInt.put("DonoDate",StrUtils.fString((String)lineArr.get("DELDATE")));
                    	 resultJsonInt.put("gino",StrUtils.fString((String)lineArr.get("GINO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("ordertype",StrUtils.fString((String)lineArr.get("ORDERTYPE")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 resultJsonInt.put("isexpense",StrUtils.fString((String)lineArr.get("ISEXPENSE")));
                    	 resultJsonInt.put("creditbalance",StrUtils.fString(crdbalCostValue));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 resultJsonInt.put("currency",StrUtils.fString(cur));
                    	 //if(cur.isEmpty())
                    		 //cur= curency;
                    	 resultJsonInt.put("invamount",Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	// resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("convamount",Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("invoiceid", StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
                         jsonArray.add(resultJsonInt);                    	            	

                }
               
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	private JSONObject getpeppolview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        String fdate="",tdate="",covunitCostValue="0";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
           String  INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String  PLNO = StrUtils.fString(request.getParameter("PLNO"));
           String  EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
   
           Hashtable ht = new Hashtable();
           
	        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
	        if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
	        if(StrUtils.fString(INVOICENO).length() > 0)       ht.put("INVOICE",INVOICENO);
	        if(StrUtils.fString(STATUS).length() > 0)       ht.put("BILL_STATUS",STATUS);
	        if(StrUtils.fString(PLNO).length() > 0)       ht.put("PLNO",PLNO);
	        if(StrUtils.fString(EMPNO).length() > 0)       ht.put("EMPNO",EMPNO);
	        
				movQryList = movHisUtil.getPeppolSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
            
            if (movQryList.size() > 0) {
            int Index = 0;
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                            double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                            
                            double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
                            
                            String crdbalCostValue = (String)lineArr.get("creditbalance");
                            double crdbalCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(crdbalCostValue);
                            if(crdbalCostVal==0f){
                            	crdbalCostValue="0.00000";
                            }else{
                            	crdbalCostValue=crdbalCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            crdbalCostValue = StrUtils.addZeroes(Double.parseDouble(crdbalCostValue), numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("INVOICE_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("INVOICE")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("DONO")));
                    	 resultJsonInt.put("gino",StrUtils.fString((String)lineArr.get("GINO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 resultJsonInt.put("isexpense",StrUtils.fString((String)lineArr.get("ISEXPENSE")));
                    	 resultJsonInt.put("creditbalance",StrUtils.fString(crdbalCostValue));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 resultJsonInt.put("currency",StrUtils.fString(cur));
                    	 resultJsonInt.put("invamount",Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("invoiceid", StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
                    	 resultJsonInt.put("peppolstatus",StrUtils.fString((String)lineArr.get("PEPPOL_STATUS")));
                    	 resultJsonInt.put("peppoldocid",StrUtils.fString((String)lineArr.get("PEPPOL_DOC_ID")));
                         jsonArray.add(resultJsonInt);                    	            	
                }
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	
	
	private JSONObject getinvoicedashboardview(HttpServletRequest request,String PLANT) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        ArrayList movQryListfilter1  = new ArrayList();
        ArrayList movQryListfilter2  = new ArrayList();
        String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
        
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
//        StrUtils strUtils = new StrUtils();
//        String fdate="",tdate="",taxby="",covunitCostValue="0";
         try {
        
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
           movQryList = movHisUtil.getInvoiceSummaryDashboardView(FROM_DATE,TO_DATE,PLANT,CUSTOMER,STATUS,numberOfDecimal);	
//           double totalcredits = 0; 
            if (movQryList.size() > 0) {/*
            int iIndex = 0,Index = 0;
             int irow = 0;
                                  
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                            double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                            
                            double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
                            
                            String crdbalCostValue = (String)lineArr.get("creditbalance");
                            double crdbalCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(crdbalCostValue);
                            if(crdbalCostVal==0f){
                            	crdbalCostValue="0.00000";
                            }else{
                            	crdbalCostValue=crdbalCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            crdbalCostValue = StrUtils.addZeroes(Double.parseDouble(crdbalCostValue), numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("INVOICE_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("INVOICE")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("DONO")));
                    	 resultJsonInt.put("gino",StrUtils.fString((String)lineArr.get("GINO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 resultJsonInt.put("isexpense",StrUtils.fString((String)lineArr.get("ISEXPENSE")));
                    	 resultJsonInt.put("creditbalance",StrUtils.fString(crdbalCostValue));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 //if(cur.isEmpty())
                    		 //cur= curency;
                    	 resultJsonInt.put("convamount",cur+covunitCostValue);
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("invoiceid", StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
                    	 resultJsonInt.put("CREDITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("CREDITS")), numberOfDecimal));
                         jsonArray.add(resultJsonInt);
                         
                         totalcredits = totalcredits+ Double.parseDouble((String)lineArr.get("CREDITS"));

                }
               
                String totalcr = StrUtils.addZeroes(totalcredits, numberOfDecimal);
                    resultJson.put("items", jsonArray);
                    resultJson.put("total", totalcr);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            */
            	
            

 			   int Index = 0;
// 			   int iIndex = 0,irow = 0;
 			   double totaldebit=0;
            
 			   for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
// 				   String result="";
 				   Map lineArr = (Map) movQryList.get(iCnt);
 				   String condition = (String)lineArr.get("CONDITIONS");
 				   if(!condition.equalsIgnoreCase("")) {
 					   JSONObject resultJsonInt = new JSONObject();
 					   movQryListfilter1.add(lineArr);
 					   Index = Index + 1;
 					   resultJsonInt.put("Index",(Index));
 					   resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
 					   resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("JOURNAL_DATE")));
 					   resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("REFERENCE")));
 					   resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("NAME")));
 					   resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
 					   resultJsonInt.put("CREDITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
 					   
 					   jsonArray.add(resultJsonInt);
 	        	            	
 					   totaldebit = totaldebit + Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT"));
 				   }
 			   }
                
 			   if (CUSTOMER.length()>0){
 				   if (movQryListfilter1.size() > 0) {
 					   totaldebit=0;
 					   jsonArray.clear();
 					   for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
// 						   String result="";
 						   Map lineArr = (Map) movQryListfilter1.get(iCnt);
 						   String customer = (String)lineArr.get("NAME");
 						   if(customer.equalsIgnoreCase(CUSTOMER)) {
 							   JSONObject resultJsonInt = new JSONObject();
 							   movQryListfilter2.add(lineArr);
 							   Index = Index + 1;
 							   resultJsonInt.put("Index",(Index));
 							   resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
 							   resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("JOURNAL_DATE")));
 							   resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("REFERENCE")));
 							   resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("NAME")));
 							   resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
 							   resultJsonInt.put("CREDITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
 							   
 							   jsonArray.add(resultJsonInt);
 			        	            	
 							   totaldebit = totaldebit + Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT"));
 						   }
 					   }
 				   }
                }else {
             	   movQryListfilter2.addAll(movQryListfilter1);
                }

 	   		   if (STATUS != null && !STATUS.equalsIgnoreCase("")) {
 	   			   if (movQryListfilter2.size() > 0) {
 	   				   	totaldebit=0;
 	   				   	jsonArray.clear();
 		   				for (int iCnt =0; iCnt<movQryListfilter2.size(); iCnt++){
// 		 				   String result="";
 		 				   Map lineArr = (Map) movQryListfilter2.get(iCnt);
 		 				   String statusf = (String)lineArr.get("STATUS");
 		 				   if(statusf.equalsIgnoreCase(STATUS)) {
 		 					   JSONObject resultJsonInt = new JSONObject();
 		 					   Index = Index + 1;
 		 					   resultJsonInt.put("Index",(Index));
 		 					   resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
 		 					   resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("JOURNAL_DATE")));
 		 					   resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("REFERENCE")));
 		 					   resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("NAME")));
 		 					   resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
 		 					   resultJsonInt.put("CREDITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
 		 					   
 		 					   jsonArray.add(resultJsonInt);
 		 	        	            	
 		 					   totaldebit = totaldebit + Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT"));
 		 				   }
 		 			   }
 	   			   }
 	  		   }
 	   		   		String totalWithoutCurrency = StrUtils.addZeroes(totaldebit, numberOfDecimal);
 	                String totald = currency+totalWithoutCurrency;
 	                resultJson.put("items", jsonArray);
 	                resultJson.put("total", totald);
 	                resultJson.put("currency", currency);
 	                resultJson.put("totalWithoutCurrency", totalWithoutCurrency);
                     JSONObject resultJsonInt = new JSONObject();
                     resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                     resultJsonInt.put("ERROR_CODE", "100");
                     jsonArrayErr.add(resultJsonInt);
                     resultJson.put("errors", jsonArrayErr);
             
            
            
            
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
}
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil itemUtil = new DOUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     ht.put("ORDNO",dono);
		     ht.put("CNAME",cname);
		     ht.put("CUSTNO",new CustMstDAO().getCustomerNoByName(ht));
		     if(dono.length()>0) extCond=" AND plant='"+plant+"' and dono like '"+dono+"%' ";
		     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = itemUtil.getOrderNoForOrderIssue(ht);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  dono = (String)m.get("DONO");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("ORDER_DATE");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("DONO", dono);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);			  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getOrderNoForAutoSuggestionRevers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil itemUtil = new DOUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     ht.put("ORDNO",dono);
		     ht.put("CNAME",cname);
		     ht.put("CUSTNO",new CustMstDAO().getCustomerNoByName(ht));
		     if(dono.length()>0) extCond=" AND plant='"+plant+"' and dono like '"+dono+"%' ";
		     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = itemUtil.getOrderNoForOrderReverse(ht);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  dono = (String)m.get("DONO");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("ORDER_DATE");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("DONO", dono);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);			  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getOrderNoForForceClose(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil itemUtil = new DOUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        OrderTypeUtil orderUtil = new OrderTypeUtil();
        orderUtil.setmLogger(mLogger);
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		    String status = StrUtils.fString(request.getParameter("STATUS")).trim();
		    String orderNo = StrUtils.fString(request.getParameter("orderNo")).trim();
		 
		    ArrayList listQry = orderUtil.getOrderHdrDetails(plant,"OUTBOUND",dono,status);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
		    		  Map m=(Map)listQry.get(i);
		    	      orderNo     = (String)m.get("orderNo");
		    	      String custName    = (String)m.get("CustName");
		    	      String status1      =  (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("DONO", orderNo);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("STATUS", status1);						  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	
	private JSONObject getOrderNoForAutoSuggestiondnpl(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil itemUtil = new DOUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond1="";
		     String extCond="";
		     //ht.put("PLANT",plant);
		     if(dono.length()>0) extCond=" AND D.plant='"+plant+"' and D.dono like '"+dono+"%' ";
		     if(cname.length()>0) extCond=" AND D.CustName = '"+cname+"' ";
		     //extCond=extCond+" AND D.dono in (select I.DONO from "+plant+"_FINGINOTOINVOICE I where I.GINO not in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.STATUS <> 'P' )) and STATUS in ('O', 'C') ";
		     //extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     String query = " with T AS (";
		     query = query + "SELECT distinct D.dono,CustName,CustCode,jobNum,status,collectiondate from ["+plant+"_DOHDR] D join "+plant+"_DODET S ON D.DONO=S.DONO  WHERE D.PLANT = '"+plant+"' "+extCond;
		     query = query + " AND D.dono in (select I.DONO from "+plant+"_FINGINOTOINVOICE I where I.GINO not in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P JOIN "+plant+"_DNPLDET V ON P.ID=V.HDRID WHERE P.STATUS <> 'P' AND S.ITEM=V.ITEM AND S.DOLNNO=V.LNNO ))    ";
		     query = query + "and STATUS in ('O', 'C')  "
		     + ") SELECT * FROM T ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     //ArrayList listQry = itemUtil.getDoHdrDetails("dono,CustName,CustCode,jobNum,status,collectiondate",ht,extCond);
		     ArrayList listQry = new DoDetDAO().selectDoDetForPage(query, ht, extCond1);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  dono = (String)m.get("dono");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("CustName"));
				  String custcode = (String)m.get("CustCode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("DONO", dono);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getOrderDetailsForInvoice(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        DOUtil doUtil = new DOUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="";
		    Hashtable ht=new Hashtable();
		    ht.put("DONO", dono);
		    ht.put("PLANT", plant);
		    
		    List listQry = doUtil.getOrderDetailsForBilling(ht);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("QTYOR"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					resultJsonInt.put("DONO", (String)m.get("DONO"));
					resultJsonInt.put("DOLNNO", (String)m.get("DOLNNO"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("OUTBOUND_GST", (String)m.get("OUTBOUND_GST"));
					resultJsonInt.put("ORDERDISCOUNT", (String)m.get("ORDERDISCOUNT"));
					resultJsonInt.put("SHIPPINGCOST", (String)m.get("SHIPPINGCOST"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					resultJsonInt.put("UNITMO", (String)m.get("UNITMO"));
					resultJsonInt.put("QTYOR", (String)m.get("QTYOR"));
					resultJsonInt.put("CONVCOST", (String)m.get("CONVCOST"));
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
					jsonArray.add(resultJsonInt);					
					
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getOrderDetailsForInvoiceUsingInvno(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ShipHisDAO shipHisDAO = new ShipHisDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String invnumber = StrUtils.fString(request.getParameter("INVOICENO")).trim();
		    
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="";//,ConvertedAmount="";
		    
		   // String querystrings = "UNITPRICE,PICKQTY,ITEM";
		    String querystrings1 = "A.UNITPRICE,A.ITEM,C.CATLOGPATH,A.ITEMDESC,C.STKUOM,SUM(ISNULL(D.QTY,0)) AS INVQTY,B.OUTBOUND_GST,A.PICKQTY";
		    String querystrings2 = "[" + plant + "_SHIPHIS] A JOIN [" + plant + "_DOHDR] B ON A.DONO = B.DONO " + " JOIN [" + plant + "_ITEMMST] C ON A.ITEM = C.ITEM LEFT JOIN ["+ plant + "_INVMST] as D ON C.ITEM = D.ITEM";
		  
		    String cgroup =" GROUP BY A.ITEM ,A.ITEMDESC,C.STKUOM,A.UNITPRICE,C.CATLOGPATH,B.OUTBOUND_GST,A.PICKQTY ORDER BY A.ITEM";
                   
		    Hashtable ht=new Hashtable();
		    ht.put("INVOICENO", invnumber);
		    ht.put("PLANT", plant);
		    
		   // List listQry = doUtil.getOrderDetailsForBilling(ht);
		   // ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings, ht);
		    ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings1,querystrings2, ht,cgroup);
		    
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					/*double dQty = Double.parseDouble((String)m.get("PICKQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);*/
					
					
					//resultJsonInt.put("OUTBOUND_GST", (String)m.get("OUTBOUND_GST"));
					
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					
					//resultJsonInt.put("QTYOR", (String)m.get("PICKQTY"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					//resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", "Inventory Asset");
					jsonArray.add(resultJsonInt);					
					
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject getSalesOrderDetailsForInvoiceUsingInvno(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ShipHisDAO shipHisDAO = new ShipHisDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String invnumber = StrUtils.fString(request.getParameter("INVOICENO")).trim();
		    
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="";//,ConvertedAmount="";
		    
		   // String querystrings = "UNITPRICE,PICKQTY,ITEM";
		    String querystrings1 = "A.UNITPRICE,A.ITEM,C.CATLOGPATH,A.ITEMDESC,C.STKUOM,SUM(ISNULL(D.QTY,0)) AS INVQTY,B.OUTBOUND_GST,A.PICKQTY";
		    String querystrings2 = "[" + plant + "_SHIPHIS] A JOIN [" + plant + "_DOHDR] B ON A.DONO = B.DONO " + " JOIN [" + plant + "_ITEMMST] C ON A.ITEM = C.ITEM LEFT JOIN ["+ plant + "_INVMST] as D ON C.ITEM = D.ITEM";
		  
		    String cgroup =" GROUP BY A.ITEM ,A.ITEMDESC,C.STKUOM,A.UNITPRICE,C.CATLOGPATH,B.OUTBOUND_GST,A.PICKQTY ORDER BY A.ITEM";
                   
		    Hashtable ht=new Hashtable();
		    ht.put("INVOICENO", invnumber);
		    ht.put("PLANT", plant);
		    
		   // List listQry = doUtil.getOrderDetailsForBilling(ht);
		   // ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings, ht);
		    ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings1,querystrings2, ht,cgroup);
		    
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					/*double dQty = Double.parseDouble((String)m.get("PICKQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);*/
					
					
					//resultJsonInt.put("OUTBOUND_GST", (String)m.get("OUTBOUND_GST"));
					
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					
					//resultJsonInt.put("QTYOR", (String)m.get("PICKQTY"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					//resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", "Sales");
					jsonArray.add(resultJsonInt);					
					
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject getEditInvoiceDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil invUtil = new InvoiceUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String TranId = StrUtils.fString(request.getParameter("Id")).trim();
		    String CUR = StrUtils.fString(request.getParameter("CUR")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="",ConvertedDiscount="";
		    Hashtable ht=new Hashtable();
		    ht.put("ID", TranId);
		    ht.put("PLANT", plant);
		    
		    String replacePreviousInvoiceCost="0";
		    Map invoiceDetails= new DOUtil().getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
		    if(!invoiceDetails.isEmpty())
		    	replacePreviousInvoiceCost = (String) invoiceDetails.get("SHOWPREVIOUSINVOICECOST");
		    
		    ArrayList listQry = invUtil.getEditInvoiceDetails(ht,plant);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);					
					
					double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					String idisc = (String)m.get("ITEM_DISCOUNT");
					if(idisc != null && !idisc.isEmpty()) {
						double dDiscount = Double.parseDouble((String)m.get("ITEM_DISCOUNT"));
						ConvertedDiscount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
					}
					
					//HDR Details
					resultJsonInt.put("INVOICE", (String)m.get("INVOICE"));					
					resultJsonInt.put("INVOICE_DATE", (String)m.get("INVOICE_DATE"));
					resultJsonInt.put("ORDERNO", (String)m.get("ORDERNO"));
					resultJsonInt.put("CUST_CODE", (String)m.get("CUST_CODE"));
					resultJsonInt.put("CUSTOMER", (String)m.get("CUSTOMER"));
					resultJsonInt.put("ORDERTYPE", (String)m.get("ORDERTYPE"));					
					resultJsonInt.put("EMPNO", (String)m.get("EMPNO"));					
					resultJsonInt.put("EMP_NAME", (String)m.get("EMP_NAME"));
					resultJsonInt.put("DUE_DATE", (String)m.get("DUE_DATE"));					
					resultJsonInt.put("PAYMENT_TERMS", (String)m.get("PAYMENT_TERMS"));
					resultJsonInt.put("TERMSCONDITIONS", (String)m.get("TERMSCONDITIONS"));
					resultJsonInt.put("NOTE", (String)m.get("NOTE"));
					resultJsonInt.put("ITEM_RATES", (String)m.get("ITEM_RATES"));					
					resultJsonInt.put("DISCOUNT", (String)m.get("DISCOUNT"));
					resultJsonInt.put("ORDER_DISCOUNT", (String)m.get("ORDER_DISCOUNT"));
					resultJsonInt.put("DISCOUNT_TYPE", (String)m.get("DISCOUNT_TYPE"));					
					resultJsonInt.put("DISCOUNT_ACCOUNT", (String)m.get("DISCOUNT_ACCOUNT"));
					resultJsonInt.put("SHIPPINGCOST", (String)m.get("SHIPPINGCOST"));					
					resultJsonInt.put("ADJUSTMENT", (String)m.get("ADJUSTMENT"));
					resultJsonInt.put("SUB_TOTAL", (String)m.get("SUB_TOTAL"));
					resultJsonInt.put("ATTACHNOTE_COUNT", (String)m.get("ATTACHNOTE_COUNT"));
					resultJsonInt.put("SALES_LOCATION", (String)m.get("SALES_LOCATION"));
					resultJsonInt.put("STATE_PREFIX", (String)m.get("STATE_PREFIX"));
					resultJsonInt.put("TAXTREATMENT", (String)m.get("TAXTREATMENT"));
					resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String)m.get("ITEM_DISCOUNT_TYPE"));
					resultJsonInt.put("BILL_STATUS", (String)m.get("BILL_STATUS"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
                    resultJsonInt.put("DISPLAY", (String)m.get("DISPLAY"));
                    resultJsonInt.put("DOB", (String)m.get("DOB"));
                    resultJsonInt.put("NATIONAL", (String)m.get("NATIONALITY"));
                    
                    resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
                    resultJsonInt.put("TAXID", (String)m.get("TAXID"));
                    resultJsonInt.put("ISDISCOUNTTAX", (String)m.get("ISDISCOUNTTAX"));
                    resultJsonInt.put("ISORDERDISCOUNTTAX", (String)m.get("ISORDERDISCOUNTTAX"));
                    resultJsonInt.put("ISSHIPPINGTAX", (String)m.get("ISSHIPPINGTAX"));
                    resultJsonInt.put("OUTBOUD_GST", (String)m.get("OUTBOUD_GST"));
                    resultJsonInt.put("PROJECTID", (String)m.get("PROJECTID"));
                    resultJsonInt.put("TRANSPORTID", (String)m.get("TRANSPORTID"));
                    resultJsonInt.put("IS_COGS_SET", (String)m.get("IS_COGS_SET"));
                    
                    //imthi added to show COST and ADD on COST
                    resultJsonInt.put("UNITCOST", (String)m.get("UNITCOST"));
                    resultJsonInt.put("INCPRICE", (String)m.get("INCPRICE"));
                    String cppi = (String) m.get("CPPI");
                    String incprice = (String) m.get("INCPRICE");
                    String unitcost = (String) m.get("UCOST");
                    String detaddcost= (String) m.get("ADDONAMOUNT");
                    String detaddtype = (String) m.get("ADDONTYPE");
                    unitcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,(String)m.get("CURRENCYID"), (String)m.get("ITEM"),unitcost);
                    unitcost = StrUtils.addZeroes(Double.parseDouble(unitcost), numberOfDecimal);
                    if(cppi.equalsIgnoreCase("BYPRICE")) {
                    	incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,(String)m.get("CURRENCYID"), (String)m.get("ITEM"),incprice); 
                    	incprice = StrUtils.addZeroes(Double.parseDouble(incprice), numberOfDecimal);
                    	cppi = incprice+" "+(String)m.get("CURRENCYID");
                    	detaddtype = CUR;
                    	//if(replacePreviousInvoiceCost.equalsIgnoreCase("1") || replacePreviousInvoiceCost.equalsIgnoreCase("2")) {
                    		//cppi = "0.00"+" "+(String)m.get("CURRENCYID");
                    		//detaddtype = (String)m.get("CURRENCYID");
                    	//}
                    }else {
                    	cppi = incprice+" "+"%";
                    	detaddtype = "%";
                    	//if(replacePreviousInvoiceCost.equalsIgnoreCase("1") || replacePreviousInvoiceCost.equalsIgnoreCase("2")) {
                    		//cppi = "0.00"+" "+"%";
                    		//detaddtype = "%";
                    	//}
                    }
                    if(replacePreviousInvoiceCost.equalsIgnoreCase("1") || replacePreviousInvoiceCost.equalsIgnoreCase("2")) {
                    	//resultJsonInt.put("UCOST", "0.00");
                    	cppi = "0.00"+" "+(String)m.get("CURRENCYID");
                		detaddtype = (String)m.get("CURRENCYID");
                    }
//                    resultJsonInt.put("CPPI", (String)m.get("CPPI"));
                    resultJsonInt.put("CPPI", detaddcost);
                    resultJsonInt.put("DETADDTYPE", detaddtype);
                    resultJsonInt.put("AOD", cppi);
                    resultJsonInt.put("UCOST", unitcost);
                    
                    //end
                    
                    FinProjectDAO finProjectDAO = new FinProjectDAO();
            		FinProject finProject=new FinProject();
            		String projectname = "";
            		int projectid = Integer.valueOf((String)m.get("PROJECTID"));
            		if(projectid > 0){
            			finProject = finProjectDAO.getFinProjectById(plant, projectid);
            			projectname = finProject.getPROJECT_NAME();
            		}
            		
            		TransportModeDAO transportmodedao = new TransportModeDAO();
            		String transportmode = "";
            		int transportid = Integer.valueOf((String)m.get("TRANSPORTID"));
            		if(transportid > 0){
            			transportmode = transportmodedao.getTransportModeById(plant,transportid);
            		}else{
            			transportmode = "";
            		}
            		
            		resultJsonInt.put("PROJECTNAME", projectname);
            		resultJsonInt.put("TRANSPORTNAME", transportmode);
            		resultJsonInt.put("SHIPPINGID", (String)m.get("SHIPPINGID"));
            		resultJsonInt.put("SHIPPINGCUSTOMER", (String)m.get("SHIPPINGCUSTOMER"));
            		
            		int taxid = Integer.valueOf((String)m.get("TAXID"));
            		FinCountryTaxType fintaxtype = new FinCountryTaxType();
            		if(taxid > 0){
            			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
            			fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
            			 resultJsonInt.put("FINTAXTYPE", fintaxtype.getTAXTYPE());
                         resultJsonInt.put("FINTAXISZERO", fintaxtype.getISZERO());
                         resultJsonInt.put("FINTAXSHOW", fintaxtype.getSHOWTAX());
            		}else {
            			 resultJsonInt.put("FINTAXTYPE", "");
                         resultJsonInt.put("FINTAXISZERO", "1");
                         resultJsonInt.put("FINTAXSHOW", "0");
            		}
                    
            		String minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,(String)m.get("CURRENCYID"),(String)m.get("ITEM"));
            		resultJsonInt.put("minSellingConvertedUnitCost", minSellingconvertedcost);
            		
            		String OBDiscount=new DOUtil().getOBDiscountSelectedItemByCustomer(plant,(String)m.get("CUST_CODE"),(String)m.get("ITEM"),"OUTBOUND");
            		resultJsonInt.put("outgoingOBDiscount", OBDiscount);
            		
            		ItemUtil itemUtil = new ItemUtil();
            		List listItem = itemUtil.queryItemMstDetails((String)m.get("ITEM"),plant);
                   
                    if(listItem.size()>0){
                    	Vector arrItem    = (Vector)listItem.get(0);
                    	resultJsonInt.put("outgoingqty", StrUtils.fString((String)arrItem.get(23)));
                		resultJsonInt.put("stkqty", StrUtils.fString((String)arrItem.get(8)));
                		resultJsonInt.put("maxstkqty", StrUtils.fString((String)arrItem.get(21)));
                		resultJsonInt.put("stockonhand", StrUtils.fString((String)arrItem.get(22)));
                		resultJsonInt.put("INVOICEFROM", "INVOICE");
					}else {
						resultJsonInt.put("outgoingqty", "0");
	            		resultJsonInt.put("stkqty", "0");
	            		resultJsonInt.put("maxstkqty", "0");
	            		resultJsonInt.put("stockonhand", "0");
	            		resultJsonInt.put("INVOICEFROM", "EXPENSE");
					}
            		
                    new Hashtable();
            	 	ht.put("item", (String)m.get("ITEM"));
            	 	ht.put("plant", plant);
            	 	Map mest = new EstDetDAO().getEstQtyByProduct(ht);
            	 	String estQty = (String) mest.get("ESTQTY");
            		resultJsonInt.put("EstQty", estQty);
            		
            		mest = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	String avlbQty= (String) mest.get("AVLBQTY");
            		
            		resultJsonInt.put("AvlbQty", avlbQty);

					
					//Item Details
					
					resultJsonInt.put("DETID", (String)m.get("DETID"));
					resultJsonInt.put("LNNO", (String)m.get("LNNO"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("QTY", (String)m.get("QTY"));
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);					
					resultJsonInt.put("ITEM_DISCOUNT", ConvertedDiscount);
					resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String)m.get("ITEM_DISCOUNT_TYPE"));
					resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
					resultJsonInt.put("Notesexp", (String)m.get("Notesexp"));
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("ACCOUNT_NAME", (String)m.get("ACCOUNT_NAME"));
                    resultJsonInt.put("LOC", (String)m.get("LOC"));
                    resultJsonInt.put("UOM", (String)m.get("UOM"));
                    resultJsonInt.put("BATCH", (String)m.get("BATCH"));
                    resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
                    resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
										
					jsonArray.add(resultJsonInt);
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getInvoiceNoforCreditNotr(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ReturnOrderDAO returnOrderDAO=new ReturnOrderDAO();
        InvoiceDAO invoicedao = new InvoiceDAO();
//        StrUtils strUtils = new StrUtils();
        invoicedao.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String custcode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
		    String soreturn = StrUtils.fString(request.getParameter("soreturn")).trim();
		    
		    Hashtable ht=new Hashtable();
		     ht.put("PLANT",plant);
		     ht.put("CUSTNO",custcode);
		     List listQry = null;
		     if(soreturn.equalsIgnoreCase("") || soreturn == null) {
		    	  listQry = invoicedao.getInvoiceHdrByCustNocredit(ht);
		     }else {
		    	 String query="INVOICE,DONO,GINO,'0' AS ID";
				 String extquery = "PLANT = '"+plant+"' AND SORETURN ='"+soreturn+"' GROUP BY INVOICE,DONO,GINO ORDER BY INVOICE DESC";
				 listQry=returnOrderDAO.getSOReturnDetailsbycustomer(plant, query, extquery);
		     }
		     
		     
		     
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String invoice = (String)m.get("INVOICE");
				  String invid = (String)m.get("ID");
				  String dono = (String) m.get("DONO");
				  String gino = (String) m.get("GINO");
		
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("INVOICE", invoice);	
				  resultJsonInt.put("INVID", invid);
				  resultJsonInt.put("DONO", dono);
				  resultJsonInt.put("GINO", gino);
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("invoices", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getBillAgingView(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        PlantMstDAO plantMstDAO = new PlantMstDAO();
        
        String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="";
        //String Order="",ordRefNo="",custcode="",currencyid="",
        String custName="",orderNo="",invoiceNo="";
        String PLANT= StrUtils.fString(request.getParameter("PLANT"));
        
        
        BillUtil billUtil = new BillUtil();
        AgeingUtil ageingUtil = new AgeingUtil();
        
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        ArrayList movQrycustList  = new ArrayList();
        
        try {
        FROM_DATE     = StrUtils.fString(request.getParameter("FDATE"));
        TO_DATE   = StrUtils.fString(request.getParameter("TDATE"));
        invoiceNo   = StrUtils.fString(request.getParameter("NAME"));
        
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
        String curDate =DateUtils.getDate();
        
        if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
        
        if (FROM_DATE.length()>5)
        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
        
        if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
        if (TO_DATE.length()>5)
        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
        
        custName = StrUtils.fString(request.getParameter("CNAME"));
        orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        
        movQrycustList = ageingUtil.getcustomerorsuppliername(PLANT,fdate,tdate,"INVOICE",custName);        
//        boolean flag =  
        		ageingUtil.InsertTempOrderPayment(PLANT, fdate,tdate,"INVOICE",custName,orderNo,invoiceNo);
        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
//        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		double totaldue = 0;
		double amount=0;
		double balance=0;
//		int k=0;
		int index=0;
//		String total="";//,stmtdate=""
		if(movQrycustList.size()>0){
        for (int i = 0; i < movQrycustList.size(); i++){
//        	Map lineArrcust = (Map) movQrycustList.get(i);
//        	custName = (String)lineArrcust.get("custname");
//            custcode = (String)lineArrcust.get("custcode");
//            String pmtdays = (String)lineArrcust.get("pmtdays");
//            currencyid = (String)lineArrcust.get("currencyid");
            
            movQryList = billUtil.getPaymentStatementDetails(PLANT, fdate, tdate, "INVOICE", custName, "");
            
            for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
           		
           		index = index + 1;
           		String ordNo = (String)lineArr.get("ORDNO");
           		String orderName = (String)lineArr.get("ORDERNAME");
           		if (ordNo.indexOf("Pmtrefno") != -1) {
           			ordNo = ordNo.replaceAll("Pmtrefno", "-");
    			}
           		String ordDate = (String)lineArr.get("TRANDATE");            
           		
           		amount = Double.parseDouble((String)lineArr.get("AMOUNT"));
           		amount = StrUtils.RoundDB(amount,2);
           		balance = Double.parseDouble((String)lineArr.get("BALANCE"));
           		balance = StrUtils.RoundDB(balance,2);
           		
//           		String amtReceived= decformat.format(amount);
//           		String balToPay= decformat.format(balance);
 		   		String orderno = "";
 		   		orderno = (String)lineArr.get("ORDNO");
           		int plusIndex = orderno.indexOf("Pmtrefno");
           		if (plusIndex != -1) {
        	   		orderno = orderno.substring(0, plusIndex);               
           		}   
           		totaldue = Double.parseDouble((String)lineArr.get("BALANCE"));
           		totaldue = StrUtils.RoundDB(totaldue,2);
//           		total = decformat.format(totaldue);
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("TRANDATE",StrUtils.fString(ordDate));
				resultJsonInt.put("ORDERNAME",StrUtils.fString(orderName));
				resultJsonInt.put("ORDNO",StrUtils.fString(orderno));
				resultJsonInt.put("CUSTCODE",StrUtils.fString(custName));
				resultJsonInt.put("AMOUNT",StrUtils.addZeroes(amount,numberOfDecimal));
				resultJsonInt.put("BALANCE",StrUtils.addZeroes(totaldue,numberOfDecimal));    
				resultJsonInt.put("TOTAL",StrUtils.addZeroes(totaldue,numberOfDecimal));    
                jsonArray.add(resultJsonInt);
            }
		}
        resultJson.put("items", jsonArray);
        JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
		}else {
			JSONObject resultJsonInt = new JSONObject();
	        resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
	        resultJsonInt.put("ERROR_CODE", "99");
	        jsonArrayErr.add(resultJsonInt);
	        jsonArray.add("");
	        resultJson.put("items", jsonArray);
	        resultJson.put("errors", jsonArrayErr);
		}
        
        } catch (Exception e) {
    		jsonArray.add("");
    		resultJson.put("items", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
    }
		return resultJson;        
	}
	
	private JSONObject getInvoiceNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        AgeingUtil ageingUtil = new AgeingUtil();
        ArrayList movQryList  = new ArrayList();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String cName = StrUtils.fString(request.getParameter("CNAME"));
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String invoice = StrUtils.fString(request.getParameter("NAME"));
        	int index=0;
        	Hashtable ht = new Hashtable();
        	ht.put("CUSTNO", cName);
        	ht.put("DONO", orderNo);
        	ht.put("INVOICE", invoice);
        	
        	movQryList = ageingUtil.getInvoiceNoForAutoSuggestion(ht, plant);
        	
        	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
           		
           		index = index + 1;           		
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("INVOICE",(String)lineArr.get("INVOICE"));
				resultJsonInt.put("CUSTNO",(String)lineArr.get("CUSTNO"));
				resultJsonInt.put("DONO",(String)lineArr.get("DONO"));
				resultJsonInt.put("INVOICE_DATE",(String)lineArr.get("INVOICE_DATE"));
				resultJsonInt.put("DUE_DATE",(String)lineArr.get("DUE_DATE"));
				resultJsonInt.put("PAYMENT_TERMS",(String)lineArr.get("PAYMENT_TERMS"));
				resultJsonInt.put("ITEM_RATES",(String)lineArr.get("ITEM_RATES"));
				resultJsonInt.put("DISCOUNT",(String)lineArr.get("DISCOUNT"));
				resultJsonInt.put("DISCOUNT_TYPE",(String)lineArr.get("DISCOUNT_TYPE"));
				resultJsonInt.put("DISCOUNT_ACCOUNT",(String)lineArr.get("DISCOUNT_ACCOUNT"));
				resultJsonInt.put("SHIPPINGCOST",(String)lineArr.get("SHIPPINGCOST"));
				resultJsonInt.put("ADJUSTMENT",(String)lineArr.get("ADJUSTMENT"));
				resultJsonInt.put("SUB_TOTAL",(String)lineArr.get("SUB_TOTAL"));
				resultJsonInt.put("TOTAL_AMOUNT",(String)lineArr.get("TOTAL_AMOUNT"));
				resultJsonInt.put("BILL_STATUS",(String)lineArr.get("BILL_STATUS"));
				resultJsonInt.put("NOTE",(String)lineArr.get("NOTE"));
				resultJsonInt.put("TERMSCONDITIONS",(String)lineArr.get("TERMSCONDITIONS"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("InvoiceDetails", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getInvoiceNoForAutoSuggestionreturn (HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
//        AgeingUtil ageingUtil = new AgeingUtil();
        ArrayList movQryList  = new ArrayList();
        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        BillDAO billDao = new BillDAO();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String cName = StrUtils.fString(request.getParameter("CNAME"));
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String invoice = StrUtils.fString(request.getParameter("NAME"));
        	String gino = StrUtils.fString(request.getParameter("GINO"));
        	int index=0;
        	Hashtable ht = new Hashtable();
        	ht.put("CUSTNO", cName);
        	ht.put("DONO", orderNo);
        	ht.put("INVOICE", invoice);
        	ht.put("GINO", gino);
        	Hashtable htCond = new Hashtable();
        	
        	StringBuffer sql = new StringBuffer("SELECT * FROM ["+plant+"_FININVOICEHDR] WHERE PLANT='"+ plant+"' AND DEDUCT_INV = '1'");		           
              
                if (ht.get("DONO") != null && ht.get("DONO") != "") {
     			  sql.append(" AND DONO = '" + ht.get("DONO") + "'");
     		    }
                if (ht.get("GINO") != null && ht.get("GINO") != "") {
       			  sql.append(" AND GINO = '" + ht.get("GINO") + "'");
       		    }
     		   if (ht.get("CUSTNO") != null && ht.get("CUSTNO") != "") {
     			   String custNo = customerBeanDAO.getCustomerCode(plant, (String)ht.get("CUSTNO"));
     			  sql.append(" AND CUSTNO = '" + custNo + "'");
     		    }
     		   if (ht.get("INVOICE") != null && ht.get("INVOICE") != "") {
     				  sql.append(" AND INVOICE LIKE '%"+ht.get("INVOICE")+"%'");
     		    }
     		   
     		   String extraCon = "ORDER BY ID DESC";
     		   
        	
        	movQryList = billDao.selectForReport(sql.toString(), htCond, extraCon);
        	
        	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
           		
           		index = index + 1;           		
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("INVOICE",(String)lineArr.get("INVOICE"));
				resultJsonInt.put("CUSTNO",(String)lineArr.get("CUSTNO"));
				resultJsonInt.put("DONO",(String)lineArr.get("DONO"));
				resultJsonInt.put("INVOICE_DATE",(String)lineArr.get("INVOICE_DATE"));
				resultJsonInt.put("DUE_DATE",(String)lineArr.get("DUE_DATE"));
				resultJsonInt.put("PAYMENT_TERMS",(String)lineArr.get("PAYMENT_TERMS"));
				resultJsonInt.put("ITEM_RATES",(String)lineArr.get("ITEM_RATES"));
				resultJsonInt.put("DISCOUNT",(String)lineArr.get("DISCOUNT"));
				resultJsonInt.put("DISCOUNT_TYPE",(String)lineArr.get("DISCOUNT_TYPE"));
				resultJsonInt.put("DISCOUNT_ACCOUNT",(String)lineArr.get("DISCOUNT_ACCOUNT"));
				resultJsonInt.put("SHIPPINGCOST",(String)lineArr.get("SHIPPINGCOST"));
				resultJsonInt.put("ADJUSTMENT",(String)lineArr.get("ADJUSTMENT"));
				resultJsonInt.put("SUB_TOTAL",(String)lineArr.get("SUB_TOTAL"));
				resultJsonInt.put("TOTAL_AMOUNT",(String)lineArr.get("TOTAL_AMOUNT"));
				resultJsonInt.put("BILL_STATUS",(String)lineArr.get("BILL_STATUS"));
				resultJsonInt.put("NOTE",(String)lineArr.get("NOTE"));
				resultJsonInt.put("TERMSCONDITIONS",(String)lineArr.get("TERMSCONDITIONS"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("InvoiceDetails", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getInvoiceNoForAutoSuggestionreturndnpl(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
//        AgeingUtil ageingUtil = new AgeingUtil();
        ArrayList movQryList  = new ArrayList();
        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        BillDAO billDao = new BillDAO();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String cName = StrUtils.fString(request.getParameter("CNAME"));
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String invoice = StrUtils.fString(request.getParameter("NAME"));
        	String gino = StrUtils.fString(request.getParameter("GINO"));
//        	int index=0;
        	Hashtable ht = new Hashtable();
        	ht.put("CUSTNO", cName);
        	ht.put("DONO", orderNo);
        	ht.put("INVOICE", invoice);
        	ht.put("GINO", gino);
        	Hashtable htCond = new Hashtable();
        	
        	//StringBuffer sql = new StringBuffer("SELECT INVOICE,CUSTNO,DONO,GINO,ISNULL((select top 1 D.CNAME from "+plant+"_CUSTMST D where D.CUSTNO=A.CUSTNO),'') CUSTNAME FROM ["+plant+"_FININVOICEHDR] A WHERE PLANT='"+ plant+"' AND (A.GINO='' OR A.GINO is null OR A.GINO not in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.STATUS <> 'P')) AND A.INVOICE not in (select ISNULL(P.INVOICE,'') from "+plant+"_DNPLHDR P WHERE P.STATUS <> 'P') ");		           
        	StringBuffer sql = new StringBuffer("WITH T AS (SELECT distinct A.INVOICE,A.CUSTNO,A.DONO,A.GINO FROM ["+plant+"_FININVOICEHDR] A join ["+plant+"_FININVOICEDET] S ON A.ID=S.INVOICEHDRID WHERE A.PLANT='"+ plant+"' ");		           
                if (ht.get("DONO") != null && ht.get("DONO") != "") {
     			  sql.append(" AND A.DONO = '" + ht.get("DONO") + "'");
     		    }
                if (ht.get("GINO") != null && ht.get("GINO") != "") {
       			  sql.append(" AND A.GINO = '" + ht.get("GINO") + "'");
       		    }
     		   if (ht.get("CUSTNO") != null && ht.get("CUSTNO") != "") {
     			   String custNo = customerBeanDAO.getCustomerCode(plant, (String)ht.get("CUSTNO"));
     			  sql.append(" AND A.CUSTNO = '" + custNo + "'");
     		    }
     		   if (ht.get("INVOICE") != null && ht.get("INVOICE") != "") {
     				  sql.append(" AND A.INVOICE LIKE '%"+ht.get("INVOICE")+"%'");
     		    }
     		   
     		   sql.append("  AND (A.GINO='' OR A.GINO is null OR A.GINO not in (select ISNULL(P.GINO,'') from "+ plant+"_DNPLHDR P JOIN "+ plant+"_DNPLDET V ON P.ID=V.HDRID WHERE P.STATUS <> 'P' AND S.ITEM=V.ITEM AND S.LNNO=V.LNNO)) ");
     		   sql.append("  AND A.INVOICE not in (select ISNULL(P.INVOICE,'') from "+ plant+"_DNPLHDR P JOIN "+ plant+"_DNPLDET V ON P.ID=V.HDRID WHERE P.STATUS <> 'P' AND S.ITEM=V.ITEM AND S.LNNO=V.LNNO)");
     		   sql.append("  ) SELECT *,ISNULL((select top 1 D.CNAME from "+ plant+"_CUSTMST D where D.CUSTNO=T.CUSTNO),'') CUSTNAME FROM T");
     		   String extraCon = "ORDER BY DONO DESC,GINO DESC,INVOICE DESC";
     		   
        	
        	movQryList = billDao.selectForReport(sql.toString(), htCond, extraCon);
        	
        	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("INVOICE",(String)lineArr.get("INVOICE"));
				resultJsonInt.put("CUSTNO",(String)lineArr.get("CUSTNO"));
				resultJsonInt.put("CUSTNAME",(String)lineArr.get("CUSTNAME"));
				resultJsonInt.put("DONO",(String)lineArr.get("DONO"));
				resultJsonInt.put("GINO",(String)lineArr.get("GINO"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("InvoiceDetails", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getginoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        DoDetDAO itemUtil = new DoDetDAO();
//        AgeingUtil ageingUtil = new AgeingUtil();
//        ArrayList movQryList  = new ArrayList();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));

//        	int index=0;
        	Hashtable ht = new Hashtable();
        	
        	 String extCond="";
		     ht.put("PLANT",plant);
		     if(orderNo.length()>0) extCond=" AND plant='"+plant+"' and dono='"+orderNo+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" GROUP BY A.INVOICENO ORDER BY IDS DESC ";
		     ArrayList listQry = itemUtil.selectginofromshiphis("ISNULL(A.INVOICENO,'') AS INVOICENO,MAX(ID) AS IDS ",ht,extCond);
        	
        	for (int iCnt =0; iCnt<listQry.size(); iCnt++){
           		Map lineArr = (Map) listQry.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("GINO",(String)lineArr.get("INVOICENO"));
				
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("gino", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	private JSONObject getginoForAutoSuggestiondnpl(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        DoDetDAO itemUtil = new DoDetDAO();
//        AgeingUtil ageingUtil = new AgeingUtil();
//        ArrayList movQryList  = new ArrayList();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String CUST_CODE = StrUtils.fString(request.getParameter("CUST_CODE"));

//        	int index=0;
        	Hashtable ht = new Hashtable();
        	
        	 String extCond="";
        	 String extCond1="";
		     //ht.put("PLANT",plant);
		     if(orderNo.length()>0) extCond=" AND A.plant='"+plant+"' and A.dono='"+orderNo+"' ";
		     if(CUST_CODE.length()>0) extCond=extCond+" AND A.CUSTNO='"+CUST_CODE+"' ";
		     String query = " with T AS ( "
		     + "SELECT distinct A.GINO,A.DONO,A.CUSTNO from ["+plant+"_FINGINOTOINVOICE] A join "+plant+"_DODET S ON A.DONO=S.DONO WHERE A.plant='"+plant+"' "+extCond
		     + "and A.GINO not in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P JOIN "+plant+"_DNPLDET V ON P.ID=V.HDRID WHERE P.STATUS <> 'P' AND S.ITEM=V.ITEM AND S.DOLNNO=V.LNNO )  "
		     + " ) SELECT *,ISNULL((select top 1 CustName from "+plant+"_DOHDR D where D.DONO=T.DONO),'')CUSTNAME FROM T ORDER BY DONO DESC,GINO DESC";
		     //extCond=extCond+" and A.GINO not in (select ISNULL(P.GINO,'') from "+plant+"_DNPLHDR P WHERE P.STATUS <> 'P' ) ";
		     //extCond=extCond+" ORDER BY A.ID desc";
		     //ArrayList listQry = itemUtil.selectgitoinvoice("A.GINO,A.DONO,A.CUSTNO,ISNULL((select top 1 CustName from "+plant+"_DOHDR D where D.DONO=A.DONO),'')CUSTNAME",ht,extCond);
		     ArrayList listQry = itemUtil.selectDoDetForPage(query, ht, extCond1);
        	for (int iCnt =0; iCnt<listQry.size(); iCnt++){
           		Map lineArr = (Map) listQry.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("GINO",(String)lineArr.get("GINO"));
				resultJsonInt.put("DONO",(String)lineArr.get("DONO"));
				resultJsonInt.put("CUSTNAME",(String)lineArr.get("CUSTNAME"));
				resultJsonInt.put("CUSTCODE",(String)lineArr.get("CUSTNO"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("gino", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	private JSONObject getInvoiceNoforDono(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ShipHisDAO shipHisDAO = new ShipHisDAO();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String invnumber = StrUtils.fString(request.getParameter("INVOICENO")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
		   
		    String querystrings1 = "DISTINCT INVOICENO AS INVOICE";
		    String querystrings2 = " (INVOICENO !='' or INVOICENO !=null) AND INVOICENO LIKE '%"+invnumber+"%'";
		    Hashtable ht=new Hashtable();
		    if(dono.length()>0)
		    	ht.put("DONO", dono);
		    ht.put("PLANT", plant);
		    
		   
		    ArrayList listQry = shipHisDAO.selectShipHis(querystrings1, ht,querystrings2);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String invoice = (String)m.get("INVOICE");
		
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("INVOICE", invoice);
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("invoices", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
       }catch (Exception e) {
           resultJson.put("SEARCH_DATA", "");
           JSONObject resultJsonInt = new JSONObject();
           resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
           resultJsonInt.put("ERROR_CODE", "98");
           jsonArrayErr.add(resultJsonInt);
           resultJson.put("ERROR", jsonArrayErr);
       }
		return resultJson;
	}
	
	private JSONObject getPLNoforDono(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceDAO invoicedao = new InvoiceDAO();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String plno = StrUtils.fString(request.getParameter("PLNO")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
			String gino = StrUtils.fString(request.getParameter("GINO")).trim();
		   
			
		    String querystrings2 = "AND (PACKINGLIST !='' or PACKINGLIST !=null) AND PACKINGLIST LIKE '%"+plno+"%' ORDER BY PACKINGLIST DESC ";
		    
		    StringBuffer querystrings1 = new StringBuffer("select DISTINCT ISNULL(PACKINGLIST,'') AS PACKINGLIST from " + plant +"_SHIPHIS WHERE PLANT='"+ plant+"' ");
		    Hashtable ht=new Hashtable();
		    if(dono.length()>0)
		    	ht.put("DONO", dono);
		    if(gino.length()>0)
		    	ht.put("INVOICENO", gino);
		    
		   
		    ArrayList listQry = invoicedao.selectForReport(querystrings1.toString(), ht, querystrings2);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String PLNO = (String)m.get("PACKINGLIST");
		
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PLNO", PLNO);
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("plno", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
       }catch (Exception e) {
           resultJson.put("SEARCH_DATA", "");
           JSONObject resultJsonInt = new JSONObject();
           resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
           resultJsonInt.put("ERROR_CODE", "98");
           jsonArrayErr.add(resultJsonInt);
           resultJson.put("ERROR", jsonArrayErr);
       }
		return resultJson;
	}
	
	private JSONObject getPLNoforInvoice(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceDAO invoicedao = new InvoiceDAO();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String plno = StrUtils.fString(request.getParameter("PLNO")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
			String invocie = StrUtils.fString(request.getParameter("INVOICE")).trim();
		   
			
		    String querystrings2 = "AND (PACKINGLIST !='' or PACKINGLIST !=null) AND PACKINGLIST LIKE '%"+plno+"%' ORDER BY PACKINGLIST DESC ";
		    
		    StringBuffer querystrings1 = new StringBuffer("select DISTINCT ISNULL(PACKINGLIST,'') AS PACKINGLIST from " + plant +"_DNPLHDR WHERE INVOICE IS NOT NULL AND PLANT='"+ plant+"' ");
		    Hashtable ht=new Hashtable();
		    if(dono.length()>0)
		    	ht.put("DONO", dono);
		    if(invocie.length()>0)
		    	ht.put("INVOICE", invocie);
		    
		   
		    ArrayList listQry = invoicedao.selectForReport(querystrings1.toString(), ht, querystrings2);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String PLNO = (String)m.get("PACKINGLIST");
		
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PLNO", PLNO);
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("plno", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
       }catch (Exception e) {
           resultJson.put("SEARCH_DATA", "");
           JSONObject resultJsonInt = new JSONObject();
           resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
           resultJsonInt.put("ERROR_CODE", "98");
           jsonArrayErr.add(resultJsonInt);
           resultJson.put("ERROR", jsonArrayErr);
       }
		return resultJson;
	}
	
	private JSONObject getOrderDetailsUsingInvno(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ShipHisDAO shipHisDAO = new ShipHisDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		InvoiceDAO invoicedao = new InvoiceDAO();
		DoHDRService doHDRService = new DoHdrServiceImpl();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String invnumber = StrUtils.fString(request.getParameter("INVOICENO")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
			String replacePreviousSalesCost = StrUtils.fString(request.getParameter("REPLACEPREVIOUSSALESCOST")).trim();
		    
			
			DoHdr doHdr = doHDRService.getDoHdrById(plant, dono);
			
			String shippingcost = invoicedao.getActualShippingCostForinvoice(plant, dono);
			String discountcost = "0";
			if(!doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
				discountcost = invoicedao.getActualOrderDiscountCostForinvoice(plant, dono);
			}else {
				discountcost = StrUtils.addZeroes(doHdr.getORDERDISCOUNT(), "3");
			}
					
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="";
		    
		    String querystrings1 = "A.ISSUEDATE,ISNULL(A.UNITPRICE,0)* ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) UNITPRICE,"
		    					+"ISNULL((SELECT TOP 1 ISNULL(UNITMO,'') UOM FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) UOM,"
		    					+"ISNULL((SELECT TOP 1 ISNULL(UNITCOST,'') DOCOST FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOCOST,"
		    					+"ISNULL((SELECT TOP 1 ISNULL(ADDONAMOUNT,'') DOAOD FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOAOD,"
		    					+"ISNULL((SELECT TOP 1 ISNULL(ADDONTYPE,'') DOAODTYPE FROM ["+ plant +"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) DOAODTYPE,"
		    					+ "A.ITEM,C.ITEMDESC,C.CATLOGPATH,A.PICKQTY,ISNULL((SELECT B.OUTBOUND_GST FROM [" + plant + "_DOHDR] B WHERE A.DONO = B.DONO),0) OUTBOUND_GST,ISNULL(D.TAXTREATMENT,'')  TAXTREATMENT,"
		    					+"ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = D.CURRENCYID),'') DISPLAY,D.CURRENCYID,ISNULL(D.ORDERDISCOUNTTYPE,'') ORDERDISCOUNTTYPE,ISNULL(D.EMPNO,'') EMPNAME,"
		    					+"ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) CURRENCYUSEQT,"
		    					+"ISNULL((SELECT COST FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as UNITCOST,"
		    					+"ISNULL((SELECT INCPRICE FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as INCPRICE,"
		    					+"ISNULL((SELECT CPPI FROM "+ plant +"_ITEMMST E WHERE C.ITEM = E.ITEM),'') as CPPI,"
		    					+"ISNULL((SELECT P.ORDERDISCOUNT FROM ["+ plant +"_DOHDR] P WHERE A.DONO = P.DONO),0) ORDERDISCOUNT,ISNULL(A.UNITPRICE,0) BASECOST,"
		    					+"CAST(ISNULL(A.UNITPRICE,0) * ISNULL((SELECT TOP 1 ISNULL(CURRENCYUSEQT,0) CURRENCYUSEQT FROM ["+plant+"_DODET] T WHERE DONO = A.DONO AND ITEM = A.ITEM AND DOLNNO = A.DOLNO),1) AS DECIMAL(25,5)) AS CONVCOST,"
		    					+ " ISNULL(D.SALES_LOCATION,'') SALES_LOCATION, ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE D.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,a.dolno as LNNO";
		    String querystrings2 = "[" + plant + "_SHIPHIS] A JOIN [" + plant + "_DOHDR] D ON A.DONO = D.DONO JOIN [" + plant + "_ITEMMST] C ON A.ITEM = C.ITEM ";
		  
		    String cgroup =" order by a.dolno";
                   
		    Hashtable ht=new Hashtable();
		    ht.put("INVOICENO", invnumber);
		    ht.put("PLANT", plant);
		    
//		    String sql =""; //imti start
//			String gino = StrUtils.fString(request.getParameter("GINO"));
//			Hashtable htm=new Hashtable();
//			
//			htm.put("INVOICENO",invnumber);
//			if(!dono.equalsIgnoreCase("")){
//				htm.put("A.DONO",dono);
//				sql = "SELECT DOLNO as LNNO,ITEM,ITEMDESC,PICKQTY as QTY,ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0) as COST,CURRENCYID,(PICKQTY*(ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0))) as AMOUNT,ISNULL((SELECT ISNULL(B.OUTBOUND_GST,0) OUTBOUND_GST FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),0) AS GSTPERCENTAGE,ISNULL((SELECT ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_DODET]  WHERE item=a.item and dono=a.dono and DOLNNO=a.DOLNO),0) as UOM from "+plant+"_SHIPHIS A WHERE A.PLANT='"+ plant+"'";
//			}
//			else{
//				sql = "SELECT DOLNO as LNNO,ITEM,ITEMDESC,PICKQTY as QTY,0 as COST,'' as CURRENCYID,(PICKQTY*(ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0))) as AMOUNT,ISNULL((SELECT ISNULL(B.OUTBOUND_GST,0) OUTBOUND_GST FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),0) AS GSTPERCENTAGE,ISNULL((SELECT TOP 1 ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_POSDET]  WHERE item=a.item and POSTRANID=a.INVOICENO and DOLNNO=a.DOLNO),0) as UOM from "+plant+"_SHIPHIS A WHERE A.PLANT='"+ plant+"'";	
//			}
//			List grninvoiceDetList =  invoicedao.selectForReport(sql, htm, " ORDER BY DOLNO "); //imti end
			
		    ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings1,querystrings2, ht,cgroup);
		    
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
//		    		Map md=(Map)grninvoiceDetList.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					//float dCost ="".equals((String)m.get("UNITPRICE")) ? 0.0f :  Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
				
					double dQty = Double.parseDouble((String)m.get("PICKQTY"));
					//float dQty ="".equals((String)m.get("PICKQTY")) ? 0.0f :  Double.parseDouble((String)m.get("PICKQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					DoDet dodet = new DoDetDAO().getDoDetByDonoItm(plant, dono, (String)m.get("ITEM"));
					
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("DOLNNO", StrUtils.fString((String)m.get("LNNO")).trim());  //imti added to get proper line number for item
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					resultJsonInt.put("QTYOR", (String)m.get("PICKQTY"));
					resultJsonInt.put("OUTBOUND_GST", (String)m.get("OUTBOUND_GST"));
					//resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("UOM", StrUtils.fString((String)m.get("UOM")));
                    //resultJsonInt.put("UOM", StrUtils.fString((String)m.get("STKUOM")));
                    //resultJsonInt.put("INVQTY", StrUtils.fString((String)m.get("INVQTY")));
                    resultJsonInt.put("ACCOUNT", dodet.getACCOUNT_NAME());
                    resultJsonInt.put("SALES_LOCATION", (String)m.get("SALES_LOCATION"));
                    resultJsonInt.put("STATE_PREFIX", (String)m.get("STATE_PREFIX"));
                    resultJsonInt.put("TAXTREATMENT", (String)m.get("TAXTREATMENT"));
                    resultJsonInt.put("SHIPPINGCOST", shippingcost);
                    resultJsonInt.put("ORDERDISCOUNT", discountcost);
                    resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
                    resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
                    resultJsonInt.put("DISPLAY", (String)m.get("DISPLAY"));
                    resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
                    resultJsonInt.put("CONVCOST", (String)m.get("CONVCOST"));
                    resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
                    resultJsonInt.put("ITEM_DISCOUNT", dodet.getDISCOUNT());
                    resultJsonInt.put("ITEM_DISCOUNT_TYPE", dodet.getDISCOUNT_TYPE());
                    resultJsonInt.put("ORQTY", dodet.getQTYOR());
                    resultJsonInt.put("EMPNAME", (String)m.get("EMPNAME"));
                    resultJsonInt.put("GOOD_DATE", (String)m.get("ISSUEDATE"));
                    
                    
                  //imthi added to show COST and ADD on COST
                    resultJsonInt.put("UNITCOST", (String)m.get("UNITCOST"));
                    resultJsonInt.put("INCPRICE", (String)m.get("INCPRICE"));
                    String cppi = (String) m.get("CPPI");
                    String incprice = (String) m.get("INCPRICE");
                    String unitcost = (String) m.get("UNITCOST");
                    unitcost = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,(String)m.get("CURRENCYID"), (String)m.get("ITEM"),unitcost);
                    unitcost = StrUtils.addZeroes(Double.parseDouble(unitcost), numberOfDecimal);
                    if(cppi.equalsIgnoreCase("BYPRICE")) {
                    	incprice = new DoHdrDAO().getInvoiceConvertedUnitCostForProductByCurrency(plant,(String)m.get("CURRENCYID"), (String)m.get("ITEM"),incprice); 
                    	incprice = StrUtils.addZeroes(Double.parseDouble(incprice), numberOfDecimal);
                    	cppi = incprice+" "+(String)m.get("CURRENCYID");
                    	if(replacePreviousSalesCost.equalsIgnoreCase("3")) {
                    	Double convl = Double.valueOf(unitcost);
               		 	Double conv2 = Double.valueOf(incprice);
               		 	Double con = (convl+conv2);
               		 	ConvertedUnitCost = Double.toString(con);
               		 	ConvertedUnitCost = StrUtils.addZeroes(con, numberOfDecimal);
                    	resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
                    	dAmount = con * dQty;
    					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
                    	resultJsonInt.put("AMOUNT", ConvertedAmount);
                    	}
                    }else {
                    	cppi = incprice+" "+"%";
                    	if(replacePreviousSalesCost.equalsIgnoreCase("3")) {
                    	 Double convl = Double.valueOf(unitcost);
                		 Double conv2 = Double.valueOf(incprice);
                		 Double Sum = ((convl*conv2)/100);
                		 Double con = Sum+convl;
                		 ConvertedUnitCost = Double.toString(con);
                		 ConvertedUnitCost = StrUtils.addZeroes(con, numberOfDecimal);
                		 resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
                		 dAmount = con * dQty;
     					 ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
                     	 resultJsonInt.put("AMOUNT", ConvertedAmount);
                    	}
                    }
                    resultJsonInt.put("CPPI", (String)m.get("CPPI"));
                    resultJsonInt.put("AOD", cppi);
                    resultJsonInt.put("UCOST", unitcost);
                    //end
                    
                    if(!replacePreviousSalesCost.equalsIgnoreCase("3")) {
                    Double covUCOST = Double.parseDouble((String)m.get("DOCOST"))*Double.parseDouble((String)m.get("CURRENCYUSEQT")); 	
                    resultJsonInt.put("UCOST", StrUtils.addZeroes(covUCOST, numberOfDecimal));
                    //resultJsonInt.put("UCOST", (String)m.get("DOCOST"));
                    resultJsonInt.put("CPPI", (String)m.get("DOAODTYPE"));
                    String docppi = (String) m.get("DOAODTYPE");
                    if(!docppi.equalsIgnoreCase("%")) {
                    	Double covcppi = Double.parseDouble((String)m.get("DOAOD"))*Double.parseDouble((String)m.get("CURRENCYUSEQT")); 
                    	cppi = StrUtils.addZeroes(covcppi, numberOfDecimal) +" "+ (String)m.get("CURRENCYID");
                    	//cppi = (String)m.get("DOAOD")+" "+(String)m.get("CURRENCYID");
                    }else {
                    	cppi = StrUtils.addZeroes(Double.parseDouble((String)m.get("DOAOD")) , "3") +" "+"%";
                    }
                    resultJsonInt.put("AOD", cppi);
                    resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
                    resultJsonInt.put("AMOUNT", ConvertedAmount);
                    }
                    
                    ItemUtil itemUtil = new ItemUtil();
            		List listItem = itemUtil.queryItemMstDetails((String)m.get("ITEM"),plant);
                   
                    if(listItem.size()>0){
                    	Vector arrItem    = (Vector)listItem.get(0);
                    	resultJsonInt.put("outgoingqty", StrUtils.fString((String)arrItem.get(23)));
                		resultJsonInt.put("stkqty", StrUtils.fString((String)arrItem.get(8)));
                		resultJsonInt.put("maxstkqty", StrUtils.fString((String)arrItem.get(21)));
                		resultJsonInt.put("stockonhand", StrUtils.fString((String)arrItem.get(22)));
                		resultJsonInt.put("INVOICEFROM", "INVOICE");
					}else {
						resultJsonInt.put("outgoingqty", "0");
	            		resultJsonInt.put("stkqty", "0");
	            		resultJsonInt.put("maxstkqty", "0");
	            		resultJsonInt.put("stockonhand", "0");
	            		resultJsonInt.put("INVOICEFROM", "EXPENSE");
					}
            		
                    new Hashtable();
            	 	ht.put("item", (String)m.get("ITEM"));
            	 	ht.put("plant", plant);
            	 	Map mest = new EstDetDAO().getEstQtyByProduct(ht);
            	 	String estQty = (String) mest.get("ESTQTY");
            		resultJsonInt.put("EstQty", estQty);
            		
            		mest = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	String avlbQty= (String) mest.get("AVLBQTY");
            		
            		resultJsonInt.put("AvlbQty", avlbQty);
            		
					jsonArray.add(resultJsonInt);					
					
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getInvoiceDetailsUsingInvno(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ShipHisDAO shipHisDAO = new ShipHisDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		MasterUtil masterUtil = new MasterUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String invnumber = StrUtils.fString(request.getParameter("INVOICENO")).trim();
		    
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="";
		    
		   // String querystrings = "UNITPRICE,PICKQTY,ITEM";
		    String querystrings1 = "B.ID,LNNO,INVOICEHDRID,B.ITEM,ISNULL(A.ADJUSTMENT,'0') ADJUSTMENT,ISNULL(A.DISCOUNT,'0') DISCOUNT,ISNULL(B.CREDITNOTE_QTY,'0') CREDITNOTE_QTY,ISNULL(A.DISCOUNT_TYPE,'') DISCOUNT_TYPE,ISNULL(A.ORDER_DISCOUNT,'0') ORDER_DISCOUNT,ACCOUNT_NAME,QTY,B.UNITPRICE,B.DISCOUNT as ITEM_DISCOUNT,TAX_TYPE,ISNULL(CATLOGPATH,'') CATLOGPATH,ITEMDESC,ITEM_RATES,B.DISCOUNT_TYPE as ITEM_DISCOUNT_TYPE,";
		    querystrings1=querystrings1+" ISNULL(SALES_LOCATION,'') SALES_LOCATION,ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE A.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,ISNULL(A.CURRENCYID,'') CURRENCYID,ISNULL((SELECT ISNULL(DISPLAY,'') DISPLAY FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = A.CURRENCYID), '') DISPLAY,ISNULL(A.SHIPPINGID,'') SHIPPINGID,ISNULL(A.SHIPPINGCUSTOMER,'')SHIPPINGCUSTOMER , ";
		    querystrings1=querystrings1+" ISNULL(A.TAXID,'0') TAXID,ISNULL(B.CURRENCYUSEQT,'1') CURRENCYUSEQT,ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.OUTBOUD_GST,'0') OUTBOUD_GST,ISNULL(A.ISORDERDISCOUNTTAX,'0') ISORDERDISCOUNTTAX,ISNULL(A.ISDISCOUNTTAX,'0') ISDISCOUNTTAX,ISNULL(A.ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,ISNULL(A.PROJECTID,'0') PROJECTID ";
		    String querystrings2 = "[" + plant + "_FININVOICEHDR] A JOIN [" + plant + "_FININVOICEDET] B ON A.ID = B.INVOICEHDRID " + " JOIN [" + plant + "_ITEMMST] C ON B.ITEM = C.ITEM ";
		  
		    String cgroup ="ORDER BY B.ITEM";
                   
		    Hashtable ht=new Hashtable();
		    ht.put("INVOICE", invnumber);
		    ht.put("PLANT", plant);
		    
		   // List listQry = doUtil.getOrderDetailsForBilling(ht);
		   // ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings, ht);
		    ArrayList listQry = shipHisDAO.selectShipHisbyjoin(querystrings1,querystrings2, ht,cgroup);
		    
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("QTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					resultJsonInt.put("SALES_LOCATION", (String)m.get("SALES_LOCATION"));
					resultJsonInt.put("STATE_PREFIX", (String)m.get("STATE_PREFIX"));
					resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
					resultJsonInt.put("ITEM_RATES", (String)m.get("ITEM_RATES"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					resultJsonInt.put("QTYOR", (String)m.get("QTY"));
					resultJsonInt.put("CREDITNOTE_QTY", (String)m.get("CREDITNOTE_QTY"));
					resultJsonInt.put("LNNO", (String)m.get("LNNO"));
					resultJsonInt.put("DISCOUNT", (String)m.get("DISCOUNT"));
					resultJsonInt.put("DISCOUNT_TYPE", (String)m.get("DISCOUNT_TYPE"));
					resultJsonInt.put("ORDER_DISCOUNT", (String)m.get("ORDER_DISCOUNT"));
					resultJsonInt.put("ITEM_DISCOUNT", (String)m.get("ITEM_DISCOUNT"));
					resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String)m.get("ITEM_DISCOUNT_TYPE"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));                                        
                    resultJsonInt.put("ACCOUNT", "Inventory Asset");
                    
                    int proid = Integer.valueOf((String)m.get("PROJECTID"));
					String projectname = "";
					if(proid > 0){
						FinProject finProject = finProjectDAO.getFinProjectById(plant, proid);
						projectname = finProject.getPROJECT_NAME();
					}
					
					resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("DISPLAY", (String)m.get("DISPLAY"));
					resultJsonInt.put("TAXID", (String)m.get("TAXID"));
					resultJsonInt.put("PROJECTID", (String)m.get("PROJECTID"));
					resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
					resultJsonInt.put("ISDISCOUNTTAX", (String)m.get("ISDISCOUNTTAX"));
					resultJsonInt.put("ISORDERDISCOUNTTAX", (String)m.get("ISORDERDISCOUNTTAX"));
					resultJsonInt.put("OUTBOUD_GST", (String)m.get("OUTBOUD_GST"));
					resultJsonInt.put("PROJECTNAME", projectname);
					resultJsonInt.put("SHIPPINGID", (String)m.get("SHIPPINGID"));
					resultJsonInt.put("SHIPPINGCUSTOMER", (String)m.get("SHIPPINGCUSTOMER"));
					resultJsonInt.put("ADJUSTMENT", (String)m.get("ADJUSTMENT"));
					
					String slocation = (String)m.get("SALES_LOCATION");
					if(slocation.equalsIgnoreCase("0") || slocation.equalsIgnoreCase("")) {
						resultJsonInt.put("SALESLOCATION", "");
						resultJsonInt.put("SALESPREFIX", "");
					}else {
						
						ArrayList sprefix =  masterUtil.getSalesLocationByState((String)m.get("SALES_LOCATION"), plant, "");
						Map msprefix=(Map)sprefix.get(0);
						
						resultJsonInt.put("SALESLOCATION", (String)m.get("SALES_LOCATION"));
						resultJsonInt.put("SALESPREFIX", (String)msprefix.get("PREFIX"));
					}
					int taxid = Integer.valueOf((String)m.get("TAXID"));
					FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
					
					
					if(taxid > 0) {
						FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
						resultJsonInt.put("PTAXTYPE", fintaxtype.getTAXTYPE());
						resultJsonInt.put("PTAXISZERO", fintaxtype.getISZERO());
						resultJsonInt.put("PTAXSHOW", fintaxtype.getSHOWTAX());
					}else {
						resultJsonInt.put("PTAXTYPE", "");
						resultJsonInt.put("PTAXISZERO", "1");
						resultJsonInt.put("PTAXSHOW", "0");
					}
                    
					jsonArray.add(resultJsonInt);					
					
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getinvoicedetailsview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
//        StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";//,taxby=""
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
           String  INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

           Hashtable ht = new Hashtable();
           
	        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
	        if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
	        if(StrUtils.fString(INVOICENO).length() > 0)       ht.put("INVOICE",INVOICENO);
	        if(StrUtils.fString(STATUS).length() > 0)       ht.put("BILL_STATUS",STATUS);
	        
				movQryList = movHisUtil.getInvoiceDetailSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
                                  
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
                   			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            
                            double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("INVOICE_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("INVOICE")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("DONO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 resultJsonInt.put("isexpense",StrUtils.fString((String)lineArr.get("ISEXPENSE")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 if(cur.isEmpty())
                    		 cur= curency;
                    	 resultJsonInt.put("invoiceid", StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
							/*
							 * resultJsonInt.put("amount",curency+unitCostValue);
							 * resultJsonInt.put("balancedue",curency+balCostValue);
							 */                         jsonArray.add(resultJsonInt);
                }
               
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private HSSFWorkbook writeToExcelInvoiceDetails(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        InvoiceUtil movHisUtil = new InvoiceUtil();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";// taxby = "", custcode = "";
		int SheetId =1;
		try {
			String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String  CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  ITEM = StrUtils.fString(request.getParameter("item"));
	           String  INVOICENO = StrUtils.fString(request.getParameter("invoice"));
	           if(INVOICENO.equalsIgnoreCase(""))
	            INVOICENO = StrUtils.fString(request.getParameter("invoiceno"));
	           String  STATUS = StrUtils.fString(request.getParameter("status"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();

	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
				if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	           Hashtable ht = new Hashtable();
	           
		        if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
		        if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
		        if(StrUtils.fString(INVOICENO).length() > 0)       ht.put("INVOICE",INVOICENO);
		        if(StrUtils.fString(STATUS).length() > 0)       ht.put("BILL_STATUS",STATUS);
		        
				movQryList = movHisUtil.getInvoiceDetailSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "expensebycustomer");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebycustomer");
					sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
					if(TO_DATE.equalsIgnoreCase(""))
						TO_DATE = curDate;
					sheet = this.createHeaderDate(sheet,CompHeader,"",FROM_DATE,TO_DATE,PLANT);
//					CreationHelper createHelper = wb.getCreationHelper();

					int index = 3;
					
//					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, CostwTax = 0;
//					float gstpercentage = 0;
//					String strDiffQty = "", deliverydateandtime = "";
//					DecimalFormat decformat = new DecimalFormat("#,##0.00");
					
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
               			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
               			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00000";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                        unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                        
                        double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                        if(balCostVal==0f){
                        	balCostValue="0.00000";
                        }else{
                        	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("BILL_STATUS"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("INVOICE_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DUE_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("INVOICE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DONO"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(unitCostValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(balCostValue)));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	private JSONObject getCustomerBalances(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        InvoiceUtil movHisUtil       = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
//        StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

           Hashtable ht = new Hashtable();
	        
           movQryList = movHisUtil.getCustomerBalances(ht,fdate,tdate,PLANT,CUSTOMER);	
			
           if (movQryList.size() > 0) {
        	   int Index = 0;
//        	   int iIndex = 0,irow = 0;
                                  
        	   for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                    String result="";
                    Map lineArr = (Map) movQryList.get(iCnt);                            
                       
//                    String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                    JSONObject resultJsonInt = new JSONObject();
           			String unitCostValue = (String)lineArr.get("BALANCE_DUE");
           			String balCostValue = (String)lineArr.get("UNUSED_AMOUNT");
           			String balanceValue = (String)lineArr.get("BALANCE");
           			
           			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                    if(unitCostVal==0f){
                    	unitCostValue="0.00000";
                    }else{
                    	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                    
                    unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                    
                    double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                    if(balCostVal==0f){
                    	balCostValue="0.00000";
                    }else{
                    	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                    balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
                    
                    double balanceCostVal ="".equals(balanceValue) ? 0.0f :  Double.parseDouble(balanceValue);
                    if(balanceCostVal==0f){
                    	balanceValue="0.00000";
                    }else{
                    	balanceValue=balanceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                    balanceValue = StrUtils.addZeroes(Double.parseDouble(balanceValue), numberOfDecimal);
                      
                	 Index = Index + 1;
                	 resultJsonInt.put("Index",(Index));
                	 resultJsonInt.put("cname",StrUtils.fString((String)lineArr.get("CNAME")));
                	 resultJsonInt.put("invoicebalance",unitCostValue);
                	 resultJsonInt.put("availablecredit",balCostValue);
                	 resultJsonInt.put("balance",balanceValue);
						/*
						 * resultJsonInt.put("invoicebalance",curency+unitCostValue);
						 * resultJsonInt.put("availablecredit",curency+balCostValue);
						 * resultJsonInt.put("balance",curency+balanceValue);
						 */                     jsonArray.add(resultJsonInt);
                }
               
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private HSSFWorkbook writeToExcelCustomerBalances(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        InvoiceUtil movHisUtil = new InvoiceUtil();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "", custcode = "";
		int SheetId =1;
		try {
			String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String  CUSTOMER = StrUtils.fString(request.getParameter("CUST_CODE"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();

	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
				if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	           Hashtable ht = new Hashtable();
				movQryList = movHisUtil.getCustomerBalances(ht,fdate,tdate,PLANT,CUSTOMER);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "customerbalance");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "customerbalance");
					sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
					if(TO_DATE.equalsIgnoreCase(""))
						TO_DATE = curDate;
					sheet = this.createHeaderDate(sheet,CompHeader,"",FROM_DATE,TO_DATE,PLANT);
//					CreationHelper createHelper = wb.getCreationHelper();

					int index = 3;
					
//					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, CostwTax = 0;
//					float gstpercentage = 0;
//					String strDiffQty = "", deliverydateandtime = "";
//					DecimalFormat decformat = new DecimalFormat("#,##0.00");
					
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String unitCostValue = (String)lineArr.get("BALANCE_DUE");
	           			String balCostValue = (String)lineArr.get("UNUSED_AMOUNT");
	           			String balanceValue = (String)lineArr.get("BALANCE");
	           			
	           			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
	                    if(unitCostVal==0f){
	                    	unitCostValue="0.00000";
	                    }else{
	                    	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                    }
	                    
	                    unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
	                    
	                    double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
	                    if(balCostVal==0f){
	                    	balCostValue="0.00000";
	                    }else{
	                    	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                    }
	                    balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
	                    
	                    double balanceCostVal ="".equals(balanceValue) ? 0.0f :  Double.parseDouble(balanceValue);
	                    if(balanceCostVal==0f){
	                    	balanceValue="0.00000";
	                    }else{
	                    	balanceValue=balanceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                    }
	                    balanceValue = StrUtils.addZeroes(Double.parseDouble(balanceValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(unitCostValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(balCostValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(balanceValue)));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "customerbalance");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "customerbalance");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
		int k = 0;
		try{
			String CNAME = "", CZIP = "", CCOUNTRY = "", CSTATE="";//CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CTEL = "",CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="" 
			PlantMstUtil pmUtil = new PlantMstUtil();
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
								
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
										
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createHeaderDate(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,
			String FROM_DATE,String TO_DATE,String PLANT){
		int k = 0;
		try{
			
			
		HSSFRow rowhead = sheet.createRow(1);
		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("From "+FROM_DATE+" To "+TO_DATE));	
		cell.setCellStyle(styleHeader);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 1, 0, 4);
		sheet.addMergedRegion(cellRangeAddress);
										
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFCellStyle createStyleHeader(HSSFWorkbook wb) {

		// Create style
		HSSFCellStyle styleHeader = wb.createCellStyle();
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontHeader.setFontName("Arial");
		styleHeader.setFont(fontHeader);
		styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleHeader.setWrapText(true);
		return styleHeader;
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
	
	private HSSFCellStyle createDataStyle(HSSFWorkbook wb) {
		// Create style
		HSSFCellStyle dataStyle = wb.createCellStyle();
		dataStyle.setWrapText(true);
		return dataStyle;
	}
	
	private HSSFSheet createWidthExpensesSummary(HSSFSheet sheet, String type) {
		int i = 0;
		try {
				sheet.setColumnWidth(i++, 1000);
				if(type.equalsIgnoreCase("customerbalance")) {
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
				}else {
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 4000);
					sheet.setColumnWidth(i++, 4000);
					sheet.setColumnWidth(i++, 5000);	
				}
				
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createHeaderExpensesSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(2);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);
			if(type.equalsIgnoreCase("customerbalance")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Customer name"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Invoice balance"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Available Credits"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Balance"));
				cell.setCellStyle(styleHeader);
			}else{
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Invoice Date"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Due Date"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Invoice"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Balance Due"));
				cell.setCellStyle(styleHeader);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private Map getOutstandingAmountForCustomer(String custCode, double orderAmount, String plant) throws Exception{
		String outstdamt = "", creditLimitBy ="", creditLimit = "";//, creditBy = "";
		Map resultMap = new HashMap();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
		
		CustUtil custUtil = new CustUtil();
    	ArrayList arrCust = custUtil.getCustomerDetails(custCode,plant);
		creditLimit   = (String)arrCust.get(24);
		creditLimitBy   = (String)arrCust.get(35);
		
		if(creditLimitBy.equalsIgnoreCase("NOLIMIT")) {
			resultMap.put("STATUS", false);
			resultMap.put("MSG", "");
		}else {
			DateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date = new Date();
	    	String current_date=currentdate.format(date); 
	    	
	    	Calendar firstDayOfMonth = Calendar.getInstance();   
	    	firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
	    	String startDate = currentdate.format(firstDayOfMonth.getTime()); 
	    	System.out.println(firstDayOfMonth.getTime()+"  "+startDate);
	    	
	        Calendar lastDayOfMonth = Calendar.getInstance();
	        lastDayOfMonth.set(Calendar.DATE, lastDayOfMonth.getActualMaximum(Calendar.DATE));
	        String endDate= currentdate.format(lastDayOfMonth.getTime());
	        System.out.println(firstDayOfMonth.getTime()+"  "+endDate);
	        
	        if(creditLimitBy.equalsIgnoreCase("DAILY")) {
	        	startDate = current_date;
	        	endDate = current_date;
	        }
	        try {
				outstdamt = new InvoicePaymentUtil().getOutStdAmt(plant,custCode, startDate, endDate);
				double allowedCreditval = Double.parseDouble(creditLimit)-((Double.parseDouble(outstdamt)+orderAmount)); 
				creditLimit = StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal);
				if(allowedCreditval >= 0){
					resultMap.put("STATUS", false);
					resultMap.put("MSG", "");
				} else {
					resultMap.put("STATUS", true);
					resultMap.put("MSG", "Invoice amount cannot exceed "+ custCode +" Available Credit Limit " 
					+ _PlantMstDAO.getBaseCurrency(plant) + " " + creditLimit);
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return resultMap;
	}
	private JSONObject getginotoinvoiceview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        InvoiceUtil invoiceUtil = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
//        StrUtils strUtils = new StrUtils();
        String fdate="",tdate="";//,taxby=""
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  cur = StrUtils.fString(request.getParameter("CURENCY"));
		   String  GINO = StrUtils.fString(request.getParameter("GINO"));
		   String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
		   String  PLNO = StrUtils.fString(request.getParameter("PLNO"));
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("DONO",ORDERNO);
		   if(StrUtils.fString(GINO).length() > 0)       ht.put("GINO",GINO);
		   if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
		   if(StrUtils.fString(PLNO).length() > 0)       ht.put("PLNO",PLNO);
		   movQryList = invoiceUtil.getGINOToInvoiceSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
		   if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
//            double sumprdQty = 0;
//            String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			
                   			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("date",StrUtils.fString((String)lineArr.get("CRAT")));
                    	 resultJsonInt.put("dono",StrUtils.fString((String)lineArr.get("DONO")));
                    	 resultJsonInt.put("gino",StrUtils.fString((String)lineArr.get("GINO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("cust_name",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("qty",StrUtils.fString((String)lineArr.get("QTY")));
                    	 resultJsonInt.put("invoicedqty",StrUtils.fString((String)lineArr.get("INVOICEDQTY")));
                    	 resultJsonInt.put("returnedqty",StrUtils.fString((String)lineArr.get("RETURNEDQTY")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
                    	 resultJsonInt.put("currency",cur);
                    	 resultJsonInt.put("amount",unitCostValue);                    	 
                         jsonArray.add(resultJsonInt);
                    	            	

                }
               
                    resultJson.put("items", jsonArray);
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                    resultJsonInt.put("ERROR_CODE", "100");
                    jsonArrayErr.add(resultJsonInt);
                    resultJson.put("errors", jsonArrayErr);
            } else {
                    JSONObject resultJsonInt = new JSONObject();
                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                    resultJsonInt.put("ERROR_CODE", "99");
                    jsonArrayErr.add(resultJsonInt);
                    jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		jsonArray.add("");
        		resultJson.put("items", jsonArray);
                resultJson.put("SEARCH_DATA", "");
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }

        return resultJson;        
        
}
	public boolean processShipHis(Map map) throws Exception {
		Hashtable htInvMst = new Hashtable();
		Hashtable htPickDet = new Hashtable();
//		StrUtils su = new StrUtils();
		String extCond = "", ExpiryDate="";
		boolean pickdet= false;
		
		String nonstocktype =  new ItemMstDAO().getNonStockFlag((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
		if(!nonstocktype.equalsIgnoreCase("Y"))	
	    {
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
				
		ExpiryDate= _InvMstDAO.getInvExpireDate( (String)map.get(IConstants.PLANT), 
				(String)map.get(IConstants.ITEM),(String) map.get(IConstants.LOC),
				(String)map.get(IConstants.BATCH));
	    }
		
		htPickDet.clear();
		htPickDet.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
		htPickDet.put("DONO", (String)map.get(IConstants.DODET_DONUM));
		htPickDet.put("DOLNO", (String)map.get(IConstants.DODET_DOLNNO));
		htPickDet.put(IDBConstants.CUSTOMER_NAME, StrUtils.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
		htPickDet.put(IDBConstants.ITEM, (String)map.get(IConstants.ITEM));
		htPickDet.put(IDBConstants.ITEM_DESC,StrUtils.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
		htPickDet.put("BATCH", (String)map.get(IConstants.BATCH));
		htPickDet.put(IDBConstants.LOC, (String)map.get(IConstants.LOC));
		htPickDet.put("LOC1", (String)map.get(IConstants.LOC));
		htPickDet.put("ORDQTY", (String)map.get(IConstants.ORD_QTY));
		htPickDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htPickDet.put(IDBConstants.CREATED_BY, (String)map.get(IConstants.LOGIN_USER));
		htPickDet.put("CONTAINER", "NOCONTAINER");
		htPickDet.put("REMARK", "");
		htPickDet.put("ExpiryDat", ExpiryDate);
		
		htPickDet.put(IDBConstants.CURRENCYID, (String)map.get(IConstants.CURRENCYID));
		htPickDet.put("UNITPRICE", (String)map.get("UNITPRICE"));
		String SHIPPINGNO = GenerateShippingNo((String)map.get(IConstants.PLANT), (String)map.get(IConstants.LOGIN_USER));
		htPickDet.put(IDBConstants.USERFLD1, SHIPPINGNO);
		htPickDet.put("STATUS", "C");
		htPickDet.put(IDBConstants.ISSUEDATE,  (String)map.get(IConstants.ISSUEDATE));
		htPickDet.put(IDBConstants.INVOICENO, (String)map.get("GINO"));
		if(nonstocktype.equals("Y"))	
	    {
        	htPickDet.put("PICKQTY", String.valueOf(map.get(IConstants.QTY)));
        	pickdet = new DoDetDAO().insertPickDet(htPickDet);
	    }
        else
        {
			
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				extCond = "QTY >= " + map.get(IConstants.QTY);
			}else{
				extCond = "QTY > 0";
			}
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					
					htPickDet.put("PICKQTY", String.valueOf(adjustedQuantity));
					htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					pickdet = new DoDetDAO().insertPickDet(htPickDet);	
					quantityToAdjust -= adjustedQuantity;
				}
			}
        }
		return pickdet;
	}
	
	private boolean processInvRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			//String cond = " QTY >= " + map.get(IConstants.QTY);
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + inqty;
			}else{
				extCond = "QTY > 0";
			}

			flag = _InvMstDAO.isExisit(htInvMst, extCond);

			if (flag) {
				//	Get details in ascending order of CRAT for that batch
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				}

				ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);

				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
					quantityToAdjust = quantityToAdjust * Double.valueOf((String)map.get("UOMQTY"));
					Iterator iterStock = alStock.iterator();
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;						
						StringBuffer sql1 = new StringBuffer(" SET ");
						sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");
						Hashtable htInvMstReduce = new Hashtable();
						htInvMstReduce.clear();
						htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
						htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
						htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
						htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
						htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
						flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
						if (!flag) {
							throw new Exception("Could not update");
						}
						quantityToAdjust -= adjustedQuantity;
					}
				}
			} else {
				throw new Exception("Error in picking OutBound Order : Inventory not found for the product: " +map.get(IConstants.ITEM)+ " with batch: " +map.get(IConstants.BATCH)+ "  scanned at the location  "+map.get(IConstants.LOC));
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
	
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htMovHis = new Hashtable();
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovHis.put("DIRTYPE", TransactionConstants.ORD_PICK_ISSUE);
			htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovHis.put("BATNO", map.get(IConstants.BATCH));
			htMovHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htMovHis.put(IDBConstants.POHDR_JOB_NUM, "");
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
			htMovHis.put("MOVTID", "OUT");
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			if((String)map.get(IConstants.UOM)!=null)
			htMovHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
			htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htMovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes((String)map.get(IConstants.INVOICENO)));
			
			flag = movHisDao.insertIntoMovHis(htMovHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	private boolean processBOM(Map map) throws Exception {
		boolean isExists = false;
//		boolean itemflag=false;
		boolean flag=true;
//		String extcond="";
		
//		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		
		try
		{
			Hashtable htBomMst = new Hashtable();
			htBomMst.clear();
			htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			htBomMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			isExists = _BomDAO.isExisit(htBomMst);
			
			if(isExists)
			{				
				 Hashtable htUpdateBOM = new Hashtable();
    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",   map.get(IConstants.LOC));
    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
    		
    			 StringBuffer sql1 = new StringBuffer(" SET ");
 				 sql1.append(" " +"STATUS" + " = 'C' ");
				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '" + DateUtils.getDateTime() + "'");
 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
		    } 
		return flag;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

	}
	
	private String GenerateShippingNo(String plant, String loginuser) {

		String PLANT = "";
//		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
//		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnShippNo = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht
					.put(IDBConstants.TBL_FUNCTION,
							IDBConstants.TBL_SHIPPING_CAPTION);

			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, plant);

			if (exitFlag == false) {

//				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) DateUtils.getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "GENERATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
//					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnShippNo = plant + updatedSeq;

//				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "Shipping");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "UPDATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
//					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return rtnShippNo;
	}
	
	public boolean processShipHisReversal(Map map) throws Exception {
		boolean flag = false;
		try {
			// update receive qty in podet
			DoDetDAO _DoDetDAO = new DoDetDAO();
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			ShipHisDAO _ShipHisDAO = new ShipHisDAO();
//			ReturnOrderDAO returnOrderDao = new ReturnOrderDAO();
			_DoDetDAO.setmLogger(mLogger);
			_DoHdrDAO.setmLogger(mLogger);
			_ShipHisDAO.setmLogger(mLogger);
			
			
			Hashtable htCondiShipHis = new Hashtable();
//			StringBuffer query = new StringBuffer("");
			
			htCondiShipHis.put("PLANT", map.get("PLANT"));
			htCondiShipHis.put("dono", map.get("DONO"));
			if((String) map.get("ISPDA") != "GIWP")
			htCondiShipHis.put("dolno", map.get("DOLNNO"));
			else
				htCondiShipHis.put("UNITPRICE", map.get("UNITPRICE"));
			htCondiShipHis.put("item", map.get("ITEM"));
			htCondiShipHis.put("batch", map.get("BATCH"));
			htCondiShipHis.put("LOC1", map.get("LOC"));
			//htCondiShipHis.put("LOC", map.get("LOC"));
			htCondiShipHis.put("INVOICENO", map.get("GINO"));
			htCondiShipHis.put("STATUS","C");
			String qty = String.valueOf(map.get("QTY"));
			double reverseqty = Double.parseDouble(qty);
			
			ALIssueddetails = new ArrayList();
			ALIssueddetails = _ShipHisDAO.getIssuedDetailsforreverse(map.get("PLANT").toString(), htCondiShipHis);
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					
					if(reverseqty == 0) break;
					
//					  String dono = (String)m.get("dono");
					  double issueqty = Double.parseDouble((String)m.get("PICKQTY"));
			          String crat = (String)m.get("CRAT");
			          String ID = (String)m.get("ID").toString();
			          
			          String updateissuedet = "";
			          if(reverseqty >= issueqty)
			          updateissuedet = "set pickqty =pickqty -" + issueqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          else
			          {
			        	  updateissuedet = "set pickqty =pickqty -" + reverseqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			        	  issueqty=reverseqty;
			          }
			          htCondiShipHis.put("CRAT", crat);
			          if(!ID.equalsIgnoreCase("0"))
			        	  htCondiShipHis.put("ID", ID);
			          if(reverseqty>0)
			          {
			          boolean isexists = _ShipHisDAO.isExisit(htCondiShipHis, "");
						 if(isexists)
						 {        			
							 
					        	  flag = _ShipHisDAO.update(updateissuedet, htCondiShipHis, "");
					        	  reverseqty=reverseqty-issueqty;
					     }
					 }
				}
				htCondiShipHis.remove("CRAT");
				htCondiShipHis.remove("ID");
			}		
			
			htCondiShipHis.put("PICKQTY", "0.000");
			boolean isexists = _ShipHisDAO.isExisit(htCondiShipHis, "");
			if(isexists){
				flag = _ShipHisDAO.deleteSHIPHIS(htCondiShipHis);
			}
			
			if (!flag) {
				flag = false;
				throw new Exception("Product Reversed  Failed, Error In update Ships :"+ " " + map.get("ITEM"));
			} else {
				flag = true;
			}					
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}     

	return flag;
	}
	
	private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();

			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String qty = String.valueOf(map.get("QTY"));
			double reverseqty = Double.parseDouble(qty);
			
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					{
						if(reverseqty==0) break;
						String ID = (String)m.get("ID").toString();
						String PQTY = (String)m.get("PICKQTY").toString();
						double issueqty = Double.parseDouble(PQTY);
						htInvMst.put(IDBConstants.INVID, ID);
				        	
			StringBuffer sql1 = new StringBuffer(" SET ");
			//sql1.append(IDBConstants.QTY + " = QTY +'" + map.get(IConstants.QTY) + "'");
			 if(reverseqty >= issueqty)
			 {
				 double inqty = Double.valueOf(PQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
			 }
			 else
			 {
				 double inqty = Double.valueOf(PQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
				 issueqty=reverseqty;
			 }
			 
			sql1.append("," + IDBConstants.UPDATED_AT + " = '"
					+ DateUtils.getDateTime() + "'");

			flag = _InvMstDAO.isExisit(htInvMst, "");
			if(flag){
				if(reverseqty>0)
				{
					flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
				reverseqty = (reverseqty-issueqty);
				}

			} else {
				if(reverseqty >= issueqty)
				{
					double inqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.QTY, inqty);
				}
				else
				{
					double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.QTY, inqty);
				}
				htInvMst.put(IDBConstants.USERFLD3,"");
				htInvMst.put(IDBConstants.CREATED_AT,  map.get(IConstants.TRAN_DATE).toString().replaceAll("-", "").replaceAll("/", "") + "000000");
				htInvMst.put(IDBConstants.STATUS, "");
				htInvMst.put(IConstants.EXPIREDATE,"");
				flag = _InvMstDAO.insertInvMst(htInvMst);
				
			}
					}
				}
				 ALIssueddetails = new ArrayList();
				}
			if((String) map.get("ISPDA") != "GIWP")
			{
			if(flag)
			{
				boolean bomexistsflag = false;
//				boolean bomupdateflag = false;
				BomDAO _BomDAO =new BomDAO() ;
				Hashtable htBomMst = new Hashtable();
				htBomMst.clear();
				htBomMst.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
				htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

				bomexistsflag = _BomDAO.isExisit(htBomMst);
			
			if(bomexistsflag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.BATCH));
				
				StringBuffer bomsql = new StringBuffer(" SET ");
				bomsql.append(" " +"STATUS" + " = 'A' ");
				
//				bomupdateflag=
						_BomDAO.update(bomsql.toString(),htBomUpdateMst,"");
			}
		  }
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {

			Hashtable htMovHis = new Hashtable();
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			if((String) map.get("ISPDA") == "GIWP")
			{
				htMovHis.put("DIRTYPE", TransactionConstants.REVERSE_DIRECT_TAX_INVOICE);
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd((String) map.get("ISSUEDQTY")));
			}
			else
			{
				htMovHis.put("DIRTYPE", TransactionConstants.SALES_RETURN);
				htMovHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			}
			htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovHis.put("BATNO", map.get(IConstants.BATCH));
			String qty = String.valueOf(map.get("QTY"));
			htMovHis.put(IDBConstants.QTY, qty);
			htMovHis.put("MOVTID", "IN");
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get("SORETURN"));
			htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));		
			htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htMovHis.put("REMARKS", map.get(IConstants.CUSTOMER_NAME)+","+map.get(IConstants.DODET_DONUM)+","+map.get("GINO")+","+map.get("INVOICE")+","+map.get("NOTES"));

			flag = movHisDao.insertIntoMovHis(htMovHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	private JSONObject getOrderTypeForAutoSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        OrderTypeBeanDAO OrderTypeBeanDAO = null;
        OrderTypeBeanDAO = new OrderTypeBeanDAO();
        try {
//        	StrUtils strUtils = new StrUtils();
//        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	String OTYPE = StrUtils.fString(request.getParameter("OTYPE"));
        	
        	
        
        	ArrayList listQry = OrderTypeBeanDAO.getOrderTypeList(plant,OTYPE,QUERY);
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ORDERTYPE", StrUtils.fString((String)m.get("ORDERTYPE")));
                    resultJsonInt.put("ORDERDESC", StrUtils.fString((String)m.get("ORDERDESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
	        		resultJson.put("ORDERTYPEMST", jsonArray);
	                JSONObject resultJsonInt = new JSONObject();
	                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	                resultJsonInt.put("ERROR_CODE", "100");
	                jsonArrayErr.add(resultJsonInt);
	                resultJson.put("errors", jsonArrayErr);
        	} else {
        		 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 resultJson.put("errors", jsonArrayErr);              
        	}
//        	 resultJson.put("ORDERTYPEMST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private void invoiceImportTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInvoiceTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"InvoiceData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
//		java.net.URL url = new java.net.URL("file://" + path);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(
				file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);

		int i;
//		byte[] b = new byte[10];
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}

		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();

	}
	
	private void InvoiceDataImportTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInvoiceTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"InvoiceData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
//		java.net.URL url = new java.net.URL("file://" + path);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(
				file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);

		int i;
//		byte[] b = new byte[10];
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
		String StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
		String StrSheetName = StrUtils.fString(request.getParameter("SheetName"));
		String orgFilePath = StrFileName;
		System.out.println("Import File  *********:" + orgFilePath);
		System.out.println("Import File  *********:" + StrFileName);
		System.out.println("Import sheet *********:" + StrSheetName);
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

		try {
			com.oreilly.servlet.MultipartRequest mreq = null;
			try {
				//Author: Azees  Create date: July 23,2022  Description: Fixed Import Null Error
				mreq = new com.oreilly.servlet.MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			String USERID =(String) request.getSession().getAttribute("LOGIN_USER");
			String NOOFORDER =(String) request.getSession().getAttribute("NOOFORDER");
			request.getSession().setAttribute("IMP_OUTBOUNDRESULT_INVOICE", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			System.out.println("After conversion Import File  *********:"+ StrFileName);
			
			boolean limitstaus = true;
			
			
			InvoiceDAO invoiceDAO = new InvoiceDAO();
//			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = DateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = DateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int noordvalid =invoiceDAO.Invoicecount(PLANT,FROM_DATE,TO_DATE);
			int convl = 0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				convl = Integer.valueOf(NOOFORDER);
				if(noordvalid>=convl)
				{
					limitstaus = false;
				}
			}
        	
        	if(limitstaus) {

				CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
				//Modified by Bruhan for base CUrrency
				String baseCurrency = (String) request.getSession().getAttribute("BASE_CURRENCY");
				alRes = util.downloadInvoiceSheetData(PLANT, StrFileName,
						StrSheetName,baseCurrency,USERID,NOOFORDER);
				
	        	List<BillHdr> billhdrlist = new ArrayList<BillHdr>();
				if (alRes.size() > 0) {
					
					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
		        	{
						for (int j = 0; j < alRes.size(); j++) {
							Map billnumber=(Map)alRes.get(j);
							String invoice = (String)billnumber.get("INVOICE");
							
							List<BillHdr> billhdrlistCheck = billhdrlist.stream().filter((b)->b.getBILL().equalsIgnoreCase(invoice)).collect(Collectors.toList());
							if(billhdrlistCheck.size() == 0) {
								BillHdr billHdr = new BillHdr();
								billHdr.setBILL(invoice);
								noordvalid = noordvalid + 1;
							}
							if(noordvalid>convl)
			        		{
			        			limitstaus = false;
			        			break;
			        		}
				          	
						}
						
						int index = alRes.size() - 1; 
						alRes.remove(index);
						
						if(limitstaus)
						{
						result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
						}
						else
						{
							result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" invoice's you can create</font>";
						}
		        	}else
					{
		        		result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
					}
				} else {
					throw new Exception(
							"no data found  for given data in the excel");
				}
        	}else {
        		result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" invoice's you can create</font>";
        	}

			/*if (alRes.size() > 0) {
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}*/

		} catch (Exception e) {

			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("OUTBOUNDRESULT_INVOICE", result);
		request.getSession().setAttribute("IMP_OUTBOUNDRESULT_INVOICE", alRes);
		//       
		response
				.sendRedirect("../invoice/import?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	
	private void onConfirmInvoiceSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
//		StrUtils strUtils = new StrUtils();
//		DateUtils dateutils = new DateUtils();
//		VendMstDAO vendMstDAO = new VendMstDAO();
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
//		MasterUtil _MasterUtil = new MasterUtil();
		
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDRESULT_INVOICE");

			List<InvoiceHdr> invoicehdrlist = new ArrayList<InvoiceHdr>();
			List<InvoiceDet> invoicedetlist = new ArrayList<InvoiceDet>();
			List<InvoicePojo> invoicepojolist = new ArrayList<InvoicePojo>();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
					String invoiceCheck = (String)lineArr.get("INVOICE");
					List<InvoiceHdr> invoicehdrlistCheck = invoicehdrlist.stream().filter((b)->b.getInvoiceNo().equalsIgnoreCase(invoiceCheck)).collect(Collectors.toList());
					
					String tran = (String)lineArr.get(IConstants.TRANSPORTID);
					int trans = Integer.parseInt(tran);
					CustUtil custUtil = new CustUtil();
					String shipname = (String)lineArr.get("SHIPPINGCUSTOMER");
					 ArrayList arrCustCUS = custUtil.getCustomerDetails(shipname,PLANT);
					 shipname = (String) arrCustCUS.get(1);
					 
					if(invoicehdrlistCheck.size() == 0) {
						String custcode = (String)lineArr.get("CUSTOMERCODE");
						String custname = "";
						if(!custcode.isEmpty()){
							ArrayList arrList = new ArrayList();
							CustMstDAO movHisDAO= new CustMstDAO();
							Hashtable htData =new Hashtable();
							htData.put("PLANT", PLANT);
							htData.put("CUSTNO", custcode);
							arrList = movHisDAO.selectCustMst("CNAME", htData,"");
							Map m = (Map) arrList.get(0);
							custname = (String) m.get("CNAME");
						}
						
						InvoiceHdr invoicehdr = new InvoiceHdr();
						invoicehdr.setCustCode((String)lineArr.get("CUSTOMERCODE"));
						invoicehdr.setCustName(custname);
						invoicehdr.setCustName1("");
						invoicehdr.setInvoiceNo((String)lineArr.get("INVOICE"));
						invoicehdr.setGino((String)lineArr.get("GINO"));
						invoicehdr.setDono((String)lineArr.get("DONO"));
						invoicehdr.setInvoiceDate((String)lineArr.get("INVOICEDATE"));
						invoicehdr.setDueDate((String)lineArr.get("DUEDATE"));
						invoicehdr.setTRANSPORTID(trans);
						invoicehdr.setPayTerms((String)lineArr.get("PAYMENTTERMS"));
						invoicehdr.setORDERTYPE((String)lineArr.get("ORDERTYPE"));
						invoicehdr.setEmpno((String)lineArr.get("EMPLOYEEID"));
						String empno = (String)lineArr.get("EMPLOYEEID");
						if(empno.equalsIgnoreCase("") || empno == null || empno.equals(null)) {
							invoicehdr.setEmpName("");
						}else {
//							EmployeeDAO employeeDAO = new EmployeeDAO();
//							String employename = employeeDAO.getEmpname(PLANT, empno, "");
							invoicehdr.setEmpName((String)lineArr.get(""));
						}
						invoicehdr.setItemRates((String)lineArr.get("ISTAXINCLUSIVE"));
						invoicehdr.setDiscount((String)lineArr.get("DISCOUNT"));
						invoicehdr.setDiscountType((String)lineArr.get("DISCOUNTTYPE"));
						invoicehdr.setOrderdiscount((String)lineArr.get("ORDERDISCOUNT"));
						invoicehdr.setShippingCost((String)lineArr.get("SHIPPINGCOST"));
						invoicehdr.setAdjustment((String)lineArr.get("ADJUSTMENT"));
						invoicehdr.setSubTotal("");
						invoicehdr.setTotalAmount("");
						invoicehdr.setTaxamount("");
						invoicehdr.setInvoiceStatus("Open");
						invoicehdr.setNote((String)lineArr.get("NOTES"));
						invoicehdr.setTerms((String)lineArr.get("TERMSANDCONDITION"));
						invoicehdr.setCmd("");
						invoicehdr.setTranId("");
						invoicehdr.setSalesloc((String)lineArr.get("SALESLOC"));
						invoicehdr.setIsexpense("0");
						invoicehdr.setTaxtreatment("");
						invoicehdr.setShipId((String)lineArr.get("SHIPPINGID"));
						invoicehdr.setShipCust(shipname);
						invoicehdr.setOrigin("manual");
						invoicehdr.setDeductInv((String)lineArr.get("ISINVENTORY"));
						invoicehdr.setIncoterm((String)lineArr.get("PAYMENTTERMS"));
						invoicehdr.setCurrencyid((String)lineArr.get("CURRENCYID"));
						invoicehdr.setCurrencyuseqt((String)lineArr.get("EQUIVALENTCURRENCY"));
						invoicehdr.setOrderdiscstatus((String)lineArr.get("ISORDERDISCOUNTTAX"));
						invoicehdr.setDiscstatus((String)lineArr.get("ISDISCOUNTTAX"));
						invoicehdr.setShipstatus((String)lineArr.get("ISSHIPPINGTAX"));
						invoicehdr.setTaxid((String)lineArr.get("TAXID"));
						invoicehdr.setOrderdisctype((String)lineArr.get("ORDERDISCOUNTTYPE"));
						invoicehdr.setProjectid((String)lineArr.get("PROJECTID"));
						invoicehdr.setGst((String)lineArr.get("TAXPERCENTAGE"));
						invoicehdr.setJobNum("");
						invoicehdr.setPersonIncharge("");
						invoicehdr.setCtype("");
						invoicehdr.setFitem("");
						invoicehdr.setFloc("");
						invoicehdr.setFloc_type_id("");
						invoicehdr.setFloc_type_id2("");
						invoicehdr.setFmodel("");
						invoicehdr.setFuom("");
						
						invoicehdrlist.add(invoicehdr);
					}
					
					InvoiceDet invoiceDet = new InvoiceDet();
					
					invoiceDet.setInvoice((String)lineArr.get("INVOICE"));
					invoiceDet.setItem((String)lineArr.get("PRODUCT"));
					invoiceDet.setEdit_item("");
					invoiceDet.setAccountName((String)lineArr.get("ACCOUNT"));
					invoiceDet.setQty((String)lineArr.get("QTY"));
					invoiceDet.setEdit_qty("");
					invoiceDet.setCost((String)lineArr.get("UNITPRICE"));
					invoiceDet.setDetDiscount((String)lineArr.get("PRODUCTDISCOUNT"));
					invoiceDet.setDetDiscounttype((String)lineArr.get("PRODUCTDISCOUNT_TYPE"));
					invoiceDet.setTaxType((String)lineArr.get("TAX"));
					
					double amount = Double.valueOf((String)lineArr.get("QTY")) * Double.valueOf((String)lineArr.get("UNITPRICE"));
					double pdiscount = Double.valueOf((String)lineArr.get("PRODUCTDISCOUNT"));
					String pdiscounttype = (String)lineArr.get("PRODUCTDISCOUNT_TYPE");
					double lamount = 0;
					if(pdiscounttype.equalsIgnoreCase("%")) {
						lamount = amount - ((amount/100)*pdiscount);
					}else {
						lamount = amount - pdiscount;
					}
					invoiceDet.setAmount(String.valueOf(lamount));
					invoiceDet.setDETID("");
					invoiceDet.setNotesexp("");
					invoiceDet.setLoc((String)lineArr.get("LOCATIONID"));
					invoiceDet.setUom((String)lineArr.get("UOM"));
					invoiceDet.setBatch((String)lineArr.get("BATCH"));
					invoiceDet.setIndex("");
					invoiceDet.setTlnno((String)lineArr.get("LNNO"));
					invoiceDet.setOrdQty((String)lineArr.get("QTY"));
					invoiceDet.setDelLnno((String)lineArr.get("LNNO"));
					invoiceDet.setDelLnQty((String)lineArr.get("QTY"));
					invoiceDet.setDelLnLoc((String)lineArr.get("LOCATIONID"));
					invoiceDet.setDelLnBatch((String)lineArr.get("BATCH"));
					invoiceDet.setDelLnUom((String)lineArr.get("UOM"));
					invoiceDet.setDelLnItem((String)lineArr.get("PRODUCT"));
					invoiceDet.setConvcost("");
					invoiceDet.setIs_cogs_set("");
					
					invoicedetlist.add(invoiceDet);
					
				}else{
					break;
				}
			}
			
			for (InvoiceHdr hdr : invoicehdrlist) {
				String invoice = hdr.getInvoiceNo();
				List<InvoiceDet> invoicedetfilter = invoicedetlist.stream().filter((b)->b.getInvoice().equalsIgnoreCase(invoice)).collect(Collectors.toList());
				if(invoicedetfilter.size() > 0) {
					InvoicePojo invoicepojo = new InvoicePojo();
					invoicepojo.setInvoicehdr(hdr);
					invoicepojo.setInvoicedet(invoicedetfilter);
					invoicepojolist.add(invoicepojo);
				}
			}
			
			for (InvoicePojo invoicepojo : invoicepojolist) {
				double subtotal = 0;
				for (InvoiceDet invoiceDetp : invoicepojo.getInvoicedet()) {
					subtotal += Double.valueOf(invoiceDetp.getAmount());
				}
				
				int itemrates = Integer.valueOf(invoicepojo.getInvoicehdr().getItemRates());
				String odisc = invoicepojo.getInvoicehdr().getOrderdiscount();
				String odisctype = invoicepojo.getInvoicehdr().getOrderdisctype();
				int odisctax =  Integer.valueOf(invoicepojo.getInvoicehdr().getOrderdiscstatus());
				String disc = invoicepojo.getInvoicehdr().getDiscount();
				String disctype = invoicepojo.getInvoicehdr().getDiscountType();
				int disctax = Integer.valueOf(invoicepojo.getInvoicehdr().getDiscstatus());
				String shipcost = invoicepojo.getInvoicehdr().getShippingCost();
				int isshiptax = Integer.valueOf(invoicepojo.getInvoicehdr().getShipstatus());
				String adjustment = invoicepojo.getInvoicehdr().getAdjustment();
				double taxper = Double.valueOf(invoicepojo.getInvoicehdr().getGst());
				
				FinCountryTaxType fintax = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(invoicepojo.getInvoicehdr().getTaxid()));
				double taxamount = 0;
				if(itemrates == 1) {
					if(fintax.getISZERO() == 0 && fintax.getSHOWTAX() == 1){
						double fsubtotal = (100*subtotal)/(100+taxper);
						taxamount += subtotal - fsubtotal;
						subtotal = fsubtotal;
						
						if(odisctax == 1) {
							if(odisctype.equalsIgnoreCase("%")) {
								taxamount += (((subtotal/100)*Double.valueOf(odisc))/100)*taxper;
							}else {
								taxamount +=  (Double.valueOf(odisc)/100)*taxper;
							}
						}
						
						if(disctax == 1) {
							if(disctype.equalsIgnoreCase("%")) {
								taxamount += (((subtotal/100)*Double.valueOf(disc))/100)*taxper;
							}else {
								taxamount +=  (Double.valueOf(disc)/100)*taxper;
							}
						}
						
						if(isshiptax == 1) {
							taxamount +=  (Double.valueOf(shipcost)/100)*taxper;
						}
					}
				}else {
					if(fintax.getISZERO() == 0 && fintax.getSHOWTAX() == 1){
						
						taxamount += (Double.valueOf(subtotal)/100)*taxper;
						
						if(odisctax == 1) {
							if(odisctype.equalsIgnoreCase("%")) {
								taxamount += (((subtotal/100)*Double.valueOf(odisc))/100)*taxper;
							}else {
								taxamount +=  (Double.valueOf(odisc)/100)*taxper;
							}
						}
						
						if(disctax == 1) {
							if(disctype.equalsIgnoreCase("%")) {
								taxamount += (((subtotal/100)*Double.valueOf(disc))/100)*taxper;
							}else {
								taxamount +=  (Double.valueOf(disc)/100)*taxper;
							}
						}
						
						if(isshiptax == 1) {
							taxamount +=  (Double.valueOf(shipcost)/100)*taxper;
						}
					}
				}
				
				double odiscount=0,discount=0;
				
				if(odisctype.equalsIgnoreCase("%")) {
					odiscount = (subtotal/100)*Double.valueOf(odisc);
				}else {
					odiscount =  Double.valueOf(odisc);
				}
				
				if(disctype.equalsIgnoreCase("%")) {
					discount = (subtotal/100)*Double.valueOf(disc);
				}else {
					discount = Double.valueOf(disc);
				}
				
				double totalamount = (subtotal + Double.valueOf(shipcost) + Double.valueOf(adjustment) + taxamount) - (odiscount + discount);
				
				invoicepojo.getInvoicehdr().setSubTotal(String.valueOf(subtotal));
				invoicepojo.getInvoicehdr().setTotalAmount(String.valueOf(totalamount));
				invoicepojo.getInvoicehdr().setTaxamount(String.valueOf(taxamount));
				
			}
			
			String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			
			flag = invoiceSave(PLANT, _login_user, curency, invoicepojolist);
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDRESULT_INVOICE");
				flag = true;
				result = "<font color=\"green\">Invoice Created Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Creating Invoice </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("OUTBOUNDRESULT_INVOICE", result);
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDRESULT_INVOICE", result);
		response.sendRedirect("../invoice/import?");

	}
	
	public boolean invoiceSave(String plant, String username, String curency, List<InvoicePojo> invoicepojolist) throws Exception {
		boolean flagout = false;
			for (InvoicePojo invoicePojo : invoicepojolist) {

				String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "",
						cmd = "", TranId = "", salesloc = "", orderdiscount = "0", itemRates = "", discount = "0",
						discountType = "", discountAccount = "", shippingCost = "", isexpense = "0", taxamount = "0",
						gino = "",  adjustment = "", subTotal = "", totalAmount = "",
						invoiceStatus = "", note = "", ORDERTYPE="",empno = "", terms = "", custName = "", custName1 = "",
						empName = "", taxtreatment = "", shipId = "", shipCust = "", incoterm = "", origin = "",
						deductInv = "", currencyid = "", currencyuseqt = "0", projectid = "", orderdiscstatus = "0",
						discstatus = "0", shipstatus = "0", taxid = "", orderdisctype = "%", gst = "0", jobNum = "";//shippingcost = "",
				int transportid;

				/* InvoiceDet */
				List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),
						notesexp = new ArrayList(), cost = new ArrayList(), detDiscount = new ArrayList(),
						detDiscounttype = new ArrayList(), taxType = new ArrayList(), 
						amount = new ArrayList(), edit_item = new ArrayList<>(), edit_qty = new ArrayList<>(),
						tlnno = new ArrayList();//DETID = new ArrayList(),
				List loc = new ArrayList(), batch = new ArrayList(), uom = new ArrayList(), 
						ordQty = new ArrayList(), delLnno = new ArrayList(), delLnQty = new ArrayList(),
						delLnLoc = new ArrayList(), delLnBatch = new ArrayList(), delLnUom = new ArrayList(),
						delLnItem = new ArrayList(), convcost = new ArrayList(), is_cogs_set = new ArrayList();//index = new ArrayList(),
				List<Hashtable<String, String>> invoiceDetInfoList = null;
//				List<Hashtable<String, String>> invoiceAttachmentList = null;
//				List<Hashtable<String, String>> invoiceAttachmentInfoList = null;
				Hashtable<String, String> invoiceDetInfo = null;
//				Hashtable<String, String> invoiceAttachment = null;

				InvoiceUtil invoiceUtil = new InvoiceUtil();
				InvoiceDAO invoiceDAO = new InvoiceDAO();
//				DateUtils dateutils = new DateUtils();
				MovHisDAO movHisDao = new MovHisDAO();
				boolean isAdded = false;
				boolean isAmntExceed = false;
				boolean Isconvcost = false;
//				String result = "";//, amntExceedMsg = "";
				int itemCount = 0, accountNameCount = 0, qtyCount = 0, costCount = 0, detDiscountCount = 0,
						detDiscounttypeCount = 0, taxTypeCount = 0, amountCount = 0, notesexpcount = 0,
						editItemCount = 0, editQtyCount = 0;//, DETIDCount = 0
				int locCount = 0, batchCount = 0, uomCount = 0,
						delLnQtyCount = 0, delLnLocCount = 0, delLnBatchCount = 0, delLnUomCount = 0,
						delLnItemCount = 0;//delLnnoCount = 0, convcostCount = 0, is_cogs_setCount = 0, idCount = 0, ordQtyCount = 0, tlnnoCount = 0
				/* others */
//				String personIncharge = "", ctype = "", fitem = "", floc = "", floc_type_id = "", floc_type_id2 = "",
//						fmodel = "", fuom = "";
				
				int trans = invoicePojo.getInvoicehdr().getTRANSPORTID();
				String transport = Integer.toString(trans);

				CustCode = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getCustCode()) ? "" :  invoicePojo.getInvoicehdr().getCustCode();
				custName = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getCustName()) ? "" :  invoicePojo.getInvoicehdr().getCustName();
				custName1 = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getCustName1()) ? "" :  invoicePojo.getInvoicehdr().getCustName1();
				invoiceNo = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getInvoiceNo()) ? "" :  invoicePojo.getInvoicehdr().getInvoiceNo();
				gino = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getGino()) ? "" :  invoicePojo.getInvoicehdr().getGino();
				dono = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getDono()) ? "" :  invoicePojo.getInvoicehdr().getDono();
				invoiceDate = invoicePojo.getInvoicehdr().getInvoiceDate();
				dueDate = invoicePojo.getInvoicehdr().getDueDate();
				payTerms = invoicePojo.getInvoicehdr().getPayTerms();
				//ORDERTYPE = invoicePojo.getInvoicehdr().getORDERTYPE();
				ORDERTYPE = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getORDERTYPE()) ? "" :  invoicePojo.getInvoicehdr().getORDERTYPE();
				empno = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getEmpno()) ? "" :  invoicePojo.getInvoicehdr().getEmpno();
				empName = "null".equalsIgnoreCase(invoicePojo.getInvoicehdr().getEmpName()) ? "" :  invoicePojo.getInvoicehdr().getEmpName();
				itemRates = invoicePojo.getInvoicehdr().getItemRates();
				discount = invoicePojo.getInvoicehdr().getDiscount();
				discountType = invoicePojo.getInvoicehdr().getDiscountType();
				orderdiscount = invoicePojo.getInvoicehdr().getOrderdiscount();
				shippingCost = invoicePojo.getInvoicehdr().getShippingCost();
				adjustment = invoicePojo.getInvoicehdr().getAdjustment();
				subTotal = invoicePojo.getInvoicehdr().getSubTotal();
				totalAmount = invoicePojo.getInvoicehdr().getTotalAmount();
				taxamount = invoicePojo.getInvoicehdr().getTaxamount();
				invoiceStatus = invoicePojo.getInvoicehdr().getInvoiceStatus();
				note = invoicePojo.getInvoicehdr().getNote();
				terms = invoicePojo.getInvoicehdr().getTerms();
				cmd = invoicePojo.getInvoicehdr().getCmd();
				TranId = invoicePojo.getInvoicehdr().getTranId();
				salesloc = invoicePojo.getInvoicehdr().getSalesloc();
				isexpense = invoicePojo.getInvoicehdr().getIsexpense();
				taxtreatment = invoicePojo.getInvoicehdr().getTaxtreatment();
				shipId = invoicePojo.getInvoicehdr().getShipId();
				shipCust = invoicePojo.getInvoicehdr().getShipCust();
				origin = invoicePojo.getInvoicehdr().getOrigin();
				deductInv = invoicePojo.getInvoicehdr().getDeductInv();
				incoterm = invoicePojo.getInvoicehdr().getIncoterm();
				currencyid = invoicePojo.getInvoicehdr().getCurrencyid();
				currencyuseqt = invoicePojo.getInvoicehdr().getCurrencyuseqt();
				orderdiscstatus = invoicePojo.getInvoicehdr().getOrderdiscstatus();
				discstatus = invoicePojo.getInvoicehdr().getDiscstatus();
				shipstatus = invoicePojo.getInvoicehdr().getShipstatus();
				taxid = invoicePojo.getInvoicehdr().getTaxid();
				orderdisctype = invoicePojo.getInvoicehdr().getOrderdisctype();
				projectid = invoicePojo.getInvoicehdr().getProjectid();
				transportid = trans;
				gst = invoicePojo.getInvoicehdr().getGst();
				jobNum = invoicePojo.getInvoicehdr().getJobNum();
//				personIncharge = invoicePojo.getInvoicehdr().getPersonIncharge();
//				ctype = invoicePojo.getInvoicehdr().getCtype();
//				fitem = invoicePojo.getInvoicehdr().getFitem();
//				floc = invoicePojo.getInvoicehdr().getFloc();
//				floc_type_id = invoicePojo.getInvoicehdr().getFloc_type_id();
//				floc_type_id2 = invoicePojo.getInvoicehdr().getFloc_type_id2();
//				fmodel = invoicePojo.getInvoicehdr().getFmodel();
//				fuom = invoicePojo.getInvoicehdr().getFuom();

				for (InvoiceDet det : invoicePojo.getInvoicedet()) {
					item.add(itemCount, det.getItem());
					itemCount++;

					edit_item.add(editItemCount, det.getEdit_item());
					editItemCount++;

					accountName.add(accountNameCount, det.getAccountName());
					accountNameCount++;

					qty.add(qtyCount, det.getQty());
					qtyCount++;

					edit_qty.add(editQtyCount, det.getEdit_qty());
					editQtyCount++;

					cost.add(costCount, det.getCost());
					costCount++;

					detDiscount.add(detDiscountCount, det.getDetDiscount());
					detDiscountCount++;

					detDiscounttype.add(detDiscounttypeCount, det.getDetDiscounttype());
					detDiscounttypeCount++;

					taxType.add(taxTypeCount, det.getTaxType());
					taxTypeCount++;

					amount.add(amountCount, det.getAmount());
					amountCount++;

					notesexp.add(notesexpcount, det.getNotesexp());
					notesexpcount++;

					loc.add(locCount, det.getLoc());
					locCount++;

					uom.add(uomCount, det.getUom());
					uomCount++;

					batch.add(batchCount, det.getBatch());
					batchCount++;

					/*tlnno.add(qtyCount, det.getTlnno());
					tlnnoCount++;*/

					/*ordQty.add(ordQtyCount, det.getOrdQty());
					ordQtyCount++;*/

					/*delLnno.add(delLnnoCount, det.getDelLnno());
					delLnnoCount++;*/

					delLnQty.add(delLnQtyCount, det.getDelLnQty());
					delLnQtyCount++;

					delLnLoc.add(delLnLocCount, det.getDelLnLoc());
					delLnLocCount++;

					delLnBatch.add(delLnBatchCount, det.getDelLnBatch());
					delLnBatchCount++;

					delLnUom.add(delLnUomCount, det.getDelLnUom());
					delLnUomCount++;

					delLnItem.add(delLnItemCount, det.getDelLnItem());
					delLnItemCount++;

					/*
					 * convcost.add(costCount, det.getConvcost()); convcostCount++;
					 * 
					 * is_cogs_set.add(delLnItemCount, det.getIs_cogs_set()); is_cogs_setCount++;
					 */

				}
				Isconvcost = false;

				// Get Employee Code by Name
				if(empName != null && !empName.isEmpty()) {
					if (empno != null && !empno.isEmpty()) {
						ArrayList arrList = new ArrayList();
						EmployeeDAO movHisDAO = new EmployeeDAO();
						Hashtable htData = new Hashtable();
						htData.put("PLANT", plant);
						htData.put("FNAME", empName);
						arrList = movHisDAO.getEmployeeDetails("EMPNO", htData, "");
						if (!arrList.isEmpty()) {
							Map m = (Map) arrList.get(0);
							empno = (String) m.get("EMPNO");
						}
					}
				}

				if (custName.isEmpty())
					if (!custName1.isEmpty())
						custName = custName1;

				if (!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf(
							(Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));

				if (!orderdisctype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Double.parseDouble(StrUtils.fString(orderdiscount))
							/ (Double.parseDouble(currencyuseqt))));

				//////////////////////
				invoiceDetInfoList = new ArrayList<Hashtable<String, String>>();
				Hashtable invoiceHdr = new Hashtable();
				invoiceHdr.put("PLANT", plant);
				invoiceHdr.put("CUSTNO", CustCode);
				invoiceHdr.put("INVOICE", invoiceNo);
				invoiceHdr.put("GINO", gino);
				invoiceHdr.put("DONO", dono);
				invoiceHdr.put("INVOICE_DATE", invoiceDate);
				invoiceHdr.put("DUE_DATE", dueDate);
				invoiceHdr.put("PAYMENT_TERMS", payTerms);
				invoiceHdr.put("ORDERTYPE", ORDERTYPE);
				invoiceHdr.put("EMPNO", empno);
				invoiceHdr.put("ITEM_RATES", itemRates);
				invoiceHdr.put("DISCOUNT", discount);
				invoiceHdr.put("ORDER_DISCOUNT", orderdiscount);
				invoiceHdr.put("DISCOUNT_TYPE", discountType);
				invoiceHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				invoiceHdr.put("SHIPPINGCOST", shippingCost);
				invoiceHdr.put("ADJUSTMENT", adjustment);
				invoiceHdr.put("SUB_TOTAL", subTotal);
				invoiceHdr.put("TOTAL_AMOUNT", totalAmount);
				invoiceHdr.put("BILL_STATUS", invoiceStatus);
				invoiceHdr.put("NOTE", note);
				invoiceHdr.put("TERMSCONDITIONS", terms);
				invoiceHdr.put("CRAT", DateUtils.getDateTime());
				invoiceHdr.put("CRBY", username);
				invoiceHdr.put("UPAT", DateUtils.getDateTime());
				invoiceHdr.put("SALES_LOCATION", salesloc);
				invoiceHdr.put("ISEXPENSE", isexpense);
				invoiceHdr.put("TAXTREATMENT", taxtreatment);
				invoiceHdr.put("TAXAMOUNT", taxamount);
				invoiceHdr.put("SHIPPINGID", shipId);
				invoiceHdr.put("SHIPPINGCUSTOMER", shipCust);
				invoiceHdr.put("INCOTERMS", incoterm);
				invoiceHdr.put("ORIGIN", origin);
				invoiceHdr.put("DEDUCT_INV", deductInv);
				invoiceHdr.put("CURRENCYUSEQT", currencyuseqt);
				invoiceHdr.put("ORDERDISCOUNTTYPE", orderdisctype);
				invoiceHdr.put("TAXID", taxid);
				invoiceHdr.put("ISDISCOUNTTAX", discstatus);
				invoiceHdr.put("ISORDERDISCOUNTTAX", orderdiscstatus);
				invoiceHdr.put("ISSHIPPINGTAX", shipstatus);
				invoiceHdr.put("PROJECTID", projectid);
				invoiceHdr.put("TRANSPORTID", transport);
				invoiceHdr.put("OUTBOUD_GST", gst);
				invoiceHdr.put(IDBConstants.CURRENCYID, currencyid);
				invoiceHdr.put("JobNum", jobNum);
				CustUtil custUtil = new CustUtil();
				ArrayList supplier_detail = custUtil.getCustomerDetails(shipCust, plant);
				invoiceHdr.put("SHIPCONTACTNAME",(String)supplier_detail.get(44));
				invoiceHdr.put("SHIPDESGINATION",(String)supplier_detail.get(45));
				invoiceHdr.put("SHIPWORKPHONE",(String)supplier_detail.get(46));
				invoiceHdr.put("SHIPHPNO",(String)supplier_detail.get(47));
				invoiceHdr.put("SHIPEMAIL",(String)supplier_detail.get(48));
				invoiceHdr.put("SHIPCOUNTRY",(String)supplier_detail.get(49));
				invoiceHdr.put("SHIPADDR1",(String)supplier_detail.get(50));
				invoiceHdr.put("SHIPADDR2",(String)supplier_detail.get(51));
				invoiceHdr.put("SHIPADDR3",(String)supplier_detail.get(52));
				invoiceHdr.put("SHIPADDR4",(String)supplier_detail.get(53));
				invoiceHdr.put("SHIPSTATE",(String)supplier_detail.get(54));
				invoiceHdr.put("SHIPZIP",(String)supplier_detail.get(55));
				
				int invoiceHdrId = 0;
				BillDAO itemCogsDao = new BillDAO();
				Costofgoods costofGoods = new CostofgoodsImpl();
				if (cmd.equalsIgnoreCase("Edit")) {
					if (!TranId.isEmpty()) {
						Hashtable htInvHdr = new Hashtable();
						htInvHdr.put("ID", TranId);
						htInvHdr.put("PLANT", plant);
						List arrayInvHdr = invoiceUtil.getInvoiceHdrById(htInvHdr);
						Map mapInvHdr = (Map) arrayInvHdr.get(0);
						double total_amount = Double.parseDouble(mapInvHdr.get("TOTAL_AMOUNT").toString());
						double balanceAmount = Double.parseDouble(totalAmount) - total_amount;
						if (balanceAmount > 0) {

							Map resultMap = getOutstandingAmountForCustomer(CustCode, balanceAmount, plant);
							isAmntExceed = (boolean) resultMap.get("STATUS");
//							amntExceedMsg = (String) resultMap.get("MSG");
						} else {
							isAmntExceed = false;
						}
						if (!isAmntExceed) {
							invoiceHdr.put("ID", TranId);
							invoiceHdr.put("UPBY", username);
							invoiceHdrId = invoiceUtil.updateInvoiceHdr(invoiceHdr);
						}
					}
				} else {
					Map resultMap = getOutstandingAmountForCustomer(CustCode, Double.parseDouble(totalAmount), plant);
					isAmntExceed = (boolean) resultMap.get("STATUS");
//					amntExceedMsg = (String) resultMap.get("MSG");
					if (!isAmntExceed) {
						invoiceHdr.put("CREDITNOTESSTATUS", "0");
						invoiceHdr.put("TOTAL_PAYING","0");
						invoiceHdrId = invoiceUtil.addInvoiceHdr(invoiceHdr, plant);
					}
				}

				if (invoiceHdrId > 0) {
					for (int i = 0; i < item.size(); i++) {
						int lnno = i + 1;
						String convDiscount = "";
						String convCost = String.valueOf(
								(Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
						if (Isconvcost)
							convCost = String.valueOf(
									(Double.parseDouble((String) convcost.get(i)) / Double.parseDouble(currencyuseqt)));
						if (!detDiscounttype.get(i).toString().contains("%")) {
							convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i))
									/ Double.parseDouble(currencyuseqt)));
						} else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf(
								(Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));

						invoiceDetInfo = new Hashtable<String, String>();
						invoiceDetInfo.put("PLANT", plant);
						invoiceDetInfo.put("LNNO", Integer.toString(lnno));
						if (cmd.equalsIgnoreCase("Edit"))
							invoiceDetInfo.put("INVOICEHDRID", TranId);
						else
							invoiceDetInfo.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
						// invoiceDetInfo.put("INVOICEHDRID", TranId);
						invoiceDetInfo.put("ITEM", (String) item.get(i));
						invoiceDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						invoiceDetInfo.put("QTY", (String) qty.get(i));
						invoiceDetInfo.put("UNITPRICE", convCost);
						invoiceDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						invoiceDetInfo.put("AMOUNT", convAmount);
						invoiceDetInfo.put("CRAT", DateUtils.getDateTime());
						invoiceDetInfo.put("CRBY", username);
						invoiceDetInfo.put("UPAT", DateUtils.getDateTime());
						invoiceDetInfo.put("DISCOUNT", convDiscount);
						invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						invoiceDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						if (cmd.equalsIgnoreCase("Edit")) {
							if (((String) is_cogs_set.get(i)).equalsIgnoreCase("Y")) {
								if (((String) qty.get(i)).equalsIgnoreCase((String) edit_qty.get(i))
										&& ((String) item.get(i)).equalsIgnoreCase((String) edit_item.get(i))) {
									invoiceDetInfo.put("IS_COGS_SET", (String) is_cogs_set.get(i));
								} else {
									invoiceDetInfo.put("IS_COGS_SET", "N");
								}
							}
						}
						if (loc.size() > 0) {
							invoiceDetInfo.put("LOC", (String) loc.get(i));
							invoiceDetInfo.put("UOM", (String) uom.get(i));
							invoiceDetInfo.put("BATCH", (String) batch.get(i));
						}
						if (notesexp.size() > 0) {
							invoiceDetInfo.put("NOTE", (String) notesexp.get(i));
						} else {
							/*
							 * invoiceDetInfo.put("DISCOUNT", (String) detDiscount.get(i));
							 * invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
							 */
						}
						invoiceDetInfoList.add(invoiceDetInfo);

						if (cmd.equalsIgnoreCase("Edit")) {
							int cogsCnt = itemCogsDao.addItemCogs(
									costofGoods.revisedSoldProductDetails((String) qty.get(i), (String) edit_qty.get(i),
											(String) item.get(i), (String) edit_item.get(i), plant, dueDate),
									plant);
							System.out.println("Insert ItemCogs Status :" + cogsCnt);
						} else {
							int cogsCnt = itemCogsDao.addItemCogs(costofGoods.soldProductDetails((String) qty.get(i),
									(String) item.get(i), plant, dueDate), plant);
							System.out.println("Insert ItemCogs Status :" + cogsCnt);
						}

					}
					if (cmd.equalsIgnoreCase("Edit")) {
						if (!TranId.isEmpty()) {
							invoiceDetInfo.put("UPBY", username);
							isAdded = invoiceDAO.deleteInvoiceDet(plant, TranId);
							if (isAdded)
								isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);
						}
					} else
						isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);

					if (isAdded) {
						System.out.println("invoice Status" + invoiceStatus);
						if (!invoiceStatus.equalsIgnoreCase("Draft")) {
							// Journal Entry
							JournalHeader journalHead = new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(invoiceDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("INVOICE");
							journalHead.setTRANSACTION_ID(invoiceNo);
							journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
							// journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							List<JournalDetail> journalDetails = new ArrayList<>();
							CoaDAO coaDAO = new CoaDAO();
							CustMstDAO cusDAO = new CustMstDAO();
							ItemMstDAO itemMstDAO = new ItemMstDAO();
							Double totalItemNetWeight = 0.00;
							Double totalCostofGoodSold = 0.00;

							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) CustCode);
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject CusJson = cusDAO.getCustomerName(plant, (String) CustCode);
								if (!CusJson.isEmpty()) {
									coaJson1 = coaDAO.getCOAByName(plant, CusJson.getString("CNAME"));
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
										coaJson1 = coaDAO.getCOAByName(plant,
												CusJson.getString("CUSTNO") + "-" + CusJson.getString("CNAME"));
									}
								}
							}
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

							} else {
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								// journalDetail_1.setACCOUNT_NAME((String) CustCode);
								if (coaJson1.getString("account_name") != null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_1);
							}

							if (discount.isEmpty()) {
								discount = "0.00";
							}
							Double discountFrom = Double.parseDouble(discount);
							Double orderDiscountFrom = 0.00;
							if (!orderdiscount.isEmpty()) {
								orderDiscountFrom = Double.parseDouble(orderdiscount);

								if (orderdisctype.toString().equalsIgnoreCase("%"))
									orderdiscount = Double
											.toString((Double.parseDouble(subTotal) * orderDiscountFrom) / 100);
							}
							if (discountFrom > 0 || orderDiscountFrom > 0) {
								if (!discountType.isEmpty()) {
									if (discountType.equalsIgnoreCase("%")) {
										Double subTotalAfterOrderDiscount = Double.parseDouble(subTotal)
												- orderDiscountFrom;
										discountFrom = (subTotalAfterOrderDiscount * discountFrom) / 100;
									}
								}
								// discountFrom=discountFrom+orderDiscountFrom;
								JournalDetail journalDetail_3 = new JournalDetail();
								journalDetail_3.setPLANT(plant);
								JSONObject coaJson3 = coaDAO.getCOAByName(plant, "Discounts given - COS");
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
								journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
								journalDetail_3.setDEBITS(discountFrom);
								journalDetails.add(journalDetail_3);
							}

							for (Map invDet : invoiceDetInfoList) {
								Double quantity = Double.parseDouble(invDet.get("QTY").toString());
								String netWeight = itemMstDAO.getItemNetWeight(plant, invDet.get("ITEM").toString());
								if (netWeight != null && !"".equals(netWeight)) {
									Double Netweight = quantity * Double.parseDouble(netWeight);
									totalItemNetWeight += Netweight;
								}

								System.out.println("TotalNetWeight:" + totalItemNetWeight);

								JournalDetail journalDetail = new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson = coaDAO.getCOAByName(plant, (String) invDet.get("ACCOUNT_NAME"));
								System.out.println("Json" + coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) invDet.get("ACCOUNT_NAME"));
								journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())
										- Double.parseDouble(orderdiscount.toString()));
								/*
								 * String avg=new InvUtil().getCostOfGoods((String)invDet.get("ITEM"),plant);
								 * if(avg!=null && !"".equals(avg)) {
								 * totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }else { avg=new
								 * InvUtil().getAvgCostofItem((String)invDet.get("ITEM"),plant); if(avg!=null &&
								 * !"".equals(avg)) { totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }
								 * 
								 * }
								 */
								/**/
								int invoicesCount = new InvoiceDAO().invoiceWoCOGSCount((String) invDet.get("ITEM"),
										plant);
								if (invoicesCount == 1) {
									Map invDetail = new InvMstDAO().getInvDataByProduct((String) invDet.get("ITEM"),
											plant);
									double bill_qty = 0, invoiced_qty = 0;//inv_qty = 0, unbill_qty = 0, 
//									inv_qty = Double.parseDouble((String) invDetail.get("INV_QTY"));
									bill_qty = Double.parseDouble((String) invDetail.get("BILL_QTY"));
//									unbill_qty = Double.parseDouble((String) invDetail.get("UNBILL_QTY"));
									invoiced_qty = Double.parseDouble((String) invDetail.get("INVOICE_QTY"));

									bill_qty = bill_qty - invoiced_qty;

									if (bill_qty >= quantity) {
										/**/
										ArrayList invQryList;
										Hashtable ht_cog = new Hashtable();
										ht_cog.put("a.PLANT", plant);
										ht_cog.put("a.ITEM", (String) invDet.get("ITEM"));
										invQryList = new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(
												ht_cog, "", "", plant, (String) invDet.get("ITEM"), "", curency,
												curency, "", "", "", "","");
										if (invQryList.isEmpty()) {
											invQryList = new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht_cog,
													"", "", plant, (String) invDet.get("ITEM"), "", curency, curency,
													"", "", "", "","");
										}
										if (invQryList != null) {
											if (!invQryList.isEmpty()) {
												Map lineArr = (Map) invQryList.get(0);
												String avg = StrUtils.addZeroes(
														Double.parseDouble((String) lineArr.get("AVERAGE_COST")), "2");
												totalCostofGoodSold += Double.parseDouble(avg) * (quantity);
											}
										}
										new InvoiceDAO().update_is_cogs_set((String) invDet.get("INVOICEHDRID"),
												(String) invDet.get("LNNO"), (String) invDet.get("ITEM"), plant);
									}
								}
								// totalCostofGoodSold+=Double.parseDouble(invDet.get("AMOUNT").toString());
								boolean isLoop = false;
								if (journalDetails.size() > 0) {
									int i = 0;
									for (JournalDetail journal : journalDetails) {
										int accountId = journal.getACCOUNT_ID();
										if (accountId == journalDetail.getACCOUNT_ID()) {
											isLoop = true;
											Double sumDetit = journal.getCREDITS() + journalDetail.getCREDITS();
											journalDetail.setCREDITS(sumDetit);
											journalDetails.set(i, journalDetail);
											break;
										}
										i++;

									}
									if (isLoop == false) {
										journalDetails.add(journalDetail);
									}

								} else {
									journalDetails.add(journalDetail);
								}
							}

							if (!shippingCost.isEmpty()) {
								Double shippingCostFrom = Double.parseDouble(shippingCost);
								if (shippingCostFrom > 0) {
									JournalDetail journalDetail_4 = new JournalDetail();
									journalDetail_4.setPLANT(plant);
									JSONObject coaJson4 = coaDAO.getCOAByName(plant, "Outward freight & shipping");
									journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
									journalDetail_4.setACCOUNT_NAME("Outward freight & shipping");
									journalDetail_4.setCREDITS(shippingCostFrom);
									journalDetails.add(journalDetail_4);
								}
							}

							if (taxamount.isEmpty()) {
								taxamount = "0.00";
							}
							Double taxAmountFrom = Double.parseDouble(taxamount);
							if (taxAmountFrom > 0) {
								JournalDetail journalDetail_2 = new JournalDetail();
								journalDetail_2.setPLANT(plant);
								/*JSONObject coaJson2 = coaDAO.getCOAByName(plant, "VAT Output");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Output");*/
								
								MasterDAO masterDAO = new MasterDAO();
								String planttaxtype = masterDAO.GetTaxType(plant);
								
								if(planttaxtype.equalsIgnoreCase("TAX")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}else if(planttaxtype.equalsIgnoreCase("GST")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Payable");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("GST Payable");
								}else if(planttaxtype.equalsIgnoreCase("VAT")) {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}else {
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("VAT Output");
								}
								
								journalDetail_2.setCREDITS(taxAmountFrom);
								journalDetails.add(journalDetail_2);
							}

							if (!adjustment.isEmpty()) {
								Double adjustFrom = Double.parseDouble(adjustment);
								if (adjustFrom > 0) {
									JournalDetail journalDetail_7 = new JournalDetail();
									journalDetail_7.setPLANT(plant);
									JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
									journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
									journalDetail_7.setACCOUNT_NAME("Adjustment");
									journalDetail_7.setCREDITS(adjustFrom);
									journalDetails.add(journalDetail_7);
								} else if (adjustFrom < 0) {
									JournalDetail journalDetail_7 = new JournalDetail();
									journalDetail_7.setPLANT(plant);
									JSONObject coaJson6 = coaDAO.getCOAByName(plant, "Adjustment");
									journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
									journalDetail_7.setACCOUNT_NAME("Adjustment");
									adjustFrom = Math.abs(adjustFrom);
									journalDetail_7.setDEBITS(adjustFrom);
									journalDetails.add(journalDetail_7);
								}
							}
							Journal journal = new Journal();
							Double totalDebitAmount = 0.00;
							for (JournalDetail jourDet : journalDetails) {
								totalDebitAmount = totalDebitAmount + jourDet.getDEBITS();
							}
							journalHead.setTOTAL_AMOUNT(totalDebitAmount);
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);
							JournalService journalService = new JournalEntry();
							Journal journalFrom = journalService.getJournalByTransactionId(plant,
									journalHead.getTRANSACTION_ID(), journalHead.getTRANSACTION_TYPE());
							if (journalFrom.getJournalHeader() != null) {
								if (journalFrom.getJournalHeader().getID() > 0) {
									journalHead.setID(journalFrom.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								} else {
									journalService.addJournal(journal, username);
									Hashtable htMovHis = new Hashtable();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									htMovHis.put(IDBConstants.ITEM, "");
									htMovHis.put(IDBConstants.QTY, "0.0");
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									htMovHis.put(IDBConstants.CREATED_BY, username);		
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(htMovHis);
								}

								// Cost of goods sold
								if (totalCostofGoodSold > 0) {
									journalDetails.clear();
									journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
									JournalDetail journalDetail_InvAsset = new JournalDetail();
									journalDetail_InvAsset.setPLANT(plant);
									JSONObject coaJson7 = coaDAO.getCOAByName(plant, "Inventory Asset");
									System.out.println("Json" + coaJson7.toString());
									journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
									journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
									journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
									journalDetails.add(journalDetail_InvAsset);

									JournalDetail journalDetail_COG = new JournalDetail();
									journalDetail_COG.setPLANT(plant);
									JSONObject coaJson8 = coaDAO.getCOAByName(plant, "Cost of goods sold");
									System.out.println("Json" + coaJson8.toString());
									journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
									journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
									journalDetail_COG.setDEBITS(totalCostofGoodSold);
									journalDetails.add(journalDetail_COG);

									journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);

									Journal journalCOG = journalService.getJournalByTransactionId(plant,
											journalHead.getTRANSACTION_ID(), journalHead.getTRANSACTION_TYPE());
									if (journalCOG.getJournalHeader() != null) {
										if (journalCOG.getJournalHeader().getID() > 0) {
											journalHead.setID(journalCOG.getJournalHeader().getID());
											journalService.updateJournal(journal, username);
											Hashtable jhtMovHis = new Hashtable();
											jhtMovHis.put(IDBConstants.PLANT, plant);
											jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
											jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
											jhtMovHis.put(IDBConstants.ITEM, "");
											jhtMovHis.put(IDBConstants.QTY, "0.0");
											jhtMovHis.put("RECID", "");
											jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
											jhtMovHis.put(IDBConstants.CREATED_BY, username);		
											jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											jhtMovHis.put("REMARKS","");
											movHisDao.insertIntoMovHis(jhtMovHis);
										} else {
											journalService.addJournal(journal, username);
											Hashtable htMovHis = new Hashtable();
											htMovHis.put(IDBConstants.PLANT, plant);
											htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
											htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
											htMovHis.put(IDBConstants.ITEM, "");
											htMovHis.put(IDBConstants.QTY, "0.0");
											htMovHis.put("RECID", "");
											htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
											htMovHis.put(IDBConstants.CREATED_BY, username);		
											htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htMovHis.put("REMARKS","");
											movHisDao.insertIntoMovHis(htMovHis);
										}
									}
								}
							}

						}
					}

					if (isexpense.equals("1")) {
						if (cmd.equalsIgnoreCase("Edit")) {

						} else {
							ExpensesUtil expUtil = new ExpensesUtil();
							Hashtable expenseHdr = new Hashtable();
							expenseHdr.put("PLANT", plant);
							expenseHdr.put("STATUS", "BILLED");
							expenseHdr.put("ID", TranId);
							expenseHdr.put("UPBY", username);
							expUtil.updateExpensesHdrstatus(expenseHdr);
						}

					}
					if (isAdded) {
						if (!deductInv.equalsIgnoreCase("1") && !isexpense.equals("1") && !gino.equalsIgnoreCase("")) {
							Hashtable htgi = new Hashtable();
							String sqlgi = "UPDATE " + plant + "_FINGINOTOINVOICE SET STATUS='INVOICED' WHERE PLANT='"
									+ plant + "' AND GINO='" + gino + "'";
							invoiceDAO.updategino(sqlgi, htgi, "");
						}
					}

					if (isAdded) {
						for (int i = 0; i < item.size(); i++) {
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							if (cmd.equalsIgnoreCase("Edit"))
								htMovHis.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);
							else {
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_INVOICE);
								if (isexpense.equalsIgnoreCase("1"))
									htMovHis.put("DIRTYPE", TransactionConstants.EXPENSES_TO_INVOICE);
								if (cmd.equalsIgnoreCase("IssueingGoodsInvoice"))
									htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_INVOICE);
							}

							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(invoiceDate));
							htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
							String billqty = String.valueOf((String) qty.get(i));
							htMovHis.put(IDBConstants.QTY, billqty);
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							if (!jobNum.isEmpty())
								htMovHis.put("REMARKS", jobNum);
							else
								htMovHis.put("REMARKS", dono);

							Hashtable htMovChk = new Hashtable();
							htMovChk.clear();
							htMovChk.put(IDBConstants.PLANT, plant);
							htMovChk.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);
							htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
							htMovChk.put(IDBConstants.QTY, billqty);
							htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
							isAdded = movHisDao.isExisit(htMovChk, " DIRTYPE LIKE '%INVOICE%' ");
							if (!isAdded)
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
				}

				/* Added by Abhilash to deduct from inventory */

				if (delLnno.size() > 0 && deductInv.equalsIgnoreCase("1")) {
					for (int i = 0; i < delLnno.size(); i++) {
						int lnno = Integer.parseInt((String) delLnno.get(i));
						String ITEM = "", UOM = "", QTY = "", LOC = "", BATCH = "";
						ITEM = (String) delLnItem.get(i);
						UOM = (String) delLnUom.get(i);
						QTY = (String) delLnQty.get(i);
						LOC = (String) delLnLoc.get(i);
						BATCH = (String) delLnBatch.get(i);

						Map invmap = null;
						String uomQty = "";
						List uomQry = new UomDAO().getUomDetails(UOM, plant, "");
						if (uomQry.size() > 0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String) m.get("QPUOM");
						} else {
							uomQty = "0";
						}

						String strTranDate = "";
						invmap = new HashMap();
						invmap.put(IConstants.PLANT, plant);
						invmap.put(IConstants.ITEM, ITEM);
						invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, ITEM));
						invmap.put("INVOICE", invoiceNo);
						invmap.put(IConstants.DODET_DONUM, "");
						invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
						invmap.put(IConstants.CUSTOMER_NAME, CustCode);
						invmap.put(IConstants.LOC, LOC);
						invmap.put(IConstants.LOC2, "SHIPPINGAREA" + "_" + LOC);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, CustCode);
						invmap.put(IConstants.BATCH, BATCH);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put("ISSUEDQTY", QTY);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						if (invoiceDate.length() > 5)
							strTranDate = invoiceDate.substring(6) + "-" + invoiceDate.substring(3, 5) + "-"
									+ invoiceDate.substring(0, 2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put("RETURN_DATE", strTranDate);
						invmap.put("UOMQTY", uomQty);
						invmap.put("NOTES", "");
						invmap.put("SORETURN", "");
						invmap.put("GINO", gino);
						invmap.put(IConstants.CUSTOMER_NAME, CustCode);
						isAdded = processShipHisReversal(invmap);
						if (isAdded) {
							processInvAdd(invmap);
						}
						if (isAdded) {
							processMovHis_IN(invmap);
						}
						if (isAdded == true) {//Shopify Inventory Update
		   					Hashtable htCond = new Hashtable();
		   					htCond.put(IConstants.PLANT, plant);
		   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
		   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,ITEM);						
								if(nonstkflag.equalsIgnoreCase("N")) {
		   						String availqty ="0";
		   						ArrayList invQryList = null;
		   						htCond = new Hashtable();
		   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,ITEM, new ItemMstDAO().getItemDesc(plant, ITEM),htCond);						
		   						if(new PlantMstDAO().getisshopify(plant)) {
		   						if (invQryList.size() > 0) {					
		   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		   								//String result="";
		                                Map lineArr = (Map) invQryList.get(iCnt);
		                                availqty = (String)lineArr.get("AVAILABLEQTY");
		                                System.out.println(availqty);
		   							}
		   							double availableqty = Double.parseDouble(availqty);
		       						new ShopifyService().UpdateShopifyInventoryItem(plant, ITEM, availableqty);
		   						}
								}
								}
		   					}
		   				}
					}
				}
				process: if (isAdded && invoiceStatus.equalsIgnoreCase("Open") && deductInv.equalsIgnoreCase("1")) {
					for (int i = 0; i < item.size(); i++) {
						int lnno = i + 1;
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
						htCondiShipHis.put("INVOICENO", gino);
						htCondiShipHis.put("STATUS", "C");
						htCondiShipHis.put("BATCH", (String) batch.get(i));
						htCondiShipHis.put("dolno", Integer.toString(lnno));
						htCondiShipHis.put("LOC1", (String) loc.get(i));
						htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
						boolean isexists = new ShipHisDAO().isExisit(htCondiShipHis, "");

						double costdouble = Double.valueOf((String) cost.get(i)) / Double.valueOf(currencyuseqt);
						String scost = String.valueOf(costdouble);

						if (isexists) {
							String query = "set UNITPRICE=" + scost + ",ORDQTY=" + (String) qty.get(i);
							isAdded = new ShipHisDAO().updateShipHis(query, htCondiShipHis, "");
						}

						/**/
						String QTY = "", ORDQTY = "";
						boolean processInv = false;
						if (ordQty.size() == 0) {
							QTY = (String) qty.get(i);
							ORDQTY = (String) qty.get(i);
							processInv = true;
						} else {
							if (ordQty.size() > i) {
								ORDQTY = (String) qty.get(i);
								double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
								double i_Qty = Double.parseDouble((String) qty.get(i));
								i_Qty = i_Qty - i_OrdQty;
								QTY = Double.toString(i_Qty);
								if (i_Qty > 0) {
									processInv = true;
								}
							} else {
								QTY = (String) qty.get(i);
								ORDQTY = (String) qty.get(i);
								processInv = true;
							}
						}
						/**/

						if (processInv) {
							String ITEM_QTY = "";
							Map invmap = new HashMap();
							String ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(plant, (String) item.get(i));
							if (!ISNONSTKFLG.equalsIgnoreCase("Y")) {
								List itemQry = new InvMstDAO().getOutBoundPickingBatchByWMS(plant, (String) item.get(i),
										(String) loc.get(i), (String) batch.get(i));
								double invqty = 0;
								if (itemQry.size() > 0) {
									for (int j = 0; j < itemQry.size(); j++) {
										Map m = (Map) itemQry.get(j);
										ITEM_QTY = (String) m.get("qty");
										invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
									}
									/* double calinqty = pickingQty * Double.valueOf(UOMQTY); */
									double pickingQty = Double.parseDouble(((String) qty.get(i)));
									if (invqty < pickingQty) {
										throw new Exception(
												"Not enough inventory found for ProductID/Batch for Order Line No "
														+ lnno + " in the location selected");
									}
								} else {
									throw new Exception(
											"Not enough inventory found for ProductID/Batch for Order Line No " + lnno
													+ " in the location selected");

								}
							}
							String uomQty = "";
							List uomQry = new UomDAO().getUomDetails((String) uom.get(i), plant, "");
							if (uomQry.size() > 0) {
								Map m = (Map) uomQry.get(0);
								uomQty = (String) m.get("QPUOM");
							} else {
								uomQty = "0";
							}

							String strTranDate = "";
							invmap.put(IDBConstants.PLANT, plant);
							invmap.put(IConstants.DODET_DONUM, "");
							invmap.put(IConstants.DODET_DOLNNO, Integer.toString(lnno));
							invmap.put(IConstants.CUSTOMER_NAME, CustCode);
							invmap.put(IDBConstants.ITEM, (String) item.get(i));
							invmap.put(IDBConstants.ITEM_DESC, (String) item.get(i));
							invmap.put(IDBConstants.LOC, (String) loc.get(i));
							invmap.put(IDBConstants.USERFLD4, (String) batch.get(i));
							invmap.put(IConstants.BATCH, (String) batch.get(i));

							invmap.put(IConstants.ORD_QTY, ORDQTY);
							invmap.put(IConstants.QTY, QTY);
							invmap.put(IConstants.LOGIN_USER, username);
							invmap.put(IDBConstants.CURRENCYID, curency);
							invmap.put("UNITPRICE", scost);
							invmap.put(IConstants.ISSUEDATE, invoiceDate);
							invmap.put(IConstants.INVOICENO, invoiceNo);
							invmap.put("GINO", gino);
							invmap.put("UOMQTY", uomQty);
							if (invoiceDate.length() > 5)
								strTranDate = invoiceDate.substring(6) + "-" + invoiceDate.substring(3, 5) + "-"
										+ invoiceDate.substring(0, 2);
							invmap.put(IConstants.TRAN_DATE, strTranDate);
							isAdded = processShipHis(invmap);
							if (!ISNONSTKFLG.equalsIgnoreCase("Y")) {
								if (isAdded) {
									isAdded = processInvRemove(invmap);
								}
								if (isAdded) {
									processBOM(invmap);
								}
							}
							if (isAdded) {
								processMovHis_OUTforInvoice(invmap);
							}
							if (isAdded == true) {//Shopify Inventory Update
			   					Hashtable htCond = new Hashtable();
			   					htCond.put(IConstants.PLANT, plant);
			   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
			   						String nonstkflag = new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));						
									if(nonstkflag.equalsIgnoreCase("N")) {
			   						String availqty ="0";
			   						ArrayList invQryList = null;
			   						htCond = new Hashtable();
			   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(plant,(String) item.get(i), new ItemMstDAO().getItemDesc(plant, (String) item.get(i)),htCond);						
			   						if(new PlantMstDAO().getisshopify(plant)) {
			   						if (invQryList.size() > 0) {					
			   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
			   								//String result="";
			                                Map lineArr = (Map) invQryList.get(iCnt);
			                                availqty = (String)lineArr.get("AVAILABLEQTY");
			                                System.out.println(availqty);
			   							}
			   							double availableqty = Double.parseDouble(availqty);
			       						new ShopifyService().UpdateShopifyInventoryItem(plant, (String) item.get(i), availableqty);
			   						}
									}
									}
			   					}
			   				}
							if (!isAdded) {
								break process;
							}
							
							if(isAdded) {
								//insert MovHis
				                //MovHisDAO movHisDao = new MovHisDAO();
				        		movHisDao.setmLogger(mLogger);
				    			Hashtable htRecvHis = new Hashtable();
				    			htRecvHis.clear();
				    			htRecvHis.put(IDBConstants.PLANT, plant);
				    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
				    			htRecvHis.put(IDBConstants.ITEM, (String) item.get(i));
				    			htRecvHis.put("MOVTID", "");
				    			htRecvHis.put("RECID", "");
				    			htRecvHis.put(IConstants.QTY, String.valueOf(qty.get(i)));
				    			htRecvHis.put(IDBConstants.CREATED_BY, username);
				    			htRecvHis.put(IDBConstants.REMARKS, gino);        					
				    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
				    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				    			isAdded = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
				}

				// azees consignmentToInvoice Status & Qty Update
				if (!cmd.equalsIgnoreCase("Edit")) {
					if (jobNum.contains("C")) {
						for (int i = 0; i < item.size(); i++) {

//							int lnv = i + 1;
							ToDetDAO _ToDetDAO = new ToDetDAO();
							ToHdrDAO _ToHdrDAO = new ToHdrDAO();
							String updateEstHdr = "", updateEstDet = "";
							Hashtable htCondition = new Hashtable();
							htCondition.put("PLANT", plant);
							htCondition.put("TONO", jobNum);
							htCondition.put("TOLNNO", String.valueOf(tlnno.get(i)));
							int tllno = Integer.parseInt(((String) tlnno.get(i)));
							String issuingQty = (String) qty.get(i);
							ToDet toDet = new ToDet();
							toDet = _ToDetDAO.getToDetById(plant, jobNum, tllno, (String) item.get(i));

							// String issuedqty = (String)qtyIs.get(index);
							BigDecimal Ordqty = toDet.getQTYOR();
							BigDecimal tranQty = BigDecimal.valueOf(Double.parseDouble(issuingQty));
							BigDecimal issqty = BigDecimal.valueOf(Double.parseDouble("0"));
							if (toDet.getQTYAC() != null) {
								issqty = toDet.getQTYAC();
							}
							BigDecimal sumqty = issqty.add(tranQty);
							// sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);

							String extraCond = " AND  QtyOr >= isNull(QTYAC,0) + " + issuingQty;
							if (Ordqty.compareTo(sumqty) == 0) {
								updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty + ", RECSTAT='C' ";

							} else {
								updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty + ", RECSTAT='O' ";

							}

							boolean insertFlag = _ToDetDAO.update(updateEstDet, htCondition, extraCond);

							if (insertFlag) {
								htCondition.remove("TOLNNO");

								insertFlag = _ToDetDAO.isExisit(htCondition, "RECSTAT in ('O','N')");
								if (!insertFlag) {
									updateEstHdr = "set  RECSTAT='C',ORDER_STATUS='INVOICED' ";
									insertFlag = _ToHdrDAO.update(updateEstHdr, htCondition, "");
								}

							}
						}
					}
				}
				
				

				if (isAdded) {
					/*
					 * ShipHisDAO shiphstdao = new ShipHisDAO(); Hashtable<String, String>
					 * htTrandId1 = new Hashtable<String, String>();
					 * htTrandId1.put(IConstants.INVOICENO, invoiceNo);
					 * htTrandId1.put(IConstants.PLANT, plant); boolean flag =
					 * shiphstdao.isExisit(htTrandId1); // Check SHIPHIS
					 */ 
					//result = "Invoice created successfully";
					flagout = true;

				} else {
//					result = "Invoice not created";
					flagout = false;
					break;
				}

			}

			if (flagout) {
				return flagout;
			} else {
				return flagout;
			}

	}
	
	public boolean processMovHis_OUTforInvoice(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htMovHis = new Hashtable();
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovHis.put("DIRTYPE", TransactionConstants.INVOICE_PICK_ISSUE);
			htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovHis.put("BATNO", map.get(IConstants.BATCH));
			htMovHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htMovHis.put(IDBConstants.POHDR_JOB_NUM, "");
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.INVOICENO));
			htMovHis.put("MOVTID", "OUT");
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htMovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes((String)map.get(IConstants.INVOICENO)));
			
			flag = movHisDao.insertIntoMovHis(htMovHis);

		} catch (Exception e) {
			e.printStackTrace();
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
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



