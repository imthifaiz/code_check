package com.track.db.util;

import com.track.constants.IDBConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.GstTypeDAO;


import com.track.util.MLogger;
import com.track.util.XMLUtils;
import com.track.util.StrUtils;

public class GstTypeUtil {
	public GstTypeUtil() {
	}

	private MLogger mLogger = new MLogger();
	private GstTypeDAO _GstTypeDAO = new GstTypeDAO();
	private boolean printLog = MLoggerConstant.GstType_PRINTPLANTMASTERLOG;
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean addGstType(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			_GstTypeDAO.setmLogger(mLogger);
			flag = _GstTypeDAO.insertIntoGstType(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return flag;
	}

	public ArrayList getGstTypeDetails(String gstType) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			_GstTypeDAO.setmLogger(mLogger);
			alResult = _GstTypeDAO.getGstTypeDetails("GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS",
					htCondition, " and gsttype like ='" + gstType + "%'"
							+ "ORDER BY GSTTYPE ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}


	
	public ArrayList getGstTypeDetails(String gstType, String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			_GstTypeDAO.setmLogger(mLogger);
			alResult = _GstTypeDAO.getGstTypeDetails(
					"GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS", htCondition,
					" and gsttype like ='" + gstType + "%'" + "ORDER BY GSTTYPE ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getAllGstTypeDetails() throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", "SIS");

		try {
			_GstTypeDAO.setmLogger(mLogger);
			alResult = _GstTypeDAO.getAllGstTypeDetails("GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS",
					htCondition, " ORDER BY GSTTYPE ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public ArrayList getAllGstTypeDetails(String plant, String cond,String user)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);

		try {
			_GstTypeDAO.setmLogger(mLogger);
               
			alResult = _GstTypeDAO.getAllGstTypeDetails(
					"GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS", htCondition, cond
							+ "   ORDER BY GSTTYPE ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
	
	public List qryGstType(String aGstType, String plant, String cond)
	throws Exception {
	List listQry = new ArrayList();
	try {
		
		listQry = _GstTypeDAO.queryGstType( aGstType, plant, cond);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return listQry;
	}

	public ArrayList getGstTypeListDetails(String plant, String gsttype)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);

		try {
			_GstTypeDAO.setmLogger(mLogger);
			alResult = _GstTypeDAO.getGstTypeDetails(
					"GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS",
					htCondition, gsttype);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public boolean updateGstType(Hashtable htValues, Hashtable htCondition,
			String extCond) throws Exception {
		boolean flag = false;
		try {
			StringBuffer sb = new StringBuffer(" set ");
			sb.append("GSTTYPE='" + htValues.get("GSTTYPE") + "'");
			sb.append(",GSTDESC='" + htValues.get("GSTDESC") + "'");
			sb.append(",GSTPERCENTAGE='" + htValues.get("GSTPERCENTAGE") + "'");
			sb.append(",REMARKS='" + htValues.get("REMARKS") + "'");
			_GstTypeDAO.setmLogger(mLogger);

			flag = _GstTypeDAO.updateGstType(sb.toString(), htCondition, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean deleteGstType(String gstType, String plant) throws Exception {

		boolean flag = false;
		try {
			 _GstTypeDAO.setmLogger(mLogger);
			flag =  _GstTypeDAO.deleteGstType(gstType, plant);

		} catch (Exception e) {

			throw e;
		}
		return flag;
	}
	
	public ArrayList getGstTypeListDetailsFromConfigKey(String plant, String configkey)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);
		htCondition.put("CONFIGKEY", configkey);

		try {
			_GstTypeDAO.setmLogger(mLogger);
			alResult = _GstTypeDAO.getGstTypeDetails(
					"GSTTYPE,GSTDESC,GSTPERCENTAGE,REMARKS",
					htCondition, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

        
}