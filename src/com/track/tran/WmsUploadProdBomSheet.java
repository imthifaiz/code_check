
package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.ProductionBomUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadProdBomSheet implements WmsTran, IMLogger {
	
	DateUtils dateUtils = null;
	
        ProductionBomUtil productionbomUtil =null;
        
	ItemSesBeanDAO itemsesdao = null;
      
	public WmsUploadProdBomSheet() {
		
		dateUtils = new DateUtils();
		productionbomUtil=new ProductionBomUtil();
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
		MLogger.log(0, "2.insertCustomersheet -  Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if (flag == true)
			flag = processMovHis(m);

		return flag;
	}

    @SuppressWarnings("unchecked")
    public boolean processCountSheet(Map map) throws Exception {
            MLogger.log(1, this.getClass() + " processCountSheet()");
            boolean flag = false;

            try {
                    this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
               
            
            boolean result = false;
                Hashtable htpb = new Hashtable();
                Hashtable htcdn = new Hashtable();
                htpb.clear();
            
               
                htpb.put("PLANT", (String)map.get("PLANT"));
                htpb.put(IDBConstants.PARENTITEM, (String) map.get(IDBConstants.PARENTITEM));
                htpb.put(IDBConstants.CHILDITEM, (String) map.get(IDBConstants.CHILDITEM));
                htpb.put(IDBConstants.QTY, (String) map.get(IDBConstants.QTY));
                htpb.put("SEQNUM", (String) map.get(IConstants.OPRSEQNUM));
                htpb.put(IDBConstants.ITEMMST_REMARK1, (String) map.get(IDBConstants.ITEMMST_REMARK1));
                htpb.put(IDBConstants.ITEMMST_REMARK2, (String) map.get(IDBConstants.ITEMMST_REMARK2));
                htpb.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
                htpb.put(IDBConstants.LOGIN_USER,map.get("LOGIN_USER"));
                htpb.put("BOMTYPE","PROD");
    		
                htcdn.put(IDBConstants.PLANT, map.get("PLANT"));
                htcdn.put(IDBConstants.PARENTITEM, (String) map.get(IDBConstants.PARENTITEM));
                htcdn.put(IDBConstants.CHILDITEM, (String) map.get(IDBConstants.CHILDITEM));
                htcdn.put("BOMTYPE","PROD");
                
                result=productionbomUtil.isExistsProdBom(htcdn);
                if(result==true)
                {
                	throw new Exception("Child Product already Created to Parent Product.Choose different Child Product.");
                }else{
                flag = productionbomUtil.insertProdBomMst(htpb);
                }


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
                    htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_PRODBOM_UPLOAD);
                    htmov.put("ORDNUM",map.get(IDBConstants.PARENTITEM));
                    htmov.put("ITEM",map.get(IDBConstants.CHILDITEM));
        			htmov.put(IDBConstants.MOVTID, "");
                    htmov.put(IDBConstants.RECID, "");
                    htmov.put("REMARKS",map.get(IConstants.OPRSEQNUM)+","+map.get(IDBConstants.ITEMMST_REMARK1)+","+map.get(IDBConstants.ITEMMST_REMARK2));
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
