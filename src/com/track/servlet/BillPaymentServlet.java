package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.ExpensesUtil;
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

@WebServlet("/BillPaymentServlet")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BillPaymentServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.BillPaymentServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.BillPaymentServlet_PRINTPLANTMASTERINFO;
	String action = "";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		if (action.equalsIgnoreCase("Save_t")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTHDR */
			String vendno = "", amountpaid = "", paymentDate = "", paymentMode = "", paidThrough = "", reference = "",
					paytype = "", ponoadv = "", newadv = "", projectid = "", amountUfp = "", amountRefunded = "",
					bankCharge = "", note = "", uuid = "", paidpdcstatus = "", currencyid = "", currencyuseqt = "0",jdesc="";// bankBranch
																													// =
																													// "",
																													// checqueNo
																													// =
																													// "",

			/* FINPAYMENTDET */
			String payHdrId = "";
			List billHdrId = new ArrayList(), amount = new ArrayList(), pono = new ArrayList(), type = new ArrayList(),
					billAmount = new ArrayList();
			List ExpHdrId = new ArrayList(), Expamount = new ArrayList(), ExpPono = new ArrayList(),
					ExpType = new ArrayList(), ExpenseAmount = new ArrayList();
			List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),
					chequeamount = new ArrayList(), pdcstatus = new ArrayList();
			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0, pdccount = 0;

			UserTransaction ut = null;
			Hashtable<String, String> paymentAttachment = null;
			Hashtable<String, String> paymentDetInfo = null;
			Hashtable<String, String> billpdcInfo = null;
			List<Hashtable<String, String>> billpdcInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentList = null;
			List<Hashtable<String, String>> paymentDetInfoList = null;
			List<Hashtable<String, String>> ExppaymentDetInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentInfoList = null;
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
//			BillPaymentDAO billPaymentDao = new BillPaymentDAO();

			BillDAO billDao = new BillDAO();
			boolean isAdded = false;
			String result = "";
			int billCount = 0, amountCount = 0, ponoCount = 0, typeCount = 0, billamount = 0;
			int ExpIdCount = 0, ExpamountCount = 0, ExpponoCount = 0, ExptypeCount = 0, ExpenseamountCount = 0;
//			FileItem fileUploadItem = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					paymentAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							/* FINPAYMENTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								vendno = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
								// if (fileItem.getFieldName().equalsIgnoreCase("conv_amount_paid_Curr")) {
								amountpaid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
								paymentDate = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
								paymentMode = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
								paidThrough = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
								paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
								amountUfp = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
								amountRefunded = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) {
								bankCharge = StrUtils.fString(fileItem.getString()).trim();
							}

							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("checqueNo")) { checqueNo =
							 * StrUtils.fString(fileItem.getString()).trim(); } if
							 * (fileItem.getFieldName().equalsIgnoreCase("cheque_date")) { chequedate =
							 * StrUtils.fString(fileItem.getString()).trim(); }
							 */
							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paytype")) {
								paytype = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ponoadv")) {
								ponoadv = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("newadv")) {
								newadv = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("uuid")) {
								uuid = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									projectid = StrUtils.fString(fileItem.getString()).trim();
								}
							}

							/* FINPAYMENTDET */
							if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
								billHdrId.add(billCount, StrUtils.fString(fileItem.getString()).trim());
								billCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
								amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
								amountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
								pono.add(ponoCount, StrUtils.fString(fileItem.getString()).trim());
								ponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("type")) {
								type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
								typeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billamount")) {
								billAmount.add(billamount, StrUtils.fString(fileItem.getString()).trim());
								billamount++;
							}

							/* FINPAYMENTDETEXP */
							if (fileItem.getFieldName().equalsIgnoreCase("ExpHdrId")) {
								ExpHdrId.add(ExpIdCount, StrUtils.fString(fileItem.getString()).trim());
								ExpIdCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("Expamount")) {
								Expamount.add(ExpamountCount, StrUtils.fString(fileItem.getString()).trim());
								ExpamountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpPono")) {
								ExpPono.add(ExpponoCount, StrUtils.fString(fileItem.getString()).trim());
								ExpponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpType")) {
								ExpType.add(ExptypeCount, StrUtils.fString(fileItem.getString()).trim());
								ExptypeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpenseAmount")) {
								ExpenseAmount.add(ExpenseamountCount, StrUtils.fString(fileItem.getString()).trim());
								ExpenseamountCount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcstatus")) {
								pdcstatus.add(pdccount, StrUtils.fString(fileItem.getString()).trim());
								pdccount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
								bankcode.add(bankcodeCount, StrUtils.fString(fileItem.getString()).trim());
								bankcodeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
								chequeno.add(chequenoCount, StrUtils.fString(fileItem.getString()).trim());
								chequenoCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
								chedate.add(chedateCount, StrUtils.fString(fileItem.getString()).trim());
								chedateCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
								chequeamount.add(chequeamountCount, StrUtils.fString(fileItem.getString()).trim());
								chequeamountCount++;
							}
						}
					}
					paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
					ExppaymentDetInfoList = new ArrayList<Hashtable<String, String>>();
					paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
					Hashtable paymentHdr = new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", vendno);
					paymentHdr.put("AMOUNTPAID", amountpaid);
					paymentHdr.put("PAYMENT_DATE", paymentDate);
					paymentHdr.put("PAYMENT_MODE", paymentMode);
					paymentHdr.put("PAID_THROUGH", paidThrough);
					paymentHdr.put("REFERENCE", reference);
					// paymentHdr.put("BANK_BRANCH", bankBranch);
					paymentHdr.put("AMOUNTUFP", amountUfp);
					paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
					paymentHdr.put("BANK_CHARGE", bankCharge);
					// paymentHdr.put("CHECQUE_NO", checqueNo);
					// paymentHdr.put("CHEQUE_DATE", chequedate);
					paymentHdr.put("NOTE", note);
					paymentHdr.put("CRAT", DateUtils.getDateTime());
					paymentHdr.put("CRBY", username);
					paymentHdr.put("UPAT", DateUtils.getDateTime());
					paymentHdr.put("CREDITAPPLYKEY", uuid);
					paymentHdr.put("ISPDCPROCESS", "0");
					paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
					paymentHdr.put("PROJECTID", projectid);
					if (paidpdcstatus.equalsIgnoreCase("1")) {
						for (int i = 0; i < bankcode.size(); i++) {
							if (pdcstatus.get(i).equals("1")) {
								paymentHdr.put("ISPDCPROCESS", "1");
							} else {
								paymentHdr.put("ISPDCPROCESS", "0");
							}
						}

					}
					/* Get Transaction object */
					ut = DbBean.getUserTranaction();
					/* Begin Transaction */
					ut.begin();

					Double totalBillAmount = 0.00;
					for (int j = 0; j < amountCount; j++) {
						totalBillAmount += Double.parseDouble((String) amount.get(j));
					}

					int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
					payHdrId = Integer.toString(paymentHdrId);

					double pdaamountiff = 0.0;
					if (paymentHdrId > 0) {
						billpdcInfoList = new ArrayList<Hashtable<String, String>>();
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							for (int i = 0; i < bankcode.size(); i++) {
								billpdcInfo = new Hashtable<String, String>();
								billpdcInfo.put("PLANT", plant);
								billpdcInfo.put("VENDNO", vendno);
								billpdcInfo.put("PAYMENTID", payHdrId);
								billpdcInfo.put("PAYMENT_DATE", paymentDate);
								billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
								billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
								billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
								billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
								billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
								billpdcInfo.put("CRAT", DateUtils.getDateTime());
								billpdcInfo.put("CRBY", username);
								if (pdcstatus.get(i).equals("1")) {
									billpdcInfo.put("STATUS", "NOT PROCESSED");
									pdaamountiff = pdaamountiff + Double.valueOf((String) chequeamount.get(i));
								} else {
									billpdcInfo.put("STATUS", "NOT APPLICABLE");
								}

								billpdcInfoList.add(billpdcInfo);
							}
							billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
						}
					}

					/*
					 * if(newadv.equalsIgnoreCase("NEW")) { double balceadd =
					 * Double.parseDouble(amountpaid) - totalBillAmount; String sbalceadd =
					 * String.valueOf(balceadd); paymentDetInfo = new Hashtable<String, String>();
					 * paymentDetInfo.put("PLANT", plant); paymentDetInfo.put("LNNO", "0");
					 * paymentDetInfo.put("AMOUNT", amountpaid); paymentDetInfo.put("BALANCE",
					 * sbalceadd); paymentDetInfo.put("PONO", ponoadv);
					 * paymentDetInfo.put("PAYHDRID", payHdrId); paymentDetInfo.put("BILLHDRID",
					 * "0"); paymentDetInfo.put("TYPE", paytype); paymentDetInfo.put("CRAT",
					 * DateUtils.getDateTime()); paymentDetInfo.put("CRBY", username);
					 * paymentDetInfo.put("UPAT", DateUtils.getDateTime());
					 * paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
					 * paymentDetInfoList.add(paymentDetInfo); }
					 */
					int explno = 0;
					if (paymentHdrId > 0) {
						if (billHdrId.size() > 0) {

							if (totalBillAmount > 0) {
								if (newadv.equalsIgnoreCase("NEW")) {
									double balceadd = Double.parseDouble(amountpaid) - totalBillAmount;
									String sbalceadd = String.valueOf(balceadd - pdaamountiff);
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									paymentDetInfo.put("LNNO", "0");
									paymentDetInfo.put("AMOUNT", amountpaid);
									paymentDetInfo.put("BALANCE", sbalceadd);
									paymentDetInfo.put("PONO", ponoadv);
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", "0");
									paymentDetInfo.put("EXPHDRID", "0");
									paymentDetInfo.put("TYPE", paytype);
									paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									paymentDetInfo.put("CRBY", username);
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfoList.add(paymentDetInfo);
								}

								for (int i = 0; i < billHdrId.size(); i++) {
									int lnno = i + 1;
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
										paymentDetInfo.put("BALANCE", detbal);
										paymentDetInfo.put("PONO", (String) pono.get(i));
										paymentDetInfo.put("PAYHDRID", payHdrId);
										paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
										paymentDetInfo.put("EXPHDRID", "0");
										paymentDetInfo.put("TYPE", (String) type.get(i));
										paymentDetInfo.put("CRAT", DateUtils.getDateTime());
										paymentDetInfo.put("CRBY", username);
										paymentDetInfo.put("UPAT", DateUtils.getDateTime());
										paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										paymentDetInfoList.add(paymentDetInfo);
									} else {

										double amocheck = Double.parseDouble((String) amount.get(i));

										if (amocheck > 0) {
											paymentDetInfo.put("LNNO", Integer.toString(lnno));
											paymentDetInfo.put("AMOUNT", (String) amount.get(i));
											paymentDetInfo.put("BALANCE", "0");
											paymentDetInfo.put("PONO", (String) pono.get(i));
											paymentDetInfo.put("PAYHDRID", payHdrId);
											paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
											paymentDetInfo.put("EXPHDRID", "0");
											paymentDetInfo.put("TYPE", (String) type.get(i));
											paymentDetInfo.put("CRAT", DateUtils.getDateTime());
											paymentDetInfo.put("CRBY", username);
											paymentDetInfo.put("UPAT", DateUtils.getDateTime());
											paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
											paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
											paymentDetInfoList.add(paymentDetInfo);
											explno = lnno;
										}
									}

								}

							} else {
								paymentDetInfo = new Hashtable<String, String>();
								paymentDetInfo.put("PLANT", plant);
								paymentDetInfo.put("LNNO", "0");
								paymentDetInfo.put("AMOUNT", amountpaid);
								String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
								paymentDetInfo.put("BALANCE", detbal);
								paymentDetInfo.put("PONO", "");
								paymentDetInfo.put("PAYHDRID", payHdrId);
								paymentDetInfo.put("BILLHDRID", "0");
								paymentDetInfo.put("EXPHDRID", "0");
								paymentDetInfo.put("TYPE", "ADVANCE");
								paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								paymentDetInfo.put("CRAT", DateUtils.getDateTime());
								paymentDetInfo.put("CRBY", username);
								paymentDetInfo.put("UPAT", DateUtils.getDateTime());
								paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
								paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								paymentDetInfoList.add(paymentDetInfo);
							}

						} else {
							/*
							 * paymentDetInfo = new Hashtable<String, String>(); paymentDetInfo.put("PLANT",
							 * plant); paymentDetInfo.put("LNNO", "0"); paymentDetInfo.put("AMOUNT",
							 * amountpaid); String detbal=
							 * String.valueOf(Double.parseDouble(amountpaid)-pdaamountiff);
							 * paymentDetInfo.put("BALANCE", detbal); paymentDetInfo.put("PONO", "");
							 * paymentDetInfo.put("PAYHDRID", payHdrId); paymentDetInfo.put("BILLHDRID",
							 * "0"); paymentDetInfo.put("EXPHDRID", "0"); paymentDetInfo.put("TYPE",
							 * "ADVANCE"); paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
							 * paymentDetInfo.put("CRAT", DateUtils.getDateTime());
							 * paymentDetInfo.put("CRBY", username); paymentDetInfo.put("UPAT",
							 * DateUtils.getDateTime()); paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
							 * paymentDetInfoList.add(paymentDetInfo);
							 */
						}

						for (int i = 0; i < ExpHdrId.size(); i++) {
							explno = explno + 1;
							paymentDetInfo = new Hashtable<String, String>();
							paymentDetInfo.put("PLANT", plant);

							double amocheck = Double.parseDouble((String) Expamount.get(i));

							if (amocheck > 0) {
								paymentDetInfo.put("LNNO", Integer.toString(explno));
								paymentDetInfo.put("AMOUNT", (String) Expamount.get(i));
								paymentDetInfo.put("BALANCE", "0");
								paymentDetInfo.put("PONO", (String) ExpPono.get(i));
								paymentDetInfo.put("PAYHDRID", payHdrId);
								paymentDetInfo.put("BILLHDRID", "0");
								paymentDetInfo.put("EXPHDRID", (String) ExpHdrId.get(i));
								paymentDetInfo.put("TYPE", (String) ExpType.get(i));
								paymentDetInfo.put("CRAT", DateUtils.getDateTime());
								paymentDetInfo.put("CRBY", username);
								paymentDetInfo.put("UPAT", DateUtils.getDateTime());
								paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
								paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								ExppaymentDetInfoList.add(paymentDetInfo);
							}
						}

						if (paymentDetInfoList.size() > 0) {
							isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
							if (isAdded) {
								if (ExppaymentDetInfoList.size() > 0) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
								}
							}
						} else {
							if (ExppaymentDetInfoList.size() > 0) {
								isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
							}
						}

						while (fileIterator.hasNext()) {
							FileItem fileItem = (FileItem) fileIterator.next();
							if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
								String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
								String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment.put("PLANT", plant);
								paymentAttachment.put("FILETYPE", fileItem.getContentType());
								paymentAttachment.put("FILENAME", fileName);
								paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								paymentAttachment.put("FILEPATH", fileLocation);
								paymentAttachment.put("CRAT", DateUtils.getDateTime());
								paymentAttachment.put("CRBY", username);
								paymentAttachmentList.add(paymentAttachment);
							}
						}
						int attchSize = paymentAttachmentList.size();
						for (int i = 0; i < attchSize; i++) {
							paymentAttachment = new Hashtable<String, String>();
							paymentAttachment = paymentAttachmentList.get(i);
							paymentAttachment.put("PAYHDRID", payHdrId);
							paymentAttachmentInfoList.add(paymentAttachment);
						}

						if (isAdded) {
							if (paymentAttachmentInfoList.size() > 0)
								isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

							String curency = StrUtils
									.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
							// Journal Entry
							JournalHeader journalHead = new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(paymentDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
							journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
							journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
							journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							List<JournalDetail> journalDetails = new ArrayList<>();
							CoaDAO coaDAO = new CoaDAO();
							VendMstDAO vendorDAO = new VendMstDAO();
							
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
								coaJson1 = coaDAO.getCOAByName(plant,
										vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
								jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
								
							}

							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME("PDC issued");
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									} else {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										// journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									}
								}
							} else {
								JournalDetail journalDetail = new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
								System.out.println("Json" + coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(paidThrough);
								journalDetail.setDESCRIPTION(jdesc+"-"+reference);
								journalDetail.setCREDITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail);
							}

							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);							
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

							} else {
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if (coaJson1.getString("account_name") != null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								} else {
									journalDetail_1.setACCOUNT_NAME((String) vendno);
								}
								
								journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
								journalDetail_1.setDEBITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail_1);
							}
							if (!bankCharge.isEmpty()) {
								Double bankChargeFrom = Double.parseDouble(bankCharge);
								if (bankChargeFrom > 0) {
									JournalDetail journalDetail_2 = new JournalDetail();
									journalDetail_2.setPLANT(plant);
									journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
									JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
									if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

									} else {
										journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail_2.setACCOUNT_NAME("Bank charges");
										journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
										journalDetails.add(journalDetail_2);
									}
								}
							}

							Journal journal = new Journal();
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);
							JournalService journalService = new JournalEntry();
							journalService.addJournal(journal, username);
							Hashtable jhtMovHis = new Hashtable();
							jhtMovHis.put(IDBConstants.PLANT, plant);
							jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
							jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							jhtMovHis.put(IDBConstants.ITEM, "");
							jhtMovHis.put(IDBConstants.QTY, "0.0");
							jhtMovHis.put("RECID", "");
							jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE()
									+ " " + journal.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis.put(IDBConstants.CREATED_BY, username);
							jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis.put("REMARKS", "");
							movHisDao.insertIntoMovHis(jhtMovHis);
						}
						if (isAdded) {
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
							// htMovHis.put(IDBConstants.ITEM, paymentMode);
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS", reference);
							isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}

						if (isAdded) {
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {

									Hashtable htMovHis = new Hashtable();
									htMovHis.clear();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS);
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
									htMovHis.put(IDBConstants.CREATED_BY, username);
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									if (pdcstatus.get(i).equals("1")) {
										htMovHis.put("REMARKS", "PDC-" + vendno + "," + bankcode.get(i) + ","
												+ chequeno.get(i) + "," + chedate.get(i) + "," + chequeamount.get(i));
									} else {
										htMovHis.put("REMARKS", "CDC-" + vendno + "," + bankcode.get(i) + ","
												+ chequeno.get(i) + "," + chedate.get(i) + "," + chequeamount.get(i));
									}

									isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}
							}
						}
					}
					if (isAdded) {
						for (int i = 0; i < billHdrId.size(); i++) {
							double billamountinit = Double.parseDouble((String) billAmount.get(i));
							double billamountpaid = Double.parseDouble((String) amount.get(i));
							String query = "";
							if (billamountpaid > 0.00) {
								if (billamountpaid < billamountinit) {
									query = " SET BILL_STATUS='Partially Paid'";
								} else if (billamountpaid == billamountinit) {
									query = " SET BILL_STATUS='Paid'";
								}

								String upid = (String) billHdrId.get(i);

								Hashtable htCondition = new Hashtable();
								htCondition.put("ID", upid);
								htCondition.put("PLANT", plant);
								if (!upid.equals(""))
									isAdded = billDao.updateBillHdr(query, htCondition, "");
							}
						}
					}
					if (isAdded) {
						DbBean.CommitTran(ut);
						result = "Payment made successfully";
					} else {
						DbBean.RollbackTran(ut);
						result = "Coudn't process payment";
					}
					if (result.equalsIgnoreCase("Coudn't process payment")) {
						if (((String) type.get(0)).equalsIgnoreCase("ADVANCE"))
							response.sendRedirect("../bill/record?type=ADVANCE&pono=" + pono + "&result=" + result);
						else
							response.sendRedirect("../banking/createbillpay?result=" + result);
					} else
						response.sendRedirect("../banking/billpaysummary?result=" + result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/createbillpay?result=" + e.toString());
			}
		} else if (action.equalsIgnoreCase("Edit")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTHDR */
			String vendno = "", amountpaid = "", paymentDate = "", paymentMode = "", paidThrough = "", reference = "",
					currencyuseqt = "0", amountUfp = "", amountRefunded = "", bankCharge = "", note = "",
					paidpdcstatus = "", projectid = "",jdesc="";// bankBranch = "", checqueNo = "", chequedate="",currencyid="",

			/* FINPAYMENTDET */
			String payHdrId = "";
			List billHdrId = new ArrayList(), amount = new ArrayList(), pono = new ArrayList(), type = new ArrayList(),
					billAmount = new ArrayList(), paymentDetId = new ArrayList();

			List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),
					chequeamount = new ArrayList(), pdcstatus = new ArrayList(), chequestatus = new ArrayList(),
					pdcid = new ArrayList();

			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0, pdccount = 0,
					checkstcount = 0, pdcidcount = 0;

			UserTransaction ut = null;
			Hashtable<String, String> paymentAttachment = null;
			Hashtable<String, String> paymentDetInfo = null;
			List<Hashtable<String, String>> paymentAttachmentList = null;
			List<Hashtable<String, String>> paymentDetInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentInfoList = null;
			Hashtable<String, String> billpdcInfo = null;
			List<Hashtable<String, String>> billpdcInfoList = null;
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
			BillPaymentDAO billPaymentDao = new BillPaymentDAO();
			BillDAO billDao = new BillDAO();
			boolean isAdded = false;
			String result = "";
			String paymentHeaderId = "";
			int billCount = 0, amountCount = 0, ponoCount = 0, typeCount = 0, billamount = 0, paymentdetCount = 0;
//			FileItem fileUploadItem = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					paymentAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							/* FINPAYMENTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								vendno = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
								// if (fileItem.getFieldName().equalsIgnoreCase("conv_amount_paid_Curr")) {
								amountpaid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
								paymentDate = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
								paymentMode = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
								paidThrough = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}
//							if (fileItem.getFieldName().equalsIgnoreCase("bank_branch")) {
//								bankBranch = StrUtils.fString(fileItem.getString()).trim();
//							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
								amountUfp = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
								amountRefunded = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) {
								bankCharge = StrUtils.fString(fileItem.getString()).trim();
							}
//							if (fileItem.getFieldName().equalsIgnoreCase("checqueNo")) {
//								checqueNo = StrUtils.fString(fileItem.getString()).trim();
//							}
//							if (fileItem.getFieldName().equalsIgnoreCase("cheque_date")) {
//								chequedate = StrUtils.fString(fileItem.getString()).trim();
//							}
							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paymenthdrid")) {
								paymentHeaderId = StrUtils.fString(fileItem.getString()).trim();
							}
