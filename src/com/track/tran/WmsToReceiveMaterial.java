package com.track.tran;

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
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsToReceiveMaterial implements WmsTran, IMLogger {
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

	public WmsToReceiveMaterial() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		boolean flag2=false;
		try {
			flag = processTodetForReceive(m);
			
			if (flag) {

				flag = processInvMst(m);

			}
		
			if (flag) {
				flag2=processBOM(m);
				
				

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

				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +" + map.get(IConstants.INV_QTY) );
				sql.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql.append("," + IDBConstants.USERFLD4 + " = '" + map.get(IConstants.INV_BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '" + DateUtils.getDateTime() + "'");
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
				
				if(flag){
					flag = processMovHis_IN(map);
				}
				

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.INV_QTY) + "'");
				sql1.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql1.append("," + IDBConstants.USERFLD4 + " = '" + map.get(IConstants.INV_BATCH) + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"  + DateUtils.getDateTime() + "'");
				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
				
				if(flag){
					flag = processMovHis_OUT(map);
				}

			} else if (!flag) {

				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.INV_QTY));
				htInvMst.put(IDBConstants.EXPIREDATE , expiredate);
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.STATUS, "");

				flag = _InvMstDAO.insertInvMst(htInvMst);
				if(flag){
					flag = processMovHis_IN(map);
				}

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.INV_QTY) + "'");
				sql1.append("," + IDBConstants.EXPIREDATE  + " = '" + expiredate + "'");
				sql1.append("," + IDBConstants.USERFLD4 + " = '" + map.get(IConstants.INV_BATCH) + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '" + dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst1, "");
				if(flag){
					flag = processMovHis_OUT(map);
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
							htInsertInvParentBom.put("QTY",  map.get("INV_QTY1"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting transfer order receiving location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
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

	/** ************Modification History*********************************
	 Oct 14 2014 Bruhan, Description: To change Tran_Date as Transaction Date
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
			htMovhis.put("QTY","-" + map.get(IConstants.INV_QTY));
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

	/** ************Modification History*********************************
	 Oct 14 2014 Bruhan, Description: To change Tran_Date as Transaction Date
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
			htMovhis.put("QTY", map.get(IConstants.INV_QTY));
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

	/* * ************Modification History*********************************
	 Sep 25 2014 Bruhan, Description: To include RECVDATE in RECVDET table
   */
	public boolean processTodetForReceive(Map map) throws Exception {

		boolean flag = false;
		boolean recvdet = false;
		try {
			// update receive qty in podet
			ToDetDAO _ToDetDAO = new ToDetDAO((String) map.get("PLANT"));
			ToHdrDAO _ToHdrDAO = new ToHdrDAO((String) map.get("PLANT"));
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
			String type = (String)(map.get(IConstants.TR_RECV_TYPE));
    		_ToDetDAO.setmLogger(mLogger);
			_ToHdrDAO.setmLogger(mLogger);
			_RecvDetDAO.setmLogger(mLogger);
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");
			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("tono", map.get("TONO"));
			htCondiPoDet.put("tolnno", map.get("TOLNNO"));
			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");
			query.append(",isnull(QtyRc,0) as QtyRc");
			Map mQty = _ToDetDAO.selectRow(query.toString(), htCondiPoDet);
			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double pickQty = Double.parseDouble((String) mQty.get("qtyPick"));
			double rcQty = Double.parseDouble((String) mQty.get("QtyRc"));
			double tranQty = Double.parseDouble((String) map.get(IConstants.INV_QTY));
			ordQty = StrUtils.RoundDB(ordQty, IConstants.DECIMALPTS);
			pickQty = StrUtils.RoundDB(pickQty, IConstants.DECIMALPTS);
			rcQty = StrUtils.RoundDB(rcQty, IConstants.DECIMALPTS);
				double sumqty = rcQty + tranQty;
				sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
//System.out.println("ordqty"+ordQty+"pickqty"+pickQty+"sumqty"+sumqty+"invqtyt"+tranQty);
			if (ordQty == pickQty && pickQty == sumqty) {
				queryPoDet = "set qtyrc= isNull(qtyrc,0) + " + map.get(IConstants.INV_QTY) + " , LNSTAT='C' ";

			} else {
				queryPoDet = "set qtyrc= isNull(qtyrc,0) + " + map.get(IConstants.INV_QTY) + " , LNSTAT='O' ";

			}
			// Added By Samatha extracond to Controll the Receive Qty Excced the
			// PickQty Aug 24 1010
			String extraCond = " AND  qtyPick >= isNull(qtyrc,0) + " + map.get(IConstants.INV_QTY);
			flag = _ToDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			if (!flag) {
				throw new Exception("Recv Qty Exceeded the Pick Qty to Recv  ");
			}
			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("tono", map.get("TONO"));

				flag = _ToDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");

				if (flag)
					queryPoHdr = "set STATUS='O' ";
				else
					queryPoHdr = "set STATUS='C' ";

				flag = _ToHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
			
			}
                        
		    if (flag) {
		    
		            Hashtable htCondiToPick = new Hashtable();
		            htCondiToPick.clear();
		            htCondiToPick.put("PLANT", map.get("PLANT"));
		            htCondiToPick.put("tono", map.get("TONO"));
		            htCondiToPick.put("tolno", map.get("TOLNNO"));
		            htCondiToPick.put("batch", map.get(IConstants.INV_BATCH));
		            htCondiToPick.put("ISNULL(STATUS,'')", " ");
		            StringBuffer sqlQry = new StringBuffer(" SET  ");
		            //Start added by Bruhan for Bulk receiving on 3 Aug 2012.
		            if(type!= null && type.equalsIgnoreCase("BULK")){
		            	double qtypick = Double.parseDouble((String)map.get(IDBConstants.PICKQTY));	
		            	double recvdQty = Double.parseDouble((String)map.get(IConstants.TR_RECVQTY ));		           
		            	double recievingQty =Double.parseDouble((String)map.get(IConstants.INV_QTY));
		            	double totalQty = recvdQty + recievingQty;
		            	sqlQry.append("STATUS" + " =  CASE WHEN " +totalQty +"< "+ qtypick +" THEN '' ELSE 'C' END " );
		            }else{
		            sqlQry.append("STATUS" + " =  CASE WHEN " + map.get(IConstants.INV_QTY) + "< PICKQTY THEN '' ELSE 'C' END " );
		            }
		            //End added by Bruhan for Bulk receiving on 3 Aug 2012.
		            String extraCon = "  ";
		            try{
		            flag = _ToDetDAO.updateToPickTable( sqlQry.toString(), htCondiToPick, extraCon);
		            }catch(Exception e){
		                
		            }
		    }
			// /{
			
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.TODET_TONUM));
			htRecvDet.put("LNNO", map.get(IConstants.TODET_TOLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
			htRecvDet.put("RECQTY", map.get(IConstants.RECV_QTY));
			htRecvDet.put("REMARK", map.get(IConstants.REMARKS));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
			htRecvDet.put("RECEIVESTATUS", "C");
			htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
			 //Start added by Bruhan for Bulk receiving on 3 Aug 2012.
			if (type!=null && type.equalsIgnoreCase("BULK")) {
				double qtypick = Double.parseDouble((String)map.get(IDBConstants.PICKQTY));	
				double recvdQty = Double.parseDouble((String)map.get(IConstants.TR_RECVQTY ));		           
            	double recievingQty =Double.parseDouble((String)map.get(IConstants.INV_QTY));
            	double totalQty = recvdQty + recievingQty;
				if(qtypick != totalQty)
					htRecvDet.put("RECEIVESTATUS", "O");
			}
			 //End added by Bruhan for Bulk receiving on 3 Aug 2012.
		        htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "TO");
			flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Recv Qty Exceeded the Pick Qty to Recv");
		}
		return flag;
	}
}