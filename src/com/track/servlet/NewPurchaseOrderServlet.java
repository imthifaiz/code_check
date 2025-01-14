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
import java.util.UUID;
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
import com.track.dao.BillDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PltApprovalMatrixDAO;
import com.track.dao.PoDetApprovalDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoEstDetDAO;
import com.track.dao.PoEstHdrDAO;
import com.track.dao.PoHdrAprrovalDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.PurchaseAttachDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.BillHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.PoDet;
import com.track.db.object.PoDetApproval;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoEstDet;
import com.track.db.object.PoHdr;
import com.track.db.object.PoHdrApproval;
import com.track.db.object.PodetApprovalRemarks;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.FieldCopier;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/purchaseorder/*")
@SuppressWarnings({"rawtypes", "unchecked"})
public class NewPurchaseOrderServlet extends PurchaseOrderServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
//		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
//		DateUtils _dateUtils = new DateUtils();
		PoHdrDAO poHdrDAO= new PoHdrDAO();
		PoDetDAO poDetDAO = new PoDetDAO();
		PoHdrAprrovalDAO poHdrAprrovalDAO = new PoHdrAprrovalDAO();
		PoDetApprovalDAO poDetApprovalDAO = new PoDetApprovalDAO();
		PurchaseAttachDAO purchaseAttachDAO = new PurchaseAttachDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		MasterDAO _MasterDAO = new MasterDAO();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (action.equalsIgnoreCase("save") ||action.equalsIgnoreCase("convertToPurchase") || action.equalsIgnoreCase("convertfromPEtoPO")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String empName="",empno = "",jobNum="",currencyUseQty = "";
			List<Hashtable<String,String>> purchaseAttachmentList = null;
			List<Hashtable<String,String>> purchaseAttachmentInfoList = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				PoHdr pohdr = new PoHdr();

				List polnno = new ArrayList(), item = new ArrayList(), unitcost = new ArrayList(),
						qtyor = new ArrayList(), qtyRc = new ArrayList(),qtyord = new ArrayList(),unitmo = new ArrayList(), pddate = new ArrayList(),
						discount = new ArrayList(), dtype = new ArrayList(), accname = new ArrayList(),
						taxtype = new ArrayList(),  item_discount = new ArrayList(),chkdPONO = new ArrayList();
				String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
						shipworkphone="",shipcountry="",shiphpno="",shipemail="";
				int polnnocount = 0, chkdPONOCount = 0;
//				int accnamecount = 0, dtypecount = 0, itemcount = 0, unitcostcount = 0, qtyorcount = 0, unitmocount = 0, pddatecount = 0,
//				discountcount = 0,  taxtypecount = 0, ;

				purchaseAttachmentList = new ArrayList<Hashtable<String, String>>();
				purchaseAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

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
								pohdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PONO")) {
								pohdr.setPONO(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								pohdr.setCustName(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								pohdr.setCustCode(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								pohdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
								jobNum = StrUtils.fString(fileItem.getString()).trim();

							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PERSON_INCHARGE")) {
								pohdr.setPersonInCharge(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TELNO")) {
								pohdr.setContactNum(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD1")) {
								pohdr.setAddress(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD2")) {
								pohdr.setAddress2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ADD3")) {
								pohdr.setAddress3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								pohdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								pohdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								pohdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								pohdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								pohdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								pohdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								pohdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								pohdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								pohdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								pohdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								pohdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								pohdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}
							
                            if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								
								empno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empName = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								pohdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								pohdr.setORDERDISCOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								pohdr.setSHIPPINGCOST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								pohdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								pohdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								pohdr.setDELIVERYDATEFORMAT(Short.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								pohdr.setTAXTREATMENT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								pohdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								pohdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								pohdr.setADJUSTMENT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
								pohdr.setISDISCOUNTTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
								pohdr.setISSHIPPINGTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								pohdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								pohdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								pohdr.setORDERDISCOUNTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								pohdr.setCURRENCYUSEQT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									pohdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									pohdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
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
								polnno.add(StrUtils.fString(fileItem.getString()).trim());
								polnnocount++;
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("chkdPONO")) {
								chkdPONO.add(StrUtils.fString(fileItem.getString()).trim());
								chkdPONOCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTYRC")) {
								qtyRc.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyIsCount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ESTQTY")) {
								qtyord.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyEstCount++;
							}
							
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							
							//attachment

							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/"+ pohdr.getPONO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"+ pohdr.getPONO();
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
								Hashtable purchaseAttachment = new Hashtable<String, String>();
								purchaseAttachment.put("PLANT", plant);
								purchaseAttachment.put("FILETYPE", fileItem.getContentType());
								purchaseAttachment.put("FILENAME", fileName);
								purchaseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								purchaseAttachment.put("FILEPATH", fileLocationATT);
								purchaseAttachment.put("CRAT",DateUtils.getDateTime());
								purchaseAttachment.put("CRBY",username);
								purchaseAttachment.put("UPAT",DateUtils.getDateTime());
								purchaseAttachmentList.add(purchaseAttachment);
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
					pohdr.setEMPNO(empno);

					
					if (action.equalsIgnoreCase("convertfromPEtoPO")) {
						if (jobNum.contains("PE")) {
							pohdr.setPOESTNO(jobNum);
						}
					} else {
						if (!jobNum.contains("PE"))
							pohdr.setPOESTNO("");
						else
							pohdr.setPOESTNO(jobNum);
					}
					
					if(pohdr.getDELIVERYDATEFORMAT() == null){
						pohdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					
					if(!pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
						pohdr.setORDERDISCOUNT((pohdr.getORDERDISCOUNT()/pohdr.getCURRENCYUSEQT()));
					}
					pohdr.setSHIPPINGCOST((pohdr.getSHIPPINGCOST()/pohdr.getCURRENCYUSEQT()));
					pohdr.setADJUSTMENT((pohdr.getADJUSTMENT()/pohdr.getCURRENCYUSEQT()));
					
					pohdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					pohdr.setGOODSIMPORT(Short.valueOf(goodsimport));
					pohdr.setSTATUS("N");
					pohdr.setLOCALEXPENSES(0.0);
					pohdr.setCRBY(username);
					pohdr.setCRAT(DateUtils.getDateTime());
					pohdr.setSHIPCONTACTNAME(shipcontactname);
					pohdr.setSHIPDESGINATION(shipdesgination);
					pohdr.setSHIPWORKPHONE(shipworkphone);
					pohdr.setSHIPHPNO(shiphpno);
					pohdr.setSHIPEMAIL(shipemail);
					pohdr.setSHIPCOUNTRY(shipcountry);
					pohdr.setSHIPADDR1(shipaddr1);
					pohdr.setSHIPADDR2(shipaddr2);
					pohdr.setSHIPADDR3(shipaddr3);
					pohdr.setSHIPADDR4(shipaddr4);
					pohdr.setSHIPSTATE(shipstate);
					pohdr.setSHIPZIP(shipzip);
					String uniqueID = UUID.randomUUID().toString();
					if(pohdr.getORDER_STATUS().equalsIgnoreCase("APPROVAL PENDING")) {
						pohdr.setORDER_STATUS("Draft");
						pohdr.setAPPROVAL_STATUS("CREATE APPROVAL PENDING");
						pohdr.setUKEY(uniqueID);
					}else {
						pohdr.setAPPROVAL_STATUS("");
					}
					boolean issadded = false;
					int hdrid = poHdrDAO.addPoHdrReturnKey(pohdr);
					if(hdrid > 0) {
						issadded = true;
					}
					
					
					if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
						PoHdrApproval poHdrApproval= new PoHdrApproval();
						new FieldCopier().copyFields(pohdr, poHdrApproval);
						poHdrApproval.setID(hdrid);
						poHdrAprrovalDAO.addPoHdr(poHdrApproval);
					}
					
					
					if(issadded) {
						List<PoDet> podetlist = new ArrayList<PoDet>();
						
						double totalDiscountAmount = 0;
						if (!jobNum.contains("PE")) {
						for(int i=0;i<polnnocount;i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String)polnno.get(i)) -1;
//							int lnno = i+1;
							String disctype=(String)dtype.get(index);
								if(disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/pohdr.getCURRENCYUSEQT();
								}	
								totalDiscountAmount+=unitCost_AD;
						}
						}
						if(action.equalsIgnoreCase("convertfromPEtoPO")) {
							for(int i=0;i<chkdPONOCount;i++) {
								int index = Integer.parseInt((String)chkdPONO.get(i));
								if (jobNum.contains("PE"))
									index = Integer.parseInt((String) chkdPONO.get(i));

								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i+1;
								String disctype=(String)dtype.get(index);
								PoDet poDet = new PoDet();
								poDet.setPLANT(plant);
								poDet.setPONO(pohdr.getPONO());
								poDet.setTRANDATE(pohdr.getCollectionDate());
								poDet.setPOLNNO(lnno);
								if (jobNum.contains("PE")) {
									
//								String estlnno = Integer.parseInt((String) elnno.get(i));
								String estlnno = (String)polnno.get(i);
									poDet.setPOESTNO(jobNum);
									poDet.setPOESTLNNO(Integer.parseInt(estlnno));
								} else {
									poDet.setPOESTNO("");
									poDet.setPOESTLNNO(0);
								}
								poDet.setLNSTAT("N");
								poDet.setITEM((String)item.get(index));
								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, poDet.getITEM());
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
								String itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));
								poDet.setItemDesc(itemDesc);
								//poDet.setTRANDATE(DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));

								
								
								poDet.setUNITCOST((Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT()));
								poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								poDet.setQTYRC(BigDecimal.valueOf(0));
								poDet.setUNITMO((String)unitmo.get(index));
								poDet.setPRODUCTDELIVERYDATE((String)pddate.get(index));
								
								if(disctype.equalsIgnoreCase("%")) {
									poDet.setDISCOUNT(Double.valueOf((String)discount.get(index)));
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									poDet.setDISCOUNT((Double.valueOf((String)discount.get(index))/pohdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/pohdr.getCURRENCYUSEQT();
								}
								
								if(pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((pohdr.getORDERDISCOUNT()/(unitCost_AD*(pohdr.getORDERDISCOUNT()/100))/(totalDiscountAmount))*unitCost_AD);
								}else {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - (((pohdr.getORDERDISCOUNT()/pohdr.getCURRENCYUSEQT())/(totalDiscountAmount))*unitCost_AD);
								}
								if(Double.isNaN(unitCost_AOD)) {
									poDet.setUNITCOST_AOD(0.0);
								}else {
									poDet.setUNITCOST_AOD(unitCost_AOD);
								}
								poDet.setDISCOUNT_TYPE((String)dtype.get(index));
								poDet.setACCOUNT_NAME((String)accname.get(index));
								poDet.setTAX_TYPE((String)taxtype.get(index));
								poDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poDet.setUSERFLD2(pohdr.getJobNum());
								poDet.setUSERFLD3(pohdr.getCustName());
								poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
								poDet.setPRODGST(0);
								poDet.setCOMMENT1("");
								poDet.setLNSTAT("N");
								poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poDet.setCRBY(username);
								poDet.setCRAT(DateUtils.getDateTime());
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									poDet.setUKEY(uniqueID);
								}
								poDetDAO.addPoDet(poDet);
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									PoDetApproval poDetApproval= new PoDetApproval();
									new FieldCopier().copyFields(poDet, poDetApproval);
									poDetApproval.setUKEY(uniqueID);
									poDetApprovalDAO.addPoDet(poDetApproval);
								}
								
								podetlist.add(poDet);
							
						
							}
						}
						else if(action.equalsIgnoreCase("convertToPurchase")) {
							for(int i=0;i<chkdPONOCount;i++) {
								int index = Integer.parseInt((String)chkdPONO.get(i));
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i+1;
								String disctype=(String)dtype.get(index);
								PoDet poDet = new PoDet();
								poDet.setPLANT(plant);
								poDet.setPONO(pohdr.getPONO());
								poDet.setTRANDATE(pohdr.getCollectionDate());
								poDet.setPOLNNO(lnno);
								poDet.setITEM((String)item.get(index));
								poDet.setUNITCOST((Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT()));
								poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								poDet.setUNITMO((String)unitmo.get(index));
								poDet.setPRODUCTDELIVERYDATE((String)pddate.get(index));
								
								if(disctype.equalsIgnoreCase("%")) {
									poDet.setDISCOUNT(Double.valueOf((String)discount.get(index)));
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									poDet.setDISCOUNT((Double.valueOf((String)discount.get(index))/pohdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String)unitcost.get(index))/pohdr.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/pohdr.getCURRENCYUSEQT();
								}
								
								if(pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((pohdr.getORDERDISCOUNT()/(unitCost_AD*(pohdr.getORDERDISCOUNT()/100))/(totalDiscountAmount))*unitCost_AD);
								}else {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - (((pohdr.getORDERDISCOUNT()/pohdr.getCURRENCYUSEQT())/(totalDiscountAmount))*unitCost_AD);
								}
								if(Double.isNaN(unitCost_AOD)) {
									poDet.setUNITCOST_AOD(0.0);
								}else {
									poDet.setUNITCOST_AOD(unitCost_AOD);
								}
								poDet.setDISCOUNT_TYPE((String)dtype.get(index));
								poDet.setACCOUNT_NAME((String)accname.get(index));
								poDet.setTAX_TYPE((String)taxtype.get(index));
								poDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poDet.setUSERFLD2(pohdr.getJobNum());
								poDet.setUSERFLD3(pohdr.getCustName());
								poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
								poDet.setPRODGST(0);
								poDet.setCOMMENT1("");
								poDet.setLNSTAT("N");
								poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								poDet.setCRBY(username);
								poDet.setCRAT(DateUtils.getDateTime());
								poDet.setPOESTNO("");
								poDet.setPOESTLNNO(0);
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									poDet.setUKEY(uniqueID);
								}
								poDetDAO.addPoDet(poDet);
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									PoDetApproval poDetApproval= new PoDetApproval();
									new FieldCopier().copyFields(poDet, poDetApproval);
									poDetApproval.setUKEY(uniqueID);
									poDetApprovalDAO.addPoDet(poDetApproval);
								}
								
								podetlist.add(poDet);
							}						 
							} else {
							for (int i = 0; i < polnnocount; i++) {
								int index = Integer.parseInt((String) polnno.get(i)) - 1;
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i + 1;
								String disctype = (String) dtype.get(index);
								PoDet poDet = new PoDet();
								poDet.setPLANT(plant);
								poDet.setPONO(pohdr.getPONO());
								poDet.setTRANDATE(pohdr.getCollectionDate());
								poDet.setPOLNNO(lnno);
								if (jobNum.contains("PE")) {
									String estlnno = (String)polnno.get(i);
										poDet.setPOESTNO(jobNum);
										poDet.setPOESTLNNO(Integer.parseInt(estlnno));
									} else {
										poDet.setPOESTNO("");
										poDet.setPOESTLNNO(0);
									}
								poDet.setITEM((String) item.get(index));
								poDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / pohdr.getCURRENCYUSEQT()));
								poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poDet.setUNITMO((String) unitmo.get(index));
								poDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));

								if (disctype.equalsIgnoreCase("%")) {
									poDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT();
								}

								if (pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((pohdr.getORDERDISCOUNT()
											/ (unitCost_AD * (pohdr.getORDERDISCOUNT() / 100)) / (totalDiscountAmount))
											* unitCost_AD);
								} else {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD)
											- (((pohdr.getORDERDISCOUNT() / pohdr.getCURRENCYUSEQT())
													/ (totalDiscountAmount)) * unitCost_AD);
								}
								
								if(Double.isNaN(unitCost_AOD)) {
									poDet.setUNITCOST_AOD(0.0);
								}else {
									poDet.setUNITCOST_AOD(unitCost_AOD);
								}
								poDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poDet.setACCOUNT_NAME((String) accname.get(index));
								poDet.setTAX_TYPE((String) taxtype.get(index));
								poDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setUSERFLD2(pohdr.getJobNum());
								poDet.setUSERFLD3(pohdr.getCustName());
								poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
								poDet.setPRODGST(0);
								poDet.setCOMMENT1("");
								poDet.setLNSTAT("N");
								poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setCRBY(username);
								poDet.setCRAT(DateUtils.getDateTime());
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									poDet.setUKEY(uniqueID);
								}
								poDetDAO.addPoDet(poDet);
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									PoDetApproval poDetApproval= new PoDetApproval();
									new FieldCopier().copyFields(poDet, poDetApproval);
									poDetApproval.setUKEY(uniqueID);
									poDetApprovalDAO.addPoDet(poDetApproval);
								}
								
								podetlist.add(poDet);
							}
						}
						
						if (action.equalsIgnoreCase("convertfromPEtoPO")) {
							   
							for (int i = 0; i < chkdPONOCount; i++) {
								int index = Integer.parseInt((String) chkdPONO.get(i)) - 1;
								if (jobNum.contains("PE"))
									index = Integer.parseInt((String) chkdPONO.get(i));
								int lnno = i + 1;								
								
								//Add Purchase Estimate Remark on PO -Azees (04.02.22)
								Hashtable<String, String> ht = new Hashtable<>();
								ht.put(IDBConstants.PLANT, plant);
								ht.put(IDBConstants.PODET_POESTNUM, jobNum);
								ht.put(IDBConstants.PODET_POESTLNNO, String.valueOf((String)polnno.get(index)));
								ht.put("ITEM", String.valueOf((String) item.get(index)));

								List al = new PoEstDetDAO().selectPoMultiRemarks("ISNULL(REMARKS,'') REMARKS ", ht,"");
								if (al.size() > 0) {
									for (int j = 0; j < al.size(); j++) {
										Map m = (Map) al.get(j);
										String remarksval = (String) m.get("REMARKS");
										
										Hashtable<String, String> htRemarksest = new Hashtable<>();
										htRemarksest.put(IDBConstants.PLANT, plant);
										htRemarksest.put(IDBConstants.PODET_PONUM, pohdr.getPONO());
										htRemarksest.put(IDBConstants.PODET_POLNNO, Integer.toString(lnno));
										htRemarksest.put(IDBConstants.PODET_ITEM, (String) item.get(index));
										htRemarksest.put(IDBConstants.REMARKS, remarksval);
										if (!new PoDetDAO().isExisitPoMultiRemarks(htRemarksest)) {
											htRemarksest.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htRemarksest.put(IDBConstants.CREATED_BY, username);
											boolean insertFlag = new POUtil().savePoMultiRemarks(htRemarksest);
																			
										if (insertFlag) {
											Hashtable<String, String> htRecvHis = new Hashtable();
											htRecvHis.put(IDBConstants.PLANT, plant);
											htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
											htRecvHis.put("ORDNUM",pohdr.getPONO());
											htRecvHis.put(IDBConstants.ITEM, "");
											htRecvHis.put("BATNO", "");
											htRecvHis.put(IDBConstants.LOC, "");
											htRecvHis.put("REMARKS", remarksval);
											htRecvHis.put(IDBConstants.CREATED_BY, username);
											htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
											htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											movHisDao.insertIntoMovHis(htRecvHis);
										}
										}
										}
									}
								
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.PODET_PONUM, pohdr.getPONO());
								htRemarks.put(IDBConstants.PODET_POLNNO, Integer.toString(lnno));
								htRemarks.put(IDBConstants.PODET_ITEM, (String) item.get(index));
								if (!new PoDetDAO().isExisitPoMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									boolean insertFlag = new POUtil().savePoMultiRemarks(htRemarks);
								}
								}
							
							// Estimate Status & Qty Update
							if (jobNum.contains("PE")) {
							
								for (int i = 0; i < chkdPONOCount; i++) {
									int index = Integer.parseInt((String) chkdPONO.get(i)) - 1;
									 
									if (jobNum.contains("PE"))
										index = Integer.parseInt((String) chkdPONO.get(i));
									int estlnno = Integer.parseInt((String) polnno.get(i));
									
									PoEstDetDAO _poEstDetDAO = new PoEstDetDAO();
									PoEstHdrDAO _poEstHdrDAO = new PoEstHdrDAO();
									String updatepoestHdr = "", updatepoestDet = "";
									Hashtable htCondition = new Hashtable();
									htCondition.put("PLANT", plant);
									htCondition.put("POESTNO", jobNum);
									htCondition.put("POESTLNNO", String.valueOf(estlnno));
									String issuingQty = (String) qtyor.get(index);
									String qtyOr = (String) qtyord.get(index);

									String issuedqty = (String) qtyRc.get(index);
									double Ordqty = Double.parseDouble(qtyOr);
									double tranQty = Double.parseDouble(issuingQty);
									double issqty = Double.parseDouble(issuedqty);
									double sumqty = issqty + tranQty;
									sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);

									String extraCond = " AND  Qtyor >= isNull(qtyRc,0) + " + issuingQty;
									if (Ordqty == sumqty) {
										updatepoestDet = "set qtyRc= isNull(qtyRc,0) + " + issuingQty
												+ ", LNSTAT='C' ";

									} else {
										updatepoestDet = "set qtyRc= isNull(qtyRc,0) + " + issuingQty
												+ ", LNSTAT='O' ";

									}

									boolean insertFlag = _poEstDetDAO.updatepo(updatepoestDet, htCondition,extraCond);

									if (insertFlag) {
										htCondition.remove("POESTLNNO");

										insertFlag = _poEstDetDAO.isExisit(htCondition, "LNSTAT in ('O','N')");
										if (!insertFlag) {
											updatepoestHdr = "set  STATUS='Confirm',ORDER_STATUS='PROCESSED' ";
											insertFlag = _poEstHdrDAO.updatePO(updatepoestHdr, htCondition, "");
										} else {
											updatepoestHdr = "set ORDER_STATUS='PARTIALLY PROCESSED' ";
											insertFlag = _poEstHdrDAO.updatePO(updatepoestHdr, htCondition, "");
										}

									}
								}
							}

						}
						
						int attchSize = purchaseAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable purchaseAttachmentat = new Hashtable<String, String>();
								purchaseAttachmentat = purchaseAttachmentList.get(i);
								purchaseAttachmentat.put("PONO", pohdr.getPONO());
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
									purchaseAttachmentat.put("UKEY", uniqueID);
								}
								purchaseAttachmentInfoList.add(purchaseAttachmentat);
							}
							purchaseAttachDAO.addpurchaseAttachments(purchaseAttachmentInfoList, plant);
							if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
								purchaseAttachDAO.addpurchaseAttachmentsApproval(purchaseAttachmentList, plant);
							}
							
						}

						if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
							List<PoDetRemarks> podetremarks = poDetDAO.getPoDetRemarksByPono(plant, pohdr.getPONO());
							if(podetremarks.size() > 0) {
									//PODETAPPROVE NULL FIX-Azees 27.08.22
								for (PoDetRemarks PoDetRk : podetremarks) {
									PodetApprovalRemarks podetApprovalRemarks = new PodetApprovalRemarks();
									new FieldCopier().copyFields(PoDetRk, podetApprovalRemarks);
									podetApprovalRemarks.setUKEY(uniqueID);
									podetApprovalRemarks.setPLANT(plant);
									poDetApprovalDAO.addPoDetApprovalRemarks(podetApprovalRemarks);
								}
							
							}
						}

						new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pohdr.getPONO());

						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						String POESTNO="";
						if(!pohdr.getPOESTNO().equalsIgnoreCase(""))
							POESTNO=","+ pohdr.getPOESTNO();
						htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +POESTNO+ "," + pohdr.getRemark1());
						if (!pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									pohdr.getCustName() +POESTNO+ "," + pohdr.getRemark1() + "," + pohdr.getREMARK3());
						} else if (!pohdr.getRemark1().equals("") && pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +POESTNO+ "," + pohdr.getRemark1());
						} else if (pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +POESTNO+ "," + pohdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +POESTNO);
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (pohdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, pohdr.getRemark1());
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
									htRecvHis.put("REMARKS", pohdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (pohdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, pohdr.getREMARK3());
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
									htRecvHis.put("REMARKS", pohdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (pohdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, pohdr.getINCOTERMS());

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
									htRecvHis.put("REMARKS", pohdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (int i = 0; i < chkdPONOCount; i++) {
								int index = Integer.parseInt((String) chkdPONO.get(i));

								int Polno = i + 1;
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.PODET_PONUM, pohdr.getPONO());
								htRemarks.put(IDBConstants.PODET_POLNNO, Integer.toString(Polno));
								htRemarks.put(IDBConstants.PODET_ITEM, (String) item.get(index));
								if (!new PoDetDAO().isExisitPoMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									flag = new POUtil().savePoMultiRemarks(htRemarks);
								}
							}

							for (PoDet porderdet : podetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
								htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
								htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.QTY, porderdet.getQTYOR());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
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
						String message = "Purchase Order Added Successfully.";
						if (ajax) {
							request.setAttribute("PONO", pohdr.getPONO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "receiving_list".equals(sendAttachment)) {
								viewPOReport(request, response, "printPO");
							}
							if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								viewPOReport(request, response, "printPOInvoice");
							}
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseorder/summary?msg=" + message);
						}
					} else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Purchase Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseorder/summary?msg=" + message);
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
					if (jobNum.contains("PE"))
					response.sendRedirect("../purchaseorderestimate/convertfromPEtoPO?POESTNO="+jobNum+"&msg=" + ThrowableUtil.getMessage(e));
					else
					response.sendRedirect("../purchaseorder/summary?msg=" + ThrowableUtil.getMessage(e));
				}
			}

		}

		if (action.equalsIgnoreCase("edit")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			List<Hashtable<String, String>> purchaseAttachmentList = null;
			List<Hashtable<String, String>> purchaseAttachmentInfoList = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);

                String empno="",empName="";
                String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
                		shipworkphone="",shipcountry="",shiphpno="",shipemail="";
				List polnno = new ArrayList(), prvlnno = new ArrayList(), item = new ArrayList(),
						unitcost = new ArrayList(), qtyor = new ArrayList(), unitmo = new ArrayList(),
						pddate = new ArrayList(), discount = new ArrayList(), dtype = new ArrayList(),
						accname = new ArrayList(), recqty = new ArrayList(), ponoline = new ArrayList(),
						taxtype = new ArrayList();
				int polnnocount = 0, draftstatus = 0;//unitmocount = 0, unitcostcount = 0, taxtypecount = 0, recqtycount = 0, qtyorcount = 0, pddatecount = 0, itemcount = 0, dtypecount = 0, discountcount = 0, accnamecount = 0, prvlnnocount = 0, ponolinecount = 0, 

				purchaseAttachmentList = new ArrayList<Hashtable<String, String>>();
				purchaseAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					String revgersecharge = "0";
					String goodsimport = "0";
					ut = DbBean.getUserTranaction();
					ut.begin();
					PoHdr pohdr = new PoHdr();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* POHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								pohdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PONO")) {
								pohdr.setPONO(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								pohdr.setCustName(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								pohdr.setCustCode(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								pohdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PERSON_INCHARGE")) {
								pohdr.setPersonInCharge(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("TELNO")) {
								pohdr.setContactNum(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD1")) {
								pohdr.setAddress(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD2")) {
								pohdr.setAddress2(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ADD3")) {
								pohdr.setAddress3(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								pohdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								pohdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								pohdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								pohdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								pohdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								pohdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								pohdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								pohdr.setCURRENCYID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								pohdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								pohdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								pohdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								pohdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								pohdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount")) {
								pohdr.setORDERDISCOUNT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shippingcost")) {
								pohdr.setSHIPPINGCOST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								pohdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								pohdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							 if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
									
									empno = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
									empName = StrUtils.fString(fileItem.getString()).trim();
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

							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								pohdr.setDELIVERYDATEFORMAT(Short
										.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								pohdr.setTAXTREATMENT(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								pohdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								pohdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("adjustment")) {
								pohdr.setADJUSTMENT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("draftstatus")) {
								draftstatus = Integer.valueOf(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("odiscounttaxstatus")) {
								pohdr.setISDISCOUNTTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("shiptaxstatus")) {
								pohdr.setISSHIPPINGTAX(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								pohdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								pohdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("discount_type")) {
								pohdr.setORDERDISCOUNTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								pohdr.setCURRENCYUSEQT(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									pohdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									pohdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							/* PODET */

							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								polnno.add(StrUtils.fString(fileItem.getString()).trim());
								polnnocount++;
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

							if (fileItem.getFieldName().equalsIgnoreCase("polnno")) {
								ponoline.add(StrUtils.fString(fileItem.getString()).trim());
//								ponolinecount++;
							}

						} else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {

							// attachment

							if (fileItem.getFieldName().equalsIgnoreCase("file")) {
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/" + pohdr.getPONO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"
										+ pohdr.getPONO();
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
								Hashtable purchaseAttachment = new Hashtable<String, String>();
								purchaseAttachment.put("PLANT", plant);
								purchaseAttachment.put("FILETYPE", fileItem.getContentType());
								purchaseAttachment.put("FILENAME", fileName);
								purchaseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								purchaseAttachment.put("FILEPATH", fileLocationATT);
								purchaseAttachment.put("CRAT", DateUtils.getDateTime());
								purchaseAttachment.put("CRBY", username);
								purchaseAttachment.put("UPAT", DateUtils.getDateTime());
								purchaseAttachmentList.add(purchaseAttachment);
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
					pohdr.setEMPNO(empno);
					if (pohdr.getDELIVERYDATEFORMAT() == null) {
						pohdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					if (!pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
						pohdr.setORDERDISCOUNT((pohdr.getORDERDISCOUNT() / pohdr.getCURRENCYUSEQT()));
					}
					
				
					pohdr.setSHIPPINGCOST((pohdr.getSHIPPINGCOST() / pohdr.getCURRENCYUSEQT()));
					pohdr.setADJUSTMENT((pohdr.getADJUSTMENT() / pohdr.getCURRENCYUSEQT()));
					pohdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					pohdr.setGOODSIMPORT(Short.valueOf(goodsimport));

					/* boolean issadded = poHdrDAO.addPoHdr(pohdr); */
					PoHdr pohdrbypono = poHdrDAO.getPoHdrByPono(plant, pohdr.getPONO());
					
					if(!pohdr.getORDER_STATUS().equalsIgnoreCase("APPROVAL PENDING")) {
						if (draftstatus == 1) {
							pohdr.setORDER_STATUS("Open");
						}else {
							pohdr.setORDER_STATUS("Draft");
						}
					}
					pohdr.setSTATUS(pohdrbypono.getSTATUS());
					pohdr.setCRBY(pohdrbypono.getCRBY());
					pohdr.setCRAT(pohdrbypono.getCRAT());
					pohdr.setLOCALEXPENSES(pohdrbypono.getLOCALEXPENSES());
					pohdr.setSHIPCONTACTNAME(shipcontactname);
					pohdr.setSHIPDESGINATION(shipdesgination);
					pohdr.setSHIPWORKPHONE(shipworkphone);
					pohdr.setSHIPHPNO(shiphpno);
					pohdr.setSHIPEMAIL(shipemail);
					pohdr.setSHIPCOUNTRY(shipcountry);
					pohdr.setSHIPADDR1(shipaddr1);
					pohdr.setSHIPADDR2(shipaddr2);
					pohdr.setSHIPADDR3(shipaddr3);
					pohdr.setSHIPADDR4(shipaddr4);
					pohdr.setSHIPSTATE(shipstate);
					pohdr.setSHIPZIP(shipzip);
					
					PoHdr oldpohdr = poHdrDAO.getPoHdrByPono(plant, pohdr.getPONO());
					String uniqueID = UUID.randomUUID().toString();
					if(pohdr.getORDER_STATUS().equalsIgnoreCase("APPROVAL PENDING")) {
						pohdr.setORDER_STATUS(oldpohdr.getORDER_STATUS());
						pohdr.setAPPROVAL_STATUS("EDIT APPROVAL PENDING");
						pohdr.setUKEY(uniqueID);
					}
					else  {
						if(oldpohdr.getAPPROVAL_STATUS().equalsIgnoreCase(""))
							pohdr.setAPPROVAL_STATUS("");
					}
					
					if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
						PoHdrApproval poHdrApproval= new PoHdrApproval();
						new FieldCopier().copyFields(oldpohdr, poHdrApproval);
						poHdrApproval.setUKEY(uniqueID);
						poHdrAprrovalDAO.addPoHdr(poHdrApproval);
						
						List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, pohdr.getPONO());
						for (PoDet poDet : podetail) {
							PoDetApproval poDetApproval = new PoDetApproval(); 
							new FieldCopier().copyFields(poDet, poDetApproval);
							poDetApproval.setUKEY(uniqueID);
							poDetApprovalDAO.addPoDet(poDetApproval);
						}
						
						
					}
					boolean issadded = poHdrDAO.updatePoHdr(pohdr);
					
					
					List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, pohdr.getPONO());
					for (PoDet poDet : podetail) {
						boolean Detstatus = true;
						for (int i = 0; i < polnnocount; i++) {
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							if (poDet.getPOLNNO() == ponumline) {
								Detstatus = false;
							}
						}
						if (Detstatus) {
							poDetDAO.DeletePoDet(plant, pohdr.getPONO(), poDet.getPOLNNO());
						}
					}

					if (issadded) {
						List<PoDet> podetlist = new ArrayList<PoDet>();
						double totalDiscountAmount = 0;
						for (int i = 0; i < polnnocount; i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String) polnno.get(i)) - 1;
