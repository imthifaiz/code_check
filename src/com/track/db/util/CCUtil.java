package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.dao.CycleCountBeanDAO;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class CCUtil {
	private CycleCountBeanDAO cycleCountBeanDAO = new CycleCountBeanDAO();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public CCUtil() {
	}

	/**
	 * method : insertIntoCycleCount(String aItem, String aLoc) description :
	 * insert new record into INVMST
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertIntoCycleCount(String aItem, String aLoc,
			String aUserId) {
		boolean inserted = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			inserted = cycleCountBeanDAO.insertIntoCycleCount(aItem, aLoc,
					aUserId);
		} catch (Exception e) {
		}
		return inserted;
	}

	/**
	 * @method : updateQtyCycleCount(String aItem,String aLoc,String aQty)
	 * @description : update the qty for the given item and loc. If the boolean
	 *              value "aAddQty" is true, qty will be increased. False, qty
	 *              'll be reduced.
	 * @param aItem
	 * @param aLoc
	 * @param aQty
	 * @return
	 * @throws Exception
	 */
	public boolean updateQtyCycleCount(String aItem, String aLoc, String aQty,
			String aUserId, boolean aAddQty) {
		boolean flgUpdate = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			flgUpdate = cycleCountBeanDAO.updateQty4PK(aItem, aLoc, aQty,
					aUserId, aAddQty);

		} catch (Exception e) {

		}
		return flgUpdate;
	}

	/**
	 * method : isExistsCycleCount(String aItem,String aLoc) description : find
	 * the existance of inv mst rec
	 * 
	 * @param ht
	 * @return
	 */
	public boolean isExistsCycleCount(String aItem, String aLoc) {
		boolean exists = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			exists = cycleCountBeanDAO.isExistsCycleCount(aItem, aLoc);
		} catch (Exception e) {
		}
		return exists;
	}

	public boolean doStockTakeProcess(String aItem, String aLoc, String aQty,
			String aUserId) throws Exception {
		boolean flg = false;
		try {
			if (!this.isExistsCycleCount(aItem, aLoc)) {
				flg = this.insertIntoCycleCount(aItem, aLoc, aUserId);
				if (flg == false)
					return flg;
			}
			flg = this.updateQtyCycleCount(aItem, aLoc, aQty, aUserId, true);
		} catch (Exception e) {
		}
		return flg;
	}

	/**
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertIntoCCItem(Hashtable ht) {
		boolean exists = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			exists = cycleCountBeanDAO.insertIntoCCItem(ht);
		} catch (Exception e) {
		}
		return exists;

	}

	/**
	 * 
	 * @param htUpdate
	 * @param htCondition
	 * @return
	 */
	public boolean updateCCItem(Hashtable htUpdate, Hashtable htCondition) {
		boolean exists = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			exists = cycleCountBeanDAO.updateCCItem(htUpdate, htCondition);
		} catch (Exception e) {
		}
		return exists;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList getCCItemList() {
		ArrayList arrList = new ArrayList();
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			arrList = cycleCountBeanDAO.getCCItemList();
		} catch (Exception e) {
		}
		return arrList;
	}

	/**
	 * 
	 * @param aItem
	 * @return
	 */
	public boolean isExistInCycleCntItem(String aItem) {
		boolean exists = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			exists = cycleCountBeanDAO.isExistInCycleCntItem(aItem);
		} catch (Exception e) {
		}
		return exists;

	}

	/**
	 * @method : deleteCycleCountItem(String aItem)
	 * @description : delete the record from cyclecnt for the given item
	 * @param aItem
	 * @return
	 */
	public boolean deleteCycleCountItem(String aItem) {
		boolean deleted = false;
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			deleted = cycleCountBeanDAO.deleteCycleCountItem(aItem);
		} catch (Exception e) {
		}
		return deleted;

	}

	//////////PDA Methods//////////////////////////
	public String getAvailabeQty4PDA(String aItem, String aLoc) {
		String xmlStr = "";
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			float fAvlQty = cycleCountBeanDAO.getAvailableQty(aItem, aLoc);
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("record");
			xmlStr += XMLUtils.getXMLNode("AVLQTY", new Float(fAvlQty)
					.toString());
			xmlStr += XMLUtils.getEndNode("record");
		} catch (Exception e) {
		}
		return xmlStr;
	}

	/**
	 * @method : getCCItemList4PA()
	 * @description : get List of Cyclecount item
	 * @return
	 */
	public String getCCItemList4PA() {
		String xmlStr = "";
		try {
			cycleCountBeanDAO.setmLogger(mLogger);
			ArrayList arrayList = cycleCountBeanDAO.getCCItemList4PA();

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Items total ='"
					+ String.valueOf(arrayList.size()) + "'");
			for (int i = 0; i < arrayList.size(); i++) {
				ArrayList arrLine = (ArrayList) arrayList.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("ITEM", (String) arrLine.get(0));
				xmlStr += XMLUtils.getXMLNode("DESC", StrUtils
						.replaceCharacters2Send((String) arrLine.get(1)));

				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("Items");
		} catch (Exception e) {
		}
		return xmlStr;

	}

}