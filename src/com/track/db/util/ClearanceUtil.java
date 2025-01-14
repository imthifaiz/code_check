package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.ClearAgentDAO;
import com.track.dao.ClearanceBeanDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CountryNCurrencyDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.PrdDeptDAO;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class ClearanceUtil {
 
	private ClearanceBeanDAO clearanceBeanDAO = new ClearanceBeanDAO();
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}


   
public boolean isExistsClearanceType(Hashtable htCT) throws Exception {
		boolean isExists = false;
		try {
			isExists = clearanceBeanDAO.isExistsClearanceType(htCT);
		} catch (Exception e) {
			throw e;
		}
		return isExists;
	}

public boolean insertClearanceTypeMst(Hashtable ht) throws Exception {
	boolean inserted = false;
	try {
		inserted = clearanceBeanDAO.insertIntoClearanceTypeMst(ht);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}
	
public boolean deleteClearanceTypeId(Hashtable ht) throws Exception {
	boolean deleted = false;
	try {
		deleted =clearanceBeanDAO.deleteClearanceTypeId(ht);
	} catch (Exception e) {
		throw e;
	}
	return deleted;
}
public boolean updateClearanceTypeId(Hashtable htUpdate, Hashtable htCondition)
		throws Exception {
	boolean update = false;
	try {
	
		update = clearanceBeanDAO.updateClearanceTypeMst(htUpdate, htCondition);

	} catch (Exception e) {
		throw e;
	}
	return update;
}
public ArrayList getClearanceTypeList(String aCustTypeId, String plant, String cond) {

	ArrayList al = null;
	try {
		al = clearanceBeanDAO.getClearanceTypeDetails(aCustTypeId, plant, cond);

	} catch (Exception e) {
	}

	return al;
}


public boolean isExistsClearingAgent(Hashtable ht) throws Exception {
	boolean exists = false;
	try {
		if (new ClearAgentDAO().getCountClearingAgentMst(ht) > 0)
			exists = true;
	} catch (Exception e) {
		
	}
	return exists;
}

public boolean isExistsClearancType(Hashtable ht) throws Exception {
	boolean exists = false;
	try {
		if (new ClearanceBeanDAO().getCountClearanceType(ht) > 0)
			exists = true;
	} catch (Exception e) {
		
	}
	return exists;
}

}
