package com.track.db.util;

import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.transaction.UserTransaction;

import com.track.gates.DbBean;
import com.track.tran.WmsDeKitting;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class DeKittingUtil {
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public Boolean doDeKitting(Hashtable<String, String> requestData)
	throws Exception {
		Boolean result = Boolean.valueOf(false);
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			WmsTran tran = new WmsDeKitting();
			((IMLogger) tran).setMapDataToLogger(this.mLogger
					.getLoggerConstans());
			result = tran.processWmsTran(requestData);
			if (result == true) {
				DbBean.CommitTran(ut);
				result = true;
			} else {
				DbBean.RollbackTran(ut);
				result = false;
			}
		
		} catch (Exception e) {
			result = false;
			DbBean.RollbackTran(ut);
			throw e;
		}
		return result;
}


}
