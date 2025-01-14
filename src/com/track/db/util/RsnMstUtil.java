package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.dao.RsnMst;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class RsnMstUtil {

	private MLogger mLogger = new MLogger();
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public RsnMstUtil() {

	}

	public boolean isExistsItemId(Hashtable htLoc) throws Exception {

		boolean isExists = false;
		RsnMst dao = new RsnMst();
		dao.setmLogger(mLogger);
		try {
			isExists = dao.isExists(htLoc);

		} catch (Exception e) {

			throw e;
		}

		return isExists;
	}

	public boolean insertRsnMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			RsnMst itemDao = new RsnMst();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertIntoRsnMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean updateItemId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			RsnMst dao = new RsnMst();
			dao.setmLogger(mLogger);
			update = dao.updateRsnMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteItemId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			RsnMst dao = new RsnMst();
			dao.setmLogger(mLogger);
			deleted = dao.deleteItemId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public Map getItemDetails(String aItemId) throws Exception {

		RsnMst dao = new RsnMst();
		dao.setmLogger(mLogger);
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.RSNCODE, aItemId);
		Map map = new HashMap();
		try {

			String sql = " RSNCODE,RSNDESC,ISACTIVE";

		} catch (Exception e) {

			throw e;
		}
		return map;

	}

	public ArrayList getReasonMstDetails(String aItemId, String plant,
			String cond) {

		ArrayList al = null;
		RsnMst dao = new RsnMst();
		dao.setmLogger(mLogger);
		try {
			al = dao.getReasonMstDetails(aItemId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}

	public String getReasonCode(String plant) {

		String xmlStr = "";
		ArrayList al = null;
		RsnMst dao = new RsnMst();
		dao.setmLogger(mLogger);
		try {
			al = dao.getReasonCode(plant);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("reasonList total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("code", (String) map
							.get("rsnCode"));
					xmlStr += XMLUtils.getXMLNode("description", (String)strUtils.replaceCharacters2SendPDA( map
							.get("rsnDesc").toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("reasonList");
			}
		} catch (Exception e) {
		}
		return xmlStr;
	}

}