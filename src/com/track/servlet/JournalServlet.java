package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONException;

import com.google.gson.Gson;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.ReconciliationHdrDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalAttachment;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.LedgerDetails;
import com.track.db.object.LedgerDetailsRec;
import com.track.db.object.ReconciliationDatePojo;
import com.track.db.object.ReconciliationHdr;
import com.track.db.object.ReconciliationPojo;
import com.track.db.util.BillUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.service.LedgerService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.LedgerServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/JournalServlet")
@SuppressWarnings({"rawtypes", "unchecked"})
public class JournalServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
//	private boolean printLog = MLoggerConstant.JournalServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.JournalServlet_PRINTPLANTMASTERINFO;

	private JournalService journalService = new JournalEntry();
	private LedgerService ledgerService = new LedgerServiceImpl();
	String action = "";
	StrUtils strUtils = new StrUtils();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JournalService journalService = new JournalEntry();
		CoaDAO coadao = new CoaDAO();
		action = StrUtils.fString(request.getParameter("action"));
		if (action.equalsIgnoreCase("create")) {
			String journaldate = "", journalno = "", reference = "", notes = "", currency = "", subtotal = "",
					total = "", trantype = "";
			UserTransaction ut = null;
			List account = new ArrayList(), description = new ArrayList(), debits = new ArrayList(),
					credits = new ArrayList();
			int accountCount = 0, descCount = 0, debitCount = 0, creditCount = 0;
			try {
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					JournalHeader journalHeader = new JournalHeader();
					List<JournalDetail> journalDetailList = new ArrayList<>();
					List<JournalAttachment> journalAttachList = new ArrayList<>();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						/* ExpenseHdr */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("journal_date")) {
								journaldate = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("journalno")) {
								journalno = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
								reference = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("notes")) {
								notes = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("currency")) {
								currency = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
								subtotal = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
								total = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("tran_type")) {
								trantype = StrUtils.fString(fileItem.getString()).trim();

							}

							// Details
							if (fileItem.getFieldName().equalsIgnoreCase("journal_account_name")) {
								account.add(accountCount, StrUtils.fString(fileItem.getString()).trim());
								accountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("desc")) {
								description.add(descCount, StrUtils.fString(fileItem.getString()).trim());
								descCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("debit")) {
								debits.add(debitCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								debitCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("credit")) {
								credits.add(creditCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								creditCount++;
							}

						}
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "";
							String filetempLocation = "";
							if (trantype.equalsIgnoreCase("CONTRA")) {
								fileLocation = "C:/ATTACHMENTS/Contra" + "/" + journalno;
								filetempLocation = "C:/ATTACHMENTS/Contra" + "/temp" + "/" + journalno;
							} else {
								fileLocation = "C:/ATTACHMENTS/Journal" + "/" + journalno;
								filetempLocation = "C:/ATTACHMENTS/Journal" + "/temp" + "/" + journalno;
							}
							String fileName = StrUtils.fString(fileItem.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

							File path = new File(fileLocation);
							if (!path.exists()) {
								path.mkdirs();
							}

							// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "\\" + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}

							fileItem.write(uploadedFile);

							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "\\" + fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							JournalAttachment journalAttach = new JournalAttachment();
							journalAttach.setPLANT(plant);
							journalAttach.setFileName(fileName);
							journalAttach.setFileType(fileItem.getContentType());
							journalAttach.setFileSize((int) fileItem.getSize());
							journalAttach.setFilePath(fileLocation);
							journalAttach.setCRAT(DateUtils.getDateTime());
							journalAttach.setCRBY(username);
							journalAttachList.add(journalAttach);
							// expenseAttachmentList.add(expenseAttachment);
						}
					}

					// Journal Header
					journalHeader.setPLANT(plant);
					journalHeader.setJOURNAL_DATE(journaldate);
					journalHeader.setJOURNAL_STATUS("PUBLISHED");
					journalHeader.setJOURNAL_TYPE("Cash");
					journalHeader.setNOTE(notes);
					journalHeader.setREFERENCE(reference);
					journalHeader.setCURRENCYID(currency);
					journalHeader.setTRANSACTION_TYPE(trantype);
					journalHeader.setSUB_TOTAL(Double.parseDouble(subtotal));
					journalHeader.setTOTAL_AMOUNT(Double.parseDouble(total));
					journalHeader.setCRAT(DateUtils.getDateTime());
					journalHeader.setCRBY(username);

					// Journal Details
					for (int i = 0; i < account.size(); i++) {
						JournalDetail journalDetail = new JournalDetail();
						journalDetail.setPLANT(plant);
						String accName = account.get(i).toString();
						JSONObject coaRecord = coadao.getCOAByName(plant, accName);
						int accId = Integer.parseInt(coaRecord.get("id").toString());
						journalDetail.setACCOUNT_ID(accId);
						journalDetail.setACCOUNT_NAME(account.get(i).toString());
						journalDetail.setDEBITS(Double.parseDouble(debits.get(i).toString()));
						journalDetail.setCREDITS(Double.parseDouble(credits.get(i).toString()));
						int getlgt=(reference.length()+description.get(i).toString().length());
						String fnote =notes;
						if(fnote.length()>1000)
							fnote = notes.substring(0, (1000-getlgt));
						journalDetail.setDESCRIPTION(description.get(i).toString()+"-"+reference+"-"+fnote);
						//journalDetail.setDESCRIPTION(description.get(i).toString());
						journalDetail.setCRAT(DateUtils.getDateTime());
						journalDetail.setCRBY(username);
						journalDetailList.add(journalDetail);
					}

					ut = DbBean.getUserTranaction();
					Journal journal = new Journal();
					journal.setJournalHeader(journalHeader);
					journal.setJournalDetails(journalDetailList);
					journal.setJournalAttachment(journalAttachList);
					/* Begin Transaction */
					ut.begin();
					int hdrid = journalService.addJournal(journal, username);
					
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put(IDBConstants.QTY, "0.0");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS","");
					movHisDao.insertIntoMovHis(htMovHis);
					
					DbBean.CommitTran(ut);
					if (trantype.equalsIgnoreCase("CONTRA"))
						response.sendRedirect("../banking/contrasummary");
					else
						response.sendRedirect("../banking/journalsummary");
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (trantype.equalsIgnoreCase("CONTRA"))
					response.sendRedirect("../banking/createcontra");
				else
					response.sendRedirect("../banking/createjournal");
			}

		}
		if (action.equalsIgnoreCase("update")) {
			String journaldate = "", journalno = "", reference = "", notes = "", currency = "", subtotal = "",
					total = "", trantype = "";
			UserTransaction ut = null;
			List account = new ArrayList(), description = new ArrayList(), debits = new ArrayList(),
					credits = new ArrayList();
			int accountCount = 0, descCount = 0, debitCount = 0, creditCount = 0;
			try {
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					JournalHeader journalHeader = new JournalHeader();
					List<JournalDetail> journalDetailList = new ArrayList<>();
					List<JournalAttachment> journalAttachList = new ArrayList<>();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						/* ExpenseHdr */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("journal_date")) {
								journaldate = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("journalno")) {
								journalno = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
								reference = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("notes")) {
								notes = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("currency")) {
								currency = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
								subtotal = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
								total = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("tran_type")) {
								trantype = StrUtils.fString(fileItem.getString()).trim();

							}

							// Details
							if (fileItem.getFieldName().equalsIgnoreCase("journal_account_name")) {
								account.add(accountCount, StrUtils.fString(fileItem.getString()).trim());
								accountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("desc")) {
								description.add(descCount, StrUtils.fString(fileItem.getString()).trim());
								descCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("debit")) {
								debits.add(debitCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								debitCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("credit")) {
								credits.add(creditCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								creditCount++;
							}

						}
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "";
							String filetempLocation = "";
							if (trantype.equalsIgnoreCase("CONTRA")) {
								fileLocation = "C:/ATTACHMENTS/Contra" + "/" + journalno;
								filetempLocation = "C:/ATTACHMENTS/Contra" + "/temp" + "/" + journalno;
							} else {
								fileLocation = "C:/ATTACHMENTS/Journal" + "/" + journalno;
								filetempLocation = "C:/ATTACHMENTS/Journal" + "/temp" + "/" + journalno;
							}
							String fileName = StrUtils.fString(fileItem.getName()).trim();
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

							File path = new File(fileLocation);
							if (!path.exists()) {
								path.mkdirs();
							}

							// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							File uploadedFile = new File(path + "\\" + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}

							fileItem.write(uploadedFile);

							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "\\" + fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}
							JournalAttachment journalAttach = new JournalAttachment();
							journalAttach.setPLANT(plant);
							journalAttach.setFileName(fileName);
							journalAttach.setFileType(fileItem.getContentType());
							journalAttach.setFileSize((int) fileItem.getSize());
							journalAttach.setFilePath(fileLocation);
							journalAttach.setCRAT(DateUtils.getDateTime());
							journalAttach.setCRBY(username);
							journalAttachList.add(journalAttach);
							// expenseAttachmentList.add(expenseAttachment);
						}
					}

					// Journal Header
					journalHeader.setPLANT(plant);
					journalHeader.setID(Integer.parseInt(journalno));
					journalHeader.setJOURNAL_DATE(journaldate);
					journalHeader.setJOURNAL_STATUS("PUBLISHED");
					journalHeader.setJOURNAL_TYPE("Cash");
					journalHeader.setNOTE(notes);
					journalHeader.setREFERENCE(reference);
					journalHeader.setCURRENCYID(currency);
					journalHeader.setTRANSACTION_TYPE(trantype);
					journalHeader.setSUB_TOTAL(Double.parseDouble(subtotal));
					journalHeader.setTOTAL_AMOUNT(Double.parseDouble(total));
					journalHeader.setCRAT(DateUtils.getDateTime());
					journalHeader.setCRBY(username);

					// Journal Details
					for (int i = 0; i < account.size(); i++) {
						JournalDetail journalDetail = new JournalDetail();
						System.out.println("account----"+account.get(i).toString());
						System.out.println("debits----"+debits.get(i).toString());
						System.out.println("credits----"+credits.get(i).toString());
						System.out.println("description----"+description.get(i).toString());
						journalDetail.setPLANT(plant);
						String accName = account.get(i).toString();
						JSONObject coaRecord = coadao.getCOAByName(plant, accName);
						int accId = Integer.parseInt(coaRecord.get("id").toString());
						journalDetail.setACCOUNT_ID(accId);
						journalDetail.setACCOUNT_NAME(account.get(i).toString());
						journalDetail.setDEBITS(Double.parseDouble(debits.get(i).toString()));
						journalDetail.setCREDITS(Double.parseDouble(credits.get(i).toString()));
						int getlgt=(reference.length()+description.get(i).toString().length());
						String fnote =notes;
						if(fnote.length()>1000)
							fnote = notes.substring(0, (1000-getlgt));
						journalDetail.setDESCRIPTION(description.get(i).toString()+"-"+reference+"-"+fnote);
						//journalDetail.setDESCRIPTION(description.get(i).toString());
						journalDetail.setCRAT(DateUtils.getDateTime());
						journalDetail.setCRBY(username);
						journalDetailList.add(journalDetail);
					}

					ut = DbBean.getUserTranaction();
					Journal journal = new Journal();
					journal.setJournalHeader(journalHeader);
					journal.setJournalDetails(journalDetailList);
					journal.setJournalAttachment(journalAttachList);
					/* Begin Transaction */
					ut.begin();
					journalService.updateJournal(journal, username);
					
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put(IDBConstants.QTY, "0.0");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journalno);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS","");
					movHisDao.insertIntoMovHis(htMovHis);
					
					DbBean.CommitTran(ut);
					if (trantype.equalsIgnoreCase("CONTRA"))
						response.sendRedirect("../banking/contrasummary");
					else
						response.sendRedirect("../banking/journalsummary");
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (trantype.equalsIgnoreCase("CONTRA"))
				response.sendRedirect("../banking/contraedit?ID="+journalno+"&result=" + ThrowableUtil.getMessage(e));
				else
					response.sendRedirect("../banking/journaledit?ID="+journalno+"&result=" + ThrowableUtil.getMessage(e));
			}

		}
		
		if (action.equalsIgnoreCase("expjcreate")) {
			String journaldate = "", transactionid = "", reference = "", notes = "", currency = "", subtotal = "",
					total = "",esummary="";
			
			UserTransaction ut = null;
			List account = new ArrayList(), description = new ArrayList(), debits = new ArrayList(),
					credits = new ArrayList();
			int accountCount = 0, descCount = 0, debitCount = 0, creditCount = 0;
			try {
				////////////////
				
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					JournalHeader journalHeader = new JournalHeader();
					List<JournalDetail> journalDetailList = new ArrayList<>();
					List<JournalAttachment> journalAttachList = new ArrayList<>();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						/* ExpenseHdr */
						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("journal_date")) {
								journaldate = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
								subtotal = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
								total = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("eid")) {
								transactionid = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("summary")) {
								esummary = StrUtils.fString(fileItem.getString()).trim();

							}
							if (fileItem.getFieldName().equalsIgnoreCase("currency")) {
								currency = StrUtils.fString(fileItem.getString()).trim();

							}

							// Details
							if (fileItem.getFieldName().equalsIgnoreCase("journal_account_name")) {
								account.add(accountCount, StrUtils.fString(fileItem.getString()).trim());
								accountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("desc")) {
								description.add(descCount, StrUtils.fString(fileItem.getString()).trim());
								descCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("debit")) {
								debits.add(debitCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								debitCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("credit")) {
								credits.add(creditCount,
										(fileItem.getString() == null || fileItem.getString().equals("") ? "0"
												: fileItem.getString()));
								creditCount++;
							}

						}

					}
					
					boolean jstatus = new JournalDAO().isjournalExisit(plant, "EXPENSE", transactionid);

					// Journal Header
					journalHeader.setPLANT(plant);
					journalHeader.setJOURNAL_DATE(journaldate);
					journalHeader.setJOURNAL_STATUS("PUBLISHED");
					journalHeader.setJOURNAL_TYPE("Cash");
					journalHeader.setNOTE(notes);
					journalHeader.setREFERENCE(reference);
					journalHeader.setCURRENCYID(currency);
					journalHeader.setTRANSACTION_TYPE("EXPENSE");
					journalHeader.setTRANSACTION_ID(transactionid);
					journalHeader.setSUB_TOTAL(Double.parseDouble(subtotal));
					journalHeader.setTOTAL_AMOUNT(Double.parseDouble(total));
					journalHeader.setCRAT(DateUtils.getDateTime());
					journalHeader.setCRBY(username);
					if(jstatus) {
						int jid = new JournalDAO().getjid(plant, "EXPENSE", transactionid);
						journalHeader.setID(jid);
					}
					// Journal Details
					for (int i = 0; i < account.size(); i++) {
						JournalDetail journalDetail = new JournalDetail();
						journalDetail.setPLANT(plant);
						String accName = account.get(i).toString();
						JSONObject coaRecord = coadao.getCOAByName(plant, accName);
						int accId = Integer.parseInt(coaRecord.get("id").toString());
						journalDetail.setACCOUNT_ID(accId);
						journalDetail.setACCOUNT_NAME(account.get(i).toString());
						journalDetail.setDEBITS(Double.parseDouble(debits.get(i).toString()));
						journalDetail.setCREDITS(Double.parseDouble(credits.get(i).toString()));
						int getlgt=(reference.length()+description.get(i).toString().length());
						String fnote =notes;
						if(fnote.length()>1000)
							fnote = notes.substring(0, (1000-getlgt));
						journalDetail.setDESCRIPTION(description.get(i).toString()+"-"+reference+"-"+fnote);
						//journalDetail.setDESCRIPTION(description.get(i).toString());
						journalDetail.setCRAT(DateUtils.getDateTime());
						journalDetail.setCRBY(username);
						journalDetailList.add(journalDetail);
					}

					ut = DbBean.getUserTranaction();
					Journal journal = new Journal();
					journal.setJournalHeader(journalHeader);
					journal.setJournalDetails(journalDetailList);
					journal.setJournalAttachment(journalAttachList);
					/* Begin Transaction */
					ut.begin();
					int hdrid = 0;
				
					if(jstatus) {
						journalService.updateJournal(journal, username);
						hdrid = journal.getJournalHeader().getID();
					}else {
						hdrid = journalService.addJournal(journal, username);
					}
					
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					if(jstatus) {
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);
					}else {
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
					}
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put(IDBConstants.QTY, "0.0");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS","");
					movHisDao.insertIntoMovHis(htMovHis);
					
					DbBean.CommitTran(ut);
					if(esummary.equalsIgnoreCase("1")) {
						response.sendRedirect("../expenses/possummary");
					}else {
						response.sendRedirect("../expenses/posdetail?TRANID="+transactionid);
					}
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if(esummary.equalsIgnoreCase("1")) {
					response.sendRedirect("../expenses/possummary");
				}else {
					response.sendRedirect("../expenses/posdetail?TRANID="+transactionid);
				}
			}

		}
		
		if (action.equalsIgnoreCase("getAllHeader")) {
			try {
				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));

				List<Journal> journalList = journalService.getAllJournalHeader(plant, username, TRAN_TYPE);

				JSONArray result = new JSONArray();
				for (Journal journal : journalList) {
					if(journal.getJournalHeader().getTRANSACTION_TYPE().equalsIgnoreCase(TRAN_TYPE)) {
						result.add(journal.getJournalHeader());
					}
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(result.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		 if (action.equals("VIEW_JOURNAL_SUMMARY")) {
		    	
				JSONObject jsonObjectResult = new JSONObject();
		        jsonObjectResult = this.getjournalSummaryView(request);	      
				response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();

		       	       
		    }
		
		
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		action = StrUtils.fString(request.getParameter("action"));
		if (action.equalsIgnoreCase("getAll")) {
			try {
				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));
				List<Journal> journalList = journalService.getAllJournalHeader(plant, username, TRAN_TYPE);
				JSONArray result = new JSONArray();
				result.add(journalList);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(result.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("getAllHeader")) {
			try {
				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));

				List<Journal> journalList = journalService.getAllJournalHeader(plant, username, TRAN_TYPE);

				JSONArray result = new JSONArray();
				for (Journal journal : journalList) {
					if(journal.getJournalHeader().getTRANSACTION_TYPE().equalsIgnoreCase("JOURNAL")) {
						result.add(journal.getJournalHeader());
					}
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(result.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("getJournalReport")) {
			JSONArray jarray = new JSONArray();
			CoaDAO coaDAO = new CoaDAO();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerService.getLedgerDetailsByDate(plant, fromDate, toDate);
				for (LedgerDetails ledgerDetails2 : ledgerDetails) {
					String account_code = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
					ledgerDetails2.setACCOUNT(account_code+" "+ledgerDetails2.getACCOUNT());
				}
				jarray.addAll(ledgerDetails);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jarray.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("getJournalReportByJournalId")) {
			JSONArray jarray = new JSONArray();
			String journalId = StrUtils.fString(request.getParameter("journalId")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerService.getLedgerDetailsByJournalId(plant, journalId);
				jarray.addAll(ledgerDetails);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jarray.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("getJournalReportByTransactionId")) {
			JSONArray jarray = new JSONArray();
			String transactionId = StrUtils.fString(request.getParameter("transactionId")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerService.getLedgerDetailsByTransactionId(plant, transactionId);
				jarray.addAll(ledgerDetails);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jarray.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("getJournalReportAsExcel")) {
			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerService.getLedgerDetailsByDate(plant, fromDate, toDate);
				jarray.addAll(ledgerDetails);

				HSSFWorkbook workbook = null;
				try {
					PlantMstDAO _PlantMstDAO = new PlantMstDAO();
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
					workbook = populateExcel("Journal Report", toDate, jarray.toString(), numberOfDecimal);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				workbook.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=JournalReport.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			int ID = Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling = new FileHandling();
			JournalDAO journalDAO = new JournalDAO();
			List<JournalAttachment> journalAttachment = null;
			try {
				journalAttachment = journalDAO.getJournalAttachmentDetailsByID(ID, plant, username);
				String filePath = journalAttachment.get(0).getFilePath();
				String fileType = journalAttachment.get(0).getFileType();
				String fileName = journalAttachment.get(0).getFileName();
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (action.equalsIgnoreCase("removeAttachmentById")) {
			System.out.println("Remove Attachments by ID");
			int ID = Integer.parseInt(request.getParameter("removeid"));
			JournalDAO journalDAO = new JournalDAO();
			try {
				journalDAO.deleteJournalAttachmentById(ID, plant, username);

			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}

		else if (action.equalsIgnoreCase("Get_Reconciliation_Details_old")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				ledgerDetails = journalDAO.getLedgerDetailsByAccountAll(plant, account);
				for (LedgerDetails ledgerDetails2 : ledgerDetails) {
					if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {

									Hashtable ht1 = new Hashtable();
									ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
									ht1.put("PLANT", plant);
									List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = (String) payHdr.get("ACCOUNT_NAME");
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = vendorDAO.getVendorName(plant,
													(String) payhdrvendo);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
													+ vendorJson.getString("VNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							Hashtable ht1 = new Hashtable();
							ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht1.put("PLANT", plant);
							List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
							if(billpayHdrList.size() > 0) {
								Map payHdr = (Map) billpayHdrList.get(0);
								String vouchertype = "Payment";
								String payhdrvendo = (String) payHdr.get("VENDNO");
								if (payhdrvendo.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
								reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
								Reconciliation.add(reconciliationPojo);
							}
						}

					}else if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {

						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
									InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
											Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
									String vouchertype = "Receipt";
									String payhdrcustno = payHdr.getCUSTNO();
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrcustno.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = payHdr.getACCOUNT_NAME();
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
													+ vendorJson.getString("CNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
									Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
							String vouchertype = "Receipt";
							String payhdrcustno = payHdr.getCUSTNO();
							if (payhdrcustno.equalsIgnoreCase("N/A")) {
								vouchertype = "Contra";
							}

							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
							reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
							reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

							Reconciliation.add(reconciliationPojo);
						}

					}else {

						ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
						reconciliationPojo.setPLANT(plant);
						reconciliationPojo.setDATE(ledgerDetails2.getDATE());
						String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
						reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
						if(ledgerDetails2.getCREDIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Payment");
						}else if(ledgerDetails2.getDEBIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Receipt");
						}else {
							reconciliationPojo.setVOUCHERTYPE("");
						}
						//reconciliationPojo.setVOUCHERTYPE(vouchertype);
						reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
						reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
						reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
						reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

						Reconciliation.add(reconciliationPojo);
					
					}
				}
				
				Reconciliation = datefilter(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
				}
				

				// jarray.addAll(Reconciliation);

				/*
				 * response.setContentType("application/json");
				 * response.setCharacterEncoding("UTF-8");
				 * response.getWriter().write(jarray.toString()); response.getWriter().flush();
				 * response.getWriter().close();
				 */
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				response.getWriter().write(resultJsonInt.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_Details")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				Reconciliation = journalDAO.getJournalDetailsByAccount(plant, fromDate, toDate, account);
				
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				response.getWriter().write(resultJsonInt.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_Details_New")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String reconDate = StrUtils.fString(request.getParameter("reconDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			String actname = StrUtils.fString(request.getParameter("accountname")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();
			ReconciliationHdrDAO ReconciliationHdrDAO = new ReconciliationHdrDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date rdate = formatter.parse(reconDate);
				Calendar cal = Calendar.getInstance();
			    cal.setTime(rdate);
			    System.out.println(cal.getTime());
			    cal.set(Calendar.DATE, 1);
			    System.out.println(cal.getTime());
				cal.add(Calendar.MONTH, -1);
				System.out.println(cal.getTime());
				Date result = cal.getTime();
				String pdate = formatter.format(result);
				
				String todatearray[] = reconDate.split("/");
				String rdatemonth = todatearray[1];
				String rdateyear = todatearray[2];
				
				String pdatearray[] = pdate.split("/");
				String pdatemonth = pdatearray[1];
				String pdateyear = pdatearray[2];

				boolean checkreconcil = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(rdatemonth), Integer.valueOf(rdateyear));
				
				
				if(!checkreconcil) {
				
				
				
				//ledgerDetails = journalDAO.getLedgerDetailsByAccountAll(plant, account);
					
				ledgerDetails = journalDAO.getLedgerDetailsByAccountAllReconcile(plant, account);
				for (LedgerDetails ledgerDetails2 : ledgerDetails) {
					if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {

									Hashtable ht1 = new Hashtable();
									ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
									ht1.put("PLANT", plant);
									List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = (String) payHdr.get("ACCOUNT_NAME");
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = vendorDAO.getVendorName(plant,
													(String) payhdrvendo);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
													+ vendorJson.getString("VNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							Hashtable ht1 = new Hashtable();
							ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht1.put("PLANT", plant);
							List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
							if(billpayHdrList.size() > 0) {
								Map payHdr = (Map) billpayHdrList.get(0);
								String vouchertype = "Payment";
								String payhdrvendo = (String) payHdr.get("VENDNO");
								if (payhdrvendo.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
								reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
								Reconciliation.add(reconciliationPojo);
							}
						}

					}else if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {

						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
									InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
											Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
									String vouchertype = "Receipt";
									String payhdrcustno = payHdr.getCUSTNO();
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrcustno.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = payHdr.getACCOUNT_NAME();
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
													+ vendorJson.getString("CNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
									Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
							String vouchertype = "Receipt";
							String payhdrcustno = payHdr.getCUSTNO();
							if (payhdrcustno.equalsIgnoreCase("N/A")) {
								vouchertype = "Contra";
							}

							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
							reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
							reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

							Reconciliation.add(reconciliationPojo);
						}

					}else {

						ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
						reconciliationPojo.setPLANT(plant);
						reconciliationPojo.setDATE(ledgerDetails2.getDATE());
						String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
						reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
						if(ledgerDetails2.getCREDIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Payment");
						}else if(ledgerDetails2.getDEBIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Receipt");
						}else {
							reconciliationPojo.setVOUCHERTYPE("");
						}
						//reconciliationPojo.setVOUCHERTYPE(vouchertype);
						reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
						reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
						reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
						reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());

						Reconciliation.add(reconciliationPojo);
					
					}
				}
				
				Reconciliation = datefilter(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				double ledgerclosingbal = journalDAO.getbankbalancecbookbalasondate(plant, account, toDate);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
				}
				

				// jarray.addAll(Reconciliation);

				/*
				 * response.setContentType("application/json");
				 * response.setCharacterEncoding("UTF-8");
				 * response.getWriter().write(jarray.toString()); response.getWriter().flush();
				 * response.getWriter().close();
				 */
				
				boolean checkreconcilpre = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
				double bankopeningbal = 0.0;
				double ledgeropeningbal = 0.0;
				if(checkreconcilpre) {
					ReconciliationHdr reconciliationHdr = ReconciliationHdrDAO.getByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
					bankopeningbal = reconciliationHdr.getBANKCLOSEBALANCE();
					ledgeropeningbal = reconciliationHdr.getCLOSEBALANCE();
				}
				
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 0);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				resultJsonInt.put("BANKOPENINGBAL", bankopeningbal);
				resultJsonInt.put("LEDGEROPENINGBAL", ledgeropeningbal);
				resultJsonInt.put("LEDGERCLOSINGGBAL", ledgerclosingbal);
				resultJsonInt.put("RMONTH", rdatemonth);
				resultJsonInt.put("RYEAR", rdateyear);
				response.getWriter().write(resultJsonInt.toString());
			
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("STATUS", 1);
					resultJsonInt.put("REC", Reconciliation);
					resultJsonInt.put("CBOOK", 0.0);
					resultJsonInt.put("PENDINGS", 0.0);
					resultJsonInt.put("BANK", 0.0);
					resultJsonInt.put("RCREDIT", 0.0);
					resultJsonInt.put("RDEPOSIT", 0.0);
					resultJsonInt.put("BANKOPENINGBAL", 0.0);
					resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
					resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
					resultJsonInt.put("RMONTH", "");
					resultJsonInt.put("RYEAR", "");
					response.getWriter().write(resultJsonInt.toString());
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 2);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", 0.0);
				resultJsonInt.put("PENDINGS", 0.0);
				resultJsonInt.put("BANK", 0.0);
				resultJsonInt.put("RCREDIT", 0.0);
				resultJsonInt.put("RDEPOSIT", 0.0);
				resultJsonInt.put("BANKOPENINGBAL", 0.0);
				resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
				resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
				resultJsonInt.put("RMONTH", "");
				resultJsonInt.put("RYEAR", "");
				response.getWriter().write(resultJsonInt.toString());
			}
		}
		
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_Details_New_re")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String reconDate = StrUtils.fString(request.getParameter("reconDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			String actname = StrUtils.fString(request.getParameter("accountname")).trim();
			JournalDAO journalDAO = new JournalDAO();
			ReconciliationHdrDAO ReconciliationHdrDAO = new ReconciliationHdrDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date rdate = formatter.parse(reconDate);
				Calendar cal = Calendar.getInstance();
			    cal.setTime(rdate);
			    System.out.println(cal.getTime());
			    cal.set(Calendar.DATE, 1);
			    System.out.println(cal.getTime());
				cal.add(Calendar.MONTH, -1);
				System.out.println(cal.getTime());
				Date result = cal.getTime();
				String pdate = formatter.format(result);
				
				String todatearray[] = reconDate.split("/");
				String rdatemonth = todatearray[1];
				String rdateyear = todatearray[2];
				
				String pdatearray[] = pdate.split("/");
				String pdatemonth = pdatearray[1];
				String pdateyear = pdatearray[2];

				boolean checkreconcil = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(rdatemonth), Integer.valueOf(rdateyear));
				
				
				if(!checkreconcil) {
				
				
				
				//ledgerDetails = journalDAO.getLedgerDetailsByAccountAll(plant, account);
					
				Reconciliation = journalDAO.getJournalDetailsByAccountTodate(plant, account);
				
				Reconciliation = datefilter(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				double ledgerclosingbal = journalDAO.getbankbalancecbookbalasondate(plant, account, reconDate);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
					String[] FDATE = reconciliationPojo.getDATE().split("/");
					String filterdate = FDATE[2] + FDATE[1]+FDATE[0];
					reconciliationPojo.setFILTERDATE(filterdate);
				}
				

				// jarray.addAll(Reconciliation);

				/*
				 * response.setContentType("application/json");
				 * response.setCharacterEncoding("UTF-8");
				 * response.getWriter().write(jarray.toString()); response.getWriter().flush();
				 * response.getWriter().close();
				 */
				
				boolean checkreconcilpre = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
				double bankopeningbal = 0.0;
				double ledgeropeningbal = 0.0;
				if(checkreconcilpre) {
					ReconciliationHdr reconciliationHdr = ReconciliationHdrDAO.getByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
					bankopeningbal = reconciliationHdr.getBANKCLOSEBALANCE();
					ledgeropeningbal = reconciliationHdr.getCLOSEBALANCE();
				}
				
				
				Reconciliation.sort((o1, o2)
		                  -> o1.getFILTERDATE().compareTo(
		                      o2.getFILTERDATE()));
				
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 0);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				resultJsonInt.put("BANKOPENINGBAL", bankopeningbal);
				resultJsonInt.put("LEDGEROPENINGBAL", ledgeropeningbal);
				resultJsonInt.put("LEDGERCLOSINGGBAL", ledgerclosingbal);
				resultJsonInt.put("RMONTH", rdatemonth);
				resultJsonInt.put("RYEAR", rdateyear);
				response.getWriter().write(resultJsonInt.toString());
			
				}else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("STATUS", 1);
					resultJsonInt.put("REC", Reconciliation);
					resultJsonInt.put("CBOOK", 0.0);
					resultJsonInt.put("PENDINGS", 0.0);
					resultJsonInt.put("BANK", 0.0);
					resultJsonInt.put("RCREDIT", 0.0);
					resultJsonInt.put("RDEPOSIT", 0.0);
					resultJsonInt.put("BANKOPENINGBAL", 0.0);
					resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
					resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
					resultJsonInt.put("RMONTH", "");
					resultJsonInt.put("RYEAR", "");
					response.getWriter().write(resultJsonInt.toString());
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 2);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", 0.0);
				resultJsonInt.put("PENDINGS", 0.0);
				resultJsonInt.put("BANK", 0.0);
				resultJsonInt.put("RCREDIT", 0.0);
				resultJsonInt.put("RDEPOSIT", 0.0);
				resultJsonInt.put("BANKOPENINGBAL", 0.0);
				resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
				resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
				resultJsonInt.put("RMONTH", "");
				resultJsonInt.put("RYEAR", "");
				response.getWriter().write(resultJsonInt.toString());
			}
		}
		
		

		else if (action.equalsIgnoreCase("Get_Reconciliation_Details_Edit_re")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String reconDate = StrUtils.fString(request.getParameter("reconDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			String actname = StrUtils.fString(request.getParameter("accountname")).trim();
			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			List<LedgerDetails> ledgerDetailsedit = new ArrayList<LedgerDetails>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();
			ReconciliationHdrDAO ReconciliationHdrDAO = new ReconciliationHdrDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			List<ReconciliationPojo> ReconciliationUnDeposit = new ArrayList<ReconciliationPojo>();
			List<ReconciliationPojo> Reconciliationedit = new ArrayList<ReconciliationPojo>();
			try {
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date rdate = formatter.parse(reconDate);
				Calendar cal = Calendar.getInstance();
			    cal.setTime(rdate);
			    System.out.println(cal.getTime());
			    cal.set(Calendar.DATE, 1);
			    System.out.println(cal.getTime());
				cal.add(Calendar.MONTH, -1);
				System.out.println(cal.getTime());
				Date result = cal.getTime();
				String pdate = formatter.format(result);
				
				String todatearray[] = reconDate.split("/");
				String rdatemonth = todatearray[1];
				String rdateyear = todatearray[2];
				
				String pdatearray[] = pdate.split("/");
				String pdatemonth = pdatearray[1];
				String pdateyear = pdatearray[2];
				
				//ledgerDetails = journalDAO.getLedgerDetailsByAccountAll(plant, account);
					
				Reconciliation = journalDAO.getJournalDetailsByAccountTodate(plant, account); //16_09_24 Azees
				String fdate="",tdate="";
			   	Date fFdate = formatter.parse(toDate);
			   	cal.setTime(fFdate);
			   	cal.add(Calendar.MONTH, -1);
			    int iyyyy = cal.get(Calendar.YEAR);
				int iMM = cal.get(Calendar.MONTH)+1;
				//fdate = iyyyy+"-"+iMM+"-"+"01";
				fdate = iyyyy+"-"+ (iMM < 10 ? "0" + iMM : iMM) +"-"+"01";
				
				//System.out.println(reconDate+" to date");
	            
	            tdate    = reconDate.substring(6)+"-"+ reconDate.substring(3,5)+"-"+reconDate.substring(0,2);
				
				ReconciliationUnDeposit = journalDAO.getJournalDetailsByAccountTodateDeposits(plant, account, rid, fdate, tdate);
				
				Reconciliation = datefilter(Reconciliation, fromDate, toDate, datestatus);
				
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				//double ledgerclosingbal = journalDAO.getbankbalancecbookbalasondate(plant, account, toDate);
				double ledgerclosingbal = journalDAO.getbankbalancecbookbalasondate(plant, account, reconDate);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
				}
				
				boolean checkreconcilpre = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
				double bankopeningbal = 0.0;
				double ledgeropeningbal = 0.0;
				if(checkreconcilpre) {
					ReconciliationHdr reconciliationHdr = ReconciliationHdrDAO.getByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
					bankopeningbal = reconciliationHdr.getBANKCLOSEBALANCE();
					ledgeropeningbal = reconciliationHdr.getCLOSEBALANCE();
				}
				
				Reconciliationedit = journalDAO.getJournalDetailsByAccountTodateRid(plant, account,rid);

				Reconciliation.sort((o1, o2)
		                  -> o1.getDATE().compareTo(
		                      o2.getDATE()));
				
				Reconciliationedit.sort((o1, o2)
		                  -> o1.getDATE().compareTo(
		                      o2.getDATE()));
				
				ReconciliationUnDeposit.sort((o1, o2)
						-> o1.getDATE().compareTo(
								o2.getDATE()));
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 0);
				resultJsonInt.put("TYPE", 0);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("RECUNDEPOSIT", ReconciliationUnDeposit);
				resultJsonInt.put("RECEDIT", Reconciliationedit);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				resultJsonInt.put("BANKOPENINGBAL", bankopeningbal);
				resultJsonInt.put("LEDGEROPENINGBAL", ledgeropeningbal);
				resultJsonInt.put("LEDGERCLOSINGGBAL", ledgerclosingbal);
				resultJsonInt.put("RMONTH", rdatemonth);
				resultJsonInt.put("RYEAR", rdateyear);
				response.getWriter().write(resultJsonInt.toString());
				
			
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 2);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", 0.0);
				resultJsonInt.put("PENDINGS", 0.0);
				resultJsonInt.put("BANK", 0.0);
				resultJsonInt.put("RCREDIT", 0.0);
				resultJsonInt.put("RDEPOSIT", 0.0);
				resultJsonInt.put("BANKOPENINGBAL", 0.0);
				resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
				resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
				resultJsonInt.put("RMONTH", "");
				resultJsonInt.put("RYEAR", "");
				response.getWriter().write(resultJsonInt.toString());
			}
		}
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_Details_Edit")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String reconDate = StrUtils.fString(request.getParameter("reconDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			String actname = StrUtils.fString(request.getParameter("accountname")).trim();
			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			List<LedgerDetails> ledgerDetailsedit = new ArrayList<LedgerDetails>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();
			ReconciliationHdrDAO ReconciliationHdrDAO = new ReconciliationHdrDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			List<ReconciliationPojo> Reconciliationedit = new ArrayList<ReconciliationPojo>();
			try {
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date rdate = formatter.parse(reconDate);
				Calendar cal = Calendar.getInstance();
			    cal.setTime(rdate);
			    System.out.println(cal.getTime());
			    cal.set(Calendar.DATE, 1);
			    System.out.println(cal.getTime());
				cal.add(Calendar.MONTH, -1);
				System.out.println(cal.getTime());
				Date result = cal.getTime();
				String pdate = formatter.format(result);
				
				String todatearray[] = reconDate.split("/");
				String rdatemonth = todatearray[1];
				String rdateyear = todatearray[2];
				
				String pdatearray[] = pdate.split("/");
				String pdatemonth = pdatearray[1];
				String pdateyear = pdatearray[2];
				
				//ledgerDetails = journalDAO.getLedgerDetailsByAccountAll(plant, account);
					
				ledgerDetails = journalDAO.getLedgerDetailsByAccountAllReconcile(plant, account);
				if(ledgerDetails.size() > 0) {
					for (LedgerDetails ledgerDetails2 : ledgerDetails) {
						if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
							Hashtable ht = new Hashtable();
							ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht.put("PLANT", plant);
							List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);
	
							if (pdcDetailList.size() > 0) {
								for (int j = 0; j < pdcDetailList.size(); j++) {
									Map pdcdet = (Map) pdcDetailList.get(j);
									String status = (String) pdcdet.get("STATUS");
									String checkdate = (String) pdcdet.get("CHEQUE_DATE");
									String checkNo = (String) pdcdet.get("CHECQUE_NO");
									String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
											"");
									if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
	
										Hashtable ht1 = new Hashtable();
										ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
										ht1.put("PLANT", plant);
										List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
										Map payHdr = (Map) billpayHdrList.get(0);
										String vouchertype = "Payment";
										String payhdrvendo = (String) payHdr.get("VENDNO");
										String accountname = ledgerDetails2.getACCOUNT();
										if (payhdrvendo.equalsIgnoreCase("N/A")) {
											vouchertype = "Contra";
											accountname = (String) payHdr.get("ACCOUNT_NAME");
										} else {
											JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												JSONObject vendorJson = vendorDAO.getVendorName(plant,
														(String) payhdrvendo);
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
														+ vendorJson.getString("VNAME"));
											}
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
	
											} else {
												accountname = coaJson1.getString("account_name");
											}
										}
										ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
										reconciliationPojo.setPLANT(plant);
										reconciliationPojo.setDATE(ledgerDetails2.getDATE());
										String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
										reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
										reconciliationPojo.setVOUCHERTYPE(vouchertype);
										reconciliationPojo.setINSTRUMENTDATE(checkdate);
										reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
										reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
										reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
										Reconciliation.add(reconciliationPojo);
									}
								}
							} else {
	
								Hashtable ht1 = new Hashtable();
								ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
								ht1.put("PLANT", plant);
								List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
								if(billpayHdrList.size() > 0) {
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
									}
		
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
		
									Reconciliation.add(reconciliationPojo);
								}
							}
	
						}else if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {
	
							Hashtable ht = new Hashtable();
							ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht.put("PLANT", plant);
							List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);
	
							if (pdcDetailList.size() > 0) {
								for (int j = 0; j < pdcDetailList.size(); j++) {
									Map pdcdet = (Map) pdcDetailList.get(j);
									String status = (String) pdcdet.get("STATUS");
									String checkdate = (String) pdcdet.get("CHEQUE_DATE");
									String checkNo = (String) pdcdet.get("CHECQUE_NO");
									String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
											"");
									if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
										InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
												Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
										String vouchertype = "Receipt";
										String payhdrcustno = payHdr.getCUSTNO();
										String accountname = ledgerDetails2.getACCOUNT();
										if (payhdrcustno.equalsIgnoreCase("N/A")) {
											vouchertype = "Contra";
											accountname = payHdr.getACCOUNT_NAME();
										} else {
											JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
														+ vendorJson.getString("CNAME"));
											}
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
	
											} else {
												accountname = coaJson1.getString("account_name");
											}
										}
										ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
										reconciliationPojo.setPLANT(plant);
										reconciliationPojo.setDATE(ledgerDetails2.getDATE());
										String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
										reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
										reconciliationPojo.setVOUCHERTYPE(vouchertype);
										reconciliationPojo.setINSTRUMENTDATE(checkdate);
										reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
										reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
										reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
										Reconciliation.add(reconciliationPojo);
									}
								}
							} else {
	
								InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
										Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
								String vouchertype = "Receipt";
								String payhdrcustno = payHdr.getCUSTNO();
								if (payhdrcustno.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
								reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
								Reconciliation.add(reconciliationPojo);
							}
	
						}else {
	
							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
							reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
							if(ledgerDetails2.getCREDIT() > 0) {
								reconciliationPojo.setVOUCHERTYPE("Payment");
							}else if(ledgerDetails2.getDEBIT() > 0) {
								reconciliationPojo.setVOUCHERTYPE("Receipt");
							}else {
								reconciliationPojo.setVOUCHERTYPE("");
							}
							//reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
							Reconciliation.add(reconciliationPojo);
						
						}
					}
				}
				
				Reconciliation = datefilter(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				double ledgerclosingbal = journalDAO.getbankbalancecbookbalasondate(plant, account, toDate);
				double rdeposit=0.0,rcredit=0.0;
				
				for (ReconciliationPojo reconciliationPojo : Reconciliation) {
					rdeposit = rdeposit + reconciliationPojo.getDEBIT();
					rcredit = rcredit + reconciliationPojo.getCREDIT();
				}
				

				// jarray.addAll(Reconciliation);

				/*
				 * response.setContentType("application/json");
				 * response.setCharacterEncoding("UTF-8");
				 * response.getWriter().write(jarray.toString()); response.getWriter().flush();
				 * response.getWriter().close();
				 */
				
				boolean checkreconcilpre = ReconciliationHdrDAO.getstatusByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
				double bankopeningbal = 0.0;
				double ledgeropeningbal = 0.0;
				if(checkreconcilpre) {
					ReconciliationHdr reconciliationHdr = ReconciliationHdrDAO.getByAccMonYear(plant, actname, Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
					bankopeningbal = reconciliationHdr.getBANKCLOSEBALANCE();
					ledgeropeningbal = reconciliationHdr.getCLOSEBALANCE();
				}
				
				ledgerDetailsedit = journalDAO.getLedgerDetailsByAccountAllReconcileByRId(plant, account,rid);
				if(ledgerDetailsedit.size() > 0) {
					for (LedgerDetails ledgerDetails2 : ledgerDetailsedit) {
						if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
							Hashtable ht = new Hashtable();
							ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht.put("PLANT", plant);
							List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);
	
							if (pdcDetailList.size() > 0) {
								for (int j = 0; j < pdcDetailList.size(); j++) {
									Map pdcdet = (Map) pdcDetailList.get(j);
									String status = (String) pdcdet.get("STATUS");
									String checkdate = (String) pdcdet.get("CHEQUE_DATE");
									String checkNo = (String) pdcdet.get("CHECQUE_NO");
									String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
											"");
									if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
	
										Hashtable ht1 = new Hashtable();
										ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
										ht1.put("PLANT", plant);
										List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
										Map payHdr = (Map) billpayHdrList.get(0);
										String vouchertype = "Payment";
										String payhdrvendo = (String) payHdr.get("VENDNO");
										String accountname = ledgerDetails2.getACCOUNT();
										if (payhdrvendo.equalsIgnoreCase("N/A")) {
											vouchertype = "Contra";
											accountname = (String) payHdr.get("ACCOUNT_NAME");
										} else {
											JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												JSONObject vendorJson = vendorDAO.getVendorName(plant,
														(String) payhdrvendo);
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
														+ vendorJson.getString("VNAME"));
											}
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
	
											} else {
												accountname = coaJson1.getString("account_name");
											}
										}
										ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
										reconciliationPojo.setPLANT(plant);
										reconciliationPojo.setDATE(ledgerDetails2.getDATE());
										String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
										reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
										reconciliationPojo.setVOUCHERTYPE(vouchertype);
										reconciliationPojo.setINSTRUMENTDATE(checkdate);
										reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
										reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
										reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
										Reconciliationedit.add(reconciliationPojo);
									}
								}
							} else {
	
								Hashtable ht1 = new Hashtable();
								ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
								ht1.put("PLANT", plant);
								List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
								if(billpayHdrList.size() > 0) {
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
									}
		
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
		
									Reconciliationedit.add(reconciliationPojo);
								}
							}
	
						}else if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {
	
							Hashtable ht = new Hashtable();
							ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht.put("PLANT", plant);
							List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);
	
							if (pdcDetailList.size() > 0) {
								for (int j = 0; j < pdcDetailList.size(); j++) {
									Map pdcdet = (Map) pdcDetailList.get(j);
									String status = (String) pdcdet.get("STATUS");
									String checkdate = (String) pdcdet.get("CHEQUE_DATE");
									String checkNo = (String) pdcdet.get("CHECQUE_NO");
									String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
											"");
									if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
										InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
												Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
										String vouchertype = "Receipt";
										String payhdrcustno = payHdr.getCUSTNO();
										String accountname = ledgerDetails2.getACCOUNT();
										if (payhdrcustno.equalsIgnoreCase("N/A")) {
											vouchertype = "Contra";
											accountname = payHdr.getACCOUNT_NAME();
										} else {
											JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
												JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
												coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
														+ vendorJson.getString("CNAME"));
											}
											if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
	
											} else {
												accountname = coaJson1.getString("account_name");
											}
										}
										ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
										reconciliationPojo.setPLANT(plant);
										reconciliationPojo.setDATE(ledgerDetails2.getDATE());
										String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
										reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
										reconciliationPojo.setVOUCHERTYPE(vouchertype);
										reconciliationPojo.setINSTRUMENTDATE(checkdate);
										reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
										reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
										reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
										Reconciliationedit.add(reconciliationPojo);
									}
								}
							} else {
	
								InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
										Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
								String vouchertype = "Receipt";
								String payhdrcustno = payHdr.getCUSTNO();
								if (payhdrcustno.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
								reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
								Reconciliationedit.add(reconciliationPojo);
							}
	
						}else {
	
							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
							reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
							if(ledgerDetails2.getCREDIT() > 0) {
								reconciliationPojo.setVOUCHERTYPE("Payment");
							}else if(ledgerDetails2.getDEBIT() > 0) {
								reconciliationPojo.setVOUCHERTYPE("Receipt");
							}else {
								reconciliationPojo.setVOUCHERTYPE("");
							}
							//reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
	
							Reconciliationedit.add(reconciliationPojo);
						
						}
					}
				}
				
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 0);
				resultJsonInt.put("TYPE", 0);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("RECEDIT", Reconciliationedit);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				resultJsonInt.put("RCREDIT", rcredit);
				resultJsonInt.put("RDEPOSIT", rdeposit);
				resultJsonInt.put("BANKOPENINGBAL", bankopeningbal);
				resultJsonInt.put("LEDGEROPENINGBAL", ledgeropeningbal);
				resultJsonInt.put("LEDGERCLOSINGGBAL", ledgerclosingbal);
				resultJsonInt.put("RMONTH", rdatemonth);
				resultJsonInt.put("RYEAR", rdateyear);
				response.getWriter().write(resultJsonInt.toString());
				
			
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS", 2);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", 0.0);
				resultJsonInt.put("PENDINGS", 0.0);
				resultJsonInt.put("BANK", 0.0);
				resultJsonInt.put("RCREDIT", 0.0);
				resultJsonInt.put("RDEPOSIT", 0.0);
				resultJsonInt.put("BANKOPENINGBAL", 0.0);
				resultJsonInt.put("LEDGEROPENINGBAL", 0.0);
				resultJsonInt.put("LEDGERCLOSINGGBAL", 0.0);
				resultJsonInt.put("RMONTH", "");
				resultJsonInt.put("RYEAR", "");
				response.getWriter().write(resultJsonInt.toString());
			}
		}
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_Summary")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetailsRec> ledgerDetails = new ArrayList<LedgerDetailsRec>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();

			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				ledgerDetails = journalDAO.getLedgerDetailsByAccountAllRec(plant, account);
				for (LedgerDetailsRec ledgerDetails2 : ledgerDetails) {
					if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {

									Hashtable ht1 = new Hashtable();
									ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
									ht1.put("PLANT", plant);
									List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = (String) payHdr.get("ACCOUNT_NAME");
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = vendorDAO.getVendorName(plant,
													(String) payhdrvendo);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
													+ vendorJson.getString("VNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
									reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							Hashtable ht1 = new Hashtable();
							ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht1.put("PLANT", plant);
							List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
							if(billpayHdrList.size() > 0) {
								Map payHdr = (Map) billpayHdrList.get(0);
								String vouchertype = "Payment";
								String payhdrvendo = (String) payHdr.get("VENDNO");
								if (payhdrvendo.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
								reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
								reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());
	
								Reconciliation.add(reconciliationPojo);
							}
						}

					}else if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {

						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
									InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
											Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
									String vouchertype = "Receipt";
									String payhdrcustno = payHdr.getCUSTNO();
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrcustno.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = payHdr.getACCOUNT_NAME();
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
													+ vendorJson.getString("CNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									String accountcode = coaDAO.GetAccountCodeByName(accountname, plant);
									reconciliationPojo.setACCOUNT(accountcode+" "+accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
									reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
									Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
							String vouchertype = "Receipt";
							String payhdrcustno = payHdr.getCUSTNO();
							if (payhdrcustno.equalsIgnoreCase("N/A")) {
								vouchertype = "Contra";
							}

							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
							reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
							reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
							reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

							Reconciliation.add(reconciliationPojo);
						}

					}else {
						ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
						reconciliationPojo.setPLANT(plant);
						reconciliationPojo.setDATE(ledgerDetails2.getDATE());
						String accountcode = coaDAO.GetAccountCodeByName(ledgerDetails2.getACCOUNT(), plant);
						reconciliationPojo.setACCOUNT(accountcode+" "+ledgerDetails2.getACCOUNT());
						if(ledgerDetails2.getCREDIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Payment");
						}else if(ledgerDetails2.getDEBIT() > 0) {
							reconciliationPojo.setVOUCHERTYPE("Receipt");
						}else {
							reconciliationPojo.setVOUCHERTYPE("");
						}
						//reconciliationPojo.setVOUCHERTYPE(vouchertype);
						reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
						reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
						reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
						reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
						reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

						Reconciliation.add(reconciliationPojo);
					}
				}
				
				Reconciliation = datefilterSummary(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);

				// jarray.addAll(Reconciliation);

				/*
				 * response.setContentType("application/json");
				 * response.setCharacterEncoding("UTF-8");
				 * response.getWriter().write(jarray.toString()); response.getWriter().flush();
				 * response.getWriter().close();
				 */
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("REC", Reconciliation);
				resultJsonInt.put("CBOOK", baspercbook);
				resultJsonInt.put("PENDINGS", bpending);
				resultJsonInt.put("BANK", basperbank);
				response.getWriter().write(resultJsonInt.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (action.equalsIgnoreCase("Process_Reconciliation")) {
			JSONObject resultJsonInt = new JSONObject();
			String[] bdate = null, jid = null;
			bdate = request.getParameterValues("bdate[]");
			jid = request.getParameterValues("jid[]");
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			JournalDAO journalDAO = new JournalDAO();
			try {
				
				for(int i =0 ; i < bdate.length ; i++){
					journalDAO.updatejournldet(plant, bdate[i], account, jid[i], username);
				}
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		else if (action.equalsIgnoreCase("Process_Reconciliation_New")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			String[] bdate = null, jid = null;
			bdate = request.getParameterValues("bdate[]");
			jid = request.getParameterValues("jid[]");
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String accountname = StrUtils.fString(request.getParameter("accountname")).trim();
			String recdate = StrUtils.fString(request.getParameter("recdate")).trim();
			String reccid = StrUtils.fString(request.getParameter("reccid")).trim();
			String recmonth = StrUtils.fString(request.getParameter("recmonth")).trim();
			String recyear = StrUtils.fString(request.getParameter("recyear")).trim();
			String recbopenbal = StrUtils.fString(request.getParameter("recbopenbal")).trim();
			String recbclosebal = StrUtils.fString(request.getParameter("recbclosebal")).trim();
			String recopenbal = StrUtils.fString(request.getParameter("recopenbal")).trim();
			String recclosebal = StrUtils.fString(request.getParameter("recclosebal")).trim();
			String recdeposit = StrUtils.fString(request.getParameter("recdeposit")).trim();
			String recwithdrawl = StrUtils.fString(request.getParameter("recwithdrawl")).trim();
			
			JournalDAO journalDAO = new JournalDAO();
			try {
				
				ReconciliationHdr reconciliationHdr = new ReconciliationHdr();
				reconciliationHdr.setPLANT(plant);
				reconciliationHdr.setRDATE(recdate);
				reconciliationHdr.setCURRENCYID(reccid);
				reconciliationHdr.setRSTATUS(0);
				reconciliationHdr.setRMONTH(Integer.valueOf(recmonth));
				reconciliationHdr.setRYEAR(Integer.valueOf(recyear));
				reconciliationHdr.setRACCOUNT(accountname);
				reconciliationHdr.setBANKOPENBALANCE(Double.valueOf(recbopenbal));
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setOPENBALANCE(Double.valueOf(recopenbal));
				reconciliationHdr.setCLOSEBALANCE(Double.valueOf(recclosebal));
				reconciliationHdr.setDEPOSITS(Double.valueOf(recdeposit));
				reconciliationHdr.setWITHDRAWL(Double.valueOf(recwithdrawl));
				reconciliationHdr.setCRBY(username);
				
				int recid = reconciliationHdrDAO.adddreconciliationHdr(reconciliationHdr);
				
				
				for(int i =0 ; i < bdate.length ; i++){
					journalDAO.updatejdetbyidforreconcile(plant, bdate[i], account, jid[i], recid, username);
				}
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		else if (action.equalsIgnoreCase("Process_Reconciliation_New_re")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			String[] jid = null;
			jid = request.getParameterValues("jid[]");
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String accountname = StrUtils.fString(request.getParameter("accountname")).trim();
			String recdate = StrUtils.fString(request.getParameter("recdate")).trim();
			String reccid = StrUtils.fString(request.getParameter("reccid")).trim();
			String recmonth = StrUtils.fString(request.getParameter("recmonth")).trim();
			String recyear = StrUtils.fString(request.getParameter("recyear")).trim();
			String recbopenbal = StrUtils.fString(request.getParameter("recbopenbal")).trim();
			String recbclosebal = StrUtils.fString(request.getParameter("recbclosebal")).trim();
			String recopenbal = StrUtils.fString(request.getParameter("recopenbal")).trim();
			String recclosebal = StrUtils.fString(request.getParameter("recclosebal")).trim();
			String recdeposit = StrUtils.fString(request.getParameter("recdeposit")).trim();
			String recwithdrawl = StrUtils.fString(request.getParameter("recwithdrawl")).trim();
			String rstatus = StrUtils.fString(request.getParameter("rstatus")).trim();
			
			JournalDAO journalDAO = new JournalDAO();
			try {
				
				ReconciliationHdr reconciliationHdr = new ReconciliationHdr();
				reconciliationHdr.setPLANT(plant);
				reconciliationHdr.setRDATE(recdate);
				reconciliationHdr.setCURRENCYID(reccid);
				reconciliationHdr.setRMONTH(Integer.valueOf(recmonth));
				reconciliationHdr.setRYEAR(Integer.valueOf(recyear));
				reconciliationHdr.setRACCOUNT(accountname);
				reconciliationHdr.setBANKOPENBALANCE(Double.valueOf(recbopenbal));
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setOPENBALANCE(Double.valueOf(recopenbal));
				reconciliationHdr.setCLOSEBALANCE(Double.valueOf(recclosebal));
				reconciliationHdr.setDEPOSITS(Double.valueOf(recdeposit));
				reconciliationHdr.setWITHDRAWL(Double.valueOf(recwithdrawl));
				reconciliationHdr.setCRBY(username);
				if(rstatus.equalsIgnoreCase("open")) {
					reconciliationHdr.setRSTATUS(0);
				}else {
					reconciliationHdr.setRSTATUS(1);
				}
				
				int recid = reconciliationHdrDAO.adddreconciliationHdr(reconciliationHdr);
				
				
				for(int i =0 ; i < jid.length ; i++){
					journalDAO.updatejdetbyidforreconcilewithoutdate(plant, account, jid[i], recid, username);
				}
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		
		else if (action.equalsIgnoreCase("Process_Reconciliation_Edit")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			String[] bdate = null, jid = null;
			bdate = request.getParameterValues("bdate[]");
			jid = request.getParameterValues("jid[]");
			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String accountname = StrUtils.fString(request.getParameter("accountname")).trim();
			String recdate = StrUtils.fString(request.getParameter("recdate")).trim();
			String reccid = StrUtils.fString(request.getParameter("reccid")).trim();
			String recmonth = StrUtils.fString(request.getParameter("recmonth")).trim();
			String recyear = StrUtils.fString(request.getParameter("recyear")).trim();
			String recbopenbal = StrUtils.fString(request.getParameter("recbopenbal")).trim();
			String recbclosebal = StrUtils.fString(request.getParameter("recbclosebal")).trim();
			String recopenbal = StrUtils.fString(request.getParameter("recopenbal")).trim();
			String recclosebal = StrUtils.fString(request.getParameter("recclosebal")).trim();
			String recdeposit = StrUtils.fString(request.getParameter("recdeposit")).trim();
			String recwithdrawl = StrUtils.fString(request.getParameter("recwithdrawl")).trim();
			
			JournalDAO journalDAO = new JournalDAO();
			try {
				ReconciliationHdr reconciliationHdr = reconciliationHdrDAO.getByID(plant, rid);
				
				double deposits = reconciliationHdr.getDEPOSITS() + Double.valueOf(recdeposit);
				double withdrawls = reconciliationHdr.getWITHDRAWL() + Double.valueOf(recwithdrawl);
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setDEPOSITS(deposits);
				reconciliationHdr.setWITHDRAWL(withdrawls);
				reconciliationHdr.setUPBY(username);

				reconciliationHdrDAO.updateHdr(reconciliationHdr);
				
				for(int i =0 ; i < bdate.length ; i++){
					journalDAO.updatejdetbyidforreconcile(plant, bdate[i], account, jid[i], Integer.valueOf(rid), username);
				}
				
				/*ReconciliationHdr reconciliationHdr = new ReconciliationHdr();
				reconciliationHdr.setPLANT(plant);
				reconciliationHdr.setRDATE(recdate);
				reconciliationHdr.setCURRENCYID(reccid);
				reconciliationHdr.setRSTATUS(0);
				reconciliationHdr.setRMONTH(Integer.valueOf(recmonth));
				reconciliationHdr.setRYEAR(Integer.valueOf(recyear));
				reconciliationHdr.setRACCOUNT(accountname);
				reconciliationHdr.setBANKOPENBALANCE(Double.valueOf(recbopenbal));
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setOPENBALANCE(Double.valueOf(recopenbal));
				reconciliationHdr.setCLOSEBALANCE(Double.valueOf(recclosebal));
				reconciliationHdr.setDEPOSITS(Double.valueOf(recdeposit));
				reconciliationHdr.setWITHDRAWL(Double.valueOf(recwithdrawl));
				reconciliationHdr.setCRBY(username);
				
				int recid = reconciliationHdrDAO.adddreconciliationHdr(reconciliationHdr);
				
				
				for(int i =0 ; i < bdate.length ; i++){
					journalDAO.updatejdetbyidforreconcile(plant, bdate[i], account, jid[i], recid, username);
				}*/
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		
		else if (action.equalsIgnoreCase("Process_Reconciliation_Edit_re")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			String[] jid = null;
			//bdate = request.getParameterValues("bdate[]");
			jid = request.getParameterValues("jid[]");
			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String accountname = StrUtils.fString(request.getParameter("accountname")).trim();
			String recdate = StrUtils.fString(request.getParameter("recdate")).trim();
			String reccid = StrUtils.fString(request.getParameter("reccid")).trim();
			String recmonth = StrUtils.fString(request.getParameter("recmonth")).trim();
			String recyear = StrUtils.fString(request.getParameter("recyear")).trim();
			String recbopenbal = StrUtils.fString(request.getParameter("recbopenbal")).trim();
			String recbclosebal = StrUtils.fString(request.getParameter("recbclosebal")).trim();
			String recopenbal = StrUtils.fString(request.getParameter("recopenbal")).trim();
			String recclosebal = StrUtils.fString(request.getParameter("recclosebal")).trim();
			String recdeposit = StrUtils.fString(request.getParameter("recdeposit")).trim();
			String recwithdrawl = StrUtils.fString(request.getParameter("recwithdrawl")).trim();
			String rstatus = StrUtils.fString(request.getParameter("rstatus")).trim();
			
			JournalDAO journalDAO = new JournalDAO();
			try {
				ReconciliationHdr reconciliationHdr = reconciliationHdrDAO.getByID(plant, rid);
				
				double deposits = reconciliationHdr.getDEPOSITS() + Double.valueOf(recdeposit);
				double withdrawls = reconciliationHdr.getWITHDRAWL() + Double.valueOf(recwithdrawl);
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setCLOSEBALANCE(Double.valueOf(recclosebal));
				reconciliationHdr.setDEPOSITS(deposits);
				reconciliationHdr.setWITHDRAWL(withdrawls);
				reconciliationHdr.setUPBY(username);
				if(rstatus.equalsIgnoreCase("open")) {
					reconciliationHdr.setRSTATUS(0);
				}else {
					reconciliationHdr.setRSTATUS(1);
				}

				reconciliationHdrDAO.updateHdr(reconciliationHdr);
				
				journalDAO.updatereconciliationstatus(plant, Integer.valueOf(rid), username, 0);
				
				for(int i =0 ; i < jid.length ; i++){
					journalDAO.updatejdetbyidforreconcilewithoutdate(plant, account, jid[i], Integer.valueOf(rid), username);
				}
				
				/*ReconciliationHdr reconciliationHdr = new ReconciliationHdr();
				reconciliationHdr.setPLANT(plant);
				reconciliationHdr.setRDATE(recdate);
				reconciliationHdr.setCURRENCYID(reccid);
				reconciliationHdr.setRSTATUS(0);
				reconciliationHdr.setRMONTH(Integer.valueOf(recmonth));
				reconciliationHdr.setRYEAR(Integer.valueOf(recyear));
				reconciliationHdr.setRACCOUNT(accountname);
				reconciliationHdr.setBANKOPENBALANCE(Double.valueOf(recbopenbal));
				reconciliationHdr.setBANKCLOSEBALANCE(Double.valueOf(recbclosebal));
				reconciliationHdr.setOPENBALANCE(Double.valueOf(recopenbal));
				reconciliationHdr.setCLOSEBALANCE(Double.valueOf(recclosebal));
				reconciliationHdr.setDEPOSITS(Double.valueOf(recdeposit));
				reconciliationHdr.setWITHDRAWL(Double.valueOf(recwithdrawl));
				reconciliationHdr.setCRBY(username);
				
				int recid = reconciliationHdrDAO.adddreconciliationHdr(reconciliationHdr);
				
				
				for(int i =0 ; i < bdate.length ; i++){
					journalDAO.updatejdetbyidforreconcile(plant, bdate[i], account, jid[i], recid, username);
				}*/
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		
		else if (action.equalsIgnoreCase("Reconciliation_Bounce")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();

			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			
			JournalDAO journalDAO = new JournalDAO();
			try {
				ReconciliationHdr reconciliationHdr = reconciliationHdrDAO.getByID(plant, rid);
				reconciliationHdr.setRSTATUS(2);
				reconciliationHdrDAO.updateHdr(reconciliationHdr);

				journalDAO.updatereconciliationstatus(plant, Integer.valueOf(rid), username, 0);

				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		
		
		
		else if (action.equalsIgnoreCase("Get_Reconciliation_excel")) {
//			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String account = StrUtils.fString(request.getParameter("AccountId")).trim();
			String datestatus = StrUtils.fString(request.getParameter("datestatus")).trim();
			String aname = StrUtils.fString(request.getParameter("aname")).trim();
			String acode = StrUtils.fString(request.getParameter("acode")).trim();
			JournalDAO journalDAO = new JournalDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			List<LedgerDetailsRec> ledgerDetails = new ArrayList<LedgerDetailsRec>();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			VendMstDAO vendorDAO = new VendMstDAO();

			InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
			CoaDAO coaDAO = new CoaDAO();
			CustMstDAO cusDAO = new CustMstDAO();
			HSSFWorkbook workbook = null;
			List<ReconciliationPojo> Reconciliation = new ArrayList<ReconciliationPojo>();
			try {
				ledgerDetails = journalDAO.getLedgerDetailsByAccountAllRec(plant, account);
				for (LedgerDetailsRec ledgerDetails2 : ledgerDetails) {
					if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("PURCHASEPAYMENT")) {
						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = billPaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {

									Hashtable ht1 = new Hashtable();
									ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
									ht1.put("PLANT", plant);
									List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
									Map payHdr = (Map) billpayHdrList.get(0);
									String vouchertype = "Payment";
									String payhdrvendo = (String) payHdr.get("VENDNO");
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrvendo.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = (String) payHdr.get("ACCOUNT_NAME");
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, (String) payhdrvendo);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = vendorDAO.getVendorName(plant,
													(String) payhdrvendo);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO") + "-"
													+ vendorJson.getString("VNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									reconciliationPojo.setACCOUNT(accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
									reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							Hashtable ht1 = new Hashtable();
							ht1.put("ID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
							ht1.put("PLANT", plant);
							List billpayHdrList = billpaymentDao.getBillPaymentHdrList(ht1);
							if(billpayHdrList.size() > 0) {
								Map payHdr = (Map) billpayHdrList.get(0);
								String vouchertype = "Payment";
								String payhdrvendo = (String) payHdr.get("VENDNO");
								if (payhdrvendo.equalsIgnoreCase("N/A")) {
									vouchertype = "Contra";
								}
	
								ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
								reconciliationPojo.setPLANT(plant);
								reconciliationPojo.setDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setACCOUNT(ledgerDetails2.getACCOUNT());
								reconciliationPojo.setVOUCHERTYPE(vouchertype);
								reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
								reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
								reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
								reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
								reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());
	
								Reconciliation.add(reconciliationPojo);
							}
						}

					}

					if (ledgerDetails2.getTRANSACTION_TYPE().equalsIgnoreCase("SALESPAYMENT")) {

						Hashtable ht = new Hashtable();
						ht.put("PAYMENTID", String.valueOf(ledgerDetails2.getTRANSACTION_ID()));
						ht.put("PLANT", plant);
						List pdcDetailList = invoicePaymentDAO.getpdcbipayid(ht);

						if (pdcDetailList.size() > 0) {
							for (int j = 0; j < pdcDetailList.size(); j++) {
								Map pdcdet = (Map) pdcDetailList.get(j);
								String status = (String) pdcdet.get("STATUS");
								String checkdate = (String) pdcdet.get("CHEQUE_DATE");
								String checkNo = (String) pdcdet.get("CHECQUE_NO");
								String jcheckNO = ledgerDetails2.getTRANSACTION_DETAILS().replaceAll("Bank check no: ",
										"");
								if (status.equalsIgnoreCase("PROCESSED") && jcheckNO.equalsIgnoreCase(checkNo)) {
									InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
											Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
									String vouchertype = "Receipt";
									String payhdrcustno = payHdr.getCUSTNO();
									String accountname = ledgerDetails2.getACCOUNT();
									if (payhdrcustno.equalsIgnoreCase("N/A")) {
										vouchertype = "Contra";
										accountname = payHdr.getACCOUNT_NAME();
									} else {
										JSONObject coaJson1 = coaDAO.getCOAByName(plant, payhdrcustno);
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {
											JSONObject vendorJson = cusDAO.getCustomerName(plant, payhdrcustno);
											coaJson1 = coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO") + "-"
													+ vendorJson.getString("CNAME"));
										}
										if (coaJson1.isEmpty() || coaJson1.isNullObject()) {

										} else {
											accountname = coaJson1.getString("account_name");
										}
									}
									ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
									reconciliationPojo.setPLANT(plant);
									reconciliationPojo.setDATE(ledgerDetails2.getDATE());
									reconciliationPojo.setACCOUNT(accountname);
									reconciliationPojo.setVOUCHERTYPE(vouchertype);
									reconciliationPojo.setINSTRUMENTDATE(checkdate);
									reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
									reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
									reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
									reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

									Reconciliation.add(reconciliationPojo);
								}
							}
						} else {

							InvPaymentHeader payHdr = invoicePaymentDAO.getInvoicePaymentById(
									Integer.valueOf(ledgerDetails2.getTRANSACTION_ID()), plant, "");
							String vouchertype = "Receipt";
							String payhdrcustno = payHdr.getCUSTNO();
							if (payhdrcustno.equalsIgnoreCase("N/A")) {
								vouchertype = "Contra";
							}

							ReconciliationPojo reconciliationPojo = new ReconciliationPojo();
							reconciliationPojo.setPLANT(plant);
							reconciliationPojo.setDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setACCOUNT(ledgerDetails2.getACCOUNT());
							reconciliationPojo.setVOUCHERTYPE(vouchertype);
							reconciliationPojo.setINSTRUMENTDATE(ledgerDetails2.getDATE());
							reconciliationPojo.setDEBIT(ledgerDetails2.getDEBIT());
							reconciliationPojo.setCREDIT(ledgerDetails2.getCREDIT());
							reconciliationPojo.setID(ledgerDetails2.getJOURNALHDRID());
							reconciliationPojo.setBANKDATE(ledgerDetails2.getBANKDATE());

							Reconciliation.add(reconciliationPojo);
						}

					}
				}
				
				Reconciliation = datefilterSummary(Reconciliation, fromDate, toDate, datestatus);
				double baspercbook = journalDAO.getbankbalancecbookbal(plant, account);
				double bpending = journalDAO.getpendingnotreflectedinbank(plant, account);
				double basperbank= journalDAO.getbalanceasperbank(plant, account);
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
				String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
				ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
				Map plntMap = (Map) plntList.get(0);
				String PLNTDESC = (String) plntMap.get("PLNTDESC");
				workbook = populateExcelFprRec(Reconciliation, numberOfDecimal,baspercbook,bpending,basperbank,PLNTDESC,aname,acode,plant);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=BankReconciliation.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}
		
		else if (action.equalsIgnoreCase("create_reverse_journal")) {
			UserTransaction ut = null;
			int jid=0;
			try {
					String journalid = StrUtils.fString(request.getParameter("ID")).trim();
					jid = Integer.valueOf(journalid);
					JournalHeader journalHeader = new JournalHeader();
					List<JournalDetail> journalDetailList = new ArrayList<>();
					List<JournalAttachment> journalAttachList = new ArrayList<>();
					
					JournalDAO journalDAO=new JournalDAO();
					Journal hjournal=journalDAO.getJournalById(plant, Integer.valueOf(journalid));
					JournalHeader jHeader=hjournal.getJournalHeader();
					List<JournalDetail> jDetailList=hjournal.getJournalDetails();


					// Journal Header
					journalHeader.setPLANT(jHeader.getPLANT());
					journalHeader.setJOURNAL_DATE(jHeader.getJOURNAL_DATE());
					journalHeader.setJOURNAL_STATUS(jHeader.getJOURNAL_STATUS());
					journalHeader.setJOURNAL_TYPE(jHeader.getJOURNAL_TYPE());
					journalHeader.setNOTE(jHeader.getNOTE());
					journalHeader.setREFERENCE(jHeader.getREFERENCE());
					journalHeader.setCURRENCYID(jHeader.getCURRENCYID());
					journalHeader.setTRANSACTION_TYPE("CONTRA");
					journalHeader.setTRANSACTION_ID(journalid);
					journalHeader.setSUB_TOTAL(jHeader.getSUB_TOTAL());
					journalHeader.setTOTAL_AMOUNT(jHeader.getTOTAL_AMOUNT());
					journalHeader.setCRAT(DateUtils.getDateTime());
					journalHeader.setCRBY(username);

					// Journal Details
					for (JournalDetail joDetail : jDetailList) {
						JournalDetail journalDetail = new JournalDetail();
						journalDetail.setPLANT(joDetail.getPLANT());
						journalDetail.setACCOUNT_ID(joDetail.getACCOUNT_ID());
						journalDetail.setACCOUNT_NAME(joDetail.getACCOUNT_NAME());
						journalDetail.setDEBITS(joDetail.getCREDITS());
						journalDetail.setCREDITS(joDetail.getDEBITS());
						int getlgt=(jHeader.getREFERENCE().length()+joDetail.getDESCRIPTION().length());
						String fnote =jHeader.getNOTE();
						if(fnote.length()>1000)
							fnote = jHeader.getNOTE().substring(0, (1000-getlgt));
						journalDetail.setDESCRIPTION(joDetail.getDESCRIPTION()+"-"+jHeader.getREFERENCE()+"-"+fnote);
						//journalDetail.setDESCRIPTION(joDetail.getDESCRIPTION());
						journalDetail.setCRAT(DateUtils.getDateTime());
						journalDetail.setCRBY(username);
						journalDetailList.add(journalDetail);
					}

					ut = DbBean.getUserTranaction();
					Journal journal = new Journal();
					journal.setJournalHeader(journalHeader);
					journal.setJournalDetails(journalDetailList);
					journal.setJournalAttachment(journalAttachList);
					/* Begin Transaction */
					ut.begin();
					int hdrid = journalService.addJournal(journal, username);
					
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put(IDBConstants.QTY, "0.0");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS","");
					movHisDao.insertIntoMovHis(htMovHis);
					
					DbBean.CommitTran(ut);
					response.sendRedirect("../banking/contrasummary");
					
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/journaldetail?ID="+jid);
				
			}

		}
		
		
		else if (action.equalsIgnoreCase("CHECK_BOUNCE_STATUS")) {
			String rid = StrUtils.fString(request.getParameter("rid")).trim();
			try {
				ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
				ReconciliationHdr reconciliationHdr = reconciliationHdrDAO.getByID(plant, rid);
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal = Calendar.getInstance();
			    cal.set(Calendar.DATE, 1);
			    cal.set(Calendar.MONTH,(reconciliationHdr.getRMONTH()-1));
			    cal.set(Calendar.YEAR,reconciliationHdr.getRYEAR());
			    System.out.println(cal.getTime());
				cal.add(Calendar.MONTH, 1);
				System.out.println(cal.getTime());
				Date result = cal.getTime();
				String pdate = formatter.format(result);
				
				
				String pdatearray[] = pdate.split("/");
				String pdatemonth = pdatearray[1];
				String pdateyear = pdatearray[2];

				boolean checkreconcil = reconciliationHdrDAO.getstatusByAccMonYear(plant, reconciliationHdr.getRACCOUNT(), Integer.valueOf(pdatemonth), Integer.valueOf(pdateyear));
				
				if(!checkreconcil) {
				
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("STATUS", "OK");
					
					response.getWriter().write(resultJsonInt.toString());
				}else{
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("STATUS", "NOT OK");
					
					response.getWriter().write(resultJsonInt.toString());
				}
				
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("STATUS","NOT OK");
				
				response.getWriter().write(resultJsonInt.toString());
			}
		}
		
		else if (action.equalsIgnoreCase("Reconciliation_Delete")) {
			JSONObject resultJsonInt = new JSONObject();
			ReconciliationHdrDAO reconciliationHdrDAO = new ReconciliationHdrDAO();
			JournalDAO journalDAO = new JournalDAO();
			try {
				String rid = StrUtils.fString(request.getParameter("rid")).trim();
				boolean rstatus = reconciliationHdrDAO.deleteById(Integer.valueOf(rid), plant);
				if(rstatus) {
					journalDAO.journalreconciliationdelete(plant, Integer.valueOf(rid), username);
				}
				resultJsonInt.put("STATUS", "OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				resultJsonInt.put("STATUS", "NOT OK");
				response.getWriter().write(resultJsonInt.toString());
				//response.sendRedirect("jsp/Reconciliation.jsp");
			}
		}
		
		else if (action.equalsIgnoreCase("JOURNAL_REVERSE_CONTRA")) {
			String journaldate = "", journalno = "", reference = "", notes = "", currency = "", subtotal = "",
					total = "", trantype = "";
			UserTransaction ut = null;
			List account = new ArrayList(), description = new ArrayList(), debits = new ArrayList(),
					credits = new ArrayList(),bankindate = new ArrayList();
			int accountCount = 0, descCount = 0, debitCount = 0, creditCount = 0,bankindatecount=0;
			try {

					String JID = StrUtils.fString(request.getParameter("JID"));
					
					JournalHeader journalHeader = new JournalHeader();
					List<JournalDetail> journalDetailList = new ArrayList<>();
					List<JournalAttachment> journalAttachList = new ArrayList<>();

					JournalDAO journalDAO=new JournalDAO();
					Journal oldjournal=journalDAO.getJournalById(plant,Integer.valueOf(JID));
					JournalHeader oldjournalHeader=oldjournal.getJournalHeader();
					List<JournalDetail> oldjournalDetails=oldjournal.getJournalDetails();
					
					
					int journalcount=journalDAO.getmaxcount(plant);
					int ajournalno=journalcount+1;
					
					journaldate = oldjournalHeader.getJOURNAL_DATE();
					journalno = String.valueOf(ajournalno);
					reference = oldjournalHeader.getTRANSACTION_TYPE()+"_"+oldjournalHeader.getTRANSACTION_ID();
					notes = oldjournalHeader.getNOTE();
					currency = oldjournalHeader.getCURRENCYID();
					subtotal = String.valueOf(oldjournalHeader.getSUB_TOTAL());
					total = String.valueOf(oldjournalHeader.getTOTAL_AMOUNT());
					trantype = "CONTRA";
					
					for (JournalDetail journalDetail : oldjournalDetails) {
						account.add(accountCount, journalDetail.getACCOUNT_NAME());
						accountCount++;
						description.add(descCount, journalDetail.getDESCRIPTION());
						descCount++;
						debits.add(debitCount,journalDetail.getCREDITS());
						debitCount++;
						credits.add(creditCount,journalDetail.getDEBITS());
						creditCount++;	
						bankindate.add(bankindatecount,(journalDetail.getBANKDATE()== null || journalDetail.getBANKDATE().equals("") ? "": journalDetail.getBANKDATE()));
						bankindatecount++;
					}
					

					// Journal Header
					journalHeader.setPLANT(plant);
					journalHeader.setJOURNAL_DATE(journaldate);
					journalHeader.setJOURNAL_STATUS("PUBLISHED");
					journalHeader.setJOURNAL_TYPE("Cash");
					journalHeader.setNOTE(notes);
					journalHeader.setREFERENCE(reference);
					journalHeader.setCURRENCYID(currency);
					journalHeader.setTRANSACTION_TYPE(trantype);
					journalHeader.setSUB_TOTAL(Double.parseDouble(subtotal));
					journalHeader.setTOTAL_AMOUNT(Double.parseDouble(total));
					journalHeader.setCRAT(DateUtils.getDateTime());
					journalHeader.setCRBY(username);

					// Journal Details
					for (int i = 0; i < account.size(); i++) {
						JournalDetail journalDetail = new JournalDetail();
						journalDetail.setPLANT(plant);
						String accName = account.get(i).toString();
						CoaDAO coaDAO = new CoaDAO();
						JSONObject coaRecord = coaDAO.getCOAByName(plant, accName);
						int accId = Integer.parseInt(coaRecord.get("id").toString());
						journalDetail.setACCOUNT_ID(accId);
						journalDetail.setACCOUNT_NAME(account.get(i).toString());
						journalDetail.setDEBITS(Double.parseDouble(debits.get(i).toString()));
						journalDetail.setCREDITS(Double.parseDouble(credits.get(i).toString()));
						int getlgt=(reference.length()+description.get(i).toString().length());
						String fnote =notes;
						if(fnote.length()>1000)
							fnote = notes.substring(0, (1000-getlgt));
						journalDetail.setDESCRIPTION(description.get(i).toString()+"-"+reference+"-"+fnote);
						//journalDetail.setDESCRIPTION(description.get(i).toString());
						journalDetail.setBANKDATE(bankindate.get(i).toString());
						journalDetail.setCRAT(DateUtils.getDateTime());
						journalDetail.setCRBY(username);
						journalDetailList.add(journalDetail);
					}

					ut = DbBean.getUserTranaction();
					Journal journal = new Journal();
					journal.setJournalHeader(journalHeader);
					journal.setJournalDetails(journalDetailList);
					journal.setJournalAttachment(journalAttachList);
					/* Begin Transaction */
					ut.begin();
					int hdrid = journalService.addJournal(journal, username);
					
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htMovHis = new Hashtable();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
					htMovHis.put(IDBConstants.ITEM, "");
					htMovHis.put(IDBConstants.QTY, "0.0");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+hdrid);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS","");
					movHisDao.insertIntoMovHis(htMovHis);
					
					DbBean.CommitTran(ut);
						response.sendRedirect("../banking/contradetail?ID="+journalno);
	
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../banking/contrasummary");
			}

		}
		
		
	}
	
	public List<ReconciliationPojo> datefilter(List<ReconciliationPojo> Reconciliation, String fromDate, String toDate, String datestatus) throws ParseException {
			
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");  
		List<ReconciliationPojo> Reconciliationold = Reconciliation;
		List<ReconciliationDatePojo> ReconciliationDate = new ArrayList<ReconciliationDatePojo>();
		for (ReconciliationPojo reconciliationPojo : Reconciliation) {
			ReconciliationDatePojo RecDate = new ReconciliationDatePojo();
			RecDate.setID(reconciliationPojo.getID());
			RecDate.setACCOUNT(reconciliationPojo.getACCOUNT());
			RecDate.setCREDIT(reconciliationPojo.getCREDIT());
			RecDate.setDEBIT(reconciliationPojo.getDEBIT());
			RecDate.setPLANT(reconciliationPojo.getPLANT());
			RecDate.setVOUCHERTYPE(reconciliationPojo.getVOUCHERTYPE());
			RecDate.setDATE(formatter.parse(reconciliationPojo.getDATE()));
			RecDate.setINSTRUMENTDATE(formatter.parse(reconciliationPojo.getINSTRUMENTDATE()));
			ReconciliationDate.add(RecDate);
		}
		
		try {
			if(datestatus.equalsIgnoreCase("rdate")) {
				if (fromDate.length() > 0 && toDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().after(cfdate) && r.getDATE().before(ctdate)).collect(Collectors.toList());
				}else if(fromDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().after(cfdate)).collect(Collectors.toList());
				}else if(toDate.length() > 0) {
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().before(ctdate)).collect(Collectors.toList());
				}
			}
			
			if(datestatus.equalsIgnoreCase("idate")) {
				
				if (fromDate.length() > 0 && toDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().after(cfdate) && r.getINSTRUMENTDATE().before(ctdate)).collect(Collectors.toList());
				}else if(fromDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().after(cfdate)).collect(Collectors.toList());
				}else if(toDate.length() > 0) {
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().before(ctdate)).collect(Collectors.toList());
				}
				
			}
			List<ReconciliationPojo> r1 =  new ArrayList<ReconciliationPojo>();
			for (ReconciliationDatePojo rd2 : ReconciliationDate) {
				ReconciliationPojo Rec1 = new ReconciliationPojo();
				Rec1.setID(rd2.getID());
				Rec1.setACCOUNT(rd2.getACCOUNT());
				Rec1.setCREDIT(rd2.getCREDIT());
				Rec1.setDEBIT(rd2.getDEBIT());
				Rec1.setPLANT(rd2.getPLANT());
				Rec1.setVOUCHERTYPE(rd2.getVOUCHERTYPE());
				Rec1.setDATE(formatter.format(rd2.getDATE()));
				Rec1.setINSTRUMENTDATE(formatter.format(rd2.getINSTRUMENTDATE()));
				r1.add(Rec1);
			}
			
			return r1;
		}catch (Exception e) {
			return Reconciliationold;
		}
	}
	
	public List<ReconciliationPojo> datefilterSummary(List<ReconciliationPojo> Reconciliation, String fromDate, String toDate, String datestatus) throws ParseException {
		
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");  
		List<ReconciliationPojo> Reconciliationold = Reconciliation;
		List<ReconciliationDatePojo> ReconciliationDate = new ArrayList<ReconciliationDatePojo>();
		for (ReconciliationPojo reconciliationPojo : Reconciliation) {
			ReconciliationDatePojo RecDate = new ReconciliationDatePojo();
			RecDate.setID(reconciliationPojo.getID());
			RecDate.setACCOUNT(reconciliationPojo.getACCOUNT());
			RecDate.setCREDIT(reconciliationPojo.getCREDIT());
			RecDate.setDEBIT(reconciliationPojo.getDEBIT());
			RecDate.setPLANT(reconciliationPojo.getPLANT());
			RecDate.setVOUCHERTYPE(reconciliationPojo.getVOUCHERTYPE());
			RecDate.setDATE(formatter.parse(reconciliationPojo.getDATE()));
			RecDate.setINSTRUMENTDATE(formatter.parse(reconciliationPojo.getINSTRUMENTDATE()));
			RecDate.setBANKDATE(reconciliationPojo.getBANKDATE());
			ReconciliationDate.add(RecDate);
		}
		
		try {
			if(datestatus.equalsIgnoreCase("rdate")) {
				if (fromDate.length() > 0 && toDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().after(cfdate) && r.getDATE().before(ctdate)).collect(Collectors.toList());
				}else if(fromDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().after(cfdate)).collect(Collectors.toList());
				}else if(toDate.length() > 0) {
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getDATE().before(ctdate)).collect(Collectors.toList());
				}
			}
			
			if(datestatus.equalsIgnoreCase("idate")) {
				
				if (fromDate.length() > 0 && toDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().after(cfdate) && r.getINSTRUMENTDATE().before(ctdate)).collect(Collectors.toList());
				}else if(fromDate.length() > 0) {
					Date cfdate = formatter.parse(fromDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().after(cfdate)).collect(Collectors.toList());
				}else if(toDate.length() > 0) {
					Date ctdate = formatter.parse(toDate);
					ReconciliationDate = ReconciliationDate.stream().filter((r)-> r.getINSTRUMENTDATE().before(ctdate)).collect(Collectors.toList());
				}
				
			}
			List<ReconciliationPojo> r1 =  new ArrayList<ReconciliationPojo>();
			for (ReconciliationDatePojo rd2 : ReconciliationDate) {
				ReconciliationPojo Rec1 = new ReconciliationPojo();
				Rec1.setID(rd2.getID());
				Rec1.setACCOUNT(rd2.getACCOUNT());
				Rec1.setCREDIT(rd2.getCREDIT());
				Rec1.setDEBIT(rd2.getDEBIT());
				Rec1.setPLANT(rd2.getPLANT());
				Rec1.setVOUCHERTYPE(rd2.getVOUCHERTYPE());
				Rec1.setDATE(formatter.format(rd2.getDATE()));
				Rec1.setINSTRUMENTDATE(formatter.format(rd2.getINSTRUMENTDATE()));
				Rec1.setBANKDATE(rd2.getBANKDATE());
				r1.add(Rec1);
			}
			
			return r1;
		}catch (Exception e) {
			return Reconciliationold;
		}
	}

	public HSSFWorkbook populateExcel(String headerTitle, String excelHeaderDate, String excelData, String noOfDecimal)
			throws JSONException {
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("JournalReport");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(headerTitle);
		cell.setCellStyle(my_style);

		HSSFRow row1 = spreadsheet.createRow(1);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellValue("As of " + excelHeaderDate);
		cell1.setCellStyle(my_style);

		HSSFRow row2 = spreadsheet.createRow(3);
		HSSFCell cellAccountHeader = row2.createCell(0);
		cellAccountHeader.setCellValue("Account");
		cellAccountHeader.setCellStyle(my_style);
		HSSFCell netDebitHeader = row2.createCell(1);
		netDebitHeader.setCellValue("Debit");
		netDebitHeader.setCellStyle(my_style);
		HSSFCell netCreditHeader = row2.createCell(2);
		netCreditHeader.setCellValue("Credit");
		netCreditHeader.setCellStyle(my_style);
		
		int dataRow = 4;
		

		
		org.json.JSONArray array = new org.json.JSONArray(excelData);
		
		DecimalFormat  n = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US); 
		DecimalFormatSymbols symbols = n.getDecimalFormatSymbols();
		symbols.setCurrencySymbol(""); // Don't use null.
		n.setDecimalFormatSymbols(symbols);
		
		String existingrowHeader = "";

		for (int i = 0; i < array.length(); i++) 
		{
				org.json.JSONObject object = array.getJSONObject(i);
			
				String rowHeader = object.getString("DATE") + "-"+object.getString("TRANSACTION_TYPE") +"  "+object.getString("TRANSACTION_ID");
				
				if(!existingrowHeader.equalsIgnoreCase(rowHeader))
				{
					HSSFRow row4 = spreadsheet.createRow(dataRow++);
					HSSFCell row4Data1 = row4.createCell(0);
					row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(rowHeader)));
					row4Data1.setCellStyle(my_style);
					
				}
				
				

				HSSFRow row5 = spreadsheet.createRow(dataRow++);
				HSSFCell row4Data11 = row5.createCell(0);
				row4Data11.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("ACCOUNT"))));
				
				
				
				String netDebitVal = StrUtils.fString(object.getString("DEBIT"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);		
				netDebitVal = n.format(Double.parseDouble(netDebitVal));
				HSSFCell row4Data2 = row5.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(netDebitVal));

				String netCreditVal = StrUtils.fString(object.getString("CREDIT"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);
				netCreditVal = n.format(Double.parseDouble(netCreditVal));

				HSSFCell row4Data3 = row5.createCell(2);
				row4Data3.setCellValue(new HSSFRichTextString(netCreditVal));
				//dataRow++;
				existingrowHeader = rowHeader;
			

		}



		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);

		return workbook;
	}
	
	public HSSFWorkbook populateExcelFprRec(List<ReconciliationPojo> Reconciliation,String noOfDecimal,double cbookv,double pendingv,double bankv,String pladesc,String aname,String acode,String plant)
			throws JSONException {
		// Create blank workbook		
		CoaDAO coaDAO = new CoaDAO();
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("BankReconciliation");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(pladesc);
		cell.setCellStyle(my_style);
		
		HSSFRow rowh = spreadsheet.createRow(1);
		HSSFCell cellh = rowh.createCell(0);
		cellh.setCellValue("Bank Reconciliation");
		cellh.setCellStyle(my_style);
		
		HSSFRow rowb = spreadsheet.createRow(3);
		HSSFCell cellb = rowb.createCell(0);
		cellb.setCellValue("Account Name");
		cellb.setCellStyle(my_style);
		HSSFCell cellbn = rowb.createCell(1);
		cellbn.setCellValue(acode+" "+aname);


		HSSFRow row2 = spreadsheet.createRow(5);
		HSSFCell dateheader = row2.createCell(0);
		dateheader.setCellValue("DATE");
		dateheader.setCellStyle(my_style);
		HSSFCell particularsHeader = row2.createCell(1);
		particularsHeader.setCellValue("PARTICULARS");
		particularsHeader.setCellStyle(my_style);
		HSSFCell vtypeHeader = row2.createCell(2);
		vtypeHeader.setCellValue("VOUCHER TYPE");
		vtypeHeader.setCellStyle(my_style);
		HSSFCell idateHeader = row2.createCell(3);
		idateHeader.setCellValue("INSTRUMENT DATE");
		idateHeader.setCellStyle(my_style);
		HSSFCell bdateHeader = row2.createCell(4);
		bdateHeader.setCellValue("BANK DATE");
		bdateHeader.setCellStyle(my_style);
		HSSFCell debitHeader = row2.createCell(5);
		debitHeader.setCellValue("DEBIT");
		debitHeader.setCellStyle(my_style);
		HSSFCell creditHeader = row2.createCell(6);
		creditHeader.setCellValue("	CREDIT");
		creditHeader.setCellStyle(my_style);
		
		int dataRow = 6;
		
		
//		String existingrowHeader = "";
		
		for (ReconciliationPojo r1 : Reconciliation) {
			HSSFRow row3 = spreadsheet.createRow(dataRow);
			
			String accountcode = coaDAO.GetAccountCodeByName(StrUtils.fString(r1.getACCOUNT()), plant);
			
			HSSFCell datedet = row3.createCell(0);
			datedet.setCellValue(new HSSFRichTextString(StrUtils.fString(r1.getDATE())));
			HSSFCell particular = row3.createCell(1);
			particular.setCellValue(new HSSFRichTextString(accountcode+" "+StrUtils.fString(r1.getACCOUNT())));
			HSSFCell vtype = row3.createCell(2);
			vtype.setCellValue(new HSSFRichTextString(StrUtils.fString(r1.getVOUCHERTYPE())));
			HSSFCell idate = row3.createCell(3);
			idate.setCellValue(new HSSFRichTextString(StrUtils.fString(r1.getINSTRUMENTDATE())));
			HSSFCell bdate = row3.createCell(4);
			bdate.setCellValue(new HSSFRichTextString(StrUtils.fString(r1.getBANKDATE())));
			HSSFCell debit = row3.createCell(5);
			debit.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(r1.getDEBIT(), noOfDecimal)));
			HSSFCell credit = row3.createCell(6);
			credit.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(r1.getCREDIT(), noOfDecimal)));
			
			dataRow++;
		}
		
		dataRow = dataRow + 2;
		
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,3,4));
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,5,6));
		
		HSSFRow row4 = spreadsheet.createRow(dataRow);
		HSSFCell cbook = row4.createCell(3);
		cbook.setCellValue(new HSSFRichTextString("Balance as per company books"));
		cbook.setCellStyle(my_style);
		HSSFCell cbookvalue = row4.createCell(5);
		cbookvalue.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(cbookv, noOfDecimal)));
		
		
		
		dataRow = dataRow + 1;
		
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,3,4));
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,5,6));
		
		HSSFRow row5 = spreadsheet.createRow(dataRow);
		HSSFCell pending = row5.createCell(3);
		pending.setCellValue(new HSSFRichTextString("Amounts not reflected in bank"));
		pending.setCellStyle(my_style);
		HSSFCell pendingvalue = row5.createCell(5);
		pendingvalue.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(pendingv, noOfDecimal)));
		
		
		
		dataRow = dataRow + 2;
		
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,3,4));
		spreadsheet.addMergedRegion(new CellRangeAddress(dataRow,dataRow,5,6));
		
		HSSFRow row6 = spreadsheet.createRow(dataRow);
		HSSFCell bankd = row6.createCell(3);
		bankd.setCellValue(new HSSFRichTextString("Balance as per bank"));
		bankd.setCellStyle(my_style);
		HSSFCell bankdvalue = row6.createCell(5);
		bankdvalue.setCellValue(new HSSFRichTextString(StrUtils.addZeroes(bankv, noOfDecimal)));
		
		

		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);

		return workbook;
	}
	
	private JSONObject getjournalSummaryView(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        JournalDAO journalDAO = new JournalDAO();
        ArrayList movQryList  = new ArrayList();
        String fdate="",tdate="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           String  TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));

           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

           String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);   
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
		   if(StrUtils.fString(TRAN_TYPE).length() > 0)       ht.put("TRANSACTION_TYPE",TRAN_TYPE);
		   movQryList = journalDAO.getJournalSummaryView(ht,fdate,tdate,PLANT);	
		   if (movQryList.size() > 0) {
            int Index = 0;
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("ID",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("JOURNAL_DATE",StrUtils.fString((String)lineArr.get("JOURNAL_DATE")));
                    	 resultJsonInt.put("REFERENCE",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("JOURNAL_STATUS",StrUtils.fString((String)lineArr.get("JOURNAL_STATUS")));
                    	 resultJsonInt.put("NOTE",StrUtils.fString((String)lineArr.get("NOTE")));
                    	 resultJsonInt.put("TOTAL_AMOUNT",unitCostValue);                    	 
                    	 resultJsonInt.put("CRBY",StrUtils.fString((String)lineArr.get("CRBY")));
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
                resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }

        return resultJson;        
        
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
