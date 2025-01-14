
package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RsnMst;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPalletization implements WmsTran, IMLogger {

	private MLogger mLogger = new MLogger();
	
	BomDAO bomDAO = new BomDAO();

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

	public boolean processWmsTran(Map map) throws Exception {
		return process(map);
	}

	@SuppressWarnings("unchecked")
	private boolean process(Map mapObject) throws Exception {
		try {
			
			MovHisDAO movHisDao = new MovHisDAO();
			InvMstDAO invMstDAO = new InvMstDAO();
			Boolean result = Boolean.TRUE;
			Boolean invResult = Boolean.TRUE;
			Boolean invChildResult = Boolean.TRUE;
			Boolean movhisResult = Boolean.TRUE;
			String queryUpdateBom="",queryUpdateInv="";
			String extCond="";
			double intQty = 0;
			int intParentQty=1;
			
			String strQty = "";
			
			Integer itemCount = new Integer(StrUtils.fString(
					(String) mapObject.get("ITEM_COUNTS")).trim());
		
			for (int i = 0; i < itemCount; i++) {
				
				strQty = (String) mapObject.get("QTY_"+i); 
				intQty = Double.parseDouble(strQty);
						
				bomDAO.setmLogger(mLogger);
				Hashtable htCheckBom = new Hashtable();
				htCheckBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
				htCheckBom.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
				htCheckBom.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
				htCheckBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
				htCheckBom.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"+ i));
				htCheckBom.put("CHILD_PRODUCT_LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
				htCheckBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
	
				//Check Data  Exists in BOM  if not exists then insert into BOM
				if (!bomDAO.isExisit(htCheckBom)) {
					
						Hashtable hrBomDao = new Hashtable();
						hrBomDao.put("PLANT", mapObject.get(IConstants.PLANT));
						hrBomDao.put("REMARKS", mapObject.get("REMARKS"));
						hrBomDao.put("PARENT_PRODUCT", mapObject
								.get("PARENT_PRODUCT"));
						hrBomDao.put("PARENT_PRODUCT_LOC", mapObject
								.get("PARENT_PRODUCT_LOC"));
						hrBomDao.put("PARENT_PRODUCT_BATCH", mapObject
								.get("PARENT_PRODUCT_BATCH"));
						hrBomDao.put("CREATE_BY", mapObject
								.get(IConstants.LOGIN_USER));
						hrBomDao.put("CREATE_AT", DateUtils.getDateTime());
						hrBomDao.put("STATUS", "A");
						hrBomDao.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"
								+ i));
						hrBomDao.put("CHILD_PRODUCT_LOC", mapObject
								.get("CHILD_PRODUCT_LOC_" + i));
						hrBomDao.put("CHILD_PRODUCT_BATCH", mapObject
								.get("CHILD_PRODUCT_BATCH_" + i ));
						hrBomDao.put("QTY", mapObject.get("QTY_" + i ));
						hrBomDao.put("KITTYPE", "PDA");
						hrBomDao.put("SCANITEM", mapObject.get("PRODUCT_NO_"+ i));
	    				result = result && bomDAO.insert(hrBomDao);
	    				
						if(result)	{
							
							//***Invetory process for  parent product, Check data exists or not
							//***If not exists then insert Parent Details into Inventory
							Hashtable htCheckBomparent = new Hashtable();
							htCheckBomparent.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							htCheckBomparent.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
							htCheckBomparent.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
							htCheckBomparent.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
							htCheckBomparent.put("STATUS", "A");
							
							int bomcount = bomDAO.getCount(htCheckBomparent);
							
							if (bomcount <= 1) 
							{
								Hashtable htInvParentBom = new Hashtable();
								htInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
								htInvParentBom.put("ITEM", mapObject.get("PARENT_PRODUCT"));
								htInvParentBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
								htInvParentBom.put("USERFLD4", mapObject.get("PARENT_PRODUCT_BATCH"));
						
								if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
									
									
									Hashtable htInsertInvParentBom = new Hashtable();
									htInsertInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
									htInsertInvParentBom.put("ITEM", mapObject.get("PARENT_PRODUCT"));
									htInsertInvParentBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
									htInsertInvParentBom.put("USERFLD4", mapObject.get("PARENT_PRODUCT_BATCH"));
									htInsertInvParentBom.put("QTY", mapObject.get("INV_QTY"));
												
									invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
									
									if (!invResult) {
										throw new Exception(
												"Unable to Process,Kitting Inventroy Inserting Parent Item Failed");
									} 
								
								}
								else
								{
									Hashtable htInvBom = new Hashtable();
									htInvBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
									
									htInvBom.put("ITEM",mapObject.get("PARENT_PRODUCT"));
									htInvBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
									htInvBom.put("USERFLD4",  mapObject.get("PARENT_PRODUCT_BATCH"));
									queryUpdateInv = "SET QTY" + " = QTY+" + mapObject.get("INV_QTY") + " ";
									
									invResult = invResult && invMstDAO.update(queryUpdateInv, htInvBom, extCond);
								
									if (!invResult) {
										throw new Exception(
												"Unable to process,Kitting Inventroy Updation Of Parent Product Failed");
										
									}	
							  }
							}
								//****Insert Into MovHistory for parent Item
								Hashtable htCylinderMOH = new Hashtable();
								
								htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
								htCylinderMOH.put("DIRTYPE", "KIT_PARENT");
								htCylinderMOH.put("QTY",mapObject.get("INV_QTY"));
								htCylinderMOH.put("MOVTID", "IN");
								htCylinderMOH.put("RECID", "");
								htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
								htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htCylinderMOH.put(IConstants.ITEM, mapObject.get("PARENT_PRODUCT"));
								htCylinderMOH.put("LOC",  mapObject.get("PARENT_PRODUCT_LOC"));
								htCylinderMOH.put("BATNO",mapObject.get("PARENT_PRODUCT_BATCH"));
							    htCylinderMOH.put("REMARKS", mapObject.get("REMARKS"));
								
								movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
													
							
							//Update Child Inventory 
							Hashtable htInvChildBom = new Hashtable();
							htInvChildBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							htInvChildBom.put("ITEM", mapObject.get("PRODUCT_NO_"+ i));
							htInvChildBom.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
							htInvChildBom.put("USERFLD4", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
							queryUpdateInv = "SET QTY" + " = QTY - " + intQty + " ";
							
							invChildResult = invChildResult && invMstDAO.update(queryUpdateInv, htInvChildBom, extCond);
							
							if (!invChildResult) {
								throw new Exception(
										"Unable to process,Kitting Inventroy Updating Of Child Product Failed");
							}
							
							htCylinderMOH.clear();
							htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
							htCylinderMOH.put("DIRTYPE", "ADD_KITTING");
							htCylinderMOH.put("QTY",mapObject.get(("QTY_" + i)));
							htCylinderMOH.put("MOVTID", "OUT");
							htCylinderMOH.put("RECID", "");
							htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
							htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htCylinderMOH.put(IConstants.ITEM, mapObject.get("PRODUCT_NO_" + i));
							htCylinderMOH.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+ i));
							htCylinderMOH.put("BATNO", mapObject.get("CHILD_PRODUCT_BATCH_"+ i));
						    htCylinderMOH.put("REMARKS",mapObject.get("PARENT_PRODUCT") + " "+ mapObject.get("REMARKS"));
							
							movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);

							
						}
						else if (!result) {
							throw new Exception("Unable to Process,Kitting BOM Inserting Item Failed");
						}
				}
				else
				{    
											
						//***Update BOM			
						Hashtable htUpdateBom = new Hashtable();
						htUpdateBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
						htUpdateBom.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
						htUpdateBom.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
						htUpdateBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
						htUpdateBom.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"+ i));
						htUpdateBom.put("CHILD_PRODUCT_LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
						htUpdateBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
						queryUpdateBom = "SET QTY" + " = QTY + " + intQty + " ";
						result = result && bomDAO.update(queryUpdateBom, htUpdateBom, extCond);
			
						if(result)	{
							
							//***Update Child Product Inventroy
							Hashtable htInvBom = new Hashtable();
							htInvBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							
							htInvBom.put("ITEM", mapObject.get("PRODUCT_NO_"+ i));
							htInvBom.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
							htInvBom.put("USERFLD4", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
							queryUpdateInv = "SET QTY" + " = QTY - " + intQty + " ";
							
							invResult = invResult && invMstDAO.update(queryUpdateInv, htInvBom, extCond);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process,Kitting Inventroy Updation Of Child Product Failed");
								
							}	
							
							//****Insert Into MovHistory
							Hashtable htCylinderMOH = new Hashtable();
							htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
							htCylinderMOH.put("DIRTYPE", "UPDATE_KITTING");
							htCylinderMOH.put("QTY",mapObject.get(("QTY_" + i)));
							htCylinderMOH.put("MOVTID", "OUT");
							htCylinderMOH.put("RECID", "");
							htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
							htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htCylinderMOH.put(IConstants.ITEM, mapObject.get("PRODUCT_NO_" + i));
							htCylinderMOH.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+ i));
							htCylinderMOH.put("BATNO", mapObject.get("CHILD_PRODUCT_BATCH_"+ i));
						    htCylinderMOH.put("REMARKS",mapObject.get("PARENT_PRODUCT") + " "+ mapObject.get("REMARKS"));
							
							movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
							
							
							//***Invetory process for  parent product, Check data exists or not
							//***If not exists then insert Parent Details into Inventory
							/*Hashtable htCheckBomparent = new Hashtable();
							htCheckBomparent.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							htCheckBomparent.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
							htCheckBomparent.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
							htCheckBomparent.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
							htCheckBomparent.put("STATUS", "A");
							
							int bomcount = bomDAO.getCount(htCheckBomparent);
							
							if (bomcount <= 1) 
							{
								Hashtable htInvParentBom = new Hashtable();
								htInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
								htInvParentBom.put("ITEM", mapObject.get("PARENT_PRODUCT"));
								htInvParentBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
								htInvParentBom.put("USERFLD4", mapObject.get("PARENT_PRODUCT_BATCH"));
						   
								if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
									
									
									Hashtable htInsertInvParentBom = new Hashtable();
									htInsertInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
									htInsertInvParentBom.put("ITEM", mapObject.get("PARENT_PRODUCT"));
									htInsertInvParentBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
									htInsertInvParentBom.put("USERFLD4", mapObject.get("PARENT_PRODUCT_BATCH"));
									htInsertInvParentBom.put("QTY", mapObject.get("INV_QTY"));
												
									invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
									
									if (!invResult) {
										throw new Exception(
												"Unable to Process,Kitting Inventroy Inserting Parent Item Failed");
									} 
								
								}
								else
								{
									Hashtable htInvUpdateBom = new Hashtable();
									htInvUpdateBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
									
									htInvUpdateBom.put("ITEM",mapObject.get("PARENT_PRODUCT"));
									htInvUpdateBom.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
									htInvUpdateBom.put("USERFLD4",  mapObject.get("PARENT_PRODUCT_BATCH"));
									queryUpdateInv = "SET QTY" + " = " + mapObject.get("INV_QTY") + " ";
									
									invResult = invResult && invMstDAO.update(queryUpdateInv, htInvUpdateBom, extCond);
								
									if (!invResult) {
										throw new Exception(
												"Unable to process,Kitting Inventroy Updation Of Parent Product Failed");
										
									}	
							  }
							}*/
								htCylinderMOH.clear();
								htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
								htCylinderMOH.put("DIRTYPE", "KIT_PARENT");
								htCylinderMOH.put("QTY",mapObject.get("INV_QTY"));
								htCylinderMOH.put("MOVTID", "IN");
								htCylinderMOH.put("RECID", "");
								htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
								htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htCylinderMOH.put(IConstants.ITEM, mapObject.get("PARENT_PRODUCT"));
								htCylinderMOH.put("LOC",  mapObject.get("PARENT_PRODUCT_LOC"));
								htCylinderMOH.put("BATNO",mapObject.get("PARENT_PRODUCT_BATCH"));
							    htCylinderMOH.put("REMARKS", mapObject.get("REMARKS"));
								
								movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
													
								
								
						
							
						}
						else if (!result) {
							throw new Exception(
									"Unable to process,Kitting BOM Updating Product Failed");
						}
			
				}
				
				
			}

			return result;
		} catch (Exception e) {
			throw e;
		}

	}
	

	
}
