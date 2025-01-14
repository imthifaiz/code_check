package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.BillUtil;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.POUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class AddExpensesServlet
 */
@WebServlet("/AddExpensesServlet")
public class AddExpensesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ExpensesServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ExpensesServlet_PRINTPLANTMASTERINFO;
	String action = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddExpensesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("Submit")).trim();
		String baction = StrUtils.fString(request.getParameter("ACTION")).trim();
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equals("Save")) {
	    	  
	    	/* ExpenseHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", shipment = "", pono = "", expenseDate = "", paidThrough = "",cmd = "",tranid = "",newtranid = "",shipcmdexp="",taxamount="",refnum="",currencyuseqt="0",paidstatus="",
					currency = "", custno = "", ref = "", subTotal = "", totalAmount = "", expenseStatus = "",billable="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="";
			String	rvname = "" ,rvno="", rgrn = "",bid="",bstatus="",chkexpenses="",taxpercentage="",taxid="",projectid="";
			String etaxtreatment="",eREVERSECHARGE="",eGOODSIMPORT="",isgrn="";
			/*ExpenseDet*/
			List type = new ArrayList(), note = new ArrayList(), 
			taxType = new ArrayList(), amount = new ArrayList(),DETID= new ArrayList();			
			List<Hashtable<String,String>> expenseDetInfoList = null;
			List<Hashtable<String,String>> expenseAttachmentList = null;
			List<Hashtable<String,String>> expenseAttachmentInfoList = null;
			Hashtable<String,String> expenseDetInfo = null;
			Hashtable<String,String> expenseAttachment = null;
			UserTransaction ut = null;
			ExpensesUtil expensesUtil = new ExpensesUtil();
			DateUtils dateutils = new DateUtils();
			BillDAO billDAO = new BillDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isAdded = false;
			String result="";
			int typeCount  = 0, noteCount  = 0,	taxTypeCount  = 0, amountCount  = 0,DETIDCount  = 0;
			ExpensesDAO expensesDAO = new ExpensesDAO();
			POUtil pOUtil = new POUtil();
			CurrencyUtil currencyUtil = new CurrencyUtil();
			
			
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				expenseAttachmentList = new ArrayList<Hashtable<String,String>>();
				expenseAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					/* ExpenseHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendnoexp")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cmdexp")) {
							cmd = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("shipcmdexp")) {
							shipcmdexp = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("TranIdexp")) {
							tranid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODEexp")) {
							custno = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("ponoexp")) {
							pono = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_date")) {
							expenseDate = StrUtils.fString(fileItem.getString()).trim();
						}					
					
						if (fileItem.getFieldName().equalsIgnoreCase("shipmentexp")) {
							shipment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_for_PO")) {
							chkexpenses = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_name")) {
							paidThrough = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYIDexp")) {
							currency = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("referenceexp")) {
							ref = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("sub_totalexp")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amountexp")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_tax_amountexp")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_statusexp")) {
							expenseStatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rvname")) {
							rvname = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rvno")) {
							rvno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rgrn")) {
							rgrn = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billhdrid")) {
							bid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billhdrstatus")) {
							bstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billable_status")) {
							billable = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eTAXTREATMENT")) {
							taxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("refnumexp")) {
							refnum = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eREVERSECHARGE")) {
						sREVERSECHARGE=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eGOODSIMPORT")) {
						sGOODSIMPORT=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						
						//Bill
						if (fileItem.getFieldName().equalsIgnoreCase("rTAXTREATMENT")) {
							etaxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("rREVERSECHARGE")) {
						eREVERSECHARGE=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("rGOODSIMPORT")) {
						eGOODSIMPORT=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("risgrn")) {
							isgrn=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQTexp")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxidexp")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GSTexp")) {
							taxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTIDexp")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
					}
					
					if(tranid.isEmpty())
					{
						newtranid=expensesUtil.GetHDRMAXId(plant);
						int expid=Integer.parseInt(newtranid) +1;
						newtranid=String.valueOf(expid);
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Expenses" + "/"+ vendno + "/"+ newtranid;
						String filetempLocation = "C:/ATTACHMENTS/Expenses" + "/temp" + "/"+ vendno + "/"+ newtranid;
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "\\" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "\\"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment.put("PLANT", plant);
						expenseAttachment.put("FILETYPE", "");
						expenseAttachment.put("FILENAME", fileName);
						expenseAttachment.put("FILESIZE", "");
						expenseAttachment.put("FILEPATH", path + "\\" +fileName);
						expenseAttachment.put("CRAT",dateutils.getDateTime());
						expenseAttachment.put("CRBY",username);
						expenseAttachment.put("UPAT",dateutils.getDateTime());
						expenseAttachmentList.add(expenseAttachment);
					}
					
					/*ExpenseDet*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_account_name")) {
							type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
							typeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("note")) {
							note.add(noteCount,StrUtils.fString(fileItem.getString()).trim());
							noteCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("taxexp")) {
							/*taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;*/
								/*
								 * if(fileItem.getString().equalsIgnoreCase("EXEMPT") ||
								 * fileItem.getString().equalsIgnoreCase("OUT OF SCOPE")) {
								 * taxType.add(taxTypeCount,
								 * StrUtils.fString(fileItem.getString()+"[0.0%]").trim()); }else {
								 * taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim()); }
								 */
							
							taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amountexp")) {
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
				}
				}		
				if(!paidThrough.equals("")) {
					paidstatus="1";
				}else {
					paidstatus="0";
				}
				CoaDAO coaDAO=new CoaDAO();
				PoDetDAO podetDAO=new PoDetDAO();
				ItemMstDAO itemMSTDAO=new ItemMstDAO();
				
				//////////////////////
				expenseDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable expenseHdr =new Hashtable(); 
				expenseHdr.put("PLANT", plant);
				expenseHdr.put("VENDNO", vendno);
				expenseHdr.put("CUSTNO", custno);
				expenseHdr.put("PONO", pono);
				expenseHdr.put("SHIPMENT_CODE", shipment);
				expenseHdr.put("EXPENSES_DATE", expenseDate);
				expenseHdr.put("CURRENCYID", currency);
				expenseHdr.put("PAID_THROUGH", paidThrough);				
				expenseHdr.put("REFERENCE", ref);				
				expenseHdr.put("SUB_TOTAL", subTotal);
				expenseHdr.put("TOTAL_AMOUNT", totalAmount);
				expenseHdr.put("STATUS", expenseStatus);	
				expenseHdr.put("CRAT",dateutils.getDateTime());
				expenseHdr.put("CRBY",username);
				expenseHdr.put("UPAT",dateutils.getDateTime());
				expenseHdr.put("ISBILLABLE",billable);
				expenseHdr.put("TAXAMOUNT",taxamount);
				expenseHdr.put("TAXTREATMENT",taxtreatment);
				expenseHdr.put("REVERSECHARGE", sREVERSECHARGE);
				expenseHdr.put("GOODSIMPORT", sGOODSIMPORT);
				expenseHdr.put("ISPAID", paidstatus);
				expenseHdr.put("TAXID", taxid);
				expenseHdr.put("STANDARDTAX", taxpercentage);
				expenseHdr.put("PROJECTID", projectid);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int expenseHdrId=0;
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!tranid.isEmpty())
					{
						expenseHdr.put("ID",tranid);
						expenseHdr.put("UPBY",username);
						expenseHdrId = expensesUtil.updateExpensesHdr(expenseHdr);
					}
				}
				else
				 expenseHdrId = expensesUtil.addexpenseHdr(expenseHdr, plant);				
				if(expenseHdrId > 0) {
					for(int i =0 ; i < type.size() ; i++){
						
						String cuid = pOUtil.getCurrencyID(plant, pono);
						
						Hashtable ht1 = new Hashtable();
						ht1.put("CURRENCYID", currency);
						ht1.put("PLANT", plant);
						
						Hashtable ht2 = new Hashtable();
						ht2.put("CURRENCYID", cuid);
						ht2.put("PLANT", plant);
						
						String curtobase = currencyUtil.getCurrencyID(ht1, "CURRENCYUSEQT");
						String basetopocur = currencyUtil.getCurrencyID(ht2, "CURRENCYUSEQT");
						curtobase=currencyuseqt;
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						int lnno = i+1;
						expenseDetInfo = new Hashtable<String, String>();
						expenseDetInfo.put("PLANT", plant);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseDetInfo.put("EXPENSESHDRID", tranid);						
						else
						expenseDetInfo.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseDetInfo.put("EXPENSES_ACCOUNT", (String) type.get(i));
						expenseDetInfo.put("DESCRIPTION", (String) note.get(i));
						expenseDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						expenseDetInfo.put("AMOUNT", convAmount);
						expenseDetInfo.put("CRAT",dateutils.getDateTime());
						expenseDetInfo.put("CRBY",username);
						expenseDetInfo.put("UPAT",dateutils.getDateTime());
						expenseDetInfo.put("CURRENCYTOBASE", curtobase);
						expenseDetInfo.put("BASETOORDERCURRENCY", basetopocur);
						expenseDetInfoList.add(expenseDetInfo);
					}
					if(cmd.equalsIgnoreCase("Edit"))
					{
						if(!tranid.isEmpty())
						{
							expenseDetInfo.put("UPBY",username);
							isAdded = expensesDAO.deleteExpenseDet(plant, tranid);
							if(isAdded)
							isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
						}
					}
					else					
					isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
					
					if(expenseAttachmentList!=null) {
						int attchSize = expenseAttachmentList.size();
						for(int i =0 ; i < attchSize ; i++){
							expenseAttachment = new Hashtable<String, String>();
							expenseAttachment = expenseAttachmentList.get(i);
							if(cmd.equalsIgnoreCase("Edit"))
								expenseAttachment.put("EXPENSESHDRID", tranid);
							else
							expenseAttachment.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
							expenseAttachmentInfoList.add(expenseAttachment);
						}
					}
					
					VendMstDAO vendorDAO=new VendMstDAO();
					if(isAdded) {
						if(expenseAttachmentInfoList.size() > 0)
							isAdded = expensesUtil.addExpenseAttachments(expenseAttachmentInfoList, plant);
						
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(expenseDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						if( !shipment.equals("")) {
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+pono+")");
						}else {
							journalHead.setTRANSACTION_TYPE("EXPENSE");
						}
						if(cmd.equalsIgnoreCase("Edit"))
						{
							journalHead.setTRANSACTION_ID(tranid);
						}
						else
						{
							journalHead.setTRANSACTION_ID(Integer.toString(expenseHdrId));
						}
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						
						if(vendno!=null && !vendno.equals("") && (paidThrough!=null && paidThrough.equals("") || !paidThrough.equals(""))) {
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								journalDetails.add(journalDetail);
							}
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
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
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
							
						}if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
								}
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_3);
								
							}
						
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_Paidthrough);
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
						
						if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
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
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								
							}else {
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								paid_journaldetails.add(journalDetail_3);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						else if(vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								paid_journaldetails.add(journalDetail);
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
								paid_journaldetails.add(journalDetail_2);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
							/*if(pono!=null && !"".equals(pono)) {
							
							List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
							JournalDetail inventorySelected=new JournalDetail();
							inventorySelected.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
							inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							inventorySelected.setACCOUNT_NAME("Inventory Asset");
							inventorySelected.setDEBITS(Double.parseDouble(totalAmount));
							expensepo_journaldetails.add(inventorySelected);
							
							for(Map expDet:expenseDetInfoList)
							{
								JournalDetail expensesSelected=new JournalDetail();
								expensesSelected.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								expensesSelected.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								expensesSelected.setCREDITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								expensepo_journaldetails.add(expensesSelected);
							}
							journal.setJournalDetails(expensepo_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO");
							Journal journal_expense_po=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journal_expense_po.getJournalHeader()!=null)
							{
								if(journal_expense_po.getJournalHeader().getID()>0)
								{
									journalHead.setID(journal_expense_po.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
								}
								else
								{
									journalService.addJournal(journal, username);
									
									
								}
						
							}
							
						}*/
					}
					if(isAdded) {
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit"))
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);	
						else
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));														
						htMovHis.put(IDBConstants.ITEM, paidThrough);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS", shipment);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, plant);
						htMovChk.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
						htMovChk.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));
						htMovChk.put(IDBConstants.ITEM, paidThrough);					
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, pono);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%EXPENSES%' ");
						if(!isAdded)
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
					if(isAdded) {
						if(shipcmdexp.equalsIgnoreCase("Edit")) {
							String query = " SET SHIPMENT_CODE='"+shipment+"' ";
							Hashtable ht = new Hashtable();
							ht.put("ID", bid);
							ht.put("PLANT", plant);
							billDAO.updateBillHdr(query, ht, "");
						}
					}
					
				}
				if(isAdded) {
					DbBean.CommitTran(ut);

					if(cmd.equalsIgnoreCase("Edit"))
						result = "Expenses updated successfully";
					else
					result = "Expenses created successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Expenses not created";
				}
				
				JSONObject expense=new JSONObject();
				expense.put("shipmentcode",shipment);
				expense.put("pono",pono);
				if(bstatus.equalsIgnoreCase("editbill")) {
					expense.put("billtype","editbill");
				}else {
					expense.put("billtype","createbill");
				}
				response.getWriter().write(expense.toString());
				
				
				/*if(bstatus.equalsIgnoreCase("editbill")) {
					response.sendRedirect("jsp/editBill.jsp?action=View&BILL_HDR="+bid);
				}else {
					response.sendRedirect("jsp/createBill.jsp?action=View&PONO="+pono+"&GRNO="+rgrn+"&VEND_NAME="+rvname+"&VENDNO="+rvno+"&shipmentcode="+shipment+"&gbill=bill&TAXTREATMENT="+etaxtreatment+"&REVERSECHARGE="+eREVERSECHARGE+"&GOODSIMPORT="+eGOODSIMPORT+"&isgrn="+isgrn+"&refnum="+refnum);
				}*/
				
				
			
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
			}
	    }else if (action.equals("Save_inv")) {

	    	  
	    	/* ExpenseHdr*/
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String vendno = "", shipment = "", ponon = "", expenseDate = "", paidThrough = "",cmd = "",tranid = "",newtranid = "",shipcmdexp="",taxamount="",refnum="",currencyuseqt="0",paidstatus="",
					currency = "", custno = "", ref = "", subTotal = "", totalAmount = "", expenseStatus = "",billable="",taxtreatment="",sREVERSECHARGE="",sGOODSIMPORT="";
			String	rvname = "" ,rvno="", rgrn = "",bid="",bstatus="",chkexpenses="",taxpercentage="",taxid="",projectid="";
			String etaxtreatment="",eREVERSECHARGE="",eGOODSIMPORT="",isgrn="",bill="";
			/*ExpenseDet*/
			List type = new ArrayList(), note = new ArrayList(), 
			taxType = new ArrayList(), amount = new ArrayList(),DETID= new ArrayList();			
			List<Hashtable<String,String>> expenseDetInfoList = null;
			List<Hashtable<String,String>> expenseAttachmentList = null;
			List<Hashtable<String,String>> expenseAttachmentInfoList = null;
			Hashtable<String,String> expenseDetInfo = null;
			Hashtable<String,String> expenseAttachment = null;
			UserTransaction ut = null;
			ExpensesUtil expensesUtil = new ExpensesUtil();
			DateUtils dateutils = new DateUtils();
			BillDAO billDAO = new BillDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			boolean isAdded = false;
			String result="";
			int typeCount  = 0, noteCount  = 0,	taxTypeCount  = 0, amountCount  = 0,DETIDCount  = 0;
			ExpensesDAO expensesDAO = new ExpensesDAO();
			POUtil pOUtil = new POUtil();
			BillUtil billUtil = new BillUtil();
			CurrencyUtil currencyUtil = new CurrencyUtil();
			
			
			try{
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);				
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				StrUtils strUtils = new StrUtils();
				expenseAttachmentList = new ArrayList<Hashtable<String,String>>();
				expenseAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
				
				while (iterator.hasNext()) {
					FileItem fileItem = (FileItem) iterator.next();
					System.out.println(fileItem.getFieldName());
					/* ExpenseHdr*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("vendnoexpinv")) {
							vendno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("cmdexpinv")) {
							cmd = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("shipcmdexpinv")) {
							shipcmdexp = StrUtils.fString(fileItem.getString()).trim();
						}
			
						if (fileItem.getFieldName().equalsIgnoreCase("TranIdexpinv")) {
							tranid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("CUST_CODEexpinv")) {
							custno = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("billexpinv")) {
							bill = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_dateinv")) {
							expenseDate = StrUtils.fString(fileItem.getString()).trim();
						}					
					
						if (fileItem.getFieldName().equalsIgnoreCase("shipmentexpinv")) {
							shipment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_for_POinv")) {
							chkexpenses = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("paid_through_account_nameinv")) {
							paidThrough = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYIDexpinv")) {
							currency = StrUtils.fString(fileItem.getString()).trim();
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("referenceexpinv")) {
							ref = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("sub_totalexpinv")) {
							//subTotal = StrUtils.fString(fileItem.getString()).trim();
							subTotal = String.valueOf(( Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("total_amountexpinv")) {
							//totalAmount = StrUtils.fString(fileItem.getString()).trim();
							totalAmount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("total_tax_amountexpinv")) {
							//taxamount = StrUtils.fString(fileItem.getString()).trim();
							taxamount = String.valueOf((Float.parseFloat(StrUtils.fString(fileItem.getString()).trim())) / (Float.parseFloat(currencyuseqt)) );
						}
					
						if (fileItem.getFieldName().equalsIgnoreCase("bill_statusexpinv")) {
							expenseStatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rvnameinv")) {
							rvname = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rvnoinv")) {
							rvno = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("rgrninv")) {
							rgrn = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billhdridinv")) {
							bid = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billhdrstatusinv")) {
							bstatus = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("billable_statusinv")) {
							billable = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eTAXTREATMENTinv")) {
							taxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("refnumexpinv")) {
							refnum = StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eREVERSECHARGEinv")) {
						sREVERSECHARGE=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("eGOODSIMPORTinv")) {
						sGOODSIMPORT=StrUtils.fString((fileItem.getString() != null) ? "1": "0").trim();
						}
						
						//Bill
						if (fileItem.getFieldName().equalsIgnoreCase("rTAXTREATMENTinv")) {
							etaxtreatment = StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("rREVERSECHARGEinv")) {
						eREVERSECHARGE=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("rGOODSIMPORTinv")) {
						eGOODSIMPORT=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("risgrninv")) {
							isgrn=StrUtils.fString(fileItem.getString()).trim();
						}
						if (fileItem.getFieldName().equalsIgnoreCase("CURRENCYUSEQTexpinv")) {
							currencyuseqt=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("taxidexpinv")) {
							taxid=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("GSTexpinv")) {
							taxpercentage=StrUtils.fString(fileItem.getString()).trim();
						}
						
						if (fileItem.getFieldName().equalsIgnoreCase("PROJECTIDexpinv")) {
							if(!StrUtils.fString(fileItem.getString()).trim().equalsIgnoreCase("")) {
								projectid = StrUtils.fString(fileItem.getString()).trim();
							}
						}
					}
					
					if(tranid.isEmpty())
					{
						newtranid=expensesUtil.GetHDRMAXId(plant);
						int expid=Integer.parseInt(newtranid) +1;
						newtranid=String.valueOf(expid);
					}
					
					if (!fileItem.isFormField() && (fileItem.getName().length() > 0)) {
						String fileLocation = "C:/ATTACHMENTS/Expenses" + "/"+ vendno + "/"+ newtranid;
						String filetempLocation = "C:/ATTACHMENTS/Expenses" + "/temp" + "/"+ vendno + "/"+ newtranid;
						String fileName = StrUtils.fString(fileItem.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocation);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "\\" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						fileItem.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "\\"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						expenseAttachment = new Hashtable<String, String>();
						expenseAttachment.put("PLANT", plant);
						expenseAttachment.put("FILETYPE", "");
						expenseAttachment.put("FILENAME", fileName);
						expenseAttachment.put("FILESIZE", "");
						expenseAttachment.put("FILEPATH", path + "\\" +fileName);
						expenseAttachment.put("CRAT",dateutils.getDateTime());
						expenseAttachment.put("CRBY",username);
						expenseAttachment.put("UPAT",dateutils.getDateTime());
						expenseAttachmentList.add(expenseAttachment);
					}
					
					/*ExpenseDet*/
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("expenses_account_nameinv")) {
							type.add(typeCount, StrUtils.fString(fileItem.getString()).trim());
							typeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("noteinv")) {
							note.add(noteCount,StrUtils.fString(fileItem.getString()).trim());
							noteCount++;
						}
					}
					
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("taxexpinv")) {
							/*taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;*/
								/*
								 * if(fileItem.getString().equalsIgnoreCase("EXEMPT") ||
								 * fileItem.getString().equalsIgnoreCase("OUT OF SCOPE")) {
								 * taxType.add(taxTypeCount,
								 * StrUtils.fString(fileItem.getString()+"[0.0%]").trim()); }else {
								 * taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim()); }
								 */
							
							taxType.add(taxTypeCount, StrUtils.fString(fileItem.getString()).trim());
							taxTypeCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("amountexpinv")) {
							amount.add(amountCount, StrUtils.fString(fileItem.getString()).trim());
							amountCount++;
						}
					}
					if (fileItem.isFormField()) {
						if (fileItem.getFieldName().equalsIgnoreCase("DETIDinv")) {
							DETID.add(DETIDCount, StrUtils.fString(fileItem.getString()).trim());
							DETIDCount++;
						}
					}
				}
				}		
				if(!paidThrough.equals("")) {
					paidstatus="1";
				}else {
					paidstatus="0";
				}
				CoaDAO coaDAO=new CoaDAO();
				PoDetDAO podetDAO=new PoDetDAO();
				ItemMstDAO itemMSTDAO=new ItemMstDAO();
				
				//////////////////////
				expenseDetInfoList = new ArrayList<Hashtable<String,String>>();
				Hashtable expenseHdr =new Hashtable(); 
				expenseHdr.put("PLANT", plant);
				expenseHdr.put("VENDNO", vendno);
				expenseHdr.put("CUSTNO", custno);
				expenseHdr.put("PONO", ponon);
				expenseHdr.put("BILL", bill);
				expenseHdr.put("SHIPMENT_CODE", shipment);
				expenseHdr.put("EXPENSES_DATE", expenseDate);
				expenseHdr.put("CURRENCYID", currency);
				expenseHdr.put("PAID_THROUGH", paidThrough);				
				expenseHdr.put("REFERENCE", ref);				
				expenseHdr.put("SUB_TOTAL", subTotal);
				expenseHdr.put("TOTAL_AMOUNT", totalAmount);
				expenseHdr.put("STATUS", expenseStatus);	
				expenseHdr.put("CRAT",dateutils.getDateTime());
				expenseHdr.put("CRBY",username);
				expenseHdr.put("UPAT",dateutils.getDateTime());
				expenseHdr.put("ISBILLABLE",billable);
				expenseHdr.put("TAXAMOUNT",taxamount);
				expenseHdr.put("TAXTREATMENT",taxtreatment);
				expenseHdr.put("REVERSECHARGE", sREVERSECHARGE);
				expenseHdr.put("GOODSIMPORT", sGOODSIMPORT);
				expenseHdr.put("ISPAID", paidstatus);
				expenseHdr.put("TAXID", taxid);
				expenseHdr.put("STANDARDTAX", taxpercentage);
				expenseHdr.put("PROJECTID", projectid);
				/*Get Transaction object*/
				ut = DbBean.getUserTranaction();				
				/*Begin Transaction*/
				ut.begin();
				int expenseHdrId=0;
				if(cmd.equalsIgnoreCase("Edit"))
				{
					if(!tranid.isEmpty())
					{
						expenseHdr.put("ID",tranid);
						expenseHdr.put("UPBY",username);
						expenseHdrId = expensesUtil.updateExpensesHdr(expenseHdr);
					}
				}
				else
				 expenseHdrId = expensesUtil.addexpenseHdr(expenseHdr, plant);				
				if(expenseHdrId > 0) {
					for(int i =0 ; i < type.size() ; i++){
						
						String cuid = billUtil.getCurrencyID(plant, bill);
						
						Hashtable ht1 = new Hashtable();
						ht1.put("CURRENCYID", currency);
						ht1.put("PLANT", plant);
						
						Hashtable ht2 = new Hashtable();
						ht2.put("CURRENCYID", cuid);
						ht2.put("PLANT", plant);
						
						String curtobase = currencyUtil.getCurrencyID(ht1, "CURRENCYUSEQT");
						String basetopocur = currencyUtil.getCurrencyID(ht2, "CURRENCYUSEQT");
						curtobase=currencyuseqt;
						String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
						int lnno = i+1;
						expenseDetInfo = new Hashtable<String, String>();
						expenseDetInfo.put("PLANT", plant);
						if(cmd.equalsIgnoreCase("Edit"))
							expenseDetInfo.put("EXPENSESHDRID", tranid);						
						else
						expenseDetInfo.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
						expenseDetInfo.put("EXPENSES_ACCOUNT", (String) type.get(i));
						expenseDetInfo.put("DESCRIPTION", (String) note.get(i));
						expenseDetInfo.put("TAX_TYPE", (String) taxType.get(i));
						expenseDetInfo.put("AMOUNT", convAmount);
						expenseDetInfo.put("CRAT",dateutils.getDateTime());
						expenseDetInfo.put("CRBY",username);
						expenseDetInfo.put("UPAT",dateutils.getDateTime());
						expenseDetInfo.put("CURRENCYTOBASE", curtobase);
						expenseDetInfo.put("BASETOORDERCURRENCY", basetopocur);
						expenseDetInfoList.add(expenseDetInfo);
					}
					if(cmd.equalsIgnoreCase("Edit"))
					{
						if(!tranid.isEmpty())
						{
							expenseDetInfo.put("UPBY",username);
							isAdded = expensesDAO.deleteExpenseDet(plant, tranid);
							if(isAdded)
							isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
						}
					}
					else					
					isAdded = expensesUtil.addMultipleExpenseDet(expenseDetInfoList, plant);
					
					if(expenseAttachmentList!=null) {
						int attchSize = expenseAttachmentList.size();
						for(int i =0 ; i < attchSize ; i++){
							expenseAttachment = new Hashtable<String, String>();
							expenseAttachment = expenseAttachmentList.get(i);
							if(cmd.equalsIgnoreCase("Edit"))
								expenseAttachment.put("EXPENSESHDRID", tranid);
							else
							expenseAttachment.put("EXPENSESHDRID", Integer.toString(expenseHdrId));
							expenseAttachmentInfoList.add(expenseAttachment);
						}
					}
					
					VendMstDAO vendorDAO=new VendMstDAO();
					if(isAdded) {
						if(expenseAttachmentInfoList.size() > 0)
							isAdded = expensesUtil.addExpenseAttachments(expenseAttachmentInfoList, plant);
						
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(expenseDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						if( !shipment.equals("")) {
							journalHead.setTRANSACTION_TYPE("EXPENSE FOR BILL("+bill+")");
						}else {
							journalHead.setTRANSACTION_TYPE("EXPENSE");
						}
						if(cmd.equalsIgnoreCase("Edit"))
						{
							journalHead.setTRANSACTION_ID(tranid);
						}
						else
						{
							journalHead.setTRANSACTION_ID(Integer.toString(expenseHdrId));
						}
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);
						
						List<JournalDetail> journalDetails=new ArrayList<>();
						
						if(vendno!=null && !vendno.equals("") && (paidThrough!=null && paidThrough.equals("") || !paidThrough.equals(""))) {
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								journalDetails.add(journalDetail);
							}
							JournalDetail journalDetail_1=new JournalDetail();
							journalDetail_1.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
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
								/*JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");*/
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
							
						}if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
								if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
								}
							}
							if(coaJson1.isEmpty() || coaJson1.isNullObject())
							{
								
							}
							else
							{
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								journalDetails.add(journalDetail_3);
								
							}
						
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_Paidthrough);
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
						
						if(vendno!=null && !vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
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
							if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
								
							}else {
								journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
								if(coaJson1.getString("account_name")!=null) {
									journalDetail_3.setACCOUNT_NAME(coaJson1.getString("account_name"));
								}
								journalDetail_3.setDEBITS(Double.parseDouble(totalAmount));
								paid_journaldetails.add(journalDetail_3);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						
						else if(vendno.equals("") && paidThrough!=null && !paidThrough.equals("")) {
							List<JournalDetail> paid_journaldetails=new ArrayList<>();
							
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							for(Map expDet:expenseDetInfoList)
							{
								
								JournalDetail journalDetail=new JournalDetail();
								journalDetail.setPLANT(plant);
								JSONObject coaJson=coaDAO.getCOAByName(plant, (String) expDet.get("EXPENSES_ACCOUNT"));
								System.out.println("Json"+coaJson.toString());
								journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
								journalDetail.setACCOUNT_NAME((String) expDet.get("EXPENSES_ACCOUNT"));
								journalDetail.setDEBITS(Double.parseDouble(expDet.get("AMOUNT").toString()));
								paid_journaldetails.add(journalDetail);
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
								paid_journaldetails.add(journalDetail_2);
							}
							
							JournalDetail journalDetail_Paidthrough=new JournalDetail();
							journalDetail_Paidthrough.setPLANT(plant);
							JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, (String) paidThrough);
							journalDetail_Paidthrough.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
							journalDetail_Paidthrough.setACCOUNT_NAME((String) paidThrough);
							journalDetail_Paidthrough.setCREDITS(Double.parseDouble(totalAmount));
							paid_journaldetails.add(journalDetail_Paidthrough);
							
							journal.setJournalDetails(paid_journaldetails);
							journalHead.setTRANSACTION_TYPE("EXPENSE PAID");
							Journal journalPaid=journalService.getJournalByTransactionId(plant, journalHead.getTRANSACTION_ID(),journalHead.getTRANSACTION_TYPE());
							if(journalPaid.getJournalHeader()!=null)
							{
								if(journalPaid.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalPaid.getJournalHeader().getID());
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
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);
						if(cmd.equalsIgnoreCase("Edit"))
							htMovHis.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);	
						else
							htMovHis.put("DIRTYPE", TransactionConstants.CREATE_EXPENSES);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));														
						htMovHis.put(IDBConstants.ITEM, paidThrough);
						htMovHis.put("RECID", "");
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, bill);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS", shipment);
						
						Hashtable htMovChk = new Hashtable();
						htMovChk.clear();
						htMovChk.put(IDBConstants.PLANT, plant);
						htMovChk.put("DIRTYPE", TransactionConstants.EDIT_EXPENSES);
						htMovChk.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(expenseDate));
						htMovChk.put(IDBConstants.ITEM, paidThrough);					
						htMovChk.put(IDBConstants.MOVHIS_ORDNUM, bill);
						isAdded = movHisDao.isExisit(htMovChk," DIRTYPE LIKE '%EXPENSES%' ");
						if(!isAdded)
						isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
					}
					if(isAdded) {
						if(shipcmdexp.equalsIgnoreCase("Edit")) {
							String query = " SET SHIPMENT_CODE='"+shipment+"' ";
							Hashtable ht = new Hashtable();
							ht.put("ID", bid);
							ht.put("PLANT", plant);
							billDAO.updateBillHdr(query, ht, "");
						}
					}
					
				}
				if(isAdded) {
					DbBean.CommitTran(ut);

					if(cmd.equalsIgnoreCase("Edit"))
						result = "Expenses updated successfully";
					else
					result = "Expenses created successfully";
				}
				else {
					DbBean.RollbackTran(ut);
					result = "Expenses not created";
				}
				
				JSONObject expense=new JSONObject();
				expense.put("shipmentcode",shipment);
				expense.put("bill",bill);
				if(bstatus.equalsIgnoreCase("editbill")) {
					expense.put("billtype","editbill");
				}else {
					expense.put("billtype","createbill");
				}
				response.getWriter().write(expense.toString());
				
				
				/*if(bstatus.equalsIgnoreCase("editbill")) {
					response.sendRedirect("jsp/editBill.jsp?action=View&BILL_HDR="+bid);
				}else {
					response.sendRedirect("jsp/createBill.jsp?action=View&PONO="+pono+"&GRNO="+rgrn+"&VEND_NAME="+rvname+"&VENDNO="+rvno+"&shipmentcode="+shipment+"&gbill=bill&TAXTREATMENT="+etaxtreatment+"&REVERSECHARGE="+eREVERSECHARGE+"&GOODSIMPORT="+eGOODSIMPORT+"&isgrn="+isgrn+"&refnum="+refnum);
				}*/
				
				
			
			}catch (Exception e) {
				 DbBean.RollbackTran(ut);
			}
	    
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
