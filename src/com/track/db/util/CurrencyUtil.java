package com.track.db.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CurrencyDAO;
import com.track.dao.ItemMstDAO;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class CurrencyUtil {

	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.InvMstUtil_PRINTPLANTMASTERLOG;
	
	StrUtils strUtils=new StrUtils();
	
	
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean isExistCurrency(Hashtable ht, String excond) {
		boolean exists = false;
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			exists = curdao.isExisit(ht, excond);

		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean updateCurrencyID(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			update = curdao.updateCurrencyMst(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}
	
	public ArrayList getCurrencyDetails(String aCurrencyid, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			arrList = curdao.getCurrencyDetails(aCurrencyid, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getCurDetailsByDisplay(String display, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			arrList = curdao.getCurencyByDisplay(display, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public boolean insertCurrency(Hashtable ht) {
		boolean inserted = false;
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			inserted = curdao.insCurrencyMst(ht);
		} catch (Exception e) {
		}
		return inserted;
	}
	//To get any column data
	public String getCurrencyID(Hashtable ht,String col) throws Exception {
		String currencyID = "";
		CurrencyDAO curDao = new CurrencyDAO();
		String query = " ISNULL( " + col+ ",'') as " + col + " ";
		curDao.setmLogger(mLogger);
		Map m = curDao.selectRow(query, ht);

		currencyID = (String) m.get(col);
		return currencyID;
	}	
 /* getMobileOrderCurrencyDetails 
 * Created By Bruhan,June 20 2014, To get Mobile Order Currency details
     *  ************Modification History*********************************
     * 
  */
public String getMobileSalesOrderCurrencyDetails(String plant,String display) {
		String xmlStr = "";
		ArrayList al = null;
		CurrencyDAO curDao = new CurrencyDAO();
		try {
			al = curDao.selectmobileordercurrencydetails(plant,display);
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("CurrencyDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("currencyid", (String) map.get("CURRENCYID"));
					xmlStr += XMLUtils.getXMLNode("description",  (String) map.get("DESCRIPTION"));
					xmlStr += XMLUtils.getXMLNode("display",  (String) map.get("DISPLAY"));
					xmlStr += XMLUtils.getXMLNode("currencyseqt",  (String) map.get("CURRENCYUSEQT"));
					xmlStr += XMLUtils.getXMLNode("remark",  (String) map.get("REMARK"));
					xmlStr += XMLUtils.getXMLNode("status",  (String) map.get("STATUS"));
					xmlStr += XMLUtils.getXMLNode("isactive",  (String) map.get("ISACTIVE"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("CurrencyDetails");
		
			}
		} catch (Exception e) {
			
		}
		return xmlStr;
	}

	public ArrayList getCurrencyDetailsfordropdown(String display, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			CurrencyDAO curdao = new CurrencyDAO();
			curdao.setmLogger(mLogger);
			arrList = curdao.getfordropdown(plant,display);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public boolean isExistCurrency(String currency, String plant) {
		boolean exists = false;
		CurrencyDAO currencyDAO = new CurrencyDAO();
		try {
			currencyDAO.setmLogger(mLogger);
			exists = currencyDAO.isExistCurrency(currency, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean isExistTax(String tax, String plant) {
		boolean exists = false;
		CurrencyDAO currencyDAO = new CurrencyDAO();
		try {
			currencyDAO.setmLogger(mLogger);
			exists = currencyDAO.isExistTax(tax, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
}
