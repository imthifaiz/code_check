package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinProject;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.ExceptionUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.ExceptionUtil.Result;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.ReturnOrderUtil;
import com.track.db.util.SupplierCreditUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class CreditNoteServlet
 */
@WebServlet("/CustomerCreditNoteServlet")
public class CustomerCreditNoteServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.CustomerCreditNoteServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CustomerCreditNoteServlet_PRINTPLANTMASTERINFO;


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("Submit")).trim();
		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		JSONObject jsonObjectResult = new JSONObject();

		if (action.equalsIgnoreCase("Save")) {

			/* InvoiceHdr */
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "", cmd = "",
					TranId = "", itemRates = "", discount = "", discountType = "", discountAccount = "",taxamount = "0",
					shippingCost = "", adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",gino="",currencyid="",currencyuseqt="0",
					empno = "", terms = "", custName = "", empName = "", Criditnote = "", Criditdate = "",soreturn="",sorstatus="",orderdiscount="",
					adjustmentname = "", invid = "", creditstatus = "", invoicenum = "", reference = "", payment_terms ="",salesloc = "",invsalesloc="",taxtreatment="",custname="",
					projectid="",orderdiscstatus = "0",discstatus = "0",taxid = "",orderdisctype = "%",gst="0",shippingid="",shippingcust="";
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="";
			/* InvoiceDet */
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(), cost = new ArrayList(), invqty = new ArrayList(),
					detDiscount = new ArrayList(), detDiscounttype = new ArrayList(), taxType = new ArrayList(),invlnno = new ArrayList(),cnqty = new ArrayList(),
					DETID = new ArrayList(), amount = new ArrayList();
			List<Hashtable<String, String>> creditnoteDetInfoList = null;
			List<Hashtable<String, String>> creditnoteAttachmentList = null;
			List<Hashtable<String, String>> creditnoteAttachmentInfoList = null;
			Hashtable<String, String> creditnoteDetInfo = null;
			Hashtable<String, String> creditnoteAttachment = null;
			InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			ExceptionUtil exceptionUtil=null;
			UserTransaction ut = null;
			MovHisDAO movHisDao = new MovHisDAO();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			CustomerCreditnoteDAO customerCreditDao = new CustomerCreditnoteDAO();
			DateUtils dateutils = new DateUtils();
			boolean isAdded = false;
			String result = "";
			int itemCount = 0, accountNameCount = 0, qtyCount = 0, costCount = 0, detDiscountCount = 0,
					detDiscounttypeCount = 0, taxTypeCount = 0, DETIDCount = 0, amountCount = 0, invlnnoCount = 0,invqtyCount = 0, cnqtyCount = 0;
			try {
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					StrUtils strUtils = new StrUtils();
					creditnoteAttachmentList = new ArrayList<Hashtable<String, String>>();
					creditnoteAttachmentInfoList = new ArrayList<Hashtable<String, String>>();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						/* InvoiceHdr */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
								CustCode = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER")) {
								custname = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("creditnote")) {
								Criditnote = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("creditnote_date")) {
								Criditdate = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								itemRates = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								empno = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
								//subTotal = StrUtils.fString(fileItem.getString()).trim();
								subTotal = String.valueOf(( Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								//shippingCost = StrUtils.fString(fileItem.getString()).trim();
								shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								//adjustment = StrUtils.fString(fileItem.getString()).trim();
								adjustment = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								discount = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderdiscount")) {
								orderdiscount = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								discountType = StrUtils.fString(fileItem.getString()).trim();
							}

							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("discount_account")) { double
							 * discountVal = Double.parseDouble(discount); if (discountVal > 0) {
							 * discountAccount = StrUtils.fString(fileItem.getString()).trim(); } }
							 */

							if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
								//totalAmount = StrUtils.fString(fileItem.getString()).trim();
								totalAmount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxamount")) {
								//taxamount = StrUtils.fString(fileItem.getString()).trim();
								taxamount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("terms")) {
								terms = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("soreturn")) {
								soreturn = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("soreturnstatus")) {
								sorstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment_name")) {
								adjustmentname = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("invselect")) {
								invid = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("credit_status")) {
								creditstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("invoice")) {
								invoicenum = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("gino")) {
								gino = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
								salesloc = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("STATE_PREFIX")) {
								invsalesloc = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxtreatment = StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								
								taxid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
								
								orderdisctype = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								
								gst = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								shippingid=StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								shippingcust=StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								payment_terms = StrUtils.fString(fileItem.getString()).trim();
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

						}

						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/CreditNote" + "/" + CustCode + "/" + invoiceNo;
							String filetempLocation = "C:/ATTACHMENTS/CreditNote" + "/temp" + "/" + CustCode + "/"
									+ invoiceNo;
							String fileName = StrUtils.fString(fileItem.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

							File path = new File(fileLocation);
							if (!path.exists()) {
								path.mkdirs();
							}

							// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "/" + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}

							fileItem.write(uploadedFile);

							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/" + fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							creditnoteAttachment = new Hashtable<String, String>();
							creditnoteAttachment.put("PLANT", plant);
							creditnoteAttachment.put("FILETYPE", fileItem.getContentType());
							creditnoteAttachment.put("FILENAME", fileName);
							creditnoteAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
							creditnoteAttachment.put("FILEPATH", fileLocation);
							creditnoteAttachment.put("CRAT", dateutils.getDateTime());
							creditnoteAttachment.put("CRBY", username);
							creditnoteAttachment.put("UPAT", dateutils.getDateTime());
							creditnoteAttachmentList.add(creditnoteAttachment);
						}

						/* InvoiceDet */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(itemCount, StrUtils.fString(fileItem.getString()).trim());
								itemCount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("qty")) {
								qty.add(qtyCount, StrUtils.fString(fileItem.getString()).trim());
								qtyCount++;
							}
						}
						
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("invlnno")) {
								invlnno.add(invlnnoCount, StrUtils.fString(fileItem.getString()).trim());
								invlnnoCount++;
							}
						}
						
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cnqty")) {
								cnqty.add(cnqtyCount, StrUtils.fString(fileItem.getString()).trim());
								cnqtyCount++;
							}
						}
						
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("invqty")) {
								invqty.add(invqtyCount, StrUtils.fString(fileItem.getString()).trim());
								invqtyCount++;
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
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accountName.add(accountNameCount,StrUtils.fString(fileItem.getString()).trim());
								accountNameCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("item_discounttype")) {
								detDiscounttype.add(detDiscounttypeCount,
										StrUtils.fString(fileItem.getString()).trim());
								detDiscounttypeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								/*if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
									taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
								else
									taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
								taxTypeCount++;*/
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

					}

				}
				
				//Get Sales Location Name
				/*
				 * if(!salesloc.isEmpty()) { ArrayList arrList = new ArrayList(); MasterDAO
				 * movHisDAO= new MasterDAO(); Hashtable htData =new Hashtable(); arrList =
				 * movHisDAO.getSalesLocationName(salesloc, plant,""); Map m = (Map)
				 * arrList.get(0); salesloc = (String) m.get("STATE");
				 * 
				 * }
				 */
				
				if(!invsalesloc.isEmpty())
				{
					ArrayList arrList = new ArrayList();
					MasterDAO movHisDAO= new MasterDAO();
					Hashtable htData =new Hashtable();					
					arrList = movHisDAO.getSalesLocationName(invsalesloc, plant,"");
					Map m = (Map) arrList.get(0);
					invsalesloc = (String) m.get("STATE");
				
				}
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();
				
				if (!invoicenum.equalsIgnoreCase("")) {
					
					if(sorstatus.equalsIgnoreCase("SORETURN")) {
						if(!invoicenum.equalsIgnoreCase("")) {
						InvoiceDAO invoiceDAO = new InvoiceDAO();
						String  uquery = "SET CREDITNOTESSTATUS = 2";
						Hashtable conditions = new Hashtable();
						conditions.put("INVOICE", invoicenum);
						conditions.put("PLANT", plant);
						invoiceDAO.update(uquery, conditions, "");
						}
					}else {
						List arrList = new ArrayList();
						InvoiceDAO invoiceDAO = new InvoiceDAO();
						Hashtable htData = new Hashtable();
						htData.put("PLANT", plant);
						htData.put("ID", invid);
						arrList = invoiceDAO.getInvoiceHdrById(htData);
						Map m = (Map) arrList.get(0);
						dono = (String) m.get("DONO");
						
						boolean invoiceupstatus = true;
						for (int i = 0; i < item.size(); i++) {
							double debitqty = Double.valueOf((String) cnqty.get(i)) + Double.valueOf((String) qty.get(i));
							double invoiceqy = Double.valueOf((String) invqty.get(i));
							if(invoiceqy != debitqty) {
								invoiceupstatus = false;
							}
							String querydet = " SET CREDITNOTE_QTY='"+debitqty+"' ";
							Hashtable ht = new Hashtable();
							ht.put("INVOICEHDRID", invid);
							ht.put("LNNO", (String) invlnno.get(i));
							ht.put("PLANT", plant);
							invoiceDAO.updateInvoiceDet(querydet, ht, "");
						}
						
						if(invoiceupstatus) {
							String  uquery = "SET CREDITNOTESSTATUS = 1";
							Hashtable conditions = new Hashtable();
							conditions.put("ID", invid);
							conditions.put("PLANT", plant);
							invoiceDAO.update(uquery, conditions, "");
						}
						
					}

					

				}
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));
				
				if(!orderdisctype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Double.parseDouble(StrUtils.fString(orderdiscount)) / (Double.parseDouble(currencyuseqt))));	
				//////////////////////
				creditnoteDetInfoList = new ArrayList<Hashtable<String, String>>();
				Hashtable CreditnoteHdr = new Hashtable();
				CreditnoteHdr.put("PLANT", plant);
				CreditnoteHdr.put("CUSTNO", CustCode);
				CreditnoteHdr.put("CREDITNOTE", Criditnote);
				CreditnoteHdr.put("DONO", dono);
				CreditnoteHdr.put("CREDIT_DATE", Criditdate);
				CreditnoteHdr.put("EMPNO", empno);
				CreditnoteHdr.put("ITEM_RATES", itemRates);
				CreditnoteHdr.put("DISCOUNT", discount);
				CreditnoteHdr.put("ORDER_DISCOUNT", orderdiscount);
				CreditnoteHdr.put("DISCOUNT_TYPE", discountType);
				CreditnoteHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				CreditnoteHdr.put("SHIPPINGCOST", shippingCost);
				CreditnoteHdr.put("ADJUSTMENT", adjustment);
				CreditnoteHdr.put("ADJUSTMENT_LABEL", adjustmentname);
				CreditnoteHdr.put("SUB_TOTAL", subTotal);
				CreditnoteHdr.put("TOTAL_AMOUNT", totalAmount);
				CreditnoteHdr.put("CREDIT_STATUS", creditstatus);
				CreditnoteHdr.put("NOTE", note);
				CreditnoteHdr.put("TERMSCONDITIONS", terms);
				CreditnoteHdr.put("CRAT", dateutils.getDateTime());
				CreditnoteHdr.put("CRBY", username);
				CreditnoteHdr.put("INVOICE", invoicenum);
				CreditnoteHdr.put("REFERENCE", reference);
				CreditnoteHdr.put("TAXAMOUNT", taxamount);
				CreditnoteHdr.put("GINO", gino);
				CreditnoteHdr.put("SORETURN", soreturn);
				CreditnoteHdr.put("CURRENCYUSEQT", currencyuseqt);
				CreditnoteHdr.put("ORDERDISCOUNTTYPE", orderdisctype);
				CreditnoteHdr.put("TAXID", taxid);
				CreditnoteHdr.put("ISDISCOUNTTAX", discstatus);
				CreditnoteHdr.put("ISORDERDISCOUNTTAX", orderdiscstatus);
				CreditnoteHdr.put("OUTBOUD_GST", gst);
				CreditnoteHdr.put("PROJECTID", projectid);
				CreditnoteHdr.put("SHIPPINGID", shippingid);
				CreditnoteHdr.put("SHIPPINGCUSTOMER", shippingcust);
				CreditnoteHdr.put("PAYMENT_TERMS", payment_terms);
				if(salesloc.equalsIgnoreCase(""))
					CreditnoteHdr.put("SALES_LOCATION", invsalesloc);
				else
					CreditnoteHdr.put("SALES_LOCATION", salesloc);
				CreditnoteHdr.put("TAXTREATMENT",taxtreatment);
				CreditnoteHdr.put(IDBConstants.CURRENCYID, currencyid);
				CreditnoteHdr.put("SHIPCONTACTNAME",shipcontactname);
				CreditnoteHdr.put("SHIPDESGINATION",shipdesgination);
				CreditnoteHdr.put("SHIPWORKPHONE",shipworkphone);
				CreditnoteHdr.put("SHIPHPNO",shiphpno);
				CreditnoteHdr.put("SHIPEMAIL",shipemail);
				CreditnoteHdr.put("SHIPCOUNTRY",shipcountry);
				CreditnoteHdr.put("SHIPADDR1",shipaddr1);
				CreditnoteHdr.put("SHIPADDR2",shipaddr2);
				CreditnoteHdr.put("SHIPADDR3",shipaddr3);
				CreditnoteHdr.put("SHIPADDR4",shipaddr4);
				CreditnoteHdr.put("SHIPSTATE",shipstate);
				CreditnoteHdr.put("SHIPZIP",shipzip);
				
				int creditNoteHdrId = 0;

				creditNoteHdrId = customerCreditDao.addCreditnoteHdr(CreditnoteHdr, plant);

				if (creditNoteHdrId > 0) {
					for (int i = 0; i < item.size(); i++) {
						int lnno = i + 1;
						String convDiscount=""; 
						String convCost = String.valueOf((Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
						
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i)) / Double.parseDouble(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));
						
						creditnoteDetInfo = new Hashtable<String, String>();
						creditnoteDetInfo.put("PLANT", plant);
						creditnoteDetInfo.put("LNNO", Integer.toString(lnno));
						creditnoteDetInfo.put("HDRID", Integer.toString(creditNoteHdrId));
						creditnoteDetInfo.put("ITEM", (String) item.get(i));
						creditnoteDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						creditnoteDetInfo.put("QTY", (String) qty.get(i));
						creditnoteDetInfo.put("UNITPRICE", convCost);
						creditnoteDetInfo.put("DISCOUNT", convDiscount);
						creditnoteDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						creditnoteDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						creditnoteDetInfo.put("AMOUNT", convAmount);
						creditnoteDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						creditnoteDetInfo.put("CRAT", dateutils.getDateTime());
						creditnoteDetInfo.put("CRBY", username);
						creditnoteDetInfo.put("UPAT", dateutils.getDateTime());
						creditnoteDetInfoList.add(creditnoteDetInfo);
					}

					isAdded = customerCreditDao.addMultipleCreditnoteDet(creditnoteDetInfoList, plant);

					int attchSize = creditnoteAttachmentList.size();
					for (int i = 0; i < attchSize; i++) {
						creditnoteAttachment = new Hashtable<String, String>();
						creditnoteAttachment = creditnoteAttachmentList.get(i);
						creditnoteAttachment.put("HDRID", Integer.toString(creditNoteHdrId));
						creditnoteAttachmentInfoList.add(creditnoteAttachment);
					}
					if (isAdded) {
						Double orderDiscountFroma = Double.parseDouble(orderdiscount);
						if(orderdisctype.toString().equalsIgnoreCase("%"))
							orderdiscount=Double.toString((Double.parseDouble(subTotal)*Double.parseDouble(orderdiscount))/100);
						
						if (creditnoteAttachmentInfoList.size() > 0)
							isAdded = customerCreditDao.addCreditnoteAttachments(creditnoteAttachmentInfoList, plant);
						
						if(!creditstatus.equalsIgnoreCase("Draft"))
						{
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(Criditdate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("CUSTOMERCREDITNOTES");
						journalHead.setTRANSACTION_ID(Criditnote);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						CustMstDAO cusDAO=new CustMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						for(Map CNDet:creditnoteDetInfoList)
						{
							Double quantity=Double.parseDouble(CNDet.get("QTY").toString());
							String netWeight=itemMstDAO.getItemNetWeight(plant, CNDet.get("ITEM").toString());
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
							//System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
							
							//double discpercal = (Double.parseDouble(CNDet.get("AMOUNT").toString()) * 100)/Double.parseDouble(subTotal);
							//double deductamt = (Double.parseDouble(orderdiscount.toString())/100)*discpercal;
							//journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())- deductamt);
							
							
							
							if(!orderdisctype.toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderDiscountFroma.toString())/creditnoteDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderDiscountFroma.toString());
								journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
							}
							
							
							
							
							//journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderdiscount));
							boolean isLoop=false;
							if(journalDetails.size()>0)
							{
								int i=0;
								for(JournalDetail journal:journalDetails) {
									int accountId=journal.getACCOUNT_ID();
									if(accountId==journalDetail.getACCOUNT_ID()) {
										isLoop=true;
										Double sumDetit=journal.getDEBITS()+journalDetail.getDEBITS();
										journalDetail.setDEBITS(sumDetit);
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
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) CustCode);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject cusJson=cusDAO.getCustomerName(plant, (String) CustCode);
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
							journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
							
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
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else if(planttaxtype.equalsIgnoreCase("GST")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Receivable");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("GST Receivable");
							}else if(planttaxtype.equalsIgnoreCase("VAT")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}
							
							journalDetail_2.setDEBITS(taxAmountFrom);
							journalDetails.add(journalDetail_2);
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
							orderDiscountFrom=(Double.parseDouble(subTotal)*orderDiscountFrom)/100;
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
							journalDetail_3.setCREDITS(discountFrom);
							journalDetails.add(journalDetail_3);
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
								journalDetail_7.setDEBITS(adjustFrom);
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
								journalDetail_7.setCREDITS(adjustFrom);
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
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
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
						}
						}
					}
					
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_CREDIT_NOTE);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(Criditdate));														
						htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
						String billqty = String.valueOf((String) qty.get(i));
						htMovHis.put(IDBConstants.QTY, billqty);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, Criditnote);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						if(dono.isEmpty())
							dono=reference;
						htMovHis.put("REMARKS",custname+","+dono+","+gino+","+invoicenum+","+soreturn+","+note);
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
					//Update Credit Note Seq
					if(isAdded) {
					Hashtable htv = new Hashtable();				
	    			htv.put("PLANT", plant);
	    			htv.put("FUNC", "CUSTOMER_CREDIT_NOTE");
	    			isAdded = _TblControlDAO.isExisit(htv, "", plant);
	    			if (isAdded) 
	    				isAdded=_TblControlDAO.updateSeqNo("CUSTOMER_CREDIT_NOTE",plant);
					}
				}
				
				/* <----Recevie hdr and det-----> */
				if (creditstatus.equalsIgnoreCase("Open")) {
					InvPaymentHeader invoicePaymentHeader = new InvPaymentHeader();
					InvPaymentDetail invoicePaymentDetail = new InvPaymentDetail();

					invoicePaymentHeader.setPLANT(plant);
					invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
					invoicePaymentHeader.setCUSTNO(CustCode);
					invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(totalAmount));
					invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble("0"));
					//invoicePaymentHeader.setBANK_BRANCH();
					//invoicePaymentHeader.setCHECQUE_NO(item.getString());
					invoicePaymentHeader.setRECEIVE_DATE(dateutils.getDB2UserDate_New(dateutils.getDateTime()));
					invoicePaymentHeader.setRECEIVE_MODE("Credit Note");
					invoicePaymentHeader.setREFERENCE(Criditnote);
					invoicePaymentHeader.setNOTE(note);
					invoicePaymentHeader.setCURRENCYID(currencyid);
					invoicePaymentHeader.setTRANSACTIONID("");
					
				
					exceptionUtil=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
					  if(exceptionUtil.getStatus()==Result.OK)
					  {
						invoicePaymentDetail.setPLANT(plant);
						invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt("0"));
						invoicePaymentDetail.setDONO("0");
						int receiveHeaderId=(int) exceptionUtil.getResultData();
						invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
						invoicePaymentDetail.setAMOUNT(Double.parseDouble(totalAmount));
						invoicePaymentDetail.setLNNO(0);
						invoicePaymentDetail.setTYPE("ADVANCE");
						invoicePaymentDetail.setBALANCE(Double.parseDouble(totalAmount));
						invoicePaymentDetail.setADVANCEFROM("CREDITNOTE");
						invoicePaymentDetail.setCREDITNOTEHDRID(creditNoteHdrId);
						invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble(currencyuseqt));
						invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
						invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
						
					  }
					  
					  Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT_RECEIVED);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));																			
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(exceptionUtil.getResultData()));
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",Criditnote+","+invoicePaymentHeader.getAMOUNTRECEIVED());
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				}
				
				if (isAdded) {
					if(sorstatus.equalsIgnoreCase("SORETURN")) {
						
						
						String soquery = "LNNO,ISNULL(RETURN_QTY,'0') AS RETURN_QTY,ISNULL(CREDITNOTE_QTY,'0') AS CREDITNOTE_QTY";
						String soretcondition = "SORETURN = '"+soreturn+"' AND STATUS != 'Applied'";
						List<Map<String, String>> soreturnbypono = returnOrderDAO.getSOReturnDetailsbycustomer(plant, soquery, soretcondition);
						int por = 0;
						for (Map<String, String> map : soreturnbypono) {
							double rqty = Double.valueOf(map.get("RETURN_QTY"));
							double cqty = Double.valueOf(map.get("CREDITNOTE_QTY"));
							double fqty = Double.valueOf((String) qty.get(por)) + cqty;
							
							if(rqty == fqty) {
								String setquery = "STATUS = 'Applied',CREDITNOTE_QTY = '"+fqty+"'";
								String pocondition = "SORETURN = '"+soreturn+"' AND DONO = '"+reference+"' AND INVOICE = '"+invoicenum+"' AND LNNO = '"+map.get("LNNO")+"'";
								returnOrderDAO.updateSoReturnDetails(setquery, pocondition, plant);
							}else {
								String setquery = "STATUS = 'Partially Applied',CREDITNOTE_QTY = '"+fqty+"'";
								String pocondition = "SORETURN = '"+soreturn+"' AND DONO = '"+reference+"' AND INVOICE = '"+invoicenum+"' AND LNNO = '"+map.get("LNNO")+"'";
								returnOrderDAO.updateSoReturnDetails(setquery, pocondition, plant);
							}
							
							por = por + 1;
						}
						
					}
				}
				
				
				if (isAdded) {

					/*TblControlDAO _TblControlDAO = new TblControlDAO();
					_TblControlDAO.updateLatestSeqNo("CUSTOMER_CREDIT_NOTE", plant, "0");*/
					DbBean.CommitTran(ut);					
					result = "Credit Note created successfully";
				} else {
					DbBean.RollbackTran(ut);
					result = "Credit Note not created";
				}
				// response.sendRedirect("jsp/createInvoice.jsp?result="+ result);/* Redirect to
				// Invoice Summary page */
				if(result.equalsIgnoreCase("Credit Note not created"))
					response.sendRedirect("../creditnote/new?result="+ result);
				else
					response.sendRedirect("../creditnote/summary?result="+ result);

			} catch (Exception e) {
				System.out.println(e);
				DbBean.RollbackTran(ut);
				 e.printStackTrace();
				response.sendRedirect("../creditnote/new?result="+ e.toString());
			}

		} else if (baction.equals("VIEW_CREDIT_NOTE_SUMMARY_VIEW")) {

			jsonObjectResult = this.getcreditnoteview(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

		}else if(action.equalsIgnoreCase("getsoreturnnoByCustandcredit"))
		{
			String SORETURN_STATUS=StrUtils.fString(request.getParameter("SORETURN_STATUS")).trim();
			String custno=StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			String plant=StrUtils.fString(request.getParameter("PLANT")).trim();
			List soreturnlist=new ArrayList<>();
			
			ReturnOrderDAO returnOrderDAO=new ReturnOrderDAO();
			JSONObject soreturnJson=new JSONObject();
			try {
				String query="SORETURN";
				String extquery = "PLANT = '"+plant+"' AND CUSTNO ='"+custno+"' AND STATUS != 'Applied' AND INVOICE != '' GROUP BY SORETURN ORDER BY SORETURN DESC";
				soreturnlist=returnOrderDAO.getSOReturnDetailsbycustomer(plant, query, extquery);
				soreturnJson.put("sortno",soreturnlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			response.getWriter().write(soreturnJson.toString());
		}else if (baction.equals("CONVERT_CREDIT_NOTE_DRAFT_TO_OPEN")) {

			CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
			MovHisDAO movHisDao = new MovHisDAO();	
			InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			ExceptionUtil exceptionUtil=null;
			String custcrdnoteHDRid = StrUtils.fString(request.getParameter("CRHDRID"));
			String plant = StrUtils.fString(request.getParameter("PLANT")); 
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			UserTransaction ut = null;
			try {
				Hashtable ht = new Hashtable();
				ht.put("ID", custcrdnoteHDRid);
				ht.put("PLANT", plant);
				List CustcrdnoteHdrList;
				CustcrdnoteHdrList = custcrdnotedao.getCustCreditnoteHdrById(ht);
				Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
				
				
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();

				InvPaymentHeader invoicePaymentHeader = new InvPaymentHeader();
				InvPaymentDetail invoicePaymentDetail = new InvPaymentDetail();
				DateUtils dateutils = new DateUtils();
				
				invoicePaymentHeader.setPLANT(plant);
				invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
				invoicePaymentHeader.setCUSTNO((String)CustcrdnoteHdr.get("CUSTNO"));
				invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
				invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble("0"));
				//invoicePaymentHeader.setBANK_BRANCH();
				//invoicePaymentHeader.setCHECQUE_NO(item.getString());
				invoicePaymentHeader.setRECEIVE_DATE(dateutils.getDB2UserDate_New(dateutils.getDateTime()));
				invoicePaymentHeader.setRECEIVE_MODE("Credit Note");
				invoicePaymentHeader.setREFERENCE((String)CustcrdnoteHdr.get("CREDITNOTE"));
				invoicePaymentHeader.setNOTE((String)CustcrdnoteHdr.get("NOTE"));
				invoicePaymentHeader.setCURRENCYID((String)CustcrdnoteHdr.get("CURRENCYID"));
				invoicePaymentHeader.setTRANSACTIONID("");
				
				
				exceptionUtil=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
				  if(exceptionUtil.getStatus()==Result.OK)
				  {

					invoicePaymentDetail.setPLANT(plant);
					invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt("0"));
					invoicePaymentDetail.setDONO("0");
					 int receiveHeaderId=(int) exceptionUtil.getResultData();
					invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
					invoicePaymentDetail.setAMOUNT(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
					invoicePaymentDetail.setLNNO(0);
					invoicePaymentDetail.setTYPE("ADVANCE");
					invoicePaymentDetail.setBALANCE(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
					invoicePaymentDetail.setADVANCEFROM("CREDITNOTE");
					invoicePaymentDetail.setCREDITNOTEHDRID(Integer.parseInt(custcrdnoteHDRid));
					invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble((String)CustcrdnoteHdr.get("CURRENCYUSEQT")));
					invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
					invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
					
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT_RECEIVED);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));																			
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(exceptionUtil.getResultData()));
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",CustcrdnoteHdr.get("CREDITNOTE")+","+invoicePaymentHeader.getAMOUNTRECEIVED());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

					
				  }
				  CustomerCreditnoteDAO customerCreditDao = new CustomerCreditnoteDAO();
				  Hashtable CreditnoteHdr = new Hashtable();
					CreditnoteHdr.put("PLANT", plant);
					CreditnoteHdr.put("CUSTNO", (String)CustcrdnoteHdr.get("CUSTNO"));
					CreditnoteHdr.put("CREDITNOTE", (String)CustcrdnoteHdr.get("CREDITNOTE"));
					CreditnoteHdr.put("DONO", (String)CustcrdnoteHdr.get("DONO"));
					CreditnoteHdr.put("GINO", (String)CustcrdnoteHdr.get("GINO"));
					CreditnoteHdr.put("CREDIT_DATE", (String)CustcrdnoteHdr.get("CREDIT_DATE"));
					CreditnoteHdr.put("EMPNO", (String)CustcrdnoteHdr.get("EMPNO"));
					CreditnoteHdr.put("ITEM_RATES", (String)CustcrdnoteHdr.get("ITEM_RATES"));
					CreditnoteHdr.put("DISCOUNT", (String)CustcrdnoteHdr.get("DISCOUNT"));
					CreditnoteHdr.put("DISCOUNT_TYPE", (String)CustcrdnoteHdr.get("DISCOUNT_TYPE"));
					CreditnoteHdr.put("DISCOUNT_ACCOUNT", (String)CustcrdnoteHdr.get("DISCOUNT_ACCOUNT"));
					CreditnoteHdr.put("SHIPPINGCOST", (String)CustcrdnoteHdr.get("SHIPPINGCOST"));
					CreditnoteHdr.put("ADJUSTMENT", (String)CustcrdnoteHdr.get("ADJUSTMENT"));
					CreditnoteHdr.put("ADJUSTMENT_LABEL", (String)CustcrdnoteHdr.get("ADJUSTMENT_LABEL"));
					CreditnoteHdr.put("SUB_TOTAL", (String)CustcrdnoteHdr.get("SUB_TOTAL"));
					CreditnoteHdr.put("TOTAL_AMOUNT", (String)CustcrdnoteHdr.get("TOTAL_AMOUNT"));
					CreditnoteHdr.put("CREDIT_STATUS", "Open");
					CreditnoteHdr.put("NOTE", (String)CustcrdnoteHdr.get("NOTE"));
					CreditnoteHdr.put("TERMSCONDITIONS", (String)CustcrdnoteHdr.get("TERMSCONDITIONS"));
					CreditnoteHdr.put("CRAT", (String)CustcrdnoteHdr.get("CRAT"));
					CreditnoteHdr.put("CRBY", (String)CustcrdnoteHdr.get("CRBY"));
					CreditnoteHdr.put("INVOICE", (String)CustcrdnoteHdr.get("INVOICE"));
					CreditnoteHdr.put("REFERENCE", (String)CustcrdnoteHdr.get("REFERENCE"));
					CreditnoteHdr.put("SALES_LOCATION", (String)CustcrdnoteHdr.get("SALES_LOCATION"));
					CreditnoteHdr.put("ID", custcrdnoteHDRid);
					CreditnoteHdr.put("UPBY", username);

					//customerCreditDao.updateCreditNoteHdr(CreditnoteHdr);
					
					String crquery = "SET CREDIT_STATUS = 'Open'";
					Hashtable CreditnoteHdrupdate = new Hashtable();
					CreditnoteHdrupdate.put("ID", custcrdnoteHDRid);
					CreditnoteHdrupdate.put("PLANT", plant);
					customerCreditDao.update(crquery, CreditnoteHdrupdate, "");
					
					List<Hashtable<String, String>> creditnoteDetInfoList=custcrdnotedao.getCustCrdnoteDedtByHrdId(CreditnoteHdrupdate);
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					//Journal Entry
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE((String)CustcrdnoteHdr.get("CREDIT_DATE"));
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("CUSTOMERCREDITNOTES");
					journalHead.setTRANSACTION_ID((String)CustcrdnoteHdr.get("CREDITNOTE"));
					journalHead.setSUB_TOTAL(Double.parseDouble((String)CustcrdnoteHdr.get("SUB_TOTAL")));
					//journalHead.setTOTAL_AMOUNT(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					List<JournalDetail> journalDetails=new ArrayList<>();
					CoaDAO coaDAO=new CoaDAO();
					CustMstDAO cusDAO=new CustMstDAO();
					ItemMstDAO itemMstDAO=new ItemMstDAO();
					Double totalItemNetWeight=0.00;
					for(Map CNDet:creditnoteDetInfoList)
					{
						Double quantity=Double.parseDouble(CNDet.get("QTY").toString());
						String netWeight=itemMstDAO.getItemNetWeight(plant, CNDet.get("ITEM").toString());
						Double Netweight=quantity*Double.parseDouble(netWeight);
						totalItemNetWeight+=Netweight;
						System.out.println("TotalNetWeight:"+totalItemNetWeight);
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
						//journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString()));
						if(!CustcrdnoteHdr.get("ORDERDISCOUNTTYPE").toString().equalsIgnoreCase("%")) {
							journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(CustcrdnoteHdr.get("ORDER_DISCOUNT").toString())/creditnoteDetInfoList.size());
						}else {
							Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(CustcrdnoteHdr.get("ORDER_DISCOUNT").toString());
							journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
						}
						boolean isLoop=false;
						if(journalDetails.size()>0)
						{
							int i=0;
							for(JournalDetail journal:journalDetails) {
								int accountId=journal.getACCOUNT_ID();
								if(accountId==journalDetail.getACCOUNT_ID()) {
									isLoop=true;
									Double sumDetit=journal.getDEBITS()+journalDetail.getDEBITS();
									journalDetail.setDEBITS(sumDetit);
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
					JSONObject coaJson1=coaDAO.getCOAByName(plant, (String)CustcrdnoteHdr.get("CUSTNO"));
					if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
						JSONObject cusJson=cusDAO.getCustomerName(plant, (String)CustcrdnoteHdr.get("CUSTNO"));
						if(!cusJson.isEmpty()) {
						coaJson1=coaDAO.getCOAByName(plant, cusJson.getString("CUSTNO")+"-"+cusJson.getString("CNAME"));
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
						journalDetail_1.setCREDITS(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
						journalDetails.add(journalDetail_1);
						
					}
				
					Double taxAmountFrom=Double.parseDouble((String)CustcrdnoteHdr.get("TAXAMOUNT"));
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
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Input");
						}else if(planttaxtype.equalsIgnoreCase("GST")) {
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Receivable");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("GST Receivable");
						}else if(planttaxtype.equalsIgnoreCase("VAT")) {
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Input");
						}else {
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Input");
						}
						
						journalDetail_2.setDEBITS(taxAmountFrom);
						journalDetails.add(journalDetail_2);
					}
					
					String discount=(String)CustcrdnoteHdr.get("DISCOUNT");
					if(discount.isEmpty())
					{
						discount="0.00";
					}
					Double discountFrom = Double.parseDouble(discount);
					String discountType=CustcrdnoteHdr.get("DISCOUNT_TYPE").toString();
					String orderdiscount=CustcrdnoteHdr.get("ORDER_DISCOUNT").toString();
					Double orderDiscountFrom=0.00;
					if(!orderdiscount.isEmpty())
					{
						orderDiscountFrom=Double.parseDouble(orderdiscount);
						orderDiscountFrom=(Double.parseDouble(CustcrdnoteHdr.get("SUB_TOTAL").toString())*orderDiscountFrom)/100;
					}
					if(discountFrom>0 || orderDiscountFrom>0)
					{
						if(!discountType.isEmpty())
						{
							if(discountType.equalsIgnoreCase("%"))
							{
								Double subTotalAfterOrderDiscount=Double.parseDouble(CustcrdnoteHdr.get("SUB_TOTAL").toString());
								discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
							}
						}
						discountFrom=discountFrom;
						JournalDetail journalDetail_3=new JournalDetail();
						journalDetail_3.setPLANT(plant);
						JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discounts given - COS");
						journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
						journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
						journalDetail_3.setCREDITS(discountFrom);
						journalDetails.add(journalDetail_3);
					}
					String adjustment=(String)CustcrdnoteHdr.get("ADJUSTMENT");
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
							journalDetail_7.setDEBITS(adjustFrom);
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
							journalDetail_7.setCREDITS(adjustFrom);
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
							Hashtable jhtMovHis = new Hashtable();
							jhtMovHis.put(IDBConstants.PLANT, plant);
							jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
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
					}
					
					
					DbBean.CommitTran(ut);
				
					
					response.sendRedirect("../creditnote/detail?custcreditid="+custcrdnoteHDRid);
				
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
			}
			
			
			
		} else if(baction.equalsIgnoreCase("convertToCancel")) {
			String creditnoteHdrId = StrUtils.fString(request.getParameter("CreditnoteHdrId"));
			String creditnote = StrUtils.fString(request.getParameter("CreditNote"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateutils = new DateUtils();
			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
			String poreturn = "";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				
				String crquery = "SET CREDIT_STATUS = 'CANCELLED'";
				Hashtable CreditnoteHdrupdate = new Hashtable();
				CreditnoteHdrupdate.put("ID", creditnoteHdrId);
				CreditnoteHdrupdate.put("PLANT", plant);
				int cid = custcrdnotedao.update(crquery, CreditnoteHdrupdate, "");
				
				InvPaymentDetail invdetail = invoicePaymentDAO.getInvoicePaymentDetailsbycreditid(Integer.parseInt(creditnoteHdrId), plant);
				if(invdetail.getCREDITNOTEHDRID() > 0){
					invoicePaymentDAO.deleteInvoicepaymentall(plant, String.valueOf(invdetail.getRECEIVEHDRID()));
				}
				
				if(cid > 0) {
					JournalService journalService=new JournalEntry();
					Journal journalFrom=journalService.getJournalByTransactionId(plant, creditnote,"CUSTOMERCREDITNOTES");
					if(journalFrom.getJournalHeader()!=null){
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
							ht1.put("ID", creditnoteHdrId);
							ht1.put("PLANT", plant);
							
							List CNhdr = custcrdnotedao.getCustCreditnoteHdrById(ht1);
							Map cnHdrmap=(Map)CNhdr.get(0);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("ID", creditnoteHdrId);
							ht2.put("PLANT", plant);
							
							List CNdet = custcrdnotedao.getCustCrdnoteDedtByHrdId(ht2);
							
								for(int i =0 ; i < CNdet.size() ; i++){
									Map cndetmap=(Map)CNdet.get(i);
									Hashtable htMovHis = new Hashtable();
									htMovHis.clear();
									htMovHis.put(IDBConstants.PLANT, plant);					
									htMovHis.put("DIRTYPE", TransactionConstants.CANCEL_CREDIT_NOTE);
									htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
									htMovHis.put(IDBConstants.ITEM, (String) cndetmap.get("ITEM"));
									String billqty = String.valueOf((String) cndetmap.get("QTY"));
									htMovHis.put(IDBConstants.QTY, billqty);
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
									htMovHis.put(IDBConstants.CREATED_BY, username);		
									htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
									//htMovHis.put("REMARKS",custname+","+dono+","+gino+","+invoicenum+","+soreturn+","+note);
									htMovHis.put("REMARKS",cnHdrmap.get("CUSTNO")+","+cnHdrmap.get("DONO")+","+cnHdrmap.get("GINO")+","+cnHdrmap.get("INVOICE")+","+cnHdrmap.get("SORETURN")+","+cnHdrmap.get("NOTE"));
									boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
										
								}
								
								DbBean.CommitTran(ut);
								result = "Credit Note cancelled successfully.";
								response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
											
				}else {
					DbBean.RollbackTran(ut);
					result = "Couldn't cancel the Credit Note.";
					response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
				}
				
				
							
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't cancel the Credit Note.";
				response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
			}
			
		}
		
			
		else if(baction.equalsIgnoreCase("convertToDraft")) {
			String creditnoteHdrId = StrUtils.fString(request.getParameter("CreditnoteHdrId"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateutils = new DateUtils();
			CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
			String poreturn = "";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				
				String crquery = "SET CREDIT_STATUS = 'Draft'";
				Hashtable CreditnoteHdrupdate = new Hashtable();
				CreditnoteHdrupdate.put("ID", creditnoteHdrId);
				CreditnoteHdrupdate.put("PLANT", plant);
				int cid = custcrdnotedao.update(crquery, CreditnoteHdrupdate, "");
				
				DbBean.CommitTran(ut);
				result = "Credit Note drafted successfully.";
				response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
							
							
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't draft the Credit Note.";
				response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
			}
			
		}
		
		else if(baction.equalsIgnoreCase("deletecreditnote")) {
			String creditnoteHdrId = StrUtils.fString(request.getParameter("CreditnoteHdrId"));
			String creditnote = StrUtils.fString(request.getParameter("CreditNote"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			InvoiceDAO invoicedao = new InvoiceDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateutils = new DateUtils();
			ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
			//String poreturn = "";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", creditnoteHdrId);
				ht1.put("PLANT", plant);
				
				List CNhdr = custcrdnotedao.getCustCreditnoteHdrById(ht1);
				Map cnHdrmap=(Map)CNhdr.get(0);
				
				Hashtable ht2 = new Hashtable();
				ht2.put("ID", creditnoteHdrId);
				ht2.put("PLANT", plant);
				
				List CNdet = custcrdnotedao.getCustCrdnoteDedtByHrdId(ht2);
					
				isUpdated = custcrdnotedao.deleteCustomerCrdnote(plant, creditnoteHdrId);
						if(isUpdated) {
							
							InvPaymentDetail invdetail = invoicePaymentDAO.getInvoicePaymentDetailsbycreditid(Integer.parseInt(creditnoteHdrId), plant);
							if(invdetail.getCREDITNOTEHDRID() > 0){
								invoicePaymentDAO.deleteInvoicepaymentall(plant, String.valueOf(invdetail.getRECEIVEHDRID()));
							}
							
							if(!cnHdrmap.get("INVOICE").equals("")) {
								Hashtable htgi = new Hashtable();	
								String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET CREDITNOTESSTATUS='0',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND INVOICE='"+cnHdrmap.get("INVOICE")+"'";
								invoicedao.updategino(sqlgi, htgi, "");
							}
							
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, creditnote,"CUSTOMERCREDITNOTES");
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
						
								if(!cnHdrmap.get("SORETURN").equals("")) {
									String setquery = "STATUS = 'Not Applied'";
									String pocondition = "SORETURN = '"+cnHdrmap.get("SORETURN")+"' AND GINO = '"+cnHdrmap.get("GINO")+"'";
									returnOrderDAO.updateSoReturnDetails(setquery, pocondition, plant);
								}
								
								for(int i =0 ; i < CNdet.size() ; i++){
									Map cndetmap=(Map)CNdet.get(i);
									Hashtable htMovHis = new Hashtable();
									htMovHis.clear();
									htMovHis.put(IDBConstants.PLANT, plant);					
									htMovHis.put("DIRTYPE", TransactionConstants.DELETE_CREDIT_NOTE);
									htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
									htMovHis.put(IDBConstants.ITEM, (String) cndetmap.get("ITEM"));
									String billqty = String.valueOf((String) cndetmap.get("QTY"));
									htMovHis.put(IDBConstants.QTY, billqty);
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
									htMovHis.put(IDBConstants.CREATED_BY, username);		
									htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
									//htMovHis.put("REMARKS",custname+","+dono+","+gino+","+invoicenum+","+soreturn+","+note);
									htMovHis.put("REMARKS",cnHdrmap.get("CUSTNO")+","+cnHdrmap.get("DONO")+","+cnHdrmap.get("GINO")+","+cnHdrmap.get("INVOICE")+","+cnHdrmap.get("SORETURN")+","+cnHdrmap.get("NOTE"));
									boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
										
								}
								
								DbBean.CommitTran(ut);
								result = "Credit Note Deleted successfully.";
								response.sendRedirect("../creditnote/summary?result="+ result);
								
						}else {
							DbBean.RollbackTran(ut);
							result = "Couldn't Delete the Credit Note .";
							response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
						}
			} catch (Exception e) {
				e.printStackTrace();
				DbBean.RollbackTran(ut);
				result = "Couldn't Delete the Credit Note .";
				response.sendRedirect("../creditnote/detail?custcreditid="+creditnoteHdrId+"&rsuccess="+ result);
			}
			
		}
		
		else if (action.equalsIgnoreCase("GET_EDIT_CREDIT_NOTE_DETAILS")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getEditCustCreditNoteDetails(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("SALES_RETURN_TO_CREDIT_NOTES")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getSalesreturnDetails(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("CHECK_CUSTOMER_CREDIT_NOTE")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.validateCreditnote(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("Edit")) {

			/* InvoiceHdr */
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "", cmd = "",
					TranId = "", itemRates = "", discount = "", discountType = "",orderdiscount="", discountAccount = "", taxamount = "0",
					shippingCost = "", adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",
					empno = "", terms = "", custName = "", empName = "", Criditnote = "", Criditdate = "",gino="",currencyid="",currencyuseqt="0",
					adjustmentname = "", invid = "", creditstatus = "", invoicenum = "", reference = "",salesloc="",invsalesloc="",taxtreatment="",custname="",
					projectid="",orderdiscstatus = "0",discstatus = "0",taxid = "",orderdisctype = "%",gst="0",shippingid="",shippingcust="",payment_terms="";
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="";

			/* InvoiceDet */
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(), cost = new ArrayList(),
					detDiscount = new ArrayList(), detDiscounttype = new ArrayList(), taxType = new ArrayList(),
					DETID = new ArrayList(), amount = new ArrayList();
			List<Hashtable<String, String>> creditnoteDetInfoList = null;
			List<Hashtable<String, String>> creditnoteAttachmentList = null;
			List<Hashtable<String, String>> creditnoteAttachmentInfoList = null;
			Hashtable<String, String> creditnoteDetInfo = null;
			Hashtable<String, String> creditnoteAttachment = null;
			
			UserTransaction ut = null;
			
			MovHisDAO movHisDao = new MovHisDAO();
			CustomerCreditnoteDAO customerCreditDao = new CustomerCreditnoteDAO();
			DateUtils dateutils = new DateUtils();
			boolean isAdded = false;
			String result = "";
			int itemCount = 0, accountNameCount = 0, qtyCount = 0, costCount = 0, detDiscountCount = 0,
					detDiscounttypeCount = 0, taxTypeCount = 0, DETIDCount = 0, amountCount = 0;
			try {
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					StrUtils strUtils = new StrUtils();
					creditnoteAttachmentList = new ArrayList<Hashtable<String, String>>();
					creditnoteAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						/* InvoiceHdr */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
								CustCode = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER")) {
								custname = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("creditnote")) {
								Criditnote = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("creditnote_date")) {
								Criditdate = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								itemRates = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								empno = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
								//subTotal = StrUtils.fString(fileItem.getString()).trim();
								subTotal = String.valueOf(( Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								//shippingCost = StrUtils.fString(fileItem.getString()).trim();
								shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								//adjustment = StrUtils.fString(fileItem.getString()).trim();
								adjustment = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
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
							 * discountVal = Double.parseDouble(discount); if (discountVal > 0) {
							 * discountAccount = StrUtils.fString(fileItem.getString()).trim(); } }
							 */

							if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
								//totalAmount = StrUtils.fString(fileItem.getString()).trim();
								totalAmount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxamount")) {
								//taxamount = StrUtils.fString(fileItem.getString()).trim();
								taxamount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
							}

							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("terms")) {
								terms = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment_name")) {
								adjustmentname = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("invselect")) {
								invid = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("credit_status")) {
								creditstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("invoice")) {
								invoicenum = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("gino")) {
								gino = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("HDRID")) {
								TranId = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DONO")) {
								dono = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
								salesloc = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("INV_STATE_LOC")) {
								invsalesloc = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxtreatment = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid=StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								
								taxid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
								
								orderdisctype = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								
								gst = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								shippingid=StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								shippingcust=StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								payment_terms=StrUtils.fString(fileItem.getString()).trim();
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

						}

						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/CreditNote" + "/" + CustCode + "/" + invoiceNo;
							String filetempLocation = "C:/ATTACHMENTS/CreditNote" + "/temp" + "/" + CustCode + "/"
									+ invoiceNo;
							String fileName = StrUtils.fString(fileItem.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

							File path = new File(fileLocation);
							if (!path.exists()) {
								path.mkdirs();
							}

							// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "/" + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}

							fileItem.write(uploadedFile);

							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/" + fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							creditnoteAttachment = new Hashtable<String, String>();
							creditnoteAttachment.put("PLANT", plant);
							creditnoteAttachment.put("FILETYPE", fileItem.getContentType());
							creditnoteAttachment.put("FILENAME", fileName);
							creditnoteAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
							creditnoteAttachment.put("FILEPATH", fileLocation);
							creditnoteAttachment.put("CRAT", dateutils.getDateTime());
							creditnoteAttachment.put("CRBY", username);
							creditnoteAttachment.put("UPAT", dateutils.getDateTime());
							creditnoteAttachmentList.add(creditnoteAttachment);
						}

						/* InvoiceDet */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(itemCount, StrUtils.fString(fileItem.getString()).trim());
								itemCount++;
							}
						}
						
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accountName.add(accountNameCount,StrUtils.fString(fileItem.getString()).trim());
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
								detDiscounttype.add(detDiscounttypeCount,
										StrUtils.fString(fileItem.getString()).trim());
								detDiscounttypeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								/*if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
									taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
								else
									taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
								taxTypeCount++;*/
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

					}

				}

				if (!invid.isEmpty()) {

					List arrList = new ArrayList();
					InvoiceDAO invoiceDAO = new InvoiceDAO();
					Hashtable htData = new Hashtable();
					htData.put("PLANT", plant);
					htData.put("ID", invid);
					arrList = invoiceDAO.getInvoiceHdrById(htData);
					Map m = (Map) arrList.get(0);
					dono = (String) m.get("DONO");

				}
				
				Double orderDiscountFroma = Double.parseDouble(orderdiscount);
				
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));
				
				if(orderdisctype.toString().equalsIgnoreCase("%"))
					orderdiscount=Double.toString((Double.parseDouble(subTotal)*Double.parseDouble(orderdiscount))/100);
				//////////////////////
				
				CustomerCreditnoteDAO custcrdnotedao = new CustomerCreditnoteDAO();
				Hashtable ht = new Hashtable();
				ht.put("ID", TranId);
				ht.put("PLANT", plant);
				List CustcrdnoteHdrList;
				CustcrdnoteHdrList = custcrdnotedao.getCustCreditnoteHdrById(ht);
				Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
				String cstatus = (String) CustcrdnoteHdr.get("CREDIT_STATUS");
				
				creditnoteDetInfoList = new ArrayList<Hashtable<String, String>>();
				Hashtable CreditnoteHdr = new Hashtable();
				CreditnoteHdr.put("PLANT", plant);
				CreditnoteHdr.put("CUSTNO", CustCode);
				CreditnoteHdr.put("CREDITNOTE", Criditnote);
				CreditnoteHdr.put("DONO", dono);
				CreditnoteHdr.put("CREDIT_DATE", Criditdate);
				CreditnoteHdr.put("EMPNO", empno);
				CreditnoteHdr.put("ITEM_RATES", itemRates);
				CreditnoteHdr.put("DISCOUNT", discount);
				CreditnoteHdr.put("DISCOUNT_TYPE", discountType);
				CreditnoteHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				CreditnoteHdr.put("SHIPPINGCOST", shippingCost);
				CreditnoteHdr.put("ADJUSTMENT", adjustment);
				CreditnoteHdr.put("ADJUSTMENT_LABEL", adjustmentname);
				CreditnoteHdr.put("SUB_TOTAL", subTotal);
				CreditnoteHdr.put("TOTAL_AMOUNT", totalAmount);
				CreditnoteHdr.put("CREDIT_STATUS", creditstatus);
				CreditnoteHdr.put("NOTE", note);
				CreditnoteHdr.put("TERMSCONDITIONS", terms);
				CreditnoteHdr.put("CRAT", dateutils.getDateTime());
				CreditnoteHdr.put("CRBY", username);
				CreditnoteHdr.put("INVOICE", invoicenum);
				CreditnoteHdr.put("GINO", gino);
				CreditnoteHdr.put("REFERENCE", reference);
				CreditnoteHdr.put("DONO", dono);
				CreditnoteHdr.put("TAXAMOUNT", taxamount);
				CreditnoteHdr.put("SALES_LOCATION", salesloc);
				CreditnoteHdr.put("TAXTREATMENT",taxtreatment);
				
				CreditnoteHdr.put("CURRENCYUSEQT", currencyuseqt);
				CreditnoteHdr.put("ORDERDISCOUNTTYPE", orderdisctype);
				CreditnoteHdr.put("TAXID", taxid);
				CreditnoteHdr.put("ISDISCOUNTTAX", discstatus);
				CreditnoteHdr.put("ISORDERDISCOUNTTAX", orderdiscstatus);
				CreditnoteHdr.put("OUTBOUD_GST", gst);
				CreditnoteHdr.put("PROJECTID", projectid);
				CreditnoteHdr.put("SHIPPINGID", shippingid);
				CreditnoteHdr.put("SHIPPINGCUSTOMER", shippingcust);
				CreditnoteHdr.put("PAYMENT_TERMS", payment_terms);
				CreditnoteHdr.put("SHIPCONTACTNAME",shipcontactname);
				CreditnoteHdr.put("SHIPDESGINATION",shipdesgination);
				CreditnoteHdr.put("SHIPWORKPHONE",shipworkphone);
				CreditnoteHdr.put("SHIPHPNO",shiphpno);
				CreditnoteHdr.put("SHIPEMAIL",shipemail);
				CreditnoteHdr.put("SHIPCOUNTRY",shipcountry);
				CreditnoteHdr.put("SHIPADDR1",shipaddr1);
				CreditnoteHdr.put("SHIPADDR2",shipaddr2);
				CreditnoteHdr.put("SHIPADDR3",shipaddr3);
				CreditnoteHdr.put("SHIPADDR4",shipaddr4);
				CreditnoteHdr.put("SHIPSTATE",shipstate);
				CreditnoteHdr.put("SHIPZIP",shipzip);
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();
				int creditNoteHdrId = 0;

				CreditnoteHdr.put("ID", TranId);
				CreditnoteHdr.put("UPBY", username);
				
				

				creditNoteHdrId = customerCreditDao.updateCreditNoteHdr(CreditnoteHdr);

				if (creditNoteHdrId > 0) {
					for (int i = 0; i < item.size(); i++) {
						int lnno = i + 1;
						String convDiscount=""; 
						String convCost = String.valueOf((Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
						
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i)) / Double.parseDouble(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));
						
						creditnoteDetInfo = new Hashtable<String, String>();
						creditnoteDetInfo.put("PLANT", plant);
						creditnoteDetInfo.put("LNNO", Integer.toString(lnno));
						creditnoteDetInfo.put("HDRID", TranId);
						creditnoteDetInfo.put("ITEM", (String) item.get(i));
						creditnoteDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						creditnoteDetInfo.put("QTY", (String) qty.get(i));
						creditnoteDetInfo.put("UNITPRICE", (String) convCost);
						creditnoteDetInfo.put("DISCOUNT", convDiscount);
						creditnoteDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						creditnoteDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						creditnoteDetInfo.put("AMOUNT", convAmount);
						creditnoteDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						creditnoteDetInfo.put("CRAT", dateutils.getDateTime());
						creditnoteDetInfo.put("CRBY", username);
						creditnoteDetInfo.put("UPAT", dateutils.getDateTime());
						creditnoteDetInfoList.add(creditnoteDetInfo);
					}
					creditnoteDetInfo.put("UPBY", username);
					isAdded = customerCreditDao.deleteCreditNoteDet(plant, TranId);
					if (isAdded)
						isAdded = customerCreditDao.addMultipleCreditnoteDet(creditnoteDetInfoList, plant);

					int attchSize = creditnoteAttachmentList.size();
					for (int i = 0; i < attchSize; i++) {
						creditnoteAttachment = new Hashtable<String, String>();
						creditnoteAttachment = creditnoteAttachmentList.get(i);
						creditnoteAttachment.put("HDRID", TranId);
						creditnoteAttachmentInfoList.add(creditnoteAttachment);
					}
					if (isAdded) {
						if (creditnoteAttachmentInfoList.size() > 0)
							isAdded = customerCreditDao.addCreditnoteAttachments(creditnoteAttachmentInfoList, plant);
						
						if(!creditstatus.equalsIgnoreCase("Draft"))
						{
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(Criditdate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("CUSTOMERCREDITNOTES");
						journalHead.setTRANSACTION_ID(Criditnote);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						CustMstDAO cusDAO=new CustMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						for(Map CNDet:creditnoteDetInfoList)
						{
							Double quantity=Double.parseDouble(CNDet.get("QTY").toString());
							String netWeight=itemMstDAO.getItemNetWeight(plant, CNDet.get("ITEM").toString());
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
							//journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderdiscount));
							if(!orderdisctype.toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderDiscountFroma.toString())/creditnoteDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderDiscountFroma.toString());
								journalDetail.setDEBITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
							}
							boolean isLoop=false;
							if(journalDetails.size()>0)
							{
								int i=0;
								for(JournalDetail journal:journalDetails) {
									int accountId=journal.getACCOUNT_ID();
									if(accountId==journalDetail.getACCOUNT_ID()) {
										isLoop=true;
										Double sumDetit=journal.getDEBITS()+journalDetail.getDEBITS();
										journalDetail.setDEBITS(sumDetit);
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
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) CustCode);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject cusJson=cusDAO.getCustomerName(plant, (String) CustCode);
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
							journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
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
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else if(planttaxtype.equalsIgnoreCase("GST")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Receivable");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("GST Receivable");
							}else if(planttaxtype.equalsIgnoreCase("VAT")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}
							
							journalDetail_2.setDEBITS(taxAmountFrom);
							journalDetails.add(journalDetail_2);
						}
						
						
						
						if(discount.isEmpty())
						{
							discount="0.00";
						}
						Double discountFrom = Double.parseDouble(discount);
						Double orderDiscountFrom=0.00;
						
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
							journalDetail_3.setCREDITS(discountFrom);
							journalDetails.add(journalDetail_3);
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
								journalDetail_7.setDEBITS(adjustFrom);
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
								journalDetail_7.setCREDITS(adjustFrom);
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
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
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
						}
						}
					}
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_CREDIT_NOTE);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(Criditdate));														
						htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
						String billqty = String.valueOf((String) qty.get(i));
						htMovHis.put(IDBConstants.QTY, billqty);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, Criditnote);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						if(dono.isEmpty())
							dono=reference;
						htMovHis.put("REMARKS",custname+","+dono+","+gino+","+invoicenum+","+note);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, plant);
						htMovChk.put("DIRTYPE", TransactionConstants.EDIT_CREDIT_NOTE);
						htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
						htMovChk.put(IDBConstants.QTY, billqty);
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, Criditnote);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%SALES_CREDIT_NOTE%' ");
						if(!isAdded)	
							isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
				}

				
				
				if(cstatus.equalsIgnoreCase("DRAFT")) {
					ExceptionUtil exceptionUtilstatus=null;
					InvPaymentHeader invoicePaymentHeader = new InvPaymentHeader();
					InvPaymentDetail invoicePaymentDetail = new InvPaymentDetail();
					InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			
					
					invoicePaymentHeader.setPLANT(plant);
					invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
					invoicePaymentHeader.setCUSTNO((String)CustcrdnoteHdr.get("CUSTNO"));
					invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
					invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble("0"));
					//invoicePaymentHeader.setBANK_BRANCH();
					//invoicePaymentHeader.setCHECQUE_NO(item.getString());
					invoicePaymentHeader.setRECEIVE_DATE(dateutils.getDB2UserDate_New(dateutils.getDateTime()));
					invoicePaymentHeader.setRECEIVE_MODE("Credit Note");
					invoicePaymentHeader.setREFERENCE((String)CustcrdnoteHdr.get("CREDITNOTE"));
					invoicePaymentHeader.setNOTE((String)CustcrdnoteHdr.get("NOTE"));
					invoicePaymentHeader.setCURRENCYID(currencyid);
					invoicePaymentHeader.setTRANSACTIONID("");
					
					
					exceptionUtilstatus=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
					  if(exceptionUtilstatus.getStatus()==Result.OK)
					  {

						invoicePaymentDetail.setPLANT(plant);
						invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt("0"));
						invoicePaymentDetail.setDONO("0");
						 int receiveHeaderId=(int) exceptionUtilstatus.getResultData();
						invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
						invoicePaymentDetail.setAMOUNT(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
						invoicePaymentDetail.setLNNO(0);
						invoicePaymentDetail.setTYPE("ADVANCE");
						invoicePaymentDetail.setBALANCE(Double.parseDouble((String)CustcrdnoteHdr.get("TOTAL_AMOUNT")));
						invoicePaymentDetail.setADVANCEFROM("CREDITNOTE");
						invoicePaymentDetail.setCREDITNOTEHDRID(Integer.parseInt(TranId));
						invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble(currencyuseqt));
						invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
						invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
						
						
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT_RECEIVED);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));																			
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(exceptionUtilstatus.getResultData()));
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",CustcrdnoteHdr.get("CREDITNOTE")+","+invoicePaymentHeader.getAMOUNTRECEIVED());
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

					  }
				}
				

				if (isAdded) {

					//TblControlDAO _TblControlDAO = new TblControlDAO();
					//_TblControlDAO.updateLatestSeqNo("CUSTOMER_CREDIT_NOTE", plant, "0");
					DbBean.CommitTran(ut);
					result = "Credit Note Updated successfully";
				} else {
					DbBean.RollbackTran(ut);
					result = "Credit Note not created";
				}
				// response.sendRedirect("jsp/createInvoice.jsp?result="+ result);/* Redirect to
				// Invoice Summary page */
				if(result.equalsIgnoreCase("Credit Note not created"))
					response.sendRedirect("../creditnote/new?result="+ result);
				else
					response.sendRedirect("../creditnote/summary?result="+ result);

			} catch (Exception e) {
				System.out.println(e);
				DbBean.RollbackTran(ut);
				 e.printStackTrace();
				response.sendRedirect("../creditnote/new?result="+ e.toString());
			}

		}
		else if(baction.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			CustomerCreditnoteDAO customerCreditnoteDAO=new CustomerCreditnoteDAO();
			List creditAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				creditAttachment = customerCreditnoteDAO.getCustCrdnoteAttachByPrimId(ht1);
				Map credAttach=(Map)creditAttachment.get(0);
				String filePath=(String) credAttach.get("FilePath");
				String fileType=(String) credAttach.get("FileType");
				String fileName=(String) credAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		else if(baction.equalsIgnoreCase("removeAttachmentById"))
		{
			System.out.println("Remove Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("removeid"));
			CustomerCreditnoteDAO customerCreditnoteDAO=new CustomerCreditnoteDAO();
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				customerCreditnoteDAO.deleteCreditAttachByPrimId(ht1);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}else if (baction.equals("VIEW_CUSTOMER_CREDIT_DETAILS_VIEW")) {	    	  
	        jsonObjectResult = this.getCustomerCreditNote(request);
			response.setContentType("application/json");			
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	       	       
	    }else if (baction.equals("ExportExcelCustomerCreditDetails")) {
			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelCustomerCreditDetails(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=credit_notes_details.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;

	}

	private JSONObject getcreditnoteview(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustomerCreditnoteDAO custCreditnoteDAO = new CustomerCreditnoteDAO();
		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		String decimalZeros = "";
		for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
			decimalZeros += "#";
		}
		DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);

		StrUtils strUtils = new StrUtils();
		String fdate = "", tdate = "", taxby = "";
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
			String INVOICE = StrUtils.fString(request.getParameter("INVOICE"));
			String CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
			String REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;

			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);

			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

			Hashtable ht = new Hashtable();

			if (StrUtils.fString(INVOICE).length() > 0)
				ht.put("INVOICE", INVOICE);
			
			if (StrUtils.fString(CREDITNOTE).length() > 0)
				ht.put("CREDITNOTE", CREDITNOTE);
			
			if (StrUtils.fString(REFERENCE).length() > 0)
				ht.put("REFERENCE", REFERENCE);
			
			if (StrUtils.fString(STATUS).length() > 0)
				ht.put("CREDIT_STATUS", STATUS);

			movQryList = custCreditnoteDAO.getCustomerCreditSummaryView(ht, fdate, tdate, PLANT, CUSTOMER);

			if (movQryList.size() > 0) {
				int iIndex = 0, Index = 0;
				int irow = 0;

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					String result = "";
					Map lineArr = (Map) movQryList.get(iCnt);

					String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#dddddd";
					JSONObject resultJsonInt = new JSONObject();
					String unitCostValue = (String) lineArr.get("TOTAL_AMOUNT");
					String balCostValue = (String) lineArr.get("BALANCE");
					double unitCostVal = "".equals(unitCostValue) ? 0.0f : Double.parseDouble(unitCostValue);
					if (unitCostVal == 0f) {
						unitCostValue = "0.00";
					} else {
						unitCostValue = unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
					
					double balCostVal = "".equals(balCostValue) ? 0.0f : Double.parseDouble(balCostValue);
					if (balCostVal == 0f) {
						balCostValue = "0.00";
					} else {
						balCostValue = balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
					String covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);

					Index = Index + 1;
					resultJsonInt.put("Index", (Index));
					resultJsonInt.put("creditdate", StrUtils.fString((String) lineArr.get("CREDIT_DATE")));
					resultJsonInt.put("creditnote", StrUtils.fString((String) lineArr.get("CREDITNOTE")));
					resultJsonInt.put("ordernumber", StrUtils.fString((String) lineArr.get("DONO")));
					resultJsonInt.put("invoice", StrUtils.fString((String) lineArr.get("INVOICE")));
					resultJsonInt.put("reference", StrUtils.fString((String) lineArr.get("REFERENCE")));
					resultJsonInt.put("custno", StrUtils.fString((String) lineArr.get("CUSTNO")));
					resultJsonInt.put("custname", StrUtils.fString((String) lineArr.get("CNAME")));
					resultJsonInt.put("status", StrUtils.fString((String) lineArr.get("CREDIT_STATUS")));
					String cur = StrUtils.fString((String) lineArr.get("CURRENCYID"));
					if(cur.isEmpty())
               		 cur= curency;
					resultJsonInt.put("custcreditid", StrUtils.fString((String) lineArr.get("ID")));
					resultJsonInt.put("amount", unitCostValue);
					resultJsonInt.put("balancedue", balCostValue);
					resultJsonInt.put("convamount",cur+com.track.util.Numbers.toMillionFormat(Double.parseDouble(covunitCostValue),Integer.parseInt(numberOfDecimal)));
               	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
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

	private JSONObject getEditCustCreditNoteDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustomerCreditnoteDAO custcrdnotedao = new CustomerCreditnoteDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		MasterUtil masterUtil = new MasterUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String TranId = StrUtils.fString(request.getParameter("Id")).trim();

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			String ConvertedUnitCost = "", ConvertedAmount = "",ConvertedDiscount="";
			Hashtable ht = new Hashtable();
			ht.put("ID", TranId);
			ht.put("PLANT", plant);

			ArrayList listQry = custcrdnotedao.getEditCreditnoteDetails(ht, plant);
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();

					double dCost = Double.parseDouble((String) m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);

					double dAmount = Double.parseDouble((String) m.get("AMOUNT"));
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					double dDiscount = Double.parseDouble((String)m.get("ITEM_DISCOUNT"));
					ConvertedDiscount = StrUtils.addZeroes(dDiscount, numberOfDecimal);

					// HDR Details
					resultJsonInt.put("CREDITNOTE", (String) m.get("CREDITNOTE"));
					resultJsonInt.put("ID", (String) m.get("ID"));
					resultJsonInt.put("CREDIT_DATE", (String) m.get("CREDIT_DATE"));
					resultJsonInt.put("INVOICE", (String) m.get("INVOICE"));
					resultJsonInt.put("GINO", (String) m.get("GINO"));
					resultJsonInt.put("CUSTNO", (String) m.get("CUSTNO"));
					resultJsonInt.put("ADJUSTMENT_LABEL", (String) m.get("ADJUSTMENT_LABEL"));
					resultJsonInt.put("EMPNO", (String) m.get("EMPNO"));
					resultJsonInt.put("EMP_NAME", (String) m.get("EMP_NAME"));
					resultJsonInt.put("REFERENCE", (String) m.get("REFERENCE"));
					resultJsonInt.put("CREDIT_STATUS", (String) m.get("CREDIT_STATUS"));
					resultJsonInt.put("TOTAL_AMOUNT", (String) m.get("TOTAL_AMOUNT"));
					resultJsonInt.put("TERMSCONDITIONS", (String) m.get("TERMSCONDITIONS"));
					resultJsonInt.put("NOTE", (String) m.get("NOTE"));
					resultJsonInt.put("ITEM_RATES", (String) m.get("ITEM_RATES"));
					resultJsonInt.put("DISCOUNT", (String) m.get("DISCOUNT"));
					resultJsonInt.put("ORDER_DISCOUNT", (String) m.get("ORDER_DISCOUNT"));
					resultJsonInt.put("DISCOUNT_TYPE", (String) m.get("DISCOUNT_TYPE"));
					resultJsonInt.put("DISCOUNT_ACCOUNT", (String) m.get("DISCOUNT_ACCOUNT"));
					resultJsonInt.put("SHIPPINGCOST", (String) m.get("SHIPPINGCOST"));
					resultJsonInt.put("ADJUSTMENT", (String) m.get("ADJUSTMENT"));
					resultJsonInt.put("ADJUSTMENT_LABEL", (String) m.get("ADJUSTMENT_LABEL"));
					resultJsonInt.put("SUB_TOTAL", (String) m.get("SUB_TOTAL"));
					resultJsonInt.put("ATTACHNOTE_COUNT", (String) m.get("ATTACHNOTE_COUNT"));
					resultJsonInt.put("CUSTOMER", (String) m.get("CNAME"));
					resultJsonInt.put("DONO", (String) m.get("DONO"));
					resultJsonInt.put("SALES_LOCATION", (String) m.get("SALES_LOCATION"));
					resultJsonInt.put("STATE_PREFIX", (String) m.get("STATE_PREFIX"));
					resultJsonInt.put("TAXTREATMENT", (String) m.get("TAXTREATMENT"));
					resultJsonInt.put("CURRENCYID", (String) m.get("CURRENCYID"));
					resultJsonInt.put("DISPLAY", (String) m.get("DISPLAY"));
					resultJsonInt.put("CURRENCYUSEQT", (String) m.get("CURRENCYUSEQT"));
					
					int proid = Integer.valueOf((String)m.get("PROJECTID"));
					String projectname = "";
					if(proid > 0){
						FinProject finProject = finProjectDAO.getFinProjectById(plant, proid);
						projectname = finProject.getPROJECT_NAME();
					}

					resultJsonInt.put("TAXID", (String)m.get("TAXID"));
					resultJsonInt.put("PROJECTID", (String)m.get("PROJECTID"));
					resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
					resultJsonInt.put("ISDISCOUNTTAX", (String)m.get("ISDISCOUNTTAX"));
					resultJsonInt.put("ISORDERDISCOUNTTAX", (String)m.get("ISORDERDISCOUNTTAX"));
					resultJsonInt.put("OUTBOUD_GST", (String)m.get("OUTBOUD_GST"));
					resultJsonInt.put("PROJECTNAME", projectname);
					resultJsonInt.put("SHIPPINGID", (String)m.get("SHIPPINGID"));
					resultJsonInt.put("SHIPPINGCUSTOMER", (String)m.get("SHIPPINGCUSTOMER"));
					resultJsonInt.put("PAYMENT_TERMS", (String)m.get("PAYMENT_TERMS"));
					
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
					
					
					// Item Details

					resultJsonInt.put("DETID", (String) m.get("DETID"));
					resultJsonInt.put("LNNO", (String) m.get("LNNO"));
					resultJsonInt.put("ITEM", (String) m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String) m.get("ITEMDESC"));
					resultJsonInt.put("ACCOUNT_NAME", (String) m.get("ACCOUNT_NAME"));
					resultJsonInt.put("QTY", (String) m.get("QTY"));
					resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String) m.get("ITEM_DISCOUNT_TYPE"));
					resultJsonInt.put("ITEM_DISCOUNT", ConvertedDiscount);
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					resultJsonInt.put("TAX_TYPE", (String) m.get("TAX_TYPE"));
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					resultJsonInt.put("BASECOST", (String) m.get("BASECOST"));
					String catlogpath = StrUtils.fString((String) m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath));

					jsonArray.add(resultJsonInt);
				}
				resultJson.put("creditnote", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO CREDIT NOTE FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject validateCreditnote(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustomerCreditnoteDAO custcrdnotedao = new CustomerCreditnoteDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String creditnote = StrUtils.fString(request.getParameter("CreditNote")).trim();
	
			Hashtable htValues = new Hashtable();
			htValues.put("A.PLANT", plant);
			htValues.put("CREDITNOTE", creditnote);
			Boolean isAdded  = new CustomerCreditnoteDAO().isExisit(htValues,"");		
			if(isAdded)
			{
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("errors", jsonArrayErr);
		} else {
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO CREDIT NOTE FOUND!");
			resultJsonInt.put("ERROR_CODE", "99");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("items", jsonArray);
			resultJson.put("errors", jsonArrayErr);
		}
	} catch (Exception e) {
		resultJson.put("SEARCH_DATA", "");
		JSONObject resultJsonInt = new JSONObject();
		resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
		resultJsonInt.put("ERROR_CODE", "98");
		jsonArrayErr.add(resultJsonInt);
		resultJson.put("ERROR", jsonArrayErr);
	}
	return resultJson;
}
	
	private JSONObject getCustomerCreditNote(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CustomerCreditnoteDAO custCreditnoteDAO = new CustomerCreditnoteDAO();
		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
		String decimalZeros = "";
		for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
			decimalZeros += "#";
		}
		DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);

		StrUtils strUtils = new StrUtils();
		String fdate = "", tdate = "", taxby = "";
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
			String INVOICE = StrUtils.fString(request.getParameter("INVOICE"));
			String CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
			String REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;

			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);

			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

			Hashtable ht = new Hashtable();

			if (StrUtils.fString(INVOICE).length() > 0)
				ht.put("INVOICE", INVOICE);
			
			if (StrUtils.fString(CREDITNOTE).length() > 0)
				ht.put("CREDITNOTE", CREDITNOTE);
			
			if (StrUtils.fString(REFERENCE).length() > 0)
				ht.put("REFERENCE", REFERENCE);
			
			if (StrUtils.fString(STATUS).length() > 0)
				ht.put("CREDIT_STATUS", STATUS);

			movQryList = custCreditnoteDAO.getCustomerCreditNote(ht, fdate, tdate, PLANT, CUSTOMER);

			if (movQryList.size() > 0) {
				int iIndex = 0, Index = 0;
				int irow = 0;

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					String result = "";
					Map lineArr = (Map) movQryList.get(iCnt);

					String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF" : "#dddddd";
					JSONObject resultJsonInt = new JSONObject();
					String unitCostValue = (String) lineArr.get("BASE_TOTAL_AMOUNT");
					String balCostValue = (String) lineArr.get("BASE_BALANCE");
					double unitCostVal = "".equals(unitCostValue) ? 0.0f : Double.parseDouble(unitCostValue);
					if (unitCostVal == 0f) {
						unitCostValue = "0.00";
					} else {
						unitCostValue = unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
					
					double balCostVal = "".equals(balCostValue) ? 0.0f : Double.parseDouble(balCostValue);
					if (balCostVal == 0f) {
						balCostValue = "0.00";
					} else {
						balCostValue = balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
					

					Index = Index + 1;
					resultJsonInt.put("Index", (Index));
					resultJsonInt.put("creditdate", StrUtils.fString((String) lineArr.get("CREDIT_DATE")));
					resultJsonInt.put("creditnote", StrUtils.fString((String) lineArr.get("CREDITNOTE")));
					resultJsonInt.put("ordernumber", StrUtils.fString((String) lineArr.get("DONO")));
					resultJsonInt.put("invoice", StrUtils.fString((String) lineArr.get("INVOICE")));
					resultJsonInt.put("reference", StrUtils.fString((String) lineArr.get("REFERENCE")));
					resultJsonInt.put("custno", StrUtils.fString((String) lineArr.get("CUSTNO")));
					resultJsonInt.put("custname", StrUtils.fString((String) lineArr.get("CNAME")));
					resultJsonInt.put("status", StrUtils.fString((String) lineArr.get("CREDIT_STATUS")));
					String cur = StrUtils.fString((String) lineArr.get("CURRENCYID"));
					if(cur.isEmpty())
               		 cur= curency;
					resultJsonInt.put("custcreditid", StrUtils.fString((String) lineArr.get("ID")));
					resultJsonInt.put("amount",unitCostValue);
					resultJsonInt.put("balancedue",balCostValue);
					/*
					 * resultJsonInt.put("amount", curency + unitCostValue);
					 * resultJsonInt.put("balancedue", curency + balCostValue);
					 */					jsonArray.add(resultJsonInt);

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
	
	private JSONObject getSalesreturnDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		ReturnOrderUtil roUtil = new ReturnOrderUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		InvoiceDAO invoiceDAO = new InvoiceDAO();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		MasterUtil masterUtil = new MasterUtil();
		
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String sorno = StrUtils.fString(request.getParameter("SORETURN")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
			String invoice = StrUtils.fString(request.getParameter("INVOICE")).trim();
			String gino = StrUtils.fString(request.getParameter("GINO")).trim();

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			String ConvertedUnitCost = "", ConvertedAmount = "",ConvertedDiscount="";


			List listQry = roUtil.getSOReturnDetailsBYso(sorno, dono, gino, invoice, plant);
			if (listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map m = (Map) listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();

					double dCost = Double.parseDouble((String) m.get("UNITPRICE"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);

					double dAmount = Double.parseDouble((String) m.get("RETURN_QTY")) * dCost;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					
					
					String famount = ConvertedAmount;
					
					String discount = "0";
					String discount_type = "";
					String qty = "0";
					String cost = "0";
					double discountvalue = 0.0;
					
					Hashtable ht = new Hashtable();
					ht.put("INVOICE", (String)m.get("INVOICE"));
					//ht.put("A.PLANT", plant);
					ht.put("B.ITEM", (String)m.get("ITEM"));
					//ht.put("B.LNNO", (String)m.get("LNNO"));
					
					String query = "SELECT B.DISCOUNT, ISNULL(B.DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,B.QTY,B.UNITPRICE FROM "+plant+"_FININVOICEHDR A JOIN "+plant+"_FININVOICEDET B ON A.ID = B.INVOICEHDRID WHERE A.PLANT='"+plant+"' ";
					List invoiceDetList =  invoiceDAO.selectForReport(query, ht, "");
					
					if(!invoiceDetList.isEmpty()) {
						Map invDet=(Map)invoiceDetList.get(0);
						discount = (String)invDet.get("DISCOUNT");
						discount_type = (String)invDet.get("DISCOUNT_TYPE");
						qty = (String)invDet.get("QTY");
						cost = (String)invDet.get("UNITPRICE");
						if(discount_type.equalsIgnoreCase("%")) {
							discountvalue = Double.parseDouble(discount);
							famount = String.valueOf(dAmount -((dAmount/100)*discountvalue));
						}else {
							discountvalue = (Double.parseDouble(discount)/Double.parseDouble(qty))*Double.parseDouble((String)m.get("RETURN_QTY"));
							famount = String.valueOf(dAmount - discountvalue);
						}
					
					}

					double balqty = Double.parseDouble((String)m.get("RETURN_QTY")) - Double.parseDouble((String)m.get("CREDITNOTE_QTY"));
					// HDR Details
					
				
					resultJsonInt.put("ITEM", (String) m.get("ITEM"));
					resultJsonInt.put("QTY", balqty);
					resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					resultJsonInt.put("DONO", (String) m.get("DONO"));
					resultJsonInt.put("INVOICE", (String) m.get("INVOICE"));
					resultJsonInt.put("ORDERDISCOUNT", (String) m.get("ORDERDISCOUNT"));
					resultJsonInt.put("INVOICEDISCOUNT", (String) m.get("INVOICEDISCOUNT"));
					resultJsonInt.put("INVOICEDISCOUNTTYPE", (String) m.get("INVOICEDISCOUNTTYPE"));
					resultJsonInt.put("INVOICESUB_TOTAL", (String) m.get("INVOICESUB_TOTAL"));
					resultJsonInt.put("INVOICELINEDISCOUNT", (String) m.get("INVOICELINEDISCOUNT"));
					resultJsonInt.put("CUSTNO", (String) m.get("CUSTNO"));
					resultJsonInt.put("CUSTOMER", (String) m.get("CNAME"));
					resultJsonInt.put("NOTE", (String) m.get("NOTE"));
					String catlogpath = StrUtils.fString((String) m.get("CATLOGPATH"));
					resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath));
					
					resultJsonInt.put("ITEM_DISCOUNT", discountvalue);
					resultJsonInt.put("ITEM_DISCOUNT_TYPE", discount_type);
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
					resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
					resultJsonInt.put("ITEMDESC", (String) m.get("ITEMDESC"));
					int proid = Integer.valueOf((String)m.get("PROJECTID"));
					String projectname = "";
					if(proid > 0){
						FinProject finProject = finProjectDAO.getFinProjectById(plant, proid);
						projectname = finProject.getPROJECT_NAME();
					}
					
					resultJsonInt.put("PROJECTID", (String)m.get("PROJECTID"));
					resultJsonInt.put("SHIPPINGID", (String)m.get("SHIPPINGID"));
					resultJsonInt.put("SHIPPINGCUSTOMER", (String)m.get("SHIPPINGCUSTOMER"));
					resultJsonInt.put("PAYMENT_TERMS", (String)m.get("PAYMENT_TERMS"));
					resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
					resultJsonInt.put("ISDISCOUNTTAX", (String)m.get("ISDISCOUNTTAX"));
					resultJsonInt.put("ISORDERDISCOUNTTAX", (String)m.get("ISORDERDISCOUNTTAX"));
					resultJsonInt.put("OUTBOUD_GST", (String)m.get("OUTBOUD_GST"));
					resultJsonInt.put("PROJECTNAME", projectname);
					
					String slocation = (String)m.get("SALES_LOCATION");
					if(slocation.equalsIgnoreCase("0") || slocation.length() == 0) {
						resultJsonInt.put("SALESLOCATION", "");
						resultJsonInt.put("SALESPREFIX", "");
					}else {
						
						ArrayList sprefix =  masterUtil.getSalesLocationByState((String)m.get("SALES_LOCATION"), plant, "");
						Map msprefix=(Map)sprefix.get(0);
						
						resultJsonInt.put("SALESLOCATION", (String)m.get("SALES_LOCATION"));
						resultJsonInt.put("SALESPREFIX", (String)msprefix.get("PREFIX"));
					}
					
					
					
					List curQryList=new ArrayList();
					curQryList = currencyDAO.getCurrencyDetails((String)m.get("CURRENCYID"),plant);
					for(int j =0; j<curQryList.size(); j++) {
						ArrayList arrCurrLine = (ArrayList)curQryList.get(j);
						resultJsonInt.put("DISPLAY", StrUtils.fString((String)arrCurrLine.get(2)));
						
					    }

					jsonArray.add(resultJsonInt);
				}
				resultJson.put("creditnote", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO CREDIT NOTE FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private HSSFWorkbook writeToExcelCustomerCreditDetails(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		StrUtils strUtils = new StrUtils();
		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        CustomerCreditnoteDAO custCreditnoteDAO = new CustomerCreditnoteDAO();
		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "", custcode = "";
		int SheetId =1;
		try {
			String PLANT = StrUtils.fString(request.getParameter("plant"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CUST_CODE"));
			String INVOICE = StrUtils.fString(request.getParameter("invoice"));
			String CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
			String REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;

			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);

			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

			Hashtable ht = new Hashtable();

			if (StrUtils.fString(INVOICE).length() > 0)
				ht.put("INVOICE", INVOICE);
			
			if (StrUtils.fString(CREDITNOTE).length() > 0)
				ht.put("CREDITNOTE", CREDITNOTE);
			
			if (StrUtils.fString(REFERENCE).length() > 0)
				ht.put("REFERENCE", REFERENCE);
			
			if (StrUtils.fString(STATUS).length() > 0)
				ht.put("CREDIT_STATUS", STATUS);

			movQryList = custCreditnoteDAO.getCustomerCreditNote(ht, fdate, tdate, PLANT, CUSTOMER);
	           
	           Boolean workSheetCreated = true;
	           if (movQryList.size() >= 0) {
	        	   HSSFSheet sheet = null;
					HSSFCellStyle styleHeader = null;
					HSSFCellStyle dataStyle = null;
					HSSFCellStyle dataStyleSpecial = null;
					HSSFCellStyle CompHeader = null;
					dataStyle = createDataStyle(wb);
					dataStyleSpecial = createDataStyle(wb);
					sheet = wb.createSheet("Sheet"+SheetId);
					styleHeader = createStyleHeader(wb);
					CompHeader = createCompStyleHeader(wb);
					sheet = this.createWidthExpensesSummary(sheet, "");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "");
					sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
					if(TO_DATE.equalsIgnoreCase(""))
						TO_DATE = curDate;
					sheet = this.createHeaderDate(sheet,CompHeader,"",FROM_DATE,TO_DATE,PLANT);
					CreationHelper createHelper = wb.getCreationHelper();

					int index = 3;
					
					double orderPriceSubTot = 0, issPriceSubTot = 0, unitcost = 0, Cost = 0, CostwTax = 0;
					float gstpercentage = 0;
					String strDiffQty = "", deliverydateandtime = "";
					DecimalFormat decformat = new DecimalFormat("#,##0.00");
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String result="";
                        custcode =(String)lineArr.get("custname");
                        String pono = (String)lineArr.get("pono");   
                        JSONObject resultJsonInt = new JSONObject();
               			String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
               			String balCostValue = (String)lineArr.get("BASE_BALANCE");
               			double unitCostVal ="".equals(unitCostValue) ? 0.0f :  Double.parseDouble(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                        
                        double balCostVal ="".equals(balCostValue) ? 0.0f :  Double.parseDouble(balCostValue);
                        if(balCostVal==0f){
                        	balCostValue="0.00";
                        }else{
                        	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        balCostValue =StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CREDIT_STATUS"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("creditdate"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CREDITNOTE"))));
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
				sheet.setColumnWidth(i++, 3000);
				sheet.setColumnWidth(i++, 3000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 5000);
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
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Credit Note"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Balance"));
				cell.setCellStyle(styleHeader);
			
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