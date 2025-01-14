package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.db.util.ItemUtil;

public class WmsUploadSupplierDiscountSheet implements WmsTran, IMLogger{
	DateUtils dateUtils = null;
	ItemMstDAO itemdao=null;
	ItemUtil itemUtil = null;
	
public WmsUploadSupplierDiscountSheet()  {
		dateUtils = new DateUtils();
		itemdao = new ItemMstDAO();
		itemUtil = new ItemUtil();
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
		MLogger.log(0, "2.insertSupplierDiscountsheet -  Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if (flag == true)
			flag = processMovHis(m);
		return flag;
	}
	public boolean processCountSheet(Map map) throws Exception {
        MLogger.log(1, this.getClass() + " processCountSheet()");
        boolean flag = false;
        try {
            Hashtable htcs = new Hashtable();
            htcs.clear();
            htcs.put("PLANT", (String)map.get("PLANT"));
            htcs.put(IDBConstants.ITEM ,  (String) map.get(IDBConstants.ITEM));
            htcs.put(IDBConstants.VENDORCODE, (String) map.get(IDBConstants.VENDORCODE));
            htcs.put(IDBConstants.IBDISCOUNT, (String) map.get(IDBConstants.IBDISCOUNT));
            htcs.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
            htcs.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
          //delete supplier discount inbound
            Hashtable ht = new Hashtable();
            ht.put("PLANT", (String)map.get("PLANT"));
            ht.put(IDBConstants.ITEM ,  (String) map.get(IDBConstants.ITEM));
            ht.put(IDBConstants.VENDORCODE, (String) map.get(IDBConstants.VENDORCODE));
            boolean isExist=itemdao.isExisitSupplierDiscount(ht, "");
            if(isExist)
            {
            	 boolean deleteCost=itemdao.removeSupplierDiscountIB((String)map.get("PLANT"),(String) map.get(IDBConstants.ITEM), " and VENDNO='" + (String) map.get(IDBConstants.VENDORCODE) +"'");
            }
           //end delete customer discount outbound
            flag = itemUtil.insertIBSupplierDiscount(htcs);
        } catch (Exception e) {
                MLogger.log(-1, "Exception :: " + e.getMessage());
                throw e;
        }
        MLogger.log(-1, this.getClass() + " processCountSheet()");
        return flag;
    }
		
		@SuppressWarnings("unchecked")
		public boolean processMovHis(Map map) throws Exception {
		        boolean flag = false;
		        MovHisDAO movHisDao = new MovHisDAO();
		        this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
		        movHisDao.setmLogger(mLogger);
		        try {
		                Hashtable htmov = new Hashtable();
		                htmov.clear();
		                htmov.put(IDBConstants.PLANT, map.get("PLANT"));
		                htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_SUPPLIERDISCOUNT_UPLOAD);
		                htmov.put(IDBConstants.MOVTID, "");
		                htmov.put(IDBConstants.RECID, "");
		                htmov.put(IDBConstants.PONO, "");
		                htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
		                htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		                htmov.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		                flag = movHisDao.insertIntoMovHis(htmov);
		
		        } catch (Exception e) {
		                throw e;
		        }
		        return flag;
		}
}
