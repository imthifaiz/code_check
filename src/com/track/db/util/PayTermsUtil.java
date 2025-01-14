package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.track.constants.MLoggerConstant;
import com.track.dao.PayTermsDAO;
import com.track.util.MLogger;

public class PayTermsUtil {
	private boolean printLog = MLoggerConstant.PayTermsUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private PayTermsDAO payTermsDao = new PayTermsDAO();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public PayTermsUtil() {
		
	}
	
	public List getPaymentTermsDetails(Hashtable ht) throws Exception {
		List paytermsList = new ArrayList();
		try {
			payTermsDao.setmLogger(mLogger);
			paytermsList = payTermsDao.selectRow(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return paytermsList;
	}
	
	public boolean addpaymentTerms(Hashtable ht, String plant){
		boolean flag = false;		
		try {
			flag = payTermsDao.addpaymentTerms(ht, plant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	

	public boolean isExistPaytype(String paytype, String plant) {
		boolean exists = false;
		PayTermsDAO payTermsDao = new PayTermsDAO();
		try {
			payTermsDao.setmLogger(mLogger);
			exists = payTermsDao.isExistPaytype(paytype, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean isExistPayterms(String payterms, String plant) {
		boolean exists = false;
		PayTermsDAO payTermsDao = new PayTermsDAO();
		try {
			payTermsDao.setmLogger(mLogger);
			exists = payTermsDao.isExistPayterms(payterms, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	
	
}
