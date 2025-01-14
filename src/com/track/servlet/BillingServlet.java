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
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.JournalDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ReturnOrderDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.BillDet;
import com.track.db.object.BillHdr;
import com.track.db.object.BillPojo;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.AgeingUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.service.Costofgoods;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
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

@WebServlet("/billing/*")
@SuppressWarnings({"rawtypes", "unchecked"})
public class BillingServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.BillingServlet_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.BillingServlet_PRINTPLANTMASTERINFO;
	String action = "";
	ArrayList ALRecvddetails = new ArrayList();

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		action = StrUtils.fString(request.getParameter("Submit")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (baction.equals("VIEW_BILL_SUMMARY_VIEW")) {

	    	  
	        jsonObjectResult = this.getbillview(request);
	      //this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    } else if (baction.equals("VIEW_PURCHASE_SUMMARY_VIEW")) {
	    	  
	        jsonObjectResult = this.getpurchaseview(request);
	      //this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    } else if (baction.equals("VIEW_BILLPAYMENT_SUMMARY")) {
	    	  
	        jsonObjectResult = this.getbillpaymentview(request);	      
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    } else if (baction.equals("VIEW_GRNOTOBILL_SUMMARY")) {
	    	  
	        jsonObjectResult = this.getgrntobillview(request);	      
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    }
	    else if (baction.equals("VIEW_BILL_AGING_SUMMARY_VIEW")) {
	    	  
	        jsonObjectResult = this.getBillAgingView(request);	      
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    } else if (action.equalsIgnoreCase("GET_BILL_NO_FOR_AUTO_SUGGESTION")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getBillNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} else if (action.equalsIgnoreCase("GET_BILL_NO_FOR_AUTO_SUGGESTION_PO_RETURN")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getBillNoForAutoSuggestionForPoReturn(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} else if (action.equalsIgnoreCase("GET_EDIT_BILLING_DETAILS")) {
			jsonObjectResult = new JSONObject();
			jsonObjectResult = this.getEditBillingDetails(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}

		
		if (baction.equals("VIEW_PURCHASE_DASHBOARD_VIEW")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	        jsonObjectResult = this.getPurchaseDashbordview(request,plant);
	      //this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
			response.setContentType("application/json");
			//((ServletRequest) response).setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();

	       	       
	    }
		if (action.equalsIgnoreCase("CheckOrderno")) {
			
			String orderno = StrUtils.fString(request.getParameter("BILL")).trim();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("BILL", orderno);
				if (new BillDAO().isExisit(ht)) {
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

		if(action.equalsIgnoreCase("Save")) {
			/* BillHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			boolean Isconvcost=false;
			String vendno = "", billNo = "", pono = "", billDate = "", dueDate = "", payTerms = "",
			itemRates = "", discount = "", discountType = "", discountAccount = "", shippingCost = "", orderdiscount="",currencyid="",currencyuseqt="0",
			adjustment = "", adjustmentLabel = "", subTotal = "", totalAmount = "", billStatus = "", note = "",grno="",taxamount="",isshtax="0",
			shipRef = "", refNum = "",paction="",vendname="",purchaseloc = "",invsalesloc="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",isgrn="0",
			orddiscounttype="",taxid="0",isdiscounttax="0",isorderdiscounttax="0",gst="0",projectid="",transportid="",empno="",ORDERTYPE="",empName="",shippingid="",shippingcust="",
			origin = "", deductInv = "", billtype = "",curency="";
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="",jdesc="";
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
//			DateUtils dateutils = new DateUtils();
			RecvDetDAO recvDao = new RecvDetDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			boolean isAdded = false;
			String result="";
			//int idCount = 0, convcostCount = 0;
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
					taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, landedCostCount = 0,cost_aodCount=0,amount_aodCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0, ordQtyCount = 0,
					delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
					delLnItemCount=0;
			String polnnoIn = "";
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
//				StrUtils strUtils = new StrUtils();
				billAttachmentList = new ArrayList<Hashtable<String,String>>();
				billAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					System.out.println(fileItem.getFieldName().toString());
					
					/* BillHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("bill")) {
							billNo = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_date")) {
							billDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("due_date")) {
							dueDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							payTerms = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
							empno=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
							empName=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
							ORDERTYPE=StrUtils.fString(fileItem.getString()).trim();
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
							shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}

					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustmentLabel")) {
							adjustmentLabel = StrUtils.fString(fileItem.getString()).trim();
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxTotal")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							billStatus = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note = StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("grno")) {
							grno = StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("shipRef")) {
							shipRef = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("refNum")) {
							refNum = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("action")) {
							paction = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
							vendname = StrUtils.fString(fileItem.getString()).trim();
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

						if (fileItem.getFieldName().equalsIgnoreCase("isgrn")) {
							isgrn=StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
							isshtax=StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currencyid=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
							orddiscounttype=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("discounttaxstatus")) {
							isdiscounttax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
							isorderdiscounttax=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							gst=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								transportid = StrUtils.fString(fileItem.getString()).trim();
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
						
						if (fileItem.getFieldName().equalsIgnoreCase("ORIGIN")) {
							
							origin = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("DEDUCT_INV")) {
							
							deductInv = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("BillType")) {
							
							billtype = StrUtils.fString(fileItem.getString()).trim();
						}
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Bill" + "/"+ vendno + "/"+ billNo;
						String filetempLocation = "C:/ATTACHMENTS/Bill" + "/temp" + "/"+ vendno + "/"+ billNo;
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

						billAttachment = new Hashtable<String, String>();
						billAttachment.put("PLANT", plant);
						billAttachment.put("FILETYPE", fileItem.getContentType());
						billAttachment.put("FILENAME", fileName);
						billAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						billAttachment.put("FILEPATH", fileLocation);
						billAttachment.put("CRAT",DateUtils.getDateTime());
						billAttachment.put("CRBY",username);
						billAttachment.put("UPAT",DateUtils.getDateTime());
						billAttachmentList.add(billAttachment);
					}
					
					/*BillDet*/
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
						if (fileItem.getFieldName().equalsIgnoreCase("convertedunitcost_aod")) {
							cost_aod.add(cost_aodCount, StrUtils.fString(fileItem.getString()).trim());
							cost_aodCount++;
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
							detDiscountType.add(detDiscountTypeCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscountTypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
							/*if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
							else*/
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
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("landedCost")) {
							landedCost.add(landedCostCount, StrUtils.fString(fileItem.getString()).trim());
							landedCostCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("convcost")) {
							if(Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())>0) //Removed By: Azees, On: 14.12.21, Desc: Zero bill error
							{
							Isconvcost=true;
							}
							convcost.add(costCount, StrUtils.fString(fileItem.getString()).trim());
//							convcostCount++;
							
						}

					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amount_aod")) {
							amount_aod.add(amount_aodCount, StrUtils.fString(fileItem.getString()).trim());
							amount_aodCount++;
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
					
				}
				}
				
//				//imti added for (if bill number alreday exits then it take the next bill number from the tbl and replace the exit num with new number)
//				Hashtable hts = new Hashtable();
//				hts.put(IDBConstants.PLANT, plant);
//				hts.put("BILL", billNo);
//				if (new BillDAO().isExisit(hts)) {
//					billNo = new TblControlDAO().getNextOrder(plant,username,"BILL");
//				}
				//Check Bill Already Exists
				if(!billNo.isEmpty())
				{
					Hashtable htValues = new Hashtable();
					htValues.put("A.PLANT", plant);
					htValues.put("B.BILL", billNo);
					isAdded  = new BillDAO().isExisit(htValues,"");
					if(isAdded)
					response.sendRedirect("../bill/new?action="+paction+"&PONO="+pono+"&GRNO="+grno+"&VEND_NAME="+vendname+"&VENDNO="+vendno+"&result=Bill already available");
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
				
				if(!discountType.toString().equalsIgnoreCase("%"))
				discount = String.valueOf((Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));
				
				if(!orddiscounttype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Double.parseDouble(StrUtils.fString(orderdiscount)) / (Double.parseDouble(currencyuseqt))));
				

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
				billHdr.put("ORDERTYPE", ORDERTYPE);
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
//				List<ItemCogs> lstCogs=new ArrayList<>();
				ItemMstDAO itemmstDao=new ItemMstDAO();
				Costofgoods costofGoods=new CostofgoodsImpl();
				double expenseAmt=0.0;
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int billHdrId = billUtil.addBillHdr(billHdr, plant);	
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
						billDetInfo.put("LNNO", Integer.toString(lnno));
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
							billDetInfo.put("BATCH", (String) batch.get(i));
						}
						if(!pono.equalsIgnoreCase("")&& !grno.equalsIgnoreCase("")){
//								if(deductInv.equalsIgnoreCase("1")&& !pono.equalsIgnoreCase("")&& !grno.equalsIgnoreCase("")){
							billDetInfo.put("UOM", (String) uom.get(i));
//							}else if(deductInv.equalsIgnoreCase("1") && !grno.equalsIgnoreCase("")){
						}else if(!grno.equalsIgnoreCase("")){
							billDetInfo.put("UOM", (String) uom.get(i));
						}else {
							billDetInfo.put("UOM", "");
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
					int attchSize = billAttachmentList.size();
					for(int i =0 ; i < attchSize ; i++){
						billAttachment = new Hashtable<String, String>();
						billAttachment = billAttachmentList.get(i);
						billAttachment.put("BILLHDRID", Integer.toString(billHdrId));
						billAttachmentInfoList.add(billAttachment);
					}
					if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
						BillDAO billDao=new BillDAO();
						for(int i=0;i<landedCostLst.size();i++) {
								int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
								System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
						}
					}
					if(isAdded) {
						if(billAttachmentInfoList.size() > 0)
							isAdded = billUtil.addBillAttachments(billAttachmentInfoList, plant);

						
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
//								TODO : Ravindra - Get verified
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
									journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) - Double.parseDouble(orderdiscount.toString())/billDetInfoList.size());
								}else {
									Double jodamt = (Double.parseDouble(billDet.get("Amount").toString())/100)*Double.parseDouble(orderdiscount.toString());
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
										jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
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
								//journalDetail_1.setACCOUNT_NAME((String) vendno);
								journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_1);
							}

							
							
							Double taxAmountFrom=Double.parseDouble(taxamount);
							if(taxAmountFrom>0)
							{
								JournalDetail journalDetail_2=new JournalDetail();
								journalDetail_2.setPLANT(plant);
								journalDetail_2.setDESCRIPTION(jdesc+"-"+billNo);
								
								//JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								//journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								//journalDetail_2.setACCOUNT_NAME("VAT Input");
								
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
							/*if(!orderdiscount.isEmpty()){
								orderDiscountFrom=Double.parseDouble(orderdiscount);
								orderDiscountFrom=(Double.parseDouble(subTotal)*orderDiscountFrom)/100;
							}*/
								
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
								Hashtable ht = new Hashtable();
								ht.put("PLANT",plant);
								ht.put("SHIPMENT_CODE",shipRef);
								ht.put("PONO",pono);
								List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
								for(int j=0;j < expenseHdrList.size();j++) {
//									Journal journal_2=new Journal();
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
									
//									double expenseTaxAmount = 0.0;
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
//											expenseTaxAmount = Double.parseDouble((String)arrCustLine.get("TAXAMOUNT").toString());
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
//					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));														
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
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
				
				/* Added by Azees for inventory*/

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
						invmap.put("BILL", billNo);
						invmap.put(IConstants.PODET_PONUM, "");
						invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
						invmap.put(IConstants.VENDOR_CODE, vendno);
						invmap.put(IConstants.LOC, LOC);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, vendno);
						
						invmap.put(IConstants.BATCH, BATCH);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put(IConstants.RECV_QTY, QTY);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						if (billDate.length()>5)
							strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put("RETURN_DATE", strTranDate);
						invmap.put("UOMQTY", uomQty);
						invmap.put("NOTES", "");
						invmap.put("PORETURN", "");
						invmap.put(IConstants.GRNO, "");
						invmap.put(IConstants.CUSTOMER_NAME, vendno);
						isAdded = processPodetForReversal(invmap);
						if(isAdded) {
							processInvRemove(invmap);
						}
						if(isAdded) {
							processMovHis_OUT(invmap);
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
				if(isAdded && billStatus.equalsIgnoreCase("Open") && deductInv.equalsIgnoreCase("1")) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
						htCondiShipHis.put("GRNO", billNo);
						htCondiShipHis.put("STATUS","C");
						htCondiShipHis.put("BATCH",(String) batch.get(i));
						htCondiShipHis.put("LNNO", Integer.toString(lnno));
						htCondiShipHis.put("LOC", (String) loc.get(i));
						htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
						boolean isexists = new RecvDetDAO().isExisit(htCondiShipHis, plant);

						
						double costex = Double.valueOf((String) cost.get(i))/Double.valueOf(currencyuseqt);
						String scostex = String.valueOf(costex);
						if(isexists){
					        String query = "set UNITCOST=" + scostex + ",ORDQTY=" + (String) qty.get(i);
					        isAdded = new RecvDetDAO().update(query,htCondiShipHis,"",plant);
			            }
						

						/**/
						String QTY = "";//, ORDQTY = "";
						boolean processInv = false;
						if(ordQty.size() == 0) {
							QTY = (String) qty.get(i);
//							ORDQTY = (String) qty.get(i);
							processInv = true;
						}else {
							if(ordQty.size()> i) {
//								ORDQTY = (String) qty.get(i);
								double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
								double i_Qty = Double.parseDouble((String) qty.get(i));
								i_Qty = i_Qty - i_OrdQty;							
								QTY = Double.toString(i_Qty);
								if(i_Qty > 0) {
									processInv = true;
								}
							}else {
								QTY = (String) qty.get(i);
//								ORDQTY = (String) qty.get(i);
								processInv = true;
							}
						}
						/**/

					
					if(processInv) { 
//					String ITEM_QTY="";
					Map invmap = new HashMap();
						String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));
						
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails((String) uom.get(i), plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap = new HashMap();
						invmap.put(IConstants.PLANT, plant);
						invmap.put(IConstants.ITEM, (String) item.get(i));
						invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, (String) item.get(i)));
						invmap.put("BILL", grno);
						invmap.put("BILLNO", billNo);
						invmap.put(IConstants.PODET_PONUM, "");
						invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
						invmap.put(IConstants.VENDOR_CODE, vendno);
						invmap.put(IConstants.LOC, (String) loc.get(i));
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, vendno);
						invmap.put(IConstants.CUSTOMER_NAME, vendname);
						invmap.put(IDBConstants.USERFLD4, (String) batch.get(i));
						invmap.put(IConstants.BATCH, (String) batch.get(i));
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put(IConstants.RECV_QTY, QTY);
						invmap.put("UOMQTY", uomQty);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						invmap.put(IDBConstants.CURRENCYID, currencyid);
						invmap.put("UNITCOST", scostex);
						invmap.put(IDBConstants.CREATED_BY, username);
						if (billDate.length()>5)
							strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put(IConstants.RECVDATE, billDate);
						invmap.put(IConstants.EXPIREDATE, "");
						invmap.put(IConstants.NONSTKFLAG, ISNONSTKFLG);						
						invmap.put(IConstants.UOM, (String) uom.get(i));						
						invmap.put(IConstants.RECVDET_TRANTYPE, "BILL");						
						
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {							
								isAdded = processInvMst(invmap);														
						}
						if(isAdded) {
							isAdded = processPodetForReceive(invmap);
						}
						if(isAdded) {
							processMovHis_IN(invmap);
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
						if(!isAdded) {
							break process;
						}
					}
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
//					String sql = "SELECT ISNULL(AMOUNT,0) AMOUNT FROM " + plant +"_FINGRNOTOBILL A WHERE A.PLANT='"+ plant+"'";					
//					List arrList = billDao.selectForReport(sql, htCondition, "");
//					Map grnbill=(Map)arrList.get(0);
//					float grnamt=Float.parseFloat((String) grnbill.get("AMOUNT"));
					
//					String sql = "SELECT ISNULL(SUM(AMOUNT),0) AMOUNT FROM " + plant +"_FINBILLDET B JOIN " + plant +"_FINBILLHDR A on A.ID=B.BILLHDRID WHERE A.PLANT='"+ plant+"'";					
//					List billarrList = billDao.selectForReport(sql, htCondition, "");
//					Map billdet=(Map)billarrList.get(0);
//					float billamt=Float.parseFloat((String) billdet.get("AMOUNT"));
					/*if(grnamt<=billamt)*/
						isAdded = billDao.updateGrntoBill("SET STATUS='BILLED' ", htCondition, "", plant);
					/*else
						isAdded = billDao.updateGrntoBill("SET STATUS='PARTIALLY BILLED' ", htCondition, "", plant);*/
					}
					if(shipRef.length()>0) {
						ExpensesDAO expDao = new ExpensesDAO();
						String query = " SET STATUS='BILLED' ";
						Hashtable<String, String> htCondition = new Hashtable<String, String>();
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
				/* Added by Abhilash to handle COGS */

				
				
				if(isAdded) {
					new TblControlUtil().updateTblControlIESeqNo(plant, "GRN", "GN", grno);
					DbBean.CommitTran(ut);
					result = "Bill created successfully!";
				}else {
					DbBean.RollbackTran(ut);
		 			result = "Bill not created";
				}
				if(result.equalsIgnoreCase("Bill not created")) {
					if (ajax) {
						JSONObject resultJson = new JSONObject();
						resultJson.put("MESSAGE", result);
						resultJson.put("ERROR_CODE", "99");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					} else {
						response.sendRedirect("../bill/new?action="+paction+"&PONO="+pono+"&GRNO="+grno+"&VEND_NAME="+vendname+"&VENDNO="+vendno+"&result="+result);
					}
				}else {
					if (ajax) {
						try {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.BILL);
							if ("bill".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								billPDFGenration(request, response, billNo, billHdrId);
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
							resultJson.put("MESSAGE", "Bill Added Successfully and email not sent");
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}
					} else {
						response.sendRedirect("../bill/summary?result="+ result);/* Redirect to Bill Summary page */
					}
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (ajax) {
					JSONObject resultJson = new JSONObject();
					resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				} else {
					response.sendRedirect("../bill/new?action=" + paction + "&PONO=" + pono + "&GRNO=" + grno
							+ "&VEND_NAME=" + vendname + "&VENDNO=" + vendno + "&result=" + ThrowableUtil.getMessage(e));
				}
			}
		}
		if(action.equalsIgnoreCase("Update")) {
			/* BillHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", billNo = "", pono = "", billDate = "", dueDate = "", payTerms = "",orderdiscount="",
			itemRates = "", discount = "", discountType = "", shippingCost = "", taxamount="",isshtax="0",//discountAccount = "", 
			adjustment = "", adjustmentLabel = "", subTotal = "", totalAmount = "", billStatus = "", note = "",grno="",
			shipRef = "", refNum = "", billHdrId="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="",currencyid="",currencyuseqt="0",
			orddiscounttype="",taxid="0",isdiscounttax="0",isorderdiscounttax="0",gst="0",projectid="",transportid="",ORDERTYPE="",empno="",empName="",shippingid="",shippingcust="",
					deductInv = "", curency = "";//origin = "",  
			String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
					shipworkphone="",shipcountry="",shiphpno="",shipemail="",jdesc="";
			boolean Isconvcost=false;
			/*BillDet*/
			List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(), returnQty = new ArrayList(), netQty = new ArrayList(),
					cost = new ArrayList(), detDiscount = new ArrayList(),detDiscountType = new ArrayList(), taxType = new ArrayList(),
					amount = new ArrayList(), polnno = new ArrayList(), landedCost = new ArrayList(), 
					convcost= new ArrayList(), amount_aod = new ArrayList(), amount_aor = new ArrayList();
			List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),//index = new ArrayList(),
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
			BillDAO billDAO = new BillDAO();
//			DateUtils dateutils = new DateUtils();
			MovHisDAO movHisDao = new MovHisDAO();
//			RecvDetDAO recvDao = new RecvDetDAO();
			boolean isUpdated = false;
			String result="";
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
					taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, landedCostCount=0,// convcostCount=0,
					amount_aodCount=0, returnQtyCount=0, amount_aorCount=0, netQtyCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0, ordQtyCount = 0, //idCount = 0, 
					delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,convcostCount  = 0,
					delLnItemCount=0;
//			String polnnoIn = "";
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
//				StrUtils strUtils = new StrUtils();
				billAttachmentList = new ArrayList<Hashtable<String,String>>();
				billAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* BillHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("bill")) {
							billNo = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("pono")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_date")) {
							billDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("due_date")) {
							dueDate = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
							payTerms = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
							empno=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
							ORDERTYPE=StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
							empName=StrUtils.fString(fileItem.getString()).trim();
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
							shippingCost = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("adjustmentLabel")) {
							adjustmentLabel = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
							//adjustment = StrUtils.fString(fileItem.getString()).trim();
							adjustment = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("sub_total")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amount")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxTotal")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Double.parseDouble(StrUtils.fString(fileItem.getString()).trim())) / (Double.parseDouble(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_status")) {
							billStatus = StrUtils.fString(fileItem.getString()).trim();
						}

					
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note = StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("grno")) {
							grno = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("shipRef")) {
							shipRef = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("refNum")) {
							refNum = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("billHdrId")) {
							billHdrId = StrUtils.fString(fileItem.getString()).trim();
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
						if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
							isshtax=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
							currencyid=StrUtils.fString(fileItem.getString()).trim();
						}

						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}

						
						
						if (fileItem.getFieldName().equalsIgnoreCase("oddiscount_type")) {
							orddiscounttype=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("discounttaxstatus")) {
							isdiscounttax=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
							isorderdiscounttax=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
							gst=StrUtils.fString(fileItem.getString()).trim();
						}

						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								transportid = StrUtils.fString(fileItem.getString()).trim();
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
						
