package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.MultiPoEstHdrDAO; //Resvi
import com.track.dao.PlantMstDAO;
import com.track.dao.PltApprovalMatrixDAO;
import com.track.dao.PoDetApprovalDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoEstDetDAO;
import com.track.dao.PoEstHdrDAO;
import com.track.dao.PoHdrAprrovalDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.PurchaseEstimateAttachDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.VendMstDAO;
import com.track.dao.multiPurchaseEstAttachDAO;//Resvi
import com.track.dao.multiPoEstDetDAO; //Resvi
import com.track.db.object.MultiPoEstHdr; //Resvi
import com.track.db.object.PoDet;
import com.track.db.object.PoDetApproval;
import com.track.db.object.PoDetRemarks;
import com.track.db.object.PoEstDet;
import com.track.db.object.PoEstHdr;
import com.track.db.object.PoHdr;
import com.track.db.object.PoHdrApproval;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.MultiPoEstDet; //Resvi
import com.track.db.object.MultiPoEstDetRemarks; //Resvi
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.FieldCopier;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MultiPoUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PoEstUtil;
import com.track.db.util.TblControlUtil;

import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/purchaseestimate/*")
public class MultiPurchaseEstimateServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String[] pathInfo = request.getPathInfo().split("/");
//		String action = pathInfo[1];
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		if ("".equals(action) && request.getPathInfo() != null) {
			String[] pathInfo = request.getPathInfo().split("/");
			action = pathInfo[1];
		}
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
		multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();
		multiPurchaseEstAttachDAO MultiPurchaseEstAttachDAO = new multiPurchaseEstAttachDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		MasterDAO _MasterDAO = new MasterDAO();

		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		JSONObject jsonObjectResults = new JSONObject();
		if (action.equals("LOAD_MULTI_PRODUCT_LIST_BY_ITEM")) {
			jsonObjectResults = this.getMultiPoProductListbyProduct(request,"");
			response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonObjectResults.toString());
            response.getWriter().flush();
            response.getWriter().close();
		 }
		
		if (action.equalsIgnoreCase("save")|| action.equalsIgnoreCase("copy")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String empName="",empno = "";
			List<Hashtable<String,String>> multipurchaseAttachmentList = null;
			List<Hashtable<String,String>> multipurchaseAttachmentInfoList = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				
								
				PoEstHdrDAO poEstHdrDAO= new PoEstHdrDAO();
				PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
				PoHdrDAO poHdrDAO= new PoHdrDAO();
				PoDetDAO poDetDAO = new PoDetDAO();
				PurchaseEstimateAttachDAO purchaseEstimateAttachDAO = new PurchaseEstimateAttachDAO();
				MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
				MultiPoEstDet multiPoEstDet = new MultiPoEstDet();
				 String ISAUTO_CONVERT_ESTPO =new PlantMstDAO().getPEST_PO(plant);//Check Purchase Estimate To Purchase Order Automatic Conversion

				List POMULTIESTLNNO = new ArrayList(), item = new ArrayList(),custname = new ArrayList(), custcode = new ArrayList(), currencyid = new ArrayList(), 
						taxtreatment = new ArrayList(), currencyuseqt = new ArrayList(), unitcost = new ArrayList(),estqty = new ArrayList(), qtyrc = new ArrayList(),
						qtyor = new ArrayList(), unitmo = new ArrayList(), pddate = new ArrayList(),
						discount = new ArrayList(), dtype = new ArrayList(), accname = new ArrayList(),
						taxtype = new ArrayList(), chkdPOMULTIESTNO = new ArrayList(),expiredate = new ArrayList();
				int POMULTIESTNOcount = 0,chkdPOMULTIESTNOCount = 0,expiredateCount=0;
				String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
						shipworkphone="",shipcountry="",shiphpno="",shipemail="";
				multipurchaseAttachmentList = new ArrayList<Hashtable<String, String>>();
				multipurchaseAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					String revgersecharge = "0";
					String goodsimport = "0";
					String selectesp = "";
					String isProductAssignedSupplier= "";
					ut = DbBean.getUserTranaction();
					ut.begin();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* PO_MULTI_ESTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								multiPoEstHdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("POMULTIESTNO")) {
								multiPoEstHdr.setPOMULTIESTNO(StrUtils.fString(fileItem.getString()).trim());
							}
							
							
							
							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								multiPoEstHdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								multiPoEstHdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								multiPoEstHdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								multiPoEstHdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								multiPoEstHdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								multiPoEstHdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								multiPoEstHdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
														
														
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								multiPoEstHdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								multiPoEstHdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								multiPoEstHdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								multiPoEstHdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}
							
	                        if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								
								empno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empName = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								multiPoEstHdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								multiPoEstHdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								multiPoEstHdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim()));
							}
							
							
							
							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								multiPoEstHdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								multiPoEstHdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
								
								if (!multiPoEstHdr.getORDER_STATUS().equalsIgnoreCase("Draft")) {
								if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
								multiPoEstHdr.setORDER_STATUS("PROCESSED");
								}
							}
							
							
							
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								multiPoEstHdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								multiPoEstHdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								multiPoEstHdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
														
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							/* PODET */
							
							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								POMULTIESTLNNO.add(StrUtils.fString(fileItem.getString()).trim());
								POMULTIESTNOcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								custname.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								custcode.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid.add(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxtreatment.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ESTQTY")) {
								estqty.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("selectesp")) {
								selectesp = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("isProductAssignedSupplier")) {
								isProductAssignedSupplier = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
						
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTYRC")) {
								qtyrc.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
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
							
//							if (fileItem.getFieldName().equalsIgnoreCase("PRODUCTDELIVERYDATE")) {
//								pddate.add(StrUtils.fString(fileItem.getString()).trim());
//								pddatecount++;
//							}
							
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("chkdPOMULTIESTNO")) {
								chkdPOMULTIESTNO.add(StrUtils.fString(fileItem.getString()).trim());
								chkdPOMULTIESTNOCount++;
							}						
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								expiredate.add(StrUtils.fString(fileItem.getString()).trim());
								expiredateCount++;
							}
							
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							
							//attachment

							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/"+ multiPoEstHdr.getPOMULTIESTNO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"+ multiPoEstHdr.getPOMULTIESTNO();
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
								Hashtable multipurchaseAttachment = new Hashtable<String, String>();
								multipurchaseAttachment.put("PLANT", plant);
								multipurchaseAttachment.put("FILETYPE", fileItem.getContentType());
								multipurchaseAttachment.put("FILENAME", fileName);
								multipurchaseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								multipurchaseAttachment.put("FILEPATH", fileLocationATT);
								multipurchaseAttachment.put("CRAT",DateUtils.getDateTime());
								multipurchaseAttachment.put("CRBY",username);
								multipurchaseAttachment.put("UPAT",DateUtils.getDateTime());
								multipurchaseAttachmentList.add(multipurchaseAttachment);
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
					multiPoEstHdr.setEMPNO(empno);
					if(multiPoEstHdr.getDELIVERYDATEFORMAT() == null){
						multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					
					
					
					multiPoEstHdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					multiPoEstHdr.setGOODSIMPORT(Short.valueOf(goodsimport));
					multiPoEstHdr.setSTATUS("N");
					short one = 1;
					short zero = 0;
					if(selectesp.equals("1")) {
						multiPoEstHdr.setSAMEASEXPDATE(Short.valueOf(one));
					}else {
						multiPoEstHdr.setSAMEASEXPDATE(Short.valueOf(zero));
					}
					if(isProductAssignedSupplier.equals("1")) {
						multiPoEstHdr.setISPRODUCTASSIGNEDSUPPLIER(Short.valueOf(one));
					}else {
						multiPoEstHdr.setISPRODUCTASSIGNEDSUPPLIER(Short.valueOf(zero));
					}
					if (!multiPoEstHdr.getORDER_STATUS().equalsIgnoreCase("Draft")) {
					if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
					multiPoEstHdr.setSTATUS("Confirm");					 
					}
					
					multiPoEstHdr.setLOCALEXPENSES(0.0);
					multiPoEstHdr.setCRBY(username);
					multiPoEstHdr.setCRAT(DateUtils.getDateTime());
					boolean issadded = multiPoEstHdrDAO.addMultiPoEstHdr(multiPoEstHdr);

					if(issadded) {
						List< MultiPoEstDet> multiPoEstDetlist = new ArrayList<MultiPoEstDet>();
						double totalDiscountAmount = 0;
						for(int i=0;i<POMULTIESTNOcount;i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String)POMULTIESTLNNO.get(i)) -1;
							String disctype=(String)dtype.get(index);
								if(disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String)unitcost.get(index))/Double.valueOf((String) currencyuseqt.get(index)))*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									double amount = (Double.valueOf((String)unitcost.get(index))/Double.valueOf((String) currencyuseqt.get(index)))*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/Double.valueOf((String) currencyuseqt.get(index));
								}	
								totalDiscountAmount+=unitCost_AD;
						}
						
						if(action.equalsIgnoreCase("copy")) {
							for(int i=0;i<chkdPOMULTIESTNOCount;i++) {
								int index = Integer.parseInt((String)chkdPOMULTIESTNO.get(i));
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i+1;
								String disctype=(String)dtype.get(index);
								MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
								multiPoEstdet.setPLANT(plant);
								multiPoEstdet.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
								multiPoEstdet.setTRANDATE(multiPoEstHdr.getCollectionDate());
								multiPoEstdet.setPOMULTIESTLNNO(lnno);
								multiPoEstdet.setITEM((String)item.get(index));
								multiPoEstdet.setCustName((String) custname.get(index));
								multiPoEstdet.setCustCode((String) custcode.get(index));
								multiPoEstdet.setCURRENCYID((String) currencyid.get(index));
								multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String) currencyuseqt.get(index)));
								multiPoEstdet.setTAXTREATMENT((String) taxtreatment.get(index));
								multiPoEstdet.setUNITCOST((Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT()));
								multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								multiPoEstdet.setUNITMO((String)unitmo.get(index));
								//multiPoEstdet.setPRODUCTDELIVERYDATE((String)pddate.get(index));
								multiPoEstdet.setEXPIREDATE((String) expiredate.get(index));
								if(disctype.equalsIgnoreCase("%")) {
									multiPoEstdet.setDISCOUNT(Double.valueOf((String)discount.get(index)));
									double amount = (Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									multiPoEstdet.setDISCOUNT((Double.valueOf((String)discount.get(index))/multiPoEstdet.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/multiPoEstdet.getCURRENCYUSEQT();
								}
								
								
								if(Double.isNaN(unitCost_AOD)) {
									multiPoEstdet.setUNITCOST_AOD(0.0);
								}else {
									multiPoEstdet.setUNITCOST_AOD(unitCost_AOD);
								}
								multiPoEstdet.setDISCOUNT_TYPE((String)dtype.get(index));
								multiPoEstdet.setACCOUNT_NAME((String)accname.get(index));
								multiPoEstdet.setTAX_TYPE((String)taxtype.get(index));
								multiPoEstdet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
								multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
								multiPoEstdet.setCURRENCYUSEQT(multiPoEstdet.getCURRENCYUSEQT());
								multiPoEstdet.setPRODGST(0);
								multiPoEstdet.setCOMMENT1((String) custcode.get(index)+"-"+(String) currencyid.get(index)+"-"+(String) currencyuseqt.get(index)+"-"+(String) expiredate.get(index));
								
								if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
								{
								multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								multiPoEstdet.setLNSTAT("C");
								} else {
								multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
								multiPoEstdet.setLNSTAT("N");
								}
								multiPoEstdet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								multiPoEstdet.setCRBY(username);
								multiPoEstdet.setCRAT(DateUtils.getDateTime());
								multiPoEstDetlist.add(multiPoEstdet);
								multiPoEstdet.setCOMMENT1("");
								MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstdet);
							}
							
						} else {
							for (int i = 0; i < POMULTIESTNOcount; i++) {
						int index = Integer.parseInt((String) POMULTIESTLNNO.get(i)) - 1;
						double unitCost_AD = 0d, unitCost_AOD = 0d;
						int lnno = i + 1;
						String disctype = (String) dtype.get(index);
						MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
						multiPoEstdet.setPLANT(plant);
						multiPoEstdet.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
						multiPoEstdet.setTRANDATE(multiPoEstHdr.getCollectionDate());
						multiPoEstdet.setPOMULTIESTLNNO(lnno);
						multiPoEstdet.setITEM((String) item.get(index));
						multiPoEstdet.setCustName((String) custname.get(index));
						multiPoEstdet.setCustCode((String) custcode.get(index));
						multiPoEstdet.setCURRENCYID((String) currencyid.get(index));
						multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String) currencyuseqt.get(index)));
						multiPoEstdet.setTAXTREATMENT((String) taxtreatment.get(index));
						multiPoEstdet.setUNITCOST(
								(Double.valueOf((String) unitcost.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
						multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
						multiPoEstdet.setUNITMO((String) unitmo.get(index));
						//multiPoEstdet.setPRODUCTDELIVERYDATE((String) pddate.get(index));
						multiPoEstdet.setEXPIREDATE((String) expiredate.get(index));
						if (disctype.equalsIgnoreCase("%")) {
							multiPoEstdet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
							double amount = (Double.valueOf((String) unitcost.get(index))
									/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
							unitCost_AD = (amount)
									- (amount * (Double.valueOf((String) discount.get(index)) / 100));
						} else {
							multiPoEstdet.setDISCOUNT(
									(Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
							double amount = (Double.valueOf((String) unitcost.get(index))
									/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
							unitCost_AD = (amount)
									- Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT();
						}

						
						
						if(Double.isNaN(unitCost_AOD)) {
							multiPoEstdet.setUNITCOST_AOD(0.0);
						}else {
							multiPoEstdet.setUNITCOST_AOD(unitCost_AOD);
						}
						multiPoEstdet.setDISCOUNT_TYPE((String) dtype.get(index));
						multiPoEstdet.setACCOUNT_NAME((String) accname.get(index));
						multiPoEstdet.setTAX_TYPE((String) taxtype.get(index));
						multiPoEstdet.setUSERFLD1(StrUtils
								.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
						multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
						multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
						multiPoEstdet.setCURRENCYUSEQT(multiPoEstdet.getCURRENCYUSEQT());
						multiPoEstdet.setPRODGST(0);
						multiPoEstdet.setCOMMENT1((String) custcode.get(index)+"-"+(String) currencyid.get(index)+"-"+(String) currencyuseqt.get(index)+"-"+(String) expiredate.get(index));
						if (!multiPoEstHdr.getORDER_STATUS().equalsIgnoreCase("Draft")) {
						if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
						{
						multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
						multiPoEstdet.setLNSTAT("C");
						} else {
						multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
						multiPoEstdet.setLNSTAT("N");
						}
						} else {
							multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
							multiPoEstdet.setLNSTAT("N");
							}
						multiPoEstdet.setItemDesc(StrUtils
								.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
						multiPoEstdet.setCRBY(username);
						multiPoEstdet.setCRAT(DateUtils.getDateTime());
						multiPoEstDetlist.add(multiPoEstdet);
						multiPoEstdet.setCOMMENT1("");
						MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstdet);
					}
				
						}
						
						
						int attchSize = multipurchaseAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable multipurchaseAttachment = new Hashtable<String, String>();
								multipurchaseAttachment = multipurchaseAttachmentList.get(i);
								multipurchaseAttachment.put("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
								multipurchaseAttachmentInfoList.add(multipurchaseAttachment);
							}
							MultiPurchaseEstAttachDAO.addmultipurchaseAttachments(multipurchaseAttachmentInfoList, plant);
						}

						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.MULTIPOEST,"PM", multiPoEstHdr.getPOMULTIESTNO());


						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PM);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, multiPoEstHdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, multiPoEstHdr.getPOMULTIESTNO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1());
						if (!multiPoEstHdr.getRemark1().equals("") && !multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,multiPoEstHdr.getRemark1() + "," + multiPoEstHdr.getREMARK3());
						} else if (!multiPoEstHdr.getRemark1().equals("") && multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,multiPoEstHdr.getRemark1());
						} else if (multiPoEstHdr.getRemark1().equals("") && !multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, multiPoEstHdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS,"");
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (multiPoEstHdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1());
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
									htRecvHis.put("REMARKS", multiPoEstHdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (multiPoEstHdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, multiPoEstHdr.getREMARK3());
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
									htRecvHis.put("REMARKS", multiPoEstHdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (multiPoEstHdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, multiPoEstHdr.getINCOTERMS());

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
									htRecvHis.put("REMARKS", multiPoEstHdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (int i = 0; i < chkdPOMULTIESTNOCount; i++) {
								int index = Integer.parseInt((String) chkdPOMULTIESTNO.get(i));

								int Polno = i + 1;
								Hashtable<String, String> htRemarks = new Hashtable<>();
								htRemarks.put(IDBConstants.PLANT, plant);
								htRemarks.put(IDBConstants.PODET_POMULTIESTNUM, multiPoEstHdr.getPOMULTIESTNO());
								htRemarks.put(IDBConstants.PODET_MULTIPOLNNO, Integer.toString(Polno));
								htRemarks.put(IDBConstants.PODET_ITEM, (String) item.get(index));
								if (!new multiPoEstDetDAO().isExisitPoEstMultiRemarks(htRemarks)) {
									htRemarks.put(IDBConstants.REMARKS, "");
									htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, username);
									flag = new MultiPoUtil().savePoMultiRemarks(htRemarks);
								}
							}

							for (MultiPoEstDet porderdet :multiPoEstDetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", TransactionConstants.PM_ADD_ITEM);
								htRecvHis.put(IDBConstants.POHDR_JOB_NUM, multiPoEstHdr.getJobNum());
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.QTY, porderdet.getQTYOR());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, multiPoEstHdr.getPOMULTIESTNO());
								htRecvHis.put(IDBConstants.CREATED_BY, username);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}						

						
						//ISAUTO_CONVERT_ESTPO	
						
						if (!multiPoEstHdr.getORDER_STATUS().equalsIgnoreCase("Draft")) {
						if (flag) {
							if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
							{
								
								Map<Object, List<MultiPoEstDet>> porderdet = multiPoEstDetlist.stream().collect(Collectors.groupingBy(p -> (p.getCustCode()+"~"+p.getCURRENCYID()+"~"+p.getCURRENCYUSEQT()+"~"+p.getEXPIREDATE())));
								System.out.println(porderdet);
								if(porderdet.size()>0) {
									porderdet.forEach((CustCode, empList) -> { 
										String custval = (String) CustCode;
										String[] svsval = custval.split("~");
										String Custcode1 =  svsval[0].toString();
										String sCURRENCYID =  svsval[1].toString();
										String sCURRENCYUSEQT =  svsval[2].toString();
										String sEXPIREDATE ="";
										if(svsval.length>3)
										sEXPIREDATE=svsval[3].toString();
										System.out.println(CustCode);
										System.out.println(empList);
										String PONO="",POESTNO="",vendname="",PERSON_INCHARGE="",TELNO="",ADD1="",ADD2="",ADD3="",TRANSPORTID="0",TAXTREATMENT="",uniqueID="";											
										try {
											POESTNO = new TblControlDAO().getNextOrder(plant, username, IConstants.POEST);
											PONO = new TblControlDAO().getNextOrder(plant, username, IConstants.INBOUND);
											uniqueID = UUID.randomUUID().toString();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										JSONObject vendorJson=new VendMstDAO().getVendorName(plant, svsval[0].toString());
										if(!vendorJson.isEmpty()) {
											vendname=vendorJson.getString("VNAME");
											PERSON_INCHARGE=vendorJson.getString("NAME");
											TELNO=vendorJson.getString("TELNO");
											ADD1=vendorJson.getString("ADDR1");
											ADD2=vendorJson.getString("ADDR2");
											ADD3=vendorJson.getString("ADDR3");
											TRANSPORTID=vendorJson.getString("TRANSPORTID");
											TAXTREATMENT=vendorJson.getString("TAXTREATMENT");
										}
										PoEstHdr poEsthdr = new PoEstHdr();	
										PoHdr pohdr = new PoHdr();
										poEsthdr.setPLANT(multiPoEstHdr.getPLANT());
										pohdr.setPLANT(multiPoEstHdr.getPLANT());
										poEsthdr.setPOESTNO(POESTNO);
										pohdr.setPOESTNO(POESTNO);
										pohdr.setPONO(PONO);
										poEsthdr.setCustName(vendname);
										pohdr.setCustName(vendname);
										poEsthdr.setCustCode(Custcode1);
										pohdr.setCustCode(Custcode1);
										poEsthdr.setJobNum(multiPoEstHdr.getPOMULTIESTNO());
										poEsthdr.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
										pohdr.setJobNum(POESTNO);
										poEsthdr.setPersonInCharge(PERSON_INCHARGE);
										pohdr.setPersonInCharge(PERSON_INCHARGE);
										poEsthdr.setContactNum(TELNO);
										pohdr.setContactNum(TELNO);
										poEsthdr.setAddress(ADD1);
										pohdr.setAddress(ADD1);
										poEsthdr.setAddress2(ADD2);
										pohdr.setAddress2(ADD2);
										poEsthdr.setAddress3(ADD3);
										pohdr.setAddress3(ADD3);
										poEsthdr.setCollectionTime(multiPoEstHdr.getCollectionTime());
										pohdr.setCollectionTime(multiPoEstHdr.getCollectionTime());
										poEsthdr.setCollectionDate(multiPoEstHdr.getCollectionDate());
										pohdr.setCollectionDate(multiPoEstHdr.getCollectionDate());
										poEsthdr.setRemark1(multiPoEstHdr.getRemark1());
										pohdr.setRemark1(multiPoEstHdr.getRemark1());
										poEsthdr.setRemark2(multiPoEstHdr.getRemark2());
										pohdr.setRemark2(multiPoEstHdr.getRemark2());
										poEsthdr.setREMARK3(multiPoEstHdr.getREMARK3());
										pohdr.setREMARK3(multiPoEstHdr.getREMARK3());										
										poEsthdr.setORDERTYPE(multiPoEstHdr.getORDERTYPE());
										//pohdr.setORDERTYPE(multiPoEstHdr.getORDERTYPE());
										pohdr.setORDERTYPE("");
										poEsthdr.setINBOUND_GST(multiPoEstHdr.getINBOUND_GST());
										pohdr.setINBOUND_GST(multiPoEstHdr.getINBOUND_GST());
										poEsthdr.setCURRENCYID(sCURRENCYID);
										pohdr.setCURRENCYID(sCURRENCYID);
										poEsthdr.setDELDATE(multiPoEstHdr.getDELDATE());
										pohdr.setDELDATE(multiPoEstHdr.getDELDATE());
										poEsthdr.setSTATUS_ID(multiPoEstHdr.getSTATUS_ID());
										pohdr.setSTATUS_ID(multiPoEstHdr.getSTATUS_ID());
										poEsthdr.setSHIPPINGID(multiPoEstHdr.getSHIPPINGID());
										pohdr.setSHIPPINGID(multiPoEstHdr.getSHIPPINGID());
										poEsthdr.setEXPIREDATE(sEXPIREDATE);
										poEsthdr.setSHIPPINGCUSTOMER(multiPoEstHdr.getSHIPPINGCUSTOMER());
										pohdr.setSHIPPINGCUSTOMER(multiPoEstHdr.getSHIPPINGCUSTOMER());
										poEsthdr.setINCOTERMS(multiPoEstHdr.getINCOTERMS());
										pohdr.setINCOTERMS(multiPoEstHdr.getINCOTERMS());
										poEsthdr.setORDERDISCOUNT(Double.valueOf("0"));
										pohdr.setORDERDISCOUNT(Double.valueOf("0"));
										poEsthdr.setSHIPPINGCOST(Double.valueOf("0"));
										pohdr.setSHIPPINGCOST(Double.valueOf("0"));
										poEsthdr.setPAYMENTTYPE(multiPoEstHdr.getPAYMENTTYPE());
										pohdr.setPAYMENTTYPE(multiPoEstHdr.getPAYMENTTYPE());
										poEsthdr.setPAYMENT_TERMS(multiPoEstHdr.getPAYMENT_TERMS());
										pohdr.setPAYMENT_TERMS(multiPoEstHdr.getPAYMENT_TERMS());
										poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf(multiPoEstHdr.getDELIVERYDATEFORMAT()));
										pohdr.setDELIVERYDATEFORMAT(Short.valueOf(multiPoEstHdr.getDELIVERYDATEFORMAT()));
										poEsthdr.setTAXTREATMENT(TAXTREATMENT);
										pohdr.setTAXTREATMENT(TAXTREATMENT);
										poEsthdr.setPURCHASE_LOCATION(multiPoEstHdr.getPURCHASE_LOCATION());
										pohdr.setPURCHASE_LOCATION(multiPoEstHdr.getPURCHASE_LOCATION());
										poEsthdr.setORDER_STATUS(multiPoEstHdr.getORDER_STATUS());
										pohdr.setORDER_STATUS("Open");
										poEsthdr.setADJUSTMENT(Double.valueOf("0"));
										pohdr.setADJUSTMENT(Double.valueOf("0"));
										poEsthdr.setISDISCOUNTTAX(Short.valueOf("0"));
										pohdr.setISDISCOUNTTAX(Short.valueOf("0"));
										poEsthdr.setISSHIPPINGTAX(Short.valueOf("0"));
										pohdr.setISSHIPPINGTAX(Short.valueOf("0"));
										poEsthdr.setTAXID(multiPoEstHdr.getTAXID());
										pohdr.setTAXID(multiPoEstHdr.getTAXID());
										poEsthdr.setISTAXINCLUSIVE(multiPoEstHdr.getISTAXINCLUSIVE());
										pohdr.setISTAXINCLUSIVE(multiPoEstHdr.getISTAXINCLUSIVE());
										poEsthdr.setORDERDISCOUNTTYPE(sCURRENCYID);
										pohdr.setORDERDISCOUNTTYPE(sCURRENCYID);
										poEsthdr.setCURRENCYUSEQT(Double.valueOf(sCURRENCYUSEQT));
										pohdr.setCURRENCYUSEQT(Double.valueOf(sCURRENCYUSEQT));
										poEsthdr.setPROJECTID(multiPoEstHdr.getPROJECTID());
										pohdr.setPROJECTID(multiPoEstHdr.getPROJECTID());
										poEsthdr.setTRANSPORTID(Integer.valueOf(TRANSPORTID));
										pohdr.setTRANSPORTID(Integer.valueOf(TRANSPORTID));
										poEsthdr.setEMPNO(multiPoEstHdr.getEMPNO());
										pohdr.setEMPNO(multiPoEstHdr.getEMPNO());
										poEsthdr.setREVERSECHARGE(Short.valueOf(multiPoEstHdr.getREVERSECHARGE()));
										pohdr.setREVERSECHARGE(Short.valueOf(multiPoEstHdr.getREVERSECHARGE()));
										poEsthdr.setGOODSIMPORT(Short.valueOf(multiPoEstHdr.getGOODSIMPORT()));
										pohdr.setGOODSIMPORT(Short.valueOf(multiPoEstHdr.getGOODSIMPORT()));
										poEsthdr.setSTATUS(multiPoEstHdr.getSTATUS());
										pohdr.setSTATUS("N");
										poEsthdr.setLOCALEXPENSES(0.0);
										pohdr.setLOCALEXPENSES(0.0);
										poEsthdr.setCRBY(username);
										pohdr.setCRBY(username);
										poEsthdr.setCRAT(DateUtils.getDateTime());
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
										
										
										
										try {
											PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();
											PoHdrAprrovalDAO poHdrAprrovalDAO = new PoHdrAprrovalDAO();
											PoDetApprovalDAO poDetApprovalDAO = new PoDetApprovalDAO();
											boolean approvalcheck = pltApproval.CheckApprovalByUser(plant, "PURCHASE", "CREATE", username);
											if(approvalcheck) {
													pohdr.setORDER_STATUS("Draft");
													pohdr.setAPPROVAL_STATUS("CREATE APPROVAL PENDING");
													pohdr.setUKEY(uniqueID);
											}  else
												pohdr.setAPPROVAL_STATUS("");
											
											boolean ispoadded = poEstHdrDAO.addPoHdr(poEsthdr);
											int hdrid = poHdrDAO.addPoHdrReturnKey(pohdr);
											boolean isPOSadded =false;
											if(hdrid > 0) {
												isPOSadded = true;
											}
											
											if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
												PoHdrApproval poHdrApproval= new PoHdrApproval();
												new FieldCopier().copyFields(pohdr, poHdrApproval);
												poHdrApproval.setID(hdrid);
												poHdrAprrovalDAO.addPoHdr(poHdrApproval);
											}
											if(ispoadded && isPOSadded) {
												List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
												List<PoDet> polistdet = new ArrayList<PoDet>();
												int lnnoval = 0;
												for (MultiPoEstDet podetdet :multiPoEstDetlist) {
													if(podetdet.getCustCode().equalsIgnoreCase(Custcode1) && podetdet.getCURRENCYUSEQT() == Double.valueOf(sCURRENCYUSEQT) && podetdet.getCURRENCYID().equalsIgnoreCase(sCURRENCYID) && podetdet.getEXPIREDATE().equalsIgnoreCase(sEXPIREDATE)) {
														lnnoval=lnnoval+1;
														PoEstDet poEstDet = new PoEstDet();
														PoDet poDet = new PoDet();
												poEstDet.setPLANT(plant);
												poDet.setPLANT(plant);
												poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
												poEstDet.setPOMULTIESTNO(poEsthdr.getPOMULTIESTNO());
												poDet.setPONO(pohdr.getPONO());
												poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
												poDet.setTRANDATE(poEsthdr.getCollectionDate());
												poEstDet.setPOESTLNNO(lnnoval);
												poEstDet.setPOMULTIESTLNNO(podetdet.getPOMULTIESTLNNO());
												poDet.setPOESTNO(poEsthdr.getPOESTNO());
												poDet.setPOESTLNNO(lnnoval);
												poDet.setPOLNNO(lnnoval);
												poEstDet.setITEM(podetdet.getITEM());
												poDet.setITEM(podetdet.getITEM());
												poEstDet.setUNITCOST(podetdet.getUNITCOST());
												poDet.setUNITCOST(podetdet.getUNITCOST());
												poEstDet.setQTYOR(podetdet.getQTYOR());
												poDet.setQTYOR(podetdet.getQTYOR());
												poEstDet.setUNITMO(podetdet.getUNITMO());
												poDet.setUNITMO(podetdet.getUNITMO());
												poEstDet.setPRODUCTDELIVERYDATE("");
												poDet.setPRODUCTDELIVERYDATE("");
												poEstDet.setDISCOUNT_TYPE(sCURRENCYID);
												poDet.setDISCOUNT_TYPE(sCURRENCYID);
												poEstDet.setACCOUNT_NAME(podetdet.getACCOUNT_NAME());
												poDet.setACCOUNT_NAME(podetdet.getACCOUNT_NAME());
												poEstDet.setTAX_TYPE(podetdet.getTAX_TYPE());
												poDet.setTAX_TYPE(podetdet.getTAX_TYPE());
												poEstDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setUSERFLD2(poEsthdr.getJobNum());
												poDet.setUSERFLD2(poEsthdr.getJobNum());
												poEstDet.setUSERFLD3(poEsthdr.getCustName());
												poDet.setUSERFLD3(poEsthdr.getCustName());
												poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
												poDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
												poEstDet.setPRODGST(0);
												poDet.setPRODGST(0);
												poEstDet.setCOMMENT1("");
												poDet.setCOMMENT1("");
												poEstDet.setLNSTAT(podetdet.getLNSTAT());
												poDet.setLNSTAT("N");
												poEstDet.setQTYRC(podetdet.getQTYRC());
												//poDet.setQTYRC(podetdet.getQTYRC());
												poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
												poEstDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setCRBY(username);
												poDet.setCRBY(username);
												poEstDet.setCRAT(DateUtils.getDateTime());
												poDet.setCRAT(DateUtils.getDateTime());
												poEstDetDAO.addPoDet(poEstDet);
												podetlist.add(poEstDet);
												
												if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
													poDet.setUKEY(uniqueID);
												}
												
												if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
													PoDetApproval poDetApproval= new PoDetApproval();
													new FieldCopier().copyFields(poDet, poDetApproval);
													poDetApproval.setUKEY(uniqueID);
													poDetApprovalDAO.addPoDet(poDetApproval);
												}
												
												
												poDetDAO.addPoDet(poDet);
												polistdet.add(poDet);
												}
												}
												new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.POEST,"PE", poEsthdr.getPOESTNO());
												new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pohdr.getPONO());
												
												//Hashtable htRecvHis = new Hashtable();
												htRecvHis.clear();
												htRecvHis.put(IDBConstants.PLANT, plant);
												htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PE);
												htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
												htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
												htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
												htRecvHis.put(IDBConstants.CREATED_BY, username);
												htRecvHis.put("MOVTID", "");
												htRecvHis.put("RECID", "");
												htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1());
												if (!poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS,
															poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1() + "," + poEsthdr.getREMARK3());
												} else if (!poEsthdr.getRemark1().equals("") && poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1());
												} else if (poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getREMARK3());
												} else {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO());
												}

												htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												boolean flagmv = movHisDao.insertIntoMovHis(htRecvHis);
												
												
												htRecvHis.clear();
												htRecvHis.put(IDBConstants.PLANT, plant);
												htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
												htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
												htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
												htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
												htRecvHis.put(IDBConstants.CREATED_BY, username);
												htRecvHis.put("MOVTID", "");
												htRecvHis.put("RECID", "");
												htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1());
												if (!pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS,
															pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1() + "," + pohdr.getREMARK3());
												} else if (!pohdr.getRemark1().equals("") && pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1());
												} else if (pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getREMARK3());
												} else {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO());
												}

												htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												boolean flagfs = movHisDao.insertIntoMovHis(htRecvHis);
												
												//Hashtable htMaster = new Hashtable();
												if (flagmv && flagfs) {
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
														}
													}
													int vi = 0;
													for (PoEstDet pordendet : podetlist) {
														int index = Integer.parseInt("0");

														//int Polno = vi + 1;
														int Polno = pordendet.getPOESTLNNO();
														Hashtable<String, String> htRemarks = new Hashtable<>();
														htRemarks.put(IDBConstants.PLANT, plant);
														htRemarks.put(IDBConstants.PODET_POESTNUM, poEsthdr.getPOESTNO());
														htRemarks.put(IDBConstants.PODET_POESTLNNO, Integer.toString(Polno));
														htRemarks.put(IDBConstants.PODET_ITEM, pordendet.getITEM());
														if (!new PoEstDetDAO().isExisitPoMultiRemarks(htRemarks)) {
															htRemarks.put(IDBConstants.REMARKS, "");
															htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
															htRemarks.put(IDBConstants.CREATED_BY, username);
															flagmv = new PoEstUtil().savePoMultiRemarks(htRemarks);
														}
													}

													for (PoEstDet pordendet : podetlist) {
														htRecvHis.clear();
														htRecvHis.put(IDBConstants.PLANT, plant);
														htRecvHis.put("DIRTYPE", TransactionConstants.PE_ADD_ITEM);
														htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
														htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
														htRecvHis.put(IDBConstants.ITEM, pordendet.getITEM());
														htRecvHis.put(IDBConstants.QTY, pordendet.getQTYOR());
														htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
														htRecvHis.put(IDBConstants.CREATED_BY, username);
														htRecvHis.put("MOVTID", "");
														htRecvHis.put("RECID", "");
														htRecvHis.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

														flagmv = movHisDao.insertIntoMovHis(htRecvHis);
													}
													
													//PO
													
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
														}
													}

													int vj = 0;
													for (PoDet pordendet : polistdet) {
														int index = Integer.parseInt("0");

														//int Polno = vj + 1;
														int Polno = pordendet.getPOLNNO();
														Hashtable<String, String> htRemarks = new Hashtable<>();
														htRemarks.put(IDBConstants.PLANT, plant);
														htRemarks.put(IDBConstants.PODET_PONUM, pohdr.getPONO());
														htRemarks.put(IDBConstants.PODET_POLNNO, Integer.toString(Polno));
														htRemarks.put(IDBConstants.PODET_ITEM, pordendet.getITEM());
														if (!new PoDetDAO().isExisitPoMultiRemarks(htRemarks)) {
															htRemarks.put(IDBConstants.REMARKS, "");
															htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
															htRemarks.put(IDBConstants.CREATED_BY, username);
															flagfs = new POUtil().savePoMultiRemarks(htRemarks);
														}
													}

													for (PoDet porddet : polistdet) {
														htRecvHis.clear();
														htRecvHis.put(IDBConstants.PLANT, plant);
														htRecvHis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
														htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
														htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
														htRecvHis.put(IDBConstants.ITEM, porddet.getITEM());
														htRecvHis.put(IDBConstants.QTY, porddet.getQTYOR());
														htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
														htRecvHis.put(IDBConstants.CREATED_BY, username);
														htRecvHis.put("MOVTID", "");
														htRecvHis.put("RECID", "");
														htRecvHis.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

														flagfs = movHisDao.insertIntoMovHis(htRecvHis);
													}
												}
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});

								}
							}
						}
					}
						DbBean.CommitTran(ut);
						String message = "Multi Purchse Estimate Added Successfully.";
						if (ajax) {
							/*
							 * request.setAttribute("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
							 * EmailMsgUtil emailMsgUtil = new EmailMsgUtil(); Map<String, String> emailMsg
							 * = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER); String
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
							response.sendRedirect("../purchaseestimate/summary?msg=" + message);
						}
					}else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Multi Purchase Estimate Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseestimate/summary?msg=" + message);
						}
					}
				
				}
			}catch (Exception e) {
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
					response.sendRedirect("../purchaseestimate/summary?msg=" + ThrowableUtil.getMessage(e));
				}
			

		
		
		
			
			}
		}
		
		if(action.equalsIgnoreCase("converttope")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			String empName="",empno = "";
			List<Hashtable<String,String>> multipurchaseAttachmentList = null;
			List<Hashtable<String,String>> multipurchaseAttachmentInfoList = null;
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				
								
				PoEstHdrDAO poEstHdrDAO= new PoEstHdrDAO();
				PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
				PoHdrDAO poHdrDAO= new PoHdrDAO();
				PoDetDAO poDetDAO = new PoDetDAO();
				PurchaseEstimateAttachDAO purchaseEstimateAttachDAO = new PurchaseEstimateAttachDAO();
				MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
				MultiPoEstDet multiPoEstDet = new MultiPoEstDet();

				List POMULTIESTLNNO = new ArrayList(), item = new ArrayList(),custname = new ArrayList(), custcode = new ArrayList(), currencyid = new ArrayList(), 
						taxtreatment = new ArrayList(), currencyuseqt = new ArrayList(), unitcost = new ArrayList(),estqty = new ArrayList(), qtyrc = new ArrayList(),
						qtyor = new ArrayList(), unitmo = new ArrayList(), pddate = new ArrayList(),
						discount = new ArrayList(), dtype = new ArrayList(), accname = new ArrayList(),
						taxtype = new ArrayList(), chkdPOMULTIESTNO = new ArrayList(),expiredate = new ArrayList();
				int POMULTIESTNOcount = 0,chkdPOMULTIESTNOCount = 0,expiredateCount=0;

				multipurchaseAttachmentList = new ArrayList<Hashtable<String, String>>();
				multipurchaseAttachmentInfoList = new ArrayList<Hashtable<String, String>>();

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					String revgersecharge = "0";
					String goodsimport = "0";
					ut = DbBean.getUserTranaction();
					ut.begin();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* PO_MULTI_ESTHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								multiPoEstHdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("POMULTIESTNO")) {
								multiPoEstHdr.setPOMULTIESTNO(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								multiPoEstHdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								multiPoEstHdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								multiPoEstHdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								multiPoEstHdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								multiPoEstHdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								multiPoEstHdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								multiPoEstHdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								multiPoEstHdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								multiPoEstHdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								multiPoEstHdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								multiPoEstHdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}
							
	                        if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
								
								empno = StrUtils.fString(fileItem.getString()).trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
								empName = StrUtils.fString(fileItem.getString()).trim();
							}
							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								multiPoEstHdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								multiPoEstHdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								multiPoEstHdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								multiPoEstHdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								multiPoEstHdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());

							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								multiPoEstHdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								multiPoEstHdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								multiPoEstHdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
														
							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							/* PODET */
							
							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								POMULTIESTLNNO.add(StrUtils.fString(fileItem.getString()).trim());
								POMULTIESTNOcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								custname.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								custcode.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid.add(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxtreatment.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ESTQTY")) {
								estqty.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("QTYRC")) {
								qtyrc.add(StrUtils.fString(fileItem.getString()).trim());
//								qtyorcount++;
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
							
//							if (fileItem.getFieldName().equalsIgnoreCase("PRODUCTDELIVERYDATE")) {
//								pddate.add(StrUtils.fString(fileItem.getString()).trim());
//								pddatecount++;
//							}
							
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
							
							if (fileItem.getFieldName().equalsIgnoreCase("chkdPOMULTIESTNO")) {
								chkdPOMULTIESTNO.add(StrUtils.fString(fileItem.getString()).trim());
								chkdPOMULTIESTNOCount++;
							}						
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								expiredate.add(StrUtils.fString(fileItem.getString()).trim());
								expiredateCount++;
							}
							
						}else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
							
							//attachment

							if(fileItem.getFieldName().equalsIgnoreCase("file")){
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/"+ multiPoEstHdr.getPOMULTIESTNO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"+ multiPoEstHdr.getPOMULTIESTNO();
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
								Hashtable multipurchaseAttachment = new Hashtable<String, String>();
								multipurchaseAttachment.put("PLANT", plant);
								multipurchaseAttachment.put("FILETYPE", fileItem.getContentType());
								multipurchaseAttachment.put("FILENAME", fileName);
								multipurchaseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								multipurchaseAttachment.put("FILEPATH", fileLocationATT);
								multipurchaseAttachment.put("CRAT",DateUtils.getDateTime());
								multipurchaseAttachment.put("CRBY",username);
								multipurchaseAttachment.put("UPAT",DateUtils.getDateTime());
								multipurchaseAttachmentList.add(multipurchaseAttachment);
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
					multiPoEstHdr.setEMPNO(empno);
					if(multiPoEstHdr.getDELIVERYDATEFORMAT() == null){
						multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					
					
					
					multiPoEstHdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					multiPoEstHdr.setGOODSIMPORT(Short.valueOf(goodsimport));
					multiPoEstHdr.setSTATUS("N");
					
					multiPoEstHdr.setLOCALEXPENSES(0.0);
					multiPoEstHdr.setCRBY(username);
					multiPoEstHdr.setCRAT(DateUtils.getDateTime());
					boolean issadded = true;

					if(issadded) {
						List< MultiPoEstDet> multiPoEstDetlist = new ArrayList<MultiPoEstDet>();
						double totalDiscountAmount = 0;
						for(int i=0;i<POMULTIESTNOcount;i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String)POMULTIESTLNNO.get(i)) -1;
							String disctype=(String)dtype.get(index);
								if(disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String)unitcost.get(index))/Double.valueOf((String) currencyuseqt.get(index)))*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									double amount = (Double.valueOf((String)unitcost.get(index))/Double.valueOf((String) currencyuseqt.get(index)))*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/Double.valueOf((String) currencyuseqt.get(index));
								}	
								totalDiscountAmount+=unitCost_AD;
						}
							for(int i=0;i<chkdPOMULTIESTNOCount;i++) {
								int index = Integer.parseInt((String)chkdPOMULTIESTNO.get(i));
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								int lnno = i+1;
								String disctype=(String)dtype.get(index);
								MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
								multiPoEstdet.setPLANT(plant);
								multiPoEstdet.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
								multiPoEstdet.setTRANDATE(multiPoEstHdr.getCollectionDate());
								multiPoEstdet.setPOMULTIESTLNNO(lnno);
								multiPoEstdet.setITEM((String)item.get(index));
								multiPoEstdet.setCustName((String) custname.get(index));
								multiPoEstdet.setCustCode((String) custcode.get(index));
								multiPoEstdet.setCURRENCYID((String) currencyid.get(index));
								multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String) currencyuseqt.get(index)));
								multiPoEstdet.setTAXTREATMENT((String) taxtreatment.get(index));
								multiPoEstdet.setUNITCOST((Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT()));
								multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
								multiPoEstdet.setUNITMO((String)unitmo.get(index));
								//multiPoEstdet.setPRODUCTDELIVERYDATE((String)pddate.get(index));
								multiPoEstdet.setEXPIREDATE((String) expiredate.get(index));
								if(disctype.equalsIgnoreCase("%")) {
									multiPoEstdet.setDISCOUNT(Double.valueOf((String)discount.get(index)));
									double amount = (Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-(amount*(Double.valueOf((String)discount.get(index))/100));
								}else {
									multiPoEstdet.setDISCOUNT((Double.valueOf((String)discount.get(index))/multiPoEstdet.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String)unitcost.get(index))/multiPoEstdet.getCURRENCYUSEQT())*Double.parseDouble((String)qtyor.get(index));
									unitCost_AD = (amount)-Double.valueOf((String)discount.get(index))/multiPoEstdet.getCURRENCYUSEQT();
								}
								
								
								if(Double.isNaN(unitCost_AOD)) {
									multiPoEstdet.setUNITCOST_AOD(0.0);
								}else {
									multiPoEstdet.setUNITCOST_AOD(unitCost_AOD);
								}
								multiPoEstdet.setDISCOUNT_TYPE((String)dtype.get(index));
								multiPoEstdet.setACCOUNT_NAME((String)accname.get(index));
								multiPoEstdet.setTAX_TYPE((String)taxtype.get(index));
								multiPoEstdet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
								multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
								multiPoEstdet.setCURRENCYUSEQT(multiPoEstdet.getCURRENCYUSEQT());
								multiPoEstdet.setPRODGST(0);
								multiPoEstdet.setCOMMENT1((String) custcode.get(index)+"-"+(String) currencyid.get(index)+"-"+(String) currencyuseqt.get(index)+"-"+(String) expiredate.get(index));
								
							
								
								multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble((String)qtyrc.get(index))));
								multiPoEstdet.setLNSTAT("N");
								
								multiPoEstdet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, (String)item.get(index))));
								multiPoEstdet.setCRBY(username);
								multiPoEstdet.setCRAT(DateUtils.getDateTime());
								multiPoEstDetlist.add(multiPoEstdet);
							}
							
						
						
						
