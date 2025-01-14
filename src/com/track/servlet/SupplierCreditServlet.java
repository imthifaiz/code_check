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
import com.track.dao.SupplierCreditDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.VendMstDAO;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.MasterDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinProject;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.SupplierCreditUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/SupplierCreditServlet")
public class SupplierCreditServlet  extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.SupplierCreditServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.SupplierCreditServlet_PRINTPLANTMASTERINFO;
	String action = "";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		action = StrUtils.fString(request.getParameter("Submit")).trim();
		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		if (baction.equals("VIEW_SUPPLIERCREDIT_SUMMARY_VIEW")) {
	    	  
	        jsonObjectResult = this.getcreditview(request);
			response.setContentType("application/json");			
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }else if(action.equalsIgnoreCase("getporeturnnoByVendandcredit"))
		{
			String PORETURN_STATUS=StrUtils.fString(request.getParameter("PORETURN_STATUS")).trim();
			String vendno=StrUtils.fString(request.getParameter("vendno")).trim();
			String plant=StrUtils.fString(request.getParameter("PLANT")).trim();
			List poreturnlist=new ArrayList<>();
			
			ReturnOrderDAO returnOrderDAO=new ReturnOrderDAO();
			JSONObject poreturnJson=new JSONObject();
			try {
				String query="PORETURN";
				String extquery = "PLANT = '"+plant+"' AND VENDNO ='"+vendno+"' AND STATUS != 'Applied' AND BILL != '' GROUP BY PORETURN ORDER BY PORETURN DESC";
				poreturnlist=returnOrderDAO.getPOReturnDetailsbyVendor(plant, query, extquery);
				poreturnJson.put("portno",poreturnlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.getWriter().write(poreturnJson.toString());
		}
		
		else if (baction.equals("VIEW_VENDOR_CREDIT_DETAILS_VIEW")) {	    	  
	        jsonObjectResult = this.getVendorCreditDetails(request);
			response.setContentType("application/json");			
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	       	       
	    }else if (baction.equals("ExportExcelVendorCreditDetails")) {
			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelVendorCreditDetails(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=vendor_credit_details.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}
	    else if (baction.equals("CheckOrderno")) {
			
			String orderno = StrUtils.fString(request.getParameter("CREDITNOTE")).trim();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("CREDITNOTE", orderno);
				if (new SupplierCreditDAO().isExisit(ht)) {
					jsonObjectResult.put("status", "100");
				} else {
					jsonObjectResult.put("status", "99");
				}
			} catch (Exception daRE) {
				jsonObjectResult.put("status", "99");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		if (action.equalsIgnoreCase("GET_EDIT_SUPPLIERCREDIT_DETAILS")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getEditDetails(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("GET_PO_RETURN")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getporeturnDetails(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}else if (action.equalsIgnoreCase("CHECK_SUPPLIER_CREDIT_NOTE")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.validateCreditnote(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (baction.equalsIgnoreCase("CONVERT_TO_OPEN")) {
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String plant= StrUtils.fString(request.getParameter("PLANT"));
			String billHdrId = StrUtils.fString(request.getParameter("ID"));
			SupplierCreditUtil SCUtil = new SupplierCreditUtil();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			Hashtable<String,String> paymentDetInfo = null;
			List<Hashtable<String,String>> paymentDetInfoList = null;
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
			DateUtils dateutils = new DateUtils();
			Hashtable ht = new Hashtable();
	    	ht.put("ID", billHdrId);
	    	ht.put("PLANT", plant);
	    	List billHdrList;
			try {
				billHdrList = SCUtil.getHdrById(ht);
				Map billHdr=(Map)billHdrList.get(0);
				
				String query = " SET CREDIT_STATUS='Open'";
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", billHdrId);
				ht1.put("PLANT", plant);
				supplierCreditDAO.updateHdr(query, ht1, "");
				
				Hashtable ht2 = new Hashtable();
				ht2.put("HDRID", billHdrId);
				ht2.put("PLANT", plant);
				
				List<Hashtable<String,String>> CNDetInfoList=SCUtil.getDetByHdrId(ht2);
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				//Journal Entry
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(billHdr.get("CREDIT_DATE").toString());
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("SUPPLIERCREDITNOTES");
				journalHead.setTRANSACTION_ID(billHdrId);
				journalHead.setSUB_TOTAL(Double.parseDouble(billHdr.get("SUB_TOTAL").toString()));
				//journalHead.setTOTAL_AMOUNT(Double.parseDouble(billHdr.get("TOTAL_AMOUNT").toString()));
				journalHead.setCRAT(dateutils.getDateTime());
				journalHead.setCRBY(username);
				
				List<JournalDetail> journalDetails=new ArrayList<>();
				CoaDAO coaDAO=new CoaDAO();
				VendMstDAO vendorDAO=new VendMstDAO();
				for(Map CNDet:CNDetInfoList)
				{
					
					JournalDetail journalDetail=new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
					System.out.println("Json"+coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
					journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString()));
					if(!billHdr.get("ORDERDISCOUNTTYPE").toString().equalsIgnoreCase("%")) {
						journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(billHdr.get("ORDER_DISCOUNT").toString())/CNDetInfoList.size());
					}else {
						Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(billHdr.get("ORDER_DISCOUNT").toString());
						journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
					}
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
				JSONObject coaJson1=coaDAO.getCOAByName(plant, billHdr.get("VENDNO").toString());
				if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
					JSONObject vendorJson=vendorDAO.getVendorName(plant, billHdr.get("VENDNO").toString());
					if(!vendorJson.isEmpty()) {
					coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
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
				journalDetail_1.setDEBITS(Double.parseDouble(billHdr.get("TOTAL_AMOUNT").toString()));
				journalDetails.add(journalDetail_1);
				}
				Double taxAmountFrom=Double.parseDouble(billHdr.get("TAXAMOUNT").toString());
				if(taxAmountFrom>0)
				{
					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
					/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
					journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
					journalDetail_2.setACCOUNT_NAME("VAT Input");*/
					
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
				
				
				
				Double discountFrom = Double.parseDouble(billHdr.get("DISCOUNT").toString());
				String discountType=billHdr.get("DISCOUNT_TYPE").toString();
				
				String orderdiscount=billHdr.get("ORDER_DISCOUNT").toString();
				Double orderDiscountFrom=0.00;
				if(!orderdiscount.isEmpty())
				{
					orderDiscountFrom=Double.parseDouble(orderdiscount);
					orderDiscountFrom=(Double.parseDouble(billHdr.get("SUB_TOTAL").toString())*orderDiscountFrom)/100;
				}
				if(discountFrom>0 || orderDiscountFrom>0)
				{
					if(!discountType.isEmpty())
					{
						if(discountType.equalsIgnoreCase("%"))
						{
							Double subTotalAfterOrderDiscount=Double.parseDouble(billHdr.get("SUB_TOTAL").toString());
							discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
						}
					}
					discountFrom=discountFrom;	
					JournalDetail journalDetail_3=new JournalDetail();
					journalDetail_3.setPLANT(plant);
					JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
					journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
					journalDetail_3.setACCOUNT_NAME("Discount Received");
					journalDetail_3.setDEBITS(discountFrom);
					journalDetails.add(journalDetail_3);
				}
				String adjustment=billHdr.get("ADJUSTMENT").toString();
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

			paymentDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable paymentHdr =new Hashtable();
			paymentHdr.put("PLANT", plant);
			paymentHdr.put("VENDNO", (String)billHdr.get("VENDNO"));
			paymentHdr.put("AMOUNTPAID", (String)billHdr.get("TOTAL_AMOUNT"));
			paymentHdr.put("PAYMENT_DATE", dateutils.getDate());
			paymentHdr.put("PAYMENT_MODE", "Cash");
			paymentHdr.put("PAID_THROUGH", "Advance");
			paymentHdr.put("REFERENCE", (String)billHdr.get("CREDITNOTE"));
			paymentHdr.put("BANK_BRANCH", "");
			paymentHdr.put("AMOUNTUFP", "0");
			paymentHdr.put("AMOUNTREFUNDED", "0");
			paymentHdr.put("BANK_CHARGE", "0");
			paymentHdr.put("CHECQUE_NO", "");
			paymentHdr.put("NOTE", "");
			paymentHdr.put("CURRENCYID", (String)billHdr.get("CURRENCYID"));
			paymentHdr.put("CRAT", dateutils.getDateTime());
			paymentHdr.put("CRBY", username);
			paymentHdr.put("UPAT", dateutils.getDateTime());
			paymentHdr.put("ISPDCPROCESS", "0");
			
			int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
			String payHdrId = Integer.toString(paymentHdrId);
			if(paymentHdrId > 0) {
				
					paymentDetInfo = new Hashtable<String, String>();
					paymentDetInfo.put("PLANT", plant);							
						paymentDetInfo.put("LNNO", "0");
						paymentDetInfo.put("AMOUNT", (String)billHdr.get("TOTAL_AMOUNT"));
						paymentDetInfo.put("BALANCE", (String)billHdr.get("TOTAL_AMOUNT"));													
					//paymentDetInfo.put("PONO", (String)billHdr.get("PONO"));
					paymentDetInfo.put("PONO", "");
					paymentDetInfo.put("PAYHDRID", payHdrId);
					paymentDetInfo.put("BILLHDRID", "0");												
					paymentDetInfo.put("TYPE", "ADVANCE");
					paymentDetInfo.put("ADVANCEFROM", "GENERAL");
					paymentDetInfo.put("CURRENCYUSEQT", (String)billHdr.get("CURRENCYUSEQT"));
					paymentDetInfo.put("CRAT", dateutils.getDateTime());
					paymentDetInfo.put("CRBY", username);
					paymentDetInfo.put("UPAT", dateutils.getDateTime());
					paymentDetInfoList.add(paymentDetInfo);
				
				billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
				
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
				htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				htMovHis.put("REMARKS",billHdr.get("CREDITNOTE")+","+billHdr.get("TOTAL_AMOUNT"));
				movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				
				
				
				response.sendRedirect("../debit/summary");
			}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	    	
		}
		
		else if(baction.equalsIgnoreCase("convertToCancel")) {
			String creditnoteHdrId = StrUtils.fString(request.getParameter("CreditnoteHdrId"));
			String creditnote = StrUtils.fString(request.getParameter("CreditNote"));
			String cnstatus = StrUtils.fString(request.getParameter("status"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateutils = new DateUtils();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			BillDAO billDAO = new BillDAO();
			String poreturn = "";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if(cnstatus.equalsIgnoreCase("draft") || cnstatus.equalsIgnoreCase("open") || cnstatus.equalsIgnoreCase("CANCELLED")) {
					if(cnstatus.equalsIgnoreCase("CANCELLED")){
						result = "Debit Note already cancelled";
						response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
					}else {
					
						Hashtable ht = new Hashtable();
						ht.put("ID", creditnoteHdrId);
						ht.put("PLANT", plant);		

						String query = " SET CREDIT_STATUS='CANCELLED'";
						isUpdated = supplierCreditDAO.updateHdr(query, ht, "");
						
						if(cnstatus.equalsIgnoreCase("open")) {
							Hashtable htHDR = new Hashtable();	
							htHDR.put("REFERENCE",creditnote);	
							String sqlHDR = "SELECT * FROM "+plant+"_FINPAYMENTHDR  WHERE PLANT='"+plant+"' ";
							List arrList = billDAO.selectForReport(sqlHDR, htHDR, "");
							Map grnbillHdr=(Map)arrList.get(0);
							isUpdated = billpaymentDao.deletePayment(plant, (String)grnbillHdr.get("ID"));
							
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, creditnoteHdrId,"SUPPLIERCREDITNOTES");
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
						
						
						if(isUpdated) {
							/*JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, creditnote,"SUPPLIERCREDITNOTES");
							if(journalFrom.getJournalHeader()!=null)
							{
								if(journalFrom.getJournalHeader().getID()>0)
								{
									journalFrom.getJournalHeader().setJOURNAL_STATUS("CANCELLED");
									journalService.updateJournal(journalFrom, username);
								}
							}*/
							
							Hashtable ht1 = new Hashtable();
							ht1.put("ID", creditnoteHdrId);
							ht1.put("PLANT", plant);
							
							List CNhdr = supplierCreditDAO.getHdrById(ht1);
							Map cnHdrmap=(Map)CNhdr.get(0);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("HDRID", creditnoteHdrId);
							ht2.put("PLANT", plant);
							
							List CNdet = supplierCreditDAO.getDetByHdrId(ht2);
							
								for(int i =0 ; i < CNdet.size() ; i++){
									Map cndetmap=(Map)CNdet.get(i);
								
										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);					
										htMovHis.put("DIRTYPE", TransactionConstants.CANCEL_DEBIT_NOTE);
										htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, (String) cndetmap.get("ITEM"));
										String billqty = String.valueOf((String) cndetmap.get("QTY"));
										htMovHis.put(IDBConstants.QTY, billqty);
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
										//htMovHis.put("REMARKS",vendname+","+pono+","+grnnumber+","+billnumber+","+poreturn+","+note);
										htMovHis.put("REMARKS",cnHdrmap.get("VNAME")+","+cnHdrmap.get("PONO")+","+cnHdrmap.get("GRNO")+","+cnHdrmap.get("BILL")+","+poreturn+","+cnHdrmap.get("NOTE"));
										movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}
							
							DbBean.CommitTran(ut);
							result = "Debit Note cancelled successfully.";
							response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&rsuccess="+ result);
						}else {
							DbBean.RollbackTran(ut);
							result = "Couldn't cancel the Debit Note.";
							response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
						}
						
					}
				}else {
					DbBean.RollbackTran(ut);
					result = "Couldn't cancel the Debit Note.";
					response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't cancel the Debit Note.";
				response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
			}
			
		}
		
		else if(baction.equalsIgnoreCase("deletecreditnote")) {
			String creditnoteHdrId = StrUtils.fString(request.getParameter("CreditnoteHdrId"));
			String creditnote = StrUtils.fString(request.getParameter("CreditNote"));
			String cnstatus = StrUtils.fString(request.getParameter("status"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			BillDAO billDAO = new BillDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateutils = new DateUtils();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
			ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			BillPaymentDAO billpaymentDao = new BillPaymentDAO();
			//String poreturn = "";
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if( cnstatus.equalsIgnoreCase("draft") || cnstatus.equalsIgnoreCase("CANCELLED") || cnstatus.equalsIgnoreCase("open")) {
					Hashtable ht1 = new Hashtable();
					ht1.put("ID", creditnoteHdrId);
					ht1.put("PLANT", plant);
					
					List CNhdr = supplierCreditDAO.getHdrById(ht1);
					Map cnHdrmap=(Map)CNhdr.get(0);
					
					Hashtable ht2 = new Hashtable();
					ht2.put("HDRID", creditnoteHdrId);
					ht2.put("PLANT", plant);
					if(cnstatus.equalsIgnoreCase("open")) {
						Hashtable htHDR = new Hashtable();	
						htHDR.put("REFERENCE",creditnote);	
						String sqlHDR = "SELECT * FROM "+plant+"_FINPAYMENTHDR  WHERE PLANT='"+plant+"' ";
						List arrList = billDAO.selectForReport(sqlHDR, htHDR, "");
						if(arrList.size() > 0) {
							Map grnbillHdr=(Map)arrList.get(0);
							isUpdated = billpaymentDao.deletePayment(plant, (String)grnbillHdr.get("ID"));
						}
					}
					
					String billno = (String)cnHdrmap.get("BILL");
					if(!billno.equalsIgnoreCase("")) {
						String querybill = " SET CREDITNOTESSTATUS =0";
						Hashtable ht = new Hashtable();
						ht.put("BILL", billno);
						ht.put("PLANT", plant);
						isUpdated = billDAO.updateBillHdr(querybill, ht, "");
					}
					
					
					List CNdet = supplierCreditDAO.getDetByHdrId(ht2);
						isUpdated = supplierCreditDAO.deleteSupplierCrdnote(plant, creditnoteHdrId);
						if(isUpdated) {
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, creditnoteHdrId,"SUPPLIERCREDITNOTES");
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
								if(!cnHdrmap.get("PORETURN").equals("")) {
									String setquery = "STATUS = 'Not Applied'";
									String pocondition = "PORETURN = '"+cnHdrmap.get("PORETURN")+"' AND PONO = '"+cnHdrmap.get("PONO")+"' AND GRNO = '"+cnHdrmap.get("GRNO")+"'";
									returnOrderDAO.updatePoReturnDetails(setquery, pocondition, plant);
								}
							}
							
							if(isUpdated) {
								for(int i =0 ; i < CNdet.size() ; i++){
									Map cndetmap=(Map)CNdet.get(i);
								
										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);					
										htMovHis.put("DIRTYPE", TransactionConstants.DELETE_DEBIT_NOTE);
										htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, (String) cndetmap.get("ITEM"));
										String billqty = String.valueOf((String) cndetmap.get("QTY"));
										htMovHis.put(IDBConstants.QTY, billqty);
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
										//htMovHis.put("REMARKS",vendname+","+pono+","+grnnumber+","+billnumber+","+poreturn+","+note);
										htMovHis.put("REMARKS",cnHdrmap.get("VNAME")+","+cnHdrmap.get("PONO")+","+cnHdrmap.get("GRNO")+","+cnHdrmap.get("BILL")+","+cnHdrmap.get("PORETURN")+","+cnHdrmap.get("NOTE"));
										movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}
								
								DbBean.CommitTran(ut);
								result = "Debit Note Deleted successfully.";
								response.sendRedirect("../debit/summary?result="+ result);/* Redirect to Bill Summary page */
								
							}else {
								DbBean.RollbackTran(ut);
								result = "Couldn't Delete the Debit Note.";
								response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
							}
						}else {
							DbBean.RollbackTran(ut);
							result = "Couldn't Delete the Debit Note .";
							response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
						}
						
					}else {
						DbBean.RollbackTran(ut);
						result = "Couldn't Delete the Debit Note .";
						response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
					}
			} catch (Exception e) {
				e.printStackTrace();
				DbBean.RollbackTran(ut);
				result = "Couldn't Delete the Debit Note .";
				response.sendRedirect("../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result);
			}
			
		}
		else if(baction.equalsIgnoreCase("convertToDraft")) {
			String billHdrId = StrUtils.fString(request.getParameter("billHdrId"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			boolean isUpdated = false;
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
			try {
				Hashtable ht = new Hashtable();
				ht.put("ID", billHdrId);
				ht.put("PLANT", plant);
				String query = " SET CREDIT_STATUS='Draft'";
				isUpdated = supplierCreditDAO.updateHdr(query, ht, "");
				
				if(isUpdated) {
					result = "Debit Note drafted successfully.";
					response.sendRedirect("../debit/detail?TRANID="+billHdrId+"&rsuccess="+ result);
				}else {
					result = "Couldn't convert to Draft.";
					response.sendRedirect("../debit/detail?TRANID="+billHdrId+"&resultnew="+ result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = "Couldn't convert to Draft.";
				response.sendRedirect("../debit/detail?TRANID="+billHdrId+"&resultnew="+ result);
			}
			
		}
		
		
		
		
		else if (action.equalsIgnoreCase("GET_BILL_DET")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getbilldet(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if(action.equalsIgnoreCase("Save")) {
			/* CNHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", creditnote = "", pono = "", creditnoteDate = "", payTerms = "",orderdiscount="",currencyid="",currencyuseqt="0",cequ="1",odisctype="",taxid="0",isdisctax="0",isodisctax="0",gst="0",
			itemRates = "", discount = "", discountType = "", discountAccount = "", shippingCost = "", billnumber = "", grnnumber = "",billhdrid = "",taxamount="",poreturnstatus="",poreturn="",
			adjustment = "", adjustmentLabel = "", subTotal = "", totalAmount = "", billStatus = "", note = "",empno="",empName="",purchaseloc = "",invsalesloc="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",vendname="",projectid="",shippingid="",shippingcust="";
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="";
			
			/*CNDet*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),lnno=new ArrayList(),dnqty=new ArrayList(),billingqty=new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(), detDiscounttype = new ArrayList(), taxType = new ArrayList(),
					amount = new ArrayList(), polnno = new ArrayList();			
			List<Hashtable<String,String>> CNDetInfoList = null;
			List<Hashtable<String,String>> CNAttachmentList = null;
			List<Hashtable<String,String>> CNAttachmentInfoList = null;
			Hashtable<String,String> CNDetInfo = null;
			Hashtable<String,String> CNAttachment = null;
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
			Hashtable<String,String> paymentDetInfo = null;
			List<Hashtable<String,String>> paymentDetInfoList = null;
			UserTransaction ut = null;
			SupplierCreditUtil supplierCreditUtil = new SupplierCreditUtil();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			BillDAO billDAO = new BillDAO();
			ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			boolean isAdded = false;
			String result="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0, detDiscountTypeCount  = 0,
					taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, lnnoCount = 0, dnqtyCount = 0, billingqtyCount = 0;
			String polnnoIn = "";
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				CNAttachmentList = new ArrayList<Hashtable<String,String>>();
				CNAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* CreditNoteHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
							vendname = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("creditnote")) {
							creditnote = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billno")) {
							billnumber = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billhdrid")) {
							billhdrid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("grnno")) {
							grnnumber = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_date")) {
							creditnoteDate = StrUtils.fString(fileItem.getString()).trim();
						}					
						
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							payTerms = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
							itemRates = StrUtils.fString(fileItem.getString()).trim();
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
							 * discountVal = Double.parseDouble(discount); if(discountVal > 0) {
							 * discountAccount = StrUtils.fString(fileItem.getString()).trim(); } }
							 */
					
						if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
							//shippingCost = StrUtils.fString(fileItem.getString()).trim();
							shippingCost = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustmentLabel")) {
							adjustmentLabel = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
							//adjustment = StrUtils.fString(fileItem.getString()).trim();
							adjustment = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxTotal")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							billStatus = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
							empName = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
							empno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
							purchaseloc = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("INV_STATE_LOC")) {
							invsalesloc = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
							taxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
							sREVERSECHARGE=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
							sGOODSIMPORT=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
							
						if (fileItem.getFieldName().equalsIgnoreCase("poreturn")) {
							poreturn = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("poreturnstatus")) {
							poreturnstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currencyid=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							cequ=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
							odisctype=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("discounttaxstatus")) {
							isdisctax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
							isodisctax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							gst=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
							shippingid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
							shippingcust=StrUtils.fString(fileItem.getString()).trim();
						}
					}
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/CreditNote" + "/"+ vendno + "/"+ creditnote;
						String filetempLocation = "C:/ATTACHMENTS/CreditNote" + "/temp" + "/"+ vendno + "/"+ creditnote;
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
						CNAttachment = new Hashtable<String, String>();
						CNAttachment.put("PLANT", plant);
						CNAttachment.put("FILETYPE", fileItem.getContentType());
						CNAttachment.put("FILENAME", fileName);
						CNAttachment.put("FILESIZE",  String.valueOf(fileItem.getSize()));
						CNAttachment.put("FILEPATH", fileLocation);
						CNAttachment.put("CRAT",dateutils.getDateTime());
						CNAttachment.put("CRBY",username);
						CNAttachmentList.add(CNAttachment);
					}
					
					/*CNDet*/
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
							detDiscounttype.add(detDiscountTypeCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscountTypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
							lnno.add(lnnoCount, StrUtils.fString(fileItem.getString()).trim());
							lnnoCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("dnqty")) {
							dnqty.add(dnqtyCount, StrUtils.fString(fileItem.getString()).trim());
							dnqtyCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("billqty")) {
							billingqty.add(billingqtyCount, StrUtils.fString(fileItem.getString()).trim());
							billingqtyCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								/*
								 * if(fileItem.getString().equalsIgnoreCase("EXEMPT") ||
								 * fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								 * taxType.add(taxTypeCount,
								 * StrUtils.fString(fileItem.getString()+"[0.0%]").trim()); else
								 */
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
						if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
							polnno.add(polnnoCount, StrUtils.fString(fileItem.getString()).trim());
							polnnoCount++;
						}
					}
					
				}
				}
				
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Float.parseFloat(StrUtils.fString(discount)) / (Float.parseFloat(currencyuseqt))));
				
				if(!odisctype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Float.parseFloat(StrUtils.fString(orderdiscount)) / (Float.parseFloat(currencyuseqt))));
				
				
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
				
				//////////////////////
				CNDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable CNHdr =new Hashtable(); 
				CNHdr.put("PLANT", plant);
				CNHdr.put("VENDNO", vendno);
				CNHdr.put("CREDITNOTE", creditnote);
				CNHdr.put("PONO", pono);
				if(billnumber.equalsIgnoreCase("")) {
					CNHdr.put("BILL", "");
				}else {
					CNHdr.put("BILL", billnumber);
				}
				if(grnnumber.equalsIgnoreCase("")) {
					CNHdr.put("GRNO", "");
				}else {
					CNHdr.put("GRNO", grnnumber);
				}
				if(poreturn.equalsIgnoreCase("")) {
					CNHdr.put("PORETURN", "");
				}else {
					CNHdr.put("PORETURN", poreturn);
				}
				CNHdr.put("CREDIT_DATE", creditnoteDate);
				CNHdr.put("PAYMENT_TERMS", payTerms);
				CNHdr.put("ITEM_RATE", itemRates);
				CNHdr.put("DISCOUNT", discount);
				CNHdr.put("ORDER_DISCOUNT", orderdiscount);
				CNHdr.put("DISCOUNT_TYPE", discountType);
				CNHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				CNHdr.put("SHIPPINGCOST", shippingCost);
				CNHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
				CNHdr.put("ADJUSTMENT", adjustment);
				CNHdr.put("SUB_TOTAL", subTotal);
				CNHdr.put("TOTAL_AMOUNT", totalAmount);
				CNHdr.put("CREDIT_STATUS", billStatus);
				CNHdr.put("NOTE", note);	
				CNHdr.put("EMPNO", empno);	
				CNHdr.put("CRAT",dateutils.getDateTime());
				CNHdr.put("CRBY",username);
				CNHdr.put("UPAT",dateutils.getDateTime());
				CNHdr.put("TAXAMOUNT", taxamount);
				if(purchaseloc.equalsIgnoreCase(""))
					CNHdr.put("PURCHASE_LOCATION", invsalesloc);
				else
					CNHdr.put("PURCHASE_LOCATION", purchaseloc);
				CNHdr.put("TAXTREATMENT",taxtreatment);
				CNHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
				CNHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
				CNHdr.put(IDBConstants.CURRENCYID, currencyid);
				CNHdr.put("CURRENCYUSEQT", cequ);
				CNHdr.put("ORDERDISCOUNTTYPE", odisctype);
				CNHdr.put("TAXID", taxid);
				CNHdr.put("ISDISCOUNTTAX", isdisctax);
				CNHdr.put("ISORDERDISCOUNTTAX", isodisctax);
				CNHdr.put("INBOUND_GST", gst);
				CNHdr.put("PROJECTID", projectid);
				CNHdr.put("SHIPCONTACTNAME",shipcontactname);
				CNHdr.put("SHIPDESGINATION",shipdesgination);
				CNHdr.put("SHIPWORKPHONE",shipworkphone);
				CNHdr.put("SHIPHPNO",shiphpno);
				CNHdr.put("SHIPEMAIL",shipemail);
				CNHdr.put("SHIPCOUNTRY",shipcountry);
				CNHdr.put("SHIPADDR1",shipaddr1);
				CNHdr.put("SHIPADDR2",shipaddr2);
				CNHdr.put("SHIPADDR3",shipaddr3);
				CNHdr.put("SHIPADDR4",shipaddr4);
				CNHdr.put("SHIPSTATE",shipstate);
				CNHdr.put("SHIPZIP",shipzip);
				CNHdr.put("SHIPPINGID", shippingid);
				CNHdr.put("SHIPPINGCUSTOMER", shippingcust);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int CNHdrId = supplierCreditUtil.addSupplierCreditHdr(CNHdr, plant);				
				if(CNHdrId > 0) {
					for(int i =0 ; i < item.size() ; i++){
						int lnnol = i+1;
						String convDiscount=""; 
						String convCost = String.valueOf((Float.parseFloat((String) cost.get(i)) / Float.parseFloat(currencyuseqt)));
						
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Float.parseFloat((String) detDiscount.get(i)) / Float.parseFloat(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						
						CNDetInfo = new Hashtable<String, String>();
						CNDetInfo.put("PLANT", plant);
						CNDetInfo.put("LNNO", Integer.toString(lnnol));
						CNDetInfo.put("HDRID", Integer.toString(CNHdrId));
						CNDetInfo.put("ITEM", (String) item.get(i));
						CNDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						CNDetInfo.put("QTY", (String) qty.get(i));
						CNDetInfo.put("COST", (String) convCost);
						CNDetInfo.put("DISCOUNT", convDiscount);
						CNDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						CNDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						CNDetInfo.put("AMOUNT", convAmount);
						CNDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						CNDetInfo.put("CRAT",dateutils.getDateTime());
						CNDetInfo.put("CRBY",username);
						CNDetInfo.put("UPAT",dateutils.getDateTime());
						CNDetInfoList.add(CNDetInfo);
						if(polnno.size() > 0) {
							if(i < (item.size()-1)) {
								polnnoIn += (String) polnno.get(i)+ ",";
							}else {
								polnnoIn += (String) polnno.get(i);
							}		
						}
					}			
					isAdded = supplierCreditUtil.addMultipleCreditDet(CNDetInfoList, plant);
					
					int attchSize = CNAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						CNAttachment = new Hashtable<String, String>();
						CNAttachment = CNAttachmentList.get(i);
						CNAttachment.put("HDRID", Integer.toString(CNHdrId));
						CNAttachmentInfoList.add(CNAttachment);
					}
					if(isAdded) {
						if(CNAttachmentInfoList.size() > 0)
							isAdded = supplierCreditUtil.addAttachments(CNAttachmentInfoList, plant);
						if(!billStatus.equalsIgnoreCase("Draft"))
						{
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(creditnoteDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("SUPPLIERCREDITNOTES");
						journalHead.setTRANSACTION_ID(Integer.toString(CNHdrId));
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						VendMstDAO vendorDAO=new VendMstDAO();
						for(Map CNDet:CNDetInfoList)
						{
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
							if(!odisctype.toString().equalsIgnoreCase("%")) {
								journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderdiscount)/CNDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderdiscount.toString());
								journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
							}
							
							
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
						JSONObject coaJson1=coaDAO.getCOAByName(plant, vendno);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject vendorJson=vendorDAO.getVendorName(plant, vendno);
							if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
									coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
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
						journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
						journalDetails.add(journalDetail_1);
						}
						Double taxAmountFrom=Double.parseDouble(taxamount);
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Input");*/
							
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
							JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
							journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
							journalDetail_3.setACCOUNT_NAME("Discount Received");
							journalDetail_3.setDEBITS(discountFrom);
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
					}
					
					if(poreturnstatus.equalsIgnoreCase("poreturn")) {
						if(!billnumber.equalsIgnoreCase("")) {
							if(isAdded) {
								String query = " SET CREDITNOTESSTATUS='2' ";
								Hashtable ht = new Hashtable();
								ht.put("BILL", billnumber);
								ht.put("PLANT", plant);
								billDAO.updateBillHdr(query, ht, "");
							}
						}
					}else {
						if(!billnumber.equalsIgnoreCase("")) {
							if(isAdded) {
								boolean billupstatus = true;
								for(int i =0 ; i < item.size() ; i++){
									double debitqty = Double.valueOf((String) dnqty.get(i)) + Double.valueOf((String) qty.get(i));
									double billingqy = Double.valueOf((String) billingqty.get(i));
									if(billingqy != debitqty) {
										billupstatus = false;
									}
									String querydet = " SET DEBITNOTE_QTY='"+debitqty+"' ";
									Hashtable ht = new Hashtable();
									ht.put("BILLHDRID", billhdrid);
									ht.put("LNNO", (String) lnno.get(i));
									ht.put("PLANT", plant);
									billDAO.updateBillDet(querydet, ht, "");
								}
								
								if(billupstatus) {
									String query = " SET CREDITNOTESSTATUS='1' ";
									Hashtable ht = new Hashtable();
									ht.put("ID", billhdrid);
									ht.put("PLANT", plant);
									billDAO.updateBillHdr(query, ht, "");
								}
							}
						}
					}
					
					if(billStatus.equalsIgnoreCase("Open")) {
						//Payment Save
						if(isAdded) {
							paymentDetInfoList = new ArrayList<Hashtable<String,String>>();
						Hashtable paymentHdr =new Hashtable();
						paymentHdr.put("PLANT", plant);
						paymentHdr.put("VENDNO", vendno);
						paymentHdr.put("AMOUNTPAID", totalAmount);
						paymentHdr.put("PAYMENT_DATE", creditnoteDate);
						paymentHdr.put("PAYMENT_MODE", "Cash");
						paymentHdr.put("PAID_THROUGH", "Advance");
						paymentHdr.put("REFERENCE", creditnote);
						paymentHdr.put("BANK_BRANCH", "");
						paymentHdr.put("AMOUNTUFP", "0");
						paymentHdr.put("AMOUNTREFUNDED", "0");
						paymentHdr.put("BANK_CHARGE", "0");
						paymentHdr.put("CHECQUE_NO", "");
						paymentHdr.put("NOTE", "");
						//paymentHdr.put("EMPNO", "");
						//paymentHdr.put("EMP_NAME", "");
						paymentHdr.put("CRAT", dateutils.getDateTime());
						paymentHdr.put("CRBY", username);
						paymentHdr.put("UPAT", dateutils.getDateTime());
						paymentHdr.put("ISPDCPROCESS", "0");
						paymentHdr.put("CURRENCYID", currencyid);
						
						int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
						String payHdrId = Integer.toString(paymentHdrId);
						if(paymentHdrId > 0) {
							
								paymentDetInfo = new Hashtable<String, String>();
								paymentDetInfo.put("PLANT", plant);							
									paymentDetInfo.put("LNNO", "0");
									paymentDetInfo.put("AMOUNT", totalAmount);
									paymentDetInfo.put("BALANCE", totalAmount);													
								//paymentDetInfo.put("PONO", pono);
									paymentDetInfo.put("PONO", "");
								paymentDetInfo.put("PAYHDRID", payHdrId);
								paymentDetInfo.put("BILLHDRID", "0");												
								paymentDetInfo.put("TYPE", "ADVANCE");
								paymentDetInfo.put("ADVANCEFROM", "GENERAL");
								paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								paymentDetInfo.put("CRAT", dateutils.getDateTime());
								paymentDetInfo.put("CRBY", username);
								paymentDetInfo.put("UPAT", dateutils.getDateTime());
								paymentDetInfoList.add(paymentDetInfo);
							
							isAdded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
						}
						
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",creditnote+","+totalAmount);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
						
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_DEBIT_NOTE);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(creditnoteDate));														
						htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
						String billqty = String.valueOf((String) qty.get(i));
						htMovHis.put(IDBConstants.QTY, billqty);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",vendname+","+pono+","+grnnumber+","+billnumber+","+poreturn+","+note);
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
					
					if(isAdded) {
						//Update Credit Note Seq
						if(isAdded) {
						Hashtable htv = new Hashtable();				
		    			htv.put("PLANT", plant);
		    			htv.put("FUNC", "SUPPLIER_CREDIT_NOTE");
		    			isAdded = _TblControlDAO.isExisit(htv, "", plant);
		    			if (isAdded) 
		    				isAdded=_TblControlDAO.updateSeqNo("SUPPLIER_CREDIT_NOTE",plant);
						}
					}
					
					if(isAdded) {
						if(poreturnstatus.equalsIgnoreCase("poreturn")) {
							
							String poquery = "LNNO,ISNULL(RETURN_QTY,'0') AS RETURN_QTY,ISNULL(CREDITED_QTY,'0') AS CREDITED_QTY";
							String poretcondition = "PORETURN = '"+poreturn+"' AND STATUS != 'Applied'";
							List<Map<String, String>> poreturnbypono = returnOrderDAO.getPOReturnDetailsbyVendor(plant, poquery, poretcondition);
							int por = 0;
							for (Map<String, String> map : poreturnbypono) {
								double rqty = Double.valueOf(map.get("RETURN_QTY"));
								double cqty = Double.valueOf(map.get("CREDITED_QTY"));
								double fqty = Double.valueOf((String) qty.get(por)) + cqty;
								
								if(rqty == fqty) {
									String setquery = "STATUS = 'Applied',CREDITED_QTY = '"+fqty+"'";
									String pocondition = "PORETURN = '"+poreturn+"' AND PONO = '"+pono+"' AND GRNO = '"+grnnumber+"' AND LNNO = '"+map.get("LNNO")+"'";
									returnOrderDAO.updatePoReturnDetails(setquery, pocondition, plant);
								}else {
									String setquery = "STATUS = 'Partially Applied',CREDITED_QTY = '"+fqty+"'";
									String pocondition = "PORETURN = '"+poreturn+"' AND PONO = '"+pono+"' AND GRNO = '"+grnnumber+"' AND LNNO = '"+map.get("LNNO")+"'";
									returnOrderDAO.updatePoReturnDetails(setquery, pocondition, plant);
								}
								
								por = por + 1;
							}
							
							
						}
					}
				}
				
				if(isAdded) {
					DbBean.CommitTran(ut);
					result = "Debit Note created successfully!";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Debit Note not created";
				}
				if(result.equalsIgnoreCase("Debit Note not created"))
					response.sendRedirect("../debit/new?result="+ result);
				else
				response.sendRedirect("../debit/summary?result="+ result);/* Redirect to Supplier Credit Summary page */
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 response.sendRedirect("../debit/new?result="+ e.toString());
			}			
		}
		if(action.equalsIgnoreCase("Update")) {
			/* Hdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", creditnote = "", pono = "", creditnoteDate = "", payTerms = "",taxamount="",currencyid="",currencyuseqt="0",odisctype="",taxid="0",isdisctax="0",isodisctax="0",gst="0",
			itemRates = "",orderdiscount="", discount = "", discountType = "", discountAccount = "", shippingCost = "",billnumber = "", grnnumber = "",cstatus = "",projectid = "",shippingid = "",shippingcust = "",
			adjustment = "", adjustmentLabel = "", subTotal = "", totalAmount = "", billStatus = "", note = "",empno="",empName="",billHdrId="",purchaseloc = "",invsalesloc="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",vendname="";
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="";
			
			/*CNDet*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(), taxType = new ArrayList(), detDiscounttype = new ArrayList(),
					amount = new ArrayList(), polnno = new ArrayList();		
			List<Hashtable<String,String>> CNDetInfoList = null;
			List<Hashtable<String,String>> CNAttachmentList = null;
			List<Hashtable<String,String>> CNAttachmentInfoList = null;
			Hashtable<String,String> CNDetInfo = null;
			Hashtable<String,String> CNAttachment = null;
			UserTransaction ut = null;
			SupplierCreditUtil supplierCreditUtil = new SupplierCreditUtil();
			SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
			BillPaymentDAO billPaymentDAO = new BillPaymentDAO();
			DateUtils dateutils = new DateUtils();
			RecvDetDAO recvDao = new RecvDetDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isUpdated = false;
			String result="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
					taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0;
			String polnnoIn = "";
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				CNAttachmentList = new ArrayList<Hashtable<String,String>>();
				CNAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* CreditNoteHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
							vendname = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("creditnote")) {
							creditnote = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_date")) {
							creditnoteDate = StrUtils.fString(fileItem.getString()).trim();
						}					
						
						if (fileItem.getFieldName().equalsIgnoreCase("billno")) {
							billnumber = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("grnno")) {
							grnnumber = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							payTerms = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
							itemRates = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("orderdiscount")) {
							orderdiscount = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
							discount = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
							discountType = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("discount_account")) {
							double discountVal = Double.parseDouble(discount);
							if(discountVal > 0) {
								discountAccount = StrUtils.fString(fileItem.getString()).trim();
							}							
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
							//shippingCost = StrUtils.fString(fileItem.getString()).trim();
							shippingCost = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustmentLabel")) {
							adjustmentLabel = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
							//adjustment = StrUtils.fString(fileItem.getString()).trim();
							adjustment = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxTotal")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							billStatus = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
							empName = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
							empno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
							billHdrId = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
							purchaseloc = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("INV_STATE_LOC")) {
							invsalesloc = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
							taxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
							sREVERSECHARGE=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
							sGOODSIMPORT=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}	
						
						if (fileItem.getFieldName().equalsIgnoreCase("cstatus")) {
							cstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currencyid=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
							odisctype=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("discounttaxstatus")) {
							isdisctax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
							isodisctax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							gst=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
							shippingid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
							shippingcust=StrUtils.fString(fileItem.getString()).trim();
						}
					}
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/CreditNote" + "/"+ vendno + "/"+ creditnote;
						String filetempLocation = "C:/ATTACHMENTS/CreditNote" + "/temp" + "/"+ vendno + "/"+ creditnote;
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
						CNAttachment = new Hashtable<String, String>();
						CNAttachment.put("PLANT", plant);
						CNAttachment.put("FILETYPE", fileItem.getContentType());
						CNAttachment.put("FILENAME", fileName);
						CNAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						CNAttachment.put("FILEPATH", fileLocation);
						CNAttachment.put("UPBY",username);
						CNAttachment.put("UPAT",dateutils.getDateTime());
						CNAttachmentList.add(CNAttachment);
					}
					
					/*CNDet*/
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
							detDiscounttype.add(detDiscountTypeCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscountTypeCount++;
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
						if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
							polnno.add(polnnoCount, StrUtils.fString(fileItem.getString()).trim());
							polnnoCount++;
						}
					}
					
				}
				}
				if (!empName.isEmpty()) {
					if (empno.isEmpty()) {
						ArrayList arrList = new ArrayList();
						EmployeeDAO movHisDAO = new EmployeeDAO();
						Hashtable htData = new Hashtable();
						htData.put("PLANT", plant);
						htData.put("FNAME", empName);
						arrList = movHisDAO.getEmployeeDetails("EMPNO", htData, "");
						Map m = (Map) arrList.get(0);
						empno = (String) m.get("EMPNO");
					}
				}
			
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Float.parseFloat(StrUtils.fString(discount)) / (Float.parseFloat(currencyuseqt))));
				
				//////////////////////
				CNDetInfoList = new ArrayList<Hashtable<String,String>>();
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();

				String query = " SET PONO='"+pono+"',CREDIT_DATE='"+creditnoteDate+"',ITEM_RATE='"+itemRates+"',NOTE='"+note+"',EMPNO='"+empno+"',SUB_TOTAL='"+subTotal+"',SHIPPINGCOST='"+shippingCost+"',BILL='"+billnumber+"',CREDIT_STATUS='"+billStatus+"',"						
						+ " ADJUSTMENT='"+adjustment+"',DISCOUNT='"+discount+"',DISCOUNT_TYPE='"+discountType+"',DISCOUNT_ACCOUNT='"+discountAccount+"',PAYMENT_TERMS='"+payTerms+"',GRNO='"+grnnumber+"',TAXTREATMENT='"+taxtreatment+"', "
						+ " PROJECTID='"+projectid+"', SHIPPINGID='"+shippingid+"', SHIPPINGCUSTOMER='"+shippingcust+"', "
						+ " SHIPCONTACTNAME='"+shipcontactname+"', SHIPDESGINATION='"+shipdesgination+"', SHIPWORKPHONE='"+shipworkphone+"', SHIPHPNO='"+shiphpno+"', "
						+ " SHIPEMAIL='"+shipemail+"', SHIPCOUNTRY='"+shipcountry+"', SHIPADDR1='"+shipaddr1+"', SHIPADDR2='"+shipaddr2+"', "
						+ " SHIPADDR3='"+shipaddr3+"', SHIPADDR4='"+shipaddr4+"', SHIPSTATE='"+shipstate+"', SHIPZIP='"+shipzip+"', "
						+ " CURRENCYUSEQT='"+currencyuseqt+"',ORDERDISCOUNTTYPE='"+odisctype+"',TAXID='"+taxid+"',ISDISCOUNTTAX='"+isdisctax+"',ISORDERDISCOUNTTAX='"+isodisctax+"',INBOUND_GST='"+gst+"', "
						+ " ADJUSTMENT_LABEL='"+adjustmentLabel+"',UPAT= '"+ dateutils.getDateTime() +"',UPBY='"+username+"',TOTAL_AMOUNT='"+totalAmount+"',TAXAMOUNT='"+taxamount+"',PURCHASE_LOCATION='"+purchaseloc+"',REVERSECHARGE='"+sREVERSECHARGE+"', GOODSIMPORT='"+sGOODSIMPORT+"' ";
				Hashtable ht = new Hashtable();
				ht.put("ID", billHdrId);
				ht.put("PLANT", plant);
				isUpdated = supplierCreditDAO.updateHdr(query, ht, "");
				
				//DET Update
				if(!billHdrId.isEmpty()) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						
						String convDiscount=""; 
						String convCost = String.valueOf((Float.parseFloat((String) cost.get(i)) / Float.parseFloat(currencyuseqt)));
						
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Float.parseFloat((String) detDiscount.get(i)) / Float.parseFloat(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						
						CNDetInfo = new Hashtable<String, String>();
						CNDetInfo.put("PLANT", plant);
						CNDetInfo.put("LNNO", Integer.toString(lnno));
						CNDetInfo.put("HDRID", (billHdrId));
						CNDetInfo.put("ITEM", (String) item.get(i));
						CNDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						CNDetInfo.put("QTY", (String) qty.get(i));
						CNDetInfo.put("COST", convCost);
						CNDetInfo.put("DISCOUNT", convDiscount);
						CNDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						CNDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						CNDetInfo.put("AMOUNT", convAmount);
						CNDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						CNDetInfo.put("CRAT",dateutils.getDateTime());
						CNDetInfo.put("CRBY",username);
						CNDetInfo.put("UPAT",dateutils.getDateTime());
						CNDetInfoList.add(CNDetInfo);
						if(polnno.size() > 0) {
							if(i < (item.size()-1)) {
								polnnoIn += (String) polnno.get(i)+ ",";
							}else {
								polnnoIn += (String) polnno.get(i);
							}		
						}
					}		
					CNDetInfo.put("UPBY",username);
					isUpdated = supplierCreditDAO.deleteDet(plant, billHdrId);
					if(isUpdated)
					isUpdated = supplierCreditUtil.addMultipleCreditDet(CNDetInfoList, plant);
					if(!cstatus.equalsIgnoreCase("Draft")) {
					if(billStatus.equalsIgnoreCase("Open")) {
						
					//payment update
						
						
					String payquery =" SET VENDNO='"+vendno+"',AMOUNTPAID='"+totalAmount+"',UPAT= '"+ dateutils.getDateTime() +"',UPBY='"+username+"' ";
					Hashtable htpay = new Hashtable();
					htpay.put("REFERENCE", creditnote);
					htpay.put("PLANT", plant);
					
					Hashtable htpay1 = new Hashtable();
					htpay1.put("[REFERENCE]", creditnote);
					htpay1.put("PLANT", plant);
					List checkpay = billPaymentDAO.getBillPaymentHdrListbyref(htpay1);
					if(checkpay.size() > 0) {
						isUpdated = billPaymentDAO.updatePaymentHdr(payquery, htpay,"");
						
						payquery =" SET CURRENCYUSEQT='"+currencyuseqt+"',AMOUNT='"+totalAmount+"',BALANCE= "+totalAmount+" - (select ISNULL(SUM(B.AMOUNT),0) AMOUNT from "+ plant +"_FINPAYMENTDET B where B.TYPE='REGULAR' AND B.PAYHDRID in (select ID from "+ plant +"_FINPAYMENTHDR where REFERENCE='"+creditnote+"')) "
								+ " ,UPAT= '"+ dateutils.getDateTime() +"',UPBY='"+username+"'";
						htpay = new Hashtable();
						htpay.put("TYPE", "ADVANCE");
						htpay.put("PLANT", plant);
						isUpdated = billPaymentDAO.updatePaymentDet(payquery, htpay," AND PAYHDRID in (select ID from "+ plant +"_FINPAYMENTHDR where REFERENCE='"+creditnote+"')");
						
						//Status Update					
						isUpdated = supplierCreditDAO.updateHdr("SET CREDIT_STATUS= case when (select ISNULL((B.BALANCE),0) AMOUNT from "+plant+"_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID in (select ID from "+plant+"_FINPAYMENTHDR where REFERENCE='"+creditnote+"'))=0 then 'Applied' when (select ISNULL((B.BALANCE),0) AMOUNT from "+plant+"_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID in (select ID from "+plant+"_FINPAYMENTHDR where REFERENCE='"+creditnote+"'))<TOTAL_AMOUNT then 'Partially Applied' else 'Open' end ", ht, "");
						
					}
					}
					}
					
				}
				int attchSize = CNAttachmentList.size();
				for(int i =0 ; i < attchSize ; i++){
					CNAttachment = new Hashtable<String, String>();
					CNAttachment = CNAttachmentList.get(i);
					CNAttachment.put("HDRID", billHdrId);
					CNAttachmentInfoList.add(CNAttachment);
				}
				if(isUpdated) {
					if(CNAttachmentInfoList.size() > 0)
						isUpdated = supplierCreditUtil.addAttachments(CNAttachmentInfoList, plant);
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					//Journal Entry
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(creditnoteDate);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("SUPPLIERCREDITNOTES");
					journalHead.setTRANSACTION_ID(billHdrId);
					journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
					//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					List<JournalDetail> journalDetails=new ArrayList<>();
					CoaDAO coaDAO=new CoaDAO();
					VendMstDAO vendorDAO=new VendMstDAO();
					for(Map CNDet:CNDetInfoList)
					{
						
						JournalDetail journalDetail=new JournalDetail();
						journalDetail.setPLANT(plant);
						JSONObject coaJson=coaDAO.getCOAByName(plant, (String) CNDet.get("ACCOUNT_NAME"));
						System.out.println("Json"+coaJson.toString());
						journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
						journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
						
						if(!odisctype.toString().equalsIgnoreCase("%")) {
							journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderdiscount)/CNDetInfoList.size());
						}else {
							Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderdiscount.toString());
							journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString()) -jodamt);
						}
						
						//journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(orderdiscount)/CNDetInfoList.size());
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
					JSONObject coaJson1=coaDAO.getCOAByName(plant, vendno);
					if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
						JSONObject vendorJson=vendorDAO.getVendorName(plant, vendno);
						if(!vendorJson.isEmpty()) {
							coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
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
					journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
					journalDetails.add(journalDetail_1);
					}
					Double taxAmountFrom=Double.parseDouble(taxamount);
					if(taxAmountFrom>0)
					{
						JournalDetail journalDetail_2=new JournalDetail();
						journalDetail_2.setPLANT(plant);
						/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
						journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
						journalDetail_2.setACCOUNT_NAME("VAT Input");*/
						
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
					
					
					
					Double discountFrom = Double.parseDouble(discount);
					Double orderDiscountFrom=0.00;
					//String orderdiscount="10.00";
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
						JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
						journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
						journalDetail_3.setACCOUNT_NAME("Discount Received");
						journalDetail_3.setDEBITS(discountFrom);
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
				
				if(isUpdated) {
					if(cstatus.equalsIgnoreCase("DRAFT")) {
						
						SupplierCreditUtil SCUtil = new SupplierCreditUtil();
						
						Hashtable<String,String> paymentDetInfo = null;
						List<Hashtable<String,String>> paymentDetInfoList = null;
						BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
						
						Hashtable htcs = new Hashtable();
						htcs.put("ID", billHdrId);
						htcs.put("PLANT", plant);
				    	List billHdrList;
						
							billHdrList = SCUtil.getHdrById(htcs);
							Map billHdr=(Map)billHdrList.get(0);
							
							String querycs = " SET CREDIT_STATUS='Open'";
							Hashtable ht1 = new Hashtable();
							ht1.put("ID", billHdrId);
							ht1.put("PLANT", plant);
							supplierCreditDAO.updateHdr(querycs, ht1, "");
							
							

						paymentDetInfoList = new ArrayList<Hashtable<String,String>>();
						Hashtable paymentHdr =new Hashtable();
						paymentHdr.put("PLANT", plant);
						paymentHdr.put("VENDNO", (String)billHdr.get("VENDNO"));
						paymentHdr.put("AMOUNTPAID", (String)billHdr.get("TOTAL_AMOUNT"));
						paymentHdr.put("PAYMENT_DATE", dateutils.getDate());
						paymentHdr.put("PAYMENT_MODE", "Cash");
						paymentHdr.put("PAID_THROUGH", "Advance");
						paymentHdr.put("REFERENCE", (String)billHdr.get("CREDITNOTE"));
						paymentHdr.put("BANK_BRANCH", "");
						paymentHdr.put("AMOUNTUFP", "0");
						paymentHdr.put("AMOUNTREFUNDED", "0");
						paymentHdr.put("BANK_CHARGE", "0");
						paymentHdr.put("CHECQUE_NO", "");
						paymentHdr.put("NOTE", "");
						paymentHdr.put("CRAT", dateutils.getDateTime());
						paymentHdr.put("CRBY", username);
						paymentHdr.put("UPAT", dateutils.getDateTime());
						paymentHdr.put(IDBConstants.CURRENCYID, currencyid);
						
						int paymentHdrId = billPaymentUtil.addPaymentHdr(paymentHdr, plant);
						String payHdrId = Integer.toString(paymentHdrId);
						if(paymentHdrId > 0) {
							
								paymentDetInfo = new Hashtable<String, String>();
								paymentDetInfo.put("PLANT", plant);							
									paymentDetInfo.put("LNNO", "0");
									paymentDetInfo.put("AMOUNT", (String)billHdr.get("TOTAL_AMOUNT"));
									paymentDetInfo.put("BALANCE", (String)billHdr.get("TOTAL_AMOUNT"));													
								//paymentDetInfo.put("PONO", (String)billHdr.get("PONO"));
								paymentDetInfo.put("PONO", "");
								paymentDetInfo.put("PAYHDRID", payHdrId);
								paymentDetInfo.put("BILLHDRID", "0");												
								paymentDetInfo.put("TYPE", "ADVANCE");
								paymentDetInfo.put("ADVANCEFROM", "GENERAL");
								paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
								paymentDetInfo.put("CRAT", dateutils.getDateTime());
								paymentDetInfo.put("CRBY", username);
								paymentDetInfo.put("UPAT", dateutils.getDateTime());
								paymentDetInfoList.add(paymentDetInfo);
							
							billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
							
							
						}
							
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",creditnote+","+totalAmount);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
				
				
				if(isUpdated) {
					for(int i =0 ; i < item.size() ; i++){
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.EDIT_DEBIT_NOTE);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(creditnoteDate));														
					htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
					String billqty = String.valueOf((String) qty.get(i));
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",vendname+","+pono+","+grnnumber+","+billnumber+","+note);
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, plant);
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_DEBIT_NOTE);
					htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
					htMovChk.put(IDBConstants.QTY, billqty);
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, creditnote);
					isUpdated = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%PURCHASE_CREDIT_NOTE%' ");
					if(!isUpdated)
					isUpdated = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
				if(isUpdated) {
					DbBean.CommitTran(ut);
					result = "Debit Note updated successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Debit Note not created";
				}
				if(result.equalsIgnoreCase("Debit Note not created"))
					response.sendRedirect("'../debit/edit?result="+ result); //imti URL changes
				else
				response.sendRedirect("../debit/summary?result="+ result);/* Redirect to Supplier Credit Summary page */
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 response.sendRedirect("../debit/edit?result="+ e.toString());
			}			
		}
		if(baction.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			SupplierCreditDAO CreditnoteDAO=new SupplierCreditDAO();
			List creditAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				creditAttachment = CreditnoteDAO.getSupplierCrdnoteAttachByPrimId(ht1);
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
			SupplierCreditDAO CreditnoteDAO=new SupplierCreditDAO();
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				CreditnoteDAO.deleteCreditAttachByPrimId(ht1);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}
		
}
	
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

		
		private JSONObject getcreditview(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        SupplierCreditUtil supplierCreditUtil = new SupplierCreditUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        String decimalZeros = "";
	        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
	            decimalZeros += "#";
	        }
	        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="",taxby="";
	         try {
	        
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
	           String  CUSTOMER = StrUtils.fString(request.getParameter("SUPPLIER"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  CURENCY = StrUtils.fString(request.getParameter("CURENCY"));
	           String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
	           //String  BILL = StrUtils.fString(request.getParameter("BILL"));
	           String  CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
	           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
	           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
	           
	           PlantMstDAO plantMstDAO = new PlantMstDAO();
	           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	              
	   
	           Hashtable ht = new Hashtable();
	           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
	           //if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
	           if(StrUtils.fString(CREDITNOTE).length() > 0)       ht.put("CREDITNOTE",CREDITNOTE);
	           if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
	           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
			   movQryList = supplierCreditUtil.getSupplierCreditSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
			   if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double sumprdQty = 0;String lastProduct="",covunitCostValue="0";
	            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	           
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);
	                            String custcode =(String)lineArr.get("custname");
	                            String pono = (String)lineArr.get("pono");   
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
	                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
	                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
	                            if(unitCostVal==0f){
	                            	unitCostValue="0.00";
	                            }else{
	                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), decimal);
	                            
	                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
	                            if(balCostVal==0f){
	                            	balCostValue="0.00";
	                            }else{
	                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            balCostValue =StrUtils.addZeroes(Double.parseDouble(balCostValue), decimal);
	                            covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*Float.parseFloat((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
	                            
	                      
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
	                    	 resultJsonInt.put("creditdate",StrUtils.fString((String)lineArr.get("CREDIT_DATE")));
	                    	 resultJsonInt.put("creditnote",StrUtils.fString((String)lineArr.get("CREDITNOTE")));
	                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("PONO")));
	                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
	                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
	                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("CREDIT_STATUS")));
	                    	 resultJsonInt.put("paymentmode",StrUtils.fString((String)lineArr.get("PAYMENT_TERMS")));
	                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
	                    	 //if(cur==null||cur.isEmpty())
	                    		 //String cur = CURENCY;
	                    	 resultJsonInt.put("amount",unitCostValue);
	                    	 resultJsonInt.put("balancedue",balCostValue);
	                    	 resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue, Integer.valueOf(numberOfDecimal)));
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
		private JSONObject getEditDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        SupplierCreditUtil SCUtil = new SupplierCreditUtil();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			    String TranId = StrUtils.fString(request.getParameter("Id")).trim();
			    
			    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			    String ConvertedUnitCost="",ConvertedAmount="";
			    Hashtable ht=new Hashtable();
			    ht.put("HDRID", TranId);
			    ht.put("PLANT", plant);
			    
			    List listQry = SCUtil.getDetByHdrId(ht);
			    if (listQry.size() > 0) {
			    	for(int i =0; i<listQry.size(); i++) {   
			    		Map m=(Map)listQry.get(i);
						JSONObject resultJsonInt = new JSONObject();
						
						double dCost = Double.parseDouble((String)m.get("COST"));
						ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);					
						
						double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
						ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
						resultJsonInt.put("NOTE", (String)m.get("NOTE"));
						resultJsonInt.put("ATTACHNOTE_COUNT", (String)m.get("ATTACHNOTE_COUNT"));
						
						resultJsonInt.put("DETID", (String)m.get("ID"));
						resultJsonInt.put("LNNO", (String)m.get("LNNO"));
						resultJsonInt.put("ITEM", (String)m.get("ITEM"));
						resultJsonInt.put("QTY", (String)m.get("QTY"));
						resultJsonInt.put("UNITPRICE", ConvertedUnitCost);					
						resultJsonInt.put("ITEM_DISCOUNT", (String)m.get("DISCOUNT"));
						resultJsonInt.put("EMP_NAME", (String)m.get("EMP_NAME"));
						resultJsonInt.put("EMPNO", (String)m.get("EMPNO"));
						resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String)m.get("DISCOUNT_TYPE"));
						resultJsonInt.put("ACCOUNT", (String)m.get("ACCOUNT_NAME"));
						resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
						resultJsonInt.put("AMOUNT", ConvertedAmount);
						resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
						resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
						resultJsonInt.put("ITEMDESC", (String) m.get("ITEMDESC"));
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

		private JSONObject validateCreditnote(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
				String creditnote = StrUtils.fString(request.getParameter("CreditNote")).trim();
		
				Hashtable htValues = new Hashtable();
				htValues.put("A.PLANT", plant);
				htValues.put("CREDITNOTE", creditnote);
				Boolean isAdded  = new SupplierCreditDAO().isExisit(htValues,"");		
				if(isAdded)
				{
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO DEBIT NOTE FOUND!");
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
		
		private JSONObject getbilldet(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        BillDAO billDAO = new BillDAO();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			FinProjectDAO finProjectDAO = new FinProjectDAO();
			
			try {
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			    String TranId = StrUtils.fString(request.getParameter("Id")).trim();
			    
			    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			    String ConvertedUnitCost="",ConvertedAmount="";
			    Hashtable ht=new Hashtable();
			    ht.put("BILLHDRID", TranId);
			    ht.put("PLANT", plant);
			    
			    List listQry = billDAO.getBillDetByHdrId(ht);
			    
			    Hashtable ht1=new Hashtable();
			    ht1.put("ID", TranId);
			    ht1.put("PLANT", plant);
			    List hdrbyidlist  = billDAO.getBillHdrById(ht1);
			    Map hdrbyid =(Map)hdrbyidlist.get(0);
			    
			    int taxintid = Integer.valueOf((String)hdrbyid.get("TAXID"));
			    FinCountryTaxType fintaxtype = new FinCountryTaxType();
				if(taxintid > 0) {
					fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxintid);
				}

			    if (listQry.size() > 0) {
			    	for(int i =0; i<listQry.size(); i++) {   
			    		Map m=(Map)listQry.get(i);
			    		
			    		double qty = Double.valueOf((String)m.get("QTY"));
			    		double rqty = Double.valueOf((String)m.get("DEBITNOTE_QTY"));
			    		
			    		if(qty != rqty) {
			    		
			    			qty = qty - rqty;
							JSONObject resultJsonInt = new JSONObject();
							
							double dCost = Double.parseDouble((String)m.get("COST"));
							ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);					
							
							double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
							ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
							
							resultJsonInt.put("DETID", (String)m.get("ID"));
							resultJsonInt.put("ITEM", (String)m.get("ITEM"));
							resultJsonInt.put("LNNO", (String)m.get("LNNO"));
							resultJsonInt.put("DEBITNOTE_QTY", (String)m.get("DEBITNOTE_QTY"));
							resultJsonInt.put("ACCOUNT_NAME", (String)m.get("ACCOUNT_NAME"));
							resultJsonInt.put("COST", ConvertedUnitCost);
							resultJsonInt.put("QTY", StrUtils.addZeroes(qty, "3"));
							resultJsonInt.put("BILL_QTY", (String)m.get("QTY"));
							resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
							resultJsonInt.put("DISCOUNT", (String)m.get("DISCOUNT"));
							resultJsonInt.put("DISCOUNT_TYPE", (String)m.get("DISCOUNT_TYPE"));
							resultJsonInt.put("ALLDISCOUNT", (String)m.get("ALLDISCOUNT"));
							resultJsonInt.put("ORDER_DISCOUNT", (String)hdrbyid.get("ORDER_DISCOUNT"));
							resultJsonInt.put("AMOUNT", ConvertedAmount);
							
							resultJsonInt.put("ISODTAX", (String)hdrbyid.get("ISORDERDISCOUNTTAX"));
							resultJsonInt.put("ISDTAX", (String)hdrbyid.get("ISDISCOUNTTAX"));
							resultJsonInt.put("ODISCTYPE", (String)hdrbyid.get("ORDERDISCOUNTTYPE"));
							resultJsonInt.put("TAXID",(String)hdrbyid.get("TAXID") );
							resultJsonInt.put("CURRENCYUSEQT", (String)hdrbyid.get("CURRENCYUSEQT"));
							resultJsonInt.put("PTAXTYPE", fintaxtype.getTAXTYPE());
							resultJsonInt.put("PTAXPERCENTAGE", (String)hdrbyid.get("INBOUND_GST"));
							resultJsonInt.put("PTAXISZERO", fintaxtype.getISZERO());
							resultJsonInt.put("PTAXISSHOW", fintaxtype.getSHOWTAX());
							resultJsonInt.put("GST", (String)hdrbyid.get("INBOUND_GST"));
							resultJsonInt.put("ISTAXINCLUSIVE", (String)hdrbyid.get("ITEM_RATES"));
							
							resultJsonInt.put("SHIPPINGID", (String)hdrbyid.get("SHIPPINGID"));
							resultJsonInt.put("SHIPPINGCUSTOMER", (String)hdrbyid.get("SHIPPINGCUSTOMER"));
							String projectid = (String)hdrbyid.get("PROJECTID");
							int pid = Integer.valueOf(projectid);
							if(pid > 0){
								FinProject finProject = finProjectDAO.getFinProjectById(plant, pid);
								resultJsonInt.put("PROJECTID", (String)hdrbyid.get("PROJECTID"));
								resultJsonInt.put("PROJECTNAME", finProject.getPROJECT_NAME());
							}else{
								resultJsonInt.put("PROJECTID", "");
								resultJsonInt.put("PROJECTNAME", "");
							}
							
							String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
		                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));	
												
							jsonArray.add(resultJsonInt);
			    		}
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
		
		private JSONObject getVendorCreditDetails(HttpServletRequest request) {
	        JSONObject resultJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        SupplierCreditUtil supplierCreditUtil = new SupplierCreditUtil();
	        DateUtils _dateUtils = new DateUtils();
	        ArrayList movQryList  = new ArrayList();
	        String decimalZeros = "";
	        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
	            decimalZeros += "#";
	        }
	        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
	       
	        StrUtils strUtils = new StrUtils();
	        String fdate="",tdate="",taxby="";
	         try {
	        
	           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
	           String  CUSTOMER = StrUtils.fString(request.getParameter("SUPPLIER"));
	           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	           String  CURENCY = StrUtils.fString(request.getParameter("CURENCY"));
	           String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
	           //String  BILL = StrUtils.fString(request.getParameter("BILL"));
	           String  CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
	           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
	           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
	           
	           PlantMstDAO plantMstDAO = new PlantMstDAO();
	           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	              
	   
	           Hashtable ht = new Hashtable();
	           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
	           //if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
	           if(StrUtils.fString(CREDITNOTE).length() > 0)       ht.put("CREDITNOTE",CREDITNOTE);
	           if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
	           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
			   movQryList = supplierCreditUtil.getVendorCreditDetails(ht,fdate,tdate,PLANT,CUSTOMER);	
			   if (movQryList.size() > 0) {
	            int iIndex = 0,Index = 0;
	             int irow = 0;
	            double sumprdQty = 0;String lastProduct="";
	            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
	           
	                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);
	                            String custcode =(String)lineArr.get("custname");
	                            String pono = (String)lineArr.get("pono");   
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                   			String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
	                   			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
	                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
	                            if(unitCostVal==0f){
	                            	unitCostValue="0.00";
	                            }else{
	                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
	                            
	                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
	                            if(balCostVal==0f){
	                            	balCostValue="0.00";
	                            }else{
	                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            balCostValue =StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
	                            
	                      
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
	                    	 resultJsonInt.put("creditdate",StrUtils.fString((String)lineArr.get("CREDIT_DATE")));
	                    	 resultJsonInt.put("creditnote",StrUtils.fString((String)lineArr.get("CREDITNOTE")));
	                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("PONO")));
	                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
	                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
	                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("CREDIT_STATUS")));
	                    	 resultJsonInt.put("paymentmode",StrUtils.fString((String)lineArr.get("PAYMENT_TERMS")));
	                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
	                    	 if(cur==null||cur.isEmpty())
	                    		 cur = CURENCY;
	                    	 resultJsonInt.put("amount",unitCostValue);
	                    	 resultJsonInt.put("balancedue",balCostValue);
								/*
								 * resultJsonInt.put("amount",CURENCY+unitCostValue);
								 * resultJsonInt.put("balancedue",CURENCY+balCostValue);
								 */	                         jsonArray.add(resultJsonInt);
	                    	            	

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
		
		
		private JSONObject getporeturnDetails(HttpServletRequest request) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
	        JSONArray jsonArrayErr = new JSONArray();
	        ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			BillDAO billDAO = new BillDAO();
			FinProjectDAO finProjectDAO = new FinProjectDAO();
			try {
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			    String poreturn = StrUtils.fString(request.getParameter("PORETURN")).trim();
			    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
			    String grno = StrUtils.fString(request.getParameter("GRNO")).trim();
			    
			    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			    String ConvertedUnitCost="",ConvertedAmount="";
			    
			    
			    List listQry = returnOrderDAO.getPOReturnDetailsbyprnoandcost(poreturn,pono,grno,plant);
			    if (listQry.size() > 0) {
			    	for(int i =0; i<listQry.size(); i++) {   
			    		Map m=(Map)listQry.get(i);
			    		String status = (String)m.get("STATUS");
			    		if(!status.equalsIgnoreCase("Applied")){
							JSONObject resultJsonInt = new JSONObject();
							
							String discount = "0";
							String discount_type = "";
							String qty = "0";
							String cost = "0";
							double discountvalue = 0.0;
							
							double dCost = Double.parseDouble((String)m.get("UNITCOST"));
							ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);		
							
							double dAmount = Double.parseDouble((String)m.get("RETURN_QTY")) * dCost;
							ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
							
							String famount = ConvertedAmount;
							
							Hashtable ht = new Hashtable();
							ht.put("BILL", (String)m.get("BILL"));
							ht.put("B.ITEM", (String)m.get("ITEM"));
							//ht.put("B.LNNO", (String)m.get("LNNO"));
							String billno = (String)m.get("BILL");
							if(!billno.equalsIgnoreCase("")) {
								String query = "SELECT B.DISCOUNT, ISNULL(B.DISCOUNT_TYPE,'') AS DISCOUNT_TYPE,B.QTY,B.COST FROM "+plant+"_FINBILLHDR A JOIN "+plant+"_FINBILLDET B ON A.ID = B.BILLHDRID WHERE A.PLANT ='"+plant+"' ";
								List billDetList =  billDAO.selectForReport(query, ht, "");
								Map billDet=(Map)billDetList.get(0);
								if(!billDetList.isEmpty()) {
									discount = (String)billDet.get("DISCOUNT");
									discount_type = (String)billDet.get("DISCOUNT_TYPE");
									qty = (String)billDet.get("QTY");
									cost = (String)billDet.get("COST");
									if(discount_type.equalsIgnoreCase("%")) {
										discountvalue = Double.parseDouble(discount);
										famount = String.valueOf(dAmount -((dAmount/100)*discountvalue));
									}else {
										discountvalue = (Double.parseDouble(discount)/Double.parseDouble(qty))*Double.parseDouble((String)m.get("RETURN_QTY"));
										famount = String.valueOf(dAmount - discountvalue);
									}
								
								}
							}
	
							resultJsonInt.put("LNNO", (String)m.get("LNNO"));
							resultJsonInt.put("ITEM", (String)m.get("ITEM"));
							resultJsonInt.put("QTY", (String)m.get("RETURN_QTY"));
							resultJsonInt.put("UNITPRICE", ConvertedUnitCost);
							resultJsonInt.put("AMOUNT", StrUtils.addZeroes(Double.parseDouble(famount), numberOfDecimal));
							resultJsonInt.put("ORDERDISCOUNT", (String)m.get("ORDERDISCOUNT"));
							resultJsonInt.put("BILLDISCOUNT", (String)m.get("BILLDISCOUNT"));
							resultJsonInt.put("BILLDISCOUNTTYPE", (String)m.get("BILLDISCOUNTTYPE"));
							resultJsonInt.put("BILLSUB_TOTAL", (String)m.get("BILLSUB_TOTAL"));
							resultJsonInt.put("BILLLINEDISCOUNT", (String)m.get("BILLLINEDISCOUNT"));
							resultJsonInt.put("ITEM_DISCOUNT", discountvalue);
							resultJsonInt.put("ITEM_DISCOUNT_TYPE", discount_type);
							resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
							resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
							resultJsonInt.put("ORDERDISCOUNTTYPE", (String)m.get("ORDERDISCOUNTTYPE"));
							resultJsonInt.put("ISTAXINCLUSIVE", (String)m.get("ISTAXINCLUSIVE"));
							resultJsonInt.put("ISDISCOUNTTAXINC", (String)m.get("ISDISCOUNTTAXINC"));
							resultJsonInt.put("ISODISCOUNTTAXINC", (String)m.get("ISODISCOUNTTAXINC"));
							resultJsonInt.put("GST", (String)m.get("GST"));
							resultJsonInt.put("EMPNO", (String)m.get("EMPNO"));
							resultJsonInt.put("EMP_NAME", (String)m.get("EMP_NAME"));
							resultJsonInt.put("PAYMENT_TERMS", (String)m.get("PAYMENT_TERMS"));
							resultJsonInt.put("ITEMDESC", (String) m.get("ITEMDESC"));
							resultJsonInt.put("SHIPPINGID", (String)m.get("SHIPPINGID"));
							resultJsonInt.put("SHIPPINGCUSTOMER", (String)m.get("SHIPPINGCUSTOMER"));
							String projectid = (String)m.get("PROJECTID");
							int pid = Integer.valueOf(projectid);
							if(pid > 0){
								FinProject finProject = finProjectDAO.getFinProjectById(plant, pid);
								resultJsonInt.put("PROJECTID", (String)m.get("PROJECTID"));
								resultJsonInt.put("PROJECTNAME", finProject.getPROJECT_NAME());
							}else{
								resultJsonInt.put("PROJECTID", "");
								resultJsonInt.put("PROJECTNAME", "");
							}
							
							String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
		                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
												
							jsonArray.add(resultJsonInt);
			    		}
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
		
		private HSSFWorkbook writeToExcelVendorCreditDetails(HttpServletRequest request, HttpServletResponse response,
				HSSFWorkbook wb) {
			StrUtils strUtils = new StrUtils();
			ExpensesUtil expensesUtil       = new  ExpensesUtil();
	        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	        SupplierCreditUtil supplierCreditUtil = new SupplierCreditUtil();
			DateUtils _dateUtils = new DateUtils();
			ArrayList movQryList = new ArrayList();
			int maxRowsPerSheet = 65535;
			String fdate = "", tdate = "", taxby = "", custcode = "";
			int SheetId =1;
			try {
				String PLANT= StrUtils.fString(request.getParameter("plant"));
		           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
		           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
		           String  CUSTOMER = StrUtils.fString(request.getParameter("vendname"));
		           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
		           String  CURENCY = StrUtils.fString(request.getParameter("CURENCY"));
		           String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
		           //String  BILL = StrUtils.fString(request.getParameter("BILL"));
		           String  CREDITNOTE = StrUtils.fString(request.getParameter("CREDITNOTE"));
		           String  STATUS = StrUtils.fString(request.getParameter("status"));
		           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
		           
		           PlantMstDAO plantMstDAO = new PlantMstDAO();
		           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
		            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
		           String curDate =DateUtils.getDate();
		           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

		           if (FROM_DATE.length()>5)
		            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

		           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
		           if (TO_DATE.length()>5)
		           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		              
		   
		           Hashtable ht = new Hashtable();
		           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
		           //if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
		           if(StrUtils.fString(CREDITNOTE).length() > 0)       ht.put("CREDITNOTE",CREDITNOTE);
		           if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
		           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
				   movQryList = supplierCreditUtil.getVendorCreditDetails(ht,fdate,tdate,PLANT,CUSTOMER);	
		           
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
                   			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            
                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
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
							cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CREDIT_DATE"))));
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CREDITNOTE"))));
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("VNAME"))));
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
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 3000);
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
					cell.setCellValue(new HSSFRichTextString("Supplier Name"));
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