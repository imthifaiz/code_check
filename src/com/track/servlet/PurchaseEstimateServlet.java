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
import javax.servlet.http.HttpServlet;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoEstHdrDAO;//Resvi
import com.track.dao.PoEstDetDAO;//Resvi
import com.track.dao.PurchaseEstimateAttachDAO;//Resvi
import com.track.dao.TblControlDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.PoEstHdr;//Resvi
import com.track.db.object.PoEstDet;//Resvi
import com.track.db.object.PoEstDetRemarks;//Resvi
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PoEstUtil;//Resvi
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/purchaseorderestimate/*")
@SuppressWarnings({"rawtypes", "unchecked"})
public class PurchaseEstimateServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

		PoEstHdrDAO poEstHdrDAO= new PoEstHdrDAO();
		PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
		PurchaseEstimateAttachDAO purchaseEstimateAttachDAO = new PurchaseEstimateAttachDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		MasterDAO _MasterDAO = new MasterDAO();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (action.equalsIgnoreCase("save") || action.equalsIgnoreCase("copy")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String empName="",empno = "";
			List<Hashtable<String,String>> purchaseEstimateAttachmentList = null;
			List<Hashtable<String,String>> purchaseEstimateAttachmentInfoList = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				PoEstHdr poEsthdr = new PoEstHdr();

				List POESTLNNO = new ArrayList(), item = new ArrayList(), unitcost = new ArrayList(),
						qtyor = new ArrayList(), unitmo = new ArrayList(), pddate = new ArrayList(),
						discount = new ArrayList(), dtype = new ArrayList(), accname = new ArrayList(),
						taxtype = new ArrayList(), chkdPOESTNO = new ArrayList();
				String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
						shipworkphone="",shipcountry="",shiphpno="",shipemail="";
				int POESTLNNOcount = 0, chkdPOESTNOCount = 0;
//				int accnamecount = 0, dtypecount = 0, itemcount = 0, unitcostcount = 0, qtyorcount = 0, unitmocount = 0, pddatecount = 0,
//				discountcount = 0,  taxtypecount = 0, ;

				purchaseEstimateAttachmentList = new ArrayList<Hashtable<String, String>>();
				purchaseEstimateAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
//					DoHdr doHdr = new DoHdr();
					String revgersecharge = "0";
					String goodsimport = "0";
					ut = DbBean.getUserTranaction();
					ut.begin();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* POHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								poEsthdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("POESTNO")) {
								poEsthdr.setPOESTNO(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								poEsthdr.setCustName(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								poEsthdr.setCustCode(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								poEsthdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PERSON_INCHARGE")) {
								poEsthdr.setPersonInCharge(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TELNO")) {
								poEsthdr.setContactNum(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD1")) {
								poEsthdr.setAddress(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD2")) {
								poEsthdr.setAddress2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD3")) {
								poEsthdr.setAddress3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								poEsthdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								poEsthdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								poEsthdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								poEsthdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								poEsthdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								poEsthdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								poEsthdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								poEsthdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								poEsthdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								poEsthdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								poEsthdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								poEsthdr.setEXPIREDATE(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								poEsthdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}
							
                            if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								
								empno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empName = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								poEsthdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								poEsthdr.setORDERDISCOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								poEsthdr.setSHIPPINGCOST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								poEsthdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								poEsthdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								poEsthdr.setTAXTREATMENT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								poEsthdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								poEsthdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								poEsthdr.setADJUSTMENT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
								poEsthdr.setISDISCOUNTTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
								poEsthdr.setISSHIPPINGTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								poEsthdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								poEsthdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								poEsthdr.setORDERDISCOUNTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								poEsthdr.setCURRENCYUSEQT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									poEsthdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									poEsthdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
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
							
							/* PODET */
							
							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								POESTLNNO.add(StrUtils.fString(fileItem.getString()).trim());
								POESTLNNOcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTY")) {
								qtyor.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("unitprice")) {
								unitcost.add(StrUtils.fString(fileItem.getString()).trim());
//								unitcostcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("UOM")) {
								unitmo.add(StrUtils.fString(fileItem.getString()).trim());
//								unitmocount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PRODUCTDELIVERYDATE")) {
								pddate.add(StrUtils.fString(fileItem.getString()).trim());
//								pddatecount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_discount")) {
								discount.add(StrUtils.fString(fileItem.getString()).trim());
//								discountcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_discounttype")) {
								dtype.add(StrUtils.fString(fileItem.getString()).trim());
								//dtypecount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accname.add(StrUtils.fString(fileItem.getString()).trim());
//								accnamecount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("tax_type")) {
								taxtype.add(StrUtils.fString(fileItem.getString()).trim());
//								taxtypecount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("chkdPOESTNO")) {
								chkdPOESTNO.add(StrUtils.fString(fileItem.getString()).trim());
								chkdPOESTNOCount++;
							}
							
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							
							//attachment

							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/"+ poEsthdr.getPOESTNO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"+ poEsthdr.getPOESTNO();
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
								Hashtable purchaseEstimateAttachment = new Hashtable<String, String>();
								purchaseEstimateAttachment.put("PLANT", plant);
								purchaseEstimateAttachment.put("FILETYPE", fileItem.getContentType());
								purchaseEstimateAttachment.put("FILENAME", fileName);
								purchaseEstimateAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								purchaseEstimateAttachment.put("FILEPATH", fileLocationATT);
								purchaseEstimateAttachment.put("CRAT",DateUtils.getDateTime());
								purchaseEstimateAttachment.put("CRBY",username);
								purchaseEstimateAttachment.put("UPAT",DateUtils.getDateTime());
								purchaseEstimateAttachmentList.add(purchaseEstimateAttachment);
							}
						
						}
					}
					
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
					poEsthdr.setEMPNO(empno);
					if(poEsthdr.getDELIVERYDATEFORMAT() == null){
						poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					
					if(!poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
						poEsthdr.setORDERDISCOUNT((poEsthdr.getORDERDISCOUNT()/poEsthdr.getCURRENCYUSEQT()));
					}
					poEsthdr.setSHIPPINGCOST((poEsthdr.getSHIPPINGCOST()/poEsthdr.getCURRENCYUSEQT()));
					poEsthdr.setADJUSTMENT((poEsthdr.getADJUSTMENT()/poEsthdr.getCURRENCYUSEQT()));
					
					poEsthdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					poEsthdr.setGOODSIMPORT(Short.valueOf(goodsimport));
					poEsthdr.setSTATUS("N");
					poEsthdr.setLOCALEXPENSES(0.0);
					poEsthdr.setCRBY(username);
					poEsthdr.setCRAT(DateUtils.getDateTime());
					poEsthdr.setSHIPCONTACTNAME(shipcontactname);
					poEsthdr.setSHIPDESGINATION(shipdesgination);
					poEsthdr.setSHIPWORKPHONE(shipworkphone);
					poEsthdr.setSHIPHPNO(shiphpno);
					poEsthdr.setSHIPEMAIL(shipemail);
					poEsthdr.setSHIPCOUNTRY(shipcountry);
					poEsthdr.setSHIPADDR1(shipaddr1);
					poEsthdr.setSHIPADDR2(shipaddr2);
					poEsthdr.setSHIPADDR3(shipaddr3);
					poEsthdr.setSHIPADDR4(shipaddr4);
					poEsthdr.setSHIPSTATE(shipstate);
					poEsthdr.setSHIPZIP(shipzip);					
					if (!poEsthdr.getJobNum().contains("PM"))
						poEsthdr.setPOMULTIESTNO("");
					else
						poEsthdr.setPOMULTIESTNO(poEsthdr.getJobNum());
					boolean issadded = poEstHdrDAO.addPoHdr(poEsthdr);
					
					
					if(issadded) {
						List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
						
						double totalDiscountAmount = 0;
						for(int i=0;i<POESTLNNOcount;i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String)POESTLNNO.get(i)) -1;
//							int lnno = i+1;
							String disctype=(String)dtype.get(index);
								if(disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String)unitcost.get(index))/poEsthdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									double amount = (Double.valueOf((String)unitcost.get(index))/poEsthdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/poEsthdr.getCURRENCYUSEQT();
								}	
								totalDiscountAmount+=unitCost_AD;
						}
						
						if(action.equalsIgnoreCase("copy")) {
							for(int i=0;i<chkdPOESTNOCount;i++) {
								int index = Integer.parseInt((String)chkdPOESTNO.get(i));
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i+1;
								String disctype=(String)dtype.get(index);
								PoEstDet poEstDet = new PoEstDet();
								poEstDet.setPLANT(plant);
								poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
								poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
								poEstDet.setPOESTLNNO(lnno);
								poEstDet.setITEM((String)item.get(index));
								poEstDet.setUNITCOST((Double.valueOf((String)unitcost.get(index))/poEsthdr.getCURRENCYUSEQT()));
								poEstDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								poEstDet.setUNITMO((String)unitmo.get(index));
								poEstDet.setPRODUCTDELIVERYDATE((String)pddate.get(index));
								
								if(disctype.equalsIgnoreCase("%")) {
									poEstDet.setDISCOUNT(Double.valueOf((String)discount.get(index)));
									double amount = (Double.valueOf((String)unitcost.get(index))/poEsthdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									poEstDet.setDISCOUNT((Double.valueOf((String)discount.get(index))/poEsthdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String)unitcost.get(index))/poEsthdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/poEsthdr.getCURRENCYUSEQT();
								}
								
								if(poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((poEsthdr.getORDERDISCOUNT()/(unitCost_AD*(poEsthdr.getORDERDISCOUNT()/100))/(totalDiscountAmount))*unitCost_AD);
								}else {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - (((poEsthdr.getORDERDISCOUNT()/poEsthdr.getCURRENCYUSEQT())/(totalDiscountAmount))*unitCost_AD);
								}
								if(Double.isNaN(unitCost_AOD)) {
									poEstDet.setUNITCOST_AOD(0.0);
								}else {
									poEstDet.setUNITCOST_AOD(unitCost_AOD);
								}
								poEstDet.setDISCOUNT_TYPE((String)dtype.get(index));
								poEstDet.setACCOUNT_NAME((String)accname.get(index));
								poEstDet.setTAX_TYPE((String)taxtype.get(index));
								poEstDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poEstDet.setUSERFLD2(poEsthdr.getJobNum());
								poEstDet.setUSERFLD3(poEsthdr.getCustName());
								poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
								poEstDet.setPRODGST(0);
								poEstDet.setCOMMENT1("");
								poEstDet.setLNSTAT("N");
								poEstDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poEstDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poEstDet.setCRBY(username);
								poEstDet.setCRAT(DateUtils.getDateTime());
								if (!poEsthdr.getJobNum().contains("PM")) {
									poEstDet.setPOMULTIESTNO("");
									poEstDet.setPOMULTIESTLNNO(lnno);
								} else {
									poEstDet.setPOMULTIESTNO(poEsthdr.getJobNum());
									poEstDet.setPOMULTIESTLNNO(0);
								}								
								poEstDetDAO.addPoDet(poEstDet);
								podetlist.add(poEstDet);
							}
						} else {
							for (int i = 0; i < POESTLNNOcount; i++) {
								int index = Integer.parseInt((String) POESTLNNO.get(i)) - 1;
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i + 1;
								String disctype = (String) dtype.get(index);
								PoEstDet poEstDet = new PoEstDet();
								poEstDet.setPLANT(plant);
								poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
								poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
								poEstDet.setPOESTLNNO(lnno);
								poEstDet.setITEM((String) item.get(index));
								poEstDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / poEsthdr.getCURRENCYUSEQT()));
								poEstDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poEstDet.setUNITMO((String) unitmo.get(index));
								poEstDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));

								if (disctype.equalsIgnoreCase("%")) {
									poEstDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poEstDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT();
								}

								if (poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((poEsthdr.getORDERDISCOUNT()
											/ (unitCost_AD * (poEsthdr.getORDERDISCOUNT() / 100)) / (totalDiscountAmount))
											* unitCost_AD);
								} else {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD)
											- (((poEsthdr.getORDERDISCOUNT() / poEsthdr.getCURRENCYUSEQT())
													/ (totalDiscountAmount)) * unitCost_AD);
								}
								
								if(Double.isNaN(unitCost_AOD)) {
									poEstDet.setUNITCOST_AOD(0.0);
								}else {
									poEstDet.setUNITCOST_AOD(unitCost_AOD);
								}
								poEstDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poEstDet.setACCOUNT_NAME((String) accname.get(index));
								poEstDet.setTAX_TYPE((String) taxtype.get(index));
								poEstDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setUSERFLD2(poEsthdr.getJobNum());
								poEstDet.setUSERFLD3(poEsthdr.getCustName());
								poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
								poEstDet.setPRODGST(0);
								poEstDet.setCOMMENT1("");
								poEstDet.setLNSTAT("N");
								poEstDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poEstDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setCRBY(username);
								poEstDet.setCRAT(DateUtils.getDateTime());
								if (!poEsthdr.getJobNum().contains("PM")) {
									poEstDet.setPOMULTIESTNO("");
									poEstDet.setPOMULTIESTLNNO(lnno);
								} else {
									poEstDet.setPOMULTIESTNO(poEsthdr.getJobNum());
									poEstDet.setPOMULTIESTLNNO(0);
								}
								poEstDetDAO.addPoDet(poEstDet);
								podetlist.add(poEstDet);
							}
						}
						int attchSize = purchaseEstimateAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable purchaseEstimateAttachmentat = new Hashtable<String, String>();
								purchaseEstimateAttachmentat = purchaseEstimateAttachmentList.get(i);
								purchaseEstimateAttachmentat.put("POESTNO", poEsthdr.getPOESTNO());
								purchaseEstimateAttachmentInfoList.add(purchaseEstimateAttachmentat);
							}
							purchaseEstimateAttachDAO.addpurchaseAttachments(purchaseEstimateAttachmentInfoList, plant);
						}


