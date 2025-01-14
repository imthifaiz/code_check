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
import java.util.UUID;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.beanutils.converters.StringArrayConverter;
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
import com.track.dao.DoDetApprovalDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrAprrovalDAO;
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
import com.track.dao.LocMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PltApprovalMatrixDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SalesAttachDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.StockTakeDAO;
import com.track.dao.StoreDetDAO;
import com.track.dao.StoreHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.dao.UomDAO;
import com.track.db.object.DoDet;
import com.track.db.object.DoDetApproval;
import com.track.db.object.DoDetRemarks;
import com.track.db.object.DoHdr;
import com.track.db.object.DoHdrApproval;
import com.track.db.object.DodetApprovalRemarks;
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
import com.track.db.util.ExpensesUtil;
import com.track.db.util.FieldCopier;
import com.track.db.util.HTReportUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
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
@WebServlet("/salesorder/*")
public class SalesOrderServlet extends DeleveryOrderServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	String autoinverr="";
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();		
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		DoHdrAprrovalDAO doHdrAprrovalDAO = new DoHdrAprrovalDAO();
		DoDetApprovalDAO doDetApprovalDAO = new DoDetApprovalDAO();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit")
				|| action.equalsIgnoreCase("convertToSales")) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String dono = "", orderType = "", delDate = "", custCode = "", custName = "", jobNum = "",
					personInCharge = "", contactNum = "", address = "", address2 = "", address3 = "", 
					collectionTime = "", remark1 = "", remark2 = "", shippingId = "", shippingCustomer = "",
					currencyId = "", deliveryDate = "", timeSlots = "", outbound_gst = "", status_id = "",
					empName="",empno = "", remark3 = "", orderDiscount = "", shippingCost = "",
					incoterms = "", paymentType = "", deliverydateFormat = "0", approveStatus = "",
					sales_location = "", taxTreatment = "", currencyUseQty = "", orderstatus = "",custorderstatus = "",
					discount = "", discount_type = "", adjustment = "", itemRates = "",projectid="0",transportid="0",paymentterms="",
					orderdiscstatus = "0",discstatus = "0",shipstatus = "0",taxid = "",orderdisctype = "",shipcontactname="",
					shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",shipworkphone="",
					shipcountry="",shiphpno="",shipemail="";
//			String collectionDate = "",estno = "";
			String oldDono="";
			List<Hashtable<String,String>> salesAttachmentList = null;
			List<Hashtable<String,String>> salesAttachmentInfoList = null;
			/*DoDet*/
			List item = new ArrayList(), unitPrice = new ArrayList(), qtyOr = new ArrayList(), qtyIs = new ArrayList(), qtyEst = new ArrayList(),
					unitMo = new ArrayList(), 
					prdDeliveryDate = new ArrayList(), accountName = new ArrayList(),
					taxType = new ArrayList(), item_discount = new ArrayList(),
					item_discountType = new ArrayList(), chkdDONO = new ArrayList(), elnno = new ArrayList(),
					addonAmount = new ArrayList(),addonType= new ArrayList(),unitcost= new ArrayList();
			
