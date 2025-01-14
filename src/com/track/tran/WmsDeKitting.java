package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class WmsDeKitting implements WmsTran, IMLogger {
	
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
			Boolean resultBomDelete=Boolean.TRUE;
			Boolean resultInvDelete=Boolean.TRUE;
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
				
				//***Update Child Qty in BOM
				Hashtable htBom=new Hashtable();
				htBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
				htBom.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
				htBom.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
				htBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
				htBom.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"+ i));
				htBom.put("CHILD_PRODUCT_LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
				htBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
				
				queryUpdateBom = "SET QTY" + " = QTY - " + intQty + " ";
	
				result = result && bomDAO.update(queryUpdateBom, htBom, extCond);
				if (result) {
					
					
					
					//Main MovHis		
					Hashtable htMOH = new Hashtable();
					htMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
					htMOH.put("DIRTYPE", "DEKITTING");
					htMOH.put("QTY",mapObject.get(("QTY_" + i)));	
					htMOH.put("MOVTID", "IN");
					htMOH.put("RECID", "");
					htMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
					htMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htMOH.put(IConstants.ITEM, mapObject.get("PRODUCT_NO_" + i));
					htMOH.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+ i));
					htMOH.put("BATNO", mapObject.get("CHILD_PRODUCT_BATCH_"+ i));	
					htMOH.put("REMARKS",mapObject.get("PARENT_PRODUCT")+" "+(String) mapObject.get("REMARKS"));
							
					movhisResult = movhisResult && movHisDao.insertIntoMovHis(htMOH);
					
					
					
						//***Update Child Qty in INV
						//***Invetory process for  parent product, Check child data exists or not
						//***If not exists then insert Child Details into Inventory
						Hashtable htInvParentBom = new Hashtable();
						htInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
					     htInvParentBom.put("ITEM", mapObject.get("PRODUCT_NO_"+ i));
						htInvParentBom.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
						htInvParentBom.put("USERFLD4", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
						
						//get expirydate from inventory
						  String  expiredate= invMstDAO.getInvExpiryDatekitting((String) mapObject.get(IConstants.PLANT),(String)  mapObject.get("PRODUCT_NO_"+ i),(String) mapObject.get("CHILD_PRODUCT_BATCH_"+i));
				    	//get expirydat from inventory end
					
						if (!invMstDAO.isExisit(htInvParentBom,"")) {
							
						
							
							Hashtable htInsertInvParentBom = new Hashtable();
							htInsertInvParentBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							htInsertInvParentBom.put("ITEM", mapObject.get("PRODUCT_NO_"+ i));
							htInsertInvParentBom.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
							htInsertInvParentBom.put("USERFLD4", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
							htInsertInvParentBom.put("QTY",mapObject.get(("QTY_" + i)));
							htInsertInvParentBom.put("EXPIREDATE",expiredate);
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to Process,Kitting Inventroy Inserting Parent Item Failed");
							} 
						
					   }
					   else
					   {
						    //Update Inventory
							Hashtable htInvChildBom = new Hashtable();
							htInvChildBom.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
							htInvChildBom.put("ITEM", mapObject.get("PRODUCT_NO_"+ i));
							htInvChildBom.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
							htInvChildBom.put("USERFLD4", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
							//queryUpdateInv = "SET QTY" + " = QTY + " + intQty + ", EXPIREDATE= ";
							
							StringBuffer sql1 = new StringBuffer(" SET ");
							sql1.append(IDBConstants.QTY + " = QTY +'" + intQty + "'");
							sql1.append("," +IDBConstants.EXPIREDATE + " = '" + expiredate + "'");
							
							;
							
							invChildResult = invChildResult && invMstDAO.update(sql1.toString(), htInvChildBom, extCond);
							
							if (!invChildResult) {
								throw new Exception(
										"Unable to process,Kitting Inventroy Updating Of Child Product Failed");
							}
					   }
					
						//***delet Qty In  BOM if qty less then or equal to zero				
						bomDAO.setmLogger(mLogger);
						
						Hashtable htCheckBomQty = new Hashtable();
						htCheckBomQty.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
						htCheckBomQty.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
						htCheckBomQty.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
						htCheckBomQty.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
						htCheckBomQty.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"+ i));
						htCheckBomQty.put("CHILD_PRODUCT_LOC", mapObject.get("CHILD_PRODUCT_LOC_"+i));
						htCheckBomQty.put("CHILD_PRODUCT_BATCH", mapObject.get("CHILD_PRODUCT_BATCH_"+i));
			
					
						if (!bomDAO.isExisitQty(htCheckBomQty)) {
							   //***If Qty less then 0 then Delete
								Hashtable hrBomDelete = new Hashtable();
								hrBomDelete.put("PLANT", mapObject.get(IConstants.PLANT));
								hrBomDelete.put("PARENT_PRODUCT", mapObject
										.get("PARENT_PRODUCT"));
								hrBomDelete.put("PARENT_PRODUCT_LOC", mapObject
										.get("PARENT_PRODUCT_LOC"));
								hrBomDelete.put("PARENT_PRODUCT_BATCH", mapObject
										.get("PARENT_PRODUCT_BATCH"));
								
								hrBomDelete.put("CHILD_PRODUCT", mapObject.get("PRODUCT_NO_"
										+ i));
								hrBomDelete.put("CHILD_PRODUCT_LOC", mapObject
										.get("CHILD_PRODUCT_LOC_" + i));
								hrBomDelete.put("CHILD_PRODUCT_BATCH", mapObject
										.get("CHILD_PRODUCT_BATCH_" + i ));
								
			    				resultBomDelete = resultBomDelete  && bomDAO.delete(hrBomDelete);
			    				
								if(!resultBomDelete)	{
									
									throw new Exception(
									"Unable to process,Kitting BOM Deleting Of Child Product Failed");
									
								}
								else
								{
									//***Delete Entry in MovHis
									//Hashtable htMOH = new Hashtable();
									htMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
									htMOH.put("DIRTYPE", "DELETE_DEKITTING_BOM");
									htMOH.put("QTY",mapObject.get(("QTY_" + i)));
									htMOH.put("MOVTID", "");
									htMOH.put("RECID", "");		
									htMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
									htMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMOH.put(IConstants.ITEM, mapObject.get("PRODUCT_NO_" + i));
									htMOH.put("LOC", mapObject.get("CHILD_PRODUCT_LOC_"+ i));
									htMOH.put("BATNO", mapObject.get("CHILD_PRODUCT_BATCH_"+ i));
									htMOH.put("REMARKS", (String)mapObject.get("PARENT_PRODUCT") + " "+ (String) mapObject.get("REMARKS"));
									
									movhisResult = movhisResult && movHisDao.insertIntoMovHis(htMOH);
									
								}
						      								
						    	//**Check Parent have child in BOM,if not then delete parent  in invmst
														
					        	Hashtable htinvQty = new Hashtable();
					        	htinvQty.put(IConstants.PLANT, mapObject.get(IConstants.PLANT));
								htinvQty.put("PARENT_PRODUCT", mapObject.get("PARENT_PRODUCT"));
								htinvQty.put("PARENT_PRODUCT_LOC", mapObject.get("PARENT_PRODUCT_LOC"));
								htinvQty.put("PARENT_PRODUCT_BATCH", mapObject.get("PARENT_PRODUCT_BATCH"));
									
								if (!bomDAO.isExisitInvQty(htinvQty)) {
									
									   
										Hashtable hrInvDelete = new Hashtable();
										hrInvDelete.put("PLANT", mapObject.get(IConstants.PLANT));
										hrInvDelete.put("ITEM", mapObject.get("PARENT_PRODUCT"));
										hrInvDelete.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
										hrInvDelete.put("USERFLD4", mapObject.get("PARENT_PRODUCT_BATCH"));
																				
					    				resultInvDelete = resultInvDelete  && invMstDAO. delete(hrInvDelete );
					    			
										if(!resultInvDelete)	{
											
											throw new Exception(
											"Unable to process,Kitting Inventory Deleting Of Parent Product Failed");
											
										}
										else
										{
											//***Delete Entry in MovHis
											
											htMOH.put(IDBConstants.PLANT, mapObject.get(IConstants.PLANT));
											htMOH.put("DIRTYPE", "DELETE_DEKITTING_INV");
											htMOH.put("QTY",mapObject.get("INV_QTY"));
											htMOH.put(IDBConstants.MOVTID, "OUT");
											htMOH.put(IDBConstants.RECID, "");	
											htMOH.put(IDBConstants.CREATED_BY, mapObject.get(IConstants.LOGIN_USER));
											htMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
											htMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htMOH.put(IConstants.ITEM,  mapObject.get("PARENT_PRODUCT"));		
											htMOH.put("LOC", mapObject.get("PARENT_PRODUCT_LOC"));
											htMOH.put("BATNO", mapObject.get("PARENT_PRODUCT_BATCH"));
											htMOH.put("REMARKS", mapObject.get("REMARKS"));
											
											movhisResult = movhisResult && movHisDao.insertIntoMovHis(htMOH);
											
										}
								}	
							
							
						}
					
						else if (!result) 
						{
							throw new Exception("Unable to Process,DeKitting Updating Of BOM Product Failed");
						}
				
				}
				
				
			}

			return result;
		} catch (Exception e) {
			throw e;
		}

	}

}
