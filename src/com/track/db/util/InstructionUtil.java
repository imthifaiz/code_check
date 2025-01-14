package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.InstructionDAO;
import com.track.dao.PoHdrDAO;

import com.track.util.MLogger;

public class InstructionUtil {
	private boolean printLog = MLoggerConstant.LoanUtil_PRINTPLANTMASTERLOG;
	private InstructionDAO _instructdao;  
		private MLogger mLogger = new MLogger();

		public MLogger getmLogger() {
			return mLogger;
		}

		public void setmLogger(MLogger mLogger) {
			this.mLogger = mLogger;
		}
		public InstructionUtil()
		{
			_instructdao = new InstructionDAO();
		}
		
		public boolean insertMst(Hashtable ht) {
			boolean inserted = false;
			try {
				_instructdao.setmLogger(mLogger);
				inserted = _instructdao.saveMst(ht);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return inserted;
		}
		public boolean isExistIns(Hashtable ht,String ext) {
			boolean exist = false;
			try {
				_instructdao.setmLogger(mLogger);
				exist = _instructdao.isExisit(ht,ext);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return exist;
		}	
		public boolean updateMst(Hashtable htupdate,Hashtable htcondtn) {
			boolean updated = false;
			try {
				_instructdao.setmLogger(mLogger);
				updated = _instructdao.updateMst(htupdate, htcondtn);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return updated;
		}	
		public ArrayList listInstrns(String query,Hashtable ht,String extcondtn) throws Exception {
	        List lst =  new ArrayList();
	        _instructdao = new InstructionDAO();
	        lst=_instructdao.selectMst(query, ht, extcondtn);
	         return (ArrayList) lst;
	 }
		
}
