package com.track.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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

import com.itextpdf.kernel.geom.PageSize;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ConsignmentAttachDAO;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.ToDet;
import com.track.db.object.ToHdr;
import com.track.db.util.CustUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.service.ToDetService;
import com.track.service.ToHDRService;
import com.track.serviceImplementation.ToDetServiceImpl;
import com.track.serviceImplementation.ToHdrServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.ImageUtil;
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
@SuppressWarnings({"rawtypes", "unchecked"})
@WebServlet("/consignment/*")
public class ConsigmentServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("edit") 
				|| action.equalsIgnoreCase("copy")) {
			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String tono = "", orderType = "", delDate = "", custCode = "", custName = "", jobNum = "",
					personInCharge = "", contactNum = "", address = "", address2 = "", address3 = "", 
					collectionTime = "", remark1 = "", remark2 = "", shippingId = "", shippingCustomer = "",
					currencyId = "", deliveryDate = "",outbound_gst = "", 
					empName="",empno = "", remark3 = "", orderDiscount = "", shippingCost = "",
					incoterms = "", paymentType = "", deliverydateFormat = "0", approveStatus = "",
					sales_location = "", taxTreatment = "", currencyUseQty = "", orderstatus = "",
					discount = "", discount_type = "", adjustment = "", itemRates = "",projectid="0",
					orderdiscstatus = "0",discstatus = "0",shipstatus = "0",taxid = "",orderdisctype = "",loc="",tloc="",actionButton = "",shipcontactname="",
							shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",shipworkphone="",
							shipcountry="",shiphpno="",shipemail="";
			//collectionDate = "",status_id = "",estno = "",  timeSlots = "", 
//			String oldTono="";
			List<Hashtable<String,String>> consignmentAttachmentList = null;
			List<Hashtable<String,String>> consignmentAttachmentInfoList = null;
			/*DoDet*/
			List item = new ArrayList(), unitPrice = new ArrayList(), qtyOr = new ArrayList(), qtyIs = new ArrayList(), qtyEst = new ArrayList(),
					unitMo = new ArrayList(), 
					prdDeliveryDate = new ArrayList(), accountName = new ArrayList(),
					taxType = new ArrayList(), item_discount = new ArrayList(),
					item_discountType = new ArrayList(), chkdDONO = new ArrayList();//proGst = new ArrayList(),
			int taxTypeCount = 0;/*,itemCount = 0, unitPriceCount = 0, qtyOrCount = 0, qtyIsCount = 0, qtyEstCount = 0, unitMoCount = 0, 
					proGstCount = 0, 
					prdDeliveryDateCount = 0, accountNameCount = 0, item_discountCount = 0, item_discountTypeCount = 0,
					chkdDONOCount = 0;*/
			consignmentAttachmentList = new ArrayList<Hashtable<String,String>>();
			consignmentAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
			ConsignmentAttachDAO consignmentAttachDAO = new ConsignmentAttachDAO();
			ToHDRService toHDRService = new ToHdrServiceImpl(); 
			ToDetService toDetService = new ToDetServiceImpl();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				boolean isOrderExists = false;
				String taxby = new PlantMstDAO().getTaxBy(plant);
				List<ToDet> ToDetList = new ArrayList();
				if(isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					ToHdr toHdr = new ToHdr();
					
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* DOHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("TONO")) {
								tono = StrUtils.fString(fileItem.getString()).trim();
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("FROMWAREHOUSE")) {
								loc = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TOWAREHOUSE")) {
								tloc = StrUtils.fString(fileItem.getString()).trim();
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
//								chkdDONOCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTYIS")) {
								qtyIs.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyIsCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ESTQTY")) {
								qtyEst.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyEstCount++;
							}
							if (fileItem.getFieldName().equalsIgnoreCase("action_button")) {
								actionButton = StrUtils.fString(fileItem.getString().trim());
							}
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/CONSIGNMENT" + "/"+ tono;
								String filetempLocationATT = "C:/ATTACHMENTS/CONSIGNMENT" + "/temp" + "/"+ tono;
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
								Hashtable consignmentAttachment = new Hashtable<String, String>();
								consignmentAttachment.put("PLANT", plant);
								consignmentAttachment.put("FILETYPE", fileItem.getContentType());
								consignmentAttachment.put("FILENAME", fileName);
								consignmentAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								consignmentAttachment.put("FILEPATH", fileLocationATT);
								consignmentAttachment.put("CRAT",DateUtils.getDateTime());
								consignmentAttachment.put("CRBY",username);
								consignmentAttachment.put("UPAT",DateUtils.getDateTime());
								consignmentAttachmentList.add(consignmentAttachment);
							}
						}
					}
					
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
						Map m = (Map) arrList.get(0);
						empno = (String) m.get("EMPNO");
						}
					}
					
					if(!orderdisctype.equalsIgnoreCase("%")) {
						discount = String.valueOf(Double.parseDouble(discount)/Double.parseDouble(currencyUseQty));
					}
					
					if(!discount_type.equalsIgnoreCase("%")) {
						orderDiscount = String.valueOf(Double.parseDouble(orderDiscount)/Double.parseDouble(currencyUseQty));
					}
					shippingCost = String.valueOf(Double.parseDouble(shippingCost)/Double.parseDouble(currencyUseQty));
					adjustment = String.valueOf(Double.parseDouble(adjustment)/Double.parseDouble(currencyUseQty));
					
					toHdr.setPLANT(plant);
					toHdr.setTONO(tono);
					toHdr.setORDERTYPE(orderType);
					toHdr.setDELDATE(delDate);
					if(!action.equalsIgnoreCase("edit")) {
					toHdr.setSTATUS("N");
					toHdr.setPickStaus("N");
					}
					toHdr.setCRAT(DateUtils.getDateTime());
					toHdr.setCRBY(username);
					toHdr.setCustCode(custCode);
					toHdr.setCustName(custName);
					toHdr.setFROMWAREHOUSE(loc);
					toHdr.setTOWAREHOUSE(tloc);
					toHdr.setJobNum(jobNum);
					toHdr.setPersonInCharge(personInCharge);
					toHdr.setContactNum(contactNum);
					toHdr.setAddress(address);
					toHdr.setAddress2(address2);
					toHdr.setAddress3(address3);
					toHdr.setCollectionDate(DateUtils.getDate());
					toHdr.setCollectionTime(collectionTime);
					toHdr.setRemark1(remark1);
					toHdr.setRemark2(remark2);
					toHdr.setRemark3(remark3);
					toHdr.setSHIPPINGID(shippingId);
					toHdr.setSHIPPINGCUSTOMER(shippingCustomer);
					toHdr.setCURRENCYID(currencyId);
					toHdr.setDELIVERYDATE(deliveryDate);
					toHdr.setCONSIGNMENT_GST(Double.parseDouble(outbound_gst));
					toHdr.setEMPNO(empno);
					toHdr.setORDERDISCOUNT(Double.parseDouble(orderDiscount));
					toHdr.setSHIPPINGCOST(Double.parseDouble(shippingCost));
					toHdr.setINCOTERMS(incoterms);
					toHdr.setPAYMENTTYPE(paymentType);
					toHdr.setDELIVERYDATEFORMAT(Short.parseShort(deliverydateFormat));
					toHdr.setAPPROVESTATUS(approveStatus);
					toHdr.setSALES_LOCATION(sales_location);
					toHdr.setTAXTREATMENT(taxTreatment);
					toHdr.setORDER_STATUS(orderstatus);
					toHdr.setDISCOUNT(Double.parseDouble(discount));
					toHdr.setDISCOUNT_TYPE(discount_type);
					toHdr.setADJUSTMENT(Double.parseDouble(adjustment));
					toHdr.setITEM_RATES(Short.parseShort(itemRates));
					toHdr.setCURRENCYUSEQT(Double.valueOf(currencyUseQty));
					toHdr.setORDERDISCOUNTTYPE(orderdisctype);
					toHdr.setTAXID(Integer.valueOf(taxid));
					toHdr.setISDISCOUNTTAX(Short.parseShort(discstatus));
					toHdr.setISORDERDISCOUNTTAX(Short.parseShort(orderdiscstatus));
					toHdr.setISSHIPPINGTAX(Short.parseShort(shipstatus));
					if(projectid.length() > 0) {
						toHdr.setPROJECTID(Integer.valueOf(projectid));
					}else {
						toHdr.setPROJECTID(Integer.valueOf("0"));
					}
					toHdr.setSHIPCONTACTNAME(shipcontactname);
					toHdr.setSHIPDESGINATION(shipdesgination);
					toHdr.setSHIPWORKPHONE(shipworkphone);
					toHdr.setSHIPHPNO(shiphpno);
					toHdr.setSHIPEMAIL(shipemail);
					toHdr.setSHIPCOUNTRY(shipcountry);
					toHdr.setSHIPADDR1(shipaddr1);
					toHdr.setSHIPADDR2(shipaddr2);
					toHdr.setSHIPADDR3(shipaddr3);
					toHdr.setSHIPADDR4(shipaddr4);
					toHdr.setSHIPSTATE(shipstate);
					toHdr.setSHIPZIP(shipzip);


						if(action.equalsIgnoreCase("edit")) {
						for(int i=0;i<item.size();i++) {
							int lnno = i+1;
							boolean detcheck = toDetService.isgetToDetById(plant, tono, lnno, (String)item.get(i));
							if(detcheck) {
							
								ToDet toDet = new ToDet();
								toDet = toDetService.getToDetById(plant, tono, lnno, (String)item.get(i));
								toDet.setPLANT(plant);
								toDet.setTONO(tono);
								toDet.setTOLNNO(lnno);
								//doDet.setPickStatus("N");
								//doDet.setLNSTAT("N");
								if(toDet.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyOr.get(i)))//Status update Azees 
								{
									if(toDet.getQTYRC().doubleValue()>0) 
									{
										toDet.setPickStatus("O");
										toDet.setLNSTAT("0");
									} else {
									toDet.setPickStatus("N");
									toDet.setLNSTAT("N");
									}
								}
								toDet.setITEM((String)item.get(i));
								
								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, toDet.getITEM());
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
								toDet.setItemDesc(itemDesc);
								toDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								
								String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(i))/Double.parseDouble(currencyUseQty));
								String disctype = (String)item_discountType.get(i);
								double discountamount = Double.valueOf((String)item_discount.get(i));
								if(!disctype.equalsIgnoreCase("%")) {
									discountamount = discountamount/Double.parseDouble(currencyUseQty);
								}
								
								if (nonstocktype.equals("Y")) {
									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										toDet.setUNITPRICE(Double.parseDouble("-"+itemunitprice));
									}else {
										toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
									}
								}else {
									toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}	
								
								toDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(i))));
								toDet.setUNITMO((String) unitMo.get(i));
								toDet.setUPAT(DateUtils.getDateTime());
								toDet.setUPBY(username);
								toDet.setUSERFLD1(itemDesc);
								toDet.setUSERFLD2(jobNum);
								toDet.setUSERFLD3(custName);
								toDet.setCURRENCYUSEQT(Double.parseDouble((String)currencyUseQty));
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									String prodgst = new ItemMstDAO().getProductGst(plant, toDet.getITEM());
									toDet.setPRODGST(Double.parseDouble((String)prodgst));
								}
								toDet.setACCOUNT_NAME((String)accountName.get(i));
								toDet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(i));
								toDet.setTAX_TYPE((String)taxType.get(i));
								toDet.setDISCOUNT(discountamount);
								toDet.setDISCOUNT_TYPE((String)item_discountType.get(i));
								
								toDet.setUPAT(DateUtils.getDateTime());
								toDet.setUPBY(username);
								
								ToDetList.add(toDet);
							}else {
							
								ToDet toDet = new ToDet();
								toDet.setPLANT(plant);
								toDet.setTONO(tono);
								toDet.setTOLNNO(lnno);
								toDet.setPickStatus("N");
								toDet.setLNSTAT("N");
								toDet.setITEM((String)item.get(i));
								
								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, toDet.getITEM());
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
								toDet.setItemDesc(itemDesc);
								toDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								
								String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(i))/Double.parseDouble(currencyUseQty));
								String disctype = (String)item_discountType.get(i);
								double discountamount = Double.valueOf((String)item_discount.get(i));
								if(!disctype.equalsIgnoreCase("%")) {
									discountamount = discountamount/Double.parseDouble(currencyUseQty);
								}
								
								if (nonstocktype.equals("Y")) {
									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										toDet.setUNITPRICE(Double.parseDouble("-"+itemunitprice));
									}else {
										toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
									}
								}else {
									toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}	
								
								toDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(i))));
								toDet.setQTYRC(BigDecimal.valueOf(0));
								toDet.setQtyPick(BigDecimal.valueOf(0));
								toDet.setUNITMO((String) unitMo.get(i));
								toDet.setCRAT(DateUtils.getDateTime());
								toDet.setCRBY(username);
								toDet.setUSERFLD1(itemDesc);
								toDet.setUSERFLD2(jobNum);
								toDet.setUSERFLD3(custName);
								toDet.setCURRENCYUSEQT(Double.parseDouble((String)currencyUseQty));
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									String prodgst = new ItemMstDAO().getProductGst(plant, toDet.getITEM());
									toDet.setPRODGST(Double.parseDouble((String)prodgst));
								}
								toDet.setACCOUNT_NAME((String)accountName.get(i));
								toDet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(i));
								toDet.setTAX_TYPE((String)taxType.get(i));
								toDet.setDISCOUNT(discountamount);
								toDet.setDISCOUNT_TYPE((String)item_discountType.get(i));
								
								toDet.setUPAT(DateUtils.getDateTime());
								toDet.setUPBY(username);
								
								ToDetList.add(toDet);
							}
						}
					}else {
						for(int i=0;i<item.size();i++) {
							int lnno = i+1;
							ToDet toDet = new ToDet();
							toDet.setPLANT(plant);
							toDet.setTONO(tono);
							toDet.setTOLNNO(lnno);
							toDet.setPickStatus("N");
							toDet.setLNSTAT("N");
							toDet.setITEM((String)item.get(i));
							
							Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, toDet.getITEM());
							String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
							String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
							toDet.setItemDesc(itemDesc);
							toDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							
							String itemunitprice = String.valueOf(Double.parseDouble((String)unitPrice.get(i))/Double.parseDouble(currencyUseQty));
							String disctype = (String)item_discountType.get(i);
							double discountamount = Double.valueOf((String)item_discount.get(i));
							if(!disctype.equalsIgnoreCase("%")) {
								discountamount = discountamount/Double.parseDouble(currencyUseQty);
							}
							
							if (nonstocktype.equals("Y")) {
								if (nonstocktypeDesc.equalsIgnoreCase("discount")
										|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
									toDet.setUNITPRICE(Double.parseDouble("-"+itemunitprice));
								}else {
									toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
								}
							}else {
								toDet.setUNITPRICE(Double.parseDouble(itemunitprice));
							}	
							
							toDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyOr.get(i))));
							toDet.setQTYRC(BigDecimal.valueOf(0));
							toDet.setQtyPick(BigDecimal.valueOf(0));
							toDet.setQTYAC(BigDecimal.valueOf(0));
							toDet.setUNITMO((String) unitMo.get(i));
							toDet.setCRAT(DateUtils.getDateTime());
							toDet.setCRBY(username);
							toDet.setUSERFLD1(itemDesc);
							toDet.setUSERFLD2(jobNum);
							toDet.setUSERFLD3(custName);
							toDet.setCURRENCYUSEQT(Double.parseDouble((String)currencyUseQty));
							if (taxby.equalsIgnoreCase("BYPRODUCT")) {
								String prodgst = new ItemMstDAO().getProductGst(plant, toDet.getITEM());
								toDet.setPRODGST(Double.parseDouble((String)prodgst));
							}
							toDet.setACCOUNT_NAME((String)accountName.get(i));
							toDet.setPRODUCTDELIVERYDATE((String)prdDeliveryDate.get(i));
							toDet.setTAX_TYPE((String)taxType.get(i));
							toDet.setDISCOUNT(discountamount);
							toDet.setDISCOUNT_TYPE((String)item_discountType.get(i));
							
							toDet.setUPAT(DateUtils.getDateTime());
							toDet.setUPBY(username);
							
							ToDetList.add(toDet);
						}
					}
					
					
					ut = DbBean.getUserTranaction();
					
					
					ut.begin();
					
				if(action.equalsIgnoreCase("new") || action.equalsIgnoreCase("copy")) {
					Hashtable doht = new Hashtable();
					doht.put("tono", toHdr.getTONO());
					doht.put("PLANT", toHdr.getPLANT());
					
					isOrderExists  = new ToHdrDAO().isExisit(doht);
					
					if(isOrderExists) {
//						oldTono = toHdr.getTONO();
						tono = new TblControlDAO().getNextOrder(plant, username, IConstants.CONSIGNMENT);
						toHdr.setTONO(tono);
					}
					
					boolean insertFlag = toHDRService.addToHdr(toHdr);
					if(insertFlag) {
						insertFlag = toDetService.addToDet(ToDetList);
					}
					if(insertFlag) {
								for(int i=0;i<item.size();i++) {
									int tolno = i+1;
									Hashtable<String, String> htRemarks = new Hashtable<>();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.TODET_TONUM, tono);
									htRemarks.put(IDBConstants.TODET_TOLNNO , Integer.toString(tolno));
									htRemarks.put(IDBConstants.TODET_ITEM, (String)item.get(i));
									if(!new ToDetDAO().isExisitToMultiRemarks(htRemarks)) {
										htRemarks.put(IDBConstants.REMARKS, "");
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, username);
										insertFlag = new TOUtil().saveToMultiRemarks(htRemarks);
									}
								}	
					}
					if(insertFlag) {
						int attchSize = consignmentAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable consignmentAttachmentat = new Hashtable<String, String>();
								consignmentAttachmentat = consignmentAttachmentList.get(i);
								consignmentAttachmentat.put("TONO", tono);
								consignmentAttachmentInfoList.add(consignmentAttachmentat);
							}
							insertFlag = consignmentAttachDAO.addconsignmentAttachments(consignmentAttachmentInfoList, plant);
						}
					}
					if(insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_TO);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
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
						
						for (ToDet toorderdet : ToDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.TO_ADD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, toHdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, toHdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, toorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, toorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
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
						new TblControlUtil().updateTblControlSeqNo(plant, IConstants.CONSIGNMENT, "C", tono);
						DbBean.CommitTran(ut);
						 String message = "Consignment Order Added Successfully.";
						if (ajax) {
							request.setAttribute("TONO", toHdr.getTONO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.CONSIGNMENT_ORDER);
							if ("btnSalesOpenEmail".equals(actionButton)) {
								String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
								if ("both".equals(sendAttachment) || "consignment".equals(sendAttachment)) {
									coPDFGenration(request, response, tono, "consignment");
								}
								if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
									coPDFGenration(request, response, tono, "invoice");
								}
							}
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../consignment/summary?msg=" + message);
						}
						/*if(isOrderExists) {
							resultJson.put("MESSAGE", "Consignment order " + oldTono + 
									" has already been used. System has auto created a new consignment order " + tono +
									" for you.");
						}else {
							resultJson.put("MESSAGE", "Consignment Order Created Successfully.");
						}						
						resultJson.put("ERROR_CODE", "100");	
						
						String isAutoEmail = new EmailMsgDAO().getIsAutoEmailDetails(plant, IConstants.OUTBOUND_ORDER);
						if (isAutoEmail.equalsIgnoreCase("Y"))
							new EmailMsgUtil().sendEmail(plant, tono, IConstants.OUTBOUND_ORDER);*/
					}else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Consignment Order.";
						//resultJson.put("MESSAGE",  "Failed to Create Consignment Order.");
				        //resultJson.put("ERROR_CODE", "98");
				        if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../consignment/summary?msg=" + message);
						}
					}
				}else if(action.equalsIgnoreCase("edit")) {
					toHdr.setORDER_STATUS("Open");
					toHdr.setSTATUS("N");
					toHdr.setUPAT(DateUtils.getDateTime());
					toHdr.setUPBY(username);					
					boolean updateFlag = toHDRService.updateToHdr(toHdr);
					if(updateFlag) {
						Hashtable ht = new Hashtable();
						ht.put("TONO", toHdr.getTONO());
						ht.put("PLANT", plant);
						updateFlag = new ToDetDAO().delete(ht);
						
						if(updateFlag)
							updateFlag = toDetService.addToDet(ToDetList);
					}
					if(updateFlag) {
						int attchSize = consignmentAttachmentList.size();
						if(attchSize > 0) {
							for(int i =0 ; i < attchSize ; i++){
								Hashtable consignmentAttachmentat = new Hashtable<String, String>();
								consignmentAttachmentat = consignmentAttachmentList.get(i);
								consignmentAttachmentat.put("TONO", tono);
								consignmentAttachmentInfoList.add(consignmentAttachmentat);
							}
							updateFlag = consignmentAttachDAO.addconsignmentAttachments(consignmentAttachmentInfoList, plant);
						}
					}
					//ORDER_STATUS Update - Azees 3.21
					if(!orderstatus.equalsIgnoreCase("Draft")) {
					if(updateFlag) {
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", plant);
					htCondition.put("TONO", toHdr.getTONO());					
					updateFlag = new ToDetDAO().isExisit(htCondition,"PickStatus in ('O','N')");
					if (!updateFlag){
						String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
						updateFlag = new ToHdrDAO().update(updateHdr, htCondition, "");
					} else {
						updateFlag = new ToDetDAO().isExisit(htCondition,"PickStatus in ('O')");
						if (!updateFlag){
							updateFlag = new ToDetDAO().isExisit(htCondition,"PickStatus in ('C','N')");
							if (!updateFlag){
								String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
								updateFlag = new ToHdrDAO().update(updateHdr, htCondition, "");	
							} else {
								updateFlag = new ToDetDAO().isExisit(htCondition,"PickStatus in ('C')");
								if (!updateFlag){
									String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
									updateFlag = new ToHdrDAO().update(updateHdr, htCondition, "");
							} else {
								String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
								updateFlag = new ToHdrDAO().update(updateHdr, htCondition, "");
							}
						} 
						}else {
							String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
							updateFlag = new ToHdrDAO().update(updateHdr, htCondition, "");	
						}						
						}
					}
					}
					if(updateFlag) {
						Hashtable ht = new Hashtable();
						ht.put("TONO", toHdr.getTONO());
						ht.put("PLANT", plant);
					}
					if(updateFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_TO);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
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
						
						for (ToDet toorderdet : ToDetList) {
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.TO_UPD_ITEM);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, toHdr.getCustCode());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, toHdr.getJobNum());
							htRecvHis.put(IDBConstants.ITEM, toorderdet.getITEM());
							htRecvHis.put(IDBConstants.QTY, toorderdet.getQTYOR());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tono);
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
						resultJson.put("MESSAGE", "Consignment Order Updated Successfully.");
						resultJson.put("ERROR_CODE", "100");
						String message = "Consignment Order Updated Successfully.";
						if (ajax) {
							request.setAttribute("TONO", toHdr.getTONO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.CONSIGNMENT_ORDER);
							if ("btnSalesOpenEmail".equals(actionButton)) {
								String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
								if ("both".equals(sendAttachment) || "consignment".equals(sendAttachment)) {
									coPDFGenration(request, response, tono, "consignment");
								}
								if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
									coPDFGenration(request, response, tono, "invoice");
								}
							}
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../consignment/summary?msg=" + message);
						}
					}else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Consignment Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../consignment/summary?msg=" + message);
						}
					}
				}
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
		        if (ajax) {
					resultJson.put("MESSAGE", e.getMessage());
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}else {
					response.sendRedirect("../consignment/summary?msg=" + e.getMessage());
				}
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if(action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();				
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String tono = request.getParameter("r_tono");
				String tolno = request.getParameter("r_lnno");
				boolean insertFlag = false;
				
				Hashtable<String, String> htRemarksDel = new Hashtable<>();
				htRemarksDel.put(IDBConstants.PLANT, plant);
				htRemarksDel.put(IDBConstants.TODET_TONUM , tono);
				htRemarksDel.put(IDBConstants.TODET_TOLNNO, tolno);
				htRemarksDel.put(IDBConstants.TODET_ITEM, item);
				if(new ToDetDAO().isExisitToMultiRemarks(htRemarksDel)) {
					new ToDetDAO().deleteToMultiRemarks(htRemarksDel);
				}
				
				for(int i=0; i<remarks.length; i++) {
					Hashtable<String, String> htRemarks = new Hashtable<>();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.TODET_TONUM, tono);
					htRemarks.put(IDBConstants.TODET_TOLNNO, tolno);
					htRemarks.put(IDBConstants.TODET_ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(remarks[i]));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, username);
					insertFlag = new TOUtil().saveToMultiRemarks(htRemarks);
				}
				
				if(insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Consignment Order Created Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE",  e.getMessage());
		        resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson.toString());
            response.getWriter().flush();
            response.getWriter().close();
		}else if (action.equalsIgnoreCase("addinvoiceRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String invoice = request.getParameter("r_invoice");
				String invlno = request.getParameter("r_lnno");
				String tono = request.getParameter("r_tono");
				boolean insertFlag = false;

				Hashtable<String, String> htRemarksDel = new Hashtable<>();
				htRemarksDel.put(IDBConstants.PLANT, plant);
				htRemarksDel.put(IDBConstants.INVOICE, invoice);
				htRemarksDel.put(IDBConstants.LNNO, invlno);
				htRemarksDel.put(IDBConstants.ITEM, item);
				if (new InvoiceDAO().isExisitInvoiceMultiRemarks(htRemarksDel)) {
					new InvoiceDAO().deleteInvoiceMultiRemarks(htRemarksDel);
				}

				for (int i = 0; i < remarks.length; i++) {
					Hashtable<String, String> htRemarks = new Hashtable<>();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.INVOICE, invoice);
					htRemarks.put(IDBConstants.LNNO, invlno);
					htRemarks.put(IDBConstants.ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(remarks[i]));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, username);
					insertFlag = new InvoiceUtil().saveInvoiceMultiRemarks(htRemarks);
				}

				if (insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Invoice Order Created Successfully.");
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
		}
		else if(action.equalsIgnoreCase("removeAttachmentById")){
			System.out.println("Remove Attachments by ID");
			int ID=Integer.parseInt(request.getParameter("removeid"));
			ConsignmentAttachDAO consignmentAttachDAO = new ConsignmentAttachDAO();
			try {
//				Hashtable ht1 = new Hashtable();
				consignmentAttachDAO.deleteconsignmentAttachByPrimId(plant, Integer.toString(ID));
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");  
		
		}else if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID"); 
			int ID=Integer.parseInt(request.getParameter("attachid"));
			FileHandling fileHandling=new FileHandling(); 
			ConsignmentAttachDAO consignmentAttachDAO = new ConsignmentAttachDAO();
			List paymentAttachment = null;
			try {
				Hashtable ht1 = new Hashtable();
				ht1.put("ID", String.valueOf(ID));
				ht1.put("PLANT", plant);
				paymentAttachment = consignmentAttachDAO.getconsignmentAttachById(plant, String.valueOf(ID));
				Map billAttach=(Map)paymentAttachment.get(0);
				String filePath=(String) billAttach.get("FilePath");
				String fileType=(String) billAttach.get("FileType");
				String fileName=(String) billAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	  
		}else if (action.equalsIgnoreCase("Delete")) {
			String tono = StrUtils.fString(request.getParameter("tono")).trim();
			ToDetDAO toDetDao = new ToDetDAO();
			TOUtil tOUtil = new TOUtil();
			JSONObject resultJson = new JSONObject();
			try {
				Hashtable htToHrd = new Hashtable();
				htToHrd.put(IDBConstants.PLANT, plant);
				htToHrd.put(IDBConstants.TODET_TONUM, tono);
				boolean isValidOrder;			
				isValidOrder = new ToHdrDAO().isExisit(htToHrd, "");			
				boolean isOrderInProgress = toDetDao.isExisit(htToHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");			
				if (isValidOrder) {
					if (!isOrderInProgress) {
								 
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("TONO", tono);
//							String query = "tono,custCode";
//							ArrayList al = tOUtil.getToHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = tOUtil.removeRow(plant, tono, username);
							if (value) {
								resultJson.put("MESSAGE", "Consignment Order Deleted Successfully.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								resultJson.put("MESSAGE", "Consignment Order Not Deleted.");
								resultJson.put("ERROR_CODE", "97");
							}
						 
					} else {
						resultJson.put("MESSAGE", "Consignment Order Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Consignment Order Not Deleted.");
					resultJson.put("ERROR_CODE", "99");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("MESSAGE",  e.getMessage());
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
		
        else if(action.equalsIgnoreCase("import")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importTransferOrderExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
}
		
 else if(action.equalsIgnoreCase("consignsummary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConsignmentOrdSummaryWOPrice.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
}
		
 else if(action.equalsIgnoreCase("consignsummarywithprice")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConsignmentOrdSummaryWithPrice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
 else if(action.equalsIgnoreCase("orderpick")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/BulkTransferOrderPickReceive.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 else if(action.equalsIgnoreCase("orderreversal")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/TransferOrderReverse.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 else if(action.equalsIgnoreCase("closeorder")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrderTransfer.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 else if(action.equalsIgnoreCase("consignorder")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printTO.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 else if(action.equalsIgnoreCase("consignorderwithprice")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printTOWithPrice.jsp");
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
		boolean isInternalRequest = !"".equals(StrUtils.fString(request.getParameter("INTERNAL_REQUEST")));
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		if("".equals(username) && isInternalRequest)
		{
			username= StrUtils.fString(request.getParameter("LOGIN_USER"));
		}
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		if("".equals(plant) && isInternalRequest)
		{
			plant= StrUtils.fString(request.getParameter("PLANT"));
		}
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		if("".equals(currency) && isInternalRequest)
		{
			currency= StrUtils.fString(request.getParameter("BASE_CURRENCY"));
		}
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		if("".equals(region) && isInternalRequest)
		{
			region= StrUtils.fString(request.getParameter("REGION"));
		}
		
		if (action.equalsIgnoreCase("CheckOrderno")) {
			JSONObject jsonObjectResult = new JSONObject();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("TONO", orderno);
				if (new CustomerCreditnoteDAO().isExisitTo(ht)) {
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
				String gst = new selectBean().getGST("SALES",plant);
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
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createConsignment.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("Auto-Generate")) {
			String tono = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				tono = _TblControlDAO.getNextOrder(plant,username,IConstants.CONSIGNMENT);
				json.put("TONO", tono);
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
		}else if(action.equalsIgnoreCase("getSalesOrderRemarks")) {
			String tono = "", tolnno="";//, item="";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				tono = StrUtils.fString(request.getParameter("TONO"));
				tolnno = StrUtils.fString(request.getParameter("TOLNO"));
//				item = StrUtils.fString(request.getParameter("ITEM"));
				
				Hashtable<String, String> ht = new Hashtable<>();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TODET_TONUM, tono);
				ht.put(IDBConstants.TODET_TOLNNO, tolnno);
				
				List al= new ToDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
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
		}else if (action.equalsIgnoreCase("getInvoiceOrderRemarks")) {
			String invoice = "", invlno = "",tono="",item="";//, item = "";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			List al = new ArrayList();
			try {
				invoice = StrUtils.fString(request.getParameter("INVOICE"));
				invlno = StrUtils.fString(request.getParameter("INLNO"));
				tono = StrUtils.fString(request.getParameter("TONO"));
				item = StrUtils.fString(request.getParameter("ITEM"));

				Hashtable<String, String> ht = new Hashtable<>();
				ht.put(IDBConstants.PLANT, plant);
//				if (tono.contains("C")) {
//					ht.put("TONO", tono);
//					ht.put("TOLNNO", invlno);
//					ht.put("ITEM", item);
//				}else {
					ht.put(IDBConstants.INVOICE, invoice);
					ht.put(IDBConstants.LNNO, invlno);
//					}

				
//				if (tono.contains("C")) {
//					 al= new ToDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
//				}else {
					 al = new InvoiceDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
//				}
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
		}else if(action.equalsIgnoreCase("summary")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String LOC = StrUtils.fString(request.getParameter("LOC"));    /*RESVI*/
				String LOC_ID = StrUtils.fString(request.getParameter("LOC_ID"));   /*RESVI*/
				String reference = StrUtils.fString(request.getParameter("REFERENCE"));
				String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String fdate="",tdate="";
				ToHDRService toHDRService = new ToHdrServiceImpl(); 
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();		
				try {
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("TONO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					if(StrUtils.fString(LOC).length() > 0)	ht.put("FROMWAREHOUSE", LOC);  /*RESVI*/
					if(StrUtils.fString(LOC_ID).length() > 0)	ht.put("TOWAREHOUSE", LOC_ID);  /*RESVI*/
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
					else
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);*/
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
					List<ToHdr> doHeaders =  toHDRService.getToHdr(ht, fdate, tdate);
					if(doHeaders.size() > 0) {
						for(ToHdr toHdr : doHeaders) {
							JSONObject json = new JSONObject();
							FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(toHdr.getTAXID());
							List<ToDet> dodetail = new ToDetDAO().getToDetById(plant, toHdr.getTONO());
							double totalAmount =0;
							double subtotal =0;
							for (ToDet toDet : dodetail) {	
								if(toDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = toDet.getUNITPRICE()*toDet.getQTYOR().doubleValue();
									double discount = (ucost/100)*toDet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
								}else {
									double ucost = toDet.getUNITPRICE()*toDet.getQTYOR().doubleValue();
									double discount = toDet.getDISCOUNT();
									subtotal = subtotal + (ucost  - discount);
								}
							}
							
							if(toHdr.getITEM_RATES() == 1){
								subtotal = (subtotal*100)/(toHdr.getCONSIGNMENT_GST()+100);
							}
							
							double dorderdiscountcost = 0;
							if(toHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
								dorderdiscountcost = (subtotal/100)*toHdr.getORDERDISCOUNT();
							}else {
								dorderdiscountcost = toHdr.getORDERDISCOUNT();
							}
							
							double discountcost = 0;
							if(toHdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
								discountcost = (subtotal/100)*toHdr.getDISCOUNT();
							}else {
								discountcost = toHdr.getDISCOUNT();
							}
							
							double totax =0;
							if(toHdr.getTAXID() != 0) {
								if(fintaxtype.getSHOWTAX() == 1){
									
									totax = (subtotal/100)*toHdr.getCONSIGNMENT_GST();
									
									if(toHdr.getISSHIPPINGTAX() == 1){
										totax = totax + ((toHdr.getSHIPPINGCOST()/100)*toHdr.getCONSIGNMENT_GST());
									}
									
									if(toHdr.getISORDERDISCOUNTTAX() == 1){
										totax = totax - ((dorderdiscountcost/100)*toHdr.getCONSIGNMENT_GST());
									}
									
									if(toHdr.getISDISCOUNTTAX() == 1){
										totax = totax - ((discountcost/100)*toHdr.getCONSIGNMENT_GST());
									}
								}
							}

							totalAmount = (subtotal + toHdr.getSHIPPINGCOST() + totax + toHdr.getADJUSTMENT()) - (dorderdiscountcost+discountcost) ;
							
							json.put("DATE", toHdr.getDELDATE());
							json.put("TONO", toHdr.getTONO());
							json.put("CUSTOMER", toHdr.getCustName());
							/*if(toHdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(toHdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", toHdr.getORDER_STATUS());
							json.put("FROMLOCATION", toHdr.getFROMWAREHOUSE());
							json.put("TOLOCATION", toHdr.getTOWAREHOUSE());
							json.put("DELIVERY_DATE", toHdr.getDELIVERYDATE());
							json.put("EXCHANGE_RATE", Numbers.toMillionFormat(toHdr.getCURRENCYUSEQT(), numberOfDecimal));
							double amt = totalAmount*toHdr.getCURRENCYUSEQT();
							json.put("ORDAMT", toHdr.getCURRENCYID() + "" + Numbers.toMillionFormat(amt, numberOfDecimal));
//									Numbers.toMillionFormat(amt, numberOfDecimal) +"("+toHdr.getCURRENCYID()+")");
							json.put("AMOUNT", Numbers.toMillionFormat(totalAmount, numberOfDecimal));
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
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/consignmentSummary.jsp");
				rd.forward(request, response);
			}			
		}else if(action.equalsIgnoreCase("detail")) {
			String tono = StrUtils.fString(request.getParameter("tono"));
			String imagePath = "", numberOfDecimal = "";
			ToHDRService toHDRService = new ToHdrServiceImpl();
			ToDetService toDetService = new ToDetServiceImpl();
			ToHdr toHdr = new ToHdr();
			Map plntMap = new HashMap();
			Map toHdrDetails = new HashMap();
			Map toHdrDetailsdo = new HashMap();
			ArrayList custDetails = new ArrayList();
			ArrayList shippingCustDetails = new ArrayList();
			List<ToDet> toDetList = new ArrayList<ToDet>();
			ArrayList itemList = new ArrayList();
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				toHdr = toHDRService.getToHdrById(plant, tono);
				toDetList = toDetService.getToDetById(plant, tono);
				
				ArrayList plntList = new PlantMstDAO().getPlantMstDetails(plant);
				plntMap = (Map) plntList.get(0);
				custDetails = new CustUtil().getCustomerDetailsForTO(tono, plant);
				shippingCustDetails = new MasterUtil().getTransferShippingDetails(tono, toHdr.getSHIPPINGCUSTOMER(), plant);
				
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				
				toHdrDetails = new TOUtil().getReceiptHdrDetails(plant,"Upon Creation");
				toHdrDetailsdo = new TOUtil().getTOReceiptHdrDetails(plant);
				
				/*Item Details*/
				for(ToDet todet: toDetList){					
					String OBDiscount = 
					new TOUtil().getOBDiscountSelectedItemByCustomer(plant,toHdr.getCustCode(),todet.getITEM(),"OUTBOUND");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new TOUtil().getConvertedUnitCostForProduct(plant,todet.getTONO(),todet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new TOUtil().getConvertedUnitCostForProductWTC(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new TOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", todet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(todet.getITEM(),plant);
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
				RequestDispatcher rd;
				if (isInternalRequest) {
					rd = request.getRequestDispatcher("/jsp/detailConsignment.jsp?tono=" + request.getParameter("TONO") + "&PLANT="  
							+ StrUtils.fString(request.getParameter("PLANT")) + "&SYSTEMNOW=" 
							+ StrUtils.fString(request.getParameter("SYSTEMNOW")) + "&BASE_CURRENCY=" 
							+ StrUtils.fString(request.getParameter("BASE_CURRENCY")) + "&LOGIN_USER=" 
						+ StrUtils.fString(request.getParameter("LOGIN_USER")) +  "&INTERNAL_REQUESET=TRUE");
				}else {
					rd = request.getRequestDispatcher("/jsp/detailConsignment.jsp");
				}
				request.setAttribute("ToHdr", toHdr);
				request.setAttribute("ToDetList", toDetList);
				request.setAttribute("PLNTMAP", plntMap);
				request.setAttribute("TOHDRDETAILS", toHdrDetails);	
				request.setAttribute("TOHDRDETAILSDO", toHdrDetailsdo);	
				request.setAttribute("CUSTDETAILS", custDetails);
				request.setAttribute("SHIPPINGCUSTDETAILS", shippingCustDetails);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("ItemList", itemList);
				
				request.setAttribute("IMAGEPATH", imagePath);
				
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}else if(action.equalsIgnoreCase("edit")) {
			
			String tono = StrUtils.fString(request.getParameter("tono"));
			String imagePath = "", numberOfDecimal = "";
			ToHDRService toHDRService = new ToHdrServiceImpl();
			ToDetService toDetService = new ToDetServiceImpl();
			ToHdr toHdr = new ToHdr();
//			Map plntMap = new HashMap();
//			Map toHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<ToDet> toDetList = new ArrayList<ToDet>();
			String msg= "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				toHdr = toHDRService.getToHdrById(plant, tono);
				toDetList = toDetService.getToDetById(plant, tono);
				String gst = new selectBean().getGST("SALES",plant);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new ConsignmentAttachDAO().getconsignmentAttachByTONO(plant, tono);
				
				/*Item Details*/
				for(ToDet todet: toDetList){					
					String OBDiscount = 
//					new TOUtil().getOBDiscountSelectedItemByCustomer(plant,toHdr.getCustCode(),todet.getITEM(),"OUTBOUND");
					new TOUtil().getOBDiscountSelectedItemByCustomer(plant,toHdr.getCustCode(),todet.getITEM(),"TRANSFER");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new TOUtil().getConvertedUnitCostForProduct(plant,todet.getTONO(),todet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new TOUtil().getConvertedUnitCostForProductWTC(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new TOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", todet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(todet.getITEM(),plant);
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
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editConsignment.jsp");
				request.setAttribute("ToHdr", toHdr);
				request.setAttribute("ToDetList", toDetList);
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
		}if(action.equalsIgnoreCase("consignmentwithprice")) {
			
			/*boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(ajax) {
				
			String	DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
			String	PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
			String  FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
			String  TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
			String	USER = StrUtils.fString(request.getParameter("USER"));
			String	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String	CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			String	status = StrUtils.fString(request.getParameter("STATUS"));
			String	sCustomerTypeId  = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
			String fdate="",tdate="",taxby="";
				HTReportUtil movHisUtil = new HTReportUtil();
				ArrayList movQryList = new ArrayList();
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				ShipHisDAO _ShipHisDAO=new ShipHisDAO();
				ArrayList prodGstList = new ArrayList();
				PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
			try {
				taxby=_PlantMstDAO.getTaxBy(plant);
				JSONObject json = new JSONObject();
				if (StrUtils.fString(ORDERNO).length() > 0)    ht.put("a.PONO", ORDERNO);
				if (StrUtils.fString(status).length() > 0)	ht.put("b.STATUS", status);
				if (StrUtils.fString(sCustomerTypeId).length() > 0)	ht.put("CUSTTYPE", sCustomerTypeId);
			
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				
				
				if (FROM_DATE == null)
					FROM_DATE = "";
				else
					FROM_DATE = FROM_DATE.trim();
			    String curDate =DateUtils.getDateMinusDays();
				if (FROM_DATE.length() < 0 || FROM_DATE == null
						|| FROM_DATE.equalsIgnoreCase(""))
					FROM_DATE = curDate;
				if (FROM_DATE.length() > 5)
					
					fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
					
				if (TO_DATE == null)
					TO_DATE = "";
				else
					TO_DATE = TO_DATE.trim();
				if (TO_DATE.length() > 5)
					
				
				if(DIRTYPE.length()<=0){
					DIRTYPE = "ISSUEDHISTORY";
					}
				movQryList = movHisUtil.getConsignmentSummary(ht, fdate,tdate,plant,CUSTOMER);
				if (movQryList.size() > 0) {
					float subtotal=0;
					double gst=0,total=0;
					float gsttotal=0;
					float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
					int k=0;
					
					for(int i =0; i<movQryList.size(); i++) {
						
						Map arrCustLine = (Map)movQryList.get(i);
						double subtotal1 =  Double.parseDouble(((String) arrCustLine.get("subtotal").toString())) ;
						
						if(taxby.equalsIgnoreCase("BYORDER"))
						{
							gstpercentage =  Float.parseFloat(((String) arrCustLine.get("consignment_gst").toString())) ;
							//gst = (subtotal1*gstpercentage)/100;
							gst =  Double.parseDouble(((String) arrCustLine.get("taxval").toString())) ;
						}
						else
						{
							prodGstList = _ShipHisDAO.getShippingProductGST(plant,(String) arrCustLine.get("tono").toString());
							prodgstsubtotal1=0;
							for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
								Map prodGstArr = (Map)prodGstList.get(jCnt);
								int jIndex = jCnt + 1;
								prodgstsubtotal1=prodgstsubtotal1+Float.parseFloat(((String) prodGstArr.get("subtotal").toString()));
								
							}
							 gst=prodgstsubtotal1;
						}
						
						
						total = subtotal1+gst;
						String tono = (String) arrCustLine.get("tono");
						String GSTValue = StrUtils.addZeroes(gst, numberOfDecimal);
						
						 String GSTValue = String.valueOf(gst); 
						String TOTALValue =   StrUtils.addZeroes(total, numberOfDecimal); 
						 String TOTALValue =  String.valueOf(total);  
						String SUBTOTAL = (String)arrCustLine.get("subtotal");
						
						float gstVal="".equals(GSTValue) ? 0.0f :  Float.parseFloat(GSTValue);
						float totalVal="".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
						float subTotalVal="".equals(SUBTOTAL) ? 0.0f :  Float.parseFloat(SUBTOTAL);
						
							if (gstVal == 0f) {
								GSTValue = "0.00000";
							} else {
								GSTValue = GSTValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							}

							double tot = Double.parseDouble(TOTALValue);
							TOTALValue = StrUtils.addZeroes(tot, numberOfDecimal);
							
							double subtot = Double.parseDouble(SUBTOTAL);
							SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
							
							double GSTTAX = Double.parseDouble(GSTValue);
							GSTValue = StrUtils.addZeroes(GSTTAX, "3");
					
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PONO", (String)arrCustLine.get("pono"));
					resultJsonInt.put("CUSTNAME", (String)arrCustLine.get("custname"));
					resultJsonInt.put("RECVDATE", (String)arrCustLine.get("recvdate"));
					resultJsonInt.put("SUBTOTAL", SUBTOTAL);
					resultJsonInt.put("GSTVALUE", GSTValue);
					resultJsonInt.put("TOTALVALUE", TOTALValue);
					resultJsonInt.put("ISSUEDBY", (String)arrCustLine.get("issuedby"));
					jsonArray.add(resultJsonInt);
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
*/				try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/consignmentSumry_IssueWPrice.jsp");
				rd.forward(request, response);
				}catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500);
				}
			//}			
		}
		if(action.equalsIgnoreCase("consignmentwithoutprice")) {
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConsignmentSummaryWithoutPrice.jsp");
				rd.forward(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}

		}else if(action.equalsIgnoreCase("getconsignmentRemarks")) {
			String tono = "", tolnno="";//, item="";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				tono = StrUtils.fString(request.getParameter("TONO"));
				tolnno = StrUtils.fString(request.getParameter("TOLNO"));
//				item = StrUtils.fString(request.getParameter("ITEM"));
				
				Hashtable<String, String> ht = new Hashtable<>();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.TODET_TONUM, tono);
				ht.put(IDBConstants.TODET_TOLNNO, tolnno);
				
				List al= new ToDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
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
		}else if(action.equalsIgnoreCase("copy")) {
			
			String tono = StrUtils.fString(request.getParameter("tono"));
			String imagePath = "", numberOfDecimal = "";
			ToHDRService toHDRService = new ToHdrServiceImpl();
			ToDetService toDetService = new ToDetServiceImpl();
			ToHdr toHdr = new ToHdr();
//			Map plntMap = new HashMap();
//			Map toHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<ToDet> toDetList = new ArrayList<ToDet>();
			String msg= "", newTono = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				toHdr = toHDRService.getToHdrById(plant, tono);
				toDetList = toDetService.getToDetById(plant, tono);
				String gst = new selectBean().getGST("SALES",plant);
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newTono = new TblControlDAO().getNextOrder(plant,username,IConstants.CONSIGNMENT);
				toHdr.setTONO(newTono);
				toHdr.setJobNum(tono);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new ConsignmentAttachDAO().getconsignmentAttachByTONO(plant, tono);
				
				/*Item Details*/
				for(ToDet todet: toDetList){					
					String OBDiscount = 
					new TOUtil().getOBDiscountSelectedItemByCustomer(plant,toHdr.getCustCode(),todet.getITEM(),"OUTBOUND");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new TOUtil().getConvertedUnitCostForProduct(plant,todet.getTONO(),todet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new TOUtil().getConvertedUnitCostForProductWTC(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new TOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", todet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(todet.getITEM(),plant);
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
                    todet.setTONO(newTono);
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/copyConsignment.jsp");
				request.setAttribute("ToHdr", toHdr);
				request.setAttribute("ToDetList", toDetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("oldtono", tono);
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
		} else if(action.equalsIgnoreCase("convertToInvocie")) {
			
			String tono = StrUtils.fString(request.getParameter("tono"));
			String imagePath = "", numberOfDecimal = "";
			ToHDRService toHDRService = new ToHdrServiceImpl();
			ToDetService toDetService = new ToDetServiceImpl();
			ToHdr toHdr = new ToHdr();
//			Map plntMap = new HashMap();
//			Map toHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<ToDet> toDetList = new ArrayList<ToDet>();
			String msg= "", newTono = "",deldate = "",collectionTime = "";
			
			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				toHdr = toHDRService.getToHdrById(plant, tono);
				toDetList = toDetService.getToDetById(plant, tono);
				String gst = new selectBean().getGST("SALES",plant);
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();
				
				newTono = new TblControlDAO().getNextOrder(plant,username,IConstants.CONSIGNMENT);
				toHdr.setTONO(newTono);
				toHdr.setJobNum(tono);
				
				ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
				ArrayList slList =  new MasterUtil().getSalesLocationList("",plant,"");
				ArrayList ccList =  new MasterUtil().getCountryList("",plant,region);
				ArrayList bankList =  new MasterUtil().getBankList("",plant);
				ArrayList itemList = new ArrayList();				
				List attachmentList = new ConsignmentAttachDAO().getconsignmentAttachByTONO(plant, tono);
				
				/*Item Details*/
				for(ToDet todet: toDetList){					
					String OBDiscount = 
					new TOUtil().getOBDiscountSelectedItemByCustomer(plant,toHdr.getCustCode(),todet.getITEM(),"OUTBOUND");
					
					String discounttype="";
					OBDiscount = (OBDiscount.length()==0) ? "0.0" : OBDiscount;
	                int plusIndex = OBDiscount.indexOf("%");
	                if (plusIndex != -1) {
	            	 	OBDiscount = OBDiscount.substring(0, plusIndex);
	            	 	discounttype = "BYPERCENTAGE";
	                }
					
					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost", 
							new TOUtil().getConvertedUnitCostForProduct(plant,todet.getTONO(),todet.getITEM())) ;
					itemMap.put("ConvertedUnitCostWTC", 
							new TOUtil().getConvertedUnitCostForProductWTC(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", 
							new TOUtil().getminSellingConvertedUnitCostForProductByCurrency(plant,todet.getTONO(),todet.getITEM()));
					itemMap.put("outgoingOBDiscount",
							OBDiscount);
					Hashtable ht = new Hashtable();
            	 	ht.put("item", todet.getITEM());
            	 	ht.put("plant", plant);
            	 	Map m = new EstDetDAO().getEstQtyByProduct(ht);
            	 	itemMap.put("EstQty",(String) m.get("ESTQTY"));
            	 	
            	 	m = new InvMstDAO().getAvailableQtyByProduct(ht);
            	 	itemMap.put("AvlbQty",(String) m.get("AVLBQTY"));
            	 	
            	 	List listItem = new ItemUtil().queryItemMstDetails(todet.getITEM(),plant);
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
                    todet.setTONO(newTono);
				}
				/*Item Details*/
				
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/consignmentToInvoice.jsp");
				request.setAttribute("ToHdr", toHdr);
				request.setAttribute("ToDetList", toDetList);
				request.setAttribute("NUMBEROFDECIMAL", numberOfDecimal);
				request.setAttribute("GST", gst);
				request.setAttribute("Currency", currency);
				request.setAttribute("Msg", msg);
				request.setAttribute("DelDate", deldate);
				request.setAttribute("CollectionTime", collectionTime);
				request.setAttribute("oldtono", tono);
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
		
else if(action.equalsIgnoreCase("import")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importTransferOrderExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
}
		
else if(action.equalsIgnoreCase("consignsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConsignmentOrdSummaryWOPrice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
else if(action.equalsIgnoreCase("consignsummarywithprice")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConsignmentOrdSummaryWithPrice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
else if(action.equalsIgnoreCase("orderpick")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/BulkTransferOrderPickReceive.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
else if(action.equalsIgnoreCase("orderreversal")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/TransferOrderReverse.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
else if(action.equalsIgnoreCase("closeorder")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrderTransfer.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
else if(action.equalsIgnoreCase("consignorder")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/printTO.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
else if(action.equalsIgnoreCase("consignorderwithprice")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/printTOWithPrice.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	}
	
	
	private JSONObject getEditSODetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        ToHDRService toHDRService = new ToHdrServiceImpl(); 
		ToDetService toDetService = new ToDetServiceImpl();
        ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		 ItemUtil itemUtil = new ItemUtil();
		 ItemMstUtil itemMstUtil = new ItemMstUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String tono = StrUtils.fString(request.getParameter("TONO")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List<ToDet> todetail = toDetService.getToDetById(plant, tono);
		    ToHdr doheader = toHDRService.getToHdrById(plant, tono);
		    if (todetail.size() > 0) {
		    	for(ToDet toDet:todetail) {   
					JSONObject resultJsonInt = new JSONObject();
					
					String catlogpath = itemMstDAO.getcatlogpath(plant, toDet.getITEM());
					String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath);
					String itemdesc="";
					String unitcost ="0";
					ArrayList listQry = itemMstUtil.getProductDetailsWithStockonHand(toDet.getITEM(),plant,itemdesc);
		        	if (listQry.size() > 0) {
		        		for(int i =0; i<listQry.size(); i++) {
		        			Map m=(Map)listQry.get(i);
		        			unitcost = StrUtils.fString((String)m.get("COST"));
		        		}
		        	}
					
					resultJsonInt.put("LNNO", toDet.getTOLNNO());
					resultJsonInt.put("LNSTATUS", toDet.getLNSTAT());
					resultJsonInt.put("QTYRC", StrUtils.addZeroes(toDet.getQTYRC().doubleValue(), "3"));
					resultJsonInt.put("ITEM", toDet.getITEM());
					resultJsonInt.put("ITEMDESC", toDet.getItemDesc());
					resultJsonInt.put("ACCOUNTNAME", toDet.getACCOUNT_NAME());
					resultJsonInt.put("UOM", toDet.getUNITMO());
					resultJsonInt.put("QTY", StrUtils.addZeroes(toDet.getQTYOR().doubleValue(), "3"));
					resultJsonInt.put("PDELDATE", toDet.getPRODUCTDELIVERYDATE());
					resultJsonInt.put("UNITCOST", StrUtils.addZeroes(Double.valueOf(unitcost) ,numberOfDecimal));	
					resultJsonInt.put("ITEMDISCOUNTTYPE", toDet.getDISCOUNT_TYPE());
					resultJsonInt.put("TAXTYPE", toDet.getTAX_TYPE());
					resultJsonInt.put("CATLOGPATH", cpath);
					
					if(toDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(toDet.getDISCOUNT(), "3"));
						double amount = (Double.valueOf(unitcost)*(toDet.getQTYOR().doubleValue())) - (((Double.valueOf(unitcost)*(toDet.getQTYOR().doubleValue()))/100)*toDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}else {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(toDet.getDISCOUNT(), numberOfDecimal));
						double amount = (Double.valueOf(unitcost)*(toDet.getQTYOR().doubleValue())) - (toDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}
					String IBDiscount=new POUtil().getIBDiscountSelectedItemVNO(plant,doheader.getCustCode(),toDet.getITEM());
		            String discounttype="";
		                
		            int plusIndex = IBDiscount.indexOf("%");
		            if (plusIndex != -1) {
		               	IBDiscount = IBDiscount.substring(0, plusIndex);
		               	discounttype = "BYPERCENTAGE";
		            }
					List listItem = itemUtil.queryItemMstDetailsforpurchase(toDet.getITEM(),plant);
                    Vector arrItem    = (Vector)listItem.get(0);
                    if(arrItem.size()>0){
                    	resultJsonInt.put("minstkqty",StrUtils.fString((String)arrItem.get(8)));
                    	resultJsonInt.put("maxstkqty",StrUtils.fString((String)arrItem.get(21)));
                    	resultJsonInt.put("stockonhand",StrUtils.fString((String)arrItem.get(22)));
                    	resultJsonInt.put("customerdiscount",IBDiscount);
                    	resultJsonInt.put("discounttype",discounttype);
                    	resultJsonInt.put("incommingqty",StrUtils.fString((String)arrItem.get(41)));
                    	resultJsonInt.put("EstQty","0.00");
                    	resultJsonInt.put("AvlbQty","0.00");
                    	 
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
	
//	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
//		JSONObject resultJson = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        JSONArray jsonArrayErr = new JSONArray();
//    	
//        TOUtil toUtil = new TOUtil();
//        StrUtils strUtils = new StrUtils();
//        toUtil.setmLogger(mLogger);
//        
//        try {
//        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		    String tono = StrUtils.fString(request.getParameter("TONO")).trim();
//		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
//		    
//		    Hashtable ht=new Hashtable();
//		     String extCond="";
//		     ht.put("PLANT",plant);
//		     if(tono.length()>0) extCond=" AND plant='"+plant+"' and tono like '"+tono+"%' ";
//		     if(cname.length()>0) extCond=" AND CustName = '"+cname+"' ";
//		     //extCond=extCond+" and STATUS <>'C'";
//		     extCond=extCond+" ORDER BY CONVERT(date, CollectionDate, 103) desc";
//		     ArrayList listQry = toUtil.getToHdrDetails("tono,CustName,CustCode,jobNum,status,collectiondate",ht,extCond);
//		     if (listQry.size() > 0) {
//		    	 for(int i =0; i<listQry.size(); i++) {		   
//				  Map m=(Map)listQry.get(i);
//				  tono = (String)m.get("tono");
//				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
//				  String custcode = (String)m.get("custcode");
//				  String orderdate = (String)m.get("collectiondate");
//				  String jobNum = (String)m.get("jobNum");
//				  String status = (String)m.get("status");
//				  JSONObject resultJsonInt = new JSONObject();
//				  resultJsonInt.put("TONO", tono);
//				  resultJsonInt.put("CUSTNAME", custName);
//				  resultJsonInt.put("CUSTCODE", custcode);
//				  resultJsonInt.put("ORDERDATE", orderdate);
//				  resultJsonInt.put("JOBNUM", jobNum);
//				  resultJsonInt.put("STATUS", status);				  
//				  jsonArray.add(resultJsonInt);
//			     }	     
//			     resultJson.put("orders", jsonArray);
//	             JSONObject resultJsonInt = new JSONObject();
//	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
//	             resultJsonInt.put("ERROR_CODE", "100");
//	             jsonArrayErr.add(resultJsonInt);
//	             resultJson.put("errors", jsonArrayErr);
//		     } else {
//                 JSONObject resultJsonInt = new JSONObject();
//                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
//                 resultJsonInt.put("ERROR_CODE", "99");
//                 jsonArrayErr.add(resultJsonInt);
//                 jsonArray.add("");
//                 resultJson.put("items", jsonArray);
//                 resultJson.put("errors", jsonArrayErr);
//		     }
//        }catch (Exception e) {
//            resultJson.put("SEARCH_DATA", "");
//            JSONObject resultJsonInt = new JSONObject();
//            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
//            resultJsonInt.put("ERROR_CODE", "98");
//            jsonArrayErr.add(resultJsonInt);
//            resultJson.put("ERROR", jsonArrayErr);
//        }
//        return resultJson;
//	}
	
//	private MLogger mLogger = new MLogger();
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		
	}

	public void coPDFGenration(HttpServletRequest request, HttpServletResponse response, String TONO, String attachmentType)
			 throws Exception{
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (ajax) {
			HttpSession session = ((HttpServletRequest) request).getSession(false);
			String rootURI = HttpUtils.getRootURI(request);
			String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
			String billDetailURL = rootURI + "/consignment/detail?tono=" + TONO + "&PLANT="
					+ plant  + "&SYSTEMNOW="
					+ StrUtils.fString((String) session.getAttribute("SYSTEMNOW")) + "&BASE_CURRENCY="
					+ StrUtils.fString((String) session.getAttribute("BASE_CURRENCY")) + "&LOGIN_USER="
					+ StrUtils.fString((String) session.getAttribute("LOGIN_USER")) +  "&INTERNAL_REQUEST=TRUE&ATTACHMENT_TYPE=" + attachmentType;
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
				barcode = BarcodeFactory.createCode128(TONO);
				barcode.setDrawingText(false);
				barcode.setBorder(BorderFactory.createEmptyBorder());
			    image = BarcodeImageHandler.getImage(barcode);
			    String base64 = ImageUtil.imgToBase64String(image, "png");
				String barCodeTag = "<img id=\"barCode\" style=\"width: 215px; height: 65px;\">";
				int barcodeStartIndex = sbData.indexOf(barCodeTag);
				int barcodeEndIndex = barcodeStartIndex + barCodeTag.length();
				if (barcodeStartIndex != -1 && barcodeEndIndex != -1) {
					sbData.replace(barcodeStartIndex, barcodeEndIndex, "<img id=\"barCode\" style=\"width:215px;height:65px;\" src=\"data:image/png;base64," + base64 + "\"/>");
				}
				String html = sbData.toString();
				Pattern pattern = Pattern.compile("<td class=\"text-left code\"><a>(.*?)</a></td>", Pattern.DOTALL);
				Matcher matcher = pattern.matcher(html);
				while (matcher.find()) {
					barcode = BarcodeFactory.createCode128(matcher.group(1));
				    image = BarcodeImageHandler.getImage(barcode);
				    base64 = ImageUtil.imgToBase64String(image, "png");
				    html = html.replaceAll("<a>" + matcher.group(1) + "<\\/a>", "<img style=\"width:215px;height:65px;\" src=\"data:image/png;base64," + base64  + "\"/>");
				}
				if ("consignment".equals(attachmentType)) {
					PdfUtil.createPDF(html, DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Consignment_" + TONO + ".pdf", rootURI, PageSize.A4, PageSize.A4.getWidth());
				}
				if ("invoice".equals(attachmentType)) {
					PdfUtil.createPDF(html, DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + TONO + ".pdf", rootURI, PageSize.A4, PageSize.A4.getWidth());
				}
			} catch (BarcodeException e) {
				e.printStackTrace();
			} catch (OutputException e) {
				e.printStackTrace();
			}
		}

	}
}
