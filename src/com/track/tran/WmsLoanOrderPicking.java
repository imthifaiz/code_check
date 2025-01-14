package com.track.tran;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsLoanOrderPicking implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsLoanOrderPicking_PRINTPLANTMASTERINFO;
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

	public WmsLoanOrderPicking() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			flag = processLoanOrder(m);

			if (flag) {

				flag = processInvMst(m);

			}

			if (flag) {
				{

					flag = processMovHis_OUT(m);

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						MLogger.log(0, "Exception locationTransfer"
								+ e.getMessage());
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
	   Oct 15 2014 Bruhan, Description: To include Issue Date
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

			double tranQty = Double.parseDouble((String) map
					.get(IConstants.QTY_ISSUE));

			String queryloanDet = "", queryloanHdr = "";
			queryloanDet = "set qtyis= isNull(qtyis,0) + " + map.get(IConstants.QTY_ISSUE) + " , PickStatus='O',lnstat='O' ";
			String extraCond = " AND  Qtyor >= isNull(qtyis,0) + " + map.get(IConstants.QTY_ISSUE);

			flag = _loanDetDAO.update(queryloanDet, htDet, extraCond);

			if (!flag) {
				throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
			}

			flag = _loanDetDAO.isExisit(htDet, "  Qtyor=Qtyis ");
			if (flag) {
				queryloanDet = "set  PickStatus='C' ,lnstat='O'";
				flag = _loanDetDAO.update(queryloanDet, htDet, "");
			} else {
				flag = true;
			}

			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiPoHdr.put("ORDNO", map.get(IConstants.LOANDET_ORDNO));
				flag = _loanDetDAO
						.isExisit(
								htCondiPoHdr,
								" (isnull(pickStatus,'') in ('','O','N') or isnull(RECVSTATUS,'') in ('','O','N'))");

				if (flag)
					queryloanHdr = "set status ='O' ";
				else
					queryloanHdr = "set status='C' ";
				flag = _loanHdrDAO.update(queryloanHdr, htCondiPoHdr, "");

			}

			if (flag) {

				boolean pickdet = false;
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
					extCond = "QTY >= " + map.get(IConstants.QTY);
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
				htPickDet.put("ORDNO", map.get(IConstants.LOANDET_ORDNO));
				htPickDet.put("ORDLNNO", map.get(IConstants.LOANDET_ORDLNNO));
				htPickDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.CUSTOMER_NAME));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC1));
				htPickDet.put(IConstants.LOC1, map.get(IConstants.LOC));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
				htPickDet.put("PICKQTY",  String.valueOf(adjustedQuantity));
				htPickDet.put("REVERSEQTY", "0");
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
				htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
				pickdet = _loanHdrDAO.insertintoLoanPick(htPickDet);
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
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + inqty;
				//extCond = "QTY >= " + map.get(IConstants.QTY);
			}else{
				extCond = "QTY > 0";
			}

			flag = _InvMstDAO.isExisit(htInvMst, extCond);

			if (flag) {
				ArrayList alStock = _InvMstDAO.selectInvMst("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY_ISSUE));
					quantityToAdjust = quantityToAdjust * Double.valueOf((String)map.get("UOMQTY"));
					Iterator iterStock = alStock.iterator();
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
						StringBuffer sql1 = new StringBuffer(" SET ");
						sql1.append(IDBConstants.QTY + " = QTY -'"
								+ adjustedQuantity + "'");

						Hashtable htInvMstReduce = new Hashtable();
						htInvMstReduce.clear();

						htInvMstReduce.put(IDBConstants.PLANT, map
								.get(IConstants.PLANT));
						htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
						htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
						htInvMstReduce.put(IDBConstants.USERFLD4, map
								.get(IConstants.BATCH));
						htInvMstReduce.put(IDBConstants.CREATED_AT, mapIterStock.get(IConstants.CREATED_AT));
						htInvMstReduce.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
						//flag = CheckInvMstForGoddsIssue(htInvMstReduce, request, response);
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
			      String expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
			//get expiredate for inventory update end
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY +'"
				+ inqty + "'");
				//+ map.get(IConstants.QTY_ISSUE) + "'");
				sql1.append("," +IDBConstants.EXPIREDATE + " = '"
						+ expiredate + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");

			} else {
				htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY_ISSUE));
				/*htInvMst.put(IDBConstants.USERFLD3, _InvMstDAO.getExpireDate(
						(String) map.get(IConstants.PLANT), (String) map
								.get(IConstants.ITEM), (String) map
								.get(IConstants.BATCH)));*/
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
										"Unable to process kitting loan order picking location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
				throw new Exception("Unable to process kitting loan order picking location transfer, Location not found");
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
	   Oct 15 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
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
			htRecvHis.put("DIRTYPE", TransactionConstants.LOAN_PIC_OUT);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY_ISSUE));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.LOANDET_ORDNO));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
		    htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
			// flag=false;
		} catch (Exception e) {

			MLogger.log(0, "Exception :: " + e.getMessage());
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
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
			htRecvHis.put("DIRTYPE", TransactionConstants.LOAN_PICK_IN);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY_ISSUE));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC1));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.LOANDET_ORDNO));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
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