//							int lnno = i + 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
//								PoDet poDet = poDetDAO.getPoDetByPonoPllno(plant, pohdrbypono.getPONO(), ponumline);
//								int lineno = poDet.getPOLNNO();
								if (disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT();
								}
								totalDiscountAmount += unitCost_AD;
							}
						}

						for (int i = 0; i < polnnocount; i++) {
							int index = Integer.parseInt((String) polnno.get(i)) - 1;
							int lnno = i + 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
								PoDet poDet = poDetDAO.getPoDetByPonoPllno(plant, pohdrbypono.getPONO(), ponumline);
								int lineno = poDet.getPOLNNO();
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								if(poDet.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyor.get(index)))//Status update Azees
								{
									if(poDet.getQTYRC().doubleValue()>0)
										poDet.setLNSTAT("O");
									else
										poDet.setLNSTAT("N");
								}
								poDet.setTRANDATE(pohdr.getCollectionDate());
								poDet.setITEM((String) item.get(index));
								poDet.setPOLNNO(lnno);
								poDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / pohdr.getCURRENCYUSEQT()));
								poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poDet.setUNITMO((String) unitmo.get(index));
								poDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));
								if (disctype.equalsIgnoreCase("%")) {
									poDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT();
								}

								if (pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - ((pohdr.getORDERDISCOUNT()
											/ (unitCost_AD * (pohdr.getORDERDISCOUNT() / 100)) / (totalDiscountAmount))
											* unitCost_AD);
								} else {
									unitCost_AD = (unitCost_AD / Double.parseDouble((String) qtyor.get(index)));
									unitCost_AOD = (unitCost_AD)
											- (((pohdr.getORDERDISCOUNT() / pohdr.getCURRENCYUSEQT())
													/ (totalDiscountAmount)) * unitCost_AD);
								}
								if(Double.isNaN(unitCost_AOD)) {
									poDet.setUNITCOST_AOD(0.0);
								}else {
									poDet.setUNITCOST_AOD(unitCost_AOD);	
								}	
								poDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poDet.setACCOUNT_NAME((String) accname.get(index));
								poDet.setTAX_TYPE((String) taxtype.get(index));
								poDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setUSERFLD2(pohdr.getJobNum());
								poDet.setUSERFLD3(pohdr.getCustName());
								poDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setUPAT(DateUtils.getDateTime());
								poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
								poDet.setUPBY(username);					
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
									poDet.setUKEY(uniqueID);
								}
								poDetDAO.updatePoDetpoEdit(poDet,lineno);
								podetlist.add(poDet);
							} else {
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								PoDet poDet = new PoDet();
								poDet.setPLANT(plant);
								poDet.setPONO(pohdr.getPONO());
								poDet.setTRANDATE(pohdr.getCollectionDate());
								poDet.setPOLNNO(lnno);
								poDet.setITEM((String) item.get(index));
								poDet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / pohdr.getCURRENCYUSEQT()));
								poDet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								poDet.setUNITMO((String) unitmo.get(index));
								poDet.setPRODUCTDELIVERYDATE((String) pddate.get(index));

								if (disctype.equalsIgnoreCase("%")) {
									poDet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									poDet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ pohdr.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / pohdr.getCURRENCYUSEQT();
								}

								if (pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD) - (unitCost_AD * (pohdr.getORDERDISCOUNT() / 100));
								} else {
									unitCost_AD = (unitCost_AD/Double.parseDouble((String)qtyor.get(index)));
									unitCost_AOD = (unitCost_AD)
											- ((pohdr.getORDERDISCOUNT() / pohdr.getCURRENCYUSEQT()));
								}
								if(Double.isNaN(unitCost_AOD)) {
									poDet.setUNITCOST_AOD(0.0);
								}else {
									poDet.setUNITCOST_AOD(unitCost_AOD);	
								}								
								poDet.setDISCOUNT_TYPE((String) dtype.get(index));
								poDet.setACCOUNT_NAME((String) accname.get(index));
								poDet.setTAX_TYPE((String) taxtype.get(index));
								poDet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setUSERFLD2(pohdr.getJobNum());
								poDet.setUSERFLD3(pohdr.getCustName());
								poDet.setCURRENCYUSEQT(pohdr.getCURRENCYUSEQT());
								poDet.setPRODGST(0);
								poDet.setCOMMENT1("");
								poDet.setLNSTAT("N");
								poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								poDet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								poDet.setCRBY(username);
								poDet.setCRAT(DateUtils.getDateTime());
								if (pohdr.getJobNum().contains("PE")) {
										poDet.setPOESTNO(pohdr.getJobNum());
										poDet.setPOESTLNNO(lnno);
									} else {
										poDet.setPOESTNO("");
										poDet.setPOESTLNNO(0);
									}
								
								if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
									poDet.setUKEY(uniqueID);
								}
								poDetDAO.addPoDet(poDet);
								podetlist.add(poDet);
							}
						}

						int attchSize = purchaseAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable purchaseAttachmentat = new Hashtable<String, String>();
								purchaseAttachmentat = purchaseAttachmentList.get(i);
								purchaseAttachmentat.put("PONO", pohdr.getPONO());
								purchaseAttachmentInfoList.add(purchaseAttachmentat);
							}
							purchaseAttachDAO.addpurchaseAttachments(purchaseAttachmentInfoList, plant);
						}
						
						if (pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
							List<PoDetRemarks> podetremarks = poDetDAO.getPoDetRemarksByPono(plant, pohdr.getPONO());
							if (podetremarks.size() > 0) {
								for (PoDetRemarks PoDetRk : podetremarks) {
									PodetApprovalRemarks podetApprovalRemarks = new PodetApprovalRemarks();
									new FieldCopier().copyFields(PoDetRk, podetApprovalRemarks);
									podetApprovalRemarks.setUKEY(uniqueID);
									podetApprovalRemarks.setPLANT(plant);
									poDetApprovalDAO.addPoDetApprovalRemarks(podetApprovalRemarks);
									
								}
							}
						}
						 

						List<PoDetRemarks> podetrmk = poDetDAO.getPoDetRemarksByPono(plant, pohdr.getPONO());
						List<PoDetRemarks> podetrmkupdate = new ArrayList<PoDetRemarks>();
						for (PoDetRemarks poDetRemarks : podetrmk) {
							boolean rmstatus = false;
							int newlnno = 0;
							for (int i = 0; i < polnnocount; i++) {
								int prvlineno = Integer.parseInt((String) prvlnno.get(i));
								if (poDetRemarks.getPOLNNO() == prvlineno) {
									rmstatus = true;
									newlnno = Integer.parseInt((String) polnno.get(i));
								}
							}

							if (rmstatus) {
								poDetRemarks.setPOLNNO(newlnno);
								poDetRemarks.setUKEY(uniqueID);
								podetrmkupdate.add(poDetRemarks);
							} else {
								poDetDAO.DeletePoDetRemarks(plant, poDetRemarks.getID_REMARKS());
							}

						}

						if (podetrmkupdate.size() > 0) {
							
							poDetDAO.updatePoDetRemarksBYID(podetrmkupdate);
						}
						
						
						

						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.UPDATE_IB);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() + "," + pohdr.getRemark1());
						if (!pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									pohdr.getCustName() + "," + pohdr.getRemark1() + "," + pohdr.getREMARK3());
						} else if (!pohdr.getRemark1().equals("") && pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() + "," + pohdr.getRemark1());
						} else if (pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() + "," + pohdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName());
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (pohdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, pohdr.getRemark1());
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
									htRecvHis.put("REMARKS", pohdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (pohdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, pohdr.getREMARK3());
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
									htRecvHis.put("REMARKS", pohdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (pohdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, pohdr.getINCOTERMS());

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
									htRecvHis.put("REMARKS", pohdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (PoDet porderdet : podetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "PURCHASE_ORDER_UPDATE_PRODUCT");
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
								htRecvHis.put(IDBConstants.MOVHIS_ORDLNO, porderdet.getPOLNNO());
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
						boolean isupdatests=false;
						boolean approvalcheck = new PltApprovalMatrixDAO().CheckApproval(plant, "PURCHASE", "UPDATE");
						if (approvalcheck) {
						if (draftstatus != 1)
							isupdatests=true;
						} else 
							isupdatests=true;
						
							if (isupdatests) {

							//if (!pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("EDIT APPROVAL PENDING")) {
								if (flag) {
									Hashtable htCondition = new Hashtable();
									htCondition.put("PLANT", plant);
									htCondition.put("PONO", pohdr.getPONO());
									flag = new PoDetDAO().isExisit(htCondition, "LNSTAT in ('O','N')");
									if (!flag) {
										String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
										flag = new PoHdrDAO().updatePO(updateHdr, htCondition, "");
									} else {
										flag = new PoDetDAO().isExisit(htCondition, "LNSTAT in ('O')");
										if (!flag) {
											flag = new PoDetDAO().isExisit(htCondition, "LNSTAT in ('C','N')");
											if (!flag) {
												String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
												flag = new PoHdrDAO().updatePO(updateHdr, htCondition, "");
											} else {
												flag = new PoDetDAO().isExisit(htCondition, "LNSTAT in ('C')");
												if (!flag) {
													String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
													flag = new PoHdrDAO().updatePO(updateHdr, htCondition, "");
												} else {
													String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
													flag = new PoHdrDAO().updatePO(updateHdr, htCondition, "");
												}
											}
										} else {
											String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
											flag = new PoHdrDAO().updatePO(updateHdr, htCondition, "");
										}
									}
								}
							//}

						}
						
						 DbBean.CommitTran(ut);
						 String message = "Purchase Order Updated Successfully.";
						if (ajax) {
							request.setAttribute("PONO", pohdr.getPONO());
							EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
							Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
							String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
							if ("both".equals(sendAttachment) || "receiving_list".equals(sendAttachment)) {
								viewPOReport(request, response, "printPO");
							}
							if ("both".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
								viewPOReport(request, response, "printPOInvoice");
							}
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "100");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseorder/summary?msg=" + message);
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
							response.sendRedirect("../purchaseorder/summary?msg=" + message);
						}
					}
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../purchaseorder/summary?msg=" + ThrowableUtil.getMessage(e));
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
				String pono = request.getParameter("r_pono");
				String polno = request.getParameter("r_lnno");
				boolean insertFlag = false;
				boolean isexist = poDetDAO.IsExistPoDetRemarks(plant, pono, polno, item);
				if (isexist) {
					List<PoDetRemarks> poDetRemarkslist = poDetDAO.GetPoDetRemarksbyitems(plant, pono, polno, item);
					for (PoDetRemarks poDetRemarks : poDetRemarkslist) {
						poDetDAO.DeletePoDetRemarks(plant, poDetRemarks.getID_REMARKS());
					}
				}

				for (int i = 0; i < remarks.length; i++) {
					PoDetRemarks poDetRemarks = new PoDetRemarks();
					poDetRemarks.setREMARKS(remarks[i]);
					poDetRemarks.setITEM(item);
					poDetRemarks.setPONO(pono);
					poDetRemarks.setPOLNNO(Integer.valueOf(polno));
					poDetRemarks.setCRBY(username);
					poDetRemarks.setPLANT(plant);
					poDetRemarks.setCRAT(DateUtils.getDateTime());
					insertFlag = poDetDAO.addPoDetRemarks(poDetRemarks);

					if (insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
						htRecvHis.put("ORDNUM", pono);
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
					resultJson.put("MESSAGE", "Purchase Order Remarks Created Successfully.");
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
			POUtil _POUtil = new POUtil();

			String User_id = username;
			String pono = StrUtils.fString(request.getParameter("PONO")).trim();
			try {
				
				 
				
				
				Hashtable htPoHrd = new Hashtable();
				htPoHrd.put(IDBConstants.PLANT, plant);
				htPoHrd.put(IDBConstants.PODET_PONUM, pono);
//				sqlBean sqlbn = new sqlBean();
				boolean isValidOrder = new PoHdrDAO().isExisit(htPoHrd, "");
				boolean isOrderInProgress = new PoDetDAO().isExisit(htPoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {
						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, pono);
						if (!isExistsPaymentDetails) {
							
							PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();
							boolean approvalcheck = pltApproval.CheckApprovalByUser(plant, "PURCHASE", "DELETE", username);
							if(approvalcheck) {
								String uniqueID = UUID.randomUUID().toString();
								PoHdrApproval poHdrApproval= new PoHdrApproval();
								PoHdr oldpohdr = poHdrDAO.getPoHdrByPono(plant, pono);
								new FieldCopier().copyFields(oldpohdr, poHdrApproval);
								poHdrApproval.setUKEY(uniqueID);
								poHdrAprrovalDAO.addPoHdr(poHdrApproval);
								
								List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, pono);
								for (PoDet poDet : podetail) {
									PoDetApproval poDetApproval = new PoDetApproval(); 
									new FieldCopier().copyFields(poDet, poDetApproval);
									poDetApproval.setUKEY(uniqueID);
									poDetApprovalDAO.addPoDet(poDetApproval);
								}
								oldpohdr.setUKEY(uniqueID);
								oldpohdr.setAPPROVAL_STATUS("DELETE APPROVAL PENDING");
								poHdrDAO.updatePoHdr(oldpohdr);
								resultJson.put("MESSAGE", "Purchase Order Delete Is Send for Approval.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								Hashtable htCond = new Hashtable();
								htCond.put("PLANT", plant);
								htCond.put("PONO", pono);
//							String query = "pono,custCode";
//							ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
								Boolean value = _POUtil.removeRow(plant, pono, User_id);
								if (value) {
									resultJson.put("MESSAGE", "Purchase Order Deleted Successfully.");
									resultJson.put("ERROR_CODE", "100");
								} else {
									resultJson.put("MESSAGE", "Purchase Order Not Deleted.");
									resultJson.put("ERROR_CODE", "97");
								}
							}
							
						} else {
							resultJson.put("MESSAGE", "Purchase Order Not Deleted.");
							resultJson.put("ERROR_CODE", "96");
						}
					} else {
						resultJson.put("MESSAGE", "Purchase Order Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Purchase Order Not Deleted.");
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
		if(action.equalsIgnoreCase("suppliersummary")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/supplierSummaryReport.jsp?action=supplierSummaryReport.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			String ID = request.getParameter("attachid");
			FileHandling fileHandling = new FileHandling();
			List purchaseAttachment = null;
			try {

				purchaseAttachment = purchaseAttachDAO.getpurchaseAttachById(plant, ID);
				Map poAttach = (Map) purchaseAttachment.get(0);
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
				purchaseAttachDAO.deletepurchaseAttachByPrimId(plant, ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}
		
		if (action.equalsIgnoreCase("summarydetails")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryInbound.jsp?action=OrderSummaryInbound.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("closeorder")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrder.jsp?action=closeByOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(action.equalsIgnoreCase("recivedordershistory")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReceivedOrdersHistory.jsp?action=ReceivedOrdersHistory.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (action.equals("SUPPLIER_INVOICE_CHECK")) {
			String SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER")).trim();
			String SINVOICENO = StrUtils.fString(request.getParameter("SINVOICENO")).trim();
			JSONObject jsonObjectResult = new JSONObject();
			try {
				PoHdrDAO PoHdrDAO = new PoHdrDAO();
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("CustCode", SUPPLIER);
				ht.put("JobNum", SINVOICENO);
				if (PoHdrDAO.isExisit(ht)) {
					jsonObjectResult.put("status", "99");
				} else {
					jsonObjectResult.put("status", "100");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
         }
		
		if (action.equals("SUPPLIER_INVOICE_CHECK_EDIT")) {
			String SUPPLIER = StrUtils.fString(request.getParameter("SUPPLIER")).trim();
			String SINVOICENO = StrUtils.fString(request.getParameter("SINVOICENO")).trim();
			String PONO = StrUtils.fString(request.getParameter("PONO")).trim();
			JSONObject jsonObjectResult = new JSONObject();
			try {
				PoHdrDAO PoHdrDAO = new PoHdrDAO();
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put("CustCode", SUPPLIER);
				ht.put("JobNum", SINVOICENO);
				String query = "SELECT COUNT(*) FROM ["+plant+"_POHDR] WHERE  CUSTCODE = '"+SUPPLIER+"' AND JOBNUM = '"+SINVOICENO+"' AND PLANT = '"+plant+"' AND PONO !='"+PONO+"'";
				if (PoHdrDAO.isExisit(query)) {
					jsonObjectResult.put("status", "99");
				} else {
					jsonObjectResult.put("status", "100");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResult.toString());
            response.getWriter().flush();
            response.getWriter().close();
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
		PoHdrDAO poHdrDAO = new PoHdrDAO();
		PoDetDAO poDetDAO = new PoDetDAO();
		if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			request.getSession().invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect("../login");
			return;
		}

		if (action.equalsIgnoreCase("Auto-Generate")) {
			String pono = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				pono = _TblControlDAO.getNextOrder(plant, username, IConstants.INBOUND);
				json.put("PONO", pono);
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
		
		if (action.equalsIgnoreCase("CheckOrderno")) {
			JSONObject jsonObjectResult = new JSONObject();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
			try {
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.PONO, orderno);
				if (new PoHdrDAO().isExisit(ht)) {
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

		if (action.equalsIgnoreCase("summarydetails")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/OrderSummaryInbound.jsp?action=OrderSummaryInbound.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("summarydetailswithcost")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/printSupplierPOInvoice.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("closeorder")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/closeByOrder.jsp?action=closeByOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equalsIgnoreCase("importpurchaseorder")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importInboundOrderExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (action.equalsIgnoreCase("importpurchaseproductremarks")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/importInboundProductRemarksExcelSheet.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (action.equalsIgnoreCase("new")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createPurchaseOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("new1")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/NewcreatePurchaseOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("edit")) {
			try {
				String pono = StrUtils.fString(request.getParameter("pono"));
				request.setAttribute("PONO", pono);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPurchaseOrder.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/* purchasereports start */
		
if(action.equalsIgnoreCase("ibsumryrecv")) {
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/ibsumryrecv.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("ibsumryrecvWCost")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ibsumryrecvWCost.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("recivedordershistory")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReceivedOrdersHistory.jsp?action=ReceivedOrdersHistory.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("recivedorderdetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReceivedOrderDetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("printpdf")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printPO.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("printpdfdetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintPODetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("printpdfcost")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/printPOInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("printpdfcostdetails")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/PrintPODetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("inboundrecvsummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/InboundRecvSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("goodsreceiptsummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/GoodsReceiptSummaryWithCost.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
if(action.equalsIgnoreCase("suppliersummary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/supplierSummaryReport.jsp?action=supplierSummaryReport.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		
		/* purchasereports end */

		if (action.equalsIgnoreCase("convertToPurchase")) {
			try {
				String pono = StrUtils.fString(request.getParameter("pono"));
				request.setAttribute("PONO", pono);
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PoConvertToPurchase.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("convertToSales")) {
			String pono = StrUtils.fString(request.getParameter("pono"));
			request.setAttribute("PONO", pono);

			String imagePath = "", numberOfDecimal = "";
			/*
			 * PoHdrDAO poHdrDAO= new PoHdrDAO(); PoDetDAO poDetDAO = new PoDetDAO();
			 */
//			Map plntMap = new HashMap();
//			Map doHdrDetails = new HashMap();
//			ArrayList custDetails = new ArrayList();
//			ArrayList shippingCustDetails = new ArrayList();
			List<PoDet> poDetList = new ArrayList<PoDet>();
			String msg = "", newDono = "", deldate = "", collectionTime = "";

			try {
				numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				PoHdr poHdr = poHdrDAO.getPoHdrByPono(plant, pono);
				poDetList = poDetDAO.getPoDetByPono(plant, pono);
				String gst = new selectBean().getGST("SALES", plant);
				poHdr.setINBOUND_GST(Double.valueOf(gst));
				deldate = DateUtils.getDate();
				collectionTime = DateUtils.getTimeHHmm();

				newDono = new TblControlDAO().getNextOrder(plant, username, IConstants.OUTBOUND);
				request.setAttribute("DONO", newDono);
				request.setAttribute("JOBNO", pono);

				ArrayList taxTreatmentList = new MasterUtil().getTaxTreatmentList("", plant, "");
				ArrayList slList = new MasterUtil().getSalesLocationList("", plant, "");
				ArrayList ccList = new MasterUtil().getCountryList("", plant, region);
				ArrayList bankList = new MasterUtil().getBankList("", plant);
				ArrayList itemList = new ArrayList();

				/* Item Details */
				for (PoDet podet : poDetList) {
					String OBDiscount = new DOUtil().getOBDiscountSelectedItemByCustomer(plant, "", podet.getITEM(),
							"OUTBOUND");

					String discounttype = "";
					OBDiscount = (OBDiscount.length() == 0) ? "0.0" : OBDiscount;
					int plusIndex = OBDiscount.indexOf("%");
					if (plusIndex != -1) {
						OBDiscount = OBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}

					Map itemMap = new HashMap();
					itemMap.put("ConvertedUnitCost",
							new DOUtil().getConvertedUnitCostForProduct(plant, "", podet.getITEM()));
					String convertedcost = new DOUtil().getConvertedAverageUnitCostForProductByCurrency(plant,podet.getUNITMO(),podet.getITEM()); //imthi add to show Sales Price By Average Cost
					itemMap.put("ConvertedUnitCostWTC",
							new DOUtil().getConvertedUnitCostForProductWTC(plant, "", podet.getITEM()));
					itemMap.put("minSellingConvertedUnitCost", new DOUtil()
							.getminSellingConvertedUnitCostForProductByCurrency(plant, "", podet.getITEM()));
					itemMap.put("outgoingOBDiscount", OBDiscount);

					Hashtable ht = new Hashtable();
					ht.put("item", podet.getITEM());
					ht.put("plant", plant);
					Map m = new EstDetDAO().getEstQtyByProduct(ht);
					itemMap.put("EstQty", (String) m.get("ESTQTY"));

					m = new InvMstDAO().getAvailableQtyByProduct(ht);
					itemMap.put("AvlbQty", (String) m.get("AVLBQTY"));

//					List listItem = new ItemUtil().queryItemMstDetails(podet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
//					if (arrItem.size() > 0) {
						
			            Map arrItem = new ItemMstUtil().GetProductForPurchase(podet.getITEM(),plant);
			            if(arrItem.size()>0){
						itemMap.put("sItemDesc",StrUtils.fString((String)arrItem.get("ITEMDESC")));
						itemMap.put("stkqty",StrUtils.fString((String)arrItem.get("STKQTY")));
						itemMap.put("price",StrUtils.fString((String)arrItem.get("UNITPRICE")));
						itemMap.put("maxstkqty",StrUtils.fString((String)arrItem.get("MAXSTKQTY")));
						itemMap.put("stockonhand",StrUtils.fString((String)arrItem.get("STOCKONHAND")));
						itemMap.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						itemMap.put("minsprice",StrUtils.fString((String)arrItem.get("MINSPRICE")));
						itemMap.put("catalogpath",StrUtils.fString((String)arrItem.get("CATLOGPATH")));
						itemMap.put("incpriceunit",StrUtils.fString((String)arrItem.get("INCPRICEUNIT")));
						itemMap.put("incprice",StrUtils.fString((String)arrItem.get("INCPRICE")));
						itemMap.put("uCOST",StrUtils.fString((String)arrItem.get("COST")));
						itemMap.put("SALESUOM",StrUtils.fString((String)arrItem.get("SALESUOM")));
						String aodtype =StrUtils.fString((String)arrItem.get("INCPRICEUNIT"));
						double adodcost =Double.valueOf((String)arrItem.get("INCPRICE"));
						double uCost =Double.valueOf((String)arrItem.get("COST"));
						double con;
						 if(!aodtype.equalsIgnoreCase("%")) {
							 con = (uCost+adodcost);
						 }else {
							 con = ((uCost*adodcost)/100);
							 con = (uCost+con);
						 }
						 itemMap.put("AODCOST",Double.valueOf(con));
						 if(!aodtype.equalsIgnoreCase("%")) {
							 con = (Double.valueOf(convertedcost)+adodcost);
						 }else {
							 con = ((Double.valueOf(convertedcost)*adodcost)/100);
							 con = (Double.valueOf(convertedcost)+con);
						 }
						 if(!aodtype.equalsIgnoreCase("%"))
							 aodtype = poHdr.getCURRENCYID();
						 itemMap.put("AVGAODCOST",Double.valueOf(con));
						 itemMap.put("AODTYPE",aodtype);
						
						itemMap.put("OBDiscountType", discounttype);
						itemMap.put("convertedcost", convertedcost);
						
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

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/PoConvertToSales.jsp");
				request.setAttribute("PoHdr", poHdr);
				request.setAttribute("PoDetList", poDetList);
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
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}

		if (action.equalsIgnoreCase("detail")) {
			try {
				String pono = StrUtils.fString(request.getParameter("pono"));
				request.setAttribute("PONO", pono);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/purchaseOrderDetail.jsp");
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
				String approve_status = StrUtils.fString(request.getParameter("APPROVE_STATUS"));
				String fdate = "", tdate = "";
//				DoHDRService doHDRService = new DoHdrServiceImpl();
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				Hashtable ht = new Hashtable();
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					String astatus="";
					if(StrUtils.fString(cname).length() > 0)	ht.put("CustName", cname);
					if(StrUtils.fString(orderno).length() > 0)	ht.put("PONO", orderno);
					if(StrUtils.fString(reference).length() > 0)	ht.put("JOBNUM", reference);
					if(StrUtils.fString(orderType).length() > 0)	ht.put("ORDERTYPE", orderType);
					if(approve_status.equalsIgnoreCase("NEW_WAITING FOR OWNER APPROVAL")) {
						if(StrUtils.fString(approve_status).length() > 0)	ht.put("ISNULL(APPROVAL_STATUS, 'CREATE APPROVAL PENDING')", "CREATE APPROVAL PENDING");
					} else if(approve_status.equalsIgnoreCase("EDIT_WAITING FOR OWNER APPROVAL")) {
						if(StrUtils.fString(approve_status).length() > 0)	ht.put("ISNULL(APPROVAL_STATUS, 'EDIT APPROVAL PENDING')", "EDIT APPROVAL PENDING");
					} else if(approve_status.equalsIgnoreCase("OWNER APPROVED")) {
						//if(StrUtils.fString(approve_status).length() > 0)	ht.put("ISNULL(APPROVAL_STATUS, 'CREATE APPROVED')", "CREATE APPROVED");
							astatus = " AND (APPROVAL_STATUS='CREATE APPROVED' OR APPROVAL_STATUS='EDIT APPROVED') ";
					} else if(approve_status.equalsIgnoreCase("OWNER REJECTED")) {
						//if(StrUtils.fString(approve_status).length() > 0)	ht.put("ISNULL(APPROVAL_STATUS, 'CREATE REJECTED')", "CREATE REJECTED");
						astatus = " AND (APPROVAL_STATUS='CREATE REJECTED' OR APPROVAL_STATUS='EDIT REJECTED') ";
					}else {
					if(StrUtils.fString(approve_status).length() > 0)	ht.put("APPROVAL_STATUS", approve_status);
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
					else
						if(StrUtils.fString(status).length() > 0)	ht.put("ISNULL(ORDER_STATUS, 'Open')", status);*/
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
					List<PoHdr> poHeaders = poHdrDAO.getPoHdrSummaryByApprove(ht, fdate, tdate, astatus);
					if (poHeaders.size() > 0) {

						for (PoHdr poHdr : poHeaders) {
							JSONObject json = new JSONObject();
							List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, poHdr.getPONO());
							FinCountryTaxType fintaxtype = finCountryTaxTypeDAO
									.getCountryTaxTypesByid(poHdr.getTAXID());
							double totalAmount = 0;
							double subtotal = 0;
							for (PoDet poDet : podetail) {
								if (poDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
									double ucost = poDet.getUNITCOST() * poDet.getQTYOR().doubleValue();
									double discount = (ucost / 100) * poDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								} else {
									double ucost = poDet.getUNITCOST() * poDet.getQTYOR().doubleValue();
									double discount = poDet.getDISCOUNT();
									subtotal = subtotal + (ucost - discount);
								}
							}

							if (poHdr.getISTAXINCLUSIVE() == 1) {
								subtotal = (subtotal * 100) / (poHdr.getINBOUND_GST() + 100);
							}

							double dorderdiscountcost = 0;
							if (poHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
								dorderdiscountcost = (subtotal / 100) * poHdr.getORDERDISCOUNT();
							} else {
								dorderdiscountcost = poHdr.getORDERDISCOUNT();
							}
							double totax = 0;
							if (poHdr.getTAXID() != 0) {
								if (fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1) {

									totax = (subtotal / 100) * poHdr.getINBOUND_GST();

									if (poHdr.getISSHIPPINGTAX() == 1) {
										totax = totax + ((poHdr.getSHIPPINGCOST() / 100) * poHdr.getINBOUND_GST());
									}

									if (poHdr.getISDISCOUNTTAX() == 1) {
										totax = totax - ((dorderdiscountcost / 100) * poHdr.getINBOUND_GST());
									}
								}
							}

							totalAmount = (subtotal + poHdr.getSHIPPINGCOST() + totax + poHdr.getADJUSTMENT())
									- dorderdiscountcost;

							// totalAmount = totalAmount + poHdr.getSHIPPINGCOST() + poHdr.getADJUSTMENT() -
							// ((subtotal/100)*poHdr.getORDERDISCOUNT());

							json.put("DATE", poHdr.getCollectionDate());
							json.put("PONO", poHdr.getPONO());
							json.put("SUPPLIER", poHdr.getCustName());
							/*if(poHdr.getSTATUS().equalsIgnoreCase("C"))
								json.put("STATUS", "PROCESSED");
							else*/ if(poHdr.getSTATUS().equalsIgnoreCase("O"))
								json.put("STATUS", "PARTIALLY PROCESSED");
							else
								json.put("STATUS", poHdr.getORDER_STATUS());
							String APPROVAL_STATUS=poHdr.getAPPROVAL_STATUS();
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
							json.put("APPROVE_STATUS", APPROVAL_STATUS);
							json.put("COLLECTION_DATE", poHdr.getCollectionDate());
							json.put("SUPPLIER_INVOICE", poHdr.getJobNum());
							json.put("AMOUNT", poHdr.getCURRENCYID() + "" + Numbers.toMillionFormat(totalAmount, numberOfDecimal));
							//json.put("EXCHAGERATE", Numbers.toMillionFormat(poHdr.getCURRENCYUSEQT(), numberOfDecimal));
							json.put("EXCHAGERATE", poHdr.getCURRENCYUSEQT());
							json.put("CAMOUNT",
									Numbers.toMillionFormat((totalAmount / poHdr.getCURRENCYUSEQT()), numberOfDecimal));
							int open=0;
							int partpaid=0;
							int paid=0;
							
							List billhdrlist = new BillDAO().getBillHdrByDono(plant, poHdr.getPONO());
							for(int j =0; j < billhdrlist.size(); j++) {
									Map billhdr=(Map)billhdrlist.get(j);
									String bstatus = (String) billhdr.get("BILL_STATUS");
									if(bstatus.equalsIgnoreCase("Open")) {
										open=1;
									}else if(bstatus.equalsIgnoreCase("Paid")){
										paid=1;
									}else if(bstatus.equalsIgnoreCase("Partially Paid")){
										partpaid=1;
									}else {
										
									}
								}
							
							
							if(open == 0 && partpaid == 0 && paid == 0) {
								json.put("PAY_STATUS", "0");
							}else if(open == 1 && partpaid == 0 && paid == 0) {
								json.put("PAY_STATUS", "0");
							}else if(open == 0 && partpaid == 0 && paid == 1) {
								json.put("PAY_STATUS", "2");
							}else {
								json.put("PAY_STATUS", "1");
							}
							
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

				RequestDispatcher rd = request.getRequestDispatcher("/jsp/purchaseOrderSummary.jsp");
				rd.forward(request, response);
			}
		}

		if (action.equalsIgnoreCase("getPurchaseOrderRemarks")) {
			String pono = "", polnno = "", item = "";
			JSONArray jsonArray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			try {
				pono = StrUtils.fString(request.getParameter("PONO"));
				polnno = StrUtils.fString(request.getParameter("POLNO"));
				item = StrUtils.fString(request.getParameter("ITEM"));

				List<PoDetRemarks> poDetRemarkslist = poDetDAO.GetPoDetRemarksbyitems(plant, pono, polnno, item);
				if (poDetRemarkslist.size() > 0) {
					for (PoDetRemarks poDetRemarks : poDetRemarkslist) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("remarks", poDetRemarks.getREMARKS());
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
		
		if (action.equalsIgnoreCase("Get-Barcode")) {
			String barcode = "";
			Hashtable htTblCnt = new Hashtable();
			TblControlDAO _TblControlDAO = new TblControlDAO();
			DateUtils _DateUtils = new DateUtils();
			String date = _DateUtils.getGeneralDate("MMyy");
			JSONObject json = new JSONObject();
			try {
				//barcode = _TblControlDAO.getNextOrder(plant,username,IConstants.BARCODE);
				String prefix = "10" + date;
				String orderPrefix = "10";
				htTblCnt.put(IDBConstants.PLANT, plant);
				htTblCnt.put(IDBConstants.TBL_FUNCTION, IConstants.BARCODE);
				htTblCnt.put(IDBConstants.TBL_PREFIX1, prefix);
				htTblCnt.put(IDBConstants.TBL_MIN_SEQ, "0000000");
				htTblCnt.put(IDBConstants.TBL_MAX_SEQ, "9999999");
				htTblCnt.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_INITIAL_NEX_SEQ);
				htTblCnt.put(IDBConstants.CREATED_BY, username);
				htTblCnt.put(IDBConstants.CREATED_AT, (String) _DateUtils.getDateTime());
				String nextSeqno =  _TblControlDAO.getnextSeqNum(htTblCnt);
				//nextSeqno = String.format("%07d", Integer.parseInt(nextSeqno) + 1);
				//nextSeqno = orderPrefix + _DateUtils.getGeneralDate("MMyy") + nextSeqno;
				
				json.put("orderPrefix", orderPrefix);
				json.put("generalDate", _DateUtils.getGeneralDate("MMyy"));
				json.put("nextSeqno", nextSeqno);
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

	}

	private JSONObject getEditPODetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		PoDetDAO poDetDAO = new PoDetDAO();
		PoHdrDAO poHdrDAO = new PoHdrDAO();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemMstUtil itemUtil = new ItemMstUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String pono = StrUtils.fString(request.getParameter("PONO")).trim();

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, pono);
			PoHdr poheader = poHdrDAO.getPoHdrByPono(plant, pono);
			if (podetail.size() > 0) {
				for (PoDet poDet : podetail) {
					/*
					 * StrUtils.addZeroes(dCost, numberOfDecimal); resultJsonInt.put("POLNNO",
					 * (String)m.get("LNNO"));
					 */
					JSONObject resultJsonInt = new JSONObject();


					resultJsonInt.put("LNNO", poDet.getPOLNNO());
					resultJsonInt.put("LNSTATUS", poDet.getLNSTAT());
					resultJsonInt.put("QTYRC", StrUtils.addZeroes(poDet.getQTYRC().doubleValue(), "3"));
					resultJsonInt.put("ITEM", poDet.getITEM());
					resultJsonInt.put("ITEMDESC", poDet.getItemDesc());
					resultJsonInt.put("ACCOUNTNAME", poDet.getACCOUNT_NAME());
					resultJsonInt.put("UOM", poDet.getUNITMO());
					resultJsonInt.put("QTY", StrUtils.addZeroes(poDet.getQTYOR().doubleValue(), "3"));
					resultJsonInt.put("PDELDATE", poDet.getPRODUCTDELIVERYDATE());
					resultJsonInt.put("UNITCOST", StrUtils.addZeroes(poDet.getUNITCOST(), numberOfDecimal));
					resultJsonInt.put("ITEMDISCOUNTTYPE", poDet.getDISCOUNT_TYPE());
					resultJsonInt.put("TAXTYPE", poDet.getTAX_TYPE());

					if (poDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(poDet.getDISCOUNT(), "3"));
						double amount = (poDet.getUNITCOST() * (poDet.getQTYOR().doubleValue()))
								- (((poDet.getUNITCOST() * (poDet.getQTYOR().doubleValue())) / 100)
										* poDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					} else {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(poDet.getDISCOUNT(), numberOfDecimal));
						double amount = (poDet.getUNITCOST() * (poDet.getQTYOR().doubleValue()))
								- (poDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}
					String IBDiscount = new POUtil().getIBDiscountSelectedItemVNO(plant, poheader.getCustCode(),
							poDet.getITEM());
					String discounttype = "";

					int plusIndex = IBDiscount.indexOf("%");
					if (plusIndex != -1) {
						IBDiscount = IBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}
					if(IBDiscount.equalsIgnoreCase(""))
						IBDiscount="0.00";
					
					//IMTI modified on 17-03-2022 To display aval.qty in hover
					String estQty = "", avlbQty = "";
                    Hashtable hts = new Hashtable();
             	 	hts.put("item", poDet.getITEM());
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
             	 	
//					List listItem = itemUtil.queryItemMstDetailsforpurchase(poDet.getITEM(), plant);
//					Vector arrItem = (Vector) listItem.get(0);
//					if (arrItem.size() > 0) {
						
//						List listItem = itemUtil.queryItemMstDetailsforpurchase(poDet.getITEM(), plant);
//						Vector arrItem = (Vector) listItem.get(0);
//						if (arrItem.size() > 0) {
//						String catlogpath = itemMstDAO.getcatlogpath(plant, poDet.getITEM());
             	 	
				Map arrItem = itemUtil.GetProductForPurchase(poDet.getITEM(),plant);
				if(arrItem.size()>0){
				String catlogpath = StrUtils.fString((String) arrItem.get("CATLOGPATH"));
				String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png": "/track/ReadFileServlet/?fileLocation=" + catlogpath);
						//ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,StrUtils.fString((String)arrItem.get(41)));
						ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,"0");
						avlbQty= (String) ma.get("AVLBQTY");
//						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get(8)));
//						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
//						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
//						resultJsonInt.put("outgoingqty",StrUtils.fString((String)arrItem.get(23)));
//						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get(41)));
						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get("STKQTY")));
						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get("MAXSTKQTY")));
						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get("STOCKONHAND")));
						resultJsonInt.put("outgoingqty",StrUtils.fString((String)arrItem.get("OUTGOINGQTY")));
						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get("INCOMINGQTY")));
						resultJsonInt.put("CATLOGPATH", cpath);
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