//							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
//								currencyid=StrUtils.fString(fileItem.getString()).trim();
//							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									projectid = StrUtils.fString(fileItem.getString()).trim();
								}
							}

							/* FINPAYMENTDET */
							if (fileItem.getFieldName().equalsIgnoreCase("paymentdetid")) {
								paymentDetId.add(paymentdetCount, StrUtils.fString(fileItem.getString()).trim());
								paymentdetCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
								billHdrId.add(billCount, StrUtils.fString(fileItem.getString()).trim());
								billCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
								amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
								amountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
								pono.add(ponoCount, StrUtils.fString(fileItem.getString()).trim());
								ponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("type")) {
								type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
								typeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billamount")) {
								billAmount.add(billamount, StrUtils.fString(fileItem.getString()).trim());
								billamount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
								paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("chequestatus")) {
								chequestatus.add(checkstcount, StrUtils.fString(fileItem.getString()).trim());
								checkstcount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcid")) {
								pdcid.add(pdcidcount, StrUtils.fString(fileItem.getString()).trim());
								pdcidcount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcstatus")) {
								pdcstatus.add(pdccount, StrUtils.fString(fileItem.getString()).trim());
								pdccount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
								bankcode.add(bankcodeCount, StrUtils.fString(fileItem.getString()).trim());
								bankcodeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
								chequeno.add(chequenoCount, StrUtils.fString(fileItem.getString()).trim());
								chequenoCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
								chedate.add(chedateCount, StrUtils.fString(fileItem.getString()).trim());
								chedateCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
								chequeamount.add(chequeamountCount, StrUtils.fString(fileItem.getString()).trim());
								chequeamountCount++;
							}
						}

					}

					paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
					paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
					Hashtable paymentHdr = new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", vendno);
					paymentHdr.put("AMOUNTPAID", amountpaid);
					paymentHdr.put("PAYMENT_DATE", paymentDate);
					paymentHdr.put("PAYMENT_MODE", paymentMode);
					paymentHdr.put("PAID_THROUGH", paidThrough);
					paymentHdr.put("REFERENCE", reference);
					// paymentHdr.put("BANK_BRANCH", bankBranch);
					paymentHdr.put("AMOUNTUFP", amountUfp);
					paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
					paymentHdr.put("BANK_CHARGE", bankCharge);
					// paymentHdr.put("CHECQUE_NO", checqueNo);
					// paymentHdr.put("CHEQUE_DATE", chequedate);
					paymentHdr.put("NOTE", note);
					paymentHdr.put("UPBY", username);
					paymentHdr.put("UPAT", DateUtils.getDateTime());
					paymentHdr.put("ISPDCPROCESS", "0");
					paymentHdr.put("PROJECTID", projectid);
					if (paidpdcstatus.equalsIgnoreCase("1")) {
						for (int i = 0; i < bankcode.size(); i++) {
							String chequecheck = (String) chequestatus.get(i);
							if (!chequecheck.equalsIgnoreCase("PROCESSED")) {
								if (pdcstatus.get(i).equals("1")) {
									paymentHdr.put("ISPDCPROCESS", "1");
								} else {
									paymentHdr.put("ISPDCPROCESS", "0");
								}
							}
						}
					}
					/* Get Transaction object */
					ut = DbBean.getUserTranaction();
					/* Begin Transaction */
					ut.begin();

					int paymentHdrId = billPaymentUtil.updatePaymentHdr(paymentHeaderId, paymentHdr, plant);
					payHdrId = Integer.toString(paymentHdrId);

					double pdaamountiff = 0.0;
					if (paymentHdrId > 0) {
						billpdcInfoList = new ArrayList<Hashtable<String, String>>();
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							billPaymentDao.deletepdcbypayid(plant, paymentHeaderId);
							for (int i = 0; i < bankcode.size(); i++) {
//								String pdcidcheck = (String) pdcid.get(i);
								String chequecheck = (String) chequestatus.get(i);
								if (!chequecheck.equalsIgnoreCase("PROCESSED")) {
									/* if(pdcidcheck.equalsIgnoreCase("0")) { */
									billpdcInfo = new Hashtable<String, String>();
									billpdcInfo.put("PLANT", plant);
									billpdcInfo.put("VENDNO", vendno);
									billpdcInfo.put("PAYMENTID", paymentHeaderId);
									billpdcInfo.put("PAYMENT_DATE", paymentDate);
									billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
									billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
									billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
									billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
									billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
									billpdcInfo.put("CRAT", DateUtils.getDateTime());
									billpdcInfo.put("CRBY", username);
									if (pdcstatus.get(i).equals("1")) {
										billpdcInfo.put("STATUS", "NOT PROCESSED");
										pdaamountiff = pdaamountiff + Double.valueOf((String) chequeamount.get(i));
									} else {
										billpdcInfo.put("STATUS", "NOT APPLICABLE");
									}
									billpdcInfoList.add(billpdcInfo);
									/*
									 * }else { String querypdc="BANK_BRANCH='"+(String)
									 * bankcode.get(i)+"',CHEQUE_DATE='"+(String)
									 * chedate.get(i)+"',CHEQUE_AMOUNT='"+(String) chequeamount.get(i)+"'," +
									 * "ISPDC='"+(String) pdcstatus.get(i)+"',CHECQUE_NO='"+(String)
									 * chequeno.get(i)+"',STATUS='"+chequecheck+"'," +
									 * ""+"UPBY='"+username+"',"+"UPAT='"+DateUtils.getDateTime()+"'";
									 * billPaymentDao.updatePaymentpdc(pdcidcheck, querypdc, plant);
									 * 
									 * }
									 */
								}
							}
							billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
						} else {
							billPaymentDao.deletepdcbypayid(plant, paymentHeaderId);
						}

					}

					if (paymentHdrId > 0) {
						Hashtable ht1 = new Hashtable();
						ht1.put("PAYHDRID", String.valueOf(paymentHeaderId));
						ht1.put("PLANT", plant);

						List paymentDetadvancelist = billPaymentDao.getBillPaymentDetListByadvance(ht1);
						if (paymentDetadvancelist.size() > 0) {
							Map paymentDetadvance = (Map) paymentDetadvancelist.get(0);
							double diffdet = 0.0;

							for (int i = 0; i < amount.size(); i++) {
								String detamountst = (String) amount.get(i);
								diffdet = diffdet + ("".equals(detamountst) ? 0.0d : Double.parseDouble(detamountst));
							}

							double detbalnced = ("".equals(amountpaid) ? 0.0d : Double.parseDouble(amountpaid))
									- diffdet;

							detbalnced = detbalnced - pdaamountiff;

							String queryad = "SET AMOUNT='" + amountpaid + "'," + "BALANCE='" + detbalnced + "',"
									+ "CURRENCYUSEQT='" + currencyuseqt + "'," + "UPBY='" + username + "'," + "UPAT='"
									+ DateUtils.getDateTime() + "'";
							Hashtable htCondition1 = new Hashtable();
							htCondition1.put("PLANT", plant);
							htCondition1.put("PAYHDRID", String.valueOf(paymentHeaderId));
							htCondition1.put("ID", (String) paymentDetadvance.get("ID"));
							isAdded = billPaymentUtil.updatePaymentDet(queryad, htCondition1, "");

						}

						/* <--check--> */
						List paymentDetList = billPaymentUtil.getPaymentDetList(ht1);
						if (billHdrId.size() > 0) {
							Double totalBillAmount = 0.00;
							for (int j = 0; j < amountCount; j++) {
								totalBillAmount += Double.parseDouble((String) amount.get(j));
							}
							if (totalBillAmount > 0) {
								for (int i = 0; i < billHdrId.size(); i++) {
									int lnno = i + 1;
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
										paymentDetInfo.put("BALANCE", detbal);
									} else {
										paymentDetInfo.put("LNNO", Integer.toString(lnno));
										paymentDetInfo.put("AMOUNT", (String) amount.get(i));
										paymentDetInfo.put("BALANCE", "0");
									}
									paymentDetInfo.put("PONO", (String) pono.get(i));
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfo.put("TYPE", (String) type.get(i));
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("UPBY", username);
									paymentDetInfoList.add(paymentDetInfo);
									if (paymentDetList.size() > 0) {
										String query = "SET AMOUNT='" + (String) amount.get(i) + "'," + "UPBY='"
												+ username + "'," + "UPAT='" + DateUtils.getDateTime() + "'";
										Hashtable htCondition = new Hashtable();
										htCondition.put("PLANT", plant);
										htCondition.put("PAYHDRID", String.valueOf(paymentHeaderId));
										htCondition.put("BILLHDRID", (String) billHdrId.get(i));
										isAdded = billPaymentUtil.updatePaymentDet(query, htCondition, "");
									}
								}
								if (!(paymentDetList.size() > 0)) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
								}
							}
						} else {
							paymentDetInfo = new Hashtable<String, String>();
							paymentDetInfo.put("AMOUNT", amountpaid);
							String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
							paymentDetInfo.put("BALANCE", detbal);
							paymentDetInfo.put("UPBY", username);
							paymentDetInfo.put("UPAT", DateUtils.getDateTime());
							paymentDetInfoList.add(paymentDetInfo);
							String query = "SET AMOUNT='" + amountpaid + "'," + "CURRENCYUSEQT='" + currencyuseqt + "',"
									+ "BALANCE='" + detbal + "'," + "UPBY='" + username + "'," + "UPAT='"
									+ DateUtils.getDateTime() + "'";
							Hashtable htCondition = new Hashtable();
							htCondition.put("PLANT", plant);
							htCondition.put("PAYHDRID", String.valueOf(paymentHeaderId));
							isAdded = billPaymentUtil.updatePaymentDet(query, htCondition, "");
						}
						/* <--check--> */
						while (fileIterator.hasNext()) {
							FileItem fileItem = (FileItem) fileIterator.next();
							if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
								String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + paymentHeaderId;
								String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + paymentHeaderId;
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
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment.put("PLANT", plant);
								paymentAttachment.put("FILETYPE", fileItem.getContentType());
								paymentAttachment.put("FILENAME", fileName);
								paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								paymentAttachment.put("FILEPATH", fileLocation);
								paymentAttachment.put("UPBY", username);
								paymentAttachment.put("UPAT", DateUtils.getDateTime());
								paymentAttachmentList.add(paymentAttachment);
							}
						}
						int attchSize = paymentAttachmentList.size();
						for (int i = 0; i < attchSize; i++) {
							paymentAttachment = new Hashtable<String, String>();
							paymentAttachment = paymentAttachmentList.get(i);
							paymentAttachment.put("PAYHDRID", paymentHeaderId);
							paymentAttachmentInfoList.add(paymentAttachment);
						}
						if (isAdded) {
							if (paymentAttachmentInfoList.size() > 0)
								isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

							String curency = StrUtils
									.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
							// Journal Entry
							JournalHeader journalHead = new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(paymentDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
							journalHead.setTRANSACTION_ID(paymentHeaderId);
							journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
							journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							List<JournalDetail> journalDetails = new ArrayList<>();
							CoaDAO coaDAO = new CoaDAO();
							VendMstDAO vendorDAO = new VendMstDAO();
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
								coaJson1 = coaDAO.getCOAByName(plant,
										vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
								jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
							}
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME("PDC issued");
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										// journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									} else {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									}
								}
							} else {
								JournalDetail journalDetail = new JournalDetail();
								journalDetail.setPLANT(plant);
								journalDetail.setDESCRIPTION(jdesc+"-"+reference);
								JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
								System.out.println("Json" + coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(paidThrough);
								journalDetail.setCREDITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail);
							}
							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);
							
							if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

							} else {
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if (coaJson1.getString("account_name") != null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								} else {
									journalDetail_1.setACCOUNT_NAME((String) vendno);
								}
								
								journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
								journalDetail_1.setDEBITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail_1);
							}
							if (!bankCharge.isEmpty()) {
								Double bankChargeFrom = Double.parseDouble(bankCharge);
								if (bankChargeFrom > 0) {
									JournalDetail journalDetail_2 = new JournalDetail();
									journalDetail_2.setPLANT(plant);
									journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
									JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
									if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

									} else {
										journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail_2.setACCOUNT_NAME("Bank charges");
										journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
										journalDetails.add(journalDetail_2);
									}
								}
							}

							Journal journal = new Journal();
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
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								} else {
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
							}

						}
						if (isAdded) {
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_PAYMENT);
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
							// htMovHis.put(IDBConstants.ITEM, paymentMode);
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(paymentHeaderId));
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS", reference);
							isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
					if (isAdded) {
						for (int i = 0; i < billHdrId.size(); i++) {
							Double billamountinit = Double.parseDouble((String) billAmount.get(i));
							Double billamountpaid = Double.parseDouble((String) amount.get(i));
							String query = "";
							if (billamountpaid > 0.00) {
								if (billamountpaid.doubleValue() < billamountinit.doubleValue()) {
									query = " SET BILL_STATUS='Partially Paid'";
								} else if (billamountpaid.doubleValue() == billamountinit.doubleValue()) {
									query = " SET BILL_STATUS='Paid'";
								} else {
									query = " SET BILL_STATUS='Open'";
								}
								Hashtable htCondition = new Hashtable();
								htCondition.put("ID", (String) billHdrId.get(i));
								htCondition.put("PLANT", plant);
								isAdded = billDao.updateBillHdr(query, htCondition, "");
							}
						}
					}
					if (isAdded) {
						DbBean.CommitTran(ut);
						result = "Payment made successfully";
					} else {
						DbBean.RollbackTran(ut);
						result = "Coudn't process payment";
					}
					if (result.equalsIgnoreCase("Coudn't process payment"))
						response.sendRedirect("../banking/editbillpay?result=" + result);
					else
						response.sendRedirect("../banking/billpaysummary?result=" + result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/editbillpay?result=" + e.toString());
			}
		}

		else if (action.equalsIgnoreCase("ApplyCredit")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTDET */
			String[] payHdrId = null, billHdrId = null, amount = null, pono = null, payDetId = null, capplyKey = null;
			payHdrId = request.getParameterValues("PAYHDRID[]");
			billHdrId = request.getParameterValues("BILLHDRID[]");
			amount = request.getParameterValues("AMOUNT[]");
			pono = request.getParameterValues("PONO[]");
			payDetId = request.getParameterValues("PAYDETID[]");
			capplyKey = request.getParameterValues("CAPPLYKEY[]");
			String curtobase = request.getParameter("CURRENCYUSEQT");
//			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			BillPaymentDAO billPaymentDao = new BillPaymentDAO();
			BillDAO billDao = new BillDAO();
//			DateUtils dateutils = new DateUtils();
			BillUtil billUtil = new BillUtil();

			UserTransaction ut = null;
			boolean isAdded = false;
			JSONObject jsonObjectResult = new JSONObject();
			try {
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();

				for (int i = 0; i < payHdrId.length; i++) {
					String billno = "";
					String pordno = "";
					Hashtable ht = new Hashtable();
					ht.put("PAYHDRID", payHdrId[i]);
					ht.put("PLANT", plant);
					List advpaymentList = billPaymentDao.getMaxLineNoForAdvancePayment(ht);
					Map map = (Map) advpaymentList.get(0);
					int lnno = Integer.parseInt((String) map.get("LNNO"));
					double balance = Double.parseDouble((String) map.get("BALANCE"));
					Hashtable<String, String> paymentDetInfo = new Hashtable<String, String>();
					paymentDetInfo.put("PLANT", plant);
					paymentDetInfo.put("LNNO", Integer.toString(lnno + 1));
					paymentDetInfo.put("AMOUNT", amount[i]);
					paymentDetInfo.put("BALANCE", "0");
					paymentDetInfo.put("PONO", pono[i]);
					paymentDetInfo.put("PAYHDRID", payHdrId[i]);
					paymentDetInfo.put("BILLHDRID", (String) billHdrId[i]);
					paymentDetInfo.put("TYPE", "REGULAR");
					paymentDetInfo.put("CURRENCYUSEQT", curtobase);
					paymentDetInfo.put("CRAT", DateUtils.getDateTime());
					paymentDetInfo.put("CRBY", username);
					paymentDetInfo.put("UPAT", DateUtils.getDateTime());
					if (capplyKey == null) {

					} else {
						if (capplyKey.length > 0) {
							paymentDetInfo.put("CREDITAPPLYKEY", capplyKey[i]);
						}
					}
					isAdded = billPaymentDao.addPaymentDet(paymentDetInfo, plant);
					if (isAdded) {
						Hashtable htCondition = new Hashtable();
						htCondition.put("PAYHDRID", payHdrId[i]);
						htCondition.put("LNNO", "0");
						htCondition.put("PLANT", plant);
						htCondition.put("ID", payDetId[i]);

						balance = balance - Double.parseDouble(amount[i]);

						String query = " SET BALANCE='" + balance + "'";
						isAdded = billPaymentDao.updatePaymentDet(query, htCondition, "");
						if (isAdded) {

							Hashtable ht1 = new Hashtable();
							ht1.put("PONO", pono[i]);
							ht1.put("BILLHDRID", billHdrId[i]);
							ht1.put("PLANT", plant);
							String paymentMade = billPaymentDao.getpaymentMadeyBillwithbillno(ht1);
							double dPaymentMade = "".equals(paymentMade) ? 0.0d : Double.parseDouble(paymentMade);

							Hashtable ht2 = new Hashtable();
							ht2.put("ID", billHdrId[i]);
							ht2.put("PLANT", plant);
							List billHdrList = billUtil.getBillHdrById(ht2);
							Map billHdr = (Map) billHdrList.get(0);
							billno = (String) billHdr.get("BILL");
							pordno = (String) billHdr.get("PONO");
							if (!pordno.equalsIgnoreCase("")) {
								pordno = pordno + ",";
							}
							String totalAmount = (String) billHdr.get("CONV_TOTAL_AMOUNT");
							double dTotalAmount = "".equals(totalAmount) ? 0.0d : Double.parseDouble(totalAmount);

							double paydiff = dTotalAmount - dPaymentMade;

							htCondition = new Hashtable();
							htCondition.put("ID", billHdrId[i]);
							htCondition.put("PLANT", plant);

							if (paydiff > 0) {
								query = " SET BILL_STATUS='Partially Paid'";
							} else {
								query = " SET BILL_STATUS='Paid'";
							}

							isAdded = billDao.updateBillHdr(query, htCondition, "");
						}

						String advancefrom = billPaymentDao.getadvancefrompaymentdet(payHdrId[i], plant);

						if (advancefrom.equalsIgnoreCase("GENERAL")) {
							// Supplier Credit Status Update
							SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
							Hashtable SCht = new Hashtable();
							SCht.put("PLANT", plant);
							isAdded = supplierCreditDAO.updateHdr(
									"SET CREDIT_STATUS= case when (select ISNULL((B.BALANCE),0) AMOUNT from " + plant
											+ "_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID ='" + payHdrId[i]
											+ "')>0 then 'Partially Applied' when (select ISNULL((B.BALANCE),0) AMOUNT from "
											+ plant + "_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID ='"
											+ payHdrId[i] + "')=0 then 'Applied' else 'Open' end ",
									SCht, " AND CREDITNOTE in (select REFERENCE from " + plant
											+ "_FINPAYMENTHDR where ID='" + payHdrId[i] + "')");
						}

						MovHisDAO movHisDao = new MovHisDAO();

						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.APPLY_CREDIT_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(payHdrId[i]));
						htMovHis.put(IDBConstants.CREATED_BY, username);
						htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htMovHis.put("REMARKS", pordno + billno + "," + amount[i]);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					} else {
						break;
					}
				}
				if (isAdded) {
					DbBean.CommitTran(ut);
					jsonObjectResult.put("MESSAGE", "Payment processed successfully");
					jsonObjectResult.put("ERROR_CODE", "100");
				} else {
					DbBean.RollbackTran(ut);
					jsonObjectResult.put("MESSAGE", "Could not process payment");
					jsonObjectResult.put("ERROR_CODE", "99");
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				jsonObjectResult.put("MESSAGE", e.getMessage());
				jsonObjectResult.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

		else if (action.equalsIgnoreCase("Save_voucher")) {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTHDR */
			String amountpaid = "", paymentDate = "", paymentMode = "", paidThrough = "", reference = "",
					currencyid = "", currencyuseqt = "0", bankBranch = "", amountUfp = "", amountRefunded = "",
					bankCharge = "", checqueNo = "", note = "", uuid = "", account_name = "", chequedate = "",
					paidpdcstatus = "", projectid = "";
			// vendno = "", paytype="",ponoadv="",newadv="",

			/* FINPAYMENTDET */
			String payHdrId = "";
//			List billHdrId = new ArrayList(), amount = new ArrayList(), pono = new ArrayList(), type = new ArrayList(),billAmount=new ArrayList();
			List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),
					chequeamount = new ArrayList(), pdcstatus = new ArrayList();
			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0, pdccount = 0;

			UserTransaction ut = null;
			Hashtable<String, String> paymentAttachment = null;
			Hashtable<String, String> paymentDetInfo = null;
			List<Hashtable<String, String>> paymentAttachmentList = null;
			List<Hashtable<String, String>> paymentDetInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentInfoList = null;
			Hashtable<String, String> billpdcInfo = null;
			List<Hashtable<String, String>> billpdcInfoList = null;
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
//			BillPaymentDAO billPaymentDao = new BillPaymentDAO();

//			BillDAO billDao = new BillDAO();
			boolean isAdded = false;
			String result = "";
//			int billCount = 0, amountCount = 0, ponoCount = 0, typeCount = 0,billamount=0;
//			FileItem fileUploadItem = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					paymentAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							/* FINPAYMENTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								account_name = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_amount_paid")) {
								// if (fileItem.getFieldName().equalsIgnoreCase("v_conv_amount_paid_Curr")) {
								amountpaid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_payment_date")) {
								paymentDate = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_payment_mode")) {
								paymentMode = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_paid_through_account_name")) {
								paidThrough = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_refrence")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}
							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("voucher_bank_branch")) {
							 * bankBranch = StrUtils.fString(fileItem.getString()).trim(); }
							 */
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_bank_charge")) {
								bankCharge = StrUtils.fString(fileItem.getString()).trim();
							}
							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("voucher_checqueNo")) {
							 * checqueNo = StrUtils.fString(fileItem.getString()).trim(); } if
							 * (fileItem.getFieldName().equalsIgnoreCase("voucher_cheque_date")) {
							 * chequedate = StrUtils.fString(fileItem.getString()).trim(); }
							 */

							if (fileItem.getFieldName().equalsIgnoreCase("v_paidpdcstatus")) {
								paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("voucher_note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("voucher_CURRENCYID")) {
								currencyid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("voucher_CURRENCYUSEQT")) {
								currencyuseqt = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("VPROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									projectid = StrUtils.fString(fileItem.getString()).trim();
								}
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("v_pdcstatus")) {
								pdcstatus.add(pdccount, StrUtils.fString(fileItem.getString()).trim());
								pdccount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("v_bankname")) {
								bankcode.add(bankcodeCount, StrUtils.fString(fileItem.getString()).trim());
								bankcodeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("v_cheque-no")) {
								chequeno.add(chequenoCount, StrUtils.fString(fileItem.getString()).trim());
								chequenoCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("v_cheque-date")) {
								chedate.add(chedateCount, StrUtils.fString(fileItem.getString()).trim());
								chedateCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("v_cheque-amount")) {
								chequeamount.add(chequeamountCount, StrUtils.fString(fileItem.getString()).trim());
								chequeamountCount++;
							}
						}
					}
					paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
					paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
					Hashtable paymentHdr = new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", "N/A");
					paymentHdr.put("ACCOUNT_NAME", account_name);
					paymentHdr.put("AMOUNTPAID", amountpaid);
					paymentHdr.put("PAYMENT_DATE", paymentDate);
					paymentHdr.put("PAYMENT_MODE", paymentMode);
					paymentHdr.put("PAID_THROUGH", paidThrough);
					paymentHdr.put("REFERENCE", reference);
					paymentHdr.put("BANK_BRANCH", bankBranch);
					paymentHdr.put("AMOUNTUFP", amountUfp);
					paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
					paymentHdr.put("BANK_CHARGE", bankCharge);
					paymentHdr.put("CHECQUE_NO", checqueNo);
					paymentHdr.put("CHEQUE_DATE", chequedate);
					paymentHdr.put("NOTE", note);
					paymentHdr.put("CRAT", DateUtils.getDateTime());
					paymentHdr.put("CRBY", username);
					paymentHdr.put("UPAT", DateUtils.getDateTime());
					paymentHdr.put("CREDITAPPLYKEY", uuid);
					paymentHdr.put("ISPDCPROCESS", "0");
					paymentHdr.put("PROJECTID", projectid);
					paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
					if (paidpdcstatus.equalsIgnoreCase("1")) {
						for (int i = 0; i < bankcode.size(); i++) {
							if (pdcstatus.get(i).equals("1")) {
								paymentHdr.put("ISPDCPROCESS", "1");
							} else {
								paymentHdr.put("ISPDCPROCESS", "0");
							}
						}
					}
					/* Get Transaction object */
					ut = DbBean.getUserTranaction();
					/* Begin Transaction */
					ut.begin();

					int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
					payHdrId = Integer.toString(paymentHdrId);

					if (paymentHdrId > 0) {
						billpdcInfoList = new ArrayList<Hashtable<String, String>>();
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							for (int i = 0; i < bankcode.size(); i++) {
								billpdcInfo = new Hashtable<String, String>();
								billpdcInfo.put("PLANT", plant);
								billpdcInfo.put("VENDNO", "N/A");
								billpdcInfo.put("ACCOUNT_NAME", account_name);
								billpdcInfo.put("PAYMENTID", payHdrId);
								billpdcInfo.put("PAYMENT_DATE", paymentDate);
								billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
								billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
								billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
								billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
								billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
								billpdcInfo.put("CRAT", DateUtils.getDateTime());
								billpdcInfo.put("CRBY", username);
								if (pdcstatus.get(i).equals("1")) {
									billpdcInfo.put("STATUS", "NOT PROCESSED");
								} else {
									billpdcInfo.put("STATUS", "NOT APPLICABLE");
								}

								billpdcInfoList.add(billpdcInfo);
							}
							billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
						}
					}

					if (paymentHdrId > 0) {
						paymentDetInfo = new Hashtable<String, String>();
						paymentDetInfo.put("PLANT", plant);
						paymentDetInfo.put("LNNO", "0");
						paymentDetInfo.put("AMOUNT", amountpaid);
						paymentDetInfo.put("BALANCE", amountpaid);
						paymentDetInfo.put("PONO", "");
						paymentDetInfo.put("PAYHDRID", payHdrId);
						paymentDetInfo.put("BILLHDRID", "0");
						paymentDetInfo.put("TYPE", "VOUCHER");
						paymentDetInfo.put("CRAT", DateUtils.getDateTime());
						paymentDetInfo.put("CRBY", username);
						paymentDetInfo.put("UPAT", DateUtils.getDateTime());
						paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
						paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						paymentDetInfoList.add(paymentDetInfo);

						isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);

						while (fileIterator.hasNext()) {
							FileItem fileItem = (FileItem) fileIterator.next();
							if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
								String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
								String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment.put("PLANT", plant);
								paymentAttachment.put("FILETYPE", fileItem.getContentType());
								paymentAttachment.put("FILENAME", fileName);
								paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								paymentAttachment.put("FILEPATH", fileLocation);
								paymentAttachment.put("CRAT", DateUtils.getDateTime());
								paymentAttachment.put("CRBY", username);
								paymentAttachmentList.add(paymentAttachment);
							}
						}
						int attchSize = paymentAttachmentList.size();
						for (int i = 0; i < attchSize; i++) {
							paymentAttachment = new Hashtable<String, String>();
							paymentAttachment = paymentAttachmentList.get(i);
							paymentAttachment.put("PAYHDRID", payHdrId);
							paymentAttachmentInfoList.add(paymentAttachment);
						}
						if (isAdded) {
							if (paymentAttachmentInfoList.size() > 0)
								isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

							String curency = StrUtils
									.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
							// Journal Entry
							JournalHeader journalHead = new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(paymentDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
							journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
							journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
							journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							List<JournalDetail> journalDetails = new ArrayList<>();
							CoaDAO coaDAO = new CoaDAO();
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME("PDC issued");
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										// journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									} else {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									}
								}
							} else {
								JournalDetail journalDetail = new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
								System.out.println("Json" + coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(paidThrough);
								journalDetail.setCREDITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail);
							}

							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) account_name);
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail_1.setACCOUNT_NAME((String) account_name);
							journalDetail_1.setDEBITS(Double.parseDouble(amountpaid));
							journalDetails.add(journalDetail_1);

							if (!bankCharge.isEmpty()) {
								Double bankChargeFrom = Double.parseDouble(bankCharge);
								if (bankChargeFrom > 0) {
									JournalDetail journalDetail_2 = new JournalDetail();
									journalDetail_2.setPLANT(plant);
									JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
									if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

									} else {
										journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail_2.setACCOUNT_NAME("Bank charges");
										journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
										journalDetails.add(journalDetail_2);
									}
								}
							}

							Journal journal = new Journal();
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);
							JournalService journalService = new JournalEntry();
							journalService.addJournal(journal, username);
							Hashtable jhtMovHis = new Hashtable();
							jhtMovHis.put(IDBConstants.PLANT, plant);
							jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
							jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							jhtMovHis.put(IDBConstants.ITEM, "");
							jhtMovHis.put(IDBConstants.QTY, "0.0");
							jhtMovHis.put("RECID", "");
							jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE()
									+ " " + journal.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis.put(IDBConstants.CREATED_BY, username);
							jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis.put("REMARKS", "");
							movHisDao.insertIntoMovHis(jhtMovHis);
						}
						if (isAdded) {
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
							// htMovHis.put(IDBConstants.ITEM, paymentMode);
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS", reference);
							isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}

					if (isAdded) {
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							for (int i = 0; i < bankcode.size(); i++) {

								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
								htMovHis.put(IDBConstants.CREATED_BY, username);
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								if (pdcstatus.get(i).equals("1")) {
									htMovHis.put("REMARKS", "PDC-N/A," + bankcode.get(i) + "," + chequeno.get(i) + ","
											+ chedate.get(i) + "," + chequeamount.get(i));
								} else {
									htMovHis.put("REMARKS", "CDC-N/A," + bankcode.get(i) + "," + chequeno.get(i) + ","
											+ chedate.get(i) + "," + chequeamount.get(i));
								}

								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}
						}
					}

					if (isAdded) {
						DbBean.CommitTran(ut);
						result = "Payment made successfully";
					} else {
						DbBean.RollbackTran(ut);
						result = "Coudn't process payment";
					}
					if (result.equalsIgnoreCase("Coudn't process payment")) {
						response.sendRedirect("../banking/createbillpay?result=" + result);
					} else
						response.sendRedirect("../banking/billpaysummary?result=" + result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/createbillpay?result=" + e.toString());
			}

		} else if (action.equalsIgnoreCase("Edit_voucher")) {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTHDR */
			String account_name = "", amountpaid = "", paymentDate = "", paymentMode = "", paidThrough = "",
					reference = "", currencyuseqt = "0", amountUfp = "", amountRefunded = "", bankCharge = "",
					note = "", paidpdcstatus = "", projectid = "";// bankBranch = "",currencyid="", checqueNo =
																	// "",chequedate="",

			/* FINPAYMENTDET */
			String payHdrId = "";
			List billHdrId = new ArrayList(), amount = new ArrayList(), pono = new ArrayList(), type = new ArrayList(),
					billAmount = new ArrayList(), paymentDetId = new ArrayList();

			List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),
					chequeamount = new ArrayList(), pdcstatus = new ArrayList(), chequestatus = new ArrayList(),
					pdcid = new ArrayList();

			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0, pdccount = 0,
					checkstcount = 0, pdcidcount = 0;

			UserTransaction ut = null;
			Hashtable<String, String> paymentAttachment = null;
			Hashtable<String, String> paymentDetInfo = null;
			List<Hashtable<String, String>> paymentAttachmentList = null;
			List<Hashtable<String, String>> paymentDetInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentInfoList = null;
			Hashtable<String, String> billpdcInfo = null;
			List<Hashtable<String, String>> billpdcInfoList = null;
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
			BillPaymentDAO billPaymentDao = new BillPaymentDAO();
			BillDAO billDao = new BillDAO();
			boolean isAdded = false;
			String result = "";
			String paymentHeaderId = "";
			int billCount = 0, amountCount = 0, ponoCount = 0, typeCount = 0, billamount = 0, paymentdetCount = 0;