//						new TblControlUtil().updateTblControlSeqNo(plant, IConstants.POEST, "PE", poEsthdr.getPOESTNO());
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.POEST,"PE", poEsthdr.getPOESTNO());

						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PE);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getRemark1());
						if (!poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									poEsthdr.getCustName() + "," + poEsthdr.getRemark1() + "," + poEsthdr.getREMARK3());
						} else if (!poEsthdr.getRemark1().equals("") && poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getRemark1());
						} else if (poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName());
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (poEsthdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, poEsthdr.getRemark1());
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (poEsthdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, poEsthdr.getREMARK3());
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (poEsthdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, poEsthdr.getINCOTERMS());

								if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertINCOTERMS(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (int i = 0; i < chkdPOESTNOCount; i++) {
								int index = Integer.parseInt((String) chkdPOESTNO.get(i));

								int Polno = i + 1;
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.PODET_POESTNUM, poEsthdr.getPOESTNO());
								htRemarks.put(IDBConstants.PODET_POESTLNNO, Integer.toString(Polno));
								htRemarks.put(IDBConstants.PODET_ITEM, (String) item.get(index));
								if (!new PoEstDetDAO().isExisitPoMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									flag = new PoEstUtil().savePoMultiRemarks(htRemarks);
								}
							}

							for (PoEstDet porderdet : podetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", TransactionConstants.PE_ADD_ITEM);
								htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
								htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.QTY, porderdet.getQTYOR());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}

						DbBean.CommitTran(ut);
						String message = "Purchse Estimate Added Successfully.";
						if (ajax) {
							/*
							 * request.setAttribute("PONO", poEsthdr.getPONO()); EmailMsgUtil emailMsgUtil =
							 * new EmailMsgUtil(); Map<String, String> emailMsg =
							 * emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER); String
							 * sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT); if
							 * ("both".equals(sendAttachment) || "receiving_list".equals(sendAttachment)) {
							 * viewPOReport(request, response, "printPO"); } if
							 * ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
							 * viewPOReport(request, response, "printPOInvoice"); }
							 * resultJson.put("MESSAGE", message); resultJson.put("ERROR_CODE", "100");
							 * response.setContentType("application/json");
							 * response.setCharacterEncoding("UTF-8");
							 * response.getWriter().write(resultJson.toString());
							 * response.getWriter().flush(); response.getWriter().close();
							 */
						}else {
							response.sendRedirect("../purchaseorderestimate/summary?msg=" + message);
						}
					} else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Purchse Estimate.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseorderestimate/summary?msg=" + message);
						}
					}
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (ajax) {
					resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}else {
					response.sendRedirect("../purchaseorderestimate/summary?msg=" + ThrowableUtil.getMessage(e));
				}
			}

		}

		if (action.equalsIgnoreCase("edit")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			List<Hashtable<String, String>> purchaseEstimateAttachmentList = null;
			List<Hashtable<String, String>> purchaseEstimateAttachmentInfoList = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);

                String empno="",empName="";
				List POESTLNNO = new ArrayList(), prvlnno = new ArrayList(), item = new ArrayList(),
						unitcost = new ArrayList(), qtyor = new ArrayList(), unitmo = new ArrayList(),
						pddate = new ArrayList(), discount = new ArrayList(), dtype = new ArrayList(),
						accname = new ArrayList(), recqty = new ArrayList(), ponoline = new ArrayList(),
						taxtype = new ArrayList();
				 String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
	                		shipworkphone="",shipcountry="",shiphpno="",shipemail="";
				int POESTLNNOcount = 0, draftstatus = 0;//unitmocount = 0, unitcostcount = 0, taxtypecount = 0, recqtycount = 0, qtyorcount = 0, pddatecount = 0, itemcount = 0, dtypecount = 0, discountcount = 0, accnamecount = 0, prvlnnocount = 0, ponolinecount = 0, 

				purchaseEstimateAttachmentList = new ArrayList<Hashtable<String, String>>();
				purchaseEstimateAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					String revgersecharge = "0";
					String goodsimport = "0";
					ut = DbBean.getUserTranaction();
					ut.begin();
					PoEstHdr poEsthdr = new PoEstHdr();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* POHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								poEsthdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("POESTNO")) {
								poEsthdr.setPOESTNO(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								poEsthdr.setCustName(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								poEsthdr.setCustCode(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								poEsthdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PERSON_INCHARGE")) {
								poEsthdr.setPersonInCharge(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("TELNO")) {
								poEsthdr.setContactNum(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD1")) {
								poEsthdr.setAddress(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD2")) {
								poEsthdr.setAddress2(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD3")) {
								poEsthdr.setAddress3(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								poEsthdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								poEsthdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								poEsthdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								poEsthdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								poEsthdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								poEsthdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								poEsthdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								poEsthdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								poEsthdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								poEsthdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								poEsthdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								poEsthdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								poEsthdr.setEXPIREDATE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								poEsthdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								poEsthdr.setORDERDISCOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								poEsthdr.setSHIPPINGCOST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								poEsthdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								poEsthdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							 if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
									
									empno = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
									empName = StrUtils.fString(fileItem.getString()).trim();
								}
							
							

							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								poEsthdr.setDELIVERYDATEFORMAT(Short
										.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								poEsthdr.setTAXTREATMENT(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								poEsthdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								poEsthdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								poEsthdr.setADJUSTMENT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("draftstatus")) {
								draftstatus = Integer.valueOf(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
								poEsthdr.setISDISCOUNTTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
								poEsthdr.setISSHIPPINGTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								poEsthdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								poEsthdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								poEsthdr.setORDERDISCOUNTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								poEsthdr.setCURRENCYUSEQT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									poEsthdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									poEsthdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
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
							/* PODET */

							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								POESTLNNO.add(StrUtils.fString(fileItem.getString()).trim());
								POESTLNNOcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("prlnno")) {
								prvlnno.add(StrUtils.fString(fileItem.getString()).trim());
//								prvlnnocount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("QTY")) {
								qtyor.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("unitprice")) {
								unitcost.add(StrUtils.fString(fileItem.getString()).trim());
//								unitcostcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("UOM")) {
								unitmo.add(StrUtils.fString(fileItem.getString()).trim());
//								unitmocount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PRODUCTDELIVERYDATE")) {
								pddate.add(StrUtils.fString(fileItem.getString()).trim());
//								pddatecount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_discount")) {
								discount.add(StrUtils.fString(fileItem.getString()).trim());
//								discountcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_discounttype")) {
								dtype.add(StrUtils.fString(fileItem.getString()).trim());
//								dtypecount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accname.add(StrUtils.fString(fileItem.getString()).trim());
//								accnamecount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("tax_type")) {
								taxtype.add(StrUtils.fString(fileItem.getString()).trim());
//								taxtypecount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("RecQty")) {
								recqty.add(StrUtils.fString(fileItem.getString()).trim());
//								recqtycount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("POESTLNNO")) {
								ponoline.add(StrUtils.fString(fileItem.getString()).trim());
//								ponolinecount++;
							}

						} else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {

							// attachment

							if (fileItem.getFieldName().equalsIgnoreCase("file")) {
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/" + poEsthdr.getPOESTNO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"
										+ poEsthdr.getPOESTNO();
								String fileName = StrUtils.fString(fileItem.getName()).trim();
								fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

								File path = new File(fileLocationATT);
								if (!path.exists()) {
									path.mkdirs();
								}

								// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
								File uploadedFile = new File(path + "/" + fileName);
								if (uploadedFile.exists()) {
									uploadedFile.delete();
								}

								fileItem.write(uploadedFile);

								// delete temp uploaded file
								File tempPath = new File(filetempLocationATT);
								if (tempPath.exists()) {
									File tempUploadedfile = new File(tempPath + "/" + fileName);
									if (tempUploadedfile.exists()) {
										tempUploadedfile.delete();
									}
								}
								Hashtable purchaseEstimateAttachment = new Hashtable<String, String>();
								purchaseEstimateAttachment.put("PLANT", plant);
								purchaseEstimateAttachment.put("FILETYPE", fileItem.getContentType());
								purchaseEstimateAttachment.put("FILENAME", fileName);
								purchaseEstimateAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								purchaseEstimateAttachment.put("FILEPATH", fileLocationATT);
								purchaseEstimateAttachment.put("CRAT", DateUtils.getDateTime());
								purchaseEstimateAttachment.put("CRBY", username);
								purchaseEstimateAttachment.put("UPAT", DateUtils.getDateTime());
								purchaseEstimateAttachmentList.add(purchaseEstimateAttachment);
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
					poEsthdr.setEMPNO(empno);
					if (poEsthdr.getDELIVERYDATEFORMAT() == null) {
						poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					if (!poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
						poEsthdr.setORDERDISCOUNT((poEsthdr.getORDERDISCOUNT() / poEsthdr.getCURRENCYUSEQT()));
					}
					
				
					poEsthdr.setSHIPPINGCOST((poEsthdr.getSHIPPINGCOST() / poEsthdr.getCURRENCYUSEQT()));
					poEsthdr.setADJUSTMENT((poEsthdr.getADJUSTMENT() / poEsthdr.getCURRENCYUSEQT()));
					poEsthdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					poEsthdr.setGOODSIMPORT(Short.valueOf(goodsimport));

					/* boolean issadded = poHdrDAO.addPoHdr(poEsthdr); */
					PoEstHdr pohdrbypono = poEstHdrDAO.getPoHdrByPono(plant, poEsthdr.getPOESTNO());
					if (draftstatus == 1) {
						poEsthdr.setORDER_STATUS("Open");
					}else {
						poEsthdr.setORDER_STATUS("Draft");
					}
					poEsthdr.setSTATUS(pohdrbypono.getSTATUS());
					poEsthdr.setCRBY(pohdrbypono.getCRBY());
					poEsthdr.setCRAT(pohdrbypono.getCRAT());
					poEsthdr.setLOCALEXPENSES(pohdrbypono.getLOCALEXPENSES());
					poEsthdr.setSHIPCONTACTNAME(shipcontactname);
					poEsthdr.setSHIPDESGINATION(shipdesgination);
					poEsthdr.setSHIPWORKPHONE(shipworkphone);
					poEsthdr.setSHIPHPNO(shiphpno);
					poEsthdr.setSHIPEMAIL(shipemail);
					poEsthdr.setSHIPCOUNTRY(shipcountry);
					poEsthdr.setSHIPADDR1(shipaddr1);
					poEsthdr.setSHIPADDR2(shipaddr2);
					poEsthdr.setSHIPADDR3(shipaddr3);
					poEsthdr.setSHIPADDR4(shipaddr4);
					poEsthdr.setSHIPSTATE(shipstate);
					poEsthdr.setSHIPZIP(shipzip);
					boolean issadded = poEstHdrDAO.updatePoHdr(poEsthdr);

					List<PoEstDet> podetail = poEstDetDAO.getPoDetByPono(plant, poEsthdr.getPOESTNO());
					for (PoEstDet poEstDet : podetail) {
						boolean Detstatus = true;
						for (int i = 0; i < POESTLNNOcount; i++) {
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							if (poEstDet.getPOESTLNNO() == ponumline) {
								Detstatus = false;
							}
						}
						if (Detstatus) {
							poEstDetDAO.DeletePoDet(plant, poEsthdr.getPOESTNO(), poEstDet.getPOESTLNNO());
						}
					}

					if (issadded) {
						List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
						double totalDiscountAmount = 0;
						for (int i = 0; i < POESTLNNOcount; i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String) POESTLNNO.get(i)) - 1;
//							int lnno = i + 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
//								PoDet poEstDet = poDetDAO.getPoDetByPonoPllno(plant, pohdrbypono.getPONO(), ponumline);
//								int lineno = poEstDet.getPOLNNO();
								if (disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT();
								}
								totalDiscountAmount += unitCost_AD;
							}
						}

						for (int i = 0; i < POESTLNNOcount; i++) {
							int index = Integer.parseInt((String) POESTLNNO.get(i)) - 1;
							int lnno = i + 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
								PoEstDet poEstDet = poEstDetDAO.getPoDetByPonoPllno(plant, pohdrbypono.getPOESTNO(), ponumline);
								int lineno = poEstDet.getPOESTLNNO();
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								if(poEstDet.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyor.get(index)))//Status update Azees
								{
									if(poEstDet.getQTYRC().doubleValue()>0)
										poEstDet.setLNSTAT("O");
									else
										poEstDet.setLNSTAT("N");
								}
								poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
								poEstDet.setITEM((String) item.get(index));
								poEstDet.setPOESTLNNO(lnno);
								poEstDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / poEsthdr.getCURRENCYUSEQT()));
								poEstDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poEstDet.setUNITMO((String) unitmo.get(index));
								poEstDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));
								if (disctype.equalsIgnoreCase("%")) {
									poEstDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poEstDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT();
								}

								if (poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((poEsthdr.getORDERDISCOUNT()
											/ (unitCost_AD * (poEsthdr.getORDERDISCOUNT() / 100)) / (totalDiscountAmount))
											* unitCost_AD);
								} else {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD)
											- (((poEsthdr.getORDERDISCOUNT() / poEsthdr.getCURRENCYUSEQT())
													/ (totalDiscountAmount)) * unitCost_AD);
								}
								if(Double.isNaN(unitCost_AOD)) {
									poEstDet.setUNITCOST_AOD(0.0);
								}else {
									poEstDet.setUNITCOST_AOD(unitCost_AOD);	
								}	
								poEstDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poEstDet.setACCOUNT_NAME((String) accname.get(index));
								poEstDet.setTAX_TYPE((String) taxtype.get(index));
								poEstDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setUSERFLD2(poEsthdr.getJobNum());
								poEstDet.setUSERFLD3(poEsthdr.getCustName());
								poEstDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setUPAT(DateUtils.getDateTime());
								poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
								poEstDet.setUPBY(username);								
								poEstDetDAO.updatePoDetpoEdit(poEstDet,lineno);
								podetlist.add(poEstDet);
							} else {
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								PoEstDet poEstDet = new PoEstDet();
								poEstDet.setPLANT(plant);
								poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
								poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
								poEstDet.setPOESTLNNO(lnno);
								poEstDet.setITEM((String) item.get(index));
								poEstDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / poEsthdr.getCURRENCYUSEQT()));
								poEstDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poEstDet.setUNITMO((String) unitmo.get(index));
								poEstDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));

								if (disctype.equalsIgnoreCase("%")) {
									poEstDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poEstDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ poEsthdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / poEsthdr.getCURRENCYUSEQT();
								}

								if (poEsthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AOD = (unitCost_AD) - (unitCost_AD * (poEsthdr.getORDERDISCOUNT() / 100));
								} else {
									unitCost_AOD = (unitCost_AD)
											- ((poEsthdr.getORDERDISCOUNT() / poEsthdr.getCURRENCYUSEQT()));
								}
								if(Double.isNaN(unitCost_AOD)) {
									poEstDet.setUNITCOST_AOD(0.0);
								}else {
									poEstDet.setUNITCOST_AOD(unitCost_AOD);	
								}								
								poEstDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poEstDet.setACCOUNT_NAME((String) accname.get(index));
								poEstDet.setTAX_TYPE((String) taxtype.get(index));
								poEstDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setUSERFLD2(poEsthdr.getJobNum());
								poEstDet.setUSERFLD3(poEsthdr.getCustName());
								poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
								poEstDet.setPRODGST(0);
								poEstDet.setCOMMENT1("");
								poEstDet.setLNSTAT("N");
								poEstDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poEstDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poEstDet.setCRBY(username);
								poEstDet.setCRAT(DateUtils.getDateTime());
								if (!poEsthdr.getJobNum().contains("PM")) {
									poEstDet.setPOMULTIESTNO("");
									poEstDet.setPOMULTIESTLNNO(lnno);
								} else {
									poEstDet.setPOMULTIESTNO(poEsthdr.getJobNum());
									poEstDet.setPOMULTIESTLNNO(0);
								}
								poEstDetDAO.addPoDet(poEstDet);
								podetlist.add(poEstDet);
							}
						}

						int attchSize = purchaseEstimateAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable purchaseEstimateAttachmentat = new Hashtable<String, String>();
								purchaseEstimateAttachmentat = purchaseEstimateAttachmentList.get(i);
								purchaseEstimateAttachmentat.put("POESTNO", poEsthdr.getPOESTNO());
								purchaseEstimateAttachmentInfoList.add(purchaseEstimateAttachmentat);
							}
							purchaseEstimateAttachDAO.addpurchaseAttachments(purchaseEstimateAttachmentInfoList, plant);
						}

						List<PoEstDetRemarks> podetrmk = poEstDetDAO.getPoDetRemarksByPono(plant, poEsthdr.getPOESTNO());
						List<PoEstDetRemarks> podetrmkupdate = new ArrayList<PoEstDetRemarks>();
						for (PoEstDetRemarks poEstDetRemarks : podetrmk) {
							boolean rmstatus = false;
							int newlnno = 0;
							for (int i = 0; i < POESTLNNOcount; i++) {
								int prvlineno = Integer.parseInt((String) prvlnno.get(i));
								if (poEstDetRemarks.getPOESTLNNO() == prvlineno) {
									rmstatus = true;
									newlnno = Integer.parseInt((String) POESTLNNO.get(i));
								}
							}

							if (rmstatus) {
								poEstDetRemarks.setPOESTLNNO(newlnno);
								podetrmkupdate.add(poEstDetRemarks);
							} else {
								poEstDetDAO.DeletePoDetRemarks(plant, poEstDetRemarks.getID_REMARKS());
							}

						}

						if (podetrmkupdate.size() > 0) {
							poEstDetDAO.updatePoDetRemarksBYID(podetrmkupdate);
						}

						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.UPDATE_IB);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getRemark1());
						if (!poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									poEsthdr.getCustName() + "," + poEsthdr.getRemark1() + "," + poEsthdr.getREMARK3());
						} else if (!poEsthdr.getRemark1().equals("") && poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getRemark1());
						} else if (poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() + "," + poEsthdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName());
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (poEsthdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, poEsthdr.getRemark1());
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (poEsthdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, poEsthdr.getREMARK3());
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (poEsthdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, poEsthdr.getINCOTERMS());

								if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, username);
									_MasterDAO.InsertINCOTERMS(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", poEsthdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (PoEstDet porderdet : podetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "PURCHASE_ESTIMATE_ORDER_UPDATE_PRODUCT");
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
								htRecvHis.put(IDBConstants.MOVHIS_ORDLNO, porderdet.getPOESTLNNO());
								htRecvHis.put("QTY", porderdet.getQTYOR());
								htRecvHis.put("REMARKS", porderdet.getUNITCOST());
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						
						//ORDER_STATUS Update - Azees 3.21
						if(draftstatus != 1) {
						if(flag) {
							Hashtable htCondition = new Hashtable();
							htCondition.put("PLANT", plant);
							htCondition.put("POESTNO", poEsthdr.getPOESTNO());					
							flag = new PoEstDetDAO().isExisit(htCondition,"LNSTAT in ('O','N')");
							if (!flag){
								String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
								flag = new PoEstHdrDAO().updatePO(updateHdr, htCondition, "");
							} else {
								flag = new PoEstDetDAO().isExisit(htCondition,"LNSTAT in ('O')");
								if (!flag){
									flag = new PoEstDetDAO().isExisit(htCondition,"LNSTAT in ('C','N')");
									if (!flag){
										String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
										flag = new PoEstHdrDAO().updatePO(updateHdr, htCondition, "");
									} else {
										flag = new PoEstDetDAO().isExisit(htCondition,"LNSTAT in ('C')");
										if (!flag){
											String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
											flag = new PoEstHdrDAO().updatePO(updateHdr, htCondition, "");									 
										} else {
										String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
										flag = new PoEstHdrDAO().updatePO(updateHdr, htCondition, "");										
										}
									}
								} else {
									String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
									flag = new PoEstHdrDAO().updatePO(updateHdr, htCondition, "");	
								}						
								}
						}
							}
						
						 DbBean.CommitTran(ut);
						 String message = "Purchase Estimate Updated Successfully.";
						if (ajax) {
							/*
							 * request.setAttribute("PONO", poEsthdr.getPONO()); EmailMsgUtil emailMsgUtil =
							 * new EmailMsgUtil(); Map<String, String> emailMsg =
							 * emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER); String
							 * sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT); if
							 * ("both".equals(sendAttachment) || "receiving_list".equals(sendAttachment)) {
							 * viewPOReport(request, response, "printPO"); } if
							 * ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
							 * viewPOReport(request, response, "printPOInvoice"); }
							 * resultJson.put("MESSAGE", message); resultJson.put("ERROR_CODE", "100");
							 * response.setContentType("application/json");
							 * response.setCharacterEncoding("UTF-8");
							 * response.getWriter().write(resultJson.toString());
							 * response.getWriter().flush(); response.getWriter().close();
							 */
						}else {
							response.sendRedirect("../purchaseorderestimate/summary?msg=" + message);
						}
					}else {
						 DbBean.RollbackTran(ut);
						 String message = "Unable To Update Purchase Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseorderestimate/summary?msg=" + message);
						}
					}
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../purchaseorderestimate/summary?msg=" + ThrowableUtil.getMessage(e));
			}

		}

		if (action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String POESTNO = request.getParameter("r_POESTNO");
				String polno = request.getParameter("r_lnno");
				boolean insertFlag = false;
				boolean isexist = poEstDetDAO.IsExistPoDetRemarks(plant, POESTNO, polno, item);
				if (isexist) {
					List<PoEstDetRemarks> poEstDetRemarkslist = poEstDetDAO.GetPoDetRemarksbyitems(plant, POESTNO, polno, item);
					for (PoEstDetRemarks poEstDetRemarks : poEstDetRemarkslist) {
						poEstDetDAO.DeletePoDetRemarks(plant, poEstDetRemarks.getID_REMARKS());
					}
				}

				for (int i = 0; i < remarks.length; i++) {
					PoEstDetRemarks poEstDetRemarks = new PoEstDetRemarks();
					poEstDetRemarks.setREMARKS(remarks[i]);
					poEstDetRemarks.setITEM(item);
					poEstDetRemarks.setPOESTNO(POESTNO);
					poEstDetRemarks.setPOESTLNNO(Integer.valueOf(polno));
					poEstDetRemarks.setCRBY(username);
					poEstDetRemarks.setPLANT(plant);
					poEstDetRemarks.setCRAT(DateUtils.getDateTime());
					insertFlag = poEstDetDAO.addPoDetRemarks(poEstDetRemarks);

					if (insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
						htRecvHis.put("ORDNUM", POESTNO);
						htRecvHis.put(IDBConstants.ITEM, "");
						htRecvHis.put("BATNO", "");
						htRecvHis.put(IDBConstants.LOC, "");
						htRecvHis.put("REMARKS", remarks[i]);
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						movHisDao.insertIntoMovHis(htRecvHis);
					}
				}

				if (insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Purchase Estimate Remarks Created Successfully.");
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

		if (action.equalsIgnoreCase("Delete")) {
			JSONObject resultJson = new JSONObject();
			PoEstUtil _PoEstUtil = new PoEstUtil();

			String User_id = username;
			String POESTNO = StrUtils.fString(request.getParameter("POESTNO")).trim();
			try {
				Hashtable htPoHrd = new Hashtable();
				htPoHrd.put(IDBConstants.PLANT, plant);
				htPoHrd.put(IDBConstants.PODET_POESTNUM, POESTNO);
//				sqlBean sqlbn = new sqlBean();
				boolean isValidOrder = new PoEstHdrDAO().isExisit(htPoHrd, "");
				boolean isOrderInProgress = new PoEstDetDAO().isExisit(htPoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {
						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, POESTNO);
						if (!isExistsPaymentDetails) {
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("POESTNO", POESTNO);
//							String query = "pono,custCode";
//							ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = _PoEstUtil.removeRow(plant, POESTNO, User_id);
							if (value) {
								resultJson.put("MESSAGE", "Purchase Estimate Deleted Successfully.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								resultJson.put("MESSAGE", "Purchase Estimate Not Deleted.");
								resultJson.put("ERROR_CODE", "97");
							}
						} else {
							resultJson.put("MESSAGE", "Purchase Estimate Not Deleted.");
							resultJson.put("ERROR_CODE", "96");
						}
					} else {
						resultJson.put("MESSAGE", "Purchase Estimate Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Purchase Estimate Not Deleted.");
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
			jsonObjectResult = getEditPODetails(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

		if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			String ID = request.getParameter("attachid");
			FileHandling fileHandling = new FileHandling();
			List purchaseEstimateAttachment = null;
			try {

				purchaseEstimateAttachment = purchaseEstimateAttachDAO.getpurchaseAttachById(plant, ID);
				Map poAttach = (Map) purchaseEstimateAttachment.get(0);
				String filePath = (String) poAttach.get("FilePath");
				String fileType = (String) poAttach.get("FileType");
				String fileName = (String) poAttach.get("FileName");
				fileHandling.fileDownload(filePath, fileName, fileType, response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (action.equalsIgnoreCase("removeAttachmentById")) {
			System.out.println("Remove Attachments by ID");
			String ID = request.getParameter("removeid");
			try {
				purchaseEstimateAttachDAO.deletepurchaseAttachByPrimId(plant, ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}
		
		
		
		
		
		

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		System.out.println("action............." + action);
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		PoEstHdrDAO poEstHdrDAO = new PoEstHdrDAO();
		PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			request.getSession().invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}

		if (action.equalsIgnoreCase("CheckOrderno")) {
			JSONObject jsonObjectResult = new JSONObject();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("POESTNO", orderno);
				if (new PoEstHdrDAO().isExisit(ht)) {
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
		
		if (action.equalsIgnoreCase("Auto-Generate")) {
			String POESTNO = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				POESTNO = _TblControlDAO.getNextOrder(plant, username, IConstants.POEST);
				json.put("POESTNO", POESTNO);
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
		}

		
		
		
		if (action.equalsIgnoreCase("new")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreatePurchaseEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		

		if (action.equalsIgnoreCase("edit")) {
			try {
				String POESTNO = StrUtils.fString(request.getParameter("POESTNO"));
				request.setAttribute("POESTNO", POESTNO);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PurchaseEstimateEdit.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	
	
		

		if (action.equalsIgnoreCase("copy")) {
			try {
				String POESTNO = StrUtils.fString(request.getParameter("POESTNO"));
				request.setAttribute("POESTNO", POESTNO);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/CopyPurchaseEstimate.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		if (action.equalsIgnoreCase("convertfromPEtoPO")) {
			try {
				String POESTNO = StrUtils.fString(request.getParameter("POESTNO"));
				request.setAttribute("POESTNO", POESTNO);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ConvertFromEstimateToPurchase.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		if (action.equalsIgnoreCase("detail")) {
			try {
				String POESTNO = StrUtils.fString(request.getParameter("POESTNO"));
				request.setAttribute("POESTNO", POESTNO);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PurchaseEstimateDetail.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("summary")) {
			boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if (ajax) {
				String FROM_DATE = StrUtils.fString(request.getParameter("FDATE"));
				String TO_DATE = StrUtils.fString(request.getParameter("TDATE"));
				String cname = StrUtils.fString(request.getParameter("CNAME"));
				String orderno = StrUtils.fString(request.getParameter("ORDERNO"));
				String reference = StrUtils.fString(request.getParameter("REFERENCE"));
				String orderType = StrUtils.fString(request.getParameter("ORDERTYPE"));
				String status = StrUtils.fString(request.getParameter("STATUS"));
				String fdate = "", tdate = "";
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				
				
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("POESTNO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					 if(status.equalsIgnoreCase("PARTIALLY PROCESSED")) {
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(STATUS, '')", "O");	
					}
					
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
					List<PoEstHdr> poHeaders = poEstHdrDAO.getPoHdrSummary(ht, fdate, tdate);
					if (poHeaders.size() > 0) {

						for (PoEstHdr poEstHdr : poHeaders) {
							JSONObject json = new JSONObject();
							List<PoEstDet> podetail = poEstDetDAO.getPoDetByPono(plant, poEstHdr.getPOESTNO());
							FinCountryTaxType fintaxtype = finCountryTaxTypeDAO
									.getCountryTaxTypesByid(poEstHdr.getTAXID());
							double totalAmount = 0;
							double subtotal = 0;
							for (PoEstDet poEstDet : podetail) {
								if (poEstDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = poEstDet.getUNITCOST() * poEstDet.getQTYOR().doubleValue();
									double discount = (ucost / 100) * poEstDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								} else {
									double ucost = poEstDet.getUNITCOST() * poEstDet.getQTYOR().doubleValue();
									double discount = poEstDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								}
							}

							if (poEstHdr.getISTAXINCLUSIVE() == 1) {
								subtotal = (subtotal * 100) / (poEstHdr.getINBOUND_GST() + 100);
							}

							double dorderdiscountcost = 0;
							if (poEstHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
								dorderdiscountcost = (subtotal / 100) * poEstHdr.getORDERDISCOUNT();
							} else {
								dorderdiscountcost = poEstHdr.getORDERDISCOUNT();
							}
							double totax = 0;
							if (poEstHdr.getTAXID() != 0) {
								if (fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1) {

									totax = (subtotal / 100) * poEstHdr.getINBOUND_GST();

									if (poEstHdr.getISSHIPPINGTAX() == 1) {
										totax = totax + ((poEstHdr.getSHIPPINGCOST() / 100) * poEstHdr.getINBOUND_GST());
									}

									if (poEstHdr.getISDISCOUNTTAX() == 1) {
										totax = totax - ((dorderdiscountcost / 100) * poEstHdr.getINBOUND_GST());
									}
								}
							}

							totalAmount = (subtotal + poEstHdr.getSHIPPINGCOST() + totax + poEstHdr.getADJUSTMENT())
									- dorderdiscountcost;

							// totalAmount = totalAmount + poEstHdr.getSHIPPINGCOST() + poEstHdr.getADJUSTMENT() -
							// ((subtotal/100)*poEstHdr.getORDERDISCOUNT());

							json.put("DATE", poEstHdr.getCollectionDate());
							json.put("POESTNO", poEstHdr.getPOESTNO());
							json.put("SUPPLIER", poEstHdr.getCustName());
							/*if(poEstHdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(poEstHdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", poEstHdr.getORDER_STATUS());
							json.put("COLLECTION_DATE", poEstHdr.getCollectionDate());
							json.put("AMOUNT", poEstHdr.getCURRENCYID() + "" + StrUtils.addZeroes(totalAmount, numberOfDecimal));
							json.put("EXCHAGERATE", StrUtils.addZeroes(poEstHdr.getCURRENCYUSEQT(), numberOfDecimal));
							json.put("CAMOUNT",
									StrUtils.addZeroes((totalAmount / poEstHdr.getCURRENCYUSEQT()), numberOfDecimal));
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

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PurchaseEstimateSummary.jsp");
				rd.forward(request, response);
			}
		}

		if (action.equalsIgnoreCase("getPurchaseOrderRemarks")) {
			String POESTNO = "", POESTLNNO = "", item = "";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				POESTNO = StrUtils.fString(request.getParameter("POESTNO"));
				POESTLNNO = StrUtils.fString(request.getParameter("POLNO"));
				item = StrUtils.fString(request.getParameter("ITEM"));

				List<PoEstDetRemarks> poEstDetRemarkslist = poEstDetDAO.GetPoDetRemarksbyitems(plant, POESTNO, POESTLNNO, item);
				if (poEstDetRemarkslist.size() > 0) {
					for (PoEstDetRemarks poEstDetRemarks : poEstDetRemarkslist) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("remarks", poEstDetRemarks.getREMARKS());
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
		}


	}

	private JSONObject getEditPODetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
		PoEstHdrDAO poEstHdrDAO = new PoEstHdrDAO();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String POESTNO = StrUtils.fString(request.getParameter("POESTNO")).trim();
		    String qtyest = StrUtils.fString(request.getParameter("QTYEST")).trim();
			String qtyRc = StrUtils.fString(request.getParameter("QTYRC")).trim();

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List<PoEstDet> podetail = poEstDetDAO.getPoDetByPono(plant, POESTNO);
			PoEstHdr poheader = poEstHdrDAO.getPoHdrByPono(plant, POESTNO);
			if (podetail.size() > 0) {
				for (PoEstDet poEstDet : podetail) {
					
					
					
					/*
					 * StrUtils.addZeroes(dCost, numberOfDecimal); resultJsonInt.put("POLNNO",
					 * (String)m.get("LNNO"));
					 */
					JSONObject resultJsonInt = new JSONObject();

					String catlogpath = itemMstDAO.getcatlogpath(plant, poEstDet.getITEM());
					String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath);

					
					qtyest = poEstDet.getQTYOR().toString();
					double dQtyest ="".equals(qtyest) ? 0.0d :  Double.parseDouble(qtyest);
		  			
		  			qtyRc = poEstDet.getQTYRC().toString();
					double dQtyRc ="".equals(qtyRc) ? 0.0d :  Double.parseDouble(qtyRc);
		  			
					double dQty = dQtyest-dQtyRc;
					resultJsonInt.put("quantity", StrUtils.addZeroes(dQty, "3"));
		  			
					resultJsonInt.put("LNNO", poEstDet.getPOESTLNNO());
					resultJsonInt.put("LNSTATUS", poEstDet.getLNSTAT());
					resultJsonInt.put("QTYRC", StrUtils.addZeroes(poEstDet.getQTYRC().doubleValue(), "3"));
					resultJsonInt.put("ITEM", poEstDet.getITEM());
					resultJsonInt.put("ITEMDESC", poEstDet.getItemDesc());
					resultJsonInt.put("ACCOUNTNAME", poEstDet.getACCOUNT_NAME());
					resultJsonInt.put("UOM", poEstDet.getUNITMO());
					resultJsonInt.put("QTY", StrUtils.addZeroes(poEstDet.getQTYOR().doubleValue(), "3"));
					resultJsonInt.put("PDELDATE", poEstDet.getPRODUCTDELIVERYDATE());
					resultJsonInt.put("UNITCOST", StrUtils.addZeroes(poEstDet.getUNITCOST(), numberOfDecimal));
					resultJsonInt.put("ITEMDISCOUNTTYPE", poEstDet.getDISCOUNT_TYPE());
					resultJsonInt.put("TAXTYPE", poEstDet.getTAX_TYPE());
					resultJsonInt.put("CATLOGPATH", cpath);

					if (poEstDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(poEstDet.getDISCOUNT(), "3"));
						double amount = (poEstDet.getUNITCOST() * (poEstDet.getQTYOR().doubleValue()))
								- (((poEstDet.getUNITCOST() * (poEstDet.getQTYOR().doubleValue())) / 100)
										* poEstDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					} else {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(poEstDet.getDISCOUNT(), numberOfDecimal));
						double amount = (poEstDet.getUNITCOST() * (poEstDet.getQTYOR().doubleValue()))
								- (poEstDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}
					
					
		  			
					String IBDiscount = new PoEstUtil().getIBDiscountSelectedItemVNO(plant, poheader.getCustCode(),
							poEstDet.getITEM());
					String discounttype = "";

					int plusIndex = IBDiscount.indexOf("%");
					if (plusIndex != -1) {
						IBDiscount = IBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}
					if(IBDiscount.equalsIgnoreCase(""))
						IBDiscount="0.00";
					
					String estQty = "", avlbQty = "";
                    Hashtable hts = new Hashtable();
             	 	hts.put("item", poEstDet.getITEM());
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
		  			
					List listItem = itemUtil.queryItemMstDetailsforpurchase(poEstDet.getITEM(), plant);
					Vector arrItem = (Vector) listItem.get(0);
					if (arrItem.size() > 0) {
						ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,"0");
						avlbQty= (String) ma.get("AVLBQTY");
						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get(8)));
						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
						resultJsonInt.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
						resultJsonInt.put("customerdiscount", IBDiscount);
						resultJsonInt.put("discounttype", discounttype);
						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get(41)));
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

	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {

	}

}