//						if (fileItem.getFieldName().equalsIgnoreCase("ORIGIN")) {
//							
//							origin = StrUtils.fString(fileItem.getString()).trim();
//						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("DEDUCT_INV")) {
							
							deductInv = StrUtils.fString(fileItem.getString()).trim();
						}
					}
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Bill" + "/"+ vendno + "/"+ billNo;
						String filetempLocation = "C:/ATTACHMENTS/Bill" + "/temp" + "/"+ vendno + "/"+ billNo;
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

						billAttachment = new Hashtable<String, String>();
						billAttachment.put("PLANT", plant);
						billAttachment.put("FILETYPE", fileItem.getContentType());
						billAttachment.put("FILENAME", fileName);
						billAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
						billAttachment.put("FILEPATH", fileLocation);
						billAttachment.put("CRAT",DateUtils.getDateTime());
						billAttachment.put("CRBY",username);
						billAttachment.put("UPAT",DateUtils.getDateTime());
						billAttachmentList.add(billAttachment);
					}
					/*BillDet*/
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
							detDiscountType.add(detDiscountTypeCount, StrUtils.fString(fileItem.getString()).trim());
							detDiscountTypeCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
							/*if(fileItem.getString().equalsIgnoreCase("EXEMPT") || fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
								taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()+"[0.0%]").trim());
							else*/
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
					if (fileItem.isFormField()) {
						System.out.println(fileItem.getFieldName());
						System.out.println(fileItem);
						if (fileItem.getFieldName().equalsIgnoreCase("landedCost")) {
							System.out.println("-------------");
							System.out.println(fileItem.getString());
							System.out.println("-------------");
							landedCost.add(landedCostCount, StrUtils.fString(fileItem.getString()).trim());
							landedCostCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("convcost")) {
							if(Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())>0) //Removed By: Azees, On: 14.12.21, Desc: Zero bill error
							{
							Isconvcost=true;
							}
							convcost.add(convcostCount, StrUtils.fString(fileItem.getString()).trim());
							convcostCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amount_aod")) {
							amount_aod.add(amount_aodCount, StrUtils.fString(fileItem.getString()).trim());
							amount_aodCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("returnQty")) {
							returnQty.add(returnQtyCount, StrUtils.fString(fileItem.getString()).trim());
							returnQtyCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("netQty")) {
							netQty.add(netQtyCount, StrUtils.fString(fileItem.getString()).trim());
							netQtyCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amount_aor")) {
							amount_aor.add(amount_aorCount, StrUtils.fString(fileItem.getString()).trim());
							amount_aorCount++;
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
				}

				}
				
				if(!discountType.toString().equalsIgnoreCase("%"))
					discount = String.valueOf((Double.parseDouble(StrUtils.fString(discount)) / (Double.parseDouble(currencyuseqt))));
					
				if(!orddiscounttype.toString().equalsIgnoreCase("%"))
					orderdiscount = String.valueOf((Double.parseDouble(StrUtils.fString(orderdiscount)) / (Double.parseDouble(currencyuseqt))));
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
				billDetInfoList = new ArrayList<Hashtable<String,String>>();
				ExpensesDAO expenseDao=new ExpensesDAO();
				CoaDAO coaDao=new CoaDAO();
//				BillDAO itemCogsDao=new BillDAO();
				List<CostofgoodsLanded> landedCostLst=new ArrayList<>();