//			List proGst = new ArrayList(),
			int chkdDONOCount = 0,taxTypeCount = 0;
			/*int itemCount = 0, unitPriceCount = 0, qtyOrCount = 0, qtyIsCount = 0, qtyEstCount = 0, unitMoCount = 0, 
					proGstCount = 0, prdDeliveryDateCount = 0, accountNameCount = 0,
					item_discountCount = 0, item_discountTypeCount = 0,
					elnnoCount = 0;*/
			salesAttachmentList = new ArrayList<Hashtable<String,String>>();
			salesAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			SalesAttachDAO salesAttachDAO = new SalesAttachDAO();
			DoHDRService doHDRService = new DoHdrServiceImpl(); 
			DoDetService doDetService = new DoDetServiceImpl();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
				List<DoDet> DoDetList = new ArrayList();
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					DoHdr doHdr = new DoHdr();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* DOHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("DONO")) {
								dono = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								orderType = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								delDate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODE")) {
								custCode = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CUST_NAME")) {
								custName = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								jobNum = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PERSON_INCHARGE")) {
								personInCharge = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD1")) {
								address = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD2")) {
								address2 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD3")) {
								address3 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								collectionTime = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								remark1 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								remark2 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								remark3 = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								shippingId = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								shippingCustomer = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyId = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELIVERYDATE")) {
								deliveryDate = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								outbound_gst = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								
								empno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empName = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderdiscount")) {
								orderDiscount = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								shippingCost = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								incoterms = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PAYMENTTYPE")) {
								paymentType = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								deliverydateFormat = StrUtils.fString(fileItem.getString()).trim();
								
								deliverydateFormat = (deliverydateFormat.equals("")) ? "0" : "1";							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
								sales_location = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxTreatment = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								orderstatus = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("custordersataus")) {
								custorderstatus = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								discount = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								discount_type = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								adjustment = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								itemRates = StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("PAYMENT_TERMS")) {
								paymentterms = StrUtils.fString(fileItem.getString()).trim();
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
							
							/* DODET */
							
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("unitprice")) {
								unitPrice .add(StrUtils.fString(fileItem.getString()).trim());
//								unitPriceCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTY")) {
								qtyOr .add(StrUtils.fString(fileItem.getString()).trim());
//								qtyOrCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("UOM")) {
								unitMo .add(StrUtils.fString(fileItem.getString()).trim());
//								unitMoCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyUseQty = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PRODUCTDELIVERYDATE")) {
								prdDeliveryDate.add(StrUtils.fString(fileItem.getString()).trim());
//								prdDeliveryDateCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accountName.add(StrUtils.fString(fileItem.getString()).trim());
//								accountNameCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
								if (fileItem.getFieldName().equalsIgnoreCase("tax")) {
									/*
									 * if(fileItem.getString().equalsIgnoreCase("EXEMPT") ||
									 * fileItem.getString().equalsIgnoreCase("OUT OF SCOPE"))
									 * taxType.add(taxTypeCount,
									 * StrUtils.fString(fileItem.getString()+"[0.0%]").trim()); else
									 * taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
									 * taxTypeCount++;
									 */
									taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
									taxTypeCount++;
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("addonprice")) {
								addonAmount.add(StrUtils.fString(fileItem.getString()).trim());

							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("addontype")) {
								addonType.add(StrUtils.fString(fileItem.getString()).trim());

							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("unitcost")) {
								unitcost.add(StrUtils.fString(fileItem.getString()).trim());

							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_discount")) {
								item_discount.add(StrUtils.fString(fileItem.getString()).trim());
//								item_discountCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("item_discounttype")) {
								item_discountType.add(StrUtils.fString(fileItem.getString()).trim());
//								item_discountTypeCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("chkdDONO")) {
								chkdDONO.add(StrUtils.fString(fileItem.getString()).trim());
								chkdDONOCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								elnno.add(StrUtils.fString(fileItem.getString()).trim());
//								elnnoCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTYIS")) {
								qtyIs.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyIsCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ESTQTY")) {
								qtyEst.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyEstCount++;
							}
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/SALES" + "/"+ dono;
								String filetempLocationATT = "C:/ATTACHMENTS/SALES" + "/temp" + "/"+ dono;
								String fileName = StrUtils.fString(fileItem.getName()).trim();
								fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
								
								File path = new File(fileLocationATT);
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
								File tempPath = new File(filetempLocationATT);
								if (tempPath.exists()) {
									File tempUploadedfile = new File(tempPath + "/"+ fileName);
									if (tempUploadedfile.exists()) {
										tempUploadedfile.delete();
									}
								}
								Hashtable salesAttachment = new Hashtable<String, String>();
								salesAttachment.put("PLANT", plant);
								salesAttachment.put("FILETYPE", fileItem.getContentType());
								salesAttachment.put("FILENAME", fileName);
								salesAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								salesAttachment.put("FILEPATH", fileLocationATT);
								salesAttachment.put("CRAT",DateUtils.getDateTime());
								salesAttachment.put("CRBY",username);
								salesAttachment.put("UPAT",DateUtils.getDateTime());
								salesAttachmentList.add(salesAttachment);
							}
						}
					}

					if (!orderdisctype.equalsIgnoreCase("%")) {
						discount = String.valueOf(Double.parseDouble(discount) / Double.parseDouble(currencyUseQty));
					}

					if (!discount_type.equalsIgnoreCase("%")) {
						orderDiscount = String
								.valueOf(Double.parseDouble(orderDiscount) / Double.parseDouble(currencyUseQty));
					}
					shippingCost = String
							.valueOf(Double.parseDouble(shippingCost) / Double.parseDouble(currencyUseQty));
					adjustment = String.valueOf(Double.parseDouble(adjustment) / Double.parseDouble(currencyUseQty));

					// Get Employee Code by Name
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
					
					DoHdr dohdrbydono = null;
					// get dono status
					if(action.equalsIgnoreCase("edit")) {
					dohdrbydono = doHDRService.getDoHdrById(plant, dono);
					}

					doHdr.setPLANT(plant);
					doHdr.setDONO(dono);
					doHdr.setORDERTYPE(orderType);
					doHdr.setDELDATE(delDate);
					doHdr.setSTATUS("N");
					doHdr.setPickStaus("N");
					doHdr.setCRAT(DateUtils.getDateTime());
					doHdr.setCRBY(username);
					doHdr.setCustCode(custCode);
					doHdr.setCustName(custName);
					doHdr.setJobNum(jobNum);
					doHdr.setPersonInCharge(personInCharge);
					doHdr.setContactNum(contactNum);
					doHdr.setAddress(address);
					doHdr.setAddress2(address2);
					doHdr.setAddress3(address3);
					//doHdr.setCollectionDate(DateUtils.getDate());--Previous Date Order Issue
					doHdr.setCollectionDate(delDate);
					doHdr.setCollectionTime(collectionTime);
					doHdr.setRemark1(remark1);
					doHdr.setRemark2(remark2);
					doHdr.setRemark3(remark3);
					doHdr.setSHIPPINGID(shippingId);
					doHdr.setSHIPPINGCUSTOMER(shippingCustomer);
					doHdr.setCURRENCYID(currencyId);
					doHdr.setDELIVERYDATE(deliveryDate);
					doHdr.setTIMESLOTS(timeSlots);
					doHdr.setOUTBOUND_GST(Double.parseDouble(outbound_gst));
					doHdr.setSTATUS_ID(status_id);
					doHdr.setEMPNO(empno);
					if (action.equalsIgnoreCase("convertToSales")) {
						if (jobNum.contains("SE")) {
							doHdr.setESTNO(jobNum);
						}
					} else {
						if (!jobNum.contains("SE"))
							doHdr.setESTNO("");
						else
							doHdr.setESTNO(jobNum);
					}
					doHdr.setORDERDISCOUNT(Double.parseDouble(orderDiscount));
					doHdr.setSHIPPINGCOST(Double.parseDouble(shippingCost));
					doHdr.setINCOTERMS(incoterms);
					doHdr.setPAYMENTTYPE(paymentType);
					if(orderType.equalsIgnoreCase("POS")) {
						ArrayList paymodebydono = new ArrayList();
						paymodebydono=new HTReportUtil().getPaymentModeByDono(dono, plant);
						if(paymodebydono.size() == 1) {
							new HTReportUtil().updatePaymodebydono(plant, paymentType, dono);
						}
					}
					doHdr.setDELIVERYDATEFORMAT(Short.parseShort(deliverydateFormat));
					doHdr.setAPPROVESTATUS(approveStatus);
					doHdr.setSALES_LOCATION(sales_location);
					doHdr.setTAXTREATMENT(taxTreatment);
					doHdr.setORDER_STATUS(orderstatus);
					//doHdr.setAPP_CUST_ORDER_STATUS(custorderstatus);//Sales Approve 08.22 - Azees
					doHdr.setDISCOUNT(Double.parseDouble(discount));
					doHdr.setDISCOUNT_TYPE(discount_type);
					doHdr.setADJUSTMENT(Double.parseDouble(adjustment));
					doHdr.setITEM_RATES(Short.parseShort(itemRates));
					doHdr.setCURRENCYUSEQT(Double.valueOf(currencyUseQty));
					doHdr.setORDERDISCOUNTTYPE(orderdisctype);
					doHdr.setTAXID(Integer.valueOf(taxid));
					doHdr.setISDISCOUNTTAX(Short.parseShort(discstatus));
					doHdr.setISORDERDISCOUNTTAX(Short.parseShort(orderdiscstatus));
					doHdr.setISSHIPPINGTAX(Short.parseShort(shipstatus));
					if (transportid.length() > 0) {
						doHdr.setTRANSPORTID(Short.parseShort(transportid));
					} else {
						doHdr.setTRANSPORTID(Integer.valueOf("0"));
					}
					doHdr.setPAYMENT_TERMS(paymentterms);
					if (projectid.length() > 0) {
						doHdr.setPROJECTID(Integer.valueOf(projectid));
					} else {
						doHdr.setPROJECTID(Integer.valueOf("0"));
					}
					doHdr.setSHIPCONTACTNAME(shipcontactname);
					doHdr.setSHIPDESGINATION(shipdesgination);
					doHdr.setSHIPWORKPHONE(shipworkphone);
					doHdr.setSHIPHPNO(shiphpno);
					doHdr.setSHIPEMAIL(shipemail);
					doHdr.setSHIPCOUNTRY(shipcountry);
					doHdr.setSHIPADDR1(shipaddr1);
					doHdr.setSHIPADDR2(shipaddr2);
					doHdr.setSHIPADDR3(shipaddr3);
					doHdr.setSHIPADDR4(shipaddr4);
					doHdr.setSHIPSTATE(shipstate);
					doHdr.setSHIPZIP(shipzip);
					String uniqueID = UUID.randomUUID().toString();//Sales Approve 08.22 - Azees
					if (action.equalsIgnoreCase("edit")) {
						DoHdr olddohdr = new DoHdrDAO().getDoHdrById(plant, doHdr.getDONO());//Sales Approve 08.22 - Azees
						if(doHdr.getORDER_STATUS().equalsIgnoreCase("APPROVAL PENDING")) {
							doHdr.setORDER_STATUS(olddohdr.getORDER_STATUS());
							doHdr.setAPP_CUST_ORDER_STATUS("EDIT APPROVAL PENDING");
							doHdr.setUKEY(uniqueID);
						}
						else  {
							doHdr.setORDER_STATUS("Draft");
							if(olddohdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase(""))
								doHdr.setAPP_CUST_ORDER_STATUS("");
						}
						if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
							DoHdrApproval doHdrApproval= new DoHdrApproval();
							new FieldCopier().copyFields(olddohdr, doHdrApproval);
							doHdrApproval.setUKEY(uniqueID);
							doHdrAprrovalDAO.addDoHdr(doHdrApproval);
							
							List<DoDet> dodetail = new DoDetDAO().getDoDetById(plant, doHdr.getDONO());
							for (DoDet doDet : dodetail) {
								DoDetApproval doDetApproval = new DoDetApproval(); 
								new FieldCopier().copyFields(doDet, doDetApproval);
								doDetApproval.setUKEY(uniqueID);
								doDetApprovalDAO.addDoDet(doDetApproval);
							}
							
							
						}
					} else {
					if(doHdr.getORDER_STATUS().equalsIgnoreCase("APPROVAL PENDING")) {
						doHdr.setORDER_STATUS("Draft");
						doHdr.setAPP_CUST_ORDER_STATUS("CREATE APPROVAL PENDING");
						doHdr.setUKEY(uniqueID);
					}else {
						doHdr.setAPP_CUST_ORDER_STATUS("");
					}
					}
					
					ut = DbBean.getUserTranaction();

					ut.begin();

					if (action.equalsIgnoreCase("new") || action.equalsIgnoreCase("convertToSales")) {
						Hashtable doht = new Hashtable();
						doht.put("dono", doHdr.getDONO());
						doht.put("PLANT", doHdr.getPLANT());

						isOrderExists = new DoHdrDAO().isExisit(doht);

						if (isOrderExists) {
							oldDono = doHdr.getDONO();
							dono = new TblControlDAO().getNextOrder(plant, username, IConstants.OUTBOUND);
							doHdr.setDONO(dono);
						}
					}
					
					if (action.equalsIgnoreCase("convertToSales")) {
						for (int i = 0; i < chkdDONOCount; i++) {
							int index = Integer.parseInt((String) chkdDONO.get(i)) - 1;
							if (jobNum.contains("SE"))
								index = Integer.parseInt((String) chkdDONO.get(i));
							int lnno = i + 1;

							DoDet doDet = new DoDet();
							doDet.setPLANT(plant);
							doDet.setDONO(dono);
							doDet.setDOLNNO(lnno);
							if (jobNum.contains("SE")) {
								int estlnno = Integer.parseInt((String) elnno.get(i));
								doDet.setESTNO(jobNum);
								doDet.setESTLNNO(estlnno);
							} else {
								doDet.setESTNO("");
								doDet.setESTLNNO(0);
							}
							doDet.setPickStatus("N");
							doDet.setLNSTAT("N");
							doDet.setITEM((String) item.get(index));

							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, doDet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							doDet.setItemDesc(itemDesc);
							doDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));

							String itemunitprice = String.valueOf(Double.parseDouble((String) unitPrice.get(index))
									/ Double.parseDouble(currencyUseQty));
							String disctype = (String) item_discountType.get(index);
							double discountamount = Double.valueOf((String) item_discount.get(index));
							if (!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount / Double.parseDouble(currencyUseQty);
							}

							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									doDet.setUNITPRICE(Double.parseDouble("-" + itemunitprice));
								}
							} else {
								doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
							}

							doDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyOr.get(index))));
							doDet.setQTYIS(BigDecimal.valueOf(0));
							doDet.setQtyPick(BigDecimal.valueOf(0));
							doDet.setUNITMO((String) unitMo.get(index));
							doDet.setCRAT(DateUtils.getDateTime());
							doDet.setCRBY(username);
							doDet.setUSERFLD1(itemDesc);
							doDet.setUSERFLD2(jobNum);
							doDet.setUSERFLD3(custName);
							doDet.setCURRENCYUSEQT(Double.parseDouble((String) currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, doDet.getITEM());
								doDet.setPRODGST(Double.parseDouble((String) prodgst));
							}
							doDet.setACCOUNT_NAME((String) accountName.get(index));
							doDet.setPRODUCTDELIVERYDATE((String) prdDeliveryDate.get(index));
							doDet.setTAX_TYPE((String) taxType.get(index));
							doDet.setDISCOUNT(discountamount);
							doDet.setDISCOUNT_TYPE((String) item_discountType.get(index));
							doDet.setADDONAMOUNT(Double.parseDouble((String) addonAmount.get(index)));
							doDet.setADDONTYPE((String) addonType.get(index));
							String itemunitcost = String.valueOf(Double.parseDouble((String) unitcost.get(index))
									/ Double.parseDouble(currencyUseQty));
							doDet.setUNITCOST(Double.parseDouble(itemunitcost));
							//doDet.setUNITCOST(Double.parseDouble((String) unitcost.get(index)));
							doDet.setUPBY(username);
							doDet.setUPAT(DateUtils.getDateTime());
							if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {//Sales Approve 08.22 - Azees
								doDet.setUKEY(uniqueID);
							}
							new DoDetDAO().addDoDet(doDet);
							if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
								DoDetApproval doDetApproval= new DoDetApproval();
								new FieldCopier().copyFields(doDet, doDetApproval);
								doDetApproval.setUKEY(uniqueID);
								doDetApprovalDAO.addDoDet(doDetApproval);
							}
							DoDetList.add(doDet);
						}
					} else if (action.equalsIgnoreCase("edit")) {
						for (int i = 0; i < item.size(); i++) {
							int lnno = i + 1;
							boolean detcheck = doDetService.isgetDoDetById(plant, dono, lnno, (String) item.get(i));
							if (detcheck) {

								DoDet doDet = new DoDet();
								doDet = doDetService.getDoDetById(plant, dono, lnno, (String) item.get(i));
								doDet.setPLANT(plant);
								doDet.setDONO(dono);
								doDet.setDOLNNO(lnno);
								//doDet.setPickStatus("N");
								//doDet.setLNSTAT("N");
								if(doDet.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyOr.get(i)))//Status update Azees 
								{
									if(doDet.getQtyPick().doubleValue()>0) 
									{
										doDet.setPickStatus("O");
										doDet.setLNSTAT("0");
									} else {
									doDet.setPickStatus("N");
									doDet.setLNSTAT("N");
									}
								}
								doDet.setITEM((String)item.get(i));
								
								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, doDet.getITEM());
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
								doDet.setItemDesc(itemDesc);
								doDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));

								String itemunitprice = String.valueOf(Double.parseDouble((String) unitPrice.get(i))
										/ Double.parseDouble(currencyUseQty));
								String disctype = (String) item_discountType.get(i);
								double discountamount = Double.valueOf((String) item_discount.get(i));
								if (!disctype.equalsIgnoreCase("%")) {
									discountamount = discountamount / Double.parseDouble(currencyUseQty);
								}

								if (nonstocktype.equals("Y")) {
									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										doDet.setUNITPRICE(Double.parseDouble("-" + itemunitprice));
									} else {
										doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
									}
								} else {
									doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}

								doDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyOr.get(i))));
								// doDet.setQTYIS(BigDecimal.valueOf(0));
								// doDet.setQtyPick(BigDecimal.valueOf(0));
								doDet.setUNITMO((String) unitMo.get(i));
								doDet.setUPAT(DateUtils.getDateTime());
								doDet.setUPBY(username);
								// doDet.setCRAT(DateUtils.getDateTime());
								// doDet.setCRBY(username);
								doDet.setUSERFLD1(itemDesc);
								doDet.setUSERFLD2(jobNum);
								doDet.setUSERFLD3(custName);
								doDet.setCURRENCYUSEQT(Double.parseDouble((String) currencyUseQty));
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									String prodgst = new ItemMstDAO().getProductGst(plant, doDet.getITEM());
									doDet.setPRODGST(Double.parseDouble((String) prodgst));
								}
								doDet.setACCOUNT_NAME((String) accountName.get(i));
								doDet.setPRODUCTDELIVERYDATE((String) prdDeliveryDate.get(i));
								doDet.setTAX_TYPE((String) taxType.get(i));
								doDet.setDISCOUNT(discountamount);
								doDet.setDISCOUNT_TYPE((String) item_discountType.get(i));
								doDet.setADDONAMOUNT(Double.parseDouble((String) addonAmount.get(i)));
								doDet.setADDONTYPE((String) addonType.get(i));
								String itemunitcost = String.valueOf(Double.parseDouble((String) unitcost.get(i))
										/ Double.parseDouble(currencyUseQty));
								doDet.setUNITCOST(Double.parseDouble(itemunitcost));
								doDet.setUPAT(DateUtils.getDateTime());
								doDet.setUPBY(username);
								if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {//Sales Approve 08.22 - Azees
									doDet.setUKEY(uniqueID);
								}
								DoDetList.add(doDet);
							} else {

								DoDet doDet = new DoDet();
								doDet.setPLANT(plant);
								doDet.setDONO(dono);
								doDet.setDOLNNO(lnno);
								doDet.setPickStatus("N");
								doDet.setLNSTAT("N");
								doDet.setITEM((String) item.get(i));

								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, doDet.getITEM());
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
								doDet.setItemDesc(itemDesc);
								doDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));

								String itemunitprice = String.valueOf(Double.parseDouble((String) unitPrice.get(i))
										/ Double.parseDouble(currencyUseQty));
								String disctype = (String) item_discountType.get(i);
								double discountamount = Double.valueOf((String) item_discount.get(i));
								if (!disctype.equalsIgnoreCase("%")) {
									discountamount = discountamount / Double.parseDouble(currencyUseQty);
								}

								if (nonstocktype.equals("Y")) {
									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										doDet.setUNITPRICE(Double.parseDouble("-" + itemunitprice));
									} else {
										doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
									}
								} else {
									doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}

								doDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyOr.get(i))));
								doDet.setQTYIS(BigDecimal.valueOf(0));
								doDet.setQtyPick(BigDecimal.valueOf(0));
								doDet.setUNITMO((String) unitMo.get(i));
								doDet.setCRAT(DateUtils.getDateTime());
								doDet.setCRBY(username);
								doDet.setUSERFLD1(itemDesc);
								doDet.setUSERFLD2(jobNum);
								doDet.setUSERFLD3(custName);
								doDet.setCURRENCYUSEQT(Double.parseDouble((String) currencyUseQty));
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									String prodgst = new ItemMstDAO().getProductGst(plant, doDet.getITEM());
									doDet.setPRODGST(Double.parseDouble((String) prodgst));
								}
								doDet.setACCOUNT_NAME((String) accountName.get(i));
								doDet.setPRODUCTDELIVERYDATE((String) prdDeliveryDate.get(i));
								doDet.setTAX_TYPE((String) taxType.get(i));
								doDet.setDISCOUNT(discountamount);
								doDet.setDISCOUNT_TYPE((String) item_discountType.get(i));
								doDet.setADDONAMOUNT(Double.parseDouble((String) addonAmount.get(i)));
								doDet.setADDONTYPE((String) addonType.get(i));
								String itemunitcost = String.valueOf(Double.parseDouble((String) unitcost.get(i))
										/ Double.parseDouble(currencyUseQty));
								doDet.setUNITCOST(Double.parseDouble(itemunitcost));
								//doDet.setUNITCOST(Double.parseDouble((String) unitcost.get(i)));
								doDet.setUPAT(DateUtils.getDateTime());
								doDet.setUPBY(username);
								if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {//Sales Approve 08.22 - Azees
									doDet.setUKEY(uniqueID);
								}
								DoDetList.add(doDet);
							}
						}
					} else {
						for (int i = 0; i < item.size(); i++) {
							int lnno = i + 1;
							DoDet doDet = new DoDet();
							doDet.setPLANT(plant);
							doDet.setDONO(dono);
							doDet.setDOLNNO(lnno);
							doDet.setPickStatus("N");
							doDet.setLNSTAT("N");
							doDet.setITEM((String) item.get(i));

							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, doDet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							doDet.setItemDesc(itemDesc);
							doDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));

							String itemunitprice = String.valueOf(
									Double.parseDouble((String) unitPrice.get(i)) / Double.parseDouble(currencyUseQty));
							String disctype = (String) item_discountType.get(i);
							double discountamount = Double.valueOf((String) item_discount.get(i));
							if (!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount / Double.parseDouble(currencyUseQty);
							}

							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									doDet.setUNITPRICE(Double.parseDouble("-" + itemunitprice));
								} else {
									doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}
							} else {
								doDet.setUNITPRICE(Double.parseDouble(itemunitprice));
							}

							doDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyOr.get(i))));
							doDet.setQTYIS(BigDecimal.valueOf(0));
							doDet.setQtyPick(BigDecimal.valueOf(0));
							doDet.setUNITMO((String) unitMo.get(i));
							doDet.setCRAT(DateUtils.getDateTime());
							doDet.setCRBY(username);
							doDet.setUSERFLD1(itemDesc);
							doDet.setUSERFLD2(jobNum);
							doDet.setUSERFLD3(custName);
							doDet.setCURRENCYUSEQT(Double.parseDouble((String) currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, doDet.getITEM());
								doDet.setPRODGST(Double.parseDouble((String) prodgst));
							}
							doDet.setACCOUNT_NAME((String) accountName.get(i));
							doDet.setPRODUCTDELIVERYDATE((String) prdDeliveryDate.get(i));
							doDet.setTAX_TYPE((String) taxType.get(i));
							doDet.setDISCOUNT(discountamount);
							doDet.setDISCOUNT_TYPE((String) item_discountType.get(i));
							doDet.setADDONAMOUNT(Double.parseDouble((String) addonAmount.get(i)));
							doDet.setADDONTYPE((String) addonType.get(i));
							String itemunitcost = String.valueOf(Double.parseDouble((String) unitcost.get(i))
									/ Double.parseDouble(currencyUseQty));
							doDet.setUNITCOST(Double.parseDouble(itemunitcost));
							//doDet.setUNITCOST(Double.parseDouble((String) unitcost.get(i)));
							doDet.setUPAT(DateUtils.getDateTime());
							doDet.setUPBY(username);
							if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {//Sales Approve 08.22 - Azees
								doDet.setUKEY(uniqueID);
							}
							new DoDetDAO().addDoDet(doDet);
							if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
								DoDetApproval doDetApproval= new DoDetApproval();
								new FieldCopier().copyFields(doDet, doDetApproval);
								doDetApproval.setUKEY(uniqueID);
								doDetApprovalDAO.addDoDet(doDetApproval);
							}
							DoDetList.add(doDet);
						}
					}

					//ut = DbBean.getUserTranaction();

					//ut.begin();

					if (action.equalsIgnoreCase("new") || action.equalsIgnoreCase("convertToSales")) {
						/*Hashtable doht = new Hashtable();
						doht.put("dono", doHdr.getDONO());
						doht.put("PLANT", doHdr.getPLANT());

						isOrderExists = new DoHdrDAO().isExisit(doht);

						if (isOrderExists) {
							oldDono = doHdr.getDONO();
							dono = new TblControlDAO().getNextOrder(plant, username, IConstants.OUTBOUND);
							doHdr.setDONO(dono);
						}*/

						
						boolean insertFlag = false;//Sales Approve 08.22 - Azees
						//boolean insertFlag = doHDRService.addDoHdr(doHdr);
						int hdrid =new DoHdrDAO().addDoHdrReturnKey(doHdr);
						if(hdrid > 0) {
							insertFlag=true;
						}
						if (insertFlag) {
							if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
								DoHdrApproval doHdrApproval= new DoHdrApproval();
								new FieldCopier().copyFields(doHdr, doHdrApproval);
								doHdrApproval.setID(hdrid);
								doHdrAprrovalDAO.addDoHdr(doHdrApproval);
							}
					

						/*if (insertFlag) {

							insertFlag = doHDRService.addDoTransferHdr(doHdr);
						}*/
						//if (insertFlag) {
							//insertFlag = doDetService.addDoDet(DoDetList);
						//}
						/*if (insertFlag) {
							insertFlag = doDetService.addDoTransferDet(DoDetList);
						}*/
						//if (insertFlag) {
							
							String enableOutletAutoPrintPopup="0",AutoPopupOrderType="";
							 Map mh =  new HashMap();		     
						       mh=new DoHdrDAO().getDOReciptHeaderDetails(plant,"Outbound order");
						       if(!mh.isEmpty()){
						           enableOutletAutoPrintPopup = (String)mh.get("ENABLEOUTLETAUTOPRINTPOPUP");
						           AutoPopupOrderType = (String)mh.get("AUTOPOPUPORDERTYPE");
						       }
							
							Hashtable<String, String> htPRINTORD = new Hashtable<>();
							htPRINTORD.put(IDBConstants.PLANT, plant);
							htPRINTORD.put("ORDERNO", doHdr.getDONO());
							htPRINTORD.put("ORDERTYPE", AutoPopupOrderType);
							htPRINTORD.put("STATUS", "PENDING");
							htPRINTORD.put("USERID", username);
							htPRINTORD.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htPRINTORD.put(IDBConstants.CREATED_BY, username);
							if(enableOutletAutoPrintPopup.equalsIgnoreCase("1"))
								insertFlag = new DoHdrDAO().insertPrintOrder(htPRINTORD);
							
							if (action.equalsIgnoreCase("convertToSales")) {
								for (int i = 0; i < chkdDONOCount; i++) {
									int index = Integer.parseInt((String) chkdDONO.get(i)) - 1;
									if (jobNum.contains("SE"))
										index = Integer.parseInt((String) chkdDONO.get(i));
									int dolno = i + 1;
									
									//Add Sales Estimate Remark on SO -Azees (04.02.22)
									if (jobNum.contains("SE"))
									{
										Hashtable<String, String> ht = new Hashtable<>();
										ht.put(IDBConstants.PLANT, plant);
										ht.put(IDBConstants.ESTHDR_ESTNUM, jobNum);
										ht.put(IDBConstants.ESTHDR_ESTLNNUM, String.valueOf((String)elnno.get(index)));
										ht.put("ITEM", String.valueOf((String) item.get(index)));

										List al = new EstDetDAO().selectEstimateMultiRemarks("ISNULL(REMARKS,'') REMARKS ", ht,"");
										if (al.size() > 0) {
											for (int j = 0; j < al.size(); j++) {
												Map m = (Map) al.get(j);
												String remarksval = (String) m.get("REMARKS");
												
												Hashtable<String, String> htRemarksest = new Hashtable<>();
												htRemarksest.put(IDBConstants.PLANT, plant);
												htRemarksest.put(IDBConstants.DODET_DONUM, dono);
												htRemarksest.put(IDBConstants.DODET_DOLNNO, Integer.toString(dolno));
												htRemarksest.put(IDBConstants.DODET_ITEM, (String) item.get(index));
												htRemarksest.put(IDBConstants.REMARKS, remarksval);
												if (!new DoDetDAO().isExisitDoMultiRemarks(htRemarksest)) {
													htRemarksest.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
													htRemarksest.put(IDBConstants.CREATED_BY, username);
													insertFlag = new DOUtil().saveDoMultiRemarks(htRemarksest);
																					
												if (insertFlag) {
													Hashtable<String, String> htRecvHis = new Hashtable();
													htRecvHis.put(IDBConstants.PLANT, plant);
													htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
													htRecvHis.put("ORDNUM",dono);
													htRecvHis.put(IDBConstants.ITEM, "");
													htRecvHis.put("BATNO", "");
													htRecvHis.put(IDBConstants.LOC, "");
													htRecvHis.put("REMARKS", remarksval);
													htRecvHis.put(IDBConstants.CREATED_BY, username);
													htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
													htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
													insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
												}
												}
												}
											}
									}
									
									Hashtable<String, String> htRemarks = new Hashtable<>();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.DODET_DONUM, dono);
									htRemarks.put(IDBConstants.DODET_DOLNNO, Integer.toString(dolno));
									htRemarks.put(IDBConstants.DODET_ITEM, (String) item.get(index));
									if (!new DoDetDAO().isExisitDoMultiRemarks(htRemarks)) {
										htRemarks.put(IDBConstants.REMARKS, "");
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, username);
										insertFlag = new DOUtil().saveDoMultiRemarks(htRemarks);
									}
								}
								// Estimate Status & Qty Update
								if (jobNum.contains("SE")) {
									for (int i = 0; i < chkdDONOCount; i++) {
										int index = Integer.parseInt((String) chkdDONO.get(i)) - 1;
										if (jobNum.contains("SE"))
											index = Integer.parseInt((String) chkdDONO.get(i));
										int estlnno = Integer.parseInt((String) elnno.get(i));
										EstDetDAO _EstDetDAO = new EstDetDAO();
										EstHdrDAO _EstHdrDAO = new EstHdrDAO();
										String updateEstHdr = "", updateEstDet = "";
										Hashtable htCondition = new Hashtable();
										htCondition.put("PLANT", plant);
										htCondition.put("ESTNO", jobNum);
										htCondition.put("ESTLNNO", String.valueOf(estlnno));
										String issuingQty = (String) qtyOr.get(index);
										String qtyor = (String) qtyEst.get(index);
										String issuedqty = (String) qtyIs.get(index);
										double Ordqty = Double.parseDouble(qtyor);
										double tranQty = Double.parseDouble(issuingQty);
										double issqty = Double.parseDouble(issuedqty);
										double sumqty = issqty + tranQty;
										sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);

										String extraCond = " AND  QtyOr >= isNull(qtyis,0) + " + issuingQty;
										if (Ordqty == sumqty) {
											updateEstDet = "set qtyis= isNull(qtyis,0) + " + issuingQty
													+ ", STATUS='C' ";

										} else {
											updateEstDet = "set qtyis= isNull(qtyis,0) + " + issuingQty
													+ ", STATUS='O' ";

										}

										insertFlag = _EstDetDAO.update(updateEstDet, htCondition, extraCond);

										if (insertFlag) {
											htCondition.remove("ESTLNNO");

											insertFlag = _EstDetDAO.isExisit(htCondition, "STATUS in ('O','N')");
											if (!insertFlag) {
												updateEstHdr = "set  STATUS='Confirm',ORDER_STATUS='PROCESSED' ";
												insertFlag = _EstHdrDAO.update(updateEstHdr, htCondition, "");
											} else {
												updateEstHdr = "set ORDER_STATUS='PARTIALLY PROCESSED' ";
												insertFlag = _EstHdrDAO.update(updateEstHdr, htCondition, "");
											}

										}
									}
								}

							} else {
								for (int i = 0; i < item.size(); i++) {
									int dolno = i + 1;
									Hashtable<String, String> htRemarks = new Hashtable<>();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.DODET_DONUM, dono);
									htRemarks.put(IDBConstants.DODET_DOLNNO, Integer.toString(dolno));
									htRemarks.put(IDBConstants.DODET_ITEM, (String) item.get(i));
									if (!new DoDetDAO().isExisitDoMultiRemarks(htRemarks)) {
										htRemarks.put(IDBConstants.REMARKS, "");
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, username);
										insertFlag = new DOUtil().saveDoMultiRemarks(htRemarks);
									}
								}
							}

						}
						if (insertFlag) {
							int attchSize = salesAttachmentList.size();
							if (attchSize > 0) {
								for (int i = 0; i < attchSize; i++) {
									Hashtable salesAttachmentat = new Hashtable<String, String>();
									salesAttachmentat = salesAttachmentList.get(i);
									salesAttachmentat.put("DONO", dono);
									salesAttachmentInfoList.add(salesAttachmentat);
								}
								insertFlag = salesAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
							}
						}
						if (insertFlag) {
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							if (!remark1.equals("")) {
								if (action.equalsIgnoreCase("convertToSales")) {
									if (jobNum.contains("SE")) {
										htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + ","
												+ StrUtils.InsertQuotes(remark1) + "," + dono + "," + jobNum);
									}
								} else
									htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + ","
											+ StrUtils.InsertQuotes(remark1));
							} else {

								if (action.equalsIgnoreCase("convertToSales")) {
									if (jobNum.contains("SE")) {
										htRecvHis.put(IDBConstants.REMARKS,
												StrUtils.InsertQuotes(custName) + "," + dono + "," + jobNum);
									}
								} else
									htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
							}

							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						
						
						for (DoDet dorderdet : DoDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, doHdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, doHdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, dorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, dorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, doHdr.getDONO());
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.TRAN_DATE,
									DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
							
						}
						}

						
						if(insertFlag) {
							/*----------------- automatic invoice-----------------------*/
							String AutoConvStatus = new PlantMstDAO().getAutoSOINVOICE(plant);
							if(AutoConvStatus.equalsIgnoreCase("1")) {
									if(doHdr.getORDER_STATUS().equalsIgnoreCase("Open")) {
										String gino = goodsIssueing(request, response, doHdr, DoDetList);
										if(gino.equalsIgnoreCase("not ok")) {
											insertFlag = false;
										}else {
											String invstatus = createInvoice(request, response, doHdr, DoDetList, gino, plant, username);
											if(invstatus.equalsIgnoreCase("ok")) {
												insertFlag = true;
											}else {
												insertFlag = false;
											}
										}
									}
							}
							/*----------------- automatic invoice-----------------------*/
						}
						
						if(doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {//Sales Approve 09.22 - Azees
							List<DoDetRemarks> dodetremarks =new DoDetDAO().getDoDetRemarksByDono(plant, doHdr.getDONO());
							if(dodetremarks.size() > 0) {
								for (DoDetRemarks DoDetRk : dodetremarks) {
									DodetApprovalRemarks dodetApprovalRemarks = new DodetApprovalRemarks();
									new FieldCopier().copyFields(DoDetRk, dodetApprovalRemarks);
									dodetApprovalRemarks.setUKEY(uniqueID);
									dodetApprovalRemarks.setPLANT(plant);
									doDetApprovalDAO.addDoDetApprovalRemarks(dodetApprovalRemarks);
									
								}
							}
						}
						
						if (insertFlag) {
							new TblControlUtil().updateTblControlSeqNo(plant, IConstants.OUTBOUND, "S", dono);
							DbBean.CommitTran(ut);
							if (isOrderExists) {
								resultJson.put("MESSAGE",
										"Sales order " + oldDono
												+ " has already been used. System has auto created a new sales order "
												+ dono + " for you.");
							} else {
								resultJson.put("MESSAGE", "Sales Order Created Successfully.");
							}
							resultJson.put("ERROR_CODE", "100");
							autoinverr="";
							String message = "Sales Order Added Successfully.";
							if (ajax) {
								request.setAttribute("DONO", doHdr.getDONO());
								EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
								Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
								// TODO: Send proper value in action parameter
								String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
								if ("both".equals(sendAttachment) || "picking_list".equals(sendAttachment)) {
									viewDOReport(request, response);
								}
								if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
									viewInvoiceReport(request, response);
								}
								// viewDOReport(request, response, "printPO");
								resultJson.put("MESSAGE", message);
								resultJson.put("ERROR_CODE", "100");
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(resultJson.toString());
								response.getWriter().flush();
								response.getWriter().close();
							} else {
								response.sendRedirect("../salesorder/summary?msg=" + message);
							}

							String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,
									IConstants.SALES_ORDER);
							if (isAutoEmail.equalsIgnoreCase("Y"))
								new EmailMsgUtil().sendEmail(plant, dono, IConstants.SALES_ORDER);
						} else {
							DbBean.RollbackTran(ut);
							String message = "Unable To Add Sales Order. "+autoinverr;
							if (ajax) {
								resultJson.put("MESSAGE", message);
								resultJson.put("ERROR_CODE", "99");
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(resultJson.toString());
								response.getWriter().flush();
								response.getWriter().close();
							} else {
								if(action.equalsIgnoreCase("new"))								
								response.sendRedirect("../salesorder/new?msg=" + message);
								else
								response.sendRedirect("../salesorder/summary?msg=" + message);
							}
							resultJson.put("MESSAGE", "Failed to Create Sales Order.");
							resultJson.put("ERROR_CODE", "98");
						}
					} else if (action.equalsIgnoreCase("edit")) {
						//doHdr.setORDER_STATUS("Open");
						doHdr.setUPAT(DateUtils.getDateTime());
						doHdr.setUPBY(username);
						boolean updateFlag = doHDRService.updateDoHdr(doHdr);
						/*if (updateFlag) {
							try {
							updateFlag = doHDRService.updateDoTransferHdr(doHdr);
							}catch (Exception e) {
								System.out.println();
							}
						}*/
						if (updateFlag) {
							Hashtable ht = new Hashtable();
							ht.put("DONO", doHdr.getDONO());
							ht.put("PLANT", plant);
							updateFlag = new DoDetDAO().delete(ht);

							if (updateFlag)
								updateFlag = doDetService.addDoDet(DoDetList);
						}
						if (updateFlag) {
							int attchSize = salesAttachmentList.size();
							if (attchSize > 0) {
								for (int i = 0; i < attchSize; i++) {
									Hashtable salesAttachmentat = new Hashtable<String, String>();
									salesAttachmentat = salesAttachmentList.get(i);
									salesAttachmentat.put("DONO", dono);
									salesAttachmentInfoList.add(salesAttachmentat);
								}
								updateFlag = salesAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
							}
						}
						if (doHdr.getAPP_CUST_ORDER_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {//Sales Approve 09.22 - Azees
							List<DoDetRemarks> dodetremarks = new DoDetDAO().getDoDetRemarksByDono(plant, doHdr.getDONO());
							if (dodetremarks.size() > 0) {
								for (DoDetRemarks DoDetRk : dodetremarks) {
									DodetApprovalRemarks dodetApprovalRemarks = new DodetApprovalRemarks();
									new FieldCopier().copyFields(DoDetRk, dodetApprovalRemarks);
									dodetApprovalRemarks.setUKEY(uniqueID);
									dodetApprovalRemarks.setPLANT(plant);
									doDetApprovalDAO.addDoDetApprovalRemarks(dodetApprovalRemarks);
									
								}
							}
						}
						//ORDER_STATUS Update - Azees 3.21
						if(!orderstatus.equalsIgnoreCase("Draft")) {
							if(updateFlag) {
								Hashtable htCondition = new Hashtable();
								htCondition.put("PLANT", plant);
								htCondition.put("DONO", doHdr.getDONO());					
								updateFlag = new DoDetDAO().isExisit(htCondition,"PickStatus in ('O','N')");
								if (!updateFlag){
									String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
									updateFlag = new DoHdrDAO().update(updateHdr, htCondition, "");
								} else {
									updateFlag = new DoDetDAO().isExisit(htCondition,"PickStatus in ('O')");
									if (!updateFlag){
										updateFlag = new DoDetDAO().isExisit(htCondition,"PickStatus in ('C','N')");
										if (!updateFlag){
											String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
											updateFlag = new DoHdrDAO().update(updateHdr, htCondition, "");	
										} else {
											updateFlag = new DoDetDAO().isExisit(htCondition,"PickStatus in ('C')");
											if (!updateFlag){
												String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
												updateFlag = new DoHdrDAO().update(updateHdr, htCondition, "");
										} else {
											String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
											updateFlag = new DoHdrDAO().update(updateHdr, htCondition, "");
										}
									} 
									}else {
										String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
										updateFlag = new DoHdrDAO().update(updateHdr, htCondition, "");	
									}						
								}
							}
						}
						/*if(updateFlag) {
							Hashtable ht = new Hashtable();
							ht.put("DONO", doHdr.getDONO());
							ht.put("PLANT", plant);
							updateFlag = new DoTransferDetDAO().delete(ht);
							
							if(updateFlag)
								updateFlag = doDetService.addDoTransferDet(DoDetList);
						}*/
						if(updateFlag) {
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_OB);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							/* Author: Azees  Create date: July 5,2021  Description: Update Issue */
							if (!remark1.equals("")) {
								
									if(jobNum.contains("SE")) {
										htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + "," 
												+ StrUtils.InsertQuotes(remark1) +","+ dono+","+ jobNum);
									}							
									else
									htRecvHis.put(IDBConstants.REMARKS,
											StrUtils.InsertQuotes(custName) + "," 
									+ StrUtils.InsertQuotes(remark1));
							}
							else {

								if(jobNum.contains("SE")) {
									htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) +","+ dono+","+ jobNum);
								} else
									htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));								
							}
							updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						}
						
						if(updateFlag) {
							/*----------------- automatic invoice-----------------------*/
							String AutoConvStatus = new PlantMstDAO().getAutoSOINVOICE(plant);
							if(AutoConvStatus.equalsIgnoreCase("1")) {
								if(dohdrbydono.getORDER_STATUS().equalsIgnoreCase("Draft")) {
									if(doHdr.getORDER_STATUS().equalsIgnoreCase("Open")) {
										String gino = goodsIssueing(request, response, doHdr, DoDetList);
										if(gino.equalsIgnoreCase("not ok")) {
											updateFlag = false;
										}else {
											String invstatus = createInvoice(request, response, doHdr, DoDetList, gino, plant, username);
											if(invstatus.equalsIgnoreCase("ok")) {
												updateFlag = true;
											}else {
												updateFlag = false;
											}
										}
									}
								}
							}
							/*----------------- automatic invoice-----------------------*/
						}
						
							if(updateFlag) {
								DbBean.CommitTran(ut);
								String message = "Sales Order Updated Successfully.";
								if (ajax) {
									request.setAttribute("DONO", doHdr.getDONO());
									EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
									Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
									// TODO: Send proper value in action parameter
									String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
									if ("both".equals(sendAttachment) || "picking_list".equals(sendAttachment)) {
										viewDOReport(request, response);
									}
									if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
										viewInvoiceReport(request, response);
									}
									// viewDOReport(request, response, "printPO");
									resultJson.put("MESSAGE", message);
									resultJson.put("ERROR_CODE", "100");
									response.setContentType("application/json");
									response.setCharacterEncoding("UTF-8");
									response.getWriter().write(resultJson.toString());
									response.getWriter().flush();
									response.getWriter().close();
								} else {
									response.sendRedirect("../salesorder/summary?msg=" + message);
								}

								String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant,
										IConstants.SALES_ORDER);
								if (isAutoEmail.equalsIgnoreCase("Y"))
									new EmailMsgUtil().sendEmail(plant, dono, IConstants.SALES_ORDER);
							//} 
						}else {
							DbBean.RollbackTran(ut);
							String message = "Unable To Add Sales Order.";
							if (ajax) {
								resultJson.put("MESSAGE", message);
								resultJson.put("ERROR_CODE", "99");
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(resultJson.toString());
								response.getWriter().flush();
								response.getWriter().close();
							} else {
								response.sendRedirect("../salesorder/summary?msg=" + message);
							}
							resultJson.put("MESSAGE", "Failed to Create Sales Order.");
							resultJson.put("ERROR_CODE", "98");
						}
					}
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String dono = request.getParameter("r_dono");
				String dolno = request.getParameter("r_lnno");
				boolean insertFlag = false;

				Hashtable<String, String> htRemarksDel = new Hashtable<>();
				htRemarksDel.put(IDBConstants.PLANT, plant);
				htRemarksDel.put(IDBConstants.DODET_DONUM, dono);
				htRemarksDel.put(IDBConstants.DODET_DOLNNO, dolno);
				htRemarksDel.put(IDBConstants.DODET_ITEM, item);
				if (new DoDetDAO().isExisitDoMultiRemarks(htRemarksDel)) {
					new DoDetDAO().deleteDoMultiRemarks(htRemarksDel);
				}

				for (int i = 0; i < remarks.length; i++) {
					Hashtable<String, String> htRemarks = new Hashtable<>();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.DODET_DONUM, dono);
					htRemarks.put(IDBConstants.DODET_DOLNNO, dolno);
					htRemarks.put(IDBConstants.DODET_ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(remarks[i]));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, username);
					insertFlag = new DOUtil().saveDoMultiRemarks(htRemarks);
				}

				if (insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Sales Order Created Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("removeAttachmentById")) {
			System.out.println("Remove Attachments by ID");
			int ID = Integer.parseInt(request.getParameter("removeid"));
			SalesAttachDAO salesAttachDAO = new SalesAttachDAO();
			try {
//				Hashtable ht1 = new Hashtable();
				salesAttachDAO.deletesalesAttachByPrimId(plant, Integer.toString(ID));
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");

		} else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			int ID = Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling = new FileHandling();
			SalesAttachDAO salesAttachDAO = new SalesAttachDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = salesAttachDAO.getsalesAttachById(plant, String.valueOf(ID));
				Map billAttach = (Map) paymentAttachment.get(0);
				String filePath = (String) billAttach.get("FilePath");
				String fileType = (String) billAttach.get("FileType");
				String fileName = (String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("Delete")) {
			String dono = StrUtils.fString(request.getParameter("dono")).trim();
			DoDetDAO dodetDao = new DoDetDAO();
			DOUtil dOUtil = new DOUtil();
			JSONObject resultJson = new JSONObject();
			try {
				Hashtable htDoHrd = new Hashtable();
				htDoHrd.put(IDBConstants.PLANT, plant);
				htDoHrd.put(IDBConstants.DODET_DONUM, dono);
				boolean isValidOrder;
				isValidOrder = new DoHdrDAO().isExisit(htDoHrd, "");
				boolean isOrderInProgress = dodetDao.isExisit(htDoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {
						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, dono);
						if (!isExistsPaymentDetails) {
							PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();//Sales Approve 08.22 - Azees
							boolean approvalcheck = pltApproval.CheckApprovalByUser(plant, "SALES", "DELETE", username);
							if(approvalcheck) {
								String uniqueID = UUID.randomUUID().toString();
								DoHdrApproval doHdrApproval= new DoHdrApproval();
								DoHdr olddohdr =new DoHdrDAO().getDoHdrById(plant, dono);
								new FieldCopier().copyFields(olddohdr, doHdrApproval);
								doHdrApproval.setUKEY(uniqueID);
								doHdrAprrovalDAO.addDoHdr(doHdrApproval);
								
								
								  List<DoDet> dodetail =new DoDetDAO().getDoDetById(plant, dono); 
								  for (DoDet doDet : dodetail) { 
								  DoDetApproval doDetApproval = new DoDetApproval(); 
								  new FieldCopier().copyFields(doDet, doDetApproval);
								  doDetApproval.setUKEY(uniqueID);
								  doDetApprovalDAO.addDoDet(doDetApproval); 
								  }
								  olddohdr.setUKEY(uniqueID);
								  olddohdr.setAPP_CUST_ORDER_STATUS("DELETE APPROVAL PENDING"); 
								  new DoHdrDAO().updateDoHdr(olddohdr);
								 
								resultJson.put("MESSAGE", "Sales Order Delete Is Send for Approval.");
								resultJson.put("ERROR_CODE", "100");
							} else {
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("DONO", dono);
//							String query = "dono,custCode";
//							ArrayList al = dOUtil.getDoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = dOUtil.removeRow(plant, dono, username);
							if (value) {
								resultJson.put("MESSAGE", "Sales Order Deleted Successfully.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								resultJson.put("MESSAGE", "Sales Order Not Deleted.");
								resultJson.put("ERROR_CODE", "97");
							}
							}
						} else {
							resultJson.put("MESSAGE", "Sales Order Not Deleted.");
							resultJson.put("ERROR_CODE", "96");
						}
					} else {
						resultJson.put("MESSAGE", "Sales Order Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Sales Order Not Deleted.");
					resultJson.put("ERROR_CODE", "99");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

		if (action.equalsIgnoreCase("LoadEditDetails")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = getEditSODetails(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
	if(action.equalsIgnoreCase("salesdetails")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryOutBound.jsp?action=OrderSummaryOutBound.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("advancepayment")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("closesalesorder")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrderSales.jsp?action=closeByOrderSales.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	//for sales reports start
	if(action.equalsIgnoreCase("salessummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/OutboundOrderSalesSummary.jsp?action=OutboundOrderSalesSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("reordersummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReorderSalesSummary.jsp?action=ReorderSalesSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salesperformance")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesPerformanceSummary.jsp?action=SalesPerformanceSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	if(action.equalsIgnoreCase("salessummarycustomer")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesInvoiceSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("containersummaryexcel")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ContainerSummaryExcel.jsp?xlAction=GenerateXLSheet");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("customersummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/COCompanyReport.jsp?action=COCompanyReport.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("customersummaryexcel")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/CoCompanyReportExcel.jsp?xlAction=GenerateXLSheetForCoCompanyReport");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("orderhistory")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/IssuedOrdersHistory.jsp?action=IssuedOrdersHistory.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("issuesummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/OutBoundPickingSummary.jsp?action=OutBoundPickingSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("issuesummaryprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/GoodsIssueSummaryWithPrice.jsp?action=GoodsIssueSummaryWithPrice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if (action.equals("MANUALSTOCKTAKE_ADDPROCESS")) {
		String loc = StrUtils.fString(request.getParameter("LOCS1"));
		try {
			manualstocktake(request, response,loc);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	if (action.equals("DISCONTINUE_PRD")) {
		String posPageToDirect = "../salesorder/salessummary";
		boolean itemUpdated = false;String msg = "";
		Hashtable htUpdate = new Hashtable();
		Hashtable htCondition = new Hashtable();
		String[] chkstkid  = request.getParameterValues("CHKSTKID"); 
		Hashtable<String, String> htInvMst = new Hashtable<String, String>();
	    for (String ITEM : chkstkid) {
	    	try {
	    		htCondition.put(IConstants.ITEM,ITEM);
	    		htCondition.put(IConstants.PLANT,plant);
	    		htUpdate.put(IConstants.ITEM,ITEM);
	    		htUpdate.put(IConstants.ISACTIVE,"N");
	    		itemUpdated = new ItemUtil().updateItem(htUpdate,htCondition);
	    		htCondition.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    msg = "Product Update Successfully ";
		response.sendRedirect(posPageToDirect+"?result="+msg);
		
	}
	
	
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
		if(action.equalsIgnoreCase("reordersummary")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReorderSalesSummary.jsp?action=ReorderSalesSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (action.equalsIgnoreCase("CheckOrderno")) {
			JSONObject jsonObjectResult = new JSONObject();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.DONO, orderno);
				if (new DoHdrDAO().isExisit(ht)) {
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
		
if(action.equalsIgnoreCase("salesdetails")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryOutBound.jsp?action=OrderSummaryOutBound.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		if(action.equalsIgnoreCase("advancepayment")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePayment.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	if(action.equalsIgnoreCase("salesdetailsprice")) { 
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryOutBoundWithPrice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("closesalesorder")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrderSales.jsp?action=closeByOrderSales.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("pdfwithoutprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintOB.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("pdfdetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintDODetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("pdfwithprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintOBInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//on below is used for sales reports
	

	if(action.equalsIgnoreCase("summarydetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/obSumry_Issue.jsp?action=obSumry_Issue.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("summarydetailsbyoutlet")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/obSumry_IssuebyOutlets.jsp?action=obSumry_IssuebyOutlets.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("salesorderdelivery")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String res = StrUtils.fString(request.getParameter("result"));
			String pgact = StrUtils.fString(request.getParameter("PGaction"));
			request.setAttribute("Msg", msg);
			request.setAttribute("result", res);
			request.setAttribute("PGaction", pgact);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesOrderDeliverySummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("salessummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/OutboundOrderSalesSummary.jsp?action=OutboundOrderSalesSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salesperformance")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesPerformanceSummary.jsp?action=SalesPerformanceSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salessummarycustomer")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/SalesInvoiceSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("containersummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ContainerSummary.jsp?action=ContainerSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("containersummaryexcel")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ContainerSummaryExcel.jsp?xlAction=GenerateXLSheet");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("customersummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/COCompanyReport.jsp?action=COCompanyReport.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("customersummaryexcel")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/CoCompanyReportExcel.jsp?xlAction=GenerateXLSheetForCoCompanyReport");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("salessummarybyprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/obSumry_IssueWPrice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
if(action.equalsIgnoreCase("salessummarybypricebyoutlets")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/obSumry_IssueWPricebyOutlets.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("orderhistory")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/IssuedOrdersHistory.jsp?action=IssuedOrdersHistory.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
if(action.equalsIgnoreCase("orderhistorybyoutlet")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/IssuedOrdersHistoryByOutlets.jsp?action=IssuedOrdersHistoryByOutlets.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
if(action.equalsIgnoreCase("salespdf")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printDO.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salespdfdetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintDODetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("salespdfprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("Impsalesorder")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importOutboundOrderExcelSheet.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("ImpSOproductremark")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importOutboundProductRemarksExcelSheet.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("ImpShopify")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importShopifySalesOrder.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	if(action.equalsIgnoreCase("ImpShopee")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importShopeeSalesOrder.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("issuesummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/OutBoundPickingSummary.jsp?action=OutBoundPickingSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("issuesummaryprice")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/GoodsIssueSummaryWithPrice.jsp?action=GoodsIssueSummaryWithPrice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		if (action.equalsIgnoreCase("new")) {
			try {
				ArrayList taxTreatmentList = new MasterUtil().getTaxTreatmentList("", plant, "");
				ArrayList slList = new MasterUtil().getSalesLocationList("", plant, "");
				ArrayList ccList = new MasterUtil().getCountryList("", plant, region);
				ArrayList bankList = new MasterUtil().getBankList("", plant);

				String deldate = DateUtils.getDate();
				String collectionTime = DateUtils.getTimeHHmm();
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String msg = StrUtils.fString(request.getParameter("msg"));
				String gst = new selectBean().getGST("SALES", plant);
				String taxbylabel = new userBean().getTaxByLable(plant);

				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);

				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("NumberOfDecimal", numberOfDecimal);
				request.setAttribute("Currency", currency);
				request.setAttribute("GST", gst);
				request.setAttribute("Msg", msg);
				request.setAttribute("Taxbylabel", taxbylabel);
				request.setAttribute("Region", region);

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createSalesOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("Auto-Generate")) {
			String dono = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				dono = _TblControlDAO.getNextOrder(plant, username, IConstants.OUTBOUND);
				json.put("DONO", dono);
				response.setStatus(200);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getSalesOrderRemarks")) {
			String dono = "", dolnno = "";//, item = "";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				dono = StrUtils.fString(request.getParameter("DONO"));
				dolnno = StrUtils.fString(request.getParameter("DOLNO"));
//				item = StrUtils.fString(request.getParameter("ITEM"));

				Hashtable<String, String> ht = new Hashtable<>();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.DODET_DONUM, dono);
				ht.put(IDBConstants.DODET_DOLNNO, dolnno);

				List al = new DoDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
				if (al.size() > 0) {
					for (int i = 0; i < al.size(); i++) {
						Map m = (Map) al.get(i);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("remarks", (String) m.get("REMARKS"));
						jsonArray.add(resultJsonInt);
					}
				} else {
					jsonArray.add("");
				}
				resultJson.put("REMARKS", jsonArray);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("summary")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if (ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String reference = StrUtils.fString(request.getParameter("REFERENCE"));
				String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String approvestatus = StrUtils.fString(request.getParameter("APPROVESTATUS"));
				String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
				String fdate = "", tdate = "";
				DoHDRService doHDRService = new DoHdrServiceImpl();
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					String astatus="";//Sales Approve 09.22 - Azees
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("DONO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					if(approvestatus.equalsIgnoreCase("Pending")) {
						if(StrUtils.fString(approvestatus).length() > 0)	ht.put("ISNULL(APP_CUST_ORDER_STATUS, 'Pending')", "Pending");
					}else {
						if(approvestatus.equalsIgnoreCase("NEW_WAITING FOR OWNER APPROVAL")) {//Sales Approve 09.22 - Azees
							if(StrUtils.fString(approvestatus).length() > 0)	ht.put("ISNULL(APP_CUST_ORDER_STATUS, 'CREATE APPROVAL PENDING')", "CREATE APPROVAL PENDING");
						} else if(approvestatus.equalsIgnoreCase("EDIT_WAITING FOR OWNER APPROVAL")) {
							if(StrUtils.fString(approvestatus).length() > 0)	ht.put("ISNULL(APP_CUST_ORDER_STATUS, 'EDIT APPROVAL PENDING')", "EDIT APPROVAL PENDING");
						} else if(approvestatus.equalsIgnoreCase("OWNER APPROVED")) {
								astatus = " AND (APP_CUST_ORDER_STATUS='CREATE APPROVED' OR APP_CUST_ORDER_STATUS='EDIT APPROVED') ";
						} else if(approvestatus.equalsIgnoreCase("OWNER REJECTED")) {
							astatus = " AND (APP_CUST_ORDER_STATUS='CREATE REJECTED' OR APPROVAL_STATUS='EDIT REJECTED') ";
						}else {
							if(StrUtils.fString(approvestatus).length() > 0)	ht.put("APP_CUST_ORDER_STATUS", approvestatus);
						}
					}
					/*if(status.equalsIgnoreCase("PROCESSED")) {
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(STATUS, '')", "C");
					}
					else*/ if(status.equalsIgnoreCase("PARTIALLY PROCESSED")) {
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(STATUS, '')", "O");
					}
					/*else if(status.equalsIgnoreCase("OPEN")) {
							if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(STATUS, '')", "N");
							if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);
					}
					else*/
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);
						

						
						
					
					if(StrUtils.fString(plant).length() > 0)	ht.put("PLANT", plant);
					
					if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			           String curDate =DateUtils.getDate();
						if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
							FROM_DATE=curDate;

					if (FROM_DATE.length() > 5)
						fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-"
								+ FROM_DATE.substring(0, 2);

					if (TO_DATE == null)
						TO_DATE = "";
					else
						TO_DATE = TO_DATE.trim();
					if (TO_DATE.length() > 5)
						tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

					String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					//List<DoHdr> doHeaders = doHDRService.getDoHdr(ht, fdate, tdate);//Sales Approve 09.22 - Azees
					List<DoHdr> doHeaders =new DoHdrDAO().getDoHdrSummaryByApprove(ht, fdate, tdate, astatus, POSSEARCH);
					if (doHeaders.size() > 0) {
						for (DoHdr doHdr : doHeaders) {
							JSONObject json = new JSONObject();
							// double totalAmount = Double.parseDouble(new
							// DoDetDAO().getTotalAmount(doHdr.getDONO(), plant));
							FinCountryTaxType fintaxtype = finCountryTaxTypeDAO
									.getCountryTaxTypesByid(doHdr.getTAXID());
							List<DoDet> dodetail = new DoDetDAO().getDoDetById(plant, doHdr.getDONO());
							double totalAmount = 0;
							double subtotal = 0;
							for (DoDet doDet : dodetail) {
								if (doDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = doDet.getUNITPRICE() * doDet.getQTYOR().doubleValue();
									double discount = (ucost / 100) * doDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								} else {
									double ucost = doDet.getUNITPRICE() * doDet.getQTYOR().doubleValue();
									double discount = doDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								}
							}

							if (doHdr.getITEM_RATES() == 1) {
								subtotal = (subtotal * 100) / (doHdr.getOUTBOUND_GST() + 100);
							}

							double dorderdiscountcost = 0;
							if (doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
								dorderdiscountcost = (subtotal / 100) * doHdr.getORDERDISCOUNT();
							} else {
								dorderdiscountcost = doHdr.getORDERDISCOUNT();
							}

							double discountcost = 0;
							if (doHdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
								discountcost = (subtotal / 100) * doHdr.getDISCOUNT();
							} else {
								discountcost = doHdr.getDISCOUNT();
							}

							double totax = 0;
							if (doHdr.getTAXID() != 0) {
								if (fintaxtype.getSHOWTAX() == 1) {

									totax = (subtotal / 100) * doHdr.getOUTBOUND_GST();

									if (doHdr.getISSHIPPINGTAX() == 1) {
										totax = totax + ((doHdr.getSHIPPINGCOST() / 100) * doHdr.getOUTBOUND_GST());
									}

									if (doHdr.getISORDERDISCOUNTTAX() == 1) {
										totax = totax - ((dorderdiscountcost / 100) * doHdr.getOUTBOUND_GST());
									}

									if (doHdr.getISDISCOUNTTAX() == 1) {
										totax = totax - ((discountcost / 100) * doHdr.getOUTBOUND_GST());
									}
								}
							}

							totalAmount = (subtotal + doHdr.getSHIPPINGCOST() + totax + doHdr.getADJUSTMENT())
									- (dorderdiscountcost + discountcost);

							json.put("DATE", doHdr.getDELDATE());
							json.put("DONO", doHdr.getDONO());
							json.put("CUSTOMER", doHdr.getCustName());
							/*if(doHdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(doHdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", doHdr.getORDER_STATUS());
							
							String APPROVAL_STATUS=doHdr.getAPP_CUST_ORDER_STATUS();
							if(APPROVAL_STATUS.equalsIgnoreCase("CREATE APPROVAL PENDING"))
								APPROVAL_STATUS="NEW_WAITING FOR OWNER APPROVAL";
							else if(APPROVAL_STATUS.equalsIgnoreCase("EDIT APPROVAL PENDING"))
								APPROVAL_STATUS="EDIT_WAITING FOR OWNER APPROVAL";
							else if(APPROVAL_STATUS.equalsIgnoreCase("CREATE APPROVED"))
								APPROVAL_STATUS="OWNER APPROVED";
							else if(APPROVAL_STATUS.equalsIgnoreCase("EDIT APPROVED"))
								APPROVAL_STATUS="OWNER APPROVED";
							else if(APPROVAL_STATUS.equalsIgnoreCase("CREATE REJECTED"))
								APPROVAL_STATUS="OWNER REJECTED";
							else if(APPROVAL_STATUS.equalsIgnoreCase("EDIT REJECTED"))
								APPROVAL_STATUS="OWNER REJECTED";
							
							json.put("APP_CUST_ORDER_STATUS", APPROVAL_STATUS);
							json.put("DELIVERY_DATE", doHdr.getDELIVERYDATE());
							json.put("EXCHANGE_RATE", doHdr.getCURRENCYUSEQT());
							double amt = totalAmount * doHdr.getCURRENCYUSEQT();
							json.put("ORDAMT", doHdr.getCURRENCYID() + "" + Numbers.toMillionFormat(amt, numberOfDecimal));
//									StrUtils.addZeroes(amt, numberOfDecimal) + "(" + doHdr.getCURRENCYID() + ")");
							json.put("AMOUNT", Numbers.toMillionFormat(totalAmount,numberOfDecimal));
							json.put("POSSTATUS", doHdr.getSTATUS());
							jsonArray.add(json);
						}
					}
					resultJson.put("ORDERS", jsonArray);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} else {
				String msg = StrUtils.fString(request.getParameter("msg"));

				request.setAttribute("Msg", msg);

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesOrderSummary.jsp");
				rd.forward(request, response);
			}
		} else if (action.equalsIgnoreCase("detail")) {
			String dono = StrUtils.fString(request.getParameter("dono"));
			String imagePath = "", numberOfDecimal = "";
			DoHDRService doHDRService = new DoHdrServiceImpl();
			DoDetService doDetService = new DoDetServiceImpl();
			DoHdr doHdr = new DoHdr();
			Map plntMap = new HashMap();
			Map doHdrDetails = new HashMap();
			ArrayList custDetails = new ArrayList();
			ArrayList shippingCustDetails = new ArrayList();
			List<DoDet> doDetList = new ArrayList<DoDet>();
			ArrayList itemList = new ArrayList();

			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				doHdr = doHDRService.getDoHdrById(plant, dono);
				doDetList = doDetService.getDoDetById(plant, dono);

				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plant);
				plntMap = (Map) plntList.get(0);
				custDetails = new CustUtil().getCustomerDetailsForDO(dono, plant);
				shippingCustDetails = new MasterUtil().getOutboundShippingDetails(dono, doHdr.getSHIPPINGCUSTOMER(),
						plant);

				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}

				doHdrDetails = new DOUtil().getDOReceiptInvoiceHdrDetails(plant, "Outbound Order");

				/* Item Details 
				for (DoDet dodet : doDetList) {
					String OBDiscount = new DOUtil().getOBDiscountSelectedItemByCustomer(plant, doHdr.getCustCode(),
							dodet.getITEM(), "OUTBOUND");

					String discounttype = "";
					OBDiscount = (OBDiscount.length() == 0) ? "0.0" : OBDiscount;
					int plusIndex = OBDiscount.indexOf("%");
					if (plusIndex != -1) {
						OBDiscount = OBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}

					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost",
							new DOUtil().getConvertedUnitCostForProduct(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("ConvertedUnitCostWTC",
							new DOUtil().getConvertedUnitCostForProductWTC(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost",
							new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant, dodet.getDONO(),
									dodet.getITEM()));
					itemMap.put("outgoingOBDiscount", OBDiscount);
					Hashtable ht = new Hashtable();
					ht.put("item", dodet.getITEM());
					ht.put("plant", plant);
					Map m = new EstDetDAO().getEstQtyByProduct(ht);
					itemMap.put("EstQty", (String) m.get("ESTQTY"));

					m = new InvMstDAO().getAvailableQtyByProduct(ht);
					itemMap.put("AvlbQty", (String) m.get("AVLBQTY"));

					List listItem = new ItemUtil().queryItemMstDetails(dodet.getITEM(), plant);
					Vector arrItem = (Vector) listItem.get(0);
					if (arrItem.size() > 0) {
						itemMap.put("sItem", (String) arrItem.get(0));
						itemMap.put("sItemDesc", StrUtils.fString((String) arrItem.get(1)));
						itemMap.put("sUOM", StrUtils.fString((String) arrItem.get(3)));
						itemMap.put("prd_cls_id", StrUtils.fString((String) arrItem.get(10)));
						itemMap.put("sArtist", StrUtils.fString((String) arrItem.get(2)));// itemtype
						itemMap.put("brand", StrUtils.fString((String) arrItem.get(19)));
						itemMap.put("sTitle", StrUtils.fString((String) arrItem.get(7)));
						itemMap.put("sMedium", StrUtils.fString((String) arrItem.get(5)));
						itemMap.put("sItemCondition", StrUtils.fString((String) arrItem.get(6)));
						itemMap.put("sRemark", StrUtils.fString((String) arrItem.get(4)));
						itemMap.put("stkqty", StrUtils.fString((String) arrItem.get(8)));
						itemMap.put("price", StrUtils.fString((String) arrItem.get(12)));
						itemMap.put("cost", StrUtils.fString((String) arrItem.get(13)));
						itemMap.put("minsprice", StrUtils.fString((String) arrItem.get(14)));
						itemMap.put("discount", StrUtils.fString((String) arrItem.get(15)));
						itemMap.put("ISPARENTCHILD", StrUtils.fString((String) arrItem.get(16)));
						itemMap.put("isActive", StrUtils.fString((String) arrItem.get(11)));
						itemMap.put("nonstkflg", StrUtils.fString((String) arrItem.get(17)));
						itemMap.put("nonstktypeid", StrUtils.fString((String) arrItem.get(18)));
						itemMap.put("loc", StrUtils.fString((String) arrItem.get(20)));
						itemMap.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
						itemMap.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
						itemMap.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
						itemMap.put("PRODGST", StrUtils.fString((String) arrItem.get(24)));
						itemMap.put("netweight", StrUtils.fString((String) arrItem.get(25)));
						itemMap.put("grossweight", StrUtils.fString((String) arrItem.get(26)));
						itemMap.put("hscode", StrUtils.fString((String) arrItem.get(27)));
						itemMap.put("coo", StrUtils.fString((String) arrItem.get(28)));
						itemMap.put("vinno", StrUtils.fString((String) arrItem.get(29)));
						itemMap.put("model", StrUtils.fString((String) arrItem.get(30)));
						itemMap.put("RentalPrice", StrUtils.fString((String) arrItem.get(31)));
						itemMap.put("ServicePrice", StrUtils.fString((String) arrItem.get(32)));
						itemMap.put("PurchaseUOM", StrUtils.fString((String) arrItem.get(33)));
						itemMap.put("SalesUOM", StrUtils.fString((String) arrItem.get(34)));
						itemMap.put("RentalUOM", StrUtils.fString((String) arrItem.get(35)));
						itemMap.put("ServiceUOM", StrUtils.fString((String) arrItem.get(36)));
						itemMap.put("InventoryUOM", StrUtils.fString((String) arrItem.get(37)));
						itemMap.put("ISBASICUOM", StrUtils.fString((String) arrItem.get(38)));
						itemMap.put("outgoingqtyloan", StrUtils.fString((String) arrItem.get(39)));
						itemMap.put("catalogpath", StrUtils.fString((String) arrItem.get(40)));
						itemMap.put("OBDiscountType", discounttype);
						itemList.add(itemMap);
					}
				}
				 Item Details */
				
				for (DoDet dodet : doDetList) {
					Map itemMap = new HashMap();
//					List listItem = new ItemUtil().queryItemMstDetails(dodet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
					Map arrItem = new ItemMstUtil().GetProductForPurchase(dodet.getITEM(),plant);
					if (arrItem.size() > 0) {
						itemMap.put("brand",StrUtils.fString((String)arrItem.get("PRD_BRAND_ID")));
						itemMap.put("hscode",StrUtils.fString((String)arrItem.get("HSCODE")));
						itemMap.put("coo",StrUtils.fString((String)arrItem.get("COO")));
//						itemMap.put("brand", StrUtils.fString((String) arrItem.get(19)));		
//						itemMap.put("hscode", StrUtils.fString((String) arrItem.get(27)));
//						itemMap.put("coo", StrUtils.fString((String) arrItem.get(28)));
						itemList.add(itemMap);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}

			RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesOrderDetail.jsp");
			request.setAttribute("DoHdr", doHdr);
			request.setAttribute("DoDetList", doDetList);
			request.setAttribute("PLNTMAP", plntMap);
			request.setAttribute("DOHDRDETAILS", doHdrDetails);
			request.setAttribute("CUSTDETAILS", custDetails);
			request.setAttribute("SHIPPINGCUSTDETAILS", shippingCustDetails);
			request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
			request.setAttribute("ItemList", itemList);

			request.setAttribute("IMAGEPATH", imagePath);

			rd.forward(request, response);
		} else if (action.equalsIgnoreCase("edit")) {

			String dono = StrUtils.fString(request.getParameter("dono"));
			String imagePath = "", numberOfDecimal = "";
			DoHDRService doHDRService = new DoHdrServiceImpl();
			DoDetService doDetService = new DoDetServiceImpl();
			DoHdr doHdr = new DoHdr();
//			Map plntMap = new HashMap();
//			Map doHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<DoDet> doDetList = new ArrayList<DoDet>();
			String msg = "";

			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				doHdr = doHDRService.getDoHdrById(plant, dono);
				doDetList = doDetService.getDoDetById(plant, dono);
				String gst = new selectBean().getGST("SALES", plant);

				ArrayList taxTreatmentList = new MasterUtil().getTaxTreatmentList("", plant, "");
				ArrayList slList = new MasterUtil().getSalesLocationList("", plant, "");
				ArrayList ccList = new MasterUtil().getCountryList("", plant, region);
				ArrayList bankList = new MasterUtil().getBankList("", plant);
				ArrayList itemList = new ArrayList();
				List attachmentList = new SalesAttachDAO().getsalesAttachByDONO(plant, dono);

				/* Item Details */
				for (DoDet dodet : doDetList) {
					String OBDiscount = new DOUtil().getOBDiscountSelectedItemByCustomer(plant, doHdr.getCustCode(),
							dodet.getITEM(), "OUTBOUND");

					String discounttype = "";
					OBDiscount = (OBDiscount.length() == 0) ? "0.0" : OBDiscount;
					int plusIndex = OBDiscount.indexOf("%");
					if (plusIndex != -1) {
						OBDiscount = OBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}

					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost",
							new DOUtil().getConvertedUnitCostForProduct(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("ConvertedUnitCostWTC",
							new DOUtil().getConvertedUnitCostForProductWTC(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost",
							new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant, dodet.getDONO(),
									dodet.getITEM()));
					itemMap.put("outgoingOBDiscount", OBDiscount);
					Hashtable ht = new Hashtable();
					ht.put("item", dodet.getITEM());
					ht.put("plant", plant);
					Map m = new EstDetDAO().getEstQtyByProduct(ht);
					itemMap.put("EstQty", (String) m.get("ESTQTY"));

					m = new InvMstDAO().getAvailableQtyByProduct(ht);
					itemMap.put("AvlbQty", (String) m.get("AVLBQTY"));

//					List listItem = new ItemUtil().queryItemMstDetails(dodet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
					Map arrItem = new ItemMstUtil().GetProductForPurchase(dodet.getITEM(),plant);
			        if(arrItem.size()>0){
			        	
						itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						itemMap.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
						itemMap.put("price",StrUtils.fString((String)arrItem.get("UNITPRICE")));
						itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
						itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
						itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						itemMap.put("minsprice",StrUtils.fString((String)arrItem.get("MINSPRICE")));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get("CATLOGPATH")));
						itemMap.put("OBDiscountType", discounttype);
						
//						itemMap.put("sItem", (String) arrItem.get(0));
//						itemMap.put("sItemDesc", StrUtils.fString((String) arrItem.get(1)));
//						itemMap.put("sUOM", StrUtils.fString((String) arrItem.get(3)));
//						itemMap.put("prd_cls_id", StrUtils.fString((String) arrItem.get(10)));
//						itemMap.put("sArtist", StrUtils.fString((String) arrItem.get(2)));// itemtype
//						itemMap.put("brand", StrUtils.fString((String) arrItem.get(19)));
//						itemMap.put("sTitle", StrUtils.fString((String) arrItem.get(7)));
//						itemMap.put("sMedium", StrUtils.fString((String) arrItem.get(5)));
//						itemMap.put("sItemCondition", StrUtils.fString((String) arrItem.get(6)));
//						itemMap.put("sRemark", StrUtils.fString((String) arrItem.get(4)));
//						itemMap.put("stkqty", StrUtils.fString((String) arrItem.get(8)));
//						itemMap.put("price", StrUtils.fString((String) arrItem.get(12)));
//						itemMap.put("cost", StrUtils.fString((String) arrItem.get(13)));
//						itemMap.put("minsprice", StrUtils.fString((String) arrItem.get(14)));
//						itemMap.put("discount", StrUtils.fString((String) arrItem.get(15)));
//						itemMap.put("ISPARENTCHILD", StrUtils.fString((String) arrItem.get(16)));
//						itemMap.put("isActive", StrUtils.fString((String) arrItem.get(11)));
//						itemMap.put("nonstkflg", StrUtils.fString((String) arrItem.get(17)));
//						itemMap.put("nonstktypeid", StrUtils.fString((String) arrItem.get(18)));
//						itemMap.put("loc", StrUtils.fString((String) arrItem.get(20)));
//						itemMap.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
//						itemMap.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
//						itemMap.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
//						itemMap.put("PRODGST", StrUtils.fString((String) arrItem.get(24)));
//						itemMap.put("netweight", StrUtils.fString((String) arrItem.get(25)));
//						itemMap.put("grossweight", StrUtils.fString((String) arrItem.get(26)));
//						itemMap.put("hscode", StrUtils.fString((String) arrItem.get(27)));
//						itemMap.put("coo", StrUtils.fString((String) arrItem.get(28)));
//						itemMap.put("vinno", StrUtils.fString((String) arrItem.get(29)));
//						itemMap.put("model", StrUtils.fString((String) arrItem.get(30)));
//						itemMap.put("RentalPrice", StrUtils.fString((String) arrItem.get(31)));
//						itemMap.put("ServicePrice", StrUtils.fString((String) arrItem.get(32)));
//						itemMap.put("PurchaseUOM", StrUtils.fString((String) arrItem.get(33)));
//						itemMap.put("SalesUOM", StrUtils.fString((String) arrItem.get(34)));
//						itemMap.put("RentalUOM", StrUtils.fString((String) arrItem.get(35)));
//						itemMap.put("ServiceUOM", StrUtils.fString((String) arrItem.get(36)));
//						itemMap.put("InventoryUOM", StrUtils.fString((String) arrItem.get(37)));
//						itemMap.put("ISBASICUOM", StrUtils.fString((String) arrItem.get(38)));
//						itemMap.put("outgoingqtyloan", StrUtils.fString((String) arrItem.get(39)));
//						itemMap.put("catalogpath", StrUtils.fString((String) arrItem.get(40)));
//						itemMap.put("OBDiscountType", discounttype);
						itemList.add(itemMap);
					}
				}
				/* Item Details */

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editSalesOrder.jsp");
				request.setAttribute("DoHdr", doHdr);
				request.setAttribute("DoDetList", doDetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("Region", region);
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);
				request.setAttribute("AttachmentList", attachmentList);

				request.setAttribute("IMAGEPATH", imagePath);

				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		} else if (action.equalsIgnoreCase("convertToSales")) {

			String dono = StrUtils.fString(request.getParameter("dono"));
			String imagePath = "", numberOfDecimal = "";
			DoHDRService doHDRService = new DoHdrServiceImpl();
			DoDetService doDetService = new DoDetServiceImpl();
			DoHdr doHdr = new DoHdr();
//			Map plntMap = new HashMap();
//			Map doHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<DoDet> doDetList = new ArrayList<DoDet>();
			String msg = "", newDono = "", deldate = "", collectionTime = "";

			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				doHdr = doHDRService.getDoHdrById(plant, dono);
				doDetList = doDetService.getDoDetById(plant, dono);
				String gst = new selectBean().getGST("SALES", plant);
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();

				newDono = new TblControlDAO().getNextOrder(plant, username, IConstants.OUTBOUND);
				doHdr.setDONO(newDono);
				doHdr.setJobNum(dono);

				ArrayList taxTreatmentList = new MasterUtil().getTaxTreatmentList("", plant, "");
				ArrayList slList = new MasterUtil().getSalesLocationList("", plant, "");
				ArrayList ccList = new MasterUtil().getCountryList("", plant, region);
				ArrayList bankList = new MasterUtil().getBankList("", plant);
				ArrayList itemList = new ArrayList();
				List attachmentList = new SalesAttachDAO().getsalesAttachByDONO(plant, dono);

				/* Item Details */
				for (DoDet dodet : doDetList) {
					String OBDiscount = new DOUtil().getOBDiscountSelectedItemByCustomer(plant, doHdr.getCustCode(),
							dodet.getITEM(), "OUTBOUND");

					String discounttype = "";
					OBDiscount = (OBDiscount.length() == 0) ? "0.0" : OBDiscount;
					int plusIndex = OBDiscount.indexOf("%");
					if (plusIndex != -1) {
						OBDiscount = OBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}

					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost",
							new DOUtil().getConvertedUnitCostForProduct(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("ConvertedUnitCostWTC",
							new DOUtil().getConvertedUnitCostForProductWTC(plant, dodet.getDONO(), dodet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost",
							new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant, dodet.getDONO(),
									dodet.getITEM()));
					itemMap.put("outgoingOBDiscount", OBDiscount);
					Hashtable ht = new Hashtable();
					ht.put("item", dodet.getITEM());
					ht.put("plant", plant);
					Map m = new EstDetDAO().getEstQtyByProduct(ht);
					itemMap.put("EstQty", (String) m.get("ESTQTY"));

					m = new InvMstDAO().getAvailableQtyByProduct(ht);
					itemMap.put("AvlbQty", (String) m.get("AVLBQTY"));

//					List listItem = new ItemUtil().queryItemMstDetails(dodet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
					Map arrItem = new ItemMstUtil().GetProductForPurchase(dodet.getITEM(),plant);
			        if(arrItem.size()>0){
			        	
						itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						itemMap.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
						itemMap.put("price",StrUtils.fString((String)arrItem.get("UNITPRICE")));
						itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
						itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
						itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						itemMap.put("minsprice",StrUtils.fString((String)arrItem.get("MINSPRICE")));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get("CATLOGPATH")));
						itemMap.put("OBDiscountType", discounttype);
						
//						itemMap.put("sItem", (String) arrItem.get(0));
//						itemMap.put("sItemDesc", StrUtils.fString((String) arrItem.get(1)));
//						itemMap.put("sUOM", StrUtils.fString((String) arrItem.get(3)));
//						itemMap.put("prd_cls_id", StrUtils.fString((String) arrItem.get(10)));
//						itemMap.put("sArtist", StrUtils.fString((String) arrItem.get(2)));// itemtype
//						itemMap.put("brand", StrUtils.fString((String) arrItem.get(19)));
//						itemMap.put("sTitle", StrUtils.fString((String) arrItem.get(7)));
//						itemMap.put("sMedium", StrUtils.fString((String) arrItem.get(5)));
//						itemMap.put("sItemCondition", StrUtils.fString((String) arrItem.get(6)));
//						itemMap.put("sRemark", StrUtils.fString((String) arrItem.get(4)));
//						itemMap.put("stkqty", StrUtils.fString((String) arrItem.get(8)));
//						itemMap.put("price", StrUtils.fString((String) arrItem.get(12)));
//						itemMap.put("cost", StrUtils.fString((String) arrItem.get(13)));
//						itemMap.put("minsprice", StrUtils.fString((String) arrItem.get(14)));
//						itemMap.put("discount", StrUtils.fString((String) arrItem.get(15)));
//						itemMap.put("ISPARENTCHILD", StrUtils.fString((String) arrItem.get(16)));
//						itemMap.put("isActive", StrUtils.fString((String) arrItem.get(11)));
//						itemMap.put("nonstkflg", StrUtils.fString((String) arrItem.get(17)));
//						itemMap.put("nonstktypeid", StrUtils.fString((String) arrItem.get(18)));
//						itemMap.put("loc", StrUtils.fString((String) arrItem.get(20)));
//						itemMap.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
//						itemMap.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
//						itemMap.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
//						itemMap.put("PRODGST", StrUtils.fString((String) arrItem.get(24)));
//						itemMap.put("netweight", StrUtils.fString((String) arrItem.get(25)));
//						itemMap.put("grossweight", StrUtils.fString((String) arrItem.get(26)));
//						itemMap.put("hscode", StrUtils.fString((String) arrItem.get(27)));
//						itemMap.put("coo", StrUtils.fString((String) arrItem.get(28)));
//						itemMap.put("vinno", StrUtils.fString((String) arrItem.get(29)));
//						itemMap.put("model", StrUtils.fString((String) arrItem.get(30)));
//						itemMap.put("RentalPrice", StrUtils.fString((String) arrItem.get(31)));
//						itemMap.put("ServicePrice", StrUtils.fString((String) arrItem.get(32)));
//						itemMap.put("PurchaseUOM", StrUtils.fString((String) arrItem.get(33)));
//						itemMap.put("SalesUOM", StrUtils.fString((String) arrItem.get(34)));
//						itemMap.put("RentalUOM", StrUtils.fString((String) arrItem.get(35)));
//						itemMap.put("ServiceUOM", StrUtils.fString((String) arrItem.get(36)));
//						itemMap.put("InventoryUOM", StrUtils.fString((String) arrItem.get(37)));
//						itemMap.put("ISBASICUOM", StrUtils.fString((String) arrItem.get(38)));
//						itemMap.put("outgoingqtyloan", StrUtils.fString((String) arrItem.get(39)));
//						itemMap.put("catalogpath", StrUtils.fString((String) arrItem.get(40)));
//						itemMap.put("OBDiscountType", discounttype);
						itemList.add(itemMap);
					}
					dodet.setDONO(newDono);
				}
				/* Item Details */

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertFromSalesToSales.jsp");
				request.setAttribute("DoHdr", doHdr);
				request.setAttribute("DoDetList", doDetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("olddono", dono);
				request.setAttribute("Region", region);
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);
				request.setAttribute("AttachmentList", attachmentList);

				request.setAttribute("IMAGEPATH", imagePath);

				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		} else if (action.equalsIgnoreCase("convertToPurchase")) {
			try {
				String dono = StrUtils.fString(request.getParameter("dono"));
				request.setAttribute("DONO", dono);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				request.setAttribute("Region", region);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertFromSalesToPurchase.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("Approve_Sales_Order")) {
			try {
				
				String dono = StrUtils.fString(request.getParameter("dono"));
				
				DoHDRService doHDRService = new DoHdrServiceImpl();
				DoDetService doDetService = new DoDetServiceImpl();
				ItemMstDAO itemMstDAO = new ItemMstDAO();
				StoreHdrDAO storeHdrDAO = new StoreHdrDAO();
				StoreDetDAO storeDetDAO = new StoreDetDAO();
				ProductionBomUtil _ProductionBomUtil=new ProductionBomUtil();
				DoHdr doHdr = new DoHdr();
//				Map plntMap = new HashMap();
//				Map doHdrDetails = new HashMap();
//				ArrayList custDetails = new ArrayList();
//				ArrayList shippingCustDetails = new ArrayList();
				List<DoDet> doDetList = new ArrayList<DoDet>();
				String msg = "";

				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				doHdr = doHDRService.getDoHdrById(plant, dono);
				doDetList = doDetService.getDoDetById(plant, dono);
				
				doHdr.setORDER_STATUS("Open");
				doHdr.setAPP_CUST_ORDER_STATUS("Approved");
				
				for (DoDet doDet : doDetList) {
					String iscomp = itemMstDAO.getItemIsComp(plant, doDet.getITEM());
					if(iscomp.equalsIgnoreCase("1")) {
						ArrayList  movQryList = _ProductionBomUtil.getProdBomList(doDet.getITEM(),plant, " AND BOMTYPE='KIT'");
			            if (movQryList.size() > 0) {
			            	for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
			            		Map lineArr = (Map) movQryList.get(iCnt);
	                            String parentitem = StrUtils.fString((String)lineArr.get("PITEM")) ;
	                            String childitem = StrUtils.fString((String)lineArr.get("CITEM")) ;
	                            String desc = StrUtils.fString((String)lineArr.get("CDESC")) ;
	                            String pdesc = StrUtils.fString((String)lineArr.get("PDESC")) ;
	                            String qty = StrUtils.fString((String)lineArr.get("QTY")) ;
	                            String PUOM = StrUtils.fString((String)lineArr.get("PUOM")) ;
	                            String CUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
	                            
	                      
	                                boolean isitemcheck1 = storeHdrDAO.checkitem(childitem,"Open",plant);
	                                boolean isitemcheck2 = storeHdrDAO.checkitem(childitem,"Partially Processed",plant);

	                                if(isitemcheck1 || isitemcheck2){
	                                    StoreHdr sthdr = new StoreHdr();
	                                    String status="";
	                                    if(isitemcheck1){
	                                        sthdr = storeHdrDAO.getbyitemandstatus(childitem,"Open",plant);
	                                        status ="Open";
	                                    }
	                                    if(isitemcheck2){
	                                        sthdr = storeHdrDAO.getbyitemandstatus(childitem,"Partially Processed",plant);
	                                        status ="Partially Processed";
	                                    }


	                                    sthdr.setORDER_QTY(sthdr.getORDER_QTY() + (Double.valueOf(qty)*doDet.getQTYOR().doubleValue()));
	                                    sthdr.setBALANCE_QTY(sthdr.getBALANCE_QTY() + (Double.valueOf(qty)*doDet.getQTYOR().doubleValue()));
	                                    sthdr.setCRAT(DateUtils.getDateTime());
	                                    sthdr.setCRBY(username);
	                                    int hdrid = storeHdrDAO.setStoreHdr(sthdr);

	                                    StoreDet stdet = new StoreDet();
	                                    stdet.setPLANT(plant);
	                                    stdet.setHDRID(hdrid);
	                                    stdet.setDONO(dono);
	                                    stdet.setIS_CHILD_ITEM((short) 1);
	                                    stdet.setPITEM(parentitem);
	                                    stdet.setPITEM_DESC(pdesc);
	                                    stdet.setITEM(childitem);
	                                    stdet.setITEM_DESC(desc);
	                                    stdet.setUOM(CUOM);
	                                    stdet.setORDER_QTY(Double.valueOf(qty)*doDet.getQTYOR().doubleValue());
	                                    stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, childitem));
	                                    stdet.setSTATUS(status);
	                                    stdet.setCRAT(DateUtils.getDateTime());
	                                    stdet.setCRBY(username);

	                                    storeDetDAO.setStoreDet(stdet);

	                                }else{
	                                    StoreHdr sthdr = new StoreHdr();
	                                    
	                                    sthdr.setPLANT(plant);
	                                    sthdr.setITEM(childitem);
	                                    sthdr.setITEM_DESC(desc);
	                                    sthdr.setUOM(CUOM);
	                                    sthdr.setORDER_QTY(Double.valueOf(qty)*doDet.getQTYOR().doubleValue());
	                                    sthdr.setPROCESSED_QTY(0.0);
	                                    sthdr.setBALANCE_QTY(Double.valueOf(qty)*doDet.getQTYOR().doubleValue());
	                                    sthdr.setDEPARTMENT(itemMstDAO.getItemDept(plant, childitem));
	                                    sthdr.setSTATUS("Open");
	                                    sthdr.setCRAT(DateUtils.getDateTime());
	                                    sthdr.setCRBY(username);
	                                    int hdrid = storeHdrDAO.setStoreHdr(sthdr);
	                                    
	                                    StoreDet stdet = new StoreDet();
	                                    stdet.setPLANT(plant);
	                                    stdet.setHDRID(hdrid);
	                                    stdet.setDONO(dono);
	                                    stdet.setIS_CHILD_ITEM((short) 1);
	                                    stdet.setPITEM(parentitem);
	                                    stdet.setPITEM_DESC(pdesc);
	                                    stdet.setITEM(childitem);
	                                    stdet.setITEM_DESC(desc);
	                                    stdet.setUOM(CUOM);
	                                    stdet.setORDER_QTY(Double.valueOf(qty)*doDet.getQTYOR().doubleValue());
	                                    stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, childitem));
	                                    stdet.setSTATUS("Open");
	                                    stdet.setCRAT(DateUtils.getDateTime());
	                                    stdet.setCRBY(username);

	                                    storeDetDAO.setStoreDet(stdet);

	                                }
			            	}
			            }
			            
			            boolean isitemcheck1 = storeHdrDAO.checkitem(doDet.getITEM(),"Processed",plant);
		                if(isitemcheck1){
		                    StoreHdr sthdr = new StoreHdr();
		                    String status="";
		                    if(isitemcheck1){
		                        sthdr = storeHdrDAO.getbyitemandstatus(doDet.getITEM(),"Processed",plant);
		                        status ="Processed";
		                    }
		                    
		                    sthdr.setORDER_QTY(sthdr.getORDER_QTY()+doDet.getQTYOR().doubleValue());
		                    sthdr.setBALANCE_QTY(sthdr.getBALANCE_QTY()+doDet.getQTYOR().doubleValue());
		                    sthdr.setUPAT(DateUtils.getDateTime());
		                    sthdr.setUPBY(username);
		                    int hdrid = storeHdrDAO.setStoreHdr(sthdr);
		                    
		                    StoreDet stdet = new StoreDet();
                            stdet.setPLANT(plant);
                            stdet.setHDRID(hdrid);
                            stdet.setDONO(dono);
                            stdet.setIS_CHILD_ITEM((short) 0);
                            stdet.setITEM(doDet.getITEM());
                            stdet.setITEM_DESC(doDet.getItemDesc());
                            stdet.setUOM(doDet.getUNITMO());
                            stdet.setORDER_QTY(doDet.getQTYOR().doubleValue());
                            stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
                            stdet.setSTATUS(status);
                            stdet.setCRAT(DateUtils.getDateTime());
                            stdet.setCRBY(username);

                            storeDetDAO.setStoreDet(stdet);
		                    

		                }else{
		                	
		                	
		                	 StoreHdr sthdr = new StoreHdr();
                             
                             sthdr.setPLANT(plant);
                             sthdr.setITEM(doDet.getITEM());
                             sthdr.setITEM_DESC(doDet.getItemDesc());
                             sthdr.setUOM(doDet.getUNITMO());
                             sthdr.setORDER_QTY(doDet.getQTYOR().doubleValue());
                             sthdr.setPROCESSED_QTY(0.0);
                             sthdr.setBALANCE_QTY(doDet.getQTYOR().doubleValue());
                             sthdr.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
                             sthdr.setSTATUS("Processed");
                             sthdr.setCRAT(DateUtils.getDateTime());
                             sthdr.setCRBY(username);
                             int hdrid = storeHdrDAO.setStoreHdr(sthdr);
                             
                             StoreDet stdet = new StoreDet();
                             stdet.setPLANT(plant);
                             stdet.setHDRID(hdrid);
                             stdet.setDONO(dono);
                             stdet.setIS_CHILD_ITEM((short) 0);
                             stdet.setITEM(doDet.getITEM());
                             stdet.setITEM_DESC(doDet.getItemDesc());
                             stdet.setUOM(doDet.getUNITMO());
                             stdet.setORDER_QTY(doDet.getQTYOR().doubleValue());
                             stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
                             stdet.setSTATUS("Processed");
                             stdet.setCRAT(DateUtils.getDateTime());
                             stdet.setCRBY(username);

                             storeDetDAO.setStoreDet(stdet);

		                }
			            	
					}else {
						
			                boolean isitemcheck1 = storeHdrDAO.checkitem(doDet.getITEM(),"Open",plant);
			                boolean isitemcheck2 = storeHdrDAO.checkitem(doDet.getITEM(),"Partially Processed",plant);
			                if(isitemcheck1 || isitemcheck2){
			                    StoreHdr sthdr = new StoreHdr();
			                    String status="";
			                    if(isitemcheck1){
			                        sthdr = storeHdrDAO.getbyitemandstatus(doDet.getITEM(),"Open",plant);
			                        status ="Open";
			                    }
			                    if(isitemcheck2){
			                        sthdr = storeHdrDAO.getbyitemandstatus(doDet.getITEM(),"Partially Processed",plant);
			                        status ="Partially Processed";
			                    }
			                    
			                    
			                    sthdr.setORDER_QTY(sthdr.getORDER_QTY()+doDet.getQTYOR().doubleValue());
			                    sthdr.setBALANCE_QTY(sthdr.getBALANCE_QTY()+doDet.getQTYOR().doubleValue());
			                    sthdr.setUPAT(DateUtils.getDateTime());
			                    sthdr.setUPBY(username);
			                    int hdrid = storeHdrDAO.setStoreHdr(sthdr);
			                    
			                    
			                    StoreDet stdet = new StoreDet();
	                            stdet.setPLANT(plant);
	                            stdet.setHDRID(hdrid);
	                            stdet.setDONO(dono);
	                            stdet.setIS_CHILD_ITEM((short) 0);
	                            stdet.setITEM(doDet.getITEM());
	                            stdet.setITEM_DESC(doDet.getItemDesc());
	                            stdet.setUOM(doDet.getUNITMO());
	                            stdet.setORDER_QTY(doDet.getQTYOR().doubleValue());
	                            stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
	                            stdet.setSTATUS(status);
	                            stdet.setCRAT(DateUtils.getDateTime());
	                            stdet.setCRBY(username);

	                            storeDetDAO.setStoreDet(stdet);
			                    
			                   

			                }else{
			                    
			                     StoreHdr sthdr = new StoreHdr();
	                             
	                             sthdr.setPLANT(plant);
	                             sthdr.setITEM(doDet.getITEM());
	                             sthdr.setITEM_DESC(doDet.getItemDesc());
	                             sthdr.setUOM(doDet.getUNITMO());
	                             sthdr.setORDER_QTY(doDet.getQTYOR().doubleValue());
	                             sthdr.setPROCESSED_QTY(0.0);
	                             sthdr.setBALANCE_QTY(doDet.getQTYOR().doubleValue());
	                             sthdr.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
	                             sthdr.setSTATUS("Open");
	                             sthdr.setCRAT(DateUtils.getDateTime());
	                             sthdr.setCRBY(username);
	                             int hdrid = storeHdrDAO.setStoreHdr(sthdr);
	                             
	                             StoreDet stdet = new StoreDet();
	                             stdet.setPLANT(plant);
	                             stdet.setHDRID(hdrid);
	                             stdet.setDONO(dono);
	                             stdet.setIS_CHILD_ITEM((short) 0);
	                             stdet.setITEM(doDet.getITEM());
	                             stdet.setITEM_DESC(doDet.getItemDesc());
	                             stdet.setUOM(doDet.getUNITMO());
	                             stdet.setORDER_QTY(doDet.getQTYOR().doubleValue());
	                             stdet.setDEPARTMENT(itemMstDAO.getItemDept(plant, doDet.getITEM()));
	                             stdet.setSTATUS("Open");
	                             stdet.setCRAT(DateUtils.getDateTime());
	                             stdet.setCRBY(username);

	                             storeDetDAO.setStoreDet(stdet);
			                }
			            
					}
				}
					
				doHDRService.updateDoHdr(doHdr);	
				msg ="Sales order '"+dono+"' is Approved Successfully" ;
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesOrderSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equals("MANUALSTOCKTAKE_ADDPROCESS")) {
			String loc = StrUtils.fString(request.getParameter("LOCS"));
			try {
				manualstocktake(request, response,loc);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
	}

	private JSONObject getEditSODetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		DoHDRService doHDRService = new DoHdrServiceImpl();
		DoDetService doDetService = new DoDetServiceImpl();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		ItemMstUtil itemMstUtil = new ItemMstUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List<DoDet> dodetail = doDetService.getDoDetById(plant, dono);
			DoHdr doheader = doHDRService.getDoHdrById(plant, dono);
			if (dodetail.size() > 0) {
				for (DoDet doDet : dodetail) {
					/*
					 * StrUtils.addZeroes(dCost, numberOfDecimal); resultJsonInt.put("POLNNO",
					 * (String)m.get("LNNO"));
					 */
					JSONObject resultJsonInt = new JSONObject();

					String catlogpath = itemMstDAO.getcatlogpath(plant, doDet.getITEM());
					String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath);
					String itemdesc = "";
					String unitcost = "0";
					ArrayList listQry = itemMstUtil.getProductDetailsWithStockonHand(doDet.getITEM(), plant, itemdesc);
					if (listQry.size() > 0) {
						for (int i = 0; i < listQry.size(); i++) {
							Map m = (Map) listQry.get(i);
							unitcost = StrUtils.fString((String) m.get("COST"));
						}
					}

					resultJsonInt.put("LNNO", doDet.getDOLNNO());
					resultJsonInt.put("LNSTATUS", doDet.getLNSTAT());
					resultJsonInt.put("QTYRC", StrUtils.addZeroes(doDet.getQTYIS().doubleValue(), "3"));
					resultJsonInt.put("ITEM", doDet.getITEM());
					resultJsonInt.put("ITEMDESC", doDet.getItemDesc());
					resultJsonInt.put("ACCOUNTNAME", doDet.getACCOUNT_NAME());
					resultJsonInt.put("UOM", doDet.getUNITMO());
					resultJsonInt.put("QTY", StrUtils.addZeroes(doDet.getQTYOR().doubleValue(), "3"));
					resultJsonInt.put("PDELDATE", doDet.getPRODUCTDELIVERYDATE());
					resultJsonInt.put("UNITCOST", StrUtils.addZeroes(Double.valueOf(unitcost), numberOfDecimal));
					resultJsonInt.put("ITEMDISCOUNTTYPE", doDet.getDISCOUNT_TYPE());
					resultJsonInt.put("TAXTYPE", doDet.getTAX_TYPE());
					resultJsonInt.put("CATLOGPATH", cpath);
					resultJsonInt.put("CURRENCYUSEQT", doDet.getCURRENCYUSEQT());

					if (doDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(doDet.getDISCOUNT(), "3"));
						double amount = (Double.valueOf(unitcost) * (doDet.getQTYOR().doubleValue()))
								- (((Double.valueOf(unitcost) * (doDet.getQTYOR().doubleValue())) / 100)
										* doDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					} else {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(doDet.getDISCOUNT(), numberOfDecimal));
						double amount = (Double.valueOf(unitcost) * (doDet.getQTYOR().doubleValue()))
								- (doDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}
					String IBDiscount = new POUtil().getIBDiscountSelectedItemVNO(plant, doheader.getCustCode(),
							doDet.getITEM());
					String discounttype = "";

					int plusIndex = IBDiscount.indexOf("%");
					if (plusIndex != -1) {
						IBDiscount = IBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}
					String estQty = "", avlbQty = "";
                    Hashtable hts = new Hashtable();
             	 	hts.put("item", doDet.getITEM());
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
//					List listItem = itemUtil.queryItemMstDetailsforpurchase(doDet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
    				Map arrItem = new ItemMstUtil().GetProductForPurchase(doDet.getITEM(),plant);
					if (arrItem.size() > 0) {
						ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,"0");
						avlbQty= (String) ma.get("AVLBQTY");
						
						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get("STKQTY")));
						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get("MAXSTKQTY")));
						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get("STOCKONHAND")));
						resultJsonInt.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get("INCOMINGQTY")));
						
//						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get(8)));
//						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
//						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
//						resultJsonInt.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
//						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get(41)));
						resultJsonInt.put("customerdiscount", IBDiscount);
						resultJsonInt.put("discounttype", discounttype);
						//resultJsonInt.put("EstQty", "0.00");
						//resultJsonInt.put("AvlbQty", "0.00");
						resultJsonInt.put("EstQty",estQty);
						resultJsonInt.put("AvlbQty",avlbQty);

					}

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
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	
	
	
	
	private String goodsIssueing(HttpServletRequest request,
			HttpServletResponse response,DoHdr doHdr,List<DoDet> dodetlist ) throws ServletException, IOException,
			Exception {
		
		boolean flag = false;
		boolean gicheck = true;
//		StrUtils StrUtils = new StrUtils();
		DOUtil _DOUtil = new DOUtil();
		InvMstUtil _InvMstUtil = new InvMstUtil();
		Map issMat_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", DONO = "", LOC = "",dolno = "",item = "",QTYOR = "",issuingQty = "",UOMQTY="",uom="";//ITEM_DESCRIPTION = "",
		String ITEM_BATCH = "NOBATCH",BATCH_ID="",ITEM_QTY = "",PICKING_QTY = "",CUST_NAME = "",LOGIN_USER = "",SHIPPINGNO = "",ISNONSTKFLG="";//PICKED_QTY = "0",
		String REMARKS = "",ISSUEDATE="",strIssueDate="",strTranDate="",INVOICENO="", creditLimit = "", creditBy = "",priceval="";		
		double pickingQty = 0,unitprice=0,totalprice=0,totalqty=0;
		Boolean allChecked = false,fullIssue = false;
		//UserTransaction ut = null;
		Map checkedDOS = new HashMap();
		JSONObject resultJson = new JSONObject();
		try {

			HttpSession session = request.getSession();
            
            
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			DONO = StrUtils.fString(doHdr.getDONO());
			//String[] chkdDoNos  = request.getParameterValues("chkdDoNo");
			String[] chkdDoNos = new String[dodetlist.size()];
			int inc=0;
			for (DoDet doDet : dodetlist) {
				chkdDoNos[inc] = String.valueOf(doDet.getDOLNNO());
				inc++;
			}
			
			//LOC = new PlantMstDAO().getapiinvloc(PLANT);			
			CUST_NAME = StrUtils.fString(doHdr.getCustName());
			//LOGIN_USER = StrUtils.fString(doHdr.getUPBY());
			REMARKS = StrUtils.fString("Automatic Goods Issue");
			ISSUEDATE = StrUtils.fString(doHdr.getDELDATE());
			
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			String gino = _TblControlDAO.getNextOrder(PLANT,LOGIN_USER,"GINO");
			INVOICENO = StrUtils.fString(gino);
			
			
			InvMstDAO invMstDAO = new InvMstDAO();
			
			if (ISSUEDATE.length()>5)
				strTranDate    = ISSUEDATE.substring(6)+"-"+ ISSUEDATE.substring(3,5)+"-"+ISSUEDATE.substring(0,2);
				strIssueDate    = ISSUEDATE.substring(0,2)+"/"+ ISSUEDATE.substring(3,5)+"/"+ISSUEDATE.substring(6);
			
			
			
			if (chkdDoNos != null)    {     
				for (DoDet doDet : dodetlist)       { 
					dolno = String.valueOf(doDet.getDOLNNO());
					issuingQty = String.valueOf(doDet.getQTYOR());
					ITEM_BATCH = "NOBATCH";
					BATCH_ID = "-1";
					LOC = _ItemMstDAO.getItemDept(PLANT, doDet.getITEM());
					if(LOC.equalsIgnoreCase(""))
					{
							gicheck = false;
							autoinverr="Department & Location mapping does not found for the ProductID "+doDet.getITEM()+" at Order Line No "+dolno;
							throw new Exception(
									"Error in picking Sales Order : Location Not found for ProductID for Order Line No "+dolno);
							
							
					}
					checkedDOS.put(dolno, issuingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedDOS", checkedDOS);
            }
			// ut = DbBean.getUserTranaction();
	        //    ut.begin();
			ArrayList DODetails = null;

    		Hashtable htDoDet = new Hashtable();
    		String queryDoDet = "item,itemDesc,QTYOR,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY,UNITPRICE";
    		process: 	
			if (chkdDoNos != null)    {     
				for (DoDet doDet : dodetlist)       { 
					dolno = String.valueOf(doDet.getDOLNNO());
					
					issuingQty = String.valueOf(doDet.getQTYOR());	
					pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
					ITEM_BATCH = "NOBATCH";
					BATCH_ID = "-1";
					LOC = _ItemMstDAO.getItemDept(PLANT, doDet.getITEM());
					/*htDoDet.put(IConstants.DODET_DONUM, DONO);
					htDoDet.put(IConstants.PLANT, PLANT);
					htDoDet.put(IConstants.DODET_DOLNNO, dolno);
		    		DODetails = _DOUtil.getDoDetDetails(queryDoDet, htDoDet);*/
					/*if (DODetails.size() > 0) {	

							Map map1 = (Map) DODetails.get(0);*/
							item = doDet.getITEM();
							uom = doDet.getUNITMO();
							QTYOR = doDet.getQTYOR().toString();
							UOMQTY = "1";
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
							unitprice= doDet.getUNITPRICE();
							String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
							
							priceval=String.valueOf(totalprice);
							double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							
					/*}*/
					// check for item in location
					
	                UserLocUtil uslocUtil = new UserLocUtil();
	                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
	                if(!isvalidlocforUser){
	                	autoinverr=" Loc : "+LOC+" is not User Assigned Location";
	                    throw new Exception(" Loc : "+LOC+" is not User Assigned Location");
	                }
					if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
					List listQry = invMstDAO.getOutBoundPickingBatchByWMS(PLANT,item, LOC, ITEM_BATCH);
					double invqty = 0;
					if (listQry.size() > 0) {
						for (int j = 0; j < listQry.size(); j++) {
							Map m = (Map) listQry.get(j);
							ITEM_QTY = (String) m.get("qty");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
						}
						double calinqty = pickingQty * Double.valueOf(UOMQTY);
						if(invqty < calinqty){
							gicheck = false;
							autoinverr="Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected";
							throw new Exception(
									"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
							}						
					} 
					
					else {
						gicheck = false;
						autoinverr="Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected";
						throw new Exception(
								"Error in picking Sales Order : Not enough inventory found for ProductID/Batch for Order Line No "+dolno+" in the location selected");
						
					}
					}
	
//				double orderqty = Double.parseDouble(((String) QTYOR.trim()));
//				double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
				pickingQty = Double.parseDouble(((String) issuingQty.trim().toString()));
				pickingQty = StrUtils.RoundDB(pickingQty,IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(pickingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);  
				SHIPPINGNO = GenerateShippingNo(PLANT, LOGIN_USER);
			
				issMat_HM = new HashMap();
				issMat_HM.put(IConstants.PLANT, PLANT);
				issMat_HM.put(IConstants.ITEM, item);
				//issMat_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				issMat_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
				issMat_HM.put(IConstants.DODET_DONUM, DONO);
				issMat_HM.put(IConstants.DODET_DOLNNO, dolno);
				issMat_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				issMat_HM.put(IConstants.LOC, LOC);
				issMat_HM.put(IConstants.LOC2, "SHIPPINGAREA" + "_"+ LOC);
				issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
				issMat_HM.put(IConstants.BATCH_ID, BATCH_ID);
				issMat_HM.put(IConstants.QTY, PICKING_QTY);
				issMat_HM.put(IConstants.ORD_QTY, QTYOR);
				issMat_HM.put(IConstants.INV_EXP_DATE, "");
				issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DONO));
				issMat_HM.put("INV_QTY", "1");
				issMat_HM.put("SHIPPINGNO", SHIPPINGNO);
				issMat_HM.put(IConstants.REMARKS, REMARKS);
				issMat_HM.put("TYPE", "OBBULK");
				issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
				issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
				issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
				issMat_HM.put(IConstants.INVOICENO, INVOICENO);
				issMat_HM.put(IConstants.UOM, uom);
				issMat_HM.put("UOMQTY", UOMQTY);
				
				CustUtil custUtil = new CustUtil();
				ArrayList arrCust = custUtil.getCustomerDetails(_DOUtil.getCustCode(PLANT, DONO),PLANT);
				creditLimit   = (String)arrCust.get(24);
				creditBy   = (String)arrCust.get(35);
				
				//String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				issMat_HM.put(IConstants.CREDITLIMIT, StrUtils.addZeroes(Double.parseDouble(creditLimit), numberOfDecimal));
				issMat_HM.put(IConstants.CREDIT_LIMIT_BY, creditBy);
				issMat_HM.put(IConstants.BASE_CURRENCY, (String) session.getAttribute("BASE_CURRENCY"));
				
				flag = _DOUtil.autoprocess_DoPickIssueForPC(issMat_HM)&& true;
				if (flag) {
				totalprice=totalprice+(unitprice* pickingQty);
				totalqty=totalqty+pickingQty;
				}
				if (flag == true) {//Shopify Inventory Update
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
//   								String result="";
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
				if(!flag)
					break process;
				}
			}
	
			if (totalqty>0) {
				
				DoDetDAO _DoDetDAO = new DoDetDAO();
//				DateUtils dateutils = new DateUtils();
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.clear();
				htRecvDet.put(IConstants.PLANT, PLANT);
				htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
				htRecvDet.put("GINO", INVOICENO);                    
				htRecvDet.put(IConstants.DODET_DONUM, DONO);
				htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
				htRecvDet.put(IConstants.AMOUNT, priceval);
				htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
				htRecvDet.put("CRAT",DateUtils.getDateTime());
				htRecvDet.put("CRBY",LOGIN_USER);
				htRecvDet.put("UPAT",DateUtils.getDateTime());
				flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
                
                //insert MovHis
                MovHisDAO movHisDao = new MovHisDAO();
    			Hashtable htRecvHis = new Hashtable();
    			htRecvHis.clear();
    			htRecvHis.put(IDBConstants.PLANT, PLANT);
    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
    			htRecvHis.put(IDBConstants.ITEM, "");
    			htRecvHis.put("MOVTID", "");
    			htRecvHis.put("RECID", "");
    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
    			htRecvHis.put(IDBConstants.REMARKS, DONO);        					
    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
    			flag = movHisDao.insertIntoMovHis(htRecvHis);

				
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
				//DbBean.CommitTran(ut);	
				String msgflag=StrUtils.fString((String)	issMat_HM.get("msgflag"));
				if(msgflag.length()>0){
					request.getSession().setAttribute("RESULT",	msgflag	);
				}else {
					request.getSession().setAttribute("RESULT","Sales Order : " + DONO + "  Picked/Issued successfully!");
				}
				
			} else {
				//DbBean.RollbackTran(ut);
				gicheck = false;
				String message = "Failed to Pick/Issue Sales Order : "
						+ DONO;
			
			}
			
			}
			
		 catch (Exception e) {

			 if (totalqty>0) {
					
					DoDetDAO _DoDetDAO = new DoDetDAO();
//					DateUtils dateutils = new DateUtils();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DONO));
					htRecvDet.put("GINO", INVOICENO);                    
					htRecvDet.put(IConstants.DODET_DONUM, DONO);
					htRecvDet.put(IConstants.STATUS, "NOT INVOICED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
					htRecvDet.put("CRAT",DateUtils.getDateTime());
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",DateUtils.getDateTime());
					flag = _DoDetDAO.insertGINOtoInvoice(htRecvDet);
	                
	                //insert MovHis
	                MovHisDAO movHisDao = new MovHisDAO();
	    			Hashtable htRecvHis = new Hashtable();
	    			htRecvHis.clear();
	    			htRecvHis.put(IDBConstants.PLANT, PLANT);
	    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
	    			htRecvHis.put(IDBConstants.ITEM, "");
	    			htRecvHis.put("MOVTID", "");
	    			htRecvHis.put("RECID", "");
	    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
	    			htRecvHis.put(IDBConstants.REMARKS, DONO);        					
	    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, INVOICENO);
	    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	    			flag = movHisDao.insertIntoMovHis(htRecvHis);

					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", INVOICENO);
					INVOICENO="";
				}
			 gicheck = false;
			// DbBean.RollbackTran(ut);
			
		}

		if(gicheck) {
			return INVOICENO;
		}else {
			return "not ok";
		}
		
	}

	private String GenerateShippingNo(String plant, String loginuser) {

		String PLANT = "";
//		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
//		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnShippNo = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht
					.put(IDBConstants.TBL_FUNCTION,
							IDBConstants.TBL_SHIPPING_CAPTION);

			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, plant);

			if (exitFlag == false) {

//				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "Shipping");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) loginuser);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) DateUtils.getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "GENERATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
//					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate Shipping Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnShippNo = plant + updatedSeq;

//				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_SHIPPING_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "Shipping");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, plant);

				MovHisDAO mdao = new MovHisDAO(plant);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) plant);
				htm.put("DIRTYPE", "UPDATE_SHIPPING");
				htm.put("RECID", "");
				htm.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				htm.put("CRBY", (String) loginuser);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
//					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update Shippoing Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
//					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update Shipping Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return rtnShippNo;
	}
	
	public String createInvoice(HttpServletRequest request,
			HttpServletResponse response,DoHdr doHdr,List<DoDet> dodetlist,String gino, String plant, String username) {

		/* InvoiceHdr*/
		String CustCode = "", invoiceNo = "", dono = "", invoiceDate = "", dueDate = "", payTerms = "",cmd = "",TranId = "",salesloc="",orderdiscount="0",
		itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "",isexpense = "0",taxamount = "0",
		adjustment = "", subTotal = "", totalAmount = "", invoiceStatus = "", note = "",empno="",terms="",custName="",custName1="",empName="",taxtreatment="",
		shipId = "", shipCust = "", incoterm = "", origin = "", deductInv = "",currencyid="",currencyuseqt="0",projectid="",transportid="",orderdiscstatus = "0",discstatus = "0",
		shipstatus = "0",taxid = "",orderdisctype = "%",gst="0",jobNum="";//shippingcost="",
		
		/*InvoiceDet*/
		List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),notesexp = new ArrayList(),
				cost = new ArrayList(), detDiscount = new ArrayList(),detDiscounttype = new ArrayList(), taxType = new ArrayList(),DETID= new ArrayList(),
				amount = new ArrayList(),edit_item=new ArrayList<>(),edit_qty=new ArrayList<>(),tlnno= new ArrayList();
		List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),index = new ArrayList(),
				 ordQty = new ArrayList(), convcost= new ArrayList(), is_cogs_set = new ArrayList();
		List<Hashtable<String,String>> invoiceDetInfoList = null;
		List<Hashtable<String,String>> invoiceAttachmentList = null;
		List<Hashtable<String,String>> invoiceAttachmentInfoList = null;
		Hashtable<String,String> invoiceDetInfo = null;
		Hashtable<String,String> invoiceAttachment = null;
		UserTransaction ut = null;
		InvoiceUtil invoiceUtil = new InvoiceUtil();
		InvoiceDAO invoiceDAO = new InvoiceDAO();
//		DateUtils dateutils = new DateUtils();
		MovHisDAO movHisDao = new MovHisDAO();
		boolean isAdded = false;
		boolean isAmntExceed = false;
		boolean Isconvcost=false;
		String result="", amntExceedMsg ="";
		int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0, detDiscounttypeCount  = 0,
				taxTypeCount  = 0,DETIDCount  = 0, amountCount  = 0, notesexpcount =0,editItemCount=0,editQtyCount=0;
		int locCount  = 0,batchCount  = 0,uomCount  = 0, idCount = 0, ordQtyCount = 0;//,convcostCount = 0,is_cogs_setCount=0,tlnnoCount = 0,;
		/*others*/
		//String personIncharge="",ctype="",fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
		try{
			
	    	
	    	
			//invoiceNo = 
			//subTotal =
			//totalAmount = 
			//taxamount =
			//shipId=
			CustCode = doHdr.getCustCode();
			custName = doHdr.getCustName();
			custName1 = doHdr.getCustName();
			dono = doHdr.getDONO();
			invoiceDate = doHdr.getDELDATE();
			dueDate ="";
			payTerms ="";
			empno ="";
			empName = "";
			itemRates ="0";
			discount ="0";
			discountType = doHdr.getCURRENCYID();
			orderdiscount ="0";
			shippingCost = String.valueOf(doHdr.getSHIPPINGCOST()/doHdr.getCURRENCYUSEQT());
			adjustment = "0";
			invoiceStatus="Open";
			terms ="";
			note="";
			salesloc = doHdr.getSALES_LOCATION();
			isexpense ="0";
			taxtreatment = doHdr.getTAXTREATMENT();
			shipCust=doHdr.getCustCode();
			origin="sales";
			deductInv="0";
			incoterm="";
			currencyid= doHdr.getCURRENCYID();
			currencyuseqt = String.valueOf(doHdr.getCURRENCYUSEQT());
			orderdiscstatus="1";
			discstatus="1";
			shipstatus="0";
			taxid = String.valueOf(doHdr.getTAXID());
			orderdisctype=doHdr.getCURRENCYID();
			transportid = String.valueOf(doHdr.getTRANSPORTID());
			gst = String.valueOf(doHdr.getOUTBOUND_GST());
			jobNum = doHdr.getJobNum();
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			invoiceNo = _TblControlDAO.getNextOrder(plant,username,"INVOICE");
			
			//subTotal =
			//totalAmount = 
			//taxamount =
			//shipId=
			
			Double subtotaldet = 0.0;
			for(DoDet doDet:dodetlist) {
				item.add(itemCount, doDet.getITEM());
				itemCount++;
				
				accountName.add(accountNameCount, "Local sales - retail");
				accountNameCount++;
				
				qty.add(qtyCount, String.valueOf(doDet.getQTYOR()));
				qtyCount++;
				
				
				cost.add(costCount, String.valueOf(doDet.getUNITPRICE()));
				costCount++;
				
				detDiscount.add(detDiscountCount, "0");
				detDiscountCount++;
				
				detDiscounttype.add(detDiscounttypeCount, doHdr.getCURRENCYID());
				detDiscounttypeCount++;
				
				taxType.add(taxTypeCount, doDet.getTAX_TYPE());
				taxTypeCount++;
				
				amount.add(amountCount,String.valueOf((doDet.getUNITPRICE()*doDet.getQTYOR().doubleValue())));
				amountCount++;
				
				uom.add(uomCount, doDet.getUNITMO());
				uomCount++;
				
				ordQty.add(ordQtyCount, String.valueOf(doDet.getQTYOR()));
				ordQtyCount++;
				
				subtotaldet = subtotaldet + (doDet.getQTYOR().doubleValue() * (doDet.getUNITPRICE()/doHdr.getCURRENCYUSEQT()));
			}
	    	
			subTotal = String.valueOf(subtotaldet);			
			FinCountryTaxType  taxtypecounty = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(doHdr.getTAXID());
			double taxamountdouble = 0.0;
			if(doHdr.getTAXID() > 0){
			if(taxtypecounty.getISZERO() == 0) {
				taxamountdouble =  ((subtotaldet/100)*doHdr.getOUTBOUND_GST()); 
			}
			} else
				taxamountdouble =  ((subtotaldet/100)*doHdr.getOUTBOUND_GST());//taxid zero issue.. fix by Azees
			double totaldouble = subtotaldet + (doHdr.getSHIPPINGCOST()/doHdr.getCURRENCYUSEQT())+taxamountdouble;
			taxamount = String.valueOf(taxamountdouble);
			totalAmount = String.valueOf(totaldouble);
			
			String CURRENCYID = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			if(currencyid.isEmpty())
				if(!CURRENCYID.isEmpty())
					currencyid=CURRENCYID;
		
			
			//////////////////////
			invoiceDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable invoiceHdr =new Hashtable(); 
			invoiceHdr.put("PLANT", plant);
			invoiceHdr.put("CUSTNO", CustCode);
			invoiceHdr.put("INVOICE", invoiceNo);
			invoiceHdr.put("GINO",gino);
			invoiceHdr.put("DONO", dono);
			invoiceHdr.put("INVOICE_DATE", invoiceDate);
			invoiceHdr.put("DUE_DATE", dueDate);
			invoiceHdr.put("PAYMENT_TERMS", payTerms);
			invoiceHdr.put("EMPNO", empno);
			invoiceHdr.put("ITEM_RATES", itemRates);
			invoiceHdr.put("DISCOUNT", discount);
			invoiceHdr.put("ORDER_DISCOUNT", orderdiscount);
			invoiceHdr.put("DISCOUNT_TYPE", discountType);
			invoiceHdr.put("DISCOUNT_ACCOUNT", discountAccount);
			invoiceHdr.put("SHIPPINGCOST", shippingCost);
			invoiceHdr.put("ADJUSTMENT", adjustment);
			invoiceHdr.put("SUB_TOTAL", subTotal);
			invoiceHdr.put("TOTAL_AMOUNT", totalAmount);
			invoiceHdr.put("BILL_STATUS", invoiceStatus);
			invoiceHdr.put("NOTE", note);
			invoiceHdr.put("TERMSCONDITIONS", terms);
			invoiceHdr.put("CRAT",DateUtils.getDateTime());
			invoiceHdr.put("CRBY",username);
			invoiceHdr.put("UPAT",DateUtils.getDateTime());
			invoiceHdr.put("SALES_LOCATION", salesloc);
			invoiceHdr.put("ISEXPENSE",isexpense);
			invoiceHdr.put("TAXTREATMENT",taxtreatment);
			invoiceHdr.put("TAXAMOUNT",taxamount);
			invoiceHdr.put("SHIPPINGID",shipId);
			invoiceHdr.put("SHIPPINGCUSTOMER",shipCust);
			invoiceHdr.put("INCOTERMS",incoterm);
			invoiceHdr.put("ORIGIN",origin);
			invoiceHdr.put("DEDUCT_INV",deductInv);
			invoiceHdr.put("CURRENCYUSEQT",currencyuseqt);
			invoiceHdr.put("ORDERDISCOUNTTYPE",orderdisctype);
			invoiceHdr.put("TAXID",taxid);
			invoiceHdr.put("ISDISCOUNTTAX",discstatus);
			invoiceHdr.put("ISORDERDISCOUNTTAX",orderdiscstatus);
			invoiceHdr.put("ISSHIPPINGTAX",shipstatus);
			invoiceHdr.put("PROJECTID",projectid);
			invoiceHdr.put("TRANSPORTID",transportid);
			invoiceHdr.put("OUTBOUD_GST",gst);
			invoiceHdr.put(IDBConstants.CURRENCYID, currencyid);
			invoiceHdr.put("JobNum",jobNum);

			int invoiceHdrId=0;
			BillDAO itemCogsDao=new BillDAO();
			Costofgoods costofGoods=new CostofgoodsImpl();
	
				Map resultMap = getOutstandingAmountForCustomer(CustCode, Double.parseDouble(totalAmount), plant);
				isAmntExceed = (boolean) resultMap.get("STATUS");
				amntExceedMsg = (String) resultMap.get("MSG");
				if(!isAmntExceed) {
					invoiceHdr.put("CREDITNOTESSTATUS","0");
					invoiceHdr.put("TOTAL_PAYING","0");
					invoiceHdrId = invoiceUtil.addInvoiceHdr(invoiceHdr, plant);
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
					
					invoiceDetInfo = new Hashtable<String, String>();
					invoiceDetInfo.put("PLANT", plant);
					invoiceDetInfo.put("LNNO", Integer.toString(lnno));
					if(cmd.equalsIgnoreCase("Edit"))
						invoiceDetInfo.put("INVOICEHDRID", TranId);						
					else
						invoiceDetInfo.put("INVOICEHDRID", Integer.toString(invoiceHdrId));
					//invoiceDetInfo.put("INVOICEHDRID", TranId);							
					invoiceDetInfo.put("ITEM", (String) item.get(i));
					invoiceDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
					invoiceDetInfo.put("QTY", (String) qty.get(i));
					invoiceDetInfo.put("UNITPRICE", convCost);
					invoiceDetInfo.put("TAX_TYPE", (String) taxType.get(i));
					invoiceDetInfo.put("AMOUNT", convAmount);
					invoiceDetInfo.put("CRAT",DateUtils.getDateTime());
					invoiceDetInfo.put("CRBY",username);
					invoiceDetInfo.put("UPAT",DateUtils.getDateTime());
					invoiceDetInfo.put("DISCOUNT", convDiscount);
					invoiceDetInfo.put("DISCOUNT_TYPE", (String) detDiscounttype.get(i));
					invoiceDetInfo.put("UOM", (String) uom.get(i));
					invoiceDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);

					if(loc.size() > 0) {
						invoiceDetInfo.put("LOC", (String) loc.get(i));
						invoiceDetInfo.put("BATCH", (String) batch.get(i));
					}
					
					invoiceDetInfoList.add(invoiceDetInfo);
					
					int cogsCnt=itemCogsDao.addItemCogs(costofGoods.soldProductDetails((String)qty.get(i), (String)item.get(i), plant, dueDate),plant);
					System.out.println("Insert ItemCogs Status :"+ cogsCnt);

					
					
				}
				
				isAdded = invoiceUtil.addMultipleInvoiceDet(invoiceDetInfoList, plant);

				if(isAdded) {

					
					String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					System.out.println("invoice Status"+invoiceStatus);
					if(!invoiceStatus.equalsIgnoreCase("Draft"))
					{
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(invoiceDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("INVOICE");
						journalHead.setTRANSACTION_ID(invoiceNo);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						CustMstDAO cusDAO=new CustMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						Double totalCostofGoodSold=0.00;
						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) CustCode);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject CusJson=cusDAO.getCustomerName(plant, (String) CustCode);
							if(!CusJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject())
								{
									coaJson1=coaDAO.getCOAByName(plant, CusJson.getString("CUSTNO")+"-"+CusJson.getString("CNAME"));
								}
							}
						}
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							
						}
						else
						{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							//journalDetail_1.setACCOUNT_NAME((String) CustCode);
							if(coaJson1.getString("account_name")!=null) {
								journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
							}
							journalDetail_1.setDEBITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
						}
						
						if(discount.isEmpty())
						{
							discount="0.00";
						}
						Double discountFrom = Double.parseDouble(discount);
						Double orderDiscountFrom=0.00;
						if(!orderdiscount.isEmpty())
						{
							orderDiscountFrom=Double.parseDouble(orderdiscount);
							
							if(orderdisctype.toString().equalsIgnoreCase("%"))
								orderdiscount=Double.toString((Double.parseDouble(subTotal)*orderDiscountFrom)/100);
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
							JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discounts given - COS");
							journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
							journalDetail_3.setACCOUNT_NAME("Discounts given - COS");
							journalDetail_3.setDEBITS(discountFrom);
							journalDetails.add(journalDetail_3);
						}
						
						for(Map invDet:invoiceDetInfoList)
						{
							Double quantity=Double.parseDouble(invDet.get("QTY").toString());
							String netWeight=itemMstDAO.getItemNetWeight(plant, invDet.get("ITEM").toString());
							if(netWeight!=null && !"".equals(netWeight))
							{
								Double Netweight=quantity*Double.parseDouble(netWeight);
								totalItemNetWeight+=Netweight;
							}
							
							
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) invDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) invDet.get("ACCOUNT_NAME"));
							//double discpercal = (Double.parseDouble(invDet.get("AMOUNT").toString()) * 100)/Double.parseDouble(subTotal);
							//double deductamt = (Double.parseDouble(orderdiscount.toString())/100)*discpercal;
							//journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())- deductamt);
							
							if(!orderdisctype.toString().equalsIgnoreCase("%")) {
								journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString())-Double.parseDouble(orderDiscountFrom.toString())/invoiceDetInfoList.size());
							}else {
								Double jodamt = (Double.parseDouble(invDet.get("AMOUNT").toString())/100)*Double.parseDouble(orderDiscountFrom.toString());
								journalDetail.setCREDITS(Double.parseDouble(invDet.get("AMOUNT").toString()) -jodamt);
							}
							
							/*
							 * String avg=new InvUtil().getCostOfGoods((String)invDet.get("ITEM"),plant);
							 * if(avg!=null && !"".equals(avg)) {
							 * totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }else { avg=new
							 * InvUtil().getAvgCostofItem((String)invDet.get("ITEM"),plant); if(avg!=null &&
							 * !"".equals(avg)) { totalCostofGoodSold+=Double.parseDouble(avg)*(quantity); }
							 * 
							 * }
							 */
							/**/
							int invoicesCount = new InvoiceDAO().invoiceWoCOGSCount((String)invDet.get("ITEM"), plant);
							if(invoicesCount == 1) {
								Map invDetail = new InvMstDAO().getInvDataByProduct((String)invDet.get("ITEM"), plant);
								double bill_qty = 0, invoiced_qty = 0;//inv_qty=0, unbill_qty = 0, 
//								inv_qty = Double.parseDouble((String)invDetail.get("INV_QTY"));
								bill_qty = Double.parseDouble((String)invDetail.get("BILL_QTY"));
//								unbill_qty = Double.parseDouble((String)invDetail.get("UNBILL_QTY"));
								invoiced_qty = Double.parseDouble((String)invDetail.get("INVOICE_QTY"));
								
								bill_qty = bill_qty - invoiced_qty;
								
								if(bill_qty >= quantity) {
							/**/
								ArrayList invQryList;
								Hashtable ht_cog = new Hashtable();
								ht_cog.put("a.PLANT",plant);
								ht_cog.put("a.ITEM",(String)invDet.get("ITEM"));
								invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOMLandedCost(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
								if(invQryList.isEmpty())
								{
									invQryList= new InvUtil().getInvListSummaryWithAverageCostWithUOM(ht_cog,"","",plant,(String)invDet.get("ITEM"),"",curency,curency,"","","","","");
								}
								if(invQryList!=null)
								{
									if(!invQryList.isEmpty())
									{
										Map lineArr = (Map) invQryList.get(0);
										String avg= StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("AVERAGE_COST")),"2");
										totalCostofGoodSold+=Double.parseDouble(avg)*(quantity);
									}
								}
								new InvoiceDAO().update_is_cogs_set((String)invDet.get("INVOICEHDRID"), (String)invDet.get("LNNO"), (String)invDet.get("ITEM"), plant);
								}
							}
							//totalCostofGoodSold+=Double.parseDouble(invDet.get("AMOUNT").toString());
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
						
						if(!shippingCost.isEmpty())
						{
							Double shippingCostFrom=Double.parseDouble(shippingCost);
							if(shippingCostFrom>0)
							{
								JournalDetail journalDetail_4=new JournalDetail();
								journalDetail_4.setPLANT(plant);
								JSONObject coaJson4=coaDAO.getCOAByName(plant, "Outward freight & shipping");
								journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
								journalDetail_4.setACCOUNT_NAME("Outward freight & shipping");
								journalDetail_4.setCREDITS(shippingCostFrom);
								journalDetails.add(journalDetail_4);
							}
						}
						
						if(taxamount.isEmpty())
						{
							taxamount="0.00";
						}
						Double taxAmountFrom=Double.parseDouble(taxamount);
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Output");
							journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
							journalDetail_2.setACCOUNT_NAME("VAT Output");*/
							
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
								
								Hashtable htMovHis = new Hashtable();
								htMovHis.put(IDBConstants.PLANT, plant);
								htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
								htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
								htMovHis.put(IDBConstants.ITEM, "");
								htMovHis.put(IDBConstants.QTY, "0.0");
								htMovHis.put("RECID", "");
								htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
								htMovHis.put(IDBConstants.CREATED_BY, username);		
								htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMovHis.put("REMARKS","");
								isAdded = movHisDao.insertIntoMovHis(htMovHis);
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
								isAdded = movHisDao.insertIntoMovHis(htMovHis);
							}
							
							//Cost of goods sold
							if(totalCostofGoodSold>0)
							{
								journalDetails.clear();
								journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
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
								
								Journal journalCOG=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
								if(journalCOG.getJournalHeader()!=null)
								{
									if(journalCOG.getJournalHeader().getID()>0)
									{
										journalHead.setID(journalCOG.getJournalHeader().getID());
										journalService.updateJournal(journal, username);
										Hashtable htMovHis = new Hashtable();
										htMovHis.put(IDBConstants.PLANT, plant);
										htMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
										htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
										htMovHis.put(IDBConstants.ITEM, "");
										htMovHis.put(IDBConstants.QTY, "0.0");
										htMovHis.put("RECID", "");
										htMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
										htMovHis.put(IDBConstants.CREATED_BY, username);		
										htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htMovHis.put("REMARKS","");
										isAdded = movHisDao.insertIntoMovHis(htMovHis);
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
										isAdded = movHisDao.insertIntoMovHis(htMovHis);
									}
								}
							}
						}
						
					}
				}
				
				

				if(isAdded) {
					if(!deductInv.equalsIgnoreCase("1") && !isexpense.equals("1") && !gino.equalsIgnoreCase("")) {
						Hashtable htgi = new Hashtable();	
						String sqlgi = "UPDATE "+ plant+"_FINGINOTOINVOICE SET STATUS='INVOICED' WHERE PLANT='"+ plant+"' AND GINO='"+gino+"'";
						invoiceDAO.updategino(sqlgi, htgi, "");
					}
				}
				
				if(isAdded) {
					for(int i =0 ; i < item.size() ; i++){
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					if(cmd.equalsIgnoreCase("Edit"))
						htMovHis.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);	
					else
					{
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_INVOICE);
						if(isexpense.equalsIgnoreCase("1"))
							htMovHis.put("DIRTYPE", TransactionConstants.EXPENSES_TO_INVOICE);
						if(cmd.equalsIgnoreCase("IssueingGoodsInvoice"))
							htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_INVOICE);
					}
					
						
					htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(invoiceDate));														
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
					htMovChk.put("DIRTYPE", TransactionConstants.EDIT_INVOICE);
					htMovChk.put(IDBConstants.ITEM, (String) item.get(i));
					htMovChk.put(IDBConstants.QTY, billqty);
					htMovChk.put(IDBConstants.MOVHIS_ORDNUM, invoiceNo);
					isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%INVOICE%' ");
					if(!isAdded)	
					isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
				}
			}
			
			
			
			//azees consignmentToInvoice Status & Qty Update
			if(!cmd.equalsIgnoreCase("Edit"))
			{
			if(jobNum.contains("C")) {
			for(int i=0;i<item.size();i++) {
				
//				int lnv=i+1;
			ToDetDAO _ToDetDAO = new ToDetDAO();
			ToHdrDAO _ToHdrDAO = new ToHdrDAO();
			String updateEstHdr = "",updateEstDet="";
			Hashtable htCondition = new Hashtable();
			htCondition.put("PLANT", plant);
			htCondition.put("TONO", jobNum);
			htCondition.put("TOLNNO", String.valueOf(tlnno.get(i)));
			int tllno=Integer.parseInt(((String)tlnno.get(i)));
			String issuingQty = (String)qty.get(i);
			ToDet toDet = new ToDet();
			toDet = _ToDetDAO.getToDetById(plant, jobNum, tllno, (String)item.get(i));
			
			//String issuedqty = (String)qtyIs.get(index);
			BigDecimal Ordqty = toDet.getQTYOR();
			BigDecimal tranQty = BigDecimal.valueOf(Double.parseDouble(issuingQty));
			BigDecimal issqty = BigDecimal.valueOf(Double.parseDouble("0"));
					if (toDet.getQTYAC() != null) {
						issqty =toDet.getQTYAC();
					}
			BigDecimal sumqty = issqty.add(tranQty);
			//sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			
			String extraCond = " AND  QtyOr >= isNull(QTYAC,0) + "+ issuingQty;
			if (Ordqty.compareTo(sumqty) == 0) {
				updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='C' ";

			} else {
				updateEstDet = "set QTYAC= isNull(QTYAC,0) + " + issuingQty  + ", RECSTAT='O' ";
			
			}
		
		boolean	insertFlag = _ToDetDAO.update(updateEstDet, htCondition, extraCond);
				
			if (insertFlag) {
				htCondition.remove("TOLNNO");
				
				insertFlag = _ToDetDAO.isExisit(htCondition,"RECSTAT in ('O','N')");
				if (!insertFlag){
					updateEstHdr = "set  RECSTAT='C',ORDER_STATUS='INVOICED' ";
					insertFlag = _ToHdrDAO.update(updateEstHdr, htCondition, "");
				}					
					
			
			}
			}
			}
			}
			
			if(isAdded) {
				DbBean.CommitTran(ut);

					ShipHisDAO shiphstdao = new ShipHisDAO();
					Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
					htTrandId1.put(IConstants.INVOICENO, invoiceNo);
					htTrandId1.put(IConstants.PLANT, plant);
//					boolean flag = 
							shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS	 						
					/*if(flag)
					{*/
					new TblControlUtil().updateTblControlIESeqNo(plant, "INVOICE", "IN", invoiceNo);
					/*}*/
					if(deductInv.equalsIgnoreCase("1")) {
						new TblControlUtil().updateTblControlIESeqNo(plant, "GINO", "GI", gino);
					}
					result = "ok";
					autoinverr="";
			}
			else {
				result = "not ok";
			}
			
		}catch (Exception e) {
			 e.printStackTrace();
			 autoinverr=e.toString();
			 result = "not ok";
		}			
	
		return result;
	
	}
	
	private void manualstocktake(HttpServletRequest request,
			HttpServletResponse response,String Loc) throws ServletException, IOException,
			Exception {
		

		try {
    	String login_user = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
    	String Mitem = "",Mloc = "",Mbatch = "",Mqty = "0",Mremark = "",Muom = "";
			String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(PLANT);
			if(pltCountry.equalsIgnoreCase("")) pltCountry="Asia/Singapore";
			String posPageToDirect = "../inhouse/manualstocktake";
			String msg = "Stock take qty could not be empty",STOCKID="",STKQTY="",INVFLAG="0",DIFFQTY="0",MUOM="";
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			StrUtils su =null;
			boolean flag = false;
			
			String baseCurrency = "";
			PlantMstDAO plantDAO = new PlantMstDAO();
			ArrayList plantDetList = plantDAO.getPlantMstDetails(PLANT);
			for (int i = 0; i < plantDetList.size(); i++) {
				Map plntdet = (Map) plantDetList.get(i);
				baseCurrency = (String) plntdet.get("BASE_CURRENCY");
			}
			String StockTakebyAvgCost = plantDAO.getStockTakebyAvgCost(PLANT);
			
//			int STKID = 0;
//			String[] chkstkid  = request.getParameterValues("CHKSTKID"); 
//			 for (String ID : chkstkid) {
//				 STKID = Integer.valueOf(ID);
			    	
//			ArrayList invQryList= new InvUtil().getManualStktakeByStatus(PLANT,"N",STKID);
			ArrayList invQryList= new InvUtil().getManualStktakeByStatusNloc(PLANT,"N",Loc); //with loc filter
			if(Loc.equalsIgnoreCase(""))
				invQryList= new InvUtil().getManualStktakeByStatus(PLANT,"N"); //without loc filter
			
			if (invQryList.size() > 0) {
				
				String grno = new TblControlDAO().getNextOrder(PLANT,login_user,"GRN");
				String gino = new TblControlDAO().getNextOrder(PLANT,login_user,"GINO");
				String stkno = new TblControlDAO().getNextOrder(PLANT,login_user,"STNO");
				
				int indxgrno =0;
				int indxgino =0;
				Double diffvalue = 0.0;
				for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
					
                  Map lineArr = (Map) invQryList.get(iCnt);
                  Mitem = StrUtils.fString((String) lineArr.get("ITEM"));
                  Mloc = StrUtils.fString((String) lineArr.get("LOC"));
                  Mbatch = StrUtils.fString((String) lineArr.get("BATCH"));
                  STOCKID = StrUtils.fString((String) lineArr.get("ID"));
                  STKQTY = StrUtils.fString((String) lineArr.get("QTY"));
                  INVFLAG = StrUtils.fString((String) lineArr.get("INVFLAG"));
                  DIFFQTY = StrUtils.fString((String) lineArr.get("DIFFQTY"));
                  MUOM = StrUtils.fString((String) lineArr.get("UOM"));
                  Mremark = StrUtils.fString((String) lineArr.get("REMARKS"));
                  String sDesc = new ItemMstDAO().getItemDesc(PLANT, Mitem);
                  String itemcost = "0";
					if(StockTakebyAvgCost.equalsIgnoreCase("1")) 
						itemcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(PLANT,MUOM,Mitem);
					else
						itemcost = new ItemMstDAO().getItemCost(PLANT, Mitem);
                  Mqty=STKQTY;
                  Double mul = -1.0;
                  Double convalue = mul*Double.valueOf(DIFFQTY);
                  System.out.println("---------");
                  System.out.println("---------");
                  System.out.println("differnce         "+Double.valueOf(DIFFQTY));
                  System.out.println("differnce         "+mul*Double.valueOf(DIFFQTY));
                  diffvalue = diffvalue+(convalue*Double.valueOf(itemcost));
                  System.out.println("---------");
                  System.out.println("---------");
                  
                  String INVQTY="0";
      			InvMstDAO  _InvMstDAO  = new InvMstDAO();
                  ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOMWithNegQty(PLANT,Mitem,Mloc,Mbatch,Muom);
                  if (listQry.size() > 0) {	               
               	   for(int i =0; i<listQry.size(); i++) {
                         Map arrCustLine = (Map)listQry.get(i);
                         INVQTY = (String)arrCustLine.get("qty");
               	   }
                  }
                  
                  double stk = Double.parseDouble(Mqty);
                  double invqty = Double.parseDouble(INVQTY);
                  double diff = (invqty - stk);
                  double olddiffqty = Double.parseDouble(DIFFQTY);
                  
                  if(INVFLAG.equalsIgnoreCase("1")){
                  	if(stk != invqty){
                  		stk = stk-(stk-invqty);
                  	}
                  	
                  }else{
                  	if((stk+olddiffqty) != invqty){
                  		stk=stk-((stk+olddiffqty)-invqty);
                  	}
                  }
                  
                  diff = (invqty - stk);
      			String diffval = StrUtils.addZeroes((diff), "3");
      			
      			if(stk==invqty)
      				INVFLAG="1";
      			else
      				INVFLAG="0";
      			
      			htInvMst.clear();
      			htInvMst.put(IDBConstants.PLANT, PLANT);
      			htInvMst.put(IDBConstants.ITEM, Mitem);
      			htInvMst.put(IDBConstants.LOC, Mloc);
      			htInvMst.put(IDBConstants.BATCH, Mbatch);
//      			htInvMst.put("STATUS", "N");
      			
      			//check for existence of item
      			boolean result=new ItemUtil().isExistsItemMst(Mitem, PLANT);
      			
      			//check for valid location
      			Hashtable htcdn = new Hashtable();
      			htcdn.put(IConstants.PLANT,PLANT);
      			htcdn.put(IDBConstants.LOC, Mloc);
      			boolean  result1=new LocMstDAO().isExisit(htcdn, "");
      			if(result==true&&result1==true){
      				flag = new InvMstDAO().isExisit(htInvMst, "");
      					
      					if(flag) {
      						        						
      						if(diff!=0) {
      						if(diff>0) {

        						indxgino =indxgino +1;
        						Hashtable htIssueDet = new Hashtable();
        						htIssueDet.put(IDBConstants.PLANT, PLANT);
        						htIssueDet.put(IDBConstants.DODET_DONUM, "");
        						htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
        						htIssueDet.put(IDBConstants.ITEM, Mitem);
        						htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
        						htIssueDet.put("BATCH", Mbatch);
        						htIssueDet.put(IDBConstants.LOC, Mloc);
        						htIssueDet.put("LOC1", Mloc);
        						htIssueDet.put("DOLNO", String.valueOf(indxgino));
        						htIssueDet.put("ORDQTY", diffval.replaceAll("-",""));
        						htIssueDet.put("PICKQTY", diffval.replaceAll("-",""));
        						htIssueDet.put("REVERSEQTY", "0");
        						htIssueDet.put("STATUS", "C");
        						htIssueDet.put("TRAN_TYPE", TransactionConstants.STOCK_TAKE);
        						htIssueDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
        						htIssueDet.put(IDBConstants.CREATED_BY, login_user);
        						htIssueDet.put(IDBConstants.ISSUEDATE, DateUtils.getDateinddmmyyyy(DateUtils.getDate()));
        						htIssueDet.put("REMARK", "");
        						htIssueDet.put("INVOICENO", gino);
        						htIssueDet.put(IDBConstants.CURRENCYID, baseCurrency);
        						htIssueDet.put("UNITPRICE", itemcost);
        						/*if(StockTakebyAvgCost.equalsIgnoreCase("1"))
        							htIssueDet.put("UNITPRICE", itemcost);
        						else
        							htIssueDet.put("UNITPRICE", new ItemMstDAO().getItemPrice(PLANT, Mitem));*/
        						flag = new ShipHisDAO().insertShipHis(htIssueDet);
        						
        						Hashtable htMovChk = new Hashtable();
        						htMovChk.clear();
        						htMovChk.put(IDBConstants.PLANT, PLANT);
        						htMovChk.put("DIRTYPE", TransactionConstants.STOCK_TAKE_OUT);
        						htMovChk.put(IDBConstants.ITEM, Mitem);
        						htMovChk.put(IDBConstants.QTY, diffval.replaceAll("-",""));
        						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, grno);
        						htMovChk.put(IDBConstants.LOC, Mloc);
        						htMovChk.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        						boolean isAdded = new MovHisDAO().isExisit(htMovChk,"");//fix duplicate on Movhis
        						if(!isAdded) {
        						
        						Hashtable htMovhis = new Hashtable();
        						htMovhis.clear();
        						htMovhis.put(IDBConstants.PLANT, PLANT);
        						htMovhis.put("DIRTYPE", TransactionConstants.STOCK_TAKE_OUT);
        						htMovhis.put(IDBConstants.ITEM, Mitem);
        						htMovhis.put("MOVTID", "OUT");
        						htMovhis.put("RECID", "");
        						htMovhis.put(IDBConstants.CUSTOMER_CODE, "");
        						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, gino);
        						htMovhis.put(IDBConstants.LOC, Mloc);
        						htMovhis.put(IDBConstants.CREATED_BY, login_user);
        						htMovhis.put("BATNO", Mbatch);
        						htMovhis.put("UOM", MUOM);
        						htMovhis.put("QTY", diffval.replaceAll("-",""));
        						htMovhis.put("USERFLD1", "");
        						htMovhis.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        						htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
        						htMovhis.put("REMARKS", "STOCK_OUT"+" "+stkno);
        						flag = new MovHisDAO().insertIntoMovHis(htMovhis);
        						
        						}
        						
      						} else {

      							indxgrno =indxgrno +1;
	        						Hashtable htRecvDet = new Hashtable();
	        						htRecvDet.put(IDBConstants.PLANT, PLANT);
	        						htRecvDet.put("PONO","");
	        						htRecvDet.put(IDBConstants.ITEM, Mitem);
	        						htRecvDet.put(IDBConstants.ITEM_DESC, sDesc);
	        						htRecvDet.put(IDBConstants.LOC, Mloc);
	        						htRecvDet.put("BATCH", Mbatch);
	        						htRecvDet.put("ORDQTY", diffval.replaceAll("-",""));
	        						htRecvDet.put("RECQTY", diffval.replaceAll("-",""));
	        						htRecvDet.put(IDBConstants.CURRENCYID, baseCurrency);
	        						//htRecvDet.put("UNITCOST", new ItemMstDAO().getItemCost(PLANT, Mitem));
	        						htRecvDet.put("UNITCOST", itemcost);
	        						htRecvDet.put("TRAN_TYPE", TransactionConstants.STOCK_TAKE);
	        						htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	        						htRecvDet.put(IDBConstants.CREATED_BY, login_user);
	        						htRecvDet.put("STATUS", "C");
	        						htRecvDet.put("GRNO", grno);
	        						htRecvDet.put(IDBConstants.RECVDATE, DateUtils.getDateinddmmyyyy(DateUtils.getDate()));
	        						htRecvDet.put(IDBConstants.LNNO, String.valueOf(indxgrno));
	        						flag = new RecvDetDAO().insertRecvDet(htRecvDet);
	        						
	        						Hashtable htMovChk = new Hashtable();
	        						htMovChk.clear();
	        						htMovChk.put(IDBConstants.PLANT, PLANT);
	        						htMovChk.put("DIRTYPE", TransactionConstants.STOCK_TAKE);
	        						htMovChk.put(IDBConstants.ITEM, Mitem);
	        						htMovChk.put(IDBConstants.QTY, diffval.replaceAll("-",""));
	        						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, grno);
	        						htMovChk.put(IDBConstants.LOC, Mloc);
	        						htMovChk.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
	        						boolean isAdded = new MovHisDAO().isExisit(htMovChk,"");//fix duplicate on Movhis
	        						if(!isAdded) {
	        						Hashtable htMovhis = new Hashtable();
	        						htMovhis.clear();
	        						htMovhis.put(IDBConstants.PLANT, PLANT);
	        						htMovhis.put("DIRTYPE", TransactionConstants.STOCK_TAKE);
	        						htMovhis.put(IDBConstants.ITEM, Mitem);
	        						htMovhis.put("MOVTID", "IN");
	        						htMovhis.put("RECID", "");
	        						htMovhis.put(IDBConstants.CUSTOMER_CODE, "");
	        						htMovhis.put(IDBConstants.MOVHIS_ORDNUM, grno);
	        						htMovhis.put(IDBConstants.LOC, Mloc);
	        						htMovhis.put(IDBConstants.CREATED_BY, login_user);
	        						htMovhis.put("BATNO", Mbatch);
	        						htMovhis.put("UOM", MUOM);
	        						htMovhis.put("QTY", diffval.replaceAll("-",""));
	        						htMovhis.put("USERFLD1", "");
	        						htMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
	        						htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	        						htMovhis.put("REMARKS", "STOCK_IN"+" "+stkno);
	        						flag = new MovHisDAO().insertIntoMovHis(htMovhis);
	        						}
	        						
      						}
      						}
      						
      						if (flag) {
      	          				
          					StringBuffer sql1 = new StringBuffer(" SET ");
          				    sql1.append(IDBConstants.QTY + " = '"+ stk + "', ");
          					sql1.append(IDBConstants.UPDATED_AT + " = '"+ DateUtils.getDateTime() + "', ");
          					sql1.append(IDBConstants.UPDATED_BY + " = '"+ login_user + "'");
          					flag = new InvMstDAO().update(sql1.toString(), htInvMst, "");
      						
      						StringBuffer sql = new StringBuffer(" SET ");
      						sql.append("" + IDBConstants.QTY + " = '" + stk + "',STATUS='C' ,STKNO='" + stkno + "',DIFFQTY='" + diffval + "',INVFLAG='" + INVFLAG + "',REMARKS='" + Mremark + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + login_user +"'");
      						htInvMst.clear();
      						htInvMst.put(IDBConstants.PLANT, PLANT);
      						htInvMst.put("ID", STOCKID);
      						flag = new StockTakeDAO().update(sql.toString(), htInvMst, "");
      						
      						msg = "Processed Successfully ";
      					}
      					
	        			} else {
	        				msg="Inventory Not Found for "+Mitem;
	        			}
      			} else {
      				msg="Not Valid "+Mitem+" or "+Mloc;
      			}
                  
				}
				
				System.out.println("dvalue=================="+diffvalue);
				
				JournalService journalService = new JournalEntry();
				CoaDAO coaDAO = new CoaDAO();
				Journal journal = new Journal();
				List<JournalDetail> journalDetails = new ArrayList<>();
				
				boolean isAccountinEnabled = new PlantMstUtil().isAccountingModuleEnabled(PLANT);
				if(isAccountinEnabled) {
				if (diffvalue >= 0) {
					JournalHeader journalHead = new JournalHeader();
					journalHead.setPLANT(PLANT);
					journalHead.setJOURNAL_DATE(DateUtils.getDate());
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(baseCurrency);
					journalHead.setTRANSACTION_TYPE("STOCKTAKE");
					//journalHead.setTRANSACTION_ID(gino+"_"+grno);
					journalHead.setTRANSACTION_ID(stkno);
					journalHead.setSUB_TOTAL(diffvalue);
					journalHead.setCRAT(DateUtils.getDateTime());
					journalHead.setCRBY(login_user);

					JournalDetail journalDetail_InvAsset = new JournalDetail();
					journalDetail_InvAsset.setPLANT(PLANT);
					JSONObject coaJson7 = coaDAO.getCOAByName(PLANT, "Inventory Asset");
					System.out.println("Json" + coaJson7.toString());
					journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
					journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
					journalDetail_InvAsset.setCREDITS(diffvalue);
					journalDetails.add(journalDetail_InvAsset);

					JournalDetail journalDetail_COG = new JournalDetail();
					journalDetail_COG.setPLANT(PLANT);
					JSONObject coaJson8 = coaDAO.getCOAByName(PLANT, "Inventory WO");
					System.out.println("Json" + coaJson8.toString());
					journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
					journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
					journalDetail_COG.setDEBITS(diffvalue);
					journalDetails.add(journalDetail_COG);

					journalHead.setTOTAL_AMOUNT(diffvalue);
					journal.setJournalHeader(journalHead);
					journal.setJournalDetails(journalDetails);

					journalService.addJournal(journal, login_user);
					Hashtable jhtMovHis = new Hashtable();
					jhtMovHis.put(IDBConstants.PLANT, PLANT);
					jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
					jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					jhtMovHis.put(IDBConstants.ITEM, "");
					jhtMovHis.put(IDBConstants.QTY, "0.0");
					jhtMovHis.put("RECID", "");
					jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE() + " "
							+ journal.getJournalHeader().getTRANSACTION_ID());
					jhtMovHis.put(IDBConstants.CREATED_BY, login_user);
					jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					jhtMovHis.put("REMARKS", "");
					new MovHisDAO().insertIntoMovHis(jhtMovHis);

				}else {
					diffvalue = -1.0 *diffvalue;
					JournalHeader journalHead = new JournalHeader();
					journalHead.setPLANT(PLANT);
					journalHead.setJOURNAL_DATE(DateUtils.getDate());
					journalHead.setJOURNAL_STATUS("PUBLISHED");
					journalHead.setJOURNAL_TYPE("Cash");
					journalHead.setCURRENCYID(baseCurrency);
					journalHead.setTRANSACTION_TYPE("STOCKTAKE");
					//journalHead.setTRANSACTION_ID(gino+"_"+grno);
					journalHead.setTRANSACTION_ID(stkno);
					journalHead.setSUB_TOTAL(diffvalue);
					journalHead.setCRAT(DateUtils.getDateTime());
					journalHead.setCRBY(login_user);
					
					JournalDetail journalDetail_COG = new JournalDetail();
					journalDetail_COG.setPLANT(PLANT);
					JSONObject coaJson8 = coaDAO.getCOAByName(PLANT,"Inventory Asset");
					System.out.println("Json" + coaJson8.toString());
					journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
					journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
					journalDetail_COG.setDEBITS(diffvalue);
					journalDetails.add(journalDetail_COG);


					JournalDetail journalDetail_InvAsset = new JournalDetail();
					journalDetail_InvAsset.setPLANT(PLANT);
					JSONObject coaJson7 = coaDAO.getCOAByName(PLANT, "Inventory WO");
					System.out.println("Json" + coaJson7.toString());
					journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
					journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
					journalDetail_InvAsset.setCREDITS(diffvalue);
					journalDetails.add(journalDetail_InvAsset);

					journalHead.setTOTAL_AMOUNT(diffvalue);
					journal.setJournalHeader(journalHead);
					journal.setJournalDetails(journalDetails);
					

					journalService.addJournal(journal, login_user);
					Hashtable jhtMovHis = new Hashtable();
					jhtMovHis.put(IDBConstants.PLANT, PLANT);
					jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);
					jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					jhtMovHis.put(IDBConstants.ITEM, "");
					jhtMovHis.put(IDBConstants.QTY, "0.0");
					jhtMovHis.put("RECID", "");
					jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM, journal.getJournalHeader().getTRANSACTION_TYPE() + " "
							+ journal.getJournalHeader().getTRANSACTION_ID());
					jhtMovHis.put(IDBConstants.CREATED_BY, login_user);
					jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					jhtMovHis.put("REMARKS", "");
					new MovHisDAO().insertIntoMovHis(jhtMovHis);
					
				}
			}
				
				
				
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", gino);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRNO", "GR", grno);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "STNO", "SK", stkno);
			}
//		}
			
			response.sendRedirect(posPageToDirect+"?result="+msg);
			
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		
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

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {

	}

}
