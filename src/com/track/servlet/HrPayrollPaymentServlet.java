package com.track.servlet;

import java.awt.Color;
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

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.HrClaimDAO;
import com.track.dao.HrPayrollHDRDAO;
import com.track.dao.HrPayrollPaymentDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.HrClaim;
import com.track.db.object.HrClaimPojo;
import com.track.db.object.HrPayrollHDR;
import com.track.db.object.HrPayrollPaymentAttachment;
import com.track.db.object.HrPayrollPaymentDet;
import com.track.db.object.HrPayrollPaymentHdr;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.PayrollPojo;
import com.track.db.util.BillUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/HrPayrollPaymentServlet")
public class HrPayrollPaymentServlet  extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.HrPayrollPaymentServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.HrPayrollPaymentServlet_PRINTPLANTMASTERINFO;
	
/** Invoice Payment Logic -Start**/
	
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String action = StrUtils.fString(req.getParameter("Submit")).trim();
	String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
	String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
	
	
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	HrClaimDAO hrClaimDAO = new HrClaimDAO();
	DateUtils dateutils = new DateUtils();
    MovHisDAO movHisDao = new MovHisDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();
    if(action.equalsIgnoreCase("Save")) {

		

		UserTransaction ut = null;
		HrPayrollPaymentHdr hrPayrollPaymentHdr=new HrPayrollPaymentHdr(); 
		hrPayrollPaymentHdr.setPLANT(plant);
		String claimids = "",paidpdcstatus="",currencyuseqt="0",payHdrId = "",result="";

		FileItem fileUploadItem = null;
		try{
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			boolean isAdded = false;
			if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(req);
				Iterator iterator = items.iterator();
				Iterator fileIterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();						
					
					if (fileItem.isFormField()) {
						/* FINPAYROLLPAYMENTHDR */
						if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
							hrPayrollPaymentHdr.setACCOUNT_NAME(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
							hrPayrollPaymentHdr.setAMOUNTPAID(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
							hrPayrollPaymentHdr.setPAYMENT_DATE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
							hrPayrollPaymentHdr.setPAYMENT_MODE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
							hrPayrollPaymentHdr.setPAID_THROUGH(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
							hrPayrollPaymentHdr.setREFERENCE(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
							hrPayrollPaymentHdr.setAMOUNTUFP(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
							hrPayrollPaymentHdr.setAMOUNTREFUNDED(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) { 
							hrPayrollPaymentHdr.setBANK_CHARGE(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							hrPayrollPaymentHdr.setNOTE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paytype")) {
							hrPayrollPaymentHdr.setPAYMENT_TYPE(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							hrPayrollPaymentHdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
							hrPayrollPaymentHdr.setBANK_BRANCH(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
							hrPayrollPaymentHdr.setCHEQUE_NO(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
							hrPayrollPaymentHdr.setCHEQUE_DATE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
							hrPayrollPaymentHdr.setCHEQUE_AMOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
							 paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("claimids")) {
							claimids=StrUtils.fString(fileItem.getString()).trim();
						}
					}

				}

				
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				
				
				hrPayrollPaymentHdr.setISPDCPROCESS(Short.valueOf("0"));
				hrPayrollPaymentHdr.setCRBY(username);
				int paymentHdrId = hrPayrollPaymentDAO.addHDR(hrPayrollPaymentHdr);
				payHdrId = Integer.toString(paymentHdrId);
				int empid =0;
				if(paymentHdrId > 0) {
					
					String[] claim = claimids.split(",");
					for (int i = 0; i < claim.length; i++) {
						int claimid = Integer.valueOf(claim[i]);
						if(claimid != 0) {
							
							HrClaim claimdata = hrClaimDAO.getHrClaimById(plant, claimid);
							empid = claimdata.getEMPNOID();
							HrPayrollPaymentDet hrPayrollPaymentDet=new HrPayrollPaymentDet();
							hrPayrollPaymentDet.setPLANT(plant);
							hrPayrollPaymentDet.setLNNO(i);
							hrPayrollPaymentDet.setTYPE(hrPayrollPaymentHdr.getPAYMENT_TYPE());
							hrPayrollPaymentDet.setPAYHDRID(paymentHdrId);
							hrPayrollPaymentDet.setPAYID(claimid);
							hrPayrollPaymentDet.setTYPE(hrPayrollPaymentHdr.getPAYMENT_TYPE());
							hrPayrollPaymentDet.setAMOUNT(claimdata.getAMOUNT());
							hrPayrollPaymentDet.setCURRENCYUSEQT(Double.valueOf(currencyuseqt));
							hrPayrollPaymentDet.setCRBY(username);
							
							hrPayrollPaymentDAO.addDET(hrPayrollPaymentDet);
							
							claimdata.setSTATUS("Processed");
							hrClaimDAO.updateHrClaim(claimdata, username);
							
						}
					}
					
					
					while (fileIterator.hasNext()) {
						FileItem fileItem = (FileItem) fileIterator.next();							
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/PayrollPayment" + "/"+ payHdrId;
							String filetempLocation = "C:/ATTACHMENTS/PayrollPayment" + "/temp" + "/"+ payHdrId;
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
						

							HrPayrollPaymentAttachment hrPayrollPaymentAttachment=new HrPayrollPaymentAttachment();
							hrPayrollPaymentAttachment.setPLANT(plant);
							hrPayrollPaymentAttachment.setPAYHDRID(paymentHdrId);
							hrPayrollPaymentAttachment.setFileType(fileItem.getContentType());
							hrPayrollPaymentAttachment.setFileName(fileName);
							hrPayrollPaymentAttachment.setFileSize(Integer.valueOf(String.valueOf(fileItem.getSize())));
							hrPayrollPaymentAttachment.setFilePath(fileLocation);
							hrPayrollPaymentAttachment.setCRBY(username);

							hrPayrollPaymentDAO.addAttachment(hrPayrollPaymentAttachment, plant, username);
							
						}
					}
					
					
						
						String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
						double bankcharge = hrPayrollPaymentHdr.getBANK_CHARGE();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(dateutils.getDate());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("CLAIMPAYMENT");
						journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
						journalHead.setSUB_TOTAL(hrPayrollPaymentHdr.getAMOUNTPAID());
						journalHead.setTOTAL_AMOUNT(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getPAID_THROUGH());
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME(hrPayrollPaymentHdr.getPAID_THROUGH());
						journalDetail.setCREDITS(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
						journalDetails.add(journalDetail);
						
						ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(empid),plant);
						Map empmst=(Map)empmstlist.get(0);
						String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
							
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
						System.out.println("Json"+coaJson1.toString());
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						journalDetail_1.setACCOUNT_NAME(payclearing);
						journalDetail_1.setDEBITS(hrPayrollPaymentHdr.getAMOUNTPAID());
						journalDetails.add(journalDetail_1);
						
						if(bankcharge > 0){

							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
							if(coaJson2.isEmpty() || coaJson2.isNullObject()){
							}else{
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("Bank charges");
								journalDetail_2.setDEBITS(bankcharge);
								journalDetails.add(journalDetail_2);
							}
						}
							
						Journal journal=new Journal();
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService=new JournalEntry();
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
				

						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_CLAIM_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(hrPayrollPaymentHdr.getPAYMENT_DATE()));														
						//htMovHis.put(IDBConstants.ITEM, paymentMode);						
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",hrPayrollPaymentHdr.getREFERENCE());
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		
					
				}
				
				if(isAdded) {
					DbBean.CommitTran(ut);
					result = "Claim Payment Processed successfully";
					resp.sendRedirect("../payroll/claimpayment?result="+ result);
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Coudn't process claim payment";
					resp.sendRedirect("../payroll/claimpayment?result="+ result);
				}
				
			}
		}catch (Exception e) {
			 DbBean.RollbackTran(ut);
			 e.printStackTrace();
			 resp.sendRedirect("../payroll/claimpayment?result="+ e.toString());
		}
	} else if(action.equalsIgnoreCase("Edit")) {

		

		UserTransaction ut = null;
		HrPayrollPaymentHdr hrPayrollPaymentHdr=new HrPayrollPaymentHdr(); 
		hrPayrollPaymentHdr.setPLANT(plant);
		String trid = "",paidpdcstatus="",currencyuseqt="0",result="";

		FileItem fileUploadItem = null;
		try{
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			boolean isAdded = false;
			if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(req);
				Iterator iterator = items.iterator();
				Iterator fileIterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();						
					
					if (fileItem.isFormField()) {
						/* FINPAYROLLPAYMENTHDR */
						if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
							hrPayrollPaymentHdr.setACCOUNT_NAME(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("amount_paid")) {
							hrPayrollPaymentHdr.setAMOUNTPAID(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("payment_date")) {
							hrPayrollPaymentHdr.setPAYMENT_DATE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("payment_mode")) {
							hrPayrollPaymentHdr.setPAYMENT_MODE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
							hrPayrollPaymentHdr.setPAID_THROUGH(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("refrence")) {
							hrPayrollPaymentHdr.setREFERENCE(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("amountUfp")) {
							hrPayrollPaymentHdr.setAMOUNTUFP(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						if (fileItem.getFieldName().equalsIgnoreCase("amountRefunded")) {
							hrPayrollPaymentHdr.setAMOUNTREFUNDED(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("bank_charge")) { 
							hrPayrollPaymentHdr.setBANK_CHARGE(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							hrPayrollPaymentHdr.setNOTE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paytype")) {
							hrPayrollPaymentHdr.setPAYMENT_TYPE(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							hrPayrollPaymentHdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
						}

						if (fileItem.getFieldName().equalsIgnoreCase("bankname")) {
							hrPayrollPaymentHdr.setBANK_BRANCH(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-no")) {
							hrPayrollPaymentHdr.setCHEQUE_NO(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-date")) {
							hrPayrollPaymentHdr.setCHEQUE_DATE(StrUtils.fString(fileItem.getString()).trim());
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cheque-amount")) {
							hrPayrollPaymentHdr.setCHEQUE_AMOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
							 paidpdcstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("trid")) {
							trid=StrUtils.fString(fileItem.getString()).trim();
						}
					}

				}

				
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				
				hrPayrollPaymentHdr.setID(Integer.valueOf(trid));
				hrPayrollPaymentHdr.setISPDCPROCESS(Short.valueOf("0"));
				hrPayrollPaymentHdr.setCRBY(username);
				hrPayrollPaymentDAO.updateHDR(hrPayrollPaymentHdr, username);
				int paymentHdrId = Integer.valueOf(trid);
				int empid=0;
				List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(plant, paymentHdrId);
				for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
					HrClaim hrClaim = hrClaimDAO.getHrClaimById(plant, hrPayrollPaymentDet.getPAYID());
					empid = hrClaim.getEMPNOID();
				}

				if(paymentHdrId > 0) {

					while (fileIterator.hasNext()) {
						FileItem fileItem = (FileItem) fileIterator.next();							
						if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							String fileLocation = "C:/ATTACHMENTS/PayrollPayment" + "/"+ paymentHdrId;
							String filetempLocation = "C:/ATTACHMENTS/PayrollPayment" + "/temp" + "/"+ paymentHdrId;
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
						

							HrPayrollPaymentAttachment hrPayrollPaymentAttachment=new HrPayrollPaymentAttachment();
							hrPayrollPaymentAttachment.setPLANT(plant);
							hrPayrollPaymentAttachment.setPAYHDRID(paymentHdrId);
							hrPayrollPaymentAttachment.setFileType(fileItem.getContentType());
							hrPayrollPaymentAttachment.setFileName(fileName);
							hrPayrollPaymentAttachment.setFileSize(Integer.valueOf(String.valueOf(fileItem.getSize())));
							hrPayrollPaymentAttachment.setFilePath(fileLocation);
							hrPayrollPaymentAttachment.setCRBY(username);

							hrPayrollPaymentDAO.addAttachment(hrPayrollPaymentAttachment, plant, username);
							
						}
					}
					
					
						
						String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
						double bankcharge = hrPayrollPaymentHdr.getBANK_CHARGE();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(dateutils.getDate());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("CLAIMPAYMENT");
						journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
						journalHead.setSUB_TOTAL(hrPayrollPaymentHdr.getAMOUNTPAID());
						journalHead.setTOTAL_AMOUNT(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getPAID_THROUGH());
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME(hrPayrollPaymentHdr.getPAID_THROUGH());
						journalDetail.setCREDITS(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
						journalDetails.add(journalDetail);
						
						ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(empid),plant);
						Map empmst=(Map)empmstlist.get(0);
						String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
						
							
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
						System.out.println("Json"+coaJson1.toString());
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						journalDetail_1.setACCOUNT_NAME(payclearing);
						journalDetail_1.setDEBITS(hrPayrollPaymentHdr.getAMOUNTPAID());
						journalDetails.add(journalDetail_1);
						
						if(bankcharge > 0){

							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
							if(coaJson2.isEmpty() || coaJson2.isNullObject()){
							}else{
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("Bank charges");
								journalDetail_2.setDEBITS(bankcharge);
								journalDetails.add(journalDetail_2);
							}
						}
							
						Journal journal=new Journal();
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
				

						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_CLAIM_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(hrPayrollPaymentHdr.getPAYMENT_DATE()));																			
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, paymentHdrId);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",hrPayrollPaymentHdr.getREFERENCE());
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
		
					
				}
				
				if(isAdded) {
					DbBean.CommitTran(ut);
					result = "Claim Payment updated successfully";
					resp.sendRedirect("../payroll/claimpayment?result="+ result);
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Coudn't updated claim payment";
					resp.sendRedirect("../payroll/claimpayment?result="+ result);
				}
				
			}
		}catch (Exception e) {
			 DbBean.RollbackTran(ut);
			 e.printStackTrace();
			 resp.sendRedirect("../payroll/claimpayment?result="+ e.toString());
		}
	} else if (action.equals("VIEW_CLAIM_PAYMENT_SUMMARY")) {
		JSONObject jsonObjectResult = new JSONObject();
        jsonObjectResult = this.getclaimpaymentsummary(req);	      
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(jsonObjectResult.toString());
		resp.getWriter().flush();
		resp.getWriter().close();
       	       
    }else if (action.equals("VIEW_PAYMENT_PAYMENT_SUMMARY")) {
		JSONObject jsonObjectResult = new JSONObject();
        jsonObjectResult = this.getpayollpaymentsummary(req);	      
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(jsonObjectResult.toString());
		resp.getWriter().flush();
		resp.getWriter().close();
       	       
    }else if (action.equals("payprocess")) {
    	String[] payid = req.getParameterValues("appcheck");
    	String ischeck = req.getParameter("pay_ischeque");
    	String paydate = req.getParameter("payment_date");
    	String exrate = req.getParameter("CURRENCYUSEQT");
    	String amount = req.getParameter("pay_netamount");
    	String paymentmode = req.getParameter("payment_mode");
    	String account = req.getParameter("accname");
    	String bank = req.getParameter("bankname");
    	String chno= req.getParameter("chequeno");
    	String cdate = req.getParameter("chequedate");
    	String camount= req.getParameter("chequeamount");
    	String refrence= req.getParameter("refrence");
    	String note= req.getParameter("note");
    	String cid= req.getParameter("CURRENCYID");
    	String bank_charge= req.getParameter("bank_charge");
    	String currencyuseqt= req.getParameter("CURRENCYUSEQT");
    	
    	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
    	UserTransaction ut = null;
    	String result="",payHdrId="";
    	boolean isAdded = false;
    	try {
	    	
			HrPayrollPaymentHdr hrPayrollPaymentHdr=new HrPayrollPaymentHdr(); 
			hrPayrollPaymentHdr.setPLANT(plant);
			hrPayrollPaymentHdr.setAMOUNTPAID(Double.valueOf(amount));
			hrPayrollPaymentHdr.setPAYMENT_DATE(paydate);
			hrPayrollPaymentHdr.setPAYMENT_MODE(paymentmode);
			hrPayrollPaymentHdr.setPAID_THROUGH(account);
			hrPayrollPaymentHdr.setREFERENCE(refrence);
			hrPayrollPaymentHdr.setBANK_BRANCH(bank);
			hrPayrollPaymentHdr.setBANK_CHARGE(Double.valueOf(bank_charge));
			hrPayrollPaymentHdr.setCHEQUE_NO(chno);
			hrPayrollPaymentHdr.setPAYMENT_TYPE("PAYROLL");
			hrPayrollPaymentHdr.setCHEQUE_AMOUNT(Double.valueOf(camount));
			hrPayrollPaymentHdr.setACCOUNT_NAME(account);
			hrPayrollPaymentHdr.setCHEQUE_DATE(cdate);
			hrPayrollPaymentHdr.setISPDCPROCESS(Short.valueOf("0"));
			hrPayrollPaymentHdr.setCURRENCYID(cid);
			hrPayrollPaymentHdr.setNOTE(note);
			hrPayrollPaymentHdr.setCRAT(dateutils.getDateTime());
			hrPayrollPaymentHdr.setCRBY(username);
			
			int paymentHdrId = hrPayrollPaymentDAO.addHDR(hrPayrollPaymentHdr);
			payHdrId = Integer.toString(paymentHdrId);
			List<JournalDetail> journalDetails=new ArrayList<>();
			CoaDAO coaDAO=new CoaDAO();

			if(paymentHdrId > 0) {
				
				for (int i = 0; i < payid.length; i++) {
					int pid = Integer.valueOf(payid[i]);
						
					HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, pid);
						
					HrPayrollPaymentDet hrPayrollPaymentDet=new HrPayrollPaymentDet();
					hrPayrollPaymentDet.setPLANT(plant);
					hrPayrollPaymentDet.setLNNO(i+1);
					hrPayrollPaymentDet.setTYPE(hrPayrollPaymentHdr.getPAYMENT_TYPE());
					hrPayrollPaymentDet.setPAYHDRID(paymentHdrId);
					hrPayrollPaymentDet.setPAYID(pid);
					hrPayrollPaymentDet.setAMOUNT(hrPayrollHDR.getTOTAL_AMOUNT());
					hrPayrollPaymentDet.setCURRENCYUSEQT(Double.valueOf(currencyuseqt));
					hrPayrollPaymentDet.setCRBY(username);
						
					hrPayrollPaymentDAO.addDET(hrPayrollPaymentDet);
						
					hrPayrollHDR.setSTATUS("PAID");
					
					hrPayrollHDRDAO.updatepayrollhdr(hrPayrollHDR, username);
					
					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrPayrollHDR.getEMPID()),plant);
					Map empmst=(Map)empmstlist.get(0);
					String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					System.out.println("Json"+coaJson1.toString());
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setDEBITS(hrPayrollHDR.getTOTAL_AMOUNT());
					journalDetails.add(journalDetail_1);
					
				}
				

				String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
				double bankcharge = hrPayrollPaymentHdr.getBANK_CHARGE();
				//Journal Entry
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(dateutils.getDate());
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("PAYROLLPAYMENT");
				journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
				journalHead.setSUB_TOTAL(hrPayrollPaymentHdr.getAMOUNTPAID());
				journalHead.setTOTAL_AMOUNT(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				
				
				JournalDetail journalDetail=new JournalDetail();
				journalDetail.setPLANT(plant);
				JSONObject coaJson=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getPAID_THROUGH());
				System.out.println("Json"+coaJson.toString());
				journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
				journalDetail.setACCOUNT_NAME(hrPayrollPaymentHdr.getPAID_THROUGH());
				journalDetail.setCREDITS(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
				journalDetails.add(journalDetail);
				
					
				/*JournalDetail journalDetail_1=new JournalDetail();
				journalDetail_1.setPLANT(plant);
				JSONObject coaJson1=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getACCOUNT_NAME());
				System.out.println("Json"+coaJson1.toString());
				journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail_1.setACCOUNT_NAME(hrPayrollPaymentHdr.getACCOUNT_NAME());
				journalDetail_1.setDEBITS(hrPayrollPaymentHdr.getAMOUNTPAID());
				journalDetails.add(journalDetail_1);*/
				
				if(bankcharge > 0){

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
					if(coaJson2.isEmpty() || coaJson2.isNullObject()){
					}else{
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("Bank charges");
						journalDetail_2.setDEBITS(bankcharge);
						journalDetails.add(journalDetail_2);
					}
				}
					
				Journal journal=new Journal();
				journal.setJournalHeader(journalHead);
				journal.setJournalDetails(journalDetails);
				JournalService journalService=new JournalEntry();
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
		

				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYROLL_PAYMENT);
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(hrPayrollPaymentHdr.getPAYMENT_DATE()));														
				//htMovHis.put(IDBConstants.ITEM, paymentMode);						
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",hrPayrollPaymentHdr.getREFERENCE());
				isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			}
			
			if(isAdded) {
				DbBean.CommitTran(ut);
				result = "Payroll Payment Processed successfully";
				resp.sendRedirect("../payroll/paymentsummary?result="+ result);
			}
			else {
				DbBean.RollbackTran(ut);
				result = "Coudn't process Payroll payment";
				resp.sendRedirect("../payroll/paymentsummary?result="+ result);
			}
		
    	}catch (Exception e) {
    		 DbBean.RollbackTran(ut);
			 e.printStackTrace();
			 resp.sendRedirect("../payroll/paymentsummary?result="+ e.toString());
		}
    }else if (action.equals("edit_payprocess")) {
    	String[] payid = req.getParameterValues("appcheck");
    	String ischeck = req.getParameter("pay_ischeque");
    	String paydate = req.getParameter("payment_date");
    	String exrate = req.getParameter("CURRENCYUSEQT");
    	String amount = req.getParameter("pay_netamount");
    	String paymentmode = req.getParameter("payment_mode");
    	String account = req.getParameter("paid_through_account_name");
    	String bank = req.getParameter("bankname");
    	String chno= req.getParameter("chequeno");
    	String cdate = req.getParameter("chequedate");
    	String camount= req.getParameter("chequeamount");
    	String refrence= req.getParameter("refrence");
    	String note= req.getParameter("note");
    	String cid= req.getParameter("CURRENCYID");
    	String bank_charge= req.getParameter("bank_charge");
    	String currencyuseqt= req.getParameter("CURRENCYUSEQT");
    	String Transid= req.getParameter("Transid");
    	String payids= req.getParameter("payids");
    	
    	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
    	UserTransaction ut = null;
    	String result="",payHdrId="";
    	boolean isAdded = false;
    	try {
	    	
			HrPayrollPaymentHdr hrPayrollPaymentHdr=hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(Transid));
			hrPayrollPaymentHdr.setPLANT(plant);
			hrPayrollPaymentHdr.setAMOUNTPAID(Double.valueOf(amount));
			hrPayrollPaymentHdr.setPAYMENT_DATE(paydate);
			hrPayrollPaymentHdr.setPAYMENT_MODE(paymentmode);
			hrPayrollPaymentHdr.setPAID_THROUGH(account);
			hrPayrollPaymentHdr.setREFERENCE(refrence);
			hrPayrollPaymentHdr.setBANK_CHARGE(Double.valueOf(bank_charge));
			hrPayrollPaymentHdr.setPAYMENT_TYPE("PAYROLL");
			hrPayrollPaymentHdr.setACCOUNT_NAME(account);
			hrPayrollPaymentHdr.setISPDCPROCESS(Short.valueOf("0"));
			hrPayrollPaymentHdr.setCURRENCYID(cid);
			hrPayrollPaymentHdr.setNOTE(note);
			hrPayrollPaymentHdr.setUPAT(dateutils.getDateTime());
			hrPayrollPaymentHdr.setUPBY(username);
			if(ischeck.equalsIgnoreCase("0")) {
				hrPayrollPaymentHdr.setBANK_BRANCH("");
				hrPayrollPaymentHdr.setCHEQUE_NO("");
				hrPayrollPaymentHdr.setCHEQUE_DATE("");
				hrPayrollPaymentHdr.setCHEQUE_AMOUNT(Double.valueOf("0"));
			}else {
				hrPayrollPaymentHdr.setBANK_BRANCH(bank);
				hrPayrollPaymentHdr.setCHEQUE_NO(chno);
				hrPayrollPaymentHdr.setCHEQUE_DATE(cdate);
				hrPayrollPaymentHdr.setCHEQUE_AMOUNT(Double.valueOf(camount));
			}
			
			hrPayrollPaymentDAO.updateHDR(hrPayrollPaymentHdr, username);
			int paymentHdrId = Integer.valueOf(Transid);
			payHdrId = Transid;
			
			String[] paydetid = payids.split(",");
			for (int i = 0; i < paydetid.length; i++) {
				int checkid = Integer.valueOf(paydetid[i]);
				if(checkid != 0) {
					HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, checkid);
					hrPayrollHDR.setSTATUS("OPEN");				
					hrPayrollHDRDAO.updatepayrollhdr(hrPayrollHDR, username);
				}
			}

			List<HrPayrollPaymentDet> hrPayrollPaymentDetlist = hrPayrollPaymentDAO.getdetbyhdrid(plant, paymentHdrId);
			for (HrPayrollPaymentDet hrPayrollPaymentDet : hrPayrollPaymentDetlist) {
				hrPayrollPaymentDAO.DeleteDET(plant, hrPayrollPaymentDet.getID());
			}
			
			List<JournalDetail> journalDetails=new ArrayList<>();
			CoaDAO coaDAO=new CoaDAO();
			
			if(paymentHdrId > 0) {
				
				for (int i = 0; i < payid.length; i++) {
					int pid = Integer.valueOf(payid[i]);
						
					HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, pid);
						
					HrPayrollPaymentDet hrPayrollPaymentDet=new HrPayrollPaymentDet();
					hrPayrollPaymentDet.setPLANT(plant);
					hrPayrollPaymentDet.setLNNO(i+1);
					hrPayrollPaymentDet.setTYPE(hrPayrollPaymentHdr.getPAYMENT_TYPE());
					hrPayrollPaymentDet.setPAYHDRID(paymentHdrId);
					hrPayrollPaymentDet.setPAYID(pid);
					hrPayrollPaymentDet.setAMOUNT(hrPayrollHDR.getTOTAL_AMOUNT());
					hrPayrollPaymentDet.setCURRENCYUSEQT(Double.valueOf(currencyuseqt));
					hrPayrollPaymentDet.setCRBY(username);
						
					hrPayrollPaymentDAO.addDET(hrPayrollPaymentDet);
						
					hrPayrollHDR.setSTATUS("PAID");
					
					hrPayrollHDRDAO.updatepayrollhdr(hrPayrollHDR, username);
					
					ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrPayrollHDR.getEMPID()),plant);
					Map empmst=(Map)empmstlist.get(0);
					String payclearing = empmst.get("EMPNO")+"-"+empmst.get("FNAME");
					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, payclearing);
					System.out.println("Json"+coaJson1.toString());
					journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
					journalDetail_1.setACCOUNT_NAME(payclearing);
					journalDetail_1.setDEBITS(hrPayrollPaymentHdr.getAMOUNTPAID());
					journalDetails.add(journalDetail_1);
				}
				

				String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
				double bankcharge = hrPayrollPaymentHdr.getBANK_CHARGE();
				//Journal Entry
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(dateutils.getDate());
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("PAYROLLPAYMENT");
				journalHead.setTRANSACTION_ID(Integer.toString(paymentHdrId));
				journalHead.setSUB_TOTAL(hrPayrollPaymentHdr.getAMOUNTPAID());
				journalHead.setTOTAL_AMOUNT(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				
				
				JournalDetail journalDetail=new JournalDetail();
				journalDetail.setPLANT(plant);
				JSONObject coaJson=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getPAID_THROUGH());
				System.out.println("Json"+coaJson.toString());
				journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
				journalDetail.setACCOUNT_NAME(hrPayrollPaymentHdr.getPAID_THROUGH());
				journalDetail.setCREDITS(hrPayrollPaymentHdr.getAMOUNTPAID() + bankcharge);
				journalDetails.add(journalDetail);
				
					
				/*JournalDetail journalDetail_1=new JournalDetail();
				journalDetail_1.setPLANT(plant);
				JSONObject coaJson1=coaDAO.getCOAByName(plant, hrPayrollPaymentHdr.getACCOUNT_NAME());
				System.out.println("Json"+coaJson1.toString());
				journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
				journalDetail_1.setACCOUNT_NAME(hrPayrollPaymentHdr.getACCOUNT_NAME());
				journalDetail_1.setDEBITS(hrPayrollPaymentHdr.getAMOUNTPAID());
				journalDetails.add(journalDetail_1);*/
				
				if(bankcharge > 0){

					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
					if(coaJson2.isEmpty() || coaJson2.isNullObject()){
					}else{
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("Bank charges");
						journalDetail_2.setDEBITS(bankcharge);
						journalDetails.add(journalDetail_2);
					}
				}
					
				Journal journal=new Journal();
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
		

				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.UPDATE_PAYROLL_PAYMENT);
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(hrPayrollPaymentHdr.getPAYMENT_DATE()));														
				//htMovHis.put(IDBConstants.ITEM, paymentMode);						
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",hrPayrollPaymentHdr.getREFERENCE());
				isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			}
			
			if(isAdded) {
				DbBean.CommitTran(ut);
				result = "Payroll Payment Updated successfully";
				resp.sendRedirect("../payroll/paymentsummary?result="+ result);
			}
			else {
				DbBean.RollbackTran(ut);
				result = "Couldn't Update Payroll payment";
				resp.sendRedirect("../payroll/paymentsummary?result="+ result);
			}
		
    	}catch (Exception e) {
    		 DbBean.RollbackTran(ut);
			 e.printStackTrace();
			 resp.sendRedirect("../payroll/paymentsummary?result="+ e.toString());
		}
    }
	

}

protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
	String action = StrUtils.fString(req.getParameter("CMD")).trim();
	String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
	String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	
	if(action.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			int ID=Integer.parseInt(req.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			try {
				HrPayrollPaymentAttachment paymentAttachment = hrPayrollPaymentDAO.getHrPayrollPaymentAttachmentId(plant, ID);
				String filePath=paymentAttachment.getFilePath();
				String fileType=paymentAttachment.getFileType();
				String fileName=paymentAttachment.getFileName();
				fileHandling.fileDownload(filePath, fileName, fileType, resp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}else if(action.equalsIgnoreCase("removeAttachmentById"))
		{
			System.out.println("Remove Attachments by ID");
			int ID=Integer.parseInt(req.getParameter("removeid"));
			try {
				hrPayrollPaymentDAO.DeleteAttachment(plant, ID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.getWriter().write("Deleted");
		}
	
	if(action.equalsIgnoreCase("GET_PAYROLL"))
	{
		 String hdrid= StrUtils.fString(req.getParameter("HDRID"));

		 ByteArrayOutputStream out = new ByteArrayOutputStream();            
		    try {
		    	
		    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
		        PdfWriter writer = PdfWriter.getInstance(doc, out);
		        doc.open();
		    	addPDFbody(doc,hdrid,plant);
		        doc.close();
		        writer.close();
			    byte[] bytes = out.toByteArray();
			    resp.addHeader("Content-disposition","attachment;filename=PayrollPayment.pdf");
			    resp.setContentLength(bytes.length);
			    resp.getOutputStream().write(bytes);
			    resp.setContentType("application/pdf");
		    }catch (Exception e) {
				System.out.println(e);
			}
	}
	
	if(action.equalsIgnoreCase("GET_PAYROLL_PRINT"))
	{
		 String hdrid= StrUtils.fString(req.getParameter("HDRID"));

		 ByteArrayOutputStream out = new ByteArrayOutputStream();            
		    try {
		    	
		    	Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
		        PdfWriter writer = PdfWriter.getInstance(doc, out);
		        doc.open();
		    	addPDFbody(doc,hdrid,plant);
		    	doc.close();
		        writer.close();
			    byte[] bytes = out.toByteArray();
			    resp.addHeader("Content-disposition","inline;filename=PayrollPayment.pdf");
			    resp.setContentLength(bytes.length);
			    resp.getOutputStream().write(bytes);
			    resp.setContentType("application/pdf");
		    }catch (Exception e) {
				System.out.println(e);
			}
	}
	

}

private void addPDFbody(Document doc, String hdrid,String plant) throws DocumentException, IOException {
	

	PlantMstDAO plantMstDAO = new PlantMstDAO();
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	EmployeeDAO employeeDAO = new EmployeeDAO();
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
	try {
		String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

    	userBean ubean = new userBean();
    	String companyname = ubean.getCompanyName(plant).toUpperCase();
    	
    	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	HrPayrollPaymentHdr payhdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(hdrid));
    	
    	double exchangerate = 0;
    	List<PayrollPojo> PayrollPojoList = new ArrayList<PayrollPojo>();
    	List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(plant, payhdr.getID());
        for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
        	HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, hrPayrollPaymentDet.getPAYID());
        	exchangerate = exchangerate + hrPayrollPaymentDet.getCURRENCYUSEQT();
        	String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
    	    String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
    		String monthyear = months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]+"-"+hrPayrollHDR.getYEAR();
    			
    		PayrollPojo PayrollPojo = new PayrollPojo();
    		PayrollPojo.setID(hrPayrollHDR.getID());
    		PayrollPojo.setEMPCODE(empcode);
    		PayrollPojo.setEMPNAME(empname);
    		PayrollPojo.setMONTH(monthyear);
    		PayrollPojo.setPAYROLL(hrPayrollHDR.getPAYROLL());
    		PayrollPojo.setATDAYS(String.valueOf(hrPayrollHDR.getPAYDAYS()));
    		PayrollPojo.setSALARY(StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal));
    		PayrollPojo.setSTATUS(hrPayrollHDR.getSTATUS());
    		PayrollPojoList.add(PayrollPojo);
    	}
    	
		

        Paragraph preface = new Paragraph(new Chunk("PAYROLL PAYMENT", new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK))); 
        preface.setAlignment(Element.ALIGN_CENTER);
        doc.add(preface);

        
        PdfPTable tableheader = new PdfPTable(2);
		
		int[] headerdata = { 30, 125 }; // 23, 20, 25, 32 };
		tableheader.setWidths(headerdata);
		tableheader.setWidthPercentage(100);
		tableheader.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        String imageUrl = MLoggerConstant.PROPS_FOLDER + "/track/Logos/"+plant.toLowerCase()+"Logo.GIF";
        Image image1 = Image.getInstance(imageUrl);
        //image1.setAbsolutePosition(100f, 550f);
        image1.scaleAbsolute(60, 60);

		Paragraph title = new Paragraph();
		title.add(new Chunk(companyname, new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK)));
		title.setAlignment(Element.ALIGN_CENTER);

		PdfPCell cell1 = new PdfPCell();
		cell1.addElement(image1);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell cell2 = new PdfPCell();
		cell2.setPaddingTop(5);
		cell2.setPaddingRight(75);
		cell2.addElement(title);
		cell2.setBorder(Rectangle.UNDEFINED);
		
		tableheader.addCell(cell1);
		tableheader.addCell(cell2);	
		
		doc.add(tableheader);
		
		
		
		
		
		
		PdfPTable tablebody1 = new PdfPTable(3);
		
		int[] body1data = { 45, 10, 120 };
		tablebody1.setWidths(body1data);
		tablebody1.setWidthPercentage(100);
		tablebody1.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		Paragraph row1a = new Paragraph();
		row1a.add(new Chunk("Payment Date", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row1a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c1a = new PdfPCell();
		c1a.addElement(row1a);
		c1a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row1b = new Paragraph();
		row1b.add(new Chunk(payhdr.getPAYMENT_DATE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row1b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c1c = new PdfPCell();
		c1c.addElement(row1b);
		c1c.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row1c = new Paragraph();
		row1c.add(new Chunk(":", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row1c.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c1b = new PdfPCell();
		c1b.addElement(row1c);
		c1b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c1a);
		tablebody1.addCell(c1b);
		tablebody1.addCell(c1c);
		
		
		
		Paragraph row2a = new Paragraph();
		row2a.add(new Chunk("Currency", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row2a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c2a = new PdfPCell();
		c2a.addElement(row2a);
		c2a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row2b = new Paragraph();
		row2b.add(new Chunk(payhdr.getCURRENCYID(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row2b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c2c = new PdfPCell();
		c2c.addElement(row2b);
		c2c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c2b = new PdfPCell();
		c2b.addElement(row1c);
		c2b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c2a);
		tablebody1.addCell(c2b);
		tablebody1.addCell(c2c);
		
		
		
		
		Paragraph row3a = new Paragraph();
		row3a.add(new Chunk("Exchange Rate", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row3a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c3a = new PdfPCell();
		c3a.addElement(row3a);
		c3a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row3b = new Paragraph();
		row3b.add(new Chunk(StrUtils.addZeroes(exchangerate, numberOfDecimal), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row3b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c3c = new PdfPCell();
		c3c.addElement(row3b);
		c3c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c3b = new PdfPCell();
		c3b.addElement(row1c);
		c3b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c3a);
		tablebody1.addCell(c3b);
		tablebody1.addCell(c3c);
		
		Paragraph row4a = new Paragraph();
		row4a.add(new Chunk("Payment Mode", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row4a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c4a = new PdfPCell();
		c4a.addElement(row4a);
		c4a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row4b = new Paragraph();
		row4b.add(new Chunk(payhdr.getPAYMENT_MODE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row4b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c4c = new PdfPCell();
		c4c.addElement(row4b);
		c4c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c4b = new PdfPCell();
		c4b.addElement(row1c);
		c4b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c4a);
		tablebody1.addCell(c4b);
		tablebody1.addCell(c4c);
		
		Paragraph row5a = new Paragraph();
		row5a.add(new Chunk("Paid Through", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row5a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c5a = new PdfPCell();
		c5a.addElement(row5a);
		c5a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row5b = new Paragraph();
		row5b.add(new Chunk(payhdr.getPAID_THROUGH(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row5b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c5c = new PdfPCell();
		c5c.addElement(row5b);
		c5c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c5b = new PdfPCell();
		c5b.addElement(row1c);
		c5b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c5a);
		tablebody1.addCell(c5b);
		tablebody1.addCell(c5c);
		
		Paragraph row6a = new Paragraph();
		row6a.add(new Chunk("Bank Charges", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row6a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c6a = new PdfPCell();
		c6a.addElement(row6a);
		c6a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row6b = new Paragraph();
		row6b.add(new Chunk(StrUtils.addZeroes(payhdr.getBANK_CHARGE(), numberOfDecimal), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row6b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c6c = new PdfPCell();
		c6c.addElement(row6b);
		c6c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c6b = new PdfPCell();
		c6b.addElement(row1c);
		c6b.setBorder(Rectangle.UNDEFINED);
		
		tablebody1.addCell(c6a);
		tablebody1.addCell(c6b);
		tablebody1.addCell(c6c);
		
		doc.add(tablebody1);
		
			/*
			 * if(!payhdr.getBANK_BRANCH().equalsIgnoreCase("")) { PdfPTable tablebody4 =
			 * new PdfPTable(4); tablebody4.setWidthPercentage(100);
			 * tablebody4.setHorizontalAlignment(Element.ALIGN_CENTER);
			 * tablebody4.setSpacingBefore(20f);
			 * 
			 * 
			 * String[] bheading = {"BANK NAME","CHEQUE NO","CHEQUE DATE","CHEQUE AMOUNT"};
			 * String[] babody =
			 * {payhdr.getBANK_BRANCH(),payhdr.getCHEQUE_NO(),payhdr.getCHEQUE_DATE(),
			 * StrUtils.addZeroes(payhdr.getCHEQUE_AMOUNT(), numberOfDecimal)}; for (int i =
			 * 0; i < bheading.length; i++) {
			 * 
			 * Paragraph row13abh = new Paragraph(); row13abh.add(new Chunk(bheading[i], new
			 * Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK)));
			 * row13abh.setAlignment(Element.ALIGN_CENTER);
			 * 
			 * PdfPCell c13abh = new PdfPCell(); c13abh.addElement(row13abh);
			 * c13abh.setBorderWidth(1); c13abh.setBorderColor(Color.black);
			 * c13abh.setBackgroundColor(Color.LIGHT_GRAY);
			 * c13abh.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 * c13abh.setBorder(Rectangle.BOX); c13abh.setMinimumHeight(25f);
			 * 
			 * tablebody4.addCell(c13abh); }
			 * 
			 * for (int i = 0; i < babody.length; i++) {
			 * 
			 * Paragraph row13abh = new Paragraph(); row13abh.add(new Chunk(babody[i], new
			 * Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			 * row13abh.setAlignment(Element.ALIGN_CENTER);
			 * 
			 * PdfPCell c13abh = new PdfPCell(); c13abh.addElement(row13abh);
			 * c13abh.setBorderWidth(1); c13abh.setVerticalAlignment(Element.ALIGN_MIDDLE);
			 * c13abh.setBorder(Rectangle.BOX); c13abh.setBorderColor(Color.black);
			 * c13abh.setMinimumHeight(25f);
			 * 
			 * tablebody4.addCell(c13abh); }
			 * 
			 * doc.add(tablebody4); }
			 */
		
		PdfPTable tablebody2 = new PdfPTable(6);
		tablebody2.setWidthPercentage(100);
		tablebody2.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablebody2.setSpacingBefore(20f);
		
		String[] theading = {"PAYROLL","EMPLOYEE ID","EMPLOYEE","MONTH","ATTENDANCE","SALARY"};
		int lt = theading.length - 1;
		for (int i = 0; i < theading.length; i++) {
			if(lt == i) {
				Paragraph row13a = new Paragraph();
				row13a.add(new Chunk(theading[i], new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK)));
				row13a.setAlignment(Element.ALIGN_RIGHT);
				
				PdfPCell c13a = new PdfPCell();
				c13a.addElement(row13a);
				c13a.setBorderWidth(1);
				c13a.setBorderColor(Color.LIGHT_GRAY);
				c13a.setBackgroundColor(Color.LIGHT_GRAY);
				c13a.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c13a.setBorder(Rectangle.TOP);
				c13a.setMinimumHeight(25f);
		
				tablebody2.addCell(c13a);
			}else {
				Paragraph row13a = new Paragraph();
				row13a.add(new Chunk(theading[i], new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK)));
				row13a.setAlignment(Element.ALIGN_CENTER);
				
				PdfPCell c13a = new PdfPCell();
				c13a.addElement(row13a);
				c13a.setBorderWidth(1);
				c13a.setBorderColor(Color.LIGHT_GRAY);
				c13a.setBackgroundColor(Color.LIGHT_GRAY);
				c13a.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c13a.setBorder(Rectangle.TOP);
				c13a.setMinimumHeight(25f);
		
				tablebody2.addCell(c13a);
			}
		}
		
        
		for(PayrollPojo payrollPojo:PayrollPojoList){
		
			Paragraph row11a = new Paragraph();
			row11a.add(new Chunk(payrollPojo.getPAYROLL(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row11a.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell c11a = new PdfPCell();
			c11a.addElement(row11a);
			c11a.setBorderWidth(1);
			c11a.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c11a.setBorder(Rectangle.BOTTOM);
			c11a.setBorderColor(Color.LIGHT_GRAY);
			c11a.setMinimumHeight(25f);

			tablebody2.addCell(c11a);
			
			Paragraph row12b = new Paragraph();
			row12b.add(new Chunk(payrollPojo.getEMPCODE(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row12b.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell c12b = new PdfPCell();
			c12b.addElement(row12b);
			c12b.setBorderWidth(1);
			c12b.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b.setBorder(Rectangle.BOTTOM);
			c12b.setBorderColor(Color.LIGHT_GRAY);
			c12b.setMinimumHeight(25f);

			tablebody2.addCell(c12b);
			
			Paragraph row12b01 = new Paragraph();
			row12b01.add(new Chunk(payrollPojo.getEMPNAME(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row12b01.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell c12b01 = new PdfPCell();
			c12b01.addElement(row12b01);
			c12b01.setBorderWidth(1);
			c12b01.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b01.setBorder(Rectangle.BOTTOM);
			c12b01.setBorderColor(Color.LIGHT_GRAY);
			c12b01.setMinimumHeight(25f);

			tablebody2.addCell(c12b01);
			
			Paragraph row12b02 = new Paragraph();
			row12b02.add(new Chunk(payrollPojo.getMONTH(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row12b02.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell c12b02 = new PdfPCell();
			c12b02.addElement(row12b02);
			c12b02.setBorderWidth(1);
			c12b02.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b02.setBorder(Rectangle.BOTTOM);
			c12b02.setBorderColor(Color.LIGHT_GRAY);
			c12b02.setMinimumHeight(25f);

			tablebody2.addCell(c12b02);
			
			Paragraph row12b03 = new Paragraph();
			row12b03.add(new Chunk(payrollPojo.getATDAYS(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row12b03.setAlignment(Element.ALIGN_CENTER);
			
			PdfPCell c12b03 = new PdfPCell();
			c12b03.addElement(row12b03);
			c12b03.setBorderWidth(1);
			c12b03.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b03.setBorder(Rectangle.BOTTOM);
			c12b03.setBorderColor(Color.LIGHT_GRAY);
			c12b03.setMinimumHeight(25f);

			tablebody2.addCell(c12b03);
			
			
			
			Paragraph row12b05 = new Paragraph();
			row12b05.add(new Chunk(StrUtils.addZeroes(Double.valueOf(payrollPojo.getSALARY()), numberOfDecimal), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
			row12b05.setAlignment(Element.ALIGN_RIGHT);
			
			PdfPCell c12b05 = new PdfPCell();
			c12b05.addElement(row12b05);
			c12b05.setBorderWidth(1);
			c12b05.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c12b05.setBorder(Rectangle.BOTTOM);
			c12b05.setBorderColor(Color.LIGHT_GRAY);
			c12b05.setMinimumHeight(25f);

			tablebody2.addCell(c12b05);
	        
		}
		
		doc.add(tablebody2);
		
		PdfPTable tablebody5 = new PdfPTable(2);
		
		//int[] body5data = { 45, 10, 120 };
		//tablebody5.setWidths(body5data);
		tablebody5.setWidthPercentage(100);
		tablebody5.setHorizontalAlignment(Element.ALIGN_LEFT);
		tablebody5.setSpacingBefore(10f);
		
		Paragraph row17a = new Paragraph();
		row17a.add(new Chunk("Total Amount", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
		row17a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c17a = new PdfPCell();
		c17a.addElement(row17a);
		c17a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row17b = new Paragraph();
		row17b.add(new Chunk(StrUtils.addZeroes(payhdr.getAMOUNTPAID(), numberOfDecimal), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
		row17b.setAlignment(Element.ALIGN_RIGHT);
		
		PdfPCell c17c = new PdfPCell();
		c17c.addElement(row17b);
		c17c.setBorder(Rectangle.UNDEFINED);
		
		//PdfPCell c17b = new PdfPCell();
		//c17b.addElement(row1c);
		//c17b.setBorder(Rectangle.UNDEFINED);
		
		tablebody5.addCell(c17a);
		//tablebody5.addCell(c17b);
		tablebody5.addCell(c17c);
		
		doc.add(tablebody5);
		
		
		PdfPTable tablebody3 = new PdfPTable(3);
		
		int[] body3data = { 45, 10, 120 };
		tablebody3.setWidths(body3data);
		tablebody3.setWidthPercentage(100);
		tablebody3.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		Paragraph row7a = new Paragraph();
		row7a.add(new Chunk("Reference", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row7a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c7a = new PdfPCell();
		c7a.addElement(row7a);
		c7a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row7b = new Paragraph();
		row7b.add(new Chunk(payhdr.getREFERENCE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row7b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c7c = new PdfPCell();
		c7c.addElement(row7b);
		c7c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c7b = new PdfPCell();
		c7b.addElement(row1c);
		c7b.setBorder(Rectangle.UNDEFINED);
		
		tablebody3.addCell(c7a);
		tablebody3.addCell(c7b);
		tablebody3.addCell(c7c);
		
		Paragraph row8a = new Paragraph();
		row8a.add(new Chunk("Notes", new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row8a.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c8a = new PdfPCell();
		c8a.addElement(row8a);
		c8a.setBorder(Rectangle.UNDEFINED);
		
		Paragraph row8b = new Paragraph();
		row8b.add(new Chunk(payhdr.getNOTE(), new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK)));
		row8b.setAlignment(Element.ALIGN_LEFT);
		
		PdfPCell c8c = new PdfPCell();
		c8c.addElement(row8b);
		c8c.setBorder(Rectangle.UNDEFINED);
		
		PdfPCell c8b = new PdfPCell();
		c8b.addElement(row1c);
		c8b.setBorder(Rectangle.UNDEFINED);
		
		tablebody3.addCell(c8a);
		tablebody3.addCell(c8b);
		tablebody3.addCell(c8c);

		doc.add(tablebody3);
		
		
		

		
		
		
	}catch (Exception e) {
		System.out.println(e);
	}
}

private JSONObject getclaimpaymentsummary(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
    HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    DateUtils _dateUtils = new DateUtils();
    String decimalZeros = "";
    for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
        decimalZeros += "#";
    }
    DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
   
    StrUtils strUtils = new StrUtils();
    String fdate="",tdate="",taxby="";
     try {
    
       String PLANT= StrUtils.fString(request.getParameter("PLANT"));
       String  cur = StrUtils.fString(request.getParameter("CURENCY"));
       String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
       String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
       
       List<HrPayrollPaymentHdr> payrollpaymenthdrlist = hrPayrollPaymentDAO.getAllHrPayrollPaymentHdr(PLANT,"CLAIM");
       
	  
	   if (payrollpaymenthdrlist.size() > 0) {
        int iIndex = 0,Index = 0;
        int irow = 0;
        double sumprdQty = 0;String lastProduct="",covunitCostValue="0";
        double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
       
        for (HrPayrollPaymentHdr PaymentHdr : payrollpaymenthdrlist) {
                        String result="";                        
                        double cuex = 1;
                        List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(PLANT, PaymentHdr.getID());
                        for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
                        	cuex = hrPayrollPaymentDet.getCURRENCYUSEQT();
						}
                        
                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
               			String unitCostValue = String.valueOf(PaymentHdr.getAMOUNTPAID());

                        float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), decimal);
                        covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*cuex), numberOfDecimal);
                        

                	 Index = Index + 1;
                	 resultJsonInt.put("Index",(Index));
                	 resultJsonInt.put("payrollpaymentdate",PaymentHdr.getPAYMENT_DATE());
                	 resultJsonInt.put("id",PaymentHdr.getID());
                	 resultJsonInt.put("reference",PaymentHdr.getREFERENCE());
                	 resultJsonInt.put("accountname",PaymentHdr.getACCOUNT_NAME());
                	 resultJsonInt.put("paymentmode",PaymentHdr.getPAYMENT_MODE());
                	 resultJsonInt.put("paidthrough",PaymentHdr.getPAID_THROUGH());
                	 cur = PaymentHdr.getCURRENCYID();
                	 resultJsonInt.put("convamount",cur+covunitCostValue);
                	 resultJsonInt.put("exchangerate",cuex);
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

private JSONObject getpayollpaymentsummary(HttpServletRequest request) {
    JSONObject resultJson = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayErr = new JSONArray();
    HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
    DateUtils _dateUtils = new DateUtils();
    String decimalZeros = "";
    for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
        decimalZeros += "#";
    }
    DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
   
    StrUtils strUtils = new StrUtils();
    String fdate="",tdate="",taxby="";
     try {
    
       String PLANT= StrUtils.fString(request.getParameter("PLANT"));
       String  cur = StrUtils.fString(request.getParameter("CURENCY"));
       String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
       String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
       
       List<HrPayrollPaymentHdr> payrollpaymenthdrlist = hrPayrollPaymentDAO.getAllHrPayrollPaymentHdr(PLANT,"PAYROLL");
       
	  
	   if (payrollpaymenthdrlist.size() > 0) {
        int iIndex = 0,Index = 0;
        int irow = 0;
        double sumprdQty = 0;String lastProduct="",covunitCostValue="0";
        double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
       
        for (HrPayrollPaymentHdr PaymentHdr : payrollpaymenthdrlist) {
                        String result="";                        
                        double cuex = 1;
                        List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(PLANT, PaymentHdr.getID());
                        for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
                        	cuex = hrPayrollPaymentDet.getCURRENCYUSEQT();
						}
                        
                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                        JSONObject resultJsonInt = new JSONObject();
               			String unitCostValue = String.valueOf(PaymentHdr.getAMOUNTPAID());

                        float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), decimal);
                        covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*cuex), numberOfDecimal);
                        

                	 Index = Index + 1;
                	 resultJsonInt.put("Index",(Index));
                	 resultJsonInt.put("payrollpaymentdate",PaymentHdr.getPAYMENT_DATE());
                	 resultJsonInt.put("id",PaymentHdr.getID());
                	 resultJsonInt.put("reference",PaymentHdr.getREFERENCE());
                	 resultJsonInt.put("accountname",PaymentHdr.getACCOUNT_NAME());
                	 resultJsonInt.put("paymentmode",PaymentHdr.getPAYMENT_MODE());
                	 resultJsonInt.put("paidthrough",PaymentHdr.getPAID_THROUGH());
                	 cur = PaymentHdr.getCURRENCYID();
                	 resultJsonInt.put("convamount",cur+covunitCostValue);
                	 resultJsonInt.put("exchangerate",cuex);
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


	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;
	}

	@Override
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
