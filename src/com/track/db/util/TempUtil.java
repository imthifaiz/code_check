package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.dao.TempDAO;
import com.track.util.MLogger;

public class TempUtil {
	
	TempDAO _TempDAO = null;
	
	private boolean printLog = MLoggerConstant.TempUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public TempUtil() {
		_TempDAO = new TempDAO();
		
		_TempDAO.setmLogger(mLogger);
		
	}
	
	
	public boolean isExitstempproduct(Hashtable ht)
	 throws Exception {

	boolean flag = false;
	try {

	 
	 
		_TempDAO.setmLogger(mLogger);
	 
	 flag = _TempDAO.isExisittemp(ht);

	} catch (Exception e) {
	 this.mLogger.exception(this.printLog, "", e);
	 throw e;
	}

	return flag;
	}
	 
	 public boolean updatetempproduct(double qty,Hashtable ht)
	 throws Exception {

	boolean flag = false;
	try {
		
		_TempDAO.setmLogger(mLogger);
	 
	 double QTY = qty;
	 
	 String query = "set QTY=QTY+" + QTY;
	 
	 flag = _TempDAO.updatetemptable(query, ht, "");

	} catch (Exception e) {
	 this.mLogger.exception(this.printLog, "", e);
	 throw e;
	}

	return flag;
	}
	 
	 public boolean savetemp(Hashtable ht) throws Exception {
			boolean flag = false;

			try {
				
				_TempDAO.setmLogger(mLogger);
				 
				flag = _TempDAO.inserttemp(ht);
			} catch (Exception e) {
				MLogger.log(-1, "saveTempDetails() ::  Exception ############### "
						+ e.getMessage());
				throw e;
			}
			return flag;
		}
	 
	 public ArrayList listtempdata(String query,Hashtable ht,String extCond)
	 throws Exception {
	 	
	 	ArrayList al = null;
	 	try {
	 		
	 		_TempDAO.setmLogger(mLogger);
			 
	 		al =  _TempDAO.selecttemp(query,ht,extCond);
	 		} catch (Exception e) {
	 this.mLogger.exception(this.printLog, "", e);
	 throw e;
	 	}

	 	return al;
	}
	 
	public boolean deletetemp(Hashtable ht) throws Exception {
			boolean flag = false;

			try {
				
				_TempDAO.setmLogger(mLogger);
				 
				flag = _TempDAO.deletetemp(ht);
			} catch (Exception e) {
				MLogger.log(-1, "Delete temptable ::  Exception ############### "
						+ e.getMessage());
				throw e;
			}
			return flag;
		}
	 

}