//			FileItem fileUploadItem = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					paymentAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							/* FINPAYMENTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								account_name = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
								// if (fileItem.getFieldName().equalsIgnoreCase("conv_amount_paid_Curr")) {
								amountpaid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
								paymentDate = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
								paymentMode = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
								paidThrough = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}
							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("bank_branch")) { bankBranch =
							 * StrUtils.fString(fileItem.getString()).trim(); }
							 */
							if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
								paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
								amountUfp = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
								amountRefunded = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) {
								bankCharge = StrUtils.fString(fileItem.getString()).trim();
							}

							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("checqueNo")) { checqueNo =
							 * StrUtils.fString(fileItem.getString()).trim(); } if
							 * (fileItem.getFieldName().equalsIgnoreCase("cheque_date")) { chequedate =
							 * StrUtils.fString(fileItem.getString()).trim(); }
							 */

							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paymenthdrid")) {
								paymentHeaderId = StrUtils.fString(fileItem.getString()).trim();
							}

//							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
//								currencyid=StrUtils.fString(fileItem.getString()).trim();
//							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt = StrUtils.fString(fileItem.getString()).trim();
							}

							/* FINPAYMENTDET */
							if (fileItem.getFieldName().equalsIgnoreCase("paymentdetid")) {
								paymentDetId.add(paymentdetCount, StrUtils.fString(fileItem.getString()).trim());
								paymentdetCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
								billHdrId.add(billCount, StrUtils.fString(fileItem.getString()).trim());
								billCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
								amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
								amountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
								pono.add(ponoCount, StrUtils.fString(fileItem.getString()).trim());
								ponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("type")) {
								type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
								typeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billamount")) {
								billAmount.add(billamount, StrUtils.fString(fileItem.getString()).trim());
								billamount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									projectid = StrUtils.fString(fileItem.getString()).trim();
								}
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("chequestatus")) {
								chequestatus.add(checkstcount, StrUtils.fString(fileItem.getString()).trim());
								checkstcount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcid")) {
								pdcid.add(pdcidcount, StrUtils.fString(fileItem.getString()).trim());
								pdcidcount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcstatus")) {
								pdcstatus.add(pdccount, StrUtils.fString(fileItem.getString()).trim());
								pdccount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
								bankcode.add(bankcodeCount, StrUtils.fString(fileItem.getString()).trim());
								bankcodeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
								chequeno.add(chequenoCount, StrUtils.fString(fileItem.getString()).trim());
								chequenoCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
								chedate.add(chedateCount, StrUtils.fString(fileItem.getString()).trim());
								chedateCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
								chequeamount.add(chequeamountCount, StrUtils.fString(fileItem.getString()).trim());
								chequeamountCount++;
							}
						}
					}

					paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
					paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
					Hashtable paymentHdr = new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", "N/A");
					paymentHdr.put("ACCOUNT_NAME", account_name);
					paymentHdr.put("AMOUNTPAID", amountpaid);
					paymentHdr.put("PAYMENT_DATE", paymentDate);
					paymentHdr.put("PAYMENT_MODE", paymentMode);
					paymentHdr.put("PAID_THROUGH", paidThrough);
					paymentHdr.put("REFERENCE", reference);
					// paymentHdr.put("BANK_BRANCH", bankBranch);
					paymentHdr.put("AMOUNTUFP", amountUfp);
					paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
					paymentHdr.put("BANK_CHARGE", bankCharge);
					// paymentHdr.put("CHECQUE_NO", checqueNo);
					// put("CHEQUE_DATE", chequedate);
					paymentHdr.put("NOTE", note);
					paymentHdr.put("UPBY", username);
					paymentHdr.put("UPAT", DateUtils.getDateTime());
					paymentHdr.put("PROJECTID", projectid);
					if (paidpdcstatus.equalsIgnoreCase("1")) {
						for (int i = 0; i < bankcode.size(); i++) {
							String chequecheck = (String) chequestatus.get(i);
							if (!chequecheck.equalsIgnoreCase("PROCESSED")) {
								if (pdcstatus.get(i).equals("1")) {
									paymentHdr.put("ISPDCPROCESS", "1");
								} else {
									paymentHdr.put("ISPDCPROCESS", "0");
								}
							}
						}
					}
					/* Get Transaction object */
					ut = DbBean.getUserTranaction();
					/* Begin Transaction */
					ut.begin();

					int paymentHdrId = billPaymentUtil.updatePaymentHdr(paymentHeaderId, paymentHdr, plant);
					payHdrId = Integer.toString(paymentHdrId);

					if (paymentHdrId > 0) {
						billpdcInfoList = new ArrayList<Hashtable<String, String>>();
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							billPaymentDao.deletepdcbypayid(plant, paymentHeaderId);
							for (int i = 0; i < bankcode.size(); i++) {
//								String pdcidcheck = (String) pdcid.get(i);
								String chequecheck = (String) chequestatus.get(i);
								if (!chequecheck.equalsIgnoreCase("PROCESSED")) {
									/* if(pdcidcheck.equalsIgnoreCase("0")) { */
									billpdcInfo = new Hashtable<String, String>();
									billpdcInfo.put("PLANT", plant);
									billpdcInfo.put("VENDNO", "N/A");
									billpdcInfo.put("ACCOUNT_NAME", account_name);
									billpdcInfo.put("PAYMENTID", paymentHeaderId);
									billpdcInfo.put("PAYMENT_DATE", paymentDate);
									billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
									billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
									billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
									billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
									billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
									billpdcInfo.put("CRAT", DateUtils.getDateTime());
									billpdcInfo.put("CRBY", username);
									if (pdcstatus.get(i).equals("1")) {
										billpdcInfo.put("STATUS", "NOT PROCESSED");
									} else {
										billpdcInfo.put("STATUS", "NOT APPLICABLE");
									}
									billpdcInfoList.add(billpdcInfo);
									/*
									 * }else { String querypdc="BANK_BRANCH='"+(String)
									 * bankcode.get(i)+"',CHEQUE_DATE='"+(String)
									 * chedate.get(i)+"',CHEQUE_AMOUNT='"+(String) chequeamount.get(i)+"'," +
									 * "ISPDC='"+(String) pdcstatus.get(i)+"',CHECQUE_NO='"+(String)
									 * chequeno.get(i)+"',STATUS='"+chequecheck+"'," +
									 * ""+"UPBY='"+username+"',"+"UPAT='"+DateUtils.getDateTime()+"'";
									 * billPaymentDao.updatePaymentpdc(pdcidcheck, querypdc, plant);
									 * 
									 * }
									 */
								}
							}
							billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
						} else {
							billPaymentDao.deletepdcbypayid(plant, paymentHeaderId);
						}

					}

					if (paymentHdrId > 0) {
						Hashtable ht1 = new Hashtable();
						ht1.put("PAYHDRID", String.valueOf(paymentHeaderId));
						ht1.put("PLANT", plant);

						List paymentDetadvancelist = billPaymentDao.getBillPaymentDetListByadvance(ht1);
						if (paymentDetadvancelist.size() > 0) {
							Map paymentDetadvance = (Map) paymentDetadvancelist.get(0);
							double diffdet = 0.0;

							for (int i = 0; i < amount.size(); i++) {
								String detamountst = (String) amount.get(i);
								diffdet = diffdet + ("".equals(detamountst) ? 0.0d : Double.parseDouble(detamountst));
							}

							double detbalnced = ("".equals(amountpaid) ? 0.0d : Double.parseDouble(amountpaid))
									- diffdet;

							String queryad = "SET AMOUNT='" + amountpaid + "'," + "CURRENCYUSEQT='" + currencyuseqt
									+ "'," + "BALANCE='" + detbalnced + "'," + "UPBY='" + username + "'," + "UPAT='"
									+ DateUtils.getDateTime() + "'";
							Hashtable htCondition1 = new Hashtable();
							htCondition1.put("PLANT", plant);
							htCondition1.put("PAYHDRID", String.valueOf(paymentHeaderId));
							htCondition1.put("ID", (String) paymentDetadvance.get("ID"));
							isAdded = billPaymentUtil.updatePaymentDet(queryad, htCondition1, "");

						}

						List paymentDetList = billPaymentUtil.getPaymentDetList(ht1);
						if (billHdrId.size() > 0) {
							Double totalBillAmount = 0.00;
							for (int j = 0; j < amountCount; j++) {
								totalBillAmount += Double.parseDouble((String) amount.get(j));
							}
							if (totalBillAmount > 0) {
								for (int i = 0; i < billHdrId.size(); i++) {
									int lnno = i + 1;
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										paymentDetInfo.put("BALANCE", amountpaid);
									} else {
										paymentDetInfo.put("LNNO", Integer.toString(lnno));
										paymentDetInfo.put("AMOUNT", (String) amount.get(i));
										paymentDetInfo.put("BALANCE", "0");
									}
									paymentDetInfo.put("PONO", (String) pono.get(i));
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfo.put("TYPE", (String) type.get(i));
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("UPBY", username);
									paymentDetInfoList.add(paymentDetInfo);
									if (paymentDetList.size() > 0) {
										String query = "SET AMOUNT='" + (String) amount.get(i) + "'," + "UPBY='"
												+ username + "'," + "UPAT='" + DateUtils.getDateTime() + "'";
										Hashtable htCondition = new Hashtable();
										htCondition.put("PLANT", plant);
										htCondition.put("PAYHDRID", String.valueOf(paymentHeaderId));
										htCondition.put("BILLHDRID", (String) billHdrId.get(i));
										isAdded = billPaymentUtil.updatePaymentDet(query, htCondition, "");
									}
								}
								if (!(paymentDetList.size() > 0)) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
								}
							}
						} else {
							paymentDetInfo = new Hashtable<String, String>();
							paymentDetInfo.put("AMOUNT", amountpaid);
							paymentDetInfo.put("BALANCE", amountpaid);
							paymentDetInfo.put("UPBY", username);
							paymentDetInfo.put("UPAT", DateUtils.getDateTime());
							paymentDetInfoList.add(paymentDetInfo);
							String query = "SET AMOUNT='" + amountpaid + "'," + "BALANCE='" + amountpaid + "',"
									+ "CURRENCYUSEQT='" + currencyuseqt + "'," + "UPBY='" + username + "'," + "UPAT='"
									+ DateUtils.getDateTime() + "'";
							Hashtable htCondition = new Hashtable();
							htCondition.put("PLANT", plant);
							htCondition.put("PAYHDRID", String.valueOf(paymentHeaderId));
							isAdded = billPaymentUtil.updatePaymentDet(query, htCondition, "");
						}

						while (fileIterator.hasNext()) {
							FileItem fileItem = (FileItem) fileIterator.next();
							if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
								String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + paymentHeaderId;
								String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + paymentHeaderId;
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
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment.put("PLANT", plant);
								paymentAttachment.put("FILETYPE", fileItem.getContentType());
								paymentAttachment.put("FILENAME", fileName);
								paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								paymentAttachment.put("FILEPATH", fileLocation);
								paymentAttachment.put("UPBY", username);
								paymentAttachment.put("UPAT", DateUtils.getDateTime());
								paymentAttachmentList.add(paymentAttachment);
							}
						}
						int attchSize = paymentAttachmentList.size();
						for (int i = 0; i < attchSize; i++) {
							paymentAttachment = new Hashtable<String, String>();
							paymentAttachment = paymentAttachmentList.get(i);
							paymentAttachment.put("PAYHDRID", paymentHeaderId);
							paymentAttachmentInfoList.add(paymentAttachment);
						}
						if (isAdded) {
							if (paymentAttachmentInfoList.size() > 0)
								isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);
						}
						if (isAdded) {
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_PAYMENT);
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
							// htMovHis.put(IDBConstants.ITEM, paymentMode);
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(paymentHeaderId));
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS", reference);
							isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}

						if (isAdded) {
							if (paymentAttachmentInfoList.size() > 0)
								isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

							String curency = StrUtils
									.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
							// Journal Entry
							JournalHeader journalHead = new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(paymentDate);
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
							journalHead.setTRANSACTION_ID(paymentHeaderId);
							journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
							journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							List<JournalDetail> journalDetails = new ArrayList<>();
							CoaDAO coaDAO = new CoaDAO();
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME("PDC issued");
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									} else {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
										if (!bankCharge.isEmpty()) {
											if (Double.parseDouble(bankCharge) > 0) {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																+ Double.parseDouble(bankCharge));
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
										} else {
											journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
										}
										journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
										journalDetails.add(journalDetail);
									}
								}
							} else {
								JournalDetail journalDetail = new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
								System.out.println("Json" + coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(paidThrough);
								journalDetail.setDESCRIPTION(reference);
								journalDetail.setCREDITS(Double.parseDouble(amountpaid));
								journalDetails.add(journalDetail);
							}

							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) account_name);
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail_1.setACCOUNT_NAME((String) account_name);
							journalDetail_1.setDEBITS(Double.parseDouble(amountpaid));
							journalDetails.add(journalDetail_1);

							if (!bankCharge.isEmpty()) {
								Double bankChargeFrom = Double.parseDouble(bankCharge);
								if (bankChargeFrom > 0) {
									JournalDetail journalDetail_2 = new JournalDetail();
									journalDetail_2.setPLANT(plant);
									journalDetail_2.setDESCRIPTION(reference);
									JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
									if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

									} else {
										journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail_2.setACCOUNT_NAME("Bank charges");
										journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
										journalDetails.add(journalDetail_2);
									}
								}
							}

							Journal journal = new Journal();
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
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								} else {
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
							}

						}
					}
					if (isAdded) {
						for (int i = 0; i < billHdrId.size(); i++) {
							Double billamountinit = Double.parseDouble((String) billAmount.get(i));
							Double billamountpaid = Double.parseDouble((String) amount.get(i));
							String query = "";
							if (billamountpaid > 0.00) {
								if (billamountpaid < billamountinit) {
									query = " SET BILL_STATUS='Partially Paid'";
								} else if (billamountpaid == billamountinit) {
									query = " SET BILL_STATUS='Paid'";
								} else {
									query = " SET BILL_STATUS='Open'";
								}

								Hashtable htCondition = new Hashtable();
								htCondition.put("ID", (String) billHdrId.get(i));
								htCondition.put("PLANT", plant);
								isAdded = billDao.updateBillHdr(query, htCondition, "");
							}
						}
					}
					if (isAdded) {
						DbBean.CommitTran(ut);
						result = "Payment made successfully";
					} else {
						DbBean.RollbackTran(ut);
						result = "Coudn't process payment";
					}
					if (result.equalsIgnoreCase("Coudn't process payment"))
						response.sendRedirect("../banking/editbillpay?result=" + result);
					else
						response.sendRedirect("../banking/billpaysummary?result=" + result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/editbillpay?result=" + e.toString());
			}

		} else if (action.equals("PDA_PAYMENTPROCESS")) {

//			boolean flag = false;
			String plant = "";// ,reverseDate = "",Id="";
			try {
				HttpSession session = request.getSession();
				BillPaymentDAO billDao = new BillPaymentDAO();
				BillDAO bdao = new BillDAO();
				MovHisDAO movHisDao = new MovHisDAO();
//	 			DateUtils dateutils = new DateUtils();
				BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
				plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
//	 			String FDATE= (String) request.getParameter("FROM_DATE");
//	 			String TDATE= (String) request.getParameter("TO_DATE");
//	 			String BANK= (String) request.getParameter("bank_account_name");
//	 			String VNAME= (String) request.getParameter("vendname");
//	 			String VCODE= (String) request.getParameter("vendno");
//	 			String CHEQUENO= (String) request.getParameter("CHEQUENO");

				String[] chkhdrid = request.getParameterValues("chkhdrid");
				String[] chkdPoNos = request.getParameterValues("chkdPoNo");

				CoaDAO coaDAO = new CoaDAO();
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				if (chkdPoNos != null) {
					for (int i = 0; i < chkdPoNos.length; i++) {
						String data = chkdPoNos[i];
						String hdrid = chkhdrid[i];
						String pdcamo = "0";
//						reverseDate = StrUtils.fString(request.getParameter("Reverse_"+data));
						Hashtable receiveMaterial_HM = new Hashtable();
						// receiveMaterial_HM.put("CHEQUE_REVERSAL_DATE", reverseDate);
						receiveMaterial_HM.put("STATUS", "PROCESSED");
						receiveMaterial_HM.put("UPBY", username);
						receiveMaterial_HM.put("UPAT", DateUtils.getDateTime());
//						int paymentHdrId = 
						billDao.updatePaymentPDC(data, receiveMaterial_HM, plant);

						Hashtable ht = new Hashtable();
						ht.put("ID", data);
						ht.put("PLANT", plant);
						List pdcDetailList = billDao.getpdcpaymentById(ht);
						String bankcharge = billDao.getBankChargeFromPaymentHdr(plant, hdrid);
						Double bankChargeCasted = 0.00;
						if (bankcharge != null)
							bankChargeCasted = Double.parseDouble(bankcharge);
						LocalDate today = LocalDate.now();
						DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						String paymentDate = today.format(datePattern);
						// Journal Entry
						List<JournalDetail> journalDetails = new ArrayList<>();
						JournalHeader journalHead = new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(paymentDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
						journalHead.setTRANSACTION_ID(hdrid);
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);

						if (pdcDetailList.size() > 0) {
							Map pdcHdr = (Map) pdcDetailList.get(0);
							String PAYMENTID = (String) pdcHdr.get("PAYMENTID");
							String vendName = (String) pdcHdr.get("SUPPLIER");
							String VENDNO = (String) pdcHdr.get("VENDNO");
							String ACCOUNT = (String) pdcHdr.get("ACCOUNT");
							String BANK_BRANCH = (String) pdcHdr.get("BANK_BRANCH");
							String CHECQUE_NO = (String) pdcHdr.get("CHECQUE_NO");
							String CHEQUE_DATE = (String) pdcHdr.get("CHEQUE_DATE");
							String CHEQUE_REVERSAL_DATE = (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
							String SAVNAME = "";
							pdcamo = (String) pdcHdr.get("CHEQUE_AMOUNT");

							if (VENDNO.equalsIgnoreCase("N/A")) {
								SAVNAME = ACCOUNT;
							} else {
								SAVNAME = vendName;
							}

							// Journal Entry for PDC
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME("PDC issued");
							journalDetail.setDEBITS(Double.parseDouble(pdcamo) + bankChargeCasted);
							journalDetail.setDESCRIPTION("Bank check no: " + (String) pdcHdr.get("CHECQUE_NO"));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail_1 = new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson_1 = coaDAO.getCOAByName(plant, (String) pdcHdr.get("BANK_BRANCH"));
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson_1.getString("id")));
							journalDetail_1.setACCOUNT_NAME((String) pdcHdr.get("BANK_BRANCH"));
							journalDetail_1.setCREDITS(Double.parseDouble(pdcamo) + bankChargeCasted);
							journalDetails.add(journalDetail_1);

							journalHead.setSUB_TOTAL(Double.parseDouble(pdcamo));
							journalHead.setTOTAL_AMOUNT(Double.parseDouble(pdcamo) + bankChargeCasted);
							Journal journal = new Journal();
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);
							JournalService journalService = new JournalEntry();
							journalService.addJournal(journal, username);
							Hashtable jhtMovHis = new Hashtable();
							jhtMovHis.put(IDBConstants.PLANT, plant);
							jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
							jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							jhtMovHis.put(IDBConstants.ITEM, "");
							jhtMovHis.put(IDBConstants.QTY, "0.0");
							jhtMovHis.put("RECID", "");
							jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE()
									+ " " + journal.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis.put(IDBConstants.CREATED_BY, username);
							jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis.put("REMARKS", "");
							movHisDao.insertIntoMovHis(jhtMovHis);

							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);
							htMovHis.put("DIRTYPE", TransactionConstants.PDC_PROCESS_PAYMENT);
							htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, PAYMENTID);
							htMovHis.put(IDBConstants.CREATED_BY, username);
							htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMovHis.put("REMARKS", SAVNAME + "," + BANK_BRANCH + "," + CHECQUE_NO + "," + CHEQUE_DATE
									+ "," + CHEQUE_REVERSAL_DATE);

//						flag = 
							movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}

						/*--------------old-------------------*/
						/*
						 * String pdcstatus = "0"; Hashtable htpdc = new Hashtable();
						 * htpdc.put("PAYMENTID",hdrid); htpdc.put("PLANT", plant); List pdcDetailListpc
						 * = billDao.getpdcbipayid(htpdc); for(int j =0; j < pdcDetailListpc.size();
						 * j++) { Map pdcdet=(Map)pdcDetailListpc.get(j); String status =
						 * (String)pdcdet.get("STATUS"); if(status.equalsIgnoreCase("NOT PROCESSED")) {
						 * pdcstatus = "1"; } }
						 * 
						 * if(pdcstatus.equalsIgnoreCase("0")) { Hashtable paymentHdr =new Hashtable();
						 * paymentHdr.put("ISPDCPROCESS", "0"); billPaymentUtil.updatePaymentHdr(hdrid,
						 * paymentHdr, plant); }
						 */
						/*--------------old-------------------*/

						Hashtable paymentHdr = new Hashtable();
						paymentHdr.put("ISPDCPROCESS", "0");
						billPaymentUtil.updatePaymentHdr(hdrid, paymentHdr, plant);

						Hashtable htrecpay1 = new Hashtable();
						htrecpay1.put("PAYHDRID", hdrid);
						htrecpay1.put("LNNO", "0");
						String sqlrepay1 = "SELECT * FROM " + plant + "_FINPAYMENTDET WHERE PLANT='" + plant + "'";
						List arrrepaylist1 = bdao.selectForReport(sqlrepay1, htrecpay1, "");
						if (arrrepaylist1.size() > 0) {
							Map paydet = (Map) arrrepaylist1.get(0);
							Hashtable htdetup = new Hashtable();
							htdetup.put("PLANT", plant);
							htdetup.put("ID", paydet.get("ID"));
							String debal = String
									.valueOf(Double.valueOf((String) paydet.get("BALANCE")) + Double.valueOf(pdcamo));
							billDao.updatePaymentDet("SET BALANCE=" + debal, htdetup, "");
						}

					}

					request.getSession().setAttribute("RESULT", "PDC Payment processed successfully!");
					// response.sendRedirect("jsp/pdcpaymentSummary.jsp?result=PDC Payment processed
					// successfully!&FROM_DATE="+FDATE+"&TO_DATE="+TDATE+"&VEND_NAME="+VNAME+"&VENDNO="+VCODE+"&BANK="+BANK+"&CHEQUENO="+CHEQUENO);
					response.sendRedirect("../banking/pdcpaysummary?result=PDC Payment processed successfully!");

				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String) request.getSession().getAttribute("CATCHERROR"));
				response.sendRedirect(
						"../banking/pdcpayprocess?result=Failed to Save PDC Payment Process" + e.toString());
			}
		} else if (action.equals("PDA_PAYMENTEDIT")) {

//			boolean flag = false;
			String plant = "", reverseDate = "", Id = "";
			try {
				HttpSession session = request.getSession();
				BillPaymentDAO billDao = new BillPaymentDAO();
				MovHisDAO movHisDao = new MovHisDAO();
//	 			DateUtils dateutils = new DateUtils();
				plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
				Id = (String) request.getParameter("TRANID");
				reverseDate = (String) request.getParameter("CHEQUE_REVERSAL_DATE");

				Hashtable receiveMaterial_HM = new Hashtable();
				receiveMaterial_HM.put("CHEQUE_REVERSAL_DATE", reverseDate);
				// receiveMaterial_HM.put("STATUS", "PROCESSED");
				receiveMaterial_HM.put("UPBY", username);
				receiveMaterial_HM.put("UPAT", DateUtils.getDateTime());
//						int paymentHdrId = 
				billDao.updatePaymentPDC(Id, receiveMaterial_HM, plant);

				Hashtable ht = new Hashtable();
				ht.put("ID", Id);
				ht.put("PLANT", plant);
				List pdcDetailList = billDao.getpdcpaymentById(ht);
				if (pdcDetailList.size() > 0) {
					Map pdcHdr = (Map) pdcDetailList.get(0);
					String PAYMENTID = (String) pdcHdr.get("PAYMENTID");
					String vendName = (String) pdcHdr.get("SUPPLIER");
					String VENDNO = (String) pdcHdr.get("VENDNO");
					String ACCOUNT = (String) pdcHdr.get("ACCOUNT");
					String BANK_BRANCH = (String) pdcHdr.get("BANK_BRANCH");
					String CHECQUE_NO = (String) pdcHdr.get("CHECQUE_NO");
					String CHEQUE_DATE = (String) pdcHdr.get("CHEQUE_DATE");
					String CHEQUE_REVERSAL_DATE = (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
					String SAVNAME = "";
					if (VENDNO.equalsIgnoreCase("N/A")) {
						SAVNAME = ACCOUNT;
					} else {
						SAVNAME = vendName;
					}
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.PDC_EDIT_PAYMENT);
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, PAYMENTID);
					htMovHis.put(IDBConstants.CREATED_BY, username);
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS", SAVNAME + "," + BANK_BRANCH + "," + CHECQUE_NO + "," + CHEQUE_DATE + ","
							+ CHEQUE_REVERSAL_DATE);

//						flag = 
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
				request.getSession().setAttribute("RESULT", "PDC Payment Updated successfully!");
				response.sendRedirect("../banking/pdcpaysummary?result=PDC Payment processed successfully!");

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String) request.getSession().getAttribute("CATCHERROR"));
				response.sendRedirect("../banking/editpdcpayment?TRANID=" + Id + "&result=Failed to Update PDC Payment"
						+ e.toString());
			}
		} else if (action.equals("PDA_PAYMENTCANCEL")) {

//			boolean flag = false;
			String plant = "", Id = "";// reverseDate = "",
			try {
				HttpSession session = request.getSession();
				BillPaymentDAO billDao = new BillPaymentDAO();
				MovHisDAO movHisDao = new MovHisDAO();
//	 			DateUtils dateutils = new DateUtils();
				plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
				Id = (String) request.getParameter("TRANID");

				Hashtable receiveMaterial_HM = new Hashtable();
				receiveMaterial_HM.put("CHEQUE_REVERSAL_DATE", "");
				receiveMaterial_HM.put("STATUS", "NOT PROCESSED");
				receiveMaterial_HM.put("UPBY", username);
				receiveMaterial_HM.put("UPAT", DateUtils.getDateTime());
//						int paymentHdrId = 
				billDao.updatePaymentPDC(Id, receiveMaterial_HM, plant);

				Hashtable ht = new Hashtable();
				ht.put("ID", Id);
				ht.put("PLANT", plant);
				List pdcDetailList = billDao.getpdcpaymentById(ht);
				if (pdcDetailList.size() > 0) {
					Map pdcHdr = (Map) pdcDetailList.get(0);
					String PAYMENTID = (String) pdcHdr.get("PAYMENTID");
					String vendName = (String) pdcHdr.get("SUPPLIER");
					String VENDNO = (String) pdcHdr.get("VENDNO");
					String ACCOUNT = (String) pdcHdr.get("ACCOUNT");
					String BANK_BRANCH = (String) pdcHdr.get("BANK_BRANCH");
					String CHECQUE_NO = (String) pdcHdr.get("CHECQUE_NO");
					String CHEQUE_DATE = (String) pdcHdr.get("CHEQUE_DATE");
					String CHEQUE_REVERSAL_DATE = (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
					String SAVNAME = "";
					if (VENDNO.equalsIgnoreCase("N/A")) {
						SAVNAME = ACCOUNT;
					} else {
						SAVNAME = vendName;
					}
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", "CANCEL_PDC_PAYMENT");
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, PAYMENTID);
					htMovHis.put(IDBConstants.CREATED_BY, username);
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS", SAVNAME + "," + BANK_BRANCH + "," + CHECQUE_NO + "," + CHEQUE_DATE + ","
							+ CHEQUE_REVERSAL_DATE);

//						flag = 
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
				request.getSession().setAttribute("RESULT", "PDC Payment Canceled successfully!");
				response.sendRedirect("../banking/pdcpaysummary?result=PDC Payment Canceled successfully!");

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				System.out.print((String) request.getSession().getAttribute("CATCHERROR"));
				response.sendRedirect("../banking/editpdcpayment?TRANID=" + Id + "&result=Failed to Cancel PDC Payment"
						+ e.toString());
			}
		}

		// ----

		if (action.equalsIgnoreCase("Save")) {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTHDR */
			String vendno = "", amountpaid = "", paymentDate = "", paymentMode = "", paidThrough = "", reference = "",
					paytype = "", ponoadv = "", newadv = "", projectid = "", amountUfp = "", amountRefunded = "",
					bankCharge = "", note = "", uuid = "", paidpdcstatus = "", currencyid = "", currencyuseqt = "0",woamount="0",jdesc="";// bankBranch
																													// =
																													// "",
																													// checqueNo
																													// =
																													// "",

			/* FINPAYMENTDET */
			String payHdrId = "";
			List billHdrId = new ArrayList(), amount = new ArrayList(), pono = new ArrayList(), type = new ArrayList(),
					billAmount = new ArrayList();
			List ExpHdrId = new ArrayList(), Expamount = new ArrayList(), ExpPono = new ArrayList(),
					ExpType = new ArrayList(), ExpenseAmount = new ArrayList();
			List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),
					chequeamount = new ArrayList(), pdcstatus = new ArrayList();
			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0, pdccount = 0;

			UserTransaction ut = null;
			Hashtable<String, String> paymentAttachment = null;
			Hashtable<String, String> paymentDetInfo = null;
			Hashtable<String, String> billpdcInfo = null;
			List<Hashtable<String, String>> billpdcInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentList = null;
			List<Hashtable<String, String>> paymentDetInfoList = null;
			List<Hashtable<String, String>> ExppaymentDetInfoList = null;
			List<Hashtable<String, String>> paymentAttachmentInfoList = null;
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
//			BillPaymentDAO billPaymentDao = new BillPaymentDAO();

			BillDAO billDao = new BillDAO();
			boolean isAdded = false;
			String result = "";
			int billCount = 0, amountCount = 0, ponoCount = 0, typeCount = 0, billamount = 0;
			int ExpIdCount = 0, ExpamountCount = 0, ExpponoCount = 0, ExptypeCount = 0, ExpenseamountCount = 0;
