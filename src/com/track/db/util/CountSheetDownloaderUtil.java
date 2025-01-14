package com.track.db.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

import com.track.DataDownloader.POIReadExcel;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EmployeeLeaveDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.HrEmpSalaryDAO;
import com.track.dao.HrEmpSalaryDetDAO;
import com.track.dao.HrEmpTypeDAO;
import com.track.dao.HrHolidayMstDAO;
import com.track.dao.HrLeaveTypeDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.LocMstThreeDAO;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToHdrDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.BillHdr;
import com.track.db.object.BlockingDates;
import com.track.db.object.EmployeeLeaveDET;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinProject;
import com.track.db.object.HrHolidayMst;
import com.track.db.object.HrLeaveApplyDet;
import com.track.db.object.HrLeaveType;
import com.track.db.object.PoDet;
import com.track.gates.DbBean;
import com.track.gates.encryptBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.service.HrHolidayMstService;
import com.track.service.HrLeaveApplyDetService;
import com.track.service.HrLeaveApplyHdrService;
import com.track.serviceImplementation.HrHolidayMstServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyDetServiceImpl;
import com.track.serviceImplementation.HrLeaveApplyHdrServiceImpl;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Hashtable;
import java.util.Locale;

import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;

public class CountSheetDownloaderUtil {
	
	// database table column lengths
	private final static int LOC_LEN = 50;
	private final static int LOC_DESC_LEN = 100;
	private final static int LOC_TYPE_ID = 50;
	private final static int LOC_REMARK_LEN = 100;
	private final static int LOC_ADD1_LEN = 100;
	private final static int LOC_ADD2_LEN = 100;
	private final static int LOC_ADD3_LEN = 100;
	private final static int LOC_ADD4_LEN = 100;
	private final static int LOC_STATE_LEN = 50;
	private final static int LOC_COUNTRY_LEN = 50;
	private final static int LOC_ZIP_LEN = 50;
	private final static int LOC_TELNO_LEN = 30;
	private final static int LOC_FAX_LEN = 30;
	private final static int LOC_COMNAME_LEN = 100;
	private final static int LOC_RCBNO_LEN = 50;
	
	
	
	private final static int ITEM_LEN = 50;
	private final static int ITEM_SUP = 20;
	private final static int ITEM_PRD_SUP = 50;
	private final static int BATCH_LEN = 40;
	
	private final static int ITEM_DESC_LEN = 100;
	private final static int ADD_ITEM_DESC_LEN = 1000;
	private final static int PRD_CLS_ID_LEN = 100;
	private final static int ITEM_TYPE_LEN = 100;
	private final static int ITEM_BRAND_LEN = 100;
	private final static int STKUOM_LEN = 14;
	private final static int PURCHASEUOM_LEN = 14;
	private final static int SALESUOM_LEN = 14;
	private final static int RENTALUOM_LEN = 14;
	private final static int SERVICEUOM_LEN = 14;
	private final static int INVENTORYUOM_LEN = 14;
	private final static int APPLYUOM_LEN = 10;
	private final static int NETWEIGHT_LEN = 50;
	private final static int GROSSWEIGHT_LEN = 50;
	private final static int DIMENSION_LEN = 50;
	private final static int HSCODE_LEN = 50;
	private final static int COO_LEN = 50;
	private final static int VINNO_LEN = 50;
    private final static int MODEL_LEN=50;
	private final static int MANUFACTURER_LEN = 100;
	private final static int RENTALPRICE_LEN = 50;
	private final static int SERVICEPRICE_LEN = 50;
	private final static int REMARKS3_LEN = 100;
	private final static int REMARKS4_LEN = 200;
	private final static int DET_DESC_LEN = 100;
	
	private final static int PONO_LEN = 20;
        private final static int PONO_REFNO = 20;
	private final static int OREDER_TYPE_LEN = 20;
	private final static int PO_REMARK_LEN = 100;
	
        
	
	private final static int UOM = 14;
	
	private final static int DONO_LEN = 20;
	private final static int LNNO_LEN = 4;
	private final static int DO_REMARK_LEN = 100;
	private final static int DO_REMARK_LEN1 = 100;
	private final static int DO_REMARK_LEN2 = 100;
	private final static int DO_CUSTOMER_CODE_LEN = 50;
	private final static int DO_CUSTOMER_LEN = 100;
    private final static int DO_REF_LEN = 20;
	private final static int TONO_LEN = 50;
	private final static int TO_REMARK_LEN = 100;
    private final static int TO_REF_LEN = 15;
	private final static int WONO_LEN = 50;
	private final static int WO_REMARK_LEN = 100;
	private final static int WO_CUSTOMER_CODE_LEN = 50;
	private final static int WO_REF_LEN = 25;
        
        //Supplier & Customer
        
         private final static int VENDNO_LEN = 50;
         private final static int VNAME_LEN = 100;
         
         private final static int CUSTNO_LEN = 50;
         private final static int CNAME_LEN = 100;
         private final static int EMPTYPE_LEN = 50;
         private final static int CONTACT_NAME_LEN = 100;
         private final static int CURRENCY_LEN = 100;
         
         private final static int DESGINATION_LEN = 30;
         private final static int TELNO_LEN = 30;
         private final static int HPNO_LEN = 30;
         private final static int FAX_LEN = 30;
         private final static int EMAIL_LEN = 50;
         private final static int FACEBOOK_LEN = 50;
         private final static int TWITTER_LEN = 50;
         private final static int LINKEDIN_LEN = 50;
         private final static int SKYPE_LEN = 50;
         private final static int WEBSITE_LEN = 100;
         private final static int ADDR_LEN = 50;
         private final static int REMARKS_LEN = 100; 
 		 private final static int GENDER_LEN = 1; 
 		 private final static int ESTNO_LEN = 50;
         private final static int CUSTOMER_TYPE_ID=50;
		 private final static int SUPPLIER_TYPE_ID=50;
		 private final static int CREDITLIMIT_LEN = 100;
		 private final static int CREDITLIMITBY_LEN = 1;
         public static String DB_PROPS_FILE = MLoggerConstant.PROPS_FOLDER + "/track/config/track.properties";
		private static String LocationNumberOfColumn;
		private static String ItemNumberOfColumn;
		private static String AddItemSupplierNumberOfColumn;
		private static String ItemDescNumberOfColumn;
		private static String ItemImgNumberOfColumn;
		private static String AddItemNumberOfColumn;
		private static String InvNumberOfColumn;
		private static String InvOrdNumberOfColumn;
		private static String StockTakeNumberOfColumn;
		private static String InboundNumberOfColumn;
		private static String OutboundNumberOfColumn;
		private static String ShopifyNumberOfColumn;
		private static String TransferNumberOfColumn;
		private static String SupplierNumberOfColumn;
	    private static String CustomerNumberOfColumn;
	    private static String AltrPrdNumberOfColumn;
	    private static String WorkOrderNumberOfColumn;
		private static String ReadFromServerFolder = null;
		private static String TransferAssigneeNumberOfColumn;
		private static String LoanAssigneeNumberOfColumn;
		private static String EmployeeNumberOfColumn;
		private static String EmployeeLeaveApplyNumberOfColumn;
		private static String HolidayMstNumberOfColumn;
		private static String SalaryMstNumberOfColumn;
		private static String EmployeeLeaveDetNumberOfColumn;
		private static String EmployeeSalaryDetNumberOfColumn;
		private static String ProdBOMNumberOfColumn;
		private static String RoutingNumberOfColumn;
		private static String EstimateNumberOfColumn;
		private static String InboundEstimateNumberOfColumn;
		private static String KittingNumberOfColumn;
		private static String SupplierDiscountNumberOfColumn;
		private static String CustomerDiscountNumberOfColumn;
		private static String BillNumberOfColumn;
		private static String InvoiceNumberOfColumn;
		private static String KitBOMNumberOfColumn;
		private final static int PO_REMARK_LEN2=100;
		private final static int CUSTOMER_STATUS_ID=50;
		private static String OutboundProdRemarksNumberOfColumn;
		private static String InboundProdRemarksNumberOfColumn;
		private static String ShopeeNumberOfColumn;
		private static String LoanNumberOfColumn;
		private final static int INCOTERM_LEN = 200;
		private final static int SHIPPING_CUSTOMER_LEN = 40;
		private final static int TAXTREATMENT_LEN = 100;
		private final static int PURCHASE_LOCATION = 100;
		private final static int REVERSECHARGE = 1;
		private final static int GOODSIMPORT = 1;
		private final static int SALES_LOCATION = 200;
		private static String MinMaxNumberOfColumn;
		private static String OutletMinMaxNumberOfColumn;
		private static String ContactNumberOfColumn;
		static {
		Properties dbpr;
		InputStream dbip;
		Map mapTableFileld = new HashMap();
		try {
			dbip = new FileInputStream(new File(DB_PROPS_FILE));
			dbpr = new Properties();
			dbpr.load(dbip);
			LocationNumberOfColumn        = dbpr.getProperty("In_LocationNumberOfColumn");
			ItemNumberOfColumn     = dbpr.getProperty("In_ItemNumberOfColumn");
			AddItemSupplierNumberOfColumn     = dbpr.getProperty("In_AddItemSupplierNumberOfColumn");
			ItemDescNumberOfColumn     = dbpr.getProperty("In_ItemDescNumberOfColumn");
			ItemImgNumberOfColumn     = dbpr.getProperty("In_ItemImgNumberOfColumn");
			AddItemNumberOfColumn     = dbpr.getProperty("In_AddItemNumberOfColumn");
			InvNumberOfColumn      = dbpr.getProperty("In_InvNumberOfColumn");
			InvOrdNumberOfColumn      = dbpr.getProperty("In_InvOrdNumberOfColumn");
			InboundNumberOfColumn  = dbpr.getProperty("In_InBoundOrderNumberOfColumn");
			OutboundNumberOfColumn = dbpr.getProperty("In_OutBoundOrderNumberOfColumn");
			ShopifyNumberOfColumn = dbpr.getProperty("In_ShopifyNumberOfColumn");
			ShopeeNumberOfColumn = dbpr.getProperty("In_ShopeeNumberOfColumn");
			BillNumberOfColumn = dbpr.getProperty("In_BillNumberOfColumn");
			InvoiceNumberOfColumn = dbpr.getProperty("In_InvoiceNumberOfColumn");
			LoanNumberOfColumn = dbpr.getProperty("In_LoanOrderNumberOfColumn");
			TransferNumberOfColumn = dbpr.getProperty("In_TransferOrderNumberOfColumn");
            SupplierNumberOfColumn = dbpr.getProperty("In_SupplerNumberOfColumn");
            CustomerNumberOfColumn = dbpr.getProperty("In_CustomerNumberOfColumn");
		    AltrPrdNumberOfColumn  = dbpr.getProperty("In_AlternateProduct");
		    WorkOrderNumberOfColumn = dbpr.getProperty("In_WorkOrderNumberOfColumn");
 			TransferAssigneeNumberOfColumn = dbpr.getProperty("In_TransferAssigneeNumberOfColumn");
		    LoanAssigneeNumberOfColumn = dbpr.getProperty("In_LoanAssigneeNumberOfColumn");
		    EmployeeNumberOfColumn = dbpr.getProperty("In_EmployeeNumberOfColumn");
		    EmployeeLeaveApplyNumberOfColumn = dbpr.getProperty("In_EmployeeLeaveApplyNumberOfColumn");
		    HolidayMstNumberOfColumn = dbpr.getProperty("In_HolidayMstNumberOfColumn");
		    SalaryMstNumberOfColumn = dbpr.getProperty("In_SalaryMstNumberOfColumn");
		    EmployeeLeaveDetNumberOfColumn  = dbpr.getProperty("In_EmployeeLeaveDetNumberOfColumn");
		    EmployeeSalaryDetNumberOfColumn  = dbpr.getProperty("In_EmployeeSalaryDetNumberOfColumn");
		    ProdBOMNumberOfColumn = dbpr.getProperty("In_ProdBOMNumberOfColumn");
		    RoutingNumberOfColumn = dbpr.getProperty("In_RoutingNumberOfColumn");
		    EstimateNumberOfColumn = dbpr.getProperty("In_EstimateOrderNumberOfColumn");
		    InboundEstimateNumberOfColumn = dbpr.getProperty("In_InboundEstimateOrderNumberOfColumn");
		    MinMaxNumberOfColumn  = dbpr.getProperty("In_MinMaxProduct");
		    OutletMinMaxNumberOfColumn  = dbpr.getProperty("In_OutletMinMaxProduct");
		    ContactNumberOfColumn  = dbpr.getProperty("In_Contact");
		    
		    KittingNumberOfColumn = dbpr.getProperty("In_KittingNumberOfColumn");
		    SupplierDiscountNumberOfColumn=dbpr.getProperty("In_SupplierDiscountNumberOfColumn");
		    CustomerDiscountNumberOfColumn=dbpr.getProperty("In_CustomerDiscountNumberOfColumn");
		    KitBOMNumberOfColumn=dbpr.getProperty("In_KITBOMNumberOfColumn");
		    OutboundProdRemarksNumberOfColumn = dbpr.getProperty("In_OutboundProdRemarksNumberOfColumn");
		    InboundProdRemarksNumberOfColumn = dbpr.getProperty("In_InboundProdRemarksNumberOfColumn");
			StockTakeNumberOfColumn     = dbpr.getProperty("In_StockTakeNumberOfColumn");
			ReadFromServerFolder = dbpr.getProperty("ReadFromServerFolder");
    		//System.out.println("Total Number of column to import : "+ NumberOfColumn);
		} catch (FileNotFoundException fnfe) {
			System.out.println("Exception : " + fnfe.getMessage());
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList downloadCountSheetData(String Plant, String filename,
			String sheetName,String truncateval,String NOOFLOCATION) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
	
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File  :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber=""; //IMTI
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			
			String[] str = poi.readExcelFile(Integer.parseInt(LocationNumberOfColumn),"Loc");

			System.out.println("Total number of record found in Excel sheet  :  "	+ str.length);
			
			//IMTI
			LocTypeDAO LocDao = new LocTypeDAO();
			int novalid =LocDao.Loc_count(Plant);
			int convl =0;
			if(!NOOFLOCATION.equalsIgnoreCase("Unlimited"))
			    convl = Integer.valueOf(NOOFLOCATION);
			
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];

				//IMTI
				novalid=novalid+1;
				if(!NOOFLOCATION.equalsIgnoreCase("Unlimited"))
				{
					
					if(novalid>convl)
					{
						ValidNumber="LOCATION";
						break;
					}
				}
			
				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}

			        int linecnt=j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String loc = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String loc_desc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String loc_type_id = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String loc_type_id2 = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String loc_type_id3 = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String loc_remarks = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String loc_comname = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String loc_RCBNO = StrUtils.replaceStr2Char(((String) list.get(7)).trim());	
                   	String loc_add1 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String loc_add2 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String loc_add3 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String loc_add4 = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String loc_state = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String loc_country = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String loc_zip = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String loc_telno = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					String loc_fax = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					String loc_active = StrUtils.replaceStr2Char(((String) list.get(17)).trim());	
					                     
				   
					if(StrUtils.isCharAllowed(loc) || StrUtils.isCharAllowed(loc_desc) || StrUtils.isCharAllowed(loc_type_id) || StrUtils.isCharAllowed(loc_remarks) || StrUtils.isCharAllowed(loc_comname) || StrUtils.isCharAllowed(loc_RCBNO) || StrUtils.isCharAllowed(loc_add1) || StrUtils.isCharAllowed(loc_add2) || StrUtils.isCharAllowed(loc_add3) || StrUtils.isCharAllowed(loc_add4) || StrUtils.isCharAllowed(loc_country) || StrUtils.isCharAllowed(loc_zip) || StrUtils.isCharAllowed(loc_telno) || StrUtils.isCharAllowed(loc_fax) || StrUtils.isCharAllowed(loc_active)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					if (("").equals(loc)|| ("null").equals(loc)) {
					    throw new Exception("Loc value is mandatory and  missing at Line "+linecnt+" .");
					}
                                        
                                        /*if(loc.length()>0){
                                            String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(loc);
                                            if(specialcharsnotAllowed.length()>0){
                                                    throw new Exception("LOC  value : '" + loc + "' has special characters "+specialcharsnotAllowed+" that are  not allowed at Line "+linecnt+".");
                                            }
                                        }*/

					// data field validations
					ImportUtils impUtil = new ImportUtils();
					
					// LOC field data validation
					boolean isValidLocData = false;
					if(LOC_LEN > loc.length()){
						isValidLocData = true;
					}
					if(!isValidLocData){
						throw new Exception("LOC value : '" + loc + "' length has exceeded the maximum value of "+LOC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// LOC DESC field data validation
					boolean isValidLocDescData = false;
				    if(truncateval.equalsIgnoreCase("Y") && loc_desc.length()>100){
				        loc_desc = loc_desc.substring(0,LOC_DESC_LEN);
				    }
					if(LOC_DESC_LEN >= loc_desc.length()){
						isValidLocDescData = true;
					}
					if(!isValidLocDescData){
						throw new Exception("LOC Desc value : '" + loc_desc + "' length has exceeded the maximum value of "+LOC_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					// LOC_TYPE_ID1 field data validation
					boolean isValidLoctypeData = false;
				    if(LOC_TYPE_ID >= loc_type_id.length()){
				    	isValidLoctypeData = true;
					}
					if(!isValidLoctypeData){
						throw new Exception("LOC Type One value : '" + loc_type_id + "' length has exceeded the maximum value of "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
					}
					//imti vaid for loc type 1 order exists
					 LocTypeUtil loctypeutil = new LocTypeUtil();
					 Hashtable ht = new Hashtable();
					 ht.put(IDBConstants.PLANT,Plant);
					 ht.put(IDBConstants.LOCTYPEID,loc_type_id);
					 if(!(loctypeutil.isExistsLocType(ht))) // if the Item  exists already
					 throw new Exception("LOC Type One value : '" + loc_type_id + "' doesn't exists "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
					//imti end
					 
					// LOC_TYPE_ID2 field data validation
					boolean isValidLoctypeData2 = false;
				    if(LOC_TYPE_ID >= loc_type_id2.length()){
				    	isValidLoctypeData2 = true;
					}
					if(!isValidLoctypeData2){
						throw new Exception("LOC Type Two value : '" + loc_type_id2 + "' length has exceeded the maximum value of "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
					}
					
					// LOC_TYPE_ID2 field data validation
					boolean isValidLoctypeData3 = false;
				    if(LOC_TYPE_ID >= loc_type_id3.length()){
				    	isValidLoctypeData3 = true;
					}
					if(!isValidLoctypeData3){
						throw new Exception("LOC Type Three value : '" + loc_type_id3 + "' length has exceeded the maximum value of "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
					}
					
					//imti vaid for loc type 2 order exists
					boolean isOrderExists = true;
					Hashtable loc_TYP_MST2 = new Hashtable();
					LocMstTwoDAO locMstTwoDAO = new LocMstTwoDAO();
					loc_TYP_MST2.put(IDBConstants.LOCTYPEID2,loc_type_id2);
					loc_TYP_MST2.put(IDBConstants.PLANT,Plant);
				    	 isOrderExists  = new LocMstTwoDAO().isExisit(loc_TYP_MST2);
					    	 if(!isOrderExists )
						 throw new Exception("LOC Type Two value : '" + loc_type_id2 + "' doesn't exists "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
						 //imti end
					    	 
					    	//imti vaid for loc type 2 order exists
								boolean isOrderExist = true;
								Hashtable loc_TYP_MST3 = new Hashtable();
								LocMstThreeDAO locMstThreeDAO = new LocMstThreeDAO();
								loc_TYP_MST3.put(IDBConstants.LOCTYPEID3,loc_type_id3);
								loc_TYP_MST3.put(IDBConstants.PLANT,Plant);
						    	 isOrderExist  = new LocMstThreeDAO().isExisit(loc_TYP_MST3);
							    	 if(!isOrderExist )
								 throw new Exception("LOC Type Three value : '" + loc_type_id3 + "' doesn't exists "+LOC_TYPE_ID+" Chars at Line "+linecnt+".");
								 //imti end
					
					//Please enter LOC_TYPE_ID2 different from LOC_TYPE_ID1
					if ((loc_type_id.equalsIgnoreCase("null")) || (loc_type_id == null)){
						loc_type_id = "NOLOCTYPE";
					}
					if ((loc_type_id2.equalsIgnoreCase("null")) || (loc_type_id2 == null)){
						loc_type_id2 = "NOLOCTYPE";
					}
					if(loc_type_id2.equalsIgnoreCase(loc_type_id)){
						if(loc_type_id!="NOLOCTYPE"&&loc_type_id2!="NOLOCTYPE"){
						throw new Exception("Enter LOC Type2 value different from LOC Type1 value at Line "+linecnt+".");
						}
					}
					
					// Remarks field data validation
					boolean isValidRemarksData = false;
                                        if(truncateval.equalsIgnoreCase("Y") && loc_remarks.length()>100){
                                            loc_remarks = loc_remarks.substring(0,LOC_REMARK_LEN);
                                        }
					if(LOC_REMARK_LEN>= loc_remarks.length()){
						isValidRemarksData = true;
					}
					if(!isValidRemarksData){
						throw new Exception("Remarks value : '" + loc_remarks + "' length has exceeded the maximum value of "+LOC_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					// active field validation
					boolean isCorrectActiveFormat = false;
					if (!loc_active.trim().equalsIgnoreCase("null") && loc_active != null){
						isCorrectActiveFormat = impUtil.isCorrectActiveFormat(loc_active);
						if(!isCorrectActiveFormat){
							throw new Exception("IsActive value '" + loc_active + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// ADD1 field data validation
					boolean isValidLocADD1 = false;
					if(LOC_ADD1_LEN >= loc_add1.length()){
						isValidLocADD1 = true;
					}
					if(!isValidLocADD1){
						throw new Exception("ADD1 value : '" + loc_add1 + "' length has exceeded the maximum value of "+LOC_ADD1_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ADD2 field data validation
					boolean isValidLocADD2 = false;
					if(LOC_ADD2_LEN >= loc_add2.length()){
						isValidLocADD2 = true;
					}
					if(!isValidLocADD2){
						throw new Exception("ADD2 value : '" + loc_add2 + "' length has exceeded the maximum value of "+LOC_ADD2_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ADD3 field data validation
					boolean isValidLocADD3 = false;
					if(LOC_ADD3_LEN >= loc_add3.length()){
						isValidLocADD3 = true;
					}
					if(!isValidLocADD3){
						throw new Exception("ADD3 value : '" + loc_add3 + "' length has exceeded the maximum value of "+LOC_ADD3_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ADD4 field data validation
					boolean isValidLocADD4 = false;
					if(LOC_ADD4_LEN >= loc_add4.length()){
						isValidLocADD4 = true;
					}
					if(!isValidLocADD4){
						throw new Exception("ADD4 value : '" + loc_add4 + "' length has exceeded the maximum value of "+LOC_ADD4_LEN+" Chars at Line "+linecnt+".");
					}
					//State Feild
					boolean isValidLocSTATE = false;
					if(LOC_STATE_LEN >= loc_state.length()){
						isValidLocSTATE = true;
					}
					if(!isValidLocSTATE){
						throw new Exception("STATE value : '" + loc_state + "' length has exceeded the maximum value of "+LOC_STATE_LEN+" Chars at Line "+linecnt+".");
					}
					
					
					// COUNTRY field data validation
					boolean isValidLocCOUNTRY = false;
					if(LOC_COUNTRY_LEN >= loc_country.length()){
						isValidLocCOUNTRY = true;
					}
					if(!isValidLocCOUNTRY){
						throw new Exception("COUNTRY value : '" + loc_country + "' length has exceeded the maximum value of "+LOC_COUNTRY_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ZIP field data validation
					boolean isValidLocZIP = false;
					if(LOC_ZIP_LEN >= loc_zip.length()){
						isValidLocZIP = true;
					}
					if(!isValidLocZIP){
						throw new Exception("ZIP value : '" + loc_zip + "' length has exceeded the maximum value of "+LOC_ZIP_LEN+" Chars at Line "+linecnt+".");
					}
					
					// TELNO field data validation
					boolean isValidLocTELNO = false;
					if(LOC_TELNO_LEN >= loc_telno.length()){
						isValidLocTELNO = true;
					}
					if(!isValidLocTELNO){
						throw new Exception("TELNO value : '" + loc_telno + "' length has exceeded the maximum value of "+LOC_TELNO_LEN+" Chars at Line "+linecnt+".");
					}
					
					// FAX field data validation
					boolean isValidLocFAX = false;
					if(LOC_FAX_LEN >= loc_fax.length()){
						isValidLocFAX = true;
					}
					if(!isValidLocFAX){
						throw new Exception("FAX value : '" + loc_fax + "' length has exceeded the maximum value of "+LOC_FAX_LEN+" Chars at Line "+linecnt+".");
					}
					
					// COMNAME field data validation
					boolean isValidLocCOMNAME = false;
					if(LOC_COMNAME_LEN >= loc_comname.length()){
						isValidLocCOMNAME = true;
					}
					if(!isValidLocCOMNAME){
						throw new Exception("Company Name value : '" + loc_comname + "' length has exceeded the maximum value of "+LOC_COMNAME_LEN+" Chars at Line "+linecnt+".");
					}
					
					// RCBNO field data validation
					boolean isValidLocRCBNO = false;
					if(LOC_RCBNO_LEN >= loc_RCBNO.length()){
						isValidLocRCBNO = true;
					}
					if(!isValidLocRCBNO){
						throw new Exception("RCBNO value : '" + loc_RCBNO + "' length has exceeded the maximum value of "+LOC_RCBNO_LEN+" Chars at Line "+linecnt+".");
					}
					
					
					m.put(IDBConstants.PLANT, Plant);
					m.put(IDBConstants.LOC, loc);
					
					if ((loc_desc.equalsIgnoreCase("null")) || (loc_desc == null)){
						loc_desc = "";
					}
					m.put(IDBConstants.LOCDESC, loc_desc);
					
					if ((loc_remarks.equalsIgnoreCase("null")) || (loc_remarks == null)){
						loc_remarks = "";
					}
					m.put(IDBConstants.REMARKS, loc_remarks);
					
					if ((loc_active.equalsIgnoreCase("null")) || (loc_active == null)){
						loc_active = "Y";
					}
					m.put(IConstants.ISACTIVE, loc_active);
					
					if ((loc_add1.equalsIgnoreCase("null")) || (loc_add1 == null)){
						loc_add1 = "";
					}
					m.put(IDBConstants.ADD1, loc_add1);
				
					if ((loc_add2.equalsIgnoreCase("null")) || (loc_add2 == null)){
						loc_add2 = "";
					}
					m.put(IDBConstants.ADD2, loc_add2);
				
					if ((loc_add3.equalsIgnoreCase("null")) || (loc_add3 == null)){
						loc_add3 = "";
					}
					m.put(IDBConstants.ADD3, loc_add3);
				
					if ((loc_add4.equalsIgnoreCase("null")) || (loc_add4 == null)){
						loc_add4 = "";
					}
					m.put(IDBConstants.ADD4, loc_add4);
					
					if ((loc_state.equalsIgnoreCase("null")) || (loc_state == null)){
						loc_state = "";
					}
					m.put(IDBConstants.STATE, loc_state);
				
					if ((loc_country.equalsIgnoreCase("null")) || (loc_country == null)){
						loc_country = "";
					}
					m.put(IDBConstants.COUNTRY, loc_country);
				
					if ((loc_zip.equalsIgnoreCase("null")) || (loc_zip == null)){
						loc_zip = "";
					}
					m.put(IDBConstants.ZIP, loc_zip);
				
					if ((loc_telno.equalsIgnoreCase("null")) || (loc_telno == null)){
						loc_telno = "";
					}
					m.put(IDBConstants.TELNO, loc_telno);
				
					if ((loc_fax.equalsIgnoreCase("null")) || (loc_fax == null)){
						loc_fax = "";
					}
					m.put(IDBConstants.FAX, loc_fax);
				
					if ((loc_comname.equalsIgnoreCase("null")) || (loc_comname == null)){
						loc_comname = "";
					}
					m.put("COMNAME", loc_comname);
				
					if ((loc_RCBNO.equalsIgnoreCase("null")) || (loc_RCBNO == null)){
						loc_RCBNO = "";
					}
					m.put(IDBConstants.RCBNO, loc_RCBNO);
				
					m.put(IDBConstants.LOCTYPEID, loc_type_id);
					
					
					m.put(IDBConstants.LOCTYPEID2, loc_type_id2);
					
					m.put(IDBConstants.LOCTYPEID3, loc_type_id3);
					//m.put("CHKSTATUS", "Y");					
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		//IMTI
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		return hdrList;

	}

	@SuppressWarnings("unchecked")
	public ArrayList downloadItemSheetData(String Plant, String filename,
			String sheetName,String truncateval,String NOOFINVENTORY) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		
		//
		System.out.println("After extraction filename : " + filename +truncateval);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
	    String basecurrency = plantMstDAO.getbasecurrency(Plant);
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer.parseInt(ItemNumberOfColumn),"Prod");
			// String[] str =null;

			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			
            //Validate no.of Product -- Azees 15.11.2020
			ItemMstDAO itemMstDAO= new ItemMstDAO();
				
			int novalid =itemMstDAO.Itemcount(Plant);
			int convl =0;
			if(!NOOFINVENTORY.equalsIgnoreCase("Unlimited"))
			    convl = Integer.valueOf(NOOFINVENTORY);
			
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				novalid=novalid+1;
				if(!NOOFINVENTORY.equalsIgnoreCase("Unlimited"))
				{
					
					if(novalid>convl)
					{
						ValidNumber="INVENTORY";
						//throw new Exception("You have reached the limit of "+NOOFINVENTORY+" products you can create");
						break;
					}
				}
				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt = linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String item_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String item_desc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String det_desc = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String isApplyuom = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String supplier = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String netweight = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String grossweight = StrUtils.replaceStr2Char(((String) list.get(7)).trim());//7
					String dimension = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String item_dept_id = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String item_class_id = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String item_type = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String item_brand = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String prodgst = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String hscode = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String coo = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					String vinno = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					String model = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
					String remark3 = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					String remark4 = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
					String purchaseuom = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String cost = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					String salesuom = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
					String price = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
					String minsprice = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
					/*String rentaluom = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
					String rentalprice = StrUtils.replaceStr2Char(((String) list.get(23)).trim());*/
					//String serviceuom = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String inventoryuom = StrUtils.replaceStr2Char(((String) list.get(25)).trim());					
					String qty = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
					String maxqty = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
					//String serviceprice = StrUtils.replaceStr2Char(((String) list.get(21)).trim());					
					//String kit = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
					String active = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
					String isNonStock = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
					String catalog = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
					String iscompro = StrUtils.replaceStr2Char(((String) list.get(31)).trim());
					String cppi = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
					String incprice = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
					String incpriceunit="";
					
					
			
					/*if(StrUtils.isCharAllowed(item_id) || StrUtils.isCharAllowed(item_desc) || StrUtils.isCharAllowed(item_class_id) || StrUtils.isCharAllowed(item_type) || StrUtils.isCharAllowed(item_brand) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(vinno) || StrUtils.isCharAllowed(model) || StrUtils.isCharAllowed(remark3) || StrUtils.isCharAllowed(remark4) || StrUtils.isCharAllowed(det_desc) ||  StrUtils.isCharAllowed(kit) || StrUtils.isCharAllowed(active) || StrUtils.isCharAllowed(isNonStock)){*/
						if(StrUtils.isCharAllowed(item_id) || StrUtils.isCharAllowed(item_desc) || StrUtils.isCharAllowed(supplier) || StrUtils.isCharAllowed(item_dept_id) || StrUtils.isCharAllowed(item_class_id) || StrUtils.isCharAllowed(item_type) || StrUtils.isCharAllowed(item_brand) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(vinno) || StrUtils.isCharAllowed(model) || StrUtils.isCharAllowed(remark3) || StrUtils.isCharAllowed(remark4) || StrUtils.isCharAllowed(det_desc) || StrUtils.isCharAllowed(active) || StrUtils.isCharAllowed(isNonStock)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					if (("").equals(item_id)|| ("null").equals(item_id)){
                                            throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
                                        }
                                        if (("").equals(item_desc)|| ("null").equals(item_desc)){
                                            throw new Exception("Product Description value is mandatory and  missing at Line "+linecnt+" .");
                                        }
                                        if (("").equals(uom)|| ("null").equals(uom)){
                                            throw new Exception("Basic UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                        }
                    
                                        if (("").equals(isApplyuom)||isApplyuom.equalsIgnoreCase("null") || isApplyuom == null)
                                        {
                    						isApplyuom = "N";
                                        }
                                        if(isApplyuom.equals("N"))
                                        {
                                        	if (("").equals(purchaseuom)|| ("null").equals(purchaseuom)){
                                                throw new Exception("Purchase UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                            }else
                                            	if (("").equals(salesuom)|| ("null").equals(salesuom)){
                                                    throw new Exception("Sales UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                                }else
                                              /*  	if (("").equals(rentaluom)|| ("null").equals(rentaluom)){
                                                    throw new Exception("Rental UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                                }else*/
                                                	/*if (("").equals(serviceuom)|| ("null").equals(serviceuom)){
                                                        throw new Exception("Service UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                                    }else*/
                                                    	if (("").equals(inventoryuom)|| ("null").equals(inventoryuom)){
                                                            throw new Exception("Inventory UOM  value is mandatory and  missing at Line "+linecnt+" .");
                                                        }
                                                	
                                        }
                                     
				
					
					ImportUtils impUtil = new ImportUtils();
					
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= item_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + item_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
                  					
					// ITEM DESC field data validation
					boolean isValidItemDescData = false;
                                        if(truncateval.equalsIgnoreCase("Y") && item_desc.length()>100){
                                            item_desc = item_desc.substring(0,ITEM_DESC_LEN);
                                        }
				  
					if(ITEM_DESC_LEN >= item_desc.length()){
                                        	isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description value :  '" + item_desc + "' length has exceeded the maximum value of "+ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					// Detail Desc field data validation
					boolean isValidDetDescData = false;
                                        if(truncateval.equalsIgnoreCase("Y") && det_desc.length()>100){
                                            det_desc = det_desc.substring(0,DET_DESC_LEN);
                                        }

					if(DET_DESC_LEN >= det_desc.length()){
						isValidDetDescData = true;
					}
					if(!isValidDetDescData){
						throw new Exception("Detailed Description value :  '" + det_desc + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM UOM field data validation
					boolean isValidItemUomData = false;
					if(STKUOM_LEN >= uom.length()){
						isValidItemUomData = true;
					}
					if(!isValidItemUomData){
						throw new Exception("Basic UOM value : '" + uom + "' length has exceeded the maximum value of "+STKUOM_LEN+" Chars at Line "+linecnt+".");
					}
					
					// is Apply to all uom
					boolean isCorrectApplyuom = false;
					if (!isApplyuom.trim().equalsIgnoreCase("null") && isApplyuom != null){
						isCorrectApplyuom = impUtil.isCorrectApplyuom(isApplyuom);
						if(!isCorrectApplyuom){
							throw new Exception("isApplyuom value '" + isApplyuom + "' is not accepted format "+linecnt+".");
						}
						
					}
					
					if (!supplier.trim().equalsIgnoreCase("null") &&  supplier != null){
					boolean isExistVendor = false;
					VendUtil vendUtil = new VendUtil();
					isExistVendor = vendUtil.isExistsVendor(supplier, Plant);
					if(!isExistVendor){
						throw new Exception("Supplier '" + supplier + "' is not created yet on system at Line "+linecnt+".");
					}
					}
					else
						supplier="";
					
					boolean isValidsupplierData = false;
					if(ITEM_PRD_SUP >= supplier.length()){
						isValidsupplierData = true;
					}
					if(!isValidsupplierData){
						throw new Exception("Supplier value : '" + supplier + "' length has exceeded the maximum value of "+ITEM_PRD_SUP+" Chars at Line "+linecnt+".");
					}
					
					boolean isCorrectNetWeight = false;
					if (!netweight.trim().equalsIgnoreCase("null") &&  netweight != null){
						isCorrectNetWeight = impUtil.isCorrectQuantityFormat(netweight);
						if(!isCorrectNetWeight ){
							throw new Exception("Net Weight (KG) value '" + netweight + "' is not accepted format "+linecnt+".");
						}
					}
					else
					{
						netweight = "0";
					}
					
					if (!netweight.trim().equalsIgnoreCase("null") && netweight != null){
						if(netweight.contains(".")){
							int netweightdecnum = netweight.substring(netweight.indexOf(".")).length() - 1;
							if(netweightdecnum>3)
							{
								throw new Exception("Invalid more than "+ 3 +" digits after decimal in Net Weight (KG) at Line "+linecnt+".");
							}
						}
						
					}
					
					boolean isCorrectGrossWeight = false;
					if (!grossweight.trim().equalsIgnoreCase("null") &&  grossweight != null){
						isCorrectGrossWeight = impUtil.isCorrectQuantityFormat(grossweight);
						if(!isCorrectGrossWeight){
							throw new Exception("Gross Weight (KG) value '" + grossweight + "' is not accepted format "+linecnt+".");
						}
					}
					else
					{
						netweight = "0";
					}
					
					if (!grossweight.trim().equalsIgnoreCase("null") && grossweight != null){
						if(grossweight.contains(".")){
							int grossweightdecnum = grossweight.substring(grossweight.indexOf(".")).length() - 1;
							if(grossweightdecnum>3)
							{
								throw new Exception("Invalid more than "+ 3 +" digits after decimal in Gross Weight (KG) at Line "+linecnt+".");
							}
						}
						
					}
					
					//DIMENSION
					boolean isValidDimentionData = false;
					if(DIMENSION_LEN >= dimension.length()){
						isValidDimentionData = true;
					}
					if(!isValidDimentionData){
						throw new Exception("Dimension. value : '" + dimension + "' length has exceeded the maximum value of "+DIMENSION_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isValidItemDeptData = false;
					if(PRD_CLS_ID_LEN >= item_dept_id.length()){
						isValidItemDeptData = true;
					}
					if(!isValidItemDeptData){
						throw new Exception("Product Department value :  '" + item_dept_id + "' length has exceeded the maximum value of "+PRD_CLS_ID_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isValidItemClassData = false;
					if(PRD_CLS_ID_LEN >= item_class_id.length()){
						isValidItemClassData = true;
					}
					if(!isValidItemClassData){
						throw new Exception("Product Category value :  '" + item_class_id + "' length has exceeded the maximum value of "+PRD_CLS_ID_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM Type field data validation
					boolean isValidItemTypeData = false;
					if(ITEM_TYPE_LEN >= item_type.length()){
						isValidItemTypeData = true;
					}
					if(!isValidItemTypeData){
						throw new Exception("Product Sub Category value :  '" + item_type + "' length has exceeded the maximum value of "+ITEM_TYPE_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isValidItemBrandData = false;
					if(ITEM_BRAND_LEN >= item_brand.length()){
						isValidItemBrandData = true;
					}
					if(!isValidItemBrandData){
						throw new Exception("Product Brand value :  '" + item_brand + "' length has exceeded the maximum value of "+ITEM_BRAND_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isCorrectProdGstFormat = false;
					if (!prodgst.trim().equalsIgnoreCase("null") && prodgst != null){
						isCorrectProdGstFormat = impUtil.isCorrectUnitPriceFormat(prodgst);
						if(!isCorrectProdGstFormat){
							throw new Exception("Product VAT/GST value '" + prodgst + "' is not accepted format at Line "+linecnt+".");
						}
					}
					else
					{
						prodgst= "0";
					}
					
					if (!prodgst.trim().equalsIgnoreCase("null") && prodgst != null){
						if(prodgst.contains(".")){
							int prodgstdecnum = prodgst.substring(prodgst.indexOf(".")).length() - 1;
							if(prodgstdecnum>3)
							{
								throw new Exception("Invalid more than "+ 3 +" digits after decimal in Product VAT/GST at Line "+linecnt+".");
							}
						}
						
					}
					
					// hs code field data validation
					boolean isValidItemhscodeData = false;
					if(HSCODE_LEN >= hscode.length()){
						isValidItemhscodeData = true;
					}
					if(!isValidItemhscodeData){
						throw new Exception("HS Code value : '" + hscode + "' length has exceeded the maximum value of "+HSCODE_LEN+" Chars at Line "+linecnt+".");
					}
					
					// COO field data validation
					boolean isValidItemcooData = false;
					if(COO_LEN >= coo.length()){
						isValidItemcooData = true;
					}
					if(!isValidItemcooData){
						throw new Exception("COO value : '" + coo + "' length has exceeded the maximum value of "+COO_LEN+" Chars at Line "+linecnt+".");
					}
					
					// VIN No field data validation
					boolean isValidVINNOData = false;
					if(VINNO_LEN >= vinno.length()){
						isValidVINNOData = true;
					}
					if(!isValidVINNOData){
						throw new Exception("VIN No. value : '" + vinno + "' length has exceeded the maximum value of "+VINNO_LEN+" Chars at Line "+linecnt+".");
					}
					
					// VIN No field data validation
					boolean isValidMODELData = false;
					if(MODEL_LEN >= model.length()){
						isValidMODELData = true;
					}
					if(!isValidMODELData){
						throw new Exception("Model value : '" + model + "' length has exceeded the maximum value of "+MODEL_LEN+" Chars at Line "+linecnt+".");
					}
					// Remark3 field data validation
					boolean isValidRemark3Data = false;
				    if(truncateval.equalsIgnoreCase("Y") && remark3.length()>100){
				        remark3 = remark3.substring(0,REMARKS3_LEN);
				    }

					if(REMARKS3_LEN >= remark3.length()){
						isValidRemark3Data = true;
					}
					if(!isValidRemark3Data){
						throw new Exception("Remark1 value :  '" + remark3 + "' length has exceeded the maximum value of "+REMARKS3_LEN+" Chars at Line "+linecnt+".");
					}
					
					// Remark4 field data validation
					boolean isValidRemark4Data = false;
                                        if(truncateval.equalsIgnoreCase("Y") && remark4.length()>100){
                                            remark4 = remark4.substring(0,REMARKS4_LEN);
                                        }
					if(REMARKS4_LEN >= remark4.length()){
						isValidRemark4Data = true;
					}
					if(!isValidRemark4Data){
						throw new Exception("Remark2 value :  '" + remark4 + "' length has exceeded the maximum value of "+REMARKS4_LEN+" Chars at Line "+linecnt+".");
					}
					
					//purchase Uom
					boolean isValidpurchaseUomData = false;
					if(PURCHASEUOM_LEN >= purchaseuom.length()){
						isValidpurchaseUomData = true;
					}
					if(!isValidpurchaseUomData){
						throw new Exception("Purchase UOM value : '" + purchaseuom + "' length has exceeded the maximum value of "+PURCHASEUOM_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isCorrectCostFormat = false;
					if (!cost.trim().equalsIgnoreCase("null") && cost != null){
						isCorrectCostFormat = impUtil.isCorrectUnitPriceFormat(cost);
						if(!isCorrectCostFormat){
							throw new Exception("Cost value '" + cost + "' is not accepted format at Line "+linecnt+".");
						}
					}
					else
					{
						cost = "0";
					}
					
					if (!cost.trim().equalsIgnoreCase("null") && cost != null){
						if(cost.contains(".")){
							int costdecnum = cost.substring(cost.indexOf(".")).length() - 1;
							if(costdecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Cost at Line "+linecnt+".");
							}
						}
						
					}
					
					//sales order uom
					boolean isValidsalesUomData = false;
					if(SALESUOM_LEN >= salesuom.length()){
						isValidsalesUomData = true;
					}
					if(!isValidsalesUomData){
						throw new Exception("Sales UOM value : '" + salesuom + "' length has exceeded the maximum value of "+SALESUOM_LEN+" Chars at Line "+linecnt+".");
					}
					
					// price field validation
					boolean isCorrectPriceFormat = false;
					if (!price.trim().equalsIgnoreCase("null") && price != null){
						isCorrectPriceFormat = impUtil.isCorrectUnitPriceFormat(price);
						if(!isCorrectPriceFormat){
							throw new Exception("Listed Price value '" + price + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					else
					{
						price="0";
					}
					
					if (!price.trim().equalsIgnoreCase("null") && price != null){
						if(price.contains(".")){
							int pricedecnum = price.substring(price.indexOf(".")).length() - 1;
							if(pricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Listed price at Line "+linecnt+".");
							}
						}
						
					}					
					
					// minimum selling price field validation
					boolean isCorrectMinSPriceFormat = false;
					if (!minsprice.trim().equalsIgnoreCase("null") && minsprice != null){
						isCorrectMinSPriceFormat = impUtil.isCorrectUnitPriceFormat(minsprice);
						if(!isCorrectMinSPriceFormat){
							throw new Exception("Minimum Selling Price value '" + minsprice + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					else
					{
						minsprice="0";
					}
					
					if (!minsprice.trim().equalsIgnoreCase("null") && minsprice != null){
						if(minsprice.contains(".")){
							int minspricedecnum = minsprice.substring(minsprice.indexOf(".")).length() - 1;
							if(minspricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Minimum Selling Price at Line "+linecnt+".");
							}
						}
						
					}
				  	
					
					//checking whether price is lessthan minimum selling price
						double nprice=Double.parseDouble(price);
						double nminsprice=Double.parseDouble(minsprice);
					
					if(nprice<nminsprice)
					{
						throw new Exception("Price value '" + price + "' is less than Minimum Selling Price '" + minsprice + " ' at line:" +linecnt+".");
						
					}
					
					//rental order uom
			/*		boolean isValidrentalUomData = false;
					if(RENTALUOM_LEN >= rentaluom.length()){
						isValidrentalUomData = true;
					}
					if(!isValidrentalUomData){
						throw new Exception("Rental UOM value : '" + rentaluom + "' length has exceeded the maximum value of "+RENTALUOM_LEN+" Chars at Line "+linecnt+".");
					}*/
					//service order uom
					/*boolean isValidserviceUomData = false;
					if(SERVICEUOM_LEN >= serviceuom.length()){
						isValidserviceUomData = true;
					}*/
				/*	if(!isValidserviceUomData){
						throw new Exception("Service UOM value : '" + serviceuom + "' length has exceeded the maximum value of "+SERVICEUOM_LEN+" Chars at Line "+linecnt+".");
					}
					//Service price
					boolean isCorrectServicePriceFormat = false;
					if (!serviceprice.trim().equalsIgnoreCase("null") && serviceprice != null){
						isCorrectServicePriceFormat = impUtil.isCorrectUnitPriceFormat(serviceprice);
						if(!isCorrectServicePriceFormat){
							throw new Exception("Service Price value '" + serviceprice + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					else
					{
						serviceprice="0";
					}
					
					if (!serviceprice.trim().equalsIgnoreCase("null") && serviceprice != null){
						if(serviceprice.contains(".")){
							int servicepricedecnum = serviceprice.substring(serviceprice.indexOf(".")).length() - 1;
							if(servicepricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Service Price at Line "+linecnt+".");
							}
						}
						
					}*/
					//Rental price
					/*boolean isCorrectRentalPriceFormat = false;
					if (!rentalprice.trim().equalsIgnoreCase("null") && rentalprice != null){
						isCorrectRentalPriceFormat = impUtil.isCorrectUnitPriceFormat(rentalprice);
						if(!isCorrectRentalPriceFormat){
							throw new Exception("Rental Price value '" + rentalprice + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					else
					{
						rentalprice="0";
					}
					
					if (!rentalprice.trim().equalsIgnoreCase("null") && rentalprice != null){
						if(rentalprice.contains(".")){
							int rentalpricedecnum = rentalprice.substring(rentalprice.indexOf(".")).length() - 1;
							if(rentalpricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Rental Price at Line "+linecnt+".");
							}
						}
						
					}*/
					//Inventory UOM
					boolean isValidinventoryUomData = false;
					if(INVENTORYUOM_LEN >= inventoryuom.length()){
						isValidinventoryUomData = true;
					}
					if(!isValidinventoryUomData){
						throw new Exception("Inventory UOM value : '" + inventoryuom + "' length has exceeded the maximum value of "+INVENTORYUOM_LEN+" Chars at Line "+linecnt+".");
					}					
														
					// min stock qty field validation
					boolean isCorrectMinstockQtyFormat = false;
					if (!qty.trim().equalsIgnoreCase("null") && qty != null){
						isCorrectMinstockQtyFormat = impUtil.isCorrectQuantityFormat(qty);
						if(!isCorrectMinstockQtyFormat){
							throw new Exception("Min Stock Qty value '" + qty + "' is not accepted format "+linecnt+".");
						}
					}
					
					// max stock qty field validation
					boolean isCorrectMaxstockQtyFormat = false;
					if (!maxqty.trim().equalsIgnoreCase("null") && maxqty != null){
						isCorrectMaxstockQtyFormat = impUtil.isCorrectQuantityFormat(maxqty);
						if(!isCorrectMaxstockQtyFormat){
							throw new Exception("Max Stock Qty value '" + qty + "' is not accepted format "+linecnt+".");
						}
					}
					
					// active field validation
					boolean isCorrectActiveFormat = false;
					if (!active.trim().equalsIgnoreCase("null") && active != null){
						isCorrectActiveFormat = impUtil.isCorrectActiveFormat(active);
						if(!isCorrectActiveFormat){
							throw new Exception("IsActive value '" + active + "' is not accepted format "+linecnt+".");
						}
					}
					
					// kiting field validation
/*					boolean isCorrectKitingFormat = false;
					if (!kit.equalsIgnoreCase("null") && kit != null){
						isCorrectKitingFormat = impUtil.isCorrectKitingFormat(kit);
						if(!isCorrectKitingFormat){
							throw new Exception("IsKiting value '" + kit + "' is not accepted format "+linecnt+".");
						}
					}*/
					//Non Stock validation
					boolean isCorrectNonStockFormat = false;
					if (!isNonStock.trim().equalsIgnoreCase("null") && isNonStock != null){
						isCorrectNonStockFormat = impUtil.isCorrectActiveFormat(isNonStock);
						if(!isCorrectNonStockFormat){
							throw new Exception("isNonStock value '" + isNonStock + "' is not accepted format "+linecnt+".");
						}
					}

					if(catalog.equals("null") )
                    {
                           	catalog = "";
                    }
					if(catalog.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(catalog);
						if(file.exists()) {
						iscatalog = true;
						}
						else{
						throw new Exception("Product image not found in the path '" + catalog + "' at Line"+linecnt+".");
						}
						}
					//Combination Product
					/* if(iscompro.equals("null") )
                    {
						iscompro = "N";
                    }
					if(iscompro.equalsIgnoreCase("Y")){
						iscompro = "1";
						if (("").equals(cppi)|| ("null").equals(cppi)){
                            throw new Exception("Price Increased By value is mandatory and  missing at Line "+linecnt+" .");
                        }else {
                        	if(cppi.equalsIgnoreCase("PRICE")) {
                        		incpriceunit = basecurrency;
                        	}else if(cppi.equalsIgnoreCase("PERCENTAGE")) {
                        		incpriceunit = "%";
                        	}else {
                        		throw new Exception("Price Increased By Product value '" + cppi + "' is not accepted format at Line "+linecnt+".");
                        	}
                        }
						
						boolean isCorrectIncpriceeFormat = false;
						if (!minsprice.trim().equalsIgnoreCase("null") && minsprice != null){
							isCorrectIncpriceeFormat = impUtil.isCorrectUnitPriceFormat(incprice);
							if(!isCorrectIncpriceeFormat){
								throw new Exception("Increased Amount/percentage value '" + incprice + "' is not accepted format at Line "+linecnt+".");
							}
							
						}
						else
						{
							incprice="0";
						}
						
					}else if(iscompro.equalsIgnoreCase("N")){
						iscompro = "0";
					}else {
						throw new Exception("Product Is Combination Product value '" + iscompro + "' is not accepted format at Line "+linecnt+".");
					}*/
					if(iscompro.equals("null") )
                    {
						iscompro = "NONE";
                    }
					if(iscompro.equalsIgnoreCase("FINISHED")){
						iscompro = "1";						
					} else if(iscompro.equalsIgnoreCase("SEMIFINISHED")){
						iscompro = "2";
					} else if(iscompro.equalsIgnoreCase("NONE")){
						iscompro = "0";
					} else {
						throw new Exception("Product Is (NONE/FINISHED/SEMIFINISHED) value '" + iscompro + "' is not accepted format at Line "+linecnt+".");
					}
					
					if (("").equals(cppi)|| ("null").equals(cppi)){
						cppi="BYPRICE";
						incpriceunit = basecurrency;
                    }else {
                    	if(cppi.equalsIgnoreCase("BYPRICE")) {
                    		incpriceunit = basecurrency;
                    	}else if(cppi.equalsIgnoreCase("BYPERCENTAGE")) {
                    		incpriceunit = "%";
                    	}else {
                    		throw new Exception("Price Increased By Product value '" + cppi + "' is not accepted format at Line "+linecnt+".");
                    	}
                    }
					
					boolean isCorrectIncpriceeFormat = false;
					if (!incprice.trim().equalsIgnoreCase("null") && incprice != null){
						isCorrectIncpriceeFormat = impUtil.isCorrectUnitPriceFormat(incprice);
						if(!isCorrectIncpriceeFormat){
							throw new Exception("Increased Amount/percentage value '" + incprice + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					else
					{
						incprice="0";
					}
					
					m.put("PLANT", Plant);
					m.put(IConstants.CATLOGPATH, catalog);
					m.put(IConstants.ITEM, item_id);
					m.put(IConstants.ITEM_DESC, item_desc);
				        m.put(IConstants.STKUOM, uom);
				/*	if (price.equalsIgnoreCase("null") || price == null)
						price = "0";*/
				        if(isApplyuom.equals("Y"))
				        {
				        	m.put(IConstants.PURCHASEUOM, uom);
					        m.put(IConstants.SALESUOM, uom);
					        m.put(IConstants.RENTALUOM, uom);
					       // m.put(IConstants.SERVICEUOM, uom);
					        m.put(IConstants.INVENTORYUOM, uom);
				        }else{
					m.put(IConstants.PURCHASEUOM, purchaseuom);
				        m.put(IConstants.SALESUOM, salesuom);
				     //   m.put(IConstants.RENTALUOM, rentaluom);
				        //m.put(IConstants.SERVICEUOM, serviceuom);
				        m.put(IConstants.INVENTORYUOM, inventoryuom);
				        }
					m.put(IConstants.PRICE, price);
					//m.put(IConstants.SERVICEPRICE, serviceprice);
				//	m.put(IConstants.RENTALPRICE, rentalprice);
					/*	if (minsprice.equalsIgnoreCase("null") || minsprice == null)
						minsprice = "0";*/
					m.put(IConstants.MIN_S_PRICE, minsprice);
								
					
					m.put(IConstants.COST, cost);
							
					m.put(IConstants.HSCODE, hscode);
					m.put(IConstants.COO, coo);
					
					m.put(IConstants.PRODGST, prodgst);
					
					/*if (discount.equalsIgnoreCase("null") || discount == null)
						discount = "0";*/
					/*m.put(IConstants.DISCOUNT, discount);*/
					
					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.STKQTY, qty);
					
					if (netweight.equalsIgnoreCase("null") || netweight == null)
						netweight = "0";
					m.put(IConstants.NETWEIGHT, netweight);
					
					if (grossweight.equalsIgnoreCase("null") || grossweight == null)
						grossweight = "0";
					m.put(IConstants.GROSSWEIGHT, grossweight);
					
					if (dimension.equalsIgnoreCase("null") || dimension == null)
						dimension = "";
					m.put(IConstants.DIMENSION, dimension);
					
					if (item_dept_id.equalsIgnoreCase("null") || item_dept_id == null)
						item_dept_id = "";
					m.put(IConstants.PRDTYPEID, item_dept_id);

					if (maxqty.equalsIgnoreCase("null") || qty == null)
					maxqty = "0";
					m.put(IConstants.MAXSTKQTY, maxqty);

				    if (item_class_id.equalsIgnoreCase("null") || item_class_id == null)
				        item_class_id="NOCLASSIFICATION";
					m.put(IDBConstants.PRDCLSID, item_class_id);
				    if (item_type.equalsIgnoreCase("null") || item_type == null)   
				        item_type="NOTYPE";
					m.put(IDBConstants.ITEMMST_ITEM_TYPE, item_type);
					if (item_brand.equalsIgnoreCase("null") || item_brand == null)   
						item_brand="NOBRAND";
					m.put(IDBConstants.PRDBRANDID, item_brand);
                                        
					if (vinno.equalsIgnoreCase("null") || vinno == null)
						vinno = "";
					m.put(IDBConstants.VINNO, vinno);
					
					if (model.equalsIgnoreCase("null") || model == null)
						model = "";
					m.put(IDBConstants.MODEL, model);
					
					
					if (remark3.equalsIgnoreCase("null") || remark3 == null)
						remark3 = "";
					m.put(IDBConstants.ITEMMST_REMARK3, remark3);
					
					if (remark4.equalsIgnoreCase("null") || remark4 == null)
						remark4 = "";
					m.put(IDBConstants.ITEMMST_REMARK4, remark4);
					
					if (det_desc.equalsIgnoreCase("null") || det_desc == null)
						det_desc = "";
					m.put(IDBConstants.ITEMMST_REMARK1, det_desc);

					/*if (kit.equalsIgnoreCase("null") || kit == null)
						kit = "N";
					m.put(IConstants.ISKITTING, kit);*/
					
					/*m.put(IConstants.ISKITTING, "N");*/
					
					if (active.equalsIgnoreCase("null") || active == null)
						active = "Y";
					m.put(IConstants.ISACTIVE, active);
					if (isNonStock.equalsIgnoreCase("null") || isNonStock == null)
						isNonStock = "N";
					m.put(IConstants.ISBASICUOM, isApplyuom);
					
					m.put(IConstants.NONSTKFLAG, isNonStock);
					/*if (item_loc.equalsIgnoreCase("null") || item_loc == null)
						item_loc = "";
					m.put("ITEM_LOC", item_loc);*/
					m.put("ISCOMPRO",iscompro);
				    m.put("CPPI",cppi);
				    m.put("INCPRICE",incprice);
				    m.put("INCPRICEUNIT",incpriceunit);
				    m.put(IConstants.VENDNO,supplier);
				    m.put(IConstants.PRDDEPTID,item_dept_id);
					

				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			throw ex;
		} finally {

		}
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
        
		return hdrList;

	}

	@SuppressWarnings("unchecked")
	public ArrayList downloadItemSupplierSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
		String basecurrency = plantMstDAO.getbasecurrency(Plant);
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			String[] str = poi.readExcelFile(Integer.parseInt(AddItemSupplierNumberOfColumn),"Prodsup");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			ItemMstDAO itemMstDAO= new ItemMstDAO();
			int novalid =itemMstDAO.Itemcount(Plant);
			int convl =0;
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				novalid=novalid+1;
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				System.out.println("Total number of record per line  :  "+ list.size());
				if(list.size() > 0) {
					String item_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String Sup1 = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String Sup2 = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String Sup3 = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String Sup4 = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String Sup5 = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String Sup6 = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String Sup7 = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String Sup8 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String Sup9 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String Sup10 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					
					if(StrUtils.isCharAllowed(item_id) || StrUtils.isCharAllowed(Sup1) || StrUtils.isCharAllowed(Sup2) || StrUtils.isCharAllowed(Sup3) || StrUtils.isCharAllowed(Sup4) || StrUtils.isCharAllowed(Sup5) || StrUtils.isCharAllowed(Sup6) || StrUtils.isCharAllowed(Sup7) || StrUtils.isCharAllowed(Sup8) || StrUtils.isCharAllowed(Sup9) || StrUtils.isCharAllowed(Sup10)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					ItemUtil _itemutil = new ItemUtil();
					VendUtil vendUtil = new VendUtil();
					
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= item_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + item_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					if(!_itemutil.isExistsItemMst(item_id,Plant)) {
						throw new Exception("No Product Found at line number"+linecnt+".");
					}
					
					if (!Sup1.trim().equalsIgnoreCase("null") &&  Sup1 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup1, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 1 '" + Sup1 + "' is not created yet on system at Line "+linecnt+".");
						}
						}else Sup1="";
					
					if (!Sup2.trim().equalsIgnoreCase("null") &&  Sup2 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup2, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 2 '" + Sup2 + "' is not created yet on system at Line "+linecnt+".");
						}
						}else Sup2="";
					
					if (!Sup3.trim().equalsIgnoreCase("null") &&  Sup3 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup3, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 3 '" + Sup3 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup3="";
					
					if (!Sup4.trim().equalsIgnoreCase("null") &&  Sup4 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup4, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 4 '" + Sup4 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup4="";
					
					if (!Sup5.trim().equalsIgnoreCase("null") &&  Sup5 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup5, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 5 '" + Sup5 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup5="";
					
					if (!Sup6.trim().equalsIgnoreCase("null") &&  Sup6 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup6, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 6 '" + Sup6 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup6="";
					
					if (!Sup7.trim().equalsIgnoreCase("null") &&  Sup7 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup7, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 7 '" + Sup7 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup7="";
					
					if (!Sup8.trim().equalsIgnoreCase("null") &&  Sup8 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup8, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 8 '" + Sup8 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup8="";
					
					if (!Sup9.trim().equalsIgnoreCase("null") &&  Sup9 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup9, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 9 '" + Sup9 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup9="";
					
					if (!Sup10.trim().equalsIgnoreCase("null") &&  Sup10 != null){
						boolean isExistVendor = false;
						isExistVendor = vendUtil.isExistsVendor(Sup10, Plant);
						if(!isExistVendor){
							throw new Exception("In Supplier ID 10 '" + Sup10 + "' is not created yet on system at Line "+linecnt+".");
						}
					}else Sup10="";
					
					List<String> strings = Arrays.asList(Sup1,Sup2,Sup3,Sup4,Sup5,Sup6,Sup7,Sup8,Sup9,Sup10);
					
				        for (int i = 0; i < strings.size(); i++) {
				            String currentString = strings.get(i);
				            
				            for (int k = i + 1; k < strings.size(); k++) {
				                String otherString = strings.get(k);
				                String supname = "Supplier ID "+(k+1);
				                
				                // Check if the same value is present in multiple strings --imthi
				                if (currentString.equals(otherString)) {
				                    if (!Sup1.trim().equalsIgnoreCase("")){
						                if(Sup1.equals(Sup2)||Sup1.equals(Sup3)||Sup1.equals(Sup4)||Sup1.equals(Sup5)||Sup1.equals(Sup6)||Sup1.equals(Sup7)||Sup1.equals(Sup8)||Sup1.equals(Sup9)||Sup1.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"' Already Exists in Supplier ID 1 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup2.trim().equalsIgnoreCase("")){
						                if(Sup2.equals(Sup1)||Sup2.equals(Sup3)||Sup2.equals(Sup4)||Sup2.equals(Sup5)||Sup2.equals(Sup6)||Sup2.equals(Sup7)||Sup2.equals(Sup8)||Sup2.equals(Sup9)||Sup2.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 2 at line number"+linecnt+".");
						                }
						                }
						                if (!Sup3.trim().equalsIgnoreCase("")){
						                if(Sup3.equals(Sup1)||Sup3.equals(Sup2)||Sup3.equals(Sup4)||Sup3.equals(Sup5)||Sup3.equals(Sup6)||Sup3.equals(Sup7)||Sup3.equals(Sup8)||Sup3.equals(Sup9)||Sup3.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 3 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup4.trim().equalsIgnoreCase("")){
						                if(Sup4.equals(Sup1)||Sup4.equals(Sup2)||Sup4.equals(Sup3)||Sup4.equals(Sup5)||Sup4.equals(Sup6)||Sup4.equals(Sup7)||Sup4.equals(Sup8)||Sup4.equals(Sup9)||Sup4.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 4 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup5.trim().equalsIgnoreCase("")){
						                if(Sup5.equals(Sup1)||Sup5.equals(Sup2)||Sup5.equals(Sup3)||Sup5.equals(Sup4)||Sup5.equals(Sup6)||Sup5.equals(Sup7)||Sup5.equals(Sup8)||Sup5.equals(Sup9)||Sup5.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 5 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup6.trim().equalsIgnoreCase("")){
						                if(Sup6.equals(Sup1)||Sup6.equals(Sup2)||Sup6.equals(Sup3)||Sup6.equals(Sup4)||Sup6.equals(Sup5)||Sup6.equals(Sup7)||Sup6.equals(Sup8)||Sup6.equals(Sup9)||Sup6.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 6 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup7.trim().equalsIgnoreCase("")){
						                if(Sup7.equals(Sup1)||Sup7.equals(Sup2)||Sup7.equals(Sup3)||Sup7.equals(Sup4)||Sup7.equals(Sup5)||Sup7.equals(Sup6)||Sup7.equals(Sup8)||Sup7.equals(Sup9)||Sup7.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 7 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup8.trim().equalsIgnoreCase("")){
						                if(Sup8.equals(Sup1)||Sup8.equals(Sup2)||Sup8.equals(Sup3)||Sup8.equals(Sup4)||Sup8.equals(Sup5)||Sup8.equals(Sup6)||Sup8.equals(Sup7)||Sup8.equals(Sup9)||Sup8.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 8 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup9.trim().equalsIgnoreCase("")){
						                if(Sup9.equals(Sup1)||Sup9.equals(Sup2)||Sup9.equals(Sup3)||Sup9.equals(Sup4)||Sup9.equals(Sup5)||Sup9.equals(Sup6)||Sup9.equals(Sup7)||Sup9.equals(Sup9)||Sup9.equals(Sup10)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 9 at line number"+linecnt+".");
						                }
						                }

						                if (!Sup10.trim().equalsIgnoreCase("")){
						                if(Sup10.equals(Sup1)||Sup10.equals(Sup2)||Sup10.equals(Sup3)||Sup10.equals(Sup4)||Sup10.equals(Sup5)||Sup10.equals(Sup6)||Sup10.equals(Sup7)||Sup10.equals(Sup8)||Sup10.equals(Sup9)) {
						                	throw new Exception("In "+supname+" The '"+currentString+"'  Already Exists in Supplier ID 10 at line number"+linecnt+".");
						                }
						                }
				                }
				            }
				        }
					
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, item_id);
					m.put("SUP1", Sup1);
					m.put("SUP2", Sup2);
					m.put("SUP3", Sup3);
					m.put("SUP4", Sup4);
					m.put("SUP5", Sup5);
					m.put("SUP6", Sup6);
					m.put("SUP7", Sup7);
					m.put("SUP8", Sup8);
					m.put("SUP9", Sup9);
					m.put("SUP10", Sup10);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		Map mv = new HashMap();
		mv.put("ValidNumber", ValidNumber);
		hdrList.add(mv);
		return hdrList;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadContactSheetData(String Plant, String filename,String sheetName,String LoginUser) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		System.out.println("ContactNumberOfColumn :" + ContactNumberOfColumn);
		
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			
			String[] str = poi.readExcelFile(Integer.parseInt(ContactNumberOfColumn),"Cont");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				
				System.out.println("Total number of record per line  :  "
						+ list.size());
				
				if(list.size() > 0) {
					String company = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String country = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String industry = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String contactper = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String designation = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String telNo = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String contactNo = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String howWeKnow = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String email = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String salesCons = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String lifecycle = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String leadsts = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String salesprob = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String website = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String facebook = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String linkedln = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					String notes = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					
					ArrayList lifecyclests = new ArrayList();
					lifecyclests.add("Contact");
					lifecyclests.add("Customer");
					lifecyclests.add("Evangelist");
					lifecyclests.add("Lead");
					lifecyclests.add("Marketing Qualified Lead");
					lifecyclests.add("Other");
					lifecyclests.add("Opportunity");
					lifecyclests.add("Pilot offer");
					lifecyclests.add("Prospect");
					lifecyclests.add("Probability");
					lifecyclests.add("Proposal");
					lifecyclests.add("Qualified Lead");
					lifecyclests.add("Subscriber");
					lifecyclests.add("Sales Qualified Lead");
					
					ArrayList leadstatus = new ArrayList();
					leadstatus.add("New");
					leadstatus.add("Open");
					leadstatus.add("In Progress");
					leadstatus.add("Open Deal");
					leadstatus.add("Product Closure");
					leadstatus.add("Declined");
					leadstatus.add("Refused");
					
					ArrayList salprobPER = new ArrayList();
					salprobPER.add("25%");
					salprobPER.add("50%");
					salprobPER.add("75%");
					salprobPER.add("100%");
					
					if(!salesprob.contains("%")) 
						salesprob = salesprob+"%";
					
					String salesprobility="";
					if(salesprob.contains("0."))
						salesprobility  = salesprob.replace("0.", "");
					else
						salesprobility = salesprob;
				
					
					if (("").equals(company)|| ("null").equals(company)){
						throw new Exception("Company Name is mandatory and  missing at Line "+linecnt+" .");
					}
					
					if (("").equals(country)|| ("null").equals(country)){
						throw new Exception("Country is mandatory and  missing at Line "+linecnt+" .");
					}
					
					
					String countryname = new PlantMstDAO().getCOUNTRYNAME(Plant,country);
					if (countryname == null) 
						throw new Exception("Country  : '" + country + "' Does not Exist at Line "+linecnt+".");
					
					if (("").equals(email)|| ("null").equals(email)){
						throw new Exception("Email is mandatory and  missing at Line "+linecnt+" .");
					}
					
					if (("").equals(lifecycle)|| ("null").equals(lifecycle)){
						throw new Exception("LifeCycle Stage is mandatory and  missing at Line "+linecnt+" .");
					}
					
					if (!lifecyclests.contains(lifecycle))
						throw new Exception("LifeCycle Stage : '" + lifecycle + "' Does not Exist at Line "+linecnt+" (Use Contact,Customer,Evangelist,Lead,Marketing Qualified Lead,Other,Opportunity,Pilot offer,Prospect,Probability,Proposal,Qualified Lead,Subscriber,Sales Qualified Lead).");
					
					if (("").equals(leadsts)|| ("null").equals(leadsts)){
						throw new Exception("Lead Status is mandatory and  missing at Line "+linecnt+" .");
					}
					
					if (!leadstatus.contains(leadsts))
						throw new Exception("Lead Status : '" + leadsts + "' Does not Exist at Line "+linecnt+" (Use New,Open,In Progress,Open Deal,Product Closure,Declined,Refused).");
					
					if (!salprobPER.contains(salesprobility))
						throw new Exception("Sales Probability : '" + salesprob + "' Does not Exist at Line "+linecnt+"(Use 25%,50%,75%,100%).");
					
					boolean isValidItemData = false;
					if(PO_REMARK_LEN >= company.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Company Name : '" + company + "' length has exceeded the maximum value of "+PO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					int companyCount=0;
						companyCount = new PlantMstDAO().getCompanyCount(salesCons);
						if (("").equals(salesCons)|| ("null").equals(salesCons)) {
							salesCons = LoginUser;
							companyCount = 1;
						}
						if(companyCount == 0)
							throw new Exception("Sales Consultant : '" + salesCons + "' Does not Exist at Line "+linecnt+".");
					
					
					
					m.put("PLANT", Plant);
					m.put("COMPANY", company);
					m.put("COUNTRY", countryname);
					m.put("INDUSTRY", industry);
					m.put("CONTACT_PERSON", contactper);
					m.put("DESIGNATION", designation);
					m.put("TEL_NO", telNo);
					m.put("CONTACT_NO", contactNo);
					m.put("HOW_WE_KNOW", howWeKnow);
					m.put("EMAIL", email);
					m.put("SALES_CONS", salesCons);
					m.put("LIFECYCLE", lifecycle);
					m.put("LEAD_STATUS", leadsts);
					m.put("SALES_PROB", salesprob);
					m.put("WEBSITE", website);
					m.put("FACEBOOK", facebook);
					m.put("LINKEDLN", linkedln);
					m.put("NOTES", notes);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		
		return hdrList;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadMinMaxSheetData(String Plant, String filename,String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		System.out.println("MinMaxNumberOfColumn :" + MinMaxNumberOfColumn);
		
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			
			String[] str = poi.readExcelFile(Integer.parseInt(MinMaxNumberOfColumn),"MinMaxPrd");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				
				System.out.println("Total number of record per line  :  "
						+ list.size());
				
				if(list.size() > 0) {
					String prd_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String Minqty = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String Maxqty = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					
					if (("").equals(prd_id)|| ("null").equals(prd_id)){
						throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
					}
					if(StrUtils.isCharAllowed(prd_id) || StrUtils.isCharAllowed(prd_id)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= prd_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + prd_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isExists = new ItemUtil().isExistsItemMst(prd_id,Plant);
					if (!isExists) {
						throw new Exception("Create Product ID  : '" + prd_id + "' first, before adding Min/Max Quantity at Line "+linecnt+".");
					}
					
					if (Minqty.equalsIgnoreCase("null") || Minqty == null)
						Minqty = "0";
					if (Maxqty.equalsIgnoreCase("null") || Maxqty == null)
						Maxqty = "0";
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, prd_id);
					m.put(IConstants.STKQTY, Minqty);
					m.put(IConstants.MAXSTKQTY, Maxqty);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		
		return hdrList;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadOutletMinMaxSheetData(String Plant, String filename,String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		System.out.println("OutletMinMaxNumberOfColumn :" + OutletMinMaxNumberOfColumn);
		
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			
			String[] str = poi.readExcelFile(Integer.parseInt(OutletMinMaxNumberOfColumn),"OutletMinMaxPrd");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				
				System.out.println("Total number of record per line  :  "
						+ list.size());
				
				if(list.size() > 0) {
					String prd_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String outlet = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String Minqty = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String Maxqty = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					
					if (("").equals(prd_id)|| ("null").equals(prd_id)){
						throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
					}
					if(StrUtils.isCharAllowed(prd_id) || StrUtils.isCharAllowed(prd_id)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= prd_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + prd_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isExists = new ItemUtil().isExistsItemMst(prd_id,Plant);
					if (!isExists) {
						throw new Exception("Create Product ID  : '" + prd_id + "' first, before adding Min/Max Quantity at Line "+linecnt+".");
					}
					
					if (!new OutletUtil().isExistOutlet(outlet, Plant) && !new OutletUtil().isExistOutletName(outlet, Plant))
					{
						throw new Exception("Create Outlet  : '" + outlet + "' first, before adding Outlet Min/Max Quantity at Line "+linecnt+".");
					}
					
					if (Minqty.equalsIgnoreCase("null") || Minqty == null)
						Minqty = "0";
					if (Maxqty.equalsIgnoreCase("null") || Maxqty == null)
						Maxqty = "0";
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, prd_id);
					m.put(IConstants.OUTLET, outlet);
					m.put(IConstants.STKQTY, Minqty);
					m.put(IConstants.MAXSTKQTY, Maxqty);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		
		return hdrList;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadItemDescSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
		String basecurrency = plantMstDAO.getbasecurrency(Plant);
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			String[] str = poi.readExcelFile(Integer.parseInt(ItemDescNumberOfColumn),"Desc");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			ItemMstDAO itemMstDAO= new ItemMstDAO();
			int novalid =itemMstDAO.Itemcount(Plant);
			int convl =0;
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				novalid=novalid+1;
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				System.out.println("Total number of record per line  :  "+ list.size());
				if(list.size() > 0) {
					String item_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String item_desc1 = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String item_desc2 = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String item_desc3 = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String item_desc4 = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String item_desc5 = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String item_desc6 = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String item_desc7 = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String item_desc8 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String item_desc9 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String item_desc10 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					
					if(StrUtils.isCharAllowed(item_id) || StrUtils.isCharAllowed(item_desc1) || StrUtils.isCharAllowed(item_desc2) || StrUtils.isCharAllowed(item_desc3) || StrUtils.isCharAllowed(item_desc4) || StrUtils.isCharAllowed(item_desc5) || StrUtils.isCharAllowed(item_desc6) || StrUtils.isCharAllowed(item_desc7) || StrUtils.isCharAllowed(item_desc8) || StrUtils.isCharAllowed(item_desc9) || StrUtils.isCharAllowed(item_desc10)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					ItemUtil _itemutil = new ItemUtil();
					
					if(!(_itemutil.isExistsItemMst(item_id,Plant))) {
						throw new Exception("No Product Found at line number"+linecnt+".");
					}
					
					boolean isValidItemData = false;
					if(ITEM_LEN >= item_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + item_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					boolean isValidItemDescData = false;
					if(item_desc1.length()>2000){
						item_desc1 = item_desc1.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc1.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 1 value :  '" + item_desc1 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc2.length()>2000){
						item_desc2 = item_desc2.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc2.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 2 value :  '" + item_desc2 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc3.length()>2000){
						item_desc3 = item_desc3.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc3.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 3 value :  '" + item_desc3 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc4.length()>2000){
						item_desc4 = item_desc4.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc4.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 4 value :  '" + item_desc4 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc5.length()>2000){
						item_desc5 = item_desc5.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc5.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 5 value :  '" + item_desc5 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc6.length()>2000){
						item_desc6 = item_desc6.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc6.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 6 value :  '" + item_desc6 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc7.length()>2000){
						item_desc7 = item_desc7.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc7.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 7 value :  '" + item_desc7 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc8.length()>2000){
						item_desc8 = item_desc8.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc8.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 8 value :  '" + item_desc8 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc9.length()>2000){
						item_desc9 = item_desc9.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc9.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 9 value :  '" + item_desc9 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// ITEM DESC field data validation
					if(item_desc10.length()>2000){
						item_desc10 = item_desc10.substring(0,ADD_ITEM_DESC_LEN);
					}
					if(ADD_ITEM_DESC_LEN >= item_desc10.length()){
						isValidItemDescData = true;
					}
					if(!isValidItemDescData){
						throw new Exception("Product Description 10 value :  '" + item_desc10 + "' length has exceeded the maximum value of "+ADD_ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, item_id);
					if (item_desc1.equalsIgnoreCase("null")|| item_desc1 == null) {
						item_desc1="";
					}
					if (item_desc2.equalsIgnoreCase("null")|| item_desc2 == null) {
						item_desc2="";
					}
					if (item_desc3.equalsIgnoreCase("null")|| item_desc3 == null) {
						item_desc3="";
					}
					if (item_desc4.equalsIgnoreCase("null")|| item_desc4 == null) {
						item_desc4="";
					}
					if (item_desc5.equalsIgnoreCase("null")|| item_desc5 == null) {
						item_desc5="";
					}
					if (item_desc6.equalsIgnoreCase("null")|| item_desc6 == null) {
						item_desc6="";
					}
					if (item_desc7.equalsIgnoreCase("null")|| item_desc7 == null) {
						item_desc7="";
					}
					if (item_desc8.equalsIgnoreCase("null")|| item_desc8 == null) {
						item_desc8="";
					}
					if (item_desc9.equalsIgnoreCase("null")|| item_desc9 == null) {
						item_desc9="";
					}
					if (item_desc10.equalsIgnoreCase("null")|| item_desc10 == null) {
						item_desc10="";
					}
					m.put("ITEM_DESC1", item_desc1);
					m.put("ITEM_DESC2", item_desc2);
					m.put("ITEM_DESC3", item_desc3);
					m.put("ITEM_DESC4", item_desc4);
					m.put("ITEM_DESC5", item_desc5);
					m.put("ITEM_DESC6", item_desc6);
					m.put("ITEM_DESC7", item_desc7);
					m.put("ITEM_DESC8", item_desc8);
					m.put("ITEM_DESC9", item_desc9);
					m.put("ITEM_DESC10", item_desc10);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		Map mv = new HashMap();
		mv.put("ValidNumber", ValidNumber);
		hdrList.add(mv);
		return hdrList;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadItemimgSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
		String basecurrency = plantMstDAO.getbasecurrency(Plant);
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			String[] str = poi.readExcelFile(Integer.parseInt(ItemImgNumberOfColumn),"Img");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			ItemMstDAO itemMstDAO= new ItemMstDAO();
			int novalid =itemMstDAO.Itemcount(Plant);
			int convl =0;
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				novalid=novalid+1;
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				System.out.println("Total number of record per line  :  "+ list.size());
				if(list.size() > 0) {
					String item_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String item_img1 = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String item_img2 = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String item_img3 = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String item_img4 = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String item_img5 = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					
					if(StrUtils.isCharAllowed(item_id)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}

					ItemUtil _itemutil = new ItemUtil();
					
					if(!(_itemutil.isExistsItemMst(item_id,Plant))) {
						throw new Exception("No Product Found at line number"+linecnt+".");
					}
					
					boolean isValidItemData = false;
					if(ITEM_LEN >= item_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + item_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					if(item_img1.equals("null") )
                    {
						item_img1 = "";
                    }
					if(item_img1.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(item_img1);
						if(file.exists()) {
							iscatalog = true;
						}
						else{
							throw new Exception("Product image 1 not found in the path '" + item_img1 + "' at Line"+linecnt+".");
						}
					}
					
					if(item_img2.equals("null") )
					{
						item_img2 = "";
					}
					if(item_img2.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(item_img2);
						if(file.exists()) {
							iscatalog = true;
						}
						else{
							throw new Exception("Product image 2 not found in the path '" + item_img2 + "' at Line"+linecnt+".");
						}
					}
					
					if(item_img3.equals("null") )
                    {
						item_img3 = "";
                    }
					if(item_img3.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(item_img3);
						if(file.exists()) {
							iscatalog = true;
						}
						else{
							throw new Exception("Product image 3 not found in the path '" + item_img3 + "' at Line"+linecnt+".");
						}
					}
					
					if(item_img4.equals("null") )
                    {
						item_img4 = "";
                    }
					if(item_img4.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(item_img4);
						if(file.exists()) {
							iscatalog = true;
						}
						else{
							throw new Exception("Product image 4 not found in the path '" + item_img4 + "' at Line"+linecnt+".");
						}
					}
					
					if(item_img5.equals("null") )
                    {
						item_img5 = "";
                    }
					if(item_img5.length()>0){
						boolean iscatalog = false;
						boolean imageSizeflg = false;
						File file = new File(item_img5);
						if(file.exists()) {
							iscatalog = true;
						}
						else{
							throw new Exception("Product image 5  not found in the path '" + item_img5 + "' at Line"+linecnt+".");
						}
					}
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, item_id);
					m.put("IMG1", item_img1);
					m.put("IMG2", item_img2);
					m.put("IMG3", item_img3);
					m.put("IMG4", item_img4);
					m.put("IMG5", item_img5);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		Map mv = new HashMap();
		mv.put("ValidNumber", ValidNumber);
		hdrList.add(mv);
		return hdrList;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadAddItemSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		filename = DbBean.CountSheetUploadPath + filename;
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		String ValidNumber="";
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
		String basecurrency = plantMstDAO.getbasecurrency(Plant);
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			String[] str = poi.readExcelFile(Integer.parseInt(AddItemNumberOfColumn),"ADProd");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
			ItemMstDAO itemMstDAO= new ItemMstDAO();
			int novalid =itemMstDAO.Itemcount(Plant);
			int convl =0;
			for (int j = 1; j < str.length; j++) {
				String s1 = str[j];
				String s2 = "";
				novalid=novalid+1;
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				System.out.println("Record " + j + "\t: " + s1);
				List list = new LinkedList(); // Doubly-linked list
				list = new ArrayList();
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}
				int linecnt = j;
				linecnt = linecnt+1;
				Map m = new HashMap();
				System.out.println("Total number of record per line  :  "+ list.size());
				if(list.size() > 0) {
					String item_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String Item1 = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String Item2 = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String Item3 = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String Item4 = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String Item5 = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String Item6 = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String Item7 = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String Item8 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String Item9 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String Item10 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					
					if(StrUtils.isCharAllowed(item_id) || StrUtils.isCharAllowed(Item1) || StrUtils.isCharAllowed(Item2) || StrUtils.isCharAllowed(Item3) || StrUtils.isCharAllowed(Item4) || StrUtils.isCharAllowed(Item5) || StrUtils.isCharAllowed(Item6) || StrUtils.isCharAllowed(Item7) || StrUtils.isCharAllowed(Item8) || StrUtils.isCharAllowed(Item9) || StrUtils.isCharAllowed(Item10)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					ItemUtil _itemutil = new ItemUtil();
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= item_id.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID  value : '" + item_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					if(!_itemutil.isExistsItemMst(item_id,Plant)) {
						throw new Exception("In Additional Product 1 No Product Found at line number"+linecnt+".");
					}
					if (!Item1.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item1,Plant))) {
							throw new Exception("In Additional Product 2 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item2.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item2,Plant))) {
							throw new Exception("In Additional Product 3 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item3.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item3,Plant))) {
							throw new Exception("In Additional Product 4 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item4.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item4,Plant))) {
							throw new Exception("No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item5.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item5,Plant))) {
							throw new Exception("In Additional Product 5 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item6.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item6,Plant))) {
							throw new Exception("In Additional Product 6 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item7.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item7,Plant))) {
							throw new Exception("In Additional Product 7 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item8.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item8,Plant))) {
							throw new Exception("In Additional Product 8 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item9.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item9,Plant))) {
							throw new Exception("In Additional Product 9 No Product Found at line number"+linecnt+".");
						}
					}
					if (!Item10.equalsIgnoreCase("null")) {
						if(!(_itemutil.isExistsItemMst(Item10,Plant))) {
							throw new Exception("In Additional Product 10 No Product Found at line number"+linecnt+".");
						}
					}
					
					m.put("PLANT", Plant);
					m.put(IConstants.ITEM, item_id);
					if (Item1.equalsIgnoreCase("null")) {
						Item1="";
					}if (Item2.equalsIgnoreCase("null")) {
						Item2="";
					}if (Item3.equalsIgnoreCase("null")) {
						Item3="";
					}if (Item4.equalsIgnoreCase("null")) {
						Item4="";	
					}if (Item5.equalsIgnoreCase("null")) {
						Item5="";
					}if (Item6.equalsIgnoreCase("null")) {
						Item6="";	
					}if (Item7.equalsIgnoreCase("null")) {
						Item7="";						
					}if (Item8.equalsIgnoreCase("null")) {
						Item8="";
					}if (Item9.equalsIgnoreCase("null")) {
						Item9="";
					}if (Item10.equalsIgnoreCase("null")) {
						Item10="";	
					}
				
					m.put("ITEM1", Item1);
					m.put("ITEM2", Item2);
					m.put("ITEM3", Item3);
					m.put("ITEM4", Item4);
					m.put("ITEM5", Item5);
					m.put("ITEM6", Item6);
					m.put("ITEM7", Item7);
					m.put("ITEM8", Item8);
					m.put("ITEM9", Item9);
					m.put("ITEM10", Item10);
				}
				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		Map mv = new HashMap();
		mv.put("ValidNumber", ValidNumber);
		hdrList.add(mv);
		return hdrList;
	}
	
    @SuppressWarnings("unchecked")
    public ArrayList downloadAltrPrdSheetData(String Plant, String filename,
                    String sheetName) throws Exception {
            System.out.println("********  Import Starts *********");

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            
           
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);
            System.out.println("AltrPrdNumberOfColumn :" + AltrPrdNumberOfColumn);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);

                    String[] str = poi.readExcelFile(Integer.parseInt(AltrPrdNumberOfColumn),"AltrPrd");
                    // String[] str =null;

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            String s2 = "";

                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }

                            int linecnt = j;
                            linecnt = linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                                    
                                    String prd_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String Altr_Prd_Id = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                   

                                    if(StrUtils.isCharAllowed(prd_id) || StrUtils.isCharAllowed(Altr_Prd_Id)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                    if (("").equals(prd_id)|| ("null").equals(prd_id)){
                                        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
                                    }
                                    if (("").equals(Altr_Prd_Id)|| ("null").equals(Altr_Prd_Id)){
                                        throw new Exception("Alternate Product value is mandatory and  missing at Line "+linecnt+" .");
                                    }
                                  
                                 
                                    // ITEM field data validation
                                    boolean isValidItemData = false;
                                    if(ITEM_LEN >= prd_id.length()){
                                            isValidItemData = true;
                                    }
                                    if(!isValidItemData){
                                            throw new Exception("Product ID  value : '" + prd_id + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    /*if(prd_id.length()>0){
                                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(prd_id);
                                        if(specialcharsnotAllowed.length()>0){
                                                throw new Exception("Product ID  value : '" + prd_id + "' has special characters "+specialcharsnotAllowed+" that are  not allowed at Line "+linecnt+".");
                                        }
                                    }*/
                                   
                                    
                                   
                                isValidItemData = false;
                                    if(ITEM_LEN >= Altr_Prd_Id.length()){
                                            isValidItemData = true;
                                    }
                                    if(!isValidItemData){
                                            throw new Exception("Alternate Product value :  '" + Altr_Prd_Id + "' length has exceeded the maximum value of "+ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                boolean isExists = new ItemUtil().isExistsItemMst(prd_id,Plant);
                                if (!isExists) {
                                   throw new Exception("Create Product ID  : '" + prd_id + "' first, before adding Alternate Product at Line "+linecnt+".");
                                }
                                
                                    m.put("PLANT", Plant);
                                    m.put(IConstants.ITEM, prd_id);
                                    m.put(IConstants.ALTERNATE_ITEM, Altr_Prd_Id);
                               
                                    

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    

	@SuppressWarnings("unchecked")
	public ArrayList downloadInventorySheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			// POIReadInventory poi = new POIReadInventory(filename, sheetName);
	        

			String[] str = poi.readExcelFile(Integer
					.parseInt(InvNumberOfColumn),"Inv");

			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				
				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}
                            int linecnt =j;
			    linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String item = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String loc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String batch = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String avgUnitCost = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String expDate = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					
					if(StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(loc) || StrUtils.isCharAllowed(batch) || StrUtils.isCharAllowed(expDate)|| StrUtils.isCharAllowed(uom)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}

				    if (("").equals(item) || ("null").equals(item)){
                                        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
                                }
				    
				   /* ItemMstDAO itemMstDAO = new ItemMstDAO();
				    String parentItem = itemMstDAO.getKittingItem(Plant, item);
				    if(parentItem!=null){
				    	 throw new Exception(item + " is a parent product  and  cannot import at Line "+linecnt+" .");  
				    }
				    */
				    if (("").equals(loc) || ("null").equals(loc)){
				        throw new Exception("LOC value is mandatory and  missing at Line "+linecnt+" .");
				    }
					
				    
					// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= item.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID value : '" + item + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					
					// LOC field data validation
					boolean isValidLocData = false;
					if(LOC_LEN >= loc.length()){
						isValidLocData = true;
					}
					if(!isValidLocData){
						throw new Exception("LOC value '" + loc + "' length has exceeded the maximum value of "+LOC_LEN+" Chars at Line "+linecnt+".");
					}
					
					// LOC field data validation
					boolean isValidBatchData = false;
					if(BATCH_LEN >= batch.length()){
						isValidBatchData = true;
					}
					if(!isValidBatchData){
						throw new Exception("BATCH value :  '" + batch + "' length has exceeded the maximum value of "+BATCH_LEN+" Chars at Line "+linecnt+".");
					}
					
					//UOM field data validation
					boolean isValidUomData = false;
					 if(UOM >= uom.length()){
					         isValidUomData = true;
					 }
					 if(!isValidUomData){
					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
					 }
					 if (!uom.equalsIgnoreCase("null") && uom != null){
					 boolean isExistUom =false;					 
					
							UomUtil uomUtil = new UomUtil();
							isExistUom = uomUtil.isExistsUom(uom, Plant);
							if(!isExistUom){
								throw new Exception("UOM '" + uom + "' is not created/in active at line "+linecnt+".");
							}
					
					 }
                                        
				    /*if(batch.length()>0){
				        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(batch);
				        if(specialcharsnotAllowed.length()>0){
				                throw new Exception("Batch  value : '" + batch + "' has special characters "+specialcharsnotAllowed+" that are  not allowed at Line "+linecnt+".");
				        }
				    }*/
					
					// data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					boolean isCorrectDateFormat = false;
					if (!expDate.trim().equalsIgnoreCase("null") && expDate != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(expDate);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + expDate + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.trim().equalsIgnoreCase("null") && qty != null){
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if(!isCorrectQuantityFormat){
							throw new Exception("Quantity value '" + qty + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// validation against database entries
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					isExistItem = itemUtil.isExistsItemMst(item, Plant);
					if(!isExistItem){
						throw new Exception("Product '" + item + "' is not created yet  at Line "+linecnt+".");
					}
					
					
					// item validation
					String isnonstockItem = "";
					ItemMstDAO itemmstdao = new ItemMstDAO();
					isnonstockItem = itemmstdao.getNonStockFlag(Plant, item);
					if(isnonstockItem.equalsIgnoreCase("Y")){
						throw new Exception("Product: '" + item + "' is non inventory item.Cannot import inventory for non inventory item  at Line "+linecnt+".");
					}
					
					
					// Loc validation
					boolean isExistLoc = false;
					LocUtil locUtil = new LocUtil();
					isExistLoc = locUtil.isValidLocInLocmst(Plant, loc);
					if(!isExistLoc){
						throw new Exception("Location '" + loc + "' is not valid location  at Line "+linecnt+".");
					}

					m.put(IDBConstants.PLANT, Plant);
					m.put(IDBConstants.ITEM, item);
					m.put(IDBConstants.LOC, loc);
					if (uom.equalsIgnoreCase("null")|| uom == null)
						uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"INVENTORYUOM");
						m.put(IConstants.IN_UNITMO, uom);
					if (batch == "" || batch.equalsIgnoreCase("null"))
						batch = "NOBATCH";
					m.put(IDBConstants.BATCH, batch);
					
					if (qty == "" || qty.equalsIgnoreCase("null"))
						qty = "0";
					m.put(IConstants.QTY,qty);
					
					if (expDate == "" || expDate.equalsIgnoreCase("null"))
						expDate = "";
					m.put(IConstants.EXPIREDATE, expDate);
					m.put("AVERAGEUNITCOST", avgUnitCost);
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadInventoryDisplaySheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");
		
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		filename = DbBean.CountSheetUploadPath + filename;
		
		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			// POIReadInventory poi = new POIReadInventory(filename, sheetName);
			
			
			String[] str = poi.readExcelFile(Integer
					.parseInt(InvOrdNumberOfColumn),"InvOrd");
			
			System.out
			.println("Total number of record found in Excel sheet  :  "
					+ str.length);
			
			for (int j = 1; j < str.length; j++) {
				
				String s1 = str[j];
				
				StringTokenizer parser = new StringTokenizer(s1, "|||");
				
				System.out.println("Record " + j + "\t: " + s1);
				
				List list = new LinkedList(); // Doubly-linked list
				
				list = new ArrayList();
				
				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
					
				}
				int linecnt =j;
				linecnt= linecnt+1;
				Map m = new HashMap();
				
				System.out.println("Total number of record per line  :  "
						+ list.size());
				
				if(list.size() > 0) {
					
					String CustName = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String PrdId = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
//					String itemdesc = StrUtils.fString(new ItemMstDAO().getItemDesc(Plant,PrdId));
//					String PrdDesc = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String Qty = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String MaxQty = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					
					if(StrUtils.isCharAllowed(CustName) || StrUtils.isCharAllowed(PrdId) || StrUtils.isCharAllowed(Qty)  || StrUtils.isCharAllowed(MaxQty)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					//customer validation
					String customer = "";
//   					String custid = "";
//   					CustUtil custUtil = new CustUtil();
//   					if (("").equals(customer)|| ("null").equals(customer)){
//   						ArrayList arrList = custUtil.getCustomerListStartsWithName(CustName,Plant);
//   						if(!arrList.isEmpty()) { 
//							Map mCustot=(Map)arrList.get(0);
//							customer=(String) mCustot.get("CNAME");
//   						}else {
//   							throw new Exception("Customer Name'" + CustName + "' is not created yet on system at Line "+linecnt+" .");
//   						}
//   					}else {
//   						boolean isValidCustomer = false;
//   						if(CUSTNO_LEN > customer.length()){
//   							isValidCustomer = true;
//   						}
//   						if(!isValidCustomer){
//   							throw new Exception("Customer Name :  '" + customer + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
//   						} 
//   						boolean isExistCust= false;
//   						if(customer.length()>0){
//   						isExistCust = custUtil.isExistCustomer(customer, Plant);
//   						if(!isExistCust){
//   							throw new Exception("Customer '" + customer + "' is not created yet on system at Line "+linecnt+" .");
//   						}else {
//   							ArrayList arrList = custUtil.getCustomerListStartsWithName(customer,Plant);
//   							Map mCustot=(Map)arrList.get(0);
//   							custid=(String) mCustot.get("CUSTNO");
//   						}
//   						}
//   					}
   					
   					// customer validation
					if (("").equals(CustName)|| ("null").equals(CustName)){
						throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}
					boolean isValidCustomer = false;
					if(CUSTNO_LEN > customer.length()){
						isValidCustomer = true;
					}
					if(!isValidCustomer){
						throw new Exception("Customer Code :  '" + customer + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isExistCustomer = false;
					isExistCustomer =new CustUtil().isExistCustomerCode(CustName, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer Code'" + CustName + "' is not created yet on system.");
					}
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", PrdId);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + PrdId + "' is not created/in active at Line " + linecnt + ".");
					}
					
					//if (("").equals(PrdId)|| ("null").equals(PrdId)){
						//throw new Exception("Product Id is mandatory and  missing at Line "+linecnt+" .");
					//}
					
					// ITEM DESC field data validation
//					if (!PrdId.equalsIgnoreCase("null") && !PrdId.equalsIgnoreCase("") && PrdId != null && PrdDesc.equalsIgnoreCase("null") && PrdDesc.equalsIgnoreCase("") && PrdDesc == null )
//					{
//						if (("").equals(PrdDesc)|| ("null").equals(PrdDesc)){
//	                         throw new Exception("Product Description value is mandatory and  missing at Line "+linecnt+" .");
//	                     }
//						
//					}
					 
//					 boolean isValidItemDescData = false;
//					
//					if(ITEM_DESC_LEN >= PrdDesc.length()){
//						isValidItemDescData = true;
//					}
//					if(!isValidItemDescData){
//						throw new Exception("Product Description value :  '" + PrdDesc + "' length has exceeded the maximum value of "+ITEM_DESC_LEN+" Chars at Line "+linecnt+".");
//					}
					
//					if (!PrdId.equalsIgnoreCase("null") && !PrdId.equalsIgnoreCase("") && PrdId != null && PrdDesc.equalsIgnoreCase("null") && PrdDesc.equalsIgnoreCase("") && PrdDesc == null ){
					String itemdesc = StrUtils.fString(new ItemMstDAO().getItemDesc(Plant,PrdId));
//					}
					
					ImportUtils impUtil = new ImportUtils();
					
					
					//order quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!Qty.trim().equalsIgnoreCase("null") && Qty != null){
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(Qty);
						if(!isCorrectQuantityFormat){
							throw new Exception("Order Quantity value '" + Qty + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					//Max order quantity field validation
					boolean isCorrectQuantityFormats = false;
					if (!MaxQty.trim().equalsIgnoreCase("null") && MaxQty != null){
						isCorrectQuantityFormats = impUtil.isCorrectQuantityFormat(MaxQty);
						if(!isCorrectQuantityFormats){
							throw new Exception("Max Order Quantity value '" + Qty + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					m.put(IDBConstants.PLANT, Plant);
					m.put("CUSTNO", CustName);
					m.put(IDBConstants.ITEM, PrdId);
					//if (!PrdId.equalsIgnoreCase("null") && PrdDesc.equalsIgnoreCase("null")){
						m.put(IConstants.ITEM_DESC, itemdesc);
					//}else {
						//m.put(IConstants.ITEM_DESC, PrdDesc);
					//}
//					m.put(IConstants.ITEM_DESC,StrUtils.fString(new ItemMstDAO().getItemDesc(Plant,PrdId)));
					if (Qty == "" || Qty.equalsIgnoreCase("null"))
						Qty = "0.000";
					m.put("ORDER_QTY", Qty);
					if (MaxQty == "" || MaxQty.equalsIgnoreCase("null"))
						MaxQty = "0.000";
					m.put("MAX_ORDER_QTY", MaxQty);
				}
				
				hdrList.add(m);
				
				System.out.println("hdrList :: " + hdrList.size());
			}
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			
		}
		
		Map mv = new HashMap();
		mv.put("ValidNumber", "");
		hdrList.add(mv);
		
		return hdrList;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadStockTakeSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
			        

			String[] str = poi.readExcelFile(Integer
					.parseInt(StockTakeNumberOfColumn),"StockTake");

			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				
				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}
                            int linecnt =j;
			    linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());
               
				if(list.size() > 0) {
					System.out.println("Come back inventory 2");
					String item = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					String loc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String batch = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
										
					if(StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(loc) || StrUtils.isCharAllowed(batch) || StrUtils.isCharAllowed(uom) ){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}

				    if (("").equals(item) || ("null").equals(item)){
                                        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
                                }
				    
				     if (("").equals(loc) || ("null").equals(loc)){
				        throw new Exception("LOC value is mandatory and  missing at Line "+linecnt+" .");
				    }
					
				  	// ITEM field data validation
					boolean isValidItemData = false;
					if(ITEM_LEN >= item.length()){
						isValidItemData = true;
					}
					if(!isValidItemData){
						throw new Exception("Product ID value : '" + item + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars at Line "+linecnt+".");
					}
					// LOC field data validation
					boolean isValidLocData = false;
					if(LOC_LEN >= loc.length()){
						isValidLocData = true;
					}
					if(!isValidLocData){
						throw new Exception("LOC value '" + loc + "' length has exceeded the maximum value of "+LOC_LEN+" Chars at Line "+linecnt+".");
					}
					// LOC field data validation
					boolean isValidBatchData = false;
					if(BATCH_LEN >= batch.length()){
						isValidBatchData = true;
					}
					if(!isValidBatchData){
						throw new Exception("BATCH value :  '" + batch + "' length has exceeded the maximum value of "+BATCH_LEN+" Chars at Line "+linecnt+".");
					}
                                        
				 	ImportUtils impUtil = new ImportUtils();
				 				
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.trim().equalsIgnoreCase("null") && qty != null){
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if(!isCorrectQuantityFormat){
							throw new Exception("Quantity value '" + qty + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// validation against database entries
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					isExistItem = itemUtil.isExistsItemMst(item, Plant);
					if(!isExistItem){
						throw new Exception("Product '" + item + "' is not created yet  at Line "+linecnt+".");
					}
					// validation against database entries
					
					// item validation
					String isnonstockItem = "";
					ItemMstDAO itemmstdao = new ItemMstDAO();
					isnonstockItem = itemmstdao.getNonStockFlag(Plant, item);
					if(isnonstockItem.equalsIgnoreCase("Y")){
						throw new Exception("Product: '" + item + "' is non inventory item.Cannot import inventory for non inventory item  at Line "+linecnt+".");
					}
					
					// Loc validation
					boolean isExistLoc = false;
					LocUtil locUtil = new LocUtil();
					isExistLoc = locUtil.isValidLocInLocmst(Plant, loc);
					if(!isExistLoc){
						throw new Exception("Location '" + loc + "' is not valid location  at Line "+linecnt+".");
					}
					
					//Validte UOM
					if(uom.equalsIgnoreCase("null"))
					{
						uom="";
					}
					if (qty == "" || qty.equalsIgnoreCase("null"))
						qty = "0";
					
					if (qty != "0")
					{
						if(uom.equalsIgnoreCase(""))
						{
							throw new Exception("Enter UOM at Line "+linecnt + ".");
						}
					}
					boolean isValidUomData = false;
					 if(UOM >= uom.length()){
					         isValidUomData = true;
					 }
					 if(!isValidUomData){
					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
					 }
					 if (!uom.equalsIgnoreCase("null") && uom != null){
					 boolean isExistUom =false;
					// htitem.put("STKUOM",uom);
					 m.put("STKUOM",uom);
							UomUtil uomUtil = new UomUtil();
							isExistUom = uomUtil.isExistsUom(uom, Plant);
							if(!isExistUom){
								throw new Exception("UOM '" + uom + "' is not created/in active at Line "+linecnt + ".");
							}
					
					 }
					 
					m.put(IDBConstants.PLANT, Plant);
				    m.put(IDBConstants.ITEM, item);
					m.put(IDBConstants.LOC, loc);
					if (batch == "" || batch.equalsIgnoreCase("null"))
						batch = "NOBATCH";
					m.put(IDBConstants.BATCH, batch);					
					m.put(IConstants.QTY,qty);
					if (uom.equalsIgnoreCase("null")|| uom == null || uom=="")
						uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"ISNULL(INVENTORYUOM,'') AS UOM");
			        if(uom == null)
			        	uom="";
						m.put("UOM",uom);
						
						String INVQTY="0",INVFLAG="0";
						InvMstDAO  _InvMstDAO  = new InvMstDAO();
			            ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOMWithNegQty(Plant,item,loc,batch,uom);
			            if (listQry.size() > 0) {	               
			         	   for(int i =0; i<listQry.size(); i++) {
			                   Map arrCustLine = (Map)listQry.get(i);
			                   INVQTY = (String)arrCustLine.get("qty");
			         	   }
			            }
			            
			            double stk = Double.parseDouble(qty);
						double invqty = Double.parseDouble(INVQTY);
						double diff = (invqty - stk);
						String diffval = new StrUtils().addZeroes((diff), "3");
						
						if(stk==invqty)
							INVFLAG="1";
						
						m.put("DIFFQTY", diffval);
						m.put("INVFLAG", INVFLAG);
						m.put("INV_QTY", INVQTY);
						
				}

				hdrList.add(m);
				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	}

	@SuppressWarnings("unchecked")
	public ArrayList downloadInboundSheetData(String Plant, String filename,
			String sheetName,String baseCurrency,String NOOFORDER,String region) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		String ValidNumber="";
		POUtil poUtil = new POUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer.parseInt(InboundNumberOfColumn),"IB");
			System.out.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			//Validate Order azees - 12/2020
			PoHdrDAO poHdrDAO = new PoHdrDAO();
			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid =poHdrDAO.Ordercount(Plant,FROM_DATE,TO_DATE);
			int convl =0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			  convl = Integer.valueOf(NOOFORDER);
			String validpo="";
			int validcot=0;
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";
				
				
				
				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt = linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
										
					String pono = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					//Validate Order azees - 12/2020
					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
					{
					int ddok =0;
					if(validpo.equalsIgnoreCase(""))
					{
						validpo=pono;
						ddok=1;
						
					}
					else {
					if(validpo.equalsIgnoreCase(pono))
					{
						validcot=validcot+1;
						ddok=validcot-1;
						if(validcot>0)
							validcot=validcot-1;
					}
					else {
						validpo=pono;
						ddok=1;
						
					}
					}
					
					novalid=novalid+ddok;
					
						if(novalid>convl)
						{
							ValidNumber=NOOFORDER;
							break;
						}
					}
					//End
					String supplier_code = StrUtils.replaceStr2Char(((String) list.get(0)).trim());//new changes done by azees Jan 21
					String shippingcustomer = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String transportmode = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					TransportModeDAO transportmodedao = new TransportModeDAO();
                    String trans = "";
					if(transportmode.length()>0){
						trans = transportmodedao.getTransportModeByName(Plant,transportmode);
					}
					else {
						
						transportmode = "";
					}
					if(transportmode.equals("null")) {
						transportmode = "" ;
						trans = "";
					}
					String project = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String paymenttype = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String paymentterms = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					
					String refNo = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String orderDate = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String orderTime = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String deliverydate = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String employeename = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String ordertype = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String incoterm = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String currencyID = StrUtils.replaceStr2Char(((String) list.get(15)).trim());			        
					String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					String inbound_gst = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
					String purcahseloc = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					String tax = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
					String reversechar = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String goodsipt = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
					String item = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
					String poLno = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
					String account = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
					String productdeliverydate = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
					String productdiscount = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
					String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(31)).trim());
					String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
					String orderdiscountAre = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
					String orderdiscountPer = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
					String shippingcostAre = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
					String adjustment = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
					String remarks = StrUtils.replaceStr2Char(((String) list.get(38)).trim());
				    String remarks2 = StrUtils.replaceStr2Char(((String) list.get(39)).trim());
				    
					String manufacturer ="null";//StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String localexpenses ="null";//String localexpenses = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String taxid="0";
					String projectid = "0";
					String shippingid = "";
					
					
					 String COUNTRYCODE = "";
		           	 String plantstate = "";
		           	 String plantstatecode = "";
		             PlantMstUtil plantmstutil = new PlantMstUtil();
		           	 List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
		           	 for (int i = 0; i < viewlistQry.size(); i++) {
		           	     Map map = (Map) viewlistQry.get(i);
		           	     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
		           	     plantstate = StrUtils.fString((String)map.get("STATE"));
		           	 }
					
		           	 MasterDAO masterDAO = new MasterDAO();
		        	 boolean ispuloc=false;		 
		        	 ArrayList purloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
		        	 if (purloctions.size() > 0) {
		        		 ispuloc = true;
		        	 }
					
					

					if(StrUtils.isCharAllowed(pono) || StrUtils.isCharAllowed(poLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(ordertype) || StrUtils.isCharAllowed(orderDate) || StrUtils.isCharAllowed(orderTime) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(inbound_gst) || StrUtils.isCharAllowed(supplier_code) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(manufacturer) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) ||StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(project) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(account) || StrUtils.isCharAllowed(productRatesAre)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					if (("").equals(supplier_code)|| ("null").equals(supplier_code)){
				        throw new Exception("Supplier Code value is mandatory and  missing at Line "+linecnt+" .");  
				    }
                   if (("").equals(pono)|| ("null").equals(pono)){
                                        throw new Exception("PONO value is mandatory and  missing at Line "+linecnt+" .");  
                                    }
                   if(ispuloc) {
	                   if (("").equals(purcahseloc)|| ("null").equals(purcahseloc)){
					        throw new Exception("Purchase Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
                   }
                   if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
                   /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
				        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
                   if (("").equals(poLno)|| ("null").equals(poLno)){
				        throw new Exception("POLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				  
				 // vendor validation
					boolean isExistVendor = false;
					VendUtil vendUtil = new VendUtil();
					isExistVendor = vendUtil.isExistsVendor(supplier_code, Plant);
					if(!isExistVendor){
						throw new Exception("Supplier '" + supplier_code + "' is not created yet on system at Line "+linecnt+".");
					}

					if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
						shippingcustomer = "";
					}
					
					boolean isValidShippingCustomer = false;
					if(SHIPPING_CUSTOMER_LEN > shippingcustomer.length()){
						isValidShippingCustomer = true;
					}
					if(!isValidShippingCustomer){
						throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
					}
					
                    if (trans == null){
							throw new Exception("Create Transport Mode : '" + transportmode + "' first, before adding Transport at Line "+linecnt+".");
					}
					
					//Shipping customer validation
					isExistVendor = false;
					CustUtil custUtil = new CustUtil();
					if(shippingcustomer.length()>0){
					isExistVendor = custUtil.isExistCustomer(shippingcustomer, Plant);
					if(!isExistVendor){
						throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system.");
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
						Map mCustot=(Map)arrList.get(0);
						shippingid=(String) mCustot.get("CUSTNO");
					}
					}
					
					//Project validation					
					boolean isValidproject = false;
					if(DET_DESC_LEN > project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistVendor = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistVendor = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistVendor){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
					
					 ArrayList arrCustSUP = custUtil.getVendorDetails(supplier_code,Plant);
					 String paytype = (String) arrCustSUP.get(17);
					  if(paymentterms.equalsIgnoreCase("null")|| paymentterms == null || paymentterms.equalsIgnoreCase("")) {
						  paymentterms= (String) arrCustSUP.get(39);
					  }
					  if(trans.equalsIgnoreCase("null")|| trans == null || trans.equalsIgnoreCase("")) {
						  trans= (String) arrCustSUP.get(38);
					  }
					// payment type data validation
					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
						paymenttype= paytype;
					}else if(paytype.equalsIgnoreCase("null")|| paytype == null || paytype.equalsIgnoreCase("")){
						paymenttype ="";
					}else {
					
						boolean isValidpaymenttype = false;
						if(OREDER_TYPE_LEN > paymenttype.length()){
							isValidpaymenttype = true;
						}
						if(!isValidpaymenttype){
							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
						}
						
						/*MasterDAO masterDAO = new MasterDAO();*/
						isValidpaymenttype=masterDAO.getPaymentModeisExist(Plant,paymenttype);
						if(!isValidpaymenttype){
			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding purchase at Line "+linecnt+".");
			     		}
					}
					
					 
					  
					// PONO field data validation
					boolean isValidPonoData = false;
					if(PONO_LEN >= pono.length()){
						isValidPonoData = true;
					}
					if(!isValidPonoData){
						throw new Exception("PONO value :  '" + pono + "' length has exceeded the maximum value of "+PONO_LEN+" Chars at Line "+linecnt+".");
					}
					
					// RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(PONO_REFNO >= refNo.length()){
				            isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				            throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
				    
				 // data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					boolean isCorrectDateFormat = false;
					if (!orderDate.equalsIgnoreCase("null") && orderDate != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(orderDate);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + orderDate + "' is not accepted format (mm/dd/yyyy) at Line "+linecnt+".");
						}
					}
					
					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!orderTime.equalsIgnoreCase("null") && orderTime != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(orderTime);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + orderTime + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// delivery date field validation
					if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
				    {
				    	deliverybydate="N";
				    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
		     		  {
		     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding purchase at Line "+linecnt+".");  
		     		  }
 				    boolean isCorrectDeliveryDateFormat = false;
					if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
						isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
						if(!isCorrectDeliveryDateFormat){
							throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}

					// order type validation
					if (!ordertype.equalsIgnoreCase("null") && ordertype != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(ordertype, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + ordertype + "' is not created yet on system at Line "+linecnt+".");
						}
					}
					
					// INCOTERM type validation
					boolean isValidINCOTERMS = false;
					if(INCOTERM_LEN > incoterm.length()){
						isValidINCOTERMS = true;
					}
					if(!isValidINCOTERMS){
						throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
					}
					
					// Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
                                            
                                            Hashtable ht = new Hashtable();
                                            ht.put(IDBConstants.PLANT, Plant);
                                            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
				    
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
			            currencyID = baseCurrency;
				    
				    // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
						
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }

					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!inbound_gst.trim().equalsIgnoreCase("null") && inbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(inbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("GST value '" + inbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}
					if (inbound_gst.equalsIgnoreCase("null")
							|| inbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "INBOUND");
						Map gstMap = (Map)gstDetails.get(0);
						inbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
					// Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
					
				// purcahseloc field validation				    
				boolean isValidPurchaseLocation = false;
					if(PURCHASE_LOCATION > purcahseloc.length()){
						isValidPurchaseLocation = true;
                }
                if(!isValidPurchaseLocation){
                        throw new Exception("Purchase Location value '" + purcahseloc + "' length has exceeded the maximum value of "+PURCHASE_LOCATION+" Chars at Line "+linecnt+".");
                }
                
               
                if(ispuloc) {
	               MasterUtil _MasterUtil=new  MasterUtil();
	     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(purcahseloc,Plant,COUNTRYCODE);
	     		   if(ccList.size()>0)
	     		   {
	     			  for (int i = 0; i < ccList.size(); i++) {
	     			     Map map1 = (Map) ccList.get(i);
	     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
	     			 }
	     		   }
	     		   else
	     		   {
	     			  throw new Exception("Enter Valid Purchase Location  : '" + purcahseloc + "', before adding purchase at Line "+linecnt+".");
	     		   }
                }
     		   
     		// tax field validation
     		  boolean isValidtax = false;
				if(REMARKS_LEN > tax.length()){
					isValidtax = true;
	          }
	          if(!isValidtax){
	                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
	          }
	          
	          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("INBOUND", COUNTRYCODE, tax);
	          if(taxtypes.size() > 0) {
	        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
	        		
	        			taxid = String.valueOf(finCountryTaxType.getID());	        			
	        			String display="";
			         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
			         	{
			         		display = finCountryTaxType.getTAXTYPE();
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
			         		display = finCountryTaxType.getTAXTYPE()+" ["+inbound_gst+"%]";
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
			         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
			         		if(plantstatecode.equalsIgnoreCase("")) {
			         			display = finCountryTaxType.getTAXTYPE();
			         		}else {
			         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
			         		}
			         	}
			         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
			         		if(plantstatecode.equalsIgnoreCase("")) {
			         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
			         		}else {
			         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
			         		}
			         	}
			         	else
			         	{
			         		if(plantstatecode.equalsIgnoreCase("")) {
			         			display = finCountryTaxType.getTAXTYPE()+" ["+inbound_gst+"%]";
			         		}else {
			         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+inbound_gst+"%]";
			         		}
			         	}
			         	
			         	tax=display;
	        			
	        		}
	          	}
	          else
	          {
	        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding purchase at Line "+linecnt+".");
	          }
	          
	          //Reverse Charge & Goods Import
	          if (reversechar.equals("null") || reversechar == null || reversechar.equals("") )
			    {
   			 reversechar="0";
			    }
   		  else if (reversechar.equalsIgnoreCase("Y")|| reversechar.equalsIgnoreCase("y"))
				{
					reversechar="1";
				}
   		  else if (reversechar.equalsIgnoreCase("N")|| reversechar.equalsIgnoreCase("n"))
				{
					reversechar="0";
				}
   		  else if(!reversechar.equalsIgnoreCase("N")|| !reversechar.equalsIgnoreCase("n")||!reversechar.equalsIgnoreCase("Y")|| !reversechar.equalsIgnoreCase("y"))
   		  {
   			 throw new Exception("Enter Valid Reverse Charge  : '" + reversechar + "', before adding purchase at Line "+linecnt+".");  
   		  }
				     		  
   		  if (goodsipt.equals("null") || goodsipt == null || goodsipt.equals("") )
			    {
   			goodsipt="0";
			    }
   		  else if (goodsipt.equalsIgnoreCase("Y")|| goodsipt.equalsIgnoreCase("y"))
				{
					goodsipt="1";
				}else if (goodsipt.equalsIgnoreCase("N")|| goodsipt.equalsIgnoreCase("n"))
				{
					goodsipt="0";
				}
				else if(!goodsipt.equalsIgnoreCase("N")|| !goodsipt.equalsIgnoreCase("n")||!goodsipt.equalsIgnoreCase("Y")|| !goodsipt.equalsIgnoreCase("y"))
	     		  {
	     			 throw new Exception("Enter Valid Goods Import  : '" + goodsipt + "', before adding purchase at Line "+linecnt+".");  
	     		  }
   		  
   		  if(reversechar.equals("1") && goodsipt.equals("1"))
   		  {
   			 throw new Exception("Reverse Charge & Goods Import can't be 'Y',Enter any one as 'Y' , before adding purchase at Line "+linecnt+".");
   		  }

   		  // Product Rates Are field validation
	   		if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
	   			productRatesAre = "Tax Exclusive";
		    }
 		  boolean isValidProductRatesAre    = false;
			if(EMPTYPE_LEN > productRatesAre.length()){
				isValidProductRatesAre = true;
          }
          if(!isValidProductRatesAre){
                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
          }
          
          if(productRatesAre.length()>0){
        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
        	  {}
        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
        	  {}
        	  else
        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding purchase at Line "+linecnt+".");
          }
          
                   // is valid Line No                                        
                   int isThisANumber = 0;
                   try
                   {
                    isThisANumber = Integer.parseInt(poLno); 
                   }
                   catch (NumberFormatException nfe)
                   {
                     throw new Exception("POLNNO value :  '" + poLno + "' for PONO "+pono+" is not a valid number  at Line "+linecnt+".");
                   }
                   boolean isValidLineNumber    = false;
       			if(LNNO_LEN >= poLno.length()){
       				isValidLineNumber = true;
                 }
                 if(!isValidLineNumber){
                         throw new Exception("POLNNO value '" + poLno + "' length has exceeded the maximum value of "+LNNO_LEN+" Chars at Line "+linecnt+".");
                 }
					
		// item validation
		boolean isExistItem = false;
		ItemUtil itemUtil = new ItemUtil();
                           Hashtable htitem= new Hashtable();
                           htitem.put("PLANT",Plant);
                           htitem.put("ITEM",item);
	        htitem.put("ISACTIVE","Y");
		isExistItem = itemUtil.isExistsItemMst(htitem);
		if(!isExistItem){
			throw new Exception("Product ID '" + item + "' is not created/in active at Line "+linecnt+".");
		}	                                      
			
		// Account field data validation
		boolean isValidAccount = false;
		if(PO_REMARK_LEN > account.length()){
			isValidAccount = true;
		}
		if(!isValidAccount){
			throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "+PO_REMARK_LEN+" Chars at Line "+linecnt+".");
		}
		
		if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
			account = "";
		}else {
		
			CoaDAO coadao = new CoaDAO();
			List<Map<String, String>> listQry = null;
			Map<String, List<Map<String, String>>> listGrouped=null;
			listQry = coadao.selectSubAccountTypeList(Plant,account);
			   if(listQry.size()>0)
			   {
				   String[] filterArray = {"3"};
					String[] detailArray= {"6"};
					listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
							.collect(Collectors.toList());
					listGrouped=listQry.stream()
							.filter(x->Arrays.stream(filterArray)
							.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
							.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
					
					if(listGrouped.size()>0)
					   {
					   
					   }
					else
					   {
						  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
					   }
			   }
			   else
			   {
				  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
			   }
		}
		
		// UOM field validation
					boolean isValidUomData = false;
					 if(UOM >= uom.length()){
					         isValidUomData = true;
					 }
					 if(!isValidUomData){
					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
					 }
					 if (!uom.equalsIgnoreCase("null") && uom != null){
					 boolean isExistUom =false;
					 htitem.put("STKUOM",uom);
					
							UomUtil uomUtil = new UomUtil();
							isExistUom = uomUtil.isExistsUom(uom, Plant);
							if(!isExistUom){
								throw new Exception("UOM '" + uom + "' is not created/in active at line "+linecnt+".");
							}
					
					 }
		   
		// quantity field validation
			boolean isCorrectQuantityFormat = false;
			if (!qty.equalsIgnoreCase("null") && qty != null){
				isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
				if(!isCorrectQuantityFormat){
					throw new Exception("Quantity value '" + qty + "' is not accepted format at Line "+linecnt+".");
				}
			}
			
			// Product delivery date field validation
			    boolean isCorrectproductdeliverydate = false;
			if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
				isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
				if(!isCorrectproductdeliverydate){
					throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
				}
			}
			
			// unit price field validation
			boolean isCorrectUnitPriceFormat = false;
			if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
				isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
				if(!isCorrectUnitPriceFormat){
					throw new Exception("Unit cost value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
				}
			}
						
			
			if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
					if(unit_price.contains(".")){
					int costdecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
					if(costdecnum>numberOfDecimal)
					{
						throw new Exception("Invalid more than "+numberOfDecimal+" digits after decimal in Unit Price at Line "+linecnt+".");
					}
				}
				
			}
			
			// Product Discount field data validation			
			boolean isCorrectproductdiscount = false;
			if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
				isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
				if(!isCorrectproductdiscount){
					throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
				}
			}else {
				productdiscount = "0";
			}
			
			// Product Discount % field validation			
			if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
		    {
				productdiscountPer=currencyID;
		    }
		  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
			{
			  productdiscountPer="%";
			}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
			{
				productdiscountPer=currencyID;
			}
			else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
     		  {
     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding purchase at Line "+linecnt+".");  
     		  }
		   
			
			boolean isCorrectOrderDiscount = false;
			if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
				isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
				if(!isCorrectOrderDiscount){
					throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
				}
			}else {
				orderdiscount = "0";
			}
			
			// Order Discount Are field validation
			if (("").equals(orderdiscountAre)|| ("null").equals(orderdiscountAre)){
				orderdiscountAre = "Tax Inclusive";
		    }
	 		  boolean isValidorderdiscountAre    = false;
				if(EMPTYPE_LEN > orderdiscountAre.length()){
					isValidorderdiscountAre = true;
	          }
	          if(!isValidorderdiscountAre){
	                  throw new Exception("Order Discount Are value '" + orderdiscountAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
	          }
	          
	          if(orderdiscountAre.length()>0){
	        	  if(orderdiscountAre.equalsIgnoreCase("Tax Inclusive"))
	        	  {}
	        	  else if(orderdiscountAre.equalsIgnoreCase("Tax Exclusive"))
	        	  {}
	        	  else
	        		  throw new Exception("Order Discount Are can't be '"+orderdiscountAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding purchase at Line "+linecnt+".");
	          }
	          
	       // Order Discount % field validation			
				if (orderdiscountPer.equals("null") || orderdiscountPer == null || orderdiscountPer.equals("") )
			    {
					orderdiscountPer=currencyID;
			    }
			  else if (orderdiscountPer.equalsIgnoreCase("Y")|| orderdiscountPer.equalsIgnoreCase("y"))
				{
				  orderdiscountPer="%";
				}else if (orderdiscountPer.equalsIgnoreCase("N")|| orderdiscountPer.equalsIgnoreCase("n"))
				{
					orderdiscountPer=currencyID;
				}
				else if(!orderdiscountPer.equalsIgnoreCase("N")|| !orderdiscountPer.equalsIgnoreCase("n")||!orderdiscountPer.equalsIgnoreCase("Y")|| !orderdiscountPer.equalsIgnoreCase("y"))
	     		  {
	     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountPer + "', before adding purchase at Line "+linecnt+".");  
	     		  }
			   
				
				   
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}else {
						shippingcost = "0";
					}
					
					// Shipping Cost Are field validation
					if (("").equals(shippingcostAre)|| ("null").equals(shippingcostAre)){
						shippingcostAre = "Tax Inclusive";
				    }
			 		  boolean isValidShippingCostAre    = false;
						if(EMPTYPE_LEN > shippingcostAre.length()){
							isValidShippingCostAre = true;
			          }
			          if(!isValidShippingCostAre){
			                  throw new Exception("Shipping Cost Are value '" + shippingcostAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if(shippingcostAre.length()>0){
			        	  if(shippingcostAre.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(shippingcostAre.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Shipping Cost Are can't be '"+shippingcostAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding purchase at Line "+linecnt+".");
			          }
					
					boolean isCorrectadjustment = false;
					if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
						isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
						if(!isCorrectadjustment){
							throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
						}
					}else {
						adjustment = "0";
					}
					
			
					// Remarks field data validation
					boolean isValidRemarksData = false;
					if(PO_REMARK_LEN >= remarks.length()){
						isValidRemarksData = true;
					}
					if(!isValidRemarksData){
						throw new Exception("Remarks value '" + remarks + "' length has exceeded the maximum value of "+PO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					boolean isValidRemarks2Data = false;
					if(PO_REMARK_LEN2 >= remarks2.length()){
						isValidRemarks2Data = true;
					}
					if(!isValidRemarks2Data){
						throw new Exception("Remarks2 value '" + remarks2 + "' length has exceeded the maximum value of "+PO_REMARK_LEN2+" Chars at Line "+linecnt+".");
					}
					                             
				   
					// Manufacturer field data validation
					boolean isValidManuData = false;
					if(MANUFACTURER_LEN > manufacturer.length()){
						isValidManuData = true;
					}
					if(!isValidManuData){
						throw new Exception("Manufacturer value :  '" + manufacturer + "' length has exceeded the maximum value of "+MANUFACTURER_LEN+" Chars at Line "+linecnt+".");
					}
					                                      					
                                        
                                        // UOM field data validation
                                        /*boolean isValidUomData = false;
                                        if(UOM > uom.length()){
                                                isValidUomData = true;
                                        }
                                        if(!isValidUomData){
                                                throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
                                        }
				      if (!uom.equalsIgnoreCase("null") && uom != null){
                                        boolean isValidUomforproduct =false;
				        htitem.put("STKUOM",uom);
				        isValidUomforproduct = itemUtil.isExistsItemMst(htitem);
                                        if(!isValidUomforproduct){
                                                throw new Exception("UOM value :  '" + uom + "' is not valid for Product ID : "+item+"  at Line "+linecnt+".");
                                        }
                                      }*/
					 
					boolean isCorrectLocalExpenses = false;
					if (!localexpenses.trim().equalsIgnoreCase("null") && localexpenses != null){
						isCorrectLocalExpenses = impUtil.isCorrectUnitPriceFormat(localexpenses);
						if(!isCorrectLocalExpenses){
							throw new Exception("Local Expenses value '" + localexpenses + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					m.put("PLANT", Plant);
					m.put(IConstants.IN_CUST_CODE, supplier_code);
					
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					m.put("PROJECT", project);
					
					if (paymenttype.equalsIgnoreCase("null")
							|| paymenttype == null)
						paymenttype = "";
					m.put(IConstants.PAYMENTTYPE, paymenttype);
					m.put(IConstants.payment_terms, paymentterms);
					m.put(IConstants.TRANSPORTID, trans);
					m.put(IConstants.IN_PONO, pono);
				         
				        if (refNo.equalsIgnoreCase("null")|| refNo == null)
				            refNo = "";
				         m.put(IConstants.IN_REF_NO, refNo);

				         if (orderDate.equalsIgnoreCase("null")|| orderDate == null)
						    	//modified by Lakshmi
						    	orderDate = DateUtils.getDate();
							m.put(IConstants.IN_COLLECTION_DATE, orderDate);
							
							if (orderTime.equalsIgnoreCase("null")
									|| orderTime == null)
								//modified by Lakshmi
								orderTime = DateUtils.getTimeHHmm();
							m.put(IConstants.IN_COLLECTION_TIME, orderTime);
							
							if (deliverydate.equalsIgnoreCase("null") || deliverydate == null)
								deliverydate = "";
							m.put(IConstants.DEL_DATE, deliverydate);	
							
							if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
							{
							deliverybydate="1";
							}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
							{
							deliverybydate="0";
							}
						m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);
						
						if (ordertype.equalsIgnoreCase("null")|| ordertype == null)
							ordertype = "";
						m.put(IConstants.IN_ORDERTYPE, ordertype);
						
						if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
							incoterm = "";
						m.put(IConstants.INCOTERMS, incoterm);

			            m.put(IConstants.IN_CURRENCYID, currencyID);
			            
			            m.put("CURRENCYUSEQT", equivalentCurrency);
			            
			            m.put(IConstants.IN_INBOUND_GST, inbound_gst);
			            
			            if (employeename.equalsIgnoreCase("null")|| employeename == null)
	  						employeename = "";
	  					m.put(IConstants.POHDR_EMPNO, employeename);
	  				    if (purcahseloc.equalsIgnoreCase("null")|| purcahseloc == null)
	  						purcahseloc = "";
			            m.put(IConstants.PURCHASE_LOCATION, purcahseloc);
			            m.put("Tax", tax);
						m.put(IConstants.REVERSECHARGE, reversechar);
						m.put(IConstants.GOODSIMPORT, goodsipt);
						
			            
						if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
						{
							productRatesAre="1";
						}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
						{
							productRatesAre="0";
						}else {
							productRatesAre="0";
						}
						m.put("ISTAXINCLUSIVE", productRatesAre);
						
				        m.put(IConstants.IN_POLNNO, poLno);
				    
				        m.put(IConstants.IN_ITEM, item);
				        
				        m.put(IConstants.IN_ITEM_DES,StrUtils.fString(new ItemMstDAO().getItemDesc(Plant,item)));
				        
				        m.put("ACCOUNT_NAME", account);
				        
				        if (uom.equalsIgnoreCase("null")|| uom == null)
							uom = new ItemMstDAO().getItemPurchaseUOM(Plant,item);
							m.put(IConstants.IN_UNITMO, uom);
						
						if (qty.equalsIgnoreCase("null") || qty == null)
							qty = "0";
						m.put(IConstants.IN_QTYOR, qty);

						if (productdeliverydate.equalsIgnoreCase("null") || productdeliverydate == null)
							productdeliverydate = "";
						m.put("PRODUCTDELIVERYDATE", productdeliverydate);
	                    
						if (unit_price.equalsIgnoreCase("null")|| unit_price == null) {
							unit_price = "0";
						}
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
						}
						m.put(IConstants.IN_UNITCOST, unit_price);

						if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null) {
							productdiscount = "0";
						}
						if(!productdiscountPer.equalsIgnoreCase("%")) {
							if(!equivalentCurrency.equalsIgnoreCase("")) {
								productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
							}
						}
						m.put("PRODUCTDISCOUNT", productdiscount);

						m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
						
						if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null) {
							orderdiscount = "0";
						}
						if(!orderdiscountPer.equalsIgnoreCase("%")) {
							if(!equivalentCurrency.equalsIgnoreCase("")) {
								orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
							}
						}
						m.put(IConstants.ORDERDISCOUNT, orderdiscount);
						
						if (orderdiscountAre.equalsIgnoreCase("Tax Inclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Inclusive"))
						{
							orderdiscountAre="1";
						}else if (orderdiscountAre.equalsIgnoreCase("Tax Exclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Exclusive"))
						{
							orderdiscountAre="0";
						}else {
							orderdiscountAre="0";
						}
						m.put("ISDISCOUNTTAX", orderdiscountAre);
						
						m.put("ORDERDISCOUNTTYPE", orderdiscountPer);
						
						if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
							shippingcost = "0";
						
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
						}
						m.put(IConstants.SHIPPINGCOST, shippingcost);
						
						if (shippingcostAre.equalsIgnoreCase("Tax Inclusive")|| shippingcostAre.equalsIgnoreCase("Tax Inclusive"))
						{
							shippingcostAre="1";
						}else if (shippingcostAre.equalsIgnoreCase("Tax Exclusive")|| shippingcostAre.equalsIgnoreCase("Tax Exclusive"))
						{
							shippingcostAre="0";
						}else {
							shippingcostAre="0";
						}
						m.put("ISSHIPPINGTAX", shippingcostAre);
						
				        
					if (remarks.equalsIgnoreCase("null")
							|| remarks == null)
						remarks = "";
					m.put(IConstants.IN_REMARK1, remarks);
					if (remarks2.equalsIgnoreCase("null")
							|| remarks2 == null)
						remarks2 = "";
					m.put(IConstants.IN_REMARK3, remarks2);
					
					
					if (manufacturer.equalsIgnoreCase("null")
							|| manufacturer == null)
						manufacturer = "";
					m.put(IConstants.IN_MANUFACTURER, manufacturer);

                    if (localexpenses.equalsIgnoreCase("null")|| localexpenses == null)
						localexpenses = "0";
					m.put(IConstants.LOCALEXPENSES, localexpenses);
					m.put("TAXID", taxid);
					m.put("PROJECTID", projectid);
					m.put("SHIPPINGID", shippingid);
					if (adjustment.equalsIgnoreCase("null")|| adjustment == null)
						adjustment = "0";
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					m.put("ADJUSTMENT", adjustment);
					
				
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		
		return hdrList;

	}
		@SuppressWarnings("unchecked")
	public ArrayList downloadOutboundSheetData(String Plant, String filename,
			String sheetName,String baseCurrency,String NOOFORDER) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		CustUtil custUtil = new CustUtil();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		DOUtil doUtil = new DOUtil();
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
	    String ValidNumber="";
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(OutboundNumberOfColumn),"OB");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid = new DoHdrDAO().Salescount(Plant,FROM_DATE,TO_DATE);
			int convl = 0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				convl = Integer.valueOf(NOOFORDER);				
			}
			String validpo="";
 			int validcot=0;
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String dono = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
 					//Validate Order azees - 12/2020
 					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
 					{
 					int ddok =0;
 					if(validpo.equalsIgnoreCase(""))
 					{
 						validpo=dono;
 						ddok=1;
 						
 					}
 					else {
 					if(validpo.equalsIgnoreCase(dono))
 					{
 						validcot=validcot+1;
 						ddok=validcot-1;
 						if(validcot>0)
 							validcot=validcot-1;
 					}
 					else {
 						validpo=dono;
 						ddok=1;
 						
 					}
 					}
 					
 					novalid=novalid+ddok;
 					
 						if(novalid>convl)
 						{
 							ValidNumber=NOOFORDER;
 							break;
 						}
 					}
 					//End
					String customerCode = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					
				    // customer validation
					if (("").equals(customerCode)|| ("null").equals(customerCode)){
					    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}
					
					boolean isExistCustomer = false;
					isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
//					isExistCustomer = custUtil.isExistCustomerCode(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
					}
					
					ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
					Map mCustot=(Map)arrList.get(0);
					customerCode=(String) mCustot.get("CUSTNO");
					
   					//Shipping customer validation
					String shippingcustomer = "";
   					String shippingid = "";
   					if (("").equals(shippingcustomer)|| ("null").equals(shippingcustomer)){
							shippingcustomer=(String) mCustot.get("CNAME");
   					}else {
   						
   						boolean isValidShippingCustomer = false;
   						if(SHIPPING_CUSTOMER_LEN > shippingcustomer.length()){
   							isValidShippingCustomer = true;
   						}
   						if(!isValidShippingCustomer){
   							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
   						} 
   						
   						boolean isExistCust= false;
   						if(shippingcustomer.length()>0){
   						isExistCust = custUtil.isExistCustomer(shippingcustomer, Plant);
   						if(!isExistCust){
   							throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system at Line "+linecnt+" .");
   						}else {
//   							ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
//   							Map mCustot=(Map)arrList.get(0);
   							shippingid=(String) mCustot.get("CUSTNO");
   						}
   						}
   					}
   					
					String transportmode = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					TransportModeDAO transportmodedao = new TransportModeDAO();
                    String trans = "";
					if(transportmode.length()>0){
						trans = transportmodedao.getTransportModeByName(Plant,transportmode);
					}
					else {
						
						transportmode = "";
					}
					if(transportmode.equals("null")) {
						transportmode = "" ;
						trans = "";
					}
					String project = StrUtils.replaceStr2Char(((String) list.get(2)).trim()); //new
					String paymenttype = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String paymentterms = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					
					String refNo = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String order_date = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String time = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String order_type = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String deliverydate = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
				    String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String outbound_gst = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String employeename = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String incoterm = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String salesLocation = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					String currencyID = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(17)).trim()); //new
					String tax = StrUtils.replaceStr2Char(((String) list.get(18)).trim()); //new
					String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(19)).trim()); //new
					String item = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String doLno = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					String account = StrUtils.replaceStr2Char(((String) list.get(22)).trim()); //new
					String uom = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
					String productdeliverydate = StrUtils.replaceStr2Char(((String) list.get(25)).trim()); //new
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
					String productdiscount = StrUtils.replaceStr2Char(((String) list.get(27)).trim()); //new
					String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(28)).trim()); //new
					String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
					String orderdiscounttaxinc= StrUtils.replaceStr2Char(((String) list.get(30)).trim()); //new
					String orderdiscountpre = StrUtils.replaceStr2Char(((String) list.get(31)).trim()); //new
					/* Author: Azees  Create date: July 5,2021  Description: Hide Discount */
					/*String discount = StrUtils.replaceStr2Char(((String) list.get(31)).trim()); //new
					String discounttaxinc = StrUtils.replaceStr2Char(((String) list.get(32)).trim()); //new
					String discountpre = StrUtils.replaceStr2Char(((String) list.get(33)).trim()); //new */
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
					String shippingcosttaxinc = StrUtils.replaceStr2Char(((String) list.get(33)).trim()); //new
					String adjustment = StrUtils.replaceStr2Char(((String) list.get(34)).trim()); //new
					String remarks1 = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
					String remarks2 = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
					
					String discount="0";
					String discounttaxinc="";
					String discountpre="";
					
					String projectid="0";
					String taxid="0";
					
					String Minprice = "";
					
					String COUNTRYCODE = "";
	           	  	String plantstate = "";
	           	  	String plantstatecode = "";
	                PlantMstUtil plantmstutil = new PlantMstUtil();
	                List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
	                for (int i = 0; i < viewlistQry.size(); i++) {
	           	     	Map map = (Map) viewlistQry.get(i);
	           	     	COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	           	     	plantstate = StrUtils.fString((String)map.get("STATE"));
	                }
	                
	                MasterDAO masterDAO = new MasterDAO();
	                boolean issuloc=false;		 
	           	 	ArrayList saloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
	           	 	if (saloctions.size() > 0) {
	           	 		issuloc = true;
	           	 	}
					

				    if(StrUtils.isCharAllowed(dono) || StrUtils.isCharAllowed(doLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(productRatesAre) || StrUtils.isCharAllowed(account)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(dono)|| ("null").equals(dono)){
				        throw new Exception("DONO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(doLno)|| ("null").equals(doLno)){
				        throw new Exception("DOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
				        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				   
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if(issuloc) {
					    if (("").equals(salesLocation)|| ("null").equals(salesLocation)){
					        throw new Exception("Sales Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
				    }
				    if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    
				    /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
				        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				    
				    
//					if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
//						shippingcustomer = "";
//					}
//					//Shipping customer validation
//					isExistCustomer = false;
//					if(shippingcustomer.length()>0){
//						isExistCustomer = custUtil.isExistCustomer(shippingcustomer, Plant);
//					if(!isExistCustomer){
//						throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system.");
//					}else {
//						ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
//						Map mCustot=(Map)arrList.get(0);
//						shippingid=(String) mCustot.get("CUSTNO");
//						shippingcustomer = (String) mCustot.get("CNAME");
//					}
//					}
					

                    if (trans == null){
							throw new Exception("Create Transport Mode : '" + transportmode + "' first, before adding Transport at Line "+linecnt+".");
					}
					
					//Project validation					
					boolean isValidproject = false;
					if(DET_DESC_LEN > project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistCustomer = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistCustomer = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistCustomer){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
//					// payment type data validation
//					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
//						paymenttype = "";
//					}else {
//						boolean isValidpaymenttype = false;
//						if(OREDER_TYPE_LEN > paymenttype.length()){
//							isValidpaymenttype = true;
//						}
//						if(!isValidpaymenttype){
//							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
//						}
//						/*MasterDAO masterDAO = new MasterDAO();*/
//						isValidpaymenttype=masterDAO.getPaymentTypeisExist(Plant,paymenttype);
//						if(!isValidpaymenttype){
//			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding sales at Line "+linecnt+".");
//			     		}
//					}
					
					 ArrayList arrCustCUS = custUtil.getCustomerDetails(customerCode,Plant);
					 String paytype = (String) arrCustCUS.get(18);
					  if(paymentterms.equalsIgnoreCase("null")|| paymentterms == null || paymentterms.equalsIgnoreCase("")) {
						  paymentterms= (String) arrCustCUS.get(59);
					  }
					  if(trans.equalsIgnoreCase("null")|| trans == null || trans.equalsIgnoreCase("")) {
						  trans= (String) arrCustCUS.get(57);
					  }
					// payment type data validation
					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
						paymenttype= paytype;
					}else if(paytype.equalsIgnoreCase("null")|| paytype == null || paytype.equalsIgnoreCase("")){
						paymenttype ="";
					}else {
					
						boolean isValidpaymenttype = false;
						if(OREDER_TYPE_LEN >= paymenttype.length()){
							isValidpaymenttype = true;
						}
						if(!isValidpaymenttype){
							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
						}
						
						/*MasterDAO masterDAO = new MasterDAO();*/
						isValidpaymenttype=masterDAO.getPaymentModeisExist(Plant,paymenttype);
						if(!isValidpaymenttype){
			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding purchase at Line "+linecnt+".");
			     		}
					}
					
					
					
					// DONO field data validation
					boolean isValidDonoData = false;
					if(DONO_LEN >= dono.length()){
						isValidDonoData = true;
					}
					if(!isValidDonoData){
						throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
					}
					
					// validation against database entries
					// DONO number validation with status
					boolean isNewStatusDoNo = false;
					isNewStatusDoNo = doUtil.isNewStatusDONO(dono,Plant);
                    if(isNewStatusDoNo){
                            throw new Exception("DONO '" + dono + "' is closed or already moved to open satus. you can't edit on system at Line "+linecnt+" ."); 
                    }
					
					// RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(DO_REF_LEN >= refNo.length()){
				        isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
					
				    // data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					boolean isCorrectDateFormat = false;
					if (!order_date.equalsIgnoreCase("null") && order_date != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!time.equalsIgnoreCase("null") && time != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// delivery date field validation
					boolean isCorrectDeliveryDateFormat = false;
					if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
						isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
						if(!isCorrectDeliveryDateFormat){
							throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					 if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
					    {
					    	deliverybydate="N";
					    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding sales at Line "+linecnt+".");  
			     		  }
					
					// order type validation
					if (!order_type.equalsIgnoreCase("null") && order_type != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
						}
					}
					
					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("VAT value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
						
					// Incoterm field validation 
					 boolean isValidINCOTERMS = false;
						if(INCOTERM_LEN > incoterm.length()){
							isValidINCOTERMS = true;
						}
						if(!isValidINCOTERMS){
							throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
						}
						
					
						// Sales Location field validation	
						boolean isValidsalesLocation = false;
							if(SALES_LOCATION > salesLocation.length()){
								isValidsalesLocation = true;
		                }
		                if(!isValidsalesLocation){
		                        throw new Exception("Sales Location value '" + salesLocation + "' length has exceeded the maximum value of "+SALES_LOCATION+" Chars at Line "+linecnt+".");
		                }
		                
		                if(issuloc) {
			               MasterUtil _MasterUtil=new  MasterUtil();
			     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesLocation,Plant,COUNTRYCODE);
			     		   if(ccList.size()>0)
			     		   {
			     			  for (int i = 0; i < ccList.size(); i++) {
			     			     Map map1 = (Map) ccList.get(i);
			     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
			     			 }
			     		   }
			     		   else
			     		   {
			     			  throw new Exception("Enter Valid Sales Location  : '" + salesLocation + "', before adding sales at Line "+linecnt+".");
			     		   }
		                }else {
		                	salesLocation = "";
		                }
					
	     		   // Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
				            
				            Hashtable ht = new Hashtable();
				            ht.put(IDBConstants.PLANT, Plant);
				            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
					
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                     currencyID = baseCurrency;
				    
				   // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }
					
				  // tax field validation
		     		  boolean isValidtax = false;
						if(REMARKS_LEN > tax.length()){
							isValidtax = true;
			          }
			          if(!isValidtax){
			                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("OUTBOUND", COUNTRYCODE, tax);
			          if(taxtypes.size() > 0) {
			        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
			        		
			        			taxid = String.valueOf(finCountryTaxType.getID());	        			
			        			String display="";
					         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
					         	{
					         		display = finCountryTaxType.getTAXTYPE();
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE();
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
					         		}
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
					         		}
					         	}
					         	else
					         	{
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+outbound_gst+"%]";
					         		}
					         	}
					         	
					         	tax=display;
			        			
			        		}
			          	}
			          else
			          {
			        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding sales at Line "+linecnt+".");
			          }
					
					
			       // Product Rates Are field validation
			          if (("").equals(productRatesAre)|| ("null").equals(productRatesAre) || productRatesAre == null){
			        	  productRatesAre = "Tax Exclusive";
					  }
			 		  boolean isValidProductRatesAre    = false;
						if(EMPTYPE_LEN > productRatesAre.length()){
							isValidProductRatesAre = true;
			          }
			          if(!isValidProductRatesAre){
			                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if(productRatesAre.length()>0){
			        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // is valid Line No
					    
					 int isThisANumber = 0;
					     try
					     {
					     isThisANumber = Integer.parseInt(doLno); 
					     }
					     catch (NumberFormatException nfe)
					     {
					         throw new Exception("DOLNNO value :  '" + doLno + "' for DONO "+dono+" is not a valid number  at Line "+linecnt+".");
					     }    boolean isValidLineNumber    = false;
			       			if(LNNO_LEN >= doLno.length()){
			       				isValidLineNumber = true;
			                 }
			                 if(!isValidLineNumber){
			                         throw new Exception("DOLNNO value '" + doLno + "' length has exceeded the maximum value of "+LNNO_LEN+" Chars at Line "+linecnt+".");
			                 }
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", item);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
					}

					// Account field data validation
					boolean isValidAccount = false;
					if (PO_REMARK_LEN > account.length()) {
						isValidAccount = true;
					}
					if (!isValidAccount) {
						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "
								+ PO_REMARK_LEN + " Chars at Line " + linecnt + ".");
					}
					
					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
						account = "Local sales - retail";
					}else {
						CoaDAO coadao = new CoaDAO();
						List<Map<String, String>> listQry = null;
						Map<String, List<Map<String, String>>> listGrouped = null;
						listQry = coadao.selectSubAccountTypeList(Plant, account);
						if (listQry.size() > 0) {
							String[] filterArray = { "8" };
							String[] detailArray = { "26", "25" };
							listQry = listQry.stream()
									.filter(y -> Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
									.collect(Collectors.toList());
							listGrouped = listQry.stream()
									.filter(x -> Arrays.stream(filterArray).anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
									.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
	
							if (listGrouped.size() > 0) {
	
							} else {
								throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
										+ linecnt + ".");
							}
						} else {
							throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
									+ linecnt + ".");
						}
					}

					// UOM field data validation
					boolean isValidUomData = false;
					if (UOM >= uom.length()) {
						isValidUomData = true;
					}
					if (!isValidUomData) {
						throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of " + UOM
								+ " Chars at Line " + linecnt + ".");
					}
					if (!uom.equalsIgnoreCase("null") && uom != null) {
						boolean isExistUom = false;
						htitem.put("STKUOM", uom);

						UomUtil uomUtil = new UomUtil();
						isExistUom = uomUtil.isExistsUom(uom, Plant);
						if (!isExistUom) {
							throw new Exception("UOM '" + uom + "' is not created/in active at line " + linecnt + ".");
						}
					}
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null) {
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if (!isCorrectQuantityFormat) {
							throw new Exception(
									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
						}
					}
					
					// Product delivery date field validation
				    boolean isCorrectproductdeliverydate = false;
				    if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
				    	isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
						if(!isCorrectproductdeliverydate){
							throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
				    }

				    // unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if(!isCorrectUnitPriceFormat){
							throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
						}
					}
					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
						if(unit_price.contains(".")){
							int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if(pricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
							}
						}
						
					}
					
					// Product Discount field data validation			
					boolean isCorrectproductdiscount = false;
					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
						if(!isCorrectproductdiscount){
							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
						}
					}else {
						productdiscount = "0";
					}
					
					// Product Discount % field validation			
					if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
				    {
						productdiscountPer=currencyID;
				    }
				  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
					{
					  productdiscountPer="%";
					}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
					{
						productdiscountPer=currencyID;
					}
					else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
		     		  {
		     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding sales at Line "+linecnt+".");  
		     		  }
					
					
					
					
					
					// Order Discount field validation
					boolean isCorrectOrderDiscount = false;
					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
						if(!isCorrectOrderDiscount){
							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
						}
					}else {
						orderdiscount = "0";
					}
					
					// Order Discount Are field validation
			 		  boolean isValidorderdiscountAre    = false;
						if(EMPTYPE_LEN > orderdiscounttaxinc.length()){
							isValidorderdiscountAre = true;
			          }
			          if(!isValidorderdiscountAre){
			                  throw new Exception("Order Discount Are value '" + orderdiscounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if (("").equals(orderdiscounttaxinc)|| ("null").equals(orderdiscounttaxinc) || orderdiscounttaxinc == null){
			        	  orderdiscounttaxinc = "Tax Inclusive";
					  }
			          
			          if(orderdiscounttaxinc.length()>0){
			        	  if(orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Order Discount Are can't be '"+orderdiscounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // Order Discount % field validation			
						if (orderdiscountpre.equals("null") || orderdiscountpre == null || orderdiscountpre.equals("") )
					    {
							orderdiscountpre=currencyID;
					    }
					  else if (orderdiscountpre.equalsIgnoreCase("Y")|| orderdiscountpre.equalsIgnoreCase("y"))
						{
						  orderdiscountpre="%";
						}else if (orderdiscountpre.equalsIgnoreCase("N")|| orderdiscountpre.equalsIgnoreCase("n"))
						{
							orderdiscountpre=currencyID;
						}
						else if(!orderdiscountpre.equalsIgnoreCase("N")|| !orderdiscountpre.equalsIgnoreCase("n")||!orderdiscountpre.equalsIgnoreCase("Y")|| !orderdiscountpre.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountpre + "', before adding sales at Line "+linecnt+".");  
			     		  }
						
						
						
						// Discount field validation
						boolean isCorrectDiscount = false;
						if (!discount.trim().equalsIgnoreCase("null") && discount != null){
							isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
							if(!isCorrectDiscount){
								throw new Exception("Discount value '" + discount + "' is not accepted format.");
							}
						}else {
							discount = "0";
						}
						
						// Discount Are field validation
				 		  boolean isValiddiscountAre    = false;
							if(EMPTYPE_LEN > discounttaxinc.length()){
								isValiddiscountAre = true;
				          }
				          if(!isValiddiscountAre){
				                  throw new Exception("Discount Are value '" + discounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          if (("").equals(discounttaxinc)|| ("null").equals(discounttaxinc) || discounttaxinc == null){
				        	  discounttaxinc = "Tax Inclusive";
						  }
				          if(discounttaxinc.length()>0){
				        	  if(discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Discount Are can't be '"+discounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
				          
				       // Discount % field validation			
							if (discountpre.equals("null") || discountpre == null || discountpre.equals("") )
						    {
								discountpre=currencyID;
						    }
						  else if (discountpre.equalsIgnoreCase("Y")|| discountpre.equalsIgnoreCase("y"))
							{
							  discountpre="%";
							}else if (discountpre.equalsIgnoreCase("N")|| discountpre.equalsIgnoreCase("n"))
							{
								discountpre=currencyID;
							}
							else if(!discountpre.equalsIgnoreCase("N")|| !discountpre.equalsIgnoreCase("n")||!discountpre.equalsIgnoreCase("Y")|| !discountpre.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Order Discount %  : '" + discountpre + "', before adding sales at Line "+linecnt+".");  
				     		  }
							
					
					
					//shipping cost validation					
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Shipping Cost Are field validation
			 		  boolean isValidShippingCostAre    = false;
						if(EMPTYPE_LEN > shippingcosttaxinc.length()){
							isValidShippingCostAre = true;
			          }
			          if(!isValidShippingCostAre){
			                  throw new Exception("Shipping Cost Are value '" + shippingcosttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          if (("").equals(shippingcosttaxinc)|| ("null").equals(shippingcosttaxinc) || shippingcosttaxinc == null){
			        	  shippingcosttaxinc = "Tax Inclusive";
					  }
			          if(shippingcosttaxinc.length()>0){
			        	  if(shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Shipping Cost Are can't be '"+shippingcosttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
													
			        //Adjustment field validation	
						boolean isCorrectadjustment = false;
						if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
							isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
							if(!isCorrectadjustment){
								throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
							}
						}else {
							adjustment = "0";
						}
					
						// Remarks1 field data validation
						boolean isValidRemarksData1 = false;
						if(DO_REMARK_LEN1 >= remarks1.length()){
							isValidRemarksData1 = true;
						}
						if(!isValidRemarksData1){
							throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
						}
						

						// Remarks1 field data validation
						boolean isValidRemarksData2 = false;
						if(DO_REMARK_LEN2 >= remarks2.length()){
							isValidRemarksData2 = true;
						}
						if(!isValidRemarksData2){
							throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
						}
				
     		   
					m.put("PLANT", Plant);
					m.put(IConstants.OUT_CUST_CODE, customerCode);
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
					m.put("SHIPPINGID", shippingid);
					m.put(IConstants.TRANSPORTID, trans);
					m.put(IConstants.payment_terms, paymentterms);
					m.put("PAYMENTTERMS", paymenttype);
					m.put("PROJECTID", projectid);
					m.put("TAX", tax);
					m.put("TAXID", taxid);
					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
					{
						productRatesAre="1";
					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
					{
						productRatesAre="0";
					}else {
						productRatesAre="0";
					}
					m.put("ISTAXINCLUSIVE", productRatesAre);
					m.put("ACCOUNT", account);
					m.put("PRODUCTDELIVERYDATE", productdeliverydate);
					if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null)
						productdiscount = "0";
					
					if(!productdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("PRODUCTDISCOUNT", productdiscount);

					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					
					m.put("ADJUSTMENT", adjustment);
					
					
					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
						orderdiscount = "0";
					if(!orderdiscountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.ORDERDISCOUNT, orderdiscount);
					
					if (orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						orderdiscounttaxinc="1";
					}else if (orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						orderdiscounttaxinc="0";
					}else {
						orderdiscounttaxinc="0";
					}
					m.put("ISORDERDISCOUNTTAX", orderdiscounttaxinc);
					
					m.put("ORDERDISCOUNTTYPE", orderdiscountpre);
					
					
					if (discount.equalsIgnoreCase("null")|| discount == null)
						discount = "0";
					if(!discountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.DISCOUNT, discount);
					
					if (discounttaxinc.equalsIgnoreCase("Tax Inclusive")|| discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						discounttaxinc="1";
					}else if (discounttaxinc.equalsIgnoreCase("Tax Exclusive")|| discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						discounttaxinc="0";
					}else {
						discounttaxinc="0";
					}
					m.put("ISDISCOUNTTAX", discounttaxinc);
					
					m.put("DISCOUNTTYPE", discountpre);
					
					if (shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						shippingcosttaxinc="1";
					}else if (shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						shippingcosttaxinc="0";
					}else {
						shippingcosttaxinc="0";
					}
					m.put("ISSHIPPINGTAX", shippingcosttaxinc);
					
					
					
					
					
					
					
					m.put(IConstants.OUT_DONO, dono);
				        m.put(IConstants.OUT_DOLNNO, doLno);
				    
				    if (refNo.equalsIgnoreCase("null")|| refNo == null)
				    refNo = "";
				    m.put(IConstants.OUT_REF_NO, refNo);
                                    
					if (order_type.equalsIgnoreCase("null")|| order_type == null)
					order_type = "";
					m.put(IConstants.OUT_ORDERTYPE, order_type);

				    if (order_date.equalsIgnoreCase("null")|| order_date == null)
				        order_date = DateUtils.getDate();
					m.put(IConstants.OUT_COLLECTION_DATE, order_date);
					
					if (time.equalsIgnoreCase("null")|| time == null)
				         time = DateUtils.getTimeHHmm();
					m.put(IConstants.OUT_COLLECTION_TIME, time);
					
					if (remarks1.equalsIgnoreCase("null")
							|| remarks1 == null)
						remarks1 = "";
					m.put(IConstants.OUT_REMARK1, remarks1);
					
					if (remarks2.equalsIgnoreCase("null")
							|| remarks2 == null)
						remarks2 = "";
					m.put(IConstants.OUT_REMARK3, remarks2);
					
					if (outbound_gst.equalsIgnoreCase("null")
							|| outbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "OUTBOUND");
						Map gstMap = (Map)gstDetails.get(0);
						outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
												
					m.put(IConstants.OUT_OUTBOUND_GST, outbound_gst);
					
					m.put(IConstants.OUT_ITEM, item);
					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.OUT_QTYOR, qty);

					if (uom.equalsIgnoreCase("null")|| uom == null)
				        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"SALESUOM");
					m.put(IConstants.OUT_UNITMO, uom);
					
					if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                      currencyID = baseCurrency;
	                      m.put(IConstants.IN_CURRENCYID, currencyID);

					if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
					unit_price = "0";
					String minSellingconvertedcost="";
					ArrayList Mprice = itemUtil.getMprice(Plant, item);
					Map gstMap = (Map)Mprice.get(0);
					Minprice  = (String)gstMap.get("MINSPRICE");	
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductimport(Plant,currencyID);
					if(unit_price != "0"  && !minSellingconvertedcost.equals("") && !minSellingconvertedcost.equals(null) &&  Double.valueOf(unit_price) < Double.valueOf(Minprice)*Double.valueOf(minSellingconvertedcost))
					{
						throw new Exception("Price should not be less than minimum selling price");
					}
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
					}
					
					m.put(IConstants.OUT_UNITCOST, unit_price);
            
                      
                    if (employeename.equalsIgnoreCase("null")|| employeename == null)
  						employeename = "";
  					m.put(IConstants.DOHDR_EMPNO, employeename);
  					
  					/* if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = DateUtils.getDate();*/
				
					 if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = "";
 					m.put(IConstants.DELIVERYDATE, deliverydate);
 					
					if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
						shippingcost = "0";
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
					}
					m.put(IConstants.SHIPPINGCOST, shippingcost);
					
					if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
						incoterm = "";
					m.put(IConstants.INCOTERMS, incoterm);
					
					

					if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
						paymenttype = "";
					m.put(IConstants.PAYMENTTYPE, paymenttype);
					if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
						{
						deliverybydate="1";
						}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
						{
						deliverybydate="0";
						}
					m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);
					m.put(IConstants.SALES_LOCATION, salesLocation);

				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		
		return hdrList;

	}
		
		public ArrayList downloadOutboundSheetDataLoan(String Plant, String filename,
				String sheetName,String baseCurrency) throws Exception {
			System.out.println("********  Import Starts *********");

			// to get file name
			ArrayList hdrList = new ArrayList();
			File f = new File(filename);
			filename = f.getName();
			f = null;
			//DOUtil doUtil = new DOUtil();
			LoanUtil loUtil = new LoanUtil();
			
			System.out.println("After extraction filename : " + filename);
			
			filename = DbBean.CountSheetUploadPath + filename;

			System.out.println("Import File :" + filename);
			System.out.println("Import sheet :" + sheetName);
			
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));

			try {
				POIReadExcel poi = new POIReadExcel(filename, sheetName);

				/*String[] str = poi.readExcelFile(Integer
						.parseInt(OutboundNumberOfColumn),"OB");*/
				String[] str = poi.readExcelFile(Integer
						.parseInt(LoanNumberOfColumn),"LO");
				System.out
						.println("Total number of record found in Excel sheet  :  "
								+ str.length);

				for (int j = 1; j < str.length; j++) {

					String s1 = str[j];
					String s2 = "";

					StringTokenizer parser = new StringTokenizer(s1, "|||");

					System.out.println("Record " + j + "\t: " + s1);

					List list = new LinkedList(); // Doubly-linked list

					list = new ArrayList();

					while (parser.hasMoreTokens()) {
						list.add(parser.nextToken());

					}

					int linecnt = j;
				        linecnt= linecnt+1;
					Map m = new HashMap();

					System.out.println("Total number of record per line  :  "
							+ list.size());

					if(list.size() > 0) {
						
						String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					    String doLno = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					    String fromloc = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					    //String toloc = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					    String refNo = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
						String order_type = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
						String order_date = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
						String time = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
						String remarks1 = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
						String remarks2 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
						String outbound_gst = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
						String customerCode = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
						String paymenttype = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
						String item = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
						//String customer = ((String) list.get(9)).trim();
						String qty = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
						String uom = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
						String unit_price = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					    String currencyID = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					    String employeename = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
					    String deliverydate = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					    String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
					    String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
						String shippingcost = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					    //String incoterm = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
						String shippingcustomer = StrUtils.replaceStr2Char(((String) list.get(22)).trim());

					    if(StrUtils.isCharAllowed(dono) || StrUtils.isCharAllowed(doLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate)){
							throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
						}
					    if (("").equals(dono)|| ("null").equals(dono)){
					        throw new Exception("DONO value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(doLno)|| ("null").equals(doLno)){
					        throw new Exception("DOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(fromloc)|| ("null").equals(fromloc)){
					        throw new Exception("From Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
					        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
					    }*/
					    if (("").equals(customerCode)|| ("null").equals(customerCode)){
					        throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(item)|| ("null").equals(item)){
					        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
					    }
	                          
					
						// DONO field data validation
						boolean isValidDonoData = false;
						if(DONO_LEN >= dono.length()){
							isValidDonoData = true;
						}
						if(!isValidDonoData){
							throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
						}
	                                        
					    // is valid Line No
					    
					     int isThisANumber = 0;
					     try
					     {
					     isThisANumber = Integer.parseInt(doLno); 
					     }
					     catch (NumberFormatException nfe)
					     {
					         throw new Exception("DOLNNO value :  '" + doLno + "' for DONO "+dono+" is not a valid number  at Line "+linecnt+".");
					     }

					   
					    
					    // RefNo field data validation
					    boolean isValidRefNoData = false;
					    if(DO_REF_LEN >= refNo.length()){
					        isValidRefNoData = true;
					    }
					    if(!isValidRefNoData){
					        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
					    }
					    
						
						// Remarks1 field data validation
						boolean isValidRemarksData1 = false;
						if(DO_REMARK_LEN1 > remarks1.length()){
							isValidRemarksData1 = true;
						}
						if(!isValidRemarksData1){
							throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
						}
						

						// Remarks1 field data validation
						boolean isValidRemarksData2 = false;
						if(DO_REMARK_LEN2 > remarks2.length()){
							isValidRemarksData2 = true;
						}
						if(!isValidRemarksData2){
							throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
						}
						
						
					
						
						// data entry validation for date, time, quantity and unit price fields
						ImportUtils impUtil = new ImportUtils();
						
						// date field validation
						boolean isCorrectDateFormat = false;
						if (!order_date.equalsIgnoreCase("null") && order_date != null){
							isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
							if(!isCorrectDateFormat){
								throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						// time field validation
						boolean isCorrectTimeFormat = false;
						if (!time.equalsIgnoreCase("null") && time != null){
							isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
							if(!isCorrectTimeFormat){
								throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
							}
						}
						

						// gst field validation
						boolean isCorrectGstFormat = false;
						if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
							isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
							if(!isCorrectGstFormat){
								throw new Exception("VAT value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
							}
						}

						
						// quantity field validation
						boolean isCorrectQuantityFormat = false;
						if (!qty.equalsIgnoreCase("null") && qty != null){
							isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
							if(!isCorrectQuantityFormat){
								throw new Exception("Quantity value '" + qty + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						// unit price field validation
						boolean isCorrectUnitPriceFormat = false;
						if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
							isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
							if(!isCorrectUnitPriceFormat){
								throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
							}
						}
						if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
							if(unit_price.contains(".")){
								int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
								if(pricedecnum>numberOfDecimal)
								{
									throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
								}
							}
							
						}
						// validation against database entries
						// DONO number validation with status
						boolean isNewStatusDoNo = false;
						//isNewStatusDoNo = doUtil.isNewStatusDONO(dono,Plant);
						isNewStatusDoNo = loUtil.isNewStatusDONO(dono,Plant);
	                    if(isNewStatusDoNo){
	                            throw new Exception("LONO '" + dono + "' is closed or already moved to open satus. you can't edit on system at Line "+linecnt+" ."); 
	                    }
						 
						 // item validation
						boolean isExistItem = false;
						 ItemUtil itemUtil = new ItemUtil();
						 Hashtable htitem= new Hashtable();
						 htitem.put("PLANT",Plant);
						 htitem.put("ITEM",item);
						 htitem.put("ISACTIVE","Y");
						 isExistItem = itemUtil.isExistsItemMst(htitem);
						 if(!isExistItem){
						          throw new Exception("Product ID '" + item + "' is not created/in active at Line "+linecnt+".");
						 }
						 
						 // UOM field data validation
					/*	 boolean isValidUomData = false;
						 if(UOM > uom.length()){
						         isValidUomData = true;
						 }
						 if(!isValidUomData){
						         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
						 }
						 if (!uom.equalsIgnoreCase("null") && uom != null){
						 boolean isValidUomforproduct =false;
						 htitem.put("STKUOM",uom);
						 isValidUomforproduct = itemUtil.isExistsItemMst(htitem);
						 if(!isValidUomforproduct){
						         throw new Exception("UOM value :  '" + uom + "' is not valid for Product ID : "+item+"  at Line "+linecnt+".");
						 }
						 }*/
						 
						 boolean isValidUomData = false;
						 if(UOM >= uom.length()){
						         isValidUomData = true;
						 }
						 if(!isValidUomData){
						         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
						 }
						 if (!uom.equalsIgnoreCase("null") && uom != null){
						 boolean isExistUom =false;
						 htitem.put("STKUOM",uom);
						
								UomUtil uomUtil = new UomUtil();
								isExistUom = uomUtil.isExistsUom(uom, Plant);
								if(!isExistUom){
									throw new Exception("UOM '" + uom + "' is not created/in active at line "+linecnt+".");
								}
						
						 }
					
						
						// customer validation
						boolean isExistCustomerLoan = false;
						CustUtil custUtil = new CustUtil();
						isExistCustomerLoan = custUtil.isExistCustomerLoan(customerCode, Plant);
						if(!isExistCustomerLoan){
							throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
						}
						
						// order type validation
						if (!order_type.equalsIgnoreCase("null") && order_type != null){
							boolean isExistOrderType = false;
							OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
							isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
							if(!isExistOrderType){
								throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
							}
						}
	                                        
					    // Currency ID validation
					    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
					            boolean isExistCurID = false;
					            CurrencyUtil currUtil = new CurrencyUtil();
					            
					            Hashtable ht = new Hashtable();
					            ht.put(IDBConstants.PLANT, Plant);
					            ht.put(IDBConstants.CURRENCYID, currencyID );
					            isExistCurID = currUtil.isExistCurrency(ht,"");
					            if(!isExistCurID){
					                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
					            }
					    }
					    
					    if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
					    
					    if (!fromloc.equalsIgnoreCase("null") && fromloc != null){
							boolean isExistsLoc = false;
							Hashtable htLoc = new Hashtable();
				    		htLoc.put(IDBConstants.PLANT,Plant);
				    		htLoc.put("LOC",fromloc);
				    		LocUtil LocUtil = new LocUtil();
				    		if(!LocUtil.isExistsLoc(htLoc))
				    	    {
				    			throw new Exception("Location'" +  fromloc + "' doesn't  Exists. Create Loaction");
				    	    }
							}
					    
					    /*if (!toloc.equalsIgnoreCase("null") && toloc != null){
							boolean isExistsLoc = false;
							Hashtable htLoc = new Hashtable();
				    		htLoc.put(IDBConstants.PLANT,Plant);
				    		htLoc.put("LOC",fromloc);
				    		LocUtil LocUtil = new LocUtil();
				    		if(!LocUtil.isExistsLoc(htLoc))
				    	    {
				    			throw new Exception("Location'" +  toloc + "' doesn't  Exists. Create Loaction");
				    	    }
							}*/
						
					    if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
					    {
					    	deliverybydate="N";
					    }
						// delivery date field validation
						boolean isCorrectDeliveryDateFormat = false;
						if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
							isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
							if(!isCorrectDeliveryDateFormat){
								throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
							}
						}
						
						boolean isCorrectOrderDiscount = false;
						if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
							isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
							if(!isCorrectOrderDiscount){
								throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
							}
						}
						
											
						boolean isCorrectShippingCost = false;
						if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
							isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
							if(!isCorrectShippingCost){
								throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
							}
						}
														
						/*boolean isValidINCOTERMS = false;
						if(INCOTERM_LEN > incoterm.length()){
							isValidINCOTERMS = true;
						}*/
						/*if(!isValidINCOTERMS){
							throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
						}*/
						
						boolean isValidShippingCustomer = false;
						if(SHIPPING_CUSTOMER_LEN > shippingcustomer.length()){
							isValidShippingCustomer = true;
						}
						if(!isValidShippingCustomer){
							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
						}

						
						m.put("PLANT", Plant);
						m.put(IConstants.LOANHDR_ORDNO, dono);
					        m.put(IConstants.LOANDET_ORDLNNO, doLno);
					        
					        if (fromloc.equalsIgnoreCase("null")|| fromloc == null)
							    fromloc = "";
							    m.put(IConstants.LOC, fromloc);
							    
							    /*if (toloc.equalsIgnoreCase("null")|| toloc == null)
								    toloc = "";*/
								    m.put(IConstants.LOC1, customerCode);
					    
					    if (refNo.equalsIgnoreCase("null")|| refNo == null)
					    refNo = "";
					    m.put(IConstants.LOANHDR_JOB_NUM, refNo);
	                                    
						if (order_type.equalsIgnoreCase("null")|| order_type == null)
						order_type = "";
						m.put(IConstants.ORDERTYPE, order_type);

					    if (order_date.equalsIgnoreCase("null")|| order_date == null)
					        order_date = DateUtils.getDate();
						m.put(IConstants.LOANHDR_COL_DATE, order_date);
						
						if (time.equalsIgnoreCase("null")|| time == null)
					         time = DateUtils.getTimeHHmm();
						m.put(IConstants.LOANHDR_COL_TIME, time);
						
						if (remarks1.equalsIgnoreCase("null")
								|| remarks1 == null)
							remarks1 = "";
						m.put(IConstants.LOANHDR_REMARK1, remarks1);
						
						if (remarks2.equalsIgnoreCase("null")
								|| remarks2 == null)
							remarks2 = "";
						m.put(IConstants.LOANHDR_REMARK2, remarks2);
						
						if (outbound_gst.equalsIgnoreCase("null")
								|| outbound_gst == null){
							//inbound_gst = "0";
							GstTypeUtil gstTypeUtil = new GstTypeUtil();
							ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "RENTAL");
							Map gstMap = (Map)gstDetails.get(0);
							outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
							
						}
						m.put(IConstants.LOANHDR_GST, outbound_gst);
						m.put(IConstants.LOANHDR_CUST_CODE, customerCode);
						m.put(IConstants.LOANDET_ITEM, item);
						if (qty.equalsIgnoreCase("null") || qty == null)
							qty = "0";
						m.put(IConstants.LOANDET_QTYOR, qty);

						if (uom.equalsIgnoreCase("null")|| uom == null)
					        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"RENTALUOM");
						m.put(IConstants.UNITMO, uom);

						if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
						unit_price = "0";
						m.put(IConstants.LOANDET_RENTALPRICE, unit_price);
	                                        
	                    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                      currencyID = baseCurrency;
	                      m.put(IConstants.CURRENCYID, currencyID);
	                      
	                    if (employeename.equalsIgnoreCase("null")|| employeename == null)
	  						employeename = "";
	  					m.put(IConstants.LOANHDR_EMPNO, employeename);
	  					
	  					/* if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
	  						deliverydate = DateUtils.getDate();*/
					
						 if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
	  						deliverydate = "";
	 					m.put(IConstants.DELIVERYDATE, deliverydate);
	 					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
							orderdiscount = "0";
						m.put(IConstants.ORDERDISCOUNT, orderdiscount);
						if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
							shippingcost = "0";
						m.put(IConstants.SHIPPINGCOST, shippingcost);
						
						/*if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
							incoterm = "";
						m.put(IConstants.INCOTERMS, incoterm);*/
						
						if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
							shippingcustomer = "";
						m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);

						if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
							paymenttype = "";
						m.put(IConstants.PAYMENTTYPE, paymenttype);
						if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
	 					{
	 						deliverybydate = "1";
	 					}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
	 					{
	 						deliverybydate = "0";
	 					}	
	 					m.put(IConstants.LOANHDR_DELIVERYDATEFORMAT, deliverybydate);
	 					m.put(IConstants.LOANHDR_EXPIRYDATEFORMAT, deliverybydate);

					}

					hdrList.add(m);

					System.out.println("hdrList :: " + hdrList.size());
				}

			} catch (Exception ex) {
				// ex.printStackTrace();
				throw ex;
			} finally {

			}
			
			//Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
			
			return hdrList;

		}
		
		@SuppressWarnings("unchecked")
		 public ArrayList downloadTransferSheetData(String Plant, String filename,
                 String sheetName,String baseCurrency,String NOOFORDER) throws Exception {
         System.out.println("********  Import Starts *********");

         // to get file name
         ArrayList hdrList = new ArrayList();
         File f = new File(filename);
         filename = f.getName();
         f = null;
         TOUtil toUtil = new TOUtil();
         //
         System.out.println("After extraction filename : " + filename);
         //
         filename = DbBean.CountSheetUploadPath + filename;
         String ValidNumber="";
         System.out.println("Import File :" + filename);
         System.out.println("Import sheet :" + sheetName);
         
     	PlantMstDAO plantMstDAO = new PlantMstDAO();
 	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));

         try {
                 POIReadExcel poi = new POIReadExcel(filename, sheetName);

                 String[] str = poi.readExcelFile(Integer
                                 .parseInt(TransferNumberOfColumn),"TO");
                 // String[] str =null;

                 System.out
                                 .println("Total number of record found in Excel sheet  :  "
                                                 + str.length);
               //Validate Order azees - 12/2020
     			
     			DateUtils _dateUtils = new DateUtils();
     			String FROM_DATE = _dateUtils.getDate();
     			if (FROM_DATE.length() > 5)
     				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
     			
     			String TO_DATE = _dateUtils.getLastDayOfMonth();
     			if (TO_DATE.length() > 5)
     				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
     			
     			int novalid =new ToHdrDAO().Salescount(Plant,FROM_DATE,TO_DATE);
     			int convl =0;
     			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
     			  convl = Integer.valueOf(NOOFORDER);
     			String validpo="";
     			int validcot=0;
                 for (int j = 1; j < str.length; j++) {

                         String s1 = str[j];
                         String s2 = "";

                         // SINO++;

                         StringTokenizer parser = new StringTokenizer(s1, "|||");

                         System.out.println("Record " + j + "\t: " + s1);

                         List list = new LinkedList(); // Doubly-linked list

                         list = new ArrayList();

                         while (parser.hasMoreTokens()) {
                                 list.add(parser.nextToken());

                         }

                         int linecnt = j;
                     linecnt = linecnt+1;
                         Map m = new HashMap();

                         System.out.println("Total number of record per line  :  "
                                         + list.size());

                        
                         if(list.size() > 0) {
                        	 String tono = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
         					//Validate Order azees - 12/2020
         					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
         					{
         					int ddok =0;
         					if(validpo.equalsIgnoreCase(""))
         					{
         						validpo=tono;
         						ddok=1;
         						
         					}
         					else {
         					if(validpo.equalsIgnoreCase(tono))
         					{
         						validcot=validcot+1;
         						ddok=validcot-1;
         						if(validcot>0)
         							validcot=validcot-1;
         					}
         					else {
         						validpo=tono;
         						ddok=1;
         						
         					}
         					}
         					
         					novalid=novalid+ddok;
         					
         						if(novalid>convl)
         						{
         							ValidNumber=NOOFORDER;
         							break;
         						}
         					}
         					//End
						
						String customerCode = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
						//Shipping customer validation
	                	CustUtil custUtil = new CustUtil();
						String shippingcustomer = "";
	   					String shippingid = "";
	   					if (("").equals(shippingcustomer)|| ("null").equals(shippingcustomer)){
	   						ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
								Map mCustot=(Map)arrList.get(0);
								shippingcustomer=(String) mCustot.get("CNAME");
	   					}else {
	   						
	   						boolean isValidShippingCustomer = false;
	   						if(SHIPPING_CUSTOMER_LEN > shippingcustomer.length()){
	   							isValidShippingCustomer = true;
	   						}
	   						if(!isValidShippingCustomer){
	   							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
	   						} 
	   						
	   						boolean isExistCust= false;
	   						if(shippingcustomer.length()>0){
	   						isExistCust = custUtil.isExistCustomer(shippingcustomer, Plant);
	   						if(!isExistCust){
	   							throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system at Line "+linecnt+" .");
	   						}else {
	   							ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
	   							Map mCustot=(Map)arrList.get(0);
	   							shippingid=(String) mCustot.get("CUSTNO");
	   						}
	   						}
	   					}
						String project = StrUtils.replaceStr2Char(((String) list.get(1)).trim()); //new
						String paymenttype = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
						//String tono = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
						String refNo = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
						String order_date = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
						String time = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					    String from_loc = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                        String to_loc = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
                        String order_type = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
						String deliverydate = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					    String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
						String consignment_gst = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
						String employeename = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
						String incoterm = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
						String salesLocation = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
						String currencyID = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
						String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(17)).trim()); //new
						String tax = StrUtils.replaceStr2Char(((String) list.get(18)).trim()); //new
						String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(19)).trim()); //new
						String item = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
						String toLno = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
						String account = StrUtils.replaceStr2Char(((String) list.get(22)).trim()); //new
						String uom = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
						String qty = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
						String productdeliverydate = StrUtils.replaceStr2Char(((String) list.get(25)).trim()); //new
						String unit_price = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
						String productdiscount = StrUtils.replaceStr2Char(((String) list.get(27)).trim()); //new
						String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(28)).trim()); //new
						String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
						String orderdiscounttaxinc= StrUtils.replaceStr2Char(((String) list.get(30)).trim()); //new
						String orderdiscountpre = StrUtils.replaceStr2Char(((String) list.get(31)).trim()); //new
//						String discount = StrUtils.replaceStr2Char(((String) list.get(31)).trim()); //new
//						String discounttaxinc = StrUtils.replaceStr2Char(((String) list.get(32)).trim()); //new
//						String discountpre = StrUtils.replaceStr2Char(((String) list.get(33)).trim()); //new
						String shippingcost = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
						String shippingcosttaxinc = StrUtils.replaceStr2Char(((String) list.get(33)).trim()); //new
						String adjustment = StrUtils.replaceStr2Char(((String) list.get(34)).trim()); //new
						String remarks1 = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
						String remarks2 = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
						String projectid="0";
						String taxid="0";
						
						String Minprice = "";
						
						String COUNTRYCODE = "";
		           	  	String plantstate = "";
		           	  	String plantstatecode = "";
		                PlantMstUtil plantmstutil = new PlantMstUtil();
		                List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
		                for (int i = 0; i < viewlistQry.size(); i++) {
		           	     	Map map = (Map) viewlistQry.get(i);
		           	     	COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
		           	     	plantstate = StrUtils.fString((String)map.get("STATE"));
		                }
		                
		                MasterDAO masterDAO = new MasterDAO();
		                boolean issuloc=false;		 
		           	 	ArrayList saloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
		           	 	if (saloctions.size() > 0) {
		           	 		issuloc = true;
		           	 	}
						

					    if(StrUtils.isCharAllowed(tono) || StrUtils.isCharAllowed(toLno) || StrUtils.isCharAllowed(from_loc) || StrUtils.isCharAllowed(to_loc) ||  StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(consignment_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(productRatesAre) || StrUtils.isCharAllowed(account)){
							throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
						}
					 // customer validation
						if (("").equals(customerCode)|| ("null").equals(customerCode)){
						    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
						}
					    if (("").equals(tono)|| ("null").equals(tono)){
					        throw new Exception("TONO value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(from_loc)|| ("null").equals(from_loc)){
                            throw new Exception("From Loc value is mandatory and  missing at Line "+linecnt+" .");  
                        }
                        if (("").equals(to_loc)|| ("null").equals(to_loc)){
                            throw new Exception("To Loc value is mandatory and  missing at Line "+linecnt+" .");  
                        }
					    if (("").equals(tax)|| ("null").equals(tax)){
					        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(item)|| ("null").equals(item)){
					        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    if (("").equals(toLno)|| ("null").equals(toLno)){
					        throw new Exception("TOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
					    }
					    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
					        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
					    }*/
					    
					    
					    if(issuloc) {
						    if (("").equals(salesLocation)|| ("null").equals(salesLocation)){
						        throw new Exception("Sales Location value is mandatory and  missing at Line "+linecnt+" .");  
						    }
					    }
					    
					    
					    /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
					        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
					    }*/
					    
					    

						boolean isExistCustomer = false;
						isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
						if(!isExistCustomer){
							throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
						}
					    
						if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
							shippingcustomer = "";
						}
						//Shipping customer validation
						isExistCustomer = false;
						if(shippingcustomer.length()>0){
							isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
						if(!isExistCustomer){
							throw new Exception("Shipping Customer '" + customerCode + "' is not created yet on system.");
						}else {
							ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
							Map mCustot=(Map)arrList.get(0);
							shippingid=(String) mCustot.get("CUSTNO");
							shippingcustomer = (String) mCustot.get("CNAME");
						}
						}
						
						//Project validation					
						boolean isValidproject = false;
						if(DET_DESC_LEN > project.length()){
							isValidproject = true;
						}
						if(!isValidproject){
							throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
						}
						
						if (project.equalsIgnoreCase("null")|| project == null)
							project = "";
						
						isExistCustomer = false;
						FinProjectDAO finProjectDAO = new FinProjectDAO();
						if(project.length()>0){
							isExistCustomer = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
						if(!isExistCustomer){
							throw new Exception("Project '" + project + "' is not created yet on system.");
						}else {
							List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
							projectid = String.valueOf(finpro.get(0).getID());
						}
						}
						
						 ArrayList arrCustCUS = custUtil.getCustomerDetails(customerCode,Plant);
						 String paytype = (String) arrCustCUS.get(18);
						// payment type data validation
						if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
							paymenttype= paytype;
						}else if(paytype.equalsIgnoreCase("null")|| paytype == null || paytype.equalsIgnoreCase("")){
							paymenttype ="";
						}else {
						
							boolean isValidpaymenttype = false;
							if(OREDER_TYPE_LEN >= paymenttype.length()){
								isValidpaymenttype = true;
							}
							if(!isValidpaymenttype){
								throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
							}
							
							/*MasterDAO masterDAO = new MasterDAO();*/
							isValidpaymenttype=masterDAO.getPaymentModeisExist(Plant,paymenttype);
							if(!isValidpaymenttype){
				     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding purchase at Line "+linecnt+".");
				     		}
						}
						
						// DONO field data validation
						boolean isValidTonoData = false;
						if(TONO_LEN >= tono.length()){
							isValidTonoData = true;
						}
						if(!isValidTonoData){
							throw new Exception("TONO value :  '" + tono + "' length has exceeded the maximum value of "+TONO_LEN+" Chars.");
						}
						
						// validation against database entries
						// DONO number validation with status
						boolean isNewStatusToNo = false;
						isNewStatusToNo = toUtil.isNewStatusTONO(tono,Plant);
	                    if(isNewStatusToNo){
	                            throw new Exception("TONO '" + tono + "' is closed or already moved to open satus. you can't edit on system at Line "+linecnt+" ."); 
	                    }
						
						// RefNo field data validation
					    boolean isValidRefNoData = false;
					    if(DO_REF_LEN >= refNo.length()){ //thanz
					        isValidRefNoData = true;
					    }
					    if(!isValidRefNoData){
					        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
					    }
						
					    // data entry validation for date, time, quantity and unit price fields
						ImportUtils impUtil = new ImportUtils();
						
						// date field validation
						boolean isCorrectDateFormat = false;
						if (!order_date.equalsIgnoreCase("null") && order_date != null){
							isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
							if(!isCorrectDateFormat){
								throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						// time field validation
						boolean isCorrectTimeFormat = false;
						if (!time.equalsIgnoreCase("null") && time != null){
							isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
							if(!isCorrectTimeFormat){
								throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						  // From, To Loc validation
                        LocUtil locUtil = new LocUtil();
                        boolean isExistFLoc = false;
                        isExistFLoc = locUtil.isValidLocInLocmst(Plant, from_loc);
                        if(!isExistFLoc){
                                throw new Exception("From Location '" + from_loc + "' is not valid location on system at Line "+linecnt+" ."); 
                        }
                        
                        boolean isExistTLoc = false;
                        isExistTLoc = locUtil.isValidLocInLocmst(Plant, to_loc);
                        if(!isExistTLoc){
                                throw new Exception("To Location '" + to_loc + "' is not valid location on system at Line "+linecnt+" ."); 
                        }
                        
                        // check for different locations
                        if(to_loc.equalsIgnoreCase(from_loc)){
                                throw new Exception("Consignment Locations '" + to_loc + "' and '" + from_loc + "'  must be different at Line "+linecnt+" ."); 
                       }
						
						// order type validation
						if (!order_type.equalsIgnoreCase("null") && order_type != null){
							boolean isExistOrderType = false;
							OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
							isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
							if(!isExistOrderType){
								throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
							}
						}
						
						// delivery date field validation
						boolean isCorrectDeliveryDateFormat = false;
						if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
							isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
							if(!isCorrectDeliveryDateFormat){
								throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
							}
						}
						
						 if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
						    {
						    	deliverybydate="N";
						    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding sales at Line "+linecnt+".");  
				     		  }
						
						// gst field validation
						boolean isCorrectGstFormat = false;
						if (!consignment_gst.trim().equalsIgnoreCase("null") && consignment_gst != null){
							isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(consignment_gst);
							if(!isCorrectGstFormat){
								throw new Exception("VAT value '" + consignment_gst + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						// Employee field validation
						 if (!employeename.equalsIgnoreCase("null") && employeename != null){
								boolean isExistEmployee = false;
								Hashtable htEmp = new Hashtable();
					    		htEmp.put(IDBConstants.PLANT,Plant);
					    		htEmp.put("EMPNO",employeename);
					    		EmployeeUtil empUtil = new EmployeeUtil();
					    		if(!empUtil.isExistsEmployee(htEmp))
					    	    {
					    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
					    	    }
								}
							
						// Incoterm field validation 
						 boolean isValidINCOTERMS = false;
							if(INCOTERM_LEN > incoterm.length()){
								isValidINCOTERMS = true;
							}
							if(!isValidINCOTERMS){
								throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
							}
							
						
							// Sales Location field validation	
							boolean isValidsalesLocation = false;
								if(SALES_LOCATION > salesLocation.length()){
									isValidsalesLocation = true;
			                }
			                if(!isValidsalesLocation){
			                        throw new Exception("Sales Location value '" + salesLocation + "' length has exceeded the maximum value of "+SALES_LOCATION+" Chars at Line "+linecnt+".");
			                }
			                
			                if(issuloc) {
				               MasterUtil _MasterUtil=new  MasterUtil();
				     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesLocation,Plant,COUNTRYCODE);
				     		   if(ccList.size()>0)
				     		   {
				     			  for (int i = 0; i < ccList.size(); i++) {
				     			     Map map1 = (Map) ccList.get(i);
				     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
				     			 }
				     		   }
				     		   else
				     		   {
				     			  throw new Exception("Enter Valid Sales Location  : '" + salesLocation + "', before adding sales at Line "+linecnt+".");
				     		   }
			                }else {
			                	salesLocation = "";
			                }
			                
			                if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
			                      currencyID = baseCurrency;
						
		     		   // Currency ID validation
					    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
					            boolean isExistCurID = false;
					            CurrencyUtil currUtil = new CurrencyUtil();
					            
					            Hashtable ht = new Hashtable();
					            ht.put(IDBConstants.PLANT, Plant);
					            ht.put(IDBConstants.CURRENCYID, currencyID );
					            isExistCurID = currUtil.isExistCurrency(ht,"");
					            if(!isExistCurID){
					                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
					            }
					    }
						
					   // Equivalent Currency validation
					    boolean isequivalentCurrencyFormat = false;
					    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
					    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
								if(!isequivalentCurrencyFormat){
									throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
								}
							
					    }
					    else
					    {
					    	CurrencyDAO currencyDAO = new CurrencyDAO();
					    	List curQryList=new ArrayList();
					    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
					    	for(int i =0; i<curQryList.size(); i++) {
					    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
					    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
					            }
					    }
						
					  // tax field validation
			     		  boolean isValidtax = false;
							if(REMARKS_LEN > tax.length()){
								isValidtax = true;
				          }
				          if(!isValidtax){
				                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
				          }
				          
				          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("CONSIGNMENT", COUNTRYCODE, tax);
				          if(taxtypes.size() > 0) {
				        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
				        		
				        			taxid = String.valueOf(finCountryTaxType.getID());	        			
				        			String display="";
						         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
						         	{
						         		display = finCountryTaxType.getTAXTYPE();
						         	}
						         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
						         		display = finCountryTaxType.getTAXTYPE()+" ["+consignment_gst+"%]";
						         	}
						         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
						         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
						         	}
						         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
						         		if(plantstatecode.equalsIgnoreCase("")) {
						         			display = finCountryTaxType.getTAXTYPE();
						         		}else {
						         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
						         		}
						         	}
						         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
						         		if(plantstatecode.equalsIgnoreCase("")) {
						         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
						         		}else {
						         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
						         		}
						         	}
						         	else
						         	{
						         		if(plantstatecode.equalsIgnoreCase("")) {
						         			display = finCountryTaxType.getTAXTYPE()+" ["+consignment_gst+"%]";
						         		}else {
						         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+consignment_gst+"%]";
						         		}
						         	}
						         	
						         	tax=display;
				        			
				        		}
				          	}
				          else
				          {
				        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding sales at Line "+linecnt+".");
				          }
						
						
				       // Product Rates Are field validation
				          if (("").equals(productRatesAre)|| ("null").equals(productRatesAre) || productRatesAre == null){
				        	  productRatesAre = "Tax Exclusive";
						  }
				 		  boolean isValidProductRatesAre    = false;
							if(EMPTYPE_LEN > productRatesAre.length()){
								isValidProductRatesAre = true;
				          }
				          if(!isValidProductRatesAre){
				                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          
				          if(productRatesAre.length()>0){
				        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
				          
				       // is valid Line No
						    
						 int isThisANumber = 0;
						     try
						     {
						     isThisANumber = Integer.parseInt(toLno); 
						     }
						     catch (NumberFormatException nfe)
						     {
						         throw new Exception("TOLNNO value :  '" + toLno + "' for TONO "+tono+" is not a valid number  at Line "+linecnt+".");
						     }
						
						// item validation
						boolean isExistItem = false;
						ItemUtil itemUtil = new ItemUtil();
						Hashtable htitem = new Hashtable();
						htitem.put("PLANT", Plant);
						htitem.put("ITEM", item);
						htitem.put("ISACTIVE", "Y");
						isExistItem = itemUtil.isExistsItemMst(htitem);
						if (!isExistItem) {
							throw new Exception(
									"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
						}

						// Account field data validation
						boolean isValidAccount = false;
						if (PO_REMARK_LEN > account.length()) {
							isValidAccount = true;
						}
						if (!isValidAccount) {
							throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "
									+ PO_REMARK_LEN + " Chars at Line " + linecnt + ".");
						}
						
						if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
							account = "";
						}else {
							CoaDAO coadao = new CoaDAO();
							List<Map<String, String>> listQry = null;
							Map<String, List<Map<String, String>>> listGrouped = null;
							listQry = coadao.selectSubAccountTypeList(Plant, account);
							if (listQry.size() > 0) {
								String[] filterArray = { "8" };
								String[] detailArray = { "26", "25" };
								listQry = listQry.stream()
										.filter(y -> Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
										.collect(Collectors.toList());
								listGrouped = listQry.stream()
										.filter(x -> Arrays.stream(filterArray).anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
										.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		
								if (listGrouped.size() > 0) {
		
								} else {
									throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
											+ linecnt + ".");
								}
							} else {
								throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
										+ linecnt + ".");
							}
						}

						// UOM field data validation
						boolean isValidUomData = false;
						if (UOM >= uom.length()) {
							isValidUomData = true;
						}
						if (!isValidUomData) {
							throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of " + UOM
									+ " Chars at Line " + linecnt + ".");
						}
						if (!uom.equalsIgnoreCase("null") && uom != null) {
							boolean isExistUom = false;
							htitem.put("STKUOM", uom);

							UomUtil uomUtil = new UomUtil();
							isExistUom = uomUtil.isExistsUom(uom, Plant);
							if (!isExistUom) {
								throw new Exception("UOM '" + uom + "' is not created/in active at line " + linecnt + ".");
							}
						}
						
						// quantity field validation
						boolean isCorrectQuantityFormat = false;
						if (!qty.equalsIgnoreCase("null") && qty != null) {
							isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
							if (!isCorrectQuantityFormat) {
								throw new Exception(
										"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
							}
						}
						
						// Product delivery date field validation
					    boolean isCorrectproductdeliverydate = false;
					    if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
					    	isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
							if(!isCorrectproductdeliverydate){
								throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
							}
					    }

					    // unit price field validation
						boolean isCorrectUnitPriceFormat = false;
						if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
							isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
							if(!isCorrectUnitPriceFormat){
								throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
							}
						}
						if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
							if(unit_price.contains(".")){
								int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
								if(pricedecnum>numberOfDecimal)
								{
									throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
								}
							}
							
						}
						
						// Product Discount field data validation			
						boolean isCorrectproductdiscount = false;
						if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
							isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
							if(!isCorrectproductdiscount){
								throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
							}
						}else {
							productdiscount = "0";
						}
						
						// Product Discount % field validation			
						if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
					    {
							productdiscountPer=currencyID;
					    }
					  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
						{
						  productdiscountPer="%";
						}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
						{
							productdiscountPer=currencyID;
						}
						else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding sales at Line "+linecnt+".");  
			     		  }
						
						
						if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
							orderdiscount = "0";
						
						
						// Order Discount field validation
						boolean isCorrectOrderDiscount = false;
						if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
							isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
							if(!isCorrectOrderDiscount){
								throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
							}
						}else {
							orderdiscount = "0";
						}
						
						// Order Discount Are field validation
				 		  boolean isValidorderdiscountAre    = false;
							if(EMPTYPE_LEN > orderdiscounttaxinc.length()){
								isValidorderdiscountAre = true;
				          }
				          if(!isValidorderdiscountAre){
				                  throw new Exception("Order Discount Are value '" + orderdiscounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          
				          if (("").equals(orderdiscounttaxinc)|| ("null").equals(orderdiscounttaxinc) || orderdiscounttaxinc == null){
				        	  orderdiscounttaxinc = "Tax Inclusive";
						  }
				          
				          if(orderdiscounttaxinc.length()>0){
				        	  if(orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Order Discount Are can't be '"+orderdiscounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
				          
				       // Order Discount % field validation			
							if (orderdiscountpre.equals("null") || orderdiscountpre == null || orderdiscountpre.equals("") )
						    {
								orderdiscountpre=currencyID;
						    }
						  else if (orderdiscountpre.equalsIgnoreCase("Y")|| orderdiscountpre.equalsIgnoreCase("y"))
							{
							  orderdiscountpre="%";
							}else if (orderdiscountpre.equalsIgnoreCase("N")|| orderdiscountpre.equalsIgnoreCase("n"))
							{
								orderdiscountpre=currencyID;
							}
							else if(!orderdiscountpre.equalsIgnoreCase("N")|| !orderdiscountpre.equalsIgnoreCase("n")||!orderdiscountpre.equalsIgnoreCase("Y")|| !orderdiscountpre.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountpre + "', before adding sales at Line "+linecnt+".");  
				     		  }
							
							
						
								
						
						
						//shipping cost validation					
						boolean isCorrectShippingCost = false;
						if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
							isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
							if(!isCorrectShippingCost){
								throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
							}
						}
						
						// Shipping Cost Are field validation
				 		  boolean isValidShippingCostAre    = false;
							if(EMPTYPE_LEN > shippingcosttaxinc.length()){
								isValidShippingCostAre = true;
				          }
				          if(!isValidShippingCostAre){
				                  throw new Exception("Shipping Cost Are value '" + shippingcosttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          if (("").equals(shippingcosttaxinc)|| ("null").equals(shippingcosttaxinc) || shippingcosttaxinc == null){
				        	  shippingcosttaxinc = "Tax Inclusive";
						  }
				          if(shippingcosttaxinc.length()>0){
				        	  if(shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Shipping Cost Are can't be '"+shippingcosttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
														
				        //Adjustment field validation	
							boolean isCorrectadjustment = false;
							if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
								isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
								if(!isCorrectadjustment){
									throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
								}
							}else {
								adjustment = "0";
							}
						
							// Remarks1 field data validation
							boolean isValidRemarksData1 = false;
							if(DO_REMARK_LEN1 >= remarks1.length()){
								isValidRemarksData1 = true;
							}
							if(!isValidRemarksData1){
								throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
							}
							

							// Remarks1 field data validation
							boolean isValidRemarksData2 = false;
							if(DO_REMARK_LEN2 >= remarks2.length()){
								isValidRemarksData2 = true;
							}
							if(!isValidRemarksData2){
								throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
							}
					
	     		   
						m.put("PLANT", Plant);
						m.put(IConstants.TR_ASSIGNEE, customerCode);
						if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
							shippingcustomer = "";
						m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
						m.put("SHIPPINGID", shippingid);
						m.put("PROJECTID", projectid);
						m.put("TAX", tax);
						m.put("TAXID", taxid);
						m.put("EQUIVALENTCURRENCY", equivalentCurrency);
						if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
						{
							productRatesAre="1";
						}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
						{
							productRatesAre="0";
						}else {
							productRatesAre="0";
						}
						m.put("ISTAXINCLUSIVE", productRatesAre);
						m.put("ACCOUNT", account);
						m.put("PRODUCTDELIVERYDATE", productdeliverydate);
						if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null)
							productdiscount = "0";
						
						if(!productdiscountPer.equalsIgnoreCase("%")) {
							if(!equivalentCurrency.equalsIgnoreCase("")) {
								productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
							}
						}
						m.put("PRODUCTDISCOUNT", productdiscount);

						m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
						
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
						}
//						
						m.put("ADJUSTMENT", adjustment);
//						
//						
						if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
							orderdiscount = "0";
						if(!orderdiscountpre.equalsIgnoreCase("%")) {
							if(!equivalentCurrency.equalsIgnoreCase("")) {
								orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
							}
						}
						m.put(IConstants.ORDERDISCOUNT, orderdiscount);
//						
						if (orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
						{
							orderdiscounttaxinc="1";
						}else if (orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
						{
							orderdiscounttaxinc="0";
						}else {
							orderdiscounttaxinc="0";
						}
						m.put("ISORDERDISCOUNTTAX", orderdiscounttaxinc);
//						
						m.put("ORDERDISCOUNTTYPE", orderdiscountpre);
						
	

						
						if (shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
						{
							shippingcosttaxinc="1";
						}else if (shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
						{
							shippingcosttaxinc="0";
						}else {
							shippingcosttaxinc="0";
						}
						m.put("ISSHIPPINGTAX", shippingcosttaxinc);
						
						m.put(IConstants.TR_TONO, tono);
					        m.put(IConstants.TR_TOLNNO, toLno);
					        m.put(IConstants.TR_FROM_LOC, from_loc);
                            m.put(IConstants.TR_TO_LOC, to_loc);
                            m.put(IConstants.TR_ASSIGNEE, customerCode);
                                                    
					    if (refNo.equalsIgnoreCase("null")|| refNo == null)
					    refNo = "";
					    m.put(IConstants.TR_REF_NO, refNo);
	                                    
						if (order_type.equalsIgnoreCase("null")|| order_type == null)
						order_type = "";
						m.put(IConstants.TR_ORDERTYPE, order_type);

					    if (order_date.equalsIgnoreCase("null")|| order_date == null)
					        order_date = DateUtils.getDate();
						m.put(IConstants.TR_ORDER_DATE, order_date);
						
						if (time.equalsIgnoreCase("null")|| time == null)
					         time = DateUtils.getTimeHHmm();
						m.put(IConstants.TR_ORDER_TIME, time);
						
						if (remarks1.equalsIgnoreCase("null")
								|| remarks1 == null)
							remarks1 = "";
						m.put(IConstants.TR_REMARK1, remarks1);
						
						if (remarks2.equalsIgnoreCase("null")
								|| remarks2 == null)
							remarks2 = "";
						m.put(IConstants.TR_REMARK3, remarks2);
						
						if (consignment_gst.equalsIgnoreCase("null")
								|| consignment_gst == null){
							//inbound_gst = "0";
							GstTypeUtil gstTypeUtil = new GstTypeUtil();
							ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "OUTBOUND");
							Map gstMap = (Map)gstDetails.get(0);
							consignment_gst  = (String)gstMap.get("GSTPERCENTAGE");					
							
						}
													
						m.put(IConstants.TR_CONSIGNMENT_GST, consignment_gst);
						
						m.put(IConstants.TR_ITEM, item);
						if (qty.equalsIgnoreCase("null") || qty == null)
							qty = "0";
						m.put(IConstants.TR_QTY, qty);

						if (uom.equalsIgnoreCase("null")|| uom == null)
					        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"SALESUOM");
						m.put(IConstants.TR_ITEM_UOM, uom);
						
						if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
		                      currencyID = baseCurrency;
		                      m.put(IConstants.IN_CURRENCYID, currencyID);

						if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
						unit_price = "0";
						String minSellingconvertedcost="";
						ArrayList Mprice = itemUtil.getMprice(Plant, item);
						Map gstMap = (Map)Mprice.get(0);
						Minprice  = (String)gstMap.get("MINSPRICE");	
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductimport(Plant,currencyID);
						if(unit_price != "0"  && !minSellingconvertedcost.equals("") && !minSellingconvertedcost.equals(null) &&  Double.valueOf(unit_price) < Double.valueOf(Minprice)*Double.valueOf(minSellingconvertedcost))
						{
							throw new Exception("Price should not be less than minimum selling price");
						}
						
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
						}
						
						m.put(IConstants.TR_UNITCOST, unit_price);
						m.put(IConstants.PRODGST, productdiscount);
	            
	                      
	                    if (employeename.equalsIgnoreCase("null")|| employeename == null)
	  						employeename = "";
	  					m.put(IConstants.TOHDR_EMPNO, employeename);
	  					
	  					/* if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
	  						deliverydate = DateUtils.getDate();*/
					
						 if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
	  						deliverydate = "";
	 					m.put(IConstants.DELIVERYDATE, deliverydate);
	 					
						if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
							shippingcost = "0";
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
						}
						m.put(IConstants.SHIPPINGCOST, shippingcost);
						
						if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
							incoterm = "";
						m.put(IConstants.INCOTERMS, incoterm);
						
						

						if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
							paymenttype = "";
						m.put(IConstants.PAYMENTTYPE, paymenttype);
						if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
							{
							deliverybydate="1";
							}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
							{
							deliverybydate="0";
							}
						m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);
						m.put(IConstants.SALES_LOCATION, salesLocation);

					}

					hdrList.add(m);

					System.out.println("hdrList :: " + hdrList.size());
				}

			} catch (Exception ex) {
				// ex.printStackTrace();
				throw ex;
			} finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", ValidNumber);
            hdrList.add(mv);
            
            return hdrList;

		}
   
    
    
    @SuppressWarnings("unchecked")
    public ArrayList downloadSupplierSheetData(String Plant, String filename,
                    String sheetName,String Region,String NOOFSUPPLIER,String baseCurrency) throws Exception {
            System.out.println("********  Import Starts *********");

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);
            String ValidNumber="";
            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer
                                    .parseInt(SupplierNumberOfColumn),"Sup");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                  //Validate no.of supplier -- Azees 15.11.2020
    				CustomerBeanDAO custdao = new CustomerBeanDAO();
    				
    				int novalid =custdao.Vendorcount(Plant);
    				int convl = 0;
    				if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
    				    convl = Integer.valueOf(NOOFSUPPLIER);
                    
                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            novalid=novalid+1;
            				if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
            				{	
            					if(novalid>convl)
            					{
            						ValidNumber="SUPPLIER";
            						//throw new Exception("You have reached the limit of "+NOOFSUPPLIER+" suppliers you can create");
            						break;
            					}
            				}
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                           if(list.size() > 0) {
                                    
                                    String vendno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String vname = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String companyregnumber = StrUtils.replaceStr2Char(((String) list.get(2)).trim());//IMTHI
                                    String CURRENCY_ID = StrUtils.replaceStr2Char(((String) list.get(3)).trim());//IMTHI
                                    String supplier_type_id  = StrUtils.replaceStr2Char(((String) list.get(4)).trim());                                    
                                    String transportmode  = StrUtils.replaceStr2Char(((String) list.get(5)).trim());    
                                    TransportModeDAO transportmodedao = new TransportModeDAO();
                                    String trans = "";
                					if(transportmode.length()>0){
                						trans = transportmodedao.getTransportModeByName(Plant,transportmode);
                					}
                					else {
                						
                						transportmode = "";
                					}
                					if(transportmode.equals("null")) {
                						transportmode = "" ;
            							trans = "";
            						}
                                    String telno = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
                                    String fax = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                                    String customeremail = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
                                    String website   = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
                                    String peppol = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String peppolid = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
                                    String taxtret   = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
                                    String rcbno  = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
//                                  String openingbalance   = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String paymenttype   = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
                                    String paymentterms   = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
                                    String days  = StrUtils.replaceStr2Char(((String) list.get(16)).trim());                                    
                                    String facebook   = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
                                    String twitter   = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
                                    String linkedin   = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
                                    String skype   = StrUtils.replaceStr2Char(((String) list.get(20)).trim());                                    
                                    String contactName = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
                                    String designation = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
                                    String workphone = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
                                    String hpno  = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
                                    String email = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
                                    String country = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
                                    String addr1 = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
                                    String addr2 = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
                                    String addr3 = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
                                    String addr4 = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
                                    String state = StrUtils.replaceStr2Char(((String) list.get(31)).trim());                                    
                                    String zip   = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
                                    String iban   = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
                                    String bank   = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
                                    String routingCode   = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
                                    String remarks   = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
                                    String isActive   = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
                                    
                                    //String rcbno  = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
                                    
                                    if(StrUtils.isCharAllowed(vendno) || StrUtils.isCharAllowed(vname) ||StrUtils.isCharAllowed(CURRENCY_ID) || StrUtils.isCharAllowed(supplier_type_id) || StrUtils.isCharAllowed(transportmode) ||StrUtils.isCharAllowed(customeremail) || StrUtils.isCharAllowed(workphone) || StrUtils.isCharAllowed(peppolid) || StrUtils.isCharAllowed(facebook) || StrUtils.isCharAllowed(twitter) || StrUtils.isCharAllowed(linkedin) || StrUtils.isCharAllowed(skype) || StrUtils.isCharAllowed(website) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(paymentterms)){
//                                    	if(StrUtils.isCharAllowed(vendno) || StrUtils.isCharAllowed(vname) || StrUtils.isCharAllowed(supplier_type_id) || StrUtils.isCharAllowed(customeremail) || StrUtils.isCharAllowed(workphone) || StrUtils.isCharAllowed(facebook) || StrUtils.isCharAllowed(twitter) || StrUtils.isCharAllowed(linkedin) || StrUtils.isCharAllowed(skype) || StrUtils.isCharAllowed(openingbalance) || StrUtils.isCharAllowed(website) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(paymentterms)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    if(StrUtils.isCharAllowed(vendno) || StrUtils.isCharAllowed(vname) || StrUtils.isCharAllowed(companyregnumber) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(paymentterms)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                if (("").equals(vendno) || ("null").equals(vendno)){
                                    throw new Exception("Supplier Id value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                
                                if (("").equals(vname) || ("null").equals(vname)){
                                    throw new Exception("Supplier Name value is mandatory and  missing at Line "+linecnt+" .");
                                }
                                
//                                if(Region.equalsIgnoreCase("GCC")){
//                                	companyregnumber = "";
//                                }else if(Region.equalsIgnoreCase("ASIA PACIFIC")) {
//                                if (("").equals(companyregnumber) || ("null").equals(companyregnumber)){
//                                    throw new Exception("Unique Entity Number (UEN) is mandatory and  missing at Line "+linecnt+" .");
//                                }
//                                }
                                
                                if (("").equals(taxtret) || ("null").equals(taxtret)){
                                    throw new Exception("Tax Treatment value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                           
                                if (("").equals(country) || ("null").equals(country)){
                                    throw new Exception("Country value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                if (("").equals(state) || ("null").equals(state)){
                                	state="";  
                            }
                                if (("").equals(bank) || ("null").equals(bank)){
                                	bank="";  
                                }
                                
                                    // VENDNO field data validation
                                    boolean isValidData = false;
                                    if(VENDNO_LEN >= vendno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Supplier Id value : '" + vendno + "' length has exceeded the maximum value of "+VENDNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    
                                    isValidData = false;
                                    if(VNAME_LEN >= vname.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Supplier Name value '" + vname + "' length has exceeded the maximum value of "+VNAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(VENDNO_LEN >= companyregnumber.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Unique Entity Number (UEN) value : '" + companyregnumber + "' length has exceeded the maximum value of "+VENDNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                  //Author Name: Resvi , Date: 12/07/2021 
                                    isValidData = false;
                                    if(CURRENCY_LEN > CURRENCY_ID.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Currency value : '" + CURRENCY_ID + "' length has exceeded the maximum value of "+CURRENCY_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (!CURRENCY_ID.equalsIgnoreCase("null") && CURRENCY_ID != null){
            				            boolean isExistCurID = false;
            				            CurrencyUtil currUtil = new CurrencyUtil();
                                                        
                                                        Hashtable ht = new Hashtable();
                                                        ht.put(IDBConstants.PLANT, Plant);
                                                        ht.put("CURRENCYID" , CURRENCY_ID );
            				            isExistCurID = currUtil.isExistCurrency(ht,"");
            				            if(!isExistCurID){
            				                    throw new Exception("Currency ID '" + CURRENCY_ID + "' is not created yet on system at Line "+linecnt+".");
            				            }
            				    }
                                    
                                
                				    if (CURRENCY_ID.equalsIgnoreCase("null")|| CURRENCY_ID == null)
                				    	CURRENCY_ID = baseCurrency;
                				    
                				    
                                    
									     isValidData = false;
                                    if(SUPPLIER_TYPE_ID > supplier_type_id.length()){
                                        isValidData = true;
                                }
                                    if(!isValidData){
                                        throw new Exception("Supplier Type (SUPPLIER_TYPE_ID) value '" + supplier_type_id + "' length has exceeded the maximum value of "+SUPPLIER_TYPE_ID+" Chars at Line "+linecnt+".");
                                   }
 									Hashtable ht = new Hashtable();
                        			ht.put(IConstants.PLANT, Plant);
                        			ht.put(IConstants.SUPPLIERTYPEID, supplier_type_id);
                                    boolean isExists = new CustomerBeanDAO().isExistsSupplierType(ht);
								
									if (!supplier_type_id.trim().equalsIgnoreCase("null") && supplier_type_id != null){
                                    if (!isExists) {
                                       throw new Exception("Create Supplier Type  : '" + supplier_type_id + "' first, before adding Supplier at Line "+linecnt+".");
                                    }
                                    }
									
                                    if (trans == null){
											throw new Exception("Create Transport Mode : '" + transportmode + "' first, before adding Transport at Line "+linecnt+".");
									}
                                    
									isValidData = false;
                                    if(TELNO_LEN >= telno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("TelNo value '" + telno + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(EMAIL_LEN >= customeremail.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    
                                            throw new Exception("Supplier Email value  length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                
                                    isValidData = false;
                                    if(WEBSITE_LEN >= website.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("Website value length has exceeded the maximum value of "+WEBSITE_LEN+" Chars at Line "+linecnt+".");
                                }
                                
                                if (peppol.equalsIgnoreCase("null") || peppol == null)
                                	peppol = "N";
                                if (peppol.equals("Y")||peppol.equals("y")||peppol.equals("N")||peppol.equals("n")){
                                	if(peppol.equals("Y")||peppol.equals("y"))
                                		peppol="1";
                                	else
                                		peppol="0";
								}
								else
								 {
									throw new Exception("Customer Peppol  '" + peppol + "' is not valid format at Line "+linecnt+".");
                                 }
                                if (peppol.equalsIgnoreCase("1")) {
                                if (("").equals(peppolid) || ("null").equals(peppolid)){
                                    throw new Exception("Peppol Id value is missing at Line "+linecnt+" .");  
                                 }
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= peppolid.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Peepol Id value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                                
                                isValidData = false;
                                if(TAXTREATMENT_LEN > taxtret.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                        throw new Exception("Tax Treatment value '" + taxtret + "' length has exceeded the maximum value of "+TAXTREATMENT_LEN+" Chars at Line "+linecnt+".");
                                }
                                
                                MasterUtil _MasterUtil=new  MasterUtil();
                     		   ArrayList ccList =  _MasterUtil.getTaxTreatmentList(taxtret,Plant,"");
                     		   if(ccList.size()>0)
                     		   {
                     			  Map gstMap = (Map)ccList.get(0);
                     			  taxtret  = (String)gstMap.get("TAXTREATMENT");
                     		   }
                     		   else
                     		   {
                     			  throw new Exception("Enter Valid Tax Treatment  : '" + taxtret + "', before adding Supplier at Line "+linecnt+".");
                     		   }
                     		   
                     		  if(taxtret.equalsIgnoreCase("VAT Registered")||taxtret.equalsIgnoreCase("VAT Registered - Desginated Zone"))
                     		   {
                     			 if (("").equals(rcbno) || ("null").equals(rcbno)){
                                     throw new Exception("TRN No. is mandatory for "+taxtret+" Tax Treatment, is missing at Line "+linecnt+" .");
                                 }
                     			//TRN Validation Changes 19.6.20 - azees
                     			 //if(Region.equalsIgnoreCase("GCC"))
                     			 //{
                     				long isThisANumber = 0;
        				     		try
        				     		{
        				     			isThisANumber = Long.parseLong(rcbno); 
        				     		}
        				     		catch (NumberFormatException nfe)
        				     		{
        				     			throw new Exception("TRN No. value :  '" + rcbno + "' for Tax Treatment is not a valid number  at Line "+linecnt+".");
        				     		}
        				     		
        				     		isValidData = false;
                                    if(TO_REF_LEN == rcbno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("TRN No. value '" + rcbno + "' is not valid "+TO_REF_LEN+" digit Tax number at Line "+linecnt+".");
                                    }
                     			 //}
                     		   }
                     		  else {
                     			 isValidData = false;
                                 if(WONO_LEN >= rcbno.length()){
                                         isValidData = true;
                                 }
                                 if(!isValidData){
                                         throw new Exception("TRN No. value '" + rcbno + "' length has exceeded the maximum value of "+WONO_LEN+" Chars at Line "+linecnt+".");
                                 }
                     		  }
                     		  
                     		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
                     		int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(Plant)); 
                     		ImportUtils impUtil = new ImportUtils(); 
                     		
//                     		boolean isopeningbalance = false;
//        					if (!openingbalance.trim().equalsIgnoreCase("null") && openingbalance != null){
//        						isopeningbalance = impUtil.isCorrectUnitPriceFormat(openingbalance);
//        						if(!isopeningbalance){
//        							throw new Exception("Opening Balance value '" + openingbalance + "' is not accepted format at Line "+linecnt+".");
//        						}
//        					}
//        					else
//        					{
//        						openingbalance = "0";
//        					}
        					
//        					if (!openingbalance.trim().equalsIgnoreCase("null") && openingbalance != null){
//        						if(openingbalance.contains(".")){
//        							int openingbalancedecnum = openingbalance.substring(openingbalance.indexOf(".")).length() - 1;
//        							if(openingbalancedecnum>numberOfDecimal)
//        							{
//        								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Opening Balance at Line "+linecnt+".");
//        							}
//        						}
//        						
//        					}
                     		  
                     		 isValidData = false;
                             if(REMARKS_LEN >= paymentterms.length() ){
                                     isValidData = true;
                             }
                             if(!isValidData){
                                     throw new Exception("Remarks for Payment Terms value length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                             }
                             
                             if(REMARKS_LEN >= paymenttype.length() ){
                            	 isValidData = true;
                             }
                             if(!isValidData){
                            	 throw new Exception("Remarks for Payment Type value length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                             }
                             if(!(("").equals(days) || ("null").equals(days))){
                                	int isThisANumber = 0;
        				     		try
        				     		{
        				     			isThisANumber = Integer.parseInt(days); 
        				     		}
        				     		catch (NumberFormatException nfe)
        				     		{
        				     			throw new Exception("Days value :  '" + days + "' for Payment days is not a valid number  at Line "+linecnt+".");
        				     		}
                             }
                     		   
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= facebook.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("Facebook value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= twitter.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Twitter value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                                    isValidData = false;
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= linkedin.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("LinkedIn value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= skype.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Skype value length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(CONTACT_NAME_LEN >= contactName.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Contact Name value '" + contactName + "' length has exceeded the maximum value of "+CONTACT_NAME_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(DESGINATION_LEN >= designation.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Designation value '" + designation + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(TELNO_LEN >= workphone.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Work Phone value  length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(EMAIL_LEN > email.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Email value '" + email + "' length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                            }
                            
                        String countrycode=""; 
                        if(country.length()>0)
                        {
                        ccList =  _MasterUtil.getCountryList(country, Plant, Region);
              		   if(ccList.size()>0)
              		   {
              			 Map getlist=(Map)ccList.get(0);
              			 countrycode=(String) getlist.get("COUNTRY_CODE");
              			 country=(String) getlist.get("COUNTRYNAME");
              		   }
              		   else
              		   {
              			  throw new Exception("Enter Valid Country  : '" + country + "', before adding Supplier at Line "+linecnt+".");
              		   }
                        }
                        
                        isValidData = false;
                        if(ADDR_LEN >= addr1.length()){
                                isValidData = true;
                        }
                        if(!isValidData){
                        System.out.println("addr1.length():::"+addr1.length());
                                throw new Exception("Unit Number value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                        }
                               
                                    isValidData = false;
                                    if(ADDR_LEN >= addr2.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Building Name value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(ADDR_LEN >= addr3.length()){
                                    	isValidData = true;
                                    }
                                    if(!isValidData){
                                    	System.out.println("addr3.length()::+"+addr3.length());
                                    	throw new Exception("Street Name value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }

                                    isValidData = false;
                                    if(ADDR_LEN >= addr4.length()){
                                    	isValidData = true;
                                    }
                                    if(!isValidData){
                                    	System.out.println("addr4.length()::+"+addr4.length());
                                    	throw new Exception("City value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    if(countrycode.length()>0)
                                    {
                                    	if(state.length()>0)
                                        {
                                    ccList =  _MasterUtil.getStateList(state, Plant, countrycode);
                          		   if(ccList.size()>0)
                          		   {
                          			   Map stateMap = (Map)ccList.get(0);
                          			   state  = (String)stateMap.get("STATE");
                          		   }
                          		   else
                          		   {
                          			  throw new Exception("Enter Valid State  : '" + state + "' for "+country+", before adding Supplier at Line "+linecnt+".");
                          		   }
                                        }
                                    }
                                    
                                    isValidData = false;
                                    if(EMAIL_LEN >= iban.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("IBAN value  length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if(bank.length()>0)
                                    {
                                		ccList =  _MasterUtil.getBankList(bank, Plant);
                      		   if(ccList.size()>0)
                      		   {
                      			   Map bankMap = (Map)ccList.get(0);
                      			 bank  = (String)bankMap.get("NAME");
                      		   }
                      		   else
                      		   {
                      			  throw new Exception("Enter Valid Bank  : '" + bank + ", before adding Supplier at Line "+linecnt+".");
                      		   }
                                    }
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= routingCode.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Routing Code value  length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(REMARKS_LEN >= remarks.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Remarks value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    
                                    
                                    
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.VENDOR_CODE, vendno);
                                    m.put(IDBConstants.VENDOR_NAME, vname);
                                    m.put(IDBConstants.companyregnumber, companyregnumber);
                                    m.put("CURRENCY_ID" , CURRENCY_ID);
                                    m.put(IDBConstants.RCBNO, rcbno);
                                    m.put("COMMENT1", " ");
                                    m.put(IDBConstants.NAME, contactName);
                                    m.put(IDBConstants.DESGINATION, designation);
                                    m.put(IDBConstants.TELNO, telno);
                                    m.put(IDBConstants.HPNO, hpno);
                                    m.put(IDBConstants.FAX, fax);
                                    m.put(IDBConstants.EMAIL, email);
                                    m.put(IDBConstants.ADDRESS1, addr1);
                                    m.put(IDBConstants.ADDRESS2, addr2);
                                    m.put(IDBConstants.ADDRESS3, addr3);
                                    m.put(IDBConstants.ADDRESS4, addr4);
                                    m.put(IDBConstants.STATE, state);
                                    m.put(IDBConstants.COUNTRY, country);
                                    m.put(IDBConstants.ZIP, zip);
                                    m.put(IDBConstants.REMARKS, remarks);
                                    if (isActive.equalsIgnoreCase("null") || isActive == null)
                                            isActive = "Y";
                                    m.put(IConstants.ISACTIVE, isActive);
                                    m.put(IConstants.PAYTERMS, paymenttype);
                                    m.put(IConstants.payment_terms, paymentterms);
                        			m.put(IConstants.PAYINDAYS, days);
                        			m.put(IDBConstants.SUPPLIERTYPEID, supplier_type_id);
                        			m.put(IConstants.TRANSPORTID, trans);
                        			String numberOfDecimalVal = _PlantMstDAO.getNumberOfDecimal(Plant);
//                        			float OPENINGBALANCEVALUE ="".equals(openingbalance) ? 0.0f :  Float.parseFloat(openingbalance);
//                        			
//                        			OPENINGBALANCEVALUE ="".equals(openingbalance) ? 0.0f :  Float.parseFloat(openingbalance);
//                        			openingbalance = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimalVal);
                        			
                        			m.put(IConstants.CUSTOMEREMAIL, customeremail);
                        			m.put(IConstants.WORKPHONE, workphone);
//                        			m.put(IConstants.OPENINGBALANCE, openingbalance);
                        			m.put(IConstants.FACEBOOK, facebook);
                        			m.put(IConstants.TWITTER, twitter);
                        			m.put(IConstants.LINKEDIN, linkedin);
                        			m.put(IConstants.SKYPE, skype);
                        			m.put(IConstants.WEBSITE, website);
                        			m.put("ISPEPPOL", peppol);
                        			m.put("PEPPOL_ID", peppolid);
                        			m.put(IConstants.TAXTREATMENT, taxtret);
                        			m.put(IDBConstants.IBAN, iban);
                                    m.put(IDBConstants.BANKNAME, bank);
                                    m.put(IDBConstants.BANKROUTINGCODE, routingCode);
                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            Map mv = new HashMap();
            mv.put("ValidNumber", ValidNumber);
            hdrList.add(mv);
            
            return hdrList;

    }

    
    @SuppressWarnings("unchecked")
    public ArrayList downloadCustomerSheetData(String Plant, String filename,
                    String sheetName,String Region,String NOOFCUSTOMER,String baseCurrency) throws Exception {
            System.out.println("********  Import Starts *********");

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);
            
            String ValidNumber="";
            jxl.Workbook workbook=null; 
            jxl.Sheet sheet =null; 
            workbook = jxl.Workbook.getWorkbook(new File(filename)); 
            sheet = workbook.getSheet(0); 
            int rows=0;
            int cols=0;
            rows=sheet.getRows();
            cols=sheet.getColumns();
            System.out.println("cols"+cols);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFileCustTemplate(Integer
                                    .parseInt(CustomerNumberOfColumn),"Cust",cols);

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                  //Validate no.of Customers -- Azees 15.11.2020
                    CustMstDAO custdao = new CustMstDAO();
    				
                    ArrayList arrCustot =custdao.getTotalCustomers(Plant);
                    Map mCustot=(Map)arrCustot.get(0);
                    String Custot = (String)mCustot.get("TOTAL_CUSTOMERS");
                    int novalid = Integer.valueOf(Custot);
                    int convl = 0;
    				if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
    				    convl = Integer.valueOf(NOOFCUSTOMER);
                    
                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            novalid=novalid+1;
            				if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
            				{
            					
            					if(novalid>convl)
            					{
            						ValidNumber="CUSTOMER";
            						//throw new Exception("You have reached the limit of "+NOOFCUSTOMER+" customers you can create");
            						break;
            					}
            				}
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                                    String custno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String custname = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String showbyproduct = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    String showbycategory = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                    String companyregnumber = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
                                    String CURRENCY_ID = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
                                    String customer_type_id = StrUtils.replaceStr2Char(((String) list.get(6)).trim());                                    
                                    String transport = StrUtils.replaceStr2Char(((String) list.get(7)).trim());                                    
                                    TransportModeDAO transportmodedao = new TransportModeDAO();
                                    String trans = "";
                					if(transport.length()>0){
                						trans = transportmodedao.getTransportModeByName(Plant,transport);
                					}
                					else {
                						
                						transport = "";
                					}
                					if(transport.equals("null")) {
            							transport = "" ;
            							trans = "";
            						}
                                    String telno = StrUtils.replaceStr2Char(((String) list.get(8)).trim()); 
                                    String fax = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
                                    String customeremail = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String website = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
                                    String peppol = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
                                    String peppolid = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
                                    String taxtret   = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
                                    String rcbno  = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
//                                    String openingbalance = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String paymenttype   = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
                                    String paymentterms   = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
                                    String days  = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
                                    String creditlimit  = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
                                    String creditlimitby  = StrUtils.replaceStr2Char(((String) list.get(20)).trim());                                    
                                    String facebook = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
                                    String twitter = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
                                    String linkedin = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
                                    String skype = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
                                    String contactName = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
                                    String designation = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
                                    String workphone = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
                                    String hpno  = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
                                    String email = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
                                    String country = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
                                    String addr1 = StrUtils.replaceStr2Char(((String) list.get(31)).trim());
                                    String addr2 = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
                                    String addr3 = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
                                    String addr4 = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
                                    String state = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
                                    String zip   = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
                                    
                                    String shipsameascontactaddress = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
                                    String shipcontactName = StrUtils.replaceStr2Char(((String) list.get(38)).trim());
                                    String shipdesignation = StrUtils.replaceStr2Char(((String) list.get(39)).trim());
                                    String shipworkphone = StrUtils.replaceStr2Char(((String) list.get(40)).trim());
                                    String shiphpno  = StrUtils.replaceStr2Char(((String) list.get(41)).trim());
                                    String shipemail = StrUtils.replaceStr2Char(((String) list.get(42)).trim());
                                    String shipcountry = StrUtils.replaceStr2Char(((String) list.get(43)).trim());
                                    String shipaddr1 = StrUtils.replaceStr2Char(((String) list.get(44)).trim());
                                    String shipaddr2 = StrUtils.replaceStr2Char(((String) list.get(45)).trim());
                                    String shipaddr3 = StrUtils.replaceStr2Char(((String) list.get(46)).trim());
                                    String shipaddr4 = StrUtils.replaceStr2Char(((String) list.get(47)).trim());
                                    String shipstate = StrUtils.replaceStr2Char(((String) list.get(48)).trim());
                                    String shipzip   = StrUtils.replaceStr2Char(((String) list.get(49)).trim());
                                    
                                    String iban   = StrUtils.replaceStr2Char(((String) list.get(50)).trim());                                    
                                    String bank = StrUtils.replaceStr2Char(((String) list.get(51)).trim());                                    
                                    String routingCode = StrUtils.replaceStr2Char(((String) list.get(52)).trim());
                                    String remarks   = StrUtils.replaceStr2Char(((String) list.get(53)).trim());
                                    String isActive   = StrUtils.replaceStr2Char(((String) list.get(54)).trim());
                                    
                                    String customer_status_id="";
                                    
                                  //Added by Bruhan on 12Dec2017 to check if the special are allowed//
                					if(StrUtils.isCharAllowed(custno) || StrUtils.isCharAllowed(custname) || StrUtils.isCharAllowed(companyregnumber) || StrUtils.isCharAllowed(CURRENCY_ID) ||  StrUtils.isCharAllowed(customer_type_id) || StrUtils.isCharAllowed(transport) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(shipcontactName) || StrUtils.isCharAllowed(shipdesignation) || StrUtils.isCharAllowed(shipworkphone) || StrUtils.isCharAllowed(shiphpno) || StrUtils.isCharAllowed(shipemail) || StrUtils.isCharAllowed(shipcountry) || StrUtils.isCharAllowed(shipaddr1) || StrUtils.isCharAllowed(shipaddr2) || StrUtils.isCharAllowed(shipaddr3) || StrUtils.isCharAllowed(shipaddr4) || StrUtils.isCharAllowed(shipstate) || StrUtils.isCharAllowed(shipzip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(paymentterms) || StrUtils.isCharAllowed(customeremail) || StrUtils.isCharAllowed(workphone) || StrUtils.isCharAllowed(peppolid)|| StrUtils.isCharAllowed(facebook) || StrUtils.isCharAllowed(twitter) || StrUtils.isCharAllowed(linkedin) || StrUtils.isCharAllowed(skype) || StrUtils.isCharAllowed(website) || StrUtils.isCharAllowed(customer_status_id)){
//                						if(StrUtils.isCharAllowed(custno) || StrUtils.isCharAllowed(custname) || StrUtils.isCharAllowed(companyregnumber) || StrUtils.isCharAllowed(customer_type_id) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(paymentterms) || StrUtils.isCharAllowed(customeremail) || StrUtils.isCharAllowed(workphone) || StrUtils.isCharAllowed(facebook) || StrUtils.isCharAllowed(twitter) || StrUtils.isCharAllowed(linkedin) || StrUtils.isCharAllowed(skype) || StrUtils.isCharAllowed(openingbalance) || StrUtils.isCharAllowed(website) || StrUtils.isCharAllowed(customer_status_id)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                					//End//
                					
                                   /* if(cols==22)
                                    {
                                    	 customer_status_id  = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
                                    	 System.out.println("customer_status_id"+customer_status_id);
                                    }*/

                               
                                 if (("").equals(custno) || ("null").equals(custno)){
                                    throw new Exception("Customer No.(CUSTNO) value is mandatory and  missing at Line "+linecnt+" .");  
                                 }
                                
                                if (("").equals(custname) || ("null").equals(custname)){
                                    throw new Exception("Customer Name(CNAME) value is mandatory and  missing at Line "+linecnt+" .");
                                }
                                
//                                if(Region.equalsIgnoreCase("GCC")){
//                                	companyregnumber = "";
//                                }else if(Region.equalsIgnoreCase("ASIA PACIFIC")) {
//                                if (("").equals(companyregnumber) || ("null").equals(companyregnumber)){
//                                    throw new Exception("Unique Entity Number (UEN) is mandatory and  missing at Line "+linecnt+" .");
//                                }
//                                }
                           
                                if (("").equals(taxtret) || ("null").equals(taxtret)){
                                    throw new Exception("Tax Treatment value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                if (("").equals(country) || ("null").equals(country)){
                                    throw new Exception("Country value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                if (("").equals(state) || ("null").equals(state)){
                                	state="";  
                            }
                                if (("").equals(bank) || ("null").equals(bank)){
                                	bank="";  
                                }
                                    // CUSTNO field data validation
                                    boolean isValidData = false;
                                    if(CUSTNO_LEN >= custno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Custmer Id value : '" + custno + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(CNAME_LEN >= custname.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Custmer Name value '" + custname + "' length has exceeded the maximum value of "+CNAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (showbyproduct.equalsIgnoreCase("null") || showbyproduct == null)
                                    	showbyproduct = "N";
                                    if (showbyproduct.equals("Y")||showbyproduct.equals("y")||showbyproduct.equals("N")||showbyproduct.equals("n")){
                                    	if(showbyproduct.equals("Y")||showbyproduct.equals("y"))
                                    		showbyproduct="1";
                                    	else
                                    		showbyproduct="0";
									}
									else
									 {
										throw new Exception("Customer Show APP Product Image '" + showbyproduct + "' is not valid format at Line "+linecnt+".");
	                                 }
                                    
                                    if (showbycategory.equalsIgnoreCase("null") || showbycategory == null)
                                    	showbycategory = "N";
                                    if (showbycategory.equals("Y")||showbycategory.equals("y")||showbycategory.equals("N")||showbycategory.equals("n")){
                                    	if(showbycategory.equals("Y")||showbycategory.equals("y"))
                                    		showbycategory="1";
                                    	else
                                    		showbycategory="0";
									}
									else
									 {
										throw new Exception("Customer Show APP Category Image '" + showbycategory + "' is not valid format at Line "+linecnt+".");
	                                 }
                                    
                                    isValidData = false;
                                    if(CUSTNO_LEN >= companyregnumber.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Unique Entity Number (UEN) value: '" + companyregnumber + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                  //Author Name: Resvi , Date: 12/07/2021 
                                    isValidData = false;
                                    if(CURRENCY_LEN > CURRENCY_ID.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Currency value: '" + CURRENCY_ID + "' length has exceeded the maximum value of "+CURRENCY_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (!CURRENCY_ID.equalsIgnoreCase("null") && CURRENCY_ID != null){
            				            boolean isExistCurID = false;
            				            CurrencyUtil currUtil = new CurrencyUtil();
                                                        
                                                        Hashtable ht = new Hashtable();
                                                        ht.put(IDBConstants.PLANT, Plant);
                                                        ht.put("CURRENCYID" , CURRENCY_ID );
            				            isExistCurID = currUtil.isExistCurrency(ht,"");
            				            if(!isExistCurID){
            				                    throw new Exception("Currency ID '" + CURRENCY_ID + "' is not created yet on system at Line "+linecnt+".");
            				            }
            				    }                            
                				    
                				    if (CURRENCY_ID.equalsIgnoreCase("null")|| CURRENCY_ID == null)
                				    	CURRENCY_ID = baseCurrency;
                                   // end
                                    
        				            
                                    isValidData = false;
									if(CUSTOMER_TYPE_ID > customer_type_id.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                        throw new Exception("Customer Type (CUSTOMER_TYPE_ID) value '" + customer_type_id + "' length has exceeded the maximum value of "+CUSTOMER_TYPE_ID+" Chars at Line "+linecnt+".");
                                   }
                                    
                                    if (trans == null){
											throw new Exception("Create Transport Mode : '" + transport + "' first, before adding Transport at Line "+linecnt+".");
									}
 									Hashtable ht = new Hashtable();
                        			ht.put(IConstants.PLANT, Plant);
                        			ht.put(IConstants.CUSTOMERTYPEID, customer_type_id);
                                    boolean isExists = new CustomerBeanDAO().isExistsCustomerType(ht);
								
									if (!customer_type_id.trim().equalsIgnoreCase("null") && customer_type_id != null){
                                    if (!isExists) {
                                       throw new Exception("Create Customer Type  : '" + customer_type_id + "' first, before adding Customer at Line "+linecnt+".");
                                    }
                                    }
									isValidData = false;
                                    if(TELNO_LEN >= telno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Customer Phone value '" + telno + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(EMAIL_LEN >= customeremail.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    
                                            throw new Exception("Customer Email value  length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                   
                                    isValidData = false;
                                    if(WEBSITE_LEN >= website.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("Website value length has exceeded the maximum value of "+WEBSITE_LEN+" Chars at Line "+linecnt+".");
                                }
                                
                                if (peppol.equalsIgnoreCase("null") || peppol == null)
                                	peppol = "N";
                                if (peppol.equals("Y")||peppol.equals("y")||peppol.equals("N")||peppol.equals("n")){
                                	if(peppol.equals("Y")||peppol.equals("y"))
                                		peppol="1";
                                	else
                                		peppol="0";
								}
								else
								 {
									throw new Exception("Customer Peppol  '" + peppol + "' is not valid format at Line "+linecnt+".");
                                 }
                                if (peppol.equalsIgnoreCase("1")) {
                                if (("").equals(peppolid) || ("null").equals(peppolid)){
                                    throw new Exception("Peppol Id value is missing at Line "+linecnt+" .");  
                                 }
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= peppolid.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Peepol Id value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
									isValidData = false;
	                                if(TAXTREATMENT_LEN > taxtret.length()){
	                                        isValidData = true;
	                                }
	                                if(!isValidData){
	                                        throw new Exception("Tax Treatment value '" + taxtret + "' length has exceeded the maximum value of "+TAXTREATMENT_LEN+" Chars at Line "+linecnt+".");
	                                }
	                                
	                                MasterUtil _MasterUtil=new  MasterUtil();
	                     		   ArrayList ccList =  _MasterUtil.getTaxTreatmentList(taxtret,Plant,"");
	                     		   if(ccList.size()>0)
	                     		   {
	                     			  Map gstMap = (Map)ccList.get(0);
	                     			  taxtret  = (String)gstMap.get("TAXTREATMENT"); 
	                     		   }
	                     		   else
	                     		   {
	                     			  throw new Exception("Enter Valid Tax Treatment  : '" + taxtret + "', before adding Customer at Line "+linecnt+".");
	                     		   }
	                     		   
	                     		  if(taxtret.equalsIgnoreCase("VAT Registered")||taxtret.equalsIgnoreCase("VAT Registered - Desginated Zone"))
	                     		   {
	                     			 if (("").equals(rcbno) || ("null").equals(rcbno)){
	                                     throw new Exception("TRN No. is mandatory for "+taxtret+" Tax Treatment, is missing at Line "+linecnt+" .");
	                                 }
	                     			//TRN Validation Changes 19.6.20 - azees
	                     			 //if(Region.equalsIgnoreCase("GCC"))
	                     			 //{
	                     				long isThisANumber = 0;
	        				     		try
	        				     		{
	        				     			isThisANumber = Long.parseLong(rcbno); 
	        				     		}
	        				     		catch (NumberFormatException nfe)
	        				     		{
	        				     			throw new Exception("TRN No. value :  '" + rcbno + "' for Tax Treatment is not a valid number  at Line "+linecnt+".");
	        				     		}
	        				     		
	        				     		isValidData = false;
	                                    if(TO_REF_LEN == rcbno.length()){
	                                            isValidData = true;
	                                    }
	                                    if(!isValidData){
	                                            throw new Exception("TRN No. value '" + rcbno + "' is not valid "+TO_REF_LEN+" digit Tax number at Line "+linecnt+".");
	                                    }
	                     			 //}
	                     		   }
	                     		  else {
	                     			 isValidData = false;
	                                 if(WONO_LEN >= rcbno.length()){
	                                         isValidData = true;
	                                 }
	                                 if(!isValidData){
	                                         throw new Exception("TRN No. value '" + rcbno + "' length has exceeded the maximum value of "+WONO_LEN+" Chars at Line "+linecnt+".");
	                                 }
	                     		  }
	                     		  
	                     		 PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	                      		int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(Plant)); 
	                      		ImportUtils impUtil = new ImportUtils(); 
	                      	
//	                      		boolean isopeningbalance = false;
//	         					if (!openingbalance.trim().equalsIgnoreCase("null") && openingbalance != null){
//	         						isopeningbalance = impUtil.isCorrectUnitPriceFormat(openingbalance);
//	         						if(!isopeningbalance){
//	         							throw new Exception("Opening Balance value '" + openingbalance + "' is not accepted format at Line "+linecnt+".");
//	         						}
//	         					}
//	         					else
//	         					{
//	         						openingbalance = "0";
//	         					}
//	         					
//	         					if (!openingbalance.trim().equalsIgnoreCase("null") && openingbalance != null){
//	         						if(openingbalance.contains(".")){
//	         							int openingbalancedecnum = openingbalance.substring(openingbalance.indexOf(".")).length() - 1;
//	         							if(openingbalancedecnum>numberOfDecimal)
//	         							{
//	         								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Opening Balance at Line "+linecnt+".");
//	         							}
//	         						}
//	         						
//	         					}
	                     		  
	                     		  isValidData = false;
                                  if(REMARKS_LEN >= paymentterms.length() ){
                                          isValidData = true;
                                  }
                                  if(!isValidData){
                                          throw new Exception("Payment Terms value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                  }
                                  
                                  if(REMARKS_LEN >= paymenttype.length() ){
                                	  isValidData = true;
                                  }
                                  if(!isValidData){
                                	  throw new Exception("Payment Type value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                  }
                                  
                                  if(!(("").equals(days) || ("null").equals(days))){
                                    	int isThisANumber = 0;
               				     		try
               				     		{
               				     			isThisANumber = Integer.parseInt(days); 
               				     		}
               				     		catch (NumberFormatException nfe)
               				     		{
               				     			throw new Exception("Days value :  '" + days + "' for Payment days is not a valid number  at Line "+linecnt+".");
               				     		}
                                    }
                                  
                                  if(CREDITLIMIT_LEN > creditlimit.length() ){
                                      isValidData = true;
                              }
                              if(!isValidData){
                                      throw new Exception("Credit Limit value  length has exceeded the maximum value of "+CREDITLIMIT_LEN+" Chars at Line "+linecnt+".");
                              }
                              if(CREDITLIMITBY_LEN > creditlimitby.length() ){
                                  isValidData = true;
                          }
                          if(!isValidData){
                                  throw new Exception("Is Credit Limit By value length has exceeded the maximum value of "+CREDITLIMITBY_LEN+" Chars at Line "+linecnt+".");
                          }
                                
                                  if(!(("").equals(creditlimit) || ("null").equals(creditlimit))){
                                  	float isThisANumber = 0;
             				     		try
             				     		{
             				     			isThisANumber = Float.parseFloat(creditlimit); 
             				     		}
             				     		catch (NumberFormatException nfe)
             				     		{
             				     			throw new Exception("Credit Limit value :  '" + creditlimit + "' is not a valid number  at Line "+linecnt+".");
             				     		}
                                  }
                                  
                                  if(!creditlimitby.equals("null") && !creditlimitby.equalsIgnoreCase("Daily") && !creditlimitby.equalsIgnoreCase("Monthly")) {
                                  	throw new Exception("Incorrect Credit Limit By value at Line "+linecnt+".");
                                  }
                                  
                                  if(creditlimitby.equalsIgnoreCase("null")) {
                                  	creditlimitby = "NOLIMIT";
                                  }
                                  
                                  if(creditlimit.equals("null") && creditlimitby.equals("null"))
                                  {
                                  	throw new Exception("Credit Limit value cannot be empty when Is Credit Limit By is given at Line "+linecnt+".");
                                  }
                                  
                                  if(!(("").equals(creditlimit) || ("null").equals(creditlimit))){
                                  if(Float.parseFloat(creditlimit)<0)
                                  {
                                  	throw new Exception("Credit Limit value  '"+creditlimit+"' is less than zero  at Line "+linecnt+".");
                                  }
                                  }
                                  
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= facebook.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("Facebook value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= twitter.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Twitter value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                                    isValidData = false;
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= linkedin.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("LinkedIn value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= skype.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Skype value length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(CONTACT_NAME_LEN >= contactName.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Contact Name value '" + contactName + "' length has exceeded the maximum value of "+CONTACT_NAME_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(DESGINATION_LEN >= designation.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Designation value '" + designation + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                            }
                            
                            isValidData = false;
                            if(TELNO_LEN >= workphone.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Work Phone value  length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                            }
                            isValidData = false;
                            if(EMAIL_LEN >= email.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Email value '" + email + "' length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                            }
                            String countrycode=""; 
                            if(country.length()>0)
                            {
                            ccList =  _MasterUtil.getCountryList(country, Plant, Region);
                  		   if(ccList.size()>0)
                  		   {
                  			 Map getlist=(Map)ccList.get(0);
                  			 countrycode=(String) getlist.get("COUNTRY_CODE");
                  			country=(String) getlist.get("COUNTRYNAME");
                  		   }
                  		   else
                  		   {
                  			  throw new Exception("Enter Valid Country  : '" + country + "', before adding Customer at Line "+linecnt+".");
                  		   }
                            }
                            
//                            String shipcountrycode=""; 
//                            String spcountry = country;
//                            if(shipsameascontactaddress.equals("Y"))
//    				        {
//                            	  if(spcountry.length()>0)
//                                  {
//                                  ccList =  _MasterUtil.getCountryList(spcountry, Plant, Region);
//                        		   if(ccList.size()>0)
//                        		   {
//                        			 Map getlist=(Map)ccList.get(0);
//                        			 shipcountrycode=(String) getlist.get("COUNTRY_CODE");
//                        			 spcountry=(String) getlist.get("COUNTRYNAME");
//                        		   }
//                        		   else
//                        		   {
//                        			  throw new Exception("Enter Valid Country  : '" + spcountry + "', before adding Customer at Line "+linecnt+".");
//                        		   }
//                                  }
//                            	
//    				        }else {
//                            if(shipcountry.length()>0)
//                            {
//                            ccList =  _MasterUtil.getCountryList(shipcountry, Plant, Region);
//                  		   if(ccList.size()>0)
//                  		   {
//                  			 Map getlist=(Map)ccList.get(0);
//                  			 shipcountrycode=(String) getlist.get("COUNTRY_CODE");
//                  			shipcountry=(String) getlist.get("COUNTRYNAME");
//                  		   }
//                  		   else
//                  		   {
//                  			  throw new Exception("Enter Valid Country  : '" + shipcountry + "', before adding Customer at Line "+linecnt+".");
//                  		   }
//                            }
//    				        }
//                            
//                            String SpState = state;
//                            if(shipsameascontactaddress.equals("Y"))
//    				        {
//                            if(shipcountrycode.length()>0)
//                            {
//                            	if(SpState.length()>0)
//                                {
//                            ccList =  _MasterUtil.getStateList(SpState, Plant, shipcountrycode);
//                  		   if(ccList.size()>0)
//                  		   {
//                  			 Map stateMap = (Map)ccList.get(0);
//                  			SpState  = (String)stateMap.get("STATE");
//                  		   }
//                  		   else
//                  		   {
//                  			  throw new Exception("Enter Valid State  : '" + SpState + "' for "+spcountry+", before adding Customer at Line "+linecnt+".");
//                  		   }
//                                }
//                            }
//    				        }else {
//                            if(shipcountrycode.length()>0)
//                            {
//                            	if(shipstate.length()>0)
//                                {
//                            ccList =  _MasterUtil.getStateList(shipstate, Plant, shipcountrycode);
//                  		   if(ccList.size()>0)
//                  		   {
//                  			 Map stateMap = (Map)ccList.get(0);
//                  			shipstate  = (String)stateMap.get("STATE");
//                  		   }
//                  		   else
//                  		   {
//                  			  throw new Exception("Enter Valid State  : '" + shipstate + "' for "+shipcountry+", before adding Customer at Line "+linecnt+".");
//                  		   }
//                                }
//                            }
//                            }
                            
                            isValidData = false;
                            if(ADDR_LEN >= addr1.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Unit Number value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                            }
                       
                            isValidData = false;
                            if(ADDR_LEN >= addr2.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            System.out.println("addr2.length()::+"+addr2.length());
                                    throw new Exception("Building Name value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                            }

                            isValidData = false;
                            if(ADDR_LEN >= addr3.length()){
                            	isValidData = true;
                            }
                            if(!isValidData){
                            	System.out.println("addr3.length()::+"+addr3.length());
                            	throw new Exception("Street Name value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                            }

                            isValidData = false;
                            if(ADDR_LEN >= addr4.length()){
                            	isValidData = true;
                            }
                            if(!isValidData){
                            	System.out.println("addr4.length()::+"+addr4.length());
                            	throw new Exception("City value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                            }
                            if(countrycode.length()>0)
                            {
                            	if(state.length()>0)
                                {
                            ccList =  _MasterUtil.getStateList(state, Plant, countrycode);
                  		   if(ccList.size()>0)
                  		   {
                  			 Map stateMap = (Map)ccList.get(0);
                			   state  = (String)stateMap.get("STATE");
                  		   }
                  		   else
                  		   {
                  			  throw new Exception("Enter Valid State  : '" + state + "' for "+country+", before adding Customer at Line "+linecnt+".");
                  		   }
                                }
                            }
                            
                            isValidData = false;
                            if(EMAIL_LEN >= iban.length() ){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("IBAN value  length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                            }
                            
                            if(bank.length()>0)
                            {
                        		ccList =  _MasterUtil.getBankList(bank, Plant);
              		   if(ccList.size()>0)
              		   {
              			   Map bankMap = (Map)ccList.get(0);
              			 bank  = (String)bankMap.get("NAME");
              		   }
              		   else
              		   {
              			  throw new Exception("Enter Valid Bank  : '" + bank + ", before adding Customer at Line "+linecnt+".");
              		   }
                            }
                            
                            isValidData = false;
                            if(DESGINATION_LEN >= routingCode.length() ){
                                    isValidData = true;
                            }
                            if(!isValidData){
                                    throw new Exception("Routing Code value  length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                            }
                            
                            isValidData = false;
                                    if(REMARKS_LEN >= remarks.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Remarks value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    
                                    if(("").equals(creditlimit) || ("null").equals(creditlimit)){
                                    	creditlimit="0";
                                    }
                                    
                                   /* if(cols==22){
                                    	if(CUSTOMER_STATUS_ID > customer_status_id.length()){
                                            isValidData = true;
                                    	}
                                    	if(!isValidData){
                                    		throw new Exception("Customer Status (CUSTOMER_STATUS_ID) value '" + customer_status_id + "' length has exceeded the maximum value of "+CUSTOMER_STATUS_ID+" Chars at Line "+linecnt+".");
                                    	}
                                                                        
	                                    Hashtable htCustomerStatus = new Hashtable();
	                                    htCustomerStatus.put(IConstants.PLANT, Plant);
	                                    htCustomerStatus.put(IConstants.CUSTOMERSTATUSID, customer_status_id);
	                                    boolean isExistsCustomerStatus = new CustomerBeanDAO().isExisit(htCustomerStatus,"");
									
										if (!customer_status_id.trim().equalsIgnoreCase("null") && customer_status_id != null){
	                                    if (!isExistsCustomerStatus) {
	                                       throw new Exception("Create Customer Status  : '" + customer_status_id + "' first, before adding Customer at Line "+linecnt+".");
	                                    }
	                                    }
                                    }*/
                					
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.CUSTOMER_CODE, custno);
                                    m.put(IDBConstants.CUSTOMER_NAME, custname);
                                    m.put(IConstants.ISSHOWBYPRODUCT, showbyproduct);
                                    m.put(IConstants.ISSHOWBYCATEGORY, showbycategory);
                                    m.put(IDBConstants.companyregnumber, companyregnumber);
                                    m.put("CURRENCY_ID" , CURRENCY_ID); 
                                    m.put(IDBConstants.RCBNO, rcbno);
                                    m.put(IDBConstants.CUSTOMERTYPEID, customer_type_id);
                                    m.put(IConstants.TRANSPORTID, trans);
                                    m.put("COMMENT1", " ");
                                    m.put(IDBConstants.NAME, contactName);
                                    m.put(IDBConstants.DESGINATION, designation);
                                    m.put(IDBConstants.TELNO, telno);
                                    m.put(IDBConstants.HPNO, hpno);
                                    m.put(IDBConstants.FAX, fax);
                                    m.put(IDBConstants.EMAIL, email);
                                    m.put(IDBConstants.ADDRESS1, addr1);
                                    m.put(IDBConstants.ADDRESS2, addr2);
                                    m.put(IDBConstants.ADDRESS3, addr3);
                                    m.put(IDBConstants.ADDRESS4, addr4);
                                    m.put(IDBConstants.STATE, state);
                                    m.put(IDBConstants.COUNTRY, country);
                                    m.put(IDBConstants.ZIP, zip);
                                    
                                    if(shipsameascontactaddress.equals("Y"))
            				        {
                                    	   m.put(IConstants.SHIP_CONTACTNAME, contactName);
                                           m.put(IConstants.SHIP_DESGINATION, designation);
                                           m.put(IConstants.SHIP_WORKPHONE, workphone);
                                           m.put(IConstants.SHIP_HPNO, hpno);
                                           m.put(IConstants.SHIP_EMAIL, email);
                                           m.put(IConstants.SHIP_COUNTRY_CODE, shipcountry);
                                           m.put(IConstants.SHIP_ADDR1, addr1);
                                           m.put(IConstants.SHIP_ADDR2, addr2);
                                           m.put(IConstants.SHIP_ADDR3, addr3);
                                           m.put(IConstants.SHIP_ADDR4, addr4);
                                           m.put(IConstants.SHIP_STATE, state);
                                           m.put(IConstants.SHIP_ZIP, zip);
            				        }else{
            				        	   m.put(IConstants.SHIP_CONTACTNAME, shipcontactName);
                                           m.put(IConstants.SHIP_DESGINATION, shipdesignation);
                                           m.put(IConstants.SHIP_WORKPHONE, shipworkphone);
                                           m.put(IConstants.SHIP_HPNO, shiphpno);
                                           m.put(IConstants.SHIP_EMAIL, shipemail);
                                           m.put(IConstants.SHIP_COUNTRY_CODE, shipcountry);
                                           m.put(IConstants.SHIP_ADDR1, shipaddr1);
                                           m.put(IConstants.SHIP_ADDR2, shipaddr2);
                                           m.put(IConstants.SHIP_ADDR3, shipaddr3);
                                           m.put(IConstants.SHIP_ADDR4, shipaddr4);
                                           m.put(IConstants.SHIP_STATE, shipstate);
                                           m.put(IConstants.SHIP_ZIP, shipzip);
            				        }
                                    m.put(IConstants.SAME_AS_CONTACT_ADDRESS, shipsameascontactaddress);
                                    
                                    m.put(IDBConstants.REMARKS, remarks);
                                    if (isActive.equalsIgnoreCase("null") || isActive == null)
                                            isActive = "Y";
                                    m.put(IConstants.ISACTIVE, isActive);
                                    m.put(IConstants.PAYTERMS, paymenttype);
                                    m.put(IConstants.payment_terms, paymentterms);
                        			m.put(IConstants.PAYINDAYS, days);
                        			String numberOfDecimalVal = _PlantMstDAO.getNumberOfDecimal(Plant);
                        			float CREDITLIMITVALUE ="".equals(creditlimit) ? 0.0f :  Float.parseFloat(creditlimit);
                        			
                        			CREDITLIMITVALUE ="".equals(creditlimit) ? 0.0f :  Float.parseFloat(creditlimit);
                        			creditlimit = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimalVal);
                        			
//                        			float OPENINGBALANCEVALUE ="".equals(openingbalance) ? 0.0f :  Float.parseFloat(openingbalance);
//                        			
//                        			OPENINGBALANCEVALUE ="".equals(openingbalance) ? 0.0f :  Float.parseFloat(openingbalance);
//                        			openingbalance = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimalVal);
                        			
                        			m.put(IConstants.CREDITLIMIT, creditlimit);
                        			/*m.put(IConstants.ISCREDITLIMIT, iscreditlimit);*/
                        			m.put(IConstants.CREDIT_LIMIT_BY, creditlimitby);
                        			
                        			m.put(IConstants.CUSTOMEREMAIL, customeremail);
                        			m.put(IConstants.WORKPHONE, workphone);
//                        			m.put(IConstants.OPENINGBALANCE, openingbalance);
                        			m.put(IConstants.FACEBOOK, facebook);
                        			m.put(IConstants.TWITTER, twitter);
                        			m.put(IConstants.LINKEDIN, linkedin);
                        			m.put(IConstants.SKYPE, skype);
                        			m.put(IConstants.WEBSITE, website);
                        			m.put("ISPEPPOL", peppol);
                        			m.put("PEPPOL_ID", peppolid);
                        			m.put(IConstants.TAXTREATMENT, taxtret);
                        			m.put(IDBConstants.IBAN, iban);
                                    m.put(IDBConstants.BANKNAME, bank);
                                    m.put(IDBConstants.BANKROUTINGCODE, routingCode);
                        			/*if(cols==22){
                        			  m.put(IConstants.CUSTOMERSTATUSID, customer_status_id);
                        			}
                        			else
                        			{
                        				 m.put(IConstants.CUSTOMERSTATUSID, "");
                        			}*/
 
                                     

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            Map mv = new HashMap();
            mv.put("ValidNumber", ValidNumber);
            hdrList.add(mv);
            
            return hdrList;

    }
    
   
    
  
   /***Created on Nov 13 2014,Bruhan,Description: To include Transfer Assignee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    public ArrayList downloadTransferAssigneeSheetData(String Plant, String filename,
                    String sheetName) throws Exception {
            System.out.println("********  Import Starts *********");

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer
                                    .parseInt(TransferAssigneeNumberOfColumn),"TOAssignee");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                                    
                                    String assigneeno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String assigneename = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String contactName = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    String designation = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                    String telno = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
                                    String hpno  = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
                                    String fax = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
                                    String email = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                                    String addr1 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
                                    String addr2 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
                                    String addr3 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String addr4 = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
                                    String state = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
                                    String country = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
                                    String zip   = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
                                    String remarks   = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
                                    String isActive   = StrUtils.replaceStr2Char(((String) list.get(16)).trim());

                                    if(StrUtils.isCharAllowed(assigneeno) || StrUtils.isCharAllowed(assigneename) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                if (("").equals(assigneeno) || ("null").equals(assigneeno)){
                                    throw new Exception("Consignment Order Customer ID value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                
                                if (("").equals(assigneename) || ("null").equals(assigneename)){
                                    throw new Exception("Consignment Order Customer Name value is mandatory and  missing at Line "+linecnt+" .");
                                }
                           
                                
                                    // CUSTNO field data validation
                                    boolean isValidData = false;
                                    if(CUSTNO_LEN > assigneeno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Consignment Order Customer ID value : '" + assigneeno + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(CNAME_LEN > assigneename.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Consignment Order Customer Name value '" + assigneename + "' length has exceeded the maximum value of "+CNAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(CONTACT_NAME_LEN > contactName.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Name value '" + contactName + "' length has exceeded the maximum value of "+CONTACT_NAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(DESGINATION_LEN > designation.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Designation value '" + designation + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(TELNO_LEN > telno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("TelNo value '" + telno + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(EMAIL_LEN >= email.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Email value '" + email + "' length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(ADDR_LEN >= addr1.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Addr1 value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                               
                                    isValidData = false;
                                    if(ADDR_LEN >= addr2.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    System.out.println("addr2.length()::+"+addr2.length());
                                            throw new Exception("Addr2 value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(REMARKS_LEN > remarks.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Remarks value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                    }
                              
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.ASSIGNENO, assigneeno);
                                    m.put(IDBConstants.ASSIGNENAME, assigneename);
                                    m.put("COMMENT1", " ");
                                     m.put(IDBConstants.NAME, contactName);
                                    m.put(IDBConstants.DESGINATION, designation);
                                    m.put(IDBConstants.TELNO, telno);
                                    m.put(IDBConstants.HPNO, hpno);
                                    m.put(IDBConstants.FAX, fax);
                                    m.put(IDBConstants.EMAIL, email);
                                    m.put(IDBConstants.ADDRESS1, addr1);
                                    m.put(IDBConstants.ADDRESS2, addr2);
                                    m.put(IDBConstants.ADDRESS3, addr3);
                                    m.put(IDBConstants.ADDRESS4, addr4);
                                    m.put(IDBConstants.STATE, state);
                                    m.put(IDBConstants.COUNTRY, country);
                                    m.put(IDBConstants.ZIP, zip);
                                    m.put(IDBConstants.REMARKS, remarks);
                                    if (isActive.equalsIgnoreCase("null") || isActive == null)
                                            isActive = "Y";
                                    m.put(IConstants.ISACTIVE, isActive);
                                   
                                     

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    
    /***Created on Nov 13 2014,Bruhan,Description: To include Loan Assignee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    public ArrayList downloadLoanAssigneeSheetData(String Plant, String filename,
                    String sheetName) throws Exception {
            System.out.println("********  Import Starts *********");

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer
                                    .parseInt(LoanAssigneeNumberOfColumn),"LOAssignee");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                                    
                                    String assigneeno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String assigneename = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String contactName = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    String designation = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                    String telno = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
                                    String hpno  = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
                                    String fax = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
                                    String email = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                                    String addr1 = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
                                    String addr2 = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
                                    String addr3 = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String addr4 = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
                                    String state = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
                                    String country = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
                                    String zip   = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
                                    String remarks   = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
                                    String isActive   = StrUtils.replaceStr2Char(((String) list.get(16)).trim());

                                    if(StrUtils.isCharAllowed(assigneeno) || StrUtils.isCharAllowed(assigneename) || StrUtils.isCharAllowed(contactName) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(hpno) || StrUtils.isCharAllowed(fax) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                if (("").equals(assigneeno) || ("null").equals(assigneeno)){
                                    throw new Exception("Rental and Service Order Customer ID value is mandatory and  missing at Line "+linecnt+" .");  
                            }
                                
                                /*if(assigneeno.length()>0){
                                    String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(assigneeno);
                                    if(specialcharsnotAllowed.length()>0){
                                            throw new Exception("Loan Assignee  value : '" + assigneeno + "' has special characters "+specialcharsnotAllowed+" that are  not allowed at Line "+linecnt+".");
                                    }
                                }*/
                                
                                if (("").equals(assigneename) || ("null").equals(assigneename)){
                                    throw new Exception("Rental and Service Customer Name value is mandatory and  missing at Line "+linecnt+" .");
                                }
                           
                                
                                    // CUSTNO field data validation
                                    boolean isValidData = false;
                                    if(CUSTNO_LEN > assigneeno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Rental and Service Custoerm ID value : '" + assigneeno + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(CNAME_LEN > assigneename.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Rental and Service Name(CUSTOMERNAME) value '" + assigneename + "' length has exceeded the maximum value of "+CNAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(CONTACT_NAME_LEN > contactName.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Name value '" + contactName + "' length has exceeded the maximum value of "+CONTACT_NAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(DESGINATION_LEN > designation.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Designation value '" + designation + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(TELNO_LEN > telno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("TelNo value '" + telno + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(EMAIL_LEN >= email.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Email value '" + email + "' length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(ADDR_LEN >= addr1.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Addr1 value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                               
                                    isValidData = false;
                                    if(ADDR_LEN >= addr2.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    System.out.println("addr2.length()::+"+addr2.length());
                                            throw new Exception("Addr2 value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(REMARKS_LEN > remarks.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Remarks value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    
                              
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.LASSIGNNO, assigneeno);
                                    m.put(IDBConstants.CUSTOMER_NAME, assigneename);
                                    m.put("COMMENT1", " ");
                                     m.put(IDBConstants.NAME, contactName);
                                    m.put(IDBConstants.DESGINATION, designation);
                                    m.put(IDBConstants.TELNO, telno);
                                    m.put(IDBConstants.HPNO, hpno);
                                    m.put(IDBConstants.FAX, fax);
                                    m.put(IDBConstants.EMAIL, email);
                                    m.put(IDBConstants.ADDRESS1, addr1);
                                    m.put(IDBConstants.ADDRESS2, addr2);
                                    m.put(IDBConstants.ADDRESS3, addr3);
                                    m.put(IDBConstants.ADDRESS4, addr4);
                                    m.put(IDBConstants.STATE, state);
                                    m.put(IDBConstants.COUNTRY, country);
                                    m.put(IDBConstants.ZIP, zip);
                                    m.put(IDBConstants.REMARKS, remarks);
                                    if (isActive.equalsIgnoreCase("null") || isActive == null)
                                            isActive = "Y";
                                    m.put(IConstants.ISACTIVE, isActive);
                                   
                                     

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    /***Created on Nov 17 2014,Bruhan,Description: To include Employee master
    /* ************Modification History*********************************
	  
    */
    @SuppressWarnings("unchecked")
    public ArrayList downloadEmployeeSheetData(String Plant, String filename,
                    String sheetName,String Region,String NOOFEMPLOYEE) throws Exception {
            System.out.println("********  Import Starts *********");
            HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
            MasterDAO masterDAO = new MasterDAO();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
            encryptBean eb = new encryptBean();
            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);
            String ValidNumber="";
            try {
            		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
            	    String ispay = _PlantMstDAO.getispayroll(Plant);
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(EmployeeNumberOfColumn),"Emp");
                   // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");
                    
                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);
                    

                    //Validate no.of Employee -- Azees 15.11.2020
                      EmployeeDAO employeeDao = new EmployeeDAO();
      				
                      int novalid =employeeDao.Employeecount(Plant);
                      int convl = 0;
      				if(!NOOFEMPLOYEE.equalsIgnoreCase("Unlimited"))
      				    convl = Integer.valueOf(NOOFEMPLOYEE);
      				

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            novalid=novalid+1;
              				if(!NOOFEMPLOYEE.equalsIgnoreCase("Unlimited"))
              				{
              					
              					if(novalid>convl)
              					{
              						ValidNumber="EMPLOYEE";
              						//throw new Exception("You have reached the limit of "+NOOFEMPLOYEE+" employees you can create");
              						break;
              					}
              				}
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());
                            
                            if(list.size() > 0) {
                            	
                            		
                                    
                                    String empno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    
					/*
					 * if(!checkempcode(empno)) { throw new
					 * Exception("Employee ID format is wrong at Line "+linecnt+" ."); }
					 */
                                    String firstname = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String empuserid =StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    if(!employeeDAO.IsChkEmpUserName(empuserid,Plant,empno)) {//For Update Employee
                                    if(employeeDAO.IsUserName(empuserid)) {
                                    	throw new Exception("Employee Login ID is already exist at Line "+linecnt+" .");  
                                    } 
                                    }
                                    String password = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                    String empPwd   = eb.encrypt(password);
                                    if(password.equalsIgnoreCase("null")) {
                                    	empPwd   = "";
                                    }
                                    String emptype =  StrUtils.replaceStr2Char(((String) list.get(4)).trim());
                                    int emptypeid = hrEmpTypeDAO.getidbyemployetypee(Plant,emptype);
                                    String employeetype = String.valueOf(emptypeid);  
                                    String gender = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
                                    String dateofbirth = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
                                    String telno = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                                    String email = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
                                    String passportNo = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
                                    String countryofIssue = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
                                    String passportExpDate  = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
                                    String empreporting  = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
                                    String emprepid = employeeDAO.getEmpid(Plant, empreporting, "");
                                    String Posoutlet  = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
                                    String posid = outletBeanDAO.getOutletid(Plant, Posoutlet, "");
                                    String country = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
                                    String addr1 = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
                                    String addr2 = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
                                    String addr3 = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
                                    String addr4 = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
                                    String state = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
                                    String zip   = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
                                    String facebook   = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
                                    String twitter   = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
                                    String linkedin   = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
                                    String skype   = StrUtils.replaceStr2Char(((String) list.get(24)).trim());                                    
                                    String countryIdNo = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
                                    String countryExpDate = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
                                    String visaNo = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
                                    String visaExpDate = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
                                    String dept = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
                                    int deptid = masterDAO.getidbyDepartment(Plant,dept);
                                    String department = String.valueOf(deptid); 
                                    String designation = StrUtils.replaceStr2Char(((String) list.get(30)).trim());                                    
                                    String cashier = StrUtils.replaceStr2Char(((String) list.get(31)).trim());                                    
                                    String salesman = StrUtils.replaceStr2Char(((String) list.get(32)).trim());                                    
                                    String dateofJoin = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
                                    String dateofLeaving = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
                                    String labourCardNo   = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
                                    String workPermitNo   = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
                                    String contractStDate   = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
                                    String contractExpDate   = StrUtils.replaceStr2Char(((String) list.get(38)).trim());
                                    String iban   = StrUtils.replaceStr2Char(((String) list.get(39)).trim());                                    
                                    String bank = StrUtils.replaceStr2Char(((String) list.get(40)).trim());                                    
                                    String routingCode = StrUtils.replaceStr2Char(((String) list.get(41)).trim());
                                    /*String basicSalary = StrUtils.replaceStr2Char(((String) list.get(39)).trim());
                                    String houseRentAllo   = StrUtils.replaceStr2Char(((String) list.get(40)).trim());
                                    String transportAllo   = StrUtils.replaceStr2Char(((String) list.get(41)).trim());
                                    String communicationAllo   = StrUtils.replaceStr2Char(((String) list.get(42)).trim());
                                    String otherAllo   = StrUtils.replaceStr2Char(((String) list.get(43)).trim());
                                    String bonus   = StrUtils.replaceStr2Char(((String) list.get(44)).trim());
                                    String commission   = StrUtils.replaceStr2Char(((String) list.get(45)).trim());*/
                                    String gratuity = StrUtils.replaceStr2Char(((String) list.get(42)).trim());
                                    String airTicket   = StrUtils.replaceStr2Char(((String) list.get(43)).trim());
                                    String leaveSalary   = StrUtils.replaceStr2Char(((String) list.get(44)).trim());
                                    String remarks   = StrUtils.replaceStr2Char(((String) list.get(45)).trim());
                                    String isActive   = StrUtils.replaceStr2Char(((String) list.get(46)).trim());
                         

                                    if(StrUtils.isCharAllowed(empno) || StrUtils.isCharAllowed(firstname) || StrUtils.isCharAllowed(gender) || StrUtils.isCharAllowed(dateofbirth) || StrUtils.isCharAllowed(telno) || StrUtils.isCharAllowed(email) || StrUtils.isCharAllowed(passportNo) || StrUtils.isCharAllowed(countryofIssue) || StrUtils.isCharAllowed(passportExpDate) || StrUtils.isCharAllowed(country) || StrUtils.isCharAllowed(addr1) || StrUtils.isCharAllowed(addr2) || StrUtils.isCharAllowed(addr3) || StrUtils.isCharAllowed(addr4) || StrUtils.isCharAllowed(state) || StrUtils.isCharAllowed(zip) || StrUtils.isCharAllowed(facebook) || StrUtils.isCharAllowed(twitter) || StrUtils.isCharAllowed(linkedin) || StrUtils.isCharAllowed(skype) || StrUtils.isCharAllowed(countryIdNo) || StrUtils.isCharAllowed(countryExpDate) || StrUtils.isCharAllowed(visaNo) || StrUtils.isCharAllowed(visaExpDate)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                    if(StrUtils.isCharAllowed(dept) || StrUtils.isCharAllowed(designation) || StrUtils.isCharAllowed(dateofJoin) || StrUtils.isCharAllowed(dateofLeaving) ||  StrUtils.isCharAllowed(labourCardNo) || StrUtils.isCharAllowed(workPermitNo) || StrUtils.isCharAllowed(contractStDate) || StrUtils.isCharAllowed(contractExpDate) || StrUtils.isCharAllowed(iban) || StrUtils.isCharAllowed(bank) || StrUtils.isCharAllowed(routingCode) ||  StrUtils.isCharAllowed(remarks) || StrUtils.isCharAllowed(isActive) || StrUtils.isCharAllowed(cashier) || StrUtils.isCharAllowed(salesman)){
                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                					}
                                    
                                if (("").equals(empno) || ("null").equals(empno)){
                                    throw new Exception("Employee Id value is mandatory and  missing at Line "+linecnt+" .");  
                                }
                                if(ispay.equalsIgnoreCase("1")) {
	                                if (("").equals(empuserid) || ("null").equals(empuserid)){
	                                    throw new Exception("Employee Login Id value is mandatory and  missing at Line "+linecnt+" .");  
	                                }
	                                
	                                if (("").equals(password) || ("null").equals(password)){
	                                    throw new Exception("Password value is mandatory and  missing at Line "+linecnt+" .");  
	                                }
	                                
	                                if (("").equals(empreporting) || ("null").equals(empreporting)){
	                                    throw new Exception("Employee Reporting Id value is mandatory and  missing at Line "+linecnt+" .");  
	                                }
	                                
	                                
                                }
                                
                                if (("").equals(empreporting) || ("null").equals(empreporting)){
                                	
                                }else {
                                	if (("").equals(emprepid) || ("null").equals(emprepid)){
	                                    throw new Exception("Employee Reporting Id '"+empreporting+"' is not created yet at Line "+linecnt+" .");  
	                                }
                                }
                                
                                if (("").equals(Posoutlet) || ("null").equals(Posoutlet)){
                                	
                                }else {
                                	if (("").equals(posid) || ("null").equals(posid)){
                                		throw new Exception("Pos Outlet Id '"+Posoutlet+"' is not created yet at Line "+linecnt+" .");  
                                	}
                                }
                              
                                if (("").equals(emptype) || ("null").equals(emptype)){
                                	
                                }else {
	                                if(emptypeid == 0) {
	                                	throw new Exception("Employee Type value is Wrong at Line "+linecnt+" .");  
	                                }
                                }
                                
                               if (("").equals(dept) || ("null").equals(dept)){
                                	
                                }else {
	                                if(deptid == 0) {
	                                	throw new Exception("Department value is Wrong at Line "+linecnt+" .");  
	                                }
                                }
    
                                if (("").equals(firstname) || ("null").equals(firstname)){
                                    throw new Exception("Employee Name value is mandatory and  missing at Line "+linecnt+" .");
                                }
                                
                                if (("").equals(gender) || ("null").equals(gender)){
                                    throw new Exception("Gender value is mandatory and  missing at Line "+linecnt+" .");
                                }
                                
                                /*if (("").equals(country) || ("null").equals(country)){
                                    throw new Exception("Country value is mandatory and  missing at Line "+linecnt+" .");
                                }*/
                                
                                if (("").equals(state) || ("null").equals(state)){
                                	state="";  
                                }
                                
                                if (("").equals(countryofIssue) || ("null").equals(countryofIssue)){
                                	countryofIssue="";  
                                }
                                
                                if (("").equals(bank) || ("null").equals(bank)){
                                	bank="";  
                                }
                                    // CUSTNO field data validation
                                    boolean isValidData = false;
                                    if(CUSTNO_LEN >= empno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Id value : '" + empno + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(CNAME_LEN >= firstname.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Name value '" + firstname + "' length has exceeded the maximum value of "+CNAME_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(20 >= empuserid.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Login ID value '" + empuserid + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(20 >= password.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Password value '" + password + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(50 >= empreporting.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Reporting ID value '" + empreporting + "' length has exceeded the maximum value of 50 Chars at Line "+linecnt+".");
                                    }
                                    
                                    if(EMPTYPE_LEN >= emptype.length()){
                                        isValidData = true;
	                                }
	                                if(!isValidData){
	                                        throw new Exception("Employee Type value '" + emptype + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
	                                }
                                    
									if (("").equals(gender) || ("null").equals(gender)){
		                                    throw new Exception("Gender value is mandatory and  missing at Line "+linecnt+" .");  
		                            }
									if (gender.equals("M")||gender.equals("m")||gender.equals("F")||gender.equals("f")){
									}
									else
									 {
										throw new Exception("Gender'" + gender + "' is not valid format at Line "+linecnt+".");
	                                 }
 				  				    isValidData = false;
                                    if(GENDER_LEN >= gender.length()    ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Gender value '" + gender + "' length has exceeded the maximum value of "+GENDER_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    ImportUtils impUtil = new ImportUtils();
                                    boolean isCorrectDateFormat = false;
                					if (!dateofbirth.equalsIgnoreCase("null") && dateofbirth != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(dateofbirth);
                						if(!isCorrectDateFormat){
                							throw new Exception("Date of Birth '" + dateofbirth + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                					isValidData = false;
                                    if(TELNO_LEN >= telno.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Phone value '" + telno + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(EMAIL_LEN >= email.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee Email value '" + email + "' length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(TELNO_LEN >= passportNo.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Passport Number value '" + passportNo + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    MasterUtil _MasterUtil=new  MasterUtil();
                                    if(countryofIssue.length()>0)
                                    {
                                        ArrayList ccList =  _MasterUtil.getCountryList(countryofIssue, Plant, Region);
                          		   if(ccList.size()>0)
                          		   {
                          			 Map getlist=(Map)ccList.get(0);                          			 
                          			countryofIssue=(String) getlist.get("COUNTRYNAME");
                          		   }
                          		   else
                          		   {
                          			  throw new Exception("Enter Valid Country of Issue  : '" + countryofIssue + "', before adding Employee at Line "+linecnt+".");
                          		   }
                                    }
                                    
                                    if (!passportExpDate.equalsIgnoreCase("null") && passportExpDate != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(passportExpDate);
                						if(!isCorrectDateFormat){
                							throw new Exception("Passport ExpiryDate '" + passportExpDate + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    
                                    String countrycode=""; 
                                    if(country.length()>0)
                                    {
                                    ArrayList ccList =  _MasterUtil.getCountryList(country, Plant, Region);
                          		   if(ccList.size()>0)
                          		   {
                          			 Map getlist=(Map)ccList.get(0);
                          			 countrycode=(String) getlist.get("COUNTRY_CODE");
                          			 country=(String) getlist.get("COUNTRYNAME");
                          		   }
                          		   else
                          		   {
                          			  throw new Exception("Enter Valid Country  : '" + country + "', before adding Employee at Line "+linecnt+".");
                          		   }
                                    }
                                    
                                    isValidData = false;
                                    if(ADDR_LEN >= addr1.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Unit No. value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                               
                                    isValidData = false;
                                    if(ADDR_LEN >= addr2.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    System.out.println("addr2.length()::+"+addr2.length());
                                            throw new Exception("Building value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(ADDR_LEN >= addr3.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    System.out.println("addr3.length()::+"+addr3.length());
                                            throw new Exception("Street value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(ADDR_LEN >= addr4.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                    System.out.println("addr4.length()::+"+addr4.length());
                                            throw new Exception("City value  length has exceeded the maximum value of "+ADDR_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if(countrycode.length()>0)
                                    {
                                    	if(state.length()>0)
                                        {
                                    		ArrayList ccList =  _MasterUtil.getStateList(state, Plant, countrycode);
                          		   if(ccList.size()>0)
                          		   {
                          			   Map stateMap = (Map)ccList.get(0);
                          			   state  = (String)stateMap.get("STATE");
                          		   }
                          		   else
                          		   {
                          			  throw new Exception("Enter Valid State  : '" + state + "' for "+country+", before adding Employee at Line "+linecnt+".");
                          		   }
                                        }
                                    }
                                    
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= facebook.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("Facebook value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= twitter.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Twitter value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }
                                    isValidData = false;
                                    isValidData = false;
                                    if(FACEBOOK_LEN >= linkedin.length()){
                                        isValidData = true;
                                }
                                if(!isValidData){
                                
                                        throw new Exception("LinkedIn value  length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                                }
                                isValidData = false;
                                if(FACEBOOK_LEN >= skype.length()){
                                    isValidData = true;
                            }
                            if(!isValidData){
                            
                                    throw new Exception("Skype value length has exceeded the maximum value of "+FACEBOOK_LEN+" Chars at Line "+linecnt+".");
                            }

                                    isValidData = false;
                                    if(TELNO_LEN >= countryIdNo.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("UAE Id Number value '" + countryIdNo + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (!countryExpDate.equalsIgnoreCase("null") && countryExpDate != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(countryExpDate);
                						if(!isCorrectDateFormat){
                							throw new Exception("UAE ExpiryDate '" + countryExpDate + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    isValidData = false;
                                    if(TELNO_LEN >= visaNo.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Visa Number value '" + visaNo + "' length has exceeded the maximum value of "+TELNO_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (!visaExpDate.equalsIgnoreCase("null") && visaExpDate != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(visaExpDate);
                						if(!isCorrectDateFormat){
                							throw new Exception("Visa ExpiryDate '" + visaExpDate + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= dept.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Department value '" + dept + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= designation.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Designation value '" + designation + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (cashier.equalsIgnoreCase("null") || cashier == null)
                                    	cashier = "N";
                                    if (cashier.equals("Y")||cashier.equals("y")||cashier.equals("N")||cashier.equals("n")){
									}
									else
									 {
										throw new Exception("Employee is Cashier '" + cashier + "' is not valid format at Line "+linecnt+".");
	                                 }
 				  				    isValidData = false;
                                    if(GENDER_LEN >= cashier.length()    ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee is Cashier value '" + cashier + "' length has exceeded the maximum value of "+GENDER_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (cashier.equals("Y")||cashier.equals("y")) {
                                    	cashier="1";
                                    }
                                    else if(cashier.equals("N")||cashier.equals("n")){
                                    	cashier="0";                                    	
									}
                                    
                                    if (salesman.equalsIgnoreCase("null") || salesman == null)
                                    	salesman = "N";
                                    if (salesman.equals("Y")||salesman.equals("y")||salesman.equals("N")||salesman.equals("n")){
                                    }
                                    else
                                    {
                                    	throw new Exception("Employee is Sales Man '" + salesman + "' is not valid format at Line "+linecnt+".");
                                    }
                                    isValidData = false;
                                    if(GENDER_LEN >= salesman.length()    ){
                                    	isValidData = true;
                                    }
                                    if(!isValidData){
                                    	throw new Exception("Employee is Sales Man value '" + salesman + "' length has exceeded the maximum value of "+GENDER_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (salesman.equals("Y")||salesman.equals("y")) {
                                    	salesman="1";
                                    }
                                    else if(salesman.equals("N")||salesman.equals("n")){
                                    	salesman="0";                                    	
                                    }
                                    
                                    if (!dateofJoin.equalsIgnoreCase("null") && dateofJoin != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(dateofJoin);
                						if(!isCorrectDateFormat){
                							throw new Exception("Date of Joining '" + dateofJoin + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    if (!dateofLeaving.equalsIgnoreCase("null") && dateofLeaving != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(dateofLeaving);
                						if(!isCorrectDateFormat){
                							throw new Exception("Date of Leaving '" + dateofLeaving + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= labourCardNo.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Labour Card Number value '" + labourCardNo + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= workPermitNo.length()){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Work Permit Number value '" + workPermitNo + "' length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (!contractStDate.equalsIgnoreCase("null") && contractStDate != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(contractStDate);
                						if(!isCorrectDateFormat){
                							throw new Exception("Contract StartDate '" + contractStDate + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    if (!contractExpDate.equalsIgnoreCase("null") && contractExpDate != null){
                						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(contractExpDate);
                						if(!isCorrectDateFormat){
                							throw new Exception("Contract ExpiryDate '" + contractExpDate + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                                    
                                    isValidData = false;
                                    if(EMAIL_LEN >= iban.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("IBAN value  length has exceeded the maximum value of "+EMAIL_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if(bank.length()>0)
                                    {
                                		ArrayList ccList =  _MasterUtil.getBankList(bank, Plant);
                      		   if(ccList.size()>0)
                      		   {
                      			   Map bankMap = (Map)ccList.get(0);
                      			 bank  = (String)bankMap.get("NAME");
                      		   }
                      		   else
                      		   {
                      			  throw new Exception("Enter Valid Bank  : '" + bank + ", before adding Employee at Line "+linecnt+".");
                      		   }
                                    }
                                    
                                    isValidData = false;
                                    if(DESGINATION_LEN >= routingCode.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Routing Code value  length has exceeded the maximum value of "+DESGINATION_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                             		int numberOfDecimal = Integer.parseInt(_PlantMstDAO.getNumberOfDecimal(Plant));
                             		boolean isCorrectUnitPriceFormat = false;
                             		if (!gratuity.trim().equalsIgnoreCase("null") && gratuity != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(gratuity);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Gratuity value '" + gratuity + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						gratuity = "0";
                					}
                					
                					if (!gratuity.trim().equalsIgnoreCase("null") && gratuity != null){
                						if(gratuity.contains(".")){
                							int decnum = gratuity.substring(gratuity.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Gratuity at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!airTicket.trim().equalsIgnoreCase("null") && airTicket != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(airTicket);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Air Ticket Allowance value '" + airTicket + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						airTicket = "0";
                					}
                					
                					if (!airTicket.trim().equalsIgnoreCase("null") && airTicket != null){
                						if(airTicket.contains(".")){
                							int decnum = airTicket.substring(airTicket.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Air Ticket Allowance at Line "+linecnt+".");
                							}
                						}
                						
                					}
                                    
                					if (!leaveSalary.trim().equalsIgnoreCase("null") && leaveSalary != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(leaveSalary);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Leave Salary value '" + leaveSalary + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						leaveSalary = "0";
                					}
                					
                					if (!leaveSalary.trim().equalsIgnoreCase("null") && leaveSalary != null){
                						if(leaveSalary.contains(".")){
                							int decnum = leaveSalary.substring(leaveSalary.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Leave Salary at Line "+linecnt+".");
                							}
                						}
                						
                					}
                             		
                             		/*if (!basicSalary.trim().equalsIgnoreCase("null") && basicSalary != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(basicSalary);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Basic Salary value '" + basicSalary + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						basicSalary = "0";
                					}
                					
                					if (!basicSalary.trim().equalsIgnoreCase("null") && basicSalary != null){
                						if(basicSalary.contains(".")){
                							int decnum = basicSalary.substring(basicSalary.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Basic Salary at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!houseRentAllo.trim().equalsIgnoreCase("null") && houseRentAllo != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(houseRentAllo);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("House Rent Allowance value '" + houseRentAllo + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						houseRentAllo = "0";
                					}
                					
                					if (!houseRentAllo.trim().equalsIgnoreCase("null") && houseRentAllo != null){
                						if(houseRentAllo.contains(".")){
                							int decnum = houseRentAllo.substring(houseRentAllo.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in House Rent Allowance at Line "+linecnt+".");
                							}
                						}
                						
                					}
                                    
                					if (!transportAllo.trim().equalsIgnoreCase("null") && transportAllo != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(transportAllo);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Transport Allowance value '" + transportAllo + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						transportAllo = "0";
                					}
                					
                					if (!transportAllo.trim().equalsIgnoreCase("null") && transportAllo != null){
                						if(transportAllo.contains(".")){
                							int decnum = transportAllo.substring(transportAllo.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Transport Allowance at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!communicationAllo.trim().equalsIgnoreCase("null") && communicationAllo != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(communicationAllo);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Communication Allowance value '" + communicationAllo + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						communicationAllo = "0";
                					}
                					
                					if (!communicationAllo.trim().equalsIgnoreCase("null") && communicationAllo != null){
                						if(communicationAllo.contains(".")){
                							int decnum = communicationAllo.substring(communicationAllo.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Communication Allowance at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!otherAllo.trim().equalsIgnoreCase("null") && otherAllo != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(otherAllo);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Other Allowance value '" + otherAllo + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						otherAllo = "0";
                					}
                					
                					if (!otherAllo.trim().equalsIgnoreCase("null") && otherAllo != null){
                						if(otherAllo.contains(".")){
                							int decnum = otherAllo.substring(otherAllo.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Other Allowance at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!bonus.trim().equalsIgnoreCase("null") && bonus != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(bonus);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Bonus value '" + bonus + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						bonus = "0";
                					}
                					
                					if (!bonus.trim().equalsIgnoreCase("null") && bonus != null){
                						if(bonus.contains(".")){
                							int decnum = bonus.substring(bonus.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Bonus at Line "+linecnt+".");
                							}
                						}
                						
                					}
                					
                					if (!commission.trim().equalsIgnoreCase("null") && commission != null){
                						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(commission);
                						if(!isCorrectUnitPriceFormat){
                							throw new Exception("Commission value '" + commission + "' is not accepted format at Line "+linecnt+".");
                						}
                					}
                					else
                					{
                						commission = "0";
                					}
                					
                					if (!commission.trim().equalsIgnoreCase("null") && commission != null){
                						if(commission.contains(".")){
                							int decnum = commission.substring(commission.indexOf(".")).length() - 1;
                							if(decnum>numberOfDecimal)
                							{
                								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Commission at Line "+linecnt+".");
                							}
                						}
                						
                					}*/
                					
                                    isValidData = false;
                                    if(REMARKS_LEN >= remarks.length() ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Remarks value  length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
                                    }
                                    
                                    if (isActive.equalsIgnoreCase("null") || isActive == null)
                                        isActive = "Y";
                                    if (isActive.equals("Y")||isActive.equals("y")||isActive.equals("N")||isActive.equals("n")){
									}
									else
									 {
										throw new Exception("Employee is Active '" + isActive + "' is not valid format at Line "+linecnt+".");
	                                 }
 				  				    isValidData = false;
                                    if(GENDER_LEN >= isActive.length()    ){
                                            isValidData = true;
                                    }
                                    if(!isValidData){
                                            throw new Exception("Employee is Active value '" + isActive + "' length has exceeded the maximum value of "+GENDER_LEN+" Chars at Line "+linecnt+".");
                                    }
                                                                        
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.EMPNO, empno);
                                    m.put(IDBConstants.FNAME, firstname);
                                    m.put(IDBConstants.EMPUSERID, empuserid);
                                    m.put(IDBConstants.PASSWORD_EMP, empPwd);
                                    m.put(IDBConstants.EMPLOYEETYPEID, employeetype); 
                                    m.put(IDBConstants.GENDER, gender);
                                    m.put(IDBConstants.DOB, dateofbirth);
                                    m.put(IDBConstants.TELNO, telno);
                                    m.put(IDBConstants.EMAIL, email);
                                    m.put(IDBConstants.PASSPORTNUMBER, passportNo);
                                    m.put(IDBConstants.COUNTRYOFISSUE, countryofIssue);
                                    m.put(IDBConstants.PASSPORTEXPIRYDATE, passportExpDate);
                                    m.put(IDBConstants.REPORTING_INCHARGE, emprepid);
                                    m.put(IDBConstants.OUTLETS_CODE, posid);
                                    m.put(IDBConstants.COUNTRY, country);
                                    m.put(IDBConstants.ADDRESS1, addr1);
                                    m.put(IDBConstants.ADDRESS2, addr2);
                                    m.put(IDBConstants.ADDRESS3, addr3);
                                    m.put(IDBConstants.ADDRESS4, addr4);
                                    m.put(IDBConstants.STATE, state);
                                    m.put(IDBConstants.ZIP, zip);
                                    m.put(IDBConstants.FACEBOOK, facebook);
                                    m.put(IDBConstants.TWITTER, twitter);
                                    m.put(IDBConstants.LINKEDIN, linkedin);
                                    m.put(IDBConstants.SKYPE, skype);
                                    m.put(IDBConstants.EMIRATESID, countryIdNo);
                                    m.put(IDBConstants.EMIRATESIDEXPIRY, countryExpDate);
                                    m.put(IDBConstants.VISANUMBER, visaNo);
                                    m.put(IDBConstants.VISAEXPIRYDATE, visaExpDate);
                                    m.put(IDBConstants.DEPTARTMENT, dept);
                                    m.put(IDBConstants.DESGINATION, designation);                                    
                                    m.put("ISCASHIER", cashier);                                    
                                    m.put("ISSALESMAN", salesman);                                    
                                    m.put(IDBConstants.DATEOFJOINING, dateofJoin);
                                    m.put(IDBConstants.DATEOFLEAVING, dateofLeaving);                                    
                                    m.put(IDBConstants.LABOURCARDNUMBER, labourCardNo);
                                    m.put(IDBConstants.WORKPERMITNUMBER, workPermitNo);
                                    m.put(IDBConstants.CONTRACTSTARTDATE, contractStDate);
                                    m.put(IDBConstants.CONTRACTENDDATE, contractExpDate);
                                    m.put(IDBConstants.IBAN, iban);
                                    m.put(IDBConstants.BANKNAME, bank);
                                    m.put(IDBConstants.BANKROUTINGCODE, routingCode);
                                    /*m.put(IDBConstants.BASICSALARY, basicSalary);
                                    m.put(IDBConstants.HOUSERENTALLOWANCE, houseRentAllo);
                                    m.put(IDBConstants.TRANSPORTALLOWANCE, transportAllo);
                                    m.put(IDBConstants.COMMUNICATIONALLOWANCE, communicationAllo);
                                    m.put(IDBConstants.OTHERALLOWANCE, otherAllo);
                                    m.put(IDBConstants.BONUS, bonus);                                    
                                    m.put(IDBConstants.COMMISSION, commission); */   
                                    m.put(IDBConstants.GRATUITY, gratuity);
                                    m.put(IDBConstants.AIRTICKET, airTicket);
                                    m.put(IDBConstants.LEAVESALARY, leaveSalary);
                                    m.put(IDBConstants.REMARKS, remarks);                                    
                                    m.put(IConstants.ISACTIVE, isActive);
                           
                                    
                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            Map mv = new HashMap();
            mv.put("ValidNumber", ValidNumber);
            hdrList.add(mv);
            
            return hdrList;

    }
    
    
  /***Created on Dec 15 2014,Bruhan,Description: To include Production Bom,routing master
  /* ************Modification History*********************************
	  
  */ 
  
  @SuppressWarnings("unchecked")
	public ArrayList downloadProdBOMSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(ProdBOMNumberOfColumn),"ProdBOM");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String parentprod = ((String) list.get(0)).trim();
				    String childprod = ((String) list.get(1)).trim();
				    String qtyper = ((String) list.get(2)).trim();
					String operationseq = ((String) list.get(3)).trim();
					String remarks1 = ((String) list.get(4)).trim();
					String remarks2 = ((String) list.get(5)).trim();
						
				
				    if (("").equals(parentprod)|| ("null").equals(parentprod)){
				        throw new Exception("Parent Product value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(childprod)|| ("null").equals(childprod)){
				        throw new Exception("CHild Prod value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				     if (("").equals(qtyper)|| ("null").equals(qtyper)){
				        throw new Exception("QtyPer value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(operationseq)|| ("null").equals(operationseq)){
				        throw new Exception("Operation SeqNum value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    
				    ImportUtils impUtil = new ImportUtils();           
           	
					// parentprod field data validation
					boolean isValidParentData = false;
					if(ITEM_LEN > parentprod.length()){
						isValidParentData = true;
					}
					if(!isValidParentData){
						throw new Exception("Parent Product value :  '" + parentprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
					
					// childprod field data validation
					boolean isValidChildData = false;
					if(ITEM_LEN > childprod.length()){
						isValidChildData = true;
					}
					if(!isValidChildData){
						throw new Exception("Child Product value :  '" + childprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
                      
					
					// qtyper field validation
					boolean isCorrectQtyperFormat = false;
					if (!qtyper.trim().equalsIgnoreCase("null") && qtyper != null){
						isCorrectQtyperFormat = impUtil.isCorrectQuantityFormat(qtyper);
						if(!isCorrectQtyperFormat){
							throw new Exception("QtyPer value '" + qtyper + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
					
								   					
					// Remarks field data validation
					boolean isValidRemarks1Data = false;
					if(WO_REMARK_LEN > remarks1.length()){
						isValidRemarks1Data = true;
					}
					if(!isValidRemarks1Data){
						throw new Exception("Remark1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+WO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					// Remarks field data validation
					boolean isValidRemarks2Data = false;
					if(WO_REMARK_LEN > remarks2.length()){
						isValidRemarks2Data = true;
					}
					if(!isValidRemarks2Data){
						throw new Exception("Remark2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+WO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
				
					// validation against database entries
					 // item validation
					ItemMstDAO _ItemMstDAO =new ItemMstDAO();
					
					Hashtable htPItemMst= new Hashtable();
					htPItemMst.clear();
					htPItemMst.put(IDBConstants.PLANT, Plant);
					htPItemMst.put(IDBConstants.ITEM, parentprod);
					
					 boolean isExistParentItem = false;
					 isExistParentItem = _ItemMstDAO.isExisit(htPItemMst, "");
					 if(!isExistParentItem){
					         throw new Exception("Parent Product : '" + parentprod + "' is not Valid at Line "+linecnt+".");
					 }
					 
					 Hashtable htCItemMst= new Hashtable();
					 htCItemMst.clear();
					 htCItemMst.put(IDBConstants.PLANT, Plant);
					 htCItemMst.put(IDBConstants.ITEM, childprod);
					
					 boolean isExistChildItem = false;
					 isExistChildItem = _ItemMstDAO.isExisit(htCItemMst, "");
					 if(!isExistChildItem){
					         throw new Exception("Child Product : '" + childprod + "' is not Valid at Line "+linecnt+".");
					 }
						
					// opseq validation
					 /*OperationSeqUtil operationsequtil = new OperationSeqUtil();
					 boolean isExistOpSeq = false;
					 Hashtable htseqCondition = new Hashtable();
						htseqCondition.put(IDBConstants.OPRSEQNUM,operationseq);
						htseqCondition.put("PLANT", Plant);
						isExistOpSeq = operationsequtil.isExistsOprSeq(htseqCondition);
						if(!isExistOpSeq){
						throw new Exception("Operation Sequence Number: '" + operationseq + "' is not Valid at Line "+linecnt+".");
					}*/
						
					if(parentprod.equalsIgnoreCase(childprod)){
							throw new Exception("Child Product cannot be same as Parent Product.Choose diff child product at Line "+linecnt+".");
							
						}
					
				
					m.put("PLANT", Plant);
					m.put(IDBConstants.PARENTITEM, parentprod);
				    m.put(IDBConstants.CHILDITEM, childprod);
				    m.put(IDBConstants.QTY, qtyper);
				    m.put(IConstants.OPRSEQNUM, operationseq);
				    
				    if (remarks1.equalsIgnoreCase("null")|| remarks1 == null)
				    	remarks1 = "";
				    m.put(IDBConstants.ITEMMST_REMARK1, remarks1);
                                  
				    if (remarks2.equalsIgnoreCase("null")|| remarks2 == null)
				    	remarks2 = "";
				    m.put(IDBConstants.ITEMMST_REMARK2, remarks2);
                  
				
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	}   
  
  @SuppressWarnings("unchecked")
	public ArrayList downloadRoutingSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(RoutingNumberOfColumn),"Routing");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String parentprod = ((String) list.get(0)).trim();
				    String operationseq = ((String) list.get(1)).trim();
					String remarks = ((String) list.get(2)).trim();
					
				
				    if (("").equals(parentprod)|| ("null").equals(parentprod)){
				        throw new Exception("Parent Product value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(operationseq)|| ("null").equals(operationseq)){
				        throw new Exception("Operation SeqNum value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    
				    ImportUtils impUtil = new ImportUtils();           
         	
					// parentprod field data validation
					boolean isValidParentData = false;
					if(ITEM_LEN > parentprod.length()){
						isValidParentData = true;
					}
					if(!isValidParentData){
						throw new Exception("Parent Product value :  '" + parentprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
					
					// Remarks field data validation
					boolean isValidRemarksData = false;
					if(WO_REMARK_LEN > remarks.length()){
						isValidRemarksData = true;
					}
					if(!isValidRemarksData){
						throw new Exception("Remark1 value :  '" + remarks + "' length has exceeded the maximum value of "+WO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					
					// validation against database entries
					 // item validation
					ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
					 boolean isExistParentItem = false;
					 	Hashtable htprodCondition = new Hashtable();
						htprodCondition.put(IDBConstants.PARENTITEM,parentprod);
						htprodCondition.put("PLANT", Plant);
						
					 isExistParentItem = _ProductionBomUtil.isExistsProdBom(htprodCondition);
					 if(!isExistParentItem){
					         throw new Exception("Parent Product : '" + parentprod + "' is not Valid at Line "+linecnt+".");
					 }
					 
					// opseq validation
					/* OperationSeqUtil operationsequtil = new OperationSeqUtil();
					 boolean isExistOpSeq = false;
					 Hashtable htseqCondition = new Hashtable();
						htseqCondition.put(IDBConstants.OPRSEQNUM,operationseq);
						htseqCondition.put("PLANT", Plant);
						isExistOpSeq = operationsequtil.isExistsOprSeq(htseqCondition);
						if(!isExistOpSeq){
						throw new Exception("Operation Sequence Number: '" + operationseq + "' is not Valid at Line "+linecnt+".");
					}*/
						
				
					m.put("PLANT", Plant);
					m.put(IDBConstants.PARENTITEM, parentprod);
				    m.put(IConstants.OPRSEQNUM, operationseq);
				    
				    if (remarks.equalsIgnoreCase("null")|| remarks == null)
				    	remarks = "";
				    m.put(IDBConstants.REMARKS, remarks);
              	
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		return hdrList;

	}
  
  @SuppressWarnings("unchecked")
	public ArrayList downloadEstimateSheetData(String Plant, String filename,
			String sheetName,String baseCurrency,String NOOFORDER) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
	    String ValidNumber="";
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(EstimateNumberOfColumn),"EST");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid = new EstHdrDAO().Estimatecount(Plant,FROM_DATE,TO_DATE);
			int convl = 0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				convl = Integer.valueOf(NOOFORDER);				
			}
			String validpo="";
			int validcot=0;
			
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					/*String estno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
				    String estLno = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
				    String refNo = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String order_date = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String time = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String remarks = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String remarks2 = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String outbound_gst = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String employeename = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String expire_date = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String customerCode = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String paymenttype = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String item = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String ordertype = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
				    String currencyID = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
				    String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
				    String incoterm = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String shippingcustomer = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
                    String deliverydate = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
                    String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
                    String Minprice="";*/
                    
					String estno = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
 					//Validate Order azees - 12/2020
 					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
 					{
 					int ddok =0;
 					if(validpo.equalsIgnoreCase(""))
 					{
 						validpo=estno;
 						ddok=1;
 						
 					}
 					else {
 					if(validpo.equalsIgnoreCase(estno))
 					{
 						validcot=validcot+1;
 						ddok=validcot-1;
 						if(validcot>0)
 							validcot=validcot-1;
 					}
 					else {
 						validpo=estno;
 						ddok=1;
 						
 					}
 					}
 					
 					novalid=novalid+ddok;
 					
 						if(novalid>convl)
 						{
 							ValidNumber=NOOFORDER;
 							break;
 						}
 					}
 					//End
					
                	String customerCode = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					//Shipping customer validation
                	CustUtil custUtil = new CustUtil();
					String shippingcustomer = "";
   					String shippingid = "";
   					if (("").equals(shippingcustomer)|| ("null").equals(shippingcustomer)){
   						ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
							Map mCustot=(Map)arrList.get(0);
							shippingcustomer=(String) mCustot.get("CNAME");
   					}else {
   						
   						boolean isValidShippingCustomer = false;
   						if(SHIPPING_CUSTOMER_LEN > shippingcustomer.length()){
   							isValidShippingCustomer = true;
   						}
   						if(!isValidShippingCustomer){
   							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
   						} 
   						
   						boolean isExistCust= false;
   						if(shippingcustomer.length()>0){
   						isExistCust = custUtil.isExistCustomer(shippingcustomer, Plant);
   						if(!isExistCust){
   							throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system at Line "+linecnt+" .");
   						}else {
   							ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
   							Map mCustot=(Map)arrList.get(0);
   							shippingid=(String) mCustot.get("CUSTNO");
   						}
   						}
   					}
					
					
					String project = StrUtils.replaceStr2Char(((String) list.get(1)).trim()); //new
					String paymenttype = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					
					String refNo = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String order_date = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String time = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String order_type = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String deliverydate = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
				    String deliverybydate = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String outbound_gst = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String employeename = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String expire_date = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String incoterm = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
					String salesLocation = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					String currencyID = StrUtils.replaceStr2Char(((String) list.get(15)).trim()); //new
					String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(16)).trim()); //new
					String tax = StrUtils.replaceStr2Char(((String) list.get(17)).trim()); //new
					String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					String item = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
					String estLno = StrUtils.replaceStr2Char(((String) list.get(20)).trim()); //new
					String account = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(23)).trim()); //new
					String productdeliverydate = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(25)).trim()); //new
					String productdiscount = StrUtils.replaceStr2Char(((String) list.get(26)).trim()); //new
					String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
					String orderdiscount= StrUtils.replaceStr2Char(((String) list.get(28)).trim()); //new
					String orderdiscounttaxinc = StrUtils.replaceStr2Char(((String) list.get(29)).trim()); //new
					String orderdiscountpre = StrUtils.replaceStr2Char(((String) list.get(30)).trim()); //new
					/* Author: Azees  Create date: July 6,2021  Description: Hide Discount */
					/*String discount = StrUtils.replaceStr2Char(((String) list.get(32)).trim()); //new
					String discounttaxinc = StrUtils.replaceStr2Char(((String) list.get(33)).trim()); //new
					String discountpre = StrUtils.replaceStr2Char(((String) list.get(34)).trim());*/
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(31)).trim()); //new
					String shippingcosttaxinc = StrUtils.replaceStr2Char(((String) list.get(32)).trim()); //new
					String adjustment = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
					String remarks1 = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
					String remarks2 = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
					
					String discount="0";
					String discounttaxinc="";
					String discountpre="";
					
					String projectid="0";
					String taxid="0";
					String Minprice="";

					String COUNTRYCODE = "";
	           	  	String plantstate = "";
	           	  	String plantstatecode = "";
	                PlantMstUtil plantmstutil = new PlantMstUtil();
	                List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
	                for (int i = 0; i < viewlistQry.size(); i++) {
	           	     	Map map = (Map) viewlistQry.get(i);
	           	     	COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	           	     	plantstate = StrUtils.fString((String)map.get("STATE"));
	                }
	                
	                MasterDAO masterDAO = new MasterDAO();
	                boolean issuloc=false;		 
	           	 	ArrayList saloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
	           	 	if (saloctions.size() > 0) {
	           	 		issuloc = true;
	           	 	}
					

				    if(StrUtils.isCharAllowed(estno) || StrUtils.isCharAllowed(estLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(productRatesAre) || StrUtils.isCharAllowed(account)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				 // customer validation
					if (("").equals(customerCode)|| ("null").equals(customerCode)){
					    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}
				    if (("").equals(estno)|| ("null").equals(estno)){
				        throw new Exception("ESTNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
				        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				   
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(estLno)|| ("null").equals(estLno)){
				        throw new Exception("ESTLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if(issuloc) {
					    if (("").equals(salesLocation)|| ("null").equals(salesLocation)){
					        throw new Exception("Sales estimate Order Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
				    }
				    
				    /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
				        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				    
				    

					boolean isExistCustomer = false;
					isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
					}
				    
					if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
						shippingcustomer = "";
					}
					
					//Shipping customer validation
					isExistCustomer = false;
					if(shippingcustomer.length()>0){
						isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Shipping Customer '" + customerCode + "' is not created yet on system.");
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
						Map mCustot=(Map)arrList.get(0);
						shippingid=(String) mCustot.get("CUSTNO");
						shippingcustomer = (String) mCustot.get("CNAME");
					}
					}
					
					//Project validation					
					boolean isValidproject = false;
					if(DET_DESC_LEN > project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistCustomer = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistCustomer = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistCustomer){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
					
					 ArrayList arrCustCUS = custUtil.getCustomerDetails(customerCode,Plant);
					 String paytype = (String) arrCustCUS.get(18);
					// payment type data validation
					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
						paymenttype= paytype;
					}else if(paytype.equalsIgnoreCase("null")|| paytype == null || paytype.equalsIgnoreCase("")){
						paymenttype ="";
					}else {
					
						boolean isValidpaymenttype = false;
						if(OREDER_TYPE_LEN >= paymenttype.length()){
							isValidpaymenttype = true;
						}
						if(!isValidpaymenttype){
							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
						}
						
						/*MasterDAO masterDAO = new MasterDAO();*/
						isValidpaymenttype=masterDAO.getPaymentModeisExist(Plant,paymenttype);
						if(!isValidpaymenttype){
			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding purchase at Line "+linecnt+".");
			     		}
					}
					
					// ESTNO field data validation
					boolean isValidEstnoData = false;
					if(ESTNO_LEN > estno.length()){
						isValidEstnoData = true;
					}
					if(!isValidEstnoData){
						throw new Exception("Sales Estimer Order Number value :  '" + estno + "' length has exceeded the maximum value of "+ESTNO_LEN+" Chars.");
					}
					
					// RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(DO_REF_LEN >= refNo.length()){
				        isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
					
				    // data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					boolean isCorrectDateFormat = false;
					if (!order_date.equalsIgnoreCase("null") && order_date != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!time.equalsIgnoreCase("null") && time != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// delivery date field validation
					boolean isCorrectDeliveryDateFormat = false;
					if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
						isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
						if(!isCorrectDeliveryDateFormat){
							throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					 if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
					    {
					    	deliverybydate="N";
					    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding sales estimate at Line "+linecnt+".");  
			     		  }
					
					// order type validation
					if (!order_type.equalsIgnoreCase("null") && order_type != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
						}
					}
					
					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("VAT value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}

					if (outbound_gst.equalsIgnoreCase("null")
							|| outbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "ESTIMATE");
						Map gstMap = (Map)gstDetails.get(0);
						outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
					
					// Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
					 
						// expire date field validation
						boolean isCorrectExpiryDateFormat = false;
						if (!expire_date.equalsIgnoreCase("null") && expire_date != null){
							isCorrectExpiryDateFormat = impUtil.isCorrectDateFormatUsingRegex(expire_date);
							if(!isCorrectExpiryDateFormat){
								throw new Exception("Date '" + expire_date + "' is not accepted Expire date format at Line "+linecnt+".");
							}
						}
						
					// Incoterm field validation 
					 boolean isValidINCOTERMS = false;
						if(INCOTERM_LEN > incoterm.length()){
							isValidINCOTERMS = true;
						}
						if(!isValidINCOTERMS){
							throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
						}
						
					
						// Sales Location field validation	
						boolean isValidsalesLocation = false;
							if(SALES_LOCATION > salesLocation.length()){
								isValidsalesLocation = true;
		                }
		                if(!isValidsalesLocation){
		                        throw new Exception("Sales estimate Location value '" + salesLocation + "' length has exceeded the maximum value of "+SALES_LOCATION+" Chars at Line "+linecnt+".");
		                }
		                
		                if(issuloc) {
			               MasterUtil _MasterUtil=new  MasterUtil();
			     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesLocation,Plant,COUNTRYCODE);
			     		   if(ccList.size()>0)
			     		   {
			     			  for (int i = 0; i < ccList.size(); i++) {
			     			     Map map1 = (Map) ccList.get(i);
			     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
			     			 }
			     		   }
			     		   else
			     		   {
			     			  throw new Exception("Enter Valid Sales estimate  Location  : '" + salesLocation + "', before adding sales estimate at Line "+linecnt+".");
			     		   }
		                }else {
		                	salesLocation = "";
		                }
					
	     		   // Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
				            
				            Hashtable ht = new Hashtable();
				            ht.put(IDBConstants.PLANT, Plant);
				            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
	                
	                if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                      currencyID = baseCurrency;
					
				   // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }
					
				  // tax field validation
		     		  boolean isValidtax = false;
						if(REMARKS_LEN > tax.length()){
							isValidtax = true;
			          }
			          if(!isValidtax){
			                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("OUTBOUND", COUNTRYCODE, tax);
			          if(taxtypes.size() > 0) {
			        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
			        		
			        			taxid = String.valueOf(finCountryTaxType.getID());	        			
			        			String display="";
					         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
					         	{
					         		display = finCountryTaxType.getTAXTYPE();
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE();
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
					         		}
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
					         		}
					         	}
					         	else
					         	{
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+outbound_gst+"%]";
					         		}
					         	}
					         	
					         	tax=display;
			        			
			        		}
			          	}
			          else
			          {
			        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding sales estimate at Line "+linecnt+".");
			          }
					
					
			       // Product Rates Are field validation
			          if (("").equals(productRatesAre)|| ("null").equals(productRatesAre) || productRatesAre == null){
			        	  productRatesAre = "Tax Exclusive";
					  }
			 		  boolean isValidProductRatesAre    = false;
						if(EMPTYPE_LEN > productRatesAre.length()){
							isValidProductRatesAre = true;
			          }
			          if(!isValidProductRatesAre){
			                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if(productRatesAre.length()>0){
			        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales estimate at Line "+linecnt+".");
			          }
			          
			       // is valid Line No
					    
					 int isThisANumber = 0;
					     try
					     {
					     isThisANumber = Integer.parseInt(estLno); 
					     }
					     catch (NumberFormatException nfe)
					     {
					         throw new Exception("ESTNNO value :  '" + estLno + "' for ESTNO "+estLno+" is not a valid number  at Line "+linecnt+".");
					     } 
					     boolean isValidLineNumber    = false;
			       			if(LNNO_LEN >= estLno.length()){
			       				isValidLineNumber = true;
			                 }
			                 if(!isValidLineNumber){
			                         throw new Exception("ESTNNO value '" + estLno + "' length has exceeded the maximum value of "+LNNO_LEN+" Chars at Line "+linecnt+".");
			                 }
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", item);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
					}

					// Account field data validation
					boolean isValidAccount = false;
					if (PO_REMARK_LEN > account.length()) {
						isValidAccount = true;
					}
					if (!isValidAccount) {
						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "
								+ PO_REMARK_LEN + " Chars at Line " + linecnt + ".");
					}
					
					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
						account = "";
					}else {
						CoaDAO coadao = new CoaDAO();
						List<Map<String, String>> listQry = null;
						Map<String, List<Map<String, String>>> listGrouped = null;
						listQry = coadao.selectSubAccountTypeList(Plant, account);
						if (listQry.size() > 0) {
							String[] filterArray = { "8" };
							String[] detailArray = { "26", "25" };
							listQry = listQry.stream()
									.filter(y -> Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
									.collect(Collectors.toList());
							listGrouped = listQry.stream()
									.filter(x -> Arrays.stream(filterArray).anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
									.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
	
							if (listGrouped.size() > 0) {
	
							} else {
								throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
										+ linecnt + ".");
							}
						} else {
							throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
									+ linecnt + ".");
						}
					}

					// UOM field data validation
					boolean isValidUomData = false;
					if (UOM >= uom.length()) {
						isValidUomData = true;
					}
					if (!isValidUomData) {
						throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of " + UOM
								+ " Chars at Line " + linecnt + ".");
					}
					if (!uom.equalsIgnoreCase("null") && uom != null) {
						boolean isExistUom = false;
						htitem.put("STKUOM", uom);

						UomUtil uomUtil = new UomUtil();
						isExistUom = uomUtil.isExistsUom(uom, Plant);
						if (!isExistUom) {
							throw new Exception("UOM '" + uom + "' is not created/in active at line " + linecnt + ".");
						}
					}
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null) {
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if (!isCorrectQuantityFormat) {
							throw new Exception(
									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
						}
					}
					
					// Product delivery date field validation
				    boolean isCorrectproductdeliverydate = false;
				    if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
				    	isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
						if(!isCorrectproductdeliverydate){
							throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
				    }

				    // unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if(!isCorrectUnitPriceFormat){
							throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
						}
					}
					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
						if(unit_price.contains(".")){
							int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if(pricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
							}
						}
						
					}
					
					// Product Discount field data validation			
					boolean isCorrectproductdiscount = false;
					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
						if(!isCorrectproductdiscount){
							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
						}
					}else {
						productdiscount = "0";
					}
					
					// Product Discount % field validation			
					if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
				    {
						productdiscountPer=currencyID;
				    }
				  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
					{
					  productdiscountPer="%";
					}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
					{
						productdiscountPer=currencyID;
					}
					else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
		     		  {
		     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding sales estimate at Line "+linecnt+".");  
		     		  }
					
					
					
					
					
					// Order Discount field validation
					boolean isCorrectOrderDiscount = false;
					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
						if(!isCorrectOrderDiscount){
							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
						}
					}else {
						orderdiscount = "0";
					}
					
					// Order Discount Are field validation
			 		  boolean isValidorderdiscountAre    = false;
						if(EMPTYPE_LEN > orderdiscounttaxinc.length()){
							isValidorderdiscountAre = true;
			          }
			          if(!isValidorderdiscountAre){
			                  throw new Exception("Order Discount Are value '" + orderdiscounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if (("").equals(orderdiscounttaxinc)|| ("null").equals(orderdiscounttaxinc) || orderdiscounttaxinc == null){
			        	  orderdiscounttaxinc = "Tax Inclusive";
					  }
			          
			          if(orderdiscounttaxinc.length()>0){
			        	  if(orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Order Discount Are can't be '"+orderdiscounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales estimate at Line "+linecnt+".");
			          }
			          
			       // Order Discount % field validation			
						if (orderdiscountpre.equals("null") || orderdiscountpre == null || orderdiscountpre.equals("") )
					    {
							orderdiscountpre=currencyID;
					    }
					  else if (orderdiscountpre.equalsIgnoreCase("Y")|| orderdiscountpre.equalsIgnoreCase("y"))
						{
						  orderdiscountpre="%";
						}else if (orderdiscountpre.equalsIgnoreCase("N")|| orderdiscountpre.equalsIgnoreCase("n"))
						{
							orderdiscountpre=currencyID;
						}
						else if(!orderdiscountpre.equalsIgnoreCase("N")|| !orderdiscountpre.equalsIgnoreCase("n")||!orderdiscountpre.equalsIgnoreCase("Y")|| !orderdiscountpre.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountpre + "', before adding sales estimate at Line "+linecnt+".");  
			     		  }
						
						
						
						// Discount field validation
						boolean isCorrectDiscount = false;
						if (!discount.trim().equalsIgnoreCase("null") && discount != null){
							isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
							if(!isCorrectDiscount){
								throw new Exception("Discount value '" + discount + "' is not accepted format.");
							}
						}else {
							discount = "0";
						}
						
						// Discount Are field validation
				 		  boolean isValiddiscountAre    = false;
							if(EMPTYPE_LEN > discounttaxinc.length()){
								isValiddiscountAre = true;
				          }
				          if(!isValiddiscountAre){
				                  throw new Exception("Discount Are value '" + discounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          if (("").equals(discounttaxinc)|| ("null").equals(discounttaxinc) || discounttaxinc == null){
				        	  discounttaxinc = "Tax Inclusive";
						  }
				          if(discounttaxinc.length()>0){
				        	  if(discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Discount Are can't be '"+discounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales estimate at Line "+linecnt+".");
				          }
				          
				       // Discount % field validation			
							if (discountpre.equals("null") || discountpre == null || discountpre.equals("") )
						    {
								discountpre=currencyID;
						    }
						  else if (discountpre.equalsIgnoreCase("Y")|| discountpre.equalsIgnoreCase("y"))
							{
							  discountpre="%";
							}else if (discountpre.equalsIgnoreCase("N")|| discountpre.equalsIgnoreCase("n"))
							{
								discountpre=currencyID;
							}
							else if(!discountpre.equalsIgnoreCase("N")|| !discountpre.equalsIgnoreCase("n")||!discountpre.equalsIgnoreCase("Y")|| !discountpre.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Order Discount %  : '" + discountpre + "', before adding sales estimate at Line "+linecnt+".");  
				     		  }
							
					
					
					//shipping cost validation					
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Shipping Cost Are field validation
			 		  boolean isValidShippingCostAre    = false;
						if(EMPTYPE_LEN > shippingcosttaxinc.length()){
							isValidShippingCostAre = true;
			          }
			          if(!isValidShippingCostAre){
			                  throw new Exception("Shipping Cost Are value '" + shippingcosttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          if (("").equals(shippingcosttaxinc)|| ("null").equals(shippingcosttaxinc) || shippingcosttaxinc == null){
			        	  shippingcosttaxinc = "Tax Inclusive";
					  }
			          if(shippingcosttaxinc.length()>0){
			        	  if(shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Shipping Cost Are can't be '"+shippingcosttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales estimate at Line "+linecnt+".");
			          }
													
			        //Adjustment field validation	
						boolean isCorrectadjustment = false;
						if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
							isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
							if(!isCorrectadjustment){
								throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
							}
						}else {
							adjustment = "0";
						}
					
						// Remarks1 field data validation
						boolean isValidRemarksData1 = false;
						if(DO_REMARK_LEN1 >= remarks1.length()){
							isValidRemarksData1 = true;
						}
						if(!isValidRemarksData1){
							throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
						}
						

						// Remarks1 field data validation
						boolean isValidRemarksData2 = false;
						if(DO_REMARK_LEN2 >= remarks2.length()){
							isValidRemarksData2 = true;
						}
						if(!isValidRemarksData2){
							throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
						}

				    								
					m.put("PLANT", Plant);
					m.put(IConstants.EST_ESTNO, estno);
				        m.put(IConstants.EST_ESTLNNO, estLno);
				    
				    if (refNo.equalsIgnoreCase("null")|| refNo == null)
				    refNo = "";
				    m.put(IConstants.EST_REF_NO, refNo);
                                  
				    if (order_date.equalsIgnoreCase("null")|| order_date == null)
				        order_date = DateUtils.getDate();
					m.put(IConstants.EST_COLLECTION_DATE, order_date);
					
					if (time.equalsIgnoreCase("null")|| time == null)
				         time = DateUtils.getTimeHHmm();
					m.put(IConstants.EST_COLLECTION_TIME, time);
					
					if (remarks1.equalsIgnoreCase("null")
							|| remarks1 == null)
						remarks1 = "";
					m.put(IConstants.EST_REMARK1, remarks1);
					m.put(IConstants.OUT_REMARK3, remarks2);
					
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
					m.put("SHIPPINGID", shippingid);
					m.put("PROJECTID", projectid);
					m.put("TAX", tax);
					m.put("TAXID", taxid);
					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
					{
						productRatesAre="1";
					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
					{
						productRatesAre="0";
					}else {
						productRatesAre="0";
					}
					m.put("ISTAXINCLUSIVE", productRatesAre);
					m.put("ACCOUNT", account);
					m.put("PRODUCTDELIVERYDATE", productdeliverydate);
					if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null)
						productdiscount = "0";
					
					if(!productdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("PRODUCTDISCOUNT", productdiscount);

					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					
					m.put("ADJUSTMENT", adjustment);
					
					
					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
						orderdiscount = "0";
					if(!orderdiscountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.ORDERDISCOUNT, orderdiscount);
					
					if (orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						orderdiscounttaxinc="1";
					}else if (orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						orderdiscounttaxinc="0";
					}else {
						orderdiscounttaxinc="0";
					}
					m.put("ISORDERDISCOUNTTAX", orderdiscounttaxinc);
					
					m.put("ORDERDISCOUNTTYPE", orderdiscountpre);
					
					
					if (discount.equalsIgnoreCase("null")|| discount == null)
						discount = "0";
					if(!discountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.DISCOUNT, discount);
					
					if (discounttaxinc.equalsIgnoreCase("Tax Inclusive")|| discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						discounttaxinc="1";
					}else if (discounttaxinc.equalsIgnoreCase("Tax Exclusive")|| discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						discounttaxinc="0";
					}else {
						discounttaxinc="0";
					}
					m.put("ISDISCOUNTTAX", discounttaxinc);
					
					m.put("DISCOUNTTYPE", discountpre);
					
					if (shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						shippingcosttaxinc="1";
					}else if (shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						shippingcosttaxinc="0";
					}else {
						shippingcosttaxinc="0";
					}
					m.put("ISSHIPPINGTAX", shippingcosttaxinc);
					m.put(IConstants.SALES_LOCATION, salesLocation);
						
					m.put(IConstants.OUT_OUTBOUND_GST, outbound_gst);
					if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
 						deliverydate = "";
                    m.put(IDBConstants.DELIVERYDATE, deliverydate);
					
					if (employeename.equalsIgnoreCase("null")|| employeename == null)
						employeename = "";
					m.put(IConstants.EST_EMPNO, employeename);
					
					if (expire_date.equalsIgnoreCase("null")|| expire_date == null)
						expire_date = "";
					m.put(IConstants.EST_EXPIRE_DATE, expire_date);
					m.put(IConstants.EST_CUST_CODE, customerCode);
					m.put(IConstants.EST_ITEM, item);
					
					

					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.EST_QTYOR, qty);

					if (uom.equalsIgnoreCase("null")|| uom == null)
				        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"SALESUOM");
					m.put(IConstants.EST_UNITMO, uom);
					
					if (order_type.equalsIgnoreCase("null")|| order_type == null)
						order_type="";
				    m.put(IConstants.EST_ORDERTYPE, order_type);
				    
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                     currencyID = baseCurrency;
	                     m.put(IConstants.EST_CURRENCYID, currencyID);

				    if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
						unit_price = "0";
						String minSellingconvertedcost="";
						ArrayList Mprice = itemUtil.getMprice(Plant, item);
						Map gstMap = (Map)Mprice.get(0);
						Minprice  = (String)gstMap.get("MINSPRICE");	
						minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductimport(Plant,currencyID);
						if(unit_price != "0"  && !minSellingconvertedcost.equals("") && !minSellingconvertedcost.equals(null) &&  Double.valueOf(unit_price) < Double.valueOf(Minprice)*Double.valueOf(minSellingconvertedcost))
						{
							throw new Exception("Price should not be less than minimum selling price");
						}
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
						}
					m.put(IConstants.EST_UNITCOST, unit_price);
                                      
                   
                     /*if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
 						orderdiscount = "0";
                     m.put(IConstants.ORDERDISCOUNT, orderdiscount);*/
 					if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
 						shippingcost = "0";
 					if(!equivalentCurrency.equalsIgnoreCase("")) {
 						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
					}
 					m.put(IConstants.SHIPPINGCOST, shippingcost);
 				   if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
 						incoterm = "";
 					m.put(IConstants.INCOTERMS, incoterm);
 					
 					/*if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
 						shippingcustomer = "";
 					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);*/

					if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
 						paymenttype = "";
 					m.put(IConstants.PAYMENTTYPE, paymenttype);
 					if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
 					{
 						deliverybydate = "1";
 					}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
 					{
 						deliverybydate = "0";
 					}	
 					m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);

				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		
		return hdrList;

	}
  @SuppressWarnings("unchecked") 
	public ArrayList downloadInboundEstimateSheetData(String Plant, String filename,
			String sheetName,String baseCurrency) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(InboundEstimateNumberOfColumn),"InEST");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String estno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
				    String estLno = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
				    String refNo = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String order_date = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String time = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String remarks = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String outbound_gst = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String employeename = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String expire_date = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String customerCode = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String item = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String qty = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String uom = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
				    String currencyID = StrUtils.replaceStr2Char(((String) list.get(14)).trim());

				    if(StrUtils.isCharAllowed(estno) || StrUtils.isCharAllowed(estLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(expire_date) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(estno)|| ("null").equals(estno)){
				        throw new Exception("ESTNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(estLno)|| ("null").equals(estLno)){
				        throw new Exception("ESTLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(customerCode)|| ("null").equals(customerCode)){
				        throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
                                                     
				
					// ESTNO field data validation
					boolean isValidEstnoData = false;
					if(ESTNO_LEN > estno.length()){
						isValidEstnoData = true;
					}
					if(!isValidEstnoData){
						throw new Exception("ESTNO value :  '" + estno + "' length has exceeded the maximum value of "+ESTNO_LEN+" Chars.");
					}
                                      
				    // is valid Line No
				    
				     int isThisANumber = 0;
				     try
				     {
				     isThisANumber = Integer.parseInt(estLno); 
				     }
				     catch (NumberFormatException nfe)
				     {
				         throw new Exception("ESTLNNO value :  '" + estLno + "' for ESTNO "+estno+" is not a valid number  at Line "+linecnt+".");
				     }

				   
				    
				    // RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(DO_REF_LEN >= refNo.length()){
				        isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
				    
					
					// Remarks field data validation
					boolean isValidRemarksData = false;
					if(DO_REMARK_LEN > remarks.length()){
						isValidRemarksData = true;
					}
					if(!isValidRemarksData){
						throw new Exception("Remarks value :  '" + remarks + "' length has exceeded the maximum value of "+DO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					
				
					
					// data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					boolean isCorrectDateFormat = false;
					if (!order_date.equalsIgnoreCase("null") && order_date != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + order_date + "' is not accepted Order date format at Line "+linecnt+".");
						}
					}
					
					
					// expire date field validation
					boolean isCorrectExpiryDateFormat = false;
					if (!expire_date.equalsIgnoreCase("null") && expire_date != null){
						isCorrectExpiryDateFormat = impUtil.isCorrectDateFormatUsingRegex(expire_date);
						if(!isCorrectExpiryDateFormat){
							throw new Exception("Date '" + expire_date + "' is not accepted Expire date format at Line "+linecnt+".");
						}
					}
					
					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!time.equalsIgnoreCase("null") && time != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("GST value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}

					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null){
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if(!isCorrectQuantityFormat){
							throw new Exception("Quantity value '" + qty + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if(!isCorrectUnitPriceFormat){
							throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
						if(unit_price.contains(".")){
							int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if(pricedecnum>5)
							{
								throw new Exception("Invalid more than 5 digits after decimal in Unit price at Line "+linecnt+".");
							}
						}
						
					}
					
					// validation against database entries
					 // item validation
					 boolean isExistItem = false;
					 ItemUtil itemUtil = new ItemUtil();
					 Hashtable htitem= new Hashtable();
					 htitem.put("PLANT",Plant);
					 htitem.put("ITEM",item);
					 htitem.put("ISACTIVE","Y");
					 isExistItem = itemUtil.isExistsItemMst(htitem);
					 if(!isExistItem){
					         throw new Exception("Product ID '" + item + "' is not created/in active at Line "+linecnt+".");
					 }
					 
					 // UOM field data validation
					 boolean isValidUomData = false;
					 if(UOM > uom.length()){
					         isValidUomData = true;
					 }
					 if(!isValidUomData){
					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
					 }
					 if (!uom.equalsIgnoreCase("null") && uom != null){
					 boolean isValidUomforproduct =false;
					 htitem.put("STKUOM",uom);
					 isValidUomforproduct = itemUtil.isExistsItemMst(htitem);
					 if(!isValidUomforproduct){
					         throw new Exception("UOM value :  '" + uom + "' is not valid for Product ID : "+item+"  at Line "+linecnt+".");
					 }
					 }
				
					
					// customer validation
					boolean isExistVendor = false;
					CustUtil custUtil = new CustUtil();
					isExistVendor = custUtil.isExistVendor(customerCode, Plant);
					if(!isExistVendor){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
					}
					
					if (!employeename.equalsIgnoreCase("null") && employeename != null){
					boolean isExistEmployee = false;
					Hashtable htEmp = new Hashtable();
		    		htEmp.put(IDBConstants.PLANT,Plant);
		    		htEmp.put("EMPNO",employeename);
		    		EmployeeUtil empUtil = new EmployeeUtil();
		    		if(!empUtil.isExistsEmployee(htEmp))
		    	    {
		    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
		    	    }
					}
					 
				    // Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
				            
				            Hashtable ht = new Hashtable();
				            ht.put(IDBConstants.PLANT, Plant);
				            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
				    								
					m.put("PLANT", Plant);
					m.put(IConstants.EST_ESTNO, estno);
				        m.put(IConstants.EST_ESTLNNO, estLno);
				    
				    if (refNo.equalsIgnoreCase("null")|| refNo == null)
				    refNo = "";
				    m.put(IConstants.EST_REF_NO, refNo);
                                  
				    if (order_date.equalsIgnoreCase("null")|| order_date == null)
				        order_date = DateUtils.getDate();
					m.put(IConstants.EST_COLLECTION_DATE, order_date);
					
					if (time.equalsIgnoreCase("null")|| time == null)
				         time = DateUtils.getTimeHHmm();
					m.put(IConstants.EST_COLLECTION_TIME, time);
					
					if (remarks.equalsIgnoreCase("null")
							|| remarks == null)
						remarks = "";
					m.put(IConstants.EST_REMARK1, remarks);
					
					if (outbound_gst.equalsIgnoreCase("null")
							|| outbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "GST");
						Map gstMap = (Map)gstDetails.get(0);
						outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
						
						
					m.put(IConstants.OUT_OUTBOUND_GST, outbound_gst);

					
					if (employeename.equalsIgnoreCase("null")|| employeename == null)
						employeename = "";
					m.put(IConstants.EST_EMPNO, employeename);
					
					if (expire_date.equalsIgnoreCase("null")|| expire_date == null)
						expire_date = "";
					m.put(IConstants.EST_EXPIRE_DATE, expire_date);
					m.put(IConstants.EST_CUST_CODE, customerCode);
					m.put(IConstants.EST_ITEM, item);
					
					

					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.EST_QTYOR, qty);

					if (uom.equalsIgnoreCase("null")|| uom == null)
				        uom = new ItemMstDAO().getItemUOM(Plant,item);
					m.put(IConstants.EST_UNITMO, uom);

					if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
					unit_price = "0";
					m.put(IConstants.EST_UNITCOST, unit_price);
                                      
                    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
                     currencyID = baseCurrency;
                     m.put(IConstants.EST_CURRENCYID, currencyID);


				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	}
  
  
  @SuppressWarnings("unchecked")
 	public ArrayList downloadKittingSheetData(String Plant, String filename,
 			String sheetName) throws Exception {
 		System.out.println("********  Import Starts *********");

 		// to get file name
 		ArrayList hdrList = new ArrayList();
 		File f = new File(filename);
 		filename = f.getName();
 		f = null;
 		//
 		System.out.println("After extraction filename : " + filename);
 		//
 		filename = DbBean.CountSheetUploadPath + filename;

 		System.out.println("Import File :" + filename);
 		System.out.println("Import sheet :" + sheetName);

 		try {
 			POIReadExcel poi = new POIReadExcel(filename, sheetName);

 			String[] str = poi.readExcelFile(Integer
 					.parseInt(KittingNumberOfColumn),"Kitting");
 			System.out
 					.println("Total number of record found in Excel sheet  :  "
 							+ str.length);

 			for (int j = 1; j < str.length; j++) {

 				String s1 = str[j];
 				String s2 = "";

 				StringTokenizer parser = new StringTokenizer(s1, "|||");

 				System.out.println("Record " + j + "\t: " + s1);

 				List list = new LinkedList(); // Doubly-linked list

 				list = new ArrayList();

 				while (parser.hasMoreTokens()) {
 					list.add(parser.nextToken());

 				}

 				int linecnt = j;
 			        linecnt= linecnt+1;
 				Map m = new HashMap();

 				System.out.println("Total number of record per line  :  "
 						+ list.size());

 				if(list.size() > 0) {
 					
 					String parentprod = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
 					String loc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
 					String parentbatch = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
 				    String childprod = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
 				    String childbatch = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
 				    String qty = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
 					String remarks = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
 					
 				
 					if(StrUtils.isCharAllowed(parentprod) || StrUtils.isCharAllowed(loc) || StrUtils.isCharAllowed(parentbatch) || StrUtils.isCharAllowed(childprod) || StrUtils.isCharAllowed(childbatch) || StrUtils.isCharAllowed(remarks)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
 				    if (("").equals(parentprod)|| ("null").equals(parentprod)){
 				        throw new Exception("Parent Product value is mandatory and  missing at Line "+linecnt+" .");  
 				    }
 				   if (("").equals(loc)|| ("null").equals(loc)){
				        throw new Exception("Location value is mandatory and  missing at Line "+linecnt+" .");  
				    }
 				      if (("").equals(childprod)|| ("null").equals(childprod)){
 				        throw new Exception("Child Prod value is mandatory and  missing at Line "+linecnt+" .");  
 				    }
 				     if (("").equals(childbatch)|| ("null").equals(childbatch)){
 				        throw new Exception("Child Batch value is mandatory and  missing at Line "+linecnt+" .");  
 				    }
 				    
 				    
 				    ImportUtils impUtil = new ImportUtils();           
            	
 					// parentprod field data validation
 					boolean isValidParentData = false;
 					if(ITEM_LEN > parentprod.length()){
 						isValidParentData = true;
 					}
 					if(!isValidParentData){
 						throw new Exception("Parent Product value :  '" + parentprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
 					}
 					
 					boolean isExistParentItem = false;
					ItemUtil itemUtil = new ItemUtil();
					isExistParentItem = itemUtil.isExistsItemMst(parentprod, Plant);
					if(!isExistParentItem){
						throw new Exception("Parent Product '" + parentprod + "' is not created yet  at Line "+linecnt+".");
					}
					
					 /*ItemMstDAO itemMstDAO = new ItemMstDAO();
					 String parentItem = itemMstDAO.getKittingItem(Plant, parentprod);
					 if(parentItem==null){
					    	 throw new Exception(parentprod + " is not a parent product  and  cannot import at Line "+linecnt+" .");  
					  }*/
					 
					 /*ItemUtil _ItemUtil = new ItemUtil();
					 String parentExits = itemUtil.getWMSkittingcheckbatch(Plant, parentprod,loc,parentbatch);
					 if (!parentExits.equalsIgnoreCase("")) {
					    	 throw new Exception("Product:" + parentprod + " Batch:" + parentbatch+ " Loc:" + loc + " already exists at Line "+linecnt+" .");
					  }*/

					
 			
					boolean isValidLocData = false;
					if(LOC_LEN >= loc.length()){
						isValidLocData = true;
					}
					if(!isValidLocData){
						throw new Exception("LOC value '" + loc + "' length has exceeded the maximum value of "+LOC_LEN+" Chars at Line "+linecnt+".");
					}
					
					
					boolean isExistLoc = false;
					LocUtil locUtil = new LocUtil();
					isExistLoc = locUtil.isValidLocInLocmst(Plant, loc);
					if(!isExistLoc){
						throw new Exception("Location '" + loc + "' is not valid location  at Line "+linecnt+".");
					}
					
					boolean isValidBatchData = false;
					if(BATCH_LEN >= parentbatch.length()){
						isValidBatchData = true;
					}
					if(!isValidBatchData){
						throw new Exception("Parent BATCH value :  '" + parentbatch + "' length has exceeded the maximum value of "+BATCH_LEN+" Chars at Line "+linecnt+".");
					}
                 
 					boolean isValidChildData = false;
 					if(ITEM_LEN > childprod.length()){
 						isValidChildData = true;
 					}
 					if(!isValidChildData){
 						throw new Exception("Child Product value :  '" + childprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
 					}
 					
 					if(parentprod.equalsIgnoreCase(childprod)){
							throw new Exception("Child Product cannot be same as Parent Product.Choose diff child product at Line "+linecnt+".");
							
						}
 					
 					boolean isExistItem = false;
					isExistItem = itemUtil.isExistsItemMst(childprod, Plant);
					if(!isExistItem){
						throw new Exception("Child Product '" + childprod + "' is not created yet  at Line "+linecnt+".");
					}
					
					
					 /*String childItem = itemMstDAO.getKittingItem(Plant, childprod);
					 if(childItem!=null){
					    	 throw new Exception(childprod + " is a parent product  and  cannot import at Line "+linecnt+" .");  
					  }*/

 					
 					boolean isValidChildBatchData = false;
					if(BATCH_LEN >= childbatch.length()){
						isValidChildBatchData = true;
					}
					if(!isValidChildBatchData){
						throw new Exception("Child BATCH value :  '" + childbatch + "' length has exceeded the maximum value of "+BATCH_LEN+" Chars at Line "+linecnt+".");
					}
 					
				    Hashtable ht = new Hashtable();
                    ht.put("PLANT",Plant);
                    ht.put("ITEM",childprod);
                    ht.put("LOC",loc);
                    ht.put("USERFLD4",childbatch);
                    InvMstDAO invMstDAO=new InvMstDAO();
                    boolean isInvQty=invMstDAO.isExisitKittingQty(ht,"");
                    if(!isInvQty)
                    {
                  
                    	throw new Exception("Not Enough Inventory For Product '" + childprod + "' with the batch '" + childbatch +"'  at Line "+linecnt+".");
                    }
 					
 					// qtyper field validation
 					boolean isCorrectQtyperFormat = false;
 					if (!qty.trim().equalsIgnoreCase("null") && qty != null){
 						isCorrectQtyperFormat = impUtil.isCorrectQuantityFormat(qty);
 						if(!isCorrectQtyperFormat){
 							throw new Exception("QtyPer value '" + qty + "' is not accepted format at Line "+linecnt+".");
 						}
 						
 					}
 					 								   					
 					// Remarks field data validation
 					boolean isValidRemarks1Data = false;
 					if(WO_REMARK_LEN > remarks.length()){
 						isValidRemarks1Data = true;
 					}
 					if(!isValidRemarks1Data){
 						throw new Exception("Remark value :  '" + remarks + "' length has exceeded the maximum value of "+WO_REMARK_LEN+" Chars at Line "+linecnt+".");
 					}
 					
 					
 					 				
 					m.put("PLANT", Plant);
 					m.put(IDBConstants.PARENTITEM, parentprod);
 					m.put(IDBConstants.LOC, loc);
 					m.put(IDBConstants.PARENT_BATCH, parentbatch);
 				    m.put(IDBConstants.CHILDITEM, childprod);
 				    m.put(IDBConstants.CHILD_BATCH, childbatch);
 				   if (qty.equalsIgnoreCase("null") || qty == null)
 				   {
 					  qty="0";
 				   }
 				    m.put(IDBConstants.QTY, qty);
 				   if (remarks.equalsIgnoreCase("null") || remarks == null)
 				   {
 					  remarks="";
 				   }
 					   
 				    m.put(IDBConstants.REMARKS, remarks);
 				                  
 				
 				}
 				hdrList.add(m);
 				System.out.println("hdrList :: " + hdrList.size());
 			}

 		} catch (Exception ex) {
 			// ex.printStackTrace();
 			throw ex;
 		} finally {

 		}
 		
 		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
 		
 		return hdrList;

 	}   
   
  @SuppressWarnings("unchecked")
  public ArrayList downloadSupplierDiscountSheetData(String Plant, String filename,
                  String sheetName) throws Exception {
          System.out.println("********  Import Starts *********");

          // to get file name
          ArrayList hdrList = new ArrayList();
          File f = new File(filename);
          filename = f.getName();
          f = null;
          //
          System.out.println("After extraction filename : " + filename);
          filename = DbBean.CountSheetUploadPath + filename;

          System.out.println("Import File :" + filename);
          System.out.println("Import sheet :" + sheetName);
          PlantMstDAO plantMstDAO = new PlantMstDAO();
          int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
          try {
                  POIReadExcel poi = new POIReadExcel(filename, sheetName);
                  
                  String[] str = poi.readExcelFile(Integer.parseInt(SupplierDiscountNumberOfColumn),"SupplierDiscount");
                 // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");

                  System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                  for (int j = 1; j < str.length; j++) {

                          String s1 = str[j];
                          
                          StringTokenizer parser = new StringTokenizer(s1, "|||");

                          System.out.println("Record " + j + "\t: " + s1);

                          List list = new LinkedList(); // Doubly-linked list

                          list = new ArrayList();

                          while (parser.hasMoreTokens()) {
                                  list.add(parser.nextToken());

                          }
                      int linecnt =j;
                      linecnt= linecnt+1;
                          Map m = new HashMap();

                          System.out.println("Total number of record per line  :  "
                                          + list.size());
                          boolean isValidItemData = false;
                          if(list.size() > 0) {
                                  
                                  String prd_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                  String supplier_code = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                  String bycost = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                  String bypercentage = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                  
                                  if(StrUtils.isCharAllowed(prd_id) || StrUtils.isCharAllowed(supplier_code)){
              						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
              					}
                                  
                                  if (("").equals(prd_id)|| ("null").equals(prd_id)){
                                      throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
                                  }
                                  
                                 boolean isExistItem = false;
             					 ItemUtil itemUtil = new ItemUtil();
             					 Hashtable htitem= new Hashtable();
             					 htitem.put("PLANT",Plant);
             					 htitem.put("ITEM",prd_id);
             					 htitem.put("ISACTIVE","Y");
             					 isExistItem = itemUtil.isExistsItemMst(htitem);
             					 if(!isExistItem){
             					          throw new Exception("Product ID '" + prd_id + "' is not created/in active at Line "+linecnt+".");
             					 }
                                  
             					if (("").equals(supplier_code)|| ("null").equals(supplier_code)){
            				        throw new Exception("Supplier Code value is mandatory and  missing at Line "+linecnt+" .");  
             					}

            				    boolean isExistVendor = false;
            					VendUtil vendUtil = new VendUtil();
            					isExistVendor = vendUtil.isExistsVendor(supplier_code, Plant);
            					if(!isExistVendor){
            						throw new Exception("Supplier '" + supplier_code + "' is not created yet on system at Line "+linecnt+".");
            					}
            					
            				
            					
            					// unit price field validation
            					ImportUtils impUtil = new ImportUtils();
            					boolean isCorrectbycostFormat = false;
            					
            					if (!bycost.equalsIgnoreCase("null") && bycost != null){
            						isCorrectbycostFormat = impUtil.isCorrectUnitPriceFormat(bycost);
            						if(!isCorrectbycostFormat){
            							throw new Exception("ByCost value '" + bycost + "' is not accepted format at Line "+linecnt+".");
            						}
            					}
            					
            					
            					if (!bycost.equalsIgnoreCase("null") && bycost != null && !bycost.equals("0")){
            						if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null && !bypercentage.equals("0")){
            							throw new Exception("Enter either ByCost or ByPercentage at Line "+linecnt+".");
            						}
            					}
            					
            					if (!bycost.trim().equalsIgnoreCase("null") && bycost != null){
            						if(bycost.contains(".")){
            							int costdecnum = bycost.substring(bycost.indexOf(".")).length() - 1;
            							if(costdecnum>numberOfDecimal)
            							{
            								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Supplier Discount By Cost at Line "+linecnt+".");
            							}
            						}
            						
            					}
            					
            					String isExistCost = "";
             					ItemMstDAO itemMstDAO = new ItemMstDAO();
             					isExistCost = itemMstDAO.getItemCost(Plant, prd_id);
             					if (isExistCost.equalsIgnoreCase("null") || isExistCost == null || isExistCost.equals("0.0")){
             						throw new Exception("Cost is not created on system for the product '" + prd_id + "' at Line "+linecnt+".");
             					}
            					
             					if (!isExistCost.equalsIgnoreCase("null") && isExistCost != null &&  !isExistCost.equals("0.0")){
             						if (!bycost.equalsIgnoreCase("null") && bycost != null && !bycost.equals("0")){
             							Float floatCost=Float.parseFloat(bycost);
             							Float floatisExistCost=Float.parseFloat(isExistCost);
             							if(floatisExistCost<floatCost)
             						 {
             								throw new Exception("ByCost not more than product cost '" + isExistCost + "' at Line "+linecnt+".");
             							}
             							
             							
             						}
             						
             					}
            					
             					boolean isCorrectbypercentageFormat = false;
            					if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null){
            						isCorrectbypercentageFormat = impUtil.isCorrectUnitPriceFormat(bypercentage);
            						if(!isCorrectbypercentageFormat){
            							throw new Exception("ByPercentage value '" + bypercentage + "' is not accepted format at Line "+linecnt+".");
            						}
            					}
            					if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null){
            						//int y = Integer.parseInt(bypercentage);
            						Float y=Float.parseFloat(bypercentage);
            						if(y>100)
            						{
            							throw new Exception("ByPercentage value '" + bypercentage + "' not more than 100 at Line "+linecnt+".");
            						}
            					}
            					
            					if(bypercentage.equalsIgnoreCase("null") && bycost.equalsIgnoreCase("null")) {
            						throw new Exception("Please enter supplier discount value at Line "+linecnt+".");
            					}
            					  m.put(IDBConstants.PLANT, Plant);
                                  m.put(IDBConstants.ITEM, prd_id);
                                  m.put(IDBConstants.VENDORCODE, supplier_code);
                                  String ibdiscount="";
                                  if (!bycost.equalsIgnoreCase("null") && bycost != null && !bycost.equals("0")){
                             	   ibdiscount=bycost;
                             	    m.put(IDBConstants.BYCOST, bycost);
                             	   m.put(IDBConstants.BYPERCENTAGE, "");
                                  }
                                  else if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null && !bypercentage.equals("0")){
                               	     ibdiscount=bypercentage+"%";
                                     m.put(IDBConstants.BYCOST, "");
                               	     m.put(IDBConstants.BYPERCENTAGE, bypercentage);
                                  }  
                                  else
                                  {
                                 	 ibdiscount="0";
                                 	 //bycost="";
                                     //bypercentage="";
                                 	
                                 			
                                  }
                                  m.put(IDBConstants.IBDISCOUNT, ibdiscount);
                               
                          }

                          hdrList.add(m);

                          System.out.println("hdrList :: " + hdrList.size());
                  }

          } catch (Exception ex) {
                  throw ex;
          } finally {

          }
          
        //Validate Order azees - 12/2020
  		Map mv = new HashMap();
          mv.put("ValidNumber", "");
          hdrList.add(mv);
          
          return hdrList;

  }
  
  @SuppressWarnings("unchecked")
  public ArrayList downloadCustomerDiscountSheetData(String Plant, String filename,
                  String sheetName) throws Exception {
          System.out.println("********  Import Starts *********");

          // to get file name
          ArrayList hdrList = new ArrayList();
          File f = new File(filename);
          filename = f.getName();
          f = null;
          //
          System.out.println("After extraction filename : " + filename);
          filename = DbBean.CountSheetUploadPath + filename;

          System.out.println("Import File :" + filename);
          System.out.println("Import sheet :" + sheetName);
          PlantMstDAO plantMstDAO = new PlantMstDAO();
          int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
          try {
                  POIReadExcel poi = new POIReadExcel(filename, sheetName);
                  
                  String[] str = poi.readExcelFile(Integer.parseInt(CustomerDiscountNumberOfColumn),"CustomerDiscount");
                 // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");

                  System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                  for (int j = 1; j < str.length; j++) {

                          String s1 = str[j];
                          
                          StringTokenizer parser = new StringTokenizer(s1, "|||");

                          System.out.println("Record " + j + "\t: " + s1);

                          List list = new LinkedList(); // Doubly-linked list

                          list = new ArrayList();

                          while (parser.hasMoreTokens()) {
                                  list.add(parser.nextToken());

                          }
                      int linecnt =j;
                      linecnt= linecnt+1;
                          Map m = new HashMap();

                          System.out.println("Total number of record per line  :  "
                                          + list.size());
                          boolean isValidItemData = false;
                          if(list.size() > 0) {
                                  
                                  String prd_id = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                  String customer_type_id = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                  String byprice = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                  String bypercentage = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                  
                                  if(StrUtils.isCharAllowed(prd_id) || StrUtils.isCharAllowed(customer_type_id)){
                                	  throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
          						  }
                                  
                                  if (("").equals(prd_id)|| ("null").equals(prd_id)){
                                      throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");
                                  }
                                 
                                  
                                 boolean isExistItem = false;
             					 ItemUtil itemUtil = new ItemUtil();
             					 Hashtable htitem= new Hashtable();
             					 htitem.put("PLANT",Plant);
             					 htitem.put("ITEM",prd_id);
             					 htitem.put("ISACTIVE","Y");
             					 isExistItem = itemUtil.isExistsItemMst(htitem);
             					 if(!isExistItem){
             					          throw new Exception("Product ID '" + prd_id + "' is not created/in active at Line "+linecnt+".");
             					 }
                                 
             					 if (("").equals(customer_type_id)|| ("null").equals(customer_type_id)){
               				        throw new Exception("Customer Type Id value is mandatory and  missing at Line "+linecnt+" .");  
               				      }
             					
             				// customer validation
             					Hashtable ht = new Hashtable();
             					ht.put(IConstants.PLANT, Plant);
                    			ht.put(IConstants.CUSTOMERTYPEID, customer_type_id);
             					 boolean isExists = new CustomerBeanDAO().isExistsCustomerType(ht);
 								if (!customer_type_id.trim().equalsIgnoreCase("null") && customer_type_id != null){
                                 if (!isExists) {
                                    throw new Exception("Customer Type  : '" + customer_type_id + "' is not created at Line "+linecnt+".");
                                 }
                                 }
            								 				
            					ImportUtils impUtil = new ImportUtils();
            					boolean isCorrectbypriceFormat = false;
        	   					if (!byprice.equalsIgnoreCase("null") && byprice != null){
            						isCorrectbypriceFormat = impUtil.isCorrectUnitPriceFormat(byprice);
            						if(!isCorrectbypriceFormat){
            							throw new Exception("ByPrice value '" + byprice + "' is not accepted format at Line "+linecnt+".");
            						}
            					}
        	   					
        	   					if (!byprice.equalsIgnoreCase("null") && byprice != null && !byprice.equals("0")){
            						if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null && !bypercentage.equals("0")){
            							throw new Exception("Enter either ByPrice or ByPercentage at Line "+linecnt+".");
            						}
            					}
        	   					if (!byprice.trim().equalsIgnoreCase("null") && byprice != null){
            						if(byprice.contains(".")){
            							int pricedecnum = byprice.substring(byprice.indexOf(".")).length() - 1;
            							if(pricedecnum>numberOfDecimal)
            							{
            								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Customer Discount By Price at Line "+linecnt+".");
            							}
            						}
            						
            					}
        	   					String isExistPrice = "";
             					ItemMstDAO itemMstDAO = new ItemMstDAO();
             					isExistPrice = itemMstDAO.getItemPrice(Plant, prd_id);
             					if (isExistPrice.equalsIgnoreCase("null") ||isExistPrice == null || isExistPrice.equals("0.0")){
             						throw new Exception("Price is not created on system for the product '" + prd_id + "' at Line "+linecnt+".");
             					}
            					
             					if (!isExistPrice.equalsIgnoreCase("null") && isExistPrice != null &&  !isExistPrice.equals("0.0")){
             						if (!byprice.equalsIgnoreCase("null") && byprice != null && !byprice.equals("0")){
             							Float floatPrice=Float.parseFloat(byprice);
             							Float floatisExistPrice=Float.parseFloat(isExistPrice);
             							if(floatisExistPrice<floatPrice)
             						 {
             								throw new Exception("ByPrice not more than product price '" + isExistPrice + "' at Line "+linecnt+".");
             							}
             						
             						}
             						
             					}
            		
             					String isExistMinPrice = itemMstDAO.getItemMinPrice(Plant, prd_id);
             					if (!isExistMinPrice.equalsIgnoreCase("null") && isExistMinPrice != null &&  !isExistMinPrice.equals("0.0")){
             						if (!byprice.equalsIgnoreCase("null") && byprice != null && !byprice.equals("0")){
             							Float floatPrice=Float.parseFloat(byprice);
             							Float floatisExistMinPrice=Float.parseFloat(isExistMinPrice);
             							if(floatPrice<floatisExistMinPrice)
             						 {
             								throw new Exception("ByPrice not less than product Min Selling Price '" + isExistMinPrice + "' at Line "+linecnt+".");
             							}
             						
             						}
             						
             					}
             		
            					boolean isCorrectbypercentageFormat = false;
            					if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null){
            						isCorrectbypercentageFormat = impUtil.isCorrectUnitPriceFormat(bypercentage);
            						if(!isCorrectbypercentageFormat){
            							throw new Exception("ByPercentage value '" + bypercentage + "' is not accepted format at Line "+linecnt+".");
            						}
            					}
            					
            					if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null){
            						Float y=Float.parseFloat(bypercentage);
            						if(y>100)
            						{
            							throw new Exception("ByPercentage value '" + bypercentage + "' not more than 100 at Line "+linecnt+".");
            						}
            					}
            					
            					if(bypercentage.equalsIgnoreCase("null") && byprice.equalsIgnoreCase("null")) {
            						throw new Exception("Please enter customer discount value at Line "+linecnt+".");
            					}
            				      m.put(IDBConstants.PLANT, Plant);
                                  m.put(IDBConstants.ITEM, prd_id);
                                  m.put(IDBConstants.CUSTOMERTYPEID, customer_type_id);
                                  String obdiscount="";
                                  if (!byprice.equalsIgnoreCase("null") && byprice != null && !byprice.equals("0")){
                             	   obdiscount=byprice;
                             	    m.put(IDBConstants.BYPRICE, byprice);
                             	   m.put(IDBConstants.BYPERCENTAGE, "");
                                  }
                                  else if (!bypercentage.equalsIgnoreCase("null") && bypercentage != null && !bypercentage.equals("0")){
                               	     obdiscount=bypercentage+"%";
                                     m.put(IDBConstants.BYPRICE, "");
                               	     m.put(IDBConstants.BYPERCENTAGE, bypercentage);
                                  }  
                                  else
                                  {
                                 	 obdiscount="0";
                                 
                                  }
                                  m.put(IDBConstants.OBDISCOUNT, obdiscount);
                          }
                          
                          hdrList.add(m);

                          System.out.println("hdrList :: " + hdrList.size());
                  }

          } catch (Exception ex) {
                  throw ex;
          } finally {

          }
          
        //Validate Order azees - 12/2020
  		Map mv = new HashMap();
          mv.put("ValidNumber", "");
          hdrList.add(mv);
          
          return hdrList;

  }

	public ArrayList downloadKitBOMSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		//
		System.out.println("After extraction filename : " + filename);
		//
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);
	    
			String[] str = poi.readExcelFile(Integer.parseInt(KitBOMNumberOfColumn),"KitBOM");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String parentprod = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
				    String childprod = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
				    String equivalentprod = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String childqty = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					
					if(StrUtils.isCharAllowed(parentprod) || StrUtils.isCharAllowed(childprod) || StrUtils.isCharAllowed(equivalentprod) || StrUtils.isCharAllowed(childqty)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
					
					if(equivalentprod.equals("null"))
					{
						equivalentprod = "";
					}
						
				
				    if (("").equals(parentprod)|| ("null").equals(parentprod)){
				        throw new Exception("Parent Product value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(childprod)|| ("null").equals(childprod)){
				        throw new Exception("Child Prod value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				     if (("").equals(childqty)|| ("null").equals(childqty)){
				        throw new Exception("Child Qty value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				   
				    
				    ImportUtils impUtil = new ImportUtils();           
         	
					// parentprod field data validation
					boolean isValidParentData = false;
					if(ITEM_LEN > parentprod.length()){
						isValidParentData = true;
					}
					if(!isValidParentData){
						throw new Exception("Parent Product value :  '" + parentprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
					
					// childprod field data validation
					boolean isValidChildData = false;
					if(ITEM_LEN > childprod.length()){
						isValidChildData = true;
					}
					if(!isValidChildData){
						throw new Exception("Child Product value :  '" + childprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
					
					// equivalent field data validation
					boolean isValidEquivalentData = false;
					if(ITEM_LEN > equivalentprod.length()){
						isValidEquivalentData = true;
					}
					if(!isValidEquivalentData){
						throw new Exception("Equivalent Product value :  '" +equivalentprod + "' length has exceeded the maximum value of "+ITEM_LEN+" Chars.");
					}
                    
					
					// child qty field validation
					boolean isCorrectChildQtyFormat = false;
					if (!childqty.trim().equalsIgnoreCase("null") && childqty != null){
						isCorrectChildQtyFormat = impUtil.isCorrectQuantityFormat(childqty);
						if(!isCorrectChildQtyFormat){
							throw new Exception("Child Qty value '" + childqty + "' is not accepted format at Line "+linecnt+".");
						}
						
					}
				
				
					// validation against database entries
					 // item validation
					ItemMstDAO _ItemMstDAO =new ItemMstDAO();
					ProductionBomDAO _ProductionBomDAO =new ProductionBomDAO();
					
					Hashtable htPItemMst= new Hashtable();
					htPItemMst.clear();
					htPItemMst.put(IDBConstants.PLANT, Plant);
					htPItemMst.put(IDBConstants.ITEM, parentprod);
					
					 boolean isExistParentItem = false;
					 isExistParentItem = _ItemMstDAO.isExisit(htPItemMst, "");
					 if(!isExistParentItem){
					         throw new Exception("Parent Product : '" + parentprod + "' is not Valid at Line "+linecnt+".");
					 }
					 
					 Hashtable htCItemMst= new Hashtable();
					 htCItemMst.clear();
					 htCItemMst.put(IDBConstants.PLANT, Plant);
					 htCItemMst.put(IDBConstants.ITEM, childprod);
					
					 boolean isExistChildItem = false;
					 isExistChildItem = _ItemMstDAO.isExisit(htCItemMst, "");
					 if(!isExistChildItem){
					         throw new Exception("Child Product : '" + childprod + "' is not Valid at Line "+linecnt+".");
					 }
					 
					 Hashtable htBomExistChildItem= new Hashtable();
					 htBomExistChildItem.clear();
					 htBomExistChildItem.put(IDBConstants.PLANT, Plant);
					 htBomExistChildItem.put("PITEM", parentprod);
					 htBomExistChildItem.put("CITEM", childprod);
					 htBomExistChildItem.put("BOMTYPE", "KIT");
					 boolean isBOMExistChildItem = false;
					 isBOMExistChildItem = _ProductionBomDAO.isExists(htBomExistChildItem);
					 if(isBOMExistChildItem){
					         throw new Exception("Child Product : '" + childprod + "'  already exists for Parent product '" + parentprod + "' at Line "+linecnt+".");
					 }
					 if(equivalentprod.length()>0)
					 {
						 if(!equivalentprod.equals("null"))
						 {
							 Hashtable htEItemMst= new Hashtable();
							 htEItemMst.clear();
							 htEItemMst.put(IDBConstants.PLANT, Plant);
							 htEItemMst.put(IDBConstants.ITEM, equivalentprod);
					
							 boolean isExistEquivalentItem = false;
							 isExistEquivalentItem = _ItemMstDAO.isExisit(htEItemMst, "");
							 if(!isExistEquivalentItem){
								 throw new Exception("Equivalent Product : '" + equivalentprod + "' is not Valid at Line "+linecnt+".");
							 }
						 }	
					 }
						
					if(parentprod.equalsIgnoreCase(childprod)){
							throw new Exception("Child Product cannot be same as Parent Product. Choose diff child product at Line "+linecnt+".");
							
						}
					
					if(parentprod.equalsIgnoreCase(equivalentprod)){
						throw new Exception("Equivalent Product cannot be same as Parent Product. Choose diff equivalent product at Line "+linecnt+".");
						
					}
					
					if(childprod.equalsIgnoreCase(equivalentprod)){
						throw new Exception("Equivalent Product cannot be same as Child Product. Choose diff equivalent product at Line "+linecnt+".");
						
					}
					
					m.put("PLANT", Plant);
					m.put(IDBConstants.PARENTITEM, parentprod);
				    m.put(IDBConstants.CHILDITEM, childprod);
				    m.put(IDBConstants.EQUIVALENTITEM, equivalentprod);
				    m.put(IDBConstants.QTY, childqty);
								
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	} 
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadOutboundProdRemarksSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		DOUtil doUtil = new DOUtil();
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(OutboundProdRemarksNumberOfColumn),"OutboundProdRemarks");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
				    String doLno = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
				   	String item = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String remarks = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					
				    if(StrUtils.isCharAllowed(dono) || StrUtils.isCharAllowed(doLno) || StrUtils.isCharAllowed(remarks)  || StrUtils.isCharAllowed(item) ){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(dono)|| ("null").equals(dono)){
				        throw new Exception("DONO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(doLno)|| ("null").equals(doLno)){
				        throw new Exception("DOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
                          
				
					// DONO field data validation
					boolean isValidDonoData = false;
					if(DONO_LEN >= dono.length()){
						isValidDonoData = true;
					}
					if(!isValidDonoData){
						throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
					}
                                        
				    // is valid Line No
				    
				     int isThisANumber = 0;
				     try
				     {
				     isThisANumber = Integer.parseInt(doLno); 
				     }
				     catch (NumberFormatException nfe)
				     {
				         throw new Exception("DOLNNO value :  '" + doLno + "' for DONO "+dono+" is not a valid number  at Line "+linecnt+".");
				     }

				   	// Remarks1 field data validation
					boolean isValidRemarksData1 = false;
					if(DO_REMARK_LEN1 > remarks.length()){
						isValidRemarksData1 = true;
					}
					if(!isValidRemarksData1){
						throw new Exception("Remarks value :  '" + remarks + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
					}
					
					// validation against database entries
					// DONO number validation with status
					boolean isexistDoNo = false;
					isexistDoNo = doUtil.isExistDONO(dono,Plant);
                    if(!isexistDoNo){
                            throw new Exception("DONO '" + dono + "' is not valid or not created in the system at Line "+linecnt+" ."); 
                    }
					 
					 // item validation
					boolean isExistdata = false;
					DoDetDAO _DoDetDAO = new DoDetDAO();
					 Hashtable htdata= new Hashtable();
					 htdata.put("PLANT",Plant);
					 htdata.put("DONO",dono);
					 htdata.put("DOLNNO",doLno);
					 htdata.put("ITEM",item);
					 isExistdata = _DoDetDAO.isExisit(htdata);
					 if(!isExistdata){
					          throw new Exception("Not a valid Order Line No '" + doLno + "'  and Item '" +item+ "' for the Sales Order '" +dono+ "' at Line "+linecnt+".");
					 }
					 					
					m.put("PLANT", Plant);
					m.put(IConstants.OUT_DONO, dono);
				    m.put(IConstants.OUT_DOLNNO, doLno);
			        m.put(IConstants.OUT_ITEM, item);
				    
				    if (remarks.equalsIgnoreCase("null")
							|| remarks == null)
						 remarks = "";
					m.put("REMARKS", remarks);
					
					
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", "");
        hdrList.add(mv);
		
		return hdrList;

	}	
	@SuppressWarnings("unchecked")
	public ArrayList downloadInboundProdRemarksSheetData(String Plant, String filename,
			String sheetName) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		POUtil poUtil = new POUtil();
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(InboundProdRemarksNumberOfColumn),"InboundProdRemarks");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String pono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
				    String poLno = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
				   	String item = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String remarks = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					
				    if(StrUtils.isCharAllowed(pono) || StrUtils.isCharAllowed(poLno) || StrUtils.isCharAllowed(remarks)  || StrUtils.isCharAllowed(item) ){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(pono)|| ("null").equals(pono)){
				        throw new Exception("PONO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(poLno)|| ("null").equals(poLno)){
				        throw new Exception("POLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
                          
				
					// DONO field data validation
					boolean isValidPonoData = false;
					if(DONO_LEN >= pono.length()){
						isValidPonoData = true;
					}
					if(!isValidPonoData){
						throw new Exception("PONO value :  '" + pono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
					}
                                        
				    // is valid Line No
				    
				     int isThisANumber = 0;
				     try
				     {
				     isThisANumber = Integer.parseInt(poLno); 
				     }
				     catch (NumberFormatException nfe)
				     {
				         throw new Exception("POLNNO value :  '" + poLno + "' for PONO "+pono+" is not a valid number  at Line "+linecnt+".");
				     }
				     boolean isValidLineNumber    = false;
		       			if(LNNO_LEN >= poLno.length()){
		       				isValidLineNumber = true;
		                 }
		                 if(!isValidLineNumber){
		                         throw new Exception("POLNNO value '" + poLno + "' length has exceeded the maximum value of "+LNNO_LEN+" Chars at Line "+linecnt+".");
		                 }

				   	// Remarks1 field data validation
					boolean isValidRemarksData1 = false;
					if(DO_REMARK_LEN1 >= remarks.length()){
						isValidRemarksData1 = true;
					}
					if(!isValidRemarksData1){
						throw new Exception("Remarks value :  '" + remarks + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
					}
					
					// validation against database entries
					// DONO number validation with status
					boolean isexistPoNo = false;
					isexistPoNo = poUtil.isExistPONO(pono,Plant);
                    if(!isexistPoNo){
                            throw new Exception("PONO '" + pono + "' is not valid or not created in the system at Line "+linecnt+" ."); 
                    }
					 
					 // item validation
					boolean isExistdata = false;
					PoDetDAO _PoDetDAO = new PoDetDAO();
					 Hashtable htdata= new Hashtable();
					 htdata.put("PLANT",Plant);
					 htdata.put("PONO",pono);
					 htdata.put("POLNNO",poLno);
					 htdata.put("ITEM",item);
					 isExistdata = _PoDetDAO.isExisit(htdata);
					 if(!isExistdata){
					          throw new Exception("Not a valid Order Line No '" + poLno + "'  and Item '" +item+ "' for the Purchase Order '" +pono+ "' at Line "+linecnt+".");
					 }
					 					
					m.put("PLANT", Plant);
					m.put(IConstants.IN_PONO, pono);
				    m.put(IConstants.IN_POLNNO, poLno);
			        m.put(IConstants.IN_ITEM, item);
				    
				    if (remarks.equalsIgnoreCase("null")
							|| remarks == null)
						 remarks = "";
					m.put("REMARKS", remarks);
					
					
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		//Map mv = new HashMap();
        //mv.put("ValidNumber", "");
        //hdrList.add(mv);
		
		return hdrList;

	}
	
	
	
    @SuppressWarnings("unchecked")
    public ArrayList downloadEmployeeleavedetSheetData(String Plant, String filename,
                    String sheetName,String Region) throws Exception {
            System.out.println("********  Import Starts *********");
            EmployeeUtil custUtil = new EmployeeUtil();
            HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(EmployeeLeaveDetNumberOfColumn),"Emplvt");
                   // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                            	
                            	ArrayList arrCust = custUtil.getEmployeeList(StrUtils.replaceStr2Char(((String) list.get(0)).trim()),Plant,"");
                                
                                	Map ma=(Map)arrCust.get(0);
                                	String empid = (String)ma.get("ID");
                                	String emptypeid = (String)ma.get("EMPLOYEETYPEID");
                                	int lvtid = hrLeaveTypeDAO.getleavettypeidbyname(Plant, StrUtils.replaceStr2Char(((String) list.get(1)).trim()), Integer.valueOf(emptypeid));
                     
                                    String empno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String leavetype = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String tdays =  StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    String lyear = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
                                    String notes = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
                                    
                                   if(StrUtils.isCharAllowed(empno) || StrUtils.isCharAllowed(leavetype) || StrUtils.isCharAllowed(lyear) || StrUtils.isCharAllowed(notes)){
                                	    throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                                   }
                                   
                                   if(isnumber(tdays)){
                                	   if(!isaday(tdays)) {
                                		   throw new Exception("**.0 or **.5 only allowed at line number"+linecnt+".");
                                	   }
                                   }else {
                                	   throw new Exception("The Numbes and . only allowed at line number"+linecnt+".");
                                   }

                                   if (("").equals(empno) || ("null").equals(empno)){
                                        throw new Exception(" value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   
                                   if (("").equals(leavetype) || ("null").equals(leavetype)){
                                       throw new Exception("Leave Type value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   
                                   if (("").equals(tdays) || ("null").equals(tdays)){
                                       throw new Exception("Total Enilement value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   
                                   if (("").equals(lyear) || ("null").equals(lyear)){
                                       throw new Exception("Leave year value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   
                                   if (lvtid == 0){
                                       throw new Exception("Leave Type is wrong at Line "+linecnt+" .");  
                                   }
                                   								
                                   boolean isValidData = false;
                                   if(50 >= empno.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Employee Id value : '" + empno + "' length has exceeded the maximum value of 50 Chars at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   if(50 >= leavetype.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Leave type value : '" + leavetype + "' length has exceeded the maximum value of 50 Chars at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   if(10 >= tdays.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Total Entitlement value : '" + tdays + "' length has exceeded the maximum value of 10 Chars at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   if(4 >= lyear.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Leave year value : '" + lyear + "' length has exceeded the maximum value of 4 Chars at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   if(1000 >= notes.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Notes value : '" + notes + "' length has exceeded the maximum value of 1000 Chars at Line "+linecnt+".");
                                   }
                                   
                                
                                    m.put("PLANT", Plant);
                                    m.put("EMPNO", empno);
                                    m.put("EMPID", empid);
                                    m.put("LEAVETYPE", leavetype);
                                    m.put("LEAVETYPEID", String.valueOf(lvtid));
                                    m.put("TDAYS", tdays);
                                    m.put("LYEAR", lyear);
                                    m.put("NOTES", notes);
                                     
                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    @SuppressWarnings("unchecked")
    public ArrayList downloadEmployeeSalarydetSheetData(String Plant, String filename,
                    String sheetName,String Region) throws Exception {
            System.out.println("********  Import Starts *********");
            EmployeeUtil custUtil = new EmployeeUtil();
            HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
            HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
            ImportUtils impUtil = new ImportUtils();
            PlantMstDAO plantMstDAO = new PlantMstDAO();
            int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
            
            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(EmployeeSalaryDetNumberOfColumn),"EmpSalary");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                            	
                            	ArrayList arrCust = custUtil.getEmployeeList(StrUtils.replaceStr2Char(((String) list.get(0)).trim()),Plant,"");
                                if(arrCust.isEmpty())
                                {
                                	throw new Exception("Invalid Employee value "+(String) list.get(0)+" at Line "+linecnt+" .");
                                }
                                else {
                                	Map ma=(Map)arrCust.get(0);
                                	String empid = (String)ma.get("ID");
                                	String emptypeid = (String)ma.get("EMPLOYEETYPEID");
                                	
                                    String empno = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String salarytype = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String salary =  StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                   if(StrUtils.isCharAllowed(empno) || StrUtils.isCharAllowed(salarytype) || StrUtils.isCharAllowed(salary)){
                                	    throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
                                   }

                                   if (("").equals(empno) || ("null").equals(empno)){
                                        throw new Exception("Employee value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   
                                   if (("").equals(salarytype) || ("null").equals(salarytype)){
                                       throw new Exception("Salary Type value is mandatory and  missing at Line "+linecnt+" .");  
                                   }
                                   								
                                   boolean isValidData = false;
                                   if(50 >= empno.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Employee Id value : '" + empno + "' length has exceeded the maximum value of 50 Chars at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   if(100 >= salarytype.length()){
                                       isValidData = true;
                                   }
                                   if(!isValidData){
                                       throw new Exception("Salary type value : '" + salarytype + "' length has exceeded the maximum value of 100 Chars at Line "+linecnt+".");
                                   }
                                   isValidData = false;
                                   isValidData = hrEmpSalaryDAO.IsSalaryExists(Plant, salarytype);
                                   if(!isValidData){
                                       throw new Exception("Salary type value : '" + salarytype + "'  is not created yet on system. at Line "+linecnt+".");
                                   }
                                   
                                   isValidData = false;
                                   isValidData = hrEmpSalaryDetDAO.IsEmpSalarydet(Plant, salarytype,Integer.valueOf(empid));
                                   if(isValidData){
                                       throw new Exception("Salary type value : '" + salarytype + "'  is already exists for Employee "+empno+" at Line "+linecnt+".");
                                   }
                                   
                                   boolean isCorrectCostFormat = false;
               					if (!salary.trim().equalsIgnoreCase("null") && salary != null){
               						isCorrectCostFormat = impUtil.isCorrectUnitPriceFormat(salary);
               						if(!isCorrectCostFormat){
               							throw new Exception("Salary value '" + salary + "' is not accepted format at Line "+linecnt+".");
               						}
               					}
               					else
               					{
               						salary = "0";
               					}
               					
               					if (!salary.trim().equalsIgnoreCase("null") && salary != null){
               						if(salary.contains(".")){
               							int costdecnum = salary.substring(salary.indexOf(".")).length() - 1;
               							if(costdecnum>numberOfDecimal)
               							{
               								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Salary at Line "+linecnt+".");
               							}
               						}
               						
               					}  
                                
                                    m.put("PLANT", Plant);
                                    m.put("EMPNO", empno);
                                    m.put("EMPID", empid);
                                    m.put("SALARYTYPE", salarytype);
                                    m.put("SALARY", salary);
                                     
                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
          //Validate Order azees - 07/2022
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    @SuppressWarnings("unchecked")
    public ArrayList downloadHolidayMstSheetData(String Plant, String filename,
                    String sheetName,String Region) throws Exception {
            System.out.println("********  Import Starts *********");
            HrHolidayMstDAO HrHolidayMstDAO = new HrHolidayMstDAO();
            encryptBean eb = new encryptBean();
            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(HolidayMstNumberOfColumn),"Hmst");
                   // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                            int linecnt =j;
                            linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
	                            	boolean datecheck = HrHolidayMstDAO.IsHoliday(Plant, StrUtils.replaceStr2Char(((String) list.get(0)).trim()));
	                            	if(datecheck) {
	                            		continue;
	                            	}
                            	
                                    String hdate = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    String hdesc = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                   

                                    
                                if (("").equals(hdate) || ("null").equals(hdate)){
                                    throw new Exception("Holiday Date value is mandatory and  missing at Line "+linecnt+" .");  
                                }else {
                                	boolean dateval = isValidFormat("dd/MM/yyyy", hdate, Locale.ENGLISH);
                                	if(!dateval) {
                                		throw new Exception("Holiday Date '"+hdate+"' format is wrong at Line "+linecnt+" .");
                                	}
                                }
                                
                                
                                                                        
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put(IDBConstants.HOLIDAY_DATE, hdate);
                                    m.put(IDBConstants.HOLIDAY_DESC, hdesc);

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    @SuppressWarnings("unchecked")
    public ArrayList downloadSalaryMstSheetData(String Plant, String filename,
                    String sheetName,String Region) throws Exception {
            System.out.println("********  Import Starts *********");
            HrEmpSalaryDAO hrEmpSalaryDAO = new HrEmpSalaryDAO();
            encryptBean eb = new encryptBean();
            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(SalaryMstNumberOfColumn),"Salarymst");

                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                            int linecnt =j;
                            linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {
                            	
                            	String salarytype = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                            	
                            	if (("").equals(salarytype) || ("null").equals(salarytype)){
                                    throw new Exception("Salary Type value is mandatory and  missing at Line "+linecnt+" .");  
                                }
                            	
                            	boolean isValidData = false;
                                if(100 >= salarytype.length()){
                                    isValidData = true;
                                }
                                if(!isValidData){
                                    throw new Exception("Salary Type value : '" + salarytype + "' length has exceeded the maximum value of 100 Chars at Line "+linecnt+".");
                                }
                            	
	                            	boolean datecheck = hrEmpSalaryDAO.IsSalaryExists(Plant, salarytype);
	                            	if(datecheck) {
	                            		throw new Exception("Salary Type value : '" + salarytype + "'  Already Exists at Line "+linecnt+" .");	
	                            	}
                            	                                                                        
                                    m.put(IDBConstants.PLANT, Plant);
                                    m.put("SALARYTYPE", salarytype);

                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    }
    
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }
    
    public boolean isnumber(String tdays) {
    	 boolean numeric = true;
         try {
             Double num = Double.parseDouble(tdays);
         } catch (NumberFormatException e) {
             numeric = false;
         }
		return numeric;
    }
    
    public boolean isaday(String tdays) {
   	 boolean numeric = true;
   	 double dtdays = Double.valueOf(tdays);
   	 tdays = String.valueOf(dtdays);
   	 String result[] = tdays.split("\\.");
   	 if(result.length == 2) {
   		String returnValue = result[result.length - 1];
      	 if(returnValue.equalsIgnoreCase("0") || returnValue.equalsIgnoreCase("5")) {
      		numeric = true;
      	 }else {
      		numeric = false;
      	 }
   	 }else {
   		numeric = true;
   	 }
   	 
	 return numeric;
   }
    
    public boolean checkempcode(String empcode){
    	char ch1 = empcode.charAt(0);
    	if(ch1 == 'E') {
    		String empvalue = empcode.substring(1);
    		if(StringUtils.isNumeric(empvalue)) {
    			return true;
    		}else {
    			return false;
    		}
    	}else {
    		return false;
    	}
    }
    
    
    @SuppressWarnings("unchecked")
    public ArrayList downloadEmployeeLeaveApplySheetData(String Plant, String filename,
                    String sheetName,String Region) throws Exception {
            System.out.println("********  Import Starts *********");
            HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
            EmployeeDAO employeeDAO = new EmployeeDAO();
            HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
            EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
            DateUtils dateutils = new DateUtils();
            String curtyear = dateutils.getYear();

            // to get file name
            ArrayList hdrList = new ArrayList();
            File f = new File(filename);
            filename = f.getName();
            f = null;
            //
            System.out.println("After extraction filename : " + filename);
            filename = DbBean.CountSheetUploadPath + filename;

            System.out.println("Import File :" + filename);
            System.out.println("Import sheet :" + sheetName);

            try {
            		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
            	    String ispay = _PlantMstDAO.getispayroll(Plant);
                    POIReadExcel poi = new POIReadExcel(filename, sheetName);
                    
                    String[] str = poi.readExcelFile(Integer.parseInt(EmployeeLeaveApplyNumberOfColumn),"EmpLeaveApply");
                   // String[] str = poi.readExcelFile(Integer.parseInt(LoanAssigneeNumberOfColumn),"Cust");
                    
                    System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

                    for (int j = 1; j < str.length; j++) {

                            String s1 = str[j];
                            
                            StringTokenizer parser = new StringTokenizer(s1, "|||");

                            System.out.println("Record " + j + "\t: " + s1);

                            List list = new LinkedList(); // Doubly-linked list

                            list = new ArrayList();

                            while (parser.hasMoreTokens()) {
                                    list.add(parser.nextToken());

                            }
                        int linecnt =j;
                        linecnt= linecnt+1;
                            Map m = new HashMap();

                            System.out.println("Total number of record per line  :  "
                                            + list.size());

                            if(list.size() > 0) {

                                    String empno 	 = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
                                    
	                                    int empid = 0;
	                                    if(StrUtils.isCharAllowed(empno)){
	                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
	                					}
	                                    if (("").equals(empno) || ("null").equals(empno)){
	                                        throw new Exception("Employee Id value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	
	                                    	  boolean isValidData = false;
	                                          if(CUSTNO_LEN >= empno.length()){
	                                                  isValidData = true;
	                                          }
	                                          if(!isValidData){
	                                                  throw new Exception("Employee Id value : '" + empno + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
	                                          }
	                                    	
	                                    	String getempid = employeeDAO.getEmpid(Plant, empno, "");
	                                    	if(getempid.equalsIgnoreCase("")) {
	                                    		throw new Exception("Please enter valid Employee Id at Line "+linecnt+" .");
	                                    	}else {
	                                    		empid = Integer.valueOf(getempid);
	                                    	}
	                                    }
	                                    
	                                    ArrayList empmst = employeeDAO.getEmployeeListbyid(String.valueOf(empid),Plant);
	                                 	Map empmstmap=(Map)empmst.get(0);
	                                 	
	                                  	String EmpName   = (String)empmstmap.get("FNAME");
	                                 	String EmployeeTypeId = (String)empmstmap.get("EMPLOYEETYPEID");
	                                 	String repid = (String)empmstmap.get("REPORTING_INCHARGE");
                                 	
                                    String firstname = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
                                    String leavetype = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
                                    
	                                    if(StrUtils.isCharAllowed(leavetype)){
	                						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
	                					}
	                                    if (("").equals(leavetype) || ("null").equals(leavetype)){
	                                        throw new Exception("Leave Type value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	
	                                    	  boolean isValidData = false;
	                                          if(CUSTNO_LEN >= leavetype.length()){
	                                                  isValidData = true;
	                                          }
	                                          if(!isValidData){
	                                                  throw new Exception("Leave Type value : '" + leavetype + "' length has exceeded the maximum value of "+CUSTNO_LEN+" Chars at Line "+linecnt+".");
	                                          }
	                                    	
	                                    	if(EmployeeTypeId.equalsIgnoreCase("0")) {
	                                    		throw new Exception("Please enter valid Leave Type at Line "+linecnt+" .");
	                                    	}
	                                    }
	                                    
	                                    int lvtid = 0;
	                                    short islop = 0;
	                                    List<HrLeaveType> leavetypelist = hrLeaveTypeDAO.IsLeavetypelist(Plant, leavetype, Integer.valueOf(EmployeeTypeId));
	                                    if(!leavetypelist.isEmpty()) {
	                                    	 HrLeaveType leavetypedata = leavetypelist.get(0);
	                                    	 lvtid = leavetypedata.getID();
	                                    	 islop = leavetypedata.getISNOPAYLEAVE();
	                                    }
	                                    
	                                    double leavebalance = 0.0;
	                                    int empdetid =0;
	                                    if(lvtid != 0) {
	                                    	List<EmployeeLeaveDET> empleavedetlist = employeeLeaveDetDAO.IsEmployeeLeavedetlist(Plant, lvtid, empid, curtyear);
	                                    	if(!empleavedetlist.isEmpty()) {
	                                    		EmployeeLeaveDET employeeLeaveDET = empleavedetlist.get(0);
	                                    		leavebalance = employeeLeaveDET.getLEAVEBALANCE();
	                                    		empdetid = employeeLeaveDET.getID();
	                                    	}else {
	                                    		throw new Exception("Please enter valid Leave Type at Line "+linecnt+" .");
	                                    	}
	                                    }else {
	                                    	throw new Exception("Please enter valid Leave Type at Line "+linecnt+" .");
	                                    }
	                                    if(islop != 1) {
		                                    if(leavebalance == 0) {
		                                    	throw new Exception("Leave Type ("+leavetype+") has zero balance at Line "+linecnt+" .");
		                                    }
	                                    }
                                  
                                    String leaveduration  = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
	                                    if (("").equals(leaveduration) || ("null").equals(leaveduration)){
	                                        throw new Exception("Leave Duration value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }
                                    String fromdate  = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
	                                    if (("").equals(fromdate) || ("null").equals(fromdate)){
	                                        throw new Exception("From Date value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	if(!datecurrentyear(fromdate)) {
	                                    		throw new Exception("From Date is not in current year at Line "+linecnt+" .");
	                                    	}
	                                    	
	                                    	if(!validateDate(fromdate)) {
	                                    		throw new Exception("From Date format is wrong at Line "+linecnt+" .");
	                                    	}
	                                    }
                                    String fpartday  = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
                                    String fdaystatus = "0";
	                                    if (("").equals(fpartday) || ("null").equals(fpartday)){
	                                        throw new Exception("From date-part of the day value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	if(fpartday.equalsIgnoreCase("All day")) {
	                                    		fdaystatus = "Fullday";
	                                    	}else if(fpartday.equalsIgnoreCase("First part of the day")) {
	                                    		fdaystatus = "Morning";
	                                    	}else if(fpartday.equalsIgnoreCase("Second part of the day")) {
	                                    		fdaystatus = "Afternoon";
	                                    	}
	                                    }
	                                    
	                                    if(fdaystatus.equalsIgnoreCase("0")) {
	                                    	throw new Exception("From date-part of the day value is wrong at Line "+linecnt+" .");  
	                                    }
	                                    
	                                    
                                    String todate    = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
	                                    if (("").equals(todate) || ("null").equals(todate)){
	                                        throw new Exception("To Date value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	if(leaveduration.equalsIgnoreCase("Multiple days")) {
		                                    	if(!datecurrentyear(todate)) {
		                                    		throw new Exception("To Date is not in current year at Line "+linecnt+" .");
		                                    	}
		                                    	
		                                    	if(!validateDate(todate)) {
		                                    		throw new Exception("To Date format is wrong at Line "+linecnt+" .");
		                                    	}
	                                    	}
	                                    }
                                    String tpartday  = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
                                    String tdaystatus ="0";
	                                    if (("").equals(tpartday) || ("null").equals(tpartday)){
	                                        throw new Exception("To date-part of the day value is mandatory and  missing at Line "+linecnt+" .");  
	                                    }else {
	                                    	if(leaveduration.equalsIgnoreCase("Multiple days")) {
		                                    	if(tpartday.equalsIgnoreCase("All day")) {
		                                    		tdaystatus = "Fullday";
		                                    	}else if(tpartday.equalsIgnoreCase("First part of the day")) {
		                                    		tdaystatus = "Morning";
		                                    	}else if(tpartday.equalsIgnoreCase("Second part of the day")) {
		                                    		tdaystatus = "Afternoon";
		                                    	}
		                                    	
		                                    	if(tdaystatus.equalsIgnoreCase("0")) {
		 	                                    	throw new Exception("To date-part of the day value is wrong at Line "+linecnt+" .");  
		 	                                    }
	                                    	}
	                                    }
	                                   
	                                 int chkdate = checkfromandtodate(fromdate,todate,leaveduration);
	                                 
	                                 if(chkdate == 1) {
	                                	 throw new Exception("Please change Leave duration from multiple days to single day or change to date at Line "+linecnt+" .");
	                                 }
	                                 
	                                 if(chkdate == 2) {
	                                	 throw new Exception("To date should be greater than from date.please check to date at Line "+linecnt+" .");
	                                 }
	                                 double leavedays = 0.0;
	                                 if(leaveduration.equalsIgnoreCase("Multiple days")) {
	                                	 leavedays = hetnoofdays(String.valueOf(empid),fromdate,todate,fdaystatus,tdaystatus,Plant);
	                                 }else {
	                                	 leavedays = hetnoofdays(String.valueOf(empid),fromdate,fromdate,fdaystatus,fdaystatus,Plant);
	                                 }
	                                 
	                                 if(leavedays == 0) {
	                                	 throw new Exception("Number of leave days is zero at Line "+linecnt+" .");
	                                 }
	                                 
	                                 if(islop != 1) {
		                                 if(leavebalance < leavedays) {
		                                	 throw new Exception("Leave days are greater than "+leavetype+" balace days at Line "+linecnt+" .");
		                                 }
	                                 }

                                                                        
                                    m.put("PLANT", Plant);
                                    m.put("EMPID", String.valueOf(empid));
                                    m.put("EMPCODE", String.valueOf(empno));
                                    m.put("EMPNAME", String.valueOf(EmpName));
                                    m.put("LEAVETYPE", String.valueOf(leavetype));
                                    m.put("EMPDETID", String.valueOf(empdetid));
                                    m.put("LEAVEDURATION", String.valueOf(leaveduration));
                                    if(leaveduration.equalsIgnoreCase("Multiple days")) {
	                                    m.put("FROMDATE", String.valueOf(fromdate));
	                                    m.put("FDAYSTATUS", String.valueOf(fdaystatus));
	                                    m.put("TODATE", String.valueOf(todate));
	                                    m.put("TDAYSTATUS", String.valueOf(tdaystatus));
                                    }else {
                                    	m.put("FROMDATE", String.valueOf(fromdate));
	                                    m.put("FDAYSTATUS", String.valueOf(fdaystatus));
	                                    m.put("TODATE", String.valueOf(fromdate));
	                                    m.put("TDAYSTATUS", String.valueOf(fdaystatus));
                                    }
                                    m.put("NUMBEROFDAYS", String.valueOf(leavedays));
                                    m.put("LOPSTATUS", String.valueOf(islop));
                                    m.put("REPID", String.valueOf(repid));
                                    
                            }

                            hdrList.add(m);

                            System.out.println("hdrList :: " + hdrList.size());
                    }

            } catch (Exception ex) {
                    throw ex;
            } finally {

            }
            
            //Validate Order azees - 12/2020
    		Map mv = new HashMap();
            mv.put("ValidNumber", "");
            hdrList.add(mv);
            
            return hdrList;

    } 
    
    public boolean validateDate(String strDate)
    {

 	    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
 	    sdfrmt.setLenient(false);
 	    try
 	    {
 	        Date javaDate = sdfrmt.parse(strDate); 
 	    }
 	    catch (ParseException e)
 	    {
 	        return false;
 	    }
 	    return true;
    }
    
    public boolean datecurrentyear(String strDate) {
    	DateUtils dateutils = new DateUtils();
        String curtyear = dateutils.getYear();
        String sdate[]=strDate.split("/");
        String syear = sdate[2];
        
        if(syear.equalsIgnoreCase(curtyear)) {
        	return true;
        }else {
        	return false;
        }
    }
    
    public int checkfromandtodate(String fdate,String tdate,String lvduration) {
    	if(lvduration.equalsIgnoreCase("Multiple days")) {
    		 String sdate[]=fdate.split("/");
    		 String edate[]=tdate.split("/");
    		 int strdate = Integer.valueOf(sdate[2]+sdate[1]+sdate[0]);
    		 int enddate = Integer.valueOf(edate[2]+edate[1]+edate[0]);
    		 if(strdate < enddate) {
    			 return 0;
    		 }else if(strdate == enddate){
    			 return 1;
    		 }else {
    			 return 2;
    		 }
    	}else {
    		return 0;
    	}
    }
    
    public double hetnoofdays(String empnoid,String strdate,String enddate,String sstatus,String estatus,String plant) {
    	HrHolidayMstService hrHolidayMstService = new HrHolidayMstServiceImpl();
		HrLeaveApplyDetService hrLeaveApplyDetService = new HrLeaveApplyDetServiceImpl();
		HrLeaveApplyHdrService hrLeaveApplyHdrService = new HrLeaveApplyHdrServiceImpl();
		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		HrLeaveTypeDAO  hrLeaveTypeDAO = new HrLeaveTypeDAO();
		try {
			double ndays = 0.0;
			List<BlockingDates> BlockingDateslist = new ArrayList<BlockingDates>();	
			
			List<HrHolidayMst> holidayList = hrHolidayMstService.getAllHoliday(plant);
			List<HrLeaveApplyDet> hrLeaveApplyDetList = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidfullday(plant,Integer.valueOf(empnoid));
			List<BlockingDates> Blockingleavedays = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpidTwohalfdays(plant, Integer.valueOf(empnoid));
			int arrsize = holidayList.size() + hrLeaveApplyDetList.size()+Blockingleavedays.size();
			ArrayList<String> datelist = new ArrayList<String>(arrsize);
			for (HrHolidayMst HrHolidayMst : holidayList) {
				datelist.add(HrHolidayMst.getHOLIDAY_DATE());
			}	
			for (HrLeaveApplyDet HrLeaveApplyDet : hrLeaveApplyDetList) {
				datelist.add(HrLeaveApplyDet.getLEAVE_DATE());
			}
			
			for (BlockingDates Blockingleave : Blockingleavedays) {
				datelist.add(Blockingleave.getBLOCKING_DATE());
			}
			
			BlockingDateslist = getdatebetween(strdate, enddate);
			int datesize = BlockingDateslist.size();
			int di = 0;
			for (BlockingDates blockingDates : BlockingDateslist) {
				if(datelist.contains(blockingDates.getBLOCKING_DATE())) {
					ndays = ndays + 0.0;
				}else {
					List<HrLeaveApplyDet> hrLeaveApplyDetListdate = hrLeaveApplyDetService.getHrLeaveApplyDetbyEmpiddate(plant, Integer.valueOf(empnoid), blockingDates.getBLOCKING_DATE());
					if(hrLeaveApplyDetListdate.size() > 0) {
						if(hrLeaveApplyDetListdate.size() == 1) {
							ndays = ndays + 0.5;
						}
					}else {
						if(di == 0) {
							if(sstatus.equalsIgnoreCase("Fullday")) {
								ndays = ndays + 1.0;
							}
							
							if(sstatus.equalsIgnoreCase("Morning")) {
								ndays = ndays + 0.5;
							}
							
							if(sstatus.equalsIgnoreCase("Afternoon")) {
								ndays = ndays + 0.5;
							}
							
						}else if(di == (datesize -1)) {
							if(estatus.equalsIgnoreCase("Fullday")) {
								ndays = ndays + 1.0;
							}
							
							if(estatus.equalsIgnoreCase("Morning")) {
								ndays = ndays + 0.5;
							}
							
							if(estatus.equalsIgnoreCase("Afternoon")) {
								ndays = ndays + 0.5;
							}
						}else {
							ndays = ndays + 1.0;
						}
						
					}
				}
				
				di++;
			}
			return ndays;   
		} catch (Exception e) {
			return 0.0; 
		}

    }
    
    public List<BlockingDates> getdatebetween(String str_date,String end_date) throws ParseException{
		List<Date> dates = new ArrayList<Date>();
		List<BlockingDates> BetweenDateslist = new ArrayList<BlockingDates>();	
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date  startDate = (Date)formatter.parse(str_date); 
		Date  endDate = (Date)formatter.parse(end_date);
		long interval = 24*1000 * 60 * 60; // 1 hour in millis
		long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
		long curTime = startDate.getTime();
		while (curTime <= endTime) {
		    dates.add(new Date(curTime));
		    curTime += interval;
		}
		for(int i=0;i<dates.size();i++){
		    Date lDate =(Date)dates.get(i);
		    String ds = formatter.format(lDate);    
		    BlockingDates blockingDates = new BlockingDates();
		    blockingDates.setBLOCKING_DATE(ds);
		    BetweenDateslist.add(blockingDates);
		}
		return BetweenDateslist;
	}
    
    
    @SuppressWarnings("unchecked")
	public ArrayList downloadBillSheetData(String Plant, String filename,
			String sheetName,String baseCurrency,String USERID,String NOOFORDER) throws Exception {
		System.out.println("********  Import Starts *********");
		ImportUtils impUtil = new ImportUtils();
		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		String ValidNumber="";
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));

		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFile(Integer
					.parseInt(BillNumberOfColumn),"BI");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);
			
			String COUNTRYCODE = "";
          	String plantstate = "";
          	String plantstatecode = "";
          	String taxid = "0";
            PlantMstUtil plantmstutil = new PlantMstUtil();
          	List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
          	for (int i = 0; i < viewlistQry.size(); i++) {
          	     Map map = (Map) viewlistQry.get(i);
          	     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
          	     plantstate = StrUtils.fString((String)map.get("STATE"));
          	}
			
          	MasterDAO masterDAO = new MasterDAO();
          	boolean ispuloc=false;		 
          	ArrayList purloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
          	if (purloctions.size() > 0) {
          		 ispuloc = true;
          	}
          	//Validate Order azees - 08/2022
          	BillDAO billDAO = new BillDAO();
			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid =billDAO.Ordercount(Plant,FROM_DATE,TO_DATE);
			int convl =0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			  convl = Integer.valueOf(NOOFORDER);
			String validpo="";
			int validcot=0;
			
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String billno = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					
					// bill validation
					BillDAO billdao = new BillDAO();
					if (("").equals(billno)|| ("null").equals(billno)){
				        throw new Exception("Bill Number value is mandatory and  missing at Line "+linecnt+" .");  
				    }else {
				    	boolean isbill = billdao.isBillNoexisit(Plant, billno);
				    	if(isbill) {
				    		//throw new Exception("Bill Number already exists at Line "+linecnt+" .");  
				    		throw new Exception("Bill Number '" + billno + "' already exists in the system at Line "+linecnt+" .");
				    	}
				    }
					
					boolean isValidBillNumber = false;
					if(20 >= billno.length()){
						isValidBillNumber = true;
					}
					if(!isValidBillNumber){
						throw new Exception("Bill number value :  '" + billno + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
					}
					
					//Validate Order azees - 08/2022
					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
					{
					int ddok =0;
					if(validpo.equalsIgnoreCase(""))
					{
						validpo=billno;
						ddok=1;
						
					}
					else {
					if(validpo.equalsIgnoreCase(billno))
					{
						validcot=validcot+1;
						ddok=validcot-1;
						if(validcot>0)
							validcot=validcot-1;
					}
					else {
						validpo=billno;
						ddok=1;
						
					}
					}
					
					novalid=novalid+ddok;
					
						if(novalid>convl)
						{
							ValidNumber=NOOFORDER;
							break;
						}
					}
					//End
					String isInventory = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
					
					//inventory validation
					String isinvty = "0";
					if (("").equals(isInventory)|| ("null").equals(isInventory)){
						isinvty="0";
				    }else {
				    	if (isInventory.equalsIgnoreCase("Y")|| isInventory.equalsIgnoreCase("y"))
						{
				    		isinvty="1";
						}else if (isInventory.equalsIgnoreCase("N")|| isInventory.equalsIgnoreCase("n"))
						{
							isinvty="0";
						}
				    }
					
					
					String supplier_code = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					
					if (("").equals(supplier_code)|| ("null").equals(supplier_code)){
				        throw new Exception("Supplier Code value is mandatory and  missing at Line "+linecnt+" .");  
				    }
					
					CustUtil custUtil = new CustUtil();
					ArrayList arrCustSUP = custUtil.getVendorDetails(supplier_code,Plant);
					if( arrCustSUP.isEmpty()) {
						throw new Exception("Supplier '" + supplier_code + "' is not created yet on system at Line "+linecnt+".");
					}else {
						supplier_code = (String) arrCustSUP.get(0);
					}

					
					// supplier validation
					boolean isExistVendor = false;
					VendUtil vendUtil = new VendUtil();
					isExistVendor = vendUtil.isExistsVendor(supplier_code, Plant);
					if(!isExistVendor){
						throw new Exception("Supplier '" + supplier_code + "' is not created yet on system at Line "+linecnt+".");
					}
					
					
					String shippingcustomer = StrUtils.replaceStr2Char(((String) list.get(2)).trim()); //new
					
					//Shipping customer validation
					String shippingid = "";
					if (("").equals(shippingcustomer)|| ("null").equals(shippingcustomer)){
						shippingcustomer = "";
					}else {
						
						boolean isValidShippingCustomer = false;
						if(SHIPPING_CUSTOMER_LEN >= shippingcustomer.length()){
							isValidShippingCustomer = true;
						}
						if(!isValidShippingCustomer){
							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
						}
						
						isExistVendor = false;
						if(shippingcustomer.length()>0){
						isExistVendor = custUtil.isExistCustomer(shippingcustomer, Plant);
						if(!isExistVendor){
							throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system at Line "+linecnt+".");
						}else {
							ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
							Map mCustot=(Map)arrList.get(0);
							shippingid=(String) mCustot.get("CUSTNO");
						}
						}
					}
					String transportmode = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					TransportModeDAO transportmodedao = new TransportModeDAO();
                    String trans = "";
					if(transportmode.length()>0){
						trans = transportmodedao.getTransportModeByName(Plant,transportmode);
					}
					else {
						
						transportmode = "";
					}
					if(transportmode.equals("null")) {
						transportmode = "" ;
						trans = "";
					}
					
                    if (trans == null){
							throw new Exception("Create Transport Mode : '" + transportmode + "' first, before adding Transport at Line "+linecnt+".");
					}
					
					String project = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					
					//Project validation
					String projectid = "";
					boolean isValidproject = false;
					if(DET_DESC_LEN >= project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistVendor = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistVendor = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistVendor){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
					
					String paymentterms = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					
					// payment terms validation
					if (("").equals(paymentterms)|| ("null").equals(paymentterms)){
				    }else {
				    	boolean isValidpaymentterms = false;
						if(DET_DESC_LEN >= paymentterms.length()){
							isValidpaymentterms = true;
						}
						if(!isValidpaymentterms){
							throw new Exception("Payment Terms value :  '" + paymentterms + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
						}
				    	PayTermsUtil payTermsUtil = new PayTermsUtil();
						Hashtable ht=new Hashtable();
						ht.put("PLANT",Plant);
					    ht.put("TERMS",paymentterms);
					    List listQry = payTermsUtil.getPaymentTermsDetails(ht);
					    if (listQry.size() == 0) {
					    	throw new Exception("Payment term '" + paymentterms + "' is not created yet on system.");
					    }
				    }
					
//					CustUtil custUtil = new CustUtil();
//					 ArrayList arrCustSUP = custUtil.getVendorDetails(supplier_code,Plant);
					 String paytype = (String) arrCustSUP.get(17);
					  if(paymentterms.equalsIgnoreCase("null")|| paymentterms == null || paymentterms.equalsIgnoreCase("")) {
						  paymentterms= (String) arrCustSUP.get(39);
					  }
					  if(trans.equalsIgnoreCase("null")|| trans == null || trans.equalsIgnoreCase("")) {
						  trans= (String) arrCustSUP.get(38);
					  }
					

					String pono = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					
					// PONO field data validation
					POUtil _poUtil = new POUtil();
					if (("").equals(pono)|| ("null").equals(pono)){
					       
				    }else {
				    	boolean isValidPonoData = false;
						if(PONO_LEN >= pono.length()){
							isValidPonoData = true;
						}
						if(!isValidPonoData){
							throw new Exception("PONO value :  '" + pono + "' length has exceeded the maximum value of "+PONO_LEN+" Chars at Line "+linecnt+".");
						}
						
						Hashtable htpono = new Hashtable();
						htpono.put("PLANT", Plant);
						htpono.put("PONO", pono);
						ArrayList al = _poUtil.getPoHdrDetails(" * ", htpono);
						if (al.size() == 0) {
						    	throw new Exception("PONO value'" + pono + "' is not created yet on system.");
						}
				    }
					
					String grno = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					
					// grno validation
					if (("").equals(grno)|| ("null").equals(grno)){
						if(isinvty.equalsIgnoreCase("1")) {
							throw new Exception("GRNO Number value is mandatory and  missing at Line "+linecnt+" .");  
						}
				       
						if (("").equals(pono)|| ("null").equals(pono) || pono.equalsIgnoreCase(null)){
							 
						}else {
							throw new Exception("GRNO Number value is mandatory and  missing at Line "+linecnt+" ."); 
						}
				    }else {
				    	boolean isValidgrno = false;
						if(20 >= grno.length()){
							isValidgrno = true;
						}
						if(!isValidgrno){
							throw new Exception("GRNO value :  '" + grno + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
						}
						
						boolean isgrno = billdao.isGRNOexisit(Plant, grno);
				    	if(isgrno) {
				    		//throw new Exception("Bill Number already exists at Line "+linecnt+" .");  
				    		throw new Exception("GRNO Number '" + grno + "' already exists in the system at Line "+linecnt+" .");
				    	}
						
				    }
					
					String shippingref = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					shippingref="";
					// Shipping reference field data validation
					if (("").equals(shippingref)|| ("null").equals(shippingref)){
					       
				    }else {
				    	boolean isValidshiprefData = false;
						if(PONO_LEN >= shippingref.length()){
							isValidshiprefData = true;
						}
						if(!isValidshiprefData){
							throw new Exception("Shipping reference value :  '" + shippingref + "' length has exceeded the maximum value of "+PONO_LEN+" Chars at Line "+linecnt+".");
						}
						
						MasterUtil _MasterUtil=new  MasterUtil();
						ArrayList  expcheck = _MasterUtil.getExpenseDetailusingponoanddnol(Plant, pono, shippingref);
						if (expcheck.size() == 0) {
							throw new Exception("Shipping reference value'" + shippingref + "' is not created yet on system.");
						}
				    }
					
					String reference = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					
					//Reference field data validation
					if (("").equals(reference)|| ("null").equals(reference)){
					       
				    }else {
				    	boolean isValidrefData = false;
						if(PONO_LEN >= reference.length()){
							isValidrefData = true;
						}
						if(!isValidrefData){
							throw new Exception("Reference value :  '" + reference + "' length has exceeded the maximum value of "+PONO_LEN+" Chars at Line "+linecnt+".");
						}
				    }
					
				    String billdate = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
				    
				    // bill date validation
					if (("").equals(billdate)|| ("null").equals(billdate)){
				        throw new Exception("Bill Date value is mandatory and  missing at Line "+linecnt+" .");  
				    }else {
				    	boolean isCorrectBillDateFormat = false;
						isCorrectBillDateFormat = impUtil.isCorrectDateFormatUsingRegex(billdate);
						if(!isCorrectBillDateFormat){
							throw new Exception("Bill Date '" + billdate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					
					String duedate = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					
					  // Due date validation
					if (("").equals(duedate)|| ("null").equals(duedate)){
						duedate = "";
				    }else {
				    	boolean isCorrectDueDateFormat = false;
				    	isCorrectDueDateFormat = impUtil.isCorrectDateFormatUsingRegex(duedate);
						if(!isCorrectDueDateFormat){
							throw new Exception("Due Date '" + duedate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					
					String ordertype = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
	   				// order type validation
						if (!ordertype.equalsIgnoreCase("null") && ordertype != null){
							boolean isExistOrderType = false;
							OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
							isExistOrderType = ordertypeUtil.isExistsOrderType(ordertype, Plant);
							if(!isExistOrderType){
								throw new Exception("Ordertype '" + ordertype + "' is not created yet on system at Line "+linecnt+".");
							}
						}
					
					 String employeename = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
					 
	  					
		             // Employee field validation
						 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							 boolean isValidEmployee = false;
								if(20 >= employeename.length()){
									isValidEmployee = true;
								}
								if(!isValidEmployee){
			                        throw new Exception("Employee value '" + employeename + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
								}
								boolean isExistEmployee = false;
								Hashtable htEmp = new Hashtable();
					    		htEmp.put(IDBConstants.PLANT,Plant);
					    		htEmp.put("EMPNO",employeename);
					    		EmployeeUtil empUtil = new EmployeeUtil();
					    		if(!empUtil.isExistsEmployee(htEmp))
					    	    {
					    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
					    	    }
								}
						 
						 if (employeename.equalsIgnoreCase("null")|| employeename == null)
		  						employeename = "";
						 
					String purchaseloc = StrUtils.replaceStr2Char(((String) list.get(15)).trim());
					
					// purchase location field validation	
					if(ispuloc) {
		                   if (("").equals(purchaseloc)|| ("null").equals(purchaseloc)){
						        throw new Exception("Purchase Location value is mandatory and  missing at Line "+linecnt+" .");  
						    }
	                }
	                if(ispuloc) {
					
						boolean isValidPurchaseLocation = false;
						if(PURCHASE_LOCATION >= purchaseloc.length()){
							isValidPurchaseLocation = true;
						}
						if(!isValidPurchaseLocation){
	                        throw new Exception("Purchase Location value '" + purchaseloc + "' length has exceeded the maximum value of "+PURCHASE_LOCATION+" Chars at Line "+linecnt+".");
						}
						MasterUtil _MasterUtil=new  MasterUtil();
		     		   	ArrayList ccList =  _MasterUtil.getSalesLocationByState(purchaseloc,Plant,COUNTRYCODE);
		     		   	if(ccList.size()>0) {
		     		   		for (int i = 0; i < ccList.size(); i++) {
		     		   			Map map1 = (Map) ccList.get(i);
		     		   			plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
		     		   		}
		     		   }else {
		     			  throw new Exception("Enter Valid Purchase Location  : '" + purchaseloc + "', before adding bill at Line "+linecnt+".");
		     		   }
	                }
					
	               
					String currencyID = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					
					// Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
                                            
                                            Hashtable ht = new Hashtable();
                                            ht.put(IDBConstants.PLANT, Plant);
                                            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
				    
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
			            currencyID = baseCurrency;
					
					String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
					
					 // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
						
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }
					
					String taxpercent = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
					
					// tax percentage field validation
					boolean isCorrectGstFormat = false;
					if (!taxpercent.trim().equalsIgnoreCase("null") && taxpercent != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(taxpercent);
						if(!isCorrectGstFormat){
							throw new Exception("Tax percentage value '" + taxpercent + "' is not accepted format at Line "+linecnt+".");
						}
					}else {
						selectBean sb = new selectBean();
						taxpercent = sb.getGST("PURCHASE",Plant);
					}
					
					
					String tax = StrUtils.replaceStr2Char(((String) list.get(19)).trim()); 
					
					// tax field validation
					if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
					
					boolean isValidtax = false;
					if(REMARKS_LEN >= tax.length()){
						isValidtax = true;
					}
					if(!isValidtax){
	                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
					}
	          
					FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
					List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("INBOUND", COUNTRYCODE, tax);
					if(taxtypes.size() > 0) {
		        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
		        		
		        			taxid = String.valueOf(finCountryTaxType.getID());	        			
		        			String display="";
				         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
				         	{
				         		display = finCountryTaxType.getTAXTYPE();
				         	}
				         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
				         		display = finCountryTaxType.getTAXTYPE()+" ["+taxpercent+"%]";
				         	}
				         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
				         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
				         	}
				         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
				         		if(plantstatecode.equalsIgnoreCase("")) {
				         			display = finCountryTaxType.getTAXTYPE();
				         		}else {
				         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
				         		}
				         	}
				         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
				         		if(plantstatecode.equalsIgnoreCase("")) {
				         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
				         		}else {
				         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
				         		}
				         	}
				         	else
				         	{
				         		if(plantstatecode.equalsIgnoreCase("")) {
				         			display = finCountryTaxType.getTAXTYPE()+" ["+taxpercent+"%]";
				         		}else {
				         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+taxpercent+"%]";
				         		}
				         	}
				         	
				         	tax=display;
		        			
		        		}
					}else{
						throw new Exception("Enter Valid Tax  : '" + tax + "', before adding bill at Line "+linecnt+".");
					}
					
					
					String reversechar = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
					String goodsipt = StrUtils.replaceStr2Char(((String) list.get(21)).trim()); 
					
					//Reverse Charge & Goods Import
					if (reversechar.equals("null") || reversechar == null || reversechar.equals("")) {
						reversechar = "0";
					} else if (reversechar.equalsIgnoreCase("Y") || reversechar.equalsIgnoreCase("y")) {
						reversechar = "1";
					} else if (reversechar.equalsIgnoreCase("N") || reversechar.equalsIgnoreCase("n")) {
						reversechar = "0";
					} else if (!reversechar.equalsIgnoreCase("N") || !reversechar.equalsIgnoreCase("n")
							|| !reversechar.equalsIgnoreCase("Y") || !reversechar.equalsIgnoreCase("y")) {
						throw new Exception("Enter Valid Reverse Charge  : '" + reversechar
								+ "', before adding bill at Line " + linecnt + ".");
					}

					if (goodsipt.equals("null") || goodsipt == null || goodsipt.equals("")) {
						goodsipt = "0";
					} else if (goodsipt.equalsIgnoreCase("Y") || goodsipt.equalsIgnoreCase("y")) {
						goodsipt = "1";
					} else if (goodsipt.equalsIgnoreCase("N") || goodsipt.equalsIgnoreCase("n")) {
						goodsipt = "0";
					} else if (!goodsipt.equalsIgnoreCase("N") || !goodsipt.equalsIgnoreCase("n")
							|| !goodsipt.equalsIgnoreCase("Y") || !goodsipt.equalsIgnoreCase("y")) {
						throw new Exception("Enter Valid Goods Import  : '" + goodsipt
								+ "', before adding bill at Line " + linecnt + ".");
					}

					if (reversechar.equals("1") && goodsipt.equals("1")) {
						throw new Exception(
								"Reverse Charge & Goods Import can't be 'Y',Enter any one as 'Y' , before adding bill at Line "
										+ linecnt + ".");
					}
					
					String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
					
					// Product Rates Are field validation
					if (("").equals(productRatesAre) || ("null").equals(productRatesAre)) {
						productRatesAre = "Tax Exclusive";
					}
					boolean isValidProductRatesAre = false;
					if (EMPTYPE_LEN >= productRatesAre.length()) {
						isValidProductRatesAre = true;
					}
					if (!isValidProductRatesAre) {
						throw new Exception("Product Rates Are value '" + productRatesAre
								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
								+ linecnt + ".");
					}

					if (productRatesAre.length() > 0) {
						if (productRatesAre.equalsIgnoreCase("Tax Inclusive")) {
						} else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")) {
						} else
							throw new Exception("Product Rates Are can't be '" + productRatesAre
									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding bill at Line "
									+ linecnt + ".");
					}
				  
					
					String item = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
					
					// item validation
					if (("").equals(item) || ("null").equals(item)) {
						throw new Exception("Product ID value is mandatory and  missing at Line " + linecnt + " .");
					}
					boolean isValiditem = false;
					if(20 >= item.length()){
						isValiditem = true;
					}
					if(!isValiditem){
                        throw new Exception("Product ID value '" + item + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
					}

					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", item);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
					}	  
					
					
					
					
					
					String lnno = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
					
					// is valid Line No
					if (("").equals(lnno) || ("null").equals(lnno)) {
						throw new Exception("LNNO value is mandatory and  missing at Line " + linecnt + " .");
					}
					boolean isValidlnno = false;
					if(4 >= lnno.length()){
						isValidlnno = true;
					}
					if(!isValidlnno){
                        throw new Exception("Bill Order Line Number value '" + lnno + "' length has exceeded the maximum value of 4 Chars at Line "+linecnt+".");
					}
					int isThisANumber = 0;
					try {
						isThisANumber = Integer.parseInt(lnno);
					} catch (NumberFormatException nfe) {
						throw new Exception("LNNO value :  '" + lnno + "' for Bill " + billno
								+ " is not a valid number  at Line " + linecnt + ".");
					}
					
					
					
					String account = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
					
					// Account field data validation
					boolean isValidAccount = false;
					if(PO_REMARK_LEN >= account.length()){
						isValidAccount = true;
					}
					if(!isValidAccount){
						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "+PO_REMARK_LEN+" Chars at Line "+linecnt+".");
					}
					
					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
						account = "Inventory Asset";
					}else {
					
						CoaDAO coadao = new CoaDAO();
						List<Map<String, String>> listQry = null;
						Map<String, List<Map<String, String>>> listGrouped=null;
						listQry = coadao.selectSubAccountTypeList(Plant,account);
						   if(listQry.size()>0)
						   {
							   String[] filterArray = {"3"};
								String[] detailArray= {"6"};
								listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
										.collect(Collectors.toList());
								listGrouped=listQry.stream()
										.filter(x->Arrays.stream(filterArray)
										.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
										.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
								
								if(listGrouped.size()>0)
								   {
								   
								   }
								else
								   {
									  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
								   }
						   }
						   else
						   {
							  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
						   }
					}
					
					String uom = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
					
					// UOM field validation
					ItemMstUtil itemmastUtil = new ItemMstUtil();
					boolean isValidUomData = false;
					 if(UOM >= uom.length()){
					         isValidUomData = true;
					 }
					 if(!isValidUomData){
					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
					 }
					 if (!uom.equalsIgnoreCase("null") && uom != null){
					 boolean isExistUom =false;
					 htitem.put("STKUOM",uom);
					
							UomUtil uomUtil = new UomUtil();
							isExistUom = uomUtil.isExistsUom(uom, Plant);
							if(!isExistUom){
								throw new Exception("UOM '" + uom + "' is not created/in active at line "+linecnt+".");
							}
							
					 }else {
//						 ArrayList listQry = itemmastUtil.getProductDetailsWithStockonHand(item,Plant," ");//Azees Fix Query Load 09.22
//				        	if (listQry.size() > 0) {
//				        		for(int i =0; i<listQry.size(); i++) {
//					        		Map um=(Map)listQry.get(i);
//					        		uom = StrUtils.fString((String)um.get("STKUOM"));
//				        		}
//				        	}
				        	String suom =new ItemMstDAO().getItemPurchaseUOM(Plant,item);
	   						uom=(String)suom;
					 }
					
					String locationid  = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
					
					//location
					if (("").equals(locationid) || ("null").equals(locationid)) {
						if(isinvty.equalsIgnoreCase("1")) {
							throw new Exception("Location value is mandatory and  missing at Line "+linecnt+" .");  
						}
					}else {
						boolean isValidlocationid = false;
						if(20 >= locationid.length()){
							isValidlocationid = true;
						}
						if(!isValidlocationid){
	                        throw new Exception("Location value '" + locationid + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
						}
						LocMstDAO  _LocMstDAO  = new LocMstDAO(); 
						List listloc =  _LocMstDAO.getLocByWMSImport(Plant,USERID,locationid);
						if(listloc.size() == 0){
							throw new Exception("Location '" + locationid + "' is not created yet on system at Line "+linecnt+".");
						}
					}
					
					String batch = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
					
					//batch
					if (("").equals(batch) || ("null").equals(batch)) {
						if(isinvty.equalsIgnoreCase("1")) {
							batch = "NOBATCH";
						}
					}else {
						boolean isValidbatchData = false;
						 if(40 >= batch.length()){
							 isValidbatchData = true;
						 }
						 if(!isValidbatchData){
						         throw new Exception("Batch value :  '" + batch + "' length has exceeded the maximum value of 40 Chars at Line "+linecnt+".");
						 }
					}
					
					
					String qty = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null) {
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if (!isCorrectQuantityFormat) {
							throw new Exception(
									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
						}
					} else {
						qty = "0";
					}

					
					//product validation from purchase order
					if (("").equals(pono)|| ("null").equals(pono)){
					       
				    }else {
						/*Hashtable htpono = new Hashtable();
						htpono.put("PLANT", Plant);
						htpono.put("PONO", pono);
						htpono.put("ITEM", item);
						ArrayList al = _poUtil.getPoDetDetails(" * ", htpono);
						QTYOR
						CONVCOST
						UNITCOST
						*/
				    	POUtil poUtil = new POUtil();
				    	Hashtable ht=new Hashtable();
					    ht.put("PONO", pono);
					    ht.put("PLANT", Plant);
					    ht.put("GRNO", grno);
					    ht.put("B.ITEM", item);
					    List listQry = poUtil.getBillingDetailsByGRNO(ht);
						if (listQry.size() == 0) {
						    //throw new Exception("Product id '" + item + "' or GRNO '" + grno + "' does not exist in the purchase order '" + pono + "' at Line " + linecnt + ".");
						    throw new Exception("Goods Receipt details does not found for Sales Order/GINO/Product/Location/Batch at Line " + linecnt + ".");
						}else {
							double recvqty =0;
					    	for(int i =0; i<listQry.size(); i++) {   
					    		Map mk=(Map)listQry.get(i);
					    		recvqty = Double.valueOf((String)mk.get("RECQTY"));
					    	}
					    	double dqty = Double.valueOf(qty);
					    	if(dqty == recvqty) {
					    	}else {
					    		/*throw new Exception("Import Bill product quantity not greater than the Purchaser Order Receipt GRNO quantity at Line " + linecnt + ".");*/
					    		throw new Exception("Import Bill product quantity ("+dqty+") should be equal to Purchaser Order Receipt GRNO quantity ("+recvqty+") at Line " + linecnt + ".");
					    	}
						}	
					    	
				    }
					
					
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
					
					// unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null) {
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if (!isCorrectUnitPriceFormat) {
							throw new Exception("Unit cost value '" + unit_price + "' is not accepted format at Line "
									+ linecnt + ".");
						}
					} else {
						unit_price = "0";
					}

					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null) {
						if (unit_price.contains(".")) {
							int costdecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if (costdecnum > numberOfDecimal) {
								throw new Exception("Invalid more than " + numberOfDecimal
										+ " digits after decimal in Unit Price at Line " + linecnt + ".");
							}
						}

					}
					
					String productdiscount = StrUtils.replaceStr2Char(((String) list.get(31)).trim());
					
					// Product Discount field data validation			
					boolean isCorrectproductdiscount = false;
					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
						if(!isCorrectproductdiscount){
							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
						}
					}else {
						productdiscount = "0";
					}
					
					String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
					
					// Product Discount % field validation
					if (productdiscountPer.equals("null") || productdiscountPer == null
							|| productdiscountPer.equals("")) {
						productdiscountPer = currencyID;
					} else if (productdiscountPer.equalsIgnoreCase("Y") || productdiscountPer.equalsIgnoreCase("y")) {
						productdiscountPer = "%";
					} else if (productdiscountPer.equalsIgnoreCase("N") || productdiscountPer.equalsIgnoreCase("n")) {
						productdiscountPer = currencyID;
					} else if (!productdiscountPer.equalsIgnoreCase("N") || !productdiscountPer.equalsIgnoreCase("n")
							|| !productdiscountPer.equalsIgnoreCase("Y") || !productdiscountPer.equalsIgnoreCase("y")) {
						throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer
								+ "', before adding purchase at Line " + linecnt + ".");
					}

					
					String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
					
					// order discount validation
					boolean isCorrectOrderDiscount = false;
					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null) {
						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
						if (!isCorrectOrderDiscount) {
							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
						}
					} else {
						orderdiscount = "0";
					}
					
					String orderdiscountAre = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
					
					// Order Discount Are field validation
					if (("").equals(orderdiscountAre) || ("null").equals(orderdiscountAre)) {
						orderdiscountAre = "Tax Inclusive";
					}
					boolean isValidorderdiscountAre = false;
					if (EMPTYPE_LEN > orderdiscountAre.length()) {
						isValidorderdiscountAre = true;
					}
					if (!isValidorderdiscountAre) {
						throw new Exception("Order Discount Are value '" + orderdiscountAre
								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
								+ linecnt + ".");
					}

					if (orderdiscountAre.length() > 0) {
						if (orderdiscountAre.equalsIgnoreCase("Tax Inclusive")) {
						} else if (orderdiscountAre.equalsIgnoreCase("Tax Exclusive")) {
						} else
							throw new Exception("Order Discount Are can't be '" + orderdiscountAre
									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding bill at Line "
									+ linecnt + ".");
					}
					
					String orderdiscountPer = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
					
					// Order Discount % field validation
					if (orderdiscountPer.equals("null") || orderdiscountPer == null || orderdiscountPer.equals("")) {
						orderdiscountPer = currencyID;
					} else if (orderdiscountPer.equalsIgnoreCase("Y") || orderdiscountPer.equalsIgnoreCase("y")) {
						orderdiscountPer = "%";
					} else if (orderdiscountPer.equalsIgnoreCase("N") || orderdiscountPer.equalsIgnoreCase("n")) {
						orderdiscountPer = currencyID;
					} else if (!orderdiscountPer.equalsIgnoreCase("N") || !orderdiscountPer.equalsIgnoreCase("n")
							|| !orderdiscountPer.equalsIgnoreCase("Y") || !orderdiscountPer.equalsIgnoreCase("y")) {
						throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountPer
								+ "', before adding bill at Line " + linecnt + ".");
					}
				   
					
					String discount = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
					
					//discount validation
					boolean isCorrectDiscount = false;
					if (!discount.trim().equalsIgnoreCase("null") && discount != null) {
						isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
						if (!isCorrectDiscount) {
							throw new Exception("Discount value '" + discount + "' is not accepted format.");
						}
					} else {
						discount = "0";
					}
					String discountAre = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
					
					//Discount Are field validation
					if (("").equals(discountAre) || ("null").equals(discountAre)) {
						discountAre = "Tax Inclusive";
					}
					boolean isValiddiscountAre = false;
					if (EMPTYPE_LEN > discountAre.length()) {
						isValiddiscountAre = true;
					}
					if (!isValiddiscountAre) {
						throw new Exception("Discount Are value '" + discountAre
								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
								+ linecnt + ".");
					}

					if (discountAre.length() > 0) {
						if (discountAre.equalsIgnoreCase("Tax Inclusive")) {
						} else if (discountAre.equalsIgnoreCase("Tax Exclusive")) {
						} else
							throw new Exception("Discount Are can't be '" + discountAre
									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding bill at Line "
									+ linecnt + ".");
					}
					
					String discountPer = StrUtils.replaceStr2Char(((String) list.get(38)).trim());
					
					// Discount % field validation
					if (discountPer.equals("null") || discountPer == null || discountPer.equals("")) {
						discountPer = currencyID;
					} else if (discountPer.equalsIgnoreCase("Y") || discountPer.equalsIgnoreCase("y")) {
						discountPer = "%";
					} else if (discountPer.equalsIgnoreCase("N") || discountPer.equalsIgnoreCase("n")) {
						discountPer = currencyID;
					} else if (!discountPer.equalsIgnoreCase("N") || !discountPer.equalsIgnoreCase("n")
							|| !discountPer.equalsIgnoreCase("Y") || !discountPer.equalsIgnoreCase("y")) {
						throw new Exception("Enter Valid Discount %  : '" + discountPer
								+ "', before adding bill at Line " + linecnt + ".");
					}
					
					
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(39)).trim());
					
					// shipping cost validation
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}else {
						shippingcost = "0";
					}
					
					String shippingcostAre = StrUtils.replaceStr2Char(((String) list.get(40)).trim());
					
					// Shipping Cost Are field validation
					if (("").equals(shippingcostAre) || ("null").equals(shippingcostAre)) {
						shippingcostAre = "Tax Inclusive";
					}
					boolean isValidShippingCostAre = false;
					if (EMPTYPE_LEN > shippingcostAre.length()) {
						isValidShippingCostAre = true;
					}
					if (!isValidShippingCostAre) {
						throw new Exception("Shipping Cost Are value '" + shippingcostAre
								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
								+ linecnt + ".");
					}

					if (shippingcostAre.length() > 0) {
						if (shippingcostAre.equalsIgnoreCase("Tax Inclusive")) {
						} else if (shippingcostAre.equalsIgnoreCase("Tax Exclusive")) {
						} else
							throw new Exception("Shipping Cost Are can't be '" + shippingcostAre
									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding bill at Line "
									+ linecnt + ".");
					}
					
					String adjustment = StrUtils.replaceStr2Char(((String) list.get(41)).trim());
					
					//adjustment validation
					boolean isCorrectadjustment = false;
					if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
						isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
						if(!isCorrectadjustment){
							throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
						}
					}else {
						adjustment = "0";
					}
					
					String notes = StrUtils.replaceStr2Char(((String) list.get(42)).trim());
					
					
					// Notes field data validation
					boolean isValidRemarksData = false;
					if(1000 >= notes.length()){
						isValidRemarksData = true;
					}
					if(!isValidRemarksData){
						throw new Exception("Notes value '" + notes + "' length has exceeded the maximum value of 1000 Chars at Line "+linecnt+".");
					}
					
					
					
					m.put("PLANT", Plant);
					m.put("ISINVENTORY", isinvty);
					m.put("SUPPLIERCODE", supplier_code);
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put("SHIPPINGCUSTOMER", shippingcustomer);
					m.put("SHIPPINGID", shippingid);
					m.put(IConstants.TRANSPORTID, trans);
					m.put("PROJECTID", projectid);
					m.put("PAYMENTTERMS", paymentterms);
					m.put("BILL", billno);
					m.put("GRNO", grno);
					m.put("PONO", pono);
					m.put("SHIPPINGREFERENCE", shippingref);
					m.put("REFERENCE", reference);
					m.put("BILLDATE", billdate);
					m.put("DUEDATE", duedate);
					m.put("ORDERTYPE", ordertype);
					m.put("EMPNO", employeename);
					m.put("PURCHASELOC", purchaseloc);
					m.put("CURRENCYID", currencyID);
					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
					m.put("TAXPERCENTAGE", taxpercent);
					m.put("TAX", tax);
					m.put("TAXID", taxid);
					m.put("REVERSECHARGE", reversechar);
					m.put("GOODSIMPORT", goodsipt);
					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
					{
						productRatesAre="1";
					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
					{
						productRatesAre="0";
					}else {
						productRatesAre="0";
					}
					m.put("ISTAXINCLUSIVE", productRatesAre);
					m.put("PRODUCT", item);
					m.put("LNNO", lnno);
					m.put("ACCOUNT", account);
					m.put("UOM", uom);
					m.put("LOCATIONID", locationid);
					m.put("BATCH", batch);
					m.put("QTY", qty);
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
					}
					m.put("UNITPRICE", unit_price);
					if(!productdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("PRODUCTDISCOUNT", productdiscount);
					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
						orderdiscount = "0";
					if(!orderdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("ORDERDISCOUNT", orderdiscount);
					
					if (orderdiscountAre.equalsIgnoreCase("Tax Inclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Inclusive"))
					{
						orderdiscountAre="1";
					}else if (orderdiscountAre.equalsIgnoreCase("Tax Exclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Exclusive"))
					{
						orderdiscountAre="0";
					}else {
						orderdiscountAre="0";
					}
					m.put("ISORDERDISCOUNTTAX", orderdiscountAre);
					m.put("ORDERDISCOUNTTYPE", orderdiscountPer);
					
					
					if (discount.equalsIgnoreCase("null")|| discount == null)
						discount = "0";
					if(!discountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("DISCOUNT", discount);
					
					if (discountAre.equalsIgnoreCase("Tax Inclusive"))
					{
						discountAre="1";
					}else if (discountAre.equalsIgnoreCase("Tax Exclusive"))
					{
						discountAre="0";
					}else {
						discountAre="0";
					}
					m.put("ISDISCOUNTTAX", discountAre);
					m.put("DISCOUNTTYPE", discountPer);
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
					}
					m.put("SHIPPINGCOST", shippingcost);
					if (shippingcostAre.equalsIgnoreCase("Tax Inclusive"))
					{
						shippingcostAre="1";
					}else if (shippingcostAre.equalsIgnoreCase("Tax Exclusive"))
					{
						shippingcostAre="0";
					}else {
						shippingcostAre="0";
					}
					m.put("ISSHIPPINGTAX", shippingcostAre);
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					m.put("ADJUSTMENT", adjustment);
					m.put("NOTES", notes);
					
				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		
	   	//Validate Order azees - 08/2022
	   	if(!ValidNumber.equalsIgnoreCase("")) {
		Map mv = new HashMap();
	    mv.put("ValidNumber", ValidNumber);
	    hdrList.add(mv);
	   		}
		
		return hdrList;

	}
    
    
    @SuppressWarnings("unchecked")
   	public ArrayList downloadInvoiceSheetData(String Plant, String filename,
   			String sheetName,String baseCurrency,String USERID,String NOOFORDER) throws Exception {
   		System.out.println("********  Import Starts *********");
   		ImportUtils impUtil = new ImportUtils();
   		// to get file name
   		ArrayList hdrList = new ArrayList();
   		File f = new File(filename);
   		filename = f.getName();
   		f = null;
   		String ValidNumber="";
   		System.out.println("After extraction filename : " + filename);
   		
   		filename = DbBean.CountSheetUploadPath + filename;

   		System.out.println("Import File :" + filename);
   		System.out.println("Import sheet :" + sheetName);
   		
   		PlantMstDAO plantMstDAO = new PlantMstDAO();
   	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));

   		try {
   			POIReadExcel poi = new POIReadExcel(filename, sheetName);

   			String[] str = poi.readExcelFile(Integer
   					.parseInt(InvoiceNumberOfColumn),"INV");
   			System.out
   					.println("Total number of record found in Excel sheet  :  "
   							+ str.length);
   			
   			String COUNTRYCODE = "";
             	String plantstate = "";
             	String plantstatecode = "";
             	String taxid = "0";
               PlantMstUtil plantmstutil = new PlantMstUtil();
             	List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
             	for (int i = 0; i < viewlistQry.size(); i++) {
             	     Map map = (Map) viewlistQry.get(i);
             	     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
             	     plantstate = StrUtils.fString((String)map.get("STATE"));
             	}
   			
             	MasterDAO masterDAO = new MasterDAO();
             	boolean ispuloc=false;		 
             	ArrayList purloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
             	if (purloctions.size() > 0) {
             		 ispuloc = true;
             	}

             	//Validate Order azees - 08/2022
             	InvoiceDAO invoiceDAO = new InvoiceDAO();
    			DateUtils _dateUtils = new DateUtils();
    			String FROM_DATE = _dateUtils.getDate();
    			if (FROM_DATE.length() > 5)
    				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
    			
    			String TO_DATE = _dateUtils.getLastDayOfMonth();
    			if (TO_DATE.length() > 5)
    				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
             	
             	int novalid =invoiceDAO.Ordercount(Plant,FROM_DATE,TO_DATE);
    			int convl =0;
    			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
    			  convl = Integer.valueOf(NOOFORDER);
    			String validpo="";
    			int validcot=0;
             	
   			for (int j = 1; j < str.length; j++) {

   				String s1 = str[j];
   				String s2 = "";

   				StringTokenizer parser = new StringTokenizer(s1, "|||");

   				System.out.println("Record " + j + "\t: " + s1);

   				List list = new LinkedList(); // Doubly-linked list

   				list = new ArrayList();

   				while (parser.hasMoreTokens()) {
   					list.add(parser.nextToken());

   				}

   				int linecnt = j;
   			        linecnt= linecnt+1;
   				Map m = new HashMap();

   				System.out.println("Total number of record per line  :  "
   						+ list.size());

   				if(list.size() > 0) {
   					
   					String invoice = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
   					
   					// invoice validation
   					if (("").equals(invoice)|| ("null").equals(invoice)){
   				        throw new Exception("Invoice Number value is mandatory and  missing at Line "+linecnt+" .");  
   				    }
   					
   					boolean isValidBillNumber = false;
   					if(20 >= invoice.length()){
   						isValidBillNumber = true;
   					}
   					if(!isValidBillNumber){
   						throw new Exception("Invoice number value :  '" + invoice + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
   					}
   					//Validate Order azees - 12/2020
					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
					{
					int ddok =0;
					if(validpo.equalsIgnoreCase(""))
					{
						validpo=invoice;
						ddok=1;
						
					}
					else {
					if(validpo.equalsIgnoreCase(invoice))
					{
						validcot=validcot+1;
						ddok=validcot-1;
						if(validcot>0)
							validcot=validcot-1;
					}
					else {
						validpo=invoice;
						ddok=1;
						
					}
					}
					
					novalid=novalid+ddok;
					
						if(novalid>convl)
						{
							ValidNumber=NOOFORDER;
							break;
						}
					}
					//End
   					String isInventory = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
   					
   					//inventory validation
   					String isinvty = "0";
   					if (("").equals(isInventory)|| ("null").equals(isInventory)){
   						isinvty="0";
   				    }else {
   				    	if (isInventory.equalsIgnoreCase("Y")|| isInventory.equalsIgnoreCase("y"))
   						{
   				    		isinvty="1";
   						}else if (isInventory.equalsIgnoreCase("N")|| isInventory.equalsIgnoreCase("n"))
   						{
   							isinvty="0";
   						}
   				    }
   					
   					
   					String customerCode = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
   					
   				 // customer validation
					if (("").equals(customerCode)|| ("null").equals(customerCode)){
					    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}
					boolean isValidcustomerCode = false;
					if(20 >= customerCode.length()){
						isValidcustomerCode = true;
					}
					if(!isValidcustomerCode){
                        throw new Exception("Customer value '" + customerCode + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
					}
					
					CustUtil custUtil = new CustUtil();
					boolean isExistCustomer = false;
					isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system at Line "+linecnt+" .");
					}
					
					ArrayList arrLists = custUtil.getCustomerListStartsWithName(customerCode,Plant);
					Map mCust=(Map)arrLists.get(0);
					customerCode=(String) mCust.get("CUSTNO");
   					
   					String shippingcustomer = StrUtils.replaceStr2Char(((String) list.get(1)).trim()); //new
   					
   					//Shipping customer validation
   					String shippingid = "";
   					if (("").equals(shippingcustomer)|| ("null").equals(shippingcustomer)){
//   						ArrayList arrList = custUtil.getCustomerListStartsWithName(customerCode,Plant);
//							Map mCustot=(Map)arrList.get(0);
							shippingcustomer=(String) mCust.get("CNAME");
   					}else {
   						
   						boolean isValidShippingCustomer = false;
   						if(SHIPPING_CUSTOMER_LEN >= shippingcustomer.length()){
   							isValidShippingCustomer = true;
   						}
   						if(!isValidShippingCustomer){
   							throw new Exception("Shipping Customer value :  '" + shippingcustomer + "' length has exceeded the maximum value of "+SHIPPING_CUSTOMER_LEN+" Chars at Line "+linecnt+".");
   						} 
   						
   						boolean isExistCust= false;
   						if(shippingcustomer.length()>0){
   						isExistCust = custUtil.isExistCustomer(shippingcustomer, Plant);
   						if(!isExistCust){
   							throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system at Line "+linecnt+" .");
   						}else {
   							ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
   							Map mCustot=(Map)arrList.get(0);
   							shippingid=(String) mCustot.get("CUSTNO");
   						}
   						}
   					}
   					
   					String project = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
   				//Project validation
   					String projectid = "";
   					boolean isValidproject = false;
   					if(DET_DESC_LEN >= project.length()){
   						isValidproject = true;
   					}
   					if(!isValidproject){
   						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
   					}
   					
   					if (project.equalsIgnoreCase("null")|| project == null)
   						project = "";
   					
   					boolean isExistVendor = false;
   					FinProjectDAO finProjectDAO = new FinProjectDAO();
   					if(project.length()>0){
   						isExistVendor = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
   					if(!isExistVendor){
   						throw new Exception("Project '" + project + "' is not created yet on system.");
   					}else {
   						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
   						projectid = String.valueOf(finpro.get(0).getID());
   					}
   					}
   					
   					String transportmode = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
   					
   					TransportModeDAO transportmodedao = new TransportModeDAO();
                    String trans = "";
					if(transportmode.length()>0){
						trans = transportmodedao.getTransportModeByName(Plant,transportmode);
					}
					else {
						
						transportmode = "";
					}
					if(transportmode.equals("null")) {
						transportmode = "" ;
						trans = "";
					}
					
                    if (trans == null){
							throw new Exception("Create Transport Mode : '" + transportmode + "' first, before adding Transport at Line "+linecnt+".");
					}
   					
   					
   					
   					
   					String paymentterms = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
   					
   					// payment terms validation
   					if (("").equals(paymentterms)|| ("null").equals(paymentterms)){
   				    }else {
   				    	boolean isValidpaymentterms = false;
						if(DET_DESC_LEN >= paymentterms.length()){
							isValidpaymentterms = true;
						}
						if(!isValidpaymentterms){
							throw new Exception("Payment Terms value :  '" + paymentterms + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
						}
   				    	PayTermsUtil payTermsUtil = new PayTermsUtil();
   						Hashtable ht=new Hashtable();
   						ht.put("PLANT",Plant);
   					    ht.put("TERMS",paymentterms);
   					    List listQry = payTermsUtil.getPaymentTermsDetails(ht);
   					    if (listQry.size() == 0) {
   					    	throw new Exception("Payment term '" + paymentterms + "' is not created yet on system.");
   					    }
   				    }
   					
   					ArrayList arrCustCUS = custUtil.getCustomerDetails(customerCode,Plant);
					 String paytype = (String) arrCustCUS.get(18);
					  if(paymentterms.equalsIgnoreCase("null")|| paymentterms == null || paymentterms.equalsIgnoreCase("")) {
						  paymentterms= (String) arrCustCUS.get(59);
					  }
					  if(trans.equalsIgnoreCase("null")|| trans == null || trans.equalsIgnoreCase("")) {
						  trans= (String) arrCustCUS.get(57);
					  }
					  
   					
   					//InvoiceDAO invoiceDAO = new InvoiceDAO();
   					boolean invcheck = invoiceDAO.isExisitInv(Plant, invoice);
   					if(invcheck) {
   						throw new Exception("Invoice Number '" + invoice + "' already exists in the system at Line "+linecnt+" .");
   					}

   					String dono = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
   					
   					// DONO field data validation
   					DOUtil doUtil = new DOUtil();
   					if (("").equals(dono)|| ("null").equals(dono)){
   					       
   				    }else {
   				  // DONO field data validation
   						boolean isValidDonoData = false;
   						if(DONO_LEN >= dono.length()){
   							isValidDonoData = true;
   						}
   						if(!isValidDonoData){
   							throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
   						}
   						
   						// validation against database entries
   						// DONO number validation with status
   						boolean isNewStatusDoNo = false;
   						isNewStatusDoNo = doUtil.isExistDONO(dono,Plant);
   	                    if(!isNewStatusDoNo){
   	                            throw new Exception("Order Number'" +  dono + "' doesn't Exists at Line "+linecnt+" ."); 
   	                    }
   				    	
   				    }
   					
   					String gino = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
   					
   					// gino validation
   					if (("").equals(gino)|| ("null").equals(gino)){
   						if(isinvty.equalsIgnoreCase("1")) {
   							throw new Exception("GINO Number value is mandatory and  missing at Line "+linecnt+" .");  
   						}
   				       
   						if (("").equals(dono)|| ("null").equals(dono) || dono.equalsIgnoreCase(null)){
   							 
   						}else {
   							throw new Exception("GINO Number value is mandatory and  missing at Line "+linecnt+" .");
   						}
   				    }else {
   				    	boolean isValidgrno = false;
   						if(20 >= gino.length()){
   							isValidgrno = true;
   						}
   						if(!isValidgrno){
   							throw new Exception("GINO value :  '" + gino + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
   						}
   						
   						boolean ginocheck = invoiceDAO.isExisitgino(Plant, gino);
   	   					if(ginocheck) {
   	   						throw new Exception("GINO Number '" + gino + "' already exists in the system at Line "+linecnt+" .");
   	   					}
   						
   				    }
   					
   					
   				    String invoicedate = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
   				    
   				    // Invoice date validation
   					if (("").equals(invoicedate)|| ("null").equals(invoicedate)){
   				        throw new Exception("Invoice Date value is mandatory and  missing at Line "+linecnt+" .");  
   				    }else {
   				    	boolean isCorrectBillDateFormat = false;
   						isCorrectBillDateFormat = impUtil.isCorrectDateFormatUsingRegex(invoicedate);
   						if(!isCorrectBillDateFormat){
   							throw new Exception("Invoice Date '" + invoicedate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
   						}
   					}
   					
   					
   					String duedate = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
   					
   					  // Due date validation
   					if (("").equals(duedate)|| ("null").equals(duedate)){
   						duedate = "";
   				    }else {
   				    	boolean isCorrectDueDateFormat = false;
   				    	isCorrectDueDateFormat = impUtil.isCorrectDateFormatUsingRegex(duedate);
   						if(!isCorrectDueDateFormat){
   							throw new Exception("Due Date '" + duedate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
   						}
   					}
   					
   					String ordertype = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
   				// order type validation
					if (!ordertype.equalsIgnoreCase("null") && ordertype != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(ordertype, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + ordertype + "' is not created yet on system at Line "+linecnt+".");
						}
					}
   					
   					String employeename = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
   					 
   					 // Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
						 	boolean isValidEmployee = false;
							if(20 >= employeename.length()){
								isValidEmployee = true;
							}
							if(!isValidEmployee){
		                        throw new Exception("Employee value '" + employeename + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
							}
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee '" +  employeename + "'  is not created yet on system at Line "+linecnt+".");
				    	    }
						}
   					
   					String salesloc = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
   					
   					// sales location field validation	
   					if(ispuloc) {
   		                   if (("").equals(salesloc)|| ("null").equals(salesloc)){
   						        throw new Exception("Sales Location value is mandatory and  missing at Line "+linecnt+" .");  
   						    }
   	                }
   	                if(ispuloc) {
   					
   						boolean isValidPurchaseLocation = false;
   						if(PURCHASE_LOCATION >= salesloc.length()){
   							isValidPurchaseLocation = true;
   						}
   						if(!isValidPurchaseLocation){
   	                        throw new Exception("Sales Location value '" + salesloc + "' length has exceeded the maximum value of "+PURCHASE_LOCATION+" Chars at Line "+linecnt+".");
   						}
   						MasterUtil _MasterUtil=new  MasterUtil();
   		     		   	ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesloc,Plant,COUNTRYCODE);
   		     		   	if(ccList.size()>0) {
   		     		   		for (int i = 0; i < ccList.size(); i++) {
   		     		   			Map map1 = (Map) ccList.get(i);
   		     		   			plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
   		     		   		}
   		     		   }else {
   		     			  throw new Exception("Enter Valid Sales Location  : '" + salesloc + "', before adding bill at Line "+linecnt+".");
   		     		   }
   	                }
   					
   					String currencyID = StrUtils.replaceStr2Char(((String) list.get(13)).trim());
   					
   					// Currency ID validation
   				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
   				            boolean isExistCurID = false;
   				            CurrencyUtil currUtil = new CurrencyUtil();
                                               
                                               Hashtable ht = new Hashtable();
                                               ht.put(IDBConstants.PLANT, Plant);
                                               ht.put(IDBConstants.CURRENCYID, currencyID );
   				            isExistCurID = currUtil.isExistCurrency(ht,"");
   				            if(!isExistCurID){
   				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
   				            }
   				    }
   				    
   				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
   			            currencyID = baseCurrency;
   					
   					String equivalentCurrency = StrUtils.replaceStr2Char(((String) list.get(14)).trim());
   					
   					 // Equivalent Currency validation
   				    boolean isequivalentCurrencyFormat = false;
   				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
   						
   				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
   							if(!isequivalentCurrencyFormat){
   								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
   							}
   						
   				    }
   				    else
   				    {
   				    	CurrencyDAO currencyDAO = new CurrencyDAO();
   				    	List curQryList=new ArrayList();
   				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
   				    	for(int i =0; i<curQryList.size(); i++) {
   				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
   				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
   				            }
   				    }
   		
   					
   					String tax = StrUtils.replaceStr2Char(((String) list.get(15)).trim()); 
   					
					// tax field validation
					if (("").equals(tax) || ("null").equals(tax)) {
						throw new Exception("Tax value is mandatory and  missing at Line " + linecnt + " .");
					}

					boolean isValidtax = false;
					if (REMARKS_LEN >= tax.length()) {
						isValidtax = true;
					}
					if (!isValidtax) {
						throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "
								+ REMARKS_LEN + " Chars at Line " + linecnt + ".");
					}
					String outbound_gst = new selectBean().getGST("SALES", Plant);
					FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
					List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("OUTBOUND", COUNTRYCODE,
							tax);
					if (taxtypes.size() > 0) {
						for (FinCountryTaxType finCountryTaxType : taxtypes) {

							taxid = String.valueOf(finCountryTaxType.getID());
							String display = "";
							if (finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
								display = finCountryTaxType.getTAXTYPE();
							} else if (finCountryTaxType.getSHOWSTATE() == 0
									&& finCountryTaxType.getSHOWPERCENTAGE() == 1
									&& finCountryTaxType.getISZERO() == 0) {
								display = finCountryTaxType.getTAXTYPE() + " [" + outbound_gst + "%]";
							} else if (finCountryTaxType.getSHOWSTATE() == 0
									&& finCountryTaxType.getSHOWPERCENTAGE() == 1
									&& finCountryTaxType.getISZERO() == 1) {
								display = finCountryTaxType.getTAXTYPE() + " [0.0%]";
							} else if (finCountryTaxType.getSHOWSTATE() == 1
									&& finCountryTaxType.getSHOWPERCENTAGE() == 0) {
								if (plantstatecode.equalsIgnoreCase("")) {
									display = finCountryTaxType.getTAXTYPE();
								} else {
									display = finCountryTaxType.getTAXTYPE() + "(" + plantstatecode + ")";
								}
							} else if (finCountryTaxType.getSHOWSTATE() == 1
									&& finCountryTaxType.getSHOWPERCENTAGE() == 1
									&& finCountryTaxType.getISZERO() == 1) {
								if (plantstatecode.equalsIgnoreCase("")) {
									display = finCountryTaxType.getTAXTYPE() + " [0.0%]";
								} else {
									display = finCountryTaxType.getTAXTYPE() + "(" + plantstatecode + ")" + " [0.0%]";
								}
							} else {
								if (plantstatecode.equalsIgnoreCase("")) {
									display = finCountryTaxType.getTAXTYPE() + " [" + outbound_gst + "%]";
								} else {
									display = finCountryTaxType.getTAXTYPE() + "(" + plantstatecode + ")" + " ["
											+ outbound_gst + "%]";
								}
							}

							tax = display;

						}
					} else {
						throw new Exception(
								"Enter Valid Tax  : '" + tax + "', before adding invoice at Line " + linecnt + ".");
					}
   					
   					
   					
   					String productRatesAre = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
   					
   					// Product Rates Are field validation
   					if (("").equals(productRatesAre) || ("null").equals(productRatesAre)) {
   						productRatesAre = "Tax Exclusive";
   					}
   					boolean isValidProductRatesAre = false;
   					if (EMPTYPE_LEN >= productRatesAre.length()) {
   						isValidProductRatesAre = true;
   					}
   					if (!isValidProductRatesAre) {
   						throw new Exception("Product Rates Are value '" + productRatesAre
   								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
   								+ linecnt + ".");
   					}

   					if (productRatesAre.length() > 0) {
   						if (productRatesAre.equalsIgnoreCase("Tax Inclusive")) {
   						} else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")) {
   						} else
   							throw new Exception("Product Rates Are can't be '" + productRatesAre
   									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding invoice at Line "
   									+ linecnt + ".");
   					}
   				  
   					
   					String item = StrUtils.replaceStr2Char(((String) list.get(17)).trim());
   					
   					// item validation
   					if (("").equals(item) || ("null").equals(item)) {
   						throw new Exception("Product ID value is mandatory and  missing at Line " + linecnt + " .");
   					}
   					boolean isValiditem = false;
					if(20 >= item.length()){
						isValiditem = true;
					}
					if(!isValiditem){
                        throw new Exception("Product ID value '" + item + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
					}
   					boolean isExistItem = false;
   					ItemUtil itemUtil = new ItemUtil();
   					Hashtable htitem = new Hashtable();
   					htitem.put("PLANT", Plant);
   					htitem.put("ITEM", item);
   					htitem.put("ISACTIVE", "Y");
   					isExistItem = itemUtil.isExistsItemMst(htitem);
   					if (!isExistItem) {
   						throw new Exception(
   								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
   					}	  
   					
   					String lnno = StrUtils.replaceStr2Char(((String) list.get(18)).trim());
   					
   					// is valid Line No
   					if (("").equals(lnno) || ("null").equals(lnno)) {
   						throw new Exception("LNNO value is mandatory and  missing at Line " + linecnt + " .");
   					}
   					boolean isValidlnno = false;
					if(4 >= lnno.length()){
						isValidlnno = true;
					}
					if(!isValidlnno){
                        throw new Exception("LNNO value '" + lnno + "' length has exceeded the maximum value of 4 Chars at Line "+linecnt+".");
					}
   					int isThisANumber = 0;
   					try {
   						isThisANumber = Integer.parseInt(lnno);
   					} catch (NumberFormatException nfe) {
   						throw new Exception("LNNO value :  '" + lnno + "' for invoice " + invoice
   								+ " is not a valid number  at Line " + linecnt + ".");
   					}
   					
   					String account = StrUtils.replaceStr2Char(((String) list.get(19)).trim());
   					
   					// Account field data validation
   					boolean isValidAccount = false;
   					if(PO_REMARK_LEN >= account.length()){
   						isValidAccount = true;
   					}
   					if(!isValidAccount){
   						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "+PO_REMARK_LEN+" Chars at Line "+linecnt+".");
   					}
   					
   					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
   						account = "Local sales - retail";
   					}else {
   					
   						CoaDAO coadao = new CoaDAO();
   						List<Map<String, String>> listQry = null;
   						Map<String, List<Map<String, String>>> listGrouped=null;
   						listQry = coadao.selectSubAccountTypeList(Plant,account);
   						   if(listQry.size()>0)
   						   {
   							   String[] filterArray = {"8"};
   							   String[] detailArray= {"26","25"};
   								listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
   										.collect(Collectors.toList());
   								listGrouped=listQry.stream()
   										.filter(x->Arrays.stream(filterArray)
   										.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
   										.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
   								
   								if(listGrouped.size()>0)
   								   {
   								   
   								   }
   								else
   								   {
   									  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
   								   }
   						   }
   						   else
   						   {
   							  throw new Exception("Enter Valid Account Name : '" + account + "' at Line "+linecnt+".");
   						   }
   					}
   					
   					String uom = StrUtils.replaceStr2Char(((String) list.get(20)).trim());
   					
   					// UOM field validation
   					ItemMstUtil itemmastUtil = new ItemMstUtil();
   					boolean isValidUomData = false;
   					 if(UOM >= uom.length()){
   					         isValidUomData = true;
   					 }
   					 if(!isValidUomData){
   					         throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of "+UOM+" Chars at Line "+linecnt+".");
   					 }
   					 if (!uom.equalsIgnoreCase("null") && uom != null){
   					 boolean isExistUom =false;
   					 htitem.put("STKUOM",uom);
   					
   							UomUtil uomUtil = new UomUtil();
   							isExistUom = uomUtil.isExistsUom(uom, Plant);
   							if(!isExistUom){
   								throw new Exception("UOM '" + uom + "' is not created/in active at line "+linecnt+".");
   							}
   					
   					 }else {
   						 //ArrayList listQry = itemmastUtil.getProductDetailsWithStockonHand(item,Plant," ");//Azees Fix Query Load 09.22
				        	//if (listQry.size() > 0) {
				        		//for(int i =0; i<listQry.size(); i++) {
					        		//Map um=(Map)listQry.get(i);
					        		//uom = StrUtils.fString((String)um.get("STKUOM"));
				        		//}
				        	//}
   						String suom =new ItemMstDAO().getItemSalesUOM(Plant,item);
   						uom=(String)suom;
   					 }
   					
   					String locationid  = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
   					
   					//location
   					if (("").equals(locationid) || ("null").equals(locationid)) {
   						if(isinvty.equalsIgnoreCase("1")) {
   							throw new Exception("Location value is mandatory and  missing at Line "+linecnt+" .");  
   						}
   					}else {
   						boolean isValidlocationid = false;
						if(20 >= locationid.length()){
							isValidlocationid = true;
						}
						if(!isValidlocationid){
	                        throw new Exception("Location value '" + locationid + "' length has exceeded the maximum value of 20 Chars at Line "+linecnt+".");
						}
   						LocMstDAO  _LocMstDAO  = new LocMstDAO(); 
   						List listloc =  _LocMstDAO.getLocByWMSImport(Plant,USERID,locationid);
   						if(listloc.size() == 0){
   							throw new Exception("Location '" + locationid + "' is not created yet on system at Line "+linecnt+".");
   						}
   					}
   					
   					String batch = StrUtils.replaceStr2Char(((String) list.get(22)).trim());
   					
   					//batch
   					if (("").equals(batch) || ("null").equals(batch)) {
   						if(isinvty.equalsIgnoreCase("1")) {
   							batch = "NOBATCH";
   						}
   					}else {
   						boolean isValidbatchData = false;
   						 if(40 >= batch.length()){
   							 isValidbatchData = true;
   						 }
   						 if(!isValidbatchData){
   						         throw new Exception("Batch value :  '" + batch + "' length has exceeded the maximum value of 40 Chars at Line "+linecnt+".");
   						 }
   					}
   					InvMstDAO invMstDAO = new InvMstDAO();
   					if(isinvty.equalsIgnoreCase("1")) {//Author: Azees  Create date: July 23,2022  Description: Fixed Import NON Inventory Validation
   					boolean batchcheck = invMstDAO.isExisitbatch(Plant, locationid, batch);
   					if(!batchcheck) {
   						throw new Exception("Enter Valid Batch Name : '" + batch + "' at Line "+linecnt+".");
   					}
   					}
   					//check shiphist table
   					if (("").equals(dono)|| ("null").equals(dono)){
					       
   				    }else {
	   					Hashtable htCondiShipHis = new Hashtable();
	   					htCondiShipHis.put("PLANT", Plant);
	   					htCondiShipHis.put("INVOICENO", gino);
	   					htCondiShipHis.put("STATUS", "C");
	   					htCondiShipHis.put("BATCH", batch);
	   					htCondiShipHis.put("LOC1", locationid);
	   					htCondiShipHis.put(IConstants.ITEM, item);
	   					htCondiShipHis.put("DONO", dono);
	   					boolean isexists = new ShipHisDAO().isExisit(htCondiShipHis, "");
	   					
	   					if (!isexists) {
							throw new Exception("Goods Issue details does not found for Sales Order/GINO/Product/Location/Batch at Line " + linecnt + ".");
						}
   				    }
   					//check shiphist table
   					
   					String qty = StrUtils.replaceStr2Char(((String) list.get(23)).trim());
   					
   					// quantity field validation
   					boolean isCorrectQuantityFormat = false;
   					if (!qty.equalsIgnoreCase("null") && qty != null) {
   						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
   						if (!isCorrectQuantityFormat) {
   							throw new Exception(
   									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
   						}
   					} else {
   						qty = "0";
   					}

   					if(isinvty.equalsIgnoreCase("1")) {
	   					int qtycheck = invMstDAO.getqty(Plant, item, locationid, batch, uom);
	   					int qtyexcel = Integer.valueOf(qty);
	   					if(qtycheck < qtyexcel) {
	   						throw new Exception("system not allow to import more than available product inventory quantity at Line "+linecnt+".");
	   					}
   					}
   					
   					String unit_price = StrUtils.replaceStr2Char(((String) list.get(24)).trim());
   					
   					// unit price field validation
   					boolean isCorrectUnitPriceFormat = false;
   					if (!unit_price.equalsIgnoreCase("null") && unit_price != null) {
   						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
   						if (!isCorrectUnitPriceFormat) {
   							throw new Exception("Unit cost value '" + unit_price + "' is not accepted format at Line "
   									+ linecnt + ".");
   						}
   					} else {
   						unit_price = "0";
   					}

   					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null) {
   						if (unit_price.contains(".")) {
   							int costdecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
   							if (costdecnum > numberOfDecimal) {
   								throw new Exception("Invalid more than " + numberOfDecimal
   										+ " digits after decimal in Unit Price at Line " + linecnt + ".");
   							}
   						}

   					}
   					
   					
   				//product validation from purchase order
					if (("").equals(dono)|| ("null").equals(dono)){
					       
				    }else {
						
				    	ShipHisDAO shipHisDAO = new ShipHisDAO();
				    	Hashtable ht=new Hashtable();
					    ht.put("S.DONO", dono);
					    ht.put("PLANT", Plant);
					    ht.put("S.INVOICENO", gino);
					    ht.put("S.ITEM", item);
					    List listQry = shipHisDAO.selectShipHis(" * ",ht,"");
						if (listQry.size() == 0) {
						    throw new Exception("Product id '" + item + "' or GINO '" + gino + "' does not exist in the sales order '" + dono + "' at Line " + linecnt + ".");
						}else {
							double recvqty =0;
					    	for(int i =0; i<listQry.size(); i++) {   
					    		Map mk=(Map)listQry.get(i);
					    		recvqty = Double.valueOf((String)mk.get("ORDQTY"));
					    	}
					    	double dqty = Double.valueOf(qty);
					    	if(dqty == recvqty) {
					    	}else {
					    		throw new Exception("Import invoice product quantity ("+dqty+") should be equal to sales Order shipment GINO quantity ("+recvqty+") at Line " + linecnt + ".");
					    	}
						}	
					    	
				    }
   					
   					String productdiscount = StrUtils.replaceStr2Char(((String) list.get(25)).trim());
   					
   					// Product Discount field data validation			
   					boolean isCorrectproductdiscount = false;
   					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
   						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
   						if(!isCorrectproductdiscount){
   							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
   						}
   					}else {
   						productdiscount = "0";
   					}
   					
   					String productdiscountPer = StrUtils.replaceStr2Char(((String) list.get(26)).trim());
   					
   					// Product Discount % field validation
   					if (productdiscountPer.equals("null") || productdiscountPer == null
   							|| productdiscountPer.equals("")) {
   						productdiscountPer = currencyID;
   					} else if (productdiscountPer.equalsIgnoreCase("Y") || productdiscountPer.equalsIgnoreCase("y")) {
   						productdiscountPer = "%";
   					} else if (productdiscountPer.equalsIgnoreCase("N") || productdiscountPer.equalsIgnoreCase("n")) {
   						productdiscountPer = currencyID;
   					} else if (!productdiscountPer.equalsIgnoreCase("N") || !productdiscountPer.equalsIgnoreCase("n")
   							|| !productdiscountPer.equalsIgnoreCase("Y") || !productdiscountPer.equalsIgnoreCase("y")) {
   						throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer
   								+ "', before adding purchase at Line " + linecnt + ".");
   					}

   					
   					String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(27)).trim());
   					
   					// order discount validation
   					boolean isCorrectOrderDiscount = false;
   					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null) {
   						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
   						if (!isCorrectOrderDiscount) {
   							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
   						}
   					} else {
   						orderdiscount = "0";
   					}
   					
   					String orderdiscountAre = StrUtils.replaceStr2Char(((String) list.get(28)).trim());
   					
   					// Order Discount Are field validation
   					if (("").equals(orderdiscountAre) || ("null").equals(orderdiscountAre)) {
   						orderdiscountAre = "Tax Inclusive";
   					}
   					boolean isValidorderdiscountAre = false;
   					if (EMPTYPE_LEN > orderdiscountAre.length()) {
   						isValidorderdiscountAre = true;
   					}
   					if (!isValidorderdiscountAre) {
   						throw new Exception("Order Discount Are value '" + orderdiscountAre
   								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
   								+ linecnt + ".");
   					}

   					if (orderdiscountAre.length() > 0) {
   						if (orderdiscountAre.equalsIgnoreCase("Tax Inclusive")) {
   						} else if (orderdiscountAre.equalsIgnoreCase("Tax Exclusive")) {
   						} else
   							throw new Exception("Order Discount Are can't be '" + orderdiscountAre
   									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding invoice at Line "
   									+ linecnt + ".");
   					}
   					
   					String orderdiscountPer = StrUtils.replaceStr2Char(((String) list.get(29)).trim());
   					
   					// Order Discount % field validation
   					if (orderdiscountPer.equals("null") || orderdiscountPer == null || orderdiscountPer.equals("")) {
   						orderdiscountPer = currencyID;
   					} else if (orderdiscountPer.equalsIgnoreCase("Y") || orderdiscountPer.equalsIgnoreCase("y")) {
   						orderdiscountPer = "%";
   					} else if (orderdiscountPer.equalsIgnoreCase("N") || orderdiscountPer.equalsIgnoreCase("n")) {
   						orderdiscountPer = currencyID;
   					} else if (!orderdiscountPer.equalsIgnoreCase("N") || !orderdiscountPer.equalsIgnoreCase("n")
   							|| !orderdiscountPer.equalsIgnoreCase("Y") || !orderdiscountPer.equalsIgnoreCase("y")) {
   						throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountPer
   								+ "', before adding invoice at Line " + linecnt + ".");
   					}
   				   
   					
   					String discount = StrUtils.replaceStr2Char(((String) list.get(30)).trim());
   					
   					//discount validation
   					boolean isCorrectDiscount = false;
   					if (!discount.trim().equalsIgnoreCase("null") && discount != null) {
   						isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
   						if (!isCorrectDiscount) {
   							throw new Exception("Discount value '" + discount + "' is not accepted format.");
   						}
   					} else {
   						discount = "0";
   					}
   					String discountAre = StrUtils.replaceStr2Char(((String) list.get(31)).trim());
   					
   					//Discount Are field validation
   					if (("").equals(discountAre) || ("null").equals(discountAre)) {
   						discountAre = "Tax Inclusive";
   					}
   					boolean isValiddiscountAre = false;
   					if (EMPTYPE_LEN > discountAre.length()) {
   						isValiddiscountAre = true;
   					}
   					if (!isValiddiscountAre) {
   						throw new Exception("Discount Are value '" + discountAre
   								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
   								+ linecnt + ".");
   					}

   					if (discountAre.length() > 0) {
   						if (discountAre.equalsIgnoreCase("Tax Inclusive")) {
   						} else if (discountAre.equalsIgnoreCase("Tax Exclusive")) {
   						} else
   							throw new Exception("Discount Are can't be '" + discountAre
   									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding invoice at Line "
   									+ linecnt + ".");
   					}
   					
   					String discountPer = StrUtils.replaceStr2Char(((String) list.get(32)).trim());
   					
   					// Discount % field validation
   					if (discountPer.equals("null") || discountPer == null || discountPer.equals("")) {
   						discountPer = currencyID;
   					} else if (discountPer.equalsIgnoreCase("Y") || discountPer.equalsIgnoreCase("y")) {
   						discountPer = "%";
   					} else if (discountPer.equalsIgnoreCase("N") || discountPer.equalsIgnoreCase("n")) {
   						discountPer = currencyID;
   					} else if (!discountPer.equalsIgnoreCase("N") || !discountPer.equalsIgnoreCase("n")
   							|| !discountPer.equalsIgnoreCase("Y") || !discountPer.equalsIgnoreCase("y")) {
   						throw new Exception("Enter Valid Discount %  : '" + discountPer
   								+ "', before adding invoice at Line " + linecnt + ".");
   					}
   					
   					
   					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(33)).trim());
   					
   					// shipping cost validation
   					boolean isCorrectShippingCost = false;
   					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
   						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
   						if(!isCorrectShippingCost){
   							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
   						}
   					}else {
   						shippingcost = "0";
   					}
   					
   					String shippingcostAre = StrUtils.replaceStr2Char(((String) list.get(34)).trim());
   					
   					// Shipping Cost Are field validation
   					if (("").equals(shippingcostAre) || ("null").equals(shippingcostAre)) {
   						shippingcostAre = "Tax Inclusive";
   					}
   					boolean isValidShippingCostAre = false;
   					if (EMPTYPE_LEN > shippingcostAre.length()) {
   						isValidShippingCostAre = true;
   					}
   					if (!isValidShippingCostAre) {
   						throw new Exception("Shipping Cost Are value '" + shippingcostAre
   								+ "' length has exceeded the maximum value of " + EMPTYPE_LEN + " Chars at Line "
   								+ linecnt + ".");
   					}

   					if (shippingcostAre.length() > 0) {
   						if (shippingcostAre.equalsIgnoreCase("Tax Inclusive")) {
   						} else if (shippingcostAre.equalsIgnoreCase("Tax Exclusive")) {
   						} else
   							throw new Exception("Shipping Cost Are can't be '" + shippingcostAre
   									+ "',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding invoice at Line "
   									+ linecnt + ".");
   					}
   					
   					String adjustment = StrUtils.replaceStr2Char(((String) list.get(35)).trim());
   					
   					//adjustment validation
   					boolean isCorrectadjustment = false;
   					if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
   						isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
   						if(!isCorrectadjustment){
   							throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
   						}
   					}else {
   						adjustment = "0";
   					}
   					
   					String notes = StrUtils.replaceStr2Char(((String) list.get(36)).trim());
   					
   					
   					// Notes field data validation
   					boolean isValidRemarksData = false;
   					if(1000 >= notes.length()){
   						isValidRemarksData = true;
   					}
   					if(!isValidRemarksData){
   						throw new Exception("Notes value '" + notes + "' length has exceeded the maximum value of 1000 Chars at Line "+linecnt+".");
   					}
   					
   					String termsandcondition = StrUtils.replaceStr2Char(((String) list.get(37)).trim());
   					
   					// terms and condition field data validation
   					boolean isValidRemarks1Data = false;
   					if(1000 >= notes.length()){
   						isValidRemarks1Data = true;
   					}
   					if(!isValidRemarks1Data){
   						throw new Exception("Terms and condition value '" + termsandcondition + "' length has exceeded the maximum value of 1000 Chars at Line "+linecnt+".");
   					}
   					
   					
   					
   					m.put("PLANT", Plant);
   					m.put("ISINVENTORY", isinvty);
   					m.put("CUSTOMERCODE", customerCode);
   					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
   						shippingcustomer = "";
   					m.put("SHIPPINGCUSTOMER", shippingcustomer);
   					m.put("SHIPPINGID", shippingid);
   					m.put(IConstants.TRANSPORTID, trans);
   					m.put("PROJECTID", projectid);
   					m.put("PAYMENTTERMS", paymentterms);
   					m.put("INVOICE", invoice);
   					m.put("GINO", gino);
   					m.put("DONO", dono);
   					m.put("INVOICEDATE", invoicedate);
   					m.put("DUEDATE", duedate);
   					m.put("SALESLOC", salesloc);
   					m.put("CURRENCYID", currencyID);
   					m.put("ORDERTYPE", ordertype);
   					m.put("EMPLOYEEID", employeename);
   					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
   					m.put("TAXPERCENTAGE", outbound_gst);
   					m.put("TAX", tax);
   					m.put("TAXID", taxid);
   					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
   					{
   						productRatesAre="1";
   					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
   					{
   						productRatesAre="0";
   					}else {
   						productRatesAre="0";
   					}
   					m.put("ISTAXINCLUSIVE", productRatesAre);
   					m.put("PRODUCT", item);
   					m.put("LNNO", lnno);
   					m.put("ACCOUNT", account);
   					m.put("UOM", uom);
   					m.put("LOCATIONID", locationid);
   					m.put("BATCH", batch);
   					m.put("QTY", qty);
   					if(!equivalentCurrency.equalsIgnoreCase("")) {
   						unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
   					}
   					m.put("UNITPRICE", unit_price);
   					if(!productdiscountPer.equalsIgnoreCase("%")) {
   						if(!equivalentCurrency.equalsIgnoreCase("")) {
   							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
   						}
   					}
   					m.put("PRODUCTDISCOUNT", productdiscount);
   					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
   					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
   						orderdiscount = "0";
   					if(!orderdiscountPer.equalsIgnoreCase("%")) {
   						if(!equivalentCurrency.equalsIgnoreCase("")) {
   							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
   						}
   					}
   					m.put("ORDERDISCOUNT", orderdiscount);
   					
   					if (orderdiscountAre.equalsIgnoreCase("Tax Inclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Inclusive"))
   					{
   						orderdiscountAre="1";
   					}else if (orderdiscountAre.equalsIgnoreCase("Tax Exclusive")|| orderdiscountAre.equalsIgnoreCase("Tax Exclusive"))
   					{
   						orderdiscountAre="0";
   					}else {
   						orderdiscountAre="0";
   					}
   					m.put("ISORDERDISCOUNTTAX", orderdiscountAre);
   					m.put("ORDERDISCOUNTTYPE", orderdiscountPer);
   					
   					
   					if (discount.equalsIgnoreCase("null")|| discount == null)
   						discount = "0";
   					if(!discountPer.equalsIgnoreCase("%")) {
   						if(!equivalentCurrency.equalsIgnoreCase("")) {
   							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
   						}
   					}
   					m.put("DISCOUNT", discount);
   					
   					if (discountAre.equalsIgnoreCase("Tax Inclusive"))
   					{
   						discountAre="1";
   					}else if (discountAre.equalsIgnoreCase("Tax Exclusive"))
   					{
   						discountAre="0";
   					}else {
   						discountAre="0";
   					}
   					m.put("ISDISCOUNTTAX", discountAre);
   					m.put("DISCOUNTTYPE", discountPer);
   					if(!equivalentCurrency.equalsIgnoreCase("")) {
   						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
   					}
   					m.put("SHIPPINGCOST", shippingcost);
   					if (shippingcostAre.equalsIgnoreCase("Tax Inclusive"))
   					{
   						shippingcostAre="1";
   					}else if (shippingcostAre.equalsIgnoreCase("Tax Exclusive"))
   					{
   						shippingcostAre="0";
   					}else {
   						shippingcostAre="0";
   					}
   					m.put("ISSHIPPINGTAX", shippingcostAre);
   					if(!equivalentCurrency.equalsIgnoreCase("")) {
   						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
   					}
   					m.put("ADJUSTMENT", adjustment);
   					m.put("NOTES", notes);
   					m.put("TERMSANDCONDITION",termsandcondition);
   				}

   				hdrList.add(m);

   				System.out.println("hdrList :: " + hdrList.size());
   			}

   		} catch (Exception ex) {
   			// ex.printStackTrace();
   			throw ex;
   		} finally {

   		}
   		
   	//Validate Order azees - 08/2022
   	if(!ValidNumber.equalsIgnoreCase("")) {
	Map mv = new HashMap();
    mv.put("ValidNumber", ValidNumber);
    hdrList.add(mv);
   		}
   		
   		return hdrList;

   	}
    
    
    @SuppressWarnings("unchecked")
	public ArrayList downloadOutboundSheetDataShopify(String Plant, String filename,
			String sheetName,String baseCurrency,String NOOFORDER,String _login_user) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		DOUtil doUtil = new DOUtil();
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
	    String ValidNumber="";
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFileShopify(Integer
					.parseInt(ShopifyNumberOfColumn),"SPY");
			System.out
					.println("Total number of record found in Excel sheet  :  "
							+ str.length);

			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid = new DoHdrDAO().Salescount(Plant,FROM_DATE,TO_DATE);
			int convl = 0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				convl = Integer.valueOf(NOOFORDER);				
			}
			String validpo="";
 			int validcot=0;
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());

				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
 					//Validate Order azees - 12/2020
 					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
 					{
 					int ddok =0;
 					if(validpo.equalsIgnoreCase(""))
 					{
 						validpo=dono;
 						ddok=1;
 						
 					}
 					else {
 					if(validpo.equalsIgnoreCase(dono))
 					{
 						validcot=validcot+1;
 						ddok=validcot-1;
 						if(validcot>0)
 							validcot=validcot-1;
 					}
 					else {
 						validpo=dono;
 						ddok=1;
 						
 					}
 					}
 					
 					novalid=novalid+ddok;
 					
 						if(novalid>convl)
 						{
 							ValidNumber=NOOFORDER;
 							break;
 						}
 					}
 					//End
					String customername = StrUtils.replaceStr2Char(((String) list.get(12)).trim());
					String customerCode = "";
					CustUtil custUtil = new CustUtil();
					if (("").equals(customername)|| ("null").equals(customername)){
					    throw new Exception("Customer name value is mandatory and  missing at Line "+linecnt+" .");  
					}
					
					boolean isExistCustomer = false;
					if(customername.length()>0){
						isExistCustomer = custUtil.isExistCustomerName(customername, Plant);
					if(!isExistCustomer){
						//create customer
						
							  TblControlDAO _TblControlDAO = new TblControlDAO();
					          Hashtable ht = new Hashtable();
					          ht.put(IConstants.PLANT,Plant);
					          String sCustCode = Autocustno(Plant,_login_user);
					          ht.put(IConstants.CUSTOMER_CODE,sCustCode);
					          ht.put("USER_ID",sCustCode);
					          ht.put(IConstants.CUSTOMER_NAME,customername);
					          ht.put(IConstants.COUNTRY,StrUtils.replaceStr2Char(((String) list.get(19)).trim()));
					          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
					          ht.put(IConstants.CREATED_BY,_login_user);
					          ht.put(IConstants.ISACTIVE,"Y");
					          ht.put("CREDITLIMIT","0");
					          ht.put("ISCREDITLIMIT","N");
					          ht.put(IConstants.CREDIT_LIMIT_BY,"NOLIMIT");
					          ht.put(IConstants.OPENINGBALANCE,"0");
					          ht.put(IConstants.TAXTREATMENT, "Non GST Registered");
					          ht.put(IConstants.companyregnumber,"");
					          ht.put(IConstants.ADDRESS1,StrUtils.replaceStr2Char(((String) list.get(16)).trim()));
					          ht.put(IConstants.ADDRESS2,StrUtils.replaceStr2Char(((String) list.get(13)).trim()));
					          ht.put(IConstants.ADDRESS3,StrUtils.replaceStr2Char(((String) list.get(14)).trim()));
					          ht.put(IConstants.ADDRESS4,StrUtils.replaceStr2Char(((String) list.get(15)).trim()));
					          ht.put(IConstants.ZIP,StrUtils.replaceStr2Char(((String) list.get(18)).trim()));
					          ht.put(IConstants.EMAIL,StrUtils.replaceStr2Char(((String) list.get(1)).trim()));

					         
					          /*  */

					          MovHisDAO mdao = new MovHisDAO(Plant);
					          Hashtable htm = new Hashtable();
					          htm.put(IDBConstants.PLANT,Plant);
					          htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_CUST);
					          htm.put("RECID","");
					          htm.put("ITEM",sCustCode);
					          htm.put(IDBConstants.CREATED_BY,_login_user);
					          htm.put(IDBConstants.REMARKS, customername);
					          htm.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
					          htm.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
					          
							  boolean updateFlag;
							if(sCustCode!="C0001")
					  		  {	
								boolean exitFlag = false;
								Hashtable htv = new Hashtable();				
								htv.put(IDBConstants.PLANT, Plant);
								htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
								exitFlag = _TblControlDAO.isExisit(htv, "", Plant);
								if (exitFlag) 
					  		    updateFlag=_TblControlDAO.updateSeqNo("CUSTOMER",Plant);
								else
								{
									boolean insertFlag = false;
									Map htInsert=null;
					            	Hashtable htTblCntInsert  = new Hashtable();           
					            	htTblCntInsert.put(IDBConstants.PLANT,Plant);          
					            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
					            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
					             	htTblCntInsert.put("MINSEQ","0000");
					             	htTblCntInsert.put("MAXSEQ","9999");
					            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
					            	htTblCntInsert.put(IDBConstants.CREATED_BY, _login_user);
					            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
					            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,Plant);
								}
							}
							  
					          boolean custInserted = custUtil.insertCustomer(ht);
					          boolean  inserted = mdao.insertIntoMovHis(htm);
					          if(custInserted&&inserted) {
					        	customerCode=sCustCode;
								customername =customername;
					          }
					    
						
						//create customer
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(customername,Plant);
						Map mCustot=(Map)arrList.get(0);
						customerCode=(String) mCustot.get("CUSTNO");
						customername = (String) mCustot.get("CNAME");
					}
					}
					
					
					String shippingcustomer = "";
					String project = "";
					String paymenttype = "";
					String refNo = "";
					String order_date = StrUtils.replaceStr2Char(((String) list.get(7)).trim());
					String time = "null";
					String order_type = "null";
					String deliverydate = "";
				    String deliverybydate = "";
				    double subtotal = Double.valueOf((String) list.get(3));
				    double taxa = Double.valueOf((String) list.get(5));
				    double outgst = (taxa * 100)/subtotal;
					String outbound_gst = String.valueOf(outgst);
					String employeename = "null";
					String incoterm = "null";
					String salesLocation = "null";
					String currencyID = StrUtils.replaceStr2Char(((String) list.get(2)).trim());
					String equivalentCurrency = "1";
					String tax = "";
					if(taxa == 0) {
						tax = "ZERO RATE";
					}else {
						tax = "STANDARD RATED";
					}
					
					String productRatesAre = "Tax Exclusive";
					String item = StrUtils.replaceStr2Char(((String) list.get(11)).trim());
					String doLno = StrUtils.replaceStr2Char(((String) list.get(21)).trim());
					String account = "Local sales - retail";
					String uom = "null";
					ItemMstUtil itemmastUtil = new ItemMstUtil();
//					ArrayList listQryuom = itemmastUtil.getProductDetailsWithStockonHand(item, Plant, " ");//Azees Fix Query Load 09.22
//					if (listQryuom.size() > 0) {
//						for (int i = 0; i < listQryuom.size(); i++) {
//							Map um = (Map) listQryuom.get(i);
//							uom = StrUtils.fString((String) um.get("STKUOM"));
//						}
//					}
					String suom =new ItemMstDAO().getItemSalesUOM(Plant,item);
						uom=(String)suom;
					String qty = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String productdeliverydate = "null";
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(10)).trim());
					String productdiscount = "0"; 
					String productdiscountPer = "Y";
					String orderdiscount = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String orderdiscounttaxinc= "Tax Exclusive";
					String orderdiscountpre = "N"; //new
					String discount = "0";
					String discounttaxinc = "Tax Exclusive";
					String discountpre = "Y";
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(4)).trim());
					String shippingcosttaxinc =  "Tax Exclusive";
					String adjustment = "0";
					String remarks1 = "null";
					String remarks2 = "null";
					String projectid="0";
					String shippingid="";
					String taxid="0";
					
					String Minprice = "";
					
					String COUNTRYCODE = "";
	           	  	String plantstate = "";
	           	  	String plantstatecode = "";
	                PlantMstUtil plantmstutil = new PlantMstUtil();
	                List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
	                for (int i = 0; i < viewlistQry.size(); i++) {
	           	     	Map map = (Map) viewlistQry.get(i);
	           	     	COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	           	     	plantstate = StrUtils.fString((String)map.get("STATE"));
	                }
	                
	                MasterDAO masterDAO = new MasterDAO();
	                boolean issuloc=false;		 
	           	 	ArrayList saloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
	           	 	if (saloctions.size() > 0) {
	           	 		issuloc = true;
	           	 	}
					

				    if(StrUtils.isCharAllowed(dono) || StrUtils.isCharAllowed(doLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(productRatesAre) || StrUtils.isCharAllowed(account)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(dono)|| ("null").equals(dono)){
				        throw new Exception("DONO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(doLno)|| ("null").equals(doLno)){
				        throw new Exception("DOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
				        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				   
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if(issuloc) {
					    if (("").equals(salesLocation)|| ("null").equals(salesLocation)){
					        throw new Exception("Sales Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
				    }
				    if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    
				    /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
				        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				    
				    
				    
				    
				    
				    // customer validation
					if (("").equals(customerCode)|| ("null").equals(customerCode)){
					    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}

					isExistCustomer = false;
					
					isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
					}
				    
					if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
						shippingcustomer = "";
					}
					//Shipping customer validation
					isExistCustomer = false;
					if(shippingcustomer.length()>0){
						isExistCustomer = custUtil.isExistCustomer(shippingcustomer, Plant);
					if(!isExistCustomer){
						throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system.");
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
						Map mCustot=(Map)arrList.get(0);
						shippingid=(String) mCustot.get("CUSTNO");
						shippingcustomer = (String) mCustot.get("CNAME");
					}
					}
					
					//Project validation					
					boolean isValidproject = false;
					if(DET_DESC_LEN > project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistCustomer = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistCustomer = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistCustomer){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
					// payment type data validation
					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
						paymenttype = "";
					}else {
						boolean isValidpaymenttype = false;
						if(OREDER_TYPE_LEN > paymenttype.length()){
							isValidpaymenttype = true;
						}
						if(!isValidpaymenttype){
							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
						}
						/*MasterDAO masterDAO = new MasterDAO();*/
						isValidpaymenttype=masterDAO.getPaymentTypeisExist(Plant,paymenttype);
						if(!isValidpaymenttype){
			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding sales at Line "+linecnt+".");
			     		}
					}
					
					// DONO field data validation
					boolean isValidDonoData = false;
					if(DONO_LEN >= dono.length()){
						isValidDonoData = true;
					}
					if(!isValidDonoData){
						throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
					}
					
					// validation against database entries
					// DONO number validation with status
					boolean isNewStatusDoNo = false;
					isNewStatusDoNo = doUtil.isNewStatusDONO(dono,Plant);
                    if(isNewStatusDoNo){
                            throw new Exception("DONO '" + dono + "' is closed or already moved to open satus. you can't edit on system at Line "+linecnt+" ."); 
                    }
					
					// RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(DO_REF_LEN >= refNo.length()){
				        isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
					
				    // data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					
					
					boolean isCorrectDateFormat = false;
					try {			             
			            DateFormat srcDf = new SimpleDateFormat("dd/MMM/yyyy");
			            Date date = srcDf.parse(order_date);
			            DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
			            order_date = destDf.format(date);
						
					if (!order_date.equalsIgnoreCase("null") && order_date != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
						}
					}
					}catch (Exception e) {
						throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
					}

					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!time.equalsIgnoreCase("null") && time != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// delivery date field validation
					boolean isCorrectDeliveryDateFormat = false;
					if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
						isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
						if(!isCorrectDeliveryDateFormat){
							throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					 if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
					    {
					    	deliverybydate="N";
					    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding sales at Line "+linecnt+".");  
			     		  }
					
					// order type validation
					if (!order_type.equalsIgnoreCase("null") && order_type != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
						}
					}
					
					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("VAT value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
						
					// Incoterm field validation 
					 boolean isValidINCOTERMS = false;
						if(INCOTERM_LEN > incoterm.length()){
							isValidINCOTERMS = true;
						}
						if(!isValidINCOTERMS){
							throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
						}
						
					
						// Sales Location field validation	
						boolean isValidsalesLocation = false;
							if(SALES_LOCATION > salesLocation.length()){
								isValidsalesLocation = true;
		                }
		                if(!isValidsalesLocation){
		                        throw new Exception("Sales Location value '" + salesLocation + "' length has exceeded the maximum value of "+SALES_LOCATION+" Chars at Line "+linecnt+".");
		                }
		                
		                if(issuloc) {
			               MasterUtil _MasterUtil=new  MasterUtil();
			     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesLocation,Plant,COUNTRYCODE);
			     		   if(ccList.size()>0)
			     		   {
			     			  for (int i = 0; i < ccList.size(); i++) {
			     			     Map map1 = (Map) ccList.get(i);
			     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
			     			 }
			     		   }
			     		   else
			     		   {
			     			  throw new Exception("Enter Valid Sales Location  : '" + salesLocation + "', before adding sales at Line "+linecnt+".");
			     		   }
		                }else {
		                	salesLocation = "";
		                }
					
	     		   // Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
				            
				            Hashtable ht = new Hashtable();
				            ht.put(IDBConstants.PLANT, Plant);
				            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
					
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                     currencyID = baseCurrency;
				    
				    
				    
				   // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }
					
				  // tax field validation
		     		  boolean isValidtax = false;
						if(REMARKS_LEN > tax.length()){
							isValidtax = true;
			          }
			          if(!isValidtax){
			                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("OUTBOUND", COUNTRYCODE, tax);
			          if(taxtypes.size() > 0) {
			        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
			        		
			        			taxid = String.valueOf(finCountryTaxType.getID());	        			
			        			String display="";
					         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
					         	{
					         		display = finCountryTaxType.getTAXTYPE();
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE();
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
					         		}
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
					         		}
					         	}
					         	else
					         	{
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+outbound_gst+"%]";
					         		}
					         	}
					         	
					         	tax=display;
			        			
			        		}
			          	}
			          else
			          {
			        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding sales at Line "+linecnt+".");
			          }
					
					
			       // Product Rates Are field validation
			          if (("").equals(productRatesAre)|| ("null").equals(productRatesAre) || productRatesAre == null){
			        	  productRatesAre = "Tax Exclusive";
					  }
			 		  boolean isValidProductRatesAre    = false;
						if(EMPTYPE_LEN > productRatesAre.length()){
							isValidProductRatesAre = true;
			          }
			          if(!isValidProductRatesAre){
			                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if(productRatesAre.length()>0){
			        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // is valid Line No
					    
					 int isThisANumber = 0;
					     try
					     {
					     isThisANumber = Integer.parseInt(doLno); 
					     }
					     catch (NumberFormatException nfe)
					     {
					         throw new Exception("DOLNNO value :  '" + doLno + "' for DONO "+dono+" is not a valid number  at Line "+linecnt+".");
					     }
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", item);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
					}

					// Account field data validation
					boolean isValidAccount = false;
					if (PO_REMARK_LEN > account.length()) {
						isValidAccount = true;
					}
					if (!isValidAccount) {
						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "
								+ PO_REMARK_LEN + " Chars at Line " + linecnt + ".");
					}
					
					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
						account = "";
					}else {
						CoaDAO coadao = new CoaDAO();
						List<Map<String, String>> listQry = null;
						Map<String, List<Map<String, String>>> listGrouped = null;
						listQry = coadao.selectSubAccountTypeList(Plant, account);
						if (listQry.size() > 0) {
							String[] filterArray = { "8" };
							String[] detailArray = { "26", "25" };
							listQry = listQry.stream()
									.filter(y -> Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
									.collect(Collectors.toList());
							listGrouped = listQry.stream()
									.filter(x -> Arrays.stream(filterArray).anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
									.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
	
							if (listGrouped.size() > 0) {
	
							} else {
								throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
										+ linecnt + ".");
							}
						} else {
							throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
									+ linecnt + ".");
						}
					}

					// UOM field data validation
					boolean isValidUomData = false;
					if (UOM >= uom.length()) {
						isValidUomData = true;
					}
					if (!isValidUomData) {
						throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of " + UOM
								+ " Chars at Line " + linecnt + ".");
					}
					if (!uom.equalsIgnoreCase("null") && uom != null) {
						boolean isExistUom = false;
						htitem.put("STKUOM", uom);

						UomUtil uomUtil = new UomUtil();
						isExistUom = uomUtil.isExistsUom(uom, Plant);
						if (!isExistUom) {
							throw new Exception("UOM '" + uom + "' is not created/in active at line " + linecnt + ".");
						}
					}
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null) {
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if (!isCorrectQuantityFormat) {
							throw new Exception(
									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
						}
					}
					
					// Product delivery date field validation
				    boolean isCorrectproductdeliverydate = false;
				    if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
				    	isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
						if(!isCorrectproductdeliverydate){
							throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
				    }

				    // unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if(!isCorrectUnitPriceFormat){
							throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
						}
					}
					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
						if(unit_price.contains(".")){
							int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if(pricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
							}
						}
						
					}
					
					// Product Discount field data validation			
					boolean isCorrectproductdiscount = false;
					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
						if(!isCorrectproductdiscount){
							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
						}
					}else {
						productdiscount = "0";
					}
					
					// Product Discount % field validation			
					if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
				    {
						productdiscountPer=currencyID;
				    }
				  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
					{
					  productdiscountPer="%";
					}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
					{
						productdiscountPer=currencyID;
					}
					else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
		     		  {
		     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding sales at Line "+linecnt+".");  
		     		  }
					
					
					
					
					
					// Order Discount field validation
					boolean isCorrectOrderDiscount = false;
					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
						if(!isCorrectOrderDiscount){
							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
						}
					}else {
						orderdiscount = "0";
					}
					
					// Order Discount Are field validation
			 		  boolean isValidorderdiscountAre    = false;
						if(EMPTYPE_LEN > orderdiscounttaxinc.length()){
							isValidorderdiscountAre = true;
			          }
			          if(!isValidorderdiscountAre){
			                  throw new Exception("Order Discount Are value '" + orderdiscounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if (("").equals(orderdiscounttaxinc)|| ("null").equals(orderdiscounttaxinc) || orderdiscounttaxinc == null){
			        	  orderdiscounttaxinc = "Tax Inclusive";
					  }
			          
			          if(orderdiscounttaxinc.length()>0){
			        	  if(orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Order Discount Are can't be '"+orderdiscounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // Order Discount % field validation			
						if (orderdiscountpre.equals("null") || orderdiscountpre == null || orderdiscountpre.equals("") )
					    {
							orderdiscountpre=currencyID;
					    }
					  else if (orderdiscountpre.equalsIgnoreCase("Y")|| orderdiscountpre.equalsIgnoreCase("y"))
						{
						  orderdiscountpre="%";
						}else if (orderdiscountpre.equalsIgnoreCase("N")|| orderdiscountpre.equalsIgnoreCase("n"))
						{
							orderdiscountpre=currencyID;
						}
						else if(!orderdiscountpre.equalsIgnoreCase("N")|| !orderdiscountpre.equalsIgnoreCase("n")||!orderdiscountpre.equalsIgnoreCase("Y")|| !orderdiscountpre.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountpre + "', before adding sales at Line "+linecnt+".");  
			     		  }
						
						
						
						// Discount field validation
						boolean isCorrectDiscount = false;
						if (!discount.trim().equalsIgnoreCase("null") && discount != null){
							isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
							if(!isCorrectDiscount){
								throw new Exception("Discount value '" + orderdiscount + "' is not accepted format.");
							}
						}else {
							discount = "0";
						}
						
						// Discount Are field validation
				 		  boolean isValiddiscountAre    = false;
							if(EMPTYPE_LEN > discounttaxinc.length()){
								isValiddiscountAre = true;
				          }
				          if(!isValiddiscountAre){
				                  throw new Exception("Discount Are value '" + discounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          if (("").equals(discounttaxinc)|| ("null").equals(discounttaxinc) || discounttaxinc == null){
				        	  discounttaxinc = "Tax Inclusive";
						  }
				          if(discounttaxinc.length()>0){
				        	  if(discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Discount Are can't be '"+discounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
				          
				       // Discount % field validation			
							if (discountpre.equals("null") || discountpre == null || discountpre.equals("") )
						    {
								discountpre=currencyID;
						    }
						  else if (discountpre.equalsIgnoreCase("Y")|| discountpre.equalsIgnoreCase("y"))
							{
							  discountpre="%";
							}else if (discountpre.equalsIgnoreCase("N")|| discountpre.equalsIgnoreCase("n"))
							{
								discountpre=currencyID;
							}
							else if(!discountpre.equalsIgnoreCase("N")|| !discountpre.equalsIgnoreCase("n")||!discountpre.equalsIgnoreCase("Y")|| !discountpre.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Order Discount %  : '" + discountpre + "', before adding sales at Line "+linecnt+".");  
				     		  }
							
					
					
					//shipping cost validation					
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Shipping Cost Are field validation
			 		  boolean isValidShippingCostAre    = false;
						if(EMPTYPE_LEN > shippingcosttaxinc.length()){
							isValidShippingCostAre = true;
			          }
			          if(!isValidShippingCostAre){
			                  throw new Exception("Shipping Cost Are value '" + shippingcosttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          if (("").equals(shippingcosttaxinc)|| ("null").equals(shippingcosttaxinc) || shippingcosttaxinc == null){
			        	  shippingcosttaxinc = "Tax Inclusive";
					  }
			          if(shippingcosttaxinc.length()>0){
			        	  if(shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Shipping Cost Are can't be '"+shippingcosttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
													
			        //Adjustment field validation	
						boolean isCorrectadjustment = false;
						if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
							isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
							if(!isCorrectadjustment){
								throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
							}
						}else {
							adjustment = "0";
						}
					
						// Remarks1 field data validation
						boolean isValidRemarksData1 = false;
						if(DO_REMARK_LEN1 > remarks1.length()){
							isValidRemarksData1 = true;
						}
						if(!isValidRemarksData1){
							throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
						}
						

						// Remarks1 field data validation
						boolean isValidRemarksData2 = false;
						if(DO_REMARK_LEN2 > remarks2.length()){
							isValidRemarksData2 = true;
						}
						if(!isValidRemarksData2){
							throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
						}
				
     		   
					m.put("PLANT", Plant);
					m.put(IConstants.OUT_CUST_CODE, customerCode);
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
					m.put("SHIPPINGID", shippingid);
					m.put("PROJECTID", projectid);
					m.put("TAX", tax);
					m.put("TAXID", taxid);
					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
					{
						productRatesAre="1";
					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
					{
						productRatesAre="0";
					}else {
						productRatesAre="0";
					}
					m.put("ISTAXINCLUSIVE", productRatesAre);
					m.put("ACCOUNT", account);
					m.put("PRODUCTDELIVERYDATE", productdeliverydate);
					if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null)
						productdiscount = "0";
					
					if(!productdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("PRODUCTDISCOUNT", productdiscount);

					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					
					m.put("ADJUSTMENT", adjustment);
					
					
					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
						orderdiscount = "0";
					if(!orderdiscountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.ORDERDISCOUNT, orderdiscount);
					
					if (orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						orderdiscounttaxinc="1";
					}else if (orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						orderdiscounttaxinc="0";
					}else {
						orderdiscounttaxinc="0";
					}
					m.put("ISORDERDISCOUNTTAX", orderdiscounttaxinc);
					
					m.put("ORDERDISCOUNTTYPE", orderdiscountpre);
					
					
					if (discount.equalsIgnoreCase("null")|| discount == null)
						discount = "0";
					if(!discountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.DISCOUNT, discount);
					
					if (discounttaxinc.equalsIgnoreCase("Tax Inclusive")|| discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						discounttaxinc="1";
					}else if (discounttaxinc.equalsIgnoreCase("Tax Exclusive")|| discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						discounttaxinc="0";
					}else {
						discounttaxinc="0";
					}
					m.put("ISDISCOUNTTAX", discounttaxinc);
					
					m.put("DISCOUNTTYPE", discountpre);
					
					if (shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						shippingcosttaxinc="1";
					}else if (shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						shippingcosttaxinc="0";
					}else {
						shippingcosttaxinc="0";
					}
					m.put("ISSHIPPINGTAX", shippingcosttaxinc);
					
					
					
					
					
					
					
					m.put(IConstants.OUT_DONO, dono);
				        m.put(IConstants.OUT_DOLNNO, doLno);
				    
				    if (refNo.equalsIgnoreCase("null")|| refNo == null)
				    refNo = "";
				    m.put(IConstants.OUT_REF_NO, refNo);
                                    
					if (order_type.equalsIgnoreCase("null")|| order_type == null)
					order_type = "";
					m.put(IConstants.OUT_ORDERTYPE, order_type);

				    if (order_date.equalsIgnoreCase("null")|| order_date == null)
				        order_date = DateUtils.getDate();
					m.put(IConstants.OUT_COLLECTION_DATE, order_date);
					
					if (time.equalsIgnoreCase("null")|| time == null)
				         time = DateUtils.getTimeHHmm();
					m.put(IConstants.OUT_COLLECTION_TIME, time);
					
					if (remarks1.equalsIgnoreCase("null")
							|| remarks1 == null)
						remarks1 = "";
					m.put(IConstants.OUT_REMARK1, remarks1);
					
					if (remarks2.equalsIgnoreCase("null")
							|| remarks2 == null)
						remarks2 = "";
					m.put(IConstants.OUT_REMARK3, remarks2);
					
					if (outbound_gst.equalsIgnoreCase("null")
							|| outbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "OUTBOUND");
						Map gstMap = (Map)gstDetails.get(0);
						outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
												
					m.put(IConstants.OUT_OUTBOUND_GST, outbound_gst);
					
					m.put(IConstants.OUT_ITEM, item);
					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.OUT_QTYOR, qty);

					if (uom.equalsIgnoreCase("null")|| uom == null)
				        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"SALESUOM");
					m.put(IConstants.OUT_UNITMO, uom);
					
					if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                      currencyID = baseCurrency;
	                      m.put(IConstants.IN_CURRENCYID, currencyID);

					if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
					unit_price = "0";
					String minSellingconvertedcost="";
					ArrayList Mprice = itemUtil.getMprice(Plant, item);
					Map gstMap = (Map)Mprice.get(0);
					Minprice  = (String)gstMap.get("MINSPRICE");	
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductimport(Plant,currencyID);
					if(unit_price != "0"  && !minSellingconvertedcost.equals("") && !minSellingconvertedcost.equals(null) &&  Double.valueOf(unit_price) < Double.valueOf(Minprice)*Double.valueOf(minSellingconvertedcost))
					{
						throw new Exception("Price should not be less than minimum selling price");
					}
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
					}
					
					m.put(IConstants.OUT_UNITCOST, unit_price);
            
                      
                    if (employeename.equalsIgnoreCase("null")|| employeename == null)
  						employeename = "";
  					m.put(IConstants.DOHDR_EMPNO, employeename);
  					
  					/* if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = DateUtils.getDate();*/
				
					 if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = "";
 					m.put(IConstants.DELIVERYDATE, deliverydate);
 					
					if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
						shippingcost = "0";
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
					}
					m.put(IConstants.SHIPPINGCOST, shippingcost);
					
					if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
						incoterm = "";
					m.put(IConstants.INCOTERMS, incoterm);
					
					

					if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
						paymenttype = "";
					m.put(IConstants.PAYMENTTYPE, paymenttype);
					if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
						{
						deliverybydate="1";
						}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
						{
						deliverybydate="0";
						}
					m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);
					m.put(IConstants.SALES_LOCATION, salesLocation);

				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		
		return hdrList;

	}
    
    
	public String Autocustno(String plant,String sUserId) {

		String sCustCode = "";
		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		TblControlDAO _TblControlDAO = new TblControlDAO();

		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "C");
				htTblCntInsert.put("MINSEQ", "0000");
				htTblCntInsert.put("MAXSEQ", "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

				sCustCode = "C" + "0001";
			} else {
				// --if exitflag is not false than we updated nxtseq batch number based on
				// plant,currentmonth

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "000";
				} else if (updatedSeq.length() == 2) {
					sZero = "00";
				} else if (updatedSeq.length() == 3) {
					sZero = "0";
				}

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "C");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

				// boolean
				// updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
				sCustCode = "C" + sZero + updatedSeq;
				
			}
			
			return sCustCode;
		} catch (Exception e) {
			return sCustCode;
		}

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList downloadOutboundSheetDataShopee(String Plant, String filename,
			String sheetName,String baseCurrency,String NOOFORDER,String _login_user) throws Exception {
		System.out.println("********  Import Starts *********");

		// to get file name
		ArrayList hdrList = new ArrayList();
		File f = new File(filename);
		filename = f.getName();
		f = null;
		DOUtil doUtil = new DOUtil();
		
		System.out.println("After extraction filename : " + filename);
		
		filename = DbBean.CountSheetUploadPath + filename;

		System.out.println("Import File :" + filename);
		System.out.println("Import sheet :" + sheetName);
		
		PlantMstDAO plantMstDAO = new PlantMstDAO();
	    int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(Plant));
	    String ValidNumber="";
		try {
			POIReadExcel poi = new POIReadExcel(filename, sheetName);

			String[] str = poi.readExcelFileShopee(Integer.parseInt(ShopeeNumberOfColumn),"SHPE");
			System.out.println("Total number of record found in Excel sheet  :  "+ str.length);

			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int novalid = new DoHdrDAO().Salescount(Plant,FROM_DATE,TO_DATE);
			int convl = 0;
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				convl = Integer.valueOf(NOOFORDER);				
			}
			String validpo="";
 			int validcot=0;
			for (int j = 1; j < str.length; j++) {

				String s1 = str[j];
				String s2 = "";

				StringTokenizer parser = new StringTokenizer(s1, "|||");

				System.out.println("Record " + j + "\t: " + s1);

				List list = new LinkedList(); // Doubly-linked list

				list = new ArrayList();

				while (parser.hasMoreTokens()) {
					list.add(parser.nextToken());
				}

				int linecnt = j;
			        linecnt= linecnt+1;
				Map m = new HashMap();

				System.out.println("Total number of record per line  :  "
						+ list.size());

				if(list.size() > 0) {
					
					String dono = StrUtils.replaceStr2Char(((String) list.get(0)).trim());
 					//Validate Order azees - 12/2020
 					if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
 					{
 					int ddok =0;
 					if(validpo.equalsIgnoreCase(""))
 					{
 						validpo=dono;
 						ddok=1;
 						
 					}
 					else {
 					if(validpo.equalsIgnoreCase(dono))
 					{
 						validcot=validcot+1;
 						ddok=validcot-1;
 						if(validcot>0)
 							validcot=validcot-1;
 					}
 					else {
 						validpo=dono;
 						ddok=1;
 						
 					}
 					}
 					
 					novalid=novalid+ddok;
 					
 						if(novalid>convl)
 						{
 							ValidNumber=NOOFORDER;
 							break;
 						}
 					}
 					//End
					String customername = StrUtils.replaceStr2Char(((String) list.get(9)).trim());
					String customerCode = "";
					CustUtil custUtil = new CustUtil();
					if (("").equals(customername)|| ("null").equals(customername)){
					    throw new Exception("Customer name value is mandatory and  missing at Line "+linecnt+" .");  
					}
					
					boolean isExistCustomer = false;
					if(customername.length()>0){
						isExistCustomer = custUtil.isExistCustomerName(customername, Plant);
					if(!isExistCustomer){
						//create customer
						
							  TblControlDAO _TblControlDAO = new TblControlDAO();
					          Hashtable ht = new Hashtable();
					          ht.put(IConstants.PLANT,Plant);
					          String sCustCode = Autocustno(Plant,_login_user);
					          ht.put(IConstants.CUSTOMER_CODE,sCustCode);
					          ht.put("USER_ID",sCustCode);
					          ht.put(IConstants.CUSTOMER_NAME,customername);
					          ht.put(IConstants.COUNTRY,StrUtils.replaceStr2Char(((String) list.get(14)).trim()));
					          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
					          ht.put(IConstants.CREATED_BY,_login_user);
					          ht.put(IConstants.ISACTIVE,"Y");
					          ht.put("CREDITLIMIT","0");
					          ht.put("ISCREDITLIMIT","N");
					          ht.put(IConstants.CREDIT_LIMIT_BY,"NOLIMIT");
					          ht.put(IConstants.OPENINGBALANCE,"0");
					          ht.put(IConstants.TAXTREATMENT, "Non GST Registered");
					          ht.put(IConstants.companyregnumber,"");
					          ht.put(IConstants.ADDRESS1,StrUtils.replaceStr2Char(((String) list.get(11)).trim()));
					          ht.put(IConstants.ADDRESS2,"");
					          ht.put(IConstants.ADDRESS3,"");
					          ht.put(IConstants.ADDRESS4,"");
					          ht.put(IConstants.ZIP,StrUtils.replaceStr2Char(((String) list.get(12)).trim()));
					          ht.put(IConstants.EMAIL,"");

					         
					          /*  */

					          MovHisDAO mdao = new MovHisDAO(Plant);
					          Hashtable htm = new Hashtable();
					          htm.put(IDBConstants.PLANT,Plant);
					          htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_CUST);
					          htm.put("RECID","");
					          htm.put("ITEM",sCustCode);
					          htm.put(IDBConstants.CREATED_BY,_login_user);
					          htm.put(IDBConstants.REMARKS, customername);
					          htm.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
					          htm.put(IDBConstants.TRAN_DATE,new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
					          
							  boolean updateFlag;
							if(sCustCode!="C0001")
					  		  {	
								boolean exitFlag = false;
								Hashtable htv = new Hashtable();				
								htv.put(IDBConstants.PLANT, Plant);
								htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMER");
								exitFlag = _TblControlDAO.isExisit(htv, "", Plant);
								if (exitFlag) 
					  		    updateFlag=_TblControlDAO.updateSeqNo("CUSTOMER",Plant);
								else
								{
									boolean insertFlag = false;
									Map htInsert=null;
					            	Hashtable htTblCntInsert  = new Hashtable();           
					            	htTblCntInsert.put(IDBConstants.PLANT,Plant);          
					            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
					            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
					             	htTblCntInsert.put("MINSEQ","0000");
					             	htTblCntInsert.put("MAXSEQ","9999");
					            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
					            	htTblCntInsert.put(IDBConstants.CREATED_BY, _login_user);
					            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
					            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,Plant);
								}
							}
							  
					          boolean custInserted = custUtil.insertCustomer(ht);
					          boolean  inserted = mdao.insertIntoMovHis(htm);
					          if(custInserted&&inserted) {
					        	customerCode=sCustCode;
								customername =customername;
					          }
					    
						
						//create customer
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(customername,Plant);
						Map mCustot=(Map)arrList.get(0);
						customerCode=(String) mCustot.get("CUSTNO");
						customername = (String) mCustot.get("CNAME");
					}
					}
					
					
					String shippingcustomer = "";
					String project = "";
					String paymenttype = "";
					String refNo = "";
					String order_date = StrUtils.replaceStr2Char(((String) list.get(1)).trim());
					String time = "null";
					String order_type = "null";
					String deliverydate = "";
				    String deliverybydate = "";
				    double subtotal = Double.valueOf((String) list.get(7));
				    double taxa = Double.valueOf((String) list.get(8));
				    double outgst = (taxa * 100)/subtotal;
					String outbound_gst = String.valueOf(outgst);
					String employeename = "null";
					String incoterm = "null";
					String salesLocation = "null";
					String currencyID = baseCurrency;
					String equivalentCurrency = "1";
					String tax = "";
					if(taxa == 0) {
						tax = "ZERO RATE";
					}else {
						tax = "STANDARD RATED";
					}
					
					String productRatesAre = "Tax Exclusive";
					String item = StrUtils.replaceStr2Char(((String) list.get(3)).trim());
					String doLno = StrUtils.replaceStr2Char(((String) list.get(16)).trim());
					String account = "Local sales - retail";
					String uom = "null";
					ItemMstUtil itemmastUtil = new ItemMstUtil();
//					ArrayList listQryuom = itemmastUtil.getProductDetailsWithStockonHand(item, Plant, " ");//Azees Fix Query Load 09.22
//					if (listQryuom.size() > 0) {
//						for (int i = 0; i < listQryuom.size(); i++) {
//							Map um = (Map) listQryuom.get(i);
//							uom = StrUtils.fString((String) um.get("STKUOM"));
//						}
//					}
					String suom =new ItemMstDAO().getItemSalesUOM(Plant,item);
						uom=(String)suom;
					String qty = StrUtils.replaceStr2Char(((String) list.get(6)).trim());
					String productdeliverydate = "null";
					String unit_price = StrUtils.replaceStr2Char(((String) list.get(5)).trim());
					String productdiscount = "0"; 
					String productdiscountPer = "Y";
					String orderdiscount = "0.00";
					String orderdiscounttaxinc= "Tax Exclusive";
					String orderdiscountpre = "N"; //new
					String discount = "0";
					String discounttaxinc = "Tax Exclusive";
					String discountpre = "Y";
					String shippingcost = StrUtils.replaceStr2Char(((String) list.get(8)).trim());
					String shippingcosttaxinc =  "Tax Exclusive";
					String adjustment = "0";
					String remarks1 = "null";
					String remarks2 = "null";
					String projectid="0";
					String shippingid="";
					String taxid="0";
					
					String Minprice = "";
					
					String COUNTRYCODE = "";
	           	  	String plantstate = "";
	           	  	String plantstatecode = "";
	                PlantMstUtil plantmstutil = new PlantMstUtil();
	                List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
	                for (int i = 0; i < viewlistQry.size(); i++) {
	           	     	Map map = (Map) viewlistQry.get(i);
	           	     	COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	           	     	plantstate = StrUtils.fString((String)map.get("STATE"));
	                }
	                
	                MasterDAO masterDAO = new MasterDAO();
	                boolean issuloc=false;		 
	           	 	ArrayList saloctions = masterDAO.getSalesLocationListByCode("", Plant, COUNTRYCODE);
	           	 	if (saloctions.size() > 0) {
	           	 		issuloc = true;
	           	 	}
					

				    if(StrUtils.isCharAllowed(dono) || StrUtils.isCharAllowed(doLno) || StrUtils.isCharAllowed(refNo) || StrUtils.isCharAllowed(order_type) || StrUtils.isCharAllowed(order_date) || StrUtils.isCharAllowed(remarks1) || StrUtils.isCharAllowed(remarks2) || StrUtils.isCharAllowed(outbound_gst) || StrUtils.isCharAllowed(customerCode) || StrUtils.isCharAllowed(paymenttype) || StrUtils.isCharAllowed(item) || StrUtils.isCharAllowed(uom) || StrUtils.isCharAllowed(currencyID) || StrUtils.isCharAllowed(employeename) || StrUtils.isCharAllowed(deliverydate) || StrUtils.isCharAllowed(tax) || StrUtils.isCharAllowed(productRatesAre) || StrUtils.isCharAllowed(account)){
						throw new Exception("The special characters ' and [ are not allowed at line number"+linecnt+".");
					}
				    if (("").equals(dono)|| ("null").equals(dono)){
				        throw new Exception("DONO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if (("").equals(doLno)|| ("null").equals(doLno)){
				        throw new Exception("DOLNNO value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    /* if (("").equals(orderDate)|| ("null").equals(orderDate)){
				        throw new Exception("Order Date value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				   
				    if (("").equals(item)|| ("null").equals(item)){
				        throw new Exception("Product ID value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    if(issuloc) {
					    if (("").equals(salesLocation)|| ("null").equals(salesLocation)){
					        throw new Exception("Sales Location value is mandatory and  missing at Line "+linecnt+" .");  
					    }
				    }
				    if (("").equals(tax)|| ("null").equals(tax)){
				        throw new Exception("Tax value is mandatory and  missing at Line "+linecnt+" .");  
				    }
				    
				    /*if (("").equals(productRatesAre)|| ("null").equals(productRatesAre)){
				        throw new Exception("Product Rates Are value is mandatory and  missing at Line "+linecnt+" .");  
				    }*/
				    
				    
				    
				    
				    
				    // customer validation
					if (("").equals(customerCode)|| ("null").equals(customerCode)){
					    throw new Exception("Customer Code value is mandatory and  missing at Line "+linecnt+" .");  
					}

					isExistCustomer = false;
					
					isExistCustomer = custUtil.isExistCustomer(customerCode, Plant);
					if(!isExistCustomer){
						throw new Exception("Customer '" + customerCode + "' is not created yet on system.");
					}
				    
					if(shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null || shippingcustomer.equalsIgnoreCase("")) {
						shippingcustomer = "";
					}
					//Shipping customer validation
					isExistCustomer = false;
					if(shippingcustomer.length()>0){
						isExistCustomer = custUtil.isExistCustomer(shippingcustomer, Plant);
					if(!isExistCustomer){
						throw new Exception("Shipping Customer '" + shippingcustomer + "' is not created yet on system.");
					}else {
						ArrayList arrList = custUtil.getCustomerListStartsWithName(shippingcustomer,Plant);
						Map mCustot=(Map)arrList.get(0);
						shippingid=(String) mCustot.get("CUSTNO");
						shippingcustomer = (String) mCustot.get("CNAME");
					}
					}
					
					//Project validation					
					boolean isValidproject = false;
					if(DET_DESC_LEN > project.length()){
						isValidproject = true;
					}
					if(!isValidproject){
						throw new Exception("Project value :  '" + project + "' length has exceeded the maximum value of "+DET_DESC_LEN+" Chars at Line "+linecnt+".");
					}
					
					if (project.equalsIgnoreCase("null")|| project == null)
						project = "";
					
					isExistCustomer = false;
					FinProjectDAO finProjectDAO = new FinProjectDAO();
					if(project.length()>0){
						isExistCustomer = finProjectDAO.isExistFinProjectforPurchase(project,Plant);
					if(!isExistCustomer){
						throw new Exception("Project '" + project + "' is not created yet on system.");
					}else {
						List<FinProject> finpro = finProjectDAO.dFinProjectbyprojectforPurchase(project,Plant);
						projectid = String.valueOf(finpro.get(0).getID());
					}
					}
					
					// payment type data validation
					if(paymenttype.equalsIgnoreCase("null")|| paymenttype == null || paymenttype.equalsIgnoreCase("")) {
						paymenttype = "";
					}else {
						boolean isValidpaymenttype = false;
						if(OREDER_TYPE_LEN > paymenttype.length()){
							isValidpaymenttype = true;
						}
						if(!isValidpaymenttype){
							throw new Exception("Payment Type value :  '" + paymenttype + "' length has exceeded the maximum value of "+OREDER_TYPE_LEN+" Chars at Line "+linecnt+".");
						}
						/*MasterDAO masterDAO = new MasterDAO();*/
						isValidpaymenttype=masterDAO.getPaymentTypeisExist(Plant,paymenttype);
						if(!isValidpaymenttype){
			     			throw new Exception("Enter Valid Payment Type  : '" + paymenttype + "', before adding sales at Line "+linecnt+".");
			     		}
					}
					
					// DONO field data validation
					boolean isValidDonoData = false;
					if(DONO_LEN >= dono.length()){
						isValidDonoData = true;
					}
					if(!isValidDonoData){
						throw new Exception("DONO value :  '" + dono + "' length has exceeded the maximum value of "+DONO_LEN+" Chars.");
					}
					
					// validation against database entries
					// DONO number validation with status
					boolean isNewStatusDoNo = false;
					isNewStatusDoNo = doUtil.isNewStatusDONO(dono,Plant);
                    if(isNewStatusDoNo){
                            throw new Exception("DONO '" + dono + "' is closed or already moved to open satus. you can't edit on system at Line "+linecnt+" ."); 
                    }
					
					// RefNo field data validation
				    boolean isValidRefNoData = false;
				    if(DO_REF_LEN >= refNo.length()){
				        isValidRefNoData = true;
				    }
				    if(!isValidRefNoData){
				        throw new Exception("Ref No value '" + refNo + "' length has exceeded the maximum value of "+PONO_REFNO+" Chars at Line "+linecnt+".");
				    }
					
				    // data entry validation for date, time, quantity and unit price fields
					ImportUtils impUtil = new ImportUtils();
					
					// date field validation
					
					
					boolean isCorrectDateFormat = false;
					try {			             
			            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
			            Date date = srcDf.parse(order_date);
			            DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
			            order_date = destDf.format(date);
						
					if (!order_date.equalsIgnoreCase("null") && order_date != null){
						isCorrectDateFormat = impUtil.isCorrectDateFormatUsingRegex(order_date);
						if(!isCorrectDateFormat){
							throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
						}
					}
					}catch (Exception e) {
						throw new Exception("Date '" + order_date + "' is not accepted format at Line "+linecnt+".");
					}

					// time field validation
					boolean isCorrectTimeFormat = false;
					if (!time.equalsIgnoreCase("null") && time != null){
						isCorrectTimeFormat = impUtil.isCorrectTimepartFormat(time);
						if(!isCorrectTimeFormat){
							throw new Exception("Time '" + time + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// delivery date field validation
					boolean isCorrectDeliveryDateFormat = false;
					if (!deliverydate.equalsIgnoreCase("null") && deliverydate != null && deliverybydate.equals("Y")){
						isCorrectDeliveryDateFormat = impUtil.isCorrectDateFormatUsingRegex(deliverydate);
						if(!isCorrectDeliveryDateFormat){
							throw new Exception("Delivery Date '" + deliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
					}
					
					 if (deliverybydate.equals("null") || deliverybydate == null || deliverybydate.equals("") )
					    {
					    	deliverybydate="N";
					    }else if(!deliverybydate.equalsIgnoreCase("N") && !deliverybydate.equalsIgnoreCase("n") && !deliverybydate.equalsIgnoreCase("Y") && !deliverybydate.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Delivery Date By Date  : '" + deliverybydate + "', before adding sales at Line "+linecnt+".");  
			     		  }
					
					// order type validation
					if (!order_type.equalsIgnoreCase("null") && order_type != null){
						boolean isExistOrderType = false;
						OrderTypeUtil ordertypeUtil = new OrderTypeUtil();
						isExistOrderType = ordertypeUtil.isExistsOrderType(order_type, Plant);
						if(!isExistOrderType){
							throw new Exception("Ordertype '" + order_type + "' is not created yet on system.");
						}
					}
					
					// gst field validation
					boolean isCorrectGstFormat = false;
					if (!outbound_gst.trim().equalsIgnoreCase("null") && outbound_gst != null){
						isCorrectGstFormat = impUtil.isCorrectUnitPriceFormat(outbound_gst);
						if(!isCorrectGstFormat){
							throw new Exception("VAT value '" + outbound_gst + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Employee field validation
					 if (!employeename.equalsIgnoreCase("null") && employeename != null){
							boolean isExistEmployee = false;
							Hashtable htEmp = new Hashtable();
				    		htEmp.put(IDBConstants.PLANT,Plant);
				    		htEmp.put("EMPNO",employeename);
				    		EmployeeUtil empUtil = new EmployeeUtil();
				    		if(!empUtil.isExistsEmployee(htEmp))
				    	    {
				    			throw new Exception("Employee Number'" +  employeename + "' doesn't  Exists.Try Again");
				    	    }
							}
						
					// Incoterm field validation 
					 boolean isValidINCOTERMS = false;
						if(INCOTERM_LEN > incoterm.length()){
							isValidINCOTERMS = true;
						}
						if(!isValidINCOTERMS){
							throw new Exception("INCOTERM value :  '" + incoterm + "' length has exceeded the maximum value of "+INCOTERM_LEN+" Chars at Line "+linecnt+".");
						}
						
					
						// Sales Location field validation	
						boolean isValidsalesLocation = false;
							if(SALES_LOCATION > salesLocation.length()){
								isValidsalesLocation = true;
		                }
		                if(!isValidsalesLocation){
		                        throw new Exception("Sales Location value '" + salesLocation + "' length has exceeded the maximum value of "+SALES_LOCATION+" Chars at Line "+linecnt+".");
		                }
		                
		                if(issuloc) {
			               MasterUtil _MasterUtil=new  MasterUtil();
			     		   ArrayList ccList =  _MasterUtil.getSalesLocationByState(salesLocation,Plant,COUNTRYCODE);
			     		   if(ccList.size()>0)
			     		   {
			     			  for (int i = 0; i < ccList.size(); i++) {
			     			     Map map1 = (Map) ccList.get(i);
			     			     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
			     			 }
			     		   }
			     		   else
			     		   {
			     			  throw new Exception("Enter Valid Sales Location  : '" + salesLocation + "', before adding sales at Line "+linecnt+".");
			     		   }
		                }else {
		                	salesLocation = "";
		                }
					
	     		   // Currency ID validation
				    if (!currencyID.equalsIgnoreCase("null") && currencyID != null){
				            boolean isExistCurID = false;
				            CurrencyUtil currUtil = new CurrencyUtil();
				            
				            Hashtable ht = new Hashtable();
				            ht.put(IDBConstants.PLANT, Plant);
				            ht.put(IDBConstants.CURRENCYID, currencyID );
				            isExistCurID = currUtil.isExistCurrency(ht,"");
				            if(!isExistCurID){
				                    throw new Exception("Currency ID '" + currencyID + "' is not created yet on system at Line "+linecnt+".");
				            }
				    }
					
				    if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                     currencyID = baseCurrency;
				    
				    
				    
				   // Equivalent Currency validation
				    boolean isequivalentCurrencyFormat = false;
				    if (!equivalentCurrency.equalsIgnoreCase("null") && equivalentCurrency != null){
				    	isequivalentCurrencyFormat = impUtil.isCorrectEquivalentCurrencyFormat(equivalentCurrency);
							if(!isequivalentCurrencyFormat){
								throw new Exception("Equivalent Currency value '" + equivalentCurrency + "' is not accepted format at Line "+linecnt+".");
							}
						
				    }
				    else
				    {
				    	CurrencyDAO currencyDAO = new CurrencyDAO();
				    	List curQryList=new ArrayList();
				    	curQryList = currencyDAO.getCurrencyDetails(currencyID,Plant);
				    	for(int i =0; i<curQryList.size(); i++) {
				    		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
				    		equivalentCurrency	= StrUtils.fString(((String)arrCurrLine.get(3)));
				            }
				    }
					
				  // tax field validation
		     		  boolean isValidtax = false;
						if(REMARKS_LEN > tax.length()){
							isValidtax = true;
			          }
			          if(!isValidtax){
			                  throw new Exception("Tax value '" + tax + "' length has exceeded the maximum value of "+REMARKS_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			          List<FinCountryTaxType> taxtypes = finCountryTaxTypeDAO.getCountryTaxTypes("OUTBOUND", COUNTRYCODE, tax);
			          if(taxtypes.size() > 0) {
			        		for (FinCountryTaxType finCountryTaxType : taxtypes) {
			        		
			        			taxid = String.valueOf(finCountryTaxType.getID());	        			
			        			String display="";
					         	if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 0)
					         	{
					         		display = finCountryTaxType.getTAXTYPE();
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 0) {
					         		display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 0 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 0) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE();
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")";
					         		}
					         	}
					         	else if(finCountryTaxType.getSHOWSTATE() == 1 && finCountryTaxType.getSHOWPERCENTAGE() == 1 && finCountryTaxType.getISZERO() == 1) {
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" [0.0%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" [0.0%]";
					         		}
					         	}
					         	else
					         	{
					         		if(plantstatecode.equalsIgnoreCase("")) {
					         			display = finCountryTaxType.getTAXTYPE()+" ["+outbound_gst+"%]";
					         		}else {
					         			display = finCountryTaxType.getTAXTYPE()+"("+plantstatecode+")"+" ["+outbound_gst+"%]";
					         		}
					         	}
					         	
					         	tax=display;
			        			
			        		}
			          	}
			          else
			          {
			        	  throw new Exception("Enter Valid Tax  : '" + tax + "', before adding sales at Line "+linecnt+".");
			          }
					
					
			       // Product Rates Are field validation
			          if (("").equals(productRatesAre)|| ("null").equals(productRatesAre) || productRatesAre == null){
			        	  productRatesAre = "Tax Exclusive";
					  }
			 		  boolean isValidProductRatesAre    = false;
						if(EMPTYPE_LEN > productRatesAre.length()){
							isValidProductRatesAre = true;
			          }
			          if(!isValidProductRatesAre){
			                  throw new Exception("Product Rates Are value '" + productRatesAre + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if(productRatesAre.length()>0){
			        	  if(productRatesAre.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(productRatesAre.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Product Rates Are can't be '"+productRatesAre+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // is valid Line No
					    
					 int isThisANumber = 0;
					     try
					     {
					     isThisANumber = Integer.parseInt(doLno); 
					     }
					     catch (NumberFormatException nfe)
					     {
					         throw new Exception("DOLNNO value :  '" + doLno + "' for DONO "+dono+" is not a valid number  at Line "+linecnt+".");
					     }
					
					// item validation
					boolean isExistItem = false;
					ItemUtil itemUtil = new ItemUtil();
					Hashtable htitem = new Hashtable();
					htitem.put("PLANT", Plant);
					htitem.put("ITEM", item);
					htitem.put("ISACTIVE", "Y");
					isExistItem = itemUtil.isExistsItemMst(htitem);
					if (!isExistItem) {
						throw new Exception(
								"Product ID '" + item + "' is not created/in active at Line " + linecnt + ".");
					}

					// Account field data validation
					boolean isValidAccount = false;
					if (PO_REMARK_LEN > account.length()) {
						isValidAccount = true;
					}
					if (!isValidAccount) {
						throw new Exception("Account value '" + account + "' length has exceeded the maximum value of "
								+ PO_REMARK_LEN + " Chars at Line " + linecnt + ".");
					}
					
					if(account.equalsIgnoreCase("null") || account.equalsIgnoreCase("") || account == null) {
						account = "";
					}else {
						CoaDAO coadao = new CoaDAO();
						List<Map<String, String>> listQry = null;
						Map<String, List<Map<String, String>>> listGrouped = null;
						listQry = coadao.selectSubAccountTypeList(Plant, account);
						if (listQry.size() > 0) {
							String[] filterArray = { "8" };
							String[] detailArray = { "26", "25" };
							listQry = listQry.stream()
									.filter(y -> Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
									.collect(Collectors.toList());
							listGrouped = listQry.stream()
									.filter(x -> Arrays.stream(filterArray).anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
									.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
	
							if (listGrouped.size() > 0) {
	
							} else {
								throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
										+ linecnt + ".");
							}
						} else {
							throw new Exception("Enter Valid Account Name : '" + account + "' at Line "
									+ linecnt + ".");
						}
					}

					// UOM field data validation
					boolean isValidUomData = false;
					if (UOM >= uom.length()) {
						isValidUomData = true;
					}
					if (!isValidUomData) {
						throw new Exception("UOM value :  '" + uom + "' length has exceeded the maximum value of " + UOM
								+ " Chars at Line " + linecnt + ".");
					}
					if (!uom.equalsIgnoreCase("null") && uom != null) {
						boolean isExistUom = false;
						htitem.put("STKUOM", uom);

						UomUtil uomUtil = new UomUtil();
						isExistUom = uomUtil.isExistsUom(uom, Plant);
						if (!isExistUom) {
							throw new Exception("UOM '" + uom + "' is not created/in active at line " + linecnt + ".");
						}
					}
					
					// quantity field validation
					boolean isCorrectQuantityFormat = false;
					if (!qty.equalsIgnoreCase("null") && qty != null) {
						isCorrectQuantityFormat = impUtil.isCorrectQuantityFormat(qty);
						if (!isCorrectQuantityFormat) {
							throw new Exception(
									"Quantity value '" + qty + "' is not accepted format at Line " + linecnt + ".");
						}
					}
					
					// Product delivery date field validation
				    boolean isCorrectproductdeliverydate = false;
				    if (!productdeliverydate.equalsIgnoreCase("null") && productdeliverydate != null){
				    	isCorrectproductdeliverydate = impUtil.isCorrectDateFormatUsingRegex(productdeliverydate);
						if(!isCorrectproductdeliverydate){
							throw new Exception("Product Delivery Date '" + productdeliverydate + "' is not accepted format (dd/mm/yyyy) at Line "+linecnt+".");
						}
				    }

				    // unit price field validation
					boolean isCorrectUnitPriceFormat = false;
					if (!unit_price.equalsIgnoreCase("null") && unit_price != null){
						isCorrectUnitPriceFormat = impUtil.isCorrectUnitPriceFormat(unit_price);
						if(!isCorrectUnitPriceFormat){
							throw new Exception("Unit price value '" + unit_price + "' is not accepted format at Line "+linecnt+".");
						}
					}
					if (!unit_price.trim().equalsIgnoreCase("null") && unit_price != null){
						if(unit_price.contains(".")){
							int pricedecnum = unit_price.substring(unit_price.indexOf(".")).length() - 1;
							if(pricedecnum>numberOfDecimal)
							{
								throw new Exception("Invalid more than "+ numberOfDecimal +" digits after decimal in Unit price at Line "+linecnt+".");
							}
						}
						
					}
					
					// Product Discount field data validation			
					boolean isCorrectproductdiscount = false;
					if (!productdiscount.trim().equalsIgnoreCase("null") && productdiscount != null){
						isCorrectproductdiscount = impUtil.isCorrectUnitPriceFormat(productdiscount);
						if(!isCorrectproductdiscount){
							throw new Exception("Product Discount value '" + productdiscount + "' is not accepted format.");
						}
					}else {
						productdiscount = "0";
					}
					
					// Product Discount % field validation			
					if (productdiscountPer.equals("null") || productdiscountPer == null || productdiscountPer.equals("") )
				    {
						productdiscountPer=currencyID;
				    }
				  else if (productdiscountPer.equalsIgnoreCase("Y")|| productdiscountPer.equalsIgnoreCase("y"))
					{
					  productdiscountPer="%";
					}else if (productdiscountPer.equalsIgnoreCase("N")|| productdiscountPer.equalsIgnoreCase("n"))
					{
						productdiscountPer=currencyID;
					}
					else if(!productdiscountPer.equalsIgnoreCase("N")|| !productdiscountPer.equalsIgnoreCase("n")||!productdiscountPer.equalsIgnoreCase("Y")|| !productdiscountPer.equalsIgnoreCase("y"))
		     		  {
		     			 throw new Exception("Enter Valid Product Discount %  : '" + productdiscountPer + "', before adding sales at Line "+linecnt+".");  
		     		  }
					
					
					
					
					
					// Order Discount field validation
					boolean isCorrectOrderDiscount = false;
					if (!orderdiscount.trim().equalsIgnoreCase("null") && orderdiscount != null){
						isCorrectOrderDiscount = impUtil.isCorrectUnitPriceFormat(orderdiscount);
						if(!isCorrectOrderDiscount){
							throw new Exception("Order Discount value '" + orderdiscount + "' is not accepted format.");
						}
					}else {
						orderdiscount = "0";
					}
					
					// Order Discount Are field validation
			 		  boolean isValidorderdiscountAre    = false;
						if(EMPTYPE_LEN > orderdiscounttaxinc.length()){
							isValidorderdiscountAre = true;
			          }
			          if(!isValidorderdiscountAre){
			                  throw new Exception("Order Discount Are value '" + orderdiscounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          
			          if (("").equals(orderdiscounttaxinc)|| ("null").equals(orderdiscounttaxinc) || orderdiscounttaxinc == null){
			        	  orderdiscounttaxinc = "Tax Inclusive";
					  }
			          
			          if(orderdiscounttaxinc.length()>0){
			        	  if(orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Order Discount Are can't be '"+orderdiscounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
			          
			       // Order Discount % field validation			
						if (orderdiscountpre.equals("null") || orderdiscountpre == null || orderdiscountpre.equals("") )
					    {
							orderdiscountpre=currencyID;
					    }
					  else if (orderdiscountpre.equalsIgnoreCase("Y")|| orderdiscountpre.equalsIgnoreCase("y"))
						{
						  orderdiscountpre="%";
						}else if (orderdiscountpre.equalsIgnoreCase("N")|| orderdiscountpre.equalsIgnoreCase("n"))
						{
							orderdiscountpre=currencyID;
						}
						else if(!orderdiscountpre.equalsIgnoreCase("N")|| !orderdiscountpre.equalsIgnoreCase("n")||!orderdiscountpre.equalsIgnoreCase("Y")|| !orderdiscountpre.equalsIgnoreCase("y"))
			     		  {
			     			 throw new Exception("Enter Valid Order Discount %  : '" + orderdiscountpre + "', before adding sales at Line "+linecnt+".");  
			     		  }
						
						
						
						// Discount field validation
						boolean isCorrectDiscount = false;
						if (!discount.trim().equalsIgnoreCase("null") && discount != null){
							isCorrectDiscount = impUtil.isCorrectUnitPriceFormat(discount);
							if(!isCorrectDiscount){
								throw new Exception("Discount value '" + orderdiscount + "' is not accepted format.");
							}
						}else {
							discount = "0";
						}
						
						// Discount Are field validation
				 		  boolean isValiddiscountAre    = false;
							if(EMPTYPE_LEN > discounttaxinc.length()){
								isValiddiscountAre = true;
				          }
				          if(!isValiddiscountAre){
				                  throw new Exception("Discount Are value '" + discounttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
				          }
				          if (("").equals(discounttaxinc)|| ("null").equals(discounttaxinc) || discounttaxinc == null){
				        	  discounttaxinc = "Tax Inclusive";
						  }
				          if(discounttaxinc.length()>0){
				        	  if(discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
				        	  {}
				        	  else if(discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
				        	  {}
				        	  else
				        		  throw new Exception("Discount Are can't be '"+discounttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
				          }
				          
				       // Discount % field validation			
							if (discountpre.equals("null") || discountpre == null || discountpre.equals("") )
						    {
								discountpre=currencyID;
						    }
						  else if (discountpre.equalsIgnoreCase("Y")|| discountpre.equalsIgnoreCase("y"))
							{
							  discountpre="%";
							}else if (discountpre.equalsIgnoreCase("N")|| discountpre.equalsIgnoreCase("n"))
							{
								discountpre=currencyID;
							}
							else if(!discountpre.equalsIgnoreCase("N")|| !discountpre.equalsIgnoreCase("n")||!discountpre.equalsIgnoreCase("Y")|| !discountpre.equalsIgnoreCase("y"))
				     		  {
				     			 throw new Exception("Enter Valid Order Discount %  : '" + discountpre + "', before adding sales at Line "+linecnt+".");  
				     		  }
							
					
					
					//shipping cost validation					
					boolean isCorrectShippingCost = false;
					if (!shippingcost.trim().equalsIgnoreCase("null") && shippingcost != null){
						isCorrectShippingCost = impUtil.isCorrectUnitPriceFormat(shippingcost);
						if(!isCorrectShippingCost){
							throw new Exception("Shipping Cost value '" + shippingcost + "' is not accepted format at Line "+linecnt+".");
						}
					}
					
					// Shipping Cost Are field validation
			 		  boolean isValidShippingCostAre    = false;
						if(EMPTYPE_LEN > shippingcosttaxinc.length()){
							isValidShippingCostAre = true;
			          }
			          if(!isValidShippingCostAre){
			                  throw new Exception("Shipping Cost Are value '" + shippingcosttaxinc + "' length has exceeded the maximum value of "+EMPTYPE_LEN+" Chars at Line "+linecnt+".");
			          }
			          if (("").equals(shippingcosttaxinc)|| ("null").equals(shippingcosttaxinc) || shippingcosttaxinc == null){
			        	  shippingcosttaxinc = "Tax Inclusive";
					  }
			          if(shippingcosttaxinc.length()>0){
			        	  if(shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
			        	  {}
			        	  else if(shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
			        	  {}
			        	  else
			        		  throw new Exception("Shipping Cost Are can't be '"+shippingcosttaxinc+"',Enter any one as (Tax Inclusive or Tax Exclusive) , before adding sales at Line "+linecnt+".");
			          }
													
			        //Adjustment field validation	
						boolean isCorrectadjustment = false;
						if (!adjustment.trim().equalsIgnoreCase("null") && adjustment != null){
							isCorrectadjustment = impUtil.isCorrectUnitPriceFormat(adjustment);
							if(!isCorrectadjustment){
								throw new Exception("Adjustment Cost value '" + adjustment + "' is not accepted format at Line "+linecnt+".");
							}
						}else {
							adjustment = "0";
						}
					
						// Remarks1 field data validation
						boolean isValidRemarksData1 = false;
						if(DO_REMARK_LEN1 > remarks1.length()){
							isValidRemarksData1 = true;
						}
						if(!isValidRemarksData1){
							throw new Exception("Remarks1 value :  '" + remarks1 + "' length has exceeded the maximum value of "+DO_REMARK_LEN1+" Chars at Line "+linecnt+".");
						}
						

						// Remarks1 field data validation
						boolean isValidRemarksData2 = false;
						if(DO_REMARK_LEN2 > remarks2.length()){
							isValidRemarksData2 = true;
						}
						if(!isValidRemarksData2){
							throw new Exception("Remarks2 value :  '" + remarks2 + "' length has exceeded the maximum value of "+DO_REMARK_LEN2+" Chars at Line "+linecnt+".");
						}
				
     		   
					m.put("PLANT", Plant);
					m.put(IConstants.OUT_CUST_CODE, customerCode);
					if (shippingcustomer.equalsIgnoreCase("null")|| shippingcustomer == null)
						shippingcustomer = "";
					m.put(IConstants.SHIPPINGCUSTOMER, shippingcustomer);
					m.put("SHIPPINGID", shippingid);
					m.put("PROJECTID", projectid);
					m.put("TAX", tax);
					m.put("TAXID", taxid);
					m.put("EQUIVALENTCURRENCY", equivalentCurrency);
					if (productRatesAre.equalsIgnoreCase("Tax Inclusive")|| productRatesAre.equalsIgnoreCase("Tax Inclusive"))
					{
						productRatesAre="1";
					}else if (productRatesAre.equalsIgnoreCase("Tax Exclusive")|| productRatesAre.equalsIgnoreCase("Tax Exclusive"))
					{
						productRatesAre="0";
					}else {
						productRatesAre="0";
					}
					m.put("ISTAXINCLUSIVE", productRatesAre);
					m.put("ACCOUNT", account);
					m.put("PRODUCTDELIVERYDATE", productdeliverydate);
					if (productdiscount.equalsIgnoreCase("null")|| productdiscount == null)
						productdiscount = "0";
					
					if(!productdiscountPer.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							productdiscount = String.valueOf(Double.valueOf(productdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put("PRODUCTDISCOUNT", productdiscount);

					m.put("PRODUCTDISCOUNT_TYPE", productdiscountPer);
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						adjustment = String.valueOf(Double.valueOf(adjustment)/Double.valueOf(equivalentCurrency));
					}
					
					m.put("ADJUSTMENT", adjustment);
					
					
					if (orderdiscount.equalsIgnoreCase("null")|| orderdiscount == null)
						orderdiscount = "0";
					if(!orderdiscountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							orderdiscount = String.valueOf(Double.valueOf(orderdiscount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.ORDERDISCOUNT, orderdiscount);
					
					if (orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						orderdiscounttaxinc="1";
					}else if (orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive")|| orderdiscounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						orderdiscounttaxinc="0";
					}else {
						orderdiscounttaxinc="0";
					}
					m.put("ISORDERDISCOUNTTAX", orderdiscounttaxinc);
					
					m.put("ORDERDISCOUNTTYPE", orderdiscountpre);
					
					
					if (discount.equalsIgnoreCase("null")|| discount == null)
						discount = "0";
					if(!discountpre.equalsIgnoreCase("%")) {
						if(!equivalentCurrency.equalsIgnoreCase("")) {
							discount = String.valueOf(Double.valueOf(discount)/Double.valueOf(equivalentCurrency));
						}
					}
					m.put(IConstants.DISCOUNT, discount);
					
					if (discounttaxinc.equalsIgnoreCase("Tax Inclusive")|| discounttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						discounttaxinc="1";
					}else if (discounttaxinc.equalsIgnoreCase("Tax Exclusive")|| discounttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						discounttaxinc="0";
					}else {
						discounttaxinc="0";
					}
					m.put("ISDISCOUNTTAX", discounttaxinc);
					
					m.put("DISCOUNTTYPE", discountpre);
					
					if (shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Inclusive"))
					{
						shippingcosttaxinc="1";
					}else if (shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive")|| shippingcosttaxinc.equalsIgnoreCase("Tax Exclusive"))
					{
						shippingcosttaxinc="0";
					}else {
						shippingcosttaxinc="0";
					}
					m.put("ISSHIPPINGTAX", shippingcosttaxinc);
					
					
					
					
					
					
					
					m.put(IConstants.OUT_DONO, dono);
				        m.put(IConstants.OUT_DOLNNO, doLno);
				    
				    if (refNo.equalsIgnoreCase("null")|| refNo == null)
				    refNo = "";
				    m.put(IConstants.OUT_REF_NO, refNo);
                                    
					if (order_type.equalsIgnoreCase("null")|| order_type == null)
					order_type = "";
					m.put(IConstants.OUT_ORDERTYPE, order_type);

				    if (order_date.equalsIgnoreCase("null")|| order_date == null)
				        order_date = DateUtils.getDate();
					m.put(IConstants.OUT_COLLECTION_DATE, order_date);
					
					if (time.equalsIgnoreCase("null")|| time == null)
				         time = DateUtils.getTimeHHmm();
					m.put(IConstants.OUT_COLLECTION_TIME, time);
					
					if (remarks1.equalsIgnoreCase("null")
							|| remarks1 == null)
						remarks1 = "";
					m.put(IConstants.OUT_REMARK1, remarks1);
					
					if (remarks2.equalsIgnoreCase("null")
							|| remarks2 == null)
						remarks2 = "";
					m.put(IConstants.OUT_REMARK3, remarks2);
					
					if (outbound_gst.equalsIgnoreCase("null")
							|| outbound_gst == null){
						//inbound_gst = "0";
						GstTypeUtil gstTypeUtil = new GstTypeUtil();
						ArrayList gstDetails = gstTypeUtil.getGstTypeListDetailsFromConfigKey(Plant, "OUTBOUND");
						Map gstMap = (Map)gstDetails.get(0);
						outbound_gst  = (String)gstMap.get("GSTPERCENTAGE");					
						
					}
												
					m.put(IConstants.OUT_OUTBOUND_GST, outbound_gst);
					
					m.put(IConstants.OUT_ITEM, item);
					if (qty.equalsIgnoreCase("null") || qty == null)
						qty = "0";
					m.put(IConstants.OUT_QTYOR, qty);

					if (uom.equalsIgnoreCase("null")|| uom == null)
				        uom = new ItemMstDAO().getItemMutiUOM(Plant,item,"SALESUOM");
					m.put(IConstants.OUT_UNITMO, uom);
					
					if (currencyID.equalsIgnoreCase("null")|| currencyID == null)
	                      currencyID = baseCurrency;
	                      m.put(IConstants.IN_CURRENCYID, currencyID);

					if (unit_price.equalsIgnoreCase("null")|| unit_price == null)
					unit_price = "0";
					String minSellingconvertedcost="";
					ArrayList Mprice = itemUtil.getMprice(Plant, item);
					Map gstMap = (Map)Mprice.get(0);
					Minprice  = (String)gstMap.get("MINSPRICE");	
					minSellingconvertedcost = new DOUtil().getminSellingConvertedUnitCostForProductimport(Plant,currencyID);
					if(unit_price != "0"  && !minSellingconvertedcost.equals("") && !minSellingconvertedcost.equals(null) &&  Double.valueOf(unit_price) < Double.valueOf(Minprice)*Double.valueOf(minSellingconvertedcost))
					{
						throw new Exception("Price should not be less than minimum selling price");
					}
					
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						unit_price = String.valueOf(Double.valueOf(unit_price)/Double.valueOf(equivalentCurrency));
					}
					
					m.put(IConstants.OUT_UNITCOST, unit_price);
            
                      
                    if (employeename.equalsIgnoreCase("null")|| employeename == null)
  						employeename = "";
  					m.put(IConstants.DOHDR_EMPNO, employeename);
  					
  					/* if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = DateUtils.getDate();*/
				
					 if (deliverydate.equalsIgnoreCase("null")|| deliverydate == null)
  						deliverydate = "";
 					m.put(IConstants.DELIVERYDATE, deliverydate);
 					
					if (shippingcost.equalsIgnoreCase("null")|| shippingcost == null)
						shippingcost = "0";
					if(!equivalentCurrency.equalsIgnoreCase("")) {
						shippingcost = String.valueOf(Double.valueOf(shippingcost)/Double.valueOf(equivalentCurrency));
					}
					m.put(IConstants.SHIPPINGCOST, shippingcost);
					
					if (incoterm.equalsIgnoreCase("null")|| incoterm == null)
						incoterm = "";
					m.put(IConstants.INCOTERMS, incoterm);
					
					

					if (paymenttype.equalsIgnoreCase("null")|| paymenttype == null)
						paymenttype = "";
					m.put(IConstants.PAYMENTTYPE, paymenttype);
					if (deliverybydate.equalsIgnoreCase("Y")|| deliverybydate.equalsIgnoreCase("y"))
						{
						deliverybydate="1";
						}else if (deliverybydate.equalsIgnoreCase("N")|| deliverybydate.equalsIgnoreCase("n"))
						{
						deliverybydate="0";
						}
					m.put(IConstants.POHDR_DELIVERYDATEFORMAT, deliverybydate);
					m.put(IConstants.SALES_LOCATION, salesLocation);

				}

				hdrList.add(m);

				System.out.println("hdrList :: " + hdrList.size());
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		} finally {

		}
		
		//Validate Order azees - 12/2020
		Map mv = new HashMap();
        mv.put("ValidNumber", ValidNumber);
        hdrList.add(mv);
		
		return hdrList;

	}
    
}
