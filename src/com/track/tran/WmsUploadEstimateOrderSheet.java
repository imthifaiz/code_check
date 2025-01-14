package com.track.tran;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.TblControlUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.EstHdrDAO;
import com.track.dao.EstDetDAO;

public class WmsUploadEstimateOrderSheet implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	EstHdrDAO EstHdrDAO = null;
    EstDetDAO EstDetDAO = null;
    ESTUtil estutil = null;
    MovHisDAO movHisDao = null;
    

	public WmsUploadEstimateOrderSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		    EstHdrDAO = new EstHdrDAO();
	        estutil = new ESTUtil();
	        EstDetDAO = new EstDetDAO();
	        movHisDao = new MovHisDAO();
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
		MLogger.log(0, "2.insertitemsheet - Stage : 1");
		flag = processCountSheet(m);
		/*MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if (flag == true)
			flag = processMovHis(m);*/

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			
			
			
			
			// check ESTNO that already exist on ESTHDR table
			boolean isExistEstNo = false;
			
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, map.get(IConstants.PLANT));
			ht.put(IConstants.EST_ESTNO, map.get(IConstants.EST_ESTNO));
			
			isExistEstNo = EstHdrDAO.isExisit(ht);
		
			if(isExistEstNo){
				 boolean isconfirmEstNo = false;
				    isconfirmEstNo = estutil.isOpenEstimateOrder((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EST_ESTNO));
				    	if(!isconfirmEstNo){
			            //Update ESTHDR 
			            flag=this.processUpdatesthdr(map);
				    	}
				    	else
				    	{
				    		  throw new Exception(" Processed Estimate Order Cannot be Modified ");
				        }
			    }else{
			      
			        flag=this.processESTHDR(map);
			        if(flag){
                                    flag=this.processMovHisForESTHDR(map);
                               	    
                                    new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo((String)map.get(IConstants.PLANT),IConstants.ESTIMATE,"SE",(String)map.get(IConstants.EST_ESTNO));
                                   
                                  }
			    }
			
			      String unitPrice= new ESTUtil().getConvertedUnitCostToLocalCurrency((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EST_ESTNO), (String)map.get(IConstants.EST_UNITCOST)) ;
                  map.put(IConstants.EST_UNITCOST, unitPrice);
			    if(flag){
			        flag=this.processDeleteestdet(map);
			             
			    }       
			    if(flag){
			        flag=this.processESTDET(map);
			    }
			    if(flag){
			          flag=this.processMovHisForESTDET(map);
			      } 
				
			      
			    

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw new Exception("Processed Estimate Order Cannot be Modified");
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	@SuppressWarnings("unchecked")
    public boolean processMovHisForESTDET(Map map) throws Exception {
            boolean flag = false;
            MovHisDAO movHisDao = new MovHisDAO();
            try {
                movHisDao.setmLogger(mLogger);
                Hashtable htmovhisdet = new Hashtable();
                // data for MOVHIS table for DODET
                htmovhisdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                htmovhisdet.put(IConstants.DIRTYPE, TransactionConstants.EST_ADD_ITEM);
                htmovhisdet.put(IConstants.LNNO,  (String)map.get(IConstants.EST_ESTLNNO));
                htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.EST_ESTNO));
                htmovhisdet.put(IConstants.EST_ITEM, (String)map.get(IConstants.EST_ITEM));
                htmovhisdet.put(IConstants.QTY, (String)map.get(IConstants.EST_QTYOR));
                htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.EST_UNITCOST));
                htmovhisdet.put("CRBY", (String)map.get("LOGIN_USER"));
                htmovhisdet.put("TRANDATE", dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
                htmovhisdet.put("CRAT", DateUtils.getDateTime());
                htmovhisdet.put("MOVTID", "");
                htmovhisdet.put("RECID", "");
                
                flag = movHisDao.insertIntoMovHis(htmovhisdet);

            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
	
	

    public boolean processMovHisForESTHDR(Map map) throws Exception {
        boolean flag = false;
      
        try {
            movHisDao.setmLogger(mLogger);
            Hashtable htmovhishdr = new Hashtable();
                
            // data for MOVHIS table for DOHDR
            htmovhishdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htmovhishdr.put(IConstants.DIRTYPE, TransactionConstants.CREATE_EST);
            htmovhishdr.put(IConstants.LNNO, "");
            htmovhishdr.put(IConstants.ORDNUM, (String)map.get(IConstants.EST_ESTNO));
            htmovhishdr.put(IDBConstants.POHDR_JOB_NUM, (String)map.get(IConstants.EST_REF_NO));
            htmovhishdr.put("CRBY", (String)map.get("LOGIN_USER"));
            htmovhishdr.put("TRANDATE", dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
            htmovhishdr.put("CRAT", DateUtils.getDateTime());
            htmovhishdr.put("MOVTID", "");
            htmovhishdr.put("RECID", "");
            flag = movHisDao.insertIntoMovHis(htmovhishdr);
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }


    public boolean processESTDET(Map map) throws Exception {
        boolean flag = false;
       
        try {
        	
            Hashtable htestdet =  new Hashtable();
            htestdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htestdet.put(IConstants.EST_ESTNO, (String)map.get(IConstants.EST_ESTNO));
            htestdet.put(IConstants.EST_ESTLNNO,(String)map.get(IConstants.EST_ESTLNNO));
            htestdet.put(IConstants.EST_ITEM, (String)map.get(IConstants.EST_ITEM));
            htestdet.put(IConstants.OUT_ITEM_DES, (String)map.get(IConstants.OUT_ITEM_DES));
            htestdet.put(IConstants.OUT_STATUS, "N");
            htestdet.put(IConstants.OUT_TRANDATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
            htestdet.put(IConstants.EST_UNITCOST, (String)map.get(IConstants.EST_UNITCOST));
            htestdet.put(IConstants.EST_QTYOR, (String)map.get(IConstants.EST_QTYOR));
            htestdet.put("QTYIS", "0");
            htestdet.put(IConstants.EST_UNITMO, (String)map.get(IConstants.EST_UNITMO));
            String CURRENCYUSEQT = new ESTUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.EST_ESTNO));
           // htestdet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
            htestdet.put("PRODUCTDELIVERYDATE", (String)map.get("PRODUCTDELIVERYDATE"));
            htestdet.put("ACCOUNT_NAME", (String)map.get("ACCOUNT"));
            htestdet.put("TAX_TYPE", (String)map.get("TAX"));
            htestdet.put("DISCOUNT", (String)map.get("PRODUCTDISCOUNT"));
            htestdet.put("DISCOUNT_TYPE", (String)map.get("PRODUCTDISCOUNT_TYPE"));
            htestdet.put("CRAT", dateUtils.getDateTime());
            htestdet.put("CRBY", (String)map.get("LOGIN_USER"));
            htestdet.put(IDBConstants.CURRENCYUSEQT, (String)map.get("EQUIVALENTCURRENCY"));
            Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.EST_ITEM));
            String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
            String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));

            if(nonstocktype.equals("Y"))	
		    {
		    	if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
		    		 htestdet.put(IConstants.EST_UNITCOST, "-"+(String)map.get(IConstants.EST_UNITCOST));
                   }
		    	
		    	
		    }
            // data insert into DODET table
        
                flag = estutil.saveestDetDetails(htestdet);
                
            

        } catch (Exception e) {
                throw e;
        }
        return flag;
    }
    
    public boolean processESTHDR(Map map) throws Exception {
        boolean flag = false;
        MasterDAO _MasterDAO = new  MasterDAO();
        MasterUtil _MasterUtil = new MasterUtil();
        Hashtable htMaster=new Hashtable();
        Hashtable htRecvHis=new Hashtable();
       
        try {
              Hashtable htesthdr =  new Hashtable();
              htesthdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
              htesthdr.put(IConstants.EST_ESTNO, (String)map.get(IConstants.EST_ESTNO));
              htesthdr.put(IConstants.OUT_STATUS, "N");
              htesthdr.put(IConstants.EST_REF_NO, (String)map.get(IConstants.EST_REF_NO));
			  htesthdr.put(IConstants.EST_ORDERTYPE, (String)map.get(IConstants.EST_ORDERTYPE));
              htesthdr.put(IConstants.EST_COLLECTION_DATE, (String)map.get(IConstants.EST_COLLECTION_DATE));
              htesthdr.put(IConstants.EST_COLLECTION_TIME, (String)map.get(IConstants.EST_COLLECTION_TIME));
              
              htesthdr.put(IConstants.LOANHDR_ADDRESS, (String)map.get(IConstants.LOANHDR_ADDRESS));
              htesthdr.put(IConstants.LOANHDR_ADDRESS2, (String)map.get(IConstants.LOANHDR_ADDRESS2));
              htesthdr.put(IConstants.LOANHDR_ADDRESS3, (String)map.get(IConstants.LOANHDR_ADDRESS3));
              htesthdr.put(IConstants.LOANHDR_CONTACT_NUM, (String)map.get(IConstants.LOANHDR_CONTACT_NUM));
              
              htesthdr.put(IConstants.EST_REMARK1, (String)map.get(IConstants.EST_REMARK1));
			  htesthdr.put(IDBConstants.DELIVERYDATE, (String)map.get(IDBConstants.DELIVERYDATE));
              if (!map.get(IConstants.EST_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.EST_REMARK1).toString()!= null){
  				htMaster.clear();
  				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.EST_REMARK1));
  			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
  				if(!map.get(IConstants.EST_REMARK1).toString().equalsIgnoreCase("")){
  					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
  					_MasterDAO.InsertRemarks(htMaster);
  					htRecvHis.clear();
  					htRecvHis.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  					htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
  					htRecvHis.put("ORDNUM", "");
  					htRecvHis.put(IDBConstants.ITEM, "");
  					htRecvHis.put("BATNO", "");
  					htRecvHis.put(IDBConstants.LOC, "");
  					htRecvHis.put("REMARKS", (String)map.get(IConstants.EST_REMARK1));
  					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
  					htRecvHis.put(IDBConstants.TRAN_DATE,
  					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
  					flag = movHisDao.insertIntoMovHis(htRecvHis);
  				}
  			 }
  			}
              htesthdr.put(IConstants.OUT_REMARK3, (String)map.get(IConstants.OUT_REMARK3));
              if (!map.get(IConstants.OUT_REMARK3).toString().equalsIgnoreCase("null") && map.get(IConstants.OUT_REMARK3).toString()!= null){
    				htMaster.clear();
    				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
    				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.OUT_REMARK3));
    			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
    				if(!map.get(IConstants.OUT_REMARK3).toString().equalsIgnoreCase("")){
    					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
    					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
    					_MasterDAO.InsertRemarks(htMaster);
    					htRecvHis.clear();
    					htRecvHis.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
    					htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
    					htRecvHis.put("ORDNUM", "");
    					htRecvHis.put(IDBConstants.ITEM, "");
    					htRecvHis.put("BATNO", "");
    					htRecvHis.put(IDBConstants.LOC, "");
    					htRecvHis.put("REMARKS", (String)map.get(IConstants.OUT_REMARK3));
    					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
    					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
    					htRecvHis.put(IDBConstants.TRAN_DATE,
    					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
    					flag = movHisDao.insertIntoMovHis(htRecvHis);
    				}
    		    	}
              }
              htesthdr.put(IConstants.OUT_OUTBOUND_GST, (String)map.get(IConstants.OUT_OUTBOUND_GST));
              htesthdr.put(IConstants.EST_EMPNO,(String)map.get(IConstants.EST_EMPNO));
              htesthdr.put(IConstants.EST_EXPIRE_DATE,(String)map.get(IConstants.EST_EXPIRE_DATE));
              htesthdr.put(IConstants.EST_CUST_CODE, (String)map.get(IConstants.EST_CUST_CODE));
              htesthdr.put(IConstants.OUT_CUST_NAME, (String)map.get(IConstants.OUT_CUST_NAME));
              htesthdr.put(IConstants.OUT_PERSON_IN_CHARGE, (String)map.get(IConstants.OUT_PERSON_IN_CHARGE));
              htesthdr.put(IConstants.EST_CURRENCYID,(String)map.get(IConstants.EST_CURRENCYID));
              htesthdr.put(IConstants.ORDERDISCOUNT, (String)map.get(IConstants.ORDERDISCOUNT));
              htesthdr.put(IConstants.SHIPPINGCOST, (String)map.get(IConstants.SHIPPINGCOST));
			  htesthdr.put(IConstants.PAYMENTTYPE, (String)map.get(IConstants.PAYMENTTYPE));
			  htesthdr.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT));
			  htesthdr.put(IConstants.TAXTREATMENT, (String)map.get(IConstants.TAXTREATMENT));
              htesthdr.put(IConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
              if (!map.get(IConstants.INCOTERMS).toString().equalsIgnoreCase("null") && map.get(IConstants.INCOTERMS).toString()!= null){
  				htMaster.clear();
  				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  				htMaster.put(IDBConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
  				if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
  					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
  					_MasterDAO.InsertINCOTERMS(htMaster);
  					htRecvHis.clear();
  					htRecvHis.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
  					htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
  					htRecvHis.put("ORDNUM", "");
  					htRecvHis.put(IDBConstants.ITEM, "");
  					htRecvHis.put("BATNO", "");
  					htRecvHis.put(IDBConstants.LOC, "");
  					htRecvHis.put("REMARKS", (String)map.get(IConstants.INCOTERMS));
  					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  					htRecvHis.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
  					htRecvHis.put(IDBConstants.TRAN_DATE,
  				    DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
  					flag = movHisDao.insertIntoMovHis(htRecvHis);
  				}
  			}
              htesthdr.put(IConstants.SHIPPINGCUSTOMER, (String)map.get(IConstants.SHIPPINGCUSTOMER));
  			 boolean isExistShippingCustomer=false;
  			  if (map.get(IConstants.SHIPPINGCUSTOMER).toString().length() > 0  && !map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase(null)) {
  				 isExistShippingCustomer = _MasterDAO.isExistsShippingDetails((String)map.get(IConstants.SHIPPINGCUSTOMER),(String)map.get(IConstants.PLANT));
    			}
  			 
  			  if (!map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase("null") && map.get(IConstants.SHIPPINGCUSTOMER).toString()!= null){
  			  if(isExistShippingCustomer==false ){
  				Hashtable ht = new Hashtable();
  				ht.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
  				ht.put(IDBConstants.CUSTOMERNAME,(String)map.get(IConstants.SHIPPINGCUSTOMER));
  				ht.put(IDBConstants.CONTACTNAME, "");
  				ht.put("TELEPHONE","");
  				ht.put("HANDPHONE", "");
  				ht.put(IDBConstants.EMAIL,"");
  				
  				ht.put(IDBConstants.UNITNO, "");
  				ht.put(IDBConstants.BUILDING, "");
  				ht.put(IDBConstants.STREET, "");
  				ht.put(IDBConstants.CITY,"");
  				ht.put(IDBConstants.STATE,"");
  				ht.put(IDBConstants.COUNTRY, "");
  				ht.put(IDBConstants.POSTALCODE, "");
  		    	ht.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  				ht.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
  				boolean insertflag =_MasterDAO.InsertShippingDetails(ht);
  				htRecvHis.clear();
  				htRecvHis.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
  				htRecvHis.put("DIRTYPE","ADD_SHIPPING_MST");
  				htRecvHis.put("ORDNUM", "");
  				htRecvHis.put(IDBConstants.ITEM, "");
  				htRecvHis.put("BATNO", "");
  				htRecvHis.put(IDBConstants.LOC, "");
  				htRecvHis.put("REMARKS", (String)map.get(IConstants.SHIPPINGCUSTOMER));
  				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
  				htRecvHis.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
  				htRecvHis.put(IDBConstants.TRAN_DATE,
  				DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
  			
  				flag = movHisDao.insertIntoMovHis(htRecvHis);
  			  }
  			  }
  			htesthdr.put(IConstants.SHIP_CONTACTNAME, (String)map.get(IConstants.SHIP_CONTACTNAME));
            htesthdr.put(IConstants.SHIP_DESGINATION, (String)map.get(IConstants.SHIP_DESGINATION));
            htesthdr.put(IConstants.SHIP_WORKPHONE, (String)map.get(IConstants.SHIP_WORKPHONE));
            htesthdr.put(IConstants.SHIP_HPNO, (String)map.get(IConstants.SHIP_HPNO));
            htesthdr.put(IConstants.SHIP_EMAIL, (String)map.get(IConstants.SHIP_EMAIL));
            htesthdr.put(IConstants.SHIP_COUNTRY, (String)map.get(IConstants.SHIP_COUNTRY));
            htesthdr.put(IConstants.SHIP_ADDR1, (String)map.get(IConstants.SHIP_ADDR1));
            htesthdr.put(IConstants.SHIP_ADDR2, (String)map.get(IConstants.SHIP_ADDR2));
            htesthdr.put(IConstants.SHIP_ADDR3, (String)map.get(IConstants.SHIP_ADDR3));
            htesthdr.put(IConstants.SHIP_ADDR4, (String)map.get(IConstants.SHIP_ADDR4));
            htesthdr.put(IConstants.SHIP_STATE, (String)map.get(IConstants.SHIP_STATE));
            htesthdr.put(IConstants.SHIP_ZIP, (String)map.get(IConstants.SHIP_ZIP));
            
  			htesthdr.put(IConstants.SALES_LOCATION, (String)map.get(IConstants.SALES_LOCATION));
  			htesthdr.put(IConstants.ORDERDATE, (String)map.get(IConstants.EST_COLLECTION_DATE));
  			htesthdr.put("SHIPPINGID", (String)map.get("SHIPPINGID"));
  			htesthdr.put("ORDER_STATUS", "Open");
  			htesthdr.put("DISCOUNT", (String)map.get("DISCOUNT"));
  			htesthdr.put("DISCOUNT_TYPE", (String)map.get("DISCOUNTTYPE"));
  			htesthdr.put("ADJUSTMENT", (String)map.get("ADJUSTMENT"));
  			htesthdr.put("ITEM_RATES", (String)map.get("ISTAXINCLUSIVE"));
  			htesthdr.put("PROJECTID", (String)map.get("PROJECTID"));
  			htesthdr.put("CURRENCYUSEQT", (String)map.get("EQUIVALENTCURRENCY"));
  			htesthdr.put("ORDERDISCOUNTTYPE", (String)map.get("ORDERDISCOUNTTYPE"));
  			htesthdr.put("TAXID", (String)map.get("TAXID"));
  			htesthdr.put("ISDISCOUNTTAX", (String)map.get("ISDISCOUNTTAX"));
  			htesthdr.put("ISORDERDISCOUNTTAX", (String)map.get("ISORDERDISCOUNTTAX"));
  			htesthdr.put("ISSHIPPINGTAX", (String)map.get("ISSHIPPINGTAX"));
  			htesthdr.put("CRAT", dateUtils.getDateTime());
  			htesthdr.put("CRBY", (String)map.get("LOGIN_USER"));
            // data insert into DOHDR table
            flag = estutil.saveestHdrDetails(htesthdr);
            
            
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }

    public boolean processUpdatesthdr(Map map) throws Exception {
        boolean flag = false;
        MasterDAO _MasterDAO = new  MasterDAO();
        MasterUtil _MasterUtil = new MasterUtil();
        Hashtable htMaster=new Hashtable();
        Hashtable htRecvHis=new Hashtable();
       

        try {
             
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.EST_ESTNO, (String)map.get(IConstants.EST_ESTNO));
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.EST_REF_NO + " = '" + (String)map.get(IConstants.EST_REF_NO) + "'");
           
            updateQyery.append("," + IConstants.EST_COLLECTION_DATE + " = '"+ (String)map.get(IConstants.EST_COLLECTION_DATE) + "'");
            updateQyery.append("," + IConstants.EST_COLLECTION_TIME + " = '"+ (String)map.get(IConstants.EST_COLLECTION_TIME) + "'");
            updateQyery.append("," + IConstants.EST_REMARK1 + " = '"+ (String)map.get(IConstants.EST_REMARK1) + "'");
            updateQyery.append("," + IConstants.OUT_REMARK2 + " = '"+ (String)map.get(IConstants.OUT_REMARK2) + "'");
            updateQyery.append("," + IConstants.OUT_OUTBOUND_GST + " = '"+ (String)map.get(IConstants.OUT_OUTBOUND_GST) + "'");
            updateQyery.append("," + IConstants.EST_EMPNO + " = '"+ (String)map.get(IConstants.EST_EMPNO) + "'");
            updateQyery.append("," + IConstants.EST_EXPIRE_DATE + " = '" +  (String)map.get(IConstants.EST_EXPIRE_DATE) + "'");
            updateQyery.append(","+ IConstants.EST_CUST_CODE + " = '" + (String)map.get(IConstants.EST_CUST_CODE) + "'");
            updateQyery.append("," +IConstants.OUT_CUST_NAME + " = '" + (String)map.get(IConstants.OUT_CUST_NAME) + "'");
            updateQyery.append("," + IConstants.OUT_PERSON_IN_CHARGE + " = '"+ (String)map.get(IConstants.OUT_PERSON_IN_CHARGE) + "'");
            updateQyery.append("," + IConstants.EST_CURRENCYID + " = '"+ (String)map.get(IConstants.EST_CURRENCYID) + "'");
            updateQyery.append("," +IConstants.ORDERDISCOUNT + " = '" + (String)map.get(IConstants.ORDERDISCOUNT) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCOST + " = '"+ (String)map.get(IConstants.SHIPPINGCOST) + "'");
            updateQyery.append("," + IConstants.INCOTERMS + " = '"+ (String)map.get(IConstants.INCOTERMS) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCUSTOMER + " = '"+ (String)map.get(IConstants.SHIPPINGCUSTOMER) + "'");
            updateQyery.append("," + IDBConstants.DELIVERYDATE + " = '"+ (String)map.get(IDBConstants.DELIVERYDATE) + "'");  
			updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"+ (String)map.get(IDBConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IDBConstants.POHDR_DELIVERYDATEFORMAT + " = '"+ (String)map.get(IDBConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IConstants.SALES_LOCATION + " = '"+ (String)map.get(IConstants.SALES_LOCATION) + "'");
			updateQyery.append(",SHIPPINGID = '"+ (String)map.get("SHIPPINGID")+ "'");
			updateQyery.append(",DISCOUNT = '"+ (String)map.get("DISCOUNT")+ "'");
			updateQyery.append(",DISCOUNT_TYPE = '"+ (String)map.get("DISCOUNTTYPE")+ "'");
			updateQyery.append(",ADJUSTMENT = '"+ (String)map.get("ADJUSTMENT")+ "'");
			updateQyery.append(",PROJECTID = '"+ (String)map.get("PROJECTID")+ "'");
			updateQyery.append(",TAXID = '"+ (String)map.get("TAXID")+ "'");
			updateQyery.append(",ISDISCOUNTTAX = '"+ (String)map.get("ISDISCOUNTTAX")+ "'");
			updateQyery.append(",ISORDERDISCOUNTTAX = '"+ (String)map.get("ISORDERDISCOUNTTAX")+ "'");
			updateQyery.append(",ISSHIPPINGTAX = '"+ (String)map.get("ISSHIPPINGTAX")+ "'");
			updateQyery.append(",CURRENCYUSEQT = '"+ (String)map.get("EQUIVALENTCURRENCY")+ "'");
			updateQyery.append(",ORDERDISCOUNTTYPE = '"+ (String)map.get("ORDERDISCOUNTTYPE")+ "'");
			updateQyery.append(",ITEM_RATES = '"+ (String)map.get("ISTAXINCLUSIVE")+ "'");
			updateQyery.append(",ORDDATE = '"+ (String)map.get(IConstants.EST_COLLECTION_DATE)+ "'");
			updateQyery.append(",UPAT = '"+ dateUtils.getDateTime()+ "'");
			updateQyery.append(",UPBY = '"+ (String)map.get("LOGIN_USER")+ "'");
			
            flag = EstHdrDAO.update(updateQyery.toString(), htCond, "  ");
            
            if(flag) //insert remarks,incoterm and shipping customer into master
            {            	
            	 if (!map.get(IConstants.EST_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.EST_REMARK1).toString()!= null){
       				htMaster.clear();
       				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
       				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.EST_REMARK1));
       			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
       				if(!map.get(IConstants.EST_REMARK1).toString().equalsIgnoreCase("")){
       					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
       					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
       					_MasterDAO.InsertRemarks(htMaster);
       					htRecvHis.clear();
       					htRecvHis.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
       					htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
       					htRecvHis.put("ORDNUM", "");
       					htRecvHis.put(IDBConstants.ITEM, "");
       					htRecvHis.put("BATNO", "");
       					htRecvHis.put(IDBConstants.LOC, "");
       					htRecvHis.put("REMARKS", (String)map.get(IConstants.EST_REMARK1));
       					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
       					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
       					htRecvHis.put(IDBConstants.TRAN_DATE,
       					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
       					flag = movHisDao.insertIntoMovHis(htRecvHis);
       				}
       			 }
       			}
                   if (!map.get(IConstants.OUT_REMARK3).toString().equalsIgnoreCase("null") && map.get(IConstants.OUT_REMARK3).toString()!= null){
         				htMaster.clear();
         				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
         				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.OUT_REMARK3));
         			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
         				if(!map.get(IConstants.OUT_REMARK3).toString().equalsIgnoreCase("")){
         					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
         					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
         					_MasterDAO.InsertRemarks(htMaster);
         					htRecvHis.clear();
         					htRecvHis.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
         					htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
         					htRecvHis.put("ORDNUM", "");
         					htRecvHis.put(IDBConstants.ITEM, "");
         					htRecvHis.put("BATNO", "");
         					htRecvHis.put(IDBConstants.LOC, "");
         					htRecvHis.put("REMARKS", (String)map.get(IConstants.OUT_REMARK3));
         					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
         					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
         					htRecvHis.put(IDBConstants.TRAN_DATE,
         					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
         					flag = movHisDao.insertIntoMovHis(htRecvHis);
         				}
         		    	}
                   }
                   
                   
            	if (!map.get(IConstants.INCOTERMS).toString().equalsIgnoreCase("null") && map.get(IConstants.INCOTERMS).toString()!= null){
      				htMaster.clear();
      				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
      				htMaster.put(IDBConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
      				if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
      					htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
      					htMaster.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
      					_MasterDAO.InsertINCOTERMS(htMaster);
      					htRecvHis.clear();
      					htRecvHis.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
      					htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
      					htRecvHis.put("ORDNUM", "");
      					htRecvHis.put(IDBConstants.ITEM, "");
      					htRecvHis.put("BATNO", "");
      					htRecvHis.put(IDBConstants.LOC, "");
      					htRecvHis.put("REMARKS", (String)map.get(IConstants.INCOTERMS));
      					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
      					htRecvHis.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
      					htRecvHis.put(IDBConstants.TRAN_DATE,
      				    DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
      					flag = movHisDao.insertIntoMovHis(htRecvHis);
      				}
      			}
                boolean isExistShippingCustomer=false;
      			  if (map.get(IConstants.SHIPPINGCUSTOMER).toString().length() > 0  && !map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase(null)) {
      				 isExistShippingCustomer = _MasterDAO.isExistsShippingDetails((String)map.get(IConstants.SHIPPINGCUSTOMER),(String)map.get(IConstants.PLANT));
        			}
      			 
      			  if (!map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase("null") && map.get(IConstants.SHIPPINGCUSTOMER).toString()!= null){
      			  if(isExistShippingCustomer==false ){
      				Hashtable ht = new Hashtable();
      				ht.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
      				ht.put(IDBConstants.CUSTOMERNAME,(String)map.get(IConstants.SHIPPINGCUSTOMER));
      				ht.put(IDBConstants.CONTACTNAME, "");
      				ht.put("TELEPHONE","");
      				ht.put("HANDPHONE", "");
      				ht.put(IDBConstants.EMAIL,"");
      				
      				ht.put(IDBConstants.UNITNO, "");
      				ht.put(IDBConstants.BUILDING, "");
      				ht.put(IDBConstants.STREET, "");
      				ht.put(IDBConstants.CITY,"");
      				ht.put(IDBConstants.STATE,"");
      				ht.put(IDBConstants.COUNTRY, "");
      				ht.put(IDBConstants.POSTALCODE, "");
      		    	ht.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
      				ht.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
      				boolean insertflag =_MasterDAO.InsertShippingDetails(ht);
      				htRecvHis.clear();
      				htRecvHis.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
      				htRecvHis.put("DIRTYPE","ADD_SHIPPING_MST");
      				htRecvHis.put("ORDNUM", "");
      				htRecvHis.put(IDBConstants.ITEM, "");
      				htRecvHis.put("BATNO", "");
      				htRecvHis.put(IDBConstants.LOC, "");
      				htRecvHis.put("REMARKS", (String)map.get(IConstants.SHIPPINGCUSTOMER));
      				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
      				htRecvHis.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
      				htRecvHis.put(IDBConstants.TRAN_DATE,
      				DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
      			
      				flag = movHisDao.insertIntoMovHis(htRecvHis);
      			  }
      			  }
            }
          
           
            if(!flag){
                throw new Exception("Estimate Order "+(String)map.get(IConstants.EST_ESTNO)+" is processed,Cannot Modify");
            }
            
         
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }
    
    public boolean processDeleteestdet(Map map) throws Exception {
        boolean flag = false;
       
        try {
            Hashtable htestdet = new Hashtable();
            htestdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htestdet.put(IConstants.EST_ESTNO, (String)map.get(IConstants.EST_ESTNO));
            htestdet.put(IConstants.EST_ESTLNNO, (String)map.get(IConstants.EST_ESTLNNO));
            
            boolean isLineExist = EstDetDAO.isExisit(htestdet,"");
            if(isLineExist){
               
               flag  = EstDetDAO.delete(htestdet);
               
            }else{
                flag=true; 
            }
           
        } catch (Exception e) {
                throw e;
          }
        return flag;
    }

   
}
