package com.track.tran;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.TOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class WmsToReceiveMaterialRandom implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsToReceiveMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsToReceiveMaterial_PRINTPLANTMASTERINFO;
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

	public WmsToReceiveMaterialRandom() {
		
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		boolean flag2=false;
		try {
			flag = processTodetForReceive(m);
			
			/*if (flag) {

				flag = processInvMst(m);

			}*/
		
			if (flag) {
				//flag2=processBOM(m);
				//flag = processMovHis(m);
				

			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		boolean flag1 = false;

		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			Hashtable htInvMst1 = new Hashtable();
			htInvMst.clear();

			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

			htInvMst1.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst1.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst1.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
			htInvMst1.put(IDBConstants.LOC, map.get(IConstants.LOC1));
			

			flag = _InvMstDAO.isExisit(htInvMst, "");
			
			//get expiredate for inventory update
			    String expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC1),(String)map.get(IConstants.BATCH));
			  
			//get expiredate for inventory update end

			if (flag) {

				

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");
				sql1.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql1.append("," + IDBConstants.USERFLD4 + " = '"+ map.get(IConstants.INV_BATCH) + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"+ dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
				if(flag){
					flag = processMovHis_OUT(map);
				}
				

				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +"+ map.get(IConstants.QTY) );
		        sql.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql.append("," + IDBConstants.USERFLD4 + " = '" + map.get(IConstants.INV_BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '" + dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				if(flag){
					flag = processMovHis_IN(map);
				}
				

			} else if (!flag) {
				
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");
				sql1.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql1.append("," + IDBConstants.USERFLD4 + " = '" + map.get(IConstants.INV_BATCH) + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '" + dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
				if(flag){
					flag = processMovHis_OUT(map);
				}

				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				htInvMst.put(IDBConstants.EXPIREDATE , expiredate);
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.STATUS, "");

				flag = _InvMstDAO.insertInvMst(htInvMst);
				if(flag){
					flag = processMovHis_IN(map);
				}

				

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
			htBomMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC1));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.INV_BATCH));

			flag = _BomDAO.isExisit(htBomMst);
			
			if(flag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC1));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.INV_BATCH));
		

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.PARENT_LOC + " ='"
						+ map.get(IConstants.LOC) + "'");
				sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
						+ map.get(IConstants.LOC) + "'");
				
				bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
				if(bomupdateflag)
				{   
					boolean invResult=false;
					boolean queryUpdateInv=false;
					
					
					InvMstDAO invMstDAO = new InvMstDAO();
					
					
					Hashtable htInvParentBom = new Hashtable();
					htInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
				    htInvParentBom.put("ITEM",  map.get(IConstants.ITEM));
					htInvParentBom.put("LOC", map.get(IConstants.LOC));
					htInvParentBom.put("USERFLD4",  map.get(IConstants.INV_BATCH));
				
						if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
							
							Hashtable htInsertInvParentBom = new Hashtable();
							htInsertInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
							htInsertInvParentBom.put("ITEM", map.get(IConstants.ITEM));
							htInsertInvParentBom.put("LOC",  map.get(IConstants.LOC));
							htInsertInvParentBom.put("USERFLD4",map.get(IConstants.INV_BATCH));
							htInsertInvParentBom.put("QTY",  map.get("QTY"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting transfer order receiving location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    }/* else {
				throw new Exception("Unable to process kitting transfer order receiving location transfer, Location not found");
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
	   Oct 24 2014 Bruhan, Description: To Change transaction date as Iconstant.TRAN_DATE
	 */

	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		
		try {

			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", TransactionConstants.PIC_TO_OUT);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("MOVTID", "OUT");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC1));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put("BATNO", map.get(IConstants.INV_BATCH));
			htMovhis.put("QTY","-" + map.get(IConstants.QTY));
			htMovhis.put("USERFLD1", map.get(IConstants.INV_EXP_DATE));
			htMovhis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put("REMARKS", map.get(IConstants.REMARKS));
			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 24 2014 Bruhan, Description: To Change transaction date as Iconstant.TRAN_DATE
	 */
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		
		try {

			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", TransactionConstants.TO_RECV);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("MOVTID", "IN");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.TODET_TONUM));
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put("BATNO", map.get(IConstants.INV_BATCH));
			htMovhis.put("QTY", map.get(IConstants.QTY));
			htMovhis.put("USERFLD1", map.get(IConstants.INV_EXP_DATE));
			htMovhis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put("REMARKS", map.get(IConstants.REMARKS));
			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 13 2014 Bruhan, Description: To include RECVDATE
	 */

	public boolean processTodetForReceive(Map map) throws Exception {

		boolean flag = false;
		boolean recvdet = false;
		try {
			// update receive qty in podet
			ToDetDAO _ToDetDAO = new ToDetDAO((String) map.get("PLANT"));
			ToHdrDAO _ToHdrDAO = new ToHdrDAO((String) map.get("PLANT"));
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
			TOUtil _TOUtil = new TOUtil();
			// update receive qty in podet
		
			_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			
			
			String type = (String)(map.get(IConstants.TR_RECV_TYPE));
					
			//processs start
			String queryPoDet = "";
			String queryPoHdr = "";
			//process dodet qty  start
			boolean insertFlag=false;
			boolean dotransferdetflag=false;
			double totBal = 0;
        	double receivedqty = 0;		 
		    double pickedqty = 0;
		    double balQty=0;
		    double orderqty=0;
		    String RECIVING_QTY = (String) map.get("QTY");
			double recivingQty = Double.parseDouble(((String) RECIVING_QTY.trim().toString()));
			recivingQty = StrUtils.RoundDB(recivingQty, IConstants.DECIMALPTS);
			double sumqty =0;
			String TOLNO="";
			double PICKINGQTY=0;
			
			ArrayList alResult = new ArrayList();
			alResult = _TOUtil.getTODetDetailsRecvRandom((String)map.get("PLANT"),(String) map.get("TONO"),(String) map.get("ITEM"));
			 if(alResult.size()>=1){
				  for (int i = 0; i < alResult.size(); i++) {   
					   Map mapLn = (Map) alResult.get(i);
	          		   totBal = Double.parseDouble((String)mapLn.get("totRecBal"));
	          		   orderqty = Double.parseDouble((String)mapLn.get("qtyor"));
	            	   receivedqty = Double.parseDouble((String)mapLn.get("qtyrc"));			 
	 		           pickedqty = Double.parseDouble((String)mapLn.get("qtyPick"));
	 		           	 		          		         		              
	 		           balQty = pickedqty - receivedqty;
	 		           TOLNO =  StrUtils.fString((String)mapLn.get("tolnno"));
	 		           
	 		           map.put(IConstants.TODET_TOLNNO,TOLNO);
	            	   map.put(IConstants.ORD_QTY,Double.toString(orderqty));
	            	   
	                  if ((recivingQty ==balQty) && (recivingQty >0)  ){   
	            	    	map.put(IConstants.QTY, Double.toString((recivingQty)));
	            	    	sumqty= receivedqty + recivingQty;
	            	    	recivingQty = 0;
	           		            	    	
	 		          }else if((recivingQty < balQty) && (recivingQty >0)  ){   
		 		           	 map.put(IConstants.QTY, Double.toString((recivingQty)));
		 		             sumqty= receivedqty + recivingQty;
		 		             recivingQty = 0;
		 		    
			 		   }
	            	   else if((recivingQty > balQty) && (recivingQty > 0)  ){   
	 		                  	
	 		        	  map.put(IConstants.QTY, Double.toString((balQty)));
	 		        	  recivingQty = recivingQty-balQty;
	 		        	  sumqty= receivedqty+balQty;
	 		        	  insertFlag=false;
	 		   	             
	 		          }
	            	     Hashtable htCondiPoDet = new Hashtable();
		       			 StringBuffer query = new StringBuffer("");
		       			 htCondiPoDet.put("PLANT", map.get("PLANT"));
		       			 htCondiPoDet.put("tono", map.get("TONO"));
		       			 htCondiPoDet.put("tolnno", TOLNO);
		       			 	       		
		    			String extraCond = " AND  qtyPick >= isNull(qtyrc,0) + " + map.get(IConstants.QTY);
		    			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
		    			
		    			if(insertFlag==false){
		    						    				
		       				if (orderqty == sumqty) {
		       					queryPoDet = "set qtyrc= isNull(qtyrc,0) + " + map.get(IConstants.QTY) + " , LNSTAT='C' ";
		       					   			
		       								
		       				} else {
		       					queryPoDet = "set qtyrc= isNull(qtyrc,0) + " + map.get(IConstants.QTY) + " , LNSTAT='O' ";
		       						       					
		       				}
		       				 
		       			    	flag = _ToDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
		       			}
		    			
		    			if (!flag) {
		    				throw new Exception("receiving qty exceeded than picked Qty to receive");
		    			}
		    			
		    			if (flag && insertFlag==false) {
		    				Hashtable htCondiPoHdr = new Hashtable();
		    				htCondiPoHdr.put("PLANT", map.get("PLANT"));
		    				htCondiPoHdr.put("tono", map.get("TONO"));

		    				flag = _ToDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");

		    				if (flag)
		    					queryPoHdr = "set STATUS='O' ";
		    				else
		    					queryPoHdr = "set STATUS='C' ";

		    				flag = _ToHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
		    			
		    			
		    			
		    			Hashtable htRecvDet = new Hashtable();
		    			htRecvDet.clear();
		    			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    			htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.TODET_TONUM));
		    			htRecvDet.put("LNNO", TOLNO);
		    			htRecvDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
		    			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		    			htRecvDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
		    			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
		    			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
		    			htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
		    			htRecvDet.put("RECQTY", map.get(IConstants.QTY));
		    			htRecvDet.put("REMARK", map.get(IConstants.REMARKS));
		    			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
		    			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
		    			htRecvDet.put("RECEIVESTATUS", "C");
		    			 htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    			
		    			flag = _RecvDetDAO.insertRecvDet(htRecvDet);
		    			
		    			
		    			//process invmst
		    			 boolean flag1 = false;

		    			InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		    			_InvMstDAO.setmLogger(mLogger);
		    			
		    			Hashtable htInvMst = new Hashtable();
		    			Hashtable htInvMst1 = new Hashtable();
		    			htInvMst.clear();

		    			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		    			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
		    			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

		    			htInvMst1.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    			htInvMst1.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		    			htInvMst1.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
		    			htInvMst1.put(IDBConstants.LOC, map.get(IConstants.LOC1));
		    			

		    			flag = _InvMstDAO.isExisit(htInvMst, "");
		    			
		    			//get expiredate for inventory update
		    			    String expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC1),(String)map.get(IConstants.BATCH));
		    			   
		    			//get expiredate for inventory update end

		    			if (flag) {

		    				StringBuffer sql = new StringBuffer(" SET ");
		    				
		    				sql.append(IDBConstants.QTY + " = QTY +"
		    						+ map.get(IConstants.QTY) );
		    				
		    				sql.append("," + IDBConstants.EXPIREDATE  + " = '"
		    						+ expiredate + "'");
		    				 
		    				sql.append("," + IDBConstants.USERFLD4 + " = '"
		    						+ map.get(IConstants.INV_BATCH) + "'");
		    				sql.append("," + IDBConstants.UPDATED_AT + " = '"
		    						+ dateUtils.getDateTime() + "'");

		    				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
		    				
		    			
		    				if(flag){
		    					flag = processMovHis_IN(map);
		    				}

		    				StringBuffer sql1 = new StringBuffer(" SET ");
		    				sql1.append(IDBConstants.QTY + " = QTY -'"
		    						+ map.get(IConstants.QTY) + "'");
		    						    				
		    				sql1.append("," + IDBConstants.EXPIREDATE  + " = '"
		    						+ expiredate + "'");
		    				
		    				sql1.append("," + IDBConstants.USERFLD4 + " = '"
		    						+ map.get(IConstants.INV_BATCH) + "'");
		    				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
		    						+ dateUtils.getDateTime() + "'");

		    				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
		    				if(flag){
		    					flag = processMovHis_OUT(map);
		    				}

		    			} else if (!flag) {

		    				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
		    				htInvMst.put(IDBConstants.USERFLD4, map
		    						.get(IConstants.INV_BATCH));
		    				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
		    				
		    				htInvMst.put(IDBConstants.EXPIREDATE , expiredate);
		    				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
		    				htInvMst.put(IDBConstants.STATUS, "");

		    				flag = _InvMstDAO.insertInvMst(htInvMst);
		    		
		    				if(flag){
		    					flag = processMovHis_IN(map);
		    				}

		    				StringBuffer sql1 = new StringBuffer(" SET ");
		    				sql1.append(IDBConstants.QTY + " = QTY -'"
		    						+ map.get(IConstants.QTY) + "'");
		    			
		    				sql1.append("," + IDBConstants.EXPIREDATE  + " = '"
		    						+ expiredate + "'");
		    				sql1.append("," + IDBConstants.USERFLD4 + " = '"
		    						+ map.get(IConstants.INV_BATCH) + "'");
		    				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
		    						+ dateUtils.getDateTime() + "'");

		    				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
		    				
		    				if(flag){
		    					flag = processMovHis_OUT(map);
		    				}

		    			}
		    			//process invmst end
		    			
		    			//process bom
		    			boolean itemflag=false;
		    			boolean bomupdateflag=false;
		    			String extcond="";
		    			
		    			ItemMstDAO _ItemMstDAO =new ItemMstDAO();
		    			BomDAO _BomDAO =new BomDAO() ;
		    			/*
		    			Hashtable htItemMst= new Hashtable();
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
		    				htBomMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC1));
		    				htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.INV_BATCH));

		    				flag = _BomDAO.isExisit(htBomMst);
		    				
		    				if(flag)
		    				{
		    					Hashtable htBomUpdateMst = new Hashtable();
		    					htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		    					htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
		    					htBomUpdateMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC1));
		    					htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.INV_BATCH));
		    			

		    					StringBuffer sql1 = new StringBuffer(" SET ");
		    					sql1.append(IDBConstants.PARENT_LOC + " ='"
		    							+ map.get(IConstants.LOC) + "'");
		    					sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
		    							+ map.get(IConstants.LOC) + "'");
		    					
		    					bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
		    					if(bomupdateflag)
		    					{   
		    						boolean invResult=false;
		    						boolean queryUpdateInv=false;
		    						
		    						InvMstDAO invMstDAO = new InvMstDAO();
		    							    						
		    						Hashtable htInvParentBom = new Hashtable();
		    						htInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
		    					    htInvParentBom.put("ITEM",  map.get(IConstants.ITEM));
		    						htInvParentBom.put("LOC", map.get(IConstants.LOC));
		    						htInvParentBom.put("USERFLD4",  map.get(IConstants.INV_BATCH));
		    					
		    							if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
		    								
		    								Hashtable htInsertInvParentBom = new Hashtable();
		    								htInsertInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
		    								htInsertInvParentBom.put("ITEM", map.get(IConstants.ITEM));
		    								htInsertInvParentBom.put("LOC",  map.get(IConstants.LOC));
		    								htInsertInvParentBom.put("USERFLD4",map.get(IConstants.INV_BATCH));
		    								htInsertInvParentBom.put("QTY",  map.get("QTY"));
		    											
		    								invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
		    								
		    								if (!invResult) {
		    									throw new Exception(
		    											"Unable to process kitting transfer order receiving location transfer ,Save inventory parent product failed");
		    								} 
		    							
		    							}
		    							
		    					}
		    					
		    				    } else {
		    				    	
		    				    	flag = true;
		    				    	//throw new Exception("Unable to process kitting transfer order receiving location transfer, Location not found");
		    				}
		    			//}
		    			
		    			//process bom end
		    		
		    			if(recivingQty==0)
						{
							insertFlag=true;
						}
		    			
		    				       			 
		    			}//if flag and insertflag end 
				  }//array end
			 }//for loop end
						
				

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Recv Qty Exceeded the Pick Qty to Recv");
		}
		return flag;
	}

	
	


}
