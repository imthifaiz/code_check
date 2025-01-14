package com.track.tran;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.POUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsProductReceiveMaterial implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsReceiveMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsReceiveMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        POUtil poutil = new POUtil();
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

	public WmsProductReceiveMaterial() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			
			String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
			if(!nonstocktype.equalsIgnoreCase("Y"))	
		    {
				flag = processInvMst(m);
		    }else{
		    	flag=true;
		    }
			if (flag) {
				flag = processPodetForReceive(m);
			}
			if (flag) {

				flag = processMovHis_IN(m);

			}
		   
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;

		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
            if(!map.get(IConstants.INV_BATCH).equals("NOBATCH")){
            	
	           
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
	
				double inqty = Double.valueOf((String)map.get(IConstants.INV_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
					htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
					htInvMst.put(IDBConstants.QTY,String.valueOf(inqty));
					htInvMst.put(IDBConstants.USERFLD3, map.get(IConstants.INV_EXP_DATE));
					//htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.RECVDATE).toString().replaceAll("/", "") + "000000");	
					htInvMst.put(IDBConstants.STATUS, "");
	
					flag = _InvMstDAO.insertInvMst(htInvMst);
//				}

			}else//if NOBATCH
			{
				double inqty = Double.valueOf((String)map.get(IConstants.INV_QTY)) * Double.valueOf((String)map.get("UOMQTY"));
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));	
				
				flag = _InvMstDAO.isExisit(htInvMst, "");
				
				if (flag  ){
	
					StringBuffer sql = new StringBuffer(" SET ");
					sql.append(IDBConstants.QTY + " = QTY +'"
							+ inqty + "'");
					sql.append("," + IDBConstants.USERFLD3 + " = '"
							+ map.get(IConstants.INV_EXP_DATE) + "'");
					sql.append("," + IDBConstants.USERFLD4 + " = '"
							+ map.get(IConstants.INV_BATCH) + "'");
					sql.append("," + IDBConstants.UPDATED_AT + " = '"
							+ dateUtils.getDateTime() + "'");
					sql.append("," + IDBConstants.EXPIREDATE + " = '"
							+ map.get(IConstants.EXPIREDATE) + "'");
					flag = _InvMstDAO.update(sql.toString(), htInvMst, "");
	
				}  else {
					htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
					htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
					htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
					htInvMst.put(IDBConstants.USERFLD3, map.get(IConstants.INV_EXP_DATE));
					//htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.RECVDATE).toString().replaceAll("/", "") + "000000");
	
					htInvMst.put(IDBConstants.STATUS, "");
	
					flag = _InvMstDAO.insertInvMst(htInvMst);
				}
			}
                     

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	
	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htMovhis = new Hashtable();
			htMovhis.clear();
			htMovhis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htMovhis.put("DIRTYPE", TransactionConstants.ORD_RECV);
			htMovhis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htMovhis.put("MOVTID", "IN");
			htMovhis.put("RECID", "");
			htMovhis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htMovhis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htMovhis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htMovhis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htMovhis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htMovhis.put("BATNO", map.get(IConstants.INV_BATCH));
			htMovhis.put("QTY", map.get(IConstants.INV_QTY));
			if((String)map.get(IConstants.UOM)!=null)
			htMovhis.put(IDBConstants.UOM, map.get(IConstants.UOM));       
