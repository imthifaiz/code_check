package com.track.tran;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
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
import com.track.db.util.ItemUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.PrdTypeUtil;
import com.track.db.util.TblControlUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsUploadLoanOrderSheet implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	PrdClassUtil prdclsutil = null;
	PrdTypeUtil prdtypeutil = null;
	ItemSesBeanDAO itemsesdao = null;
    DOTransferUtil _DOTransferUtil = null;
    DOUtil doUtil = null;
    LoanUtil loUtil = null;
    MovHisDAO movHisDao = null;

	public WmsUploadLoanOrderSheet() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
		prdclsutil = new PrdClassUtil();
		prdtypeutil = new PrdTypeUtil();
	    _DOTransferUtil = new DOTransferUtil();
	    doUtil = new DOUtil();
	    loUtil = new LoanUtil();
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
            loUtil.setmLogger(mLogger);
			// check DONO that already exist on DOHDR table
			boolean isExistDoNo = false;
		//	DOUtil doUtil = new DOUtil();
			LoanUtil loUtil = new LoanUtil();
			isExistDoNo = loUtil.isExistLONO((String)map.get(IConstants.LOANHDR_ORDNO), (String)map.get(IConstants.PLANT));
			    if(isExistDoNo){
			            //Update DOHDR 
			            flag=this.processUpdateDohdr(map);
			    }else{
			        flag=this.processDOHDR(map);
			        if(flag){
                                    flag=this.processMovHisForDOHDR(map);
                                    new TblControlUtil().updateTblControlSeqNo((String)map.get(IConstants.PLANT),IConstants.LOAN,"R",(String)map.get(IConstants.LOANHDR_ORDNO));
                                }
			    }
			    
			  
			    boolean isNewStatusDoNo = false;
				isNewStatusDoNo = loUtil.isNewStatusDONO((String)map.get(IConstants.LOANHDR_ORDNO), (String)map.get(IConstants.PLANT));
				if(isNewStatusDoNo)
				{
					flag=this.processUpdateDodet(map);
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
                htmovhisdet.put(IConstants.DIRTYPE, TransactionConstants.LOAN_ADD_ITEM);
                htmovhisdet.put(IConstants.LNNO,  (String)map.get(IConstants.LOANDET_ORDLNNO));
                htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.LOANDET_ORDNO));
                htmovhisdet.put(IConstants.LOANDET_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
                htmovhisdet.put(IConstants.QTY, (String)map.get(IConstants.LOANDET_QTYOR));
                htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.LOANDET_RENTALPRICE));
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
                htmovhisdet.put(IConstants.DIRTYPE, "LOAN_ORDER_UPDATE_PRODUCT");
                htmovhisdet.put(IConstants.LNNO,  (String)map.get(IConstants.LOANDET_ORDLNNO));
                htmovhisdet.put(IConstants.ORDNUM, (String)map.get(IConstants.LOANDET_ORDNO));
                htmovhisdet.put(IConstants.OUT_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
                htmovhisdet.put(IConstants.REMARKS, (String)map.get(IConstants.LOANDET_RENTALPRICE));
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
            htmovhishdr.put(IConstants.ORDNUM, (String)map.get(IConstants.LOANHDR_ORDNO));
            htmovhishdr.put(IConstants.LOANDET_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
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
            htdodet.put(IConstants.LOANDET_ORDNO, (String)map.get(IConstants.LOANDET_ORDNO));
            htdodet.put(IConstants.LOANDET_ORDLNNO,(String)map.get(IConstants.LOANDET_ORDLNNO));
             {
            	htdodet.put(IConstants.LOANDET_RENTALPRICE, (String)map.get(IConstants.LOANDET_RENTALPRICE));

            }
            //end
            htdodet.put(IConstants.LOANDET_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
            htdodet.put(IConstants.LOANDET_ITEMDESC, (String)map.get(IConstants.LOANDET_ITEMDESC));
            htdodet.put(IConstants.TRAN_DATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
            htdodet.put(IConstants.LOANDET_QTYOR, (String)map.get(IConstants.LOANDET_QTYOR));
            htdodet.put(IConstants.UNITMO, (String)map.get(IConstants.UNITMO));            
            htdodet.put(IConstants.LOANDET_LNSTATUS, "N");
                flag = loUtil.saveLoanDetDetails(htdodet);
                
  				LoanDetDAO _LoanDetDAO=new LoanDetDAO();
                Hashtable htRemarksDel = new Hashtable();
                htRemarksDel.put(IDBConstants.PLANT,(String)map.get(IConstants.PLANT));
                htRemarksDel.put(IDBConstants.LOANDET_ORDNO,  (String)map.get(IConstants.LOANDET_ORDNO));
                htRemarksDel.put(IDBConstants.LOANDET_ORDLNNO,  (String)map.get(IConstants.LOANDET_ORDLNNO));
 				if(_LoanDetDAO.isExisitLoanMultiRemarks(htRemarksDel))
                {
                   flag = _LoanDetDAO.deleteLoanMultiRemarks(htRemarksDel);
                }
 				
 				Hashtable htRemarks = new Hashtable();
				htRemarks.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htRemarks.put(IDBConstants.LOANDET_ORDNO, (String)map.get(IConstants.LOANDET_ORDNO));
				htRemarks.put(IDBConstants.LOANDET_ORDLNNO, (String)map.get(IConstants.LOANDET_ORDLNNO));
				htRemarks.put(IDBConstants.LOANDET_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
				htRemarks.put(IDBConstants.REMARKS,"");
				htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htRemarks.put(IDBConstants.CREATED_BY,(String)map.get("LOGIN_USER"));
				flag = loUtil.saveLoanMultiRemarks(htRemarks);
         
            

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
            htdohdr.put(IConstants.LOANHDR_ORDNO, (String)map.get(IConstants.LOANHDR_ORDNO));
           // htdohdr.put(IConstants.LOANDET_LNSTATUS, "N");
            htdohdr.put(IConstants.ORDERTYPE, (String)map.get(IConstants.ORDERTYPE));
            htdohdr.put(IConstants.LOANHDR_COL_DATE, (String)map.get(IConstants.LOANHDR_COL_DATE));
            htdohdr.put(IConstants.LOANHDR_COL_TIME, (String)map.get(IConstants.LOANHDR_COL_TIME));
            htdohdr.put(IConstants.LOANHDR_REMARK1, (String)map.get(IConstants.LOANHDR_REMARK1));
            if (!map.get(IConstants.LOANHDR_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.LOANHDR_REMARK1).toString()!= null){
				htMaster.clear();
				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.LOANHDR_REMARK1));
			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
				if(!map.get(IConstants.LOANHDR_REMARK1).toString().equalsIgnoreCase("")){
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
					htRecvHis.put("REMARKS", (String)map.get(IConstants.LOANHDR_REMARK1));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
					htRecvHis.put(IDBConstants.TRAN_DATE,
					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					flag = movHisDao.insertIntoMovHis(htRecvHis);
				}
			 }
			}
           // htdohdr.put(IConstants.OUT_REMARK2, (String)map.get(IConstants.OUT_REMARK2));
            htdohdr.put(IConstants.LOANHDR_REMARK2, (String)map.get(IConstants.LOANHDR_REMARK2));
            if (!map.get(IConstants.LOANHDR_REMARK2).toString().equalsIgnoreCase("null") && map.get(IConstants.LOANHDR_REMARK2).toString()!= null){
				htMaster.clear();
				htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.LOANHDR_REMARK2));
			if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
				if(!map.get(IConstants.LOANHDR_REMARK2).toString().equalsIgnoreCase("")){
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
					htRecvHis.put("REMARKS", (String)map.get(IConstants.LOANHDR_REMARK2));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htRecvHis.put(IDBConstants.CREATED_BY,  (String)map.get("LOGIN_USER"));
					htRecvHis.put(IDBConstants.TRAN_DATE,
					DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					flag = movHisDao.insertIntoMovHis(htRecvHis);
				}
		    	}
			}
          
            htdohdr.put(IConstants.LOANHDR_GST, (String)map.get(IConstants.LOANHDR_GST));
            htdohdr.put(IConstants.LOANHDR_CUST_CODE, (String)map.get(IConstants.LOANHDR_CUST_CODE));
            htdohdr.put(IConstants.LOANHDR_CUST_NAME, (String)map.get(IConstants.LOANHDR_CUST_NAME));
            htdohdr.put(IConstants.LOANHDR_PERSON_INCHARGE, (String)map.get(IConstants.LOANHDR_PERSON_INCHARGE));
            htdohdr.put(IConstants.LOANHDR_JOB_NUM,(String)map.get(IConstants.LOANHDR_JOB_NUM));
            htdohdr.put(IConstants.CURRENCYID,(String)map.get(IConstants.CURRENCYID));
            htdohdr.put(IConstants.LOANHDR_EMPNO, (String)map.get(IConstants.LOANHDR_EMPNO));
            htdohdr.put(IConstants.DELIVERYDATE, (String)map.get(IConstants.DELIVERYDATE));
            htdohdr.put(IConstants.SHIPPINGCOST, (String)map.get(IConstants.SHIPPINGCOST));
            htdohdr.put(IConstants.ORDERDISCOUNT, (String)map.get(IConstants.ORDERDISCOUNT));
			htdohdr.put(IConstants.PAYMENTTYPE, (String)map.get(IConstants.PAYMENTTYPE));
			htdohdr.put(IConstants.LOC, (String)map.get(IConstants.LOC));
			htdohdr.put(IConstants.LOC1, (String)map.get(IConstants.LOC1));
			htdohdr.put(IConstants.LOANHDR_DELIVERYDATEFORMAT, (String)map.get(IConstants.LOANHDR_DELIVERYDATEFORMAT));
			htdohdr.put(IConstants.LOANHDR_EXPIRYDATEFORMAT, (String)map.get(IConstants.LOANHDR_EXPIRYDATEFORMAT));
			htdohdr.put("STATUS", "N");          
            htdohdr.put(IConstants.SHIPPINGCUSTOMER, (String)map.get(IConstants.SHIPPINGCUSTOMER));
            htdohdr.put("DAYS", "");
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
           flag = loUtil.saveLoanHdrDetails(htdohdr);
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
             
            LoanHdrDAO _LoanHdrDAO =new LoanHdrDAO();
            _LoanHdrDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.LOANHDR_ORDNO, (String)map.get(IConstants.LOANHDR_ORDNO));
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.LOANHDR_JOB_NUM + " = '" + (String)map.get(IConstants.LOANHDR_JOB_NUM) + "'");
           
            updateQyery.append("," + IConstants.LOANHDR_COL_DATE + " = '"+ (String)map.get(IConstants.LOANHDR_COL_DATE) + "'");
            updateQyery.append("," + IConstants.LOANHDR_COL_TIME + " = '"+ (String)map.get(IConstants.LOANHDR_COL_TIME) + "'");
            updateQyery.append("," + IConstants.LOANHDR_REMARK1 + " = '"+ (String)map.get(IConstants.LOANHDR_REMARK1) + "'");
            updateQyery.append("," + IConstants.LOANHDR_REMARK2 + " = '"+ (String)map.get(IConstants.LOANHDR_REMARK2) + "'");
           // updateQyery.append("," + IConstants.OUT_REMARK3 + " = '"+ (String)map.get(IConstants.OUT_REMARK3) + "'");
            updateQyery.append("," + IConstants.LOANHDR_GST + " = '"+ (String)map.get(IConstants.LOANHDR_GST) + "'");
            updateQyery.append(","+ IConstants.LOANHDR_CUST_CODE + " = '" + (String)map.get(IConstants.LOANHDR_CUST_CODE) + "'");
            updateQyery.append("," +IConstants.LOANHDR_CUST_NAME + " = '" + (String)map.get(IConstants.LOANHDR_CUST_NAME) + "'");
            updateQyery.append("," + IConstants.LOANHDR_PERSON_INCHARGE + " = '"+ (String)map.get(IConstants.LOANHDR_PERSON_INCHARGE) + "'");
            updateQyery.append("," + IConstants.LOANHDR_EMPNO + " = '"+ (String)map.get(IConstants.LOANHDR_EMPNO) + "'");
            updateQyery.append("," + IConstants.DELIVERYDATE + " = '"+ (String)map.get(IConstants.DELIVERYDATE) + "'");
            updateQyery.append("," +IConstants.ORDERDISCOUNT + " = '" + (String)map.get(IConstants.ORDERDISCOUNT) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCOST + " = '"+ (String)map.get(IConstants.SHIPPINGCOST) + "'");
            //updateQyery.append("," + IConstants.INCOTERMS + " = '"+ (String)map.get(IConstants.INCOTERMS) + "'");
            updateQyery.append("," + IConstants.SHIPPINGCUSTOMER + " = '"+ (String)map.get(IConstants.SHIPPINGCUSTOMER) + "'");
            updateQyery.append("," + IConstants.PAYMENTTYPE + " = '"+ (String)map.get(IConstants.PAYMENTTYPE) + "'");
          //  flag = new DoTransferHdrDAO().update(updateQyery.toString(), htCond, " ");
            updateQyery.append("," + IConstants.CURRENCYID + " = '"+ (String)map.get(IConstants.CURRENCYID) + "'");
            updateQyery.append("," + IConstants.ORDERTYPE + " = '" +  (String)map.get(IConstants.ORDERTYPE) + "'");
            updateQyery.append("," + IConstants.LOC + " = '" +  (String)map.get(IConstants.LOC) + "'");
            updateQyery.append("," + IConstants.LOC1 + " = '" +  (String)map.get(IConstants.LOC1) + "'");
            updateQyery.append("," + IConstants.LOANHDR_DELIVERYDATEFORMAT + " = '" +  (String)map.get(IConstants.LOANHDR_DELIVERYDATEFORMAT) + "'");
            updateQyery.append("," + IConstants.LOANHDR_EXPIRYDATEFORMAT + " = '" +  (String)map.get(IConstants.LOANHDR_EXPIRYDATEFORMAT) + "'");
            flag = _LoanHdrDAO.update(updateQyery.toString(), htCond, "  ");
            /*if(flag){
                updateQyery.append("," + IConstants.OUT_CURRENCYID + " = '"+ (String)map.get(IConstants.OUT_CURRENCYID) + "'");
                updateQyery.append("," + IConstants.OUT_ORDERTYPE + " = '" +  (String)map.get(IConstants.OUT_ORDERTYPE) + "'");
                flag = _DoHdrDAO.update(updateQyery.toString(), htCond, "  ");
            }*/
            
