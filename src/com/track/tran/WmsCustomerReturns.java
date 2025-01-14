package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


import javax.swing.Icon;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.MerchantBeanDAO;

import com.track.dao.MovHisDAO;
import com.track.dao.BomDAO;
import com.track.dao.ItemMstDAO;

import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsCustomerReturns implements WmsTran, IMLogger{
	private boolean printLog = MLoggerConstant.WmsMiscReceiveMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsMiscReceiveMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = new DateUtils();
        StrUtils su = new StrUtils();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	public WmsCustomerReturns() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = true;

		if (flag) {

			flag = processInvMst(m);

		}

		if (flag) {

			flag = processMovHis(m);

		}
		
		if(flag)
flag = processCustReturnSummary(m);
			
		if(flag)
		{
			flag = processBOM(m);
		}
		
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {
     
		boolean flag = false;
		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
			
           
			flag = _InvMstDAO.isExisit(htInvMst, "");
            
			if (flag) {
				
				
                 
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +'"
						+ map.get(IConstants.QTY) + "'");

				sql.append("," + IDBConstants.USERFLD4 + " = '"
						+ map.get(IConstants.INV_BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");
				sql.append("," + IDBConstants.UPDATED_BY + " = '"
						+ map.get(IConstants.LOGIN_USER) + "'");
				sql.append("," + IDBConstants.EXPIREDATE + " = '"
						+ map.get(IDBConstants.EXPIREDATE) + "'");
				
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				

			} else if (!flag) {
				// INSERT DATA TO INV MST
                
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map
						.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htInvMst.put(IDBConstants.STATUS, "");
				htInvMst.put(IDBConstants.EXPIREDATE,map.get(IDBConstants.EXPIREDATE));
				
				flag = _InvMstDAO.insertInvMst(htInvMst);
							
				
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.CUSTOMER_RETURNS);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			
			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS)));
			htRecvHis.put("LOC", map.get(IConstants.LOC));
			htRecvHis.put("BATNO", map.get(IConstants.INV_BATCH));
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
					.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			
			flag = movHisDao.insertIntoMovHis(htRecvHis);
					
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	//BOM Processing
	public boolean processBOM(Map map) throws Exception {
		boolean flag=false;
	    BomDAO bomdao = new BomDAO();
	    ItemMstDAO itemmstdao=new ItemMstDAO();
	    Hashtable htItem = new Hashtable();
	    htItem.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
	    htItem.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
	    try
		{
		   // if(itemmstdao.isExisit(htItem, " userfld1='K'") )
		   	if(itemmstdao.isExisit(htItem, " ") )
		    {
		    	 Hashtable htBOM = new Hashtable();
		    	 htBOM.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    	 htBOM.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
		    	 htBOM.put("PARENT_PRODUCT_BATCH", map.get(IConstants.INV_BATCH));
		    	 if(bomdao.isExisit(htBOM, " PARENT_PRODUCT_LOC LIKE 'SHIPPING%'"))
		    	 {     
		    		     Hashtable htUpdateBOM = new Hashtable();
		    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
		    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", map.get(IConstants.INV_BATCH));
		    		
		    			 StringBuffer sql = new StringBuffer(" SET ");
		 				 sql.append("PARENT_PRODUCT_LOC" + " = '"
		 					+ map.get(IConstants.LOC) + "'");
		 				 sql.append("," +"CHILD_PRODUCT_LOC" + " = '"
				 					+ map.get(IConstants.LOC) + "'");
		 				 sql.append("," +"STATUS" + " = 'A' ");
	    				 sql.append("," + IDBConstants.UPDATED_AT1 + " = '"
		 						+ dateUtils.getDateTime() + "'");
		 				 flag=bomdao.update(sql.toString(), htUpdateBOM, " and PARENT_PRODUCT_LOC LIKE 'SHIPPING%'");
		    	 }
		    	 
		    }
		    else
		    {
		    	flag=true;
		    }
			
		
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	   
	    return flag;
		
    	
	}
    		


//BOM Processing End


	public boolean processCustReturnSummary(Map map) throws Exception {
		boolean flag = false;
		MerchantBeanDAO merchdao = new MerchantBeanDAO();
		merchdao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			
			htRecvHis.put(IConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
			htRecvHis.put(IDBConstants.CUSTNAME, map.get(IDBConstants.CUSTNAME));
			htRecvHis.put(IDBConstants.EXPIREDATE, map.get(IDBConstants.EXPIREDATE));
			htRecvHis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS)));
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.SERIALNO, map.get(IConstants.INV_BATCH));
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.REASONCODE, map.get(IDBConstants.REASONCODE));
			htRecvHis.put(IConstants.ORDERNO , map.get(IConstants.ORDERNO)); //claim price
			
			htRecvHis.put("CUSTNO", map.get(IConstants.CUSTOMER_CODE));//Cust Code
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			
			

			flag = merchdao.insertIntoCustretsumry(htRecvHis);
			
		

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	

	
}
