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

import javax.servlet.ServletException;
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
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.FinProject;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
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
@SuppressWarnings({"rawtypes", "unchecked"})
public class ExpensesServlet extends HttpServlet implements IMLogger{
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ExpensesServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.ExpensesServlet_PRINTPLANTMASTERINFO;
	String action = "";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		action = StrUtils.fString(request.getParameter("Submit")).trim();
		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		if (baction.equals("VIEW_EXPENSES_SUMMARY")) {
	    	  
	        jsonObjectResult = this.getexpensesview(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		if (baction.equals("VIEW_POS_EXPENSES_SUMMARY")) {
	    	  
	        jsonObjectResult = this.getposexpensesview(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		if (baction.equals("VIEW_AP_EXPENSES_SUMMARY")) {
	    	  
	        jsonObjectResult = this.getapexpensesview(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		if (action.equalsIgnoreCase("GET_EDIT_EXPENSES_DETAILS")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getEditExpensesDetails(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();				
		}
		
		if (baction.equals("GET_CURRENCY")) {
	    	  
	        jsonObjectResult = this.getcurrency(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		
		if (baction.equals("GET_BASECURRENCY")) {
	    	  
	        jsonObjectResult = this.getBasecurrency(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		
		if (baction.equals("GET_EXPENSEHDRID")) {
	    	  
	        jsonObjectResult = this.getexpensehdrid(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }
		else if (action.equals("Save")) {
	    	  
	    	/* ExpenseHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", shipment = "", pono = "", expenseDate = "", paidThrough = "",cmd = "",tranid = "",taxid="",taxpercentage="",etaxpercentage="",taxamount="",etaxamount="",currencyuseqt="0",chkexpenses="",paidstatus="",
					currency = "", custno = "", ref = "", subTotal = "", totalAmount = "", expenseStatus = "",billable="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",projectid="",exbill="",jdesc="";
			
			/*ExpenseDet*/
			List type = new ArrayList(), note = new ArrayList(), 
					taxType = new ArrayList(), amount = new ArrayList(),DETID= new ArrayList(),isExpTax= new ArrayList();			
			List<Hashtable<String,String>> expenseDetInfoList = null;
			List<Hashtable<String,String>> expenseAttachmentList = null;
			List<Hashtable<String,String>> expenseAttachmentInfoList = null;
			Hashtable<String,String> expenseDetInfo = null;
			Hashtable<String,String> expenseAttachment = null;
			UserTransaction ut = null;
			ExpensesUtil expensesUtil = new ExpensesUtil();
			DateUtils dateutils = new DateUtils();
			boolean isAdded = false;
			String result="";
			int typeCount  = 0, noteCount  = 0,	taxTypeCount  = 0, amountCount  = 0,DETIDCount  = 0,isExpTaxCount = 0;
			ExpensesDAO expensesDAO = new ExpensesDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			POUtil pOUtil = new POUtil();
			CurrencyUtil currencyUtil = new CurrencyUtil();
			
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				expenseAttachmentList = new ArrayList<Hashtable<String,String>>();
				expenseAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* ExpenseHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cmd")) {
							cmd = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("TranId")) {
							tranid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							custno = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("exbillno")) {
							exbill = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_for_PO")) {
							chkexpenses = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_date")) {
							expenseDate = StrUtils.fString(fileItem.getString()).trim();
						}					
					
						if (fileItem.getFieldName().equalsIgnoreCase("shipment")) {
							shipment = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
							paidThrough = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currency = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
							ref = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_tax_amount")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_etax_amount")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							etaxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							expenseStatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billable_status")) {
							billable = StrUtils.fString(fileItem.getString()).trim();
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							taxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EXGST")) {
							etaxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
					}
					
					if(tranid.isEmpty())
					{
						tranid=expensesUtil.GetHDRMAXId(plant);
						int expid=Integer.parseInt(tranid) +1;
						tranid=String.valueOf(expid);
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Expenses" + "/"+ vendno + "/"+ tranid;
						String filetempLocation = "C:/ATTACHMENTS/Expenses" + "/temp" + "/"+ vendno + "/"+ tranid;
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "\\" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "\\"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment.put("PLANT", plant);
						expenseAttachment.put("FILETYPE", fileItem.getContentType());
						expenseAttachment.put("FILENAME", fileName);
						expenseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						expenseAttachment.put("FILEPATH", fileLocation);
						expenseAttachment.put("CRAT",dateutils.getDateTime());
						expenseAttachment.put("CRBY",username);
						expenseAttachment.put("UPAT",dateutils.getDateTime());
						expenseAttachmentList.add(expenseAttachment);
					}
					
					/*ExpenseDet*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_account_name")) {
							type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
							typeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note.add(noteCount,StrUtils.fString(fileItem.getString()).trim());
							noteCount++;
						}
					}
					
					/*if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax_type")) {
							if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
							else
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}*/
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("isexptax")) {
								isExpTax.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
								isExpTaxCount++;
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
							
				if(!paidThrough.equals("")) {
					paidstatus="1";
				}else {
					paidstatus="0";
				}
				CoaDAO coaDAO=new CoaDAO();
				VendMstDAO vendorDAO=new VendMstDAO();
				PoDetDAO podetDAO=new PoDetDAO();
				ItemMstDAO itemMSTDAO=new ItemMstDAO();
				//////////////////////
				expenseDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable expenseHdr =new Hashtable(); 
				expenseHdr.put("PLANT", plant);
				expenseHdr.put("VENDNO", vendno);
				expenseHdr.put("CUSTNO", custno);
				expenseHdr.put("PONO", pono);
				expenseHdr.put("SHIPMENT_CODE", shipment);
				expenseHdr.put("EXPENSES_DATE", expenseDate);
				expenseHdr.put("CURRENCYID", currency);
				expenseHdr.put("PAID_THROUGH", paidThrough);				
				expenseHdr.put("REFERENCE", ref);				
				expenseHdr.put("SUB_TOTAL", subTotal);
				expenseHdr.put("TOTAL_AMOUNT", totalAmount);
				expenseHdr.put("STATUS", expenseStatus);	
				expenseHdr.put("CRAT",dateutils.getDateTime());
				expenseHdr.put("CRBY",username);
				expenseHdr.put("UPAT",dateutils.getDateTime());
				expenseHdr.put("ISBILLABLE",billable);
				expenseHdr.put("TAXTREATMENT",taxtreatment);
				expenseHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
				expenseHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
				expenseHdr.put("TAXAMOUNT", taxamount);
				expenseHdr.put("EXPENSETAXAMOUNT", etaxamount);
				expenseHdr.put("ISPAID", paidstatus);
				expenseHdr.put("TAXID", taxid);
				expenseHdr.put("STANDARDTAX", taxpercentage);
				expenseHdr.put("EXPENSETAX", etaxpercentage);
				expenseHdr.put("PROJECTID", projectid);
				expenseHdr.put("EXBILL", exbill);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();
				
				
				/*Begin Transaction*/
				ut.begin();
				int expenseHdrId=0;
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!tranid.isEmpty())
					{
						expenseHdr.put("ID",tranid);
						expenseHdr.put("UPBY",username);
						expenseHdrId = expensesUtil.updateExpensesHdr(expenseHdr);
					}
					
				}
				else {
					
				 expenseHdrId = expensesUtil.addexpenseHdr(expenseHdr, plant);	
				 
				}
				if(expenseHdrId > 0) {
					for(int i =0 ; i < type.size() ; i++){
						
						Hashtable ht1 = new Hashtable();
						ht1.put("CURRENCYID", currency);
						ht1.put("PLANT", plant);
						
						String curtobase = currencyUtil.getCurrencyID(ht1, "CURRENCYUSEQT");
						
						curtobase=currencyuseqt;
						
						String basetopocur="";
						if(!pono.isEmpty()) {
							String cuid = pOUtil.getCurrencyID(plant, pono);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("CURRENCYID", cuid);
							ht2.put("PLANT", plant);
							
							basetopocur = currencyUtil.getCurrencyID(ht2, "CURRENCYUSEQT");
						}else {
							basetopocur = curtobase;
						}
						
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						
				
//						int lnno = i+1;
						expenseDetInfo = new Hashtable<String, String>();
						expenseDetInfo.put("PLANT", plant);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseDetInfo.put("EXPENSESHDRID", tranid);						
						else
						expenseDetInfo.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseDetInfo.put("EXPENSES_ACCOUNT", (String) type.get(i));
						expenseDetInfo.put("DESCRIPTION", (String) note.get(i));
						expenseDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						expenseDetInfo.put("ISEXPENSEGST", (String) isExpTax.get(i));
						expenseDetInfo.put("AMOUNT", convAmount);
						expenseDetInfo.put("CRAT",dateutils.getDateTime());
						expenseDetInfo.put("CRBY",username);
						expenseDetInfo.put("UPAT",dateutils.getDateTime());
						expenseDetInfo.put("CURRENCYTOBASE", curtobase);
						expenseDetInfo.put("BASETOORDERCURRENCY", basetopocur);
						expenseDetInfoList.add(expenseDetInfo);
					}
					if(cmd.equalsIgnoreCase("Edit"))
					{
						if(!tranid.isEmpty())
						{
							expenseDetInfo.put("UPBY",username);
							isAdded = expensesDAO.deleteExpenseDet(plant, tranid);
							if(isAdded)
							isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
						}
					}
					else					
					isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
					
					int attchSize = expenseAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment = expenseAttachmentList.get(i);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseAttachment.put("EXPENSESHDRID", tranid);
						else
						expenseAttachment.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseAttachmentInfoList.add(expenseAttachment);
					}
					if(isAdded) {
						if(expenseAttachmentInfoList.size() > 0)
							isAdded = expensesUtil.addExpenseAttachments(expenseAttachmentInfoList, plant);
						
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(expenseDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						if("1".equals(chkexpenses) && !chkexpenses.equals("")) {
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+pono+")");
						}else {
							journalHead.setTRANSACTION_TYPE("EXPENSE");
						}						
						//System.out.println("Command:"+cmd);
						if(cmd.equalsIgnoreCase("Edit"))
						{
							if(!tranid.isEmpty())
							{
								journalHead.setTRANSACTION_ID(tranid);
							}
						}
						else
						{
							journalHead.setTRANSACTION_ID(Integer.toString(expenseHdrId));
						}
						
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						
						
						if(vendno!=null && !vendno.equals("") && (paidThrough!=null && paidThrough.equals("") || !paidThrough.equals(""))) {
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
									coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
									if(coaJson1.isEmpty() || coaJson1.isNullObject())
									{
										coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
								
								}
							}
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								journalDetails.add(journalDetail);
							}
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_1.setDESCRIPTION(jdesc+"-"+exbill);								
								journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_1);
							}
							
							
							
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
						
						//boolean deleteJournal=journalService.DeleteJournal(plant, tranid);
						//System.out.println("Deleting Journal Details :"+deleteJournal);
						//Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID());
						Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(), journalHead.getTRANSACTION_TYPE());
						
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
						
						if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
									coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
									if(coaJson1.isEmpty() || coaJson1.isNullObject())
									{
										coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
								
								}
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								
							}else {
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								paid_journaldetails.add(journalDetail_3);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setDESCRIPTION(jdesc+"-"+exbill);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						else if(vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								paid_journaldetails.add(journalDetail);
							}
							
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
								paid_journaldetails.add(journalDetail_2);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setDESCRIPTION(jdesc+"-"+exbill);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						
						/*if("1".equals(chkexpenses) && !chkexpenses.equals("")) {
							
							List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
							JournalDetail inventorySelected=new JournalDetail();
							inventorySelected.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
							inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							inventorySelected.setACCOUNT_NAME("Inventory Asset");
							inventorySelected.setDEBITS(Double.parseDouble(subTotal));
							expensepo_journaldetails.add(inventorySelected);
							
							for(Map expDet:expenseDetInfoList)
							{
								JournalDetail expensesSelected=new JournalDetail();
								expensesSelected.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								expensesSelected.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setCREDITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								expensepo_journaldetails.add(expensesSelected);
							}
							journal.setJournalDetails(expensepo_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO");
							Journal journal_expense_po=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journal_expense_po.getJournalHeader()!=null)
							{
								if(journal_expense_po.getJournalHeader().getID()>0)
								{
									journalHead.setID(journal_expense_po.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
								}
								else
								{
									journalService.addJournal(journal, username);
									
									
								}
						
							}
							
						}*/
						
						
					}				
					
					
				}
				if(isAdded) {
					Hashtable htMovHis = new Hashtable();
					Hashtable htmovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					if(cmd.equalsIgnoreCase("Edit")) {
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
						htMovHis.put("RECID",tranid);
					}
					else {
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES);
					htMovHis.put("RECID",expenseHdrId);
					}
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));														
					htMovHis.put(IDBConstants.ITEM, paidThrough);
//					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS", shipment);
				
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, plant);
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
					htMovChk.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));
					htMovChk.put(IDBConstants.ITEM, paidThrough);					
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htMovChk.put("RECID",Integer.toString(expenseHdrId));
					isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%EXPENSES%' ");
					if(!isAdded)
					isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					for(Map expDet:expenseDetInfoList)
					{
						htmovHis.clear();
						htmovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit")) {
							htmovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES_UPD);
							htmovHis.put("RECID",tranid);
						} else {
						htmovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES_ADD);
						htmovHis.put("RECID",expenseHdrId);
						}
						htmovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));	
