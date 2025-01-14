package com.track.tran;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.db.util.DOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.PlantMstDAO;

import com.track.dao.MovHisDAO;

public class WmsSignatureCapture  implements WmsTran, IMLogger {
	
	private boolean printLog = MLoggerConstant.WmsProcessSignatureCapture_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsProcessSignatureCapture_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        DOUtil doutil = new DOUtil();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsSignatureCapture() {
		dateUtils = new DateUtils();
	}
	
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;

		if (flag) {

			flag = processSignatureCapture(m);

		}
		
		if (flag) {
			flag = processMovHis(m);
		}

		
		return flag;
	}
	public boolean processSignatureCapture(Map map) throws Exception {
	boolean flag = false;
	try {		
		PlantMstDAO _PlantMstDAO=new PlantMstDAO();
		DateUtils dateutils = new DateUtils();
		Hashtable htSign  = new Hashtable();
		htSign.put(IConstants.PLANT,  map.get(IConstants.PLANT));
		htSign.put(IConstants.DONO,  map.get(IConstants.DONO));
		htSign.put("SIGNPATH", map.get("SIGNPATH"));
		htSign.put("SIGNERNAME", map.get("SIGNERNAME"));
		//htSign.put("SIGNBINARY", BitConverter.ToString(map.get("SIGNBINARY")));
		htSign.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		htSign.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		flag = _PlantMstDAO.insertSignatureCapture(htSign);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
  }
	
	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DONO));
			htRecvHis.put("DIRTYPE", "SIGNATURE_CAPTURE");
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get("SIGNERNAME")));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
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