//						int attchSize = multipurchaseAttachmentList.size();
//						if (attchSize > 0) {
//							for (int i = 0; i < attchSize; i++) {
//								Hashtable multipurchaseAttachment = new Hashtable<String, String>();
//								multipurchaseAttachment = multipurchaseAttachmentList.get(i);
//								multipurchaseAttachment.put("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
//								multipurchaseAttachmentInfoList.add(multipurchaseAttachment);
//							}
//							MultiPurchaseEstAttachDAO.addmultipurchaseAttachments(multipurchaseAttachmentInfoList, plant);
//						}

						
						//ISAUTO_CONVERT_ESTPO	
							
						if (issadded) {
								Map<Object, List<MultiPoEstDet>> porderdet = multiPoEstDetlist.stream().collect(Collectors.groupingBy(p -> (p.getCustCode()+"~"+p.getCURRENCYID()+"~"+p.getCURRENCYUSEQT()+"~"+p.getEXPIREDATE())));
								System.out.println(porderdet);
								if(porderdet.size()>0) {
									porderdet.forEach((CustCode, empList) -> { 
										String custval = (String) CustCode;
										String[] svsval = custval.split("~");
										String Custcode1 =  svsval[0].toString();
										String sCURRENCYID =  svsval[1].toString();
										String sCURRENCYUSEQT =  svsval[2].toString();
										String sEXPIREDATE ="";
										if(svsval.length>3)
										sEXPIREDATE=svsval[3].toString();
										System.out.println(CustCode);
										System.out.println(empList);
										String POESTNO="",vendname="",PERSON_INCHARGE="",TELNO="",ADD1="",ADD2="",ADD3="",TRANSPORTID="0",TAXTREATMENT="";											
										try {
											POESTNO = new TblControlDAO().getNextOrder(plant, username, IConstants.POEST);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										JSONObject vendorJson=new VendMstDAO().getVendorName(plant, svsval[0].toString());
										if(!vendorJson.isEmpty()) {
											vendname=vendorJson.getString("VNAME");
											PERSON_INCHARGE=vendorJson.getString("NAME");
											TELNO=vendorJson.getString("TELNO");
											ADD1=vendorJson.getString("ADDR1");
											ADD2=vendorJson.getString("ADDR2");
											ADD3=vendorJson.getString("ADDR3");
											TRANSPORTID=vendorJson.getString("TRANSPORTID");
											TAXTREATMENT=vendorJson.getString("TAXTREATMENT");
										}
										PoEstHdr poEsthdr = new PoEstHdr();	
										poEsthdr.setPLANT(multiPoEstHdr.getPLANT());
										poEsthdr.setPOESTNO(POESTNO);
										poEsthdr.setCustName(vendname);
										poEsthdr.setCustCode(Custcode1);
										poEsthdr.setJobNum(multiPoEstHdr.getJobNum());
										poEsthdr.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
										poEsthdr.setPersonInCharge(PERSON_INCHARGE);
										poEsthdr.setContactNum(TELNO);
										poEsthdr.setAddress(ADD1);
										poEsthdr.setAddress2(ADD2);
										poEsthdr.setAddress3(ADD3);
										poEsthdr.setCollectionTime(multiPoEstHdr.getCollectionTime());
										poEsthdr.setCollectionDate(multiPoEstHdr.getCollectionDate());
										poEsthdr.setRemark1(multiPoEstHdr.getRemark1());
										poEsthdr.setRemark2(multiPoEstHdr.getRemark2());
										poEsthdr.setREMARK3(multiPoEstHdr.getREMARK3());
										poEsthdr.setORDERTYPE(multiPoEstHdr.getORDERTYPE());
										poEsthdr.setINBOUND_GST(multiPoEstHdr.getINBOUND_GST());
										poEsthdr.setCURRENCYID(sCURRENCYID);
										poEsthdr.setDELDATE(multiPoEstHdr.getDELDATE());
										poEsthdr.setSTATUS_ID(multiPoEstHdr.getSTATUS_ID());
										poEsthdr.setSHIPPINGID(multiPoEstHdr.getSHIPPINGID());
										poEsthdr.setEXPIREDATE(sEXPIREDATE);
										poEsthdr.setSHIPPINGCUSTOMER(multiPoEstHdr.getSHIPPINGCUSTOMER());
										poEsthdr.setINCOTERMS(multiPoEstHdr.getINCOTERMS());
										poEsthdr.setORDERDISCOUNT(Double.valueOf("0"));
										poEsthdr.setSHIPPINGCOST(Double.valueOf("0"));
										poEsthdr.setPAYMENTTYPE(multiPoEstHdr.getPAYMENTTYPE());
										poEsthdr.setPAYMENT_TERMS(multiPoEstHdr.getPAYMENT_TERMS());
										poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf(multiPoEstHdr.getDELIVERYDATEFORMAT()));
										poEsthdr.setTAXTREATMENT(TAXTREATMENT);
										poEsthdr.setPURCHASE_LOCATION(multiPoEstHdr.getPURCHASE_LOCATION());
										poEsthdr.setORDER_STATUS(multiPoEstHdr.getORDER_STATUS());
										poEsthdr.setADJUSTMENT(Double.valueOf("0"));
										poEsthdr.setISDISCOUNTTAX(Short.valueOf("0"));
										poEsthdr.setISSHIPPINGTAX(Short.valueOf("0"));
										poEsthdr.setTAXID(multiPoEstHdr.getTAXID());
										poEsthdr.setISTAXINCLUSIVE(multiPoEstHdr.getISTAXINCLUSIVE());
										poEsthdr.setORDERDISCOUNTTYPE(sCURRENCYID);
										poEsthdr.setCURRENCYUSEQT(Double.valueOf(sCURRENCYUSEQT));
										poEsthdr.setPROJECTID(multiPoEstHdr.getPROJECTID());
										poEsthdr.setTRANSPORTID(Integer.valueOf(TRANSPORTID));
										poEsthdr.setEMPNO(multiPoEstHdr.getEMPNO());
										poEsthdr.setREVERSECHARGE(Short.valueOf(multiPoEstHdr.getREVERSECHARGE()));
										poEsthdr.setGOODSIMPORT(Short.valueOf(multiPoEstHdr.getGOODSIMPORT()));
										poEsthdr.setSTATUS(multiPoEstHdr.getSTATUS());
										poEsthdr.setLOCALEXPENSES(0.0);
										poEsthdr.setCRBY(username);
										poEsthdr.setCRAT(DateUtils.getDateTime());
										try {
											boolean ispoadded = poEstHdrDAO.addPoHdr(poEsthdr);
											if(ispoadded) {
												List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
												int lnnoval = 0;
												for (MultiPoEstDet podetdet :multiPoEstDetlist) {
													if(podetdet.getCustCode().equalsIgnoreCase(Custcode1) && podetdet.getCURRENCYUSEQT() == Double.valueOf(sCURRENCYUSEQT) && podetdet.getCURRENCYID().equalsIgnoreCase(sCURRENCYID) && podetdet.getEXPIREDATE().equalsIgnoreCase(sEXPIREDATE)) {
														lnnoval=lnnoval+1;
														PoEstDet poEstDet = new PoEstDet();
												poEstDet.setPLANT(plant);
												poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
												poEstDet.setPOMULTIESTNO(poEsthdr.getPOESTNO());
												poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
												poEstDet.setPOESTLNNO(lnnoval);
												poEstDet.setPOMULTIESTLNNO(podetdet.getPOMULTIESTLNNO());
												poEstDet.setITEM(podetdet.getITEM());
												poEstDet.setUNITCOST(podetdet.getUNITCOST());
												poEstDet.setQTYOR(podetdet.getQTYOR());
												poEstDet.setLNSTAT("N");
												poEstDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
												poEstDet.setUNITMO(podetdet.getUNITMO());
												poEstDet.setPRODUCTDELIVERYDATE("");
												poEstDet.setDISCOUNT_TYPE(sCURRENCYID);
												poEstDet.setACCOUNT_NAME(podetdet.getACCOUNT_NAME());
												poEstDet.setTAX_TYPE(podetdet.getTAX_TYPE());
												poEstDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setUSERFLD2(poEsthdr.getJobNum());
												poEstDet.setUSERFLD3(poEsthdr.getCustName());
												poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
												poEstDet.setPRODGST(0);
												poEstDet.setCOMMENT1("");
											
												poEstDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setCRBY(username);
												poEstDet.setCRAT(DateUtils.getDateTime());
												poEstDetDAO.addPoDet(poEstDet);
												podetlist.add(poEstDet);
												
//												int estlnno = Integer.parseInt((String) POMULTIESTLNNO.get(0));
												Hashtable htCondition = new Hashtable();
												htCondition.put("PLANT", plant);
												htCondition.put("POMULTIESTNO", multiPoEstHdr.getJobNum());
												htCondition.put("POMULTIESTLNNO", String.valueOf(podetdet.getPOMULTIESTLNNO())); //original
//												htCondition.put("POMULTIESTLNNO", podetdet.getPOMULTIESTLNNO());  //dup
//												htCondition.put("POMULTIESTLNNO", String.valueOf(estlnno)); //dub 1
												double ordqty = Double.parseDouble(podetdet.getQTYOR().toString());
												double sumqty = Double.parseDouble(podetdet.getQTYRC().toString()) + Double.parseDouble(podetdet.getQTYOR().toString());
												sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
												String updatepoestDet = "" , updatepoestHdr = "";
												if(ordqty == sumqty) {
													updatepoestDet = "set qtyRc= isNull(qtyRc,0) + " + podetdet.getQTYOR().toString()
															+ ", LNSTAT='C' ";

												} else {
													updatepoestDet = "set qtyRc= isNull(qtyRc,0) + " + podetdet.getQTYOR().toString()
															+ ", LNSTAT='O' ";
												}
												String extraCond = " AND  Qtyor >= isNull(qtyRc,0) + " + podetdet.getQTYOR().toString();
												multiPoEstDetDAO multipoEstDetdao = new multiPoEstDetDAO(); 
												MultiPoEstHdrDAO multipoEsthdrdao = new MultiPoEstHdrDAO(); 
												boolean insertFlag = multipoEstDetdao.updatepo(updatepoestDet, htCondition,extraCond);
												
												if (insertFlag) {
													htCondition.remove("POMULTIESTLNNO");

													insertFlag = multipoEstDetdao.isExisit(htCondition, "LNSTAT in ('O','N')");
													if (!insertFlag) {
														updatepoestHdr = "set  STATUS='Confirm',ORDER_STATUS='PROCESSED' ";
														insertFlag = multipoEsthdrdao.updatePO(updatepoestHdr, htCondition, "");
													} else {
														updatepoestHdr = "set ORDER_STATUS='PARTIALLY PROCESSED' ";
														insertFlag = multipoEsthdrdao.updatePO(updatepoestHdr, htCondition, "");
													}

												}
												
												}
												}
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
												htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ poEsthdr.getJobNum() + "," + poEsthdr.getRemark1());
												if (!poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS,
															poEsthdr.getCustName() +","+ poEsthdr.getJobNum() +  "," + poEsthdr.getRemark1() + "," + poEsthdr.getREMARK3());
												} else if (!poEsthdr.getRemark1().equals("") && poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ poEsthdr.getJobNum() + "," + poEsthdr.getRemark1());
												} else if (poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ poEsthdr.getJobNum() + "," + poEsthdr.getREMARK3());
												} else {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ poEsthdr.getJobNum());
												}

												htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												boolean flagmv = movHisDao.insertIntoMovHis(htRecvHis);
												
												
												
												
												Hashtable htMaster = new Hashtable();
												if (flagmv) {
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
														}
													}
													int vi = 0;
													for (PoEstDet pordendet : podetlist) {
														int index = Integer.parseInt("0");

														//int Polno = vi + 1;
														int Polno = pordendet.getPOESTLNNO();
														Hashtable<String, String> htRemarks = new Hashtable<>();
														htRemarks.put(IDBConstants.PLANT, plant);
														htRemarks.put(IDBConstants.PODET_POESTNUM, poEsthdr.getPOESTNO());
														htRemarks.put(IDBConstants.PODET_POESTLNNO, Integer.toString(Polno));
														htRemarks.put(IDBConstants.PODET_ITEM, pordendet.getITEM());
														if (!new PoEstDetDAO().isExisitPoMultiRemarks(htRemarks)) {
															htRemarks.put(IDBConstants.REMARKS, "");
															htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
															htRemarks.put(IDBConstants.CREATED_BY, username);
															flagmv = new PoEstUtil().savePoMultiRemarks(htRemarks);
														}
													}

													for (PoEstDet pordendet : podetlist) {
														htRecvHis.clear();
														htRecvHis.put(IDBConstants.PLANT, plant);
														htRecvHis.put("DIRTYPE", TransactionConstants.PE_ADD_ITEM);
														htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
														htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
														htRecvHis.put(IDBConstants.ITEM, pordendet.getITEM());
														htRecvHis.put(IDBConstants.QTY, pordendet.getQTYOR());
														htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
														htRecvHis.put(IDBConstants.CREATED_BY, username);
														htRecvHis.put("MOVTID", "");
														htRecvHis.put("RECID", "");
														htRecvHis.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

														flagmv = movHisDao.insertIntoMovHis(htRecvHis);
													}
