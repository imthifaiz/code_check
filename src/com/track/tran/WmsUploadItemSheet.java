package com.track.tran;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CatalogDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.util.CatalogUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PrdBrandUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdDeptUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.UomUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsUploadItemSheet implements WmsTran,WmsTranDesc,WmsTranImg,WmsTranItem,WmsTranItemSup, IMLogger {
	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	PrdClassUtil prdclsutil = null;
	PrdTypeUtil prdtypeutil = null;
	PrdBrandUtil prdBranndutil = null;
	ItemSesBeanDAO itemsesdao = null;
        UomUtil uomutil = null;

	public WmsUploadItemSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		prdclsutil = new PrdClassUtil();
		prdtypeutil = new PrdTypeUtil();
		prdBranndutil = new PrdBrandUtil();
	        uomutil = new UomUtil();
		// itemsesdao = new ItemSesBeanDAO();
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertitemsheet -  Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if (flag == true)
			flag = processMovHis(m);

		return flag;
	}
	
	public boolean processWmsTranDesc(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertitemsheet -  Stage : 1");
		flag = processDescCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		return flag;
	}
	
	public boolean processWmsTranImg(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertitemsheet -  Stage : 1");
		flag = processImgCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		return flag;
	}
	
	public boolean processWmsTranItem(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertitemsheet -  Stage : 1");
		flag = processItemCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		return flag;
	}
	
		
	public boolean processWmsTranItemSupplier(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertitemsheet -  Stage : 1");
		flag = processWmsTranItemSup(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		return flag;
	}

	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map
					.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			prdclsutil.setmLogger(mLogger);
			prdtypeutil.setmLogger(mLogger);
			prdBranndutil.setmLogger(mLogger);
		        uomutil.setmLogger(mLogger);
			boolean insertAlternateItem = false;
			boolean result = false;
			Hashtable htcs = new Hashtable();
			Hashtable htupd = new Hashtable();
			Hashtable htcdn = new Hashtable();
			Hashtable htaltupd = new Hashtable();
			Hashtable htaltcdn = new Hashtable();
			htcs.clear();
			htcs.put("PLANT", map.get("PLANT"));

			htcs.put(IConstants.ITEM, map.get(IConstants.ITEM));
			htcs.put(IConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
			String price = (String) map.get(IConstants.PRICE);
		//	String rentalprice =(String)map.get(IConstants.RENTALPRICE);
			//String serviceprice =(String)map.get(IConstants.SERVICEPRICE);
			String minsprice = (String) map.get(IConstants.MIN_S_PRICE);
			String cost = (String) map.get(IConstants.COST);
			String prodgst = (String) map.get(IConstants.PRODGST);
			//String discount = (String) map.get(IConstants.DISCOUNT);
			String stkqty = (String) map.get(IConstants.STKQTY);
			String isbasicuom =(String)map.get(IConstants.ISBASICUOM);
			String maxstkqty = (String) map.get(IConstants.MAXSTKQTY);
			String netweight = (String) map.get(IConstants.NETWEIGHT);
			String grossweight = (String) map.get(IConstants.GROSSWEIGHT);
			String dimension = (String) map.get(IConstants.DIMENSION);
			String hscode = (String) map.get(IConstants.HSCODE);
			String coo = (String) map.get(IConstants.COO);
			String vinno = (String) map.get(IConstants.VINNO);
			String model = (String) map.get(IConstants.MODEL);
			String purchaseuom = (String) map.get(IConstants.PURCHASEUOM);
			String salesuom = (String) map.get(IConstants.SALESUOM);
		//	String rentaluom = (String) map.get(IConstants.RENTALUOM);
			//String serviceuom = (String) map.get(IConstants.SERVICEUOM);
			String inventoryuom = (String) map.get(IConstants.INVENTORYUOM);
			String catalog = (String) map.get(IConstants.CATLOGPATH);
			String item_id = (String) map.get(IConstants.ITEM);
			String Plant = (String) map.get(IConstants.PLANT);
			String VENDNO = (String) map.get(IConstants.VENDNO);
			String PRDDEPTID = (String) map.get(IConstants.PRDDEPTID);
			String[] prddeptid = PRDDEPTID.split(" ");
			 
			 PRDDEPTID=(prddeptid[0]);
			 
			String iscompro = (String) map.get("ISCOMPRO");
			String cppi = (String) map.get("CPPI");
			String incprice = (String) map.get("INCPRICE");
			String incpriceunit = (String) map.get("INCPRICEUNIT");
			boolean iscatalog = false;
			boolean imageSizeflg = false;
			StrUtils strUtils = new StrUtils();
			File file = new File(catalog);
			if(file.exists()) {
	 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ Plant;

 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
 			String strpath="",results="";
				iscatalog = true;
					String fileName = file.getName();
					
					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					System.out.println("Extensions:::::::" + extension);
					if (!imageFormatsList.contains(extension)) {
						results = "<font color=\"red\"> Image extension not valid </font>";
						imageSizeflg = true;
					}
					
					File path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName.substring(fileName
							.lastIndexOf("\\") + 1);

					File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)
							+ ".JPEG");
					strpath = path + "/" + fileName;
					catalog = uploadedFile.getAbsolutePath();
					File imgpath = new File(catalog);
					OutputStream out = new FileOutputStream(imgpath);
					  byte[] writfile = getBytesFromFile(file);
					   int fileLength = writfile.length;
				      out.write(writfile, 0, fileLength);// Write your data
					out.close();
			} 
			htcs.put(IConstants.CATLOGPATH, catalog);
			

			if (price.equalsIgnoreCase("null"))
				price = "0.0";
			else
				price = (String) map.get(IConstants.PRICE);
			htcs.put(IConstants.PRICE, price);
			
			if (minsprice.equalsIgnoreCase("null"))
				minsprice = "0.0";
			else
				minsprice = (String) map.get(IConstants.MIN_S_PRICE);
			htcs.put(IConstants.MIN_S_PRICE, minsprice);

			if (cost.equalsIgnoreCase("null"))
				cost = "0.0";
			else
				cost = (String) map.get(IConstants.COST);
			htcs.put(IConstants.COST, cost);
			
			if (netweight.equalsIgnoreCase("null"))
				netweight = "0.0";
			else
				netweight = (String) map.get(IConstants.NETWEIGHT);
			htcs.put(IConstants.NETWEIGHT, netweight);
			
			if (grossweight.equalsIgnoreCase("null"))
				grossweight = "0.0";
			else
				grossweight = (String) map.get(IConstants.GROSSWEIGHT);
			htcs.put(IConstants.GROSSWEIGHT, grossweight);
			htcs.put(IConstants.DIMENSION, dimension);
						
			
			if (prodgst.equalsIgnoreCase("null"))
				prodgst = "0.0";
			else
				prodgst = (String) map.get(IConstants.PRODGST);
			htcs.put(IConstants.PRODGST, prodgst);

		
			if (stkqty.equalsIgnoreCase("null"))
				stkqty = "0.0";
			else
				stkqty = (String) map.get(IConstants.STKQTY);
			htcs.put(IConstants.STKQTY, stkqty);
	     	if (maxstkqty.equalsIgnoreCase("null"))
				maxstkqty = "0.0";
			else
				maxstkqty = (String) map.get(IConstants.MAXSTKQTY);
			htcs.put(IConstants.MAXSTKQTY, maxstkqty);

			htcs.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
			htcs.put(IDBConstants.ITEMMST_ITEM_TYPE, map
					.get(IDBConstants.ITEMMST_ITEM_TYPE));
			//Start code added by Bruhan for Product brand on 11/9/12
			htcs.put(IDBConstants.PRDBRANDID, map
					.get(IDBConstants.PRDBRANDID));
			//End code added by Bruhan for Product brand on 11/9/12
			htcs.put(IConstants.STKUOM, map.get(IConstants.STKUOM));
			htcs.put(IDBConstants.ITEMMST_REMARK1, map
					.get(IDBConstants.ITEMMST_REMARK1));
			//htcs.put(IDBConstants.ITEMMST_REMARK2, map
				//	.get(IDBConstants.ITEMMST_REMARK2));
			htcs.put(IDBConstants.ITEMMST_REMARK3, map
					.get(IDBConstants.ITEMMST_REMARK3));
			htcs.put(IDBConstants.ITEMMST_REMARK4, map
					.get(IDBConstants.ITEMMST_REMARK4));
			htcs.put(IConstants.ISACTIVE, map.get(IConstants.ISACTIVE));
			/*htcs.put(IConstants.ISKITTING, map.get(IConstants.ISKITTING));*/
			htcs.put(IConstants.NONSTKFLAG, map.get(IConstants.NONSTKFLAG));
			htcs.put(IConstants.HSCODE, hscode);
			htcs.put(IConstants.COO, coo);
			htcs.put(IConstants.VINNO, vinno);
			htcs.put(IConstants.MODEL, model);
			
			htcs.put(IConstants.PURCHASEUOM, purchaseuom);
			htcs.put(IConstants.SALESUOM, salesuom);
			/*htcs.put(IConstants.RENTALUOM, rentaluom);*/
			//htcs.put(IConstants.SERVICEUOM, serviceuom);
			htcs.put(IConstants.INVENTORYUOM, inventoryuom);
			htcs.put(IConstants.ISBASICUOM, isbasicuom);
			htcs.put(IConstants.VENDNO, VENDNO);
			htcs.put(IConstants.PRDDEPTID,PRDDEPTID);
			htcs.put("ISCOMPRO",iscompro);
			htcs.put("CPPI",cppi);
			htcs.put("INCPRICE",incprice);
			htcs.put("INCPRICEUNIT",incpriceunit);
			htcs.put(IConstants.CREATED_AT, dateUtils.getDateTime());
			htcs.put(IConstants.CREATED_BY, map.get("LOGIN_USER"));
		/*	if (rentalprice.equalsIgnoreCase("null"))
				rentalprice = "0.0";
			else
				rentalprice = (String) map.get(IConstants.RENTALPRICE);
			htcs.put(IConstants.RENTALPRICE, rentalprice);*/
			
			//update price only in own warehouse & outlet-azees
	          String isprice_updateonly_in_ownoutlet = new PlantMstDAO().getISPRICE_UPDATEONLY_IN_OWNOUTLET((String) map.get("PLANT"));
			
		    String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
		    boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			Hashtable htBrandtype = new Hashtable();
			htBrandtype.put(IDBConstants.PLANT, map.get("PLANT"));
			htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
			flag = prdBranndutil.isExistsItemType(htBrandtype);
			Hashtable<String, Object> prdBrand = new Hashtable<String, Object>();
			if (flag == false) {
				prdBrand.clear();
				prdBrand.put(IDBConstants.PLANT, map.get("PLANT"));
				prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
				prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
				prdBrand.put(IConstants.ISACTIVE, "Y");
				prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				prdBrand.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				boolean itemInserted = prdBranndutil.insertPrdBrandMst(prdBrand);
			}
			
			if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  	htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = prdBranndutil.isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, childplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        					boolean itemInserted = prdBranndutil.insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        		  	}
		        	  	}
	        	  	}
           	}else if(PARENT_PLANT == null){
           		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  	htBrandtype.clear();
			        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
			        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
			        				flag = prdBranndutil.isExistsItemType(htBrandtype);
			        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
			        				if(brand.length()>0) {
			        				if (flag == false) {
			        					prdBrand.clear();
			        					prdBrand.put(IDBConstants.PLANT, parentplant);
			        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
			        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
			        					prdBrand.put(IConstants.ISACTIVE, "Y");
			        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			        					prdBrand.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			        					boolean itemInserted = prdBranndutil.insertPrdBrandMst(prdBrand);
			        				}
			        				}
			        		  }
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  Map mt = (Map) arrList.get(k);
			        			  String childplant = (String) mt.get("CHILD_PLANT");
			        			  if(childplant!=(String) map.get("PLANT")) {
			        				  	htBrandtype.clear();
				        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
				        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
				        				flag = prdBranndutil.isExistsItemType(htBrandtype);
				        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
				        				if(brand.length()>0) {
				        				if (flag == false) {
				        					prdBrand.clear();
				        					prdBrand.put(IDBConstants.PLANT, childplant);
				        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
				        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
				        					prdBrand.put(IConstants.ISACTIVE, "Y");
				        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				        					prdBrand.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				        					boolean itemInserted = prdBranndutil.insertPrdBrandMst(prdBrand);
				        				}
				        				}
			        			  	}
			        		  	}
			        	  	}
		        	  	}
           			}
           	  }//IMTHI END	
			
		    //PRD DEPT START
		    Hashtable htsdept = new Hashtable();
		  	Hashtable dept = new Hashtable();
		  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		  	htsdept.put(IDBConstants.PLANT, Plant);
		  	htsdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		  	flag = new PrdDeptUtil().isExistsItemType(htsdept);
		  	if (flag == false) {
				dept.put(IDBConstants.PLANT, Plant);
				dept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
				dept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
				dept.put(IConstants.ISACTIVE, "Y");
				dept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				dept.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(dept);
			}
		  	}
		   //PRD DEPT END
		
			htcdn.put("PLANT", map.get("PLANT"));
			htcdn.put(IConstants.ITEM, map.get(IConstants.ITEM));
			result = itemdao.isExistsItemMst((String) map.get(IConstants.ITEM),
					(String) map.get("PLANT"));
			if (result == true) {

				Hashtable htprdcls = new Hashtable();
				Hashtable ht = new Hashtable();
				htprdcls.put(IDBConstants.PLANT, map.get("PLANT"));
				htprdcls.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
				Hashtable htprdtype = new Hashtable();
				htprdtype.put(IDBConstants.PLANT, map.get("PLANT"));
				htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
			    Hashtable htuom = new Hashtable();
			    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
			    htuom.put("UOM", map.get(IConstants.STKUOM));                
			  
				// check existence of product classid
				flag = prdclsutil.isExistsItemType(htprdcls);

				if (flag == false) {
					ht.put(IDBConstants.PLANT, map.get("PLANT"));
					ht.put(IDBConstants.PRDCLSID, map
							.get(IDBConstants.PRDCLSID));
					ht.put(IDBConstants.PRDCLSDESC, map
							.get(IDBConstants.PRDCLSID));
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IDBConstants.CREATED_AT, new DateUtils()
							.getDateTime());
					ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));

					boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
				}
				
				String alternateItemName = (String) map.get("ITEM");
				String addalternateItem = (String) map.get("ALTERNATE_ITEM");
				List<String> alternateItemNameLists = new ArrayList<String>();
				alternateItemNameLists.add(alternateItemName);
				if (addalternateItem != "" && addalternateItem != null) {
					// System.out.println("Add alternate item"+addalternateItem);
					alternateItemNameLists.add(addalternateItem);
				}

				insertAlternateItem = itemdao.insertAlternateItemLists((String) map.get("PLANT"), (String) map.get("ITEM"),alternateItemNameLists);
				
				//IMTHI START 10-11-2022 DESC: IMPORT ALTERNATE PRODUCT TO CHILD COMPANY BASED ON PARENT COMPANY ALLOW 
				if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  insertAlternateItem = itemdao.insertAlternateItemLists(childplant, (String) map.get("ITEM"), alternateItemNameLists);
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  insertAlternateItem = itemdao.insertAlternateItemLists(parentplant, (String) map.get("ITEM"), alternateItemNameLists);
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
				        				  insertAlternateItem = itemdao.insertAlternateItemLists(childplant, (String) map.get("ITEM"), alternateItemNameLists);
				        			  }
				        		  	}
				        	  	}
			        	  }	
	             		}
	             	  }
				
				if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  	htprdcls.clear();
			        			  	htprdcls.put(IDBConstants.PLANT, childplant);
			        				htprdcls.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
			        				String prdclass = (String)map.get(IDBConstants.PRDCLSID);
			        				if(prdclass.length()>0) {
			      					flag = prdclsutil.isExistsItemType(htprdcls);
			      					if (flag == false) {
			      						ht.put(IDBConstants.PLANT, childplant);
			      						ht.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
			      						ht.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
			      						ht.put(IConstants.ISACTIVE, "Y");
			      						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			      						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			      						boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
			      					}
			        				}
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  	htprdcls.clear();
				        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
				        				htprdcls.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
				      					flag = prdclsutil.isExistsItemType(htprdcls);
				      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
				        				if(prdclass.length()>0) {
				      					if (flag == false) {
				      						ht.put(IDBConstants.PLANT, parentplant);
				      						ht.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
				      						ht.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
				      						ht.put(IConstants.ISACTIVE, "Y");
				      						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				      						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				      						boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
				      					}
				        				}
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  Map mt = (Map) arrList.get(k);
				        			  String childplant = (String) mt.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
					        			  	htprdcls.clear();
					        			  	htprdcls.put(IDBConstants.PLANT, childplant);
					        				htprdcls.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
					        				String prdclass = (String)map.get(IDBConstants.PRDCLSID);
					        				if(prdclass.length()>0) {
					      					flag = prdclsutil.isExistsItemType(htprdcls);
					      					if (flag == false) {
					      						ht.put(IDBConstants.PLANT, childplant);
					      						ht.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
					      						ht.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
					      						ht.put(IConstants.ISACTIVE, "Y");
					      						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					      						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					      						boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
					      					}
					        				}
				        			  	}
				        		  	}
				        	  	}
			        	  	}	
	             		}
	             	  }//IMTHI END	
				
				// check existence of product typeid
				flag = prdtypeutil.isExistsItemType(htprdtype);

				if (flag == false) {
					ht.clear();
					ht.put(IDBConstants.PLANT, map.get("PLANT"));
					ht.put("PRD_TYPE_ID", map
							.get(IDBConstants.ITEMMST_ITEM_TYPE));
					ht.put(IDBConstants.PRDTYPEDESC, map
							.get(IDBConstants.ITEMMST_ITEM_TYPE));
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IDBConstants.CREATED_AT, new DateUtils()
							.getDateTime());
					ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
				}
				
				if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  	htprdtype.clear();
			      					htprdtype.put(IDBConstants.PLANT, childplant);
			      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
			      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
			        				if(type.length()>0) {
								    flag = prdtypeutil.isExistsItemType(htprdtype);
								    if (flag == false) {
		                                   	ht.clear();
		                                   	ht.put(IDBConstants.PLANT, childplant);
		               						ht.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
		               						ht.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
		               						ht.put(IConstants.ISACTIVE, "Y");
		               						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		               						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		               						boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
		                               }
			        				}
								    
								    //PRD DEPT START
								    Hashtable htprddept = new Hashtable();
			        			  	Hashtable htdept = new Hashtable();
			        			  	htprddept.put(IDBConstants.PLANT, childplant);
			        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
			        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
			        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
			        			  	if (flag == false) {
										htdept.put(IDBConstants.PLANT, childplant);
										htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
										htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
										htdept.put(IConstants.ISACTIVE, "Y");
										htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
										htdept.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
										boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
										}
									}
								   //PRD DEPT END
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  htprdtype.clear();
				      					htprdtype.put(IDBConstants.PLANT, parentplant);
				      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
				      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
				        				if(type.length()>0) {
									    flag = prdtypeutil.isExistsItemType(htprdtype);
									    if (flag == false) {
			                                   	ht.clear();
			                                   	ht.put(IDBConstants.PLANT, parentplant);
			               						ht.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
			               						ht.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
			               						ht.put(IConstants.ISACTIVE, "Y");
			               						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			               						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			               						boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
			                               }
				        				}
									    //PRD DEPT START
									    Hashtable htprddept = new Hashtable();
				        			  	Hashtable htdept = new Hashtable();
				        			  	htprddept.put(IDBConstants.PLANT, parentplant);
				        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
				        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
				        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
				        			  	if (flag == false) {
											htdept.put(IDBConstants.PLANT, parentplant);
											htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
											htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
											htdept.put(IConstants.ISACTIVE, "Y");
											htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
											htdept.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
											boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
										}
				        			  	}
									   //PRD DEPT END
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  Map mt = (Map) arrList.get(k);
				        			  String childplant = (String) mt.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
				        				  htprdtype.clear();
					      					htprdtype.put(IDBConstants.PLANT, childplant);
					      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
					      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
					        				if(type.length()>0) {
										    flag = prdtypeutil.isExistsItemType(htprdtype);
										    if (flag == false) {
				                                   	ht.clear();
				                                   	ht.put(IDBConstants.PLANT, childplant);
				               						ht.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
				               						ht.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
				               						ht.put(IConstants.ISACTIVE, "Y");
				               						ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				               						ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				               						boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
				                               }
					        				}
										    //PRD DEPT START
										    Hashtable htprddept = new Hashtable();
					        			  	Hashtable htdept = new Hashtable();
					        			  	htprddept.put(IDBConstants.PLANT, childplant);
					        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
					        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
					        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
					        			  	if (flag == false) {
												htdept.put(IDBConstants.PLANT, childplant);
												htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
												htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
												htdept.put(IConstants.ISACTIVE, "Y");
												htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
												htdept.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
												boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
											}
					        			  	}
										   //PRD DEPT END
				        			  	}
				        		  	}
				        	  	}
			        	  	}	
	             		}
	             	  }//IMTHI END	
				
                               //check if Basic UOm exists 
			       flag = uomutil.isExistsUom(htuom);
			      if (flag == false) {
                                   ht.clear();
                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
                                   ht.put("UOM", map.get(IConstants.STKUOM));
                                   ht.put("UOMDESC", map.get(IConstants.STKUOM));
                                   ht.put("Display", map.get(IConstants.STKUOM));
                                   ht.put("QPUOM", "1");
                                   ht.put(IConstants.ISACTIVE, "Y");
                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
                                   boolean uomInserted = uomutil.insertUom(ht);
                               }
			      
			      if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  Hashtable HTuom = new Hashtable();
								    HTuom.put(IDBConstants.PLANT, childplant);
								    HTuom.put("UOM", map.get(IConstants.STKUOM));
								    flag = uomutil.isExistsUom(HTuom);
								    if (flag == false) {
		                                   ht.clear();
		                                   ht.put(IDBConstants.PLANT, childplant);
		                                   ht.put("UOM", map.get(IConstants.STKUOM));
		                                   ht.put("UOMDESC", map.get(IConstants.STKUOM));
		                                   ht.put("Display", map.get(IConstants.STKUOM));
		                                   ht.put("QPUOM", "1");
		                                   ht.put(IConstants.ISACTIVE, "Y");
		                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		                                   boolean uomInserted = uomutil.insertUom(ht);
		                               }
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  Hashtable HTuom = new Hashtable();
									    HTuom.put(IDBConstants.PLANT, parentplant);
									    HTuom.put("UOM", map.get(IConstants.STKUOM));
									    flag = uomutil.isExistsUom(HTuom);
									    if (flag == false) {
			                                   ht.clear();
			                                   ht.put(IDBConstants.PLANT, parentplant);
			                                   ht.put("UOM", map.get(IConstants.STKUOM));
			                                   ht.put("UOMDESC", map.get(IConstants.STKUOM));
			                                   ht.put("Display", map.get(IConstants.STKUOM));
			                                   ht.put("QPUOM", "1");
			                                   ht.put(IConstants.ISACTIVE, "Y");
			                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			                                   boolean uomInserted = uomutil.insertUom(ht);
			                               }
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  Map mt = (Map) arrList.get(k);
				        			  String childplant = (String) mt.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
					        			  Hashtable HTuom = new Hashtable();
										    HTuom.put(IDBConstants.PLANT, childplant);
										    HTuom.put("UOM", map.get(IConstants.STKUOM));
										    flag = uomutil.isExistsUom(HTuom);
										    if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, childplant);
				                                   ht.put("UOM", map.get(IConstants.STKUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.STKUOM));
				                                   ht.put("Display", map.get(IConstants.STKUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }
				        			  	}
				        		  	}
				        	  	}
			        	  	}	
	             		}
	             	  }//IMTHI END	
			      
			    //check if Purchase UOM exists 
			      htuom = new Hashtable();
				    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
				    htuom.put("UOM", map.get(IConstants.PURCHASEUOM));
				    flag = uomutil.isExistsUom(htuom);
				      if (flag == false) {
	                                   ht.clear();
	                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
	                                   ht.put("UOM", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("UOMDESC", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("Display", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("QPUOM", "1");
	                                   ht.put(IConstants.ISACTIVE, "Y");
	                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
	                                   boolean uomInserted = uomutil.insertUom(ht);
	                               }
				      
				      if(PARENT_PLANT != null){
			        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
			        	  if(CHECKplantCompany == null)
			  				CHECKplantCompany = "0";
			        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  Map m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  Hashtable HTuom = new Hashtable();
									    HTuom.put(IDBConstants.PLANT, childplant);
									    HTuom.put("UOM", map.get(IConstants.PURCHASEUOM));
									    flag = uomutil.isExistsUom(HTuom);
									    if (flag == false) {
			                                   ht.clear();
			                                   ht.put(IDBConstants.PLANT, childplant);
			                                   ht.put("UOM", map.get(IConstants.PURCHASEUOM));
			                                   ht.put("UOMDESC", map.get(IConstants.PURCHASEUOM));
			                                   ht.put("Display", map.get(IConstants.PURCHASEUOM));
			                                   ht.put("QPUOM", "1");
			                                   ht.put(IConstants.ISACTIVE, "Y");
			                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			                                   boolean uomInserted = uomutil.insertUom(ht);
			                               }
				        		  	}
				        	  	}
			        	  	}
		             	}else if(PARENT_PLANT == null){
		             		boolean ischild = false;
				        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				        	  if (arrLi.size() > 0) {
				        	  Map mst = (Map) arrLi.get(0);
				        	  String parent = (String) mst.get("PARENT_PLANT");
				         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
				        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
				        	  if(Ischildasparent){
				        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        			  ischild = true;
				        		  }
				        	  }else{
				        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  ischild = true;
				        	  }
				        	  }
				        	  if(ischild){
				        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
					        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
					        	  String  parentplant ="";
					        	  if (arrLists.size() > 0) {
					        		  for (int j=0; j < arrLists.size(); j++ ) {
					        			  Map ms = (Map) arrLists.get(j);
					        			  parentplant = (String) ms.get("PARENT_PLANT");
					        			  Hashtable HTuom = new Hashtable();
										    HTuom.put(IDBConstants.PLANT, parentplant);
										    HTuom.put("UOM", map.get(IConstants.PURCHASEUOM));
										    flag = uomutil.isExistsUom(HTuom);
										    if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, parentplant);
				                                   ht.put("UOM", map.get(IConstants.PURCHASEUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.PURCHASEUOM));
				                                   ht.put("Display", map.get(IConstants.PURCHASEUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }
					        		  }
					        	  }
					        	  
					        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  Map m = new HashMap();
					        	  if (arrList.size() > 0) {
					        		  for (int k=0; k < arrList.size(); k++ ) {
					        			  Map mt = (Map) arrList.get(k);
					        			  String childplant = (String) mt.get("CHILD_PLANT");
					        			  if(childplant!=(String) map.get("PLANT")) {
						        			  Hashtable HTuom = new Hashtable();
											    HTuom.put(IDBConstants.PLANT, childplant);
											    HTuom.put("UOM", map.get(IConstants.PURCHASEUOM));
											    flag = uomutil.isExistsUom(HTuom);
											    if (flag == false) {
					                                   ht.clear();
					                                   ht.put(IDBConstants.PLANT, childplant);
					                                   ht.put("UOM", map.get(IConstants.PURCHASEUOM));
					                                   ht.put("UOMDESC", map.get(IConstants.PURCHASEUOM));
					                                   ht.put("Display", map.get(IConstants.PURCHASEUOM));
					                                   ht.put("QPUOM", "1");
					                                   ht.put(IConstants.ISACTIVE, "Y");
					                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					                                   boolean uomInserted = uomutil.insertUom(ht);
					                               }
					        			  	}
					        		  	}
					        	  	}
				        	  	}	
		             		}
		             	  }//IMTHI END	
				      
				    //check if Sales UOM exists 
				      htuom = new Hashtable();
					    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
					    htuom.put("UOM", map.get(IConstants.SALESUOM));
					    flag = uomutil.isExistsUom(htuom);
					      if (flag == false) {
		                                   ht.clear();
		                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
		                                   ht.put("UOM", map.get(IConstants.SALESUOM));
		                                   ht.put("UOMDESC", map.get(IConstants.SALESUOM));
		                                   ht.put("Display", map.get(IConstants.SALESUOM));
		                                   ht.put("QPUOM", "1");
		                                   ht.put(IConstants.ISACTIVE, "Y");
		                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		                                   boolean uomInserted = uomutil.insertUom(ht);
		                               }
					      
					      if(PARENT_PLANT != null){
				        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
				        	  if(CHECKplantCompany == null)
				  				CHECKplantCompany = "0";
				        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
				        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
					        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					        	  if (arrList.size() > 0) {
					        		  for (int i=0; i < arrList.size(); i++ ) {
					        			  Map m = (Map) arrList.get(i);
					        			  String childplant = (String) m.get("CHILD_PLANT");
					        			  Hashtable HTuom = new Hashtable();
										    HTuom.put(IDBConstants.PLANT, childplant);
										    HTuom.put("UOM", map.get(IConstants.SALESUOM));
										    flag = uomutil.isExistsUom(HTuom);
										    if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, childplant);
				                                   ht.put("UOM", map.get(IConstants.SALESUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.SALESUOM));
				                                   ht.put("Display", map.get(IConstants.SALESUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }
					        		  	}
					        	  	}
				        	  	}
			             	}else if(PARENT_PLANT == null){
			             		boolean ischild = false;
					        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
					        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
					        	  if (arrLi.size() > 0) {
					        	  Map mst = (Map) arrLi.get(0);
					        	  String parent = (String) mst.get("PARENT_PLANT");
					         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
					        	  if(Ischildasparent){
					        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        			  ischild = true;
					        		  }
					        	  }else{
					        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
					        		  ischild = true;
					        	  }
					        	  }
					        	  if(ischild){
					        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
						        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						        	  String  parentplant ="";
						        	  if (arrLists.size() > 0) {
						        		  for (int j=0; j < arrLists.size(); j++ ) {
						        			  Map ms = (Map) arrLists.get(j);
						        			  parentplant = (String) ms.get("PARENT_PLANT");
						        			  Hashtable HTuom = new Hashtable();
											    HTuom.put(IDBConstants.PLANT, parentplant);
											    HTuom.put("UOM", map.get(IConstants.SALESUOM));
											    flag = uomutil.isExistsUom(HTuom);
											    if (flag == false) {
					                                   ht.clear();
					                                   ht.put(IDBConstants.PLANT, parentplant);
					                                   ht.put("UOM", map.get(IConstants.SALESUOM));
					                                   ht.put("UOMDESC", map.get(IConstants.SALESUOM));
					                                   ht.put("Display", map.get(IConstants.SALESUOM));
					                                   ht.put("QPUOM", "1");
					                                   ht.put(IConstants.ISACTIVE, "Y");
					                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					                                   boolean uomInserted = uomutil.insertUom(ht);
					                               }
						        		  }
						        	  }
						        	  
						        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
						        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						        	  Map m = new HashMap();
						        	  if (arrList.size() > 0) {
						        		  for (int k=0; k < arrList.size(); k++ ) {
						        			  Map mt = (Map) arrList.get(k);
						        			  String childplant = (String) mt.get("CHILD_PLANT");
						        			  if(childplant!=(String) map.get("PLANT")) {
							        			  Hashtable HTuom = new Hashtable();
												    HTuom.put(IDBConstants.PLANT, childplant);
												    HTuom.put("UOM", map.get(IConstants.SALESUOM));
												    flag = uomutil.isExistsUom(HTuom);
												    if (flag == false) {
						                                   ht.clear();
						                                   ht.put(IDBConstants.PLANT, childplant);
						                                   ht.put("UOM", map.get(IConstants.SALESUOM));
						                                   ht.put("UOMDESC", map.get(IConstants.SALESUOM));
						                                   ht.put("Display", map.get(IConstants.SALESUOM));
						                                   ht.put("QPUOM", "1");
						                                   ht.put(IConstants.ISACTIVE, "Y");
						                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						                                   boolean uomInserted = uomutil.insertUom(ht);
						                               }
						        			  	}
						        		  	}
						        	  	}
					        	  	}	
			             		}
			             	  }//IMTHI END	
					      
					    //check if Rental UOM exists 
		/*			      htuom = new Hashtable();
						    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
						    htuom.put("UOM", map.get(IConstants.RENTALUOM));
						    flag = uomutil.isExistsUom(htuom);
						      if (flag == false) {
			                                   ht.clear();
			                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
			                                   ht.put("UOM", map.get(IConstants.RENTALUOM));
			                                   ht.put("UOMDESC", map.get(IConstants.RENTALUOM));
			                                   ht.put("Display", map.get(IConstants.RENTALUOM));
			                                   ht.put("QPUOM", "1");
			                                   ht.put(IConstants.ISACTIVE, "Y");
			                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			                                   boolean uomInserted = uomutil.insertUom(ht);
			                               }*/
						    //check if Service UOM exists 
						    /*  htuom = new Hashtable();
							    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
							    htuom.put("UOM", map.get(IConstants.SERVICEUOM));
							    flag = uomutil.isExistsUom(htuom);
							      if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
				                                   ht.put("UOM", map.get(IConstants.SERVICEUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.SERVICEUOM));
				                                   ht.put("Display", map.get(IConstants.SERVICEUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }*/
							    //check if Inventory UOM exists 
							      htuom = new Hashtable();
								    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
								    htuom.put("UOM", map.get(IConstants.INVENTORYUOM));
								    flag = uomutil.isExistsUom(htuom);
								      if (flag == false) {
					                                   ht.clear();
					                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
					                                   ht.put("UOM", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("UOMDESC", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("Display", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("QPUOM", "1");
					                                   ht.put(IConstants.ISACTIVE, "Y");
					                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					                                   boolean uomInserted = uomutil.insertUom(ht);
					                               }

			//IMTHI START  
	          	if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  Hashtable HTuom = new Hashtable();
								    HTuom.put(IDBConstants.PLANT, childplant);
								    HTuom.put("UOM", map.get(IConstants.INVENTORYUOM));
								    flag = uomutil.isExistsUom(HTuom);
								    if (flag == false) {
		                                   ht.clear();
		                                   ht.put(IDBConstants.PLANT, childplant);
		                                   ht.put("UOM", map.get(IConstants.INVENTORYUOM));
		                                   ht.put("UOMDESC", map.get(IConstants.INVENTORYUOM));
		                                   ht.put("Display", map.get(IConstants.INVENTORYUOM));
		                                   ht.put("QPUOM", "1");
		                                   ht.put(IConstants.ISACTIVE, "Y");
		                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		                                   boolean uomInserted = uomutil.insertUom(ht);
		                               }
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int j=0; j < arrLists.size(); j++ ) {
				        			  Map ms = (Map) arrLists.get(j);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  Hashtable HTuom = new Hashtable();
									    HTuom.put(IDBConstants.PLANT, parentplant);
									    HTuom.put("UOM", map.get(IConstants.INVENTORYUOM));
									    flag = uomutil.isExistsUom(HTuom);
									    if (flag == false) {
			                                   ht.clear();
			                                   ht.put(IDBConstants.PLANT, parentplant);
			                                   ht.put("UOM", map.get(IConstants.INVENTORYUOM));
			                                   ht.put("UOMDESC", map.get(IConstants.INVENTORYUOM));
			                                   ht.put("Display", map.get(IConstants.INVENTORYUOM));
			                                   ht.put("QPUOM", "1");
			                                   ht.put(IConstants.ISACTIVE, "Y");
			                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			                                   boolean uomInserted = uomutil.insertUom(ht);
			                               }
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int k=0; k < arrList.size(); k++ ) {
				        			  Map mt = (Map) arrList.get(k);
				        			  String childplant = (String) mt.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
					        			  Hashtable HTuom = new Hashtable();
										    HTuom.put(IDBConstants.PLANT, childplant);
										    HTuom.put("UOM", map.get(IConstants.INVENTORYUOM));
										    flag = uomutil.isExistsUom(HTuom);
										    if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, childplant);
				                                   ht.put("UOM", map.get(IConstants.INVENTORYUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.INVENTORYUOM));
				                                   ht.put("Display", map.get(IConstants.INVENTORYUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }
				        			  	}
				        		  	}
				        	  	}
			        	  	}
	             		}
	             	  }//IMTHI END				      
				
				// update itemmst table
				htupd.put(IConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
				htupd.put(IConstants.PRICE, price);
			//	htupd.put(IConstants.RENTALPRICE, rentalprice);
				//htupd.put(IConstants.SERVICEPRICE, serviceprice);
				htupd.put(IConstants.MIN_S_PRICE, minsprice);
				htupd.put(IConstants.COST, cost);
				htupd.put(IConstants.PRODGST, prodgst);
				//htupd.put(IConstants.DISCOUNT, discount);
				htupd.put(IConstants.STKQTY, stkqty);
				htupd.put(IConstants.MAXSTKQTY, maxstkqty);
				htupd.put(IConstants.ISBASICUOM, isbasicuom);
				htupd.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
				htupd.put(IDBConstants.ITEMMST_ITEM_TYPE, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
				htupd.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
				htupd.put(IConstants.STKUOM, map.get(IConstants.STKUOM));
				htupd.put(IConstants.PURCHASEUOM, map.get(IConstants.PURCHASEUOM));
				htupd.put(IConstants.SALESUOM, map.get(IConstants.SALESUOM));
				//htupd.put(IConstants.RENTALUOM, map.get(IConstants.RENTALUOM));
				//htupd.put(IConstants.SERVICEUOM, map.get(IConstants.SERVICEUOM));
				htupd.put(IConstants.INVENTORYUOM, map.get(IConstants.INVENTORYUOM));
				htupd.put(IDBConstants.ITEMMST_REMARK1, map.get(IDBConstants.ITEMMST_REMARK1));
				//htupd.put(IDBConstants.ITEMMST_REMARK2, map.get(IDBConstants.ITEMMST_REMARK2));
				htupd.put(IDBConstants.ITEMMST_REMARK3, map.get(IDBConstants.ITEMMST_REMARK3));
				htupd.put(IDBConstants.ITEMMST_REMARK4, map.get(IDBConstants.ITEMMST_REMARK4));
				htupd.put(IConstants.ISACTIVE, map.get(IConstants.ISACTIVE));
				/*htupd.put(IConstants.ISKITTING, map.get(IConstants.ISKITTING));*/
				htupd.put(IConstants.NONSTKFLAG, map.get(IConstants.NONSTKFLAG));
				htupd.put(IConstants.NETWEIGHT, netweight);
				htupd.put(IConstants.GROSSWEIGHT, grossweight);
				htupd.put(IConstants.DIMENSION, dimension);
				htupd.put(IConstants.HSCODE, hscode);
				htupd.put(IConstants.COO, coo);
				htupd.put(IConstants.VINNO, vinno);
				htupd.put(IConstants.MODEL, model);
				htupd.put(IConstants.UPDATED_AT, dateUtils.getDateTime());
				htupd.put(IConstants.UPDATED_BY, map.get("LOGIN_USER"));
				htupd.put(IConstants.VENDNO, VENDNO);
				htupd.put(IConstants.PRDDEPTID,PRDDEPTID);
				htupd.put("ISCOMPRO",iscompro);
				htupd.put("CPPI",cppi);
				htupd.put("INCPRICE",incprice);
				htupd.put("INCPRICEUNIT",incpriceunit);
				htupd.put(IConstants.CATLOGPATH, catalog);
				htaltupd.put("PLANT", map.get("PLANT"));
				//PRD DEPT START
				if(!PRDDEPTID.equalsIgnoreCase("null")) {
			  	htsdept.put(IDBConstants.PLANT, Plant);
			  	htsdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
			  	flag = new PrdDeptUtil().isExistsItemType(htsdept);
			  	if (flag == false) {
					dept.put(IDBConstants.PLANT, Plant);
					dept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
					dept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
					dept.put(IConstants.ISACTIVE, "Y");
					dept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					dept.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(dept);
				}
			  	}
			   //PRD DEPT END
			    flag = itemdao.updateItem(htupd, htcdn);
			    
			    
		          String posquery="select PLANT,OUTLET from "+map.get("PLANT")+"_POSOUTLETS WHERE PLANT ='"+map.get("PLANT")+"'";
		          ArrayList posList = new ItemMstUtil().selectForReport(posquery,htData,"");
		          Map pos = new HashMap();
		          if (posList.size() > 0) {
		        	  for (int j=0; j < posList.size(); j++ ) {
		        		  pos = (Map) posList.get(j);
		        		  String outlet = (String) pos.get("OUTLET");
		        		  String posplant = (String) pos.get("PLANT");
		       	       	Hashtable htCond = new Hashtable();
		       	       		htCond.put(IConstants.ITEM,map.get(IConstants.ITEM));
	    	        		htCond.put(IConstants.PLANT,map.get("PLANT"));
	    	        		htCond.put("OUTLET",outlet);
		        	    Hashtable hPos = new Hashtable();	
		        		  hPos.put(IConstants.PLANT,map.get("PLANT"));
		        		  hPos.put("OUTLET",outlet);
		        		  hPos.put(IConstants.ITEM,map.get(IConstants.ITEM));
		        		  hPos.put("SALESUOM",salesuom);
		        		  hPos.put(IConstants.PRICE,price);
		        		  hPos.put("CRBY",map.get("LOGIN_USER"));
		        		  hPos.put("CRAT",new DateUtils().getDateTime());
		        		  if(!(itemdao.isExistsPosOutletPrice((String)map.get(IConstants.ITEM),outlet,(String)map.get("PLANT")))) {
		        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
		        		  }else {
		        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
		        		  }
		        	  }
		          }
			    
			  //imthi start ADD PRODUCT to Child based on plantmaster
		          if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  String childplant="";
		        	  Map m = new HashMap();
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  m = (Map) arrList.get(i);
		        			  childplant = (String) m.get("CHILD_PLANT");
		        			  if(!(itemdao.isExistsItemMst((String) map.get(IConstants.ITEM),childplant))) {
		        				  htupd.put(IConstants.PLANT,childplant);
		        				  htupd.put(IConstants.ITEM,map.get(IConstants.ITEM));
		        				  
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htupd.put(IConstants.CATLOGPATH, catalog);
		        				  
		        				  boolean childitemInserted = itemdao.insertItem(htupd);
					          }else{
					  	        	Hashtable hts = new Hashtable();
						        	hts.put(IConstants.ITEM,map.get(IConstants.ITEM));
						        	hts.put(IConstants.PLANT,childplant);
						        	
			        				  file = new File(catalog);
			        					if(file.exists()) {
			        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
			        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			        			 			String strpath="",results="";
			        			 			iscatalog = true;
			        			 			String fileName = file.getName();
			        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			        			 			if (!imageFormatsList.contains(extension)) {
			        								results = "<font color=\"red\"> Image extension not valid </font>";
			        								imageSizeflg = true;
			        							}
			        			 			File path = new File(fileLocation);
			        			 			if (!path.exists()) {
			        			 				boolean status = path.mkdirs();
			        			 			}
			        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
			        			 			strpath = path + "/" + fileName;
			        			 			catalog = uploadedFile.getAbsolutePath();
			        			 			File imgpath = new File(catalog);
			        			 			OutputStream out = new FileOutputStream(imgpath);
			        			 			byte[] writfile = getBytesFromFile(file);
			        			 			int fileLength = writfile.length;
			        			 			out.write(writfile, 0, fileLength);// Write your data
			        			 			out.close();
			        					} 
			        					htupd.put(IConstants.CATLOGPATH, catalog);
			        					if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
			        						htupd.remove(IConstants.PRICE);	
						        	flag = itemdao.updateItem(htupd, hts);
					          }
		        			  
		        			//PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, childplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, childplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, childplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, childplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
		      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, childplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, childplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, childplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if(salesuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,childplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,childplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  	
		        			  //HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, childplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,childplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, childplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,childplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,childplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        			  	
					          posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					       	       	Hashtable htCond = new Hashtable();
					       	       		htCond.put(IConstants.ITEM,map.get(IConstants.ITEM));
					       	       		htCond.put(IConstants.PLANT,childplant);
					       	       		htCond.put("OUTLET",outlet);
					       	        Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,childplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,map.get(IConstants.ITEM));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String)map.get(IConstants.ITEM),outlet,childplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
					        		  }
					        	  }
					          }
		        		  }
		        	  }
		        	}
		        	  
		          }else if(PARENT_PLANT == null){
		        	  boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        	  
		        	  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
		        	  String parentplant = "";
		        	  Map ms = new HashMap();
		        	  if (arrLists.size() > 0) {
		        		  for (int i=0; i < arrLists.size(); i++ ) {
		        			  ms = (Map) arrLists.get(i);
		        			  parentplant = (String) ms.get("PARENT_PLANT");
		        			  if(!(itemdao.isExistsItemMst((String)map.get(IConstants.ITEM),parentplant))) {
		        			  	  htupd.put(IConstants.PLANT,parentplant);
		        			  	  htupd.put(IConstants.ITEM,map.get(IConstants.ITEM));
		        			  	  
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htupd.put(IConstants.CATLOGPATH, catalog);
		        					
		        				  boolean childitemInserted = itemdao.insertItem(htupd);
		        			  }else{
		        				  	Hashtable hts = new Hashtable();
						        	hts.put(IConstants.ITEM,map.get(IConstants.ITEM));
						        	hts.put(IConstants.PLANT,parentplant);
						        	
			        				  file = new File(catalog);
			        					if(file.exists()) {
			        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
			        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			        			 			String strpath="",results="";
			        			 			iscatalog = true;
			        			 			String fileName = file.getName();
			        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			        			 			if (!imageFormatsList.contains(extension)) {
			        								results = "<font color=\"red\"> Image extension not valid </font>";
			        								imageSizeflg = true;
			        							}
			        			 			File path = new File(fileLocation);
			        			 			if (!path.exists()) {
			        			 				boolean status = path.mkdirs();
			        			 			}
			        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
			        			 			strpath = path + "/" + fileName;
			        			 			catalog = uploadedFile.getAbsolutePath();
			        			 			File imgpath = new File(catalog);
			        			 			OutputStream out = new FileOutputStream(imgpath);
			        			 			byte[] writfile = getBytesFromFile(file);
			        			 			int fileLength = writfile.length;
			        			 			out.write(writfile, 0, fileLength);// Write your data
			        			 			out.close();
			        					} 
			        					htupd.put(IConstants.CATLOGPATH, catalog);
			        					if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
			        						htupd.remove(IConstants.PRICE);		
						        	flag = itemdao.updateItem(htupd, hts);
					          }
		        			  
		        			//PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, parentplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, parentplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, parentplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
		      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, parentplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, parentplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, parentplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, parentplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	if(salesuom.length()>0) {
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, parentplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,parentplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,parentplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  	
		        			  //HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, parentplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,parentplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, parentplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, parentplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,parentplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, parentplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,parentplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        			  	
					          posquery="select PLANT,OUTLET from "+parentplant+"_POSOUTLETS WHERE PLANT ='"+parentplant+"'";
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					      	       	Hashtable htCond= new Hashtable();
					      	        	htCond.put(IConstants.ITEM,map.get(IConstants.ITEM));
					      	        	htCond.put(IConstants.PLANT,parentplant);
					      	        	htCond.put("OUTLET",outlet);
					    	        Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,parentplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,map.get(IConstants.ITEM));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String)map.get(IConstants.ITEM),outlet,parentplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
					        		  }
					        	  }
					          }
		        		  }
		        	  }
		        	  
		        	  
		        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  Map m = new HashMap();
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  if(childplant!=(String) map.get("PLANT")) {
		        			  posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
		        			  if(!(itemdao.isExistsItemMst((String)map.get(IConstants.ITEM),childplant))) {
		        				  htupd.put(IConstants.PLANT,childplant);
		        				  htupd.put(IConstants.ITEM,map.get(IConstants.ITEM));
		        				  
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htupd.put(IConstants.CATLOGPATH, catalog);
		        				  boolean childitemInserted = itemdao.insertItem(htupd);
		        			  }else{
		        				  	Hashtable hts = new Hashtable();
						        	hts.put(IConstants.ITEM,map.get(IConstants.ITEM));
						        	hts.put(IConstants.PLANT,childplant);
						        	
			        				  file = new File(catalog);
			        					if(file.exists()) {
			        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
			        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			        			 			String strpath="",results="";
			        			 			iscatalog = true;
			        			 			String fileName = file.getName();
			        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			        			 			if (!imageFormatsList.contains(extension)) {
			        								results = "<font color=\"red\"> Image extension not valid </font>";
			        								imageSizeflg = true;
			        							}
			        			 			File path = new File(fileLocation);
			        			 			if (!path.exists()) {
			        			 				boolean status = path.mkdirs();
			        			 			}
			        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
			        			 			strpath = path + "/" + fileName;
			        			 			catalog = uploadedFile.getAbsolutePath();
			        			 			File imgpath = new File(catalog);
			        			 			OutputStream out = new FileOutputStream(imgpath);
			        			 			byte[] writfile = getBytesFromFile(file);
			        			 			int fileLength = writfile.length;
			        			 			out.write(writfile, 0, fileLength);// Write your data
			        			 			out.close();
			        					} 
			        					htupd.put(IConstants.CATLOGPATH, catalog);
			        					
			        					if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("1"))
			        						htupd.remove(IConstants.PRICE);		
						        	flag = itemdao.updateItem(htupd, hts);
					          }
		        			  
		        			//PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, childplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, childplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, childplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, childplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
		      					String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, childplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, childplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, childplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if(salesuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,childplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,childplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  	
		        			  //HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, childplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,childplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, childplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,childplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,childplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        			  	
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					        	     Hashtable htCond = new Hashtable();
					      	        	htCond.put(IConstants.ITEM,map.get(IConstants.ITEM));
					      	        	htCond.put(IConstants.PLANT,childplant);
					      	        	htCond.put("OUTLET",outlet);
					        		  Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,childplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,map.get(IConstants.ITEM));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String)map.get(IConstants.ITEM),outlet,childplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCond);
					        		  }
					        	  }
					          }
		        			  }
		        		  }
		        	  }
		        	  
		        	  }
		          	}
		          }

			} else {
				Hashtable htprdcls = new Hashtable();
				Hashtable ht = new Hashtable();
				htprdcls.put(IDBConstants.PLANT, map.get("PLANT"));
				htprdcls.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));

				Hashtable htprdtype = new Hashtable();
				htprdtype.put(IDBConstants.PLANT, map.get("PLANT"));
				htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
				// check existence of product classid
				flag = prdclsutil.isExistsItemType(htprdcls);

				if (flag == false) {
					ht.put(IDBConstants.PLANT, map.get("PLANT"));
					ht.put(IDBConstants.PRDCLSID, map
							.get(IDBConstants.PRDCLSID));
					ht.put(IDBConstants.PRDCLSDESC, map
							.get(IDBConstants.PRDCLSID));
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IDBConstants.CREATED_AT, new DateUtils()
							.getDateTime());
					ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));

					boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
				}
				// check existence of product typeid
				flag = prdtypeutil.isExistsItemType(htprdtype);

				if (flag == false) {
					ht.clear();
					ht.put(IDBConstants.PLANT, map.get("PLANT"));
					ht.put("PRD_TYPE_ID", map
							.get(IDBConstants.ITEMMST_ITEM_TYPE));
					ht.put(IDBConstants.PRDTYPEDESC, map
							.get(IDBConstants.ITEMMST_ITEM_TYPE));
					ht.put(IConstants.ISACTIVE, "Y");
					ht.put(IDBConstants.CREATED_AT, new DateUtils()
							.getDateTime());
					ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
				}
				
				  Hashtable htuom = new Hashtable();
				    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
				    htuom.put("UOM", map.get(IConstants.STKUOM));  
				 //check if Basic UOm exists 
			       flag = uomutil.isExistsUom(htuom);
			      if (flag == false) {
                                ht.clear();
                                ht.put(IDBConstants.PLANT, map.get("PLANT"));
                                ht.put("UOM", map.get(IConstants.STKUOM));
                                ht.put("UOMDESC", map.get(IConstants.STKUOM));
                                ht.put("Display", map.get(IConstants.STKUOM));
                                ht.put("QPUOM", "1");
                                ht.put(IConstants.ISACTIVE, "Y");
                                ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
                                ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
                                boolean uomInserted = uomutil.insertUom(ht);
                            }
			    //check if Purchase UOM exists 
			      htuom = new Hashtable();
				    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
				    htuom.put("UOM", map.get(IConstants.PURCHASEUOM));
				    flag = uomutil.isExistsUom(htuom);
				      if (flag == false) {
	                                   ht.clear();
	                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
	                                   ht.put("UOM", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("UOMDESC", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("Display", map.get(IConstants.PURCHASEUOM));
	                                   ht.put("QPUOM", "1");
	                                   ht.put(IConstants.ISACTIVE, "Y");
	                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
	                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
	                                   boolean uomInserted = uomutil.insertUom(ht);
	                               }
				    //check if Sales UOM exists 
				      htuom = new Hashtable();
					    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
					    htuom.put("UOM", map.get(IConstants.SALESUOM));
					    flag = uomutil.isExistsUom(htuom);
					      if (flag == false) {
		                                   ht.clear();
		                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
		                                   ht.put("UOM", map.get(IConstants.SALESUOM));
		                                   ht.put("UOMDESC", map.get(IConstants.SALESUOM));
		                                   ht.put("Display", map.get(IConstants.SALESUOM));
		                                   ht.put("QPUOM", "1");
		                                   ht.put(IConstants.ISACTIVE, "Y");
		                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		                                   boolean uomInserted = uomutil.insertUom(ht);
		                               }
					    //check if Rental UOM exists 
		/*			      htuom = new Hashtable();
						    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
						    htuom.put("UOM", map.get(IConstants.RENTALUOM));
						    flag = uomutil.isExistsUom(htuom);
						      if (flag == false) {
			                                   ht.clear();
			                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
			                                   ht.put("UOM", map.get(IConstants.RENTALUOM));
			                                   ht.put("UOMDESC", map.get(IConstants.RENTALUOM));
			                                   ht.put("Display", map.get(IConstants.RENTALUOM));
			                                   ht.put("QPUOM", "1");
			                                   ht.put(IConstants.ISACTIVE, "Y");
			                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			                                   boolean uomInserted = uomutil.insertUom(ht);
			                               }*/
						    //check if Service UOM exists 
					/*	      htuom = new Hashtable();
							    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
							    htuom.put("UOM", map.get(IConstants.SERVICEUOM));
							    flag = uomutil.isExistsUom(htuom);
							      if (flag == false) {
				                                   ht.clear();
				                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
				                                   ht.put("UOM", map.get(IConstants.SERVICEUOM));
				                                   ht.put("UOMDESC", map.get(IConstants.SERVICEUOM));
				                                   ht.put("Display", map.get(IConstants.SERVICEUOM));
				                                   ht.put("QPUOM", "1");
				                                   ht.put(IConstants.ISACTIVE, "Y");
				                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
				                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
				                                   boolean uomInserted = uomutil.insertUom(ht);
				                               }
*/							    //check if Inventory UOM exists 
							      htuom = new Hashtable();
								    htuom.put(IDBConstants.PLANT, map.get("PLANT"));
								    htuom.put("UOM", map.get(IConstants.INVENTORYUOM));
								    flag = uomutil.isExistsUom(htuom);
								      if (flag == false) {
					                                   ht.clear();
					                                   ht.put(IDBConstants.PLANT, map.get("PLANT"));
					                                   ht.put("UOM", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("UOMDESC", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("Display", map.get(IConstants.INVENTORYUOM));
					                                   ht.put("QPUOM", "1");
					                                   ht.put(IConstants.ISACTIVE, "Y");
					                                   ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					                                   ht.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
					                                   boolean uomInserted = uomutil.insertUom(ht);
					                               }

				String alternateItemName = (String) map.get("ITEM");
				String addalternateItem = (String) map.get("ALTERNATE_ITEM");
				List<String> alternateItemNameLists = new ArrayList<String>();
				alternateItemNameLists.add(alternateItemName);
				if (addalternateItem != "" && addalternateItem != null) {
					// System.out.println("Add alternate item"+addalternateItem);
					alternateItemNameLists.add(addalternateItem);
				}

				insertAlternateItem = itemdao.insertAlternateItemLists((String) map.get("PLANT"), (String) map.get("ITEM"),alternateItemNameLists);
				
				//IMTHI START 10-11-2022 DESC: IMPORT ALTERNATE PRODUCT TO CHILD COMPANY BASED ON PARENT COMPANY ALLOW 
				if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
			        			  insertAlternateItem = itemdao.insertAlternateItemLists(childplant, (String) map.get("ITEM"), alternateItemNameLists);
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
				        			  insertAlternateItem = itemdao.insertAlternateItemLists(parentplant, (String) map.get("ITEM"), alternateItemNameLists);
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
				        				  insertAlternateItem = itemdao.insertAlternateItemLists(childplant, (String) map.get("ITEM"), alternateItemNameLists);
				        			  }
				        		  	}
				        	  	}
			        	  }	
	             		}
	             	  }//IMTHI END ALTERNATE ITEM TO CHILD PARENT
				
				
				flag = itemdao.insertItem(htcs);
				
				//IMTHI START 10-11-2022 DESC: IMPORT PRODUCT TO CHILD COMPANY BASED ON PARENT COMPANY ALLOW
		          String posquery="select PLANT,OUTLET from "+(String) map.get("PLANT")+"_POSOUTLETS WHERE PLANT ='"+(String) map.get("PLANT")+"'";
		          ArrayList posList = new ItemMstUtil().selectForReport(posquery,htData,"");
		          Map pos = new HashMap();
		          if (posList.size() > 0) {
		        	  for (int j=0; j < posList.size(); j++ ) {
		        		  pos = (Map) posList.get(j);
		        		  String outlet = (String) pos.get("OUTLET");
		        		  String posplant = (String) pos.get("PLANT");
		       	       	Hashtable htCondition = new Hashtable();
		       	       		htCondition.put(IConstants.ITEM,(String) map.get("ITEM"));
	      	        		htCondition.put(IConstants.PLANT,(String) map.get("PLANT"));
	      	        		htCondition.put("OUTLET",outlet);
		        	    Hashtable hPos = new Hashtable();	
		        		  hPos.put(IConstants.PLANT,(String) map.get("PLANT"));
		        		  hPos.put("OUTLET",outlet);
		        		  hPos.put(IConstants.ITEM,(String) map.get("ITEM"));
		        		  hPos.put("SALESUOM",salesuom);
		        		  hPos.put("CRBY",map.get("LOGIN_USER"));
		        		  hPos.put("CRAT",new DateUtils().getDateTime());
		        		  if(!(itemdao.isExistsPosOutletPrice((String) map.get("ITEM"),outlet,(String) map.get("PLANT")))) {
		        			  hPos.put(IConstants.PRICE,price);
		        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
		        		  }else {
		        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
			        		  	  hPos.put(IConstants.PRICE,price);
		        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
		        		  }
		        	  }
		          }
		          
		          if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  String childplant="";
		        	  Map m = new HashMap();
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  m = (Map) arrList.get(i);
		        			  childplant = (String) m.get("CHILD_PLANT");
		        			  htcs.put(IConstants.PLANT,childplant);
		        			  if(!(itemdao.isExistsItemMst((String) map.get("ITEM"),childplant))) {
		        				  
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htcs.put(IConstants.CATLOGPATH, catalog);
		        				  boolean childitemInserted = itemdao.insertItem(htcs);
					          }
		        			  
		        			//PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, childplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, childplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, childplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, childplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, childplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, childplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, childplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	if(salesuom.length()>0) {
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,childplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,childplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  
		        			//HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, childplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,childplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, childplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,childplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,childplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        				
					          posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					       	       	Hashtable htCondition = new Hashtable();
					       	       		htCondition.put(IConstants.ITEM,(String) map.get("ITEM"));
					       	       		htCondition.put(IConstants.PLANT,childplant);
					       	       		htCondition.put("OUTLET",outlet);
					       	        Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,childplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,(String) map.get("ITEM"));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String) map.get("ITEM"),outlet,childplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
					        		  }
					        	  }
					          }
		        		  }
		        	  }
		        	}
		        	  
		          }else if(PARENT_PLANT == null){
		        	  boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = "";
		        	  parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        	  
		        	  
		        	  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
		        	  String parentplant = "";
		        	  Map ms = new HashMap();
		        	  if (arrLists.size() > 0) {
		        		  for (int i=0; i < arrLists.size(); i++ ) {
		        			  ms = (Map) arrLists.get(i);
		        			  parentplant = (String) ms.get("PARENT_PLANT");
		        			  htcs.put(IConstants.PLANT,parentplant);
		        			  if(!(itemdao.isExistsItemMst((String) map.get("ITEM"),parentplant))) {
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htcs.put(IConstants.CATLOGPATH, catalog);
		        				  boolean childitemInserted = itemdao.insertItem(htcs);
		        			  }
		        			  
		        			  //PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, parentplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, parentplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, parentplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, parentplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, parentplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, parentplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, parentplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, parentplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, parentplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if(salesuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, parentplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,parentplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, parentplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,parentplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  
		        			//HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, parentplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,parentplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, parentplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, parentplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,parentplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, parentplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, parentplant) && !new CustUtil().isExistVendorName(sCustName, parentplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,parentplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        				
					          posquery="select PLANT,OUTLET from "+parentplant+"_POSOUTLETS WHERE PLANT ='"+parentplant+"'";
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					      	       	Hashtable htCondition = new Hashtable();
					      	        	htCondition.put(IConstants.ITEM,(String) map.get("ITEM"));
					      	        	htCondition.put(IConstants.PLANT,parentplant);
					      	        	htCondition.put("OUTLET",outlet);
					    	        Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,parentplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,(String) map.get("ITEM"));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String) map.get("ITEM"),outlet,parentplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
					        		  }
					        	  }
					          }
		        		  }
		        	  }
		        	  
		        	  
		        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  Map m = new HashMap();
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  if(childplant!=(String) map.get("PLANT")) {
		        				  htcs.put(IConstants.PLANT,childplant);
		        			  posquery="select PLANT,OUTLET from "+childplant+"_POSOUTLETS WHERE PLANT ='"+childplant+"'";
		        			  if(!(itemdao.isExistsItemMst((String) map.get("ITEM"),childplant))) {
		        				  file = new File(catalog);
		        					if(file.exists()) {
		        			 			String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
		        			 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
		        			 			String strpath="",results="";
		        			 			iscatalog = true;
		        			 			String fileName = file.getName();
		        			 			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		        			 			if (!imageFormatsList.contains(extension)) {
		        								results = "<font color=\"red\"> Image extension not valid </font>";
		        								imageSizeflg = true;
		        							}
		        			 			File path = new File(fileLocation);
		        			 			if (!path.exists()) {
		        			 				boolean status = path.mkdirs();
		        			 			}
		        			 			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		        			 			File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(item_id)+ ".JPEG");
		        			 			strpath = path + "/" + fileName;
		        			 			catalog = uploadedFile.getAbsolutePath();
		        			 			File imgpath = new File(catalog);
		        			 			OutputStream out = new FileOutputStream(imgpath);
		        			 			byte[] writfile = getBytesFromFile(file);
		        			 			int fileLength = writfile.length;
		        			 			out.write(writfile, 0, fileLength);// Write your data
		        			 			out.close();
		        					} 
		        					htcs.put(IConstants.CATLOGPATH, catalog);
		        				  boolean childitemInserted = itemdao.insertItem(htcs);
		        			  }
		        			  
		        			  //PRD BRAND START
		        				htBrandtype.clear();
		        			  	htBrandtype.put(IDBConstants.PLANT, childplant);
		        				htBrandtype.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        				flag = new PrdBrandUtil().isExistsItemType(htBrandtype);
		        				String brand = (String)map.get(IDBConstants.PRDBRANDID);
		        				if(brand.length()>0) {
		        				if (flag == false) {
		        					prdBrand.clear();
		        					prdBrand.put(IDBConstants.PLANT, childplant);
		        					prdBrand.put(IDBConstants.PRDBRANDID, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IDBConstants.PRDBRANDDESC, map.get(IDBConstants.PRDBRANDID));
		        					prdBrand.put(IConstants.ISACTIVE, "Y");
		        					prdBrand.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        					prdBrand.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        					boolean PrdBrandInserted  = new PrdBrandUtil().insertPrdBrandMst(prdBrand);
		        				}
		        				}
		        			  //PRD BRAND END
		        				
		        			  //PRD CLASS START
		        			  	Hashtable htclass = new Hashtable();
		        			  	htprdcls.clear();
		        			  	htprdcls.put(IDBConstants.PLANT, childplant);
		        				htprdcls.put(IDBConstants.PRDCLSID,map.get(IDBConstants.PRDCLSID));
		      					flag = new PrdClassUtil().isExistsItemType(htprdcls);
		      					String prdclass = (String)map.get(IDBConstants.PRDCLSID);
		        				if(prdclass.length()>0) {
		      					if (flag == false) {
		      						htclass.put(IDBConstants.PLANT, childplant);
		      						htclass.put(IDBConstants.PRDCLSID, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IDBConstants.PRDCLSDESC, map.get(IDBConstants.PRDCLSID));
		      						htclass.put(IConstants.ISACTIVE, "Y");
		      						htclass.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		      						htclass.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		      						boolean PrdClsInserted = new PrdClassUtil().insertPrdClsMst(htclass);
		      					}
		        				}
		      				  //PRD CLASS END
		      					
		      				  //PRD TYPE START	
		        			  	Hashtable htprdtp = new Hashtable();
		        			  	htprdtype.clear();
		      					htprdtype.put(IDBConstants.PLANT, childplant);
		      					htprdtype.put("PRD_TYPE_ID", map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    flag = new PrdTypeUtil().isExistsItemType(htprdtype);
							    String type = (String)map.get(IDBConstants.ITEMMST_ITEM_TYPE);
		        				if(type.length()>0) {
							    if (flag == false) {
							    	htprdtp.clear();
							    	htprdtp.put(IDBConstants.PLANT, childplant);
							    	htprdtp.put("PRD_TYPE_ID",  map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IDBConstants.PRDTYPEDESC, map.get(IDBConstants.ITEMMST_ITEM_TYPE));
							    	htprdtp.put(IConstants.ISACTIVE, "Y");
							    	htprdtp.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							    	htprdtp.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
	               					boolean PrdTypeInserted = new PrdTypeUtil().insertPrdTypeMst(htprdtp);
	                               }
		        				}
		      					//PRD TYPE END
							    
							    //PRD DEPT START
							    Hashtable htprddept = new Hashtable();
		        			  	Hashtable htdept = new Hashtable();
		        			  	htprddept.put(IDBConstants.PLANT, childplant);
		        			  	htprddept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
		        			  	if(!PRDDEPTID.equalsIgnoreCase("null")) {
		        			  	flag = new PrdDeptUtil().isExistsItemType(htprddept);
		        			  	if (flag == false) {
									htdept.put(IDBConstants.PLANT, childplant);
									htdept.put(IDBConstants.PRDDEPTID, PRDDEPTID);
									htdept.put(IDBConstants.PRDDEPTDESC, PRDDEPTID);
									htdept.put(IConstants.ISACTIVE, "Y");
									htdept.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
									htdept.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
									boolean PrdDepInserted = new PrdDeptUtil().insertPrdDepMst(htdept);
								}
		        			  	}
							   //PRD DEPT END
		        			 	
		        			  //check if Purchase UOM exists 
		        			  	Hashtable htInv = new Hashtable();
		        			  	Hashtable HtPurchaseuom = new Hashtable();
		        			  	HtPurchaseuom.put(IDBConstants.PLANT, childplant);
		        			  	HtPurchaseuom.put("UOM", purchaseuom);
		        			  	flag = new UomUtil().isExistsUom(HtPurchaseuom);
		        			  	if(purchaseuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", purchaseuom);
		        			  		htInv.put("UOMDESC", purchaseuom);
		        			  		htInv.put("Display", purchaseuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END PURCHASE UOM
		        			  	
		        			  //check if Sales UOM exists 
		        			  	Hashtable HtSalesuom = new Hashtable();
		        			  	HtSalesuom.put(IDBConstants.PLANT, childplant);
		        			  	HtSalesuom.put("UOM", salesuom);
		        			  	flag = new UomUtil().isExistsUom(HtSalesuom);
		        			  	if(salesuom.length()>0) {
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT, childplant);
		        			  		htInv.put("UOM", salesuom);
		        			  		htInv.put("UOMDESC", salesuom);
		        			  		htInv.put("Display", salesuom);
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER,  map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END SALES UOM
		        			  	
							   //check if Inventory UOM exists 
		        			  	Hashtable HtInvuom = new Hashtable();
		        			  	HtInvuom.put(IDBConstants.PLANT, childplant);
		        			  	HtInvuom.put("UOM", inventoryuom);
		        			  	flag = new UomUtil().isExistsUom(HtInvuom);
		        			  	if(inventoryuom.length()>0) {
		        			  	if (flag == false) {
						    	  htInv.clear();
						    	  htInv.put(IDBConstants.PLANT,childplant);
						    	  htInv.put("UOM", inventoryuom);
						    	  htInv.put("UOMDESC", inventoryuom);
						    	  htInv.put("Display",inventoryuom);
						    	  htInv.put("QPUOM", "1");
						    	  htInv.put(IConstants.ISACTIVE, "Y");
						    	  htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
						    	  htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
						    	  boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	}
		        			  	//END INV UOM
		        			  	
		        			  	//check if Stk UOM exists 
		        			  	Hashtable HtStkuom = new Hashtable();
		        			  	HtStkuom.put(IDBConstants.PLANT, childplant);
		        			  	HtStkuom.put("UOM", map.get(IConstants.STKUOM));
		        			  	flag = new UomUtil().isExistsUom(HtStkuom);
		        			  	if (flag == false) {
		        			  		htInv.clear();
		        			  		htInv.put(IDBConstants.PLANT,childplant);
		        			  		htInv.put("UOM", map.get(IConstants.STKUOM));
		        			  		htInv.put("UOMDESC", map.get(IConstants.STKUOM));
		        			  		htInv.put("Display",map.get(IConstants.STKUOM));
		        			  		htInv.put("QPUOM", "1");
		        			  		htInv.put(IConstants.ISACTIVE, "Y");
		        			  		htInv.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
		        			  		htInv.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
		        			  		boolean uomInserted = new UomUtil().insertUom(htInv);
		        			  	}
		        			  	//END STOCK UOM
		        			  
		        			//HSCODE
		        			  	if(hscode.length()>0) {
		        			  	if (!new MasterUtil().isExistHSCODE(hscode, childplant)) 
								{						
					    			Hashtable htHS = new Hashtable();
					    			htHS.put(IDBConstants.PLANT,childplant);
					    			htHS.put(IDBConstants.HSCODE,hscode);
					    			htHS.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htHS.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddHSCODE(htHS);
									boolean insertflag = new MasterDAO().InsertHSCODE(htHS);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_HSCODE);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",hscode);
									htRecvHis.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean HSmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        			  	//HSCODE END
		        			  	
		        			  	//COO 
		        			  	if(coo.length()>0) {
		        				if (!new MasterUtil().isExistCOO(coo, childplant)) 
								{						
					    			Hashtable htCoo = new Hashtable();
					    			htCoo.put(IDBConstants.PLANT,childplant);
					    			htCoo.put(IDBConstants.COO,coo);
					    			htCoo.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
					    			htCoo.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
//									boolean insertflag = new MasterUtil().AddCOO(htCoo);
									boolean insertflag = new MasterDAO().InsertCOO(htCoo);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, childplant);
									htRecvHis.put("DIRTYPE",TransactionConstants.ADD_COO);
									htRecvHis.put("ORDNUM","");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS",coo);
									htRecvHis.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
									htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									boolean COOmovhis = new MovHisDAO().insertIntoMovHis(htRecvHis);
								}
		        			  	}
		        				//COO END
		        				
		        				//SUPPLIER START 
		        				ArrayList arrCust = new CustUtil().getVendorDetails(VENDNO,(String) map.get("PLANT"));
		        				if (arrCust.size() > 0) {
		        				String sCustCode = (String) arrCust.get(0);
		        				String sCustName = (String) arrCust.get(1);
		        				if (!new CustUtil().isExistVendor(VENDNO, childplant) && !new CustUtil().isExistVendorName(sCustName, childplant)) // if the Customer exists already 
		        				{
		        					String sAddr1 = (String) arrCust.get(2);
		        					String sAddr2 = (String) arrCust.get(3);
		        					String sAddr3 = (String) arrCust.get(4);
		        					String sAddr4 = (String) arrCust.get(15);
		        					String sCountry = (String) arrCust.get(5);
		        					String sZip = (String) arrCust.get(6);
		        					String sCons = (String) arrCust.get(7);
		        					String sContactName = (String) arrCust.get(8);
		        					String sDesgination = (String) arrCust.get(9);
		        					String sTelNo = (String) arrCust.get(10);
		        					String sHpNo = (String) arrCust.get(11);
		        					String sEmail = (String) arrCust.get(12);
		        					String sFax = (String) arrCust.get(13);
		        					String sRemarks = (String) arrCust.get(14);
		        					String isActive = (String) arrCust.get(16);
		        					String sPayTerms = (String) arrCust.get(17);
		        					String sPayMentTerms = (String) arrCust.get(39);
		        					String sPayInDays = (String) arrCust.get(18);
		        					String sState = (String) arrCust.get(19);
		        					String sRcbno = (String) arrCust.get(20);
		        					String CUSTOMEREMAIL = (String) arrCust.get(22);
		        					String WEBSITE = (String) arrCust.get(23);
		        					String FACEBOOK = (String) arrCust.get(24);
		        					String TWITTER = (String) arrCust.get(25);
		        					String LINKEDIN = (String) arrCust.get(26);
		        					String SKYPE = (String) arrCust.get(27);
		        					String OPENINGBALANCE = (String) arrCust.get(28);
		        					String WORKPHONE = (String) arrCust.get(29);
		        					String sTAXTREATMENT = (String) arrCust.get(30);
		        					String sCountryCode = (String) arrCust.get(31);
		        					String sBANKNAME = (String) arrCust.get(32);
		        					String sBRANCH= (String) arrCust.get(33);
		        					String sIBAN = (String) arrCust.get(34);
		        					String sBANKROUTINGCODE = (String) arrCust.get(35);
		        					String companyregnumber = (String) arrCust.get(36);
		        					String PEPPOL = (String) arrCust.get(40);
		        					String PEPPOL_ID = (String) arrCust.get(41);
		        					String CURRENCY = (String) arrCust.get(37);
		        					String transport = (String) arrCust.get(38);
		        					String suppliertypeid = (String) arrCust.get(21);
		        					Hashtable htsup = new Hashtable();
		        					htsup.put(IDBConstants.PLANT,childplant);
		        					htsup.put(IConstants.VENDOR_CODE, sCustCode);
		        					htsup.put(IConstants.VENDOR_NAME, sCustName);
		        					htsup.put(IConstants.companyregnumber,companyregnumber);
		        					htsup.put("ISPEPPOL", PEPPOL);
		        					htsup.put("PEPPOL_ID", PEPPOL_ID);
		        					htsup.put("CURRENCY_ID", CURRENCY);
		        					htsup.put(IConstants.NAME, sContactName);
		        					htsup.put(IConstants.DESGINATION, sDesgination);
		        					htsup.put(IConstants.TELNO, sTelNo);
		        					htsup.put(IConstants.HPNO, sHpNo);
		        					htsup.put(IConstants.FAX, sFax);
		        					htsup.put(IConstants.EMAIL, sEmail);
		        					htsup.put(IConstants.ADDRESS1, sAddr1);
		        					htsup.put(IConstants.ADDRESS2, sAddr2);
		        					htsup.put(IConstants.ADDRESS3, sAddr3);
		        					htsup.put(IConstants.ADDRESS4, sAddr4);
		        					if(sState.equalsIgnoreCase("Select State"))
		        						sState="";
		        					htsup.put(IConstants.STATE, strUtils.InsertQuotes(sState));
		        					htsup.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
		        					htsup.put(IConstants.ZIP, sZip);
		        					htsup.put(IConstants.USERFLG1, sCons);
		        					htsup.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
		        					htsup.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		        					htsup.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
		        					htsup.put(IConstants.PAYINDAYS, sPayInDays);
		        					htsup.put(IConstants.ISACTIVE, isActive);
		        					htsup.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
		        					htsup.put(IConstants.TRANSPORTID,transport);
		        					htsup.put(IConstants.RCBNO, sRcbno);
		        					htsup.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
		        					htsup.put(IConstants.WEBSITE,WEBSITE);
		        					htsup.put(IConstants.FACEBOOK,FACEBOOK);
		        					htsup.put(IConstants.TWITTER,TWITTER);
		        					htsup.put(IConstants.LINKEDIN,LINKEDIN);
		        					htsup.put(IConstants.SKYPE,SKYPE);
		        					htsup.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
		        					htsup.put(IConstants.WORKPHONE,WORKPHONE);
		        					htsup.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
		        					if(sBANKNAME.equalsIgnoreCase("Select Bank"))
		        			        	  sBANKNAME="";
		        			          htsup.put(IDBConstants.BANKNAME,sBANKNAME);
		        			          htsup.put(IDBConstants.IBAN,sIBAN);
		        			          htsup.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
		        			          htsup.put("CRAT",new DateUtils().getDateTime());
		        			          htsup.put("CRBY",map.get("LOGIN_USER"));
		        			          htsup.put("Comment1", " 0 ");
		        			          boolean custInserted = new CustUtil().insertVendor(htsup);
		        				}
		        				}
		        				//Supplier END
		        				
					          posList = new ItemMstUtil().selectForReport(posquery,htData,"");
					          pos = new HashMap();
					          if (posList.size() > 0) {
					        	  for (int j=0; j < posList.size(); j++ ) {
					        		  pos = (Map) posList.get(j);
					        		  String outlet = (String) pos.get("OUTLET");
					        		  String posplant = (String) pos.get("PLANT");
					        	     Hashtable htCondition = new Hashtable();
					      	        	htCondition.put(IConstants.ITEM,(String) map.get("ITEM"));
					      	        	htCondition.put(IConstants.PLANT,childplant);
					      	        	htCondition.put("OUTLET",outlet);
					        		  Hashtable hPos = new Hashtable();	
					        		  hPos.put(IConstants.PLANT,childplant);
					        		  hPos.put("OUTLET",outlet);
					        		  hPos.put(IConstants.ITEM,(String) map.get("ITEM"));
					        		  hPos.put("SALESUOM",salesuom);
					        		  hPos.put("CRBY",map.get("LOGIN_USER"));
					        		  hPos.put("CRAT",new DateUtils().getDateTime());
					        		  if(!(itemdao.isExistsPosOutletPrice((String) map.get("ITEM"),outlet,childplant))) {
					        			  hPos.put(IConstants.PRICE,price);
					        			  boolean posInserted = new POSHdrDAO().insertIntoPosOutletPrice(hPos);
					        		  }else {
					        			  if(isprice_updateonly_in_ownoutlet.equalsIgnoreCase("0")) 
						        		  	  hPos.put(IConstants.PRICE,price);
					        			  boolean posUpdated = new POSHdrDAO().updatePosOutletPrice(hPos,htCondition);
					        		  }
					        	  }
					          }
		        			  }
		        		  }
		        	  }
		        	  
		        	  }
		          	}
		          }//IMTHI END IMPORT PRODUCT TO CHILD PARENT
				
				//added by azees 12-08-2022 ->DESC: get item and insert with CUSTOMERID from custmst tbl
		          CustUtil custUtils = new CustUtil(); 
		          CustMstDAO custMstDAO = new CustMstDAO(); 
		          ArrayList movQryLists =custUtils.getCustomerListWithType("",(String) map.get("PLANT"),"");
		          if (movQryLists.size() > 0) {
		  			for(int i =0; i<movQryLists.size(); i++) {
		  				Map arrCustLine = (Map)movQryLists.get(i);
		  				String customerNo=(String)arrCustLine.get("CUSTNO");
		  					Hashtable apporderht = new Hashtable();
		  					apporderht.put(IConstants.PLANT,(String) map.get("PLANT"));
		  					apporderht.put("CUSTNO",customerNo);
		  					apporderht.put(IConstants.ITEM,(String) map.get("ITEM"));
		  					apporderht.put(IConstants.ITEM_DESC,(String) map.get(IConstants.ITEM_DESC));
		  					apporderht.put("ORDER_QTY","0");
		  					apporderht.put("MAX_ORDER_QTY","0");
		  					apporderht.put("CRBY",map.get("LOGIN_USER"));
		  					apporderht.put("CRAT",new DateUtils().getDateTime());
		  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
		  			}
		          }
		          
		          //IMTHI START to insert for table APPORDERQTYCONFIG in child parent
		          if(PARENT_PLANT != null){
		        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
		        	  if(CHECKplantCompany == null)
		  				CHECKplantCompany = "0";
		        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  if (arrList.size() > 0) {
			        		  for (int i=0; i < arrList.size(); i++ ) {
			        			  Map m = (Map) arrList.get(i);
			        			  String childplant = (String) m.get("CHILD_PLANT");
						          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
						          if (movQryLists.size() > 0) {
						  			for(int t =0; t<movQryLists.size(); t++) {
						  				Map arrCustLine = (Map)movQryLists.get(t);
						  				String customerNo=(String)arrCustLine.get("CUSTNO");
						  					Hashtable apporderht = new Hashtable();
						  					apporderht.put(IConstants.PLANT,childplant);
						  					apporderht.put("CUSTNO",customerNo);
						  					apporderht.put(IConstants.ITEM,(String) map.get("ITEM"));
						  					apporderht.put(IConstants.ITEM_DESC,(String) map.get(IConstants.ITEM_DESC));
						  					apporderht.put("ORDER_QTY","0");
						  					apporderht.put("MAX_ORDER_QTY","0");
						  					apporderht.put("CRBY",map.get("LOGIN_USER"));
						  					apporderht.put("CRAT",new DateUtils().getDateTime());
						  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
						  			}
						          }
			        		  	}
			        	  	}
		        	  	}
	             	}else if(PARENT_PLANT == null){
	             		boolean ischild = false;
			        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
			        	  if (arrLi.size() > 0) {
			        	  Map mst = (Map) arrLi.get(0);
			        	  String parent = (String) mst.get("PARENT_PLANT");
			         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
			        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
			        	  if(Ischildasparent){
			        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        			  ischild = true;
			        		  }
			        	  }else{
			        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
			        		  ischild = true;
			        	  }
			        	  }
			        	  if(ischild){
			        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
				        	  String  parentplant ="";
				        	  if (arrLists.size() > 0) {
				        		  for (int i=0; i < arrLists.size(); i++ ) {
				        			  Map ms = (Map) arrLists.get(i);
				        			  parentplant = (String) ms.get("PARENT_PLANT");
							          movQryLists =custUtils.getCustomerListWithType("",parentplant,"");
							          if (movQryLists.size() > 0) {
							  			for(int t =0; t<movQryLists.size(); t++) {
							  				Map arrCustLine = (Map)movQryLists.get(t);
							  				String customerNo=(String)arrCustLine.get("CUSTNO");
							  					Hashtable apporderht = new Hashtable();
							  					apporderht.put(IConstants.PLANT,parentplant);
							  					apporderht.put("CUSTNO",customerNo);
							  					apporderht.put(IConstants.ITEM,(String) map.get("ITEM"));
							  					apporderht.put(IConstants.ITEM_DESC,(String) map.get(IConstants.ITEM_DESC));
							  					apporderht.put("ORDER_QTY","0");
							  					apporderht.put("MAX_ORDER_QTY","0");
							  					apporderht.put("CRBY",map.get("LOGIN_USER"));
							  					apporderht.put("CRAT",new DateUtils().getDateTime());
							  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
							  			}
							          }
				        		  }
				        	  }
				        	  
				        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
				        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
				        	  Map m = new HashMap();
				        	  if (arrList.size() > 0) {
				        		  for (int i=0; i < arrList.size(); i++ ) {
				        			  m = (Map) arrList.get(i);
				        			  String childplant = (String) m.get("CHILD_PLANT");
				        			  if(childplant!=(String) map.get("PLANT")) {
								          movQryLists =custUtils.getCustomerListWithType("",childplant,"");
								          if (movQryLists.size() > 0) {
								  			for(int t =0; t<movQryLists.size(); t++) {
								  				Map arrCustLine = (Map)movQryLists.get(t);
								  				String customerNo=(String)arrCustLine.get("CUSTNO");
								  					Hashtable apporderht = new Hashtable();
								  					apporderht.put(IConstants.PLANT,childplant);
								  					apporderht.put("CUSTNO",customerNo);
								  					apporderht.put(IConstants.ITEM,(String) map.get("ITEM"));
								  					apporderht.put(IConstants.ITEM_DESC,(String) map.get(IConstants.ITEM_DESC));
								  					apporderht.put("ORDER_QTY","0");
								  					apporderht.put("MAX_ORDER_QTY","0");
								  					apporderht.put("CRBY",map.get("LOGIN_USER"));
								  					apporderht.put("CRAT",new DateUtils().getDateTime());
								  					boolean itemorderInserted = custMstDAO.insertAppQty(apporderht);
								  			}
								          }
				        			  }
				        		  	}
				        	  	}
			        	  }	
	             		}
	             	  }//IMTHI END
			}
			
			
			

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}
	
	public boolean processDescCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			ItemUtil itemUtil = new ItemUtil();
			ItemMstDAO itemdao = new ItemMstDAO();
			List DETAILDESC = new ArrayList();
			StrUtils strUtils = new StrUtils();
			boolean result = false;
			String Plant = (String) map.get(IConstants.PLANT);
			String item_id = (String) map.get(IConstants.ITEM);
			String item_desc1 = (String) map.get("ITEM_DESC1");
			String item_desc2 = (String) map.get("ITEM_DESC2");
			String item_desc3 = (String) map.get("ITEM_DESC3");
			String item_desc4 = (String) map.get("ITEM_DESC4");
			String item_desc5 = (String) map.get("ITEM_DESC5");
			String item_desc6 = (String) map.get("ITEM_DESC6");
			String item_desc7 = (String) map.get("ITEM_DESC7");
			String item_desc8 = (String) map.get("ITEM_DESC8");
			String item_desc9 = (String) map.get("ITEM_DESC9");
			String item_desc10 = (String) map.get("ITEM_DESC10");
			if(!item_desc1.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc1);
			}
			if(!item_desc2.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc2);
			}
			if(!item_desc3.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc3);
			}
			if(!item_desc4.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc4);
			}
			if(!item_desc5.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc5);
			}
			if(!item_desc6.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc6);
			}
			if(!item_desc7.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc7);
			}
			if(!item_desc8.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc8);
			}
			if(!item_desc9.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc9);
			}
			if(!item_desc10.equalsIgnoreCase("")) {
				DETAILDESC.add(item_desc10);
			}
				
		    String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
		    boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			boolean deleteDesc=itemdao.removeAdditionalDesc(Plant,item_id, "");
			 for(int i =0 ; i < DETAILDESC.size() ; i++){
       		  String ITEMDESC = (String) DETAILDESC.get(i);
			  Hashtable HM = new Hashtable();
			  HM.put(IConstants.PLANT, Plant);
			  HM.put(IConstants.ITEM, item_id);
			  HM.put("ITEMDETAILDESC", ITEMDESC);
			  HM.put("CRAT",new DateUtils().getDateTime());
			  HM.put("CRBY",map.get("LOGIN_USER"));
			  flag = itemUtil.insertDetailDesc(HM);
			 }
			
			if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  deleteDesc=itemdao.removeAdditionalDesc(childplant,item_id, "");
		        					for(int j =0 ; j < DETAILDESC.size() ; j++){
		        			       		  String ITEMDESC = (String) DETAILDESC.get(j);
		        						  Hashtable HM = new Hashtable();
		        						  HM.put(IConstants.PLANT, childplant);
		        						  HM.put(IConstants.ITEM, item_id);
		        						  HM.put("ITEMDETAILDESC", ITEMDESC);
		        						  HM.put("CRAT",new DateUtils().getDateTime());
		        						  HM.put("CRBY",map.get("LOGIN_USER"));
		        						  flag = itemUtil.insertDetailDesc(HM);
		        						 }
		        		  	}
		        	  	}
	        	  	}
           	}else if(PARENT_PLANT == null){
           		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  deleteDesc=itemdao.removeAdditionalDesc(parentplant,item_id, "");
			        					for(int k =0 ; k < DETAILDESC.size() ; k++){
			        			       		  String ITEMDESC = (String) DETAILDESC.get(k);
			        						  Hashtable HM = new Hashtable();
			        						  HM.put(IConstants.PLANT, parentplant);
			        						  HM.put(IConstants.ITEM, item_id);
			        						  HM.put("ITEMDETAILDESC", ITEMDESC);
			        						  HM.put("CRAT",new DateUtils().getDateTime());
			        						  HM.put("CRBY",map.get("LOGIN_USER"));
			        						  flag = itemUtil.insertDetailDesc(HM);
			        						 }
			        		  		}
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  Map mt = (Map) arrList.get(k);
			        			  String childplant = (String) mt.get("CHILD_PLANT");
			        			  if(childplant!=(String) map.get("PLANT")) {
			        			  deleteDesc=itemdao.removeAdditionalDesc(childplant,item_id, "");
				        					for(int l =0 ; l < DETAILDESC.size() ; l++){
				        			       		  String ITEMDESC = (String) DETAILDESC.get(l);
				        						  Hashtable HM = new Hashtable();
				        						  HM.put(IConstants.PLANT, childplant);
				        						  HM.put(IConstants.ITEM, item_id);
				        						  HM.put("ITEMDETAILDESC", ITEMDESC);
				        						  HM.put("CRAT",new DateUtils().getDateTime());
				        						  HM.put("CRBY",map.get("LOGIN_USER"));
				        						  flag = itemUtil.insertDetailDesc(HM);
				        						 }
			        			  	}
			        		  	}
			        	  	}
		        	  	}
           			}
           	  }//IMTHI END	

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}
	
	public boolean processImgCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;
		
		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			itemdao.setmLogger(mLogger);
			List IMGCOUNT = new ArrayList();
			String Plant = (String) map.get(IConstants.PLANT);
			String item_id = (String) map.get(IConstants.ITEM);
			String catalog1 = (String) map.get("IMG1");
			String catalog2 = (String) map.get("IMG2");
			String catalog3 = (String) map.get("IMG3");
			String catalog4 = (String) map.get("IMG4");
			String catalog5 = (String) map.get("IMG5");
			
			if(!catalog1.equalsIgnoreCase("")) {
				IMGCOUNT.add(catalog1);
			}
			if(!catalog2.equalsIgnoreCase("")) {
				IMGCOUNT.add(catalog2);
			}
			if(!catalog3.equalsIgnoreCase("")) {
				IMGCOUNT.add(catalog3);
			}
			if(!catalog4.equalsIgnoreCase("")) {
				IMGCOUNT.add(catalog4);
			}
			if(!catalog5.equalsIgnoreCase("")) {
				IMGCOUNT.add(catalog5);
			}
			
			boolean iscatalog = false;
			boolean imageSizeflg = false;
			StrUtils strUtils = new StrUtils();
			Hashtable ht = new Hashtable();
			ht.put("PRODUCTID", item_id);
			ht.put("PLANT", Plant);
			boolean itemAddImgDlt = new CatalogDAO().delMst(ht);
			
			if(PARENT_PLANT != null){
				String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
				if(CHECKplantCompany == null)
					CHECKplantCompany = "0";
				if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
					String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
					ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
					if (arrList.size() > 0) {
						for (int z=0; z < arrList.size(); z++ ) {
							Map m = (Map) arrList.get(z);
							String childplant = (String) m.get("CHILD_PLANT");
								ht.put("PLANT", childplant);
								itemAddImgDlt = new CatalogDAO().delMst(ht);
						}
					}
				}
			}else if(PARENT_PLANT == null){
				boolean ischild = false;
				String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
				ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
				if (arrLi.size() > 0) {
					Map mst = (Map) arrLi.get(0);
					String parent = (String) mst.get("PARENT_PLANT");
					String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
					if(CHECKplantCompany == null) CHECKplantCompany = "0";
					if(Ischildasparent){
						if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
							ischild = true;
						}
					}else{
						if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
							ischild = true;
						}
					}
					if(ischild){
						String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
						ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
						String  parentplant ="";
						if (arrLists.size() > 0) {
							for (int j=0; j < arrLists.size(); j++ ) {
								Map ms = (Map) arrLists.get(j);
									parentplant = (String) ms.get("PARENT_PLANT");
									ht.put("PLANT", parentplant);
									itemAddImgDlt = new CatalogDAO().delMst(ht);
							}
						}
						
						String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
						ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
						Map m = new HashMap();
						if (arrList.size() > 0) {
							for (int k=0; k < arrList.size(); k++ ) {
								Map mt = (Map) arrList.get(k);
								String childplant = (String) mt.get("CHILD_PLANT");
								if(childplant!=(String) map.get("PLANT")) {
										ht.put("PLANT", childplant);
										itemAddImgDlt = new CatalogDAO().delMst(ht);
								}
							}
						}
					}
				}
			}
			
			for(int i =0 ; i < IMGCOUNT.size() ; i++){
				String catlogue = (String) IMGCOUNT.get(i);
				int count = i+2;
				File file = new File(catlogue);
				if(file.exists()) {
					String fileLocation = DbBean.COMPANY_CATALOG_PATH + "/"+ Plant;
					List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
					String strpath="",results="";
					iscatalog = true;
					String fileName = file.getName();
					String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					System.out.println("Extensions:::::::" + extension);
					if (!imageFormatsList.contains(extension)) {
						results = "<font color=\"red\"> Image extension not valid </font>";
						imageSizeflg = true;
					}
					
					File path = new File(fileLocation);
					if (!path.exists()) {
						boolean status = path.mkdirs();
					}
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					File uploadedFile = new File(path + "/" +(item_id+"_"+count)+ ".JPEG");
					String img = uploadedFile.toString();
					if (uploadedFile.exists()) {
						uploadedFile.delete();
					}
					
					strpath = path + "/" + fileName;
					catlogue = uploadedFile.getAbsolutePath();
					File imgpath = new File(catlogue);
					OutputStream out = new FileOutputStream(imgpath);
					byte[] writfile = getBytesFromFile(file);
					int fileLength = writfile.length;
					out.write(writfile, 0, fileLength);// Write your data
					out.close();
						
						Hashtable htCatalog = new Hashtable();
						String catlogpath="";
						int usertime=0;
		      		  	catlogpath = img;
		      		  	usertime = i+2;
		      		  	Hashtable HM = new Hashtable();
		      		  	htCatalog.put(IDBConstants.PRODUCTID, item_id);
		      		  	htCatalog.put(IConstants.PLANT, Plant);
					  	htCatalog.put(IConstants.CREATED_BY, map.get("LOGIN_USER"));
					  	htCatalog.put(IConstants.ISACTIVE, "Y");
					  	htCatalog.put(IDBConstants.CATLOGPATH, catlogue);
					  	htCatalog.put("USERTIME1", Integer.toString(usertime));
					  	flag =  new CatalogUtil().insertMst(htCatalog);
					
					if(PARENT_PLANT != null){
						String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
						if(CHECKplantCompany == null)
							CHECKplantCompany = "0";
						if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
							String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
							ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
							if (arrList.size() > 0) {
								for (int z=0; z < arrList.size(); z++ ) {
									Map m = (Map) arrList.get(z);
									String childplant = (String) m.get("CHILD_PLANT");
										File files = new File(catlogue);
										String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
										List<String> imageFormatsLists = Arrays.asList(DbBean.imageFormatsArray);
										String strpaths="";
										iscatalog = true;
										String fileNames = file.getName();
										String extensions = fileName.substring(fileNames.lastIndexOf(".")).toLowerCase();
										System.out.println("Extensions:::::::" + extensions);
										if (!imageFormatsLists.contains(extensions)) {
											results = "<font color=\"red\"> Image extension not valid </font>";
											imageSizeflg = true;
										}
										
										File paths = new File(fileLocations);
										if (!paths.exists()) {
											boolean status = paths.mkdirs();
										}
										fileNames = fileNames.substring(fileNames.lastIndexOf("\\") + 1);
										File uploadedFiles = new File(paths + "/" +(item_id+"_"+count)+ ".JPEG");
										if (uploadedFiles.exists()) {
											uploadedFiles.delete();
										}
										strpaths = paths + "/" + fileNames;
										String catlogues = uploadedFiles.getAbsolutePath();
										File imgpaths = new File(catlogues);
										OutputStream outs = new FileOutputStream(imgpaths);
										byte[] writfiles = getBytesFromFile(files);
										int fileLengths = writfiles.length;
										outs.write(writfiles, 0, fileLengths);// Write your data
										outs.close();
										
//										ht.put("PLANT", childplant);
//										itemAddImgDlt = new CatalogDAO().delMst(ht);
										htCatalog.put(IConstants.PLANT, childplant);
										htCatalog.put(IDBConstants.CATLOGPATH, catlogues);
										flag=  new CatalogUtil().insertMst(htCatalog);
										
								}
							}
						}
					}else if(PARENT_PLANT == null){
						boolean ischild = false;
						String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
						ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
						if (arrLi.size() > 0) {
							Map mst = (Map) arrLi.get(0);
							String parent = (String) mst.get("PARENT_PLANT");
							String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
							if(CHECKplantCompany == null) CHECKplantCompany = "0";
							if(Ischildasparent){
								if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
									ischild = true;
								}
							}else{
								if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
									ischild = true;
								}
							}
							if(ischild){
								String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
								ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
								String  parentplant ="";
								if (arrLists.size() > 0) {
									for (int j=0; j < arrLists.size(); j++ ) {
										Map ms = (Map) arrLists.get(j);
											parentplant = (String) ms.get("PARENT_PLANT");
											File files = new File(catlogue);
											String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ parentplant;
											List<String> imageFormatsLists = Arrays.asList(DbBean.imageFormatsArray);
											String strpaths="";
											iscatalog = true;
											String fileNames = file.getName();
											String extensions = fileName.substring(fileNames.lastIndexOf(".")).toLowerCase();
											System.out.println("Extensions:::::::" + extensions);
											if (!imageFormatsLists.contains(extensions)) {
												results = "<font color=\"red\"> Image extension not valid </font>";
												imageSizeflg = true;
											}
											
											File paths = new File(fileLocations);
											if (!paths.exists()) {
												boolean status = paths.mkdirs();
											}
											fileNames = fileNames.substring(fileNames.lastIndexOf("\\") + 1);
											File uploadedFiles = new File(paths + "/" +(item_id+"_"+count)+ ".JPEG");
											if (uploadedFiles.exists()) {
												uploadedFiles.delete();
											}
											strpaths = paths + "/" + fileNames;
											String catlogues = uploadedFiles.getAbsolutePath();
											File imgpaths = new File(catlogues);
											OutputStream outs = new FileOutputStream(imgpaths);
											byte[] writfiles = getBytesFromFile(files);
											int fileLengths = writfiles.length;
											outs.write(writfiles, 0, fileLengths);// Write your data
											outs.close();
											
//											ht.put("PLANT", parentplant);
//											itemAddImgDlt = new CatalogDAO().delMst(ht);
											htCatalog.put(IConstants.PLANT, parentplant);
											flag=  new CatalogUtil().insertMst(htCatalog);
									}
								}
								
								String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
								ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
								Map m = new HashMap();
								if (arrList.size() > 0) {
									for (int k=0; k < arrList.size(); k++ ) {
										Map mt = (Map) arrList.get(k);
										String childplant = (String) mt.get("CHILD_PLANT");
										if(childplant!=(String) map.get("PLANT")) {
												File files = new File(catlogue);
												String fileLocations = DbBean.COMPANY_CATALOG_PATH + "/"+ childplant;
												List<String> imageFormatsLists = Arrays.asList(DbBean.imageFormatsArray);
												String strpaths="";
												iscatalog = true;
												String fileNames = file.getName();
												String extensions = fileName.substring(fileNames.lastIndexOf(".")).toLowerCase();
												System.out.println("Extensions:::::::" + extensions);
												if (!imageFormatsLists.contains(extensions)) {
													results = "<font color=\"red\"> Image extension not valid </font>";
													imageSizeflg = true;
												}
												
												File paths = new File(fileLocations);
												if (!paths.exists()) {
													boolean status = paths.mkdirs();
												}
												fileNames = fileNames.substring(fileNames.lastIndexOf("\\") + 1);
												File uploadedFiles = new File(paths + "/" +(item_id+"_"+count)+ ".JPEG");
												if (uploadedFiles.exists()) {
													uploadedFiles.delete();
												}
												strpaths = paths + "/" + fileNames;
												String catlogues = uploadedFiles.getAbsolutePath();
												File imgpaths = new File(catlogues);
												OutputStream outs = new FileOutputStream(imgpaths);
												byte[] writfiles = getBytesFromFile(files);
												int fileLengths = writfiles.length;
												outs.write(writfiles, 0, fileLengths);// Write your data
												outs.close();
												
//												ht.put("PLANT", childplant);
//												itemAddImgDlt = new CatalogDAO().delMst(ht);
												htCatalog.put(IConstants.PLANT, childplant);
												flag=  new CatalogUtil().insertMst(htCatalog);
												
										}
									}
								}
							}
						}
					}
			} 
		}
			