//						htmovHis.put(IDBConstants.ITEM,expDet.get("EXPENSES_ACCOUNT") );
						htmovHis.put(IDBConstants.ITEM,expDet.get("EXPENSES_ACCOUNT"));
//						htmovHis.put("RECID", "");
						htmovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htmovHis.put(IDBConstants.CREATED_BY, username);
						htmovHis.put(IDBConstants.REMARKS, paidThrough 	);
						htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	
						isAdded = movHisDao.insertIntoMovHis(htmovHis);
					}
				
					
				}
				if(isAdded) {
					DbBean.CommitTran(ut);

					if(cmd.equalsIgnoreCase("Edit"))
						result = "Expenses updated successfully";
					else
					result = "Expenses created successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Expenses not created";
				}
				if(result.equalsIgnoreCase("Expenses not created"))
					response.sendRedirect("../expenses/new?result="+ result);
				else
				response.sendRedirect("../expenses/summary?result="+ result);/* Redirect to Expenses Summary page */
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 response.sendRedirect("../expenses/new?result="+ e.toString());
			}
	    }
		
		if(action.equals("APSave")) {

	    	  
	    	/* ExpenseHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", shipment = "", pono = "", expenseDate = "", paidThrough = "",cmd = "",tranid = "",taxid="",taxpercentage="",etaxpercentage="",taxamount="",etaxamount="",currencyuseqt="0",chkexpenses="",paidstatus="",
					currency = "", custno = "", ref = "", subTotal = "", totalAmount = "", expenseStatus = "",billable="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",projectid="",exbill="",outlet="",terminal="",jdesc="";
			
			/*ExpenseDet*/
			List type = new ArrayList(), note = new ArrayList(), 
					taxType = new ArrayList(), amount = new ArrayList(),DETID= new ArrayList(),isExpTax= new ArrayList();			
			List<Hashtable<String,String>> expenseDetInfoList = null;
			List<Hashtable<String,String>> expenseAttachmentList = null;
			List<Hashtable<String,String>> expenseAttachmentInfoList = null;
			Hashtable<String,String> expenseDetInfo = null;
			Hashtable<String,String> expenseAttachment = null;
			UserTransaction ut = null;
			ExpensesUtil expensesUtil = new ExpensesUtil();
			DateUtils dateutils = new DateUtils();
			boolean isAdded = false;
			String result="";
			int typeCount  = 0, noteCount  = 0,	taxTypeCount  = 0, amountCount  = 0,DETIDCount  = 0,isExpTaxCount = 0;
			ExpensesDAO expensesDAO = new ExpensesDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			POUtil pOUtil = new POUtil();
			CurrencyUtil currencyUtil = new CurrencyUtil();
			
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				expenseAttachmentList = new ArrayList<Hashtable<String,String>>();
				expenseAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* ExpenseHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cmd")) {
							cmd = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("TranId")) {
							tranid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
							custno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("OUTCODE")) {
							outlet = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("TERMINALCODE")) {
							terminal = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("exbillno")) {
							exbill = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_for_PO")) {
							chkexpenses = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_date")) {
							expenseDate = StrUtils.fString(fileItem.getString()).trim();
						}					
					
						if (fileItem.getFieldName().equalsIgnoreCase("shipment")) {
							shipment = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
							paidThrough = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currency = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("reference")) {
							ref = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_tax_amount")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_etax_amount")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							etaxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							expenseStatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billable_status")) {
							billable = StrUtils.fString(fileItem.getString()).trim();
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							taxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EXGST")) {
							etaxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
					}
					
					if(tranid.isEmpty())
					{
						tranid=expensesUtil.GetHDRMAXId(plant);
						int expid=Integer.parseInt(tranid) +1;
						tranid=String.valueOf(expid);
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Expenses" + "/"+ vendno + "/"+ tranid;
						String filetempLocation = "C:/ATTACHMENTS/Expenses" + "/temp" + "/"+ vendno + "/"+ tranid;
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "\\" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "\\"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment.put("PLANT", plant);
						expenseAttachment.put("FILETYPE", fileItem.getContentType());
						expenseAttachment.put("FILENAME", fileName);
						expenseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						expenseAttachment.put("FILEPATH", fileLocation);
						expenseAttachment.put("CRAT",dateutils.getDateTime());
						expenseAttachment.put("CRBY",username);
						expenseAttachment.put("UPAT",dateutils.getDateTime());
						expenseAttachmentList.add(expenseAttachment);
					}
					
					/*ExpenseDet*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_account_name")) {
							type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
							typeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note.add(noteCount,StrUtils.fString(fileItem.getString()).trim());
							noteCount++;
						}
					}
					
					/*if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax_type")) {
							if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
							else
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}*/
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("isexptax")) {
								isExpTax.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
								isExpTaxCount++;
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
							
				if(!paidThrough.equals("")) {
					paidstatus="1";
				}else {
					paidstatus="0";
				}
				CoaDAO coaDAO=new CoaDAO();
				VendMstDAO vendorDAO=new VendMstDAO();
				PoDetDAO podetDAO=new PoDetDAO();
				ItemMstDAO itemMSTDAO=new ItemMstDAO();
				//////////////////////
				expenseDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable expenseHdr =new Hashtable(); 
				expenseHdr.put("PLANT", plant);
				expenseHdr.put("VENDNO", vendno);
				expenseHdr.put("CUSTNO", custno);
				expenseHdr.put("PONO", pono);
				expenseHdr.put("SHIPMENT_CODE", shipment);
				expenseHdr.put("EXPENSES_DATE", expenseDate);
				expenseHdr.put("CURRENCYID", currency);
				expenseHdr.put("PAID_THROUGH", paidThrough);				
				expenseHdr.put("REFERENCE", ref);				
				expenseHdr.put("SUB_TOTAL", subTotal);
				expenseHdr.put("TOTAL_AMOUNT", totalAmount);
				expenseHdr.put("STATUS", expenseStatus);	
				expenseHdr.put("CRAT",dateutils.getDateTime());
				expenseHdr.put("CRBY",username);
				expenseHdr.put("UPAT",dateutils.getDateTime());
				expenseHdr.put("ISBILLABLE",billable);
				expenseHdr.put("TAXTREATMENT",taxtreatment);
				expenseHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
				expenseHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
				expenseHdr.put("TAXAMOUNT", taxamount);
				expenseHdr.put("EXPENSETAXAMOUNT", etaxamount);
				expenseHdr.put("ISPAID", paidstatus);
				expenseHdr.put("TAXID", taxid);
				expenseHdr.put("STANDARDTAX", taxpercentage);
				expenseHdr.put("EXPENSETAX", etaxpercentage);
				expenseHdr.put("PROJECTID", projectid);
				expenseHdr.put("EXBILL", exbill);
				expenseHdr.put("OUTLET", outlet);
				expenseHdr.put("TERMINAL", terminal);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();
				
				
				/*Begin Transaction*/
				ut.begin();
				int expenseHdrId=0;
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!tranid.isEmpty())
					{
						expenseHdr.put("ID",tranid);
						expenseHdr.put("UPBY",username);
						expenseHdrId = expensesUtil.updateExpensesHdr(expenseHdr);
					}
					
				}
				else {
					
				 expenseHdrId = expensesUtil.addexpenseHdr(expenseHdr, plant);	
				 
				}
				if(expenseHdrId > 0) {
					for(int i =0 ; i < type.size() ; i++){
						
						Hashtable ht1 = new Hashtable();
						ht1.put("CURRENCYID", currency);
						ht1.put("PLANT", plant);
						
						String curtobase = currencyUtil.getCurrencyID(ht1, "CURRENCYUSEQT");
						
						curtobase=currencyuseqt;
						
						String basetopocur="";
						if(!pono.isEmpty()) {
							String cuid = pOUtil.getCurrencyID(plant, pono);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("CURRENCYID", cuid);
							ht2.put("PLANT", plant);
							
							basetopocur = currencyUtil.getCurrencyID(ht2, "CURRENCYUSEQT");
						}else {
							basetopocur = curtobase;
						}
						
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						
				
//						int lnno = i+1;
						expenseDetInfo = new Hashtable<String, String>();
						expenseDetInfo.put("PLANT", plant);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseDetInfo.put("EXPENSESHDRID", tranid);						
						else
						expenseDetInfo.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseDetInfo.put("EXPENSES_ACCOUNT", (String) type.get(i));
						expenseDetInfo.put("DESCRIPTION", (String) note.get(i));
						expenseDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						expenseDetInfo.put("ISEXPENSEGST", (String) isExpTax.get(i));
						expenseDetInfo.put("AMOUNT", convAmount);
						expenseDetInfo.put("CRAT",dateutils.getDateTime());
						expenseDetInfo.put("CRBY",username);
						expenseDetInfo.put("UPAT",dateutils.getDateTime());
						expenseDetInfo.put("CURRENCYTOBASE", curtobase);
						expenseDetInfo.put("BASETOORDERCURRENCY", basetopocur);
						expenseDetInfoList.add(expenseDetInfo);
					}
					if(cmd.equalsIgnoreCase("Edit"))
					{
						if(!tranid.isEmpty())
						{
							expenseDetInfo.put("UPBY",username);
							isAdded = expensesDAO.deleteExpenseDet(plant, tranid);
							if(isAdded)
							isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
						}
					}
					else					
					isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
					
					int attchSize = expenseAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment = expenseAttachmentList.get(i);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseAttachment.put("EXPENSESHDRID", tranid);
						else
						expenseAttachment.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseAttachmentInfoList.add(expenseAttachment);
					}
					if(isAdded) {
						if(expenseAttachmentInfoList.size() > 0)
							isAdded = expensesUtil.addExpenseAttachments(expenseAttachmentInfoList, plant);
						
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(expenseDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						if("1".equals(chkexpenses) && !chkexpenses.equals("")) {
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+pono+")");
						}else {
							journalHead.setTRANSACTION_TYPE("EXPENSE");
						}						
						//System.out.println("Command:"+cmd);
						if(cmd.equalsIgnoreCase("Edit"))
						{
							if(!tranid.isEmpty())
							{
								journalHead.setTRANSACTION_ID(tranid);
							}
						}
						else
						{
							journalHead.setTRANSACTION_ID(Integer.toString(expenseHdrId));
						}
						
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						
						
						if(vendno!=null && !vendno.equals("") && (paidThrough!=null && paidThrough.equals("") || !paidThrough.equals(""))) {
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
									coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
									if(coaJson1.isEmpty() || coaJson1.isNullObject())
									{
										coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
								
								}
							}
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								journalDetails.add(journalDetail);
							}
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
								journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_1.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_1);
							}
							
							
							
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
							
							Double etaxAmountFrom=Double.parseDouble(etaxamount);
							if(etaxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
								journalDetail_2.setDEBITS(etaxAmountFrom);
								journalDetails.add(journalDetail_2);
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
						/*
						 * boolean deleteJournal=journalService.DeleteJournal(plant, tranid);
						 * System.out.println("Deleting Journal Details :"+deleteJournal); Journal
						 * journalFrom=journalService.getJournalByTransactionId(plant,
						 * journalHead.getTRANSACTION_ID());
						 */
						
						Journal journalFrom=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(), journalHead.getTRANSACTION_TYPE());
						
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
						
						if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
									coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
									if(coaJson1.isEmpty() || coaJson1.isNullObject())
									{
										coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
									}
								
								}
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								
							}else {
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								paid_journaldetails.add(journalDetail_3);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setDESCRIPTION(jdesc+"-"+exbill);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						else if(vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDESCRIPTION(jdesc+"-"+exbill);
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								paid_journaldetails.add(journalDetail);
							}
							
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
								paid_journaldetails.add(journalDetail_2);
							}
							
							Double etaxAmountFrom=Double.parseDouble(etaxamount);
							if(etaxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+exbill);
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
								
								journalDetail_2.setDEBITS(etaxAmountFrom);
								paid_journaldetails.add(journalDetail_2);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setDESCRIPTION(jdesc+"-"+exbill);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						
						/*if("1".equals(chkexpenses) && !chkexpenses.equals("")) {
							
							List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
							JournalDetail inventorySelected=new JournalDetail();
							inventorySelected.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
							inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							inventorySelected.setACCOUNT_NAME("Inventory Asset");
							inventorySelected.setDEBITS(Double.parseDouble(subTotal));
							expensepo_journaldetails.add(inventorySelected);
							
							for(Map expDet:expenseDetInfoList)
							{
								JournalDetail expensesSelected=new JournalDetail();
								expensesSelected.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								expensesSelected.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setCREDITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								expensepo_journaldetails.add(expensesSelected);
							}
							journal.setJournalDetails(expensepo_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO");
							Journal journal_expense_po=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journal_expense_po.getJournalHeader()!=null)
							{
								if(journal_expense_po.getJournalHeader().getID()>0)
								{
									journalHead.setID(journal_expense_po.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
								}
								else
								{
									journalService.addJournal(journal, username);
									
									
								}
						
							}
							
						}*/
						
						
					}				
					
					
				}
				if(isAdded) {
					Hashtable htMovHis = new Hashtable();
					Hashtable htmovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					if(cmd.equalsIgnoreCase("Edit")) {
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
						htMovHis.put("RECID",tranid);
					}
					else {
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES);
					htMovHis.put("RECID",expenseHdrId);
					}
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));														
					htMovHis.put(IDBConstants.ITEM, paidThrough);