//            if(flag)///insert remarks,incoterm and shipping customer into master
            { 
      	  
            	  if (!map.get(IConstants.LOANHDR_REMARK1).toString().equalsIgnoreCase("null") && map.get(IConstants.LOANHDR_REMARK1).toString()!= null){
  					htMaster.clear();
  					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.LOANHDR_REMARK1));
  				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
  					if(!map.get(IConstants.LOANHDR_REMARK1).toString().equalsIgnoreCase("")){
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
                
                  if (!map.get(IConstants.LOANHDR_REMARK2).toString().equalsIgnoreCase("null") && map.get(IConstants.LOANHDR_REMARK2).toString()!= null){
  					htMaster.clear();
  					htMaster.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
  					htMaster.put(IDBConstants.REMARKS, (String)map.get(IConstants.LOANHDR_REMARK2));
  				if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
  					if(!map.get(IConstants.LOANHDR_REMARK2).toString().equalsIgnoreCase("")){
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
                  
                /*  if (!map.get(IConstants.INCOTERMS).toString().equalsIgnoreCase("null") && map.get(IConstants.INCOTERMS).toString()!= null){
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
    				}*/
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
                throw new Exception("Loan Order "+(String)map.get(IConstants.LOANHDR_ORDNO)+" is processed,Cannot Modify");
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
            htdodet.put(IConstants.LOANDET_ORDNO, (String)map.get(IConstants.LOANDET_ORDNO));
            htdodet.put(IConstants.LOANDET_ORDLNNO, (String)map.get(IConstants.LOANDET_ORDLNNO));
            LoanUtil loUtil = new LoanUtil();
            boolean isLineExist = loUtil.isExitsDoLine(htdodet);
            if(isLineExist){
             /*   DoDetDAO _dodetDAO = new DoDetDAO();
               _dodetDAO.setmLogger(mLogger);*/
            	LoanDetDAO _loanDetDAO = new LoanDetDAO();
            	_loanDetDAO.setmLogger(mLogger);
               flag  =_loanDetDAO.delete(htdodet);
               /*if(flag){
                   flag  =new DoTransferDetDAO().delete(htdodet);
               }*/
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
             
           /* DoDetDAO _DoDetDAO =new DoDetDAO();
            _DoDetDAO.setmLogger(mLogger);*/
        	LoanDetDAO _loanDetDAO = new LoanDetDAO();
        	_loanDetDAO.setmLogger(mLogger);
            Hashtable htCond = new Hashtable();
            htCond.put(IDBConstants.PLANT, (String) map.get(IConstants.PLANT));
            htCond.put(IConstants.LOANDET_ORDNO, (String)map.get(IConstants.LOANDET_ORDNO));
            htCond.put(IConstants.LOANDET_ORDLNNO, (String)map.get(IConstants.LOANDET_ORDLNNO)); 
            htCond.put(IConstants.LOANDET_ITEM, (String)map.get(IConstants.LOANDET_ITEM));
            
           // String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT((String)map.get(IConstants.PLANT), (String)map.get(IConstants.LOANDET_ORDNO));
       
            
            StringBuffer updateQyery = new StringBuffer("set ");
            updateQyery.append(IConstants.LOANDET_RENTALPRICE + " = '" + (String)map.get(IConstants.LOANDET_RENTALPRICE) + "'");
            //updateQyery.append("," + IDBConstants.CURRENCYUSEQT + " = '"+ CURRENCYUSEQT + "'");
            updateQyery.append("," + IDBConstants.CURRENCYUSEQT + " = '"+ (String)map.get(IDBConstants.CURRENCYUSEQT) + "'");
        
           
            
            flag = _loanDetDAO.update(updateQyery.toString(), htCond, " ");
            if(!flag){
               throw new Exception("Line No:"+(String)map.get(IConstants.LOANDET_ORDLNNO)+" , Item : "+(String)map.get(IConstants.LOANDET_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.OUT_DONO) );
            }
            
         
        } catch (Exception e) {
        	throw new Exception("Line No:"+(String)map.get(IConstants.LOANDET_ORDLNNO)+" , Item : "+(String)map.get(IConstants.LOANDET_ITEM)+ " is not valid record for this Order:" +(String)map.get(IConstants.OUT_DONO) );
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
            htCond.put(IConstants.LOANDET_ORDNO, (String)map.get(IConstants.LOANDET_ORDNO));
            htCond.put("DOLNO", (String)map.get(IConstants.LOANDET_ORDLNNO)); 
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
