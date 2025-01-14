package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;
import com.track.constants.MLoggerConstant;
import com.track.dao.BannerDAO;
import com.track.dao.CatalogDAO;
import com.track.gates.DbBean;
import com.track.tran.WmsMobileEnquiry;
import com.track.tran.WmsMobileRegistration;
import com.track.tran.WmsMobileShopping;
import com.track.tran.WmsTran;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class BannerUtil {
	private boolean printLog = MLoggerConstant.LoanUtil_PRINTPLANTMASTERLOG;
	private BannerDAO _bannerdao;  
		private MLogger mLogger = new MLogger();

		public MLogger getmLogger() {
			return mLogger;
		}

		public void setmLogger(MLogger mLogger) {
			this.mLogger = mLogger;
		}
public BannerUtil()
{
	_bannerdao = new BannerDAO();
}
		

	
		public boolean insertMst(Hashtable ht) {
			boolean inserted = false;
			try {
				_bannerdao.setmLogger(mLogger);
				inserted = _bannerdao.saveMst(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return inserted;
		}	
		public boolean updateMst(Hashtable htupdate,Hashtable htcondtn) {
			boolean updated = false;
			try {
				_bannerdao.setmLogger(mLogger);
				updated = _bannerdao.updateMst(htupdate, htcondtn);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return updated;
		}	


	public boolean delMst(Hashtable<String, String> htcondtn) {
		boolean deleted = false;
		try {
			_bannerdao.setmLogger(mLogger);
			deleted = _bannerdao.delMst(htcondtn);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return deleted;
	}

}
