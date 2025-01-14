package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ShipHisDAO;
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

public class WmsUploadInboundOrderSheet implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	PrdClassUtil prdclsutil = null;
	PrdTypeUtil prdtypeutil = null;
	ItemSesBeanDAO itemsesdao = null;
    POUtil poUtil =null;
   MovHisDAO movHisDao = null;

	public WmsUploadInboundOrderSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		prdclsutil = new PrdClassUtil();
		prdtypeutil = new PrdTypeUtil();
	    poUtil = new POUtil();
	    movHisDao = new MovHisDAO();
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,String userCode) {
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
		

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			prdclsutil.setmLogger(mLogger);
			prdtypeutil.setmLogger(mLogger);
			
			// check PONO that already exist on POHDR table
			boolean isExistPoNo = false;
			
			isExistPoNo = poUtil.isExistPONO((String)map.get(IConstants.IN_PONO), (String)map.get(IConstants.PLANT));
			System.out.println("isExistPoNo:"+isExistPoNo);
			if(isExistPoNo){
                        	//Update POHDR 
                        	 System.out.println("Update");
				flag=this.processUpdatePohdr(map);
			}else{
                            System.out.println("Insert statement");
			    flag=this.processPOHDR(map);
			    if(flag){
                    flag=this.processMovHisForPOHDR(map);
                
                            new TblControlUtil().updateTblControlSeqNo((String)map.get(IConstants.PLANT),IConstants.INBOUND,"P",(String)map.get(IConstants.IN_PONO));
                           // new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pono);
                        }
				}   
                        
                        
                        boolean isNewStatusPoNo = false;
    					isNewStatusPoNo = poUtil.isNewStatusPONO((String)map.get(IConstants.IN_PONO), (String)map.get(IConstants.PLANT));
    					//By Samatha to Capture the Cost in Local currency on 17/09/2013
    	                 String unitCost = new POUtil().getConvertedUnitCostToLocalCurrency((String)map.get(IConstants.PLANT),(String) map.get(IConstants.IN_PONO), (String) map.get(IConstants.IN_UNITCOST)) ;
    	                 map.put(IConstants.IN_UNITCOST, unitCost);
    	                 //End Samatha
    	                 
    	               ///Get Non Stock Type
    					 Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String) map.get(IConstants.IN_ITEM));
    					 String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
    					 String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
    					
    					 if(nonstocktype.equals("Y"))	
    					    {
    						 	//map.put(IDBConstants.PODET_LNSTATUS, "C");
    						 	//map.put(IDBConstants.PODET_QTYRC, (String) map.get(IConstants.IN_QTYOR));
    					        if(nonstocktypeDesc.equalsIgnoreCase("discount")){ 
    					        	map.put(IConstants.IN_UNITCOST, "-"+unitCost);
    							 }
    					    }
    					  
    					  map.put(IDBConstants.PODET_LNSTATUS, "N");	
    					
    					 
    					if(isNewStatusPoNo)
    					{
    						flag=this.processUpdatePodet(map);
    						if(flag){
                                flag=this.processUpdateRECVDET(map);
    						}
    						
    					}
    					else{
                        if(flag){
                            flag=this.processDeletePodet(map);
                                 
                        }	
                        if(flag){
                            flag=this.processPODET(map);
                        }
                        /*if(flag){
                        	if(nonstocktype.equals("Y")){	
                        	flag = this.processRECVDET(map);}
                        }*/
    					}
                          if(flag){
                              flag=this.processMovHisForPODET(map);
                          } 
			
	

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processMovHisForPODET(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		try {
                    movHisDao.setmLogger(mLogger);
                    Hashtable htmovhisdet = new Hashtable();
                        
		    htmovhisdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
		    htmovhisdet.put(IConstants.DIRTYPE, TransactionConstants.IB_ADD_ITEM);
		    htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.IN_PONO));
		    htmovhisdet.put(IConstants.LNNO, (String)map.get(IConstants.IN_POLNNO));
		    htmovhisdet.put(IConstants.IN_ITEM, (String)map.get(IConstants.IN_ITEM));
		    htmovhisdet.put(IConstants.QTY, (String)map.get(IConstants.IN_QTYOR));
		    htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.IN_UNITCOST));
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

    public boolean processMovHisForPOHDR(Map map) throws Exception {
            boolean flag = false;
          
            try {
                movHisDao.setmLogger(mLogger);
                Hashtable htmovhishdr = new Hashtable();
                    
                htmovhishdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                htmovhishdr.put(IConstants.DIRTYPE, TransactionConstants.CREATE_IB);
                htmovhishdr.put(IConstants.LNNO, "");
                htmovhishdr.put(IConstants.ORDNUM, (String)map.get(IConstants.IN_PONO));
                htmovhishdr.put(IConstants.IN_ITEM, (String)map.get(IConstants.IN_ITEM));
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


    public boolean processPODET(Map map) throws Exception {
        boolean flag = false;
       
        try {
              Hashtable htpodet =  new Hashtable();
              htpodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
              htpodet.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
              htpodet.put(IConstants.IN_POLNNO, (String)map.get(IConstants.IN_POLNNO));
              htpodet.put(IConstants.IN_LNSTAT, (String)map.get(IDBConstants.PODET_LNSTATUS));
              htpodet.put(IConstants.IN_ITEM, (String)map.get(IConstants.IN_ITEM));
              htpodet.put(IConstants.IN_ITEM_DES, (String)map.get(IConstants.IN_ITEM_DES));
              htpodet.put(IConstants.IN_TRANDATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
              System.out.println("(String)map.get(IConstants.IN_UNITCOST"+(String)map.get(IConstants.IN_UNITCOST));
             // htpodet.put(IConstants.IN_UNITCOST, (String)map.get(IConstants.IN_UNITCOST));
              htpodet.put(IConstants.IN_UNITCOST, (String)map.get(IConstants.IN_UNITCOST));
              htpodet.put(IConstants.IN_QTYOR, (String)map.get(IConstants.IN_QTYOR));
              htpodet.put(IConstants.IN_UNITMO, (String)map.get(IConstants.IN_UNITMO));
              htpodet.put(IConstants.IN_ITEM_DET_DES, (String)map.get(IConstants.IN_ITEM_DET_DES));
              htpodet.put(IConstants.IN_SUPPLIER, (String)map.get(IConstants.IN_CUST_NAME));
              htpodet.put(IConstants.IN_MANUFACTURER, (String)map.get(IConstants.IN_MANUFACTURER));
              htpodet.put(IConstants.IN_ITEM_DET_DES, (String)map.get(IConstants.IN_ITEM_DES));
              htpodet.put(IConstants.IN_USERFLD2, (String)map.get(IConstants.IN_REF_NO));
              String CURRENCYUSEQT = new POUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.IN_PONO));
              htpodet.put(IDBConstants.CURRENCYUSEQT, (String)map.get("CURRENCYUSEQT"));
              htpodet.put("DISCOUNT", (String)map.get("PRODUCTDISCOUNT"));
              htpodet.put("DISCOUNT_TYPE", (String)map.get("PRODUCTDISCOUNT_TYPE"));
              htpodet.put("ACCOUNT_NAME", (String)map.get("ACCOUNT_NAME"));
              htpodet.put("PRODUCTDELIVERYDATE", (String)map.get("PRODUCTDELIVERYDATE"));
              htpodet.put("TAX_TYPE", (String)map.get("Tax"));
              htpodet.put("QTYRC", "0.0");
              htpodet.put("PRODGST", "0");
              htpodet.put("CRBY", (String)map.get("LOGIN_USER"));
              htpodet.put("CRAT", dateUtils.getDate());
              htpodet.put("POESTNO", "");
              htpodet.put("POESTLNNO", "");
          
              flag = poUtil.savePoDetDetails(htpodet);
          
           	  PoDetDAO _PoDetDAO=new PoDetDAO();
              Hashtable htPoRemarksDel = new Hashtable();
              htPoRemarksDel.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
              htPoRemarksDel.put(IDBConstants.PODET_PONUM, (String)map.get(IConstants.IN_PONO));
              htPoRemarksDel.put(IDBConstants.PODET_POLNNO, (String)map.get(IConstants.IN_POLNNO));
			      if(_PoDetDAO.isExisitPoMultiRemarks(htPoRemarksDel))
              {
                 flag = _PoDetDAO.deletePoMultiRemarks(htPoRemarksDel);
                                   
              }
			      
			        Hashtable htPoRemarks = new Hashtable();
				htPoRemarks.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htPoRemarks.put(IDBConstants.PODET_PONUM, (String)map.get(IConstants.IN_PONO));
				htPoRemarks.put(IDBConstants.PODET_POLNNO,(String)map.get(IConstants.IN_POLNNO));
				htPoRemarks.put(IDBConstants.PODET_ITEM, (String)map.get(IConstants.IN_ITEM));
				htPoRemarks.put(IDBConstants.REMARKS,"");
				htPoRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPoRemarks.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
				flag =  poUtil.savePoMultiRemarks(htPoRemarks);
        } catch (Exception e) {
                throw e;
        }
        return flag;
}
    public boolean processPOHDR(Map map) throws Exception {
            boolean flag = false;
            MasterDAO _MasterDAO = new  MasterDAO();
            MasterUtil _MasterUtil = new MasterUtil();
            Hashtable htMaster=new Hashtable();
            Hashtable htRecvHis=new Hashtable();
            try {
                  Hashtable htpohdr =  new Hashtable();
                htpohdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
               
                htpohdr.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
                
                htpohdr.put(IConstants.payment_terms, (String)map.get(IConstants.payment_terms));
                htpohdr.put(IConstants.TRANSPORTID, (String)map.get(IConstants.TRANSPORTID));
               
                htpohdr.put(IConstants.IN_STATUS, "N");
                htpohdr.put(IConstants.IN_ORDERTYPE, (String)map.get(IConstants.IN_ORDERTYPE));
              
                htpohdr.put(IConstants.IN_COLLECTION_DATE, (String)map.get(IConstants.IN_COLLECTION_DATE));
                htpohdr.put(IConstants.IN_COLLECTION_TIME, (String)map.get(IConstants.IN_COLLECTION_TIME));
                htpohdr.put(IConstants.IN_REMARK1, (String)map.get(IConstants.IN_REMARK1));
                
                htpohdr.put(IConstants.LOANHDR_ADDRESS, (String)map.get(IConstants.LOANHDR_ADDRESS));
                htpohdr.put(IConstants.LOANHDR_ADDRESS2, (String)map.get(IConstants.LOANHDR_ADDRESS2));
                htpohdr.put(IConstants.LOANHDR_ADDRESS3, (String)map.get(IConstants.LOANHDR_ADDRESS3));
                htpohdr.put(IConstants.LOANHDR_CONTACT_NUM, (String)map.get(IConstants.LOANHDR_CONTACT_NUM));
                htpohdr.put("POESTNO", "");
                
                System.out.println("map.get(IConstants.IN_REMARK1).toString()"+map.get(IConstants.IN_REMARK1).toString());
                if (!map.get(IConstants.IN_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.IN_REMARK1).toString()!= null){
					htMaster.clear();
					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK1));
				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
					if(!map.get(IConstants.IN_REMARK1).toString().equalsIgnoreCase("")){
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
						htRecvHis.put("REMARKS", (String)map.get(IConstants.IN_REMARK1));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
						htRecvHis.put(IDBConstants.TRAN_DATE,
						DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						flag = movHisDao.insertIntoMovHis(htRecvHis);
				   	}
					}
				}
                 htpohdr.put(IConstants.IN_REMARK2, (String)map.get(IConstants.IN_REMARK2));
                 htpohdr.put(IConstants.IN_REMARK3, (String)map.get(IConstants.IN_REMARK3));
                 if (!map.get(IConstants.IN_REMARK3).toString().equalsIgnoreCase("null") && map.get(IConstants.IN_REMARK3).toString()!= null){
					htMaster.clear();
					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK3));
				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
					if(!map.get(IConstants.IN_REMARK3).toString().equalsIgnoreCase("")){
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
						htRecvHis.put("REMARKS", (String)map.get(IConstants.IN_REMARK3));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
						htRecvHis.put(IDBConstants.TRAN_DATE,
						DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						flag = movHisDao.insertIntoMovHis(htRecvHis);
					  }
					}
				}
               
                htpohdr.put(IConstants.IN_INBOUND_GST, (String)map.get(IConstants.IN_INBOUND_GST));
                htpohdr.put(IConstants.IN_CUST_CODE, (String)map.get(IConstants.IN_CUST_CODE));
                htpohdr.put(IConstants.IN_CUST_NAME, (String)map.get(IConstants.IN_CUST_NAME));
                htpohdr.put(IConstants.IN_PERSON_IN_CHARGE, (String)map.get(IConstants.IN_PERSON_IN_CHARGE));
                htpohdr.put(IConstants.IN_REF_NO, (String)map.get(IConstants.IN_REF_NO));
                htpohdr.put(IConstants.IN_CURRENCYID, (String)map.get(IConstants.IN_CURRENCYID));
				htpohdr.put(IConstants.DEL_DATE, (String)map.get(IConstants.DEL_DATE));
				htpohdr.put(IConstants.ORDERDISCOUNT, (String)map.get(IConstants.ORDERDISCOUNT));
                htpohdr.put(IConstants.SHIPPINGCOST, (String)map.get(IConstants.SHIPPINGCOST));
                htpohdr.put(IConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
                htpohdr.put(IConstants.LOCALEXPENSES, (String)map.get(IConstants.LOCALEXPENSES));
                htpohdr.put(IConstants.PAYMENTTYPE, (String)map.get(IConstants.PAYMENTTYPE));
                htpohdr.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT));
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
                              
     			
     			 htpohdr.put(IConstants.SHIPPINGCUSTOMER, (String)map.get(IConstants.SHIPPINGCUSTOMER));
     			 htpohdr.put(IConstants.TAXTREATMENT, (String)map.get(IConstants.TAXTREATMENT));
     			 htpohdr.put(IConstants.POHDR_EMPNO, (String)map.get(IConstants.POHDR_EMPNO));
     			 htpohdr.put(IConstants.PURCHASE_LOCATION, (String)map.get(IConstants.PURCHASE_LOCATION));
     			htpohdr.put(IConstants.REVERSECHARGE, (String)map.get(IConstants.REVERSECHARGE));
     			htpohdr.put(IConstants.GOODSIMPORT, (String)map.get(IConstants.GOODSIMPORT));
     			 boolean isExistShippingCustomer=false;
     			  if (map.get(IConstants.SHIPPINGCUSTOMER).toString().length() > 0) {
     				 isExistShippingCustomer = _MasterDAO.isExistsShippingDetails((String)map.get(IConstants.SHIPPINGCUSTOMER),(String)map.get(IConstants.PLANT));
     				   
                     htpohdr.put(IConstants.SHIP_CONTACTNAME, (String)map.get(IConstants.SHIP_CONTACTNAME));
                     htpohdr.put(IConstants.SHIP_DESGINATION, (String)map.get(IConstants.SHIP_DESGINATION));
                     htpohdr.put(IConstants.SHIP_WORKPHONE, (String)map.get(IConstants.SHIP_WORKPHONE));
                     htpohdr.put(IConstants.SHIP_HPNO, (String)map.get(IConstants.SHIP_HPNO));
                     htpohdr.put(IConstants.SHIP_EMAIL, (String)map.get(IConstants.SHIP_EMAIL));
                     htpohdr.put(IConstants.SHIP_COUNTRY, (String)map.get(IConstants.SHIP_COUNTRY));
                     htpohdr.put(IConstants.SHIP_ADDR1, (String)map.get(IConstants.SHIP_ADDR1));
                     htpohdr.put(IConstants.SHIP_ADDR2, (String)map.get(IConstants.SHIP_ADDR2));
                     htpohdr.put(IConstants.SHIP_ADDR3, (String)map.get(IConstants.SHIP_ADDR3));
                     htpohdr.put(IConstants.SHIP_ADDR4, (String)map.get(IConstants.SHIP_ADDR4));
                     htpohdr.put(IConstants.SHIP_STATE, (String)map.get(IConstants.SHIP_STATE));
                     htpohdr.put(IConstants.SHIP_ZIP, (String)map.get(IConstants.SHIP_ZIP));
       			} else {
       				htpohdr.put(IConstants.SHIP_CONTACTNAME, "");
       				htpohdr.put(IConstants.SHIP_DESGINATION, "");
       				htpohdr.put(IConstants.SHIP_WORKPHONE, "");
       				htpohdr.put(IConstants.SHIP_HPNO, "");
       				htpohdr.put(IConstants.SHIP_EMAIL, "");
       				htpohdr.put(IConstants.SHIP_COUNTRY, "");
       				htpohdr.put(IConstants.SHIP_ADDR1, "");
       				htpohdr.put(IConstants.SHIP_ADDR2, "");
       				htpohdr.put(IConstants.SHIP_ADDR3, "");
       				htpohdr.put(IConstants.SHIP_ADDR4, "");
       				htpohdr.put(IConstants.SHIP_STATE, "");
       				htpohdr.put(IConstants.SHIP_ZIP, "");       				
       			}
     			 if (!map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase("null") && map.get(IConstants.SHIPPINGCUSTOMER).toString()!= null){
     			  if(isExistShippingCustomer==false){
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
     			 
     			htpohdr.put("CURRENCYUSEQT", (String)map.get("CURRENCYUSEQT"));
     			htpohdr.put("ISDISCOUNTTAX", (String)map.get("ISDISCOUNTTAX"));
     			htpohdr.put("ISSHIPPINGTAX", (String)map.get("ISSHIPPINGTAX"));
     			htpohdr.put("ISTAXINCLUSIVE", (String)map.get("ISTAXINCLUSIVE"));
     			htpohdr.put("ORDERDISCOUNTTYPE", (String)map.get("ORDERDISCOUNTTYPE"));
     			htpohdr.put("TAXID", (String)map.get("TAXID"));
     			htpohdr.put("PROJECTID", (String)map.get("PROJECTID"));
     			htpohdr.put("SHIPPINGID", (String)map.get("SHIPPINGID"));
     			htpohdr.put("STATUS_ID", "NOT PAID");
     			htpohdr.put("ADJUSTMENT", (String)map.get("ADJUSTMENT"));
     			htpohdr.put("ORDER_STATUS", "Open");
     			htpohdr.put("CRBY", (String)map.get("LOGIN_USER"));
     			htpohdr.put("CRAT", dateUtils.getDate());
                
                // data insert into POHDR table
                flag = poUtil.savePoHdrDetails(htpohdr);
            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }

    public boolean processUpdatePohdr(Map map) throws Exception {
            boolean flag = false;
            MasterDAO _MasterDAO = new  MasterDAO();
            MasterUtil _MasterUtil = new MasterUtil();
            Hashtable htMaster=new Hashtable();
            Hashtable htRecvHis=new Hashtable();
            try {
                 
                PoHdrDAO _PoHdrDAO =new PoHdrDAO();
                _PoHdrDAO.setmLogger(mLogger);
                Hashtable htCond = new Hashtable();
                htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
                htCond.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
                
                StringBuffer updateQyery = new StringBuffer("set ");
                updateQyery.append(IConstants.IN_REF_NO + " = '" + (String)map.get(IConstants.IN_REF_NO) + "'");
                updateQyery.append("," + IConstants.IN_ORDERTYPE + " = '" +  (String)map.get(IConstants.IN_ORDERTYPE) + "'");
                updateQyery.append("," + IConstants.IN_COLLECTION_DATE + " = '"+ (String)map.get(IConstants.IN_COLLECTION_DATE) + "'");
                updateQyery.append("," + IConstants.IN_COLLECTION_TIME + " = '"+ (String)map.get(IConstants.IN_COLLECTION_TIME) + "'");
                updateQyery.append("," + IConstants.IN_REMARK1 + " = '"+ (String)map.get(IConstants.IN_REMARK1) + "'");
                updateQyery.append("," + IConstants.IN_REMARK2 + " = '"+ (String)map.get(IConstants.IN_REMARK2) + "'");
                updateQyery.append("," + IConstants.IN_REMARK3 + " = '"+ (String)map.get(IConstants.IN_REMARK3) + "'");
                updateQyery.append("," + IConstants.IN_INBOUND_GST + " = '"+ (String)map.get(IConstants.IN_INBOUND_GST) + "'");
                updateQyery.append(","+ IConstants.IN_CUST_CODE + " = '" + (String)map.get(IConstants.IN_CUST_CODE) + "'");
                updateQyery.append("," +IConstants.IN_CUST_NAME + " = '" + (String)map.get(IConstants.IN_CUST_NAME) + "'");
                updateQyery.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ (String)map.get(IConstants.IN_PERSON_IN_CHARGE) + "'");
                updateQyery.append("," + IConstants.IN_CURRENCYID + " = '"+ (String)map.get(IConstants.IN_CURRENCYID) + "'");
                 updateQyery.append("," + IConstants.DEL_DATE + " = '"+ (String)map.get(IConstants.DEL_DATE) + "'");
                 updateQyery.append("," +IConstants.ORDERDISCOUNT + " = '" + (String)map.get(IConstants.ORDERDISCOUNT) + "'");
                 updateQyery.append("," + IConstants.SHIPPINGCOST + " = '"+ (String)map.get(IConstants.SHIPPINGCOST) + "'");
                 updateQyery.append("," + IConstants.INCOTERMS + " = '"+ (String)map.get(IConstants.INCOTERMS) + "'");
                  updateQyery.append("," + IConstants.SHIPPINGCUSTOMER + " = '"+ (String)map.get(IConstants.SHIPPINGCUSTOMER) + "'");
                  updateQyery.append("," + IConstants.LOCALEXPENSES + " = '"+ (String)map.get(IConstants.LOCALEXPENSES) + "'");
				  updateQyery.append("," + IConstants.PAYMENTTYPE + " = '"+ (String)map.get(IConstants.PAYMENTTYPE) + "'");
				  updateQyery.append("," + IConstants.POHDR_DELIVERYDATEFORMAT + " = '"+ (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT) + "'");
				  updateQyery.append("," + IConstants.TAXTREATMENT + " = '"+ (String)map.get(IConstants.TAXTREATMENT) + "'");
				  updateQyery.append("," + IConstants.PURCHASE_LOCATION + " = '"+ (String)map.get(IConstants.PURCHASE_LOCATION) + "'"); 
				  updateQyery.append("," + IConstants.POHDR_EMPNO + " = '"+ (String)map.get(IConstants.POHDR_EMPNO) + "'"); 
				  
	                updateQyery.append("," + IConstants.payment_terms + " = '"+ (String)map.get(IConstants.payment_terms) + "'"); 
	                updateQyery.append("," + IConstants.TRANSPORTID + " = '"+ (String)map.get(IConstants.TRANSPORTID) + "'"); 
	                
	                updateQyery.append("," + IConstants.LOANHDR_ADDRESS + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS) + "'"); 
	                updateQyery.append("," + IConstants.LOANHDR_ADDRESS2 + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS2) + "'"); 
	                updateQyery.append("," + IConstants.LOANHDR_ADDRESS3 + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS3) + "'"); 
	                updateQyery.append("," + IConstants.LOANHDR_CONTACT_NUM + " = '"+ (String)map.get(IConstants.LOANHDR_CONTACT_NUM) + "'"); 
	                
				  updateQyery.append("," + IConstants.SHIP_CONTACTNAME + " = '"+ (String)map.get(IConstants.SHIP_CONTACTNAME) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_DESGINATION + " = '"+ (String)map.get(IConstants.SHIP_DESGINATION) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_WORKPHONE + " = '"+ (String)map.get(IConstants.SHIP_WORKPHONE) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_HPNO + " = '"+ (String)map.get(IConstants.SHIP_HPNO) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_EMAIL + " = '"+ (String)map.get(IConstants.SHIP_EMAIL) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_COUNTRY + " = '"+ (String)map.get(IConstants.SHIP_COUNTRY) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_ADDR1 + " = '"+ (String)map.get(IConstants.SHIP_ADDR1) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_ADDR2 + " = '"+ (String)map.get(IConstants.SHIP_ADDR2) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_ADDR3 + " = '"+ (String)map.get(IConstants.SHIP_ADDR3) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_ADDR4 + " = '"+ (String)map.get(IConstants.SHIP_ADDR4) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_STATE + " = '"+ (String)map.get(IConstants.SHIP_STATE) + "'"); 
				  updateQyery.append("," + IConstants.SHIP_ZIP + " = '"+ (String)map.get(IConstants.SHIP_ZIP) + "'"); 
				  
                  
                  
				  updateQyery.append(", CURRENCYUSEQT = '"+(String)map.get("CURRENCYUSEQT") + "'");
				  updateQyery.append(", ISDISCOUNTTAX = '"+(String)map.get("ISDISCOUNTTAX") + "'");
				  updateQyery.append(", ISSHIPPINGTAX = '"+ (String)map.get("ISSHIPPINGTAX") + "'");
				  updateQyery.append(", ISTAXINCLUSIVE = '"+ (String)map.get("ISTAXINCLUSIVE") + "'");
				  updateQyery.append(", ORDERDISCOUNTTYPE = '"+ (String)map.get("ORDERDISCOUNTTYPE") + "'");
				  updateQyery.append(", TAXID = '"+ (String)map.get("TAXID") + "'");
				  updateQyery.append(", PROJECTID = '"+ (String)map.get("PROJECTID") + "'");
				  updateQyery.append(", SHIPPINGID = '"+ (String)map.get("SHIPPINGID") + "'");
				  updateQyery.append(", ADJUSTMENT = '"+ (String)map.get("ADJUSTMENT") + "'");
                flag = _PoHdrDAO.updatePO(updateQyery.toString(), htCond, " ");
                if(flag)////insert remarks,incoterm and shipping customer into master
                {
          	  
                	  if (!map.get(IConstants.IN_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.IN_REMARK1).toString()!= null){
      					htMaster.clear();
      					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
      					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK1));
      				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
      					if(!map.get(IConstants.IN_REMARK1).toString().equalsIgnoreCase("")){
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
      						htRecvHis.put("REMARKS", (String)map.get(IConstants.IN_REMARK1));
      						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
      						htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
      						htRecvHis.put(IDBConstants.TRAN_DATE,
      						DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
      						flag = movHisDao.insertIntoMovHis(htRecvHis);
      					  }
      					}
      				}
                    
                      if (!map.get(IConstants.IN_REMARK3).toString().equalsIgnoreCase("null") && map.get(IConstants.IN_REMARK3).toString()!= null){
      					htMaster.clear();
      					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
      					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK3));
      				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
      					if(!map.get(IConstants.IN_REMARK3).toString().equalsIgnoreCase("")){
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
      						htRecvHis.put("REMARKS", (String)map.get(IConstants.IN_REMARK3));
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
         			  if (map.get(IConstants.SHIPPINGCUSTOMER).toString().length() > 0) {
         				 isExistShippingCustomer = _MasterDAO.isExistsShippingDetails((String)map.get(IConstants.SHIPPINGCUSTOMER),(String)map.get(IConstants.PLANT));
           			}
                      if (!map.get(IConstants.SHIPPINGCUSTOMER).toString().equalsIgnoreCase("null") && map.get(IConstants.SHIPPINGCUSTOMER).toString()!= null){
             			  if(isExistShippingCustomer==false){
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
                    throw new Exception("Inbound Order "+(String)map.get(IConstants.IN_PONO)+" is processed,Cannot Modify");
                }
                
             
            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
    
    public boolean processDeletePodet(Map map) throws Exception {
            boolean flag = false;
           
            try {
                Hashtable htpodet = new Hashtable();
                htpodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                htpodet.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
                htpodet.put(IConstants.IN_POLNNO, (String)map.get(IConstants.IN_POLNNO));
                
                boolean isLineExist = poUtil.isExitsPoLine(htpodet);
                if(isLineExist){
                    PoDetDAO _podetDAO = new PoDetDAO();
                   _podetDAO.setmLogger(mLogger);
                   flag  =_podetDAO.delete(htpodet);
                }else{
                    flag=true; 
                }
                
             
            } catch (Exception e) {
                    throw e;
            }
            return flag;
    }
   //start code by Bruhan for export excel if status is open updating price on 16 sep 2013 
    public boolean processUpdatePodet(Map map) throws Exception {
        boolean flag = false;
       
        try {
             
            PoDetDAO _PoDetDAO =new PoDetDAO();
            _PoDetDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
            htCond.put(IConstants.IN_POLNNO, (String)map.get(IConstants.IN_POLNNO));
            htCond.put(IConstants.IN_ITEM, (String)map.get(IConstants.IN_ITEM));
            htCond.put("DISCOUNT", (String)map.get("PRODUCTDISCOUNT"));
            htCond.put("DISCOUNT_TYPE", (String)map.get("PRODUCTDISCOUNT_TYPE"));
            htCond.put("ACCOUNT_NAME", (String)map.get("ACCOUNT_NAME"));
            htCond.put("PRODUCTDELIVERYDATE", (String)map.get("PRODUCTDELIVERYDATE"));
            htCond.put("TAX_TYPE", (String)map.get("Tax"));
            
            
            String CURRENCYUSEQT = new POUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.IN_PONO));
       
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.IN_UNITCOST + " = '" + (String)map.get(IConstants.IN_UNITCOST) + "'");
            updateQyery.append("," + IDBConstants.CURRENCYUSEQT + " = '"+ (String)map.get("CURRENCYUSEQT") + "'");
        
            flag = _PoDetDAO.update(updateQyery.toString(), htCond, " ");
            if(!flag){
                throw new Exception("Line No:"+(String)map.get(IConstants.IN_POLNNO)+" , Item : "+(String)map.get(IConstants.IN_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.IN_PONO) );
             }
         
        } catch (Exception e) {
        	throw new Exception("Line No:"+(String)map.get(IConstants.IN_POLNNO)+" , Item : "+(String)map.get(IConstants.IN_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.IN_PONO) );
        }
        return flag;
}
    
    public boolean processUpdateRECVDET(Map map) throws Exception {
        boolean flag = false;
       
        try {
             
        	RecvDetDAO _RecvDetDAO =new RecvDetDAO();
        	_RecvDetDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.IN_PONO, (String)map.get(IConstants.IN_PONO));
            htCond.put("LNNO", (String)map.get(IConstants.IN_POLNNO)); 
            htCond.put(IConstants.IN_ITEM, (String)map.get(IConstants.IN_ITEM));
            
            boolean isExist = false;
			isExist = _RecvDetDAO.isExisit(htCond, (String) map.get(IConstants.PLANT));
			
            if(isExist){
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.IN_UNITCOST + " = '" + (String)map.get(IConstants.IN_UNITCOST) + "'");
            updateQyery.append("," + IConstants.IN_CURRENCYID + " = '"+ (String)map.get(IConstants.IN_CURRENCYID) + "'");
        
           flag = _RecvDetDAO.update(updateQyery.toString(), htCond, " ",(String) map.get(IConstants.PLANT));
            }
            else{
            	flag = true;
            }
            
         
        } catch (Exception e) {
                throw e;
        }
        return flag;
}

  //end code by Bruhan for export excel if status is open updating price on 16 sep 2013
 public boolean processRECVDET(Map map) throws Exception {
        boolean flag = false;
        RecvDetDAO _RecvDetDAO =new RecvDetDAO();
    	_RecvDetDAO.setmLogger(mLogger);
       
    try {
    	 
  
    		Hashtable 	htRecvDet = new Hashtable();
			htRecvDet.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM,(String)map.get(IConstants.IN_PONO));
			htRecvDet.put("LNNO",(String)map.get(IConstants.IN_POLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, (String)map.get(IConstants.IN_CUST_NAME));
			htRecvDet.put(IDBConstants.ITEM, (String)map.get(IConstants.IN_ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC,(String)map.get(IConstants.IN_ITEM_DES));
			htRecvDet.put("BATCH", "NOBATCH");
			htRecvDet.put(IDBConstants.LOC, "");
			htRecvDet.put("ORDQTY", (String)map.get(IDBConstants.PODET_QTYRC));
			htRecvDet.put("RECQTY",(String)map.get(IDBConstants.PODET_QTYRC));
			Hashtable pohashtable = new Hashtable();
			pohashtable.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
			pohashtable.put(IDBConstants.PODET_PONUM, (String)map.get(IConstants.IN_PONO));
			pohashtable.put("POLNNO",(String)map.get(IConstants.IN_POLNNO));
			pohashtable.put(IDBConstants.ITEM, (String)map.get(IConstants.IN_ITEM));
			htRecvDet.put("UNITCOST",poUtil.getPodetColdata(pohashtable,"UNITCOST"));
			String userfld4 = poUtil.getPodetColdata(pohashtable,"USERFLD4");
			htRecvDet.put("MANUFACTURER",userfld4);
		
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));

			htRecvDet.put(IDBConstants.CURRENCYID,poUtil.getCurrencyID((String) map.get(IConstants.PLANT), (String)map.get(IConstants.IN_PONO)));
	        htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "NS");
		flag = _RecvDetDAO.insertRecvDet(htRecvDet);
	}  catch (Exception e) {
        throw e;
}
return flag;
}
 
}