//					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS", shipment);
				
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, plant);
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
					htMovChk.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));
					htMovChk.put(IDBConstants.ITEM, paidThrough);					
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htMovChk.put("RECID",Integer.toString(expenseHdrId));
					isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%EXPENSES%' ");
					if(!isAdded)
					isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					for(Map expDet:expenseDetInfoList)
					{
						htmovHis.clear();
						htmovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit")) {
							htmovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES_UPD);
							htmovHis.put("RECID",tranid);
						} else {
						htmovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES_ADD);
						htmovHis.put("RECID",expenseHdrId);
						}
						htmovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));	
//						htmovHis.put(IDBConstants.ITEM,expDet.get("EXPENSES_ACCOUNT") );
						htmovHis.put(IDBConstants.ITEM,expDet.get("EXPENSES_ACCOUNT"));
//						htmovHis.put("RECID", "");
						htmovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htmovHis.put(IDBConstants.CREATED_BY, username);
						htmovHis.put(IDBConstants.REMARKS, paidThrough 	);
						htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	
						isAdded = movHisDao.insertIntoMovHis(htmovHis);
					}
				
					
				}
				if(isAdded) {
					DbBean.CommitTran(ut);

					if(cmd.equalsIgnoreCase("Edit"))
						result = "Expenses updated successfully";
					else
					result = "Expenses created successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Expenses not created";
				}
				if(result.equalsIgnoreCase("Expenses not created"))
					response.sendRedirect("../expenses/apnew?result="+ result);
				else
				response.sendRedirect("../expenses/apsummary?result="+ result);/* Redirect to Expenses Summary page */
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 response.sendRedirect("../expenses/apnew?result="+ e.toString());
			}
	    
		}
		
		if(baction.equalsIgnoreCase("deleteexpense"))
		{
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
            String pono = StrUtils.fString(request.getParameter("ORDERNO"));
            String shipmentcode = StrUtils.fString(request.getParameter("SHIPMENT_CODE"));
            String expid = StrUtils.fString(request.getParameter("ID"));
            String status = StrUtils.fString(request.getParameter("STATUS"));
            String taxstatus = StrUtils.fString(request.getParameter("TAXSTATUS"));
            UserTransaction ut = null;
            BillDAO bildao = new BillDAO();
            DateUtils dateutils = new DateUtils();
            ExpensesUtil expensesUtil = new ExpensesUtil();
            MovHisDAO movHisDao = new MovHisDAO();
            boolean isUpdated = false;
            boolean mesflag = false;
            String result="";
           
            try {
            	/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
            	if(status.equalsIgnoreCase("BILLED")) {
            		DbBean.CommitTran(ut);
            		result = "Couldn't delete the Expense.";
 					response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
            	}else if(taxstatus.equalsIgnoreCase("Tax Generated")){
            		result = "Couldn't delete the Expense.";
 					response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
				}else {
					if(!pono.equalsIgnoreCase("")) {
            		Hashtable Shipment =new Hashtable(); 
	                 Shipment.put("PLANT", plant);
	                 Shipment.put("PONO", pono);
	                 Shipment.put("SHIPMENT_CODE", shipmentcode);
	                 mesflag = bildao.isShipmentcode(Shipment, "");
					}
	                 if(!mesflag) {
	                	 
	                	 Hashtable ht2 = new Hashtable();
							ht2.put("ID", expid);
							List expdet = expensesUtil.getEditIExpensesDetails(ht2, plant);
							
	                	 isUpdated = expensesUtil.deleteexpense(plant, expid);
							if(isUpdated) {
								JournalService journalService=new JournalEntry();
								Journal journalFrom=journalService.getJournalByTransactionId(plant, expid,"EXPENSE");
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
									journalFrom=journalService.getJournalByTransactionId(plant, expid,"EXPENSE");
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
									for(int i =0 ; i < expdet.size() ; i++){
										Map expdetmap=(Map)expdet.get(i);
										

										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.DELETE_EXPENSES);
										htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd((String)expdetmap.get("EXPENSES_DATE")));														
										htMovHis.put(IDBConstants.ITEM,expdetmap.get("EXPENSES_ACCOUNT") );
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
										htMovHis.put("REMARKS", shipmentcode);
										
										Hashtable htMovChk = new Hashtable();
										htMovChk.clear();
										htMovChk.put(IDBConstants.PLANT, plant);
										htMovChk.put("DIRTYPE", TransactionConstants.DELETE_EXPENSES);
										htMovChk.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd((String)expdetmap.get("EXPENSES_DATE")));
										htMovChk.put(IDBConstants.ITEM, expdetmap.get("EXPENSES_ACCOUNT"));					
										htMovChk.put(IDBConstants.MOVHIS_ORDNUM, pono);
										boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%EXPENSES%' ");
										if(!isAdded)
										isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
									}
									
									DbBean.CommitTran(ut);
									result = "Expense Deleted successfully.";
				                	response.sendRedirect("../expenses/summary?result="+ result);
								}else {
									DbBean.RollbackTran(ut);
									result = "Couldn't delete the Expense.";
				 					response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
								}
							}else {
								DbBean.RollbackTran(ut);
								result = "Couldn't delete the Expense.";
			 					response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
							}
	                 }else {
	                	 DbBean.RollbackTran(ut);
	                	 result = "Couldn't delete the Expense.";
	 					response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
	                 }
            	}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				result = "Couldn't delete the Expense.";
				response.sendRedirect("../expenses/detail?TRANID="+expid+"&resultnew="+ result);
			}
            
            
           
		}
		
		if(baction.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			ExpensesDAO expenseDAO=new ExpensesDAO();
			List Attachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				Attachment = expenseDAO.getExpenseAttachByPrimId(ht1);
				Map expenseAttach=(Map)Attachment.get(0);
				String filePath=(String) expenseAttach.get("FilePath");
				String fileType=(String) expenseAttach.get("FileType");
				String fileName=(String) expenseAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
		}
		else if(baction.equalsIgnoreCase("removeAttachmentById"))
		{
			System.out.println("Remove Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("removeid"));
			ExpensesDAO expenseDAO=new ExpensesDAO();
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				expenseDAO.deleteExpenseAttachByPrimId(ht1);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}else if (baction.equals("VIEW_EXPENSES_DETAILS")) {
	    	  
	        jsonObjectResult = this.getexpensesDetailView(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }else if (baction.equals("VIEW_EXPENSES_BY_CATEGORY")) {
	    	  
	        jsonObjectResult = this.getexpensesByCategory(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }else if (baction.equals("VIEW_EXPENSES_BY_CUSTOMER")) {
	    	  
	        jsonObjectResult = this.getexpensesByCustomer(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
			
	    }else if (baction.equals("CHECK_BILL_SUPPLIER")) {
	    	
			jsonObjectResult = new JSONObject();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			String vendno = StrUtils.fString(request.getParameter("VENDNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("EXBILL", orderno);
				ht.put("VENDNO", vendno);
				if (new ExpensesUtil().isExisitBillSupplier(ht)) {
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
		
	    }else if (baction.equals("VIEW_EXPENSES_BY_SUPPLIER")) {
	    	  
	        jsonObjectResult = this.getexpensesBySupplier(request);
	     	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	       	       
	    }else if (baction.equals("ExportExcelExpenseDetails")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelExpenseSummary(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=expense_details.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("ExportExcelExpenseByCategory")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelExpenseByCategory(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=expenses_by_category.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("ExportExcelExpenseByCustomer")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelExpenseByCustomer(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=expenses_by_customer.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("ExportExcelExpenseBySupplier")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelExpenseBySupplier(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=expenses_by_supplier.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("getexpByVend")) {
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String vendno=StrUtils.fString(request.getParameter("vendno")).trim();
			ExpensesUtil expensesUtil = new ExpensesUtil();
			Hashtable ht = new Hashtable();
			ht.put("VENDNO", vendno);
			ht.put("PLANT", PLANT);
//			BillDAO billDAO=new BillDAO();
			JSONObject expHdrJson=new JSONObject();
			List expHdr =new ArrayList<>();
			BillPaymentDAO billDao = new BillPaymentDAO();
			Hashtable ht1 = new Hashtable();
			ht1.put("VENDNO", vendno);
			ht1.put("PLANT", PLANT);
			try {
				List creditDetailList = billDao.getCreditDetails(ht1);
				double allpayment = 0;
				for (int j = 0; j < creditDetailList.size(); j++) {
					Map creditDetail = (Map) creditDetailList.get(j);
					String balforcheck = (String) creditDetail.get("BALANCE");
					double dbalforcheck = "".equals(balforcheck) ? 0.0d : Double.parseDouble(balforcheck);
					allpayment = allpayment + dbalforcheck;
				}
				expHdr=expensesUtil.getExpensesByVendno(ht, PLANT);
				expHdrJson.put("data",expHdr);
				expHdrJson.put("credit",allpayment);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response.getWriter().write(expHdrJson.toString());
		
		}else if(action.equalsIgnoreCase("showcreditforapply"))
		{
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String vendno=StrUtils.fString(request.getParameter("vendno")).trim();
//			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();
			
			//ht.put("PONO", req.getParameter("pono"));
			ht.put("VENDNO", vendno);
			ht.put("EXPHDRID", request.getParameter("eid"));
			ht.put("PLANT", plant);
			
			BillPaymentDAO billDao = new BillPaymentDAO();
			
			try {
				 List creditDetailList = billDao.getCreditDetails(ht);	
//				 JSONObject resultJsonInt = new JSONObject();
				 resultJson.put("CREDIT", creditDetailList);
		           
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			response.getWriter().write(resultJson.toString());
			
		}else if(action.equalsIgnoreCase("getbalanceofexp"))
		{
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String vendno=StrUtils.fString(request.getParameter("vendno")).trim();
			String balace = "0";
			JSONObject resultJson = new JSONObject();
//			JSONArray jsonArray = new JSONArray();
//			JSONObject resultJsonInt1 = new JSONObject();
	        JSONArray jsonArrayErr = new JSONArray();
			Hashtable ht = new Hashtable();
			
			//ht.put("PONO", req.getParameter("pono"));
			ht.put("VENDNO", vendno);
			ht.put("EXPHDRID", request.getParameter("eid"));
			ht.put("PLANT", plant);
			
			BillPaymentDAO billDao = new BillPaymentDAO();
			
			try {
				 balace =billDao.getpaymentMadeyBillwithexpno(ht);
				 resultJson.put("BALANCE", balace);
		           
			} catch (Exception e) {
				resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
			}
			
			
			response.getWriter().write(resultJson.toString());
			
		}else if(action.equalsIgnoreCase("ApplyCredit")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			/* FINPAYMENTDET */
			String[] payHdrId = null, expHdrId = null, amount = null, pono = null, payDetId = null,capplyKey = null;
			payHdrId = request.getParameterValues("PAYHDRID[]");
			expHdrId = request.getParameterValues("EXPHDRID[]");
			amount = request.getParameterValues("AMOUNT[]");
			pono = request.getParameterValues("PONO[]");
			payDetId = request.getParameterValues("PAYDETID[]");
			capplyKey = request.getParameterValues("CAPPLYKEY[]");
			String curtobase= request.getParameter("CURRENCYUSEQT");
//			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			BillPaymentDAO billPaymentDao = new BillPaymentDAO();
//			BillDAO billDao = new BillDAO();
			DateUtils dateutils = new DateUtils();
			/*BillUtil billUtil = new BillUtil();*/
			ExpensesUtil expensesUtil = new ExpensesUtil();
			UserTransaction ut = null;
			boolean isAdded = false;
			
			try {
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				
				for(int i =0 ; i < payHdrId.length ; i++){
					
					String pordno = "";
					Hashtable ht = new Hashtable();
					ht.put("PAYHDRID", payHdrId[i]);
					ht.put("PLANT", plant);
					List advpaymentList = billPaymentDao.getMaxLineNoForAdvancePayment(ht);
					Map map=(Map)advpaymentList.get(0);
					int lnno = Integer.parseInt((String)map.get("LNNO"));
					double balance = Double.parseDouble((String)map.get("BALANCE"));
					Hashtable<String,String> paymentDetInfo = new Hashtable<String, String>();
					paymentDetInfo.put("PLANT", plant);
					paymentDetInfo.put("LNNO", Integer.toString(lnno+1));
					paymentDetInfo.put("AMOUNT", amount[i]);
					paymentDetInfo.put("BALANCE", "0");							
					paymentDetInfo.put("PONO", pono[i]);
					paymentDetInfo.put("PAYHDRID", payHdrId[i]);
					paymentDetInfo.put("BILLHDRID", "0");		
					paymentDetInfo.put("EXPHDRID", (String) expHdrId[i]);	
					paymentDetInfo.put("TYPE", "REGULAR");
					paymentDetInfo.put("CURRENCYUSEQT", curtobase);
					paymentDetInfo.put("CRAT", dateutils.getDateTime());
					paymentDetInfo.put("CRBY", username);
					paymentDetInfo.put("UPAT", dateutils.getDateTime());
					if(capplyKey == null) {
						
					}else {
						if(capplyKey.length > 0) {
							paymentDetInfo.put("CREDITAPPLYKEY", capplyKey[i]);
						}
					}
					isAdded = billPaymentDao.addPaymentDet(paymentDetInfo, plant);
					if(isAdded) {
						Hashtable htCondition = new Hashtable();
						htCondition.put("PAYHDRID", payHdrId[i]);
						htCondition.put("LNNO", "0");
						htCondition.put("PLANT", plant);
						htCondition.put("ID", payDetId[i]);
						
						balance = balance - Double.parseDouble(amount[i]);
						
						String query = " SET BALANCE='"+balance+"'";
						isAdded = billPaymentDao.updatePaymentDet(query, htCondition, "");
						if(isAdded) {
							
							
							Hashtable ht1 = new Hashtable();
							ht1.put("PONO", pono[i]);
							ht1.put("EXPHDRID", expHdrId[i]);
							ht1.put("PLANT", plant);
							String paymentMade = billPaymentDao.getpaymentMadeyBillwithexpno(ht1); 
							double dPaymentMade ="".equals(paymentMade) ? 0.0d :  Double.parseDouble(paymentMade);
							
							
							
							
							Hashtable ht2 = new Hashtable();
							ht2.put("ID", expHdrId[i]);
							ht2.put("PLANT", plant);
							List expHdrList =  expensesUtil.getExpensesforDetails(ht2,plant);
							Map expHdr=(Map)expHdrList.get(0);
							pordno = (String) expHdr.get("ORDERNO");
							if(!pordno.equalsIgnoreCase("")) {
								pordno = pordno+",";
							}
							String totalAmount= (String) expHdr.get("TOTAL_AMOUNT");
							double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
							
							double paydiff = dTotalAmount - dPaymentMade;
							
							htCondition = new Hashtable();
							htCondition.put("ID", expHdrId[i]);
							htCondition.put("PLANT", plant);
							
							if(paydiff > 0) {
								query = " SET PAYMENT_STATUS='Partially Paid'";
							}else {
								query = " SET PAYMENT_STATUS='Paid'";
							}
							
							
							
							isAdded = expensesUtil.updateexpHdr(query, htCondition, "");
							
							
							
						}
						
						String advancefrom  = billPaymentDao.getadvancefrompaymentdet(payHdrId[i], plant);
						
						if(advancefrom.equalsIgnoreCase("GENERAL")) {
							//Supplier Credit Status Update
							SupplierCreditDAO supplierCreditDAO = new SupplierCreditDAO();
							Hashtable SCht = new Hashtable();												
							SCht.put("PLANT", plant);
							isAdded = supplierCreditDAO.updateHdr("SET CREDIT_STATUS= case when (select ISNULL((B.BALANCE),0) AMOUNT from "+plant+"_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID ='"+payHdrId[i]+"')>0 then 'Partially Applied' when (select ISNULL((B.BALANCE),0) AMOUNT from "+plant+"_FINPAYMENTDET B where B.TYPE='ADVANCE' AND B.PAYHDRID ='"+payHdrId[i]+"')=0 then 'Applied' else 'Open' end ", SCht, " AND CREDITNOTE in (select REFERENCE from "+plant+"_FINPAYMENTHDR where ID='"+payHdrId[i]+"')");
						}
						
						
						MovHisDAO movHisDao = new MovHisDAO();

						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
					    htMovHis.put(IDBConstants.PLANT, plant);
					    htMovHis.put("DIRTYPE", TransactionConstants.APPLY_CREDIT_PAYMENT);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																		
						htMovHis.put("RECID", "");
					    htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(payHdrId[i]));
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",pordno+expHdrId[i]+","+amount[i]);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}else {
						break;
					}
				}
				if(isAdded) {
					DbBean.CommitTran(ut);
					jsonObjectResult.put("MESSAGE", "Payment processed successfully");
					jsonObjectResult.put("ERROR_CODE", "100");
				}else {
					 DbBean.RollbackTran(ut);
					 jsonObjectResult.put("MESSAGE", "Could not process payment");
					 jsonObjectResult.put("ERROR_CODE", "99");
				}
				
				
			}catch (Exception e) {
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
		
		 if (baction.equals("GET_EXPENSE_DET")) {
			 String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			 String EID = StrUtils.fString(request.getParameter("ID")).trim();
             jsonObjectResult = this.getexpensedescription(plant, EID);
             response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
             response.getWriter().write(jsonObjectResult.toString());
             response.getWriter().flush();
             response.getWriter().close();
          }
		 
		 if (baction.equals("AP_DEBIT_NOTE")) {
			 
			 	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				String vendno=StrUtils.fString(request.getParameter("vendno")).trim();
				String eid=StrUtils.fString(request.getParameter("eid")).trim();
				String totalAmount=StrUtils.fString(request.getParameter("debitnoteamount")).trim();
				String creditnoteDate=StrUtils.fString(request.getParameter("debitnotedate")).trim();
				String username=StrUtils.fString(request.getParameter("username")).trim();
				String currencyid=StrUtils.fString(request.getParameter("currency")).trim();
				String expacc =StrUtils.fString(request.getParameter("expacc")).trim(); 
				String expdebitremarks =StrUtils.fString(request.getParameter("expdebitremarks")).trim();
				String jdesc="";
				  
					/*
					 * String =StrUtils.fString(request.getParameter("")).trim(); String
					 * =StrUtils.fString(request.getParameter("")).trim();
					 */
				 
				
				JSONObject resultJson = new JSONObject();
		        JSONArray jsonArrayErr = new JSONArray();
				Hashtable ht = new Hashtable();
				BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
				DateUtils dateutils = new DateUtils();
				MovHisDAO movHisDao = new MovHisDAO();
				ExpensesUtil expensesUtil = new ExpensesUtil();
				try {
					
					boolean isadded = false;
					
					List<Hashtable<String,String>>  paymentDetInfoList = new ArrayList<Hashtable<String,String>>();
					Hashtable paymentHdr =new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", vendno);
					paymentHdr.put("AMOUNTPAID", totalAmount);
					paymentHdr.put("PAYMENT_DATE", creditnoteDate);
					paymentHdr.put("PAYMENT_MODE", "Cash");
					paymentHdr.put("PAID_THROUGH", "Advance");
					paymentHdr.put("REFERENCE", eid);
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
						
						Hashtable<String,String> paymentDetInfo = new Hashtable<String, String>();
							paymentDetInfo.put("PLANT", plant);							
								paymentDetInfo.put("LNNO", "0");
								paymentDetInfo.put("AMOUNT", totalAmount);
								paymentDetInfo.put("BALANCE", totalAmount);													
							//paymentDetInfo.put("PONO", pono);
								paymentDetInfo.put("PONO", "");
							paymentDetInfo.put("PAYHDRID", payHdrId);
							paymentDetInfo.put("BILLHDRID", "0");												
							paymentDetInfo.put("TYPE", "ADVANCE");
							paymentDetInfo.put("ADVANCEFROM", "APDOCUMENT");
							paymentDetInfo.put(IDBConstants.CURRENCYUSEQT, "1");
							paymentDetInfo.put("CRAT", dateutils.getDateTime());
							paymentDetInfo.put("CRBY", username);
							paymentDetInfo.put("UPAT", dateutils.getDateTime());
							paymentDetInfoList.add(paymentDetInfo);
						
							isadded = billPaymentUtil.addMultiplePaymentDet(paymentDetInfoList, plant);
					}
					
					if(isadded) {
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_PAYMENT);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																			
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, payHdrId);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS","AP Doucumet "+eid+","+totalAmount);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					
					String query1 = " SET PAYMENTID='"+payHdrId+"',DEBITNOTEAMOUNT='"+totalAmount+"',DEBITNOTEREMARK='"+expdebitremarks+"' ";
					Hashtable ht4 = new Hashtable();
					ht4.put("ID", eid);
					ht4.put("PLANT", plant);
					expensesUtil.updateexpHdr(query1, ht4, "");
					
					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					//Journal Entry
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(creditnoteDate);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("APDOCUMENTCREDITNOTES");
					journalHead.setTRANSACTION_ID(payHdrId);
					journalHead.setSUB_TOTAL(Double.parseDouble(totalAmount));
					//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
					journalHead.setCRAT(dateutils.getDateTime());
					journalHead.setCRBY(username);
					
					
					
					
					List<JournalDetail> journalDetails=new ArrayList<>();
					CoaDAO coaDAO=new CoaDAO();
					VendMstDAO vendorDAO=new VendMstDAO();

						
					JournalDetail journalDetail=new JournalDetail();
					journalDetail.setPLANT(plant);
					JSONObject coaJson1=coaDAO.getCOAByName(plant, vendno);
					if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
						JSONObject vendorJson=vendorDAO.getVendorName(plant, vendno);
						if(!vendorJson.isEmpty()) {
							coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
								jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
							}
						
						}
					}
					JSONObject coaJson=coaDAO.getCOAByName(plant, expacc);
					System.out.println("Json"+coaJson.toString());
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME(expacc);
					journalDetail.setDESCRIPTION(jdesc);
					journalDetail.setCREDITS(Double.parseDouble(totalAmount));
					journalDetails.add(journalDetail);
						

					
					JournalDetail journalDetail_1=new JournalDetail();
					journalDetail_1.setPLANT(plant);
					
					if(coaJson1.isEmpty() || coaJson1.isNullObject())
					{
						
					}
					else
					{
						journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						if(coaJson1.getString("account_name")!=null) {
							journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
						}
					journalDetail_1.setDESCRIPTION(jdesc);	
					journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
					journalDetails.add(journalDetail_1);
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
					
					
					response.sendRedirect("../expenses/apdetail?TRANID="+ eid);/* Redirect to Expenses Summary page */
				} catch (Exception e) {
					response.sendRedirect("../expenses/apdetail?TRANID="+ eid);/* Redirect to Expenses Summary page */
		          
				}
				
				
				response.getWriter().write(resultJson.toString());
          }
	}
	
	private JSONObject getexpensesview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String VENDNO = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
//           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if (ACCOUNT_NAME.length()>0)
        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
           if (REFERENCE.length()>0)
        	   ht.put("REFERENCE",REFERENCE);
           if (STATUS.length()>0)
        	   ht.put("STATUS",STATUS);
           movQryList = expensesUtil.getExpensesSummary(ht,fdate,tdate,PLANT,VENDNO);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;
            String covunitCostValue="0";//lastProduct="",
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*Float.parseFloat((String)lineArr.get("CURRENCYTOBASE"))), numberOfDecimal);
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("expensesdate",StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 //if(cur.isEmpty())
                    		 //String cur= curency;
                    	 resultJsonInt.put("currency",cur);
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("convamount",covunitCostValue);
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYTOBASE")));
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
	
	private JSONObject getapexpensesview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String VENDNO = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
//           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE="";

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if (ACCOUNT_NAME.length()>0)
        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
           if (REFERENCE.length()>0)
        	   ht.put("REFERENCE",REFERENCE);
           if (STATUS.length()>0)
        	   ht.put("STATUS",STATUS);
           movQryList = expensesUtil.getApExpensesSummary(ht,fdate,tdate,PLANT,VENDNO);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;
            String covunitCostValue="0";//lastProduct="",
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*Float.parseFloat((String)lineArr.get("CURRENCYTOBASE"))), numberOfDecimal);
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("expensesdate",StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("EXBILL")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 //if(cur.isEmpty())
                    		 //String cur= curency;
                    	 resultJsonInt.put("currency",cur);
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("convamount",covunitCostValue);
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYTOBASE")));
                    	 
                    	 resultJsonInt.put("OUTLET", (String)lineArr.get("OUTLET"));
     					String outletname = outletBeanDAO.getOutletname(PLANT, (String)lineArr.get("OUTLET"), "");
     					resultJsonInt.put("OUTLETNAME", outletname);
     					resultJsonInt.put("TERMINAL", (String)lineArr.get("TERMINAL"));
     					String terminalname = outletBeanDAO.getOutletTerminalname(PLANT, (String)lineArr.get("OUTLET"), (String)lineArr.get("TERMINAL"));
     					resultJsonInt.put("TERMINALNAME", terminalname);
                    	 
                    	 String samount = new BillPaymentDAO().gettotalpayforapexpenses(PLANT, (String)lineArr.get("ID"));
                    	 double damount = Double.valueOf(samount);
                    	 double camount = Double.parseDouble(unitCostValue);
                    	 
                    	 String ispaid = StrUtils.fString((String)lineArr.get("ISPAID"));
                    	 
                    	 if(ispaid.equalsIgnoreCase("1")) {
                    		 resultJsonInt.put("paystatus",2);
                    	 }else {
	                    	 if(damount == 0) {
	                    		 resultJsonInt.put("paystatus",0);
	                    	 }else if(damount == camount) {
	                    		 resultJsonInt.put("paystatus",2);
	                    	 }else {
	                    		 resultJsonInt.put("paystatus",1); 
	                    	 }
                    	 }
                    	 
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
	
	private JSONObject getposexpensesview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String VENDNO = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String  OUTLET = StrUtils.fString(request.getParameter("OUTLET"));
           String  TERMINAL = StrUtils.fString(request.getParameter("TERMINAL"));
//           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           //String curDate =DateUtils.getDate();
           //if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if (ACCOUNT_NAME.length()>0)
        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
           if (REFERENCE.length()>0)
        	   ht.put("REFERENCE",REFERENCE);
           if (STATUS.length()>0)
        	   ht.put("STATUS",STATUS);
           if (OUTLET.length()>0)
        	   ht.put("OUTLET",OUTLET);
           if (TERMINAL.length()>0)
        	   ht.put("TERMINAL",TERMINAL);
           movQryList = expensesUtil.getPosExpensesSummary(ht,fdate,tdate,PLANT,VENDNO);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;
            String covunitCostValue="0";//lastProduct="",
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*Float.parseFloat((String)lineArr.get("CURRENCYTOBASE"))), numberOfDecimal);
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("expensesdate",StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 //if(cur.isEmpty())
                    		 //String cur= curency;
                    	 resultJsonInt.put("currency",cur);
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("convamount",covunitCostValue);
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYTOBASE")));
                    	 
                    	 resultJsonInt.put("OUTLET", (String)lineArr.get("OUTLET"));
      					String outletname = outletBeanDAO.getOutletname(PLANT, (String)lineArr.get("OUTLET"), "");
      					resultJsonInt.put("OUTLETNAME", outletname);
      					resultJsonInt.put("TERMINAL", (String)lineArr.get("TERMINAL"));
      					String terminalname = outletBeanDAO.getOutletTerminalname(PLANT, (String)lineArr.get("OUTLET"), (String)lineArr.get("TERMINAL"));
      					resultJsonInt.put("TERMINALNAME", terminalname);
      					
      					Integer jid =new JournalDAO().getJournalTridandTrtype(PLANT, StrUtils.fString((String)lineArr.get("ID")), "EXPENSE");
      					if(jid == 0) {
      						resultJsonInt.put("journal", "0");
      					}else {
      						resultJsonInt.put("journal", "1");
      					}
                    	 
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
	
	private JSONObject getexpensesDetailView(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="",numberOfDecimals="2";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String VENDNO = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if (ACCOUNT_NAME.length()>0)
        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
           if (REFERENCE.length()>0)
        	   ht.put("REFERENCE",REFERENCE);
           if (STATUS.length()>0)
        	   ht.put("STATUS",STATUS);
           movQryList = expensesUtil.getExpenseDetails(ht,fdate,tdate,PLANT,VENDNO);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			/*String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);*/
                            String currencyUseqtValue = (String)lineArr.get("CURRENCYUSEQT");
                            float currencyUseqtVal ="".equals(currencyUseqtValue) ? 0.0f :  Float.parseFloat(currencyUseqtValue);
                            
                            String subTotalValue = (String)lineArr.get("SUB_TOTAL_AMOUNT");
                            float subTotalVal ="".equals(subTotalValue) ? 0.0f :  Float.parseFloat(subTotalValue);
                            if(subTotalVal==0f){
                            	subTotalValue = "0.00";
                            }else{
                            	subTotalValue = subTotalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            /*subTotalValue = StrUtils.addZeroes(Double.parseDouble(subTotalValue), numberOfDecimal);*/
                            subTotalValue = StrUtils.addZeroes(Double.parseDouble(subTotalValue)*currencyUseqtVal, numberOfDecimal);
                            
                            float totalval = 0.0f;
                            String totalValue = "0.0";
                            String taxValue = (String)lineArr.get("TAX_TYPE");
                            if(!taxValue.equalsIgnoreCase("")) {
                            	int beginIndex = taxValue.indexOf('[');
                            	int endIndex = taxValue.indexOf('%');
                            	float tax = 0;
                            	if(beginIndex == -1) {
                            		tax = 0;
                            	}else {
                            		tax = Float.parseFloat(taxValue.substring(beginIndex+1, endIndex));
                            	}                       	
                            	totalval = (Float.parseFloat(subTotalValue))*(1+(tax/100));
                            	totalValue = Float.toString(totalval);
                            	totalValue = StrUtils.addZeroes(Double.parseDouble(totalValue), numberOfDecimal);
                            }else {
                            	totalValue = subTotalValue;
                            }
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("expensesdate",StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("currencyuseqt",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 if(cur.isEmpty())
                    		 cur= curency;
                    	 resultJsonInt.put("currency",cur);
                    	 resultJsonInt.put("basecurrency",curency);
                    	 resultJsonInt.put("amount",totalValue);
                    	 resultJsonInt.put("subtotal",subTotalValue);
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
	
	private JSONObject getexpensesByCategory(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="";//,taxby="";
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));   
           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if (ACCOUNT_NAME.length()>0)
        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
           movQryList = expensesUtil.getExpensesByCategory(ht,fdate,tdate,PLANT);	
			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);  
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                            float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                            if(amountwithtaxval==0f){
                            	amountwithtaxValue = "0.00";
                            }else{
                            	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);
 
                            String amountValue = (String)lineArr.get("amount");
                            float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                            if(amountVal==0f){
                            	amountValue = "0.00";
                            }else{
                            	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
 
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
                    	 resultJsonInt.put("amount",amountValue);
                    	 resultJsonInt.put("amountwithtax",amountwithtaxValue);
                    	 resultJsonInt.put("basecurrency",curency);
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
	
	private JSONObject getexpensesByCustomer(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        ArrayList movQryList  = new ArrayList();
        CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="",custcode="";//taxby="",
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));   
           String CNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if(!CNAME.equalsIgnoreCase("")) {
           	custcode = customerBeanDAO.getCustomerCode(PLANT, CNAME);
           	ht.put("CUSTNO",custcode);
           }
           movQryList = expensesUtil.getExpensesByCustomer(ht,fdate,tdate,PLANT);			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);  
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                            float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                            if(amountwithtaxval==0f){
                            	amountwithtaxValue = "0.00";
                            }else{
                            	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);
 
                            String amountValue = (String)lineArr.get("amount");
                            float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                            if(amountVal==0f){
                            	amountValue = "0.00";
                            }else{
                            	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
 
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("cname",StrUtils.fString((String)lineArr.get("CNAME")));
                    	 resultJsonInt.put("expensecount",StrUtils.fString((String)lineArr.get("expensecount")));
                    	 resultJsonInt.put("amount",amountValue);
                    	 resultJsonInt.put("amountwithtax",amountwithtaxValue);
                    	 resultJsonInt.put("basecurrency",curency);
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
	
	private JSONObject getexpensesBySupplier(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        ArrayList movQryList  = new ArrayList();
      CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="",vendno="";//taxby="",
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));   
           String VNAME = StrUtils.fString(request.getParameter("vendname"));
           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           Hashtable ht = new Hashtable();
           if(!VNAME.equalsIgnoreCase("")) {
        	   vendno = customerBeanDAO.getVendorCode(PLANT, VNAME);
           	ht.put("VENDNO",vendno);
           }
           movQryList = expensesUtil.getExpensesBySupplier(ht,fdate,tdate,PLANT);			
            
            if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);  
                            JSONObject resultJsonInt = new JSONObject();
                            
                            String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                            float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                            if(amountwithtaxval==0f){
                            	amountwithtaxValue = "0.00";
                            }else{
                            	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);
 
                            String amountValue = (String)lineArr.get("amount");
                            float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                            if(amountVal==0f){
                            	amountValue = "0.00";
                            }else{
                            	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
 
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("vname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("expensecount",StrUtils.fString((String)lineArr.get("expensecount")));
                    	 resultJsonInt.put("amount",amountValue);
                    	 resultJsonInt.put("amountwithtax",amountwithtaxValue);
                    	 resultJsonInt.put("basecurrency",curency);
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

	private JSONObject getEditExpensesDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesUtil expUtil = new ExpensesUtil();
        OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String TranId = StrUtils.fString(request.getParameter("Id")).trim();
		    
//		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
//		    String ConvertedUnitCost="",ConvertedAmount="";
		    Hashtable ht=new Hashtable();
		    ht.put("ID", TranId);
		    ht.put("PLANT", plant);
		    
		    ArrayList listQry = expUtil.getEditIExpensesDetails(ht,plant);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();					
					
					//double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
					//ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					//HDR Details
					resultJsonInt.put("ID", (String)m.get("ID"));	
					resultJsonInt.put("ISPAID", (String)m.get("ISPAID"));		
					resultJsonInt.put("EXPENSES_DATE", (String)m.get("EXPENSES_DATE"));
					resultJsonInt.put("ORDERNO", (String)m.get("ORDERNO"));
					resultJsonInt.put("BILL", (String)m.get("BILL"));
					resultJsonInt.put("CUST_CODE", (String)m.get("CUST_CODE"));
					resultJsonInt.put("CUSTOMER", (String)m.get("CUSTOMER"));
					resultJsonInt.put("VENDNO", (String)m.get("VENDNO"));					
					resultJsonInt.put("VNAME", (String)m.get("VNAME"));
					resultJsonInt.put("SHIPMENT_CODE", (String)m.get("SHIPMENT_CODE"));					
					resultJsonInt.put("PAID_THROUGH", (String)m.get("PAID_THROUGH"));
					resultJsonInt.put("REFERENCE", (String)m.get("REFERENCE"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("STATUS", (String)m.get("STATUS"));					
					resultJsonInt.put("TOTAL_AMOUNT", (String)m.get("TOTAL_AMOUNT"));
					resultJsonInt.put("SUB_TOTAL", (String)m.get("SUB_TOTAL"));
					resultJsonInt.put("ATTACHNOTE_COUNT", (String)m.get("ATTACHNOTE_COUNT"));
					resultJsonInt.put("ISBILLABLE", (String)m.get("ISBILLABLE"));
					resultJsonInt.put("TAXTREATMENT", (String)m.get("TAXTREATMENT"));
					resultJsonInt.put("CATLOGPATH","../jsp/dist/img/NO_IMG.png");
					resultJsonInt.put("sTAXTREATMENT", (String)m.get("sTAXTREATMENT"));
					resultJsonInt.put("sGOODSIMPORT", (String)m.get("GOODSIMPORT"));
					resultJsonInt.put("sREVERSECHARGE", (String)m.get("REVERSECHARGE"));
					resultJsonInt.put("TAXAMOUNT", (String)m.get("TAXAMOUNT"));
					resultJsonInt.put("TAXID", (String)m.get("TAXID"));
					resultJsonInt.put("STANDARDTAX", (String)m.get("STANDARDTAX"));
					resultJsonInt.put("EXBILL", (String)m.get("EXBILL"));
					String proid = (String)m.get("PROJECTID");
					if(!proid.equalsIgnoreCase("")) {
						int projectid = Integer.valueOf(proid);
						if(projectid == 0) {
	                   	 	resultJsonInt.put("PROJECTNAME", "");
	                   	 	resultJsonInt.put("PROJECTID", "");
						}else {
							FinProjectDAO finProjectDAO = new FinProjectDAO();
							FinProject finProject= finProjectDAO.getFinProjectById(plant, projectid);
							resultJsonInt.put("PROJECTNAME", finProject.getPROJECT_NAME());
							resultJsonInt.put("PROJECTID", projectid);
						}
					}else {
						resultJsonInt.put("PROJECTNAME", "");
                   	 	resultJsonInt.put("PROJECTID", "");
					}
					//Item Details
					
					resultJsonInt.put("DETID", (String)m.get("DETID"));
					resultJsonInt.put("EXPENSES_ACCOUNT", (String)m.get("EXPENSES_ACCOUNT"));
					resultJsonInt.put("DESCRIPTION", (String)m.get("DESCRIPTION"));
					resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
					resultJsonInt.put("AMOUNT", (String)m.get("AMOUNT"));
					resultJsonInt.put("CURRENCYTOBASE", (String)m.get("CURRENCYTOBASE"));
					resultJsonInt.put("DISPLAY", (String)m.get("DISPLAY"));
					resultJsonInt.put("EXPENSETAX", (String)m.get("EXPENSETAX"));
					resultJsonInt.put("EXPENSETAXAMOUNT", (String)m.get("EXPENSETAXAMOUNT"));
					resultJsonInt.put("ISEXPENSEGST", (String)m.get("ISEXPENSEGST"));
					
					resultJsonInt.put("OUTLET", (String)m.get("OUTLET"));
					String outletname = outletBeanDAO.getOutletname(plant, (String)m.get("OUTLET"), "");
					resultJsonInt.put("OUTLETNAME", outletname);
					resultJsonInt.put("TERMINAL", (String)m.get("TERMINAL"));
					String terminalname = outletBeanDAO.getOutletTerminalname(plant, (String)m.get("OUTLET"), (String)m.get("TERMINAL"));
					resultJsonInt.put("TERMINALNAME", terminalname);
										
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
	
	private JSONObject getcurrency(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        CurrencyUtil currencyUtil = new CurrencyUtil();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		   
		    ArrayList listQry = currencyUtil.getCurrencyDetailsfordropdown("", plant);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();					
									
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("DESCRIPTION", (String)m.get("DESCRIPTION"));
					resultJsonInt.put("DISPLAY", (String)m.get("DISPLAY"));
					resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
					jsonArray.add(resultJsonInt);
		    	}
		    	resultJson.put("currency", jsonArray);
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
	
	private JSONObject getexpensehdrid(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ExpensesDAO expensesDAO = new ExpensesDAO();
//		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String shipmentcode = StrUtils.fString(request.getParameter("SHIPMENT_CODE")).trim();
			String pono = StrUtils.fString(request.getParameter("PONO")).trim();
			
			Hashtable ht=new Hashtable();
		    ht.put("SHIPMENT_CODE", shipmentcode);
		    ht.put("PONO", pono);
		    ht.put("PLANT", plant);
			
		   
		    List listQry = expensesDAO.getExpensehdrbyponoandshipment(ht);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();					
									
					resultJsonInt.put("ID", (String)m.get("ID"));
					resultJsonInt.put("STATUS", (String)m.get("STATUS"));
					
					
					jsonArray.add(resultJsonInt);
		    	}
		    	resultJson.put("expense", jsonArray);
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
                resultJson.put("expense", jsonArray);
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
	
	private JSONObject getBasecurrency(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			String basecurrency = plantMstDAO.getBaseCurrency(plant);

			String CURRENCYUSEQT="0",DISPLAY="";
			List curQryList=new ArrayList();
			curQryList = currencyDAO.getCurrencyDetails(basecurrency,plant);
			for(int i =0; i<curQryList.size(); i++) {
				ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				DISPLAY	= StrUtils.fString((String)arrCurrLine.get(2));
				CURRENCYUSEQT	= StrUtils.fString((String)arrCurrLine.get(3));
				
                double CURRENCYUSEQTValue ="".equals(CURRENCYUSEQT) ? 0.0d :  Double.parseDouble(CURRENCYUSEQT);        					
                CURRENCYUSEQT = StrUtils.addZeroes(CURRENCYUSEQTValue, numberOfDecimal);
		        }
		   
					JSONObject resultJsonadd = new JSONObject();					
									
					resultJsonadd.put("bcur",basecurrency);
					resultJsonadd.put("CURRENCYUSEQT", CURRENCYUSEQT);
					resultJsonadd.put("DISPLAY", DISPLAY);
					
					jsonArray.add(resultJsonadd);
		    	
		    	resultJson.put("currency", jsonArray);
		    	JSONObject resultJsonInt = new JSONObject();	
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		   
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
	
//	private JSONObject getExpenseDetails(HttpServletRequest request) {
//        JSONObject resultJson = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        JSONArray jsonArrayErr = new JSONArray();
//        ExpensesUtil expensesUtil       = new  ExpensesUtil();
//        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//        DateUtils _dateUtils = new DateUtils();
//        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
//       
//        StrUtils strUtils = new StrUtils();
//        String fdate="",tdate="",taxby="";
//         try {
//        
//           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
//           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
//           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
//           String VENDNO = StrUtils.fString(request.getParameter("SUPPLIER"));
//           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("ACCOUNT_NAME"));
//           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
//           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
//           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
//           String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
//           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
//           String curDate =DateUtils.getDate();
//           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
//
//           if (FROM_DATE.length()>5)
//            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
//
//           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
//           if (TO_DATE.length()>5)
//           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
//           Hashtable ht = new Hashtable();
//           if (ACCOUNT_NAME.length()>0)
//        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
//           if (REFERENCE.length()>0)
//        	   ht.put("REFERENCE",REFERENCE);
//           if (STATUS.length()>0)
//        	   ht.put("STATUS",STATUS);
//           movQryList = expensesUtil.getExpensesSummary(ht,fdate,tdate,PLANT,VENDNO);	
//			
//            
//            if (movQryList.size() > 0) {
//            int iIndex = 0,Index = 0;
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
//           
//                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
//                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
//                            JSONObject resultJsonInt = new JSONObject();
//                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
//                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
//                            if(unitCostVal==0f){
//                            	unitCostValue="0.00";
//                            }else{
//                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
//                            }
//                            unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), numberOfDecimal);
//                      
//                    	 Index = Index + 1;
//                    	 resultJsonInt.put("Index",(Index));
//                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
//                    	 resultJsonInt.put("expensesdate",StrUtils.fString((String)lineArr.get("EXPENSES_DATE")));
//                    	 resultJsonInt.put("expensesaccount",StrUtils.fString((String)lineArr.get("EXPENSES_ACCOUNT")));
//                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
//                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CUSTNO")));
//                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CNAME")));
//                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
//                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
//                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("STATUS")));
//                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
//                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
//                    	 if(cur.isEmpty())
//                    		 cur= curency;
//                    	 resultJsonInt.put("currency",cur);
//                    	 resultJsonInt.put("amount",unitCostValue);
//                         jsonArray.add(resultJsonInt);
//                    	            	
//
//                }
//               
//                    resultJson.put("items", jsonArray);
//                    JSONObject resultJsonInt = new JSONObject();
//                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
//                    resultJsonInt.put("ERROR_CODE", "100");
//                    jsonArrayErr.add(resultJsonInt);
//                    resultJson.put("errors", jsonArrayErr);
//            } else {
//                    JSONObject resultJsonInt = new JSONObject();
//                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
//                    resultJsonInt.put("ERROR_CODE", "99");
//                    jsonArrayErr.add(resultJsonInt);
//                    jsonArray.add("");
//                    resultJson.put("items", jsonArray);
//
//                    resultJson.put("errors", jsonArrayErr);
//            }
//        } catch (Exception e) {
//        		jsonArray.add("");
//        		resultJson.put("items", jsonArray);
//                resultJson.put("SEARCH_DATA", "");
//                JSONObject resultJsonInt = new JSONObject();
//                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
//                resultJsonInt.put("ERROR_CODE", "98");
//                jsonArrayErr.add(resultJsonInt);
//                resultJson.put("ERROR", jsonArrayErr);
//        }
//        return resultJson;
//	}
	
	private HSSFWorkbook writeToExcelExpenseSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "";
		int SheetId =1;
		try {
			   String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String VENDNO = StrUtils.fString(request.getParameter("vendname"));
	           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("expenses_account_name"));
	           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
	           String  STATUS = StrUtils.fString(request.getParameter("status"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	           Hashtable ht = new Hashtable();
	           if (ACCOUNT_NAME.length()>0)
	        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
	           if (REFERENCE.length()>0)
	        	   ht.put("REFERENCE",REFERENCE);
	           if (STATUS.length()>0)
	        	   ht.put("STATUS",STATUS);
	           movQryList = expensesUtil.getExpenseDetails(ht,fdate,tdate,PLANT,VENDNO);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "expensedetail");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensedetail");
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
						
						String currencyUseqtValue = (String)lineArr.get("CURRENCYUSEQT");
                        float currencyUseqtVal ="".equals(currencyUseqtValue) ? 0.0f :  Float.parseFloat(currencyUseqtValue);
                        
                        String subTotalValue = (String)lineArr.get("SUB_TOTAL_AMOUNT");
                        float subTotalVal ="".equals(subTotalValue) ? 0.0f :  Float.parseFloat(subTotalValue);
                        if(subTotalVal==0f){
                        	subTotalValue = "0.00";
                        }else{
                        	subTotalValue = subTotalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        /*subTotalValue = StrUtils.addZeroes(Double.parseDouble(subTotalValue), numberOfDecimal);*/
                        subTotalValue = StrUtils.addZeroes(Double.parseDouble(subTotalValue)*currencyUseqtVal, numberOfDecimal);
                        
                        float totalval = 0.0f;
                        String totalValue = "0.0";
                        String taxValue = (String)lineArr.get("TAX_TYPE");
                        if(!taxValue.equalsIgnoreCase("")) {
                        	int beginIndex = taxValue.indexOf('[');
                        	int endIndex = taxValue.indexOf('%');
                        	float tax = 0;
                        	if(beginIndex == -1) {
                        		tax = 0;
                        	}else {
                        		tax = Float.parseFloat(taxValue.substring(beginIndex+1, endIndex));   
                        	}
                        	                         	
                        	totalval = (Float.parseFloat(subTotalValue))*(1+(tax/100));
                        	totalValue = Float.toString(totalval);
                        	totalValue = StrUtils.addZeroes(Double.parseDouble(totalValue), numberOfDecimal);
                        }else {
                        	totalValue = subTotalValue;
                        }
						
						HSSFRow row = sheet.createRow(index);
						
						HSSFCellStyle numericCellStyle = createDataStyle(wb);
						numericCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("STATUS"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("EXPENSES_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("REFERENCE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("VNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PAID_THROUGH"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(Numbers.toMillionFormat(StrUtils.fString(subTotalValue),Integer.valueOf(numberOfDecimal))));
						cell.setCellStyle(numericCellStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(Numbers.toMillionFormat(StrUtils.fString(totalValue),Integer.valueOf(numberOfDecimal))));
						cell.setCellStyle(numericCellStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "expensedetail");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensedetail");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	private HSSFWorkbook writeToExcelExpenseByCategory(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "";
		int SheetId =1;
		try {
			   String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE")); 
	           String  ACCOUNT_NAME = StrUtils.fString(request.getParameter("expenses_account_name"));
//	           String  STATUS = StrUtils.fString(request.getParameter("status"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	           Hashtable ht = new Hashtable();
	           if (ACCOUNT_NAME.length()>0)
	        	   ht.put("ACCOUNT_NAME",ACCOUNT_NAME);
	           movQryList = expensesUtil.getExpensesByCategory(ht,fdate,tdate,PLANT);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "expensebycategory");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebycategory");
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
						
						String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                        float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                        if(amountwithtaxval==0f){
                        	amountwithtaxValue = "0.00";
                        }else{
                        	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);

                        String amountValue = (String)lineArr.get("amount");
                        float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                        if(amountVal==0f){
                        	amountValue = "0.00";
                        }else{
                        	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("EXPENSES_ACCOUNT"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountwithtaxValue)));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "expensebycategory");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebycategory");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	private HSSFWorkbook writeToExcelExpenseByCustomer(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", custcode = "";//taxby = "", 
		int SheetId =1;
		try {
			   String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE")); 
	           String CNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	           Hashtable ht = new Hashtable();
	           if(!CNAME.equalsIgnoreCase("")) {
	        	   CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	              	custcode = customerBeanDAO.getCustomerCode(PLANT, CNAME);
	              	ht.put("CUSTNO",custcode);
              }
	           movQryList = expensesUtil.getExpensesByCustomer(ht,fdate,tdate,PLANT);	
	           
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
						
						String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                        float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                        if(amountwithtaxval==0f){
                        	amountwithtaxValue = "0.00";
                        }else{
                        	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);

                        String amountValue = (String)lineArr.get("amount");
                        float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                        if(amountVal==0f){
                        	amountValue = "0.00";
                        }else{
                        	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("expensecount"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountwithtaxValue)));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "expensebycategory");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebycategory");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}
	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	
	private HSSFWorkbook writeToExcelExpenseBySupplier(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		ExpensesUtil expensesUtil       = new  ExpensesUtil();
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", vendno = "";//taxby = "", 
		int SheetId =1;
		try {
			   String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE")); 
	           String VNAME = StrUtils.fString(request.getParameter("vendname"));
//	           String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	           Hashtable ht = new Hashtable();
	           if(!VNAME.equalsIgnoreCase("")) {
	        	   CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	        	   vendno = customerBeanDAO.getVendorCode(PLANT, VNAME);
	           	ht.put("VENDNO",vendno);
	           }
	           movQryList = expensesUtil.getExpensesBySupplier(ht,fdate,tdate,PLANT);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "expensebysupplier");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebysupplier");
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
						
						String amountwithtaxValue = (String)lineArr.get("amountwithtax");
                        float amountwithtaxval ="".equals(amountwithtaxValue) ? 0.0f :  Float.parseFloat(amountwithtaxValue);
                        if(amountwithtaxval==0f){
                        	amountwithtaxValue = "0.00";
                        }else{
                        	amountwithtaxValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountwithtaxValue = StrUtils.addZeroes(Double.parseDouble(amountwithtaxValue), numberOfDecimal);

                        String amountValue = (String)lineArr.get("amount");
                        float amountVal ="".equals(amountValue) ? 0.0f :  Float.parseFloat(amountValue);
                        if(amountVal==0f){
                        	amountValue = "0.00";
                        }else{
                        	amountValue = amountwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        amountValue = StrUtils.addZeroes(Double.parseDouble(amountValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("VNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("expensecount"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountValue)));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString(amountwithtaxValue)));
						cell.setCellStyle(dataStyle);
						
						index++;
						 if((index-2)%maxRowsPerSheet==0){
							index = 2;
							SheetId++;
							sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthExpensesSummary(sheet, "expensebysupplier");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "expensebysupplier");
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
			String CNAME = "", CZIP = "", CCOUNTRY = "", CSTATE="" ;// CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2=""
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
			if (type.equals("expensedetail")) {
				sheet.setColumnWidth(i++, 1000);
				sheet.setColumnWidth(i++, 4500);
				sheet.setColumnWidth(i++, 3500);
				sheet.setColumnWidth(i++, 4000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 4000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 4000);
				sheet.setColumnWidth(i++, 4000);
			}
			if (type.equals("expensebycategory")) {
				sheet.setColumnWidth(i++, 1000);
				sheet.setColumnWidth(i++, 6000);
				sheet.setColumnWidth(i++, 5000);
				sheet.setColumnWidth(i++, 5000);
			}
			if (type.equals("expensebycustomer") || type.equals("expensebysupplier")) {
				sheet.setColumnWidth(i++, 1000);
				sheet.setColumnWidth(i++, 6000);
				sheet.setColumnWidth(i++, 3500);
				sheet.setColumnWidth(i++, 6000);
				sheet.setColumnWidth(i++, 5000);
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
			if (type.equals("expensedetail")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Reference"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Vendor Name"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Category"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount With Tax"));
				cell.setCellStyle(styleHeader);
			}
			
			if (type.equals("expensebycategory")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Category"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount With Tax"));
				cell.setCellStyle(styleHeader);
			}
			
			if (type.equals("expensebycustomer")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Customer Name"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Expense Count"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount With Tax"));
				cell.setCellStyle(styleHeader);
			}
			
			if (type.equals("expensebysupplier")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Supplier Name"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Expense Count"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount With Tax"));
				cell.setCellStyle(styleHeader);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	
	private JSONObject getexpensedescription(String plant, String exid) {
		JSONObject resultJson = new JSONObject();
		String description = "";
		try {
			ExpensesUtil expensesUtil = new ExpensesUtil();
			Hashtable ht2 = new Hashtable();
			ht2.put("ID", exid);
			ht2.put("PLANT", plant);
			List expHdrList =  expensesUtil.getExpensesforDetails(ht2,plant);
			
			for (int i = 0; i < expHdrList.size(); i++) {
				Map expHdr=(Map)expHdrList.get(i);
				if(i != 0) {
					description +=",";
				}
				description += (String) expHdr.get("DESCRIPTION");
				
			}
			resultJson.put("description", description);
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("description", description);
			return resultJson;
		}
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
