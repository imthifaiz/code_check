package com.track.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BomDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.PoDet;
import com.track.db.object.PoHdr;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ExceptionUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.SupplierCreditUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.db.util.ExceptionUtil.Result;
import com.track.gates.DbBean;
import com.track.service.Costofgoods;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class InvoiceAccServlet
 */
@WebServlet("/ItemReturnServlet")
@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemReturnServlet  extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.InvoiceServlet_PRINTPLANTMASTERLOG;
	String action = "";
	ArrayList ALIssueddetails = new ArrayList();
	String autoinverr="";
	private String xmlStr = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = "";
		String baction = "";
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
		
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
		TblControlDAO _TblControlDAO = new TblControlDAO();
		
		 if (baction.equalsIgnoreCase("BulkReceive")) {
			xmlStr = "";
			try {
				xmlStr = processOrderBulkReceivingByWMS(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		 if(action.equalsIgnoreCase("Save")) {
			/* Hdr*/
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
			/*Det*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),addcost = new ArrayList(),addtype = new ArrayList(),Ucost = new ArrayList(),notesexp = new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(),detDiscounttype = new ArrayList(), taxType = new ArrayList(),DETID= new ArrayList(),
					amount = new ArrayList(),edit_item=new ArrayList<>(),edit_qty=new ArrayList<>(),tlnno= new ArrayList(),dolnno= new ArrayList();
			List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),index = new ArrayList(),
					 ordQty = new ArrayList(),delLnno = new ArrayList(),delLnQty = new ArrayList(),
							 delLnLoc = new ArrayList(),delLnBatch = new ArrayList(),delLnUom = new ArrayList(),
							 delLnItem = new ArrayList(), convcost= new ArrayList(), is_cogs_set = new ArrayList();
			List<Hashtable<String,String>> productDetInfoList = null;
			List<Hashtable<String,String>> productHdrInfoList = null;
			Hashtable<String,String> productDetInfo = null;
			Hashtable<String,String> invoiceAttachment = null;
			UserTransaction ut = null;
			InvoiceUtil invoiceUtil = new InvoiceUtil();
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isAdded = false;
			boolean isAmntExceed = false;
			boolean Isconvcost=false;
			String result="", amntExceedMsg ="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0,addcostCount  = 0,addtypeCount  = 0,UcostCount  = 0,dolnnoCount = 0, costCount  = 0, detDiscountCount  = 0, detDiscounttypeCount  = 0,
					taxTypeCount  = 0,DETIDCount  = 0, amountCount  = 0, notesexpcount =0,editItemCount=0,editQtyCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0, idCount = 0, ordQtyCount = 0,
					delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
					delLnItemCount=0;
			/*others*/
			String personIncharge="",ctype="",fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
			try{
				List listQry = new ArrayList();
		    	listQry = (List) request.getSession().getAttribute("EST_LIST");
		    	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
		    	String ISINVENTORYMINQTY = DoHdrDAO.getISINVENTORYMINQTY(plant);
				if(isMultipart) {
								
				iterator = items.iterator();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* Hdr*/
					if (fileItem.isFormField()) {
						
//						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
//							
//							CustCode = StrUtils.fString(fileItem.getString()).trim();
//						}
//						
//						if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER")) {
//							
//							custName = StrUtils.fString(fileItem.getString()).trim();
//						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							
							CustCode = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
							
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
					
					
						if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
							shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
							adjustment = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							subTotal = String.valueOf(( Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							totalAmount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxamount")) {
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
				
					
					/*Det*/
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
							}
							convcost.add(costCount, StrUtils.fString(fileItem.getString()).trim());
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("IS_COGS_SET")) {							
							is_cogs_set.add(delLnItemCount, StrUtils.fString(fileItem.getString()).trim());
						}
					}
				}
				}
				
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
				
				
				productDetInfoList = new ArrayList<Hashtable<String,String>>();
				productHdrInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable productHdr =new Hashtable(); 
				productHdr.put("PLANT", plant);
//				productHdr.put("CUSTNO", CustCode);
				productHdr.put("VENDNO", CustCode);
				productHdr.put("VNAME", custName);
				productHdr.put("IDRNO", invoiceNo);
				productHdr.put("RETURN_DATE", invoiceDate);
				productHdr.put("DUE_DATE", dueDate);
				productHdr.put("PAYMENT_TERMS", payTerms);
				productHdr.put("EMPNO", empno);
				productHdr.put("ORDERTYPE", ORDERTYPE);
				productHdr.put("ITEM_RATES", itemRates);
				productHdr.put("DISCOUNT", discount);
				productHdr.put("ORDER_DISCOUNT", orderdiscount);
				productHdr.put("DISCOUNT_TYPE", discountType);
				productHdr.put("DISCOUNT_ACCOUNT", discountAccount);
				productHdr.put("SHIPPINGCOST", shippingCost);
				productHdr.put("ADJUSTMENT", adjustment);
				productHdr.put("SUB_TOTAL", subTotal);
				productHdr.put("TOTAL_AMOUNT", totalAmount);
				productHdr.put("RETURN_STATUS", invoiceStatus);
				productHdr.put("NOTE", note);
				productHdr.put("TERMSCONDITIONS", terms);
				productHdr.put("CRAT",DateUtils.getDateTime());
				productHdr.put("CRBY",username);
				productHdr.put("UPAT",DateUtils.getDateTime());
				productHdr.put("TAXTREATMENT",taxtreatment);
				productHdr.put("TAXAMOUNT",taxamount);
				productHdr.put("SHIPPINGID",shipId);
				productHdr.put("SHIPPINGCUSTOMER",shipCust);
				productHdr.put("CURRENCYUSEQT",currencyuseqt);
				productHdr.put("ORDERDISCOUNTTYPE",orderdisctype);
				productHdr.put("TAXID",taxid);
				productHdr.put("ISDISCOUNTTAX",discstatus);
				productHdr.put("ISORDERDISCOUNTTAX",orderdiscstatus);
				productHdr.put("ISSHIPPINGTAX",shipstatus);
				productHdr.put("TRANSPORTID",transportid);
				productHdr.put("INBOUND_GST",gst);
				productHdr.put(IDBConstants.CURRENCYID, currencyid);
				productHdr.put("JobNum",jobNum);
				productHdr.put("SHIPCONTACTNAME",shipcontactname);
				productHdr.put("SHIPDESGINATION",shipdesgination);
				productHdr.put("SHIPWORKPHONE",shipworkphone);
				productHdr.put("SHIPHPNO",shiphpno);
				productHdr.put("SHIPEMAIL",shipemail);
				productHdr.put("SHIPCOUNTRY",shipcountry);
				productHdr.put("SHIPADDR1",shipaddr1);
				productHdr.put("SHIPADDR2",shipaddr2);
				productHdr.put("SHIPADDR3",shipaddr3);
				productHdr.put("SHIPADDR4",shipaddr4);
				productHdr.put("SHIPSTATE",shipstate);
				productHdr.put("SHIPZIP",shipzip);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int productHdrId=0;
				productHdr.put("CREDITNOTESSTATUS","0");
				productHdr.put("TOTAL_PAYING","0");
				productHdrId = new ItemMstDAO().addProductReturnHdr(productHdr, plant);
				productHdr.put("PRODUCTHDRID",productHdrId);
				 productHdrInfoList.add(productHdr);
				
				if(productHdrId > 0) {
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
						
						productDetInfo = new Hashtable<String, String>();
						productDetInfo.put("PLANT", plant);
						productDetInfo.put("LNNO", Integer.toString(lnno));
						productDetInfo.put("PRODUCTHDRID", Integer.toString(productHdrId));
						productDetInfo.put("ITEM", (String) item.get(i));
						productDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						productDetInfo.put("QTY", (String) qty.get(i));
						productDetInfo.put("UNITPRICE", convCost);
						productDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						productDetInfo.put("AMOUNT", convAmount);
						productDetInfo.put("CRAT",DateUtils.getDateTime());
						productDetInfo.put("CRBY",username);
						productDetInfo.put("UPAT",DateUtils.getDateTime());
						productDetInfo.put("DISCOUNT", convDiscount);
						productDetInfo.put("ADDONAMOUNT", (String)addcost.get(i));
						productDetInfo.put("ADDONTYPE", (String)addtype.get(i));
						double uconv = ((Double.valueOf((String)Ucost.get(i)))/(Double.valueOf(currencyuseqt)));
						productDetInfo.put("UNITCOST", (String.valueOf(uconv)));
						productDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
						if(!dono.equalsIgnoreCase("") && !gino.equalsIgnoreCase("")){
//							if(deductInv.equalsIgnoreCase("1") && !dono.equalsIgnoreCase("") && !gino.equalsIgnoreCase("")){
							if(uom.size() > 0)
							productDetInfo.put("UOM", (String) uom.get(i));
						}else if(!gino.equalsIgnoreCase("")){
							if(uom.size() > 0)
								productDetInfo.put("UOM", (String) uom.get(i));
						}else {
							productDetInfo.put("UOM", "");	
						}
						productDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
						productDetInfo.put("IS_COGS_SET", "N");
						if(loc.size() > 0) {
							productDetInfo.put("LOC", (String) loc.get(i));
							
							productDetInfo.put("BATCH", (String) batch.get(i));
						}
						if(notesexp.size() > 0) {
							productDetInfo.put("NOTE", (String) notesexp.get(i));
						}else {
						}
						productDetInfoList.add(productDetInfo);
					}
					isAdded = new ItemMstDAO().addProductReturnDet(productDetInfoList, plant);
					
					
					if(isAdded) {
						for(int i =0 ; i < item.size() ; i++){
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						htMovHis.put("DIRTYPE", TransactionConstants.PRD_RETURN);
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
						htMovChk.put("DIRTYPE", TransactionConstants.PRD_RETURN);
						htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
						htMovChk.put(IDBConstants.QTY, billqty);
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%PRODUCT_RETURN%' ");
						if(!isAdded)	
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
						}
					}
				}
				
				boolean flag = false;
				Map receiveMaterial_HM = null;
				process: 
				if(isAdded && invoiceStatus.equalsIgnoreCase("PROCESSED") && deductInv.equalsIgnoreCase("1")) {
					String ginoinvno = _TblControlDAO.getNextOrder(plant,username,"GINO");
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
//						htCondiShipHis.put("INVOICENO", gino);
						htCondiShipHis.put("INVOICENO", ginoinvno);
						htCondiShipHis.put("STATUS","C");
						htCondiShipHis.put("BATCH",(String) batch.get(i));
						htCondiShipHis.put("dolno", Integer.toString(lnno));
						htCondiShipHis.put("LOC1", (String) loc.get(i));
						htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
						boolean isexists = new ShipHisDAO().isExisit(htCondiShipHis, "");
						
						double costdouble = Double.valueOf((String) cost.get(i))/Double.valueOf(currencyuseqt);
						String scost = String.valueOf(costdouble);
						
						
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
				
				if(isAdded) {
					DbBean.CommitTran(ut);
						ShipHisDAO shiphstdao = new ShipHisDAO();
						Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
						htTrandId1.put(IConstants.INVOICENO, invoiceNo);
						htTrandId1.put(IConstants.PLANT, plant);
						shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS	 						
//						new TblControlUtil().updateTblControlIESeqNo(plant, "ITEMRETURN", "IRN", invoiceNo);
						if(alertitem.equalsIgnoreCase("")) {
							result = "Product Returned successfully";
							isAdded=_TblControlDAO.updateSeqNo("ITEMRETURN",plant);
							new TblControlUtil().updateTblControlIESeqNo(plant, "GINO", "GI", gino);
							String Creditstatus = createDebitnode(request, response, productHdrInfoList, productDetInfoList, gino, plant, username);
							
							 String parentplnt = new PlantMstDAO().CheckPlantDesc(custName);//imthi
							 if(parentplnt == null) parentplnt = "0";
							 String currentplntdesc = new PlantMstDAO().getcmpyname(plant);//imthi
							 
							 String ReceiveHdr = createreceiveHdr(request, response, productHdrInfoList, productDetInfoList, gino, plant, username,currentplntdesc,parentplnt);
							
							
						}else {
							result = "Product Returned successfully <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>";
						}
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Product Not Returned";
				}
				
				if(result.equalsIgnoreCase("Product Not Returned"))
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
					}
				}else {
						response.sendRedirect("../productreturn/summary?result="+ result);/* Redirect to Summary page */
				}
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
				 e.printStackTrace();
				 if (ajax) {
					 JSONObject resultJson = new JSONObject();
					resultJson.put("MESSAGE", "Product not Returned. " + ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				 }else {
						response.sendRedirect("../productreturn/new?result="+ e.getMessage());
				 }
			}			
		
		}
		  
		
	}
	
	private String processOrderBulkReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String chkdPoNo = "",collectionTime = "",orderLNo = "",receivingQty = "",ITEM_BATCH = "",item = "",ITEM_DESCRIPTION = "",ISNONSTKFLG="";
		String xmlStr = "" ,QTYOR="",ICRNO = "",CUST_NAME = "",jobNum = "",UOMQTY="";
		String REMARKS = "",collectionDate = "",remark= "",ITEM_LOC= "",REF = "",EXPIREDATE="",RECVDATE = "",invoiceNum= "",strTranDate="",strRecvDate="",GRNO="",UNITMO="",
		createBill="",CUST_CODE="",priceval="",CLEARAGENT="",CLEARANCETYPE="",TRANSPORTID="",CONTACT_NAME="",ICRNOHDR="",UNITCOST="";
		double unitprice=0,totalprice=0,totalqty=0;
		String[]  checkedOrder;
		UserLocUtil uslocUtil = new UserLocUtil();
		Map receiveMaterial_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		Map checkedPOS = new HashMap();
		Boolean allChecked = false,fullReceive = false;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject resultJson = new JSONObject();
		POUtil _POUtil = new POUtil();
		List<Hashtable<String,String>> productDetInfoList = null;
		List<Hashtable<String,String>> productHdrInfoList = null;
		Hashtable<String,String> productDetInfo = null;
		Hashtable productHdr =new Hashtable();
		try {
			
			HttpSession session = request.getSession(false);
			String PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
			ICRNO = StrUtils.fString(request.getParameter("ICRNO"));
			ICRNOHDR = StrUtils.fString(request.getParameter("ICRNOHDR"));
			String[] chkdPoNos  = request.getParameterValues("chkdPoNo");
			RECVDATE = StrUtils.fString(request.getParameter("RECVDATE_0"));
			REF = StrUtils.fString(request.getParameter("REF"));
			String LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REMARK2"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
			collectionDate = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			remark = StrUtils.fString(request.getParameter("reamark"));
			GRNO = StrUtils.fString(request.getParameter("GRNO"));
			createBill = StrUtils.fString(request.getParameter("createBill"));
			CUST_CODE = StrUtils.fString(request.getParameter("CUST_CODE"));
			CLEARAGENT = StrUtils.fString(request.getParameter("clearingagent"));
			CLEARANCETYPE = StrUtils.fString(request.getParameter("typeofclearance"));
			TRANSPORTID = StrUtils.fString(request.getParameter("TRANSPORTID"));
			CONTACT_NAME = StrUtils.fString(request.getParameter("CONTACT_NAME"));
			if (RECVDATE.length()>5)
				strTranDate    = RECVDATE.substring(6)+"-"+ RECVDATE.substring(3,5)+"-"+RECVDATE.substring(0,2);
			    strRecvDate    = RECVDATE.substring(0,2)+"/"+ RECVDATE.substring(3,5)+"/"+RECVDATE.substring(6);
	
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullReceive")!=null){
				fullReceive = true;
			}
			
			DateUtils dateUtils = new DateUtils();
			String fromdates = dateUtils.parseDateddmmyyyy(strRecvDate);
			String time = DateUtils.getTimeHHmm();
			String orderdate = fromdates+time+"12";
			LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
			String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tran = formattedDate;
			
			productDetInfoList = new ArrayList<Hashtable<String,String>>();
			productHdrInfoList = new ArrayList<Hashtable<String,String>>();
			int amount=0;
			double totalAmount = 0;
            if (chkdPoNos != null)    {     
				for (int i = 0; i < chkdPoNos.length; i++)       { 
					orderLNo = chkdPoNos[i];
					receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
					ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"+orderLNo));
					EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_"+orderLNo));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					checkedPOS.put(orderLNo, receivingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedPOS", checkedPOS);
            }
            Boolean transactionHandler = true;
    		ArrayList PODetails = null;
    		ArrayList alPoHdr = null;

    		Hashtable htPoDet = new Hashtable();
    		String queryPoDet = "D.item,( select top 1 itemdesc from ["+PLANT+"_ITEMMST] a where D.PLANT =a.PLANT and D.ITEM =a.ITEM ) as ItemDesc,D.QTY,D.QTYRC,D.UOM,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM A where A.UOM=D.UOM),1) UOMQTY,(D.UNITCOST*D.CURRENCYUSEQT) as UNITCOST,(D.UNITPRICE*D.CURRENCYUSEQT) as UNITPRICE";
    		process: 	
			if (chkdPoNos != null)    {     
				for (int i = 0; i < chkdPoNos.length; i++)       { 
					orderLNo = chkdPoNos[i];
					receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
						ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"+orderLNo));
						EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_"+orderLNo));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					boolean isvalidlocforUser = uslocUtil .isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
		            if (!isvalidlocforUser) {
		                throw new Exception("Receiving Loc :" + ITEM_LOC + " is not User Assigned Location");
		            }
					htPoDet.put("RECEIVEHDRID", ICRNOHDR);
		    		htPoDet.put(IConstants.PLANT, PLANT);
		    		htPoDet.put("LNNO", orderLNo);
					PODetails = _POUtil.getPorductReceiveDetails(queryPoDet, htPoDet);
					if (PODetails.size() > 0) {	

							Map map1 = (Map) PODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTY");
							UNITMO = (String) map1.get("UOM");
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
							UOMQTY= (String) map1.get("UOMQTY");
							UNITCOST= (String) map1.get("UNITCOST");
//							unitprice = Double.parseDouble((String) map1.get("UNITCOST"));//doubt in passing priceval input//insertGRNtoBill
							unitprice = Double.parseDouble((String) map1.get("UNITPRICE"));
							PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
							StrUtils strUtils     = new StrUtils();
							String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
							totalprice=totalprice+(unitprice* Float.parseFloat(receivingQty));
							priceval=String.valueOf(totalprice);
							double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							totalqty=totalqty+Float.parseFloat(receivingQty);
							
//							Hashtable ht = new Hashtable();
//							ht.put("PLANT", PLANT);
//							ht.put("ID", ICRNOHDR);
//							List invoiceHdrList =  new ItemMstDAO().getConvProductReceiveHdrById(ht);
//							if(invoiceHdrList.size()>0){
//								Map Hdr=(Map)invoiceHdrList.get(0);
//								productHdr.put("PLANT", (String) Hdr.get("PLANT"));
//								productHdr.put("CustCode",(String) Hdr.get("CustCode"));
//								productHdr.put("CustName", (String) Hdr.get("CustName"));
//								productHdr.put("ICRNO", (String) Hdr.get("ICRNO"));
//								productHdr.put("RECEIVE_DATE", (String) Hdr.get("RECEIVE_DATE"));
//								productHdr.put("PAYMENT_TERMS", (String) Hdr.get("PAYMENT_TERMS"));
//								productHdr.put("EMPNO", (String) Hdr.get("EMPNO"));
//								productHdr.put("ITEM_RATES", (String) Hdr.get("ITEM_RATES"));
//								productHdr.put("DISCOUNT", (String) Hdr.get("DISCOUNT"));
//								productHdr.put("ORDER_DISCOUNT", (String) Hdr.get("ORDER_DISCOUNT"));
//								productHdr.put("DISCOUNT_TYPE", (String) Hdr.get("DISCOUNT_TYPE"));
//								productHdr.put("TAX_STATUS", (String) Hdr.get("TAX_STATUS"));
//								productHdr.put("DISCOUNT_ACCOUNT", (String) Hdr.get("DISCOUNT_ACCOUNT"));
//								productHdr.put("SHIPPINGCOST",(String) Hdr.get("SHIPPINGCOST"));
//								productHdr.put("ADJUSTMENT", (String) Hdr.get("ADJUSTMENT"));
//								productHdr.put("SUB_TOTAL", (String) Hdr.get("SUB_TOTAL"));
//								productHdr.put("TOTAL_AMOUNT", (String) Hdr.get("TOTAL_AMOUNT"));
//								productHdr.put("RECEIVE_STATUS", (String) Hdr.get("RECEIVE_STATUS"));
//								productHdr.put("NOTE", (String) Hdr.get("NOTE"));
//								productHdr.put("CRAT",DateUtils.getDateTime());
//								productHdr.put("CRBY",LOGIN_USER);
//								productHdr.put("UPAT",DateUtils.getDateTime());
//								productHdr.put("TAXTREATMENT",(String) Hdr.get("TAXTREATMENT"));
//								productHdr.put("TAXAMOUNT",(String) Hdr.get("TAXAMOUNT"));
//								productHdr.put("CURRENCYUSEQT",(String) Hdr.get("CURRENCYUSEQT"));
//								productHdr.put("ORDERDISCOUNTTYPE",(String) Hdr.get("ORDERDISCOUNTTYPE"));
//								productHdr.put("TAXID",(String) Hdr.get("TAXID"));
//								productHdr.put("ISDISCOUNTTAX",(String) Hdr.get("ISDISCOUNTTAX"));
//								productHdr.put("ISORDERDISCOUNTTAX",(String) Hdr.get("ISORDERDISCOUNTTAX"));
//								productHdr.put("ISSHIPPINGTAX",(String) Hdr.get("ISSHIPPINGTAX"));
//								productHdr.put("TRANSPORTID",(String) Hdr.get("TRANSPORTID"));
//								productHdr.put("INBOUND_GST",(String) Hdr.get("INBOUND_GST"));
//								productHdr.put(IDBConstants.CURRENCYID, (String) Hdr.get("CURRENCYID"));
//								productHdr.put("JobNum",(String) Hdr.get("JobNum"));;
//								productHdr.put("SHIPPINGID",(String) Hdr.get("SHIPPINGID"));
//								productHdr.put("SHIPPINGCUSTOMER",(String) Hdr.get("SHIPPINGCUSTOMER"));
//								productHdr.put("SHIPCONTACTNAME",(String) Hdr.get("SHIPCONTACTNAME"));
//								productHdr.put("SHIPDESGINATION",(String) Hdr.get("SHIPDESGINATION"));
//								productHdr.put("SHIPWORKPHONE",(String) Hdr.get("SHIPWORKPHONE"));
//								productHdr.put("SHIPHPNO",(String) Hdr.get("SHIPHPNO"));
//								productHdr.put("SHIPEMAIL",(String) Hdr.get("SHIPEMAIL"));
//								productHdr.put("SHIPCOUNTRY",(String) Hdr.get("SHIPCOUNTRY"));
//								productHdr.put("SHIPADDR1",(String) Hdr.get("SHIPADDR1"));
//								productHdr.put("SHIPADDR2",(String) Hdr.get("SHIPADDR2"));
//								productHdr.put("SHIPADDR3",(String) Hdr.get("SHIPADDR3"));
//								productHdr.put("SHIPADDR4",(String) Hdr.get("SHIPADDR4"));
//								productHdr.put("SHIPSTATE",(String) Hdr.get("SHIPSTATE"));
//								productHdr.put("SHIPZIP",(String) Hdr.get("SHIPZIP"));
//								productHdr.put("CREDITNOTESSTATUS",(String) Hdr.get("CREDITNOTESSTATUS"));
//								productHdr.put("TOTAL_PAYING",(String) Hdr.get("TOTAL_PAYING"));
//								 productHdrInfoList.add(productHdr);
//							}
							
							Hashtable ht = new Hashtable();
							ht.put("PLANT", PLANT);
							ht.put("ID", ICRNOHDR);
							ht.remove("ID");
							ht.put("LNNO", orderLNo);
							ht.put("RECEIVEHDRID", ICRNOHDR);
							List invoiceDetList =  new ItemMstDAO().getConvProductReceiveDetByHdrIdbyLnno(ht);
							if(invoiceDetList.size()>0){
							for(int d =0; d<invoiceDetList.size(); d++) {   
						  		Map m=(Map)invoiceDetList.get(d);
						  		productDetInfo = new Hashtable<String, String>();
						  		productDetInfo.put("PLANT", (String) m.get("PLANT"));
								productDetInfo.put("LNNO", (String) m.get("LNNO"));
								productDetInfo.put("ITEM", (String) m.get("ITEM"));
								productDetInfo.put("ACCOUNT_NAME", (String) m.get("ACCOUNT_NAME"));
//								productDetInfo.put("QTY", (String) m.get("QTY"));
								productDetInfo.put("QTY", receivingQty);
								
								double reqty = Double.parseDouble(receivingQty); 
								double unitPrice = Double.parseDouble((String) m.get("UNITPRICE")); 
								double ritemDiscount = Double.parseDouble((String) m.get("DISCOUNT")); 
								int rqty =  (int) reqty;
								int cost = (int) unitPrice; 
								int itemDiscount =(int) ritemDiscount;
								String discounType = (String) m.get("DISCOUNT_TYPE");
								int itemDiscountval=itemDiscount;
								if(discounType.equalsIgnoreCase("%")){
									 itemDiscountval = ((rqty*cost)*(itemDiscount/100));
									 itemDiscount = itemDiscount;
								}
								amount = (((rqty*cost)-itemDiscountval));
								String ramount = String.valueOf(amount);
								totalAmount += amount;
								
								
								productDetInfo.put("UNITPRICE", (String) m.get("UNITPRICE"));
								productDetInfo.put("TAX_TYPE", (String) m.get("TAX_TYPE"));
//								productDetInfo.put("AMOUNT", (String) m.get("AMOUNT"));
								productDetInfo.put("AMOUNT", ramount);
								productDetInfo.put("CRAT",(String) m.get("CRAT"));
								productDetInfo.put("CRBY",(String) m.get("CRBY"));
								productDetInfo.put("UPAT",DateUtils.getDateTime());
								productDetInfo.put("DISCOUNT", (String) m.get("DISCOUNT"));
								productDetInfo.put("UNITCOST", (String) m.get("UNITCOST"));
								productDetInfo.put("DISCOUNT_TYPE", (String) m.get("DISCOUNT_TYPE"));
								productDetInfo.put("UOM", (String) m.get("UOM"));	
								productDetInfo.put(IDBConstants.CURRENCYUSEQT, (String) m.get("CURRENCYUSEQT"));
								productDetInfo.put("LOC", ITEM_LOC);
								productDetInfo.put("BATCH", ITEM_BATCH);
								productDetInfo.put("NOTE", "");
								productDetInfoList.add(productDetInfo);
						  		
							}
							}
							
							ht.clear();
							ht.put("PLANT", PLANT);
							ht.put("ID", ICRNOHDR);
							String subtl = String.valueOf(totalAmount);
							String totval = "";
							double taxTotal=0;
							double totamt=0;
							double overalltotamt=0;
							double taxsubtotal=0;
							List invoiceHdrList =  new ItemMstDAO().getConvProductReceiveHdrById(ht);
							if(invoiceHdrList.size()>0){
								Map Hdr=(Map)invoiceHdrList.get(0);
								double gst = Double.parseDouble((String) Hdr.get("INBOUND_GST")); 
								double dis = Double.parseDouble((String) Hdr.get("DISCOUNT")); 
								String isdiscounttax = ((String) Hdr.get("ISDISCOUNTTAX")); 
								String item_rates = ((String) Hdr.get("ITEM_RATES")); 
								if(item_rates.equalsIgnoreCase("1"))
								 taxsubtotal = ((100 *totalAmount)/(100 + gst));
								else
									taxsubtotal = totalAmount;
								 subtl = String.valueOf(taxsubtotal);
								
								if(isdiscounttax.equalsIgnoreCase("1"))
									totamt = taxsubtotal - dis;
								else 
									totamt = taxsubtotal;
									
								taxTotal = ((totamt*gst)/100);
								
								overalltotamt = totamt+taxTotal;
								
								
								productHdr.put("PLANT", (String) Hdr.get("PLANT"));
								productHdr.put("CustCode",(String) Hdr.get("CustCode"));
								productHdr.put("CustName", (String) Hdr.get("CustName"));
								productHdr.put("ICRNO", (String) Hdr.get("ICRNO"));
								productHdr.put("RECEIVE_DATE", (String) Hdr.get("RECEIVE_DATE"));
								productHdr.put("PAYMENT_TERMS", (String) Hdr.get("PAYMENT_TERMS"));
								productHdr.put("EMPNO", (String) Hdr.get("EMPNO"));
								productHdr.put("ITEM_RATES", (String) Hdr.get("ITEM_RATES"));
								productHdr.put("DISCOUNT", (String) Hdr.get("DISCOUNT"));
								productHdr.put("ORDER_DISCOUNT", (String) Hdr.get("ORDER_DISCOUNT"));
								productHdr.put("DISCOUNT_TYPE", (String) Hdr.get("DISCOUNT_TYPE"));
								productHdr.put("TAX_STATUS", (String) Hdr.get("TAX_STATUS"));
								productHdr.put("DISCOUNT_ACCOUNT", (String) Hdr.get("DISCOUNT_ACCOUNT"));
//								productHdr.put("SUB_TOTAL", (String) Hdr.get("SUB_TOTAL"));
//								double totalvalue = ((totalAmount)- (Double.parseDouble((String) Hdr.get("ORDER_DISCOUNT"))) - (Double.parseDouble((String) Hdr.get("DISCOUNT"))) + (Double.parseDouble((String) Hdr.get("TAXAMOUNT"))) + (Double.parseDouble((String) Hdr.get("SHIPPINGCOST"))) + (Double.parseDouble((String) Hdr.get("ADJUSTMENT"))));
//								totval = toString().valueOf(totalvalue);
//								productHdr.put("TOTAL_AMOUNT", (String) Hdr.get("TOTAL_AMOUNT"));
//								productHdr.put("SHIPPINGCOST",(String) Hdr.get("SHIPPINGCOST"));
//								productHdr.put("ADJUSTMENT", (String) Hdr.get("ADJUSTMENT"));
//								productHdr.put("TAXAMOUNT",(String) Hdr.get("TAXAMOUNT"));
								productHdr.put("TAXAMOUNT",String.valueOf(taxTotal));
								productHdr.put("SHIPPINGCOST","0");
								productHdr.put("ADJUSTMENT", "0");
								productHdr.put("SUB_TOTAL", subtl);
								productHdr.put("TOTAL_AMOUNT", String.valueOf(overalltotamt));
								productHdr.put("RECEIVE_STATUS", (String) Hdr.get("RECEIVE_STATUS"));
								productHdr.put("NOTE", (String) Hdr.get("NOTE"));
								productHdr.put("CRAT",DateUtils.getDateTime());
								productHdr.put("CRBY",LOGIN_USER);
								productHdr.put("UPAT",DateUtils.getDateTime());
								productHdr.put("TAXTREATMENT",(String) Hdr.get("TAXTREATMENT"));
								productHdr.put("CURRENCYUSEQT",(String) Hdr.get("CURRENCYUSEQT"));
								productHdr.put("ORDERDISCOUNTTYPE",(String) Hdr.get("ORDERDISCOUNTTYPE"));
								productHdr.put("TAXID",(String) Hdr.get("TAXID"));
								productHdr.put("ISDISCOUNTTAX",(String) Hdr.get("ISDISCOUNTTAX"));
								productHdr.put("ISORDERDISCOUNTTAX",(String) Hdr.get("ISORDERDISCOUNTTAX"));
								productHdr.put("ISSHIPPINGTAX",(String) Hdr.get("ISSHIPPINGTAX"));
								productHdr.put("TRANSPORTID",(String) Hdr.get("TRANSPORTID"));
								productHdr.put("INBOUND_GST",(String) Hdr.get("INBOUND_GST"));
								productHdr.put(IDBConstants.CURRENCYID, (String) Hdr.get("CURRENCYID"));
								productHdr.put("JobNum",(String) Hdr.get("JobNum"));;
								productHdr.put("SHIPPINGID",(String) Hdr.get("SHIPPINGID"));
								productHdr.put("SHIPPINGCUSTOMER",(String) Hdr.get("SHIPPINGCUSTOMER"));
								productHdr.put("SHIPCONTACTNAME",(String) Hdr.get("SHIPCONTACTNAME"));
								productHdr.put("SHIPDESGINATION",(String) Hdr.get("SHIPDESGINATION"));
								productHdr.put("SHIPWORKPHONE",(String) Hdr.get("SHIPWORKPHONE"));
								productHdr.put("SHIPHPNO",(String) Hdr.get("SHIPHPNO"));
								productHdr.put("SHIPEMAIL",(String) Hdr.get("SHIPEMAIL"));
								productHdr.put("SHIPCOUNTRY",(String) Hdr.get("SHIPCOUNTRY"));
								productHdr.put("SHIPADDR1",(String) Hdr.get("SHIPADDR1"));
								productHdr.put("SHIPADDR2",(String) Hdr.get("SHIPADDR2"));
								productHdr.put("SHIPADDR3",(String) Hdr.get("SHIPADDR3"));
								productHdr.put("SHIPADDR4",(String) Hdr.get("SHIPADDR4"));
								productHdr.put("SHIPSTATE",(String) Hdr.get("SHIPSTATE"));
								productHdr.put("SHIPZIP",(String) Hdr.get("SHIPZIP"));
								productHdr.put("CREDITNOTESSTATUS",(String) Hdr.get("CREDITNOTESSTATUS"));
								productHdr.put("TOTAL_PAYING",(String) Hdr.get("TOTAL_PAYING"));
								 productHdrInfoList.add(productHdr);
							}
							
							
					}
					   receiveMaterial_HM = new HashMap();
                       receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                       receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                       receiveMaterial_HM.put(IConstants.ITEM, item);
                       receiveMaterial_HM.put("UNITCOST", UNITCOST);
                       receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
                       receiveMaterial_HM.put(IConstants.PODET_PONUM, ICRNO);
                       receiveMaterial_HM.put(IConstants.PODET_POLNNO, orderLNo);
                       receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                       receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
//                       receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, ICRNO));
                       receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, CUST_CODE);
                       receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.INV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.RECV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
                       receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
                       receiveMaterial_HM.put(IConstants.CREATED_AT,  (String) DateUtils.getDateTime());
                       receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
//                       receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum( PLANT, ICRNO));
                       receiveMaterial_HM.put(IConstants.JOB_NUM, jobNum);
                       receiveMaterial_HM.put(IConstants.REMARKS, REF);
                      receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
                      receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
                      receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
                      receiveMaterial_HM.put(IConstants.GRNO, GRNO);
                      receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
                      receiveMaterial_HM.put("UOMQTY", UOMQTY);
                      receiveMaterial_HM.put(IDBConstants.UOM, UNITMO);
                       _POUtil.setmLogger(mLogger);
                       
       				if (isvalidlocforUser) {
       					transactionHandler = _POUtil.processProductReceiveMaterialByWMS(receiveMaterial_HM) && transactionHandler;
       					if(!transactionHandler) break process;
       				} else {
       					transactionHandler = transactionHandler && false;
       					throw new Exception("Receiving Loc :" + ITEM_LOC
       							+ " is not User Assigned Location");

       				}     	
       				
       				if (transactionHandler == true) {
       					Hashtable htCond = new Hashtable();
       					htCond.put(IConstants.PLANT, PLANT);
       					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
       						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
    						if(nonstkflag.equalsIgnoreCase("N")) {
       						String availqty ="0";
       						ArrayList invQryList = null;
       						htCond = new Hashtable();
       						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);
       						if(new PlantMstDAO().getisshopify(PLANT)) {
	       						if (invQryList.size() > 0) {					
	       							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	       								String result="";
	                                    Map lineArr = (Map) invQryList.get(iCnt);
	                                    availqty = (String)lineArr.get("AVAILABLEQTY");
	                                    System.out.println(availqty);
	       							}
	       							double availableqty = Double.parseDouble(availqty);
	           						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
	       						}	
       						}
    						}
       					}
       				}
					
				}
					
			}	
			if (transactionHandler == true) {
				//GRNOTOBILL
				if(createBill.equalsIgnoreCase("Y"))
				{
					RecvDetDAO _RecvDetDAO = new RecvDetDAO();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.VENDOR_CODE, CUST_CODE);
					htRecvDet.put(IConstants.GRNO, GRNO);                    
					htRecvDet.put(IConstants.PODET_PONUM, ICRNO);
					htRecvDet.put(IConstants.STATUS, "NOT BILLED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
//					htRecvDet.put("CRAT",DateUtils.getDateTime());
					htRecvDet.put("CRAT",orderdate);
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",DateUtils.getDateTime());
//                    transactionHandler = _RecvDetDAO.insertGRNtoBill(htRecvDet);//doubt
                    
                    Hashtable htShipHdr = new Hashtable();
                    htShipHdr.put(IConstants.PLANT, PLANT);
                    htShipHdr.put("DIRTYPE", TransactionConstants.ORD_RECV);
                    htShipHdr.put(IConstants.ORDNUM, ICRNO);
                    htShipHdr.put("CLEARING_AGENT_ID", CLEARAGENT);                    
                    htShipHdr.put("CLEARANCETYPE", CLEARANCETYPE);                    
                    if(TRANSPORTID.equalsIgnoreCase(""))
                    	TRANSPORTID="0";
                    htShipHdr.put("TRANSPORTID", TRANSPORTID);                    
                    htShipHdr.put("CONTACTNAME", CONTACT_NAME);                    
                    htShipHdr.put("TRACKINGNO", "");                    
                    htShipHdr.put("RECEIPTNO", GRNO);                    
                    htShipHdr.put("CRAT",DateUtils.getDateTime());
                    htShipHdr.put("CRBY",LOGIN_USER);
//                    transactionHandler = _RecvDetDAO.insertShippingHdr(htShipHdr);//doubt
                    
                    //insert MovHis
                    MovHisDAO movHisDao = new MovHisDAO();
            		movHisDao.setmLogger(mLogger);
        			Hashtable htRecvHis = new Hashtable();
        			htRecvHis.clear();
        			htRecvHis.put(IDBConstants.PLANT, PLANT);
        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
        			htRecvHis.put(IDBConstants.ITEM, "");
        			htRecvHis.put("MOVTID", "");
        			htRecvHis.put("RECID", "");
        			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
        			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
        			htRecvHis.put(IDBConstants.REMARKS, ICRNO);        					
        			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
        			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
        			transactionHandler = movHisDao.insertIntoMovHis(htRecvHis);

				}
					//DbBean.CommitTran(ut);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
				String message = "Products received successfully!";
					
				String Creditstatus = createCreditnode(request, response, productHdrInfoList, productDetInfoList, GRNO, PLANT, LOGIN_USER);
					/*----------------- automatic invoice-----------------------*/
//					String AutoConvStatus = new PlantMstDAO().getconvertRECEIPTBILL(PLANT);
//					if(AutoConvStatus.equalsIgnoreCase("1")) {
//						String invstatus = createAutoBill(request, response, receiveMaterial_HM, GRNO, PLANT, LOGIN_USER);
//						if(invstatus.equalsIgnoreCase("ok")) {
//							transactionHandler = true;
//							message = "Products Received & Bill Created Successfully" ;
//						}else {
//							transactionHandler = false;
//							message = "Products received successfully! Bill not created";
//						}
//					}					
					
					if (ajax) {
						//	Generate PDF and and send success response
						String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						request.setAttribute("chkdPoNo", new String[] {request.getParameter("PONO")});
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
							if (sendAttachment.contains("withgrno")) {
								request.setAttribute("printwithgrno", "Y");
							}
//							viewPOReport(request, response, "printPOWITHBATCH");
						}
						//	Print with GRNO option is not available for invoice
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
//							viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
						}
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "100");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						request.getSession(false).setAttribute("RESULTINBRECEIVE",
								message);
							response.sendRedirect("../productreceive/receiptsummary?action=View&ICRNO="+ ICRNO+ "&HDRID="+ ICRNOHDR);
					}
					
				} else {
					String message = "Unable to process..!";
					if (ajax) {
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "98");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						throw new Exception(message);
					}
				}
			
		}
		catch(Exception e)
		{
			this.mLogger.exception(this.printLog, "", e);
			String message = ThrowableUtil.getMessage(e);			
			if (ajax) {
				resultJson.put("MESSAGE", message);
				resultJson.put("ERROR_CODE", "99");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				request.getSession(false).setAttribute("CATCHERROR", message);
				response
						.sendRedirect("../productreceive/receiptsummary?action=catcerror&ICRNO="
								+ ICRNO
								+ "&HDRID="
								+ ICRNOHDR
								+ "&COLLECTION_DATE="
								+ collectionDate
								+ "&COLLECTION_TIME="
								+ collectionTime
								+ "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&REMARK="
								+ remark
								+ "&JOB_NUM="
								+ jobNum
								+ "&REMARK2="
								+ REMARKS
								+"&ITEM_LOC="
								+ITEM_LOC
								+ "&EXPIREDATE="
								+EXPIREDATE
								+ "&RECVDATE="
								+RECVDATE
								+"&REF="
								+REF
								+ "&allChecked="
								+allChecked
								+"&fullReceive="
								+fullReceive
								);
				throw e;
			}
		}
		return xmlStr;
	}
	
	public String createAutoBill(HttpServletRequest request,
			HttpServletResponse response,Map map,String grno, String plant, String username) {

		/* BillHdr*/
		boolean Isconvcost=false;
		String vendno = "", billNo = "", pono = "", billDate = "", dueDate = "", payTerms = "",
		itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "", orderdiscount="",currencyid="",currencyuseqt="0",
		adjustment = "0", adjustmentLabel = "Adjustment", subTotal = "", totalAmount = "", billStatus = "Open", note = "",taxamount="",isshtax="0",
		shipRef = "", refNum = "",paction="",vendname="",purchaseloc = "",invsalesloc="",taxtreatment="",sREVERSECHARGE="0",sGOODSIMPORT="0",isgrn="0",
		orddiscounttype="",taxid="0",isdiscounttax="1",isorderdiscounttax="0",gst="0",projectid="",transportid="",empno="",empName="",shippingid="",shippingcust="",
		origin = "sales", deductInv = "0", billtype = "PURCHASE",curency="";
		String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
				shipworkphone="",shipcountry="",shiphpno="",shipemail="";
		//	String ceqt="0",
		
		/*BillDet*/
		List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),
				cost = new ArrayList(), detDiscount = new ArrayList(),detDiscountType = new ArrayList(), taxType = new ArrayList(),
				amount = new ArrayList(), polnno = new ArrayList(), landedCost = new ArrayList(), convcost= new ArrayList(),
				cost_aod = new ArrayList(), amount_aod = new ArrayList();
		//List index = new ArrayList();
		List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),
				 ordQty = new ArrayList(),delLnno = new ArrayList(),delLnQty = new ArrayList(),
						 delLnLoc = new ArrayList(),delLnBatch = new ArrayList(),delLnUom = new ArrayList(),
						 delLnItem = new ArrayList();
		List<Hashtable<String,String>> billDetInfoList = null;
		List<Hashtable<String,String>> billAttachmentList = null;
		List<Hashtable<String,String>> billAttachmentInfoList = null;
		Hashtable<String,String> billDetInfo = null;
		Hashtable<String,String> billAttachment = null;
		UserTransaction ut = null;
		BillUtil billUtil = new BillUtil();
		RecvDetDAO recvDao = new RecvDetDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		TblControlDAO _TblControlDAO = new TblControlDAO();
		boolean isAdded = false;
		String result="";
		int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
				taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, landedCostCount = 0,convcostCount=0, cost_aodCount=0,amount_aodCount=0;
		int locCount  = 0,batchCount  = 0,uomCount  = 0, ordQtyCount = 0,
				delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
				delLnItemCount=0;
		String polnnoIn = "";
		try{
			
			pono= (String)map.get(IConstants.PODET_PONUM);
			vendno= (String)map.get(IConstants.CUSTOMER_CODE);
			billDate= (String)map.get(IConstants.RECVDATE);
			
			DateUtils dateUtils = new DateUtils();
			String fromdates = dateUtils.parseDateddmmyyyy(billDate);
			String time = DateUtils.getTimeHHmm();
			String orderdate = fromdates+time+"12";
			LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
			String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tran = formattedDate;
			
			String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(plant);
			String ConvertedUnitCost="",ConvertedAmount="",ConvertedUnitCost_Aod="",ConvertedAmount_Aod="";
			Hashtable ht=new Hashtable();
		    ht.put("PONO", pono);
		    ht.put("PLANT", plant);
		    ht.put("GRNO", grno);
		    List listQry =new POUtil().getBillingDetailsByGRNO(ht);
		    PoHdr pohdr =new PoHdrDAO().getPoHdrByPono(plant, pono);
		    String actualShippingCost =new PoHdrDAO().getActualShippingCostForBill(plant, pono);
		    double shipcost = Double.valueOf(actualShippingCost) * pohdr.getCURRENCYUSEQT();
		    double oddiscount = pohdr.getORDERDISCOUNT();
		    if(!pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
		    	 String actualDiscountCost =new PoHdrDAO().getActualDiscoutForBill(plant, pono);
		    	 oddiscount = Double.valueOf(actualDiscountCost) * pohdr.getCURRENCYUSEQT();
		    }
		    
		    actualShippingCost = StrUtils.addZeroes(shipcost, numberOfDecimal);
		    
		    payTerms=pohdr.getPAYMENT_TERMS();
		    empno=pohdr.getEMPNO();
		    itemRates=String.valueOf(pohdr.getISTAXINCLUSIVE());
		    discountType=pohdr.getCURRENCYID();
		    dueDate=pohdr.getDELDATE();
		    //adjustment=String.valueOf(pohdr.getADJUSTMENT());
		    isshtax=String.valueOf(pohdr.getISSHIPPINGTAX());
		    taxtreatment=pohdr.getTAXTREATMENT();
		    purchaseloc=pohdr.getPURCHASE_LOCATION();
		    currencyid=pohdr.getCURRENCYID();
		    currencyuseqt=String.valueOf(pohdr.getCURRENCYUSEQT());
		    orddiscounttype=pohdr.getORDERDISCOUNTTYPE();
		    taxid=String.valueOf(pohdr.getTAXID());
		    isorderdiscounttax=String.valueOf(pohdr.getISDISCOUNTTAX());
		    orderdiscount=String.valueOf(oddiscount);
		    shippingCost=String.valueOf(actualShippingCost);
		    gst=String.valueOf(pohdr.getINBOUND_GST());
		    projectid=String.valueOf(pohdr.getPROJECTID());
		    transportid=String.valueOf(pohdr.getTRANSPORTID());
		    shipcontactname=pohdr.getSHIPCONTACTNAME();
		    shipdesgination=pohdr.getSHIPDESGINATION();
		    shipworkphone=pohdr.getSHIPWORKPHONE();
		    shiphpno=pohdr.getSHIPHPNO();
		    shipemail=pohdr.getSHIPEMAIL();
		    shipcountry=pohdr.getSHIPCOUNTRY();
		    shipaddr1=pohdr.getSHIPADDR1();
		    shipaddr2=pohdr.getSHIPADDR2();
		    shipaddr3=pohdr.getSHIPADDR3();
		    shipaddr4=pohdr.getSHIPADDR4();
		    shipstate=pohdr.getSHIPSTATE();
		    shipzip=pohdr.getSHIPZIP();
		    shippingid=pohdr.getSHIPPINGID();
		    shippingcust=pohdr.getSHIPPINGCUSTOMER();
		    refNum = pohdr.getJobNum();
		    FinCountryTaxType fintaxtype= new FinCountryTaxType();
		    String ptaxtype="";
		    String ptaxiszero="1";
		    String ptaxisshow ="0";
		    if(pohdr.getTAXID() != 0) {
            	fintaxtype =new FinCountryTaxTypeDAO().getCountryTaxTypesByid(pohdr.getTAXID());
            	ptaxtype=String.valueOf(fintaxtype.getTAXTYPE());
            	ptaxiszero=String.valueOf(fintaxtype.getISZERO());
            	ptaxisshow=String.valueOf(fintaxtype.getSHOWTAX());
            }
		    
		    double dsubTotal =0;
		    double dtotalAmount =0;
		    double dtaxTotal =0;
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
		    		
		    		PoDet podet =new PoDetDAO().getPoDetByPonoItem(plant, (String)m.get("PONO"), (String)m.get("ITEM"));
					
		    		double dCost = Double.parseDouble((String)m.get("UNITCOST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dUnitCost_Aod = Double.parseDouble((String)m.get("UNITCOST_AOD"));
					ConvertedUnitCost_Aod = StrUtils.addZeroes(dUnitCost_Aod, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("RECQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					double dConvertedAmount_Aod = dUnitCost_Aod * dQty;
					ConvertedAmount_Aod = StrUtils.addZeroes(dConvertedAmount_Aod, numberOfDecimal);
					
					double totalamount = 0;
					double tdiscamount = podet.getDISCOUNT();    
					if(podet.getDISCOUNT_TYPE().equalsIgnoreCase("%")){
						totalamount = dAmount - (dAmount/100)*(podet.getDISCOUNT());
						//tamount = StrUtils.addZeroes(totalamount, numberOfDecimal);
					}else{
						totalamount = dAmount - ((podet.getDISCOUNT()/Double.parseDouble(String.valueOf(podet.getQTYOR())))*(Double.parseDouble((String)m.get("RECQTY"))));
						//tamount = StrUtils.addZeroes(totalamount, numberOfDecimal);
						tdiscamount = ((podet.getDISCOUNT()/Double.parseDouble(String.valueOf(podet.getQTYOR())))*(Double.parseDouble((String)m.get("RECQTY"))));
						//discamount = StrUtils.addZeroes(tdiscamount, numberOfDecimal);
					}
					dsubTotal=dsubTotal+totalamount;
					
					item.add(itemCount, StrUtils.fString((String)m.get("ITEM")));
					itemCount++;
					
					accountName.add(accountNameCount,StrUtils.fString(podet.getACCOUNT_NAME()));
					accountNameCount++;
					
					qty.add(qtyCount, StrUtils.fString((String)m.get("RECQTY")));
					qtyCount++;
					
					cost.add(costCount, StrUtils.fString((String)m.get("UNITCOST")));
					costCount++;
					
					detDiscount.add(detDiscountCount, StrUtils.fString(String.valueOf(tdiscamount)));
					detDiscountCount++;
					
					detDiscountType.add(detDiscountTypeCount, StrUtils.fString(podet.getDISCOUNT_TYPE()));
					detDiscountTypeCount++;
					
					taxType.add(taxTypeCount, StrUtils.fString(podet.getTAX_TYPE()));
					taxTypeCount++;
					
					amount.add(amountCount, StrUtils.fString(String.valueOf(totalamount)));
					amountCount++;
					
					polnno.add(polnnoCount, StrUtils.fString((String)m.get("LNNO")));
					polnnoCount++;
					
					landedCost.add(landedCostCount, StrUtils.fString("0.0"));
					landedCostCount++;
					
					if(Float.parseFloat(StrUtils.fString((String)m.get("UNITCOST")))>0) 
					{
					Isconvcost=true;
					}
					convcost.add(convcostCount, StrUtils.fString((String)m.get("UNITCOST")));
					convcostCount++;
					
					amount_aod.add(amount_aodCount, StrUtils.fString(ConvertedAmount_Aod));
					amount_aodCount++;
		    	}
		    }
		    
		    if(orddiscounttype.toString().equalsIgnoreCase("%"))
		    	orderdiscount = String.valueOf(( dsubTotal* (oddiscount /100)));
		    //else
		    	//orderdiscount = String.valueOf(((oddiscount) / (Double.parseDouble(currencyuseqt))));
		    
		    if(!itemRates.equalsIgnoreCase("0"))
		    {
		    	if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow .equalsIgnoreCase("1")){					
					double taxsubtotal =(100*dsubTotal) / (100+pohdr.getINBOUND_GST());
					dtotalAmount = taxsubtotal;
				    dsubTotal = taxsubtotal;
				}
		    }
		    
		    if(itemRates.equalsIgnoreCase("0")){
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					dtaxTotal = dtaxTotal + ((dsubTotal/100)*pohdr.getINBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*pohdr.getINBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*pohdr.getINBOUND_GST());
					}
				}
		 }else{
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					
					dtaxTotal = (dtaxTotal) + ((dsubTotal/100)*pohdr.getINBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*pohdr.getINBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*pohdr.getINBOUND_GST());
					}
					
				}
		}
		    
		    if(ptaxisshow.equalsIgnoreCase("0"))
		    	dtaxTotal=0;
		    
		    double pretotal = ((dsubTotal) - Double.parseDouble(orderdiscount) + (dtaxTotal)	 + Double.parseDouble(shippingCost)); 
		    
		    //if(pretotal >= Double.parseDouble(adjustment)){
				 
			 //}else{
				 //adjustment = "0";				 
			 //}
			 
		    dtotalAmount = ((dsubTotal)- Double.parseDouble(orderdiscount) + (dtaxTotal)
					 + Double.parseDouble(shippingCost) + Double.parseDouble(adjustment));
		    adjustment= String.valueOf(Double.parseDouble(adjustment)/ (Double.parseDouble(currencyuseqt)));
		    shippingCost= String.valueOf(Double.parseDouble(shippingCost)/ (Double.parseDouble(currencyuseqt)));
		    orderdiscount = StrUtils.addZeroes((Double.parseDouble(orderdiscount) / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    subTotal = String.valueOf(dsubTotal / (Double.parseDouble(currencyuseqt)));
		    totalAmount = StrUtils.addZeroes((dtotalAmount / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    taxamount =String.valueOf(dtaxTotal / (Double.parseDouble(currencyuseqt)));
			 
			billNo= _TblControlDAO.getNextOrder(plant,username,"BILL");
			//////////////////////
			billDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable billHdr =new Hashtable(); 
			billHdr.put("PLANT", plant);
			billHdr.put("VENDNO", vendno);
			billHdr.put("BILL", billNo);
			billHdr.put("PONO", pono);
			billHdr.put("GRNO", grno);
			billHdr.put("BILL_DATE", billDate);
			billHdr.put("DUE_DATE", dueDate);
			billHdr.put("PAYMENT_TERMS", payTerms);
			billHdr.put("EMPNO", empno);
			billHdr.put("ITEM_RATES", itemRates);
			billHdr.put("DISCOUNT_TYPE", discountType);
			billHdr.put("DISCOUNT_ACCOUNT", discountAccount);
			billHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
			billHdr.put("ADJUSTMENT", adjustment);
			billHdr.put("SUB_TOTAL", subTotal);
			billHdr.put("TOTAL_AMOUNT", totalAmount);
			billHdr.put("BILL_STATUS", billStatus);
			billHdr.put("SHIPMENT_CODE", shipRef);
			billHdr.put("REFERENCE_NUMBER", refNum);
			billHdr.put("NOTE", note);	
			billHdr.put("CRAT",DateUtils.getDateTime());
			billHdr.put("CRBY",username);
			billHdr.put("UPAT",DateUtils.getDateTime());
			billHdr.put("CREDITNOTESSTATUS","0");
			billHdr.put("TAXAMOUNT", taxamount);
			billHdr.put("ISSHIPPINGTAXABLE", isshtax);
			if(purchaseloc.equalsIgnoreCase(""))
				billHdr.put("PURCHASE_LOCATION", invsalesloc);
			else
				billHdr.put("PURCHASE_LOCATION", purchaseloc);

			
			billHdr.put("TAXTREATMENT",taxtreatment);
			billHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
			billHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
			billHdr.put(IDBConstants.CURRENCYID, currencyid);
			billHdr.put("CURRENCYUSEQT",currencyuseqt);
			billHdr.put("ORDERDISCOUNTTYPE",orddiscounttype);
			billHdr.put("TAXID",taxid);
			billHdr.put("ISDISCOUNTTAX",isdiscounttax);
			billHdr.put("ISORDERDISCOUNTTAX",isorderdiscounttax);
			billHdr.put("DISCOUNT", discount);
			billHdr.put("SHIPPINGCOST", shippingCost);
			billHdr.put("ORDER_DISCOUNT", orderdiscount);
			//billHdr.put("ORDER_DISCOUNT", String.valueOf(oddiscount));			
			billHdr.put("INBOUND_GST", gst);
			billHdr.put("PROJECTID", projectid);
			billHdr.put("TRANSPORTID", transportid);
			billHdr.put("SHIPCONTACTNAME",shipcontactname);
			billHdr.put("SHIPDESGINATION",shipdesgination);
			billHdr.put("SHIPWORKPHONE",shipworkphone);
			billHdr.put("SHIPHPNO",shiphpno);
			billHdr.put("SHIPEMAIL",shipemail);
			billHdr.put("SHIPCOUNTRY",shipcountry);
			billHdr.put("SHIPADDR1",shipaddr1);
			billHdr.put("SHIPADDR2",shipaddr2);
			billHdr.put("SHIPADDR3",shipaddr3);
			billHdr.put("SHIPADDR4",shipaddr4);
			billHdr.put("SHIPSTATE",shipstate);
			billHdr.put("SHIPZIP",shipzip);
			billHdr.put("SHIPPINGID", shippingid);
			billHdr.put("SHIPPINGCUSTOMER", shippingcust);
			billHdr.put("ORIGIN",origin);
			billHdr.put("DEDUCT_INV",deductInv);
			billHdr.put("BILL_TYPE",billtype);
			
			ExpensesDAO expenseDao=new ExpensesDAO();
			CoaDAO coaDao=new CoaDAO();
			BillDAO itemCogsDao=new BillDAO();
			List<CostofgoodsLanded> landedCostLst=new ArrayList<>();
//			List<ItemCogs> lstCogs=new ArrayList<>();
			ItemMstDAO itemmstDao=new ItemMstDAO();
			Costofgoods costofGoods=new CostofgoodsImpl();
			double expenseAmt=0.0;
			/*Get Transaction object*/
			ut = DbBean.getUserTranaction();				
			/*Begin Transaction*/
			ut.begin();
			int billHdrId = billUtil.addBillHdr(billHdr, plant);	
			//int billHdrId = 0;	
			if(billHdrId > 0) {
				if(shipRef!=null && !shipRef.isEmpty()) {
				List<String> expensesId=expenseDao.getExpesesHDR(pono, plant,shipRef);
				List<CostofgoodsLanded> expensesAccount=expenseDao.getExpesesDET(expensesId, plant);
				List<CostofgoodsLanded> listofLanded=coaDao.getCOAByName(expensesAccount, plant);
				for(int i=0;i<expensesAccount.size();i++) {
					expenseAmt+=expensesAccount.get(i).getAmount();
				}
			
				for(int i=0;i<expensesAccount.size();i++) {
					for(int j=0;j<listofLanded.size();j++) {
						if(expensesAccount.get(i).getAccount_name().equalsIgnoreCase(listofLanded.get(j).getAccount_name())) {
							expensesAccount.get(i).setLandedcostcal(listofLanded.get(j).getLandedcostcal());
						}
					}
				}
				boolean isBasedOnCost=false,isBasedOnWeight=false;
				for(int i=0;i<expensesAccount.size();i++) {
					if(expensesAccount.get(i).getLandedcostcal().equals("1")) {
						isBasedOnCost=true;
					}if(expensesAccount.get(i).getLandedcostcal().equals("2")) {
						isBasedOnWeight=true;
					}
				}
				int itemCnt=0;
				if(amount_aod.size()==0) {
					amount_aod = amount;
				}
				Double avg_rate=0.0;
				Double weightedQty=costofGoods.calculateWeightedQty(item,qty,plant);
				Double subtotal=costofGoods.getProductSubtotalAmount(amount);
				for(int i=0;i<item.size();i++) {
					itemCnt=item.size();
					CostofgoodsLanded costof=new CostofgoodsLanded();
					costof.setProd_id((String)item.get(i));
					CostofgoodsLanded weight=itemmstDao.getItemMSTDetails((String)item.get(i), plant);
					costof.setWeight(weight.getNet_weight());
					costof.setQuantity(Double.parseDouble((String)qty.get(i)));
					costof.setWeight_qty(weightedQty);
					
					CostofgoodsLanded reqObj=new CostofgoodsLanded();
					reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
					reqObj.setDiscount(Double.parseDouble(discount));
					reqObj.setDiscountType(discountType);
					reqObj.setShippingCharge(Double.parseDouble(shippingCost));
					reqObj.setLstamount(amount);
					reqObj.setSub_total(subtotal);
					reqObj.setAmount(Double.parseDouble((String)amount.get(i)));
				
					Double amt=costofGoods.calculateIndividualAmount(reqObj,itemCnt,i);
					costof.setAmount(amt);
					Double totalCost=costofGoods.calculateTotalCost(item,reqObj);
					costof.setTotal_cost(totalCost);
					
					if("%".equals(reqObj.getDiscountType())) {
					reqObj.setAmount(amt);
					reqObj.setTotal_cost(totalCost);
					reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
					amt=costofGoods.calculateIndividualAmountForOrderDiscount(reqObj,itemCnt,i);
					costof.setAmount(amt);
					totalCost=costofGoods.calculateTotalCostForOrderDiscount(item,reqObj);
					costof.setTotal_cost(totalCost);
					}
					
					costof.setUnit_cost(Double.parseDouble((String)cost.get(i)));
					costof.setExpenses_amount(expenseAmt);
					
					if(isBasedOnWeight && !isBasedOnCost) {
						// implement calculation based on weight
						avg_rate=costofGoods.calculateLandedWeightBased(costof);
					}else if(!isBasedOnWeight && isBasedOnCost){
						// implement calculation based on cost
						costof.setAmount((amt/costof.getQuantity()));
						avg_rate=costofGoods.calculateLandedCostBased(costof);
					}else if(isBasedOnWeight && isBasedOnCost) {
						// implement calculation based on both
						Double costedExpensesAmt=0.0,weight_allocation=0.0,weightedExpensesAmt=0.0,cost_allocation=0.0,landed_cost=0.0;
						for(int j=0;j<expensesAccount.size();j++) {
							if(expensesAccount.get(j).getLandedcostcal().equals("1")) {
								costedExpensesAmt+=expensesAccount.get(j).getAmount();
							}if(expensesAccount.get(j).getLandedcostcal().equals("2")) {
								weightedExpensesAmt+=expensesAccount.get(j).getAmount();
							}
						}
						costof.setExpenses_amount(weightedExpensesAmt);
						weight_allocation=costofGoods.calculateWeightAllocaiton(costof);
						costedExpensesAmt+=Double.parseDouble(shippingCost);
						costof.setExpenses_amount(costedExpensesAmt);
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=weight_allocation+cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}else {
						// implement calculation based on both not applicable
						Double costedExpensesAmt=0.0,cost_allocation=0.0,landed_cost=0.0;
						costedExpensesAmt=costof.getExpenses_amount()+Double.parseDouble(shippingCost);
						costof.setExpenses_amount(costedExpensesAmt);
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}
					  costof.setAvg_rate(avg_rate);
					  costof.setBillhdrid(String.valueOf(billHdrId));
					  landedCostLst.add(costof);
					 
					  int cogsCnt=itemCogsDao.addItemCogs(costofGoods.entryProductDetails((String)qty.get(i), (String)item.get(i), plant, avg_rate, dueDate),plant);
					  System.out.println("Insert ItemCogs Status :"+ cogsCnt);
							

					}
				}

	
				
				for(int i =0 ; i < item.size() ; i++){
					int lnno = i+1;
					String convDiscount=""; 
					String convCost = String.valueOf((Float.parseFloat((String) cost.get(i)) / Float.parseFloat(currencyuseqt)));
					if(Isconvcost)
						convCost = String.valueOf((Float.parseFloat((String) convcost.get(i)) / Float.parseFloat(currencyuseqt)));
					if(!detDiscountType.get(i).toString().contains("%"))
					{
						convDiscount = String.valueOf((Float.parseFloat((String) detDiscount.get(i)) / Float.parseFloat(currencyuseqt)));
					}
					else
						convDiscount = (String) detDiscount.get(i);
					String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
					double clancost = (Float.parseFloat((String) landedCost.get(i)) / Float.parseFloat(currencyuseqt));
					if(Double.isNaN(clancost)) {
						clancost = 0.0;
					}
					String convlandedCost = String.valueOf(clancost);

					billDetInfo = new Hashtable<String, String>();
					billDetInfo.put("PLANT", plant);
//					billDetInfo.put("LNNO", Integer.toString(lnno));
					billDetInfo.put("LNNO", (String) polnno.get(i));
					billDetInfo.put("BILLHDRID", Integer.toString(billHdrId));
					billDetInfo.put("ITEM", (String) item.get(i));
					billDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
					billDetInfo.put("QTY", (String) qty.get(i));
					billDetInfo.put("COST", convCost);
					billDetInfo.put("DISCOUNT", convDiscount);
					billDetInfo.put("DISCOUNT_TYPE", (String) detDiscountType.get(i));
					billDetInfo.put("Tax_Type", (String) taxType.get(i));
					billDetInfo.put("Amount", convAmount);
					billDetInfo.put("LANDED_COST", convlandedCost);
					billDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
					if(loc.size() > 0) {
						billDetInfo.put("LOC", (String) loc.get(i));
						billDetInfo.put("UOM", (String) uom.get(i));
						billDetInfo.put("BATCH", (String) batch.get(i));
					}
					billDetInfo.put("CRAT",DateUtils.getDateTime());
					billDetInfo.put("CRBY",username);
					billDetInfo.put("UPAT",DateUtils.getDateTime());
					billDetInfoList.add(billDetInfo);
					if(polnno.size() > 0) {
						if(!grno.equalsIgnoreCase("")) {
						if(i < (item.size()-1)) {
							polnnoIn += (String) polnno.get(i)+ ",";
						}else {
							polnnoIn += (String) polnno.get(i);
						}		
						}
					}
				}			
				isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);
				/*
				 * int attchSize = billAttachmentList.size(); for(int i =0 ; i < attchSize ;
				 * i++){ billAttachment = new Hashtable<String, String>(); billAttachment =
				 * billAttachmentList.get(i); billAttachment.put("BILLHDRID",
				 * Integer.toString(billHdrId)); billAttachmentInfoList.add(billAttachment); }
				 */
				if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
					BillDAO billDao=new BillDAO();
					for(int i=0;i<landedCostLst.size();i++) {
							int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
							System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
					}
				}
				if(isAdded) {
					//if(billAttachmentInfoList.size() > 0)
						//isAdded = billUtil.addBillAttachments(billAttachmentInfoList, plant);

					
					curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					System.out.println("billStatus"+billStatus);
					if(!billStatus.equalsIgnoreCase("Draft"))
					{
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(billDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("BILL");
						journalHead.setTRANSACTION_ID(billNo);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);

						
						List<JournalDetail> journalDetails=new ArrayList<>();
						List<JournalDetail> journalReversalList=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						VendMstDAO vendorDAO=new VendMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						Double totalline=0.00;
						for(Map billDet:billDetInfoList)
						{	
							Double quantity=Double.parseDouble(billDet.get("QTY").toString());
							totalline++;
							String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
//							TODO : Ravindra - Get verified
							if (netWeight == null || "".equals(netWeight)) {
								netWeight = "0";
							}
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
							if(!orddiscounttype.toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS((Double.parseDouble(billDet.get("Amount").toString()) - oddiscount/billDetInfoList.size()));
							}else {
								Double jodamt = ((Double.parseDouble(billDet.get("Amount").toString())/100)*oddiscount);
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) -jodamt);
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
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
							if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject())
								{
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
							journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
						}

						
						
						Double taxAmountFrom=Double.parseDouble(taxamount);
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							
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

						
						
						
						Double discountFrom = Double.parseDouble(discount);
						Double orderDiscountFrom=0.00;
							
						if(discountFrom>0 || orderDiscountFrom>0)
						{
							if(!discountType.isEmpty())
							{
								if(discountType.equalsIgnoreCase("%"))
								{
									Double subTotalAfterOrderDiscount=Double.parseDouble(subTotal)-orderDiscountFrom;
									discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
								}
							}
							discountFrom=discountFrom+orderDiscountFrom;
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
							journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
							journalDetail_3.setACCOUNT_NAME("Discount Received");
							journalDetail_3.setCREDITS(discountFrom);
							journalDetails.add(journalDetail_3);
						}

						
						if(!shippingCost.isEmpty())
						{
							Double shippingCostFrom=Double.parseDouble(shippingCost);
							if(shippingCostFrom>0)
							{
								JournalDetail journalDetail_4=new JournalDetail();
								journalDetail_4.setPLANT(plant);
								JSONObject coaJson4=coaDAO.getCOAByName(plant, "Inward freight & shipping");
								journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
								journalDetail_4.setACCOUNT_NAME("Inward freight & shipping");
								journalDetail_4.setDEBITS(shippingCostFrom);
								journalDetails.add(journalDetail_4);

								
								for(Map billDet:billDetInfoList)
								{
									Double quantity=Double.parseDouble(billDet.get("QTY").toString());
									String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
									Double Netweight=quantity*Double.parseDouble(netWeight);
									Double calculatedShippingCost=0.0;
										if(totalItemNetWeight>0)
										{
											if(Netweight>0)
											{
												calculatedShippingCost=(shippingCostFrom*Netweight)/totalItemNetWeight;
											}
											else
											{
												calculatedShippingCost=0.00;
											}
										}
										else
										{
											calculatedShippingCost=shippingCostFrom/totalline;
										}
									System.out.println("calculatedShippingCost:"+calculatedShippingCost);
									
									JournalDetail journalDetail_5=new JournalDetail();
									journalDetail_5.setPLANT(plant);
									JSONObject coaJson5=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
									journalDetail_5.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
									journalDetail_5.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
									journalDetail_5.setDEBITS(calculatedShippingCost);
									boolean isLoop=false;
									if(journalReversalList.size()>0)
									{
										int i=0;
										for(JournalDetail journal:journalReversalList) {
											int accountId=journal.getACCOUNT_ID();
											if(accountId==journalDetail_5.getACCOUNT_ID()) {
												isLoop=true;
												Double sumDetit=journal.getDEBITS()+journalDetail_5.getDEBITS();
												journalDetail_5.setDEBITS(sumDetit);
												journalReversalList.set(i, journalDetail_5);
												break;
											}
											i++;

											
										}
										if(isLoop==false) {
											journalReversalList.add(journalDetail_5);
										}
									}
									else
									{
										journalReversalList.add(journalDetail_5);
									}
								}

								
								JournalDetail journalDetail_6=new JournalDetail();
								journalDetail_6.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Inward freight & shipping");
								journalDetail_6.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_6.setACCOUNT_NAME("Inward freight & shipping");
								journalDetail_6.setCREDITS(shippingCostFrom);
								journalReversalList.add(journalDetail_6);
							}

							
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
						
						List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
						if(shipRef != "") {
						ArrayList  movQryList = new MasterUtil().getExpenseDetailusingponoanddnol(plant, pono, shipRef);
						if (movQryList.size() > 0) {
							ht = new Hashtable();
							ht.put("PLANT",plant);
							ht.put("SHIPMENT_CODE",shipRef);
							ht.put("PONO",pono);
							List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
							for(int j=0;j < expenseHdrList.size();j++) {
//								Journal journal_2=new Journal();
								Map expenseHdr = (Map) expenseHdrList.get(j);									
								JournalHeader expensepo_journalHead=new JournalHeader();
								expensepo_journalHead.setPLANT(plant);								
								expensepo_journalHead.setJOURNAL_STATUS("PUBLISHED");
								expensepo_journalHead.setJOURNAL_TYPE("Cash");
								expensepo_journalHead.setCURRENCYID(curency);
								expensepo_journalHead.setTRANSACTION_ID((String) expenseHdr.get("ID"));
								expensepo_journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+pono+")");
								expensepo_journalHead.setJOURNAL_DATE(billDate);
								expensepo_journalHead.setSUB_TOTAL(Double.parseDouble((String) expenseHdr.get("SUB_TOTAL")));
								expensepo_journalHead.setTOTAL_AMOUNT(Double.parseDouble((String) expenseHdr.get("TOTAL_AMOUNT")));
								expensepo_journalHead.setCRAT(DateUtils.getDateTime());
								expensepo_journalHead.setCRBY(username);
								
								
								JournalDetail inventorySelected=new JournalDetail();
								inventorySelected.setPLANT(plant);
								JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
								inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
								inventorySelected.setACCOUNT_NAME("Inventory Asset");
								inventorySelected.setDEBITS(Double.parseDouble((String) expenseHdr.get("SUB_TOTAL")));
								expensepo_journaldetails.add(inventorySelected);
								
//								double expenseTaxAmount = 0.0;
								for(int i =0; i<movQryList.size(); i++) {
									Map arrCustLine = (Map)movQryList.get(i);
									if(Double.parseDouble((String)expenseHdr.get("ID")) == Double.parseDouble((String)arrCustLine.get("ID"))) {
										JournalDetail expensesSelected=new JournalDetail();
										expensesSelected.setPLANT(plant);
										JSONObject coaJson=coaDAO.getCOAByName(plant, (String)arrCustLine.get("EXPENSES_ACCOUNT"));
										expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										expensesSelected.setACCOUNT_NAME((String)arrCustLine.get("EXPENSES_ACCOUNT"));
										expensesSelected.setCREDITS(Double.parseDouble((String)arrCustLine.get("AMOUNT").toString()));
										expensepo_journaldetails.add(expensesSelected);
										journalReversalList.add(expensesSelected);
//										expenseTaxAmount = Double.parseDouble((String)arrCustLine.get("TAXAMOUNT").toString());
									}

								}
								
								
								
								boolean isDebitExists = false;
								for(JournalDetail journDet:journalReversalList)
								{
									double debits = journDet.getDEBITS();
									if(journDet.getACCOUNT_NAME().equalsIgnoreCase("Inventory Asset")) {
										journDet.setDEBITS(debits+inventorySelected.getDEBITS());
										journDet.setCREDITS(discountFrom);
										isDebitExists = true;

									}
									
								}

								if(!isDebitExists) {
									Map billDet = billDetInfoList.get(0);
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson5=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
									journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
									journalDetail.setDEBITS(inventorySelected.getDEBITS());
									journalDetail.setCREDITS(discountFrom);
									journalReversalList.add(journalDetail);
								}
								
								/*if(expenseTaxAmount>0)
								{
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail.setACCOUNT_NAME("VAT Input");
									journalDetail.setCREDITS(expenseTaxAmount);
									journalReversalList.add(journalDetail);
								}*/
								
								if(discountFrom>0 || orderDiscountFrom>0)
								{
									JournalDetail journalDetail_3=new JournalDetail();
									journalDetail_3.setPLANT(plant);
									JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
									journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
									journalDetail_3.setACCOUNT_NAME("Discount Received");
									journalDetail_3.setDEBITS(discountFrom);
									journalReversalList.add(journalDetail_3);
								}
								
								/*journal_2.setJournalHeader(expensepo_journalHead);
								journal_2.setJournalDetails(expensepo_journaldetails);
								journalService.addJournal(journal_2, username);*/
							}
							
						}

						}
						
						if(journalReversalList.size()>0)
						{
							JournalHeader journalReversalHead=journalHead;
							Double totalDebitReversal=0.00;
							for(JournalDetail journDet:journalReversalList)
							{
								totalDebitReversal=totalDebitReversal+journDet.getDEBITS();
								

							}
							journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
							Journal journal_1=new Journal();
							journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
							journal_1.setJournalHeader(journalReversalHead);
							journal_1.setJournalDetails(journalReversalList);
							journalService.addJournal(journal_1, username);
							Hashtable jhtMovHis1 = new Hashtable();
							jhtMovHis1.put(IDBConstants.PLANT, plant);
							jhtMovHis1.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							jhtMovHis1.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							jhtMovHis1.put(IDBConstants.ITEM, "");
							jhtMovHis1.put(IDBConstants.QTY, "0.0");
							jhtMovHis1.put("RECID", "");
							jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,journal_1.getJournalHeader().getTRANSACTION_TYPE()+" "+journal_1.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis1.put(IDBConstants.CREATED_BY, username);		
							jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis1.put("REMARKS","");
							movHisDao.insertIntoMovHis(jhtMovHis1);
						}
					}
				}
				if(isAdded && (polnno.size() > 0 && pono.length() > 0)) {
					if(!grno.equalsIgnoreCase("")) {
						String query = "SET BILL_STATUS = 'BILLED'";
						String extCond = " AND LNNO IN ("+polnnoIn+") ";
						Hashtable<String, String> htCondition = new Hashtable<String, String>();
						htCondition.put("PONO", pono);
						htCondition.put("GRNO", grno);				
						isAdded = recvDao.update(query, htCondition, extCond, plant);
					}
				}
			}

			
			
			
			if(isAdded) {
				for(int i =0 ; i < item.size() ; i++){
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_BILL);
				if(isgrn.equalsIgnoreCase("1"))
					htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_BILL);	
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
//				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));														
				htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
				String billqty = String.valueOf((String) qty.get(i));
				htMovHis.put(IDBConstants.QTY, billqty);
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, billNo);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS",pono+","+grno+","+refNum);
				isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
			}
						
			//Update BILL Seq
			if(isAdded) {
			Hashtable htv = new Hashtable();				
			htv.put("PLANT", plant);
			htv.put("FUNC", "BILL");
			isAdded = _TblControlDAO.isExisit(htv, "", plant);
			if (isAdded) 
				isAdded=_TblControlDAO.updateSeqNo("BILL",plant);
			}

			
			//GRN to Bill Status Update
			if(isAdded) {
				if(grno.length()>0&&pono.length()>0)
				{
				BillDAO billDao = new BillDAO();
				Hashtable<String, String> htCondition = new Hashtable<String, String>();
				htCondition.put("PONO", pono);
				htCondition.put("GRNO", grno);
					isAdded = billDao.updateGrntoBill("SET STATUS='BILLED' ", htCondition, "", plant);
				if(shipRef.length()>0) {
					ExpensesDAO expDao = new ExpensesDAO();
					String query = " SET STATUS='BILLED' ";
					htCondition = new Hashtable<String, String>();
					htCondition.put("PONO", pono);
					htCondition.put("SHIPMENT_CODE", shipRef);
					htCondition.put("PLANT", plant);
					int count = expDao.update(query, htCondition, "");
					if(count > 0) {
						isAdded = true;
					}else {
						isAdded = false;
					}						
				}
			}
			/* Added by Abhilash to handle COGS */
			for(Map billDet:billDetInfoList){			
				
				List pendingCogsInvoiceDetails = new InvoiceDAO().invoiceWoCOGS((String)billDet.get("ITEM"), plant);
				if(pendingCogsInvoiceDetails.size()>0) {								
					for(int i = 0; i < pendingCogsInvoiceDetails.size(); i++) {
						Double totalCostofGoodSold=0.00;
						CoaDAO coaDAO=new CoaDAO();
						Journal journal=new Journal();
						List<JournalDetail> journalDetails=new ArrayList<>();
						Map pendingCogsInvoice = (Map) pendingCogsInvoiceDetails.get(i);

						
						Map invDetail = new InvMstDAO().getInvDataByProduct((String)pendingCogsInvoice.get("ITEM"), plant);
						double inv_qty=0, bill_qty = 0, unbill_qty = 0, net_bill_qty=0, 
								invoiced_qty = 0, invoice_quantity = 0, quantity = 0;
						inv_qty = Double.parseDouble((String)invDetail.get("INV_QTY"));
						bill_qty = Double.parseDouble((String)invDetail.get("BILL_QTY"));
						unbill_qty = Double.parseDouble((String)invDetail.get("UNBILL_QTY"));
						invoiced_qty = Double.parseDouble((String)invDetail.get("INVOICE_QTY"));
						invoice_quantity = Double.parseDouble((String)pendingCogsInvoice.get("QTY"));
						quantity =Double.parseDouble(pendingCogsInvoice.get("QTY").toString());
						

						bill_qty = bill_qty - invoiced_qty;
						net_bill_qty = bill_qty + unbill_qty;							
						
						if((net_bill_qty != inv_qty) && (bill_qty >= invoice_quantity)) {
							ArrayList invQryList;
							Hashtable ht_cog = new Hashtable();
							ht_cog.put("a.PLANT",plant);
							ht_cog.put("a.ITEM",(String)billDet.get("ITEM"));
							invQryList= new InvUtil().getAverageCost(ht_cog, plant, (String)billDet.get("ITEM"), curency, curency);
							if(invQryList!=null){
								if(!invQryList.isEmpty()){
									Map lineArr = (Map) invQryList.get(0);
									String avg = StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("AVERAGE_COST")),"2");
									totalCostofGoodSold += Double.parseDouble(avg)*(quantity);
								}
							}

							
							JournalService journalService=new JournalEntry();
							Journal journalCOG=journalService.getJournalByTransactionId(plant, (String)pendingCogsInvoice.get("INVOICE"), "COSTOFGOODSOLD");
							
							if(journalCOG.getJournalHeader()!=null){
								if(journalCOG.getJournalHeader().getID()>0){
									totalCostofGoodSold += journalCOG.getJournalHeader().getTOTAL_AMOUNT();
								}
							}

							
							JournalHeader journalHead=new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(DateUtils.getDate());
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
							journalHead.setTRANSACTION_ID((String)pendingCogsInvoice.get("INVOICE"));
							journalHead.setSUB_TOTAL(totalCostofGoodSold);
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							
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

							
							if(journalCOG.getJournalHeader()!=null)
							{
								if(journalCOG.getJournalHeader().getID()>0)
								{
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
							 new InvoiceDAO().update_is_cogs_set((String)pendingCogsInvoice.get("INVOICEHDRID"), (String)pendingCogsInvoice.get("LNNO"), (String)pendingCogsInvoice.get("ITEM"), plant);
						}									
					}
				}					
			}
			}
			if(isAdded) {
				DbBean.CommitTran(ut);
				result = "ok";
			}else {
				DbBean.RollbackTran(ut);
	 			result = "Bill not created";
			}
			if(result.equalsIgnoreCase("Bill not created")) {
				 result = "not ok";
			}
			
		} catch (Exception e) {
			 e.printStackTrace();
			 autoinverr=e.toString();
			 result = "not ok";
		}
	
		return result;
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;

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
		boolean flag=true;
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
		boolean updateFlag = false;
		boolean insertFlag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnShippNo = "";

		try {
			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION,IDBConstants.TBL_SHIPPING_CAPTION);

			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, plant);

			if (exitFlag == false) {

				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) DateUtils.getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,plant);

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
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
				} else if (!inserted) {

					throw new Exception(
							"Generate Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnShippNo = plant + updatedSeq;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "Shipping");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");
				updateFlag = _TblControlDAO.update(updateQyery.toString(),htTblCntUpdate, extCond, plant);

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
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
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
	
	
	private boolean processPrdInvRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			ArrayList ALRecvddetails = new ArrayList();
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			if(map.get(IConstants.BATCH).equals("NOBATCH")){
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			}
			else
			{
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				//htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
			}
			double reverseqty = Double.parseDouble((String) map.get("QTY"));
			
			if (ALRecvddetails.size() > 0) {
				for (int i = 0; i < ALRecvddetails.size(); i++) {
					Map m = (Map) ALRecvddetails.get(i);
					{
						if(reverseqty==0) break;
						String ID = (String)m.get("ID").toString();
						String RQTY = (String)m.get("RECQTY").toString();
						double issueqty = Double.parseDouble(RQTY);
						htInvMst.put(IDBConstants.INVID, ID);
			String cond ="";
			 if(reverseqty >= issueqty)
				 {
				 double inqty = Double.valueOf(RQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 cond =" QTY >="+ inqty;
				 }
			 else
			 {
				 double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
				 cond =" QTY >="+ inqty;
			 }
			flag = _InvMstDAO.isExisit(htInvMst,cond );
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				//sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");
				if(reverseqty >= issueqty)
				{
					double inqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + inqty + "'");
				}
				else
				{
					double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + inqty + "'");
					issueqty=reverseqty;
				}
				if(reverseqty>0)
				{
				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
				reverseqty = (reverseqty-issueqty);
				}
			} else {
				throw new Exception("Product Reversed  Failed, Error In update InvMst :"	+ " " + map.get(IConstants.ITEM));
			}
					}
					
				}
				ALRecvddetails = new ArrayList();
			}
		} catch (Exception e) {

		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
  }
	
	public String createCreditnode(HttpServletRequest request,
			HttpServletResponse response,List<Hashtable<String,String>> returnHdr,List<Hashtable<String,String>> returnDetlist,String gino, String plant, String username) throws Exception {
			String result = "";
			boolean isAdded = false;
			
			MovHisDAO movHisDao = new MovHisDAO();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			DateUtils dateutils = new DateUtils();
			InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			ExceptionUtil exceptionUtil=null;
			
			CustomerCreditnoteDAO customerCreditDao = new CustomerCreditnoteDAO();
			List<Hashtable<String, String>> creditnoteDetInfoList = null;
			Hashtable<String, String> creditnoteDetInfo = null;
		
			try{
			Hashtable<String, String> header = returnHdr.get(0);
			String PLANT = header.get("PLANT");
			String irno = header.get("ICRNO");
			String return_date = header.get("RECEIVE_DATE");
			String empno = header.get("EMPNO");
			String custno = header.get("CustCode");
	        String return_status = header.get("RECEIVE_STATUS");
	        String payment_terms = header.get("PAYMENT_TERMS");
	        String transportid = header.get("TRANSPORTID");
	        String taxtreatment = header.get("TAXTREATMENT");
	        String jobnum = header.get("JobNum");
	        String note = header.get("NOTE");
	        String item_rates = header.get("ITEM_RATES");
	        String currencyid = header.get("CURRENCYID");
	        String currencyuseqt = header.get("CURRENCYUSEQT");
	        String taxid = header.get("TAXID");
	        
	        String discount = header.get("DISCOUNT");
	        String isshippingtax = header.get("ISSHIPPINGTAX");
	        String discount_account = header.get("DISCOUNT_ACCOUNT");
	        String outboud_gst = header.get("INBOUND_GST");
	        String discount_type = header.get("DISCOUNT_TYPE");
	        String total_paying = header.get("TOTAL_PAYING");
	        String adjustment = header.get("ADJUSTMENT");
	        String order_discount = header.get("ORDER_DISCOUNT");
	        String isdiscounttax = header.get("ISDISCOUNTTAX");
	        String orderdiscounttype = header.get("ORDERDISCOUNTTYPE");
	        String isorderdiscounttax = header.get("ISORDERDISCOUNTTAX");
	        String total_amount = header.get("TOTAL_AMOUNT");
	        String taxamount = header.get("TAXAMOUNT");
	        String creditnotesstatus = header.get("CREDITNOTESSTATUS");
	        String sub_total = header.get("SUB_TOTAL");
	        
	        String shippingid = header.get("SHIPPINGID");
	        String shippingcustomer = header.get("SHIPCOUNTRY");
	        String shipcontactname = header.get("SHIPCONTACTNAME");
	        String shipstate = header.get("SHIPSTATE");
	        String shipdesgination = header.get("SHIPDESGINATION");
	        String shipaddr1 = header.get("SHIPADDR1");
	        String shipaddr2 = header.get("SHIPADDR2");
	        String shipaddr3 = header.get("SHIPADDR3");
	        String shipaddr4 = header.get("SHIPADDR4");
	        String shipemail = header.get("SHIPEMAIL");
	        String shippingcost = header.get("SHIPPINGCOST");
	        String shipworkphone = header.get("SHIPWORKPHONE");
	        String shiphpno = header.get("SHIPHPNO");
	        String shipcountry = header.get("SHIPCOUNTRY");
	        String shipzip = header.get("SHIPZIP");
	        String crat = header.get("CRAT");
	        String crby = header.get("CRBY");
	        String upat = header.get("UPAT");
	        String creditstatus = "OPEN";
	       
	        //HDR
	        String creditNo = _TblControlDAO.getNextOrder(plant,username,"CUSTOMER_CREDIT_NOTE");
	        Hashtable CreditnoteHdr = new Hashtable();
	        creditnoteDetInfoList = new ArrayList<Hashtable<String, String>>();
			CreditnoteHdr.put("PLANT", plant);
			CreditnoteHdr.put("CUSTNO", custno);
			CreditnoteHdr.put("CREDITNOTE", creditNo);
			CreditnoteHdr.put("DONO", "");
			CreditnoteHdr.put("ICRNO", irno);
			CreditnoteHdr.put("CREDIT_DATE", return_date);
			CreditnoteHdr.put("EMPNO", empno);
			CreditnoteHdr.put("ITEM_RATES", item_rates);
			CreditnoteHdr.put("DISCOUNT", discount);
			CreditnoteHdr.put("ORDER_DISCOUNT", order_discount);
			CreditnoteHdr.put("DISCOUNT_TYPE", discount_type);
			CreditnoteHdr.put("DISCOUNT_ACCOUNT", discount_account);
			CreditnoteHdr.put("SHIPPINGCOST", shippingcost);
			CreditnoteHdr.put("ADJUSTMENT", adjustment);
			CreditnoteHdr.put("ADJUSTMENT_LABEL", "");
			CreditnoteHdr.put("SUB_TOTAL", sub_total);
			CreditnoteHdr.put("TOTAL_AMOUNT", total_amount);
			CreditnoteHdr.put("CREDIT_STATUS", creditstatus);
			CreditnoteHdr.put("NOTE", note);
			CreditnoteHdr.put("TERMSCONDITIONS", "");
			CreditnoteHdr.put("CRAT", dateutils.getDateTime());
			CreditnoteHdr.put("CRBY", username);
			CreditnoteHdr.put("INVOICE", "");
			CreditnoteHdr.put("REFERENCE", "");
			CreditnoteHdr.put("TAXAMOUNT", taxamount);
			CreditnoteHdr.put("GINO", gino);
			CreditnoteHdr.put("SORETURN", "");
			CreditnoteHdr.put("CURRENCYUSEQT", currencyuseqt);
			CreditnoteHdr.put("ORDERDISCOUNTTYPE", orderdiscounttype);
			CreditnoteHdr.put("TAXID", taxid);
			CreditnoteHdr.put("ISDISCOUNTTAX", isdiscounttax);
			CreditnoteHdr.put("ISORDERDISCOUNTTAX", isorderdiscounttax);
			CreditnoteHdr.put("OUTBOUD_GST", outboud_gst);
			CreditnoteHdr.put("PROJECTID", "");
			CreditnoteHdr.put("SHIPPINGID", shippingid);
			CreditnoteHdr.put("SHIPPINGCUSTOMER", shippingcustomer);
			CreditnoteHdr.put("PAYMENT_TERMS", payment_terms);
			CreditnoteHdr.put("SALES_LOCATION", "");
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
			
			//DET
			if (creditNoteHdrId > 0) {
			for (Hashtable<String, String> item : returnDetlist) {
				String ITEM = item.get("ITEM");
				String LNNO = item.get("LNNO");
				String QTY = item.get("QTY");
				String UOM = item.get("UOM");
				String LOC = item.get("LOC");
				String BATCH = item.get("BATCH");
				String UNITPRICE = item.get("UNITPRICE");
				String UNITCOST = item.get("UNITCOST");
				String AMOUNT = item.get("AMOUNT");
				String DISCOUNTTYPE = item.get("DISCOUNT_TYPE");
			    String TAX_TYPE = item.get("TAX_TYPE");
			    String CURRENCYUSEQT = item.get("CURRENCYUSEQT");
			    String ACCOUNT_NAME = item.get("ACCOUNT_NAME");
			    String DISCOUNT = item.get("DISCOUNT");
			    String CRAT = item.get("CRAT");
			    String CRBY = item.get("CRBY");
			    String UPAT = item.get("ITEM");
			    
			    int lnno = Integer.valueOf(LNNO);
//			    int qty = Integer.valueOf(QTY);
			    
				String convDiscount=""; 
//				String convCost = String.valueOf((Double.parseDouble(UNITCOST) / Double.parseDouble(currencyuseqt)));
				String convCost = String.valueOf((Double.parseDouble(UNITPRICE) / Double.parseDouble(currencyuseqt)));
				
				if(!DISCOUNTTYPE.toString().contains("%"))
				{
					convDiscount = String.valueOf((Double.parseDouble(DISCOUNT) / Double.parseDouble(currencyuseqt)));
				}
				else
					convDiscount = (String) DISCOUNT;
				String convAmount = String.valueOf((Double.parseDouble(AMOUNT) / Double.parseDouble(currencyuseqt)));
				
				creditnoteDetInfo = new Hashtable<String, String>();
				creditnoteDetInfo.put("PLANT", plant);
				creditnoteDetInfo.put("LNNO", Integer.toString(lnno));
				creditnoteDetInfo.put("HDRID", Integer.toString(creditNoteHdrId));
				creditnoteDetInfo.put("ITEM", ITEM);
				creditnoteDetInfo.put("ACCOUNT_NAME", ACCOUNT_NAME);
				creditnoteDetInfo.put("QTY", QTY);
				creditnoteDetInfo.put("UNITPRICE", convCost);
				creditnoteDetInfo.put("DISCOUNT", convDiscount);
				creditnoteDetInfo.put("DISCOUNT_TYPE", DISCOUNTTYPE);
				creditnoteDetInfo.put("TAX_TYPE", TAX_TYPE);
				creditnoteDetInfo.put("AMOUNT", convAmount);
				creditnoteDetInfo.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
				creditnoteDetInfo.put("CRAT", dateutils.getDateTime());
				creditnoteDetInfo.put("CRBY", username);
				creditnoteDetInfo.put("UPAT", dateutils.getDateTime());
				creditnoteDetInfoList.add(creditnoteDetInfo);
			}
			isAdded = customerCreditDao.addMultipleCreditnoteDet(creditnoteDetInfoList, plant);
			
			if (isAdded) {
				Double orderDiscountFroma = Double.parseDouble(order_discount);
				if(orderdiscounttype.toString().equalsIgnoreCase("%"))
					order_discount=Double.toString((Double.parseDouble(sub_total)*Double.parseDouble(order_discount))/100);
				
				
				if(!creditstatus.equalsIgnoreCase("Draft"))
				{
				String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				//Journal Entry
				JournalHeader journalHead=new JournalHeader();
				journalHead.setPLANT(plant);
				journalHead.setJOURNAL_DATE(return_date);
				journalHead.setJOURNAL_STATUS("PUBLISHED");
				journalHead.setJOURNAL_TYPE("Cash");
				journalHead.setCURRENCYID(curency);
				journalHead.setTRANSACTION_TYPE("CUSTOMERCREDITNOTES");
				journalHead.setTRANSACTION_ID(creditNo);
				journalHead.setSUB_TOTAL(Double.parseDouble(sub_total));
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
					journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
					journalDetail.setACCOUNT_NAME((String) CNDet.get("ACCOUNT_NAME"));
					
					if(!orderdiscounttype.toString().equalsIgnoreCase("%")) {
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
				JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) custno);
				if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
					JSONObject cusJson=cusDAO.getCustomerName(plant, (String) custno);
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
					journalDetail_1.setCREDITS(Double.parseDouble(total_amount));
					journalDetails.add(journalDetail_1);
					
				}
				
				Double taxAmountFrom=Double.parseDouble(taxamount);
				if(taxAmountFrom>0)
				{
					JournalDetail journalDetail_2=new JournalDetail();
					journalDetail_2.setPLANT(plant);
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
				if(!order_discount.isEmpty())
				{
					orderDiscountFrom=Double.parseDouble(order_discount);
					orderDiscountFrom=(Double.parseDouble(sub_total)*orderDiscountFrom)/100;
				}
				if(discountFrom>0 || orderDiscountFrom>0)
				{
					if(!discount_type.isEmpty())
					{
						if(discount_type.equalsIgnoreCase("%"))
						{
							Double subTotalAfterOrderDiscount=Double.parseDouble(sub_total);
							discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
						}
					}
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
				for (Hashtable<String, String> item : returnDetlist) {
					String PRODUCTHDRID = item.get("PRODUCTHDRID");
					String ITEM = item.get("ITEM");
					String LNNO = item.get("LNNO");
					String QTY = item.get("QTY");
					String UOM = item.get("UOM");
					String LOC = item.get("LOC");
					String BATCH = item.get("BATCH");
					String UNITPRICE = item.get("UNITPRICE");
					String UNITCOST = item.get("UNITCOST");
					String AMOUNT = item.get("AMOUNT");
					String DISCOUNTTYPE = item.get("DISCOUNT_TYPE");
				    String TAX_TYPE = item.get("TAX_TYPE");
				    String CURRENCYUSEQT = item.get("CURRENCYUSEQT");
				    String ACCOUNT_NAME = item.get("ACCOUNT_NAME");
				    String DISCOUNT = item.get("DISCOUNT");
				    String CRAT = item.get("CRAT");
				    String CRBY = item.get("CRBY");
				    String UPAT = item.get("ITEM");
				    
				    int lnno = Integer.valueOf(LNNO);
//				    int qty = Integer.valueOf(QTY);
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_CREDIT_NOTE);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(return_date));														
					htMovHis.put(IDBConstants.ITEM, ITEM);
					String billqty = QTY;
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, creditNo);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
	//				if(dono.isEmpty())
	//					dono=reference;
					htMovHis.put("REMARKS",irno+","+note);
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
				invoicePaymentHeader.setCUSTNO(custno);
				invoicePaymentHeader.setAMOUNTRECEIVED(Double.parseDouble(total_amount));
				invoicePaymentHeader.setBANK_CHARGE(Double.parseDouble("0"));
				//invoicePaymentHeader.setBANK_BRANCH();
				//invoicePaymentHeader.setCHECQUE_NO(item.getString());
				invoicePaymentHeader.setRECEIVE_DATE(dateutils.getDB2UserDate_New(dateutils.getDateTime()));
				invoicePaymentHeader.setRECEIVE_MODE("Credit Note");
				invoicePaymentHeader.setREFERENCE(creditNo);
				invoicePaymentHeader.setNOTE(note);
				invoicePaymentHeader.setCURRENCYID(currencyid);
				invoicePaymentHeader.setTRANSACTIONID("");
				
			
				exceptionUtil=invPaymentUtil.addInvoicePaymentHdr(invoicePaymentHeader,plant,username);
				  if(exceptionUtil.getStatus()==Result.OK)
				  {
					invoicePaymentDetail.setPLANT(plant);
					invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt("0"));
//					invoicePaymentDetail.setDONO(irno);
					invoicePaymentDetail.setDONO("0");
					int receiveHeaderId=(int) exceptionUtil.getResultData();
					invoicePaymentDetail.setRECEIVEHDRID(receiveHeaderId);
					invoicePaymentDetail.setAMOUNT(Double.parseDouble(total_amount));
					invoicePaymentDetail.setLNNO(0);
					invoicePaymentDetail.setTYPE("ADVANCE");
					invoicePaymentDetail.setBALANCE(Double.parseDouble(total_amount));
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
					htMovHis.put("REMARKS",creditNo+","+invoicePaymentHeader.getAMOUNTRECEIVED());
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

			}
			result = "ok";
			}catch (Exception e) {
				 e.printStackTrace();
				 autoinverr=e.toString();
				 result = "not ok";
			}	
		
		return result;
	}
	
	
	public String createDebitnode(HttpServletRequest request,
			HttpServletResponse response,List<Hashtable<String,String>> returnHdr,List<Hashtable<String,String>> returnDetlist,String gino, String plant, String username) throws Exception {
			String result = "";
			boolean isAdded = false;
			
			MovHisDAO movHisDao = new MovHisDAO();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			DateUtils dateutils = new DateUtils();
			ExceptionUtil exceptionUtil=null;
			
			InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			BillPaymentUtil billPaymentUtil = new BillPaymentUtil();
			Hashtable<String,String> paymentDetInfo = null;
			List<Hashtable<String,String>> paymentDetInfoList = null;
			
			List<Hashtable<String, String>> CNDetInfoList = null;
			Hashtable<String, String> creditnoteDetInfo = null;
			Hashtable<String, String> CNDetInfo = null;
		
			try{
			Hashtable<String, String> header = returnHdr.get(0);
			String PLANT = header.get("PLANT");
			String irno = header.get("IDRNO");
//			String producthdrid = header.get("PRODUCTHDRID");
			String return_date = header.get("RETURN_DATE");
			String due_date = header.get("DUE_DATE");
			String empno = header.get("EMPNO");
			String custno = header.get("VENDNO");
			String custname = header.get("VNAME");
	        String ordertype = header.get("ORDERTYPE");
	        String return_status = header.get("RETURN_STATUS");
	        String payment_terms = header.get("PAYMENT_TERMS");
	        String transportid = header.get("TRANSPORTID");
	        String taxtreatment = header.get("TAXTREATMENT");
	        String jobnum = header.get("JobNum");
	        String note = header.get("NOTE");
	        String termsconditions = header.get("TERMSCONDITIONS");
	        String incoterms = header.get("INCOTERMS");
	        String item_rates = header.get("ITEM_RATES");
	        String currencyid = header.get("CURRENCYID");
	        String currencyuseqt = header.get("CURRENCYUSEQT");
	        String taxid = header.get("TAXID");
	        
	        String discount = header.get("DISCOUNT");
	        String isshippingtax = header.get("ISSHIPPINGTAX");
	        String discount_account = header.get("DISCOUNT_ACCOUNT");
	        String outboud_gst = header.get("INBOUND_GST");
	        String discount_type = header.get("DISCOUNT_TYPE");
	        String total_paying = header.get("TOTAL_PAYING");
	        String adjustment = header.get("ADJUSTMENT");
	        String order_discount = header.get("ORDER_DISCOUNT");
	        String isdiscounttax = header.get("ISDISCOUNTTAX");
	        String orderdiscounttype = header.get("ORDERDISCOUNTTYPE");
	        String isorderdiscounttax = header.get("ISORDERDISCOUNTTAX");
	        String total_amount = header.get("TOTAL_AMOUNT");
	        String taxamount = header.get("TAXAMOUNT");
	        String creditnotesstatus = header.get("CREDITNOTESSTATUS");
	        String sub_total = header.get("SUB_TOTAL");
	        
	        String shippingid = header.get("SHIPPINGID");
	        String shippingcustomer = header.get("SHIPCOUNTRY");
	        String shipcontactname = header.get("SHIPCONTACTNAME");
	        String shipstate = header.get("SHIPSTATE");
	        String shipdesgination = header.get("SHIPDESGINATION");
	        String shipaddr1 = header.get("SHIPADDR1");
	        String shipaddr2 = header.get("SHIPADDR2");
	        String shipaddr3 = header.get("SHIPADDR3");
	        String shipaddr4 = header.get("SHIPADDR4");
	        String shipemail = header.get("SHIPEMAIL");
	        String shippingcost = header.get("SHIPPINGCOST");
	        String shipworkphone = header.get("SHIPWORKPHONE");
	        String shiphpno = header.get("SHIPHPNO");
	        String shipcountry = header.get("SHIPCOUNTRY");
	        String shipzip = header.get("SHIPZIP");
	        String crat = header.get("CRAT");
	        String crby = header.get("CRBY");
	        String upat = header.get("UPAT");
	        String creditstatus = "OPEN";
	       
	        //HDR
	        String creditNo = _TblControlDAO.getNextOrder(plant,username,"CUSTOMER_CREDIT_NOTE");
	        String debitNo = _TblControlDAO.getNextOrder(plant,username,"SUPPLIER_CREDIT_NOTE");
	        Hashtable CreditnoteHdr = new Hashtable();
	        Hashtable CNHdr = new Hashtable();
	        CNDetInfoList = new ArrayList<Hashtable<String, String>>();
	        
	        CNHdr.put("PLANT", plant);
			CNHdr.put("VENDNO", custno);
			CNHdr.put("CREDITNOTE", debitNo);
			CNHdr.put("IDRNO", irno);
			CNHdr.put("PONO", "");
//			if(billnumber.equalsIgnoreCase("")) {
//				CNHdr.put("BILL", "");
//			}else {
//				CNHdr.put("BILL", billnumber);
//			}
//			if(grnnumber.equalsIgnoreCase("")) {
//				CNHdr.put("GRNO", "");
//			}else {
//				CNHdr.put("GRNO", grnnumber);
//			}
//			if(poreturn.equalsIgnoreCase("")) {
//				CNHdr.put("PORETURN", "");
//			}else {
//				CNHdr.put("PORETURN", poreturn);
//			}
//			CNHdr.put("PURCHASE_LOCATION", purchaseloc);
//			CNHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
//			CNHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
			CNHdr.put("GRNO", "");
			CNHdr.put("CREDIT_DATE", return_date);
			CNHdr.put("PAYMENT_TERMS", payment_terms);
			CNHdr.put("ITEM_RATE", item_rates);
			CNHdr.put("DISCOUNT", discount);
			CNHdr.put("ORDER_DISCOUNT", order_discount);
			CNHdr.put("DISCOUNT_TYPE", discount_type);
			CNHdr.put("DISCOUNT_ACCOUNT", discount_account);
			CNHdr.put("SHIPPINGCOST", shippingcost);
			CNHdr.put("ADJUSTMENT_LABEL", "");
			CNHdr.put("ADJUSTMENT", adjustment);
			CNHdr.put("SUB_TOTAL", sub_total);
			CNHdr.put("TOTAL_AMOUNT", total_amount);
			CNHdr.put("NOTE", note);	
			CNHdr.put("EMPNO", empno);	
			CNHdr.put("CRAT",dateutils.getDateTime());
			CNHdr.put("CRBY",username);
			CNHdr.put("UPAT",dateutils.getDateTime());
			CNHdr.put("TAXAMOUNT", taxamount);
			CNHdr.put("TAXTREATMENT",taxtreatment);
			CNHdr.put(IDBConstants.CURRENCYID, currencyid);
			CNHdr.put("CURRENCYUSEQT", currencyuseqt);
			CNHdr.put("ORDERDISCOUNTTYPE", orderdiscounttype);
			CNHdr.put("TAXID", taxid);
			CNHdr.put("ISDISCOUNTTAX", isdiscounttax);
			CNHdr.put("ISORDERDISCOUNTTAX", isorderdiscounttax);
			CNHdr.put("INBOUND_GST", outboud_gst);
			CNHdr.put("PROJECTID", "");
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
			CNHdr.put("SHIPPINGCUSTOMER", shippingcustomer);
			CNHdr.put("CREDIT_STATUS", creditstatus);
	        
			
			int creditNoteHdrId = 0;
			int CNHdrId = new SupplierCreditUtil().addSupplierCreditHdr(CNHdr, plant);
			
			//DET
			if (CNHdrId > 0) {
				for (Hashtable<String, String> item : returnDetlist) {
					String PRODUCTHDRID = item.get("PRODUCTHDRID");
					String ITEM = item.get("ITEM");
					String LNNO = item.get("LNNO");
					String QTY = item.get("QTY");
					String UOM = item.get("UOM");
					String LOC = item.get("LOC");
					String BATCH = item.get("BATCH");
					String UNITPRICE = item.get("UNITPRICE");
					String UNITCOST = item.get("UNITCOST");
					String AMOUNT = item.get("AMOUNT");
					String DISCOUNTTYPE = item.get("DISCOUNT_TYPE");
				    String TAX_TYPE = item.get("TAX_TYPE");
				    String IS_COGS_SET = item.get("IS_COGS_SET");
				    String CURRENCYUSEQT = item.get("CURRENCYUSEQT");
				    String ACCOUNT_NAME = item.get("ACCOUNT_NAME");
				    String ADDONTYPE = item.get("ADDONTYPE");
				    String ADDONAMOUNT = item.get("ADDONAMOUNT");
				    String DISCOUNT = item.get("DISCOUNT");
				    String CRAT = item.get("CRAT");
				    String CRBY = item.get("CRBY");
				    String UPAT = item.get("ITEM");
				    
				    int lnno = Integer.valueOf(LNNO);
					String convDiscount=""; 
					String convCost = String.valueOf((Float.parseFloat(UNITCOST) / Float.parseFloat(currencyuseqt)));
					
					if(!DISCOUNTTYPE.toString().contains("%"))
					{
						convDiscount = String.valueOf((Float.parseFloat(DISCOUNT) / Float.parseFloat(currencyuseqt)));
					}
					else
						convDiscount = (String) DISCOUNT;
					String convAmount = String.valueOf((Float.parseFloat(AMOUNT) / Float.parseFloat(currencyuseqt)));
					
					CNDetInfo = new Hashtable<String, String>();
					CNDetInfo.put("PLANT", plant);
					CNDetInfo.put("LNNO", Integer.toString(lnno));
					CNDetInfo.put("HDRID", Integer.toString(CNHdrId));
					CNDetInfo.put("ITEM", ITEM);
					CNDetInfo.put("ACCOUNT_NAME", ACCOUNT_NAME);
					CNDetInfo.put("QTY", QTY);
					CNDetInfo.put("COST", (String) convCost);
					CNDetInfo.put("DISCOUNT", convDiscount);
					CNDetInfo.put("DISCOUNT_TYPE", DISCOUNTTYPE);
					CNDetInfo.put("TAX_TYPE", TAX_TYPE);
					CNDetInfo.put("AMOUNT", convAmount);
					CNDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
					CNDetInfo.put("CRAT",dateutils.getDateTime());
					CNDetInfo.put("CRBY",username);
					CNDetInfo.put("UPAT",dateutils.getDateTime());
					CNDetInfoList.add(CNDetInfo);
				}			
				isAdded = new SupplierCreditUtil().addMultipleCreditDet(CNDetInfoList, plant);
				
				if(isAdded) {
					if(!creditstatus.equalsIgnoreCase("Draft"))
					{
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					//Journal Entry
					JournalHeader journalHead=new JournalHeader();
					journalHead.setPLANT(plant);
					journalHead.setJOURNAL_DATE(return_date);
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(curency);
					journalHead.setTRANSACTION_TYPE("SUPPLIERCREDITNOTES");
					journalHead.setTRANSACTION_ID(Integer.toString(CNHdrId));
					journalHead.setSUB_TOTAL(Double.parseDouble(sub_total));
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
						if(!orderdiscounttype.toString().equalsIgnoreCase("%")) {
							journalDetail.setCREDITS(Double.parseDouble(CNDet.get("AMOUNT").toString())-Double.parseDouble(order_discount)/CNDetInfoList.size());
						}else {
							Double jodamt = (Double.parseDouble(CNDet.get("AMOUNT").toString())/100)*Double.parseDouble(order_discount.toString());
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
					JSONObject coaJson1=coaDAO.getCOAByName(plant, custno);
					if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
						JSONObject vendorJson=vendorDAO.getVendorName(plant, custno);
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
					journalDetail_1.setDEBITS(Double.parseDouble(total_amount));
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
					if(!order_discount.isEmpty())
					{
						orderDiscountFrom=Double.parseDouble(order_discount);
						orderDiscountFrom=(Double.parseDouble(sub_total)*orderDiscountFrom)/100;
					}
					
					if(discountFrom>0 || orderDiscountFrom>0)
					{
						if(!discount_type.isEmpty())
						{
							if(discount_type.equalsIgnoreCase("%"))
							{
								Double subTotalAfterOrderDiscount=Double.parseDouble(sub_total);
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
				
				
				
				if(creditstatus.equalsIgnoreCase("Open")) {
					//Payment Save
					if(isAdded) {
						paymentDetInfoList = new ArrayList<Hashtable<String,String>>();
					Hashtable paymentHdr =new Hashtable();
					paymentHdr.put("PLANT", plant);
					paymentHdr.put("VENDNO", custno);
					paymentHdr.put("AMOUNTPAID", total_amount);
					paymentHdr.put("PAYMENT_DATE", return_date);
					paymentHdr.put("PAYMENT_MODE", "Cash");
					paymentHdr.put("PAID_THROUGH", "Advance");
					paymentHdr.put("REFERENCE", debitNo);
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
								paymentDetInfo.put("AMOUNT", total_amount);
								paymentDetInfo.put("BALANCE", total_amount);													
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
					htMovHis.put("REMARKS",debitNo+","+total_amount);
					movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
					
				if(isAdded) {
					for (Hashtable<String, String> item : returnDetlist) {
						
						String PRODUCTHDRID = item.get("PRODUCTHDRID");
						String ITEM = item.get("ITEM");
						String LNNO = item.get("LNNO");
						String QTY = item.get("QTY");
						String UOM = item.get("UOM");
						String LOC = item.get("LOC");
						String BATCH = item.get("BATCH");
						String UNITPRICE = item.get("UNITPRICE");
						String UNITCOST = item.get("UNITCOST");
						String AMOUNT = item.get("AMOUNT");
						String DISCOUNTTYPE = item.get("DISCOUNT_TYPE");
					    String TAX_TYPE = item.get("TAX_TYPE");
					    String IS_COGS_SET = item.get("IS_COGS_SET");
					    String CURRENCYUSEQT = item.get("CURRENCYUSEQT");
					    String ACCOUNT_NAME = item.get("ACCOUNT_NAME");
					    String ADDONTYPE = item.get("ADDONTYPE");
					    String ADDONAMOUNT = item.get("ADDONAMOUNT");
					    String DISCOUNT = item.get("DISCOUNT");
					    String CRAT = item.get("CRAT");
					    String CRBY = item.get("CRBY");
					    String UPAT = item.get("ITEM");
					    
					    int lnno = Integer.valueOf(LNNO);
					    
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.CREATE_DEBIT_NOTE);
					htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(return_date));														
					htMovHis.put(IDBConstants.ITEM, ITEM);
					String billqty = QTY;
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, debitNo);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
					htMovHis.put("REMARKS",custname+","+irno+","+note);
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
				
			}
			
			result = "ok";
			}catch (Exception e) {
				 e.printStackTrace();
				 autoinverr=e.toString();
				 result = "not ok";
			}	
		
		return result;
	}
	
	public String createreceiveHdr(HttpServletRequest request,
			HttpServletResponse response,List<Hashtable<String,String>> returnHdr,List<Hashtable<String,String>> returnDetlist,String gino, String plant, String username,
			String currentplantcustomer,String parentplant) throws Exception {
			String result = "";
			boolean isAdded = false;
			
			MovHisDAO movHisDao = new MovHisDAO();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			DateUtils dateutils = new DateUtils();
			CustUtil custUtil = new CustUtil();
			ExceptionUtil exceptionUtil=null;
			
			List<Hashtable<String,String>> receiveDetInfoList = null;
			Hashtable<String,String> receiveDetInfo = null;
			
			String sCustCode="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sTelNo="";
			
//			parentplant = plant;
			
			ArrayList arrCust = new CustUtil().getCustomerDetails(currentplantcustomer,parentplant);
			if (arrCust.size() > 0) {
				sCustCode = (String) arrCust.get(0);
				sCustName = (String) arrCust.get(1);
				sAddr1 = (String) arrCust.get(2);
				sAddr2 = (String) arrCust.get(3);
				sAddr3 = (String) arrCust.get(4);
				sTelNo = (String) arrCust.get(11);
			}else {
				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(parentplant);
				Map plntMap  = (Map) plntList.get(0);
				String COUNTRY = (String) plntMap.get("COUNTY");
				sAddr1 = (String) plntMap.get("ADD1");
				sAddr2 = (String) plntMap.get("ADD2");
				sAddr3 = (String) plntMap.get("ADD3");
				sCustCode = currentplantcustomer;
				sCustName = currentplantcustomer;
				String Supcurrency=new PlantMstDAO().getBaseCurrency(parentplant);
				Hashtable htsup = new Hashtable();
				htsup.put(IDBConstants.PLANT,parentplant);
				htsup.put(IConstants.CUSTOMER_CODE, currentplantcustomer);
				htsup.put(IConstants.CUSTOMER_NAME, currentplantcustomer);
				htsup.put("CURRENCY_ID", Supcurrency);
				htsup.put(IConstants.COUNTRY, COUNTRY);
				htsup.put(IConstants.ISACTIVE, "Y");
				htsup.put(IConstants.TAXTREATMENT, "Non GST Registered");
				htsup.put("CRAT",new DateUtils().getDateTime());
		        htsup.put("CRBY",username);
		        htsup.put("Comment1", " 0 ");
		        boolean custInserted = custUtil.insertCustomer(htsup);
			}
		
			try{
			Hashtable<String, String> header = returnHdr.get(0);
			String PLANT = header.get("PLANT");
			String idrno = header.get("IDRNO");
			String return_date = header.get("RETURN_DATE");
			String due_date = header.get("DUE_DATE");
			String empno = header.get("EMPNO");
			String custno = header.get("VENDNO");
			String custname = header.get("VNAME");
	        String ordertype = header.get("ORDERTYPE");
	        String return_status = header.get("RETURN_STATUS");
	        String payment_terms = header.get("PAYMENT_TERMS");
	        String transportid = header.get("TRANSPORTID");
	        String taxtreatment = header.get("TAXTREATMENT");
	        String jobnum = header.get("JobNum");
	        String note = header.get("NOTE");
	        String termsconditions = header.get("TERMSCONDITIONS");
	        String incoterms = header.get("INCOTERMS");
	        String item_rates = header.get("ITEM_RATES");
	        String currencyid = header.get("CURRENCYID");
	        String currencyuseqt = header.get("CURRENCYUSEQT");
	        String taxid = header.get("TAXID");
	        
	        String discount = header.get("DISCOUNT");
	        String isshippingtax = header.get("ISSHIPPINGTAX");
	        String discount_account = header.get("DISCOUNT_ACCOUNT");
	        String outboud_gst = header.get("INBOUND_GST");
	        String discount_type = header.get("DISCOUNT_TYPE");
	        String total_paying = header.get("TOTAL_PAYING");
	        String adjustment = header.get("ADJUSTMENT");
	        String order_discount = header.get("ORDER_DISCOUNT");
	        String isdiscounttax = header.get("ISDISCOUNTTAX");
	        String orderdiscounttype = header.get("ORDERDISCOUNTTYPE");
	        String isorderdiscounttax = header.get("ISORDERDISCOUNTTAX");
	        String total_amount = header.get("TOTAL_AMOUNT");
	        String taxamount = header.get("TAXAMOUNT");
	        String creditnotesstatus = header.get("CREDITNOTESSTATUS");
	        String sub_total = header.get("SUB_TOTAL");
	        
	        String shippingid = header.get("SHIPPINGID");
	        String shippingcustomer = header.get("SHIPCOUNTRY");
	        String shipcontactname = header.get("SHIPCONTACTNAME");
	        String shipstate = header.get("SHIPSTATE");
	        String shipdesgination = header.get("SHIPDESGINATION");
	        String shipaddr1 = header.get("SHIPADDR1");
	        String shipaddr2 = header.get("SHIPADDR2");
	        String shipaddr3 = header.get("SHIPADDR3");
	        String shipaddr4 = header.get("SHIPADDR4");
	        String shipemail = header.get("SHIPEMAIL");
	        String shippingcost = header.get("SHIPPINGCOST");
	        String shipworkphone = header.get("SHIPWORKPHONE");
	        String shiphpno = header.get("SHIPHPNO");
	        String shipcountry = header.get("SHIPCOUNTRY");
	        String shipzip = header.get("SHIPZIP");
	        String crat = header.get("CRAT");
	        String crby = header.get("CRBY");
	        String upat = header.get("UPAT");
	        String status = "OPEN";
	        
	        String icrno = _TblControlDAO.getNextOrder(parentplant,username,"ITEMRECEIVE");
	        
	        receiveDetInfoList = new ArrayList<Hashtable<String,String>>();
	        Hashtable receiveHdr =new Hashtable(); 
			receiveHdr.put("PLANT", parentplant);
			receiveHdr.put("CustCode", sCustCode);
			receiveHdr.put("CustName", sCustName);
			receiveHdr.put("ICRNO", icrno);
			receiveHdr.put("RECEIVE_DATE", return_date);
			receiveHdr.put("RECEIVE_STATUS", status);
			receiveHdr.put("STATUS", "N");
			receiveHdr.put("PAYMENT_TERMS", payment_terms);
			receiveHdr.put("EMPNO", empno);
			receiveHdr.put("ITEM_RATES", item_rates);
			receiveHdr.put("DISCOUNT", discount);
			receiveHdr.put("ORDER_DISCOUNT", order_discount);
			receiveHdr.put("DISCOUNT_TYPE", discount_type);
			receiveHdr.put("DISCOUNT_ACCOUNT", discount_account);
			receiveHdr.put("SHIPPINGCOST", shippingcost);
			receiveHdr.put("ADJUSTMENT", adjustment);
			receiveHdr.put("SUB_TOTAL", sub_total);
			receiveHdr.put("TOTAL_AMOUNT", total_amount);
			receiveHdr.put("NOTE", note);
			receiveHdr.put("CRAT",DateUtils.getDateTime());
			receiveHdr.put("CRBY",username);
			receiveHdr.put("UPAT",DateUtils.getDateTime());
			receiveHdr.put("TAXTREATMENT",taxtreatment);
			receiveHdr.put("TAXAMOUNT",taxamount);
			receiveHdr.put("SHIPPINGID",shippingid);
			receiveHdr.put("SHIPPINGCUSTOMER",shippingcustomer);
			receiveHdr.put("CURRENCYUSEQT",currencyuseqt);
			receiveHdr.put("ORDERDISCOUNTTYPE",orderdiscounttype);
			receiveHdr.put("TAXID",taxid);
			receiveHdr.put("ISDISCOUNTTAX",isdiscounttax);
			receiveHdr.put("ISORDERDISCOUNTTAX",isorderdiscounttax);
			receiveHdr.put("ISSHIPPINGTAX",isshippingtax);
			receiveHdr.put("TRANSPORTID",transportid);
			receiveHdr.put("INBOUND_GST",outboud_gst);
			receiveHdr.put(IDBConstants.CURRENCYID, currencyid);
			receiveHdr.put("JobNum",idrno);
			receiveHdr.put("SHIPCONTACTNAME",shipcontactname);
			receiveHdr.put("SHIPDESGINATION",shipdesgination);
			receiveHdr.put("SHIPWORKPHONE",shipworkphone);
			receiveHdr.put("SHIPHPNO",shiphpno);
			receiveHdr.put("SHIPEMAIL",shipemail);
			receiveHdr.put("SHIPCOUNTRY",shipcountry);
			receiveHdr.put("SHIPADDR1",shipaddr1);
			receiveHdr.put("SHIPADDR2",shipaddr2);
			receiveHdr.put("SHIPADDR3",shipaddr3);
			receiveHdr.put("SHIPADDR4",shipaddr4);
			receiveHdr.put("SHIPSTATE",shipstate);
			receiveHdr.put("SHIPZIP",shipzip);
			receiveHdr.put("CREDITNOTESSTATUS","0");
			receiveHdr.put("TOTAL_PAYING","0");
			int receiveHdrId=0;
			receiveHdrId = new ItemMstDAO().addProductReceiveHdr(receiveHdr, parentplant);
			
			//DET
			if (receiveHdrId > 0) {
				for (Hashtable<String, String> item : returnDetlist) {
					String PRODUCTHDRID = item.get("PRODUCTHDRID");
					String ITEM = item.get("ITEM");
					String LNNO = item.get("LNNO");
					String QTY = item.get("QTY");
					String UOM = item.get("UOM");
					String LOC = item.get("LOC");
					String BATCH = item.get("BATCH");
					String UNITPRICE = item.get("UNITPRICE");
					String UNITCOST = item.get("UNITCOST");
					String AMOUNT = item.get("AMOUNT");
					String DISCOUNTTYPE = item.get("DISCOUNT_TYPE");
				    String TAX_TYPE = item.get("TAX_TYPE");
				    String IS_COGS_SET = item.get("IS_COGS_SET");
				    String CURRENCYUSEQT = item.get("CURRENCYUSEQT");
				    String ACCOUNT_NAME = item.get("ACCOUNT_NAME");
				    String ADDONTYPE = item.get("ADDONTYPE");
				    String ADDONAMOUNT = item.get("ADDONAMOUNT");
				    String DISCOUNT = item.get("DISCOUNT");
				    String CRAT = item.get("CRAT");
				    String CRBY = item.get("CRBY");
				    
				    int lnno = Integer.valueOf(LNNO);
				    

					String convDiscount=""; 
					String convCost = String.valueOf((Double.parseDouble(UNITCOST) / Double.parseDouble(currencyuseqt)));
					if(!DISCOUNTTYPE.toString().contains("%"))
					{
						convDiscount = String.valueOf((Double.parseDouble(DISCOUNT) / Double.parseDouble(currencyuseqt)));
					}
					else
						convDiscount = DISCOUNT;
					String convAmount = String.valueOf((Double.parseDouble(AMOUNT) / Double.parseDouble(currencyuseqt)));
					
					receiveDetInfo = new Hashtable<String, String>();
					receiveDetInfo.put("PLANT", parentplant);
					receiveDetInfo.put("LNNO", Integer.toString(lnno));
					receiveDetInfo.put("LNSTAT", "N");
					receiveDetInfo.put("RECEIVEHDRID", Integer.toString(receiveHdrId));
					receiveDetInfo.put("ICRNO", icrno);
					receiveDetInfo.put("ITEM", ITEM);
					receiveDetInfo.put("ACCOUNT_NAME", ACCOUNT_NAME);
					receiveDetInfo.put("QTY", QTY);
					receiveDetInfo.put("QTYRC", "0");
					receiveDetInfo.put("UNITPRICE", UNITPRICE);
					receiveDetInfo.put("TAX_TYPE", TAX_TYPE);
					receiveDetInfo.put("AMOUNT", AMOUNT);
					receiveDetInfo.put("CRAT",DateUtils.getDateTime());
					receiveDetInfo.put("CRBY",username);
					receiveDetInfo.put("UPAT",DateUtils.getDateTime());
					receiveDetInfo.put("DISCOUNT", DISCOUNT);
					receiveDetInfo.put("ADDONAMOUNT", ADDONAMOUNT);
					receiveDetInfo.put("ADDONTYPE", ADDONTYPE);
					double uconv = ((Double.valueOf(UNITCOST)/(Double.valueOf(currencyuseqt))));
					receiveDetInfo.put("UNITCOST", UNITCOST);
					receiveDetInfo.put("DISCOUNT_TYPE", DISCOUNTTYPE);
					receiveDetInfo.put("UOM", UOM);	
					receiveDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
					receiveDetInfo.put("IS_COGS_SET", "N");
					receiveDetInfoList.add(receiveDetInfo);
				
				}	
				
				isAdded = new ItemMstDAO().addProductReceiveDet(receiveDetInfoList, parentplant);
				
				if(isAdded) {
					for (Hashtable<String, String> item : returnDetlist) {
						String PRODUCTHDRID = item.get("PRODUCTHDRID");
						String ITEM = item.get("ITEM");
						String QTY = item.get("QTY");
					    
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, parentplant);
					htMovHis.put("DIRTYPE", TransactionConstants.PRD_RECEIVE);
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htMovHis.put(IDBConstants.ITEM, ITEM);
					String billqty = QTY;
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, icrno);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS",idrno);
					
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, parentplant);
					htMovChk.put("DIRTYPE", TransactionConstants.PRD_RECEIVE);
					htMovChk.put(IDBConstants.ITEM, ITEM);
					htMovChk.put(IDBConstants.QTY, billqty);
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, icrno);
					isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%PRODUCT_RECEIVE%' ");
					if(!isAdded)	
					isAdded = movHisDao.insertIntoMovHis(htMovHis);
					}
					Hashtable htv = new Hashtable();				
	    			htv.put("PLANT", parentplant);
	    			htv.put("FUNC", "ITEMRECEIVE");
	    			isAdded = _TblControlDAO.isExisit(htv, "", parentplant);
	    			if (isAdded) 
	    			isAdded=_TblControlDAO.updateSeqNo("ITEMRECEIVE",parentplant);
				}
				
				
			}
			
			result = "ok";
			}catch (Exception e) {
				 e.printStackTrace();
				 autoinverr=e.toString();
				 result = "not ok";
			}	
		
		return result;
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



