package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.EstimateAttachDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.estDet;
import com.track.db.object.estHdr;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.sqlBean;
import com.track.gates.userBean;
import com.track.service.EstDetService;
import com.track.service.EstHDRService;
import com.track.serviceImplementation.EstDetServiceImpl;
import com.track.serviceImplementation.EstHdrServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SalesEstimateServlet
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@WebServlet("/salesestimate/*")
public class SalesEstimateServlet extends EstimateServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
       
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));	
		if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit") 
				|| action.equalsIgnoreCase("copy")  ||action.equalsIgnoreCase("converttoestimate")) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String estno = "", orderType = "", delDate = "", custCode = "", custName = "", jobNum = "",
					personInCharge = "", contactNum = "", address = "", address2 = "", address3 = "", 
					collectionTime = "", remark1 = "", remark2 = "", shippingId = "", shippingCustomer = "",
					currencyId = "", deliveryDate = "", timeSlots = "", estimate_gst = "", status_id = "",
					empno = "", /* estno = "", */ remark3 = "", orderDiscount = "", shippingCost = "",
					incoterms = "", paymentType = "", deliverydateFormat = "0", approveStatus = "",
					sales_location = "", taxTreatment = "", currencyUseQty = "", orderstatus = "",
					discount = "", discount_type = "", adjustment = "", itemRates = "",projectid="",
					orderdiscstatus = "0",discstatus = "0",shipstatus = "0",taxid = "",orderdisctype = "",expiredate = "", shipcontactname="",
							shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",shipworkphone="",
							shipcountry="",shiphpno="",shipemail="";//collectionDate = "",
			String oldestno="";
			List<Hashtable<String,String>> salesAttachmentList = null;
			List<Hashtable<String,String>> salesAttachmentInfoList = null;
			/*ESTDet by navas*/
			List item = new ArrayList(), unitPrice = new ArrayList(), qtyOr = new ArrayList(),
					unitMo = new ArrayList(),
					prdDeliveryDate = new ArrayList(), accountName = new ArrayList(),
					taxType = new ArrayList(), item_discount = new ArrayList(),
					item_discountType = new ArrayList(), chkdDONO = new ArrayList();// proGst = new ArrayList(),
			int taxTypeCount = 0, chkdDONOCount = 0;/*itemCount = 0, unitPriceCount = 0, qtyOrCount = 0, unitMoCount = 0, 
					proGstCount = 0, prdDeliveryDateCount = 0,
					item_discountCount = 0, item_discountTypeCount = 0, accountNameCount = 0,*/
			salesAttachmentList = new ArrayList<Hashtable<String,String>>();
			salesAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			EstimateAttachDAO estimateAttachDAO = new EstimateAttachDAO();
			EstDetService estDetService = new EstDetServiceImpl();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
				List<estDet> estDetList = new ArrayList();
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					estHdr esthdr = new estHdr();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* ESTHDR by navas*/
							if (fileItem.getFieldName().equalsIgnoreCase("ESTNO")) {
								estno = StrUtils.fString(fileItem.getString()).trim();
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
								estimate_gst = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empno = StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								expiredate = StrUtils.fString(fileItem.getString()).trim();
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
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/"+ estno;
								String filetempLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/temp" + "/"+ estno;
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
					if(projectid.equalsIgnoreCase("")) {
						projectid = "0";
					}
					
					if(!orderdisctype.equalsIgnoreCase("%")) {
						discount = String.valueOf(Double.parseDouble(discount)/Double.parseDouble(currencyUseQty));
					}
					
					if(!discount_type.equalsIgnoreCase("%")) {
						orderDiscount = String.valueOf(Double.parseDouble(orderDiscount)/Double.parseDouble(currencyUseQty));
					}
					shippingCost = String.valueOf(Double.parseDouble(shippingCost)/Double.parseDouble(currencyUseQty));
					adjustment = String.valueOf(Double.parseDouble(adjustment)/Double.parseDouble(currencyUseQty));
					
					esthdr.setPLANT(plant);
					esthdr.setESTNO(estno);
					esthdr.setORDERTYPE(orderType);
					esthdr.setORDDATE(delDate);
					esthdr.setSTATUS("N");
					/* esthdr.setPickStaus("N"); */
					esthdr.setCRAT(DateUtils.getDateTime());
					esthdr.setCRBY(username);
					esthdr.setCustCode(custCode);
					esthdr.setCustName(custName);
					esthdr.setJobNum(jobNum);
					esthdr.setPersonInCharge(personInCharge);
					esthdr.setContactNum(contactNum);
					esthdr.setAddress(address);
					esthdr.setAddress2(address2);
					esthdr.setAddress3(address3);
					esthdr.setCollectionDate(DateUtils.getDate());
					esthdr.setCollectionTime(collectionTime);
					esthdr.setRemark1(remark1);
					esthdr.setRemark2(remark2);
					esthdr.setRemark3(remark3);
					esthdr.setSHIPPINGID(shippingId);
					esthdr.setSHIPPINGCUSTOMER(shippingCustomer);
					esthdr.setCURRENCYID(currencyId);
					esthdr.setDELIVERYDATE(deliveryDate);
					esthdr.setTIMESLOTS(timeSlots);
					esthdr.setOUTBOUND_GST(Double.parseDouble(estimate_gst));
					esthdr.setSTATUS_ID(status_id);
					esthdr.setEMPNO(empno);
					esthdr.setESTNO(estno);
					esthdr.setORDERDISCOUNT(Double.parseDouble(orderDiscount));
					esthdr.setSHIPPINGCOST(Double.parseDouble(shippingCost));
					esthdr.setINCOTERMS(incoterms);
					esthdr.setPAYMENTTYPE(paymentType);
					esthdr.setDELIVERYDATEFORMAT(Short.parseShort(deliverydateFormat));
					esthdr.setAPPROVESTATUS(approveStatus);
					esthdr.setSALES_LOCATION(sales_location);
					esthdr.setTAXTREATMENT(taxTreatment);
					esthdr.setORDER_STATUS(orderstatus);
					esthdr.setDISCOUNT(Double.parseDouble(discount));
					esthdr.setDISCOUNT_TYPE(discount_type);
					esthdr.setADJUSTMENT(Double.parseDouble(adjustment));
					esthdr.setITEM_RATES(Short.parseShort(itemRates));
					esthdr.setCURRENCYUSEQT(Double.valueOf(currencyUseQty));
					esthdr.setORDERDISCOUNTTYPE(orderdisctype);
					esthdr.setTAXID(Integer.valueOf(taxid));
					esthdr.setISDISCOUNTTAX(Short.parseShort(discstatus));
					esthdr.setISORDERDISCOUNTTAX(Short.parseShort(orderdiscstatus));
					esthdr.setISSHIPPINGTAX(Short.parseShort(shipstatus));
					esthdr.setPROJECTID(Integer.valueOf(projectid));
					esthdr.setEXPIREDATE(expiredate);
					esthdr.setSHIPCONTACTNAME(shipcontactname);
					esthdr.setSHIPDESGINATION(shipdesgination);
					esthdr.setSHIPWORKPHONE(shipworkphone);
					esthdr.setSHIPHPNO(shiphpno);
					esthdr.setSHIPEMAIL(shipemail);
					esthdr.setSHIPCOUNTRY(shipcountry);
					esthdr.setSHIPADDR1(shipaddr1);
					esthdr.setSHIPADDR2(shipaddr2);
					esthdr.setSHIPADDR3(shipaddr3);
					esthdr.setSHIPADDR4(shipaddr4);
					esthdr.setSHIPSTATE(shipstate);
					esthdr.setSHIPZIP(shipzip);
					
					/*if(action.equalsIgnoreCase("copy")){
						for(int i=0;i<chkdDONOCount;i++) {
							int index = Integer.parseInt((String)chkdDONO.get(i)) -1;
							int lnno = i+1;
							estDet estdet = new estDet();
							estdet.setPLANT(plant);
							estdet.setESTNO(estno);
							estdet.setESTLNNO(lnno);
							
							 * estdet.setPickStatus("N"); estdet.setLNSTAT("N");
							 
							estdet.setITEM((String)item.get(index));
							
							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, estdet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							estdet.setItemDesc(itemDesc);
							estdet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(index))/Double.parseDouble(currencyUseQty));
							String disctype = (String)item_discountType.get(index);
							double discountamount = Double.valueOf((String)item_discount.get(index));
							if(!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount/Double.parseDouble(currencyUseQty);
							}
							
							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									estdet.setUNITPRICE(Float.parseFloat("-"+itemunitprice));
								}
							}else {
								estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
							}	
							
							estdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(index))));
							estdet.setQTYIS(BigDecimal.valueOf(0));
							 estdet.setQtyPick(BigDecimal.valueOf(0)); 
							estdet.setUNITMO((String) unitMo.get(index));
							estdet.setCRAT(DateUtils.getDateTime());
							estdet.setCRBY(username);
							
							 * estdet.setUSERFLD1(itemDesc); estdet.setUSERFLD2(jobNum);
							 * estdet.setUSERFLD3(custName);
							 
							estdet.setCURRENCYUSEQT(Float.parseFloat((String)currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, estdet.getITEM());
								estdet.setPRODGST(Float.parseFloat((String)prodgst));
							}
							estdet.setACCOUNT_NAME((String)accountName.get(index));
							estdet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(index));
							estdet.setTAX_TYPE((String)taxType.get(index));
							estdet.setDISCOUNT(discountamount);
							estdet.setDISCOUNT_TYPE((String)item_discountType.get(index));
							
							estdet.setUPAT(DateUtils.getDateTime());
							estdet.setUPBY(username);
							
							estDetList.add(estdet);
						}
					}else {*/
						for(int i=0;i<item.size();i++) {
							int lnno = i+1;
							estDet estdet = new estDet();
							estdet.setPLANT(plant);
							estdet.setESTNO(estno);
							estdet.setESTLNNO(lnno);
							if(action.equalsIgnoreCase("edit")) {//Status update Azees
							estDet estdetold = estDetService.isgetestDetById(plant, estno, lnno, (String)item.get(i));
							if(estdetold.getITEM()!=null) {
							estdet.setQTYIS(estdetold.getQTYIS());
							if(estdetold.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyOr.get(i))) 
							{
								if(estdetold.getQTYIS().doubleValue()>0) 
								{
									estdet.setSTATUS("O");
								} else {
									estdet.setSTATUS("N");
								}
							} else {
								estdet.setSTATUS(estdetold.getSTATUS());
							}
							}
							else
							{
							 estdet.setSTATUS("N");
							 estdet.setQTYIS(BigDecimal.valueOf(0));
							}
							} else {
								 estdet.setSTATUS("N");
								 estdet.setQTYIS(BigDecimal.valueOf(0));
							}
							estdet.setITEM((String)item.get(i));
							
							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, estdet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							estdet.setItemDesc(itemDesc);
							estdet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(i))/Double.parseDouble(currencyUseQty));
							String disctype = (String)item_discountType.get(i);
							double discountamount = Double.valueOf((String)item_discount.get(i));
							if(!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount/Double.parseDouble(currencyUseQty);
							}
							
							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									estdet.setUNITPRICE(Float.parseFloat("-"+itemunitprice));
								}else {
									estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
								}
							}else {
								estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
							}	
							
							estdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(i))));
							
							/* estdet.setQtyPick(BigDecimal.valueOf(0)); */
							estdet.setUNITMO((String) unitMo.get(i));
							estdet.setCRAT(DateUtils.getDateTime());
							estdet.setCRBY(username);
							/*
							 * estdet.setUSERFLD1(itemDesc); estdet.setUSERFLD2(jobNum);
							 * estdet.setUSERFLD3(custName);
							 */
							estdet.setCURRENCYUSEQT(Float.parseFloat((String)currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, estdet.getITEM());
								estdet.setPRODGST(Float.parseFloat((String)prodgst));
							}
							estdet.setACCOUNT_NAME((String)accountName.get(i));
							estdet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(i));
							estdet.setTAX_TYPE((String)taxType.get(i));
							estdet.setDISCOUNT(discountamount);
							estdet.setDISCOUNT_TYPE((String)item_discountType.get(i));
							
							estdet.setUPAT(DateUtils.getDateTime());
							estdet.setUPBY(username);
							
							estDetList.add(estdet);
						}
					//}
					
					
					ut = DbBean.getUserTranaction();
					EstHDRService estHDRService = new EstHdrServiceImpl(); 
					
					
					ut.begin();
					
					if (action.equalsIgnoreCase("new") || action.equalsIgnoreCase("copy")  || action.equalsIgnoreCase("converttoestimate")) {
					Hashtable doht = new Hashtable();
					doht.put("estno", esthdr.getESTNO());
					doht.put("PLANT", esthdr.getPLANT());
					
					isOrderExists  = new EstHdrDAO().isExisit(doht);
					
//					resvi starts
					
					if(isOrderExists) {
						oldestno = esthdr.getESTNO();
						estno = new TblControlDAO().getNextOrder(plant, username, IConstants.ESTIMATE);
						esthdr.setESTNO(estno);
					}
//					resvi commands
					boolean insertFlag = estHDRService.addestHdr(esthdr);
//					if(insertFlag) {
//						insertFlag = estHDRService.addDoTransferHdr(esthdr);
//					}
					if(insertFlag) {
						insertFlag = estDetService.addestDet(estDetList);
					}
//					resvi commands

//					if(insertFlag) {
//						insertFlag = estDetService.addDoTransferDet(estDetList);
//					}
					if(insertFlag) {
						if(action.equalsIgnoreCase("converttoestimate")) {
							for(int i=0;i<chkdDONOCount;i++) {
								int index = Integer.parseInt((String)chkdDONO.get(i)) -1;
								int estlno = i+1;
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.ESTDET_DONUM, estno);
								htRemarks.put(IDBConstants.ESTDET_ESTLNNO, Integer.toString(estlno));
								htRemarks.put(IDBConstants.ESTDET_ITEM, (String)item.get(index));
								if(!new EstDetDAO().isExisitDoMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									insertFlag = new ESTUtil().saveDoMultiRemarks(htRemarks);
								}
							}
								/* CREATED BY NAVAS */
							}else {
								for(int i=0;i<item.size();i++) {
									int estlno = i+1;
									Hashtable<String, String> htRemarks = new Hashtable<>();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.ESTDET_DONUM, estno);
									htRemarks.put(IDBConstants.ESTDET_ESTLNNO, Integer.toString(estlno));
									htRemarks.put(IDBConstants.ESTDET_ITEM, (String)item.get(i));
									if(!new EstDetDAO().isExisitDoMultiRemarks(htRemarks)) {
										htRemarks.put(IDBConstants.REMARKS, "");
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, username);
										insertFlag = new ESTUtil().saveDoMultiRemarks(htRemarks);
									}
								}	
							}
						
						
					}
					if(insertFlag) {
						int attchSize = salesAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable salesAttachmentat = new Hashtable<String, String>();
								salesAttachmentat = salesAttachmentList.get(i);
								salesAttachmentat.put("ESTNO", estno);
								salesAttachmentInfoList.add(salesAttachmentat);
							}
							insertFlag = estimateAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
						}
					}
					if(insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," 
							+ StrUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						
						for (estDet estorderdet : estDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.EST_ADD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, esthdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, esthdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, estorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, estorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
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
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.ESTIMATE,"SE",estno);
						DbBean.CommitTran(ut);
						if(isOrderExists) {
//							resvi change sales to estimate
							resultJson.put("MESSAGE", "Estimate order " + oldestno + 
									" has already been used. System has auto created a new Estimate order " + estno +
									" for you.");
						}else {
							resultJson.put("MESSAGE", "Estimate Order Created Successfully.");
						}						
						resultJson.put("ERROR_CODE", "100");	
						String message = "Sales Estimate Added Successfully.";
						if (ajax) {
							request.setAttribute("ESTNO", esthdr.getESTNO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
							// TODO: Send proper value in action parameter
							if ("estimate".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
//								System.out.println("Generating EST Report");
								viewESTReport(request, response);
							}
							// viewDOReport(request, response, "printPO");
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						} 
						String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant, IConstants.ESTIMATE_ORDER);
						if (isAutoEmail.equalsIgnoreCase("Y"))
							new EmailMsgUtil().sendEmail(plant, estno, IConstants.ESTIMATE_ORDER);
					}else {
						DbBean.RollbackTran(ut);
						String message = "Failed to Create Estimate Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}
						resultJson.put("MESSAGE",  "Failed to Create Estimate Order.");
				        resultJson.put("ERROR_CODE", "98");
					}
				}else if(action.equalsIgnoreCase("edit")) {
					esthdr.setORDER_STATUS("Open");
					esthdr.setUPAT(DateUtils.getDateTime());
					esthdr.setUPBY(username);					
					boolean updateFlag = estHDRService.updateestHdr(esthdr);
					
//					resvi commands
//					if(updateFlag) {
//						updateFlag = EstHDRService.updateDoTransferHdr(doHdr);
//					}
					
					if(updateFlag) {
						Hashtable ht = new Hashtable();
						ht.put("ESTNO", esthdr.getESTNO());
						ht.put("PLANT", plant);
						updateFlag = new EstDetDAO().delete(ht);
						
						if(updateFlag)
							updateFlag = estDetService.addestDet( estDetList);
					}
					if(updateFlag) {
						int attchSize = salesAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable salesAttachmentat = new Hashtable<String, String>();
								salesAttachmentat = salesAttachmentList.get(i);
								salesAttachmentat.put("ESTNO", estno);
								salesAttachmentInfoList.add(salesAttachmentat);
							}
							updateFlag = estimateAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
						}
					}
					//ORDER_STATUS Update - Azees 3.21
					if(!orderstatus.equalsIgnoreCase("Draft")) {
					if(updateFlag) {
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("ESTNO", esthdr.getESTNO());					
					updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('O','N')");
					if (!updateFlag){
						String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
						updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
					} else {
						updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('O')");
						if (!updateFlag){
							updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('C','N')");
							if (!updateFlag){
								String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
								updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");	
							} else {
								updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('C')");
								if (!updateFlag){
									String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
									updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
							} else {
								String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
								updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
							}
						} 
						}else {
							String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
							updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");	
						}						
						}
					}
					}
					if(updateFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," 
							+ StrUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						
						for (estDet estorderdet : estDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.EST_UPD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, esthdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, esthdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, estorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, estorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.TRAN_DATE,
									DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
							
						}
					}
					if(updateFlag) {
						DbBean.CommitTran(ut);
						resultJson.put("MESSAGE", "Sales Estimate Updated Successfully.");
						resultJson.put("ERROR_CODE", "100");
						if (ajax) {
							request.setAttribute("ESTNO", esthdr.getESTNO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
							// TODO: Send proper value in action parameter
							if ("estimate".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
//								System.out.println("Generating EST Report");
								viewESTReport(request, response);
							}
						}
					}else {
						DbBean.RollbackTran(ut);
					}
				}
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  ThrowableUtil.getMessage(e));
		        resultJson.put("ERROR_CODE", "98");
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} else  if(action.equalsIgnoreCase("new1") || action.equalsIgnoreCase("edit1") || action.equalsIgnoreCase("copy1") ) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			ItemUtil itemUtil = new ItemUtil();
//			DateUtils dateutils= new DateUtils();
			String estno = "", orderType = "", delDate = "", custCode = "", custName = "", jobNum = "",
					personInCharge = "", contactNum = "", address = "", address2 = "", address3 = "",
					collectionTime = "", remark1 = "", remark2 = "", shippingId = "", shippingCustomer = "",
					currencyId = "", deliveryDate = "", timeSlots = "", estimate_gst = "", status_id = "",
					empno = "", /* estno = "", */ remark3 = "", orderDiscount = "", shippingCost = "",
					incoterms = "", paymentType = "", deliverydateFormat = "0", approveStatus = "",
					sales_location = "", taxTreatment = "", currencyUseQty = "", orderstatus = "",
					discount = "", discount_type = "", adjustment = "", itemRates = "",projectid="",
					orderdiscstatus = "0",discstatus = "0",shipstatus = "0",taxid = "",orderdisctype = "",expiredate = "";// collectionDate = "",
			String oldestno="";
			List<Hashtable<String,String>> salesAttachmentList = null;
			List<Hashtable<String,String>> salesAttachmentInfoList = null;
			/*ESTDet by navas*/
			List item = new ArrayList(), unitPrice = new ArrayList(), qtyOr = new ArrayList(),
					unitMo = new ArrayList(), 
					prdDeliveryDate = new ArrayList(), accountName = new ArrayList(),
					taxType = new ArrayList(), item_discount = new ArrayList(),
					item_discountType = new ArrayList(), chkdDONO = new ArrayList();//proGst = new ArrayList(),
			int taxTypeCount = 0, chkdDONOCount = 0;/*, itemCount = 0, unitPriceCount = 0, qtyOrCount = 0, unitMoCount = 0, 
					proGstCount = 0, prdDeliveryDateCount = 0, 
					item_discountCount = 0, item_discountTypeCount = 0, accountNameCount = 0,*/
			salesAttachmentList = new ArrayList<Hashtable<String,String>>();
			salesAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			EstimateAttachDAO estimateAttachDAO = new EstimateAttachDAO();
			EstDetService estDetService = new EstDetServiceImpl();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
				List<estDet> estDetList = new ArrayList();
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					estHdr esthdr = new estHdr();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* ESTHDR by navas*/
							if (fileItem.getFieldName().equalsIgnoreCase("ESTNO")) {
								estno = StrUtils.fString(fileItem.getString()).trim();
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
								estimate_gst = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empno = StrUtils.fString(fileItem.getString()).trim();
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
								
								deliverydateFormat = (deliverydateFormat.equals("")) ? "0" : "1";
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SALES_LOC")) {
								sales_location = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TAXTREATMENT")) {
								taxTreatment = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								orderstatus = StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								expiredate = StrUtils.fString(fileItem.getString()).trim();
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
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/"+ estno;
								String filetempLocationATT = "C:/ATTACHMENTS/ESTIMATE" + "/temp" + "/"+ estno;
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
					if(projectid.equalsIgnoreCase("")) {
						projectid = "0";
					}
					
					if(!orderdisctype.equalsIgnoreCase("%")) {
						discount = String.valueOf(Double.parseDouble(discount)/Double.parseDouble(currencyUseQty));
					}
					
					if(!discount_type.equalsIgnoreCase("%")) {
						orderDiscount = String.valueOf(Double.parseDouble(orderDiscount)/Double.parseDouble(currencyUseQty));
					}
					shippingCost = String.valueOf(Double.parseDouble(shippingCost)/Double.parseDouble(currencyUseQty));
					adjustment = String.valueOf(Double.parseDouble(adjustment)/Double.parseDouble(currencyUseQty));
					
					esthdr.setPLANT(plant);
					esthdr.setESTNO(estno);
					esthdr.setORDERTYPE(orderType);
					esthdr.setORDDATE(delDate);
					esthdr.setSTATUS("N");
					/* esthdr.setPickStaus("N"); */
					esthdr.setCRAT(DateUtils.getDateTime());
					esthdr.setCRBY(username);
					esthdr.setCustCode(custCode);
					esthdr.setCustName(custName);
					esthdr.setJobNum(jobNum);
					esthdr.setPersonInCharge(personInCharge);
					esthdr.setContactNum(contactNum);
					esthdr.setAddress(address);
					esthdr.setAddress2(address2);
					esthdr.setAddress3(address3);
					esthdr.setCollectionDate(DateUtils.getDate());
					esthdr.setCollectionTime(collectionTime);
					esthdr.setRemark1(remark1);
					esthdr.setRemark2(remark2);
					esthdr.setRemark3(remark3);
					esthdr.setSHIPPINGID(shippingId);
					esthdr.setSHIPPINGCUSTOMER(shippingCustomer);
					esthdr.setCURRENCYID(currencyId);
					esthdr.setDELIVERYDATE(deliveryDate);
					esthdr.setTIMESLOTS(timeSlots);
					esthdr.setOUTBOUND_GST(Double.parseDouble(estimate_gst));
					esthdr.setSTATUS_ID(status_id);
					esthdr.setEMPNO(empno);
					esthdr.setESTNO(estno);
					esthdr.setORDERDISCOUNT(Double.parseDouble(orderDiscount));
					esthdr.setSHIPPINGCOST(Double.parseDouble(shippingCost));
					esthdr.setINCOTERMS(incoterms);
					esthdr.setPAYMENTTYPE(paymentType);
					esthdr.setDELIVERYDATEFORMAT(Short.parseShort(deliverydateFormat));
					esthdr.setAPPROVESTATUS(approveStatus);
					esthdr.setSALES_LOCATION(sales_location);
					esthdr.setTAXTREATMENT(taxTreatment);
					esthdr.setORDER_STATUS(orderstatus);
					esthdr.setDISCOUNT(Double.parseDouble(discount));
					esthdr.setDISCOUNT_TYPE(discount_type);
					esthdr.setADJUSTMENT(Double.parseDouble(adjustment));
					esthdr.setITEM_RATES(Short.parseShort(itemRates));
					esthdr.setCURRENCYUSEQT(Double.valueOf(currencyUseQty));
					esthdr.setORDERDISCOUNTTYPE(orderdisctype);
					esthdr.setTAXID(Integer.valueOf(taxid));
					esthdr.setISDISCOUNTTAX(Short.parseShort(discstatus));
					esthdr.setISORDERDISCOUNTTAX(Short.parseShort(orderdiscstatus));
					esthdr.setISSHIPPINGTAX(Short.parseShort(shipstatus));
					esthdr.setPROJECTID(Integer.valueOf(projectid));
					esthdr.setEXPIREDATE(expiredate);
					
					/*if(action.equalsIgnoreCase("copy")){
						for(int i=0;i<chkdDONOCount;i++) {
							int index = Integer.parseInt((String)chkdDONO.get(i)) -1;
							int lnno = i+1;
							estDet estdet = new estDet();
							estdet.setPLANT(plant);
							estdet.setESTNO(estno);
							estdet.setESTLNNO(lnno);
							
							 * estdet.setPickStatus("N"); estdet.setLNSTAT("N");
							 
							estdet.setITEM((String)item.get(index));
							
							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, estdet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							estdet.setItemDesc(itemDesc);
							estdet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(index))/Double.parseDouble(currencyUseQty));
							String disctype = (String)item_discountType.get(index);
							double discountamount = Double.valueOf((String)item_discount.get(index));
							if(!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount/Double.parseDouble(currencyUseQty);
							}
							
							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									estdet.setUNITPRICE(Float.parseFloat("-"+itemunitprice));
								}
							}else {
								estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
							}	
							
							estdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(index))));
							estdet.setQTYIS(BigDecimal.valueOf(0));
							 estdet.setQtyPick(BigDecimal.valueOf(0)); 
							estdet.setUNITMO((String) unitMo.get(index));
							estdet.setCRAT(DateUtils.getDateTime());
							estdet.setCRBY(username);
							
							 * estdet.setUSERFLD1(itemDesc); estdet.setUSERFLD2(jobNum);
							 * estdet.setUSERFLD3(custName);
							 
							estdet.setCURRENCYUSEQT(Float.parseFloat((String)currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, estdet.getITEM());
								estdet.setPRODGST(Float.parseFloat((String)prodgst));
							}
							estdet.setACCOUNT_NAME((String)accountName.get(index));
							estdet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(index));
							estdet.setTAX_TYPE((String)taxType.get(index));
							estdet.setDISCOUNT(discountamount);
							estdet.setDISCOUNT_TYPE((String)item_discountType.get(index));
							
							estdet.setUPAT(DateUtils.getDateTime());
							estdet.setUPBY(username);
							
							estDetList.add(estdet);
						}
					}else {*/
						for(int i=0;i<item.size();i++) {
							int lnno = i+1;
							estDet estdet = new estDet();
							estdet.setPLANT(plant);
							estdet.setESTNO(estno);
							estdet.setESTLNNO(lnno);
							if(action.equalsIgnoreCase("edit")) {//Status update Azees
							estDet estdetold = estDetService.isgetestDetById(plant, estno, lnno, (String)item.get(i));
							if(estdetold.getITEM()!=null) {
							estdet.setQTYIS(estdetold.getQTYIS());
							if(estdetold.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyOr.get(i))) 
							{
								if(estdetold.getQTYIS().doubleValue()>0) 
								{
									estdet.setSTATUS("O");
								} else {
									estdet.setSTATUS("N");
								}
							} else {
								estdet.setSTATUS(estdetold.getSTATUS());
							}
							}
							else
							{
							 estdet.setSTATUS("N");
							 estdet.setQTYIS(BigDecimal.valueOf(0));
							}
							} else {
								 estdet.setSTATUS("N");
								 estdet.setQTYIS(BigDecimal.valueOf(0));
							}
							estdet.setITEM((String)item.get(i));
							
							
							String nonstocktype = "";
							String nonstocktypeDesc = "";
							String itemDesc = "";

							String sItem = (String)item.get(i);
							if(!(itemUtil.isExistsItemMst(sItem,plant))) // if the item exists already
						    {
				                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumericWithSpace(sItem);
				                        if(sItem.equals("")){
				                        	DbBean.RollbackTran(ut);
				                            throw new Exception("");
				                    }
				                        System.out.println(specialcharsnotAllowed.length());
				                        if(specialcharsnotAllowed.length()>0){
				                        	 DbBean.RollbackTran(ut);
				                                throw new Exception("Product ID  value : '" + sItem + "' has special characters "+specialcharsnotAllowed+" that are  not allowed ");
				                        }
				                        
				                        /*if(loc.length()>0){
				                        boolean isExistLoc = false;
				    					LocUtil locUtil = new LocUtil();
				    					isExistLoc = locUtil.isValidLocInLocmst(plant, loc);
				    					if(!isExistLoc){
				    						throw new Exception("Location: " + loc + " is not valid location");
				    						}
				                        }*/
						          String sItemDesc=StrUtils.InsertQuotes(sItem);
						          Hashtable ht = new Hashtable();
						          ht.put(IConstants.PLANT,plant);
						          ht.put(IConstants.ITEM,sItem);
						          ht.put(IConstants.ITEM_DESC,sItemDesc);
						          ht.put("STKUOM","PCS");
						          ht.put("USERFLD1", "N");
						          ht.put(IConstants.ISACTIVE,"Y");
						          ht.put(IConstants.NONSTKFLAG, "Y");
						          ht.put("PURCHASEUOM","PCS");
							      ht.put("SALESUOM","PCS");
							      ht.put("RENTALUOM","PCS");
							      ht.put("SERVICEUOM","PCS");
							      ht.put("INVENTORYUOM","PCS");
							      ht.put("ISBASICUOM","1");
							      ht.put(IDBConstants.STKQTY, "0");
							      ht.put(IDBConstants.MAXSTKQTY, "0");
							      ht.put("CRBY",username);
						          ht.put("CRAT",DateUtils.getDateTime());
						          ht.put(IConstants.PRDCLSID,"");
						          
						          
						         
						          String remark = sItem+" Create Estimate Order";
						          MovHisDAO mdao = new MovHisDAO(plant);
						          mdao.setmLogger(mLogger);
						          Hashtable htm = new Hashtable();
						          htm.put("PLANT",plant);
						          htm.put("DIRTYPE",TransactionConstants.ADD_ITEM);
						          htm.put("RECID","");
						          htm.put("ITEM",sItem);
						          htm.put("LOC","");
						          htm.put(IDBConstants.REMARKS,StrUtils.InsertQuotes(remark));  
						          htm.put("CRBY",username);
						          htm.put("CRAT",DateUtils.getDateTime());
						          htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));  
						          boolean  inserted=false; 
						          boolean  insertAlternateItem = false; 
						          boolean itemInserted = itemUtil.insertItem(ht);
						          { 
						        	  if(itemInserted) {
							        	 String alternateItemName = sItem; 
							           	 List<String> alternateItemNameLists = new ArrayList<String>();
							        	 alternateItemNameLists.add(alternateItemName);
							        	 insertAlternateItem = itemUtil.insertAlternateItemLists(plant, sItem, alternateItemNameLists);
							          }
							          if(itemInserted && insertAlternateItem) {
							          	 inserted = mdao.insertIntoMovHis(htm);
							          }
							          
							          htm.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
						              htm.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						              
//						    		  boolean updateFlag;
						    		/*if(sItem!="P0001")
						      		  {	
						    			boolean exitFlag = false;
						    			Hashtable htv = new Hashtable();				
						    			htv.put(IDBConstants.PLANT, plant);
						    			htv.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
						    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
						    			if (exitFlag) 
						      		    updateFlag=_TblControlDAO.updateSeqNo("PRODUCT",plant);
						    			else
						    			{
						    				boolean insertFlag = false;
						    				Map htInsert=null;
						                	Hashtable htTblCntInsert  = new Hashtable();           
						                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
						                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
						                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
						                 	htTblCntInsert.put("MINSEQ","0000");
						                 	htTblCntInsert.put("MAXSEQ","9999");
						                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
						                	htTblCntInsert.put(IDBConstants.CREATED_BY, username);
						                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)DateUtils.getDateTime());
						                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
						    			}
						    		}*/
							          
						          	sItemDesc=StrUtils.RemoveDoubleQuotesToSingle(sItemDesc);
						          }
						          if(itemInserted&&inserted) {
						             
						          } else {
				                      DbBean.RollbackTran(ut);
						
						          }
						          
						          nonstocktype = "Y";
						          nonstocktypeDesc = "";
						          itemDesc = sItem;
						   
						    }else {
						    	Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, estdet.getITEM());
								nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
						    }
							
							
							
							estdet.setItemDesc(itemDesc);
							estdet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(i))/Double.parseDouble(currencyUseQty));
							String disctype = (String)item_discountType.get(i);
							double discountamount = Double.valueOf((String)item_discount.get(i));
							if(!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount/Double.parseDouble(currencyUseQty);
							}
							
							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									estdet.setUNITPRICE(Float.parseFloat("-"+itemunitprice));
								}else {
									estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
								}
							}else {
								estdet.setUNITPRICE(Float.parseFloat(itemunitprice));
							}	
							
							estdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(i))));
							
							/* estdet.setQtyPick(BigDecimal.valueOf(0)); */
							estdet.setUNITMO((String) unitMo.get(i));
							estdet.setCRAT(DateUtils.getDateTime());
							estdet.setCRBY(username);
							/*
							 * estdet.setUSERFLD1(itemDesc); estdet.setUSERFLD2(jobNum);
							 * estdet.setUSERFLD3(custName);
							 */
							estdet.setCURRENCYUSEQT(Float.parseFloat((String)currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, estdet.getITEM());
								estdet.setPRODGST(Float.parseFloat((String)prodgst));
							}
							estdet.setACCOUNT_NAME((String)accountName.get(i));
							estdet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(i));
							estdet.setTAX_TYPE((String)taxType.get(i));
							estdet.setDISCOUNT(discountamount);
							estdet.setDISCOUNT_TYPE((String)item_discountType.get(i));
							
							estdet.setUPAT(DateUtils.getDateTime());
							estdet.setUPBY(username);
							
							estDetList.add(estdet);
						}
					//}
					
					
					ut = DbBean.getUserTranaction();
					EstHDRService estHDRService = new EstHdrServiceImpl(); 
					
					
					ut.begin();
					
					if (action.equalsIgnoreCase("new1") || action.equalsIgnoreCase("copy1")) {
					Hashtable doht = new Hashtable();
					doht.put("estno", esthdr.getESTNO());
					doht.put("PLANT", esthdr.getPLANT());
					
					isOrderExists  = new EstHdrDAO().isExisit(doht);
					
//					resvi starts
					
					if(isOrderExists) {
						oldestno = esthdr.getESTNO();
						estno = new TblControlDAO().getNextOrder(plant, username, IConstants.ESTIMATE);
						esthdr.setESTNO(estno);
					}
//					resvi commands
					boolean insertFlag = estHDRService.addestHdr(esthdr);
//					if(insertFlag) {
//						insertFlag = estHDRService.addDoTransferHdr(esthdr);
//					}
					if(insertFlag) {
						insertFlag = estDetService.addestDet(estDetList);
					}
//					resvi commands

//					if(insertFlag) {
//						insertFlag = estDetService.addDoTransferDet(estDetList);
//					}
					if(insertFlag) {
						if(action.equalsIgnoreCase("converttoestimate")) {
							for(int i=0;i<chkdDONOCount;i++) {
								int index = Integer.parseInt((String)chkdDONO.get(i)) -1;
								int estlno = i+1;
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.ESTDET_DONUM, estno);
								htRemarks.put(IDBConstants.ESTDET_ESTLNNO, Integer.toString(estlno));
								htRemarks.put(IDBConstants.ESTDET_ITEM, (String)item.get(index));
								if(!new EstDetDAO().isExisitDoMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									insertFlag = new ESTUtil().saveDoMultiRemarks(htRemarks);
								}
							}
								/* CREATED BY NAVAS */
							}else {
								for(int i=0;i<item.size();i++) {
									int estlno = i+1;
									Hashtable<String, String> htRemarks = new Hashtable<>();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.ESTDET_DONUM, estno);
									htRemarks.put(IDBConstants.ESTDET_ESTLNNO, Integer.toString(estlno));
									htRemarks.put(IDBConstants.ESTDET_ITEM, (String)item.get(i));
									if(!new EstDetDAO().isExisitDoMultiRemarks(htRemarks)) {
										htRemarks.put(IDBConstants.REMARKS, "");
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, username);
										insertFlag = new ESTUtil().saveDoMultiRemarks(htRemarks);
									}
								}	
							}
						
						
					}
					if(insertFlag) {
						int attchSize = salesAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable salesAttachmentat = new Hashtable<String, String>();
								salesAttachmentat = salesAttachmentList.get(i);
								salesAttachmentat.put("ESTNO", estno);
								salesAttachmentInfoList.add(salesAttachmentat);
							}
							insertFlag = estimateAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
						}
					}
					if(insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," 
							+ StrUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						insertFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						
						for (estDet estorderdet : estDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.EST_ADD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, esthdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, esthdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, estorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, estorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
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
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.ESTIMATE,"SE",estno);
						DbBean.CommitTran(ut);
						if(isOrderExists) {
//							resvi change sales to estimate
							resultJson.put("MESSAGE", "Estimate order " + oldestno + 
									" has already been used. System has auto created a new Estimate order " + estno +
									" for you.");
						}else {
							resultJson.put("MESSAGE", "Estimate Order Created Successfully.");
						}						
						resultJson.put("ERROR_CODE", "100");	
						String message = "Sales Estimate Added Successfully.";
						if (ajax) {
							request.setAttribute("ESTNO", esthdr.getESTNO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
							// TODO: Send proper value in action parameter
							if ("estimate".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
//								System.out.println("Generating EST Report");
								viewESTReport(request, response);
							}
							// viewDOReport(request, response, "printPO");
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						} 
						String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant, IConstants.ESTIMATE_ORDER);
						if (isAutoEmail.equalsIgnoreCase("Y"))
							new EmailMsgUtil().sendEmail(plant, estno, IConstants.ESTIMATE_ORDER);
					}else {
						DbBean.RollbackTran(ut);
						String message = "Failed to Create Estimate Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "98");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}
						resultJson.put("MESSAGE",  "Failed to Create Estimate Order.");
				        resultJson.put("ERROR_CODE", "98");
					}
				}else if(action.equalsIgnoreCase("edit1")) {
					esthdr.setORDER_STATUS("Open");
					esthdr.setUPAT(DateUtils.getDateTime());
					esthdr.setUPBY(username);					
					boolean updateFlag = estHDRService.updateestHdr(esthdr);
					
//					resvi commands
//					if(updateFlag) {
//						updateFlag = EstHDRService.updateDoTransferHdr(doHdr);
//					}
					
					if(updateFlag) {
						Hashtable ht = new Hashtable();
						ht.put("ESTNO", esthdr.getESTNO());
						ht.put("PLANT", plant);
						updateFlag = new EstDetDAO().delete(ht);
						
						if(updateFlag)
							updateFlag = estDetService.addestDet( estDetList);
					}
					if(updateFlag) {
						int attchSize = salesAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable salesAttachmentat = new Hashtable<String, String>();
								salesAttachmentat = salesAttachmentList.get(i);
								salesAttachmentat.put("ESTNO", estno);
								salesAttachmentInfoList.add(salesAttachmentat);
							}
							updateFlag = estimateAttachDAO.addsalesAttachments(salesAttachmentInfoList, plant);
						}
					}
					//ORDER_STATUS Update - Azees 3.21
					if(!orderstatus.equalsIgnoreCase("Draft")) {
					if(updateFlag) {
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("ESTNO", esthdr.getESTNO());					
					updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('O','N')");
					if (!updateFlag){
						String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
						updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
					} else {
						updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('O')");
						if (!updateFlag){
							updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('C','N')");
							if (!updateFlag){
								String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
								updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");	
							} else {
								updateFlag = new EstDetDAO().isExisit(htCondition,"STATUS in ('C')");
								if (!updateFlag){
									String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
									updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
							} else {
								String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
								updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");
							}
						} 
						}else {
							String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
							updateFlag = new EstHdrDAO().update(updateHdr, htCondition, "");	
						}						
						}
					}
					}
					if(updateFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_EST);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," 
							+ StrUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
						
						for (estDet estorderdet : estDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.EST_UPD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, esthdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, esthdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, estorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, estorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, estno);
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.TRAN_DATE,
									DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							updateFlag = new MovHisDAO().insertIntoMovHis(htRecvHis);
							
						}
					}
					if(updateFlag) {
						DbBean.CommitTran(ut);
						resultJson.put("MESSAGE", "Sales Estimate Updated Successfully.");
						resultJson.put("ERROR_CODE", "100");
						if (ajax) {
							request.setAttribute("ESTNO", esthdr.getESTNO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
							// TODO: Send proper value in action parameter
							if ("estimate".equals(emailMsg.get(IDBConstants.SEND_ATTACHMENT))) {
//								System.out.println("Generating EST Report");
								viewESTReport(request, response);
							}
						}
					}else {
						DbBean.RollbackTran(ut);
					}
				}
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  ThrowableUtil.getMessage(e));
		        resultJson.put("ERROR_CODE", "98");
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if (action.equalsIgnoreCase("convertestimate")) {
			try {
				String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				
				String deldate = DateUtils.getDate();
				String collectionTime = DateUtils.getTimeHHmm();
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String msg = StrUtils.fString(request.getParameter("msg"));
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
				String taxbylabel = new userBean().getTaxByLable(plant);
				
				 String[] unitPrice = request.getParameterValues("UNITPRICE");
		    	   String[] orderQty = request.getParameterValues("ORDERPRICE");
		    	   String[] uom = request.getParameterValues("UOM");
		    	   String custno = StrUtils.fString(request.getParameter("CUSTNO"));
		    	   String cname = StrUtils.fString(request.getParameter("CNAME"));
		    	   String taxtreatment = StrUtils.fString(request.getParameter("TAXTREATMENT"));
//		    	   String name = StrUtils.fString(request.getParameter("NAME"));
//		    	   String customer_type_id = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//		    	   String fitem = StrUtils.fString(request.getParameter("ITEM"));
//		    	   String floc = StrUtils.fString(request.getParameter("LOC"));
//		    	   String floc_type_id = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
//		    	   String floc_type_id2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
//		    	   String fmodel = StrUtils.fString(request.getParameter("MODEL"));
//		    	   String fuom = StrUtils.fString(request.getParameter("FUOM"));				
		    	   
		    	   estHdr esthdr =new estHdr();
		    	   esthdr.setCustName(cname);
		    	   esthdr.setCustCode(custno);
		    	   esthdr.setTAXTREATMENT(taxtreatment);
		    	   
				List<estDet> estdetList = new ArrayList<estDet>();
				 List listQry = new ArrayList();
				 String[] index = request.getParameterValues("ID");
				 ArrayList itemList = new ArrayList();	
         	 	
				 com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
					_TblControlDAO.setmLogger(mLogger);
					String newestno = _TblControlDAO.getNextOrder(plant, username, IConstants.ESTIMATE);
               
		    	   listQry = (List) request.getSession().getAttribute("EST_LIST");
		    	   for(int i=0; i<index.length; i++) {
		    		   Map mt=(Map)listQry.get(Integer.parseInt(index[i]));
		    		   String item =sqlBean.formatHTML(StrUtils.fString((String)mt.get("PRODUCT")));
		    		   String itemDesc =sqlBean.formatHTML(StrUtils.fString((String)mt.get("PRODUCTDESC")));
		    		  double unitprice= Double.valueOf(unitPrice[i]);
		    		  estDet estdet = new estDet();
		    		  estdet.setESTLNNO(Integer.parseInt(index[i])+1);
		    		   estdet.setITEM(item);
		    		   estdet.setItemDesc(itemDesc);
		    		   estdet.setUNITPRICE(unitprice);
		    		   estdet.setQTYOR(new java.math.BigDecimal(orderQty[i]));
		    		   estdet.setQTYIS(new java.math.BigDecimal(orderQty[i]));
		    		   estdet.setACCOUNT_NAME("");
		    		   estdet.setUNITMO(uom[i]);		    		  
		    		   
		    		   Map itemMap = new HashMap();
						itemMap.put("ConvertedUnitCost", 
								new ESTUtil().getConvertedUnitCostForProductByCurrency(plant,currency, item)) ;
						itemMap.put("ConvertedUnitCostWTC", 
								new ESTUtil().getConvertedUnitCostForProductWTCByCurrency(plant,currency, item)) ;
						itemMap.put("minSellingConvertedUnitCost", 
								new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,currency, item)) ;
						itemMap.put("outgoingOBDiscount",
								0);
						Hashtable ht = new Hashtable();
	         	 	ht.put("item", item);
	         	 	ht.put("plant", plant);
	         	 
	         	 	itemMap.put("EstQty",(String) orderQty[i] );
	         	 	
	         	 Map m = new InvMstDAO().getAvailableQtyByProduct(ht);
	         	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
		    		   
		    			List listItem = new ItemUtil().queryItemMstDetails( item,plant);
		                 Vector arrItem = (Vector)listItem.get(0);
		                 if(arrItem.size()>0){
		                 	itemMap.put("sItem",(String)arrItem.get(0)); 
		                     itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
		                     itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
		                     itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
		                     itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
		                     itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
		                     itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
		                     itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
		                     itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
		                     itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
		                     itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
		                     itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
		                     itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
		                     itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
		                     itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
		                     itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
		                     itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
		                     itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
		                     itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
		                     itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
		                     itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
		                     itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
		                     itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
		                     itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
		                     itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
		                     itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
		                     itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
		                     itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
		                     itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
		                     itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
								itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
		                     itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
		                     itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
		                     itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
		                     itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
		                     itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
		                     itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
		                     itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
								itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
								itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
		                     itemMap.put("OBDiscountType","");
		                     itemList.add(itemMap);
		                 }
		                 estdet.setESTNO(newestno);
				    	   estdetList.add(estdet);
		    	   }   
				
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				
				request.setAttribute("ORDDATE", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("NumberOfDecimal", numberOfDecimal);
				request.setAttribute("Currency", currency);
				request.setAttribute("GST", gst);
				request.setAttribute("Msg", msg);
				request.setAttribute("Taxbylabel", taxbylabel);
				request.setAttribute("Region", region);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertToEstimate.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estDetList", estdetList);
				request.setAttribute("ItemList", itemList);
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	   
		}
		
		
		else if(action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String estno = request.getParameter("r_estno");
				String estlno = request.getParameter("r_lnno");
				boolean insertFlag = false;
				
				Hashtable<String, String> htRemarksDel = new Hashtable<>();
				htRemarksDel.put(IDBConstants.PLANT, plant);
				htRemarksDel.put(IDBConstants.ESTDET_DONUM, estno);
				htRemarksDel.put(IDBConstants.ESTDET_ESTLNNO, estlno);
				htRemarksDel.put(IDBConstants.ESTDET_ITEM, item);
				if(new EstDetDAO().isExisitDoMultiRemarks(htRemarksDel)) {
					new EstDetDAO().deleteDoMultiRemarks(htRemarksDel);
				}
				
				for(int i=0; i<remarks.length; i++) {
					Hashtable<String, String> htRemarks = new Hashtable<>();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.ESTDET_DONUM, estno);
					htRemarks.put(IDBConstants.ESTDET_ESTLNNO, estlno);
					htRemarks.put(IDBConstants.ESTDET_ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(remarks[i]));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, username);
					insertFlag = new ESTUtil().saveDoMultiRemarks(htRemarks);
				}
				
				if(insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Sales Estimate Created Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  ThrowableUtil.getMessage(e));
		        resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if(action.equalsIgnoreCase("removeAttachmentById")){
			System.out.println("Remove Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("removeid"));
			EstimateAttachDAO estimateAttachDAO = new EstimateAttachDAO();
			try {
//				Hashtable ht1 = new Hashtable();
				estimateAttachDAO.deletesalesAttachByPrimId(plant, Integer.toString(ID));
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");  
		
		}else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID"); 
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			EstimateAttachDAO estimateAttachDAO = new EstimateAttachDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = estimateAttachDAO.getsalesAttachById(plant, String.valueOf(ID));
				Map billAttach=(Map)paymentAttachment.get(0);
				String filePath=(String) billAttach.get("FilePath");
				String fileType=(String) billAttach.get("FileType");
				String fileName=(String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	  
		}else if (action.equalsIgnoreCase("Delete")) {
			String estno = StrUtils.fString(request.getParameter("estno")).trim();
			EstDetDAO estDetDao = new EstDetDAO();
			ESTUtil eSTUtil = new ESTUtil();
			JSONObject resultJson = new JSONObject();
			try {
				Hashtable htDoHrd = new Hashtable();
				htDoHrd.put(IDBConstants.PLANT, plant);
				htDoHrd.put(IDBConstants.ESTDET_DONUM, estno);
				boolean isValidOrder;			
				isValidOrder = new EstHdrDAO().isExisit(htDoHrd, "");			
				boolean isOrderInProgress = estDetDao.isExisit(htDoHrd,
						"STATUS in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");			
				if (isValidOrder) {
					if (!isOrderInProgress) {
								 
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("ESTNO", estno);
//							String query = "estno,custCode";
//							ArrayList al = eSTUtil.getestHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = eSTUtil.removeRow(plant, estno, username);
							if (value) {
								resultJson.put("MESSAGE", "Estimate Order Deleted Successfully.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								resultJson.put("MESSAGE", "Estimate Order Not Deleted.");
								resultJson.put("ERROR_CODE", "97");
							}
						 
					} else {
						resultJson.put("MESSAGE", "Estimate Order Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Estimate Order Not Deleted.");
					resultJson.put("ERROR_CODE", "99");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("MESSAGE",  ThrowableUtil.getMessage(e));
		        resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}
		
		else if(action.equalsIgnoreCase("import")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEstimateOrderExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
}

		else if(action.equalsIgnoreCase("salesestordersummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryEstimatenoprice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
else if(action.equalsIgnoreCase("salesestordersumwithprice")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
else if(action.equalsIgnoreCase("addproduct")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createESTDET.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
		else if(action.equalsIgnoreCase("editestimate")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/maintEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(action.equalsIgnoreCase("newestimate")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		  else if(action.equalsIgnoreCase("convertob")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConvertEstimateToOutbound.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("salesestordersumdetail")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertedestimatesummarynoprice.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("salesestorderdetailbyprice")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/estSumyr_issueWprice.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("convertinvoice")) {
			  
			  
			  try {
				  String msg = StrUtils.fString(request.getParameter("msg"));
				  request.setAttribute("Msg", msg);
				  RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertToInvoice.jsp");
				  rd.forward(request, response);
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		  }
		
		  else if(action.equalsIgnoreCase("salescounter")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/alternatebrandproductsummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		EstHdrDAO estHdrDAO= new EstHdrDAO();
		EstDetDAO estDetDAO = new EstDetDAO();
		
		JSONObject jsonObjectResult = new JSONObject();
		
		if (action.equalsIgnoreCase("CheckOrderno")) {
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("ESTNO", orderno);
				if (new EstHdrDAO().isExisit(ht)) {
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
		
		if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {			
			jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
		} 

		if(action.equalsIgnoreCase("new")) {
			try {
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				
				String deldate = DateUtils.getDate();
				String collectionTime = DateUtils.getTimeHHmm();
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String msg = StrUtils.fString(request.getParameter("msg"));
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
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
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEstimateOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("new1")) {
			try {
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				
				String deldate = DateUtils.getDate();
				String collectionTime = DateUtils.getTimeHHmm();
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String msg = StrUtils.fString(request.getParameter("msg"));
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
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
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEstimateOrderProduct.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("Auto-Generate")) {
			String estno = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				estno = _TblControlDAO.getNextOrder(plant,username,IConstants.ESTIMATE);
				json.put("ESTNO", estno);
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
		}else if(action.equalsIgnoreCase("getsalesestimateRemarks")) {
			String estno = "", estlnno="";//, item="";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				estno = StrUtils.fString(request.getParameter("ESTNO"));
				estlnno = StrUtils.fString(request.getParameter("ESTLNO"));
//				item = StrUtils.fString(request.getParameter("ITEM"));
				
				Hashtable<String, String> ht = new Hashtable<>();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.ESTDET_DONUM, estno);
				ht.put(IDBConstants.ESTDET_ESTLNNO, estlnno);
				
				List al= new EstDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
				if(al.size()> 0){
                  for(int i=0 ; i<al.size();i++){
                 	  Map m = (Map)al.get(i);
                 	  JSONObject resultJsonInt = new JSONObject();
	                  resultJsonInt.put("remarks", (String)m.get("REMARKS"));
	                  jsonArray.add(resultJsonInt);
                  }
                }else {
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
		}else if(action.equalsIgnoreCase("summary")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String reference = StrUtils.fString(request.getParameter("REFERENCE"));
				String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String fdate="",tdate="";
				EstHDRService estHDRService = new EstHdrServiceImpl(); 
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();		
				try {
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("ESTNO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);
					if(StrUtils.fString(plant).length() > 0)	ht.put("PLANT", plant);
					
					if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			           String curDate =DateUtils.getDate();
						if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
							FROM_DATE=curDate;

			           if (FROM_DATE.length()>5)
			            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

			           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			           if (TO_DATE.length()>5)
			           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					List<estHdr> doHeaders =  estHDRService.getestHdr(ht, fdate, tdate);
					if(doHeaders.size() > 0) {
						for(estHdr esthdr : doHeaders) {
							JSONObject json = new JSONObject();
							//double totalAmount = Double.parseDouble(new DoDetDAO().getTotalAmount(doHdr.getDONO(), plant));
							EstDetService estDetService = new EstDetServiceImpl();
							List<estDet> estdetail = estDetService.getestDetById(plant, esthdr.getESTNO());
							double totalAmount =0;
							double subtotal =0;
							for (estDet estdet : estdetail) {
								String tax = "0";
								String str = estdet.getTAX_TYPE();
								tax = org.apache.commons.lang.StringUtils.substringBetween(str, "[", "%]");
								if(tax != null) {
									double taxamount = Double.valueOf(tax);
									if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = (ucost/100)*estdet.getDISCOUNT();
										double dettax = ((ucost - discount)/100)*taxamount;
										totalAmount = totalAmount + ((ucost + dettax) - discount);
										//totalAmount = totalAmount + ((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) + ((((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())-((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT())/100)*taxamount));
									}else {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = estdet.getDISCOUNT();
										double dettax = ((ucost - discount)/100)*taxamount;
										totalAmount = totalAmount + ((ucost + dettax) - discount);
										//totalAmount = totalAmount + ((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) + ((((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())-poDet.getDISCOUNT())/100)*taxamount));
									}
								}else {
									if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = (ucost/100)*estdet.getDISCOUNT();
										totalAmount = totalAmount + (ucost  - discount);
										//totalAmount = totalAmount + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT());
									}else {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = estdet.getDISCOUNT();
										totalAmount = totalAmount + (ucost  - discount);
										//totalAmount = totalAmount + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (poDet.getDISCOUNT());
									}
									
								}
								
								if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
									double discount = (ucost/100)*estdet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
									//subtotal = subtotal + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT());
								}else {
									double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
									double discount = estdet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
									//subtotal = subtotal + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (poDet.getDISCOUNT());
								}
							}
							//totalAmount = totalAmount + doHdr.getSHIPPINGCOST() + doHdr.getADJUSTMENT() - ((subtotal/100)*doHdr.getORDERDISCOUNT());
							if(esthdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
								System.out.println(totalAmount);
								System.out.println(esthdr.getSHIPPINGCOST());
								System.out.println(esthdr.getADJUSTMENT());
								System.out.println((subtotal/100)*esthdr.getORDERDISCOUNT());
								System.out.println((subtotal/100)*esthdr.getDISCOUNT());
								totalAmount = totalAmount + esthdr.getSHIPPINGCOST() + esthdr.getADJUSTMENT() - (((subtotal/100)*esthdr.getORDERDISCOUNT()) + ((subtotal/100)*esthdr.getDISCOUNT()));
							}else {
								System.out.println(totalAmount);
								System.out.println(esthdr.getSHIPPINGCOST());
								System.out.println(esthdr.getADJUSTMENT());
								System.out.println((subtotal/100)*esthdr.getORDERDISCOUNT());
								System.out.println(esthdr.getDISCOUNT());
								totalAmount = totalAmount + esthdr.getSHIPPINGCOST() + esthdr.getADJUSTMENT() - (((subtotal/100)*esthdr.getORDERDISCOUNT()) + esthdr.getDISCOUNT());
							}
							
							json.put("DATE", esthdr.getORDDATE());
							json.put("ESTNO", esthdr.getESTNO());
							json.put("CUSTOMER", esthdr.getCustName());
							/*if(esthdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(esthdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", esthdr.getORDER_STATUS());
							json.put("DELIVERY_DATE", esthdr.getDELIVERYDATE());
							json.put("EXCHANGE_RATE", esthdr.getCURRENCYUSEQT());
							double amt = totalAmount * esthdr.getCURRENCYUSEQT();
							json.put("ORDAMT",esthdr.getCURRENCYID() + "" + Numbers.toMillionFormat(amt, numberOfDecimal));
//									StrUtils.addZeroes(amt, numberOfDecimal) + "(" + esthdr.getCURRENCYID() + ")");
							json.put("AMOUNT", Numbers.toMillionFormat(totalAmount,numberOfDecimal));
							jsonArray.add(json);
						}
					}					
					resultJson.put("ORDERS", jsonArray);
				}catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500);
				}				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				String msg = StrUtils.fString(request.getParameter("msg"));
				
				request.setAttribute("Msg", msg);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesEstimateSummary.jsp");

				rd.forward(request, response);
			}			
		}else if(action.equalsIgnoreCase("summaryPro")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String reference = StrUtils.fString(request.getParameter("REFERENCE"));
				String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String fdate="",tdate="";
				EstHDRService estHDRService = new EstHdrServiceImpl(); 
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();		
				try {
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("ESTNO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);
					if(StrUtils.fString(plant).length() > 0)	ht.put("PLANT", plant);
					
					if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
			           String curDate =DateUtils.getDate();
						if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
							FROM_DATE=curDate;

			           if (FROM_DATE.length()>5)
			            fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

			           if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
			           if (TO_DATE.length()>5)
			           tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					List<estHdr> doHeaders =  estHDRService.getestHdr(ht, fdate, tdate);
					if(doHeaders.size() > 0) {
						for(estHdr esthdr : doHeaders) {
							JSONObject json = new JSONObject();
							//double totalAmount = Double.parseDouble(new DoDetDAO().getTotalAmount(doHdr.getDONO(), plant));
							EstDetService estDetService = new EstDetServiceImpl();
							List<estDet> estdetail = estDetService.getestDetById(plant, esthdr.getESTNO());
							double totalAmount =0;
							double subtotal =0;
							for (estDet estdet : estdetail) {
								String tax = "0";
								String str = estdet.getTAX_TYPE();
								tax = org.apache.commons.lang.StringUtils.substringBetween(str, "[", "%]");
								if(tax != null) {
									double taxamount = Double.valueOf(tax);
									if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = (ucost/100)*estdet.getDISCOUNT();
										double dettax = ((ucost - discount)/100)*taxamount;
										totalAmount = totalAmount + ((ucost + dettax) - discount);
										//totalAmount = totalAmount + ((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) + ((((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())-((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT())/100)*taxamount));
									}else {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = estdet.getDISCOUNT();
										double dettax = ((ucost - discount)/100)*taxamount;
										totalAmount = totalAmount + ((ucost + dettax) - discount);
										//totalAmount = totalAmount + ((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) + ((((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())-poDet.getDISCOUNT())/100)*taxamount));
									}
								}else {
									if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = (ucost/100)*estdet.getDISCOUNT();
										totalAmount = totalAmount + (ucost  - discount);
										//totalAmount = totalAmount + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT());
									}else {
										double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
										double discount = estdet.getDISCOUNT();
										totalAmount = totalAmount + (ucost  - discount);
										//totalAmount = totalAmount + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (poDet.getDISCOUNT());
									}
									
								}
								
								if(estdet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
									double discount = (ucost/100)*estdet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
									//subtotal = subtotal + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (((poDet.getUNITCOST()*poDet.getQTYOR().doubleValue())/100)*poDet.getDISCOUNT());
								}else {
									double ucost = estdet.getUNITPRICE()*estdet.getQTYOR().doubleValue();
									double discount = estdet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
									//subtotal = subtotal + (poDet.getUNITCOST()*poDet.getQTYOR().doubleValue()) - (poDet.getDISCOUNT());
								}
							}
							//totalAmount = totalAmount + doHdr.getSHIPPINGCOST() + doHdr.getADJUSTMENT() - ((subtotal/100)*doHdr.getORDERDISCOUNT());
							if(esthdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
								System.out.println(totalAmount);
								System.out.println(esthdr.getSHIPPINGCOST());
								System.out.println(esthdr.getADJUSTMENT());
								System.out.println((subtotal/100)*esthdr.getORDERDISCOUNT());
								System.out.println((subtotal/100)*esthdr.getDISCOUNT());
								totalAmount = totalAmount + esthdr.getSHIPPINGCOST() + esthdr.getADJUSTMENT() - (((subtotal/100)*esthdr.getORDERDISCOUNT()) + ((subtotal/100)*esthdr.getDISCOUNT()));
							}else {
								System.out.println(totalAmount);
								System.out.println(esthdr.getSHIPPINGCOST());
								System.out.println(esthdr.getADJUSTMENT());
								System.out.println((subtotal/100)*esthdr.getORDERDISCOUNT());
								System.out.println(esthdr.getDISCOUNT());
								totalAmount = totalAmount + esthdr.getSHIPPINGCOST() + esthdr.getADJUSTMENT() - (((subtotal/100)*esthdr.getORDERDISCOUNT()) + esthdr.getDISCOUNT());
							}
							
							json.put("DATE", esthdr.getORDDATE());
							json.put("ESTNO", esthdr.getESTNO());
							json.put("CUSTOMER", esthdr.getCustName());
							/*if(esthdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(esthdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", esthdr.getORDER_STATUS());
							json.put("DELIVERY_DATE", esthdr.getDELIVERYDATE());
							json.put("AMOUNT", StrUtils.addZeroes(totalAmount, numberOfDecimal));
							jsonArray.add(json);
						}
					}					
					resultJson.put("ORDERS", jsonArray);
				}catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500);
				}				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				String msg = StrUtils.fString(request.getParameter("msg"));
				
				request.setAttribute("Msg", msg);
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesEstimateSummaryPro.jsp");

				rd.forward(request, response);
			}			
		}if(action.equalsIgnoreCase("convertToSales")) {
			String estno = StrUtils.fString(request.getParameter("estno"));
			request.setAttribute("ESTNO", estno);
			
			String imagePath = "", numberOfDecimal = "";
			
//			Map plntMap = new HashMap();
//			Map esthdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "", newDono = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				estHdr esthdr = estHdrDAO.getestHdrById(plant, estno);
				estdetList = estDetDAO.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES",plant);
				esthdr.setOUTBOUND_GST(Double.valueOf(gst));
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newDono = new TblControlDAO().getNextOrder(plant,username,IConstants.OUTBOUND);
				request.setAttribute("DONO", newDono);
				request.setAttribute("JOBNO", estno);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				

				
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new DOUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"OUTBOUND");
					//new DOUtil().getOBDiscountSelectedItemByCustomer(plant,"",estdet.getITEM(),"OUTBOUND");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new DOUtil().getConvertedUnitCostForProduct(plant,"",estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new DOUtil().getConvertedUnitCostForProductWTC(plant,"",estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,"",estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					
					
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertFromEstimateToSales.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estdetList", estdetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("Region", region);
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);

				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}	
		}else if(action.equalsIgnoreCase("convertToSalesPro")) {
			String estno = StrUtils.fString(request.getParameter("estno"));
			request.setAttribute("ESTNO", estno);
			
			String imagePath = "", numberOfDecimal = "";
			
//			Map plntMap = new HashMap();
//			Map esthdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "", newDono = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				estHdr esthdr = estHdrDAO.getestHdrById(plant, estno);
				estdetList = estDetDAO.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES",plant);
				esthdr.setOUTBOUND_GST(Double.valueOf(gst));
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newDono = new TblControlDAO().getNextOrder(plant,username,IConstants.OUTBOUND);
				request.setAttribute("DONO", newDono);
				request.setAttribute("JOBNO", estno);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				

				
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new DOUtil().getOBDiscountSelectedItemByCustomer(plant,"",estdet.getITEM(),"OUTBOUND");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new DOUtil().getConvertedUnitCostForProduct(plant,"",estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new DOUtil().getConvertedUnitCostForProductWTC(plant,"",estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new DOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,"",estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					
					
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertFromEstimateToSalesPro.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estdetList", estdetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);

				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}	
		}
		
		else if(action.equalsIgnoreCase("detail")) {
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
			Map plntMap = new HashMap();
			Map esthdrDetails = new HashMap();
			ArrayList custDetails = new ArrayList();
			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			ArrayList itemList = new ArrayList();
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				
				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plant);
				plntMap = (Map) plntList.get(0);
				custDetails = new CustUtil().getCustomerDetailsForEST(estno, plant);
				shippingCustDetails = new MasterUtil().getEstimateShippingDetails(estno, esthdr.getSHIPPINGID(), plant);
				
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				
				esthdrDetails = new ESTUtil().getESTReceiptInvoiceHdrDetails(plant);
				
				/*Item Details*/
				/* created by resvi */
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					/* created by navas */
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesEstimateDetail.jsp");
			request.setAttribute("estHdr", esthdr);
			request.setAttribute("estDetList", estdetList);
			request.setAttribute("PLNTMAP", plntMap);
			request.setAttribute("ESTHDRDETAILS", esthdrDetails);		
			request.setAttribute("CUSTDETAILS", custDetails);
			request.setAttribute("SHIPPINGCUSTDETAILS", shippingCustDetails);
			request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
			request.setAttribute("ItemList", itemList);
			
			request.setAttribute("IMAGEPATH", imagePath);
			
			rd.forward(request, response);
		}
		
		else if(action.equalsIgnoreCase("detailPro")) {
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
			Map plntMap = new HashMap();
			Map esthdrDetails = new HashMap();
			ArrayList custDetails = new ArrayList();
			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			ArrayList itemList = new ArrayList();
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				
				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plant);
				plntMap = (Map) plntList.get(0);
				custDetails = new CustUtil().getCustomerDetailsForEST(estno, plant);
				shippingCustDetails = new MasterUtil().getEstimateShippingDetails(estno, esthdr.getSHIPPINGID(), plant);
				
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				
				esthdrDetails = new ESTUtil().getESTReceiptInvoiceHdrDetails(plant);
				
				/*Item Details*/
				/* created by resvi */
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					/* created by navas */
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesEstimateDetailPro.jsp");
			request.setAttribute("estHdr", esthdr);
			request.setAttribute("estDetList", estdetList);
			request.setAttribute("PLNTMAP", plntMap);
			request.setAttribute("ESTHDRDETAILS", esthdrDetails);		
			request.setAttribute("CUSTDETAILS", custDetails);
			request.setAttribute("SHIPPINGCUSTDETAILS", shippingCustDetails);
			request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
			request.setAttribute("ItemList", itemList);
			
			request.setAttribute("IMAGEPATH", imagePath);
			
			rd.forward(request, response);
		}
		
		else if(action.equalsIgnoreCase("edit")) {
			
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
//			Map plntMap = new HashMap();
//			Map estHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new EstimateAttachDAO().getsalesAttachByESTNO(plant, estno);
				
				/* CREATED BY RESVI */
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					
					  itemMap.put("ConvertedUnitCostWTC", new
					  ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.
					  getITEM()));
					 
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editEstimateOrder.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estDetList", estdetList);
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
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
		
		else if(action.equalsIgnoreCase("edit1")) {
			
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
//			Map plntMap = new HashMap();
//			Map estHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new EstimateAttachDAO().getsalesAttachByESTNO(plant, estno);
				
				/* CREATED BY RESVI */
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					
					  itemMap.put("ConvertedUnitCostWTC", new
					  ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.
					  getITEM()));
					 
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editEstimateOrderProduct.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estDetList", estdetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);
				request.setAttribute("AttachmentList", attachmentList);
				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}  
		else if(action.equalsIgnoreCase("copy")) {
			
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
//			Map plntMap = new HashMap();
//			Map estHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "", newEstno = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newEstno = new TblControlDAO().getNextOrder(plant,username,IConstants.ESTIMATE);
				esthdr.setESTNO(newEstno);
				esthdr.setJobNum(estno);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new EstimateAttachDAO().getsalesAttachByESTNO(plant, estno);
				
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
                    estdet.setESTNO(newEstno);
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CopyEstimateOrder.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estDetList", estdetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("oldestno", estno);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("Region", region);
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);
				request.setAttribute("AttachmentList", attachmentList);
				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}else if(action.equalsIgnoreCase("copyPro")) {
			
			String estno = StrUtils.fString(request.getParameter("estno"));
			String imagePath = "", numberOfDecimal = "";
			
			EstHDRService estHDRService = new EstHdrServiceImpl();
			EstDetService estDetService = new EstDetServiceImpl();
			estHdr esthdr = new estHdr();
//			Map plntMap = new HashMap();
//			Map estHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<estDet> estdetList = new ArrayList<estDet>();
			String msg= "", newEstno = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				esthdr = estHDRService.getestHdrById(plant, estno);
				estdetList = estDetService.getestDetById(plant, estno);
				String gst = new selectBean().getGST("SALES ESTIMATE",plant);
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newEstno = new TblControlDAO().getNextOrder(plant,username,IConstants.ESTIMATE);
				esthdr.setESTNO(newEstno);
				esthdr.setJobNum(estno);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new EstimateAttachDAO().getsalesAttachByESTNO(plant, estno);
				
				/*Item Details*/
				for(estDet estdet: estdetList){					
					String OBDiscount = 
					new ESTUtil().getOBDiscountSelectedItemByCustomer(plant,esthdr.getCustCode(),estdet.getITEM(),"ESTIMATE");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new ESTUtil().getConvertedUnitCostForProduct(plant,estdet.getESTNO(),estdet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new ESTUtil().getConvertedUnitCostForProductWTC(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new ESTUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,estdet.getESTNO(),estdet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", estdet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(estdet.getITEM(),plant);
                    Vector arrItem = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	itemMap.put("sItem",(String)arrItem.get(0)); 
                        itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get(1)));
                        itemMap.put("sUOM",StrUtils.fString((String)arrItem.get(3)));
                        itemMap.put("prd_cls_id",StrUtils.fString((String)arrItem.get(10)));
                        itemMap.put("sArtist",StrUtils.fString((String)arrItem.get(2)));//itemtype
                        itemMap.put("brand",StrUtils.fString((String)arrItem.get(19)));
                        itemMap.put("sTitle",StrUtils.fString((String)arrItem.get(7)));
                        itemMap.put("sMedium",StrUtils.fString((String)arrItem.get(5)));
                        itemMap.put("sItemCondition",StrUtils.fString((String)arrItem.get(6)));
                        itemMap.put("sRemark",StrUtils.fString((String)arrItem.get(4)));
                        itemMap.put("stkqty",StrUtils.fString((String)arrItem.get(8)));
                        itemMap.put("price",StrUtils.fString((String)arrItem.get(12)));
                        itemMap.put("cost",StrUtils.fString((String)arrItem.get(13)));
                        itemMap.put("minsprice",StrUtils.fString((String)arrItem.get(14)));
                        itemMap.put("discount",StrUtils.fString((String)arrItem.get(15)));
                        itemMap.put("ISPARENTCHILD",StrUtils.fString((String)arrItem.get(16)));
                        itemMap.put("isActive",StrUtils.fString((String)arrItem.get(11)));
                        itemMap.put("nonstkflg",StrUtils.fString((String)arrItem.get(17)));
                        itemMap.put("nonstktypeid",StrUtils.fString((String)arrItem.get(18)));
                        itemMap.put("loc",StrUtils.fString((String)arrItem.get(20)));
                        itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                        itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                        itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
                        itemMap.put("PRODGST",StrUtils.fString((String)arrItem.get(24)));
                        itemMap.put("netweight",StrUtils.fString((String)arrItem.get(25)));
                        itemMap.put("grossweight",StrUtils.fString((String)arrItem.get(26)));
                        itemMap.put("hscode",StrUtils.fString((String)arrItem.get(27)));
                        itemMap.put("coo",StrUtils.fString((String)arrItem.get(28)));
                        itemMap.put("vinno",StrUtils.fString((String)arrItem.get(29)));
                        itemMap.put("model",StrUtils.fString((String)arrItem.get(30)));
						itemMap.put("RentalPrice",StrUtils.fString((String)arrItem.get(31)));
                        itemMap.put("ServicePrice",StrUtils.fString((String)arrItem.get(32)));
                        itemMap.put("PurchaseUOM",StrUtils.fString((String)arrItem.get(33)));
                        itemMap.put("SalesUOM",StrUtils.fString((String)arrItem.get(34)));
                        itemMap.put("RentalUOM",StrUtils.fString((String)arrItem.get(35)));
                        itemMap.put("ServiceUOM",StrUtils.fString((String)arrItem.get(36)));
                        itemMap.put("InventoryUOM",StrUtils.fString((String)arrItem.get(37)));
                        itemMap.put("ISBASICUOM",StrUtils.fString((String)arrItem.get(38)));
						itemMap.put("outgoingqtyloan",StrUtils.fString((String)arrItem.get(39)));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get(40)));
                        itemMap.put("OBDiscountType",discounttype);
                        itemList.add(itemMap);
                    }
                    estdet.setESTNO(newEstno);
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CopyEstimateOrderPro.jsp");
				request.setAttribute("estHdr", esthdr);
				request.setAttribute("estDetList", estdetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("oldestno", estno);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				
				request.setAttribute("TaxTreatmentList", taxTreatmentList);
				request.setAttribute("SalesLocations", slList);
				request.setAttribute("CountryList", ccList);
				request.setAttribute("BankList", bankList);
				request.setAttribute("ItemList", itemList);
				request.setAttribute("AttachmentList", attachmentList);
				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
else if(action.equalsIgnoreCase("import")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEstimateOrderExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
}

		else if(action.equalsIgnoreCase("salesestordersummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryEstimatenoprice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
else if(action.equalsIgnoreCase("salesestordersumwithprice")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
else if(action.equalsIgnoreCase("addproduct")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createESTDET.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
		else if(action.equalsIgnoreCase("editestimate")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/maintEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(action.equalsIgnoreCase("newestimate")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		  else if(action.equalsIgnoreCase("convertob")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConvertEstimateToOutbound.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("salesestordersumdetail")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertedestimatesummarynoprice.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("salesestorderdetailbyprice")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/estSumyr_issueWprice.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		  else if(action.equalsIgnoreCase("convertinvoice")) {
			  
			  
			  try {
				  String msg = StrUtils.fString(request.getParameter("msg"));
				  request.setAttribute("Msg", msg);
				  RequestDispatcher rd = request.getRequestDispatcher("/jsp/convertToInvoice.jsp");
				  rd.forward(request, response);
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		  }
		
		  else if(action.equalsIgnoreCase("salescounter")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/alternatebrandproductsummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	}
	

	/* CREATED BY NAVAS */
	public JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        ESTUtil itemUtil = new ESTUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String estno = StrUtils.fString(request.getParameter("ESTNO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(estno.length()>0) extCond=" AND plant='"+plant+"' and estno like '"+estno+"%' ";
		     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
		     //extCond=extCond+" and STATUS <>'C'";
		     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = itemUtil.getestHdrDetails("estno,CustName,CustCode,jobNum,status,collectiondate",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  estno = (String)m.get("estno");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("ESTNO", estno);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);				  
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
	private MLogger mLogger = new MLogger();
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		
	}

}
