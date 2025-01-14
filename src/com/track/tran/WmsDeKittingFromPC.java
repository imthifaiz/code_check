package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ProductionBomDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class WmsDeKittingFromPC implements WmsTran, IMLogger {
	
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
			ProductionBomDAO ProdBomDao = new ProductionBomDAO();
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
			
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)mapObject.get(IDBConstants.PLANT),(String)mapObject.get(IDBConstants.PARENTITEM));
	        String parentnonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
	        Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails((String)mapObject.get(IDBConstants.PLANT),(String)mapObject.get(IDBConstants.CHILDITEM));
	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));

			String pqty = ProdBomDao.getkittingparentqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());
			String invpqty = ProdBomDao.getkittingparentinvqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());
			
				strQty = (String) mapObject.get("QTY"); 
				intQty = Double.parseDouble(strQty);
				
				
				//***delete child item in BOM			
				bomDAO.setmLogger(mLogger);
				
				Hashtable htinvQty = new Hashtable();
	        	htinvQty.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
				htinvQty.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
				htinvQty.put("KONO",mapObject.get("KONO"));								
				
				ArrayList bomQryList = bomDAO.selectForReport("SELECT distinct CHILD_PRODUCT FROM "+(String)mapObject.get(IDBConstants.PLANT)+"_BOM WHERE PLANT='"+(String)mapObject.get(IDBConstants.PLANT)+"'", htinvQty, "");
				int bomcount = bomQryList.size();
				
				Hashtable htCheckBom = new Hashtable();
				htCheckBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
				htCheckBom.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
				htCheckBom.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
				htCheckBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
				htCheckBom.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
				htCheckBom.put("CHILD_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
				htCheckBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CBATCH"));
				htCheckBom.put("KONO", mapObject.get("KONO"));
	
				if (bomDAO.isExisit(htCheckBom)) {
					   //***If child exits then delete from bom
						Hashtable hrBomDelete = new Hashtable();
						hrBomDelete.put("PLANT", mapObject.get(IDBConstants.PLANT));
						hrBomDelete.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
						hrBomDelete.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						hrBomDelete.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
						hrBomDelete.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
						hrBomDelete.put("CHILD_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						hrBomDelete.put("CHILD_PRODUCT_BATCH", mapObject.get("CBATCH"));
						hrBomDelete.put("KONO", mapObject.get("KONO"));
						//hrBomDelete.put("QTY", mapObject.get(IDBConstants.QTY));

						result = result  && bomDAO.delete(hrBomDelete);
	    				
						if(!result)	{
							
							throw new Exception(
							"Unable to process,Kitting BOM Deleting Of Child Product Failed");
							
						}
						else
						{
							//***Update Child Qty in INV
							//***If not exists then insert Child Details into Inventory
							if(!childnonstocktype.equalsIgnoreCase("Y"))
							{
								Hashtable htInvchildBom = new Hashtable();
								htInvchildBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htInvchildBom.put("ITEM", mapObject.get("INVITEM"));
								htInvchildBom.put("LOC", mapObject.get(IDBConstants.LOC));
								htInvchildBom.put("USERFLD4", mapObject.get("CBATCH"));
							
							//get expirydate from inventory
								String  expiredate= invMstDAO.getInvExpiryDatekitting((String) mapObject.get(IConstants.PLANT),(String)  mapObject.get(IDBConstants.CHILDITEM),(String) mapObject.get("CBATCH"));
					    	
								if (!invMstDAO.isExisit(htInvchildBom,"")) {
								
									Hashtable htInsertInvchildBom = new Hashtable();
									htInsertInvchildBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									htInsertInvchildBom.put("ITEM", mapObject.get("INVITEM"));
									htInsertInvchildBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInsertInvchildBom.put("USERFLD4", mapObject.get("CBATCH"));
									double convqty = Double.parseDouble((String) mapObject.get(("QTY"))) *  Double.parseDouble((String) mapObject.get("CUOMQTY"));
									htInsertInvchildBom.put("QTY", String.valueOf(convqty));
									htInsertInvchildBom.put("EXPIREDATE",expiredate);
											
									invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvchildBom);
								
									if (!invResult) {
										throw new Exception(
											"Unable to Process,Kitting Inventroy Inserting Parent Item Failed");
									} 
							
								}
								else
								{
							    //Update Inventory
									Hashtable htInvChildBom = new Hashtable();
									htInvChildBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									htInvChildBom.put("ITEM", mapObject.get("INVITEM"));
									htInvChildBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInvChildBom.put("USERFLD4", mapObject.get("CBATCH"));
								
									StringBuffer sql1 = new StringBuffer(" SET ");
									double convqty = intQty *  Double.parseDouble((String) mapObject.get("CUOMQTY"));
									sql1.append(IDBConstants.QTY + " = QTY +'" + convqty + "'");
									sql1.append("," +IDBConstants.EXPIREDATE + " = '" + expiredate + "'");
									
									invChildResult = invChildResult && invMstDAO.update(sql1.toString(), htInvChildBom, extCond);
								
									if (!invChildResult) {
										throw new Exception(
											"Unable to process,Kitting Inventroy Updating Of Child Product Failed");
									}
								}
							}
							//***Delete Entry in MovHis
							Hashtable htMOH = new Hashtable();
							htMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htMOH.put("DIRTYPE", "DELETE_DEKITTING_BOM");
							htMOH.put("QTY",mapObject.get("QTY"));
							htMOH.put("MOVTID", "");
							htMOH.put("RECID", "");
							htMOH.put("ORDNUM",mapObject.get("KONO"));
							htMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
							htMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMOH.put(IConstants.ITEM, mapObject.get(IDBConstants.CHILDITEM));
							htMOH.put("LOC", mapObject.get(IDBConstants.LOC));
							htMOH.put("BATNO", mapObject.get("CBATCH"));
							htMOH.put("REMARKS", (String)mapObject.get(IDBConstants.PARENTITEM) + " "+ (String) mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE)+" "+mapObject.get(IDBConstants.EMPNO));
							
							movhisResult = movhisResult && movHisDao.insertIntoMovHis(htMOH);
							
						}
				      								

						//**Check Parent have child in BOM,if not then delete parent  in invmst
								if (Double.parseDouble(invpqty) > 0) {						
					        	
								ArrayList prtQryList = ProdBomDao.getProdBomDetails((String)mapObject.get(IDBConstants.PARENTITEM),(String)mapObject.get(IDBConstants.PLANT), " AND BOMTYPE='KIT'");
								if (bomcount == prtQryList.size()) {								
									if(!parentnonstocktype.equalsIgnoreCase("Y"))
									{
										Hashtable hrInvupdate = new Hashtable();
										hrInvupdate.put("PLANT", mapObject.get(IDBConstants.PLANT));
										hrInvupdate.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
										hrInvupdate.put("LOC", mapObject.get(IDBConstants.LOC));
										hrInvupdate.put("USERFLD4", mapObject.get("PBATCH"));
										double convqty = Double.parseDouble(invpqty) *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
										queryUpdateInv = "SET QTY=QTY-"+convqty ;
										
										invResult = invResult && invMstDAO.update(queryUpdateInv, hrInvupdate, extCond);
										if(invResult)	{
										String queryUpdateInvBom = "SET INVQTY =INVQTY-" +invpqty ;
										bomDAO.update(queryUpdateInvBom, htinvQty, "");
										}
										if(!invResult)	{
											
											throw new Exception(
											"Unable to process,Kitting Inventory updating Of Parent Product Failed");
											
										}									
										else
										{
											//***Delete Entry in MovHis
											Hashtable htmovhis = new Hashtable();
											htmovhis.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
											htmovhis.put("DIRTYPE", "DELETE_DEKITTING_INV");
											htmovhis.put("QTY","1");
											htmovhis.put(IDBConstants.MOVTID, "OUT");
											htmovhis.put(IDBConstants.RECID, "");
											htmovhis.put("ORDNUM",mapObject.get("KONO"));
											htmovhis.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
											htmovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
											htmovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											htmovhis.put(IConstants.ITEM,  mapObject.get(IDBConstants.PARENTITEM));		
											htmovhis.put("LOC", mapObject.get(IDBConstants.LOC));
											htmovhis.put("BATNO", mapObject.get("PBATCH"));
											htmovhis.put("REMARKS", mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE)+" "+mapObject.get(IDBConstants.EMPNO));
											
											movhisResult = movhisResult && movHisDao.insertIntoMovHis(htmovhis);
											
										}
									}
								
								}
								}
					
	                 	}
						else if (!result) 
							{
								throw new Exception("Unable to Process,DeKitting Updating Of BOM Product Failed");
							}
				
				
		return result;
		} catch (Exception e) {
			throw e;
		}

	}

}
