package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.EmployeeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.QuotesHdrDAO;
import com.track.db.object.QuotesDET;
import com.track.db.object.QuotesHDR;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.service.QuotesDETService;
import com.track.service.QuotesHDRService;
import com.track.serviceImplementation.QuotesDETServiceImpl;
import com.track.serviceImplementation.QuotesHDRServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONObject;

@WebServlet("/invoicequotes/*")
public class InvoiceQuotesServlet extends HttpServlet implements IMLogger {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String[] pathInfo = request.getPathInfo().split("/");
		//String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		String action = "";
		String baction = "";
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
		if(action.equalsIgnoreCase("Save")) {

			/* InvoiceHdr*/
			String CustCode = "", invoicequotesNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "",cmd = "",TranId = "",salesloc="",orderdiscount="0",
			itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "",isexpense = "0",taxamount = "0",gino="",shippingcost="",
			adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",empno="",terms="",custName="",custName1="",empName="",taxtreatment="",
			shipId = "", shipCust = "", incoterm = "", origin = "", deductInv = "",currencyid="",currencyuseqt="0",projectid="",transportid="",orderdiscstatus = "0",discstatus = "0",
			shipstatus = "0",taxid = "",orderdisctype = "%",gst="0",jobNum="";
			
			/*InvoiceDet*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),notesexp = new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(),detDiscounttype = new ArrayList(), taxType = new ArrayList(),DETID= new ArrayList(),
					amount = new ArrayList(),edit_item=new ArrayList<>(),edit_qty=new ArrayList<>(),tlnno= new ArrayList();
			List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),index = new ArrayList(),
					 ordQty = new ArrayList(),delLnno = new ArrayList(),delLnQty = new ArrayList(),
							 delLnLoc = new ArrayList(),delLnBatch = new ArrayList(),delLnUom = new ArrayList(),
							 delLnItem = new ArrayList(), convcost= new ArrayList(), is_cogs_set = new ArrayList();
			List<Hashtable<String,String>> invoiceAttachmentList = null;
			List<Hashtable<String,String>> invoiceAttachmentInfoList = null;
			Hashtable<String,String> invoiceAttachment = null;
			UserTransaction ut = null;
			QuotesHDRService quoteshdrservice = new QuotesHDRServiceImpl();
			QuotesDETService quotesdetservice = new QuotesDETServiceImpl();
			QuotesHdrDAO quotesHdrDAO = new QuotesHdrDAO();
			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isAdded = false;
			boolean isAmntExceed = false;
			boolean Isconvcost=false;
			String result="", amntExceedMsg ="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0, detDiscounttypeCount  = 0,
					taxTypeCount  = 0,DETIDCount  = 0, amountCount  = 0, notesexpcount =0,editItemCount=0,editQtyCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0, idCount = 0, ordQtyCount = 0,tlnnoCount = 0,
					delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
					delLnItemCount=0,convcostCount = 0,is_cogs_setCount=0;
			/*others*/
			String personIncharge="",ctype="",fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
			try{
				////////////////
				List listQry = new ArrayList();
		    	listQry = (List) request.getSession().getAttribute("EST_LIST");
		    	
				if(isMultipart) {
								
				iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
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
			
						if (fileItem.getFieldName().equalsIgnoreCase("invoicequotes")) {
							
							invoicequotesNo = StrUtils.fString(fileItem.getString()).trim();
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
					
						if (fileItem.getFieldName().equalsIgnoreCase("expiry_date")) {
							
							dueDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							
							payTerms = StrUtils.fString(fileItem.getString()).trim();
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
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						
						String fileLocation = "C:/ATTACHMENTS/InvoiceQuotes" + "/"+ CustCode + "/"+ invoicequotesNo;
						String filetempLocation = "C:/ATTACHMENTS/InvoiceQuotes" + "/temp" + "/"+ CustCode + "/"+ invoicequotesNo;
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
						invoiceAttachment.put("CRAT",dateutils.getDateTime());
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
							tlnnoCount++;
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
							
							if(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())>0)
							{
							Isconvcost=true;
							convcost.add(costCount, StrUtils.fString(fileItem.getString()).trim());
							convcostCount++;
							}
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("IS_COGS_SET")) {							
							is_cogs_set.add(delLnItemCount, StrUtils.fString(fileItem.getString()).trim());
							is_cogs_setCount++;
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
				
				
				QuotesHDR quoteshdr = new QuotesHDR();
				
				quoteshdr.setPLANT(plant);
				quoteshdr.setCUSTNO(CustCode);
				quoteshdr.setQUOTESNO(invoicequotesNo);
				quoteshdr.setGINO(gino);
				quoteshdr.setDONO(dono);
				quoteshdr.setQUOTES_DATE(invoiceDate);
				quoteshdr.setDUE_DATE(dueDate);
				quoteshdr.setPAYMENT_TERMS(payTerms);
				quoteshdr.setEMPNO(empno);
				quoteshdr.setITEM_RATES(Short.valueOf(itemRates));
				quoteshdr.setDISCOUNT(Double.valueOf(discount));
				quoteshdr.setORDER_DISCOUNT(Double.valueOf(orderdiscount));
				quoteshdr.setDISCOUNT_TYPE(discountType);
				quoteshdr.setDISCOUNT_ACCOUNT(discountAccount);
				quoteshdr.setSHIPPINGCOST(Double.valueOf(shippingCost));
				quoteshdr.setADJUSTMENT(Double.valueOf(adjustment));
				quoteshdr.setSUB_TOTAL(Double.valueOf(subTotal));
				quoteshdr.setTOTAL_AMOUNT(Double.valueOf(totalAmount));
				quoteshdr.setQUOTES_STATUS(invoiceStatus);
				quoteshdr.setNOTE(note);
				quoteshdr.setTERMSCONDITIONS(terms);
				quoteshdr.setCRAT(dateutils.getDateTime());
				quoteshdr.setCRBY(username);
				quoteshdr.setUPAT(dateutils.getDateTime());
				quoteshdr.setSALES_LOCATION(salesloc);
				quoteshdr.setISEXPENSE(Short.valueOf(isexpense));
				quoteshdr.setTAXTREATMENT(taxtreatment);
				quoteshdr.setTAXAMOUNT(Double.valueOf(taxamount));
				quoteshdr.setSHIPPINGID(shipId);
				quoteshdr.setSHIPPINGCUSTOMER(shipCust);
				quoteshdr.setINCOTERMS(incoterm);
				quoteshdr.setORIGIN(origin);
				quoteshdr.setDEDUCT_INV(Short.valueOf(deductInv));
				quoteshdr.setCURRENCYUSEQT(Double.valueOf(currencyuseqt));
				quoteshdr.setORDERDISCOUNTTYPE(orderdisctype);
				quoteshdr.setTAXID(Integer.valueOf(taxid));
				quoteshdr.setISDISCOUNTTAX(Short.valueOf(discstatus));
				quoteshdr.setISORDERDISCOUNTTAX(Short.valueOf(orderdiscstatus));
				quoteshdr.setISSHIPPINGTAX(Short.valueOf(shipstatus));
				quoteshdr.setPROJECTID(Integer.valueOf(projectid));
				quoteshdr.setTRANSPORTID(0);
				quoteshdr.setOUTBOUD_GST(gst);
				quoteshdr.setCURRENCYID(currencyid);
				quoteshdr.setJobNum(Double.valueOf(jobNum));
				quoteshdr.setCREDITNOTESSTATUS(Short.valueOf("0"));

				
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int invoiceHdrId=0;
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!TranId.isEmpty())
					{
							quoteshdr.setID(Integer.valueOf(TranId));
							quoteshdr.setUPBY(username);
							invoiceHdrId = quoteshdrservice.updateQuotesHDR(quoteshdr, username);
						
					}
				}
				else {	
						
						invoiceHdrId = quoteshdrservice.addQuotesHDR(quoteshdr);
				}
				
				if(invoiceHdrId > 0) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						String convDiscount=""; 
						String convCost = String.valueOf((Double.parseDouble((String) cost.get(i)) / Double.parseDouble(currencyuseqt)));
						if(Isconvcost)
							convCost = String.valueOf((Double.parseDouble((String) convcost.get(i)) / Double.parseDouble(currencyuseqt)));
						if(!detDiscounttype.get(i).toString().contains("%"))
						{
							convDiscount = String.valueOf((Double.parseDouble((String) detDiscount.get(i)) / Double.parseDouble(currencyuseqt)));
						}
						else
							convDiscount = (String) detDiscount.get(i);
						String convAmount = String.valueOf((Double.parseDouble((String) amount.get(i)) / Double.parseDouble(currencyuseqt)));
						
						QuotesDET quotesdet = new QuotesDET();

						quotesdet.setPLANT(plant);
						quotesdet.setLNNO(lnno);
						if(cmd.equalsIgnoreCase("Edit"))
							quotesdet.setQUOTESHDRID(Integer.valueOf(TranId));				
						else
							quotesdet.setQUOTESHDRID(Integer.valueOf(invoiceHdrId));	
						quotesdet.setITEM((String) item.get(i));
						quotesdet.setACCOUNT_NAME((String) accountName.get(i));
						quotesdet.setQTY(Double.valueOf((String) qty.get(i)));
						quotesdet.setUNITPRICE(Double.valueOf(convCost));
						quotesdet.setTAX_TYPE((String) taxType.get(i));
						quotesdet.setAMOUNT(Double.valueOf(convAmount));
						quotesdet.setCRAT(dateutils.getDateTime());
						quotesdet.setCRBY(username);
						quotesdet.setUPAT(dateutils.getDateTime());
						quotesdet.setDISCOUNT(Double.valueOf(convDiscount));
						quotesdet.setDISCOUNT_TYPE((String) detDiscounttype.get(i));
						quotesdet.setCURRENCYUSEQT(Double.valueOf(currencyuseqt));
						
						if(loc.size() > 0) {
							quotesdet.setLOC((String) loc.get(i));
							quotesdet.setUOM((String) uom.get(i));
							quotesdet.setBATCH((String) batch.get(i));
						}
						if(notesexp.size() > 0) {
							quotesdet.setNOTE((String) notesexp.get(i));
						}
						
						if(cmd.equalsIgnoreCase("Edit"))
						{
							if(!TranId.isEmpty())
							{
								quotesdet.setUPBY(username);
								isAdded = quotesdetservice.DeleteQuotesDET(plant, Integer.valueOf(TranId));
								if(isAdded)
								quotesdetservice.addQuotesDET(quotesdet);
							}
						}else {
							quotesdetservice.addQuotesDET(quotesdet);
						}
						
					}
					
					int attchSize = invoiceAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						invoiceAttachment = new Hashtable<String, String>();
						invoiceAttachment = invoiceAttachmentList.get(i);
						if(cmd.equalsIgnoreCase("Edit"))
						invoiceAttachment.put("QUOTESHDRID", TranId);
						else
							invoiceAttachment.put("QUOTESHDRID", Integer.toString(invoiceHdrId));
						invoiceAttachmentInfoList.add(invoiceAttachment);
					}
					if(isAdded) {
						if(invoiceAttachmentInfoList.size() > 0)
							isAdded = quotesHdrDAO.addInvoiceQuotesAttachments(invoiceAttachmentInfoList, plant);
					}
					
					
					
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit"))
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_INVOICE_QUOTES);	
						else
						{
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_INVOICE_QUOTES);
						}
						
							
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(invoiceDate));														
						htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
						String billqty = String.valueOf((String) qty.get(i));
						htMovHis.put(IDBConstants.QTY, billqty);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, invoicequotesNo);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						if(!jobNum.isEmpty())
							htMovHis.put("REMARKS",jobNum);
						else
							htMovHis.put("REMARKS",dono);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, plant);
						htMovChk.put("DIRTYPE", TransactionConstants.EDIT_INVOICE_QUOTES);
						htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
						htMovChk.put(IDBConstants.QTY, billqty);
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoicequotesNo);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE_QUOTES%' ");
						if(!isAdded)	
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
				}

				
				if(isAdded) {
					DbBean.CommitTran(ut);
					if(cmd.equalsIgnoreCase("Edit"))
						result = "Invoice quotes updated successfully";
					else
					{
						
						new TblControlUtil().updateTblControlIESeqNoreturn(plant, "INVOICE_QUOTES", "INQ", invoicequotesNo);
						result = "Invoice quotes created successfully";
					}
						
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Invoice quotes not created";
				}
				
				if(result.equalsIgnoreCase("Invoice quotes not created"))
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
						
						response.sendRedirect("../invoicequotes/new?result="+ result);
					}
				}
				else {
					if (ajax) {
						try {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
							if ("invoice".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								//invoicePDFGenration(request, response, invoiceNo, "Edit".equalsIgnoreCase(cmd) ? Integer.parseInt(TranId) : invoiceHdrId);
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
							resultJson.put("MESSAGE", "Invoice Quotes Added Successfully and email not sent");
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
							
						}
					}else {
						response.sendRedirect("../invoicequotes/summary?result="+ result);/* Redirect to Invoice Summary page */
					}
				}
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 if (ajax) {
					 JSONObject resultJson = new JSONObject();
					resultJson.put("MESSAGE", "Invoice quotes not created. " + ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }else {
					response.sendRedirect("../invoicequotes/new?result="+ e.getMessage());
				 }
			}			
		
		
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("summary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceQuotesSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("new")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createInvoiceQuotes.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("detail")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceQuotesDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	if(action.equalsIgnoreCase("edit")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editInvoiceQuotes.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("copy")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editInvoiceQuotes.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

	
	private Map getOutstandingAmountForCustomer(String custCode, double orderAmount, String plant) throws Exception{
		String outstdamt = "", creditLimitBy ="", creditLimit = "", creditBy = "";
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

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}
