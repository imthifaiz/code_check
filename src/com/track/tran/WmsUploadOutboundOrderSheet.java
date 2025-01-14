package com.track.tran;


import java.util.ArrayList;
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
import com.track.db.util.CustUtil;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
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

public class WmsUploadOutboundOrderSheet implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	PrdClassUtil prdclsutil = null;
	PrdTypeUtil prdtypeutil = null;
	ItemSesBeanDAO itemsesdao = null;
    DOTransferUtil _DOTransferUtil = null;
    DOUtil doUtil = null;
    MovHisDAO movHisDao = null;

	public WmsUploadOutboundOrderSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		prdclsutil = new PrdClassUtil();
		prdtypeutil = new PrdTypeUtil();
	    _DOTransferUtil = new DOTransferUtil();
	    doUtil = new DOUtil();
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
            doUtil.setmLogger(mLogger);
			// check DONO that already exist on DOHDR table
			boolean isExistDoNo = false;
			DOUtil doUtil = new DOUtil();
			isExistDoNo = doUtil.isExistDONO((String)map.get(IConstants.OUT_DONO), (String)map.get(IConstants.PLANT));
			    if(isExistDoNo){
			            //Update DOHDR 
			            flag=this.processUpdateDohdr(map);
			    }else{
			        flag=this.processDOHDR(map);
			        if(flag){
                                    flag=this.processMovHisForDOHDR(map);
                                    new TblControlUtil().updateTblControlSeqNo((String)map.get(IConstants.PLANT),IConstants.OUTBOUND,"S",(String)map.get(IConstants.OUT_DONO));
                                }
			    }
			    
			  
			    boolean isNewStatusDoNo = false;
				isNewStatusDoNo = doUtil.isNewStatusDONO((String)map.get(IConstants.OUT_DONO), (String)map.get(IConstants.PLANT));
				 //By Samatha to Capture the price in Local currency on 17/09/2013
	             String unitPrice= new DOUtil().getConvertedUnitCostToLocalCurrency((String)map.get(IConstants.PLANT),(String) map.get(IConstants.OUT_DONO), (String) map.get(IConstants.OUT_UNITCOST)) ;
	             map.put(IConstants.OUT_UNITCOST, unitPrice);
	            //End Samatha
				if(isNewStatusDoNo)
				{
					flag=this.processUpdateDodet(map);
					if(flag){
				        flag=this.processUpdateShipHis(map);
				             
				    }   
					if(flag){
						flag= this.processMovHisForUpdateDODET(map);
					}
					
				}
				else{
		          
			    if(flag){
			        flag=this.processDeleteDodet(map);
			             
			    }       
			    if(flag){
			        flag=this.processDODET(map);
			    }
			    if(flag){
			          flag=this.processMovHisForDODET(map);
			      } 
				}
			      
			    

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	@SuppressWarnings("unchecked")
    public boolean processMovHisForDODET(Map map) throws Exception {
            boolean flag = false;
             try {
                movHisDao.setmLogger(mLogger);
                Hashtable htmovhisdet = new Hashtable();
                // data for MOVHIS table for DODET
                htmovhisdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                htmovhisdet.put(IConstants.DIRTYPE, TransactionConstants.OB_ADD_ITEM);
                htmovhisdet.put(IConstants.LNNO,  (String)map.get(IConstants.OUT_DOLNNO));
                htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.OUT_DONO));
                htmovhisdet.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
                htmovhisdet.put(IConstants.QTY, (String)map.get(IConstants.OUT_QTYOR));
                htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.OUT_UNITCOST));
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
	
	@SuppressWarnings("unchecked")
    public boolean processMovHisForUpdateDODET(Map map) throws Exception {
            boolean flag = false;
              try {
                movHisDao.setmLogger(mLogger);
                Hashtable htmovhisdet = new Hashtable();
                // data for MOVHIS table for DODET
                htmovhisdet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
                htmovhisdet.put(IConstants.DIRTYPE, "SALES_ORDER_UPDATE_PRODUCT");
                htmovhisdet.put(IConstants.LNNO,  (String)map.get(IConstants.OUT_DOLNNO));
                htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.OUT_DONO));
                htmovhisdet.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
                htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.OUT_UNITCOST));
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
	
	

    public boolean processMovHisForDOHDR(Map map) throws Exception {
        boolean flag = false;
      
        try {
            movHisDao.setmLogger(mLogger);
            Hashtable htmovhishdr = new Hashtable();
                
            // data for MOVHIS table for DOHDR
            htmovhishdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htmovhishdr.put(IConstants.DIRTYPE, TransactionConstants.CREATE_OB);
            htmovhishdr.put(IConstants.LNNO, "");
            htmovhishdr.put(IConstants.ORDNUM, (String)map.get(IConstants.OUT_DONO));
            htmovhishdr.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
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


    public boolean processDODET(Map map) throws Exception {
        boolean flag = false;
       
        try {
        	String nonstocktpid="";//added by Bruhan
            Hashtable htdodet =  new Hashtable();
            htdodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htdodet.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            htdodet.put(IConstants.OUT_DOLNNO,(String)map.get(IConstants.OUT_DOLNNO));
            //added by Bruhan
            nonstocktpid = (String)map.get(IConstants.OUT_NONSTKTYPEID);
            Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.OUT_ITEM));
            String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
            String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
          
            if(nonstocktype.equals("Y"))	
		    {
            	  if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
                	htdodet.put(IConstants.OUT_UNITCOST, "-"+(String)map.get(IConstants.OUT_UNITCOST));
                }else{
                	htdodet.put(IConstants.OUT_UNITCOST, (String)map.get(IConstants.OUT_UNITCOST));
                }
		    }
            else
            {
            	htdodet.put(IConstants.OUT_UNITCOST, (String)map.get(IConstants.OUT_UNITCOST));

            }
            //end
            htdodet.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
            htdodet.put(IConstants.OUT_ITEM_DES, (String)map.get(IConstants.OUT_ITEM_DES));
       
            htdodet.put(IConstants.OUT_TRANDATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
            //htdodet.put(IConstants.OUT_UNITCOST, (String)map.get(IConstants.OUT_UNITCOST));
            htdodet.put(IConstants.OUT_QTYOR, (String)map.get(IConstants.OUT_QTYOR));
            htdodet.put(IConstants.OUT_UNITMO, (String)map.get(IConstants.OUT_UNITMO));
            htdodet.put(IConstants.OUT_ITEM_DET_DES, (String)map.get(IConstants.OUT_ITEM_DET_DES));
            htdodet.put(IConstants.OUT_CUSTOMER, (String)map.get(IConstants.OUT_CUST_NAME));
            htdodet.put(IConstants.OUT_ITEM_DET_DES, (String)map.get(IConstants.OUT_ITEM_DES));
            htdodet.put(IConstants.OUT_USERFLD2, (String)map.get(IConstants.OUT_REF_NO));
            htdodet.put(IConstants.OUT_LNSTAT, "N");
        	htdodet.put(IConstants.OUT_PICK_STAT, "N");
            String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.OUT_DONO));
            htdodet.put("QTYIS", "0.0");
            htdodet.put("QtyPick", "0.0");
            htdodet.put("ESTLNNO", "0");
            htdodet.put("PRODUCTDELIVERYDATE", (String)map.get("PRODUCTDELIVERYDATE"));
            htdodet.put("ACCOUNT_NAME", (String)map.get("ACCOUNT"));
            htdodet.put("TAX_TYPE", (String)map.get("TAX"));
            htdodet.put("DISCOUNT", (String)map.get("PRODUCTDISCOUNT"));
            htdodet.put("DISCOUNT_TYPE", (String)map.get("PRODUCTDISCOUNT_TYPE"));
            htdodet.put("CRAT", dateUtils.getDate());
            htdodet.put("CRBY", (String)map.get("LOGIN_USER"));
            htdodet.put(IDBConstants.CURRENCYUSEQT, (String)map.get("EQUIVALENTCURRENCY"));
                flag = doUtil.saveDoDetDetails(htdodet);
                if(flag)    {
                     flag = _DOTransferUtil.saveDoTransferDetDetails(htdodet);
                }
  				DoDetDAO _DoDetDAO=new DoDetDAO();
                Hashtable htRemarksDel = new Hashtable();
                htRemarksDel.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
                htRemarksDel.put(IDBConstants.DODET_DONUM,  (String)map.get(IConstants.OUT_DONO));
                htRemarksDel.put(IDBConstants.DODET_DOLNNO,  (String)map.get(IConstants.OUT_DOLNNO));
 				if(_DoDetDAO.isExisitDoMultiRemarks(htRemarksDel))
                {
                   flag = _DoDetDAO.deleteDoMultiRemarks(htRemarksDel);
                }
 				
 				Hashtable htRemarks = new Hashtable();
				htRemarks.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htRemarks.put(IDBConstants.DODET_DONUM, (String)map.get(IConstants.OUT_DONO));
				htRemarks.put(IDBConstants.DODET_DOLNNO, (String)map.get(IConstants.OUT_DOLNNO));
				htRemarks.put(IDBConstants.DODET_ITEM, (String)map.get(IConstants.OUT_ITEM));
				htRemarks.put(IDBConstants.REMARKS,"");
				htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htRemarks.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
				flag = doUtil.saveDoMultiRemarks(htRemarks);
         
            

        } catch (Exception e) {
                throw e;
        }
        return flag;
    }
     public boolean processDOHDR(Map map) throws Exception {
        boolean flag = false;
        MasterDAO _MasterDAO = new  MasterDAO();
        MasterUtil _MasterUtil = new MasterUtil();
        Hashtable htMaster=new Hashtable();
        Hashtable htRecvHis=new Hashtable();
        try {
              Hashtable htdohdr =  new Hashtable();
            htdohdr.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htdohdr.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            htdohdr.put(IConstants.OUT_STATUS, "N");
            htdohdr.put(IConstants.OUT_ORDERTYPE, (String)map.get(IConstants.OUT_ORDERTYPE));
            htdohdr.put(IConstants.OUT_COLLECTION_DATE, (String)map.get(IConstants.OUT_COLLECTION_DATE));
            htdohdr.put(IConstants.OUT_COLLECTION_TIME, (String)map.get(IConstants.OUT_COLLECTION_TIME));
            
            htdohdr.put(IConstants.LOANHDR_ADDRESS, (String)map.get(IConstants.LOANHDR_ADDRESS));
            htdohdr.put(IConstants.LOANHDR_ADDRESS2, (String)map.get(IConstants.LOANHDR_ADDRESS2));
            htdohdr.put(IConstants.LOANHDR_ADDRESS3, (String)map.get(IConstants.LOANHDR_ADDRESS3));
            htdohdr.put(IConstants.LOANHDR_CONTACT_NUM, (String)map.get(IConstants.LOANHDR_CONTACT_NUM));
            
            htdohdr.put(IConstants.OUT_REMARK1, (String)map.get(IConstants.OUT_REMARK1));
            if (!map.get(IConstants.OUT_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.OUT_REMARK1).toString()!= null){
				htMaster.clear();
				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.OUT_REMARK1));
			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
				if(!map.get(IConstants.OUT_REMARK1).toString().equalsIgnoreCase("")){
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
					htRecvHis.put("REMARKS", (String)map.get(IConstants.OUT_REMARK1));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
					htRecvHis.put(IDBConstants.TRAN_DATE,
					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					flag = movHisDao.insertIntoMovHis(htRecvHis);
				}
			 }
			}
            htdohdr.put(IConstants.OUT_REMARK2, (String)map.get(IConstants.OUT_REMARK2));
            htdohdr.put(IConstants.OUT_REMARK3, (String)map.get(IConstants.OUT_REMARK3));
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
          
            htdohdr.put(IConstants.OUT_OUTBOUND_GST, (String)map.get(IConstants.OUT_OUTBOUND_GST));
            htdohdr.put(IConstants.OUT_CUST_CODE, (String)map.get(IConstants.OUT_CUST_CODE));
            htdohdr.put(IConstants.OUT_CUST_NAME, (String)map.get(IConstants.OUT_CUST_NAME));
            htdohdr.put(IConstants.OUT_PERSON_IN_CHARGE, (String)map.get(IConstants.OUT_PERSON_IN_CHARGE));
            htdohdr.put(IConstants.OUT_REF_NO,(String)map.get(IConstants.OUT_REF_NO));
            htdohdr.put(IConstants.OUT_CURRENCYID,(String)map.get(IConstants.OUT_CURRENCYID));
            htdohdr.put(IConstants.DOHDR_EMPNO, (String)map.get(IConstants.DOHDR_EMPNO));
            htdohdr.put(IConstants.DELIVERYDATE, (String)map.get(IConstants.DELIVERYDATE));
            htdohdr.put(IConstants.SHIPPINGCOST, (String)map.get(IConstants.SHIPPINGCOST));
            htdohdr.put(IConstants.ORDERDISCOUNT, (String)map.get(IConstants.ORDERDISCOUNT));
			htdohdr.put(IConstants.PAYMENTTYPE, (String)map.get(IConstants.PAYMENTTYPE));
			htdohdr.put(IConstants.POHDR_DELIVERYDATEFORMAT, (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT));
			htdohdr.put(IConstants.TAXTREATMENT, (String)map.get(IConstants.TAXTREATMENT));
			htdohdr.put(IConstants.SALES_LOCATION, (String)map.get(IConstants.SALES_LOCATION));
            htdohdr.put(IConstants.INCOTERMS, (String)map.get(IConstants.INCOTERMS));
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
            CustUtil custUtil = new CustUtil();
            String shipCust = (String)map.get(IConstants.SHIPPINGCUSTOMER);
			 ArrayList arrCustCUS = custUtil.getCustomerDetails(shipCust,(String)map.get(IConstants.PLANT));
			 shipCust = (String) arrCustCUS.get(1);
			String shipCode = (String) arrCustCUS.get(0);
			 
            htdohdr.put(IConstants.SHIPPINGCUSTOMER, shipCust);
			 boolean isExistShippingCustomer=false;
			  if (shipCust.length() > 0  && !shipCust.equalsIgnoreCase(null)) {
				 isExistShippingCustomer = _MasterDAO.isExistsShippingDetails(shipCust,(String)map.get(IConstants.PLANT));
  			}
			 
			  if (!shipCust.equalsIgnoreCase("null") && shipCust!= null){
			  if(isExistShippingCustomer==false ){
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
				ht.put(IDBConstants.CUSTOMERNAME,shipCust);
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
				htRecvHis.put("REMARKS", shipCust);
				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htRecvHis.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
				htRecvHis.put(IDBConstants.TRAN_DATE,
				DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			
				flag = movHisDao.insertIntoMovHis(htRecvHis);
			  }
			  }
			  
			  htdohdr.put("DELDATE", (String)map.get(IConstants.OUT_COLLECTION_DATE));
			  htdohdr.put("PickStaus", "N");
			  htdohdr.put("SHIPPINGID", shipCode);
			  htdohdr.put("ORDER_STATUS", "Open");
			  htdohdr.put("DISCOUNT", (String)map.get("DISCOUNT"));
			  htdohdr.put("DISCOUNT_TYPE", (String)map.get("DISCOUNTTYPE"));
			  htdohdr.put("ADJUSTMENT", (String)map.get("ADJUSTMENT"));
			  htdohdr.put("ITEM_RATES", (String)map.get("ISTAXINCLUSIVE"));
			  htdohdr.put("PROJECTID", (String)map.get("PROJECTID"));
			  htdohdr.put("CURRENCYUSEQT", (String)map.get("EQUIVALENTCURRENCY"));
			  htdohdr.put("ORDERDISCOUNTTYPE", (String)map.get("ORDERDISCOUNTTYPE"));
			  htdohdr.put("TAXID", (String)map.get("TAXID"));
			  htdohdr.put("ISDISCOUNTTAX", (String)map.get("ISDISCOUNTTAX"));
			  htdohdr.put("ISORDERDISCOUNTTAX", (String)map.get("ISORDERDISCOUNTTAX"));
			  htdohdr.put("ISSHIPPINGTAX", (String)map.get("ISSHIPPINGTAX"));
			  htdohdr.put("CRAT", dateUtils.getDate());
			  htdohdr.put("CRBY", (String)map.get("LOGIN_USER"));
			  htdohdr.put(IConstants.payment_terms, (String)map.get(IConstants.payment_terms));
	          htdohdr.put(IConstants.TRANSPORTID, (String)map.get(IConstants.TRANSPORTID));
	          
				htdohdr.put(IConstants.SHIP_CONTACTNAME, (String)map.get(IConstants.SHIP_CONTACTNAME));
	            htdohdr.put(IConstants.SHIP_DESGINATION, (String)map.get(IConstants.SHIP_DESGINATION));
	            htdohdr.put(IConstants.SHIP_WORKPHONE, (String)map.get(IConstants.SHIP_WORKPHONE));
	            htdohdr.put(IConstants.SHIP_HPNO, (String)map.get(IConstants.SHIP_HPNO));
	            htdohdr.put(IConstants.SHIP_EMAIL, (String)map.get(IConstants.SHIP_EMAIL));
	            htdohdr.put(IConstants.SHIP_COUNTRY, (String)map.get(IConstants.SHIP_COUNTRY));
	            htdohdr.put(IConstants.SHIP_ADDR1, (String)map.get(IConstants.SHIP_ADDR1));
	            htdohdr.put(IConstants.SHIP_ADDR2, (String)map.get(IConstants.SHIP_ADDR2));
	            htdohdr.put(IConstants.SHIP_ADDR3, (String)map.get(IConstants.SHIP_ADDR3));
	            htdohdr.put(IConstants.SHIP_ADDR4, (String)map.get(IConstants.SHIP_ADDR4));
	            htdohdr.put(IConstants.SHIP_STATE, (String)map.get(IConstants.SHIP_STATE));
	            htdohdr.put(IConstants.SHIP_ZIP, (String)map.get(IConstants.SHIP_ZIP));
			 // htdohdr.put("", (String)map.get(""));
			  
            flag = doUtil.saveDoHdrDetails(htdohdr);
            htdohdr.remove(IDBConstants.ORDERTYPE);
            htdohdr.remove(IDBConstants.ORDERDISCOUNT);
            htdohdr.remove(IDBConstants.SHIPPINGCOST);
            htdohdr.remove("ISSHIPPINGTAX");
            htdohdr.remove("ISORDERDISCOUNTTAX");
            htdohdr.remove("ISDISCOUNTTAX");
            htdohdr.remove("CURRENCYUSEQT");
            htdohdr.remove("ITEM_RATES");
            htdohdr.remove("ORDERDISCOUNTTYPE");
            htdohdr.remove("TAXID");
            htdohdr.remove(IConstants.payment_terms);
            htdohdr.remove(IConstants.TRANSPORTID);
            htdohdr.remove(IConstants.SHIP_CONTACTNAME);
            htdohdr.remove(IConstants.SHIP_DESGINATION);
            htdohdr.remove(IConstants.SHIP_WORKPHONE);
            htdohdr.remove(IConstants.SHIP_HPNO);
            htdohdr.remove(IConstants.SHIP_EMAIL);
            htdohdr.remove(IConstants.SHIP_COUNTRY);
            htdohdr.remove(IConstants.SHIP_ADDR1);
            htdohdr.remove(IConstants.SHIP_ADDR2);
            htdohdr.remove(IConstants.SHIP_ADDR3);
            htdohdr.remove(IConstants.SHIP_ADDR4);
            htdohdr.remove(IConstants.SHIP_STATE);
            htdohdr.remove(IConstants.SHIP_ZIP);
            if (flag) {

                    flag = _DOTransferUtil.saveDoTransferHdrDetails(htdohdr);
            }
            
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }

    public boolean processUpdateDohdr(Map map) throws Exception {
        boolean flag = false;
        MasterDAO _MasterDAO = new  MasterDAO();
        MasterUtil _MasterUtil = new MasterUtil();
        Hashtable htMaster=new Hashtable();
        Hashtable htRecvHis=new Hashtable();
       
        try {
             
            DoHdrDAO _DoHdrDAO =new DoHdrDAO();
            _DoHdrDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            
            CustUtil custUtil = new CustUtil();
            String shipCust = (String)map.get(IConstants.SHIPPINGCUSTOMER);
			ArrayList arrCustCUS = custUtil.getCustomerDetails(shipCust,(String)map.get(IConstants.PLANT));
			shipCust = (String) arrCustCUS.get(1);
			String shipCode = (String) arrCustCUS.get(0);
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.OUT_REF_NO + " = '" + (String)map.get(IConstants.OUT_REF_NO) + "'");
           
            updateQyery.append("," + IConstants.OUT_COLLECTION_DATE + " = '"+ (String)map.get(IConstants.OUT_COLLECTION_DATE) + "'");
            updateQyery.append("," + IConstants.OUT_COLLECTION_TIME + " = '"+ (String)map.get(IConstants.OUT_COLLECTION_TIME) + "'");
            updateQyery.append("," + IConstants.OUT_REMARK1 + " = '"+ (String)map.get(IConstants.OUT_REMARK1) + "'");
            updateQyery.append("," + IConstants.OUT_REMARK2 + " = '"+ (String)map.get(IConstants.OUT_REMARK2) + "'");
            updateQyery.append("," + IConstants.OUT_REMARK3 + " = '"+ (String)map.get(IConstants.OUT_REMARK3) + "'");
            updateQyery.append("," + IConstants.OUT_OUTBOUND_GST + " = '"+ (String)map.get(IConstants.OUT_OUTBOUND_GST) + "'");
            updateQyery.append(","+ IConstants.OUT_CUST_CODE + " = '" + (String)map.get(IConstants.OUT_CUST_CODE) + "'");
            updateQyery.append("," +IConstants.OUT_CUST_NAME + " = '" + (String)map.get(IConstants.IN_CUST_NAME) + "'");
            updateQyery.append("," + IConstants.OUT_PERSON_IN_CHARGE + " = '"+ (String)map.get(IConstants.OUT_PERSON_IN_CHARGE) + "'");
            updateQyery.append("," + IConstants.DOHDR_EMPNO + " = '"+ (String)map.get(IConstants.DOHDR_EMPNO) + "'");
            updateQyery.append("," + IConstants.DELIVERYDATE + " = '"+ (String)map.get(IConstants.DELIVERYDATE) + "'");
            updateQyery.append("," +IConstants.ORDERDISCOUNT + " = '" + (String)map.get(IConstants.ORDERDISCOUNT) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCOST + " = '"+ (String)map.get(IConstants.SHIPPINGCOST) + "'");
            updateQyery.append("," + IConstants.INCOTERMS + " = '"+ (String)map.get(IConstants.INCOTERMS) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCUSTOMER + " = '"+ shipCust + "'");
            updateQyery.append("," + IConstants.PAYMENTTYPE + " = '"+ (String)map.get(IConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IConstants.POHDR_DELIVERYDATEFORMAT + " = '"+ (String)map.get(IConstants.POHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IConstants.TAXTREATMENT + " = '"+ (String)map.get(IConstants.TAXTREATMENT) + "'");
			updateQyery.append("," + IConstants.SALES_LOCATION + " = '"+ (String)map.get(IConstants.SALES_LOCATION) + "'");
			

              
              updateQyery.append("," + IConstants.LOANHDR_ADDRESS + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS) + "'"); 
              updateQyery.append("," + IConstants.LOANHDR_ADDRESS2 + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS2) + "'"); 
              updateQyery.append("," + IConstants.LOANHDR_ADDRESS3 + " = '"+ (String)map.get(IConstants.LOANHDR_ADDRESS3) + "'"); 
              updateQyery.append("," + IConstants.LOANHDR_CONTACT_NUM + " = '"+ (String)map.get(IConstants.LOANHDR_CONTACT_NUM) + "'"); 
              
			
			
			updateQyery.append(",SHIPPINGID = '"+ shipCode + "'");
			updateQyery.append(",DISCOUNT = '"+ (String)map.get("DISCOUNT")+ "'");
			updateQyery.append(",DISCOUNT_TYPE = '"+ (String)map.get("DISCOUNTTYPE")+ "'");
			updateQyery.append(",ADJUSTMENT = '"+ (String)map.get("ADJUSTMENT")+ "'");
			updateQyery.append(",PROJECTID = '"+ (String)map.get("PROJECTID")+ "'");
			
			
			
            flag = new DoTransferHdrDAO().update(updateQyery.toString(), htCond, " ");
          
            if(flag){
                updateQyery.append("," + IConstants.OUT_CURRENCYID + " = '"+ (String)map.get(IConstants.OUT_CURRENCYID) + "'");
                updateQyery.append("," + IConstants.OUT_ORDERTYPE + " = '" +  (String)map.get(IConstants.OUT_ORDERTYPE) + "'");
                updateQyery.append(",TAXID = '"+ (String)map.get("TAXID")+ "'");
    			updateQyery.append(",ISDISCOUNTTAX = '"+ (String)map.get("ISDISCOUNTTAX")+ "'");
    			updateQyery.append(",ISORDERDISCOUNTTAX = '"+ (String)map.get("ISORDERDISCOUNTTAX")+ "'");
    			updateQyery.append(",ISSHIPPINGTAX = '"+ (String)map.get("ISSHIPPINGTAX")+ "'");
    			updateQyery.append(",CURRENCYUSEQT = '"+ (String)map.get("EQUIVALENTCURRENCY")+ "'");
    			updateQyery.append(",ORDERDISCOUNTTYPE = '"+ (String)map.get("ORDERDISCOUNTTYPE")+ "'");
    			updateQyery.append(",ITEM_RATES = '"+ (String)map.get("ISTAXINCLUSIVE")+ "'");
    			updateQyery.append(",DELDATE = '"+ (String)map.get(IConstants.OUT_COLLECTION_DATE)+ "'");
  			  	updateQyery.append("," + IConstants.payment_terms + " = '"+ (String)map.get(IConstants.payment_terms) + "'"); 
  			  	updateQyery.append("," + IConstants.TRANSPORTID + " = '"+ (String)map.get(IConstants.TRANSPORTID) + "'"); 
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
                flag = _DoHdrDAO.update(updateQyery.toString(), htCond, "  ");
            }
            
//            if(flag)///insert remarks,incoterm and shipping customer into master
            { 
      	  
            	  if (!map.get(IConstants.OUT_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.OUT_REMARK1).toString()!= null){
  					htMaster.clear();
  					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK1));
  				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
  					if(!map.get(IConstants.OUT_REMARK1).toString().equalsIgnoreCase("")){
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
  						htRecvHis.put("REMARKS", (String)map.get(IConstants.OUT_REMARK1));
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
  					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.IN_REMARK3));
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
                throw new Exception("Outbound Order "+(String)map.get(IConstants.OUT_DONO)+" is processed,Cannot Modify");
            }
            
         
        } catch (Exception e) {
                throw e;
        }
        return flag;
    }
    
    public boolean processDeleteDodet(Map map) throws Exception {
        boolean flag = false;
       
        try {
            Hashtable htdodet = new Hashtable();
            htdodet.put(IConstants.PLANT, (String)map.get(IConstants.PLANT));
            htdodet.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            htdodet.put(IConstants.OUT_DOLNNO, (String)map.get(IConstants.OUT_DOLNNO));
            
            boolean isLineExist = doUtil.isExitsDoLine(htdodet);
            if(isLineExist){
                DoDetDAO _dodetDAO = new DoDetDAO();
               _dodetDAO.setmLogger(mLogger);
               flag  =_dodetDAO.delete(htdodet);
               if(flag){
                   flag  =new DoTransferDetDAO().delete(htdodet);
               }
            }else{
                flag=true; 
            }
           
        } catch (Exception e) {
                throw e;
          }
        return flag;
    }

    //start code by Bruhan for export excel if status is open updating price on 16 sep 2013 
    public boolean processUpdateDodet(Map map) throws Exception {
        boolean flag = false;
       
        try {
             
            DoDetDAO _DoDetDAO =new DoDetDAO();
            _DoDetDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            htCond.put(IConstants.OUT_DOLNNO, (String)map.get(IConstants.OUT_DOLNNO)); 
            htCond.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
            
            String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.OUT_DONO));
       
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.OUT_UNITCOST + " = '" + (String)map.get(IConstants.OUT_UNITCOST) + "'");
            updateQyery.append("," + IDBConstants.CURRENCYUSEQT + " = '"+ (String)map.get("EQUIVALENTCURRENCY")  + "'");
            updateQyery.append(",PRODUCTDELIVERYDATE = '" + (String)map.get("PRODUCTDELIVERYDATE") + "'");
            updateQyery.append(",ACCOUNT_NAME = '" + (String)map.get("ACCOUNT") + "'");
            updateQyery.append(",TAX_TYPE = '" + (String)map.get("TAX") + "'");
            updateQyery.append(",DISCOUNT = '" + (String)map.get("PRODUCTDISCOUNT") + "'");
            updateQyery.append(",DISCOUNT_TYPE = '" + (String)map.get("PRODUCTDISCOUNT_TYPE") + "'");
           
            
            flag = _DoDetDAO.update(updateQyery.toString(), htCond, " ");
            if(!flag){
               throw new Exception("Line No:"+(String)map.get(IConstants.OUT_DOLNNO)+" , Item : "+(String)map.get(IConstants.OUT_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.OUT_DONO) );
            }
            
         
        } catch (Exception e) {
        	throw new Exception("Line No:"+(String)map.get(IConstants.OUT_DOLNNO)+" , Item : "+(String)map.get(IConstants.OUT_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.OUT_DONO) );
        }
        return flag;
}

    public boolean processUpdateShipHis(Map map) throws Exception {
        boolean flag = false;
       
        try {
             
        	ShipHisDAO _ShipHisDAO =new ShipHisDAO();
        	_ShipHisDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.OUT_DONO, (String)map.get(IConstants.OUT_DONO));
            htCond.put("DOLNO", (String)map.get(IConstants.OUT_DOLNNO)); 
            htCond.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
            
            boolean isExist = false;
			isExist = _ShipHisDAO.isExisit(htCond);
			
            if(isExist){
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.OUT_UNITCOST + " = '" + (String)map.get(IConstants.OUT_UNITCOST) + "'");
            updateQyery.append("," + IConstants.OUT_CURRENCYID + " = '"+ (String)map.get(IConstants.OUT_CURRENCYID) + "'");
        
           flag = _ShipHisDAO.update(updateQyery.toString(), htCond, " ");
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
	
}
