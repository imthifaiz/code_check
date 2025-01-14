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
import com.track.dao.ProcessingReceiveDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.ItemUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsProcessingReceive implements IMLogger {
	
	DateUtils dateUtils = null;
	InvMstDAO _InvMstDAO = null;
	RecvDetDAO _RecvDetDAO = null;
	ItemUtil itemdao = null;
	LocMstDAO locdao = null;
	ProcessingReceiveDAO processingReceiveDAO = null;
	public WmsProcessingReceive() {
		_InvMstDAO = new InvMstDAO();
		dateUtils = new DateUtils();
		itemdao = new ItemUtil();
		locdao = new LocMstDAO();
		_RecvDetDAO = new RecvDetDAO();
		processingReceiveDAO = new ProcessingReceiveDAO();
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
        	Boolean ischildcal =new ItemMstDAO().getischildcal((String)map.get("PLANT"), (String)map.get(IConstants.ITEM));
        	if(!ischildcal)
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
		//No need to check now if batch is provided. Check only for NOBATCH.
		if ("NOBATCH".equals(map.get(IDBConstants.BATCH))) {
			flag = _InvMstDAO.isExisit(htInvMst, "");				
		} else {
			htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
		}
		flag = _InvMstDAO.isExisit(htInvMst, "");
		
		if (flag) {
			StringBuffer sql1 = new StringBuffer(" SET ");
		    sql1.append(IDBConstants.QTY + " =  QTY +'"
		                    + map.get(IConstants.QTY) + "', ");
                                        
			sql1.append(IDBConstants.EXPIREDATE + " = '"
					+ map.get(IConstants.EXPIREDATE) + "', ");
			sql1.append(IDBConstants.UPDATED_AT + " = '"
					+ DateUtils.getDateTime() + "', ");
			sql1.append(IDBConstants.UPDATED_BY + " = '"
					+ map.get("LOGIN_USER") + "'");
			flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");			

		} else {
		        htInvMst.put(IConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
			htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));

			//htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htInvMst.put(IDBConstants.CREATED_AT,  DateUtils.getDate().toString().replaceAll("/", "") + "000000");
			htInvMst.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			flag = _InvMstDAO.insertInvMst(htInvMst);			
		}
		
		if(flag) {
			
			Integer hdrid= Integer.parseInt(map.get("HDR_ID").toString());
			//PROCESSING_RECEIVE_DET
        	Hashtable htkitbominsert = new Hashtable();
			htkitbominsert.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htkitbominsert.put("HDR_ID", String.valueOf(hdrid));
			htkitbominsert.put(IDBConstants.CHILD_PRODUCT, map.get(IDBConstants.ITEM));
			htkitbominsert.put(IDBConstants.CHILD_LOC, map.get(IDBConstants.LOC));
			htkitbominsert.put(IDBConstants.CHILD_BATCH, map.get(IDBConstants.BATCH));
			htkitbominsert.put("QTY", map.get(IConstants.QTY));
			htkitbominsert.put("CHILDUOM", map.get("CHILDUOM"));
			htkitbominsert.put("CHILD_COST",  map.get("AVERAGEUNITCOST"));
			htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
			htkitbominsert.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
			flag = processingReceiveDAO.addProcessingReceiveDet(htkitbominsert, (String)map.get(IConstants.PLANT));
			
			//Double pCostPerqty=Double.parseDouble((String)map.get("AVERAGEUNITCOST"))/Double.parseDouble((String)map.get(IConstants.QTY));
			
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put("PONO","");
			htRecvDet.put("GRNO",map.get(IConstants.GRNO));
			htRecvDet.put(IDBConstants.ITEM, map.get(IDBConstants.ITEM));
			htRecvDet.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
			htRecvDet.put("BATCH", map.get(IDBConstants.BATCH));
			htRecvDet.put("ORDQTY", map.get("CQTY"));
			htRecvDet.put("RECQTY", map.get("CQTY"));
			htRecvDet.put("UNITCOST", map.get("AVERAGEUNITCOST"));
			htRecvDet.put("CURRENCYID", map.get("CURRENCY"));
			//htRecvDet.put("UNITCOST", String.valueOf(pCostPerqty));
			htRecvDet.put("TRAN_TYPE", "DE-KITTING");
			htRecvDet.put(IDBConstants.RECVDATE, DateUtils.getDate());
			htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			flag = _RecvDetDAO.insertRecvDet(htRecvDet);
		}
		
		 }else
		 {
			 throw new Exception("One of the ITEM or Loc is not created yet.");
		 }
        	}
        	else {
        		
        		htInvMst.clear();
        		htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
        		htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
        		htInvMst.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
        		htInvMst.put(IDBConstants.BATCH, map.get(IDBConstants.BATCH));
        		
        		flag = _InvMstDAO.isExisit(htInvMst, "QTY>="+map.get(IConstants.QTY));
        		
        		if (!flag) {
        			throw new Exception(" Not Enough Inventory for Processing Product:"+map.get(IDBConstants.ITEM)+" with Batch:"+map.get(IDBConstants.BATCH));
        		}
            	else
            	{
        			
        			ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, "");
        			if (!alStock.isEmpty()) {
        				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
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
        					quantityToAdjust -= adjustedQuantity;
        				}
        			}
            	}
        		
        		Integer hdrid= Integer.parseInt(map.get("HDR_ID").toString());
    			//PROCESSING_RECEIVE_DET
            	Hashtable htkitbominsert = new Hashtable();
    			htkitbominsert.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
    			htkitbominsert.put("HDR_ID", String.valueOf(hdrid));
    			htkitbominsert.put(IDBConstants.CHILD_PRODUCT, map.get(IDBConstants.ITEM));
    			htkitbominsert.put(IDBConstants.CHILD_LOC, map.get(IDBConstants.LOC));
    			htkitbominsert.put(IDBConstants.CHILD_BATCH, map.get(IDBConstants.BATCH));
    			htkitbominsert.put("QTY", map.get(IConstants.QTY));
    			htkitbominsert.put("CHILDUOM", map.get("CHILDUOM"));
    			htkitbominsert.put("CHILD_COST",  map.get("AVERAGEUNITCOST"));
    			htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
    			htkitbominsert.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
    			flag = processingReceiveDAO.addProcessingReceiveDet(htkitbominsert, (String)map.get(IConstants.PLANT));
        	}
	    }
        else {
        	//flag=true;
        	Integer hdrid= Integer.parseInt(map.get("HDR_ID").toString());
			//PROCESSING_RECEIVE_DET
        	Hashtable htkitbominsert = new Hashtable();
			htkitbominsert.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htkitbominsert.put("HDR_ID", String.valueOf(hdrid));
			htkitbominsert.put(IDBConstants.CHILD_PRODUCT, map.get(IDBConstants.ITEM));
			htkitbominsert.put(IDBConstants.CHILD_LOC, map.get(IDBConstants.LOC));
			htkitbominsert.put(IDBConstants.CHILD_BATCH, map.get(IDBConstants.BATCH));
			htkitbominsert.put("QTY", map.get(IConstants.QTY));
			htkitbominsert.put("CHILDUOM", map.get("CHILDUOM"));
			htkitbominsert.put("CHILD_COST",  map.get("AVERAGEUNITCOST"));
			htkitbominsert.put(IDBConstants.CREATED_AT,DateUtils.getDateTime());
			htkitbominsert.put(IDBConstants.CREATED_BY,map.get("LOGIN_USER"));
			flag = processingReceiveDAO.addProcessingReceiveDet(htkitbominsert, (String)map.get(IConstants.PLANT));
        	
        	Hashtable htRecvDet = new Hashtable();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put("PONO","");
			htRecvDet.put("GRNO",map.get(IConstants.GRNO));
			htRecvDet.put(IDBConstants.ITEM, map.get(IDBConstants.ITEM));
			htRecvDet.put(IDBConstants.LOC, map.get(IDBConstants.LOC));
			htRecvDet.put("BATCH", map.get(IDBConstants.BATCH));
			htRecvDet.put("ORDQTY", map.get("CQTY"));
			htRecvDet.put("RECQTY", map.get("CQTY"));
			//htRecvDet.put("UNITCOST", map.get("AVERAGEUNITCOST"));
			//htRecvDet.put("CURRENCYID", map.get("CURRENCY"));
			//htRecvDet.put("UNITCOST", String.valueOf(pCostPerqty));
			htRecvDet.put("TRAN_TYPE", "DE-KITTING");
			htRecvDet.put(IDBConstants.RECVDATE, DateUtils.getDate());
			htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			flag = _RecvDetDAO.insertRecvDet(htRecvDet);
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
			htmov.put(IDBConstants.DIRTYPE, "DE-KITTING_IN");
			htmov.put(IDBConstants.LOC, map.get("LOC"));
			if((String)map.get("CHILDUOM")!=null);
			htmov.put(IDBConstants.UOM, map.get("CHILDUOM"));
			//htmov.put("Qty", map.get(IConstants.QTY));
			htmov.put("Qty", map.get("CQTY"));
			htmov.put(IDBConstants.ITEM, map.get("ITEM"));
			htmov.put("BATNO", map.get(IDBConstants.BATCH));
			htmov.put(IDBConstants.MOVTID, "IN");
			htmov.put(IDBConstants.RECID, "");
			htmov.put(IDBConstants.PONO, "");
			htmov.put("ORDNUM",map.get(IConstants.GRNO));
			htmov.put(IDBConstants.CREATED_BY, map.get("LOGIN_USER"));
			htmov.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
		    htmov .put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		  

			flag = movHisDao.insertIntoMovHis(htmov);

		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
}
