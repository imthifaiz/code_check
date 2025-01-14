
package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ProductionBomDAO;
import com.track.dao.RsnMst;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsKittingFromPC implements WmsTran, IMLogger {

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
			Boolean invResult = Boolean.TRUE;
			Boolean invChildResult = Boolean.TRUE;
			Boolean movhisResult = Boolean.TRUE;
			String queryUpdateBom="",queryUpdateInv="";
			String extCond="";
			double intQty = 0;
			//int intParentQty=1;
			
			String strQty = "";
			String childqty="";
			
			//Integer itemCount = new Integer(StrUtils.fString((String) mapObject.get("ITEM_COUNTS")).trim());
		
			//for (int i = 0; i < itemCount; i++) {
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)mapObject.get(IDBConstants.PLANT),(String)mapObject.get(IDBConstants.PARENTITEM));
	        String parentnonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
	       
	        Map mPrddetchild = new ItemMstDAO().getProductNonStockDetails((String)mapObject.get(IDBConstants.PLANT),(String)mapObject.get(IDBConstants.CHILDITEM));
	        String childnonstocktype= StrUtils.fString((String) mPrddetchild.get("NONSTKFLAG"));

				
				strQty = (String) mapObject.get(IDBConstants.QTY); 
				intQty = Double.parseDouble(strQty);
						
				bomDAO.setmLogger(mLogger);
				Hashtable htCheckBom = new Hashtable();
				htCheckBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
				htCheckBom.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
				htCheckBom.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
				htCheckBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
				htCheckBom.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
				htCheckBom.put("CHILD_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
				htCheckBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CBATCH"));
				htCheckBom.put("KONO", mapObject.get("KONO"));
	
								//Check Data  Exists in BOM  if not exists then insert into BOM
				if (!bomDAO.isExisit(htCheckBom)) {
					
					childqty = ProdBomDao.getkittingchildqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get(IDBConstants.CHILDITEM).toString(),mapObject.get("KONO").toString());
					String pqty = ProdBomDao.getkittingparentqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());

						Hashtable hrBomDao = new Hashtable();
						hrBomDao.put("PLANT", mapObject.get(IDBConstants.PLANT));
						hrBomDao.put("EMPNO", mapObject.get(IDBConstants.EMPNO));
						hrBomDao.put("LOC_TYPE_ID", mapObject.get(IDBConstants.LOCTYPEID));
						hrBomDao.put("RSNCODE", mapObject.get(IDBConstants.RSNCODE));
						hrBomDao.put("REMARKS", mapObject.get(IDBConstants.REMARKS));
						hrBomDao.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
						hrBomDao.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						hrBomDao.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
						hrBomDao.put("PARENT_PRODUCT_QTY", mapObject.get("PQTY"));
						hrBomDao.put("TRANQTY", mapObject.get("TRANQTY"));
						hrBomDao.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
						hrBomDao.put("CHILD_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						hrBomDao.put("CHILD_PRODUCT_BATCH", mapObject.get("CBATCH"));
						hrBomDao.put("QTY", mapObject.get(IDBConstants.QTY));
						hrBomDao.put("KITTYPE", mapObject.get("KITTYPE"));
						hrBomDao.put("CREATE_BY", mapObject.get(IDBConstants.LOGIN_USER));
						hrBomDao.put("CREATE_AT", DateUtils.getDateTime());
						hrBomDao.put("STATUS", "A");
						hrBomDao.put("SCANITEM", mapObject.get("SCANITEM"));
						hrBomDao.put("PARENTUOM",mapObject.get("PARENTUOM"));
						hrBomDao.put("CHILDUOM",mapObject.get("CHILDUOM"));
						hrBomDao.put("KONO",mapObject.get("KONO"));
						hrBomDao.put("INVQTY","0");
						String ORDDATE = (String) mapObject.get("ORDDATE");
						if(ORDDATE.equalsIgnoreCase(""))
							hrBomDao.put(IDBConstants.ORDERDATE, (DateUtils.getDate()));
						else
							hrBomDao.put(IDBConstants.ORDERDATE, ORDDATE);	
	    				result = result && bomDAO.insert(hrBomDao);
	    				
						if(result)	{
							
    					    double sumqty = Double.parseDouble(childqty) + intQty;
							double tranqty = Double.parseDouble((String) mapObject.get("TRANQTY"));
							double parentqty = Double.parseDouble((String) mapObject.get("PQTY"));
							double ptqty = Double.parseDouble(pqty);
							double updatepqty;
							if(parentqty==ptqty)
							{
								updatepqty = parentqty;
							}
							else
							{
								if(parentqty>ptqty)
								{
									updatepqty = parentqty-ptqty;
								}
								else
								{
									updatepqty = ptqty;
									parentqty = ptqty;
								}
							}
							
							String updatebom="";
							
							if (tranqty == sumqty) 
							{
								updatebom = "SET SCANSTATUS='C',PARENT_PRODUCT_QTY="+parentqty+",TRANQTY="+tranqty;
							}
							else
							{
								updatebom = "SET SCANSTATUS='O',PARENT_PRODUCT_QTY="+parentqty+",TRANQTY="+tranqty;
							}
							
							Hashtable htCond = new Hashtable();
							htCond.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htCond.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
							htCond.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
							htCond.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
							htCond.put("KONO", mapObject.get("KONO"));
							if(parentqty<=ptqty)
							{
								htCond.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
							}
							
							result = bomDAO.update(updatebom, htCond, extCond);
							
							//****Insert Into MovHistory for parent Item
							
							Hashtable htInvParent = new Hashtable();
							htInvParent.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htInvParent.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
							htInvParent.put("LOC", mapObject.get(IDBConstants.LOC));
							htInvParent.put("USERFLD4", mapObject.get("PBATCH"));
							
							Hashtable htCylinderMOH = new Hashtable();
							
								htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htCylinderMOH.put("DIRTYPE", "KIT_PARENT");
								htCylinderMOH.put(IConstants.ITEM, mapObject.get(IDBConstants.PARENTITEM));
								htCylinderMOH.put("LOC",  mapObject.get(IDBConstants.LOC));
								htCylinderMOH.put("BATNO",mapObject.get("PBATCH"));
								htCylinderMOH.put("ORDNUM",mapObject.get("KONO"));
								htCylinderMOH.put("QTY",mapObject.get("PQTY"));
								htCylinderMOH.put("MOVTID", "IN");
								htCylinderMOH.put("RECID", "");
								htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
								htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htCylinderMOH.put("REMARKS", mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE)+" "+mapObject.get(IDBConstants.EMPNO)+" "+mapObject.get("KITTYPE"));
							if(parentnonstocktype.equalsIgnoreCase("Y"))
								{
									movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
							    }
							else
							{
								if (!invMstDAO.isExisitKittingQty(htInvParent,"")) 
								{
									movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
								}
							}
							
							//Invetory process for  parent product, Check data exists or not
							///If not exists then insert Parent Details into Inventory
							if(!parentnonstocktype.equalsIgnoreCase("Y"))
							{
								Hashtable htCheckBomparent = new Hashtable();
								htCheckBomparent.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htCheckBomparent.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
								htCheckBomparent.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
								htCheckBomparent.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
								htCheckBomparent.put("PARENT_PRODUCT_QTY", pqty);
								htCheckBomparent.put("KONO", mapObject.get("KONO"));
								htCheckBomparent.put("STATUS", "A");
								
								//int bomcount = bomDAO.getCount(htCheckBomparent);
								boolean prtinv = false;
								String XQTY =(String)mapObject.get("PQTY");
								String YQTY=(String)mapObject.get("PQTY");
								ArrayList prtQryList = ProdBomDao.getProdBomDetails((String)mapObject.get(IDBConstants.PARENTITEM),(String)mapObject.get(IDBConstants.PLANT), " AND BOMTYPE='KIT'");
								if (prtQryList.size() > 0) {					        	       
						            for (int iCnt =0; iCnt<prtQryList.size(); iCnt++){
						            	Map lineArr = (Map) prtQryList.get(iCnt);
						            	String childvitem = StrUtils.fString((String)lineArr.get("CITEM"));
						            	String childpqty = StrUtils.fString((String)lineArr.get("QTY"));
						            	double ctrnspqty =  Double.parseDouble((String)mapObject.get("PQTY")) *  Double.parseDouble(childpqty);
						            	Hashtable htCheckBomCH = new Hashtable();
						            	htCheckBomCH.put("CHILD_PRODUCT", childvitem);
						            	htCheckBomCH.put("KONO", mapObject.get("KONO"));
						            	ArrayList childQryList = bomDAO.selectForReport("SELECT ISNULL(SUM(ISNULL(QTY,0)),0) QTY FROM "+(String)mapObject.get(IDBConstants.PLANT)+"_BOM WHERE PLANT='"+(String)mapObject.get(IDBConstants.PLANT)+"'", htCheckBomCH, "");
						            	if (childQryList.size() > 0) {					        	       
								            for (int yCnt =0; yCnt<childQryList.size(); yCnt++){
								            	Map clineArr = (Map) childQryList.get(yCnt);								            	
								            	String childcqty = StrUtils.fString((String)clineArr.get("QTY"));
								            	if(Double.parseDouble(childcqty)>0) {
								            	if(Double.parseDouble(childcqty)<ctrnspqty)
								            	{
								            		double newchildqty = Double.parseDouble(childcqty)/Double.parseDouble(childpqty);
								            		int vint = (int)(newchildqty);
								            		XQTY = String.valueOf(vint);
								            		if(Double.parseDouble(XQTY)<Double.parseDouble(YQTY))
								            		{
								            			YQTY=XQTY;
								            			prtinv = true;
								            		}
								            		else
													{
														prtinv = false;
														//break;
													}
								            	}
								            	else
								            	{
								            		if(Double.parseDouble(childcqty)==ctrnspqty)
								            		prtinv = true;
								            	}
								            	}
								            	else
												{
								            		YQTY="0";
													prtinv = false;
													break;
												}
								            }
						            	}
						            	else
										{
											prtinv = false;
											//break;
										}
						            }
								}
								else
								{
									prtinv = false;
								}
								if (prtinv)
								//if (bomcount <= 1) 
								{
									
									String invpqty = ProdBomDao.getkittingparentinvqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());									
									YQTY= String.valueOf(Double.parseDouble(YQTY)-Double.parseDouble(invpqty));
									if (Double.parseDouble(YQTY) > 0) {
									Hashtable htInvParentBom = new Hashtable();
									htInvParentBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									htInvParentBom.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
									htInvParentBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInvParentBom.put("USERFLD4", mapObject.get("PBATCH"));
						
									if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
									
										Hashtable htInsertInvParentBom = new Hashtable();
										htInsertInvParentBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
										htInsertInvParentBom.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
										htInsertInvParentBom.put("LOC", mapObject.get(IDBConstants.LOC));
										htInsertInvParentBom.put("USERFLD4", mapObject.get("PBATCH"));
										//double convqty =  Double.parseDouble((String)mapObject.get("PQTY")) *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
										double convqty =  Double.parseDouble(YQTY) *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
										htInsertInvParentBom.put("QTY", String.valueOf(convqty));
										htInsertInvParentBom.put("CRAT", DateUtils.getDateTime());
												
										invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
										
										String queryUpdateInvBom = "SET INVQTY =INVQTY+" +YQTY ;
										bomDAO.update(queryUpdateInvBom, htCheckBomparent, "");
									
									if (!invResult) {
										throw new Exception(
												"Unable to Process,Kitting Inventory Inserting Parent Item Failed");
									} 
								
								}
							
								else
								{
									updatepqty = Double.parseDouble(YQTY);
									
									Hashtable htInvBom = new Hashtable();
									htInvBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									
									htInvBom.put("ITEM",mapObject.get(IDBConstants.PARENTITEM));
									htInvBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInvBom.put("USERFLD4",  mapObject.get("PBATCH"));
									double convqty = updatepqty *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
									queryUpdateInv = "SET QTY =QTY+" +convqty ;
									
									invResult = invResult && invMstDAO.update(queryUpdateInv, htInvBom, extCond);
									
									String queryUpdateInvBom = "SET INVQTY =INVQTY+" +YQTY ;
									bomDAO.update(queryUpdateInvBom, htCheckBomparent, "");
								
									if (!invResult) {
										throw new Exception(
												"Unable to process,Kitting Inventory Updation Of Parent Product Failed");
										
									}	
							  }
									}
							}
						}
												
							
							//Update Child Inventory 
							if(!childnonstocktype.equalsIgnoreCase("Y"))
							{
								Hashtable htInvChildBom = new Hashtable();
								htInvChildBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htInvChildBom.put("ITEM", mapObject.get("INVITEM"));
								htInvChildBom.put("LOC", mapObject.get(IDBConstants.LOC));
								htInvChildBom.put("USERFLD4", mapObject.get("CBATCH"));
								double convqty = intQty *  Double.parseDouble((String) mapObject.get("CUOMQTY"));
								queryUpdateInv = "SET QTY" + " = QTY - " + convqty + " ";
							
								invChildResult = invChildResult && invMstDAO.update(queryUpdateInv, htInvChildBom, extCond);
							
								if (!invChildResult) {
									throw new Exception(
										"Unable to process,Kitting Inventory Updating Of Child Product Failed");
								}
							}
							htCylinderMOH.clear();
							htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htCylinderMOH.put("DIRTYPE", "ADD_KITTING");
							htCylinderMOH.put("QTY",mapObject.get(IDBConstants.QTY));
							htCylinderMOH.put("MOVTID", "OUT");
							htCylinderMOH.put("RECID", "");
							htCylinderMOH.put("ORDNUM",mapObject.get("KONO"));
							htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
							htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htCylinderMOH.put(IConstants.ITEM, mapObject.get("SCANITEM"));
							htCylinderMOH.put("LOC", mapObject.get(IDBConstants.LOC));
							htCylinderMOH.put("BATNO", mapObject.get("CBATCH"));
						    htCylinderMOH.put("REMARKS",mapObject.get(IDBConstants.PARENTITEM) + " "+ mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE) + " "+mapObject.get(IDBConstants.EMPNO)+ " "+mapObject.get("EQUIITEM")+ " "+mapObject.get("KITTYPE"));
							
							movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);

							
						}
						else if (!result) {
							throw new Exception("Unable to Process,Kitting BOM Inserting Item Failed");
						}
				}
				else
				{    
						
					    childqty = ProdBomDao.getkittingchildqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get(IDBConstants.CHILDITEM).toString(),mapObject.get("KONO").toString());
					    String pqty = ProdBomDao.getkittingparentqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());
					    String invpqty = ProdBomDao.getkittingparentinvqtywithkono(mapObject.get(IDBConstants.PLANT).toString(),mapObject.get(IDBConstants.PARENTITEM).toString(),mapObject.get("PBATCH").toString(),mapObject.get("KONO").toString());
					    
					    double sumqty = Double.parseDouble(childqty) + intQty;
						double tranqty = Double.parseDouble((String) mapObject.get("TRANQTY"));
						double parentqty = Double.parseDouble((String) mapObject.get("PQTY"));
						double ptqty = Double.parseDouble(pqty);
						double updatepqty;
						if(parentqty==ptqty)
						{
							updatepqty = parentqty;
						}
						else
						{
							if(parentqty>ptqty)
							{
								updatepqty = parentqty-ptqty;
							}
							else
							{
								updatepqty = ptqty;
								parentqty = ptqty;
							}
							 
						}
						String updatebom="";
						
						if (tranqty == sumqty) 
						{
							updatebom = "SET SCANSTATUS='C',PARENT_PRODUCT_QTY="+parentqty+",TRANQTY="+tranqty;
						}
						else
						{
							updatebom = "SET SCANSTATUS='O',PARENT_PRODUCT_QTY="+parentqty+",TRANQTY="+tranqty;
						}
						//***Update BOM			
						Hashtable htUpdateBom = new Hashtable();
						htUpdateBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
						htUpdateBom.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
						htUpdateBom.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						htUpdateBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
						htUpdateBom.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
						htUpdateBom.put("CHILD_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
						htUpdateBom.put("CHILD_PRODUCT_BATCH", mapObject.get("CBATCH"));
						htUpdateBom.put("KONO", mapObject.get("KONO"));
						queryUpdateBom = "SET QTY" + " = QTY + " + intQty + ",SCANITEM='"+mapObject.get("SCANITEM")+"'";
						result = result && bomDAO.update(queryUpdateBom, htUpdateBom, extCond);
												
						
						if(result)	{
							
							
							Hashtable htCond = new Hashtable();
							htCond.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htCond.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
							htCond.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
							htCond.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
							htCond.put("KONO", mapObject.get("KONO"));
							if(parentqty<=ptqty)
							{
								htCond.put("CHILD_PRODUCT", mapObject.get(IDBConstants.CHILDITEM));
							}
							
							result = bomDAO.update(updatebom, htCond, extCond);
														
							//***Update Child Product Inventory
							if(!childnonstocktype.equalsIgnoreCase("Y"))
							{
								Hashtable htInvBom = new Hashtable();
								htInvBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							
								htInvBom.put("ITEM", mapObject.get("INVITEM"));
								htInvBom.put("LOC", mapObject.get(IDBConstants.LOC));
								htInvBom.put("USERFLD4", mapObject.get("CBATCH"));
								double convqty = intQty *  Double.parseDouble((String) mapObject.get("CUOMQTY"));
								queryUpdateInv = "SET QTY" + " = QTY - " + convqty + " ";
							
								invResult = invResult && invMstDAO.update(queryUpdateInv, htInvBom, extCond);
							
								if (!invResult) {
									throw new Exception(
										"Unable to process,Kitting Inventory Updation Of Child Product Failed");
								
								}	
							}
							
							
							//****Insert Into MovHistory
							Hashtable htCylinderMOH = new Hashtable();
							htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htCylinderMOH.put("DIRTYPE", "UPDATE_KITTING");
							htCylinderMOH.put("QTY",mapObject.get("QTY"));
							htCylinderMOH.put("MOVTID", "OUT");
							htCylinderMOH.put("RECID", "");
							htCylinderMOH.put("ORDNUM",mapObject.get("KONO"));
							htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
							htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htCylinderMOH.put(IConstants.ITEM, mapObject.get("SCANITEM"));
							htCylinderMOH.put("LOC", mapObject.get(IDBConstants.LOC));
							htCylinderMOH.put("BATNO", mapObject.get("CBATCH"));
						    htCylinderMOH.put("REMARKS",mapObject.get(IDBConstants.PARENTITEM) + " "+ mapObject.get(IDBConstants.REMARKS) + " "+ mapObject.get(IDBConstants.RSNCODE) +" "+mapObject.get(IDBConstants.EMPNO)+ " "+mapObject.get("EQUIITEM")+ " "+mapObject.get("KITTYPE"));
							
							movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
							
							
							//Insert into movhis for parent
							Hashtable htInvParent = new Hashtable();
							htInvParent.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htInvParent.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
							htInvParent.put("LOC", mapObject.get(IDBConstants.LOC));
							htInvParent.put("USERFLD4", mapObject.get("PBATCH"));
							
								htCylinderMOH.clear();						
								htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htCylinderMOH.put("DIRTYPE", "KIT_PARENT");
								htCylinderMOH.put(IConstants.ITEM, mapObject.get(IDBConstants.PARENTITEM));
								htCylinderMOH.put("LOC",  mapObject.get(IDBConstants.LOC));
								htCylinderMOH.put("BATNO",mapObject.get("PBATCH"));
								htCylinderMOH.put("QTY", (String) mapObject.get("PQTY"));
								htCylinderMOH.put("ORDNUM",mapObject.get("KONO"));
								htCylinderMOH.put("MOVTID", "IN");
								htCylinderMOH.put("RECID", "");
								htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
								htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htCylinderMOH.put("REMARKS", mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE)+" "+mapObject.get(IDBConstants.EMPNO)+ " "+mapObject.get("KITTYPE"));
							if(parentnonstocktype.equalsIgnoreCase("Y"))
								{
									movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
							    }
							else
							{
								if (!invMstDAO.isExisitKittingQty(htInvParent,"")) 
								{
									movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
								}
							}
							
							//***Invetory process for  parent product, Check data exists or not
							//***If not exists then insert Parent Details into Inventory
							if(!parentnonstocktype.equalsIgnoreCase("Y"))
							{
								Hashtable htCheckBomparent = new Hashtable();
								htCheckBomparent.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htCheckBomparent.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
								htCheckBomparent.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
								htCheckBomparent.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
								htCheckBomparent.put("PARENT_PRODUCT_QTY", pqty);
								htCheckBomparent.put("KONO", mapObject.get("KONO"));
								htCheckBomparent.put("STATUS", "A");
													
								//int bomcount = bomDAO.getCount(htCheckBomparent);
								boolean prtinv = false;
								String XQTY =(String)mapObject.get("PQTY");
								String YQTY=(String)mapObject.get("PQTY");
								ArrayList prtQryList = ProdBomDao.getProdBomDetails((String)mapObject.get(IDBConstants.PARENTITEM),(String)mapObject.get(IDBConstants.PLANT), " AND BOMTYPE='KIT'");
								if (prtQryList.size() > 0) {					        	       
						            for (int iCnt =0; iCnt<prtQryList.size(); iCnt++){
						            	Map lineArr = (Map) prtQryList.get(iCnt);
						            	String childvitem = StrUtils.fString((String)lineArr.get("CITEM"));
						            	String childpqty = StrUtils.fString((String)lineArr.get("QTY"));
						            	double ctrnspqty =  Double.parseDouble((String)mapObject.get("PQTY")) *  Double.parseDouble(childpqty);
						            	Hashtable htCheckBomCH = new Hashtable();
						            	htCheckBomCH.put("CHILD_PRODUCT", childvitem);
						            	htCheckBomCH.put("KONO", mapObject.get("KONO"));
						            	ArrayList childQryList = bomDAO.selectForReport("SELECT ISNULL(SUM(ISNULL(QTY,0)),0) QTY FROM "+(String)mapObject.get(IDBConstants.PLANT)+"_BOM WHERE PLANT='"+(String)mapObject.get(IDBConstants.PLANT)+"'", htCheckBomCH, "");
						            	if (childQryList.size() > 0) {					        	       
								            for (int yCnt =0; yCnt<childQryList.size(); yCnt++){
								            	Map clineArr = (Map) childQryList.get(yCnt);								            	
								            	String childcqty = StrUtils.fString((String)clineArr.get("QTY"));
								            	if(Double.parseDouble(childcqty)>0) {
								            	if(Double.parseDouble(childcqty)<ctrnspqty)
								            	{
								            		double newchildqty = Double.parseDouble(childcqty)/Double.parseDouble(childpqty);
								            		int vint = (int)(newchildqty);
								            		XQTY = String.valueOf(vint);
								            		if(Double.parseDouble(XQTY)<Double.parseDouble(YQTY))
								            		{
								            			YQTY=XQTY;								            			
								            			prtinv = true;
								            		}
								            		else
													{
														prtinv = false;
														//break;
													}
								            	} 
								            	else
								            	{
								            		if(Double.parseDouble(childcqty)==ctrnspqty)
								            		prtinv = true;
								            	}
								            	}
								            	else
												{
								            		YQTY="0";
													prtinv = false;
													break;
												}
								            }
						            	}
						            	else
										{
											prtinv = false;
											//break;
										}
						            }
								}
								else
								{
									prtinv = false;
								}
								if (prtinv)
								//if (bomcount <= 1) 
								{
									YQTY= String.valueOf(Double.parseDouble(YQTY)-Double.parseDouble(invpqty));
									if (Double.parseDouble(YQTY) > 0) {									
									
									Hashtable htInvParentBom = new Hashtable();
									htInvParentBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									htInvParentBom.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
									htInvParentBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInvParentBom.put("USERFLD4", mapObject.get("PBATCH"));
						   
									if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
										Hashtable htInsertInvParentBom = new Hashtable();
										htInsertInvParentBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
										htInsertInvParentBom.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
										htInsertInvParentBom.put("LOC", mapObject.get(IDBConstants.LOC));
										htInsertInvParentBom.put("USERFLD4", mapObject.get("PBATCH"));
										//double convqty = Double.parseDouble((String) mapObject.get("PQTY")) *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
										double convqty = Double.parseDouble(YQTY) *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
										htInsertInvParentBom.put("QTY", String.valueOf(convqty));
										htInsertInvParentBom.put("CRAT", DateUtils.getDateTime());
										
																					
										invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
									
									if (!invResult) {
										throw new Exception(
												"Unable to Process,Kitting Inventory Inserting Parent Item Failed");
									} 
								
								}
							
								else
								{
									updatepqty = Double.parseDouble(YQTY);
									
									Hashtable htInvUpdateBom = new Hashtable();
									htInvUpdateBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
									
									htInvUpdateBom.put("ITEM",mapObject.get(IDBConstants.PARENTITEM));
									htInvUpdateBom.put("LOC", mapObject.get(IDBConstants.LOC));
									htInvUpdateBom.put("USERFLD4",  mapObject.get("PBATCH"));
									double convqty = updatepqty *  Double.parseDouble((String) mapObject.get("PUOMQTY"));
									queryUpdateInv = "SET QTY" + " = QTY+" +convqty ;
									
									invResult = invResult && invMstDAO.update(queryUpdateInv, htInvUpdateBom, extCond);
								
									String queryUpdateInvBom = "SET INVQTY =INVQTY+" +YQTY ;
									bomDAO.update(queryUpdateInvBom, htCheckBomparent, "");
									
									if (!invResult) {
										throw new Exception(
												"Unable to process,Kitting Inventory Updation Of Parent Product Failed");
										
									}
								}
								}
							  }
							}
							
							//****Insert Into MovHistory for parent Item
							Hashtable htInvParent1 = new Hashtable();
							htInvParent1.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
							htInvParent1.put("ITEM", mapObject.get(IDBConstants.PARENTITEM));
							htInvParent1.put("LOC", mapObject.get(IDBConstants.LOC));
							htInvParent1.put("USERFLD4", mapObject.get("PBATCH"));
							
							if (!invMstDAO.isExisitKittingQty(htInvParent1,"")) 
							{
								htCylinderMOH.clear();
								htCylinderMOH.put(IDBConstants.PLANT, mapObject.get(IDBConstants.PLANT));
								htCylinderMOH.put("DIRTYPE", "KIT_PARENT");
								htCylinderMOH.put(IConstants.ITEM, mapObject.get(IDBConstants.PARENTITEM));
								htCylinderMOH.put("LOC",  mapObject.get(IDBConstants.LOC));
								htCylinderMOH.put("BATNO",mapObject.get("PBATCH"));
								htCylinderMOH.put("QTY","1");
								htCylinderMOH.put("MOVTID", "IN");
								htCylinderMOH.put("RECID", "");
								htCylinderMOH.put("ORDNUM",mapObject.get("KONO"));
								htCylinderMOH.put(IDBConstants.CREATED_BY, mapObject.get(IDBConstants.LOGIN_USER));
								htCylinderMOH.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htCylinderMOH.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htCylinderMOH.put("REMARKS", mapObject.get(IDBConstants.REMARKS)+ " "+ mapObject.get(IDBConstants.RSNCODE)+" "+mapObject.get(IDBConstants.EMPNO)+ " "+mapObject.get("KITTYPE"));
							
								movhisResult = movhisResult && movHisDao.insertIntoMovHis(htCylinderMOH);
							}		
							
						}
						else if (!result) {
							throw new Exception(
									"Unable to process,Kitting BOM Updating Product Failed");
						}
			
				}
				
				//***Update BOM	REMARKS		
				Hashtable htUpdateBom = new Hashtable();
				htUpdateBom.put(IConstants.PLANT, mapObject.get(IDBConstants.PLANT));
				htUpdateBom.put("PARENT_PRODUCT", mapObject.get(IDBConstants.PARENTITEM));
				htUpdateBom.put("PARENT_PRODUCT_LOC", mapObject.get(IDBConstants.LOC));
				htUpdateBom.put("PARENT_PRODUCT_BATCH", mapObject.get("PBATCH"));
				htUpdateBom.put("KONO", mapObject.get("KONO"));
				queryUpdateBom = "SET REMARKS='"+mapObject.get(IDBConstants.REMARKS)+"',RSNCODE='"+ mapObject.get(IDBConstants.RSNCODE)+"',EMPNO='"+ mapObject.get(IDBConstants.EMPNO)+"'";
				result = result && bomDAO.update(queryUpdateBom, htUpdateBom, extCond);
			//}

			return result;
		} catch (Exception e) {
			throw e;
		}

	}
	

	
}
