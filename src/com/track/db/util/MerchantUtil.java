package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;


import com.track.dao.MerchantBeanDAO;
import com.track.util.MLogger;

public class MerchantUtil {
	private MLogger mLogger = new MLogger();
	private MerchantBeanDAO merchantBeanDAO = new MerchantBeanDAO();
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public MerchantUtil() {
	}
	public boolean insertMerchant(Hashtable ht) {
		boolean inserted = false;
		try {
			merchantBeanDAO.setmLogger(mLogger);
			inserted = merchantBeanDAO.insertIntoMerchant(ht);
		} catch (Exception e) {
		}
		return inserted;
	}
	public boolean isExistMerchant(String merchantCode,String plant) {
		boolean exists = false;
		try {
			merchantBeanDAO.setmLogger(mLogger);
			exists = merchantBeanDAO.isExistsMerchant(merchantCode,plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	public ArrayList getMerchantDetails(String merchno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			merchantBeanDAO.setmLogger(mLogger);
			arrList = merchantBeanDAO.getMerchantDetails(merchno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public boolean updateMerchant(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			merchantBeanDAO.setmLogger(mLogger);
			update = merchantBeanDAO.updateMerchant(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}
	
	public ArrayList getMerchantListStartsWithName(String aMerchName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			merchantBeanDAO.setmLogger(mLogger);
			arrList = merchantBeanDAO.getMerchantListStartsWithName(aMerchName,plant);
		} catch (Exception e) {
		}
		return arrList;
	}
}
