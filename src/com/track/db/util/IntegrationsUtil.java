package com.track.db.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.IntegrationsDAO;
import com.track.util.MLogger;

public class IntegrationsUtil {
	private boolean printLog = MLoggerConstant.INTEGRATIONSUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public boolean insertShopifyConfig(Hashtable ht) {
		boolean inserted = false;
		IntegrationsDAO integrationsDAO = new IntegrationsDAO();
		try {
			integrationsDAO.setmLogger(mLogger);
			inserted = integrationsDAO.insertShopifyConfig(ht);
		} catch (Exception e) {
		}
		return inserted;
	}
	
	public Map getShopifyConfigDetail(String plant) {
		Map map = new HashMap();
		IntegrationsDAO integrationsDAO = new IntegrationsDAO();
		try {
			map = integrationsDAO.getShopifyConfigDetail(plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return map;
	}	
	
	public boolean insertApiConfig(Hashtable ht, String tableName) {
		boolean inserted = false;
		IntegrationsDAO integrationsDAO = new IntegrationsDAO();
		try {
			integrationsDAO.setmLogger(mLogger);
			inserted = integrationsDAO.insertData(ht, tableName);
		} catch (Exception e) {
		}
		return inserted;
	}
	
	public Map<String, String> getShopeeConfigDetail(String plant) {
		Map<String, String> map = new HashMap<String, String>();
		IntegrationsDAO integrationsDAO = new IntegrationsDAO();
		try {
			map = integrationsDAO.getShopeeConfigDetail(plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return map;
	}
	
}
