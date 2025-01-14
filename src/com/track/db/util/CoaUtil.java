package com.track.db.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.CoaDAO;
import com.track.util.MLogger;

import net.sf.json.JSONObject;

public class CoaUtil {
	private boolean printLog = MLoggerConstant.CoaUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	private CoaDAO coaDao = new CoaDAO();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean addAccount(Hashtable<String, String> ht, String plant) {
		boolean accountCreated = false;
		try {
			accountCreated = coaDao.addAccount(ht, plant,"FINCHARTOFACCOUNTS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountCreated;
	}
	public int addExtendedType(Hashtable<String, String> ht, String plant) {
		int CreatedId = 0;
		try {
			CreatedId = coaDao.addExtendedType(ht, plant,"FINACCOUNTDETAILTYPE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CreatedId;
	}
	public List<Map<String, String>> getAccountGroup(String plant, String group) {

		List<Map<String, String>> listQry = null;
		try {
			listQry = coaDao.selectGroup(plant, group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQry;
	}
	
	public List<Map<String, String>> getGroup(String plant, String group) {

		List<Map<String, String>> listQry = null;
		try {
			listQry = coaDao.selectGroupData(plant, group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQry;
	}
	
	public List<Map<String, String>> getAccountType(String plant) {

		List<Map<String, String>> listQry = null;
		try {
			listQry = coaDao.selectAccountType(plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQry;
	}
	
	public List<Map<String, String>> getAccountDetailType(String plant, String group, String type) {

		List<Map<String, String>> listQry = null;
		try {
			listQry = coaDao.selectAccountDetailType(plant, group ,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQry;
	}

	public List<Map<String, String>> getSubAccountType(String plant, String group,String type) {

		List<Map<String, String>> listQry = null;
		try {
			listQry = coaDao.selectSubAccountType(plant, group,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQry;
	}

	public JSONObject readTable(String plant) {
		return coaDao.readTable(plant);
	}

	public JSONObject readTableRecord(String plant, String id) {
		return coaDao.readTableRecord(plant, id);
	}

	public boolean updateTable(String plant, String accountId, String accountName, String account_type,
			String account_det_type, String description, String account_is_sub, String account_subAcct,
			String account_balance, String account_balanceDate,String landedcost,String account_is_exp_gst) {
		boolean accountupdated = false;
		try {
			accountupdated = coaDao.updateTable(plant, accountId, accountName, account_type, account_det_type, description, account_is_sub, account_subAcct, account_balance, account_balanceDate,landedcost,account_is_exp_gst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountupdated;		 
	}

	public JSONObject readTableRecordByName(String plant, String name) {
		return coaDao.readTableRecordByName(plant, name);
	}
	
	public boolean isExistsType(String Type, String Group, String plant) {
		boolean exists = false;
		try {			
			exists = coaDao.isExistsType(Type, Group, plant);

		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean addAccountType(Hashtable<String, String> ht, String plant) {
		boolean accountCreated = false;
		try {
			accountCreated = coaDao.addAccount(ht, plant,"FINACCTYPE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountCreated;
	}
	
	public String GetaccountGroupId(String Group, String plant) {
		String GroupId = "";
		try {			
			GroupId = coaDao.GetaccountGroupId(Group, plant);

		} catch (Exception e) {
		}
		return GroupId;
	}
	
	public boolean addAccountGroup(Hashtable<String, String> ht, String plant) {
		boolean accountCreated = false;
		try {
			accountCreated = coaDao.addAccount(ht, plant,"FINACCTYPEGROUP");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountCreated;
	}
}