//			htMovhis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htMovhis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htMovhis.put("REMARKS", su.InsertQuotes((String)map.get(IConstants.REMARKS))+","+map.get(IConstants.GRNO));

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
	public boolean processPodetForReceive(Map map) throws Exception {

		boolean flag = false;
		try {
			// update receive qty in podet
			PoDetDAO _PoDetDAO = new PoDetDAO((String) map.get("PLANT"));
			PoHdrDAO _PoHdrDAO = new PoHdrDAO((String) map.get("PLANT"));
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();

			_PoDetDAO.setmLogger(mLogger);
			_PoHdrDAO.setmLogger(mLogger);
			_RecvDetDAO.setmLogger(mLogger);
			poutil.setmLogger(mLogger);
			Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");

			// getQty from podet
			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("ICRNO", map.get("PONO"));
			htCondiPoDet.put("LNNO", map.get("POLNNO"));
			htCondiPoDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));

			query.append("isnull(QTY,0) as QTY");
			query.append(",isnull(QTYRC,0) as QTYRC,isnull(UNITCOST,0) as UNITCOST");

			Map mQty = _PoDetDAO.selectProductReceiceRow(query.toString(), htCondiPoDet);

			double ordQty = Double.parseDouble((String) mQty.get("QTY"));
			double rcQty = Double.parseDouble((String) mQty.get("QTYRC"));

			double tranQty = Double.parseDouble((String) map.get(IConstants.INV_QTY));
			double sumqty = rcQty +tranQty;
			sumqty = StrUtils.RoundDB(sumqty,IConstants.DECIMALPTS );

			String queryPoDet = "";
			String queryPoHdr = "";

			if (ordQty == sumqty) {
				queryPoDet = " set QTYRC= isNull(QTYRC,0) + "
						+ map.get(IConstants.INV_QTY) + " , LNSTAT='C' ";

			} else {
				queryPoDet = " set QTYRC= isNull(QTYRC,0) + "
						+ map.get(IConstants.INV_QTY) + " , LNSTAT='O' ";

			}
			String extraCond = " AND  QTY >= isNull(QTYRC,0) + "+ map.get(IConstants.INV_QTY);
			flag = _PoDetDAO.updateProductReceive(queryPoDet, htCondiPoDet, extraCond);
			if (flag) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get("PLANT"));
				htCondiPoHdr.put("ICRNO", map.get("PONO"));

				flag = _PoDetDAO.isExisitProductReceive(htCondiPoHdr, "LNSTAT in ('O','N')");

				if (flag)
					queryPoHdr = "set STATUS='O',RECEIVE_STATUS='PARTIALLY PROCESSED' ";
				else
					queryPoHdr = "set STATUS='C',RECEIVE_STATUS='PROCESSED' ";

				flag = _PoHdrDAO.updateProductHdr(queryPoHdr, htCondiPoHdr, "");

			}
			
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.PODET_PONUM));
			htRecvDet.put("LNNO", map.get(IConstants.PODET_POLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, su.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
			
			htRecvDet.put(IDBConstants.CURRENCYID,poutil.getProductCurrencyID((String)map.get(IConstants.PLANT), (String)map.get(IConstants.PODET_PONUM)) );
			Hashtable pohashtable = new Hashtable();
			pohashtable.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			pohashtable.put(IDBConstants.PODET_PONUM, map.get(IConstants.PODET_PONUM));
			pohashtable.put("POLNNO", map.get(IConstants.PODET_POLNNO));
			pohashtable.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			
			htRecvDet.put("UNITCOST", map.get("UNITCOST"));
			htRecvDet.put("MANUFACTURER","");
			
			htRecvDet.put("REMARK",su.InsertQuotes((String) map.get(IConstants.REMARKS)));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
		    htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "IB");
		    htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    htRecvDet.put("EXPIRYDAT", map.get(IConstants.EXPIREDATE));
		    htRecvDet.put(IConstants.GRNO, map.get(IConstants.GRNO));//Ravindra
		    
		    htRecvDet.put("RECQTY", String.valueOf(map.get(IConstants.RECV_QTY))); //new added by imthi
			String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
			if(nonstocktype.equalsIgnoreCase("Y"))	
		    {
				flag = _RecvDetDAO.insertRecvDet(htRecvDet);
		    }
			else
			{
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String extCond = "";			
				extCond = "QTY > 0";			
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMstDes("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.RECV_QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
//					htRecvDet.put("RECQTY", String.valueOf(adjustedQuantity)); //commented by imthi
					htRecvDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			quantityToAdjust -= adjustedQuantity;
				}
			}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

}