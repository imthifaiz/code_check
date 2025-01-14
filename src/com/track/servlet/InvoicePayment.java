package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.BillUtil;
import com.track.db.util.ExceptionUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.ExceptionUtil.Result;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.PlantMstUtil;
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
 * Servlet implementation class InvoicePayment
 */
@WebServlet("/InvoicePayment")
public class InvoicePayment  extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.InvoicePayment_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.InvoicePayment_PRINTPLANTMASTERINFO;
	
/** Invoice Payment Logic -Start**/
	
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String action = StrUtils.fString(req.getParameter("CMD")).trim();
	String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
	String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
	InvPaymentHeader invoicePaymentHeader=new InvPaymentHeader();
	InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
	List<InvPaymentDetail> invoicePaymentDetailList=new ArrayList<>();
	InvPaymentAttachment invoicePaymentAttachment=new InvPaymentAttachment();
	InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
	InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
	DateUtils dateutils = new DateUtils();
    MovHisDAO movHisDao = new MovHisDAO();
	ExceptionUtil exceptionUtil=null;
	if (ServletFileUpload.isMultipartContent(req)) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
        FileItem itemFile = null;
        resp.setContentType("text/plain");
        if(action.equalsIgnoreCase("SAVE"))
        {
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(req);
            Iterator iter = formItems.iterator();            
            List<FileItem> files=new ArrayList<FileItem>();
            invoicePaymentHeader.setPLANT(plant);
            invoicePaymentDetail.setPLANT(plant);
            invoicePaymentAttachment.setPLANT(plant);
            String paidpdcstatus="";
            List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),chequeamount = new ArrayList(),pdcstatus = new ArrayList();
			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0,pdccount = 0;
			
			Hashtable<String,String> recivepdcInfo = null;
			List<Hashtable<String,String>> recivepdcInfoList = null;
            
            Double invoiceamount = Double.parseDouble("0");
            String invoiceStatus="";
            //invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                	
                	if (item.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
						 paidpdcstatus = StrUtils.fString(item.getString()).trim();
					}
                	
                    if (item.getFieldName().equalsIgnoreCase("CUST_CODE")) {
                    	invoicePaymentHeader.setCUSTNO(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("amount"))
                    {
                    	invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                    	
                    }
                    else if(item.getFieldName().equalsIgnoreCase("bankcharges"))
                    {
                    	invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                    }
                   /* else if(item.getFieldName().equalsIgnoreCase("bank_name"))
                    {
                    	invoicePaymentHeader.setBANK_BRANCH(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("checqueNo"))
                    {
                    	invoicePaymentHeader.setCHECQUE_NO(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("checquedate"))
                    {
                    	invoicePaymentHeader.setCHEQUE_DATE(item.getString());
                    }*/
                    else if(item.getFieldName().equalsIgnoreCase("payment_date"))
                    {
                    	invoicePaymentHeader.setRECEIVE_DATE(item.getString());
                    }
                   
                    else if(item.getFieldName().equalsIgnoreCase("payment_mode"))
                    {
                    	invoicePaymentHeader.setRECEIVE_MODE(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("paid_through_account_name"))
                    {
                    	invoicePaymentHeader.setDEPOSIT_TO(item.getString());
                    }
                    
                    else if(item.getFieldName().equalsIgnoreCase("referenceno"))
                    {
                    	invoicePaymentHeader.setREFERENCE(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("notes"))
                    {
                    	invoicePaymentHeader.setNOTE(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("TranId"))
                    {
                    	invoicePaymentHeader.setTRANSACTIONID("");
                    }
                    
                    else if(item.getFieldName().equalsIgnoreCase("uuid"))
                    {
                    	invoicePaymentHeader.setCREDITAPPLYKEY(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("invoiceheaderid"))
                    {
                    	invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                    }
                    else if(item.getFieldName().equalsIgnoreCase("initialAmount"))
                    {
                    	if(!item.getString().isEmpty())
                    		invoiceamount=Double.parseDouble(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("type"))
                    {
                    	invoicePaymentDetail.setTYPE(item.getString());
                    }
                    else if(item.getFieldName().equalsIgnoreCase("dono"))
                    {
                    	invoicePaymentDetail.setDONO(item.getString());
                    }
                    if (item.getFieldName().equalsIgnoreCase("CURRENCYID")) {
                    	invoicePaymentHeader.setCURRENCYID(item.getString());
					}
					if (item.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
						invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
					}
					if (item.getFieldName().equalsIgnoreCase("PROJECTID")) {
						invoicePaymentHeader.setPROJECTID(Integer.valueOf(StrUtils.fString(item.getString()).trim()));
					}
					
                    else if(item.getFieldName().startsWith("invoice_"))
                	{
                    		InvPaymentDetail invoicePaymentDetailWithInv=new InvPaymentDetail();
                        	String fieldName=item.getFieldName();
                        	String[] fieldValues= fieldName.split("\\_");
                        	invoicePaymentDetailWithInv.setPLANT(invoicePaymentDetail.getPLANT());
                        	invoicePaymentDetailWithInv.setINVOICEHDRID(Integer.parseInt(fieldValues[1]));
                        	invoicePaymentDetailWithInv.setDONO(fieldValues[3]);
                        	Double amount1=Double.parseDouble(fieldValues[5]);
                        	Double amount2=Double.parseDouble(item.getString());
                        	invoicePaymentDetailWithInv.setAMOUNT(amount2);
                        	Double balance=(amount1-amount2);
                        	invoicePaymentDetailWithInv.setBALANCE(balance);
                        	invoicePaymentDetailList.add(invoicePaymentDetailWithInv);
                    }
                }
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    itemFile = item;
                    if(!itemFile.getName().isEmpty())
                    	files.add(itemFile);
                
                }
                
                if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("pdcstatus")) {
						pdcstatus.add(pdccount, StrUtils.fString(item.getString()).trim());
						pdccount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("bankname")) {
						bankcode.add(bankcodeCount,StrUtils.fString(item.getString()).trim());
						bankcodeCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("cheque-no")) {
						chequeno.add(chequenoCount, StrUtils.fString(item.getString()).trim());
						chequenoCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("cheque-date")) {
						chedate.add(chedateCount, StrUtils.fString(item.getString()).trim());
						chedateCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("cheque-amount")) {
						chequeamount.add(chequeamountCount, StrUtils.fString(item.getString()).trim());
						chequeamountCount++;
					}
				}
                
            }
					
			  UserTransaction ut = DbBean.getUserTranaction();
			  ut.begin();
			  boolean ispdcenable = false;
			  if(paidpdcstatus.equalsIgnoreCase("1")) {
					for(int i =0 ; i < bankcode.size() ; i++){
						if(pdcstatus.get(i).equals("1")) {
							invoicePaymentHeader.setISPDCPROCESS((short) 1);
							ispdcenable = true;
						}else {
							invoicePaymentHeader.setISPDCPROCESS((short) 0);
							ispdcenable = false;
						}
					}
				
				}
			  exceptionUtil=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
			  
			  if(exceptionUtil.getStatus()==Result.OK)
			  {
				  int receiveHeaderId=(int) exceptionUtil.getResultData();
				  recivepdcInfoList = new ArrayList<Hashtable<String,String>>();
					if(paidpdcstatus.equalsIgnoreCase("1")) {
						for(int i =0 ; i < bankcode.size() ; i++){
							recivepdcInfo = new Hashtable<String, String>();
							recivepdcInfo.put("PLANT", plant);
							recivepdcInfo.put("CUSTNO", invoicePaymentHeader.getCUSTNO());
							recivepdcInfo.put("RECEIVEID", String.valueOf(receiveHeaderId));
							recivepdcInfo.put("RECEIVE_DATE", invoicePaymentHeader.getRECEIVE_DATE());
							recivepdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
							recivepdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
							recivepdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
							recivepdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
							recivepdcInfo.put("ISPDC", (String) pdcstatus.get(i));
							recivepdcInfo.put("CRAT", dateutils.getDateTime());
							recivepdcInfo.put("CRBY", username);
							if(pdcstatus.get(i).equals("1")) {
								recivepdcInfo.put("STATUS", "NOT PROCESSED");
							}else {
								recivepdcInfo.put("STATUS", "NOT APPLICABLE");
							}
							
							recivepdcInfoList.add(recivepdcInfo);
						}
						invpaymentdao.addMultiplePaymentpdc(recivepdcInfoList, plant);
					}
			  }
			  if(exceptionUtil.getStatus()==Result.OK)
			  {
				  int receiveHeaderId=(int) exceptionUtil.getResultData();
				  invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
				  invoicePaymentDetail.setAMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
				  
				  
					
				  if(invoicePaymentDetail.getINVOICEHDRID()!=0)
				  {
					  invoicePaymentDetail.setLNNO(1);
					  invoicePaymentDetail.setTYPE("REGULAR");
					  Double balance=invoiceamount-invoicePaymentDetail.getAMOUNT();
					  invoicePaymentDetail.setBALANCE(balance);
					  if(balance>0)
					  {
						  invoiceStatus="Partially Paid";
					  }
					  else
					  {
						  invoiceStatus="Paid";
					  }
					  
//					  if(invoicePaymentDetail.getAMOUNT() != 0) {
						  invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
						  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
						  invPaymentUtil.updateInvoiceStatus(invoicePaymentDetail, invoiceStatus, plant, username);
//					  }
				  }
				  else
				  {
				  double balnce = 0.0;
				  double addamt =  0.0;
				  for(int j=0;j<invoicePaymentDetailList.size();j++)
				  {
					  InvPaymentDetail invPayDet=invoicePaymentDetailList.get(j);
					  addamt = addamt + invPayDet.getAMOUNT();
				  }
				  balnce = invoicePaymentHeader.getAMOUNTRECEIVED() - addamt;
					  invoicePaymentDetail.setLNNO(0);
					  invoicePaymentDetail.setTYPE("ADVANCE");
					  System.out.println(invoicePaymentDetail.getDONO());
					  if(invoicePaymentDetail.getDONO() == "" || invoicePaymentDetail.getDONO() == null) {
						  invoicePaymentDetail.setDONO("0");
					  }
					  invoicePaymentDetail.setBALANCE(invoicePaymentDetail.getAMOUNT());
					  if(ispdcenable) {
						  invoicePaymentDetail.setBALANCE(0.0);
					  }else {
						  invoicePaymentDetail.setBALANCE(balnce);
					  }
					  invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
					  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
					  
					  if(!invoicePaymentDetailList.isEmpty())
					  {
						  for(int j=0;j<invoicePaymentDetailList.size();j++)
						  {
							  InvPaymentDetail invPayDet=invoicePaymentDetailList.get(j);
							  invPayDet.setRECEIVEHDRID(receiveHeaderId);
							  invPayDet.setLNNO(j+1);
							  invPayDet.setTYPE("REGULAR");
							  Double balance=invPayDet.getBALANCE();
							  if(balance>0)
							  {
								  invoiceStatus="Partially Paid";
							  }
							  else
							  {
								  invoiceStatus="Paid";
							  }
//							  if(invPayDet.getAMOUNT() != 0) {
								  invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
								  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invPayDet,plant, username);
								  invPaymentUtil.updateInvoiceStatus(invPayDet, invoiceStatus, plant, username);
//							  }
						  }
					  }
					  
					  
				  }
				  
			/*  if(!invoicePaymentDetailList.isEmpty())
			  {
				  for(int j=0;j<invoicePaymentDetailList.size();j++)
				  {
					  InvPaymentDetail invPayDet=invoicePaymentDetailList.get(j);
					  invPayDet.setRECEIVEHDRID(receiveHeaderId);
					  invPayDet.setLNNO(j+1);
					  invPayDet.setTYPE("REGULAR");
					  Double balance=invPayDet.getBALANCE();
					  if(balance>0)
					  {
						  invoiceStatus="Partially Paid";
					  }
					  else
					  {
						  invoiceStatus="Paid";
					  }
					  if(invPayDet.getAMOUNT() != 0) {
						  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invPayDet,plant, username);
						  invPaymentUtil.updateInvoiceStatus(invPayDet, invoiceStatus, plant, username);
					  }
				  }
			  }*/
			  /*else
			  {
				  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);
				  if(invoicePaymentDetail.getINVOICEHDRID()>0)
				  {
					  invPaymentUtil.updateInvoiceStatus(invoicePaymentDetail, invoiceStatus, plant, username);
				  }
				  
			  }*/
			   
				  if(exceptionUtil.getStatus()==Result.OK) { 
					  if(!files.isEmpty())
					  {
						  String fileLocation = "C:/ATTACHMENTS/Payment/Invoice" + "/"+ receiveHeaderId;
						  FileHandling fileHandling=new FileHandling(); 
						  fileHandling.fileUpload(files,fileLocation); 
						  for(FileItem IFile:files) {
							  invoicePaymentAttachment.setRECEIVEHDRID(receiveHeaderId); 
							  String fileName =StrUtils.fString(IFile.getName()); 
							  fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
							  invoicePaymentAttachment.setFileName(IFile.getName());
							  invoicePaymentAttachment.setFilePath(fileLocation);
							  invoicePaymentAttachment.setFileType(IFile.getContentType());
							  invoicePaymentAttachment.setFileSize((int) IFile.getSize());
							  exceptionUtil=invPaymentUtil.addInvoicePaymentAttachment(invoicePaymentAttachment, plant, username);
					  
						  } 
					  }
					  
				  
				  String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
					//Journal Entry
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(invoicePaymentHeader.getRECEIVE_DATE());
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("SALESPAYMENT");
					journalHead.setTRANSACTION_ID(String.valueOf(receiveHeaderId));
					journalHead.setSUB_TOTAL(invoicePaymentHeader.getAMOUNTRECEIVED());
					journalHead.setTOTAL_AMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					List<JournalDetail> journalDetails=new ArrayList<>();
					CoaDAO coaDAO=new CoaDAO();
					CustMstDAO cusDAO=new CustMstDAO();
					if(paidpdcstatus.equalsIgnoreCase("1")) {
						for(int i =0 ; i < bankcode.size() ; i++){
							if(pdcstatus.get(i).equals("1")) {
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, "PDC received");
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME("PDC received");
								journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
								journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
								journalDetails.add(journalDetail);
							}
							else
							{
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
								journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
								journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
								journalDetails.add(journalDetail);
							}
						}
					}
					else
					{
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
						journalDetail.setDEBITS(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalDetails.add(journalDetail);
					}
					
							/*
							 * JournalDetail journalDetail=new JournalDetail();
							 * journalDetail.setPLANT(plant); JSONObject coaJson=coaDAO.getCOAByName(plant,
							 * invoicePaymentHeader.getDEPOSIT_TO());
							 * System.out.println("Json"+coaJson.toString());
							 * journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							 * journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
							 * journalDetail.setDEBITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							 * journalDetails.add(journalDetail);
							 */
						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, invoicePaymentHeader.getCUSTNO());
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject vendorJson=cusDAO.getCustomerName(plant, invoicePaymentHeader.getCUSTNO());
							coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO")+"-"+vendorJson.getString("CNAME"));
						}
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							
						}
						else
						{
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						if(coaJson1.getString("account_name")!=null) {
							journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
						}else {
							journalDetail_1.setACCOUNT_NAME(invoicePaymentHeader.getCUSTNO());
						}
						if(invoicePaymentHeader.getBANK_CHARGE()>0) {
							journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED()+invoicePaymentHeader.getBANK_CHARGE());
						}else {
							journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
						}
						//journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalDetails.add(journalDetail_1);
						}
						if(invoicePaymentHeader.getBANK_CHARGE()>0)
						{
							Double bankChargeFrom=invoicePaymentHeader.getBANK_CHARGE();
							if(bankChargeFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
								if(coaJson2.isEmpty() || coaJson2.isNullObject())
								{
									
								}
								else
								{
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("Bank charges");
								journalDetail_2.setDEBITS(bankChargeFrom);
								journalDetails.add(journalDetail_2);
								}
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
				  
				  if(exceptionUtil.getStatus()==Result.OK) {
						
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT_RECEIVED);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));														
						//htMovHis.put(IDBConstants.ITEM, invoicePaymentHeader.getRECEIVE_MODE());						
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(receiveHeaderId));
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",invoicePaymentHeader.getAMOUNTRECEIVED());
						boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						
						
						if(paidpdcstatus.equalsIgnoreCase("1")) {
							for(int i =0 ; i < bankcode.size() ; i++){
								
								Hashtable htMovHis1 = new Hashtable();
								htMovHis1.clear();
								htMovHis1.put(IDBConstants.PLANT, plant);					
								htMovHis1.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS_RECEIVED);
								htMovHis1.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));																			
								htMovHis1.put("RECID", "");
								htMovHis1.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(receiveHeaderId));
								htMovHis1.put(IDBConstants.CREATED_BY, username);		
								htMovHis1.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
								if(pdcstatus.get(i).equals("1")) {
									htMovHis1.put("REMARKS",	"PDC-"+invoicePaymentHeader.getCUSTNO()+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+chequeamount.get(i));
								}else {
									htMovHis1.put("REMARKS", invoicePaymentHeader.getCUSTNO()+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+chequeamount.get(i));
								}
								
								isAdded = movHisDao.insertIntoMovHis(htMovHis1); // Insert MOVHIS
							}
						}
					}
				  
				  if(exceptionUtil.getStatus()==Result.OK) { 
					  DbBean.CommitTran(ut);
					  resp.getWriter().write("Payment Received Successfully");
					  }
			  }
			  if(exceptionUtil.getStatus()==Result.ERROR) { 
				  DbBean.RollbackTran(ut);
				  resp.getWriter().write(exceptionUtil.getClassName()+"::"+exceptionUtil.getErrorMessage()); 
				  }
					 
			
	}
        catch (Exception e) {
        	e.printStackTrace();
        }
      }
        else if(action.equalsIgnoreCase("EDIT"))
        {
        	System.out.println("Edit mode Active----------");
        	try {
                // parses the request's content to extract file data
                List formItems = upload.parseRequest(req);
                Iterator iter = formItems.iterator();
                List<FileItem> files=new ArrayList<FileItem>();
                List<InvPaymentDetail> invoicePaymentDetailsOld=new ArrayList<InvPaymentDetail>();
                List<InvPaymentDetail> invoicePaymentDetailsNew=new ArrayList<InvPaymentDetail>();
                List<InvPaymentAttachment> InvPaymentAttachmentsOld=new ArrayList<InvPaymentAttachment>();
                List<InvPaymentAttachment> InvPaymentAttachmentsNew=new ArrayList<InvPaymentAttachment>();
                invoicePaymentHeader.setPLANT(plant);
                invoicePaymentDetail.setPLANT(plant);
                invoicePaymentAttachment.setPLANT(plant);
                InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
                //invoicePaymentHeader.setTRANSACTIONID("1234");
                //invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
                String paidpdcstatus="";
                List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),chequeamount = new ArrayList(),
    					pdcstatus = new ArrayList(),chequestatus = new ArrayList(),pdcid = new ArrayList();

    			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0,pdccount = 0,
    					checkstcount = 0,pdcidcount = 0;
    			
    			Hashtable<String,String> recivepdcInfo = null;
    			List<Hashtable<String,String>> recivepdcInfoList = null;
                
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                    	
                    	if (item.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
							 paidpdcstatus = StrUtils.fString(item.getString()).trim();
						}
                    	
                        if (item.getFieldName().equalsIgnoreCase("CUST_CODE")) {
                        	invoicePaymentHeader.setCUSTNO(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("amount"))
                        {
                        	invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else if(item.getFieldName().equalsIgnoreCase("bankcharges"))
                        {
                        	invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else if(item.getFieldName().equalsIgnoreCase("bank_name"))
                        {
                        	invoicePaymentHeader.setBANK_BRANCH(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("checqueNo"))
                        {
                        	invoicePaymentHeader.setCHECQUE_NO(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("checquedate"))
                        {
                        	invoicePaymentHeader.setCHEQUE_DATE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("payment_date"))
                        {
                        	invoicePaymentHeader.setRECEIVE_DATE(item.getString());
                        }
                        
                        else if (item.getFieldName().equalsIgnoreCase("PROJECTID")) {
    						invoicePaymentHeader.setPROJECTID(Integer.valueOf(StrUtils.fString(item.getString()).trim()));
    					}
                       
                        else if(item.getFieldName().equalsIgnoreCase("payment_mode"))
                        {
                        	invoicePaymentHeader.setRECEIVE_MODE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("paid_through_account_name"))
                        {
                        	invoicePaymentHeader.setDEPOSIT_TO(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("referenceno"))
                        {
                        	invoicePaymentHeader.setREFERENCE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("notes"))
                        {
                        	invoicePaymentHeader.setNOTE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("headerid"))
                        {
                        	invoicePaymentHeader.setID(Integer.parseInt(item.getString()));
                        	
                        	invoicePaymentDetailsOld=invoicePaymentDAO.getInvoicePaymentDetails(Integer.parseInt(item.getString()), plant, username);
                        	
                        }
                        else if(item.getFieldName().equalsIgnoreCase("amountufp"))
                        {
                        	invoicePaymentHeader.setAMOUNTUFP(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else
                        {
                        	
                        	for(InvPaymentDetail invPayDet:invoicePaymentDetailsOld) {
                        		String paymentid=String.valueOf(invPayDet.getID());
                        		if(item.getFieldName().equalsIgnoreCase(paymentid))
                        		{
                        			invPayDet.setAMOUNT(Double.parseDouble(item.getString()));
                        			invoicePaymentDetailsNew.add(invPayDet);
                        		}
                        	}
                        }
                        
                    }
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        itemFile = item;
                        if(!itemFile.getName().isEmpty())
                        	files.add(itemFile);
                    
                    }
                    
                    if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("chequestatus")) {
							chequestatus.add(checkstcount, StrUtils.fString(item.getString()).trim());
							checkstcount++;
						}
					}
					
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("pdcid")) {
							pdcid.add(pdcidcount, StrUtils.fString(item.getString()).trim());
							pdcidcount++;
						}
					}
					
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("pdcstatus")) {
							pdcstatus.add(pdccount, StrUtils.fString(item.getString()).trim());
							pdccount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("bankname")) {
							bankcode.add(bankcodeCount,StrUtils.fString(item.getString()).trim());
							bankcodeCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-no")) {
							chequeno.add(chequenoCount, StrUtils.fString(item.getString()).trim());
							chequenoCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-date")) {
							chedate.add(chedateCount, StrUtils.fString(item.getString()).trim());
							chedateCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-amount")) {
							chequeamount.add(chequeamountCount, StrUtils.fString(item.getString()).trim());
							chequeamountCount++;
						}
					}
                }
    			UserTransaction ut = DbBean.getUserTranaction();				
    			ut.begin();
    			boolean ispdcenable = false;
  			  	if(paidpdcstatus.equalsIgnoreCase("1")) {
  					for(int i =0 ; i < bankcode.size() ; i++){
  						if(pdcstatus.get(i).equals("1")) {
  							invoicePaymentHeader.setISPDCPROCESS((short) 1);
  							ispdcenable = true;
  						}else {
  							invoicePaymentHeader.setISPDCPROCESS((short) 0);
  							ispdcenable = false;
  						}
  					}
  				
  				}
    			
    			exceptionUtil=invPaymentUtil.updateInvoicePaymentHdr(invoicePaymentHeader, plant,username);
    			boolean recpaystatus = true;
    			for(InvPaymentDetail invPayDet:invoicePaymentDetailsOld) {
            		String lnno=String.valueOf(invPayDet.getLNNO());
            		if(lnno.equalsIgnoreCase("0"))
            		{
            			recpaystatus = false;
            			double detbal = invPayDet.getBALANCE() + (invoicePaymentHeader.getAMOUNTRECEIVED() - invPayDet.getAMOUNT());
            			invPayDet.setAMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
            			if(ispdcenable) {
            				invPayDet.setBALANCE(0.0);
            			}else {
            				invPayDet.setBALANCE(detbal);
            			}
            			
            			invoicePaymentDetailsNew.add(invPayDet);
            		}
            	}
    			
    			if(recpaystatus) {
    				for(InvPaymentDetail invPayDet:invoicePaymentDetailsOld) {
    					invPayDet.setAMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
            			invoicePaymentDetailsNew.add(invPayDet);
                	}
    			}
    			
    			if(exceptionUtil.getStatus()==Result.OK)
    			{
    				recivepdcInfoList = new ArrayList<Hashtable<String,String>>();
					if(paidpdcstatus.equalsIgnoreCase("1")) {
						invpaymentdao.deletepdcbypayid(plant, String.valueOf(invoicePaymentHeader.getID()));
						for(int i =0 ; i < bankcode.size() ; i++){
							String pdcidcheck = (String) pdcid.get(i);
							String chequecheck = (String) chequestatus.get(i);
							if(!chequecheck.equalsIgnoreCase("PROCESSED")) {
								recivepdcInfo = new Hashtable<String, String>();
								recivepdcInfo.put("PLANT", plant);
								recivepdcInfo.put("CUSTNO", invoicePaymentHeader.getCUSTNO());
								recivepdcInfo.put("RECEIVEID", String.valueOf(invoicePaymentHeader.getID()));
								recivepdcInfo.put("RECEIVE_DATE", invoicePaymentHeader.getRECEIVE_DATE());
								recivepdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
								recivepdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
								recivepdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
								recivepdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
								recivepdcInfo.put("ISPDC", (String) pdcstatus.get(i));
								recivepdcInfo.put("CRAT", dateutils.getDateTime());
								recivepdcInfo.put("CRBY", username);
								if(pdcstatus.get(i).equals("1")) {
									recivepdcInfo.put("STATUS", "NOT PROCESSED");
								}else {
									recivepdcInfo.put("STATUS", "NOT APPLICABLE");
								}
								
								recivepdcInfoList.add(recivepdcInfo);
								
							}
						}
						invpaymentdao.addMultiplePaymentpdc(recivepdcInfoList, plant);
					}else {
						invpaymentdao.deletepdcbypayid(plant, String.valueOf(invoicePaymentHeader.getID()));
					}
    			}
    			
    			if(exceptionUtil.getStatus()==Result.OK)
    			{
    				for(InvPaymentDetail invPayDet:invoicePaymentDetailsNew) {
    					exceptionUtil=invPaymentUtil.updateInvoicePaymentDet(invPayDet, plant, username);
    				}
    				if(exceptionUtil.getStatus()==Result.OK)
    				{
	    				if(!files.isEmpty())
	   					  {
							String fileLocation = "C:/ATTACHMENTS/Payment/Invoice" + "/"+ invoicePaymentHeader.getID();
							FileHandling fileHandling=new FileHandling();
							//fileHandling.fileDelete(fileLocation);
							fileHandling.fileUpload(files, fileLocation); 
							//invPaymentUtil.deleteInvoicePaymentAttachmentById(invoicePaymentHeader.getID(), plant, username);
							  for(FileItem IFile:files) {
								  invoicePaymentAttachment.setRECEIVEHDRID(invoicePaymentHeader.getID());
								  String fileName = StrUtils.fString(IFile.getName()); fileName =
								  fileName.substring(fileName.lastIndexOf("\\") + 1);
								  invoicePaymentAttachment.setFileName(IFile.getName());
								  invoicePaymentAttachment.setFilePath(fileLocation);
								  invoicePaymentAttachment.setFileType(IFile.getContentType());
								  invoicePaymentAttachment.setFileSize((int) IFile.getSize());
								  exceptionUtil=invPaymentUtil.addInvoicePaymentAttachment(invoicePaymentAttachment, plant, username);
							  }
	   					  } 
	    				
	    				String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(invoicePaymentHeader.getRECEIVE_DATE());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("SALESPAYMENT");
						journalHead.setTRANSACTION_ID(String.valueOf(invoicePaymentHeader.getID()));
						journalHead.setSUB_TOTAL(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalHead.setTOTAL_AMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						CustMstDAO cusDAO=new CustMstDAO();
						if(paidpdcstatus.equalsIgnoreCase("1")) {
							for(int i =0 ; i < bankcode.size() ; i++){
								if(pdcstatus.get(i).equals("1")) {
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson=coaDAO.getCOAByName(plant, "PDC received");
									System.out.println("Json"+coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME("PDC received");
									journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									journalDetails.add(journalDetail);
								}
								else
								{
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
									System.out.println("Json"+coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
									journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									journalDetails.add(journalDetail);
								}
							}
						}
						else
						{
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
							journalDetail.setDEBITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							journalDetails.add(journalDetail);
						}
							 
							
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, invoicePaymentHeader.getCUSTNO());
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=cusDAO.getCustomerName(plant, invoicePaymentHeader.getCUSTNO());
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("CUSTNO")+"-"+vendorJson.getString("CNAME"));
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							if(coaJson1.getString("account_name")!=null) {
								journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
							}else {
								journalDetail_1.setACCOUNT_NAME(invoicePaymentHeader.getCUSTNO());
							}
							if(invoicePaymentHeader.getBANK_CHARGE()>0) {
								journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED()+invoicePaymentHeader.getBANK_CHARGE());
							}else {
								journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							}
							//journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							journalDetails.add(journalDetail_1);
							}
							if(invoicePaymentHeader.getBANK_CHARGE()>0)
							{
								Double bankChargeFrom=invoicePaymentHeader.getBANK_CHARGE();
								if(bankChargeFrom>0)
								{
									JournalDetail journalDetail_2=new JournalDetail();
									journalDetail_2.setPLANT(plant);
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
									if(coaJson2.isEmpty() || coaJson2.isNullObject())
									{
										
									}
									else
									{
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("Bank charges");
									journalDetail_2.setDEBITS(bankChargeFrom);
									journalDetails.add(journalDetail_2);
									}
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
	    				
	    				
	    				
	    				if(exceptionUtil.getStatus()==Result.OK) {
							
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_PAYMENT_RECEIVED);
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));														
							//htMovHis.put(IDBConstants.ITEM, invoicePaymentHeader.getRECEIVE_MODE());						
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoicePaymentHeader.getID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",invoicePaymentHeader.getAMOUNTRECEIVED());
							boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							
						}
	    				
						  if(exceptionUtil.getStatus()==Result.OK) {
							  DbBean.CommitTran(ut);
							  resp.getWriter().write("Payment Received Successfully"); 
						  }
							 
    					
    					
    				}	
    			}
    			if(exceptionUtil.getStatus()==Result.ERROR)
    			{
    				DbBean.RollbackTran(ut);
    				resp.getWriter().write(exceptionUtil.getClassName()+"::"+exceptionUtil.getErrorMessage());
    			}
    			
    	}
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        else  if(action.equalsIgnoreCase("V_SAVE")) {

            try {
                // parses the request's content to extract file data
                List formItems = upload.parseRequest(req);
                Iterator iter = formItems.iterator();            
                List<FileItem> files=new ArrayList<FileItem>();
                invoicePaymentHeader.setPLANT(plant);
                invoicePaymentDetail.setPLANT(plant);
                invoicePaymentAttachment.setPLANT(plant);
                Double invoiceamount = Double.parseDouble("0");
                String invoiceStatus="";
    			String paidpdcstatus="";
                List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),chequeamount = new ArrayList(),pdcstatus = new ArrayList();
    			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0,pdccount = 0;
    			
    			Hashtable<String,String> recivepdcInfo = null;
    			List<Hashtable<String,String>> recivepdcInfoList = null;
                
                //invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
                invoicePaymentHeader.setCUSTNO("N/A");
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                    	
                    	if (item.getFieldName().equalsIgnoreCase("v_paidpdcstatus")) {
                    		paidpdcstatus = StrUtils.fString(item.getString()).trim();
                    	}
                    	
                        if (item.getFieldName().equalsIgnoreCase("account_name")) {
                        	invoicePaymentHeader.setACCOUNT_NAME(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_amount"))
                        {
                        	invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        	
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_bankcharges"))
                        {
                        	invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                       /* else if(item.getFieldName().equalsIgnoreCase("v_bank_name"))
                        {
                        	invoicePaymentHeader.setBANK_BRANCH(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_checqueNo"))
                        {
                        	invoicePaymentHeader.setCHECQUE_NO(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_checquedate"))
                        {
                        	invoicePaymentHeader.setCHEQUE_DATE(item.getString());
                        }*/
                        else if(item.getFieldName().equalsIgnoreCase("v_payment_date"))
                        {
                        	invoicePaymentHeader.setRECEIVE_DATE(item.getString());
                        }
                       
                        else if(item.getFieldName().equalsIgnoreCase("v_payment_mode"))
                        {
                        	invoicePaymentHeader.setRECEIVE_MODE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_paid_through_account_name"))
                        {
                        	invoicePaymentHeader.setDEPOSIT_TO(item.getString());
                        }
                        
                        else if(item.getFieldName().equalsIgnoreCase("v_referenceno"))
                        {
                        	invoicePaymentHeader.setREFERENCE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_notes"))
                        {
                        	invoicePaymentHeader.setNOTE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_TranId"))
                        {
                        	invoicePaymentHeader.setTRANSACTIONID("");
                        }
                        
                        else if(item.getFieldName().equalsIgnoreCase("v_uuid"))
                        {
                        	invoicePaymentHeader.setCREDITAPPLYKEY(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_invoiceheaderid"))
                        {
                        	invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_initialAmount"))
                        {
                        	if(!item.getString().isEmpty())
                        		invoiceamount=Double.parseDouble(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_type"))
                        {
                        	invoicePaymentDetail.setTYPE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("v_dono"))
                        {
                        	invoicePaymentDetail.setDONO(item.getString());
                        }
                        if (item.getFieldName().equalsIgnoreCase("v_CURRENCY")) {
                        	invoicePaymentHeader.setCURRENCYID(item.getString());
    					}
    					if (item.getFieldName().equalsIgnoreCase("v_CURRENCYUSEQT")) {
    						invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
    					}
    					
    					if (item.getFieldName().equalsIgnoreCase("v_PROJECTID")) {
    						invoicePaymentHeader.setPROJECTID(Integer.valueOf(StrUtils.fString(item.getString()).trim()));
    					}
                        
                    }
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        itemFile = item;
                        if(!itemFile.getName().isEmpty())
                        	files.add(itemFile);
                    
                    }
                    
                    if (item.isFormField()) {
    					if (item.getFieldName().equalsIgnoreCase("v_pdcstatus")) {
    						pdcstatus.add(pdccount, StrUtils.fString(item.getString()).trim());
    						pdccount++;
    					}
    				}
    				if (item.isFormField()) {
    					if (item.getFieldName().equalsIgnoreCase("v_bankname")) {
    						bankcode.add(bankcodeCount,StrUtils.fString(item.getString()).trim());
    						bankcodeCount++;
    					}
    				}
    				if (item.isFormField()) {
    					if (item.getFieldName().equalsIgnoreCase("v_cheque-no")) {
    						chequeno.add(chequenoCount, StrUtils.fString(item.getString()).trim());
    						chequenoCount++;
    					}
    				}
    				if (item.isFormField()) {
    					if (item.getFieldName().equalsIgnoreCase("v_cheque-date")) {
    						chedate.add(chedateCount, StrUtils.fString(item.getString()).trim());
    						chedateCount++;
    					}
    				}
    				if (item.isFormField()) {
    					if (item.getFieldName().equalsIgnoreCase("v_cheque-amount")) {
    						chequeamount.add(chequeamountCount, StrUtils.fString(item.getString()).trim());
    						chequeamountCount++;
    					}
    				}
                }
    					
    			  UserTransaction ut = DbBean.getUserTranaction();
    			  ut.begin();
    			  if(paidpdcstatus.equalsIgnoreCase("1")) {
    					for(int i =0 ; i < bankcode.size() ; i++){
    						if(pdcstatus.get(i).equals("1")) {
    							invoicePaymentHeader.setISPDCPROCESS((short) 1);
    						}else {
    							invoicePaymentHeader.setISPDCPROCESS((short) 0);
    						}
    					}
    				
    				}
    			  exceptionUtil=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
    			  
    			  if(exceptionUtil.getStatus()==Result.OK)
    			  {
    				  int receiveHeaderId=(int) exceptionUtil.getResultData();
    				  recivepdcInfoList = new ArrayList<Hashtable<String,String>>();
    					if(paidpdcstatus.equalsIgnoreCase("1")) {
    						for(int i =0 ; i < bankcode.size() ; i++){
    							recivepdcInfo = new Hashtable<String, String>();
    							recivepdcInfo.put("PLANT", plant);
    							recivepdcInfo.put("CUSTNO", invoicePaymentHeader.getCUSTNO());
    							recivepdcInfo.put("ACCOUNT_NAME", invoicePaymentHeader.getACCOUNT_NAME());
    							recivepdcInfo.put("RECEIVEID", String.valueOf(receiveHeaderId));
    							recivepdcInfo.put("RECEIVE_DATE", invoicePaymentHeader.getRECEIVE_DATE());
    							recivepdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
    							recivepdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
    							recivepdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
    							recivepdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
    							recivepdcInfo.put("ISPDC", (String) pdcstatus.get(i));
    							recivepdcInfo.put("CRAT", dateutils.getDateTime());
    							recivepdcInfo.put("CRBY", username);
    							if(pdcstatus.get(i).equals("1")) {
    								recivepdcInfo.put("STATUS", "NOT PROCESSED");
    							}else {
    								recivepdcInfo.put("STATUS", "NOT APPLICABLE");
    							}
    							
    							recivepdcInfoList.add(recivepdcInfo);
    						}
    						invpaymentdao.addMultiplePaymentpdc(recivepdcInfoList, plant);
    					}
    			  }
    			  
    			  
    			  if(exceptionUtil.getStatus()==Result.OK)
    			  {
    				  int receiveHeaderId=(int) exceptionUtil.getResultData();
    				  invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
    				  invoicePaymentDetail.setAMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
    				  invoicePaymentDetail.setLNNO(0);
    				  invoicePaymentDetail.setTYPE("VOUCHER");
    				  invoicePaymentDetail.setBALANCE(invoicePaymentDetail.getAMOUNT());
    				  invoicePaymentDetail.setDONO("0");
    				  invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
    				  exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail,plant, username);

    				 
    				  
    		
    				  if(exceptionUtil.getStatus()==Result.OK) { 
    					  if(!files.isEmpty())
    					  {
    						  String fileLocation = "C:/ATTACHMENTS/Payment/Invoice" + "/"+ receiveHeaderId;
    						  FileHandling fileHandling=new FileHandling(); 
    						  fileHandling.fileUpload(files,fileLocation); 
    						  for(FileItem IFile:files) {
    							  invoicePaymentAttachment.setRECEIVEHDRID(receiveHeaderId); 
    							  String fileName =StrUtils.fString(IFile.getName()); 
    							  fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
    							  invoicePaymentAttachment.setFileName(IFile.getName());
    							  invoicePaymentAttachment.setFilePath(fileLocation);
    							  invoicePaymentAttachment.setFileType(IFile.getContentType());
    							  invoicePaymentAttachment.setFileSize((int) IFile.getSize());
    							  exceptionUtil=invPaymentUtil.addInvoicePaymentAttachment(invoicePaymentAttachment, plant, username);
    					  
    						  } 
    					  }
    					  
    					  String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
    						//Journal Entry
    						JournalHeader journalHead=new JournalHeader();
    						journalHead.setPLANT(plant);
    						journalHead.setJOURNAL_DATE(invoicePaymentHeader.getRECEIVE_DATE());
    						journalHead.setJOURNAL_STATUS("PUBLISHED");
    						journalHead.setJOURNAL_TYPE("Cash");
    						journalHead.setCURRENCYID(curency);
    						journalHead.setTRANSACTION_TYPE("SALESPAYMENT");
    						journalHead.setTRANSACTION_ID(String.valueOf(receiveHeaderId));
    						journalHead.setSUB_TOTAL(invoicePaymentHeader.getAMOUNTRECEIVED());
    						journalHead.setTOTAL_AMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
    						journalHead.setCRAT(dateutils.getDateTime());
    						journalHead.setCRBY(username);
    						
    						List<JournalDetail> journalDetails=new ArrayList<>();
    						CoaDAO coaDAO=new CoaDAO();
    						
    						if(paidpdcstatus.equalsIgnoreCase("1")) {
    							for(int i =0 ; i < bankcode.size() ; i++){
    								if(pdcstatus.get(i).equals("1")) {
    									JournalDetail journalDetail=new JournalDetail();
    									journalDetail.setPLANT(plant);
    									JSONObject coaJson=coaDAO.getCOAByName(plant, "PDC received");
    									System.out.println("Json"+coaJson.toString());
    									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
    									journalDetail.setACCOUNT_NAME("PDC received");
    									journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
    									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
    									journalDetails.add(journalDetail);
    								}
    								else
    								{
    									JournalDetail journalDetail=new JournalDetail();
    									journalDetail.setPLANT(plant);
    									JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
    									System.out.println("Json"+coaJson.toString());
    									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
    									journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
										journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
    									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
    									journalDetails.add(journalDetail);
    								}
    							}
    						}
    						else
    						{
    							JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
								journalDetail.setDEBITS(invoicePaymentHeader.getAMOUNTRECEIVED());
								journalDetails.add(journalDetail);
    						}
    							
    							JournalDetail journalDetail_1=new JournalDetail();
    							journalDetail_1.setPLANT(plant);
    							JSONObject coaJson1=coaDAO.getCOAByName(plant, invoicePaymentHeader.getACCOUNT_NAME());
    							if(coaJson1.isEmpty() || coaJson1.isNullObject())
    							{
    								
    							}
    							else
    							{
    							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
    							journalDetail_1.setACCOUNT_NAME(invoicePaymentHeader.getACCOUNT_NAME());
    							if(invoicePaymentHeader.getBANK_CHARGE()>0) {
    							journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED()+invoicePaymentHeader.getBANK_CHARGE());
    							}else {
    							 journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
    							}
    							journalDetails.add(journalDetail_1);
    							}
    							if(invoicePaymentHeader.getBANK_CHARGE()>0)
    							{
    								Double bankChargeFrom=invoicePaymentHeader.getBANK_CHARGE();
    								if(bankChargeFrom>0)
    								{
    									JournalDetail journalDetail_2=new JournalDetail();
    									journalDetail_2.setPLANT(plant);
    									JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
    									if(coaJson2.isEmpty() || coaJson2.isNullObject())
    									{
    										
    									}
    									else
    									{
    									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
    									journalDetail_2.setACCOUNT_NAME("Bank charges");
    									journalDetail_2.setDEBITS(bankChargeFrom);
    									journalDetails.add(journalDetail_2);
    									}
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
    				  
    				  if(exceptionUtil.getStatus()==Result.OK) {
    						
    						Hashtable htMovHis = new Hashtable();
    						htMovHis.clear();
    						htMovHis.put(IDBConstants.PLANT, plant);					
    						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT_RECEIVED);
    						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));														
    						//htMovHis.put(IDBConstants.ITEM, invoicePaymentHeader.getRECEIVE_MODE());						
    						htMovHis.put("RECID", "");
    						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(receiveHeaderId));
    						htMovHis.put(IDBConstants.CREATED_BY, username);		
    						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
    						htMovHis.put("REMARKS",invoicePaymentHeader.getAMOUNTRECEIVED());
    						boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
    						
    						if(paidpdcstatus.equalsIgnoreCase("1")) {
    							for(int i =0 ; i < bankcode.size() ; i++){
    								
    								Hashtable htMovHis1 = new Hashtable();
    								htMovHis1.clear();
    								htMovHis1.put(IDBConstants.PLANT, plant);					
    								htMovHis1.put("DIRTYPE", TransactionConstants.CHEQUE_DETAILS_RECEIVED);
    								htMovHis1.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));																			
    								htMovHis1.put("RECID", "");
    								htMovHis1.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(receiveHeaderId));
    								htMovHis1.put(IDBConstants.CREATED_BY, username);		
    								htMovHis1.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
    								if(pdcstatus.get(i).equals("1")) {
    									htMovHis1.put("REMARKS",	"PDC-"+invoicePaymentHeader.getCUSTNO()+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+chequeamount.get(i));
    								}else {
    									htMovHis1.put("REMARKS", invoicePaymentHeader.getCUSTNO()+","+bankcode.get(i)+","+chequeno.get(i)+","+chedate.get(i)+","+chequeamount.get(i));
    								}
    								
    								isAdded = movHisDao.insertIntoMovHis(htMovHis1); // Insert MOVHIS
    							}
    						}
    						
    					}
    				  
    				  if(exceptionUtil.getStatus()==Result.OK) { 
    					  DbBean.CommitTran(ut);
    					  resp.getWriter().write("Payment Received Successfully");
    					  }
    			  }
    			  if(exceptionUtil.getStatus()==Result.ERROR) { 
    				  DbBean.RollbackTran(ut);
    				  resp.getWriter().write(exceptionUtil.getClassName()+"::"+exceptionUtil.getErrorMessage()); 
    				  }
    					 
    			
    	}
            catch (Exception e) {
            	e.printStackTrace();
            }
          
        }
        
        else if(action.equalsIgnoreCase("V_EDIT"))
        {
        	try {
                // parses the request's content to extract file data
                List formItems = upload.parseRequest(req);
                Iterator iter = formItems.iterator();
                List<FileItem> files=new ArrayList<FileItem>();
                List<InvPaymentDetail> invoicePaymentDetailsOld=new ArrayList<InvPaymentDetail>();
                List<InvPaymentDetail> invoicePaymentDetailsNew=new ArrayList<InvPaymentDetail>();
                List<InvPaymentAttachment> InvPaymentAttachmentsOld=new ArrayList<InvPaymentAttachment>();
                List<InvPaymentAttachment> InvPaymentAttachmentsNew=new ArrayList<InvPaymentAttachment>();
                invoicePaymentHeader.setPLANT(plant);
                invoicePaymentDetail.setPLANT(plant);
                invoicePaymentAttachment.setPLANT(plant);
                InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
                //invoicePaymentHeader.setTRANSACTIONID("1234");
                //invoicePaymentHeader.setDEPOSIT_TO("Receivable Account");
                String paidpdcstatus="";
                List bankcode = new ArrayList(), chequeno = new ArrayList(), chedate = new ArrayList(),chequeamount = new ArrayList(),
    					pdcstatus = new ArrayList(),chequestatus = new ArrayList(),pdcid = new ArrayList();

    			int bankcodeCount = 0, chequenoCount = 0, chedateCount = 0, chequeamountCount = 0,pdccount = 0,
    					checkstcount = 0,pdcidcount = 0;
    			
    			Hashtable<String,String> recivepdcInfo = null;
    			List<Hashtable<String,String>> recivepdcInfoList = null;
                invoicePaymentHeader.setCUSTNO("N/A");
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                    	if (item.getFieldName().equalsIgnoreCase("paidpdcstatus")) {
							 paidpdcstatus = StrUtils.fString(item.getString()).trim();
						}
                    	
                        if (item.getFieldName().equalsIgnoreCase("account_name")) {
                        	invoicePaymentHeader.setACCOUNT_NAME(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("amount"))
                        {
                        	invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else if(item.getFieldName().equalsIgnoreCase("bankcharges"))
                        {
                        	invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble(item.getString() == null || item.getString().equals("") ? "0":item.getString()));
                        }
                        else if(item.getFieldName().equalsIgnoreCase("bank_name"))
                        {
                        	invoicePaymentHeader.setBANK_BRANCH(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("checqueNo"))
                        {
                        	invoicePaymentHeader.setCHECQUE_NO(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("checquedate"))
                        {
                        	invoicePaymentHeader.setCHEQUE_DATE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("payment_date"))
                        {
                        	invoicePaymentHeader.setRECEIVE_DATE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("paid_through_account_name"))
                        {
                        	invoicePaymentHeader.setDEPOSIT_TO(item.getString());
                        }
                        else if (item.getFieldName().equalsIgnoreCase("PROJECTID")) {
    						invoicePaymentHeader.setPROJECTID(Integer.valueOf(StrUtils.fString(item.getString()).trim()));
    					}
                        else if(item.getFieldName().equalsIgnoreCase("payment_mode"))
                        {
                        	invoicePaymentHeader.setRECEIVE_MODE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("referenceno"))
                        {
                        	invoicePaymentHeader.setREFERENCE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("notes"))
                        {
                        	invoicePaymentHeader.setNOTE(item.getString());
                        }
                        else if(item.getFieldName().equalsIgnoreCase("headerid"))
                        {
                        	invoicePaymentHeader.setID(Integer.parseInt(item.getString()));
                        	
                        	invoicePaymentDetailsOld=invoicePaymentDAO.getInvoicePaymentDetails(Integer.parseInt(item.getString()), plant, username);
                        	
                        }
                        else
                        {
                        	for(InvPaymentDetail invPayDet:invoicePaymentDetailsOld) {
                        		String paymentid=String.valueOf(invPayDet.getID());
                        		if(item.getFieldName().equalsIgnoreCase(paymentid))
                        		{
                        			invPayDet.setAMOUNT(Double.parseDouble(item.getString()));
                        			invoicePaymentDetailsNew.add(invPayDet);
                        		}
                        	}
                        }
                        
                    }
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        itemFile = item;
                        if(!itemFile.getName().isEmpty())
                        	files.add(itemFile);
                    
                    }
                    
                    if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("chequestatus")) {
							chequestatus.add(checkstcount, StrUtils.fString(item.getString()).trim());
							checkstcount++;
						}
					}
					
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("pdcid")) {
							pdcid.add(pdcidcount, StrUtils.fString(item.getString()).trim());
							pdcidcount++;
						}
					}
					
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("pdcstatus")) {
							pdcstatus.add(pdccount, StrUtils.fString(item.getString()).trim());
							pdccount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("bankname")) {
							bankcode.add(bankcodeCount,StrUtils.fString(item.getString()).trim());
							bankcodeCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-no")) {
							chequeno.add(chequenoCount, StrUtils.fString(item.getString()).trim());
							chequenoCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-date")) {
							chedate.add(chedateCount, StrUtils.fString(item.getString()).trim());
							chedateCount++;
						}
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("cheque-amount")) {
							chequeamount.add(chequeamountCount, StrUtils.fString(item.getString()).trim());
							chequeamountCount++;
						}
					}
                }
    			UserTransaction ut = DbBean.getUserTranaction();				
    			ut.begin();
    			boolean ispdcenable = false;
  			  	if(paidpdcstatus.equalsIgnoreCase("1")) {
  					for(int i =0 ; i < bankcode.size() ; i++){
  						if(pdcstatus.get(i).equals("1")) {
  							invoicePaymentHeader.setISPDCPROCESS((short) 1);
  							ispdcenable = true;
  						}else {
  							invoicePaymentHeader.setISPDCPROCESS((short) 0);
  							ispdcenable = false;
  						}
  					}
  				
  				}
    			exceptionUtil=invPaymentUtil.updateInvoicePaymentHdr(invoicePaymentHeader, plant,username);
    			
    			
    			for(InvPaymentDetail invPayDet:invoicePaymentDetailsOld) {
            		String lnno=String.valueOf(invPayDet.getLNNO());
            		if(lnno.equalsIgnoreCase("0"))
            		{
            			invPayDet.setAMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
            			invoicePaymentDetailsNew.add(invPayDet);
            		}
            	}
    			
    			if(exceptionUtil.getStatus()==Result.OK)
    			{
    				recivepdcInfoList = new ArrayList<Hashtable<String,String>>();
					if(paidpdcstatus.equalsIgnoreCase("1")) {
						invpaymentdao.deletepdcbypayid(plant, String.valueOf(invoicePaymentHeader.getID()));
						for(int i =0 ; i < bankcode.size() ; i++){
							String pdcidcheck = (String) pdcid.get(i);
							String chequecheck = (String) chequestatus.get(i);
							if(!chequecheck.equalsIgnoreCase("PROCESSED")) {
								recivepdcInfo = new Hashtable<String, String>();
								recivepdcInfo.put("PLANT", plant);
								recivepdcInfo.put("CUSTNO", invoicePaymentHeader.getCUSTNO());
								recivepdcInfo.put("ACCOUNT_NAME", invoicePaymentHeader.getACCOUNT_NAME());
								recivepdcInfo.put("RECEIVEID", String.valueOf(invoicePaymentHeader.getID()));
								recivepdcInfo.put("RECEIVE_DATE", invoicePaymentHeader.getRECEIVE_DATE());
								recivepdcInfo.put("BANK_BRANCH", (String) bankcode.get(i));
								recivepdcInfo.put("CHECQUE_NO", (String) chequeno.get(i));
								recivepdcInfo.put("CHEQUE_DATE", (String) chedate.get(i));
								recivepdcInfo.put("CHEQUE_AMOUNT", (String) chequeamount.get(i));
								recivepdcInfo.put("ISPDC", (String) pdcstatus.get(i));
								recivepdcInfo.put("CRAT", dateutils.getDateTime());
								recivepdcInfo.put("CRBY", username);
								if(pdcstatus.get(i).equals("1")) {
									recivepdcInfo.put("STATUS", "NOT PROCESSED");
								}else {
									recivepdcInfo.put("STATUS", "NOT APPLICABLE");
								}
								
								recivepdcInfoList.add(recivepdcInfo);
								
							}
						}
						invpaymentdao.addMultiplePaymentpdc(recivepdcInfoList, plant);
					}else {
						invpaymentdao.deletepdcbypayid(plant, String.valueOf(invoicePaymentHeader.getID()));
					}
    			}
    			
    			if(exceptionUtil.getStatus()==Result.OK)
    			{
    				for(InvPaymentDetail invPayDet:invoicePaymentDetailsNew) {
    					exceptionUtil=invPaymentUtil.updateInvoicePaymentDet(invPayDet, plant, username);
    				}
    				if(exceptionUtil.getStatus()==Result.OK)
    				{
	    				if(!files.isEmpty())
	   					  {
							String fileLocation = "C:/ATTACHMENTS/Payment/Invoice" + "/"+ invoicePaymentHeader.getID();
							FileHandling fileHandling=new FileHandling();
							//fileHandling.fileDelete(fileLocation);
							fileHandling.fileUpload(files, fileLocation); 
							//invPaymentUtil.deleteInvoicePaymentAttachmentById(invoicePaymentHeader.getID(), plant, username);
							  for(FileItem IFile:files) {
								  invoicePaymentAttachment.setRECEIVEHDRID(invoicePaymentHeader.getID());
								  String fileName = StrUtils.fString(IFile.getName()); fileName =
								  fileName.substring(fileName.lastIndexOf("\\") + 1);
								  invoicePaymentAttachment.setFileName(IFile.getName());
								  invoicePaymentAttachment.setFilePath(fileLocation);
								  invoicePaymentAttachment.setFileType(IFile.getContentType());
								  invoicePaymentAttachment.setFileSize((int) IFile.getSize());
								  exceptionUtil=invPaymentUtil.addInvoicePaymentAttachment(invoicePaymentAttachment, plant, username);
							  }
	   					  } 
	    				
	    				String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(invoicePaymentHeader.getRECEIVE_DATE());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("SALESPAYMENT");
						journalHead.setTRANSACTION_ID(String.valueOf(invoicePaymentHeader.getID()));
						journalHead.setSUB_TOTAL(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalHead.setTOTAL_AMOUNT(invoicePaymentHeader.getAMOUNTRECEIVED());
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						
							
						if(paidpdcstatus.equalsIgnoreCase("1")) {
							for(int i =0 ; i < bankcode.size() ; i++){
								if(pdcstatus.get(i).equals("1")) {
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson=coaDAO.getCOAByName(plant, "PDC received");
									System.out.println("Json"+coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME("PDC received");
									journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									journalDetails.add(journalDetail);
								}
								else
								{
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
									System.out.println("Json"+coaJson.toString());
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
									journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
									journalDetail.setDEBITS(Double.parseDouble((String) chequeamount.get(i)));
									journalDetail.setDESCRIPTION("Bank check no: "+(String) chequeno.get(i));
									journalDetails.add(journalDetail);
								}
							}
						}
						else
						{
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, invoicePaymentHeader.getDEPOSIT_TO());
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(invoicePaymentHeader.getDEPOSIT_TO());
							journalDetail.setDEBITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							journalDetails.add(journalDetail);
						}
							 
							
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, invoicePaymentHeader.getACCOUNT_NAME());
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail_1.setACCOUNT_NAME(invoicePaymentHeader.getACCOUNT_NAME());
							if(invoicePaymentHeader.getBANK_CHARGE()>0) {
							journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED()+invoicePaymentHeader.getBANK_CHARGE());
							}else {
							 journalDetail_1.setCREDITS(invoicePaymentHeader.getAMOUNTRECEIVED());
							}
							journalDetails.add(journalDetail_1);
							}
							if(invoicePaymentHeader.getBANK_CHARGE()>0)
							{
								Double bankChargeFrom=invoicePaymentHeader.getBANK_CHARGE();
								if(bankChargeFrom>0)
								{
									JournalDetail journalDetail_2=new JournalDetail();
									journalDetail_2.setPLANT(plant);
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "Bank charges");
									if(coaJson2.isEmpty() || coaJson2.isNullObject())
									{
										
									}
									else
									{
									journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail_2.setACCOUNT_NAME("Bank charges");
									journalDetail_2.setDEBITS(bankChargeFrom);
									journalDetails.add(journalDetail_2);
									}
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
	    				if(exceptionUtil.getStatus()==Result.OK) {
							
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, plant);					
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_PAYMENT_RECEIVED);
							htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoicePaymentHeader.getRECEIVE_DATE()));														
							//htMovHis.put(IDBConstants.ITEM, invoicePaymentHeader.getRECEIVE_MODE());						
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoicePaymentHeader.getID());
							htMovHis.put(IDBConstants.CREATED_BY, username);		
							htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htMovHis.put("REMARKS",invoicePaymentHeader.getAMOUNTRECEIVED());
							boolean isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
							
						}
	    				
						  if(exceptionUtil.getStatus()==Result.OK) {
							  DbBean.CommitTran(ut);
							  resp.getWriter().write("Payment Received Successfully"); 
						  }
							 
    					
    					
    				}	
    			}
    			if(exceptionUtil.getStatus()==Result.ERROR)
    			{
    				DbBean.RollbackTran(ut);
    				resp.getWriter().write(exceptionUtil.getClassName()+"::"+exceptionUtil.getErrorMessage());
    			}
    			
    	}
            catch (Exception e) {
            	e.printStackTrace();
            }
        }

	}
	
	if(action.equals("PDA_PAYMENTPROCESS")){
		
		boolean flag = false;
		String reverseDate = "",Id="";
		InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
		try {
 			
 			String FDATE= (String) req.getParameter("FROM_DATE");
 			String TDATE= (String) req.getParameter("TO_DATE");
 			String BANK= (String) req.getParameter("bank_account_name");
 			String CNAME= (String) req.getParameter("CUSTOMER");
 			String CCODE= (String) req.getParameter("CUST_CODE");
 			String CHEQUENO= (String) req.getParameter("CHEQUENO");
 			
 			String[] chkhdrid  = req.getParameterValues("chkhdrid");
 	        String[] chkdPoNos  = req.getParameterValues("chkdPoNo");
 	       CoaDAO coaDAO=new CoaDAO();
			String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY")).trim();
 	       if (chkdPoNos != null)    {
 	    	  for (int i = 0; i < chkdPoNos.length; i++){
					String	data = chkdPoNos[i];
					String	hdrid = chkhdrid[i];
					reverseDate = StrUtils.fString(req.getParameter("Reverse_"+data));
					Hashtable receiveMaterial_HM = new Hashtable();
						//receiveMaterial_HM.put("CHEQUE_REVERSAL_DATE", reverseDate);
						receiveMaterial_HM.put("STATUS", "PROCESSED");
						receiveMaterial_HM.put("UPBY", username);
						receiveMaterial_HM.put("UPAT", dateutils.getDateTime());
					int paymentHdrId = invpaymentdao.updatePaymentPDC(data,receiveMaterial_HM,plant);
					String camount = "0";
					Hashtable ht = new Hashtable();						
					ht.put("ID", data);
					ht.put("PLANT", plant);			
					List pdcDetailList = invpaymentdao.getpdcpaymentById(ht);
					int payHdrid=Integer.parseInt(hdrid);
					InvPaymentHeader invPayHeader=invpaymentdao.getInvoicePaymentById(payHdrid, plant, username);
					LocalDate today = LocalDate.now();
					DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					String paymentDate=today.format(datePattern);
					//Journal Entry
					List<JournalDetail> journalDetails=new ArrayList<>();
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(paymentDate);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("SALESPAYMENT");
					journalHead.setTRANSACTION_ID(hdrid);
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					if(pdcDetailList.size()>0)
					{
						Map pdcHdr=(Map)pdcDetailList.get(0);
						String PAYMENTID= (String) pdcHdr.get("RECEIVEID");
						String CUSTOMER= (String) pdcHdr.get("CUSTOMER");
						String CUST_CODE= (String) pdcHdr.get("CUSTNO");
						String ACCOUNT= (String) pdcHdr.get("ACCOUNT");
						String BANK_BRANCH= (String) pdcHdr.get("BANK_BRANCH");
						String CHECQUE_NO= (String) pdcHdr.get("CHECQUE_NO");
						String CHEQUE_DATE= (String) pdcHdr.get("CHEQUE_DATE");
						String CHEQUE_REVERSAL_DATE= (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
						camount = (String) pdcHdr.get("CHEQUE_AMOUNT");
						Double bank_charges=0.00;
						if(invPayHeader.getBANK_CHARGE()!=null && !"".equals(invPayHeader.getBANK_CHARGE())) {
							bank_charges=invPayHeader.getBANK_CHARGE();
						}
						String SAVNAME="";
						if(CUST_CODE.equalsIgnoreCase("N/A")) {SAVNAME=ACCOUNT;}else {SAVNAME=CUSTOMER;}
						
						//Journal Entry for PDC
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, "PDC received");
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME("PDC received");
						journalDetail.setCREDITS(Double.parseDouble(camount)+bank_charges);
						journalDetail.setDESCRIPTION("Bank check no: "+(String) pdcHdr.get("CHECQUE_NO"));
						journalDetails.add(journalDetail);
						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson_1=coaDAO.getCOAByName(plant, invPayHeader.getDEPOSIT_TO());
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson_1.getString("id")));
						journalDetail_1.setACCOUNT_NAME(invPayHeader.getDEPOSIT_TO());
						journalDetail_1.setDEBITS(Double.parseDouble(camount)+bank_charges);
						journalDetails.add(journalDetail_1);
						
						journalHead.setSUB_TOTAL(Double.parseDouble(camount)+bank_charges);
						journalHead.setTOTAL_AMOUNT(Double.parseDouble(camount)+bank_charges);
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
					htMovHis.put("DIRTYPE", TransactionConstants.PDC_PROCESS_PAYMENT_RECEIVED);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, PAYMENTID);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS", SAVNAME+","+BANK_BRANCH+","+CHECQUE_NO+","+CHEQUE_DATE+","+CHEQUE_REVERSAL_DATE);
					
					flag = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
					
					InvPaymentHeader invoicepayheader = invoicePaymentDAO.getInvoicePaymentById(Integer.parseInt(chkhdrid[i]), plant, username);
					invoicepayheader.setISPDCPROCESS((short) 0);
					invoicePaymentDAO.updateInvoicePaymentHdr(invoicepayheader, plant, username);
					
					List<InvPaymentDetail> invpaydetail = invoicePaymentDAO.getInvoicePaymentDetailsbylineno(Integer.parseInt(chkhdrid[i]), "0", plant);
					if(invpaydetail.size() > 0) {
						InvPaymentDetail det = invpaydetail.get(0);
						det.setBALANCE(Double.parseDouble(camount) + det.getBALANCE());
						invoicePaymentDAO.updateInvoicePaymentDet(det, plant, username);
					}
 	    	  }
 	    	  
 	    	 	  				
 	    	 req.getSession().setAttribute("RESULT",	"PDC Payment processed successfully!");
  				//resp.sendRedirect("jsp/pdcpaymentReceiveSummary.jsp?result=PDC Payment processed successfully!&FROM_DATE="+FDATE+"&TO_DATE="+TDATE+"&CUSTOMER="+CNAME+"&CUST_CODE="+CCODE+"&BANK="+BANK+"&CHEQUENO="+CHEQUENO);
				resp.sendRedirect("../banking/pdcpayreceiveprocess?result=PDC Payment processed successfully!");
  			
 	       }
	}catch (Exception e) {
 			this.mLogger.exception(this.printLog, "", e);
 			req.getSession().setAttribute("CATCHERROR", e.getMessage());
 			System.out.print((String)req.getSession().getAttribute("CATCHERROR"));
 			resp.sendRedirect("../banking/pdcpayreceiveprocess?result=Failed to Save PDC Payment Process"+ e.toString());
 		}
	}else if(action.equals("PDA_PAYMENTEDIT")){
		
		boolean flag = false;
		String reverseDate = "",Id="";
		try {
 			Id= (String) req.getParameter("TRANID");
 			reverseDate= (String) req.getParameter("CHEQUE_REVERSAL_DATE");
 			
					Hashtable receiveMaterial_HM = new Hashtable();
						receiveMaterial_HM.put("CHEQUE_REVERSAL_DATE", reverseDate);
						//receiveMaterial_HM.put("STATUS", "PROCESSED");
						receiveMaterial_HM.put("UPBY", username);
						receiveMaterial_HM.put("UPAT", dateutils.getDateTime());
					int paymentHdrId = invpaymentdao.updatePaymentPDC(Id,receiveMaterial_HM,plant);
 	    	  
					Hashtable ht = new Hashtable();						
					ht.put("ID", Id);
					ht.put("PLANT", plant);			
					List pdcDetailList = invpaymentdao.getpdcpaymentById(ht);
					if(pdcDetailList.size()>0)
					{
						Map pdcHdr=(Map)pdcDetailList.get(0);
						String PAYMENTID= (String) pdcHdr.get("RECEIVEID");
						String CUSTOMER= (String) pdcHdr.get("CUSTOMER");
						String CUST_CODE= (String) pdcHdr.get("CUSTNO");
						String ACCOUNT= (String) pdcHdr.get("ACCOUNT");
						String BANK_BRANCH= (String) pdcHdr.get("BANK_BRANCH");
						String CHECQUE_NO= (String) pdcHdr.get("CHECQUE_NO");
						String CHEQUE_DATE= (String) pdcHdr.get("CHEQUE_DATE");
						String CHEQUE_REVERSAL_DATE= (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
						String SAVNAME="";
						if(CUST_CODE.equalsIgnoreCase("N/A")) {SAVNAME=ACCOUNT;}else {SAVNAME=CUSTOMER;}
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.PDC_EDIT_PAYMENT_RECEIVED);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, PAYMENTID);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS", SAVNAME+","+BANK_BRANCH+","+CHECQUE_NO+","+CHEQUE_DATE+","+CHEQUE_REVERSAL_DATE);
					
					flag = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
					req.getSession().setAttribute("RESULT",	"PDC Payment Updated successfully!");
					resp.sendRedirect("../banking/pdcpayreceiveprocess?result=PDC Payment processed successfully!");
  			
 	}catch (Exception e) {
 			this.mLogger.exception(this.printLog, "", e);
 			req.getSession().setAttribute("CATCHERROR", e.getMessage());
 			System.out.print((String)req.getSession().getAttribute("CATCHERROR"));
 			resp.sendRedirect("../banking/editpdcpayreceive?TRANID="+Id+"&result=Failed to Update PDC Payment"+ e.toString());
 		}
	}
}

protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
	String action = StrUtils.fString(req.getParameter("CMD")).trim();
	String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
	String username = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
	List<InvPaymentHeader> invoicePayments=new ArrayList<InvPaymentHeader>();
	InvoicePaymentUtil invoicePaymentUtil=new InvoicePaymentUtil();
	JSONObject invoicePaymentJson = new JSONObject();
	
	if(action.equalsIgnoreCase("deletepayment"))
	{
		
		String transid = req.getParameter("TRANSID");
		String remark = req.getParameter("REMARK");
		
		InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
		InvoiceDAO invoicedao = new InvoiceDAO();
		DateUtils dateutils = new DateUtils();
		MovHisDAO movHisDao = new MovHisDAO();
		UserTransaction ut = null;
		String result="";
		try {
			/*Get Transaction object*/
			ut = DbBean.getUserTranaction();				
			/*Begin Transaction*/
			ut.begin();
			
			List<InvPaymentDetail> invoicePaymentDetails=invoicePaymentDAO.getInvoicePaymentDetails(Integer.parseInt(transid), plant, username);
			for(InvPaymentDetail invoicePaymentDetail:invoicePaymentDetails)  {
				if(invoicePaymentDetail.getINVOICEHDRID() > 0) {
					double invpaymentmade=invoicePaymentDAO.getbalacedue(plant, String.valueOf(invoicePaymentDetail.getINVOICEHDRID()));
					double invoicebalance = invpaymentmade -invoicePaymentDetail.getAMOUNT();
					if(invoicebalance > 0) {
						Hashtable htgi = new Hashtable();	
						String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Partially Paid',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoicePaymentDetail.getINVOICEHDRID()+"'";
						invoicedao.updategino(sqlgi, htgi, "");
					}else {
						Hashtable htgi = new Hashtable();	
						String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Open',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoicePaymentDetail.getINVOICEHDRID()+"'";
						invoicedao.updategino(sqlgi, htgi, "");
					}
				}
			}
			
			JournalService journalService=new JournalEntry();
			Journal journalFrom=journalService.getJournalByTransactionId(plant, transid,"SALESPAYMENT");
			if(journalFrom.getJournalHeader()!=null)
			{
				if(journalFrom.getJournalHeader().getID()>0)
				{
					journalService.DeleteJournal(plant, journalFrom.getJournalHeader().getID());
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

			Hashtable htMovHis = new Hashtable();
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, plant);					
			htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT_RECEIVED);
			htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(transid));
			htMovHis.put(IDBConstants.CREATED_BY, username);		
			htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htMovHis.put("REMARKS",remark);
			movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			
			boolean isdeleted = invoicePaymentDAO.deleteInvoicepaymentall(plant, transid);
			
			if(isdeleted) {
				DbBean.CommitTran(ut);
				result = "Received payment deleted successfully";
				resp.sendRedirect("../banking/invoicepaysummary?result="+ result);
			}else {
				DbBean.RollbackTran(ut);
				result = "unable to delete received payment";
				resp.sendRedirect("../banking/invoicepaydetail?ID="+transid+"&resultnew="+ result);
			}
			
			
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "unable to delete received payment";
				resp.sendRedirect("../banking/invoicepaydetail?ID="+transid+"&resultnew="+ result);
			}
		
	}
	
	if(action.equalsIgnoreCase("checkpdcstatusfordelete"))
	{
		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
		String pdcstatus = "0";
		try {
			Hashtable ht = new Hashtable();
			ht.put("PAYMENTID", req.getParameter("ID"));
			ht.put("PLANT", plant);
			List pdcDetailList = invpaymentdao.getpdcbipayid(ht);		
			for(int j =0; j < pdcDetailList.size(); j++) {
				Map pdcdet=(Map)pdcDetailList.get(j);
				String status = (String)pdcdet.get("STATUS");
				if(status.equalsIgnoreCase("PROCESSED")) {
					pdcstatus = "1";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			pdcstatus="2";
		}
		resp.getWriter().write(pdcstatus);
	}
	
	
	
	if(action.equalsIgnoreCase("checkpdcstatus"))
	{
		String pdcstatus = "0";
		Hashtable ht = new Hashtable();
		ht.put("PAYMENTID", req.getParameter("ID"));
		ht.put("PLANT", plant);
		
		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
		
		try {
			List pdcDetailList = invpaymentdao.getpdcbipayid(ht);		
			for(int j =0; j < pdcDetailList.size(); j++) {
				Map pdcdet=(Map)pdcDetailList.get(j);
				String status = (String)pdcdet.get("STATUS");
				if(status.equalsIgnoreCase("PROCESSED")) {
					pdcstatus = "1";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			pdcstatus="2";
		}
		
		resp.getWriter().write(pdcstatus);
	}
	
	if(action.equalsIgnoreCase("refundcredit"))
	{
		String detid = req.getParameter("DETID");
		String transid = req.getParameter("TRANSID");
		String invoice = req.getParameter("INVOICENO");
		String pamount = req.getParameter("AMOUNT");
		String logstatus = req.getParameter("LOGSTATUS");

		InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
		InvoiceDAO invoicedao = new InvoiceDAO();
		CustomerCreditnoteDAO customerCreditDao = new CustomerCreditnoteDAO();
		DateUtils dateutils = new DateUtils();
		MovHisDAO movHisDao = new MovHisDAO();
		UserTransaction ut = null;
		String result="";
		try {
			/*Get Transaction object*/
			ut = DbBean.getUserTranaction();				
			/*Begin Transaction*/
			ut.begin();
			
			InvPaymentDetail invPayDetails = invoicePaymentDAO.getInvoicePaymentDetailsbyid(Integer.parseInt(detid), plant);
			
			List<InvPaymentDetail> invPayDetailslistbyhdrid = invoicePaymentDAO.getInvoicePaymentDetails(invPayDetails.getRECEIVEHDRID(), plant,"");
			InvPaymentDetail invPayDetailsbyhdrid = invPayDetailslistbyhdrid.get(0);
			double detbalance = invPayDetailsbyhdrid.getBALANCE();
			
			InvPaymentDetail invPayDetailslineupdate = invPayDetailslistbyhdrid.get(0);
			invPayDetailslineupdate.setBALANCE(invPayDetailslineupdate.getBALANCE()+invPayDetails.getAMOUNT());
			invoicePaymentDAO.updateInvoicePaymentDet(invPayDetailslineupdate, plant, username);
			
			double invpaymentmade=invoicePaymentDAO.getbalacedue(plant, String.valueOf(invPayDetails.getINVOICEHDRID()));
			
			double invoicebalance = invpaymentmade -invPayDetails.getAMOUNT();
			if(invoicebalance > 0) {
				Hashtable htgi = new Hashtable();	
				String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Partially Paid',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invPayDetails.getINVOICEHDRID()+"'";
				invoicedao.updategino(sqlgi, htgi, "");
			}else {
				Hashtable htgi = new Hashtable();	
				String sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Open',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invPayDetails.getINVOICEHDRID()+"'";
				invoicedao.updategino(sqlgi, htgi, "");
			}
			
			invoicePaymentDAO.deleteInvoicePaymentDetById(Integer.valueOf(detid), plant, username);
			
			int crid = invPayDetailsbyhdrid.getCREDITNOTEHDRID();
			if(crid > 0) {
				String crquery= "0";
				double la = invPayDetailsbyhdrid.getAMOUNT();
				double tb = detbalance+invPayDetails.getAMOUNT();
				if(la==tb) {
					crquery = " SET CREDIT_STATUS='Open'";	
				}else {
					crquery = " SET CREDIT_STATUS='Partially Applied'";	
				}								
				
				Hashtable CreditnoteHdrupdate = new Hashtable();
				CreditnoteHdrupdate.put("ID", String.valueOf(crid));
				CreditnoteHdrupdate.put("PLANT", plant);
				customerCreditDao.update(crquery, CreditnoteHdrupdate, "");
			}
			
			Hashtable ht9 = new Hashtable();	
			ht9.put("ID",String.valueOf(invPayDetails.getINVOICEHDRID()));
			ht9.put("PLANT",plant);
			List invoicebyidlist = invoicedao.getInvoiceHdrById(ht9);
			Map invoicebyid=(Map)invoicebyidlist.get(0);
			String Dono = (String)invoicebyid.get("DONO");
			String rDono = "";
			if(!Dono.isEmpty()) {
				rDono = Dono+",";
			}
			
			
			Hashtable htMovHis = new Hashtable();
			htMovHis.clear();
			htMovHis.put(IDBConstants.PLANT, plant);
			if(logstatus.equalsIgnoreCase("unapply")) {
				htMovHis.put("DIRTYPE", TransactionConstants.UNAPPLY_CREDIT_PAYMENT_RECEIVED);
			}else {
				htMovHis.put("DIRTYPE", TransactionConstants.DELETE_PAYMENT_RECEIVED);
			}
			htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
			//htMovHis.put(IDBConstants.ITEM, (String)billpayHdr.get("PAYMENT_MODE"));						
			htMovHis.put("RECID", "");
			htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(transid));
			htMovHis.put(IDBConstants.CREATED_BY, username);		
			htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htMovHis.put("REMARKS",rDono+invoice+","+pamount);
			movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			
			DbBean.CommitTran(ut);
			result = "Received credit payment unapplied successfully";
			resp.sendRedirect("../banking/invoicepaydetail?ID="+transid+"&rsuccess="+ result);
			
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "unable to unapply received credit payment";
				resp.sendRedirect("../banking/invoicepaydetail?ID="+transid+"&resultnew="+ result);
			}
		
	}
	
	
	//getAllInvoicePayment
	if(action.equalsIgnoreCase("getAllInvoicePayments"))
	{
		System.out.println("Invoice Payment Records ALL");
		try {
			invoicePayments=invoicePaymentUtil.getAllInvoicePayment(plant, username);
			
			invoicePaymentJson.put("data",invoicePayments);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------------------------------------------------");
		System.out.println(invoicePaymentJson.toString());
		resp.getWriter().write(invoicePaymentJson.toString());
	}
	
	else if(action.equalsIgnoreCase("getpdcbyid"))
	{
		String balace = "0";
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJsonInt1 = new JSONObject();
        JSONArray jsonArrayErr = new JSONArray();
		Hashtable ht = new Hashtable();
		
		ht.put("PAYMENTID", req.getParameter("paymentid"));
		ht.put("PLANT", plant);
		
		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
		
		try {
			 List pdcDetailList = invpaymentdao.getpdcbipayid(ht);
			 JSONObject resultJsonInt = new JSONObject();
			 resultJson.put("PDC", pdcDetailList);
	           
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		
		
		resp.getWriter().write(resultJson.toString());
		
	}
	
	else if(action.equalsIgnoreCase("getInvoicePaymentsByFilter"))
	{
		System.out.println("Invoice Payment Records Filter");
		InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
		String fromDate= StrUtils.fString((String) req.getParameter("FROM_DATE")).trim();
		String toDate= StrUtils.fString((String) req.getParameter("TO_DATE")).trim();
		String custNo= StrUtils.fString((String) req.getParameter("CUST_NO")).trim();
		String refNo= StrUtils.fString((String) req.getParameter("REFERENCE")).trim();
		boolean flag=false; 
		String fdate="",tdate="",sCondition="";
		
		 if(fromDate==null) fromDate=""; else fromDate = fromDate.trim();
         String curDate =DateUtils.getDate();
			if (fromDate.length() < 0 || fromDate == null || fromDate.equalsIgnoreCase(""))
				fromDate=curDate;
		
		 if (fromDate.length()>5)
	            fdate    = fromDate.substring(6)+"-"+ fromDate.substring(3,5)+"-"+fromDate.substring(0,2);
		 if (toDate.length()>5)
	           tdate    = toDate.substring(6)+"-"+ toDate.substring(3,5)+"-"+toDate.substring(0,2);
		String whereQuery="Where ";
		whereQuery+="ISNULL(RECEIVE_DATE,'')<>'' AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)";
		if (fdate.length() > 0) {
			whereQuery = whereQuery +  "  >= '" 
						+ fdate
						+ "'  ";
				if (tdate.length() > 0) {
					whereQuery = whereQuery + "AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '" 
					+ tdate
					+ "'  ";
				}
				flag=true;
			  } else {
				if (tdate.length() > 0) {
					whereQuery = whereQuery + " <= '" 
					+ tdate
					+ "'  ";
					flag=true;
				}
		     	}
		if(custNo!=null && custNo!="")
		{
			if(flag)
			{
				whereQuery = whereQuery +" AND CUSTNO='"+custNo+"'";
			}
			else
			{
				whereQuery = whereQuery +" CUSTNO='"+custNo+"'";
				flag=true;
			}
			
		}
		if(refNo!=null && refNo!="")
		{
			whereQuery = whereQuery +" AND REFERENCE='"+refNo+"'";
		}
		if(flag)
		{
			try {
				invoicePayments=invoicePaymentUtil.getAllInvoicePaymentByFilter(whereQuery, plant, username);
				for(InvPaymentHeader invpay:invoicePayments) {
					Hashtable ht = new Hashtable();
					ht.put("PAYMENTID", String.valueOf(invpay.getID()));
					ht.put("PLANT", plant);
					List pdclist = invoicePaymentDAO.getpdcbipayid(ht);
					for(int j =0; j < pdclist.size(); j++) {
						Map pdcfor=(Map)pdclist.get(j);
						String status = (String)pdcfor.get("STATUS");
						if(status.equalsIgnoreCase("PROCESSED")) {
							invpay.setISPDCPROCESS((short) 1);
						}else if(status.equalsIgnoreCase("NOT PROCESSED")){
							invpay.setISPDCPROCESS((short) 1);
						}else {
							invpay.setISPDCPROCESS((short) 0);
						}
					}
					
				}
				invoicePaymentJson.put("data",invoicePayments);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			invoicePaymentJson.put("data",invoicePayments);
		}
		resp.getWriter().write(invoicePaymentJson.toString());
	}
	else if(action.equalsIgnoreCase("getInvoiceByCust"))
	{
		System.out.println("Invoice by customer");
		String custno=StrUtils.fString(req.getParameter("custnumber")).trim();
		InvoiceDAO invoiceDao=new InvoiceDAO();
		Hashtable ht = new Hashtable();
		ht.put("CUSTNO", custno);
		ht.put("PLANT", plant);
		List invoiceHdrList=null;
		JSONObject invoiceHdrlistJson=new JSONObject();
		try {
			invoiceHdrList =  invoiceDao.getInvoiceHdrByCustNoadvance(ht);
			
			invoiceHdrlistJson.put("custlist", invoiceHdrList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.getWriter().write(invoiceHdrlistJson.toString());
	}
	
	if(action.equalsIgnoreCase("getbalanceofinvoice"))
	{
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJsonInt1 = new JSONObject();
        JSONArray jsonArrayErr = new JSONArray();
        InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
	
		try {
			double advancebalace = invpaymentdao.getcreditamout(plant, (String) req.getParameter("CUSTNO"));
			double orderbalance  = invpaymentdao.getcreditamoutusingorderno(plant, (String) req.getParameter("DONO"));
			double ovallbalace = advancebalace + orderbalance;
			
			double balance = invpaymentdao.getbalacedue(plant, req.getParameter("INVID"));
			
			 JSONObject resultJsonInt = new JSONObject();
			 resultJson.put("BALANCE",  balance);
			 resultJson.put("CREDIT", orderbalance);
			 resultJson.put("ALLCREDIT", ovallbalace);
			/*
			 * jsonArrayErr.add(resultJsonInt); resultJson.put("balance", jsonArrayErr);
			 */
				
	           
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		resp.getWriter().write(resultJson.toString());
		
	}
	
	if(action.equalsIgnoreCase("showcreditforapply"))
	{
		String balace = "0";
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJsonInt1 = new JSONObject();
        JSONArray jsonArrayErr = new JSONArray();
        InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
        CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
		Hashtable ht = new Hashtable();

		try {
			List advanceCridit = invpaymentdao.getInvoicePaymentDetails(plant, "ADVANCE", (String) req.getParameter("CUSTNO"), (String) req.getParameter("DONO"));
			
			for(int j =0; j < advanceCridit.size(); j++) {
					Map creditDetail=(Map)advanceCridit.get(j);	
					String crdid =(String)creditDetail.get("CREDITNOTEHDRID");
					
					if(crdid.equals("0")){
						
					}else{
						Hashtable ht11 = new Hashtable();
						ht11.put("ID", crdid);
						ht11.put("PLANT", plant);
						List CustcrdnoteHdrList =  custcrdnotedao.getCustCreditnoteHdrById(ht11);
						Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
						creditDetail.put("CREDITNOTEHDRID", CustcrdnoteHdr.get("CREDITNOTE"));
					}
			}
			
			
			resultJson.put("CREDIT", advanceCridit);       
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		
		
		resp.getWriter().write(resultJson.toString());
		
	}
	else if(action.equalsIgnoreCase("getAttachment"))
	{
		System.out.println("Attachments");
		int paymentid=Integer.parseInt(req.getParameter("paymentid"));
		InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
		JSONObject paymentAttachlistJson=new JSONObject();
		List<InvPaymentAttachment> paymentAttachment = null;
		try {
			paymentAttachment = invoicePaymentDAO.getInvoiceAttachmentDetails(paymentid, plant, username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paymentAttachlistJson.put("attachlist", paymentAttachment);
		
		resp.getWriter().write(paymentAttachlistJson.toString());
	}
	else if(action.equalsIgnoreCase("downloadAttachmentById"))
	{
		System.out.println("Attachments by ID");
		int ID=Integer.parseInt(req.getParameter("attachid"));
		FileHandling fileHandling=new FileHandling(); 
		InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
		List<InvPaymentAttachment> paymentAttachment = null;
		try {
			paymentAttachment = invoicePaymentDAO.getInvoiceAttachmentDetailsByID(ID, plant, username);
			String filePath=paymentAttachment.get(0).getFilePath();
			String fileType=paymentAttachment.get(0).getFileType();
			String fileName=paymentAttachment.get(0).getFileName();
			fileHandling.fileDownload(filePath, fileName, fileType, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	else if(action.equalsIgnoreCase("removeAttachmentById"))
	{
		System.out.println("Remove Attachments by ID");
		int ID=Integer.parseInt(req.getParameter("removeid"));
		InvoicePaymentDAO invoicePaymentDAO=new InvoicePaymentDAO();
		try {
			invoicePaymentDAO.deleteInvoicePaymentAttachmentById(ID, plant, username);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.getWriter().write("Deleted");
	}else if(action.equalsIgnoreCase("getPaymentsReceivedDetails")){

		String fromDate= StrUtils.fString((String) req.getParameter("FROM_DATE")).trim();
		String toDate= StrUtils.fString((String) req.getParameter("TO_DATE")).trim();
		String custNo= StrUtils.fString((String) req.getParameter("CUST_NO")).trim();
		String refNo= StrUtils.fString((String) req.getParameter("REFERENCE")).trim();
		boolean flag=false; 
		String fdate="",tdate="",sCondition="";
		
		 if(fromDate==null) fromDate=""; else fromDate = fromDate.trim();
         String curDate =DateUtils.getDate();
			if (fromDate.length() < 0 || fromDate == null || fromDate.equalsIgnoreCase(""))
				fromDate=curDate;
		
		 if (fromDate.length()>5)
	            fdate    = fromDate.substring(6)+"-"+ fromDate.substring(3,5)+"-"+fromDate.substring(0,2);
		 if (toDate.length()>5)
	           tdate    = toDate.substring(6)+"-"+ toDate.substring(3,5)+"-"+toDate.substring(0,2);
		String whereQuery="Where ";
		whereQuery+="ISNULL(RECEIVE_DATE,'')<>'' AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)";
		if (fdate.length() > 0) {
			whereQuery = whereQuery +  "  >= '" 
						+ fdate
						+ "'  ";
				if (tdate.length() > 0) {
					whereQuery = whereQuery + "AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '" 
					+ tdate
					+ "'  ";
				}
				flag=true;
			  } else {
				if (tdate.length() > 0) {
					whereQuery = whereQuery + " <= '" 
					+ tdate
					+ "'  ";
					flag=true;
				}
		     	}
		if(custNo!=null && custNo!="")
		{
			if(flag)
			{
				whereQuery = whereQuery +" AND CUSTNO='"+custNo+"'";
			}
			else
			{
				whereQuery = whereQuery +" CUSTNO='"+custNo+"'";
				flag=true;
			}
			
		}
		if(refNo!=null && refNo!="")
		{
			whereQuery = whereQuery +" AND REFERENCE='"+refNo+"'";
		}
		if(flag)
		{
			try {
				invoicePayments=invoicePaymentUtil.getPaymentReceivedDetails(whereQuery, plant, username);
				invoicePaymentJson.put("data",invoicePayments);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			invoicePaymentJson.put("data",invoicePayments);
		}
		resp.getWriter().write(invoicePaymentJson.toString());
	}else if (action.equals("ExportExcelPaymentsReceived")) {
		String newHtml = "";
		HSSFWorkbook wb = new HSSFWorkbook();
		String PLANT = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		wb = this.writeToExcelPaymentsReceived(req, resp, wb);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		resp.setContentType("application/ms-excel");
		resp.setContentLength(outArray.length);
		resp.setHeader("Expires:", "0"); // eliminates browser caching
		resp.setHeader("Content-Disposition", "attachment; filename=customer_payments.xls");
		OutputStream outStream = resp.getOutputStream();
		outStream.write(outArray);
		outStream.flush();
		outStream.close();
	}
	
	if(action.equalsIgnoreCase("GET_PDC_PAYMENT"))
	{
		String balace = "0";
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJsonInt1 = new JSONObject();
        JSONArray jsonArrayErr = new JSONArray();
		Hashtable ht = new Hashtable();
		String fdate="",tdate="";
		String FROM_DATE   =StrUtils.fString(req.getParameter("FDATE"));
		String TO_DATE   =StrUtils.fString(req.getParameter("TDATE"));
		String CUSTOMER   =StrUtils.fString(req.getParameter("CUSTOMER"));
		String BANK   =StrUtils.fString(req.getParameter("BANK"));
		String CHEQUENO   =StrUtils.fString(req.getParameter("CHEQUENO"));
		String TYPE   =StrUtils.fString(req.getParameter("TYPE"));
		String STATUS   =StrUtils.fString(req.getParameter("STATUS"));
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
       String curDate =DateUtils.getDate();
       if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

       if (FROM_DATE.length()>5)
        fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

       if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
       //if(TO_DATE.length()<0||TO_DATE==null||TO_DATE.equalsIgnoreCase(""))TO_DATE=curDate;
       if (TO_DATE.length()>5)
       tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

       
		
		ht.put("PLANT", plant);
		
		InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
		
		try {
			 List pdcDetailList = invpaymentdao.getpdcpayment(ht,fdate,tdate,CUSTOMER,BANK,CHEQUENO,TYPE,STATUS);
			 JSONObject resultJsonInt = new JSONObject();
			 resultJson.put("PDC", pdcDetailList);
	           
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		
		
		resp.getWriter().write(resultJson.toString());
		
	}

}

private HSSFWorkbook writeToExcelPaymentsReceived(HttpServletRequest request, HttpServletResponse response,
		HSSFWorkbook wb) {
	StrUtils strUtils = new StrUtils();
	ExpensesUtil expensesUtil       = new  ExpensesUtil();
    PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	List movQryList = new ArrayList();
	InvoicePaymentUtil invoicePaymentUtil=new InvoicePaymentUtil();
	int maxRowsPerSheet = 65535;
	String taxby = "", custcode = "";
	int SheetId =1;
	try {
		String PLANT= StrUtils.fString((String) request.getParameter("plant")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String fromDate= StrUtils.fString((String) request.getParameter("FROM_DATE")).trim();
		String toDate= StrUtils.fString((String) request.getParameter("TO_DATE")).trim();
		String custNo= StrUtils.fString((String) request.getParameter("CUST_CODE")).trim();
		String refNo= StrUtils.fString((String) request.getParameter("REFERENCE")).trim();
		boolean flag=false; 
		String fdate="",tdate="",sCondition="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
		
		 if(fromDate==null) fromDate=""; else fromDate = fromDate.trim();
         String curDate =DateUtils.getDate();
			if (fromDate.length() < 0 || fromDate == null || fromDate.equalsIgnoreCase(""))
				fromDate=curDate;
		
		 if (fromDate.length()>5)
	            fdate    = fromDate.substring(6)+"-"+ fromDate.substring(3,5)+"-"+fromDate.substring(0,2);
		 if (toDate.length()>5)
	           tdate    = toDate.substring(6)+"-"+ toDate.substring(3,5)+"-"+toDate.substring(0,2);
		String whereQuery="Where ";
		whereQuery+="ISNULL(RECEIVE_DATE,'')<>'' AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date)";
		if (fdate.length() > 0) {
			whereQuery = whereQuery +  "  >= '" 
						+ fdate
						+ "'  ";
				if (tdate.length() > 0) {
					whereQuery = whereQuery + "AND CAST((SUBSTRING(RECEIVE_DATE, 7, 4) + '-' + SUBSTRING(RECEIVE_DATE, 4, 2) + '-' + SUBSTRING(RECEIVE_DATE, 1, 2)) AS date) <= '" 
					+ tdate
					+ "'  ";
				}
				flag=true;
			  } else {
				if (tdate.length() > 0) {
					whereQuery = whereQuery + " <= '" 
					+ tdate
					+ "'  ";
					flag=true;
				}
		     	}
		if(custNo!=null && custNo!="")
		{
			if(flag)
			{
				whereQuery = whereQuery +" AND CUSTNO='"+custNo+"'";
			}
			else
			{
				whereQuery = whereQuery +" CUSTNO='"+custNo+"'";
				flag=true;
			}
			
		}
		if(refNo!=null && refNo!="")
		{
			whereQuery = whereQuery +" AND REFERENCE='"+refNo+"'";
		}
		   movQryList = invoicePaymentUtil.getPaymentReceivedDetails(whereQuery, PLANT, username);	
           
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
				if(toDate.equalsIgnoreCase(""))
					toDate = curDate;
				sheet = this.createHeaderDate(sheet,CompHeader,"",fromDate,toDate,PLANT);
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
					
					String unitCostValue = (String)lineArr.get("AMOUNTRECEIVED");
           			String balCostValue = (String)lineArr.get("AMOUNTUFP");
                    float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                    if(unitCostVal==0f){
                    	unitCostValue="0.00000";
                    }else{
                    	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                    
                    unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                    
                    float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                    if(balCostVal==0f){
                    	balCostValue="0.00000";
                    }else{
                    	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                    }
                    balCostVal = unitCostVal - balCostVal;
                    balCostValue = StrUtils.addZeroes(balCostVal, numberOfDecimal);
					
					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ID"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("RECEIVE_DATE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("REFERENCE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CUSTNO"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("RECEIVE_MODE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("NOTE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("INVOICE"))));
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
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 5000);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
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
		cell.setCellValue(new HSSFRichTextString("Payment Number"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Date"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Reference"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Customer Name"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Payment Mode"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Notes"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Invoice"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Amount"));
		cell.setCellStyle(styleHeader);
		
		cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString("Unused Amount"));
		cell.setCellStyle(styleHeader);
		
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return sheet;
}

/** Invoice Payment Logic -End**/

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
