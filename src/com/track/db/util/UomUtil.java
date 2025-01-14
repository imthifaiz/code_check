package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.RsnMst;
import com.track.dao.UomDAO;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class UomUtil {

	private boolean printLog = MLoggerConstant.TOUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public UomUtil() {

	}

	public boolean isExistsUom(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		UomDAO dao = new UomDAO();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}

	 public boolean isExistsUom(String uom, String plant) throws Exception {
			boolean exists = false;
			try {
				UomDAO _UomDAO = new UomDAO();
				_UomDAO.setmLogger(mLogger);
				Hashtable ht = new Hashtable();
				ht.put(IConstants.PLANT, plant);
				ht.put("UOM", uom);
				if (isExistUom(ht))
					exists = true;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return exists;
		}
	 
	 public boolean isExistUom(Hashtable ht) throws Exception {
			boolean exists = false;
			try {
				UomDAO _UomDAO = new UomDAO();
				_UomDAO.setmLogger(mLogger);
				if (_UomDAO.getCountUOM(ht) > 0)
					exists = true;
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return exists;
		}
		
	public boolean insertUom(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			UomDAO itemDao = new UomDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertIntoUom(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean updateUom(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			UomDAO dao = new UomDAO();
			dao.setmLogger(mLogger);
			update = dao.updateUom(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteUom(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			UomDAO dao = new UomDAO();
			dao.setmLogger(mLogger);
			deleted = dao.deleteUom(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public Map getUomDetails(String aUom) throws Exception {

		UomDAO dao = new UomDAO();
		dao.setmLogger(mLogger);
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.UOMCODE, aUom);
		Map map = new HashMap();
		try {

			String sql = " UOM,UOMDESC,Display,QPUOM,ISACTIVE";

		} catch (Exception e) {

			throw e;
		}
		return map;

	}

	public ArrayList getUomDetails(String aUom, String plant,
			String cond) {

		ArrayList al = null;
		UomDAO dao = new UomDAO();
		dao.setmLogger(mLogger);
		try {
			al = dao.getUomDetails(aUom, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}

	public String getAllUomWithQty(String plant1,String user) {

		String xmlStr = "";
		ArrayList al = null;
		UomDAO dao = new UomDAO();
		dao.setmLogger(mLogger);

		try {
			al = dao.getAllUOMByOrder(plant1,user);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("uom total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("uom"));
					 xmlStr += XMLUtils.getXMLNode("uomqty", (String) map
								.get("uomqty"));  
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("uom");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
	
	
    public ArrayList getUomDetails(String uom, String plant) {
        ArrayList arrList = new ArrayList();
        try {
        	UomDAO dao = new UomDAO();
        		dao.setmLogger(mLogger);
                arrList = dao.getUomDetail(uom, plant);
        } catch (Exception e) {
        }
        return arrList;
}

}