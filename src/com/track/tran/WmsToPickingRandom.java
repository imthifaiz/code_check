package com.track.tran;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.DOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.db.util.TOUtil;

public class WmsToPickingRandom implements WmsTran, IMLogger {

	private boolean printLog = MLoggerConstant.WmsTOPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsTOPicking_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
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

	public WmsToPickingRandom() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			flag = processTodetForPick(m);

			/*if (flag) {

				flag = processInvMst(m);

			}*/

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
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 24 2014, Bruhan, Description: To include Issue Date
	*/
	public boolean processTodetForPick(Map map) throws Exception {

		boolean flag = false;
		try {
			// update receive qty in podet
			ToDetDAO _ToDetDAO = new ToDetDAO();
			ToHdrDAO _ToHdrDAO = new ToHdrDAO();
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			TOUtil _TOUtil = new TOUtil();
			
			String queryPoDet = "";
			String queryPoHdr = "";
			//process dodet qty  start
			boolean insertFlag=false;
			boolean dotransferdetflag=false;
			double totBal = 0;
        	double orderqty = 0;		 
		    double pickedqty = 0;
		    double balQty=0;
		    String PICKING_QTY = (String) map.get("QTY");
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
			pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
			double sumqty =0;
			String TOLNO="";
			double ORDQTY=0;
			
			ArrayList alResult = new ArrayList();
			alResult = _TOUtil.getTODetDetailsRandom((String)map.get("PLANT"),(String) map.get("TONO"),(String) map.get("ITEM"));
			
			 if(alResult.size()>=1){
				  for (int i = 0; i < alResult.size(); i++) {          
					  Map mapLn = (Map) alResult.get(i);
	          		   totBal = Double.parseDouble((String)mapLn.get("totBal"));
	            	   orderqty = Double.parseDouble((String)mapLn.get("qtyor"));			 
	 		           pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));
	 		          		         		              
	 		           balQty = orderqty - pickedqty;
	 		           TOLNO =  StrUtils.fString((String)mapLn.get("tolnno"));
	 		           map.put(IConstants.TODET_TOLNNO,TOLNO);
	            	   map.put(IConstants.ORD_QTY,Double.toString(orderqty));
	            	   
	            	 
	            	   
	            	    if ((pickingQty ==balQty) && (pickingQty >0)  ){   
	            	    	map.put(IConstants.QTY, Double.toString((pickingQty)));
	            	    	sumqty= pickedqty + pickingQty;
	            	    	pickingQty = 0;
	            	    		            	    	
	 		          }else if((pickingQty < balQty) && (pickingQty >0)  ){   
		 		           	 map.put(IConstants.QTY, Double.toString((pickingQty)));
		 		             sumqty= pickedqty + pickingQty;
		 		            	 		           
			 		         pickingQty = 0;
			 		     	 		       
			 		      
			 		   }
	            	  else if((pickingQty > balQty) && (pickingQty >0)  ){   
	 		                  	
	 		        	  map.put(IConstants.QTY, Double.toString((balQty)));
	 		        	  pickingQty = pickingQty-balQty;
	 		        	  sumqty= pickedqty+balQty;
	 		        		        	
	 		           	  insertFlag=false;
	 		           	 		            
	 		          }
	            	  
	            		 		        	            	   
	            	 Hashtable htCondiPoDet = new Hashtable();
	       			 StringBuffer query = new StringBuffer("");

	       			
	       			 htCondiPoDet.put("PLANT", map.get("PLANT"));
	       			 htCondiPoDet.put("tono", map.get("TONO"));
	       			 htCondiPoDet.put("tolnno", TOLNO);
	       			 htCondiPoDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
	       			 
	       			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + " + map.get(IConstants.QTY);
	       			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
	       			
	       			if(insertFlag==false){
	    				
	    				
	       				if (orderqty == sumqty) {
	       					queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
	       							+ map.get(IConstants.QTY) + " , PickStatus='C' ";
	       								
	       								
	       				} else {
	       					queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
	       							+ map.get(IConstants.QTY) + " , PickStatus='O' ";
	       				       					
	       				}
	       				 
	       			    	flag = _ToDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
	       			}
	            	   
	            			
			if (!flag) {
				throw new Exception("picked qty exceeded than Order Qty to Pick");
			}
			if (flag && insertFlag==false) {
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
				htPickDet.clear();
				
				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("TONO", map.get(IConstants.TODET_TONUM));
				htPickDet.put("TOLNO", TOLNO);
				htPickDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put("FROMLOC", map.get(IConstants.FROMLOC));
				htPickDet.put("TOLOC", map.get(IConstants.TOLOC));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC2));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htPickDet.put("PICKQTY", map.get(IConstants.QTY));
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put("REMARK" , map.get(IConstants.REMARKS));
				htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));

				flag = _ToDetDAO.insertPickDet(htPickDet);
				
				//invmst Remove
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				Hashtable htInvMst = new Hashtable();

				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
	                        String cond =" QTY >="+ map.get(IConstants.QTY);
				flag = _InvMstDAO.isExisit(htInvMst,cond );
				if (flag) {
					StringBuffer sql1 = new StringBuffer(" SET ");
					sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");

					Hashtable htInvMstReduce = new Hashtable();
					htInvMstReduce.clear();
					htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));

					flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");

				}
				
				//Invmst Remove end
				
				//invmst add
				
				Hashtable htInvMstAdd = new Hashtable();
				htInvMstAdd.clear();
				htInvMstAdd.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMstAdd.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMstAdd.put(IDBConstants.LOC, map.get(IConstants.LOC2));
				htInvMstAdd.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));

				flag = _InvMstDAO.isExisit(htInvMstAdd, "");
				
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

					flag = _InvMstDAO.update(sql1.toString(), htInvMstAdd, "");

				} else {
					htInvMstAdd.put(IDBConstants.QTY, map.get(IConstants.QTY));
					htInvMstAdd.put(IDBConstants.EXPIREDATE, expiredate);
					htInvMstAdd.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

					flag = _InvMstDAO.insertInvMst(htInvMstAdd);

				}
								
				//invmst add end
				
				//bom
								
				boolean itemflag=false;
				boolean bomupdateflag=false;
				String extcond="";
				
				ItemMstDAO _ItemMstDAO =new ItemMstDAO();
				BomDAO _BomDAO =new BomDAO() ;
				
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
									htInsertInvParentBom.put("QTY",  map.get("QTY"));
												
									invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
									if (!invResult) {
										throw new Exception(
												"Unable to process kitting transfer picking location transfer ,Save inventory parent product failed");
									} 
								
								}
								
						}
						
					    } else {
					    	
					    	flag = true;
					    	
						//throw new Exception("Unable to process kitting transfer picking location transfer, Location not found");
					}
				//}
				/*else
				{
					System.out.println("Come in bom4");
					return bomupdateflag ;
				}*/
						
				
				//bom end
				
				if(pickingQty==0)
				{
					insertFlag=true;
				}
				
			}
				  } // array end
					 
			 }// for loop end

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
                        String cond =" QTY >="+ map.get(IConstants.QTY);
			flag = _InvMstDAO.isExisit(htInvMst,cond );
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");

				Hashtable htInvMstReduce = new Hashtable();
				htInvMstReduce.clear();
				htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));

				flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");

			} else {
				throw new Exception("Not Enough Inventory found to Pick for Product: "+ map.get(IConstants.ITEM) + "  with Batch : "+ map.get(IConstants.BATCH) );
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
	   Oct 24 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
	*/
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", TransactionConstants.PIC_TO_OUT);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htMovhis.put("MOVTID", "OUT");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.FROMLOC));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put(IDBConstants.REMARKS , map.get(IConstants.REMARKS));

			flag = movHisDao.insertIntoMovHis(htMovhis);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 24 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
	*/
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {

			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", "PIC_TO_IN");
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htMovhis.put("MOVTID", "IN");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put(IDBConstants.REMARKS , map.get(IConstants.REMARKS));

			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

}