//			Hashtable ht = new Hashtable();
//			ht.put("PRODUCTID", item_id);
//    	 	ht.put("PLANT", Plant);
//			boolean itemAddImgDlt = new CatalogDAO().delMst(ht);
//			Hashtable htCatalog = new Hashtable();
//			String catlogpath="";
//			int usertime=0;
//			 for(int i =0 ; i < IMGCOUNT.size() ; i++){
//      		  catlogpath = (String) IMGCOUNT.get(i);
//      		  usertime = i+2;
//			  Hashtable HM = new Hashtable();
//			  htCatalog.put(IDBConstants.PRODUCTID, item_id);
//				htCatalog.put(IConstants.PLANT, Plant);
//				htCatalog.put(IConstants.CREATED_BY, map.get("LOGIN_USER"));
//				htCatalog.put(IConstants.ISACTIVE, "Y");
//				htCatalog.put(IDBConstants.CATLOGPATH, catlogpath);
//				htCatalog.put("USERTIME1", Integer.toString(usertime));
//				flag =  new CatalogUtil().insertMst(htCatalog);
//			 }
			 	
			
			
			if(PARENT_PLANT != null){}else if(PARENT_PLANT == null){}//IMTHI END	
			
		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}
	
	public boolean processItemCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			ItemUtil itemUtil = new ItemUtil();
			ItemMstDAO itemdao = new ItemMstDAO();
			List ADDITEMS = new ArrayList();
			StrUtils strUtils = new StrUtils();
			boolean result = false;
			String Plant = (String) map.get(IConstants.PLANT);
			String item_id = (String) map.get(IConstants.ITEM);
			String item1 = (String) map.get("ITEM1");
			String item2 = (String) map.get("ITEM2");
			String item3 = (String) map.get("ITEM3");
			String item4 = (String) map.get("ITEM4");
			String item5 = (String) map.get("ITEM5");
			String item6 = (String) map.get("ITEM6");
			String item7 = (String) map.get("ITEM7");
			String item8 = (String) map.get("ITEM8");
			String item9 = (String) map.get("ITEM9");
			String item10 = (String) map.get("ITEM10");
			if(!item1.equalsIgnoreCase("")) {
				ADDITEMS.add(item1);
			}
			if(!item2.equalsIgnoreCase("")) {
				ADDITEMS.add(item2);
			}
			if(!item3.equalsIgnoreCase("")) {
				ADDITEMS.add(item3);
			}
			if(!item4.equalsIgnoreCase("")) {
				ADDITEMS.add(item4);
			}
			if(!item5.equalsIgnoreCase("")) {
				ADDITEMS.add(item5);
			}
			if(!item6.equalsIgnoreCase("")) {
				ADDITEMS.add(item6);
			}
			if(!item7.equalsIgnoreCase("")) {
				ADDITEMS.add(item7);
			}
			if(!item8.equalsIgnoreCase("")) {
				ADDITEMS.add(item8);
			}
			if(!item9.equalsIgnoreCase("")) {
				ADDITEMS.add(item9);
			}
			if(!item10.equalsIgnoreCase("")) {
				ADDITEMS.add(item10);
			}
				
		    String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
		    boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			boolean deletePrd=itemdao.removeAdditionalPrd(Plant,item_id, "");
			 for(int i =0 ; i < ADDITEMS.size() ; i++){
       		  String ADDIONALITEM = (String) ADDITEMS.get(i);
			  Hashtable HM = new Hashtable();
			  HM.put(IConstants.PLANT, Plant);
			  HM.put(IConstants.ITEM, item_id);
			  HM.put("ADDITIONALITEM", ADDIONALITEM);
			  HM.put("CRAT",new DateUtils().getDateTime());
			  HM.put("CRBY",map.get("LOGIN_USER"));
			  flag = itemUtil.insertAdditionalPrd(HM);
			 }
			
			if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        				deletePrd=itemdao.removeAdditionalPrd(childplant,item_id, "");
		        					for(int j =0 ; j < ADDITEMS.size() ; j++){
		        			       		  String ADDIONALITEM = (String) ADDITEMS.get(j);
		        						  Hashtable HM = new Hashtable();
		        						  HM.put(IConstants.PLANT, childplant);
		        						  HM.put(IConstants.ITEM, item_id);
		        						  HM.put("ADDITIONALITEM", ADDIONALITEM);
		        						  HM.put("CRAT",new DateUtils().getDateTime());
		        						  HM.put("CRBY",map.get("LOGIN_USER"));
		        						  flag = itemUtil.insertAdditionalPrd(HM);
		        						 }
		        		  	}
		        	  	}
	        	  	}
           	}else if(PARENT_PLANT == null){
           		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }}
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  deletePrd=itemdao.removeAdditionalPrd(parentplant,item_id, "");
			        					for(int k =0 ; k < ADDITEMS.size() ; k++){
			        			       		  String ADDIONALITEM = (String) ADDITEMS.get(k);
			        						  Hashtable HM = new Hashtable();
			        						  HM.put(IConstants.PLANT, parentplant);
			        						  HM.put(IConstants.ITEM, item_id);
			        						  HM.put("ADDITIONALITEM", ADDIONALITEM);
			        						  HM.put("CRAT",new DateUtils().getDateTime());
			        						  HM.put("CRBY",map.get("LOGIN_USER"));
			        						  flag = itemUtil.insertAdditionalPrd(HM);
			        						 }
			        		  		}
			        	  }
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  Map mt = (Map) arrList.get(k);
			        			  String childplant = (String) mt.get("CHILD_PLANT");
			        			  deletePrd=itemdao.removeAdditionalPrd(childplant,item_id, "");
			        			  if(childplant!=(String) map.get("PLANT")) {
				        					for(int l =0 ; l < ADDITEMS.size() ; l++){
				        			       		  String ADDIONALITEM = (String) ADDITEMS.get(l);
				        						  Hashtable HM = new Hashtable();
				        						  HM.put(IConstants.PLANT, childplant);
				        						  HM.put(IConstants.ITEM, item_id);
				        						  HM.put("ADDITIONALITEM", ADDIONALITEM);
				        						  HM.put("CRAT",new DateUtils().getDateTime());
				        						  HM.put("CRBY",map.get("LOGIN_USER"));
				        						  flag = itemUtil.insertAdditionalPrd(HM);
				        						 }
			        			  	}
			        		  	}
			        	  	}
		        	  	}
           			}
           	  }//IMTHI END	

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		try {
			movHisDao.setmLogger(mLogger);
			Hashtable htmov = new Hashtable();
			htmov.clear();
			htmov.put("PLANT", map.get("PLANT"));
			htmov.put("DIRTYPE", TransactionConstants.CNT_ITEM_UPLOAD);
			htmov.put("Item", map.get("ITEM"));
			//htmov.put("LOC",map.get("ITEM_LOC"));
			htmov.put("LOC","");
			htmov.put("MOVTID", "");
			htmov.put("RECID", "");
			htmov.put("PONO", "");
			htmov.put("CRBY", map.get("LOGIN_USER"));
			htmov.put("TRANDATE", dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htmov.put("CRAT", dateUtils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htmov);
			
			String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
			boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			//IMTHI ADD for CHILD PARENT to insert Activity log
          	if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  htmov.put("PLANT", childplant);
		        			  movHisDao.insertIntoMovHis(htmov);
		        		  	}
		        	  	}
	        	  	}
             	}else if(PARENT_PLANT == null){
             		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }
		        	  }
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  htmov.put("PLANT", parentplant);
			        			  movHisDao.insertIntoMovHis(htmov);
			        		  }
			        	  }
			        	  
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  Map mt = (Map) arrList.get(k);
			        			  String childplant = (String) mt.get("CHILD_PLANT");
			        			  if(childplant!=(String) map.get("PLANT")) {
			        				  htmov.put("PLANT", childplant);
			        				  movHisDao.insertIntoMovHis(htmov);
			        			  }
			        		  	}
			        	  	}
		        	  	}	
             		}
             	  }//IMTHI END

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	
	private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
                // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                                + file.getName());
        }
 System.out.println("read the bytes");	        
        is.close();
        return bytes;
}

	public boolean processWmsTranItemSup(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			ItemUtil itemUtil = new ItemUtil();
			ItemMstDAO itemdao = new ItemMstDAO();
			List ADDSUP = new ArrayList();
			StrUtils strUtils = new StrUtils();
			boolean result = false;
			String Plant = (String) map.get(IConstants.PLANT);
			String item_id = (String) map.get(IConstants.ITEM);
			String sup1 = (String) map.get("SUP1");
			String sup2 = (String) map.get("SUP2");
			String sup3 = (String) map.get("SUP3");
			String sup4 = (String) map.get("SUP4");
			String sup5 = (String) map.get("SUP5");
			String sup6 = (String) map.get("SUP6");
			String sup7 = (String) map.get("SUP7");
			String sup8 = (String) map.get("SUP8");
			String sup9 = (String) map.get("SUP9");
			String sup10 = (String) map.get("SUP10");
			if(!sup1.equalsIgnoreCase("")) {
				ADDSUP.add(sup1);
			}
			if(!sup2.equalsIgnoreCase("")) {
				ADDSUP.add(sup2);
			}
			if(!sup3.equalsIgnoreCase("")) {
				ADDSUP.add(sup3);
			}
			if(!sup4.equalsIgnoreCase("")) {
				ADDSUP.add(sup4);
			}
			if(!sup5.equalsIgnoreCase("")) {
				ADDSUP.add(sup5);
			}
			if(!sup6.equalsIgnoreCase("")) {
				ADDSUP.add(sup6);
			}
			if(!sup7.equalsIgnoreCase("")) {
				ADDSUP.add(sup7);
			}
			if(!sup8.equalsIgnoreCase("")) {
				ADDSUP.add(sup8);
			}
			if(!sup9.equalsIgnoreCase("")) {
				ADDSUP.add(sup9);
			}
			if(!sup10.equalsIgnoreCase("")) {
				ADDSUP.add(sup10);
			}
				
		    String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD((String) map.get("PLANT"));//Check Parent Plant or child plant
		    boolean Ischildasparent= new ParentChildCmpDetDAO().getisChildAsParent((String) map.get("PLANT"));//imti added to check child plant as parent or not
			Hashtable htData = new Hashtable();
			
			boolean deleteItemSupplier=itemdao.removeItemsupplier(Plant,item_id);
//			boolean deletePrd=itemdao.removeAdditionalPrd(Plant,item_id, "");
			 for(int i =0 ; i < ADDSUP.size() ; i++){
       		  String ADDIONALITEM = (String) ADDSUP.get(i);
			  Hashtable HM = new Hashtable();
			  HM.put(IConstants.PLANT, Plant);
			  HM.put(IConstants.ITEM, item_id);
			  HM.put(IConstants.VENDNO, ADDIONALITEM);
			  HM.put("CRAT",new DateUtils().getDateTime());
			  HM.put("CRBY",map.get("LOGIN_USER"));
//			  flag = itemUtil.insertAdditionalPrd(HM);
			  flag = itemUtil.insertItemSupplier(HM);
			 }
			
			if(PARENT_PLANT != null){
	        	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany((String) map.get("PLANT"));
	        	  if(CHECKplantCompany == null)
	  				CHECKplantCompany = "0";
	        	  if(CHECKplantCompany.equalsIgnoreCase("1") || CHECKplantCompany.equalsIgnoreCase("3")) {
	        		  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  deleteItemSupplier=itemdao.removeItemsupplier(childplant,item_id);
		        					for(int j =0 ; j < ADDSUP.size() ; j++){
		        			       		  String ADDIONALITEM = (String) ADDSUP.get(j);
		        						  Hashtable HM = new Hashtable();
		        						  HM.put(IConstants.PLANT, childplant);
		        						  HM.put(IConstants.ITEM, item_id);
		        						  HM.put(IConstants.VENDNO, ADDIONALITEM);
		        						  HM.put("CRAT",new DateUtils().getDateTime());
		        						  HM.put("CRBY",map.get("LOGIN_USER"));
//		        						  flag = itemUtil.insertAdditionalPrd(HM);
		        						  flag = itemUtil.insertItemSupplier(HM);
		        						 }
		        		  	}
		        	  	}
	        	  	}
           	}else if(PARENT_PLANT == null){
           		boolean ischild = false;
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		         	  String CHECKplantCompany = new PlantMstDAO().getProducttoOtherCompany(parent);
		        	  if(CHECKplantCompany == null) CHECKplantCompany = "0";
		        	  if(Ischildasparent){
		        		  if(CHECKplantCompany.equalsIgnoreCase("1") ||CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        			  ischild = true;
		        		  }
		        	  }else{
		        	  if(CHECKplantCompany.equalsIgnoreCase("2") || CHECKplantCompany.equalsIgnoreCase("3")) {
		        		  ischild = true;
		        	  }}
		        	  if(ischild){
		        		  String querys="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+(String) map.get("PLANT")+"'";
			        	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
			        	  String  parentplant ="";
			        	  if (arrLists.size() > 0) {
			        		  for (int j=0; j < arrLists.size(); j++ ) {
			        			  Map ms = (Map) arrLists.get(j);
			        			  parentplant = (String) ms.get("PARENT_PLANT");
			        			  deleteItemSupplier=itemdao.removeItemsupplier(parentplant,item_id);
			        					for(int k =0 ; k < ADDSUP.size() ; k++){
			        			       		  String ADDIONALITEM = (String) ADDSUP.get(k);
			        						  Hashtable HM = new Hashtable();
			        						  HM.put(IConstants.PLANT, parentplant);
			        						  HM.put(IConstants.ITEM, item_id);
			        						  HM.put(IConstants.VENDNO, ADDIONALITEM);
			        						  HM.put("CRAT",new DateUtils().getDateTime());
			        						  HM.put("CRBY",map.get("LOGIN_USER"));
//			        						  flag = itemUtil.insertAdditionalPrd(HM);
			        						  flag = itemUtil.insertItemSupplier(HM);
			        						 }
			        		  		}
			        	  }
			        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parentplant+"' AND CHILD_PLANT !='"+(String) map.get("PLANT")+"' ";
			        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
			        	  Map m = new HashMap();
			        	  if (arrList.size() > 0) {
			        		  for (int k=0; k < arrList.size(); k++ ) {
			        			  Map mt = (Map) arrList.get(k);
			        			  String childplant = (String) mt.get("CHILD_PLANT");
			        			  deleteItemSupplier=itemdao.removeItemsupplier(childplant,item_id);
			        			  if(childplant!=(String) map.get("PLANT")) {
				        					for(int l =0 ; l < ADDSUP.size() ; l++){
				        			       		  String ADDIONALITEM = (String) ADDSUP.get(l);
				        						  Hashtable HM = new Hashtable();
				        						  HM.put(IConstants.PLANT, childplant);
				        						  HM.put(IConstants.ITEM, item_id);
				        						  HM.put(IConstants.VENDNO, ADDIONALITEM);
				        						  HM.put("CRAT",new DateUtils().getDateTime());
				        						  HM.put("CRBY",map.get("LOGIN_USER"));
//				        						  flag = itemUtil.insertAdditionalPrd(HM);
				        						  flag = itemUtil.insertItemSupplier(HM);
				        						 }
			        			  	}
			        		  	}
			        	  	}
		        	  	}
           			}
           	  }//IMTHI END	
		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}
	
}
