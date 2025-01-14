
package com.track.tran;


import com.track.constants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.LocTypeUtil;

import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadCountSheet implements WmsTran,IMLogger {
	
	DateUtils dateUtils = null;
	LocMstDAO locdao = null;
	LocTypeUtil locTypeUtil = null;
	public WmsUploadCountSheet() {
		locdao = new LocMstDAO();
		locTypeUtil = new LocTypeUtil();
		dateUtils = new DateUtils();
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
	

		MLogger.log(0,"2.insert LocMSt - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if(flag== true)
			flag=processMovHis(m);

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
			locdao.setmLogger(mLogger);
		
		boolean result = false;
      Hashtable htcs = new Hashtable();
      Hashtable htcdn = new Hashtable();
      
      
      
      htcs.clear();     
      htcs.put(IDBConstants.PLANT, map.get("PLANT"));
      htcs.put(IConstants.LOC, map.get(IConstants.LOC));
      htcs.put("WHID", "");
      htcs.put(IDBConstants.LOCDESC, map.get("LOCDESC"));
      htcs.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
      htcs.put("USERFLD1", map.get("REMARKS"));
      htcs.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
      htcs.put(IConstants.ISACTIVE, map.get("ISACTIVE"));
      htcs.put(IDBConstants.ADD1, map.get("ADD1"));
      htcs.put(IDBConstants.ADD2, map.get("ADD2"));
      htcs.put(IDBConstants.ADD3, map.get("ADD3"));
      htcs.put(IDBConstants.ADD4, map.get("ADD4"));
      htcs.put(IDBConstants.STATE, map.get("STATE"));
      htcs.put(IDBConstants.COUNTRY, map.get("COUNTRY"));
      htcs.put(IDBConstants.ZIP, map.get("ZIP"));
      htcs.put(IDBConstants.TELNO, map.get("TELNO"));
      htcs.put(IConstants.FAX, map.get("FAX"));
      htcs.put("CHKSTATUS", "Y");
      htcs.put("COMNAME", map.get("COMNAME"));
      htcs.put(IDBConstants.RCBNO, map.get("RCBNO"));
      htcs.put(IDBConstants.LOCTYPEID, map.get("LOC_TYPE_ID"));
      htcs.put(IDBConstants.LOCTYPEID2, map.get("LOC_TYPE_ID2"));
      htcs.put(IDBConstants.LOCTYPEID3, map.get("LOC_TYPE_ID3"));
      
      Hashtable htlocType = new Hashtable();
	  htlocType.put(IDBConstants.PLANT, map.get("PLANT"));
	  htlocType.put(IDBConstants.LOCTYPEID,  map.get("LOC_TYPE_ID"));
      result = locTypeUtil.isExistsLocType(htlocType);
      if (result == false) {
    	  
    	  htlocType.put("LOC_TYPE_DESC",  map.get("LOC_TYPE_ID"));
    	  htlocType.put(IConstants.ISACTIVE, "Y");
    	  htlocType.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
    	  htlocType.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			boolean locTypeInserted = locTypeUtil.insertLocTypeMst(htlocType);
		}
      Hashtable htlocType2 = new Hashtable();
	  htlocType2.put(IDBConstants.PLANT, map.get("PLANT"));
	  htlocType2.put(IDBConstants.LOCTYPEID,  map.get("LOC_TYPE_ID2"));
	  result = locTypeUtil.isExistsLocType(htlocType2);
      if (result == false) {
    	  
    	  htlocType2.put("LOC_TYPE_DESC",  map.get("LOC_TYPE_ID2"));
    	  htlocType2.put(IConstants.ISACTIVE, "Y");
    	  htlocType2.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
    	  htlocType.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			boolean locTypeInserted = locTypeUtil.insertLocTypeMst(htlocType2);
		}
      Hashtable htlocType3 = new Hashtable();
	  htlocType3.put(IDBConstants.PLANT, map.get("PLANT"));
	  htlocType3.put(IDBConstants.LOCTYPEID,  map.get("LOC_TYPE_ID3"));
	  result = locTypeUtil.isExistsLocType(htlocType3);
      if (result == false) {
    	  
    	  htlocType3.put("LOC_TYPE_DESC",  map.get("LOC_TYPE_ID3"));
    	  htlocType3.put(IConstants.ISACTIVE, "Y");
    	  htlocType3.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
    	  htlocType.put(IDBConstants.LOGIN_USER, map.get("LOGIN_USER"));
			boolean locTypeInserted = locTypeUtil.insertLocTypeMst(htlocType3);
		}
      
      htcdn.put(IDBConstants.PLANT, map.get("PLANT"));
      htcdn.put(IConstants.LOC, map.get(IConstants.LOC));
      result=locdao.isExisit(htcdn, "");
      if(result==true)
      {
    	  StringBuffer sql = new StringBuffer(" ");

    	  sql.append(" set LOCDESC="+"'"+map.get("LOCDESC")+"'");
    	  sql.append(",");
    	  sql.append("USERFLD1="+"'"+map.get("REMARKS")+"'");
    	  sql.append(",");
    	  sql.append("isActive="+"'"+map.get("ISACTIVE")+"'");
    	  sql.append(",");
    	  sql.append("UPAT="+"'"+ DateUtils.getDateTime() +"'");
    	  sql.append(",");
    	  sql.append("UPBY="+"'"+ map.get("LOGIN_USER") +"'");
    	  sql.append(",");
    	  sql.append("ADD1="+"'"+map.get("ADD1")+"'");
    	  sql.append(",");
    	  sql.append("ADD2="+"'"+map.get("ADD2")+"'");
    	  sql.append(",");
    	  sql.append("ADD3="+"'"+ map.get("ADD3")+"'");
    	  sql.append(",");
    	  sql.append("ADD4="+"'"+ map.get("ADD4") +"'");
    	  sql.append(",");
    	  sql.append("STATE="+"'"+map.get("STATE")+"'");
    	  sql.append(",");
    	  sql.append("COUNTRY="+"'"+map.get("COUNTRY")+"'");
    	  sql.append(",");
    	  sql.append("ZIP="+"'"+map.get("ZIP")+"'");
    	  sql.append(",");
    	  sql.append("TELNO="+"'"+map.get("TELNO")+"'");
    	  sql.append(",");
    	  sql.append("FAX="+"'"+ map.get("FAX") +"'");
    	  sql.append(",");
    	  sql.append("COMNAME="+"'"+map.get("COMNAME")+"'");
    	  sql.append(",");
    	  sql.append("RCBNO="+"'"+ map.get("RCBNO") +"'");
    	  sql.append(",");
    	  sql.append("LOC_TYPE_ID="+"'"+ map.get("LOC_TYPE_ID") +"'");
    	  sql.append(",");
    	  sql.append("LOC_TYPE_ID2="+"'"+ map.get("LOC_TYPE_ID2") +"'");
    	  sql.append(",");
    	  sql.append("LOC_TYPE_ID3="+"'"+ map.get("LOC_TYPE_ID3") +"'");
    	
 	  
    	  flag = locdao.update(sql.toString(),htcdn, "");
    	  
      }else{
     flag = locdao.insertLocMst(htcs);}


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
			htmov.put(IDBConstants.DIRTYPE, TransactionConstants.CNT_LOC_UPLOAD);
			htmov.put(IDBConstants.LOC, map.get("LOC"));
			htmov.put(IDBConstants.MOVTID, "");
			htmov.put(IDBConstants.RECID, "");
			htmov.put(IDBConstants.PONO, "");
			htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			htmov.put(IDBConstants.TRAN_DATE,  dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		    htmov.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
		  

			flag = movHisDao.insertIntoMovHis(htmov);

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

}