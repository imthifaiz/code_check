package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.dao.ItemSesBeanDAO;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsUploadAlternateProduct implements WmsTran, IMLogger {

	DateUtils dateUtils = null;
	ItemUtil itemdao = null;
	ItemSesBeanDAO itemsesdao = null;
       
	public WmsUploadAlternateProduct() {
		itemdao = new ItemUtil();
		dateUtils = new DateUtils();
	
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		MLogger.log(0, "2.insertAlternatePrdsheet -  Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		return flag;
	}

	public boolean processCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " processCountSheet()");
		boolean flag = false;

		try {
			this.setMapDataToLogger(this.populateMapData((String) map.get("PLANT"), (String) map.get("LOGIN_USER")));
			itemdao.setmLogger(mLogger);
			
			
			   boolean isAltrPrdExists= itemdao.isAlternatePrdExists((String)map.get("PLANT"),(String) map.get(IConstants.ALTERNATE_ITEM));
			   if(!isAltrPrdExists){
				String alternateItemName = (String) map.get(IConstants.ITEM);
				String addalternateItem = (String) map.get(IConstants.ALTERNATE_ITEM);
				List<String> alternateItemNameLists = new ArrayList<String>();
    				if (addalternateItem != "" && addalternateItem != null) {
                                  alternateItemNameLists.add(addalternateItem);
				}

				flag = itemdao.insertAlternateItemLists(
						(String) map.get("PLANT"), (String) map.get("ITEM"),
						alternateItemNameLists);
				
			}else{
				flag=true;
			}
                  

		} catch (Exception e) {
			MLogger.log(-1, "Exception :: " + e.getMessage());
			throw e;
		}
		MLogger.log(-1, this.getClass() + " processCountSheet()");
		return flag;
	}

	
	
}
