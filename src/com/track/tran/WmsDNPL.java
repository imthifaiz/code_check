package com.track.tran;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.DNPLDetDAO;
import com.track.dao.DNPLHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.TempDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsDNPL implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsDNPL_PRINTPLANTMASTERLOG ;
	
	DateUtils dateUtils = null;
    StrUtils su = new StrUtils();
    DOUtil doutil = new DOUtil();
    TempUtil _TempUtil = new  TempUtil();
    TempDAO _TempDAO = new TempDAO();
private MLogger mLogger = new MLogger();

public MLogger getmLogger() {
	return mLogger;
}

public void setmLogger(MLogger mLogger) {
	this.mLogger = mLogger;
}

public WmsDNPL() {
	dateUtils = new DateUtils();
}
public boolean processWmsTran(Map m) throws Exception {
	boolean flag = false;
	try {
			
         String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
			if(!nonstocktype.equalsIgnoreCase("Y"))	
		   
				flag = processDNPLDet(m);
			
			if (flag) {
				flag = processMovHis(m);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					MLogger.log(0, "Exception Create Delivery Note/Packing List"
							+ e.getMessage());
				}
				
				
		}
				
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	}
	return flag;
}

public boolean processDNPLDet(Map map) throws Exception {
	
//    String ExpiryDate="";
	boolean flag = false;
//	boolean flag1 = false;
//	boolean dotransferdetflag=false;
//	boolean dotransferhdrflag=false;
	try {
		DNPLDetDAO _DNPLDetDAO = new DNPLDetDAO();
		_DNPLDetDAO.setmLogger(mLogger);
		DNPLHdrDAO _DNPLHdrDAO = new DNPLHdrDAO();
		_DNPLHdrDAO.setmLogger(mLogger);
		DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
		DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
		_DoTransferDetDAO.setmLogger(mLogger);
		_DoTransferHdrDAO.setmLogger(mLogger);
//		String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));  
		Hashtable<String, Object> htCondiPoDet = new Hashtable<>();
//		StringBuffer query = new StringBuffer("");
		htCondiPoDet.put("PLANT", map.get("PLANT"));
		htCondiPoDet.put("dono", map.get(IConstants.DNPLDET_DONUM));
		htCondiPoDet.put("dolnno", map.get(IConstants.DNPLDET_DOLNNO));
		String queryPoDet = "";
//		String queryPoHdr = "";
		String extraCond = " ";
		queryPoDet = "set netweight= '"+ map.get(IConstants.NETWEIGHT) +"',grossweight= '"+ map.get(IConstants.GROSSWEIGHT) +"', dimension= '"+ map.get(IConstants.DIMENSION) +"',packing= '"+ map.get(IConstants.PACKING) +"'";
    	flag = _DNPLDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
		
				
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw new Exception("Failed to Create Delivery Note/Packing List!");
	}
	return flag;
}

public boolean processMovHis(Map map) throws Exception {
	boolean flag = false;
	MovHisDAO movHisDao = new MovHisDAO();
	CustMstDAO _CustMstDAO = new CustMstDAO();
	movHisDao.setmLogger(mLogger);
	_CustMstDAO.setmLogger(mLogger);
	try {
		Hashtable<String, Object> htMovHis = new Hashtable<>();
		htMovHis.clear();
		htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htMovHis.put("DIRTYPE", TransactionConstants.CREATEDNPL);
		htMovHis.put(IDBConstants.ITEM, "");
		htMovHis.put("BATNO", "");
		//htMovHis.put(IDBConstants.QTY, '0');
		htMovHis.put(IDBConstants.POHDR_JOB_NUM, "");
		htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DNPLDET_DONUM) + "_" + map.get("INVOICENO"));
		htMovHis.put("MOVTID", "");
		htMovHis.put("RECID", "");
		htMovHis.put(IDBConstants.LOC, "");
		htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		htMovHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htMovHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS)));
		
		flag = movHisDao.insertIntoMovHis(htMovHis);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}

public HashMap<String, String> populateMapData(String companyCode,
		String userCode) {
	// TODO Auto-generated method stub
	return null;
}

public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
	this.mLogger.setLoggerConstans(dataForLogging);
}

}