//													
//													Hashtable htCondition = new Hashtable();
//													htCondition.put("PLANT", plant);
//													htCondition.put("MULTIPOESTNO", multiPoEstHdr.getPOMULTIESTNO());
//													String updatepoestHdr ="";
//													boolean insertFlag = _poEstDetDAO.isExisit(htCondition, "LNSTAT in ('O','N')");
//													if (!insertFlag) {
//														updatepoestHdr = "set  STATUS='Confirm',ORDER_STATUS='PROCESSED' ";
//														insertFlag = _poEstHdrDAO.updatePO(updatepoestHdr, htCondition, "");
//													} else {
//														updatepoestHdr = "set ORDER_STATUS='PARTIALLY PROCESSED' ";
//														insertFlag = _poEstHdrDAO.updatePO(updatepoestHdr, htCondition, "");
//													}

												}
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});

								}
							
						}
						DbBean.CommitTran(ut);
						String message = "Purchse Estimate Added Successfully.";
						if (ajax) {

						}else {
							response.sendRedirect("../purchaseorderestimate/summary?msg=" + message);
						}
					}else {
						DbBean.RollbackTran(ut);
						String message = "Unable To Add Purchase Estimate Order.";
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
			}catch (Exception e) {
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
			List<Hashtable<String, String>> multipurchaseAttachmentList = null;
			List<Hashtable<String, String>> multipurchaseAttachmentInfoList = null;
			try {
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);

                String empno="",empName="";
				List POMULTIESTLNNO = new ArrayList(), prvlnno = new ArrayList(), item = new ArrayList(),
						custname = new ArrayList(), custcode = new ArrayList(), currencyid = new ArrayList(), 
								taxtreatment = new ArrayList(), currencyuseqt = new ArrayList(),	unitcost = new ArrayList(), qtyor = new ArrayList(), unitmo = new ArrayList(),
						pddate = new ArrayList(), discount = new ArrayList(), dtype = new ArrayList(),
						accname = new ArrayList(), recqty = new ArrayList(), ponoline = new ArrayList(),expiredate = new ArrayList(),
						taxtype = new ArrayList();
				int POMULTIESTNOcount = 0, draftstatus = 0;//unitmocount = 0, unitcostcount = 0, taxtypecount = 0, recqtycount = 0, qtyorcount = 0, pddatecount = 0, itemcount = 0, dtypecount = 0, discountcount = 0, accnamecount = 0, prvlnnocount = 0, ponolinecount = 0, 

				multipurchaseAttachmentList = new ArrayList<Hashtable<String, String>>();
				multipurchaseAttachmentInfoList = new ArrayList<Hashtable<String, String>>();
				String ISAUTO_CONVERT_ESTPO =new PlantMstDAO().getPEST_PO(plant);//Check Purchase Estimate To Purchase Order Automatic Conversion
				PoHdrDAO poHdrDAO= new PoHdrDAO();
				PoDetDAO poDetDAO = new PoDetDAO();
				PoEstHdrDAO poEstHdrDAO= new PoEstHdrDAO();
				PoEstDetDAO poEstDetDAO = new PoEstDetDAO();
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					String revgersecharge = "0";
					String goodsimport = "0";
					String selectesp = "";
					String isProductAssignedSupplier = "";
					ut = DbBean.getUserTranaction();
					ut.begin();
					MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
					MultiPoEstDet multiPoEstDet = new MultiPoEstDet();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* POHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								multiPoEstHdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("POMULTIESTNO")) {
								multiPoEstHdr.setPOMULTIESTNO(StrUtils.fString(fileItem.getString()).trim());
							}

							

							if (fileItem.getFieldName().equalsIgnoreCase("JOB_NUM")) {
								multiPoEstHdr.setJobNum(StrUtils.fString(fileItem.getString()).trim());
							}

							

							if (fileItem.getFieldName().equalsIgnoreCase("COLLECTION_TIME")) {
								multiPoEstHdr.setCollectionTime(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDDATE")) {
								multiPoEstHdr.setCollectionDate(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK1")) {
								multiPoEstHdr.setRemark1(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK2")) {
								multiPoEstHdr.setRemark2(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REMARK3")) {
								multiPoEstHdr.setREMARK3(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("ORDERTYPE")) {
								multiPoEstHdr.setORDERTYPE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("draftstatus")) {
								draftstatus = Integer.valueOf(StrUtils.fString(fileItem.getString()).trim());
							}							

							if (fileItem.getFieldName().equalsIgnoreCase("DELDATE")) {
								multiPoEstHdr.setDELDATE(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("STATUS_ID")) {
								multiPoEstHdr.setSTATUS_ID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGID")) {
								multiPoEstHdr.setSHIPPINGID(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("SHIPPINGCUSTOMER")) {
								multiPoEstHdr.setSHIPPINGCUSTOMER(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("INCOTERMS")) {
								multiPoEstHdr.setINCOTERMS(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GST")) {
								multiPoEstHdr.setINBOUND_GST(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}							

							if (fileItem.getFieldName().equalsIgnoreCase("payment_type")) {
								multiPoEstHdr.setPAYMENTTYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("payment_terms")) {
								multiPoEstHdr.setPAYMENT_TERMS(StrUtils.fString(fileItem.getString()).trim());
							}
							
							 if (fileItem.getFieldName().equalsIgnoreCase("EMPNO")) {
									
									empno = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("EMP_NAME")) {
									empName = StrUtils.fString(fileItem.getString()).trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("selectesp")) {
									selectesp = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
								}
								
								if (fileItem.getFieldName().equalsIgnoreCase("isProductAssignedSupplier")) {
									isProductAssignedSupplier = StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
								}
							
									

							if (fileItem.getFieldName().equalsIgnoreCase("DATEFORMAT")) {
								multiPoEstHdr.setDELIVERYDATEFORMAT(Short
										.valueOf(StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim()));
							}

							

							if (fileItem.getFieldName().equalsIgnoreCase("PURCHASE_LOC")) {
								multiPoEstHdr.setPURCHASE_LOCATION(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("REVERSECHARGE")) {
								revgersecharge = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("GOODSIMPORT")) {
								goodsimport = StrUtils.fString((fileItem.getString() != null) ? "1" : "0").trim();
							}

							if (fileItem.getFieldName().equalsIgnoreCase("orderstatus")) {
								multiPoEstHdr.setORDER_STATUS(StrUtils.fString(fileItem.getString()).trim());
							}

			                 if (fileItem.getFieldName().equalsIgnoreCase("taxid")) {
								multiPoEstHdr.setTAXID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item_rates")) {
								multiPoEstHdr.setISTAXINCLUSIVE(Short.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}

							

							if (fileItem.getFieldName().equalsIgnoreCase("PROJECTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setPROJECTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("TRANSPORTID")) {
								if (!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
									multiPoEstHdr.setTRANSPORTID(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								}
							}
							/* PODET */

							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								POMULTIESTLNNO.add(StrUtils.fString(fileItem.getString()).trim());
								POMULTIESTNOcount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("prlnno")) {
								prvlnno.add(StrUtils.fString(fileItem.getString()).trim());
//								prvlnnocount++;
							}

							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								item.add(StrUtils.fString(fileItem.getString()).trim());
//								itemcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("vendname")) {
								custname.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("vendno")) {
								custcode.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYID")) {
								currencyid.add(StrUtils.fString(fileItem.getString()).trim());
							}

							if (fileItem.getFieldName().equalsIgnoreCase("nTAXTREATMENT")) {
								taxtreatment.add(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQT")) {
								currencyuseqt.add(StrUtils.fString(fileItem.getString()).trim());
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

							if (fileItem.getFieldName().equalsIgnoreCase("POMULTIESTLNNO")) {
								ponoline.add(StrUtils.fString(fileItem.getString()).trim());
//								ponolinecount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("EXPIREDATE")) {
								expiredate.add(StrUtils.fString(fileItem.getString()).trim());
							}

						} else if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {

							// attachment

							if (fileItem.getFieldName().equalsIgnoreCase("file")) {
								String fileLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/" + multiPoEstHdr.getPOMULTIESTNO();
								String filetempLocationATT = "C:/ATTACHMENTS/PURCHASE" + "/temp" + "/"
										+ multiPoEstHdr.getPOMULTIESTNO();
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
								Hashtable multipurchaseAttachment = new Hashtable<String, String>();
								multipurchaseAttachment.put("PLANT", plant);
								multipurchaseAttachment.put("FILETYPE", fileItem.getContentType());
								multipurchaseAttachment.put("FILENAME", fileName);
								multipurchaseAttachment.put("FILESIZE", String.valueOf(fileItem.getSize()));
								multipurchaseAttachment.put("FILEPATH", fileLocationATT);
								multipurchaseAttachment.put("CRAT", DateUtils.getDateTime());
								multipurchaseAttachment.put("CRBY", username);
								multipurchaseAttachment.put("UPAT", DateUtils.getDateTime());
								multipurchaseAttachmentList.add(multipurchaseAttachment);
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
					multiPoEstHdr.setEMPNO(empno);
					if (multiPoEstHdr.getDELIVERYDATEFORMAT() == null) {
						multiPoEstHdr.setDELIVERYDATEFORMAT(Short.valueOf("0"));
					}
					
					
				
				
					multiPoEstHdr.setREVERSECHARGE(Short.valueOf(revgersecharge));
					multiPoEstHdr.setGOODSIMPORT(Short.valueOf(goodsimport));
					short one = 1;
					short zero = 0;
					if(selectesp.equals("1")) {
						multiPoEstHdr.setSAMEASEXPDATE(Short.valueOf(one));
					}else {
						multiPoEstHdr.setSAMEASEXPDATE(Short.valueOf(zero));
					}
					if(isProductAssignedSupplier.equals("1")) {
						multiPoEstHdr.setISPRODUCTASSIGNEDSUPPLIER(Short.valueOf(one));
					}else {
						multiPoEstHdr.setISPRODUCTASSIGNEDSUPPLIER(Short.valueOf(zero));
					}

					/* boolean issadded = poHdrDAO.addPoHdr(multiPoEstHdr); */
					MultiPoEstHdr pohdrbypono = multiPoEstHdrDAO.getPoHdrByPono(plant, multiPoEstHdr.getPOMULTIESTNO());
					multiPoEstHdr.setSTATUS(pohdrbypono.getSTATUS());
					if (draftstatus == 1 ) {
						if(pohdrbypono.getORDER_STATUS().equalsIgnoreCase("Draft")) {
						if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1")) {
							multiPoEstHdr.setORDER_STATUS("PROCESSED");
							multiPoEstHdr.setSTATUS("Confirm");
						} else
						multiPoEstHdr.setORDER_STATUS("Open");
						} else
							multiPoEstHdr.setORDER_STATUS("Open");
					}else {
						multiPoEstHdr.setORDER_STATUS("Draft");
					}
					
					multiPoEstHdr.setCRBY(pohdrbypono.getCRBY());
					multiPoEstHdr.setCRAT(pohdrbypono.getCRAT());
					multiPoEstHdr.setLOCALEXPENSES(pohdrbypono.getLOCALEXPENSES());
					boolean issadded = multiPoEstHdrDAO.updatePoHdr(multiPoEstHdr);

					
					List<MultiPoEstDet> podetail = MultiPoEstDetDAO.getPoDetByPono(plant, multiPoEstHdr.getPOMULTIESTNO());
					for (MultiPoEstDet multiPoEstdet : podetail) {
						boolean Detstatus = true;
						for (int i = 0; i < POMULTIESTNOcount; i++) {
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							if (multiPoEstdet.getPOMULTIESTLNNO() == ponumline) {
								Detstatus = false;
							}
						}
						if (Detstatus) {
							MultiPoEstDetDAO.DeletePoDet(plant, multiPoEstHdr.getPOMULTIESTNO(), multiPoEstdet.getPOMULTIESTLNNO());
						}
					}

					if (issadded) {
						List<MultiPoEstDet> multiPoEstDetlist = new ArrayList<MultiPoEstDet>();
						double totalDiscountAmount = 0;
						for (int i = 0; i < POMULTIESTNOcount; i++) {
							double unitCost_AD = 0d;
							int index = Integer.parseInt((String) POMULTIESTLNNO.get(i)) - 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
								if (disctype.equalsIgnoreCase("%")) {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstDet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstDet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / multiPoEstDet.getCURRENCYUSEQT();
								}
								totalDiscountAmount += unitCost_AD;
							}
						}

						for (int i = 0; i < POMULTIESTNOcount; i++) {
							int index = Integer.parseInt((String) POMULTIESTLNNO.get(i)) - 1;
							int lnno = i + 1;
							int ponumline = Integer.parseInt((String) ponoline.get(i));
							String disctype = (String) dtype.get(index);
							if (ponumline != 0) {
								MultiPoEstDet multiPoEstdet = MultiPoEstDetDAO.getPoDetByPonoPllno(plant, pohdrbypono.getPOMULTIESTNO(), ponumline);
								int lineno = multiPoEstdet.getPOMULTIESTLNNO();
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								if(multiPoEstdet.getQTYOR().doubleValue()!=Double.parseDouble((String)qtyor.get(index)))//Status update Azees
								{
									if(multiPoEstdet.getQTYRC().doubleValue()>0)
										multiPoEstdet.setLNSTAT("O");
									else
										multiPoEstdet.setLNSTAT("N");
								}
								multiPoEstdet.setTRANDATE(multiPoEstHdr.getCollectionDate());
								multiPoEstdet.setITEM((String) item.get(index));
								multiPoEstdet.setPOMULTIESTLNNO(lnno);
								multiPoEstdet.setCustName((String) custname.get(index));
								multiPoEstdet.setCustCode((String) custcode.get(index));
								multiPoEstdet.setCURRENCYID((String) currencyid.get(index));
								multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String) currencyuseqt.get(index)));
								multiPoEstdet.setTAXTREATMENT((String) taxtreatment.get(index));
								multiPoEstdet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
								multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								multiPoEstdet.setUNITMO((String) unitmo.get(index));
								//multiPoEstdet.setPRODUCTDELIVERYDATE((String) pddate.get(index));
								multiPoEstdet.setEXPIREDATE((String) expiredate.get(index));
								if (disctype.equalsIgnoreCase("%")) {
									multiPoEstdet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									multiPoEstdet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT();
								}

								
								if(Double.isNaN(unitCost_AOD)) {
									multiPoEstdet.setUNITCOST_AOD(0.0);
								}else {
									multiPoEstdet.setUNITCOST_AOD(unitCost_AOD);	
								}	
								multiPoEstdet.setDISCOUNT_TYPE((String) dtype.get(index));
								multiPoEstdet.setACCOUNT_NAME((String) accname.get(index));
								multiPoEstdet.setTAX_TYPE((String) taxtype.get(index));
								multiPoEstdet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
								multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
								multiPoEstdet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								multiPoEstdet.setUPAT(DateUtils.getDateTime());
								multiPoEstdet.setCURRENCYUSEQT(multiPoEstdet.getCURRENCYUSEQT());
								multiPoEstdet.setCOMMENT1((String) custcode.get(index)+"-"+(String) currencyid.get(index)+"-"+(String) currencyuseqt.get(index)+"-"+(String) expiredate.get(index));
								if(draftstatus == 1 && pohdrbypono.getORDER_STATUS().equalsIgnoreCase("Draft")) {
									if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
									{
									multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
									multiPoEstdet.setLNSTAT("C");
									} else {
									multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
									multiPoEstdet.setLNSTAT("N");
									}
									} else {
										multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
										multiPoEstdet.setLNSTAT("N");
										}
								multiPoEstdet.setUPBY(username);
								multiPoEstDetlist.add(multiPoEstdet);
								multiPoEstdet.setCOMMENT1("");
								MultiPoEstDetDAO.updatePoDetpoEdit(multiPoEstdet,lineno);
							} else {
								double unitCost_AD = 0d, unitCost_AOD = 0d;
								MultiPoEstDet multiPoEstdet = new MultiPoEstDet();
								multiPoEstdet.setPLANT(plant);
								multiPoEstdet.setPOMULTIESTNO(multiPoEstHdr.getPOMULTIESTNO());
								multiPoEstdet.setCustName((String) custname.get(index));
								multiPoEstdet.setCustCode((String) custcode.get(index));
								multiPoEstdet.setCURRENCYID((String) currencyid.get(index));
								multiPoEstdet.setCURRENCYUSEQT(Double.valueOf((String) currencyuseqt.get(index)));
								multiPoEstdet.setTRANDATE(multiPoEstHdr.getCollectionDate());
								multiPoEstdet.setPOMULTIESTLNNO(lnno);
								multiPoEstdet.setITEM((String) item.get(index));
								multiPoEstdet.setUNITCOST(
										(Double.valueOf((String) unitcost.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
								multiPoEstdet.setQTYOR(BigDecimal.valueOf(Double.parseDouble((String) qtyor.get(index))));
								multiPoEstdet.setUNITMO((String) unitmo.get(index));
								//multiPoEstdet.setPRODUCTDELIVERYDATE((String) pddate.get(index));
								multiPoEstdet.setEXPIREDATE((String) expiredate.get(index));
								if (disctype.equalsIgnoreCase("%")) {
									multiPoEstdet.setDISCOUNT(Double.valueOf((String) discount.get(index)));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- (amount * (Double.valueOf((String) discount.get(index)) / 100));
								} else {
									multiPoEstdet.setDISCOUNT(
											(Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT()));
									double amount = (Double.valueOf((String) unitcost.get(index))
											/ multiPoEstdet.getCURRENCYUSEQT()) * Double.parseDouble((String) qtyor.get(index));
									unitCost_AD = (amount)
											- Double.valueOf((String) discount.get(index)) / multiPoEstdet.getCURRENCYUSEQT();
								}

								
								if(Double.isNaN(unitCost_AOD)) {
									multiPoEstdet.setUNITCOST_AOD(0.0);
								}else {
									multiPoEstdet.setUNITCOST_AOD(unitCost_AOD);	
								}								
								multiPoEstdet.setDISCOUNT_TYPE((String) dtype.get(index));
								multiPoEstdet.setACCOUNT_NAME((String) accname.get(index));
								multiPoEstdet.setTAX_TYPE((String) taxtype.get(index));
								multiPoEstdet.setUSERFLD1(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								multiPoEstdet.setUSERFLD2(multiPoEstHdr.getJobNum());
								multiPoEstdet.setUSERFLD3(multiPoEstdet.getCustName());
								multiPoEstdet.setCURRENCYUSEQT(multiPoEstdet.getCURRENCYUSEQT());
								multiPoEstdet.setPRODGST(0);
								multiPoEstdet.setCOMMENT1((String) custcode.get(index)+"-"+(String) currencyid.get(index)+"-"+(String) currencyuseqt.get(index)+"-"+(String) expiredate.get(index));
								if(draftstatus == 1 && pohdrbypono.getORDER_STATUS().equalsIgnoreCase("Draft")) {
									if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
									{
									multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble((String)qtyor.get(index))));
									multiPoEstdet.setLNSTAT("C");
									} else {
									multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
									multiPoEstdet.setLNSTAT("N");
									}
									} else {
										multiPoEstdet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
										multiPoEstdet.setLNSTAT("N");
										}
								multiPoEstdet.setItemDesc(StrUtils
										.fString(new ItemMstDAO().getItemDesc(plant, (String) item.get(index))));
								multiPoEstdet.setCRBY(username);
								multiPoEstdet.setCRAT(DateUtils.getDateTime());								
								multiPoEstDetlist.add(multiPoEstdet);
								multiPoEstdet.setCOMMENT1("");
								MultiPoEstDetDAO.addMultiPoEstDet(multiPoEstdet);
							}
						}
					
						int attchSize = multipurchaseAttachmentList.size();
						if (attchSize > 0) {
							for (int i = 0; i < attchSize; i++) {
								Hashtable multipurchaseAttachmentat = new Hashtable<String, String>();
								multipurchaseAttachmentat = multipurchaseAttachmentList.get(i);
								multipurchaseAttachmentat.put("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
								multipurchaseAttachmentInfoList.add(multipurchaseAttachmentat);
							}
							MultiPurchaseEstAttachDAO.addmultipurchaseAttachments(multipurchaseAttachmentInfoList, plant);
						}

						
						  						  
						  
						  
						  List<MultiPoEstDetRemarks> podetrmk = MultiPoEstDetDAO.getPoDetRemarksByPono(plant, multiPoEstHdr.getPOMULTIESTNO());
							List<MultiPoEstDetRemarks> multipodetrmkupdate = new ArrayList<MultiPoEstDetRemarks>();
							for (MultiPoEstDetRemarks multiPoEstDetRemarks : podetrmk) { 
								boolean rmstatus = false;
								int newlnno = 0;
								for (int i = 0; i < POMULTIESTNOcount; i++) {
									int prvlineno = Integer.parseInt((String) prvlnno.get(i));
									if (multiPoEstDetRemarks.getPOMULTIESTLNNO() == prvlineno) {
										rmstatus = true;
										newlnno = Integer.parseInt((String) POMULTIESTLNNO.get(i));
									}
								}
								
						  if (rmstatus) { multiPoEstDetRemarks.setPOMULTIESTLNNO(newlnno);
						  multipodetrmkupdate.add(multiPoEstDetRemarks); } else {
						  MultiPoEstDetDAO.DeletePoDetRemarks(plant,
						  multiPoEstDetRemarks.getID_REMARKS()); }
						  
						  }
						 

						
						 if (multipodetrmkupdate.size() > 0) {
						 MultiPoEstDetDAO.updateMultiPoEstDetRemarksBYID(multipodetrmkupdate); }
						 
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.UPDATE_PM);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, multiPoEstHdr.getJobNum());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, multiPoEstHdr.getPOMULTIESTNO());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1());
						if (!multiPoEstHdr.getRemark1().equals("") && !multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1() + "," + multiPoEstHdr.getREMARK3());
						} else if (!multiPoEstHdr.getRemark1().equals("") && multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1());
						} else if (multiPoEstHdr.getRemark1().equals("") && !multiPoEstHdr.getREMARK3().equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,multiPoEstHdr.getREMARK3());
						} else {
							htRecvHis.put(IDBConstants.REMARKS,"");
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (multiPoEstHdr.getRemark1().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, multiPoEstHdr.getRemark1());
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
									htRecvHis.put("REMARKS", multiPoEstHdr.getRemark1());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (multiPoEstHdr.getREMARK3().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, multiPoEstHdr.getREMARK3());
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
									htRecvHis.put("REMARKS", multiPoEstHdr.getREMARK3());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (multiPoEstHdr.getINCOTERMS().length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, multiPoEstHdr.getINCOTERMS());

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
									htRecvHis.put("REMARKS", multiPoEstHdr.getINCOTERMS());
									htRecvHis.put(IDBConstants.CREATED_BY, username);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							for (MultiPoEstDet porderdet : multiPoEstDetlist) {
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "MULTI_PURCHASE_ORDER_UPDATE_PRODUCT");
								htRecvHis.put(IDBConstants.ITEM, porderdet.getITEM());
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, multiPoEstHdr.getPOMULTIESTNO());
								htRecvHis.put(IDBConstants.MOVHIS_ORDLNO, porderdet.getPOMULTIESTLNNO());
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
						
						//ISAUTO_CONVERT_ESTPO	
						
						if(draftstatus == 1 && pohdrbypono.getORDER_STATUS().equalsIgnoreCase("Draft")) {
						if (flag) {
							if(ISAUTO_CONVERT_ESTPO.equalsIgnoreCase("1"))
							{
								
								Map<Object, List<MultiPoEstDet>> porderdet = multiPoEstDetlist.stream().collect(Collectors.groupingBy(p -> (p.getCustCode()+"~"+p.getCURRENCYID()+"~"+p.getCURRENCYUSEQT()+"~"+p.getEXPIREDATE())));
								System.out.println(porderdet);
								if(porderdet.size()>0) {
									porderdet.forEach((CustCode, empList) -> { 
										String custval = (String) CustCode;
										String[] svsval = custval.split("~");
										String Custcode1 =  svsval[0].toString();
										String sCURRENCYID =  svsval[1].toString();
										String sCURRENCYUSEQT =  svsval[2].toString();
										String sEXPIREDATE ="";
										if(svsval.length>3)
										sEXPIREDATE=svsval[3].toString();
										System.out.println(CustCode);
										System.out.println(empList);
										String PONO="",POESTNO="",vendname="",PERSON_INCHARGE="",TELNO="",ADD1="",ADD2="",ADD3="",TRANSPORTID="0",TAXTREATMENT="",uniqueID="";											
										try {
											POESTNO = new TblControlDAO().getNextOrder(plant, username, IConstants.POEST);
											PONO = new TblControlDAO().getNextOrder(plant, username, IConstants.INBOUND);
											uniqueID = UUID.randomUUID().toString();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										JSONObject vendorJson=new VendMstDAO().getVendorName(plant, svsval[0].toString());
										if(!vendorJson.isEmpty()) {
											vendname=vendorJson.getString("VNAME");
											PERSON_INCHARGE=vendorJson.getString("NAME");
											TELNO=vendorJson.getString("TELNO");
											ADD1=vendorJson.getString("ADDR1");
											ADD2=vendorJson.getString("ADDR2");
											ADD3=vendorJson.getString("ADDR3");
											TRANSPORTID=vendorJson.getString("TRANSPORTID");
											TAXTREATMENT=vendorJson.getString("TAXTREATMENT");
										}
										PoEstHdr poEsthdr = new PoEstHdr();	
										PoHdr pohdr = new PoHdr();
										poEsthdr.setPLANT(multiPoEstHdr.getPLANT());
										pohdr.setPLANT(multiPoEstHdr.getPLANT());
										poEsthdr.setPOESTNO(POESTNO);
										pohdr.setPOESTNO(POESTNO);
										pohdr.setPONO(PONO);
										poEsthdr.setCustName(vendname);
										pohdr.setCustName(vendname);
										poEsthdr.setCustCode(Custcode1);
										pohdr.setCustCode(Custcode1);
										poEsthdr.setJobNum(multiPoEstHdr.getPOMULTIESTNO());
										pohdr.setJobNum(POESTNO);
										poEsthdr.setPersonInCharge(PERSON_INCHARGE);
										pohdr.setPersonInCharge(PERSON_INCHARGE);
										poEsthdr.setContactNum(TELNO);
										pohdr.setContactNum(TELNO);
										poEsthdr.setAddress(ADD1);
										pohdr.setAddress(ADD1);
										poEsthdr.setAddress2(ADD2);
										pohdr.setAddress2(ADD2);
										poEsthdr.setAddress3(ADD3);
										pohdr.setAddress3(ADD3);
										poEsthdr.setCollectionTime(multiPoEstHdr.getCollectionTime());
										pohdr.setCollectionTime(multiPoEstHdr.getCollectionTime());
										poEsthdr.setCollectionDate(multiPoEstHdr.getCollectionDate());
										pohdr.setCollectionDate(multiPoEstHdr.getCollectionDate());
										poEsthdr.setRemark1(multiPoEstHdr.getRemark1());
										pohdr.setRemark1(multiPoEstHdr.getRemark1());
										poEsthdr.setRemark2(multiPoEstHdr.getRemark2());
										pohdr.setRemark2(multiPoEstHdr.getRemark2());
										poEsthdr.setREMARK3(multiPoEstHdr.getREMARK3());
										pohdr.setREMARK3(multiPoEstHdr.getREMARK3());										
										poEsthdr.setORDERTYPE(multiPoEstHdr.getORDERTYPE());
										//pohdr.setORDERTYPE(multiPoEstHdr.getORDERTYPE());
										pohdr.setORDERTYPE("");
										poEsthdr.setINBOUND_GST(multiPoEstHdr.getINBOUND_GST());
										pohdr.setINBOUND_GST(multiPoEstHdr.getINBOUND_GST());
										poEsthdr.setCURRENCYID(sCURRENCYID);
										pohdr.setCURRENCYID(sCURRENCYID);
										poEsthdr.setDELDATE(multiPoEstHdr.getDELDATE());
										pohdr.setDELDATE(multiPoEstHdr.getDELDATE());
										poEsthdr.setSTATUS_ID(multiPoEstHdr.getSTATUS_ID());
										pohdr.setSTATUS_ID(multiPoEstHdr.getSTATUS_ID());
										poEsthdr.setSHIPPINGID(multiPoEstHdr.getSHIPPINGID());
										pohdr.setSHIPPINGID(multiPoEstHdr.getSHIPPINGID());
										poEsthdr.setEXPIREDATE(sEXPIREDATE);
										poEsthdr.setSHIPPINGCUSTOMER(multiPoEstHdr.getSHIPPINGCUSTOMER());
										pohdr.setSHIPPINGCUSTOMER(multiPoEstHdr.getSHIPPINGCUSTOMER());
										poEsthdr.setINCOTERMS(multiPoEstHdr.getINCOTERMS());
										pohdr.setINCOTERMS(multiPoEstHdr.getINCOTERMS());
										poEsthdr.setORDERDISCOUNT(Double.valueOf("0"));
										pohdr.setORDERDISCOUNT(Double.valueOf("0"));
										poEsthdr.setSHIPPINGCOST(Double.valueOf("0"));
										pohdr.setSHIPPINGCOST(Double.valueOf("0"));
										poEsthdr.setPAYMENTTYPE(multiPoEstHdr.getPAYMENTTYPE());
										pohdr.setPAYMENTTYPE(multiPoEstHdr.getPAYMENTTYPE());
										poEsthdr.setPAYMENT_TERMS(multiPoEstHdr.getPAYMENT_TERMS());
										pohdr.setPAYMENT_TERMS(multiPoEstHdr.getPAYMENT_TERMS());
										poEsthdr.setDELIVERYDATEFORMAT(Short.valueOf(multiPoEstHdr.getDELIVERYDATEFORMAT()));
										pohdr.setDELIVERYDATEFORMAT(Short.valueOf(multiPoEstHdr.getDELIVERYDATEFORMAT()));
										poEsthdr.setTAXTREATMENT(TAXTREATMENT);
										pohdr.setTAXTREATMENT(TAXTREATMENT);
										poEsthdr.setPURCHASE_LOCATION(multiPoEstHdr.getPURCHASE_LOCATION());
										pohdr.setPURCHASE_LOCATION(multiPoEstHdr.getPURCHASE_LOCATION());
										poEsthdr.setORDER_STATUS(multiPoEstHdr.getORDER_STATUS());
										pohdr.setORDER_STATUS("Open");
										poEsthdr.setADJUSTMENT(Double.valueOf("0"));
										pohdr.setADJUSTMENT(Double.valueOf("0"));
										poEsthdr.setISDISCOUNTTAX(Short.valueOf("0"));
										pohdr.setISDISCOUNTTAX(Short.valueOf("0"));
										poEsthdr.setISSHIPPINGTAX(Short.valueOf("0"));
										pohdr.setISSHIPPINGTAX(Short.valueOf("0"));
										poEsthdr.setTAXID(multiPoEstHdr.getTAXID());
										pohdr.setTAXID(multiPoEstHdr.getTAXID());
										poEsthdr.setISTAXINCLUSIVE(multiPoEstHdr.getISTAXINCLUSIVE());
										pohdr.setISTAXINCLUSIVE(multiPoEstHdr.getISTAXINCLUSIVE());
										poEsthdr.setORDERDISCOUNTTYPE(sCURRENCYID);
										pohdr.setORDERDISCOUNTTYPE(sCURRENCYID);
										poEsthdr.setCURRENCYUSEQT(Double.valueOf(sCURRENCYUSEQT));
										pohdr.setCURRENCYUSEQT(Double.valueOf(sCURRENCYUSEQT));
										poEsthdr.setPROJECTID(multiPoEstHdr.getPROJECTID());
										pohdr.setPROJECTID(multiPoEstHdr.getPROJECTID());
										poEsthdr.setTRANSPORTID(Integer.valueOf(TRANSPORTID));
										pohdr.setTRANSPORTID(Integer.valueOf(TRANSPORTID));
										poEsthdr.setEMPNO(multiPoEstHdr.getEMPNO());
										pohdr.setEMPNO(multiPoEstHdr.getEMPNO());
										poEsthdr.setREVERSECHARGE(Short.valueOf(multiPoEstHdr.getREVERSECHARGE()));
										pohdr.setREVERSECHARGE(Short.valueOf(multiPoEstHdr.getREVERSECHARGE()));
										poEsthdr.setGOODSIMPORT(Short.valueOf(multiPoEstHdr.getGOODSIMPORT()));
										pohdr.setGOODSIMPORT(Short.valueOf(multiPoEstHdr.getGOODSIMPORT()));
										poEsthdr.setSTATUS(multiPoEstHdr.getSTATUS());
										
										pohdr.setSTATUS("N");
										poEsthdr.setLOCALEXPENSES(0.0);
										pohdr.setLOCALEXPENSES(0.0);
										poEsthdr.setCRBY(username);
										pohdr.setCRBY(username);
										poEsthdr.setCRAT(DateUtils.getDateTime());
										pohdr.setCRAT(DateUtils.getDateTime());
										try {
											PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();
											PoHdrAprrovalDAO poHdrAprrovalDAO = new PoHdrAprrovalDAO();
											PoDetApprovalDAO poDetApprovalDAO = new PoDetApprovalDAO();
											boolean approvalcheck = pltApproval.CheckApprovalByUser(plant, "PURCHASE", "CREATE", username);
											if(approvalcheck) {
												pohdr.setORDER_STATUS("Draft");
												pohdr.setAPPROVAL_STATUS("CREATE APPROVAL PENDING");
												pohdr.setUKEY(uniqueID);
										} else
											pohdr.setAPPROVAL_STATUS("");
										
										boolean ispoadded = poEstHdrDAO.addPoHdr(poEsthdr);
										int hdrid = poHdrDAO.addPoHdrReturnKey(pohdr);
										boolean isPOSadded =false;
										if(hdrid > 0) {
											isPOSadded = true;
										}
										
										if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
											PoHdrApproval poHdrApproval= new PoHdrApproval();
											new FieldCopier().copyFields(pohdr, poHdrApproval);
											poHdrApproval.setID(hdrid);
											poHdrAprrovalDAO.addPoHdr(poHdrApproval);
										}
											/*
											 * boolean ispoadded = poEstHdrDAO.addPoHdr(poEsthdr); boolean isPOSadded =
											 * poHdrDAO.addPoHdr(pohdr);
											 */
											if(ispoadded && isPOSadded) {
												List<PoEstDet> podetlist = new ArrayList<PoEstDet>();
												List<PoDet> polistdet = new ArrayList<PoDet>();
												int lnnoval = 0;
												for (MultiPoEstDet podetdet :multiPoEstDetlist) {
													if(podetdet.getCustCode().equalsIgnoreCase(Custcode1) && podetdet.getCURRENCYUSEQT() == Double.valueOf(sCURRENCYUSEQT) && podetdet.getCURRENCYID().equalsIgnoreCase(sCURRENCYID) && podetdet.getEXPIREDATE().equalsIgnoreCase(sEXPIREDATE)) {
														lnnoval=lnnoval+1;
														PoEstDet poEstDet = new PoEstDet();
														PoDet poDet = new PoDet();
												poEstDet.setPLANT(plant);
												poDet.setPLANT(plant);
												poEstDet.setPOESTNO(poEsthdr.getPOESTNO());
												poEstDet.setPOMULTIESTNO(poEsthdr.getPOMULTIESTNO());
												poDet.setPONO(pohdr.getPONO());
												poEstDet.setTRANDATE(poEsthdr.getCollectionDate());
												poDet.setTRANDATE(poEsthdr.getCollectionDate());
												poEstDet.setPOESTLNNO(lnnoval);
												poEstDet.setPOMULTIESTLNNO(podetdet.getPOMULTIESTLNNO());
												poDet.setPOESTNO(poEsthdr.getPOESTNO());
												poDet.setPOESTLNNO(lnnoval);
												poDet.setPOLNNO(lnnoval);
												poEstDet.setITEM(podetdet.getITEM());
												poDet.setITEM(podetdet.getITEM());
												poEstDet.setUNITCOST(podetdet.getUNITCOST());
												poDet.setUNITCOST(podetdet.getUNITCOST());
												poEstDet.setQTYOR(podetdet.getQTYOR());
												poDet.setQTYOR(podetdet.getQTYOR());
												poEstDet.setUNITMO(podetdet.getUNITMO());
												poDet.setUNITMO(podetdet.getUNITMO());
												poEstDet.setPRODUCTDELIVERYDATE("");
												poDet.setPRODUCTDELIVERYDATE("");
												poEstDet.setDISCOUNT_TYPE(sCURRENCYID);
												poDet.setDISCOUNT_TYPE(sCURRENCYID);
												poEstDet.setACCOUNT_NAME(podetdet.getACCOUNT_NAME());
												poDet.setACCOUNT_NAME(podetdet.getACCOUNT_NAME());
												poEstDet.setTAX_TYPE(podetdet.getTAX_TYPE());
												poDet.setTAX_TYPE(podetdet.getTAX_TYPE());
												poEstDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poDet.setUSERFLD1(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setUSERFLD2(poEsthdr.getJobNum());
												poDet.setUSERFLD2(poEsthdr.getJobNum());
												poEstDet.setUSERFLD3(poEsthdr.getCustName());
												poDet.setUSERFLD3(poEsthdr.getCustName());
												poEstDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
												poDet.setCURRENCYUSEQT(poEsthdr.getCURRENCYUSEQT());
												poEstDet.setPRODGST(0);
												poDet.setPRODGST(0);
												poEstDet.setCOMMENT1("");
												poDet.setCOMMENT1("");
												poEstDet.setLNSTAT(podetdet.getLNSTAT());
												poDet.setLNSTAT("N");
												poEstDet.setQTYRC(podetdet.getQTYRC());
												//poDet.setQTYRC(podetdet.getQTYRC());
												poDet.setQTYRC(BigDecimal.valueOf(Double.parseDouble("0.0")));
												poEstDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poDet.setItemDesc(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, podetdet.getITEM())));
												poEstDet.setCRBY(username);
												poDet.setCRBY(username);
												poEstDet.setCRAT(DateUtils.getDateTime());
												poDet.setCRAT(DateUtils.getDateTime());
												poEstDetDAO.addPoDet(poEstDet);
												podetlist.add(poEstDet);
												
												if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
													poDet.setUKEY(uniqueID);
												}
												
												if(pohdr.getAPPROVAL_STATUS().equalsIgnoreCase("CREATE APPROVAL PENDING")) {
													PoDetApproval poDetApproval= new PoDetApproval();
													new FieldCopier().copyFields(poDet, poDetApproval);
													poDetApproval.setUKEY(uniqueID);
													poDetApprovalDAO.addPoDet(poDetApproval);
												
												}
												poDetDAO.addPoDet(poDet);
												polistdet.add(poDet);
												}
												}
												new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.POEST,"PE", poEsthdr.getPOESTNO());
												new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pohdr.getPONO());
												
												//Hashtable htRecvHis = new Hashtable();
												htRecvHis.clear();
												htRecvHis.put(IDBConstants.PLANT, plant);
												htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PE);
												htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
												htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
												htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
												htRecvHis.put(IDBConstants.CREATED_BY, username);
												htRecvHis.put("MOVTID", "");
												htRecvHis.put("RECID", "");
												htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1());
												if (!poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS,
															poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1() + "," + poEsthdr.getREMARK3());
												} else if (!poEsthdr.getRemark1().equals("") && poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getRemark1());
												} else if (poEsthdr.getRemark1().equals("") && !poEsthdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO() + "," + poEsthdr.getREMARK3());
												} else {
													htRecvHis.put(IDBConstants.REMARKS, poEsthdr.getCustName() +","+ multiPoEstHdr.getPOMULTIESTNO());
												}

												htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												boolean flagmv = movHisDao.insertIntoMovHis(htRecvHis);
												
												
												htRecvHis.clear();
												htRecvHis.put(IDBConstants.PLANT, plant);
												htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
												htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
												htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
												htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
												htRecvHis.put(IDBConstants.CREATED_BY, username);
												htRecvHis.put("MOVTID", "");
												htRecvHis.put("RECID", "");
												htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1());
												if (!pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS,
															pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1() + "," + pohdr.getREMARK3());
												} else if (!pohdr.getRemark1().equals("") && pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getRemark1());
												} else if (pohdr.getRemark1().equals("") && !pohdr.getREMARK3().equals("")) {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO() + "," + pohdr.getREMARK3());
												} else {
													htRecvHis.put(IDBConstants.REMARKS, pohdr.getCustName() +","+ poEsthdr.getPOESTNO());
												}

												htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
												htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												boolean flagfs = movHisDao.insertIntoMovHis(htRecvHis);
												
												//Hashtable htMaster = new Hashtable();
												if (flagmv && flagfs) {
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagmv = movHisDao.insertIntoMovHis(htRecvHis);
														}
													}
													int vi = 0;
													for (PoEstDet pordendet : podetlist) {
														int index = Integer.parseInt("0");

														//int Polno = vi + 1;
														int Polno = pordendet.getPOESTLNNO();
														Hashtable<String, String> htRemarks = new Hashtable<>();
														htRemarks.put(IDBConstants.PLANT, plant);
														htRemarks.put(IDBConstants.PODET_POESTNUM, poEsthdr.getPOESTNO());
														htRemarks.put(IDBConstants.PODET_POESTLNNO, Integer.toString(Polno));
														htRemarks.put(IDBConstants.PODET_ITEM, pordendet.getITEM());
														if (!new PoEstDetDAO().isExisitPoMultiRemarks(htRemarks)) {
															htRemarks.put(IDBConstants.REMARKS, "");
															htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
															htRemarks.put(IDBConstants.CREATED_BY, username);
															flagmv = new PoEstUtil().savePoMultiRemarks(htRemarks);
														}
													}

													for (PoEstDet pordendet : podetlist) {
														htRecvHis.clear();
														htRecvHis.put(IDBConstants.PLANT, plant);
														htRecvHis.put("DIRTYPE", TransactionConstants.PE_ADD_ITEM);
														htRecvHis.put(IDBConstants.CUSTOMER_CODE, poEsthdr.getCustCode());
														htRecvHis.put(IDBConstants.POHDR_JOB_NUM, poEsthdr.getJobNum());
														htRecvHis.put(IDBConstants.ITEM, pordendet.getITEM());
														htRecvHis.put(IDBConstants.QTY, pordendet.getQTYOR());
														htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, poEsthdr.getPOESTNO());
														htRecvHis.put(IDBConstants.CREATED_BY, username);
														htRecvHis.put("MOVTID", "");
														htRecvHis.put("RECID", "");
														htRecvHis.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

														flagmv = movHisDao.insertIntoMovHis(htRecvHis);
													}
													
													//PO
													
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
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
															flagfs = movHisDao.insertIntoMovHis(htRecvHis);
														}
													}

													int vj = 0;
													for (PoDet pordendet : polistdet) {
														int index = Integer.parseInt("0");

														//int Polno = vj + 1;
														int Polno = pordendet.getPOLNNO();
														Hashtable<String, String> htRemarks = new Hashtable<>();
														htRemarks.put(IDBConstants.PLANT, plant);
														htRemarks.put(IDBConstants.PODET_PONUM, pohdr.getPONO());
														htRemarks.put(IDBConstants.PODET_POLNNO, Integer.toString(Polno));
														htRemarks.put(IDBConstants.PODET_ITEM, pordendet.getITEM());
														if (!new PoDetDAO().isExisitPoMultiRemarks(htRemarks)) {
															htRemarks.put(IDBConstants.REMARKS, "");
															htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
															htRemarks.put(IDBConstants.CREATED_BY, username);
															flagfs = new POUtil().savePoMultiRemarks(htRemarks);
														}
													}

													for (PoDet porddet : polistdet) {
														htRecvHis.clear();
														htRecvHis.put(IDBConstants.PLANT, plant);
														htRecvHis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
														htRecvHis.put(IDBConstants.CUSTOMER_CODE, pohdr.getCustCode());
														htRecvHis.put(IDBConstants.POHDR_JOB_NUM, pohdr.getJobNum());
														htRecvHis.put(IDBConstants.ITEM, porddet.getITEM());
														htRecvHis.put(IDBConstants.QTY, porddet.getQTYOR());
														htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pohdr.getPONO());
														htRecvHis.put(IDBConstants.CREATED_BY, username);
														htRecvHis.put("MOVTID", "");
														htRecvHis.put("RECID", "");
														htRecvHis.put(IDBConstants.TRAN_DATE,
																DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
														htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

														flagfs = movHisDao.insertIntoMovHis(htRecvHis);
													}
												}
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});

								}
							}
						}
					}
												
						//ORDER_STATUS Update - Azees 3.21
						if(draftstatus != 1) {
						if(flag) {
							Hashtable htCondition = new Hashtable();
							htCondition.put("PLANT", plant);
							htCondition.put("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());					
							flag = new multiPoEstDetDAO().isExisit(htCondition,"LNSTAT in ('O','N')");
							if (!flag){
								String updateHdr = "set STATUS='C',ORDER_STATUS='PROCESSED' ";
								flag = new MultiPoEstHdrDAO().updatePO(updateHdr, htCondition, "");
							} else {
								flag = new multiPoEstDetDAO().isExisit(htCondition,"LNSTAT in ('O')");
								if (!flag){
									flag = new multiPoEstDetDAO().isExisit(htCondition,"LNSTAT in ('C','N')");
									if (!flag){
										String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
										flag = new MultiPoEstHdrDAO().updatePO(updateHdr, htCondition, "");
									} else {
										flag = new multiPoEstDetDAO().isExisit(htCondition,"LNSTAT in ('C')");
										if (!flag){
											String updateHdr = "set STATUS='N',ORDER_STATUS='Open' ";
											flag = new MultiPoEstHdrDAO().updatePO(updateHdr, htCondition, "");									 
										} else {
										String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
										flag = new MultiPoEstHdrDAO().updatePO(updateHdr, htCondition, "");										
										}
									}
								} else {
									String updateHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
									flag = new MultiPoEstHdrDAO().updatePO(updateHdr, htCondition, "");	
								}						
								}
						}
							}
						
						 DbBean.CommitTran(ut);
						 String message = "Multi Purchase Estimate Updated Successfully.";
						if (ajax) {
							request.setAttribute("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
							/*
							 * EmailMsgUtil emailMsgUtil = new EmailMsgUtil(); Map<String, String> emailMsg
							 * = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER); String
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
							response.sendRedirect("../purchaseestimate/summary?msg=" + message);
						}
					}else {
						 DbBean.RollbackTran(ut);
						 String message = "Unable To Update Multi Purchase Estimate Order.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../purchaseestimate/summary?msg=" + message);
						}
					}
				}

			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				response.sendRedirect("../purchaseestimate/summary?msg=" + ThrowableUtil.getMessage(e));
			}

		}

		
		
	 if (action.equalsIgnoreCase("GET_PREVIOUS_ORDER_DETAILS")) {
		JSONObject jsonObjectResult = new JSONObject();
		jsonObjectResult = this.getPreviousOrderDetails(request);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObjectResult.toString());
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
		
		if (action.equalsIgnoreCase("Delete")) {
			JSONObject resultJson = new JSONObject();
			MultiPoUtil _multiPoUtil = new MultiPoUtil();

			String User_id = username;
			String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO")).trim();
			try {
				Hashtable htPoHrd = new Hashtable();
				htPoHrd.put(IDBConstants.PLANT, plant);
				htPoHrd.put(IDBConstants.PODET_POMULTIESTNUM, POMULTIESTNO);
//				sqlBean sqlbn = new sqlBean();
				boolean isValidOrder = new MultiPoEstHdrDAO().isExisit(htPoHrd, "");
				boolean isOrderInProgress = new multiPoEstDetDAO().isExisit(htPoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {
						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, POMULTIESTNO);
						if (!isExistsPaymentDetails) {
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("POMULTIESTNO", POMULTIESTNO);
//							String query = "pono,custCode";
//							ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = _multiPoUtil.removeRow(plant, POMULTIESTNO, User_id);
							if (value) {
								resultJson.put("MESSAGE", "Multi Purchase Estimate Order Deleted Successfully.");
								resultJson.put("ERROR_CODE", "100");
							} else {
								resultJson.put("MESSAGE", "Multi Purchase Estimate Not Deleted.");
								resultJson.put("ERROR_CODE", "97");
							}
						} else {
							resultJson.put("MESSAGE", "Multi Purchase Estimate Not Deleted.");
							resultJson.put("ERROR_CODE", "96");
						}
					} else {
						resultJson.put("MESSAGE", "Multi Purchase Estimate Not Deleted.");
						resultJson.put("ERROR_CODE", "98");
					}
				} else {
					resultJson.put("MESSAGE", "Multi Purchase Estimate Not Deleted.");
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

		
		if (action.equalsIgnoreCase("downloadAttachmentById")) {
			System.out.println("Attachments by ID");
			String ID = request.getParameter("attachid");
			FileHandling fileHandling = new FileHandling();
			List multipurchaseAttachment = null;
			try {

				multipurchaseAttachment = MultiPurchaseEstAttachDAO.getpurchaseAttachById(plant, ID);
				Map poAttach = (Map) multipurchaseAttachment.get(0);
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
				MultiPurchaseEstAttachDAO.deletemultipurchaseAttachByPrimId(plant, ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.getWriter().write("Deleted");
		}
		
		
		
		if (action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String POMULTIESTNO = request.getParameter("r_POMULTIESTNO");
				String polno = request.getParameter("r_lnno");
				boolean insertFlag = false;
				
				boolean isexist = MultiPoEstDetDAO.IsExistPoDetRemarks(plant, POMULTIESTNO, polno, item);
				if (isexist) {
					List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist = MultiPoEstDetDAO.GetPoDetRemarksbyitems(plant, POMULTIESTNO, polno, item);
					for (MultiPoEstDetRemarks multiPoEstDetRemarks : multiPoEstDetRemarkslist) {
						MultiPoEstDetDAO.DeletePoDetRemarks(plant, multiPoEstDetRemarks.getID_REMARKS());
					}
				}

				for (int i = 0; i < remarks.length; i++) {
					MultiPoEstDetRemarks multiPoEstDetRemarks = new MultiPoEstDetRemarks();
					multiPoEstDetRemarks.setREMARKS(remarks[i]);
					multiPoEstDetRemarks.setITEM(item);
					multiPoEstDetRemarks.setPOMULTIESTNO(POMULTIESTNO);
					multiPoEstDetRemarks.setPOMULTIESTLNNO(Integer.valueOf(polno));
					multiPoEstDetRemarks.setCRBY(username);
					multiPoEstDetRemarks.setPLANT(plant);
					multiPoEstDetRemarks.setCRAT(DateUtils.getDateTime());
					insertFlag = MultiPoEstDetDAO.addPoDetRemarks(multiPoEstDetRemarks);

					if (insertFlag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
						htRecvHis.put("ORDNUM", POMULTIESTNO);
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
					resultJson.put("MESSAGE", " Multi Purchase Estimate Order Remarks Created Successfully.");
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
		
		}

	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
//			String[] pathInfo = request.getPathInfo().split("/");
//			String action = pathInfo[1];
			String action = StrUtils.fString(request.getParameter("ACTION")).trim();
			if ("".equals(action) && request.getPathInfo() != null) {
				String[] pathInfo = request.getPathInfo().split("/");
				action = pathInfo[1];
			}
			System.out.println("action............." + action);
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
			String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
			MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
			multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();
			
			
			JSONObject jsonObjectResults = new JSONObject();
			if (action.equals("LOAD_MULTI_PRODUCT_LIST_BY_ITEM")) {
				jsonObjectResults = this.getMultiPoProductListbyProduct(request,"");
				response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObjectResults.toString());
	            response.getWriter().flush();
	            response.getWriter().close();
			 }
			
		
			if (action.equalsIgnoreCase("CheckOrderno")) {
				JSONObject jsonObjectResult = new JSONObject();
				String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
				try {
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PLANT, plant);
					ht.put("POMULTIESTNO", orderno);
					if (new MultiPoEstHdrDAO().isExisit(ht)) {
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
			
			
			if (action.equalsIgnoreCase("multipoestdetail")) {
				try {
					String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO"));
					request.setAttribute("POMULTIESTNO", POMULTIESTNO);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/multipurchaseEstDetail.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			if (action.equalsIgnoreCase("summary")) {
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
						if(StrUtils.fString(orderno).length() > 0)	ht.put("POMULTIESTNO", orderno);
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
						List<MultiPoEstHdr> multipoHeaders = multiPoEstHdrDAO.getPoHdrSummary(ht, fdate, tdate);
						if (multipoHeaders.size() > 0) {

							for (MultiPoEstHdr multiPoEstHdr : multipoHeaders) {
								JSONObject json = new JSONObject();
								List<MultiPoEstDet> podetail = MultiPoEstDetDAO.getPoDetByPono(plant, multiPoEstHdr.getPOMULTIESTNO());
								FinCountryTaxType fintaxtype = finCountryTaxTypeDAO
										.getCountryTaxTypesByid(multiPoEstHdr.getTAXID());
								
								json.put("DATE", multiPoEstHdr.getCollectionDate());
								json.put("POMULTIESTNO", multiPoEstHdr.getPOMULTIESTNO());
								 if(multiPoEstHdr.getSTATUS().equalsIgnoreCase("O"))
									json.put("STATUS", "PARTIALLY PROCESSED");
								else
									json.put("STATUS", multiPoEstHdr.getORDER_STATUS());
								json.put("COLLECTION_DATE", multiPoEstHdr.getCollectionDate());
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
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/multiPurchaseEstimateSummary.jsp");
					rd.forward(request, response);
				}
			}

			
			if (action.equalsIgnoreCase("edit")) {
				try {
					String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO"));
					request.setAttribute("POMULTIESTNO", POMULTIESTNO);
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditMultiPurchaseEstimate.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			if (action.equalsIgnoreCase("copy")) {
				try {
					String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO"));
					request.setAttribute("POMULTIESTNO", POMULTIESTNO);
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/MultipurchaseEstimateCopy.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			if (action.equalsIgnoreCase("convertpurchaseestimate")) {
				try {
					String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO"));
					request.setAttribute("POMULTIESTNO", POMULTIESTNO);
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/MultipurchaseEstimateToPurchaseEstimate.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		if(action.equalsIgnoreCase("new")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createMultiPurchaseEstimate.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
			
	
	if (action.equalsIgnoreCase("Auto-Generate")) {
		String POMULTIESTNO = "";
		TblControlDAO _TblControlDAO = new TblControlDAO();
		JSONObject json = new JSONObject();
		try {
			POMULTIESTNO = _TblControlDAO.getNextOrder(plant, username, IConstants.MULTIPOEST);
			json.put("POMULTIESTNO", POMULTIESTNO);
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
	
	
	
	if (action.equalsIgnoreCase("getPurchaseOrderRemarks")) {
		String POMULTIESTNO = "", POMULTIESTLNNO = "", item = "";
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJson = new JSONObject();
		try {
			POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO"));
			POMULTIESTLNNO = StrUtils.fString(request.getParameter("POLNO"));
			item = StrUtils.fString(request.getParameter("ITEM"));

			List<MultiPoEstDetRemarks> multiPoEstDetRemarkslist = MultiPoEstDetDAO.GetmultiPoEstDetRemarksbyitems(plant, POMULTIESTNO, POMULTIESTLNNO, item);
			if (multiPoEstDetRemarkslist.size() > 0) {
				for (MultiPoEstDetRemarks multiPoEstDetRemarks : multiPoEstDetRemarkslist) {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("remarks", multiPoEstDetRemarks.getREMARKS());
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
	
	
	

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	private JSONObject getEditPODetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
		multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();		
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String POMULTIESTNO = StrUtils.fString(request.getParameter("POMULTIESTNO")).trim();
			

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List<MultiPoEstDet> podetail = MultiPoEstDetDAO.getPoDetByPono(plant, POMULTIESTNO);
			MultiPoEstHdr poheader = multiPoEstHdrDAO.getPoHdrByPono(plant, POMULTIESTNO);
			if (podetail.size() > 0) {
				for (MultiPoEstDet multiPoEstDet : podetail) {
					JSONObject resultJsonInt = new JSONObject();
					String catlogpath = itemMstDAO.getcatlogpath(plant, multiPoEstDet.getITEM());
					String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath);

					resultJsonInt.put("LNNO", multiPoEstDet.getPOMULTIESTLNNO());
					resultJsonInt.put("LNSTATUS", multiPoEstDet.getLNSTAT());
					resultJsonInt.put("QTYRC", StrUtils.addZeroes(multiPoEstDet.getQTYRC().doubleValue(), "3"));
					resultJsonInt.put("ITEM", multiPoEstDet.getITEM());
					resultJsonInt.put("ITEMDESC", multiPoEstDet.getItemDesc());
					resultJsonInt.put("VNAME", multiPoEstDet.getCustName());
					resultJsonInt.put("VENDNO", multiPoEstDet.getCustCode());
					resultJsonInt.put("TAXTREATMENT", multiPoEstDet.getTAXTREATMENT());
					resultJsonInt.put("CURRENCY", multiPoEstDet.getCURRENCYID());

					resultJsonInt.put("CURRENCYID", multiPoEstDet.getCURRENCYID());
					resultJsonInt.put("CURRENCYUSEQT", multiPoEstDet.getCURRENCYUSEQT());
//					resultJsonInt.put("CURRENCYUSEQTOLD", multiPoEstDet.getCURRENCYUSEQT());
					resultJsonInt.put("ACCOUNTNAME", multiPoEstDet.getACCOUNT_NAME());
					resultJsonInt.put("UOM", multiPoEstDet.getUNITMO());
					resultJsonInt.put("QTY", StrUtils.addZeroes(multiPoEstDet.getQTYOR().doubleValue(), "3"));
					resultJsonInt.put("PDELDATE", multiPoEstDet.getPRODUCTDELIVERYDATE());
					resultJsonInt.put("EXPIREDATE", multiPoEstDet.getEXPIREDATE());
					resultJsonInt.put("UNITCOST", StrUtils.addZeroes(multiPoEstDet.getUNITCOST(), numberOfDecimal));
					resultJsonInt.put("ITEMDISCOUNTTYPE", multiPoEstDet.getDISCOUNT_TYPE());
					resultJsonInt.put("TAXTYPE", multiPoEstDet.getTAX_TYPE());
					resultJsonInt.put("CATLOGPATH", cpath);

					if (multiPoEstDet.getDISCOUNT_TYPE().equalsIgnoreCase("%")) {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(multiPoEstDet.getDISCOUNT(), "3"));
						double amount = (multiPoEstDet.getUNITCOST() * (multiPoEstDet.getQTYOR().doubleValue()))
								- (((multiPoEstDet.getUNITCOST() * (multiPoEstDet.getQTYOR().doubleValue())) / 100)
										* multiPoEstDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					} else {
						resultJsonInt.put("ITEMDISCOUNT", StrUtils.addZeroes(multiPoEstDet.getDISCOUNT(), numberOfDecimal));
						double amount = (multiPoEstDet.getUNITCOST() * (multiPoEstDet.getQTYOR().doubleValue()))
								- (multiPoEstDet.getDISCOUNT());
						resultJsonInt.put("AMOUNT", StrUtils.addZeroes(amount, numberOfDecimal));
					}
					String IBDiscount = new POUtil().getIBDiscountSelectedItemVNO(plant, multiPoEstDet.getCustCode(),
							multiPoEstDet.getITEM());
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
             	 	hts.put("item", multiPoEstDet.getITEM());
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
					List listItem = itemUtil.queryItemMstDetailsforpurchase(multiPoEstDet.getITEM(), plant);
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
						resultJsonInt.put("EstQty", estQty);
						resultJsonInt.put("AvlbQty", avlbQty);

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
				resultJson.put("VENDMST", jsonArray);
				resultJson.put("CURRENCYMST", jsonArray);
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

	private JSONObject getPreviousOrderDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        MultiPoUtil multiPoUtil = new MultiPoUtil();
//        StrUtils strUtils = new StrUtils();
        multiPoUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
		    String item = StrUtils.fString(request.getParameter("ITEM")).trim();
		    String rows = StrUtils.fString(request.getParameter("ROWS")).trim();
		    String uom = StrUtils.fString(request.getParameter("UOM")).trim();
		    Hashtable ht=new Hashtable();
//		    String extCond="";
		    ht.put("PLANT",plant);
		    ht.put("ITEM",item);
		    ht.put("VENDNO",custCode);
		    ht.put("UOM",uom);
		    if(rows.equalsIgnoreCase(""))
		    	rows = "1";
		    List listQry = multiPoUtil.getPreviousOrderDetails(ht,rows);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String POMULTIESTNO = (String)m.get("POMULTIESTNO");
				  String custcode = (String)m.get("CustCode");
				  String custName = (String)m.get("VNAME");
				  item = (String)m.get("ITEM");
				  String collectionDate = (String)m.get("CollectionDate");
				  String unitCost = (String)m.get("UNITCOST");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("POMULTIESTNO", POMULTIESTNO);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("CUSTNAME", custName);				  
				  resultJsonInt.put("ITEM", item);
				  resultJsonInt.put("COLLECTIONDATE", collectionDate);
				  resultJsonInt.put("UNITCOST", unitCost);			  
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
                 resultJson.put("orders", jsonArray);
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
	
	private JSONObject getMultiPoProductListbyProduct(HttpServletRequest request,String IsReport) {//By Imti-05.12.24 FOR POmulti modal
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			StrUtils strUtils = new StrUtils();
			ItemMstUtil itemUtil = new ItemMstUtil();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
			
			
			int HdrId=0,lnno=0;
			MultiPoEstHdr multiPoEstHdr = new MultiPoEstHdr();
			MultiPoEstDet multiPoEstDet = new MultiPoEstDet();
			MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
			multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();
			String POMULTIESTNO ="";
			MultiPoEstHdr poheader = multiPoEstHdrDAO.getPoHdrByDraft(plant, "","DRAFT");
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
			if(poheader.getPOMULTIESTNO()!=null){
				POMULTIESTNO = poheader.getPOMULTIESTNO();
				HdrId = poheader.getID();
				
				MultiPoEstDet podetail = MultiPoEstDetDAO.getPoDetByPonoItem(plant, POMULTIESTNO,sItem);
				if(podetail.getITEM()!=null){
						lnno = podetail.getPOMULTIESTLNNO();
						HdrId = podetail.getID();
						
						JSONObject resultJsonInt = new JSONObject();
						BigDecimal AMT = podetail.getQTYOR().multiply(new BigDecimal(podetail.getUNITCOST()));
						resultJsonInt.put("ITEM", podetail.getITEM());
						resultJsonInt.put("ITEMDESC", podetail.getItemDesc());
						resultJsonInt.put("VNAME", podetail.getCustName());
						resultJsonInt.put("VENDNO", podetail.getCustCode());
						resultJsonInt.put("CURRENCY", podetail.getCURRENCYID());
						resultJsonInt.put("EQCUR", podetail.getCURRENCYUSEQT());
						resultJsonInt.put("COST", StrUtils.addZeroes(podetail.getUNITCOST(), numberOfDecimal));
						resultJsonInt.put("QTY", StrUtils.addZeroes(podetail.getQTYOR().doubleValue(), "3"));
						resultJsonInt.put("FAMNT", StrUtils.addZeroes(AMT.doubleValue(), numberOfDecimal));
						resultJsonInt.put("PURCHASEUOM", podetail.getUNITMO());
						jsonArray.add(resultJsonInt);
						resultJson.put("items", jsonArray);
						resultJsonInt = new JSONObject();
						resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
						resultJsonInt.put("ERROR_CODE", "100");
						jsonArrayErr.add(resultJsonInt);
						resultJson.put("errors", jsonArrayErr);
					}
				} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
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

		