//				List<ItemCogs> lstCogs=new ArrayList<>();
				ItemMstDAO itemmstDao=new ItemMstDAO();
				Costofgoods costofGoods=new CostofgoodsImpl();
				double expenseAmt=0.0;
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				String query = " SET REFERENCE_NUMBER='"+refNum+"', BILL_DATE='"+billDate+"', EMPNO='"+empno+"',ORDERTYPE='"+ORDERTYPE+"', ITEM_RATES='"+itemRates+"',ISSHIPPINGTAXABLE='"+isshtax+"',"
						+ " DUE_DATE='"+dueDate+"', PAYMENT_TERMS='"+payTerms+"', ADJUSTMENT_LABEL='"+adjustmentLabel+"', "
						+ " PROJECTID='"+projectid+"', TRANSPORTID='"+transportid+"', SHIPPINGID='"+shippingid+"', SHIPPINGCUSTOMER='"+shippingcust+"', "
						+ " SHIPCONTACTNAME='"+shipcontactname+"', SHIPDESGINATION='"+shipdesgination+"', SHIPWORKPHONE='"+shipworkphone+"', SHIPHPNO='"+shiphpno+"', "
						+ " SHIPEMAIL='"+shipemail+"', SHIPCOUNTRY='"+shipcountry+"', SHIPADDR1='"+shipaddr1+"', SHIPADDR2='"+shipaddr2+"', "
						+ " SHIPADDR3='"+shipaddr3+"', SHIPADDR4='"+shipaddr4+"', SHIPSTATE='"+shipstate+"', SHIPZIP='"+shipzip+"', "
						+ " DISCOUNT='"+discount+"', DISCOUNT_TYPE='"+discountType+"', ORDER_DISCOUNT='"+orderdiscount+"', INBOUND_GST='"+gst+"', "
						+ " SHIPPINGCOST='"+shippingCost+"', ADJUSTMENT='"+adjustment+"', REVERSECHARGE='"+sREVERSECHARGE+"', GOODSIMPORT='"+sGOODSIMPORT+"', "
						+ " ORDERDISCOUNTTYPE='"+orddiscounttype+"', TAXID='"+taxid+"', ISDISCOUNTTAX='"+isdiscounttax+"', ISORDERDISCOUNTTAX='"+isorderdiscounttax+"', "
						+ " SUB_TOTAL='"+subTotal+"', TOTAL_AMOUNT='"+totalAmount+"', NOTE='"+note+"', TAXAMOUNT='"+taxamount+"', TAXTREATMENT='"+taxtreatment+"' ";
				if(billStatus.contentEquals("Draft") || billStatus.contentEquals("Open")) {
					query=query+" ,BILL_STATUS='Open' ";
				}
				if(billStatus.contentEquals("CANCELLED")) {
					query=query+" ,BILL_STATUS='Draft' ";
				}
				Hashtable ht = new Hashtable();
				ht.put("ID", billHdrId);
				ht.put("PLANT", plant);
				isUpdated = billDAO.updateBillHdr(query, ht, "");
				

					List<String> expensesId=expenseDao.getExpesesHDR(pono, plant,shipRef);
					boolean isBasedOnCost=false,isBasedOnWeight=false;
					List<CostofgoodsLanded> expensesAccount = new ArrayList<CostofgoodsLanded>();
					if(expensesId.size() > 0 && (!pono.equalsIgnoreCase(""))) {
						expensesAccount=expenseDao.getExpesesDET(expensesId, plant);
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
						
						for(int i=0;i<expensesAccount.size();i++) {
							if(expensesAccount.get(i).getLandedcostcal().equals("1")) {
								isBasedOnCost=true;
							}if(expensesAccount.get(i).getLandedcostcal().equals("2")) {
								isBasedOnWeight=true;
							}
						}
					}
					
					int itemCnt=0;
					Double avg_rate=0.0;
					if(netQty.size()==0) {
						netQty = qty;
					}
					if(amount_aor.size()==0) {
						amount_aor = amount;
					}
					
					Double weightedQty=costofGoods.calculateWeightedQty(item,netQty,plant);
					Double subtotal=costofGoods.getProductSubtotalAmount(amount_aor);
					for(int i=0;i<item.size();i++) {
						itemCnt=item.size();
						CostofgoodsLanded costof=new CostofgoodsLanded();
						costof.setProd_id((String)item.get(i));
						CostofgoodsLanded weight=itemmstDao.getItemMSTDetails((String)item.get(i), plant);
						//	TODO : Ravindra - Get verified
						if (weight == null) {
							weight = new CostofgoodsLanded();
						}
						costof.setWeight(weight.getNet_weight());
						costof.setQuantity(Double.parseDouble((String)netQty.get(i)));
						costof.setWeight_qty(weightedQty);
						
						CostofgoodsLanded reqObj=new CostofgoodsLanded();
						reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
						reqObj.setDiscount(Double.parseDouble(discount));
						reqObj.setDiscountType(discountType);
						reqObj.setShippingCharge(Double.parseDouble(shippingCost));
						reqObj.setLstamount(amount_aor);
						reqObj.setSub_total(subtotal);
						reqObj.setAmount(Double.parseDouble((String)amount_aor.get(i)));
						System.out.println(reqObj.toString());
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
							System.out.println(costof.toString());
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
						 
						  /*int cogsCnt=itemCogsDao.addItemCogs(costofGoods.entryProductDetails((String)qty.get(i), (String)item.get(i), plant, avg_rate, dueDate),plant);
						  System.out.println("Insert ItemCogs Status :"+ cogsCnt);*/
								
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
						double lacostloop = 0.0;
						if (!Double.isNaN(Double.valueOf((String) landedCost.get(i)))) {
							lacostloop = Double.valueOf((String) landedCost.get(i));
						}
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						String convlandedCost = String.valueOf(( lacostloop / Float.parseFloat(currencyuseqt)));
						billDetInfo = new Hashtable<String, String>();
						billDetInfo.put("PLANT", plant);
						billDetInfo.put("LNNO", Integer.toString(lnno));
						billDetInfo.put("BILLHDRID", billHdrId);
						billDetInfo.put("ITEM", (String) item.get(i));
						billDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
						billDetInfo.put("QTY", (String) qty.get(i));
						billDetInfo.put("COST", convCost);
						billDetInfo.put("DISCOUNT",convDiscount);
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
						billDetInfo.put("UPBY",username);
						billDetInfoList.add(billDetInfo);
					}
					isUpdated = billDAO.deleteBillDet(plant, billHdrId);
					if(isUpdated)
					isUpdated = billUtil.addMultipleBillDet(billDetInfoList, plant);

					
				
				int attchSize = billAttachmentList.size();
				for(int i =0 ; i < attchSize ; i++){
					billAttachment = new Hashtable<String, String>();
					billAttachment = billAttachmentList.get(i);
					billAttachment.put("BILLHDRID", billHdrId);
					billAttachmentInfoList.add(billAttachment);
				}
				if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
					BillDAO billDao=new BillDAO();
					for(int i=0;i<landedCostLst.size();i++) {
							int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
							System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
					}
				}
				if(isUpdated) {
					if(billAttachmentInfoList.size() > 0)
						isUpdated = billUtil.addBillAttachments(billAttachmentInfoList, plant);

					
					curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					System.out.println("billStatus"+billStatus);
					if(!billStatus.equalsIgnoreCase("CANCELLED")) {
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
							//	TODO : Ravindra - Get verified
							if (netWeight == null || "".equals(netWeight)) {
								netWeight = "0";
							}
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
							
							if(!orddiscounttype.toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) - Double.parseDouble(orderdiscount.toString())/billDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(billDet.get("Amount").toString())/100)*Double.parseDouble(orderdiscount.toString());
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) -jodamt);
							}
							
							//journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString())-Double.parseDouble(orderdiscount)/billDetInfoList.size());
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
									jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
								}

							
							}
						}
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							
						}
						else
						{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
						//	journalDetail_1.setACCOUNT_NAME((String) vendno);
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
							journalDetail_2.setDESCRIPTION(jdesc+"-"+billNo);
							
							//JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							//journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							//journalDetail_2.setACCOUNT_NAME("VAT Input");

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
						/*if(!orderdiscount.isEmpty())
						{
							orderDiscountFrom=Double.parseDouble(orderdiscount);
							orderDiscountFrom=(Double.parseDouble(subTotal)*orderDiscountFrom)/100;
						}*/
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
									//Double calculatedShippingCost=(shippingCostFrom*Netweight)/totalItemNetWeight;
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
										for(JournalDetail journal:journalDetails) {
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
							}else {
								JournalDAO journalDAO = new JournalDAO();
								Integer trid = journalDAO.getJournalTridandTrtype(plant, billNo, "BILL_REVERSAL");
								if(trid != 0) {
									journalDAO.DeleteJournal(plant, String.valueOf(trid));
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.DELETE_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,"BILL_REVERSAL"+" "+billNo);
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
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
								/*if(journalReversalList.size()>0)
								{*/
									List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
									if(shipRef != "") {
									ArrayList  movQryList = new MasterUtil().getExpenseDetailusingponoanddnol(plant, pono, shipRef);
									if (movQryList.size() > 0) {
										ht = new Hashtable();
										ht.put("PLANT",plant);
										ht.put("SHIPMENT_CODE",shipRef);
										ht.put("PONO",pono);
										List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
//										double expenseTaxAmount = 0.0;
										for(int j=0;j < expenseHdrList.size();j++) {
//											Journal journal_2=new Journal();
											Map expenseHdr = (Map) expenseHdrList.get(j);

											
											JournalDetail inventorySelected=new JournalDetail();
											inventorySelected.setPLANT(plant);
											JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
											inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
											inventorySelected.setACCOUNT_NAME("Inventory Asset");
											inventorySelected.setDEBITS(Double.parseDouble((String) expenseHdr.get("SUB_TOTAL")));
											expensepo_journaldetails.add(inventorySelected);											
											
											for(int i =0; i<movQryList.size(); i++) {
												Map arrCustLine = (Map)movQryList.get(i);
												if(Double.parseDouble((String)expenseHdr.get("ID")) == Double.parseDouble((String)arrCustLine.get("ID"))) {
													JournalDetail expensesSelected=new JournalDetail();
													expensesSelected.setPLANT(plant);
													JSONObject coaJson=coaDAO.getCOAByName(plant, (String)arrCustLine.get("EXPENSES_ACCOUNT"));
													expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
													expensesSelected.setACCOUNT_NAME((String)arrCustLine.get("EXPENSES_ACCOUNT"));
													expensesSelected.setCREDITS(Double.parseDouble((String)arrCustLine.get("AMOUNT").toString()));
													journalReversalList.add(expensesSelected);
//													expenseTaxAmount = Double.parseDouble((String)arrCustLine.get("TAXAMOUNT").toString());
												}
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
										}

										
									}
									}
									
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
									if(journalReversalList.size()>0){
									JournalHeader journalReversalHead=journalHead;
									Double totalDebitReversal=0.00;
									for(JournalDetail journDet:journalReversalList)
									{
										totalDebitReversal=totalDebitReversal+journDet.getCREDITS();
										

									}
									boolean isDebitExists = false;
									for(JournalDetail journDet:journalReversalList)
									{										
										if(journDet.getACCOUNT_NAME().equalsIgnoreCase("Inventory Asset")) {
											journDet.setDEBITS(totalDebitReversal);
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
										journalDetail.setDEBITS(totalDebitReversal);
										journalDetail.setCREDITS(discountFrom);
										journalReversalList.add(journalDetail);
									}

									
									journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
									journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
									Journal journalReversal=journalService.getJournalByTransactionId(plant, journalReversalHead.getTRANSACTION_ID(),journalReversalHead.getTRANSACTION_TYPE());
									if(journalReversal.getJournalHeader().getID()>0)
									{
										journalHead.setID(journalReversal.getJournalHeader().getID());
										Journal journal_1=new Journal();
										journal_1.setJournalHeader(journalReversalHead);
										journal_1.setJournalDetails(journalReversalList);
										journalService.updateJournal(journal_1, username);
										Hashtable jhtMovHis1 = new Hashtable();
										jhtMovHis1.put(IDBConstants.PLANT, plant);
										jhtMovHis1.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
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
									else
									{
										Journal journal_1=new Journal();
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

						
					}

					
				}
				if(isUpdated) {
					for(int i =0 ; i < item.size() ; i++){
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);					
					htMovHis.put("DIRTYPE", TransactionConstants.EDIT_BILL);
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));														
					htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
					String billqty = String.valueOf((String) qty.get(i));
					htMovHis.put(IDBConstants.QTY, billqty);
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, billNo);
					htMovHis.put(IDBConstants.CREATED_BY, username);		
					htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMovHis.put("REMARKS",pono+","+grno+","+refNum);
					Hashtable htMovChk = new Hashtable();
					htMovChk.clear();
					htMovChk.put(IDBConstants.PLANT, plant);
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_BILL);
					htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
					htMovChk.put(IDBConstants.QTY, billqty);
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, billNo);
					boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%BILL%' ");
					if(!isAdded)
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
				
				/* Added by Azees for inventory*/

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
						invmap.put("BILL", grno);
						invmap.put("BILLNO", billNo);
						invmap.put(IConstants.PODET_PONUM, "");
						invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
						invmap.put(IConstants.VENDOR_CODE, vendno);
						invmap.put(IConstants.LOC, LOC);
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, vendno);
						invmap.put(IConstants.BATCH, BATCH);
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put(IConstants.RECV_QTY, QTY);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						if (billDate.length()>5)
							strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put("RETURN_DATE", strTranDate);
						invmap.put("UOMQTY", uomQty);
						invmap.put("NOTES", "");
						invmap.put("PORETURN", "");
						invmap.put(IConstants.GRNO, "");
						invmap.put(IConstants.CUSTOMER_NAME, vendno);
						invmap.put(IConstants.UOM, (String) uom.get(i));
						isUpdated = processPodetForReversal(invmap);
						if(isUpdated) {
							processInvRemove(invmap);
						}
						if(isUpdated) {
							processMovHis_OUT(invmap);
						}
						if (isUpdated == true) {//Shopify Inventory Update
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
				if(isUpdated && billStatus.equalsIgnoreCase("Open") && deductInv.equalsIgnoreCase("1")) {
					for(int i =0 ; i < item.size() ; i++){
						int lnno = i+1;
						Hashtable htCondiShipHis = new Hashtable();
						htCondiShipHis.put("PLANT", plant);
						htCondiShipHis.put("GRNO", grno);
						htCondiShipHis.put("STATUS","C");
						htCondiShipHis.put("BATCH",(String) batch.get(i));
						htCondiShipHis.put("LNNO", Integer.toString(lnno));
						htCondiShipHis.put("LOC", (String) loc.get(i));
						htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
						boolean isexists = new RecvDetDAO().isExisit(htCondiShipHis, plant);
						double costex = Double.valueOf((String) cost.get(i))/Double.valueOf(currencyuseqt);
						String scostex = String.valueOf(costex);
						if(isexists){
					        String queryup = "set UNITCOST=" + scostex+ ",ORDQTY=" + (String) qty.get(i);
					        isUpdated = new RecvDetDAO().update(queryup,htCondiShipHis,"",plant);
			            }
						

						/**/
						String QTY = "";//, ORDQTY = "";
						boolean processInv = false;
						if(ordQty.size() == 0) {
							QTY = (String) qty.get(i);
//							ORDQTY = (String) qty.get(i);
							processInv = true;
						}else {
							if(ordQty.size()> i) {
//								ORDQTY = (String) qty.get(i);
								double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
								double i_Qty = Double.parseDouble((String) qty.get(i));
								i_Qty = i_Qty - i_OrdQty;							
								QTY = Double.toString(i_Qty);
								if(i_Qty > 0) {
									processInv = true;
								}
							}else {
								QTY = (String) qty.get(i);
//								ORDQTY = (String) qty.get(i);
								processInv = true;
							}
						}
						/**/

					
					if(processInv) { 
//					String ITEM_QTY="";
					Map invmap = new HashMap();
						String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));
						
						String uomQty="";
						List uomQry = new UomDAO().getUomDetails((String) uom.get(i), plant, "");
						if(uomQry.size()>0) {
							Map m = (Map) uomQry.get(0);
							uomQty = (String)m.get("QPUOM");
						}else {
							uomQty = "0";
						}
						
						String strTranDate ="";
						invmap = new HashMap();
						invmap.put(IConstants.PLANT, plant);
						invmap.put(IConstants.ITEM, (String) item.get(i));
						invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, (String) item.get(i)));
						invmap.put("BILL", grno);
						invmap.put("BILLNO", billNo);
						invmap.put(IConstants.PODET_PONUM, "");
						invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
						invmap.put(IConstants.VENDOR_CODE, vendno);
						invmap.put(IConstants.LOC, (String) loc.get(i));
						invmap.put(IConstants.LOGIN_USER, username);
						invmap.put(IConstants.CUSTOMER_CODE, vendno);
						invmap.put(IDBConstants.USERFLD4, (String) batch.get(i));
						invmap.put(IConstants.BATCH, (String) batch.get(i));
						invmap.put(IConstants.QTY, QTY);
						invmap.put(IConstants.ORD_QTY, QTY);
						invmap.put(IConstants.RECV_QTY, QTY);
						invmap.put("UOMQTY", uomQty);
						invmap.put(IConstants.REMARKS, "");
						invmap.put(IConstants.RSNDESC, "");
						invmap.put(IDBConstants.CURRENCYID, currencyid);
						invmap.put("UNITCOST", scostex);
						invmap.put(IDBConstants.CREATED_BY, username);
						if (billDate.length()>5)
							strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
						invmap.put(IConstants.TRAN_DATE, strTranDate);
						invmap.put(IConstants.RECVDATE, billDate);
						invmap.put(IConstants.EXPIREDATE, "");
						invmap.put(IConstants.NONSTKFLAG, ISNONSTKFLG);
						invmap.put(IConstants.UOM, (String) uom.get(i));	
						invmap.put("STATUS", "C");
						
						if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {							
								isUpdated = processInvMst(invmap);														
						}
					if(isUpdated) {
						isUpdated = processPodetForReceive(invmap);
					}
						if(isUpdated) {
							processMovHis_IN(invmap);
						}
						if (isUpdated == true) {//Shopify Inventory Update
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
						if(!isUpdated) {
							break process;
						}
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
				/* Added by Abhilash to handle COGS */

				
				if(isUpdated) {
					DbBean.CommitTran(ut);
					result = "Bill updated successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Bill not created";
				}
				if(result.equalsIgnoreCase("Bill not created")){
					if(ajax){
						JSONObject resultJson = new JSONObject();
						resultJson.put("MESSAGE", result);
						resultJson.put("ERROR_CODE", "99");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else{
						response.sendRedirect("../bill/edit?action=View&BILL_HDR=" + billHdrId + "&result=" + result);
					}
				}else{
					if (ajax){
						try {
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.BILL);
							if ("bill".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
								billPDFGenration(request, response, billNo, Integer.parseInt(billHdrId));
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
							resultJson.put("MESSAGE", "Bill Updated Successfully and email not sent");
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}
					}else{
						response.sendRedirect("../bill/summary?result=" + result);/* Redirect to Bill Summary page */
					}
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (ajax) {
					JSONObject resultJson = new JSONObject();
					resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				} else {
					response.sendRedirect(
							"../bill/edit?action=View&BILL_HDR=" + billHdrId + "&result=" + ThrowableUtil.getMessage(e));
				}
			}
		}
		if(action.equalsIgnoreCase("convertToOpen")) {
			String billHdrId = StrUtils.fString(request.getParameter("billHdrId"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "",jdesc="";
			boolean isUpdated = false;
			BillDAO billDAO = new BillDAO();
			try {
				String query1 = " SET BILL_STATUS = 'Open' ";
				Hashtable ht = new Hashtable();
				ht.put("ID", billHdrId);
				ht.put("PLANT", plant);			
				isUpdated = billDAO.updateBillHdr(query1, ht, "");
				if(isUpdated) {
					result = "Bill opened successfully.";
					 List billHdrList =  billDAO.getBillHdrById(ht);
					    Map billHdr=(Map)billHdrList.get(0);
//					    DateUtils dateutils = new DateUtils();
					    String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(billHdr.get("BILL_DATE").toString());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("BILL");
						journalHead.setTRANSACTION_ID(billHdr.get("BILL").toString());
						journalHead.setSUB_TOTAL(Double.parseDouble(billHdr.get("SUB_TOTAL").toString()));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(billHdr.get("TOTAL_AMOUNT").toString()));
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						List<JournalDetail> journalReversalList=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						VendMstDAO vendorDAO=new VendMstDAO();
						plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						Hashtable ht1 = new Hashtable();
						ht1.put("BILLHDRID", billHdrId);
						ht1.put("PLANT", plant);	
						List billDetInfoList=billDAO.getBillDetByHdrId(ht1);
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						Double totalline=0.00;
						for(int i =0; i<billDetInfoList.size(); i++) {   
				    		Map billDet=(Map)billDetInfoList.get(i);
				    		Double quantity=Double.parseDouble(billDet.get("QTY").toString());
				    		totalline++;
				    		String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
				    		Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
							if(!billHdr.get("ORDERDISCOUNTTYPE").toString().toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) - Double.parseDouble(billHdr.get("ORDER_DISCOUNT").toString())/billDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(billDet.get("Amount").toString())/100)*Double.parseDouble(billHdr.get("ORDER_DISCOUNT").toString());
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) -jodamt);
							}
							//journalDetail.setDEBITS(Double.parseDouble(billDet.get("AMOUNT").toString()));
							boolean isLoop=false;
							if(journalDetails.size()>0)
							{
								int index=0;
								for(JournalDetail journal:journalDetails) {
									int accountId=journal.getACCOUNT_ID();
									if(accountId==journalDetail.getACCOUNT_ID()) {
										isLoop=true;
										Double sumDetit=journal.getDEBITS()+journalDetail.getDEBITS();
										journalDetail.setDEBITS(sumDetit);
										journalDetails.set(index, journalDetail);
										break;
									}
									index++;
									
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
							jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
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
							//journalDetail_1.setACCOUNT_NAME(billHdr.get("VENDNO").toString());
							journalDetail_1.setCREDITS(Double.parseDouble(billHdr.get("TOTAL_AMOUNT").toString()));
							journalDetails.add(journalDetail_1);
						}
						
						
						Double taxAmountFrom=Double.parseDouble(billHdr.get("TAXAMOUNT").toString());
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							journalDetail_2.setDESCRIPTION(jdesc+"-"+billHdr.get("BILL").toString());
							
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
							
							
							//JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							
							journalDetail_2.setDEBITS(taxAmountFrom);
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
									Double subTotalAfterOrderDiscount=Double.parseDouble(billHdr.get("SUB_TOTAL").toString())-orderDiscountFrom;
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
						String shippingCost=billHdr.get("SHIPPINGCOST").toString();
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
								
								for(int i =0; i<billDetInfoList.size(); i++) {   
						    		Map billDet=(Map)billDetInfoList.get(i);
						    		Double quantity=Double.parseDouble(billDet.get("QTY").toString());
									String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
									Double Netweight=quantity*Double.parseDouble(netWeight);
									//Double calculatedShippingCost=(shippingCostFrom*Netweight)/totalItemNetWeight;
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
									//journalDetail_5.setDEBITS(calculatedShippingCost);
									journalDetail_5.setDEBITS(shippingCostFrom);
									boolean isLoop=false;
									if(journalReversalList.size()>0)
									{
										int j=0;
										for(JournalDetail journal:journalDetails) {
											int accountId=journal.getACCOUNT_ID();
											if(accountId==journalDetail_5.getACCOUNT_ID()) {
												isLoop=true;
												Double sumDetit=journal.getDEBITS()+journalDetail_5.getDEBITS();
												journalDetail_5.setDEBITS(sumDetit);
												journalDetails.set(j, journalDetail_5);
												break;
											}
											j++;
											
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
								journal.setJournalHeader(journalHead);
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
								if(journalReversalList.size()>0)
								{
									JournalHeader journalReversalHead=journalHead;
									Double totalDebitReversal=0.00;
									for(JournalDetail journDet:journalReversalList)
									{
										totalDebitReversal=totalDebitReversal+journDet.getDEBITS();
										
									}
									journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
									journalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
									Journal journalReversal=journalService.getJournalByTransactionId(plant, journalReversalHead.getTRANSACTION_ID(),journalReversalHead.getTRANSACTION_TYPE());
									if(journalReversal.getJournalHeader().getID()>0)
									{
										journalHead.setID(journalReversal.getJournalHeader().getID());
										Journal journal_1=new Journal();
										journal_1.setJournalHeader(journalReversalHead);
										journal_1.setJournalDetails(journalReversalList);
										journalService.updateJournal(journal_1, username);
										Hashtable jhtMovHis1 = new Hashtable();
										jhtMovHis1.put(IDBConstants.PLANT, plant);
										jhtMovHis1.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
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
									else
									{
										Journal journal_1=new Journal();
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
							else
							{
								journalService.addJournal(journal, username);
								MovHisDAO movHisDao = new MovHisDAO();
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
						}else {
							journalService.addJournal(journal, username);
							MovHisDAO movHisDao = new MovHisDAO();
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
						
						boolean isAdded = true;
						/*if(billHdr.get("BILL_TYPE").toString().equalsIgnoreCase("INVENTORY")) {
							for(int i =0; i<billDetInfoList.size(); i++) {   
					    		Map billDet=(Map)billDetInfoList.get(i);
								int lnno = Integer.parseInt((String)billDet.get("LNNO"));
								String ITEM = "",UOM = "", QTY = "", LOC = "", BATCH = "";
								ITEM = (String)billDet.get("ITEM");
								UOM = (String)billDet.get("UOM");
								QTY = (String)billDet.get("QTY");
								LOC = (String)billDet.get("LOC");
								BATCH = (String)billDet.get("BATCH");
								
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
								invmap.put("BILL", billHdr.get("GRNO").toString());
								invmap.put(IConstants.PODET_PONUM, "");
								invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
								invmap.put(IConstants.VENDOR_CODE, billHdr.get("VENDNO").toString());
								invmap.put(IConstants.LOC, LOC);
								invmap.put(IConstants.LOGIN_USER, username);
								invmap.put(IConstants.CUSTOMER_CODE, billHdr.get("VENDNO").toString());
								invmap.put(IConstants.BATCH, BATCH);
								invmap.put(IConstants.QTY, QTY);
								invmap.put(IConstants.ORD_QTY, QTY);
								invmap.put(IConstants.RECV_QTY, QTY);
								invmap.put(IConstants.REMARKS, "");
								invmap.put(IConstants.RSNDESC, "");
								if (billHdr.get("BILL_DATE").toString().length()>5)
									strTranDate    = billHdr.get("BILL_DATE").toString().substring(6)+"-"+ billHdr.get("BILL_DATE").toString().substring(3,5)+"-"+billHdr.get("BILL_DATE").toString().substring(0,2);
								invmap.put(IConstants.TRAN_DATE, strTranDate);
								invmap.put("RETURN_DATE", strTranDate);
								invmap.put("UOMQTY", uomQty);
								invmap.put("NOTES", "");
								invmap.put("PORETURN", "");
								invmap.put(IConstants.GRNO, "");
								invmap.put(IConstants.CUSTOMER_NAME,  billHdr.get("VENDNO").toString());
								isAdded = processPodetForReversal(invmap);
								if(isAdded) {
									processInvRemove(invmap);
								}
								if(isAdded) {
									processMovHis_OUT(invmap);
								}					
							}
						}*/
						process: 
						if(isAdded && billHdr.get("BILL_TYPE").toString().equalsIgnoreCase("INVENTORY")) {
							for(int i =0; i<billDetInfoList.size(); i++) {   
					    		Map billDet=(Map)billDetInfoList.get(i);
					    		int lnno = Integer.parseInt((String)billDet.get("LNNO"));
								Hashtable htCondiShipHis = new Hashtable();
								htCondiShipHis.put("PLANT", plant);
								htCondiShipHis.put("GRNO", billHdr.get("GRNO").toString());
								htCondiShipHis.put("STATUS","C");
								htCondiShipHis.put("BATCH",(String)billDet.get("BATCH"));
								htCondiShipHis.put("LNNO", String.valueOf(lnno));
								htCondiShipHis.put("LOC", (String)billDet.get("LOC"));
								htCondiShipHis.put(IConstants.ITEM, (String)billDet.get("ITEM"));
								boolean isexists = new RecvDetDAO().isExisit(htCondiShipHis, plant);
								double costex = Double.valueOf((String)billDet.get("COST"))/Double.valueOf((String)billDet.get("CURRENCYUSEQT"));
								String scostex = String.valueOf(costex);
								if(isexists){
							        String query = "set UNITCOST=" + scostex + ",ORDQTY=" + (String)billDet.get("QTY");
							        isAdded = new RecvDetDAO().update(query,htCondiShipHis,"",plant);
					            }
								
								/**/
								String QTY = "";//, ORDQTY = "";
								boolean processInv = true;
								QTY = (String)billDet.get("QTY");
//								ORDQTY = (String)billDet.get("QTY");
								/*boolean processInv = false;
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
								}*/
								/**/
							
							if(processInv) { 
//							String ITEM_QTY="";
							Map invmap = new HashMap();
								String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,(String)billDet.get("ITEM"));
								
								String uomQty="";
								List uomQry = new UomDAO().getUomDetails((String)billDet.get("UOM"), plant, "");
								if(uomQry.size()>0) {
									Map m = (Map) uomQry.get(0);
									uomQty = (String)m.get("QPUOM");
								}else {
									uomQty = "0";
								}
								
								String strTranDate ="";
								invmap = new HashMap();
								invmap.put(IConstants.PLANT, plant);
								invmap.put(IConstants.ITEM, (String)billDet.get("ITEM"));
								invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, (String)billDet.get("ITEM")));
								invmap.put("BILL", billHdr.get("GRNO").toString());
								invmap.put(IConstants.PODET_PONUM, "");
								invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
								invmap.put(IConstants.VENDOR_CODE,  billHdr.get("VENDNO").toString());
								invmap.put(IConstants.LOC, (String)billDet.get("LOC"));
								invmap.put(IConstants.LOGIN_USER, username);
								invmap.put(IConstants.CUSTOMER_CODE,  billHdr.get("VENDNO").toString());
								invmap.put(IDBConstants.USERFLD4, (String)billDet.get("BATCH"));
								invmap.put(IConstants.BATCH, (String)billDet.get("BATCH"));
								invmap.put(IConstants.QTY, QTY);
								invmap.put(IConstants.ORD_QTY, QTY);
								invmap.put(IConstants.RECV_QTY, QTY);
								invmap.put("UOMQTY", uomQty);
								invmap.put(IConstants.REMARKS, "");
								invmap.put(IConstants.RSNDESC, "");
								invmap.put(IDBConstants.CURRENCYID, billHdr.get("CURRENCYID").toString());
								invmap.put("UNITCOST",scostex);
								invmap.put(IDBConstants.CREATED_BY, username);
								if (billHdr.get("BILL_DATE").toString().length()>5)
									strTranDate    = billHdr.get("BILL_DATE").toString().substring(6)+"-"+ billHdr.get("BILL_DATE").toString().substring(3,5)+"-"+billHdr.get("BILL_DATE").toString().substring(0,2);
								invmap.put(IConstants.TRAN_DATE, strTranDate);
								invmap.put(IConstants.RECVDATE, billHdr.get("BILL_DATE").toString());
								invmap.put(IConstants.EXPIREDATE, "");
								invmap.put(IConstants.NONSTKFLAG, ISNONSTKFLG);						
								
								if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {							
										isAdded = processInvMst(invmap);														
								}
								if(isAdded) {
									isAdded = processPodetForReceive(invmap);
								}
								if(isAdded) {
									processMovHis_IN(invmap);
								}
								if(!isAdded) {
									break process;
								}
							}
						}
					}

						
						
						
						response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&rsuccess="+ result);
				}else {
					result = "Couldn't convert to Open.";
					response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
				}
				//response.sendRedirect("jsp/billSummary.jsp?result="+ result);/* Redirect to Bill Summary page */
				

			} catch (Exception e) {
				e.printStackTrace();
				result = "Couldn't convert to Open.";
				response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
			}

			
		}
		if(action.equalsIgnoreCase("convertToCancel")) {
			String billHdrId = StrUtils.fString(request.getParameter("billHdrId"));
			String bill = StrUtils.fString(request.getParameter("bill"));
			String billstatus = StrUtils.fString(request.getParameter("status"));
			String taxstatus = StrUtils.fString(request.getParameter("taxstatus"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
			MovHisDAO movHisDao = new MovHisDAO();
//			DateUtils dateutils = new DateUtils();
			BillDAO billDAO = new BillDAO();
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if(billstatus.equalsIgnoreCase("open") || billstatus.equalsIgnoreCase("draft") ) {
					if(billstatus.equalsIgnoreCase("CANCELLED")){
						result = "Bill already cancelled";
						response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
					}else if(taxstatus.equalsIgnoreCase("Tax Generated")){
						result = "Couldn't cancel the bill.";
						response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
					}else {
					

						String query = " SET BILL_STATUS = 'CANCELLED' ";
						Hashtable ht = new Hashtable();
						ht.put("ID", billHdrId);
						ht.put("PLANT", plant);			
						isUpdated = billDAO.updateBillHdr(query, ht, "");
						if(isUpdated) {
							JournalService journalService=new JournalEntry();
							Journal journalFrom=journalService.getJournalByTransactionId(plant, bill,"BILL");
							if(journalFrom.getJournalHeader()!=null)
							{
								if(journalFrom.getJournalHeader().getID()>0)
								{
									journalFrom.getJournalHeader().setJOURNAL_STATUS("CANCELLED");
									journalService.updateJournal(journalFrom, username);
									Hashtable jhtMovHis1 = new Hashtable();
									jhtMovHis1.put(IDBConstants.PLANT, plant);
									jhtMovHis1.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
									jhtMovHis1.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis1.put(IDBConstants.ITEM, "");
									jhtMovHis1.put(IDBConstants.QTY, "0.0");
									jhtMovHis1.put("RECID", "");
									jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,journalFrom.getJournalHeader().getTRANSACTION_TYPE()+" "+journalFrom.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis1.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis1.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis1);
								}
							}

							
							Hashtable ht1 = new Hashtable();
							ht1.put("ID", billHdrId);
							ht1.put("PLANT", plant);

							
							List billhdr = billDAO.getBillHdrById(ht1);
							Map billHdrmap=(Map)billhdr.get(0);
							

							Hashtable ht2 = new Hashtable();
							ht2.put("BILLHDRID", billHdrId);
							ht2.put("PLANT", plant);

							
							List billdet = billDAO.getBillDetByHdrId(ht2);

							
							String origin = (String)billHdrmap.get("ORIGIN");
							String deductInv = (String)billHdrmap.get("DEDUCT_INV");	
							
								for(int i =0 ; i < billdet.size() ; i++){
									Map billdetmap=(Map)billdet.get(i);
								Hashtable htMovHis = new Hashtable();
								htMovHis.clear();
								htMovHis.put(IDBConstants.PLANT, plant);					
								htMovHis.put("DIRTYPE", TransactionConstants.CANCEL_BILL);
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, (String) billdetmap.get("ITEM"));
								String billqty = String.valueOf((String) billdetmap.get("QTY"));
								htMovHis.put(IDBConstants.QTY, billqty);
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM, bill);
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								//htMovHis.put("REMARKS",pono+","+grno+","+refNum);
								htMovHis.put("REMARKS",billHdrmap.get("PONO")+","+billHdrmap.get("GRNO")+","+billHdrmap.get("REFERENCE_NUMBER"));
								Hashtable htMovChk = new Hashtable();
								htMovChk.clear();
								htMovChk.put(IDBConstants.PLANT, plant);
								htMovChk.put("DIRTYPE", TransactionConstants.CANCEL_BILL);
								htMovChk.put(IDBConstants.ITEM, (String) billdetmap.get("ITEM"));
								htMovChk.put(IDBConstants.QTY, billqty);
								htMovChk.put(IDBConstants.MOVHIS_ORDNUM, bill);
								boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%BILL%' ");
								if(!isAdded)
									isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								
								/* Code to reverse the Inventory */								

								if(isAdded&&!billstatus.equalsIgnoreCase("draft")&&
									origin.equalsIgnoreCase("manual")&&deductInv.equalsIgnoreCase("1")) {
											int lnno = i+1;
											String ITEM = "",UOM = "", LOC = "", BATCH = "",//QTY = "", 
													billNo="", CustCode ="", billDate="";
											ITEM = (String) billdetmap.get("ITEM");
											UOM = (String) billdetmap.get("UOM");
//											QTY = billqty;
											LOC = (String) billdetmap.get("LOC");
											BATCH = (String) billdetmap.get("BATCH");
											billNo = (String)billHdrmap.get("GRNO");
											CustCode = (String)billHdrmap.get("VENDNO");
											billDate = (String)billHdrmap.get("BILL_DATE");
											
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
											invmap.put("BILL", billNo);
											invmap.put(IConstants.PODET_PONUM, "");
											invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
											invmap.put(IConstants.VENDOR_CODE, CustCode);
											invmap.put(IConstants.LOC, LOC);
											invmap.put(IConstants.LOGIN_USER, username);
											invmap.put(IConstants.CUSTOMER_CODE, CustCode);
											invmap.put(IConstants.BATCH, BATCH);
											invmap.put(IConstants.QTY, billqty);
											invmap.put(IConstants.ORD_QTY, billqty);
											invmap.put(IConstants.RECV_QTY, billqty);
											invmap.put(IConstants.REMARKS, "");
											invmap.put(IConstants.RSNDESC, "");
											if (billDate.length()>5)
												strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
											invmap.put(IConstants.TRAN_DATE, strTranDate);
											invmap.put("RETURN_DATE", strTranDate);
											invmap.put("UOMQTY", uomQty);
											invmap.put("NOTES", "");
											invmap.put("PORETURN", "");
											invmap.put(IConstants.GRNO, "");
											invmap.put(IConstants.CUSTOMER_NAME, CustCode);
											isAdded = processPodetForReversal(invmap);
											if(isAdded) {
												processInvRemove(invmap);
											}
											if(isAdded) {
												processMovHis_OUT(invmap);
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

								
								}
							

							DbBean.CommitTran(ut);
							result = "Bill cancelled successfully.";
							response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&rsuccess="+ result);
						}else {
							DbBean.RollbackTran(ut);
							result = "Couldn't cancel the bill.";
							response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
						}

						
					}
				}else {
					DbBean.RollbackTran(ut);
					result = "Couldn't cancel the bill.";
					response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				result = "Couldn't cancel the bill.";
				response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
			}

			
		}

		
		if(action.equalsIgnoreCase("deletebill")) {
			String billHdrId = StrUtils.fString(request.getParameter("billHdrId"));
			String bill = StrUtils.fString(request.getParameter("bill"));
			String billstatus = StrUtils.fString(request.getParameter("status"));
			String taxstatus = StrUtils.fString(request.getParameter("taxstatus"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			UserTransaction ut = null;
			boolean isUpdated = false;
//			boolean isAddedgrn = false;
			BillDAO billDAO = new BillDAO();
			MovHisDAO movHisDao = new MovHisDAO();
//			DateUtils dateutils = new DateUtils();
			RecvDetDAO recvDetDAO = new RecvDetDAO();
			try {
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				if(billstatus.equalsIgnoreCase("open") || billstatus.equalsIgnoreCase("draft") || billstatus.equalsIgnoreCase("CANCELLED")) {
					
					if(taxstatus.equalsIgnoreCase("Tax Generated")){
						result = "Couldn't delete the bill.";
						response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
					}else {
							Hashtable ht1 = new Hashtable();
							ht1.put("ID", billHdrId);
							ht1.put("PLANT", plant);
							
							List billhdr = billDAO.getBillHdrById(ht1);
							Map billHdrmap=(Map)billhdr.get(0);
							
							Hashtable ht2 = new Hashtable();
							ht2.put("BILLHDRID", billHdrId);
							ht2.put("PLANT", plant);
							
							List billdet = billDAO.getBillDetByHdrId(ht2);
							
								isUpdated = billDAO.deleteBill(plant, billHdrId);
								if(isUpdated) {
									JournalService journalService=new JournalEntry();
									Journal journalFrom=journalService.getJournalByTransactionId(plant, bill,"BILL");
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
										String pono = (String) billHdrmap.get("PONO");
										String shipRef = (String) billHdrmap.get("SHIPMENT_CODE");
										
										Hashtable ht = new Hashtable();
										ht.put("PLANT",plant);
										ht.put("SHIPMENT_CODE",shipRef);
										ht.put("PONO",pono);
										List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
										for(int i=0; i<expenseHdrList.size();i++) {
											Map expenseHdr = (Map) expenseHdrList.get(i);											
											journalFrom=journalService.getJournalByTransactionId(plant, (String)expenseHdr.get("ID"),"EXPENSE FOR PO("+pono+")");
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
									}
									
									if(isUpdated) {
										String pono = (String) billHdrmap.get("PONO");
										String shipRef = (String) billHdrmap.get("SHIPMENT_CODE");
										
										Hashtable ht = new Hashtable();
										ht.put("PLANT",plant);
										ht.put("SHIPMENT_CODE",shipRef);
										ht.put("PONO",pono);
										List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
										for(int i=0; i<expenseHdrList.size();i++) {
//											Map expenseHdr = (Map) expenseHdrList.get(i);											
											journalFrom=journalService.getJournalByTransactionId(plant, bill,"BILL_REVERSAL");
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
									}
									
									
									if(isUpdated) {
										//if(!billHdrmap.get("GRNO").equals("")) {
										if(billHdrmap.get("BILL_TYPE").toString().equalsIgnoreCase("PURCHASE")) {
											
											
											
												String recvquery = "SET BILL_STATUS = NULL ";
												Hashtable ht3 = new Hashtable();
												ht3.put("PLANT", plant);
												ht3.put("PONO", billHdrmap.get("PONO"));
												ht3.put("GRNO", billHdrmap.get("GRNO"));
												if(recvDetDAO.isExisit(ht3, plant)) {
													recvDetDAO.update(recvquery, ht3, "", plant);	
												}
												
//												isAddedgrn = billDAO.updateGrntoBill("SET STATUS='NOT BILLED' ", ht3, "", plant);
										}
									}
									
									if(isUpdated) {
										
										String origin = (String)billHdrmap.get("ORIGIN");
										String deductInv = (String)billHdrmap.get("DEDUCT_INV");
										
										for(int i =0 ; i < billdet.size() ; i++){
											Map billdetmap=(Map)billdet.get(i);
										Hashtable htMovHis = new Hashtable();
										htMovHis.clear();
										htMovHis.put(IDBConstants.PLANT, plant);					
										htMovHis.put("DIRTYPE", TransactionConstants.DELETE_BILL);
										htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, (String) billdetmap.get("ITEM"));
										String billqty = String.valueOf((String) billdetmap.get("QTY"));
										htMovHis.put(IDBConstants.QTY, billqty);
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM, bill);
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										//htMovHis.put("REMARKS",pono+","+grno+","+refNum);
										htMovHis.put("REMARKS",billHdrmap.get("PONO")+","+billHdrmap.get("GRNO")+","+billHdrmap.get("REFERENCE_NUMBER"));
										Hashtable htMovChk = new Hashtable();
										htMovChk.clear();
										htMovChk.put(IDBConstants.PLANT, plant);
										htMovChk.put("DIRTYPE", TransactionConstants.DELETE_BILL);
										htMovChk.put(IDBConstants.ITEM, (String) billdetmap.get("ITEM"));
										htMovChk.put(IDBConstants.QTY, billqty);
										htMovChk.put(IDBConstants.MOVHIS_ORDNUM, bill);
										boolean isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%BILL%' ");
										if(!isAdded)
											isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
										
										/* Code to reverse the Inventory */								

										if(isAdded&&!billstatus.equalsIgnoreCase("draft")&&
											origin.equalsIgnoreCase("manual")&&deductInv.equalsIgnoreCase("1")) {
													int lnno = i+1;
													String ITEM = "",UOM = "", LOC = "", BATCH = "",//QTY = "", 
															billNo="", CustCode ="", billDate="";
													ITEM = (String) billdetmap.get("ITEM");
													UOM = (String) billdetmap.get("UOM");
//													QTY = billqty;
													LOC = (String) billdetmap.get("LOC");
													BATCH = (String) billdetmap.get("BATCH");
													billNo = (String)billHdrmap.get("GRNO");
													CustCode = (String)billHdrmap.get("VENDNO");
													billDate = (String)billHdrmap.get("BILL_DATE");
													
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
													invmap.put("BILL", billNo);
													invmap.put(IConstants.PODET_PONUM, "");
													invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
													invmap.put(IConstants.VENDOR_CODE, CustCode);
													invmap.put(IConstants.LOC, LOC);
													invmap.put(IConstants.LOGIN_USER, username);
													invmap.put(IConstants.CUSTOMER_CODE, CustCode);
													invmap.put(IConstants.BATCH, BATCH);
													invmap.put(IConstants.QTY, billqty);
													invmap.put(IConstants.ORD_QTY, billqty);
													invmap.put(IConstants.RECV_QTY, billqty);
													invmap.put(IConstants.REMARKS, "");
													invmap.put(IConstants.RSNDESC, "");
													if (billDate.length()>5)
														strTranDate    = billDate.substring(6)+"-"+ billDate.substring(3,5)+"-"+billDate.substring(0,2);
													invmap.put(IConstants.TRAN_DATE, strTranDate);
													invmap.put("RETURN_DATE", strTranDate);
													invmap.put("UOMQTY", uomQty);
													invmap.put("NOTES", "");
													invmap.put("PORETURN", "");
													invmap.put(IConstants.GRNO, "");
													invmap.put(IConstants.CUSTOMER_NAME, CustCode);
													if(!billstatus.equalsIgnoreCase("CANCELLED")) {
													isAdded = processPodetForReversal(invmap);
													if(isAdded) {
														processInvRemove(invmap);
													}
													if(isAdded) {
														processMovHis_OUT(invmap);
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
										/*	*/

										
										}
										
										DbBean.CommitTran(ut);
										result = "Bill Deleted successfully.";
										response.sendRedirect("../bill/summary?result="+ result);/* Redirect to Bill Summary page */
									}else {
										DbBean.RollbackTran(ut);
										result = "Couldn't Delete the bill.";
										response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
									}
								}else {
									DbBean.RollbackTran(ut);
									result = "Couldn't Delete the bill.";
									response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);

								}

						}
					}else {
						DbBean.RollbackTran(ut);
						result = "Couldn't Delete the bill.";
						response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
					}
			} catch (Exception e) {
				e.printStackTrace();
				result = "Couldn't Deleted the bill.";
				response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
			}

			
		}
		if(action.equalsIgnoreCase("convertToDraft")) {
			String billHdrId = StrUtils.fString(request.getParameter("billHdrId"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String result = "";
			boolean isUpdated = false;
			BillDAO billDAO = new BillDAO();
			try {
				String query = " SET BILL_STATUS = 'Draft' ";
				Hashtable ht = new Hashtable();
				ht.put("ID", billHdrId);
				ht.put("PLANT", plant);			
				isUpdated = billDAO.updateBillHdr(query, ht, "");
				if(isUpdated) {
					result = "Bill drafted successfully.";
				}else {
					result = "Couldn't convert to Draft.";
				}
				response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&rsuccess="+ result);
			} catch (Exception e) {
				e.printStackTrace();
				result = "Couldn't convert to Draft.";
				response.sendRedirect("../bill/detail?BILL_HDR="+billHdrId+"&resultnew="+ result);
			}

			
		}
		else if(baction.equalsIgnoreCase("downloadAttachmentById"))
		{
			System.out.println("Attachments by ID");
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			BillDAO billDAO=new BillDAO();
			List billAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				billAttachment = billDAO.getBillAttachByPrimId(ht1);
				Map billAttach=(Map)billAttachment.get(0);
				String filePath=(String) billAttach.get("FilePath");
				String fileType=(String) billAttach.get("FileType");
				String fileName=(String) billAttach.get("FileName");
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
			BillDAO billDAO=new BillDAO();
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				billDAO.deleteBillAttachByPrimId(ht1);

				
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}else if (baction.equals("VIEW_BILL_DETAILS")) {	    	  
	        jsonObjectResult = this.getbilldetailview(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	    }else if (baction.equals("ExportExcelBillDetails")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelBillDetails(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=bill_details.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}  else if (baction.equals("VIEW_PAYMENTS_MADE_SUMMARY")) {
	        jsonObjectResult = this.getpaymentsmadeview(request);	      
			response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();	       	       
	    }else if (baction.equals("ExportExcelPaymentsMade")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelpaymentsMade(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=vendor_payments.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (baction.equals("VIEW_VENDOR_BALANCES")) {	    	  
	        jsonObjectResult = this.getVendorBalances(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
	    }else if (baction.equals("ExportExcelVendorBalances")) {
//			String newHtml = "";
			HSSFWorkbook wb = new HSSFWorkbook();
//			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			wb = this.writeToExcelVendorBalances(request, response, wb);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename=vendor_balances.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();
		}else if (action.equalsIgnoreCase("BillImportTemplate")) {
			try {
				billImportTemplate(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("importCountSheet")) {
			try {
				onImportCountSheet(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase("confirmCountSheet")) {
			try {
				onConfirmBillSheet(request, response);
			} catch (Exception e) {
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

	
	private JSONObject getbillview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       // HTReportUtil movHisUtil       = new HTReportUtil();
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
       
        String fdate="",tdate="",covunitCostValue="0";//taxby="",
         try {
        
           String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
		   String  BILL = StrUtils.fString(request.getParameter("BILL"));
		   String  GRNO = StrUtils.fString(request.getParameter("GRNO"));
		   String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
		   String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
		   String  ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
//		   String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
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
		   if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
		   if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
		   if(StrUtils.fString(GRNO).length() > 0)       ht.put("GRNO",GRNO);
		   if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
		   if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
		   if(StrUtils.fString(ORDERTYPE).length() > 0)       ht.put("ORDERTYPE",ORDERTYPE);
		   movQryList = billUtil.getBillSummaryView(ht,fdate,tdate,PLANT,CUSTOMER);	
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
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                   			String availablecredit = (String)lineArr.get("AVAILABLE_CREDIT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(balCostVal, numberOfDecimal);
                            double availablecreditValue ="".equals(availablecredit) ? 0.0d :  Double.parseDouble(availablecredit);        					
                            availablecredit = StrUtils.addZeroes(availablecreditValue, numberOfDecimal);
                            covunitCostValue= StrUtils.addZeroes((Double.parseDouble(unitCostValue)*Double.parseDouble((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("BILL_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("BILL")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("PONO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("ordertype",StrUtils.fString((String)lineArr.get("ORDERTYPE")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 resultJsonInt.put("grno",StrUtils.fString((String)lineArr.get("GRNO")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE_NUMBER")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 //if(cur.isEmpty())
                    	 //String cur= curency;
                    	 resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue,Integer.valueOf(numberOfDecimal)));
                    	 resultJsonInt.put("exchangerate",StrUtils.fString((String)lineArr.get("CURRENCYUSEQT")));
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
                    	 resultJsonInt.put("availablecredit",StrUtils.fString(availablecredit));
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
	private JSONObject getpurchaseview(HttpServletRequest request) {        
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       // HTReportUtil movHisUtil       = new HTReportUtil(); 
        POUtil itemUtil = new POUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           String  BILLSTATUS = StrUtils.fString(request.getParameter("BILLSTATUS"));
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
           
if(STATUS.equalsIgnoreCase("DRAFT"))
	STATUS="N";
else if(STATUS.equalsIgnoreCase("OPEN"))
	STATUS="O";
else if(STATUS.equalsIgnoreCase("CLOSED"))
	STATUS="C";

if(BILLSTATUS.equalsIgnoreCase("NOT BILLED"))
	BILLSTATUS="";
              
           String extCond="";
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
           if(StrUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
           if(ORDERNO.length()>0) extCond=" AND pono like '"+ORDERNO+"%' ";
		     if(CUSTOMER.length()>0) extCond=" AND custName = '"+CUSTOMER+"' ";
		     if(STATUS.length()>0) extCond=" AND STATUS = '"+STATUS+"' ";
		     if(BILLSTATUS.length()>0) extCond=" AND BILL_STATUS = '"+BILLSTATUS+"' ";
		    String dtCondStr=" and ISNULL(CollectionDate,'')<>'' AND CAST((SUBSTRING(CollectionDate, 7, 4) + '-' + SUBSTRING(CollectionDate, 4, 2) + '-' + SUBSTRING(CollectionDate, 1, 2)) AS date)";
		     
		     if (fdate.length() > 0) {
		    	 extCond = extCond + dtCondStr + "  >= '" 
	  						+ fdate
	  						+ "'  ";
	  				if (tdate.length() > 0) {
	  					extCond = extCond +dtCondStr+ " <= '" 
	  					+ tdate
	  					+ "'  ";
	  				}
	  			  } else {
	  				if (tdate.length() > 0) {
	  					extCond = extCond +dtCondStr+ " <= '" 
	  					+ tdate
	  					+ "'  ";
	  				}
	  		     	}   
		     extCond=extCond+" order by CustCode, PONO,CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date)";
		     movQryList = itemUtil.getPoHdrDetails("CollectionDate,PONO,JobNum,CustCode,CustName,STATUS,ISNULL(BILL_STATUS,'') as BILL_STATUS,CURRENCYID,DELDATE,cast(ISNULL((select SUM(UNITCOST) from "+PLANT+"_PODET p where p.PONO="+PLANT+"_POHDR.PONO ),0) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT",ht,extCond);	
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
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("podate",StrUtils.fString((String)lineArr.get("CollectionDate")));
                    	 resultJsonInt.put("pono",StrUtils.fString((String)lineArr.get("PONO")));
                    	 resultJsonInt.put("refnum",StrUtils.fString((String)lineArr.get("JobNum")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("CustCode")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("CustName")));
                    	 if(StrUtils.fString((String)lineArr.get("STATUS")).equals("N"))
                    	 resultJsonInt.put("status","DRAFT");
                    	 else if(StrUtils.fString((String)lineArr.get("STATUS")).equals("O"))
                        	 resultJsonInt.put("status","OPEN");
                    	 else if(StrUtils.fString((String)lineArr.get("STATUS")).equals("C"))
                        	 resultJsonInt.put("status","CLOSED");
                    	 resultJsonInt.put("billstatus",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("expdate",StrUtils.fString((String)lineArr.get("DELDATE")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 resultJsonInt.put("amount",cur+unitCostValue);
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
	

	private JSONObject getbillpaymentview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           String  SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  cur = StrUtils.fString(request.getParameter("CURENCY"));
           String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
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
           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
		   movQryList = billUtil.getBillPaymentSummaryView(ht,fdate,tdate,PLANT,SUPPLIER);	
		   if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;
            String covunitCostValue="0";//lastProduct="",
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                   			
                   			String usamCostValue = (String)lineArr.get("UNUSEDAMOUNT");
                   			float balCostValusm ="".equals(usamCostValue) ? 0.0f :  Float.parseFloat(usamCostValue);
                            if(balCostValusm==0f){
                            	usamCostValue="0.00";
                            }else{
                            	usamCostValue=usamCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            usamCostValue =StrUtils.addZeroes(Double.parseDouble(usamCostValue), decimal);
                   			
                   			
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue =StrUtils.addZeroes(Double.parseDouble(unitCostValue), decimal);
                            covunitCostValue= StrUtils.addZeroes((Float.parseFloat(unitCostValue)*Float.parseFloat((String)lineArr.get("CURRENCYUSEQT"))), numberOfDecimal);
                            
                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue =StrUtils.addZeroes(Double.parseDouble(balCostValue), decimal);
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("billpaymentdate",StrUtils.fString((String)lineArr.get("PAYMENT_DATE")));
                    	 resultJsonInt.put("paymentmode",StrUtils.fString((String)lineArr.get("PAYMENT_MODE")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("accountname",StrUtils.fString((String)lineArr.get("ACCOUNT_NAME")));
                    	 resultJsonInt.put("amount",Numbers.toMillionFormat(unitCostValue,numberOfDecimal));
                    	 resultJsonInt.put("unusedamount",Numbers.toMillionFormat(balCostValue,numberOfDecimal));
                    	 resultJsonInt.put("usmamount",Numbers.toMillionFormat(usamCostValue,numberOfDecimal));
                    	 cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 resultJsonInt.put("convamount",cur+Numbers.toMillionFormat(covunitCostValue,numberOfDecimal));
                    	 resultJsonInt.put("exchangerate",StrUtils.fString(Numbers.toMillionFormat((String)lineArr.get("CURRENCYUSEQT"),numberOfDecimal)));
                    	 
                    	
                    	 Hashtable htpdc = new Hashtable();
                    	 htpdc.put("PAYMENTID",(String)lineArr.get("ID"));
                    	 htpdc.put("PLANT", PLANT);
 						 BillPaymentDAO billDao = new BillPaymentDAO();
 						 List pdcDetailList = billDao.getpdcbipayid(htpdc);
 						 if(pdcDetailList.size() > 0) {
 							 String pdcstatus = "0";
	 						 for(int j =0; j < pdcDetailList.size(); j++) {
	 							Map pdcdet=(Map)pdcDetailList.get(j);
	 							String status = (String)pdcdet.get("STATUS");
	 							if(status.equalsIgnoreCase("NOT APPLICABLE")) {
	 								pdcstatus = "1";
	 							}
	 						 }
	 						 
	 						 if(pdcstatus.equalsIgnoreCase("1")) {
	 							resultJsonInt.put("pdcstatus","NON PDC");
	 						 }else {
	 							resultJsonInt.put("pdcstatus","PDC");
	 						 }
	 						 
 						 }else {
 							 resultJsonInt.put("pdcstatus","NON PDC");
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
                resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }

        return resultJson;        
        
}

	private JSONObject getgrntobillview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           String  SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  cur = StrUtils.fString(request.getParameter("CURENCY"));
		   String  GRNO = StrUtils.fString(request.getParameter("GRNO"));
		   String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           
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
		   if(StrUtils.fString(GRNO).length() > 0)       ht.put("GRNO",GRNO);
		   if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
		   movQryList = billUtil.getGRNOToBillSummaryView(ht,fdate,tdate,PLANT,SUPPLIER);	
		   if (movQryList.size() > 0) {
            int Index = 0;
//             int iIndex = 0,irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
                   			
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("date",StrUtils.fString((String)lineArr.get("CRAT")));
                    	 resultJsonInt.put("pono",StrUtils.fString((String)lineArr.get("PONO")));
                    	 resultJsonInt.put("grno",StrUtils.fString((String)lineArr.get("GRNO")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("qty",StrUtils.fString((String)lineArr.get("QTY")));
                    	 resultJsonInt.put("billedqty",StrUtils.fString((String)lineArr.get("BILLEDQTY")));
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
                resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
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
        
        String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="";
        String orderNo="",billNo="",vendName="",custName="";//Order="",ordRefNo="",custcode="",currencyid="";
        String PLANT= StrUtils.fString(request.getParameter("PLANT"));
        
        BillUtil billUtil = new BillUtil();
        AgeingUtil ageingUtil = new AgeingUtil();
        
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        ArrayList movQrycustList  = new ArrayList();
        
        try {
        FROM_DATE     = StrUtils.fString(request.getParameter("FDATE"));
        TO_DATE   = StrUtils.fString(request.getParameter("TDATE"));
        billNo   = StrUtils.fString(request.getParameter("NAME"));
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
        String curDate =DateUtils.getDate();
        
        if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
        
        if (FROM_DATE.length()>5)
        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
        
        if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
        if (TO_DATE.length()>5)
        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
        
        vendName = StrUtils.fString(request.getParameter("CNAME"));
        orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        
        movQrycustList = ageingUtil.getcustomerorsuppliername(PLANT,fdate,tdate,"BILL",vendName);        
//        boolean flag =  
        		ageingUtil.InsertTempOrderPayment(PLANT, fdate,tdate,"BILL",vendName,orderNo,billNo);
        
        DecimalFormat decformat = new DecimalFormat("#,##0.00");
		double totaldue = 0;
		double amount=0;
		double balance=0;
//		int k=0;
		int index=0;
		String total="";//,stmtdate="";
		if(movQrycustList.size()>0){
        for (int i = 0; i < movQrycustList.size(); i++){
        	Map lineArrcust = (Map) movQrycustList.get(i);
        	custName = (String)lineArrcust.get("custname");
//            custcode = (String)lineArrcust.get("custcode");
//            currencyid = (String)lineArrcust.get("currencyid");
//            String pmtdays = (String)lineArrcust.get("pmtdays");
            
            movQryList = billUtil.getPaymentStatementDetails(PLANT, fdate, tdate, "BILL", custName, "");
            
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
           		total = decformat.format(totaldue);
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("TRANDATE",StrUtils.fString(ordDate));
				resultJsonInt.put("ORDERNAME",StrUtils.fString(orderName));
				resultJsonInt.put("ORDNO",StrUtils.fString(orderno));
				resultJsonInt.put("CUSTCODE",StrUtils.fString(custName));
				resultJsonInt.put("AMOUNT",amount);
				resultJsonInt.put("BALANCE",totaldue);    
				resultJsonInt.put("TOTAL",total);    
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
            resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
    }
		return resultJson;        
	}

	
	private JSONObject getBillNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BillUtil billUtil = new BillUtil();
        ArrayList movQryList  = new ArrayList();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String vendName = StrUtils.fString(request.getParameter("CNAME"));
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String GRNO = StrUtils.fString(request.getParameter("GRNO"));
        	String bill = StrUtils.fString(request.getParameter("BILL"));
        	int index=0;
        	Hashtable ht = new Hashtable();
        	ht.put("VENDNO", vendName);
        	ht.put("PONO", orderNo);
        	ht.put("GRNO", GRNO);
        	ht.put("BILL", bill);
        	
        	movQryList = billUtil.getBillNoForAutoSuggestion(ht, plant);
        	
        	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
           		
           		index = index + 1;           		
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("BILL",(String)lineArr.get("BILL"));
				resultJsonInt.put("VENDNO",(String)lineArr.get("VENDNO"));
				resultJsonInt.put("PONO",(String)lineArr.get("PONO"));
				resultJsonInt.put("BILL_DATE",(String)lineArr.get("BILL_DATE"));
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
				resultJsonInt.put("GRNO",(String)lineArr.get("GRNO"));
				resultJsonInt.put("ADJUSTMENT_LABEL",(String)lineArr.get("ADJUSTMENT_LABEL"));
				resultJsonInt.put("SHIPMENT_CODE",(String)lineArr.get("SHIPMENT_CODE"));
				resultJsonInt.put("REFERENCE_NUMBER",(String)lineArr.get("REFERENCE_NUMBER"));
				resultJsonInt.put("BILL_TYPE",(String)lineArr.get("BILL_TYPE"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("BillDetails", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	private JSONObject getBillNoForAutoSuggestionForPoReturn(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BillUtil billUtil = new BillUtil();
        ArrayList movQryList  = new ArrayList();
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
        	String vendName = StrUtils.fString(request.getParameter("CNAME"));
        	String orderNo = StrUtils.fString(request.getParameter("ORDERNO"));
        	String GRNO = StrUtils.fString(request.getParameter("GRNO"));
        	String bill = StrUtils.fString(request.getParameter("BILL"));
        	int index=0;
        	Hashtable ht = new Hashtable();
        	ht.put("VENDNO", vendName);
        	ht.put("PONO", orderNo);
        	ht.put("GRNO", GRNO);
        	ht.put("BILL", bill);
        	
        	movQryList = billUtil.getBillNoForAutoSuggestionForPoReturn(ht, plant);
        	
        	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
           		Map lineArr = (Map) movQryList.get(iCnt);
           		JSONObject resultJsonInt = new JSONObject();
           		
           		index = index + 1;           		
           		
           		resultJsonInt.put("Index",(index));
				resultJsonInt.put("BILL",(String)lineArr.get("BILL"));
				resultJsonInt.put("VENDNO",(String)lineArr.get("VENDNO"));
				resultJsonInt.put("PONO",(String)lineArr.get("PONO"));
				resultJsonInt.put("BILL_DATE",(String)lineArr.get("BILL_DATE"));
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
				resultJsonInt.put("GRNO",(String)lineArr.get("GRNO"));
				resultJsonInt.put("ADJUSTMENT_LABEL",(String)lineArr.get("ADJUSTMENT_LABEL"));
				resultJsonInt.put("SHIPMENT_CODE",(String)lineArr.get("SHIPMENT_CODE"));
				resultJsonInt.put("REFERENCE_NUMBER",(String)lineArr.get("REFERENCE_NUMBER"));
				resultJsonInt.put("BILL_TYPE",(String)lineArr.get("BILL_TYPE"));
                jsonArray.add(resultJsonInt);
            }
        	resultJson.put("BillDetails", jsonArray);
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	private JSONObject getEditBillingDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        BillUtil billUtil = new BillUtil();
        ItemUtil itemUtil = new ItemUtil();
		

		PlantMstDAO plantMstDAO = new PlantMstDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String billHdrId = StrUtils.fString(request.getParameter("ID")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="",qty="",Converteddiscount="",
		    		ConvertedUnitCost_Aod="",ConvertedAmount_Aod="",ConvertedAmount_Aor="";//,returnQty="",netQty;
		    
		    Hashtable ht=new Hashtable();
		    ht.put("PLANT", plant);
		    ht.put("ID", billHdrId);
		    List billHdrList =  billUtil.getBillHdrById(ht);
		    Map billHdr=(Map)billHdrList.get(0);
		    
		    ht=new Hashtable();
		    ht.put("BILLHDRID", billHdrId);
		    ht.put("PLANT", plant);
		    List listQry = billUtil.getBillDetByHdrId(ht);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();

					
					double dCost = Double.parseDouble((String)m.get("COST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);

					
					double dQty = Double.parseDouble((String)m.get("QTY"));
					qty = StrUtils.addZeroes(dQty, "3");

					
					double dReturnQty = Double.parseDouble((String)m.get("RETURN_QTY"));
//					returnQty = StrUtils.addZeroes(dReturnQty, "3");

					
					double ddiscount = Double.parseDouble((String)m.get("DISCOUNT"));
					Converteddiscount = StrUtils.addZeroes(ddiscount, numberOfDecimal);

					
					
					double dAmount = Double.parseDouble((String)m.get("AMOUNT"));
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);

					
					double dUnitCost_Aod = Double.parseDouble((String)m.get("UNITCOST_AOD"));
					ConvertedUnitCost_Aod = StrUtils.addZeroes(dUnitCost_Aod, numberOfDecimal);

					
					double dAmount_Aod = dUnitCost_Aod * dQty;
					ConvertedAmount_Aod = StrUtils.addZeroes(dAmount_Aod, numberOfDecimal);

					
					double dNetQty = dQty - dReturnQty;
//					netQty = StrUtils.addZeroes(dNetQty, numberOfDecimal);

					
					double dAmount_Aor = (dAmount/dQty) * (dNetQty); /*Amount_AfterOrderReturn*/
					ConvertedAmount_Aor = StrUtils.addZeroes(dAmount_Aor, numberOfDecimal);

					
					resultJsonInt.put("POLNNO", (String)m.get("LNNO"));
					resultJsonInt.put("DETID", (String)m.get("ID"));
					resultJsonInt.put("ITEM_DISCOUNT", Converteddiscount);
					resultJsonInt.put("TAX_TYPE", (String)m.get("TAX_TYPE"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("UNITCOST", ConvertedUnitCost);					
					resultJsonInt.put("QTYOR", qty);					
					resultJsonInt.put("ACCOUNT", (String)m.get("ACCOUNT_NAME"));
					resultJsonInt.put("EMPNO", (String)billHdr.get("EMPNO"));		
					resultJsonInt.put("EMP_NAME", (String)billHdr.get("EMP_NAME"));

					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("PURCHASE_LOCATION", (String)billHdr.get("PURCHASE_LOCATION"));
                    resultJsonInt.put("INBOUND_GST", (String)billHdr.get("INBOUND_GST"));
                    resultJsonInt.put("STATE_PREFIX", (String)billHdr.get("STATE_PREFIX"));
                    resultJsonInt.put("LANDED_COST", (String)m.get("LANDED_COST"));
                    resultJsonInt.put("ITEM_DISCOUNT_TYPE", (String)m.get("DISCOUNT_TYPE"));
                    resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
                    resultJsonInt.put("BILL_STATUS", (String)m.get("BILL_STATUS"));
                    resultJsonInt.put("UNITCOST_AOD", ConvertedUnitCost_Aod);
                    resultJsonInt.put("AMOUNT_AOD", ConvertedAmount_Aod);
                    resultJsonInt.put("RETURNQTY", dReturnQty);
                    resultJsonInt.put("NETQTY", dNetQty);
                    resultJsonInt.put("AMOUNT_AOR", ConvertedAmount_Aor);
                    
                    resultJsonInt.put("UOM", (String)m.get("UOM"));
                    resultJsonInt.put("LOC", (String)m.get("LOC"));
                    resultJsonInt.put("BATCH", (String)m.get("BATCH"));
                    
                    //IMTI modified on 14-03-2022 to display qty and cost hover
                    String IBDiscount = new POUtil().getIBDiscountSelectedItemVNO(plant,(String)billHdr.get("VENDNO"),
                    		(String)m.get("ITEM"));
					String discounttype = "";

					int plusIndex = IBDiscount.indexOf("%");
					if (plusIndex != -1) {
						IBDiscount = IBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}
					if(IBDiscount.equalsIgnoreCase(""))
						IBDiscount="0.00";
        			List listItem = itemUtil.queryItemMstDetailsforpurchase((String)m.get("ITEM"), plant);
        			
        			Map arrItem = new ItemMstUtil().GetProductForPurchase((String)m.get("ITEM"),plant);
		            
        			
        			//IMTI modified on 17-03-2022 To display aval.qty in hover
        			String estQty = "", avlbQty = "";
                    Hashtable hts = new Hashtable();
             	 	hts.put("item", (String)m.get("ITEM"));
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
        			
//					Vector arrItem = (Vector) listItem.get(0);
					if (arrItem.size() > 0) {
						//ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,StrUtils.fString((String)arrItem.get(41)));
						ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,"0");
						avlbQty= (String) ma.get("AVLBQTY");
//						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get(8)));
//						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
//						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
//						resultJsonInt.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
//						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get(41)));
						
						resultJsonInt.put("minstkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
						resultJsonInt.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
						resultJsonInt.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
						resultJsonInt.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						resultJsonInt.put("incommingqty",StrUtils.fString((String)arrItem.get("INCOMINGQTY")));
						
						resultJsonInt.put("customerdiscount", IBDiscount);
						resultJsonInt.put("discounttype", discounttype);
						resultJsonInt.put("EstQty",estQty);
//						resultJsonInt.put("EstQty", "0.00");
						resultJsonInt.put("AvlbQty",avlbQty);
//						resultJsonInt.put("AvlbQty", "0.00");
					}
					//END
					
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
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}

	
	private JSONObject getbilldetailview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       // HTReportUtil movHisUtil       = new HTReportUtil();
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           String  ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
           String  ITEM = StrUtils.fString(request.getParameter("ITEM"));
		   String  BILL = StrUtils.fString(request.getParameter("BILL"));
		   String  GRNO = StrUtils.fString(request.getParameter("GRNO"));
		   String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
		   String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
		   String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
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
		   if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
		   if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
		   if(StrUtils.fString(GRNO).length() > 0)       ht.put("GRNO",GRNO);
		   if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
		   if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
		   movQryList = billUtil.getBillDetailsSView(ht,fdate,tdate,PLANT,CUSTOMER);	
		   if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0
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
                   			String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
                   			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
                   			String availablecredit = (String)lineArr.get("AVAILABLE_CREDIT");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(balCostVal, numberOfDecimal);
                            double availablecreditValue ="".equals(availablecredit) ? 0.0d :  Double.parseDouble(availablecredit);        					
                            availablecredit = StrUtils.addZeroes(availablecreditValue, numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("billdate",StrUtils.fString((String)lineArr.get("BILL_DATE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("BILL")));
                    	 resultJsonInt.put("jobnum",StrUtils.fString((String)lineArr.get("PONO")));
                    	 resultJsonInt.put("custno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("custname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("status",StrUtils.fString((String)lineArr.get("BILL_STATUS")));
                    	 resultJsonInt.put("duedate",StrUtils.fString((String)lineArr.get("DUE_DATE")));
                    	 String cur = StrUtils.fString((String)lineArr.get("CURRENCYID"));
                    	 if(cur.isEmpty())
                    		 cur= curency;
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("balancedue",balCostValue);
							/*
							 * resultJsonInt.put("amount",cur+unitCostValue);
							 * resultJsonInt.put("balancedue",cur+balCostValue);
							 */                    	 resultJsonInt.put("availablecredit",cur+StrUtils.fString(availablecredit));
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

	
	private HSSFWorkbook writeToExcelBillDetails(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
//        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		BillUtil billUtil = new BillUtil();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "", custcode = "";
		int SheetId =1;
		try {
			   String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String CUSTOMER = StrUtils.fString(request.getParameter("vendname"));
	           String ORDERNO = StrUtils.fString(request.getParameter("pono"));
	           String ITEM = StrUtils.fString(request.getParameter("item"));
			   String BILL = StrUtils.fString(request.getParameter("bill"));
			   String GRNO = StrUtils.fString(request.getParameter("grno"));
			   String REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
			   String STATUS = StrUtils.fString(request.getParameter("status"));
//			   String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	           PlantMstDAO plantMstDAO = new PlantMstDAO();
	           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	        	   fdate = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	   
	           Hashtable ht = new Hashtable();
	           if(StrUtils.fString(ORDERNO).length() > 0)       ht.put("PONO",ORDERNO);
			   if(StrUtils.fString(ITEM).length() > 0)       ht.put("ITEM",ITEM);
			   if(StrUtils.fString(BILL).length() > 0)       ht.put("BILL",BILL);
			   if(StrUtils.fString(GRNO).length() > 0)       ht.put("GRNO",GRNO);
			   if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
			   if(StrUtils.fString(STATUS).length() > 0)       ht.put("STATUS",STATUS);
			   movQryList = billUtil.getBillDetailsSView(ht,fdate,tdate,PLANT,CUSTOMER);	
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "");
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
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String unitCostValue = (String)lineArr.get("BASE_TOTAL_AMOUNT");
               			String balCostValue = (String)lineArr.get("BASE_BALANCE_DUE");
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
                        balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("BILL_STATUS"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("BILL_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DUE_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("BILL"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PONO"))));
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

	
	private JSONObject getpaymentsmadeview(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           String  SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER"));
           String  cur = StrUtils.fString(request.getParameter("CURENCY"));
           String  decimal = StrUtils.fString(request.getParameter("DECIMAL"));
           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
           
            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
           String curDate =DateUtils.getDate();
           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

           if (FROM_DATE.length()>5)
            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
           if (TO_DATE.length()>5)
           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

              
   
           Hashtable ht = new Hashtable();
           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
		   movQryList = billUtil.getPaymentsMadeSummaryView(ht,fdate,tdate,PLANT,SUPPLIER);	
		   if (movQryList.size() > 0) {
            int Index = 0;//iIndex = 0,
//             int irow = 0;
//            double sumprdQty = 0;String lastProduct="";
//            double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);                            
                               
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
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
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
                    	 resultJsonInt.put("billpaymentdate",StrUtils.fString((String)lineArr.get("PAYMENT_DATE")));
                    	 resultJsonInt.put("paymentmode",StrUtils.fString((String)lineArr.get("PAYMENT_MODE")));
                    	 resultJsonInt.put("paidthrough",StrUtils.fString((String)lineArr.get("PAID_THROUGH")));
                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
                    	 resultJsonInt.put("vendname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("reference",StrUtils.fString((String)lineArr.get("REFERENCE")));
                    	 resultJsonInt.put("bill",StrUtils.fString((String)lineArr.get("BILL")));
                    	 resultJsonInt.put("note",StrUtils.fString((String)lineArr.get("NOTE")));                    	 
                    	 resultJsonInt.put("amount",unitCostValue);
                    	 resultJsonInt.put("unusedamount",balCostValue);
							/*
							 * resultJsonInt.put("amount",cur+unitCostValue);
							 * resultJsonInt.put("unusedamount",cur+balCostValue);
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
                resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }

        return resultJson;        
        

	}

	
	private HSSFWorkbook writeToExcelpaymentsMade(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
//        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		BillUtil billUtil = new BillUtil();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "", custcode = "";
		int SheetId =1;
		try {
			String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String  SUPPLIER = StrUtils.fString(request.getParameter("vendname"));
//	           String  cur = StrUtils.fString(request.getParameter("curency"));
	           String  decimal = StrUtils.fString(request.getParameter("DECIMALNO"));
	           String  REFERENCE = StrUtils.fString(request.getParameter("REFERENCE"));
	           
	            if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

	           Hashtable ht = new Hashtable();
	           if(StrUtils.fString(REFERENCE).length() > 0)       ht.put("REFERENCE",REFERENCE);
			   movQryList = billUtil.getPaymentsMadeSummaryView(ht,fdate,tdate,PLANT,SUPPLIER);		
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "paymentsmade");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "paymentsmade");
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
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String unitCostValue = (String)lineArr.get("TOTAL_AMOUNT");
               			String balCostValue = (String)lineArr.get("BALANCE_DUE");
                        float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00000";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        
                        unitCostValue = StrUtils.addZeroes(Double.parseDouble(unitCostValue), decimal);
                        
                        float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                        if(balCostVal==0f){
                        	balCostValue="0.00000";
                        }else{
                        	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        balCostValue = StrUtils.addZeroes(Double.parseDouble(balCostValue), decimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PAYMENT_DATE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("REFERENCE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("BILL"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("VNAME"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PAYMENT_MODE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("NOTE"))));
						cell.setCellStyle(dataStyle);
						
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PAID_THROUGH"))));
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
							sheet = this.createWidthExpensesSummary(sheet, "paymentsmade");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "paymentsmade");
							sheet = this.createHeaderCompanyReports(sheet,CompHeader,"",PLANT);
						}
					}

	           }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}

	
	private JSONObject getVendorBalances(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       // HTReportUtil movHisUtil       = new HTReportUtil();
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
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
           
           if(!CUSTOMER.equalsIgnoreCase("")) {
        	   CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
        	   CUSTOMER = customerBeanDAO.getVendorCode(PLANT, CUSTOMER);
           }

           Hashtable ht = new Hashtable();
		   movQryList = billUtil.getVendorBalances(ht,fdate,tdate,PLANT,CUSTOMER);	
		   if (movQryList.size() > 0) {
			   int Index = 0;
//			   int iIndex = 0,irow = 0;
//			   double sumprdQty = 0;String lastProduct="";
//			   double totalRecvCost=0,totaltax=0,totRecvCostWTax=0,costGrandTot=0,
//					   taxGrandTot=0,costWTaxGrandTot=0;
           
                for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//                            String result="";
                            Map lineArr = (Map) movQryList.get(iCnt);
//                            String custcode =(String)lineArr.get("custname");
//                            String pono = (String)lineArr.get("pono");   
//                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                            JSONObject resultJsonInt = new JSONObject();
                   			String unitCostValue = (String)lineArr.get("BALANCE_DUE");
                   			String balCostValue = (String)lineArr.get("EXCESS_PAYMENT");
                   			String balanceValue = (String)lineArr.get("BALANCE");
                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                            if(unitCostVal==0f){
                            	unitCostValue="0.00000";
                            }else{
                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                            
                            float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                            if(balCostVal==0f){
                            	balCostValue="0.00000";
                            }else{
                            	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                            }
                            balCostValue = StrUtils.addZeroes(balCostVal, numberOfDecimal);
                            
                            double balanceVal ="".equals(balanceValue) ? 0.0d :  Double.parseDouble(balanceValue);        					
                            balanceValue = StrUtils.addZeroes(balanceVal, numberOfDecimal);
                      
                    	 Index = Index + 1;
                    	 resultJsonInt.put("Index",(Index));
                    	 resultJsonInt.put("vname",StrUtils.fString((String)lineArr.get("VNAME")));
                    	 resultJsonInt.put("billbalance",unitCostValue);
                    	 resultJsonInt.put("excesspayment",balCostValue);
                    	 resultJsonInt.put("balance",StrUtils.fString(balanceValue));
							/*
							 * resultJsonInt.put("billbalance",curency+unitCostValue);
							 * resultJsonInt.put("excesspayment",curency+balCostValue);
							 * resultJsonInt.put("balance",curency+StrUtils.fString(balanceValue));
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
                resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
                resultJsonInt.put("ERROR_CODE", "98");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	private HSSFWorkbook writeToExcelVendorBalances(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
//		ExpensesUtil expensesUtil       = new  ExpensesUtil();
//        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		BillUtil billUtil = new BillUtil();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";//, taxby = "", custcode = "";
		int SheetId =1;
		try {
			String PLANT= StrUtils.fString(request.getParameter("plant"));
	           String FROM_DATE   = StrUtils.fString(request.getParameter("FROM_DATE"));
	           String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));           
	           String  SUPPLIER = StrUtils.fString(request.getParameter("vendname"));
//	           String  cur = StrUtils.fString(request.getParameter("curency"));
	           
	           if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	           String curDate =DateUtils.getDate();
	           if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	           if (FROM_DATE.length()>5)
	            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	           if (TO_DATE.length()>5)
	           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	           
	           if(!SUPPLIER.equalsIgnoreCase("")) {
	        	   CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	        	   SUPPLIER = customerBeanDAO.getVendorCode(PLANT, SUPPLIER);
	           }
	           PlantMstDAO plantMstDAO = new PlantMstDAO();
	           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	           
	           Hashtable ht = new Hashtable();
			   movQryList = billUtil.getVendorBalances(ht,fdate,tdate,PLANT,SUPPLIER);		
	           
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
					sheet = this.createWidthExpensesSummary(sheet, "vendorBalances");
					sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "vendorBalances");
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
					
					DecimalFormat decimalFormat = new DecimalFormat("#.#####");
					decimalFormat.setRoundingMode(RoundingMode.FLOOR);
					
					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);
						int k = 0;
						
						String unitCostValue = (String)lineArr.get("BALANCE_DUE");
               			String balCostValue = (String)lineArr.get("EXCESS_PAYMENT");
               			String balanceValue = (String)lineArr.get("BALANCE");
                        float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
                        if(unitCostVal==0f){
                        	unitCostValue="0.00000";
                        }else{
                        	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
                        
                        float balCostVal ="".equals(balCostValue) ? 0.0f :  Float.parseFloat(balCostValue);
                        if(balCostVal==0f){
                        	balCostValue="0.00000";
                        }else{
                        	balCostValue=balCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
                        }
                        balCostValue = StrUtils.addZeroes(balCostVal, numberOfDecimal);
                        
                        double balanceVal ="".equals(balanceValue) ? 0.0d :  Double.parseDouble(balanceValue);        					
                        balanceValue = StrUtils.addZeroes(balanceVal, numberOfDecimal);
						
						HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
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
							sheet = this.createWidthExpensesSummary(sheet, "vendorBalances");
							sheet = this.createHeaderExpensesSummary(sheet, styleHeader, "vendorBalances");
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
			String CNAME = "",  CZIP = "", CCOUNTRY = "", CSTATE=""; 
//					CONTACTNAME = "", CHPNO = "";//, CEMAIL = "", COL1="";//,COL2="" ;//CADD1 = "",CADD4 = "", CADD2 = "", CADD3 = "",CRCBNO = "",CTEL = "",CFAX = "",  
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
				if(type.equalsIgnoreCase("paymentsmade")){
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 3000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
				}else if(type.equalsIgnoreCase("vendorBalances")){
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
				}else {
					sheet.setColumnWidth(i++, 2000);
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 3500);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 5000);
					sheet.setColumnWidth(i++, 4000);
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
			if(type.equalsIgnoreCase("paymentsmade")){
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Reference"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Bill"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Supplier Name"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Payment Mode"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Notes"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Paid Through"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Amount"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Unused Amount"));
				cell.setCellStyle(styleHeader);
			}else if(type.equalsIgnoreCase("vendorBalances")){
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Supplier Name"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Bill Balance"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Excess Payment"));
				cell.setCellStyle(styleHeader);

				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Balance"));
				cell.setCellStyle(styleHeader);
			}else {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Status"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Bill Date"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Due Date"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Bill"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Order No"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Supplier Name"));
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
	
	private JSONObject getPurchaseDashbordview(HttpServletRequest request,String PLANT) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
       // HTReportUtil movHisUtil       = new HTReportUtil();
        BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        ArrayList movQryListfilter1  = new ArrayList();
        ArrayList movQryListfilter2  = new ArrayList();
        String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
        
//        String decimalZeros = "";
//        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
//            decimalZeros += "#";
//        }
//        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
//       
//        StrUtils strUtils = new StrUtils();
//        String fdate="",tdate="",taxby="",covunitCostValue="0";
         try {
        
           //String PLANT= StrUtils.fString(request.getParameter("PLANT"));
           String FROM_DATE   = StrUtils.fString(request.getParameter("FDATE"));
           String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));           
           String  CUSTOMER = StrUtils.fString(request.getParameter("CNAME"));
		   String  STATUS = StrUtils.fString(request.getParameter("STATUS"));
           PlantMstDAO plantMstDAO = new PlantMstDAO();
           String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
           
		   movQryList = billUtil.getpurchaseDashboard(PLANT,FROM_DATE,TO_DATE,STATUS,CUSTOMER,numberOfDecimal);	
		   if (movQryList.size() > 0) {
			   int Index = 0;
//			   int iIndex = 0,irow = 0;
			   double totaldebit=0;
           
			   for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
//				   String result="";
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
					   resultJsonInt.put("DEBITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
					   
					   jsonArray.add(resultJsonInt);
	        	            	
					   totaldebit = totaldebit + Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT"));
				   }
			   }
               
			   if (CUSTOMER.length()>0){
				   if (movQryListfilter1.size() > 0) {
					   totaldebit=0;
					   jsonArray.clear();
					   for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
//						   String result="";
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
							   resultJsonInt.put("DEBITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
							   
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
//		 				   String result="";
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
		 					   resultJsonInt.put("DEBITS",StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT")), numberOfDecimal));
		 					   
		 					   jsonArray.add(resultJsonInt);
		 	        	            	
		 					   totaldebit = totaldebit + Double.parseDouble((String)lineArr.get("TOTAL_AMOUNT"));
		 				   }
		 			   }
	   			   }
	  		   }
	   		   
	           String totalWithoutCurrency=StrUtils.addZeroes(totaldebit, numberOfDecimal);
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
                    //jsonArray.add("");
                    resultJson.put("items", jsonArray);

                    resultJson.put("errors", jsonArrayErr);
            }
        } catch (Exception e) {
        		//jsonArray.add("");
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
	

	public boolean processPodetForReversal(Map map) throws Exception {

		boolean flag = false;
		try {
			PoDetDAO _PoDetDAO= new PoDetDAO();
			PoHdrDAO _PoHdrDAO = new PoHdrDAO();
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
			ReturnOrderDAO returnOrderDao = new ReturnOrderDAO();
			_PoDetDAO.setmLogger(mLogger);
			_PoHdrDAO.setmLogger(mLogger);
			_RecvDetDAO.setmLogger(mLogger);
			returnOrderDao.setmLogger(mLogger);

			
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
	        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
			

			Hashtable htCondiRecvDet = new Hashtable();

			
//				StringBuffer query = new StringBuffer("");
				htCondiRecvDet.put("PLANT", map.get("PLANT"));
				htCondiRecvDet.put("pono", map.get("PONO"));
				htCondiRecvDet.put("lnno", map.get("POLNNO"));
				htCondiRecvDet.put("item", map.get("ITEM"));
				htCondiRecvDet.put("batch", map.get("BATCH"));
				htCondiRecvDet.put("LOC", map.get("LOC"));
				htCondiRecvDet.put("GRNO", map.get("BILL"));
				htCondiRecvDet.put("STATUS","C");				
			

			double reverseqty = Double.parseDouble((String) map.get("QTY"));
//			String qty = "";
			ALRecvddetails = new ArrayList();
			if(!nonstocktype.equals("Y"))
					ALRecvddetails = _RecvDetDAO.getRcvdDetailsforreverse(map.get("PLANT").toString(), htCondiRecvDet);
			else
					ALRecvddetails = _RecvDetDAO.getRcvdDetailsforreverse_nonstock(map.get("PLANT").toString(), htCondiRecvDet);
					if (ALRecvddetails.size() > 0) {
						for (int i = 0; i < ALRecvddetails.size(); i++) {
							Map m = (Map) ALRecvddetails.get(i);
							
							if(reverseqty == 0) break;
							
//							  String pono = (String)m.get("pono");
							  double recqty = Double.parseDouble((String)m.get("RECQTY"));
					          String crat = (String)m.get("CRAT");
					          String ID = (String)m.get("ID").toString();
					          
					          
					          String updaterecdet = "";
					          
					          if(reverseqty >= recqty)
					        	  updaterecdet = "set recqty = recqty-" +recqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					          else
					          {
					        	  updaterecdet = "set recqty = recqty-" +reverseqty +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					        	  recqty=reverseqty;
					          }
					        	  
					          htCondiRecvDet.put("CRAT", crat);
					          if(!ID.equalsIgnoreCase("0"))
					        	  htCondiRecvDet.put("ID", ID);
					          if(reverseqty>0)
					          {
					        	  boolean isexists = _RecvDetDAO.isExisit(htCondiRecvDet, map.get("PLANT").toString());
									 if(isexists)
									 {
								          flag = _RecvDetDAO.update(updaterecdet, htCondiRecvDet, "", map.get("PLANT").toString());
								          reverseqty=reverseqty-recqty;
									 }
					          }							
						}
					
				
					 htCondiRecvDet.remove("CRAT");
					 htCondiRecvDet.remove("ID");
					}
					 htCondiRecvDet.put("RECQTY", "0.000");
					 
					 boolean isexists = _RecvDetDAO.isExisit(htCondiRecvDet, map.get("PLANT").toString());
					 if(isexists)
					 {
						 flag = _RecvDetDAO.deleteRecvDet(htCondiRecvDet);
					 }
					

			if (!flag) {
				flag = false;
				throw new Exception("Product Reversed  Failed, Error In update  PODET :"+ " " + map.get("ITEM"));
				} else {
				flag = true;

		
			}				
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
		

		}
		return flag;
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

	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.REVERSE_BILL);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get("BILL"));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htRecvHis.put("REMARKS", map.get(IConstants.CUSTOMER_NAME)+","+map.get(IConstants.PODET_PONUM)+","+map.get(IConstants.GRNO)+","+map.get("BILL")+","+map.get("NOTES"));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			

		}
		return flag;
	}

	
	public boolean processPodetForReceive(Map map) throws Exception {

		boolean flag = false;
		try {

			
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();

//			Hashtable htCondiPoDet = new Hashtable();
//			StringBuffer query = new StringBuffer("");

//			double ordQty = Double.parseDouble((String) map
//					.get(IConstants.ORD_QTY));
			double rcQty = Double.parseDouble((String) map
					.get(IConstants.RECV_QTY));
			
			

			double sumqty = rcQty;
			sumqty = StrUtils.RoundDB(sumqty,IConstants.DECIMALPTS );

//			String queryPoDet = "";
//			String queryPoHdr = "";

//			if (ordQty == sumqty) {
//				queryPoDet = " set qtyrc= isNull(qtyrc,0) + "
//						+ map.get(IConstants.ORD_QTY) + " , LNSTAT='C' ";
//
//			} else {
//				queryPoDet = " set qtyrc= isNull(qtyrc,0) + "
//						+ map.get(IConstants.ORD_QTY) + " , LNSTAT='O' ";
//
//			}

			
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM, map
					.get(IConstants.PODET_PONUM));
			htRecvDet.put("LNNO", map.get(IConstants.PODET_POLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, StrUtils.InsertQuotes((String)map
					.get(IConstants.CUSTOMER_NAME)));
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC,StrUtils.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));

			
			htRecvDet.put(IDBConstants.CURRENCYID, map.get(IConstants.CURRENCYID));

			
			
			htRecvDet.put("UNITCOST",map.get("UNITCOST"));
			htRecvDet.put("MANUFACTURER","");
			
			htRecvDet.put("REMARK",StrUtils.InsertQuotes((String) map.get(IConstants.REMARKS)));
			htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
		    //htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "IB");
		    htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    htRecvDet.put("EXPIRYDAT", map.get(IConstants.EXPIREDATE));
		    htRecvDet.put(IConstants.GRNO, map.get("BILL"));//Ravindra
		    htRecvDet.put("STATUS", "C");
		    if((String)map.get(IConstants.RECVDET_TRANTYPE)!=null)
		    htRecvDet.put((IConstants.RECVDET_TRANTYPE), map.get(IConstants.RECVDET_TRANTYPE));
			String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
			if(nonstocktype.equalsIgnoreCase("Y"))	
		    {
				htRecvDet.put("RECQTY", String.valueOf(map.get(IConstants.RECV_QTY)));
				flag = _RecvDetDAO.insertRecvDet(htRecvDet);
		    }
			else
			{
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String extCond = "";			
				extCond = "QTY > 0";			
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMstDes("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.RECV_QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					htRecvDet.put("RECQTY", String.valueOf(adjustedQuantity));
					htRecvDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			quantityToAdjust -= adjustedQuantity;
				}
			}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;

		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
            if(!map.get(IConstants.BATCH).equals("NOBATCH")){
            	
	           

				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));

	
				double inqty = Double.valueOf((String)map.get(IConstants.ORD_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
					htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
					htInvMst.put(IDBConstants.QTY,String.valueOf(inqty));
					htInvMst.put(IDBConstants.USERFLD3, map.get(IConstants.EXPIREDATE));
					//htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.TRAN_DATE).toString().replaceAll("-", "").replaceAll("/", "") + "000000");	
					htInvMst.put(IDBConstants.STATUS, "");
	
					flag = _InvMstDAO.insertInvMst(htInvMst);
//				}

			}else//if NOBATCH
			{
				double inqty = Double.valueOf((String)map.get(IConstants.ORD_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));	
				

				flag = _InvMstDAO.isExisit(htInvMst, "");

				
				if (flag  ){
	

					StringBuffer sql = new StringBuffer(" SET ");
					sql.append(IDBConstants.QTY + " = QTY +'"
							+ inqty + "'");
					sql.append("," + IDBConstants.USERFLD3 + " = '"
							+ map.get(IConstants.EXPIREDATE) + "'");
					sql.append("," + IDBConstants.USERFLD4 + " = '"
							+ map.get(IConstants.BATCH) + "'");
					sql.append("," + IDBConstants.UPDATED_AT + " = '"
							+ DateUtils.getDateTime() + "'");
					sql.append("," + IDBConstants.EXPIREDATE + " = '"
							+ map.get(IConstants.EXPIREDATE) + "'");
					flag = _InvMstDAO.update(sql.toString(), htInvMst, "");

	
				}  else {
					htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
					htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
					htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
					htInvMst.put(IDBConstants.USERFLD3, map.get(IConstants.EXPIREDATE));
					//htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.TRAN_DATE).toString().replaceAll("-", "").replaceAll("/", "") + "000000");
	

					htInvMst.put(IDBConstants.STATUS, "");

	
					flag = _InvMstDAO.insertInvMst(htInvMst);
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
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			String pono = (String)map.get(IConstants.PODET_PONUM);
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
//			htMovhis.put("DIRTYPE", TransactionConstants.CREATE_BILL);
			htMovhis.put("DIRTYPE", TransactionConstants.ORD_RECV);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("MOVTID", "IN");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htMovhis.put(IDBConstants.POHDR_JOB_NUM, "");
			if(pono=="") {
				htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get("BILLNO"));
				htMovhis.put("REMARKS", StrUtils.InsertQuotes((String)map.get(IConstants.REMARKS))+","+map.get("BILL"));
			}else {
				htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
				htMovhis.put("REMARKS", StrUtils.InsertQuotes((String)map.get(IConstants.REMARKS)));
			}
//			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			if((String)map.get(IConstants.UOM)!=null)
				htMovhis.put(IDBConstants.UOM, map.get(IConstants.UOM));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put("QTY", map.get(IConstants.ORD_QTY));
			htMovhis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//			htMovhis.put("REMARKS", StrUtils.InsertQuotes((String)map.get(IConstants.REMARKS)));

			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	
	private void billImportTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadBillTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"BillData.xls\"");
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

			try {
				//Author: Azees  Create date: July 10,2021  Description: Fixed Import Null Error				
				com.oreilly.servlet.MultipartRequest mreq = new com.oreilly.servlet.MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			String USERID =(String) request.getSession().getAttribute("LOGIN_USER");
			String NOOFORDER =(String) request.getSession().getAttribute("NOOFORDER");
			request.getSession().setAttribute("IMP_OUTBOUNDRESULT", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			System.out.println("After conversion Import File  *********:"+ StrFileName);
			
			boolean limitstaus = true;
			CustomerBeanDAO venddao = new CustomerBeanDAO();
//          	DateUtils _dateUtils = new DateUtils();
        	String FROM_DATE = DateUtils.getDate();
        	if (FROM_DATE.length() > 5)
        		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
        	
        	String TO_DATE = DateUtils.getLastDayOfMonth();
        	if (TO_DATE.length() > 5)
        		TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
        	int noordvalid =venddao.Ordercount(PLANT,FROM_DATE,TO_DATE);
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
				alRes = util.downloadBillSheetData(PLANT, StrFileName,
						StrSheetName,baseCurrency,USERID,NOOFORDER);
				
	        	List<BillHdr> billhdrlist = new ArrayList<BillHdr>();
				if (alRes.size() > 0) {
					
					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
		        	{
						for (int j = 0; j < alRes.size(); j++) {
							Map billnumber=(Map)alRes.get(j);
							String bill = (String)billnumber.get("BILL");
							
							List<BillHdr> billhdrlistCheck = billhdrlist.stream().filter((b)->b.getBILL().equalsIgnoreCase(bill)).collect(Collectors.toList());
							if(billhdrlistCheck.size() == 0) {
								BillHdr billHdr = new BillHdr();
								billHdr.setBILL(bill);
								noordvalid = noordvalid + 1;
							}
							if(noordvalid>convl)
			        		{
			        			limitstaus = false;
			        			break;
			        		}
				          	
						}
						
						//below line comment by IMTHI 
						//[DESC: after importing the excel sheet the given excel data is not shown in import bill summary screen also CONFIRM button in importbill is disabled
//						int index = alRes.size() - 1; 
//						alRes.remove(index);
						
						if(limitstaus)
						{
						result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
						}
						else
						{
							result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" bill's you can create</font>";
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
        		result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" bill's you can create</font>";
        	}

			/*if (alRes.size() > 0) {
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}*/

		} catch (Exception e) {

			
			result = "<font color=\"red\">" + ThrowableUtil.getMessage(e) + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("OUTBOUNDRESULT", result);
		request.getSession().setAttribute("IMP_OUTBOUNDRESULT", alRes);
		//       
		response
				.sendRedirect("../bill/import?ImportFile="
						+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	
	private void onConfirmBillSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";

		UserTransaction ut = null;
		boolean flag = true;
//		StrUtils strUtils = new StrUtils();
//		DateUtils dateutils = new DateUtils();
		VendMstDAO vendMstDAO = new VendMstDAO();
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		MasterUtil _MasterUtil = new MasterUtil();
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_OUTBOUNDRESULT");

			List<BillHdr> billhdrlist = new ArrayList<BillHdr>();
			List<BillDet> billdetlist = new ArrayList<BillDet>();
			List<BillPojo> billpojolist = new ArrayList<BillPojo>();

			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);

				
				
				if(flag){
					String billCheck = (String)lineArr.get("BILL");
					List<BillHdr> billhdrlistCheck = billhdrlist.stream().filter((b)->b.getBILL().equalsIgnoreCase(billCheck)).collect(Collectors.toList());
					if(billhdrlistCheck.size() == 0) {
						
						
						String tran = (String)lineArr.get(IConstants.TRANSPORTID);
						int trans = Integer.parseInt(tran);
						
						String shippingid = "",shippingcustomer = "";
						CustUtil custUtil = new CustUtil();
	   						ArrayList arrList = custUtil.getCustomerListStartsWithName((String)lineArr.get("SHIPPINGID"),PLANT);
								Map mCustot=(Map)arrList.get(0);
								shippingcustomer=(String) mCustot.get("CNAME");
						
						BillHdr billHdr = new BillHdr();
						billHdr.setPLANT((String)lineArr.get("PLANT"));
						billHdr.setVENDNO((String)lineArr.get("SUPPLIERCODE"));
						billHdr.setBILL((String)lineArr.get("BILL"));
						//billHdr.setPONO((String)lineArr.get("PONO"));
						//billHdr.setGRNO((String)lineArr.get("GRNO"));
						billHdr.setPONO("null".equalsIgnoreCase((String)lineArr.get("PONO")) ? "" :  (String)lineArr.get("PONO"));
						billHdr.setGRNO("null".equalsIgnoreCase((String)lineArr.get("GRNO")) ? "" :  (String)lineArr.get("GRNO"));
						billHdr.setBILL_DATE((String)lineArr.get("BILLDATE"));
						billHdr.setDUE_DATE((String)lineArr.get("DUEDATE"));
						billHdr.setTRANSPORTID(trans);
						billHdr.setPAYMENT_TERMS((String)lineArr.get("PAYMENTTERMS"));
						billHdr.setEMPNO((String)lineArr.get("EMPNO"));
						billHdr.setORDERTYPE((String)lineArr.get("ORDERTYPE"));
						billHdr.setITEM_RATES((String)lineArr.get("ISTAXINCLUSIVE"));
						billHdr.setDISCOUNT_TYPE((String)lineArr.get("DISCOUNTTYPE"));
						billHdr.setDISCOUNT_ACCOUNT("");
						billHdr.setADJUSTMENT_LABEL("Adjustment");
						billHdr.setADJUSTMENT((String)lineArr.get("ADJUSTMENT"));
						billHdr.setSUB_TOTAL("");
						billHdr.setTOTAL_AMOUNT("");
						billHdr.setBILL_STATUS("Open");
						billHdr.setSHIPMENT_CODE((String)lineArr.get("SHIPPINGREFERENCE"));
						billHdr.setREFERENCE_NUMBER((String)lineArr.get("REFERENCE"));
						billHdr.setNOTE((String)lineArr.get("NOTES"));
						billHdr.setCRAT(DateUtils.getDateTime());
						billHdr.setCRBY(_login_user);
						billHdr.setUPAT(DateUtils.getDateTime());
						billHdr.setCREDITNOTESSTATUS("0");
						billHdr.setTAXAMOUNT("");
						billHdr.setISSHIPPINGTAXABLE((String)lineArr.get("ISSHIPPINGTAX"));
						billHdr.setPURCHASE_LOCATION((String)lineArr.get("PURCHASELOC"));
						String taxtreatment = vendMstDAO.gettaxtreatmentByNo((String)lineArr.get("PLANT"), (String)lineArr.get("SUPPLIERCODE"));
						billHdr.setTAXTREATMENT(taxtreatment);
						billHdr.setREVERSECHARGE((String)lineArr.get("REVERSECHARGE"));
						billHdr.setGOODSIMPORT((String)lineArr.get("GOODSIMPORT"));
						billHdr.setCURRENCYID((String)lineArr.get("CURRENCYID"));
						billHdr.setCURRENCYUSEQT((String)lineArr.get("EQUIVALENTCURRENCY"));
						billHdr.setORDERDISCOUNTTYPE((String)lineArr.get("ORDERDISCOUNTTYPE"));
						billHdr.setTAXID((String)lineArr.get("TAXID"));
						billHdr.setISDISCOUNTTAX((String)lineArr.get("ISDISCOUNTTAX"));
						billHdr.setISORDERDISCOUNTTAX((String)lineArr.get("ISORDERDISCOUNTTAX"));
						billHdr.setDISCOUNT((String)lineArr.get("DISCOUNT"));
						billHdr.setSHIPPINGCOST((String)lineArr.get("SHIPPINGCOST"));
						billHdr.setORDER_DISCOUNT((String)lineArr.get("ORDERDISCOUNT"));
						billHdr.setINBOUND_GST((String)lineArr.get("TAXPERCENTAGE"));
						billHdr.setPROJECTID((String)lineArr.get("PROJECTID"));
						billHdr.setSHIPPINGID((String)lineArr.get("SHIPPINGID"));
						billHdr.setSHIPPINGCUSTOMER(shippingcustomer);
						billHdr.setORIGIN("manual");

						
						String pono = billHdr.getPONO();
						String grno = billHdr.getGRNO();
//						String billno = billHdr.getBILL();

						
						if(pono.equalsIgnoreCase("") || pono == null || pono.equals(null)) {
							if(grno.equalsIgnoreCase("") || grno == null || grno.equals(null)) {
								billHdr.setDEDUCT_INV("0");
								billHdr.setBILL_TYPE("NON INVENTORY");
							}else {
								billHdr.setDEDUCT_INV("1");
								billHdr.setBILL_TYPE("INVENTORY");
							}
						}else {
							billHdr.setDEDUCT_INV("0");
							billHdr.setBILL_TYPE("PURCHASE");
						}

						
						billhdrlist.add(billHdr);
						

					}

					
					
					BillDet billDet = new BillDet();

					
					billDet.setPLANT(PLANT);
					billDet.setBILL((String)lineArr.get("BILL"));
					billDet.setLNNO((String)lineArr.get("LNNO"));
					billDet.setBILLHDRID("");
					billDet.setITEM((String)lineArr.get("PRODUCT"));
					billDet.setACCOUNT_NAME((String)lineArr.get("ACCOUNT"));
					billDet.setQTY((String)lineArr.get("QTY"));
					billDet.setCOST((String)lineArr.get("UNITPRICE"));
					billDet.setDISCOUNT((String)lineArr.get("PRODUCTDISCOUNT"));
					billDet.setDISCOUNT_TYPE((String)lineArr.get("PRODUCTDISCOUNT_TYPE"));
					billDet.setTax_Type((String)lineArr.get("TAX"));
					double amount = Double.valueOf((String)lineArr.get("QTY")) * Double.valueOf((String)lineArr.get("UNITPRICE"));
					double pdiscount = Double.valueOf((String)lineArr.get("PRODUCTDISCOUNT"));
					String pdiscounttype = (String)lineArr.get("PRODUCTDISCOUNT_TYPE");
					double lamount = 0;
					if(pdiscounttype.equalsIgnoreCase("%")) {
						lamount = amount - ((amount/100)*pdiscount);
					}else {
						lamount = amount - pdiscount;
					}
					billDet.setAmount(String.valueOf(lamount));
					billDet.setLANDED_COST("0");
					billDet.setCURRENCYUSEQT((String)lineArr.get("EQUIVALENTCURRENCY"));
					billDet.setLOC((String)lineArr.get("LOCATIONID"));
					billDet.setUOM((String)lineArr.get("UOM"));
					billDet.setBATCH((String)lineArr.get("BATCH"));
					billDet.setCRAT(DateUtils.getDateTime());
					billDet.setCRBY(_login_user);
					billDet.setUPAT(DateUtils.getDateTime());
					

					billdetlist.add(billDet);

					
				}else{
					break;
				}
			}

			
			for (BillHdr hdr : billhdrlist) {
				String billno = hdr.getBILL();
				List<BillDet> billdetfilter = billdetlist.stream().filter((b)->b.getBILL().equalsIgnoreCase(billno)).collect(Collectors.toList());
				if(billdetfilter.size() > 0) {
					BillPojo billpojo = new BillPojo();
					billpojo.setBillhdr(hdr);
					billpojo.setBilldet(billdetfilter);
					billpojolist.add(billpojo);
				}
			}

			
			for (BillPojo billPojo : billpojolist) {
				double subtotal = 0;
				for (BillDet billdetp : billPojo.getBilldet()) {
					subtotal += Double.valueOf(billdetp.getAmount());
				}

				
				int itemrates = Integer.valueOf(billPojo.getBillhdr().getITEM_RATES());
				String odisc = billPojo.getBillhdr().getORDER_DISCOUNT();
				String odisctype = billPojo.getBillhdr().getORDERDISCOUNTTYPE();
				int odisctax =  Integer.valueOf(billPojo.getBillhdr().getISORDERDISCOUNTTAX());
				String disc = billPojo.getBillhdr().getDISCOUNT();
				String disctype = billPojo.getBillhdr().getDISCOUNT_TYPE();
				int disctax = Integer.valueOf(billPojo.getBillhdr().getISDISCOUNTTAX());
				String shipcost = billPojo.getBillhdr().getSHIPPINGCOST();
				int isshiptax = Integer.valueOf(billPojo.getBillhdr().getISSHIPPINGTAXABLE());
				String adjustment = billPojo.getBillhdr().getADJUSTMENT();
				double taxper = Double.valueOf(billPojo.getBillhdr().getINBOUND_GST());

				
				FinCountryTaxType fintax = finCountryTaxTypeDAO.getCountryTaxTypesByid(Integer.valueOf(billPojo.getBillhdr().getTAXID()));
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
				

				billPojo.getBillhdr().setSUB_TOTAL(String.valueOf(subtotal));
				billPojo.getBillhdr().setTOTAL_AMOUNT(String.valueOf(totalamount));
				billPojo.getBillhdr().setTAXAMOUNT(String.valueOf(taxamount));

				
				double addexpense = 0;
				String shippingcode = billPojo.getBillhdr().getSHIPMENT_CODE();
				String pono = billPojo.getBillhdr().getPONO();
				if(pono.equalsIgnoreCase("") || pono == null || pono.equals(null)) {
				}else {
					if(shippingcode.equalsIgnoreCase("") || shippingcode == null || shippingcode.equals(null)) {
					}else {
						ArrayList  movQryList = _MasterUtil.getExpenseDetailusingponoanddnol(PLANT, pono, shippingcode);
			               if (movQryList.size() > 0) {
				               for(int i =0; i<movQryList.size(); i++) {
				            	   Map arrCustLine = (Map)movQryList.get(i);
				            	   addexpense += (Double.valueOf((String)arrCustLine.get("AMOUNT"))/Double.valueOf((String)arrCustLine.get("CURRENCYTOBASE")))*Double.valueOf((String)arrCustLine.get("BASETOORDERCURRENCY"));
				               }
			               }
					}
				}

				
				double prelandcost = (Double.valueOf(shipcost) + addexpense)/subtotal;
				if(Double.isNaN(prelandcost)) {
					prelandcost = 0.0;
				}
				

				for (BillDet billdetp : billPojo.getBilldet()) {
					billdetp.setLANDED_COST(String.valueOf(prelandcost * Double.valueOf(billdetp.getCOST())));
				}

				
			}

			
			String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();

			
			flag = billSave(PLANT, _login_user, curency, billpojolist);

			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_OUTBOUNDRESULT");
				flag = true;
				result = "<font color=\"green\">Bill Created Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Creating Bill </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("OUTBOUNDRESULT", result);
			result = "<font color=\"red\">" + ThrowableUtil.getMessage(e) + "</font>";
		}

		request.getSession().setAttribute("OUTBOUNDRESULT", result);
		response.sendRedirect("../bill/import?");

	}
	
	public boolean billSave(String plant,String username,String curency,List<BillPojo> billpojolist) {

		//UserTransaction ut = null;
		List<Hashtable<String,String>> billDetInfoList = new ArrayList<Hashtable<String,String>>();
		BillUtil billUtil = new BillUtil();
//		DateUtils dateutils = new DateUtils();
		RecvDetDAO recvDao = new RecvDetDAO();
		MovHisDAO movHisDao = new MovHisDAO();
//		TblControlDAO _TblControlDAO = new TblControlDAO();
		ExpensesDAO expenseDao=new ExpensesDAO();
		CoaDAO coaDao=new CoaDAO();
		BillDAO itemCogsDao=new BillDAO();
		List<CostofgoodsLanded> landedCostLst=new ArrayList<>();
//		List<ItemCogs> lstCogs=new ArrayList<>();
		ItemMstDAO itemmstDao=new ItemMstDAO();
		Costofgoods costofGoods=new CostofgoodsImpl();
		double expenseAmt=0.0;
		boolean isAdded = false;
		String jdesc="";

		
		try{
			//ut = DbBean.getUserTranaction();				
			//ut.begin();
			
			for (BillPojo billhdrdet: billpojolist) {
			

				BillHdr billhdr = billhdrdet.getBillhdr();
				CustUtil custUtil = new CustUtil();

				int trans = billhdr.getTRANSPORTID();
				String transport = Integer.toString(trans);
				
				Hashtable billHdr =new Hashtable(); 
				billHdr.put("PLANT", billhdr.getPLANT());
				billHdr.put("VENDNO", billhdr.getVENDNO());
				billHdr.put("BILL", billhdr.getBILL());
				billHdr.put("PONO", "null".equalsIgnoreCase(billhdr.getPONO()) ? "" :  billhdr.getPONO());
				billHdr.put("GRNO", "null".equalsIgnoreCase(billhdr.getGRNO()) ? "" :  billhdr.getGRNO());
				billHdr.put("BILL_DATE", billhdr.getBILL_DATE());
				billHdr.put("DUE_DATE", billhdr.getDUE_DATE());
				billHdr.put("TRANSPORTID", transport);
				billHdr.put("PAYMENT_TERMS", billhdr.getPAYMENT_TERMS());
				billHdr.put("ORDERTYPE", billhdr.getORDERTYPE());
				billHdr.put("EMPNO", billhdr.getEMPNO());
				billHdr.put("ITEM_RATES", billhdr.getITEM_RATES());
				billHdr.put("DISCOUNT_TYPE", billhdr.getDISCOUNT_TYPE());
				billHdr.put("DISCOUNT_ACCOUNT", billhdr.getDISCOUNT_ACCOUNT());
				billHdr.put("ADJUSTMENT_LABEL", billhdr.getADJUSTMENT_LABEL());
				billHdr.put("ADJUSTMENT", billhdr.getADJUSTMENT());
				billHdr.put("SUB_TOTAL", billhdr.getSUB_TOTAL());
				billHdr.put("TOTAL_AMOUNT", billhdr.getTOTAL_AMOUNT());
				billHdr.put("BILL_STATUS", billhdr.getBILL_STATUS());
				billHdr.put("SHIPMENT_CODE", billhdr.getSHIPMENT_CODE());
				billHdr.put("REFERENCE_NUMBER", billhdr.getREFERENCE_NUMBER());
				billHdr.put("NOTE", billhdr.getNOTE());	
				billHdr.put("CRAT",DateUtils.getDateTime());
				billHdr.put("CRBY",username);
				billHdr.put("UPAT",DateUtils.getDateTime());
				billHdr.put("CREDITNOTESSTATUS","0");
				billHdr.put("TAXAMOUNT", billhdr.getTAXAMOUNT());
				billHdr.put("ISSHIPPINGTAXABLE", billhdr.getISSHIPPINGTAXABLE());
				ArrayList supplier_detail = custUtil.getCustomerDetails((String) billhdr.getSHIPPINGCUSTOMER(), plant);
				billHdr.put("SHIPCONTACTNAME",(String)supplier_detail.get(44));
				billHdr.put("SHIPDESGINATION",(String)supplier_detail.get(45));
				billHdr.put("SHIPWORKPHONE",(String)supplier_detail.get(46));
				billHdr.put("SHIPHPNO",(String)supplier_detail.get(47));
				billHdr.put("SHIPEMAIL",(String)supplier_detail.get(48));
				billHdr.put("SHIPCOUNTRY",(String)supplier_detail.get(49));
				billHdr.put("SHIPADDR1",(String)supplier_detail.get(50));
				billHdr.put("SHIPADDR2",(String)supplier_detail.get(51));
				billHdr.put("SHIPADDR3",(String)supplier_detail.get(52));
				billHdr.put("SHIPADDR4",(String)supplier_detail.get(53));
				billHdr.put("SHIPSTATE",(String)supplier_detail.get(54));
				billHdr.put("SHIPZIP",(String)supplier_detail.get(55));
				if(billhdr.getPURCHASE_LOCATION().equalsIgnoreCase("") || billhdr.getPURCHASE_LOCATION().equalsIgnoreCase(null) || billhdr.getPURCHASE_LOCATION() == null)
					billHdr.put("PURCHASE_LOCATION", "");
				else
					billHdr.put("PURCHASE_LOCATION", billhdr.getPURCHASE_LOCATION());

				
				billHdr.put("TAXTREATMENT",billhdr.getTAXTREATMENT());
				billHdr.put(IDBConstants.REVERSECHARGE, billhdr.getREVERSECHARGE());
				billHdr.put(IDBConstants.GOODSIMPORT, billhdr.getGOODSIMPORT());
				billHdr.put(IDBConstants.CURRENCYID, billhdr.getCURRENCYID());
				billHdr.put("CURRENCYUSEQT",billhdr.getCURRENCYUSEQT());
				billHdr.put("ORDERDISCOUNTTYPE",billhdr.getORDERDISCOUNTTYPE());
				billHdr.put("TAXID",billhdr.getTAXID());
				billHdr.put("ISDISCOUNTTAX",billhdr.getISDISCOUNTTAX());
				billHdr.put("ISORDERDISCOUNTTAX",billhdr.getISORDERDISCOUNTTAX());
				billHdr.put("DISCOUNT", billhdr.getDISCOUNT());
				billHdr.put("SHIPPINGCOST", billhdr.getSHIPPINGCOST());
				billHdr.put("ORDER_DISCOUNT", billhdr.getORDER_DISCOUNT());			
				billHdr.put("INBOUND_GST", billhdr.getINBOUND_GST());
				billHdr.put("PROJECTID", billhdr.getPROJECTID());
				billHdr.put("SHIPPINGID", billhdr.getSHIPPINGID());
				billHdr.put("SHIPPINGCUSTOMER", billhdr.getSHIPPINGCUSTOMER());
				billHdr.put("ORIGIN",billhdr.getORIGIN());
				billHdr.put("DEDUCT_INV",billhdr.getDEDUCT_INV());
				billHdr.put("BILL_TYPE",billhdr.getBILL_TYPE());
			//hdr add
			int billHdrId = billUtil.addBillHdr(billHdr, plant);
			
			
			
			/*BillDet*/
				List item = new ArrayList(), 
					accountName = new ArrayList(), 
					qty = new ArrayList(),
					cost = new ArrayList(), 
					detDiscount = new ArrayList(),
					detDiscountType = new ArrayList(), 
					taxType = new ArrayList(),
					amount = new ArrayList(), 
					polnno = new ArrayList(), 
					landedCost = new ArrayList(), 
					//convcost= new ArrayList(),
					//cost_aod = new ArrayList(), 
//					amount_aod = new ArrayList(),
					loc = new ArrayList(), 
					batch = new ArrayList(),
					uom = new ArrayList(),
					//index = new ArrayList(),
				    ordQty = new ArrayList();
					// delLnno = new ArrayList(),
					// delLnQty = new ArrayList(),
					// delLnLoc = new ArrayList(),
					// delLnBatch = new ArrayList(),
					// delLnUom = new ArrayList(),
					// delLnItem = new ArrayList();
			
			int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
					taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, landedCostCount = 0;//,convcostCount = 0,cost_aodCount=0,amount_aodCount=0;
			int locCount  = 0,batchCount  = 0,uomCount  = 0;//,//  idCount = 0,ordQtyCount = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnQtyCount  = 0,delLnUomCount= 0,
//					delLnnoCount  = 0,
//					delLnItemCount=0;
			String polnnoIn="";
			
			for (BillDet billdetp : billhdrdet.getBilldet()) {
				item.add(itemCount, StrUtils.fString(billdetp.getITEM()).trim());
				itemCount++;
				
				accountName.add(accountNameCount,StrUtils.fString(billdetp.getACCOUNT_NAME()).trim());
				accountNameCount++;
				
				qty.add(qtyCount, StrUtils.fString(billdetp.getQTY()).trim());
				qtyCount++;
				
				cost.add(costCount, StrUtils.fString(billdetp.getCOST()).trim());
				costCount++;
				
				detDiscount.add(detDiscountCount, StrUtils.fString(billdetp.getDISCOUNT()).trim());
				detDiscountCount++;
				
				detDiscountType.add(detDiscountTypeCount, StrUtils.fString(billdetp.getDISCOUNT_TYPE()).trim());
				detDiscountTypeCount++;
				
				taxType.add(taxTypeCount, StrUtils.fString(billdetp.getTax_Type()).trim());
				taxTypeCount++;
				
				amount.add(amountCount, StrUtils.fString(billdetp.getAmount()).trim());
				amountCount++;
				
				polnno.add(polnnoCount, StrUtils.fString(billdetp.getLNNO()).trim());
				polnnoCount++;
				
				landedCost.add(landedCostCount, StrUtils.fString(billdetp.getLANDED_COST()).trim());
				landedCostCount++;
				
				loc.add(locCount, StrUtils.fString(billdetp.getLOC()).trim());
				locCount++;
				
				uom.add(uomCount, StrUtils.fString(billdetp.getUOM()).trim());
				uomCount++;
				
				batch.add(batchCount, StrUtils.fString(billdetp.getBATCH()).trim());
				batchCount++;
			}			
			
			if(billHdrId > 0) {
				if(billhdr.getSHIPMENT_CODE()!=null && !billhdr.getSHIPMENT_CODE().isEmpty() && !billhdr.getSHIPMENT_CODE().equalsIgnoreCase("NULL")) {
				List<String> expensesId=expenseDao.getExpesesHDR(billhdr.getPONO(), plant,billhdr.getSHIPMENT_CODE());
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
//				amount_aod = amount;
				/*if(amount_aod.size()==0) { //check later
					amount_aod = amount;
				}*/
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
					reqObj.setOrderdiscount(Double.parseDouble(billhdr.getORDER_DISCOUNT()));
					reqObj.setDiscount(Double.parseDouble(billhdr.getDISCOUNT()));
					reqObj.setDiscountType(billhdr.getDISCOUNT_TYPE());
					reqObj.setShippingCharge(Double.parseDouble(billhdr.getSHIPPINGCOST()));
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
					reqObj.setOrderdiscount(Double.parseDouble(billhdr.getORDER_DISCOUNT()));
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
						costedExpensesAmt+=Double.parseDouble(billhdr.getSHIPPINGCOST());
						costof.setExpenses_amount(costedExpensesAmt);
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=weight_allocation+cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}

					  costof.setAvg_rate(avg_rate);
					  costof.setBillhdrid(String.valueOf(billHdrId));
					  landedCostLst.add(costof);
					 
					  int cogsCnt=itemCogsDao.addItemCogs(costofGoods.entryProductDetails((String)qty.get(i), (String)item.get(i), plant, avg_rate, billhdr.getDUE_DATE()),plant);
					  System.out.println("Insert ItemCogs Status :"+ cogsCnt);
							
					}
				}
	
				
				String currencyuseqt = billhdr.getCURRENCYUSEQT();
				for(int i =0 ; i < item.size() ; i++){
					int lnno = i+1;
					String convDiscount=""; 
					String convCost = String.valueOf((Float.parseFloat((String) cost.get(i)) / Float.parseFloat(currencyuseqt)));
					/*if(Isconvcost)
						convCost = String.valueOf((Float.parseFloat((String) convcost.get(i)) / Float.parseFloat(currencyuseqt)));*/
					if(!detDiscountType.get(i).toString().contains("%"))
					{
						convDiscount = String.valueOf((Float.parseFloat((String) detDiscount.get(i)) / Float.parseFloat(currencyuseqt)));
					}
					else
						convDiscount = (String) detDiscount.get(i);
					String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
					String convlandedCost = String.valueOf((Float.parseFloat((String) landedCost.get(i)) / Float.parseFloat(currencyuseqt)));
					
					Hashtable<String, String> billDetInfo = new Hashtable<String, String>();
					billDetInfo.put("PLANT", plant);
					billDetInfo.put("LNNO", Integer.toString(lnno));
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
						if(!billhdr.getGRNO().equalsIgnoreCase("")) {
						if(i < (item.size()-1)) {
							polnnoIn += (String) polnno.get(i)+ ",";
						}else {
							polnnoIn += (String) polnno.get(i);
						}		
						}
					}
				}			
				
				//det add		
				isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);

				if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
					BillDAO billDao=new BillDAO();
					for(int i=0;i<landedCostLst.size();i++) {
							int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
							System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
					}
				}
				if(isAdded) {
					System.out.println("billStatus"+billhdr.getBILL_STATUS());
					if(!billhdr.getBILL_STATUS().equalsIgnoreCase("Draft"))
					{
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(billhdr.getBILL_DATE());
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("BILL");
						journalHead.setTRANSACTION_ID(billhdr.getBILL());
						journalHead.setSUB_TOTAL(Double.parseDouble(billhdr.getSUB_TOTAL()));
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
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
							journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) - Double.parseDouble(billhdr.getORDER_DISCOUNT().toString())/billDetInfoList.size());
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
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) billhdr.getVENDNO());
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) billhdr.getVENDNO());
							if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject())
								{
									coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
									jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
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

							//journalDetail_1.setACCOUNT_NAME((String) vendno);
							journalDetail_1.setCREDITS(Double.parseDouble(billhdr.getTOTAL_AMOUNT()));
							journalDetails.add(journalDetail_1);
						}
						
						
						Double taxAmountFrom=Double.parseDouble(billhdr.getTAXAMOUNT());
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							journalDetail_2.setDESCRIPTION(jdesc+"-"+billHdr.get("BILL").toString());
							
							//JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
							//journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							//journalDetail_2.setACCOUNT_NAME("VAT Input");
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
						
						
						
						Double discountFrom = Double.parseDouble(billhdr.getDISCOUNT());
						Double orderDiscountFrom=0.00;
						if(!billhdr.getORDER_DISCOUNT().isEmpty()){
							orderDiscountFrom=Double.parseDouble(billhdr.getORDER_DISCOUNT());
							orderDiscountFrom=(Double.parseDouble(billhdr.getSUB_TOTAL())*orderDiscountFrom)/100;
						}
							
						if(discountFrom>0 || orderDiscountFrom>0)
						{
							if(!billhdr.getDISCOUNT_TYPE().isEmpty())
							{
								if(billhdr.getDISCOUNT_TYPE().equalsIgnoreCase("%"))
								{
									Double subTotalAfterOrderDiscount=Double.parseDouble(billhdr.getSUB_TOTAL())-orderDiscountFrom;
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
						
						if(!billhdr.getSHIPPINGCOST().isEmpty())
						{
							Double shippingCostFrom=Double.parseDouble(billhdr.getSHIPPINGCOST());
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
						if(!billhdr.getADJUSTMENT().isEmpty())
						{
							Double adjustFrom=Double.parseDouble(billhdr.getADJUSTMENT());
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
						if(billhdr.getSHIPMENT_CODE() != "") {
						ArrayList  movQryList = new MasterUtil().getExpenseDetailusingponoanddnol(plant, billhdr.getPONO(), billhdr.getSHIPMENT_CODE());
						if (movQryList.size() > 0) {
							Hashtable ht = new Hashtable();
							ht.put("PLANT",plant);
							ht.put("SHIPMENT_CODE",billhdr.getSHIPMENT_CODE());
							ht.put("PONO",billhdr.getPONO());
							List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
							for(int j=0;j < expenseHdrList.size();j++) {
								Journal journal_2=new Journal();
								Map expenseHdr = (Map) expenseHdrList.get(j);									
								JournalHeader expensepo_journalHead=new JournalHeader();
								expensepo_journalHead.setPLANT(plant);								
								expensepo_journalHead.setJOURNAL_STATUS("PUBLISHED");
								expensepo_journalHead.setJOURNAL_TYPE("Cash");
								expensepo_journalHead.setCURRENCYID(curency);
								expensepo_journalHead.setTRANSACTION_ID((String) expenseHdr.get("ID"));
								expensepo_journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+billhdr.getPONO()+")");
								expensepo_journalHead.setJOURNAL_DATE(billhdr.getBILL_DATE());
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
								
								double expenseTaxAmount = 0.0;
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
										expenseTaxAmount = Double.parseDouble((String)arrCustLine.get("TAXAMOUNT").toString());
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
								
								if(expenseTaxAmount>0)
								{
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									//JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
									//journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									//journalDetail.setACCOUNT_NAME("VAT Input");
									MasterDAO masterDAO = new MasterDAO();
									String planttaxtype = masterDAO.GetTaxType(plant);
									
									if(planttaxtype.equalsIgnoreCase("TAX")) {
										JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail.setACCOUNT_NAME("VAT Input");
									}else if(planttaxtype.equalsIgnoreCase("GST")) {
										JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Receivable");
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail.setACCOUNT_NAME("GST Receivable");
									}else if(planttaxtype.equalsIgnoreCase("VAT")) {
										JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail.setACCOUNT_NAME("VAT Input");
									}else {
										JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
										journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
										journalDetail.setACCOUNT_NAME("VAT Input");
									}
									journalDetail.setCREDITS(expenseTaxAmount);
									journalReversalList.add(journalDetail);
								}
								
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
								
								journal_2.setJournalHeader(expensepo_journalHead);
								journal_2.setJournalDetails(expensepo_journaldetails);
								journalService.addJournal(journal_2, username);
								Hashtable jhtMovHis2 = new Hashtable();
								jhtMovHis2.put(IDBConstants.PLANT, plant);
								jhtMovHis2.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
								jhtMovHis2.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								jhtMovHis2.put(IDBConstants.ITEM, "");
								jhtMovHis2.put(IDBConstants.QTY, "0.0");
								jhtMovHis2.put("RECID", "");
								jhtMovHis2.put(IDBConstants.MOVHIS_ORDNUM,journal_2.getJournalHeader().getTRANSACTION_TYPE()+" "+journal_2.getJournalHeader().getTRANSACTION_ID());
								jhtMovHis2.put(IDBConstants.CREATED_BY, username);		
								jhtMovHis2.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								jhtMovHis2.put("REMARKS","");
								movHisDao.insertIntoMovHis(jhtMovHis2);
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

				if(isAdded && (polnno.size() > 0 && billhdr.getPONO().length() > 0 && !billhdr.getPONO().equalsIgnoreCase("null"))) {
					if(!billhdr.getGRNO().equalsIgnoreCase("")) {
						String query = "SET BILL_STATUS = 'BILLED'";
						String extCond = " AND LNNO IN ("+polnnoIn+") ";
						Hashtable<String, String> htCondition = new Hashtable<String, String>();
						htCondition.put("PONO", billhdr.getPONO());
						htCondition.put("GRNO", billhdr.getGRNO());				
						isAdded = recvDao.update(query, htCondition, extCond, plant);
					}
				}
			}
			
			
			
			
			
			 //Added by Azees for inventory

			if(polnno.size() > 0 && billhdr.getDEDUCT_INV().equalsIgnoreCase("1")) {
				/*for(int i =0 ; i < polnno.size() ; i++){
					int lnno = Integer.parseInt((String)polnno.get(i));
					String ITEM = "",UOM = "", QTY = "", LOC = "", BATCH = "";
					ITEM = (String)item.get(i);
					UOM = (String)uom.get(i);
					QTY = (String)qty.get(i);
					LOC = (String)loc.get(i);
					BATCH = (String)batch.get(i);
					
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
					invmap.put("BILL", billhdr.getBILL());
					invmap.put(IConstants.PODET_PONUM, "");
					invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
					invmap.put(IConstants.VENDOR_CODE, billhdr.getVENDNO());
					invmap.put(IConstants.LOC, LOC);
					invmap.put(IConstants.LOGIN_USER, username);
					invmap.put(IConstants.CUSTOMER_CODE, billhdr.getVENDNO());
					invmap.put(IConstants.BATCH, BATCH);
					invmap.put(IConstants.QTY, QTY);
					invmap.put(IConstants.ORD_QTY, QTY);
					invmap.put(IConstants.RECV_QTY, QTY);
					invmap.put(IConstants.REMARKS, "");
					invmap.put(IConstants.RSNDESC, "");
					if (billhdr.getBILL_DATE().length()>5)
						strTranDate    = billhdr.getBILL_DATE().substring(6)+"-"+ billhdr.getBILL_DATE().substring(3,5)+"-"+billhdr.getBILL_DATE().substring(0,2);
					invmap.put(IConstants.TRAN_DATE, strTranDate);
					invmap.put("RETURN_DATE", strTranDate);
					invmap.put("UOMQTY", uomQty);
					invmap.put("NOTES", "");
					invmap.put("PORETURN", "");
					invmap.put(IConstants.GRNO, "");
					invmap.put(IConstants.CUSTOMER_NAME, billhdr.getVENDNO());
					isAdded = processPodetForReversal(invmap);
					if(isAdded) {
						processInvRemove(invmap);
					}
					if(isAdded) {
						processMovHis_OUT(invmap);
					}					
				}*/
			}
			process: 
			if(isAdded && billhdr.getBILL_STATUS().equalsIgnoreCase("Open") && billhdr.getDEDUCT_INV().equalsIgnoreCase("1")) {
				for(int i =0 ; i < item.size() ; i++){
					int lnno = i+1;
					Hashtable htCondiShipHis = new Hashtable();
					htCondiShipHis.put("PLANT", plant);
					htCondiShipHis.put("GRNO", billhdr.getBILL());
					htCondiShipHis.put("STATUS","C");
					htCondiShipHis.put("BATCH",(String) batch.get(i));
					htCondiShipHis.put("LNNO", Integer.toString(lnno));
					htCondiShipHis.put("LOC", (String) loc.get(i));
					htCondiShipHis.put(IConstants.ITEM, (String) item.get(i));
					boolean isexists = new RecvDetDAO().isExisit(htCondiShipHis, plant);
					
					double costex = Double.valueOf((String) cost.get(i))/Double.valueOf(billhdr.getCURRENCYUSEQT());
					String scostex = String.valueOf(costex);
					if(isexists){
				        String query = "set UNITCOST=" + scostex + ",ORDQTY=" + (String) qty.get(i);
				        isAdded = new RecvDetDAO().update(query,htCondiShipHis,"",plant);
		            }
					
					
					String QTY = "";//, ORDQTY = "";
					boolean processInv = false;
					if(ordQty.size() == 0) {
						QTY = (String) qty.get(i);
//						ORDQTY = (String) qty.get(i);
						processInv = true;
					}else {
						if(ordQty.size()> i) {
//							ORDQTY = (String) qty.get(i);
							double i_OrdQty = Double.parseDouble((String) ordQty.get(i));
							double i_Qty = Double.parseDouble((String) qty.get(i));
							i_Qty = i_Qty - i_OrdQty;							
							QTY = Double.toString(i_Qty);
							if(i_Qty > 0) {
								processInv = true;
							}
						}else {
							QTY = (String) qty.get(i);
//							ORDQTY = (String) qty.get(i);
							processInv = true;
						}
					}
					
				
				if(processInv) { 
//				String ITEM_QTY="";
				Map invmap = new HashMap();
					String ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(plant,(String) item.get(i));
					
					String uomQty="";
					List uomQry = new UomDAO().getUomDetails((String) uom.get(i), plant, "");
					if(uomQry.size()>0) {
						Map m = (Map) uomQry.get(0);
						uomQty = (String)m.get("QPUOM");
					}else {
						uomQty = "0";
					}
					
					String strTranDate ="";
					invmap = new HashMap();
					invmap.put(IConstants.PLANT, plant);
					invmap.put(IConstants.ITEM, (String) item.get(i));
					invmap.put(IConstants.ITEM_DESC, new ItemMstDAO().getItemDesc(plant, (String) item.get(i)));
					invmap.put("BILL", billhdr.getGRNO());
					invmap.put(IConstants.PODET_PONUM, "");
					invmap.put(IConstants.PODET_POLNNO, Integer.toString(lnno));
					invmap.put(IConstants.VENDOR_CODE, billhdr.getVENDNO());
					invmap.put(IConstants.LOC, (String) loc.get(i));
					invmap.put(IConstants.LOGIN_USER, username);
					invmap.put(IConstants.CUSTOMER_CODE, billhdr.getVENDNO());
					invmap.put(IDBConstants.USERFLD4, (String) batch.get(i));
					invmap.put(IConstants.BATCH, (String) batch.get(i));
					invmap.put(IConstants.QTY, QTY);
					invmap.put(IConstants.ORD_QTY, QTY);
					invmap.put(IConstants.RECV_QTY, QTY);
					invmap.put("UOMQTY", uomQty);
					invmap.put(IConstants.REMARKS, "");
					invmap.put(IConstants.RSNDESC, "");
					invmap.put(IDBConstants.CURRENCYID, billhdr.getCURRENCYID());
					invmap.put("UNITCOST", scostex);
					invmap.put(IDBConstants.CREATED_BY, username);
					if (billhdr.getBILL_DATE().length()>5)
						strTranDate    = billhdr.getBILL_DATE().substring(6)+"-"+ billhdr.getBILL_DATE().substring(3,5)+"-"+billhdr.getBILL_DATE().substring(0,2);
					invmap.put(IConstants.TRAN_DATE, strTranDate);
					invmap.put(IConstants.RECVDATE, billhdr.getBILL_DATE());
					invmap.put(IConstants.EXPIREDATE, "");
					invmap.put(IConstants.NONSTKFLAG, ISNONSTKFLG);						
					
					if(!ISNONSTKFLG.equalsIgnoreCase("Y")) {							
							isAdded = processInvMst(invmap);														
					}
					if(isAdded) {
						isAdded = processPodetForReceive(invmap);
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
//	   								String result="";
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
					/*if(isAdded) {
						processMovHis_IN(invmap);
					}*/
					
					if(!isAdded) {
						break process;
					}
					
					if(isAdded) {
//						DateUtils dateUtils = new DateUtils();
						Hashtable htMovhis = new Hashtable();
						htMovhis.clear();
						htMovhis.put(IDBConstants.PLANT, plant);
						htMovhis.put("DIRTYPE", TransactionConstants.ORD_RECV);
						htMovhis.put(IDBConstants.ITEM,(String) item.get(i));
						htMovhis.put("MOVTID", "IN");
						htMovhis.put("RECID", "");
						htMovhis.put(IDBConstants.CUSTOMER_CODE, billhdr.getVENDNO());
						htMovhis.put(IDBConstants.POHDR_JOB_NUM, billhdr.getREFERENCE_NUMBER());
						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, billhdr.getBILL());
						htMovhis.put(IDBConstants.LOC, (String) loc.get(i));
						htMovhis.put(IDBConstants.CREATED_BY, billhdr.getCRBY());
						htMovhis.put("BATNO", (String) batch.get(i));
						htMovhis.put("QTY", QTY);
						htMovhis.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(billhdr.getBILL_DATE()));
						htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htMovhis.put("REMARKS",billhdr.getPONO()+","+billhdr.getGRNO()+","+billhdr.getREFERENCE_NUMBER());
						movHisDao.insertIntoMovHis(htMovhis);
					}
				}
			}
		}
			
		if(isAdded) {
			for(int i =0 ; i < item.size() ; i++){
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_BILL);
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billhdr.getBILL_DATE()));														
				htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
				String billqty = String.valueOf((String) qty.get(i));
				htMovHis.put(IDBConstants.QTY, billqty);
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, billhdr.getBILL());
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS",billhdr.getPONO()+","+billhdr.getGRNO()+","+billhdr.getREFERENCE_NUMBER());
				isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
			}
		}
			
			//GRN to Bill Status Update
			if(isAdded) {
				if(billhdr.getGRNO().length()>0&&billhdr.getPONO().length()>0)
				{
				BillDAO billDao = new BillDAO();
				Hashtable<String, String> htCondition = new Hashtable<String, String>();
				htCondition.put("PONO", billhdr.getPONO());
				htCondition.put("GRNO", billhdr.getGRNO());					
				String sql = "SELECT ISNULL(AMOUNT,0) AMOUNT FROM " + plant +"_FINGRNOTOBILL A WHERE A.PLANT='"+ plant+"'";					
				List arrList = billDao.selectForReport(sql, htCondition, "");
				Map grnbill=(Map)arrList.get(0);
				float grnamt=Float.parseFloat((String) grnbill.get("AMOUNT"));
				
				sql = "SELECT ISNULL(SUM(AMOUNT),0) AMOUNT FROM " + plant +"_FINBILLDET B JOIN " + plant +"_FINBILLHDR A on A.ID=B.BILLHDRID WHERE A.PLANT='"+ plant+"'";					
				List billarrList = billDao.selectForReport(sql, htCondition, "");
				Map billdet=(Map)billarrList.get(0);
				float billamt=Float.parseFloat((String) billdet.get("AMOUNT"));
				if(grnamt<=billamt)
					isAdded = billDao.updateGrntoBill("SET STATUS='BILLED' ", htCondition, "", plant);
				else
					isAdded = billDao.updateGrntoBill("SET STATUS='PARTIALLY BILLED' ", htCondition, "", plant);
				}
				if(billhdr.getSHIPMENT_CODE().length()>0) {
					ExpensesDAO expDao = new ExpensesDAO();
					String query = " SET STATUS='BILLED' ";
					Hashtable<String, String> htCondition = new Hashtable<String, String>();
					htCondition.put("PONO", billhdr.getPONO());
					htCondition.put("SHIPMENT_CODE", billhdr.getSHIPMENT_CODE());
					htCondition.put("PLANT", plant);
					int count = expDao.update(query, htCondition, "");
					if(count > 0) {
						isAdded = true;
					}else {
						isAdded = false;
					}						
				}
			}
			 //Added by Abhilash to handle COGS 
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

			// Added by Abhilash to handle COGS 
		}
			
			if(isAdded) {
				//DbBean.CommitTran(ut);
				return true;
			}

			else {
				//DbBean.RollbackTran(ut);
				return false;
			}				
			
		}catch (Exception e) {
			 //DbBean.RollbackTran(ut);
			 e.printStackTrace();
			 return false;
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

	public void billPDFGenration(HttpServletRequest request, HttpServletResponse response, String billNo,
			int billHdrId) throws IOException{
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (ajax) {
			HttpSession session = ((HttpServletRequest) request).getSession(false);
			String rootURI = HttpUtils.getRootURI(request);
			String billDetailURL = rootURI + "/jsp/billDetail.jsp?BILL_HDR=" + billHdrId + "&PLANT="
					+ StrUtils.fString((String) session.getAttribute("PLANT")) + "&SYSTEMNOW="
					+ StrUtils.fString((String) session.getAttribute("SYSTEMNOW")) + "&BASE_CURRENCY="
					+ StrUtils.fString((String) session.getAttribute("BASE_CURRENCY")) + "&LOGIN_USER="
					+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&NOOFPAYMENT="
					+ StrUtils.fString((String) session.getAttribute("NOOFPAYMENT")) + "&LOGIN_USER="
					+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) + "&INTERNAL_REQUESET=TRUE";
			URL url = new URL(billDetailURL);
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
				barcode = BarcodeFactory.createCode128(billNo);
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
				PdfUtil.createPDF(sbData.toString(), DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Bill_" + billNo + ".pdf", rootURI, PageSize.A4, PageSize.A4.getWidth());
			} catch (BarcodeException e) {
				e.printStackTrace();
			} catch (OutputException e) {
				e.printStackTrace();
			}
		}

	}
	
}
