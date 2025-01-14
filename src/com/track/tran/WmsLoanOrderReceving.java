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
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

public class WmsLoanOrderReceving implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsLoanOrderReceving_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderReceving_PRINTPLANTMASTERINFO;
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

	public WmsLoanOrderReceving() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		boolean flag2=false;
		try {
			flag = processLoanOrder(m);

			if (flag) {

				flag = processInvMst(m);
				flag2=processBOM(m);

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
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* * ************Modification History*********************************
	 Bruhan,Oct 14 2014,Description: To include RECVDATE in RECVDET table
    */
	public boolean processLoanOrder(Map map) throws Exception {

		boolean flag = false;
		try {
			LoanDetDAO _loanDetDAO = new LoanDetDAO();
			LoanHdrDAO _loanHdrDAO = new LoanHdrDAO();
			_loanDetDAO.setmLogger(mLogger);
			_loanHdrDAO.setmLogger(mLogger);
			Hashtable htDet = new Hashtable();
			StringBuffer query = new StringBuffer("");

			htDet.put("PLANT", map.get(IConstants.PLANT));
			htDet.put("Ordno", map.get(IConstants.LOANDET_ORDNO));
			htDet.put("ordlnno", map.get(IConstants.LOANDET_ORDLNNO));

			double tranQty = Double.parseDouble((String) map.get(IConstants.RECV_QTY));

			String queryloanDet = "", queryloanHdr = "";
			queryloanDet = "set qtyRc= isNull(qtyRc,0) + "
					+ map.get(IConstants.RECV_QTY) + " , RecvStatus='O' ";

			String extraCond = " AND  qtyis >= isNull(qtyRc,0) + "
					+ map.get(IConstants.RECV_QTY);
			flag = _loanDetDAO.update(queryloanDet, htDet, extraCond);
			if (!flag) {
				throw new Exception("Recv Qty Exceeded the Pick Qty to Recv");
			}
			queryloanDet = "set RecvStatus='C' ,LNSTAT ='C'";
			flag = _loanDetDAO.isExisit(htDet,
					" (Qtyor=qtyis and  qtyis=qtyrc)");
			if (flag) {
				flag = _loanDetDAO.update(queryloanDet, htDet, "");
			} else {
				flag = true;
			}

			if (flag) {
				Hashtable htCondiLoanHdr = new Hashtable();
				htCondiLoanHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiLoanHdr.put("ORDNO", map.get(IConstants.LOANDET_ORDNO));
				flag = _loanDetDAO
						.isExisit(
								htCondiLoanHdr,
								" (isnull(pickStatus,'') in ('','O','N') or isnull(RECVSTATUS,'') in ('','O','N'))");

				if (flag)
					queryloanHdr = "set STATUS='O' ";
				else
					queryloanHdr = "set STATUS='C' ";
				flag = _loanHdrDAO.update(queryloanHdr, htCondiLoanHdr, "");

			}

			if (flag) {
				RecvDetDAO _RecvDetDAO = new RecvDetDAO();
				_RecvDetDAO.setmLogger(mLogger);
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.clear();
				htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvDet.put(IDBConstants.PODET_PONUM, map	.get(IConstants.LOANDET_ORDNO));
				htRecvDet.put(IDBConstants.LNNO, map.get(IConstants.LOANDET_ORDLNNO));
				htRecvDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
				htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htRecvDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
				htRecvDet.put("BATCH", map.get(IConstants.BATCH));
				htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC1));
				htRecvDet.put(IConstants.LOC1, map.get(IConstants.LOC));
				htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htRecvDet.put("RECQTY", map.get(IConstants.RECV_QTY));
				htRecvDet.put("REMARK", map.get(IConstants.REMARKS));
				htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			        htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "LO");
				htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
				flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Recv Qty Exceeded the Pick Qty to Recv");
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		try {
			flag = processInvRemove(map);

			if (flag) {
				flag = processInvAdd(map);

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
			double inqty = Double.valueOf((String)map.get(IConstants.RECV_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			String cond = "QTY >= " + inqty;
			//String cond =" QTY >="+ map.get(IConstants.RECV_QTY);

			flag = _InvMstDAO.isExisit(htInvMst, cond);

			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'"
						+ inqty + "'");
						//+ map.get(IConstants.RECV_QTY) + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");

			} else {
				throw new Exception("Not Enough Inventory found to Receive for Product: "+ map.get(IConstants.ITEM) + "  with Batch : "+ map.get(IConstants.BATCH)+ "  scanned at the location  "+map.get(IConstants.LOC) );
				
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();

			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC1));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));

			flag = _InvMstDAO.isExisit(htInvMst, "");
			
			//get expiredate for inventory update
			   String expiredate="";
			   expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
			//get expiredate for inventory update end
			double inqty = Double.valueOf((String)map.get(IConstants.RECV_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY +'"
				+ inqty + "'");
				//+ map.get(IConstants.RECV_QTY) + "'");
				sql1.append("," +IDBConstants.EXPIREDATE + " = '"
						+ expiredate + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");

			} else {
				htInvMst.put(IDBConstants.QTY, inqty);
				htInvMst.put(IDBConstants.EXPIREDATE, expiredate);
				htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			    //NO EXPIRYDT
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
			htBomMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
			htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

			flag = _BomDAO.isExisit(htBomMst);
			
			if(flag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC", map.get(IConstants.LOC));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.BATCH));
		

				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.PARENT_LOC + " ='"
						+ map.get(IConstants.LOC1) + "'");
				sql1.append(" , "+IDBConstants.CHILD_LOC+"='"
						+ map.get(IConstants.LOC1) + "'");
				
				bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst,"");
				if(bomupdateflag)
				{   
					boolean invResult=false;
					boolean queryUpdateInv=false;
					
					
					InvMstDAO invMstDAO = new InvMstDAO();
					
					
					Hashtable htInvParentBom = new Hashtable();
					htInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
				    htInvParentBom.put("ITEM",  map.get(IConstants.ITEM));
					htInvParentBom.put("LOC", map.get(IConstants.LOC1));
					htInvParentBom.put("USERFLD4",  map.get(IConstants.BATCH));
				
						if (!invMstDAO.isExisitBomQty(htInvParentBom,"")) {
							
							Hashtable htInsertInvParentBom = new Hashtable();
							htInsertInvParentBom.put(IConstants.PLANT, map.get(IConstants.PLANT));
							htInsertInvParentBom.put("ITEM", map.get(IConstants.ITEM));
							htInsertInvParentBom.put("LOC",  map.get(IConstants.LOC1));
							htInsertInvParentBom.put("USERFLD4",map.get(IConstants.BATCH));
							htInsertInvParentBom.put("QTY",  map.get("INV_QTY1"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting loan order receiving location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
				throw new Exception("Unable to process kitting loan order receiving location transfer, Location not found");
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
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.LOAN_RECV_OUT);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.RECV_QTY));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.LOANDET_ORDNO));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
			htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
			// flag=false;
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
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.LOAN_RECV_IN);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.RECV_QTY));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC1));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.LOANDET_ORDNO));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
    		flag = movHisDao.insertIntoMovHis(htRecvHis);
			// flag=false;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
}
