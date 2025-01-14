package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsPicking implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsPicking_PRINTPLANTMASTERLOG;
//	private boolean printInfo = MLoggerConstant.WmsPicking_PRINTPLANTMASTERINFO;

	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        DOUtil doutil = new DOUtil();
        TempUtil _TempUtil = new  TempUtil();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsPicking() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			flag = processInvAdd(m);
			

			if (flag) {
				flag = processDodetForPick(m);
		        String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
				flag = processInvMst(m);
				}
			 }else{
			    	flag=true;
			}
			
			if (flag) {

				{
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
			
			String type = 	m.get("TYPE").toString();
			if(flag && type.equalsIgnoreCase("OBRANDOM"))
			{
				flag = processTempRemove(m);
			}	
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processDodetForPick(Map map) throws Exception {
		//---Modified by Bruhan on March 04 2014, Description: Insert Container as 'NOCONTAINER'
        String ExpiryDate="";
		boolean flag = false;
		try {
			 DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			_DoHdrDAO.setmLogger(mLogger);
                      //   DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
                       // _DoTransferHdrDAO.setmLogger(mLogger);
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");
			// getQty from podet

			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("dono", map.get("PONO"));
			htCondiPoDet.put("dolnno", map.get("POLNNO"));
			htCondiPoDet.put(IDBConstants.ITEM,  map.get(IConstants.ITEM));

			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");

			Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);

			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double qtyPick = Double.parseDouble((String) mQty.get("qtyPick"));

			double tranQty = Double.parseDouble((String) map.get(IConstants.QTY));
			double sumqty = qtyPick + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
			// Added By Samatha extracond to Controll the Pick/Issue Qty Excced
			// the Order Qty Aug 24 1010
			String extraCond = " AND  QtyOr >= isNull(qtyPick,0) + "+ map.get(IConstants.QTY);
			if (ordQty == sumqty) {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='C',Lnstat='O' ";

			} else {
				queryPoDet = "set qtyPick= isNull(qtyPick,0) + "
						+ map.get(IConstants.QTY) + " , PickStatus='O',Lnstat='O' ";

			}

			flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);

			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("dono", map.get("PONO"));

				flag = _DoDetDAO.isExisit(htCondiPoHdr,
						"pickStatus in ('O','N')");
				if (flag)
					queryPoHdr = "set  STATUS='O',PickStaus='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
				else
					queryPoHdr = "set  STATUS='O',PickStaus='C',ORDER_STATUS='PROCESSED' ";

				flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				
			    String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
		        	InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					Hashtable htInvMstExpDate = new Hashtable();
					htInvMstExpDate.clear();
					htInvMstExpDate.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htInvMstExpDate.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htInvMstExpDate.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMstExpDate.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
					
					
					ExpiryDate= _InvMstDAO.getInvExpireDate( (String)map.get(IConstants.PLANT), 
							(String)map.get(IConstants.ITEM),(String) map.get(IConstants.LOC),
							(String)map.get(IConstants.BATCH));
			
			    } 
				boolean pickdet = false;
				Hashtable htPickDet = new Hashtable();
				Hashtable htInvMst = new Hashtable();				
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				String extCond = "";
				String chkqt = "";
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
					extCond = "QTY >= " + map.get(IConstants.QTY);
					chkqt="1";
				}else{
					extCond = "QTY > 0";
				}
				InvMstDAO _InvMstDAO = new InvMstDAO();
				ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC2));
				htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");
				if(chkqt=="1")
					htInvMst.remove(IDBConstants.INVID);
				
				htPickDet.clear();						
				htPickDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htPickDet.put("DONO", map.get(IConstants.PODET_PONUM));
				htPickDet.put("DOLNO", map.get(IConstants.PODET_POLNNO));
				htPickDet.put(IDBConstants.CUSTOMER_NAME, su.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htPickDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
				htPickDet.put("BATCH", map.get(IConstants.BATCH));
				htPickDet.put(IDBConstants.LOC, map.get(IConstants.LOC2));
				htPickDet.put("LOC1", map.get(IConstants.LOC));
				htPickDet.put("ORDQTY", map.get(IConstants.ORD_QTY));				
				htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htPickDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
				htPickDet.put("REMARK", map.get(IConstants.INV_EXP_DATE));
				htPickDet.put("CONTAINER", "NOCONTAINER");
				htPickDet.put("ExpiryDat", ExpiryDate);
				Hashtable htCurrency = new Hashtable();
				htCurrency.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htCurrency.put("DONO", map.get(IConstants.PODET_PONUM));
				String currencyid = doutil.getDohdrcol(htCurrency, "CURRENCYID");
				htCurrency.put("DOLNNO", map.get(IConstants.PODET_POLNNO));
				htPickDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			    String unitprice = doutil.getDodetcol(htCurrency, "UNITPRICE");
				htPickDet.put(IDBConstants.CURRENCYID, currencyid);
				htPickDet.put("UNITPRICE", unitprice);
				htPickDet.put("STATUS", "O");
				htPickDet.put(IDBConstants.ISSUEDATE,  map.get(IConstants.ISSUEDATE));
				htPickDet.put(IDBConstants.INVOICENO, map.get(IConstants.INVOICENO));
				
		        if(nonstocktype.equals("Y"))	
			    {
		        	htPickDet.put("PICKQTY", String.valueOf(map.get(IConstants.QTY)));
		        	pickdet = _DoDetDAO.insertPickDet(htPickDet);
			    }
		        else
		        {
				ArrayList shalStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					if (!shalStock.isEmpty()) {
							
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
					Iterator iterStock = alStock.iterator();
					Iterator shiterStock = shalStock.iterator();
					String ShId ="";
					while(shiterStock.hasNext()) {
						Map mapIterStock = (Map)shiterStock.next();
						ShId = (String)mapIterStock.get(IDBConstants.INVID);
					} 
					while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
						Map mapIterStock = (Map)iterStock.next();
						double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
						double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
						
						htPickDet.put("PICKQTY", String.valueOf(adjustedQuantity));						
						htPickDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));						
						htPickDet.put(IDBConstants.INVSHID, ShId);
						pickdet = _DoDetDAO.insertPickDet(htPickDet);
						quantityToAdjust -= adjustedQuantity;
					}
				}
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
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			String extCond = "";			
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				extCond = "QTY >= " + inqty;
			}else{
				extCond = "QTY > 0";
			}
			flag = _InvMstDAO.isExisit(htInvMst, extCond);
			if (flag) {
				//	Get details in ascending order of CRAT for that batch
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
					htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				}
				ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
				if (!alStock.isEmpty()) {
					double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
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
						//extCond = "AND QTY >='" + map.get(IConstants.QTY) + "'";
						extCond = "";
						
						flag = _InvMstDAO.update(sql1.toString(), htInvMstReduce, extCond);
						if (!flag) {
							throw new Exception("Could not update");
						}
						quantityToAdjust -= adjustedQuantity;
					}
				}
				
			} else {
				throw new Exception("Product not found in the location");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception("Not Enough Qty available for Batch :"+(String)map.get(IConstants.BATCH));
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
			htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");
			flag = _InvMstDAO.isExisit(htInvMst, "");
			
			//get expiredate for inventory update
			  
			   String expiredate= _InvMstDAO.getInvExpiryDate((String)map.get(IConstants.PLANT), (String)map.get(IConstants.ITEM),(String)map.get(IConstants.LOC),(String)map.get(IConstants.BATCH));
			//get expiredate for inventory update end
			   double inqty = Double.valueOf((String)map.get(IConstants.QTY)) * Double.valueOf((String)map.get("UOMQTY"));
			if (flag) {
			
				StringBuffer sql1 = new StringBuffer(" SET ");
				sql1.append(IDBConstants.QTY + " = QTY +'"
						+ inqty + "'");
				sql1.append("," +IDBConstants.EXPIREDATE + " = '"
						+ expiredate + "'");
				sql1.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");

				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");

			} else {
				htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
				/*htInvMst.put(IDBConstants.USERFLD3, _InvMstDAO.getExpireDate(
						(String) map.get(IConstants.PLANT), (String) map
								.get(IConstants.ITEM), (String) map
								.get(IConstants.BATCH)));*/
				htInvMst.put(IDBConstants.EXPIREDATE, expiredate);
				//htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_AT,  map.get(IConstants.ISSUEDATE).toString().replaceAll("/", "") + "000000");

				flag = _InvMstDAO.insertInvMst(htInvMst);
                                
			       //NO EXPIRYDT

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
			//flag = _BomDAO.isExisit(htBomMst," PARENT_PRODUCT_LOC ='"+map.get(IConstants.LOC)+"' OR PARENT_PRODUCT_LOC = 'SHIPPINGAREA_"+map.get(IConstants.LOC)+"'");
						
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
				
				bomupdateflag= _BomDAO.update(sql1.toString(),htBomUpdateMst," ");
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
										"Unable to process kitting outbound picking location transfer ,Save inventory parent product failed");
							} 
						
						}
						
				}
				
			    }/* else {
				throw new Exception("Unable to process kitting outbound picking location transfer, Location not found");
			}
		/*}
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
	   Oct 14 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
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
			htRecvHis.put("DIRTYPE", TransactionConstants.PIC_TRAN_OUT);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, "-" + map.get(IConstants.QTY));
			if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			if((String)map.get(IConstants.UOM)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
//			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.INV_EXP_DATE)));
			
			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	/* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To change TRAN_DATE as Transaction date
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
			htRecvHis.put("DIRTYPE", TransactionConstants.PIC_TRAN_IN);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			if((String)map.get(IConstants.CUSTOMER_CODE)!=null)
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			if((String)map.get(IConstants.UOM)!=null)
				htRecvHis.put(IDBConstants.UOM, map.get(IConstants.UOM));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC2));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
//			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.INV_EXP_DATE));

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	
	private boolean processTempRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			
			
			Hashtable htdeletetemp = new Hashtable();
			htdeletetemp.put("PLANT", map.get(IConstants.PLANT));
			htdeletetemp.put("ORDERNO", map.get(IConstants.PODET_PONUM));
			htdeletetemp.put("ITEM", map.get(IConstants.ITEM));
			htdeletetemp.put("LOC", map.get(IConstants.LOC));
			htdeletetemp.put("BATCH", map.get(IConstants.BATCH));
			
			double qty = Double.parseDouble((String)map.get(IConstants.PICKQTY));
			
			
			if(qty<0){
			flag = _TempUtil.deletetemp(htdeletetemp);}
			else{
				flag = true;
			}
			
			
			
			
			if(flag)
			{

			} else {
				throw new Exception("Error in deleting line in temp table");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}	

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

}