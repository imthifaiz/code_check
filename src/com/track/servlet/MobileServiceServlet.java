package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import java.util.Vector;

import javax.servlet.RequestDispatcher;
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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.SalesAttachDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.StoreDetDAO;
import com.track.dao.StoreHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.StoreDet;
import com.track.db.object.StoreHdr;
import com.track.db.object.ToDet;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.service.Costofgoods;
import com.track.service.DoDetService;
import com.track.service.DoHDRService;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.DoDetServiceImpl;
import com.track.serviceImplementation.DoHdrServiceImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({"unchecked", "rawtypes"})
@WebServlet("/MobileService/*")
public class MobileServiceServlet  extends DynamicFileServlet implements IMLogger{
	
    @SuppressWarnings("unused")
	private DeleveryOrderServlet mDOClass;
    private PurchaseOrderServlet mPOClass;


	private static final long serialVersionUID = 1L;
	String autoinverr="";
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		if (action.equalsIgnoreCase("SendPurchaseApprove")) {

			JSONObject jsonObjectResult = new JSONObject();
			String CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
			String orderno = StrUtils.fString(request.getParameter("PO")).trim();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String PTYPE = StrUtils.fString(request.getParameter("PTYPE")).trim();
			try {
				ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
				Map map = (Map) al.get(0);
				String PLNTDESC = (String) map.get("PLNTDESC");
				List<String> attachmentLocations = new ArrayList<>();
				String attachmentLocation = null;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("PONO", orderno);
				if (new PoHdrDAO().isExisit(ht)) {
					
					ArrayList list =new CustomerBeanDAO().getVendorDetailsForPO(plant,orderno);					       
					if (list.size() > 0) { 
			        	   CUSTNAME=(String)list.get(1);
				           CONTACTNAME = (String)list.get(8);
				           EMAIL=(String)list.get(12);
					}				
					request.setAttribute("PONO", orderno);
					request.setAttribute("PLANT", plant);
					request.setAttribute("ISAUTOMAIL", "1");
					EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
					Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
					// TODO: Send proper value in action parameter
					String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
					if ("invoice".equals(sendAttachment)) {
						mPOClass = new PurchaseOrderServlet();
						String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
							mPOClass.viewPOReport(request, response, "printPOInvoiceKitchen");
							attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals("printPOInvoiceKitchen") ? "/Receiving_List_" : "/Invoice_") + orderno + ".pdf";
						} else {
							mPOClass.viewPOReport(request, response, "printPOInvoice");
							attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals("printPOInvoice") ? "/Receiving_List_" : "/Invoice_") + orderno + ".pdf";
						}
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					
					String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.PURCHASE_ORDER);
					if (isAutoEmail.equalsIgnoreCase("Y"))
						new EmailMsgUtil().sendEmailTo(plant, "", IConstants.PURCHASE_ORDER,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,orderno,"",PTYPE);
					
					
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
		if (action.equalsIgnoreCase("SendSalesApprove")) {//Azees-09.22 for SalesApprove 
			
			JSONObject jsonObjectResult = new JSONObject();
			String CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
			String orderno = StrUtils.fString(request.getParameter("PO")).trim();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String PTYPE = StrUtils.fString(request.getParameter("PTYPE")).trim();
			try {
				ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
				Map map = (Map) al.get(0);
				String PLNTDESC = (String) map.get("PLNTDESC");
				List<String> attachmentLocations = new ArrayList<>();
				String attachmentLocation = null;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("DONO", orderno);
				if (new DoHdrDAO().isExisit(ht)) {
					
					ArrayList list =new CustomerBeanDAO().getCustomerDetailsForDO(orderno,plant);					       
					if (list.size() > 0) { 
						CUSTNAME=(String)list.get(1);
						CONTACTNAME = (String)list.get(9);
						EMAIL=(String)list.get(14);
					}				
					request.setAttribute("DONO", orderno);
					request.setAttribute("PLANT", plant);
					request.setAttribute("ISAUTOMAIL", "1");
					EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
					Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
					// TODO: Send proper value in action parameter
					String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
					if ("invoice".equals(sendAttachment)) {
						mDOClass = new DeleveryOrderServlet();
						//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
							mDOClass.viewInvoiceReport(request, response);
							attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + orderno + ".pdf";
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					
					String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.SALES_ORDER);
					if (isAutoEmail.equalsIgnoreCase("Y"))
						new EmailMsgUtil().sendEmailTo(plant, "", IConstants.SALES_ORDER,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,orderno,"",PTYPE);
					
					
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
		if (action.equalsIgnoreCase("SendSalesInvoiceAndDo")) {
			JSONObject jsonObjectResult = new JSONObject();
			String DONO="",GINO="",CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
			String orderno = StrUtils.fString(request.getParameter("INVOICE")).trim();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			try {
				//DoHDRService doHDRService = new DoHdrServiceImpl();
				//DoDetService doDetService = new DoDetServiceImpl();
				ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
				Map map = (Map) al.get(0);
				String PLNTDESC = (String) map.get("PLNTDESC");
				List<String> attachmentLocations = new ArrayList<>();
				String attachmentLocation = null;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("INVOICE", orderno);
				if (new InvoiceDAO().isExisit(ht)) {
					
					ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.INVOICE, orderno);
					ArrayList listship =new ShipHisDAO().selectShipHisbyjoin(" ISNULL(INVOICENO,'') GINO,ISNULL(S.DONO,'') DONO,ISNULL(SIGNATURENAME,'') SIGNATURENAME,ISNULL(SIGNATUREEMAIL,'') SIGNATUREEMAIL ", plant+"_FININVOICEHDR A JOIN "+plant+"_SHIPHIS S ON S.INVOICENO=A.GINO ",ht,"");
					for(int j=0;j<listship.size();j++)
			        {
						 Map ms = (Map)listship.get(0);
						 CUSTNAME=(String)ms.get("SIGNATURENAME");
						 EMAIL=(String)ms.get("SIGNATUREEMAIL");
						 DONO = (String)ms.get("DONO");
						 GINO = (String)ms.get("GINO");
						 
					ArrayList list =new DoHdrDAO().getOutBoundOrderCustamerDetailsByWMS(plant,DONO);
				       
				       for(int i=0;i<list.size();i++)
				        {
				          Map m = (Map)list.get(i);
				           if(CUSTNAME==null||"".equals(CUSTNAME))
				        	   CUSTNAME=(String)m.get("custname");
				           CONTACTNAME = (String)m.get("contactname");
				           TELNO=(String)m.get("telno");
				           if(EMAIL==null||"".equals(EMAIL))
				           EMAIL=(String)m.get("email");
				        }
			        }
					
					request.setAttribute("INVOICENO", GINO);
					request.setAttribute("DONO", DONO);
					request.setAttribute("PLANT", plant);
					request.setAttribute("printwithigino", "1");
					request.setAttribute("ISAUTOMAIL", "1");
					EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
					Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.DELIVERY);
					// TODO: Send proper value in action parameter
					String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
					if ("both".equals(sendAttachment) || "Do".equals(sendAttachment)) {
						viewDOReport(request, response,"printDOWITHOUTBATCH");
						attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/DO_" + StrUtils.fString(DONO) + ".pdf";
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
						//viewInvoiceReport(request, response);
						mDOClass = new DeleveryOrderServlet();
						mDOClass.printInvoiceReport(request, response);
						attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(orderno).trim() + ".pdf";
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					
					
					String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.DELIVERY);
					if (isAutoEmail.equalsIgnoreCase("Y"))
						new EmailMsgUtil().sendEmailTo(plant, orderno, IConstants.DELIVERY,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,DONO,GINO,"");
					
					//DoHdr doHdr = new DoHdr();
					//List<DoDet> doDetList = new ArrayList<DoDet>();
					//doHdr = doHDRService.getDoHdrById(plant, orderno);
					//doDetList = doDetService.getDoDetById(plant, orderno);
					
					//jsonObjectResult.put("doHdr", doHdr.toString());
					//jsonObjectResult.put("doDet", doDetList.toString());
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
	
	
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
			if (action.equalsIgnoreCase("SendPurchaseApprove")) {

				JSONObject jsonObjectResult = new JSONObject();
				String CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
				String orderno = StrUtils.fString(request.getParameter("PO")).trim();
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
				String PTYPE = StrUtils.fString(request.getParameter("PTYPE")).trim();
				try {
					ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
					Map map = (Map) al.get(0);
					String PLNTDESC = (String) map.get("PLNTDESC");
					List<String> attachmentLocations = new ArrayList<>();
					String attachmentLocation = null;
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put("PONO", orderno);
					if (new PoHdrDAO().isExisit(ht)) {
						
						ArrayList list =new CustomerBeanDAO().getVendorDetailsForPO(plant,orderno);					       
						if (list.size() > 0) { 
				        	   CUSTNAME=(String)list.get(1);
					           CONTACTNAME = (String)list.get(8);
					           EMAIL=(String)list.get(12);
						}				
						request.setAttribute("PONO", orderno);
						request.setAttribute("PLANT", plant);
						request.setAttribute("ISAUTOMAIL", "1");
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
						// TODO: Send proper value in action parameter
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						if ("invoice".equals(sendAttachment)) {
							mPOClass = new PurchaseOrderServlet();
							String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
								mPOClass.viewPOReport(request, response, "printPOInvoiceKitchen");
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals("printPOInvoiceKitchen") ? "/Receiving_List_" : "/Invoice_") + orderno + ".pdf";
							} else {
								mPOClass.viewPOReport(request, response, "printPOInvoice");
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals("printPOInvoice") ? "/Receiving_List_" : "/Invoice_") + orderno + ".pdf";
							}
							if (attachmentLocation != null) {
								attachmentLocations.add(attachmentLocation);
							}
						}
						
						String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.PURCHASE_ORDER);
						if (isAutoEmail.equalsIgnoreCase("Y"))
							new EmailMsgUtil().sendEmailTo(plant, "", IConstants.PURCHASE_ORDER,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,orderno,"",PTYPE);
						
						
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
			if (action.equalsIgnoreCase("SendSalesApprove")) {//Azees-09.22 for SalesApprove 
				
				JSONObject jsonObjectResult = new JSONObject();
				String CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
				String orderno = StrUtils.fString(request.getParameter("PO")).trim();
				String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
				String PTYPE = StrUtils.fString(request.getParameter("PTYPE")).trim();
				try {
					ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
					Map map = (Map) al.get(0);
					String PLNTDESC = (String) map.get("PLNTDESC");
					List<String> attachmentLocations = new ArrayList<>();
					String attachmentLocation = null;
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put("DONO", orderno);
					if (new DoHdrDAO().isExisit(ht)) {
						
						ArrayList list =new CustomerBeanDAO().getCustomerDetailsForDO(orderno,plant);					       
						if (list.size() > 0) { 
							CUSTNAME=(String)list.get(1);
							CONTACTNAME = (String)list.get(9);
							EMAIL=(String)list.get(14);
						}				
						request.setAttribute("DONO", orderno);
						request.setAttribute("PLANT", plant);
						request.setAttribute("ISAUTOMAIL", "1");
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
						// TODO: Send proper value in action parameter
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						if ("invoice".equals(sendAttachment)) {
							mDOClass = new DeleveryOrderServlet();
							//String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
								mDOClass.viewInvoiceReport(request, response);
								attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + orderno + ".pdf";
							if (attachmentLocation != null) {
								attachmentLocations.add(attachmentLocation);
							}
						}
						
						String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.SALES_ORDER);
						if (isAutoEmail.equalsIgnoreCase("Y"))
							new EmailMsgUtil().sendEmailTo(plant, "", IConstants.SALES_ORDER,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,orderno,"",PTYPE);
						
						
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
			if (action.equalsIgnoreCase("SendSalesInvoiceAndDo")) {
			JSONObject jsonObjectResult = new JSONObject();
			String DONO="",GINO="",PDASIGNPATH="",CUSTNAME="",CONTACTNAME="",TELNO="",EMAIL="";
			String orderno = StrUtils.fString(request.getParameter("INVOICE")).trim();
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			try {
				//DoHDRService doHDRService = new DoHdrServiceImpl();
				//DoDetService doDetService = new DoDetServiceImpl();
				ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
				Map map = (Map) al.get(0);
				String PLNTDESC = (String) map.get("PLNTDESC");
				List<String> attachmentLocations = new ArrayList<>();
				String attachmentLocation = null;
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("INVOICE", orderno);
				if (new InvoiceDAO().isExisit(ht)) {
					
					ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.INVOICE, orderno);
					ArrayList listship =new ShipHisDAO().selectShipHisbyjoin(" ISNULL(INVOICENO,'') GINO,ISNULL(S.DONO,'') DONO,ISNULL(SIGNATURENAME,'') SIGNATURENAME,isnull(S.PDASIGNPATH, '') as PDASIGNPATH,ISNULL(SIGNATUREEMAIL,'') SIGNATUREEMAIL ", plant+"_FININVOICEHDR A JOIN "+plant+"_SHIPHIS S ON S.INVOICENO=A.GINO ",ht,"");
					for(int j=0;j<listship.size();j++)
			        {
						 Map ms = (Map)listship.get(0);
						 CUSTNAME=(String)ms.get("SIGNATURENAME");
						 EMAIL=(String)ms.get("SIGNATUREEMAIL");
						 DONO = (String)ms.get("DONO");
						 GINO = (String)ms.get("GINO");
						 PDASIGNPATH = (String)ms.get("PDASIGNPATH"); //to display digitalsign -imti on 26-04-2022
						 
					ArrayList list =new DoHdrDAO().getOutBoundOrderCustamerDetailsByWMS(plant,DONO);
				       
				       for(int i=0;i<list.size();i++)
				        {
				          Map m = (Map)list.get(i);
				           if(CUSTNAME==null||"".equals(CUSTNAME))
				        	   CUSTNAME=(String)m.get("custname");
				           CONTACTNAME = (String)m.get("contactname");
				           TELNO=(String)m.get("telno");
				           if(EMAIL==null||"".equals(EMAIL))
				           EMAIL=(String)m.get("email");
//				           EMAIL="xyz@gmail.com";
				        }
			        }
					
					request.setAttribute("INVOICENO", GINO);
					request.setAttribute("DONO", DONO);
					request.setAttribute("PLANT", plant);
					request.setAttribute("PDASIGN", PDASIGNPATH);
					request.setAttribute("printwithigino", "1");
					request.setAttribute("ISAUTOMAIL", "1");
					EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
					Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.DELIVERY);
					// TODO: Send proper value in action parameter
					String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
					if ("both".equals(sendAttachment) || "Do".equals(sendAttachment)) {
						viewDOReport(request, response,"printDOWITHOUTBATCH");
						attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/DO_" + StrUtils.fString(DONO) + ".pdf";
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
						//viewInvoiceReport(request, response);
						mDOClass = new DeleveryOrderServlet();
						mDOClass.printInvoiceReport(request, response);
						attachmentLocation = DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + StrUtils.fString(orderno).trim() + ".pdf";
						if (attachmentLocation != null) {
							attachmentLocations.add(attachmentLocation);
						}
					}
					
					
					String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,IConstants.DELIVERY);
					if (isAutoEmail.equalsIgnoreCase("Y"))
						new EmailMsgUtil().sendEmailTo(plant, orderno, IConstants.DELIVERY,EMAIL,CUSTNAME,attachmentLocations,PLNTDESC,DONO,GINO,"");
					
					//DoHdr doHdr = new DoHdr();
					//List<DoDet> doDetList = new ArrayList<DoDet>();
					//doHdr = doHDRService.getDoHdrById(plant, orderno);
					//doDetList = doDetService.getDoDetById(plant, orderno);
					
					//jsonObjectResult.put("doHdr", doHdr.toString());
					//jsonObjectResult.put("doDet", doDetList.toString());
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
