package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsTOPicking implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsTOPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsTOPicking_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	private MLogger mLogger = new MLogger();
	TempUtil _TempUtil = new  TempUtil();

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

	public WmsTOPicking() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			
			
			flag = processInvMst(m); 

			if (flag) {

				flag = processTodetForPick(m);

			}

			if (flag) {

				{

					flag = processMovHis_OUT(m);

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						this.mLogger.exception(this.printLog, "", e);
					}
				}
				if (flag) {

					flag = processMovHis_IN(m);

				}

			}
			
			String type = 	m.get("TYPE").toString();
			if(flag && type.equalsIgnoreCase("TRRANDOM"))
			{
				flag = processTempRemove(m);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 16 2014, Bruhan, Description: To include Issue Date
	*/
	public boolean processTodetForPick(Map map) throws Exception {

		boolean flag = false;
		try {
			// update receive qty in podet
			ToDetDAO _ToDetDAO = new ToDetDAO();
			ToHdrDAO _ToHdrDAO = new ToHdrDAO();
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);

			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");

			// getQty from todet

			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("tono", map.get("TONO"));
			htCondiPoDet.put("tolnno", map.get("TOLNNO"));
			htCondiPoDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			

			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");

			Map mQty = _ToDetDAO.selectRow(query.toString(), htCondiPoDet);

			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double qtyPick = Double.parseDouble((String) mQty.get("qtyPick"));
			double tranQty = Double.parseDouble((String) map
					.get(IConstants.QTY));
			double sumqty = qtyPick + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			 
			String queryPoDet = "";
			String queryPoHdr = "";

			if (ordQty == sumqty) {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='C' ";

			} else {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='O' ";

			}
			// Added By Samatha extracond to Controll the Pick/Issue Qty Excced
			// the Order Qty Aug 24 1010
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "
			+ map.get(IConstants.QTY);

			flag = _ToDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			if (!flag) {
				throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
			}
			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("tono", map.get("TONO"));

				flag = _ToDetDAO.isExisit(htCondiPoHdr,
						"pickStatus in ('O','N')");

				if (flag)
					queryPoHdr = "set  STATUS='O',PickStaus='O' ";
				else
					queryPoHdr = "set STATUS='O',PickStaus='C' ";

				flag = _ToHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				Hashtable htPickDet = new Hashtable();
				Hashtable htInvMst = new Hashtable();
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				String extCond = "";
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
					extCond = "QTY > " + map.get(IConstants.QTY);
				}else{
					extCond = "QTY > 0";
				}
				InvMstDAO _InvMstDAO = new InvMstDAO();
				ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
					Iterator iterStock = alStock.iterator();
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
						htPickDet.clear();
						htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htPickDet.put("TONO", map.get(IConstants.TODET_TONUM));
						htPickDet.put("TOLNO", map.get(IConstants.TODET_TOLNNO));
						htPickDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
						htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
						htPickDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
						htPickDet.put("BATCH", map.get(IConstants.BATCH));
						htPickDet.put("FROMLOC", map.get(IConstants.FROMLOC));
						htPickDet.put("TOLOC", map.get(IConstants.TOLOC));
						htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC2));
						htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
						htPickDet.put("PICKQTY", String.valueOf(adjustedQuantity));
						htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
						htPickDet.put("REMARK" , map.get(IConstants.REMARKS));
						htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
						htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
						flag = _ToDetDAO.insertPickDet(htPickDet);
						quantityToAdjust -= adjustedQuantity;
					}
				}
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
		}

		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		boolean flag2=false;
		try {
			flag = processInvRemove(map);
			if (flag) {
				flag = processInvAdd(map);
				flag2=processBOM(map);
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;

	}
	
	//start code by Bruhan to delete line in temp table on 06 August 2013
	private boolean processTempRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", map.get(IConstants.PLANT));
			htdeletetemp.put("ORDERNO", map.get(IConstants.TODET_TONUM));
			htdeletetemp.put("ITEM", map.get(IConstants.ITEM));
			//htdeletetemp.put("LOC", map.get(IConstants.FROMLOC));
			//htdeletetemp.put("LOC1", map.get(IConstants.TOLOC));
			htdeletetemp.put("BATCH", map.get(IConstants.BATCH));
			
			double qty = Double.parseDouble((String)map.get(IConstants.PICKQTY));
			
			if(qty<0){
				flag = _TempUtil.deletetemp(htdeletetemp);}
			else{
				flag = true;
			}
			
			
			
			if(flag)
			{

			} else {
				throw new Exception("Error in deleting line in temp table");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Error in deleting line in temp table");
		}
		return flag;
	}
	
	//End code by Bruhan to delete line in temp table on 06 August 2013


	private boolean processInvRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();

			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
                        //String cond =" QTY >="+ map.get(IConstants.QTY);
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				extCond = "QTY > " + map.get(IConstants.QTY);
			}else{
				extCond = "QTY > 0";
			}
			flag = _InvMstDAO.isExisit(htInvMst,"" );
			if (flag) {
//				Get details in ascending order of CRAT for that batch
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				}
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
						htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
						htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
						htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
						htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));

						flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");
						if (!flag) {
							throw new Exception("Could not update");
						}
						quantityToAdjust -= adjustedQuantity;
					}
				}

			} else {
				throw new Exception("Not Enough Inventory found to Pick for Product: "+ map.get(IConstants.ITEM) + "  with Batch : "+ map.get(IConstants.BATCH)+ "  scanned at the location  "+map.get(IConstants.LOC) );
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			flag = _InvMstDAO.isExisit(htInvMst, "");
  		//get expiredate for inventory update
			   String expiredate="";
			   expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
			//get expiredate for inventory update end

			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY +'"
						+ map.get(IConstants.QTY) + "'");
				sql1.append("," +IDBConstants.EXPIREDATE + " = '"
						+ expiredate + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
			} else {
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				htInvMst.put(IDBConstants.EXPIREDATE, expiredate);
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

				flag = _InvMstDAO.insertInvMst(htInvMst);
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	
	
	private boolean processBOM(Map map) throws Exception {
		boolean flag = false;
		boolean itemflag=false;
		boolean bomupdateflag=false;
		String extcond="";
		
		ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		BomDAO _BomDAO =new BomDAO() ;
		
		try
		{
		/*Hashtable htItemMst= new Hashtable();
		htItemMst.clear();
		
		htItemMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		htItemMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		
		extcond=" userfld1='K'";
		
		itemflag = _ItemMstDAO .isExisit(htItemMst, extcond);
		
		if(itemflag)
		{*/
			Hashtable htBomMst = new Hashtable();
			htBomMst.clear();
			htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			htBomMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			flag = _BomDAO.isExisit(htBomMst);
			
			if(flag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));
		

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.PARENT_LOC + " ='"
						+ map.get(IConstants.LOC2) + "'");
				sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
						+ map.get(IConstants.LOC2) + "'");
				
				bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
				if(bomupdateflag)
				{   
					boolean invResult=false;
					boolean queryUpdateInv=false;
					
					
					InvMstDAO invMstDAO = new InvMstDAO();
					
					
					Hashtable htInvParentBom = new Hashtable();
					htInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
				    htInvParentBom.put("ITEM",  map.get(IConstants.ITEM));
					htInvParentBom.put("LOC", map.get(IConstants.LOC2));
					htInvParentBom.put("USERFLD4", map.get(IConstants.BATCH));
				
						if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
							
							Hashtable htInsertInvParentBom = new Hashtable();
							htInsertInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
							htInsertInvParentBom.put("ITEM", map.get(IConstants.ITEM));
							htInsertInvParentBom.put("LOC",  map.get(IConstants.LOC2));
							htInsertInvParentBom.put("USERFLD4", map.get(IConstants.BATCH));
							htInsertInvParentBom.put("QTY",  map.get("INV_QTY"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting transfer picking location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
				throw new Exception("Unable to process kitting transfer picking location transfer, Location not found");
			}
		}
		else
		{
			return bomupdateflag ;
		}
		*/
		return bomupdateflag;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

	}

	/* ************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
	*/
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.PIC_TO_OUT);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.FROMLOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			
			htRecvHis.put(IDBConstants.REMARKS , map.get(IConstants.REMARKS));

			flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 16 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
	*/
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {

			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", "PIC_TO_IN");
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS , map.get(IConstants.REMARKS));

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	

}