//			FileItem fileUploadItem = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					Iterator fileIterator = items.iterator();
					paymentAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							/* FINPAYMENTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								vendno = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
								// if (fileItem.getFieldName().equalsIgnoreCase("conv_amount_paid_Curr")) {
								amountpaid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
								paymentDate = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
								paymentMode = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
								paidThrough = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
								reference = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
								paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
								amountUfp = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
								amountRefunded = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) {
								bankCharge = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payablewo")) {
								woamount = StrUtils.fString(fileItem.getString()).trim();
							}

							/*
							 * if (fileItem.getFieldName().equalsIgnoreCase("checqueNo")) { checqueNo =
							 * StrUtils.fString(fileItem.getString()).trim(); } if
							 * (fileItem.getFieldName().equalsIgnoreCase("cheque_date")) { chequedate =
							 * StrUtils.fString(fileItem.getString()).trim(); }
							 */
							if (fileItem.getFieldName().equalsIgnoreCase("note")) {
								note = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("paytype")) {
								paytype = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ponoadv")) {
								ponoadv = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("newadv")) {
								newadv = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("uuid")) {
								uuid = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt = StrUtils.fString(fileItem.getString()).trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									projectid = StrUtils.fString(fileItem.getString()).trim();
								}
							}

							/* FINPAYMENTDET */
							if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
								billHdrId.add(billCount, StrUtils.fString(fileItem.getString()).trim());
								billCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("amount")) {
								amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
								amountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
								pono.add(ponoCount, StrUtils.fString(fileItem.getString()).trim());
								ponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("type")) {
								type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
								typeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("billamount")) {
								billAmount.add(billamount, StrUtils.fString(fileItem.getString()).trim());
								billamount++;
							}

							/* FINPAYMENTDETEXP */
							if (fileItem.getFieldName().equalsIgnoreCase("ExpHdrId")) {
								ExpHdrId.add(ExpIdCount, StrUtils.fString(fileItem.getString()).trim());
								ExpIdCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("Expamount")) {
								Expamount.add(ExpamountCount, StrUtils.fString(fileItem.getString()).trim());
								ExpamountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpPono")) {
								ExpPono.add(ExpponoCount, StrUtils.fString(fileItem.getString()).trim());
								ExpponoCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpType")) {
								ExpType.add(ExptypeCount, StrUtils.fString(fileItem.getString()).trim());
								ExptypeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("ExpenseAmount")) {
								ExpenseAmount.add(ExpenseamountCount, StrUtils.fString(fileItem.getString()).trim());
								ExpenseamountCount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("pdcstatus")) {
								pdcstatus.add(pdccount, StrUtils.fString(fileItem.getString()).trim());
								pdccount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
								bankcode.add(bankcodeCount, StrUtils.fString(fileItem.getString()).trim());
								bankcodeCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
								chequeno.add(chequenoCount, StrUtils.fString(fileItem.getString()).trim());
								chequenoCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
								chedate.add(chedateCount, StrUtils.fString(fileItem.getString()).trim());
								chedateCount++;
							}
						}
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
								chequeamount.add(chequeamountCount, StrUtils.fString(fileItem.getString()).trim());
								chequeamountCount++;
							}
						}
					}

					/* Get Transaction object */
					ut = DbBean.getUserTranaction();
					/* Begin Transaction */
					ut.begin();
					
					double billonlyamt = 0.0;
					double exponlyamt = 0.0;
					for (int i = 0; i < billHdrId.size(); i++) {
						billonlyamt = billonlyamt + Double.valueOf((String) amount.get(i));
					}
					for (int i = 0; i < ExpHdrId.size(); i++) {
						exponlyamt = exponlyamt + Double.valueOf((String) Expamount.get(i));
					}

					if (billonlyamt > 0 && exponlyamt > 0) {

						
						
						String eamountpaid = amountpaid;

						if (billHdrId.size() > 0) {

							amountpaid = String.valueOf(Double.valueOf(amountpaid) - exponlyamt);

							paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
							ExppaymentDetInfoList = new ArrayList<Hashtable<String, String>>();
							paymentAttachmentList = new ArrayList<Hashtable<String, String>>();

							Hashtable paymentHdr = new Hashtable();
							paymentHdr.put("PLANT", plant);
							paymentHdr.put("VENDNO", vendno);
							paymentHdr.put("AMOUNTPAID", amountpaid);
							paymentHdr.put("PAYMENT_DATE", paymentDate);
							paymentHdr.put("PAYMENT_MODE", paymentMode);
							paymentHdr.put("PAID_THROUGH", paidThrough);
							paymentHdr.put("REFERENCE", reference);
							// paymentHdr.put("BANK_BRANCH", bankBranch);
							paymentHdr.put("AMOUNTUFP", amountUfp);
							paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
							paymentHdr.put("BANK_CHARGE", bankCharge);
							paymentHdr.put("WO_AMOUNT", woamount);
							// paymentHdr.put("CHECQUE_NO", checqueNo);
							// paymentHdr.put("CHEQUE_DATE", chequedate);
							paymentHdr.put("NOTE", note);
							paymentHdr.put("CRAT", DateUtils.getDateTime());
							paymentHdr.put("CRBY", username);
							paymentHdr.put("UPAT", DateUtils.getDateTime());
							paymentHdr.put("CREDITAPPLYKEY", uuid);
							paymentHdr.put("ISPDCPROCESS", "0");
							paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
							paymentHdr.put("PROJECTID", projectid);
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										paymentHdr.put("ISPDCPROCESS", "1");
									} else {
										paymentHdr.put("ISPDCPROCESS", "0");
									}
								}

							}

							Double totalBillAmount = 0.00;
							for (int j = 0; j < amountCount; j++) {
								totalBillAmount += Double.parseDouble((String) amount.get(j));
							}

							int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
							payHdrId = Integer.toString(paymentHdrId);

							double pdaamountiff = 0.0;
							if (paymentHdrId > 0) {
								billpdcInfoList = new ArrayList<Hashtable<String, String>>();
								if (paidpdcstatus.equalsIgnoreCase("1")) {
									for (int i = 0; i < bankcode.size(); i++) {
										billpdcInfo = new Hashtable<String, String>();
										billpdcInfo.put("PLANT", plant);
										billpdcInfo.put("VENDNO", vendno);
										billpdcInfo.put("PAYMENTID", payHdrId);
										billpdcInfo.put("PAYMENT_DATE", paymentDate);
										billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
										billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
										billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
										billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
										billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
										billpdcInfo.put("CRAT", DateUtils.getDateTime());
										billpdcInfo.put("CRBY", username);
										if (pdcstatus.get(i).equals("1")) {
											billpdcInfo.put("STATUS", "NOT PROCESSED");
											pdaamountiff = pdaamountiff + Double.valueOf((String) chequeamount.get(i));
										} else {
											billpdcInfo.put("STATUS", "NOT APPLICABLE");
										}

										billpdcInfoList.add(billpdcInfo);
									}
									billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
								}
							}

							int explno = 0;
							if (paymentHdrId > 0) {
								if (billHdrId.size() > 0) {

									if (totalBillAmount > 0) {
										if (newadv.equalsIgnoreCase("NEW")) {
											double balceadd = Double.parseDouble(amountpaid) - totalBillAmount;
											String sbalceadd = String.valueOf(balceadd - pdaamountiff);
											paymentDetInfo = new Hashtable<String, String>();
											paymentDetInfo.put("PLANT", plant);
											paymentDetInfo.put("LNNO", "0");
											paymentDetInfo.put("AMOUNT", amountpaid);
											paymentDetInfo.put("BALANCE", sbalceadd);
											paymentDetInfo.put("PONO", ponoadv);
											paymentDetInfo.put("PAYHDRID", payHdrId);
											paymentDetInfo.put("BILLHDRID", "0");
											paymentDetInfo.put("EXPHDRID", "0");
											paymentDetInfo.put("TYPE", paytype);
											paymentDetInfo.put("CRAT", DateUtils.getDateTime());
											paymentDetInfo.put("CRBY", username);
											paymentDetInfo.put("UPAT", DateUtils.getDateTime());
											paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
											paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
											paymentDetInfoList.add(paymentDetInfo);
										}

										for (int i = 0; i < billHdrId.size(); i++) {
											int lnno = i + 1;
											paymentDetInfo = new Hashtable<String, String>();
											paymentDetInfo.put("PLANT", plant);
											if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
												paymentDetInfo.put("LNNO", "0");
												paymentDetInfo.put("AMOUNT", amountpaid);
												String detbal = String
														.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
												paymentDetInfo.put("BALANCE", detbal);
												paymentDetInfo.put("PONO", (String) pono.get(i));
												paymentDetInfo.put("PAYHDRID", payHdrId);
												paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
												paymentDetInfo.put("EXPHDRID", "0");
												paymentDetInfo.put("TYPE", (String) type.get(i));
												paymentDetInfo.put("CRAT", DateUtils.getDateTime());
												paymentDetInfo.put("CRBY", username);
												paymentDetInfo.put("UPAT", DateUtils.getDateTime());
												paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
												paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
												paymentDetInfoList.add(paymentDetInfo);
											} else {

												double amocheck = Double.parseDouble((String) amount.get(i));

												if (amocheck > 0) {
													paymentDetInfo.put("LNNO", Integer.toString(lnno));
													paymentDetInfo.put("AMOUNT", (String) amount.get(i));
													paymentDetInfo.put("BALANCE", "0");
													paymentDetInfo.put("PONO", (String) pono.get(i));
													paymentDetInfo.put("PAYHDRID", payHdrId);
													paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
													paymentDetInfo.put("EXPHDRID", "0");
													paymentDetInfo.put("TYPE", (String) type.get(i));
													paymentDetInfo.put("CRAT", DateUtils.getDateTime());
													paymentDetInfo.put("CRBY", username);
													paymentDetInfo.put("UPAT", DateUtils.getDateTime());
													paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
													paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
													paymentDetInfoList.add(paymentDetInfo);
													explno = lnno;
												}
											}

										}

									} else {
										paymentDetInfo = new Hashtable<String, String>();
										paymentDetInfo.put("PLANT", plant);
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
										paymentDetInfo.put("BALANCE", detbal);
										paymentDetInfo.put("PONO", "");
										paymentDetInfo.put("PAYHDRID", payHdrId);
										paymentDetInfo.put("BILLHDRID", "0");
										paymentDetInfo.put("EXPHDRID", "0");
										paymentDetInfo.put("TYPE", "ADVANCE");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										paymentDetInfo.put("CRAT", DateUtils.getDateTime());
										paymentDetInfo.put("CRBY", username);
										paymentDetInfo.put("UPAT", DateUtils.getDateTime());
										paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										paymentDetInfoList.add(paymentDetInfo);
									}

								} else {
									/*
									 * paymentDetInfo = new Hashtable<String, String>(); paymentDetInfo.put("PLANT",
									 * plant); paymentDetInfo.put("LNNO", "0"); paymentDetInfo.put("AMOUNT",
									 * amountpaid); String detbal=
									 * String.valueOf(Double.parseDouble(amountpaid)-pdaamountiff);
									 * paymentDetInfo.put("BALANCE", detbal); paymentDetInfo.put("PONO", "");
									 * paymentDetInfo.put("PAYHDRID", payHdrId); paymentDetInfo.put("BILLHDRID",
									 * "0"); paymentDetInfo.put("EXPHDRID", "0"); paymentDetInfo.put("TYPE",
									 * "ADVANCE"); paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									 * paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									 * paymentDetInfo.put("CRBY", username); paymentDetInfo.put("UPAT",
									 * DateUtils.getDateTime()); paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									 * paymentDetInfoList.add(paymentDetInfo);
									 */
								}

								if (paymentDetInfoList.size() > 0) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
									if (isAdded) {
										if (ExppaymentDetInfoList.size() > 0) {
											isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList,
													plant);
										}
									}
								} else {
									if (ExppaymentDetInfoList.size() > 0) {
										isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
									}
								}

								while (fileIterator.hasNext()) {
									FileItem fileItem = (FileItem) fileIterator.next();
									if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
										String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
										String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
										paymentAttachment = new Hashtable<String, String>();
										paymentAttachment.put("PLANT", plant);
										paymentAttachment.put("FILETYPE", fileItem.getContentType());
										paymentAttachment.put("FILENAME", fileName);
										paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
										paymentAttachment.put("FILEPATH", fileLocation);
										paymentAttachment.put("CRAT", DateUtils.getDateTime());
										paymentAttachment.put("CRBY", username);
										paymentAttachmentList.add(paymentAttachment);
									}
								}
								int attchSize = paymentAttachmentList.size();
								for (int i = 0; i < attchSize; i++) {
									paymentAttachment = new Hashtable<String, String>();
									paymentAttachment = paymentAttachmentList.get(i);
									paymentAttachment.put("PAYHDRID", payHdrId);
									paymentAttachmentInfoList.add(paymentAttachment);
								}

								if (isAdded) {
									if (paymentAttachmentInfoList.size() > 0)
										isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList,
												plant);

									String curency = StrUtils
											.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
											.trim();
									// Journal Entry
									JournalHeader journalHead = new JournalHeader();
									journalHead.setPLANT(plant);
									journalHead.setJOURNAL_DATE(paymentDate);
									journalHead.setJOURNAL_STATUS("PUBLISHED");
									journalHead.setJOURNAL_TYPE("Cash");
									journalHead.setCURRENCYID(curency);
									journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
									journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
									journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
									journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
									journalHead.setCRAT(DateUtils.getDateTime());
									journalHead.setCRBY(username);

									List<JournalDetail> journalDetails = new ArrayList<>();
									CoaDAO coaDAO = new CoaDAO();
									VendMstDAO vendorDAO = new VendMstDAO();
									JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
										JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
										coaJson1 = coaDAO.getCOAByName(plant,
												vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
									if (paidpdcstatus.equalsIgnoreCase("1")) {
										for (int i = 0; i < bankcode.size(); i++) {
											if (pdcstatus.get(i).equals("1")) {
												JournalDetail journalDetail = new JournalDetail();
												journalDetail.setPLANT(plant);
												JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
												System.out.println("Json" + coaJson.toString());
												journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
												journalDetail.setACCOUNT_NAME("PDC issued");
												if (!bankCharge.isEmpty()) {
													if (Double.parseDouble(bankCharge) > 0) {
														journalDetail.setCREDITS(
																Double.parseDouble((String) chequeamount.get(i))
																		+ Double.parseDouble(bankCharge));
													} else {
														journalDetail.setCREDITS(
																Double.parseDouble((String) chequeamount.get(i)));
													}
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
												journalDetail
														.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
												journalDetails.add(journalDetail);
											} else {
												JournalDetail journalDetail = new JournalDetail();
												journalDetail.setPLANT(plant);
												JSONObject coaJson = coaDAO.getCOAByName(plant,
														(String) bankcode.get(i));
												System.out.println("Json" + coaJson.toString());
												journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
												journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
												if (!bankCharge.isEmpty()) {
													if (Double.parseDouble(bankCharge) > 0) {
														journalDetail.setCREDITS(
																Double.parseDouble((String) chequeamount.get(i))
																		+ Double.parseDouble(bankCharge));
													} else {
														journalDetail.setCREDITS(
																Double.parseDouble((String) chequeamount.get(i)));
													}
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
												// journalDetail.setCREDITS(Double.parseDouble((String)
												// chequeamount.get(i)));
												journalDetail
														.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
												journalDetails.add(journalDetail);
											}
										}
									} else {
										JournalDetail journalDetail = new JournalDetail();
										journalDetail.setPLANT(plant);
										JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
										System.out.println("Json" + coaJson.toString());
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										journalDetail.setACCOUNT_NAME(paidThrough);
										journalDetail.setDESCRIPTION(jdesc+"-"+reference);
										journalDetail.setCREDITS(Double.parseDouble(amountpaid));
										journalDetails.add(journalDetail);
									}
									
									if(Double.parseDouble(woamount) > 0) {
										JournalDetail journalDetail_wo = new JournalDetail();
										journalDetail_wo.setPLANT(plant);
										JSONObject coaJsonwo = coaDAO.getCOAByName(plant, "Payable WO");
										
										if (coaJsonwo.isEmpty() || coaJsonwo.isNullObject()) {
	
										} else {
											journalDetail_wo.setACCOUNT_ID(Integer.parseInt(coaJsonwo.getString("id")));
											if (coaJsonwo.getString("account_name") != null) {
												journalDetail_wo.setACCOUNT_NAME(coaJsonwo.getString("account_name"));
											}
	
											journalDetail_wo.setDESCRIPTION(jdesc+"-"+reference);
											journalDetail_wo.setDEBITS(Double.parseDouble(woamount));
											journalDetails.add(journalDetail_wo);
										}
									}
									

									JournalDetail journalDetail_1 = new JournalDetail();
									journalDetail_1.setPLANT(plant);
									
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

									} else {
										journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
										if (coaJson1.getString("account_name") != null) {
											journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
										} else {
											journalDetail_1.setACCOUNT_NAME((String) vendno);
										}
										
										journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
										journalDetail_1.setDEBITS(Double.parseDouble(amountpaid)-Double.parseDouble(woamount));
										journalDetails.add(journalDetail_1);
									}
									if (!bankCharge.isEmpty()) {
										Double bankChargeFrom = Double.parseDouble(bankCharge);
										if (bankChargeFrom > 0) {
											JournalDetail journalDetail_2 = new JournalDetail();
											journalDetail_2.setPLANT(plant);
											journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
											if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

											} else {
												journalDetail_2
														.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
												journalDetail_2.setACCOUNT_NAME("Bank charges");
												journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
												journalDetails.add(journalDetail_2);
											}
										}
									}

									Journal journal = new Journal();
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);
									JournalService journalService = new JournalEntry();
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
								if (isAdded) {
									Hashtable htMovHis = new Hashtable();
									htMovHis.clear();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
									// htMovHis.put(IDBConstants.ITEM, paymentMode);
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
									htMovHis.put(IDBConstants.CREATED_BY, username);
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMovHis.put("REMARKS", reference);
									isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}

								if (isAdded) {
									if (paidpdcstatus.equalsIgnoreCase("1")) {
										for (int i = 0; i < bankcode.size(); i++) {

											Hashtable htMovHis = new Hashtable();
											htMovHis.clear();
											htMovHis.put(IDBConstants.PLANT, plant);
											htMovHis.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS);
											htMovHis.put(IDBConstants.TRAN_DATE,
													DateUtils.getDateinyyyy_mm_dd(paymentDate));
											htMovHis.put("RECID", "");
											htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
											htMovHis.put(IDBConstants.CREATED_BY, username);
											htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											if (pdcstatus.get(i).equals("1")) {
												htMovHis.put("REMARKS",
														"PDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
																+ "," + chedate.get(i) + "," + chequeamount.get(i));
											} else {
												htMovHis.put("REMARKS",
														"CDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
																+ "," + chedate.get(i) + "," + chequeamount.get(i));
											}

											isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
										}
									}
								}
							}
							if (isAdded) {
								for (int i = 0; i < billHdrId.size(); i++) {
									double billamountinit = Double.parseDouble((String) billAmount.get(i));
									double billamountpaid = Double.parseDouble((String) amount.get(i));
									String query = "";
									if (billamountpaid > 0.00) {
										if (billamountpaid < billamountinit) {
											query = " SET BILL_STATUS='Partially Paid'";
										} else if (billamountpaid == billamountinit) {
											query = " SET BILL_STATUS='Paid'";
										}

										String upid = (String) billHdrId.get(i);

										Hashtable htCondition = new Hashtable();
										htCondition.put("ID", upid);
										htCondition.put("PLANT", plant);
										if (!upid.equals(""))
											isAdded = billDao.updateBillHdr(query, htCondition, "");
									}
								}
							}
						}

						if (ExpHdrId.size() > 0) {

							amountpaid = String.valueOf(Double.valueOf(eamountpaid) - billonlyamt);

							UUID uniqueKey = UUID.randomUUID();

							List checkexpapplied = new BillPaymentDAO().getpaydetbykeyandexpense(uuid, plant);

							if (checkexpapplied.size() > 0) {
								Hashtable htdetup = new Hashtable();
								htdetup.put("PLANT", plant);
								htdetup.put("BILLHDRID", "0");
								htdetup.put("CREDITAPPLYKEY", uuid);
								new BillPaymentDAO().updatePaymentDet("SET CREDITAPPLYKEY='" + uniqueKey + "'", htdetup,
										"");
							}

							paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
							ExppaymentDetInfoList = new ArrayList<Hashtable<String, String>>();
							paymentAttachmentList = new ArrayList<Hashtable<String, String>>();

							Hashtable paymentHdr = new Hashtable();
							paymentHdr.put("PLANT", plant);
							paymentHdr.put("VENDNO", vendno);
							paymentHdr.put("AMOUNTPAID", amountpaid);
							paymentHdr.put("PAYMENT_DATE", paymentDate);
							paymentHdr.put("PAYMENT_MODE", paymentMode);
							paymentHdr.put("PAID_THROUGH", paidThrough);
							paymentHdr.put("REFERENCE", reference);
							// paymentHdr.put("BANK_BRANCH", bankBranch);
							paymentHdr.put("AMOUNTUFP", amountUfp);
							paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
							paymentHdr.put("BANK_CHARGE", bankCharge);
							paymentHdr.put("WO_AMOUNT", woamount);
							// paymentHdr.put("CHECQUE_NO", checqueNo);
							// paymentHdr.put("CHEQUE_DATE", chequedate);
							paymentHdr.put("NOTE", note);
							paymentHdr.put("CRAT", DateUtils.getDateTime());
							paymentHdr.put("CRBY", username);
							paymentHdr.put("UPAT", DateUtils.getDateTime());
							paymentHdr.put("CREDITAPPLYKEY", uniqueKey.toString());
							paymentHdr.put("ISPDCPROCESS", "0");
							paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
							paymentHdr.put("PROJECTID", projectid);
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									if (pdcstatus.get(i).equals("1")) {
										paymentHdr.put("ISPDCPROCESS", "1");
									} else {
										paymentHdr.put("ISPDCPROCESS", "0");
									}
								}

							}
							/*
							 * Get Transaction object ut = DbBean.getUserTranaction(); Begin Transaction
							 * ut.begin();
							 */

							Double totalBillAmount = 0.00;
							for (int j = 0; j < amountCount; j++) {
								totalBillAmount += Double.parseDouble((String) amount.get(j));
							}

							int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
							payHdrId = Integer.toString(paymentHdrId);

							double pdaamountiff = 0.0;
							if (paymentHdrId > 0) {
								billpdcInfoList = new ArrayList<Hashtable<String, String>>();
								/*
								 * if(paidpdcstatus.equalsIgnoreCase("1")) { for(int i =0 ; i < bankcode.size()
								 * ; i++){ billpdcInfo = new Hashtable<String, String>();
								 * billpdcInfo.put("PLANT", plant); billpdcInfo.put("VENDNO", vendno);
								 * billpdcInfo.put("PAYMENTID", payHdrId); billpdcInfo.put("PAYMENT_DATE",
								 * paymentDate); billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
								 * billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
								 * billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
								 * billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
								 * billpdcInfo.put("ISPDC", (String) pdcstatus.get(i)); billpdcInfo.put("CRAT",
								 * DateUtils.getDateTime()); billpdcInfo.put("CRBY", username);
								 * if(pdcstatus.get(i).equals("1")) { billpdcInfo.put("STATUS",
								 * "NOT PROCESSED"); pdaamountiff = pdaamountiff + Double.valueOf((String)
								 * chequeamount.get(i)); }else { billpdcInfo.put("STATUS", "NOT APPLICABLE"); }
								 * 
								 * billpdcInfoList.add(billpdcInfo); }
								 * billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant); }
								 */
							}

							int explno = 0;
							if (paymentHdrId > 0) {

								for (int i = 0; i < ExpHdrId.size(); i++) {
									explno = explno + 1;
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);

									double amocheck = Double.parseDouble((String) Expamount.get(i));

									if (amocheck > 0) {
										paymentDetInfo.put("LNNO", Integer.toString(explno));
										paymentDetInfo.put("AMOUNT", (String) Expamount.get(i));
										paymentDetInfo.put("BALANCE", "0");
										paymentDetInfo.put("PONO", (String) ExpPono.get(i));
										paymentDetInfo.put("PAYHDRID", payHdrId);
										paymentDetInfo.put("BILLHDRID", "0");
										paymentDetInfo.put("EXPHDRID", (String) ExpHdrId.get(i));
										paymentDetInfo.put("TYPE", (String) ExpType.get(i));
										paymentDetInfo.put("CRAT", DateUtils.getDateTime());
										paymentDetInfo.put("CRBY", username);
										paymentDetInfo.put("UPAT", DateUtils.getDateTime());
										paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										ExppaymentDetInfoList.add(paymentDetInfo);
									}
								}

								if (paymentDetInfoList.size() > 0) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
									if (isAdded) {
										if (ExppaymentDetInfoList.size() > 0) {
											isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList,
													plant);
										}
									}
								} else {
									if (ExppaymentDetInfoList.size() > 0) {
										isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
									}
								}

								while (fileIterator.hasNext()) {
									FileItem fileItem = (FileItem) fileIterator.next();
									if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
										String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
										String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
										paymentAttachment = new Hashtable<String, String>();
										paymentAttachment.put("PLANT", plant);
										paymentAttachment.put("FILETYPE", fileItem.getContentType());
										paymentAttachment.put("FILENAME", fileName);
										paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
										paymentAttachment.put("FILEPATH", fileLocation);
										paymentAttachment.put("CRAT", DateUtils.getDateTime());
										paymentAttachment.put("CRBY", username);
										paymentAttachmentList.add(paymentAttachment);
									}
								}
								int attchSize = paymentAttachmentList.size();
								for (int i = 0; i < attchSize; i++) {
									paymentAttachment = new Hashtable<String, String>();
									paymentAttachment = paymentAttachmentList.get(i);
									paymentAttachment.put("PAYHDRID", payHdrId);
									paymentAttachmentInfoList.add(paymentAttachment);
								}

								if (isAdded) {
									if (paymentAttachmentInfoList.size() > 0)
										isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList,
												plant);

									String curency = StrUtils
											.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
											.trim();
									// Journal Entry
									JournalHeader journalHead = new JournalHeader();
									journalHead.setPLANT(plant);
									journalHead.setJOURNAL_DATE(paymentDate);
									journalHead.setJOURNAL_STATUS("PUBLISHED");
									journalHead.setJOURNAL_TYPE("Cash");
									journalHead.setCURRENCYID(curency);
									journalHead.setTRANSACTION_TYPE("EXPENSEPAYMENT");
									journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
									journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
									journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
									journalHead.setCRAT(DateUtils.getDateTime());
									journalHead.setCRBY(username);

									List<JournalDetail> journalDetails = new ArrayList<>();
									CoaDAO coaDAO = new CoaDAO();
									VendMstDAO vendorDAO = new VendMstDAO();
									JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
										JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
										coaJson1 = coaDAO.getCOAByName(plant,
												vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
									/*
									 * if(paidpdcstatus.equalsIgnoreCase("1")) { for(int i =0 ; i < bankcode.size()
									 * ; i++){ if(pdcstatus.get(i).equals("1")) { JournalDetail journalDetail=new
									 * JournalDetail(); journalDetail.setPLANT(plant); JSONObject
									 * coaJson=coaDAO.getCOAByName(plant, "PDC issued");
									 * System.out.println("Json"+coaJson.toString());
									 * journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									 * journalDetail.setACCOUNT_NAME("PDC issued"); if(!bankCharge.isEmpty()) {
									 * if(Double.parseDouble(bankCharge)>0) {
									 * journalDetail.setCREDITS(Double.parseDouble((String)
									 * chequeamount.get(i))+Double.parseDouble(bankCharge)); } else {
									 * journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i))); }
									 * } else { journalDetail.setCREDITS(Double.parseDouble((String)
									 * chequeamount.get(i))); }
									 * journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									 * journalDetails.add(journalDetail); } else { JournalDetail journalDetail=new
									 * JournalDetail(); journalDetail.setPLANT(plant); JSONObject
									 * coaJson=coaDAO.getCOAByName(plant, (String) bankcode.get(i));
									 * System.out.println("Json"+coaJson.toString());
									 * journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									 * journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
									 * if(!bankCharge.isEmpty()) { if(Double.parseDouble(bankCharge)>0) {
									 * journalDetail.setCREDITS(Double.parseDouble((String)
									 * chequeamount.get(i))+Double.parseDouble(bankCharge)); } else {
									 * journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i))); }
									 * } else { journalDetail.setCREDITS(Double.parseDouble((String)
									 * chequeamount.get(i))); }
									 * //journalDetail.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
									 * journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									 * journalDetails.add(journalDetail); } } } else {
									 */
									JournalDetail journalDetail = new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
									System.out.println("Json" + coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME(paidThrough);
									journalDetail.setDESCRIPTION(jdesc+"-"+reference);
									journalDetail.setCREDITS(Double.parseDouble(amountpaid));
									journalDetails.add(journalDetail);
									/* } */
									
									if(Double.parseDouble(woamount) > 0) {
										JournalDetail journalDetail_wo = new JournalDetail();
										journalDetail_wo.setPLANT(plant);
										JSONObject coaJsonwo = coaDAO.getCOAByName(plant, "Payable WO");
										
										if (coaJsonwo.isEmpty() || coaJsonwo.isNullObject()) {
	
										} else {
											journalDetail_wo.setACCOUNT_ID(Integer.parseInt(coaJsonwo.getString("id")));
											if (coaJsonwo.getString("account_name") != null) {
												journalDetail_wo.setACCOUNT_NAME(coaJsonwo.getString("account_name"));
											}
	
											journalDetail_wo.setDESCRIPTION(jdesc+"-"+reference);
											journalDetail_wo.setDEBITS(Double.parseDouble(woamount));
											journalDetails.add(journalDetail_wo);
										}
									}

									JournalDetail journalDetail_1 = new JournalDetail();
									journalDetail_1.setPLANT(plant);
									
									if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

									} else {
										journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
										if (coaJson1.getString("account_name") != null) {
											journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
										} else {
											journalDetail_1.setACCOUNT_NAME((String) vendno);
										}
										
										journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
										journalDetail_1.setDEBITS(Double.parseDouble(amountpaid)-Double.parseDouble(woamount));
										journalDetails.add(journalDetail_1);
									}
									if (!bankCharge.isEmpty()) {
										Double bankChargeFrom = Double.parseDouble(bankCharge);
										if (bankChargeFrom > 0) {
											JournalDetail journalDetail_2 = new JournalDetail();
											journalDetail_2.setPLANT(plant);
											journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
											JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
											if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

											} else {
												journalDetail_2
														.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
												journalDetail_2.setACCOUNT_NAME("Bank charges");
												journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
												journalDetails.add(journalDetail_2);
											}
										}
									}

									Journal journal = new Journal();
									journal.setJournalHeader(journalHead);
									journal.setJournalDetails(journalDetails);
									JournalService journalService = new JournalEntry();
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
									jhtMovHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
											journal.getJournalHeader().getTRANSACTION_TYPE() + " "
													+ journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS", "");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
								if (isAdded) {
									Hashtable htMovHis = new Hashtable();
									htMovHis.clear();
									htMovHis.put(IDBConstants.PLANT, plant);
									htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
									htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
									// htMovHis.put(IDBConstants.ITEM, paymentMode);
									htMovHis.put("RECID", "");
									htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
									htMovHis.put(IDBConstants.CREATED_BY, username);
									htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMovHis.put("REMARKS", reference);
									isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}

								/*
								 * if(isAdded) { if(paidpdcstatus.equalsIgnoreCase("1")) { for(int i =0 ; i <
								 * bankcode.size() ; i++){
								 * 
								 * Hashtable htMovHis = new Hashtable(); htMovHis.clear();
								 * htMovHis.put(IDBConstants.PLANT, plant); htMovHis.put("DIRTYPE",
								 * TransactionConstants.CHEQUE_DETAILS); htMovHis.put(IDBConstants.TRAN_DATE,
								 * DateUtils.getDateinyyyy_mm_dd(paymentDate)); htMovHis.put("RECID", "");
								 * htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
								 * htMovHis.put(IDBConstants.CREATED_BY, username);
								 * htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								 * if(pdcstatus.get(i).equals("1")) { htMovHis.put("REMARKS",
								 * "PDC-"+vendno+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+
								 * chequeamount.get(i)); }else { htMovHis.put("REMARKS",
								 * "CDC-"+vendno+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+
								 * chequeamount.get(i)); }
								 * 
								 * isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS } } }
								 */
							}
							/*
							 * if(isAdded) { for(int i =0 ; i < billHdrId.size() ; i++){ double
							 * billamountinit=Double.parseDouble((String) billAmount.get(i)); double
							 * billamountpaid=Double.parseDouble((String) amount.get(i)); String query="";
							 * if(billamountpaid>0.00) { if(billamountpaid<billamountinit) { query =
							 * " SET BILL_STATUS='Partially Paid'"; } else if(billamountpaid ==
							 * billamountinit) { query = " SET BILL_STATUS='Paid'"; }
							 * 
							 * String upid=(String) billHdrId.get(i);
							 * 
							 * Hashtable htCondition = new Hashtable(); htCondition.put("ID", upid);
							 * htCondition.put("PLANT", plant); if(!upid.equals("")) isAdded =
							 * billDao.updateBillHdr(query, htCondition, ""); } } }
							 */
						}

					} else if (billonlyamt > 0) {
						paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
						ExppaymentDetInfoList = new ArrayList<Hashtable<String, String>>();
						paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
						Hashtable paymentHdr = new Hashtable();
						paymentHdr.put("PLANT", plant);
						paymentHdr.put("VENDNO", vendno);
						paymentHdr.put("AMOUNTPAID", amountpaid);
						paymentHdr.put("PAYMENT_DATE", paymentDate);
						paymentHdr.put("PAYMENT_MODE", paymentMode);
						paymentHdr.put("PAID_THROUGH", paidThrough);
						paymentHdr.put("REFERENCE", reference);
						// paymentHdr.put("BANK_BRANCH", bankBranch);
						paymentHdr.put("AMOUNTUFP", amountUfp);
						paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
						paymentHdr.put("BANK_CHARGE", bankCharge);
						paymentHdr.put("WO_AMOUNT", woamount);
						// paymentHdr.put("CHECQUE_NO", checqueNo);
						// paymentHdr.put("CHEQUE_DATE", chequedate);
						paymentHdr.put("NOTE", note);
						paymentHdr.put("CRAT", DateUtils.getDateTime());
						paymentHdr.put("CRBY", username);
						paymentHdr.put("UPAT", DateUtils.getDateTime());
						paymentHdr.put("CREDITAPPLYKEY", uuid);
						paymentHdr.put("ISPDCPROCESS", "0");
						paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
						paymentHdr.put("PROJECTID", projectid);
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							for (int i = 0; i < bankcode.size(); i++) {
								if (pdcstatus.get(i).equals("1")) {
									paymentHdr.put("ISPDCPROCESS", "1");
								} else {
									paymentHdr.put("ISPDCPROCESS", "0");
								}
							}

						}
						/* Get Transaction object */
						/*
						 * ut = DbBean.getUserTranaction(); Begin Transaction ut.begin();
						 */

						Double totalBillAmount = 0.00;
						for (int j = 0; j < amountCount; j++) {
							totalBillAmount += Double.parseDouble((String) amount.get(j));
						}

						int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
						payHdrId = Integer.toString(paymentHdrId);

						double pdaamountiff = 0.0;
						if (paymentHdrId > 0) {
							billpdcInfoList = new ArrayList<Hashtable<String, String>>();
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									billpdcInfo = new Hashtable<String, String>();
									billpdcInfo.put("PLANT", plant);
									billpdcInfo.put("VENDNO", vendno);
									billpdcInfo.put("PAYMENTID", payHdrId);
									billpdcInfo.put("PAYMENT_DATE", paymentDate);
									billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
									billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
									billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
									billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
									billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
									billpdcInfo.put("CRAT", DateUtils.getDateTime());
									billpdcInfo.put("CRBY", username);
									if (pdcstatus.get(i).equals("1")) {
										billpdcInfo.put("STATUS", "NOT PROCESSED");
										pdaamountiff = pdaamountiff + Double.valueOf((String) chequeamount.get(i));
									} else {
										billpdcInfo.put("STATUS", "NOT APPLICABLE");
									}

									billpdcInfoList.add(billpdcInfo);
								}
								billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
							}
						}

						int explno = 0;
						if (paymentHdrId > 0) {
							if (billHdrId.size() > 0) {

								if (totalBillAmount > 0) {
									if (newadv.equalsIgnoreCase("NEW")) {
										double balceadd = Double.parseDouble(amountpaid) - totalBillAmount;
										String sbalceadd = String.valueOf(balceadd - pdaamountiff);
										paymentDetInfo = new Hashtable<String, String>();
										paymentDetInfo.put("PLANT", plant);
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										paymentDetInfo.put("BALANCE", sbalceadd);
										paymentDetInfo.put("PONO", ponoadv);
										paymentDetInfo.put("PAYHDRID", payHdrId);
										paymentDetInfo.put("BILLHDRID", "0");
										paymentDetInfo.put("EXPHDRID", "0");
										paymentDetInfo.put("TYPE", paytype);
										paymentDetInfo.put("CRAT", DateUtils.getDateTime());
										paymentDetInfo.put("CRBY", username);
										paymentDetInfo.put("UPAT", DateUtils.getDateTime());
										paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										paymentDetInfoList.add(paymentDetInfo);
									}

									for (int i = 0; i < billHdrId.size(); i++) {
										int lnno = i + 1;
										paymentDetInfo = new Hashtable<String, String>();
										paymentDetInfo.put("PLANT", plant);
										if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
											paymentDetInfo.put("LNNO", "0");
											paymentDetInfo.put("AMOUNT", amountpaid);
											String detbal = String
													.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
											paymentDetInfo.put("BALANCE", detbal);
											paymentDetInfo.put("PONO", (String) pono.get(i));
											paymentDetInfo.put("PAYHDRID", payHdrId);
											paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
											paymentDetInfo.put("EXPHDRID", "0");
											paymentDetInfo.put("TYPE", (String) type.get(i));
											paymentDetInfo.put("CRAT", DateUtils.getDateTime());
											paymentDetInfo.put("CRBY", username);
											paymentDetInfo.put("UPAT", DateUtils.getDateTime());
											paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
											paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
											paymentDetInfoList.add(paymentDetInfo);
										} else {

											double amocheck = Double.parseDouble((String) amount.get(i));

											if (amocheck > 0) {
												paymentDetInfo.put("LNNO", Integer.toString(lnno));
												paymentDetInfo.put("AMOUNT", (String) amount.get(i));
												paymentDetInfo.put("BALANCE", "0");
												paymentDetInfo.put("PONO", (String) pono.get(i));
												paymentDetInfo.put("PAYHDRID", payHdrId);
												paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
												paymentDetInfo.put("EXPHDRID", "0");
												paymentDetInfo.put("TYPE", (String) type.get(i));
												paymentDetInfo.put("CRAT", DateUtils.getDateTime());
												paymentDetInfo.put("CRBY", username);
												paymentDetInfo.put("UPAT", DateUtils.getDateTime());
												paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
												paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
												paymentDetInfoList.add(paymentDetInfo);
												explno = lnno;
											}
										}

									}

								} else {
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									paymentDetInfo.put("LNNO", "0");
									paymentDetInfo.put("AMOUNT", amountpaid);
									String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
									paymentDetInfo.put("BALANCE", detbal);
									paymentDetInfo.put("PONO", "");
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", "0");
									paymentDetInfo.put("EXPHDRID", "0");
									paymentDetInfo.put("TYPE", "ADVANCE");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									paymentDetInfo.put("CRBY", username);
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfoList.add(paymentDetInfo);
								}

							} else {
								/*
								 * paymentDetInfo = new Hashtable<String, String>(); paymentDetInfo.put("PLANT",
								 * plant); paymentDetInfo.put("LNNO", "0"); paymentDetInfo.put("AMOUNT",
								 * amountpaid); String detbal=
								 * String.valueOf(Double.parseDouble(amountpaid)-pdaamountiff);
								 * paymentDetInfo.put("BALANCE", detbal); paymentDetInfo.put("PONO", "");
								 * paymentDetInfo.put("PAYHDRID", payHdrId); paymentDetInfo.put("BILLHDRID",
								 * "0"); paymentDetInfo.put("EXPHDRID", "0"); paymentDetInfo.put("TYPE",
								 * "ADVANCE"); paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								 * paymentDetInfo.put("CRAT", DateUtils.getDateTime());
								 * paymentDetInfo.put("CRBY", username); paymentDetInfo.put("UPAT",
								 * DateUtils.getDateTime()); paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
								 * paymentDetInfoList.add(paymentDetInfo);
								 */
							}

							for (int i = 0; i < ExpHdrId.size(); i++) {
								explno = explno + 1;
								paymentDetInfo = new Hashtable<String, String>();
								paymentDetInfo.put("PLANT", plant);

								double amocheck = Double.parseDouble((String) Expamount.get(i));

								if (amocheck > 0) {
									paymentDetInfo.put("LNNO", Integer.toString(explno));
									paymentDetInfo.put("AMOUNT", (String) Expamount.get(i));
									paymentDetInfo.put("BALANCE", "0");
									paymentDetInfo.put("PONO", (String) ExpPono.get(i));
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", "0");
									paymentDetInfo.put("EXPHDRID", (String) ExpHdrId.get(i));
									paymentDetInfo.put("TYPE", (String) ExpType.get(i));
									paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									paymentDetInfo.put("CRBY", username);
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									ExppaymentDetInfoList.add(paymentDetInfo);
								}
							}

							if (paymentDetInfoList.size() > 0) {
								isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
								if (isAdded) {
									if (ExppaymentDetInfoList.size() > 0) {
										isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
									}
								}
							} else {
								if (ExppaymentDetInfoList.size() > 0) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
								}
							}

							while (fileIterator.hasNext()) {
								FileItem fileItem = (FileItem) fileIterator.next();
								if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
									String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
									String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
									paymentAttachment = new Hashtable<String, String>();
									paymentAttachment.put("PLANT", plant);
									paymentAttachment.put("FILETYPE", fileItem.getContentType());
									paymentAttachment.put("FILENAME", fileName);
									paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
									paymentAttachment.put("FILEPATH", fileLocation);
									paymentAttachment.put("CRAT", DateUtils.getDateTime());
									paymentAttachment.put("CRBY", username);
									paymentAttachmentList.add(paymentAttachment);
								}
							}
							int attchSize = paymentAttachmentList.size();
							for (int i = 0; i < attchSize; i++) {
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment = paymentAttachmentList.get(i);
								paymentAttachment.put("PAYHDRID", payHdrId);
								paymentAttachmentInfoList.add(paymentAttachment);
							}

							if (isAdded) {
								if (paymentAttachmentInfoList.size() > 0)
									isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

								String curency = StrUtils
										.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
								// Journal Entry
								JournalHeader journalHead = new JournalHeader();
								journalHead.setPLANT(plant);
								journalHead.setJOURNAL_DATE(paymentDate);
								journalHead.setJOURNAL_STATUS("PUBLISHED");
								journalHead.setJOURNAL_TYPE("Cash");
								journalHead.setCURRENCYID(curency);
								journalHead.setTRANSACTION_TYPE("PURCHASEPAYMENT");
								journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
								journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
								journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
								journalHead.setCRAT(DateUtils.getDateTime());
								journalHead.setCRBY(username);

								List<JournalDetail> journalDetails = new ArrayList<>();
								CoaDAO coaDAO = new CoaDAO();
								VendMstDAO vendorDAO = new VendMstDAO();
								JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
								if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
									JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
									coaJson1 = coaDAO.getCOAByName(plant,
											vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
									jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
								}
								if (paidpdcstatus.equalsIgnoreCase("1")) {
									for (int i = 0; i < bankcode.size(); i++) {
										if (pdcstatus.get(i).equals("1")) {
											JournalDetail journalDetail = new JournalDetail();
											journalDetail.setPLANT(plant);
											JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
											System.out.println("Json" + coaJson.toString());
											journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
											journalDetail.setACCOUNT_NAME("PDC issued");
											if (!bankCharge.isEmpty()) {
												if (Double.parseDouble(bankCharge) > 0) {
													journalDetail
															.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																	+ Double.parseDouble(bankCharge));
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
											journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
											journalDetails.add(journalDetail);
										} else {
											JournalDetail journalDetail = new JournalDetail();
											journalDetail.setPLANT(plant);
											JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
											System.out.println("Json" + coaJson.toString());
											journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
											journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
											if (!bankCharge.isEmpty()) {
												if (Double.parseDouble(bankCharge) > 0) {
													journalDetail
															.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																	+ Double.parseDouble(bankCharge));
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
											// journalDetail.setCREDITS(Double.parseDouble((String)
											// chequeamount.get(i)));
											journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
											journalDetails.add(journalDetail);
										}
									}
								} else {
									JournalDetail journalDetail = new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
									System.out.println("Json" + coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME(paidThrough);
									journalDetail.setDESCRIPTION(jdesc+"-"+reference);
									journalDetail.setCREDITS(Double.parseDouble(amountpaid));
									journalDetails.add(journalDetail);
								}
								
								if(Double.parseDouble(woamount) > 0) {
									JournalDetail journalDetail_wo = new JournalDetail();
									journalDetail_wo.setPLANT(plant);
									JSONObject coaJsonwo = coaDAO.getCOAByName(plant, "Payable WO");
									
									if (coaJsonwo.isEmpty() || coaJsonwo.isNullObject()) {

									} else {
										journalDetail_wo.setACCOUNT_ID(Integer.parseInt(coaJsonwo.getString("id")));
										if (coaJsonwo.getString("account_name") != null) {
											journalDetail_wo.setACCOUNT_NAME(coaJsonwo.getString("account_name"));
										}

										journalDetail_wo.setDESCRIPTION(jdesc+"-"+reference);
										journalDetail_wo.setDEBITS(Double.parseDouble(woamount));
										journalDetails.add(journalDetail_wo);
									}
								}

								JournalDetail journalDetail_1 = new JournalDetail();
								journalDetail_1.setPLANT(plant);
								
								if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

								} else {
									journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
									if (coaJson1.getString("account_name") != null) {
										journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
									} else {
										journalDetail_1.setACCOUNT_NAME((String) vendno);
									}
									
									journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
									journalDetail_1.setDEBITS(Double.parseDouble(amountpaid)-Double.parseDouble(woamount));
									journalDetails.add(journalDetail_1);
								}
								if (!bankCharge.isEmpty()) {
									Double bankChargeFrom = Double.parseDouble(bankCharge);
									if (bankChargeFrom > 0) {
										JournalDetail journalDetail_2 = new JournalDetail();
										journalDetail_2.setPLANT(plant);
										journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
										JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
										if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

										} else {
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("Bank charges");
											journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
											journalDetails.add(journalDetail_2);
										}
									}
								}

								Journal journal = new Journal();
								journal.setJournalHeader(journalHead);
								journal.setJournalDetails(journalDetails);
								JournalService journalService = new JournalEntry();
								journalService.addJournal(journal, username);
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
								jhtMovHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
										journal.getJournalHeader().getTRANSACTION_TYPE() + " "
												+ journal.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS", "");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}
							if (isAdded) {
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
								// htMovHis.put(IDBConstants.ITEM, paymentMode);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
								htMovHis.put(IDBConstants.CREATED_BY, username);
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS", reference);
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}

							if (isAdded) {
								if (paidpdcstatus.equalsIgnoreCase("1")) {
									for (int i = 0; i < bankcode.size(); i++) {

										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS);
										htMovHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(paymentDate));
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
										htMovHis.put(IDBConstants.CREATED_BY, username);
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										if (pdcstatus.get(i).equals("1")) {
											htMovHis.put("REMARKS",
													"PDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
															+ "," + chedate.get(i) + "," + chequeamount.get(i));
										} else {
											htMovHis.put("REMARKS",
													"CDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
															+ "," + chedate.get(i) + "," + chequeamount.get(i));
										}

										isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
									}
								}
							}
						}
						if (isAdded) {
							for (int i = 0; i < billHdrId.size(); i++) {
								double billamountinit = Double.parseDouble((String) billAmount.get(i));
								double billamountpaid = Double.parseDouble((String) amount.get(i));
								String query = "";
								if (billamountpaid > 0.00) {
									if (billamountpaid < billamountinit) {
										query = " SET BILL_STATUS='Partially Paid'";
									} else if (billamountpaid == billamountinit) {
										query = " SET BILL_STATUS='Paid'";
									}

									String upid = (String) billHdrId.get(i);

									Hashtable htCondition = new Hashtable();
									htCondition.put("ID", upid);
									htCondition.put("PLANT", plant);
									if (!upid.equals(""))
										isAdded = billDao.updateBillHdr(query, htCondition, "");
								}
							}
						}
					} else if (exponlyamt > 0) {
						paymentDetInfoList = new ArrayList<Hashtable<String, String>>();
						ExppaymentDetInfoList = new ArrayList<Hashtable<String, String>>();
						paymentAttachmentList = new ArrayList<Hashtable<String, String>>();
						Hashtable paymentHdr = new Hashtable();
						paymentHdr.put("PLANT", plant);
						paymentHdr.put("VENDNO", vendno);
						paymentHdr.put("AMOUNTPAID", amountpaid);
						paymentHdr.put("PAYMENT_DATE", paymentDate);
						paymentHdr.put("PAYMENT_MODE", paymentMode);
						paymentHdr.put("PAID_THROUGH", paidThrough);
						paymentHdr.put("REFERENCE", reference);
						// paymentHdr.put("BANK_BRANCH", bankBranch);
						paymentHdr.put("AMOUNTUFP", amountUfp);
						paymentHdr.put("AMOUNTREFUNDED", amountRefunded);
						paymentHdr.put("BANK_CHARGE", bankCharge);
						// paymentHdr.put("CHECQUE_NO", checqueNo);
						// paymentHdr.put("CHEQUE_DATE", chequedate);
						paymentHdr.put("NOTE", note);
						paymentHdr.put("CRAT", DateUtils.getDateTime());
						paymentHdr.put("CRBY", username);
						paymentHdr.put("UPAT", DateUtils.getDateTime());
						paymentHdr.put("CREDITAPPLYKEY", uuid);
						paymentHdr.put("ISPDCPROCESS", "0");
						paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
						paymentHdr.put("PROJECTID", projectid);
						if (paidpdcstatus.equalsIgnoreCase("1")) {
							for (int i = 0; i < bankcode.size(); i++) {
								if (pdcstatus.get(i).equals("1")) {
									paymentHdr.put("ISPDCPROCESS", "1");
								} else {
									paymentHdr.put("ISPDCPROCESS", "0");
								}
							}

						}
						/* Get Transaction object */
						/*
						 * ut = DbBean.getUserTranaction(); Begin Transaction ut.begin();
						 */

						Double totalBillAmount = 0.00;
						for (int j = 0; j < amountCount; j++) {
							totalBillAmount += Double.parseDouble((String) amount.get(j));
						}

						int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
						payHdrId = Integer.toString(paymentHdrId);

						double pdaamountiff = 0.0;
						if (paymentHdrId > 0) {
							billpdcInfoList = new ArrayList<Hashtable<String, String>>();
							if (paidpdcstatus.equalsIgnoreCase("1")) {
								for (int i = 0; i < bankcode.size(); i++) {
									billpdcInfo = new Hashtable<String, String>();
									billpdcInfo.put("PLANT", plant);
									billpdcInfo.put("VENDNO", vendno);
									billpdcInfo.put("PAYMENTID", payHdrId);
									billpdcInfo.put("PAYMENT_DATE", paymentDate);
									billpdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
									billpdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
									billpdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
									billpdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
									billpdcInfo.put("ISPDC", (String) pdcstatus.get(i));
									billpdcInfo.put("CRAT", DateUtils.getDateTime());
									billpdcInfo.put("CRBY", username);
									if (pdcstatus.get(i).equals("1")) {
										billpdcInfo.put("STATUS", "NOT PROCESSED");
										pdaamountiff = pdaamountiff + Double.valueOf((String) chequeamount.get(i));
									} else {
										billpdcInfo.put("STATUS", "NOT APPLICABLE");
									}

									billpdcInfoList.add(billpdcInfo);
								}
								billPaymentUtil.addMultiplePaymentpdc(billpdcInfoList, plant);
							}
						}

						int explno = 0;
						if (paymentHdrId > 0) {
							if (billHdrId.size() > 0) {

								if (totalBillAmount > 0) {
									if (newadv.equalsIgnoreCase("NEW")) {
										double balceadd = Double.parseDouble(amountpaid) - totalBillAmount;
										String sbalceadd = String.valueOf(balceadd - pdaamountiff);
										paymentDetInfo = new Hashtable<String, String>();
										paymentDetInfo.put("PLANT", plant);
										paymentDetInfo.put("LNNO", "0");
										paymentDetInfo.put("AMOUNT", amountpaid);
										paymentDetInfo.put("BALANCE", sbalceadd);
										paymentDetInfo.put("PONO", ponoadv);
										paymentDetInfo.put("PAYHDRID", payHdrId);
										paymentDetInfo.put("BILLHDRID", "0");
										paymentDetInfo.put("EXPHDRID", "0");
										paymentDetInfo.put("TYPE", paytype);
										paymentDetInfo.put("CRAT", DateUtils.getDateTime());
										paymentDetInfo.put("CRBY", username);
										paymentDetInfo.put("UPAT", DateUtils.getDateTime());
										paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
										paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
										paymentDetInfoList.add(paymentDetInfo);
									}

									for (int i = 0; i < billHdrId.size(); i++) {
										int lnno = i + 1;
										paymentDetInfo = new Hashtable<String, String>();
										paymentDetInfo.put("PLANT", plant);
										if (((String) type.get(i)).equalsIgnoreCase("ADVANCE")) {
											paymentDetInfo.put("LNNO", "0");
											paymentDetInfo.put("AMOUNT", amountpaid);
											String detbal = String
													.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
											paymentDetInfo.put("BALANCE", detbal);
											paymentDetInfo.put("PONO", (String) pono.get(i));
											paymentDetInfo.put("PAYHDRID", payHdrId);
											paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
											paymentDetInfo.put("EXPHDRID", "0");
											paymentDetInfo.put("TYPE", (String) type.get(i));
											paymentDetInfo.put("CRAT", DateUtils.getDateTime());
											paymentDetInfo.put("CRBY", username);
											paymentDetInfo.put("UPAT", DateUtils.getDateTime());
											paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
											paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
											paymentDetInfoList.add(paymentDetInfo);
										} else {

											double amocheck = Double.parseDouble((String) amount.get(i));

											if (amocheck > 0) {
												paymentDetInfo.put("LNNO", Integer.toString(lnno));
												paymentDetInfo.put("AMOUNT", (String) amount.get(i));
												paymentDetInfo.put("BALANCE", "0");
												paymentDetInfo.put("PONO", (String) pono.get(i));
												paymentDetInfo.put("PAYHDRID", payHdrId);
												paymentDetInfo.put("BILLHDRID", (String) billHdrId.get(i));
												paymentDetInfo.put("EXPHDRID", "0");
												paymentDetInfo.put("TYPE", (String) type.get(i));
												paymentDetInfo.put("CRAT", DateUtils.getDateTime());
												paymentDetInfo.put("CRBY", username);
												paymentDetInfo.put("UPAT", DateUtils.getDateTime());
												paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
												paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
												paymentDetInfoList.add(paymentDetInfo);
												explno = lnno;
											}
										}

									}

								} else {
									paymentDetInfo = new Hashtable<String, String>();
									paymentDetInfo.put("PLANT", plant);
									paymentDetInfo.put("LNNO", "0");
									paymentDetInfo.put("AMOUNT", amountpaid);
									String detbal = String.valueOf(Double.parseDouble(amountpaid) - pdaamountiff);
									paymentDetInfo.put("BALANCE", detbal);
									paymentDetInfo.put("PONO", "");
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", "0");
									paymentDetInfo.put("EXPHDRID", "0");
									paymentDetInfo.put("TYPE", "ADVANCE");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									paymentDetInfo.put("CRBY", username);
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									paymentDetInfoList.add(paymentDetInfo);
								}

							} else {
								/*
								 * paymentDetInfo = new Hashtable<String, String>(); paymentDetInfo.put("PLANT",
								 * plant); paymentDetInfo.put("LNNO", "0"); paymentDetInfo.put("AMOUNT",
								 * amountpaid); String detbal=
								 * String.valueOf(Double.parseDouble(amountpaid)-pdaamountiff);
								 * paymentDetInfo.put("BALANCE", detbal); paymentDetInfo.put("PONO", "");
								 * paymentDetInfo.put("PAYHDRID", payHdrId); paymentDetInfo.put("BILLHDRID",
								 * "0"); paymentDetInfo.put("EXPHDRID", "0"); paymentDetInfo.put("TYPE",
								 * "ADVANCE"); paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								 * paymentDetInfo.put("CRAT", DateUtils.getDateTime());
								 * paymentDetInfo.put("CRBY", username); paymentDetInfo.put("UPAT",
								 * DateUtils.getDateTime()); paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
								 * paymentDetInfoList.add(paymentDetInfo);
								 */
							}

							for (int i = 0; i < ExpHdrId.size(); i++) {
								explno = explno + 1;
								paymentDetInfo = new Hashtable<String, String>();
								paymentDetInfo.put("PLANT", plant);

								double amocheck = Double.parseDouble((String) Expamount.get(i));

								if (amocheck > 0) {
									paymentDetInfo.put("LNNO", Integer.toString(explno));
									paymentDetInfo.put("AMOUNT", (String) Expamount.get(i));
									paymentDetInfo.put("BALANCE", "0");
									paymentDetInfo.put("PONO", (String) ExpPono.get(i));
									paymentDetInfo.put("PAYHDRID", payHdrId);
									paymentDetInfo.put("BILLHDRID", "0");
									paymentDetInfo.put("EXPHDRID", (String) ExpHdrId.get(i));
									paymentDetInfo.put("TYPE", (String) ExpType.get(i));
									paymentDetInfo.put("CRAT", DateUtils.getDateTime());
									paymentDetInfo.put("CRBY", username);
									paymentDetInfo.put("UPAT", DateUtils.getDateTime());
									paymentDetInfo.put("ADVANCEFROM", "PAYMENT");
									paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
									ExppaymentDetInfoList.add(paymentDetInfo);
								}
							}

							if (paymentDetInfoList.size() > 0) {
								isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
								if (isAdded) {
									if (ExppaymentDetInfoList.size() > 0) {
										isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
									}
								}
							} else {
								if (ExppaymentDetInfoList.size() > 0) {
									isAdded = billPaymentUtil.addMultiplePaymentDet(ExppaymentDetInfoList, plant);
								}
							}

							while (fileIterator.hasNext()) {
								FileItem fileItem = (FileItem) fileIterator.next();
								if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
									String fileLocation = "C:/ATTACHMENTS/Payment" + "/" + payHdrId;
									String filetempLocation = "C:/ATTACHMENTS/Payment" + "/temp" + "/" + payHdrId;
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
									paymentAttachment = new Hashtable<String, String>();
									paymentAttachment.put("PLANT", plant);
									paymentAttachment.put("FILETYPE", fileItem.getContentType());
									paymentAttachment.put("FILENAME", fileName);
									paymentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
									paymentAttachment.put("FILEPATH", fileLocation);
									paymentAttachment.put("CRAT", DateUtils.getDateTime());
									paymentAttachment.put("CRBY", username);
									paymentAttachmentList.add(paymentAttachment);
								}
							}
							int attchSize = paymentAttachmentList.size();
							for (int i = 0; i < attchSize; i++) {
								paymentAttachment = new Hashtable<String, String>();
								paymentAttachment = paymentAttachmentList.get(i);
								paymentAttachment.put("PAYHDRID", payHdrId);
								paymentAttachmentInfoList.add(paymentAttachment);
							}

							if (isAdded) {
								if (paymentAttachmentInfoList.size() > 0)
									isAdded = billPaymentUtil.addPaymentAttachments(paymentAttachmentInfoList, plant);

								String curency = StrUtils
										.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
								// Journal Entry
								JournalHeader journalHead = new JournalHeader();
								journalHead.setPLANT(plant);
								journalHead.setJOURNAL_DATE(paymentDate);
								journalHead.setJOURNAL_STATUS("PUBLISHED");
								journalHead.setJOURNAL_TYPE("Cash");
								journalHead.setCURRENCYID(curency);
								journalHead.setTRANSACTION_TYPE("EXPENSEPAYMENT");
								journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
								journalHead.setSUB_TOTAL(Double.parseDouble(amountpaid));
								journalHead.setTOTAL_AMOUNT(Double.parseDouble(amountpaid));
								journalHead.setCRAT(DateUtils.getDateTime());
								journalHead.setCRBY(username);

								List<JournalDetail> journalDetails = new ArrayList<>();
								CoaDAO coaDAO = new CoaDAO();
								VendMstDAO vendorDAO = new VendMstDAO();
								JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) vendno);
								if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
									JSONObject vendorJson = vendorDAO.getVendorName(plant, (String) vendno);
									coaJson1 = coaDAO.getCOAByName(plant,
											vendorJson.getString("VENDNO") + "-" + vendorJson.getString("VNAME"));
									jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
								}
								if (paidpdcstatus.equalsIgnoreCase("1")) {
									for (int i = 0; i < bankcode.size(); i++) {
										if (pdcstatus.get(i).equals("1")) {
											JournalDetail journalDetail = new JournalDetail();
											journalDetail.setPLANT(plant);
											JSONObject coaJson = coaDAO.getCOAByName(plant, "PDC issued");
											System.out.println("Json" + coaJson.toString());
											journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
											journalDetail.setACCOUNT_NAME("PDC issued");
											if (!bankCharge.isEmpty()) {
												if (Double.parseDouble(bankCharge) > 0) {
													journalDetail
															.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																	+ Double.parseDouble(bankCharge));
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
											journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
											journalDetails.add(journalDetail);
										} else {
											JournalDetail journalDetail = new JournalDetail();
											journalDetail.setPLANT(plant);
											JSONObject coaJson = coaDAO.getCOAByName(plant, (String) bankcode.get(i));
											System.out.println("Json" + coaJson.toString());
											journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
											journalDetail.setACCOUNT_NAME((String) bankcode.get(i));
											if (!bankCharge.isEmpty()) {
												if (Double.parseDouble(bankCharge) > 0) {
													journalDetail
															.setCREDITS(Double.parseDouble((String) chequeamount.get(i))
																	+ Double.parseDouble(bankCharge));
												} else {
													journalDetail.setCREDITS(
															Double.parseDouble((String) chequeamount.get(i)));
												}
											} else {
												journalDetail
														.setCREDITS(Double.parseDouble((String) chequeamount.get(i)));
											}
											// journalDetail.setCREDITS(Double.parseDouble((String)
											// chequeamount.get(i)));
											journalDetail.setDESCRIPTION("Bank check no: " + (String) chequeno.get(i));
											journalDetails.add(journalDetail);
										}
									}
								} else {
									JournalDetail journalDetail = new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson = coaDAO.getCOAByName(plant, paidThrough);
									System.out.println("Json" + coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME(paidThrough);
									journalDetail.setDESCRIPTION(jdesc+"-"+reference);
									journalDetail.setCREDITS(Double.parseDouble(amountpaid));
									journalDetails.add(journalDetail);
								}

								JournalDetail journalDetail_1 = new JournalDetail();
								journalDetail_1.setPLANT(plant);
								
								if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

								} else {
									journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
									if (coaJson1.getString("account_name") != null) {
										journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
									} else {
										journalDetail_1.setACCOUNT_NAME((String) vendno);
									}
									
									journalDetail_1.setDESCRIPTION(jdesc+"-"+reference);
									journalDetail_1.setDEBITS(Double.parseDouble(amountpaid));
									journalDetails.add(journalDetail_1);
								}
								if (!bankCharge.isEmpty()) {
									Double bankChargeFrom = Double.parseDouble(bankCharge);
									if (bankChargeFrom > 0) {
										JournalDetail journalDetail_2 = new JournalDetail();
										journalDetail_2.setPLANT(plant);
										journalDetail_2.setDESCRIPTION(jdesc+"-"+reference);
										JSONObject coaJson2 = coaDAO.getCOAByName(plant, "Bank charges");
										if (coaJson2.isEmpty() || coaJson2.isNullObject()) {

										} else {
											journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
											journalDetail_2.setACCOUNT_NAME("Bank charges");
											journalDetail_2.setDEBITS(Double.parseDouble(bankCharge));
											journalDetails.add(journalDetail_2);
										}
									}
								}

								Journal journal = new Journal();
								journal.setJournalHeader(journalHead);
								journal.setJournalDetails(journalDetails);
								JournalService journalService = new JournalEntry();
								journalService.addJournal(journal, username);
								Hashtable jhtMovHis = new Hashtable();
								jhtMovHis.put(IDBConstants.PLANT, plant);
								jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
								jhtMovHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								jhtMovHis.put(IDBConstants.ITEM, "");
								jhtMovHis.put(IDBConstants.QTY, "0.0");
								jhtMovHis.put("RECID", "");
								jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,
										journal.getJournalHeader().getTRANSACTION_TYPE() + " "
												+ journal.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis.put(IDBConstants.CREATED_BY, username);
								jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis.put("REMARKS", "");
								movHisDao.insertIntoMovHis(jhtMovHis);
							}
							if (isAdded) {
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(paymentDate));
								// htMovHis.put(IDBConstants.ITEM, paymentMode);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
								htMovHis.put(IDBConstants.CREATED_BY, username);
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS", reference);
								isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							}

							if (isAdded) {
								if (paidpdcstatus.equalsIgnoreCase("1")) {
									for (int i = 0; i < bankcode.size(); i++) {

										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS);
										htMovHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(paymentDate));
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
										htMovHis.put(IDBConstants.CREATED_BY, username);
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										if (pdcstatus.get(i).equals("1")) {
											htMovHis.put("REMARKS",
													"PDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
															+ "," + chedate.get(i) + "," + chequeamount.get(i));
										} else {
											htMovHis.put("REMARKS",
													"CDC-" + vendno + "," + bankcode.get(i) + "," + chequeno.get(i)
															+ "," + chedate.get(i) + "," + chequeamount.get(i));
										}

										isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
									}
								}
							}
						}
						if (isAdded) {
							for (int i = 0; i < billHdrId.size(); i++) {
								double billamountinit = Double.parseDouble((String) billAmount.get(i));
								double billamountpaid = Double.parseDouble((String) amount.get(i));
								String query = "";
								if (billamountpaid > 0.00) {
									if (billamountpaid < billamountinit) {
										query = " SET BILL_STATUS='Partially Paid'";
									} else if (billamountpaid == billamountinit) {
										query = " SET BILL_STATUS='Paid'";
									}

									String upid = (String) billHdrId.get(i);

									Hashtable htCondition = new Hashtable();
									htCondition.put("ID", upid);
									htCondition.put("PLANT", plant);
									if (!upid.equals(""))
										isAdded = billDao.updateBillHdr(query, htCondition, "");
								}
							}
						}
					}

					if (isAdded) {
						DbBean.CommitTran(ut);
						result = "Payment made successfully";
					} else {
						DbBean.RollbackTran(ut);
						result = "Coudn't process payment";
					}
					if (result.equalsIgnoreCase("Coudn't process payment")) {
						if (((String) type.get(0)).equalsIgnoreCase("ADVANCE"))
							response.sendRedirect("../bill/record?type=ADVANCE&pono=" + pono + "&result=" + result);
						else
							response.sendRedirect("../banking/createbillpay?result=" + result);
					} else
						response.sendRedirect("../banking/billpaysummary?result=" + result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/createbillpay?result=" + e.toString());
			}

		}

		// ----------

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("CMD")).trim();
		String vendno = StrUtils.fString(req.getParameter("vendno")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
//		String billStatus="Open";
		List billHdr = new ArrayList<>();

		if (action.equalsIgnoreCase("deletepayment")) {

			String transid = req.getParameter("TRANSID");
			String remark = req.getParameter("REMARK");
			BillDAO bdao = new BillDAO();
			ExpensesUtil expensesUtil = new ExpensesUtil();
			RecvDetDAO recvDetDAO = new RecvDetDAO();
			BillPaymentDAO billDao = new BillPaymentDAO();
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			UserTransaction ut = null;
			String result = "";
			try {
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();
				Hashtable htid = new Hashtable();
				htid.put("PAYHDRID", transid);
				String sql1 = "SELECT A.ID,A.PAYHDRID as HDRID,(ISNULL((SELECT SUM(C.AMOUNT) FROM [" + plant
						+ "_FINPAYMENTDET] C WHERE C.BILLHDRID=A.BILLHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,LNNO,BILL,BILL_DATE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "
						+ plant + "_FINPAYMENTDET A JOIN " + plant + "_FINBILLHDR B on A.BILLHDRID=B.ID WHERE A.PLANT='"
						+ plant + "'";
				List grnbillDetList = recvDetDAO.selectForReport(sql1, htid, "");
				for (int i = 0; i < grnbillDetList.size(); i++) {
					Map m = (Map) grnbillDetList.get(i);

					String detid = (String) m.get("ID");
					Hashtable ht = new Hashtable();
					ht.put("ID", detid);
					ht.put("PLANT", plant);

					List payDetailList = billDao.getpaymentdetbyid(ht);
					Map paydet = (Map) payDetailList.get(0);
					String billhdrid = (String) paydet.get("BILLHDRID");
					String exphdrid = (String) paydet.get("EXPHDRID");
//					String payhdrid =(String) paydet.get("PAYHDRID");
					String amount = (String) paydet.get("AMOUNT");

					/*
					 * Hashtable ht1 = new Hashtable(); ht1 = new Hashtable();
					 * ht1.put("PAYHDRID",payhdrid); String sql =
					 * "SELECT * FROM ["+plant+"_FINPAYMENTDET] WHERE PLANT='"+ plant+"'"; String
					 * ext = "ORDER BY LNNO ASC"; List paymentlineDetList =
					 * recvDetDAO.selectForReport(sql, ht1, ext); Map
					 * paydetline=(Map)paymentlineDetList.get(0);
					 * 
					 * String lineid = (String) paydetline.get("ID"); String linebalance =(String)
					 * paydetline.get("BALANCE");
					 * 
					 * Hashtable ht2 = new Hashtable(); ht2.put("ID", lineid); ht2.put("PLANT",
					 * plant); String totalbalance = String.valueOf(Double.valueOf(linebalance) +
					 * Double.valueOf(amount)); String query = "SET BALANCE="+totalbalance;
					 * billDao.updatePaymentDet(query, ht2, "");
					 */
					if (Integer.valueOf(billhdrid) != 0) {
						Hashtable ht3 = new Hashtable();
						ht3.put("BILLHDRID", billhdrid);
						ht3.put("PLANT", plant);
						String billpaidamount = billDao.getpaymentMadeyBillbibid(ht3);
						double billpaid = Double.valueOf(billpaidamount) - Double.valueOf(amount);
						String query1 = "";
						if (billpaid > 0) {
							query1 = " SET BILL_STATUS='Partially Paid'";
						} else {
							query1 = " SET BILL_STATUS='Open'";
						}
						Hashtable ht4 = new Hashtable();
						ht4.put("ID", billhdrid);
						ht4.put("PLANT", plant);
						bdao.updateBillHdr(query1, ht4, "");
					}

					if (Integer.valueOf(exphdrid) != 0) {

						Hashtable ht3 = new Hashtable();
						ht3.put("EXPHDRID", exphdrid);
						ht3.put("PLANT", plant);
						String exppaidamount = billDao.getpaymentMadeyEXPbibid(ht3);
						double exppaid = Double.valueOf(exppaidamount) - Double.valueOf(amount);
						String query1 = "";

						if (exppaid > 0) {
							query1 = " SET PAYMENT_STATUS='Partially Paid'";
						} else {
							query1 = " SET PAYMENT_STATUS='Open'";
						}
						Hashtable ht4 = new Hashtable();
						ht4.put("ID", exphdrid);
						ht4.put("PLANT", plant);
						expensesUtil.updateexpHdr(query1, ht4, "");
					}
					/*
					 * Hashtable ht5 = new Hashtable(); ht5.put("ID", detid); ht5.put("PLANT",
					 * plant); billDao.deleteBillPaymentdet(ht5);
					 */

				}

				billDao.deletepdcbypayid(plant, transid);

				JournalService journalService = new JournalEntry();
				Journal journalFrom = journalService.getJournalByTransactionId(plant, transid, "PURCHASEPAYMENT");
				if (journalFrom.getJournalHeader() != null) {
					if (journalFrom.getJournalHeader().getID() > 0) {
						journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journalFrom.getJournalHeader().getTRANSACTION_TYPE()
								+ " " + journalFrom.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS", "");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}
				}
				journalFrom = journalService.getJournalByTransactionId(plant, transid, "EXPENSEPAYMENT");
				if (journalFrom.getJournalHeader() != null) {
					if (journalFrom.getJournalHeader().getID() > 0) {
						journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journalFrom.getJournalHeader().getTRANSACTION_TYPE()
								+ " " + journalFrom.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS", "");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}
				}

				Hashtable ht9 = new Hashtable();
				ht9.put("ID", transid);
//				String sql9 = "SELECT ID,PAYMENT_DATE,REFERENCE,VENDNO,ACCOUNT_NAME,ISNULL(A.CREDITAPPLYKEY,'') AS CREDITAPPLYKEY,ISNULL((SELECT VNAME FROM "+plant+"_VENDMST V WHERE V.VENDNO=A.VENDNO),0) AS VNAME,PAID_THROUGH,PAYMENT_MODE,AMOUNTPAID,AMOUNTPAID-(ISNULL((SELECT SUM(B.AMOUNT) FROM "+plant+"_FINPAYMENTDET B WHERE B.PAYHDRID=A.ID AND B.LNNO != 0),0)) AS OVERPAYMENT FROM "+plant+"_FINPAYMENTHDR A WHERE A.PLANT='"+ plant+"'";
//				List arrList = bdao.selectForReport(sql9, ht9, "");
//				Map billpayHdr=(Map)arrList.get(0);

				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);
				htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT);
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				// htMovHis.put(IDBConstants.ITEM, (String)billpayHdr.get("PAYMENT_MODE"));
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(transid));
				htMovHis.put(IDBConstants.CREATED_BY, username);
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS", remark);
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				boolean isdeleted = billDao.deletePayment(plant, transid);

				if (isdeleted) {
					DbBean.CommitTran(ut);
					result = "Payment deleted successfully";
					resp.sendRedirect("../banking/billpaysummary??result=" + result);
				} else {
					DbBean.RollbackTran(ut);
					result = "unable to delete payment";
					resp.sendRedirect("../banking/billpaydetail?TRANID=" + transid + "&resultnew=" + result);
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "unable to delete payment";
				resp.sendRedirect("../banking/billpaydetail?TRANID=" + transid + "&resultnew=" + result);
			}

		}

		if (action.equalsIgnoreCase("checkpdcstatusfordelete")) {
			String pdcstatus = "0";
			RecvDetDAO recvDetDAO = new RecvDetDAO();
			try {
				Hashtable htid = new Hashtable();
				htid.put("PAYHDRID", req.getParameter("ID"));
				String sql = "SELECT A.ID,A.PAYHDRID as HDRID,(ISNULL((SELECT SUM(C.AMOUNT) FROM " + plant
						+ "_FINPAYMENTDET C WHERE C.BILLHDRID=A.BILLHDRID AND C.TYPE = 'REGULAR'),0)) AS PAYMENTDONE,LNNO,BILL,BILL_DATE,TOTAL_AMOUNT as BILL_AMOUNT,A.AMOUNT from "
						+ plant + "_FINPAYMENTDET A JOIN " + plant + "_FINBILLHDR B on A.BILLHDRID=B.ID WHERE A.PLANT='"
						+ plant + "'";
				List grnbillDetList = recvDetDAO.selectForReport(sql, htid, "");
				if (grnbillDetList.size() > 0) {
					for (int i = 0; i < grnbillDetList.size(); i++) {
						Map m = (Map) grnbillDetList.get(i);
						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", m.get("HDRID"));
						ht.put("PLANT", plant);
						BillPaymentDAO billDao = new BillPaymentDAO();
						List pdcDetailList = billDao.getpdcbipayid(ht);
						for (int j = 0; j < pdcDetailList.size(); j++) {
							Map pdcdet = (Map) pdcDetailList.get(j);
							String status = (String) pdcdet.get("STATUS");
							if (status.equalsIgnoreCase("PROCESSED")) {
								pdcstatus = "1";
							}
						}
					}
				} else {
					Hashtable ht = new Hashtable();
					ht.put("PAYMENTID", req.getParameter("ID"));
					ht.put("PLANT", plant);
					BillPaymentDAO billDao = new BillPaymentDAO();
					List pdcDetailList = billDao.getpdcbipayid(ht);
					for (int j = 0; j < pdcDetailList.size(); j++) {
						Map pdcdet = (Map) pdcDetailList.get(j);
						String status = (String) pdcdet.get("STATUS");
						if (status.equalsIgnoreCase("PROCESSED")) {
							pdcstatus = "1";
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				pdcstatus = "2";
			}

			resp.getWriter().write(pdcstatus);
		}

		if (action.equalsIgnoreCase("refundcredit")) {
			String detid = req.getParameter("DETID");
			String transid = req.getParameter("TRANSID");
			String billno = req.getParameter("BILLNO");
			String pamount = req.getParameter("AMOUNT");
			String logstatus = req.getParameter("LOGSTATUS");
			Hashtable ht = new Hashtable();
			ht.put("ID", detid);
			ht.put("PLANT", plant);
			BillPaymentDAO billDao = new BillPaymentDAO();
			BillDAO bdao = new BillDAO();
			RecvDetDAO recvDetDAO = new RecvDetDAO();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			UserTransaction ut = null;
			String result = "";
			try {
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();

				Hashtable htHDR = new Hashtable();
				htHDR.put("A.ID", transid);
				String sqlHDR = "SELECT REFERENCE,ISNULL((SELECT ID FROM " + plant
						+ "_FINVENDORCREDITNOTEHDR C WHERE C.CREDITNOTE=A.REFERENCE),0) AS CRID FROM " + plant
						+ "_FINPAYMENTHDR A WHERE A.PLANT='" + plant + "' ";
				List arrList = bdao.selectForReport(sqlHDR, htHDR, "");
				Map grnbillHdr = (Map) arrList.get(0);

				List payDetailList = billDao.getpaymentdetbyid(ht);
				Map paydet = (Map) payDetailList.get(0);
				String billhdrid = (String) paydet.get("BILLHDRID");
				String payhdrid = (String) paydet.get("PAYHDRID");
				String amount = (String) paydet.get("AMOUNT");

				Hashtable ht1 = new Hashtable();
				ht1 = new Hashtable();
				ht1.put("PAYHDRID", payhdrid);
				String sql = "SELECT * FROM [" + plant + "_FINPAYMENTDET] WHERE PLANT='" + plant + "'";
				String ext = "ORDER BY LNNO ASC";
				List paymentlineDetList = recvDetDAO.selectForReport(sql, ht1, ext);
				Map paydetline = (Map) paymentlineDetList.get(0);

				String lineid = (String) paydetline.get("ID");
				String linebalance = (String) paydetline.get("BALANCE");
				String lineamount = (String) paydetline.get("AMOUNT");

				Hashtable ht2 = new Hashtable();
				ht2.put("ID", lineid);
				ht2.put("PLANT", plant);
				String totalbalance = String.valueOf(Double.valueOf(linebalance) + Double.valueOf(amount));
				String query = "SET BALANCE=" + totalbalance;
				billDao.updatePaymentDet(query, ht2, "");

				Hashtable ht3 = new Hashtable();
				ht3.put("BILLHDRID", billhdrid);
				ht3.put("PLANT", plant);
				String billpaidamount = billDao.getpaymentMadeyBillbibid(ht3);
				double billpaid = Double.valueOf(billpaidamount) - Double.valueOf(amount);
				String query1 = "";
				if (billpaid > 0) {
					query1 = " SET BILL_STATUS='Partially Paid'";
				} else {
					query1 = " SET BILL_STATUS='Open'";
				}
				Hashtable ht4 = new Hashtable();
				ht4.put("ID", billhdrid);
				ht4.put("PLANT", plant);
				bdao.updateBillHdr(query1, ht4, "");

				Hashtable ht5 = new Hashtable();
				ht5.put("ID", detid);
				ht5.put("PLANT", plant);
				billDao.deleteBillPaymentdet(ht5);

				String crid = (String) grnbillHdr.get("CRID");
				if (!crid.equalsIgnoreCase("0")) {
					String query12 = "";
					double la = Double.valueOf(lineamount);
					double tb = Double.valueOf(totalbalance);
					if (la == tb) {
						query12 = " SET CREDIT_STATUS='Open'";
					} else {
						query12 = " SET CREDIT_STATUS='Partially Applied'";
					}
					Hashtable ht6 = new Hashtable();
					ht6.put("ID", crid);
					ht6.put("PLANT", plant);
					supplierCreditDAO.updateHdr(query12, ht6, "");
				}

				/*
				 * Hashtable ht9 = new Hashtable(); ht9.put("ID",transid); String sql9 =
				 * "SELECT ID,PAYMENT_DATE,REFERENCE,VENDNO,ACCOUNT_NAME,ISNULL(A.CREDITAPPLYKEY,'') AS CREDITAPPLYKEY,ISNULL((SELECT VNAME FROM "
				 * +plant+"_VENDMST V WHERE V.VENDNO=A.VENDNO),0) AS VNAME,PAID_THROUGH,PAYMENT_MODE,AMOUNTPAID,AMOUNTPAID-(ISNULL((SELECT SUM(B.AMOUNT) FROM "
				 * +plant+"_FINPAYMENTDET B WHERE B.PAYHDRID=A.ID AND B.LNNO != 0),0)) AS OVERPAYMENT FROM "
				 * +plant+"_FINPAYMENTHDR A WHERE A.PLANT='"+ plant+"'"; List payList =
				 * bdao.selectForReport(sql9, ht9, ""); Map billpayHdr=(Map)payList.get(0);
				 */

				Hashtable ht9 = new Hashtable();
				ht9.put("BILL", billno);
				String sql9 = "SELECT ISNULL(PONO,'') PONO FROM " + plant + "_FINBILLHDR A WHERE A.PLANT='" + plant
						+ "'";
				List payList = bdao.selectForReport(sql9, ht9, "");
				Map billpayHdr = (Map) payList.get(0);
				String pono = (String) billpayHdr.get("PONO");
				String rpono = "";
				if (!pono.isEmpty()) {
					rpono = pono + ",";
				}

				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);
				if (logstatus.equalsIgnoreCase("unapply")) {
					htMovHis.put("DIRTYPE", TransactionConstants.UNAPPLY_CREDIT_PAYMENT);
				} else {
					htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT);
				}
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				// htMovHis.put(IDBConstants.ITEM, (String)billpayHdr.get("PAYMENT_MODE"));
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(transid));
				htMovHis.put(IDBConstants.CREATED_BY, username);
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS", rpono + billno + "," + pamount);
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				DbBean.CommitTran(ut);
				result = "credit payment unapplied successfully";
				resp.sendRedirect("../banking/billpaydetail?TRANID=" + transid + "&rsuccess=" + result);

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "unable to unapply credit payment";
				resp.sendRedirect("../banking/billpaydetail?TRANID=" + transid + "&resultnew=" + result);
			}

		}
		if (action.equalsIgnoreCase("refundcreditexp")) {
			String detid = req.getParameter("DETID");
			String transid = req.getParameter("TRANSID");
			String expid = req.getParameter("EXPID");
			String pamount = req.getParameter("AMOUNT");
			String logstatus = req.getParameter("LOGSTATUS");
			Hashtable ht = new Hashtable();
			ht.put("ID", detid);
			ht.put("PLANT", plant);
			BillPaymentDAO billDao = new BillPaymentDAO();
			BillDAO bdao = new BillDAO();
			RecvDetDAO recvDetDAO = new RecvDetDAO();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			ExpensesUtil expensesUtil = new ExpensesUtil();
			UserTransaction ut = null;
			String result = "";
			try {
				/* Get Transaction object */
				ut = DbBean.getUserTranaction();
				/* Begin Transaction */
				ut.begin();

				Hashtable htHDR = new Hashtable();
				htHDR.put("A.ID", transid);
				String sqlHDR = "SELECT REFERENCE,ISNULL((SELECT ID FROM " + plant
						+ "_FINVENDORCREDITNOTEHDR C WHERE C.CREDITNOTE=A.REFERENCE),0) AS CRID FROM " + plant
						+ "_FINPAYMENTHDR A WHERE A.PLANT='" + plant + "' ";
				List arrList = bdao.selectForReport(sqlHDR, htHDR, "");
				Map grnbillHdr = (Map) arrList.get(0);

				List payDetailList = billDao.getpaymentdetbyid(ht);
				Map paydet = (Map) payDetailList.get(0);
				// String billhdrid =(String) paydet.get("BILLHDRID");
				String payhdrid = (String) paydet.get("PAYHDRID");
				String amount = (String) paydet.get("AMOUNT");

				Hashtable ht1 = new Hashtable();
				ht1 = new Hashtable();
				ht1.put("PAYHDRID", payhdrid);
				String sql = "SELECT * FROM [" + plant + "_FINPAYMENTDET] WHERE PLANT='" + plant + "'";
				String ext = "ORDER BY LNNO ASC";
				List paymentlineDetList = recvDetDAO.selectForReport(sql, ht1, ext);
				Map paydetline = (Map) paymentlineDetList.get(0);

				String lineid = (String) paydetline.get("ID");
				String linebalance = (String) paydetline.get("BALANCE");
				String lineamount = (String) paydetline.get("AMOUNT");

				Hashtable ht2 = new Hashtable();
				ht2.put("ID", lineid);
				ht2.put("PLANT", plant);
				String totalbalance = String.valueOf(Double.valueOf(linebalance) + Double.valueOf(amount));
				String query = "SET BALANCE=" + totalbalance;
				billDao.updatePaymentDet(query, ht2, "");

				Hashtable ht3 = new Hashtable();
				ht3.put("EXPHDRID", expid);
				ht3.put("PLANT", plant);
				String exppaidamount = billDao.getpaymentMadeyEXPbibid(ht3);
				double exppaid = Double.valueOf(exppaidamount) - Double.valueOf(amount);
				String query1 = "";

				if (exppaid > 0) {
					query1 = " SET PAYMENT_STATUS='Partially Paid'";
				} else {
					query1 = " SET PAYMENT_STATUS='Open'";
				}
				Hashtable ht4 = new Hashtable();
				ht4.put("ID", expid);
				ht4.put("PLANT", plant);
				expensesUtil.updateexpHdr(query1, ht4, "");

				Hashtable ht5 = new Hashtable();
				ht5.put("ID", detid);
				ht5.put("PLANT", plant);
				billDao.deleteBillPaymentdet(ht5);

				String crid = (String) grnbillHdr.get("CRID");
				if (!crid.equalsIgnoreCase("0")) {
					String query12 = "";
					double la = Double.valueOf(lineamount);
					double tb = Double.valueOf(totalbalance);
					if (la == tb) {
						query12 = " SET CREDIT_STATUS='Open'";
					} else {
						query12 = " SET CREDIT_STATUS='Partially Applied'";
					}
					Hashtable ht6 = new Hashtable();
					ht6.put("ID", crid);
					ht6.put("PLANT", plant);
					supplierCreditDAO.updateHdr(query12, ht6, "");
				}

				/*
				 * Hashtable ht9 = new Hashtable(); ht9.put("ID",transid); String sql9 =
				 * "SELECT ID,PAYMENT_DATE,REFERENCE,VENDNO,ACCOUNT_NAME,ISNULL(A.CREDITAPPLYKEY,'') AS CREDITAPPLYKEY,ISNULL((SELECT VNAME FROM "
				 * +plant+"_VENDMST V WHERE V.VENDNO=A.VENDNO),0) AS VNAME,PAID_THROUGH,PAYMENT_MODE,AMOUNTPAID,AMOUNTPAID-(ISNULL((SELECT SUM(B.AMOUNT) FROM "
				 * +plant+"_FINPAYMENTDET B WHERE B.PAYHDRID=A.ID AND B.LNNO != 0),0)) AS OVERPAYMENT FROM "
				 * +plant+"_FINPAYMENTHDR A WHERE A.PLANT='"+ plant+"'"; List payList =
				 * bdao.selectForReport(sql9, ht9, ""); Map billpayHdr=(Map)payList.get(0);
				 */

				Hashtable ht9 = new Hashtable();
				ht9.put("ID", expid);
				ht9.put("PLANT", plant);
				List expHdrList = expensesUtil.getExpensesforDetails(ht9, plant);
				Map expHdr = (Map) expHdrList.get(0);
				String pono = (String) expHdr.get("ORDERNO");
				String rpono = "";
				if (!pono.isEmpty()) {
					rpono = pono + ",";
				}

				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);
				if (logstatus.equalsIgnoreCase("unapply")) {
					htMovHis.put("DIRTYPE", TransactionConstants.UNAPPLY_CREDIT_PAYMENT);
				} else {
					htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT);
				}
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				// htMovHis.put(IDBConstants.ITEM, (String)billpayHdr.get("PAYMENT_MODE"));
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(transid));
				htMovHis.put(IDBConstants.CREATED_BY, username);
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS", rpono + expid + "," + pamount);
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

				DbBean.CommitTran(ut);
				result = "credit payment unapplied successfully";
				resp.sendRedirect("jsp/billPaymentDetail.jsp?TRANID=" + transid + "&rsuccess=" + result);

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "unable to unapply credit payment";
				resp.sendRedirect("jsp/billPaymentDetail.jsp?TRANID=" + transid + "&resultnew=" + result);
			}

		}
		if (action.equalsIgnoreCase("checkpdcstatus")) {
			String pdcstatus = "0";
			Hashtable ht = new Hashtable();
			ht.put("PAYMENTID", req.getParameter("DETID"));
			ht.put("PLANT", plant);
			BillPaymentDAO billDao = new BillPaymentDAO();
			try {
				List pdcDetailList = billDao.getpdcbipayid(ht);
				for (int j = 0; j < pdcDetailList.size(); j++) {
					Map pdcdet = (Map) pdcDetailList.get(j);
					String status = (String) pdcdet.get("STATUS");
					if (status.equalsIgnoreCase("PROCESSED")) {
						pdcstatus = "1";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				pdcstatus = "2";
			}

			resp.getWriter().write(pdcstatus);
		}

		if (action.equalsIgnoreCase("getBillByVend")) {
			Hashtable ht = new Hashtable();
			ht.put("VENDNO", vendno);
			ht.put("PLANT", plant);
			BillDAO billDAO = new BillDAO();
			JSONObject billHdrJson = new JSONObject();
			try {
				billHdr = billDAO.getBillHdrByVendNoopen(ht);
				billHdrJson.put("data", billHdr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			resp.getWriter().write(billHdrJson.toString());
		}
		if (action.equalsIgnoreCase("getbalanceofbill")) {
			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();
			Hashtable ht1 = new Hashtable();

			// ht.put("PONO", req.getParameter("pono"));
			ht.put("VENDNO", vendno);
			ht.put("BILLHDRID", req.getParameter("bid"));
			ht.put("PLANT", plant);

			ht1.put("PONO", req.getParameter("pono"));
			ht1.put("VENDNO", vendno);
			ht1.put("BILLHDRID", req.getParameter("bid"));
			ht1.put("PLANT", plant);

			BillPaymentDAO billDao = new BillPaymentDAO();

			try {
				balace = billDao.getpaymentMadeyBillwithbillno(ht1);
				List creditDetailList = billDao.getCreditDetails(ht);

				double cpayment = 0;
				if (!req.getParameter("pono").equals("")) {
					for (int j = 0; j < creditDetailList.size(); j++) {
						Map creditDetail = (Map) creditDetailList.get(j);
						String pono = (String) creditDetail.get("PONO");
						if (pono.equals("") || pono == null) {
						} else {
							if (req.getParameter("pono").equals(pono)) {
								String balforcheck = (String) creditDetail.get("BALANCE");
								double dbalforcheck = "".equals(balforcheck) ? 0.0d : Double.parseDouble(balforcheck);
								cpayment = cpayment + dbalforcheck;
							}
						}
					}
				}

				double allpayment = 0;

				for (int j = 0; j < creditDetailList.size(); j++) {
					Map creditDetail = (Map) creditDetailList.get(j);
					String pono = (String) creditDetail.get("PONO");

					if (pono.equals("") || pono == null) {
						String balforcheck = (String) creditDetail.get("BALANCE");
						double dbalforcheck = "".equals(balforcheck) ? 0.0d : Double.parseDouble(balforcheck);
						allpayment = allpayment + dbalforcheck;
					} else {
						if (req.getParameter("pono").equals(pono)) {
							String balforcheck = (String) creditDetail.get("BALANCE");
							double dbalforcheck = "".equals(balforcheck) ? 0.0d : Double.parseDouble(balforcheck);
							allpayment = allpayment + dbalforcheck;
						}
					}
				}

//				 JSONObject resultJsonInt = new JSONObject();
				resultJson.put("BALANCE", balace);
				resultJson.put("CREDIT", cpayment);
				resultJson.put("ALLCREDIT", allpayment);
				/*
				 * jsonArrayErr.add(resultJsonInt); resultJson.put("balance", jsonArrayErr);
				 */

			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			resp.getWriter().write(resultJson.toString());

		}

		if (action.equalsIgnoreCase("showcreditforapply")) {
//			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();

			// ht.put("PONO", req.getParameter("pono"));
			ht.put("VENDNO", vendno);
			ht.put("BILLHDRID", req.getParameter("bid"));
			ht.put("PLANT", plant);

			BillPaymentDAO billDao = new BillPaymentDAO();

			try {
//				 balace =billDao.getpaymentMadeyBillwithbillno(ht);
				List creditDetailList = billDao.getCreditDetails(ht);
//				 JSONObject resultJsonInt = new JSONObject();
				resultJson.put("CREDIT", creditDetailList);

			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}

		/*
		 * if(action.equalsIgnoreCase("showcreditforapply")) { String balace = "0";
		 * JSONObject resultJson = new JSONObject(); JSONArray jsonArray = new
		 * JSONArray(); JSONObject resultJsonInt1 = new JSONObject(); JSONArray
		 * jsonArrayErr = new JSONArray(); Hashtable ht = new Hashtable();
		 * 
		 * //ht.put("PONO", req.getParameter("pono")); ht.put("VENDNO", vendno);
		 * ht.put("BILLHDRID", req.getParameter("bid")); ht.put("PLANT", plant);
		 * 
		 * BillPaymentDAO billDao = new BillPaymentDAO();
		 * 
		 * try { balace =billDao.getpaymentMadeyBillwithbillno(ht); List
		 * creditDetailList = billDao.getCreditDetails(ht); JSONObject resultJsonInt =
		 * new JSONObject(); resultJson.put("CREDIT", creditDetailList);
		 * 
		 * } catch (Exception e) { resultJson.put("SEARCH_DATA", ""); JSONObject
		 * resultJsonInt = new JSONObject(); resultJsonInt.put("ERROR_MESSAGE",
		 * e.getMessage()); resultJsonInt.put("ERROR_CODE", "98");
		 * jsonArrayErr.add(resultJsonInt); resultJson.put("ERROR", jsonArrayErr); }
		 * 
		 * 
		 * resp.getWriter().write(resultJson.toString());
		 * 
		 * }if(action.equalsIgnoreCase("showcreditforapply")) { String balace = "0";
		 * JSONObject resultJson = new JSONObject(); JSONArray jsonArray = new
		 * JSONArray(); JSONObject resultJsonInt1 = new JSONObject(); JSONArray
		 * jsonArrayErr = new JSONArray(); Hashtable ht = new Hashtable();
		 * 
		 * //ht.put("PONO", req.getParameter("pono")); ht.put("VENDNO", vendno);
		 * ht.put("BILLHDRID", req.getParameter("bid")); ht.put("PLANT", plant);
		 * 
		 * BillPaymentDAO billDao = new BillPaymentDAO();
		 * 
		 * try { balace =billDao.getpaymentMadeyBillwithbillno(ht); List
		 * creditDetailList = billDao.getCreditDetails(ht); JSONObject resultJsonInt =
		 * new JSONObject(); resultJson.put("CREDIT", creditDetailList);
		 * 
		 * } catch (Exception e) { resultJson.put("SEARCH_DATA", ""); JSONObject
		 * resultJsonInt = new JSONObject(); resultJsonInt.put("ERROR_MESSAGE",
		 * e.getMessage()); resultJsonInt.put("ERROR_CODE", "98");
		 * jsonArrayErr.add(resultJsonInt); resultJson.put("ERROR", jsonArrayErr); }
		 * 
		 * 
		 * resp.getWriter().write(resultJson.toString());
		 * 
		 * }
		 */

		if (action.equalsIgnoreCase("getpdcbyid")) {
//			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();

			ht.put("PAYMENTID", req.getParameter("paymentid"));
			ht.put("PLANT", plant);

			BillPaymentDAO billDao = new BillPaymentDAO();

			try {
				List pdcDetailList = billDao.getpdcbipayid(ht);
//				 JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PDC", pdcDetailList);

			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}

		if (action.equalsIgnoreCase("getBillByVendandcredit")) {

			ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			String BILL_STATUS = StrUtils.fString(req.getParameter("BILL_STATUS")).trim();
			String poreturn = StrUtils.fString(req.getParameter("poreturn")).trim();
			JSONObject billHdrJson = new JSONObject();

			if (poreturn.equalsIgnoreCase("") || poreturn == null) {
				Hashtable ht = new Hashtable();
				ht.put("VENDNO", vendno);
				ht.put("PLANT", plant);
				ht.put("BILL_STATUS", BILL_STATUS);
				BillDAO billDAO = new BillDAO();

				try {
					billHdr = billDAO.getBillHdrByVendNoforcreditnotes(ht);
					billHdrJson.put("billno", billHdr);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				String query = "BILL,GRNO,PONO,'0'AS ID,'PORETURN' AS BILLFROM ,'-' AS PURCHASE_LOCATION ";
				String extquery = "PLANT = '" + plant + "' AND PORETURN ='" + poreturn
						+ "' AND STATUS != 'Applied' GROUP BY BILL,GRNO,PONO";
				try {
					billHdr = returnOrderDAO.getPOReturnDetailsbyVendor(plant, query, extquery);
					billHdrJson.put("billno", billHdr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			resp.getWriter().write(billHdrJson.toString());
		} else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			int ID = Integer.parseInt(req.getParameter("attachid"));
			FileHandling fileHandling = new FileHandling();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = billPaymentDAO.getBillPaymentAttachByPrimId(ht1);
				Map billAttach = (Map) paymentAttachment.get(0);
				String filePath = (String) billAttach.get("FilePath");
				String fileType = (String) billAttach.get("FileType");
				String fileName = (String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (action.equalsIgnoreCase("removeAttachmentById")) {
			System.out.println("Remove Attachments by ID");
			int ID = Integer.parseInt(req.getParameter("removeid"));
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				billPaymentDAO.deleteBillPaymentAttachByPrimId(ht1);

			} catch (Exception e) {
				e.printStackTrace();
			}
			resp.getWriter().write("Deleted");
		}

		if (action.equalsIgnoreCase("GET_PDC_PAYMENT")) {
//			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
			JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();
			String fdate = "", tdate = "";
			String FROM_DATE = StrUtils.fString(req.getParameter("FDATE"));
			String TO_DATE = StrUtils.fString(req.getParameter("TDATE"));
			String SUPPLIER = StrUtils.fString(req.getParameter("SUPPLIER"));
			String BANK = StrUtils.fString(req.getParameter("BANK"));
			String CHEQUENO = StrUtils.fString(req.getParameter("CHEQUENO"));
			String TYPE = StrUtils.fString(req.getParameter("TYPE"));
			String STATUS = StrUtils.fString(req.getParameter("STATUS"));
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
			// if(TO_DATE.length()<0||TO_DATE==null||TO_DATE.equalsIgnoreCase(""))TO_DATE=curDate;
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

			ht.put("PLANT", plant);

			BillPaymentDAO billDao = new BillPaymentDAO();

			try {
				List pdcDetailList = billDao.getpdcpayment(ht, fdate, tdate, SUPPLIER, BANK, CHEQUENO, TYPE, STATUS);
//				 JSONObject resultJsonInt = new JSONObject();
				resultJson.put("PDC", pdcDetailList);

			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}

			resp.getWriter().write(resultJson.toString());

		}

	}

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
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
