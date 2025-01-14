package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.db.util.ItemUtil;

public class WmsUploadOutboundProdRemarksSheet implements WmsTran, IMLogger{
	DateUtils dateUtils = null;
	DoDetDAO dodetdao=null;
	ItemUtil itemUtil = null;
	
public WmsUploadOutboundProdRemarksSheet()  {
		
		dateUtils = new DateUtils();
		dodetdao = new DoDetDAO();
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
	MLogger.log(0, "2.insertCustomerDiscountsheet -  Stage : 1");
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
        htcs.put(IConstants.OUT_DONO ,  (String) map.get(IConstants.OUT_DONO));
        htcs.put(IConstants.OUT_DOLNNO, (String) map.get(IConstants.OUT_DOLNNO));
        htcs.put(IConstants.OUT_ITEM, (String) map.get(IConstants.OUT_ITEM));
        htcs.put("REMARKS", (String) map.get("REMARKS"));
        htcs.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
        htcs.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
      
        flag = dodetdao.insertDoMultiRemarks(htcs);
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
	                htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_OUTBOUNDPRODREMARKS_UPLOAD);
	                htmov.put(IDBConstants.MOVTID, "");
	                htmov.put(IDBConstants.RECID, "");
	                htmov.put(IConstants.ORDNUM, (String)map.get(IConstants.OUT_DONO));
	                htmov.put(IConstants.OUT_ITEM, (String)map.get(IConstants.OUT_ITEM));
	                htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
	                htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
	                htmov.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
	                htmov.put("REMARKS", (String) map.get("REMARKS"));
	                
	                flag = movHisDao.insertIntoMovHis(htmov);
	
	        } catch (Exception e) {
	                throw e;
	        }
	        return flag;
	}
}
