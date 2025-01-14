package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.SemiFinishedProductDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsSemiFinishedProduct implements IMLogger {
	
	DateUtils dateUtils = null;
	InvMstDAO _InvMstDAO = null;
	RecvDetDAO _RecvDetDAO = null;
	ItemUtil itemdao = null;
	LocMstDAO locdao = null;
	SemiFinishedProductDAO processingReceiveDAO = null;
	public WmsSemiFinishedProduct() {
		_InvMstDAO = new InvMstDAO();
		dateUtils = new DateUtils();
		itemdao = new ItemUtil();
		locdao = new LocMstDAO();
		_RecvDetDAO = new RecvDetDAO();
		processingReceiveDAO = new SemiFinishedProductDAO();
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
	public boolean processWmsReceive(Map m) throws Exception {
		boolean flag = false;
	

		MLogger.log(0,"2.insert LocMSt - XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Stage : 1");
		flag = processCountSheet(m);
		MLogger.log(0, "processCountSheet() : Transaction : " + flag);
		if(flag== true)
			flag=processMovHis(m);

		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean processCountSheet(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
		_InvMstDAO.setmLogger(mLogger);
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
        if(!nonstocktype.equals("Y"))	
	    {
		//check for existence of item
		itemdao.setmLogger(mLogger);
		locdao.setmLogger(mLogger);
		boolean result=itemdao.isExistsItemMst((String)map.get(IConstants.ITEM), (String)map.get("PLANT"));
		
		//check for valid location
		Hashtable htcdn = new Hashtable();
		htcdn.put(IConstants.PLANT, map.get(IConstants.PLANT));
		htcdn.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
		boolean  result1=locdao.isExisit(htcdn, "");	
		 if(result==true&&result1==true){
		//flag = _InvMstDAO.isExisit(htInvMst, "QTY>="+map.get(IConstants.QTY));		
		//if (!flag) {
		String stkqty = _InvMstDAO.getItemTotalQty((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM), " USERFLD4='NOBATCH' AND LOC in ('"+map.get(IDBConstants.LOC)+"','KITCHEN')");
     	if(Double.parseDouble(stkqty)<Double.parseDouble((String) map.get(IConstants.QTY))){
			throw new Exception(" Not Enough Inventory for Processing Product:"+map.get(IDBConstants.ITEM)+" with Batch:"+map.get(IDBConstants.BATCH));
		}
    	else
    	{
    		//STOCK MOVE PRODUCT LOC TO KITCHEN
    		String kitchenstkqty = _InvMstDAO.getItemTotalQty((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM), " USERFLD4='NOBATCH' AND LOC in ('KITCHEN')");
    		if(Double.parseDouble(kitchenstkqty)<Double.parseDouble((String) map.get(IConstants.QTY))){    			
    			//STOCK OUT
    			String stkout = processSTOCK_OUTQTY(map);
    			flag = processSTOCK_OUT(map);
    			if(flag) {
    			//Kitchen STOCK IN
        		flag = processKitchenSTOCK_IN(map,stkout);    				
    			}	    			
    		} else
    			flag=true;
    		
    		if(flag) {
    		//Kitchen OUT
    			flag = processKitchenSTOCK_OUT(map);
    			if(flag) {
    			//RESERVE KITCHEN IN
    			flag = processReserveKitchenSTOCK_IN(map);
    			if(flag) {
    			//RESERVE KITCHEN OUT
    		htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, "RESERVE_KITCHEN");
			htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
			ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, "");
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					if(adjustedQuantity > 0.0) {
					StringBuffer sql1 = new StringBuffer(" SET ");
					sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");

					Hashtable htInvMstReduce = new Hashtable();
					htInvMstReduce.clear();
					htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htInvMstReduce.put(IDBConstants.LOC, "RESERVE_KITCHEN");
					htInvMstReduce.put(IDBConstants.USERFLD4,  map.get(IDBConstants.BATCH));
					htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
					htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));

					flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
					if (!flag) {
						throw new Exception("Could not update");
					}										    			
					
					//SHIP_HIS
					Hashtable htIssueDet = new Hashtable();
					htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					//htIssueDet.put(IDBConstants.DODET_DONUM, map.get("GINO"));
					htIssueDet.put(IDBConstants.DODET_DONUM, "");
					htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
					htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htIssueDet.put(IDBConstants.ITEM_DESC, map.get(IDBConstants.ITEM_DESC));
					htIssueDet.put("BATCH", map.get(IDBConstants.BATCH));
					htIssueDet.put(IDBConstants.LOC, "RESERVE_KITCHEN");
					htIssueDet.put("LOC1", "RESERVE_KITCHEN");
					//htIssueDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
					//htIssueDet.put("LOC1", map.get(IConstants.LOC));
					htIssueDet.put("DOLNO", String.valueOf(map.get(IConstants.OUT_DOLNNO)));
					htIssueDet.put("ORDQTY", map.get(IConstants.QTY));
					htIssueDet.put("PICKQTY", String.valueOf(adjustedQuantity));
					htIssueDet.put("REVERSEQTY", "0");
					htIssueDet.put("STATUS", "C");
					htIssueDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htIssueDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htIssueDet.put(IDBConstants.ISSUEDATE, DateUtils.getDate());
					htIssueDet.put(IDBConstants.EMPNO, "");
					htIssueDet.put(IDBConstants.RSNCODE, "");
					htIssueDet.put(IDBConstants.REFNO, "");
					htIssueDet.put("REMARK", "");
					htIssueDet.put("INVOICENO", map.get("GINO"));
					htIssueDet.put("TRAN_TYPE", "KITTING");
					htIssueDet.put(IConstants.CURRENCYID, map.get("CURRENCY"));
					htIssueDet.put(IConstants.INVOICENO, map.get("GINO"));
					htIssueDet.put(IDBConstants.PRICE, map.get("UNITCOST"));
					htIssueDet.put(IDBConstants.INVID, String.valueOf(mapIterStock.get(IDBConstants.INVID)));
					flag =new ShipHisDAO().insertShipHis(htIssueDet);
					
					quantityToAdjust -= adjustedQuantity;
					}
				}
    			}    		
    		}
    			}
    	}
    	}
		
		if(flag) {
			
			Integer hdrid= Integer.parseInt(map.get("HDR_ID").toString());
			//SEMIPRODUCT_PROCESSING_DET
        	Hashtable htkitbominsert = new Hashtable();
			htkitbominsert.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htkitbominsert.put("HDR_ID", String.valueOf(hdrid));
			htkitbominsert.put("GINO", map.get("GINO"));
			htkitbominsert.put(IDBConstants.CHILD_PRODUCT, map.get(IDBConstants.ITEM));
			htkitbominsert.put(IDBConstants.CHILD_LOC, map.get(IDBConstants.LOC));
			htkitbominsert.put(IDBConstants.CHILD_BATCH, map.get(IDBConstants.BATCH));
			htkitbominsert.put("QTY", map.get(IConstants.QTY));
			htkitbominsert.put("CHILDUOM", map.get("CHILDUOM"));
			htkitbominsert.put("CHILD_COST",  map.get("AVERAGEUNITCOST"));
			htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
			htkitbominsert.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
			flag = processingReceiveDAO.addProcessingDet(htkitbominsert, (String)map.get(IConstants.PLANT));			
			
		}
		
		 }else
		 {
			 throw new Exception("One of the ITEM or Loc is not created yet.");
		 }
	} else {
		Integer hdrid= Integer.parseInt(map.get("HDR_ID").toString());
		
		//SHIP_HIS
		Hashtable htIssueDet = new Hashtable();
		double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
		
		htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		//htIssueDet.put(IDBConstants.DODET_DONUM, map.get("GINO"));
		htIssueDet.put(IDBConstants.DODET_DONUM, "");
		htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
		htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htIssueDet.put(IDBConstants.ITEM_DESC, map.get(IDBConstants.ITEM_DESC));
		htIssueDet.put("BATCH", map.get(IDBConstants.BATCH));
		htIssueDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
		htIssueDet.put("LOC1", map.get(IConstants.LOC));
		htIssueDet.put("DOLNO", String.valueOf(map.get(IConstants.OUT_DOLNNO)));
		htIssueDet.put("ORDQTY", map.get(IConstants.QTY));
		htIssueDet.put("PICKQTY", String.valueOf(quantityToAdjust));
		htIssueDet.put("REVERSEQTY", "0");
		htIssueDet.put("STATUS", "C");
		htIssueDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		htIssueDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htIssueDet.put(IDBConstants.ISSUEDATE, DateUtils.getDate());
		htIssueDet.put(IDBConstants.EMPNO, "");
		htIssueDet.put(IDBConstants.RSNCODE, "");
		htIssueDet.put(IDBConstants.REFNO, "");
		htIssueDet.put("REMARK", "");
		htIssueDet.put("INVOICENO", map.get("GINO"));
		htIssueDet.put("TRAN_TYPE", "KITTING");
		htIssueDet.put(IConstants.CURRENCYID, map.get("CURRENCY"));
		htIssueDet.put(IConstants.INVOICENO, map.get("GINO"));
		htIssueDet.put(IDBConstants.PRICE, map.get("UNITCOST"));
		flag =new ShipHisDAO().insertShipHis(htIssueDet);
		
		//SEMIPRODUCT_PROCESSING_DET
    	Hashtable htkitbominsert = new Hashtable();
		htkitbominsert.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htkitbominsert.put("HDR_ID", String.valueOf(hdrid));
		htkitbominsert.put("GINO", map.get("GINO"));
		htkitbominsert.put(IDBConstants.CHILD_PRODUCT, map.get(IDBConstants.ITEM));
		htkitbominsert.put(IDBConstants.CHILD_LOC, map.get(IDBConstants.LOC));
		htkitbominsert.put(IDBConstants.CHILD_BATCH, map.get(IDBConstants.BATCH));
		htkitbominsert.put("QTY", map.get(IConstants.QTY));
		htkitbominsert.put("CHILDUOM", map.get("CHILDUOM"));
		htkitbominsert.put("CHILD_COST",  map.get("AVERAGEUNITCOST"));
		htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
		htkitbominsert.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
		flag = processingReceiveDAO.addProcessingDet(htkitbominsert, (String)map.get(IConstants.PLANT));
	}
		return flag;
	}
	@SuppressWarnings("unchecked")
	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		this.setMapDataToLogger(this.populateMapData( (String)map.get("PLANT"), (String)map.get("LOGIN_USER")));
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htmov = new Hashtable();
			htmov.clear();
			htmov.put(IDBConstants.PLANT, map.get("PLANT"));
			htmov.put(IDBConstants.DIRTYPE, "KITTING_OUT");
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
	        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
	        if(!nonstocktype.equals("Y"))		    
			htmov.put(IDBConstants.LOC, "RESERVE_KITCHEN");
	        else
			htmov.put(IDBConstants.LOC, map.get("LOC"));
			htmov.put("Qty", map.get(IConstants.QTY));
			htmov.put(IDBConstants.ITEM, map.get("ITEM"));
			if((String)map.get("CHILDUOM")!=null ) 
			htmov.put(IDBConstants.UOM, map.get("CHILDUOM"));
			htmov.put("BATNO", map.get(IDBConstants.BATCH));
			htmov.put(IDBConstants.MOVTID, "OUT");
			htmov.put(IDBConstants.RECID, "");
			htmov.put(IDBConstants.PONO, "");
			htmov.put("ORDNUM",map.get("GINO"));
			htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		    htmov .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		  

			flag = movHisDao.insertIntoMovHis(htmov);

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	public String processSTOCK_OUTQTY(Map map) throws Exception {
		String adjuctqty = "0";
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, "");
		if (!alStock.isEmpty()) {
    		String kitchenstkqty = _InvMstDAO.getItemTotalQty((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM), " USERFLD4='NOBATCH' AND LOC in ('KITCHEN')");
    		double movToAdjust = Double.parseDouble((String) map.get(IConstants.QTY)) - Double.parseDouble(kitchenstkqty);    		
    		double quantityToAdjust = 1;
    		if(movToAdjust > 1) {
    	//imti added for qty testing
    			double Pqty = (Double.parseDouble("" + map.get("PQTY")));
    			double Adjqty = movToAdjust;
    			if(Pqty<Adjqty) 
    				quantityToAdjust = movToAdjust;	
    			else
    				quantityToAdjust = (Double.parseDouble("" + map.get("PQTY"))) - movToAdjust;
    	//imti end
//    			quantityToAdjust = (Double.parseDouble("" + map.get("PQTY"))) - movToAdjust;
	    		int rtqty = (int)(quantityToAdjust);
	    		if (quantityToAdjust % 1 != 0) 
	    			 rtqty = rtqty + 1;
	 
	    		quantityToAdjust = Double.valueOf(rtqty);
    		}
			Iterator iterStock = alStock.iterator();
			double totadjustedQuantity = 0.0; 
			while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
				Map mapIterStock = (Map)iterStock.next();
				double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
				double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
				totadjustedQuantity=totadjustedQuantity+adjustedQuantity;
				quantityToAdjust -= adjustedQuantity;
				}			
			adjuctqty=String.valueOf(totadjustedQuantity);
			
		}
		} catch (Exception e) {
			throw e;
		}
		return adjuctqty;
	}
	
	public boolean processSTOCK_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
			htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
			ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, "");
			if (!alStock.isEmpty()) {
				String kitchenstkqty = _InvMstDAO.getItemTotalQty((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM), " USERFLD4='NOBATCH' AND LOC in ('KITCHEN')");
				//double quantityToAdjust = Double.parseDouble("" + map.get("PQTY")) - Double.parseDouble(kitchenstkqty);
				double movToAdjust = Double.parseDouble((String) map.get(IConstants.QTY)) - Double.parseDouble(kitchenstkqty);    		
	    		double quantityToAdjust = 1;
	    		if(movToAdjust > 1) {
	    	//imti added for qty testing
	    			double Pqty = (Double.parseDouble("" + map.get("PQTY")));
	    			double Adjqty = movToAdjust;
	    			if(Pqty<Adjqty) 
	    				quantityToAdjust = movToAdjust;	
	    			else
	    				quantityToAdjust = (Double.parseDouble("" + map.get("PQTY"))) - movToAdjust;
	    	//imti end
//	    			quantityToAdjust = (Double.parseDouble("" + map.get("PQTY"))) - movToAdjust;
		    		int rtqty = (int)(quantityToAdjust);
		    		if (quantityToAdjust % 1 != 0) 
		    			 rtqty = rtqty + 1;
		    		quantityToAdjust = Double.valueOf(rtqty);
	    		}
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					StringBuffer sql1 = new StringBuffer(" SET ");
					sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");
					
					Hashtable htInvMstReduce = new Hashtable();
					htInvMstReduce.clear();
					htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htInvMstReduce.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
					htInvMstReduce.put(IDBConstants.USERFLD4,  map.get(IDBConstants.BATCH));
					htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
					htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					
					flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
					if (!flag) {
						throw new Exception("Could not update");
					}
								
				
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_OUT);
				htRecvHis.put("ORDNUM",  "");
				htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				if((String)map.get("CHILDUOM")!=null ) 
					htRecvHis.put(IDBConstants.UOM, map.get("CHILDUOM"));
				htRecvHis.put("BATNO", map.get(IDBConstants.BATCH));
				htRecvHis.put(IDBConstants.QTY, "-" + String.valueOf(adjustedQuantity));
				htRecvHis.put("MOVTID", "OUT");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
				htRecvHis.put(IDBConstants.USERFLD1, map.get(IDBConstants.LOC));
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
				htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
				htRecvHis .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htRecvHis.put(IDBConstants.REMARKS, "KITTING");
				flag = movHisDao.insertIntoMovHis(htRecvHis);
				quantityToAdjust -= adjustedQuantity;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	
	public boolean processKitchenSTOCK_IN(Map map,String stkout) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();			
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, "KITCHEN");
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		//htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
		//double curqty = Double.parseDouble((String) map.get("PQTY"));
		//String kitchenstkqty = _InvMstDAO.getItemTotalQty((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM), " USERFLD4='NOBATCH' AND LOC in ('KITCHEN')");
		//double curqty = Double.parseDouble("" + map.get("PQTY")) - Double.parseDouble(kitchenstkqty);
		//int rtqty = (int)(curqty);
		double curqty = Double.parseDouble(stkout);
		flag = _InvMstDAO.isExisit(htInvMst, "");
		if(flag)
		{
			StringBuffer sql1 = new StringBuffer(" SET ");
		    sql1.append(IDBConstants.QTY + " =  QTY +'"
		                    + curqty + "', ");	                                            
			sql1.append(IDBConstants.EXPIREDATE + " = '', ");
			sql1.append(IDBConstants.UPDATED_AT + " = '"
					+ DateUtils.getDateTime() + "', ");
			sql1.append(IDBConstants.UPDATED_BY + " = '"
					+ map.get(IConstants.LOGIN_USER) + "'");
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
		} else {
			htInvMst.put(IConstants.EXPIREDATE, "");
			htInvMst.put(IDBConstants.QTY, String.valueOf(curqty));
			htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
			htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			flag = _InvMstDAO.insertInvMst(htInvMst);
		}
		
		Hashtable htRecvHis = new Hashtable();
		htRecvHis.clear();
		htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_IN);
		htRecvHis.put("ORDNUM",  "");
		htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		if((String)map.get("CHILDUOM")!=null ) 
		htRecvHis.put(IDBConstants.UOM, map.get("CHILDUOM"));
		htRecvHis.put("BATNO", map.get(IDBConstants.BATCH));
		htRecvHis.put(IDBConstants.QTY, String.valueOf(curqty));
		htRecvHis.put("MOVTID", "IN");
		htRecvHis.put("RECID", "");
		htRecvHis.put(IDBConstants.LOC, "KITCHEN");
		htRecvHis.put(IDBConstants.USERFLD1, "KITCHEN");
		htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
		htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		htRecvHis .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htRecvHis.put(IDBConstants.REMARKS, "KITTING");
		flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	
	public boolean processKitchenSTOCK_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();			
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, "KITCHEN");
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, "");
		if (!alStock.isEmpty()) {
			double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
			Iterator iterStock = alStock.iterator();
			while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
				Map mapIterStock = (Map)iterStock.next();
				double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
				double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
				if(adjustedQuantity > 0.0) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + adjustedQuantity + "'");

				Hashtable htInvMstReduce = new Hashtable();
				htInvMstReduce.clear();
				htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMstReduce.put(IDBConstants.LOC, "KITCHEN");
				htInvMstReduce.put(IDBConstants.USERFLD4,  map.get(IDBConstants.BATCH));
				htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
				htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));

				flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
				if (!flag) {
					throw new Exception("Could not update");
				}
				
				Hashtable htRecvHis = new Hashtable();
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_OUT);
				htRecvHis.put("ORDNUM",  "");
				htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				if((String)map.get("CHILDUOM")!=null ) 
				htRecvHis.put(IDBConstants.UOM, map.get("CHILDUOM"));
				htRecvHis.put("BATNO", map.get(IDBConstants.BATCH));
				htRecvHis.put(IDBConstants.QTY, "-" + adjustedQuantity);
				htRecvHis.put("MOVTID", "OUT");
				htRecvHis.put("RECID", "");
				htRecvHis.put(IDBConstants.LOC, "KITCHEN");
				htRecvHis.put(IDBConstants.USERFLD1, "KITCHEN");
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
				htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
				htRecvHis .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htRecvHis.put(IDBConstants.REMARKS, "KITTING");
    			flag = movHisDao.insertIntoMovHis(htRecvHis);
				quantityToAdjust -= adjustedQuantity;
			}
			}
		}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	public boolean processReserveKitchenSTOCK_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
		Hashtable<String, Object> htInvMst = new Hashtable<String, Object>();			
		htInvMst.clear();
		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htInvMst.put(IDBConstants.LOC, "RESERVE_KITCHEN");
		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
		//htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
		double curqty = Double.parseDouble("" + map.get(IConstants.QTY));
		flag = _InvMstDAO.isExisit(htInvMst, "");
		if(flag)
		{
			StringBuffer sql1 = new StringBuffer(" SET ");
		    sql1.append(IDBConstants.QTY + " =  QTY +'"
		                    + curqty + "', ");	                                            
			sql1.append(IDBConstants.EXPIREDATE + " = '', ");
			sql1.append(IDBConstants.UPDATED_AT + " = '"
					+ DateUtils.getDateTime() + "', ");
			sql1.append(IDBConstants.UPDATED_BY + " = '"
					+ map.get(IConstants.LOGIN_USER) + "'");
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
		} else {
			htInvMst.put(IConstants.EXPIREDATE, "");
			htInvMst.put(IDBConstants.QTY, String.valueOf(curqty));
			htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
			htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			flag = _InvMstDAO.insertInvMst(htInvMst);
		}
		
		Hashtable htRecvHis = new Hashtable();
		htRecvHis.clear();
		htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htRecvHis.put("DIRTYPE", TransactionConstants.LT_TRAN_IN);
		htRecvHis.put("ORDNUM",  "");
		htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		if((String)map.get("CHILDUOM")!=null ) 
		htRecvHis.put(IDBConstants.UOM, map.get("CHILDUOM"));
		htRecvHis.put("BATNO", map.get(IDBConstants.BATCH));
		htRecvHis.put(IDBConstants.QTY, (String) map.get(IConstants.QTY));
		htRecvHis.put("MOVTID", "IN");
		htRecvHis.put("RECID", "");
		htRecvHis.put(IDBConstants.LOC, "RESERVE_KITCHEN");
		htRecvHis.put(IDBConstants.USERFLD1, "RESERVE_KITCHEN");
		htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
		htRecvHis.put(IDBConstants.CUSTOMER_CODE, "");
		htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		htRecvHis .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		htRecvHis.put(IDBConstants.REMARKS, "KITTING");
		flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}	
}
