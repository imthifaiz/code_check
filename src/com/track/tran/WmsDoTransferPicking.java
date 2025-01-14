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
import com.track.dao.DoDetDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsDoTransferPicking implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsDOTransferPicking_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsDOTransferPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
        StrUtils _strUtils =null;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public  WmsDoTransferPicking() {
		dateUtils = new DateUtils();
                _strUtils = new StrUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		boolean flag2=false;
		
		try {
			flag = processDoTransferdetForPick(m);

			if (flag) {

				flag = processDoTransferInvMst(m);

			}
			if (flag) {

				{
					flag2=processBOM(m);
					flag = processMovHis_OUT(m);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						MLogger.log(0, "Exception locationTransfer"+ e.getMessage());
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

	public boolean processDoTransferdetForPick(Map map) throws Exception {
		
		boolean flag = true;
		boolean flagpick=false;
		try {
			DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
			DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
			
			_DoTransferDetDAO.setmLogger(mLogger);
			_DoTransferHdrDAO.setmLogger(mLogger);
			
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");
			// getQty from podet
			
			
			
			//-------------------------------put  DOtransfer order qty picked qty checking here

			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("dono", map.get("PONO"));
			htCondiPoDet.put("dolnno", map.get("POLNNO"));

			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");

			Map mQty = _DoTransferDetDAO.selectRow(query.toString(), htCondiPoDet);
			
			

			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double qtyPick = Double.parseDouble((String) mQty.get("qtyPick"));

			double tranQty = Double.parseDouble((String) map.get(IConstants.QTY));
			ordQty = StrUtils.RoundDB(ordQty, IConstants.DECIMALPTS);
			qtyPick = StrUtils.RoundDB(qtyPick, IConstants.DECIMALPTS);
			tranQty = StrUtils.RoundDB(tranQty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
	
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "
					+ map.get(IConstants.QTY);
			
			
			
			/*Commanded by Bruhan on 20100923   due to Update Hdr detail in WmsMaterialIssue
				//queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
					//+ map.get(IConstants.QTY) + " , PickStatus='O' ";
	
				//flag = _DoTransferDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			//***Commanded by Bruhan on 20100923 end */
			
			

			if (flag) {
				
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("dono", map.get("PONO"));
				
				

				flag = _DoTransferDetDAO.isExisit(htCondiPoHdr,	"pickStatus in ('O','N')");
				
				
				
				
				queryPoHdr = "set PickStaus='O' ";
				
                /*commanded by Bruhan due to Update Hdr detail in WmsMaterialIssue
				   flag = _DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");*/
				
				
				//coding for update or insert dotransfer_pick table
				boolean pickdet = false;
				
				Hashtable htPickCalc = new Hashtable();
				htPickCalc.clear();
				
				Hashtable htPickDet = new Hashtable();
				htPickDet.clear();
				
				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
				htPickDet.put("DOLNO", map.get(IConstants.PODET_POLNNO));
				htPickDet.put("ITEM", map.get(IConstants.ITEM));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				//htPickDet.put("FROMLOC", map.get(IConstants.LOC));
				htPickDet.put("TOLOC", map.get(IConstants.LOC2));
				
				
				
				
				
				//find out dotransfer_pick have entry
				flagpick= _DoTransferDetDAO.isExisitDoTransferPick(htPickDet, "");
				
					
				if(!flagpick)
				{
					
					htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
					htPickDet.put("DOLNO", map.get(IConstants.PODET_POLNNO));
					htPickDet.put(IDBConstants.CUSTOMER_NAME,_strUtils.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
					htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htPickDet.put(IDBConstants.ITEM_DESC, map.get(IConstants.ITEM_DESC));
					htPickDet.put("BATCH", map.get(IConstants.BATCH));
					//htPickDet.put("FROMLOC", map.get(IConstants.LOC));
					htPickDet.put("TOLOC", map.get(IConstants.LOC2));
					htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
					htPickDet.put("PICKQTY", map.get(IConstants.QTY));
					htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));

					pickdet = _DoTransferDetDAO.insertDoTransferPickDet(htPickDet);
					
					
					//Coding for decrease qty in FromLoc in DOTransfer_Pick Table
					htPickCalc.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htPickCalc.put("DONO", map.get(IConstants.PODET_PONUM));
					htPickCalc.put("DOLNO", map.get(IConstants.PODET_POLNNO));
					htPickCalc.put("ITEM", map.get(IConstants.ITEM));
					htPickCalc.put("BATCH", map.get(IConstants.BATCH));
					htPickCalc.put("TOLOC", map.get(IConstants.LOC));
					
					boolean flagUpdate1= _DoTransferDetDAO.isExisitDoTransferPick(htPickCalc, "");
					if(flagUpdate1)
					{
						
						htPickCalc.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htPickCalc.put("DONO", map.get(IConstants.PODET_PONUM));
						htPickCalc.put("DOLNO", map.get(IConstants.PODET_POLNNO));
						htPickCalc.put("ITEM", map.get(IConstants.ITEM));
						htPickCalc.put("BATCH", map.get(IConstants.BATCH));
						htPickCalc.put("TOLOC", map.get(IConstants.LOC));
						StringBuffer sqlUpdateCalc = new StringBuffer(" SET ");
						sqlUpdateCalc.append(IDBConstants.PICKQTY + " = PICKQTY -'"
								+ map.get(IConstants.QTY) + "'");
						sqlUpdateCalc.append("," + IDBConstants.UPDATED_AT + " = '"
								+ dateUtils.getDateTime() + "'");
						
						boolean flagUpdate2 = _DoTransferDetDAO.updateDoTransferPickDet(sqlUpdateCalc.toString(), htPickCalc, "");
						
					}
							
					
				    ////Coding for decrease qty in FromLoc in DOTransfer_Pick Table end 
				}
				else
				{
					
					htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
					htPickDet.put("DOLNO", map.get(IConstants.PODET_POLNNO));
					htPickDet.put("ITEM", map.get(IConstants.ITEM));
					
					htPickDet.put("BATCH", map.get(IConstants.BATCH));
					htPickDet.put("TOLOC", map.get(IConstants.LOC2));
					
				
					
					StringBuffer sql1 = new StringBuffer(" SET ");
					sql1.append(IDBConstants.PICKQTY + " = PICKQTY +'"
							+ map.get(IConstants.QTY) + "'");
					sql1.append("," + IDBConstants.UPDATED_AT + " = '"
							+ dateUtils.getDateTime() + "'");
					
					flag = _DoTransferDetDAO.updateDoTransferPickDet(sql1.toString(), htPickDet, "");
					
					
					//Coding for decrease qty in FromLoc in DOTransfer_Pick Table
					htPickCalc.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htPickCalc.put("DONO", map.get(IConstants.PODET_PONUM));
					htPickCalc.put("DOLNO", map.get(IConstants.PODET_POLNNO));
					htPickCalc.put("ITEM", map.get(IConstants.ITEM));
					htPickCalc.put("BATCH", map.get(IConstants.BATCH));
					htPickCalc.put("TOLOC", map.get(IConstants.LOC));
					
					boolean flagUpdate1= _DoTransferDetDAO.isExisitDoTransferPick(htPickCalc, "");
					
					if( flagUpdate1){
						htPickCalc.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htPickCalc.put("DONO", map.get(IConstants.PODET_PONUM));
						htPickCalc.put("DOLNO", map.get(IConstants.PODET_POLNNO));
						htPickCalc.put("ITEM", map.get(IConstants.ITEM));
						htPickCalc.put("BATCH", map.get(IConstants.BATCH));
						htPickCalc.put("TOLOC", map.get(IConstants.LOC));
						StringBuffer sqlUpdateCalc = new StringBuffer(" SET ");
						sqlUpdateCalc.append(IDBConstants.PICKQTY + " = PICKQTY -'"
								+ map.get(IConstants.QTY) + "'");
						sqlUpdateCalc.append("," + IDBConstants.UPDATED_AT + " = '"
								+ dateUtils.getDateTime() + "'");
						
						boolean flagUpdate2 = _DoTransferDetDAO.updateDoTransferPickDet(sqlUpdateCalc.toString(), htPickCalc, "");
						
					}
				    ////Coding for decrease qty in FromLoc in DOTransfer_Pick Table end 
					
				}
				
			  ////coding for update or insert dotransfer_pick table end
				
			/*	//To Find Out <=0 Pickqty and Updat Pickqty=0 after above calc
					Hashtable htPickQtyZero = new Hashtable();
					htPickQtyZero.clear();
					htPickQtyZero.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htPickQtyZero.put("DONO", map.get(IConstants.PODET_PONUM));
					htPickQtyZero.put("DOLNO", map.get(IConstants.PODET_POLNNO));
					htPickQtyZero.put("ITEM", map.get(IConstants.ITEM));
					htPickQtyZero.put("BATCH", map.get(IConstants.BATCH));
					htPickCalc.put("TOLOC", map.get(IConstants.LOC));
					
					boolean isExistpickqtyzero=_DoTransferDetDAO.isExisitDoTransferPick(htPickCalc, "");
					
					if(isExistpickqtyzero){
					  int pickqty = 0;
					  String sqlUpdateQtyZero = "";
					  sqlUpdateQtyZero = "set PICKQTY= "+pickqty+"" ;
					  boolean pickqtyzero = _DoTransferDetDAO.updateDoTransferPickQtyZero(sqlUpdateQtyZero, htPickQtyZero, "");
					
					}
			//To Find Out <=0 Pickqty and Update Pickqty=0 after above calc end  */

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw new Exception("Pick Qty Exceeded the Order Qty to Pick");
		}
		
		return flag;
	}

	public boolean processDoTransferInvMst(Map map) throws Exception {

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
			
			
			flag = _InvMstDAO.isExisit(htInvMst, "");

			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY -'"
						+ map.get(IConstants.QTY) + "'");
				Hashtable htInvMstReduce = new Hashtable();
				htInvMstReduce.clear();
				htInvMstReduce.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMstReduce.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMstReduce.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMstReduce.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, "");

			} else {
				throw new Exception("Product not found in the location");
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
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));

			flag = _InvMstDAO.isExisit(htInvMst, "");
			
			//get expiredate for inventory update
			  
			   String  expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
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
							htInsertInvParentBom.put("QTY",  map.get("INV_QTY1"));
										
							invResult  = invResult  && invMstDAO.insertInvMst(htInsertInvParentBom);
							
							if (!invResult) {
								throw new Exception(
										"Unable to process kitting outbound transfer picking location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    } /*else {
				throw new Exception("Unable to process kitting outbound transfer picking location transfer, Location not found");
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
			htMovhis.put("DIRTYPE", TransactionConstants.OB_TRANSFER_PIC_OUT);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			htMovhis.put(IDBConstants.POHDR_JOB_NUM, map
					.get(IConstants.JOB_NUM));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map
					.get(IConstants.PODET_PONUM));
			htMovhis.put("MOVTID", "OUT");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovhis.put(IDBConstants.CREATED_BY, map
					.get(IConstants.LOGIN_USER));
			htMovhis.put(IDBConstants.TRAN_DATE,  dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put(IDBConstants.REMARKS,  _strUtils.InsertQuotes((String)map.get(IConstants.REMARKS)));

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

			htMovhis.put("DIRTYPE", TransactionConstants.OB_TRANSFER_PIC_IN);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("BATNO", map.get(IConstants.BATCH));
			htMovhis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htMovhis.put("MOVTID", "IN");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htMovhis.put(IDBConstants.POHDR_JOB_NUM, map
					.get(IConstants.JOB_NUM));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map
					.get(IConstants.PODET_PONUM));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));

			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put(IDBConstants.REMARKS, _strUtils.InsertQuotes((String)map.get(IConstants.REMARKS)));

			flag = movHisDao.insertIntoMovHis(htMovhis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		
		return flag;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

}