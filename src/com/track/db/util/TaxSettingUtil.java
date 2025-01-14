package com.track.db.util;

import java.util.Hashtable;

import com.track.constants.MLoggerConstant;
import com.track.dao.TaxSettingDAO;
import com.track.util.MLogger;

public class TaxSettingUtil {
	
	private boolean printLog = MLoggerConstant.TaxSettingUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	
	private TaxSettingDAO taxSettingDAO=new TaxSettingDAO();
	
	public int addTaxSettings(Hashtable ht, String plant,boolean isEdit,String taxid) {
		int taxSettingId = 0;
		try {
			taxSettingId = taxSettingDAO.addTaxSetting(ht, plant,isEdit,taxid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taxSettingId;		
	}

}
